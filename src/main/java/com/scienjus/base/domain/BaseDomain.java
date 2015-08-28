package com.scienjus.base.domain;

import java.util.LinkedHashMap;

/**
 * @author XieEnlong
 * @date 2015/8/19.
 */
public class BaseDomain {

    protected LinkedHashMap<String, Object> changeFields = new LinkedHashMap<>();

    public LinkedHashMap<String, Object> getChangeFields() {
        return changeFields;
    }

    public void setChangeFields(LinkedHashMap<String, Object> changeFields) {
        this.changeFields = changeFields;
    }

    public void clearChangeFields() {
        this.changeFields.clear();
    }
}
