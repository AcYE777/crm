<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
		$("#activitySource").click(function(){
			$("#tbdy").html("");
			$("#actiSearch").val("");
			//打开模态窗口
			$("#searchActivityModal").modal("show");
		})
		$("#actiSearch").keydown(function(event){
			if(event.keyCode == 13) {
				//获取用户输入的内容
				var activityInfo = $("#actiSearch").val();
				$.ajax({
					url:"workbench/clue/searchActivityInfo.do",
					data:{"activityInfo":activityInfo},
					type:"post",
					dataType:"json",
					success:function (data) {
						//{{xx},{xx}}
						var html = "";
						$.each(data,function(i,n){
							html +='<tr>'
							html +='<td><input type="radio" name="activity" value="'+ n.id+'"/></td>'
							html +='<td id="y'+n.id+'">'+n.name+'</td>'
							html +='<td>'+n.startDate+'</td>'
							html +='<td>'+n.endDate+'</td>'
							html +='<td>'+n.owner+'</td>'
							html +='</tr>'
						})
						$("#tbdy").html(html);
					}
				})

				return false;
			}
		})
		$("#submitFill").click(function(){
			//获取用户选择的内容信息及这条活动的id，id放到隐藏域中,名称放到文本框中
			var aid = $("input[name='activity']:checked").val()
			var aname = $("#y"+aid).html();
			$("#actIdHidden").val(aid);
			$("#activity").val(aname);
			//关闭模态窗口
			$("#searchActivityModal").modal("hide");
		})

		$("#tranform").click(function(){
			//绑定复选框为了知道是否创建交易
			if($("#isCreateTransaction").prop("checked")){
				//说明需要为客户创建交易，但是用户也可以不填这些内容
				$("#createTran").submit();
			}else{
				//说明不需要为客户创建交易
				window.location.href="workbench/clue/convert.do?clueId=${param.clueId}";
			}

		})


	});

</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="actiSearch" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="tbdy">
<%--							<tr>--%>
<%--								<td><input type="radio" name="activity"/></td>--%>
<%--								<td>发传单</td>--%>
<%--								<td>2020-10-10</td>--%>
<%--								<td>2020-10-20</td>--%>
<%--								<td>zhangsan</td>--%>
<%--							</tr>--%>
<%--							<tr>--%>
<%--								<td><input type="radio" name="activity"/></td>--%>
<%--								<td>发传单</td>--%>
<%--								<td>2020-10-10</td>--%>
<%--								<td>2020-10-20</td>--%>
<%--								<td>zhangsan</td>--%>
<%--							</tr>--%>
						</tbody>
					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" id="submitFill">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form action="workbench/clue/convert.do" method="post" id="createTran">
			<!--这两个隐藏域主要是用于在用户打上复选框后进行传递线索id和flag给后台知道已经提交表单了-->
			<input type="hidden" name="clueId" value=${param.clueId}>
			<input type="hidden" name="hasTran" value="hasTran">
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" name="money" class="form-control" id="amountOfMoney">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" name="tradeName" class="form-control" id="tradeName" value="动力节点-">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" name="expectedDate" class="form-control time" id="expectedClosingDate" readonly>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage" name="stageName" class="form-control">
		    	<option></option>
				<c:forEach items="${applicationScope.stageList}" var="a">
					<option value="${a.value}">${a.text}</option>
				</c:forEach>
<%--		    	<option>资质审查</option>--%>
<%--		    	<option>需求分析</option>--%>
<%--		    	<option>价值建议</option>--%>
<%--		    	<option>确定决策者</option>--%>
<%--		    	<option>提案/报价</option>--%>
<%--		    	<option>谈判/复审</option>--%>
<%--		    	<option>成交</option>--%>
<%--		    	<option>丢失的线索</option>--%>
<%--		    	<option>因竞争丢失关闭</option>--%>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="activitySource" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
			  <input type="hidden" id="actIdHidden" name="activityId">
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="tranform" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>