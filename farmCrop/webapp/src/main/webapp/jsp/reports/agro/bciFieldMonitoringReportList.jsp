<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
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
	var temp=0;
$(document).ready(function(){
	jQuery(".well").hide();
	onFilterData();
	$('.applyBtn').click();
	document.getElementById('farmName').selectedIndex=0;
	document.getElementById('farmerName').selectedIndex=0;
	//document.getElementById('fatherName').selectedIndex=0;
	document.getElementById('villageName').selectedIndex=0;
	document.getElementById('seasonId').selectedIndex=0;
	loadGrid();
	
});	
 
function loadGrid(){
	jQuery("#detail").jqGrid(
			{
			   	url:'bciFieldMonitoringReport_data.action',
			   	pager: '#pagerForDetail',
				mtype: 'POST',
				  styleUI : 'Bootstrap',
				postData:{	
					"farmName" : function(){
					    return document.getElementById("farmName").value;
				       },    
					"farmerName" : function(){
						    return document.getElementById("farmerName").value;
					       },
				      /* "fatherName" : function(){
						   return document.getElementById("fatherName").value;
					       },*/
				        "villageName" : function(){
				        		return document.getElementById("villageName").value;
				            }, 
				         "seasonCode" : function(){
						    return document.getElementById("seasonId").value;
					       }
    	        },	
			   	datatype: "json",	
			   	colNames:[	
						  '<s:text name="BCIfarmName"/>',
			  		   	  '<s:text name="farmerCode"/>',
			  		  	  '<s:text name="farmerName"/>',
			  		     // '<s:property value="%{getLocaleProperty('fatherName')}"/>',
			  		      '<s:text name="village"/>',
			  		  	  '<s:text name="totBCArea"/>',
			  		  	  '<s:text name="basicInfo"/>',
			  		  	  '<s:text name="landPreparation"/>',
			  			  '<s:text name="sowingDate"/>',
			  			  '<s:text name="seedDetail"/>',
			  			  '<s:text name="pumpTime"/>',
			  			  '<s:text name="weedFirst"/>',
			  			  '<s:text name="weedSecond"/>',
			  			  '<s:text name="weedThird"/>',
			  			  '<s:text name="fymFirst"/>',
			  			  '<s:text name="fymSecond"/>',
			  			  '<s:text name="fymThird"/>',
			  			  '<s:text name="moreFym"/>',
			  			  '<s:text name="fertiFirst"/>',
			  			  '<s:text name="fertiSecond"/>',
			  			  '<s:text name="fertiThird"/>',
			  			  '<s:text name="moreFerti"/>',
			  			  '<s:text name="pestFirst"/>',
			  			  '<s:text name="pestSecond"/>',
			  			  '<s:text name="pestThird"/>',
			  			  '<s:text name="morePest"/>',
			  			  '<s:text name="pickFirst"/>',
			  			  '<s:text name="pickSecond"/>',
			  			  '<s:text name="pickThird"/>'
			  			  
			  		     
			      	 ],
			   	colModel:[						
								{name:'farmName',index:'farmName',width:150,sortable:false},
								{name:'farmerCode',index:'farmerCode',width:150,sortable:false},
								{name:'farmerName',index:'farmerName',width:150,sortable:false},
							//	{name:'father',index:'father',width:150,sortable:false},
								{name:'village',index:'village',width:150,sortable:false},
								{name:'totBCArea',index:'totBCArea',width:150,sortable:false},
								{name:'basicInfo',index:'basicInfo',width:150,sortable:false},
								{name:'landPreparation',index:'landPreparation',width:150,sortable:false},
								{name:'sowingDate',index:'sowingDate',width:150,sortable:false},
								{name:'seedDetail',index:'seedDetail',width:150,sortable:false},
								{name:'pumpTime',index:'pumpTime',width:150,sortable:false},
								{name:'weedFirst',index:'weedFirst',width:150,sortable:false},
								{name:'weedSecond',index:'weedSecond',width:150,sortable:false},
								{name:'weedThird',index:'weedThird',width:150,sortable:false},
								{name:'fymFirst',index:'fymFirst',width:150,sortable:false},
								{name:'fymSecond',index:'fymSecond',width:150,sortable:false},
								{name:'fymThird',index:'fymThird',width:150,sortable:false},
								{name:'moreFym',index:'moreFym',width:150,sortable:false},
								{name:'fertiFirst',index:'fertiFirst',width:150,sortable:false},
								{name:'fertiSecond',index:'fertiSecond',width:150,sortable:false},
								{name:'fertiThird',index:'fertiThird',width:150,sortable:false},
								{name:'moreFerti',index:'moreFerti',width:150,sortable:false},
								{name:'pestFirst',index:'pestFirst',width:150,sortable:false},
								{name:'pestSecond',index:'pestSecond',width:150,sortable:false},
								{name:'pestThird',index:'pestThird',width:150,sortable:false},
								{name:'morePest',index:'morePest',width:150,sortable:false},
								{name:'pickFirst',index:'pickFirst',width:150,sortable:false},                       
								{name:'pickSecond',index:'pickSecond',width:150,sortable:false},
								{name:'pickThird',index:'pickThird',width:150,sortable:false}
								
						
						
						
			   	],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			
			    autowidth:true,
				shrinkToFit:false,	
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    beforeSelectRow: 
			        function(rowid, e) {
			            var iCol = jQuery.jgrid.getCellIndex(e.target);
			            if (iCol >=8){return false; }
			            else{ return true; }
			        },
			        
		    });
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('setGroupHeaders', {
		useColSpanStyle: true, 
  		groupHeaders:[
			{startColumnName: 'weedFirst', numberOfColumns: 3, titleText: '<s:text name="weed"/>'},
			{startColumnName: 'fymFirst', numberOfColumns: 4, titleText: '<s:text name="fym"/>'},
			{startColumnName: 'fertiFirst', numberOfColumns: 4, titleText: '<s:text name="fertilizer"/>'},
			{startColumnName: 'pestFirst', numberOfColumns: 4, titleText: '<s:text name="pesticide"/>'},
			{startColumnName: 'pickFirst', numberOfColumns: 3, titleText: '<s:text name="pick"/>'}
  ] });
			
			
			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail")[0].id) +
		        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		        var cmi = colModel[i], colName = cmi.name;

		        if (cmi.sortable !== false) {
		            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		            $(this).find('>div.ui-jqgrid-sortable.ui-jqgrid-htable').css({cursor: 'default'});
		        }
		    });
		    jQuery("#generate").click( function() {
				reloadGrid();	
			});
			
			jQuery("#clear").click( function() {
				clear();
			});	
			jQuery("#minus").click( function() {
				var flag="edit";
				var index=jQuery("#fieldl option:selected").index();
				if(!jQuery("."+index).hasClass("hide")){
					jQuery("."+index).addClass("hide");
					temp--;
				}
				
				jQuery("."+index+"> div select").val("");
				jQuery("#fieldl").val("0");
				
				jQuery("#filter-fields").remove(jQuery("."+index));
				document.getElementById('fieldl').selectedIndex=0;
				if(temp==0){
					jQuery(".well").hide();
				}
				$('.'+index).find('select').val('');
				reloadGrid();
				});

				jQuery("#plus").click( function() {
					if(jQuery("#fieldl option:selected").val() != ''){
					jQuery(".well").show();
					var index=jQuery("#fieldl option:selected").index();
					if(jQuery("."+index).hasClass("hide")){
						temp++;
						}
					jQuery("."+index).removeClass("hide");
					
				
					jQuery("#searchbtn").append(jQuery("."+index));
					}
					
				});
}
function reloadGrid(){
			
		jQuery("#detail").jqGrid('setGridParam',{url:"bciFieldMonitoringReport_data.action?",page:1}).trigger('reloadGrid');	
}
function clear(){
	$("#seasonId").val("");
	resetReportFilter();
	document.form.submit();
}

