<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->
<s:head />
<style type="text/css">
.alignTopLeft {
	float: left;
	width: 6em;
}
.view {
	display: table-cell;
	background-color: #d2dae3;
}

.alignTopLeft {
	float: left;
	width: 6em;
}

select {
	width: 120px !important;
}

input[type="text"] {
	width: 120px;
}

.datepicker_clear {
	display: none;
}

.column-1 h3,.column-2 h3,.column-3 h3 {
	/*background: none repeat scroll 0 0 #6F9505 !important;*/
	border: 1px solid #fff !important;
	color: #fff !important;
	padding: 5px;
	margin: 0;
	text-align: left;
}

.alignCenter {
	text-align: center !important;
}

.alignLeft {
	text-align: left !important;
}

.alignRight {
	text-align: right !important;
}

.generalTable th {
	marign-top: 10px;
}

.column-2,.column-3 {
	margin-top: 25px;
}

.borNone {
	border-right: none !important;
	border-left: none !important;
	border-top: none !important;
	border-bottom: none !important;
}

.div.error {
	color: #FD0000;
	font-size: 12px;
	/*padding: 5px 10px 5px 0;*/
	text-align: left;
	width: auto;
	margin-bottom:0px !important;
}
</style>

</head>
<body>

<s:form id="stockTransferForm" action="farmerProductTransfer_create" method="post"
	cssClass="fillform">
	<s:hidden name="agentId" />
	<s:hidden id="gradeInputString" name="gradeInputString" />
	<div class="error">
		<div id="validateError" style="text-align: center;"></div>
	</div>
	<font color="red"> <s:actionerror /></font>
		<div>
				 <div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<p class="notification">
							<span class="manadatory">*</span>
							<s:text name="reqd.field" />
							<span id="validateError"></span> <span id="validatePriceError"></span>
						</p>
					</div>
				</div>
			</div>
			
			
			<div style="text-align:right;">
       <!--  <b><i class="fa fa-tint" aria-hidden="true" style="color:blue;"></i> -->
        <b><s:text name="season.name"/>:<s:property value="seasonName"/></b>
       </div>
		<div class="appContentWrapper marginBottom ">
							<div class="formContainerWrapper">
								<h2><s:text name="info.general" /></h2>
									<div class="flexiWrapper filterControls">
									
										<div class="flexi flexi8">
												<label for="txt"><s:text name="sender" /> <span class="manadatory">*</span></label>
												<div class="form-element">
													<s:select name="selectedCoOperative" list="listWarehouse" cssClass="col-sm-3 form-control select2"  headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key"
													listValue="value" id="coOperative" onchange="populateFarmers(this);resetTableDetails();"/>
												</div>
										</div>
									 	
										<div class="flexi flexi8">
												<label for="txt"><s:text name="receiver" /> <span class="manadatory">*</span></label>
												<div class="form-element">
													<s:select name="selectedRecCoOperative" list="listWarehouse" cssClass="col-sm-3 form-control select2"  headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key"
														listValue="value" id="recCoOperative"/>
												</div>
										</div>
										
									<div class="flexi flexi8">
												<label for="txt"><s:text name="stockTransferdate" /> <span class="manadatory">*</span></label>
												<div class="form-element">
													<s:textfield name="startDate" id="startDate" readonly="true"  theme="simple" data-date-format="dd/mm/yyyy" 
														data-date-viewmode="years" cssClass="date-picker form-control col-sm-3"/>
												</div>
										</div>									
										
										<div class="flexi flexi8">
												<label for="txt"><s:text name="truckId" /> <span class="manadatory">*</span></label>
												<div class="form-element">
													<s:textfield name="truckId" id="truckId" maxlength="35" cssClass="form-control"/>
												</div>
										</div>
											<div class="flexi flexi8">
												<label for="txt"><s:text name="driverId" /> <span class="manadatory">*</span></label>
												<div class="form-element">
													<s:textfield name="driverId" id="driverId" maxlength="35" cssClass="form-control"/>
												</div>
										</div>
										
									</div>
							</div>
						</div>	 
			
				<div class="service-content-wrapper">
								<div class="service-content-section">

									<div class="appContentWrapper marginBottom">
										<div class="formContainerWrapper">
												<h2><s:text name="info.stockTransferDetails" /></h2>
											<div class="flexiWrapper filterControls">
											
												<div class="flexi flexi8">
														<label for="txt"><s:text name="farmer" /><span class="manadatory">*</span> </label>
														<div class="form-element">
															<s:select id="farmer" headerKey="" headerValue="%{getText(txt.select)}"
																name="selectedFarmer" list="farmerMap" cssClass="col-sm-3 form-control" 
																onchange="populateProducts(this);resetTableDetails();" />
														</div>
													</div>
													
												<div class="flexi flexi8">
														<label for="txt"><s:text name="product" /> <span class="manadatory">*</span> </label>
														<div class="form-element">
															<s:select id="product" headerKey="" headerValue="%{getText(txt.select)}"
													name="selectedProduct" list="productMap" cssClass="col-sm-3 form-control" 
													onchange="populateVariety(this);resetVariety();" />
														</div>
													</div>
											<div class="flexi flexi8">
														<label for="txt"><s:text name="variety" /></label>
														<div class="form-element">
															<s:select list="{}" headerKey="" name="selectedVariety"
														headerValue="%{getText('txt.select')}" id="variety"
														cssClass="col-sm-3 form-control" onchange="populateGrade(this);"/>
														</div>
													</div>
											<div class="flexi flexi8 hide">
														<label for="txt"><s:text name="grade" /></label>
														<div class="form-element">
														<s:select list="{}" id="gradeList" 
											listKey="name" listValue="gradeCode"  cssClass="col-sm-3 form-control" name="selectedGrade"  />
														</div>
													</div>
											<div class="flexi flexi8">
														<label for="txt" style="display: block; text-align: center"><s:text name="stock" /></label>
														<div class="form-element">
															<s:label id="stockLabel" style="display: block; text-align: center"></s:label>
														</div>
													</div>
											<div class="flexi flexi8">
														<label for="txt"><s:text name="noOfBags" /></label>
														<div class="form-element">
															<s:textfield id="givenBag" maxlength="3" onkeypress="return isNumber(event)" cssStyle="text-align:right;width:100%" />
														</div>
													</div>
													
											<div class="flexi flexi8">
														<label for="txt"><s:text name="NetWeight" /> <span class="manadatory">*</span></label>
														<div class="form-element">
															<s:textfield id="givenGrossWt" maxlength="8" cssStyle="width:100%;" />
														</div>
													</div>
											<div class="flexi flexi8 button-group-container">
												 
													<legend style="padding: 10px 0px 0px 27px; border: none;margin-right : 50px;"><i id="add"
															class="fa fa-check"  aria-hidden="true" title="Add"
															style="cursor: pointer; color: white; background-color: #5cb85c; padding: 5px 10px; font-size: 13px; line-height: 1.5;"></i>
													
															
															</legend><legend style="padding: 10px 0px 0px 27px; border: none;margin-right : -50px;">
															<i onclick="resetProductData()" id="reset_but" class="fa fa-trash" style="cursor: pointer;color: #fff;background-color: #c9302c;border-color: #761c19;
																padding: 5px 10px;font-size: 13px;line-height: 1.5; margin-left: -70px;"
														title="Reset"></i></legend>
													</div> 
											</div>
										</div>
									</div>
									
									
									<div class="appContentWrapper marginBottom">
											<div class="formContainerWrapper">
											
												<div class="flexiWrapper filterControls">
													<table border="1"
												class="table table-bordered aspect-detail"
												id="transferDetailTable">
												<thead>
													<tr>
														<th><s:text name="farmer" /></th>
														<th><s:text name="product" /></th>
														<th><s:text name="variety" /></th>
														<th class="hide"><s:text name="grade" /></th>
														<th><s:text name="stock" /></th>
														<th style="width:10%"><s:text name="noOfBags" /></th>														
														<th style="width:10%"><s:text name="NetWeight" /></th>														
														<th style="text-align: center;width:10%"><s:text name="action" /></th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td colspan="7" style="text-align: center"><s:text
																name="empty.farmerProductTransfer"></s:text></td>
													</tr>

												</tbody>
												<tfoot>
													<tr>
														<th style="text-align: right"></th>
														<th style="text-align: right"></th>
														<th style="text-align: right"></th>
														<th style="text-align: right" class="hide"></th>
														<th style="text-align: right"><s:text name="total" /></th>														
														<th style="text-align: right">
															<div id="noOfBagsTotalLabel">0</div>
														</th>
														<th style="text-align: right">
															<div id="subTotalLabel">0.00</div>
														</th>
														<th></th>
													</tr>

												</tfoot>
											</table>
												</div>
											</div>
									</div>
									
									
									
									
									
								</div>
				</div>
			
			
			
			
			
		</div>
		
			<div class="yui-skin-sam" id="savebutton"><sec:authorize
		ifAllGranted="service.farmerProductTransfer.update">
		
		<span id="save" class=""><span class="first-child">
		<button id="saveBtn" type="button" onclick="submitstockTransfer()" class="save-btn btn btn-success"><font
			color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>
		</span></span>
		
	</sec:authorize> <span class="" style="cursor: pointer;"><span
		class="first-child"><a id="cancelBtnId"
		onclick="reset();" class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
	<s:text name="cancel.button" /> </font> </a></span></span></div>
