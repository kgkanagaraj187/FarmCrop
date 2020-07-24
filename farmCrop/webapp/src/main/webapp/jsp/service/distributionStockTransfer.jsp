<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
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

.pdalign {
	text-align: center;
	padding-top: 21px;
}

.column-1 h3, .column-2 h3, .column-3 h3 {
	/*background: none repeat scroll 0 0 #6F9505 !important;*/
	border: 1px solid #fff !important;
	color: #fff !important;
	padding: 5px;
	margin: 0;
	text-align: left;
}

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
	action="distributionStockTransfer_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" />
	<s:hidden key="command" />
	<div>
		<div class="appContentWrapper marginBottom ">
			<div class="formContainerWrapper">
				<div class="row">
					<div class="container-fluid">
						<div class="notificationBar">
							<div class="error">
								<p class="notification">
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
					<div class="flexi flexi12" style="width: 130px">
						<label for="txt"><s:text name="distributionDate" /></label>
						<div class="form-element">
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years"
								cssClass="date-picker form-control input-sm" size="20" />
						</div>
					</div>
						<div class="flexi flexi12 season" style="width: 130px">
						<label for="txt"><s:text
								name="%{getLocaleProperty('season')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="season" list="seasonList"
								headerKey="" headerValue="%{getText('txt.select')}"
								onchange="resetValues();" theme="simple" id="season"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="flexi flexi12 senderWarhouse" style="width: 130px">
						<label for="txt"><s:text
								name="%{getLocaleProperty('senderWarehouse')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="senderWarehouse" list="senderWarehouseList"
								onchange="populateCategory();validatewarehouse();"
								headerKey="" headerValue="%{getText('txt.select')}"
								theme="simple" id="senderWarehouse"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="flexi flexi12 receiverWarhouse" style="width: 130px">
						<label for="txt"><s:text
								name="%{getLocaleProperty('receiverWarhouse')}" /><span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:select name="receiverWarhouse" list="receiverWarehouseList"
								headerKey="" headerValue="%{getText('txt.select')}"
								theme="simple" id="receiverWarhouse" onchange="validatewarehouse();"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					<div class="flexi flexi12 truckId" style="width:130px">
						<label for="txt"><s:text name="truckId" /></label>
						<div class="form-element">
							<s:textfield id="truckId" name="truckId"
								cssClass="form-control input-sm" maxlength="20"	/>
						</div>
					</div>
					<div class="flexi flexi12 driverName" style="width:130px">
						<label for="txt"><s:text name="driverName" /></label>
						<div class="form-element">
							<s:textfield id="driverName" name="driverName"
								cssClass="form-control input-sm" maxlength="25"	/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="service-content-wrapper">
			<div class="service-content-section">

				<div class="appContentWrapper">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="productDetails" />
						</h2>
						<div class="flexiWrapper filterControls marginBottom">
							<div class="flexi flexi12" style="width: 200px">
								<label for="txt"><s:text name="subCategory" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="selectedCategory"
										cssClass="form-control input-sm select2" list="{}"
										headerKey="" headerValue="%{getText('txt.select')}"
										id="category" onchange="listProduct();resetUnitData();" />
								</div>
							</div>
							<div class="flexi flexi12" style="width: 200px">
								<label for="txt"><s:text name="product" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<s:select name="selectedProduct"
										cssClass="form-control input-sm select2"
										list="{}" headerKey=""
										headerValue="%{getText('txt.select')}" id="product"
										onchange="resetUnitData();availableStock();loadUnit();" />
								</div>
							</div>
							<div class="flexi flexi12">
								<label for="txt" style="display: block; text-align: center"><s:text
										name="availableStock" /></label>
								<div class="form-element">
									<span class="avlStkTxt" id="stock"
										style="display: block; text-align: center"></span>

								</div>
							</div>
							<div class="flexi flexi12 ">
								<label for="txt"><s:text name="distributionStock" /><span
									class="manadatory">*</span></label>
								<div class="form-element">
									<div class="button-group-container">
										<s:textfield id="distributionStock" name="distributionStock"
											maxlength="8" onkeypress="return isNumber(event)"
											onkeyup="calulateAvailableStock()"
											cssStyle="text-align:right;padding-right:1px; width:60px!important;" />
										<div>.</div>
										<s:textfield id="stockPaise" name="stockPaise" maxlength="3"
											onkeypress="return isDecimal(event)"
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
							<div class="flexi flexi12">
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
					<s:text name="distributedStockProducts" />
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
								<th width="8%" style="text-align: center"><s:text
										name="distribution.unit" /></th>
								<th width="9%" style="text-align: center"><s:text
										name="qty" /></th>
								<th width="9%"><s:text name="action" /></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="6" style="text-align: center;"><s:text
										name="noDistributionStocckRecordFound" /></td>
							</tr>
						</tbody>
						<tfoot id="footerId">
							<tr>
								<td style="border: none !important;">&nbsp;</td>
								<td colspan="3" class="alignRight"><s:text name="total" /></td>
								<td style="text-align: right">0</td>
								<td style="border: none !important;">&nbsp;</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>



	</div>
