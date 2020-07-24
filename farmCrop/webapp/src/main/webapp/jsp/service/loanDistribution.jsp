<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">

</head>
<style>
tr.noBorder>td, td.noBorder {
	border-style: hidden;
	height: 40px;
}
</style>
<body>
	<s:form name="form" cssClass="fillform">

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
					<s:property value="%{getLocaleProperty('Loan Distribution')}" />
				</h2>
				<div class="flexiWrapper filterControls">


					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('Loan Date')}" /> <span
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
								value="%{getLocaleProperty('Vendor')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="loanTo" list="listVendor" headerKey="" headerValue="%{getText('txt.select')}"
								listKey="key" listValue="value" id="vendor"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="flexi flexi10 city hide">
						<label for="txt"><s:property
								value="%{getLocaleProperty('city.name')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="cityId" list="listMunicipality" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" onchange="listVillage(this.value);" id="city"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="flexi flexi10 village hide">
						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="village"
								cssClass="form-control input-sm select2" name="village"
								onchange="listFarmer(this.value)" />
						</div>
					</div>

					<div class="flexi flexi10 regFarmer ">

						<label for="txt"><s:property
								value="%{getLocaleProperty('farmerName')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select list="farmerList" onchange="populateGroup(this);" headerKey=""
								headerValue="%{getText('txt.select')}" name="farmerId"
								id="farmer" cssClass="form-control input-sm select2" />
						</div>

					</div>

					<div class="flexi flexi10 ">
						<label for="txt" style="display: block; text-align: center"><s:property
								value="%{getLocaleProperty('samithi')}" /> </label>
						<div class="form-element">
							<span id="selAgentGroup"
								style="display: block; text-align: center" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div>
			<div class="service-content-section">
				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
							<s:property value="%{getLocaleProperty('Product Details')}" />
						</h2>
						<div class="flexiWrapper filterControls">

							<div class="flexi flexi10 category">
								<label for="txt"><s:property
										value="%{getLocaleProperty('distributionBalance.subCategory')}" />
									<span class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="categoryId" list="categoryList" headerKey=""
										headerValue="%{getText('txt.select')}" id="categoryList"
										cssClass="form-control input-sm select2"
										onchange="listProduct(this.value)" />
								</div>
							</div>
							<div class="flexi flexi10 product">
								<label for="txt"><s:property
										value="%{getLocaleProperty('product')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="product" id="product" list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" onchange="listCostPrice(this)"
										cssClass="form-control input-sm select2" />
								</div>
							</div>

							<div class="flexi flexi10 pricePerUnit">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('pricePerUnit')}" /><span
									class="manadatory">*</span></label>
								<%-- <div class="form-element">
									<s:textfield id="pricePerUnitLabel" maxlength="15"
										cssClass="form-control input-sm"
										onkeypress="return isDecimal(event)" />
								</div> --%>
								<div class="form-element">
									<span id="pricePerUnitLabel" style="display: block; text-align: center" />
								</div>
							</div>

							<div class="flexi flexi10 quantity">
								<label for="txt"><s:property
										value="%{getLocaleProperty('quantity')}" /> <span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:textfield id="quantity" maxlength="15"
										cssClass="form-control input-sm"
										onkeypress="return isDecimal(event)" />
								</div>
							</div>
							<div class="flexi flexi10 totalAmt">
								<label for="txt"><s:property
										value="%{getLocaleProperty('totalAmt')}" /></label>
								<div class="form-element">
									<s:textfield id="totalAmt" maxlength="15"
										cssClass="form-control input-sm"
										onkeypress="return isDecimal(event)" />
								</div>
							</div>

							<div class="flexi flexi10 action">
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
						<div>
							<h2>
								<s:text name="distributedProducts" />
							</h2>
							<div class="flexiWrapper filterControls">
								<table class="table table-bordered aspect-detail"
									id="procurementDetailTable" style="width: 100%;">
									<thead>
										<tr>

											<th style="text-align: center" class="product"><s:property
													value="%{getLocaleProperty('product')}" /></th>
											<th style="text-align: center" class="pricePerUnit"><s:property
													value="%{getLocaleProperty('pricePerUnit')}" /></th>
											<th style="text-align: center" class="quantity"><s:property
													value="%{getLocaleProperty('quantity')}" /></th>
											<%-- <th style="text-align: center" class="subsidyPercentage"><s:property value="%{getLocaleProperty('subsidyPercentage')}" /></th> --%>
											<th style="text-align: center" class="totalAmtLabel"><s:property
													value="%{getLocaleProperty('totalAmt')}" /></th>
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
											<th style="text-align: right"></th>
											<th
												style="text-align: right; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;"><div
													id="subTotalLabel">0.00</div></th>
											<th style="text-align: right"></th>

											<%-- <th><s:property value="%{getCurrencyType().toUpperCase()}" /></th> --%>

										</tr>
										<tr class="noBorder" style="height: 40px;">

										</tr>
										<tr class='hide'>
											<td></td>
											<td style="text-align: right"><s:property
													value="%{getLocaleProperty('subsidyPercentage')}" /></td>
											<td><input name="subsidyQty" value="" id="subsidyQty"
												placeholder="Enter Subsidy % Here"
												style="text-align: right; padding-right: 1px; width: 190px !important; float: right"
												onkeypress="return isDecimal(event)"
												onkeyup="calculateCostToFarmer(this.value);" type="text"></td>
											<td><input name="subsidyAmt" value="" id="subsidyAmt"
												placeholder="Enter Amount Here"
												style="text-align: right; padding-right: 1px; width: 190px !important; float: right"
												type="text" readonly></td>
											<td></td>
										</tr>

										<tr class='hide'>
											<td></td>
											
											<td style="text-align: right"><s:property
													value="%{getLocaleProperty('costToFarmer')}" /></td>
											<td></td>
											<td style="text-align: right"><span id="costToFarmer" />0.00</td>
											<td><s:property
													value="%{getCurrencyType().toUpperCase()}" /></td>
											<td></td>
										</tr>

										<tr class='hide'>
											<td></td>
											
											<td style="text-align: right"><s:property
													value="%{getLocaleProperty('downPayment')}" /></td>
											<td><input name="downPaymentQty" value=""
												id="downPaymentQty" placeholder="Enter Quantity Here"
												style="text-align: right; padding-right: 1px; width: 190px !important; float: right"
												onkeypress="return isDecimal(event)"
												onkeyup="calculateDownPaymentQty(this.value);" type="text"></td>
											<td><input name="downPaymentAmt" value=""
												id="downPaymentAmt" placeholder="Enter Amount Here"
												style="text-align: right; padding-right: 1px; width: 190px !important; float: right"
												onkeypress="return isDecimal(event)"
												onkeyup="calculateDownPaymentAmt(this.value);" type="text"></td>
											<td></td>
										</tr>

										<tr class='hide'>
											<td></td>
											
											<td style="text-align: right"><s:property
													value="%{getLocaleProperty('currentQtyAndAmnt')}" /></td>
											<td style="text-align: right"><span id="currentQty" />0.00
											</td>
											<td style="text-align: right"><span id="currentAmt" />0.00</td>
											<td><s:property
													value="%{getCurrencyType().toUpperCase()}" /></td>
											<td></td>
										</tr>
