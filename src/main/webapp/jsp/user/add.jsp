<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>新增用户</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>

    <%@ include file="/jsp/common/formValidate.jspf" %>

	<script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.js"></script>
    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.maskedinput.js"></script>

    <script type="text/javascript">
		jQuery(function($) {
			$("#birthday").mask("9999-99-99", {placeholder:"_"});
			$("#telephone").mask("+86 999 9999 9999", {placeholder:"_"});
		});
	</script>

</head>

<body>

<div class="list">

    <div class="add">
        <span class="font_size_13">
            <a href="javascript:void(0);" class="label label-blue">新增用户 &gt;&gt;</a>
        </span>
    </div>

    <div class="col-1-1">
        <form class="form form-stacked" method="post" action="${ pageContext.request.contextPath }/servlet/user/add">
            <fieldset>

                <div class="grid">
                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;登录名称：</label>
                        <input type="text" name="loginName" placeholder="登录名称"/>
                        <span style="color: red;">用户名不能为空！</span>
                    </div>

                    <div class="col-1-2">
                        <label>用户性别：</label>
                        <select name="gender" class="width_193">
                            <option value="男">男</option>
                            <option value="女">女</option>
                        </select>
                    </div>

                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;用户密码：</label>
                        <input type="password" name="password" placeholder="密码"/>
                    </div>

                    <div class="col-1-2">
                        <label>出生日期：</label>
                        <input type="text" id="birthday" name="birthday" placeholder="出生日期"/>
                    </div>

                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;确认密码：</label>
                        <input type="password" name="rePassword" placeholder="确认密码"/>
                    </div>

                    <div class="col-1-2">
                        <label>电话号码：</label>
                        <input type="text" id="telephone" name="telephone" placeholder="电话号码"/>
                    </div>

                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;权限角色：</label>
                        <%-- 列出角色信息 --%>
                        <c:forEach var="role" items="${ requestScope.roleList }">
                        	<label class="width_193">${ role.name }<input type="radio" name="roleId" value="${ role.id }"/></label>
                        </c:forEach>
                    </div>

                    <div class="col-1-2">
                        <label>用户地址：</label>
                        <textarea name="address" rows="2" class="x1-large" placeholder="用户地址"></textarea>
                    </div>

                    <div class="col-1-1 width_830">
                        <button type="submit" class="button-blue">提交数据</button>
                        <a href="${ pageContext.request.contextPath }/servlet/user/list" class="button button-red">返回</a>
                    </div>
                </div>

            </fieldset>
        </form>
    </div>

</div>

</body>

</html>