</s:form>
<s:form name="butform" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="distId" name="distId" />
	<div class="appContentWrapper marginBottom" id="paynmentAmountDiv"
		style="display: block;">
		<div class="formContainerWrapper">
			<div class="flexiWrapper filterControls">
				<div class="yui-skin-sam" id="loadList" style="display: block">
					<sec:authorize
						ifAllGranted="service.distributionStockTransfer.create">
						<span id="savebutton" class=""><span class="first-child">
								<button type="button" class="save-btn btn btn-success"
									onclick="event.preventDefault();submitDistribution();" id="sucessbtn">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
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
		</div>
	</div>
</s:form>
<button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
					<s:text name="distributionStockTransfer" />
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
</div>
<script type="text/javascript">
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
jQuery(document).ready(function(){
	$('#startDate').datepicker('setDate', new Date());
	 $("#sucessbtn").on('click', function (event) {  
         event.preventDefault();
         var el = $(this);
         el.prop('disabled', true);
         setTimeout(function(){el.prop('disabled', false); }, 1000);
   });
});
function resetValues(){
	  $("#senderWarehouse option[value='']").prop("selected","selected");
		 $("#senderWarehouse").trigger("change");
		 $("#receiverWarhouse option[value='']").prop("selected","selected");
		 $("#receiverWarhouse").trigger("change");
		 $("#truckId").val("");
		 $("#driverName").val("");
		 $("#category option[value='']").prop("selected","selected");
		 $("#category").trigger("change");
		 $("#product option[value='']").prop("selected","selected");
		 $("#product").trigger("change");
		 	productsInfoArray = new Array();
			pArrayList = new Array();
			productTotalArray=new Array();
			productNameArray=new Array();
			reloadTable();
		
		 resetProductData();
}
function populateCategory(){
	 var sWare=$("#senderWarehouse option:selected").val();
	 var season=$("#season option:selected").val();
	$.ajax({
		 type: "POST",
        async: false,
        url: "distributionStockTransfer_populateProductCategory",
        data: {senderWarehouse: sWare,season:season},
        success: function(result) {
       	 if(result.length==0){
       	 }else{
       		 insertOptions("category",JSON.parse(result));
       		 
       	 }
        }
	}); 
}
function listProduct(){
	var category=$("#category option:selected").val();
	var sWare=$("#senderWarehouse option:selected").val();
	var season=$("#season option:selected").val();
	$.ajax({
		 type: "POST",
        async: false,
        url: "distributionStockTransfer_populateProduct",
        data: {season:season,category:category,senderWarehouse: sWare},
        success: function(result) {
       	 if(result.length==0){
       	 }else{
       		 insertOptions("product",JSON.parse(result));
       		 
       	 }
        }
	}); 
}

