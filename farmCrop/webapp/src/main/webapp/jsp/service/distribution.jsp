<%@ include file="/jsp/common/report-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style type="text/css">
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

#productInfoTbl th, td {
	text-align: center;
}

#productInfo th, td {
	text-align: center;
}

.centerCls {
	text-align: center;
}

.datepicker_clear {
	display: none;
}

.baseDistributionTable {
	margin: 0 auto 15px;
	width: 99.8%; /*border:solid 1px #666;*/
}
.pdalign{
	text-align: center;
    padding-top: 21px;
}

/*.column-1 h3, .column-2 h3, .column-3 h3{background: none repeat scroll 0 0 #94ad07 !important; border: 1px solid #fff !important; color: #FFFFFF !important; padding: 5px; margin:0; text-align: left;}*/
.column-1 h3, .column-2 h3, .column-3 h3 {
	/*background: none repeat scroll 0 0 #6F9505 !important;*/
	border: 1px solid #fff !important;
	color: #fff !important;
	padding: 5px;
	margin: 0;
	text-align: left;
}

/*.column-2 h3{background: none repeat scroll 0 0 #5f8101 !important; border: 1px solid #567304 !important; color: #FFFFFF !important; padding: 5px; margin:0; text-align: left;}
	.column-3 h3{background: none repeat scroll 0 0 #476001 !important; border: 1px solid #476001 !important; color: #FFFFFF !important; padding: 5px; margin:0; text-align: left;}*/
.alignCenter {
	text-align: center !important;
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

.popPendingMTNTAlert {
	display: none;
	clear: both;
	position: absolute;
	top: 100px;
	left: 450px;
	width: 360px;
	height: 110px;
	padding: 25px 20px 0px;
	background-color: #ebfac2;
	border: 1px solid #567304;
	text-align: center;
	z-index: 1;
}

.pageTitle {
	display: none;
}
</style>
</head>


<s:form name="form" cssClass="fillform" action="distribution_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" />
	<s:hidden key="command" />
	<s:hidden id="farmerId" name="farmerId" />
	<s:hidden id="farmerName" name="farmerName" />
	<s:hidden key="selecteddropdwon" id="listname" />
	<s:hidden key="temp" id="temp" />
	<s:hidden id="enableBatchNo" name="enableBatchNo" />

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

				<font color="red"> <s:actionerror /></font>

				<h2>
					<s:text name="info.general" />
				</h2>
				<div class="flexiWrapper filterControls marginBottom">
				<div class="flexi flexi12" style="width:130px">
								<label for="txt"><s:text name="distributionDate" /></label>
								<div class="form-element">
									<s:textfield name="startDate" id="startDate" readonly="true"
										theme="simple"
										data-date-format="%{getGeneralDateFormat().toLowerCase()}"
										data-date-viewmode="years"
										cssClass="date-picker form-control input-sm" size="20" />
								</div>
							</div>

					<div class="flexi flexi12" style="width:auto;">
						<label for="txt"><s:text name="stock"></s:text></label>
						<div class="">
							<label class="" style="margin-left:10px;"> <input type="radio"
								value="true" id="isCooperativeStock" onchange="resetAllData()"
								name="productStock" checked="checked" />&nbsp; <s:text
									name="%{getLocaleProperty('warehouse')}" />
							</label><label class="" style="margin-left:10px;"> <input type="radio"
								value="false" id="isFieldStaffStock" onchange="resetAllData()"
								name="productStock" />&nbsp; <s:text name="%{getLocaleProperty('fieldStaff.stock')}" /></label>
						</div>
					</div>

					<s:if test="harvestSeasonEnabled==1">
						<div class="flexi flexi12" style="width:130px">
							<label for="txt"><s:text name="season.name" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedSeason" id="season"
									list="harvestSeasonList"
									onchange="populateWarehouseCategory();resetAllData();"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" cssClass="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
					<div class="flexi flexi12 warehouseData warehousetd" style="width:130px">
						<label for="txt"><s:text name="%{getLocaleProperty('warehouse.name')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:if test="currentTenantId!='lalteer'">
								<s:select name="selectedCooperative" list="coopearativeList"
									onchange="populateWarehouseCategory();resetWarehouseData();resetUnitData();"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="cooperative"
									cssClass="form-control input-sm select2" />
							</s:if>
							<s:else>

								<s:select name="selectedCooperative" list="coopearativeList"
									onchange="populateLalteerWarehouseCategory();resetWarehouseData();resetUnitData();"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="cooperative"
									cssClass="form-control input-sm select2" />
							</s:else>
						</div>
					</div>
					<div class="flexi flexi12 mobileUserData fieldstafftd" style="width:130px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('agentName')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:if test="currentTenantId!='lalteer'">
								<s:select name="selectedAgent" list="agentLists"
									cssStyle="width:235px;"
									onchange="populateAgentCategory(this);resetWarehouseData();"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="fieldstaff"
									cssClass="form-control input-sm select2" />
							</s:if>
							<s:else>

								<s:select name="selectedAgent" list="agentLists"
									cssStyle="width:235px;"
									onchange="populatelalteerAgentCategory(this);resetWarehouseData()"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="fieldstaff"
									cssClass="form-control input-sm select2" />
							</s:else>
						</div>
					</div>
					 <s:if test="currentTenantId!='livelihood'">
					 <div class="flexi flexi12" style="width:auto;">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer')}" /></label>
						<div class="">
							<label class="" style="margin-left:10px;">  <input type="radio"
								value="true" id="isRegisteredFarmer" name="registeredFarmer"
								checked="checked"> <s:text name="farmer.registered" /></label>
							<label class="" style="margin-left:10px;"> <input type="radio"
								value="false" id="isUnRegisteredFarmer" name="registeredFarmer" />&nbsp;
								<s:text name="farmer.unregistered" /></label>
						</div>
					</div> 
				</s:if>
			

					<div class="flexi flexi12 regFarmerData samithitd" style="width:130px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('city.name')}" /> </label>
						<div class="form-element">
							<s:select name="selectedCity" id="cities" list="cities"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}" theme="simple"
								onchange="listVillageByCity(this)"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
                   <s:if test="currentTenantId!='livelihood'">
					<div class="flexi flexi12 " style="width:130px">
						<label for="txt"><s:text name="village.name" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select cssClass="form-control input-sm select2"
								name="selectedVillage" list="{}" theme="simple" id="village"
								onchange="listFarmer(this);" headerKey=""
								headerValue="%{getText('txt.select')}" />
						</div>
					</div>
				</s:if>
				<s:if test="currentTenantId=='livelihood'">
					<div class="flexi flexi12 " style="width:130px">
						<label for="txt"><s:text name="village.name" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select cssClass="form-control input-sm select2"
								name="selectedVillage" list="{}" theme="simple" id="village"
								onchange="listFarmerLivelihood(this);" headerKey=""
								headerValue="%{getText('txt.select')}" />
						</div>
					</div>
				</s:if>
					<div class="flexi flexi12 farmercombotd" style="width:130px">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.name')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select cssClass="form-control input-sm select2"
								name="selectedFarmer" onchange="loadFarmerAccountBalance();"
								list="farmerList" headerKey=""
								headerValue="%{getText('txt.select')}" theme="simple"
								id="farmer" listKey="key" listValue="value" />
						</div>
					</div>

					<div class="flexi flexi12 unregFarmerData" style="width:130px">
						<label for="txt"><s:property value="%{getLocaleProperty('farmerName')}" /></label>
						<div class="form-element">
							<s:textfield id="farmerNameInput" name="unRegisterFarmerName"
								maxlength="35" cssClass="form-control input-sm " />
						</div>
					</div>
					<div class="flexi flexi12 unregFarmerData" style="width:130px">
						<label for="txt"><s:text name="farmer.mobileNumber" /></label>
						<div class="form-element">
							<s:textfield id="mobileNoInput" name="mobileNo"
								cssClass="form-control input-sm" maxlength="11"
								onkeypress="return isDecimal(event,this)" />
						</div>
					</div>
                    
					<s:if test="harvestSeasonEnabled==0">
						<div class="flexi flexi12 seasonAlign" style="width:130px">
							<div class="form-element">
								<b><s:text name="season.name" />:<s:property
										value="seasonName" /></b>
							</div>
						</div>
					</s:if>

				</div>
				
				
				
			</div>
		</div>
		<div class="service-content-wrapper">
			<div class="service-content-section">

				<div class="appContentWrapper">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="productDetails" />
							<b><s:checkboxlist list='checkBoxs' name="freeDistribution"
										id="checkFreeDis" theme="simple" onclick="isCheck(this);" /></b>
						</h2>
						<div class="flexiWrapper filterControls marginBottom">
							<%-- <div class="flexi flexi12" style="text-align: center;padding-top: 21px;">
									<s:checkboxlist list='checkBoxs' name="freeDistribution"
										id="checkFreeDis" theme="simple" onclick="isCheck(this);" />
							</div> --%>
							<div class="flexi flexi12" style="width:200px">
								<label for="txt"><s:text name="subCategory" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:if test="currentTenantId=='lalteer'">
										<s:select name="selectedCategory"
											cssClass="form-control input-sm select2" list="categoryList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="category"
											onchange="listProductLalteer(this);resetUnitData();" />
									</s:if>
									<s:else>
										<s:select name="selectedCategory"
											cssClass="form-control input-sm select2" list="categoryList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="category" onchange="listProduct(this);resetUnitData();" />
									</s:else>
								</div>
							</div>

							<div class="flexi flexi12" style="width:200px">
								<label for="txt"><s:text name="product" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:if test="enableBatchNo ==1">
										<s:select name="selectedProduct"
											cssClass="form-control input-sm select2"
											list="productsNameUnitMap" headerKey=""
											headerValue="%{getText('txt.select')}" id="product1"
											onchange="listBatchNo();loadUnit();" />
									</s:if>
									<s:else>
										<s:select name="selectedProduct"
											cssClass="form-control input-sm select2"
											list="productsNameUnitMap" headerKey=""
											headerValue="%{getText('txt.select')}" id="product1"
											onchange="resetUnitData();listCostPrice(this);availableStock();loadUnit();" />

									</s:else>
								</div>
							</div>
							<s:if test="enableBatchNo ==1">

								<div class="flexi flexi12">
									<label for="txt"><s:text
											name="warehouseProduct.batchNo" /><span class="manadatory">*</span></label>
									<div class="form-element">
										<s:select name="selectedBatchNo" list="batchNoList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="batchNo" cssClass="form-control input-sm select2"
											onchange="resetUnitData();listCostPrice(this);availableStock()" />
									</div>
								</div>
							</s:if>
							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="warehouseProduct.availableStock" /></label>
								<div class="form-element">
									<span class="avlStkTxt" id="stock"
										style="display: block; text-align: center"></span>

								</div>
							</div>
							
							<div class="flexi flexi12 hide">
								<div class="form-element">
									<s:select name="selectedUnit" list="{}" headerKey="1-Unit"
										cssClass="form-control input-sm select2" headerValue="1-Unit"
										id="unit1" onchange="" cssStyle="display:none;" />

									<s:select name="subUnit" id="subUnit"
										onchange="calulateAvailableStock();" list="{}" disabled="true"
										cssStyle="width:80px!important; display:none;" />
								</div>
							</div>



							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="distribution.costPrice" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</label>
								<div class="form-element">
									<span id="costPrice" style="display: block; text-align: center" />
								</div>
							</div>
							<%-- <div class="flexi flexi10 hide" id="sellingRupeeTd">
								<label for="txt"><s:text
										name="distribution.sellingPrice" /><span class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="selllingRupee" name="sellingRupee"
										maxlength="10" onkeypress="return isDecimal(event,this)"
										onkeyup="calulateAvailableStock()" />

								</div>
							</div> --%>
							<div class="flexi flexi12 ">
								<label for="txt"><s:text name="distributionStock" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="distributionStock" name="distributionStock"
											maxlength="8" onkeypress="return isDecimal(event,this)"
											onkeyup="calulateAvailableStock()"
											cssStyle="text-align:right;padding-right:1px; width:60px!important;" />
										<div>.</div>
										<s:textfield id="stockPaise" name="stockPaise" maxlength="3"
											onkeypress="return isDecimal(event,this)"
											onkeyup="calulateAvailableStock()"
											cssStyle="text-align:right;padding-right:1px; width:30px!important;" />
									</div>
								</div>
							</div>
							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="distribution.unit" /></label>
								<div class="form-element">
									<span id="productUnit"
										style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12" id="totalPriceDiv">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="totalPrice" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</label>
								<div class="form-element">
									<span id="totalPriceLabel"
										style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12">
                               <label for="txt"><s:text name="action" /></label>
								<td colspan="4" class="alignCenter" >
								<table class="actionBtnWarpper">

								<td class="textAlignCenter">

									<button type="button" class="btn btn-sm btn-success"
										aria-hidden="true" onclick="validateData()"  
										title="<s:text name="Ok" />">
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
				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="distributedProducts" />
						</h2>
						<div class="flexiWrapper filterControls">
							<table id="productInfoTbl"
								class="table table-bordered aspect-detail">
								<thead>
									<tr class="odd">

										<th width="5%" style="text-align: center"><s:text
												name="s.no" /></th>
										<th width="15%" style="text-align: center"><s:text
												name="subCategory" /></th>
										<th width="15%" style="text-align: center"><s:text
												name="product" /></th>
										<s:if test="enableBatchNo ==1">
											<th width="8%" style="text-align: center"><s:text
													name="warehouseProduct.batchNo" /></th>
										</s:if>
										<!-- <th width="17%"><s:text name="units" /></th>
				<th width="11%"><s:text name="totalQuantity" /></th>  -->
										<th width="8%" style="text-align: center"><s:text
												name="distribution.unit" /></th>
										<th width="8%" id="costPriceLable" style="text-align: center"><s:text
												name="distribution.costPrice" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
									<%-- 	<th width="8%" id="sellingPriceLevel"
											style="text-align: center"><s:text
												name="distribution.sellingPrice" /></th> --%>
										<th width="9%" style="text-align: center"><s:text
												name="quantity" /></th>
										<th width="11%" id="finalPriceLabel"><s:text
												name="totalPrice" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
										<th width="9%"><s:text name="action" /></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="6" style="text-align: center;"><s:text
												name=" " /></td>
									</tr>
								</tbody>
								<tfoot id="footerId">
									<tr>
										<td style="border: none !important;">&nbsp;</td>
										<td colspan="3" class="alignRight"><s:text name="total" /></td>
										<td class="alignRight">0</td>
										<td style="border: none !important;">&nbsp;</td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
				
				
				<div class="appContentWrapper marginBottom" id="paynmentAmountDiv"
					style="display: block;">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="dist.paymnent" />
						</h2>
						<div class="flexiWrapper filterControls">
							<table class="table table-bordered aspect-detail">

								<tbody>
									<tr class="odd paymnt">
										<th width="25%"><s:text name="dist.balance" /></th>
										<th width="25%"><s:text name="dist.type" /></th>
										<th width="25%" class="cashTypeDivLabel"><s:text
												name="dist.payment" /></th>
										<th width="25%" class="creditTypeDivLabel"
											style="display: none;"><s:text name="dist.amount" /></th>
									</tr>

									<tr>

										<td class="row paymnt">
											<div class="row paymnt">
												<div class="col-sm-3">
													<s:text name="dist.cash" />
												</div>
												<div class="col-sm-3" id="distributionCashBal"
													style="font-weight: bold;"></div>
												<div class="col-sm-3">
													<s:text name="dist.credit" />
												</div>
												<div class="col-sm-3" id="distributionCreditBal"
													style="font-weight: bold;"></div>
											</div>
										</td>

										<td><label class="payementAmt "> <input
												type="radio" name="amtType" checked="checked" class="paymnt"
												id="distribution_create_amtType0" value="1"
												onchange="hideDivBasedOnCashType(this);"> <s:text
													name="%{getLocaleProperty('payementAmt')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)
										</label> <label class="radio-inline paymnt"> <input
												type="radio" class="paymnt" name="amtType"
												id="distribution_create_amtType1"
												onchange="hideDivBasedOnCashType(this);"> <s:text
													name="dist.radio2" />
										</label></td>

										<td class="cashTypeDiv"><input type="text"
											class="fullwidth textAlignRight" name="paymentRupee" value=""
											id="paymentRupee" maxlength="10" onkeypress="return isDecimal(event,this)"></td>

										<td class="creditTypeDiv" style="display: none;">
											<div id="farmerCreditAmt"></div>
										</td>

									</tr>
								</tbody>
							</table>
						</div>
	</div></div>
	</div>


	<s:hidden id="productList" name="distributionProductList" />
