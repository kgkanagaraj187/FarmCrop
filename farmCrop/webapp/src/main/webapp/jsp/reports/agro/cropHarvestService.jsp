<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout -->
<meta name="decorator" content="swithlayout">


</head>


<font color="red"> <s:actionerror /></font>
<div id="orderNoValidat" style="color: red;"></div>

<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" name="cropSupply.id" />
	<s:hidden key="command" />
	<s:hidden key="temp" id="temp" />

	<s:hidden key="productTotalString" id="productTotalString" />

	<font color="red"> <s:actionerror /></font>

	<div class="appContentWrapper marginBottom ">
		<div class="formContainerWrapper">
			<div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<div class="error pull-left">
							<p class="notification">
								<%-- <span class="manadatory">*</span>
								<s:text name="reqd.field" /> --%>
								&nbsp;&nbsp;
							<div id="validateError" style="text-align: center;"></div>
							</p>
						</div>
						<div class="pull-right">
							<b><s:text name="season" /> :</b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>
					</div>
				</div>
			</div>


			<h2>
				<s:text name="info.general" />
			</h2>
			<div class="flexiWrapper filterControls">

				<div class="flexi flexi10">
					<label for="txt"><s:text name="harvestDate" /><span
						class="manadatory">*</span></label>

					<div class="form-element">
						<s:if test="currentTenantId=='chetna'">
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple" size="20"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years" cssClass="date-picker form-control" />
						</s:if>
						<s:else>
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple" size="20"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years" cssClass="date-picker form-control" />
						</s:else>

					</div>
				</div>


				<div class="flexi flexi10">
					<label for="txt"><s:property
							value="%{getLocaleProperty('village.name')}" /> <span
						class="manadatory">*</span></label>
					<div class="form-element">
						<s:select id="village" name="selectedVillage" headerKey=""
							headerValue="%{getText('txt.select')}" list="villageList"
							cssClass="form-control select2" theme="simple"
							onchange="listFarmers(this);" disabled="false" />
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:property value="%{getLocaleProperty('farmerName')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedFarmer" list="farmers" listKey="id"
							listValue="name" headerKey=""
							headerValue="%{getText('txt.select')}" theme="simple" id="farmer"
							cssClass="form-control select2" onchange="listFarms(this)" />
					</div>
				</div>
				<div class="flexi flexi10">
					<label for="txt"><s:text name="farmName" /> <sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedFarm" list="farms" listKey="id"
							listValue="name" headerKey=""
							headerValue="%{getText('txt.select')}" theme="simple" id="farm"
							cssClass="form-control select2" onchange="resetProductData();" />
					</div>
				</div>
				<s:if test="currentTenantId=='chetna'">
					<div class="flexi flexi10">
						<label for="txt"><s:text name="storageIn" /></label>
						<div class="form-element">
							<s:select cssClass="form-control " id="storageId"
								list="storageId" name="cropHarvest.storageIn" headerKey=" "
								headerValue="%{getText('txt.select')}"
								onChange="processStorageType(this.value);" theme="simple" />
						</div>

						<div class="storageIdOther">
							<s:textfield id="storageIdOtherVal"
								name="cropHarvest.otherStorageInType"
								cssClass="form-control selecct2" maxlength="45" tabindex="11"
								cssStyle="margin-top:1%" />
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:text name="packedIn" /></label>
						<div class="form-element">
							<s:select cssClass="form-control " id="packedId" list="packId"
								name="cropHarvest.packedIn" headerKey=""
								headerValue="%{getText('txt.select')}"
								onChange="processPackedType(this.value);" theme="simple" />

						</div>

					</div>
					<div class="packedIdOther flexi flexi10">
						<label for="txt"></label>
						<div class="form-element">
							<s:textfield id="packedIdOtherVal"
								name="cropHarvest.otherPackedInType" cssClass="form-control "
								maxlength="45" tabindex="11" cssStyle="margin-top:1%" />
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
						<s:text name="info.cropDetails" />

					</h2>
					<div class="flexiWrapper filterControls">
						<div class="flexi flexi10" style="width: 175px;">
							<label for="txt"><s:text
									name="%{getLocaleProperty('cropType')}" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedCropType"
									cssClass="form-control select2" list="cropTypeList"
									headerKey="" headerValue="%{getText('txt.select')}"
									id="cropType" onchange="listCropType(this);" />
							</div>
						</div>
						<div class="flexi flexi10 serviceFldSet" style="width: 175px;">
							<label for="txt"><s:text name="cropName" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedCropName"
									cssClass="form-control select2" list="cropNamesList"
									headerKey="" headerValue="%{getText('txt.select')}"
									id="cropName"
									onchange="listCrops(this);resetDatas();loadUnit();" />
							</div>
						</div>

						<div class="flexi flexi10 serviceFldSet" style="width: 175px;">
							<label for="txt"><s:text name="variety" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedVariety" cssClass="form-control select2"
									list="varietysList" headerKey=""
									headerValue="%{getText('txt.select')}" id="variety"
									onchange="listVariety(this.value); sowingDate(this.value)" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:text name="SowingDate" /></label>
							<div class="form-element">
								<div id="sowingDate"></div>
							</div>
						</div>
						
						<div class="flexi flexi10" style="width: 175px;">
							<label for="txt"><s:text name="grade" /><sup
									style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select
										name="selectedGrade" list="gradesList" headerKey=""
										headerValue="%{getText('txt.select')}" id="grade"
										cssClass="form-control select2"/>
							</div>
						</div>
						
						<div class="flexi flexi10" style="width:115px">
							<label for="txt"><s:text name="unit" /></label>
							<div class="form-element">
								<div id="productUnit"></div>
							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:text
										name="%{getLocaleProperty('quantitykg')}" /><sup
									style="color: red;">*</sup></label>
							<div class="form-element">
							<s:textfield id="quantity" name="quantity" maxlength="10"  onkeyup="calToTonnes()"
										onkeypress="return isDecimal(event)" disabled="false"
										cssClass="form-control" />
									<div id="validateProdQtyError"
										style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
							</div>
						</div>
						
						<div class="flexi flexi10 hide">
							<label for="txt"><s:text
										name="quantityTonnes" /></label>
							<div class="form-element">
							<div id="qtyInTonnes"></div>
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:text
										name="action" /></label>
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

			<div class="appContentWrapper marginBottom filter-background">
				<div class="flexiWrapper filterControls">

				<%-- 	<table id="productInfoTbl"
						class="table table-bordered aspect-detail">

						<thead>
							<tr class="odd">
								<th width="5%"><s:text name="s.no" /></th>
									<th width="7%"><s:text name="cropType" /><sup style="color: red;">*</sup></th>
								<th width="8%"><s:text name="cropName" /><sup style="color: red;">*</sup></th>
									<th width="8%">	<s:text name="variety" /><sup style="color: red;">*</sup></th>
								<th width="30%"><s:text name="grade" /><sup
									style="color: red;">*</sup></th>
								<th width="20%"><s:text name="unit" /></th>
								<th class="hide" width="20%"><s:text
										name="%{getLocaleProperty('prices')}"/><sup
									style="color: red;">*</sup></th>
								<th width="15"><s:text
										name="%{getLocaleProperty('quantitykg')}" /><sup
									style="color: red;">*</sup></th>
								<th width="10%" style="display: none;"><s:text
										name="quantityTonnes" /></th>
								<th class="hide" width="6%"><s:text
										name="%{getLocaleProperty('totalPrice')}" /></th>
								<th width="30%" colspan="4"><s:text
										name="action" /></th>
							</tr>

							<tr>
								<td></td>

								<td style="padding-right: 10px !important;"><s:select
										name="selectedGrade" list="gradesList" headerKey=""
										headerValue="%{getText('txt.select')}" id="grade"
										cssClass="form-control select2"/></td>

								<td><div id="productUnit"></div></td>

								<td class="hide" style="text-align: center; padding-right: 10px !important;">
									<s:textfield id="sellPrice" name="sellPrice" maxlength="7"
										size="30" onkeypress="return isDecimal(event)"
										onkeyup="calTotalQty(this); " cssClass="form-control" disabled="false"/>

									<div id="validatePriceError"
										style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

								</td>

								<td style="text-align: center; padding-right: 10px !important;">
									<s:textfield id="quantity" name="quantity" maxlength="10"
										onkeypress="return isDecimal(event)"
										onkeyup="calTotalQty(this); calToTonnes();" disabled="false"
										cssClass="form-control" />
									<div id="validateProdQtyError"
										style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
								</td>

								<td class="alignRight"
									style="text-align: center; padding-right: 10px !important; display: none">
									<div id="qtyInTonnes" style="display: none"></div>
								</td>

								<td class="alignRight hide"
									style="text-align: center; padding-right: 10px !important;">
									<div id="totalPrice"></div>
								</td>
								<td colspan="4" class="alignCenter" style="" align="center">
									<table class="actionBtnWarpper">

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
									</table>
								</td>
							</tr>

						</thead>
					</table> --%>
					
					
					<table id="productInfoTbl1"
								class="table table-bordered aspect-detail">
								<thead>
									<tr class="odd">
										<th width="5%"><s:text name="s.no" /></th>
										<th width="15%"><s:text name="cropType" /></th>
										<th width="15%"><s:text name="cropName" /></th>
										<th width="15%"><s:text name="variety" /></th>
										<th width="15%"><s:text name="grade" /></th>
										<th width="10%"><s:text name="unit" /></th>
										<th class="hide" width="13%"><s:text
												name="%{getLocaleProperty('prices')}" /></th>
										<th width="10%"><s:text
												name="%{getLocaleProperty('quantitykg')}" /></th>
										<th width="10%" style="display: none"><s:text
												name="quantityTonnes" /></th>
										<th class="hide" width="10%"><s:text name="%{getLocaleProperty('totalPrice')}" /></th>
										<th width="20%">Action</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="8" style="text-align: center;"><s:text
												name="noRecordFound" /></td>
									</tr>
								</tbody>
								<tfoot>
									<tr>
										<td colspan="5" style="border: none !important;">&nbsp;</td>
										<td style="border: none !important;">Total Quantity</td>
										<td
											style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important; text-align: right;">0.00</td>
										<td style="border: none !important;">&nbsp;</td>
									</tr>
								</tfoot>
							</table>

					<div class="yui-skin-sam" id="loadList" style="display: block">
						<sec:authorize
							ifAllGranted="service.report.cropHarvestReport.create">
							<span id="savebutton" class=""> <span class="first-child">
									<button id="submitButton" type="button"
										class="save-btn btn btn-success"
										onclick="event.preventDefault();submitCropHarvest();" id="sucessbtn">
										<font color="#FFFFFF"> <b><s:text
													name="save.button" /></b>
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
					<br />
				</div>

			</div>
		</div>
	</div>
	</div>
	<s:hidden id="productList" name="warehouseProductList" />
