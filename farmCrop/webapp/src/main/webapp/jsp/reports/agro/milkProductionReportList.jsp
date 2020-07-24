<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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
	padding: 2px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: auto !important;
	padding: 2px;
}
</style>
</head>
<body>
	<script type="text/javascript">
	 var recordLimit='<s:property value="exportLimit"/>';
	 jQuery(document).ready(function(){
		 jQuery(".well").hide();
		 //document.getElementById('fieldl').selectedIndex=0;
			$('.applyBtn').click();
		
		<s:if test='branchId==null'>
		document.getElementById('branchIdParma').selectedIndex=0;
		</s:if>
		document.getElementById('researchId').selectedIndex=0;
		document.getElementById('farmerName').selectedIndex=0;
		document.getElementById('cowId').selectedIndex=0;
		$('#daterange').val()
		jQuery("#detail").jqGrid(
				{
					url:'milkProductionReport_data.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				    styleUI : 'Bootstrap',
				   	postData:{	
					 "filter.cow.farm.farmer.firstName" : function(){
					    return document.getElementById("farmerName").value;
				     },
				   
					"filter.researchStation.id" : function(){
					       return document.getElementById("researchId").value;
				     },
					 "filter.cow.id" : function(){
						   return document.getElementById("cowId").value;
					 },
			             <s:if test='branchId==null'>
		             "branchIdParma":function(){
			               return document.getElementById("branchIdParma").value;
		                 }
			             </s:if>
				 },
				   	colNames:[
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
							'<s:text name="cowId"/>',
							'<s:text name="cow.researchStationName"/>',
	  						'<s:text name="farmerId"/>',
	  						'<s:text name="litreMilkMrng"/>',
							'<s:text name="litreMilkEvn"/>',
							'<s:text name="totMilkPerDay"/>',
							'<s:text name="totMilkPeriod"/>'	
                      	 ],
		 	      	colModel:[
		 	      	 <s:if test='branchId==null'>
					{name:'branchId',index:'branchId',width:170,sortable: false},	   				   		
					</s:if>
					{name:'cowId',index:'cowId',width:200,sortable:false},
					{name:'researchStationName',index:'researchStationName',width:150,sortable:false,align:'center'},
					{name:'farmerId',index:'farmerId',width:150,sortable:false,align:'center'},
					{name:'litreMilkMrng',index:'litreMilkMrng',width:150,sortable:false,align:'center'},
					{name:'litreMilkEvn',index:'litreMilkEvn',width:150,sortable:false,align:'center'},
					{name:'totMilkPerDay',index:'totMilkPerDay',width:150,sortable:false,align:'center'},
					{name:'totMilkPeriod',index:'totMilkPeriod',width:150,sortable:false,align:'center'}
					],
									
				   onSelectRow : function(id) {
						document.detailForm.id.value = id;
						document.detailForm.submit();
					},
				   height: 301, 
				   width: $("#baseDiv").width(),
				   autowidth:true,
				   shrinkToFit:false,	
				   scrollOffset: 0,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true  	
				});


		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	

		function reloadGrid(){
			
		   jQuery("#detail").jqGrid('setGridParam',{url:"milkProductionReport_data.action?",page:1}).trigger('reloadGrid');
		}	
		function clear(){
			resetReportFilter();
			document.form.submit();
		}

		$('#detail').jqGrid('setGridHeight',(windowHeight));
		
	});
	
	
	
</script>

	
		<s:form name="form" action="milkProductionReport_list">
			
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
			
			
				<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="cowId" /></label>
					<div class="form-element">
						<s:select name="cowId" id="cowId" listKey="key" listValue="value"
					list="cowList" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="cow.researchStationName" /></label>
					<div class="form-element">
						<s:select name="researchId" id="researchId" list="researchStationList" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="farmerName" /></label>
					<div class="form-element">
						<s:select name="farmerName" id="farmerName" list="farmerList" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
				</div>
			
			<s:if test='branchId==null'>
				<div class="reportFilterItem">
					<label for="branchIdParma"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma"
						list="branchesMap" headerKey="" cssStyle="width:200px"
						headerValue="%{getText('txt.select')}"
						cssClass="form-control input-sm select2" />
					</div>
				</div>
			</s:if>	
				
			
			<div class="reportFilterItem" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
				
			</div>
		</div>
			
	
	</s:form>
	

	<div style="width: 98%; padding-top: 35px" ; position:
		relative;" id="baseDiv">


		<table id="detail"></table>

		<div id="pagerForDetail"></div>
		<div id="pager_id"></div>
	</div>
	
	  <s:form name="detailForm" action="milkProductionReport_detail">
		<s:hidden name="id" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