</s:form>
<%-- <div id="restartAlert" class="popPendingMTNTAlert">
<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
	align="center"></div>
<div id="defaultRestartAlert">	
	<div id="divMsg" align="center"></div>
	<table align="center">
	<tr>
	<td id="closePopup" style="text-align: center"><input id="ok"
			type="button" class="popBtn" value="<s:text name="ok"/>"
			onclick="disablePopupAlert();" /></td>
	</tr>
</table>
</div>
</div> --%>

<button type="button" data-toggle="modal" data-target="#myModal" style="display:none" id="enableModal" class="modal hide" data-backdrop="static" data-keyboard="false">Open Modal</button>

  <!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick="disablePopupAlert()">&times;</button>
          <h4 class="modal-title"><s:text name="create"/></h4>
        </div>
        <div class="modal-body">
          <div id="divMsg" align="center"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" onclick="disablePopupAlert()">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  
<script type="text/javascript">


 document.getElementById("startDate").value='<s:property value="currentDate" />';

</script>
<s:form action="farmerProductTransfer_populatePrintHTML" id="receiptForm" method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
</s:form>
<!-- Include web_transaction-assets.jsp for getting popups and timer related logics -->
<%--<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>


<script>
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
var GRADE_MASTER_LENGTH = <s:property value="gradeMasterList.size()"/>;
var gradeIdString= '<s:property value="gradeMasterIdList"/>';
var globalGradeIdArray=gradeIdString.split('~~');
var transferInfoArray = new Array();
var tArrayList = new Array();
var procurementProductJSON;
try{
	procurementProductJSON = <s:property value="procurementProductJSON" escape="false"/>;
}catch(e){	
}
var gradeInputStr='';
var selectedWarehouse = 0;
jQuery(document).ready(function(){
	jQuery("#stockLabel").html("0 Bags -0.000");
	/* jQuery("#product").change(function(){
		createGrid(this);
	}); */
	populateFarmers($('#farmer'));
	populateProducts($('#product'));
	//resetGradeDetails();
	//jQuery("#product").val('');
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	jQuery("#startDate").val('<s:property value="currentDate" />');
    
    jQuery("#add").click(function(){
		var error = validate();
		jQuery("#validateError").html(error);
		if(error==""){
			var selectedCoOperative=jQuery.trim(jQuery("#coOperative").val());
		
			var selectedFarmer = jQuery.trim(jQuery("#farmer").val());
			var selectedFarmerName = jQuery.trim(jQuery("#farmer option:selected").text());	
			
			var selectedProduct = jQuery.trim(jQuery("#product").val());
			var selectedProductName = jQuery.trim(jQuery("#product option:selected").text());
			
			var selectedVariety = jQuery.trim(jQuery("#variety").val());		
			var selectedVarietyName = jQuery.trim(jQuery("#variety option:selected").text());
			
			var selectedGrade = jQuery.trim($("#gradeList").val());
			//alert(selectedGrade);
			var selectedGradeList = $("#gradeList option:selected").text();
			//alert(selectedGradeList);
			var selectedBag = jQuery.trim(jQuery("#givenBag").val());
			var selectedGrossWt = parseFloat(jQuery.trim(jQuery("#givenGrossWt").val())).toFixed(3);			
			var selectedStock = jQuery("#stockLabel").text();

			if(selectedBag==""){
				selectedBag = 0;
			}

			if(selectedGrossWt==""){
				selectedGrossWt = 0;
			}
			
			var stockInfo = selectedStock.split(" Bags - ");
			var selBag = parseFloat(stockInfo[0]);
			var selectedGrossWeight=parseFloat(stockInfo[1]);		
			//var selectedGradePrice = parseFloat(varietyInfo[1]).toFixed(2);
			//var procurementGradeCodeInfo=selectedGradeList.split("-");
			//var procurementGradeCode= procurementGradeCodeInfo[1];
			//alert(procurementGradeCode);
			
			//var netWeight = getGrossWeight(grossWeightKgEntered, grossWeightGmEntered);
			//var subTotal = getSubTotal(netWeight,selectedGradePrice);
// alert(selectedGrade);
			if(isRowExistsInTable(selectedFarmer,selectedProduct,selectedVariety,selectedGrade)){
				//alert("A");
				for(var cnt=0;cnt<transferInfoArray.length;cnt++){
					if(transferInfoArray[cnt].farmerId==selectedFarmer && transferInfoArray[cnt].productId==selectedProduct && transferInfoArray[cnt].varietyId==selectedVariety && transferInfoArray[cnt].gradeId==selectedGrade){
					
					 if(transferInfoArray[cnt].isEdit){
					//	alert("B");
					
						    transferInfoArray[cnt].farmerId = selectedFarmer;	
						    transferInfoArray[cnt].productId = selectedProduct;
						    transferInfoArray[cnt].varietyId = selectedVariety;
						    transferInfoArray[cnt].gradeId = selectedGrade;
							transferInfoArray[cnt].noOfBags = selectedBag;	
							transferInfoArray[cnt].stock = selectedStock;	
							transferInfoArray[cnt].netWeight = selectedGrossWt							
							transferInfoArray[cnt].isEdit=false;
					
						break;
					}else{
						var totalbag = parseFloat(transferInfoArray[cnt].noOfBags)+parseFloat(selectedBag);
							if(selBag>=totalbag){
								transferInfoArray[cnt].noOfBags = parseFloat(transferInfoArray[cnt].noOfBags)+parseFloat(selectedBag);
							}else{					
								alert('<s:text name="noOfBagGreater"/>');						
								
							}
						var netWeightSum = parseFloat(transferInfoArray[cnt].netWeight) + parseFloat(selectedGrossWt);
						if(selectedGrossWeight>=netWeightSum){
							transferInfoArray[cnt].netWeight = netWeightSum.toFixed(3);
						}else{
							alert('<s:text name="netWeightGreater"/>');							
						} 
						
						//var subTotalSum =parseFloat(transferInfoArray[cnt].subTotal)+ parseFloat(subTotal);
						//transferInfoArray[cnt].subTotal = subTotalSum.toFixed(2);
						transferInfoArray[cnt].isEdit=false;
					}
						
					}
				}				
			}else{
				//alert("D");
				//alert(selectedFarmer +"***"+selectedFarmerName+"###"+selectedProduct+"$$$"+selectedProductName+"!!!"+selectedVariety+"@@@"+selectedVarietyName+"&&&"+selectedStock+"%%%"+selectedBag+"~~~"+selectedGrossWt);
				var transferData = { 	coOperativeId : selectedCoOperative,
										farmerId    :	selectedFarmer,				
										farmerName	  : selectedFarmerName,
										productId   :	selectedProduct,
										productName	  : selectedProductName,
										varietyId   :	selectedVariety,
										varietyName	  : selectedVarietyName,
										gradeId		:	selectedGrade,
										gradeName	: selectedGradeList,
										stock	  	:	selectedStock,
										noOfBags	:	selectedBag,
										netWeight	:	selectedGrossWt,										
										isEdit:false
									  };			
			    
				transferInfoArray[transferInfoArray.length] = transferData;
				//alert(transferData);
			}
			resetSecondTable();
			reloadTable();
			jQuery("#farmer").focus();
		}		
	});
	
    
});