function resetUnitData(){
	document.getElementById('distributionStock').value = 0;
	document.getElementById('stockPaise').value = 0000;
	document.getElementById('stock').innerHTML = "";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("validatePriceError").innerHTML="";	
}
function availableStock(){
	var sWare=$("#senderWarehouse option:selected").val();
	var product=$("#product option:selected").val();
	var season=$("#season option:selected").val();
	if(!isEmpty(product) && product!=""){
	$.ajax({
		 type: "POST",
        async: false,
        url: "distributionStockTransfer_populateAvailableStock",
        data: {senderWarehouse: sWare,product:product,season:season},
        success: function(result) {
       	 if(result=='NA'){
       	 }else{
       		 var data=result.split(",");
       		 var totalQty = parseFloat(data[0]).toFixed(3);
			$("#stock").text(totalQty);
			$("#productUnit").text(data[1]);
       	 }
        }
	}); 
	}
}
function loadUnit(){
	
}
function calulateAvailableStock(){
	
}
function isDecimal(evt,obj) {
	
  
}

function isNumber(evt,obj) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;

}

function validateData(){
	var hit=validateDetails(true);
	if(hit){
		   reload();
		}
	}
function validateDetails(validateEmpty){
	var hit=true;
	var selectedCategory=document.getElementById("category").value;
	var selectedProduct = document.getElementById("product").value;
	var selectedUnit=document.getElementById("productUnit").innerHTML;
	var distributionStock = document.getElementById("distributionStock").value;
	var stockPaise = document.getElementById("stockPaise").value;
	var availableStock = document.getElementById("stock").innerHTML;
	var QtyPerUnit,toatalQty;
	var distStock=distributionStock+"."+stockPaise;

    
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
			
	else if((validateEmpty  || distributionStock.trim()!="")  && parseFloat(distributionStock) > parseFloat(availableStock)){
			document.getElementById("validateError").innerHTML='<s:text name="insufficientstock"/>';
			hit=false;
	
    }
	return hit;
}
function reload(){
	addRow();
	resetProductData();
}
var productsInfoArray = new Array();
var pArrayList = new Array();
var productTotalArray=new Array();
var productNameArray=new Array();
function addRow(){
	var editIndex=getEditIndex();
	var categoryId=$("#category option:selected").val();
	var categoryName=$("#category option:selected").text();
	var prodName=$("#product option:selected").text();
	var prodId=$("#product option:selected").val();
	var availableStock = document.getElementById("stock").innerHTML;
	var stockPre=document.getElementById("distributionStock").value;
	var stockPaise=document.getElementById("stockPaise").value;
	var distributionStock = stockPre+"."+stockPaise;
	var proUnitVal = document.getElementById("productUnit").innerHTML;
	var Qty,QtyPerUnit;
	Qty=distributionStock;
	QtyPerUnit=(parseFloat(distributionStock)).toFixed(4);
	var productArray=null;
	var productExists=false;
	if(editIndex==-1){
	 productArray = {
			 productId :prodId,
			 productName : prodName,
			 distributionStock : Qty,
			 categoryName:categoryName,
			 categoryId:categoryId,
			 unit : proUnitVal,
			 QtyPerUnit:QtyPerUnit,
			 enteredStock:parseFloat(distributionStock).toFixed(4),
			 isEdit:false,
			 stockPre:stockPre,
			 stockPaise:stockPaise,
			 
         };


	 for(var i=0;i<productsInfoArray.length;i++){
		 if(productsInfoArray[i].productId== productArray.productId){
				Qty=parseFloat(productsInfoArray[i].enteredStock)+(parseFloat(productArray.enteredStock));
				QtyPerUnit=parseFloat(productsInfoArray[i].QtyPerUnit)+(parseFloat(productArray.QtyPerUnit));
				distributionStock=parseFloat(productsInfoArray[i].distributionStock)+(parseFloat(productArray.distributionStock));
				enteredStock=parseFloat(productsInfoArray[i].enteredStock)+(parseFloat(productArray.enteredStock));
				productsInfoArray[i].distributionStock = Qty;
				productsInfoArray[i].Qty = Qty.toFixed(4);
				productsInfoArray[i].QtyPerUnit=QtyPerUnit.toFixed(4);
				productsInfoArray[i].enteredStock=enteredStock.toFixed(4);
				productsInfoArray[i].categoryName=categoryName;
				productsInfoArray[i].categoryId=categoryId;
				productsInfoArray[i].unit = proUnitVal;
				productsInfoArray[i].isEdit=false;
				productsInfoArray[i].stockPre=stockPre;
				productsInfoArray[i].stockPaise=stockPaise;
				 productExists=true;
				
			}
		
		}
		if(!productExists)
	 	productsInfoArray[productsInfoArray.length] = productArray;
	}else{
		productArray=productsInfoArray[editIndex];
	    editproductTotalArray(productArray);
	    productsInfoArray[editIndex].productId= prodId;
		productsInfoArray[editIndex].productName= prodName;
		productsInfoArray[editIndex].distributionStock = Qty;
		productsInfoArray[editIndex].unit = proUnitVal;
		productsInfoArray[editIndex].Qty = Qty;
		productsInfoArray[editIndex].QtyPerUnit=QtyPerUnit;
		productsInfoArray[editIndex].categoryName=categoryName;
		productsInfoArray[editIndex].categoryId=categoryId;
		productsInfoArray[editIndex].enteredStock=parseFloat(distributionStock).toFixed(2);
		productsInfoArray[editIndex].isEdit=false;
		productsInfoArray[editIndex].unit = proUnitVal;
		productsInfoArray[editIndex].stockPre=stockPre;
		productsInfoArray[editIndex].stockPaise=stockPaise;
		productArray=productsInfoArray[editIndex];
	}
	var isExist=false;
	var pName=prodName;
	for(var i=0;i<productTotalArray.length;i++){
		if(productTotalArray[i].productName==pName){
			productTotalArray[i].qty= QtyPerUnit;
			productTotalArray[i].distributionStock = distributionStock,
			productTotalArray[i].enteredStock=parseFloat(distributionStock).toFixed(4),
			productTotalArray[i].stockPre=stockPre,
			productTotalArray[i].stockPaise=stockPaise,
			isExist=true;
			break;
		}
	}
	if(!isExist){
		var  totalArray={
				productName:	prodName,
				prodId:prodId,
				categoryId:categoryId,
				categoryName:categoryName,
				unit :proUnitVal,
				categoryName:categoryName,
		        qty :   QtyPerUnit,
		        distributionStock : distributionStock,
		        enteredStock:parseFloat(distributionStock).toFixed(4),
		        unit:proUnitVal,
		       stockPre:stockPre,
				stockPaise:stockPaise,
		}
		productTotalArray[productTotalArray.length]=totalArray;
	}
reloadTable();
}

