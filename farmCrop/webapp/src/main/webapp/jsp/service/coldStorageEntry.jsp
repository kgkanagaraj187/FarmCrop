<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<s:form name="form" cssClass="fillform">
		<s:hidden id="enableTraceability" name="enableTraceability" />
		<font color="red"> <s:actionerror /></font>

		<!--start decorator body-->

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
					<s:property value="%{getLocaleProperty('info.general')}" />
				</h2>
				<div class="flexiWrapper filterControls ">
					<div class="flexi flexi12">
						<label for="txt"><s:property
								value="%{getLocaleProperty('date')}" /> <span
							class="manadatory">*</span></label>
							<div class="form-element">
									<s:textfield id="date" data-provide="datepicker" readonly="true"
										theme="simple"
										data-date-format="%{getGeneralDateFormat().toLowerCase()}"
										data-date-end-date="0d"
										cssClass="date-picker form-control input-sm" size="20" />
								</div> 
					</div>

					<s:if test="currentTenantId=='griffith'">
						<div class="flexi flexi12">
							<label for="txt"><s:property
									value="%{getLocaleProperty('procurement.batchNo')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedBatchNo" list="listLotCode" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" onchange="populateData(this.value);"  id="batchNo"
											cssClass="form-control input-sm select2" />
								<div id="validatebatchNoError"
									style="text-align: center; padding: 5px 0 0 0; color: red; font-weight: normal;"></div>
							</div>
						</div>

						<div class="flexi flexi12">

							<label for="txt"><s:property
									value="%{getLocaleProperty('procFarmer')}" /></label>
							<div class="form-element">
								<p data-farmer-id="" id="farmerlabelname"></p>
							</div>

						</div>
						
						<div class="flexi flexi12">

						<label for="txt"><s:property
								value="%{getLocaleProperty('roundOfHarvest')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select list="listRoundOfHarvesting" headerKey=""
								headerValue="%{getText('txt.select')}" name="roundOfHarvest"
								id="roundOfHarvest" cssClass="form-control input-sm select2" />
						</div>

					</div>
						
					</s:if>

					<s:if test="currentTenantId!='griffith'">
					<div class="flexi flexi12">
						<label for="txt"><s:property
								value="%{getLocaleProperty('profile.location.municipality')}" /></label>
						<div class="form-element">
							<s:select name="cityId" list="listMunicipality" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listVillage(this.value);" id="city"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="flexi flexi12">
						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /></label>
						<div class="form-element">
						<s:select name="villageId" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listFarmer(this.value);" id="village"
								cssClass="form-control input-sm select2" />
							
						</div>
					</div>
					<div class="flexi flexi12">

						<label for="txt"><s:property
								value="%{getLocaleProperty('procFarmer')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" name="farmerId"
								id="farmer" onchange="validateBatchNo(this.value);" cssClass="form-control input-sm select2" />
						</div>

					</div>
					
					<div class="flexi flexi12">

						<label for="txt"><s:property
								value="%{getLocaleProperty('roundOfHarvest')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select list="roundOfHarvestList" headerKey=""
								headerValue="%{getText('txt.select')}" name="roundOfHarvest"
								id="roundOfHarvest" cssClass="form-control input-sm select2" />
						</div>

					</div>
					
					</s:if>
					
					
					<div class="flexi flexi12">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="selectedWarehouseId" list="listWarehouse"
								headerKey="" headerValue="%{getText('txt.select')}"
								listkey="key" listValue="value" id="warehouse"
								cssClass="form-control input-sm select2" onchange="listColdStorageName(this.value);" />
						</div>
					</div>
					
					<div class="flexi flexi12">
						<label for="txt"><s:property
								value="%{getLocaleProperty('coldStorageName')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="coldStorageName" list="{}"
								headerKey="-1" headerValue="%{getText('txt.select')}"
								listkey="key" listValue="value" id="coldStorageName"
								cssClass="form-control input-sm select2" onchange="getMaxBinHold(this.value);" />
						</div>
					</div>
					
					
					
					
					
					<div class="flexi flexi12">
									<label for="txt"> <s:property
											value="%{getLocaleProperty('maxBayWeight')}" /> <%-- <s:text name="unit" /> --%>
									</label>
									<div class="form-element">
									<span align="center" style="margin:auto;" id="maxBayHold">0
								</div></div>
					
					<s:if test="currentTenantId!='griffith'">
					<div class="flexi flexi12">
									<label for="txt"><s:property
											value="%{getLocaleProperty('procurement.batchNo')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="selectedBatchNo" id="batchNo" maxlength="25" onchange="validateBatchNo(this.value);" />
										<div id="validatebatchNoError"
											style="text-align: center; padding: 5px 0 0 0; color: red; font-weight: normal;"></div>
									</div>
								</div>
								</s:if>
								
						
						<s:if test="currentTenantId=='griffith'">
						<div class="flexi flexi12">
							<label for="txt"><s:property
									value="%{getLocaleProperty('bond.status')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="bondStatus" list="listBondStatus" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" onchange="enableBondNo(this.value);"  id="bondStatus"
											cssClass="form-control input-sm select2" />
								
							</div>
						</div>
						</s:if>
								
				<div class="flexi flexi12" id="bondNoDiv">
									<label for="txt"><s:property
											value="%{getLocaleProperty('bondNo')}" /> <span
								class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="selectedBondNo" id="bondNo" maxlength="25" />
										<div id="validatebondNoError"
											style="text-align: center; padding: 5px 0 0 0; color: red; font-weight: normal;"></div>
									</div>
								</div>


					<s:if test="currentTenantId=='griffith'">
						<div class="flexi flexi12" id="fileDiv">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('coldStorageEntry.img')}" /> <span
								style="font-size: 8px"> <s:text name="coldStorage.pdfTypes" />
									<font color="red"> <s:text name="pdfSizeMsg" /></font>
							</span>
							</label>
							<div class="form-element">
								<s:file name="imgFile" id="imgFile" onchange="checkImgHeightAndWidth(this)"
									 cssClass="form-control" />
							</div>
						</div>
					</s:if>

				</div>
			</div>
		</div>
		<div class="service-content-wrapper">
			<div class="service-content-section">

				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
						<s:text name="productDetails" />
						</h2>
						<div class="flexiWrapper filterControls">

							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('product')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="procurementProductId" list="productList"
										headerKey="" headerValue="%{getText('txt.select')}"
										listKey="id" listValue="name" id="procurementProductList"
										cssClass="form-control input-sm select2"
										onchange="listProVarierty(this);" />
								</div>
							</div>
							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('variety')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="varietyList"
										cssClass="form-control input-sm select2"
										onchange="listGrade(this.value); resetTableData();" />
								</div>
							</div>
							<div class="flexi flexi12 procGrade hide">
								<label for="txt"><s:property
										value="%{getLocaleProperty('procGrade')}" /></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="gradeList"
										cssClass="form-control input-sm select2" />
								</div>
							</div>					
						
								<!-- <div id="productUnit"></div>  -->
								
							 <div class="flexi flexi12">
									<label for="txt"><s:property
											value="%{getLocaleProperty('blockName')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
									<s:select id="blockName" name="blockName" list="blockNameList" 
									headerValue="%{getText('txt.select')}" headerKey="" listKey="key" listValue="value" 
									cssClass="form-control input-sm select2" />
									</div>
								</div>
								
								<div class="flexi flexi12">
									<label for="txt"><s:property
											value="%{getLocaleProperty('floorName')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
									<s:select id="floorName" name="floorName" list="floorNameList" 
									headerValue="%{getText('txt.select')}" headerKey="" listKey="key" listValue="value" 
									cssClass="form-control input-sm select2" />
									</div>
								</div>														
								
								<div class="flexi flexi12">
									<label for="txt"><s:property
											value="%{getLocaleProperty('bayNumber')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
									<s:select id="bayNumber" name="bayNumber" list="bayNumList" 
									headerValue="%{getText('txt.select')}" headerKey="" listKey="key" listValue="value" 
									cssClass="form-control input-sm select2" onchange="calculateAvailableQty(this.value)"  />
									</div><!-- -->
								</div> 	
								
								<div class="flexi flexi12 ">
									<label for="txt"> <s:property
											value="%{getLocaleProperty('availableQty')}" /> <%-- <s:text name="unit" /> --%>
									</label>
									<div class="form-element"></div>
									<span align="center" style="margin:auto;"id="availableQty">0.00
								</div>	

							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noofBags')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="noOfBagsTxt" maxlength="5"
										cssClass="form-control input-sm"
										onkeypress="return isNumber(event);" />
								</div>
							</div>

							<div class="flexi flexi12 ">
								<label for="txt"><s:property value="%{getLocaleProperty('quantitykg')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									
										<s:textfield id="quantity" maxlength="8" onkeypress="validateAvailableQty(this.value);return isDecimal(event)" />
								
								</div>
							</div>
							
							<div class="flexi flexi12 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('action')}" /></label>
								<td colspan="4" class="alignCenter">
									<table class="actionBtnWarpper">

										<td class="textAlignCenter">


											<button type="button" id="add" class="btn btn-sm btn-success"
												aria-hidden="true" onclick="addRow()"
												title="<s:text name="Ok" />">
												<i class="fa fa-check"></i>
											</button>
											<button type="button" class="btn btn-sm btn-success"
												aria-hidden="true" id="edit" onclick="addRow()"
												title="<s:text name="Edit" />">
												<i class="fa fa-check"></i>
											</button>
											<button type="button" class="btn btn-sm btn-danger"
												aria-hidden="true " onclick="resetProductData()"
												title="<s:text name="Cancel" />">
												<i class="glyphicon glyphicon-remove-sign"></i>
											</button>

										</td>
									</table>
								</td>

							</div>





						</div>
					</div>
				</div>
	</div>
				</div>
				

			<div class="service-content-section">

				<div class="appContentWrapper">
					<div class="formContainerWrapper">
						<h2>
	<s:property
								value="%{getLocaleProperty('info.coldStorage')}" />
						</h2>
						

				<table class="table table-bordered aspect-detail"
					id="procurementDetailTable" style="width: 100%; margin-top: 30px;">
					<thead>
						<tr>
							<th><s:property value="%{getLocaleProperty('product')}" /></th>
							<th><s:property value="%{getLocaleProperty('variety')}" /></th>
						<%-- 	<th class="procGrade"><s:property value="%{getLocaleProperty('procGrade')}" /></th>		 --%>					
							<th><s:property value="%{getLocaleProperty('maxBayHold')}" /></th>
							<th><s:property value="%{getLocaleProperty('blockName')}" /></th>
							<th><s:property value="%{getLocaleProperty('floorName')}" /></th>
							<th><s:property value="%{getLocaleProperty('bayNumber')}" /></th>
							<th><s:property value="%{getLocaleProperty('availableQty')}" /></th>
							<th><s:property value="%{getLocaleProperty('noofBags')}" /></th>
							<th><s:property value="%{getLocaleProperty('quantitykg')}" /></th>
							<th style="text-align: center"><s:property value="%{getLocaleProperty('action')}" /></th>
						</tr>
					</thead>
					<tbody id="coldStorageEntryDetailContent">
					</tbody>
					<tfoot>
						<tr>
							<th style="text-align: right"></th>
							<th style="text-align: right"></th>
							<!-- <th style="text-align: right"></th> -->
							<th style="text-align: right"></th>
							<th style="text-align: right"></th>
							<th style="text-align: right"></th>
							<th style="text-align: right"></th>
							<th style="text-align: right"><s:property value="%{getLocaleProperty('total')}" /></th>
							<th style="text-align: right"><div id="toalNoOfBagsLabel">0.000</div></th>
							<th style="text-align: right"><div id="totalQtyLabel" />0.00</th>							
							<th style="text-align: right"></th>
						</tr>

					</tfoot>
				</table>
				</div></div></div>
		<p></p>
				<div class="yui-skin-sam " id="loadList" style="display: block">
			<sec:authorize ifAllGranted="service.coldStorageEntryService.create">
				<span id="save" class=""><span class="first-child">
						<button type="button" onclick="saveColdStorageEntry()"
							class="save-btn btn btn-success" id="sucessbtn">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</sec:authorize>
			<span class="" style="cursor: pointer;"><span
				class="first-child"><a id="cancelBtnId" onclick="onCancel();"
					class="cancel-btn btn btn-sts"> <font color="#FFFFFF"> <s:text
								name="cancel.button" />
					</font>
				</a></span></span>
		</div>

		</div>
		</div>


		<!--end decorator body -->




		<button type="button" id="enableModal"
			class="hide addBankInfo slide_open btn btn-sm btn-success"
			data-toggle="modal" data-target="#slideModal" data-backdrop="static"
			data-keyboard="false">
			<i class="fa fa-plus" aria-hidden="true"></i>
		</button>



		<div id="slideModal" class="modal fade" role="dialog">
			<div class="modal-dialog modal-sm">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" id="model-close-edu-btn" class="close hide"
							data-dismiss="modal">&times;</button>
						<h4 class="modal-title">
							<s:property
								value="%{getLocaleProperty('info.coldStorageEntry')}" />
						</h4>
					</div>
					<div class="modal-body">
						<b><p style="text-align: center; font-size: 120%;">
								<s:property value="%{getLocaleProperty('coldStorageEntrySucess')}" />
							</p></b>
						<%-- 	<p style="text-align: center;" id="receiptNumber"><s:text name="receiptNumber"/></p> --%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default"
							onclick="buttonEdcCancel()">
							<s:text name="close" />
						</button>
					</div>
				</div>

			</div>

		</div>

	</s:form>
	<script>
	
		var rowCounter=0;
		var enableSupplier = "<s:property value='enableSupplier'/>";
		var enableTraceability = "<s:property value='enableTraceability'/>";
		var enableBuyer="<s:property value='enableBuyer'/>";
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		
		$("#warehouse").val("");

		
		$(document).ready(function(){
			 $('.select2').select2();
			var test='<s:property value="%{getGeneralDateFormat()}" />';
			 $.fn.datepicker.defaults.format = test.toLowerCase();
			$('#date').datepicker('setDate', new Date());		
			jQuery("#edit").hide();
			jQuery("#maxBayHold").text("0");
			jQuery("#avaliableQty").val("0.00");
			
			 if(tenantId == "griffith"){
				 $("#bondNoDiv").hide();
			 }
	
		});
		
		function listVillage(value) {
			if (!isEmpty(value)) {
				$.post("coldStorageEntry_populateVillage", {
					selectedCity : value
				}, function(data) {
					insertOptions("village", $.parseJSON(data));
				});
			}
		}
		
