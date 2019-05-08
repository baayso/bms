<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>用户列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>

    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.js"></script>

    <script type="text/javascript">

    	function query() {
    		var $queryLoginName = $("#queryLoginName").val();
    		// 进行第一次编码
    		$queryLoginName = encodeURI($queryLoginName);
    		// 进行第二次编码
    		$queryLoginName = encodeURI($queryLoginName);

    		window.location.href = "${ pageContext.request.contextPath }/servlet/user/list?queryLoginName=" + $queryLoginName;
    	}

    </script>

</head>

<body>
<div class="list">
    <div class="query">
    	<%-- 需以post方式提交（分页的gotoPage(pageNum)会使用到此from），如果使用get方式会出现乱码，但可以encodeURI()后以get方式传递，服务器在解码 --%>
        <form class="form" method="post" name="queryForm" action="${ pageContext.request.contextPath }/servlet/user/list">
            <fieldset>
                <label>登录名：</label>
                <label>
                    <input type="text" id="queryLoginName" name="queryLoginName" value="${ requestScope.queryLoginName }" placeholder="登录名">
                </label>
                <label>
                    <button type="button" class="button-blue" onclick="query();">查询</button>
                    <%-- <button type="submit" class="button-blue">查询</button> --%>
                </label>
            </fieldset>
        </form>
    </div>

    <div class="add">
        <span class="font_size_13">
            <a href="javascript:void(0);" class="label label-blue">用户管理 &gt;&gt;</a>
        </span>
        <span class="float_right">
            <a href="${ pageContext.request.contextPath }/servlet/user/addUI" class="button button-green">新增用户</a>
        </span>
    </div>

    <div class="col-1-1 data">
        <table class="table table-striped">
            <!-- <caption>Table样式效果<code>class="table table-striped"</code></caption> -->
            <thead>
	            <tr>
	                <th>序号</th>
	                <th>登录名</th>
	                <th>性别</th>
	                <th>出生日期</th>
	                <th>电话号码</th>
	                <th>地址</th>
	                <th>权限角色</th>
	            </tr>
            </thead>
            <tbody>
            	<c:forEach varStatus="status" var="user" items="${ requestScope.pageBean.recordList }">
            		<tr>
		                <td>${status.count}</td>
		                <td><a href="${ pageContext.request.contextPath }/servlet/user/updateUI?id=${ user.id }">${ user.loginName }</a></td>
		                <td>${ user.gender }</td>
		                <td>${ user.birthday }</td>
		                <td>${ user.tel }</td>
		                <td>${ user.address }</td>
		                <td>${ user.role.name }</td>
	            	</tr>
            	</c:forEach>
            </tbody>
        </table>
    </div>

    <%-- <form method="post" name="pagingForm" action="${ pageContext.request.contextPath }/servlet/user/list"></form> --%>

    <%-- include分页公共代码 --%>
    <%@ include file="/jsp/common/pageView.jspf" %>

</div>
</body>

</html>
