<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<s:form name="form" cssClass="fillform" enctype="multipart/form-data">
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
					<s:property value="%{getLocaleProperty('info.farmer')}" />
				</h2>
				<div class="flexiWrapper filterControls">
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('procurementCenter')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedProcurementCenter"
								list="listProcurementCenter" headerKey=""
								headerValue="%{getText('txt.select')}" lisykey="key"
								listValue="value" id="selectedProcurementCenter"
								cssClass="col-sm-6 form-control select2" />
						</div>
					</div>
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('group')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select list="listshg" headerKey="0" name="selectedGroup"
								headerValue="%{getText('txt.select')}" id="group"
								cssClass="form-control input-sm select2"
								onchange="listFarmer(this.value);" />
						</div>
					</div>
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('city')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select list="cities" headerKey="" name="selectedCity"
								headerValue="%{getText('txt.select')}" id="cities"
								cssClass="form-control input-sm select2"
								onchange="listVillageByCity(this);" />
						</div>
					</div>
					<div class="flexi flexi10 ">
						<label for="txt"><s:text name="village.name" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select cssClass="form-control input-sm select2"
								name="selectedVillage" list="{}" theme="simple" id="village"
								onchange="listFarmerByVillage(this);" headerKey=""
								headerValue="%{getText('txt.select')}" />
						</div>
					</div>


					<div class="flexi flexi10">
						<label for="txt"> <s:text name="farmerName" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element ">
							<s:select name="selectedFarmer" list="{}" listKey="id"
								listValue="name" headerKey=""
								headerValue="%{getText('txt.select')}" theme="simple"
								id="farmer" cssClass="col-sm-6 form-control select2"
								onchange="listIcsDetail(this.value)" />
						</div>
					</div>


					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouseProduct.date')}" /></label>
						<div class="form-element">
							<div class="form-element">
								<s:textfield name="startDate" id="startDate" readonly="true"
									theme="simple"
									data-date-format="%{getGeneralDateFormat().toLowerCase()}"
									data-date-viewmode="years"
									cssClass="date-picker col-sm-3 form-control" size="20" />

							</div>
						</div>
					</div>
					<div class="flexi flexi10">
						<label for="txt" style="display: block; text-align: center"><s:property
								value="%{getLocaleProperty('cooperative.name')}" /></label>
						<div class="form-element">
							<span id="cooperativeLabel"
								style="display: block; text-align: center"></span>
						</div>
					</div>
					<div class="flexi flexi10">
						<label for="txt" style="display: block; text-align: center"><s:property
								value="%{getLocaleProperty('icsNames')}" /></label>
						<div class="form-element">
							<span id="icsNameLabel"
								style="display: block; text-align: center"></span>
						</div>
					</div>
					<div class="flexi flexi10">
						<label for="txt" style="display: block; text-align: center"><s:property
								value="%{getLocaleProperty('icsStatus')}" /></label>
						<div class="form-element">
							<span id="icsStatusLabel"
								style="display: block; text-align: center"></span>
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
							<s:property value="%{getLocaleProperty('info.crop')}" />
						</h2>
						<div class="flexiWrapper filterControls">

							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('crop')}" /></label>
								<div class="form-element">
									<s:select name="procurementProductId" list="productList"
										headerKey="" headerValue="%{getText('txt.select')}"
										listKey="id" listValue="name" id="procurementProductList"
										cssClass="form-control input-sm select2"
										onchange="listProVarierty(this);listPrice(this);" />
								</div>
							</div>


							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('variety')}" /></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="varietyList"
										cssClass="form-control input-sm select2"
										onchange="listGrade(this.value);" />
								</div>
							</div>
							<div class="flexi flexi12">
								<label for="txt"><s:property
										value="%{getLocaleProperty('procGrade')}" /></label>
								<div class="form-element">
									<s:select list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="gradeList"
										cssClass="form-control input-sm select2" />
								</div>
							</div>

							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('unit')}" /></label>
								<div class="form-element">
									<span id="unitLabel" style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('noOfBags')}" /></label>
								<div class="form-element">
									<s:textfield id="noOfBagsTxt" maxlength="3"
										cssClass="col-sm-3 form-control"
										onkeypress="return isNumber(event)" />
								</div>
							</div>

							<div class="flexi flexi12 ">
								<label for="txt"><s:property
										value="%{getLocaleProperty('quantity')}" />
										<span class="manadatory">*</span> </label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="grossWeightKgTxt" maxlength="7"
											cssStyle="width:140%" onkeypress="return isNumber(event)"
											onchange="calcNetWeight()" cssClass="col-sm-3 form-control" />
										<div>.</div>
										<s:textfield id="grossWeightGmTxt" maxlength="2"
											cssStyle="width:65%" onkeypress="return isNumber(event)"
											cssClass="col-sm-4 form-control" onchange="calcNetWeight()" />
									</div>
								</div>
							</div>

							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('msp')}" /><span class="manadatory">*</span> </label></label>
								<div class="form-element">
									<%-- <span id=""
										style="display: block; text-align: center"></span> --%>
										<s:textfield id="priceLabel" maxlength="4"
											cssStyle="width:65%" onkeypress="return isDecimal(event)"
											cssClass="col-sm-4 form-control" onchange="calcNetWeight()" />
								</div>
							</div>


							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="totalPriceLabel" /></label>
								<div class="form-element">
									<span id="totalPrice"
										style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:property
										value="%{getLocaleProperty('mspWithPremium')}" /></label>
								<div class="form-element">
									<span id="premiumLabel"
										style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center">
									<s:property
										value="%{getLocaleProperty('totalPricePremiumLabel')}" />
								</label>
								<div class="form-element">
									<span id="totalPricePremium"
										style="display: block; text-align: center"></span>
								</div>
							</div>

							<div class="flexi flexi12">
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

						<table class="table table-bordered aspect-detail"
							id="procurementDetailTable"
							style="width: 100%; margin-top: 30px;">
							<thead>
								<tr>
									<th><s:property value="%{getLocaleProperty('crop')}" /></th>
									<th><s:property value="%{getLocaleProperty('variety')}" /></th>
									<th><s:property value="%{getLocaleProperty('grade')}" /></th>
									<th><s:property value="%{getLocaleProperty('unit')}" /></th>
									<th><s:property value="%{getLocaleProperty('noOfBags')}" /></th>
									<th><s:property value="%{getLocaleProperty('quantity')}" /></th>
									<th><s:property value="%{getLocaleProperty('msp')}" /></th>
									<th><s:property
											value="%{getLocaleProperty('totalPriceLabel')}" /></th>
									<th><s:property
											value="%{getLocaleProperty('mspWithPremium')}" /></th>
									<th><s:property
											value="%{getLocaleProperty('totalPricePremiumLabel')}" /></th>
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
									<th style="text-align: right"><s:property
											value="%{getLocaleProperty('total')}" /></th>
									<th style="text-align: right"><div id="noOfBagsTotalLabel" />0.00</th>
									<th style="text-align: right"><div id="quantityTotalLabel">0.000</div></th>
									<th style="text-align: right"><div
											id="pricePerUnitTotalLabel">0</div></th>
									<th style="text-align: right"><div id="totalPriceLabel" />0.00</th>
									<th style="text-align: right"><div id="totalPremiumLabel" />0.00</th>
									<th style="text-align: right"><div
											id="totalPremiumPriceLabel" />0.00</th>
									<th style="text-align: right"></th>
								</tr>

							</tfoot>
						</table>
					</div>
				</div>
				<div class="appContentWrapper marginBottom ">
					<div class="formContainerWrapper">
						<div class="row">
							<div class="container-fluid">
								<div class="notificationBar"></div>
							</div>
						</div>

						<h2>
							<s:property
								value="%{getLocaleProperty('info.stripTestPositive')}" />
						</h2>

						<div class="flexform">
							<div class="flexform-item">
								<label for="txt"><s:text name="stripTesting" /></label>
								<div class="form-element">
									<s:radio id="stripTesting" list="stripList" name="stripTesting"
										listKey="key" listValue="value" theme="simple" />
								</div>
							</div>

							<div class="flexform-item">
								<label for="txt"> <s:property
										value="%{getLocaleProperty('stripTestingImage')}" /> <font
									color="red"><span style="font-size: 8px"><br>
										<s:text name="farmer.imageTypes" /> </span></font>
								</label>
								<div class="form-element">
									<s:file name="stripImage" id="stripImage"
										cssClass="form-control"
										onchange="checkImgHeightAndWidth(this)" />
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="appContentWrapper marginBottom ">
					<div class="formContainerWrapper">
						<div class="row">
							<div class="container-fluid">
								<div class="notificationBar"></div>
							</div>
						</div>

						<h2>
							<s:property
								value="%{getLocaleProperty('info.fullQualityParameters')}" />
						</h2>

						<div class="flexform">
							<div class="flexform-item">
								<label for="txt"><s:text name="trash" /><sup
							style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="trash" name="trash" theme="simple"
										maxlength="3" cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
							<div class="flexform-item">
								<label for="txt"><s:text name="moisture" /><sup
							style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="moisture" name="moisture" theme="simple"
										maxlength="3" cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
							<div class="flexform-item">
								<label for="txt"><s:text name="stapleLengt" /><sup
							style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="stapleLengt" name="stapleLengt" theme="simple"
										maxlength="3" cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
							<div class="flexform-item">
								<label for="txt"><s:text name="kowdiKapas" /><sup
							style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="kowdiKapas" name="kowdiKapas" theme="simple"
										maxlength="3" cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
							<div class="flexform-item">
								<label for="txt"><s:text name="yellowCotton" /><sup
							style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield id="yellowCotton" name="yellowCotton"
										theme="simple" maxlength="3" cssClass="form-control input-sm"
										onkeypress="return isNumber(event)" />
								</div>
							</div>
						</div>
					</div>
				</div>

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
									onkeyup="calBalanceAmt(this.value)"
									style="text-align: right; padding-right: 10px; width: 60% !important;"></td>
								<td><label for="txt"><s:property
											value="%{getLocaleProperty('balanceAmt')}" /></label></td>
								<td><span id="balAmt"
									style="display: block; text-align: center"></span></td>
							</table>
						</div>
					</div>
				</div>

				<div class="yui-skin-sam " id="loadList" style="display: block">
					<sec:authorize
						ifAllGranted="service.procurementTraceability.create">
						<span id="save" class=""><span class="first-child">
								<button type="button" onclick="saveProcurementTraceability();"
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


		<button type="button" id="enableModal"
			class="hide slide_open btn btn-sm btn-success" data-toggle="modal"
			data-target="#slideModal" data-backdrop="static"
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
								value="%{getLocaleProperty('info.procurementTraceability')}" />
						</h4>
					</div>
					<div class="modal-body">
						<b><p style="text-align: center; font-size: 100%;">
								<s:property
									value="%{getLocaleProperty('procurementTraceabilitySucess')}" />
							</p></b>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default"
							onclick="buttonEdcCancel()">
							<s:text name="cancel" />
						</button>
					</div>
				</div>

			</div>

		</div>
	</s:form>
	<script>
	    document.getElementById("startDate").value='<s:property value="currentDate" />';
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		$("#group").val("");
		$("#grossWeightKgTxt").val("");
		$("#grossWeightGmTxt").val("");
		$("#unitLabel").text("");
		$("#priceLabel").val("");
		$("#premiumLabel").text("");
		$("#totalPrice").text("");
		$("#noOfBagsTotalLabel").text("0.0");
		$("#pricePerUnitTotalLabel").text("0.0");
		$("#quantityTotalLabel").text("0.0");
		$("#totalPriceLabel").text("0.0");
		$("#totalPremiumLabel").text("0.0");
		$("#totalPrice").text("");
		$("#totalPricePremium").text("");
		$("#totalPremiumPriceLabel").text("0.0");
		var rowCounter=0;
		var hit=true;
		$(document).ready(function(){
			jQuery("#edit").hide();
		jQuery("#paymentRupee").val("0.00");
		//jQuery("#balAmt").val("0.00");
		document.getElementById("balAmt").innerHTML="0.00";
		
		//var mainCrop=document.getElementById("").innerHTML=value.name;
		
		});
		
		function listFarmer(value) {
			
			 document.getElementById("icsNameLabel").innerHTML="";
			 document.getElementById("icsStatusLabel").innerHTML="";
			 document.getElementById("cooperativeLabel").innerHTML="";
				if (!isEmpty(value)) {
					$.post("procurementTraceability_populateFarmer", {
						selectedGroup : value
					},  function(data) {
						insertOptions("farmer",$.parseJSON(data));
					});
				}
		}

		
		function listIcsDetail(value) {
			 document.getElementById("icsNameLabel").innerHTML="";
			 document.getElementById("icsStatusLabel").innerHTML="";
			 document.getElementById("cooperativeLabel").innerHTML="";
				if (!isEmpty(value)) {
					$.post("procurementTraceability_populateIcs",{selectedFarmer:value},function(data){
						   var jsonData = $.parseJSON(data);
						   $.each(jsonData, function(index, value) {
						   if(value.id=="icsName"){
						   document.getElementById("icsNameLabel").innerHTML=value.name;
						   }
						   if(value.id=="icsStatus"){
						   if(!isEmpty(value.name)&&value.name!=undefined){
							  document.getElementById("icsStatusLabel").innerHTML=value.name;
					     }}
						   if(value.id=="icsCoop"){
							   document.getElementById("cooperativeLabel").innerHTML=value.name;
							   }
						}); 
					});
		}}
		
		function listProVarierty(call) {
			var selectedPro = call.value;
			if(!isEmpty(selectedPro)){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "procurementTraceability_populateVariety.action",
			        data: {selectedProduct : selectedPro},
			        success: function(result) {
			        	insertOptions("varietyList", $.parseJSON(result));
			        }
				});
			}
			listGrade(jQuery("#varietyList").val());
			resetSelect2();
			$("#grossWeightKgTxt").val("");
			$("#grossWeightGmTxt").val("");
			jQuery("#totalPrice").text("");
			jQuery("#noOfBagsTxt").val("");
					
		}
		
		
		function listPrice(call){
			var selectedCrop = call.value;
			$.post("procurementTraceability_populatePricePremium",{selectedCrop:selectedCrop},function(data){
				   var jsonData = $.parseJSON(data);
				   $.each(jsonData, function(index, value) {
				   if(value.id=="unit"){
				   document.getElementById("unitLabel").innerHTML=value.name;
				   }
				   if(value.id=="price"){
					   document.getElementById("priceLabel").value=value.name;
				   }
				   if(value.id=="premium"){
					   document.getElementById("premiumLabel").innerHTML=value.name;
				   }
				}); 
			});
			jQuery("#totalPrice").text("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#totalPricePremium").text("");
			$("#grossWeightKgTxt").val("");
			$("#grossWeightGmTxt").val("");
			resetSelect2();
			}
		
		function listGrade(call) {
			var selectedVariety = call;
			if(!isEmpty(selectedVariety)&&selectedVariety!="0"){
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "procurementTraceability_populateGrade.action",
			        data: {selectedVariety : selectedVariety},
			        success: function(result) {
			        	insertOptions("gradeList", $.parseJSON(result));
			        }
				});
			}
			jQuery("#price").val("");
			jQuery("#totalPrice").text("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#totalPricePremium").text("");
			$("#grossWeightKgTxt").val("");
		}

		
		
		function isNumber(evt) {
		    evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		        return false;
		    }
		    return true;
		}

		function onCancel() {
			window.location.href="procurementTraceability_create.action";    
		}
		
			
		 function isDecimal(evt) {
	    		
	    	 evt = (evt) ? evt : window.event;
	    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	    	        return false;
	    	    }
	    	    return true;
	    }
		 
		 function calcNetWeight(){
				var netWeightKg=jQuery("#grossWeightKgTxt").val();
				var netWeightGm=jQuery("#grossWeightGmTxt").val();
				var bags=jQuery("#noOfBagsTxt").val();
				var price= $("#priceLabel").val();
				if(isEmpty(netWeightKg)){
					netWeightKg = 0;
				}
				if(isEmpty(netWeightGm)){
					netWeightGm = 0;
				}
				if(isEmpty(bags)){
					bags="0";
				}
				
				if(isEmpty(price)){
					price="0";
				}
				price = price.replace (/,/g, "");
				var netWeight=parseInt(netWeightKg)+"."+parseInt(netWeightGm);
				if(parseFloat(netWeight)<=parseFloat(bags)){
					//jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				}
				
				if(netWeight>0){
					var val1 = (isNaN(parseFloat(netWeight)) ? 0 : parseFloat(netWeight));
					var val2 = (isNaN(parseFloat(price)) ? 0 : parseFloat(price));
					var sum = val1 * val2;
					sum=sum.toFixed(2);
					document.getElementById("totalPrice").innerHTML=sum;

				var premium= $("#premiumLabel").text();
			    var value2= (isNaN(parseFloat(premium)) ? 0 : parseFloat(premium));
			    var finalamount=(val1*val2)+(val1*val2*premium/100);
			    finalamount=finalamount.toFixed(2);
			    document.getElementById("totalPricePremium").innerHTML=finalamount;
				}else{
					 document.getElementById("totalPricePremium").innerHTML=0.0;
					 document.getElementById("totalPrice").innerHTML=0.0;
				}
		 }
		/**Add row related Functions*/
		function addRow(){
			
			jQuery("#validateError").text("");
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var unit = jQuery("#unitLabel").text();
			var price = jQuery("#priceLabel").val();
			var premium = jQuery("#premiumLabel").text();
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var bags=jQuery("#noOfBagsTxt").val();
			var totalPrice  = document.getElementById("totalPrice").innerHTML;
			var totalPricePremium  = document.getElementById("totalPricePremium").innerHTML;
			var gradename =jQuery("#gradeList option:selected").text();
			var netWeightCalc="";

			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.crop')}" />');
				return false;
			}
			else if(isEmpty(unit)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />');
				return false;
			}
			else if(isEmpty(price)||price==0.00){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.price')}" />');
				return false;
			}
			else if(isEmpty(premium)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.premium')}" />');
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
			else if(isEmpty(netWeightKg)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.quantity')}" />'); 
				return false;
			}
			else if(netWeightKg==0){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.quantityValidation')}" />'); 
				return false;
			}
			
			else if(isEmpty(netWeightGm)){
				netWeightGm="000";
			}
			
			else if(isEmpty(totalPrice)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.totalPrice')}" />');
				return false;
			}
			else if(isEmpty(totalPricePremium)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.totalPricePremium')}" />');
				return false;
			}
			else if(isEmpty(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.bags')}" />');
				return false;
			}
			
			var netWeight=netWeightKg+"."+netWeightGm;
			
			if(parseFloat(netWeight)<=parseFloat(bags)){
				//jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.grossWeight')}" />');
				//return false;
			}
			if(isEmpty(bags) || bags=="" || bags==null){
				bags="0";
			}
			
			if(!checkGradeExists(grade)){
					var tableRow="<tr id=row"+(++rowCounter)+">";
					tableRow+="<td class='hide productId'>"+product+"</td>";
					tableRow+="<td class='hide varietyId'>"+variety+"</td>";
					tableRow+="<td class='hide gradeId'>"+grade+"</td>";
					tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
					tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
					tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
					tableRow+="<td class='unit textAlignRight'>"+unit+"</td>";
					tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
					tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
					tableRow+="<td class='price textAlignRight'>"+price+"</td>";
					tableRow+="<td class='totalPrice textAlignRight'>"+totalPrice+"</td>";
					tableRow+="<td class='premium textAlignRight'>"+premium+"</td>";
					tableRow+="<td class='totalPricePremium textAlignRight'>"+totalPricePremium+"</td>";
					
				tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
				tableRow+="</tr>";
			    }
				console.log(tableRow);
				jQuery("#procurementDetailContent").append(tableRow);
				resetProductData();
				updateTableFooter();
		}
		
		function editRow(rowCounter){
			var id="#row"+rowCounter;
			$.each(jQuery(id), function(index, value) {
				var selectedProduct = jQuery(this).find(".productId").text();
				var selectedVariety = jQuery(this).find(".varietyId").text();
				var selectedGrade = jQuery(this).find(".gradeId").text();
				var netWeight = jQuery(this).find(".netWeight").text();
				var netWeightSplit = netWeight.split(".");
				jQuery("#procurementProductList").val(selectedProduct);
				var selectedUnit = jQuery(this).find(".unit").text();
				
				callTrigger("procurementProductList");
				jQuery("#varietyList").val(selectedVariety);
				callTrigger("varietyList");
				jQuery("#gradeList").val(selectedGrade);
				callTrigger("gradeList");
				
				jQuery("#unitLabel").text(jQuery(this).find(".unit").text());
				jQuery("#priceLabel").val(jQuery(this).find(".price").text());
				jQuery("#premiumLabel").text(jQuery(this).find(".premium").text());
				jQuery("#quantity").val(jQuery(this).find(".quantity").text());
				jQuery("#totalPrice").text(jQuery(this).find(".totalPrice").text());
				jQuery("#noOfBagsTxt").val(jQuery(this).find(".bags").text());
				jQuery("#totalPricePremium").text(jQuery(this).find(".totalPricePremium").text());
				jQuery("#grossWeightKgTxt").val(netWeightSplit[0]);
				jQuery("#grossWeightGmTxt").val(netWeightSplit[1]);
				calcNetWeight();
				resetSelect2();
			});
			jQuery("#add").hide();
			jQuery("#edit").show();
			$("#edit").attr("onclick","editProduct("+rowCounter+")");
			 
		}
		
		function editProduct(index){

			var id="#row"+index;
			jQuery("#validateError").text("");
			var product = jQuery("#procurementProductList").val();
			var variety = jQuery("#varietyList").val();
			var grade=jQuery("#gradeList").val();			
			var unit = jQuery("#unitLabel").text();
			var price = jQuery("#priceLabel").val();
			
			var premium = jQuery("#premiumLabel").text();
			var netWeightKg=jQuery("#grossWeightKgTxt").val();
			var netWeightGm=jQuery("#grossWeightGmTxt").val();
			var bags=jQuery("#noOfBagsTxt").val();
			var totalPrice  = document.getElementById("totalPrice").innerHTML;
			var totalPricePremium  = document.getElementById("totalPricePremium").innerHTML;
			var gradename =jQuery("#gradeList option:selected").text();
			var netWeightCalc="";
			
			if(isEmpty(product)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.crop')}" />');
				return false;
			}
			else if(isEmpty(unit)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.unit')}" />');
				return false;
			}
			else if(isEmpty(price)||price==0.00){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.price')}" />');
				return false;
			}
			else if(isEmpty(premium)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.premium')}" />');
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
			else if(isEmpty(netWeightKg)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.quantity')}" />'); 
				return false;
			}
			else if(isEmpty(netWeightGm)){
				netWeightGm="000";
			}
			else if(isEmpty(totalPrice)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.totalPrice')}" />');
				return false;
			}
			else if(isEmpty(totalPricePremium)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.totalPricePremium')}" />');
				return false;
			}
			else if(isEmpty(bags)){
				jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.bags')}" />');
				return false;
			}
			
			var netWeight=netWeightKg+"."+netWeightGm;
			
			if(parseFloat(netWeight)<=parseFloat(bags)){
				
				//jQuery("#validateError").text('<s:property value="%{getLocaleProperty('constraint.quality')}" />');
				return false;
			}
			jQuery(id).empty();

			if(!checkGradeExists(grade)){
				var tableRow="";
				tableRow+="<td class='hide productId'>"+product+"</td>";
				tableRow+="<td class='hide varietyId'>"+variety+"</td>";
				tableRow+="<td class='hide gradeId'>"+grade+"</td>";
				tableRow+="<td>"+jQuery("#procurementProductList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#varietyList option:selected").text()+"</td>";
				tableRow+="<td>"+jQuery("#gradeList option:selected").text()+"</td>";
				tableRow+="<td class='unit textAlignRight'>"+unit+"</td>";
				tableRow+="<td class='bags textAlignRight'>"+bags+"</td>";
				tableRow+="<td class='netWeight textAlignRight'>"+netWeight+"</td>";
				tableRow+="<td class='price textAlignRight'>"+price+"</td>";
				tableRow+="<td class='totalPrice textAlignRight'>"+totalPrice+"</td>";
				tableRow+="<td class='premium textAlignRight'>"+premium+"</td>";
				tableRow+="<td class='totalPricePremium textAlignRight'>"+totalPricePremium+"</td>";

			tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteProduct("+rowCounter+")'></td>";
			jQuery(id).append(tableRow);
			resetProductData();
			updateTableFooter();
			jQuery("#add").show();
			jQuery("#edit").hide();
		    }
			
		}
		
		
		function updateTableFooter(){

			var tableBody = jQuery("#procurementDetailContent tr");
			var totalPrice=0.0;
			var totalPriceValue=0.0;
			var totalPremium=0.0;
			var totalPremiumPriceValue=0.0;
			var totalNetWeights=0.0;
			var totalBags=0.0;
			$.each(tableBody, function(index, value) {
				totalBags+=getFormattedFloatValue(jQuery(this).find(".bags").text());
				totalPrice+=getFormattedFloatValue(jQuery(this).find(".price").text());
				totalNetWeights+=getFormattedFloatValue(jQuery(this).find(".netWeight").text());
				totalPriceValue+=getFormattedFloatValue(jQuery(this).find(".totalPrice").text());
				totalPremium+=getFormattedFloatValue(jQuery(this).find(".premium").text());
				totalPremiumPriceValue+=getFormattedFloatValue(jQuery(this).find(".totalPricePremium").text());
			});
			
			$("#noOfBagsTotalLabel").text(totalBags.toFixed(2));
			$("#pricePerUnitTotalLabel").text(totalPrice.toFixed(2));
			$("#quantityTotalLabel").text(totalNetWeights.toFixed(2));
			$("#totalPriceLabel").text(totalPriceValue.toFixed(2));
			$("#totalPremiumLabel").text(totalPremium.toFixed(2));
			$("#totalPremiumPriceLabel").text(totalPremiumPriceValue.toFixed(2));
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
			jQuery("#priceLabel").val("");
			jQuery("#unitLabel").text("");
			jQuery("#premiumLabel").text("");
			jQuery("#totalPrice").text("");
			jQuery("#totalPricePremium").text("");
			jQuery("#grossWeightKgTxt").val("");
			jQuery("#grossWeightGmTxt").val("");
			jQuery("#noOfBagsTxt").val("");
			jQuery("#paymentRupee").val("0.00");
			resetSelect2();
			jQuery("#add").show();
			jQuery("#edit").hide();
		}
		function checkGradeExists(gradeId){
			    var returnVal = false;
				var tableBody = jQuery("#procurementDetailContent tr");
				$.each(tableBody, function(index, value) {
				var grade = jQuery(this).find(".gradeId").text();
				
				if(grade==gradeId){
				alert('<s:property value="%{getLocaleProperty('product.alreadyExists')}" />');
				resetProductData();
			    returnVal=true 
				}
				});
			return returnVal;	
		}
		
		function callTrigger(id){
			$("#"+id).trigger("change");
		}
		
		function deleteProduct(rowCounter){
			var id="#row"+rowCounter;
			jQuery(id).remove();
			updateTableFooter();
		}
		
		function checkImgHeightAndWidth(val) {

			var _URL = window.URL || window.webkitURL;
			var img;
			var file = document.getElementById('stripImage').files[0];
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
			var file=document.getElementById('stripImage').files[0];
			var filename=document.getElementById('stripImage').value;
			var fileExt=filename.split('.').pop();
			hit=true;
			if(file != undefined){
				if(fileExt=='jpg' || fileExt=='jpeg' || fileExt=='png'||fileExt=='JPG'||fileExt=='JPEG'||fileExt=='PNG')
				{ 	
					hit=true;
				}else{
					//alert('<s:text name="invalidFileExtension"/>')	
					hit=false;
					}
			}
		return hit;
		}
		
		function saveProcurementTraceability(){
				
				$("#sucessbtn").prop('disabled', true);
				var group=jQuery("#group").val();
				var farmer=jQuery("#farmer").val();
				var icsName = jQuery("#icsNameLabel").text();
				var icsStatus = jQuery("#icsStatusLabel").text();
				var icsCoop = jQuery("#cooperativeLabel").text();
				var procurementCenter=jQuery("#selectedProcurementCenter").val();
				var date=jQuery("#startDate").val();
				var selectedStripTestType =$('input[type=radio][name=stripTesting]:checked').val();
			    var file=document.getElementById('stripImage').files[0];
				var filename=document.getElementById('stripImage').value;
				var trashValue = jQuery("#trash").val();
				var moistureValue = jQuery("#moisture").val();
				var stableValue = jQuery("#stapleLengt").val();
				var kowdiKapasValue = jQuery("#kowdiKapas").val();
				var yellowCottonValue = jQuery("#yellowCotton").val();
				var error = "";
				var paymentRupee=0.00;
				
				var procurementProductInfoArray =  buildProcurementProductInfoArray();
				if(isEmpty(procurementProductInfoArray)){
					error = '<s:property value="%{getLocaleProperty('noRecordFound')}" />';
				}

			    if(isEmpty(group)){
					error ='<s:property value="%{getLocaleProperty('empty.shg')}" />';
					jQuery("#group").focus();
				}else if(isEmpty(farmer)){
					error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';
					jQuery("#farmer").focus();
				}else if(isEmpty(procurementCenter)){
					error = '<s:property value="%{getLocaleProperty('empty.procurementCenter')}" />';
					jQuery("#selectedProcurementCenter").focus();
				}else if(isEmpty(icsName)){
					error = '<s:property value="%{getLocaleProperty('empty.icsName')}" />';
				}
				else if(isEmpty(trashValue)){
					error = '<s:property value="%{getLocaleProperty('empty.trash')}" />';
					jQuery("#trash").focus();
				}
				else if(isEmpty(moistureValue)){
					error = '<s:property value="%{getLocaleProperty('empty.moistureValue')}" />';
					jQuery("#moisture").focus();
				}
				else if(isEmpty(stableValue)){
					error = '<s:property value="%{getLocaleProperty('empty.stableValue')}" />';
					jQuery("#stapleLengt").focus();
				}
				else if(isEmpty(kowdiKapasValue)){
					error = '<s:property value="%{getLocaleProperty('empty.kowdiKapasValue')}" />';
					jQuery("#kowdiKapas").focus();
				}
				else if(isEmpty(yellowCottonValue)){
					error = '<s:property value="%{getLocaleProperty('empty.yellowCottonValue')}" />';
					jQuery("#yellowCotton").focus();
				}
			    if(!isEmpty(jQuery("#paymentRupee").val())){
					paymentRupee = jQuery("#paymentRupee").val(); 
				}
			    
				var paymentAmount=paymentRupee;
				
				if(!isEmpty(error)){
					jQuery("#validateError").text(error);
					$("#sucessbtn").prop('disabled', false);
					return false;
				}
				 
				 validateImage();
				 if(hit == false){
					 jQuery("#validateError").text("please select valid image");
					 document.getElementById('stripImage').value="";
					 $("#sucessbtn").prop('disabled', false);
					 return false;
				}
				else{			
				var totalAmount=jQuery("#totalPriceLabel").text();
				var finalAmount=jQuery("#totalPremiumPriceLabel").text();
				var formdata = new FormData();  
				formdata.append("stripImage", file);
				formdata.append("selectedGroup", group); 
				formdata.append("selectedDate",date); 
				formdata.append("procurementDate",date.toString()); 
				formdata.append("selectedFarmer",farmer); 
				formdata.append("selectedProcurementCenter",procurementCenter); 
				formdata.append("productTotalString",procurementProductInfoArray); 
				formdata.append("selectedStripTestType",selectedStripTestType); 
				formdata.append("selectedIcsName",icsName); 
				formdata.append("selectedIcsStatus",icsStatus); 
				formdata.append("selectedTrashValue",trashValue); 
				formdata.append("selectedMoistureValue",moistureValue); 
				formdata.append("selectedStableValue",stableValue); 
				formdata.append("selectedKowdiKapasValue",kowdiKapasValue); 
				formdata.append("selectedYellowCottonValue",yellowCottonValue); 
				formdata.append("totalAmount",totalAmount); 
				formdata.append("finalAmount",finalAmount); 
				formdata.append("paymentAmount",paymentAmount);
				$.ajax({
				        url: 'procurementTraceability_populateProcurement.action',
				        type: 'post',
				        dataType: 'json',
				        data: formdata,
				        enctype: 'multipart/form-data',
				        cache: false,
				        processData: false,
				        contentType: false,
				        success: function(data) {
				        	document.getElementById("enableModal").click();	
				        },
				        error: function(data) {
				            alert("Some Error Occured , Please Try again");
				            $("#myButton").removeAttr("disabled");
				        }

				    }); 
				}
			}
		
