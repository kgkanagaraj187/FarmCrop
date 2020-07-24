<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>


<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->
<s:head />
<style type="text/css">
.alignTopLeft {
	float: left;
	width: 6em;
}

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

.datepicker_clear {
	display: none;
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

.alignLeft {
	text-align: left !important;
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

.div.error {
	color: #FD0000;
	font-size: 12px;
	/*padding: 5px 10px 5px 0;*/
	text-align: left;
	width: auto;
	margin-bottom: 0px !important;
}
</style>
</head>
<body>
	<s:form id="baleGeneration" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" name="id" />
	<s:hidden key="command" />
	<s:hidden key="productTotalString" id="productTotalString" />
		<font color="red"> <s:actionerror /></font>
		<div>
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
						<s:text name="info.general" />
					</h2>
					<div class="flexiWrapper filterControls">
						<div class="flexi flexi8">
							<label for="txt"><s:text name="date" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="startDate" id="startDate" readonly="true"
									theme="simple"
									data-date-format="%{getGeneralDateFormat().toLowerCase()}"
									data-date-viewmode="years" cssClass="date-picker form-control"
									size="20" />
							</div>
						</div>
						
						<%-- <div class="flexi flexi8">
							<label for="txt"><s:property
									value="%{getLocaleProperty('season')}" /> <sup
								style="color: red;">*</sup></label>

							<div class="form-element">
								<s:select name="selectedSeason" list="seasonList"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="select2 form-control" id="season"/>
							</div>
						</div> --%>
						<div class="flexi flexi8">
							<label for="txt"><s:property
									value="%{getLocaleProperty('ginning')}" /> <sup
								style="color: red;">*</sup></label>

							<div class="form-element">
								<s:select name="selectedGinningId" list="ginnerCenterList"
									headerKey="" headerValue="%{getText('txt.select')}"
									cssClass="select2 form-control" id="ginning"
									onchange="populateHeap(this)" />
							</div>
						</div>
						<div class="flexi flexi8">
							<label for="txt"><s:text name="heapName" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="heap" list="{}" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" id="heap"
									onchange="populateGinningDate(this);"
									cssClass="form-control input-sm select2" />
							</div>
						</div>

						<div class="flexi flexi8">
							<label for="txt"><s:text name="ginningDate" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="ginningDate" list="{}" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" id="ginningDate"
									cssClass="form-control input-sm select2" />
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="formContainerWrapper prodRecTable">
				<h2>
					<s:text name="info.baleGeneration" />
				</h2>
				<div class="flexiWrapper filterControls">


					<div class="flexi flexi8">
						<label for="txt"><s:text name="lotNo" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield name="lotNo" id="lotNo" maxlength="35"
								cssClass="form-control" />
						</div>
					</div>

					<div class="flexi flexi8">
						<label for="txt"><s:text name="prNo" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield name="prNo" id="prNo" maxlength="35"
								cssClass="form-control" />
						</div>
					</div>

					<div class="flexi flexi8">
						<label for="txt"><s:text name="baleWeight" /> (Kgs)<span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield name="baleWeight" id="baleWeight" maxlength="35"
								cssClass="form-control" onkeypress="return isDecimal(event)" />
						</div>
					</div>
					<div class="flexi flexi8">
						<label for="txt" style="padding-left:130px!important;"><s:text name="action" /></label>
						<div class="form-element" style="text-align: center;">
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
			
			<div class="appContentWrapper marginBottom filter-background">
				<div class="flexiWrapper filterControls">
					<table id="productInfoTbl1"
						class="table table-bordered aspect-detail">
						<thead>
							<tr class="odd">
								<th width="10%" style="text-align: center;"><s:text name="s.no" /></th>
								<th width="25%" style="text-align: center;"><s:text name="lotNo" /></th>
								<th width="25%" style="text-align: center;"><s:text name="prNo" /></th>
								<th width="25%" style="text-align: center;"><s:text	name="%{getLocaleProperty('baleWeight')}" /></th>
								<th width="15%" style="text-align: center;">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="8" style="text-align: center;"><s:text
										name="noGinningRecordFound" /></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="2" width="30%" style="border: none !important;">&nbsp;</td>
								<td style="border: none !important;">Total Bale Weight</td>
								<td
									style="padding-top: 6px !important; padding-bottom: 6px !important; font-weight: bold; border-top: solid 2px #567304; border-bottom: solid 2px #567304 !important; border-right: none !important; border-left: none !important; text-align: right;">0.00</td>
								<td style="border: none !important;">&nbsp;</td>
							</tr>
						</tfoot>
					</table>

					<div class="yui-skin-sam" id="loadList" style="display: block">
						<sec:authorize
							ifAllGranted="service.baleGeneration.create">
							<span id="savebutton" class=""> <span class="first-child">
									<button id="submitButton" type="button"
										class="save-btn btn btn-success"
										onclick="event.preventDefault();submitBaleGeneration();" id="sucessbtn">
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
					<s:text name="bale.generation" />
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
	<script>
var tenant = '<s:property value="%{getCurrentTenantId()}" />';
jQuery(document).ready(function(){
	 $('#startDate').datepicker('setDate', new Date());
});
var productsInfoArray = new Array();
var productTotalArray=new Array();

$("#submitButton").on('click', function (event) {  
    event.preventDefault();
    var el = $(this);
    el.prop('disabled', true);
    setTimeout(function(){el.prop('disabled', false); }, 1000);
});


function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function isDecimal(evt) {
	
	 evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
	        return false;
	    }
	    return true;
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

function reset(){
	$('#startDate').datepicker('setDate', new Date());
	jQuery("#validateError").text('');
	window.location.href="baleGeneration_list.action";    
}

function removeRow(indx){
	for(var i=0;i<productTotalArray.length;i++){
	  if(productTotalArray[i].name==productsInfoArray[indx].ginningName+'='+productsInfoArray[indx].ginningName || productTotalArray[i].heapName==productsInfoArray[indx].heapName|| productTotalArray[i].ginningDate==productsInfoArray[indx].ginningDate){
		productTotalArray[i].baleWeight-=productsInfoArray[indx].baleWeight;
		break;
		}
	}
	
	if(productsInfoArray.length>0){				
		productsInfoArray.splice(indx,1);		
	}
	
	resetProductData();
	reloadTable();
}

function populateHeap(val){
	    var idsArray=['heap','ginningDate'];
	    resetSelect2(idsArray);
	    if(!isEmpty(val))
		jQuery("#validateError").text('');
	    $("#heap").empty();
	    var warehouseId= $('#ginning').val();
	    $.post("baleGeneration_populateHeap",{warehouseId:warehouseId},function(result){
		var data =jQuery.parseJSON(result);
		insertOptions("heap",data);
	});
}

function populateGinningDate(val){
    var idsArray=['ginningDate'];
    resetSelect2(idsArray);
    if(!isEmpty(val))
	jQuery("#validateError").text('');
    $("#ginningDate").empty();
    var warehouseId= $('#ginning').val();
    var heapCode= $('#heap').val();
    $.post("baleGeneration_populateGinningDate",{warehouseId:warehouseId,heapCode:heapCode},function(result){
	var data =jQuery.parseJSON(result);
	insertOptions("ginningDate",data);
	clearRows();
});
}

function clearRows(){
	resetSelect2(idsArray);
	var len = productsInfoArray.length;	
	productsInfoArray.splice(0,len);	
	resetProductData();
	reloadTable();	
}

function resetProductData(){
	document.getElementById("validateError").innerHTML="";
	document.getElementById('baleWeight').value = "";
	document.getElementById('lotNo').value = "";
	document.getElementById('prNo').value = "";	
} 

function addRow(){
	var editIndex = getEditIndex();
	var ginningCode= $('#ginning').val();
	var heapCode= $('#heap').val();
	var ginningDate= $('#ginningDate').val();
	var ginningName =$("#ginning option:selected").text(); 
	var heapName =$("#heap option:selected").text(); 
	var lotNo = document.getElementById("lotNo").value;
	var prNo = document.getElementById("prNo").value;
	var baleWeight = document.getElementById("baleWeight").value;
	//var seasonCode= $('#season').val();
	   
	if(ginningCode =="" || ginningCode=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.ginning"/>';
		return false;
	}
	
	/* if(seasonCode =="" || seasonCode=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.season"/>';
		return false;
	} */
	
	if(heapName =="" || heapName=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.batch"/>';
		return false;
	}
	
	if(ginningDate =="" || ginningDate=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.ginningDate"/>';
		return false;
	}
	
	if(isEmpty(lotNo) || lotNo =="" || lotNo=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.lotNo"/>';
		return false;
	}
	
	if(isEmpty(prNo) || prNo =="" || prNo=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.prNo"/>';
		return false;
	}
	
	if(baleWeight =="" || baleWeight=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.baleWeight"/>';
		return false;
	}
	
	 for(var cnt=0;cnt<productsInfoArray.length;cnt++)
	 {
	 if(productsInfoArray[cnt].lotNo==lotNo && productsInfoArray[cnt].prNo==prNo && productsInfoArray[cnt].isEdit==false)
	  {
	  document.getElementById("validateError").innerHTML='<s:text name="alreadyExist"/>';
	  return false;
	  }
	 }
	
	var productArray=null;
	var productExists=false;	
	if(editIndex==-1){
		 productArray = {
				 lotNo:lotNo,
				 prNo:prNo,
				 baleWeight:baleWeight,
				 isEdit:false
	     };
		 if(!productExists)
			 productsInfoArray[productsInfoArray.length] = productArray;
	}else{
		productArray = {
				 lotNo:lotNo,
				 prNo:prNo,
				 baleWeight:baleWeight,
				 isEdit:false
	     };
		productsInfoArray[editIndex]=productArray;
	}
	reloadTable();
	resetProductData();
}

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
					'<td style="text-align:right;">'+productsInfoArray[cnt].lotNo+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].prNo+'</td>'+
					'<td style="text-align:right;">'+productsInfoArray[cnt].baleWeight+'</td>'+
					'<td style="text-align:right; display: none">'+productsInfoArray[cnt].baleWeight+'</td>'+
					'<td colspan="2" class="alignCenter" style="padding-left:80px!important" ><button type="button" class="fa fa-pencil-square-o" aria-hidden="true" onclick="editRow('+cnt+')" title="<s:text name="Edit" />"></button>'+
					'<button type="button" class="fa fa-trash" aria-hidden="true " onclick="removeRow('+cnt+')" title="<s:text name="Delete" />"></button></td>'+
					'</tr>';
					
		if(tfootArray.length==0){
		    tfootArray[tfootArray.length] = {
			baleWeight : productsInfoArray[cnt].baleWeight,};
		} else { 
		   tfootArray[tfootArray.length-1].baleWeight=(parseFloat(tfootArray[tfootArray.length-1].baleWeight)+parseFloat(productsInfoArray[cnt].baleWeight));
		}
	  }	
	}	
	
	if(rowCount==0){
		tbodyRow += '<tr>'+	'<td colspan="13" style="text-align:center"><s:text name="noRecordFound"/></td>'+'</tr>';
	}
	
	jQuery('#productInfoTbl1 > tbody').html(tbodyRow);
	var proudctDataRow="";
	proudctDataRow += '<tr>'+'<td style="text-align:center;"></td>'+'<td style="text-align:left;"></td>'+'</tr>';
	var tfootRow = "";
	jQuery('#productInfoTbl1 > tfoot').html('');
 	tfootRow += '<tr>'+
	'<td colspan="2" style="border:none!important;">&nbsp;</td>'+
	'<td class="alignRight" style="border:none!important; padding-right:10px!important; padding-top:6px!important; padding-bottom:6px!important;"><s:text name="totalBaleWeight"/></td>';
	if(tfootArray.length>0){
		tfootRow +=	'<td class="alignRight" id="totalYieldPrice" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important;padding-left:270px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">'+parseFloat(tfootArray[tfootArray.length-1].baleWeight).toFixed(2)+'</td>';
   }else{
	 tfootRow +='<td class="alignRight" style="padding-right:10px!important;padding-top:6px!important; padding-bottom:6px!important; font-weight:bold; border-top:solid 2px #567304;border-bottom:solid 2px #567304!important;border-right:none!important;border-left:none!important;">0.00</td>';
   }
	tfootRow +='<td style="border:none!important;">&nbsp;</td>'+'</tr>'; 
   jQuery('#productInfoTbl1 > tfoot').html(tfootRow);
   jQuery("#product1").focus();
}

