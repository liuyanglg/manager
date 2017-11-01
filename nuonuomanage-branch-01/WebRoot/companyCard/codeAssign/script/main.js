function assignCodes() {
    $('#cmp_code_assign_assign_div').dialog({
        title: '分配六位代码',
        iconCls: 'icon-add',
        width: 320,
        height: 400,
        closed: false,
        cache: false,
        href: 'companyCard/codeAssign/assign.jsp',
        modal: true,
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: function () {
                var assignCompanyName = $('#cmp_code_assign_set_assign_company_name').combobox('getText');
                var assignCompanyTaxid = $('#cmp_code_assign_set_assign_company_name').combobox('getValue');
                var assignEmployeeName = $('#cmp_code_assign_set_assign_employee').combobox('getText');
                var assignEmployeeId = $('#cmp_code_assign_set_assign_employee').combobox('getValue');
                var assignAmount = $('#cmp_code_assign_set_assign_amount').val();
                if (assignEmployeeName.indexOf("---请选择---") != -1) {
                    assignEmployeeName = "";
                }
                if (assignEmployeeId.indexOf("---请选择---") != -1) {
                    assignEmployeeId = "";
                }
                var data = "";
                data = convertParams(data, assignCompanyName, "assignCompanyName");
                data = convertParams(data, assignCompanyTaxid, "assignCompanyTaxid");
                data = convertParams(data, assignEmployeeName, "assignEmployeeName");
                data = convertParams(data, assignEmployeeId, "assignEmployeeId");
                data = convertParams(data, assignAmount, "assignAmount");
                // console.log("params data:", data);
                $('#cmp_code_assign_form').form('submit', {
                    type: "post",
                    url: 'companyCard/codeAssign/assignCodes.action?' + data,
                    onSubmit: function () {
                        $.messager.progress({
                            title: '请等待',
                            msg: '',
                            text: '正在提交...'
                        });
                        var assignCompanyTaxid = $('#cmp_code_assign_set_assign_company_taxid').val();
                        if (!(assignCompanyTaxid != null && assignCompanyTaxid.trim().length > 0)) {
                            $.messager.progress('close');
                            $.messager.alert("提示", "企业税号为空，请检查！");
                            return false;
                        }
                        if (!$(this).form('validate')) {
                            $.messager.progress('close');
                            return false;
                        }
                        return true;
                    },
                    success: function (result, rec) {
                        if (result == "success") {
                            $.messager.alert("提示", "分配成功");
                            $('#cmp_code_assign_query_table').datagrid('reload');
                            $('#cmp_code_assign_assign_div').dialog('close');
                        } else {
                            $.messager.alert("提示", "分配失败");
                        }
                        $.messager.progress('close');
                    }
                });
            }
        }, {
            text: '取消',
            handler: function () {
                $('#cmp_code_assign_assign_div').dialog('close');
            }
        }]
    });
}

function searchCompany() {
    $('#cmp_code_assign_set_assign_company_name').combobox({
        onChange: function (newValue, oldValue) {
            $.ajax({
                url: 'companyCard/codeAssign/queryCompanyInfo.action?assignCompanyName=' + newValue,
                dataType: 'json',
                success: function (data) {
                    if (data == null) {
                        $('#cmp_code_assign_set_assign_company_name').combobox("hidePanel");
                        data = new Array();
                    } else if (data.length > 1) {
                        $('#cmp_code_assign_set_assign_company_name').combobox("loadData", data);
                        $('#cmp_code_assign_set_assign_company_name').combobox("showPanel");
                    }
                }
            });
        }
    });
}

function companyOnSelect() {
    $('#cmp_code_assign_set_assign_company_name').combobox({
        onSelect: function () {
            setAssignCompanyTaxid();
            setAssignEmployees();
        }
    });
}

function setAssignCompanyTaxid() {
    var assignCompanyTaxid = $('#cmp_code_assign_set_assign_company_name').combobox('getValue');
    $('#cmp_code_assign_set_assign_company_taxid').val(assignCompanyTaxid);
}

function setAssignEmployees() {
    var assignCompanyTaxid = $('#cmp_code_assign_set_assign_company_name').combobox('getValue');
    $.ajax({
        url: 'companyCard/codeAssign/queryEmployeeInfo.action?assignCompanyTaxid=' + assignCompanyTaxid,
        dataType: 'json',
        success: function (data) {
            $('#cmp_code_assign_set_assign_employee').combobox("setValue", "---请选择---");
            $('#cmp_code_assign_set_assign_employee').combobox("loadData", data);
        }
    });
}

function employeeOnSelect() {
    $('#cmp_code_assign_set_assign_company_name').combobox({
        onSelect: function () {
        }
    });
}

function convertParams(data, paramValue, paramName) {
    if (paramValue.trim().length > 0) {
        if (data != null && data.trim().length > 0) {
            data += "&";
        }
        data += paramName + "=" + paramValue;
    }
    return data;
}