var printWindowCnt=0;
var windowRef;
function printReceipt(receiptNo){	
	jQuery("#receiptNo").val(receiptNo);	
	var targetName="printWindow"+printWindowCnt;
	windowRef = window.open('',targetName);
	try{
		windowRef.referenceWindow = windowRef;		
	}catch(e){
	}
	jQuery("#receiptForm").attr("target",targetName);	
	jQuery("#receiptForm").submit();
	++printWindowCnt;
}

function listProcurementDetails(call){
	
	if(call.value=='-1' || call.value == null || call.value == undefined){
		resetGradeDetails();
	}else{
		$.post("farmerProductTransfer_populateMTNTDetails",{receiptNumber:call.value},function(data){
			if(data == '' || data == null || data == undefined || data < 0 ){
				resetGradeDetails();	
		     }else{
		    	var tripSheetData=data.split('@@');
		    	var tripSheetArr=tripSheetData[0].split('$$');
				jQuery("#mtntDetails").html('<s:text name="stockTransferdate"/>  :  <b>'+tripSheetArr[0]+'</b>  |  <s:text name="area"/>  :  <b>'+tripSheetArr[1] +'</b>  |  <s:text name="truckId"/>  :  <b>'+tripSheetArr[2]+'</b>  |  <s:text name="driverName"/>  :  <b>'+tripSheetArr[3]+'</b>');
				reloadGradeTable(tripSheetData[1]);	 	
			}
			});
	}
}

