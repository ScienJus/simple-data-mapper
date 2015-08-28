package com.scienjus.base.domain;

import com.scienjus.base.util.Table;

// 用户信息
@Table(name = "user_", keys = {"id"})
public class User extends BaseDomain {
	// id
	private long id;
	// 用户名
	private String username;
	// 密码
	private String password;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
