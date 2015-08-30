package com.scienjus.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scienjus.generator.config.DataSource;
import com.scienjus.generator.config.Generator;
import com.scienjus.generator.config.Packages;
import com.scienjus.generator.meta.FieldMeta;
import com.scienjus.generator.meta.TableMeta;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
public class DataMapperGenerator {

    private static final String GENERATOR_CONFIG_PATH = "/generator.json";

    public static void main(String[] args) throws Exception {
        //读取配置文件
        ObjectMapper mapper = new ObjectMapper();
        Generator generator = mapper.readValue(DataMapperGenerator.class.getResource(GENERATOR_CONFIG_PATH), Generator.class);
        //配置数据库
        DataSource dataSource = generator.getDataSource();
        Class.forName(dataSource.getDriver());
        Connection con = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        //映射表
        List<TableMeta> tables = new ArrayList<>();
        for (String table : generator.getTables()) {
            TableMeta tableMeta = new TableMeta(table);
            DatabaseMetaData dbmd = con.getMetaData();
            //注释
            ResultSet rs = dbmd.getTables(null, "%", table, new String[]{"TABLE"});
            if (rs.next()) {
                tableMeta.setComment(rs.getString("REMARKS"));
            }
            rs.close();
            //字段
            rs = dbmd.getColumns(null, null, table, null);
            List<FieldMeta> fields = new ArrayList<>();
            while (rs.next()) {
                fields.add(new FieldMeta(
                        rs.getString("COLUMN_NAME"),
                        rs.getString("REMARKS"),
                        rs.getBoolean("IS_AUTOINCREMENT"),
                        rs.getInt("DATA_TYPE")));
            }
            tableMeta.setFields(fields);
            //主键
            List<FieldMeta> primaryKeys = new ArrayList<>();
            for (FieldMeta field : fields) {
                if (field.isAutoKey()) {
                    primaryKeys.add(field);
                }
            }
            tableMeta.setPrimaryKeys(primaryKeys);
            rs.close();
            tables.add(tableMeta);
        }
        //生成代码
        Packages packages = generator.getPackages();
        for (TableMeta table : tables) {
            createDomain(table, packages.getDomain(), generator.getPath());
            createDao(table, packages.getDao(), generator.getPath());
        }
    }

    private static void createDao(TableMeta table, String aPackage, String path) throws IOException {
        String dirPath = path + "/" + aPackage.replace(".", "/");
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dirPath + "/" + getClassName(table) + "Dao.java")));
        //声明包
        writer.format("package %s;\n", aPackage);
        writer.println();

        //声明类
        writer.format("public interface %sDao {\n", getClassName(table));
        writer.println();
        //增删改
        writer.format("\tpublic void insert(%s %s);\n", getClassName(table), getClassFieldName(table));
        writer.println();
        writer.format("\tpublic void update(%s %s);\n", getClassName(table), getClassFieldName(table));
        writer.println();
        writer.format("\tpublic void delete(%s %s);\n", getClassName(table), getClassFieldName(table));
        writer.println();
        //主键查询
        writer.format("\tpublic %s get(%s); \n", getClassName(table), getPrimaryKeyFields(table));
        writer.println();
        //条件查询
        writer.format("\tpublic List<%s> find(String where, Object... args);\n", getClassName(table));
        writer.println();
        writer.println("}");
        writer.close();
    }

    private static String getPrimaryKeyFields(TableMeta table) {
        StringBuilder primaryKeys = new StringBuilder();
        for (FieldMeta field : table.getPrimaryKeys()) {
            primaryKeys.append(getFieldType(field)).append(" ").append(getFieldName(field)).append(", ");
        }
        if (primaryKeys.length() > 0) {
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
        }
        return primaryKeys.toString();
    }

    private static String getClassFieldName(TableMeta table) {
        String className = getClassName(table);
        char[] chars = className.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);

    }

    private static void createDomain(TableMeta table, String aPackage, String path) throws IOException {
        String dirPath = path + "/" + aPackage.replace(".", "/");
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dirPath + "/" + getClassName(table) + ".java")));
        //声明包
        writer.format("package %s;\n", aPackage);
        writer.println();
        //表注释
        writer.format("// %s\n", table.getComment());
        //注解
        writer.format("@Table(name = \"%s\", keys = {%s})\n", table.getName(), getPrimaryKeys(table));
        //声明类
        writer.format("public class %s extends BaseDomain {\n", getClassName(table));
        for (FieldMeta field : table.getFields()) {
            //声明成员变量
            writer.format("\t// %s\n", field.getComment());
            writer.format("\tprivate %s %s;\n", getFieldType(field), getFieldName(field));
        }
        writer.println();
        for (FieldMeta field : table.getFields()) {
            //声明get方法
            writer.format("\tpublic %s get%s() {\n", getFieldType(field), getFieldMethodName(field));
            writer.format("\t\treturn this.%s;\n", getFieldName(field));
            writer.format("\t}\n");
            writer.println();
            //声明set方法
            writer.format("\tpublic void set%s(%s %s) {\n", getFieldMethodName(field), getFieldType(field), getFieldName(field));
            writer.format("\t\tthis.%s = %s;\n", getFieldName(field), getFieldName(field));
            writer.format("\t}\n");
            writer.println();

        }
        writer.println("}");
        writer.close();
    }

    private static String getFieldMethodName(FieldMeta field) {
        String fieldName = getFieldName(field);
        char[] chars = fieldName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    private static String getPrimaryKeys(TableMeta table) {
        StringBuilder primaryKeys = new StringBuilder();
        for (FieldMeta field : table.getPrimaryKeys()) {
            primaryKeys.append("\"").append(field.getName()).append("\", ");
        }
        if (primaryKeys.length() > 0) {
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
        }
        return primaryKeys.toString();
    }

    private static String getFieldName(FieldMeta field) {
        String[] temp = field.getName().split("_");
        StringBuilder fieldName = new StringBuilder(temp[0]);
        for (int i = 1; i < temp.length; i++) {
            char[] chars = temp[i].toCharArray();
            chars[0]=Character.toUpperCase(chars[0]);
            temp[i] = new String(chars);
            fieldName.append(temp[i]);
        }
        return fieldName.toString();
    }

    private static String getFieldType(FieldMeta field) {
        switch (field.getType()) {
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                return String.class.getSimpleName();
            case Types.SMALLINT:
            case Types.TINYINT:
                return int.class.getSimpleName();
            case Types.INTEGER:
                return long.class.getSimpleName();
            case Types.BIT:
            case Types.BOOLEAN:
                return boolean.class.getSimpleName();
            case Types.BIGINT:
                return long.class.getSimpleName();
            case Types.FLOAT:
                return float.class.getSimpleName();
            case Types.BLOB:
                return Blob.class.getSimpleName();
            case Types.DATE:
            case Types.TIMESTAMP:
                return Timestamp.class.getSimpleName();
            case Types.LONGVARBINARY:
            case Types.BINARY:
                return InputStream.class.getSimpleName();
            case Types.DATALINK:
                return URL.class.getSimpleName();
            case Types.DOUBLE:
            case Types.REAL:
                return double.class.getSimpleName();
            default:
                return null;
        }
    }

    private static String getClassName(TableMeta table) {
        String[] temp = table.getName().toLowerCase().split("_");
        StringBuilder className = new StringBuilder();
        for (int i = 0; i < temp.length; i++) {
            char[] chars = temp[i].toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            temp[i] = new String(chars);
            className.append(temp[i]);
        }
        return className.toString();
    }
}