</s:form>

<s:form name="listForm" id="listForm"
	action="cropHarvestServiceReport_list">
	<s:hidden name="currentPage" />
</s:form>



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
            <span><h5><s:text name="cropharvestMsg" /></h5></span>
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

<s:form name="redirectform" action="cropHarvestServiceReport_list"
	method="POST">
</s:form>


<script src="plugins/jquery-input-mask/jquery.inputmask.bundle.min.js"></script>
<script type="text/javascript">

var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();
var sowing=new Array();

$("#village").val("");

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

jQuery(document).ready(function(){
	//$("#").inputmask({"alias": "currency","prefix":""});
	jQuery(".storageIdOther").hide();
	jQuery(".packedIdOther").hide();	
	$("#sucessbtn").prop('disabled', false);
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
$("#").inputmask({"alias": "currency","prefix":""});

$("#submitButton").on('click', function (event) {  
    event.preventDefault();
    var el = $(this);
    el.prop('disabled', true);
    setTimeout(function(){el.prop('disabled', false); }, 1000);
});


});
var printWindowCnt=0;
var windowRef;




function resetUnitData(){
	document.getElementById("validateError").innerHTML="";

}


function reload() {
	addRow();
	resetProductData();
}

function loadUnit(){
	
	var selectedCrop = document.getElementById("cropName").value;
	$.post("cropHarvestServiceReport_populateUnit",{selectedCrop:selectedCrop},function(data){
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
			}
		});
	});
	
	/* 
	var selectedGrade = document.getElementById("grade").value;
	
	var availableUnit;

	$.post("cropHarvestServiceReport_populateGradeUnit",{selectedGrade:selectedGrade},function(data){
	
		var jsonData = $.parseJSON(data);
		$.each(jsonData, function(index, value) {
			if(value.id=="unit"){
				document.getElementById("productUnit").innerHTML=value.name;
			}
		});
	}); */
	
}