function listColdStorageName(value) {

	$("#coldStorageName").val('').trigger('change');
	jQuery("#maxBayHold").text("0");		
	
	if (!isEmpty(value)) {
				$.post("coldStorageEntry_populateColdStorageName", {
					selectedWarehouse : value
				}, function(data) {
					insertOptions("coldStorageName", $.parseJSON(data));
				});
			}
		}
function getMaxBinHold(val){
	jQuery("#maxBayHold").text("0");
	var selectedWarehouse = $("#warehouse option:selected").val();
	if(!isEmpty(val)){
		$.post("coldStorageEntry_getMaxBinHold", {selectedWarehouse : selectedWarehouse,selectedColdStorageName :val}, function(data) {
			var jsonData = $.parseJSON(data);
			$.each(jsonData, function(index, value) {
				if(value.id=="maxBayHold"){
					document.getElementById("maxBayHold").innerHTML=value.name;
				}
			});
		});
	}
}

function validateAvailableQty(val){
	jQuery("#validateError").text("");
	var maxBayHold = $("#maxBayHold").text();
	if(!isEmpty(val) && !isEmpty(maxBayHold) && maxBayHold!=null){
			if(val > parseFloat(maxBayHold)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('invalidQty.maxBayHold')}" />');
				return false;
			}
		
	}
	
}
function validateBatchNo(val){
	jQuery("#validateError").text("");
	var selectedFarmer;
	var batchNo;
	selectedFarmer = $("#farmer").val();
	batchNo=$("#batchNo").val();
	
	if(isEmpty(selectedFarmer)){
		selectedFarmer = $("#farmer option:selected").val();
	}
	
	//lalert(batchNo);
	if(!isEmpty(batchNo)){
		$.post("coldStorageEntry_validateBatchNo", {selectedFarmer : selectedFarmer,batchNo :batchNo}, function(data) {
			var jsonData = $.parseJSON(data);
			$.each(jsonData, function(index, value) {
				if(value.id=="farmer"){
					if(value.name=='0'){
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('batchNoexist')}" />');	
					
					jQuery("#sucessbtn").prop('disabled', true);
					return false;
					}else{
						jQuery("#sucessbtn").prop('disabled', false);
					}
				}
			});
		});
	}
}

