package com.baayso.bms.user;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFind() {
        UserDao dao = new UserDao();
        User user = dao.find("admin", "admin");

        Assert.assertNotNull("测试失败，查询结果为null！", user);
        Assert.assertEquals("admin", user.getLoginName());
    }

    @Test
    public void testList() {
        UserDao dao = new UserDao();
        List<User> users = dao.list();

        Assert.assertNotNull("测试失败，查询结果为null！", users);
        Assert.assertEquals("admin", users.get(0).getLoginName());
    }

}
