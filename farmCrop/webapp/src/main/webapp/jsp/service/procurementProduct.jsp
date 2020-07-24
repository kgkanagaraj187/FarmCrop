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
					<div class="flexi flexi12" style="width:140px">
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
					<div class="flexi flexi12" id="supplierDiv" style="width:150px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('supplier')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="supplier" list="masterTypeList" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="loadSupplier(this.value);"
								id="masterType" cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="flexi flexi12" style="width:150px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="selectedWarehouseId" list="listWarehouse"
								headerKey="" headerValue="%{getText('txt.select')}"
								lisykey="key" listValue="value" id="warehouse"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="flexi flexi12" style="width:150px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('profile.location.municipality')}" /></label>
						<div class="form-element">
							<s:select name="cityId" list="listMunicipality" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listVillage(this.value);" id="city"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="flexi flexi12" style="width:150px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /></label>
						<div class="form-element">
						<s:select name="villageId" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listFarmer(this.value);" id="village"
								cssClass="form-control input-sm select2" />
							<%-- <s:select list="{}" headerKey="" listKey="key"
								listValue="value"
								headerValue="%{getText('txt.select')}" id="village"
								cssClass="form-control input-sm select2"
								onchange="listFarmer(this.value);" /> --%>
						</div>
					</div>


					<div class="flexi flexi12 supplierTypeDiv" style="width:150px">

						<label id="supplierTypeLabel"><span class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="masterType" list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="typeList"
								cssClass="form-control input-sm select2" />
						</div>

					</div>
					<s:if test="currentTenantId=='kpf' || currentTenantId=='simfed' || currentTenantId=='wub'">
						<div class="flexi flexi12" style="width:150px">
							<label><s:property
									value="%{getLocaleProperty('cropType')}" /></label>
							<div class="">
								<s:radio list="cTypes" listKey="key" listValue="value"
									name="selectedCropType" theme="simple" id="ctype" />
							</div>
						</div>
					</s:if>

					<div class="flexi flexi12 farmerDiv farmerSelectionDiv" style="width:auto;">

						<label for="txt"><s:property
								value="%{getLocaleProperty('isFarmer')}" /><span
							class="manadatory">*</span></label>
						<div class="" style="margin-left:10px;">
							<s:radio list="regType" listKey="key" listValue="value"
								name="isRegisteredFarmer" id="isRegisteredFarmerId"
								style="color: black;" />
						</div>

					</div>
					<div class="flexi flexi12 regFarmer " style="width:150px">

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

					<div class="flexi flexi12 unregFarmer" style="width:150px">

						<label for="txt"><s:property
								value="%{getLocaleProperty('farmerName')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield id="farmerName" name="farmerId" maxlength="35"
								cssClass="col-sm-3 form-control " />
						</div>

					</div>

					
					<div class="flexi flexi12 unregFarmer" style="width:150px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.mobileNumber')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield id="mobileNoInput" name="mobileNo" maxlength="10"
								cssClass="col-sm-3 form-control"
								onkeypress="return isNumber(event)" />
						</div>
					</div>
					<s:if test="enableBuyer==1">
						<div class="flexi flexi12" style="width:150px">
							<label for="txt"><s:text name="buyerName" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedBuyer" id="selectedBuyer"
									list="buyersList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" />

							</div>
						</div>
					</s:if>
				</div>
				
				<div class="flexiWrapper filterControls ">
				<s:if test="currentTenantId=='awi'||currentTenantId=='AWI'">
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm')}" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select list="{}" headerKey=""
									headerValue="%{getText('txt.select')}" name="selectedFarm"
									id="farm" cssClass="form-control input-sm select2" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('roadMapCode')}" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:textfield name="roadMapCode" id="roadMapCode" maxlength="30"
									cssClass="col-sm-3 form-control " />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('vehicleNo')}" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:textfield name="vehicleNo" id="vehicleNo" maxlength="30"
									cssClass="col-sm-3 form-control " />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farmerAttendance')}" /><span
								class="manadatory">*</span></label>
							<div class="">
								<s:radio list="farmerAttence" name="farmerAttnce"
									value="FarmerAttendanceDefaultValue" id="farmerAttnceId"
									onchange="farmerAttendance(this);" />
							</div>
						</div>

						<div class="flexi flexi12 substituteName" style="display: none">
							<label for="txt"><s:property
									value="%{getLocaleProperty('substituteName')}" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:textfield name="substituteName" id="substituteName"
									maxlength="45" cssClass="col-sm-3 form-control " />
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

							<div class="flexi flexi10">
								<label for="txt"><s:property
										value="%{getLocaleProperty('product')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="procurementProductId" list="productList"
										headerKey="" headerValue="%{getText('txt.select')}"
										listKey="id" listValue="name" id="procurementProductList"
										cssClass="form-control input-sm select2"
										onchange="listProVarierty(this);loadUnit(this);" />
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
							<s:if test="currentTenantId!='kpf'&&currentTenantId!='simfed'&&currentTenantId!='wub'">
								<!-- <div id="productUnit"></div>  -->
								<div class="flexi flexi10 unit">
									<label for="txt"> <s:property
											value="%{getLocaleProperty('unit')}" /> <%-- <s:text name="unit" /> --%>
									</label>
									<div class="form-element"></div>
									<span id="productUnit">
								</div>
							</s:if>
							<s:else>
								<div class="flexi flexi10 unit">
									<label for="txt"><s:property
											value="%{getLocaleProperty('unit')}" /> <span
										class="manadatory">*</span></label>
									<div class="form-element">
										<s:select id="unitList" name="unitList" list="listUom"
											headerKey="-1" listKey="code" listValue="name"
											headerValue="%{getText('txt.select')}"
											cssClass="form-control select2" />
									</div>
								</div>
							</s:else>

							<div class="flexi flexi10 pricePerUnit">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('pricePerUnit')}" />(<s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)<span
									class="manadatory">*</span></label>
								<div class="form-element">
										<s:textfield id="pricePerUnitLabel" maxlength="7"
										cssClass="form-control input-sm" readOnly="true"
										onkeypress="return isDecimal(event)" />
								</div>
							</div>
						<s:if test="currentTenantId!='pratibha'">
							<div class="flexi flexi10 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noofBags')}" /></label>
								<div class="form-element">
									<s:textfield id="noOfBagsTxt" maxlength="3"
										cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" onchange='calcWeight(this.value);' />
								</div>
							</div>
						</s:if>
						<s:if test="currentTenantId=='awi'">
						<div class="flexi flexi10 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noofFruitBags')}" /></label>
								<div class="form-element">
									<s:textfield id="noOfFruitBagsTxt" maxlength="3"
										cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
						</s:if>
							<div class="flexi flexi10 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('grossWeights')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="grossWeightKgTxt" maxlength="8"
											cssStyle="width:180%" onkeypress="return isNumber(event)"
											onchange="calcNetWeight()" />
										<div>.</div>
										<s:textfield id="grossWeightGmTxt" maxlength="3"
											cssStyle="width:60%" onkeypress="return isNumber(event)"
											onchange="calcNetWeight()" />
									</div>
								</div>
							</div>
							<s:if
								test="currentTenantId=='lalteer'||currentTenantId=='LALTEER'">
								<div class="flexi flexi10 ">
									<label for="txt"><s:property
											value="%{getLocaleProperty('dryLoss')}" /> </label>
									<div class="form-element">
										<s:textfield id="dryLossTxt" maxlength="7"
											cssClass="form-control input-sm"
											onkeypress="return isDecimal(event)"
											onchange="calcNetWeight()" />
									</div>
								</div>

								<div class="flexi flexi10 ">
									<label for="txt"><s:property
											value="%{getLocaleProperty('gradingLoss')}" /></label>
									<div class="form-element">
										<s:textfield id="gradingLossTxt" maxlength="7"
											cssClass="form-control input-sm"
											onkeypress="return isDecimal(event)"
											onchange="calcNetWeight()" />
									</div>
								</div>

								<div class="flexi flexi10 ">
									<label for="txt" style="display: block; text-align: center"><s:property
											value="%{getLocaleProperty('NetWeight')}" /></label>
									<div class="form-element">
										<span id="netWeightCalc"
											style="display: block; text-align: center">0.00</span>
									</div>
								</div>

							</s:if>
							<div class="flexi flexi10 ">
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
								value="%{getLocaleProperty('info.procurementProduct')}" />
						</h2>
						

				<table class="table table-bordered aspect-detail"
					id="procurementDetailTable" style="width: 100%; margin-top: 30px;">
					<thead>
						<tr>
							<th><s:property value="%{getLocaleProperty('product')}" /></th>
							<th><s:property value="%{getLocaleProperty('variety')}" /></th>
							<s:if
								test="currentTenantId!='lalteer'||currentTenantId!='LALTEER'">
								<th class="procGrade"><s:property
										value="%{getLocaleProperty('procGrade')}" /></th>
								<s:if test="enableTraceability==1">
									<th><s:property
											value="%{getLocaleProperty('procurement.batchNo')}" /></th>
								</s:if>
								<th width="8%"><s:property
										value="%{getLocaleProperty('unit')}" /></th>
								<th class="pricePerUnit"><s:property
										value="%{getLocaleProperty('pricePerUnit')}" />(<s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)</th>
							</s:if>
							<s:if test="currentTenantId!='pratibha'">
							<th><s:property value="%{getLocaleProperty('noofBags')}" /></th>
							</s:if>
							<s:if test="currentTenantId=='awi'">
							<th><s:property value="%{getLocaleProperty('noofFruitBags')}" /></th>
							</s:if>
							<th><s:property value="%{getLocaleProperty('grossWeights')}" /></th>
							<s:if
								test="currentTenantId=='lalteer'||currentTenantId=='LALTEER'">

								<th class="col-md-2"><s:property
										value="%{getLocaleProperty('dryLoss')}" /></th>

								<th class="col-md-2"><s:property
										value="%{getLocaleProperty('gradingLoss')}" /></th>

								<th class="col-md-2"><s:property
										value="%{getLocaleProperty('grossWeight')}" /></th>

							</s:if>
							<s:if test="currentTenantId!='lalteer' ">
							
								<th><s:property value="%{getLocaleProperty('subTotal')}" />( <s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)</th>
							</s:if>
							<th style="text-align: center"><s:property
									value="%{getLocaleProperty('action')}" /></th>
						</tr>
					</thead>
					<tbody id="procurementDetailContent">
					</tbody>
					<tfoot>
						<tr>
							<th style="text-align: right"></th>
							<th style="text-align: right"></th>
							<th style="text-align: right"><s:property
									value="%{getLocaleProperty('total')}" /></th>
							<s:if test="currentTenantId!='lalteer' ">
								<th style="text-align: right"></th>
								<s:if test="enableTraceability==1">
									<th style="text-align: right"></th>
								</s:if>
								<th style="text-align: right">
									<div id="pricePerUnitTotalLabel">0.00</div>
								</th>
							</s:if>
							<s:if test="currentTenantId!='olivado' ">
							<s:if test="currentTenantId!='pratibha' ">
							<th style="text-align: right">
								<div id="noOfBagsTotalLabel">0</div>
							</th>
							</s:if>
							</s:if>
							<s:else>
							<th style="text-align: right">
								<div id=""></div>
							</th>
							</s:else>
							<s:if test="currentTenantId=='awi'">
							<th style="text-align: right">
								<div id="noOfFruitBagsTotalLabel">0</div>
							</th>
							</s:if>
							<th style="text-align: right">
								<div id="grossWeightTotalLabel">0.000</div>
							</th>

							<s:if
								test="currentTenantId=='lalteer'||currentTenantId=='LALTEER'">
								<th class="col-md-2" style="text-align: right">
									<div id="dryLossTotalLabel">0</div>
								</th>

								<th class="col-md-2" style="text-align: right">
									<div id="gradeLossTotalLabel">0</div>
								</th>


								<th class="col-md-2" style="text-align: right">
									<div id="netWeightTotalLabel">0.00</div>
								</th>
							</s:if>
							<s:else>
								<th style="text-align: right"><div id="subTotalLabel" />0.00</th>
							</s:else>
							<th></th>
						</tr>
						<s:if test="enableLoanModule==1">
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							 <th></th> 
							<th style="text-align: right"><s:property
									value="%{getLocaleProperty('loanInterest')}" /></th>
							<th style="text-align: right"><div id="loanIntId" />0.00</th>
							<th></th>
						</tr>
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th> 
							<th style="text-align: right"><s:property
									value="%{getLocaleProperty('finalPay')}" /></th>
							<th style="text-align: right"><div id="finalPayAmt" />0.00</th>
							<th></th>
						</tr>
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
					<th></th> 
							<th style="text-align: right"><s:property
									value="%{getLocaleProperty('repaymentAmt')}" /></th>
							<th style="text-align: right"><div id="repaymentAmt" />0.00</th>
							<th></th>
						</tr>
						
