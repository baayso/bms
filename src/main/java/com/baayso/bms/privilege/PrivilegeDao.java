package com.baayso.bms.privilege;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.baayso.bms.common.db.IRowMapper;
import com.baayso.bms.common.db.JdbcTemplate;

/**
 * 数据库操作：权限
 * 
 * @author ChenFangjie
 * 
 */
public class PrivilegeDao {

    private JdbcTemplate template = new JdbcTemplate();

    /**
     * 根据 角色编号 查询出 此角色包含的权限信息
     * 
     * @param roleId
     *            角色编号
     * @return 权限列表
     */
    public List<Privilege> findListByRoleId(String roleId) {
        final String sql = "SELECT p.id, p.name, p.url, p.parentId FROM t_privilege p INNER JOIN t_roles_privilege rp ON p.id = rp.privilegeid WHERE rp.roleid = ?";

        return this.template.queryForList(sql, new PrivilegeRowMapper(), roleId);
    }

    /**
     * 根据 权限编号 查询出 此权限的上级权限
     * 
     * @param id
     *            权限编号
     * @return 上级权限
     */
    public Privilege findParentById(String id) {
        final String sql = "SELECT p.id, p.name, p.url, p.parentId FROM t_privilege p INNER JOIN t_privilege c ON p.id = c.parentId WHERE c.id = ?";

        return this.template.query(sql, new PrivilegeRowMapper(), id);
    }

    /**
     * 根据 权限编号 查询子权限列表
     * 
     * @param id
     *            权限编号
     * @return 子权限列表
     */
    public List<Privilege> findChildrenById(String id) {
        final String sql = "SELECT * FROM t_privilege WHERE parentId = ?";

        return this.template.queryForList(sql, new PrivilegeRowMapper(), id);
    }

    /**
     * 查询所有权限的url
     * 
     * @return
     */
    public List<String> findAllPrivilegeUrls() {
        final String sql = "SELECT p.url FROM t_privilege p WHERE p.url IS NOT NULL";
        return this.template.queryForList(sql, new IRowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs) throws SQLException {
                return rs.getString("url");
            }
        });
    }

    private class PrivilegeRowMapper implements IRowMapper<Privilege> {
        @Override
        public Privilege mapRow(ResultSet rs) throws SQLException {
            Privilege privilege = new Privilege();
            privilege.setId(rs.getString("id"));
            privilege.setName(rs.getString("name"));
            privilege.setUrl(rs.getString("url"));
            privilege.setParentId(rs.getString("parentId"));
            return privilege;
        }
    }

}
