//添加开票信息
function add() {
    $('#cmpMaintain_add_div').dialog({
        title: '添加开票信息',
        iconCls: 'icon-add',
        width: 650,
        height: 280,
        closed: false,
        cache: false,
        href: 'companyCard/cardMaintain/add.jsp',
        modal: true,
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: function () {
                $('#add_form').form('submit', {
                    type: "post",
                    url: "companyCard/cardMaintain/addCardMaintain.action",
                    onSubmit: function () {
                        $.messager.progress({
                            title: '请等待',
                            msg: '',
                            text: '正在提交...'
                        });
                        if (!$(this).form('validate')) {
                            $.messager.progress('close');
                            return false;
                        }
                        return true;
                    },
                    success: function (result, rec) {
                        if (result == "success") {
                            $.messager.alert("提示", "操作成功");
                            $('#cmpMaintain_query').datagrid('reload');
                            $('#cmpMaintain_add_div').dialog('close');
                        } else {
                            $.messager.alert("提示", "添加失败");
                        }
                        $.messager.progress('close');
                    }
                });
            }
        }, {
            text: '取消',
            handler: function () {
                $('#cmpMaintain_add_div').dialog('close');
            }
        }]
    });
}

/**
 * 删除开票信息
 *
 */
function del() {
    var rows = $("#cmpMaintain_query").datagrid('getSelections');
    var len = rows.length;
    if (len == 0) {
        $.messager.alert("操作提示", "请选择数据再进行删除操作!");
        return;
    } else if (len > 3) {
        $.messager.alert("操作提示", "批量删除操作最多支持3条!");
        return;
    }
    var codes = [];
    for (var i = 0; i < rows.length; i++) {
        codes.push(rows[i].code);
    }
    var params = $.param({'codes': codes}, true);
    $.messager.confirm("确认", "是否删除选择的数据,删除后将无法进行恢复", function (btn) {
        if (!btn) return;
        $.ajax({
            url: 'companyCard/cardMaintain/deleteCardMaintain.action',
            data: params,
            success: function (result) {
                if (result == "success") {
                    $.messager.alert("提示", "删除成功");
                } else {
                    $.messager.alert("提示", "删除出错");
                }
                $('#cmpMaintain_query').querygrid("reload");
                $('#cmpMaintain_query').querygrid("clearSelections");
            }
        });
    });
}

function query() {
    $('#cmpMaintain_search_div').dialog({
        title: '查询',
        iconCls: 'icon-edit',
        width: 700,
        height: 250,
        closed: false,
        cache: false,
        href: 'companyCard/cardMaintain/search.jsp',
        modal: true,
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: function () {
                var name = $("input[name='name']").hasClass("validatebox-invalid");
                var code = $("input[name='code']").hasClass("validatebox-invalid");
                var len = $('#code').val().length;
                if (len != 6 && len != 0) {
                    alert("请输入六位开票代码");
                    return false;
                }
                if (name || code) {
                    return false;
                }
                $("#cmpMaintain_query").datagrid({
                    url: "companyCard/cardMaintain/queryCardMaintain.action?" + $("#cmpMaintain_search_form").serialize(),
                    pageNumber: 1,//显示第一页
                });
                $("#cmpMaintain_query").datagrid('clearSelections');
                $("#cmpMaintain_search_div").dialog('close');
            }
        }, {
            text: '取消',
            handler: function () {
                $('#cmpMaintain_search_div').dialog('close');
            }
        }]
    });
}

function mod() {
    var rows = $("#cmpMaintain_query").datagrid('getSelections');
    var len = rows.length;
    if (len == 1) {
        $('#cmpMaintain_edit_div').dialog({
            title: '修改开票信息',
            iconCls: 'icon-edit',
            width: 700,
            height: 300,
            closed: false,
            cache: false,
            async: true,
            href: 'companyCard/cardMaintain/bmodCardMaintain.action?code=' + rows[0].code,
            modal: true,
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function () {
                    $("#cmpMaintain_edit_form").form('submit', {
                        url: 'companyCard/cardMaintain/updateCardMaintain.action',
                        onSubmit: function () {
                            var len = $('#code').val().length;
                            if (len != 6 && len != 0) {
                                alert("请输入六位开票代码");
                                return false;
                            }
                            if (!$(this).form('validate')) {
                                return false;
                            }
                            return true;
                        }, success: function (data) {
                            if (data == 'success') {
                                $("#cmpMaintain_query").datagrid('clearSelections');
                                $('#cmpMaintain_edit_div').dialog('close');
                                $.messager.alert("操作提示", "修改成功");
                                $("#cmpMaintain_query").querygrid('reload');
                            } else {
                                $.messager.alert("操作提示", "修改失败");
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                handler: function () {
                    $('#cmpMaintain_edit_div').dialog('close');
                }
            }]
        });
    } else {
        $.messager.alert("操作提示", "请选择一行进行操作！");
    }
}

function shield() {
    var rows = $("#cmpMaintain_query").datagrid('getSelections');
    var len = rows.length;
    if (len == 0) {
        $.messager.alert("操作提示", "请选择数据再进行屏蔽操作!");
        return;
    } else if (len > 10) {
        $.messager.alert("操作提示", "批量屏蔽操作最多支持10条!");
        return;
    }
    var codes = [];
    for (var i = 0; i < rows.length; i++) {
        codes.push(rows[i].code);
    }
    var params = $.param({'codes': codes}, true);
    $.messager.confirm("确认", "是否屏蔽选择的数据？", function (btn) {
        if (!btn) return;
        $.ajax({
            url: 'companyCard/cardMaintain/shieldCardMaintain.action',
            data: params,
            success: function (result) {
                if (result == "success") {
                    $.messager.alert("提示", "屏蔽成功");
                } else {
                    $.messager.alert("提示", "屏蔽出错");
                }
                $('#cmpMaintain_query').querygrid("reload");
                $('#cmpMaintain_query').querygrid("clearSelections");
            }
        });
    });
}

function editCode() {
    var rows = $("#cmpMaintain_query").datagrid('getSelections');
    var len = rows.length;
    if (len == 1) {
        var oldCode = rows[0].code;
        $('#cmpMaintain_edit_div').dialog({
            title: '修改六位代码',
            iconCls: 'icon-edit',
            width: 413,
            height: 161,
            closed: false,
            cache: false,
            async: true,
            href: 'companyCard/cardMaintain/hrefEditCode.action?oldCode=' + oldCode,
            modal: true,
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function () {
                    $("#cmp_maintain_edit_code_form").form('submit', {
                        url: 'companyCard/cardMaintain/editCode.action' + '?oldCode=' + oldCode,
                        onSubmit: function () {
                            var len = $('#code').val().length;
                            if (len != 6 && len != 0) {
                                alert("请输入六位开票代码");
                                return false;
                            }
                            if (!$(this).form('validate')) {
                                return false;
                            }
                            return true;
                        }, success: function (data) {
                            if (data == 'success') {
                                $("#cmpMaintain_query").datagrid('clearSelections');
                                $('#cmpMaintain_edit_div').dialog('close');
                                $.messager.alert("操作提示", "修改成功");
                                $("#cmpMaintain_query").querygrid('reload');
                            } else {
                                $.messager.alert("操作提示", "修改失败");
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                handler: function () {
                    $('#cmpMaintain_edit_div').dialog('close');
                }
            }]
        });
    } else {
        $.messager.alert("操作提示", "请选择一行进行操作！");
    }
}

function detail() {
}