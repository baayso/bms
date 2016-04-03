package com.baayso.bms.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.privilege.PrivilegeService;

/**
 * 监听器: 从数据库中查询出所有权限
 * 
 * @author ChenFangjie
 * 
 */
public class InitServletContextListener implements ServletContextListener {

    private static final Logger log = Log.get();

    @Override
    public void contextInitialized(ServletContextEvent sec) {
        ServletContext application = sec.getServletContext();

        PrivilegeService service = new PrivilegeService();

        // 准备所有权限的url集合
        List<String> allPrivilegeUrls = service.findAllPrivilegeUrls();
        application.setAttribute("allPrivilegeUrls", allPrivilegeUrls);

        log.info("=== 已向Application作用域中存储所有权限的URL数据 ===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sec) {

    }
}
