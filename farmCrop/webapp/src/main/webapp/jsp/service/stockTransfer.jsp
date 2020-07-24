<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
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

.column-1 h3, .column-2 h3, .column-3 h3 {
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

.column-2, .column-3 {
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
	margin-bottom: 0px !important;
}
</style>

</head>
<body>

	<s:form id="stockTransferForm" action="stockTransfer_create"
		method="post" cssClass="fillform">
		<s:hidden name="agentId" />
		<s:hidden id="gradeInputString" name="gradeInputString" />

		<font color="red"> <s:actionerror /></font>
		<div>


			<div class="appContentWrapper marginBottom ">
				<div class="formContainerWrapper">
					<div class="row">
						<div class="container-fluid">
							<div class="notificationBar">
								<div class="error">
									<p class="notification">
										<%-- <span class="manadatory">*</span>
										<s:text name="reqd.field" /> --%>
									<div id="validateError" style="text-align: center;"></div>
									<div id="validatePriceError" style="text-align: center;"></div>
									</p>
								</div>
                                </div>
                                    </div>

							</div>
							<h2>
								<s:text name="info.general" />
							</h2>
							<div class="flexiWrapper filterControls">
								<s:if test="getCurrentTenantId()=='lalteer'">
									<div class="flexi flexi8">
										<label for="txt"><s:text name="sender" /> <span
											class="manadatory">*</span></label>
										<div class="form-element">
											<s:select name="selectedCoOperative" list="listWarehouse"
												cssClass="col-sm-3 form-control select2" headerKey="-1"
												headerValue="%{getText('txt.select')}" listKey="key"
												listValue="value" id="coOperative"
												onchange="populateFarmers(this);resetTableDetails();" />
										</div>
									</div>
								</s:if>
								<s:else>
									<div class="flexi flexi8">
										<label for="txt"><s:property value="%{getLocaleProperty('sender')}"/>	 <span
											class="manadatory">*</span></label>
										<div class="form-element">
											<s:select name="selectedCoOperative" list="listWarehouse"
												cssClass="col-sm-3 form-control select2" headerKey=""
												headerValue="%{getText('txt.select')}" listKey="key"
												listValue="value" id="coOperative"
												onchange="populateProducts(this);resetTableDetails();" />
										</div>
									</div>
								</s:else>
								<div class="flexi flexi8">
									<label for="txt"><s:property value="%{getLocaleProperty('receiver')}"/>	 <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select name="selectedRecCoOperative" list="listWarehouse"
											cssClass="col-sm-3 form-control select2" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" id="recCoOperative" />
									</div>
								</div>
								<s:if test="getCurrentTenantId()=='lalteer'">
									<div class="flexi flexi8">
										<label for="txt"><s:text name="farmer" /> <span
											class="manadatory">*</span></label>
										<div class="form-element">
											<s:select id="farmer" headerKey=""
												headerValue="%{getText('txt.select')}" name="selectedFarmer"
												list="farmerMap" cssClass="col-sm-3 form-control select2"
												onchange="populateProducts(this);resetTableDetails();" />
										</div>
									</div>
								</s:if>
								<div class="flexi flexi8">
									<label for="txt"><s:text name="product" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select id="product" headerKey=""
											headerValue="%{getText('txt.select')}" name="selectedProduct"
											list="productMap" cssClass="col-sm-3 form-control select2" />
									</div>
								</div>
								<div class="flexi flexi8">
									<label for="txt"><s:text name="stockTransferdate" /> </label>
									<div class="form-element">
										<s:textfield name="startDate" id="startDate" readonly="true"
											theme="simple"
											data-date-format="%{getGeneralDateFormat().toLowerCase()}"
											data-date-viewmode="years"
											cssClass="date-picker form-control col-sm-3" />
									</div>
								</div>
								<div class="flexi flexi8">
									<label for="txt"><s:property value="%{getLocaleProperty('truckId')}"/> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="truckId" id="truckId" maxlength="35"
											cssClass="form-control" />
									</div>
								</div>
								<div class="flexi flexi8">
									<label for="txt"><s:text name="driverId" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="driverId" id="driverId" maxlength="35"
											cssClass="form-control" />
									</div>
								</div>

							</div>
						</div>
					</div>

					<div class="service-content-wrapper">
						<div class="service-content-section">

							<div class="appContentWrapper marginBottom">
								<div class="formContainerWrapper">
									<h2>
										<s:text name="info.stockTransferDetails" />
									</h2>
									<div class="flexiWrapper filterControls">
										<div id="stockTransferInfo"
											style="text-align: center; height: 20px; width: 100%;">
											<s:text name="stockTransfer.select.info" />
										</div>

										<div style="width: 98%;" id="baseDiv">
											<table id="detail" style="width: auto !Important;"></table>
										</div>

									</div>
									<div class="yui-skin-sam" id="savebutton">
										<sec:authorize ifAllGranted="service.stockTransfer.update">

											<span id="save" class=""><span class="first-child">
													<button id="saveBtn" type="button"
														onclick="submitstockTransfer()"
														class="save-btn btn btn-success">
														<font color="#FFFFFF"> <b><s:text
																	name="save.button" /></b>
														</font>
													</button>
											</span></span>

										</sec:authorize>
										<span class="" style="cursor: pointer;"><span
											class="first-child"><a id="cancelBtnId"
												onclick="reset();" class="cancel-btn btn btn-sts"> <font
													color="#FFFFFF"> <s:text name="cancel.button" />
												</font>
											</a></span> </span>
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>
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
</div>  --%>

	<button type="button" data-toggle="modal" data-target="#slideModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>



<div id="slideModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="contentHdr">
				<button type="button" class="close" data-dismiss="modal">&times;</button>			
			<div class="modal-body">
				<i class="fa fa-check" aria-hidden="true"></i></br>
            <span><h5>Product Transfer has been Done Successfully</h5></span>
				<div id="divMsg" align="center"></div>
			</div>
			</div>
			<div class="contentFtr">
			<%-- <button type="button" class="btn btn-danger btnBorderRadius" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button> --%>
				<button type="button" class="btn btn-success btnBorderRadius" data-dismiss="modal"
					onclick="distributionEnable()">
					<s:text name="Continue" />
				</button>
			</div>
		</div>
	</div>

</div>

	<s:form action="stockTransfer_populatePrintHTML" id="receiptForm"
		method="POST" target="printWindow">
		<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
	</s:form>
	<!-- Include web_transaction-assets.jsp for getting popups and timer related logics -->
	<%--<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>


<script>
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
var GRADE_MASTER_LENGTH = <s:property value="gradeMasterList.size()"/>;
var gradeIdString= '<s:property value="gradeMasterIdList"/>';
var globalGradeIdArray=gradeIdString.split('~~');
var procurementProductJSON;
try{
	procurementProductJSON = <s:property value="procurementProductJSON" escape="false"/>;
}catch(e){
}
var gradeInputStr='';

$("#coOperative").val("");
$("#recCoOperative").val("");


jQuery(document).ready(function(){
	jQuery("#product").change(function(){
		createGrid(this);
	});
	
	resetGradeDetails();
	jQuery("#product").val('');
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
//	jQuery("#startDate").val('<s:property value="currentDate" />');
	
	 $('#startDate').datepicker('setDate', new Date());
	// Here the timer starts for stockTransfer action page. 
	// "stockTransfer" is the action url 
	//startTimer("stockTransfer");
	<s:if test='stockTransferDescription!=null'>
	jQuery('<tr><td><s:property value="stockTransferDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
	jQuery("#restartAlert").css('height','150px');
    </s:if>

    populateProducts($('#coOperative'));
    
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


function createGrid(product){
  	
	try {
  		jQuery("#detail").empty();
	  jQuery("#detail").jqGrid('GridUnload');          
    }catch(e){}
	globalGradeIdArray=new Array();
	if(product!=""){
		jQuery("#stockTransferInfo").hide();
		var agentIdx='<%=request.getParameter("agentId")%>';	
		jQuery.post("stockTransfer_populateStock",{selectedCoOperative:$("#coOperative").val(),selectedProduct:$('#product').val()},function(result){
			console.log($.parseJSON(result));
			var procurementProductJSON=$.parseJSON(result);
			for(var count=0;count<procurementProductJSON.products.length;count++){
				if(procurementProductJSON.products[count].id==$(product).val()){	
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					
					var procurementGrades = procurementProductJSON.products[count].procurementGrades;

					for(var i=0;i<procurementGrades.length;i++){

						globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;

						var row = new Object();
						row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="gradeVarietyIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].id+'</div><div id="gradeVarietyNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						row['grade'] =  procurementGrades[i].name+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].id+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].name+'</div>';
						row['unit'] = '<label id="unit'+(procurementGrades[i].id)+'" style="padding-right:1px; width:150px!important;">'+(procurementGrades[i].unit)+'</label>'
						if(tenant!="pratibha"){
						row['stock'] ='<label id="lblStock'+(procurementGrades[i].id)+'" style="padding-right:1px; width:150px!important;">'+(procurementGrades[i].numberOfBags)+' <s:property value="%{getLocaleProperty('noofBags')}"/> - '+(procurementGrades[i].grossWeight)+'</label>'
						row['noOfBags'] ='<input id="quantityTB'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalQuantity();" onkeypress="return isNumber(event)" style="text-align:right;" value="0" maxlength="6" name="" class="col-sm-3 form-control">'
						}
						else{
							row['stock'] ='<label id="lblStock'+(procurementGrades[i].id)+'" style="padding-right:1px; width:150px!important;">'+(procurementGrades[i].grossWeight)+'</label>'
						}
						row['netWeight'] = '<input id="grossWeightTB'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalGrossWeight();" onkeypress="return isDecimal(event)" style="text-align:right;padding-right:1px;" value="0.000" maxlength="10" name="" class="col-sm-3 form-control">'
										  
						rowData[rowData.length] = row;
					}
					
					globalGradeIdArray[globalGradeIdArray.length] = '';
					if(tenant!="pratibha"){	
					gridColumnNames  = ['<s:text name="procurementvariety"/>','<s:text name="procurementgrade"/>','<s:text name="unit" />','<s:text name="stock" />','<s:property value="%{getLocaleProperty('noofBags')}"/>','<s:text name="grossWeight" /><sup style="color: red;">*</sup>']
					}
					else{
						gridColumnNames  = ['<s:text name="procurementvariety"/>','<s:text name="procurementgrade"/>','<s:text name="unit" />','<s:text name="stock" />','<s:property value="%{getLocaleProperty('grossWeight')}"/><sup style="color: red;">*</sup>']
					}
					    				gridColumnModels = [{name:'procurementVarietyName',index:'procurementVarietyName',sortable:false, width: 100,resizable: false},
					    				    				{name:'grade',index:'grade',sortable:false, width: 100,resizable: false},
					    				    				{name:'unit',index:'unit',sortable:false, width: 100,resizable: false},
					    				    				{name:'stock',index:'stock',sortable:false, width: 150, align:'center',resizable: false},
					    				    				 <s:if test="currentTenantId!='pratibha'">
					    				    				{name:'noOfBags',index:'noOfBags',sortable:false, width: 100, align:'center',resizable: false},
					    				    				</s:if>
					    				    				{name:'netWeight',index:'netWeight',sortable:false, width: 100, align:'center',resizable: false}]									    					


					footerRow = {stock:'<s:text name="total"/>',noOfBags:'<div id="totalQtyFinal">0</div>',netWeight:'<center><div id="totalGrossWtFinal">0.000</div></center>'};
					    				
					jQuery("#detail").jqGrid(
					{		   		
					   	colNames:gridColumnNames,
					   	colModel:gridColumnModels,
					   	width: $("#baseDiv").width(),
					   	footerrow: true,
					   	height:'auto'
				    });

					for(var i=0;i<rowData.length;i++)
						jQuery("#detail").jqGrid('addRowData',(i+1),rowData[i]);
					
			    	jQuery("#detail").jqGrid('footerData' , 'set' , footerRow);

			    	calculateTotalQuantity();
			    	calculateTotalGrossWeight();
					break;
				}
			}
		});

		
			
	}else{
		jQuery("#stockTransferInfo").show();
	}	
}

function listProcurementDetails(call){
	
	if(call.value=='-1' || call.value == null || call.value == undefined){
		resetGradeDetails();
	}else{
		$.post("stockTransfer_populateMTNTDetails",{receiptNumber:call.value},function(data){
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

function reloadGradeTable(gradeData){
    if(!(gradeData=='' || gradeData==null)){	
	var gradeDataArr=gradeData.split('||');
	var gradeIdDataArr=gradeDataArr[0].split('~~');
	var noOfBagsDataArr=gradeDataArr[1].split('~~');
	var grossWtArr=gradeDataArr[2].split('~~');

	 for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
		   jQuery("#qty"+globalGradeIdArray[cnt]).html('0');
		   jQuery("#grossWt"+globalGradeIdArray[cnt]).html('0.000');
		   jQuery("#quantityTB"+globalGradeIdArray[cnt]).removeAttr('disabled');
		   jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).removeAttr('disabled');
		   jQuery("#quantityTB"+globalGradeIdArray[cnt]).val('0');
		   jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).val('0.000');
	   }

	   var totalQty=0;
	   var totalGrossWt=0.000;
	   for(var cnt=0;cnt<gradeIdDataArr.length-1;cnt++){
		   jQuery("#qty"+gradeIdDataArr[cnt]).html(noOfBagsDataArr[cnt].toString());
		   jQuery("#grossWt"+gradeIdDataArr[cnt]).html(parseFloat(grossWtArr[cnt]).toFixed(3));
		   jQuery("#quantityTB"+gradeIdDataArr[cnt]).val(parseInt(noOfBagsDataArr[cnt]));
		   jQuery("#grossWeightTB"+gradeIdDataArr[cnt]).val(parseFloat(grossWtArr[cnt]).toFixed(3));
		   totalQty+=parseInt(noOfBagsDataArr[cnt]);
		   totalGrossWt+=parseFloat(grossWtArr[cnt]);
	   }

	   jQuery("#totalQtyInfo").html(totalQty.toString());
	   jQuery("#totalGrossWtInfo").html(totalGrossWt.toFixed(3));
	   jQuery("#totalQtyFinal").html(totalQty.toString());
	   jQuery("#totalGrossWtFinal").html(totalGrossWt.toFixed(3));	    
    } 
}

function calculateTotalQuantity(){
	   var totalQuantity=0;
       for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
           var qty=jQuery("#quantityTB"+globalGradeIdArray[cnt]).val();
           if(!isNaN(qty) && qty.trim()!='')
    	   totalQuantity+=parseInt(qty);
       }
       jQuery("#totalQtyFinal").text(totalQuantity);
}

function calculateTotalGrossWeight(){
	var weight=0;
    for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
        var wt=jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).val();
        if(!isNaN(wt) && wt.trim()!='')
        	weight+=parseFloat(wt);
    }
    jQuery("#totalGrossWtFinal").text(weight.toFixed(3));
}

