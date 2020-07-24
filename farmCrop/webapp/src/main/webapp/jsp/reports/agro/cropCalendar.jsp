<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout -->
<meta name="decorator" content="swithlayout">


</head>


<font color="red"> <s:actionerror /></font>


<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" name="cropCalendar.id" />
	<s:hidden key="command" />
	


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
				<s:property value="%{getLocaleProperty('info.cropCalendar')}" />
			</h2>
			<div class="flexiWrapper filterControls">

				<div class="flexi flexi10" >
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
				
				<div class="flexi flexi10">
					<label for="txt"><s:property value="%{getLocaleProperty('name')}" /><span
						class="manadatory">*</span></label>

					<div class="form-element">						
							<s:textfield name="calendarName" id="calendarName"
								theme="simple" size="20" cssClass="form-control" />
						</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:property
							value="%{getLocaleProperty('crop.name')}" /> <span
						class="manadatory">*</span></label>
					<div class="form-element">
						<s:select id="crop" name="selectedCrop" list="productList"
							headerKey="" headerValue="%{getText('txt.select')}"
							listKey="id" listValue="name" 							
							cssClass="form-control select2"
							onchange="listProVarierty(this);"/>
					</div>
				</div>

				<div class="flexi flexi10">
					<label for="txt"><s:property
							value="%{getLocaleProperty('variety.name')}" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedVariety" id="variety" list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control select2" />
					</div>
				</div>
				<div class="flexi flexi10">
					<label for="txt"><s:property
							value="%{getLocaleProperty('activityType.name')}" /> <sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="selectedActivity"  id="activityType" list='cropTypeList'
							headerKey="" headerValue="%{getText('txt.select')}" theme="simple" 
							cssClass="form-control select2" />
					</div>
				</div>
				
						<div class="flexi flexi10">
							<label for="txt"><s:text name="season.name" /><span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select name="selectedSeason" id="season"
									list="harvestSeasonList"									
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" cssClass="form-control input-sm select2" />
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
					<s:property
							value="%{getLocaleProperty('info.cropCalendarDetails')}" />	

					</h2>
					<div class="flexiWrapper filterControls">
					 <div class="flexi flexi10">
							<label for="txt"><s:text
									name="%{getLocaleProperty('activityMethodName')}" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select name="selectedActivityMethod"
									cssClass="form-control select2" list="activityMethodList"
									headerKey="" headerValue="%{getText('txt.select')}"
									id="activityMethod" />
							</div>
						</div> 
						<div class="flexi flexi10 ">
							<label for="txt"><s:text
									name="%{getLocaleProperty('noOfDays')}" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="noOfDays" name="noOfDays" maxlength="3"
										cssClass="col-sm-3 form-control " onkeypress="return isNumber(event)" />
							</div>
						</div>
						<div class="flexi flexi10 ">
								<label for="txt"><s:property value="%{getLocaleProperty('action')}" /></label>
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
												aria-hidden="true " onclick="resetCalendarDetailData()"
												title="<s:text name="Cancel" />">
												<i class="fa fa-trash"></i>
											</button>

										</td>
									</table>
								</td>

							</div>
						

						
					</div>
					<table class="table table-bordered aspect-detail" id="cropCalendarDetailTable"
							style="width: 100%; margin-top: 30px;">
							<thead>
								<tr>
								<th><s:property value="%{getLocaleProperty('activityMethodName')}" /></th>
									<th><s:property value="%{getLocaleProperty('noOfDays')}" /></th>								
									<th style="text-align: center"><s:property value="%{getLocaleProperty('action')}" /></th>
								</tr>
							</thead>
							<tbody id="cropCalendarDetailContent">
							</tbody>
							<!-- <tfoot>
								<tr>
									<th></th>
									<th></th>								
									<th></th>
								</tr>

							</tfoot> -->
						</table>
					
					
					
				</div>
			</div>

			<div class="appContentWrapper marginBottom filter-background">
				<div class="flexiWrapper filterControls">

					

					<div class="yui-skin-sam" id="loadList" style="display: block">
						<sec:authorize
							ifAllGranted="service.cropCalendar.create">
							<span id="savebutton" class=""> <span class="first-child">
									<button id="submitButton" type="button"
										class="save-btn btn btn-success"
										onclick="submitCropCalendar();" id="sucessbtn">
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

</s:form>

<s:form name="listForm" id="listForm" action="cropCalendar_list">
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
            <span><h5><s:text name="crpclndrMsg" /></h5></span>
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

<s:form name="redirectform" action="cropCalendar_list" method="POST">
</s:form>


<script src="plugins/jquery-input-mask/jquery.inputmask.bundle.min.js"></script>
<script type="text/javascript">
var rowCounter=0;
jQuery(document).ready(function(){
	// $.fn.datepicker.defaults.format = test.toLowerCase();
		$('#date').datepicker('setDate', new Date());
	jQuery("#edit").hide();
	resetSelect2();
	if('<s:property value="command" />'=="update") {
		var calendarData = '<s:property value="calendarString" />'.split("|");
		//alert(calendarData);
		for(var j=0; j < calendarData.length; j++){
			var calendarDataArray = calendarData[j].split("#");
			
			addEditRow(calendarDataArray[0], calendarDataArray[1]);
		}
	}
});
var printWindowCnt=0;
var windowRef;


