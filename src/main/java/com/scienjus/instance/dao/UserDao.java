package com.scienjus.instance.dao;

import java.util.List;
import com.scienjus.instance.domain.User;

public interface UserDao {

	public void insert(User user);

	public void update(User user);

	public void delete(User user);

	public User get(Long id); 

	public List<User> find(String where, Object... args);

}
