package com.scienjus.base.dao;

import com.scienjus.base.domain.BaseDomain;
import com.scienjus.base.util.TableUtil;
import com.sun.deploy.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XieEnlong
 * @date 2015/8/19.
 */
public abstract class SimpleDataMapper<E extends BaseDomain> {

    private static final Logger LOGGER = Logger.getLogger(SimpleDataMapper.class);

    @Autowired
    protected DataSource dataSource;

    public void insertEntity(E domain) {
        if (TableUtil.isAutoKey(this.getEntityClass())) {
            insertAutoKeyEntity(domain);
            return;
        }
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = buildInsertStatement(
                    con,
                    TableUtil.getTableName(this.getEntityClass()),
                    domain.getChangeFields())) {
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

    public void insertAutoKeyEntity(E domain) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = buildAutoKeyInsertStatement(
                    con,
                    TableUtil.getTableName(this.getEntityClass()),
                    domain.getChangeFields())) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    Method idSetter = TableUtil.getAutoKeySetter(this.getEntityClass());
                    idSetter.invoke(domain, rs.getLong(1));
                }
            } catch (Exception e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }

    }

    public void updateEntity(E domain) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = buildUpdateStatement(
                    con,
                    TableUtil.getTableName(this.getEntityClass()),
                    TableUtil.getPrimaryKeyGetters(this.getEntityClass()),
                    domain)) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }
    }

    public void deleteEntity(E domain) {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = buildDeleteStatement(
                    con,
                    TableUtil.getTableName(this.getEntityClass()),
                    TableUtil.getPrimaryKeyGetters(this.getEntityClass()),
                    domain)) {
                con.setAutoCommit(false);
                statement.executeUpdate();
                con.commit();
            } catch (Exception e) {
                con.rollback();
            } finally {
                domain.clearChangeFields();
            }
        } catch (SQLException e) {
            //...
        }
    }

    public E getEntity(Object... args) {
        String sql = TableUtil.getSelectSQL(this.getEntityClass()).concat(TableUtil.getIdSelect(this.getEntityClass()));
        LOGGER.info(sql);
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            int i = 1;
            for (Object arg : args) {
                if (arg instanceof Boolean) {
                    statement.setBoolean(i++, (Boolean) arg);
                } else {
                    statement.setString(i++, arg.toString());
                }
            }
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return this.convertEntity(rs);
                }
            }
        } catch (SQLException e) {
            //...
        }
        return null;
    }

    public List<E> findEntity(String where, Object... args) {
        String sql = TableUtil.getSelectSQL(this.getEntityClass()).concat(where);
        LOGGER.info(sql);
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {
            int i = 1;
            for (Object arg : args) {
                if (arg instanceof Boolean) {
                    statement.setBoolean(i++, (Boolean) arg);
                } else {
                    statement.setString(i++, arg.toString());
                }
            }
            List<E> list = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    list.add(this.convertEntity(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            //...
        }
        return null;
    }

    protected abstract E convertEntity(ResultSet rs) throws SQLException;

    protected abstract Class getEntityClass();

    protected PreparedStatement buildInsertStatement(Connection con, String tableName,
                                                     Map<String, Object> changeFields) throws SQLException {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String field : changeFields.keySet()) {
            fields.append(field).append(",");
            values.append("?").append(",");
        }
        fields.deleteCharAt(fields.length() - 1);
        values.deleteCharAt(values.length() - 1);

        String sql = String.format("insert into %s (%s) values (%s)", tableName, fields.toString(), values.toString());

        LOGGER.info(sql);

        PreparedStatement statement = con.prepareStatement(sql);
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

    protected PreparedStatement buildAutoKeyInsertStatement(Connection con, String tableName,
                                                     Map<String, Object> changeFields) throws SQLException {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String field : changeFields.keySet()) {
            fields.append(field).append(",");
            values.append("?").append(",");
        }
        fields.deleteCharAt(fields.length() - 1);
        values.deleteCharAt(values.length() - 1);

        String sql = String.format("insert into %s (%s) values (%s)", tableName, fields.toString(), values.toString());

        LOGGER.info(sql);

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

    private PreparedStatement buildUpdateStatement(Connection con, String tableName,
                                                   Map<String, Method> primaryKeyGetMethods, E domain) throws Exception {
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

        String sql = String.format("update %s set %s where %s", tableName, StringUtils.join(values, ", "), StringUtils.join(keys, " and "));

        LOGGER.info(sql);

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

    private PreparedStatement buildDeleteStatement(Connection con, String tableName,
                                                   Map<String, Method> primaryKeyGetMethods, E domain) throws Exception {
        List<String> keys = new ArrayList<>();
        for (String primaryKey : primaryKeyGetMethods.keySet()) {
            keys.add(primaryKey.concat(" = ?"));
        }

        String sql = String.format("delete from %s where %s", tableName, StringUtils.join(keys, " and "));

        LOGGER.info(sql);

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
