<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>欢迎页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/welcome.css"/>
</head>

<body>
<div>
    <div class="welcome">
        <i class="icon-save"></i>

        <p>欢迎使用账单管理系统</p>
    </div>
</div>
</body>
</html>
