<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'modifyCode.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
</head>
<body>
<script type="text/javascript" src="companyCard/cardMaintain/script/crud.js"></script>
<script type="text/javascript">
    $(function () {
        $.extend($.fn.validatebox.defaults.rules, {
            companyCodeRex: {
                validator: function (value) {
                    var rex = /^[A-Z2-9]{6}$/;
                    if (rex.test(value)) {
                        return true;
                    } else {
                        return false;
                    }
                },
                message: '六位代码必须是(2-9,A-Z)中的6位大写字母或数字组成'
            }
        });
    });

    function validateCode() {
        var row = $("#cmpMaintain_query").datagrid('getSelected');
        var id = row.id;
        var oldCode = row.code;
        var code = $("#cmp_maintain_edit_code").val();
        if (code.length == 6) {
            $.ajax({
                type: 'POST',
                url: 'companyCard/cardMaintain/checkCard.action' + "?oldCode=" + oldCode + "&code=" + code,
                dataType: 'json',
                success: function (result) {
                    var data = result;
                    if (data.code != true) {
                        $("#cmp_maintain_edit_code_ico").attr('src', 'Contents/common/easyui/themes/icons/no.png');
                        $.messager.alert("提示", data.message);
                    } else {
                        $("#cmp_maintain_edit_code_ico").attr('src', 'Contents/common/easyui/themes/icons/ok.png');
                        $("#cmp_maintain_edit_code_message").val("");
                    }

                }
            });
        }
    }
</script>
<div style="padding:35px 0 0 0;width:95%;font-size: 14px;margin: 0 auto;" >
    <form id="cmp_maintain_edit_code_form" method="post">
        <%--<div style="padding:30px 0 0 0;width:98%;font-size: 14px;margin: 0 auto;">--%>
        <label for="cmp_maintain_edit_code" style="margin-left: 60px">六位代码:</label>
        <input type="text" name="code" value="${code}" id="cmp_maintain_edit_code"
               class='easyui-validatebox'
               data-options="required:true,missingMessage:'六位代码不能为空'" validType="companyCodeRex"
               onchange="validateCode()"/>
        <img id="cmp_maintain_edit_code_ico" style="padding-top: 2px"/>
        <%--<label for="cmp_maintain_edit_code" style="margin-left: 10px">(点这里检测六位代码)</label>--%>
    </form>
</div>
</body>
</html>
