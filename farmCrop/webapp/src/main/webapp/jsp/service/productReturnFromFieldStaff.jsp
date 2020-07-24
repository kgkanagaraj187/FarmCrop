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

<s:form name="form" cssClass="fillform"
	action="productReturnFromFieldStaff_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" />
	<s:hidden key="command" />
	<s:hidden id="farmerId" name="farmerId" />
	<s:hidden id="farmerName" name="farmerName" />
	<s:hidden key="selecteddropdwon" id="listname" />
	<s:hidden key="temp" id="temp" />
	<s:hidden key="receiptNumber" id="receiptNumber" />
	<s:hidden id="enableBatchNo" name="enableBatchNo" />
	<s:textfield name="costPriceArrayValue" cssClass="hide"
		id="costPriceMap" />
	<font color="red"> <s:actionerror /></font>

	<div>

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
							</div>
							</p>
						</div>
					</div>


				</div>



				<h2>
					<s:text name="info.general" />
				</h2>
				<div class="flexiWrapper filterControls">
					<s:if test="harvestSeasonEnabled==1">
						<div class="flexi flexi10">
							<label for="txt"><s:text name="season.name" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedSeason" list="harvestSeasonList"
									onchange="populateSubCategory();resetSeasonData();"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" id="season"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
					</s:if>
				<s:if test="currentTenantId!='pratibha'">
					<div class="flexi flexi10">
						<label for="txt"><s:text name="agentName" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="selectedAgent" id="agentId" list="agentLists"
								onchange="populateGroup(this);" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" cssClass="form-control input-sm select2" />
						</div>
					</div>
				</s:if>
				<s:else>
						<div class="flexi flexi10">
						<label for="txt"><s:text name="agentName" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="selectedAgent" id="agentId" list="agentListDatas"
								onchange="populateWarehouse(this);populateSubCategory();resetWarehouseData();populateGroup(this);resetPrefixAndSuffix();" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" cssClass="form-control input-sm select2" />
						</div>
					</div>
				
				</s:else>
					<div class="flexi flexi10">
						<label for="txt"><s:text name="warehouse.name" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:if test="currentTenantId!='lalteer' && currentTenantId!='pratibha'">
								<s:select name="selectedWarehouse" id="warehouse"
									list="coopearativeList"
									onchange="populateSubCategory();resetWarehouseData();resetPrefixAndSuffix();"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" listKey="key"
									listValue="value" />
							</s:if>
							<s:elseif test="currentTenantId=='pratibha'">
							
								<s:label class="userWarehouse" id="userWarehouse"
										style="display : block;text-align : center" />
										<s:hidden id="warehouseId" name="warehouseId"/>
							</s:elseif>
						
							<s:else>

								<s:select name="selectedWarehouse" id="warehouse"
									list="coopearativeList"
									onchange="populateLalteerSubCategory();resetWarehouseData();resetPrefixAndSuffix();"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="form-control input-sm select2" listKey="key"
									listValue="value" />
							</s:else>
						</div>
					</div>
					<div class="flexi flexi10 ">
						<label for="txt" style="display: block; text-align: center"><s:property
								value="%{getLocaleProperty('Group')}" /> </label>
						<div class="form-element">
							<span id="selAgentGroup"
								style="display: block; text-align: center" />
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('distributionDate')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple" size="20"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								cssClass="date-picker form-control input-sm" />
						</div>
					</div>
					<s:if test="harvestSeasonEnabled==0">
						<div class="flexi flexi10 seasonAlign">
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

				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="productDetails" />
						</h2>
						<div class="flexiWrapper filterControls">

							<div class="flexi flexi10 hide">
								<label for="txt"><s:text name="unit" /></label>
								<div class="form-element">
									<s:select name="selectedUnit" list="{}" headerKey="1-Unit"
										cssClass="hide" headerValue="1-Unit" id="unit1" onchange=""
										cssStyle="display:none;" />

									<s:select name="subUnit" id="subUnit" cssClass="hide"
										onchange="calulateAvailableStock(0);" list="{}"
										disabled="true" cssStyle="width:80px!important; display:none;" />
								</div>
							</div>



							<div class="flexi flexi10" >

								<label for="txt"><s:text name="subCategory" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:if test="currentTenantId=='lalteer'">
										<s:select name="selectedCategory" list="categoryList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="category"
											onchange="listProductLalteer();resetPrefixAndSuffix();populateUnitLalteer();"
											cssClass="form-control input-sm select2" />


									</s:if>
									<s:else>
										<s:select name="selectedCategory" list="categoryList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="category"
											onchange="listProduct();resetPrefixAndSuffix();populateUnit();"
											cssClass="form-control input-sm select2" />
									</s:else>
								</div>

							</div>
							<div class="flexi flexi10 " >

								<label for="txt"><s:text name="product" /><span
									class="manadatory">*</span></label>
								<div class="form-element">

									<s:if test="enableBatchNo ==1">
										<s:select name="selectedProduct" list="productList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="product1" cssClass="form-control input-sm select2"
											onchange="listBatchNo();populateUnit();" />
									</s:if>
									<s:else>
										<s:select name="selectedProduct" list="productList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="product1" cssClass="form-control input-sm select2"
											onchange="resetUnitData();availableStock(0);resetPrefixAndSuffix();populateUnit();" />
									</s:else>
								</div>

							</div>
							<s:if test="enableBatchNo ==1">
								<div class="flexi flexi10">

									<label for="txt"><s:text
											name="warehouseProduct.batchNo" /><span class="manadatory">*</span></label>
									<div class="form-element">
										<s:select name="selectedBatchNo" list="batchNoList"
											headerKey="" headerValue="%{getText('txt.select')}"
											id="batchNo" cssClass="form-control input-sm select2"
											onchange="resetUnitData();availableStock(0);resetPrefixAndSuffix();" />
									</div>

								</div>
							</s:if>


							<div class="flexi flexi10">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="warehouseProduct.availableStock" /></label>
								<div class="form-element">

									<s:label class="avlStkTxt" id="stock"
										style="display : block;text-align : center" />

								</div>
							</div>



							<div class="flexi flexi10">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="distribution.unit" /></label>
								<div class="form-element">
									<span id="productUnit"
										style="display: block; text-align: center"></span>
								</div>
							</div>
						<%-- 	<div class="flexi flexi10">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="pricePerUnit" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</label>
								<div class="form-element">
									<span id="productPrice"
										style="display: block; text-align: center">0.00</span>

								</div>
							</div> --%>
							<s:else>
							<div class="flexi flexi10 ">
								<label for="txt"><s:text name="returnStock" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="returnStock" name="returnStock" maxlength="8"
											onkeypress="return isNumber(event)"
											onkeyup="calulateAvailableStock();" cssStyle="width:90%" />
										<div>.</div>
										<s:textfield id="stockPaise" name="stockPaise" maxlength="3"
											onkeypress="return isNumber(event)"
											onkeyup="calulateAvailableStock();" cssStyle="width:60%" />
									</div>
								</div>
							</div>
							</s:else>

							<div class="flexi flexi10 ">
								<label for="txt"><s:text name="action" /></label>
								<td colspan="4" class="alignCenter">
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
												<i class="fa fa-trash"></i>
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
					<s:text name="productReturnProducts" />
				</h2>
				<div class="flexiWrapper filterControls">
					<table id="productInfoTbl"
						class="table table-bordered aspect-detail">
						<thead>
							<tr class="odd">
								<th width="6%" style="text-align: center"><s:text
										name="sno" /></th>
								<th width="16%" style="text-align: center"><s:text
										name="subCategory" /></th>
								<th width="17%" style="text-align: center"><s:text
										name="product" /></th>
								<s:if test="enableBatchNo ==1">
									<th width="16%" style="text-align: center"><s:text
											name="warehouseProduct.batchNo" /></th>
								</s:if>
								<th width="10%" style="text-align: center"><s:text
										name="distribution.unit" /></th>
								<th width="17%" style="display: none";><s:text name="units" /></th>
								<th width="11%" style="display: none";><s:text
										name="totalQuantity" /></th>
								<%-- <th width="12%" style="text-align: center"><s:text
										name="pricePerUnit" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</th> --%>
								<th width="13%" style="text-align: center"><s:text
										name="quantity" /></th>

								<th width="18%" style="text-align: center"><s:text
										name="action" /></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="8" style="text-align: center;"><s:text
										name="noRecordFoundProdReturAgentProdReturAgent" /></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td style="border: none !important;">&nbsp;</td>
								<td colspan="2" class="alignRight"
									style="border: none !important; padding-right: 10px !important; padding-top: 6px !important; padding-bottom: 6px !important;"><s:text
										name="total" /></td>
								<td align="right"
									style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important;">0</td>
								<td style="border: none !important;">&nbsp;</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
			<div class="yui-skin-sam" id="loadList" style="display: block">
				<%-- <sec:authorize
							ifAllGranted="service.productReturn.fieldStaff.create"> --%>
				<span id="savebutton" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="submitProductReturn();" id="sucessbtn">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>

				<%-- </sec:authorize> --%>
				<span class="yui-button"><span class="first-child"><a
						id="cancelbutton" onclick="redirectForm();"
						class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
								<s:text name="cancel.button" />
						</font>
					</a></span></span>
			</div>

		</div>



	</div>
	</div>


	<!--end decorator body -->

	</div>


	<s:hidden id="productList" name="productReturnProductList" />