/* function printReceipt(receiptNo){	
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
} */

function resetData(){
	document.getElementById('village').selectedIndex = "";
	document.getElementById("farmer").selectedIndex="";
	
	//document.getElementById('stock').value = "";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("farm").innerHTML="";
	document.getElementById("cropType").value="";
	document.getElementById("cropName").value="";
	document.getElementById("variety").innerHTML="";
	document.getElementById("grade").innerHTML="";
	document.getElementById("quantity").innerHTML="";
	//document.getElementById('sellPrice').selectedIndex = "";
	document.getElementById('totalYieldPrice').selectedIndex = "";
	//document.getElementById("costPrice").value="";
	productsInfoArray = new Array();
	pArrayList = new Array();
	productTotalArray=new Array();
	productNameArray=new Array();
	reloadTable();
}


function processStorageType(val){
				
		if (val == "99") {	
			
			jQuery(".storageIdOther").show();
			} else {  
				jQuery("#storageIdOtherVal").val("");
				jQuery(".storageIdOther").hide();
			}
		}

function processPackedType(val)
{
	if (val == "99") {	
		
		jQuery(".packedIdOther").show();
		} else {  
			jQuery("#packedIdOtherVal").val("");
			jQuery(".packedIdOther").hide();
		}
	}

function addRow(){

	var editIndex = getEditIndex();
	var selectedVillage = $('#village').val();
	var selectedFarmer = $('#farmer').val();
	var selectedFarm = $('#farm').val();
	var selectedCropType = $("#cropType").val();
	var selectedCropName=$("#cropName").val();
	var selectedVariety = $("#variety").val();
	var selectedGrade = $("#grade").val();
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	//var sellPrice=document.getElementById("sellPrice").value;
	var quantity = document.getElementById("quantity").value;
	 var cropTypeName =$("#cropType option:selected").text();
	var cropNames =$("#cropName option:selected").text();
	var varietyName =$("#variety option:selected").text();
	var gradeName =$("#grade option:selected").text(); 
	
	/* var coPricePrefix = document.getElementById("costPriceRupees").value;
	var coPriceSuffix = document.getElementById("costPricePaise").value;
	var costPricePreSuf = coPricePrefix +"."+coPriceSuffix; */
  var currentDate = new Date();
  var currentMonth = currentDate.getMonth()+1;
  var currentYear = currentDate.getFullYear();
  //var packedId=jQuery("#storageId").val();
  //var storage=jQuery("#packedId").val();
  var packedId=jQuery("#packedId").val();
  var storage=jQuery("#storageId").val();
  var otherPackedInType=jQuery("#packedIdOtherVal").val();
  var otherStorageInType=jQuery("#storageIdOtherVal").val();
  var qtyT = document.getElementById("qtyInTonnes").innerHTML;
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
	
 /*   if(sellPrice == "" || sellPrice == null)
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.price"/>';
		return false;
	}
	 */
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
						 qtyT :qtyT,
						 //sellPrice : sellPrice,
						 //tAmt : parseFloat(quantity*sellPrice).toFixed(2),
						 isEdit:false
			     };


				 if(!productExists)
				 productsInfoArray[productsInfoArray.length] = productArray;
		 
				} else {
					
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
							 qtyT :qtyT,
							 //sellPrice : sellPrice,
							 //tAmt : parseFloat(quantity*sellPrice).toFixed(2),
							 isEdit:false
				     };
					
					productsInfoArray[editIndex]=productArray;
				 /*editproductTotalArray(productArray); //NOTE: Commented to fix bug 542.
				  productsInfoArray[editIndex].cropType= selectedCropType;
					productsInfoArray[editIndex].cropName= selectedCropName;
					productsInfoArray[editIndex].variety= selectedVariety;
					productsInfoArray[editIndex].grade= selectedGrade;
					productsInfoArray[editIndex].sellPrice = sellPrice;
					productsInfoArray[editIndex].quantity = quantity;
					productsInfoArray[editIndex].tAmt = parseFloat(quantity*sellPrice).toFixed(2);
					productsInfoArray[editIndex].isEdit=false;
					productArray=productsInfoArray[editIndex]; */
					
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
	//document.getElementById("sellPrice").value=productsInfoArray[indx].sellPrice;
	document.getElementById('quantity').value = productsInfoArray[indx].quantity;
	document.getElementById('qtyInTonnes').innerHTML = productsInfoArray[indx].qtyT;
	//document.getElementById('totalPrice').innerHTML = productsInfoArray[indx].tAmt;
	resetEditFlag();
		productsInfoArray[indx].isEdit=true;
}

