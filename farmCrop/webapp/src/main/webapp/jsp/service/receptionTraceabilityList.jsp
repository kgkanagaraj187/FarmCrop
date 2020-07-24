<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

</head>
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	/*height: auto !important;*/
}
</style>
<body>
<script type="text/javascript">

	jQuery(document).ready(function(){
		jQuery("#detail").jqGrid(
				{
					url:'receptionTraceability_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',	
				    styleUI : 'Bootstrap',
				   	
				
				   	colNames:[
								<s:if test='branchId==null'>
								'<s:text name="%{getLocaleProperty('app.branch')}"/>',
								</s:if>
								<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
								</s:if>    
								'<s:property value="%{getLocaleProperty('date')}"/>',
								'<s:property value="%{getLocaleProperty('cooperative')}"/>',
								'<s:property value="%{getLocaleProperty('ginning')}"/>',
								'<s:property value="%{getLocaleProperty('ics')}" />' ,
								'<s:property value="%{getLocaleProperty('pp.name')}" />' ,
								'<s:property value="%{getLocaleProperty('variety')}"/>',
								'<s:property value="%{getLocaleProperty('grade')}"/>',
								'<s:property value="%{getLocaleProperty('truckId')}"/>',
								'<s:property value="%{getLocaleProperty('totalBags')}"/>',
								'<s:property value="%{getLocaleProperty('receiveBags')}"/>',
								'<s:property value="%{getLocaleProperty('totalGrossWeight')}"/> (<s:property value="%{getLocaleProperty('quintal')}"/>)' ,
								'<s:property value="%{getLocaleProperty('receiveWeight')}"/> (<s:property value="%{getLocaleProperty('quintal')}"/>)',
								'<s:property value="%{getLocaleProperty('moveToHeap')}"/>',
								
		 	      	 ],
		 	      	colModel:[
		 	      	       <s:if test='branchId==null'>
  					   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
  					   			value: '<s:property value="parentBranchFilterText"/>',
  					   			dataEvents: [{
  					   			            type: "change",
  					   			            fn: function () {
  					   			             	getSubBranchValues($(this).val())}
  					   			        }] }},	   				   		
  					   </s:if>
  					   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
  					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
 					   </s:if>  
                           {name: 'mtntDate', index: 'mtntDate',sortable: false},
                           {name: 'proCenter', index: 'proCenter', sortable: false},
                           {name: 'ginning', index: 'ginning', sortable: false},
                          	{name: 'ics', index: 'ics',sortable: false},
                          	{name: 'product', index: 'product', sortable: false},
                          	{name: 'variety', index: 'variety', sortable: false},
                          	{name: 'grade', index: 'grade', sortable: false},
                           {name: 'truckId', index: 'truckId',sortable: false},
                           {name: 'totalBags', index: 'totalBags',sortable: false},
                           {name: 'receiveBags', index: 'receiveBags',sortable: false, align: 'right'},
                           {name: 'totTransGrossWeight', index: 'totTransGrossWeight', sortable: false, align: 'right'},
                           {name: 'receiveWeight', index: 'receiveWeight',sortable: false, align: 'right'},
                           {name: 'moveToHeap',index: 'moveToHeap', sortable: false, align: 'right'},
		 					   	],
				   height: 255, 
				   width: $("#baseDiv").width(),
				   shrinkToFit:true,
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
				   subGrid: false,
				
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
				});
				$('#detail').jqGrid('setGridHeight',(reportWindowHeight));
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
		
			    jQuery("#detail").setGridWidth($("#baseDiv").width());
			    jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
				jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	

		function reloadGrid(){
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			//	alert(d1);
			var value1= d2[0];
			//alert(value1);
			var value2= d2[1];
			//alert(value2);
		document.getElementById("hiddenStartDate").value=value1;
		
		document.getElementById("hiddenEndDate").value=value2;
		
			var startDate=new Date(document.getElementById("hiddenStartDate").value);
		//	alert(startDate);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
		//	alert(endDate);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
		   jQuery("#detail").jqGrid('setGridParam',{url:"receptionTraceability_data.action?",page:1}).trigger('reloadGrid');
		   }
		}	
		function clear(){
			resetReportFilter();
			document.form.submit();
		}

		
		
	});
	function prepareMove(val) {
		
		$.ajax({
			type:"POST",
			async:false,
			url:"receptionTraceability_populateMove.action",
			data: {argData : val},
			success: function(result){
				  $("#detail").trigger("reloadGrid");
			}
		});
	}
</script>
<s:form name="form" action="receptionTraceability_list" >
</s:form>
<sec:authorize ifAllGranted="service.receptionTraceability.procurement.create">
	<input type="BUTTON" id="add" class="btn btn-sts" value="<s:text name="create.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>
<s:form name="createform" action="receptionTraceability_create">

</s:form>

<div class="appContentWrapper marginBottom">
 <div class="flex-layout">
<div style="width: 98%; position: relative;" id="baseDiv">
<table id="detail"></table>

<div id="pagerForDetail"></div>
<div id="pager_id"></div>
</div>
</div>
</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>