</s:form>
						

		<s:if test="distImgAvil==1">
				<s:form name="distImageform" cssClass="fillform" action="distribution_createImage.action"
				method="post" enctype="multipart/form-data" id="imgForm">
					<s:hidden key="currentPage" />
					<s:hidden key="id" id="distId" name="distId" />
					<div class="appContentWrapper marginBottom" id="paynmentAmountDiv"
					style="display: block;">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="dist.image" />
						</h2>
						
						<div class="flexiWrapper filterControls">
							<div class="flexform-item">
								<label for="txt"> <s:property
								value="%{getLocaleProperty('distribution.image1')}" /> <span
									style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
								</span>
								</label>
								<div class="form-element">
								<s:file name="distImg1" id="distImg1"
									onchange="checkImgHeightAndWidth(this)" cssClass="form-control" />
								</div>
							</div>
						
						
							<div class="flexform-item">
								<label for="txt"> <s:property
								value="%{getLocaleProperty('distribution.image2')}" /> <span
									style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
								</span>
								</label>
								<div class="form-element">
								<s:file name="distImg2" id="distImg2"
									onchange="checkImgHeightAndWidth(this)" cssClass="form-control" />
								</div>
							</div>
						
						
						
						
						</div>
					</div>
				</div>
				</s:form>
				</s:if>
			<s:form name="butform" cssClass="fillform">
					<s:hidden key="currentPage" />
					<s:hidden key="id" id="distId" name="distId" />
					<div class="appContentWrapper marginBottom" id="paynmentAmountDiv"
					style="display: block;">
					<div class="formContainerWrapper">
						<div class="flexiWrapper filterControls">
							<div class="yui-skin-sam" id="loadList" style="display: block">
							<sec:authorize
								ifAllGranted="service.distribution.fieldStaff.create">
								<span id="savebutton" class=""><span class="first-child">
										<button type="button" class="save-btn btn btn-success"
											onclick="submitDistribution();" id="sucessbtn">
											<font color="#FFFFFF"> <b><s:text
														name="save.button" /></b>
											</font>
										</button>
								</span></span>

							</sec:authorize>
							<span class="yui-button"><span class="first-child"><a
									id="cancelbutton" onclick="redirectForm();"
									class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
											<s:text name="cancel.button" />
									</font>
								</a></span></span>
						</div>
						</div>
						</div></div>
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
</div>
 --%>


<button type="button" data-toggle="modal" data-target="#slideModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>

<!-- Modal -->
<div id="slideModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="contentHdr">
				<button type="button" class="close" data-dismiss="modal">&times;</button>	
				<%-- <h4 class="modal-title">
					<s:text name="service.seedDistribution.farmer" />
				</h4> --%>
			
			<div class="modal-body">
			<i class="fa fa-check" aria-hidden="true"></i></br>
			 <span><h5><s:text name="distributionMsg" /></h5></span>
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

<%-- <div id="slideModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="contentHdr">
				<button type="button" class="close" data-dismiss="modal">&times;</button>			
			<div class="modal-body">
				<i class="fa fa-check" aria-hidden="true"></i></br>
            <span><h5>Distribution Done Successfully</h5></span>
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

</div> --%>

<script src="plugins/jquery-input-mask/jquery.inputmask.bundle.min.js"></script>
<script type="text/javascript">

var tenant = '<s:property value="%{getCurrentTenantId()}" />';
var harvestSeasonEnable='<s:property value="%{getHarvestSeasonEnabled()}" />';
var distImageEnable='<s:property value="%{getDistImgAvil()}" />';


