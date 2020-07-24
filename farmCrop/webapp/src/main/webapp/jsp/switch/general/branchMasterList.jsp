<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript">

$(document).ready(function(){	

	jQuery("#detail").jqGrid(
			{
			   	url:'branchMaster_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",	
			   	styleUI : 'Bootstrap',
			   	colNames:[				   	       	  
						'<s:text name="branchMaster.branchId.prop"/>',
						'<s:text name="branchMaster.name.prop"/>',
						'<s:text name="branchMaster.contactPerson.prop"/>',
						'<s:text name="branchMaster.phoneNo.prop"/>',
						'<s:text name="branchMaster.address.prop"/>',
						'<s:text name="status"/>'
				   	 ],
			   	colModel:[						
								   		
			   		{name:'branchId',index:'branchId',width:125,sortable:true},
			   		{name:'name',index:'name',width:125,sortable:true},	
			   		{name:'contactPerson',index:'contactPerson',width:125,sortable:true},		
			   		{name:'phoneNo',index:'phoneNo',width:125,sortable:true},			
			   		{name:'address',index:'address',width:125,sortable:false},
			   		{name:'status',index:'status',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="status1"/>;0:<s:text name="status0"/>' }}
			   			   		   
			   	],
			   	height: 255, 
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
			jQuery("#detail").jqGrid("setLabel","branchId","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","name","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","contactPerson","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","phoneNo","",{"text-align":"center"});
			jQuery("#detail").jqGrid("setLabel","address","",{"text-align":"center"});
			
			
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


<sec:authorize ifAllGranted="profile.branchMaster.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="branchMaster_create">
</s:form>
<s:form name="updateform" action="branchMaster_detail">
	<s:hidden name="id" />
	<s:hidden name="currentPage" />
</s:form>
</body>
