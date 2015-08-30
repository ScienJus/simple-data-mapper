package com.scienjus.generator.meta;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
public class FieldMeta {

    private String name;
    private String comment;
    private boolean autoKey = false;
    private int type;

    public FieldMeta(String name, String comment, boolean autoKey, int type) {
        this.name = name;
        this.comment = comment;
        this.autoKey = autoKey;
        this.type = type;
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

    public boolean isAutoKey() {
        return autoKey;
    }

    public void setAutoKey(boolean autoKey) {
        this.autoKey = autoKey;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
