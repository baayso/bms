package com.baayso.bms.user;

import java.io.Serializable;

/**
 * 实体：在线用户信息
 * 
 * @author ChenFangjie(2015年9月2日 下午10:55:49)
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 *
 */
public class OnlineUser implements Serializable {

    private static final long serialVersionUID = -2759133280236852865L;

    private String sessionId; // 用户Session ID
    private String userId; // 用户编号
    private String loginName; // 登录名
    private String userName; // 用户名
    private String clientIp; // 客户端IP
    private String pageUrl; // 用户停留页面
    private long createTime; // 在线用户信息创建时间
    private long lastAccessTime; // 用户最后访问时间

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

}
