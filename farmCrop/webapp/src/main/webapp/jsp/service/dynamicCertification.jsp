<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<style>
.form-element textarea {
	width: 100%;
	height: 100px;
}

.wrap {
    word-wrap: break-word;
}
</style>

<script src="js/dynamicComponents.js?v=2.60"></script>


<link rel="stylesheet" type="text/css" media="screen" href="plugins/flexi/css/bjqs.css" />
<script src="plugins/flexi/js/bjqs-1.3.js" type="text/javascript"></script>
<script type="text/javascript">
var txnType=<%out.print("'" + request.getParameter("txnType") + "'");%>
<%-- var entityType=<%out.print("'"+request.getParameter("entityType")+"'");%> --%>
var entityType = '<s:property value="entityType"/>';
var constants="";
var url="";
var selectedId='';
var $overlays;
var $modals;
var $sliders;
var selectedShape;
var mapLabel2;
var coorArr = new Array();
var drawingManager =null;

$(document).ready(function() {
	loadCustomPopup();
	if ($(".photo").length <=0) {
	       $(".imgError").hide();
	    }
	$("#map").css("height", (($(window).innerHeight()) - 80));
	var entityType = '<s:property value="entityType"/>';
	 var seasonType = '<s:property value="seasonType"/>';
	 
	if (seasonType == '0') {
		$(".seasonClass").hide();
	} 
	
	
	if('<s:property value="command"/>'!="updateActPlan"){
	renderDynamicFeilds();
	}
	 if('<s:property value="command"/>'=="update"){
		 setDynamicFieldUpdateValuesByTxnId();
		 $(".crearteDiv").remove();
		  $(".detailDiv").show();
		  $(".farmIcsDetailDiv").show();
		  selectedId = '<s:property value="farmerDynamicData.referenceId"/>';
		 
	 }else if('<s:property value="command"/>'=="updateActPlan"){
		 renderDynamicFeildsWithActPlan();
		 $(".crearteDiv").remove();
		  $(".detailDiv").show();
		  $(".farmIcsDetailDiv").show();
		  selectedId = '<s:property value="farmerDynamicData.referenceId"/>';
	 }else{
		 
		 $(".crearteDiv").show();
		  $(".farmIcsDetailDiv").show();
		  $(".detailDiv").remove();
	 }
	   $(".approved").hide();
		$(".decline").hide();
		$(".farmDiv").hide();
		$(".farmerDiv").hide();
		$(".icsDetailDiv").hide();
		$(".inspectClass").hide();
		$(".farmCropsDiv").hide();
		 $(".groupDiv").hide();
		     $(".farmIcsDetailDiv").hide();
		if(entityType=='4'){
					$(".approved").show();
			$(".decline").show();
			$(".icsDetailDiv").show();
			$(".farmDiv").show();
			$(".groupDiv").hide();
			$(".farmerDiv").show();
			$(".inspectClass").show();
		
			  $(".farmIcsDetailDiv").show();
		} else if(entityType=='1'){
			$(".farmerDiv").show();
			$(".farmDiv").hide();
			$(".inspectClass").hide();
			 $(".farmCropDiv").hide();
		} else if(entityType=='2'){
			$(".farmerDiv").show();
		     $(".farmDiv").show();
		     $(".farmCropDiv").hide();
		     $(".inspectClass").hide();
		     $(".icsOtherDiv").hide();
		}  else if(entityType=='3'){
				$(".farmerDiv").hide();
		     $(".farmDiv").hide();
		     $(".groupDiv").show();
		     $(".inspectClass").hide();
		} else if(entityType=='5'){
			$(".farmerDiv").show();
			  $('#farmer').find('option').get(0).remove();
			$("#farmer").select2({
			    multiple:true
			    });
			
		     $(".farmDiv").hide();
		     $(".groupDiv").hide();
		     $(".inspectClass").hide();
		} else if(entityType=='6'){
			$(".farmerDiv").show();
			$(".farmCropsDiv").show();
		     $(".farmDiv").show();
		   
		} 
		$(".approved").hide();
		$(".decline").hide();
	if(entityType=='4'){
		if('<s:property value="command"/>'=="update"){
			//setDynamicFieldUpdateValues();
			$(".icsOtherDiv").show();
			$(".icsDetailDiv").hide();
			$(".farmCropDiv").hide();
			//$(".dynamicFieldsRender").hide();
			$("#farmer").attr("disabled", true); 
			$("#farmList").attr("disabled", true);
			
			if('<s:property value="inspectionStatus"/>'=="1"){
				$(".approved").show();
				$(".decline").hide();
			}else{
				$(".approved").hide();
				$(".decline").show();
			}
			
		}
	}else if(entityType=='5'){
		 var selectedImplements =  '<s:property value="farmerDynamicData.referenceId" />';
		 if(!isEmpty(selectedImplements)){
			 var values = selectedImplements.split("\\,");
			  $.each(selectedImplements.split(","), function(i,e){
				  alert(e.trim());
				  $("#farmer option[value='" + e.trim() + "']").prop("selected", true);
			  });
			  $("#farmer").select2();
		 }
	}
	/* $(".approved").hide();
	$(".decline").hide(); */
	 var  url = window.location.href;
 	  var command=$("#command").val();
 	if(url.indexOf('?txnType=') < 0){ 
     
 		  url = url+'?txnType='+txnType;
   
     $( ".lanMenu" ).each(function() {
	    	 var url1 = $(this).attr("href") +'&url='+ encodeURIComponent(url);
	    	  $( this ).attr( "href",url1);
	    	});
	     }

 	
	if(isEmpty(txnType)){
		alert("Please Configure Transaction type");
		return false;
	}
	if(isEmpty(entityType)){
		alert("Please Configure Entity type");
		return false;
	}else{
		
		//url="dynamicCertification_list?txnType="+txnType+"&entityType="+entityType;
		/* url="dynamicCertification_list?txnType="+txnType;
		$(".breadCrumbNavigation").html('');
		$(".breadCrumbNavigation").append("<li><a href='#'>Service </a></li>");
		$(".breadCrumbNavigation").append("<li><a href="+url+"> Dynamic ICS Certification</a></li>"); */
	}
	
});
function getTxnType(){
	return txnType;
	} 
	function triggerChange(){
		$('.consChange').trigger("change");
	}