jQuery(document).ready(function(){
	document.getElementById("isCooperativeStock").checked = true;
	if(tenant!="livelihood"){
	document.getElementById("isRegisteredFarmer").checked = true;
	}
	jQuery(".unregFarmerData").hide();
	$(".paymnt").hide();
	$("#sucessbtn").prop('disabled', false);
	//startTimer("distribution");
	jQuery(".fieldstafftd").hide();
	<s:if test='distributionDescription!=null'>
		jQuery('<tr><td><s:property value="distributionDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
		jQuery("#restartAlert").css('height','150px');
	</s:if>
	var selectedWarehose=jQuery("#warehouse option:selected").text();
	//var selectedCooperative=jQuery("#cooperative option:selected").text();
	jQuery("#areaLabel").html(selectedWarehose.split('-')[0]);
	//jQuery("#isCooperativeStock").attr('checked',true);
    // isCheck(document.getElementById('checkFreeDis'));
    hideDivBasedOnCashType(document.getElementById('cashId'));
    
  //  $("#mobileNoInput").inputmask({"mask": "(+99) 99999-99999"}); //specifying options

  //  $("#selllingRupee").inputmask({"alias": "currency","prefix":""}); //specifying options
    //sellingPrice

    $("#paymentRupee").inputmask({"alias": "currency","prefix":" "});
    
    resetPrefixAndSuffix();
    $('#paymentRupee').val("");
    //document.getElementById("startDate").value='<s:property value="currentDate" />';
    $('#startDate').datepicker('setDate', new Date());
    
    if($("#enableBatchNo").val()=='1'){
    	
    	var th = ($('table#productInfoTbl th:last').index() + 1);
    
    	var values= th - 3;
    	
    	 $('#totalAmt').attr("colspan",values);
    	$('#taxAmt').attr("colspan",values);
    	$('#finalAmt').attr("colspan",values); 
    	
    	}else{
    		var th = ($('table#productInfoTbl th:last').index() + 1);	
    		var values= th - 3;
    		 $('#totalAmt').attr("colspan",values);
    			$('#taxAmt').attr("colspan",values);
    			$('#finalAmt').attr("colspan",values); 
    		
    	}

});
var printWindowCnt=0;
var windowRef;

function hideDivBasedOnCashType(evt){
/* var cashVal = $(evt).val();
//alert(cashVal);
	if(cashVal==''){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("farmerCreditAmt").innerHTML="";
	}
	else if(cashVal==0){
		jQuery(".cashTypeDivLabel").show();
		jQuery(".cashTypeDiv").show();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("farmerCreditAmt").innerHTML="";	
	}
	else if(cashVal==1){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").show();
		jQuery(".creditTypeDiv").show();
		document.getElementById("farmerCreditAmt").innerHTML=document.getElementById("finalTaxAmount").innerHTML;	
	}
	else {
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").show();
		jQuery(".creditTypeDiv").show();
		document.getElementById("farmerCreditAmt").innerHTML=document.getElementById("finalTaxAmount").innerHTML;	
	}
	 */
	
	
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

function isCheck(evt){
if(jQuery(evt).is(":checked")==true){
	resetWarehouseData();
	//reloadTable(); 
	   $(".sellingDiv").hide();
	   $("#totalPriceDiv").hide();
	   $("#amountLable").hide();	  
	 //  $("#sellingRupeeTd").hide();
	 //  $("#sellingPriceLevel").hide();
	  // $("#distributionStockUnitLable").hide();
	   $("#finalPriceLabel").hide();
	   $("#footerId").hide();
	   $("#paynmentAmountDiv").hide();
	   $('input[name=amtType]').attr("checked", false);
	   hideDivBasedOnCashType(document.getElementById('cashId'));
	  
   }
if(jQuery(evt).is(":checked")==false)
   {
	resetWarehouseData();
	//reloadTable(); 
	   $('#isFree').val("N");
	   $(".sellingDiv").show();
	   $("#totalPriceDiv").show();
	   $("#amountLable").show();
	  // $("#sellingRupeeTd").show();
	
	   $("#distributionStockUnitLable").show();
	 //  $("#sellingPriceLevel").show();
	   $("#finalPriceLabel").show();
	   $("#footerId").show();
	   $("#paynmentAmountDiv").show();
	   $('input[name=amtType]').attr("checked", false);
	   hideDivBasedOnCashType(document.getElementById('cashId'));
	   
	   
   } 
}


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

var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();

function listFarmer(call){
	var selectedWarehose=jQuery("#warehouse option:selected").text();
	var farmerType=jQuery('#isRegisteredFarmer').is(':checked');
	if( call.value==""){
		document.form.selectedFarmer.length = 0;
		addOption(document.form.farmer, '<s:text name="txt.select"/>', "");
	}
	else if(farmerType){
	 $.post("distribution_populateFarmer", {selectedVillage: call.value,selectedWarehouse:selectedWarehose}, function (data) {
				/*var result=data;
				result = result.replace("{","").replace("}","");
				var arry =result.split("=");
				document.form.selectedFarmer.length = 0;
				addOption(document.form.farmer, '<s:text name="txt.select"/>', "");
				 for (var i=0; i < arry.length;i++){
						if(arry[i]!="")
							addOption(document.form.farmer, arry[i], arry[i]);
				}*/
				$("#farmer").empty();
				var result=data;
				if(result.length > 2)
				{
					
					addOption(document.form.farmer, 'Select', '0');
					result = result.replace("{","").replace("}","");
					var farmersArr =result.split(",");
					for (var i=0; i < farmersArr.length;i++){
						var arr = farmersArr[i].split("=");
						addOption(document.form.farmer, arr[1], arr[0]);
					}
				}
		});
	}

}

function listFarmerLivelihood(call){
	var selectedWarehose=jQuery("#warehouse option:selected").text();
	if( call.value==""){
		document.form.selectedFarmer.length = 0;
		addOption(document.form.farmer, '<s:text name="txt.select"/>', "");
	}
	else{
	 $.post("distribution_populateFarmer", {selectedVillage: call.value,selectedWarehouse:selectedWarehose}, function (data) {
				/*var result=data;
				result = result.replace("{","").replace("}","");
				var arry =result.split("=");
				document.form.selectedFarmer.length = 0;
				addOption(document.form.farmer, '<s:text name="txt.select"/>', "");
				 for (var i=0; i < arry.length;i++){
						if(arry[i]!="")
							addOption(document.form.farmer, arry[i], arry[i]);
				}*/
				
				var result=data;
				if(result.length > 2)
				{
					$("#farmer").empty();
					addOption(document.form.farmer, 'Select', '0');
					result = result.replace("{","").replace("}","");
					var farmersArr =result.split(",");
					for (var i=0; i < farmersArr.length;i++){
						var arr = farmersArr[i].split("=");
						addOption(document.form.farmer, arr[1], arr[0]);
					}
				}
		});
	}

}


function listCostPrice(){
	var selectedCooperative=document.getElementById("cooperative").value;
	//alert("selectedCooperative"+selectedCooperative);
	var selectedProduct = document.getElementById("product1").value;
	var selectedAgent=document.getElementById("fieldstaff").value;
	if(tenant!="lalteer"){
	$.post("distribution_populateCostPrice",{selectedCooperative:selectedCooperative,selectedProduct:selectedProduct,selectedAgent:selectedAgent},function(data){
			if(data!=null && data!=""){
				//alert("aaa");
				document.getElementById("costPrice").innerHTML=data;
				//document.getElementById("selllingRupee").value=data;
		}
			else{
				document.getElementById("costPrice").innerHTML=0;	
				//document.getElementById("selllingRupee").value=0;	
			}	

		//	alert("---------->"+document.getElementById("costPrice").innerHTML);
	});
}else{
	$.post("distribution_populateLalteerCostPrice",{selectedCooperative:selectedCooperative,selectedProduct:selectedProduct,selectedAgent:selectedAgent},function(data){
		if(data!=null && data!=""){
			//alert("aaa");
			document.getElementById("costPrice").innerHTML=data;
			//document.getElementById("selllingRupee").value=data;
	}
		else{
			document.getElementById("costPrice").innerHTML=0;	
			//document.getElementById("selllingRupee").value=0;	
		}	

	//	alert("---------->"+document.getElementById("costPrice").innerHTML);
});
}
}

function listProductLalteer(call){	
	
	resetUnitData();	
	var selectedCooperative = document.getElementById("cooperative").value;
	var cat= call.value;
	selectedCooperative=(selectedCooperative==0)?"":selectedCooperative;
	cat=(cat==0)?"":cat;

	if(selectedCooperative !=""&cat!=""){
		
	$.ajax({
		 type: "POST",
         async: false,
         url: "distribution_populateLalteerProduct",
         data: {selectedCategory: call.value,selectedCooperative:selectedCooperative},
         success: function(result) {
        	 if(result.length==0){
        		 
        		 /* $('#product1 option').remove();
        		 $("#product1").append('<option val=" ">Select</option>'); */
        	 }else{
        		 insertOptions("product1",JSON.parse(result));
        		 
        	 }
         }
	});
	
		  /* $.post("distribution_populateProduct", {selectedCategory: call.value,selectedCooperative:selectedCooperative}, function (result) {
			  insertOptions("product1",JSON.parse(result));
      }); */
	
	reset();
	}
}


function resetWarehouseData()
{
	    	resetUnitData();
			resetProductData();
			resetUnitData();
			//reloadTable(); 
			 //listSubcategory();
			 
			 resetEditFlag();
			 if(productsInfoArray.length != 0){

			  for(var i=0; i< productsInfoArray.length; i++){
			  productsInfoArray.splice(i);
			  }
			  
			}
			reloadTable(); 
			
			}

function listProduct(call){	
	if(tenant!="lalteer"){
	resetUnitData();	
	var selectedCooperative = document.getElementById("cooperative").value;
	
	if(selectedCooperative !=""){
		
	$.ajax({
		 type: "POST",
         async: false,
         url: "distribution_populateProduct",
         data: {selectedCategory: call.value,selectedCooperative:selectedCooperative},
         success: function(result) {
        	 if(result.length==0){
        		 
        		 /* $('#product1 option').remove();
        		 $("#product1").append('<option val=" ">Select</option>'); */
        	 }else{
        		 insertOptions("product1",JSON.parse(result));
        	 }
         }
	});
	
		  /* $.post("distribution_populateProduct", {selectedCategory: call.value,selectedCooperative:selectedCooperative}, function (result) {
			  insertOptions("product1",JSON.parse(result));
      }); */
	
	reset();
	}
	}
}



function populateProductCooperative(call){	
	
	  $.post("distribution_populateProductCooperative", {selectedCooperative: call.value}, function (result) {
		  
		  insertOptions("product1",JSON.parse(result));
        
      });
	
	resetProductData();	   
}


function populateWarehouseCategory(){	
	 $('#category option').remove();
	 $('#category').append('<option value=" ">Select</option>');
	 $('#product1 option').remove();
	 $('#product1').append('<option value=" ">Select</option>');
	 $("#stock").html(" ");
	var seasonCode =$("#season option:selected").val();
	var selectedCooperative =$("#cooperative option:selected").val();
	if(seasonCode.length==0){
		 $('#category option').remove();
		 $('#category').append('<option value=" ">Select</option>');
		 $('#product1 option').remove();
		 $('#product1').append('<option value=" ">Select</option>');
		 $("#stock").html(" ");
		//document.getElementById("validateError").innerHTML='<s:text name="selectSeason"/>';
	}else if(selectedCooperative.length==0){
	//	document.getElementById("validateError").innerHTML='<s:text name="selectWarehouse"/>';
		$('#category option').remove();
		//var newOption = $('<option value="''">'"Select"'</option>');
		 $('#category').append('<option value=" ">Select</option>');
		 $('#product1 option').remove();
		 $('#product1').append('<option value=" ">Select</option>');
		 $("#stock").html(" ");
		
	}
	else{
	$.post("distribution_populateWarehouseCategory", {selectedCooperative:selectedCooperative,seasonCode:seasonCode}, function (result) {
    	insertOptions("category",JSON.parse(result));
    });
	
	resetProductData();	   
	}	   
}
function populateLalteerWarehouseCategory(){	
	 $('#category option').remove();
	 $('#category').append('<option value=" ">Select</option>');
	 $('#product1 option').remove();
	 $('#product1').append('<option value=" ">Select</option>');
	 $("#stock").html(" ");
	
	var selectedCooperative =$("#cooperative option:selected").val();
if(selectedCooperative.length==0){
	//	document.getElementById("validateError").innerHTML='<s:text name="selectWarehouse"/>';
		$('#category option').remove();
		//var newOption = $('<option value="''">'"Select"'</option>');
		 $('#category').append('<option value=" ">Select</option>');
		 $('#product1 option').remove();
		 $('#product1').append('<option value=" ">Select</option>');
		 $("#stock").html(" ");
		
	}
	else{
	$.post("distribution_populateLalteerWarehouseCategory", {selectedCooperative:selectedCooperative}, function (result) {
   	insertOptions("category",JSON.parse(result));
   });
	
	resetProductData();	   
	}	   
}


function populateAgentCategory(call){	
	 var selectedSeason= document.getElementById("season").value;
	
	  $.post("distribution_populateAgentCategory", {selectedAgent: call.value,selectedSeason:selectedSeason}, function (result) {
		  
		  insertOptions("category",JSON.parse(result));
	
	    });
	
	resetProductData();	   
}
function populatelalteerAgentCategory(call){	
	 var selectedSeason= document.getElementById("season").value;
	
	  $.post("distribution_populateLalteerAgentCategory", {selectedAgent: call.value,selectedSeason:selectedSeason}, function (result) {
		  
		  insertOptions("category",JSON.parse(result));
	
	    });
	
	resetProductData();	   
}
function populateProductFieldStaff(call){

	/*  if($("#enableBatchNo").val()=='1'){
		 batchNo =document.getElementById("batchNo").value;
	 }else{
		 batchNo='NA';
	 } */
	var selectedAgent = document.getElementById("fieldstaff").value;
	 var selectedSeason= document.getElementById("season").value;
		if(selectedAgent!=null && selectedAgent!="")
		{
			$.ajax({
				 type: "POST",
		         async: false,
		         url: "distribution_populateProductFieldStaff",
		         data: {selectedCategory: call.value,selectedAgent:selectedAgent,selectedSeason:selectedSeason,batchNo:'NA'},
		         success: function(result) {
		        	  insertOptions("product1",JSON.parse(result));
			
		         }	
		    	
		    });
		}
   
}
function populateLalteerProductFieldStaff(call){	
	var selectedAgent = document.getElementById("fieldstaff").value;
	
		if(selectedAgent!=null && selectedAgent!="")
		{
			$.ajax({
				 type: "POST",
		         async: false,
		         url: "distribution_populateLalteerProductFieldStaff",
		         data: {selectedCategory: call.value,selectedAgent:selectedAgent},
		         success: function(result) {
		        	  insertOptions("product1",JSON.parse(result));
			
		         }	
		    	
		    });
		}
   
}


function listVillage(call){
	var selectedValue=jQuery("#warehouse").val();
	if(selectedValue==""){
		document.form.village.length = 0;
		addOption(document.getElementById("village"), '<s:text name="txt.select"/>', "");
		document.form.selectedFarmer.length = 0;
		addOption(document.form.farmer, '<s:text name="txt.select"/>', "");
	}else{
	     $('#village').html('');
	     addOption(document.getElementById("village"), '<s:text name="txt.select"/>', "");
		 $.post("distribution_populateVillage", {selectedWarehouse: selectedValue}, function (data) {
		var result=data;
		var arry =populateValues(result);
		document.form.village.length = 0;
		addOption(document.getElementById("village"),'<s:text name="txt.select"/>', "");
	      
	
		 for (var i=0; i < arry.length;i++){
				if(arry[i]!="")
					addOption(document.getElementById("village"), arry[i], arry[i]);			
				}
		 
		 document.form.selectedFarmer.length = 0;
			addOption(document.form.farmer, '<s:text name="txt.select"/>', "");			
	       
	    });
	
	}
}

function listVillageForUnregisteredFarmer(){
		
	 $.post("distribution_populateVillageForUnRegisteredFarmer", function (result) {		
			document.form.village.length = 0;
			addOption(document.getElementById("village"),'<s:text name="txt.select"/>', "");
    	 insertOptions("village",JSON.parse(result));
    });

}


function listUnits(call){
    var productUnits=call.value;
	var start=productUnits.indexOf("=");
	 var resultString=productUnits.substring(start+1);
	 var arry=resultString.split('~');
	document.form.unit1.length = 0;
	addOption(document.getElementById("unit1"), '<s:text name="txt.select"/>', "");
	for (var i=0; i < arry.length;i++){
		if(arry[i]!="")
			addOption(document.getElementById("unit1"), arry[i], arry[i]);
		}	
	resetUnitData();
}

function populateMapValues(responseValue){
	var start=responseValue.indexOf("{");
	var end=responseValue.indexOf("}");
	var resultString=responseValue.substring(start+1, end);
	return resultString.split(',');
}

function validateData() {
	var hit=validateDetails(true);
	if(hit){
		var totalPrice=jQuery("#totalPriceLabel").val();
		 var isFreeDistributionChk=jQuery('#checkFreeDis-1').is(":checked");
		if(parseFloat(totalPrice)==0 && (!isFreeDistributionChk)){
			document.getElementById("validateError").innerHTML="invalidLowStock";
		}else{
		   document.getElementById("validateError").innerHTML="";
		   
		   reload();
		}
	}
}

function calAmntWithTax() {

 var totalAmount=document.getElementById("totalAmtVal").innerHTML;
 var taxValue = $("#amntTax").val();
 var finalTaxAmt = (taxValue/100 * totalAmount);
 //var finalTaxAmt=taxValue;
	var finalAmtWithTax = parseFloat(finalTaxAmt) + parseFloat(totalAmount);
	if(taxValue!=""){
		
		document.getElementById("finalTaxAmount").innerHTML=parseFloat(finalAmtWithTax).toFixed(2);
		document.getElementById("farmerCreditAmt").innerHTML=parseFloat(finalAmtWithTax).toFixed(2);
		}
	else{
		document.getElementById("finalTaxAmount").innerHTML=totalAmount;
		document.getElementById("farmerCreditAmt").innerHTML=totalAmount;
		
	}
	
	
}


function validateDetails(validateEmpty){
	var hit=true;
	var selectedCategory=document.getElementById("category").value;
	var selectedProduct = document.getElementById("product1").value;
	var selectedUnit=document.getElementById("unit1").value;
	var distributionStock = document.getElementById("distributionStock").value;
	var stockPaise = document.getElementById("stockPaise").value;
	
	var availableStock = document.getElementById("stock").innerHTML;
	availableStock=availableStock.split('-')[0];
	var units=jQuery.trim(jQuery("#unit1").val()).split('-');
	var subUnit=jQuery("#subUnit").val();
	var isPartial=!(subUnit===selectedUnit);
	var coPrice=document.getElementById("costPrice").innerHTML;
	var QtyPerUnit,toatalQty;
    var isFreeDist=jQuery('#checkFreeDis-1').is(":checked");    
   // var sellRupee=$('#selllingRupee').val();
	//var sellPrice = sellRupee.replace(',','');
	var isFreeDistrChk=jQuery('#checkFreeDis-1').is(":checked");
	var distStock=distributionStock+"."+stockPaise;

    if(!isFreeDist){
	if(selectedProduct==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectProduct"/>';
		hit=false;
	}
	else if(selectedUnit==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectUnit"/>';
		hit=false;
	}/* else if(sellPrice=="")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty"/>';
		hit=false;
	} */
	/* else if((sellRupee == 0)){
		document.getElementById('validatePriceError').innerHTML='<s:text name="empty.sellingPrice" />';
		return false;
	}
	else if((sellRupee <= 0)){
		document.getElementById('validatePriceError').innerHTML='<s:text name="min.price" />';
		return false;
	} */
	/* else if(parseFloat(sellPrice) <= parseFloat(coPrice))
	{
		document.getElementById("validateError").innerHTML='<s:text name="invalid.price"/>';
		hit=false;
	} */
	else if(isNaN(distributionStock)==true){
		document.getElementById("validateError").innerHTML='<s:text name="invalidStock"/>';
		hit=false;	 
	}
	else if(parseFloat(availableStock)<=0){
		document.getElementById("validateError").innerHTML='<s:text name="insufficientstock"/>';
		hit=false;
	}
	else if(validateEmpty && distStock.trim()=="" || parseFloat(distStock)<=0){
		document.getElementById("validateError").innerHTML='<s:text name="validStock"/>';
		hit=false;
	}
			
	else if((validateEmpty  || distributionStock.trim()!="")  && parseFloat(distributionStock) > parseFloat(availableStock)){
			document.getElementById("validateError").innerHTML='<s:text name="insufficientstock"/>';
			hit=false;
	
    }
	else if(isPartial &&(!isFreeDistrChk)){
			if(units[1].toUpperCase()==="KG"  && subUnit.toUpperCase()=="GM"){
				toatalQty=units[0]*1000;
			}else if(units[1].toUpperCase()==="LT"  && subUnit.toUpperCase()=="ML"){
				toatalQty=units[0]*1000;
			}else{
				toatalQty=units[0];
			}
			
			QtyPerUnit=(parseFloat(distStock)/toatalQty).toFixed(4);
			if(parseFloat(QtyPerUnit)==0){
				document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
				hit=false;
			}
	}else if(!isPartial &&(!isFreeDistrChk)){
		QtyPerUnit=(parseFloat(distStock)).toFixed(4);
		if(parseFloat(QtyPerUnit)==0){
			document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
			hit=false;
		}
	}
    }
    else {
    if(selectedProduct==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectProduct"/>';
		hit=false;
    }
    else if(selectedUnit==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectUnit"/>';
		hit=false;
	}
    else if(isNaN(distributionStock)==true){
		document.getElementById("validateError").innerHTML='<s:text name="invalidStock"/>';
		hit=false;	 
	}
	else if(parseFloat(availableStock)<=0){
		document.getElementById("validateError").innerHTML='<s:text name="insufficientstock"/>';
		hit=false;
	}
	else if(validateEmpty && distStock.trim()=="" || parseFloat(distStock)<=0){
		document.getElementById("validateError").innerHTML='<s:text name="validStock"/>';
		hit=false;
	}
			
	else if((validateEmpty  || distStock.trim()!="")  && parseFloat(distStock) > parseFloat(availableStock)){
			document.getElementById("validateError").innerHTML='<s:text name="insufficientstock"/>';
			hit=false;
	}
	
	
    }
	return hit;
}

function listBatchNo()
{
var selectedProduct = document.getElementById("product1").value;
var selectedCooperative = document.getElementById("cooperative").value;
var selectedUnit=document.getElementById("unit1").value;

var availableStock = document.getElementById("stock").value;
var distributionStock = document.getElementById("distributionStock").value;
var selectedAgent=document.getElementById("fieldstaff").value;

var selectedCategory=document.getElementById("category").value;
 var selectedSeason= document.getElementById("season").value;
var hit=true;

if(selectedProduct==0){
	//resetProductData();
	hit=false;
}
if(selectedUnit==""){
	resetUnitData();
	hit=false;
}
if(selectedSeason==""){
	resetUnitData();
	hit=false;	
}
if(hit) {
	var units=selectedUnit.split('-');
	document.getElementById("validateError").innerHTML="";
	document.getElementById("distributionStock").disabled=false;
	
		
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "distribution_populateBatchNo.action",
	         data: {selectedCooperative:selectedCooperative,selectedProduct:selectedProduct,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedCategory:selectedCategory,selectedSeason:selectedSeason},
	        success: function(result) {
	        	insertOptions("batchNo",JSON.parse(result));
	        }
		});
		
}

}
function availableStock(){
	var selectedProduct = document.getElementById("product1").value;
	var selectedCooperative = document.getElementById("cooperative").value;
	var selectedUnit=document.getElementById("unit1").value;
	var availableStock = document.getElementById("stock").value;
	var distributionStock = document.getElementById("distributionStock").value;
	var selectedAgent=document.getElementById("fieldstaff").value;
	var selectedCategory=document.getElementById("category").value;
	 var selectedSeason= document.getElementById("season").value;
	 var batchNo="NA";
	 if($("#enableBatchNo").val()=='1'){
		 batchNo =document.getElementById("batchNo").value;
	  if(batchNo==""){
			resetUnitData();
			hit=false;	
		}
	 }
	var hit=true;
	
	if(selectedProduct==0){
		//resetProductData();
		hit=false;
	}
	if(selectedUnit==""){
		resetUnitData();
		hit=false;
	}
	if(selectedSeason==""){
		resetUnitData();
		hit=false;	
	}
	
	if(hit) {
		var units=selectedUnit.split('-');
		document.getElementById("validateError").innerHTML="";
		document.getElementById("distributionStock").disabled=false;
		if(tenant!="lalteer"){
		
	$.ajax({
			 type: "POST",
	         async: false,
	         url: "distribution_loadAvailableStock",
	         data: {selectedCooperative:selectedCooperative,selectedProduct:selectedProduct,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedCategory:selectedCategory,selectedSeason:selectedSeason,batchNo:batchNo},
	         success: function(data) { 
		
		
			var n=data.split(",");

			for(var i=0; i< productsInfoArray.length; i++){
				if(productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo ){
					n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
				}
			}
			document.getElementById('distributionStock').disabled = false;
			document.getElementById('subUnit').disabled = false;
			if(n[0] == "N/A" || n[1] == "N/A"){
				document.getElementById('distributionStock').disabled = true;
				document.getElementById('subUnit').disabled = true;
			}
            if(n[0]<=0){
            	document.getElementById('distributionStock').disabled = true;
				document.getElementById('subUnit').disabled = true;				
			}
				
			var totalQty;
			totalQty=(parseFloat(n[0])).toFixed(4);
			document.getElementById("subUnit").length=0;
			
			addOption(document.getElementById("subUnit"),selectedUnit,selectedUnit);
			if(parseFloat(units[0])!=1){
			addOption(document.getElementById("subUnit"), units[1], units[1]);
			}
            if(units[1].toUpperCase()==="KG"){	
            	addOption(document.getElementById("subUnit"), "gm", "gm");
            }else if(units[1].toUpperCase()==="LT"){
            	addOption(document.getElementById("subUnit"), "ml", "ml");
            }		
            totalQty = parseFloat(totalQty).toFixed(3);
           
			$("#stock").text(totalQty);
			$("#productPrice").text(n[1]);


			document.getElementById('distributionStock').value="";
			jQuery("#totalPriceLabel").html('');
			document.getElementById("validateError").innerHTML="";
			jQuery("#distributionStock").focus();
			
		}   
		});
		} else{
			

			
			$.ajax({
					 type: "POST",
			         async: false,
			         url: "distribution_loadAvailableStockLalteer",
			         data: {selectedCooperative:selectedCooperative,selectedProduct:selectedProduct,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedCategory:selectedCategory,batchNo:batchNo},
			         success: function(data) { 
				
				
					var n=data.split(",");

					for(var i=0; i< productsInfoArray.length; i++){
						if(productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo ){
							n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
						}
					}
					document.getElementById('distributionStock').disabled = false;
					document.getElementById('subUnit').disabled = false;
					if(n[0] == "N/A" || n[1] == "N/A"){
						document.getElementById('distributionStock').disabled = true;
						document.getElementById('subUnit').disabled = true;
					}
		            if(n[0]<=0){
		            	document.getElementById('distributionStock').disabled = true;
						document.getElementById('subUnit').disabled = true;				
					}
						
					var totalQty;
					totalQty=(parseFloat(n[0])).toFixed(4);
					document.getElementById("subUnit").length=0;
					
					addOption(document.getElementById("subUnit"),selectedUnit,selectedUnit);
					if(parseFloat(units[0])!=1){
					addOption(document.getElementById("subUnit"), units[1], units[1]);
					}
		            if(units[1].toUpperCase()==="KG"){	
		            	addOption(document.getElementById("subUnit"), "gm", "gm");
		            }else if(units[1].toUpperCase()==="LT"){
		            	addOption(document.getElementById("subUnit"), "ml", "ml");
		            }		
		            totalQty = parseFloat(totalQty).toFixed(3);
		           
					$("#stock").text(totalQty);
					$("#productPrice").text(n[1]);


					document.getElementById('distributionStock').value="";
					jQuery("#totalPriceLabel").html('');
					document.getElementById("validateError").innerHTML="";
					jQuery("#distributionStock").focus();
					
				}   
				});
		}  
	        
	}
}


