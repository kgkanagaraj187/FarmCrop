<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
var fertiCulitvIds=[];
var pestiCulitvIds=[];
var manureCulitvIds=[];
var labourTotDatas="";
var landTotDatas="";
var sowingDatas="";
var gapTotDatas="";
var weedData="";
var cultureData="";
var irrigaData="";
var festiTotalData="";
var harvestData="";
var otherExpData="";
var totPestiData="";
var totManureData="";
var tenantId='<s:property value="getCurrentTenantId()"/>';

jQuery(document).ready(function(){
	jQuery(".back-btn").click( function() {
	document.cancelform.submit();
	});
	jQuery("#update").click( function() {
		document.updateform.submit();
	});
	jQuery("#delete").click( function() {
		document.deleteform.submit();
	});
	
	
		$(".hideSoilPrepare").show();
	
});

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}


function calLandTotal(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		
			var cultiId=jQuery(this).find(".landCultiId").val();
			totAmt= parseFloat(jQuery(this).find(".totalLandVal").val());
			if(!isNaN(totAmt)){
					totAmt = totAmt;	
			}else{
					totAmt=0.00
			}
			landTotDatas+='0'+"###"+'0'+"###"+totAmt.toFixed(2)+"###"+cultiId+"###"+'0'+"@@@";
		});
		jQuery('#landTotalDatas').val(landTotDatas);
}



function calSowingTotal(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
			var cultiId=jQuery(this).find(".sowingCultiId").val();
			var totAmt = parseFloat(jQuery(this).find(".totalSowing").val());
			
			if(!isNaN(totAmt)){
				totAmt =totAmt;
			}
			else{
				totAmt = 0.00;	
			}
			sowingDatas+='0'+"###"+'0'+"###"+totAmt.toFixed(2)+"###"+cultiId+"@@@";
		
	});
	jQuery('#sowingDatas').val(sowingDatas);
}


function calGapTotal(obj){
	
	var totAmt =0.00;
   var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		
   var cost1 = parseFloat(jQuery(this).find(".totalGapCost").val());
	var cultiId=jQuery(this).find(".gapCultiId").val();
	var totAmt =0.00;
	if(!isNaN(cost1)){
	 	totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}

	gapTotDatas+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	jQuery('#gapDatas').val(gapTotDatas);
}


function calWeedingTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	var cost1 = parseFloat(jQuery(this).find(".totWeedId").val());
	var cultiId=jQuery(this).find(".weedingCultiId").val();
	var totAmt =0.00;
	if(!isNaN(cost1)){
	 totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	weedData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	
	jQuery('#weedingDatas').val(weedData);
}


function calCultureTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	var cost1 = parseFloat(jQuery(this).find(".totCulture").val());
	var cultiId=jQuery(this).find(".cultureCultiId").val();
	var totAmt =0.00;
	if(!isNaN(cost1)){
	 totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	cultureData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	
	jQuery('#cultureDatas').val(cultureData);
}


function calIrrigationTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	var cost1 = parseFloat(jQuery(this).find(".totIrrigation").val());
	var cultiId=jQuery(this).find(".irrigatCultiId").val();
	if(!isNaN(cost1)){
	 totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	irrigaData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	
	jQuery('#irrigationDatas').val(irrigaData);
}


function calFertiTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		
		var cost1 = parseFloat(jQuery(this).find(".fertiTot").val());
		var cultiId=jQuery(this).find(".fertiTotCultiId").val();
		 
		if(!isNaN(cost1)){
			 totAmt = cost1;	
			}else{
				totAmt = 0.00;	
			}
		festiTotalData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	jQuery('#festiTotalDatas').val(festiTotalData);
}

			

function fertiValue(){
	var rows = $(".fertiCostTR").find('td.fertiCostTD');
	var fertiDatas="";
	for(i=1;i<=rows.length;i++){
		var fertiCatType=$("#fertiCatType"+i).val();
		//console.log(fertiCatType)
		var	fertiQty=$("#fertiQty"+i).val();
		var	fertiCost=$("#fertiCost"+i).val();
		var	fertiUsage=$("#fertiUsage"+i).val();
		var	fertiId=jQuery("#fertiId"+i).val();
		var cultiId=jQuery("#fertiCultiId"+i).val();
		var fertiUom=jQuery("#fertiUom"+i).val();
		
			fertiDatas+='0'+"###"+'0'+"###"+fertiCost+"###"+'0'+"###"+fertiId+"###"+cultiId+"###"+'0'+"@@@";
		
	}
	jQuery("#fertilizerDatas").val(fertiDatas); 
	
}


