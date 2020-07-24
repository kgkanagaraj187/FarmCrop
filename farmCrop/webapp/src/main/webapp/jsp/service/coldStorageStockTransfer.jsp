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

	<s:form id="stockTransferForm" action="coldStorageStockTransfer_create"
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
							<div class="flexi flexi10">
									<label for="txt"><s:text name="stockTransferdate" /> </label>
									<div class="form-element">
										<s:textfield name="startDate" id="startDate" readonly="true"
											theme="simple"
											data-date-format="%{getGeneralDateFormat().toLowerCase()}"
											data-date-viewmode="years"
											cssClass="date-picker form-control col-sm-3" />
									</div>
								</div>
								<div class="flexi flexi10">
										<label for="txt"><s:property value="%{getLocaleProperty('warehouse')}"/>	 <span
											class="manadatory">*</span></label>
										<div class="form-element">
										<s:select name="selectedWarehouseId" list="listWarehouse"
								headerKey="" headerValue="%{getText('txt.select')}"
								lisykey="key" listValue="value" id="warehouse"
								cssClass="form-control input-sm select2" onchange="listColdStorageName(this.value);" />
										</div>
									</div>
								
								<div class="flexi flexi10">
									<label for="txt"><s:property value="%{getLocaleProperty('coldStorageName')}"/>	 <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select name="coldStorageName" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								lisykey="key" listValue="value" id="coldStorageName"
								cssClass="form-control input-sm select2" onchange="listProduct(this.value);"  />
									</div>
								</div>
								
								<div class="flexi flexi10">
									<label for="txt"><s:text name="product" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select id="product" headerKey=""
											headerValue="%{getText('txt.select')}" name="selectedProduct"
											list="{}" cssClass="col-sm-3 form-control select2" onchange="listBatchNo(this.value);"/>
									</div>
								</div>
								<div class="flexi flexi10">
									<label for="txt"><s:property value="%{getLocaleProperty('batchNo')}"/> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select id="batchNo" headerKey=""
											headerValue="%{getText('txt.select')}" name="selectedBatchNo"
											list="{}" cssClass="col-sm-3 form-control select2" multiple="true" onchange="getSelectedBatch();"/>
									</div>
								</div>
								<div class="flexi flexi10">
									<label for="txt"><s:property value="%{getLocaleProperty('buyer')}"/> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select name="selectedBuyer" id="buyer"
									list="buyersList" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									cssClass="form-control select2" />
									</div>
								</div>
								
								<div class="flexi flexi10">
									<label for="txt"><s:property value="%{getLocaleProperty('truckId')}"/> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="truckId" id="truckId" maxlength="35"
											cssClass="form-control" />
									</div>
								</div>
								<div class="flexi flexi10">
									<label for="txt"><s:text name="driverId" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="driverId" id="driverId" maxlength="35"
											cssClass="form-control" />
									</div>
								</div>
								
								<div class="flexi flexi10">
									<label for="txt"><s:text name="poNumber" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="poNumber" id="poNumber" maxlength="35"
											cssClass="form-control" />
									</div>
								</div>
								
								<div class="flexi flexi10">
									<label for="txt"><s:text name="invoiceNumber" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="invoiceNumber" id="invoiceNumber" maxlength="35"
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
										<s:property value="%{getLocaleProperty('coldStorageStockTransfer.select.info')}"/>	</div>

										<div style="width: 98%;" id="baseDiv">
											<table id="detail" style="width: auto !Important;"></table>
										</div>

									</div>
									<div class="yui-skin-sam" id="savebutton">
										<sec:authorize ifAllGranted="service.coldStorageStockTransfer.update">

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

	<button type="button" data-toggle="modal" data-target="#myModal"
		style="display: none" id="enableModal" class="modal hide"
		data-backdrop="static" data-keyboard="false">Open Modal</button>

	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-sm">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						onclick="disablePopupAlert()">&times;</button>
					<h4 class="modal-title">
						<s:property value="%{getLocaleProperty('info.coldStorageStockTransfer')}" />
					</h4>
				</div>
				<div class="modal-body">
					<div id="divMsg" align="center"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="disablePopupAlert()">Close</button>
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
	/* jQuery("#batchNo").change(function(){
		createGrid(this);
	}); */
	//createGrid();
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

   // populateProducts($('#coOperative'));
    
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