function addEditRow(activityMethod, noOfDays) {
	//alert();
	var editIndex = getEditIndex();
	var days = noOfDays;
	var method =activityMethod;
	
	var calendarExists=false;	
	
	var calendarArray=null;
	
	if(editIndex==-1){
		calendarArray = {
			 activitymethod : activityMethod.split('=')[0],
			 days : days,
			 isEdit:false
     };
   
	 for(var cnt=0;cnt<calendarInfoArray.length;cnt++) {
		if((calendarInfoArray[cnt].name== productArray.name) ){
		 if(calendarInfoArray[cnt].vendor== productArray.vendor){
			 productExists=true;
			 calendarInfoArray[cnt].stockN=parseFloat(calendarInfoArray[cnt].stockN)+parseFloat(productArray.stockN);
			 calendarInfoArray[cnt].stockD=parseFloat(calendarInfoArray[cnt].stockD)+parseFloat(productArray.stockD);			}		
		}else{
      batchExists=true;
		}
	 }
   if(!productExists)
	   calendarInfoArray[calendarInfoArray.length] = productArray;
	 
	} else {
		productArray=calendarInfoArray[editIndex];
	  editproductTotalArray(productArray);
		// productsInfoArray[editIndex].name= productName.split('=')[0];
		calendarInfoArray[editIndex].stock = stock;
		calendarInfoArray[editIndex].isEdit=false;
		productArray=calendarInfoArray[editIndex];
	}
	
	reloadTable();
	resetCalendarDetailData();

}

function listProVarierty(call) {
	var selectedPro = call.value;
	//alert(selectedPro);
	if(!isEmpty(selectedPro)){
		$.ajax({
			 type: "POST",
	        async: false,
	        url: "cropCalendar_populateVariety.action",
	        data: {selectedProduct : selectedPro},
	        success: function(result) {
	        	//alert($.parseJSON(result));
	        	insertOptions("variety", $.parseJSON(result));
	        }
		});
	}
	resetSelect2();
	
}




function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}
function submitCropHarvest() {
}

function enableButton(){
jQuery(".save-btn").prop('disabled',false);
}

function cancel() {
	document.form.action = "cropCalendar_list.action";
	document.listForm.submit();
}

function submitCropCalendar(){
	$("#sucessbtn").prop('disabled', true);
	var hit=true;	
	var date =$('#date').val();
	var name=$('#calendarName').val();
	var crop=$('#crop').val();
	var variety=$('#variety').val();
	var activityType=$('#activityType').val();
	var season=$('#season').val();
	
	if(isEmpty(name) || name=="" || name==null)
	{
		
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.cropCalendarName')}" />');
  		hit=false;
  		enableButton();
 		return false;
	}
	if(isEmpty(crop) || crop=="" || crop==null)
	{
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.crop')}" />');		
  		hit=false;
  		enableButton();
 		return false;
	}
	if(isEmpty(variety) || variety=="" || variety==null)
	{
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.variety')}" />');	
  		hit=false;
  		enableButton();
 		return false;
	}
	if(isEmpty(activityType) || activityType=="" || activityType==null)
	{		
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.activityType')}" />');	
  		hit=false;
  		enableButton();
 		return false;
	}
	if(isEmpty(season) || season=="" || season==null)
	{
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.season')}" />');	
  		hit=false;
  		enableButton();
 		return false;
	}
	var cropCalendarInfoArray =  buildCropCalendarInfoArray();
	if(isEmpty(cropCalendarInfoArray)){		
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('noCropCalendarRecordFound')}" />');	
  		hit=false;
  		enableButton();
 		return false;
	}
	if(hit){
		$.post("cropCalendar_populateCropCalendar.action",{
			txnDate:date.toString(),
			calendarName:name,
			selectedCrop:crop,
			selectedVariety:variety,
			selectedActivity:activityType,
			selectedSeason:season,
			calendarString:cropCalendarInfoArray
			},function(data,result){
				//alert(result);
				if(result=='success')
					{
						//document.getElementById("divMsg").innerHTML=result;
						document.getElementById("enableModal").click();
						//disablePopupAlert();
						//enablePopupAlert();
						
					}
				
	});
		
	}else{
		$("#sucessbtn").prop('disabled', false);
	}
}

function disablePopupAlert(){
	
	document.redirectform.action="cropCalendar_list.action";
	document.redirectform.submit();
}


function distributionEnable(){
	
	document.redirectform.action="cropCalendar_create.action";
	document.redirectform.submit();
}
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function redirectForm()
{
	document.redirectform.action="cropCalendar_create.action";
	document.redirectform.submit();
}

function enableButton(){
	jQuery(".save-btn").prop('disabled',false);
	}

