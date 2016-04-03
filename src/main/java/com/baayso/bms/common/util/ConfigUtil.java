package com.baayso.bms.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取 Properties配置文件 的工具类
 * 
 * @author ChenFangjie
 * 
 */
public final class ConfigUtil {
    private static final Properties systemProperties = new Properties();
    private static final Properties jdbcProperties = new Properties();

    private static int pageSize = 10; // 每页显示记录数，默认为10条

    static {
        try {
            systemProperties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(ConstantUtil.SYSTEM_PROPERTY_NAME));
            jdbcProperties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(ConstantUtil.JDBC_PROPERTY_NAME));

            // 读取配置文件中的pageSize
            String pageSizeStr = ConfigUtil.getSystemConfigValue(ConstantUtil.SYSTEM_PAGESIZE);
            if (CharacterUtil.isNotEmpty(pageSizeStr) && CharacterUtil.isNumber(pageSizeStr)) {
                int pageSizeTemp = Integer.parseInt(pageSizeStr);
                if (0 < pageSizeTemp) {
                    pageSize = pageSizeTemp;
                }
            }
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // 让工具类彻底不可以实例化
    private ConfigUtil() {
        throw new Error("工具类不可以实例化！");
    }

    /**
     * 根据 key值获取system配置文件中的value
     * 
     * @param key
     * @return
     */
    public static String getSystemConfigValue(String key) {
        return systemProperties.getProperty(key);
    }

    /**
     * 根据 key值获取jdbc配置文件中的value
     * 
     * @param key
     * @return
     */
    public static String getJdbcConfigValue(String key) {
        return jdbcProperties.getProperty(key);
    }

    /** 获取每页显示记录数 */
    public static int getPageSize() {
        return pageSize;
    }

}
