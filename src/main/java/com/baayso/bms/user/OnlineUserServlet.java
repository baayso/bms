package com.baayso.bms.user;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.page.PageBean;

/**
 * 在线用户列表Servlet。
 * 
 * @author ChenFangjie(2015年9月3日 下午9:50:14)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class OnlineUserServlet extends HttpServlet {

    private static final long serialVersionUID = 7654152633544452073L;

    private static final Logger logger = LoggerFactory.getLogger(OnlineUserServlet.class);

    private static final String METHOD_LIST = "LIST";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String method = request.getParameter("method");

        logger.debug("Servlet：请求参数：method=" + method);

        if (null == method) {
            logger.error("请求地址缺少method参数！");
            return;
        }
        else if (METHOD_LIST.equals(method)) {
            this.list(request, response);
        }
        else {
            logger.error("请求地址的method参数不正确！");
            return;
        }
    }

    // 查询在线用户列表
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pageNumStr = request.getParameter("pageNum"); // 当前页码
        String queryLoginName = request.getParameter("queryLoginName"); // 页面上的查询条件（根据登录名进行查询）

        if (null != queryLoginName) {
            // request.getParameter(...)会进行第一次解码
            // 再调用 java.net.URLDecoder.decode(String s, String enc) 方法进行第二次解码
            queryLoginName = URLDecoder.decode(queryLoginName, "UTF-8");
        }

        int pageNum = 1; // 当前页码（默认为第1页）
        if (CharacterUtil.isNumber(pageNumStr)) {
            pageNum = Integer.parseInt(pageNumStr);
        }

        OnlineUserService userService = new OnlineUserService();
        // 分页查询
        PageBean<OnlineUser> pageBean = userService.findPagingOnlineUsers(pageNum, queryLoginName);

        request.setAttribute("queryLoginName", queryLoginName); // 回显查询条件
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("online.jsp").forward(request, response);
    }

}
