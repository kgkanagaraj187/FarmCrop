<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
	<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		jQuery("#detail").jqGrid(
		{
		   	url:'cropSaleEntryReport_data.action',
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
	  			  '<s:text name="dateOfSale"/>',   
			 	    '<s:text name="cSeasonCode"/>',
	  				'<s:property value="%{getLocaleProperty('village.name')}" />',
                    '<s:property value="%{getLocaleProperty('farmerName')}" />',
                    '<s:text name="farmName"/>',
                     '<s:text name="receipt"/>',
                     '<s:text name="%{getLocaleProperty('totalSaleKg')}"/>',
                     '<s:text name="%{getLocaleProperty('totalSale')}"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'
                     ],
 	      	colModel:[
 	      	       <s:if test='branchId==null'>
	           {name:'branchId',index:'branchId',width:250,sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
	          </s:if>
	           <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
	   			{name:'subBranchId',index:'subBranchId',width:250,sortable: false,width :125,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
	   		</s:if>
						{name:'dateOfSale',index:'dateOfSale',width:250,sortable:false,search:false},
 						{name:'currentSeasonCode',index:'currentSeasonCode',width:250,sortable:false},
						{name:'village',index:'village',width:250,sortable:false},
						{name:'farmerId',index:'farmerId',width:250,sortable:false},
						{name:'farmCode',index:'farmCode',width:250,sortable:false},
						{name:'receiptNo',index:'receiptNo',width:250,align:'right',sortable:false},		
						{name:'totalSaleKg',index:'totalSaleKg',width:250,sortable:false,align:'right',search:false},
				   		{name:'totalSale',index:'totalSale',width:250,sortable:false,align:'right',search:false}
				   	
				   	],
		   	height: 301, 
		    width: $("#baseDiv").width(), // assign parent div width
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',		
		    shrinkToFit:true,
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
		    onSelectRow: function(id){ 
			  document.detailform.id.value  =id;
		      document.detailform.submit();      
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
 
 <sec:authorize ifAllGranted="service.report.cropSaleEntryReport.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>" onclick="document.createform.submit()" />
</sec:authorize>   

<div class="appContentWrapper marginBottom">
<div style="width: 99%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="detailform" action="cropSaleEntryReport_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>

<s:form name="createform" action="cropSaleEntryReport_create">
</s:form>
 