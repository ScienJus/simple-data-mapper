package com.scienjus.generator.config;

/**
 * Created by Administrator on 2015/8/30.
 */
public class Packages {
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
