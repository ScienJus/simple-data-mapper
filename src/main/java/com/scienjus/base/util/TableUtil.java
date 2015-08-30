package com.scienjus.base.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XieEnlong
 * @date 2015/8/27.
 */
public class TableUtil {

    public static final Map<Class, String> TABLE_NAME_MAP = new HashMap<>();
    public static final Map<Class, Map<String, Method>> PRIMARY_KEY_GETTERS_MAP = new HashMap<>();
    public static final Map<Class, String> ID_SELECT_MAP = new HashMap<>();
    public static final Map<Class, String> SELECT_SQL_MAP = new HashMap<>();

    public static String getTableName(Class clazz) {
        return TABLE_NAME_MAP.get(clazz);
    }

    public static Map<String, Method> getPrimaryKeyGetters(Class clazz) {
        return PRIMARY_KEY_GETTERS_MAP.get(clazz);
    }

    public static String getIdSelect(Class clazz) {
        return ID_SELECT_MAP.get(clazz);
    }

    public static String getSelectSQL(Class clazz) {
        return SELECT_SQL_MAP.get(clazz);
    }

    public static void setTableName(Class clazz, String tableName) {
        TABLE_NAME_MAP.put(clazz, tableName);
    }

    public static void setPrimaryKeyGetters(Class clazz, Map<String, Method> primaryKeyGetMethods) {
        PRIMARY_KEY_GETTERS_MAP.put(clazz, primaryKeyGetMethods);
    }

    public static void setIdSelect(Class clazz, String tableName) {
        ID_SELECT_MAP.put(clazz, tableName);
    }

    public static void setSelectSQL(Class clazz, String tableName) {
        SELECT_SQL_MAP.put(clazz, tableName);
    }

}
