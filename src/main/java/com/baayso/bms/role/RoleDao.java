package com.baayso.bms.role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.baayso.bms.common.db.IRowMapper;
import com.baayso.bms.common.db.JdbcTemplate;

/**
 * 数据库操作：角色
 * 
 * @author ChenFangjie
 * 
 */
public class RoleDao {

    private JdbcTemplate template = new JdbcTemplate();

    public List<Role> list() {
        final String sql = "SELECT id, name, descriptions FROM t_role";
        return this.template.queryForList(sql, new RoleRowMapper());
    }

    public Role findByUserId(String userId) {
        final String sql = "SELECT t_role.id, t_role.name, t_role.descriptions from t_role INNER JOIN t_user on t_role.id = t_user.roleId WHERE t_user.id = ?";

        return this.template.query(sql, new RoleRowMapper(), userId);
    }

    public Role find(String id) {
        final String sql = "SELECT id, name, descriptions FROM t_role WHERE id = ?";

        return this.template.query(sql, new RoleRowMapper(), id);
    }

    private class RoleRowMapper implements IRowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs) throws SQLException {
            Role role = new Role();
            role.setId(rs.getString("id"));
            role.setName(rs.getString("name"));
            role.setDescriptions(rs.getString("descriptions"));
            return role;
        }
    }

}
