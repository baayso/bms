package com.baayso.bms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.common.util.WebUtils;
import com.baayso.bms.user.User;

/**
 * 过滤器：检查权限
 * 
 * @author ChenFangjie
 * 
 */
public class CheckPrivilegeFilter implements Filter {

    private static final Logger log = Log.get();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 获取当前登录用户
        User currentUser = (User) request.getSession().getAttribute(ConstantUtil.CURRENT_USER);

        String url = WebUtils.getUrl(request);

        log.info("权限过滤器，客户端请求地址: " + url);

        if (null == currentUser) { // 用户未登录
            // 去掉查找到第一个"&"符号后的字符
            int index = url.indexOf("&");
            if (index > 0) {
                url = url.substring(0, index);
            }

            if (url.endsWith(".bmp") //
                    || url.endsWith(".gif") //
                    || url.endsWith(".png") //
                    || url.endsWith(".jpg") //
                    || url.endsWith(".jpeg") //
                    || url.endsWith(".js") //
                    || url.endsWith(".css") //
                    || url.endsWith("login.jsp") //
                    || url.endsWith("method=LOGIN") //
                    || url.endsWith("method=CHECK_LOGIN_NAME")) { // 登录操作（未登录的用户进行登录操作）
                chain.doFilter(req, resp); // 正常执行
            }
            else {
                // 执行非登录操作，转到登录页面
                response.sendRedirect(request.getContextPath() + "/jsp/home/login.jsp");
            }
        }
        else {
            // 对于已登录的用户，需要判断其是否拥有权限
            if (currentUser.hasPrivilegeByUrl(url, request)) {
                // 有权限，正常执行
                chain.doFilter(req, resp);
            }
            else {
                // 没有权限，转到提示页面
                log.info("权限过滤器，登录用户：" + currentUser.getLoginName() + "访问了没有权限的请求地址，请示地址为：" + url);
                response.sendRedirect(request.getContextPath() + "/jsp/noPrivilegeError.jsp");
            }
        }

    }

    @Override
    public void destroy() {

    }

}