function calculateAvailableQty(val){
	var selectedWarehouse = $("#warehouse option:selected").val();
	var selectedColdStorageName = $("#coldStorageName option:selected").val();
	var selectedProduct = $("#procurementProductList option:selected").val();
	var selectedBlockName = $("#blockName option:selected").val();
	var selectedFloorName = $("#floorName option:selected").val();
	var selectedBayNumber = $("#bayNumber option:selected").val();
	//var selectedBatchNo = $("#batchNo").val();
	
	if(!isEmpty(val)){
		$.post("coldStorageEntry_populateAvailableQty", {
			selectedWarehouse : selectedWarehouse,
			selectedColdStorageName : selectedColdStorageName,
			selectedProduct : selectedProduct,
			selectedBlockName : selectedBlockName,
			selectedFloorName : selectedFloorName,
			selectedBayNumber : val
			//selectedBatchNo : selectedBatchNo
		}, function(data) {
		
			if(data!=null && data!=""){
				document.getElementById("availableQty").innerHTML=parseInt(data);
			 }
			else{
				document.getElementById("availableQty").innerHTML=0.00;
			}
		});
	}
	
}
		function listFarmer(value) {
		
				if (!isEmpty(value)) {
					$.post("coldStorageEntry_populateFarmer", {
						selectedVillage : value
					}, function(data) {
						insertOptions("farmer",$.parseJSON(data));
					});
				}
			
		}
		

