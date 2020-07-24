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

.baseStockTable {
	margin: 0 auto 15px;
	width: 99.8%; /*border:solid 1px #666;*/
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


<font color="red"> <s:actionerror /></font>
<div id="orderNoValidat" style="color: red;"></div>

<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" name="warehouseProduct.id" />
	<s:hidden key="command" />
	<s:hidden id="selectedWarehouse" name="selectedWarehouse" />
	<s:hidden key="selecteddropdwon" id="listname" />
	<s:hidden key="temp" id="temp" />
	<s:hidden id="enableBatchNo" name="enableBatchNo" />
	<s:hidden key="productTotalString" id="productTotalString" />



	<!--start decorator body-->

	<div class="appContentWrapper marginBottom">
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
							<div class="prodError" style="text-align: center;color: red"></div>
							</p>
						</div>
					</div>
				</div>


			</div>


			<font color="red"> <s:actionerror /></font>
			<h2>
				<s:property value="%{getLocaleProperty('info.warehouseStockInfo')}"/>
			</h2>
			<div class="flexiWrapper filterControls">

				<div class="flexi flexi10">
					<label for="txt"><s:text name="season.name" /><span
						class="manadatory">*</span></label>
					<div class="form-element">
						<s:select id="season" name="selectedSeason"
							onchange="resetData();" cssClass="col-sm-3 form-control select2"
							headerKey="" headerValue="%{getText('txt.select')}"
							list="harvestSeasonList" theme="simple" enabled="true" />

					</div>
				</div>
				<div class="flexi flexi10">
					<label for="txt">
						<s:property value="%{getLocaleProperty('warehouse')}"/>
						<span class="manadatory">*</span></label>
					<div class="form-element">
						<s:select id="warehouse" name="selectedWarehouseCode"
							cssClass="col-sm-3 form-control select2" headerKey=""
							headerValue="%{getText('txt.select')}" list="listWarehouse"
							theme="simple" enabled="true" />
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:property value="%{getLocaleProperty('warehouseProduct.orderNo')}" />
						<span class="manadatory manadatoryField">*</span></label>
					<div class="form-element">
						<s:textfield id="orderNo" name="orderNo" maxlength="6"
							cssClass="col-sm-3 form-control " />
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:property value="%{getLocaleProperty('warehouseProduct.vendorId')}" />
						<span class="manadatory">*</span></label>
					<div class="form-element">
						<s:select id="vendor" name="vendorId" headerKey=""
							headerValue="%{getText('txt.select')}" list="listVendor"
							theme="simple" cssClass="col-sm-3 form-control select2"
							enabled="true" onchange="loadVendorAccBal();" />
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:text name="warehouseProduct.date" /><span
						class="manadatory">*</span> </label>
					<div class="form-element">
						<div class="form-element">
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple" data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years"
								cssClass="date-picker col-sm-3 form-control" size="20" />

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="service-content-wrapper">
		<div class="service-content-section">

			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					
					<div class="flexiWrapper filterControls">
						<div class="flexi flexi12" >
							<label for="txt"><s:text name="warehouseProduct.category" />
								<span class="manadatory">*</span></label>
							<div class="form-element">

								<s:select name="selectedSubCategoryList"
									cssClass="col-sm-4 form-control select2"
									list="categoryCategoryList" headerKey=""
									headerValue="%{getText('txt.select')}" id="categoryId"
									onchange="listProduct(this,0); resetUnitAndAvastock();" />
							</div>
						</div>
						<div class="flexi flexi12">
							<label for="txt"><s:text
									name="%{getLocaleProperty('warehouseProduct.product')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedProduct"
									cssClass="col-sm-4 form-control select2" list="productList"
									headerKey="" headerValue="%{getText('txt.select')}"
									id="product1" theme="simple" enabled="true"
									onchange="resetUnitData();loadProductUnit();loadUnit();" />
							</div>
						</div>
					
						<div class="flexi flexi12 manadatoryField">
							<label for="txt" style="display: block; text-align: center"><s:text
									name="warehouseProduct.unit" /></label>
							<div class="form-element">
								<span id="productUnit"
									style="display: block; text-align: center"></span>
							</div>
						</div>
						<div class="flexi flexi12 manadatoryField">
							<label for="txt" style="display: block; text-align: center"><s:text
									name="warehouseProduct.currentStock" /></label>
							<div class="form-element">
								<span class="availStock" id="productStock"
									style="display: block; text-align: center"> 0.00 </span>
							</div>
						</div>
						
						<s:if test="enableBatchNo==1">
							<div class="flexi flexi12">
								<label for="txt"><s:text
										name="%{getLocaleProperty('warehouseProduct.batchNo')}" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield name="selectedBatchNo" id="batchNo" maxlength="35" />
									<div id="validatebatchNoError"
										style="text-align: center; padding: 5px 0 0 0; color: red; font-weight: normal;"></div>
								</div>
							</div>
						</s:if>
						
						<div class="flexi flexi12">
							<label for="txt"><s:text name="warehouseProduct.goodQty" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<div class="button-group-container">
									<s:textfield id="stock" name="stock" maxlength="5"
										onkeypress="return isNumber(event)"
										onkeyup="calTotalQty(this); calTotalAmt();" enabled="true"
										cssStyle="text-align:right;padding-right:1px; width:90%;" />
									<div>.</div>
									<s:textfield id="stockPaise" name="stockPaise" maxlength="3"
										onkeypress="return isNumber(event)"
										onkeyup="calTotalQty(this); calTotalAmt();" enabled="true"
										cssStyle="text-align:right;padding-right:1px; width:60%;" />

								</div>
								<div id="validateProdQtyError"
									style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
							</div>
						</div>
						<div class="flexi flexi12 manadatoryField">
							<label for="txt"><s:text
									name="warehouseProduct.damagedQty" /></label>
							<div class="form-element">
								<div class="button-group-container">
									<s:textfield id="damagQty" name="damagedQty" maxlength="5"
										onkeypress="return isNumber(event)"
										cssStyle="text-align:right;padding-right:1px; width:90%;"
										onkeyup="calTotalQty(this); calTotalAmt();" />

									<div>.</div>

									<s:textfield id="damagQtyPaise" name="damagedQtyPaise"
										maxlength="3" onkeypress="return isNumber(event)"
										cssStyle="text-align:right;padding-right:1px; width:60%;"
										onkeyup="calTotalQty(this); calTotalAmt();" />
								</div>
							</div>
						</div>
						<div class="flexi flexi12">
							<label for="txt"><s:text name="warehouseProduct.totalQty"  /></label>
							<div class="form-element">
								<span id="totalStockQty"></span>
							</div>
						</div>
						<%-- <div class="flexi flexi10 manadatoryField">
							<label for="txt"><s:text name="warehouseProduct.amount" /></label>
							<div class="form-element">
								<span id="prodAmt"></span>
							</div>
						</div> --%>
						<div class="flexi flexi12">
							<label for="txt"><s:text name="action" /></label>
							<td colspan="4" class="alignCenter">
								<table class="actionBtnWarpper">

									<td class="textAlignCenter">

										<button type="button" class="btn btn-sm btn-success"
											aria-hidden="true" onclick="addRow()" id="addButton"
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


			

			<%-- <div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">

					<div class="flexiWrapper filterControls">

						
						<div class="flexi flexi10 manadatoryField">
							<label for="txt"><s:text
									name="warehouseProduct.costPrice" /></label>
							<div class="form-element">
								<div class="button-group-container">
									<s:textfield id="costPriceRupees" name="costpriceRupee"
										maxlength="5" onkeypress="return isNumber(event)"
										onkeyup="calTotalAmt();"
										cssStyle="text-align:right;padding-right:1px; width:90%!important;" />
									<div>.</div>
									<s:textfield id="costPricePaise" name="costpricePaise"
										maxlength="2" onkeypress="return isNumber(event)"
										onkeyup="calTotalAmt();"
										cssStyle="text-align:right;padding-right:1px; width:90%!important;" />
									<div id="validatePriceError"
										style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
								</div>
							</div>
						</div>
						
						
					</div>
				</div>

				<div class="button-group-container margin-top-10">
													<legend style="float: left;margin-right: 16px"><button type="button" class="btn btn-sm btn-success"
														aria-hidden="true" onclick="addRow();"
														title="<s:text name="titleOk" />">
														<i class="fa fa-check"></i>
													</button>
													</legend> <legend><button type="button" class="btn btn-sm btn-danger"
														aria-hidden="true" onclick="resetProductData()"
														title="<s:text name="titleCancel" />">
														<i class="fa fa-trash"></i></legend>
												</div>

			</div> --%>
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
                   <h2>
					<s:text name="Added Stock Information"/> 
					</h2>
					<table class="table table-bordered aspect-detail"
						id="productInfoTbl">
						<thead>
							<tr>
								<th><s:text name="s.no" /></th>
								<%-- <s:if test="currentTenantId!='sagi'"> --%>
									<th><s:text name="warehouseProduct.category" /></th>
								<%--</s:if>--%>
								<th><s:text
										name="%{getLocaleProperty('warehouseProduct.product')}" /></th>
								
									<th><s:text name="warehouseProduct.unit" /></th>
									<th><s:text name="warehouseProduct.currentStock" /></th>
								
								<s:if test="enableBatchNo==1">
									<th><s:text
											name="%{getLocaleProperty('warehouseProduct.batchNo')}" /></th>
								</s:if>
						
									<%-- <th><s:text name="warehouseProduct.costPrice" /></th> --%>
								
									<th><s:text name="warehouseProduct.goodQty" /></th>
									<th><s:text name="warehouseProduct.damagedQty" /></th>
									<th><s:text name="warehouseProduct.totalQty" /></th>
									<%-- <th><s:text name="warehouseProduct.amount" /></th> --%>
								
								<th><s:text name="action" /></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="13" align="center"><s:text
										name="warehouseStockNoRecordFound" /></td>
							</tr>
						</tbody>
						
					</table>


					<div id="paynmentAmountDiv" style="display: block;">
						<%-- <h2>
							<s:text name="info.vendorPayment" />
						</h2> --%>
						<table id="vendorInfoTbl"
							class="table table-bordered aspect-detail">

							<thead>
								<%-- <tr class="odd">
									<th width="25%"><s:text
											name="warehouseProduct.vendorAccBal" /></th>
									<th width="25%"><s:text name="warehouseProduct.type" /></th>
									<th width="25%" class="cashTypeDivLabel"><s:text
											name="warehouseProduct.enterAmt" /></th>
									<th width="25%" class="creditTypeDivLabel"><s:text
											name="warehouseProduct.creditText" /></th>
									<th width="25%"><s:text name="warehouseProduct.remarks" /></th>
								</tr> --%>

								<tr>
									<!-- 	<td> -->

									<%-- <div class="row">
											<div class="col-sm-4">
												<s:text name="cashType1" />
											</div>
											<div class="col-sm-5" id="vendorCashBal"
												style="font-weight: bold;"></div>

											<div class="col-sm-4">
												<s:text name="cashType2" />
											</div>

											<div class="col-sm-5" id="vendorCreditBal"
												style="font-weight: bold;"></div>
										</div>  --%>
									<%-- <table class="table table-bordered aspect-detail">
														<tr>
															<td></td>
															<td id="vendorCashBal" style="font-weight: bold;"></td>
															<td><s:text name="cashType2" /> :</td>
															<td id="vendorCreditBal" style="font-weight: bold;"></td>
														</tr>
													</table> --%>
									<!-- </td> -->

									<%-- <td><s:radio list="cashType" listKey="key"
											listValue="value" name="amtType" theme="simple"
											onchange="hideDivBasedOnCashType(this); " value="1"
											checked="checked" /></td> --%>

									<td class="cashTypeDiv hide"><s:textfield
											id="vendorCashAmtRupees" name="cashAmtRupee" maxlength="10"
											onkeypress="return isNumber(event)"
											cssStyle="text-align:right;padding-right:1px; width:50px!important;" />
										.<s:textfield id="vendorCashAmtPaise" name="cashAmtPaise"
											maxlength="2" onkeypress="return isNumber(event)"
											cssStyle="text-align:right;padding-right:1px; width:50px!important;" />
									</td>

									<td class="creditTypeDiv hide">
										<div id="vendorCreditAmt"></div>
									</td>
									<%-- <td><s:textarea id="remarks" name="remarks" /></td> --%>
								</tr>

							</thead>

							<thead>
								<tr class="odd">
									<th width="25%" style="display: none;"><s:text
											name="warehouseProduct.vendorFinalBal" /></th>
								</tr>

								<tr style="height: 40px">
									<td class="alignRight"
										style="text-align: center; padding-right: 10px !important; display: none;">
										<div id="vendorFinalTaxAmt" style="font-weight: bold;"></div>
									</td>

								</tr>
							</thead>

						</table>
						<%-- <div class="flexiWrapper">
										<div class="flexi flexi10">
													<label for="txt"><s:text name="warehouseProduct.vendorAccBal" /></label>
													<div class="form-element">
													<label class="radio-inline">
													 	<s:text name="cashType1" />
													</label>
													<label class="radio-inline">
													<s:text name="cashType2" />
													</label>
													</div>
										</div>
										
										<div class="flexi flexi10">
											<label for="txt"><s:text name="warehouseProduct.type" /></label>
													<div class="form-element">
													<label class="radio-inline">
													 	<s:radio list="cashType" listKey="key"
														listValue="value" name="amtType" theme="simple"
														onchange="hideDivBasedOnCashType(this); " value="1"
														checked="checked" />
													</label>
													
													</div>
										
										</div>
										
										<div class="flexi flexi10 cashTypeDivLabel">
											<label for="txt"><s:text
														name="warehouseProduct.enterAmt" /></label>
													<div class="form-element">
													<div class="button-group-container">
												<s:textfield
														id="vendorCashAmtRupees" name="cashAmtRupee"
														maxlength="10" onkeypress="return isNumber(event)"
														cssStyle="text-align:right;padding-right:1px; width:90px!important;" />
													<div>.</div>
													
													<s:textfield id="vendorCashAmtPaise" name="cashAmtPaise"
														maxlength="2" onkeypress="return isNumber(event)"
														cssStyle="text-align:right;padding-right:1px; width:90px!important;" />
													</div>
													</div>
										
										</div>
										<div class="flexi flexi10 vendorCreditAmt">
											<label for="txt"><s:text
														name="warehouseProduct.creditText" /></label>
													<div class="form-element">
												<span id="vendorCreditAmt"></span>
													
													</div>
										
										</div>
										<div class="flexi flexi10">
											<label for="txt"><s:text name="warehouseProduct.remarks" /></label>
													<div class="form-element">
												<s:textarea id="remarks" name="remarks" />
													
													</div>
										
										</div>
										
									</div> --%>


					</div>
				</div>


				<div class="yui-skin-sam" id="loadList" style="display: block">
					<sec:authorize ifAllGranted="service.warehouseProduct.create">
						<span id="savebutton" class=""> <span class="first-child">
								<button id="sucessbtn" class="save-btn btn btn-success"
									type="button" class="save-btn" onclick="event.preventDefault();submitStock();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span>
						</span>
					</sec:authorize>
					<span class=""> <span class="first-child"><a
							id="cancelbutton" onclick="cancel();"
							class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
									<s:text name="cancel.button" />
							</font>
						</a></span></span>
				</div>

			</div>

		</div>
	</div>


	<!--end decorator body -->


	<s:hidden id="productList" name="warehouseProductList" />
</s:form>

<s:form name="listForm" id="listForm" action="warehouseProduct_list">
	<s:hidden name="currentPage" />
</s:form>

<%-- <div id="restartAlert" class="popPendingMTNTAlert" style="display:none">
<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
	align="center"></div>
<div id="defaultRestartAlert">	
	<div id="divMsg" align="center"></div>
	<table align="center" >
	<tr>
	<td id="closePopup" style="text-align: center"><input id="ok"
			type="button" class="popBtn" value="<s:text name="ok"/>"
			onclick="disablePopupAlert();" /></td>
	</tr>
</table>
</div>
</div> --%>

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
            <span><h5><s:text name="warehouseStockMsg" /></h5></span>
				<div id="divMsg" align="center"></div>
			</div>
			</div>
			<div class="contentFtr">
			<button type="button" class="btn btn-danger btnBorderRadius" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button>
				<button type="button" class="btn btn-success btnBorderRadius" data-dismiss="modal"
					onclick="document.createform.submit()">
					<s:text name="Continue" />
				</button>
			</div>
		</div>
	</div>

</div>


<script type="text/javascript">

var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();

var tenant = '<s:property value="currentTenantId"/>';
jQuery(document).ready(function(){	  
	//alert("Rows = " + ($('table#productInfoTbl th:last').index() + 1));
	
	//alert(tenant);
	 $("#sucessbtn").on('click', function (event) {  
	              event.preventDefault();
	              var el = $(this);
	              el.prop('disabled', true);
	              setTimeout(function(){el.prop('disabled', false); }, 1000);
	        });
	 
	
	
	if($("#enableBatchNo").val()=='1'){
		var th = ($('table#productInfoTbl th:last').index() + 1);
		var values= th - 3;
		$('#totalAmt').attr("colspan",values);
		$('#taxAmt').attr("colspan",values);
		$('#finalAmt').attr("colspan",values); 
		
		}else{
			var th = ($('table#productInfoTbl th:last').index() + 1);
			//alert(th);
			var values= th - 3;
			 $('#totalAmt').attr("colspan",values);
				$('#taxAmt').attr("colspan",values);
				$('#finalAmt').attr("colspan",values); 
			/* $('#totalAmt').attr('colspan',8);
			$('#taxAmt').attr('colspan',8);
			$('#finalAmt').attr('colspan',8); */
		}
	
		
	if('<s:property value="command" />'=="update") {
		var productData = '<s:property value="productTotalString" />'.split("|");
		
		for(var j=0; j < productData.length; j++){
			var productDataArray = productData[j].split("#");
			addEditRow(productDataArray[0], productDataArray[1]);
		}
	}

	<s:if test='stockDescription!=null'>
	jQuery('<tr><td><s:property value="stockDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first"))));
	jQuery("#restartAlert").css('height','180px');
</s:if>
var distributi='';
if(distributi=="")
{
	$('#restartAlert').hide();
}  

else
{
	$('#restartAlert').show();
}
hideDivBasedOnCashType('');
 resetPrefixAndSuffix();

 
});

function resetPrefixAndSuffix(){
	 //jQuery("#costPriceRupees").val("0");
	 //jQuery("#costPricePaise").val("00");
	 jQuery("#damagQty").val("0");
	 jQuery("#damagQtyPaise").val("000");
	 jQuery("#stock").val("0");
	 jQuery("#stockPaise").val("000");
	 
	// jQuery("#expMth").val("00");
// jQuery("#expYr").val("00");
	 //jQuery("#orderNo").val("00");
	 
}

function hideDivBasedOnCashType(evt){
	 var cashVal = $(evt).val();
	 if(cashVal==0){
		jQuery(".cashTypeDivLabel").show();
		jQuery(".cashTypeDiv").show();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		jQuery("#vendorCashAmtRupees").val("0");
		jQuery("#vendorCashAmtPaise").val("00");
        document.getElementById("vendorCreditAmt").innerHTML="";
	}
	else if(cashVal==1  || cashVal==undefined){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").show();
		jQuery(".creditTypeDiv").show();
		jQuery("#vendorCashAmtRupees").val("0");
		jQuery("#vendorCashAmtPaise").val("00");
		jQuery("vendorCreditAmt").innerHTML= jQuery("finalTaxAmount").innerHTML;
	}
	else {
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery("#vendorCashAmtRupees").val("0");
		jQuery("#vendorCashAmtPaise").val("00");
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		$('input:radio[name=amtType][value=1]').prop('checked',true);
	
		jQuery("vendorCreditAmt").innerHTML= jQuery("finalTaxAmount").innerHTML;
	}
}

function resetUnitData(){
	jQuery(".prodError").text('');	
	jQuery("#totalStockQty").text('');	
	//document.getElementById('costPriceRupees').value = 0;
	document.getElementById('stock').value = 0;
	document.getElementById('stockPaise').value = 000;
	//document.getElementById('expMth').value = 00;
//	document.getElementById('expYr').value = 00;
//	document.getElementById('orderNo').value = 00;
	document.getElementById("validateError").innerHTML="";
	
	//document.getElementById("validateProdError").innerHTML="";	
	//document.getElementById("validateProdQtyError").innerHTML="";
	//document.getElementById("validatePriceError").innerHTML="";
	//document.getElementById("validateExpError").innerHTML="";	
}


function validateData() {
	
	var hit=validateDetails(true);
	if(hit){
		var totalPrice=jQuery("#totalPriceLabel").val();
		if(parseFloat(totalPrice)==0){
			document.getElementById("validateError").innerHTML="inValid.stock";
		}else{
		   document.getElementById("validateError").innerHTML="";
		   reload();
		}
	}
}

function showProductEnrollDetail(){	
	jQuery("#product1").focus();
}

function reload() {
	addRow();
	resetProductData();
}

function resetData(){
	/* if(document.getElementById('product1').selectedIndex==0){
		$('#product1').children('option:not(:first)').remove();
	}

	var idsArray=['categoryId','product1','vendor','warehouse']; */
	//resetSelect2(idsArray);
	
	
	document.getElementById("validateError").innerHTML="";
	document.getElementById("productStock").innerHTML=" 0.00";
	document.getElementById("totalStockQty").innerHTML="";
	//document.getElementById("prodAmt").innerHTML="";
	
	
	productsInfoArray = new Array();
	pArrayList = new Array();
	productTotalArray=new Array();
	productNameArray=new Array();
	reloadTable();

}

function resetUnitAndAvastock(){
	document.getElementById("productUnit").innerHTML="";
	document.getElementById("productStock").innerHTML=" 0.00";
	//$("#costPriceRupees").val("00");
    //$("#costPricePaise").val("00");
}

function check() {

	jQuery(".save-btn").attr('enabled',true);
	var selectedWarehouse = $("#warehouse option:selected").val();
	if(selectedWarehouse == "" || selectedWarehouse == null) {
		document.getElementById("validateError").innerHTML='<s:property value='%{getLocaleProperty("empty.warehouse")}' />';	
		return false;	
 	}
	var stockAddedDate = $("#startDate").val();
	if(stockAddedDate == "" || stockAddedDate == null) {
	 	document.getElementById("validateError").innerHTML='<s:text name="empty.addedDate"/>';	
	return false;
	}
	var productName = $("#product1 option:selected").text();
	
	var vendor = $("#vendor option:selected").text();
	var stock=document.getElementById("stock").value;
	var orderNo=document.getElementById("orderNo").value;	
	//var variety=document.getElementById("variety").value;
	//var selectedVariety=document.getElementById("variety").value;
	if($("#enableBatchNo").val()=='1'){
	var batchno=document.getElementById("batchNo").value;
	}
	document.getElementById("validateProdError").innerHTML = "";
	document.getElementById("validateProdQtyError").innerHTML = "";
	document.getElementById("validatePriceError").innerHTML = "";
	document.getElementById("validateBatchError").innerHTML = "";
	$.post("warehouseProduct_populateValidation",{dt:new Date(),stock:stock,selectedWarehouse:selectedWarehouse,selectedProduct:productName,selectedVendor:vendor,batchno:batchno},function(data){
			if(data == null || data == ""){
				addRow();
			}else {
				errors= data
				if(errors=='<s:text name="emptyStock"/>'){
					jQuery("#validateProdQtyError").html(errors);
				}
				if(errors=='<s:text name="batchno.warn"/>'){
					jQuery("#validateBatchError").html(errors);	
				}
	 	 	}
	 	});
		
}


function addRow(){
    
	var editIndex = getEditIndex();		
	var hit=true;
	var selectedProduct = $("#product1 option:selected").val();	
	
	//var selectedProduct = $("#product1 option:selected").val();	
	var selectedWarehouseCode = document.getElementById("warehouse").value;
	//var productName=document.getElementById("product1").value;
	var season=document.getElementById("season").value;
		var productName = $("#product1 option:selected").text();
	//var productId = $("#product1 option:selected").value();
 	var stockPre = document.getElementById("stock").value;
 	var stockSuff = document.getElementById("stockPaise").value;
	var stock=stockPre+"."+stockSuff;
	 $("#validateProdError").html('');
	$("#validateProdQtyError").html('');
	$("#validatePriceError").html(''); 
	var productStock=document.getElementById("productStock").innerHTML;	

	//document.getElementById("validateExpError").innerHTML = "";
	 if($("#enableBatchNo").val()=='1'){
			//document.getElementById("batchNo").innerHTML = "";
			 var batchNo =document.getElementById("batchNo").value;
		  //$('#batchNo').val('');
	 }

	var selectedCategory = $("#categoryId option:selected").text();
	
	var selectedCategoryId = document.getElementById("categoryId").value;
	
	//var coPrice=document.getElementById("costPrice").value;
	var damQtyPrf = document.getElementById("damagQty").value;
	var damQtySuff = document.getElementById("damagQtyPaise").value;
	var damQty = damQtyPrf+"."+damQtySuff;
	var totQty = $("#totalStockQty").text();
	//var totAmt = $("#prodAmt").text();
	var proUnitVal = document.getElementById("productUnit").innerHTML;	

	//var coPricePrefix = document.getElementById("costPriceRupees").value;
	//var coPriceSuffix = document.getElementById("costPricePaise").value;
	//var costPricePreSuf =  coPricePrefix +"."+coPriceSuffix;
   /*  var currentDate = new Date();
    var currentMonth = currentDate.getMonth()+1;
    var currentYear = currentDate.getFullYear(); */
  
    var tenant='<s:property value="getCurrentTenantId()"/>';
  

	if(selectedProduct == "" || selectedProduct == null|| selectedProduct == '0') {
		//document.getElementById("validateProdError").innerHTML='<s:text name="empty.product"/>';
		jQuery(".prodError").text('<s:text name="empty.product"/>');
		hit=false;
		enableButton1();
		return false;
	}
	
	if($("#enableBatchNo").val()=='1'){
		if(batchNo==""){
			//document.getElementById("validatebatchNoError").innerHTML='<s:text name="emptyBatchNo"/>';
			jQuery(".prodError").text('<s:text name="emptyBatchNo"/>');
			return false;
		}
	} 
	
	$("#validateProdError").html('');
	
	/* if((coPricePrefix == 0) && (coPriceSuffix == 00) ){
		document.getElementById('validatePriceError').innerHTML='<s:text name="empty.Price" />';
		return false;
	} 
	if((coPricePrefix <= 0)){
		document.getElementById('validatePriceError').innerHTML='<s:text name="min.price" />';
		return false;
	}*/

	var distStock = $("#stock").val();
	
	
	if((stock == "" || stock == null)){
		//document.getElementById('validateProdQtyError').innerHTML='<s:text name="emptyStock" />';
		jQuery(".prodError").text('<s:text name="emptyStock"/>');
		return false;
	}

	if((stock <= 0 )){
		//document.getElementById('validateProdQtyError').innerHTML='<s:text name="emptystock" />';
		jQuery(".prodError").text( '<s:property value="%{getLocaleProperty('empty.goodStock')}"/>');
		return false;
		
	}
	if(distStock > 5000000){
		//document.getElementById('validateProdQtyError').innerHTML='<s:text name="max.stock" />';
		jQuery(".prodError").text('<s:text name="max.stock"/>');
		return false;		
	}
	
	
    
	if($("#enableBatchNo").val()=='1'){
		//document.getElementById("batchNo").innerHTML = "";
		 var batchNo =document.getElementById("batchNo").value;
		 $.post("warehouseProduct_populateProductAvailableStock",{selectedWarehouse:selectedWarehouseCode,selectedProduct:selectedProduct,selectedSeason:season,batchno:batchNo},function(data){
				if(data!=null && data!=""){
					productStock=parseInt(data);
				 	
							
				}
				else{
					productStock = 0;
				}
				if(editIndex==-1){
					productsInfoArray[productsInfoArray.length-1].productStock = productStock;
				}else{
					productsInfoArray[editIndex].productStock = productStock;
				}
				reloadTable();
				
		});
      
	 
}
	
	
	
	var Qty,QtyPerUnit;
	var productExists=false;	
	var batchExists=false;
	Qty=stock;
	var productArray=null;
	if(editIndex==-1){
		//alert(selectedVariety+"&&"+selectedCategory+"###"+productName+"--"+proUnitVal+"**"+productStock+"++"+costPricePreSuf+"@@"+stock+"!!"+damQty+"%%"+totQty+"&&"+totAmt+"##"+selectedUom+"@@"+parentCode+"--"+packetInfo+"^^"+selectedSeed);
		
		productArray = {
						 category : selectedCategory,
						 categoryId : selectedCategoryId,
						 name : productName,
						 //uGroup:selectedUom,
						
						 unit : proUnitVal,
						 productStock :productStock,
						 //price : costPricePreSuf,
						 productId :selectedProduct,
						// costPriceRupees:coPricePrefix,
					    // costPricePaise:coPriceSuffix,
						 //sGroup:selectedSeed,
						 stock : stock,
						
						 
						 badQty : damQty,
						 tQty : totQty,
						
						 
						 batchNo : batchNo,
					
						 isEdit:false
			         };
			
					for(var cnt=0;cnt<productsInfoArray.length;cnt++) {
					
					 if(productsInfoArray[cnt].productId==productArray.productId && productsInfoArray[cnt].batchNo==productArray.batchNo  &&  productsInfoArray[cnt].seed==productArray.seed && productsInfoArray[cnt].variety==productArray.variety){
					
						 productExists=true;
							// alert(coPriceSuffix);
							// productsInfoArray[cnt].stock=parseFloat(parseFloat(productsInfoArray[cnt].stock)+parseFloat(productArray.stock)).toFixed(2);
							 productsInfoArray[cnt].price=parseFloat(productArray.price);
							 productsInfoArray[cnt].stock=parseFloat(parseFloat(productsInfoArray[cnt].stock)+parseFloat(productArray.stock)).toFixed(2);
							 productsInfoArray[cnt].badQty=parseFloat(parseFloat(productsInfoArray[cnt].badQty)+parseFloat(productArray.badQty)).toFixed(2);
							 productsInfoArray[cnt].tQty=parseFloat(parseFloat(productsInfoArray[cnt].tQty)+parseFloat(productArray.tQty)).toFixed(2);
							// productsInfoArray[cnt].tAmt=parseFloat(parseFloat(productsInfoArray[cnt].tAmt)+parseFloat(productArray.tAmt)).toFixed(2);
							// productsInfoArray[cnt].tAmt=productsInfoArray[cnt].tQty* productsInfoArray[cnt].price;
							 productsInfoArray[cnt].expDate=productArray.expDate;
							// productsInfoArray[cnt].costPriceRupees=productArray.costPriceRupees;
							// productsInfoArray[cnt].costPricePaise=productArray.costPricePaise;
						}	
					}
				 if(!productExists)
				 productsInfoArray[productsInfoArray.length] = productArray;
				
				 /* for(var cnt=1;cnt<productsInfoArray.length;cnt++) {
					 alert("fdh");
									 for(var i=0;i<cnt;i++){
					 if(productsInfoArray[i].name!=productsInfoArray[cnt].name && productsInfoArray[i].variety==productsInfoArray[cnt].variety){
						 removeRow(cnt);
						 jQuery(".prodError").text('<s:text name="Diff product with same sagi code is not added"/>');
						 return false;
						 } }
					 }*/
		 
				} else {
					
					//alert(selectedCategory)
					productArray=productsInfoArray[editIndex];
				    editproductTotalArray(productArray);
				    productsInfoArray[editIndex].category= selectedCategory;
				    productsInfoArray[editIndex].categoryId= selectedCategoryId,
				   // alert(selectedProduct)
				    productsInfoArray[editIndex].productId= selectedProduct;
					productsInfoArray[editIndex].name= productName;
						productsInfoArray[editIndex].productStock= productStock;
					//productsInfoArray[editIndex].price = costPricePreSuf;
					productsInfoArray[editIndex].stock = stock;
					productsInfoArray[editIndex].badQty = damQty;
					productsInfoArray[editIndex].tQty = totQty;
					//productsInfoArray[editIndex].tAmt = parseFloat(totAmt).toFixed(2);
					if($("#enableBatchNo").val()=='1'){
					productsInfoArray[editIndex].batchNo = batchNo;
					}
				
					productsInfoArray[editIndex].isEdit=false;
				//	productsInfoArray[editIndex].expMonth=expMnVal;
					productArray=productsInfoArray[editIndex];
					//alert("-5555")
				/* 	 for(var cnt=1;cnt<productsInfoArray.length;cnt++) {
									 for(var i=0;i<cnt;i++){
					 if(productsInfoArray[i].name!=productsInfoArray[cnt].name && productsInfoArray[i].variety==productsInfoArray[cnt].variety){
						 removeRow(cnt);
						 document.getElementById("validateError").innerHTML='<s:text name="Diff product with same sagi code is not added"/>';
						 return false;
						 } }
					 } */
				}
	
	  // $('#categoryId').prop('selectedIndex','');
	//$("#categoryId").val("");
	//document.getElementById('product1').selectedIndex =0;
	
	resetProductData();
	reloadTable();
	
}


function addEditRow(productName, productQty) {
	var editIndex = getEditIndex();
	var stock = productQty;
	
	var Qty,QtyPerUnit;
	var productExists=false;	
	
	Qty=stock;
	var productArray=null;
	
	if(editIndex==-1){
	 productArray = {
			 name : productName.split('=')[0],
			 stock : stock,
			 isEdit:false
         };
     
	 for(var cnt=0;cnt<productsInfoArray.length;cnt++) {
		if((productsInfoArray[cnt].batchno== productArray.batchno) && (productsInfoArray[cnt].name== productArray.name) ){
		 if(productsInfoArray[cnt].vendor== productArray.vendor){
			 productExists=true;
			 productsInfoArray[cnt].stockN=parseFloat(productsInfoArray[cnt].stockN)+parseFloat(productArray.stockN);
			 productsInfoArray[cnt].stockD=parseFloat(productsInfoArray[cnt].stockD)+parseFloat(productArray.stockD);			}		
		}else{
            batchExists=true;
		}
	 }
     if(!productExists)
	 	productsInfoArray[productsInfoArray.length] = productArray;
	 
	} else {
		productArray=productsInfoArray[editIndex];
	    editproductTotalArray(productArray);
		// productsInfoArray[editIndex].name= productName.split('=')[0];
		productsInfoArray[editIndex].stock = stock;
		//productsInfoArray[editIndex].costPriceRupees = costPriceRupees
		productsInfoArray[editIndex].isEdit=false;
		productArray=productsInfoArray[editIndex];
	}
	
	reloadTable();
	resetProductData();

}

function editRow(indx) {
	 //  alert(productsInfoArray[indx].category)
	    jQuery('#categoryId').val(productsInfoArray[indx].categoryId);
	   // alert(productsInfoArray[indx].productId)
        	  listProduct(document.getElementById('categoryId'),productsInfoArray[indx].productId);
		//addOptionService(document.getElementById("product1"), productsInfoArray[indx].name, productsInfoArray[indx].productId); //this line commented to fix bug id 598 c&a.		
		document.getElementById("productStock").innerHTML=productsInfoArray[indx].productStock;
		//document.getElementById("costPrice").value=productsInfoArray[indx].price;
		document.getElementById('stock').value  = productsInfoArray[indx].stock.split(".")[0];
		document.getElementById('stockPaise').value  = productsInfoArray[indx].stock.split(".")[1];
		document.getElementById('damagQty').value  = productsInfoArray[indx].badQty.split(".")[0];
		document.getElementById('damagQtyPaise').value  = productsInfoArray[indx].badQty.split(".")[1];
		document.getElementById('totalStockQty').innerHTML  = productsInfoArray[indx].tQty;
		//document.getElementById('prodAmt').innerHTML  = productsInfoArray[indx].tAmt;
		document.getElementById("productUnit").innerHTML=productsInfoArray[indx].unit;
	//	document.getElementById("variety").innerHTML=productsInfoArray[indx].variety;
	
		if($("#enableBatchNo").val()=='1'){
			document.getElementById("batchNo").value=productsInfoArray[indx].batchNo;
			}
		//alert(productsInfoArray[indx].costPriceRupees)
		//document.getElementById("costPriceRupees").value=productsInfoArray[indx].costPriceRupees;
	   // document.getElementById("costPricePaise").value=productsInfoArray[indx].costPricePaise;
		//document.getElementById("costPricePreSuf").value =productsInfoArray[indx].pprice +"."+productsInfoArray[indx].sprice;
		
		//alert(productsInfoArray[indx].unit+"###"+productsInfoArray[indx].expMonth+"###"+productsInfoArray[indx].expYear);
		resetEditFlag();
		productsInfoArray[indx].isEdit=true;
		calulateAvailableStock();
		//reloadTable();
		
		jQuery(".select2").select2();
		
	   
}

function calulateAvailableStock(){	
	
	var selectedProduct = document.getElementById('product1').value;
	var stock = document.getElementById('stock').value;
}
function reloadTable(){
	
	var enableBatchNo=$("#enableBatchNo").val();
	if($("#enableBatchNo").val()=='1'){
	var batchNo =document.getElementById("batchNo").value;
	
		}
	var tbodyRow = "";
	var tfootArray = new Array();	
	jQuery('#productInfoTbl > tbody').html('');
	
	var rowCount=0;
	for(var cnt=0; cnt<productsInfoArray.length; cnt++){
			
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:left;width: 10%;">'+productsInfoArray[cnt].category+'</td>'+
					'<td style="text-align:left;width: 10%;">'+productsInfoArray[cnt].name+'</td>'+
				
					'<td style="text-align:left;">'+productsInfoArray[cnt].unit+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].productStock+'</td>'+
				
					/* if($("#enableBatchNo").val()=='1'){
					'<td style="text-align:left;">'+productsInfoArray[cnt].batchNo+'</td>'+
					} */
					((enableBatchNo === '1') ? '<td style="text-align:left;">'+productsInfoArray[cnt].batchNo+'</td>' :'') +
					//'<td style="text-align:right;">'+productsInfoArray[cnt].price+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].stock+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].badQty+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].tQty+'</td>'+
					//'<td style="text-align:right; width:1%">'+productsInfoArray[cnt].tAmt+'</td>'+
				
					'<td style="border:none!important; width :6%"><legend style="display: inherit!important;"><button type="button" class="btn btn-sm btn-primary" aria-hidden="true" onclick="editRow('+cnt+')" title="<s:text name="Edit" />"><i class="fa fa-pencil"></i></button></legend>'+
					'<legend style="display: inherit!important;"><button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="removeRow('+cnt+')" title="<s:text name="Delete" />"><i class="fa fa-trash"></i></button></legend></td>'+
					'</tr>';
					
		if(tfootArray.length==0){
			tfootArray[tfootArray.length] = {
											 totalPrice : productsInfoArray[cnt].tAmt
										 	};
		} else { 
		   tfootArray[tfootArray.length-1].totalPrice=(parseFloat(tfootArray[tfootArray.length-1].totalPrice)+parseFloat(productsInfoArray[cnt].tAmt));
		}
	  }	
	}	
	
	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="12" style="text-align:center"><s:text name="warehouseStockNoRecordFound"/></td>'+
					'</tr>';
	}
	
	jQuery('#productInfoTbl > tbody').html(tbodyRow);
	//jQuery('#productInfoTbl > tbody').html('');
	var proudctDataRow="";
	proudctDataRow += '<tr>'+
	'<td style="text-align:center;"></td>'+
	'<td style="text-align:left;"></td>'+	
	'</tr>';
	//jQuery('#productInfoTbl > tbody').html(proudctDataRow);
	var taxAmtValue = $("#amtTax").val();
	var tfootRow = "";
	jQuery('#productInfoTbl > tfoot').html('');
	/* tfootRow += '<tr>'+
	'<td  id="totalAmt" colspan="8" style="border:none!important;">&nbsp;</td>'+
	'<td  class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="totalAmt"/></td>';

	if(tfootArray.length>0){
			tfootRow +=		'<td id="totalAmtVal" class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2)+'</td>';
	}else{
		 tfootRow +='<td class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">0.00</td>';
	}
	tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
			   '</tr>';

	tfootRow += '<tr>'+
				'<td id="taxAmt" colspan="8" style="border:none!important;">&nbsp;</td>'+
				'<td  class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="warehouseProduct.tax"/></td>';
	tfootRow+='<td class="alignRight"><s:textfield name="tax" maxlength="5" id="amtTax" onkeypress="return isDecimal(event)" onkeyup="calAmtWithTax();"  cssStyle="text-align:right;padding-right:1px; width:90px!important;"/><div id="validateTaxError" style="text-align: center; padding: 5px 0 0 0; color: red;font-weight: normal;width:150px;"></div></td>';
			  '</tr>';

	tfootRow += '<tr>'+
				'<td id="finalAmt" colspan="8" style="border:none!important;">&nbsp;</td>'+
				'<td  class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="warehouseProduct.finalAmt"/></td>';
	if(tfootArray.length>0){
		tfootRow +=		'<td id="finalTaxAmount" class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2)+'</td>';
		jQuery("#vendorCreditAmt").text(parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2));
	}else if(tfootArray.length>0 && taxAmtValue!=""){
	 tfootRow +='<td id="finalTaxAmount" class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+finalAmtWithTax+'</td>';
	}else{
		tfootRow +='<td id="finalTaxAmount" class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">0.00</td>';
	} */
			   