function exportXLS(){
	if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		jQuery("#detail").jqGrid('excelExport', {url: 'bciFieldMonitoringReport_populateXLS.action'});
	}
	else{
	     alert('<s:text name="export.nodata"/>');
	}

}
function onFilterData(){
	callAjaxMethod("bciFieldMonitoringReport_populateFarmList.action","farmName");
	callAjaxMethod("bciFieldMonitoringReport_populatFarmerList.action","farmerName");
	callAjaxMethod("bciFieldMonitoringReport_populateFatherNameList.action","fatherName");
	callAjaxMethod("bciFieldMonitoringReport_populateVillageList.action","villageName");
	callAjaxMethod("bciFieldMonitoringReport_populateSeasonList.action","seasonId");
}
function callAjaxMethod(url,name){
	var cat = $.ajax({
		url: url,
		async: true, 
		type: 'post',
		success: function(result) {
			insertOptions(name,JSON.parse(result));
		}        	

	});
	
}
</script>


	<s:form name="form" action="bciFieldMonitoringReport_list">
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
			
			<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="BCIfarmName" /></label>
					<div class="form-element">
						<s:select name="farmName" id="farmName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="farmerName" /></label>
					<div class="form-element">
						<s:select name="farmerName" id="farmerName" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"/>
					</div>
				</div>
				<div class="reportFilterItem hide">
					<label for="filter.farmer.id"><s:text name="fatherName" /></label>
					<div class="form-element">
					<s:select name="fatherName" id="fatherName"
					list="{}" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="villageName" id="villageName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="seasonCode" /></label>
					<div class="form-element">
						<s:select name="seasonCode" id="seasonId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
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
	</s:form>
	<div class="clear"></div>
	<s:form name="detailForm" action="bciFieldMonitoringReport_list">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>

</body>
