<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'add.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
</head>
<body>
	<script type="text/javascript" src="companyCard/cardMaintain/script/crud.js"></script>
	<script type="text/javascript">
	$(function(){
		$.extend($.fn.validatebox.defaults.rules, {
				phoneRex: {
					validator: function(value){
					var rex=/^0?(13|14|15|17|18)[0-9]{9}$/;	 
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
	<div align="center">
		<form id="cmpMaintain_edit_form" method="post">
			<table style="padding:10px 0 0 0;width:98%;font-size: 12px;margin: 0 auto;">
				<tr>
					<th>企业名称:</th>
					<td>
						<input type="text" name="name" value="${card.name}" class='easyui-validatebox' data-options="required:true,missingMessage:'企业名称不能为空'" validType="length[2,35]"/>
					</td>
					<th>开票信息来源:</th>
					<td>
						<select name='source' class="easyui-combobox">
							<c:choose>         
								<c:when test="${card.source == 20}">
									<option value="20" selected="selected">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">开票软件-购方</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>     
								<c:when test="${card.source == 10}">
									<option value="20">crm</option>
									<option value="10" selected="selected">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 11}">             
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11" selected="selected">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 12}">             
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12" selected="selected">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 30}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30" selected="selected">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 31}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31" selected="selected">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 32}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32" selected="selected">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 40}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40" selected="selected">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 50}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50" selected="selected">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:when test="${card.source == 99}">
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99" selected="selected">ADMIN</option>
									<option value="">未知</option>
								</c:when>
								<c:otherwise>
									<option value="20">crm</option>
									<option value="10">开票软件</option>
									<option value="11">开票软件-百旺</option>
									<option value="12">购方信息</option>
									<option value="30">诺诺网</option>
									<option value="31">微信未登录</option>
									<option value="32">微信已登录</option>
									<option value="40">用户中心</option>
									<option value="50">请求开票</option>
									<option value="99">ADMIN</option>
									<option value="" selected="selected">未知</option>
								</c:otherwise>
							</c:choose>
						</select>
					</td>
				</tr>
				<tr>
					<th>税号:</th>
					<td>
						${card.taxid}
					</td>
					<th>纳税人标识:</th>
					<td>
						<select name='type' class="easyui-combobox">
							<c:choose>
								<c:when test="${card.type == 0}">
									<option value="">未知</option>
									<option value="0" selected="selected">一般纳税人</option>
									<option value="1">小规模</option>
									<option value="2">个体工商户</option>
								</c:when>
								<c:when test="${card.type == 1}">
									<option value="">未知</option>
									<option value="0">一般纳税人</option>
									<option value="1" selected="selected">小规模</option>
									<option value="2">个体工商户</option>
								</c:when>
								<c:when test="${card.type == 2}">
									<option value="">未知</option>
									<option value="0">一般纳税人</option>
									<option value="1">小规模</option>
									<option value="2" selected="selected">个体工商户</option>
								</c:when>
								<c:otherwise>
									<option value="" selected="selected">未知</option>
									<option value="0">一般纳税人</option>
									<option value="1">小规模</option>
									<option value="2">个体工商户</option>
								</c:otherwise>
							</c:choose>
						</select>
					</td>
				</tr>
				<tr>
					<th>六位代码:</th>
					<td>
						${card.code}
						<input type="hidden" name="code" value="${card.code}" id="code"/>
					</td>
					<th>认证标识:</th>
					<td>
						<select name='cert' class="easyui-combobox" style="width:90px">
							<c:choose>
								<c:when test="${card.cert == 0}">
									<option value="0" selected="selected">未认证</option>
									<option value="1">认证</option>
								</c:when>
								<c:when test="${card.cert == 1}">
									<option value="0">未认证</option>
									<option value="1" selected="selected">认证</option>
								</c:when>
							</c:choose>
						</select>
					</td>
				</tr>
				<tr>
					<th>地址:</th>
					<td>
						<input type="text" name="address" value="${card.address}" class='easyui-validatebox' data-options="required:true,missingMessage:'地址不能为空'" validType="length[3,60]"/>
					</td>
					<th>电话:</th>
					<td>
						<input type="text" name="telephone" value="${card.telephone}" class='easyui-validatebox' data-options="required:true,validType:'phoneRex'"/>
					</td>
				</tr>
				<tr>
					<th>开户行:</th>
					<td>
						<input type="text" name="bank" value="${card.bank}" class='easyui-validatebox' validType="length[2,40]"/>
					</td>
					<th>银行账号</th>
					<td>
						<input type="text" name="account" value="${card.account}" class='easyui-validatebox' validType="length[9,25]"/>
					</td>
				</tr>
				<tr>
					<th>数据状态:</th>
					<td>
						<select name='status' class="easyui-combobox">
							<c:choose>
								<c:when test="${card.status == 0}">
									<option value="0" selected="selected">常规显示</option>
									<option value="8">已屏蔽</option>
								</c:when>
								<c:when test="${card.status == 8}">
									<option value="0">常规显示</option>
									<option value="8" selected="selected">已屏蔽</option>
								</c:when>
								<c:otherwise>
									<option value="0">常规显示</option>
									<option value="8">已屏蔽</option>
								</c:otherwise>
							</c:choose>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
</body>
</html>
