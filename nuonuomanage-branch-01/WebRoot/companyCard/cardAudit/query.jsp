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

    <title>开票信息审核</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>

<body>
<script type="text/javascript" src="companyCard/cardAudit/script/crud.js"></script>
<script>
    var editIndex = null;
    var type = "0";

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

    function validateValue(c_kpnameValue, c_kpaddrValue, c_kptelValue, c_account_blankValue, c_bank_accountValue) {
        /*        if (c_kpnameValue.length <= 3 || c_kpnameValue.length > 25) {
                    $.messager.alert("操作提示", "企业名称只能4-25个字符!");
                    return false;
                }
                if (c_kpcodeValue.length < 15 || c_kpcodeValue.length > 18) {
                    $.messager.alert("操作提示", "企业税号只能15-18个字符!");
                    return false;
                }
                if (c_kpaddrValue.length <= 5 || c_kpaddrValue.length > 40) {
                    $.messager.alert("操作提示", "地址只能6-40个字符!");
                    return false;
                }
                if (c_kptelValue.length <= 6 || c_kptelValue.length > 20) {
                    $.messager.alert("操作提示", "电话号码只能7-20个字符!");
                    return false;
                }
                if (c_account_blankValue.length == 0 || c_account_blankValue.length > 25) {
                    $.messager.alert("操作提示", "开户行只能少于25个字符且不为空!");
                    return false;
                }
                if (c_bank_accountValue.length < 9 || c_bank_accountValue.length > 25) {
                    $.messager.alert("操作提示", "银行账户只能9-25个字符!");
                    return false;
                }*/
        return true;
    }

    function search() {
        var data = $("#searchForm").serialize();
        type = "1";
        $("#cardAudit_query").datagrid({
            url: 'companyCard/cardAudit/queryCardAudit.action?' + data,
            pageNumber: 1//显示第一页
        });
    }

    function changeAuthentication(c_auditid, _this) {
        var cert = $(_this).val();
        $.ajax({
            url: 'companyCard/cardAudit/changeCert.action?id=' + c_auditid + "&cert=" + cert,
            type: 'POST',
            success: function (rec) {
                if (rec == 'success') {
                    if (cert == "1") {
                        $.messager.alert("提示", "设置认证成功!");
                    } else if (cert == "0") {
                        $.messager.alert("提示", "设置未认证成功!");
                    } else {
                        $.messager.alert("提示", "设置成功!");
                    }
                } else {
                    $.messager.alert("提示", "设置失败!");
                }
            }
        });
    }

    function updateCardAudit(index) {
        if (index == null) {
            return true;
        }
        if ($('#cardAudit_query').datagrid('validateRow', index)) {
            var id = $('#cardAudit_query').datagrid('getRows')[index]['id'];
            var taxidCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'taxid'});
            var taxid;
            if (taxidCol != null) {
                taxid = taxidCol.target.val();
            }
            var nameCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'name'});
            var name;
            if (nameCol != null) {
                name = nameCol.target.val();
            }
            var codeCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'code'});
            var code;
            if (codeCol != null) {
                code = codeCol.target.val();
            }
            var addressCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'address'});
            var address;
            if (addressCol != null) {
                address = addressCol.target.val();
            }
            var telephoneCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'telephone'});
            var telephone;
            if (telephoneCol != null) {
                telephone = telephoneCol.target.val();
            }
            var bankCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'bank'});
            var bank;
            if (bankCol != null) {
                bank = bankCol.target.val();
            }
            var accountCol = $('#cardAudit_query').datagrid('getEditor', {index: index, field: 'account'});
            var account;
            if (accountCol != null) {
                account = accountCol.target.val();
            }
            var companyId = $("#c_company_id").combotree("getValues");
            $.ajax({
                type: "post",
                url: "companyCard/cardAudit/editCardAudit.action",
                async: false,
                data: {
                    "id": id,
                    "taxid": taxid,
                    "name": name,
                    "code": code,
                    "address": address,
                    "telephone": telephone,
                    "bank": bank,
                    "account": account,
                    "companyId": companyId
                },
                success: function (message) {
                    $('#cardAudit_query').datagrid('endEdit', index);
                    if(message!="success"){
                        $.messager.alert("提示", "修改失败!");
                    }
                }
            });
            editIndex = null;
            return true;
        } else {
            editIndex = null;
            return false;
        }
    }


    $(document).ready(function () {
        document.onkeyup = function (e) {
            if (window.event)//如果window.event对象存在，就以此事件对象为准
                e = window.event;
            var code = e.charCode || e.keyCode;
            if (code == 13) {
                updateCardAudit(editIndex);
                var data = $("#searchForm").serialize();
                $("#cardAudit_query").datagrid({
                    url: 'companyCard/cardAudit/queryCardAudit.action?' + data,
                });
            }
            if (editIndex != null && code == 27) {
                $('#cardAudit_query').datagrid('cancelEdit', editIndex);
                editIndex = null;
            }
        }

        var data = $("#searchForm").serialize();

        $("#dateRangStart").datebox({
            value: get7DaysBefore()
        });

        $("#dateRangEnd").datebox({
            value: myformatter()
        });

        $('#c_company_id').combotree({
            valueField: 'id',
            textField: 'text',
            editable: false,
            panelHeight: 300,
            multiple: true,
            value: '',
            url: "companyCard/cardAudit/getCompanyTree.action"
        });

        $('#nameOrTax').searchbox({
            searcher: function (value, name) {
                if (value == null) {
                    return;
                }
                if (value.trim().length <= 0) {
                    return;
                }
                if (value.trim().length < 4) {
                    type = "2";
                    $.messager.alert("操作提示", "企业名称大于4位!");
                } else {
                    type = "2";
                    $("#cardAudit_query").datagrid({
                        url: 'companyCard/cardAudit/matchNameOrTax.action?nameOrTax=' + encodeURIComponent(value),
                        pageNumber: 1
                    });
                }
            },
            prompt: '输入40位以下的字符或汉字'
        });

        $('#cardAudit_query').datagrid({
            url: 'companyCard/cardAudit/queryCardAudit.action?' + data,
            idField: 'id',
            iconCls: 'icon-save',
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
                {field: 'id', hidden: true},
                {
                    field: 'operTypeStr', title: '操作类型', align: 'center', width: 120, formatter: function (value, rec) {
                    if (value == "新增") {
                        return "新增<span style='margin-left:12px;color:blue' onclick=\"compareNT(" + rec.id + ")\">合并</span>";
                    } else {
                        return "<span style='margin-left:12px;color:blue' onclick=\"compare(" + rec.id + ")\">对比</span>";
                    }
                }
                },
                {
                    field: 'source', title: '数据来源', align: 'center', width: 150, formatter: function (value, rec) {
                    if (value == "20") {
                        return "CRM";
                    } else if (value == "10") {
                        return "开票软件";
                    } else if (value == "11") {
                        return "开票软件-百旺";
                    } else if (value == "12") {
                        return "购方信息";
                    } else if (value == "30") {
                        return "诺诺网";
                    } else if (value == "31") {
                        return "微信未登录";
                    } else if (value == "32") {
                        return "微信已登录";
                    } else if (value == "33") {
                        return "工商信息";
                    } else if (value == "40") {
                        return "用户中心";
                    } else if (value == "50") {
                        return "请求开票";
                    } else if (value == "99") {
                        return "ADMIN";
                    }
                    return "未知";
                }
                },
                {field: 'statusStr', title: '审核状态', align: 'center', width: 90},
                {field: 'code', title: '六位代码', align: 'center', width: 90},
                {field: 'name', title: '企业名称', align: 'center', width: 200, editor: 'text'},
                {field: 'taxid', title: '税号', align: 'center', width: 160, editor: 'text'},
                {field: 'address', title: '地址', align: 'center', width: 200, editor: 'text'},
                {field: 'telephone', title: '电话', align: 'center', width: 110, editor: 'text'},
                {field: 'bank', title: '开户行', align: 'center', width: 150, editor: 'text'},
                {field: 'account', title: '银行账号', align: 'center', width: 100, editor: 'text'},
                {
                    field: 'cert', title: '认证状态', align: 'center', width: 80, formatter: function (value, rec) {
                    if (value == "0") {
                        return "<select onchange='changeAuthentication(\"" + rec.id + "\",this)'><option value=''>未知</option><option value='0' selected>未认证</option><option value='1'>认证</option></select>";
                    } else if (value == "1") {
                        return "<select onchange='changeAuthentication(\"" + rec.id + "\",this)'><option value=''>未知</option><option value='0'>未认证</option><option value='1' selected>认证</option></select>";
                    }
                    else {
                        return "<select onchange='changeAuthentication(\"" + rec.id + "\",this)'><option value='' selected>未知</option><option value='0'>未认证</option><option value='1'>认证</option></select>";
                    }
                }
                },
                {field: 'createTimeStr', title: '创建时间', align: 'center', width: 120},
                {
                    field: 'type', title: '纳税人标识', align: 'center', width: 110, formatter: function (value, rec) {
                    if (value == "0") {
                        return "一般纳税人";
                    } else if (value == "1") {
                        return "小规模";
                    } else if (value == "2") {
                        return "个体工商户";
                    }
                }
                },
                {field: 'auditor', title: '审核人', align: 'center', width: 90},
                {field: 'auditTimeStr', title: '审核时间', align: 'center', width: 120}
            ]],
            onDblClickRow: function (index, row) {
                if (editIndex != null) {
                    updateCardAudit(editIndex)
                }

                $('#cardAudit_query').datagrid('beginEdit', index);
                editIndex = index;
            },

            onLoadSuccess: function (data) {
                $(".datagrid-header-rownumber").html("编号");
            }
        })
    });
