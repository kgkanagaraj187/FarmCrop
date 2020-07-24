<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">


</head>

<s:form name="form" cssClass="fillform"  >
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" name="cropSupply.id" />
	<s:hidden key="command" />
	<s:hidden key="temp" id="temp" />

	<s:hidden key="productTotalString" id="productTotalString" />

	<div class="appContentWrapper marginBottom ">
		<div class="formContainerWrapper">
			<div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<div class="error pull-left">
							<p class="notification">
								<%-- <sup>*</sup>
								<s:text name="reqd.field" /> --%>
								&nbsp;&nbsp;
							<div id="validateError" style="text-align: center;"></div>
							</P>
						</div>
						<div class="pull-right">
							<b><s:text name="season" /> : </b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>
					</div>
				</div>
			</div>
			<h2>
				<s:text name="info.cropSale" />
			</h2>
			<div class="flxboxer filterControls">

				<div class="flexi flexi10">
					<label for="txt"><s:text name="dateOfSale" /> <span
						class="manadatory">*</span></label>

					<div class="form-element">
						<s:textfield name="startDate" id="startDate" readonly="true"
							theme="simple"
							data-date-format="%{getGeneralDateFormat().toLowerCase()}"
							data-date-viewmode="years" cssClass="date-picker form-control" />


					</div>
				</div>
				<div class="flexi flexi10">
					<label for="txt"> <s:property
							value="%{getLocaleProperty('village.name')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element ">
						<s:select id="village" name="selectedVillage" headerKey=""
									headerValue="%{getText('txt.select')}" list="villageList"
									cssClass="form-control select2" theme="simple"
									onchange="listFarmers(this);" disabled="false" />
					</div>
				</div>


				<div class="flexi flexi10">
					<label for="txt"><s:property value="%{getLocaleProperty('farmerName')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element ">
						<s:select name="selectedFarmer" list="farmers" listKey="id"
									listValue="name" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									id="farmer" cssClass="form-control select2"
									onchange="listFarms(this)" />
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"> <s:text name="farmName" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element ">
						<s:select name="selectedFarm" list="farms" listKey="id"
							listValue="name" headerKey=""
							headerValue="%{getText('txt.select')}" theme="simple" id="farm"
							cssClass="form-control select2" />
					</div>
				</div>



				<div class="flexi flexi10">
					<label for="txt"><s:text name="buyerName" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedBuyer" id="selectedBuyer"
							list="buyersList" headerKey=""
							headerValue="%{getText('txt.select')}" theme="simple"
							cssClass="form-control select2"
							onchange="loadBuyerAccBal();" />

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
						<s:text name="info.cropSaleDetails" />
					</h2>
					<div class="flexiWrapper filterControls">
					<div class="flexi flexi10" style="width: 148px;">
					<label for="txt"><s:text name="cropType" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedCropType"
											cssClass="form-control select2" list="cropTypeList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="cropType" onchange="listCropType(this);"
											/>

					</div>
				</div>
					<div class="flexi flexi10" style="width: 148px;">
					<label for="txt"><s:text name="cropName" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedCropName"
											cssClass="form-control select2" list="cropNamesList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="cropName"
											onchange="listCrops(this);resetDatas();loadUnit();" />

					</div>
				</div>
						
						

						<div class="flexi flexi10" style="width: 148px;">
							<label for="txt"> <s:text name="variety" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedVariety"
											cssClass="form-control select2" list="varietysList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="variety" onchange="listVariety(this.value)" />
							</div>
						</div>
						
						
						<div class="flexi flexi10" style="width: 148px;">
							<label for="txt"> <s:text name="grade" /><sup style="color: red;">*</sup></label>
							<div class="form-element">
							<s:select name="selectedGrade" list="gradesList" headerKey=""
											headerValue="%{getText('txt.select')}" id="grade"
											cssClass="form-control  select2" />
							</div>
						</div>
						
						<div class="flexi flexi10" style="width: 75px;">
							<label for="txt"><s:text name="unit" /></label>
							<div class="form-element">
							<div id="productUnit" style="text-align: center;"></div>
							</div>
						</div>
						
						<div class="flexi flexi10" style="width: 80px;">
							<label for="txt"><s:text name="prices" /><sup style="color: red;">*</sup></label>
							<div class="form-element">
							<s:textfield id="sellPrice"
											name="sellPrice" size="30" maxlength="7"
											onkeypress="return isNumber(event)"
											onkeyup="calTotalQty(this);" disabled="false"
											cssStyle="text-align:right;padding-right:1px; width:70px!important;" />

										<div id="validatePriceError"
											style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:property
										value="%{getLocaleProperty('quantity.name')}" /><sup
									style="color: red;">*</sup></label>
							<div class="form-element">
							<s:textfield id="quantity"
											name="quantity" maxlength="8"
											onkeypress="return isDecimal(event)"
											onkeyup="calTotalQty(this);" disabled="false"
											cssStyle="text-align:right;padding-right:1px; width:90px!important;" />
										<div id="validateProdQtyError"
											style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:text name="batch" /></label>
							<div class="form-element">
							<s:textfield name="batchNo" id="batchNo" maxlength="20"
											theme="simple" />
							</div>
						</div>
						
						
						<div class="flexi flexi10">
							<label for="txt"><s:text name="totalPrice" /></label>
							<div class="form-element">
							<div id="totalPrice"></div>
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:text name="action" /></label>
							<div class="form-element">
							<button type="button" class="btn btn-sm btn-success"
														aria-hidden="true" onclick="addRow();"
														title="<s:text name="Ok" />">
														<i class="fa fa-check"></i>
													</button>


													<button type="button" class="btn btn-sm btn-danger"
														aria-hidden="true " onclick="resetProductData()"
														title="<s:text name="Cancel" />">
														<i class="glyphicon glyphicon-remove-sign"></i>
													</button>
							</div>
						</div>

					</div>
				</div>
			</div>

		</div>
	</div>
	

			<div class="appContentWrapper marginBottom">

				<div class="formContainerWrapper">

					<%-- <table id="productInfoTbl"
						class="table table-bordered aspect-detail">

						<thead>
							<tr class="odd">
								<th  width="2%"><s:text name="sno" /></th>

								<th width="30%"><s:text name="grade" /><sup style="color: red;">*</sup></th>
								<th width="8%"><s:text name="unit" /></th>
								<th width="15%"><s:text name="prices" /><sup style="color: red;">*</sup></th>
								<th width="10%"><s:property
										value="%{getLocaleProperty('quantity.name')}" /><sup
									style="color: red;">*</sup></th>
								<th width="10%"><s:text name="batch" /></th>
								<th width="10%"><s:text name="totalPrice" /></th>
								<th width="15%"><s:text name="action" /></th>
							</tr></thead>

							<tr>
									<td></td>
									<td style="text-align: center; padding-right: -5px !important;">
										<s:select name="selectedGrade" list="gradesList" headerKey=""
											headerValue="%{getText('txt.select')}" id="grade"
											cssClass="form-control  select2" />
									</td>

									<td><div id="productUnit"></div></td>

									<td class="alignCenter"><s:textfield id="sellPrice"
											name="sellPrice" size="30" maxlength="7"
											onkeypress="return isNumber(event)"
											onkeyup="calTotalQty(this);" disabled="false"
											cssStyle="text-align:right;padding-right:1px; width:70px!important;" />

										<div id="validatePriceError"
											style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

									</td>

									<td class="alignCenter"><s:textfield id="quantity"
											name="quantity" maxlength="8"
											onkeypress="return isDecimal(event)"
											onkeyup="calTotalQty(this);" disabled="false"
											cssStyle="text-align:right;padding-right:1px; width:90px!important;" />
										<div id="validateProdQtyError"
											style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
									</td>
									<td><s:textfield name="batchNo" id="batchNo" maxlength="20"
											theme="simple" /></td>
									<td class="alignRight"
										style="text-align: center; padding-right: 10px !important;">
										<div id="totalPrice"></div>
									</td>
									<td colspan="3" class="alignCenter"
										style="padding: 5px -2px 3px 0px !important;">
										<table cellpadding="0" cellspacing="0"
											class="actionBtnWarpper">
											<tr>
												<td class="textAlignCenter">
													<button type="button" class="btn btn-sm btn-success"
														aria-hidden="true" onclick="addRow();"
														title="<s:text name="Ok" />">
														<i class="fa fa-check"></i>
													</button>


													<button type="button" class="btn btn-sm btn-danger"
														aria-hidden="true " onclick="resetProductData()"
														title="<s:text name="Cancel" />">
														<i class="glyphicon glyphicon-remove-sign"></i>
													</button>
												</td>
											</tr>
										</table>
									</td>
								</tr>

									</table> --%>
							
						<!-- 		<tfoot>
				<tr>
				<td colspan="6"  style="border: none !important;">&nbsp;</td>
				<td style="border: none !important;">
					Total Qty-
				</td>
				<td style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;">0.00</td>
				<td style="border: none !important;">
					Total Price
				</td>
				<td style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;">0.00</td>
				
			</tr>
		</tfoot> -->
					<table id="productInfoTbl1"
							class="table table-bordered aspect-detail">
							<thead>
								<tr class="odd">
									<th><s:text name="sno" /></th>
									<th><s:text name="cropType" /></th>
									<th><s:text name="cropName" /></th>
									<th><s:text name="variety" /></th>
									<th><s:text name="grade" /></th>
									<th><s:text name="unit" /></th>
									<th><s:property value="%{getLocaleProperty('prices')}" /></th>
									<%--  <th><s:text name="quantity" /><sup style="color: red;">*</sup></th> --%>
									<th><s:property
											value="%{getLocaleProperty('quantity.name')}" /></th>
									<th><s:text name="batch" /></th>
									<th><s:text
											name="%{getLocaleProperty('totalPrice')}" /></th>
									<th><s:text name="action" /></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="12" style="text-align: center;"><s:text
											name="noRecordFound" /></td>
								</tr>
							</tbody>
						</table>
				</div>
				</div>
				

			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>
						<s:text name="info.corpTrans" />
					</h2>


					<table id="productInfoTbl"
						class="table table-bordered aspect-detail">

						<thead>
							<tr class="odd">
								<th><s:text name="transporterName" /></th>
								<th><s:text name="vechileNo" /></th>
								<th><s:text name="receipt" /></th>

							</tr>
							<tr>
								<td class="alignCenter"><s:textfield name="transportName"
										id="transportName" theme="simple" /></td>
								<td class="alignCenter"><s:textfield name="vechicleNo"
										id="vechicleNo" theme="simple" /></td>
								<td class="alignCenter"><s:textfield name="invoiceNo"
										id="invoiceNo" theme="simple" /></td>
							</tr>

						</thead>
					</table>
                 </div>
                 </div>
                 
                 
                 <div class="appContentWrapper marginBottom ">
				<div class="formContainerWrapper">
					<h2>
						<s:text name="info.payment" />
					</h2>


					<table id="vendorInfoTbl"
						class="table table-bordered aspect-detail">

						<thead>
									<tr class="odd">
										<th width="10%"><s:text
												name="%{getLocaleProperty('totalSaleQty')}" /></th>
										<th width="10%"><s:text name="%{getLocaleProperty('totalSale')}" /></th>
										<th width="25%"><s:text name="buyerAccBal" /></th>
										<th width="15%"><s:text name="payment.type" /></th>
										<th width="20%" class="cashTypeDivLabel"><s:text
												name="enterAmt" /><sup
						style="color: red;">*</sup></th>
										<th width="20%" class="creditTypeDivLabel"><s:text
												name="creditText" /></th>
										<%-- <th width="25%"><s:text name="payment" /></th> --%>
									</tr>

									<tr>
										<td><div id="totalSaleQty"></div></td>
										<td><div id="totalSalePrice"></div></td>
										<td>
											<table class="table table-bordered aspect-detail">
												<tr>
													<td><s:text name="cashType1" /> :</td>
													<td id="buyerCashBal" style="font-weight: bold;"></td>
													<td><s:text name="cashType2" /> :</td>
													<td id="buyerCreditBal" style="font-weight: bold;"></td>
												</tr>
											</table>
										</td>

										<td><s:radio list="cashType" listKey="key"
												listValue="value" name="amtType" id="cashCred"
												theme="simple" onchange="hideDivBasedOnCashType(this);" /></td>

										<td class="cashTypeDiv">
											<%-- <s:textfield id="buyerCashAmtRupees" name="cashAmtRupee" maxlength="10"
								onkeypress="return isNumber(event)" cssStyle="text-align:right;padding-right:1px; width:50px!important;"/>
								 .<s:textfield id="buyerCashAmtPaise" name="cashAmtPaise" maxlength="2"
								 onkeypress="return isNumber(event)" cssStyle="text-align:right;padding-right:1px; width:50px!important;"/> --%>
											<s:textfield id="payment" name="payment" maxlength="10"
												onkeypress="return isDecimal(event)"
												cssStyle="text-align:right;padding-right:1px; width:75px!important;" />
										</td>

										<td class="creditTypeDiv">
											<div id="buyerCreditAmt"></div>
										</td>

										<%-- <td>
			   		<s:textfield id="payment" name="payment" maxlength="10" 
					onkeypress="return isNumber(event)" cssStyle="text-align:right;padding-right:1px; width:75px!important;"/>
			 	</td> --%>


									</tr>

								</thead>



					</table>
				</div>
				
				
				
		
			</div>
			<s:hidden id="productList" name="warehouseProductList" />
