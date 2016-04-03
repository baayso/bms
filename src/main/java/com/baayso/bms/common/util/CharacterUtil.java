package com.baayso.bms.common.util;

/**
 * 字符串工具类
 * 
 * @author ChenFangjie
 * 
 */
public final class CharacterUtil {

    // 让工具类彻底不可以实例化
    private CharacterUtil() {
        throw new Error("工具类不可以实例化！");
    }

    /**
     * 判断给定的字符串是否为空。如果为空返回true；否则返回false。
     * 
     * @param str
     *            给定的字符串
     * @return 为空返回true；否则返回false
     */
    public static boolean isEmpty(String str) {
        if (null == str || str.trim().isEmpty()) { // "".equals(str.trim()) 0 == str.trim().length()
            return true;
        }

        return false;
    }

    /**
     * 判断给定的字符串是否不为空。如果不为空返回true；否则返回false。
     * 
     * @param str
     *            给定的字符串
     * @return 不为空返回true；否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断给定的字符串是否为数字。是数字返回true；否则返回false。
     * 
     * @param str
     *            给定的字符串
     * @return 是数字返回true；否则返回false
     */
    public static boolean isNumber(String str) {
        if (CharacterUtil.isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

}