function submitstockTransfer(){
	try{	
		//alert(transferInfoArray.length==0);
		var error=""
	//		alert(transferInfoArray.length==0);
	var coOperative=$('#coOperative').val();
    var recCoOperative=$('#recCoOperative').val();  
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var date = jQuery.trim(jQuery("#startDate").val());

		var selectedFarmer = jQuery.trim(jQuery("#farmer").val());
		var selectedProduct = jQuery.trim(jQuery("#product").val());		
		var selectedVariety = jQuery.trim(jQuery("#variety").val());
		var gradeId = jQuery.trim(jQuery("#gradeList").val());
		var givenBag = jQuery.trim(jQuery("#givenBag").val());
	
		var givenGrossWt = jQuery.trim(jQuery("#givenGrossWt").val());
		
		var selStock = jQuery("#stockLabel").text();
		
		var stockInfo = selStock.split(" Bags - ");
		var selectedBag = parseFloat(stockInfo[0]);
		var selectedGrossWeight=parseFloat(stockInfo[1]);
		
	if(coOperative=="-1"){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.senderWarehouse")}' />');
		return false;
	}else if(recCoOperative=="-1"){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.receiverWarehouse")}' />');
		return false;
	}else if(truckId.trim()==""){
    	 jQuery("#validateError").text('<s:text name="empty.truckId"/>');
         return false;
     }else if(isValidAlphaNumericWithSpaceValue(truckId.trim())==false){
    	 jQuery("#validateError").text('<s:text name="invalid.truckId"/>');
         return false;
     }else if(driverId.trim()==""){
    	 jQuery("#validateError").text('<s:text name="empty.driverName"/>');
         return false;
     }else if(isValidAlphaNumericWithSpaceValue(driverId.trim())==false){
    	 jQuery("#validateError").text('<s:text name="invalid.driverName"/>');
         return false;
     }else if(recCoOperative==""){
 		 jQuery("#validateError").text('<s:text name="recCoOperative.empty"/>');
         return false;
 	 }else if(recCoOperative==coOperative){
		 jQuery("#validateError").text('<s:text name="sameCoOperative"/>');
        return false;
	 }
	 else if(transferInfoArray.length==0){
			 jQuery("#validateError").text('<s:text name="empty.farmerProductTransfer"/>');
			// error = '<s:text name="empty.farmerProductTransfer"/>'
		      return false;
			 /* error = '<s:text name="empty.farmerProductTransfer"/>'; 
			 jQuery("#farmer").focus();	*/
		}
	jQuery("#validateError").html(error);
	if(error==""){
		
		$("#saveBtn").prop("disabled",true);
		buildTransferDetailRequest();	
			jQuery("#gradeInputString").val(tArrayList);	
			
		//alert(coOperative +" *** "+ recCoOperative +" *** "+ date+" *** "+ truckId+" *** "+ driverId+" *** "+ tArrayList);
		 $.post("farmerProductTransfer_populateProductTransfer.action",{selectedCoOperative:coOperative,selectedRecCoOperative:recCoOperative,startDate:date,truckId:truckId,driverId:driverId,gradeInputString:jQuery("#gradeInputString").val()},function(data){ 		
		
			if(data == null || data == ""){		
			$("#saveBtn").prop("disabled",false);
			}
		else{ 	 			
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();
			$("#saveBtn").prop("disabled",false);
			reloadTable();			
			reset();
		}
		});	
		 reloadTable();
	  }
	
	}catch(er){
		console.log(er);
	}
 }

