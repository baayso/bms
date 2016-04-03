package com.baayso.bms.user.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;
import com.baayso.bms.common.util.Tasks;
import com.baayso.bms.page.PageBean;
import com.baayso.bms.user.OnlineUser;
import com.baayso.bms.user.OnlineUserService;

/**
 * 负责启动一条后台线程，此后台线程将会定期检查在线记录，并删除那些长时间没有重新请求过的记录。
 * 
 * @author ChenFangjie(2015/9/2 20:57:34)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class OnlineListener implements ServletContextListener {

    private static final Logger log = Log.get();

    // 用户访问超时时间，超过该时间（20分钟）没有访问本站即认为用户已经离线
    private final long MAX_MILLIS = 20L * 60 * 1000;

    // 数据检查时间（10分钟）
    private final long PERIOD_SECONDS = 10L * 60;

    private OnlineUserService service = new OnlineUserService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Tasks.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<String> sessionIds = new ArrayList<String>();

                int currentPage = 1;
                long totalPage = 0L;

                do {
                    PageBean<OnlineUser> pageBean = service.findPagingOnlineUsers(currentPage, null);

                    for (int i = 0; i < pageBean.getRecordList().size(); i++) {
                        OnlineUser onlineUser = pageBean.getRecordList().get(i);
                        if (System.currentTimeMillis() - onlineUser.getLastAccessTime() > MAX_MILLIS) {
                            // 将需要删除的数据的sessionId加入集合
                            sessionIds.add(onlineUser.getSessionId());
                        }
                    }

                    currentPage = pageBean.getCurrentPage() + 1;
                    totalPage = pageBean.getTotalPage();
                }
                while (currentPage <= totalPage);

                // 检查是否有需要删除的数据
                if (!sessionIds.isEmpty()) {
                    // 删除所有“用户访问时间超时”的在线用户信息
                    boolean success = service.delete(sessionIds);
                    if (!success) {
                        log.error("Listener：删除所有“用户访问时间超时”的在线用户信息失败！");
                    }
                }
            }
        }, PERIOD_SECONDS);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