function insertOptions(ctrlName, jsonArr) {
	document.getElementById(ctrlName).length = 0;
	addOption(document.getElementById(ctrlName), '<s:text name="txt.select"/>', "");
	for (var i = 0; i < jsonArr.length; i++) {
		addOption(document.getElementById(ctrlName), jsonArr[i].name,
				jsonArr[i].id);
	}
}
function resetCalendarDetailData(){	
	jQuery("#activityMethod").val("");
	jQuery("#noOfDays").val("");
	 resetSelect2();	
	
}
function addRow(){

	jQuery("#validateError").text("");	
	
	var activityMethod=jQuery("#activityMethod").val();
	var activityMethodText=jQuery("#activityMethod option:selected").text();
	var noOfDays=jQuery("#noOfDays").val();
	
	if(isEmpty(activityMethod)){
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.activityMethod')}" />');
		return false;
	}if(isEmpty(noOfDays)){
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.noOfDays')}" />');
		return false;
	}
	
	if(!checkCropCalendarExists(activityMethod)){
		var tableRow="<tr id=row"+(++rowCounter)+">";
		tableRow+="<td class='hide activityMethodId'>"+activityMethod+"</td>";
		tableRow+="<td class='activityMethodText'>"+activityMethodText+"</td>";
		tableRow+="<td class='noDays'>"+noOfDays+"</td>";	
		tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteCropCalendar("+rowCounter+")'></td>";
		tableRow+="</tr>";
		console.log(tableRow);
		jQuery("#cropCalendarDetailContent").append(tableRow);
		resetCalendarDetailData();
		//updateTableFooter();
	} 
}
function deleteCropCalendar(rowCounter){
	var id="#row"+rowCounter;
	jQuery(id).remove();
	//updateTableFooter();
}
function editRow(rowCounter){
	var id="#row"+rowCounter;
	
	$.each(jQuery(id), function(index, value) {
		var selectedActivityMethod=jQuery(this).find(".activityMethodId").text();	
		//var selectedActivityMethod=jQuery(this).find(".activityMethodText").text();		
		var noOfDays=jQuery(this).find(".noDays").text();	
		
		jQuery("#activityMethod").val(selectedActivityMethod);
		callTrigger("activityMethod");
		jQuery("#noOfDays").val(noOfDays);
		resetSelect2();
		
	});
	jQuery("#add").hide();
	jQuery("#edit").show();
	$("#edit").attr("onclick","editCalendar("+rowCounter+")");
}
function callTrigger(id){
	$("#"+id).trigger("change");
}
function editCalendar(index){
	jQuery("#validateError").text("");
	var id="#row"+index;
	jQuery(id).empty();

	
	var activityMethod=jQuery("#activityMethod").val();
	var activityMethodText=jQuery("#activityMethod option:selected").text();
	var noOfDays=jQuery("#noOfDays").val();

	if(isEmpty(activityMethod)){
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.activityMethod')}" />');
		return false;
	}if(isEmpty(noOfDays)){
		jQuery("#validateError").text('<s:property value="%{getLocaleProperty('empty.noOfDays')}" />');
		return false;
	}
	
	if(!checkCropCalendarExists(activityMethod)){
		var tableRow="";
		
		tableRow+="<td class='hide activityMethodId'>"+activityMethod+"</td>";
		tableRow+="<td class='activityMethodText'>"+activityMethodText+"</td>";
		tableRow+="<td class='noDays'>"+noOfDays+"</td>";	
		tableRow+="<td><i style='cursor: pointer; font-size: 150%; color: blue;' class='fa fa-pencil-square-o' aria-hidden='true' onclick='editRow("+rowCounter+")' ></i><i style='cursor: pointer; font-size: 150%; color: black;' class='fa fa-trash-o' aria-hidden='true' onclick='deleteCropCalendar("+rowCounter+")'></td>";
		tableRow+="</tr>";
		jQuery(id).append(tableRow);
		resetCalendarDetailData();
		//updateTableFooter();
		jQuery("#add").show();
		jQuery("#edit").hide();
	}
	
}

function checkCropCalendarExists(value1){
	//alert(value1);
	var returnVal = false;

		var tableBody = jQuery("#cropCalendarDetailContent tr");
		
		$.each(tableBody, function(index, value) {
			var activityMethod = jQuery(this).find(".activityMethodId").text();
		//alert(activityMethod);
			 if(activityMethod==value1){
				alert('<s:property value="%{getLocaleProperty('cropCalendar.alreadyExists')}" />');
				returnVal=true 
			}
		});
	
	return returnVal;	
}
function resetSelect2(){
	$(".select2").select2();
}

function buildCropCalendarInfoArray(){
	
	var tableBody = jQuery("#cropCalendarDetailContent tr");
	var calendarInfo="";
	
	$.each(tableBody, function(index, value) {
		calendarInfo+=jQuery(this).find(".activityMethodId").text(); //0
		
		calendarInfo+="#"+jQuery(this).find(".noDays").text()+"@";//1
		
		
		
	});

	return calendarInfo;
}
</script>


