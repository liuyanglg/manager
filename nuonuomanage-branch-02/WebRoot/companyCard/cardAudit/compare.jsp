<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<body>
<script type="text/javascript">
    function merge() {
        if (!$("#auditId").is(':checked')) {
            $.messager.alert("提示", "请选择需要合并的数据！");
            return;
        }
        if ($("#nameCode").is(':checked') && $("#taxCode").is(':checked')) {
            $.messager.alert("提示", "请选择1条需要合并的数据！");
            return;
        }
        if (!$("#nameCode").is(':checked') && !$("#taxCode").is(':checked')) {
            $.messager.alert("提示", "请选择1条需要合并的数据！");
            return;
        }
        $.messager.confirm("确认", "确认审核通过并且合并选中的数据吗？", function (btn) {
            if (btn) {
                var auditId = $("#auditId").val();
                var code;
                var type;//1-nameCode 2-taxCode
                if ($("#nameCode").is(':checked')) {
                    code = $("#nameCode").val();
                    type = 1;
                }
                else if ($("#taxCode").is(':checked')) {
                    code = $("#taxCode").val();
                    type = 2;
                }
                $.ajax({
                    url: 'companyCard/cardAudit/merge.action?auditId=' + auditId + "&code=" + code + "&type=" + type,
                    type: 'POST',
                    success: function (res) {
                        if (res != "success") {
                            $.messager.alert("提示", "操作出错，请核对正式库、审核库及审核日志数据！");
                        }
                        else {
                            $('#cardAudit_compare').dialog('close');
                            $("#cardAudit_query").datagrid("clearSelections");
                            var data = $("#searchForm").serialize();
                            $("#cardAudit_query").datagrid({
                                url: 'companyCard/cardAudit/queryCardAudit.action?' + data,
                            });
                        }
                    },
                    error: function () {
                        $.messager.alert("提示", "操作出错，请核对正式库、审核库及审核日志数据！");
                    }
                });
            }
        });
    }

    function noPass() {
        if (!$("#auditId").is(':checked')) {
            $.messager.alert("提示", "请选择需要处理的数据！");
            return;
        }
        $.messager.confirm("确认", "确认不审核通过吗？", function (btn) {
            if (btn) {
                var auditId = $("#auditId").val();
                $.ajax({
                    url: 'companyCard/cardAudit/notApproveCardAudit.action?ids=' + auditId,
                    type: 'POST',
                    success: function (res) {
                        if (res != "success") {
                            $.messager.alert("提示", "操作出错，请核对正式库、审核库及审核日志数据！");
                        }
                        else {
                            $('#cardAudit_compare').dialog('close');
                            $("#cardAudit_query").datagrid("clearSelections");
                            var data = $("#searchForm").serialize();
                            $("#cardAudit_query").datagrid({
                                url: 'companyCard/cardAudit/queryCardAudit.action?' + data,
                            });
                        }
                    },
                    error: function () {
                        $.messager.alert("提示", "操作出错，请核对正式库、审核库及审核日志数据！");
                    }
                });
            }
        });
    }
</script>
<style>
    .m-window {
        float: left;
        width: 376px;
        margin-top: 10px;
        margin-left: 10px;
    }

    .name, .address {
        float: left;
        display: inline-block;
        line-height: 22px;
    }

    .address {
        width: 274px;
        overflow: hidden;
    }

    .name {
        width: 102px;
        text-align: right;
    }

    .m-table {
        width: 100%;
        text-align: center;
        border-collapse: collapse;
    }

    .m-table td {
        border: 1px solid #000;
        font-size: 16px
    }

    .formal td {
        color: #B9B9B9;
    }

    .color {
        color: red;
    }