<tr>
								<td></td>
								
								<td style="text-align: right"><s:property value="%{getLocaleProperty('interestPercentage')}" /><span class="manadatory">*</span></td>
								<td><input name="interestPercentageQty" value="" id="interestPercentageQty" placeholder="Enter Interest % Here" style="text-align:right;padding-right:1px; width:190px!important;float:right" onkeypress="return isDecimal(event)" onkeyup="calculateInterest(this.value);" type="text"></td>
								<td><input name="interestPercentageAmt" placeholder="Enter Amount Here"  value="" id="interestPercentageAmt" style="text-align:right;padding-right:1px; width:190px!important;float:right" type="text" readonly></td>
								<td><s:property value="%{getCurrencyType().toUpperCase()}" /></td>	
								</tr>		
								
								<tr>
								<td></td>
								
								<td style="text-align: right"><s:property value="%{getLocaleProperty('loanTenureLabel')}" /><span class="manadatory">*</span></td>								
								<td style="text-align: right"><input name="loanTenure" placeholder="Enter Year Here" value="" id="loanTenure" style="text-align:right;padding-right:1px; width:190px!important;float:right" onkeypress="return isNumber(event)" type="text" onkeyup="calculateLoanTenure(this.value);"></td>
								<td><input name="loanTenureAmt" placeholder="Enter Amount Here"  value="" id="loanTenureAmt" style="text-align:right;padding-right:1px; width:190px!important;float:right" type="text" readonly></td>
								<td><s:property value="%{getCurrencyType().toUpperCase()}" /></td>	
								</tr>
								
								<tr>
								<td></td>
								
								<td style="text-align: right"><s:property value="%{getLocaleProperty('totalCostToFarmer')}" /></td>
								<td></td>
								<td style="text-align: right;border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;"><span id="totalCostToFarmer" /> 0.00</td><td><s:property value="%{getCurrencyType().toUpperCase()}" />	</td>						
								
								</tr>
								
								<tr>
								<td></td>
								
								<td style="text-align: right"><s:property value="%{getLocaleProperty('monthlyLoanRepay')}" /></td>
								<td></td>								
								<td style="text-align: right"><span id="monthlyLoanRepay" />0.00</td><td><s:property value="%{getCurrencyType().toUpperCase()}" />	</td>						
								</tr>
								

									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</div>


				<div class="yui-skin-sam " id="loadList">
					<sec:authorize
						ifAllGranted="service.loanDistributionService.create">
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
							<s:property value="%{getLocaleProperty('info.loanDistribution')}" />
						</h4>
					</div>
					<div class="modal-body">
						<b><p style="text-align: center; font-size: 120%;">
								<s:property
									value="%{getLocaleProperty('loanDistributionSucess')}" />
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
		var tenantId="<s:property value='getCurrentTenantId()'/>";
	   var currency="<s:property value='getCurrency()'/>";
		$(document).ready(function(){
			
			
			var test='<s:property value="%{getGeneralDateFormat()}" />';
			 $.fn.datepicker.defaults.format = test.toLowerCase();
			$('#date').datepicker('setDate', new Date());
			$('#loanDistributionStatus0').attr('checked', true);
			jQuery("#edit").hide();
			jQuery(".regFarmer").show();
			jQuery(".city").show();
			jQuery(".village").show();			
			jQuery(".group").hide();
			jQuery(".totalAmt").hide();
			

		});
		
		
		
		function listVillage(value) {
			
			$("#farmer").empty();			
			$("#farmer").append("<option value=''>Select</option>");	
			callTrigger("farmer");
			if (!isEmpty(value)) {
				
				$.post("loanDistribution_populateVillage", {
					selectedCity : value
				}, function(data) {
					
					insertOptions("village", $.parseJSON(data));
				});
				
			
			}
		}
