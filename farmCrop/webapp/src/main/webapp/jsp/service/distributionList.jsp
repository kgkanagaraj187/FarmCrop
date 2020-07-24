<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/grid-assets.jsp"%>
<html>
<META name="decorator" content="swithlayout">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){	
	jQuery("#detail").jqGrid(
	{
	   	url:'distribution_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   /* 	postData:{
	  	  <s:if test='branchId==null'>
			  "branchIdParma" : function(){			  
			      return document.getElementById("branchIdParma").value;
	 		  },
	 		  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
		  "subBranchIdParam" : function(){
			  return document.getElementById("subBranchIdParam").value;
		  },
		  </s:if>
			  </s:if>
	   	}, */
	    colNames:[		
		<s:if test='branchId==null'>
			'<s:text name="app.branch"/>',
		</s:if>
		 /*  '<s:text name="txnDate"/>', */
		  /* '<s:text name='%{"location"+type}'/>', */
		  '<s:property value="%{getLocaleProperty('stockType')}" />',
		  '<s:property value="%{getLocaleProperty('warehouse')}" />',
		  '<s:property value="%{getLocaleProperty('profile.agent')}" />',
		/*   '<s:text name="farmerType"/>', */
		 '<s:property value="%{getLocaleProperty('farmer')}" />',
		  <s:if test="currentTenantId=='pratibha'">
		   '<s:text name="%{getLocaleProperty('fatherName')}"/>', 
		   </s:if>
		  '<s:property value="%{getLocaleProperty('village.name')}" />',
		 /*  '<s:text name="farmer.mobileNumber"/>',
		  '<s:text name="freeDist"/>',
		  '<s:text name="totalQty"/>',
		  '<s:text name="grossAmt"/>',
		  '<s:text name="tax"/>',
		  '<s:text name="finalAmt"/>',
		  '<s:text name="cash"/>',
		  '<s:text name="credit"/>', */
		  '<s:text name="seasonCode"/>'
	     
	 ],   
	colModel:[	
	<s:if test='branchId==null'>
					{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
	</s:if>		
	/* {name:'txnDate',index:'a1.txnTime',width:270,sortable:false}, */
	/* {name:'type1',index:'type1',width:250,sortable:false}, */
	{name:'stockType',index:'stockType',width:280,sortable:true,stype: 'select',searchoptions: { value: '<s:property value="stockTypeText"/>' }},
	{name:'warehouseName',index:'warehouseName',width:280,sortable:true},
	{name:'agentName',index:'agentName',width:280,sortable:true},
	/* {name:'farmerType',index:'farmerType',width:280,sortable:false}, */
	{name:'farmer',index:'farmer',width:180,sortable:true},
	<s:if test="currentTenantId=='pratibha'">
	 {name:'lastName',index:'lastName',width:180,sortable:true}, 
	 </s:if>
	{name:'village',index:'village',width:180,sortable:true},
	/* {name:'farmer.mobileNumber',index:'farmer.mobileNumber',width:220,sortable:false},
	{name:'freeDist',index:'freeDist',width:280,sortable:false},
	{name:'totalQty',index:'totalQty',align:'right',width:220,sortable:false},
	{name:'grossAmt',index:'grossAmt',align:'right',width:220,sortable:false},
	{name:'tax',index:'tax',width:200,align:'right',sortable:false},
	{name:'finalAmt',index:'finalAmt',width:200,align:'right',sortable:false},
	{name:'cash',index:'cash',width:170,align:'right',sortable:false},
	{name:'credit',index:'credit',width:170,align:'right',sortable:false}, */
	{name:'seasonCode',index:'seasonCode',width:150,sortable:true}
  	],
	
height: 301, 
width: $("#baseDiv").width(),
scrollOffset: 0,
rowNum:10,
rowList : [10,25,50],
autowidth:true,
shrinkToFit:true,	
viewrecords: true  ,

onSelectRow: function(id){ 
	  document.detail.id.value  =id;
    document.detail.submit();      
	},	
beforeSelectRow: function(rowid, e) {
    var iCol = jQuery.jgrid.getCellIndex(e.target);
    if (iCol >11){return false; }
    else{ return true; }
},
loadComplete: function(){//in subgrid 
	 <s:if test="type == 'agent'">	
	jQuery("#detail").jqGrid('hideCol', "subgrid");
	</s:if>
},


sortname:'id',
sortorder: "desc",
subGrid: false,
subGridOptions: {
"plusicon" : "glyphicon-plus-sign",
"minusicon" : "glyphicon-minus-sign",
"openicon" : "glyphicon-hand-right",
},
 subGridRowExpanded: function(subgrid_id, row_id){
   	
   var subgrid_table_id, pager_id; 
   subgrid_table_id = subgrid_id+"_t"; 
   pager_id = "p_"+subgrid_table_id; 
 $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
   			   	jQuery("#"+subgrid_table_id).jqGrid(
			{
				url:'distribution_data.action?id='+row_id+'&identityForGrid='+"distributionDetail",
			   	pager: pager_id,
			    styleUI : 'Bootstrap',
			   	datatype: "json",	
			   	colNames:[				   	       	  
                          '<s:text name="categoryName"/>',	
			  		      '<s:text name="productName"/>',
			  		      '<s:text name="existingQty"/>',
			  		      '<s:text name="cstPrice"/>',
			  		      '<s:text name="sellingPrice"/>',
			  		      '<s:text name="amount"/>',
			  		      '<s:text name="distQty"/>',
			  		      '<s:text name="avlQty"/>'
			  		      
	 	      	 ],
			   	colModel:[						
							{name:'subCategoryName',index:'subCategoryName',sortable:false},
							{name:'productName',index:'productName',sortable:false},
							{name:'existingQty',index:'existingQty',sortable:false,align:'right'},
							{name:'cstPrice',index:'cstPrice',align:'right',sortable:false,},
							{name:'sellingPrice',index:'sellingPrifieldsetce',align:'right',sortable:false},
							{name:'amount',index:'amount',align:'right',sortable:false},
							{name:'distQty',index:'distQty',align:'right',sortable:false},
							{name:'avlQty',index:'avlQty',align:'right',sortable:false}
		
			   	],
			   	scrollOffset: 0, 
			    sortname:'id',
			    height: '100%', 
			    sortorder: "desc",
			    autowidth: true,
			    rowNum:10,
				rowList : [10,25,50],
			    viewrecords: true
});


    jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
   			//jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
		    jQuery("#"+subgrid_id).parent().css("width","100%");
		    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
		    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
		    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
		    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");
},		
onSortCol: function (index, idxcol, sortorder) {
if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
        && this.p.colModel[this.p.lastsort].sortable !== false) {
    $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
}
}
});	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

	colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;

        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        }
    });	
    $('#detail').jqGrid('setGridHeight',(windowHeight));
});
</script>

<sec:authorize ifAllGranted="service.distribution.farmer.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
 <div class="flex-layout reportData">
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
</div>
<s:form name="createform" action="distribution_create">

</s:form>
<s:form name="detail" action="distribution_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
</body>
</html>