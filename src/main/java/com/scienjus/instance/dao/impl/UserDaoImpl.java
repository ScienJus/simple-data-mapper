package com.scienjus.instance.dao.impl;

import com.scienjus.base.dao.SimpleDataMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.scienjus.instance.domain.User;
import com.scienjus.instance.dao.UserDao;

@Repository
public class UserDaoImpl extends SimpleDataMapper<User> implements UserDao {

	@Override
	protected User convertEntity(ResultSet rs) throws SQLException {
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
	public User get(Long id) {
		return this.getEntity(id);
	}

	@Override
	public List<User> find(String where, Object... args) {
		return this.findEntity(where, args);
	}
}
