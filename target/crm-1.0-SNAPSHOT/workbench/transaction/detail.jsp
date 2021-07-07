<%@ page import="com.gy.crm.settings.entity.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.gy.crm.workbench.entity.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName()+":"+
request.getServerPort()+request.getContextPath()+"/";
	List<DicValue> dvList = (List<DicValue>)application.getAttribute("stageList");
	Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");
	int point = 0;
	for(int i = 0; i < dvList.size(); i++) {
		DicValue dicValue = dvList.get(i);
		String stage = dicValue.getValue();
		String possibility = pMap.get(stage);
		if("0".equals(possibility)) {
			point = i;
			break;
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });
		//页面加载完展示交易历史信息
		showTranHistory();
	});
	function showTranHistory() {
		var tid = "${tran.id}";
		$.ajax({
			url:"workbench/transaction/show.do",
			data:{"tid":tid},
			type:"post",
			dataType:"json",
			success:function(data){
				//{{xxx},{xxx}}
				var html="";
				$.each(data,function(i,n){
					html +='<tr>'
					html +='<td>'+n.stage+'</td>'
					html +='<td>'+n.money+'</td>'
					html +='<td>'+n.possibility+'</td>'
					html +='<td>'+n.expectedDate+'</td>'
					html +='<td>'+n.createTime+'</td>'
					html +='<td>'+n.createBy+'</td>'
					html +='</tr>'
				})
				$("#tbd").html(html);
			}
		})
	}
	function changeStage(stage,i) {
		//stage为将要更改的阶段，i为该阶段对应的下标
		$.ajax({
			url:"workbench/transaction/changeStage.do",
			data:{
				"tid":"${tran.id}",
				"money":"${tran.money}",
				"expectedDate":"${tran.expectedDate}",
				"stage":stage
			},
			type:"post",
			dataType:"json",
			success:function(data){
				//{success:true/false,tran:{xxx:xxx,xxx:xxx}}
				if(data.success){
					$("#post_stage").html(data.tran.stage);
					$("#post_possibility").html(data.tran.possibility);
					$("#post_editBy").html(data.tran.editBy);
					$("#post_editTime").html(data.tran.editTime);
					//更改完数据后更改图标
					updateIcon(data.tran.stage,data.tran.possibility,i);
				}else{
					alert("更改失败")
				}
			}
		})
	}
	function updateIcon(stage,possibility,k) {
		//stage为当前阶段，i为当前阶段的下标,possibility为当前阶段的可能性
		var index = <%=point%>;
		if("0"==possibility) {
			//那么前面7个为黑圈，后面2个一个红叉和一个黑叉
			for(var i = 0; i < index; i++) {
				//黑圈
				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-record mystage");
				$("#"+i).css("color","#000000");
			}
			for(var i = index; i < <%=dvList.size()%>; i++) {
				if(i == k) {
					//红叉
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-remove mystage");
					$("#"+i).css("color","red");
				}else {
					//黑叉
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-remove mystage");
					$("#"+i).css("color","#000000");
				}
			}
		}else {
			//后面两个为黑叉,前面7个有绿标，绿圈和黑圈
			for(var i = 0; i < index; i++) {
				if(i == k) {
					//绿标
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-map-marker mystage");
					$("#"+i).css("color","#90F790");
				}else if(i < k) {
					//绿圈
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-ok-circle mystage");
					$("#"+i).css("color","#90F790");
				}else{
					//黑圈
					$("#"+i).removeClass();
					$("#"+i).addClass("glyphicon glyphicon-record mystage");
					$("#"+i).css("color","#000000");
				}
			}
			for(var i = index; i < <%=dvList.size()%>; i++) {
				//黑叉
				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-remove mystage");
				$("#"+i).css("color","#000000");
			}
		}
	}
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${tran.name}-交易01 <small>￥${tran.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			//获取当前阶段和可能性
			Tran tran = (Tran) request.getAttribute("tran");
			String curStage = tran.getStage();
			String curStagePossibility = tran.getPossibility();
			//判断当前阶段的可能性是否为0来进行划分
			if("0".equals(curStagePossibility)) {
				//当前阶段可能性为0说明前7个一定是黑圈后面两个一个红一个黑
				//循环遍历找到分界点,即找到可能性为0
				for(int i = 0; i < dvList.size(); i++) {
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
					String possibility = pMap.get(stage);
					if("0".equals(possibility)) {
						if(stage.equals(curStage)) {
							//红叉
		%>
						<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
							  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: red;">
						</span>
						-------------
		<%

						}else {
							//黑叉
		%>
						<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
							  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #000000;">
						</span>
						-------------
		<%
						}
					}else {
						//黑圈
		%>
						<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
							  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #000000;">
						</span>
						-------------
		<%
					}
				}
			}else {
				//不为0说明前面7个可能是绿色标签或绿色圈，或黑圈，后面两个一定是黑叉
				//找分界点，先将后面两个至黑叉
				int index = 0;
				for(int i = 0; i < dvList.size(); i++) {
					DicValue dicValue = dvList.get(i);
					String possibility = pMap.get(dicValue.getValue());
					if("0".equals(possibility)) {
						index = i;
						break;
					}
				}
		%>

		<%
				for(int i = 0; i < index; i++) {
					//有三种可能性
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
					if(stage.compareTo(curStage) == 0) {
						//标绿标
		%>
					<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-map-marker mystage"
						  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #90F790;">
					</span>
					-------------
		<%
					}else if(stage.compareTo(curStage) > 0) {
						//绿圈
		%>
						<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-record mystage"
							  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #000000;">
						</span>
						-------------
		<%
					}else {
						//黑圈
		%>
						<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-ok-circle mystage"
							  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #90F790;">
						</span>
						-------------

		<%
					}
				}
				for(int i = index; i < dvList.size(); i++) {
					//标黑叉
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
		%>
					<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage"
						  data-toggle="popover" data-placement="bottom" data-content="<%=dicValue.getText()%>" style="color: #000000;">
					</span>
		-------------
		<%

				}

			}

		%>
		<span class="closingDate">${tran.expectedDate}</span>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="post_stage">${tran.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="post_possibility">${tran.possibility}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>&nbsp;${tran.activityId}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">2017-01-18 10:10:10</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="post_editBy">${tran.editBy}&nbsp;&nbsp;</b><small id="post_editTime" style="font-size: 10px; color: gray;">${tran.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					&nbsp;${tran.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>&nbsp;${tran.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="tbd">

<%--						<tr>--%>
<%--							<td>需求分析</td>--%>
<%--							<td>5,000</td>--%>
<%--							<td>20</td>--%>
<%--							<td>2017-02-07</td>--%>
<%--							<td>2016-10-20 10:10:10</td>--%>
<%--							<td>zhangsan</td>--%>
<%--						</tr>--%>
<%--						<tr>--%>
<%--							<td>谈判/复审</td>--%>
<%--							<td>5,000</td>--%>
<%--							<td>90</td>--%>
<%--							<td>2017-02-07</td>--%>
<%--							<td>2017-02-09 10:10:10</td>--%>
<%--							<td>zhangsan</td>--%>
<%--						</tr>--%>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>