jQuery('#productInfoTbl > tfoot').html(tfootRow);
//jQuery("#product1").focus();
jQuery('#categoryId').selectedIndex=-1;
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
	    if(productTotalArray[i].name==productsInfoArray[indx].name+'='+productsInfoArray[indx].units || productTotalArray[i].batchno==productsInfoArray[indx].batchNo){
		productTotalArray[i].qty-=productsInfoArray[indx].QtyPerUnit;
		break;
		}
	}
	
	if(productsInfoArray.length>0){			
		productsInfoArray.splice(indx,1);		
	}
	
	resetProductData();
	reloadTable();
}

function buildReqProductObject() {
	
	pArrayList = new Array();	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
	pArrayList[pArrayList.length] =	productsInfoArray[cnt].name.split('=')[0]+'|'+productsInfoArray[cnt].stock;
	}
}

function submitStock() {
	var hit=true;		
	var selectedWarehouse = $("#warehouse option:selected").val();
	var orderNo=document.getElementById("orderNo").value;
	//var variety=document.getElementById("variety").value;
	//var selectedVariety=document.getElementById("variety").value;
	var startDate=document.getElementById("startDate").value;
	var selectedProduct = $("#product1 option:selected").val();
	var selectedVendor = $("#vendor option:selected").text();
	var selectedVendorValue = $("#vendor option:selected").val();
	var taxValue =getElementValueById("amtTax"); //document.getElementById("amtTax").value;
	var finalAmtValue = jQuery("finalTaxAmount").text();//document.getElementById("finalTaxAmount").innerHTML;
	var remarks = $("#remarks").val();
	var paymentMode = '';
	//var paymentAmount = $("#vendorCashAmt").val();
	var payAmtPrefix = $("#vendorCashAmtRupees").val();
	var payAmtSuffix = $("#vendorCashAmtPaise").val();
	var paymentAmount = payAmtPrefix+"."+payAmtSuffix;
	var creditAmount = document.getElementById("vendorCreditAmt").innerHTML;
	var selectedSeason=$("#season option:selected").val();
	var batchNo="NA";
	var enableBatchNo=$("#enableBatchNo").val();
	if(enableBatchNo =='1'){
	batchNo=document.getElementById("batchNo").value;
	}
	
	if(paymentMode==''||paymentMode===undefined){
		paymentMode=1;
	}
	
	if(selectedSeason == "" || selectedSeason == null) {
		document.getElementById("validateError").innerHTML='<s:text name="empty.season"/>';
		hit=false;
		enableButton();
		return false; 
	}
	
	
	if(selectedWarehouse == "" || selectedWarehouse == null) {
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.warehouse')}"/>';
		hit=false;
		 enableButton();
		return false; 
	}

	 if(orderNo<0){
		document.getElementById("validateError").innerHTML='<s:text name="invalid.orderNo"/>';
		hit=false;
		enableButton();
		return false;
	}
	 
	 if(orderNo=="" || orderNo==null){
			document.getElementById("validateError").innerHTML='<s:text name="empty.orderNo"/>';
			hit=false;
			enableButton();
			return false;
		}

	if(selectedVendorValue == "" || selectedVendorValue == null) {
		document.getElementById("validateError").innerHTML='<s:text name="%{getLocaleProperty('empty.vendor')}"/>';
		hit=false;
		enableButton();
		return false;
	}
	
	var stockAddedDate = $("#startDate").val();
	if(stockAddedDate == "" || stockAddedDate == null) {
 		document.getElementById("validateError").innerHTML='<s:text name="empty.addedDate"/>';
 		hit=false;
 		enableButton();
		return false;
	}
	
	if(productsInfoArray.length==0) {
		document.getElementById("validateError").innerHTML='<s:text name="warehouseStockNoRecordFound"/>';
		hit=false;
		enableButton();
		return false;
	}

	if(paymentMode == "" || paymentMode == null) {
		document.getElementById("validateError").innerHTML='<s:text name="empty.cashType"/>';
		hit=false;
		enableButton();
		return false;
	} 

	 if(paymentMode == 0 && payAmtPrefix == 0 && payAmtSuffix== 00) {
		document.getElementById("validateError").innerHTML='<s:text name="empty.amount"/>';
		hit=false;
		enableButton();
		return false;
	}
	if(paymentMode == 0 && payAmtPrefix <= 0 ) {
		document.getElementById("validateError").innerHTML='<s:text name="validAmunt"/>';
		hit=false;
		enableButton();crea
		return false;
	} 

	if(hit){
		var flag = true;
		buildReqProductObject();
		var productTotalString=' ';
		for(var i=0;i<productsInfoArray.length;i++){
			var amtVal ="0.00";
			if(enableBatchNo=="1"){
				productTotalString+=productsInfoArray[i].productId+"*"+productsInfoArray[i].stock+"*"+amtVal+"*"+productsInfoArray[i].badQty+"*"+amtVal+"*"+"expdate"+"*"+productsInfoArray[i].tQty+"*"+productsInfoArray[i].batchNo+"*"+"|";
			    var variety=productsInfoArray[i].variety;
			   
			    
			}
			else{
				
				productTotalString+=productsInfoArray[i].productId+"*"+productsInfoArray[i].stock+"*"+amtVal+"*"+productsInfoArray[i].badQty+"*"+amtVal+"*"+"expdate"+"*"+productsInfoArray[i].tQty+"*"+"NA"+"*"+"|";
			}
		}
	
		$.post("warehouseProduct_populateValidationOfOrderNo.action",{orderNo:orderNo}
		,function(data){
				if(data==="true")
				{	
					
					document.getElementById("orderNoValidat").innerHTML='<s:text name="isOrderNoExits"/>';
					resetProductData();						
				}	
				else{
					$.post("warehouseProduct_create.action",{selectedWarehouse:selectedWarehouse,orderNo:orderNo,dt:stockAddedDate,productTotalString:productTotalString,startDate:startDate,
						selectedVendorValue:selectedVendorValue,remarks:remarks,paymentMode:paymentMode,paymentAmount:paymentAmount,creditAmount:creditAmount,selectedSeason:selectedSeason,variety:variety},function(data,result){
							if(data == null || data == "")
							{
								
								document.getElementById("orderNoValidat").innerHTML='<s:text name="isOrderNoExits"/>';
								resetProductData();
							}
							if(result=='success')
							{
								document.getElementById("divMsg").innerHTML="";
								document.getElementById("enableModal").click();
								//disablePopupAlert();
								//enablePopupAlert();
								
							}
						else
						{
							document.form.action = "warehouseProduct_list.action";
							document.listForm.submit();
							//enablePopupAlert();
			 			}
				});
				}
	});
		
		
		
		}
}

