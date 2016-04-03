package com.baayso.bms.privilege;

import java.util.List;
import java.util.RandomAccess;

/**
 * 业务：权限
 * 
 * @author ChenFangjie
 * 
 */
public class PrivilegeService {

    private PrivilegeDao privilegeDao = new PrivilegeDao();

    /**
     * 根据角色编号查询出此角色包含的权限信息
     * 
     * @param roleId
     *            角色编号
     * @return 权限列表
     */
    public List<Privilege> findListByRoleId(String roleId) {
        List<Privilege> privileges = privilegeDao.findListByRoleId(roleId);

        // 此处查询出权限列表中每个权限的上级权限以及子权限
        if (privileges instanceof RandomAccess) { // 不同的列表使用不同的遍历方式
            for (int i = 0; i < privileges.size(); i++) {
                String id = privileges.get(i).getId();
                // 查询出上级权限
                privileges.get(i).setParent(privilegeDao.findParentById(id));
                // 查询出子权限列表
                privileges.get(i).setChildren(privilegeDao.findChildrenById(id));
            }
        }
        else {
            for (Privilege privilege : privileges) {
                String id = privilege.getId();
                // 查询出上级权限
                privilege.setParent(privilegeDao.findParentById(id));
                // 查询出子权限列表
                privilege.setChildren(privilegeDao.findChildrenById(id));
            }
        }

        return privileges;
    }

    /**
     * 查询所有权限的url
     * 
     * @return
     */
    public List<String> findAllPrivilegeUrls() {
        return this.privilegeDao.findAllPrivilegeUrls();
    }

}
