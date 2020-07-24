<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
</head>
<body>
<script type="text/javascript">
var editedId;
var cancelId;
$(document).ready(function(){	

	jQuery("#detail").jqGrid(
			{
			   	url:'menuCreationToolGrid_data.action',
			   	pager: '#pagerForDetail',
			   	datatype: "json",	
			    styleUI : 'Bootstrap',
			   	colNames:[		
						/*   <s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if> */
						
						
						'<s:text name="dynamicMenu.name"/>',
						'<s:text name="dynamicMenu.entity"/>'
					],
			   	colModel:[		
			   	          
								/*  <s:if test='branchId==null'>
					   			{name:'branchId',index:'branchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: {
					   			value: '<s:property value="parentBranchFilterText"/>',
					   			dataEvents: [ 
			   			          {
			   			            type: "change",
			   			            fn: function () {
			   			            	console.log($(this).val());
			   			             	getSubBranchValues($(this).val())
			   			            }
			   			        }
			   			    ]
			   			
			   			}},	   				   		
			   		</s:if>
			   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if>   */ 		
	
			   		{name:'name',index:'name',width:125,sortable:false,editable:true},
			   		{name:'entity',index:'entity',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;1:<s:text name="entityType1"/>;2:<s:text name="entityType2"/>;3:<s:text name="entityType3"/>;4:<s:text name="entityType4"/>;5:<s:text name="entityType5"/>;6:<s:text name="entityType6"/>' }}
			   				   		   
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
	
			
			 jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
			jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});
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


<sec:authorize ifAllGranted="admin.menuCreationToolGrid.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="menuCreationToolGrid_create">
</s:form>
<s:form name="updateform" action="menuCreationToolGrid_detail">
	<s:hidden name="id" />
	<s:hidden name="currentPage" />
</s:form>
</body>
