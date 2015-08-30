package com.scienjus.instance.domain;

import com.scienjus.base.domain.BaseDomain;
import com.scienjus.base.util.Table;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2015/8/30.
 */
@Table(name = "table_", keys = {"id_"}, selectSQL = "select a.id_, a.username_, a.password_ from user_ a")
public class User extends BaseDomain {

    public User() {
        super();
    }

    public User(ResultSet rs) {
        super(rs);
    }
}
