package com.baayso.bms.common.util;

/**
 * 字符串校验工具类
 * 
 * @author ChenFangjie
 * 
 */
public final class ValidateUtil {

    // 让工具类彻底不可以实例化
    private ValidateUtil() {
        throw new Error("工具类不可以实例化！");
    }

    /**
     * 告知此字符串是否匹配给定的正则表达式。
     * 
     * @param regex
     *            用来匹配此字符串的正则表达式
     * @param str
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串匹配给定的正则表达式时，返回 true
     */
    public static boolean validateByRegexp(String str, String regex) {
        if (null != str) {
            return str.matches(regex);
        }
        return false;
    }

    /**
     * 验证登录名。 3-16位的字符串且以字母开头（只可包涵字母，数字，下划线）。
     * 
     * @param loginName
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串满足要求时，返回 true
     */
    public static boolean validateLoginName(String loginName) {
        return ValidateUtil.validateByRegexp(loginName, "^[a-zA-Z][a-zA-Z0-9_]{2,15}$");
    }

    /**
     * 验证姓名。 <br/>
     * 1、可以是汉字或字母，但是不能两者都有； <br/>
     * 2、不能包含任何符号和数字； <br/>
     * 3、允许英文名字中出现空格，但是不能连续出现多个； <br/>
     * 4、中文名不能出现空格。
     * 
     * @param name
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串满足要求时，返回 true
     */
    public static boolean validateName(String name) {
        return ValidateUtil.validateByRegexp(name, "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$");
    }

    /**
     * 验证密码。 <br/>
     * 1、密码不能包含中文,长度在16以内；<br/>
     * 2、可以输入纯数字或纯字母；<br/>
     * 3、可以输入数字和字母组合；<br/>
     * 4、可以输入数字，字母，特殊符号(!@#$%&)组合。
     * 
     * @param password
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串满足要求时，返回 true
     */
    public static boolean validatePassword(String password) {
        return ValidateUtil.validateByRegexp(password, "^[\\dA-Za-z(!@#$%&)]{1,16}$");
    }

    /**
     * 验证日期（要求为：yyyy-mm-dd格式且必须是有效的日期）。
     * 
     * @param date
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串满足要求时，返回 true
     */
    public static boolean validateDate(String date) {
        // "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
        String regex = "^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
        return ValidateUtil.validateByRegexp(date, regex);
    }

    /**
     * 验证中国大陆的电话号码（格式要求为：+86 1xx xxxx xxxx 注：x表示[0-9]的数字）。
     * 
     * @param phoneNumber
     *            需要进行验证的字符串，传入null值将返回 false
     * @return 当且仅当此字符串满足要求时，返回 true
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        // ^\\d{11,}$
        return ValidateUtil.validateByRegexp(phoneNumber, "^\\+86\\s1[0-9]{2}\\s[0-9]{4}\\s[0-9]{4}$");
    }

    /**
     * 验证电子邮件地址。
     * 
     * @param email
     *            需要进行验证的字符串，传入null值将返回 false
     * @return
     */
    public static boolean validateEmail(String email) {
        return ValidateUtil.validateByRegexp(email, "^([a-z][a-z0-9_-]*\\.?[a-z0-9_-]*)*[a-z0-9]@([a-z0-9-]+\\.[a-z]+)+$");
    }

}
