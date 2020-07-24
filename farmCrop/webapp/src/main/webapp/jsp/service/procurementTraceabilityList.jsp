<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		jQuery("#detail").jqGrid(
				{
				   	url:'procurementTraceability_data.action',
				   	pager: '#pagerForDetail',	 
				    styleUI : 'Bootstrap',
				   	datatype: "json",	
				   	mtype: 'POST',
				   	colNames:[
				   	       <s:if test='branchId==null'>
			  			   '<s:text name="app.branch"/>',
				 		    </s:if>
			  				 <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',    
						</s:if>
				  		   	  '<s:text name="harvestDate"/>',
				  		      '<s:text name="cSeasonCode"/>',
				  		   	  '<s:text name="village"/>',
				  		   	  '<s:text name="farmerId"/>',
				  		   	  '<s:text name="%{getLocaleProperty('totQty')}"/>'
				  		   	  
				  		  
		 	      	 ],
		 	      	colModel:[
		 	      	       <s:if test='branchId==null'>
			           {name:'branchId',index:'branchId',sortable: false,width :250,search:true,frozen:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
			          </s:if>
			           <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
			   			{name:'subBranchId',index:'subBranchId',width:125,sortable: false,width :125,search:true,frozen:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
			   		</s:if>
								{name:'harvestDate',index:'harvestDate',width:120,sortable:false,search:false,frozen:true},
								{name:'seasonCode',index:'seasonCode',width:150,sortable:false,frozen:true},
								{name:'village',index:'village',width:250,sortable:false,frozen:true},
								{name:'farmerId',index:'farmerId',width:250,sortable:false,frozen:true},
								{name:'totalQuantity',index:'totalQuantity',width:250,sortable:false,search:false}						   	
						   	],
				   	height: 301, 
				    width: $("#baseDiv").width(), // assign parent div width
				    scrollOffset: 0,
				   	rowNum:10,
				   	rowList : [10,25,50],
				    sortname:'id',	
				    autowidth:true,
					shrinkToFit:true,
				    sortorder: "desc",
				    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				   /*  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
				      document.updateform.submit();      
					}, */		
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
<sec:authorize ifAllGranted="service.procurementTraceability.create">
	<input type="BUTTON" class="btn btn-sts" id="add"
		value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<div class="appContentWrapper marginBottom">
	<div style="width: 100%;" id="baseDiv">
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>
</div>
<s:form name="createform" action="procurementTraceability_create">
</s:form>
<%-- <s:form name="updateform" action="procurementTraceability_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form> --%>