</s:form>


<button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>

<!-- Modal -->
<%-- <div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content" style="width: 330px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
					<s:text name="productReturndialog.title" />
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
<div id="myModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="contentHdr">
				<button type="button" class="close" data-dismiss="modal">&times;</button>			
			<div class="modal-body">
				<i class="fa fa-check" aria-hidden="true"></i></br>
            <span><h5><s:text name="productreturnMsg" /></h5></span>
				<div id="divMsg" align="center"></div>
			</div>
			</div>
			<div class="contentFtr" >
			<button type="button" class="btn btn-danger btnBorderRadius hide" data-dismiss="modal"
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

<script type="text/javascript">
var printWindowCnt=0;
var windowRef;
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
var season = '<s:property value="%{getCurrentSeasonsCode()}" />';
var harvestSeasonEnable='<s:property value="%{getHarvestSeasonEnabled()}" />';
jQuery(document).ready(function(){
	document.getElementById("returnStock").disabled=false;

	//startTimer("productReturnFromFieldStaff");
	//$("#sucessbtn").prop('disabled', false);
	<s:if test='productReturnDescription!=null'>
		jQuery('<tr><td><s:property value="productReturnDescription" escape="false"/></td></tr>').insertBefore(jQuery(jQuery("#restartAlert").find("table").find("tr:first")));
		jQuery("#restartAlert").css('height','130px');
	</s:if>
	   var prodtRtrn='';
	if(prodtRtrn=="")
	{
		$('#restartAlert').hide();
	}  
	
	else
	{
		$('#restartAlert').show();
	}
	 
	//hideGroupDiv(jQuery("#agentId").val());
	resetPrefixAndSuffix();
});


