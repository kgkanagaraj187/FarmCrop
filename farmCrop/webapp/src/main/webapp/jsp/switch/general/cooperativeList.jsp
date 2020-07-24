<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		var type='<s:property value="type"/>';
		
		$(".type").val(" ");
		$(".type").val(type);
		var detailAction = "cooperative_data.action?type=<%=request.getParameter("type")%>";
		
		jQuery("#detail").jqGrid(
		{//url:'sowingReport_subGridDetail.action?id='+row_id,
		   	url:'cooperative_data.action?type='+$(".type").val(),
		   	pager: '#pagerForDetail',	  
		   	datatype: "json",	
		   	mtype: 'POST',
		    styleUI : 'Bootstrap',
		    postData:{
  			  "postdata" :  function(){	
  	   			return  decodeURI(postdata);
  			  } 
  	   	},
		   	colNames:[ 
		   	             <s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
						</s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						'<s:text name="app.subBranch"/>',
						</s:if>
		  		   	  '<s:text name="coOperative.code"/>',
		  		      '<s:text name="coOperative.name"/>',
		  		    <s:if test="currentTenantId=='chetna' || currentTenantId=='livelihood'">
		  		      '<s:text name="coOperative.typez"/>',
		  		      </s:if>
		  		   	  '<s:text name="coOperative.location"/>',
		  		   	<s:if test="currentTenantId!='livelihood'">
		  		   	'<s:property value="%{getLocaleProperty('coOperative.warehouseInCharge')}"/>',
		  		  '<s:property value="%{getLocaleProperty('coOperative.capacityInTonnes')}"/>',
		  		  </s:if>
		      	 	 ],
		   	colModel:[	
			   	          
		   	       <s:if test='branchId==null'>
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
	  		</s:if>   	
		   		{name:'code',index:'code',width:125,sortable:true},
		   		{name:'name',index:'name',width:125,sortable:true},
		   		<s:if test="currentTenantId=='chetna'">
		   		{name:'typez',index:'typez',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;0:<s:property value="%{getLocaleProperty('WarehouseType0')}"/>;2:<s:text name="WarehouseType2"/>;3:<s:text name="WarehouseType3"/>;4:<s:text name="WarehouseType4"/>' }},
		   		</s:if>
		   		<s:if test="currentTenantId=='livelihood'">
		   		{name:'typez',index:'typez',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="filter.allStatus"/>;2:<s:property value="%{getLocaleProperty('WarehouseType2')}"/>;3:<s:property value="%{getLocaleProperty('WarehouseType3')}"/>' }},
		   		</s:if>
		   		{name:'location',index:'location',width:125,sortable:true},
		   		<s:if test="currentTenantId!='livelihood'">
		   		{name:'warehouseInCharge',index:'warehouseInCharge',width:125,sortable:true},
		   		{name:'capacityInTonnes',index:'capacityInTonnes',width:125,sortable:true, align:'right'}
		   		</s:if>
		   		],
		   	height: 301, 
		     width: $("#baseDiv").width(),  // assign parent div width
		 /*   width:1301, */
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',			  
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		    onSelectRow: function(id){ 
			  document.updateform.id.value  =id;
			  postDataSubmit();
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
		retainFields();
		$('#detail').jqGrid('setGridHeight',(windowHeight));
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
	    postdata =  '';
	}); 
</script>
<s:if test="type=='cooperative'">

	<sec:authorize ifAllGranted="profile.cooperative.create">
		<input type="BUTTON" id="add" class="btn btn-sts"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
	</sec:authorize>

</s:if>
<s:else>
	<sec:authorize ifAllGranted="profile.fieldCollectionCentre.create">
		<input type="BUTTON" id="add" class="btn btn-sts"
			value="<s:text name="create.button"/>"
			onclick="document.createform.submit()" />
	</sec:authorize>

</s:else>



<div class="appContentWrapper marginBottom">
	<div style="width: 99%;" id="baseDiv">
		<s:hidden name="type" class="type" />
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>
</div>

<s:form name="createform" action="cooperative_create">
	<s:hidden name="type" class="type" />
</s:form>
<s:form name="updateform" action="cooperative_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
	<s:hidden name="type" class="type" />
	<s:hidden name="postdata" id="postdata" />
</s:form>