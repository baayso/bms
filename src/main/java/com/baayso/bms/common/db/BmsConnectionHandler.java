package com.baayso.bms.common.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 此类使用动态代理技术生成代理类 。<br />
 * 代理类重写了Connection.close()方法。<br />
 * 重写后的close()方法不会在关闭数据库连接，而是将数据库连接释放回连接池。
 * 
 * @author ChenFangjie
 * 
 */
public class BmsConnectionHandler implements InvocationHandler {

	private Connection realConn;
	private Connection warpedConn;

	private BmsDataSource dataSource;

	BmsConnectionHandler(BmsDataSource dataSource) {
		this.dataSource = dataSource;
	}

	// 返回动态生成的代理类
	Connection bind(Connection realConn) {
		this.realConn = realConn;
		this.warpedConn = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Connection.class },
				this);

		return warpedConn;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// 拦截并重写close()方法
		if ("close".equals(method.getName())) {
			this.dataSource.free(this.warpedConn);
			return null;
		}

		// 其它方法（除close()以外的方法）正常调用
		return method.invoke(this.realConn, args);
	}

}
