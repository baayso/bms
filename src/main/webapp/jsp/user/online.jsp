<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>在线用户列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>

    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.js"></script>
    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/js/common.js"></script>

    <script type="text/javascript">

    	function query() {
    		var $queryLoginName = $("#queryLoginName").val();
    		// 进行第一次编码
    		$queryLoginName = encodeURI($queryLoginName);
    		// 进行第二次编码
    		$queryLoginName = encodeURI($queryLoginName);

    		window.location.href = "${ pageContext.request.contextPath }/jsp/user/OnlineUserServlet?method=LIST&queryLoginName=" + $queryLoginName;
    	}

    </script>

</head>

<body>
<div class="list">
    <div class="query">
    	<%-- 需以post方式提交（分页的gotoPage(pageNum)会使用到此from），如果使用get方式会出现乱码，但可以encodeURI()后以get方式传递，服务器在解码 --%>
        <form class="form" method="post" name="queryForm" action="${ pageContext.request.contextPath }/jsp/user/OnlineUserServlet">
            <fieldset>
                <label>登录名：</label>
                <label>
                    <input type="hidden" name="method" value="LIST">
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
            <a href="javascript:void(0);" class="label label-blue">在线用户列表 &gt;&gt;</a>
        </span>
    </div>

    <div class="col-1-1 data">
        <table class="table table-striped">
            <!-- <caption>Table样式效果<code>class="table table-striped"</code></caption> -->
            <thead>
	            <tr>
	                <th>序号</th>
	                <th>登录名</th>
	                <th>IP</th>
	                <th>停留页面</th>
	                <th>首次访问时间</th>
	                <th>最后访问时间</th>
	            </tr>
            </thead>
            <tbody>
            	<c:forEach varStatus="status" var="onlineUser" items="${ requestScope.pageBean.recordList }">
            		<tr>
		                <td>${status.count}</td>
		                <td>
                            <c:choose>
                                <c:when test="${ '游客' == onlineUser.userId }">
                                    ${ onlineUser.loginName }
                                </c:when>
                                <c:otherwise>
                                    <a href="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=UPDATE_UI&id=${ onlineUser.userId }">${ onlineUser.loginName }</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
		                <td>${ onlineUser.clientIp }</td>
		                <td>${ onlineUser.pageUrl }</td>
		                <td>
                            <script type="text/javascript">
                                var time = getLocalTime(${ onlineUser.createTime });
                                document.write(time);
                            </script>
                        </td>
		                <td>
                            <script type="text/javascript">
                                var time = getLocalTime(${ onlineUser.lastAccessTime });
                                document.write(time);
                            </script>
                         </td>
	            	</tr>
            	</c:forEach>
            </tbody>
        </table>
    </div>

    <%-- <form method="post" name="pagingForm" action="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=LIST"></form> --%>

    <%-- include分页公共代码 --%>
    <%@ include file="/jsp/common/pageView.jspf" %>

</div>
</body>

</html>