function listProduct(value) {
			
	jQuery("#quantity").val("");	
			if (!isEmpty(value)) {
				
				$.post("loanDistribution_populateProduct", {
					selectedCategory : value
				}, function(data) {
					
					insertOptions("product", $.parseJSON(data));
				});
				
			
			}
		}

		 function listFarmer(value) {

			if (!isEmpty(value)) {
				$.post("loanDistribution_populateFarmer", {
					selectedVillage : value
				}, function(data) {
					insertOptions("farmer",$.parseJSON(data));
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
		
		
		/**Product related Functions*/
		function addRow(){
			/**Validation Part*/
				jQuery("#validateError").text("");
			var subsidyPercentage=0.00;
			var error = "";
			var tempTotal=0.00;
			var tempTotalAmt=0.00;
			var totalAmt=0.00;
			var loanProduct=jQuery("#product").val();	
			var loanProductName=jQuery("#product option:selected").text();		
			var ratePerPrice = jQuery("#pricePerUnitLabel").text();
			var quantity=jQuery("#quantity").val();
			var approvSeedLabel=jQuery("#approvedSeedlingLabel").text();
			quantity = Number(quantity);
			if(isEmpty(loanProduct)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.loanProduct')}" />');
				return false;
			}else if(isEmpty(ratePerPrice) || ratePerPrice==0){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.ratePerPrice')}" />'); 
				return false;
			}else if(isEmpty(quantity) || quantity==0){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.quantity')}" />'); 
				return false;
			}
			if(!isEmpty(quantity) || quantity!=0){
				tempTotal = (parseFloat(ratePerPrice)*parseFloat(quantity)).toFixed(1)		
					subsidyPercentage=0.00;
					tempTotalAmt=(parseFloat(tempTotal) * parseFloat(0.00))/100;
				totalAmt = tempTotal - tempTotalAmt;
			}
			
			if(!checkLoanProductExists(loanProduct)){
				var tableRow="<tr id=row"+(++rowCounter)+">";
				tableRow+="<td class='hide loanProductCode'>"+loanProduct+"</td>";				
				tableRow+="<td style='text-align:center;' class='loanProductName'>"+loanProductName+"</td>";				
				tableRow+="<td style='text-align:right;' class='ratePerPrice'>"+ratePerPrice+"</td>";
				tableRow+="<td style='text-align:right;' class='quantity'>"+quantity+"</td>";
				tableRow+="<td class='hide amount'>"+(parseFloat(ratePerPrice)*parseFloat(quantity)).toFixed(1)+"</td>";
				//tableRow+="<td style='text-align:right;' class='subsidyPercent'>"+subsidyPercentage+"</td>";
				//tableRow+="<td class='hide subsidyAmt'>"+tempTotalAmt.toFixed(1)+"</td>";
				tableRow+="<td style='text-align:right;' class='totalAmount'>"+totalAmt.toFixed(1)+"</td>";
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
				var selectedLoanProduct=jQuery(this).find(".loanProductCode").text();			
				var selectedRatePerPrice=jQuery(this).find(".ratePerPrice").text();	
				var selectedQuantity=jQuery(this).find(".quantity").text();		
				//var selectedSubsidy=jQuery(this).find(".subsidyPercent").text();		
				jQuery("#product").val(selectedLoanProduct);
				callTrigger("product");
				
				jQuery("#pricePerUnitLabel").val(selectedRatePerPrice);
				jQuery("#quantity").val(selectedQuantity);
				//jQuery("#subsidyPercentage").val(selectedSubsidy);
				resetSelect2();
			});
			jQuery("#add").hide();
			jQuery("#edit").show();
			$("#edit").attr("onclick","editProduct("+rowCounter+")");
			 
		}
		
		function editProduct(index){
			var tempTotal=0.00;
			var tempTotalAmt=0.00;
			var totalAmt=0.00;
			var id="#row"+index;
			jQuery(id).empty();
			
			jQuery("#validateError").text("");			
			var error = "";						
			var loanProduct=jQuery("#product").val();	
			var loanProductName=jQuery("#product option:selected").text();		
			var ratePerPrice = jQuery("#pricePerUnitLabel").text();
			var quantity=jQuery("#quantity").val();
			//var subsidyPercentage=jQuery("#subsidyPercentage").val();
					
			if(isEmpty(loanProduct)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.loanProduct')}" />');
				return false;
			}else if(isEmpty(ratePerPrice) || ratePerPrice==0){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.ratePerPrice')}" />'); 
				return false;
			}
		 	if(isEmpty(quantity)){
		 		quantity="0";
			}
		 	tempTotal = (parseFloat(ratePerPrice)*parseFloat(quantity)).toFixed(1)		
			/* if(!subsidyPercentage=="" || !subsidyPercentage==null){
				tempTotalAmt=(parseFloat(tempTotal) * parseFloat(subsidyPercentage))/100;
			} */
		 	totalAmt = tempTotal - tempTotalAmt;
