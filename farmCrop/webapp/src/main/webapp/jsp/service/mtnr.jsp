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

	<s:form id="mtnrForm" action="mtnr_create"
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
								<div class="flexi flexi8">
									<label for="txt"><s:property value="%{getLocaleProperty('warehouse')}"/> <sup
									style="color: red;">*</sup></label>

					<div class="form-element">
						<s:select name="selectedWarehouseId"  list="listWarehouse" headerKey="" headerValue="%{getText('txt.select')}" listKey="key" listValue="value" id="warehouse" onchange="populateReceiptNos(this)" cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="flexi flexi8">
					<label for="txt"><s:text name="receiptNumber" /><sup style="color: red;">*</sup></label>

					<div class="form-element">
										<s:select name="receiptNo"  list="listOfReceiptNo" headerKey="" headerValue="%{getText('txt.select')}" listKey="key" listValue="value" id="receiptNoList" onchange="createGrid(this);loadTruck(this);" cssClass="form-control input-sm select2" />
					</div>
				</div>
								<%-- <div class="flexi flexi8">
									<label for="txt"><s:text name="product" /><sup
										style="color: red;">*</sup></label>
								<div class="form-element">
										<s:select name="product"  list="listProduct" headerKey="" headerValue="%{getText('txt.select')}" listKey="key" listValue="value" id="receiveingProduct" cssClass="form-control input-sm select2" onchange="createGrid(this);loadUnit();"/>
								</div>
								</div>
								
								<div class="flexi flexi8">
									<label for="txt"><s:text name="unit" /></label>
								<div class="form-element">
										<s:label id="unit" />
								</div>
								</div> --%>
								
								<div class="flexi flexi8">
									<label for="txt"><s:text name="mtnrdate" /><sup
										style="color: red;">*</sup></label>

									<div class="form-element">
										<s:textfield name="startDate" id="startDate" readonly="true"  theme="simple" data-date-format="%{getGeneralDateFormat().toLowerCase()}"
										data-date-viewmode="years" cssClass="date-picker form-control"
										size="20" />						
									</div>
								</div>
				
								<div class="flexi flexi8">
									<label for="txt"><s:text name="truckId" /> <span
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
								<div class="formContainerWrapper prodRecTable">
									<h2>
										<s:text name="product.reception" />
									</h2>
									<div class="flexiWrapper filterControls">
										<div id="prodRecpInfo"
											style="text-align: center; height: 20px; width: 100%;">
										<%-- 	<s:text name="stockTransfer.select.info" /> --%>
										</div>

										<div style="width: 98%;" id="baseDiv">
											<table id="detail" style="width: auto !Important;"></table>
										</div>

									</div>
									</div>
									<div class="yui-skin-sam" id="savebutton">
										<sec:authorize ifAllGranted="service.mtnr.procurement.update">

											<span id="save" class=""><span class="first-child">
													<button id="saveBtn" type="button"
														onclick="submitProdRecp()"
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
            <span><h5>Product Reception has been Done Successfully</h5></span>
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

	<s:form action="mtnr_populatePrintHTML" id="receiptForm"
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