function listProVarierty(call) {
			var selectedPro = call.value;
			if(!isEmpty(selectedPro)){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "coldStorageEntry_populateVariety.action",
			        data: {selectedProduct : selectedPro},
			        success: function(result) {
			        	insertOptions("varietyList", $.parseJSON(result));
			        }
				});
			}
			listGrade(jQuery("#varietyList").val());
			resetSelect2();
		}

		
function listGrade(call) {
			var selectedVariety = call;
			if(!isEmpty(selectedVariety)&&selectedVariety!="0"){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "coldStorageEntry_populateGrade.action",
			        data: {selectedVariety : selectedVariety},
			        success: function(result) {
			        	insertOptions("gradeList", $.parseJSON(result));
			        }
				});
			}
			resetSelect2();
		}
		
		
		function isNumber(evt) {
		    evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		        return false;
		    }
		    return true;
		}
		
		
		/**Product related Functions*/
		function addRow(){
			/**Validation Part*/			
			jQuery("#validateError").text("");
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var maxBayHold=jQuery("#maxBayHold").text();
			var blockName=jQuery("#blockName").val();
			var floorName = jQuery("#floorName").val();
			var bayNumber = jQuery("#bayNumber").val();
			var availableQty=jQuery("#availableQty").text();
			var noOfBags=jQuery("#noOfBagsTxt").val();
			var quantity=jQuery("#quantity").val();
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}/* else if(isEmpty(grade)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			} */else if(isEmpty(blockName)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.blockName')}" />');				
				return false;
			}else if(isEmpty(floorName)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.floorName')}" />');				
				return false;
			}else if(isEmpty(bayNumber)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.bayNumber')}" />');				
				return false;
			}else if(isEmpty(noOfBags)){
				netWeightKg="0";
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.noOfBags')}" />');				
				return false;
			}else if(isEmpty(quantity)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.quantity')}" />');				
				return false;
			}	
			/* 
			if(parseFloat(maxBayHold)<=parseFloat(quantity)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.quantity')}" />');
				return false;
			} */
			
			/* if(parseFloat(availableQty)<parseFloat(quantity)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('invalid.quantity')}" />');
				return false;
			} */
			 if(parseFloat(maxBayHold)<(parseFloat(availableQty) + parseFloat(quantity))){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.quantity')}" />');
				return false;
			} 
			
			if(!validateQuantity(blockName,floorName,bayNumber,quantity)){
				
			
			if(!checkGradeAndBayNumberExists(variety,bayNumber)){
			
				var tableRow="<tr id=row"+(++rowCounter)+">";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";			
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				tableRow+="<td class='hide gradeId'>"+grade+"</td>";
				/* tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>"; */
				tableRow+="<td class='maxBayHold textAlignRight'>"+maxBayHold+"</td>";
				
				tableRow+="<td class='hide blockName'>"+blockName+"</td>";
				tableRow+="<td>"+jQuery("#blockName option:selected").text()+"</td>";
				
				tableRow+="<td class='hide floorName'>"+floorName+"</td>";
				tableRow+="<td>"+jQuery("#floorName option:selected").text()+"</td>";
				
				tableRow+="<td class='hide bayNum'>"+bayNumber+"</td>";
				tableRow+="<td>"+jQuery("#bayNumber option:selected").text()+"</td>";
				
				tableRow+="<td class='availableQty textAlignRight'>"+availableQty+"</td>";
				tableRow+="<td class='noOfBags textAlignRight'>"+noOfBags+"</td>";
				tableRow+="<td class='quantity textAlignRight'>"+parseFloat(quantity).toFixed(2)+"</td>";
				tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
				tableRow+="</tr>";
				console.log(tableRow);
				jQuery("#coldStorageEntryDetailContent").append(tableRow);
				resetProductData();
				updateTableFooter();
			} 
			
		}
		}
		function editRow(rowCounter){
			var id="#row"+rowCounter;
			
			$.each(jQuery(id), function(index, value) {
				var selectedProduct = jQuery(this).find(".productId").text();
				var selectedVariety = jQuery(this).find(".varietyId").text();
				var selectedGrade = jQuery(this).find(".gradeId").text();
				
				var selectedBlockName = jQuery(this).find(".blockName").text();
				var selectedFloorName = jQuery(this).find(".floorName").text();
				var selectedBayNumber = jQuery(this).find(".bayNum").text();
			
				jQuery("#procurementProductList").val(selectedProduct);
				callTrigger("procurementProductList");
				
				jQuery("#varietyList").val(selectedVariety);
				callTrigger("varietyList");
				
				 jQuery("#gradeList").val(selectedGrade);
				callTrigger("gradeList"); 
				
				jQuery("#blockName").val(selectedBlockName);
				callTrigger("blockName");
				
				jQuery("#floorName").val(selectedFloorName);
				callTrigger("floorName");
				
				jQuery("#bayNumber").val(selectedBayNumber);
				callTrigger("bayNumber");
								
				jQuery("#maxBayHold").text(jQuery(this).find(".maxBayHold").text());				
				jQuery("#availableQty").val(jQuery(this).find(".availableQty ").text());
				jQuery("#noOfBagsTxt").val(jQuery(this).find(".noOfBags").text());
				jQuery("#quantity").val(jQuery(this).find(".quantity").text());
				
				resetSelect2();
			});
			jQuery("#add").hide();
			jQuery("#edit").show();
			$("#edit").attr("onclick","editProduct("+rowCounter+")");
			 
		}
		
		function editProduct(index){
			
			var id="#row"+index;
			jQuery(id).empty();			
			jQuery("#validateError").text("");		
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var maxBayHold=jQuery("#maxBayHold").text();
			var blockName=jQuery("#blockName").val();
			var floorName = jQuery("#floorName").val();
			var bayNumber = jQuery("#bayNumber").val();
			var availableQty=jQuery("#availableQty").text();
			var noOfBags=jQuery("#noOfBagsTxt").val();
			var quantity=jQuery("#quantity").val();
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}/* else if(isEmpty(grade)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			} */else if(isEmpty(blockName)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.blockName')}" />');				
				return false;
			}else if(isEmpty(floorName)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.floorName')}" />');				
				return false;
			}else if(isEmpty(bayNumber)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.bayNumber')}" />');				
				return false;
			}
			
			if(isEmpty(noOfBags)){
				netWeightKg="0";
			}	
			
			if(parseFloat(maxBayHold)<=parseFloat(noOfBagsTxt)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.maxBayHold')}" />');
				return false;
			}
			
			if(!checkGradeAndBayNumberExists(variety,bayNumber)){
				
				var tableRow="";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";			
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				tableRow+="<td class='hide gradeId'>"+grade+"</td>";
				/* tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>"; */
				tableRow+="<td class='maxBayHold textAlignRight'>"+maxBayHold+"</td>";
				
				tableRow+="<td class='hide blockName'>"+blockName+"</td>";
				tableRow+="<td>"+jQuery("#blockName option:selected").text()+"</td>";
				
				tableRow+="<td class='hide floorName'>"+floorName+"</td>";
				tableRow+="<td>"+jQuery("#floorName option:selected").text()+"</td>";
				
				tableRow+="<td class='hide bayNum'>"+bayNumber+"</td>";
				tableRow+="<td>"+jQuery("#bayNumber option:selected").text()+"</td>";
				
				tableRow+="<td class='availableQty textAlignRight'>"+availableQty+"</td>";
				tableRow+="<td class='noOfBags textAlignRight'>"+noOfBags+"</td>";
				tableRow+="<td class='quantity textAlignRight'>"+parseFloat(quantity).toFixed(2)+"</td>";
				tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
				tableRow+="</tr>";
				console.log(tableRow);
				//jQuery("#coldStorageEntryDetailContent").append(tableRow);
				jQuery(id).append(tableRow);
				resetProductData();
				updateTableFooter();
				jQuery("#add").show();
				jQuery("#edit").hide();
			} 
			
		}
		
		function callTrigger(id){
			$("#"+id).trigger("change");
		}
		
		function deleteProduct(rowCounter){
			var id="#row"+rowCounter;
			jQuery(id).remove();
			updateTableFooter();
		}
		
		function checkGradeAndBayNumberExists(varietyId,bayNumber){
			//alert(gradeId);
			var returnVal = false;
			
				var tableBody = jQuery("#coldStorageEntryDetailContent tr");
				
				$.each(tableBody, function(index, value) {
					var variety = jQuery(this).find(".varietyId").text();
					var bayCode = jQuery(this).find(".bayNum").text();
					//alert(grade);
					if(variety==varietyId && bayCode==bayNumber){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}
				});
			
			return returnVal;	
		}
		
		function validateQuantity(blockName,floorName,bayNum,quantity){
			
			var returnVal = false;
			var totalQty='0.00';
			var maxBayHold=jQuery("#maxBayHold").text();
			var totalQtyLabel=jQuery("#totalQtyLabel").text();
			
			var tableBody = jQuery("#coldStorageEntryDetailContent tr");
			
			$.each(tableBody, function(index, value) {
				var block = jQuery(this).find(".blockName").text();
				var floor = jQuery(this).find(".floorName").text();
				var bay = jQuery(this).find(".bayNum").text();
				if(block==blockName && floor==floorName && bay==bayNum){
					totalQty=parseFloat(totalQtyLabel)+parseFloat(quantity);
					
				}
				
			});
			//alert(maxBayHold+"***"+totalQty)
			if(parseFloat(maxBayHold)<parseFloat(totalQty)){
				alert("Quantity Should be less than Max Bay Hold");
				resetProductData();
				returnVal=true 
			}
		return returnVal;	
			
		}
		function updateTableFooter(){
			var tableBody = jQuery("#coldStorageEntryDetailContent tr");			
			var totalNoOfBags=0.0;		
			var totalQty=0.0;
			
			$.each(tableBody, function(index, value) {			
				totalNoOfBags+=getFormattedFloatValue(jQuery(this).find(".noOfBags").text());			
				totalQty+=getFormattedFloatValue(jQuery(this).find(".quantity").text());
				
			});
			//alert(totalNoOfBags +"****"+ totalQty);
			$("#toalNoOfBagsLabel").text(totalNoOfBags);			
			$("#totalQtyLabel").text(totalQty.toFixed(2));
		}
		
		function getFormattedFloatValue(n){
			if(!isNaN(n)){
				return parseFloat(n);
			}else{
				return parseFloat(0);
			}
		}
		
		function resetProductData(){
			jQuery("validatebatchNoError").text("");
			//jQuery("#procurementProductList").val("");
			//jQuery("#varietyList").val("");
			//jQuery("#gradeList").val("");			
			//jQuery("#maxBayHold").text("0.00");
			jQuery("#blockName").val("");
			jQuery("#floorName").val("");
			jQuery("#bayNumber").val("");			
			jQuery("#availableQty").text("0.00");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#quantity").val("");
			
			resetSelect2();
			
			jQuery("#add").show();
			jQuery("#edit").hide();
		}
		function resetTableData(){
			//jQuery("#varietyList").val("");
			//jQuery("#gradeList").val("");			
			jQuery("#pricePerUnitLabel").text("0.00");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			
		
		}
		