function resetGradeDetails(){
	jQuery("#mtntDetails").html('');
	for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
		   jQuery("#qty"+globalGradeIdArray[cnt]).html('0');
		   jQuery("#grossWt"+globalGradeIdArray[cnt]).text('0.000');
		   jQuery("#quantityTB"+globalGradeIdArray[cnt]).val('0');
		   jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).val('0.000');
		   //jQuery("#quantityTB"+globalGradeIdArray[cnt]).attr('disabled','disabled');
		  // jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).attr('disabled','disabled');
	 }
	jQuery("#totalQtyInfo").html('0');
	jQuery("#totalGrossWtInfo").html('0.000');
    jQuery("#totalQtyFinal").html('0');
	jQuery("#totalGrossWtFinal").html('0.000');
}

function validateData(){
	var product=jQuery("#product").val();
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var coOperative=$('#coOperative').val();
    var recCoOperative=$('#recCoOperative').val();

    if(coOperative==""){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.senderWarehouse")}' />');
		return false;
	}
    if(recCoOperative=="" ){
    	
    	jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.receiverWarehouse")}' />');
		return false;
	}
	
	if(product==""){
		jQuery("#validateError").text('<s:text name="empty.product"/>');
		return false;
	}
     if(truckId.trim()==""){
    	 jQuery("#validateError").text('<s:text name="empty.truckId"/>');
         return false;
     }
     if(isValidAlphaNumericWithSpaceValue(truckId.trim())==false){
    	 jQuery("#validateError").text('<s:text name="invalid.truckId"/>');
         return false;
     }
     if(driverId.trim()==""){
    	 jQuery("#validateError").text('<s:text name="empty.driverName"/>');
         return false;
     }
     if(isValidAlphaNumericWithSpaceValue(driverId.trim())==false){
    	 jQuery("#validateError").text('<s:text name="invalid.driverName"/>');
         return false;
     }

 	 if(globalGradeIdArray.length==0){
 		 jQuery("#validateError").text('<s:text name="grades.not.exists"/>');
         return false;
 	 }

 	 if(recCoOperative==""){
 		 jQuery("#validateError").text('<s:text name="recCoOperative.empty"/>');
         return false;
 	 }
 	 if(recCoOperative==coOperative){
		 jQuery("#validateError").text('<s:text name="sameCoOperative"/>');
        return false;
	 }
	    gradeInputStr='';
		for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
		   var gradeId=jQuery("#gradeIdHeader"+globalGradeIdArray[cnt]).html();
		   var grade=jQuery("#gradeNameHeader"+globalGradeIdArray[cnt]).html();		   
		   if(tenant!='pratibha'){
		   var givenQty=jQuery("#quantityTB"+globalGradeIdArray[cnt]).val().trim();
		   }
		   var givenGrossWt=jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).val().trim();
		   var wgtLbl=$("#lblStock"+globalGradeIdArray[cnt]).text();
		   if(tenant!='pratibha'){
		   var arr=wgtLbl.split(" Bags - ");
		   
		   var exnoOfBags=parseFloat(arr[0]);
		   var exWeight=parseFloat(arr[1]);
		   }else{
			   var exnoOfBags=wgtLbl;
		   }
		 /*   console.log(exnoOfBags+" x "+exWeight);
		   console.log(givenQty+" y "+givenGrossWt);
 */			 //Not a number Validation
			   if(tenant!='pratibha'){
			if(givenQty!='' && (isNaN(givenQty) || isNaN(parseInt(givenQty)))){
				jQuery("#validateError").text('<s:text name="invalidQtyFor"/>'+ ' '+grade);
				return false;
			}
			if(givenQty!='' && parseInt(givenQty)>0 &&  givenGrossWt==''){
				jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+grade);
				return false;
			}
			if(givenQty!='' && givenGrossWt !='' && parseInt(givenQty)!=0 && parseFloat(givenQty)>=parseFloat(givenGrossWt)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />'+ ' '+grade);
				return false;
			}
			if( givenQty.trim()==''){
				givenQty=0;
			}
			if(givenQty>exnoOfBags){
				jQuery("#validateError").text('<s:text name="greaterNoOfBags"/>'+ ' '+grade);
				return false;
			}
			

			}
			 if(givenGrossWt=='' || givenGrossWt=='0.000' || givenGrossWt=='undefined' || parseFloat(givenGrossWt)<=0){
				 jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+grade);
					return false;
			 }
			  
			//No of bags Entered But Gross Weight Empty
		
			if(givenGrossWt !='' && (isNaN(givenGrossWt) || isNaN(parseFloat(givenGrossWt)))){
				
				jQuery("#validateError").text('<s:text name="invalidWtFor"/>'+ ' '+grade);
				return false;
			}
			//Gross Weight Entered but Number Of bags Empty
		    /*if(givenGrossWt !='' &&  parseFloat(givenGrossWt)>0  && givenQty=='' ){
				jQuery("#validateError").text('<s:text name="emptyQtyFor"/>'+ ' '+grade);
				return false;
			}*/
			//Gross Weight Entered but Number Of bags Zero
			 /*if(givenGrossWt !='' &&  parseFloat(givenGrossWt)>0  && givenQty!='' && givenQty=='0'){
				jQuery("#validateError").text('<s:text name="invalidQtyFor"/>'+ ' '+grade);
				return false;
			}*/
			//Number Of bags Entered but Gross Weight Zero	
			//Number Of bags is Greater than Gross Weight Zero	
			
			
			
			if(givenGrossWt>exWeight){
				jQuery("#validateError").text('<s:text name="greaterGrassWeight"/>'+ ' '+grade);
				return false;
			}
			if( givenGrossWt.trim()!='' && parseFloat(givenGrossWt)>0 )
				 var givenQty=jQuery("#quantityTB"+globalGradeIdArray[cnt]).val().trim();
			//alert("givenQty :"+givenQty);
				gradeInputStr+=gradeId.trim()+'~~'+givenQty+'~~'+parseFloat(givenGrossWt.trim()).toFixed(3)+'||'; 
			
				

		}
		var totQty=jQuery("#totalQtyFinal").text();
		var totGrossWt=jQuery("#totalGrossWtFinal").text();		
		if(parseInt(totQty)<=0 && parseFloat(totGrossWt)<=0){
			jQuery("#validateError").text('<s:text name="minimumGrade"/>');
			return false;
		}
		return true; 
}