jQuery(document).ready(function(){
	resetGradeDetails();
	jQuery("#receiveingProduct").val('');
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	jQuery("#warehouse").val("");
	jQuery(".prodRecTable").hide();
	
	
	
	 $('#startDate').datepicker('setDate', new Date());
	// Here the timer starts for stockTransfer action page. 
	// "stockTransfer" is the action url 
	//startTimer("stockTransfer");
	<s:if test='stockTransferDescription!=null'>
	jQuery('<tr><td><s:property value="stockTransferDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
	jQuery("#restartAlert").css('height','150px');
    </s:if>

  /*   populateProducts($('#coOperative')); */
    
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

function loadUnit(){
	var product=$("#receiveingProduct").val();
	 $.post("mtnr_populateUnit",{selectedProduct:product},function(result){
		var strData=JSON.stringify(result);
		var data1 =jQuery.parseJSON(strData);
		//alert("data1:"+data1.unit);
		$('#unit').text(data1.unit);
	}); 	 
} 

$( "#startDate" ).datepicker({
    endDate: '+0d',
     autoclose: true,
		beforeShow : function()
		{
		jQuery( this ).datepicker({ maxDate: 0 });
		},
		changeMonth: true,
		changeYear: true
		});

function createGrid(val){
	
	jQuery(".prodRecTable").show();
	try {
  		jQuery("#detail").empty();
	  jQuery("#detail").jqGrid('GridUnload');          
    }catch(e){}
	globalGradeIdArray=new Array();
	if(val!=""){
		jQuery("#prodRecpInfo").hide();
		var agentIdx='<%=request.getParameter("agentId")%>';	
		jQuery.post("mtnr_populateStock",{receiptNoId:$("#receiptNoList").val(),selectedCoOperative:$('#warehouse').val()},function(result){
			var procurementProductJSON=$.parseJSON(result);
			for(var count=0;count<procurementProductJSON.products.length;count++){
				//if(procurementProductJSON.products[count].id==$(product).val()){	
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					
					var procurementGrades = procurementProductJSON.products[count].procurementGrades;

					for(var i=0;i<procurementGrades.length;i++){

						globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;

						var row = new Object();
						row['procurementProdName'] =  procurementGrades[i].procurementProdName+'<center><div id="prodIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].procurementProdId+'</div><div id="prodNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementProdName+'</div>';
						row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="gradeVarietyIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].id+'</div><div id="gradeVarietyNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						row['gradeName'] =  procurementGrades[i].gradeName+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].gradeId+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].gradeName+'</div>';
						row['unit'] =  procurementGrades[i].unit+'<center><div id="unitHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].id+'</div><div id="unitNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].unit+'</div>';
						if(tenant!="pratibha"){
						row['mtntBags'] = procurementGrades[i].mtntBags+'<center><div id="mtnrBagVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].mtntBags+'</div><div id="mtnrBagVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].mtntBags+'</div>';
						}
						row['mtntGrssWeight'] =procurementGrades[i].mtntGrssWeight+'<center><div id="mtnrwtVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].mtntGrssWeight+'</div><div id="mtnrwtVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].mtntGrssWeight+'</div>';
						if(tenant!="pratibha"){
						row['noOfBags'] ='<input id="quantityTB'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalQuantity();" onkeypress="return isNumber(event)" style="text-align:right;" value="0" maxlength="6" name="" class="col-sm-3 form-control">'
						}
						row['netWeight'] = '<input id="grossWeightTB'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalGrossWeight();" onkeypress="return isDecimal(event)" style="text-align:right;padding-right:1px;" value="0.000" maxlength="10" name="" class="col-sm-3 form-control">'
										 
									  
						rowData[rowData.length] = row;
					}
					
					globalGradeIdArray[globalGradeIdArray.length] = '';
					if(tenant!="pratibha"){	
					gridColumnNames  = ['<s:text name="productCodeName"/>','<s:text name="procurementvariety"/>','<s:text name="procurementgrade"/>','<s:text name="unit"/>','<s:text name="stocktranfered" />','<s:text name="mtntWeight" />','<s:text name="noofBags" />','<s:text name="grossWeight" /> <sup style="color: red;">*</sup>']
					}
					else{
						gridColumnNames  = ['<s:text name="productCodeName"/>','<s:text name="procurementvariety"/>','<s:text name="procurementgrade"/>','<s:text name="unit"/>','<s:property value="%{getLocaleProperty('mtntWeight')}"/>','<s:property value="%{getLocaleProperty('grossWeight')}"/> <sup style="color: red;">*</sup>']
					}
					    				gridColumnModels = [{name:'procurementProdName',index:'procurementProdName',sortable:false, width: 100,resizable: false},
					    				                    {name:'procurementVarietyName',index:'procurementVarietyName',sortable:false, width: 100,resizable: false},
					    				    				{name:'gradeName',index:'gradeName',sortable:false, width: 100,resizable: false},
					    				    				{name:'unit',index:'unit',sortable:false, width: 100,resizable: false},
					    				    				 <s:if test="currentTenantId!='pratibha'">
					    				    				{name:'mtntBags',index:'mtntBags',sortable:false, width: 100,resizable: false},
					    				    				</s:if>
					    				    				{name:'mtntGrssWeight',index:'mtntGrssWeight',sortable:false, width: 150, align:'center',resizable: false},
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
				//}
			}
		});

		
			
	}else{
		jQuery("#prodRecpInfo").show();
	}	
}

function listProcurementDetails(call){
	
	if(call.value=='-1' || call.value == null || call.value == undefined){
		resetGradeDetails();
	}else{
		$.post("mtnr_populateMTNTDetails",{receiptNumber:call.value},function(data){
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
	//var product=jQuery("#receiveingProduct").val();
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var coOperative=$('#warehouse').val();
	var recNo = $('#receiptNoList').val();
    
    if(coOperative=="-1" || coOperative==""){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.warehouse")}' />');
		return false;
	}else if(recNo=="-1"|| recNo==""){
		jQuery("#validateError").text('<s:text name="empty.RecpNo"/>');
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
     }else if(globalGradeIdArray.length==0){
 		 jQuery("#validateError").text('<s:text name="grades.not.exists"/>');
         return false;
 	 }
	    gradeInputStr='';
		for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
			var prodId=jQuery("#prodIdHeader"+globalGradeIdArray[cnt]).html();
		   var gradeId=jQuery("#gradeIdHeader"+globalGradeIdArray[cnt]).html();
		   var grade=jQuery("#gradeNameHeader"+globalGradeIdArray[cnt]).html();
		   if(tenant!='pratibha'){
		   var givenQty=jQuery("#quantityTB"+globalGradeIdArray[cnt]).val().trim();
		   var exnoOfBags=jQuery("#mtnrBagVal"+globalGradeIdArray[cnt]).html();	
		   }
		   var givenGrossWt=jQuery("#grossWeightTB"+globalGradeIdArray[cnt]).val().trim();		
		   //var wgtLbl=$("#lblStock"+globalGradeIdArray[cnt]).text();
		   //var arr=wgtLbl.split(" Bags - ");
		   
		   var exWeight=jQuery("#mtnrwtVal"+globalGradeIdArray[cnt]).html();
		   var pmtDetailId=jQuery("#unitHeader"+globalGradeIdArray[cnt]).html();
		   //console.log(exnoOfBags+" x "+exWeight);
		  // console.log(givenQty+" y "+givenGrossWt);
			 //Not a number Validation
			  if(tenant!='pratibha'){
				  if(givenQty!='' && (isNaN(givenQty) || isNaN(parseInt(givenQty)))){
						jQuery("#validateError").text('<s:text name="invalidQtyFor"/>'+ ' '+grade);
						return false;
					}
				  if(givenQty!='' && parseInt(givenQty)>0 &&  givenGrossWt==''){
						jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+grade);
						return false;
					}
				  if(parseInt(givenQty)>parseInt(exnoOfBags)){
						jQuery("#validateError").text('<s:text name="greaterNoOfBags"/>'+ ' '+grade);
						return false;
					}
				  if(givenQty!='' && givenGrossWt !='' && parseInt(givenQty)!=0 && parseFloat(givenQty)>=parseFloat(givenGrossWt)){
						jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />'+ ' '+grade);
						return false;
					}
					
					if( givenQty.trim()==''){
						givenQty=0;
					}
			  }
			if(givenGrossWt=='' || parseFloat(givenGrossWt)<=0 || givenGrossWt=='undefined'){
				jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+grade);
				return false;
			}
			if(givenGrossWt !='' && (isNaN(givenGrossWt) || isNaN(parseFloat(givenGrossWt)))){
				jQuery("#validateError").text('<s:text name="invalidWtFor"/>'+ ' '+grade);
				return false;
			}
			//No of bags Entered But Gross Weight Empty
			
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
			
			
			if(parseFloat(givenGrossWt)>parseFloat(exWeight)){
				jQuery("#validateError").text('<s:text name="greaterGrassWeight"/>'+ ' '+grade);
				return false;
			}
			
			//Number Of bags Entered but Gross Weight Zero	
			//Number Of bags is Greater than Gross Weight Zero	
			
			if( givenGrossWt.trim()!='' && parseFloat(givenGrossWt)>0 )
				 if(tenant!='pratibha'){
				gradeInputStr+=prodId.trim()+'##'+gradeId.trim()+'##'+parseInt(givenQty.trim())+'##'+parseFloat(givenGrossWt.trim()).toFixed(3)+'##'+parseInt(pmtDetailId.trim())+'||';
				 }
				 else{
					 gradeInputStr+=prodId.trim()+'##'+gradeId.trim()+'##0##'+parseFloat(givenGrossWt.trim()).toFixed(3)+'##'+parseInt(pmtDetailId.trim())+'||';	 
				 }
			
		}
		var totQty=jQuery("#totalQtyFinal").text();
		var totGrossWt=jQuery("#totalGrossWtFinal").text();		
		if(parseInt(totQty)<=0 && parseFloat(totGrossWt)<=0){
			jQuery("#validateError").text('<s:text name="minimumGrade"/>');
			return false;
		}
		return true; 
}

function submitProdRecp(){
	var coOperative=$('#warehouse').val();
    var recNum=$('#receiptNoList').val();
    //var product=jQuery("#receiveingProduct").val();
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var date = jQuery.trim(jQuery("#startDate").val());

	var hit=validateData();
	if(hit){
		$("#saveBtn").prop("disabled",true);
		jQuery.post("mtnr_populateProductReception.action",{selectedCoOperative:coOperative,startDate:date,truckId:truckId,driverId:driverId,productDetailsStr:gradeInputStr,recNum:recNum},function(data){			
			if(data == null || data == ""){		
			$("#saveBtn").prop("disabled",false);
			jQuery("#totalQtyFinal").html("0");
			jQuery("#totalGrossWtFinal").html("0.00");
			//jQuery(".prodRecTable").hide();
			}
		else{ 	 			
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();
			$("#saveBtn").prop("disabled",false);
			jQuery("#totalQtyFinal").html("0");
			jQuery("#totalGrossWtFinal").html("0.00");
			
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
 	$.post("mtnr_create.action",{procurementInfo:""},function(data){
 	}); 
 	jQuery(".prodRecTable").hide();	
 	document.redirectform.action="mTNR_list.action";
	document.redirectform.submit();
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}
function reset(){

	var idsArray=['warehouse','receiptNoList'];
	resetSelect2(idsArray);
	var data='';
	reloadGradeTable(data);
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	//jQuery("#unit").val('');
	 $('#startDate').datepicker('setDate', new Date());
	gradeInputStr='';
	createGrid(jQuery("#receiveingProduct").val());
	jQuery("#validateError").text('');
	jQuery(".prodRecTable").hide();
}

// mtnr
function populateReceiptNos(val){
	 resetFirstGrid();
	$("#receiptNo").empty();
	var warehouseId= $('#warehouse').val();
	$.post("mtnr_populateReceiptNos",{warehouseId:warehouseId},function(result){
		var data =jQuery.parseJSON(result);
		/* $("#receiptNo").append('<option value="-1">'+'<s:text name="txt.select"/>'+'</option>');
		$.each(data, function(k, v) {
			if(v.name!=""||v.name!=null){
			$("#receiptNo").append('<option value="' + v.id + '">' + v.name + '</option>');
			}
		}) */
		insertOptions("receiptNoList",data);
	});
}


function loadProducts(val){
	$("#receiveingGrade").empty();
	var receiptNoId=$("#receiptNoList").val();
	var farmer  = '';
	if($('#farmer')!=null){
	
		farmer = $('#farmer').val();
	}
	$.post("mtnr_populateProduct",{receiptNoId:receiptNoId,farmer:farmer},function(result){
		var data =jQuery.parseJSON(result);
		insertOptions("receiveingProduct",data);
	});
	
}
function resetFirstGrid(){
	var idsArray=['receiptNoList'];
	resetSelect2(idsArray);
	//$('#unit').val("");
	$('#truckId').val("");
	$('#driverId').val("");
	jQuery(".prodRecTable").hide();
	
}

function loadTruck(){
	var value=$("#receiptNoList").val();
	 $.post("mtnr_populateTruck",{receiptNoId:value},function(result){
		 var strData=JSON.stringify(result);
			var data1 =jQuery.parseJSON(strData);
			$("#truckId").val(data1.truck);
			$("#driverId").val(data1.driver);
			
	}); 		
} 

function distributionEnable(){
	window.location.href="mtnr_create.action";
}

</script>
</body>