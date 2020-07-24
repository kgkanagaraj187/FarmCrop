<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
</head>
<style>

.flexi12:first-child:nth-last-child(11),
.flexi12:first-child:nth-last-child(11) ~ .flexi12 {
  width: 9.09091%;
  border-right: none;
  border-top:none;
  border-bottom: none;
  border-left: none;
   }

</style>
<body>


	<s:form name="form">
		<div class="filterDiv  flexiWrapper appContentWrapper marginBottom">
			<!-- <section class="reportWrap row">
				<div class="gridly"></div>
 -->
				<div class="filterEle flexi flexi12 dateFilter" style="width:180px;">
					<label for="txt"><s:text name="startingDateRange" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" />
					</div>
				</div>

					<div class="filterEle flexi flexi12 agent">
					<label for="branchIdParam"><s:property
							value="%{getLocaleProperty('agent')}" /></label>
					<div class="form-element ">
						<s:select name="filter.createdUser" id="agent" list="agentList"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div> 
				<div class="filterEle flexi flexi12 hide textBoxFilter">
					<label for="txt"></label>
					<div class="form-element">
						<s:textfield theme="simple" maxlength="25" cssClass="form-control" />
					</div>
				</div>

				<div class="filterEle flexi flexi12 hide selectFilter">
					<label for="txt"></label>
					<div class="form-element">
						<s:select list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm" />
					</div>
				</div>
	
				<div class="filterEle flexi flexi12 seasonClass">
					<label for="branchIdParam"><s:property
							value="%{getLocaleProperty('cSeasonCode')}" /></label>
					<div class="form-element ">
						<s:select name="filter.season" id="seasonId" list="seasonList"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<s:if test="currentTenantId=='wilmar'">
				<div class="filterEle flexi flexi12 farmerClass ">
					<label for="branchIdParam"><s:property
							value="%{getLocaleProperty('State')}" /></label>
					<div class="form-element">
						<s:select cssClass="form-control input-sm cond select2" name="state"
							list="stateList" id="s.code" headerKey=""
							headerValue="%{getText('txt.select')}"
							onchange="loadFarmerWilmar(this);" />

					</div>
				</div>
				</s:if>
				<s:else>
				<div class="filterEle flexi flexi12 farmerClass ">
					<label for="branchIdParam"><s:property
							value="%{getLocaleProperty('village')}" /></label>
					<div class="form-element">
						<s:select cssClass="form-control input-sm cond select2" name="village"
							list="villageList" id="v.code" headerKey=""
							headerValue="%{getText('txt.select')}"
							onchange="loadFarmer(this);" />

					</div>
				</div>
				</s:else>
				
				<div class="filterEle flexi flexi12 farmerClass ">
					<label for="txt"><s:text name="farmer" /></label>
					<div class="form-element">
						<s:select name="farmer" id="farmerId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							onchange="loadFarm(this);"
							class="form-control  farmer cond input-sm select2" />
					</div>
				</div>

				<div class="filterEle flexi flexi12 farmClass">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farm')}" /></label>
					<div class="form-element">
						<s:select name="farmList" id="farmId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control farm cond  input-sm select2" />
					</div>
				</div>
               <div class="filterEle flexi flexi12 farmCropClass">
					<label for="txt"><s:text name="cropName" /></label>
					<div class="form-element">
						<s:select name="crop" id="cropId" list="cropList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control cond input-sm select2" />
					</div>
				</div>
				
				<div class="filterEle flexi flexi12 farmCropClass">
					<label for="txt"><s:text name="variety" /></label>
					<div class="form-element">
						<s:select name="variety" id="varietyId" list="varietyList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control cond input-sm select2" />
					</div>
				</div>
	 <%-- 		<s:if  test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle flexi flexi12 agent"> 	
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="select2" onchange="populateChildBranch(this.value)" />

								</div>
							</div>
						</s:if>
						<div class="filterEle flexi flexi12 agent">
							<label for="branchIdParam"><s:text name="app.subBranch" /></label>
							<div class="form-element">
								<s:select name="subBranchIdParam" id="subBranchIdParam"
									list="subBranchesMap" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2" />
							</div>
						</div>
					</s:if>
					<s:else>
						<s:if test='branchId==null'>
							<div class="filterEle flexi flexi12 agent">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParama" id="branchIdParama"
										list="branchesMap" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else>   --%>
 <s:if test='branchId==null'>
						<div class="filterEle flexi flexi12 agent"> 	
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="select2" onchange="populateChildBranch(this.value)" />

								</div>
							</div>
					</s:if>
					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle flexi flexi12 agent">
								<label for="txt"><s:text name="app.branch" /></label>
								<s:select name="branchIdParam" id="branchIdParam"
									list="parentBranches" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2"
									onchange="populateChildBranch(this.value)" />
							</div>
						</s:if>
						<div class="filterEle flexi flexi12 agent">
							<label for="txt"><s:text name="app.subBranch" /></label>
							<s:select name="subBranchIdParam" id="subBranchIdParam"
								list="subBranchesMap" headerKey="" headerValue="Select"
								cssClass="input-sm form-control select2" />
						</div>
					</s:if> 

			<div class="filterEle flexi flexi12 groupClass">
					<label for="txt"><s:text name="group" /></label>
					<div class="form-element">
						<s:select name="group" id="groupId" list="groupList" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control cond input-sm select2" />
					</div>
				</div>

				<div class="filterEle  buttonss" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm" id="generate"
						onclick="reloadGrid()" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>

			<!-- </section> -->
		</div>
		
	</s:form>


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

						<li><a href="#" onclick="exportXLS('')" role="tab"
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
	<s:form name="updateform" id="updateform"
		action="dynmaicCertificationReport_detail.action">
	<%-- 	<s:hidden key="id" /> --%>
		<s:hidden key="currentPage" />
		<s:hidden id="txnType" name="txnType" />
		<s:hidden id="reportType" name="entityType" />
	</s:form>