function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}

function isValidAlphaNumericWithSpaceValue(val){
	 var numberReg = /^[A-Za-z0-9 ]+$/;
	 var result = true;
	 var regExVal = val.match(numberReg);
	 if(regExVal=='' || regExVal==null || regExVal== undefined ){
	  result = false;
	 }
	 return result;
}
function populateFarmers(call){
	
	$.post("farmerProductTransfer_populateFarmers",{selectedCoOperative:$(call).val()},function(result){
		var rrr=result.replace(/((\[)|(\]))/g,'');
		var options = rrr.split(",");
		$('#farmer').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');
		//addOption(document.getElementById("farmer"),'<s:text name="txt.select"/>','<s:text name="txt.select"/>');
		for(var i=0;i<options.length;i++){
			var option = options[i];
			if(jQuery.trim(option)!=""){
				var optionVal = jQuery.trim(option).split(" - ");
				var id = optionVal[2];
				var val = optionVal[0];
				var name = optionVal[0]+" - "+optionVal[1];
                $("#farmer").append("<option value='"+id+"'>"+name+"</option>");
			}
		}
	});
	
} 
function populateProducts(call){
	
	jQuery.post("farmerProductTransfer_populateProducts1",{selectedFarmer:$("#farmer").val(),selectedCoOperative:$("#coOperative").val()},function(result){
		var rrr=result.replace(/((\[)|(\]))/g,'');
		var options = rrr.split(",");
		$('#product').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');
		for(var i=0;i<options.length;i++){
			var option = options[i];
			if(jQuery.trim(option)!=""){
				var optionVal = jQuery.trim(option).split(" - ");
				var id = optionVal[2];
				var val = optionVal[0];
				var name = optionVal[0]+" - "+optionVal[1];
                $("#product").append("<option value='"+id+"'>"+name+"</option>");
			}
		}
	});
	//createGrid(jQuery("#product").val());

}
function populateVariety(call){	
	
	jQuery.post("farmerProductTransfer_populateVariety",{selectedFarmer:$("#farmer").val(),selectedCoOperative:$("#coOperative").val(),selectedProduct:$("#product").val()},function(data){			
			 var result=data;			
			 result = result.replace("{","").replace("}","");		
			 var farmersArr =result.split(",");		
				$('#variety').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');
			 for (var i=0; i < farmersArr.length;i++){
				 var arr = farmersArr[i].split("=");
				 
				 if(arr!="")
				 {//alert(arr[1]+"*****"+arr[0]);
				 addOption(document.getElementById("variety"), arr[1], arr[0]);
				 }
				 	
			}
				
	    });
	
	
}