function inspectionStatusDiv(val){
	if(val=='1'){
		
		$(".approved").show();
		$(".decline").hide();
	}else{
		$(".approved").hide();
		$(".decline").show();
	}
	
}	


function loadFarmer(call) {

	var selectedFarmer = call.value;
	$('#farmer').empty();
	$('#farmer').select2();
	if(!isEmpty(selectedFarmer)){
		 $.post("dynamicCertification_populateFarmer", {selectedVillage: selectedFarmer}, function (data) {
			 insertOptions("farmer",$.parseJSON(data));
	     });
	}
			
}


function loadFarmCrops(call) {

	var selectedFarmer = call.value;
	$('#farmCropList').empty();
	if(!isEmpty(selectedFarmer)){
		 $.post("dynamicCertification_populateFarmCrops", {selectedFarm: selectedFarmer}, function (data) {
	     
			 insertOptions("farmCropList",$.parseJSON(data));
	     });
	}
			
}

function loadFarm(call) {

	var selectedFarmer = call.value;
	
	if(!isEmpty(selectedFarmer)){
		 $.post("dynamicCertification_populateFarm", {selectedFarmer: selectedFarmer}, function (data) {
	     
			 insertOptions("farmList",$.parseJSON(data));
	     });
	}
			
}
function loadICSType(call) {

	var selectedFarm = call.value;
	
	if(!isEmpty(selectedFarm)){
		 $.post("dynamicCertification_populateICSType", {selectedFarm: selectedFarm}, function (data) {
	   
			 insertOptions("icsType",$.parseJSON(data));
				
			});
	     
	}
			
}
function cancel() {
	
	 //window.location.href="dynamicCertification_list.action?txnType="+txnType+"&entityType="+entityType;
	window.location.href="dynamicCertification_list.action?txnType="+txnType;
	
}
function buttonEdcCancel(){
	//refreshPopup();
	document.getElementById("model-close-edu-btn").click();	
	
	 //window.location.href="dynamicCertification_list.action?txnType="+txnType+"&entityType="+entityType;
	window.location.href="dynamicCertification_list.action?txnType="+txnType;
	
 }