function saveColdStorageEntry(){
			var error = "";
		
		$("#sucessbtn").prop('disabled', true);
			var date=jQuery("#date").val();
			var farmer;
			 if(tenantId != "griffith"){
				  farmer = jQuery("#farmer").val();
			 }else{
				  farmer = jQuery("#farmerlabelname").data("farmer-id");
				  if(isEmpty($("#bondStatus").val())){
					  error = '<s:property value="%{getLocaleProperty('empty.bondstatus')}" />';
				  }
				  
			 }
			 
			var warehouse=jQuery("#warehouse").val();
			var coldStorageName=jQuery("#coldStorageName").val();
			var batchNo=jQuery("#batchNo").val();
			var bondNo =jQuery("#bondNo").val();
			var totalNoOfBag=jQuery("#toalNoOfBagsLabel").text();
			var totalQtyLabel=jQuery("#totalQtyLabel").text();
			var roundOfHarvesting=jQuery("#roundOfHarvest").val();
			var bondStatus=jQuery("#bondStatus").val();
			var coldStorageEntryInfoArray =  buildColdStorageEntryInfoArray();
			//alert(coldStorageEntryInfoArray);
			if(isEmpty(coldStorageEntryInfoArray)){
				error = '<s:property value="%{getLocaleProperty('noRecordFound')}" />';
			}
			
			if(isEmpty(date)){
				error = '<s:property value="%{getLocaleProperty('empty.date')}" />';
				jQuery("#date").focus();
			}else if(isEmpty(farmer)){
				error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';
				jQuery("#farmer").focus();
			}else if(isEmpty(warehouse)){
				error = '<s:property value="%{getLocaleProperty('empty.warehouse')}"/>';
				jQuery("#warehouse").focus();
			}else if(isEmpty(coldStorageName)){
				error ='<s:property value="%{getLocaleProperty('empty.coldStorageName')}" />';
				jQuery("#coldStorageName").focus();
			}else if(isEmpty(batchNo)){
				error ='<s:property value="%{getLocaleProperty('empty.batchNo')}" />';
				jQuery("#batchNo").focus();
			}else if(isEmpty(roundOfHarvesting)){
				error ='<s:property value="%{getLocaleProperty('empty.roundOfHarvesting')}" />';
				jQuery("#roundOfHarvesting").focus();
			} 
			else if(bondStatus==2 && isEmpty(bondNo)){
				error ='<s:property value="%{getLocaleProperty('empty.bondNo')}" />';
				jQuery("#bondNo").focus();
			} 
			if(!isEmpty(error)){
				jQuery("#validateError").text(error);
				$("#sucessbtn").prop('disabled', false);
				return false;
			}		
			
			var dataa = new FormData();
			if($('#imgFile')[0].files[0] != undefined){
				var file = $('#imgFile')[0].files[0];
				dataa.append('imgFile', file);
			}
			
			dataa.append('selectedFarmer', farmer);
			dataa.append('selectedWarehouse', warehouse);
			dataa.append('coldStorageName', coldStorageName);
			dataa.append('selectedDate', date);
			dataa.append('procurementDate', date.toString());
			dataa.append('batchNo', batchNo);
			dataa.append('bondNo', bondNo);
			dataa.append('productTotalString', coldStorageEntryInfoArray);
			dataa.append('totalNoOfBag', totalNoOfBag);
			dataa.append('totalQtyLabel', totalQtyLabel);
			dataa.append('roundOfHarvesting', roundOfHarvesting);
			dataa.append('bondStatus', $("#bondStatus").val());
			
			
		 	/* var dataa = {
				selectedFarmer:farmer,
				selectedWarehouse:warehouse,
				coldStorageName:coldStorageName,
				selectedDate:date,
				procurementDate:date.toString(),
				batchNo:batchNo,
				bondNo:bondNo,				
				productTotalString:coldStorageEntryInfoArray,
				totalNoOfBag:totalNoOfBag,
				totalQtyLabel:totalQtyLabel,
				roundOfHarvesting:roundOfHarvesting,
				bondStatus:$("#bondStatus").val()
		
				
			} 
			 */
			
			$.ajax({
			        url: 'coldStorageEntry_populateColdStorageEntry.action',
			        enctype: 'multipart/form-data',
			        processData: false,
			        contentType: false,
			        type: 'post',
			        dataType: 'json',
			        data: dataa,
			        success: function(data) {
			        	document.getElementById("enableModal").click();	
			        },
			        error: function(data) {
			            alert("Some Error Occured , Please Try again");
			            $("#myButton").removeAttr("disabled");
			        }

			    }); 
		
			
		}
		
		function enablePopup(){
			
		}
		
		function buildColdStorageEntryInfoArray(){
			
			var tableBody = jQuery("#coldStorageEntryDetailContent tr");
			var productInfo="";
			
			$.each(tableBody, function(index, value) {
				productInfo+=jQuery(this).find(".varietyId").text().trim(); //0
				
				productInfo+="#"+jQuery(this).find(".blockName").text().trim(); //1
				
				productInfo+="#"+jQuery(this).find(".floorName").text().trim(); //2				
				
				productInfo+="#"+jQuery(this).find(".bayNum").text().trim(); //3
				
				productInfo+="#"+jQuery(this).find(".availableQty").text().trim(); //4
				
				productInfo+="#"+jQuery(this).find(".noOfBags").text().trim(); //5
				
				productInfo+="#"+jQuery(this).find(".quantity").text().trim()+"@"; //6
				//alert(productInfo);
				
				
			});
			//alert(productInfo);
			return productInfo;
		}
		
		function resetSelect2(){
			$(".select2").select2();
		}
		
		function buttonEdcCancel(){
			//refreshPopup();
			document.getElementById("model-close-edu-btn").click();	
			if(tenantId!="fincocoa"  && tenantId!="movcd" && tenantId!="pgss"){
			window.location.href="coldStorageEntry_list.action";
	     }else{
	    	 window.location.href="coldStorageEntry_create.action";
	    	 }
	     }
		
		
		
		function onCancel() {
			if(tenantId!="fincocoa"  && tenantId!="movcd" && tenantId!="pgss" ){
				window.location.href="coldStorageEntry_list.action";
		      }else{
		    	 window.location.href="coldStorageEntry_create.action";
		    	 } 
		}
		
		 function isDecimal(evt) {
	    		
	    	 evt = (evt) ? evt : window.event;
	    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	    	        return false;
	    	    }
	    	    return true;
	    }

		 function populateData(value){
			 $("#coldStorageEntryDetailContent").empty();
			 $("#toalNoOfBagsLabel").text(0);			
				$("#totalQtyLabel").text(0.00);
			 if (!isEmpty(value)) {
					$.post("coldStorageEntry_populateDataByFarmCropId", {
						dynamic_field_value_id : value
					}, function(data) {
						
						if(data.farmer != null){
							$("#farmerlabelname").text(data.farmer.name);
							$("#farmerlabelname").attr({
			            		"data-farmer-id" : data.farmer.id
			           		 });
						}
						if(data.roundOfHarvesting != null){
							insertOptionsWithJsonObject("roundOfHarvest",data.roundOfHarvesting);
						}
						if(data.product != null){
							insertOptionsWithJsonObject("procurementProductList",data.product);
						}
						if(data.variety != null){
							insertOptionsWithJsonObject("varietyList", data.variety);
							$("#varietyList").val(data.variety.id).trigger("change"); 
						}
						
						
						
						
					});
				}
		 }
		 
		 function enableBondNo(val){
			 if(tenantId == "griffith"){
				 if(val == "2"){
					 $("#bondNoDiv").show();
				 }else{
					 $("#bondNoDiv").hide();
				 }
				
			 }
		 }
		 
		 function insertOptionsWithJsonObject(ctrlName, json) {
		        document.getElementById(ctrlName).length = 0;
		        addOption(document.getElementById(ctrlName), json.name, json.id);
		       	var id="#"+ctrlName;
		        jQuery(id).select2().attr('disabled','disabled');
		    }
		 
		   function insertOptions(ctrlName, jsonArr) {
		        document.getElementById(ctrlName).length = 0;
		       
		       if(jsonArr.length > 1){
		    	   addOption(document.getElementById(ctrlName), "Select", "");
		       }
		        for (var i = 0; i < jsonArr.length; i++) {
		            addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
		            if(jsonArr.length == 1){
		    		$("#"+ctrlName).val(jsonArr[i].id).trigger("change"); 
		            }
		        }
		       
		        var id="#"+ctrlName;
		       jQuery(id).select2();
		    }
		 
		   function checkImgHeightAndWidth(val) {

		        var _URL = window.URL || window.webkitURL;
		        var img;
		        var file = document.getElementById('imgFile').files[0];
		        var filename=document.getElementById('imgFile').value;
		        var fileExt=filename.split('.').pop();
		        
		        if(file != undefined){
					if(fileExt=='pdf' ){
						img = new Image();
			            img.onload = function () {
			                imgHeight = this.height;
			                imgWidth = this.width;
			            };
			            img.src = _URL.createObjectURL(file);
						
					}else{
						$('#imgFile').val("");
						alert("Kindly upload pdf document only.")
					}

		            
		        }
		    }
		   
	</script>
</body>