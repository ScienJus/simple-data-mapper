package com.scienjus.base.dao;

import com.scienjus.base.domain.BaseDomain;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XieEnlong
 * @date 2015/8/19.
 */
public class SimpleDataMapper<E extends BaseDomain> {

    @Autowired
    private DataSource dataSource;


    public void insertEntity(E domain) {
        try (Connection con = dataSource.getConnection();) {
            try (PreparedStatement statement = buildInsertStatement(con, domain.toString(), domain.getChangeFields())) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
//                ResultSet rs = statement.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
            } catch (SQLException e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }
    }

    public void updateEntity(E domain) {
        try (Connection con = dataSource.getConnection();) {
            try (PreparedStatement statement = buildUpdateStatement(con, domain.toString(), domain)) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }
    }

    public void deleteEntity(E domain) {
        try (Connection con = dataSource.getConnection();) {
            try (PreparedStatement statement = buildDeleteStatement(con, domain.toString(), domain)) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }
    }

    pu

    private PreparedStatement buildInsertStatement(Connection con, String tableName, Map<String, Object> changeFields) throws SQLException {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String field : changeFields.keySet()) {
            fields.append(field).append(",");
            values.append("?").append(",");
        }
        fields.deleteCharAt(fields.length() - 1);
        values.deleteCharAt(values.length() - 1);

        String sql = String.format("insert into %s (%s) values (%s)", tableName, fields.toString(), values.toString());
        PreparedStatement statement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        int i = 1;
        for (Object value : changeFields.values()) {
            if (value instanceof Boolean) {
                statement.setBoolean(i++, (Boolean) value);
            } else {
                statement.setString(i++, value.toString());
            }
        }
        return statement;
    }

    private PreparedStatement buildUpdateStatement(Connection con, String tableName, Map<String, Method> primaryKeyGetMethods, E domain) throws Exception {
        LinkedHashMap<String, Object> changeFields = domain.getChangeFields();

        List<String> values = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (String primaryKey : primaryKeyGetMethods.keySet()) {
            keys.add(primaryKey.concat(" = ?"));
            changeFields.remove(primaryKey);
        }

        for (String field : changeFields.keySet()) {
            values.add(field.concat(" = ?"));
        }

        String sql = String.format("update %s set %s where %s", tableName, StringUtils.join(keys, ", "), StringUtils.join(keys, " and "));
        PreparedStatement statement = con.prepareStatement(sql);
        int i = 1;
        for (Object value : changeFields.values()) {
            if (value instanceof Boolean) {
                statement.setBoolean(i++, (Boolean) value);
            } else {
                statement.setString(i++, value.toString());
            }
        }
        for (Method getMethod : primaryKeyGetMethods.values()) {
            Object value = getMethod.invoke(domain);
            if (value instanceof Boolean) {
                statement.setBoolean(i++, (Boolean) value);
            } else {
                statement.setString(i++, value.toString());
            }
        }
        return statement;
    }

    private PreparedStatement buildDeleteStatement(Connection con, String tableName, Map<String, Method> primaryKeyGetMethods, E domain) throws Exception {
        List<String> keys = new ArrayList<>();
        for (String primaryKey : primaryKeyGetMethods.keySet()) {
            keys.add(primaryKey.concat(" = ?"));
        }

        String sql = String.format("update %s where %s", tableName, StringUtils.join(keys, " and "));
        PreparedStatement statement = con.prepareStatement(sql);

        int i = 1;
        for (Method getMethod : primaryKeyGetMethods.values()) {
            Object value = getMethod.invoke(domain);
            if (value instanceof Boolean) {
                statement.setBoolean(i++, (Boolean) value);
            } else {
                statement.setString(i++, value.toString());
            }
        }
        return statement;
    }
}