var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();
var subCategoryArray=new Array();
var costPriceArray=new Array();
function listUnits(call){	
    var productUnits=call.value;
	var start=productUnits.indexOf("=");	
	 var resultString=productUnits.substring(start+1);
	 var arry=resultString.split('~');
	document.form.unit1.length = 0;
	addOption(document.getElementById("unit1"),'<s:text name="txt.select"/>', "");
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

/* function hideGroupDiv(data){
	if(data==""){
		$(".groupDiv").hide();
		$(".groupDivValue").hide();
	}
	else{
		$(".groupDiv").show();
		$(".groupDivValue").show();
	}
} */

function validateData() {
	
	var hit=validateDetails(true);
	if(hit){
		var totalPrice=jQuery("#totalPriceLabel").val();
		if(parseFloat(totalPrice)==0){
			document.getElementById("validateError").innerHTML="invalidLowStock";
		}else{
		   document.getElementById("validateError").innerHTML="";
		   reload();
		  jQuery('#category').prop("disabled",false);
		  jQuery('#product1').prop("disabled",false);
			if($("#enableBatchNo").val()=='1'){
		  jQuery('#batchNo').prop("disabled",false);
			}
		}
	}
}

function validateDetails(validateEmpty){
	var hit=true;
	var selectedCategory = document.getElementById("category").value;
	var selectedProduct = document.getElementById("product1").value;
	var selectedUnit=document.getElementById("unit1").value;
	var returnStock = document.getElementById("returnStock").value;
	var stockPaise = document.getElementById("stockPaise").value;
	var availableStock = document.getElementById("stock").innerHTML;
	availableStock=availableStock.split('-')[0];	
	var units=jQuery.trim(jQuery("#unit1").val()).split('-');
	var subUnit=jQuery("#subUnit").val();
	var isPartial=!(subUnit===selectedUnit);
	var QtyPerUnit,toatalQty;
	var distStock=returnStock+"."+stockPaise;
	//alert("disStock :"+distStock);


	if(selectedCategory==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectedCategory"/>';
		hit=false;
	}
	
	if(selectedProduct==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectProduct"/>';
		hit=false;
	}
	else if(selectedUnit==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectUnit"/>';
		hit=false;
	}
	else if(isNaN(returnStock)==true){
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
	else if(isPartial){
			if(units[1].toUpperCase()==="KG"  && subUnit.toUpperCase()=="GM"){
				toatalQty=units[0]*1000;
			}else if(units[1].toUpperCase()==="LT"  && subUnit.toUpperCase()=="ML"){
				toatalQty=units[0]*1000;
			}else{
				toatalQty=units[0];
			}
			QtyPerUnit=(parseFloat(distStock)/toatalQty).toFixed(3);
			if(parseFloat(QtyPerUnit)==0){
				document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
				hit=false;
			}
	}else if(!isPartial){
		QtyPerUnit=(parseFloat(distStock)).toFixed(3);
		if(parseFloat(QtyPerUnit)==0){
			document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
			hit=false;
		}
	}
	
	return hit;
}

function availableStock(val){
	var selectedProduct = document.getElementById("product1").value;
	if(tenant!="pratibha"){
	var selectedWarehouse = document.getElementById("warehouse").value;
	}
	else{
		var selectedWarehouse=document.getElementById("warehouseId").value;
	}
	var selectedUnit=document.getElementById("unit1").value;
	var availableStock = document.getElementById("stock").value;
	var returnStock = document.getElementById("returnStock").value;
	var selectedAgent=document.getElementById("agentId").value;
	var selectedSeason= document.getElementById("season").value;
	 
	var selectedCategory=document.getElementById("category").value;
	var batchNo="NA";
	var hit=true;	
		if(selectedProduct==""){
		resetProductData();
		hit=false;
	}
	if(selectedProduct=="0"){
		document.getElementById('stock').innerHTML = "";
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
	
	if(selectedCategory=="" ||selectedCategory==0){
	resetUnitData();
		hit=false;
	}
	if($("#enableBatchNo").val()=='1'){
		var selectedBatchNo=document.getElementById("batchNo").value;
		batchNo=selectedBatchNo;
		if(selectedBatchNo==""){
			resetProductData();
			hit=false;
		}
	}
	if(hit) {
		
		var units=selectedUnit.split('-');
		document.getElementById("validateError").innerHTML="";
		document.getElementById("returnStock").disabled=false;
		var category=$("#category").val();
		if(category.trim()!=''){
			  if(tenant=="lalteer"){
				  $.ajax({
						 type: "POST",
				        async: false,
				        url: "productReturnFromFieldStaff_loadAvailableStockLalteer.action",
				        data: {selectedWarehouse:selectedWarehouse,selectedProduct:selectedProduct,selectedUnit:selectedUnit,batchNo:batchNo},
				        success: function(data) {
					if(data!=null && data!=""){
						if(data.indexOf("j_username")!= -1){
							redirectForm();
						}else{
							var n=data.split(",");
							for(var i=0; i< productsInfoArray.length; i++){
								if(productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit  && productsInfoArray[i].batchNo===batchNo ){
									n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
								}
							}
							
							
							document.getElementById('returnStock').disabled = false;
							document.getElementById('subUnit').disabled = false;
							if(n[0] == "N/A" || n[1] == "N/A"){
								document.getElementById('returnStock').disabled = true;
								document.getElementById('subUnit').disabled = true;
							}
				            if(n[0]<=0){
				            	document.getElementById('returnStock').disabled = true;
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
				            totalQty = parseFloat(totalQty);
				          //  alert("totalQty"+totalQty)
							$("#stock").text(totalQty);
							$("#productPrice").text(n[1]);
							if(val==0){
							document.getElementById('returnStock').value="";
							}
							jQuery("#totalPriceLabel").html('');
							document.getElementById("validateError").innerHTML="";
							jQuery("#returnStock").focus();
						}
					}
				        }	
				}); 
			  }
			  else{
			$.ajax({
				 type: "POST",
		        async: false,
		        url: "productReturnFromFieldStaff_loadAvailableStock.action",
		        data: {selectedWarehouse:selectedWarehouse,selectedProduct:selectedProduct,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedSeason:selectedSeason,batchNo:batchNo},
		        success: function(data) {
			if(data!=null && data!=""){
				if(data.indexOf("j_username")!= -1){
					redirectForm();
				}else{
					var n=data.split(",");
					for(var i=0; i< productsInfoArray.length; i++){
						if(productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit  && productsInfoArray[i].batchNo===batchNo ){
							//n[0]= parseFloat(parseFloat(n[0]).toFixed(4))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
							n[0]= parseFloat(parseFloat(n[0]).toFixed(4));
						}
					}
					
					
					document.getElementById('returnStock').disabled = false;
					document.getElementById('subUnit').disabled = false;
					if(n[0] == "N/A" || n[1] == "N/A"){
						document.getElementById('returnStock').disabled = true;
						document.getElementById('subUnit').disabled = true;
					}
		           /*  if(n[0]<=0){
		            	document.getElementById('returnStock').disabled = true;
						document.getElementById('subUnit').disabled = true;				
					} */
					
					
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
		            totalQty = parseFloat(totalQty);	
					$("#stock").text(totalQty);
					$("#productPrice").text(n[1]);
					if(val==0){
					document.getElementById('returnStock').value="";
					}
					jQuery("#totalPriceLabel").html('');
					document.getElementById("validateError").innerHTML="";
					jQuery("#returnStock").focus();
				}
			}
		        }	
		});
	   }
		}else{
			document.getElementById("validateError").innerHTML="please Select Category";
		}
	}
}


function loadUnit(){
	
	var selectedProduct = document.getElementById("product1").value;
	var productName=document.getElementById("product1").value;
	
	var availableUnit;
	$.post("productReturnFromFieldStaff_populatePopulateProductUnit",{selectedProduct:selectedProduct},function(data){
		
			
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
	
	var stockPre=document.getElementById("returnStock").value;
	var stockPaise=document.getElementById("stockPaise").value;
	var returnStock = stockPre+"."+stockPaise;
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	//var returnStock=document.getElementById("returnStock").value;
	//var pricePerUnit=document.getElementById("productPrice").innerHTML;
	var units=document.getElementById("unit1").value;
    var subUnit=jQuery("#subUnit").val();
	var Qty,QtyPerUnit;
	var unitArr=units.split('-');
	var subUnit=jQuery('#subUnit').val();
	var productExists=false;
	var selectedProduct = document.getElementById("product1").value;
	var selectedBatchNo="NA";
	if(enableBatchNo=='1'){
		batchNo = document.getElementById("batchNo").value;
	}
	var pName=productName.split('=')[0]+'='+units;var isPartial=!(jQuery("#subUnit").val()===units);
	var totalStockPrice=calculateTotalPrice(returnStock,isPartial,units.split('-'),subUnit);	
		
	if(isPartial){
		
		if(unitArr[1].toUpperCase()==="KG" &&  subUnit.toUpperCase()=="GM"){
			QtyPerUnit=(parseFloat(Qty)/(parseFloat(unitArr[0])*1000)).toFixed(3);
			Qty=(parseFloat(Qty)/1000).toFixed(3);
			
		}else if(unitArr[1].toUpperCase()==="LT" &&  subUnit.toUpperCase()=="ML"){
			QtyPerUnit=(parseFloat(Qty)/(parseFloat(unitArr[0])*1000)).toFixed(3);
			Qty=(parseFloat(Qty)/1000).toFixed(3);
			
		}else{
	        QtyPerUnit=(parseFloat(Qty)/parseFloat(unitArr[0])).toFixed(3);
		    Qty=(parseFloat(Qty)).toFixed(3);
		}
	}else{
		
		Qty=(parseFloat(Qty)*parseFloat(unitArr[0])).toFixed(3);
		QtyPerUnit=(parseFloat(returnStock)).toFixed(3);

		
			
	}

	/* costPriceArray.push(pricePerUnit);
	$("#costPriceMap").val(costPriceArray.toString());  */
	
	var productArray=null;
	if(editIndex==-1){
	
	 productArray = {
			 productId :selectedProduct,
			 name : productName.split('=')[0],
			 returnStock : Qty+"-"+unitArr[1],
			 units: units, 
			// pricePerUnit : pricePerUnit, 
		     //totalStockPrice : totalStockPrice,
			 distributionType : isPartial,
			 Qty : Qty,
			 QtyPerUnit:QtyPerUnit,
			 subUnit:subUnit,
			 enteredStock:parseFloat(returnStock).toFixed(3),
			 categoryName : categoryId,
			 unit : proUnitVal,
			 prodName:prodName,
			 catName:catName,
			 batchNo:batchNo,
			 isEdit:false
         };

	 for(var i=0;i<productsInfoArray.length;i++){
		 
		 if(productsInfoArray[i].productId== productArray.productId && productsInfoArray[i].batchNo== productArray.batchNo){
				Qty=parseFloat(productsInfoArray[i].Qty)+(parseFloat(productArray.Qty));
				QtyPerUnit=parseFloat(productsInfoArray[i].QtyPerUnit)+(parseFloat(productArray.QtyPerUnit));
			    //totalStockPrice=parseFloat(productsInfoArray[i].totalStockPrice)+parseFloat(productArray.totalStockPrice);
				returnStock=parseFloat(productsInfoArray[i].returnStock)+(parseFloat(productArray.returnStock));
				enteredStock=parseFloat(productsInfoArray[i].enteredStock)+(parseFloat(productArray.enteredStock));
				productsInfoArray[i].returnStock = Qty+"-"+unitArr[1];
				productsInfoArray[i].units= units;
				productsInfoArray[i].pricePerUnit = pricePerUnit; 				
				//productsInfoArray[i].totalStockPrice=parseFloat(totalStockPrice).toFixed(2);
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
	    productsInfoArray[editIndex].productId= selectedProduct;
		productsInfoArray[editIndex].name= productName.split('=')[0];
		productsInfoArray[editIndex].returnStock = Qty+"-"+unitArr[1];
		productsInfoArray[editIndex].units= units;
		//productsInfoArray[editIndex].pricePerUnit = pricePerUnit; 
	    //productsInfoArray[editIndex].totalStockPrice = totalStockPrice;
		productsInfoArray[editIndex].distributionType = isPartial;
		productsInfoArray[editIndex].Qty = Qty;
		productsInfoArray[editIndex].QtyPerUnit=QtyPerUnit;
		productsInfoArray[editIndex].batchNo=batchNo;
		productsInfoArray[editIndex].subUnit=subUnit;
		productsInfoArray[editIndex].enteredStock=parseFloat(returnStock).toFixed(3);
		productsInfoArray[editIndex].categoryName=categoryId;
		productsInfoArray[editIndex].isEdit=false;
		productArray=productsInfoArray[editIndex];
		
	}
	var isExist=false;
	var pName=productName.split('=')[0]+'='+units;
	
	for(var i=0;i<productTotalArray.length;i++){
		if(productTotalArray[i].name==pName &&	productTotalArray[i].batchNo==batchNo){
			
			//productTotalArray[i].qty=(parseFloat(productTotalArray[i].qty)+parseFloat(QtyPerUnit)).toFixed(3);
			productTotalArray[i].qty= QtyPerUnit;
			productTotalArray[i].units=units;
			productTotalArray[i].batchNo=batchNo;
			//productTotalArray[i].pricePerUnit = pricePerUnit; 
		    //productTotalArray[i].totalStockPrice =totalStockPrice;
			isExist=true;
			break;
		}
	}
	if(!isExist){
		var  totalArray={
				name:	productName.split('=')[0]+'='+units,
		        qty :   QtyPerUnit,
		        units:units,
		        batchNo:batchNo,
		      //  pricePerUnit : pricePerUnit, 
		        returnStock : returnStock,
		        enteredStock:parseFloat(returnStock).toFixed(4),
		        //totalStockPrice : totalStockPrice,
		}
		productTotalArray[productTotalArray.length]=totalArray;
	}
reloadTable();

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

function reloadTable(){
	var enableBatchNo=$("#enableBatchNo").val();
	var tbodyRow = "";
	var tfootArray = new Array();	
	jQuery('#productInfoTbl > tbody').html('');
	var rowCount=0;
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].catName+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].prodName+'</td>'+
					 ((enableBatchNo === '1') ? '<td style="text-align:center;">'+productsInfoArray[cnt].batchNo+'</td>' :'') +
					 '<td style="text-align:center;">'+productsInfoArray[cnt].unit+'</td>'+
					// '<td style="text-align:left;">'+productsInfoArray[cnt].batchNo+'</td>'+
					 //'<td style="text-align:right;">'+productsInfoArray[cnt].returnStock+'</td>'+
					 // '<td style="text-align:center;">'+productsInfoArray[cnt].pricePerUnit+'</td>'+		
					 '<td style="text-align:center;">'+productsInfoArray[cnt].QtyPerUnit+'</td>'+
							
				     //'<td style="text-align:right;">'+productsInfoArray[cnt].totalStockPrice+'</td>'+
					 '<td style="width :6%"><i title="Edit" style="cursor: pointer;color: white;background-color: #337ab7;padding: 5px 10px; font-size: 13px; line-height: 1.5;margin-left: 10px;" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')"></i>'+
					 '<i title="Delete" class="fa fa-trash" style="cursor: pointer;color: #fff;background-color: #c9302c;border-color: #761c19;padding: 5px 10px;font-size: 13px;line-height: 1.5;margin-left: 25px;" aria-hidden="true " onclick="removeRow('+cnt+')"></i></td>'+
					 '</tr>';	
		if(tfootArray.length==0){
			tfootArray[tfootArray.length] = {
											 totalPrice : productsInfoArray[cnt].QtyPerUnit
										 	};
		}else{ 
		   tfootArray[tfootArray.length-1].totalPrice=(parseFloat(tfootArray[tfootArray.length-1].totalPrice)+parseFloat(productsInfoArray[cnt].QtyPerUnit)).toFixed(2);
		}
	  }	
	}	

	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="8" style="text-align:center"><s:text name="noRecordFoundProdReturAgent"/></td>'+
					'</tr>';
		//document.getElementById("warehouse").disabled=false;
	}else{
		//document.getElementById("warehouse").disabled=true;
	}
		
	jQuery('#productInfoTbl > tbody').html(tbodyRow);	
	var tfootRow = "";
	jQuery('#productInfoTbl > tfoot').html('');

	if(enableBatchNo=='1'){
		tfootRow += '<tr>'+
		'<td style="border:none!important;">&nbsp;</td>'+
	'<td colspan="4" align="right" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="total"/></td>';
}else{
	tfootRow += '<tr>'+
	'<td style="border:none!important;">&nbsp;</td>'+
	'<td colspan="3" align="right" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="total"/></td>';
}
	if(tfootArray.length>0){
			tfootRow +=		'<td align="right" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+tfootArray[tfootArray.length-1].totalPrice+'</td>';
	}else{
		 tfootRow +='<td align="right" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">0</td>';
	}
	tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
				'</tr>';
jQuery('#productInfoTbl > tfoot').html(tfootRow);
jQuery("#product1").focus();
jQuery("#category").focus();
$('#productUnit').html('');
if(enableBatchNo=='1')
jQuery("#batchNo").focus();



}

function removeRow(indx){
	for(var i=0;i<productTotalArray.length;i++){
	    if(productTotalArray[i].name==productsInfoArray[indx].name+'='+productsInfoArray[indx].units){
		productTotalArray[i].qty-=productsInfoArray[indx].QtyPerUnit;
		productTotalArray[i].units-=productsInfoArray[indx].units;
		//productTotalArray[i].pricePerUnit-=productsInfoArray[indx].pricePerUnit;
		productTotalArray[i].returnStock-=productsInfoArray[indx].returnStock;
		productTotalArray[i].enteredStock-=productsInfoArray[indx].enteredStock;
	    //productTotalArray[i].totalStockPrice-=productsInfoArray[indx].totalStockPrice;
		
		break;
		}
	}
	
	 if(productTotalArray.length>0){	
		productTotalArray.splice(indx,1);	
	} 
	if(productsInfoArray.length>0){
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
														productsInfoArray[cnt].batchNo ;
														//productsInfoArray[cnt].pricePerUnit +'|'+
														//productsInfoArray[cnt].totalStockPrice ;
	}	
	
}
function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}
function submitProductReturn() {	
$("#sucessbtn").prop('disabled', true);
var hit=true;
if(tenant!="pratibha"){
	var selectedWarehouse = document.getElementById("warehouse").value;
	}
	else{
		var selectedWarehouse=document.getElementById("warehouseId").value;
	}
  var selectedAgent=$("#agentId").val(); 	
  var selectedDate= $("#startDate").val();
  var selectedProduct=$("#product1").val();
  var batchNo="NA";
	
 
  if(harvestSeasonEnable=="1"){
	     
	    var selectedSeason= document.getElementById("season").value;
	   	 
 }else{
			var selectedSeason='<s:property value="%{seasonName}" />';
		}
  if(selectedSeason=="" &&  selectedSeason==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptySeason')}" />';
		enableButton();
		hit=false;
		return false;
	
  } else if(selectedWarehouse==""){
	  document.getElementById("validateError").innerHTML='<s:text name="emptyWarehouse"/>';
	  //$("#validateError").innerHTML='<s:text name="emptyCooperative"/>';
	  
		hit=false;
		enableButton();
		return false;
	}else if(selectedAgent==""){
		document.getElementById("validateError").innerHTML='<s:text name="emptyAgent"/>';
		// $("#validateError").innerHTML='<s:text name="emptyAgent"/>';
		hit=false;
		enableButton();
		return false;
	}else if(selectedDate.trim()==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectDate"/>';
		// $("#validateError").innerHTML='<s:text name="selectDate"/>';
		hit=false;
		enableButton();
		return false;
	}
  	else  if(productsInfoArray.length==0){
  		document.getElementById("validateError").innerHTML='<s:text name="noRecordFoundProdReturAgent"/>';
  		// $("#validateError").innerHTML='<s:text name="noRecordFoundProdReturAgent"/>';
		hit=false;
		enableButton();
		return false;
	}
  if($("#enableBatchNo").val()=='1'){
		batchNo=document.getElementById("batchNo").value;
		if(isEmpty(batchNo)){
			document.getElementById("validateError").innerHTML='<s:text name="selectBatch"/>';
			// $("#validateError").innerHTML='<s:text name="selectDate"/>';
			hit=false;
			enableButton();
			return false;
		}
		}

  
	if(hit){
	buildReqProductObject();
	var productTotalString=' ';
	var costPriceString=$("#costPriceMap").val();
	for(var i=0;i<productTotalArray.length;i++){	
		
		if($("#enableBatchNo").val()=='1'){
			productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+productTotalArray[i].QtyPerUnit+"##"+productTotalArray[i].batchNo+"||";
		}else{
			productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+productTotalArray[i].QtyPerUnit+"##"+'NA'+"||";
		}
		//productTotalString+=productTotalArray[i].name+"##"+productTotalArray[i].qty+"##"+productTotalArray[i].pricePerUnit+"##"+productTotalArray[i].totalStockPrice+"||";
	}	
	$.post("productReturnFromFieldStaff_populateProductReturn.action",{selectedSeason:selectedSeason,productTotalString:productTotalString,selectedWarehouse:selectedWarehouse,selectedAgent:selectedAgent,selectedDate:selectedDate,costPriceArrayValue:costPriceString,selectedProduct:selectedProduct,batchNo:batchNo},function(data){
		if(data == null || data == ""){		
			
			
			}
		else{ 	 			
			document.getElementById("divMsg").innerHTML=data;
		//	alert(data);
			$("#distributionFS").val(data);
			document.getElementById("enableModal").click();
			//disablePopupAlert();
			//enablePopupAlert();
		}
 	});
 
	}else{
		$("#sucessbtn").prop('disabled', false);
	}
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
	document.redirectform.action="productReturnFromFieldStaff_list.action";
	document.redirectform.submit();
}
function getWindowHeight(){
	var height = document.documentElement.scrollHeight;
	if(height<$(document).height())
		height = $(document).height();
	return height;
}


  function calculateTotalPrice(returnStock,pricePerUnit,isPartial,units,subUnit){
	if(!isPartial)
	return (parseFloat(returnStock) * parseFloat(pricePerUnit)).toFixed(2);
  	else{
		var toatalQty;
		if(units[1].toUpperCase()=="KG" && subUnit.toUpperCase()=="GM"){
			toatalQty=units[0]*1000;
		}else if(units[1].toUpperCase()=="LT" && subUnit.toUpperCase()=="ML"){
			toatalQty=units[0]*1000;
		}else{
			toatalQty=units[0];
		}
			return (parseFloat(pricePerUnit)/parseFloat(toatalQty)*parseFloat(returnStock)).toFixed(2);
	}
}

function resetProductData(){
	resetEditFlag();
	reloadTable();	
	resetPrefixAndSuffix();
	jQuery('#category').prop("disabled",false);
	jQuery('#product1').prop("disabled",false);
	
	if($("#enableBatchNo").val()=='1'){
	jQuery('#batchNo').prop("batchNo",false);
	document.getElementById('batchNo').selectedIndex = ""; 
	jQuery("#batchNo").val('');
	jQuery("#batchNo").select2();
	}
	//document.getElementById('warehouse').selectedIndex="";
	/* document.getElementById('category').selectedIndex ="";*/
	document.getElementById('product1').selectedIndex = ""; 
	jQuery("#category").val('');
	jQuery("#product1").val('');
	
	jQuery("#category").select2();
	jQuery("#product1").select2();
	//jQuery("#agentId").select2();
	
	document.getElementById('stock').innerHTML = "";
	//document.getElementById('productPrice').innerHTML = "";
	//document.getElementById('unit1').selectedIndex = "";
	//document.form.unit1.length = 0;
	document.form.subUnit.length = 0;
	document.getElementById('subUnit').disabled = true;
	//addOption(document.getElementById("unit1"), "Select", "");
	document.getElementById('returnStock').value = "";
	document.getElementById('stockPaise').value = "";
	document.getElementById('returnStock').disabled=true;
	document.getElementById("validateError").innerHTML="";
	jQuery("#totalPriceLabel").html('');
}


function resetUnitData(){
	document.getElementById('stock').innerHTML = "";
	//document.getElementById('productPrice').innerHTML = "";
	document.getElementById('returnStock').value = "";
	document.getElementById('stockPaise').value = "";
	
	document.getElementById('returnStock').disabled=true;
	document.getElementById("validateError").innerHTML="";
	jQuery("#totalPriceLabel").html('');
	document.form.subUnit.length = 0;
	document.getElementById('subUnit').disabled = true;
	
}

function calulateAvailableStock(val){
	
	var batchNo="NA";
  	var selectedProduct = document.getElementById("product1").value;
	if(tenant!="pratibha"){
		var selectedWarehouse = document.getElementById("warehouse").value;
		}
	else{
		var selectedWarehouse=document.getElementById("warehouseId").value;
		}
	
	var selectedUnit=document.getElementById("unit1").value;
	var availableStock = document.getElementById("stock").value;
	
	//var returnStock = document.getElementById("returnStock").value;
	var stock1 = document.getElementById("returnStock").value;
	var stock2 = document.getElementById("stockPaise").value;
	var selectedAgent=document.getElementById("agentId").value;
	var selectedSeason= document.getElementById("season").value;
	
	var selectedCategory=document.getElementById("category").value;
	var returnStock = stock1+"."+stock2;
	var hit=true;
	
	
	if(selectedProduct==""){
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
	if(selectedCategory=="" ||selectedCategory==0){
		resetUnitData();
		hit=false;
	}
	if($("#enableBatchNo").val()=='1'){
		batchNo=document.getElementById("batchNo").value;
		if(batchNo==""){
			resetUnitData();
			hit=false;
		}
	}
	if(hit) {
	var units=selectedUnit.split('-');
	document.getElementById("validateError").innerHTML="";
	document.getElementById("returnStock").disabled=false;
//	$("#stock").text('');
var category=$("#category").val();
		if(category.trim()!=''){
			
			if(tenant=="lalteer"){
				$.post("productReturnFromFieldStaff_loadAvailableStockLalteer",{selectedProduct:selectedProduct,selectedWarehouse:selectedWarehouse,selectedUnit:selectedUnit,batchNo:batchNo},function(data){
					var n=data.split(",");
					for(var i=0; i< productsInfoArray.length; i++){
						if( !productsInfoArray[i].isEdit && productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo){
							n[0]= parseFloat(parseFloat(n[0]).toFixed(3))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
						}
					}
					document.getElementById('returnStock').disabled = false;
					document.getElementById('subUnit').disabled = false;
					if(n[0] == "N/A" || n[1] == "N/A"){
						document.getElementById('returnStock').disabled = true;
						document.getElementById('subUnit').disabled = true;
					}
			        if(n[0]<=0){
			        	document.getElementById('returnStock').disabled = true;
						document.getElementById('subUnit').disabled = true;
					}
					
					var totalQty;
					var selectedSubUnit=document.getElementById('subUnit').value;
					if(selectedUnit==selectedSubUnit){
						totalQty=(parseFloat(n[0])).toFixed(3);
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
					//totalQty = parseInt(totalQty);	
				   //jQuery("#stock").text(totalQty);	
				   //jQuery('#productPrice').text(parseFloat(n[1]).toFixed(2));
				   var stockValidationFlag =validateDetails(false);
				   var returnStock = jQuery.trim(jQuery("#returnStock").val());
				   jQuery("#totalPriceLabel").html('');
				  // alert("stockValidationFlag:"+stockValidationFlag);
				//   alert("returnStock:"+returnStock);
					if((stockValidationFlag && returnStock!="") || val){
						
					//var productPrice = jQuery.trim(jQuery("#productPrice").text());
				    var subUnit=jQuery("#subUnit").val();
				    var isPartial=!(jQuery("#subUnit").val()===jQuery.trim(jQuery("#unit1").val()));
					totalPrice = calculateTotalPrice(returnStock,isPartial,units,subUnit);
					if(parseFloat(totalPrice)==0){
					
						document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
						jQuery("#totalPriceLabel").html('');
					}else{
				        jQuery("#totalPriceLabel").html(totalPrice);
					    document.getElementById("validateError").innerHTML="";
					}
					}
				});
				
			}else{
			
			
			
	$.post("productReturnFromFieldStaff_loadAvailableStock",{selectedProduct:selectedProduct,selectedWarehouse:selectedWarehouse,selectedUnit:selectedUnit,selectedAgent:selectedAgent,selectedSeason:selectedSeason,batchNo:batchNo},function(data){
		var n=data.split(",");
		for(var i=0; i< productsInfoArray.length; i++){
			if( !productsInfoArray[i].isEdit && productsInfoArray[i].name===selectedProduct.split('=')[0] && productsInfoArray[i].units===selectedUnit && productsInfoArray[i].batchNo===batchNo){
				n[0]= parseFloat(parseFloat(n[0]).toFixed(3))-parseFloat(parseFloat(productsInfoArray[i].QtyPerUnit).toFixed(4));
			}
		}
		
		document.getElementById('returnStock').disabled = false;
		document.getElementById('subUnit').disabled = false;
		if(n[0] == "N/A" || n[1] == "N/A"){
			
			document.getElementById('returnStock').disabled = true;
			document.getElementById('subUnit').disabled = true;
		}
        /* if(n[0]<=0){
        	document.getElementById('returnStock').disabled = true;
			document.getElementById('subUnit').disabled = true;
		} */
		
		var totalQty;
		var selectedSubUnit=document.getElementById('subUnit').value;
		if(selectedUnit==selectedSubUnit){
			totalQty=(parseFloat(n[0])).toFixed(3);
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
		
		//totalQty = parseInt(totalQty);	
	   //jQuery("#stock").text(totalQty);	
	   //jQuery('#productPrice').text(parseFloat(n[1]).toFixed(2));
	   var stockValidationFlag =validateDetails(false);
	   var returnStock = jQuery.trim(jQuery("#returnStock").val());
	   jQuery("#totalPriceLabel").html('');
	  // alert("stockValidationFlag:"+stockValidationFlag);
	//   alert("returnStock:"+returnStock);
		if((stockValidationFlag && returnStock!="") || val){
		
		//var productPrice = jQuery.trim(jQuery("#productPrice").text());
	    var subUnit=jQuery("#subUnit").val();
	    var isPartial=!(jQuery("#subUnit").val()===jQuery.trim(jQuery("#unit1").val()));
		totalPrice = calculateTotalPrice(returnStock,isPartial,units,subUnit);
		if(parseFloat(totalPrice)==0){
		
			document.getElementById("validateError").innerHTML='<s:text name="invalidLowStock"/>';
			jQuery("#totalPriceLabel").html('');
		}else{
			
	        jQuery("#totalPriceLabel").html(totalPrice);
		    document.getElementById("validateError").innerHTML="";
		}
		}
	});
			}
	}
		else{
				document.getElementById("validateError").innerHTML="please Select Category";
			}
	}

}
function editRow(indx){
	jQuery('#category').prop("disabled",true);
	jQuery('#product1').prop("disabled",true);
	
	 $("#category option[value='"+productsInfoArray[indx].categoryName+"']").prop("selected","selected");
	 $("#category").trigger("change");

	 $("#product1 option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
	 $("#product1").trigger("change");
	 if($("#enableBatchNo").val()=='1'){
			jQuery('#batchNo').prop("disabled",true);
			
			 $("#batchNo option[value='"+productsInfoArray[indx].batchNo+"']").prop("selected","selected");
			 $("#batchNo").trigger("change");
			}
	 ///jQuery('#category').val(productsInfoArray[indx].categoryName);
	//addOption(document.getElementById("product1"), productsInfoArray[indx].name, productsInfoArray[indx].productId);
    ///jQuery('#product1').val(productsInfoArray[indx].productId);
	var prodInfo=jQuery('#product1 option:selected').val();	 
	
	var start=prodInfo.indexOf("=");
	var resultString=prodInfo.substring(start+1);
	var arry=resultString.split('~');
	
	/*document.form.unit1.length = 0;
	addOption(document.getElementById("unit1"), "Select", "");
	for (var i=0; i < arry.length;i++){
		if(arry[i]!="")
			addOption(document.getElementById("unit1"), arry[i], arry[i]);
		}	
	jQuery('#unit1').val(productsInfoArray[indx].units);*/
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	//alert(proUnitVal);
	var selectedProduct = document.getElementById("product1").value;
	if(tenant!="pratibha"){
		var selectedWarehouse = document.getElementById("warehouse").value;
		}
	else{
			var selectedWarehouse="";
		}
	var selectedUnit=document.getElementById("unit1").value;
	
	
		var selectedSeason= document.getElementById("season").value;
		
	var selectedCategory=document.getElementById("category").value;
	var batchNo="NA"
	if($("#enableBatchNo").val()=='1'){
		var selectedBatchNo=document.getElementById("batchNo").value;
		batchNo=selectedBatchNo;
	}
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
	//document.getElementById('returnStock').disabled=false;
	//document.getElementById('returnStock').value =productsInfoArray[indx].enteredStock;
	
	document.getElementById('returnStock').value =productsInfoArray[indx].enteredStock.split(".")[0];
	document.getElementById('stockPaise').value  = productsInfoArray[indx].enteredStock.split(".")[1];
	
	
	resetEditFlag();
	productsInfoArray[indx].isEdit=true;
//calulateAvailableStock(true);
	var category=$("#category").val();
	if(category.trim()!=''){
		  if(tenant!="lalteer"){
$.post("productReturnFromFieldStaff_loadAvailableStock",{selectedProduct:selectedProduct,selectedWarehouse:selectedWarehouse,selectedUnit:selectedUnit,selectedSeason:selectedSeason,batchNo:batchNo},function(data){
	var jsonData = $.parseJSON(data);
	//alert("data:"+jsonData);
	var n=data.split(",");
	//alert("nnnnnn:"+n);
	var totalQty;
	
	totalQty=(parseFloat(n[0])).toFixed(4);

totalQty = parseFloat(totalQty);	

	$("#stock").text(totalQty);
	
	
});
}else{
	$.post("productReturnFromFieldStaff_loadAvailableStockLalteer",{selectedProduct:selectedProduct,selectedWarehouse:selectedWarehouse,selectedUnit:selectedUnit,batchNo:batchNo},function(data){
		var n=data.split(",");
		var totalQty;
		
		totalQty=(parseFloat(n[0])).toFixed(4);

	totalQty = parseFloat(totalQty);	
		$("#stock").text(totalQty);
		
		
	});
	
}
}
	//reloadTable();
	//resetUnitData();
//availableStock(1);
	
	
	
	
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
function listAgent(call){
	$.post("productReturnFromFieldStaff_populateFieldStaff", {selectedWarehouse: call.value}, function (data) {
        var result = data;
        var arry = populateValues(result);
        document.form.selectedAgent.length = 0;
        addOption(document.form.selectedAgent, '<s:text name="txt.select"/>', "");

        for (var i = 0; i < arry.length; i++) {
            if (arry[i] !== "")
                addOption(document.form.selectedAgent, arry[i], arry[i]);
        }
    });
	
}


 function populateGroup(){

			var selectedAgent = document.getElementById("agentId").value;
			var availableGroup;
		/* 	$.post("productReturnFromFieldStaff_populateGroup",{selectedAgent:selectedAgent},function(data){
					if(data!=null && data!=""){
						document.getElementById("selAgentGroup").innerHTML=data;				
					}
					else{
						document.getElementById("selAgentGroup").innerHTML="";
					}
			}); */	 
			
			
			$.ajax({
				 type: "POST",
		        async: false,
		        url: "productReturnFromFieldStaff_populateGroup.action",
		        data: {selectedAgent:selectedAgent},
		        success: function(data) {
		        	if(data!=null && data!=""){
						document.getElementById("selAgentGroup").innerHTML=data;				
					}
					else{
						document.getElementById("selAgentGroup").innerHTML="";
					}
		        }
			});
			
			
		}  
 function populateWarehouse(){
	 var selectedAgent = document.getElementById("agentId").value;
		var availableGroup;
		/* $.post("productReturnFromFieldStaff_populateAgentWarehouse",{selectedAgent:selectedAgent},function(data){
				if(data!=null && data!=""){
					 var arry=data.split('~');
					document.getElementById("userWarehouse").innerHTML=arry[1];
					document.getElementById("warehouseId").value=arry[0];
				}
				else{
					document.getElementById("userWarehouse").innerHTML="";
					document.getElementById("warehouseId").value="";
				}
		}); */	 
		
		
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "productReturnFromFieldStaff_populateAgentWarehouse.action",
	        data: {selectedAgent:selectedAgent},
	        success: function(data) {
	        	if(data!=null && data!=""){
					 var arry=data.split('~');
					document.getElementById("userWarehouse").innerHTML=arry[1];
					document.getElementById("warehouseId").value=arry[0];
				}
				else{
					document.getElementById("userWarehouse").innerHTML="";
					document.getElementById("warehouseId").value="";
				}
	        }
		});
		
		
 }

   function populateUnit(){
	   var priceUnitLable="<s:text name='pricePerUnit'/>";
	var returnStockLable="<s:text name='returnStock'/>";
//alert(lable);
		var selectedCategory = document.getElementById("category").value;
		//alert(selectedCategory);
		var availableGroup;
		$.post("productReturnFromFieldStaff_populateUnit",{selectedCategory:selectedCategory},function(data){
				if(data!=null && data!=""){
					document.getElementById("productUnit").innerHTML=data;					
				}
				else{
					document.getElementById("pricePerUnit").innerHTML="";
				}
		});	   
	}  
   function populateUnitLalteer(){
	   var priceUnitLable="<s:text name='pricePerUnit'/>";
	var returnStockLable="<s:text name='returnStock'/>";
//alert(lable);
		var selectedCategory = document.getElementById("category").value;
		//alert(selectedCategory);
		var availableGroup;
		$.post("productReturnFromFieldStaff_populateUnitLalteer",{selectedCategory:selectedCategory},function(data){
			if(data!=null && data!=""){
				document.getElementById("productUnit").innerHTML=data;
			}
			else{
				document.getElementById("productUnit").innerHTML="";
			}
		});	   
	}  

   
   function populateProduct(call){	
		$.post("productReturnFromFieldStaff_populateProduct", {selectedWarehouse: call.value}, function (result) {
	    	 insertOptions("product1",JSON.parse(result));
	      
	    });
	    
	}

function populateSubCategory()
{
		var seasonCode =$("#season option:selected").val();	
		if(tenant!="pratibha"){
			var selectedWarehouse = document.getElementById("warehouse").value;
			}
			else{
				var selectedWarehouse=document.getElementById("agentId").value;
			}
	if(seasonCode!='' && selectedWarehouse!='')
		{
		$.ajax({
			 type: "POST",
	        //async: false,
	        url: "productReturnFromFieldStaff_populateSubCategory.action",
	        data: {selectedWarehouse:selectedWarehouse,seasonCode:seasonCode},
	        success: function(result) {
	        	//alert(result);
	        	insertOptions("category",JSON.parse(result));
	        }
		});
     /*  $.post("productReturnFromFieldStaff_populateSubCategory", {selectedWarehouse:selectedWarehouse,seasonCode:seasonCode}, function (result) {
      	 insertOptions("category",JSON.parse(result));
      }); */

	resetProductData();	 
	listProduct();
	
		}
}
function populateLalteerSubCategory(){
	var selectedWarehouse = document.getElementById("warehouse").value;
	if(selectedWarehouse!='')
		{
	
      $.post("productReturnFromFieldStaff_populateLalteerSubCategory", {selectedWarehouse:selectedWarehouse}, function (result) {
      	 insertOptions("category",JSON.parse(result));
      });

	resetProductData();	   
	
	listProduct();
	
		}
}


function listBatchNo()
{
	if(tenant!="lalteer"){
	var season=document.getElementById("season").value;
     }else{
    	 var season=season;
	}
	if(tenant!="pratibha"){
		var warehouse=document.getElementById("warehouse").value;
		}
	else{
			var warehouse="";
		}
	
	
	var product=document.getElementById("product1").value;
		
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "productReturnFromFieldStaff_populateBatchNo.action",
	        data: {selectedWarehouse:warehouse,selectedSeason:season,selectedProduct:product},
	        success: function(result) {
	        	insertOptions("batchNo",JSON.parse(result));
	        }
		});
		
}
function listProduct()
{
	if(tenant!="pratibha"){
		var selectedWarehouse = document.getElementById("warehouse").value;
		}
	else{
		var selectedWarehouse=document.getElementById("warehouseId").value;
		}
	
	var category=document.getElementById("category").value;
	
	var agent=document.getElementById("agentId").value;
	
	var season=document.getElementById("season").value;
	
	if(selectedWarehouse.trim()!='' && category.trim()!=''){
		
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "productReturnFromFieldStaff_populateProductBasedOnCategory.action",
	        data: {selectedCategory:category,selectedWarehouse:selectedWarehouse,selectedAgent:agent,seasonCode:season},
	        success: function(result) {
	        	 insertOptions("product1",JSON.parse(result));
	        }
		});
		
		resetProductDetails();	
	}
	else{
		$("#product1").html('<option value=""><s:text name="txt.select"/></option>');
		$("#batchNo").html('<option value=""><s:text name="txt.select"/></option>');
		$("#category").html('<option value=""><s:text name="txt.select"/></option>');
		//document.getElementById("validateError").innerHTML="please Select Warehouse";
	}
}

function listProductLalteer()
{
	var selectedWarehouse = document.getElementById("warehouse").value;
	var category=document.getElementById("category").value;
	if(selectedWarehouse.trim()!='' && category.trim()!=''){
		
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "productReturnFromFieldStaff_populateProductBasedOnCategoryLalteer.action",
	        data: {selectedCategory:category,selectedWarehouse:selectedWarehouse},
	        success: function(result) {
	        	 insertOptions("product1",JSON.parse(result));
	        }
		});
		
		resetProductDetails();	
	}
	else{
		$("#product1").html('<option value=""><s:text name="txt.select"/></option>');
		$("#batchNo").html('<option value=""><s:text name="txt.select"/></option>');
		$("#category").html('<option value=""><s:text name="txt.select"/></option>');
		//document.getElementById("validateError").innerHTML="please Select Warehouse";
	}
}
   
