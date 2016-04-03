package com.baayso.bms.user.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.user.OnlineUser;
import com.baayso.bms.user.OnlineUserService;
import com.baayso.bms.user.User;

/**
 * 负责监听每次用户请求，每次用户请求到达时，如果是新的会话，将相关信息存入数据库；<br>
 * 如果是老的会话，则更新数据库中已有的在线记录。
 * 
 * @author ChenFangjie(2015/9/2 20:52:42)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class RequestListener implements ServletRequestListener {

    private static final Logger log = Log.get();

    // 当用户请求结束被销毁时触发该方法
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

    // 当用户请求到达被初始化时触发该方法
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        HttpSession session = request.getSession();
        String sessionId = session.getId(); // 获取Session ID

        // 获取访问的IP和正在访问的页面
        String ip = request.getRemoteAddr();
        String url = null;
        if (null != request.getQueryString()) {
            url = String.format("%s?%s", request.getRequestURI(), request.getQueryString());
        }
        else {
            url = request.getRequestURI();
        }

        if ((url.indexOf('.') > -1) && !url.endsWith(".jsp")) {
            return;
        }

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
            onlineUser.setPageUrl(url);
            onlineUser.setLastAccessTime(System.currentTimeMillis());

            boolean success = service.update(onlineUser);

            if (!success) {
                log.error("Listener：修改（更新） 在线用户信息失败！");
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
                log.error("Listener：新增在线用户信息失败！");
            }
        }

    }

}