function loadUnit(){
	
	var selectedProduct = document.getElementById("product1").value;
	var productName=document.getElementById("product1").value;
	
	var availableUnit;
	$.post("distribution_populatePopulateProductUnit",{selectedProduct:selectedProduct},function(data){
		
			
		var jsonData = $.parseJSON(data);
		 $.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
			}else if(value.id=="costPrice"){
				if(value.name!=null&&value.name!==undefined&&value.name!=''){
					var costSplit = (value.name).split(".");
					$("#costPriceRupees").val(costSplit[0]);
					$("#costPricePaise").val(costSplit[1]);
				}
			}
		}); 
	});
	
}


function showProductEnrollDetail(){	
	jQuery("#product1").focus();
}

function reload() {
	addRow();
	resetProductData();
}


function addRow(){
	jQuery('#category').prop("disabled",false);
	jQuery('#product1').prop("disabled",false);
	jQuery('#batch').prop("disabled",false);
	
	var editIndex=getEditIndex();
	var categoryId=document.getElementById("category").value;
	var categoryName = $("#category option:selected").text();
	var prodName=$("#product1 option:selected").text();
	var catName=$("#category option:selected").text(); 
	var enableBatchNo=$("#enableBatchNo").val();
	var batchNo="NA";
	if(enableBatchNo=='1'){
	 batchNo=$("#batchNo option:selected").text();
	}
	/* var categoryName = selectizeText("#category");
	var prodName = selectizeText("#product1");
	var catName = selectizeText("#category"); */
	var productName=document.getElementById("product1").value;
	
	var availableStock = document.getElementById("stock").innerHTML;
	var stockPre=document.getElementById("distributionStock").value;
	var stockPaise=document.getElementById("stockPaise").value;
	var distributionStock = stockPre+"."+stockPaise;
	var coPrice=document.getElementById("costPrice").innerHTML;
	var Price=document.getElementById("costPrice").value;
	//var sellingPrice=document.getElementById("sellingPrice").value;
	//var pricePerUnit=document.getElementById("productPrice").innerHTML;
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	var proUnitValue = document.getElementById("productUnit").innerHTML;
	var units=document.getElementById("unit1").value;
	var selectedProduct = document.getElementById("product1").value;
    var subUnit=jQuery("#subUnit").val();
	var Qty,QtyPerUnit;
	var unitArr=units.split('-');
	var subUnit=jQuery('#subUnit').val();
	Qty=distributionStock;
	var isPartial=!(jQuery("#subUnit").val()===units);
	//var sellRupee=$('#selllingRupee').val();
	//var sellingPrice = sellRupee.replace(",","");
	var selectedBatchNo="NA";
	if(enableBatchNo=='1'){
		batchNo = document.getElementById("batchNo").value;
	}
	var totalStockPrice=calculateTotalPrice(distributionStock,coPrice,isPartial,units.split('-'),subUnit);
	//var totalStockPrice=calculateTotalPrice(distributionStock,sellingPrice,isPartial,units.split('-'),subUnit);	
	if(isPartial){
		if(unitArr[1].toUpperCase()==="KG" &&  subUnit.toUpperCase()=="GM"){
			QtyPerUnit=(parseFloat(Qty)/(parseFloat(unitArr[0])*1000)).toFixed(4);
			Qty=(parseFloat(Qty)/1000).toFixed(4);
		}else if(unitArr[1].toUpperCase()==="LT" &&  subUnit.toUpperCase()=="ML"){
			QtyPerUnit=(parseFloat(Qty)/(parseFloat(unitArr[0])*1000)).toFixed(4);
			Qty=(parseFloat(Qty)/1000).toFixed(4);
		}else{
	        QtyPerUnit=(parseFloat(Qty)/parseFloat(unitArr[0])).toFixed(4);
		    Qty=(parseFloat(Qty)).toFixed(4);
		
		}
	}else{
		Qty=(parseFloat(Qty)*parseFloat(unitArr[0])).toFixed(4);
		QtyPerUnit=(parseFloat(distributionStock)).toFixed(4);
	}
	var productArray=null;
	var productExists=false;
	if(editIndex==-1){
	 productArray = {
			 productId :selectedProduct,
			 name : productName.split('=')[0],
			 distributionStock : Qty+"-"+unitArr[1],
			 units: units, 
			 //pricePerUnit : pricePerUnit,
			 coPrice:coPrice,
			 categoryName:categoryId,
			 //sellingPrice:sellingPrice,
			 totalStockPrice : totalStockPrice,
			 distributionType : isPartial,
			 Qty : Qty,
			 QtyPerUnit:QtyPerUnit,
			 subUnit:subUnit,
			 unit : proUnitVal,
			 enteredStock:parseFloat(distributionStock).toFixed(4),
			 isEdit:false,
			 prodName:prodName,
			 batchNo:batchNo,
			 catName:catName
         };


	 for(var i=0;i<productsInfoArray.length;i++){
		 if(productsInfoArray[i].productId== productArray.productId && productsInfoArray[i].batchNo== productArray.batchNo){
				Qty=parseFloat(productsInfoArray[i].Qty)+(parseFloat(productArray.Qty));
				QtyPerUnit=parseFloat(productsInfoArray[i].QtyPerUnit)+(parseFloat(productArray.QtyPerUnit));
				totalStockPrice=parseFloat(productsInfoArray[i].totalStockPrice)+parseFloat(productArray.totalStockPrice);
				distributionStock=parseFloat(productsInfoArray[i].distributionStock)+(parseFloat(productArray.distributionStock));
				enteredStock=parseFloat(productsInfoArray[i].enteredStock)+(parseFloat(productArray.enteredStock));
				productsInfoArray[i].distributionStock = Qty+"-"+unitArr[1];
				productsInfoArray[i].units= units;
				//productsInfoArray[i].pricePerUnit = pricePerUnit;
				//productsInfoArray[i].sellingPrice = sellingPrice;
				productsInfoArray[i].totalStockPrice=parseFloat(totalStockPrice).toFixed(4);
				productsInfoArray[i].Qty = Qty.toFixed(4);
				productsInfoArray[i].QtyPerUnit=QtyPerUnit.toFixed(4);
				productsInfoArray[i].subUnit=subUnit;
				productsInfoArray[i].enteredStock=enteredStock.toFixed(4);	
				productsInfoArray[i].batchNo=batchNo;
				productExists=true;
			}
		}
		if(!productExists)
	 	productsInfoArray[productsInfoArray.length] = productArray;
		
	}else{
		productArray=productsInfoArray[editIndex];
	    editproductTotalArray(productArray);
	    //productsInfoArray[editIndex].productId= selectedProduct;
		productsInfoArray[editIndex].name= productName.split('=')[0];
		productsInfoArray[editIndex].distributionStock = Qty+"-"+unitArr[1];
		productsInfoArray[editIndex].units= units;
		//productsInfoArray[editIndex].pricePerUnit = pricePerUnit;
		productsInfoArray[editIndex].coPrice = coPrice;		
		//productsInfoArray[editIndex].sellingPrice = sellingPrice;
		productsInfoArray[editIndex].totalStockPrice = totalStockPrice;
		productsInfoArray[editIndex].distributionType = isPartial;
		productsInfoArray[editIndex].Qty = Qty;
		productsInfoArray[editIndex].QtyPerUnit=QtyPerUnit;
		productsInfoArray[editIndex].batchNo=batchNo;
		productsInfoArray[editIndex].subUnit=subUnit;
		productsInfoArray[editIndex].enteredStock=parseFloat(distributionStock).toFixed(2);
		productsInfoArray[editIndex].isEdit=false;
		productArray=productsInfoArray[editIndex];
	}
	var isExist=false;
	var pName=productName.split('=')[0]+'='+units;
	for(var i=0;i<productTotalArray.length;i++){
		if(productTotalArray[i].name==pName	&& productTotalArray[i].batchNo==batchNo){
			//productTotalArray[i].qty=(parseFloat(productTotalArray[i].qty)+parseFloat(QtyPerUnit)).toFixed(3);
			productTotalArray[i].qty= QtyPerUnit;
			productTotalArray[i].coPrice= coPrice;
			//productTotalArray[i].pricePerUnit = pricePerUnit; 
			//productTotalArray[i].sellingPrice = sellingPrice;
			productTotalArray[i].totalStockPrice =totalStockPrice;
			productTotalArray[i].batchNo=batchNo;
			isExist=true;
			break;
		}
	}
	if(!isExist){
		
		var  totalArray={
				name:	productName.split('=')[0]+'='+units,
		        qty :   QtyPerUnit,
		        units:units,
		        //pricePerUnit : pricePerUnit,
		        coPrice:coPrice,
		        //sellingPrice:sellingPrice,
		        batchNo:batchNo,
		        distributionStock : distributionStock,
		        enteredStock:parseFloat(distributionStock).toFixed(4),
		        totalStockPrice : totalStockPrice,
		}
		productTotalArray[productTotalArray.length]=totalArray;
		
	}
reloadTable();
//$('#selllingRupee').val('');

}

