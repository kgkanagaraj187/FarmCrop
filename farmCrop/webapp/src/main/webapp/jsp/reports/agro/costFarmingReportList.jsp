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
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.updateView();
	    $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		
		 <s:if test='branchId==null'>
		document.getElementById('branchIdParma').selectedIndex=0;
		</s:if> 
		
		document.getElementById('researchId').selectedIndex=0;
		document.getElementById('farmerName').selectedIndex=0;
		document.getElementById('receiptNo').selectedIndex=0;
		document.getElementById('elitype').selectedIndex=0;
		document.getElementById('cowId').selectedIndex=0;
		$('#daterange').val()
		jQuery("#detail").jqGrid(
				{
					url:'costFarmingReport_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				    styleUI : 'Bootstrap',
				   	postData:{	
					"startDate" : function(){
                     return document.getElementById("hiddenStartDate").value;           
                    },		
                    "endDate" : function(){
		               return  document.getElementById("hiddenEndDate").value;
	                 },
	                "filter.cow.farm.farmer.firstName" : function(){
					    return document.getElementById("farmerName").value;
				     },
				   
					"filter.researchStationId" : function(){
					       return document.getElementById("researchId").value;
				     },
					 "filter.receiptNo" : function(){
						   return document.getElementById("receiptNo").value;
					 },
					 "filter.elitype" : function(){
						   return document.getElementById("elitype").value;
					 },
					 "filter.cow.id" : function(){
						   return document.getElementById("cowId").value;
					 },
			            <s:if test='branchId==null'>
		             "branchIdParma":function(){
			               return document.getElementById("branchIdParma").value;
		                 }
			             </s:if> 
			             
			           /*   <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						  "subBranchIdParma" : function(){
							  return document.getElementById("subBranchIdParam").value;
						  },
						  </s:if> */
				 },
				   	colNames:[
							<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
							</s:if>
	  						'<s:text name="collectionDate"/>',
							'<s:text name="cow.researchStationName"/>',
	  						'<s:text name="farmerId"/>',
	  						'<s:text name="farmName"/>',
  							'<s:text name="cowId"/>',
  							'<s:text name="receiptNo"/>',
  							'<s:text name="housingCost"/>',
  							'<s:text name="feedCost"/>',
  							'<s:text name="treatementCost"/>',
							'<s:text name="otherCost"/>',
							'<s:text name="totalExpence"/>',
							'<s:text name="incomeMilk"/>',
							'<s:text name="incomeOther"/>',
							'<s:text name="totalIncome"/>'	
                      	 ],
		 	      	colModel:[
		 	      	 <s:if test='branchId==null'>
					{name:'branchId',index:'branchId',width:170,sortable: false},	   				   		
					</s:if>
					{name:'collectionDate',index:'collectionDate',width:170,sortable:false},
					{name:'researchStationId',index:'researchStationId',width:200,sortable:false},
					{name:'farmerId',index:'farmerId',width:150,sortable:false,align:'center'},
					{name:'farmCode',index:'farmCode',width:150,sortable:false,align:'center'},
					{name:'c.cowId',index:'c.cowId',width:150,sortable:false,align:'center'},
					{name:'receiptNo',index:'receiptNo',width:150,sortable:false,align:'center'},
					{name:'housingCost',index:'housingCost',width:150,sortable:false,align:'center'},
					{name:'feedCost',index:'feedCost',width:150,sortable:false,align:'center'},
					{name:'treatementCost',index:'treatementCost',width:150,sortable:false,align:'center'},
					{name:'otherCost',index:'otherCost',width:150,sortable:false,align:'center'},
					{name:'totalExpence',index:'totalExpence',width:150,sortable:false,align:'center'},
					{name:'incomeMilk',index:'incomeMilk',width:150,sortable:false,align:'center'},
					{name:'incomeOther',index:'incomeOther',width:150,sortable:false,align:'center'},
					{name:'totalIncome',index:'totalIncome',width:150,sortable:false,align:'center'}
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
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			var value1= d2[0];
			var value2= d2[1];
		document.getElementById("hiddenStartDate").value=value1;
		document.getElementById("hiddenEndDate").value=value2;
		
			var startDate=new Date(document.getElementById("hiddenStartDate").value);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
		   jQuery("#detail").jqGrid('setGridParam',{url:"costFarmingReport_data.action?",page:1}).trigger('reloadGrid');
		   }
		}	
		function clear(){
			resetReportFilter();
			document.form.submit();
		}
	
	});
	
	
	
</script>

		<s:form name="form" action="costFarmingReport_list">
		
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
			
			<div class="reportFilterItem">
					<label for="daterange"><s:text name="startingDate" /></label>
					<div class="form-element">
						 <input
							id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" style="width: 150px" />
					</div>
			</div>
				
			<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="cow.researchStationName" /></label>
					<div class="form-element">
						 <s:select name="researchId" id="researchId" list="researchList" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
			</div>
			
			<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="farmerName" /></label>
					<div class="form-element">
						<s:select name="farmerName" id="farmerName" list="farmersList" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
			</div>
			
			<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="receiptInfor" /></label>
					<div class="form-element">
						<s:select name="receiptNo" id="receiptNo" listKey="key" listValue="value"
					list="receiptList" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
			</div>
			
			<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="cowId" /></label>
					<div class="form-element">
						<s:select name="cowId" id="cowId" listKey="key" listValue="value"
					list="cowList" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
			</div>
			
			<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="elitType" /></label>
					<div class="form-element">
						<s:select name="elitype" id="elitype"
					list="typeList" listKey="key" listValue="value" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" cssStyle="width:150px"/>
					</div>
			</div>
			
				<s:if test='branchId==null'>
				 <div class="reportFilterItem">
					<label for="branchIdParma"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma"
						list="branchesMap" headerKey="" cssStyle="width:150px"
						headerValue="%{getText('txt.select')}"
						cssClass="form-control input-sm select2" />
					</div>
				</div> 
				</s:if>
				
			<%-- <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
					<div class="reportFilterItem">
						<label for="branchIdParam"><s:text name="app.branch" /></label>
							<div class="form-element">
						<s:select name="branchIdParam" id="branchIdParam"
							list="parentBranches" headerKey="" headerValue="Select"
							cssClass="input-sm form-control select2"
							onchange="populateChildBranch(this.value)" />
							</div>
				</div>
					</s:if>
					<div class="reportFilterItem">
					<label for="branchIdParam"><s:text name="app.subBranch" /></label>
						<div class="form-element">
					<s:select name="subBranchIdParam" id="subBranchIdParam"
						list="subBranchesMap" headerKey="" headerValue="Select"
						cssClass="input-sm form-control select2"/>
						</div>
				</div>
				</s:if> --%>
				
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
	
	  <s:form name="detailForm" action="costFarmingReport_detail">
		<s:hidden name="id" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
