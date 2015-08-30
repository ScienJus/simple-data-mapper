package com.scienjus.generator.config;

import java.util.List;

/**
 * @author XieEnlong
 * @date 2015/8/28.
 */
public class Generator {
    private DataSource dataSource;

    private Packages packages;

    private List<String> tables;

    private String path;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource datasource) {
        this.dataSource = datasource;
    }

    public Packages getPackages() {
        return packages;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Config{" +
                "datasource=" + dataSource +
                ", packages=" + packages +
                ", tables=" + tables +
                ", path=" + path +
                '}';
    }
}