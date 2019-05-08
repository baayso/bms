package com.baayso.bms.user;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.baayso.bms.common.BaseServlet;
import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.common.util.ValidateUtil;
import com.baayso.bms.common.util.WebUtils;
import com.baayso.bms.page.PageBean;
import com.baayso.bms.role.Role;
import com.baayso.bms.role.RoleService;
import com.google.gson.Gson;

/**
 * 用户Servlet。
 *
 * @author ChenFangjie (2019/5/7 10:12)
 * @since 1.3.0
 */
@WebServlet("/servlet/user/*")
public class UserServlet2 extends BaseServlet {

    private static final long serialVersionUID = -5714785465233126142L;

    private static final Logger log = Log.get();

    // 用户登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");

        // 数据校验
        if (!ValidateUtil.validateLoginName(loginName) && !ValidateUtil.validatePassword(password)) {
            log.error("Servlet：数据校验失败，用户名或密码不符合要求！");
            response.sendRedirect(request.getContextPath() + "/jsp/home/login.jsp");
            return;
        }

        UserService userService = new UserService();
        // 查询用户
        User currentUser = userService.login(loginName, password);

        if (null != currentUser) {
            log.info("Servlet：用户 {} 登录成功！", currentUser.getLoginName());
            // 登录成功，将用户对象存入Session作用域中
            request.getSession().setAttribute(ConstantUtil.CURRENT_USER, currentUser);
            response.sendRedirect(request.getContextPath() + "/jsp/home/index.jsp");
        }
        else {
            log.error("Servlet：登录失败，登录名或密码错误！");
            response.sendRedirect(request.getContextPath() + "/jsp/home/login.jsp");
        }
    }

    // 用户注销（退出）
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = (User) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);
        log.info("Servlet：用户：" + currentUser.getLoginName() + " 退出！");

        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/jsp/home/login.jsp");
    }

    // 添加用户页面（准备页面上所需的数据）
    public void addUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RoleService roleService = new RoleService();
        List<Role> roleList = roleService.findAll(); // 查询出添加用户页面上所需的角色列表信息
        request.setAttribute("roleList", roleList);
        request.getRequestDispatcher("/jsp/user/add.jsp").forward(request, response);
    }

    // 新增用户信息
    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("rePassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String telephone = request.getParameter("telephone");
        String roleId = request.getParameter("roleId");
        String address = request.getParameter("address");

        // 数据校验
        if (!ValidateUtil.validateLoginName(loginName)) {
            log.error("Servlet：数据校验失败，用户名不符合要求！");
            // response.sendRedirect("");
            return;
        }

        if (!ValidateUtil.validatePassword(password)) {
            log.error("Servlet：数据校验失败，密码不符合要求！");
            // response.sendRedirect("");
            return;
        }

        if (!password.equals(rePassword)) {
            log.error("Servlet：数据校验失败，密码和重复密码不一致！");
            return;
        }

        if (!"男".equals(gender) && !"女".equals(gender)) {
            log.error("Servlet：数据校验失败，性别不符合要求！");
            return;
        }

        if (CharacterUtil.isEmpty(roleId) || !(36 == roleId.length())) {
            log.error("Servlet：数据校验失败，roleId为空或长度不符合要求！");
            return;
        }

        RoleService roleService = new RoleService();
        Role role = roleService.findWithoutPrivilege(roleId);

        if (null == role) {
            log.error("Servlet：数据校验失败，为用户赋予的角色不存在！");
            return;
        }

        if (!CharacterUtil.isEmpty(birthday) && !ValidateUtil.validateDate(birthday)) {
            log.error("Servlet：数据校验失败，出生日期不符合要求！");
            return;
        }

        if (!CharacterUtil.isEmpty(telephone) && !ValidateUtil.validatePhoneNumber(telephone)) {
            log.error("Servlet：数据校验失败，电话号码不符合要求！");
            return;
        }

        if (!CharacterUtil.isEmpty(address) && address.length() > 140) {
            log.error("Servlet：数据校验失败，地址长度不符合要求！");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(ConstantUtil.LONG_TIME_FORMAT);

        // 封装数据
        User user = new User();
        user.setLoginName(loginName);
        user.setUserName(loginName);
        user.setPassword(rePassword);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setTel(telephone);
        user.setRoleId(roleId);
        user.setAddress(address);
        user.setCreateTime(dateFormat.format(new Date()));

        UserService userService = new UserService();
        // 新增数据
        if (userService.add(user)) {
            response.sendRedirect(request.getContextPath() + "/servlet/user/list");
        }
        else {
            log.error("Servlet：新增用户信息失败！");
        }
    }

    // 删除用户信息
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (CharacterUtil.isEmpty(id) || !(36 == id.length())) {
            log.error("Servlet：数据校验失败，id参数不正确，可能为空或长度不符合要求！");
            return;
        }

        UserService userService = new UserService();
        User user = userService.find(id);

        if (null == user) {
            log.error("Servlet：数据校验失败，待删除的用户不存在！");
            return;
        }

        // 获取当前登录用户
        User currentUser = (User) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

        if (user.getLoginName().equals(currentUser.getLoginName())) { // 不可删除当前登录用户
            log.error("Servlet：数据校验失败，当前登录用户不可删除！");
            return;
        }

        if ("admin".equals(user.getLoginName())) { // 不可以删除"admin"用户
            log.error("Servlet：数据校验失败，超级管理员“admin”不可以删除！");
            return;
        }

        // 执行删除
        if (userService.delete(id)) {
            response.sendRedirect(request.getContextPath() + "/servlet/user/list");
        }
        else {
            log.error("删除用户信息失败！");
        }
    }

    // 查看用户的详细信息（准备编辑用户页面的数据）
    public void updateUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (CharacterUtil.isEmpty(id) || !(36 == id.length())) {
            log.error("Servlet：数据校验失败，id参数不正确，可能为空或长度不符合要求！");
            return;
        }

        UserService userService = new UserService();
        User user = userService.find(id); // 查询出待查看（编辑）的用户信息

        RoleService roleService = new RoleService();
        List<Role> roleList = roleService.findAll(); // 查询出查看（编辑）页面上所需的角色列表

        request.setAttribute("roleList", roleList);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/jsp/user/update.jsp").forward(request, response);
    }

    // 更新用户信息
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        // String oldLoginName = request.getParameter("oldLoginName");
        String loginName = request.getParameter("loginName");
        // String password = request.getParameter("password");
        // String rePassword = request.getParameter("rePassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String telephone = request.getParameter("telephone");
        String roleId = request.getParameter("roleId");
        String address = request.getParameter("address");

        // 数据校验
        if (CharacterUtil.isEmpty(id) || !(36 == id.length())) {
            log.error("Servlet：数据校验失败，id参数不正确，可能为空或长度不符合要求！");
            return;
        }

        UserService userService = new UserService();
        User user = userService.findWithoutRole(id);

        if (null == user) {
            log.error("Servlet：数据校验失败，待更新的用户不存在！");
            return;
        }

        // 如果修改后的登录名不为'admin'，则需要判断是否修改的是admin用户的登录名
        // 增加前一个条件主要是为了修改admin用户的其他属性，否则当修改admin用户时就会进入此if
        if (!"admin".equals(loginName) && "admin".equals(user.getLoginName())) {
            log.error("Servlet：数据校验失败，超级管理员“admin”用户的登录名不可以修改！");
            // response.sendRedirect("");
            return;
        }

        if (CharacterUtil.isEmpty(roleId) || !(36 == roleId.length())) {
            log.error("Servlet：数据校验失败，roleId为空或长度不符合要求！");
            return;
        }

        RoleService roleService = new RoleService();
        Role role = roleService.findWithoutPrivilege(roleId);

        if (null == role) {
            log.error("Servlet：数据校验失败，为用户赋予的角色不存在！");
            return;
        }

        // 不可以修改admin用户的角色
        if ("admin".equals(user.getLoginName()) && !"管理员用户".equals(role.getName())) {
            log.error("Servlet：数据校验失败，超级管理员“admin”用户的角色不可以修改！");
            return;
        }

        if (!ValidateUtil.validateLoginName(loginName)) {
            log.error("Servlet：数据校验失败，登录名不符合要求！");
            // response.sendRedirect("");
            return;
        }

        if (!"男".equals(gender) && !"女".equals(gender)) {
            log.error("Servlet：数据校验失败，性别不符合要求！");
            return;
        }

        if (!CharacterUtil.isEmpty(birthday) && !ValidateUtil.validateDate(birthday)) {
            log.error("Servlet：数据校验失败，出生日期不符合要求！");
            return;
        }

        if (!CharacterUtil.isEmpty(telephone) && !ValidateUtil.validatePhoneNumber(telephone)) {
            log.error("Servlet：数据校验失败，电话号码不符合要求！");
            return;
        }

        if (!CharacterUtil.isEmpty(address) && address.length() > 140) {
            log.error("Servlet：数据校验失败，地址长度不符合要求！");
            return;
        }

        // 封装数据
        user = new User();
        user.setId(id);
        user.setLoginName(loginName);
        user.setUserName(loginName);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setTel(telephone);
        user.setRoleId(roleId);
        user.setAddress(address);

        // 更新数据
        if (userService.update(user)) {
            response.sendRedirect(request.getContextPath() + "/servlet/user/list");
        }
        else {
            log.error("Servlet：修改（更改）用户信息失败！");
        }
    }

    // 修改密码
    public void updatePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        // String loginName = request.getParameter("loginName"); // 被修改密码的用户
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String rePassword = request.getParameter("rePassword");

        // 数据校验
        if (CharacterUtil.isEmpty(id) || !(36 == id.length())) {
            log.error("Servlet：数据校验失败，id参数不正确，可能为空或长度不符合要求！");
            return;
        }

        UserService userService = new UserService();
        User user = userService.findWithoutRole(id);

        if (null == user) {
            log.error("Servlet：数据校验失败，待修改密码的用户不存在！");
            return;
        }

        // 获取当前登录用户
        User currentUser = (User) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

        if ("admin".equals(user.getLoginName()) && !"admin".equals(currentUser.getLoginName())) {
            log.error("Servlet：数据校验失败，超级管理员“admin”用户的密码只能由“admin”用户修改！");
            // response.sendRedirect("");
            return;
        }
        if (!user.getPassword().equals(oldPassword)) {
            log.error("Servlet：数据校验失败，旧密码不正确！");
            // response.sendRedirect("");
            return;
        }
        if (!ValidateUtil.validatePassword(newPassword)) {
            log.error("Servlet：数据校验失败，新密码不符合要求！");
            // response.sendRedirect("");
            return;
        }
        if (!newPassword.equals(rePassword)) {
            log.error("Servlet：数据校验失败，新密码和重复密码不一致！");
            return;
        }
        if (user.getPassword().equals(newPassword)) {
            log.error("Servlet：数据校验失败，新密码和旧密码一致！");
            return;
        }

        // 修改密码
        if (userService.modifyPassword(id, newPassword)) {
            response.sendRedirect(request.getContextPath() + "/servlet/user/list");
        }
        else {
            log.error("Servlet：修改（更改）用户密码失败！");
        }

    }

    // 查询用户列表
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNumStr = request.getParameter("pageNum"); // 当前页码
        String queryLoginName = request.getParameter("queryLoginName"); // 页面上的查询条件（根据登录名进行查询）

        if (null != queryLoginName) {
            // request.getParameter(...)会进行第一次解码
            // 再调用 java.net.URLDecoder.decode(String s, String enc) 方法进行第二次解码
            queryLoginName = URLDecoder.decode(queryLoginName, ConstantUtil.UTF_8);
        }

        int pageNum = 1; // 当前页码（默认为第1页）
        if (CharacterUtil.isNumber(pageNumStr)) {
            pageNum = Integer.parseInt(pageNumStr);
        }

        UserService userService = new UserService();
        // List<User> userList = userService.findAll(); // 查询所有用户
        // 分页查询
        PageBean<User> pageBean = userService.findPagingUsers(pageNum, queryLoginName);

        request.setAttribute("queryLoginName", queryLoginName); // 回显查询条件
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/jsp/user/list.jsp").forward(request, response);
    }

    // 验证登录名是否已经存在
    public void checkLoginName(HttpServletRequest request, HttpServletResponse response) {
        String loginName = request.getParameter("loginName");
        // 为true则表示是登录时的验证，否则是新增或修改用户时的验证
        boolean isLogin = Boolean.parseBoolean(request.getParameter("isLogin"));

        UserService userService = new UserService();
        // 根据登录名判断用户是否存在，存在返回true，否则返回false
        boolean isAvailable = userService.loginNameExists(loginName);

        // 新增或修改用户时的验证，查询后如果登录名不存在则表示该登录名未使用，应返回true
        if (!isLogin) {
            isAvailable = !isAvailable;
        }

        String result = new Gson().toJson(isAvailable);

        WebUtils.writeJson(response, result, ConstantUtil.HTTP_OK);
    }

}
