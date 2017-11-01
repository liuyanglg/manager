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
    <title>分配六位代码</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<script type="text/javascript" src="companyCard/codeAssign/script/main.js"></script>

<body>
<script type="text/javascript">
    $(document).ready(function () {
        searchCompany();
        companyOnSelect();
    });

    $(function () {
        $.extend($.fn.validatebox.defaults.rules, {
            assignAmountRex: {
                validator: function (value) {
                    var rex = /^([1-9]\d{0,3}|10000)$/;
                    if (rex.test(value)) {
                        return true;
                    } else {
                        return false;
                    }
                },
                message: '分配数量必须在1到10000之间'
            }
        });
    });
</script>
<div align="center">
    <form id="cmp_code_assign_form" method="post">
        <input name="init" type="hidden" value="0"/>
        <table style="padding:10px 0 0 0 ;width:98%;font-size: 14px;margin: 0 auto;border-collapse: separate;border-spacing: 15px">
            <tr>
                <th align="right">分配数量:</th>
                <td>
                    <input id="cmp_code_assign_set_assign_amount" type="text" name="assignAmount"
                           class='easyui-validatebox'
                           data-options="required:true,missingMessage:'分配数量不能为空'" validType="assignAmountRex"/>
                </td>
            </tr>
            <tr>
                <th align="right">分配企业:</th>
                <td>
                    <select id="cmp_code_assign_set_assign_company_name" type="text" style="width:154px"
                            data-options="valueField:'assignCompanyTaxid',textField:'assignCompanyName',required:true,missingMessage:'企业名不能为空'"
                            class="easyui-combobox" validType="length[2,20]">
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">企业税号:</th>
                <td>
                    <input id="cmp_code_assign_set_assign_company_taxid" type="text"
                           style="width:154px;border-style:none" disabled="disabled"/>
                </td>
            </tr>

            <tr>
                <th align="right">分配员工:</th>
                <td>
                    <select id="cmp_code_assign_set_assign_employee" type="text" style="width:154px"
                            data-options="valueField:'assignEmployeeId',textField:'assignEmployeeName'"
                            class="easyui-combobox" editable="false">
                    </select>
                    <input id="cmp_code_assign_set_assign_employee_mobile" type="hidden"
                           style="width:154px;border-style:none" disabled="disabled"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
