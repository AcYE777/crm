<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName()+":"+
request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function() {
			//判断登录页面是否是顶级窗口
			if(window.top != window) {
				window.top.location = window.location;
			}

			$("#loginAct").val("");
			$("#loginPwd").val("");
			$("#loginAct").focus();

			$("#submitBtn").click(function(){
				login();
			})
			$(document).keydown(function(e) {
				if(e.keyCode == 13) {
					login();
				}
			})
		})
		function login() {
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			if(loginAct=="" || loginPwd =="") {
				$("#msg").html("用户名或密码不能为空");
				return;
			}
			//用户输入合法利用ajax进行发送请求验证用户名和密码是否合法
			$.ajax({
				url:"settings/user/login.do",
				data:{"loginAct":loginAct,"loginPwd":loginPwd},
				type:"get",
				dataType:"json",
				success:function (resp) {
					//拿到的数据可能是登录成功或者登录失败{success:true/false,msg:value}
					if(resp.success) {
						document.location.href = "workbench/index.jsp";
					}else {
						$("#msg").html(resp.msg);
					}
				}
				
			})
		}


	</script>

</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;YE</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>

			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" id="loginAct" name="loginAct" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" name="loginPwd" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>