package com.scienjus;

import com.scienjus.instance.Application;
import com.scienjus.instance.dao.UserDao;
import com.scienjus.instance.domain.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author XieEnlong
 * @date 2015/8/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void test_0_insertTest() {
        User user = new User();
        user.setId(1);
        user.setUsername("zzz");
        user.setPassword("zzz");
        userDao.insert(user);
    }

    @Test
    public void test_1_getTest() {
        User user = userDao.get(1);
        System.out.println(user);
    }

    @Test
    public void test_2_updateTest() {
        User user = userDao.get(1);
        user.setUsername("aaa");
        user.setPassword("aaa");
        userDao.update(user);
    }

    @Test
    public void test_3_findTest() {
        List<User> users = userDao.find("where username_ = ?", "zzz");
        System.out.println(users);
    }

    @Test
    public void test_4_deleteTest() {
        User user = userDao.get(1);
        userDao.delete(user);
    }

}