function pestiValue(){
	var rows = $(".pestCostTR").find('td.pestCostTD');
	var pestiDatas="";
	for(i=1;i<=rows.length;i++){
	var pestiCatType=$("#pestiCatType"+i).val();
	var	qty=$("#pestiQty"+i).val();
	var	cost=$("#pestiCost"+i).val();
	var	pestiUsage=$("#pestiUsage"+i).val();
	var	pestiId=jQuery("#pestiId"+i).val();
	var cultiId=jQuery("#pestiCultiId"+i).val();
	var pestiUom=jQuery("#pestiUom"+i).val();
	
		pestiDatas+='0'+"###"+'0'+"###"+cost+"###"+'0'+"###"+pestiId+"###"+cultiId+"###"+'0'+"@@@";
		
	}
	
	jQuery("#pesticideDatas").val(pestiDatas); 
	
}
function manureValue(){
	var rows = $(".manureCostTR").find('td.manureCostTD');
	var manureDatas="";
	for(i=1;i<=rows.length;i++){
		var manureCatType=$("#manureCatType"+i).val();
		var	manureQty=$("#manureQty"+i).val();
		var	manureCost=$("#manureCost"+i).val();
		var	manureUsage=$("#manureUsage"+i).val();
		var	manureId=jQuery("#manureId"+i).val();
		var	manureCultiId=jQuery("#manureCultiId"+i).val();
		var manureUom=jQuery("#manureUom"+i).val();
		
			manureDatas+='0'+"###"+'0'+"###"+manureCost+"###"+'0'+"###"+manureId+"###"+manureCultiId+"###"+'0'+"@@@";
		
		
	}
	jQuery("#manureDatas").val(manureDatas); 
	
}


function calHarvestTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		
		var cost1 = parseFloat(jQuery(this).find(".totHarvest").val());
		var cultiId=jQuery(this).find(".harvestCultiId").val();
		
		if(!isNaN(cost1)){
		 totAmt = cost1;	
		}else{
			totAmt = 0.00;	
		}
		harvestData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";
	});
	
	jQuery("#harvestDatas").val(harvestData); 
}


function calOtherExp(obj){
	
	var totAmt = 0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	totAmt = parseFloat(jQuery(this).find(".totExpenseVal").val());
	var cultiId=jQuery(this).find(".otherExpCultiId").val();
	
	if(!isNaN(totAmt)){
		totAmt = totAmt;
		}else{
			totAmt = 0.00;	
		}
	otherExpData+='0'+"###"+'0'+"###"+'0'+"###"+totAmt.toFixed(2)+"###"+cultiId+"@@@";
	});
	jQuery("#otherExpenceDatas").val(otherExpData); 
}



function calPestiTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	var cost1 = parseFloat(jQuery(this).find(".pestiTot").val());
	var cultiId=jQuery(this).find(".pestiTotCultiId").val();
	if(!isNaN(cost1)){
	 totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	totPestiData+=cost1+"###"+cultiId+"@@@";
	});
	
	jQuery("#totPestiDatas").val(totPestiData); 
}


function calManureTot(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
	var cost1 = parseFloat(jQuery(this).find(".manureTot").val());
	var cultiId=jQuery(this).find(".manureTotCultiId").val();
	if(!isNaN(cost1)){
	 totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	totManureData+=totAmt.toFixed(2)+"###"+cultiId+"@@@";

	});
	
	jQuery("#totalManureDatas").val(totManureData); 
}

