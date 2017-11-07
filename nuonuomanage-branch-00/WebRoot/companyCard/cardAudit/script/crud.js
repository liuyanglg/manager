function pass(){
	var rows = $("#cardAudit_query").datagrid('getSelections');
	var len = rows.length;
    var ids=[];
	if(len!=0){
		var isCanPass=true;
	    for(var i=0;i<rows.length;i++)
		{
	    	ids.push(rows[i].id);
	    	if(editIndex!=null){
	    		var index=$("#cardAudit_query").datagrid('getRowIndex',rows[i]);
	    		if(editIndex!=index){
	    			var thisRowCanPass=validateValue();
			    	if(!thisRowCanPass){
			    		isCanPass=false;
			    		break;
			    	}
	    		}
	    	}
		}
        var params = $.param({'ids':ids},true);
        if(!isCanPass){
			return;
	    }
		 $.messager.confirm("确认", "确认审核通过吗？", function (btn) {
			 if (btn) {
				 var isSuccess=endEditing(undefined,true);
				 if(!isSuccess){
					 return;
				 }
			     $.ajax({
			    	 url: 'companyCard/cardAudit/approveCardAudit.action?',
                     data:params,
			    	 type: 'POST',
			    	 success: function(res){
			    		 if(res!="success"){
			    			 $.messager.alert("提示","操作出错，请核对正式库、审核库及审核日志数据！");
			    		 }
			    		 $("#cardAudit_query").datagrid("clearSelections");  
			    		 var data = $("#searchForm").serialize();
			      		 $("#cardAudit_query").datagrid({
			      			 url:'companyCard/cardAudit/queryCardAudit.action?' + data,
			      		 });
			    	 }
			     });
			  }  
		 });
			return false;
	}else{
		$.messager.alert("操作提示", "请选择数据再进行操作!");
			return;
	}
}

function unpass(){
	var rows = $("#cardAudit_query").datagrid('getSelections');
    var ids=[];
    for(var i=0;i<rows.length;i++)
	{ids.push(rows[i].id);}
	var len = rows.length;
	if(len!=0){
        var params = $.param({'ids':ids},true);
        $.messager.confirm("确认", "确认审核不通过吗？", function (btn) {
			 if (btn) {
			     $.ajax({
			    	 url: 'companyCard/cardAudit/notApproveCardAudit.action?',
                     data:params,
			    	 type: 'POST',
			    	 success: function(res){
			    		 if(res!="success"){
			    			 $.messager.alert("提示","操作出错，请核对正式库、审核库及审核日志数据！");
			    		 }
			    		 $("#cardAudit_query").datagrid("clearSelections");  
			    		 var data = $("#searchForm").serialize();
			      		 $("#cardAudit_query").datagrid({
			      			 url:'companyCard/cardAudit/queryCardAudit.action?' + data,
			      		 });
			    	 }
			     });
			  }  
		 });
			return false;
	}else{
		$.messager.alert("操作提示", "请选择数据再进行操作!");
			return;
	}
}

function compare(id){
	$('#cardAudit_compare').dialog({
		title: '开票信息对比',   
	    iconCls:'icon-edit',  
	    width: 1200,    
	    height:250,
	    resizable:true,
	    closed: false,    
	    cache: false,    
	    href: 'companyCard/cardAudit/compareCardAudit.action?id='+id,
	    modal: true, 
	    onClose:function(){ 
        //$(this).dialog('destroy');//销毁 
       }
	});
}

function compareNT(id){
	$('#cardAudit_compare').dialog({
		title: '开票信息对比',   
	    iconCls:'icon-edit',  
	    width: 1200,    
	    height:250,
	    resizable:true,
	    closed: false,    
	    cache: false,    
	    href: 'companyCard/cardAudit/compareCardAuditNT.action?id='+id,
	    modal: true, 
	    onClose:function(){ 
        //$(this).dialog('destroy');//销毁 
       }
	});
}

function exportList(){
    if(type=="2"){
        if($('#nameOrTax').val().length<4){
            $.messager.alert("操作提示", "企业名称大于4位!");
        }else{
            $.ajax({
                url:"companyCard/cardAudit/exportMatch.action?nameOrTax="+encodeURIComponent($("#nameOrTax").val()),
                type: 'POST',
                success: function(res){
                    if(res=="error"){
                        $.messager.alert("提示","导出条数超出最大限制！");
                    }else {
                        window.open("companyCard/cardAudit/exportMatch.action?nameOrTax="+encodeURIComponent($("#nameOrTax").val()));
                    }
                }
            });
        }
    }else{
        var data = $("#searchForm").serialize();
        $.ajax({
			url:"companyCard/cardAudit/exportList.action?"+data,
            type: 'POST',
            success: function(res){
                if(res=="error"){
                    $.messager.alert("提示","导出条数超出最大限制！");
                }else {
                    window.open("companyCard/cardAudit/exportList.action?"+data);
                }
            }
        });
    }
}