function enableButton(){
			jQuery(".save-btn").prop('disabled',false);
		}
function resetProductDetails()
{
	document.getElementById('product1').selectedIndex = "";
	document.getElementById('stock').innerHTML = "";
	document.getElementById('returnStock').value = "";
	
	if($("#enableBatchNo").val()=='1'){
		jQuery('#batchNo').prop("batchNo",false);
		document.getElementById('batchNo').selectedIndex = ""; 
		jQuery("#batchNo").val('');
		jQuery("#batchNo").select2();
		}
		
		
}
function redirectForm()
{
	document.redirectform.action="productReturnFromFieldStaff_list.action";
	document.redirectform.submit();
}
function resetWarehouseData()
{
	resetProductData();
	resetUnitData();
    resetEditFlag();
    if(productsInfoArray.length != 0)
       {
       for(var i=0; i< productsInfoArray.length; i++){
			  productsInfoArray.splice(i);
			  }
			  
			}
			reloadTable(); 
			
}
function resetPrefixAndSuffix(){
	 jQuery("#returnStock").val("0");
	 jQuery("#stockPaise").val("000");
}


function resetSeasonData(){
	document.getElementById("agentId").selectedIndex=0;
	if(tenant!="pratibha"){
	document.getElementById("warehouse").selectedIndex=0;
	}
	document.getElementById("product1").selectedIndex=0;
	//document.getElementById("startDate").value='<s:property value="currentDate" />';
	resetProductData();
	jQuery("#agentId").focus();
	jQuery("#warehouse").focus();
	
	var idsArray=['agentId'];
	resetSelect2(idsArray);
	
	
}


function resetSelect2(){
	$(".select2").select2();
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
		
</script>
<script type="text/javascript">
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
document.getElementById("agentId").selectedIndex=0;
if(tenant!="pratibha"){
document.getElementById("warehouse").selectedIndex=0;
}
document.getElementById("product1").selectedIndex=0;
document.getElementById("season").selectedIndex=0;
//document.getElementById("category").selectedIndex=0;

//document.getElementById("startDate").value='<s:property value="currentDate" />';
$('#startDate').datepicker('setDate', new Date());
resetProductData();
jQuery("#agentId").focus();
jQuery("#warehouse").focus();
//jQuery("#product1").val("");
</script>

<s:form action="productReturnFromFieldStaff_populatePrintHTML"
	id="receiptForm" method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>
	<s:hidden value="514" name="distTxnType"></s:hidden>

</s:form>

<s:form name="redirectform"
	action="productReturnFromFieldStaff_list.action" method="POST">
	<%-- <s:textfield name="hidden" id="distributionFS"></s:textfield> --%>
</s:form>
<%-- <jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>