function reloadTable(){
	var enableBatchNo=$("#enableBatchNo").val();
	var tbodyRow = "";
	var tfootArray = new Array();	
	var isFree = jQuery('#checkFreeDis-1').is(":checked")
	jQuery('#productInfoTbl > tbody').html('');
	var rowCount=0;

	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		if(!isFree){
			
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].catName+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].prodName+'</td>'+
					 ((enableBatchNo === '1') ? '<td style="text-align:center;">'+productsInfoArray[cnt].batchNo+'</td>' :'') +
					//'<td style="text-align:left;">'+productsInfoArray[cnt].units+'</td>'+
					//'<td style="text-align:right;">'+productsInfoArray[cnt].distributionStock+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].unit+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].coPrice+'</td>'+
					//'<td style="text-align:center;">'+productsInfoArray[cnt].sellingPrice+'</td>'+
					'<td style="text-align:center; width="5%" " >'+productsInfoArray[cnt].QtyPerUnit+'</td>'+
					//'<td style="text-align:right;">'+productsInfoArray[cnt].pricePerUnit+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].totalStockPrice+'</td>'+
					/* '<td><button type="button" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')"></button>'+
					'<button type="button" class="fa fa-trash" aria-hidden="true " onclick="removeRow('+cnt+')"></button></td>'+ */
					 '<td style="width :6%"><i title="Edit" style="cursor: pointer;color: white;background-color: #337ab7;padding: 5px 10px; font-size: 13px; line-height: 1.5;margin-left: 10px;" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')"></i>'+
					 '<i title="Delete" class="fa fa-trash" style="cursor: pointer;color: #fff;background-color: #c9302c;border-color: #761c19;padding: 5px 10px;font-size: 13px;line-height: 1.5;margin-left: 25px;" aria-hidden="true " onclick="removeRow('+cnt+')"></i></td>'+
					'</tr>';	
		}
		else{
			
			tbodyRow += '<tr>'+
			'<td style="text-align:center;">'+(cnt+1)+'</td>'+
			'<td style="text-align:center;">'+productsInfoArray[cnt].catName+'</td>'+
			'<td style="text-align:center;">'+productsInfoArray[cnt].prodName+'</td>'+	
			 ((enableBatchNo === '1') ? '<td style="text-align:center;">'+productsInfoArray[cnt].batchNo+'</td>' :'') +
			//'<td style="text-align:left;">'+productsInfoArray[cnt].units+'</td>'+
			//'<td style="text-align:right;">'+productsInfoArray[cnt].distributionStock+'</td>'+
		    //'<td style="text-align:right;">'+productsInfoArray[cnt].sellingPrice+'</td>'+
		    '<td style="text-align:center;">'+productsInfoArray[cnt].unit+'</td>'+
		    '<td style="text-align:center;">'+productsInfoArray[cnt].coPrice+'</td>'+		    
			'<td style="text-align:center; width="5%"">'+productsInfoArray[cnt].QtyPerUnit+'</td>'+
			//'<td style="text-align:right;">'+productsInfoArray[cnt].pricePerUnit+'</td>'+
		    //'<td style="text-align:right;">'+productsInfoArray[cnt].totalStockPrice+'</td>'+
			/* '<td class="alignCenter borNone" style="border:none!important;"><button type="button" class="fa fa-pencil-square-o" onclick="editRow('+cnt+')"></button>'+
			'<button type="button" class="fa fa-trash" onclick="removeRow('+cnt+')"></button></td>'+ */
			 '<td style="width :6%"><i style="cursor: pointer;color: white;background-color: #337ab7;padding: 5px 10px; font-size: 13px; line-height: 1.5;margin-left: 10px;" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')"></i>'+
			 '<i class="fa fa-trash" style="cursor: pointer;color: #fff;background-color: #c9302c;border-color: #761c19;padding: 5px 10px;font-size: 13px;line-height: 1.5;margin-left: 25px;" aria-hidden="true " onclick="removeRow('+cnt+')"></i></td>'+
			'</tr>';	
		}
		if(tfootArray.length==0){
			tfootArray[tfootArray.length] = {
											 totalPrice : productsInfoArray[cnt].totalStockPrice
										 	};
		}else{ 
		   tfootArray[tfootArray.length-1].totalPrice=(parseFloat(tfootArray[tfootArray.length-1].totalPrice)+parseFloat(productsInfoArray[cnt].totalStockPrice)).toFixed(4);
		}
	  }	
	}	

	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="11" style="text-align:center"><s:text name="noDistributionRecordFound"/></td>'+
					'</tr>';
	}
		
	jQuery('#productInfoTbl > tbody').html(tbodyRow);
	var taxAmntValue = $("#amntTax").val();	
	var tfootRow = "";
	jQuery('#productInfoTbl > tfoot').html('');
	tfootRow += '<tr>'+
	'<td colspan="6" id="totalAmt" style="border:none!important;">&nbsp;</td>'+
	'<td class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="total"/></td>';

	if(tfootArray.length>0){
			tfootRow +=		'<td id="totalAmtVal" class="totColBorder" style="text-align : center">'+tfootArray[tfootArray.length-1].totalPrice+'</td>';
	}else{
		 tfootRow +='<td class="alignRight" class="totColBorder" style="text-align : center">0.00</td>';
	}
	tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
				'</tr>';


	tfootRow += '<tr>'+
				'<td colspan="6" id="taxAmt"  style="border:none!important;">&nbsp;</td>'+
				'<td  class="alignRight"><s:text name="distribution.tax"/></td>';
	tfootRow+='<td class="alignRight"><s:textfield name="tax" maxlength="5" id="amntTax" onkeyup="calAmntWithTax();" onkeypress="return isDecimal(event,this)"  cssStyle="text-align:center;padding-right:1px; width:140px!important;"/></td>';
			  '</tr>';
    tfootRow += '<tr>'+
				'<td colspan="6" id="finalAmt" style="border:none!important;">&nbsp;</td>'+
				'<td  class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="distribution.finalAmt"/>(<s:property value="%{getCurrencyType().toUpperCase()}" />)</td>';
if(!isFree){
   if(tfootArray.length>0){
		tfootRow +=		'<td id="finalTaxAmount" style="text-align : center">'+parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2)+'</td>';
		jQuery("#farmerCreditAmt").text(parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2));
	}else{
		tfootRow +='<td id="finalTaxAmount" style="text-align : center">0.00</td>';
	}
   tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
	'</tr>';
}
    else if(tfootArray.length>0 && taxAmntValue!=""){
    	
	 tfootRow +='<td id="finalTaxAmount" class="alignRight">'+finalAmtWithTax+'</td>';
	}else{
		tfootRow +='<td id="finalTaxAmount" class="alignRight">0.00</td>';
	}
tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
'</tr>';
											
jQuery('#productInfoTbl > tfoot').html(tfootRow);
jQuery("#product1").focus();
jQuery("#category").focus();
if(enableBatchNo=='1'){
	jQuery("#batchNo").focus();
	}
	
