package com.scienjus.instance.dao;

import com.scienjus.base.dao.SimpleDataMapper;
import com.scienjus.instance.domain.User;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

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