</style>
<table class="m-table">
    <tr>
        <c:if test="${merge==1}">
            <td>选择</td>
        </c:if>
        <td>数据库</td>
        <td>数据源</td>
        <td>六位代码</td>
        <td>企业名称</td>
        <td>税号</td>
        <td>地址</td>
        <td>电话</td>
        <td>开户行</td>
        <td>银行账号</td>
        <td>认证状态</td>
        <td>纳税人标识</td>
    </tr>
    <tr>
        <c:if test="${merge==1}">
            <td><input type="checkbox" id="auditId" value="${cardAudit.id}" checked="checked"/></td>
        </c:if>
        <td>审核库</td>
        <td <c:if test="${source_cp0==1||source_cp1==1||source_cp2==1}">class="color"</c:if>>${cardAudit.sourceStr}</td>
        <td>${cardAudit.code}</td>
        <td <c:if test="${name_cp0==1||name_cp1==1||name_cp2==1}">class="color"</c:if>>${cardAudit.name}</td>
        <td <c:if test="${taxid_cp0==1||taxid_cp1==1||taxid_cp2==1}">class="color"</c:if>>${cardAudit.taxid}</td>
        <td
                <c:if test="${address_cp0==1||address_cp1==1||address_cp2==1}">class="color"</c:if>>${cardAudit.address}</td>
        <td
                <c:if test="${telephone_cp0==1||telephone_cp1==1||telephone_cp2==1}">class="color"</c:if>>${cardAudit.telephone}</td>
        <td <c:if test="${bank_cp0==1||bank_cp1==1||bank_cp2==1}">class="color"</c:if>>${cardAudit.bank}</td>
        <td
                <c:if test="${account_cp0==1||account_cp1==1||account_cp2==1}">class="color"</c:if>>${cardAudit.account}</td>
        <td <c:if test="${cert_cp0==1||cert_cp1==1||cert_cp2==1}">class="color"</c:if>>${cardAudit.certStr}</td>
        <td <c:if test="${type_cp0==1||type_cp1==1||type_cp2==1}">class="color"</c:if>>${cardAudit.typeStr}</td>
    </tr>
    <c:if test="${merge==1}">
        <c:if test="${nameCard!=null}">
            <tr class="formal">
                <td><input type="checkbox" id="nameCode" value="${nameCard.code}" checked="checked"/></td>
                <td>正式库</td>
                <td>${nameCard.sourceStr}</td>
                <td>${nameCard.code}</td>
                <td>${nameCard.name}</td>
                <td>${nameCard.taxid}</td>
                <td>${nameCard.address}</td>
                <td>${nameCard.telephone}</td>
                <td>${nameCard.bank}</td>
                <td>${nameCard.account}</td>
                <td>${nameCard.certStr}</td>
                <td>${nameCard.typeStr}</td>
            </tr>
        </c:if>
        <c:if test="${taxCard!=null}">
            <tr class="formal">
                <td><input type="checkbox" id="taxCode" value="${taxCard.code}" checked="checked"/></td>
                <td>正式库</td>
                <td>${taxCard.sourceStr}</td>
                <td>${taxCard.code}</td>
                <td>${taxCard.name}</td>
                <td>${taxCard.taxid}</td>
                <td>${taxCard.address}</td>
                <td>${taxCard.telephone}</td>
                <td>${taxCard.bank}</td>
                <td>${taxCard.account}</td>
                <td>${taxCard.certStr}</td>
                <td>${taxCard.typeStr}</td>
            </tr>
        </c:if>
        <tr>
            <td colspan="12" align="center"><input type="button" style="margin:5px 5px 5px 20px;" value="审核通过"
                                                   onclick="merge()"/><input type="button" style="margin-left:50px"
                                                                             value="不通过" onclick="noPass()"/></td>
        </tr>
    </c:if>
    <c:if test="${merge==0}">
        <tr class="formal">
            <td>正式库</td>
            <td>${cmpCard.sourceStr}</td>
            <td>${cmpCard.code}</td>
            <td>${cmpCard.name}</td>
            <td>${cmpCard.taxid}</td>
            <td>${cmpCard.address}</td>
            <td>${cmpCard.telephone}</td>
            <td>${cmpCard.bank}</td>
            <td>${cmpCard.account}</td>
            <td>${cmpCard.certStr}</td>
            <td>${cmpCard.typeStr}</td>
        </tr>

        <c:if test="${nameCard!=null}">
            <tr class="formal">
                <td>正式库</td>
                <td>${nameCard.sourceStr}</td>
                <td>${nameCard.code}</td>
                <td>${nameCard.name}</td>
                <td>${nameCard.taxid}</td>
                <td>${nameCard.address}</td>
                <td>${nameCard.telephone}</td>
                <td>${nameCard.bank}</td>
                <td>${nameCard.account}</td>
                <td>${nameCard.certStr}</td>
                <td>${nameCard.typeStr}</td>
            </tr>
        </c:if>
    </c:if>
</table>
</body>
