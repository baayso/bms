package com.baayso.bms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.baayso.bms.common.db.IRowMapper;
import com.baayso.bms.common.db.JdbcTemplate;
import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.common.util.SQLHelper;
import com.baayso.bms.page.PageBean;

/**
 * 数据库操作：用户
 * 
 * @author ChenFangjie
 * 
 */
public class UserDao {

    private JdbcTemplate template = new JdbcTemplate();

    /**
     * 新增用户
     * 
     * @param user
     *            封装了待插入用户信息的对象
     * @return 受影响的行数
     */
    public int add(User user) {
        final String sql = "INSERT INTO t_user(id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = new Object[] { //
                user.getId(), //
                user.getLoginName(), //
                user.getUserName(), //
                user.getPassword(), //
                user.getGender(), //
                user.getBirthday(), //
                user.getTel(), //
                user.getAddress(), //
                user.getRoleId(), //
                user.getCreateTime() //
        };

        return this.template.update(sql, params);
    }

    /**
     * 根据用户id删除用户
     * 
     * @param id
     *            用户id
     * @return 受影响的行数
     */
    public int delete(String id) {
        final String sql = "DELETE FROM t_user WHERE id = ?";

        return this.template.update(sql, id);
    }

    /**
     * 修改用户
     * 
     * @param user
     *            封装了用户新信息的对象
     * @return 受影响的行数
     */
    public int update(User user) {
        final String sql = "UPDATE t_user SET loginName=?, userName=?, gender=?, birthday=?, tel=?, address=?, roleId=? WHERE id=?";

        Object[] params = new Object[] { //
                user.getLoginName(), //
                user.getUserName(), //
                user.getGender(), //
                user.getBirthday(), //
                user.getTel(), //
                user.getAddress(), //
                user.getRoleId(), //
                user.getId() //
        };

        return this.template.update(sql, params);
    }

    /**
     * 根据用户id修改用户密码
     * 
     * @param id
     *            用户id
     * @param newPassword
     *            新密码
     * @return 受影响的行数
     */
    public int modifyPassword(String id, String newPassword) {
        final String sql = "UPDATE t_user SET password=? WHERE id=?";

        Object[] params = new Object[] { newPassword, id };

        return this.template.update(sql, params);
    }

    /**
     * 根据用户id查询用户
     * 
     * @param id
     *            用户id
     * @return 用户对象
     */
    public User find(String id) {
        final String sql = "SELECT id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime FROM t_user WHERE id=?";

        return this.template.query(sql, new UserRowMapper(), id);
    }

    /**
     * 根据用户名和密码查询用户
     * 
     * @param loginName
     *            用户名
     * @param password
     *            密码
     * @return 用户对象
     */
    public User find(String loginName, String password) {
        final String sql = "SELECT id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime FROM t_user WHERE loginName=? AND password=?";

        Object[] params = new Object[] { loginName, password };

        return this.template.query(sql, new UserRowMapper(), params);
    }

    /**
     * 查询所有的用户
     * 
     * @return 封装了所有用户的列表
     */
    public List<User> list() {
        final String sql = "SELECT id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime FROM t_user ORDER BY createTime";

        return this.template.queryForList(sql, new UserRowMapper());
    }

    /**
     * 分页查询用户列表（用户列表及分页信息封装于PageBean中）
     * 
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @param sqlHelper
     *            封装了分页查询语句以及查询条件的SQLHelper对象
     * @return 分页信息对象
     */
    public PageBean<User> findPagingUsers(int pageNum, int pageSize, SQLHelper sqlHelper) {
        return this.template.pagination(pageNum, pageSize, sqlHelper, new UserRowMapper());
    }

    /**
     * 查询数据库中是否有指定登录名的记录
     * 
     * @param loginName
     *            登录名
     * @return 返回符合条件的记录数量
     */
    public long loginNameExists(String loginName) {
        String sql = "SELECT COUNT(*) FROM t_user WHERE loginName=?";

        Object tempObj = this.template.executeScalar(sql, loginName);

        long count = 0;
        if (null != tempObj) {
            String tempStr = tempObj.toString();
            if (CharacterUtil.isNumber(tempStr)) {
                count = Long.parseLong(tempStr);
            }
        }

        return count;
    }

    private class UserRowMapper implements IRowMapper<User> {
        @Override
        public User mapRow(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setLoginName(rs.getString("loginName"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setGender(rs.getString("gender"));
            user.setBirthday(rs.getString("birthday"));
            user.setTel(rs.getString("tel"));
            user.setAddress(rs.getString("address"));
            user.setRoleId(rs.getString("roleId"));
            user.setCreateTime(rs.getString("createTime"));
            return user;
        }
    }

}
