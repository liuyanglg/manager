﻿﻿<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body>
<script type="text/javascript">
	$(function(){
		$.extend($.fn.validatebox.defaults.rules, {
				phoneRex: {
					validator: function(value){
					var rex=/^0?(13|14|15|17|18)[0-9]{9}$/;
				    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				    //电话号码：7-8位数字： \d{7,8
				    //分机号：一般都是3位数字： \d{3,}
				    //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
					var rex2=/^[0-9-()（）]{7,18}$/;
					if(rex.test(value)||rex2.test(value)){
						return true;
					}else{
						return false;
					}
				},
				message: '请输入正确电话或手机格式'
			}
		});
	});
</script>
	<script type="text/javascript" src="companyCard/cardMaintain/script/crud.js"></script>
	<div align="center">
		<form id="add_form" method="post" >
			<table style="padding:10px 0 0 0;;width:98%;font-size: 12px;margin: 0 auto;">
				<tr>
					<th>企业名称:</th>
					<td>
						<input type="text" name="name" class='easyui-validatebox' data-options="required:true,missingMessage:'企业名称不能为空'" validType="length[2,35]"/>
					</td>
					<th>开票信息来源:</th>
					<td>
						<select name='source' class="easyui-combobox" editable="false" readonly="readonly">
							<option value="99">ADMIN</option>
							<option value="20">crm</option>
							<option value="10">开票软件</option>
							<option value="11">开票软件-百旺</option>
							<option value="12">购方信息</option>
							<option value="31">微信未登录</option>
							<option value="32">微信已登录</option>
							<option value="40">用户中心</option>
							<option value="50">请求开票</option>
							<option value="">未知</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>税号:</th>
					<td>
						<input type="text" name="taxid" class='easyui-validatebox' data-options="required:true,missingMessage:'税号不能为空'" validType="length[15,20]"/>
					</td>
					<th>纳税人标识:</th>
					<td>
						<select name='type' class="easyui-combobox" editable="false">
							<option value="0">一般纳税人</option>
							<option value="1">小规模</option>
							<option value="2">个体工商户</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>地址:</th>
					<td>
						<input type="text" name="address" class='easyui-validatebox' data-options="required:true,missingMessage:'地址不能为空'" validType="length[3,60]"/>
					</td>
					<th>认证标识:</th>
					<td>
						<select name='cert' class="easyui-combobox" editable="false">
							<option value="0">未认证</option>
							<option value="1">认证</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>开户行:</th>
					<td>
						<input type="text" name="bank" class='easyui-validatebox' data-options="required:false" validType="length[0,40]"/>
					</td>
					<th>电话:</th>
					<td>
						<input type="text" name="telephone" class='easyui-validatebox' data-options="required:true,validType:'phoneRex'"/>
					</td>
				</tr>
				<tr>
					<th>银行账号</th>
					<td>
						<input type="text" name="account" class='easyui-validatebox' data-options="required:false" validType="length[9,25]"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