function sameAsFarmerAddress(element) {

        farmer = '<s:property value="farmerId" />';
        if (farmer == null || farmer == "") {
            farmer = document.getElementById('farmer').value;
        }
        $.post("dynamicCertification_detailFarmAddressSameAsFarmer", {
            selectedFarmerId: farmer
        }, function (data) {
            if (data != "") {
                document.getElementById('addressTxt').value = data;
                document.getElementById('addressTxt').disabled = true;
            }
        });
}

function farmAreas(element) {

    farm = '<s:property value="farmList" />';
    if (farm == null || farm == "") {
        farm = document.getElementById('farmList').value;
    }
    $.post("dynamicCertification_detailFarmArea", {
        selectedFarmerId: farm
    }, function (data) {
        if (data != "") {
            document.getElementById('areaTxt').value = data;
            document.getElementById('areaTxt').disabled = true;
        }
    });
}
 
function save(){
	
		var hit=true;
		$("#sucessbtn").prop('disabled', true);
		jQuery("#validateError").text("");
		var dataa ="";
		var error = "";
		var farmer = $("#farmer").val();
		var farmList = $("#farmList").val();
		var farmCrop = $("#farmCropList").val();
		//alert("farmCrop"+farmCrop);
		var groupList =  $("#groupId").val();
		var season = $("#season").val();
		var inspectionStatus=$("input[type=radio][name=inspectionStatus]:checked").val();
		var icsType=$("#icsType").val();
		var correctiveActionPlan=$("#correctiveActionPlann").val();
		var inspectionDate=$("#insDate").val();
		var inspectorName=$("#inspectorName").val();
		var mbeNo=$("#inspectorMobile").val();
		var inspectionType=$("#insType").val();
		var scope=$("#scope").val();
		var totLand=$("#totLand").val();
		var orgLand=$("#orgLand").val();
		var totSite=$("#totSite").val();
			if('<s:property value="command"/>'=="create"){
				 var isSeason = '<s:property value="seasonType"/>';
				if (isSeason == '1') {
					if(isEmpty(season)){
						 error = '<s:property value="%{getLocaleProperty('empty.season')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#season").focus();
							hit=false;
							enableButton();
							return false;
					}
				} 
				
				if(entityType=='1'){
			 	if(isEmpty(farmer)){
			 error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
				jQuery("#validateError").text(error);
				jQuery("#farmer").focus();
				hit=false;
				enableButton();
				return false;
		} 
				}	
				if(entityType=='5'){
					if(isEmpty(village)){
						 error = '<s:property value="%{getLocaleProperty('empty.village')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#villageList").focus();
							hit=false;
							enableButton();
							return false;
					}
					
					if(isEmpty(farmer)){
						 error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#farmer").focus();
							hit=false;
							enableButton();
							return false;
					} 
				}
				
				if(entityType=='2'){
					if(isEmpty(village)){
						 error = '<s:property value="%{getLocaleProperty('empty.village')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#villageList").focus();
							hit=false;
							enableButton();
							return false;
					}
					
					if(isEmpty(farmer)){
						 error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#farmer").focus();
							hit=false;
							enableButton();
							return false;
					} 
					
					if(isEmpty(farmList)){
						 error = '<s:property value="%{getLocaleProperty('empty.farm')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#farmList").focus();
							hit=false;
							enableButton();
							return false;
					}
				}
				 if(entityType=='3'){
					
					 if(isEmpty(groupList)){
					     
						 error = '<s:property value="%{getLocaleProperty('empty.samithi')}" />';
						
					     jQuery("#validateError").text(error);
					     jQuery("#groupId").focus();
					     hit=false;
					     enableButton();
					     return false;
					   }
					   }
				 
				 if(entityType=='6'){

						
						if(isEmpty(farmer)){
							 error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
								jQuery("#validateError").text(error);
								jQuery("#farmer").focus();
								hit=false;
								enableButton();
								return false;
						} 
						
						if(isEmpty(farmList)){
							 error = '<s:property value="%{getLocaleProperty('empty.farm')}" />';	
								jQuery("#validateError").text(error);
								jQuery("#farmList").focus();
								hit=false;
								enableButton();
								return false;
						}	
					 if(isEmpty(farmCrop)){
					 error = '<s:property value="%{getLocaleProperty('empty.farmCrop')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#farmCropList").focus();
						hit=false;
						enableButton();
						return false;
				} 
						}	
				 
 		if(entityType=='4'){
 		
 			if(isEmpty(farmer)){
				 error = '<s:property value="%{getLocaleProperty('empty.farmer')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#farmer").focus();
					hit=false;
					enableButton();
					return false;
			} 
			if(isEmpty(farmList)){
			 error = '<s:property value="%{getLocaleProperty('empty.farm')}" />';	
				jQuery("#validateError").text(error);
				jQuery("#farmList").focus();
				hit=false;
				enableButton();
				return false;
		}
			if(isEmpty(inspectionDate)){
				 error = '<s:property value="%{getLocaleProperty('empty.inspectionDate')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			if(isEmpty(inspectorName)){
				 error = '<s:property value="%{getLocaleProperty('empty.inspectorName')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			
			/* if(isEmpty(mbeNo)){
				 error = '<s:property value="%{getLocaleProperty('empty.mbeNo')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
				if(isNaN(mbeNo)==true){
					 error = '<s:property value="%{getLocaleProperty('invalid.mbeNo')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#inspectionDate").focus();
						hit=false;
						enableButton();
						return false;
					} */
			
			if(isEmpty(inspectionType)){
				 error = '<s:property value="%{getLocaleProperty('empty.inspectionType')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			if(isEmpty(scope)){
				 error = '<s:property value="%{getLocaleProperty('empty.scope')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			/* if(isEmpty(totLand)){
				 error = '<s:property value="%{getLocaleProperty('empty.totLand')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			if(isNaN(totLand)==true){
				 error = '<s:property value="%{getLocaleProperty('invalid.totLand')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			if(isEmpty(orgLand)){
				 error = '<s:property value="%{getLocaleProperty('empty.orgLand')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
			
			if(isNaN(orgLand)==true){
				 error = '<s:property value="%{getLocaleProperty('invalid.orgLand')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				} */
			
			/* if(isEmpty(totSite)){
				 error = '<s:property value="%{getLocaleProperty('empty.totSite')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				} */
			
			
			if(isNaN(totSite)==true){
				 error = '<s:property value="%{getLocaleProperty('invalid.totSite')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#inspectionDate").focus();
					hit=false;
					enableButton();
					return false;
				}
		
		if(isEmpty(inspectionStatus)){
			 error = '<s:property value="%{getLocaleProperty('empty.inspectionStatus')}" />';	
				jQuery("#validateError").text(error);
				jQuery("#inspectionStatus").focus();
				hit=false;
				enableButton();
				return false;
		 }
		 if(inspectionStatus=='1'){
			 if(isEmpty(icsType)){
				 error = '<s:property value="%{getLocaleProperty('empty.icsType')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#icsType").focus();
					hit=false;
					enableButton();
					return false;
			 }
		 }else{
			 if(isEmpty(correctiveActionPlan)){
				 error = '<s:property value="%{getLocaleProperty('empty.correctiveActionPlan')}" />';	
					jQuery("#validateError").text(error);
					jQuery("#correctiveActionPlann").focus();
					hit=false;
					enableButton();
					return false;
			 }
		 }
		} 
 		
		

					if(!isEmpty(error)){
						jQuery("#validateError").text(error);
						$("#sucessbtn").prop('disabled', true);
						return false;
					}
		
			}else if('<s:property value="command"/>'=="update" || '<s:property value="command"/>'=="updateActPlan" ){
				if(entityType=='4'){
			 	
				if(isEmpty(inspectionStatus)){
					 error = '<s:property value="%{getLocaleProperty('empty.inspectionStatus')}" />';	
						jQuery("#validateError").text(error);
						jQuery("#inspectionStatus").focus();
						hit=false;
						enableButton();
						return false;
				 }
				 if(inspectionStatus=='1'){
					 if(isEmpty(icsType)){
						 error = '<s:property value="%{getLocaleProperty('empty.icsType')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#icsType").focus();
							hit=false;
							enableButton();
							return false;
					 }
				 }else{
					 if(isEmpty(correctiveActionPlan)){
						 error = '<s:property value="%{getLocaleProperty('empty.correctiveActionPlan')}" />';	
							jQuery("#validateError").text(error);
							jQuery("#correctiveActionPlann").focus();
							hit=false;
							enableButton();
							return false;
					 }
				 }
				}
			}
			
if(hit){
	 selectedId="";
	if(entityType=='1' || entityType=='5'){
		selectedId=$("#farmer").val();
	}else if(entityType=='2'){
		selectedId=$("#farmList").val();
	}else if(entityType=='3'){
		selectedId=$("#groupId").val();
	}else if(entityType=='4'){
		selectedId=$("#farmList").val();
	}
	else if(entityType=='6'){
		selectedId=$("#farmCropList").val();
	}

	$("#selectedId").val(selectedId);
	
	if(addDynamicField()){
	//	alert("AAA");
		//$("#inspectionStatus").val(inspectionStatus);		
	//	$("#correctiveActionPlan").val(correctiveActionPlan);
		$("#entityType").val(entityType);
		$("#txnType").val(txnType);
		$("#farmerDynamicData_icsName").val(icsType);
		document.form.selectedId.value  =selectedId;
		document.form.submit();
		//var url="dynamicCertification_list?txnType="+typez+"&entityType="+entityType;
		
		// window.location.href="dynamicCertification_list?txnType="+typez+"&entityType="+entityType;
	}else{
		$("#sucessbtn").prop('disabled', false);
	}
}
	
	
	
}
function enableButton(){
	$("#sucessbtn").prop('disabled', false);
}
 function insertOptions(ctrlName, jsonArr) {
    document.getElementById(ctrlName).length = 0;
    addOption(document.getElementById(ctrlName), "Select", "");
    for (var i = 0; i < jsonArr.length; i++) {
        addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
    }
   
    var id="#"+ctrlName;
    jQuery(id).select2();
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
	
function getBranchIdDyn(){
	return null;
}
 
</script>


<s:form name="form" cssClass="fillform" enctype="multipart/form-data"
	action="dynamicCertification_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />

	<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />
	<s:hidden id="dynamicListArray" name="dynamicListArray" />
	<s:hidden key="farmerDynamicData.id" class="uId" />

	<s:hidden name="inspectionStatuss" id="inspectionStatus" />
	<s:hidden name="correctiveActionPlann" id="correctiveActionPlan" />
	<s:hidden name="entityType" id="entityType" />
	<s:hidden id="seasonType" name="seasonType" />
	<s:hidden name="txnType" id="txnType" />
	<s:hidden name="farmerDynamicData.icsName"
		id="farmerDynamicData_icsName" />
	<s:hidden name="selectedId" id="selectedId" />

	<s:hidden key="command"  name="command" id="command" />
	<s:hidden key="temp" id="temp" />
	<!--start decorator body-->

	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<div class="error">
							<p class="notification">
								<%-- <span class="manadatory">*</span>
								<s:text name="reqd.field" /><br> --%>
								<div class="imgError">
									<s:text name=" Image Format : " />
								<s:text name="imageTypes" /><br>
								
								<s:text name="imgSizeMsg" />
								</div>
								<div id="validateError" class="ferror" style="text-align: center;"></div>
					
							</p>
						</div>
					</div>
				</div>


			</div>


			<font color="red"> <s:actionerror /></font><h2>

							
				<%-- <s:property
					value="%{getLocaleProperty('info.dynamicCertification')}" /> --%>
					<s:property value="infoName" />
					
					
			</h2>

			<div class="appContentWrapper marginBottom crearteDiv ">
				<div class="formContainerWrapper">
					<div class="flexform">
					

          <div class="flexform-item farmerDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.village')}" />
								<span class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="selectedVillage"
									list="villageList" id="village" headerKey=""
									headerValue="%{getText('txt.select')}"
									onchange="loadFarmer(this);" />

							</div>
						</div>
						<s:if test="currentTenantId=='wilmar'">
						<div class="flexform-item farmerDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.farmer')}" />
								<span class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="farmer"
									list="{}" id="farmer" headerKey=""
									headerValue="%{getText('txt.select')}"
									onchange="loadFarm(this);" />

							</div>
						</div>
						<%-- <div class="flexform-item farmerDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farmer.address')}" /></label>
							<div class="form-element">
							<s:textarea id="addressTxt"
								name="farmAddress"
								cssClass="form-control " cssStyle="height:50px" theme="simple" />
						</div>
						</div> --%>
						<div class="flexform-item farmDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.farm')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
							 <s:select cssClass="form-control input-sm select2"
									name="farmList" list="{}" id="farmList" headerKey=""
									headerValue="%{getText('txt.select')}" 
									onchange="loadICSType(this);loadFarmCrops(this);" /> 
									<%-- <s:select cssClass="form-control input-sm select2"
									name="farmList" list="farmListt" id="farmList" headerKey=""
									headerValue="%{getText('txt.select')}"
									/> --%>

							</div>
						</div>
						
								<%-- <div class="flexform-item farmDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.area')}" /></label>
							<div class="form-element">
							<s:textfield id="areaTxt"
								name="farmArea"
								cssClass="form-control " cssStyle="height:50px" theme="simple" />
						</div>
						</div> --%>
						</s:if>
						<s:else>
								<div class="flexform-item farmerDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.farmer')}" />
								<span class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="farmer"
									list="{}" id="farmer" headerKey=""
									headerValue="%{getText('txt.select')}"
									onchange="loadFarm(this);" />

							</div>
						</div>
								<div class="flexform-item farmDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.farm')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
							 <s:select cssClass="form-control input-sm select2"
									name="farmList" list="{}" id="farmList" headerKey=""
									headerValue="%{getText('txt.select')}" 
									onchange="loadICSType(this);loadFarmCrops(this);" /> 
									<%-- <s:select cssClass="form-control input-sm select2"
									name="farmList" list="farmListt" id="farmList" headerKey=""
									headerValue="%{getText('txt.select')}"
									/> --%>

							</div>
						</div>
						
						</s:else>
						
							<div class="flexform-item farmCropsDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.farmCrops')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
							 <s:select cssClass="form-control input-sm select2"
									name="farmCropListName" list="{}" id="farmCropList" headerKey=""
									headerValue="%{getText('txt.select')}" 
									/> 
								

							</div>
						</div>
						
						
						
						<div class="flexform-item groupDiv">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.group')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="group"
									list="groupList" id="groupId" headerKey=""
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>
						
						<div class="flexform-item seasonClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.season')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="season"
									list="seasonList" id="season" headerKey=""
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>
						
						<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.insDate')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
									<div class="form-element">
							<s:textfield name="insDate" id="insDate"
								readonly="true" theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								cssClass="date-picker form-control calender"/>
						</div>
							</div>
						</div>
						
								<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.scope')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="scope"
									list="scopeList" id="scope" headerKey=""
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>
						
						
									<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.insType')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm select2" name="insType"
									list="insTypeList" id="insType" headerKey="" listKey="key"
								listValue="value"
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>
						
						
						
						<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.inspectorName')}" /> <span
								class="manadatory">*</span></label>
							<div class="form-element">
								<s:textfield id="inspectorName" name="inspectorName" theme="simple"
								maxlength="25" cssClass="form-control" />
							</div>
						</div>
						
							<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.inspectorMobile')}" /> <!-- <span
								class="manadatory">*</span> --></label>
							<div class="form-element">
								<s:textfield id="inspectorMobile" name="inspectorMobile" theme="simple"
								maxlength="10" cssClass="form-control" onkeypress="return isNumber(event)"/>
							</div>
						</div>
						
						
						<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.totLand')}" /> <!-- <span
								class="manadatory">*</span> --></label>
							<div class="form-element">
								<s:textfield id="totLand" name="totLand" theme="simple"
								maxlength="10" cssClass="form-control" onkeypress="return isDecimal(event)"/>
							</div>
						</div>

						<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.orgLand')}" /> <!-- <span
								class="manadatory">*</span> --></label>
							<div class="form-element">
								<s:textfield id="orgLand" name="orgLand" theme="simple"
								maxlength="10" cssClass="form-control" onkeypress="return isDecimal(event)"/>
							</div>
						</div>
				
						<div class="flexform-item inspectClass">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dynamicCertification.totSite')}" /> </label>
							<div class="form-element">
								<s:textfield id="totSite" name="totSite" theme="simple"
								maxlength="10" cssClass="form-control" onkeypress="return isNumber(event)"/>
							</div>
						</div>
					</div>

				</div>
			</div>
			<div class="appContentWrapper marginBottom detailDiv">
	
	<div class="formContainerWrapper">
		<div class="aPanel">
			
		<div class="aContent dynamic-form-con">
				
			<s:if test="currentTenantId=='wilmar'">
				<div class="dynamic-flexItem2 dateDiv">
						<p class="flexItem">
									<s:property value="%{getLocaleProperty('date')}" />
								</p>
						<p class="flexItem">
								<s:property value="farmerDynamicData.txnDate" />
								</p>
				</div>
				<div class="dynamic-flexItem2 villageDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('village')}" />
								</p>
						<p class="flexItem">
									<s:property value="selectedVillage" />
								</p>
				</div>
				</s:if>
				<div class="dynamic-flexItem2 farmerDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.farmer')}" />
								</p>
						<p class="flexItem">
									<s:property value="farmer" />
								</p>
				</div>
<%-- <s:if test="currentTenantId=='wilmar'">
<div class="dynamic-flexItem2 farmerDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farmer.address')}" />
								</p>
						<p class="flexItem">
								<s:property value="address" />	
								</p>
				</div>
</s:if>	 --%>			
				<div class="dynamic-flexItem2 farmDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.farm')}" />
								</p>
						<p class="flexItem">
									<s:property value="farmList" />
								</p>
				</div>
				<%-- <s:if test="currentTenantId=='wilmar'">
				<div class="dynamic-flexItem2 farmerDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.area')}" />
								</p>
						<p class="flexItem" >
									<s:property value="area" />
								</p>
				</div>

</s:if> --%>
				<div class="dynamic-flexItem2 farmCropDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('variety')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmCropList.name" />
							</p>
						</div>
						
						<div class="dynamic-flexItem2 farmCropDiv">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('farmCrop')}" />
							</p>
							<p class="flexItem">
								<s:property value="farmCropList.procurementProduct.name" />
							</p>
						</div>
				
				
				<div class="dynamic-flexItem2 groupDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.group')}" />
								</p>
						<p class="flexItem">
									<s:property value="group" />
								</p>
				</div>
				
				<div class="dynamic-flexItem2 seasonClass">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.season')}" />
								</p>
						<p class="flexItem">
									<s:property value="season" />
								</p>
				</div>
				
				<div class="dynamic-flexItem2 icsDetailDiv">
						<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectionStatus')}" />
								</p>
						<p class="flexItem">
									<s:property value="inspectionStatus" />
								</p>
				</div>
			
				<s:if test="farmerDynamicData.conversionStatus==1">
				<div class="dynamic-flexItem2 icsDetailDiv">
						<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('dynamicCertification.icsType')}" />
									</p>
						<p class="flexItem">
										<s:property value="icsType" />
									</p>
						
				</div>
				
				</s:if>
							<s:else>
				
				<div class="dynamic-flexItem2 icsDetailDiv">
						<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('dynamicCertification.correctiveActionPlan')}" />
									</p>
						<p class="flexItem">
										<s:property value="correctiveActionPlan" />
									</p>
						
				</div>
							</s:else>
							
					<s:if test="currentTenantId=='wilmar'">
							<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.insDate')}" />
										
								</p>
							<p class="flexItem">
									<s:property value="insDate" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.scope')}" />
										
									
								</p>
							<p class="flexItem">
									<s:property value="scope" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.insType')}" />
									
									
								</p>
							<p class="flexItem">
									<s:property value="insType" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectorName')}" />
									
									
								</p>
							<p class="flexItem">
									<s:property value="inspectorName" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectorMobile')}" />
										
									
								</p>
							<p class="flexItem">
									<s:property value="inspectorMobile" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.totLand')}" />
										
									
								</p>
							<p class="flexItem">
									<s:property value="totLand" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.orgLand')}" />
									
									
								</p>
							<p class="flexItem">
									<s:property value="orgLand" />
								</p>
					</div>
					
					<div class="dynamic-flexItem2 icsOtherDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.totSite')}" />
										
									
								</p>
							<p class="flexItem">
									<s:property value="totSite" />
								</p>
					</div>
					
					
					</s:if>
							
			</div>
		</div>			
		</div>	
		</div>
		
			<div class="filterControls">




				<div class="dynamicFieldsRender"></div>



			</div>
		</div>
	</div>
	
		
	<div class="appContentWrapper marginBottom farmIcsDetailDiv">
		<div class="formContainerWrapper">
			<div class="flexform">


				<div class="flexform-item">
					<label for="txt"><s:property
							value="%{getLocaleProperty('dynamicCertification.inspectionStatus')}" />
						<span class="manadatory">*</span></label>
					<div class="form-element">
						<s:radio id="inspectionStatus" list="inspectionStatusList"
							name="inspectionStatus" listKey="key" listValue="value"
							theme="simple" onchange="inspectionStatusDiv(this.value);" />

					</div>
				</div>
				<div class="flexform-item approved">
					<label for="txt"><s:property
							value="%{getLocaleProperty('dynamicCertification.icsType')}" />
						<span class="manadatory">*</span></label>
					<div class="form-element">
						<s:select cssClass="form-control input-sm select2" name="icsType"
							list="icsList" id="icsType" headerKey=""
							headerValue="%{getText('txt.select')}" />

						<%-- 	<s:select cssClass="form-control" name="icsType" list="icsStatusList" id="icsType"
										headerKey="" headerValue="%{getText('txt.select')}" /> --%>

					</div>
				</div>

				<div class="flexform-item decline">
					<label for="txt"><s:property
							value="%{getLocaleProperty('dynamicCertification.correctiveActionPlan')}" />
						<span class="manadatory">*</span></label>
					<div class="form-element">
						<s:textarea name="correctiveActionPlan" id="correctiveActionPlann"
							maxlength="255" cssClass="form-control" />

					</div>
				</div>


			</div>

		</div>
	</div>
<div class="yui-skin-sam" id="loadList" style="display: block">
		<sec:authorize ifAllGranted="service.dynamicCertification.create">
			<span id="savebutton" class=""> <span class="first-child">
					<button id="sucessbtn" class="save-btn btn btn-success"
						type="button" class="save-btn" onclick="save()">
						<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
						</font>
					</button>
			</span>
			</span>
		</sec:authorize>
		<span class=""> <span class="first-child"><a
				id="cancelbutton" onclick="cancel();" class="cancel-btn btn btn-sts">
					<font color="#FFFFFF"> <s:text name="cancel.button" />
				</font>
			</a></span></span>
	</div>
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
						<s:property
							value="%{getLocaleProperty('info.dynamicCertification')}" />
					</h4>
				</div>
				<div class="modal-body">
					<b><p style="text-align: center; font-size: 120%;">
							<s:property value="%{getLocaleProperty('farmerDynamicSucess')}" />
						</p></b>
					<%-- 	<p style="text-align: center;" id="receiptNumber"><s:text name="receiptNumber"/></p> --%>
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

		<s:hidden id="area" />
		<s:hidden id="farmLatLon" />
		
</s:form>

<s:form name="cancelform" action="dynamicCertification_list.action">
	<s:hidden key="currentPage" />
	<s:hidden id="txnType" name="txnType" />
	<s:hidden id="entityType" name="entityType" />
	<s:hidden id="seasonType" name="seasonType" />
</s:form>
<s:form id="audioFileDownload" action="farmer_populateVideoPlay">
	<s:hidden id="loadId" name="imgId" />
</s:form>
<div class="pac-card hide" id="pac-card">
			<div id="pac-container">
				<input id="pac-input" type="text" placeholder="Enter a location">
			</div>
			<div class="pull-right">
				<button id="delete-button" onclick="deleteSelectedShape()">
					<i class="fa fa-trash"></i>Delete Plot
				</button>
				
					<button id="saveBtn" onclick="savePlotting()">
					<i class="fa fa-trash"></i>Save Plotting
				</button>
				
			</div>
		</div>
<script>


</script>
<script
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.28&libraries=geometry,drawing,places"></script>
	<script src="js/maplabel-compiled.js?k=2.16"></script>