function calLabourTotal(obj){
	
	var totAmt =0.00;
	var tableBody = $(obj).parent().parent();
	$.each(tableBody, function(index, value) {
		
		
	var cost1 = parseFloat(jQuery(this).find(".labourCostClas").val());
	var cultiId=jQuery(this).find(".labourCultiId").val();
	var totAmt =0.00;
	if(!isNaN(cost1)&& !isNaN(cost1) ){
	
		totAmt = cost1;	
	}else{
		totAmt = 0.00;	
	}
	labourTotDatas+=cost1+"###"+cultiId+"@@@";

	});
	jQuery('#labourCostDatas').val(labourTotDatas);
}

							
</script>
<s:hidden key="cultivation.id" id="id" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform"
	action="cultivation_%{command}?type=service" method="post"
	enctype="multipart/form-data">
	<s:hidden id="landTotalDatas" name="landTotalDatas" />
	<s:hidden id="sowingDatas" name="sowingDatas" />
	<s:hidden id="gapDatas" name="gapDatas" />
	<s:hidden id="weedingDatas" name="weedingDatas" />
	<s:hidden id="cultureDatas" name="cultureDatas" />
	<s:hidden id="irrigationDatas" name="irrigationDatas" />
	<s:hidden id="festiTotalDatas" name="festiTotalDatas" />
	<s:hidden id="harvestDatas" name="harvestDatas" />
	<s:hidden id="otherExpenceDatas" name="otherExpenceDatas" />
	<s:hidden id="totPestiDatas" name="totPestiDatas" />
	<s:hidden id="totalManureDatas" name="totalManureDatas" />
	<s:hidden id="fertilizerDatas" name="fertilizerDatas" />
	<s:hidden id="pesticideDatas" name="pesticideDatas" />
	<s:hidden id="manureDatas" name="manureDatas" />
	<s:hidden id="labourCostDatas" name="labourCostDatas" />
	<s:hidden key="command" />