function buildProcurementProductInfoArray(){
			
			var tableBody = jQuery("#procurementDetailContent tr");
			var productInfo="";
			$.each(tableBody, function(index, value) {
				productInfo+=jQuery(this).find(".productId").text(); //0
				productInfo+="#"+jQuery(this).find(".varietyId").text(); //1
				productInfo+="#"+jQuery(this).find(".gradeId").text(); //2
				productInfo+="#"+jQuery(this).find(".bags").text();
				productInfo+="#"+jQuery(this).find(".unit").text(); //3
				productInfo+="#"+jQuery(this).find(".price").text(); //4
				productInfo+="#"+jQuery(this).find(".netWeight").text();
				productInfo+="#"+jQuery(this).find(".premium").text();
				productInfo+="#"+jQuery(this).find(".totalPricePremium").text();
				productInfo+="#"+jQuery(this).find(".totalPrice").text()+"@";//6
			});
			return productInfo;
		}
		
function buttonEdcCancel(){
	document.getElementById("model-close-edu-btn").click();	
	window.location.href="procurementTraceability_create.action";
 }
 
function calBalanceAmt(labl){	

	var pay=parseFloat(labl);
	var totAmt=parseFloat($('#totalPremiumPriceLabel').text());
	var finalAmount=jQuery("#totalPremiumPriceLabel").text();
	
	if(!isNaN(parseFloat(pay))&&finalAmount>0.00 ){	
	var bal=totAmt-pay;
	}else{	
		var bal=0.00;
	}
	
	document.getElementById("balAmt").innerHTML=parseFloat(bal);
	
}
function listVillageByCity(obj){
	 document.getElementById("icsNameLabel").innerHTML="";
	 document.getElementById("icsStatusLabel").innerHTML="";
	 document.getElementById("cooperativeLabel").innerHTML="";
		var idsArray=['group','farmer'];
		resetSelect2(idsArray);

	var selectedCity = $('#cities').val();
	jQuery.post("procurementTraceability_populateVillageByCity.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
		insertOptions("village",$.parseJSON(result));
		//listsamithi(document.getElementById("village"));
	});
}
 
function listFarmerByVillage(call){
	var selectedGroup="";
	 $.post("procurementTraceability_populateFarmer", {selectedVillage:call.value,selectedGroup:selectedGroup}, function (data) {
		 insertOptions("farmer",$.parseJSON(data));
				
		});
}

function resetSelect2(idArray)
{
	for(var i=0;i<idArray.length;i++)
	{
		var id="#"+idArray[i];
		$(""+id+" > option").removeAttr("selected");
       	$(""+id+"").trigger("change");
	}
   
}

function resetSelect2(){
	$(".select2").select2();
}
	

	</script>
</body>