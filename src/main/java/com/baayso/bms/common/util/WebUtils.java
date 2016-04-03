package com.baayso.bms.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;

/**
 * Miscellaneous utilities for web applications.
 * 
 * @author ChenFangjie
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 * 
 */
public final class WebUtils {

    private static final Logger log = Log.get();

    // 让工具类彻底不可以实例化
    private WebUtils() {
        throw new Error("工具类不可以实例化！");
    }

    /**
     * 获取客户端IP地址。
     * 
     * @param request
     *            http servlet request
     * @return 客户端IP
     * 
     * @since 1.1.0
     * @version 1.1.0
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 将json字符串返回给客户端。
     * 
     * @param response
     *            {@linkplain javax.servlet.http.HttpServletResponse}
     * @param json
     *            JSON字符串
     * @param httpStatusCode
     *            HTTP状态码
     * 
     * @since 1.1.0
     * @version 1.1.0
     */
    public static void writeJson(HttpServletResponse response, String json, int httpStatusCode) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setStatus(httpStatusCode);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(json);
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 获取客户端访问路径，且包含查询字符串（如果有）。
     * 
     * @param request
     *            http servlet request
     * @return 访问路径
     * 
     * @since 1.1.0
     * @version 1.1.0
     */
    public static String getUrl(HttpServletRequest request) {
        // 获取正在访问的url
        String url = null;

        if (null != request.getQueryString()) {
            url = String.format("%s?%s", request.getRequestURI(), request.getQueryString());
        }
        else {
            url = request.getRequestURI();
        }

        return url;
    }

}
