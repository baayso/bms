package com.baayso.bms.common.db;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JdbcUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetConn() throws SQLException {
        Connection conn = JdbcUtil.getConn();
        DatabaseMetaData md = conn.getMetaData();

        System.out.println(md.getDriverName());
        System.out.println(md.getDriverVersion());
        System.out.println(md.getURL());

        JdbcUtil.closeDB(conn, null, null);
    }

    @Test
    public void testCloseDB() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDbType() {
        System.out.println(JdbcUtil.getDbType());
    }

}