</script>
<div style="height: 100%; position: relative; ">
    <div style="height: 115px;">
        <form id="searchForm">
            <table id="buttons">
                <tr>
                    <td width="100px" style="text-align:right;">审核状态：</td>
                    <td width="230px">
                        <select name='status' style="width:200px" id='status' class="easyui-combobox" editable="false"
                                data-options="panelHeight:'auto'">
                            <option value="">全部</option>
                            <option value="0" selected="selected">未审核</option>
                            <option value="1">审核通过</option>
                            <option value="-1">审核不通过</option>
                        </select>
                    </td>
                    <td width="100px" style="text-align:right;">数据来源：</td>
                    <td width="230px">
                        <select name='source' style="width:200px" id='source' class="easyui-combobox" editable="false"
                                data-options="panelHeight:'auto'">
                            <option value="">全部</option>
                            <option value="20">crm</option>
                            <option value="10">开票软件</option>
                            <option value="11">开票软件-百旺</option>
                            <option value="12">购方信息</option>
                            <option value="30">诺诺网</option>
                            <option value="31">微信未登录</option>
                            <option value="32">微信已登录</option>
                            <option value="33">工商信息</option>
                            <option value="40">用户中心</option>
                            <option value="50">请求开票</option>
                            <option value="99">ADMIN</option>
                        </select>
                    </td>
                    <td width="100px" style="text-align:right;">创建时间：</td>
                    <td width="230px">
                        <input name="dateRangStart" id="dateRangStart" type="text" size="10" style="width: 94px"/>
                        &nbsp;-&nbsp;
                        <input name="dateRangEnd" type="text" id="dateRangEnd" size="10" style="width: 94px"/>
                    </td>
                </tr>
                <tr>
                    <td width="100px" style="text-align:right;">操作类型：</td>
                    <td width="230px">
                        <select name='operType' style="width:200px" id='operType' class="easyui-combobox"
                                editable="false" data-options="panelHeight:'auto'">
                            <option value="">全部</option>
                            <option value="0">新增</option>
                            <option value="1">修改</option>
                        </select>
                    </td>
                    <td width="100px" style="text-align:right;">纳税人标识：</td>
                    <td width="230px">
                        <select name='type' style="width:200px" id='type' class="easyui-combobox" editable="false"
                                data-options="panelHeight:'auto'">
                            <option value="">全部</option>
                            <option value="0">一般纳税人</option>
                            <option value="1">小规模</option>
                            <option value="2">个体工商户</option>
                        </select>
                    </td>
                    <td width="100px" style="text-align:right;">服务单位：</td>
                    <td width="230px">
                        <select name='companyId' id='c_company_id' validType="checkName" style="width: 207px"
                                class="easyui-validatebox"> </select>
                    </td>
                </tr>

                <tr>
                    <td width="100px" style="text-align:right;">六位代码：</td>
                    <td>
                        <input type="text" name="code" id="code" style="width: 196px" class='easyui-validatebox'/>
                    </td>
                    <td width="100px" style="text-align:right;">企业名称/税号：</td>
                    <td width="230px" style="text-align:left" colspan="3">
                        <input type="text" id="nameOrTax" class="easyui-searchbox" maxlength="40"
                               placeholder="输入40位以下的字符或汉字" style="width: 200px"/>
                    </td>
                </tr>

                <tr>
                    <td width="300px" style="text-align: center;width: 300px" colspan="2">
                        <input type="button" style="border-radius:3px" value="查询" onclick="search();"/>
                        <input type="button" style="border-radius:3px;margin-left:10px" value="审核通过" onclick="pass();"/>
                        <input type="button" style="border-radius:3px;margin-left:10px" value="不通过"
                               onclick="unpass();"/>
                        <input type="button" style="border-radius:3px;margin-left:10px" value="导出"
                               onclick="exportList();"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="cardAudit_query_div" style="width: 100%; position: absolute; top: 115px ; left: 0 ; bottom: 0;">
        <table id="cardAudit_query" style="display: none;"/>
    </div>
</div>

<div id="cardAudit_detail" class="easyui-dialog" data-options="closed:true"></div>
<div id="cardAudit_compare" class="easyui-dialog" data-options="closed:true"></div>
</body>
</html>
