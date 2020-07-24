<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript">

$(document).ready(function(){		
	
	
 	jQuery("#detail").jqGrid(
	{
	   	url:'creationTool_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',
	   	datatype: "json",	
	   	styleUI : 'Bootstrap',
	   	colNames:[
					
	  		   	 
	  		      '<s:text name="Section"/>',
	  		      '<s:text name="Component Type"/>',
	  			  '<s:text name="Component Name" />',
	  			 /*  '<s:text name="Max Len"/>', */
	  			  /* '<s:text name="Default value"/>', */
	  			  '<s:text name="Is Required" />', 
	  			/* '<s:text name="Data Format"/>', */
	  			/* '<s:text name="Validation"/>', */
	  			'<s:text name="Catalogue Type"/>',
	  			/* '<s:text name="Reference Id"/>', */
	  			'<s:text name="is Mobile Avail"/>',
	  			'<s:text name="is Report Avail"/>'
	      	 ],
	   	colModel:[						


			{name:'dynamicSectionConfig.sectionCode',index:'dynamicSectionConfig.sectionCode',width:125,sortable:true},
	   		{name:'componentType',index:'componentType',width:125,sortable:true,search:false},
	   		{name:'componentName',index:'componentName',width:125,sortable:true},
	   		/* {name:'componentMaxLength',index:'componentMaxLength',width:125,sortable:true,search:false},
	   		{name:'defaultValue',index:'defaultValue',width:125,sortable:true,search:false}, */
	   		{name:'isRequired',index:'isRequired',width:125,sortable:true,search:false},
	   		/* {name:'dataFormat',index:'dataFormat',width:125,sortable:true,search:false},
	   		{name:'validation',index:'validation',width:125,sortable:true,search:false}, */
	   		{name:'catalogueType',index:'catalogueType',width:125,sortable:true,search:false},
	   		/* {name:'referenceId',index:'referenceId',width:125,sortable:true,search:false}, */
	   		{name:'isMobileAvail',index:'isMobileAvail',width:125,sortable:true,search:false},
	   		{name:'isReportAvail',index:'isReportAvail',width:125,sortable:true,search:false}
	   		
	   	//	{name:'bodStatus',index:'bodStatus',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: '-1:<s:text name="filter.allStatus"/>;1:<s:text name="bodStatus1"/>;0:<s:text name="bodStatus0"/>' }}
	   		
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
	
	jQuery("#detail").setGridWidth($("#baseDiv").width());
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
	<%-- <sec:authorize ifAllGranted="profile.agent.create">
		
	</sec:authorize> --%>
	<div class="appContentWrapper ">
	<input type="BUTTON" id="add" class="btn btn-sts"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
	</div>	
	<s:form name="createform" action="creationTool_list">
	</s:form>
	<s:form name="updateform" action="creationTool_detail">
		 <s:hidden name="id" /> 
		 <s:hidden  name="currentPage" /> 
	</s:form>
</body>
</html>