</s:form>
			
	<s:form name="form" cssClass="fillform" action="cropSaleEntryReport_createImage"
		method="post" enctype="multipart/form-data" id="target">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="csId" name="id" />
	 <div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>
						<s:text name="image" />
					</h2>
							<tr>
							<div class="flexform-item">
							<s:file name="cropSaleImage" id="cropSaleImage"
								 cssClass="form-control" />
								</div>
								<div class="flexform-item">
							<s:file name="cropSaleImage1" id="cropSaleImage1"
								 cssClass="form-control" />
								</div>
							</tr>
                 </div>
                 </div>
</s:form>		

<div class="yui-skin-sam" id="loadList" style="display: block">
					<sec:authorize
						ifAllGranted="service.report.cropSaleEntryReport.create">
						<span id="savebutton" class=""> <span class="first-child">
								<button id="submitButton" type="button"
									class="save-btn btn btn-success" onclick="submitCropSale();">
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
	

			
<s:form name="listForm" id="listForm" action="cropSaleEntryReport_list">
	<s:hidden name="currentPage" />
</s:form>
<!-- 
<button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button> -->
	
	<button type="button" data-toggle="modal" data-target="#slideModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>
	

<%-- <div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
					<s:text name="saleCreate" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="divMsg" align="center"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button>
			</div>
		</div>

	</div>