function listColdStorageName(value) {
	if (!isEmpty(value)) {
		$.post("coldStorageStockTransfer_populateColdStorageName", {
			selectedWarehouse : value
		}, function(data) {
			insertOptions("coldStorageName", $.parseJSON(data));
		});
	}
}

function listProduct(value) {
	var warehouse = $("#warehouse option:selected").val();
	if (!isEmpty(value)) {
		$.post("coldStorageStockTransfer_populateProduct", {
			selectedWarehouse : warehouse,
			selectedColdStorageName:value
		}, function(data) {
			insertOptions("product", $.parseJSON(data));
		});
	}
}

function listBatchNo(value) {
	var warehouse = $("#warehouse option:selected").val();
	var coldStorageName = $("#coldStorageName option:selected").val();
	if (!isEmpty(value)) {
		$.post("coldStorageStockTransfer_populateBatchNo", {
			selectedWarehouse : warehouse,
			selectedColdStorageName:coldStorageName,
			selectedProduct:value
		}, function(data) {
			insertOptions("batchNo", $.parseJSON(data));
		});
	}
}

function getSelectedBatch(){


	
	jQuery("#detail").clearGridData(true).trigger("reloadGrid");
	var selectedBatchNo="";
		globalGradeIdArray=new Array();

		jQuery("#stockTransferInfo").hide();
		var agentIdx='<%=request.getParameter("agentId")%>';	
		
		$($("#batchNo").val()).each(function(k, v) {
			selectedBatchNo +="'"+v+"'" + ",";
		});
		//alert(selectedBatchNo);
		jQuery.post("coldStorageStockTransfer_populateStockByLotCode",{
			selectedCoOperative:$("#warehouse").val(),
			selectedProduct:$('#product').val(),
			selectedColdStorageName:$('#coldStorageName').val(),
			selectedBatchNo:selectedBatchNo,
			},
			function(result){
//alert(JSON.stringify($.parseJSON(result)));
			console.log(JSON.stringify($.parseJSON(result)));
			
			var procurementProductJSON=$.parseJSON(result);
			console.log(procurementProductJSON.products.length);
			for(var count=0;count<procurementProductJSON.products.length;count++){
				//alert($(product).val());
				//if(procurementProductJSON.products[count].id==$(product).val()){	
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					
					var procurementGrades = procurementProductJSON.products[count].procurementGrades;

					for(var i=0;i<procurementGrades.length;i++){

						globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;

						var row = new Object();
						row['farmerName'] =  procurementGrades[i].farmerName+'<center><div id="farmerIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].farmerId+'</div><div id="farmerNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].farmerName+'</div>';
						
						row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="varietyIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].varietyId+'</div><div id="varietyNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						
						//row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].gradeId+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						
						//row['gradeName'] =  procurementGrades[i].gradeName+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].gradeId+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].gradeName+'</div>';
						
						row['blockName'] = procurementGrades[i].blockName+'<center><div id="blockCodeVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].blockCode+'</div><div id="blockNameVal'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].blockName+'</div>';
						
						
						row['floorName'] =procurementGrades[i].floorName+'<center><div id="floorCodeVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].floorCode+'</div><div id="floorNameVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].floorName+'</div>';
						
						row['bayNum'] =procurementGrades[i].bayNum+'<center><div id="bayNoVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].bayNo+'</div><div id="bayNumVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].bayNum+'</div>';
						
						
						row['existingNoOfBags'] = procurementGrades[i].existingNoOfBags+'<center><div id="existingNoOfBagsVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].existingNoOfBags+'</div><div id="existingNoOfBagsVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].existingNoOfBags+'</div>';
						row['existingNoOfQty'] =procurementGrades[i].existingNoOfQty+'<center><div id="existingNoOfQtyVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].existingNoOfQty+'</div><div id="existingNoOfQtyVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].existingNoOfQty+'</div>';
						row['batchNo'] =procurementGrades[i].batchNo+'<center><div id="batchNoVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].batchNo+'</div>';
						
						row['noOfBags'] ='<input id="noOfBags'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalQuantity();" onkeypress="return isNumber(event)" style="text-align:right;" value="0" maxlength="6" name="" class="col-sm-3 form-control">' 
						row['noOfQty'] = '<input id="noOfQty'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalGrossWeight();" onkeypress="return isDecimal(event)" style="text-align:right;padding-right:1px;" value="0.000" maxlength="10" name="" class="col-sm-3 form-control">'
										 
						rowData[rowData.length] = row;
					}
					
					globalGradeIdArray[globalGradeIdArray.length] = '';

					gridColumnNames  = ['<s:text name="farmerName"/>','<s:text name="procurementvariety"/>','<s:property value="%{getLocaleProperty('lotCode')}"/> ','<s:property value="%{getLocaleProperty('blockName')}"/> ','<s:property value="%{getLocaleProperty('floorName')}"/> ','<s:property value="%{getLocaleProperty('bayNum')}"/> ','<s:property value="%{getLocaleProperty('Avaliable Bags')}"/> ','<s:property value="%{getLocaleProperty('Avaliable Weight')}"/> ','<s:property value="%{getLocaleProperty('Transfered Bags')}"/><sup style="color: red;">*</sup>','<s:property value="%{getLocaleProperty('Transfered Net Weight')}"/> <sup style="color: red;">*</sup>']
    				
    				gridColumnModels = [{name:'farmerName',index:'farmerName',sortable:false, width: 100,resizable: false},
    				                    {name:'procurementVarietyName',index:'procurementVarietyName',sortable:false, width: 100,resizable: false},
    				    				{name:'batchNo',index:'batchNo',sortable:false, width: 100,resizable: false},
    				    				{name:'blockName',index:'blockName',sortable:false, width: 100,resizable: false},
    				    				{name:'floorName',index:'floorName',sortable:false, width: 100,resizable: false},
    				    				{name:'bayNum',index:'bayNum',sortable:false, width: 100,resizable: false},
    				    				{name:'existingNoOfBags',index:'existingNoOfBags',sortable:false, width: 100,resizable: false},
    				    				{name:'existingNoOfQty',index:'existingNoOfQty',sortable:false, width: 150, align:'center',resizable: false},
    				    				{name:'noOfBags',index:'noOfBags',sortable:false, width: 100, align:'center',resizable: false},
    				    				{name:'noOfQty',index:'netWeight',sortable:false, width: 100, align:'center',resizable: false}]									    					


					footerRow = {stock:'<s:text name="total"/>',noOfBags:'<div id="totalQtyFinal">0</div>',noOfQty:'<center><div id="totalGrossWtFinal">0.000</div></center>'};
					    				
					jQuery("#detail").jqGrid(
					{		   		
					   	colNames:gridColumnNames,
					   	colModel:gridColumnModels,
					    width: $("#baseDiv").width(),
						scrollOffset: 0,   
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
			//}
		});

}
function createGrid(product){
	var selectedBatchNo="";
	 
	try {
  		jQuery("#detail").empty();
	  jQuery("#detail").jqGrid('GridUnload');          
    }catch(e){}
	globalGradeIdArray=new Array();
	if(product!=""){
		jQuery("#stockTransferInfo").hide();
		var agentIdx='<%=request.getParameter("agentId")%>';	
		
		$($("#batchNo").val()).each(function(k, v) {
			selectedBatchNo +="'"+v+"'" + ",";
		});

		jQuery.post("coldStorageStockTransfer_populateStock",{
			selectedCoOperative:$("#warehouse").val(),
			selectedProduct:$('#product').val(),
			selectedColdStorageName:$('#coldStorageName').val(),
			selectedBatchNo:$('#batchNo').val(),
			},
			function(result){
//alert(JSON.stringify($.parseJSON(result)));
			console.log(JSON.stringify($.parseJSON(result)));
			var procurementProductJSON=$.parseJSON(result);
			for(var count=0;count<procurementProductJSON.products.length;count++){
				//alert($(product).val());
				//if(procurementProductJSON.products[count].id==$(product).val()){	
					var gridColumnNames = new Array();
					var gridColumnModels = new Array();
					var footerRow = new Object();
					var rowData = new Array();
					
					var procurementGrades = procurementProductJSON.products[count].procurementGrades;

					for(var i=0;i<procurementGrades.length;i++){

						globalGradeIdArray[globalGradeIdArray.length] = procurementGrades[i].id;

						var row = new Object();
						row['farmerName'] =  procurementGrades[i].farmerName+'<center><div id="farmerIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].farmerId+'</div><div id="farmerNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].farmerName+'</div>';
						
						row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="varietyIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].varietyId+'</div><div id="varietyNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						
						//row['procurementVarietyName'] =  procurementGrades[i].procurementVarietyName+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].gradeId+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].procurementVarietyName+'</div>';
						
						//row['gradeName'] =  procurementGrades[i].gradeName+'<center><div id="gradeIdHeader'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].gradeId+'</div><div id="gradeNameHeader'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].gradeName+'</div>';
						
						row['blockName'] = procurementGrades[i].blockName+'<center><div id="blockCodeVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].blockCode+'</div><div id="blockNameVal'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].blockName+'</div>';
						
						
						row['floorName'] =procurementGrades[i].floorName+'<center><div id="floorCodeVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].floorCode+'</div><div id="floorNameVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].floorName+'</div>';
						
						row['bayNum'] =procurementGrades[i].bayNum+'<center><div id="bayNoVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].bayNo+'</div><div id="bayNumVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].bayNum+'</div>';
						
						
						row['existingNoOfBags'] = procurementGrades[i].existingNoOfBags+'<center><div id="existingNoOfBagsVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].existingNoOfBags+'</div><div id="existingNoOfBagsVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].existingNoOfBags+'</div>';
						row['existingNoOfQty'] =procurementGrades[i].existingNoOfQty+'<center><div id="existingNoOfQtyVal'+(procurementGrades[i].id)+'" style="display: none">'+procurementGrades[i].existingNoOfQty+'</div><div id="existingNoOfQtyVal1'+ procurementGrades[i].id+'" style="display: none">'+ procurementGrades[i].existingNoOfQty+'</div>';
						row['noOfBags'] ='<input id="noOfBags'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalQuantity();" onkeypress="return isNumber(event)" style="text-align:right;" value="0" maxlength="6" name="" class="col-sm-3 form-control">' 
						row['noOfQty'] = '<input id="noOfQty'+(procurementGrades[i].id)+'" type="text" onkeyup="calculateTotalGrossWeight();" onkeypress="return isDecimal(event)" style="text-align:right;padding-right:1px;" value="0.000" maxlength="10" name="" class="col-sm-3 form-control">'
										 
						rowData[rowData.length] = row;
					}
					
					globalGradeIdArray[globalGradeIdArray.length] = '';

					gridColumnNames  = ['<s:text name="farmerName"/>','<s:text name="procurementvariety"/>','<s:property value="%{getLocaleProperty('lotCode')}"/> ','<s:property value="%{getLocaleProperty('blockName')}"/> ','<s:property value="%{getLocaleProperty('floorName')}"/> ','<s:property value="%{getLocaleProperty('bayNum')}"/> ','<s:property value="%{getLocaleProperty('Avaliable Bags')}"/> ','<s:property value="%{getLocaleProperty('Avaliable Weight')}"/> ','<s:property value="%{getLocaleProperty('Transfered Bags')}"/><sup style="color: red;">*</sup>','<s:property value="%{getLocaleProperty('Transfered Net Weight')}"/> <sup style="color: red;">*</sup>']
    				
    				gridColumnModels = [{name:'farmerName',index:'farmerName',sortable:false, width: 100,resizable: false},
    				                    {name:'procurementVarietyName',index:'procurementVarietyName',sortable:false, width: 100,resizable: false},
    				    			//	{name:'gradeName',index:'gradeName',sortable:false, width: 100,resizable: false},
    				    				{name:'blockName',index:'blockName',sortable:false, width: 100,resizable: false},
    				    				{name:'floorName',index:'floorName',sortable:false, width: 100,resizable: false},
    				    				{name:'bayNum',index:'bayNum',sortable:false, width: 100,resizable: false},
    				    				{name:'existingNoOfBags',index:'existingNoOfBags',sortable:false, width: 100,resizable: false},
    				    				{name:'existingNoOfQty',index:'existingNoOfQty',sortable:false, width: 150, align:'center',resizable: false},
    				    				{name:'noOfBags',index:'noOfBags',sortable:false, width: 100, align:'center',resizable: false},
    				    				{name:'noOfQty',index:'netWeight',sortable:false, width: 100, align:'center',resizable: false}]									    					


					footerRow = {stock:'<s:text name="total"/>',noOfBags:'<div id="totalQtyFinal">0</div>',noOfQty:'<center><div id="totalGrossWtFinal">0.000</div></center>'};
					    				
					jQuery("#detail").jqGrid(
					{		   		
					   	colNames:gridColumnNames,
					   	colModel:gridColumnModels,
					    width: $("#baseDiv").width(),
						scrollOffset: 0,   
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
			//}
		});

		
			
	}else{
		jQuery("#stockTransferInfo").show();
	}	
}

function calculateTotalQuantity(){
	   var totalQuantity=0;
       for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
           var qty=jQuery("#noOfBags"+globalGradeIdArray[cnt]).val();
           if(!isNaN(qty) && qty.trim()!='')
    	   totalQuantity+=parseInt(qty);
       }
       jQuery("#totalQtyFinal").text(totalQuantity);
}

function calculateTotalGrossWeight(){
	var weight=0;
    for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
        var wt=jQuery("#noOfQty"+globalGradeIdArray[cnt]).val();
        if(!isNaN(wt) && wt.trim()!='')
        	weight+=parseFloat(wt);
    }
    jQuery("#totalGrossWtFinal").text(weight.toFixed(3));
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
		   jQuery("#existingNoOfBags"+globalGradeIdArray[cnt]).removeAttr('disabled');
		   jQuery("#noOfQty"+globalGradeIdArray[cnt]).removeAttr('disabled');
		   jQuery("#noOfBags"+globalGradeIdArray[cnt]).val('0');
		   jQuery("#noOfQty"+globalGradeIdArray[cnt]).val('0.000');
	   }

	   var totalQty=0;
	   var totalGrossWt=0.000;
	   for(var cnt=0;cnt<gradeIdDataArr.length-1;cnt++){
		   jQuery("#qty"+gradeIdDataArr[cnt]).html(noOfBagsDataArr[cnt].toString());
		   jQuery("#grossWt"+gradeIdDataArr[cnt]).html(parseFloat(grossWtArr[cnt]).toFixed(3));
		   jQuery("#noOfBags"+gradeIdDataArr[cnt]).val(parseInt(noOfBagsDataArr[cnt]));
		   jQuery("#noOfQty"+gradeIdDataArr[cnt]).val(parseFloat(grossWtArr[cnt]).toFixed(3));
		   totalQty+=parseInt(noOfBagsDataArr[cnt]);
		   totalGrossWt+=parseFloat(grossWtArr[cnt]);
	   }

	   jQuery("#totalQtyInfo").html(totalQty.toString());
	   jQuery("#totalGrossWtInfo").html(totalGrossWt.toFixed(3));
	   jQuery("#totalQtyFinal").html(totalQty.toString());
	   jQuery("#totalGrossWtFinal").html(totalGrossWt.toFixed(3));	    
    } 
}

function resetGradeDetails(){
	jQuery("#mtntDetails").html('');
	for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
		   jQuery("#qty"+globalGradeIdArray[cnt]).html('0');
		   jQuery("#grossWt"+globalGradeIdArray[cnt]).text('0.000');
		   jQuery("#noOfBags"+globalGradeIdArray[cnt]).val('0');
		   jQuery("#noOfQty"+globalGradeIdArray[cnt]).val('0.000');
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
	var warehouse=$('#warehouse').val();
    var coldStorageName=$('#coldStorageName').val();
	var batchNo=$('#batchNo').val();
	var buyer=$('#buyer').val();
	var poNumber = $("#poNumber").val();
	var invoiceNumber = $("#invoiceNumber").val();
    if(isEmpty(warehouse)){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.warehouse")}' />');
		return false;
	}
    if(isEmpty(coldStorageName)){
    	jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.coldStorageName")}' />');
		return false;
	}
	
	if(isEmpty(product)){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.product")}' />');
		return false;
	}
     if(isEmpty(batchNo)){
    	 jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.batchNo")}' />');
         return false;
     }
     if(isEmpty(buyer)){
    	 jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.buyer")}' />');
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

 	if(isEmpty(poNumber)){
 		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.poNumber")}' />');
 		return false;
	}else if(isEmpty(invoiceNumber)){
		jQuery("#validateError").text('<s:property value='%{getLocaleProperty("empty.invoiceNumber")}' />');
		return false;
	}
 	 /* if(globalGradeIdArray.length==0){
 		 jQuery("#validateError").text('<s:text name="grades.not.exists"/>');
         return false;
 	 } */
 //	alert(gradeInputStr);
	    gradeInputStr='';
		for(var cnt=0;cnt<globalGradeIdArray.length-1;cnt++){
		//alert("AAAA");
			var farmerId=jQuery("#farmerIdHeader"+globalGradeIdArray[cnt]).html();
		   var farmerName=jQuery("#farmerNameHeader"+globalGradeIdArray[cnt]).html();
		 // alert(farmerId +" ***** "+farmerName);
		 //  var gradeId=jQuery("#gradeIdHeader"+globalGradeIdArray[cnt]).html();
		  // var grade=jQuery("#gradeNameHeader"+globalGradeIdArray[cnt]).html();	
		  var varietyId=jQuery("#varietyIdHeader"+globalGradeIdArray[cnt]).html();
		  var variety=jQuery("#varietyNameHeader"+globalGradeIdArray[cnt]).html();
		   var givenBags=jQuery("#noOfBags"+globalGradeIdArray[cnt]).val();
		   var givenQty=jQuery("#noOfQty"+globalGradeIdArray[cnt]).val();
		   var blockName=jQuery("#blockCodeVal"+globalGradeIdArray[cnt]).html();
		   var floorName=jQuery("#floorCodeVal"+globalGradeIdArray[cnt]).html();
		   var bayNum=jQuery("#bayNoVal"+globalGradeIdArray[cnt]).html();
		   var batchNo=jQuery("#batchNoVal"+globalGradeIdArray[cnt]).html();
		   var existingNoOfBags=jQuery("#existingNoOfBagsVal"+globalGradeIdArray[cnt]).html();
		   var existingNoOfQty=jQuery("#existingNoOfQtyVal"+globalGradeIdArray[cnt]).html();
		 // alert(blockName+" *** "+floorName+" @@@@ "+bayNum);
		   if(isEmpty(givenBags)){
				jQuery("#validateError").text('<s:text name="emptyBagsFor"/>'+ ' '+variety);
				return false;
			}	
		//Not a number Validation
		else if(!isEmpty(givenBags) && (isNaN(givenBags) || isNaN(parseInt(givenBags)))){
				jQuery("#validateError").text('<s:text name="invalidBagsFor"/>'+ ' '+variety);
				return false;
			}
		
		else if(isEmpty(givenQty)){
				jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+variety);
				return false;
			}
		else if(!isEmpty(givenQty) && (isNaN(givenQty) || isNaN(parseFloat(givenQty)))){
				jQuery("#validateError").text('<s:text name="invalidWtFor"/>'+ ' '+variety);
				return false;
			}
			//No of bags Entered But Gross Weight Empty
			else if(!isEmpty(givenBags) && parseInt(givenBags)>0  && isEmpty(givenQty)){
				jQuery("#validateError").text('<s:text name="emptyWtFor"/>'+ ' '+variety);
				return false;
			}
		   if(parseInt(givenBags)>parseInt(existingNoOfBags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('greaterNoOfBags')}" />'+ ' '+variety);
				return false;
			}
			else if(!isEmpty(givenBags)  && !isEmpty(givenQty) && parseInt(givenBags)!=0 && parseFloat(givenBags)>=parseFloat(givenQty)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />'+ ' '+variety);
				return false;
			} 
		   
			if(givenQty !='' && (isNaN(givenQty) || isNaN(parseFloat(givenQty)))){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('invalidWtFor')}" />'+ ' '+variety);
				return false;
			}

			
			if( givenBags.trim()==''){
				givenBags=0;
			}
			
			if(parseFloat(givenQty)>parseFloat(existingNoOfQty)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('greaterGrassWeight')}" />'+ ' '+variety);
				return false;
			}
			if( givenQty.trim()!='' && parseFloat(givenQty)>0 )
				//alert(varietyId);
			gradeInputStr+=varietyId.trim()+'~~'+blockName.trim()+'~~'+floorName.trim()+'~~'+bayNum.trim()+'~~'+parseInt(givenBags.trim())+'~~'+parseFloat(givenQty.trim()).toFixed(2)+'~~'+farmerId.trim()+'~~'+batchNo.trim()+'||'; 

			
		}
	//alert(gradeInputStr);
		var totQty=jQuery("#totalQtyFinal").text();
		var totGrossWt=jQuery("#totalGrossWtFinal").text();		
		if(parseInt(totQty)<=0 && parseFloat(totGrossWt)<=0){
			jQuery("#validateError").text('<s:text name="minimumGrade"/>');
			return false;
		}
		return true; 
}

