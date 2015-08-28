package com.scienjus.base.dao;

import com.scienjus.base.domain.User;

import java.util.List;

public interface UserDao {

	public void insert(User user);

	public void update(User user);

	public void delete(User user);

	public User get(long id); 

	public List<User> find(String where, Object... args);

}
