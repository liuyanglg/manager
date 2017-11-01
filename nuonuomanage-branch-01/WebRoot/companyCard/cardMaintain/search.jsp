<%@ page import="com.aisino.global.context.common.model.UserModel" %>
﻿
<%@ page language="java" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
    $(function () {
        $.extend($.fn.validatebox.defaults.rules, {
            phoneRex: {
                validator: function (value) {
                    var rex = /^1[3-8]+\d{9}$/;
                    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
                    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
                    //电话号码：7-8位数字： \d{7,8
                    //分机号：一般都是3位数字： \d{3,}
                    //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/
                    var rex2 = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
                    if (rex.test(value) || rex2.test(value)) {
                        return true;
                    } else {
                        return false;
                    }
                },
                message: '请输入正确电话或手机格式'
            }
        });
    });

    $(function () {
        var userType = $("#cmp_user_type").val();
        console.log(userType);
        if (userType.indexOf("USER") >= 0) {
            $("#cmp_company_card_search_type").attr("disabled","disabled");
            $("#cmp_company_card_search_cert").attr("disabled","disabled");
            $("#cmp_company_card_search_source").attr("disabled", "disabled");
            $("#cmp_company_card_search_status").attr("disabled", "disabled");
        }
    });
</script>
<script type="text/javascript" src="companyCard/cardMaintain/script/crud.js"></script>
<input type="hidden" id="cmp_user_type" value="<%=((UserModel)session.getAttribute("userModel")).getUserType() %>"/>
<div align="center">
    <form id="cmpMaintain_search_form" method="post">
        <input name="init" type="hidden" value="0"/>
        <table style="padding:10px 0 0 0 ;width:98%;font-size: 12px;margin: 0 auto;">
            <tr>
                <th align="right">企业名称:</th>
                <td>
                    <input type="text" name="name" class='easyui-validatebox' validType="length[0,35]"/>
                </td>
                <th align="right">开票信息来源:</th>
                <td>
                    <select id="cmp_company_card_search_source" name='source' style="width:154px"
                            class="easyui-combobox" editable="false"
                            readonly="readonly">
                        <option value="">全部</option>
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
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">税号:</th>
                <td>
                    <input type="text" name="taxid" class='easyui-validatebox' validType="length[0,20]"/>
                </td>
                <th align="right">纳税人标识:</th>
                <td>
                    <select id="cmp_company_card_search_type" name='type' style="width:154px" class="easyui-combobox"
                            editable="false">
                        <option value="">全部</option>
                        <option value="0">一般纳税人</option>
                        <option value="1">小规模</option>
                        <option value="2">个体工商户</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">六位代码:</th>
                <td>
                    <input type="text" name="code" id="code" class='easyui-validatebox'/>
                </td>
                <th align="right">认证标识:</th>
                <td>
                    <select id="cmp_company_card_search_cert" name='cert' style="width:154px" class="easyui-combobox"
                            editable="false">
                        <option value="">全部</option>
                        <option value="0">未认证</option>
                        <option value="1">已认证</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">数据状态:</th>
                <td>
                    <select id="cmp_company_card_search_status" name='status' style="width:154px"
                            class="easyui-combobox" editable="false">
                        <option value="">全部</option>
                        <option value="0">常规显示</option>
                        <option value="8">已屏蔽</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
