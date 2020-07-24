<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
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
</head>

<body>
	<script type="text/javascript">
	  var procurementProductId = '';
var recordLimit='<s:property value="exportLimit"/>';
	jQuery(document).ready(function(){
		onFilterData();
		populateFarmerMethod();
		//document.getElementById('seasonCode').selectedIndex=0;
		document.getElementById('procurementProductId').selectedIndex=0;
		jQuery(".well").hide();
		$('.showProp').hide();
		jQuery("#detail").jqGrid(
				{
					url:'procurementTraceabilityStockReport_data.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',				   	
				   	postData:{				 
			             "filter.procurementTraceabilityStock.coOperative.id":function(){
				               return document.getElementById("coOperative").value;
			              },
			              "filter.procurementTraceabilityStock.procurementProduct.id":function(){
				               return document.getElementById("procurementProductId").value;
			              },
			              <s:if test="currentTenantId=='chetna'">
			              "filter.procurementTraceabilityStock.ics":function(){
				               return document.getElementById("ics").value;
			              }, 
			              </s:if>
			              "filter.farmer.farmerId":function(){
				               return document.getElementById("farmerId").value;
			              },
			              "filter.procurementTraceabilityStock.village.id":function(){
				               return document.getElementById("villageName").value;
			              },
			              <s:if test="currentTenantId!='livelihood'">
			              "filter.procurementTraceabilityStock.city.id":function(){
				               return document.getElementById("cityName").value;
			              },</s:if>
			              <s:if test="currentTenantId=='chetna'">
			              "filter.farmer.fpo":function(){
				               return document.getElementById("fpoName").value;
			              },
			              <s:if test="currentTenantId!='livelihood'">
			              "selectedSeason" : function(){
			 			    	return document.getElementById("selectedSeason").value;
			 			    },</s:if>
			 			    </s:if>
						  <s:if test='branchId==null'>
						    "branchIdParma" : function(){			  
	 	 					  return document.getElementById("branchIdParma").value;
	 	 	 			      } 
						  </s:if>
					},
				   	colNames:[
				   	           <s:if test='branchId==null'>
                                     '<s:text name="%{getLocaleProperty('app.branch')}"/>',
                               </s:if>
                               <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
                               '<s:text name="app.subBranch"/>',
                                </s:if>
                               <s:if test="currentTenantId=='chetna'">
                               '<s:property value="%{getLocaleProperty('season')}"/>	',
                               </s:if>
				  		 	'<s:property value="%{getLocaleProperty('cooperative')}"/>',
				  		 	<s:if test="currentTenantId=='chetna'">
				  			'<s:property value="%{getLocaleProperty('icsName')}"/>	',
				  			'<s:property value="%{getLocaleProperty('fpo')}"/>',
				  			</s:if>
				  			'<s:property value="%{getLocaleProperty('Block')}"/>',
				  			'<s:property value="%{getLocaleProperty('village')}"/>',
				  			'<s:property value="%{getLocaleProperty('Date')}"/>',
				  			'<s:property value="%{getLocaleProperty('farmer')}"/>',
				  			'<s:property value="%{getLocaleProperty('pp.name')}"/>',
				  			'<s:property value="%{getLocaleProperty('pp.variety')}"/>',
				  			'<s:property value="%{getLocaleProperty('pp.grade')}"/>',
				  			<s:if test="currentTenantId=='chetna'">
				  			'<s:property value="%{getLocaleProperty('noofBags')}"/>',
				  			</s:if>
				  			'<s:property value="%{getLocaleProperty('netWeight')}"/>'
				  			
				  		   
		 	      	 ],
				   	colModel:[
						<s:if test='branchId==null'>
				   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
				   			value: '<s:property value="parentBranchFilterText"/>',
				   			dataEvents: [{
				   			            type: "change",
				   			            fn: function () {
				   			            	console.log($(this).val());
				   			             	getSubBranchValues($(this).val())
				   			            }
				   			        }]
				   			
				   			}},	   				   		
				   		</s:if>
				   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
				   		</s:if>
				   		  <s:if test="currentTenantId=='chetna'">
				   			{name:'season',index:'season',sortable:false,frozen:true},
				   			</s:if>
							{name:'co.name',index:'co.name',sortable:false},
							  <s:if test="currentTenantId=='chetna'">
							{name:'ics',index:'ics',sortable:false},
							</s:if>
							{name:'farmer',index:'farmer',sortable:false},
							  <s:if test="currentTenantId=='chetna'">
							{name:'fpo',index:'fpo',sortable:false},
							</s:if>
							{name:'city',index:'city',sortable:false},
							{name:'village',index:'village',sortable:false},
							{name:'date',index:'date',sortable:false},
							{name:'pp.name',index:'pp.name',sortable:false},
							{name:'pp.name',index:'pp.variety',sortable:false},
							{name:'pp.name',index:'pp.grade',sortable:false},
							<s:if test="currentTenantId=='chetna'">
							{name:'pp.name',index:'noOfBags',sortable:false},
							</s:if>
							{name:'pp.name',index:'netWeight',sortable:false},
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   shrinkToFit:true,
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:15,
				   rowList : [15,60,100],
				   viewrecords: true,  	
				   subGrid: false,
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
			    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
		jQuery("#clear").click( function() {
			clear();
		});	
		function reloadGrid(){
			
			jQuery("#detail").jqGrid('setGridParam',{url:"procurementTraceabilityStockReport_data.action",page:1}).trigger('reloadGrid');
			populateFarmerMethod();
		}
		function clear(){
/* 			document.form.action="procurementTraceabilityStockReport_list.action";
			document.getElementById('coOperative').selectedIndex=0;
			document.getElementById('procurementProductId').selectedIndex=0;
			document.getElementById('branchIdParma').selectedIndex=null; */
			document.form.submit();
		}
	});
	function loadCentralWarehouse(){
		$.post("procurementTraceabilityStockReport_populateCentralWarehouse",{dt:new Date()},function(data){
			if(data == null || data == ""){
				alert('<s:text name="noRecordFoundForProcurementCentral"/>')
			}else{
			enableRestartAlert(data);
			}
		});
	}	
	function enableRestartAlert(data){	
		$('body').css('overflow','hidden');
		$('#pendingRestartAlertErrMsg').html('');
		$('#popupBackground').css('width','100%');
		$('#popupBackground').css('height',getWindowHeight());
		$('#popupBackground').css('top','0');
		$('#popupBackground').css('left','0');
		$('#popupBackground').show();
		$('#restartAlert').css({top:'50%',left:'50%',margin:'-'+($('#restartAlert').height() / 2)+'px 0 0 -'+($('#restartAlert').width() / 2)+'px'});
		$('#restartAlert').show();
		var newRow="<thead><tr><th colspan='3'><s:text name='centralWarehouse' /></th></tr><tr bgcolor='#E8FAE6'><th><s:text name='qualitty' /></th><th><s:text name='noOfBags' /></th><th><s:text name='netWeight' /></th></tr></thead><tbody>"+data+"</tbody>";
		$('#tableData').append(newRow); 
		jQuery("#tableData").css('height','170px');
		window.location.hash="#restartAlert";
	}
	function getWindowHeight(){
		var height = document.documentElement.scrollHeight;
		if(height<$(document).height())
			height = $(document).height();
		return height;
	}
	function exportXLS(){
		var count=jQuery("#detail").jqGrid('getGridParam', 'records');
		if(count>recordLimit){
			 alert('<s:text name="export.limited"/>');
		}
		else if(isDataAvailable("#detail")){
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'procurementTraceabilityStockReport_populateXLS.action'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}
	function onFilterData(){
		callAjaxMethod("procurementTraceabilityStockReport_populateCooperativeList.action","coOperative");
		callAjaxMethod("procurementTraceabilityStockReport_populateProductList.action","procurementProductId");
		callAjaxMethod("procurementTraceabilityStockReport_populateFarmerList.action","farmerId");
		//callAjaxMethod("procurementTraceabilityStockReport_populateICSList.action","ics");
		callAjaxMethod("procurementTraceabilityStockReport_populateVillageList.action","villageName");
		//callAjaxMethod("procurementTraceabilityStockReport_populateCityList.action","cityName");
		callAjaxMethod("procurementTraceabilityStockReport_populateFpoList.action","fpoName");
		//callAjaxMethod("procurementTraceabilityStockReport_populateSeasonList.action","selectedSeason");
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
	function populateFarmerMethod() {
		
		var branchIdParma='<s:property value="getBranchId()"/>';
		//var startDate=document.getElementById("hiddenStartDate").value;  
		//var endDate=document.getElementById("hiddenEndDate").value;
	    var tenant='<s:property value="getCurrentTenantId()"/>';
		var farmerName=document.getElementById("farmerId").value;
		var product=document.getElementById("procurementProductId").value;
		var warehouse=document.getElementById("coOperative").value;
		var village=document.getElementById("villageName").value;
		var city="";		
		var ics="";
		var season="";
		var fpo="";
		if(tenant!='livelihood'){
		season=document.getElementById("selectedSeason").value;
		ics=document.getElementById("ics").value;
		fpo=document.getElementById("fpoName").value;
		city=document.getElementById("cityName").value;
		}
		$.post("procurementTraceabilityStockReport_populateFarmerMethod",{village:village,city:city,fpo:fpo,ics:ics,farmerName:farmerName,product:product,warehouse:warehouse,branchIdParma:branchIdParma,selectedSeason:season},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalQty").html(json[0].totalQty);
	 		if(tenant!='livelihood'){
	 			$("#totalNoOfBags").html(json[0].totalNoOfBags);
	 		}
	 		
	 	});
	}
</script>
	<s:form name="form" action="procurementTraceabilityStockReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('cooperative')}" /></label>
						<div class="form-element">
							<s:select name="filter.coOperative" id="coOperative" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="product" /></label>
						<div class="form-element">
							<s:select name="filter.product" id="procurementProductId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
					<label for="txt"><s:text name="%{getLocaleProperty('villageName')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.villageName" id="villageName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<s:if test="currentTenantId!='livelihood'">
				<div class="filterEle">
					<label for="txt"><s:text name="%{getLocaleProperty('cityName')}" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.cityName" id="cityName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="cooperative" /></label>
					<div class="form-element">
						<s:select name="filter.farmer.fpoName" id="fpoName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div></s:if>
						<div class="filterEle">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="filter.farmer.farmerId" id="farmerId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
						<s:if test="currentTenantId!='livelihood'">
						<div class="filterEle">
						<label for="txt"><s:text name="icsName" /></label>
						<div class="form-element">
							<s:select name="filter.ics" id="ics" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					</s:if>
					
					
					<s:if test='branchId==null'>
						<div class="filterEle">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element">
								<s:select name="branchIdParma" id="branchIdParma"
									list="branchesMap" headerKey=""
									headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<s:select name="branchIdParam" id="branchIdParam"
									list="parentBranches" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2"
									onchange="populateChildBranch(this.value)" />
							</div>
						</s:if>
						<div class="filterEle">
							<label for="txt"><s:text name="app.subBranch" /></label>
							<s:select name="subBranchIdParam" id="subBranchIdParam"
								list="subBranchesMap" headerKey="" headerValue="Select"
								cssClass="input-sm form-control select2" />
						</div>
					</s:if>
						<s:if test="currentTenantId!='livelihood'">
					
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('season')}" /></label>
						<div class="form-element">
							<s:select name="selectedSeason" id="selectedSeason"
								list="{}" headerKey=""
								headerValue="Select"
								class="form-control input-sm select2" />
						</div>
					</div></s:if>
					<div class="filterEle" style="margin-top: 24px;">
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
			</section>
		</div>
	</s:form>

	<div class="appContentWrapper marginBottom">
 		<div class="flex-layout reportData">
 				<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span> <s:text name="totalQty" />&nbsp;<span class="strong"
							id="totalQty"></span>&nbsp;kgs
						</span>
					</div>
					<s:if test="currentTenantId!='livelihood'">
					<div class="report-summaryBlockItem">
						<span><s:text name="totalNoOfBags" />&nbsp;<span
							class="strong" id="totalNoOfBags"></span></span>
					</div>
				</s:if>
				</div>
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
		</div>
		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