/* function calulateAvailableStock(){	
	var selectedProduct = document.getElementById('product1').value;
	var stock = document.getElementById('stock').value;
} */
function reloadTable(){
	var tbodyRow = "";
	var tfootArray = new Array();
	//jQuery('#productInfoTbl1').removeClass('hide');
	jQuery('#productInfoTbl1 > tbody').html('');
	
	
	var rowCount=0;
	for(var cnt=0; cnt<productsInfoArray.length; cnt++){
			
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		
		tbodyRow += '<tr>'+
					'<td style="text-align:center;">'+(cnt+1)+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].cropTypeName+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].cropNames+'</td>'+
					'<td style="text-align:center;">'+productsInfoArray[cnt].varietyName+'</td>'+
					'<td style="text-align :center;">'+productsInfoArray[cnt].gradeName+'</td>'+
					'<td style="text-align :center;">'+productsInfoArray[cnt].unitName+'</td>'+
					/* '<td style="text-align:right;">'+productsInfoArray[cnt].sellPrice+'</td>'+ */
					'<td style="text-align:right;">'+productsInfoArray[cnt].quantity+'</td>'+
					'<td style="text-align:right; display: none">'+productsInfoArray[cnt].qtyT+'</td>'+
					/* '<td style="text-align:right;">'+productsInfoArray[cnt].tAmt+'</td>'+ */
					'<td colspan="2" class="alignCenter" ><button type="button" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')" title="<s:text name="Edit" />"></button>'+
					'<button type="button" class="fa fa-trash" aria-hidden="true " onclick="removeRow('+cnt+')" title="<s:text name="Delete" />"></button></td>'+
					'</tr>';
					
		if(tfootArray.length==
			0){
			tfootArray[tfootArray.length] = {
											 totalPrice : productsInfoArray[cnt].quantity,
										 	};
			
		} else { 
		  tfootArray[tfootArray.length-1].totalPrice=(parseFloat(tfootArray[tfootArray.length-1].totalPrice)+parseFloat(productsInfoArray[cnt].quantity));
		}
		

		
	 }	
	}	
	
	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="13" style="text-align:center"><s:text name="noRecordFound"/></td>'+
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
 	tfootRow += '<tr>'+
	'<td colspan="5" style="border:none!important;">&nbsp;</td>'+
	'<td class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="totalQty"/></td>';

	
	if(tfootArray.length>0){
		tfootRow +=		'<td class="alignRight" id="totalYieldPrice" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+parseFloat(tfootArray[tfootArray.length-1].totalPrice).toFixed(2)+'</td>';
}else{
	 tfootRow +='<td class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">0.00</td>';
}

	
	tfootRow +='<td style="border:none!important;">&nbsp;</td>'+
			  '</tr>'; 


			  