//alert(ratePerPrice);
			if(!checkLoanProductExists(loanProduct)){
				var tableRow="";
				tableRow+="<td class='hide loanProductCode'>"+loanProduct+"</td>";				
				tableRow+="<td style='text-align:center;' class='loanProductName'>"+loanProductName+"</td>";				
				tableRow+="<td style='text-align:right;' class='ratePerPrice'>"+ratePerPrice+"</td>";
				tableRow+="<td style='text-align:right;' class='quantity'>"+quantity+"</td>";
				tableRow+="<td class='hide amount'>"+(parseFloat(ratePerPrice)*parseFloat(quantity)).toFixed(1)+"</td>";
				//tableRow+="<td style='text-align:right;' class='subsidyPercent'>"+subsidyPercentage+"</td>";
				//tableRow+="<td class='hide subsidyAmt'>"+tempTotalAmt.toFixed(1)+"</td>";
				tableRow+="<td style='text-align:right;' class='totalAmount'>"+totalAmt.toFixed(1)+"</td>";
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
		
		function checkLoanProductExists(value1){
			var returnVal = false;
		
				var tableBody = jQuery("#procurementDetailContent tr");
				
				$.each(tableBody, function(index, value) {
					var loanProductName = jQuery(this).find(".loanProductCode").text();
					
					 if(loanProductName==value1){
						alert('<s:property value="%{getLocaleProperty('loanProduct.alreadyExists')}" />');
						returnVal=true 
					}
				});
			
			return returnVal;	
		}
		
		function updateTableFooter(){
			var tableBody = jQuery("#procurementDetailContent tr");
			var totalPrice=0.0;
			var totalQuantity=0.0;			
			var totalAmount=0.0;
			//var totalSubsidy=0.0;
			//var loanTenureAmt=0.00;
			var totalLoanAmt=0.00;
			var interestPercentage=0.00;
			var loanyear=0;
			var totalCost=0.00
			$.each(tableBody, function(index, value) {
				totalPrice+=getFormattedFloatValue(jQuery(this).find(".ratePerPrice").text());
				totalQuantity+=getFormattedFloatValue(jQuery(this).find(".quantity").text());			
				totalAmount+=getFormattedFloatValue(jQuery(this).find(".totalAmount").text());
				//totalSubsidy+=getFormattedFloatValue(jQuery(this).find(".subsidyPercent").text());
			});
			
				totalLoanAmt=totalAmount.toFixed(1);
			$("#pricePerUnitTotalLabel").text(totalPrice.toFixed(1));
			$("#noOfBagsTotalLabel").text(totalQuantity.toFixed(1));		
			$("#subTotalLabel").text(totalAmount.toFixed(1));			
			$("#costToFarmer").text(totalAmount.toFixed(1));
			$("#totalCostToFarmer").text(totalLoanAmt);
			//$("#subsidyPercent").text(totalSubsidy.toFixed(1));
			$("#currentQty").text(totalQuantity.toFixed(1));
			$("#currentAmt").text(totalAmount.toFixed(1));	
			
		}
		
		function getFormattedFloatValue(n){
			if(!isNaN(n)){
				return parseFloat(n);
			}else{
				return parseFloat(0);
			}
		}
		
		function resetProductData(){
			
			jQuery("#categoryId").val("");	
			jQuery("#product").val("");			
			jQuery("#pricePerUnitLabel").text("");			
			jQuery("#quantity").val("");	
			resetSelect2();			
			jQuery("#add").show();
			jQuery("#edit").hide();
		}
		function resetTableData(){					
			jQuery("#pricePerUnitLabel").val("");	
			jQuery("#quantity").val("");		
		}
		
		function saveProcurement(){
		
		jQuery("#validateError").text("");
		var dataa ="";
		var error = "";	
		var cityId;
		var villageId;
		var farmerId;
		var productId;
		var vendorId;
		var costToFarmer=0.00;
		var currentQty=0.00;
		var currentAmt=0.00;
		var downPaymentQty=0.00;
		var downPaymentAmt=0.00;
		var interestPercentage=0.00;
		var interestAmt=0.00;
		var totalCost=0.00;
		var loanTenure=0;
		var loanTenureAmt=0.00;
		var monthyRepayment=0.00;
			var date=jQuery("#date").val();	
			var loanTo=jQuery("#loanTo").val();	
			
			vendorId=jQuery('#vendor').val();
				cityId=jQuery('#city').val();
			/* 	if(isEmpty(cityId)){
					error = '<s:property value="%{getLocaleProperty('empty.city')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#city").focus();	
					return false;
				} */
				villageId=jQuery('#village').val();
			/* 	if(isEmpty(villageId)){
					error = '<s:property value="%{getLocaleProperty('empty.village')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#village").focus();	
					return false;
				} */
				
				farmerId=jQuery('#farmer').val();	
					if(isEmpty(farmerId)){
						error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#farmer").focus();	
						return false;
					}
			if(isEmpty(date)){
				error = '<s:property value="%{getLocaleProperty('empty.date')}" />';
				jQuery("#date").focus();
			}
			var loanCategory =jQuery("#loanCategory").val();
			var productInfoArray =  buildProductInfoArray();
			if(isEmpty(productInfoArray)){
				error = '<s:property value="%{getLocaleProperty('noRecordFound')}" />';
			}
			costToFarmer=jQuery('#costToFarmer').text();
			downPaymentQty=jQuery('#downPaymentQty').val();
			downPaymentAmt=jQuery('#downPaymentAmt').val();
			currentQty=jQuery('#currentQty').text();
			currentAmt=jQuery('#currentAmt').text();			
			interestPercentage=jQuery('#interestPercentageQty').val();
			interestAmt=jQuery('#interestPercentageAmt').val();			
			loanTenure=jQuery('#loanTenure').val();	
			loanTenureAmt=jQuery('#loanTenureAmt').val();
			totalCost=jQuery('#totalCostToFarmer').text();
			monthyRepayment=jQuery('#monthlyLoanRepay').text();
			
			if(isEmpty(interestPercentage)){

				error = '<s:property value="%{getLocaleProperty('error.interestPercentage')}" />';	
				jQuery("#validateError").text(error);
				jQuery("#interestPercentageQty").focus();	
				return false;
			}
			if(isEmpty(loanTenure)){
				
				error = '<s:property value="%{getLocaleProperty('error.loanTenure')}" />';	
				jQuery("#validateError").text(error);
				jQuery("#loanTenure").focus();	
				return false;
			}
			
			
			if(!isEmpty(error)){
				jQuery("#validateError").text(error);
				$("#sucessbtn").prop('disabled', false);
				return false;
			}
			
			dataa = {
						selectedDate:date,
						selectedLoanTo:loanTo,
						selectedVillage:villageId,
						selectedFarmer:farmerId,
						selectedCProduct:productId,
						productTotalString:productInfoArray,						
						costToFarmer:costToFarmer,						
						currentQty:currentQty,
						selectedVendor:vendorId,
						totalCost:totalCost,
						interestPercentage:interestPercentage,
						interestAmt:interestAmt,						
						loanTenure:loanTenure,
						loanTenureAmt:loanTenureAmt,
						monthyRepayment:monthyRepayment
					}
		
		
			$("#sucessbtn").attr("disabled", true);
			$.ajax({
			        url: 'loanDistribution_populateLoanDistribution.action',
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
		
		
		
		function buildProductInfoArray(){
			
			var tableBody = jQuery("#procurementDetailContent tr");
			var productInfo="";
			
			$.each(tableBody, function(index, value) {
				
				productInfo+=jQuery(this).find(".loanProductCode").text(); //0
				
				productInfo+="#"+jQuery(this).find(".ratePerPrice").text(); //1
				
				productInfo+="#"+jQuery(this).find(".quantity").text(); //2
				
				productInfo+="#"+jQuery(this).find(".amount").text(); //3
				
				productInfo+="#"+jQuery(this).find(".totalAmount").text()+"@"; //4
				
							
			});
			//alert(productInfo);
			return productInfo;
		}
		
		function resetSelect2(){
			$(".select2").select2();
		}
		
		
			
		
		function calculateCostToFarmer(val){
			//alert(val);
			var totalAmt = 0;
			var subsidyQty=0;
			var tempTotal=0.00;
			if(!val==""){
			subsidyQty=val;		
			}
			totalAmt = $('#subTotalLabel').text();			
			
			tempTotal=(parseFloat(totalAmt) * parseFloat(subsidyQty))/100;
			
			var total = totalAmt - tempTotal;
			
			$("#costToFarmer").text(total.toFixed(1));
			$("#totalCostToFarmer").text(total.toFixed(1));
		}
		function calculateDownPaymentAmt(val){
			var downpaymentAmt=0.00;
			var tempTotalCost=0.00;
			var totalCost=0.00;
			if(!val==""){
				downpaymentAmt=val;
			}
			tempTotalCost=$("#costToFarmer").text();
			if(parseFloat(tempTotalCost)<=parseFloat(downpaymentAmt)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('error.downpaymentAmt')}" />');
				$("#currentAmt").text(totalCost);
				$("#totalCostToFarmer").text(totalCost);
			}else{
				totalCost=parseFloat(tempTotalCost) - parseFloat(downpaymentAmt);
				$("#currentAmt").text(totalCost.toFixed(1));
				$("#totalCostToFarmer").text(totalCost.toFixed(1));
			}
		}
		
		function calculateDownPaymentQty(val){
			var downpaymentQty=0.00;
			var tempTotalQty=0.00;
			var totalQty=0.00;
			if(!val==""){
				downpaymentQty=val;
			}
			tempTotalQty=$("#noOfBagsTotalLabel").text();
			
			if(parseFloat(tempTotalQty)<=parseFloat(downpaymentQty)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('error.downpaymentQty')}" />');
				$("#currentQty").text(totalQty);
			}else{
				totalQty=parseFloat(tempTotalQty) - parseFloat(downpaymentQty);
				
				$("#currentQty").text(totalQty.toFixed(1));
			}
		}
		function calculateInterest(val){
		//	alert(val);
			var totalAmt = 0;
			var interestQty=0.00;
			var tempTotal=0.00;
			var tempCost=0.00;
			var totalCostAmt=0.00;
			var loanTenure=0;
			var tempLoanTenureAmt=0.00;
			var totalCostOfFarmer=0.00;
			var tempTotalCostOfFarmer=0.00;
			var monthlyRepay=0.00;
			var loanTenureAmt=0.00;
			loanTenure=$("#loanTenure").val();
			tempTotalCostOfFarmer=$('#subTotalLabel').text();
			if(val==""){
				
				$("#interestPercentageAmt").val(interestQty.toFixed(1));	
				if(!isEmpty(loanTenure)){
					
					tempLoanTenureAmt=parseFloat(interestQty) *loanTenure;
					$("#loanTenureAmt").val(tempLoanTenureAmt.toFixed(1));
				}
				
				totalCostOfFarmer=parseFloat(tempTotalCostOfFarmer) + parseFloat(tempLoanTenureAmt)
				monthlyRepay = parseFloat(totalCostOfFarmer) / loanTenure;
				$('#totalCostToFarmer').text(totalCostOfFarmer.toFixed(1));
				$('#monthlyLoanRepay').text(monthlyRepay.toFixed(1));
			}else{
				interestQty=val;	
			totalAmt = $('#currentAmt').text();			
			
			tempTotal=(parseFloat(totalAmt) * parseFloat(interestQty))/100;
			
			var total = totalAmt - tempTotal;
			$("#interestPercentageAmt").val(tempTotal.toFixed(1));	
			if(!isEmpty(loanTenure)){
				//alert("AA");
			
				loanTenureAmt=parseFloat(tempTotal.toFixed(1)) * loanTenure;
				totalCostOfFarmer=parseFloat(tempTotalCostOfFarmer) + parseFloat(loanTenureAmt)
				monthlyRepay = parseFloat(totalCostOfFarmer) / loanTenure
				$("#loanTenureAmt").val(loanTenureAmt.toFixed(1));
				$('#totalCostToFarmer').text(totalCostOfFarmer.toFixed(1));
				$('#monthlyLoanRepay').text(monthlyRepay.toFixed(1));
			}else{
				//alert("BB");
				totalCostAmt = parseFloat(totalAmt) + parseFloat(tempTotal)
				$("#totalCostToFarmer").text(totalCostAmt.toFixed(1));
			}
			
			}
		}
		function calculateLoanTenure(val){
			var loanTenure=val;
			var tempTotalCostOfFarmer=0.00;
			var tempInterestPercentageAmt=0.00;
			var loanTenureAmt=0.00;
			var totalCostOfFarmer=0.00;
			var monthlyRepay=0.00;
			tempInterestPercentageAmt = $('#interestPercentageAmt').val();
			tempTotalCostOfFarmer=$('#subTotalLabel').text();
			
			
			if(loanTenure==0 || loanTenure==""){				
			
				//jQuery("#validateError").text('<s:property value="%{getLocaleProperty('error.loanTenure')}" />');
				$("#loanTenureAmt").val(loanTenureAmt.toFixed(1));
				totalCostOfFarmer=parseFloat(tempTotalCostOfFarmer) + parseFloat(tempInterestPercentageAmt);				
				$('#totalCostToFarmer').text(totalCostOfFarmer.toFixed(1));				
				$('#monthlyLoanRepay').text(monthlyRepay.toFixed(1));
			}else{
				
				loanTenureAmt=parseFloat(tempInterestPercentageAmt) * loanTenure;
				totalCostOfFarmer=parseFloat(tempTotalCostOfFarmer) + parseFloat(loanTenureAmt)
				monthlyRepay = parseFloat(totalCostOfFarmer) / loanTenure
				$("#loanTenureAmt").val(loanTenureAmt.toFixed(1));
				$('#totalCostToFarmer').text(totalCostOfFarmer.toFixed(1));
				$('#monthlyLoanRepay').text(monthlyRepay.toFixed(1));
			}
			
		
		}
		
		function loadFarmerGroup(value){
		if(value=='2'){
			$(".village").show();
			$(".city").show();
			$(".regFarmer").hide();
			$(".group").show();
			}else{
				$(".village").show();
				$(".city").show();
				$(".regFarmer").show();
				$(".group").hide();
			}
		
		
		}
		function loadLoanCateroty(val){
			if(val=='1'){
				$(".product").show();
				$(".pricePerUnit").show();
				$(".quantity").show();
				//$(".totalAmt").hide();
				//$(".subTotal").show();
			}else{
				$(".product").hide();
				$(".pricePerUnit").hide();
				$(".quantity").hide();
				//$(".totalAmt").show();
				//$(".subTotal").hide();
				
			}
		}
		
		function buttonEdcCancel(){
			//refreshPopup();
			document.getElementById("model-close-edu-btn").click();	
	
	    	 window.location.href="loanDistribution_create.action";
	    	
	     }
		
	
		
		function onCancel() {
			
		    	 window.location.href="loanDistribution_list.action";
		    	
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
			    	var procurementProductInfoArray =  buildProductInfoArray();			    	
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
 
 function farmerLoanYear(val){
	 $("#loanYearLabel").text("");
	 var selectedFarmer=val;
	 if (!isEmpty(selectedFarmer)) {
			$.post("loanDistribution_populateFarmerLoanYear", {
				selectedFarmer : selectedFarmer
			}, function(data) {
				//insertOptions("farmer",$.parseJSON(data));
				$('#loanYearLabel').text($.parseJSON(data));
			});
		}
	
 }
 
 function groupLoanYear(val){
	 var selectedFarmer=val;
	 if (!isEmpty(selectedFarmer)) {
			$.post("loanDistribution_populateGroupLoanYear", {
				selectedFarmer : selectedFarmer
			}, function(data) {
				//insertOptions("farmer",$.parseJSON(data));
				$('#loanYearLabel').text($.parseJSON(data));
			});
		}
	
 }
 
 function farmerapprovedSeed(val){
	 $("#approvedSeedlingLabel").text("");
	 var selectedFarmer=val;
	 if (!isEmpty(selectedFarmer)) {
			$.post("loanDistribution_populateFarmerapprovedSeed", {
				selectedFarmer : selectedFarmer
			}, function(data) {
				//insertOptions("farmer",$.parseJSON(data));
				$('#approvedSeedlingLabel').text($.parseJSON(data));
			});
		}
	
 }
 
 function groupapprovedSeed(val){
	 $("#approvedSeedlingLabel").text("");
	 var selectedFarmer=val;
	 if (!isEmpty(selectedFarmer)) {
			$.post("loanDistribution_populateGroupapprovedSeed", {
				selectedFarmer : selectedFarmer
			}, function(data) {
				//insertOptions("farmer",$.parseJSON(data));
				$('#approvedSeedlingLabel').text($.parseJSON(data));
			});
		}
	
 }
 
 function populateGroup(){

		var selectedFarmer = document.getElementById("farmer").value;
		var availableGroup;
		$.post("loanDistribution_populateGroup",{selectedFarmer:selectedFarmer},function(data){
				if(data!=null && data!=""){
					document.getElementById("selAgentGroup").innerHTML=data;				
				}
				else{
					document.getElementById("selAgentGroup").innerHTML="";
				}
		});	   
	}
 
 function listCostPrice(){

		var selectedProduct = document.getElementById("product").value;

		$.post("loanDistribution_populateCostPrice",{selectedProduct:selectedProduct},function(data){
				if(data!=null && data!=""){
					//alert("aaa");
					document.getElementById("pricePerUnitLabel").innerHTML=data;
					//document.getElementById("selllingRupee").value=data;
			}
				else{
					document.getElementById("pricePerUnitLabel").innerHTML=0;	
					//document.getElementById("selllingRupee").value=0;	
				}	

			//	alert("---------->"+document.getElementById("costPrice").innerHTML);
		});

	}

	</script>
</body>