function enablePopupAlert(){
	
	$('body').css('overflow','hidden');
	$('#pendingRestartAlertErrMsg').html('');
	$('#popupBackground').addClass('loginPanelContent')
	$('#popupBackground').css('width','100%');
	$('#popupBackground').css('height',getWindowHeight());
	$('#popupBackground').css('top','0');
	$('#popupBackground').css('left','0');
	$('#popupBackground').show();
	$('#restartAlert').css({top:'50%',left:'50%',margin:'-'+($('#restartAlert').height() / 2)+'px 0 0 -'+($('#restartAlert').width() / 2)+'px'});
	$('#restartAlert').show();

	window.location.hash="#restartAlert";
}

function disablePopupAlert(){
	
	$('#pendingRestartAlertErrMsg').html('');
	$('#popupBackground').hide();
	$('#restartAlert').hide();
	$('body').css('overflow','');
	reset();
	$.post("farmerProductTransfer_create.action",{procurementInfo:""},function(data){
 	});
 
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}
function reset(){
	var idsArray=['coOperative','recCoOperative','product'];
	resetSelect2(idsArray);
	//document.getElementById("coOperative").selectedIndex=0;
	//document.getElementById("recCoOperative").selectedIndex=0;
	//document.getElementById("product").selectedIndex=0;
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	gradeInputStr='';
//	createGrid(jQuery("#product").val());
	jQuery("#validateError").text('');
	jQuery("#stockTransferInfo").show();
	populateFarmers($('#farmer'));
	populateProducts($('#product'));
	populateVariety($('#variety'));
	//document.getElementById("farmer").selectedIndex=0;
	document.getElementById("product").selectedIndex=0;
	document.getElementById("variety").selectedIndex=0;
	jQuery("#stockLabel").html("0 Bags -0.000");
	resetSecondTable();
	transferInfoArray = new Array();
	tArrayList = new Array();
	reloadTable();
}
function resetTableDetails(){
	
	//document.getElementById("recCoOperative").selectedIndex=0;
	//document.getElementById("product").selectedIndex=0;
	//jQuery("#truckId").val('');
	//jQuery("#driverId").val('');
	gradeInputStr='';
	//createGrid(jQuery("#product").val());
	jQuery("#validateError").text('');
	$('#variety').empty();
	 $('#variety').append('<option value="">Select</option');	
	//document.getElementById("variety").selectedIndex=0;
	jQuery("#stockLabel").html("0 Bags -0.000");
	jQuery("#stockTransferInfo").show();
	reloadTable();
	
}
function resetVariety(){
	$('#variety').empty();
	 $('#variety').append('<option value="">Select</option');	
	
	jQuery("#stockLabel").html("0 Bags -0.000");	
}

function getStock(){
	var selgrad = $("#gradeList").val();
 	//alert("GR: "+$("#gradeList").val());
		 jQuery.post("farmerProductTransfer_populateStocks", {selectedCoOperative:$("#coOperative").val(),selectedFarmer:$("#farmer").val(),selectedProduct:$("#product").val(),selectedGrade:$("#gradeList").val()}, function (data) {   
		   		
				   document.getElementById("stockLabel").value = "";
				 
					 jQuery("#stockLabel").html(data);
			   
		   });
		   
}
function validate(){
var error="";
	var selectedFarmer = jQuery.trim(jQuery("#farmer").val());
	var selectedProduct = jQuery.trim(jQuery("#product").val());		
	var selectedVariety = jQuery.trim(jQuery("#variety").val());
	var gradeId = jQuery.trim(jQuery("#gradeList").val());
	var givenBag = jQuery.trim(jQuery("#givenBag").val());

	var givenGrossWt = jQuery.trim(jQuery("#givenGrossWt").val());
	
	var selStock = jQuery("#stockLabel").text();
	
	var stockInfo = selStock.split(" Bags - ");
	var selectedBag = parseFloat(stockInfo[0]);
	var selectedGrossWeight=parseFloat(stockInfo[1]);
	
	if(selectedFarmer==""){
		error = '<s:text name="empty.farmer"/>';
		jQuery("#farmer").focus();
	}else if(selectedProduct==""){
		error = '<s:text name="empty.product"/>';
		jQuery("#product").focus();
	}else if(selectedVariety==""){
		error = '<s:text name="empty.variety"/>';
		jQuery("#variety").focus();
	} 
	 else if(givenGrossWt=="" && (isNaN(givenGrossWt) || isNaN(parseFloat(givenGrossWt)))){
		error='<s:text name="invalidWtFor"/>';
		jQuery("#givenGrossWt").focus();
	}
	//No of bags Entered But Gross Weight Empty
	else if(givenBag!="" && parseInt(givenBag)>0 &&  givenGrossWt==""){
		error='<s:text name="emptyWtFor"/>';
		jQuery("#givenGrossWt").focus();
	}
	else if(givenBag!="" && givenGrossWt !="" && parseInt(givenBag)!=0 && parseFloat(givenBag)>=parseFloat(givenGrossWt)){
		error='<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />';
		jQuery("#givenBag").focus();
	}else if(givenGrossWt > selectedGrossWeight){
		
		error='<s:text name="greaterGrassWeight"/>';
		jQuery("#givenGrossWt").focus();
	}
	else if(givenBag==""){
		givenBag=0;
	}
	else if(givenGrossWt==""){
		givenGrossWt=0;
	}else if(givenBag > selectedBag){
		error='<s:text name="greaterNoOfBags"/>';
		jQuery("#givenBag").focus();
	}
	
		return error; 
	}
	
		
