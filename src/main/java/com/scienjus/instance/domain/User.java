package com.scienjus.instance.domain;

import com.scienjus.base.domain.BaseDomain;
import com.scienjus.base.util.Table;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/8/30.
 */
@Table(name = "user_", keys = {"id_"}, selectSQL = "select a.id_, a.username_, a.password_ from user_ a ")
public class User extends BaseDomain {

    private long id;

    private String username;

    private String password;

    public User() {
        super();
    }

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getLong(1);
        this.username = rs.getString(2);
        this.password = rs.getString(3);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this.changeFields.put("id_", id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.changeFields.put("username_", username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.changeFields.put("password_", password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