<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
							<s:if test="cultivationList.size()>0">
							<div class="aPanel cost_info">
								<div class="aTitle">
									<h2>
										<s:property
											value="%{getLocaleProperty('info.costCultivation')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#costInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="#costInfo">
									<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop1" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">
								<s:if
									test="%{#cultivation.landTotal!='' && #cultivation.landTotal!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop1">
										<s:set var="breakLoop1" value="%{false}" />
										<tr style="background-color: #EAF2F8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
													value="%{getLocaleProperty('landPreparation')}" /></span></td>
										</tr>
										<tr class="odd">
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>

									</s:if>
									<tr>
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd-MM-yyyy hh:mm:ss a" /></td>
										<%-- <td colspan="2" class="landTotalTD hideSoilPrepare"><s:textfield
												class="ploughVal" maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.landPloughing)}"
												onkeyup="calLandTotal(this);"
												onkeypress="return isDecimal(event)" /></td>
										<td colspan="2"><s:textfield class="ridgeVal hideSoilPrepare"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.ridgeFurrow)}"
												onkeyup="calLandTotal(this);"
												onkeypress="return isDecimal(event)" /></td>
												<td colspan="2"><s:textfield class="soilProperation"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.soilPreparation)}"
												onkeyup="calLandTotal(this);"
												onkeypress="return isDecimal(event)" /></td> --%>
										<td colspan="6"><s:textfield class="totalLandVal"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.landTotal)}"
												onkeyup="calLandTotal(this);"
												onkeypress="return isDecimal(event)" /></td>
										<td class="hide" colspan="3"><s:textfield
												class="landCultiId" value="%{#cultivation.id}" /></td>

									</tr>
								</s:if>
							</s:iterator>
						</table>
						<table class="table table-bordered aspect-detail">	
							<s:set var="breakLoop2" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">


								<s:if
									test="%{#cultivation.totalSowing!='' && #cultivation.totalSowing!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop2">
										<s:set var="breakLoop2" value="%{false}" />
										<tr style="background-color: #EAF2F8">
											<td colspan="12"><span style='font-weight: bold;'>
											<s:property value="%{getLocaleProperty('sowing')}" /></span></td>
										</tr>
										<tr class="odd">
											
																<th colspan="6"><s:text name="date" /></th>
																<%-- <th colspan="3" class="hideSoilPrepare"><s:text
																		name="seedCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="sowingTreat" />(<s:property
																		value="%{getCurrencyType()}" />)</th> --%>
																<%-- <th colspan="2"><s:text name="labourCost-men"/></th>
											<th><s:text name="labourCost-women"/></th>  --%>
																<th colspan="6"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															
															<%-- 
																<th colspan="3"><s:text name="date" /></th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="seedCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="sowingTreat" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2"><s:text name="labourCost-men"/></th>
											<th><s:text name="labourCost-women"/></th> 
																<th colspan="3"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																		
															 --%>
										</tr>
									</s:if>
									<tr>
										<%-- <td colspan="3"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<td colspan="3"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}" /></td>
										<td class="hide" colspan="3"><s:textfield
												class="seedCost"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}"
												onkeyup="calSowingTotal(this);" /></td>

										<td colspan="3"><s:textfield class="sowingTreat"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingTreat)}"
												onkeyup="calSowingTotal(this);" maxLength="9"
												onkeypress="return isDecimal(event)" /></td>
										<td colspan="3"><s:label class="totalSowing"
												value="%{#cultivation.totalSowing}" /></td>
										<td class="hide" colspan="3"><s:textfield
												class="sowingCultiId" value="%{#cultivation.id}" /></td> --%>
												
															<td colspan="6"><s:date
																	name="%{#cultivation.expenseDate}"
																	format="dd/MM/yyyy hh:mm:ss a" /></td>
															<%-- <td colspan="3" class="hideSoilPrepare"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:textfield class="sowingTreat"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingTreat)}"
												onkeyup="calSowingTotal(this);" maxLength="9"
												onkeypress="return isDecimal(event)" /></td> --%>
															<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostWomen)}"/></td> --%>
															<td colspan="6"><s:textfield class="totalSowing"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.totalSowing)}" maxLength="9" onkeyup="calSowingTotal(this);"
												onkeypress="return isDecimal(event)" /></td>
												<td class="hide" colspan="3"><s:textfield
												class="sowingCultiId" value="%{#cultivation.id}" /></td>
														<%-- 
															<td colspan="3"><s:date
																	name="%{#cultivation.expenseDate}"
																	format="dd/MM/yyyy hh:mm:ss a" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:textfield class="sowingTreat"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingTreat)}"
												onkeyup="calSowingTotal(this);" maxLength="9"
												onkeypress="return isDecimal(event)" /></td>
															<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostWomen)}"/></td>
															<td colspan="3"><s:label class="totalSowing"
												value="%{#cultivation.totalSowing}" /></td>
												<td class="hide" colspan="3"><s:textfield
												class="sowingCultiId" value="%{#cultivation.id}" /></td>
														 --%>	
									</tr>
								</s:if>
							</s:iterator>
						</table>
						<table class="table table-bordered aspect-detail">	
							<s:set var="breakLoop3" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">

								<s:if
									test="%{#cultivation.totalGap!='' && #cultivation.totalGap!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop3">
										<s:set var="breakLoop3" value="%{false}" />
										<tr style="background-color: #EAF2F8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																	value="%{getLocaleProperty('gapFilling')}" /></span></td>
										</tr>
										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<td colspan="6"><s:textfield class="totalGapCost" onchange="calGapTotal(this)"
												onkeypress="return isDecimal(event)" value="%{#cultivation.totalGap}" /></td>
										<td class="hide"><s:textfield class="gapCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop4" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">

								<s:if
									test="%{#cultivation.totalWeed!='' && #cultivation.totalWeed!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop4">

										<s:set var="breakLoop4" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><b>
											<s:property value="%{getLocaleProperty('weeding')}"/></b></span></td>
										</tr>

										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>

										<td colspan="6"><s:textfield class="totWeedId"  onchange="calWeedingTot(this)"
												onkeypress="return isDecimal(event)" value="%{#cultivation.totalWeed}" /></td>
										<td class="hide"><s:textfield class="weedingCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop5" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">

								<s:if
									test="%{#cultivation.totalCulture!='' && #cultivation.totalCulture!=null }">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop5">
										<s:set var="breakLoop5" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																		value="%{getLocaleProperty('interCultural')}" /></span></td>
										</tr>

										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>

										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>

										<td colspan="6"><s:textfield class="totCulture" onchange="calCultureTot(this)"
												onkeypress="return isDecimal(event)" value="%{#cultivation.totalCulture}" /></td>
										<td class="hide"><s:textfield class="cultureCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop6" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">

								<s:if
									test="%{#cultivation.totalIrrigation!='' && #cultivation.totalIrrigation!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop6">
										<s:set var="breakLoop6" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																	value="%{getLocaleProperty('irrigation')}" /></span></td>
										</tr>
										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>

										<td colspan="6"><s:textfield class="totIrrigation" onchange="calIrrigationTot(this)"
												onkeypress="return isDecimal(event)" value="%{#cultivation.totalIrrigation}" /></td>
										<td class="hide"><s:textfield class="irrigatCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop10" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">




								<s:if
									test="%{#cultivation.totalHarvest!='' && #cultivation.totalHarvest!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop10">
										<s:set var="breakLoop10" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																	value="%{getLocaleProperty('harvesting')}" /></span></td>
										</tr>
										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>

										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>

										<td colspan="6"><s:textfield class="totHarvest" onchange="calHarvestTot(this)"
												onkeypress="return isDecimal(event)" value="%{#cultivation.totalHarvest}" /></td>
										<td class="hide"><s:textfield class="harvestCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop11" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">
								<s:if
									test="%{#cultivation.totalExpense!='' && #cultivation.totalExpense!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop11">
										<s:set var="breakLoop11" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																	value="%{getLocaleProperty('otherExpenses')}" /></span></td>
										</tr>
										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="totalCost" /></th>
										</tr>
									</s:if>
									<tr>
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<%-- <td colspan="2"><s:textfield class="packingVal"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.packingMaterial)}"
												onkeyup="calOtherExp(this);"
												onkeypress="return isDecimal(event)" /></td>
										<td colspan="2"><s:textfield class="transportVal"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.transport)}"
												onkeyup="calOtherExp(this);"
												onkeypress="return isDecimal(event)" /></td>
										<td colspan="2"><s:textfield class="extraVal"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.miscellaneous)}"
												onkeyup="calOtherExp(this);"
												onkeypress="return isDecimal(event)" /></td> --%>
										<td colspan="6"><s:textfield class="totExpenseVal"
												maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.totalExpense)}" onkeyup="calOtherExp(this);"
												onkeypress="return isDecimal(event)" /></td>
										<td class="hide"><s:textfield class="otherExpCultiId"
												value="%{#cultivation.id}" /></td>

									</tr>
								</s:if>

							</s:iterator>