function isRowExistsInTable(farmerId,productId,varietyId,gradeId){
	//alert(farmerId+" *** "+productId+" *** "+varietyId+" *** "+gradeId);	
	var result = false;
	for(var cnt=0;cnt<transferInfoArray.length;cnt++){
		
		if(transferInfoArray[cnt].farmerId==farmerId && transferInfoArray[cnt].productId==productId && transferInfoArray[cnt].varietyId==varietyId && transferInfoArray[cnt].gradeId==gradeId){
			//alert("MMM");		
			result = true;
			break;
		}
	}
	
	return result;
}
function reloadTable(){
	buildTableBody();
	buildTableFooter();
}
function buildTableBody(){

	var tbodyRow = "";
	var tfootArray = new Array();	
	jQuery('#transferDetailTable > tbody').html('');	
	if(transferInfoArray.length==0){				
		tbodyRow += '<tr>'+
					'<td colspan=\'7"/>\' style="text-align:center"><s:text name="empty.farmerProductTransfer"/></td>'+
					'</tr>';
	}else{
		
		for(var cnt=0;cnt<transferInfoArray.length;cnt++){
			
			tbodyRow += '<tr>'+
							'<td style="text-align:left;">'+transferInfoArray[cnt].farmerName+'</td>'+
							'<td style="text-align:left;font-weight:normal;">'+transferInfoArray[cnt].productName+'</td>'+
							'<td style="text-align:left;font-weight:normal;">'+transferInfoArray[cnt].varietyName+'</td>'+
							'<td style="text-align:left;font-weight:normal;"class="hide">'+transferInfoArray[cnt].gradeName+'</td>'+
							'<td style="text-align:left;font-weight:normal;">'+transferInfoArray[cnt].stock+'</td>'+
							'<td style="text-align:right;font-weight:normal;" class="noOfBags">'+transferInfoArray[cnt].noOfBags+'</td>'+							
							'<td style="text-align:right;" class="price">'+transferInfoArray[cnt].netWeight+'</td>'+							
							'<td colspan="2" class="alignCenter borNone" style="border:none!important;"><legend style="display: inherit!important;"><button type="button" class="btn btn-sm btn-primary" aria-hidden="true" onclick="updateRow('+cnt+')" title="<s:text name="Edit" />"><i class="fa fa-pencil"></i></button></legend>'+
							'<legend style="display: inherit!important;"><button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="removeRow('+cnt+')" title="<s:text name="Delete" />"><i class="fa fa-trash"></i></button></legend></td>'+
							'</tr>';
				  		    
		}
	}
	jQuery('#transferDetailTable > tbody').html(tbodyRow);				
}
function buildTableFooter(){		
	var tfootRow = "";		
	jQuery('#transferDetailTable > tfoot').html('');	
	
	var noOfBagsTotal = 0;
	var priceTotal = 0;
	var netWeighttotal = 0;
	var subTotal = 0;
	
	jQuery(".noOfBags").each(function() {
		noOfBagsTotal = noOfBagsTotal+parseInt(jQuery(this).text());
	});

	jQuery(".price").each(function() {
		priceTotal = (parseFloat(priceTotal)+parseFloat(jQuery(this).text())).toFixed(3);
	});
	

	tfootRow +='<tr>'+
				'<th style="text-align:right;"></th>'+
				'<th style="text-align:right;"></th>'+
				'<th style="text-align:right;"></th>'+
				'<th style="text-align:right;"><s:text name="total"/></th>'+
			 	'<th style="text-align:right;">'+noOfBagsTotal+'</th>'+
				'<th style="text-align:right;">'+parseFloat(priceTotal).toFixed(3)+'</th>'+			
				'<th></th>'+
			    '</tr>';
	jQuery('#transferDetailTable > tfoot').html(tfootRow);
}
function resetSecondTable(){	
	//restButtton(true);
	//document.getElementById("product").disabled = false;
	//document.getElementById("product").selectedIndex="";
    
   $('#product').empty();	
    $('#product').append('<option value="">Select</option');	
    
    $('#variety').empty();	
    $('#variety').append('<option value="">Select</option');
    
    document.getElementById("farmer").disabled = false;
	document.getElementById("farmer").selectedIndex="";
	
	document.getElementById("product").disabled = false;
	document.getElementById("product").selectedIndex="";
	document.getElementById("variety").disabled = false;
	document.getElementById("variety").selectedIndex=""; 
	document.getElementById("gradeList").selectedIndex="";
	//document.getElementById("gradeList option:selected").remove();
	jQuery("#givenBag").val("");
	jQuery("#givenGrossWt").val("");
	jQuery("#stockLabel").html("0 Bags -0.000");
}
function updateRow(index){
	//checkFarmer(transferInfoArray[index].farmerId,index);
	jQuery("#farmer").val(transferInfoArray[index].farmerId);
	document.getElementById("farmer").disabled = true;
	//alert(transferInfoArray[index].productId);
	
	checkProduct(transferInfoArray[index].productId,index);
	jQuery("#product").val(transferInfoArray[index].productId);
	document.getElementById("product").disabled = true;
	
    checkVariety(transferInfoArray[index].varietyId,index);
	jQuery("#variety").val(transferInfoArray[index].varietyId);
	document.getElementById("variety").disabled = true;
	
	//var selectedGrade = jQuery.trim($("#gradeList option").eq(1).val());
	jQuery("#gradeList option").eq(1).val(transferInfoArray[index].gradeId);
	document.getElementById("gradeList").disabled = true;
	
	
	jQuery("#stockLabel").html(transferInfoArray[index].stock);
	jQuery("#givenBag").val(transferInfoArray[index].noOfBags);	
	jQuery("#givenGrossWt").val(transferInfoArray[index].netWeight);
	
	transferInfoArray[index].isEdit=true;

}
function checkFarmer(obj,indx){
	var selectedFarmer = transferInfoArray[indx].farmerId;	
	 jQuery.post("farmerProductTransfer_populateEditFarmer.action",{selectedFarmer:obj},function(result){	
		insertOptions("farmer",JSON.parse(result));			
		jQuery('#farmer').val(transferInfoArray[indx].farmerId);
	
	});
	
}
function checkProduct(obj,indx)
{
	var selectedProduct = transferInfoArray[indx].productId;	
	 jQuery.post("farmerProductTransfer_populateEditProduct.action",{selectedProduct:obj},function(result){			 
		insertOptionss("product",JSON.parse(result));	
		jQuery('#product').val(transferInfoArray[indx].productId);
	
	});
	
}

