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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			language:  "zh-CN",
			format: "yyyy-mm-ddhh:ii:ss",//显示格式
			minView: "hour",//设置只显示到月份
			initialDate: new Date(),//初始化当前日期
			autoclose: true,//选中自动关闭
			todayBtn: true, //显示今日按钮
			clearBtn : true,
			pickerPosition: "bottom-left"
		});

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#addCreate").click(function(){
			//reset方法jquery不供但是idea提示了，需要换成dom来调用
			$("#modalForm")[0].reset();
			$.ajax({
				url:"workbench/activity/queryUser.do",
				type:"get",
				dataType:"json",
				success:function (jsonArr) {
					//{{id:value,name:value}，{}}
					var selectText = ""
					$.each(jsonArr,function(i,n) {
						//n为一个json对象
						selectText +="<option value="+ n.id+">"+n.name+"</option>";
					})
					$("#create-owner").html(selectText);
					$("#create-owner").val("${sessionScope.user.id}");

					$("#createActivityModal").modal("show");


				}
			})
		})

		$("#saveBtn").click(function(){
			//点击保存按钮获取用户的输入利用ajax发送请求
			var owner = $.trim($("#create-owner").val());
			var name = $.trim($("#create-name").val());
			var startDate = $.trim($("#create-startDate").val());
			var endDate = $.trim($("#create-endDate").val());
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());
			$.ajax({
				url:"workbench/activity/save.do",
				data:{"owner":owner,"name":name,"startDate":startDate,"endDate":endDate,"cost":cost,"description":description},
				type:"post",
				dataType:"json",
				success:function(json) {
					if(json.success) {
						//成功后先刷新列表然后关闭模态窗口
						//调用分页进行展示最新的数据
						//pageList(1,2);
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#createActivityModal").modal("hide");
					}else {
						alert("添加失败");
					}
				}
			})
		})
		//页面加载完后也需要进行展示最新的数据
		pageList(1,2);
		//点击查询需要刷新
		$("#submitBtn").click(function() {
			//在每一次的查询中将文本内容保存到隐藏域中
			var name = $.trim($("#search-name").val());
			var owner = $.trim($("#search-owner").val());
			var startDate = $.trim($("#search-startDate").val());
			var endDate = $.trim($("#search-ende").val());
			name = $("#search-hiddenName").val(name)
			owner = $("#search-hiddenOwner").val(owner)
			startDate = $("#search-hiddenStartDate").val(startDate)
			endDate = $("#search-hiddenEndDate").val(endDate)
			pageList(1,2);
		})
		//复选框操作-全选操作
		$("#allCheckBox").click(function(){
			$("input[name='subCheckBox']").prop("checked",this.checked)
		})
		//动态生成的标签不能必须利用on来绑定事件
		$("#td").on("click",$("input[name='subCheckBox']"),function(){
			$("#allCheckBox").prop("checked",$("input[name='subCheckBox']:checked").length == $("input[name='subCheckBox']").length);
		})

		//删除操作
		$("#deleteBtn").click(function() {

			var $checkedObj = $("input[name='subCheckBox']:checked");
			if($checkedObj.length == 0) {
				alert("请选择要删除的内容");
				return;
			}
			var html = "";
			$.each($checkedObj,function(i,n){
				//遍历jquery数组将id进行拼接
				html += n.value;
				if(i != $checkedObj.length - 1) {
					html += "&";
				}
			})
			if(window.confirm("您确定要删除?")) {
				$.ajax({
					url:"workbench/activity/deleteActivity.do",
					data:{"activityId":html},
					type:"get",
					dataType:"json",
					success: function(data){
						//{success:true/false}
						if(data.success) {
							alert("删除成功");
							//删除后刷新列表
							//pageList(1,2);
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							alert("删除失败")
						}
					}

				})
			}
		})
		$("#editBtn").click(function(){
			var $checkedBox = $("input[name='subCheckBox']:checked")
			if($checkedBox.length == 0) {
				alert("请选择要修改的内容");
				return;
			}
			if($checkedBox.length > 1) {
				alert("当前只能选择一项修改");
				return;
			}
			//说明可以进行修改
			var actid = $("input[name='subCheckBox']:checked").val();
			$.ajax({
				url:"workbench/activity/queryUserAndActivity.do",
				data:{"actid":actid},
				type:"get",
				dataType:"json",
				success:function(data) {
					//{activity:对象,list:用户list列表}
					/**
					 * <option>zhangsan</option>
					 	<option>lisi</option>
					 	<option>wangwu</option>
					 */
					var html ="";
					$.each(data.list,function(i,n) {
						html += "<option value="+ n.id +">"+n.name+"</option>";
					})
					$("#edit-owner").html(html);
					$("#edit-name").val(data.activity.name);
					$("#edit-cost").val(data.activity.cost);
					$("#edit-startDate").val(data.activity.startDate);
					$("#edit-endDate").val(data.activity.endDate);
					$("#edit-description").val(data.activity.description);
					//展示模态窗口
					//在模态窗口打开的时候给隐藏域hiddenActId赋值,目的是得到活动的id为了进行修改这项活动
					$("#hiddenActId").val(actid);
					$("#editActivityModal").modal("show");
				}

			})
		})
		$("#updateBtn").click(function(){
			var owner = $("#edit-owner").val()//获取所有者下拉表选中的内容
			var name = $("#edit-name").val();
			var cost = $("#edit-cost").val();
			var startDate = $("#edit-startDate").val();
			var endDate = $("#edit-endDate").val();
			var description = $("#edit-description").val();
			var id = $("#hiddenActId").val();
			$.ajax({
				url:"workbench/activity/updateActivity.do",
				data:{"owner":owner,"name":name,"cost":cost,"startDate":startDate,"endDate":endDate,
					"description":description,"id":id},
				type:"get",
				dataType:"json",
				success:function(data) {
					//返回{success:true/false}
					if(data.success) {
						alert("修改成功");
						//关闭模态窗口
						//pageList(1,2);
						//停留在本页
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#editActivityModal").modal("hide");
					}else {
						alert("修改失败");
					}
				}
			})
		})
	});
	//页面刷新展示最新数据函数，哪里需要用在哪里调用
	function pageList(pageNo,pageSize) {
		//在每次查询的过程中将从隐藏域中取出数据再进行查询，这样避免没有点击查询按钮点击换页将其先前的条件覆盖掉
		var name = $("#search-hiddenName").val()
		var owner = $("#search-hiddenOwner").val()
		var startDate = $("#search-hiddenStartDate").val()
		var endDate = $("#search-hiddenEndDate").val()
		$("#search-name").val(name)
		$("#search-owner").val(owner)
		$("#search-startDate").val(startDate)
		$("#search-ende").val(endDate)
		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{"name":name,"owner":owner,"startDate":startDate,"endDate":endDate,
				"pageNo":pageNo,"pageSize":pageSize},
			type:"get",
			dataType:"json",
			success:function(data) {
				//{total:xxx,list:{{xxx:xxx},{xxx:xxx},{xxx:xxx}}}
				/**
				 * <tr class="active">
				 	<td><input type="checkbox" /></td>
				 	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
				 	<td>zhangsan</td>
				 	<td>2020-10-10</td>
				 	<td>2020-10-20</td>
				   </tr>
				 */
				var total = data.total;//总记录条数
				var html="";
				$.each(data.list,function(i,n){
					html +='<tr class="active">'
					html +='	<td><input type="checkbox" name="subCheckBox" value="'+ n.id +'"/></td> '
					html +='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?actid='+ n.id+'\';">'+ n.name+'</a></td>'
					html +='	<td>'+n.owner+'</td>'
					html +='	<td>'+ n.startDate+'</td>'
					html +='	<td>'+n.endDate+'</td>'
					html +='</tr>'
				})
				$("#td").html(html);
				//处理分页
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: total % pageSize == 0 ? total / pageSize : Math.floor(total / pageSize) + 1, // 总页数
					totalRows: total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="modalForm">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<!--创建一个隐藏域保存选中活动的id-->
	<input type="hidden" id="hiddenActId"/>
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" value="2020-10-10" readonly>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" value="2020-10-20" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
						<input type="hidden" id="search-hiddenName">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
						<input type="hidden" id="search-hiddenOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" readonly/>
						<input type="hidden" id="search-hiddenStartDate">
					</div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate" readonly>
						<input type="hidden" id="search-hiddenEndDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="submitBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addCreate"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="allCheckBox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="td">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>