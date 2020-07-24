<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
</head>
<body><script type="text/javascript">
$(document).ready(function(){
	jQuery("#detail").jqGrid(
	{
	   	url:'farmerTrainingSelection_data.action',
	   	pager: '#pagerForDetail',
	   	datatype: "json",	
	   	mtype: 'POST',
	   	styleUI : 'Bootstrap',
	   	colNames:[		
	   	       <s:if test='branchId==null'>
	   	    '<s:text name="app.branch"/>',
	 		  </s:if>
	   		    '<s:property value="%{getLocaleProperty('farmerTrainingSelection.code')}" />', 
	  		   	'<s:property value="%{getLocaleProperty('trainingTopic')}" />',
	  		  	/* <s:if test="currentTenantId!='wilmar'">
				'<s:text name="farmerTrainingSelection.type"/>',
				</s:if> */
				'<s:property value="%{getLocaleProperty('farmerTrainingSelection.status')}" />'
	      	     ],
	   	colModel:[	
	   	       <s:if test='branchId==null'>
	           {name:'branchId',index:'branchId',sortable: false,width :250,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
	          </s:if>
	   		{name:'code',index:'code',width:125,sortable:true},
	   		{name:'tt.name',index:'tt.name',width:125,sortable:true},
	   		/* <s:if test="currentTenantId!='wilmar'">
	   		{name:'selectionType',index:'selectionType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: '<s:property value="trainingSelectionFilter"/>' }},
	   		</s:if> */
	   		{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="%{getLocaleProperty('statusValue1')}"/>;2:<s:text name="%{getLocaleProperty('statusValue2')}"/>' }}	   		

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
		  document.updateform.id.value  = id;
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
<sec:authorize ifAllGranted="service.farmerTrainingSelection.create">
	<input type="BUTTON" class="btn btn-sts" id="add" value="<s:property
					value="%{getLocaleProperty('create.button')}" />" onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">

<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="farmerTrainingSelection_create">
</s:form>
<s:form name="updateform" action="farmerTrainingSelection_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>
