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
    <title>六位代码分配信息</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<body>
<script type="text/javascript" src="companyCard/codeAssign/script/main.js"></script>
<script>
    $(document).ready(function () {
        $('#cmp_code_assign_query_table').datagrid({
            url: 'companyCard/codeAssign/queryCodeAssign.action',
            iconCls: 'icon-save',
            idField: 'cmp_code_assign_query_table_id_field',
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
            onLoadSuccess: function () {
                $(this).datagrid('clearSelections');
            },
            frozenColumns: [[
                {field: 'ck', checkbox: true}
            ]],
            columns: [[
                {field: 'preAssignCode', title: '预分配代码', align: 'center', width: 150},
                {field: 'assignCompanyNameTaxid', title: '预分配企业', align: 'center', width: 250},
                {field: 'assignEmployeeNameId', title: '预分配员工', align: 'center', width: 350},
                {field: 'createPersonTime', title: '创建人', align: 'center', width: 250},
                {field: 'bindCompanyNameTaxid', title: '绑定企业', align: 'center', width: 250},
                {field: 'bindPersonTime', title: '绑定人', align: 'center', width: 250},
                {field: 'originCode', title: '原始代码', align: 'center', width: 150},
                {field: 'modifyPersonTime', title: '修改人', align: 'center', width: 250},
                {
                    field: 'status', title: '状态', align: 'center', width: 220, formatter: function (value, rec) {
                    if (value == "0") {
                        return "新建";
                    } else if (value == "1") {
                        return "已绑定";
                    } else if (value == "2") {
                        return "已恢复";
                    }
                    return;
                }
                }
            ]]
        })
    });
</script>
<div style="height: 100%; position: relative; ">
    <div id="cmp_code_assign_search_form_div" style="height: 50px;">
        <table>
            <tr>
                <td>
                    <input type="button" value="获取代码" onclick="assignCodes();"/>
                </td>
            </tr>
        </table>
    </div>
    <div id="cmp_code_assign_table_div" style="width: 100%; position: absolute; top: 50px ; left: 0 ; bottom: 0;">
        <table id="cmp_code_assign_query_table" style="display: none;"/>
    </div>
</div>

<div id="cmp_code_assign_assign_div"/>
</body>
</html>