function submitstockTransfer(){
	var coOperative=$('#coOperative').val();
    var recCoOperative=$('#recCoOperative').val();
    var product=jQuery("#product").val();
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var date = jQuery.trim(jQuery("#startDate").val());
	
	
	
	var hit=validateData();
	if(hit){
		$("#saveBtn").prop("disabled",true);
		
		jQuery.post("stockTransfer_populateProductTransfer.action",{selectedCoOperative:coOperative,selectedRecCoOperative:recCoOperative,selectedProduct:product,startDate:date,truckId:truckId,driverId:driverId,gradeInputString:gradeInputStr},function(data){			
			if(data == null || data == ""){		
			$("#saveBtn").prop("disabled",false);
			}
		else{ 	 			
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();
			$("#saveBtn").prop("disabled",false);
		}
		});				
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

function populateProducts(call){
	if(tenant=="lalteer"){
	jQuery.post("stockTransfer_populateProducts1",{selectedFarmer:$(call).val()},function(result){
		var rrr=result.replace(/((\[)|(\]))/g,'');
		var options = rrr.split(",");
		$('#product').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');		
		addOption(document.getElementById('product'), "Select", "0");		
		for(var i=0;i<options.length;i++){
			var option = options[i];
			if(jQuery.trim(option)!=""){
				var optionVal = jQuery.trim(option).split("-");
				var id = optionVal[1];
				var val = optionVal[0];
                //$("#product").append("<option value='"+id+"'>"+val+"</option>");
                addOption(document.getElementById('product'), val, id);
			}
		}
	});
	createGrid(jQuery("#product").val());
}else{
	jQuery.post("stockTransfer_populateProducts",{selectedCoOperative:$(call).val()},function(result){
		var rrr=result.replace(/((\[)|(\]))/g,'');
		var options = rrr.split(",");
		$('#product').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');		
		//addOption(document.getElementById('product'), "Select", "0");			
		for(var i=0;i<options.length;i++){
			var option = options[i];
			if(jQuery.trim(option)!=""){
				var optionVal = jQuery.trim(option).split("-");
				var id = optionVal[1];
				var val = optionVal[0];
                //$("#product").append("<option value='"+id+"'>"+val+"</option>");
                addOption(document.getElementById('product'), val, id);
			}
		}
	});
	createGrid(jQuery("#product").val());
	
}
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
 	$.post("stockTransfer_create.action",{procurementInfo:""},function(data){
 	});
 	document.redirectform.action="stockTransfer_list.action";
	document.redirectform.submit();
 
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
	var data='';
	reloadGradeTable(data);
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	 $('#startDate').datepicker('setDate', new Date());
	document.getElementById('totalGrossWtFinal').innerHTML='';
	if(tenant!='pratibha'){	
	document.getElementById('totalQtyFinal').innerHTML='';
}
	gradeInputStr='';
	createGrid(jQuery("#product").val());
	jQuery("#validateError").text('');
	jQuery("#stockTransferInfo").show();
}
function resetTableDetails(){
	
	var idsArray=['product','recCoOperative'];
	resetSelect2(idsArray);
	
	
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	gradeInputStr='';
	createGrid(jQuery("#product").val());
	jQuery("#validateError").text('');
	jQuery("#stockTransferInfo").show();
}

function populateFarmers(call){
	jQuery.post("stockTransfer_populateFarmers",{selectedCoOperative:$(call).val()},function(result){
		var rrr=result.replace(/((\[)|(\]))/g,'');
		var options = rrr.split(",");
		$('#farmer').find('option').remove().end().append('<option value=""><s:text name="txt.select"/></option>').val('');
		for(var i=0;i<options.length;i++){
			var option = options[i];
			if(jQuery.trim(option)!=""){
				var optionVal = jQuery.trim(option).split("-");
				var id = optionVal[1];
				var val = optionVal[0];
                $("#farmer").append("<option value='"+id+"'>"+val+"</option>");
			}
		}
	});
	createGrid(jQuery("#farmer").val());
}

function distributionEnable(){
	window.location.href="stockTransfer_create.action";
	reset();
	resetTableDetails();
}


</script>
</body>