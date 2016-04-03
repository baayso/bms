package com.baayso.bms.common.dbutils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.baayso.bms.common.db.JdbcUtil;
import com.baayso.bms.common.db.exception.DBException;

/**
 * 数据库连接工具类
 * <p>
 * 源自 http://www.cnblogs.com/xdp-gacl/p/4007225.html
 * 
 * @author (2016/3/4 10:05)
 * 
 */
public class JdbcUtils {

    private static DataSource ds = null;

    // 使用ThreadLocal存储当前线程中的Connection对象
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    // 在静态代码块中创建数据库连接池
    static {
        try {
            // 通过代码创建C3P0数据库连接池
            // ds = new ComboPooledDataSource();
            // ds.setDriverClass("com.mysql.jdbc.Driver");
            // ds.setJdbcUrl("jdbc:mysql://localhost:3306/bms");
            // ds.setUser("root");
            // ds.setPassword("root");
            // ds.setInitialPoolSize(10);
            // ds.setMinPoolSize(5);
            // ds.setMaxPoolSize(20);

            // 通过读取C3P0的xml配置文件创建数据源，C3P0的xml配置文件c3p0-config.xml必须放在src目录下
            // ds = new ComboPooledDataSource(); //使用C3P0的默认配置来创建数据源
            // ds = new ComboPooledDataSource("MySQL");// 使用C3P0的命名配置来创建数据源

            ds = JdbcUtil.getDataSource();
        }
        catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 从数据源中获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        // 从当前线程中获取Connection
        Connection conn = threadLocal.get();
        if (conn == null) {
            // 从数据源中获取数据库连接
            conn = getDataSource().getConnection();
            // 将conn绑定到当前线程
            threadLocal.set(conn);
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void startTransaction() {
        try {
            Connection conn = threadLocal.get();
            if (conn == null) {
                conn = getConnection();
                // 把 conn绑定到当前线程上
                threadLocal.set(conn);
            }
            // 开启事务
            conn.setAutoCommit(false);
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                // 回滚事务
                conn.rollback();
            }
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    /**
     * 提交事务
     */
    public static void commit() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                // 提交事务
                conn.commit();
            }
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    /**
     * 关闭数据库连接(注意，并不是真的关闭，而是把连接还给数据库连接池)
     */
    public static void close() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                conn.close();
                // 解除当前线程上绑定conn
                threadLocal.remove();
            }
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        // 从数据源中获取数据库连接
        return ds;
    }

    /**
     * 创建QueryRunner
     */
    public static QueryRunner createQueryRunner() {
        return new QueryRunner(getDataSource());
    }

}