if($("#enableBatchNo").val()=='1'){
	
	var th = ($('table#productInfoTbl th:last').index() + 1);
	
	var values= th - 3;
	
	 $('#totalAmt').attr("colspan",values);
	$('#taxAmt').attr("colspan",values);
	$('#finalAmt').attr("colspan",values); 
	
	}else{
		var th = ($('table#productInfoTbl th:last').index() + 1);	
		var values= th - 3;
		 $('#totalAmt').attr("colspan",values);
			$('#taxAmt').attr("colspan",values);
			$('#finalAmt').attr("colspan",values); 
		
	}
}

function removeRow(indx){
	for(var i=0;i<productTotalArray.length;i++){
	    if(productTotalArray[i].name==productsInfoArray[indx].name+'='+productsInfoArray[indx].units){
		productTotalArray[i].qty-=productsInfoArray[indx].QtyPerUnit;
		productTotalArray[i].units-=productsInfoArray[indx].units;
		productTotalArray[i].pricePerUnit-=productsInfoArray[indx].pricePerUnit;
		productTotalArray[i].distributionStock-=productsInfoArray[indx].distributionStock;
		productTotalArray[i].enteredStock-=productsInfoArray[indx].enteredStock;
		productTotalArray[i].totalStockPrice-=productsInfoArray[indx].totalStockPrice;
		
		break;
		}
	}
	if(productTotalArray.length>0){	
	 productTotalArray.splice(indx,1);	
	}if(productsInfoArray.length>0){
		 productsInfoArray.splice(indx,1);	
	} 
	resetProductData();
	reloadTable();
}

function buildReqProductObject(){
	
	pArrayList = new Array();	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		pArrayList[pArrayList.length] =					productsInfoArray[cnt].name.split('=')[0]+'|'+
														productsInfoArray[cnt].QtyPerUnit +'|'+
														productsInfoArray[cnt].units +'|'+
														productsInfoArray[cnt].pricePerUnit +'|'+
														productsInfoArray[cnt].batchNo +'|'+
														productsInfoArray[cnt].totalStockPrice +'|'+productsInfoArray[cnt].coPrice;
	}	

}


function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}

function submitDistribution() {
	try{
	$("#sucessbtn").prop('disabled', true);
	var stockType=false;
    var hit=true;
   
	var selectedFarmer = document.getElementById("farmer").value;
	var selectedWarehouse = document.getElementById("cooperative").value;
	var selectedSamithi = "";//document.getElementById("warehouse").value; 
	var selectedVillage = document.getElementById("village").value;
	//alert(harvestSeasonEnable);
	if(harvestSeasonEnable=="1"){
    var selectedSeason= document.getElementById("season").value;
	}else{
		var selectedSeason='<s:property value="%{seasonName}" />';
	}
	
	var selectedDate= document.getElementById("startDate").value;
	var paymentRupee = jQuery.trim(jQuery("#paymentRupee").val());
	var paymentPaise = 0;//jQuery.trim(jQuery("#paymentPaise").val());
	if(tenant!="livelihood"){
	var	farmerType=jQuery('#isRegisteredFarmer').is(':checked');
	}else{
		var	farmerType=true;
	}
	
	
	var farmerName = jQuery.trim(jQuery("#farmerNameInput").val());
	var mobileNo =jQuery.trim(jQuery("#mobileNoInput").val());
    var selectedAgent=document.getElementById('fieldstaff').value;
    var stockTypeCooperative=jQuery('#isCooperativeStock').is(':checked');
    var stockTypeFieldStaff=jQuery('#isFieldStaffStock').is(':checked');
    var unRegisterFarmerName = document.getElementById("farmerNameInput").value;
    var paymentMode = $('input[name=amtType]:checked').val();
	var taxValue = document.getElementById("amntTax").value;
	var finalAmtValue = document.getElementById("finalTaxAmount").innerHTML;
	var selectedFarmerValue = $("#farmer option:selected").val();
	var isFree = jQuery('#checkFreeDis-1').is(":checked");
	var distCreditBalValue = document.getElementById("distributionCreditBal").innerHTML;
	//var coPrice=document.getElementById("costPrice").innerHTML;
	paymentRupee=paymentRupee.replace(/\,/g,"");
	var payAmountArr=paymentRupee.split('.');
	console.log(payAmountArr+payAmountArr);
	paymentRupee=payAmountArr[0];
	paymentPaise=payAmountArr[1];
	var totalAmountWithCredit  =0;
	if(distCreditBalValue.charAt(0)=='-'){
		distCreditBalValue = distCreditBalValue.slice(1,distCreditBalValue.length-1);
	totalAmountWithCredit = parseFloat(finalAmtValue)+parseFloat(distCreditBalValue);
	}else{
		totalAmountWithCredit = parseFloat(finalAmtValue);
	}
	/* if(parseFloat(paymentRupee) > parseFloat(totalAmountWithCredit)){
		document.getElementById("validateError").innerHTML='<s:text name="invaidAMt"/>';
		hit=false;
		enableButton();
		return false;
	}
	 */
	var isFreeDistribution='';
    if(isFree){
        isFreeDistribution = "1";
    }else{
        isFreeDistribution = "0";
    }
    if(selectedSeason=="" &&  selectedSeason==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptySeason')}" />';
		enableButton();
		hit=false;
		return false;
	}else if(stockTypeCooperative && selectedWarehouse==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.warehouse')}" />';
		hit=false;
		enableButton();
		return false;
	}else if(stockTypeFieldStaff && selectedAgent==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptyAgent')}" />';
		enableButton();
		hit=false;
		return false;
	}
	
	else if(selectedVillage=="" || selectedVillage==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptyVillage')}" />';
		enableButton();
		hit=false;
		return false;
	}
	else if(farmerType && selectedFarmer==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptyFarmer')}" />';
		enableButton();
		hit=false;
		return false;
	}
	else if(!farmerType && farmerName==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.farmerName')}" />';
		enableButton();
		hit=false;
	}
	else if(!farmerType && isValidAlphaNumericWithSpaceValue(farmerName)==false){
		document.getElementById("validateError").innerHTML='<s:text name="invalid.farmerName"/>';
		enableButton();
		hit=false;
		return false;
	}
	else if(!farmerType && mobileNo!="" && !isValidValue(mobileNo)){
		document.getElementById("validateError").innerHTML='<s:text name="invalid.mobileNo"/>';
		enableButton();
		hit=false;
		return false;
	}
	else if(selectedDate.trim()==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectDate"/>';
		enableButton();
		hit=false;
		return false;
	}
	else if(productsInfoArray.length==0){
		document.getElementById("validateError").innerHTML='<s:text name="noRecordFoundDist"/>';
		enableButton();
		hit=false;
		return false;
	}
if(distImageEnable=="1"){     
    hit=validateImage();
    if(!hit){
    	return false;
    }
	}
/* 	if(!isFree){
	if(paymentRupee!="" && !isValidValue(paymentRupee)){
		console.log("paymentRupee"+paymentRupee);
		document.getElementById("validateError").innerHTML = '<s:text name="invalidPayment"/>';
		hit=false;
		enableButton();
		jQuery("#paymentRupee").focus();
		enableButton();
		return false;
	}
	else if(paymentMode == "" || paymentMode == null) {
		document.getElementById("validateError").innerHTML='<s:text name="empty.cashType"/>';
		hit=false;
		enableButton();
		return false;
	}
	else if(paymentMode == 0 && (paymentRupee == "" || paymentRupee==null)) {
		document.getElementById("validateError").innerHTML='<s:text name="empty.amount"/>';
		hit=false;
		enableButton();
		return false;
	}
    
	} */
	if(stockTypeCooperative){
		stockType=stockTypeCooperative;
	}

	
	if(hit){
	buildReqProductObject();
	var productTotalString=' ';
	if(isFree){
     paymentMode='';
	}
	for(var i=0;i<productTotalArray.length;i++){
		
		//alert("len"+productTotalArray.length);

		//productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"||";
	  if(isFree){
		  if($("#enableBatchNo").val()=='1'){
		productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+0+"##"+0+"##"+productTotalArray[i].coPrice+"##"+productTotalArray[i].batchNo+"||";
		  }else{
			  productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+0+"##"+0+"##"+productTotalArray[i].coPrice+"##"+'NA'+"||";
		  }
	  }else{
		  if($("#enableBatchNo").val()=='1'){
			//productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+productTotalArray[i].sellingPrice+"##"+productTotalArray[i].totalStockPrice+"##"+productTotalArray[i].coPrice+"##"+productTotalArray[i].batchNo+"||";
			  productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+0+"##"+productTotalArray[i].totalStockPrice+"##"+productTotalArray[i].coPrice+"##"+productTotalArray[i].batchNo+"||";
		  }else{
			 //productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+productTotalArray[i].sellingPrice+"##"+productTotalArray[i].totalStockPrice+"##"+productTotalArray[i].coPrice+"##"+'NA'+"||";
			  productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+0+"##"+productTotalArray[i].totalStockPrice+"##"+productTotalArray[i].coPrice+"##"+'NA'+"||";
		  }	  
	}
	}
	var farmerIdOrName;
	if(farmerType){
		farmerIdOrName=selectedFarmer.split("-")[1]
	}else{
		farmerIdOrName=farmerName;
	}
	
	$.post("distribution_populateDistribution.action",{selectedSeason:selectedSeason,selectedCooperative:selectedWarehouse,selectedWarehouse:selectedSamithi,selectedVillage:selectedVillage,selectedDate:selectedDate,productStock:stockType,farmerId:farmerIdOrName,
		registeredFarmer:farmerType,productTotalString:productTotalString,selectedAgent:selectedAgent
		,paymentRupee:paymentRupee,paymentPaise:paymentPaise,unRegisterFarmerName:unRegisterFarmerName,taxValue:taxValue,finalAmtValue:finalAmtValue,paymentMode:paymentMode,selectedFarmerValue:selectedFarmerValue,freeDistribution:isFreeDistribution,mobileNo:mobileNo},function(data,result){
			//alert(result);
			//alert();
			//$("#sucessbtn").prop('disabled', true);
			if(result=='success')
			{
				
				if(distImageEnable=="1"){
		 			$("#distId").val(data.id);
					distImageUpload();	
				}
				document.getElementById("divMsg").innerHTML=data.des;
				document.getElementById("enableModal").click();		 
		//disablePopupAlert();
		//enablePopupAlert();
				}
 	});
	
	}else{
		$("#sucessbtn").prop('disabled', true);
	}
	}catch(e){
		$("#sucessbtn").prop('disabled', true);
		console.log(e);
	}
}
function distImageUpload(){
	$("#imgForm").submit();
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
	/* $('#pendingRestartAlertErrMsg').html('');
	$('#popupBackground').hide();
	$('#restartAlert').hide();
	$('body').css('overflow',''); */
	document.redirectform.action="distribution_list.action";
	document.redirectform.submit();
}
function distributionEnable(){
	document.redirectform.action="distribution_create.action";
	document.redirectform.submit();
}

function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
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

function isValidValue(val){
	 var numberReg = /^[0-9]+$/;
	 var result = true;
	 if(val=='' || val==null || val== undefined ){
		 return false;
	}
	 var regExVal = val.match(numberReg);
	 if(regExVal=='' || regExVal==null || regExVal== undefined ){
	  result = false;
	 }
	 return result;
}

/*function calculateTotalPrice(distributionStock,pricePerUnit,isPartial,units,subUnit){
	if(!isPartial)
	return (parseFloat(distributionStock) * parseFloat(pricePerUnit)).toFixed(2);
	else{
		var toatalQty;
		if(units[1].toUpperCase()=="KG" && subUnit.toUpperCase()=="GM"){
			toatalQty=units[0]*1000;
		}else if(units[1].toUpperCase()=="LT" && subUnit.toUpperCase()=="ML"){
			toatalQty=units[0]*1000;
		}else{
			toatalQty=units[0];
		}
			return (parseFloat(pricePerUnit)/parseFloat(toatalQty)*parseFloat(distributionStock)).toFixed(2);
	}
}*/
/* 
function calculateTotalPrice(distributionStock,sellingPrice,isPartial,units,subUnit){

	if(!isPartial)
	{
		var finalPrice=parseFloat(distributionStock) * parseFloat(sellingPrice);
		console.log("parseFloat(distributionStock)"+parseFloat(distributionStock));
		return finalPrice.toFixed(2);	
	}
	else{
		var toatalQty;
		if(units[1].toUpperCase()=="KG" && subUnit.toUpperCase()=="GM"){
			toatalQty=units[0]*1000;
		}else if(units[1].toUpperCase()=="LT" && subUnit.toUpperCase()=="ML"){
			toatalQty=units[0]*1000;
		}else{
			toatalQty=units[0];
		}
			return (parseFloat(sellingPrice)/parseFloat(toatalQty)*parseFloat(distributionStock)).toFixed(2);
	}
	
} */

