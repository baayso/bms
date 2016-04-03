<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>menu</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/menu.css"/>

    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.js"></script>

    <script type="text/javascript">
        function select(obj) {
            var $li = $(obj).parent();
            if ("active" != $li.attr("class")) {
                $(".active").removeClass("active");
                $li.addClass("active");
            }
        }

        function quitSystem() {
            if (confirm("确认退出系统吗？")) {
                // 如果当前窗口不存在上级，那么window.parent也不会为null，而是等于window。所以判断时使用!= window
                if (window.parent != window) {
                    window.parent.location.href = "${ pageContext.request.contextPath }/jsp/user/UserServlet?method=LOGOUT";
                }
            }
        }
    </script>
</head>

<body>
<div>
    <nav class="menu-stacked">
        <ul>
            <li class="active">
                <a href="welcome.jsp" target="main_iframe" onclick="select(this);"><i class="icon-home"></i>欢迎页面</a>
            </li>

			<%-- 列出顶级菜单（权限） --%>
            <c:forEach var="privilege" items="${ sessionScope.currentUser.role.privileges }">
            	<c:if test="${ privilege.parent eq null }">
            		<li>
						<a href="${ pageContext.request.contextPath }/jsp/${ privilege.url }" target="main_iframe" onclick="select(this);"><i class="icon-list-alt"></i>${ privilege.name }</a>
            		</li>
            	</c:if>
            </c:forEach>

            <li>
                <a href="javascript:void(0);" target="main_iframe" onclick="quitSystem();"><i class="icon-off"></i>退出系统</a>
            </li>
        </ul>
    </nav>
</div>
</body>

</html>