function getEditIndex(){
	for(var i=0; i< productsInfoArray.length; i++){
		if(productsInfoArray[i].isEdit)
			return i;
		
	}
	return -1;
}

function redirectForm()
{
	document.redirectform.action="distributionStockTransfer_create.action";
	document.redirectform.submit();
}

function editproductTotalArray(productArray){
	for(var i=0;i<productTotalArray.length;i++){
		if(productTotalArray[i].name==productArray.name){
		productTotalArray[i].qty-=productArray.QtyPerUnit;
		break;
		}
	}
}
function reloadTable(){
	var tbodyRow = "";
	var tfootArray = new Array();
	jQuery('#productInfoTbl > tbody').html('');
	var rowCount=0;
	var totStock=parseFloat(0).toFixed(4);
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		if(!productsInfoArray[cnt].isEdit){
		rowCount++;
		tbodyRow += '<tr>'+
		'<td style="text-align:center;">'+(cnt+1)+'</td>'+
		'<td style="text-align:center;">'+productsInfoArray[cnt].categoryName+'</td>'+
		'<td style="text-align:center;">'+productsInfoArray[cnt].productName+'</td>'+	
	    '<td style="text-align:center;">'+productsInfoArray[cnt].unit+'</td>'+
		'<td style="text-align:right; width="5%"">'+productsInfoArray[cnt].QtyPerUnit+'</td>'+
		 '<td style="width :6%"><i style="cursor: pointer;color: white;background-color: #337ab7;padding: 5px 10px; font-size: 13px; line-height: 1.5;margin-left: 10px;" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')"></i>'+
		 '<i class="fa fa-trash" style="cursor: pointer;color: #fff;background-color: #c9302c;border-color: #761c19;padding: 5px 10px;font-size: 13px;line-height: 1.5;margin-left: 25px;" aria-hidden="true " onclick="removeRow('+cnt+')"></i></td>'+
		'</tr>';
		totStock= parseFloat(parseFloat(totStock) + parseFloat(productsInfoArray[cnt].QtyPerUnit)).toFixed(4);
		}
	}
	
	if(rowCount==0){
		tbodyRow += '<tr>'+
					'<td colspan="6" style="text-align:center"><s:text name="noDistributionStocckRecordFound"/></td>'+
					'</tr>';
	}
	jQuery('#productInfoTbl > tbody').html(tbodyRow);
	var tfootRow = "";
	jQuery('#productInfoTbl > tfoot').html('');
	tfootRow += '<tr>'+
	'<td  colspan="4" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;text-align:right;"><s:text name="total"/></td>'+
	'<td style="text-align:right;">'+totStock+'</td><td></td></tr>';
	
	jQuery('#productInfoTbl > tfoot').html(tfootRow);
	jQuery("#product1").focus();
	jQuery("#category").focus();
}
function resetProductData(){
	
	listProduct();
	resetEditFlag();
	reloadTable();	
	resetPrefixAndSuffix();
	jQuery('#category').prop("disabled",false);
	jQuery('#product').prop("disabled",false);
	var idsArray=['category','product'];
	resetSelect2(idsArray);
	 document.getElementById("productUnit").innerHTML="";
	document.getElementById('stock').innerHTML = "";
	document.getElementById('distributionStock').innerHTML = "";
	document.getElementById('distributionStock').value="";
	document.getElementById("validateError").innerHTML="";
	document.getElementById("validatePriceError").innerHTML="";
	jQuery('#category').prop("disabled",false);
	jQuery('#product').prop("disabled",false);
}
function resetEditFlag(){
	for(var i=0; i< productsInfoArray.length; i++)
		productsInfoArray[i].isEdit=false;	
}
function resetPrefixAndSuffix(){
	 jQuery("#distributionStock").val("0");
	 jQuery("#stockPaise").val("000");
}