jQuery('#productInfoTbl1 > tfoot').html(tfootRow);
jQuery("#product1").focus();
}

function removeRow(indx){
	
	for(var i=0;i<productTotalArray.length;i++){
	  if(productTotalArray[i].name==productsInfoArray[indx].cropTypeName+'='+productsInfoArray[indx].cropNames || productTotalArray[i].cropType==productsInfoArray[indx].cropType){
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

//Method to remove product rows while farmer and village is changed.
function clearRows(){
	var len = productsInfoArray.length;	
		productsInfoArray.splice(0,len);	
	resetProductData();
	reloadTable();	
}

function buildReqProductObject() {
	
	pArrayList = new Array();	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
	pArrayList[pArrayList.length] =	productsInfoArray[cnt].name.split('=')[0]+'|'+productsInfoArray[cnt].stock;
	}
}


function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}
function submitCropHarvest() {
	$("#sucessbtn").prop('disabled', true);
	var hit=true;		
	var editIndex = getEditIndex();
	var selectedVillage = $('#village').val();
	var selectedFarmer = $('#farmer').val();
	var selectedFarm = $('#farm').val();
	/* var coPricePrefix = document.getElementById("costPriceRupees").value;
	var coPriceSuffix = document.getElementById("costPricePaise").value;
	var costPricePreSuf = coPricePrefix +"."+coPriceSuffix; */
	var harvestDate= $('#startDate').val();
	
  var currentDate = new Date();
  var currentMonth = currentDate.getMonth()+1;
  var currentYear = currentDate.getFullYear();
  var packedId=jQuery("#packedId").val();
  var storage=jQuery("#storageId").val();
  var otherPackedInType=jQuery("#packedIdOtherVal").val();
  var otherStorageInType=jQuery("#storageIdOtherVal").val();
  
  if(harvestDate == "" || harvestDate == null) {
  		document.getElementById("validateError").innerHTML='<s:text name="empty.addedDate"/>';
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
	 if(harvestDate == "" || harvestDate == null) {
 		document.getElementById("validateError").innerHTML='<s:text name="empty.addedDate"/>';
 		hit=false;
 		enableButton();
 		return false;
 	}
   
   if(productsInfoArray.length==0) {
 		document.getElementById("validateError").innerHTML='<s:text name="noRecordFoundCropHarvest"/>';
 		hit=false;
 		enableButton();
 		return false;
 	}
   
 
   
	if(hit){
		var productTotalString=' ';
		for(var i=0;i<productsInfoArray.length;i++){
			
			productTotalString+=productsInfoArray[i].cropType+"#"+productsInfoArray[i].cropName+"#"+productsInfoArray[i].variety+"#"+productsInfoArray[i].grade+"#"+'0.00'+"#"+productsInfoArray[i].quantity+"|";
			
		 }
		$.post("cropHarvestServiceReport_create.action",{selectedVillage:selectedVillage,selectedFarmer:selectedFarmer,selectedFarm:selectedFarm,productTotalString:productTotalString,harvestDate:harvestDate,storage:storage,packed:packedId,otherStorageInType:otherStorageInType,otherPackedInType:otherPackedInType
		},function(data,result){
				//alert(result);
				if(result=='success')
					{
						document.getElementById("divMsg").innerHTML="";
						document.getElementById("enableModal").click();
						//disablePopupAlert();
						//enablePopupAlert();
						
					}
				
	});
		
		}
	else{
		$("#sucessbtn").prop('disabled', false);
	}
}

function enableButton(){
jQuery(".save-btn").prop('disabled',false);
}

//function disableButton(){
	//	jQuery(".save-btn").attr('disabled',true);
//}
function cancel() {
	document.form.action = "cropHarvestServiceReport_list.action";
	document.listForm.submit();
}

function resetProductData(){
	resetEditFlag();
	var idsArray=['cropType','cropName','variety','grade'];
	resetSelect2(idsArray);
	document.getElementById("validateError").innerHTML="";
	document.getElementById('qtyInTonnes').innerHTML="";
	//document.getElementById("sellPrice").value="";
	document.getElementById('quantity').value = "";
	//document.getElementById('totalPrice').innerHTML = "";
	
} 

function resetDatas(){
	var idsArray=['variety','grade'];
	resetSelect2(idsArray);
	document.getElementById("productUnit").innerHTML="";
	document.getElementById("sowingDate").innerHTML="";
	//document.getElementById("sellPrice").value="";
	document.getElementById('quantity').value = "";
	//document.getElementById('totalPrice').innerHTML = "";
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

 /* $('#sellPrice').keypress(function(event) {
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
	}); */
 
 
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





/* function calTotalQty(call){
	
	//var val1 = (isNaN(parseInt($("#quantity").val()))) ? 0 : parseInt($("#quantity").val());
	var val1 = (isNaN(parseFloat($("#quantity").val()))) ? 0.0 : parseFloat($("#quantity").val());
	var val2 = (isNaN(parseFloat($("#sellPrice").val()))) ? 0.0: parseFloat($("#sellPrice").val());

	var sum = val1 * val2;
	document.getElementById("totalPrice").innerHTML=sum.toFixed(2);;
	
	
} */

function calToTonnes(){
	var qtyInKgs = $("#quantity").val();
	
	var qtyInTonnesVal = qtyInKgs/1000;
	//alert(qtyInTonnesVal);
	document.getElementById('qtyInTonnes').innerHTML = qtyInTonnesVal.toFixed(3);
}

function disablePopupAlert(){
	
	document.redirectform.action="cropHarvestServiceReport_list.action";
	document.redirectform.submit();
}


function redirectForm()
{
	document.redirectform.action="cropHarvestServiceReport_create.action";
	document.redirectform.submit();
}


function listFarmers(obj){
	var selectedVillage = $('#village').val();
	var idsArray=['farmer','farm'];
	resetSelect2(idsArray);
	jQuery.post("cropHarvestServiceReport_populateFarmer.action",{id:obj.value,dt:new Date(),selectedVillage:obj.value},function(result){
		insertOptions("farmer",JSON.parse(result));
		//listFarms(document.getElementById("farm"));
	});	
}	


function listFarms(obj){
	var selectedFarmer = $('#farmer').val();
	var idsArray=['farm'];
	resetSelect2(idsArray);
	jQuery.post("cropHarvestServiceReport_populateFarm.action",{id:obj.value,dt:new Date(),selectedFarmer:obj.value},function(result){
		insertOptions("farm",JSON.parse(result));
	});
	//clearRows();
}


function listCropType(obj){
	var idsArray=['cropName','variety','grade'];
	resetSelect2(idsArray);
	var selectedCropType = $('#cropType').val();
  var farm= $('#farm').val();
	document.getElementById('quantity').value = "";
	//document.getElementById('totalPrice').innerHTML = "";
	//document.getElementById("sellPrice").value="";
	document.getElementById("qtyInTonnes").innerHTML="";	
	document.getElementById("sowingDate").innerHTML="";
	/* jQuery.post("cropSaleEntryReport_populateCropName.action",{id:obj.value,dt:new Date(),selectedCropType:obj.value,selectedFarm:farm},function(result){
		insertOptions("cropName",JSON.parse(result));
		listCrops(document.getElementById("cropName"));
	}); */
	
	
	$.ajax({
		 type: "POST",
    async: false,
    url: "cropHarvestServiceReport_populateCropName.action",
    data: {id:obj.value,dt:new Date(),selectedCropType:obj.value,selectedFarm:farm},
    success: function(result) {
    	insertOptions("cropName",JSON.parse(result));
  		listCrops(document.getElementById("cropName"));
    }
	});
	
}

function listCrops(obj){
	
	var selectedCropType = $('#cropType').val();
	var selectedCropName = $('#cropName').val();
	var selectedFarm= $('#farm').val();
	document.getElementById("sowingDate").innerHTML="";
	/* jQuery.post("cropSaleEntryReport_populateVarietyName.action",{id:obj.value,dt:new Date(),selectedCropName:obj.value},function(result){
		insertOptions("variety",JSON.parse(result));
		listVariety(document.getElementById("variety"));
	}); */
	 sowing = new Array();
	
	$.ajax({
		 type: "POST",
    async: false,
    url: "cropHarvestServiceReport_populateVarietyName.action",
    data: {id:obj.value,dt:new Date(),selectedCropName:obj.value,selectedCropType:selectedCropType,selectedFarm:selectedFarm},
    success: function(result) {
  	 	
  	  var sow = new Object();
  	 
   	 // insertOptions("variety",JSON.parse(result));
   	 document.getElementById("variety").length = 0;
	addOption(document.getElementById("variety"), '<s:text name="txt.select"/>', "0");
 			listVariety(document.getElementById("variety").value);
var jsonArr = JSON.parse(result)
  			for (var i = 0; i < jsonArr.length; i++) {
  				sow.id = jsonArr[i].id;
  				sow.name = jsonArr[i].name.split('~')[1];
  				sowing.push(sow);
  				//alert("sow"+jsonArr[i].name);
  				//alert( jsonArr[i].name.split('~')[0]);
  				addOption(document.getElementById("variety"), jsonArr[i].name.split('~')[0],
  						jsonArr[i].id);
  			}
  			
    }
	});
}

function insertOptions(ctrlName, jsonArr) {
	document.getElementById(ctrlName).length = 0;
	addOption(document.getElementById(ctrlName), '<s:text name="txt.select"/>', "0");
	for (var i = 0; i < jsonArr.length; i++) {
		addOption(document.getElementById(ctrlName), jsonArr[i].name,
				jsonArr[i].id);
	}
}


function sowingDate(val){
	/* var obj = $.grep(sowing, function(e){ return e.id == val; }); */
document.getElementById("sowingDate").innerHTML="";
	for(var i = 0, len = sowing.length; i < len; i++) {
		var obj = sowing[i];

    if (obj.id == val){
   	$('#sowingDate').html(obj.name);
    	break;
    }
  }
	/* var obj = sowing.map(function(e) { return e.name; }).indexOf(val);
	alert("obj:"+obj);
	if(obj!=undefined){
		$('#sowingDate').html(obj.name);
	} */

	 
}

function listVariety(obj){
	var selectedVariety = $('#variety').val();
	/* jQuery.post("cropSaleEntryReport_populateGradeName.action",{id:obj.value,dt:new Date(),selectedVariety:obj.value},function(result){
		insertOptions("grade",JSON.parse(result));
		listGrade(document.getElementById("grade"));
	});
	
	 */

	 
	$.ajax({
		 type: "POST",
   async: false,
   url: "cropHarvestServiceReport_populateGradeName.action",
   data: {id:obj.value,dt:new Date(),selectedVariety:obj},
   success: function(result) {
  	 // console.log("0000"+result);
  	 insertOptions("grade",JSON.parse(result));
  	// console.log("111111111111111");
   }
	}); 
}





</script>
<s:form action="distribution_populatePrintHTML" id="receiptForm"
	method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNumber"></s:hidden>

</s:form>
<script type="text/javascript">


 document.getElementById("startDate").value='<s:property value="currentDate" />';
resetProductData();
jQuery("#village").focus();

function distributionEnable(){
	window.location.href="cropHarvestServiceReport_create.action";
	resetProductData();
	resetDatas();
}


</script>

