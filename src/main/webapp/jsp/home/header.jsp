<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>header</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/header.css"/>
</head>

<body>
<div>
    <div class="logo">
        <!-- <a href="javascript:void(0);"><i class="icon-home"></i>账单管理系统</a> -->
        <i class="icon-leaf"></i>账单管理系统
    </div>
    <div class="headerTitle">
        欢迎您，${ sessionScope.currentUser.userName }！
    </div>
</div>
</body>
</html>