</div> --%>

<div id="slideModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="contentHdr">
				<button type="button" class="close" data-dismiss="modal">&times;</button>			
			<div class="modal-body">
				<i class="fa fa-check" aria-hidden="true"></i></br>
            <span><h5><s:text name="cropsaletMsg" /></h5></span>
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

<s:form name="redirectform" action="cropSaleEntryReport_list"
	method="POST">
</s:form>

<script type="text/javascript">

var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();

$("#village").val("");
$("#selectedBuyer").val("");
$("#transportName").val("");
$("#vechicleNo").val("");
$("#invoiceNo").val("");

jQuery(document).ready(function(){	
	$("#sellPrice").inputmask({"alias": "currency","prefix":"","min":"0","max":"99999.99"});
	if('<s:property value="command" />'=="update") {
		var productData = '<s:property value="productTotalString" />'.split("|");
		
		for(var j=0; j < productData.length; j++){
			var productDataArray = productData[j].split("#");
			addEditRow(productDataArray[0], productDataArray[1]);
		}
	}

	<s:if test='stockDescription!=null'>
	jQuery('<tr><td><s:property value="stockDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
	jQuery("#restartAlert").css('height','180px');
</s:if>

	hideDivBasedOnCashType('');
});

function hideDivBasedOnCashType(evt){
	var cashVal = $(evt).val();
	//alert(cashVal);
	if(cashVal==0){
		
		jQuery(".cashTypeDivLabel").show();
		jQuery(".cashTypeDiv").show();
		//jQuery("#payment").val("0");
		//jQuery("#buyerCashAmtRupees").val("0");
		//jQuery("#buyerCashAmtPaise").val("00");
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
		document.getElementById("buyerCreditAmt").innerHTML="";
	
	}
	else if(cashVal==1){
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").show();
		jQuery(".creditTypeDiv").show();
		//jQuery("#payment").val("0");
		//jQuery("#buyerCashAmtRupees").val("0");
		//jQuery("#buyerCashAmtPaise").val("00");
        document.getElementById("buyerCreditAmt").innerHTML=document.getElementById("totalSalePrice").innerHTML;   // totalPrice
	}
	else {
		jQuery(".cashTypeDivLabel").hide();
		jQuery(".cashTypeDiv").hide();
		jQuery(".creditTypeDivLabel").hide();
		jQuery(".creditTypeDiv").hide();
	
		$('input:radio[name=amtType]').prop('checked',false);
		document.getElementById("buyerCreditAmt").innerHTML="";
		
	}
}

function loadBuyerAccBal(){
	
	var selectedBuyerValue = $("#selectedBuyer option:selected").val();
	var buyerCashBalValue;
	var buyerCreditBalValue;
	$.post("cropSaleEntryReport_populateBuyerAccBalance",{selectedBuyerValue:selectedBuyerValue},function(data){
		if(data!=null && data!=""){
			var dataArr = data.split(",");
			buyerCashBalValue=parseFloat(dataArr[0]).toFixed(2);
			buyerCreditBalValue = parseFloat(dataArr[1]).toFixed(2);
			document.getElementById("buyerCashBal").innerHTML=buyerCashBalValue;
			document.getElementById("buyerCreditBal").innerHTML=buyerCreditBalValue;
			}
	});
}


function resetUnitData(){
	document.getElementById("validateError").innerHTML="";

}




function reload() {
	addRow();
	resetProductData();
}

function resetData(){
	//document.getElementById("buyerName").selectedIndex="";
	
	document.getElementById("cropType").selectedIndex="";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("cropName").selectedIndex="";
	document.getElementById("variety").selectedIndex="";
	document.getElementById("grade").selectedIndex="";
	document.getElementById("sellPrice").value="";
	document.getElementById('quantity').value = "";
	document.getElementById('batchNo').value = "";
	document.getElementById('totalPrice').innerHTML = "";
	productsInfoArray = new Array();
	pArrayList = new Array();
	productTotalArray=new Array();
	productNameArray=new Array();
	reloadTable();
}
	

function addRow(){
	var editIndex = getEditIndex();
	var selectedVillage = $('#village').val();
	var selectedFarmer = $('#farmer').val();
	var selectedFarm = $('#farm').val();
	var selectedBuyer=$("#selectedBuyer").val();
	var selectedCropType = $("#cropType").val();
	var selectedCropName=$("#cropName").val();
	var selectedVariety = $("#variety").val();
	var selectedGrade = $("#grade").val();
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	var sellPrice=document.getElementById("sellPrice").value;
	sellPrice=sellPrice.replace (/,/g, "");	
	var quantity = document.getElementById("quantity").value;
	var batchNo = document.getElementById("batchNo").value;
	var cropTypeName =$("#cropType option:selected").text();
	var cropNames =$("#cropName option:selected").text();
	var varietyName =$("#variety option:selected").text();
	var gradeName =$("#grade option:selected").text();
	/* var coPricePrefix = document.getElementById("costPriceRupees").value;
	var coPriceSuffix = document.getElementById("costPricePaise").value;
	var costPricePreSuf =  coPricePrefix +"."+coPriceSuffix; */
    var currentDate = new Date();
    var currentMonth = currentDate.getMonth()+1;
    var currentYear = currentDate.getFullYear();
   
    //var cooperative = $("#cooperative").val();
    //alert("cooperative:"+cooperative);
    if(selectedVillage == "" || selectedVillage=="-1")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.village"/>';
		return false;
	}
	
     if(selectedFarmer == "" || selectedFarmer=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.farmer"/>';
		return false;
	}
	
     if(selectedFarm == "" || selectedFarm=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.farm"/>';
		return false;
	}
	
     if(selectedBuyer =="" || selectedBuyer=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.buyer"/>';
		return false;
	}
	
     if(selectedCropType =="" )
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.cropType"/>';
		return false;
	}
	
     if(selectedCropName == "" || selectedCropName=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.crop"/>';
		return false;
	}
	
     if(selectedVariety =="" || selectedVariety=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.variety"/>';
		return false;
	}
	
     if(selectedGrade =="" || selectedGrade=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.grade"/>';
		return false;
	}
	
     if(sellPrice == "" || sellPrice == null)
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.price"/>';
		return false;
	}
	
     if(quantity == "" || quantity == null)
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.qty"/>';
		return false;
	}
     
     
     for(var cnt=0;cnt<productsInfoArray.length;cnt++)
     {
    	 
    	  if(productsInfoArray[cnt].cropNames==cropNames && productsInfoArray[cnt].varietyName==varietyName && productsInfoArray[cnt].gradeName==gradeName
    			  && productsInfoArray[cnt].isEdit==false)
    	  {
    		  document.getElementById("validateError").innerHTML='<s:text name="alreadyExs"/>';
    			return false;
    	   }
    	 
     }
     
   
    
	
	//alert("quantity"+quantity);
	var totalQty=document.getElementById('totalSaleQty').innerHTML;
	if(totalQty=='')
	{
		totalQty=0;
	}
	
	var totQty=parseFloat(quantity)+parseFloat(totalQty);
	document.getElementById('totalSaleQty').innerHTML=totQty;
	var totalPrice=document.getElementById('totalSalePrice').innerHTML;
	if(totalPrice=='')
	{
		totalPrice=0;
	}
	var totPrice=parseFloat(quantity*sellPrice).toFixed(2);
	var totalPrice=parseFloat(totalPrice)+parseFloat(totPrice);
	document.getElementById('totalSalePrice').innerHTML=totalPrice;
	var Qty,QtyPerUnit;
	var productExists=false;	
	var batchExists=false;
	var productArray=null;
	if(editIndex==-1){
				 productArray = {
						 cropType:selectedCropType,
						 cropName:selectedCropName,
						 variety:selectedVariety,
						 grade:selectedGrade,
						 cropTypeName:cropTypeName,
						 cropNames:cropNames,
						 varietyName:varietyName,
						 gradeName:gradeName,
						 unitName:proUnitVal,
						 quantity :quantity,
						 sellPrice : sellPrice,
						 farm:selectedFarm,
						 batchNo:batchNo,
						 tAmt : parseFloat(quantity*sellPrice).toFixed(2),
						 isEdit:false
			         };
				 
				 
		
		

				 if(!productExists)
				 productsInfoArray[productsInfoArray.length] = productArray;
		 
				} else {
					productArray=productsInfoArray[editIndex];
				    editproductTotalArray(productArray);
				    productsInfoArray[editIndex].cropType= selectedCropType;
					productsInfoArray[editIndex].cropName= selectedCropName;
					productsInfoArray[editIndex].variety= selectedVariety;
					productsInfoArray[editIndex].grade= selectedGrade;
					
					productsInfoArray[editIndex].sellPrice = sellPrice;
					productsInfoArray[editIndex].quantity = quantity;
					productsInfoArray[editIndex].batchNo = batchNo;
					productsInfoArray[editIndex].tAmt = parseFloat(quantity*sellPrice).toFixed(2);
					productsInfoArray[editIndex].isEdit=false;
					productArray=productsInfoArray[editIndex];
					
				}
	reloadTable();
	resetProductData();
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
		productsInfoArray[editIndex].isEdit=false;
		productArray=productsInfoArray[editIndex];
	}
	
	reloadTable();
	resetProductData();

}


