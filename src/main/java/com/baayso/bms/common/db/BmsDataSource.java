package com.baayso.bms.common.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.baayso.bms.common.util.ConfigUtil;
import com.baayso.bms.common.util.ConstantUtil;

/**
 * 数据库连接池
 * 
 * @author ChenFangjie
 * 
 */
public class BmsDataSource implements DataSource {

    private static String url = ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_URL);
    private static String username = ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_USERNAME);
    private static String password = ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_PASSWORD);

    // 初始连接数
    private static int initSize = Integer.parseInt(ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_INIT_SIZE));
    // 最大连接数
    private static int maxActive = Integer.parseInt(ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_MAX_ACTIVE));
    // 当前已使用的连接数
    private int currentActive = 0;

    private LinkedList<Connection> connPool = new LinkedList<Connection>();

    public BmsDataSource() {
        // 初始化数据库连接
        try {
            for (int i = 0; i < initSize; i++) {
                this.connPool.addLast(this.createConnection());
                this.currentActive++;
            }
        }
        catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // 创建数据库连接
    private Connection createConnection() throws SQLException {
        Connection realConn = DriverManager.getConnection(url, username, password);

        BmsConnectionHandler proxy = new BmsConnectionHandler(this);
        // 获到并返回动态生成的代理类，此代理类重写了close()方法
        return proxy.bind(realConn);
    }

    /**
     * 释放数据库连接
     * 
     * @param conn
     */
    public void free(Connection conn) {
        this.connPool.addLast(conn);
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (connPool) {
            // 如果连接池中还有剩余连接，则从连接池中返回连接
            if (this.connPool.size() > 0) {
                return this.connPool.removeFirst();
            }
            else if (this.currentActive < maxActive) {
                this.currentActive++;
                return this.createConnection();
            }

            // 数据库连接数已超过最大连接，则抛出异常
            throw new SQLException("已超过最大连接数！");
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return false;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return null;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported by BmsDataSource");
        // return null;
    }

}
