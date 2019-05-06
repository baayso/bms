package com.baayso.bms.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;

/**
 * Base Servlet.
 *
 * @author ChenFangjie (2019/5/5 12:57)
 * @since 1.2.0
 */
public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 6491990636655474640L;

    private static final Logger log = Log.get();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        // 1.获取请求路径
        String uri = req.getRequestURI();
        log.debug("request uri: {}", uri);

        // 2.从uri中获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/'));
        log.debug("method name: {}", methodName);

        try {
            // 3.通过反射获取Method对象
            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // 关闭访问控制安全检查以提高反射性能
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            // 4.执行Servlet中具体的业务方法
            method.invoke(this, req, resp);
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
