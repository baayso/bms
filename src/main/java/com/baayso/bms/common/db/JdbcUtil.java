package com.baayso.bms.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.baayso.bms.common.db.exception.DBException;
import com.baayso.bms.common.util.ConfigUtil;
import com.baayso.bms.common.util.ConstantUtil;

/**
 * 数据库连接工具类
 * 
 * @author ChenFangjie
 * 
 */
public final class JdbcUtil {

    public static enum DatabaseType {
        MYSQL, ORACLE, MSSQLSERVER, SQLITE, POSTGRESQL, SYSBASE, UNKNOWN
    }

    private static DatabaseType dbType = null;

    private static DataSource dataSource = null;

    // 加载驱动
    static {
        try {
            String driverClassName = ConfigUtil.getJdbcConfigValue(ConstantUtil.JDBC_DRIVER_CLASS_NAME);

            if (-1 < driverClassName.indexOf("mysql")) {
                JdbcUtil.dbType = DatabaseType.MYSQL;
            }
            else if (-1 < driverClassName.indexOf("oracle")) {
                JdbcUtil.dbType = DatabaseType.ORACLE;
            }
            else if (-1 < driverClassName.indexOf("sqlserver")) {
                JdbcUtil.dbType = DatabaseType.MSSQLSERVER;
            }
            else if (-1 < driverClassName.indexOf("sqlite")) {
                JdbcUtil.dbType = DatabaseType.SQLITE;
            }
            else if (-1 < driverClassName.indexOf("postgresql")) {
                JdbcUtil.dbType = DatabaseType.POSTGRESQL;
            }
            else if (-1 < driverClassName.indexOf("sysbase")) {
                JdbcUtil.dbType = DatabaseType.SYSBASE;
            }
            else {
                JdbcUtil.dbType = DatabaseType.UNKNOWN;
            }

            Class.forName(driverClassName);

            dataSource = new BmsDataSource();
        }
        catch (ClassNotFoundException e) {
            throw new DBException(e);
        }
        catch (Exception e) {
            throw new DBException(e);
        }
    }

    // 让工具类彻底不可以实例化
    private JdbcUtil() {
        throw new Error("工具类不可以实例化！");
    }

    /**
     * 获取数据库连接
     * 
     * @return
     * @throws SQLException
     */
    public static Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭数据库连接
     * 
     * @param conn
     * @param pstmt
     * @param rs
     */
    public static void closeDB(Connection conn, Statement pstmt, ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
        finally {
            try {
                if (null != pstmt) {
                    pstmt.close();
                }
            }
            catch (SQLException e) {
                throw new DBException(e);
            }
            finally {
                try {
                    if (null != conn) {
                        conn.close();
                    }
                }
                catch (SQLException e) {
                    throw new DBException(e);
                }
            }
        }
    }

    public static DatabaseType getDbType() {
        return JdbcUtil.dbType;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

}
