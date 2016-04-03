<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>首页</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/index.css"/>
</head>

<body>
<div id="header">
    <iframe scrolling="no" src="${ pageContext.request.contextPath }/jsp/home/header.jsp" frameborder="0" width="100%" height="100%"></iframe>
</div>

<div id="bd">

    <div id="menu">
        <iframe scrolling="no" src="${ pageContext.request.contextPath }/jsp/home/menu.jsp" frameborder="0" width="100%" height="100%"></iframe>
    </div>

    <!--
    <div id="toolbar">
        <ul>
            <li><a href="javascript:void(0);">退出</a></li>
            <li><a href="javascript:void(0);">关于</a></li>
            <li><a href="javascript:void(0);">帮助</a></li>
            <li id="datetime">当前时间：</li>
            <li>当前用户：test_test</li>
        </ul>
    </div>
    -->

    <div id="main">

        <iframe scrolling="yes" id="main_iframe" name="main_iframe" src="${ pageContext.request.contextPath }/jsp/home/welcome.jsp" frameborder="0" width="100%" height="100%"></iframe>

        <!--
        <div id="contents">
            <div id="feature" class="feature">
                <div class="hd">
                    <h2>div仿框架布局Version2.0的特征：</h2>
                    <span>update: 2008.09.22</span>
                </div>
                <div class="bd">
                    <h3>优点：</h3>
                    <ol>
                        <li>以div代替frameset，用css实现仿框架布局</li>
                        <li>在web标准模式Standard Mode下运行</li>
                        <li>兼容IE6,7,8; Firefox; Chrome; Safari; Opera浏览器，没被列入的浏览器未测试</li>
                        <li>内容栏区域可以允许出现宽为100%或超100%的元素，如：
                            <p>&lt;div class=&quot;main&quot;&gt;&lt;div style=&quot;width:100%;&quot;&gt;&lt;/div&gt;&lt;/div&gt;</p>

                            <p>&lt;div class=&quot;main&quot;&gt;&lt;iframe style=&quot;width:100%;&quot;&gt;&lt;/iframe&gt;&lt;/div&gt;</p>
                        </li>
                        <li>适用于：后台；邮箱等地方</li>
                    </ol>
                </div>
                <div class="ft">
                    <a href="http://blog.doyoe.com/" title="去css探索之旅浏览更多div布局文章">More</a>
                </div>
            </div>
        </div>
        -->

    </div>
</div>

<div id="footer">
    版本号：1.02.03.20130807.121&nbsp;&nbsp;
</div>

<noscript>
    抱歉！该浏览器不支持javascript!!!
    <br/>
    抱歉！該流覽器不支持javascript!!!
    <br/>
    Sorry!This browser not support javascript!!!
</noscript>

</body>
</html>