</table>
							<table class="table table-bordered aspect-detail">
								<s:set var="breakLoop15" value="%{true}" />
							<s:iterator value="cultivationList" var="cultivation"
								status="status">

								<s:if
									test="%{#cultivation.labourCost!='' && #cultivation.labourCost!=null}">
									<s:set var="recordIdentifier" value="%{true}" />
									<s:if test="#breakLoop15">
										<s:set var="breakLoop15" value="%{false}" />
										<tr style="background-color: #eaf2f8">
											<td colspan="12"><span style='font-weight: bold;'><s:property
																	value="%{getLocaleProperty('labourDetail')}" /></span></td>
										</tr>
										<tr>
											<th colspan="6"><s:text name="date" /></th>
											<th colspan="6"><s:text name="laboutCost" /></th>
										</tr>
									</s:if>
									<tr>
											<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
											<td colspan="6"><s:textfield id="labourCost" class="labourCostClas" onkeyup="calLabourTotal(this);"
												value="%{#cultivation.labourCost}" /></td>
												
												<td class="hide"><s:textfield class="labourCultiId"
												value="%{#cultivation.id}" /></td>
									</tr>
								</s:if>
							</s:iterator>
							</table>
							
								</div>
							</div>
						</s:if>
						<s:if
							test='pesticidesList.size()>0 || fymsList.size()>0 || fertilizersList.size()>0'>
							<div class="aPanel quantity_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:property
											value="%{getLocaleProperty('info.quantityCultivation')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#costInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>

								<div class="aContent dynamic-form-con" id="#quantityInfo">

									<table class="table table-bordered aspect-detail">
							<s:set var="breakLoop7" value="%{true}" />
							<s:if test='fertilizersList.size()>0'>
								<s:set var="recordIdentifier" value="%{true}" />
								<s:if test="#breakLoop7">
									<s:set var="breakLoop7" value="%{false}" />
									<tr style="background-color: #eaf2f8">
										<td colspan="12"><span style='font-weight: bold;'><s:property
													value="%{getLocaleProperty('fertilizer')}" /> <%-- <s:text name="fertilizer" /></span> --%></td>
									</tr>
									<tr>
									
										<th colspan="6"><s:text name="date" /></th>
										<s:set var="recordIdentifier" value="%{true}" />
										<th colspan="6"><s:text name="costofFertilizer" /></th>
										
								
									</tr>
								</s:if>
								<s:set var="recordIdentifier" value="%{true}" />
								<s:iterator value="fertilizersList" var="cultiDetail"
									status="fertiStatus">
									<tr class="fertiCostTR">
									
										<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<%-- <td colspan="2"><s:property
												value="%{#cultiDetail.fertilizerName}" /></td> --%>
										<%-- <td class="hide"><s:textfield maxLength="9"
												id="fertiCatType%{#fertiStatus.count}"
												name="%{#cultiDetail.fertilizerType}"
												value="%{#cultiDetail.fertilizerType}" /></td> --%>
										<%-- <td class="fertiCostTD" colspan="2"><s:textfield
												onchange="fertiValue()" onkeypress="return isDecimal(event)"
												id='fertiQty%{#fertiStatus.count}' maxLength="9"
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td>
												--%>
										<td colspan="6" class="fertiCostTD"><s:textfield
												id='fertiCost%{#fertiStatus.count}' maxLength="9"
												onkeypress="return isDecimal(event)" onchange="fertiValue()"
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td>
										<%-- <td colspan="2"><s:property value="usageLevelName" /></td> --%>
										<td class="hide"><s:textfield
												id='fertiUsage%{#fertiStatus.count}'
												value="%{#cultiDetail.usageLevel}" /></td>
										<s:hidden value='%{#cultiDetail.id}'
											id='fertiId%{#fertiStatus.count}' />

										<s:hidden value='%{#cultiDetail.cultivation.id}'
											id='fertiCultiId%{#fertiStatus.count}' />
				


									</tr>
								</s:iterator>

							</s:if>
