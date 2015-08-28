package com.scienjus.tools;

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

class DataSource {
    private String driver;

    private String url;

    private String username;

    private String password;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

class Packages {
    private String domain;

    private String dao;

    private String daoImpl;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDao() {
        return dao;
    }

    public void setDao(String dao) {
        this.dao = dao;
    }

    public String getDaoImpl() {
        return daoImpl;
    }

    public void setDaoImpl(String daoImpl) {
        this.daoImpl = daoImpl;
    }

    @Override
    public String toString() {
        return "Packages{" +
                "domain='" + domain + '\'' +
                ", dao='" + dao + '\'' +
                ", daoImpl='" + daoImpl + '\'' +
                '}';
    }

}
