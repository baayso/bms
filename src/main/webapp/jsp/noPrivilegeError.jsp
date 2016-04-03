<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>没有权限</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/font-awesome/font-awesome.css"/>
    
    <script type="text/javascript">

 		// 在被嵌套时就刷新上级窗口
		// 如果当前窗口不存在上级，那么window.parent也不会为null，而是等于window。所以判断时使用!= window
		if(window.parent != window){
			// window.parent.location.reload(true);
			window.parent.location.href = "${ pageContext.request.contextPath }/jsp/noPrivilegeError.jsp";
		}
    
    </script>

    <style type="text/css">
        body {
            background-color: #FFFFFF;
            color: #333333;
            font-family: "Microsoft Yahei", "Tahoma", "SimSun";
            font-size: 14px;
            line-height: 20px;
            margin: 0;
            padding: 0;
        }

        .http-error {
            color: #444444;
            margin-top: 5em;
            text-align: center;
        }

        .http-error i {
            font-size: 3em;
            line-height: 0.75em;
            text-shadow: 1px 1px 0 #FFFFFF;
        }

        .http-error .info {
            font-size: 2em;
            line-height: 1.5em;
            margin-bottom: 1em;
        }

        .http-error p {
            margin: 0;
        }

        a {
            color: #0088CC;
            text-decoration: none;
        }

        a:hover {
            color: #005580;
            text-decoration: none;
        }
    </style>

</head>
<body>
<div class="http-error">
    <h1>出错了！</h1>

    <p class="info">您没有权限访问此页面！</p>

    <p><i class="icon-home"></i></p>

    <p><a href="${ pageContext.request.contextPath }/jsp/home/index.jsp">点这里返回首页</a></p>
</div>
</body>
</html>