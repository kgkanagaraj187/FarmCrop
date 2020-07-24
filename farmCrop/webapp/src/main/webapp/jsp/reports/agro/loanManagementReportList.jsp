<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="assets/common/openlayers/OpenLayers.js"></script>
</head>

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
<script src="plugins/highcharts/charts.js"></script>
<script src="plugins/highcharts/data.js"></script>
<script src="plugins/highcharts/drilldown.js"></script>
<script src="plugins/highcharts/charts-3d.js"></script>
<!-- <script src="js/procurementReportCharts.js"></script> -->
<style>
	.reportWrap .filterEle {
		float: left;
		cursor: pointer;
		margin-right: 1%;
		margin-left: 1%;
		margin-bottom: 5px;
		box-sizing: border-box;
	}
	.reportWrap .filterEle {
		width: 14%;
		height: auto;
		box-sizing: border-box;
	}
	
	.reportWrap .filterEle label{
		font-weight:bold;
		color:#000;
	}
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
	  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.updateView();
	      $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
	loadGrid();
});

function loadGrid(){
	jQuery("#detail").jqGrid({
		   	url:'loanManagementReport_data.action',
		   	pager: '#pagerForDetail',
		   	datatype: "json",
		   	mtype: 'POST',
			styleUI : 'Bootstrap',
			datatype: "json",
			postData:{	
				"startDate" : function(){
					  return  document.getElementById("hiddenStartDate").value;
				  },
				  "endDate" : function(){
					  return document.getElementById("hiddenEndDate").value;
				  },
			},
			colNames:[
			          '<s:property value="%{getLocaleProperty('Date')}" />' ,	
			          '<s:property value="%{getLocaleProperty('totalLoanAmount')}" />' ,	
			          '<s:property value="%{getLocaleProperty('loanRepaymentAmount')}" />' ,	
			         /*  '<s:property value="%{getLocaleProperty('loanpendingAmount')}" />' ,	 */
			
			],
			colModel:[
{name:'loanDate',index:'loanDate',width:250,sortable:false},
{name:'loanAmount',index:'loanAmount',width:350,sortable:false,align:'right'},
{name:'loanRepayment',index:'loanRepayment',width:250,sortable:false,align:'right'},
/* {name:'loanpending',index:'loanpending',width:250,sortable:false}, */
			],
			  height: 380, 
			   width: $("#baseDiv").width(),
			   //autowidth:true,
			   shrinkToFit:true,			   
			   scrollOffset: 19,     
			   sortname:'id',	
			   sortorder: "asc",
			   rowNum:10,
			   rowList : [10,25,50],
			   viewrecords: true,  

			    onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	          }
	   });
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
			
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
			//jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
	 }
	 
function exportXLS(){
	if(isDataAvailable("#detail")){
		
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			var label =$(this).parent().parent().find("label").html();
			if(!isEmpty(value)){
				data.push({name:name,value:value,label:label});
			}
		});
		var d1=	jQuery('#daterange').val();
		var d2= d1.split("-");
		if(d2.length>1){
			var value1= d2[0];
			var value2= d2[1];
			document.getElementById("hiddenStartDate").value=value1;
			document.getElementById("hiddenEndDate").value=value2;
		}
		var dataa={
				filterList:json,
				startDate:value1,
				endDate:value2
		};
		var json = JSON.stringify(dataa);
		
	
		

		jQuery("#detail").jqGrid('excelExport', {url: 'loanManagementReport_populateXLS.action?',postData:dataa});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}	
function search(){
	data = new Array();
	$("#form select").each(function(){
		var value = $(this).val();
		var name=this.name;
		var label =$(this).parent().parent().find("label").html();
		if(!isEmpty(value)){
			data.push({name:name,value:value,label:label});
		}
	});
	var json = JSON.stringify(data);
	$("#filterListData").val(json);
	
		var d1=	jQuery('#daterange').val();
		var d2= d1.split("-");
		if(d2.length>1){
			var value1= d2[0];
			var value2= d2[1];
			document.getElementById("hiddenStartDate").value=value1;
			document.getElementById("hiddenEndDate").value=value2;
		}
		var dataa={
				filterList:json,
				startDate:value1,
				endDate:value2
		};
	//loadFields();
	jQuery("#detail").jqGrid('setGridParam',{url:'loanManagementReport_data.action',page:1,postData:dataa}).trigger('reloadGrid');				
	}	

</script>
<s:form id="form">
		<s:hidden id="filterListData" name="filterListData"/>
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control  input-sm" />
					</div>
				</div>
				
				
				
					<div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true" onclick="search()">
						<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="reset" onclick="reset()">
						<b class="fa fa-close"></b>
						</button>
					</div>
				</div>
			</section>
			</div>
			<!-- <div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div> -->
		</s:form>
		
		
			<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<%--<div class="flexItem-2">
				 <div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span> <s:text name="totalNetwt"/>&nbsp;<span class="strong" id="totalNetwt"></span>&nbsp;kgs</span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="txnAmount"/>&nbsp;<span class="strong" id="txnAmount"></span>&nbsp; <s:property value="%{getCurrencyType().toUpperCase()}" /></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="txnAmt"/>&nbsp;<span class="strong" id="totalPayAmt"></span>&nbsp; <s:property value="%{getCurrencyType().toUpperCase()}" /></span>
					</div>
					
				</div> 
			</div>--%>
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
						<%-- <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li> --%>
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
		</div>

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
</body>