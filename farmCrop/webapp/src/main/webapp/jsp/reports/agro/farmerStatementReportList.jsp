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
	var farmerId='';
	var villageName='';
	var sDate='';
	var eDate='';
	var seasonCode='';
	var columnNames='' ;

	
	var branchId='<s:property value="branchId"/>';
	jQuery(document).ready(function(){
		
		onLoadValues();
		//loadCustomPopup();
		//loadFields();
		
		loadGrid();
		
		
			$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
	});
	var data = new Array();
		
	function loadGrid(){
	
		jQuery("#detail").jqGrid(
				{
					url:'farmerStatementReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',				   	
					postData:{				 
						  "startDate" : function(){
							  return  document.getElementById("hiddenStartDate").value;
						  },
						  "endDate" : function(){
							  return document.getElementById("hiddenEndDate").value;
						  },
						  "farmerId" : function(){
							  return document.getElementById("farmerId").value;
						  },
						  "groupId" : function(){
							  return document.getElementById("groupId").value;
						  },
						  "village" : function(){
							  return document.getElementById("villageName").value;
						  },
						  
						   
						},	
				   	colNames:[
				   	  		   	  
				   		//'<s:property value="%{getLocaleProperty('loanAccNo')}" />' ,				  				    					
				    	'<s:property value="%{getLocaleProperty('farmerGroupName')}" />',
				    	'<s:property value="%{getLocaleProperty('farmerGroupCode')}" />',
						'<s:property value="%{getLocaleProperty('villageName')}" />',				  		  	
			  		  	'<s:property value="%{getLocaleProperty('totalPurchase')}" />'+" ("+'<s:property value="%{getCurrencyType().toUpperCase()}" />'+")",	
			  		  	'<s:property value="%{getLocaleProperty('totalRepayment')}" />' +" ("+'<s:property value="%{getCurrencyType().toUpperCase()}" />'+")",	
			  		  		
				  		
				  		
				  		
		 	      	 ],
				   	colModel:[
					
						//{name:'loanAccNo',index:'loanAccNo',width:350,sortable:false},						
						{name:'farmerName',index:'farmerName',width:350,sortable:false},
						{name:'farmerCode',index:'farmerCode',width:350,sortable:false},
						{name:'v.name',index:'v.name',width:350,sortable:false},					
						{name:'totalPurchase',index:'totalPurchase',width:300,sortable:false,align:'right'},
				   		{name:'totalRepayment',index:'totalRepayment',width:300,sortable:false,align:'right'},
				   		
				   		
				   		
					],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   autowidth:true,
				   shrinkToFit:true,
				   viewrecords: true,  	
				   subGrid: true,
				   subGridOptions: {
					   "plusicon" : "glyphicon-plus-sign",
					   "minusicon" : "glyphicon-minus-sign",
					   "openicon" : "glyphicon-hand-right",
				   },
				   subGridRowExpanded: function(subgrid_id, row_id){
					
					   var subgrid_table_id, pager_id; 
					   subgrid_table_id = subgrid_id+"_t"; 
					   pager_id = "p_"+subgrid_table_id; 
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table> <div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'farmerStatementReport_subList.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
						   	    '<s:property value="%{getLocaleProperty('txnTime')}" />',
						   	  	'<s:property value="%{getLocaleProperty('receiptNo')}" />',	  	
						   	 	'<s:property value="%{getLocaleProperty('txnDesc')}" />',
						  		'<s:property value="%{getLocaleProperty('txnAmount')}" />'+" ("+'<s:property value="%{getCurrencyType().toUpperCase()}" />'+")",	  	
						  		  	
						  		
						  		    
				 	      	 ],
						   	colModel:[	
									{name:'txnTime',index:'txnTime',width:100,sortable:false},
									{name:'receiptNo',index:'receiptNo',width:100,sortable:false},
		   							{name:'txnDesc',index:'txnDesc',width:150,sortable:false},
		   							{name:'txnAmount',index:'txnAmount',width:100,sortable:false,align:'right'},		   							
		   							
						   	],
						   	scrollOffset: 10, 
						    sortname:'id',
						    height: '100%', 
						    rowNum:10,
						    sortorder: "desc",
						    autowidth: true,
							shrinkToFit:false,
						    viewrecords: true				
					   });
					 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
					   jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},				
				
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
			var json = JSON.stringify(data);
			
			var totalNetwt=$("#totalNetwt").text();
			var txnAmount=$("#txnAmount").text();
			var totalPayAmt=$("#totalPayAmt").text();
			headerData='<s:text name="totalNetwt"/>(Kgs)'+"###"+totalNetwt+"###"+'<s:text name="txnAmount"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+txnAmount+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});

			jQuery("#detail").jqGrid('excelExport', {url: 'farmerStatementReport_populateXLS.action?',postData:dataa});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}	
	function onLoadValues(){

		 $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	 	sDate = document.getElementById("hiddenStartDate").value;
		
		eDate = document.getElementById("hiddenEndDate").value;
	     $("#daterange").data().daterangepicker.updateView();
	      $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		
			document.getElementById('farmerId').selectedIndex=0;
			document.getElementById('villageName').selectedIndex=0;
		
	
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
		jQuery("#detail").jqGrid('setGridParam',{url:'farmerStatementReport_detail.action',page:1,postData:dataa}).trigger('reloadGrid');				
		}	
function loadFields(){
 		
		var filterDatas=$("#filterListData").val();
		 var startDate=document.getElementById("hiddenStartDate").value;  
		  //alert("startDate:"+startDate);
		  var endDate=document.getElementById("hiddenEndDate").value;
		  //alert("endDate:"+endDate);
		  var branchId='<s:property value="branchId"/>';
		  
		var weightUnit = "<s:property value='%{getLocaleProperty(\"weightUnit\")}'/>";
		var drillDownMsg = "<s:property value='%{getLocaleProperty(\"drillDownMsg\")}'/>";
		var drilldownTooltip1 = "<s:property value='%{getLocaleProperty(\"drilldownTooltip1\")}'/>";
		var drilldownTooltip2 = "<s:property value='%{getLocaleProperty(\"drilldownTooltip2\")}'/>";
		var barChartLegend = "<s:property value='%{getLocaleProperty(\"procurementWeightChartLegend\")}'/>";
		var barChartLegendTxnAmt = "<s:property value='%{getLocaleProperty(\"procurementTxnAmtChartLegend\")}'/>";
		var barChartLegendPayAmt = "<s:property value='%{getLocaleProperty(\"procurementPayAmtChartLegend\")}'/>";
		
	  $.post("farmerStatementReport_populateHeaderFields",{filterList:filterDatas,startDate:startDate,endDate:endDate,branch:branchId},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalNetwt").html(json[0].totalNetwt);
	 		$("#txnAmount").html(json[0].txnAmount);
	 		$("#totalPayAmt").html(json[0].totalPayAmt);
	 	});  	
	 	
	 		 	
	}
	
</script>

			
	
		<s:form id="form">
		<s:hidden id="filterListData" name="filterListData"/>
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle hide">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control  input-sm" />
					</div>
				</div>
				
				<div class="filterEle">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="filter.agroTransaction.farmerName" id="farmerId" list="farmersList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="filterEle hide">
						<label for="txt"><s:text name="group" /></label>
						<div class="form-element">
							<s:select name="filter.agroTransaction.farmerNames" id="groupId" list="groupTypeList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
					<label for="txt"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="filter.village.name" id="villageName" list="villageList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
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
	
			
			<%-- GRID SECTION --%>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			
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
