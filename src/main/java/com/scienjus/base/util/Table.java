package com.scienjus.base.util;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
public @interface Table {

    public String name();

    public String[] keys();

    public String selectSQL();
}
