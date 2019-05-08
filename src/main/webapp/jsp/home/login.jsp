<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>登录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/login.css"/>

    <%@ include file="/jsp/common/formValidate.jspf" %>

    <script type="text/javascript">

   		$(document).ready(function(){
    		$("#loginName").focus();
    	});

		// 在被嵌套时就刷新上级窗口
		// 如果当前窗口不存在上级，那么window.parent也不会为null，而是等于window。所以判断时使用!= window
		if(window.parent != window){
			window.parent.location.reload(true);
		}
    
    </script>

</head>
<body>
<div>
    <div class="loginDiv">
        <form id="loginForm" name="loginForm" class="form form-aligned" method="post" action="${ pageContext.request.contextPath }/servlet/user/login">
            <fieldset>
                <legend class="textBold">用户登录</legend>
                <div class="control">
                    <label>登录名</label>
                    <input type="text" id="loginName" name="loginName" maxlength="16" placeholder="登录名"/>
                    <!-- <span style="color: red;">登录名不能为空！</span> -->
                </div>
                <div class="control">
                    <label>密码</label>
                    <input type="password" id="password" name="password" maxlength="16" placeholder="密码"/>
                </div>
                <div class="control">
                    <label></label>
                    <label>是否记住密码？<input type="checkbox" id="" name=""></label>
                </div>
                <div class="control">
                    <label></label>
                    <button type="submit" class="button-blue">登陆</button>
                    <em><a href="javascript:void(0);">忘记密码？</a></em>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>