package com.baayso.bms.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.baayso.bms.common.db.exception.DBException;
import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.common.util.SQLHelper;
import com.baayso.bms.page.PageBean;

/**
 * JDBC操作数据库工具类。
 * 
 * @author ChenFangjie
 * 
 */
public class JdbcTemplate {

    private static final Logger log = Log.get();

    /**
     * 新增、修改、删除（格式化好的SQL语句）。
     * 
     * @param sql
     *            格式化好的SQL语句
     * @return 受影响的行数
     */
    public int update(final String sql) {
        return update(sql, (Object[]) null);
    }

    /**
     * 新增、修改、删除（带占位符(?)的SQL语句）。
     * 
     * @param sql
     *            带占位符(?)的SQL语句
     * @param params
     *            参数数组
     * @return 受影响的行数
     */
    public int update(final String sql, Object... params) {

        if (sql == null) {
            throw new DBException("SQL must not be null.");
        }

        int count = -1;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JdbcUtil.getConn();
            pstmt = conn.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            count = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
        finally {
            JdbcUtil.closeDB(conn, pstmt, null);
        }

        return count;
    }

    /**
     * 查询单个对象（单条记录），使用格式化好的SQL语句。
     * 
     * @param <T>
     *            返回类型
     * @param sql
     *            格式化好的SQL语句
     * @param rowMapper
     *            结果集映射对象
     * @return 实体对象
     */
    public <T> T query(final String sql, IRowMapper<T> rowMapper) {
        return query(sql, rowMapper, (Object[]) null);
    }

    /**
     * 查询单个对象（单条记录），使用带占位符(?)的SQL语句。
     * 
     * @param <T>
     *            返回类型
     * @param sql
     *            带占位符(?)的SQL语句
     * @param rowMapper
     *            结果集映射对象
     * @param params
     *            参数数组
     * @return 实体对象
     */
    public <T> T query(final String sql, IRowMapper<T> rowMapper, Object... params) {

        if (rowMapper == null) {
            throw new DBException("RowMapper must not be null.");
        }

        if (sql == null) {
            throw new DBException("SQL must not be null.");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtil.getConn();
            pstmt = conn.prepareStatement(sql);
            if (null != params && 0 < params.length) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            T entity = null;
            if (rs.next()) {
                entity = rowMapper.mapRow(rs);
            }

            return entity;
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
        finally {
            JdbcUtil.closeDB(conn, pstmt, rs);
        }
    }

    /**
     * 查询列表（多条记录），使用格式化好的SQL语句。
     * 
     * @param <T>
     *            返回类型
     * @param sql
     *            格式化好的SQL语句
     * @param rowMapper
     *            结果集映射对象
     * @return 列表
     */
    public <T> List<T> queryForList(final String sql, IRowMapper<T> rowMapper) {
        return queryForList(sql, rowMapper, (Object[]) null);
    }

    /**
     * 查询列表（多条记录），使用带占位符(?)的SQL语句。
     * 
     * @param <T>
     *            返回类型
     * @param sql
     *            带占位符(?)的SQL语句
     * @param rowMapper
     *            结果集映射对象
     * @param params
     *            参数数组
     * @return 列表
     */
    public <T> List<T> queryForList(final String sql, IRowMapper<T> rowMapper, Object... params) {

        if (rowMapper == null) {
            throw new DBException("RowMapper must not be null.");
        }

        if (sql == null) {
            throw new DBException("SQL must not be null.");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtil.getConn();
            pstmt = conn.prepareStatement(sql);
            if (null != params && 0 < params.length) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            List<T> list = new ArrayList<T>();
            T entity = null;
            while (rs.next()) {
                entity = rowMapper.mapRow(rs);
                list.add(entity);
            }

            return list;
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
        finally {
            JdbcUtil.closeDB(conn, pstmt, rs);
        }
    }

    /**
     * 行查询，并返回查询结果集中首行首列的值，忽略其他行或列。使用格式化好的SQL语句。
     * 
     * @param sql
     *            格式化好的SQL语句
     * @return 返回查询结果集中首行首列的值
     */
    public Object executeScalar(String sql) {
        return executeScalar(sql, (Object[]) null);
    }

    /**
     * 执行查询，并返回查询结果集中首行首列的值，忽略其他行或列。使用带占位符(?)的SQL语句。
     * 
     * @param sql
     *            带占位符(?)的SQL语句
     * @param params
     *            封装参数的数组
     * @return 返回查询结果集中首行首列的值
     */
    public Object executeScalar(String sql, Object... params) {

        if (sql == null) {
            throw new DBException("SQL must not be null.");
        }

        Object value = null; // 存储首行首列的值

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtil.getConn();
            pstmt = conn.prepareStatement(sql);
            if (null != params && 0 < params.length) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();

            if (rs.next()) {
                value = rs.getObject(1);
            }
        }
        catch (SQLException e) {
            throw new DBException(e);
        }
        finally {
            JdbcUtil.closeDB(conn, pstmt, rs);
        }

        return value;
    }

    /**
     * 分页查询（用户列表及分页信息封装于PageBean中）
     * 
     * @param <T>
     *            返回类型
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @param sqlHelper
     *            封装了分页查询语句以及查询条件的SQLHelper对象
     * @param rowMapper
     *            结果集映射对象
     * @return 分页信息对象
     */
    public <T> PageBean<T> pagination(int pageNum, int pageSize, SQLHelper sqlHelper, IRowMapper<T> rowMapper) {

        if (sqlHelper == null) {
            throw new DBException("SQLHelper must not be null.");
        }

        if (rowMapper == null) {
            throw new DBException("RowMapper must not be null.");
        }

        log.debug("Query Count SQL: {}", sqlHelper.getQueryCountSql());

        // SQL语句使用的'参数列表'
        Object[] params = sqlHelper.getParameters().toArray();

        String paginationQuerySql = "";

        switch (JdbcUtil.getDbType()) {
            case MYSQL:
                paginationQuerySql = sqlHelper.getMySQLPaginationQueryListSql(pageNum, pageSize);
                break;
            case ORACLE:
                paginationQuerySql = sqlHelper.getOraclePaginationQueryListSql(pageNum, pageSize);
                break;
            case MSSQLSERVER:
                paginationQuerySql = sqlHelper.getSQLServerPaginationQueryListSql(pageNum, pageSize);
                break;
            case SQLITE:
                paginationQuerySql = sqlHelper.getSQLitePaginationQueryListSql(pageNum, pageSize);
                break;
            case POSTGRESQL:
                paginationQuerySql = sqlHelper.getPostgreSQLPaginationQueryListSql(pageNum, pageSize);
                break;
            case SYSBASE:
                paginationQuerySql = sqlHelper.getSysbasePaginationQueryListSql(pageNum, pageSize);
                break;
            case UNKNOWN:
                throw new DBException("unknown database type.");
            default:
                throw new AssertionError("program should not run into here.");
        }

        log.debug("Pagination Query SQL: {}", paginationQuerySql);

        // 1. 查询指定页的数据列表
        List<T> recordList = this.queryForList(paginationQuerySql, rowMapper, params);

        // 2. 查询总记录数
        Object tempObj = this.executeScalar(sqlHelper.getQueryCountSql(), params);
        long totalRecord = 0L;
        if (null != tempObj) {
            String tempStr = tempObj.toString();
            if (CharacterUtil.isNumber(tempStr)) {
                totalRecord = Long.parseLong(tempStr);
            }
        }

        return new PageBean<T>(pageNum, pageSize, recordList, totalRecord);
    }

}
