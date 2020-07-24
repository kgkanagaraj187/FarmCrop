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
									<span class="manadatory">*</span>
									<s:text name="reqd.field" />
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
				<div class="flexiWrapper filterControls">
				
				
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('date')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield id="date" data-provide="datepicker"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-end-date="0d" readonly="true"
								cssClass="col-sm-3 form-control " />
						</div>
					</div>
					
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="selectedWarehouseId" list="listWarehouse"
								headerKey="" headerValue="%{getText('txt.select')}"
								lisykey="key" listValue="value" id="warehouse"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
				<div class="flexi flexi10" id="supplierDiv">
						<label for="txt"><s:property
								value="%{getLocaleProperty('supplier')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="supplier" list="masterTypeList" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="loadSupplier(this.value);reloadTable();"
								id="masterType" cssClass="form-control input-sm select2" />
						</div>
					</div>
				<div class="flexi flexi10 isSupplier">

								<label for="txt"><s:property
										value="%{getLocaleProperty('isSupplier')}" /><span
									class="manadatory">*</span></label>
								<div class="">
									<s:radio list="regType" listKey="key" listValue="value"
										name="isRegisteredSupplier" id="isRegisteredSupplierId"
										style="color: black;" />
								</div>

							</div>
				
					
					
					<div class="flexi flexi10 unregSupplier">

								<label for="txt"><s:property
										value="%{getLocaleProperty('supplierName')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="supplierName" name="supplierId" maxlength="35"
										cssClass="col-sm-3 form-control " />
								</div>

							</div>
							
							<div class="flexi flexi10 unregSupplier">
								<label for="txt"><s:property
										value="%{getLocaleProperty('supplierMobileNumber')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="supplierMobileNoInput" name="mobileNo" maxlength="10"
										cssClass="col-sm-3 form-control"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
							
							<div class="flexi flexi10">
								<label for="txt"><s:property
										value="%{getLocaleProperty('invoiceNo')}" /></label>
								<div class="form-element">
									<s:textfield id="invoiceNo" name="invoiceNo" maxlength="10"
										cssClass="col-sm-3 form-control"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
				
					<%-- --%>
					<p></p>
					

					<div class="flexi flexi10 supplierTypeDiv">

						<label id="supplierTypeLabel"></label><span class="manadatory">*</span>
						<div class="form-element">
							<s:select name="masterType" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="typeList"
								cssClass="form-control input-sm select2" />
						</div>

					</div>
						
					
				<s:if test="currentTenantId=='wilmar'">
					<div class="flexi flexi10 traderDiv">

						<label id="traderLabel"></label><span class="manadatory">*</span>
						<div class="form-element">
							<s:select name="trader" list="traderList" headerKey=""
								headerValue="%{getText('txt.select')}" id="traderList"
								cssClass="form-control input-sm select2" />
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
						<s:property value="%{getLocaleProperty('info.product')}" />
						</h2>
						<div class="flexiWrapper filterControls">

						
							 <div class="flexi flexi10 city">
						<label for="txt"><s:property
								value="%{getLocaleProperty('profile.location.municipality')}" /></label>
						<div class="form-element">
							<s:select name="cityId" list="listMunicipality" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listVillage(this.value);" id="city"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="flexi flexi10 village">
						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="village"
								cssClass="form-control input-sm select2" name="village"
								onchange="listFarmer(this.value)" />
						</div>
					</div>

							<div class="flexi flexi10 farmerDiv farmerSelectionDiv">

								<label for="txt"><s:property
										value="%{getLocaleProperty('isFarmer')}" /><span
									class="manadatory">*</span></label>
								<div class="">
									<s:radio list="regType" listKey="key" listValue="value"
										name="isRegisteredFarmer" id="isRegisteredFarmerId"
										style="color: black;" />
								</div>

							</div>
							<div class="flexi flexi10 regFarmer ">

								<label for="txt"><s:property
										value="%{getLocaleProperty('procFarmer')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" name="farmerId"
										id="farmer" cssClass="form-control input-sm select2"
										onchange="loadFarmerAccountBalance();" />
								</div>

							</div>

							<div class="flexi flexi10 unregFarmer">

								<label for="txt"><s:property
										value="%{getLocaleProperty('farmerName')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="farmerName" name="farmerId" maxlength="35"
										cssClass="col-sm-3 form-control " />
								</div>

							</div>
							
							<div class="flexi flexi10 unregFarmer">
								<label for="txt"><s:property
										value="%{getLocaleProperty('farmer.mobileNumber')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="mobileNoInput" name="mobileNo" maxlength="10"
										cssClass="col-sm-3 form-control"
										onkeypress="return isNumber(event)" />
								</div>
							</div>

			

						</div>
					</div>
				</div>


				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<div class="flexiWrapper filterControls">
							<div class="flexi flexi10 cropType">
							<label><s:property
									value="%{getLocaleProperty('cropType')}" /></label>
							<div class="">
								<s:radio list="cTypes" listKey="key" listValue="value"
									name="selectedCropType" theme="simple" id="ctype" />
							</div>
						</div>
								<div class="flexi flexi10">
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
							<div class="flexi flexi10">
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
							<div class="flexi flexi10 procGrade">
								<label for="txt"><s:property
										value="%{getLocaleProperty('procGrade')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="gradeList"
										cssClass="form-control input-sm select2"
										onchange="listPrice(this)" />
								</div>
							</div>
							<s:if test="enableTraceability==1">
								<div class="flexi flexi10">
									<label for="txt"><s:property
											value="%{getLocaleProperty('procurement.batchNo')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:textfield name="selectedBatchNo" id="batchNo" maxlength="6" />
										<div id="validatebatchNoError"
											style="text-align: center; padding: 5px 0 0 0; color: red; font-weight: normal;"></div>
									</div>
								</div>
							</s:if>
						
								<div class="flexi flexi10 unit">
									<label for="txt"><s:property
											value="%{getLocaleProperty('unit')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select id="unitList" name="unitList" list="listUom"
											headerKey="" listKey="code" listValue="name"
											headerValue="%{getText('txt.select')}"
											cssClass="form-control select2" />
									</div>
								</div>
							

							

							<div class="flexi flexi10 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfBags')}" /></label>
								<div class="form-element">
									<s:textfield id="noOfBagsTxt" maxlength="3"
										cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>

							<div class="flexi flexi10 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('grossWeights')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="grossWeightKgTxt" maxlength="8"
											cssStyle="width:90%" onkeypress="return isNumber(event)"
											onchange="calcNetWeight()" />
										<div>.</div>
										<s:textfield id="grossWeightGmTxt" maxlength="3"
											cssStyle="width:90%" onkeypress="return isNumber(event)"
											onchange="calcNetWeight()" />
									</div>
								</div>
							</div>
							<div class="flexi flexi10 pricePerUnit">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('pricePerUnit')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									
										<s:textfield id="pricePerUnitLabel" maxlength="7"
										cssClass="form-control input-sm"
										onkeypress="return isDecimal(event)" />
								</div>
							</div>
							<div class="flexi flexi10 ">
								<label for="txt"><s:property value="%{getLocaleProperty('action')}" /></label>
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
												<i class="fa fa-pencil"></i>
											</button>
											<button type="button" class="btn btn-sm btn-danger"
												aria-hidden="true " onclick="resetProductData()"
												title="<s:text name="Cancel" />">
												<i class="fa fa-trash"></i>
											</button>

										</td>
									</table>
								</td>

							</div>

						</div>


						<table class="table table-bordered aspect-detail"
							id="procurementDetailTable"
							style="width: 100%; margin-top: 30px;">
							<thead>
								<tr>
								<th><s:property value="%{getLocaleProperty('farmer')}" /></th>
									<th><s:property value="%{getLocaleProperty('product')}" /></th>
									<th><s:property value="%{getLocaleProperty('variety')}" /></th>
									
										<th class="procGrade"><s:property
												value="%{getLocaleProperty('procGrade')}" /></th>
										<s:if test="enableTraceability==1">
											<th><s:property
													value="%{getLocaleProperty('procurement.batchNo')}" /></th>
										</s:if>
										<th width="8%"><s:property
													value="%{getLocaleProperty('unit')}" /></th>
													
										<th width="8%"><s:property
													value="%{getLocaleProperty('cropType')}" /></th>
													
										<th class="pricePerUnit"><s:property
												value="%{getLocaleProperty('pricePerUnit')}" /></th>
									

									<th><s:property
												value="%{getLocaleProperty('noOfBags')}" /></th>
									<th><s:property
											value="%{getLocaleProperty('grossWeights')}" /></th>
									
										<th><s:property value="%{getLocaleProperty('Sub Total')}" /></th>
									
									<th style="text-align: center"><s:property value="%{getLocaleProperty('action')}" /></th>
								</tr>
							</thead>
							<tbody id="procurementDetailContent">
							</tbody>
							<tfoot>
								<tr>
									<th style="text-align: right"></th>
									<th style="text-align: right"></th>
									<th style="text-align: right"></th>
									<th style="text-align: right"><s:property value="%{getLocaleProperty('total')}" /></th>
									<th style="text-align: right"></th>
									<th style="text-align: right"></th>
										<s:if test="enableTraceability==1">
											<th style="text-align: right"></th>
										</s:if>
										<th style="text-align: right">
											<div id="pricePerUnitTotalLabel">0.00</div>
										</th>
									
									<th style="text-align: right">
										<div id="noOfBagsTotalLabel">0</div>
									</th>
									<th style="text-align: right">
										<div id="grossWeightTotalLabel">0.000</div>
									</th>
									<th style="text-align: right"><div id="subTotalLabel" />0.00</th>
									
									<th></th>
								</tr>

							</tfoot>
						</table>
					</div>
				</div>
				
				
				
				
				
				
				
				
					
			<div class="service-content-wrapper">
			<div class="service-content-section">

				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
						<s:property value="%{getLocaleProperty('info.product')}" />
						</h2>
						<div class="flexiWrapper filterControls">
						
						<div class="flexi flexi10 totalLabourCostDiv">

						<label id="supplierTypeLabel"><s:property
								value="%{getLocaleProperty('totalLabourCost')}" /></label>
						<div class="form-element">
							<s:textfield id="totalLabourCost" name="totalLabourCost" maxlength="15"
										cssClass="col-sm-3 form-control " />
						</div>

					</div>
						<div class="flexi flexi10 transportCostDiv">

						<label id="supplierTypeLabel"><s:property
								value="%{getLocaleProperty('transportCost')}" /></label>
						<div class="form-element">
								<s:textfield id="transportCost" name="transportCost" maxlength="15"
										cssClass="col-sm-3 form-control " />
						</div>

					</div>
					
					
					
					<div class="flexi flexi10 taxAmtDiv">

						<label id="supplierTypeLabel"><s:property
								value="%{getLocaleProperty('taxAmt')}" /></label>
						<div class="form-element">
							<s:textfield id="taxAmt" name="taxAmt" maxlength="15"
										cssClass="col-sm-3 form-control " />
						</div>

					</div>
						<div class="flexi flexi10 otherCostDiv">

						<label id="supplierTypeLabel"><s:property
								value="%{getLocaleProperty('otherCost')}" /></label>
						<div class="form-element">
								<s:textfield id="otherCost" name="otherCost" maxlength="15"
										cssClass="col-sm-3 form-control " />
						</div>

					</div>
					
					
					
			</div>
			</div>
			</div>
			</div>
			</div>
		
		
		
		
				
				<s:if test="currentTenantId!='lalteer'">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper">

							<h2>
								<s:property value="%{getLocaleProperty('info.payment')}" />
							</h2>
							<div class="flexiWrapper filterControls">
								<table class="table table-bordered aspect-detail">
									<td><label for="txt"><s:property
												value="%{getLocaleProperty('enterAmount')}" /></label></td>

									<td class="cashTypeDiv"><input type="text"
										class="fullwidth textAlignRight" name="paymentRupee" value=""
										id="paymentRupee" maxlength="10"
										onkeypress="return isDecimal(event)"
										style="text-align: right; padding-right: 1px; width: 90% !important;"></td>
								</table>
							</div>
							<%-- <div class="flexiWrapper filterControls">
			<s:if test="currentTenantId!='lalteer'">
				<label for="txt"><s:text name="enterAmount" /></label>
				<div class="form-element">
					<div class="button-group-container">
						<s:textfield id="paymentRupee" name="paymentRupee" maxlength="10"
							onkeypress="return isNumber(event)"
							cssStyle="text-align:right;padding-right:1px;width: 150px;
										margin-left: 20px;"
							cssClass="form-control input-md" />
						<div>.</div>
						<s:textfield id="paymentPaise" name="paymentPaise" maxlength="2"
							onkeypress="return isNumber(event)"
							cssStyle="text-align:right;padding-right:1px;width:80px"
							cssClass="form-control input-md" />
					</div>

				</div>

			</s:if>
			</div> --%>
						</div>
					</div>
				</s:if>

				<div class="yui-skin-sam " id="loadList" style="display: block">
					<sec:authorize ifAllGranted="service.supplierProcurementService.create">
						<span id="save" class=""><span class="first-child">
								<button type="button" onclick="saveProcurement()"
									class="save-btn btn btn-success" id="sucessbtn">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span></span>
					</sec:authorize>
					<span class="" style="cursor: pointer;"><span
						class="first-child"><a id="cancelBtnId"
							onclick="onCancel();" class="cancel-btn btn btn-sts"> <font
								color="#FFFFFF"> <s:text name="cancel.button" />
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
								value="%{getLocaleProperty('info.procurementProduct')}" />
						</h4>
					</div>
					<div class="modal-body">
						<b><p style="text-align: center; font-size: 120%;">
								<s:property value="%{getLocaleProperty('procurementSucess')}" />
							</p></b>
						<%-- 	<p style="text-align: center;" id="receiptNumber"><s:text name="receiptNumber"/></p> --%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default"
							onclick="buttonEdcCancel()">
							<s:text name="Close" />
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
		//alert(enableTraceability);
		var tenantId="<s:property value='getCurrentTenantId()'/>";
	       var isKpfBased = '<s:property value="%{getIsKpfBased()}" />';
		$("#warehouse").val("");
		$("#city").val("");
		
		$(document).ready(function(){
			var test='<s:property value="%{getGeneralDateFormat()}" />';
			 $.fn.datepicker.defaults.format = test.toLowerCase();
			$('#date').datepicker('setDate', new Date());
			$('#isRegisteredSupplierId0').attr('checked', true);
			$('#isRegisteredFarmerId0').attr('checked', true);
			$('#ctype1').attr('checked', true);
			jQuery("#edit").hide();
			jQuery(".unregFarmer").hide();
			jQuery(".unregSupplier").hide();
			jQuery(".regFarmer").hide();
			jQuery(".city").hide();
			jQuery(".village").hide();			
			jQuery(".isSupplier").hide();
			jQuery(".totalLabourCost").show();
			jQuery(".transportCost").show();
		
			  //$("#").inputmask({"alias": "currency","prefix":" "});
			  
			  jQuery("#paymentRupee").val("0.00");
			  
			  $('input[type=radio][name=isRegisteredSupplier]').change(function() {
					
					if (this.value == '0') {
						jQuery(".farmerSelectionDiv").hide();
						jQuery(".regFarmer").hide();
						jQuery(".city").hide();
						jQuery(".village").hide();
						jQuery(".unregSupplier").hide();				
						jQuery(".supplierTypeDiv").show();						
						jQuery("#SupplierName").val("");
						jQuery("#supplierMobileNoInput").val("");
						$('#isRegisteredSupplierId1').attr('checked', false);
						$('#isRegisteredSupplierId0').attr('checked', true);
					}else{
						$('#isRegisteredSupplierId1').attr('checked', true);
						$('#isRegisteredSupplierId0').attr('checked', false);
						
						jQuery(".supplierTypeDiv").hide();					
						jQuery(".unregSupplier").show();
						jQuery(".city").hide();
						jQuery(".village").hide();
						jQuery(".farmerSelectionDiv").hide();
						jQuery(".regFarmer").hide();
						
						resetProductData();
						//jQuery("#masterType").val("");
						resetSelect2();
					}
				});
			  
			$('input[type=radio][name=isRegisteredFarmer]').change(function() {
				
				if (this.value == '0') {
					jQuery(".unregFarmer").hide();
					jQuery(".city").show();
					jQuery(".village").show();
					jQuery(".regFarmer").show();
					
					jQuery("#farmerName").val("");
					jQuery("#mobileNoInput").val("");
					$('#isRegisteredFarmerId1').attr('checked', false);
					$('#isRegisteredFarmerId0').attr('checked', true);
				}else{
					$('#isRegisteredFarmerId1').attr('checked', true);
					$('#isRegisteredFarmerId0').attr('checked', false);
					jQuery(".unregFarmer").show();
					jQuery(".regFarmer").hide();
					jQuery(".village").hide();
					jQuery(".city").hide();
					jQuery("#farmer").val("");
					resetSelect2();
				}
			});
		
			
			//if(isKpfBased=="1"){
				if(isKpfBased=="1"){
					processSupplierFunctionality();
			} else if(tenantId=="wilmar"){
				processWilmarFunctionality();
			}
			
		});
		
		function processWilmarFunctionality(){
	
			if(enableSupplier=='1'){
				jQuery("#supplierDiv").show();
				jQuery(".supplierTypeDiv").hide();
				jQuery(".farmerDiv").hide();
				jQuery(".regFarmer ").show();
				jQuery(".village").show();
				jQuery(".city").show();
				jQuery(".traderDiv").hide();
				
			}else{
				jQuery(".farmerDiv").show();
				jQuery("#masterType").val('99');
			}
			
		}
		
		
		
		function processSupplierFunctionality(){
			jQuery("#supplierDiv").hide();
			jQuery(".farmerDiv").hide();
			jQuery(".supplierTypeDiv").hide();
			
			
			if(enableSupplier=='1'){
				jQuery("#supplierDiv").show();
			}else{
				jQuery(".farmerDiv").show();
				jQuery("#masterType").val('99');
			}
		}
		
		function listVillage(value) {
			if (!isEmpty(value)) {
				
				$.post("supplierProcurement_populateVillage", {
					selectedCity : value
				}, function(data) {
					
					insertOptions("village", $.parseJSON(data));
				});
			}
		}

		function listFarmer(value) {
			var supplier=jQuery("#masterType").val();
			if(isKpfBased=="1"){
				if(supplier=='99'){
					if (!isEmpty(value)) {
						$.post("supplierProcurement_populateFarmer", {
							selectedVillage : value
						}, function(data) {
							insertOptions("farmer",$.parseJSON(data));
						});
					}
				}
			}else if(tenantId=="wilmar"){
				if (!isEmpty(value)) {
					$.post("supplierProcurement_populateFarmer", {
						selectedVillage : value
					}, function(data) {
						insertOptions("farmer",$.parseJSON(data));
					});
				}
			}
		
			
		}
		function loadFarmerAccountBalance(){
		}

		function listProVarierty(call) {
			var selectedPro = call.value;
			if(!isEmpty(selectedPro)){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "supplierProcurement_populateVariety.action",
			        data: {selectedProduct : selectedPro},
			        success: function(result) {
			        	insertOptions("varietyList", $.parseJSON(result));
			        }
				});
			}
			listGrade(jQuery("#varietyList").val());
			resetSelect2();
		}

		function loadUnit(call){
			
			var selectedPro = call.value;
			
			$.post("supplierProcurement_populateUnit",{selectedProduct : selectedPro},function(data){
				var jsonData = $.parseJSON(data);
				$.each(jsonData, function(index, value) {
					if(value.id=="unit"){
						document.getElementById("productUnit").innerHTML=value.name;
					}
				});
			});
		
		}
		
		function listGrade(call) {
			var selectedVariety = call;
			if(!isEmpty(selectedVariety)&&selectedVariety!="0"){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "supplierProcurement_populateGrade.action",
			        data: {selectedVariety : selectedVariety},
			        success: function(result) {
			        	insertOptions("gradeList", $.parseJSON(result));
			        }
				});
			}
			resetSelect2();
		}
		
		function listPrice(call){
			var selectedGrade = call.value;
			$.post("supplierProcurement_populatePrice", {
				selectedGrade : selectedGrade
			}, function(data) {
				jQuery("#pricePerUnitLabel").val(data);
			});
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
			var para1="";
			var para2="";
			var error = "";
			var farmer="";
			var farmerName=""
			var proUnitVal="";
			var mobileNo="";
			var batchNo="";
			var supplierType=jQuery("#masterType").val();
			var city=jQuery("#city").val();
			var village=jQuery("#village").val();	
			var villageName=jQuery("#village option:selected" ).text();
			var farmerType=jQuery('input[type=radio][name=isRegisteredFarmer]:checked').val();			
			var cType =jQuery('input[type=radio][name=selectedCropType]:checked').val(); 
			var cropType =jQuery('input[type=radio][name=selectedCropType]:checked').text();
			var supplierTypeList=jQuery("#typeList").val();
			var supplierName=jQuery("#supplierName").val();
			var isSupplier=jQuery('input[type=radio][name=isRegisteredSupplier]:checked').val();
			if(isKpfBased=="1"){
				if(supplierType==99 ||supplierType==11){			
					if(farmerType==0){
						farmer=jQuery("#farmer").val();
						farmerName =jQuery("#farmer option:selected" ).text();
						mobileNo="";
					
					}else{
						farmer="";
						farmerName=jQuery("#farmerName").val();
						mobileNo = jQuery("#mobileNoInput").val();
					}
				}else{
					farmer="";
					farmerName="NA";
					mobileNo="";
				}
			}else if(tenantId=="wilmar"){
				farmer=jQuery("#farmer").val();
				farmerName =jQuery("#farmer option:selected" ).text();
				mobileNo="";
			}
			
			
			//var mobileNo = jQuery("#mobileNoInput").val();
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var price = jQuery("#pricePerUnitLabel").val();
			var bags=jQuery("#noOfBagsTxt").val();
			var unit=jQuery( "#unitList option:selected" ).text();
			var uom=jQuery( "#unitList" ).val();
		
				if(cType==undefined || cType==null){
					cropType='NA';
					cType='';
				}else if(cType==0){
					cropType='<s:property value="%{getLocaleProperty('cpType0')}" />';
				}else{
					cropType='<s:property value="%{getLocaleProperty('cpType1')}" />';
				}
				
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}
			else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}
			else if(isEmpty(grade)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			}else if(isEmpty(uom)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />'); 
				return false;
			}else if(isEmpty(price) || price==0){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.price')}" />'); 
				return false;
			}
			else if(isEmpty(netWeightKg)&&isEmpty(netWeightGm)){
				var error = '<s:property value="%{getLocaleProperty('invalid.grossWeight')}" />';
				jQuery("#validateError").text(error);
				return false;
			}
		if(isKpfBased=="1"){
			if(supplierType=="99"||supplierType=="11"){
				 if(supplierType=="99"){
						if(document.getElementById("isRegisteredFarmerId0").checked){						
							if(isEmpty(village)){
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.village')}" />');
								jQuery("#village").focus();
								return false;						
								
						
							}else if(isEmpty(farmer)){
									jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmer')}" />');
									jQuery("#farmer").focus();
									return false;							
									
								}
						}else{
							if(isEmpty(farmerName)){
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmerName')}" />');
								jQuery("#farmerName").focus();
								return false;						
								
							}else if(isEmpty(mobileNo)){							
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('invalid.mobileNo')}" />');
								jQuery("#mobileNoInput").focus();
								return false;							
								
							}
						}
					 
						
					}else{
					
						if(isEmpty(farmer)){
							jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmer')}" />');
							jQuery("#farmer").focus();
							return false;							
							
						}
					}
				
				}
		}else if(tenantId=="wilmar"){
			
			if(isEmpty(village)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.village')}" />');
				jQuery("#village").focus();
				return false;						
				
		
			}else if(isEmpty(farmer)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmer')}" />');
				jQuery("#farmer").focus();
				return false;							
				
			}
		}
			 
	
			if(isEmpty(netWeightKg)){
				netWeightKg="0";
			}
			
			if(isEmpty(netWeightGm)){
				netWeightGm="000";
			}
			
			if(isEmpty(bags)){
				bags="0";
			}
			
			
			
			if(enableTraceability=='1'){
				//alert("AA");
				//var batchNo=document.getElementById("batchNo").value;
				batchNo=jQuery("#batchNo").val();
				}
			if(enableTraceability=='1'){
				if(batchNo==""){
					//document.getElementById("validatebatchNoError").innerHTML='<s:text name="emptyBatchNo"/>';
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('emptyBatchNo')}" />');
					jQuery("#farmer").focus();
					return false;			
				}
			} 
			var netWeight=netWeightKg+"."+netWeightGm;
			
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				return false;
			}
			if(isKpfBased=="1"){
				if(supplierType==99 || supplierType==11){
					if(supplierType==99 && farmerType==0){
						
						para1=farmer;
						para2=grade;
					}else if(supplierType==11){
						
						para1=farmer;
						para2=grade;
					}else{
						
						para1=farmerName;
						para2=grade;
					}
				}else {
					if(isSupplier==0){
						
					para1=supplierTypeList;
					para2=grade;
					}else{
						
						para1=supplierName;
						para2=grade;
					}
				}
			}else if(tenantId=="wilmar"){
				para1=farmer;
				para2=grade;
			}
			
			//alert(villageName);
			if(!checkGradeExists(para1,para2)){
				var tableRow="<tr id=row"+(++rowCounter)+">";
				tableRow+="<td class='hide farmerId'>"+farmer+"</td>";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";
				tableRow+="<td class='hide cType'>"+cType+"</td>";
				tableRow+="<td class='hide city'>"+city+"</td>";
				tableRow+="<td class='hide village'>"+village+"</td>";
				tableRow+="<td class='hide farmerType'>"+farmerType+"</td>";			
				tableRow+="<td class='hide supplierType'>"+supplierType+"</td>";
				tableRow+="<td class='hide supplierTypeList'>"+supplierTypeList+"</td>";
				tableRow+="<td class='hide supplierName'>"+supplierName+"</td>";
				tableRow+="<td class='hide villageName'>"+villageName+"</td>";
				tableRow+="<td class='farmerName textAlignRight'>"+farmerName+"</td>";
				tableRow+="<td class='hide mobileNo'>"+mobileNo+"</td>";
				
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				
					tableRow+="<td class='hide gradeId'>"+grade+"</td>";
					tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
					if(enableTraceability=="1"){
						tableRow+="<td class='batchNo textAlignRight'>"+batchNo+"</td>";
						}
					
						tableRow+="<td class='hide unitId'>"+uom+"</td>";
						tableRow+="<td>"+jQuery("#unitList option:selected").text()+"</td>";
						tableRow+="<td class='cropType textAlignRight'>"+cropType+"</td>";
						tableRow+="<td class='price textAlignRight'>"+price+"</td>";
					
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='amount textAlignRight'>"+(parseFloat(price)*parseFloat(netWeight)).toFixed(2)+"</td>";
			
				
			
				tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
				tableRow+="</tr>";
				console.log(tableRow);
				jQuery("#procurementDetailContent").append(tableRow);
				resetProductData();
				updateTableFooter();
			} 
		}
		
		function editRow(rowCounter){
			var id="#row"+rowCounter;
		
			$.each(jQuery(id), function(index, value) {
				var selectedCity=jQuery(this).find(".city").text();
				var selectedVillage=jQuery(this).find(".village").text();
			//	alert("selectedVillage:"+selectedVillage);
				var selectedFarmerType = jQuery(this).find(".farmerType").text();
				var selectedFarmer = jQuery(this).find(".farmerId").text();
				var selectedFarmerName=jQuery(this).find(".farmerName").text();
				var selectedProduct = jQuery(this).find(".productId").text();
				var selectedVariety = jQuery(this).find(".varietyId").text();
				var selectedGrade = jQuery(this).find(".gradeId").text();
				var selectedUnit = jQuery(this).find(".unitId").text();
				var selectedCropType = jQuery(this).find(".cType").text();
				var netWeight = jQuery(this).find(".netWeight").text();
				var netWeightSplit = netWeight.split(".");
				var selectedMobileNo=jQuery(this).find(".mobileNo").text();
				var selectedSupplierType=jQuery(this).find(".supplierType").text();
				var selectedPrice=jQuery(this).find(".price").text();
				var selectedVillageName=jQuery(this).find(".villageName").text();
				//alert("selectedVillageName:"+selectedVillageName);
				jQuery("#procurementProductList").val(selectedProduct);
				callTrigger("procurementProductList");
				
				jQuery("#varietyList").val(selectedVariety);
				callTrigger("varietyList");
				
				jQuery("#gradeList").val(selectedGrade);
			/* callTrigger("gradeList"); */
				
				if(enableTraceability=="1"){
				jQuery("#batchNo").val(jQuery(this).find(".batchNo").text());
				}
				console.log("Net weight Split :"+netWeightSplit);
				
			
				jQuery("#unitList").val(selectedUnit);
				callTrigger("unitList");
				$('input:radio[name=isRegisteredFarmer]').val([selectedFarmerType]);	
				
				if(isKpfBased=="1"){
				//	alert("test");
					if(selectedSupplierType=="99"){
						if(selectedFarmerType=="0"){
							
							jQuery(".regFarmer").show();
							jQuery(".city").show();
							jQuery(".village").show();
							jQuery(".unregFarmer").hide();					
							jQuery("#city").val(selectedCity);
							jQuery("#city").trigger("change");
							jQuery("#village").val(selectedVillage);
							jQuery("#farmer").val(selectedFarmer);
							
						}else{
							jQuery(".unregFarmer").show();	
							jQuery(".regFarmer").hide();
							jQuery(".city").hide();
							jQuery(".village").hide();
							jQuery("#farmerName").val(selectedFarmerName);
							jQuery("#mobileNoInput").val(selectedMobileNo);
						}
					}else if(selectedSupplierType=="11"){
							jQuery(".regFarmer").show();
							jQuery("#farmer").val(selectedFarmer);
							callTrigger("farmer");
					}else{				
						jQuery(".regFarmer").hide();
						jQuery(".city").hide();
						jQuery(".village").hide();
					}
				}else if(tenantId=="wilmar"){
				/* 	if(selectedSupplierType=="99"){ */	
				//alert(selectedVillageName);
							jQuery(".regFarmer").show();
							jQuery(".city").show();
							jQuery(".village").show();
							jQuery(".unregFarmer").hide();					
							jQuery("#city").val(selectedCity);
							$("#village").empty();							
							$("#village").append("<option value='"+selectedVillage +"'>"+selectedVillageName +"</option>");							
							$("#farmer").empty();
							$("#farmer").append("<option value='"+selectedFarmer +"'>"+selectedFarmerName +"</option>");
							//$("#village").html("<option value="selectedVillage">"selectedVillageName"</option>");
							//$("#farmer").html("<option value="selectedFarmer">"selectedFarmerName"</option>");
							/* jQuery("#village").val(selectedVillage);
							jQuery("#farmer").val(selectedFarmer); */
													
					/* }else if(selectedSupplierType=="11"){
							jQuery(".regFarmer").show();
							jQuery("#farmer").val(selectedFarmer);
							callTrigger("farmer");
					}else{				
						jQuery(".regFarmer").hide();
						jQuery(".city").hide();
						jQuery(".village").hide();
					} */
				}
				
				$('input:radio[name=selectedCropType]').val([selectedCropType]);
			//alert(selectedPrice);
				jQuery("#pricePerUnitLabel").val(selectedPrice);
				jQuery("#noOfBagsTxt").val(jQuery(this).find(".bags").text());
				jQuery("#dryLossTxt").val(jQuery(this).find(".dryLoss").text());
				jQuery("#gradingLossTxt").val(jQuery(this).find(".gradeLoss").text());
				jQuery("#netWeightCalc").text(jQuery(this).find(".netWeights").text());
				jQuery("#grossWeightKgTxt").val(netWeightSplit[0]);
				jQuery("#grossWeightGmTxt").val(netWeightSplit[1]);
				resetSelect2();
			});
			jQuery("#add").hide();
			jQuery("#edit").show();
			$("#edit").attr("onclick","editProduct("+rowCounter+")");
			 
		}
		
		function editProduct(index){
			var para1="";
			var para2="";
			var unit="";
			var uom="";
			var proUnitVal="";
			var cropType="";
			var farmer="";
			var farmerName=""
			var batchNo=""
			var id="#row"+index;
			jQuery(id).empty();
			
			jQuery("#validateError").text("");
			var city =jQuery("#city").val();
			var village =jQuery("#village").val();
			var villageName=jQuery("#village").text();
			var supplierType=jQuery("#masterType").val();
			var supplierTypeList=jQuery("#typeList").val();
			var farmerType=jQuery('input[type=radio][name=isRegisteredFarmer]:checked').val();			
			var cType =jQuery('input[type=radio][name=selectedCropType]:checked').val(); 
			var cropType =jQuery('input[type=radio][name=selectedCropType]:checked').text();
			var supplierName=jQuery("#supplierName").val();
			var isSupplier=jQuery('input[type=radio][name=isRegisteredSupplier]:checked').val();
			if(isKpfBased=="1"){
				if(supplierType==99 ||supplierType==11){			
					if(farmerType==0){
						farmer=jQuery("#farmer").val();
						farmerName =jQuery("#farmer option:selected" ).text();
						mobileNo="";
					
					}else{
						farmer="";
						farmerName=jQuery("#farmerName").val();
						mobileNo = jQuery("#mobileNoInput").val();
					}
				}else{
					farmer="";
					farmerName="NA";
					mobileNo="";
				}
			}else if(tenantId=="wilmar"){
				farmer=jQuery("#farmer").val();
				farmerName =jQuery("#farmer option:selected" ).text();
				mobileNo="";
			}
		//alert(farmerName);
		var mobileNo = jQuery("#mobileNoInput").val();
			var farmerType=jQuery('input[type=radio][name=isRegisteredFarmer]:checked').val();
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var price = jQuery("#pricePerUnitLabel").val();
			var cType =$('input[type=radio][name=selectedCropType]:checked').val();
			var bags=jQuery("#noOfBagsTxt").val();
		
				 unit=$( "#unitList option:selected" ).text();
				 uom=$( "#unitList" ).val();
				
			if(enableTraceability=="1"){
				var batch=jQuery("#batchNo").val();
			}
			/* if(farmerType==0){
				farmer=jQuery("#farmer").val();
				farmerName =jQuery("#farmer option:selected" ).text();
			}else{
				farmer="";
				farmerName=jQuery("#farmerName").val();
			} */
		
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}
			else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}
			else if(isEmpty(grade)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			}
			else if(isEmpty(netWeightKg)&&isEmpty(netWeightGm)){
				var error = '<s:property value="%{getLocaleProperty('invalid.grossWeight')}" />';
				jQuery("#validateError").text(error);
			}
			
				if(isEmpty(unit)){
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />'); 
					return false;
				}
		
			
			if(isEmpty(netWeightKg)){
				netWeightKg="0";
			}
			
			if(isEmpty(netWeightGm)){
				netWeightGm="000";
			}
			
			if(isEmpty(bags)){
				bags="0";
			}
			if(enableTraceability=="1"){
				if(isEmpty(batch)){
					batch="";
				}
			}
			 if(supplierType=="99"||supplierType=="11"){
				 if(supplierType=="99"){
						if(document.getElementById("isRegisteredFarmerId0").checked){						
							if(isEmpty(village)){
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.village')}" />');
								jQuery("#village").focus();
								return false;						
								
						
							}else if(isEmpty(farmer)){
									jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmer')}" />');
									jQuery("#farmer").focus();
									return false;							
									
								}
						}else{
							if(isEmpty(farmerName)){
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmerName')}" />');
								jQuery("#farmerName").focus();
								return false;						
								
							}else if(isEmpty(mobileNo)){							
								jQuery("#validateError").text('<s:property value="%{getLocaleProperty('invalid.mobileNo')}" />');
								jQuery("#mobileNoInput").focus();
								return false;							
								
							}
						}
					 
						
					}else{
					
						if(isEmpty(farmer)){
							jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.farmer')}" />');
							jQuery("#farmer").focus();
							return false;							
							
						}
					}
				
				}
			if(cType==undefined || cType==null){
				cropType='NA';
			}else if(cType==0){
				cropType='<s:property value="%{getLocaleProperty('cpType0')}" />';
			}else{
				cropType='<s:property value="%{getLocaleProperty('cpType1')}" />';
			}
			
			if(enableTraceability=='1'){
			
				batchNo=jQuery("#batchNo").val();
				}
			if(enableTraceability=='1'){
				if(batchNo==""){
				
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('emptyBatchNo')}" />');
					jQuery("#batchNo").focus();
					return false;			
				}
			} 
			
			var netWeight=netWeightKg+"."+netWeightGm;
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				return false;
			}
			
			if(isKpfBased=="1"){
				if(supplierType==99 || supplierType==11){
					if(supplierType==99 && farmerType==0){
						
						para1=farmer;
						para2=grade;
					}else if(supplierType==11){
						
						para1=farmer;
						para2=grade;
					}else{
						
						para1=farmerName;
						para2=grade;
					}
				}else {
					if(isSupplier==0){
						
					para1=supplierTypeList;
					para2=grade;
					}else{
						
						para1=supplierName;
						para2=grade;
					}
				}
			}else if(tenantId=="wilmar"){
				para1=farmer;
				para2=grade;
			}
			
			if(!checkGradeExists(para1,para2)){
				
				var tableRow="";
				tableRow+="<td class='hide farmerId'>"+farmer+"</td>";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";
				tableRow+="<td class='hide cType'>"+cType+"</td>";
				tableRow+="<td class='hide city'>"+city+"</td>";
				tableRow+="<td class='hide village'>"+village+"</td>";
				tableRow+="<td class='hide farmerType'>"+farmerType+"</td>";				
				tableRow+="<td class='hide supplierType'>"+supplierType+"</td>";
				tableRow+="<td class='hide supplierTypeList'>"+supplierTypeList+"</td>";
				tableRow+="<td class='hide supplierTypeList'>"+supplierTypeList+"</td>";
				tableRow+="<td class='hide supplierName'>"+supplierName+"</td>";
				tableRow+="<td class='hide villageName'>"+villageName+"</td>";
				tableRow+="<td class='farmerName textAlignRight'>"+farmerName+"</td>";
				tableRow+="<td class='hide mobileNo'>"+mobileNo+"</td>";
				
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				
					tableRow+="<td class='hide gradeId'>"+grade+"</td>";
					tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
					if(enableTraceability=="1"){
						tableRow+="<td class='batchNo textAlignRight'>"+batchNo+"</td>";
						}
					
						tableRow+="<td class='hide unitId'>"+uom+"</td>";
						tableRow+="<td>"+jQuery("#unitList option:selected").text()+"</td>";
						tableRow+="<td class='cropType textAlignRight'>"+cropType+"</td>";
						tableRow+="<td class='price textAlignRight'>"+price+"</td>";
					
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='amount textAlignRight'>"+(parseFloat(price)*parseFloat(netWeight)).toFixed(2)+"</td>";
			
				
				
				tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
				tableRow+="</tr>";
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
		
		function checkGradeExists(value1,value2){
			//alert(value1+"----"+value2);
			var returnVal = false;
		
				var tableBody = jQuery("#procurementDetailContent tr");
				
				$.each(tableBody, function(index, value) {
					var supType = jQuery(this).find(".supplierTypeList").text();
					var farmer = jQuery(this).find(".farmerId").text();
					var grade = jQuery(this).find(".gradeId").text();
					var farmerName=jQuery(this).find(".farmerName").text();
					var supplierName=jQuery(this).find(".supplierName").text();
					//alert(supType+"****"+farmer+"####"+grade+"@@@@"+farmerName);
					 if(farmer==value1 && grade==value2){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}else if(supType==value1 && grade==value2){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}else if(farmerName==value1 && grade==value2){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}else if(supplierName==value1 && grade==value2){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}
				});
			
			return returnVal;	
		}
		
		function updateTableFooter(){
			var tableBody = jQuery("#procurementDetailContent tr");
			var totalPrice=0.0;
			var totalBags=0.0;
			var totalNetWeight=0.0;
			var totalAmount=0.0;
			var totalDryLoss=0.0;
			var totalGradeLoss=0.0;
			var totalNetWeights=0.0;
			var totalSubTotal=0.0;
			$.each(tableBody, function(index, value) {
				totalPrice+=getFormattedFloatValue(jQuery(this).find(".price").text());
				totalBags+=getFormattedFloatValue(jQuery(this).find(".bags").text());
				totalNetWeight+=getFormattedFloatValue(jQuery(this).find(".netWeight").text());
				totalDryLoss+=getFormattedFloatValue(jQuery(this).find(".dryLoss").text());
				totalGradeLoss+=getFormattedFloatValue(jQuery(this).find(".gradeLoss").text());
				totalNetWeights+=getFormattedFloatValue(jQuery(this).find(".netWeights").text());
				totalAmount+=getFormattedFloatValue(jQuery(this).find(".amount").text());
				
			});
			$("#pricePerUnitTotalLabel").text(totalPrice.toFixed(2));
			$("#noOfBagsTotalLabel").text(totalBags);
			$("#grossWeightTotalLabel").text(totalNetWeight.toFixed(2));
			$("#dryLossTotalLabel").text(totalDryLoss.toFixed(2));
			$("#gradeLossTotalLabel").text(totalGradeLoss.toFixed(2));
			$("#netWeightTotalLabel").text(totalNetWeights.toFixed(2));
			$("#subTotalLabel").text(totalAmount.toFixed(2));
		}
		
		function getFormattedFloatValue(n){
			if(!isNaN(n)){
				return parseFloat(n);
			}else{
				return parseFloat(0);
			}
		}
		
		function resetProductData(){
			jQuery("#city").val("");
			/* jQuery("#village").val("");
			jQuery("#farmer").val(""); */
			jQuery("#farmer").val("");
			$("#village").empty();			
			$("#village").append("<option value=''>Select</option>");		
			//$("#village").html("<option value=''>Select</option>");
			//$("#farmer").empty();
			//$("#farmer").append("<option value=''>Select</option>");	
			//$("#farmer").html("<option value=''>Select</option>");
			jQuery("#procurementProductList").val("");
			jQuery("#varietyList").val("");
			jQuery("#gradeList").val("");
			//$('input[name=selectedCropType]').attr('checked',false);
			//document.getElementById("validatebatchNoError").innerHTML="";
			jQuery("validatebatchNoError").text("");
			if(enableTraceability=='1'){
				var batchNo =document.getElementById("batchNo").value;
			  	$('#batchNo').val('');
			 }
			
			jQuery("#unitList").val("");
			jQuery("#farmerName").val("");
			jQuery("#mobileNoInput").val("");
			jQuery("#pricePerUnitLabel").val("");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#dryLossTxt").val("");
			jQuery("#gradingLossTxt").val("");
			jQuery("#netWeightCalc").text("0.00");
			
			 jQuery("#paymentRupee").val("0.00");
			//jQuery("#farmer").val("");
			//jQuery("#farm").val("");
			
			//jQuery("#roadMapCode").val("");
			//jQuery("#vehicleNo").val("");
			
			resetSelect2();
			
			jQuery("#add").show();
			jQuery("#edit").hide();
		}
		function resetTableData(){
			//jQuery("#varietyList").val("");
			//jQuery("#gradeList").val("");			
			jQuery("#pricePerUnitLabel").val("");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#dryLossTxt").val("");
			jQuery("#gradingLossTxt").val("");
			jQuery("#netWeightCalc").text("0.00");
		
		}
		
		function saveProcurement(){
		//	var mobileUser=jQuery("#agent").val();
		//$("#sucessbtn").prop('disabled', true);
		jQuery("#validateError").text("");
		var dataa ="";
		var error = "";
			var warehouse=jQuery("#warehouse").val();			
			var date=jQuery("#date").val();			
			var farmerType=jQuery('input[type=radio][name=isRegisteredFarmer]:checked').val();
			var isSupplier=jQuery('input[type=radio][name=isRegisteredSupplier]:checked').val();			
			var supplierName =jQuery("#supplierName").val();
			var supplierMobileNo = jQuery("#supplierMobileNoInput").val();
			var typeList=jQuery("#typeList").val();
			var supplierType=jQuery("#masterType").val();
			var farmer =jQuery("#farmer").val();
			var totalLabourCost=jQuery("#totalLabourCost").val();
			var transportCost=jQuery("#transportCost").val();
			var taxAmt=jQuery("#taxAmt").val();
			var otherCost=jQuery("#otherCost").val();
			var invoiceNo = jQuery("#invoiceNo").val();
			if(tenantId=="wilmar"){
				var trader =jQuery("#traderList").val();
				
			}
			if(enableSupplier==1){
				
					if(isEmpty(supplierType)){
						error = '<s:property value="%{getLocaleProperty('empty.supplier')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#masterType").focus();	
						return false;
						//alert(error);
										
					}
					
						if(isKpfBased=="1"){
							if(isSupplier==1){
								
								if(isEmpty(supplierName)){
									error = '<s:property value="%{getLocaleProperty('empty.supplierName')}" />';
									jQuery("#validateError").text(error);
									jQuery("#supplierName").focus();
									return false;
									
								}else if(isEmpty(supplierMobileNo)){
									error = '<s:property value="%{getLocaleProperty('invalid.mobileNo')}" />';
									jQuery("#validateError").text(error);
									jQuery("#supplierMobileNoInput").focus();
									return false;
									
								}
							}
							if(tenantId=="iffco"){
							if(supplierType==99 ){
								isSupplier=farmerType;
							}
							}
						}
					
					
			}
			
			var procurementProductInfoArray =  buildProcurementProductInfoArray();
			if(isEmpty(procurementProductInfoArray)){
				error = '<s:property value="%{getLocaleProperty('noRecordFound')}" />';
			}
	
			
			
			if(isEmpty(warehouse)){
				error = '<s:property value="%{getLocaleProperty('empty.warehouse')}"/>';
				jQuery("#warehouse").focus();
			}else if(isEmpty(date)){
				error = '<s:property value="%{getLocaleProperty('empty.date')}" />';
				jQuery("#date").focus();
			}
		
			if(supplierType!=99 ){
				if(isSupplier==0){
			
				if(isEmpty(typeList)){
					error = '<s:property value="%{getLocaleProperty('empty.supplierType')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#typeList").focus();
					return false;
					//alert(error);
				}					
				}
				
			}	
			if(tenantId=="wilmar"){
				if(supplierType==2){
					if(isEmpty(trader)){
						error = '<s:property value="%{getLocaleProperty('empty.trader')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#traderList").focus();
						return false;
						//alert(error);
					}	
				}
			}
				
			
			/* if(farmerType==0){
				if(isEmpty(farmer)){
					error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
					jQuery("#validateError").text(error);
					return false;
					//alert(error);
					jQuery("#farmer").focus();					
				}
			}	
			 */
			if(!isEmpty(error)){
				jQuery("#validateError").text(error);
				$("#sucessbtn").prop('disabled', false);
				return false;
			}
			
			var paymentRupee=0.00;
			//var paymentPaise=0.00;
			
			
			if(!isEmpty(jQuery("#paymentRupee").val())){
				paymentRupee = jQuery("#paymentRupee").val(); 
			}
			/* if(!isEmpty(jQuery("#paymentPaise"))){
				paymentPaise = jQuery("#paymentPaise").val();
			} */
			//var paymentAmount=paymentRupee+"."+paymentPaise;
			var paymentAmount=paymentRupee;
			
			var totalAmount=jQuery("#subTotalLabel").text();
			if(isKpfBased=="1"){
				dataa = {
						selectedWarehouse:warehouse,
						selectedDate:date,
						procurementDate:date.toString(),
						selectedFarmerType:farmerType,				
						paymentAmount:paymentAmount,
						totalAmount:totalAmount,
						productTotalString:procurementProductInfoArray,			
						mobileNo:supplierMobileNo,			
						selectedSupplier:supplierType,
						isSupplier:isSupplier,
						supplierName:supplierName,
						totalLabourCost:totalLabourCost,
						transportCost:transportCost,
						taxAmt:taxAmt,
						otherCost:otherCost,
						selectedMasterType:jQuery("#typeList").val(),
						invoiceNo:invoiceNo
						
					}
			}else if(tenantId=="wilmar"){
				dataa = {
						selectedWarehouse:warehouse,
						selectedDate:date,
						procurementDate:date.toString(),
						selectedFarmerType:farmerType,				
						paymentAmount:paymentAmount,
						totalAmount:totalAmount,
						productTotalString:procurementProductInfoArray,			
						mobileNo:supplierMobileNo,			
						selectedSupplier:supplierType,
						isSupplier:isSupplier,
						supplierName:supplierName,
						selectedMasterType:jQuery("#typeList").val(),			
						selectedTrader:trader
					}
			}
		
			$("#sucessbtn").attr("disabled", true);
			$.ajax({
			        url: 'supplierProcurement_populateProcurement.action',
			        type: 'post',
			        dataType: 'json',
			        data: dataa,
			        success: function(data) {
			        	$("#sucessbtn").prop('disabled', false);
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
		
		function buildProcurementProductInfoArray(){
			
			var tableBody = jQuery("#procurementDetailContent tr");
			var productInfo="";
			
			/** 
				INFO :- 
					##=Splitter for td
					@@=Splitter for tr
			*/
			$.each(tableBody, function(index, value) {
				
				productInfo+=jQuery(this).find(".farmerType").text(); //0
				
				productInfo+="#"+jQuery(this).find(".farmerId").text(); //1
				
				productInfo+="#"+jQuery(this).find(".farmerName").text(); //2
				
				productInfo+="#"+jQuery(this).find(".mobileNo").text(); //3
				
				productInfo+="#"+jQuery(this).find(".productId").text(); //4
				
				productInfo+="#"+jQuery(this).find(".varietyId").text(); //5
				
				productInfo+="#"+jQuery(this).find(".gradeId").text(); //6	
				
				productInfo+="#"+jQuery(this).find(".bags").text(); //7
				
				productInfo+="#"+jQuery(this).find(".cType").text();//8	
				
				productInfo+="#"+jQuery(this).find(".netWeight").text(); //9
				
				productInfo+="#"+jQuery(this).find(".amount").text(); //10
				
				productInfo+="#"+jQuery(this).find(".supplierType").text(); //11
				
				productInfo+="#"+jQuery(this).find(".unitId").text(); //12				
				
				if(enableTraceability=="1"){					
					productInfo+="#"+jQuery(this).find(".price").text(); //13
					productInfo+="#"+jQuery(this).find(".batchNo").text()+"@"; //14
				}else{
					productInfo+="#"+jQuery(this).find(".price").text()+"@"; //13
				}
				
				
				
				
			});
			//alert(productInfo);
			return productInfo;
		}
		
		function resetSelect2(){
			$(".select2").select2();
		}
		
		
		function calcNetWeight(){
			
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var dryLoss = jQuery("#dryLossTxt").val();
			var gradeLoss = jQuery("#gradingLossTxt").val();
			var bags=jQuery("#noOfBagsTxt").val();
			
			
			if(isEmpty(netWeightKg)){
				netWeightKg = 0;
			}
			
			if(isEmpty(netWeightGm)){
				netWeightGm = 0;
			}
			
			if(isEmpty(bags)){
				bags="0";
			}
			
			var netWeight=parseInt(netWeightKg)+"."+parseInt(netWeightGm);
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
			}
			if(parseInt(netWeightKg)<=parseInt(dryLoss) || parseInt(netWeightKg)<=parseInt(gradeLoss) ||  parseInt(netWeightKg)<=parseInt(gradeLoss)+parseInt(dryLoss)){
				netWeight = 0;
				$("#netWeightCalc").text(netWeight);
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('weight.product')}" /> ');
				jQuery("#dryLossTxt").val('');
				jQuery("#gradingLossTxt").val('');
			}
			
			if(netWeight>0){
				if(!isEmpty(dryLoss)){
					netWeight = netWeight - dryLoss;
					netWeight=netWeight.toFixed(2);
				}
				
				if(!isEmpty(gradeLoss)){
					netWeight = netWeight - gradeLoss;
					netWeight=netWeight.toFixed(2);
				}
			    
				$("#netWeightCalc").text(netWeight);
			}
			
		}			
		
		
		
		
		function loadSupplier(value){
			if(isKpfBased=="1"){
				if(!isEmpty(value)){
					if(value=='99'){
						$("#supplierTypeLabel").text("");
						$("#typeList").val("");
						$(".supplierTypeDiv").hide();
						//$(".farmerDiv").show();
						if(tenantId=="livelihood"){
							$(".farmerDiv").hide();
						}else{
							$(".farmerDiv").show();
						}
						$(".regFarmer").show();
						$(".isSupplier").hide();			
						$(".unregSupplier").hide();
						$(".village").show();
						$(".city").show();
						$(".cropType").show();
						$('input:radio[name=isRegisteredFarmer]')[0].checked = true;
					}else if(value=='11'){
						$(".supplierTypeDiv").show();
						$(".farmerSelectionDiv").hide();
						$(".unregFarmer").hide();
						$(".regFarmer").show();
						$(".isSupplier").hide();
						$(".unregSupplier").hide();
						$(".village").hide();
						$(".city").hide();
						$(".cropType").show();
						$('input[name=isRegisteredSupplier]').attr('checked',false);
						$("#supplierTypeLabel").text($("#masterType option:selected").text());
						
						$.post("supplierProcurement_populateSamithi", {
							selectedMasterType : value
						}, function(data) {
							insertOptions("typeList", $.parseJSON(data));
						});
						
						$("#typeList").change(function(){
							{
								$.post("supplierProcurement_populateFarmerByFPO", {
									selectedSupplier : $(this).val()
								}, function(data) {
									insertOptions("farmer", $.parseJSON(data));
								});
							}
						});
					}else{
					
						$(".isSupplier").show();
						$(".cropType").show();
						$(".supplierTypeDiv").show();
						$(".farmerDiv").hide();
						$(".regFarmer").hide();
						$(".farmerSelectionDiv").hide();
						$(".unregFarmer").hide();
						$(".village").hide();
						$(".city").hide();
						$(".unregSupplier").hide();
						//$("#isRegisteredSupplierId0").checked = true;
						$('input:radio[name=isRegisteredSupplier]')[0].checked = true;
						$("#supplierTypeLabel").text($("#masterType option:selected").text());
						$.post("supplierProcurement_populateMasterData", {
							selectedMasterType : value
						}, function(data) {
							insertOptions("typeList", $.parseJSON(data));
						});	
					}
				}else{
					$(".regFarmer").hide();
					jQuery("#supplierTypeLabel").text("");
					jQuery("#typeList").val("");
					jQuery(".supplierTypeDiv").hide();
				}
				//reloadTable();
			}else if (tenantId=="wilmar"){
				if(!isEmpty(value)){
				if(value=='99'){
					$("#supplierTypeLabel").text("");
					$("#typeList").val("");
					$(".supplierTypeDiv").hide();
					$(".farmerDiv").show();
					$(".regFarmer").show();
					$(".isSupplier").hide();			
					$(".unregSupplier").hide();
					$(".village").show();
					$(".city").show();
					$(".cropType").show();
					$(".farmerSelectionDiv").hide();
					$(".traderDiv").hide();	
					$('input:radio[name=isRegisteredFarmer]')[0].checked = true;
				}else if(value=='1'){
					$(".isSupplier").hide();
					$(".cropType").show();
					$(".supplierTypeDiv").show();
					$(".farmerDiv").hide();
					$(".regFarmer").show();
					$(".farmerSelectionDiv").hide();
					$(".unregFarmer").hide();
					$(".village").show();
					$(".city").show();
					$(".unregSupplier").hide();	
					$(".traderDiv").hide();	
					//$("#isRegisteredSupplierId0").checked = true;
					$('input:radio[name=isRegisteredSupplier]')[0].checked = true;
					$("#supplierTypeLabel").text($("#masterType option:selected").text());
					$.post("supplierProcurement_populateMasterData", {
						selectedMasterType : value
					}, function(data) {
						insertOptions("typeList", $.parseJSON(data));
					});		
				}else{
					$(".isSupplier").hide();
					$(".cropType").show();
					$(".supplierTypeDiv").show();
					$(".farmerDiv").hide();
					$(".regFarmer").show();
					$(".farmerSelectionDiv").hide();
					$(".unregFarmer").hide();
					$(".village").show();
					$(".city").show();
					$(".unregSupplier").hide();	
					$(".traderDiv").show();	
					$("#traderLabel").text("Trader");
					//$("#isRegisteredSupplierId0").checked = true;
					$('input:radio[name=isRegisteredSupplier]')[0].checked = true;
					$("#supplierTypeLabel").text($("#masterType option:selected").text());
					$.post("supplierProcurement_populateMasterData", {
						selectedMasterType : value
					}, function(data) {
						insertOptions("typeList", $.parseJSON(data));
					});	
				}
			}else{
				$(".regFarmer").hide();
				//jQuery("#supplierTypeLabel").text("");
				jQuery("#typeList").val("");
				jQuery(".supplierTypeDiv").hide();
			}
			}
			
		}
		
		
		function buttonEdcCancel(){
			//refreshPopup();
			document.getElementById("model-close-edu-btn").click();	
	
	    	 window.location.href="supplierProcurement_create.action";
	    	
	     }
		
		function farmerAttendance(obj){
			if(obj.value=='1'){
					jQuery(".substituteName").show();
			}else{
				jQuery(".substituteName").hide();
			}  
		}
		
		function onCancel() {
			
		    	 window.location.href="supplierProcurement_create.action";
		    	
		}
		
		 function isDecimal(evt) {
	    		
	    	 evt = (evt) ? evt : window.event;
	    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	    	        return false;
	    	    }
	    	    return true;
	    }
		 
 function reloadTable(){
			    	var procurementProductInfoArray =  buildProcurementProductInfoArray();			    	
			    	if(procurementProductInfoArray.length>0){
					   var r = confirm('<s:text name="confirm.deleteProduct"/>');
					   if (r == true) {
			    			$("#procurementDetailContent").empty();
						    $("#pricePerUnitTotalLabel").text("0.00");
							$("#noOfBagsTotalLabel").text("0");
							$("#grossWeightTotalLabel").text("0.000");
							$("#subTotalLabel").text("0.00");
					   }
		 }
			    }

	</script>
</body>