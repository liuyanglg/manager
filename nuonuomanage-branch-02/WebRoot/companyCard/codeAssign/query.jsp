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
    <style type="text/css">
        td {
            white-space: nowrap;
        }
    </style>
</head>
<body>
<script type="text/javascript" src="companyCard/codeAssign/script/main.js"></script>
<script>
    function myformatter() {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
    }

    function get7DaysBefore() {
        var date = new Date(),
            timestamp, newDate;
        if (!(date instanceof Date)) {
            date = new Date(date.replace(/-/g, '/'));
        }
        timestamp = date.getTime();
        newDate = new Date(timestamp - 7 * 24 * 3600 * 1000);
        return [[newDate.getFullYear(), newDate.getMonth() + 1, newDate.getDate()].join('-')].join(' ');
    }

    $(document).ready(function () {

        $("#dateRangStart").datebox({
            value: get7DaysBefore()
        });

        $("#dateRangEnd").datebox({
            value: myformatter()
        });

        $('#cmp_code_assign_select_assign_employee').combobox({
            prompt: '输入关键字后自动搜索',
        });

        var data = $('#cmp_code_assign_search_form').serialize();
        console.log("data:", data);
        $('#cmp_code_assign_query_table').datagrid({
            url: 'companyCard/codeAssign/queryCodeAssign.action?'+data,
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
            ]],
            toolbar: [
                {id: 'btnAddAssign', text: '获取代码', iconCls: 'icon-add', handler: assignCodes}
            ]
        })
    });


</script>
<div style="height: 100%; position: relative; ">
    <div id="cmp_code_assign_search_form_div" style="height: 90px;">
        <form id="cmp_code_assign_search_form">
            <table>
                <tr>
                    <td width="80px" style="text-align:right;">分配信息：</td>
                    <td>
                        <input id="cmp_code_assign_assign_cmp_name_or_taxid" name="assignCmpNameOrTaxid" type="text" placeholder="分配企业名称/税号" style="width:200px;"/>
                    </td>
                    <td>
                        <select id="cmp_code_assign_select_assign_employee" name="assignEmployeeName" type="text" style="width:100px"
                                data-options="valueField:'assignEmployeeId',textField:'assignEmployeeName'"
                                class="easyui-combobox">
                        </select>
                    </td>
                    <td width="100px">
                        <input id="cmp_code_assign_pre_assign_code" name="preAssignCode" type="text" placeholder="分配六位代码" style="width:100px;"/>
                    </td>
                    <td width="150px" style="text-align:right;">
                        <select id="cmp_code_assign_select_time_type" name='timeType' style="width:80px;" class="easyui-combobox"
                                editable="false" data-options="panelHeight:'auto'">
                        <option value="0" selected="selected">创建时间</option>
                        <option value="1">绑定时间</option>
                        <option value="2">修改时间</option>
                    </select></td>
                    <td style="width:210px;">
                        <input name="dateRangStart" id="dateRangStart" type="text" size="10" style="width: 95px"/>
                        <label style="text-align:center;width: 10px">-</label>
                        <input name="dateRangEnd" type="text" id="dateRangEnd" size="10" style="width: 95px"/>
                    </td>
                </tr>
                <tr>
                    <td width="80px" style="text-align:right;">绑定信息：</td>
                    <td>
                        <input  id="cmp_code_assign_bind_cmp_name_or_taxid" name="bindCmpNameOrTaxid" type="text" placeholder="绑定企业名称/税号" style="width:200px;"/>
                    </td>
                    <td>
                        <select id="cmp_code_assign_select_bind_person" name="bindPerson" type="text" style="width:100px"
                                data-options="valueField:'assignEmployeeId',textField:'assignEmployeeName'"
                                class="easyui-combobox">
                        </select>
                    </td>
                    <td>
                        <input  id="cmp_code_assign_origin_code" name="originCode" type="text" placeholder="原始六位代码" style="width:100px;"/>
                    </td>
                    <td width="200px" style="text-align:right;">代码状态：</td>
                    <td>
                        <select id="cmp_code_assign_select_status" name='status' style="width:203px;" class="easyui-combobox"
                                editable="false"data-options="panelHeight:'auto'">
                            <option value="">全部</option>
                            <option value="0">新建</option>
                            <option value="1">已绑定</option>
                            <option value="2">已恢复</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;" colspan="10">
                        <input type="button" style="border-radius: 3px" onclick="fuzzyQuery()" value="查询"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="cmp_code_assign_table_div" style="width: 100%; position: absolute; top: 90px ; left: 0 ; bottom: 0;">
        <table id="cmp_code_assign_query_table" style="display: none;"/>
    </div>
</div>

<div id="cmp_code_assign_assign_div"/>
</body>
</html>