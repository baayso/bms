package com.baayso.bms.user;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baayso.bms.privilege.Privilege;
import com.baayso.bms.role.Role;

/**
 * 实体: 用户表
 * 
 * @author ChenFangjie
 * 
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7826178495775809744L;

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private String id; // 用户编号
    private String loginName; // 登录名
    private String userName; // 用户名
    private String password; // 用户密码
    private String gender; // 用户性别，M 表示男，F 表示女
    private String birthday; // 用户出生日期
    private String tel; // 用户电话
    private String address; // 用户地址
    private String roleId; // 外键，对应角色表的id
    private String createTime; // 用户创建时间

    private Role role = new Role(); // 对应的角色

    /**
     * 根据url判断用户是否拥有该url对应功能的权限
     * 
     * @param url
     *            客户端访问地址
     * @param request
     *            HttpServletRequest
     * @return 有权限返回true，否则返回false。
     */
    public boolean hasPrivilegeByUrl(String url, HttpServletRequest request) {

        // 超级管理员拥有系统所有的权限
        if (isAdmin()) {
            return true;
        }

        // 截取倒数第二个"/"后面的url
        // 例：/BMS/jsp/user/UserServlet?method=LIST
        // user/UserServlet?method=LIST
        // 用lastIndexOf("/")找到最后一个"/"的位置，然后substring，得到的新字符串，再调用一次之前的过程，就可以了。
        int position1 = url.lastIndexOf("/");
        String newUrl = url.substring(0, position1);
        int position2 = newUrl.lastIndexOf("/");
        url = url.substring(position2 + 1);

        // 去年查找到第一个"&"符号后的字符
        int index = url.indexOf("&");
        if (index > 0) {
            url = url.substring(0, index);
        }

        // 如果url地址以 "_UI" 字符串结尾，则去掉其中的 "_UI" 字符串，以得到对应的权限。
        // 例：user/UserServlet?method=ADD_UI --> user/UserServlet?method=ADD
        // ADD_UI与ADD是同一个权限
        if (url.endsWith("_UI")) {
            url = url.substring(0, url.length() - 3);
        }

        // 获取准备好的“所有权限的url集合”
        @SuppressWarnings("unchecked")
        List<String> allPrivilegeUrls = (List<String>) request.getSession().getServletContext().getAttribute("allPrivilegeUrls");

        if (!allPrivilegeUrls.contains(url)) // 不受控的功能未存储在权限表中
        {
            // 不受控制的功能，所有用户都可以使用（例：登录，注销，访问首页）
            logger.info("登录用户：" + loginName + "进行访问权限判断，不受控制的请求地址：" + url);
            return true;
        }
        else {
            for (Privilege privilege : role.getPrivileges()) {
                // 判断权限列表中是否存在指定权限，有则返回true
                if (url.equals(privilege.getUrl())) {
                    logger.info("登录用户：" + loginName + "进行访问权限判断，受控制的请求地址（用户拥有此请求地址的权限）：" + url);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断是否为超级管理员
     * 
     * @return 是超级管理员就返回true，否则返回false。
     */
    public boolean isAdmin() {
        return "admin".equals(loginName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
