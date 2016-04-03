package com.baayso.bms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.baayso.bms.common.db.IRowMapper;
import com.baayso.bms.common.db.JdbcTemplate;
import com.baayso.bms.common.util.SQLHelper;
import com.baayso.bms.page.PageBean;

/**
 * 数据库操作：在线用户信息
 * 
 * @author ChenFangjie(2015年9月2日 下午11:01:26)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class OnlineUserDao {

    private JdbcTemplate template = new JdbcTemplate();

    /**
     * 新增在线用户信息
     * 
     * @param onlineUser
     *            封装了待插入在线用户信息的对象
     * @return 受影响的行数
     */
    public int add(OnlineUser onlineUser) {
        final String sql = "INSERT INTO t_online_user(sessionId, userId, loginName, userName, clientIp, pageUrl, createTime, lastAccessTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = new Object[] { //
                onlineUser.getSessionId(), //
                onlineUser.getUserId(), //
                onlineUser.getLoginName(), //
                onlineUser.getUserName(), //
                onlineUser.getClientIp(), //
                onlineUser.getPageUrl(), //
                onlineUser.getCreateTime(), //
                onlineUser.getLastAccessTime() };

        return this.template.update(sql, params);
    }

    /**
     * 根据会话ID删除在线用户信息
     * 
     * @param sessionId
     *            会话ID
     * @return 受影响的行数
     */
    public int delete(String sessionId) {
        final String sql = "DELETE FROM t_online_user WHERE sessionId = ?";

        return this.template.update(sql, sessionId);
    }

    /**
     * 根据一组会话ID删除在线用户信息
     * 
     * @param sessionIds
     *            一组会话ID
     * @return 受影响的行数
     */
    public int delete(List<String> sessionIds) {
        if (sessionIds == null || sessionIds.isEmpty()) {
            return 0;
        }

        StringBuilder sb = new StringBuilder();

        // Object[] params = sessionIds.toArray();
        Object[] params = new Object[sessionIds.size()];

        for (int i = 0; i < sessionIds.size(); i++) {
            if (i == (sessionIds.size() - 1)) {
                sb.append("?");
            }
            else {
                sb.append("?, ");
            }

            params[i] = sessionIds.get(i);
        }

        final String sql = String.format("DELETE FROM t_online_user WHERE sessionId IN (%s)", sb.toString());

        return this.template.update(sql, params);
    }

    /**
     * 修改在线用户信息
     * 
     * @param onlineUser
     *            封装了新在线用户信息的对象
     * @return 受影响的行数
     */
    public int update(OnlineUser onlineUser) {
        final String sql = "UPDATE t_online_user SET userId=?, loginName=?, userName=?, clientIp=?, pageUrl=?, lastAccessTime=? WHERE sessionId=?";

        Object[] params = new Object[] { //
                onlineUser.getUserId(), //
                onlineUser.getLoginName(), //
                onlineUser.getUserName(), //
                onlineUser.getClientIp(), //
                onlineUser.getPageUrl(), //
                onlineUser.getLastAccessTime(), //
                onlineUser.getSessionId() };

        return this.template.update(sql, params);
    }

    /**
     * 根据会话ID查询在线用户信息
     * 
     * @param sessionId
     *            会话ID
     * @return 在线用户信息对象
     */
    public OnlineUser find(String sessionId) {
        final String sql = "SELECT sessionId, userId, loginName, userName, clientIp, pageUrl, createTime, lastAccessTime FROM t_online_user WHERE sessionId=?";

        return this.template.query(sql, new OnlineUserRowMapper(), sessionId);
    }

    /**
     * 分页查询在线用户信息（在线用户信息列表及分页信息封装于PageBean中）
     * 
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @param sqlHelper
     *            封装了分页查询语句以及查询条件的SQLHelper对象
     * @return 分页信息对象
     */
    public PageBean<OnlineUser> findPagingOnlineUsers(int pageNum, int pageSize, SQLHelper sqlHelper) {
        return this.template.pagination(pageNum, pageSize, sqlHelper, new OnlineUserRowMapper());
    }

    private class OnlineUserRowMapper implements IRowMapper<OnlineUser> {
        @Override
        public OnlineUser mapRow(ResultSet rs) throws SQLException {
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setSessionId(rs.getString("sessionId"));
            onlineUser.setUserId(rs.getString("userId"));
            onlineUser.setLoginName(rs.getString("loginName"));
            onlineUser.setUserName(rs.getString("userName"));
            onlineUser.setClientIp(rs.getString("clientIp"));
            onlineUser.setPageUrl(rs.getString("pageUrl"));
            onlineUser.setCreateTime(rs.getLong("createTime"));
            onlineUser.setLastAccessTime(rs.getLong("lastAccessTime"));
            return onlineUser;
        }
    }

}
