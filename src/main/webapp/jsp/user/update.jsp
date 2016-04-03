<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
--%>

<!DOCTYPE html>

<html lang="zh-cn">
<head>
    <title>查看用户</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="author" content="陈芳杰（baayso@qq.com）"/>

    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/lib/quick.css"/>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/jsp/css/public.css"/>
    <script type="text/javascript" src="${ pageContext.request.contextPath }/jsp/lib/jquery.js"></script>

    <script type="text/javascript">

        function enableEdit() {
            var $btnEdit = $("#btnEdit");
            var $btnSave = $("#btnSave");
            // var $id = $("#id");
            var $loginName = $("#loginName");
            var $gender = $("#gender");
            var $birthday = $("#birthday");
            var $roleIdArray = $(":radio[name='roleId']"); // 获取的是一个数组
            var $telephone = $("#telephone");
            var $address = $("#address");

            var hiddenLoginNameValue = $("#hiddenLoginName").val();

            if ("编辑" == $btnEdit.text()) {
                // 将页面上的控件置为可用
                $loginName.attr("disabled", false);
                $gender.attr("disabled", false);
                $birthday.attr("disabled", false);
                $telephone.attr("disabled", false);
                $address.attr("disabled", false);
                // 不允许修改超级管理员（admin）的登录名和角色
				if ("admin" == hiddenLoginNameValue) {
					// 此处需求可使用和角色一样的实现方式（使用JSTL判断要修改的用户是否是admin，如果是admin，则使用隐藏域传值，否则不使用隐藏域）
					// 但为了做示例，所以采用了不同的实现方式
               		$loginName.attr("readonly", true);
               		$loginName.css("background", "#F0F0F0");
               		$loginName.css("cursor", "default");
               		$loginName.focus(function(){
               			this.blur();
               		});
				}
				else {
					for (var i = 0; i < $roleIdArray.length; i++) {
	                	$($roleIdArray[i]).attr("disabled", false);
	                }
				}

                $btnEdit.text("取消");
                $btnSave.fadeIn("slow");
            }
            else {
                // 将页面上的控件置为不可用
               	$loginName.attr("disabled", true);
                $gender.attr("disabled", true);
                $birthday.attr("disabled", true);
                $telephone.attr("disabled", true);
                $address.attr("disabled", true);
                for (var i = 0; i < $roleIdArray.length; i++) {
                    $($roleIdArray[i]).attr("disabled", true);
                }

                $btnEdit.text("编辑");
                $btnSave.fadeOut("slow");
            }

        }

        function del(id) {
        	if (confirm("确定删除用户信息吗？")) {
        		// var roleId = $("input[name='roleId']").attr("checked", true).val();
				location.href = "${ pageContext.request.contextPath }/jsp/user/UserServlet?method=DEL&id=" + id;
			}
        }
        
        function update() {
        	if (confirm("确定要保存修改的用户信息吗？")) {
        		$("#updateUserForm").submit();
			}
        }

    </script>
</head>

<body>

<div class="list">

    <div class="add">
        <span class="font_size_13">
            <a href="javascript:void(0);" class="label label-blue">查看用户 &gt;&gt;</a>
        </span>
    </div>

    <div class="col-1-1">
        <form id="updateUserForm" name="updateUserForm" class="form form-stacked" method="post" action="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=UPDATE">
            <fieldset>

                <div class="grid">
                	<%--
                    <div class="col-1-2">
                        <label>用户编号：</label>
                        <input type="text" placeholder="用户编号" value="${ user.id }" disabled/>
                    </div>
                    --%>

                    <input type="hidden" name="id" value="${ user.id }"/>

                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;登录名称：</label>
                        <!-- 此处hidden的值不会传递到后台，只用做JS判断 -->
                        <input type="hidden" id="hiddenLoginName" value="${ user.loginName }"/>
                        <input type="text" id="loginName" name="loginName" placeholder="登录名称" value="${ user.loginName }" disabled/>
                        <!-- <span style="color: red;">用户名不能空！</span> -->
                    </div>

                    <div class="col-1-2">
                        <label>用户性别：</label>
                        <select id="gender" name="gender" class="width_193" disabled>
                            <option value="男" <c:if test="${ '男' eq user.gender }">selected</c:if> >男</option>
                            <option value="女" <c:if test="${ '女' eq user.gender }">selected</c:if> >女</option>
                        </select>
                    </div>

                    <div class="col-1-2">
                        <label>电话号码：</label>
                        <input type="text" id="telephone" name="telephone" placeholder="电话号码" value="${ user.tel }" disabled/>
                    </div>

                    <div class="col-1-2">
                        <label>出生日期：</label>
                        <input type="text" id="birthday" name="birthday" placeholder="出生日期" value="${ user.birthday }" disabled/>
                    </div>

                    <div class="col-1-2">
                        <label><span style="color: red;">*</span>&nbsp;权限角色：</label>
                        <%-- 列出角色信息 --%>
                        <c:forEach var="role" items="${ requestScope.roleList }">
                        	<label class="width_193">
                        		${ role.name }<input type="radio" name="roleId" value="${ role.id }" <c:if test="${ role.id == user.roleId }">checked</c:if> disabled/>
                        	</label>
                        </c:forEach>
                        <%-- 当要修改的用户为admin用户时，页面上的角色单选按钮为disabled状态（表示admin用户不可修改角色）。由于disabled状态的元素不向后台传值，所以需要用到下面的hidden元素 --%>
                        <c:if test="${ 'admin' == user.loginName }">
                        	<input type="hidden" name="roleId" value="${ user.roleId }"/>
                        </c:if>
                    </div>

                    <div class="col-1-2">
                        <label>用户地址：</label>
                        <textarea id="address" name="address" rows="2" class="x1-large" placeholder="用户地址" disabled>${ user.address }</textarea>
                    </div>

                    <div class="col-1-1 width_830">
                        <!-- <button type="submit" class="button-blue">提交数据</button> -->
                        <a href="javascript:void(0);" id="btnEdit" class="button button-blue" onclick="enableEdit();">编辑</a>
                        <a href="javascript:void(0);" id="btnSave" class="button button-green" onclick="update();" style="display: none;">保存</a>
                        <a href="${ pageContext.request.contextPath }/jsp/user/updatePwd.jsp?id=${user.id}&loginName=${user.loginName}" class="button button-blue">修改密码</a>
                        <a href="javascript:void(0);" onclick="del('${user.id}');" class="button button-red">删除</a>
                        <a href="${ pageContext.request.contextPath }/jsp/user/UserServlet?method=LIST" class="button button-red">返回</a>
                    </div>
                </div>

            </fieldset>
        </form>
    </div>

</div>

</body>

</html>