function enableButton(){

		jQuery("#submitButton").prop('enabled',true);
	
}
function enableButton1(){

	jQuery("#addButton").prop('enabled',true);

}

function disableButton(){
	 
		jQuery("#submitButton").prop('disabled', true);
}
function cancel() {
	document.form.action = "warehouseProduct_list.action";
	document.listForm.submit();
}

function resetProductData(){
	
	resetEditFlag();
	reloadTable();	
	resetPrefixAndSuffix();
	
	$("#categoryId").select2("val", "");
	$('#product1').select2("val", "0");
	//document.getElementById('categoryId').selectedIndex="";
	document.getElementById("product1").selectedIndex=0;
	if(document.getElementById('product1').selectedIndex==''){
		$('#product1').children('option:not(:first)').remove();
	}
	
	document.getElementById("totalStockQty").innerHTML="";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("productStock").innerHTML="";
	document.getElementById("totalStockQty").innerHTML="";
	//document.getElementById("prodAmt").innerHTML="";
	
	$("#validateProdError").html('');
	$("#validatePriceError").html('');
	$("#validatebatchNoError").html('');
	$("#validatevarietyError").html('');
	$("#validatepacketCodeError").html('');
	$("#validatepacketInfoError").html('');
	if($("#enableBatchNo").val()=='1'){
		var batchNo =document.getElementById("batchNo").value;
	  	$('#batchNo').val('');
	}

	document.getElementById("remarks").innerHTML="";
	//document.getElementById('totalStockQty').selectedIndex = "";
	//document.getElementById('expYr').selectedIndex = "";
	document.getElementById("productUnit").innerHTML="";
	
}