function insertOptionss(ctrlName, jsonArr) {
	var id="#"+ctrlName;	
	
	document.getElementById(ctrlName).length = 0;
	addOption(document.getElementById(ctrlName), "Select", "0");
	for (var i = 0; i < jsonArr.length; i++) {
		addOption(document.getElementById(ctrlName), jsonArr[i].name,
				jsonArr[i].id);
	}
}

function checkVariety(obj,indx)
{
	var selectedVariety = transferInfoArray[indx].varietyId;
	
	 jQuery.post("farmerProductTransfer_populateEditVariety.action",{selectedVariety:obj},function(result){	
		insertOptionss("variety",JSON.parse(result));	
		jQuery('#variety').val(transferInfoArray[indx].varietyId);
	
	});
	
}
function populateGrade(call){
	 $('#gradeList').empty();
   jQuery.post("farmerProductTransfer_populateGrade",{selectedCoOperative:$("#coOperative").val(),selectedFarmer:$("#farmer").val(),selectedProduct:$("#product").val(),selectedVariety:$("#variety").val(),}, function (data) {
		var result=data;
	
		
	 	 result = result.replace("{","").replace("}","");
		var farmersArr =result.split(",");	
		
		//addOption(document.getElementById("gradeList"), '<s:text name="txt.select"/>', "-1"); 
		for (var i=0; i < farmersArr.length;i++){
			 var arr = farmersArr[i].split("=");
		
			 if(arr!="")
			 {		 
				 addOption(document.getElementById("gradeList"), arr[1], arr[0]);
				
			 }
			
		}
		
		getStock();
	
	    });
	
}

 function buildTransferDetailRequest(){
	tArrayList = new Array();
	var record = "";
	
	var transInfo= "";	
	for(var cnt=0;cnt<transferInfoArray.length;cnt++){
		
		transInfo=transInfo+transferInfoArray[cnt].farmerId+"-"+transferInfoArray[cnt].productId+"-"+transferInfoArray[cnt].gradeId+"-"+transferInfoArray[cnt].noOfBags+"-"+transferInfoArray[cnt].netWeight;
		//alert(transInfo);	
		if((cnt+1)!=transferInfoArray.length){
			transInfo+="|";
		}
	}
	var record = transInfo
	//alert(record);
	tArrayList[tArrayList.length] = record;
} 
 function removeRow(indx){	
		alert('<s:text name="delete.rowGrid"/>');
		if(transferInfoArray.length>0){				
			transferInfoArray.splice(indx,1);		
		}	
		reloadTable();
		resetSecondTable();
	}
 
 function resetProductData(){
	 reloadTable();
		resetSecondTable();
		
 }
 function resetWareHouse(){
	 
	 if(transferInfoArray.length>0){
		 alert('<s:text name="warehouse.exists"/>');
		 reloadTable();
			resetSecondTable();
			reset();
	 }
 }
function resetGrade(){
	 $('#gradeList').empty();
}



</script>
</body>