function editRow(indx) {
	document.getElementById('prNo').value = productsInfoArray[indx].prNo;
	document.getElementById('lotNo').value = productsInfoArray[indx].lotNo;
    document.getElementById('baleWeight').value = productsInfoArray[indx].baleWeight;
	resetEditFlag();
	productsInfoArray[indx].isEdit=true;
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

function insertOption(ctrlName, jsonArr) {
    document.getElementById(ctrlName).length = 0;
    for (var i = 0; i < jsonArr.length; i++) {
        addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
    }
    var id="#"+ctrlName;
    jQuery(id).select2();
}

function cancel() {
	window.location.href="baleGeneration_create.action";    
}

function submitBaleGeneration() {
	
	$("#sucessbtn").prop('disabled', true);
	var hit=true;		
	var editIndex = getEditIndex();
 	var date = $('#startDate').val();
	var ginningCode= $('#ginning').val();
	var heapCode= $('#heap').val();
	var ginningDate= $('#ginningDate').val();
	var ginningName =$("#ginning option:selected").text(); 
	var heapName =$("#heap option:selected").text(); 
	var lotNo = document.getElementById("lotNo").value;
	var prNo = document.getElementById("prNo").value;
	var baleWeight = document.getElementById("baleWeight").value;
	//var seasonCode= $('#season').val();
	  
	if(date =="" || date=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.date"/>';
		return false;
	}
	
	/* if(seasonCode =="" || seasonCode=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.season"/>';
		return false;
	} */
	
	
	if(ginningCode =="" || ginningCode=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.ginning"/>';
		return false;
	}
	
	if(heapCode =="" || heapCode=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.batch"/>';
		return false;
	}
	
	if(ginningDate =="" || ginningDate=="0")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.ginningDate"/>';
		return false;
	}
	   
	if(productsInfoArray.length==0) {
	 	document.getElementById("validateError").innerHTML='<s:text name="balenoRecordFound"/>';
	 	hit=false;
	 	enableButton();
	 	return false;
	 }
	
	if(hit){
		var productTotalString=' ';
		for(var i=0;i<productsInfoArray.length;i++){
			productTotalString+=productsInfoArray[i].lotNo+"#"+productsInfoArray[i].prNo+"#"+productsInfoArray[i].baleWeight+"|";
		}
		$.post("baleGeneration_create.action",{selectedDate:date,selectedGinningCode:ginningCode,selectedHeapCode:heapCode,selectedGinnDate:ginningDate,productTotalString:productTotalString
		},function(data,result){
		if(result=='success')
		{
		 document.getElementById("divMsg").innerHTML=result;
		 document.getElementById("enableModal").click();
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


function disablePopupAlert(){
	window.location.href="baleGeneration_create.action";
}

</script>
</body>