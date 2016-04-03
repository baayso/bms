package com.baayso.bms.user;

import java.util.List;

import com.baayso.bms.common.util.CharacterUtil;
import com.baayso.bms.common.util.ConfigUtil;
import com.baayso.bms.common.util.ConstantUtil;
import com.baayso.bms.common.util.SQLHelper;
import com.baayso.bms.page.PageBean;

/**
 * 业务：在线用户信息
 * 
 * @author ChenFangjie(2015年9月3日 下午4:11:19)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class OnlineUserService {

    private OnlineUserDao dao = new OnlineUserDao();

    public boolean add(OnlineUser onlineUser) {
        if (0 < dao.add(onlineUser)) {
            return true;
        }

        return false;
    }

    public boolean delete(String sessionId) {
        if (0 < dao.delete(sessionId)) {
            return true;
        }

        return false;
    }

    public boolean delete(List<String> sessionIds) {
        if (0 < dao.delete(sessionIds)) {
            return true;
        }

        return false;
    }

    public boolean update(OnlineUser onlineUser) {
        if (0 < dao.update(onlineUser)) {
            return true;
        }

        return false;
    }

    public OnlineUser find(String sessionId) {
        return dao.find(sessionId);
    }

    public PageBean<OnlineUser> findPagingOnlineUsers(int pageNum, String loginName) {
        int pageSize = 10; // 每页显示记录数，默认为10条
        // 读取配置文件中的pageSize
        String pageSizeStr = ConfigUtil.getSystemConfigValue(ConstantUtil.SYSTEM_PAGESIZE);

        if (!CharacterUtil.isEmpty(pageSizeStr) && CharacterUtil.isNumber(pageSizeStr)) {
            int pageSizeTemp = Integer.parseInt(pageSizeStr);
            if (0 < pageSizeTemp) {
                pageSize = pageSizeTemp;
            }
        }

        // 生成SQL语句的工具类（此处接受一些设置）
        SQLHelper sqlHelper = new SQLHelper("t_online_user") //
                .addWhere(null != loginName && 0 < loginName.length(), "loginName LIKE ?", "%" + loginName + "%") //
                .addOrderBy("createTime", "ASC");

        // SQLHelper sqlHelper = new SQLHelper("t_online_user", "ou") //
        // .addWhere(CharacterUtil.isNotEmpty(loginName), "ou.loginName LIKE ?", "%" + loginName + "%") //
        // .addOrderBy("ou.createTime", "ASC");

        // 查询指定页的数据列表
        PageBean<OnlineUser> pageBeans = dao.findPagingOnlineUsers(pageNum, pageSize, sqlHelper);

        return pageBeans;
    }

}
