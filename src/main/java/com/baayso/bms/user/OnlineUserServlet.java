package com.baayso.bms.user;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.baayso.bms.common.BaseServlet;
import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.page.PageBean;

/**
 * 在线用户列表Servlet。
 *
 * @author ChenFangjie (2015/9/3 21:50:14)
 * @version 1.1.0
 * @since 1.1.0
 */
public class OnlineUserServlet extends BaseServlet {

    private static final long serialVersionUID = -473631516629326904L;

    private static final Logger log = Log.get();

    // 查询在线用户列表
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

        OnlineUserService userService = new OnlineUserService();
        // 分页查询
        PageBean<OnlineUser> pageBean = userService.findPagingOnlineUsers(pageNum, queryLoginName);

        request.setAttribute("queryLoginName", queryLoginName); // 回显查询条件
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/jsp/user/online.jsp").forward(request, response);
    }

}
