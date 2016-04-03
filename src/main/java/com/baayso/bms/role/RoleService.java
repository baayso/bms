package com.baayso.bms.role;

import java.util.List;

import com.baayso.bms.privilege.Privilege;
import com.baayso.bms.privilege.PrivilegeService;

/**
 * 业务：角色
 * 
 * @author ChenFangjie
 * 
 */
public class RoleService {

    private RoleDao roleDao = new RoleDao();
    private PrivilegeService privilegeService = new PrivilegeService();

    public List<Role> findAll() {
        return roleDao.list();
    }

    /**
     * 根据用户编号查询用户所对应的角色
     * 
     * @param userId
     *            用户编号
     * @return 角色实体对象
     */
    public Role findByUserId(String userId) {
        Role role = roleDao.findByUserId(userId);

        if (null != role) {
            // 查询出此角色所包含的权限信息
            List<Privilege> privileges = privilegeService.findListByRoleId(role.getId());
            role.setPrivileges(privileges);
        }

        return role;
    }

    /**
     * 查询角色，但不查询出角色所对应的权限列表
     * 
     * @param id
     *            角色编号
     * @return 角色实体对象
     */
    public Role findWithoutPrivilege(String id) {
        return roleDao.find(id);
    }

}
