<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<STYLE type="text/css">
.ui-autocomplete {
	background: #D3DEB0;
	padding: 10px;
	border: 1px solid #939585;
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden
}
</STYLE>
<style>
.ui-jqgrid-btable td {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-btable td:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid-btable tr {
	border: none !important;
}

.ui-jqgrid-htable th {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-htable th:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid .ui-jqgrid-htable TH.ui-th-ltr {
	border-bottom: solid 1px #567304 !important;
}

.ui-jqgrid .ui-jqgrid-btable {
	border-right: none !important;
}

.ui-jqgrid .ui-jqgrid-htable th div {
	padding-right: 0px !important;
}
</style>
</head>
<body>
	<script type="text/javascript">
	jQuery(document).ready(function(){
		  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
		 $('.applyBtn').click();
		
		//document.getElementById('fieldl').selectedIndex=0;
		 
		document.getElementById('farmerId').selectedIndex=0;
		jQuery("#detail").jqGrid(
				{
				   	url:'farmerInspectionUtzReport_data.action?',
				   	pager: '#pagerForDetail',				   
				   	datatype: "json",	
				    styleUI : 'Bootstrap',
					postData:{				 
						"startDate" : function(){
		                     return document.getElementById("hiddenStartDate").value;           
		                    },		
		                 "endDate" : function(){
				               return document.getElementById("hiddenEndDate").value;
			                 },
						 "filter.farmerId" : function(){			  
								return document.getElementById("farmerId").value;
						  },
					  	<s:if test='branchId==null'>
						  "branchIdParma" : function(){
							  return document.getElementById("branchIdParma").value;
						  }
						  </s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							  "subBranchIdParma" : function(){
								  return document.getElementById("subBranchIdParam").value;
							  },
							  </s:if>
						},	
					colNames:[	
					          
								<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
								</s:if>
								'<s:text name="groupId"/>',
								'<s:text name="groupName"/>',
								/* <s:if test="farmerCodeEnabled==1">
								'<s:text name="farmerId"/>',
								</s:if> */
								'<s:text name="farmerName"/>',
								'<s:text name="answeredDate"/>'			  		   	  
			 	      		 ],
			 	     
				   	colModel:[
				   	          
					   	       <s:if test='branchId==null'>
					           {name:'branchId',index:'branchId',width:125,sortable: false,width :200,search:true,stype: 'select',searchoptions: { value: '<s:property value="branchFilterText"/>' }},	   				   		
					          </s:if>
								{name:'groupId',index:'groupId',width:200,sortable:false},	
								{name:'groupName',index:'groupName',width:200,sortable:false},
								/* <s:if test="farmerCodeEnabled==1">
								{name:'farmerId',index:'farmerId',width:200,sortable:false},
								</s:if> */
								{name:'farmerName',index:'farmerName',width:200,sortable:false},
								{name:'answeredDate',index:'answeredDate',width:200,sortable:false}
				   			 ],
				   	height: 255,
			 		width: $("#baseDiv").width(),
			 		scrollOffset: 0,
			 		rowNum:10,
			 		rowList : [10,25,50],
			 		viewrecords: true,
			 		sortname:'answeredDate',
			 		sortorder: "desc",
				   	onSelectRow: function(id){ 
						  document.updateform.id.value  =id;
				          document.updateform.submit();      
						}
				   });

			jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
			jQuery("#detail").jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			  groupHeaders:[
				{startColumnName: 'groupId', numberOfColumns: 2, titleText: '<s:text name="farmerCropProdAnswers.group"/>'},
				{startColumnName: 'farmerId', numberOfColumns: 2, titleText: '<s:text name="farmerCropProdAnswers.farmer"/>'}
				 
			  ]
			});
				
			jQuery("#generate").click(function() {
				reloadGrid();	
			});
			
			jQuery("#clear").click(function() {
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
			jQuery("#detail").jqGrid('setGridParam',{url:"farmerInspectionUtzReport_data.action?",page:1})
				.trigger('reloadGrid');	
				
			}
			}


		function clear(){
			resetReportFilter();
			document.form.submit();
		}

		function getQuery(){
			var queryString="startDate="+document.getElementById("startDate").value+
			"&endDate="+document.getElementById("endDate").value;
			return queryString;
		}
	});
	function exportXLS() {
	    if (isDataAvailable("#detail")) {
	        jQuery("#detail").setGridParam({postData: {rows: 0}});
	        jQuery("#detail").jqGrid('excelExport', {url: 'farmerInspectionUtzReport_populateXLS.action?'});
	}     else {
	        alert('<s:text name="export.nodata"/>');
	    }
	}
	</script>

	<s:form name="form" action="farmerInspectionUtzReport_list">

		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
				<div class="reportFilterItem">
					<label for=""><s:text name="answeredDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm " />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for=""><s:text name="farmer" /></label>
					<div class="form-element">
						<s:select name="filter.farmerId" id="farmerId" list="farmersList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"/>
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for=""><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParma" id="branchIdParma" list="branchesMap"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
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


<div class="appContentWrapper marginBottom">
			<div class="flex-layout reportData">
				<!-- <div class="flexItem-2">
					<div class="summaryBlocksWrapper flex-container">
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="farmerCount"></span> Farmers&nbsp;<i
								class="fa fa-user"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="tArea"></span> Total Area&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						
						<div class="report-summaryBlockItem">
							<span><span class="strong" id="totalCoc"></span> Total Cost&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
					</div>
				</div> -->

			</div>

<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
					
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
			<div class="jqGridwrapper baseDiv">
				<table id="grid"></table>
				<div id="pager"></div>
			</div>

			<div style="width: 100%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>

		</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>

	<s:form name="updateform" action="farmerInspectionUtzReport_detail">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>
</body>
