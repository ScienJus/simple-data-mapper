package com.scienjus.instance.domain;

import com.scienjus.base.domain.BaseDomain;
import com.scienjus.base.util.Table;
import java.sql.ResultSet;
import java.sql.SQLException;

// 用户
@Table(name = "user_", keys = {"id_"}, selectSQL = "select a.id_, a.username_, a.password_ from user_ a ", isAutoKey = true)
public class User extends BaseDomain {
	// id
	private Long id;
	// 用户名
	private String username;
	// 密码
	private String password;

	public User() {
		super();
	}

	public User(ResultSet rs) throws SQLException {
		this.id = rs.getLong(1);
		this.username = rs.getString(2);
		this.password = rs.getString(3);
}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
		this.changeFields.put("id_", id);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.changeFields.put("username_", username);
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.changeFields.put("password_", password);
	}

}