function calculateTotalPrice(distributionStock,costPrice,isPartial,units,subUnit){

	if(!isPartial)
	{
		var finalPrice=parseFloat(distributionStock) * parseFloat(costPrice);
		console.log("parseFloat(distributionStock)"+parseFloat(distributionStock));
		return finalPrice.toFixed(2);	
	}
	else{
		var toatalQty;
		if(units[1].toUpperCase()=="KG" && subUnit.toUpperCase()=="GM"){
			toatalQty=units[0]*1000;
		}else if(units[1].toUpperCase()=="LT" && subUnit.toUpperCase()=="ML"){
			toatalQty=units[0]*1000;
		}else{
			toatalQty=units[0];
		}
			return (parseFloat(costPrice)/parseFloat(toatalQty)*parseFloat(distributionStock)).toFixed(2);
	}
	
}

function resetAllData()
{
	var idsArray=['cooperative','cities','village','farmer'];
	resetSelect2(idsArray);
	resetProductData();
}

function resetProductData(){
	
	listProduct(this);
	resetEditFlag();
	reloadTable();	
	resetPrefixAndSuffix();
	
	jQuery('#category').prop("disabled",false);
	jQuery('#product1').prop("disabled",false);
	 $('#product1 option').remove();
	 $('#product1').append('<option value=" ">Select</option>');
	jQuery('#batchNo').prop("disabled",false);
	if($("#enableBatchNo").val()=='1'){
		jQuery('#batchNo').prop("batchNo",false);
		
		}
	var idsArray=['category','product1','batchNo'];
	resetSelect2(idsArray);
	

	//$("#sellingPrice").val("");
	document.getElementById('stock').innerHTML = "";
	document.form.subUnit.length = 0;
	document.getElementById('subUnit').disabled = true;
	document.getElementById('distributionStock').innerHTML = "";
	document.getElementById('distributionStock').value="";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("validatePriceError").innerHTML="";
	document.getElementById('costPrice').innerHTML = "";
	
	jQuery("#totalPriceLabel").html('');
	jQuery('#category').prop("disabled",false);
	jQuery('#product1').prop("disabled",false);
	jQuery('#batchNo').prop("batchNo",false);
}


function resetUnitData(){
	
	
	
	document.getElementById('distributionStock').value = 0;
	document.getElementById('stockPaise').value = 0000;
	document.getElementById('stock').innerHTML = "";
	//document.getElementById('productPrice').innerHTML = "";
	//document.getElementById('distributionStock').value = "";
	//document.getElementById('distributionStock').disabled=true;
	document.getElementById("validateError").innerHTML="";
	document.getElementById("validatePriceError").innerHTML="";	
	jQuery("#totalPriceLabel").html('');
	jQuery("#costPrice").html('');
	document.form.subUnit.length = 0;
	document.getElementById('subUnit').disabled = true;
	//$('#selllingRupee').val('');
	
	
}

function calulateAvailableStock(){
	var selectedProduct = document.getElementById("product1").value;
	var selectedCooperative = document.getElementById("cooperative").value;
	//var selectedWarehouse = document.getElementById("warehouse").value;
	var selectedUnit=document.getElementById("unit1").value;
	var availableStock = document.getElementById("stock").value;
	var stock1 = document.getElementById("distributionStock").value;
	var stock2 = document.getElementById("stockPaise").value;
	var coPrice=document.getElementById("costPrice").innerHTML;
	
	var distributionStock = stock1+"."+stock2;
	var selectedAgent=document.getElementById("fieldstaff").value;
	var selectedProduct = document.getElementById("product1").value;
	//var selectedWarehouse = document.getElementById("warehouse").value;
	var selectedUnit=document.getElementById("unit1").value;
	var availableStock = document.getElementById("stock").value;
	var selectedCategory=document.getElementById("category").value;
	//var distributionStock = document.getElementById("distributionStock").value;
	//var sellRupee=$('#selllingRupee').val();
	//var sellingPrice =sellRupee.replace(',','');
	 var selectedSeason= document.getElementById("season").value;
	 
	var hit=true;
	 var selectedSeason= document.getElementById("season").value;
	 var batchNo="NA";
	 if($("#enableBatchNo").val()=='1'){
		 batchNo =document.getElementById("batchNo").value;
	  if(batchNo==""){
			resetUnitData();
			hit=false;	
		}
	 }
	if(selectedProduct==0){
		resetProductData();
		hit=false;
	}
	if(selectedUnit==""){
		resetUnitData();
		hit=false;
	}
	if(selectedSeason==""){
		resetUnitData();
		hit=false;
	}
	if(hit) {
	var units=selectedUnit.split('-');
	document.getElementById("validateError").innerHTML="";
	document.getElementById("distributionStock").disabled=false;
	document.getElementById("stockPaise").disabled=false;
	
	$("#stock").text('');
	if(tenant=="lalteer"){
	$.post("distribution_loadAvailableStockLalteer",{selectedProduct:selectedProduct,selectedCooperative:selectedCooperative,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedCategory:selectedCategory,batchNo:batchNo},function(data){
		var n=data.split(",");
		for(var i=0; i< productsInfoArray.length; i++){
		
			if( !productsInfoArray[i].isEdit && productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo){
				n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
			}
		}
		document.getElementById('distributionStock').disabled = false;
		document.getElementById("stockPaise").disabled=false;
		document.getElementById('subUnit').disabled = false;
		if(n[0] == "N/A" || n[1] == "N/A"){
			document.getElementById('distributionStock').disabled = true;
			document.getElementById("stockPaise").disabled=true;
			document.getElementById('subUnit').disabled = true;
		}
        if(n[0]<=0){
        	document.getElementById('distributionStock').disabled = true;
        	document.getElementById("stockPaise").disabled=true;
			document.getElementById('subUnit').disabled = true;
		}
		
		var totalQty;
		var selectedSubUnit=document.getElementById('subUnit').value;
		if(selectedUnit==selectedSubUnit){
			totalQty=(parseFloat(n[0])).toFixed(4);
		}else{
			if(selectedSubUnit==units[1]){
				totalQty=(parseFloat(n[0])*parseFloat(units[0])).toFixed(3)+"-"+units[1];
			}else{
				if(units[1].toUpperCase()=="KG" && selectedSubUnit.toUpperCase()=="GM"){
					totalQty=(parseFloat(n[0])*parseFloat(units[0]*1000)).toFixed(3)+"-"+selectedSubUnit;
				}else if(units[1].toUpperCase()=="LT" && selectedSubUnit.toUpperCase()=="ML"){
					totalQty=(parseFloat(n[0])*parseFloat(units[0]*1000)).toFixed(3)+"-"+selectedSubUnit;
				}else{
					totalQty=(parseFloat(n[0])*parseFloat(units[0])).toFixed(3)+"-"+units[1];
				}	
			}
		}
	   totalQty = parseFloat(totalQty).toFixed(3);	
	   jQuery("#stock").text(totalQty);
	   //jQuery('#productPrice').text(parseFloat(n[1]).toFixed(2));
	   //var sellingPrice=document.getElementById('sellingPrice').value;
	   //jQuery('#sPrice').text(parseFloat(n[1]).toFixed(2));
	   var stockValidationFlag =validateDetails(false);
	   var qtyStock = jQuery.trim(jQuery("#distributionStock").val());
	   var qtyStockVal = jQuery.trim(jQuery("#stockPaise").val());
	   var distributionStock = qtyStock+"."+qtyStockVal;
	   //alert("distributionStock :"+distributionStock);
	   //jQuery("#totalPriceLabel").html('');
		if(stockValidationFlag && distributionStock!=""){
		//var productPrice = jQuery.trim(jQuery("#productPrice").text());
		//var sPrice=jQuery.trim(jQuery("#sPrice").text());
	    var subUnit=jQuery("#subUnit").val();
	    var isPartial=!(jQuery("#subUnit").val()===jQuery.trim(jQuery("#unit1").val()));
	    totalPrice = calculateTotalPrice(distributionStock,coPrice,isPartial,units,subUnit);
		//totalPrice = calculateTotalPrice(distributionStock,sellingPrice,isPartial,units,subUnit);
		if(parseFloat(totalPrice)==0){
			document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
			jQuery("#totalPriceLabel").html('');
		}else{
			if(!isNaN(totalPrice)){
	        jQuery("#totalPriceLabel").html(totalPrice);
		    document.getElementById("validateError").innerHTML="";		
			}
		}
		}
	});
	}else{
		$.post("distribution_loadAvailableStock",{selectedProduct:selectedProduct,selectedCooperative:selectedCooperative,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedCategory:selectedCategory,selectedSeason:selectedSeason,batchNo:batchNo},function(data){
			var n=data.split(",");
			for(var i=0; i< productsInfoArray.length; i++){
			
				if( !productsInfoArray[i].isEdit && productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo){
					n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
				}
			}
			document.getElementById('distributionStock').disabled = false;
			document.getElementById("stockPaise").disabled=false;
			document.getElementById('subUnit').disabled = false;
			if(n[0] == "N/A" || n[1] == "N/A"){
				document.getElementById('distributionStock').disabled = true;
				document.getElementById("stockPaise").disabled=true;
				document.getElementById('subUnit').disabled = true;
			}
	        if(n[0]<=0){
	        	document.getElementById('distributionStock').disabled = true;
	        	document.getElementById("stockPaise").disabled=true;
				document.getElementById('subUnit').disabled = true;
			}
			
			var totalQty;
			var selectedSubUnit=document.getElementById('subUnit').value;
			if(selectedUnit==selectedSubUnit){
				totalQty=(parseFloat(n[0])).toFixed(4);
			}else{
				if(selectedSubUnit==units[1]){
					totalQty=(parseFloat(n[0])*parseFloat(units[0])).toFixed(3)+"-"+units[1];
				}else{
					if(units[1].toUpperCase()=="KG" && selectedSubUnit.toUpperCase()=="GM"){
						totalQty=(parseFloat(n[0])*parseFloat(units[0]*1000)).toFixed(3)+"-"+selectedSubUnit;
					}else if(units[1].toUpperCase()=="LT" && selectedSubUnit.toUpperCase()=="ML"){
						totalQty=(parseFloat(n[0])*parseFloat(units[0]*1000)).toFixed(3)+"-"+selectedSubUnit;
					}else{
						totalQty=(parseFloat(n[0])*parseFloat(units[0])).toFixed(3)+"-"+units[1];
					}	
				}
			}
		   totalQty = parseFloat(totalQty).toFixed(3);	
		   jQuery("#stock").text(totalQty);
		   //jQuery('#productPrice').text(parseFloat(n[1]).toFixed(2));
		   //var sellingPrice=document.getElementById('sellingPrice').value;
		   //jQuery('#sPrice').text(parseFloat(n[1]).toFixed(2));
		   var stockValidationFlag =validateDetails(false);
		   var qtyStock = jQuery.trim(jQuery("#distributionStock").val());
		   var qtyStockVal = jQuery.trim(jQuery("#stockPaise").val());
		   var distributionStock = qtyStock+"."+qtyStockVal;
		   //alert("distributionStock :"+distributionStock);
		   //jQuery("#totalPriceLabel").html('');
			if(stockValidationFlag && distributionStock!=""){
			//var productPrice = jQuery.trim(jQuery("#productPrice").text());
			//var sPrice=jQuery.trim(jQuery("#sPrice").text());
		    var subUnit=jQuery("#subUnit").val();
		    var isPartial=!(jQuery("#subUnit").val()===jQuery.trim(jQuery("#unit1").val()));
		    totalPrice = calculateTotalPrice(distributionStock,coPrice,isPartial,units,subUnit);
			//totalPrice = calculateTotalPrice(distributionStock,sellingPrice,isPartial,units,subUnit);
			if(parseFloat(totalPrice)==0){
				document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
				jQuery("#totalPriceLabel").html('');
			}else{
				if(!isNaN(totalPrice)){
		        jQuery("#totalPriceLabel").html(totalPrice);
			    document.getElementById("validateError").innerHTML="";		
				}
			}
			}
		});
	}
	}
}

jQuery('input[type=radio][name=productStock]').change(function() {
	var selectedValues = $(this).val();
	document.getElementById("validateError").innerHTML="";
	document.getElementById("cooperative").selectedIndex="0";
	/* document.getElementById("warehouse").selectedIndex="0"; */
	document.getElementById("village").selectedIndex="0";
	document.getElementById("farmer").selectedIndex="0";
	productsInfoArray = new Array();
	pArrayList = new Array();
	productTotalArray=new Array();
	productNameArray=new Array();
	reloadTable();
	if(selectedValues=='false'){
		jQuery(".fieldstafftd").show();	
		jQuery(".warehousetd").hide();
		//jQuery("#cooperative").val("");	
		 if(tenant!='lalteer'){
		   document.getElementById("category").setAttribute("onchange","populateProductFieldStaff(this)");
		 }else{
			 document.getElementById("category").setAttribute("onchange","populateLalteerProductFieldStaff(this)");
		 }
	}else{
		jQuery(".warehousetd").show();
		jQuery(".fieldstafftd").hide();
		jQuery("#fieldstaff").val("");
		//jQuery("#cooperative").val("");
		document.getElementById("category").setAttribute("onchange","listProduct(this)");
	}       	
});



