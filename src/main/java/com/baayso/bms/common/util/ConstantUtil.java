package com.baayso.bms.common.util;

/**
 * 常量
 * 
 * @author ChenFangjie
 * 
 */
public final class ConstantUtil {

    // 让工具类彻底不可以实例化
    private ConstantUtil() {
        throw new Error("工具类不可以实例化！");
    }

    /** 系统配置文件 */
    public static final String SYSTEM_PROPERTY_NAME = "system.properties";
    /** 分页时每页显示的记录数 */
    public static final String SYSTEM_PAGESIZE = "pageSize";

    /** 数据库配置文件 */
    public static final String JDBC_PROPERTY_NAME = "jdbc.properties";
    /** 数据库驱动类名 */
    public static final String JDBC_DRIVER_CLASS_NAME = "driverClassName";
    /** 连接数据库的URL */
    public static final String JDBC_URL = "url";
    /** 连接数据库的用户名 */
    public static final String JDBC_USERNAME = "username";
    /** 连接数据库的密码 */
    public static final String JDBC_PASSWORD = "password";
    /** 数据库连接初始连接数 */
    public static final String JDBC_INIT_SIZE = "initSize";
    /** 数据库连接最大连接数 */
    public static final String JDBC_MAX_ACTIVE = "maxActive";

    /** 当前登录用户 */
    public static final String CURRENT_USER = "currentUser";

    /** 长时间格式 */
    public static final String LONG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
