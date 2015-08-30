package com.scienjus.instance.dao.impl;

import com.scienjus.base.dao.SimpleDataMapper;
import com.scienjus.instance.dao.UserDao;
import com.scienjus.instance.domain.User;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/30.
 */
public class UserDapImpl extends SimpleDataMapper<User> implements UserDao {
    @Override
    protected User convertEntity(ResultSet rs) {
        return new User(rs);
    }

    @Override
    protected Class getEntityClass() {
        return User.class;
    }

    @Override
    public void insert(User user) {
        this.insertEntity(user);
    }

    @Override
    public void update(User user) {
        this.updateEntity(user);
    }

    @Override
    public void delete(User user) {
        this.deleteEntity(user);
    }

    @Override
    public User get(long id) {
        return this.getEntity(id);
    }

    @Override
    public List<User> find(String where, Object... args) {
        return this.findEntity(where, args);
    }
}