function submitstockTransfer(){
	var date = jQuery.trim(jQuery("#startDate").val());
	var warehouse=$('#warehouse').val();
    var coldStorageName=$('#coldStorageName').val();
    var product=jQuery("#product").val();
    var batchNo="";
	var truckId=jQuery("#truckId").val();
	var driverId=jQuery("#driverId").val();
	var buyer=jQuery("#buyer").val();
	var poNumber = $("#poNumber").val();
	var invoiceNumber = $("#invoiceNumber").val();
	$($("#batchNo").val()).each(function(k, v) {
		batchNo +="'"+v+"'" + ",";
	});
	var hit=validateData();
	
	
	if(hit){
		$("#saveBtn").prop("disabled",true);
		jQuery.post("coldStorageStockTransfer_populateProductTransfer.action",{
			selectedWarehouse:warehouse,
			selectedColdStorageName:coldStorageName,
			selectedProduct:product,
			selectedBatchNo:batchNo,
			selectedBuyer:buyer,
			startDate:date,
			truckId:truckId,
			driverId:driverId,
			gradeInputString:gradeInputStr,
			poNumber:poNumber,
			invoiceNumber:invoiceNumber
			},function(data){			
		
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
	$("#poNumber").val('');
	$("#invoiceNumber").val('');
	$("#validateError").text('');
	reset();
 	$.post("coldStorageStockTransfer_create.action",{procurementInfo:""},function(data){
 	}); 
 
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}
function reset(){

	var idsArray=['warehouse','coldStorageName','product','batchNo','buyer'];
	resetSelect2(idsArray);
	var data='';
	reloadGradeTable(data);
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	jQuery("#poNumber").val('');
	jQuery("#invoiceNumber").val('');
	 $('#startDate').datepicker('setDate', new Date());
	document.getElementById('totalGrossWtFinal').innerHTML='';
	document.getElementById('totalQtyFinal').innerHTML='';
	gradeInputStr='';
	getSelectedBatch();
	//createGrid(jQuery("#batchNo").val());
	jQuery("#validateError").text('');
	jQuery("#stockTransferInfo").show();
}
function resetTableDetails(){
	
	var idsArray=['product','coldStorageName'];
	resetSelect2(idsArray);
	
	
	jQuery("#truckId").val('');
	jQuery("#driverId").val('');
	gradeInputStr='';
	createGrid(jQuery("#batchNo").val());
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


</script>
</body>