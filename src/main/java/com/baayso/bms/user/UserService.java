package com.baayso.bms.user;

import java.util.List;
import java.util.RandomAccess;
import java.util.UUID;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.ConfigUtil;
import com.baayso.bms.common.util.SQLHelper;
import com.baayso.bms.page.PageBean;
import com.baayso.bms.role.Role;
import com.baayso.bms.role.RoleService;

/**
 * 业务：用户
 * 
 * @author ChenFangjie
 * 
 */
public class UserService {

    private static final Logger log = Log.get();

    private UserDbUtilsDao userDao = new UserDbUtilsDao();
    private RoleService roleService = new RoleService();

    public boolean add(User user) {

        String uuid = UUID.randomUUID().toString(); // 生成UUID主键

        log.debug("primary key(uuid): {}", uuid);

        user.setId(uuid); // 设置主键

        if (0 < userDao.add(user)) {
            return true;
        }

        return false;
    }

    public boolean delete(String id) {
        if (0 < userDao.delete(id)) {
            return true;
        }
        return false;
    }

    public boolean update(User user) {
        if (0 < userDao.update(user)) {
            return true;
        }
        return false;
    }

    /**
     * 查询用户，并且查询出用户所对应的角色以及角色所对应的权限列表
     * 
     * @param id
     *            用户编号
     * @return
     */
    public User find(String id) {
        User user = userDao.find(id);
        // 查询出此用户对应的角色
        user.setRole(roleService.findByUserId(user.getId()));

        return user;
    }

    /**
     * 查询用户，但不查询出用户所对应的角色
     * 
     * @param id
     *            用户编号
     * @return
     */
    public User findWithoutRole(String id) {
        return userDao.find(id);
    }

    /**
     * 用户登录
     * 
     * @param loginName
     *            登录名
     * @param password
     *            登录密码
     * @return 用户实体对象
     */
    public User login(String loginName, String password) {
        User user = userDao.find(loginName, password);

        if (null != user) {
            // 查询出此用户所对应的角色
            Role role = roleService.findByUserId(user.getId());
            user.setRole(role);
        }

        return user;
    }

    public List<User> findAll() {
        List<User> users = userDao.list();

        if (users instanceof RandomAccess) { // 不同的列表选择不同的遍历方式
            for (int i = 0; i < users.size(); i++) {
                String userId = users.get(i).getId();
                // 查询出此用户对应的角色
                users.get(i).setRole(roleService.findByUserId(userId));
            }
        }
        else {
            for (User user : users) {
                String userId = user.getId();
                // 查询出此用户对应的角色
                user.setRole(roleService.findByUserId(userId));
            }
        }

        return users;
    }

    public boolean modifyPassword(String id, String newPassword) {
        if (0 < userDao.modifyPassword(id, newPassword)) {
            return true;
        }
        return false;
    }

    public PageBean<User> findPagingUsers(int pageNum, String loginName) {

        int pageSize = ConfigUtil.getPageSize();

        // 生成SQL语句的工具类（此处接受一些设置）
        SQLHelper sqlHelper = new SQLHelper("t_user", "u") //
                .addWhere(null != loginName && 0 < loginName.length(), "u.loginName LIKE ?", "%" + loginName + "%") //
                .addOrderBy("u.createTime", "ASC");

        // SQLHelper sqlHelper = new SQLHelper("t_user") //
        // .addWhere(null != loginName && 0 < loginName.length(), "loginName LIKE ?", "%" + loginName + "%") //
        // .addOrderBy("createTime", "ASC");

        // 查询指定页的数据列表
        PageBean<User> pageBeans = userDao.findPagingUsers(pageNum, pageSize, sqlHelper);

        List<User> users = pageBeans.getRecordList();

        if (users instanceof RandomAccess) { // 不同的列表选择不同的遍历方式
            for (int i = 0; i < users.size(); i++) {
                String userId = users.get(i).getId();
                // 查询出此用户对应的角色
                users.get(i).setRole(roleService.findByUserId(userId));
            }
        }
        else {
            for (User user : users) {
                String userId = user.getId();
                // 查询出此用户对应的角色
                user.setRole(roleService.findByUserId(userId));
            }
        }

        return pageBeans;
    }

    /**
     * 判断登录名是否存在
     * 
     * @param loginName
     *            登录名
     * @return 存在返回true，否则返回false
     */
    public boolean loginNameExists(String loginName) {
        long count = userDao.loginNameExists(loginName);

        if (0L < count) {
            return true;
        }

        return false;
    }

}
