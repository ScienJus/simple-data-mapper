package com.scienjus.instance.dao;

import com.scienjus.instance.domain.User;

import java.util.List;

/**
 * Created by Administrator on 2015/8/30.
 */
public interface UserDao {

    public void insert(User user);

    public void update(User user);

    public void delete(User user);

    public User get(long id);

    public List<User> find(String where, Object... args);
}
