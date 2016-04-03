package com.baayso.bms.user.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.common.util.WebUtils;
import com.baayso.bms.user.OnlineUser;
import com.baayso.bms.user.OnlineUserService;
import com.baayso.bms.user.User;

/**
 * 负责过滤每次用户请求，每次用户请求到达时，如果是新的会话，将相关信息存入数据库；<br>
 * 如果是老的会话，则更新数据库中已有的在线记录。
 * 
 * @author ChenFangjie(2015年9月4日 下午4:07:42)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class RequestFilter implements Filter {

    private static final Logger log = Log.get();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 获取正在访问的url
        String url = WebUtils.getUrl(httpRequest);

        // 不记录除.jsp和servlet以外的路径
        if ((url.indexOf('.') > -1) && !url.endsWith(".jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession();
        String sessionId = session.getId(); // 获取Session ID

        // 获取访问的IP
        String ip = WebUtils.getRealIp(httpRequest);

        User currentUser = (User) session.getAttribute(ConstantUtil.CURRENT_USER);

        // 未登录用户做游客处理
        boolean hasNull = currentUser == null;
        String userId = hasNull ? "游客" : currentUser.getId();
        String loginName = hasNull ? "游客" : currentUser.getLoginName();

        OnlineUserService service = new OnlineUserService();

        OnlineUser onlineUser = service.find(sessionId);

        if (onlineUser != null) {
            onlineUser.setUserId(userId);
            onlineUser.setLoginName(loginName);
            onlineUser.setUserName(loginName);
            onlineUser.setClientIp(ip);
            onlineUser.setPageUrl(url);
            onlineUser.setLastAccessTime(System.currentTimeMillis());

            boolean success = service.update(onlineUser);

            if (!success) {
                log.error("Filter：修改（更新） 在线用户信息失败！");
            }
        }
        else {
            long currentTimeMillis = System.currentTimeMillis();

            onlineUser = new OnlineUser();
            onlineUser.setSessionId(sessionId);
            onlineUser.setUserId(userId);
            onlineUser.setLoginName(loginName);
            onlineUser.setUserName(loginName);
            onlineUser.setClientIp(ip);
            onlineUser.setPageUrl(url);
            onlineUser.setCreateTime(currentTimeMillis);
            onlineUser.setLastAccessTime(currentTimeMillis);

            boolean success = service.add(onlineUser);

            if (!success) {
                log.error("Filter：新增在线用户信息失败！");
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
