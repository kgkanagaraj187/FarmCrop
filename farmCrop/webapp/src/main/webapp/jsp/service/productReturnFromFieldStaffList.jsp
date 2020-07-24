<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/grid-assets.jsp"%>
<html>
<META name="decorator" content="swithlayout">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){	
	jQuery("#detail").jqGrid(
	{
	   	url:'productReturnFromFieldStaff_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	colNames:[	
				    <s:if test='branchId==null'>
					'<s:text name="app.branch"/>',
				   </s:if>
	  		   	  '<s:text name="warehouse"/>',
	  		      '<s:text name="mobileUser"/>',
	  		    '<s:text name="category"/>',
	  		  '<s:text name="product"/>'
				 ],
	   	colModel:[
				<s:if test='branchId==null'>
					{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
				</s:if>
	   			{name:'warehouse',index:'warehouse',width:125,sortable:true},
	   			{name:'mobileUser',index:'mobileUser',width:125,sortable:true},	   		
	   			{name:'category',index:'category',width:125,sortable:true},
	   			{name:'product',index:'product',width:125,sortable:true}
				 ],
	   	height: 301, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:10,
	   	rowList : [10,25,50],
	    sortname:'id',			  
	    sortorder: "desc",
	    loadonce: true,
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	    onSelectRow: function(id){ 
		  document.detail.id.value  =id;
          document.detail.submit();      
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
});
</script>
<sec:authorize ifAllGranted="service.distribution.fieldStaff.create">
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
<s:form name="createform" action="productReturnFromFieldStaff_create">

</s:form>
<s:form name="detail" action="productReturnFromFieldStaff_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
</body>
</html>