</table>

									<table class="table table-bordered aspect-detail">



							<s:set var="breakLoop8" value="%{true}" />


							<s:if test='pesticidesList.size()>0'>
								<s:set var="recordIdentifier" value="%{true}" />
								<s:if test="#breakLoop8">
									<s:set var="breakLoop8" value="%{false}" />
									<tr style="background-color: #eaf2f8">
										<td colspan="12"><span style='font-weight: bold;'><s:property
													value="%{getLocaleProperty('pesticide')}" /> <%-- <s:text name="pesticide" /> --%></span></td>
									</tr>
									<tr>
									
								   		<th colspan="6"><s:text name="date" /></th>
										<s:set var="recordIdentifier" value="%{true}" />
										<th colspan="6"><s:text name="costofPesticide" /></th>
								  

									</tr>
								</s:if>


								<s:set var="recordIdentifier" value="%{true}" />
								<s:iterator value="pesticidesList" var="cultiDetail"
									status="pestiStatus">
									<tr class="pestCostTR">
									
									<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<%-- <td colspan="2"><s:property
												value="%{#cultiDetail.fertilizerName}" /></td>
										<td class="hide"><s:textfield maxLength="9"
												id="pestiCatType%{#pestiStatus.count}"
												name="%{#cultiDetail.fertilizerType}"
												value="%{#cultiDetail.fertilizerType}" /></td>
										<td class="pestCostTD" colspan="2"><s:textfield
												maxLength="9" onchange="pestiValue()"
												onkeypress="return isDecimal(event)"
												id='pestiQty%{#pestiStatus.count}'
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td>
													 --%>
									<%-- 	<td colspan="6" class="pestCostTD"><s:textfield
												id='pestiCost%{#pestiStatus.count}' maxLength="9"
												onkeypress="return isDecimal(event)" onchange="pestiValue()"
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td> --%>
												
												<td colspan="6" class="pestCostTD"><s:textfield
												id='pestiCost%{#pestiStatus.count}' maxLength="9"
												onkeypress="return isDecimal(event)" onchange="pestiValue()"
												value="%{#cultivation.totalPesticide}" /></td>
												
										<%-- <td class="hide" colspan="2"><s:property value="usageLevelName" /></td> --%>
										<td class="hide"><s:textfield
												id='pestiUsage%{#pestiStatus.count}'
												value="%{#cultiDetail.usageLevel}" /></td>
										<s:hidden value='%{#cultiDetail.id}'
											id='pestiId%{#pestiStatus.count}' />

										<s:hidden value='%{#cultiDetail.cultivation.id}'
											id='pestiCultiId%{#pestiStatus.count}' />
									

									</tr>
								</s:iterator>



							</s:if>