<script async defer
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>

	<script type="text/javascript">	
		var typez=<%out.print("'" + request.getParameter("txnType") + "'");%>
		var entityType = '<s:property value="entityType"/>';
		var filters = '<s:property value="filters"/>';
		var filterCols = '<s:property value="filterCols"/>';
		var tenant = '<s:property value="getCurrentTenantId()"/>';
			var url = window.location.href;
		var temp = url;
		for(var i = 0 ; i < 1 ; i++) {
			temp = temp.substring(0, temp.lastIndexOf('/'));
			}
		var href = temp;
		var coordinateImg = "red_placemarker.png";
		var iconImage = temp + '/img/' + coordinateImg;

		// Variable relate to loading Custom Popup
		var $overlays;
		var $modals;
		var $sliders;
		jQuery(document).ready(function(){
			loadCustomPopup();
		//	var filterCols='<s:property value="filters"/>';
			var tenant='<s:property value="getCurrentTenantId()"/>';
			var score='<s:property value="score"/>';
			if(tenant=='iccoa'){
				$('.iccoaHide').hide();
			}
			else{
				$('.iccoaHide').show();
			}
			if(isEmpty(typez)){
				alert("Please Configure Transaction type");
				return false;
			}else if(isEmpty(entityType)){
				alert("Please Configure Report type");
				return false;
			}
		
		
			if (entityType == '4' || entityType == '2') {
				$(".groupClass").remove();
				$(".groupClass").empty();
				$('#farmId').attr("name","filter.referenceId");
				$(".farmCropClass").remove();
				$(".farmCropClass").empty();
				$(".auditClass").remove();
				$('#farmId').select2();
			} else if (entityType == '3') {
				jQuery(".farmerClass").remove();
				jQuery(".farmClass").remove();
				$(".farmClass").empty();
				$(".farmerClass").empty();
				$('#group').attr("name","filter.referenceId");
				$(".farmCropClass").remove();
				$(".farmCropClass").empty();
				$(".auditClass").remove();
				$('#group').select2();
			} else if (entityType == '1' || entityType == '5') {
				$(".groupClass").remove();
				$(".farmClass").remove();
				$(".groupClass").empty();
				$(".farmClass").empty();
				$('#farmerId').attr("name","filter.referenceId");
				$(".farmCropClass").remove();
				$(".farmCropClass").empty();
			
				$('#farmerId').select2();
				 if(score!='2'){
						$(".auditClass").remove();
				 }
			}else if (entityType == '6') {
				$(".auditClass").remove();
				$(".groupClass").remove();
				$(".groupClass").empty();
				jQuery(".farmCropClass").removeClass("hide");
				jQuery(".farmerClass").removeClass("hide");
				jQuery(".farmClass").removeClass("hide");
				/* $('#group').attr("name","filter.referenceId");
				//$('#group').attr("id","filter.referenceId");
			
				$('#group').select2(); */
			} 
			
			var seasonType = '<s:property value="seasonType"/>';
			//alert("seasonType:"+seasonType);
			if (seasonType == '0') {
				//alert("###");
				$(".seasonClass").hide();
			}
			
			$("#daterange").data().daterangepicker.startDate = moment(
					document.getElementById("hiddenStartDate").value,
					"MM-DD-YYYY");
			$("#daterange").data().daterangepicker.endDate = moment(
					document.getElementById("hiddenEndDate").value,
					"MM-DD-YYYY");
			$("#daterange").data().daterangepicker.updateView();
			$("#daterange").data().daterangepicker
					.updateCalendars();
			
			
			
			
			$('.applyBtn').click();
			var d1 = jQuery('#daterange').val();
			if(filterCols !=''){
			//	alert("filterCols"+filterCols+","+filterCols.split(','));
				 $(filterCols.split(',')).each(function(k, v) {
					 var splittedVa = v.split("-");
				//	 alert(" v"+v);
					 $('#'+splittedVa[1]).attr("id",splittedVa[0]);
				 });
			}
	$('.filterss').empty();
	
			if(filters!=''){
				
				filters = JSON.parse(filters.replace(/&quot;/g,'"'));;
				 $(filters).each(function(k, v) {
					 var type=v.type;
					// alert("type"+type);
					 if(type=='1'){
						 var act = $('.textBoxFilter');
						var filt =  $(act).clone();
						 $(filt).find('label').text(v.name);
						 $(filt).find('input').addClass("filterss");
						 $(filt).find('input').attr("name","filterMap['"+v.code+"']");
						 $(filt).find('input').attr("id",v.code+"~1");
						 $(filt).removeClass('hide');
						 $(filt).removeClass('textBoxFilter');
						 $(filt).insertBefore('.buttonss');
						
					 }else  if(type=='2'){
						
						 var act = $('.dateFilter');
							var filt =  $(act).clone();
						 $(filt).find('label').text(v.name);
						 $(filt).find('input').addClass("filterss");
						 $(filt).find('input').attr("name","filterMap['"+v.code+"']");
						 $(filt).find('input').attr("id",v.code+"~2");
						 $(filt).removeClass('hide');
						 $(filt).removeClass('dateFilter');
						 $(filt).insertBefore('.buttonss');
						 $(filt).find('input').daterangepicker(
								 {
								     locale: {
								       format: "MM-DD-YYYY"
								     },
								   //  startDate: document.getElementById("hiddenStartDate").value,
								    // endDate: document.getElementById("hiddenEndDate").value
								 });
						$(filt).find('input').val('');
					 }else  if(type=='3'){
						 
							 var act = $('.selectFilter');
							var filt =  $(act).clone();
					var id = v.code+'~3';
						 var listVal = v.catVal;
						 $(filt).find('label').text(v.name);
						 $(filt).find('select').addClass("filterss");
						 $(filt).find('select').attr("name","filterMap['"+v.code+"']");
					 $(filt).find('select').attr("id",id);
					if(listVal!=''){
						
						
						 $(filt).removeClass('hide');
						 $(filt).removeClass('selectFilter');
						
						 $(filt).insertBefore('.buttonss');
						 $(filt).find('select').children('option:not(:first)').remove();
						 $(filt).find('select').select2();
						
						 if(listVal.indexOf("|") >=0){
							 $(listVal.split("|")).each(function(k,val){
							
								if(val!=''){
								
									$(filt).find('select').append($('<option>', {
									    value: val.split("~")[0].trim(),
									    text: val.split("~")[1].trim()
									}));
								}
							 });
						 }else{
							 $(filt).find('select').append($('<option>', {
								    value: listVal.split("~")[0].trim(),
								    text: listVal.split("~")[1].trim()
								}));
						 }
						 
						
					}
						 
					
					 }
					
				 });
			} 
			
			loadGrid();
		
			var url = window.location.href;
			if (url.indexOf('Report_') > 0) {
				$('.filterDiv').show();
				
				$('#updateform').prop('action','dynmaicCertificationReport_detail.action');
			}else{
				$('.filterDiv').hide();
				
			}
		});
		
		function loadGrid(){
			var colNames='<s:property value="mainGridCols"/>';
			var gridColumnNames = new Array();
			var gridColumnModels = new Array();
			
			$("#entityType").val(entityType);
			$("#txnType").val(typez);
			
			$(colNames.split("|")).each(function(k,val){
					if(!isEmpty(val)){
						gridColumnNames.push(val);
						gridColumnModels.push({name: val,sortable: false});
					}
			});
			
			jQuery("#detail").jqGrid({
				url:'dynmaicCertificationReport_data.action?',
			   	pager: '#pagerForDetail',
			   	datatype: "json",
			   	mtype: 'POST',			
			   	postData : {
					"startDate" : function() {
						return document
								.getElementById("hiddenStartDate").value;
					},
					"endDate" : function() {
						return document
								.getElementById("hiddenEndDate").value;
					},
					
					"txType" : function() {
						
						return "report";
					},
				
					 "condtion" : function() {
						
						return getCondition();
					}, "fieldCondition" : function() {
						
						return getFieldCondition();
					},
					 "filter.referenceId" : function() {
						return gerRefId();
					}, "filter.createdUser" : function() {
						return $('#agent').val()+"~"+$('#agent').find('option:selected').text()
						
					} ,
					<s:if test='branchId==null'>
					  "branchIdParma" : function(){
						  return document.getElementById("branchIdParma").value;
					  },
					  </s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						  "subBranchIdParam" : function(){
							  return document.getElementById("subBranchIdParam").value;
						  },
						  </s:if>
					/*"filter.totalScore" : function() {
						return document
						.getElementById("score").value;;
					},
					"filter.conversionStatus" : function() {
						return document
						.getElementById("inspectionStatus").value;;
					}, */
					
					"txnType" : function() {
						return typez;
					},
					"entityType" : function() {
						return entityType;
					},
					"seasonCode" : function() {
						return document
								.getElementById("seasonId").value;
					},
				},
			   	colNames:gridColumnNames,
			   	colModel:gridColumnModels,
			   	height: 301, 
			   	width: $("#baseDiv").width(),
			   	scrollOffset: 19,     
			   	sortname:'id',
			   	shrinkToFit:true,
			   	sortorder: "desc",
			   	rowNum:10,
			   	rowList : [10,25,50],
			   	viewrecords: true,  	
			   	subGrid: false,
			     beforeSelectRow: 
			        function(id, e) {
			    	var iCol = jQuery.jgrid.getCellIndex(e.target);
			    	var rowData = jQuery("#detail").getRowData(id);
			        if(parseInt(iCol) == parseInt(gridColumnNames.length-1) || parseInt(iCol) == parseInt(gridColumnNames.length-2)){
			              	return false; 
			            }else{
			            	return true;
			            }
			           
			        }, 
			 	onSelectRow: function(id){ 
			 		var act =  $('#updateform').prop('action') ;
			 		
			 		if(act.indexOf("?txnType=")){
				act = act.split("?txnType=")[0]+"?txnType="+typez+"&id="+id;
				
			}
			 			
			 		   document.updateform.action  =act;
					//  document.updateform.typez.value  =id;
			          document.updateform.submit();       
					},
			
			
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
			});
			jQuery("#clear").click( function() {
				clear();
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
			//jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
			
		}
function getCondition(){
			
			var cond = {};
		
			$('.filterss').each(function(button) {
				if($(this).val()!=''){
				//cond = $(this).attr("name")+"="+$(this).val() +"," +cond;
				cond[$(this).attr("id")] = $(this).val();
				
				
				}
			});
			
			return JSON.stringify(cond);
		}
		
function getFieldCondition(){
	
	var cond = {};

	$('.cond').each(function(button) {
		if($(this).val()!='' && $(this).attr("name")!='filter.referenceId'){
		//cond = $(this).attr("name")+"="+$(this).val() +"," +cond;
		cond[$(this).attr("id")+"~"+$(this).parent().parent().find('label').text()] = $(this).val()+"~"+$(this).find('option:selected').text();
		
		}
	});
	
	return JSON.stringify(cond);
}

function loadFarm(call) {

	   var selectedFarmer = call.value;
	   $('.farm').empty();

	   if(!isEmpty(selectedFarmer)){
	     $.post("dynamicCertification_populateFarm", {selectedFarmer: selectedFarmer}, function (data) {
	   // $('[name="farmList"]').attr("id");
	   //  insertOptions( $('[name="farmList"]').attr("id"),$.parseJSON(data));
	    	 insertOptions($('.farm').attr("id"),$.parseJSON(data));
	        });
	   }
	     
	  }
	  


function loadFarmer(call) {

	var selectedFarmer = call.value;
	$('.farmer').empty();
	//$('#farmerId').select2();
	if(!isEmpty(selectedFarmer)){
		 $.post("dynamicCertification_populateFarmer", {selectedVillage: selectedFarmer}, function (data) {
	     
			 insertOptions($('.farmer').attr("id"),$.parseJSON(data));
	     });
	}
			
}

function loadFarmerWilmar(call) {

	var selectedFarmer = call.value;
	$('.farmer').empty();
	//$('#farmerId').select2();
	if(!isEmpty(selectedFarmer)){
		 $.post("dynamicCertification_populateFarmerWilmar", {selectedState: selectedFarmer}, function (data) {
	     
			 insertOptions($('.farmer').attr("id"),$.parseJSON(data));
	     });
	}
			
}


function gerRefId(){
			
			var refId = '';
			//alert($("select[name='filter.referenceId']").val());
			refId = $("select[name='filter.referenceId']").val();


			if(refId==undefined){
				refId='';
			}
			return refId;
		}
		function clear() {

		var typez =<%out.print("'" + request.getParameter("txnType") + "'");%>
		<%-- var reportType =<%out.print("'" + request.getParameter("reportType") + "'");%> --%> 
		
		window.location.href="dynmaicCertificationReport_list?txnType="+typez;
	//	window.location.href="dynmaicCertificationReport_list?txnType="+typez;
		
		}
	
		function exportXLS(objId) {
			var dataa = {};
			jQuery("#detail").setGridParam({
				postData : {
					rows : 0,
					headerFields : {}
				}
			});
			jQuery("#detail").jqGrid('excelExport', {
				url : 'dynmaicCertificationReport_populateXLS.action?id='+objId,
				postData : dataa
			});
		}

		function reloadGrid() {

			//jQuery("#detail").jqGrid('setGridParam',{url:"dynmaicCertificationReport_data.action?",page:1}).trigger('reloadGrid');

			var d1 = jQuery('#daterange').val();
			var d2 = d1.split("-");
			
			var value1 = d2[0];
			
			var value2 = d2[1];
		
			document.getElementById("hiddenStartDate").value = value1;

			document.getElementById("hiddenEndDate").value = value2;
			var startDate = new Date(
					document.getElementById("hiddenStartDate").value);
		
			var endDate = new Date(
					document.getElementById("hiddenEndDate").value);
			
			if (startDate > endDate) {
				alert('<s:text name="date.range"/>');
			} else {
				document.getElementById("hiddenStartDate").value;
				document.getElementById("hiddenEndDate").value;
				jQuery("#detail").jqGrid('setGridParam', {
					url : "dynmaicCertificationReport_data.action?",
					page : 1
				}).trigger('reloadGrid'); 
			}
		}
	

		function loadCustomPopup(){
			$overlays = $('<div id="modOverlay"></div>');
			$modals = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
			$sliders = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
			$close = $('<button id="clsBtn" class="btnCls">X</button>');
			
			$modals.append($sliders, $close);
			$('body').append($overlays);
			$('body').append($modals);
			$modals.hide();
			$overlays.hide();

			jQuery("#clsBtn").click(function () {
		    	$('#modalWin').css('margin-top','-230px');	
				$modals.hide();
				$overlays.hide();			
				$('body').css('overflow','visible');
			});
		}


		function enablePopup(head, cont){
			$(window).scrollTop(0); 
			$('body').css('overflow','hidden');
			$(".bjqs").empty();		
			var heading='';
			var contentWidth='100%';
			if(head!=''){
				heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
				contentWidth='92%';
			}
			var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
			$(".bjqs").append('<li>'+heading+content+'</li>')
			$(".bjqs-controls").css({'display':'block'});
			$('#modalWin').css('margin-top','-200px');
			$modals.show();
			$overlays.show();
			$('#banner-fade').bjqs({
		        height      : 482,
		        width       : 600,
		        showmarkers : false,
		        usecaptions : true,
		        automatic : true,
		        nexttext :'',
		        prevtext :'',
		        hoverpause : false                                           

		    });
		}

		function showFarmMap(latitude, longitude){	
			var heading ='<s:text name="coordinates"/>&nbsp;:&nbsp'+latitude+","+longitude;
			var content="<div id='map' class='smallmap'></div>";
			enablePopup(heading,content);
			//loadMap('map', latitude, longitude);
			initMap();
			 loadMap(latitude, longitude);
			//loadMap("farmMap",landArea);
			
	          /*   var farmCoordinate = jQuery("#farmCordinates").val();
	            var landArea = JSON.parse(farmCoordinate); */
	           
	            
		}
		
		function initMap() {
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 11.0168,
					lng : 76.9558
				},
				zoom : 5,
				mapTypeId: google.maps.MapTypeId.HYBRID,
			});
			
			
		}
		function loadMap(latitude, longitude){
	
			intermediateImg = "red_placemarker.png";
			 intermediatePointIcon = temp + '/img/'+intermediateImg;
				//alert(intermediatePointIcon+","+longitude);
			marker = new google.maps.Marker({
				position : new google.maps.LatLng(latitude,
						longitude),
				
				icon:intermediatePointIcon,
				map : map
						
			});
			map.setCenter(new google.maps.LatLng(latitude, longitude));
			 map.setZoom(7);
			
		}
		
	          function setMapOnAll(map) {
			        for (var i = 0; i < markersArray.length; i++) {
			        	markersArray[i].setMap(map);
			        }
			        markersArray = new Array();
			 }
	         
	          function getBranchIdDyn(){
	        		return null;
	        	}
		
	</script>
</body>