function editRow(indx) {
	

 $("#cropType option[value='"+productsInfoArray[indx].cropType+"']").prop("selected","selected");
 $("#cropType").trigger("change");

 $("#cropName option[value='"+productsInfoArray[indx].cropName+"']").prop("selected","selected");
 $("#cropName").trigger("change");
 
 $("#variety option[value='"+productsInfoArray[indx].variety+"']").prop("selected","selected");
 $("#variety").trigger("change");
 
  $("#grade option[value='"+productsInfoArray[indx].grade+"']").prop("selected","selected");
 $("#grade").trigger("change");
 
	document.getElementById("sellPrice").value=productsInfoArray[indx].sellPrice;
		//document.getElementById("costPrice").value=productsInfoArray[indx].price;
		document.getElementById('quantity').value  = productsInfoArray[indx].quantity;
		document.getElementById('batchNo').value  = productsInfoArray[indx].batchNo;
		document.getElementById('totalPrice').innerHTML  = productsInfoArray[indx].tAmt;
		
		if(productsInfoArray.length==1){     // To check if the product is the last product.
		document.getElementById('totalSalePrice').innerHTML="";	
		document.getElementById('totalSaleQty').innerHTML="";	
		}
		else {
			document.getElementById('totalSalePrice').innerHTML=document.getElementById('totalSalePrice').innerHTML-productsInfoArray[indx].tAmt;	
			document.getElementById('totalSaleQty').innerHTML=document.getElementById('totalSaleQty').innerHTML-productsInfoArray[indx].quantity;	
				
		}
		
		//alert(productsInfoArray[indx].unit+"###"+productsInfoArray[indx].expMonth+"###"+productsInfoArray[indx].expYear);
		resetEditFlag();
		productsInfoArray[indx].isEdit=true;
	//	calulateAvailableStock();
		reloadTable();
		
}