</s:if>
					</tfoot>
				</table>
				</div></div></div>
		<p></p>
		<s:if test="currentTenantId!='lalteer'">
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">

					<h2>
						<s:property value="%{getLocaleProperty('info.payment')}" />
					</h2>
					<%-- <div class="flexiWrapper filterControls">
							<table class="table table-bordered aspect-detail">
									<td><label for="txt"><s:property
												value="%{getLocaleProperty('farmerBalAmount')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</label></td>
							<td <div id="farmerCashBal" style="font-weight: bold;"></div></td>
							</table>
							</div> --%>
					<div class="flexiWrapper filterControls">
						<table class="table table-bordered aspect-detail">
							<s:if test="enableLoanModule==1">
							<td><label for="txt"><s:property
										value="%{getLocaleProperty('farmer.loanAmount')}" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</label></td>
							<td> <div id="farmerloanAmt" style="font-weight: bold;">0.00</div></td>
							<td><label for="txt"><s:property
										value="%{getLocaleProperty('farmer.outstandingBal')}" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</label></td>
							<td> <div id="farmerOutBal" style="font-weight: bold;">0.00</div></td>
							<%-- <td><label for="txt"><s:property
										value="%{getLocaleProperty('enterAmount')}" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</label></td> --%>
							
							</s:if>
							<td><label for="txt"><s:property
										value="%{getLocaleProperty('farmerBalAmount')}" />(<s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)</label></td>
							<td> <div id="farmerCashBal" style="font-weight: bold;">0.00</div></td>
							<td><label for="txt"><s:property
										value="%{getLocaleProperty('enterAmount')}" />(<s:if test="currentTenantId!='pratibha'"><s:property value="%{getCurrencyType().toUpperCase()}" /></s:if><s:else><s:property value="%{getLocaleProperty('priceUnit')}" /></s:else>)</label></td>
							
							
							
							
							
							
							
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

		<s:if test="currentTenantId == 'efk'">
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2><s:property value="%{getLocaleProperty('Procurement.transpotation.Details')}" /></h2>


					<div class="flexiWrapper filterControls">

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('procurement.transport.vehicleList')}" /></label>

							<div class="form-element">
								<s:select name="procurement.vehicleType" list="vehicleList" id="vehicleType"
									listKey="key" listValue="value" theme="simple"
									cssClass="form-control input-sm select2" multiple="true" />

							</div>
						</div>


						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('procurement.transport.cost')}" />(<s:property
									value="%{getCurrencyType().toUpperCase()}" />)</label>

							<div class="form-element">
								<input type="text" class="fullwidth textAlignRight"
									name="procurement.transportCost" id="transportCost"
									 maxlength="10"
									onkeypress="return isDecimal(event)"
									style="text-align: right; padding-right: 1px; width: 90% !important;">

							</div>
						</div>



					</div>

				</div>
			</div>
		</s:if>


		<div class="yui-skin-sam " id="loadList" style="display: block">
			<sec:authorize ifAllGranted="service.procurementService.create">
				<span id="save" class=""><span class="first-child">
						<button type="button" onclick="saveProcurement()"
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
            <span><h5><s:text name="procprodMsg" /></h5></span>
				<div id="divMsg" align="center"></div>
			</div>
			</div>
			<div class="contentFtr">
			<button type="button" class="btn btn-danger btnBorderRadius" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button>
				<button type="button" class="btn btn-success btnBorderRadius" data-dismiss="modal"
					onclick="distributionEnable()">
					<s:text name="Continue" />
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
		var noOfCrates="<s:property value='noOfCrates'/>";
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		var enableLoanModule = "<s:property value='enableLoanModule'/>";
		var currency ="<s:property value='%{getCurrencyType().toUpperCase()}' />";
		//alert(currency);
		if(tenantId=="fincocoa"){
	    $(".breadcrumb").find('li:not(:first)').remove();
	
		$(".breadcrumb").append("<li><a href='procurementProduct_create.action'>  /  Procurement </a></li> ");
		}
		$("#warehouse").val("");
		$("#city").val("");
		
		$(document).ready(function(){
			var test='<s:property value="%{getGeneralDateFormat()}" />';
			 $.fn.datepicker.defaults.format = test.toLowerCase();
			$('#date').datepicker('setDate', new Date());
			$('#isRegisteredFarmerId0').attr('checked', true);
			jQuery("#edit").hide();
			jQuery(".unregFarmer").hide();
			  //$("#").inputmask({"alias": "currency","prefix":" "});
			  
			  jQuery("#paymentRupee").val("0.00");
			  
			$('input[type=radio][name=isRegisteredFarmer]').change(function() {
				
				if (this.value == '0') {
					jQuery(".unregFarmer").hide();
			
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
					
					jQuery("#farmer").val("");
					resetSelect2();
				}
			});
			processSupplierFunctionality();
			
			if(tenantId=="awi"||tenantId=="AWI"){
				processAwiFunctionality();
			} else if(tenantId=="lalteer"||tenantId=="LALTEER"){
				processLalteerFunctionality();
			}
			
		});
		
		function processAwiFunctionality(){
			jQuery(".farmerSelectionDiv").hide();
			$('#farmerAttnceId0').attr('checked', true);
			
			
			$( "#farmer" ).change(function() {
				var selectedFarmer=jQuery("#farmer").val();
				if(!isEmpty(selectedFarmer)){
					$.post("procurementProduct_populateFarm", {
						selectedFarmer : selectedFarmer
					}, function(data) {
						insertOptions("farm", $.parseJSON(data));
					});
				}
			});
			
		}
		
		function processLalteerFunctionality(){
			jQuery(".farmerSelectionDiv").hide();
			jQuery(".pricePerUnit").hide();
			jQuery(".procGrade").hide();
		}
	/* 	if(enableSupplier!='1'){
			jQuery("#supplierDiv").remove();
			jQuery(".supplierTypeDiv").remove();
		} */
		
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
				$.post("procurementProduct_populateVillage", {
					selectedCity : value
				}, function(data) {
					insertOptions("village", $.parseJSON(data));
				});
			}
		}

		function listFarmer(value) {
			
			var supplier=jQuery("#masterType").val();
			if(supplier=='99'){
				if (!isEmpty(value)) {
					$.post("procurementProduct_populateFarmer", {
						selectedVillage : value
					}, function(data) {
						insertOptions("farmer",$.parseJSON(data));
					});
				}
			}
		}
		function loadFarmerAccountBalance(){
				
				var selectedFarmerValue = $("#farmer option:selected").val();
				var farmerCashBalValue;
				$.post("procurementProduct_populateFarmerAccBalance",{selectedFarmerValue:selectedFarmerValue},function(data){
					if(data!=null && data!=""){
						var dataArr = data.split(",");
						buyerCashBalValue=parseFloat(dataArr[0]).toFixed(2);
						buyerCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
						if(enableLoanModule=='1'){
							$("#farmerloanAmt").text(buyerCashBalValue);
							$("#farmerOutBal").text(buyerCreditBalValue);
							$("#farmerCashBal").text(parseFloat(dataArr[2]).toFixed(2));
						}else{
							document.getElementById("farmerCashBal").innerHTML=buyerCashBalValue;
						}
						
					}
				});
			}

		function listProVarierty(call) {
			var selectedPro = call.value;
			if(!isEmpty(selectedPro)){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "procurementProduct_populateVariety.action",
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
			
			$.post("procurementProduct_populateUnit",{selectedProduct : selectedPro},function(data){
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
			        url: "procurementProduct_populateGrade.action",
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
			$.post("procurementProduct_populatePrice", {
				selectedGrade : selectedGrade
			}, function(data) {
				console.log(data);
				jQuery("#pricePerUnitLabel").val(data);
				//document.getElementById("pricePerUnitLabel").innerHTML=data;
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
			//alert("AAA");
			var unit="";
			var uom="";
			var proUnitVal="";
			jQuery("#validateError").text("");
			var product = $("#procurementProductList").val();
			//alert(tenantId+"****"+product);
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var price = jQuery("#pricePerUnitLabel").val();
			var bags=jQuery("#noOfBagsTxt").val();
			var fruitBags=jQuery("#noOfFruitBagsTxt").val();
			if( tenantId=="kpf" ||tenantId=="simfed"||tenantId=="wub" ){
			 unit=$( "#unitList option:selected" ).text();
			 uom=$( "#unitList" ).val();
			}else{
			 proUnitVal = document.getElementById("productUnit").innerHTML;
			}

			/**Lalteer related fields*/
			var dryLoss="";
			var gradeLoss="";
			var netWeightCalc="";
			if(tenantId=="lalteer"){
				dryLoss=jQuery("#dryLossTxt").val();
				gradeLoss=jQuery("#gradingLossTxt").val();
				netWeightCalc=jQuery("#netWeightCalc").text();
			}
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}
			else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}
			else if(isEmpty(grade) && tenantId!="lalteer"){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			}
			else if(isEmpty(netWeightKg)&&isEmpty(netWeightGm)){
				var error = '<s:property value="%{getLocaleProperty('invalid.grossWeight')}" />';
				jQuery("#validateError").text(error);
				return false;
			}
			if( tenantId=="kpf" ||tenantId=="simfed"||tenantId=="wub"){
				
				if(isEmpty(unit)){
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />'); 
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
			if(isEmpty(fruitBags)){
				fruitBags="0";
			}
			if(isEmpty(dryLoss)){
				dryLoss = "0";
			}
			if(isEmpty(gradeLoss)){
				gradeLoss = "0";
			}
		
			if(enableTraceability=='1'){
				//var batchNo=document.getElementById("batchNo").value;
				var batchNo=jQuery("#batchNo").val();
			
				//}
			//if($("#enableTraceability").val()=='1'){
				//alert("dgfgfg:"+batchNo);
				
				if(batchNo==""){
					//document.getElementById("validatebatchNoError").innerHTML='<s:text name="emptyBatchNo"/>';
					//jQuery("validatebatchNoError").text('<s:property value="%{getLocaleProperty('emptyBatchNo')}" />');
				
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('emptyBatchNo')}" />'); 
					return false;
				}
			} 
			var netWeight=netWeightKg+"."+netWeightGm;
			if(tenantId!="olivado"){
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				return false;
			}}
			
			if(!checkGradeExists(grade)){
				var tableRow="<tr id=row"+(++rowCounter)+">";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";
				
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				if(tenantId!="lalteer" && tenantId!="LALTEER" ){
					tableRow+="<td class='hide gradeId'>"+grade+"</td>";
					tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
					if(enableTraceability=="1"){
						tableRow+="<td class='batchNo textAlignRight'>"+batchNo+"</td>";
						}
					if(tenantId!="kpf" ||tenantId!="simfed"||tenantId!="wub"){
					tableRow+="<td class='unit textAlignRight'>"+proUnitVal+"</td>";
										
					}else{
						tableRow+="<td class='hide unitId'>"+uom+"</td>";
						tableRow+="<td>"+jQuery("#unitList option:selected").text()+"</td>";
					}
					tableRow+="<td class='price textAlignRight'>"+price+"</td>";
					if(tenantId!="pratibha"){
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					}
					if(tenantId=="awi"){
						tableRow+="<td class='fruitBags textAlignRight'>"+fruitBags+"</td>";
					}
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='amount textAlignRight'>"+(parseFloat(price)*parseFloat(netWeight)).toFixed(2)+"</td>";
				}
				else{
					if(enableTraceability=="1"){
					tableRow+="<td class='batchNo textAlignRight'>"+batchNo+"</td>";
				}
					if(tenantId!="pratibha"){
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					}
					if(tenantId=="awi"){
						tableRow+="<td class=='fruitBags textAlignRight'>"+fruitBags+"</td>";
					}
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='dryLoss textAlignRight'>"+dryLoss+"</td>";
					tableRow+="<td class='gradeLoss textAlignRight'>"+gradeLoss+"</td>";
					tableRow+="<td class='netWeights textAlignRight'>"+netWeightCalc+"</td>";
				}
			
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
				var selectedProduct = jQuery(this).find(".productId").text();
				var selectedVariety = jQuery(this).find(".varietyId").text();
				var selectedGrade = jQuery(this).find(".gradeId").text();
				var selectedUnit = jQuery(this).find(".unitId").text();
				var netWeight = jQuery(this).find(".netWeight").text();
				var netWeightSplit = netWeight.split(".");
				
				jQuery("#procurementProductList").val(selectedProduct);
				callTrigger("procurementProductList");
				
				jQuery("#varietyList").val(selectedVariety);
				callTrigger("varietyList");
				
				jQuery("#gradeList").val(selectedGrade);
				callTrigger("gradeList");
				if(enableTraceability=="1"){
				jQuery("#batchNo").val(jQuery(this).find(".batchNo").text());
				}
				console.log("Net weight Split :"+netWeightSplit);
				
				if( tenantId=="kpf"||tenantId=="simfed"||tenantId=="wub"){
				jQuery("#unitList").val(selectedUnit);
				callTrigger("unitList");
				}
				
				jQuery("#pricePerUnitLabel").val(jQuery(this).find(".price").text());
				jQuery("#noOfBagsTxt").val(jQuery(this).find(".bags").text());
				jQuery("#noOfFruitBagsTxt").val(jQuery(this).find(".fruitBags").text());
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
			var unit="";
			var uom="";
			var proUnitVal="";
			var id="#row"+index;
			jQuery(id).empty();
			
			jQuery("#validateError").text("");
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var price = jQuery("#pricePerUnitLabel").val();
			var bags=jQuery("#noOfBagsTxt").val();
			var fruitBags=jQuery("#noOfFruitBagsTxt").val();
			if( tenantId=="kpf"||tenantId=="simfed"||tenantId=="wub"){
				 unit=$( "#unitList option:selected" ).text();
				 uom=$( "#unitList" ).val();
				}else{
				 proUnitVal = document.getElementById("productUnit").innerHTML;
				}
			if(enableTraceability=="1"){
				var batch=jQuery("#batchNo").val();
			}
			/**Lalteer related fields*/
			var dryLoss="";
			var gradeLoss="";
			var netWeightCalc="";
			if(tenantId=="lalteer"){
				dryLoss=jQuery("#dryLossTxt").val();
				gradeLoss=jQuery("#gradingLossTxt").val();
				netWeightCalc=jQuery("#netWeightCalc").text();
			}
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.product')}" />');
				return false;
			}
			else if(isEmpty(variety)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');
				return false;
			}
			else if(isEmpty(grade) && tenantId!="lalteer"){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.grade')}" />'); 
				return false;
			}
			else if(isEmpty(netWeightKg)&&isEmpty(netWeightGm)){
				var error = '<s:property value="%{getLocaleProperty('invalid.grossWeight')}" />';
				jQuery("#validateError").text(error);
			}
			if( tenantId=="kpf"||tenantId=="simfed"||tenantId=="wub"){
				if(isEmpty(unit)){
					jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />'); 
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
			if(isEmpty(fruitBags)){
				fruitBags="0";
			}
			if(enableTraceability=="1"){
				if(isEmpty(batch)){
					batch="";
				}
			}
			if(isEmpty(dryLoss)){
				dryLoss = "0";
			}
			if(isEmpty(gradeLoss)){
				gradeLoss = "0";
			}
			
			
			var netWeight=netWeightKg+"."+netWeightGm;
			if(tenantId!="olivado"){
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				return false;
			}}
			
			if(!checkGradeExists(grade)){
				
				var tableRow="";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";
				
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				if(tenantId!="lalteer" && tenantId!="LALTEER" ){
					tableRow+="<td class='hide gradeId'>"+grade+"</td>";
					tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
					if(enableTraceability=="1"){
						tableRow+="<td class='batchNo textAlignRight'>"+batch+"</td>";
					}
					if(tenantId!="kpf" ||tenantId!="simfed"||tenantId!="wub"){
						tableRow+="<td class='unit textAlignRight'>"+proUnitVal+"</td>";
						}else{
							tableRow+="<td class='unit textAlignRight'>"+unit+"</td>";
						}
					tableRow+="<td class='price textAlignRight'>"+price+"</td>";
					if(tenantId!="pratibha"){
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					}
					if(tenantId=="awi"){
						tableRow+="<td class='fruitBags textAlignRight'>"+fruitBags+"</td>";
						}
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='amount textAlignRight'>"+(parseFloat(price)*parseFloat(netWeight)).toFixed(2)+"</td>";
				}
				else{
					if(enableTraceability=="1"){
						tableRow+="<td class='batchNo textAlignRight'>"+batch+"</td>";
					}
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					if(tenantId=="awi"){
					tableRow+="<td class='fruitBags textAlignRight'>"+fruitBags+"</td>";
					}
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='dryLoss textAlignRight'>"+dryLoss+"</td>";
					tableRow+="<td class='gradeLoss textAlignRight'>"+gradeLoss+"</td>";
					tableRow+="<td class='netWeights textAlignRight'>"+netWeightCalc+"</td>";
				}
				
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
		
		function checkGradeExists(gradeId){
			//alert(gradeId);
			var returnVal = false;
			if(tenantId!="lalteer" && tenantId!="LALTEER"){
				var tableBody = jQuery("#procurementDetailContent tr");
				
				$.each(tableBody, function(index, value) {
					var grade = jQuery(this).find(".gradeId").text();
					//alert(grade);
					if(grade==gradeId){
						alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
						returnVal=true 
					}
				});
			}
			return returnVal;	
		}
		
		function updateTableFooter(){
			var tableBody = jQuery("#procurementDetailContent tr");
			var totalPrice=0.0;
			var totalBags=0.0;
			var totalFruitBags=0.0;
			var totalNetWeight=0.0;
			var totalAmount=0.0;
			var totalDryLoss=0.0;
			var totalGradeLoss=0.0;
			var totalNetWeights=0.0;
			var totalSubTotal=0.0;
			$.each(tableBody, function(index, value) {
				totalPrice+=getFormattedFloatValue(jQuery(this).find(".price").text());
				totalBags+=getFormattedFloatValue(jQuery(this).find(".bags").text());
				totalFruitBags+=getFormattedFloatValue(jQuery(this).find(".fruitBags").text());
				totalNetWeight+=getFormattedFloatValue(jQuery(this).find(".netWeight").text());
				totalDryLoss+=getFormattedFloatValue(jQuery(this).find(".dryLoss").text());
				totalGradeLoss+=getFormattedFloatValue(jQuery(this).find(".gradeLoss").text());
				totalNetWeights+=getFormattedFloatValue(jQuery(this).find(".netWeights").text());
				totalAmount+=getFormattedFloatValue(jQuery(this).find(".amount").text());
				
			});
			$("#pricePerUnitTotalLabel").text(totalPrice.toFixed(2));
			$("#noOfBagsTotalLabel").text(totalBags);
			$("#noOfFruitBagsTotalLabel").text(totalFruitBags);
			$("#grossWeightTotalLabel").text(totalNetWeight.toFixed(2));
			$("#dryLossTotalLabel").text(totalDryLoss.toFixed(2));
			$("#gradeLossTotalLabel").text(totalGradeLoss.toFixed(2));
			$("#netWeightTotalLabel").text(totalNetWeights.toFixed(2));
			$("#subTotalLabel").text(totalAmount.toFixed(2));
			calculateLoanInterest(totalAmount.toFixed(2));
		}
		function calculateLoanInterest(val){
			//alert("AAAA "+val);
			var selectedFarmerValue;
			var finalAmt=0.00;
			var actualAmt=val;
			var procurementFrom=$("#procurementFrom").val();
			selectedFarmerValue = $("#farmer option:selected").val();
			
			
		//	alert(farmer);
			 $.post("procurementProduct_populateLoanInt",{actualAmt :val,selectedFarmerValue:selectedFarmerValue},function(data){
				//alert(data);
				 if(data!=null && data!=""){
				var dataArr = data.split(",");			
						//alert(dataArr);	
				$("#loanIntId").text(parseFloat(dataArr[0]).toFixed(2));
				var outStandingBal=parseFloat(dataArr[1]).toFixed(2);
				var actualAmt = $("#subTotalLabel").text();
				var interestAmt =((parseFloat(dataArr[0]).toFixed(2))/100*(parseFloat(actualAmt)).toFixed(2)); 
				
				if(outStandingBal==0)	{
					$("#finalPayAmt").text($("#subTotalLabel").text());
				}else{
					if(interestAmt>0 && outStandingBal < interestAmt){
						
						var result=	confirm( '*Note\nLoan Repayment Amount ('+currency+' '+interestAmt +'  )is Exceeding his/her Loan OutStanding Balance( '+currency+' '+outStandingBal+' ) \nLoan Repayment Amount will Reduce from your Loan OutStanding Balance');	
							if(result){
								finalAmt = parseFloat(actualAmt) - parseFloat(outStandingBal);
								$("#finalPayAmt").text(finalAmt.toFixed(2));					
								$("#repaymentAmt").text(parseFloat(outStandingBal).toFixed(2));
							}/* else{
								finalAmt = parseFloat(actualAmt) - parseFloat(interestAmt);
								$("#finalPayAmt").text(finalAmt.toFixed(2));
								$("#repaymentAmt").text(parseFloat(0-interestAmt).toFixed(2));
							} */
							}else{
								finalAmt = parseFloat(actualAmt).toFixed(2) - parseFloat(interestAmt).toFixed(2);
								//alert(finalAmt);
								$("#finalPayAmt").text(finalAmt.toFixed(2));
								$("#repaymentAmt").text(parseFloat(interestAmt).toFixed(2));
							}
							
				}
				
				
				
				}else{
					//alert("CCCCC");
					$("#finalPayAmt").text($("#subTotalLabel").text());
				}
			}); 	
			 
		}
		function getFormattedFloatValue(n){
			if(!isNaN(n)){
				return parseFloat(n);
			}else{
				return parseFloat(0);
			}
		}
		
		function resetProductData(){			
			jQuery("#procurementProductList").val("");
			jQuery("#varietyList").val("");
			jQuery("#gradeList").val("");
			
			//document.getElementById("validatebatchNoError").innerHTML="";
			jQuery("validatebatchNoError").text("");
			if(enableTraceability=='1'){
				var batchNo =document.getElementById("batchNo").value;
			  	$('#batchNo').val('');
			 }
			if(tenantId=="kpf"||tenantId=="simfed"||tenantId=="wub"){
			jQuery("#unitList").val("");
			}else{
			document.getElementById("productUnit").innerHTML="";
			}						
			jQuery("#pricePerUnitLabel").val("0.00");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#noOfFruitBagsTxt").val("");
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
			jQuery("#pricePerUnitLabel").val("0.00");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#noOfFruitBagsTxt").val("");
			jQuery("#dryLossTxt").val("");
			jQuery("#gradingLossTxt").val("");
			jQuery("#netWeightCalc").text("0.00");
		
		}
		
		function saveProcurement(){
		//	var mobileUser=jQuery("#agent").val();
		
		$("#sucessbtn").prop('disabled', true);
			var warehouse=jQuery("#warehouse").val();
			var city=jQuery("#city").val();
			var village=jQuery("#village").val();
			var date=jQuery("#date").val();
			var farmerType=$('input[type=radio][name=isRegisteredFarmer]:checked').val();
			var cropType =$('input[type=radio][name=selectedCropType]:checked').val(); 
		
			var farmer=jQuery("#farmer").val();
			var farmerName =jQuery("#farmerName").val();
			var mobileNo = jQuery("#mobileNoInput").val();
			var supplierType=jQuery("#masterType").val();
			var buyer=jQuery("#selectedBuyer").val();
			var farm="";
			var totalAmount="0";
			var loanInt="0";
			var finalPayAmount="0";
			var repaymentAmt="0";
			/**AWI Tenant Specifi Values*/
			var roadMap="";
			var vehicleNumber="";
			var attendance="";
			var substitute="";
			
			var error = "";
			
			
			var procurementProductInfoArray =  buildProcurementProductInfoArray();
			if(isEmpty(procurementProductInfoArray)){
				error = '<s:property value="%{getLocaleProperty('noRecordFound')}" />';
			}
			
			if(tenantId=="awi"){
				roadMap=jQuery("#roadMapCode").val();	
				vehicleNumber = jQuery("#vehicleNo").val();
				attendance = jQuery('input[type=radio][name=farmerAttnce]:checked').val(); 
				substitute = jQuery("#substituteName").val();
				farm = jQuery("#farm").val();
				
				if(isEmpty(roadMap)){
					error = '<s:property value="%{getLocaleProperty('empty.roadMapCode')}" />';
					jQuery("#roadMapCode").focus();
				}else if(isEmpty(vehicleNumber)){
					error = '<s:property value="%{getLocaleProperty('empty.vehicleNo')}" />';
					jQuery("#vehicleNo").focus();
				}else if(attendance=="1"){
					if(isEmpty(substitute)){
						error = '<s:property value="%{getLocaleProperty('invalid.substituteName')}" />';
						jQuery("#substituteName").focus();
					}
				}else if(isEmpty(farm)){
					error = '<s:property value="%{getLocaleProperty('empty.farm')}" />';
					jQuery("#farm").focus();
				}
			}
			
			if(enableSupplier==0){
				
				if(document.getElementById("isRegisteredFarmerId0").checked){
					if(isEmpty(farmer)){
						error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';
						jQuery("#farmer").focus();
					}
				}else{
					if(isEmpty(farmerName)){
						error = '<s:property value="%{getLocaleProperty('empty.farmerName')}" />';
						jQuery("#farmerName").focus();
					}else if(isEmpty(mobileNo)){
						error = '<s:property value="%{getLocaleProperty('invalid.mobileNo')}" />';
						jQuery("#mobileNoInput").focus();
					}
				}
				if(enableBuyer=="1"){
					if(isEmpty(buyer)){
						error = '<s:property value="%{getLocaleProperty('invalid.buyer')}" />';
						jQuery("#selectedBuyer").focus();
					}
				}
				
			}else{
				if(isEmpty(supplierType)){
					error = '<s:property value="%{getLocaleProperty('empty.supplier')}" />';
					jQuery("#masterType").focus();
				}else if(supplierType!="99"&&isEmpty(jQuery("#typeList").val())){
					error = '<s:property value="%{getLocaleProperty('empty.supplierType')}" />';
					jQuery("#typeList").focus();
				}else if(supplierType=="99"||supplierType=="11"){
					if(document.getElementById("isRegisteredFarmerId0").checked){
						if(isEmpty(farmer)){
							error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';
							jQuery("#farmer").focus();
						}
					}else{
						if(isEmpty(farmerName)){
							error = '<s:property value="%{getLocaleProperty('empty.farmerName')}" />';
							jQuery("#farmerName").focus();
						}else if(isEmpty(mobileNo)){
							error = '<s:property value="%{getLocaleProperty('invalid.mobileNo')}" />';
							jQuery("#mobileNoInput").focus();
						}
					}
				}
			}
			if(isEmpty(warehouse)){
				error = '<s:property value="%{getLocaleProperty('empty.warehouse')}"/>';
				jQuery("#warehouse").focus();
			}else if(isEmpty(village)){
				error ='<s:property value="%{getLocaleProperty('empty.village')}" />';
				jQuery("#village").focus();
			}else if(isEmpty(date)){
				error = '<s:property value="%{getLocaleProperty('empty.date')}" />';
				jQuery("#date").focus();
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
			var selectedVehicleType = ""+$("#vehicleType").val();
			if(enableLoanModule=='1'){
				repaymentAmt=$("#repaymentAmt").text();		
				totalAmount=jQuery("#subTotalLabel").text();
				loanInt=jQuery("#loanIntId").text();
				finalPayAmount=jQuery("#finalPayAmt").text();
			
				if(parseFloat(paymentAmount) > parseFloat(finalPayAmount)){
					error ='<s:property value="%{getLocaleProperty('invalidPaymentAmount')}" />';
					jQuery("#paymentRupee").focus();
				}
				
				if(repaymentAmt<0){
					error = 'Repayment Amount Should not in Negative';
					return false;
				}
			}
			if(!isEmpty(error)){
				jQuery("#validateError").text(error);
				$("#sucessbtn").prop('disabled', false);
				return false;
			}
			
			
			var dataa = {
				selectedWarehouse:warehouse,
				selectedDate:date,
				procurementDate:date.toString(),
				selectedFarmerType:farmerType,
				selectedFarmer:farmer,
				paymentAmount:paymentAmount,
				totalAmount:totalAmount,
				productTotalString:procurementProductInfoArray,
				selectedVillage:village,
				farmerName:farmerName,
				mobileNumber:mobileNo,
				farmerType:farmerType,
				selectedSupplier:supplierType,
				selectedMasterType:jQuery("#typeList").val(),
				selectedBuyer:buyer,
				/**AWI*/
				roadMapCode:roadMap,
				vehicleNo:vehicleNumber,
				farmerAttnce:attendance,
				substituteName:substitute,
				selectedFarm:farm,
				selectedCropType:cropType,
				transport_cost:$("#transportCost").val(),
				vehicleType:selectedVehicleType,
				selectedLoanInt : loanInt,
				repaymentAmt : repaymentAmt,
				selectedFinalAmt : finalPayAmount
			}
			console.log(dataa);
			$.ajax({
			        url: 'procurementProduct_populateProcurement.action',
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
			$("#sucessbtn").prop('disabled', false);
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
				productInfo+=jQuery(this).find(".productId").text(); //0
				
				productInfo+="#"+jQuery(this).find(".varietyId").text(); //1
				
				productInfo+="#"+jQuery(this).find(".gradeId").text(); //2
				
				
				productInfo+="#"+jQuery(this).find(".bags").text(); //3
				
				
				productInfo+="#"+jQuery(this).find(".netWeight").text(); //4
				
				productInfo+="#"+jQuery(this).find(".amount").text(); //5
				
				productInfo+="#"+jQuery(this).find(".dryLoss").text();//6		
				
				productInfo+="#"+jQuery(this).find(".gradeLoss").text();//7
				if(enableTraceability=="1"){
					productInfo+="#"+jQuery(this).find(".batchNo").text(); //8
				}
				 productInfo+="#"+jQuery(this).find(".netWeights").text();//9
				/* productInfo+="#"+jQuery(this).find(".unitId").text()+"@";//10 
				productInfo+="#"+jQuery(this).find(".netWeight").text(); //5 */
				productInfo+="#"+jQuery(this).find(".unitId").text(); //10
				
				productInfo+="#"+jQuery(this).find(".fruitBags").text()+"@"; //11
				//alert(productInfo);
				
				
			});
			//alert(productInfo);
			return productInfo;
		}
		
		function resetSelect2(){
			$(".select2").select2();
		}
		
		function calcWeight(val){
			//alert(val +" *** "+ noOfCrates);
			var totalWeight=0.00;
			if(!isEmpty(val)){
			totalWeight=noOfCrates * val;
			//alert(totalWeight);
			if(tenantId=="fruitmaster"){
				$("#grossWeightKgTxt").val(totalWeight);
			}
			
			}
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
			if(tenantId!="olivado"){
			if(parseFloat(netWeight)<=parseFloat(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
			}}
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
			if(!isEmpty(value)){
				if(value=='99'){
					$("#supplierTypeLabel").text("");
					$("#typeList").val("");
					$(".supplierTypeDiv").hide();
					$(".farmerDiv").show();
					$(".regFarmer").show();
				}else if(value=='11'){
					$(".supplierTypeDiv").show();
					$(".farmerSelectionDiv").hide();
					$(".unregFarmer").hide();
					$(".regFarmer").show();
					$("#supplierTypeLabel").text($("#masterType option:selected").text());
					
					$.post("procurementProduct_populateSamithi", {
						selectedMasterType : value
					}, function(data) {
						insertOptions("typeList", $.parseJSON(data));
					});
					
					$("#typeList").change(function(){
						{
							$.post("procurementProduct_populateFarmerByFPO", {
								selectedSupplier : $(this).val()
							}, function(data) {
								insertOptions("farmer", $.parseJSON(data));
							});
						}
					});
				}else{
					$(".supplierTypeDiv").show();
					jQuery(".farmerDiv").hide();
					$(".regFarmer").hide();
					$(".farmerSelectionDiv").hide();
					$(".unregFarmer").hide();
					$("#supplierTypeLabel").text($("#masterType option:selected").text());
					$.post("procurementProduct_populateMasterData", {
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
		}
		
		
		function buttonEdcCancel(){
			//refreshPopup();
			document.getElementById("model-close-edu-btn").click();	
			if(tenantId!="fincocoa"  && tenantId!="movcd" && tenantId!="pgss"){
			window.location.href="procurementProduct_list.action";
	     }else{
	    	 window.location.href="procurementProduct_create.action";
	    	 }
	     }
		
		function farmerAttendance(obj){
			if(obj.value=='1'){
					jQuery(".substituteName").show();
			}else{
				jQuery(".substituteName").hide();
			}  
		}
		
		function onCancel() {
			if(tenantId!="fincocoa"  && tenantId!="movcd" && tenantId!="pgss" ){
				window.location.href="procurementProduct_list.action";
		      }else{
		    	 window.location.href="procurementProduct_create.action";
		    	 } 
		}
		
		function disablePopupAlert(){
			window.location.href="procurementProduct_list.action";
			document.redirectform.submit();
		}
		
		 function isDecimal(evt) {
	    		
	    	 evt = (evt) ? evt : window.event;
	    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	    	        return false;
	    	    }
	    	    return true;
	    }
		 function resetDatas()
		 {
			 	jQuery("#warehouse").val("");
				jQuery("#city").val("");
				jQuery("#village").val("");			 
				jQuery("#date").val("");
				jQuery("#farmer").val("");
				jQuery("#farmerName").val(""); 
				jQuery("#mobileNoInput").val("");
				jQuery("#validateError").text("");	
				jQuery("#procurementDetailContent tr").remove();
				updateTableFooter();
				resetProductData();			 	
				resetTableData();								
		 }
		 function distributionEnable(){
			 window.location.href="procurementProduct_create.action";
			}

	</script>
</body>