<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ include file="../including/quote.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>开票信息维护</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<body>
<script type="text/javascript" src="companyCard/cardMaintain/script/crud.js?v=4"></script>
<script>
    $(document).ready(function () {
        $('#cmpMaintain_query').datagrid({
            url: 'companyCard/cardMaintain/queryCardMaintain.action?init=1',
            iconCls: 'icon-save',
            idField: 'code',
            nowrap: true,
            striped: true,
            pagination: true,
            rownumbers: true,
            fitColumns: true,
            remoteSort: false,
            custom: true,
            collapsible: true,
            fit: true,
            pageSize: 15,
            pageList: [15, 25, 40, 100],
            onLoadError: function (data) {
                $.messager.alert("加载提示", data.responseText);
            },
            frozenColumns: [[
                {field: 'ck', checkbox: true}
            ]],
            columns: [[
                {field: 'sourceStr', title: '来源', align: 'center', width: 180},
                {field: 'code', title: '六位代码', align: 'center', width: 150},
                {field: 'name', title: '企业名称', align: 'center', width: 350},
                {field: 'taxid', title: '税号', align: 'center', width: 300},
                {field: 'address', title: '地址', align: 'center', width: 350},
                {field: 'telephone', title: '电话', align: 'center', width: 220},
                {field: 'bank', title: '开户行', align: 'center', width: 350},
                {field: 'account', title: '银行账号', align: 'center', width: 320},
                {field: 'typeStr', title: '纳税人标识', align: 'center', width: 180},
                {field: 'certStr', title: '认证标识', align: 'center', width: 150},
                {field: 'statusStr', title: '数据状态', align: 'center', width: 150}
            ]],
            toolbar: (function () {
                var buttons = eval([${current_ajaxclient_authority}]);
                for (var i = 0; i < buttons.length; i++) {
                    if (buttons[i].id == "companyCardMaintainModifyCode") {
                        buttons[i].iconCls = "icon-edit";
                    }
                    if (buttons[i].id == "companyCardMaintainShield") {
                        buttons[i].iconCls = "icon-remove";
                    }
                }
                return buttons;
            })()
        })
    });
</script>
<table id="cmpMaintain_query" style="display: none;"></table>
<div id='cmpMaintain_add_div'></div>
<div id="cmpMaintain_search_div"></div>
<div id="cmpMaintain_edit_div"></div>
</body>
</html>