/* function calulateAvailableStock(){	
	var selectedProduct = document.getElementById('product1').value;
	var stock = document.getElementById('stock').value;
} */
function reloadTable(){
	var tbodyRow = "";
	var tfootArray = new Array();	
	jQuery('#productInfoTbl1 > tbody').html('');
	
	
	var rowCount=0;
	for(var cnt=0; cnt<productsInfoArray.length; cnt++){
			
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].cropTypeName+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].cropNames+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].varietyName+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].gradeName+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].unitName+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].sellPrice+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].quantity+'</td>'+
					'<td style="text-align:left;">'+productsInfoArray[cnt].batchNo+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].tAmt+'</td>'+
					'<td colspan="4" class="alignCenter borNone" style="border:none!important;"><button type="button" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')" title="<s:text name="Edit" />"></button>'+
					'<button type="button" class="fa fa-trash" aria-hidden="true " onclick="removeRow('+cnt+')" title="<s:text name="Delete" />"></button></td>'+
					'</tr>';
					
		if(tfootArray.length==
			0){
			tfootArray[tfootArray.length] = {
											 totalPrice : productsInfoArray[cnt].tAmt,
										 	};
			
		} else { 
		   tfootArray[tfootArray.length-1].totalPrice=(parseFloat(tfootArray[tfootArray.length-1].totalPrice)+parseFloat(productsInfoArray[cnt].tAmt));
		}
		

		
	  }	
	}	
	
	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="12" style="text-align:center"><s:text name="noRecordFound"/></td>'+
					'</tr>';
	}
	
	
	jQuery('#productInfoTbl1 > tbody').html(tbodyRow);
	//jQuery('#productInfoTbl > tbody').html('');
	var proudctDataRow="";
	proudctDataRow += '<tr>'+
	'<td style="text-align:center;"></td>'+
	'<td style="text-align:left;"></td>'+	
	'</tr>';
	//jQuery('#productInfoTbl > tbody').html(proudctDataRow);
	var taxAmtValue = $("#amtTax").val();
	var tfootRow = "";
	jQuery('#productInfoTbl1 > tfoot').html('');

