package com.scienjus.base.util;

import java.lang.annotation.*;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

    public String name();

    public String[] keys();

    public String selectSQL();

    public boolean isAutoKey() default false;
}