function editRow(indx){
	jQuery('#category').prop("disabled",true);
	jQuery('#product').prop("disabled",true);
	var prodInfo='';
	/* $("#distributionStock").val(productsInfoArray[indx].stockPre);
	 $("#stockPaise").val(productsInfoArray[indx].stockPaise); */
	 $("#category option[value='"+productsInfoArray[indx].categoryId+"']").prop("selected","selected");
	 $("#category").trigger("change");
	var prod=productsInfoArray[indx].productId;
	var category=$("#category").val();
	 
	var selSenderWarehouse = document.getElementById("senderWarehouse").value;
		  $("#product option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
		 $("#product").trigger("change");
		
	var urlEdit="distributionStockTransfer_populateProduct";
	category=(category==0)?"":category;
	selSenderWarehouse=(selSenderWarehouse==0)?"":selSenderWarehouse;
	if(selSenderWarehouse !="" && category!=""){
	$.ajax({
		 type: "POST",
         async: false,
        url:urlEdit,
         data: {category:category,senderWarehouse:selSenderWarehouse},
         success: function(result) {
        	 insertOptions("product",JSON.parse(result));
        	 $("#product option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
        	 $("#product").trigger("change");
        	 prodInfo=jQuery('#product').val();
        		var start=prodInfo;
        		var resultString=prodInfo.substring(start+1);
        		var arry=resultString.split('~'); 
        		var units=productsInfoArray[indx].unit;
        		document.getElementById('distributionStock').value =productsInfoArray[indx].enteredStock.split(".")[0];
        		document.getElementById('stockPaise').value  = productsInfoArray[indx].enteredStock.split(".")[1];
         }
	});
	}else
		{
		$("#product option[value='"+productsInfoArray[indx].productId+"']").prop("selected","selected");
		$("#product").trigger("change");
		document.getElementById('distributionStock').value =productsInfoArray[indx].enteredStock.split(".")[0];
		document.getElementById('stockPaise').value  = productsInfoArray[indx].enteredStock.split(".")[1];
		}
	resetEditFlag();
	productsInfoArray[indx].isEdit=true;
	calulateAvailableStock();
	reloadTable();
}
function removeRow(indx){
	for(var i=0;i<productTotalArray.length;i++){
	    if(productTotalArray[i].prodName==productsInfoArray[indx].prodName){
		productTotalArray[i].qty-=productsInfoArray[indx].QtyPerUnit;
		productTotalArray[i].distributionStock-=productsInfoArray[indx].distributionStock;
		productTotalArray[i].enteredStock-=productsInfoArray[indx].enteredStock;
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
function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}
function submitDistribution() {
	try{
	var stockType=false;
    var hit=true;
	var senderWarehouse = document.getElementById("senderWarehouse").value;
	var receiverWarehouse=document.getElementById("receiverWarhouse").value;
	var selectedDate= document.getElementById("startDate").value;
	var driverName=document.getElementById("driverName").value;
	var truckId=document.getElementById("truckId").value;
	var season=document.getElementById("season").value;
	if(selectedDate.trim()==""){
		document.getElementById("validateError").innerHTML='<s:text name="selectDate"/>';
		enableButton();
		hit=false;
		return false;
	}if(season==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.season')}" />';
		hit=false;
		enableButton();
		return false;
	}
	if(senderWarehouse==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.senderWarehouse')}" />';
		hit=false;
		enableButton();
		return false;
	}if(receiverWarehouse==""){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('empty.receiverWarehouse')}" />';
		hit=false;
		enableButton();
		return false;
	}
	if(senderWarehouse==receiverWarehouse){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('sameWarehouse')}" />';
		enableButton();
		hit=false;
		return false;
   }
	if(productsInfoArray.length==0){
		document.getElementById("validateError").innerHTML='<s:text name="noDistributionStocckRecordFound"/>';
		enableButton();
		hit=false;
		return false;
	}
	if(hit){
	buildReqProductObject();
	var productTotalString=' ';
	for(var i=0;i<productTotalArray.length;i++){
			  productTotalString+=productTotalArray[i].prodId+"##"+productTotalArray[i].productName+"##"+productTotalArray[i].qty+"||";
	}
	$.post("distributionStockTransfer_populateDistribution.action",{season:season,senderWarehouse:senderWarehouse,receiverWarehouse:receiverWarehouse,selectedDate:selectedDate,driverName:driverName,truckId:truckId,productTotalString:productTotalString},function(data,result){
			if(result=='success')
			{
				document.getElementById("divMsg").innerHTML=data.des;
				document.getElementById("enableModal").click();		 
				}
 	});
	
	}else{
		$("#sucessbtn").prop('disabled', false);
	}
	}catch(e){
		$("#sucessbtn").prop('disabled', false);
		console.log(e);
	}
}
function buildReqProductObject(){
	
	pArrayList = new Array();	
	for(var cnt=0;cnt<productsInfoArray.length;cnt++){
		pArrayList[pArrayList.length] =					productsInfoArray[cnt].productName+'|'+
														productsInfoArray[cnt].prodId+'|'+
														productsInfoArray[cnt].Qty +'|'+
														productsInfoArray[cnt].unit +'|'+
														productsInfoArray[cnt].QtyPerUnit;
	}	

}
function validatewarehouse(){
	document.getElementById("validateError").innerHTML='';
	var senderWarehouse = document.getElementById("senderWarehouse").value;
	var receiverWarehouse=document.getElementById("receiverWarhouse").value;
    if(!isEmpty(senderWarehouse) && !isEmpty(receiverWarehouse) && senderWarehouse!="" && receiverWarehouse!="" && senderWarehouse==receiverWarehouse){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('sameWarehouse')}" />';
		 $('#receiverWarhouse').focus().select()
    }
}
function disablePopupAlert(){
	document.redirectform.action="distributionStockTransfer_create.action";
	document.redirectform.submit();
}
</script>
<s:form name="redirectform" action="distributionStockTransfer_create.action"
	method="POST">
</s:form>	
		