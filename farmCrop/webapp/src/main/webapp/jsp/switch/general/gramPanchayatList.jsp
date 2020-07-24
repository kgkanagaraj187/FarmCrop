<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){	
	jQuery("#detail").jqGrid(
	{
	   	url:'gramPanchayat_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	colNames:[	
	   	          
	   	       <s:if test='branchId==null'>
				'<s:text name="app.branch"/>',
			   </s:if>
	  		   	  '<s:text name="grampanchayat.code"/>',
	  		      '<s:text name="grampanchayat.name"/>',
		  		  '<s:property value="%{getLocaleProperty('city.name')}" />'
				 ],
	   	colModel:[
	   	          
				<s:if test='branchId==null'>
				{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
				</s:if>  
	   			{name:'code',index:'code',width:125,sortable:true},
	   			{name:'name',index:'name',width:125,sortable:true},
	   			{name:'c.name',index:'c.name',width:125,sortable:true}
				 ],
	   	height: 301, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:10,
	   	rowList : [10,25,50],
	    sortname:'id',			  
	    sortorder: "desc",
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	    onSelectRow: function(id){ 
		  document.updateform.id.value  =id;
          document.updateform.submit();      
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
	jQuery("#detail").jqGrid("setLabel","code","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","name","",{"text-align":"center"});
	jQuery("#detail").jqGrid("setLabel","c.name","",{"text-align":"center"});

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
<sec:authorize ifAllGranted="profile.location.gramPanchayat.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
<s:form name="createform" action="gramPanchayat_create">
</s:form>
<s:form name="updateform" action="gramPanchayat_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>