function editRow(indx){
	jQuery('#category').prop("disabled",true);
	jQuery('#product1').prop("disabled",true);
	 if($("#enableBatchNo").val()=='1'){
	jQuery('#batchNo').prop("disabled",true);
	 }
	var prodInfo='';
	
	//document.getElementById('distributionStock').value  = productsInfoArray[indx].stock.split(".")[0];
	//document.getElementById('stockPaise').value  = productsInfoArray[indx].stock.split(".")[1];
	 $("#category option[value='"+productsInfoArray[indx].categoryName+"']").prop("selected","selected");
	 $("#category").trigger("change");

	//listProduct(document.getElementById("category"));
	var prod=productsInfoArray[indx].productId;
	
	var category=$("#category").val();
	var selectedCooperative = document.getElementById("cooperative").value;
	if(!selectedCooperative=="")
	{
		 $("#product1 option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
		// $("#product1").trigger("change");
	}
	var urlEdit;
	if(tenant=="lalteer"){
		urlEdit="distribution_populateLalteerProduct";
	}else{
		urlEdit="distribution_populateProduct";
	}
	
	category=(category==0)?"":category;
	selectedCooperative=(selectedCooperative==0)?"":selectedCooperative;
	
	if(selectedCooperative !="" && category!=""){
		
	$.ajax({
		 type: "POST",
         async: false,
        url:urlEdit,
         data: {selectedCategory:category,selectedCooperative:selectedCooperative},
         success: function(result) {
        	 insertOptions("product1",JSON.parse(result));
        	 
        	 $("#product1 option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
        	 
        	 $("#product1").trigger("change");
        	
        	 if($("#enableBatchNo").val()=='1'){
     			 $("#batchNo option[value='"+productsInfoArray[indx].batchNo+"']").prop("selected","selected");
     			 $("#batchNo").trigger("change");
     			}
        	
        	// $('#product1').val(prod);
        	 prodInfo=jQuery('#product1').val();
        		var start=prodInfo;
        		var resultString=prodInfo.substring(start+1);
        		var arry=resultString.split('~'); 
        		
        		document.getElementById('subUnit').disabled = false;
        		document.getElementById('subUnit').length=0;
        		addOption(document.getElementById("subUnit"),productsInfoArray[indx].units,productsInfoArray[indx].units);
        		var units=productsInfoArray[indx].units.split('-');
        		if(parseFloat(units[0])!=1){
        		addOption(document.getElementById("subUnit"), units[1], units[1]);
        		}
        	    if(units[1].toUpperCase()==="KG"){	
        	    	addOption(document.getElementById("subUnit"), "gm", "gm");
        	    }else if(units[1].toUpperCase()==="LT"){
        	    	addOption(document.getElementById("subUnit"), "ml", "ml");
        	    }	
        	    jQuery('#subUnit').val(productsInfoArray[indx].subUnit);
        	    
        	    
        		//document.getElementById('distributionStock').disabled=false;
        		//document.getElementById('distributionStock').value =productsInfoArray[indx].enteredStock;
        		document.getElementById('distributionStock').value =productsInfoArray[indx].enteredStock.split(".")[0];
        		document.getElementById('stockPaise').value  = productsInfoArray[indx].enteredStock.split(".")[1];
        		//var sellingPrice = productsInfoArray[indx].sellingPrice.split('.');
        	//	$('#selllingRupee').val(sellingPrice[0]);
        //	$('#sellingPaise').val(sellingPrice[1]);
        	//	alert(productsInfoArray[indx].totalStockPrice);
        		$('#totalPriceLabel').text(productsInfoArray[indx].totalStockPrice);
        		//document.getElementById('totalPriceLabel').innerHTML = productsInfoArray[indx].totalStockPrice;//
        		//jQuery('#totalPriceLabel').val(productsInfoArray[indx].totalStockPrice);
        		document.getElementById('costPrice').innerHTML = productsInfoArray[indx].coPrice;
        		//jQuery('#costPrice').val(productsInfoArray[indx].coPrice);
        	
        		
        		
         }
	});
	
	//alert(prod);
	
	}else
		{
		$("#product1 option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
		$("#product1").trigger("change");
		document.getElementById('distributionStock').value =productsInfoArray[indx].enteredStock.split(".")[0];
		document.getElementById('stockPaise').value  = productsInfoArray[indx].enteredStock.split(".")[1];
	//	alert(productsInfoArray[indx].totalStockPrice);
		jQuery('#totalPriceLabel').text(productsInfoArray[indx].totalStockPrice);
		 if($("#enableBatchNo").val()=='1'){
 			 $("#batchNo option[value='"+productsInfoArray[indx].batchNo+"']").prop("selected","selected");
 			 $("#batchNo").trigger("change");
 			}
		}
	resetEditFlag();
	productsInfoArray[indx].isEdit=true;
	calulateAvailableStock();
	reloadTable();
	//listProductWhileEdit(category);
}

jQuery('input[type=radio][name=registeredFarmer]').change(function() {
	var selectedValue = $(this).val();
	
	document.getElementById("validateError").innerHTML="";
	if(selectedValue=='false'){
		jQuery(".samithitd").hide();
		jQuery(".farmercombotd").hide();
		jQuery(".unregFarmerData").show();	
		jQuery("#farmerNameInput").val("");	
		jQuery(".mobileNotd").show();
		jQuery("#mobileNoInput").val("");	
		jQuery("#warehouse").val("");
		jQuery("#village").empty();
		listVillageForUnregisteredFarmer();	
	
	}else{
		jQuery("#warehouse").val("");
		jQuery(".samithitd").show();
		jQuery(".farmercombotd").show();
		jQuery(".unregFarmerData").hide();
		jQuery(".mobileNotd").hide();
		jQuery("#village").empty();
		listVillage(document.getElementById("samithi"));			
	}       	
});
function resetEditFlag(){
	for(var i=0; i< productsInfoArray.length; i++)
		productsInfoArray[i].isEdit=false;	
}

function getEditIndex(){
	for(var i=0; i< productsInfoArray.length; i++){
		if(productsInfoArray[i].isEdit)
			return i;
	}
	return -1;
}

function editproductTotalArray(productArray){
	for(var i=0;i<productTotalArray.length;i++){
		if(productTotalArray[i].name==productArray.name+'='+productArray.units){
		productTotalArray[i].qty-=productArray.QtyPerUnit;
		break;
		}
	}
}

function isDecimal(evt,obj) {
	
	    var value=obj.value;
	    var charCode = (evt.which) ? evt.which : evt.keyCode
 		 if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode!=46)
 		        return false;
 		    if(value.indexOf('.')!=-1 && charCode==46)
 		         return false;
 		    return true;
}

function isNumber(evt,obj) {
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;

}

function isAlphaNumeric(evt){
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && !((charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))) {
	        return false;
	    }
	    return true;
}
/*
jQuery('input[type=radio][name=productStock]').change(function() {
	var selectedValue = $(this).val();
	document.getElementById("validateError").innerHTML="";
	if(selectedValue=='false'){
		jQuery("#fieldstafftd").show();		
		jQuery("#warehousetd").hide();
		
	}else{
		jQuery("#warehousetd").show();
		jQuery("#fieldstafftd").hide();	
	}       	
});
*/
		
--></script>
<script type="text/javascript">
/* document.getElementById("warehouse").selectedIndex=0; */
document.getElementById("village").selectedIndex=0;
/* document.getElementById("warehouse").disabled=false; */
document.getElementById("village").disabled=false;
document.getElementById("farmer").disabled=false;
document.getElementById("farmer").selectedIndex=0;
//document.getElementById("season").value='<s:property value="selectedSeason" />';
var farmerValue ='<%=request.getParameter("fId")%>';
if(farmerValue != null && farmerValue != ""){
document.getElementById("village").value='<s:property value="selectedVillage" />';
document.getElementById("farmer").value='<s:property value="selectedFarmer" />';
document.getElementById("village").disabled=true;
document.getElementById("farmer").disabled=true;
}
if(farmerValue === 'null'){
	document.getElementById("village").disabled=false;
	document.getElementById("farmer").disabled=false;
}

jQuery("#referenceNo").val('');
jQuery("#serialNo").val('');
resetProductData();
//document.form.product1.length = 0;
//addOption(document.getElementById("product1"), "Select", "");
jQuery("#isCooperativeStock").attr('checked',true);
//jQuery("#isFieldStaffStock").attr('checked',true);
jQuery("#warehouse").focus();
jQuery("#farmerNameInput").val("");	
jQuery("#isRegisteredFarmer").attr('checked', true);
jQuery("#mobileNoInput").val("");	
jQuery("#productStock").val("");
jQuery("#cooperative").val("");
jQuery("#fieldstaff").val("");
if(jQuery("#season")!=null){
jQuery("#season").val("");
}
jQuery("#farmer").val("");
jQuery("#cities").val("");
jQuery("#isCooperativeStock").focus();
jQuery("#checkFreeDis-1").attr('checked', false);
//jQuery("#startDate").val("");

function redirectForm()
{
	document.redirectform.action="distribution_list.action";
	document.redirectform.submit();
}

function loadFarmerAccountBalance()
{
	var selectedFarmerValue = $("#farmer option:selected").val();
	var distCashBalValue;
	var distCreditBalValue;
	$.post("distribution_populateFarmerAccBalance",{selectedFarmerValue:selectedFarmerValue},function(data){
		//alert("selectedFarmerValue"+selectedFarmerValue);
		if(data!=null && data!=""){
			var dataArr = data.split(",");
			distCashBalValue=parseFloat(dataArr[0]).toFixed(2);
			distCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
			document.getElementById("distributionCashBal").innerHTML=distCashBalValue;
			document.getElementById("distributionCreditBal").innerHTML=distCreditBalValue;
			}
	});
}

function enableButton(){
	jQuery(".save-btn").prop('disabled',false);
}
function reset()
{   
	if($("#enableBatchNo").val()=='1'){
		jQuery('#batchNo').prop("batchNo",false);
		document.getElementById('batchNo').selectedIndex = ""; 
		jQuery("#batchNo").val('');
		jQuery("#batchNo").select2();
		}
	document.getElementById('product1').selectedIndex = "";
	document.getElementById('stock').innerHTML = "";
	document.getElementById('distributionStock').value = "";
	//document.getElementById('sellingPrice').value = "";
	document.getElementById('distributionStock').value = "";
	document.getElementById('stockPaise').value = "";
	
}

function resetPrefixAndSuffix(){
	 jQuery("#distributionStock").val("0");
	 jQuery("#stockPaise").val("000");
	 $('#paymentRupee').val("");
}

function listVillageByCity(obj){
	var selectedCity = $('#city').val();
	jQuery.post("distribution_populateVillageByCity.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
		insertOptions("village",$.parseJSON(result));
		//listSamithi(document.getElementById("village"));
	});
}
 function checkImgHeightAndWidth(val) {

	var _URL = window.URL || window.webkitURL;
	var img;
	var file = document.getElementById(val.id).files[0];
	if (file) {

		img = new Image();
		img.onload = function() {
			imgHeight = this.height;
			imgWidth = this.width;
		};
		img.src = _URL.createObjectURL(file);
	}		
} 
function validateImage(){
	
	var file1=document.getElementById('distImg1').files[0];
	var filename1=document.getElementById('distImg1').value;
	var fileExt1=filename1.split('.').pop();
	var file2=document.getElementById('distImg2').files[0];
	var filename2=document.getElementById('distImg2').value;
	var fileExt2=filename2.split('.').pop();
	hit=true;
	
		
	
	
	if(file1 == undefined && file2 == undefined){
		document.getElementById("validateError").innerHTML='<s:text name="empty.oneImage"/>';
		hit=false;
	}else{
		if(fileExt1=='jpg' || fileExt1=='jpeg' || fileExt1=='png'||fileExt1=='JPG'||fileExt1=='JPEG'||fileExt1=='PNG'||fileExt2=='jpg' || fileExt2=='jpeg' || fileExt2=='png'||fileExt2=='JPG'||fileExt2=='JPEG'||fileExt2=='PNG')
		{ 	
			hit=true;
		}else{
			document.getElementById("validateError").innerHTML='<s:text name="Invalid File Extension"/>';
			//alert('<s:text name="invalidFileExtension"/>')	
			hit=false;
			}
	
	}
return hit;
}
function populateUnit(){
//var label=document.getElementById("distributionStockLable").value;
var distributionStockLable="<s:text name='distributionStock'/>";
	var selectedCategory = document.getElementById("category").value;
	if(!selectedCategory=="" || !selectedCategory=="0"){
	
	$.post("distribution_populateUnit",{selectedCategory:selectedCategory},function(data){
			if(data!=null && data!=""){				
				document.getElementById("distributionStockUnitLable").innerHTML= distributionStockLable+"("+data+")";	
			}
			
	});	   
}  
}


</script>
<s:form action="distribution_populatePrintHTML" id="receiptForm"
	method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
	<s:hidden value="314" name="distTxnType"></s:hidden>

</s:form>

<%--<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>
<s:form name="redirectform" action="distribution_list.action"
	method="POST">
</s:form>