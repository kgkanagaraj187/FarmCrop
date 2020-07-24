<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
	<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		jQuery("#detail").jqGrid(
		{
   		   	url:'farmCrops_data.action',
   		   	pager: '#pagerForDetail',
   			mtype: 'POST',
   		   	datatype: "json",	
   		   	colNames:[				   	       	  
   		  		      '<s:text name="farmfarmcrops.code"/>',
   		  		      '<s:text name="farmfarmcrops.name"/>',
   		  		      '<s:text name="farmcrops.farmName"/>',
   		  		      '<s:text name="farmfarmcrops.area"/>',
   		  		      <s:if test="cropInfoEnabled=='1'">
   		  		  	  '<s:text name="farmfarmcrops.productionperyear"/>';
   		  		  	  </s:if>
   		  		  	  <%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
   		  		      '<s:text name="Action"/>'</sec:authorize>--%>
   			  		    
   		      	 ],
   		   	colModel:[						
   		   		{name:'fcm.code',index:'fcm.code', width:125,sortable:true},
   		   		{name:'fcm.name',index:'fcm.name', width:125,sortable:true},
   		   		{name:'f.farmName',index:'f.farmName', width:125,sortable:true},
   		   		{name:'cropArea',index:'cropArea', width:125,sortable:true},
   		      	<s:if test="cropInfoEnabled=='1'">
   		   		{name:'productionPerYear',index:'productionPerYear', width:125,sortable:true};
   		   		</s:if>
   		   		<%--<sec:authorize ifAllGranted="profile.farmCrop.delete">,
   		   		{name:'action',index:'action',width:125,sortable:false,search:false}</sec:authorize>--%>
   		   	],
   		   	height: 301, 
   		    width: $("#baseDiv").width(),
   		    scrollOffset: 0,
   		   	rowNum:10,
   		   	rowList : [10,25,50],
   		    sortname:'id',			  
   		    sortorder: "desc",
   		    viewrecords: true,
   		    beforeSelectRow: 
		        function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >=5){return false; }
		            else{ return true; }
		        },
   		    onSelectRow: function(id){ 
   			  document.updateform.id.value=id;   			  
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

	function deleteFarmCrops(id){	
		if(confirm( '<s:text name="confirm.deleteFarmCrops"/> ')){
			document.farmCropsDeleteform.id.value  =id;
			//document.farmCropsDeleteform.tabIndex.value = tabIndex;
			document.farmCropsDeleteform.submit();
		}
	}
</script>
<sec:authorize ifAllGranted="profile.farmCrop.create">
	<input type="BUTTON" id="add" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>

<s:form name="createform" action="farmCrops_create">
</s:form>

<s:form name="updateform" action="farmCrops_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
</s:form>

<s:form name="farmCropsDeleteform" action="farmCrops_delete">	
	<s:hidden name="id"/>	
</s:form>