jQuery('#productInfoTbl1 > tfoot').html(tfootRow);
jQuery("#product1").focus();
}


function loadUnit(){
	
	/* var selectedGrade = document.getElementById("grade").value;
	
	var availableUnit;
	
	$.post("cropSaleEntryReport_populateGradeUnit",{selectedGrade:selectedGrade},function(data){
	
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
			}
		});
	}); */
	var selectedCrop = document.getElementById("cropName").value;
	$.post("cropSaleEntryReport_populateUnit",{selectedCrop:selectedCrop},function(data){
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
			}
		});
	});
	
}


function removeRow(indx){
	
	for(var i=0;i<productTotalArray.length;i++){
	    if(productTotalArray[i].name==productsInfoArray[indx].name+'='+productsInfoArray[indx].units || productTotalArray[i].batchno==productsInfoArray[indx].batchno){
		productTotalArray[i].qty-=productsInfoArray[indx].QtyPerUnit;		
		break;
		}
	}
	
	if(productsInfoArray.length>0){		
				
		// To reset the Total Sale Quantity (Kg) and Total Sale Value (INR) below
		
		if(productsInfoArray.length==1){     // To check if the product is the last product.
			document.getElementById('totalSalePrice').innerHTML="";	
			document.getElementById('totalSaleQty').innerHTML="";	
			}
			else {
				document.getElementById('totalSalePrice').innerHTML=document.getElementById('totalSalePrice').innerHTML-productsInfoArray[indx].tAmt;	
				document.getElementById('totalSaleQty').innerHTML=document.getElementById('totalSaleQty').innerHTML-productsInfoArray[indx].quantity;						
			}
		
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

function submitCropSaleImage(){	
	$("#target").submit();
}

function submitCropSale() {
	
	var hit=true;		
	var editIndex = getEditIndex();
	var selectedVillage = $('#village').val();
	var selectedFarmer = $('#farmer').val();
	var selectedFarm = $('#farm').val();
	var selectedBuyer=$("#selectedBuyer").val();
	/* var coPricePrefix = document.getElementById("costPriceRupees").value;
	var coPriceSuffix = document.getElementById("costPricePaise").value;
	var costPricePreSuf =  coPricePrefix +"."+coPriceSuffix; */
	var saleDate= $('#startDate').val();
    var currentDate = new Date();
    var currentMonth = currentDate.getMonth()+1;
    var currentYear = currentDate.getFullYear();
    var transportName=$('#transportName').val();
    var vechicleNo=$('#vechicleNo').val();
    var invoiceNo=$('#invoiceNo').val();
    //alert("cooperative:"+cooperative);
    var cooperative = $("#cooperativeId").val();
    
    var paymentMode = $('input[name=amtType]:checked').val();
    var totalSalePrice = document.getElementById("totalSalePrice").innerHTML;
    var payment=$('#payment').val();
    var creditAmount = document.getElementById("buyerCreditAmt").innerHTML;


    if(saleDate == "" || saleDate == null) {
  		document.getElementById("validateError").innerHTML='<s:text name="empty.Date"/>';
  		hit=false;
  		enableButton();
 		return false;
 	}
    
    if(selectedVillage == "" || selectedVillage=="-1")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.village"/>';
		return false;
	}
	
    if(selectedFarmer == "" || selectedFarmer=="0")
	{
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.farmer')}" />';
		return false;
	}
	
    if(selectedFarm == "" || selectedFarm=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.farm"/>';
		return false;
	}
	
    if(selectedBuyer =="" || selectedBuyer=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.buyer"/>';
		return false;
	}
    if(productsInfoArray.length==0) {
 		document.getElementById("validateError").innerHTML='<s:text name="noRecordFoundCropSale"/>';
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
 
    if((payment=="" || payment==null) && paymentMode=='0')
    	{
    	document.getElementById("validateError").innerHTML='<s:text name="empty.paymentAmount"/>';
    	return false;
    	}
    if(payment == "" || payment == null) {
    	payment = 0;
    }
     
	if(hit){
		var productTotalString=' ';
		for(var i=0;i<productsInfoArray.length;i++){
			if(productsInfoArray[i].batchNo=='undefined' || productsInfoArray[i].batchNo=="" )
			{
				productsInfoArray[i].batchNo="";
			}
			//alert("done"+productsInfoArray[i].batchNo);
			productTotalString+=productsInfoArray[i].cropType+"#"+productsInfoArray[i].cropName+"#"+productsInfoArray[i].variety+"#"+productsInfoArray[i].grade+"#"+productsInfoArray[i].sellPrice+"#"+productsInfoArray[i].batchNo+"#"+productsInfoArray[i].quantity+"|";
			
		 }
		
		$.post("cropSaleEntryReport_populateSave.action",{selectedVillage:selectedVillage,selectedFarmer:selectedFarmer,selectedFarm:selectedFarm,productTotalString:productTotalString,saleDate:saleDate,
			totalSalePrice:totalSalePrice,payment:payment,selectedBuyer:selectedBuyer,transportName:transportName,vechicleNo:vechicleNo,invoiceNo:invoiceNo,
			paymentMode:paymentMode, creditAmount:creditAmount,coOperative:cooperative},function(data,result){
			  if(result=='success')
				{
				  $("#csId").val(data);				 
				 submitCropSaleImage();
				   document.getElementById("divMsg").innerHTML=result;
				  document.getElementById("enableModal").click(); 
					//window.location.href="cropSaleEntryReport_list.action";
				} 				
			 	
		});
	}
	
	jQuery(".save-btn").prop('disabled',true);
	
}

function enableButton(){
jQuery(".save-btn").prop('enabled',true);
}

function cancel() {
	document.form.action = "cropSaleEntryReport_list.action";
	document.listForm.submit();
}
function disablePopupAlert(){
	
	document.redirectform.action="cropSaleEntryReport_list.action";
	document.redirectform.submit();
}

function resetProductData(){
	
	resetEditFlag()
	var idsArray=['cropType','cropName','variety','grade'];
	resetSelect2(idsArray);
   	document.getElementById("validateError").innerHTML="";
	document.getElementById("sellPrice").value="";
	document.getElementById('quantity').value = "";
	document.getElementById('batchNo').value = "";
	document.getElementById('totalPrice').innerHTML = "";
}

function resetDatas(){
	var idsArray=['variety','grade'];
	resetSelect2(idsArray);
	document.getElementById("productUnit").innerHTML="";
	document.getElementById("sellPrice").value="";
	document.getElementById('quantity').value = "";
	document.getElementById('batchNo').value = "";
	document.getElementById('totalPrice').innerHTML = "";
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

$('#quantity').keypress(function(event) {
	  if ((event.which != 46 || $(this).val().indexOf('.') != -1) &&
	    ((event.which < 48 || event.which > 57) &&
	      (event.which != 0 && event.which != 8))) {
	    event.preventDefault();
	  }

	  var text = $(this).val();

	  if ((text.indexOf('.') != -1) &&
	    (text.substring(text.indexOf('.')).length > 2) &&
	    (event.which != 0 && event.which != 8) &&
	    ($(this)[0].selectionStart >= text.length - 2)) {
	    event.preventDefault();
	  }
	});
	 

function isAlphaNumeric(evt){
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && !((charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))) {
	        return false;
	    }
	    return true;
}

function calTotalQty(call){
	var quantity = $("#quantity").val();
	var sellPrice = $("#sellPrice").val();
	quantity = quantity.replace (/,/g, "");
	sellPrice = sellPrice.replace (/,/g, "");
	
	var val1 = (isNaN(parseFloat(quantity)) ? 0 : parseFloat(quantity));

	var val2 = (isNaN(parseFloat(sellPrice)) ? 0 : parseFloat(sellPrice));
	var sum = val1 * val2;
	sum=sum.toFixed(2);
	document.getElementById("totalPrice").innerHTML=sum;
	
	
}

function listFarmers(obj){
	var selectedVillage = $('#village').val();
	jQuery.post("cropSaleEntryReport_populateFarmer.action",{id:obj.value,dt:new Date(),selectedVillage:obj.value},function(result){
		insertOptions("farmer",JSON.parse(result));
		//listFarms(document.getElementById("farm"));
	});
}	


function listFarms(obj){
	var selectedFarmer = $('#farmer').val();
	jQuery.post("cropSaleEntryReport_populateFarm.action",{id:obj.value,dt:new Date(),selectedFarmer:obj.value},function(result){
		insertOptions("farm",JSON.parse(result));
	});
}


function listCropType(obj){
	var selectedCropType = $('#cropType').val();
	var farm= $('#farm').val();
	
	
	$.ajax({
		 type: "POST",
        async: false,
        url: "cropSaleEntryReport_populateCropName.action",
        data: {id:obj.value,dt:new Date(),selectedCropType:obj.value,selectedFarm:farm},
        success: function(result) {
        	insertOptions("cropName",JSON.parse(result));
    		listCrops(document.getElementById("cropName"));
        }
	});
	
}

function listCrops(obj){
	var selectedCropName = $('#cropName').val();
	var selectedCropType = $('#cropType').val();
	var selectedFarm = $('#farm').val();

	$.ajax({
		 type: "POST",
       async: false,
       url: "cropSaleEntryReport_populateVarietyName.action",
       data: {id:obj.value,dt:new Date(),selectedCropName:obj.value,selectedCropType:selectedCropType,selectedFarm:selectedFarm},
       success: function(result) {
    	   insertOptions("variety",JSON.parse(result));
   			listVariety(document.getElementById("variety").value); 	
       }
	});
}


function listVariety(obj){
	var selectedVariety = $('#variety').val();
	$.ajax({
		 type: "POST",
		 async: false,
      url: "cropSaleEntryReport_populateGradeName.action",
      data: {id:obj.value,dt:new Date(),selectedVariety:obj},
      success: function(result) {
    	  insertOptions("grade",JSON.parse(result));
      }
	}); 
}



</script>
<script src="plugins/jquery-input-mask/jquery.inputmask.bundle.min.js"></script>
<script type="text/javascript">


 document.getElementById("startDate").value='<s:property value="currentDate" />';
//resetProductData();
jQuery("#village").focus();


function distributionEnable(){
	window.location.href="cropSaleEntryReport_create.action";
	resetProductData();
	resetDatas();
}

</script>

