package com.baayso.bms.common.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JdbcTemplateTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQuery() {
        JdbcTemplate template = new JdbcTemplate();

        template.query(null, null);
    }

}
