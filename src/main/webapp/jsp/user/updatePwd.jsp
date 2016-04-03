<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>修改（更新）密码</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>
</head>

<body>

<div class="list">

    <div class="add">
        <span class="font_size_13">
            <a href="javascript:void(0);" class="label label-blue">修改（更新）密码 &gt;&gt;</a>
        </span>
    </div>

    <div>
        <form class="form form-stacked" method="post" action="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=UPDATE_PWD">
            <fieldset>
            	<%--
                <label>用户编号：</label>
                <input type="text" placeholder="用户编号" value="${ param.id }" disabled/>
                --%>

                <input type="hidden" name="id" value="${ param.id }" />

                <label>登录名称：</label>
                <input type="text" placeholder="登录名称" value="${ param.loginName }" disabled/>
                <%-- <input type="hidden" name="loginName" value="${ param.loginName }" /> --%>

                <label><span style="color: red;">*</span>&nbsp;旧密码：</label>
                <input type="password" name="oldPassword" placeholder="旧密码"/>

                <label><span style="color: red;">*</span>&nbsp;新密码：</label>
                <input type="password" name="newPassword" placeholder="新密码"/>
                <span style="color: red;">新密码不能为空！</span>

                <label><span style="color: red;">*</span>&nbsp;确认密码：</label>
                <input type="password" name="rePassword" placeholder="确认密码"/>

                <br/>
                <br/>
                <span>
                    <button type="submit" class="button-blue">确认修改</button>
                    <a href="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=UPDATE_UI&id=${param.id}" class="button button-red">返回</a>
                </span>
            </fieldset>
        </form>
    </div>

</div>

</body>

</html>
