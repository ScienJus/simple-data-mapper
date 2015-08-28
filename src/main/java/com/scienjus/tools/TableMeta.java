package com.scienjus.tools;

import java.util.List;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
public class TableMeta {

    private String name;
    private String comment;
    private List<FieldMeta> fields;
    private List<FieldMeta> primaryKeys;

    public TableMeta(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldMeta> getFields() {
        return fields;
    }

    public void setFields(List<FieldMeta> fields) {
        this.fields = fields;
    }

    public List<FieldMeta> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<FieldMeta> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