function resetWarehouseData(){
	resetEditFlag();
	if(!productsInfoArray.length==0){

		for(var i=0; i< productsInfoArray.length; i++){
		productsInfoArray.splice(i);
		}
		
}
	reloadTable();	
	document.getElementById('product1').selectedIndex = "";
	document.getElementById('stock').value = "";
	document.getElementById('orderNo').value = "";
	document.getElementById("validateError").innerHTML="";
}

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

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
}

function isNumber(evt) {
	
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


function listSubcategory(call){
	
	
	 $.post("warehouseProduct_populateSubcategory", {subcategory: call}, function (data) {
         var result = data;
         var arry = populateValues(result);
         document.form.subcategory.length = 0;
         addOptionService(document.form.subcategory, "Select", "");

         for (var i = 0; i < arry.length; i++) {
             if (arry[i] !== "")
                 addOptionService(document.form.subcategory, arry[i], arry[i]);
         }
     });
	
	
	
	
	
/* 	var callback = {
	    success: function (oResponse) 	{   
			var result=oResponse.responseText;
			var arry =populateValues(result);
			document.form.subcategory.length = 0;
			addOptionService(document.getElementById("subcategory"), "Select", "");
			
			for (var i=0; i < arry.length;i++){
				if(arry[i]!="")
					addOptionService(document.getElementById("subcategory"), arry[i].trim(), arry[i].trim());
			}
			
			listProduct(document.getElementsByName('selectedSubCategory')[0]);
			//reloadGrid();
			}
	}
    var data = "selectedCategory="+call.value;	 
	var url='warehouseProduct_populateSubcategory';	   
	var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	 */   
   }


function listProduct(obj,preSelVal) {
	$.ajax({
		 type: "POST",
        async: false,
        url: "warehouseProduct_populateProduct.action",
        data: {selectedSubCategoryList:obj.value},
        success: function(result) {
        	insertOptions("product1",$.parseJSON(result));
    		$('#product1').val(preSelVal);	
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
	window.location.href="warehouseProduct_list.action";
	
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}

function loadProductUnit(){
	
	var selectedProduct = document.getElementById("product1").value;
	var selectedWarehouseCode = document.getElementById("warehouse").value;
	var productName=document.getElementById("product1").value;
	var season=document.getElementById("season").value;
	var productName =$("#product1 option:selected").text();
	
	if(selectedProduct==""){
		
	}
	
  if(selectedWarehouseCode==""){
	//document.getElementById("validateError").innerHTML='<s:text name="empty.warehouse"/>';
	
	}else if(selectedProduct==""){
		
	}else if(season==""){
		
	}else{
  
	var availableStock;
	$.post("warehouseProduct_populateProductAvailableStock",{selectedWarehouse:selectedWarehouseCode,selectedProduct:selectedProduct,selectedSeason:season},function(data){
			if(data!=null && data!=""){
			 availableStock=parseInt(data);
			 	
					document.getElementById("productStock").innerHTML = " "+ availableStock;				
			}
			else{
				
				document.getElementById("productStock").innerHTML= 0.00;	
			}
				
	});
}

}

function calTotalQty(call){
	var val1 = (isNaN(parseInt($("#stock").val()))) ? 0 : parseInt($("#stock").val());
	var val2 = (isNaN(parseInt($("#damagQty").val()))) ? 0 : parseInt($("#damagQty").val());
	var val3 = (isNaN(parseInt($("#stockPaise").val()))) ? 0 : parseInt($("#stockPaise").val());
	//var val3 = (isNaN(parseInt($("#stockPaise").val()))) ? 0 : parseInt($("#stockPaise").val());
	var val4 = (isNaN(parseInt($("#damagQtyPaise").val()))) ? 0 : parseInt($("#damagQtyPaise").val());
	var toStock = val1 + val2;
	var toDam = val3 + val4;
	var sum = toStock+"."+toDam;
	document.getElementById("totalStockQty").innerHTML=sum;
}


/* function calTotalAmt(){
	
	var pricePrefix = $("#costPriceRupees").val();
	var priceSuffix = $("#costPricePaise").val();
	var priceValue = pricePrefix+"."+priceSuffix;
	//var priceValue = (isNaN(parseInt($("#costPrice").val()))) ? 0 : parseInt($("#costPrice").val());
	var tstockValue = $("#totalStockQty").text();
	var totAmt = ((priceValue) * (tstockValue));
	//totAmt=Math.round(totAmt);
	document.getElementById("prodAmt").innerHTML=totAmt.toFixed(2);
} */

function calAmtWithTax(){
	
	var totalAmount = document.getElementById("totalAmtVal").innerHTML;
	var taxValue = $("#amtTax").val();
	var finalTaxAmt = (taxValue/100 * totalAmount);
	//var finalTaxAmt=taxValue;
	var finalAmtWithTax = parseFloat(finalTaxAmt) + parseFloat(totalAmount);
	if(taxValue>100)
	{
		document.getElementById('validateTaxError').innerHTML='<s:text name="validateTaxAmt" />';
		document.getElementById("finalTaxAmount").innerHTML="";
		document.getElementById("vendorCreditAmt").innerHTML="";
			
	}
	else if(taxValue!="" && taxValue<100)
	{
		document.getElementById('validateTaxError').innerHTML="";
		document.getElementById("finalTaxAmount").innerHTML=parseFloat(finalAmtWithTax).toFixed(2);
		document.getElementById("vendorCreditAmt").innerHTML=document.getElementById("finalTaxAmount").innerHTML;
	}
	else
	{
		document.getElementById("finalTaxAmount").innerHTML=totalAmount;
		document.getElementById("vendorCreditAmt").innerHTML=document.getElementById("finalTaxAmount").innerHTML;
	}
}

function loadVendorAccBal(){
	
	var selectedVendorValue = $("#vendor option:selected").val();
	var vendorCashBalValue;
	var vendorCreditBalValue;
	$.post("warehouseProduct_populateVendorAccBalance",{selectedVendorValue:selectedVendorValue},function(data){
		if(data!=null && data!=""){
			var dataArr = data.split(",");
			vendorCashBalValue=parseFloat(dataArr[0]).toFixed(2);
			vendorCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
			document.getElementById("vendorCashBal").innerHTML=vendorCashBalValue;
			document.getElementById("vendorCreditBal").innerHTML=vendorCreditBalValue;
			}
	});

}

function loadUnit(){
	
	var selectedProduct = document.getElementById("product1").value;
	var productName=document.getElementById("product1").value;
	
	var availableUnit;
	$.post("warehouseProduct_populatePopulateProductUnit",{selectedProduct:selectedProduct},function(data){
		/* 	if(data!=null && data!='null' && data!=""){
			   // availableUnit=data;
				document.getElementById("productUnit").innerHTML=data;					
		}
			else{
				document.getElementById("productUnit").innerHTML="";	
			}	 */
			
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
				$('#uomGroup').text(value.name);
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

function getElementValueById(id) {
	var val = $("#" + id).val();
	return val;
}


</script>
<script type="text/javascript">

 document.getElementById("warehouse").selectedIndex=0;
 document.getElementById("season").selectedIndex=0;
 document.getElementById("orderNo").value="";
// document.getElementById("").value='<s:property value="currentDate" />';
 document.getElementById("startDate").value='<s:property value="currentDate" />';
 //$('#startDate').datepicker('setDate', new Date());
resetProductData();
jQuery("#warehouse").focus();
document.getElementById("remarks").value="";

</script>
<s:form action="warehouseProduct_populatePrintHTML" id="receiptForm"
	method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
</s:form>
<s:form name="createform" action="warehouseProduct_create">

</s:form>