</table>
						<table class="table table-bordered aspect-detail">
						<s:set var="breakLoop9" value="%{true}" />
							<s:if test='fymsList.size()>0'>
								<s:set var="recordIdentifier" value="%{true}" />
								<s:if test="#breakLoop9">
									<s:set var="breakLoop9" value="%{false}" />
									<tr style="background-color: #eaf2f8">
										<td colspan="12"><span style='font-weight: bold;'><s:text
													name="fym" /></span></td>
									</tr>
									<tr>
										
										<th colspan="6"><s:text name="date" /></th>
										<s:set var="recordIdentifier" value="%{true}" />
										<th colspan="6"><s:text name="costofFYM" /></th>
										

									</tr>
								</s:if>
								<s:set var="recordIdentifier" value="%{true}" />
								<s:iterator value="fymsList" var="cultiDetail"
									status="manureStatus">
									<tr class="manureCostTR">
									
								<td colspan="6"><s:date
												name="%{#cultivation.expenseDate}"
												format="dd/MM/yyyy hh:mm:ss a" /></td>
										<%-- <td colspan="2"><s:property
												value="%{#cultiDetail.fertilizerName}" /></td>
										<td class="hide"><s:textfield maxLength="9"
												id='manureCatType%{#manureStatus.count}'
												value="%{#cultiDetail.fertilizerType}" /></td>
										<td class="manureCostTD" colspan="2"><s:textfield
												maxLength="9" onkeypress="return isDecimal(event)"
												onchange="manureValue()"
												id='manureQty%{#manureStatus.count}'
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td>
												 --%>
										<td colspan="6" class="manureCostTD"><s:textfield
												id='manureCost%{#manureStatus.count}' maxLength="9"
												onkeypress="return isDecimal(event)"
												onchange="manureValue()"
												value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td>
										<%-- <td colspan="2"><s:property value="usageLevelName" /></td> --%>
										<td class="hide"><s:textfield
												id='manureUsage%{#manureStatus.count}'
												value="%{#cultiDetail.usageLevel}" /></td>

										<s:hidden value='%{#cultiDetail.id}'
											id='manureId%{#manureStatus.count}' />
										<s:hidden value='%{#cultiDetail.cultivation.id}'
											id='manureCultiId%{#manureStatus.count}' />
								
									</tr>
								</s:iterator>

							</s:if>

</table>
</div>
							</div>
						</s:if>
<s:if test="cultivationList.size()<=0 && pesticidesList.size()=0 && fymsList.size()=0 && fertilizersList.size()=0">
<s:set var="recordIdentifier" value="%{true}" />
			No records found....
</s:if>


</div>
<div class="yui-skin-sam">

						<span id="savebutton" class=""><span class="first-child">
								<button id="submitButton" class="save-btn btn btn-success"
									type="button" onclick="onSubmit();">
									<font color="#FFFFFF"> <b><s:text
												name="update.button" /></b>
									</font>
								</button>
						</span> <span id="cancel" class=""> <span class="first-child">
									<button type="button" class="back-btn btn btn-sts">
										<b><FONT color="#FFFFFF"><s:text
													name="cancel.button" /> </font></b>
									</button>
							</span>
						</span>
</div>
				</div>

			</div>

		</div>

	</div>

	<br />

</s:form>



<s:form name="cancelform" action="cultivation_list.action?type=service">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>

<s:form name="updateform"
	action="cultivation_update.action?type=service">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>

<%-- <s:form name="deleteform" action="cultivation_delete.action?type=service">
			<s:hidden key="currentPage" />
</s:form> --%>


<script type="text/javascript">

function onSubmit(){
	/* var ferti='<s:property value="fertilizersList.size()" />';
	var pesti='<s:property value="pesticidesList.size()" />';
	var manure='<s:property value="fymsList.size()" />';
	if(ferti!='0')
	{
		fertiValue();
	}
	if(pesti!='0')
	{
		pestiValue();
	}
	
	if(manure!='0')
	{
		manureValue();
	} */
	document.form.submit();
}
</script>