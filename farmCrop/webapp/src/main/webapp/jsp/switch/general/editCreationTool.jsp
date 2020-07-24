<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="swithlayout">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" href="bootstrap.min.css">
<script src="jquery.min.js"></script>
<script src="bootstrap.min.js"></script>
<script src="js/bootpopup.js"></script>
<script src="js/bootpopup.min.js"></script>

</head>

<body>

<style>
td, th {
	padding: 20px
}
</style>

<script type="text/javascript">
var dependencyKeyArray = new Array();
	$(document).ready(function() {
		
		$("#dfc_Id").val(<s:property value="id"/>);
		$("#ref_id").val(<s:property value="dfc.referenceId"/>);
		$("#dfc_componentType").val(<s:property value="dfc.componentType"/>);
		
		$("#componentTypesSelection_btn").hide();
		
		var catalogueTypeArray = new Array(); //for reset catalogueValuesDropDown
		
		showOrHideParentField_RadioButtons();
		
		//Field related
		populateSectionDropDown_fieldTab();	
		
		$("#componentTypesDiv").hide();
		//hideFields_fieldTab();
		showComponentMandatoryFields(<s:property value="dfc.componentType"/>);
		if("<s:property value="dfc.catalogueType"/>" != ""){
			chooseCatalogueTypeDropDown_fieldTab("type");
		}
		
		
		var mobAvl = "<s:property value="dfc.isMobileAvail"/>";
		
	 	if(mobAvl == 1){
			document.getElementById("firstRadioBtn_mbl").checked=true;
		}else{
			document.getElementById("secondRadioBtn_mbl").checked=true;
		}
		
		var is_required =  "<s:property value="dfc.isRequired"/>";
		
		if(is_required == 1){
			document.getElementById("firstRadioBtn_mandatory").checked=true;
		}else{
			document.getElementById("secondRadioBtn_mandatory").checked=true;
		} 
		
		var catalogueType = "<s:property value="dfc.catalogueType"/>";
		if(catalogueType != ""){
			
		if(catalogueType.includes("CG")){
			
			document.getElementById("secondRadioBtn_catalogueType").checked=true;
			$("#getCatalogueTypeDropDown_fieldTab").hide();
			document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
			$("#getCatalogueValuesDropDown_fieldTab").show();
			$("#optionTransfer_fieldTab").show();
			$("#buttonDiv1_fieldTab").hide();
			
			
			jQuery.post("creationTool_findCatalogueTypeByCataloueCode.action",{catalogueCode:catalogueType},function(result){
				//var catalogueTypeArray = new Array();
				var jsonData = $.parseJSON(result);
				$.each(jsonData, function(index, value) {
					catalogueTypeArray.push(value.name);
				});
				populateCatalogueValuesDropDown_fieldTab(catalogueTypeArray,catalogueType)
				
			});
			
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="RemoveAll"/>']")
					.addClass("btn btn-warning");
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="Add"/>']").addClass(
					"btn btn-small btn-success fa fa-step-forward");
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="Remove"/>']")
					.addClass("btn btn-danger");
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="AddAll"/>']")
					.addClass("btn btn-sts");
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="v"/>']")
					.addClass("btn btn-warning");
			$("#optionTransfer_fieldTab").find(
					"input[type='button'][value='<s:text name="^"/>']")
					.addClass("btn btn-success");
			
		}else{
			populateCatalogueTypezDropDown_fieldTab();
			document.getElementById("firstRadioBtn_catalogueType").checked=true;
			$("#getCatalogueValuesDropDown_fieldTab").hide();
			$("#optionTransfer_fieldTab").hide();
			document.getElementById("optionTransfer2_fieldTab").options.length=0;
			$("#getCatalogueTypeDropDown_fieldTab").show();
			$("#buttonDiv1_fieldTab").show();
			
		}
		
		}
		
	});		
	
	function cancelFormSubmit() {
		$("#cancelForm").submit();
	}
	
	function deleteFormSubmit(){
		$("#deleteForm").submit();
	}
	
	//field related
	
	function populateSectionDropDown_fieldTab(){
		jQuery.post("creationTool_populateSection.action",{},function(result){
			insertOptionsWithNoDefaultValue_sectionDropDown("sectionDropDown_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function populateCatalogueTypezDropDown_fieldTab(){
		var types = "<s:property value="dfc.catalogueType"/>";
		
		$.post("creationTool_populateExistingcatalogueType",{existingcatalogueType:types},function(result){
			var jsonArr = jQuery.parseJSON(result);
			$("#ExistingcatalogueType").val(jsonArr[0].name);
		});
		
		//following code need  when ever catalogue type and catalogue value both are enabled
		/* document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
		 $.post("creationTool_populateCatalogueValuesByType",{selectedType:types},function(result){
			 insertOptionsWithNoDefaultValue_catalogueTypezDropDown_fieldTab("catalogueTypezDropDown_fieldTab",jQuery.parseJSON(result));
		}); */
	}
	
	
	function insertOptionsWithNoDefaultValue_catalogueTypezDropDown_fieldTab(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			addOption_catalogueTypezDropDown_fieldTab(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}
	
	 function addOption_catalogueTypezDropDown_fieldTab(selectbox, text, value)
	    {
	        var optn = document.createElement("OPTION");
	        optn.text = text;
	        optn.value = value;
	        optn.selected="selected";
	        selectbox.options.add(optn);
	    }
	
	function populateCatalogueValuesDropDownReset_fieldTab(){
		
		//document.getElementById("optionTransfer2_fieldTab").options.length=0;
		var catalogueType;
		alert(catalogueTypeArray.length)
		for(var i = 0; i<catalogueTypeArray.length ; i++){
			if(i == catalogueTypeArray.length){
				catalogueType=catalogueType+catalogueTypeArray[i];	
			}else{
				catalogueType=catalogueType+catalogueTypeArray[i]+",";	
			}
		}
		
		
		document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
		 $.post("creationTool_populateCatalogueValuesByType",{selectedType:catalogueType},function(result){
			 insertOptionsWithNoDefaultValue("catalogueValuesDropDown_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function populateCatalogueValuesDropDown_fieldTab(catalogueTypesArray,catalogueType_code){
		
		 arrayOfJsonArray = new Array();
		for(var i = 0; i<catalogueTypesArray.length ; i++){
			
			 document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
			 $.post("creationTool_populateCatalogueValuesByType",{selectedType:catalogueTypesArray[i]},function(result){
				 arrayOfJsonArray.push(jQuery.parseJSON(result));
				 insertOptionsWithNoDefaultValue_catalogueValue("catalogueValuesDropDown_fieldTab",arrayOfJsonArray,catalogueType_code,"catalogueValuesDropDown_fieldTab");
				 insertOptionsWithNoDefaultValue_catalogueValue("optionTransfer1_fieldTab",arrayOfJsonArray,catalogueType_code,"optionTransfer1");
				 insertOptionsWithNoDefaultValue_catalogueValue("optionTransfer2_fieldTab",arrayOfJsonArray,catalogueType_code,"optionTransfer2");
				
			});
			
		 }
		
		
		
		/* alert("a - "+a.length)
		for(var i = 0; i<a.length ; i++){
			alert("a - "+a[i])
		} */
		
		
		 /* document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
		 $.post("creationTool_populateCatalogueValuesByType",{selectedType:types},function(result){
			 insertOptionsWithNoDefaultValue("catalogueValuesDropDown_fieldTab",jQuery.parseJSON(result));
			 insertOptionsWithNoDefaultValue("optionTransfer1_fieldTab",jQuery.parseJSON(result));
			 
		}); */
		 
		
	}
	
	function insertOptionsWithNoDefaultValue_catalogueValue(ctrlName, arrayOf_jsonArray,catalogueType_code,string) {
		
		document.getElementById(ctrlName).length = 0;
		 for (var i = 0; i < arrayOf_jsonArray.length; i++) {
			
			var spiltJsonArray=arrayOf_jsonArray[i];
			for(var j=0;j<spiltJsonArray.length;j++)
				
				{	
					if(string=="optionTransfer2"){
						if(catalogueType_code.includes(spiltJsonArray[j].id)){
							addOption(document.getElementById(ctrlName), spiltJsonArray[j].name,spiltJsonArray[j].id);
							
							}
					}else if(string=="catalogueValuesDropDown_fieldTab"){
						addOption_catalogueValuesDropDown_fieldTab(document.getElementById(ctrlName), spiltJsonArray[j].name,spiltJsonArray[j].id);
					}else{
						if(catalogueType_code.includes(spiltJsonArray[j].id)){
							console.log(spiltJsonArray[j].id);
							console.log(spiltJsonArray[j].name);
							}else{
								addOption(document.getElementById(ctrlName), spiltJsonArray[j].name,spiltJsonArray[j].id);
							}
					}
						
				}
		} 
		
		
	}
	
	 function addOption_catalogueValuesDropDown_fieldTab(selectbox, text, value)
	    {
	        var optn = document.createElement("OPTION");
	        optn.text = text;
	        optn.value = value;
	        optn.selected="selected";
	        selectbox.options.add(optn);
	    }
	
	function changeCatalogueTypezDropDown_fieldTab(){
		$("#catalogueTypezDropDown_fieldTab").show();
		document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
		 jQuery.post("creationTool_getCatalogueTypezFromFarmCatalogueMaster.action",{},function(result){
			 insertOptions("catalogueTypezDropDown_fieldTab",jQuery.parseJSON(result));
			 insertOptions("catalogueValuesDropDown_fieldTab",jQuery.parseJSON(result));
		 }); 
	}
	
	
	
	function showComponentTypesDiv(){
		$("#componentTypesDiv").show();
	}
	
	function hideFields_fieldTab(){
		$("#componentTypesDiv").hide();
		$("#getComponentName_fieldTab").hide();
		$("#getComponentMaxLen_fieldTab").hide();
		$("#getComponentDataFormat_fieldTab").hide();
		$("#getIsMobileAvl_fieldTab").hide();
		$("#getIsReportAvl_fieldTab").hide();
		$("#getMandatory_fieldTab").hide();
		$("#getCatalogueType_fieldTab").hide();
		$("#getCatalogueTypeDropDown_fieldTab").hide();
		$("#getCatalogueValuesDropDown_fieldTab").hide();
		$("#optionTransfer_fieldTab").hide();
		$("#buttonDiv1_fieldTab").hide();
		$("#getComponentValidation_fieldTab").hide();
		
		//formula
		$("#getFieldsListDropDown_fieldTab").hide();
		$("#getExpressionDropDown_fieldTab").hide();
		$("#getControlsOfFormula_fieldTab").hide();
		$("#getFormulaLabel_fieldTab").hide();
		
		$("#catalogueTypezDropDown_fieldTab").hide();
		       
	}
	
	function showComponentMandatoryFields(componentType){
		switch (Number(componentType)) {
		
		case 1:
				hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Text Box";
				$("#getComponentName_fieldTab").show();
				$("#getComponentMaxLen_fieldTab").show();
				$("#getComponentValidation_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("1");
				$("#buttonDiv1_fieldTab").show();
				//$("#getComponentDataFormat_fieldTab").show();
				//$("#getIsReportAvl_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				break;
		    case 2:
		    	hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Radio Button";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show(); unhide when ever need cataloguetypes and values dropdown choosing by radio button
				$("#componentType_fieldTab").val("2");
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
		        break;
		    case 3:
		    	hideFields_fieldTab();
			    document.getElementById('selectedComponentType_fieldTab').innerHTML = "Date Picker";
			    $("#getComponentName_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("3");
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show();
				$("#getCatalogueType_fieldTab").show(); */
		        break;
		    case 4:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Drop-Down";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("4");
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
				break;
		    case 5:
		    	hideFields_fieldTab();
				document.getElementById('selectedComponentType_fieldTab').innerHTML = "Text Area";
				$("#getComponentName_fieldTab").show();
				$("#getComponentMaxLen_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#getComponentValidation_fieldTab").show();
				
				$("#componentType_fieldTab").val("5");
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show();
				$("#getCatalogueType_fieldTab").show(); */
		        break;
		    case  6:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Check Box";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("6");
				$("#buttonDiv1_fieldTab").show();
				/* $("#getComponentMaxLen_fieldTab").show();
				$("#getComponentDataFormat_fieldTab").show();
				$("#getIsReportAvl_fieldTab").show(); */
				break;
		    case  7:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Label (Formula)";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#componentType_fieldTab").val("7");
				populateFieldsListDropDown_fieldTab();
				$("#getFieldsListDropDown_fieldTab").show();
				$("#getExpressionDropDown_fieldTab").show();
				$("#getControlsOfFormula_fieldTab").show();
				$("#getFormulaLabel_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				
				break;
		    case  8: 
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "List";
		    	$("#getComponentName_fieldTab").show();
				$("#componentType_fieldTab").val("8");
				$("#buttonDiv1_fieldTab").show();
		    	break;
		    case  9:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Drop-Down Multiple";
		    	$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				//$("#getCatalogueType_fieldTab").show();
				$("#componentType_fieldTab").val("9");
				//$("#getComponentMaxLen_fieldTab").show();
				//$("#getComponentDataFormat_fieldTab").show();
				//$("#getIsReportAvl_fieldTab").show();
				break;
		    case 12:
		    	hideFields_fieldTab();
		    	document.getElementById('selectedComponentType_fieldTab').innerHTML = "Photo Component";
				$("#getComponentName_fieldTab").show();
				$("#getIsMobileAvl_fieldTab").show();
				$("#getMandatory_fieldTab").show();
				$("#componentType_fieldTab").val("12");
				$("#getEnableParent_fieldTab").show();
				$("#buttonDiv1_fieldTab").show();
				break;	
			
		    default:
		    	hideFields_fieldTab();
		    }
		}
	
	function isMobileAvl_fieldTab(val) {
		if (val.value== "yes") {
			$("#mobileAvl_fieldTab").val("1");
		}else{
			$("#mobileAvl_fieldTab").val("0");
		}
		
	}
	
	function isReportAvl_fieldTab(val) {
		if (val.value== "yes") {
			$("#reportAvl_fieldTab").val("1");
		}else{
			$("#reportAvl_fieldTab").val("0");
		}
	}
	
	function isMandatory_fieldTab(val) {
		if (val.value== "yes") {
			$("#mandatoryOrNot_fieldTab").val("1");
		}else{
			$("#mandatoryOrNot_fieldTab").val("0");
		}
		
	}
	
	function chooseCatalogueTypeDropDown_fieldTab(val) {
		// following condition for when only catalogue type is enabled
		if (val=="type") {
			$("#getCatalogueValuesDropDown_fieldTab").hide();
			$("#optionTransfer_fieldTab").hide();
			document.getElementById("optionTransfer2_fieldTab").options.length=0;
			$("#getCatalogueTypeDropDown_fieldTab").show();
			$("#buttonDiv1_fieldTab").show();
		}
		
		// following condition need  for when ever catalogue value and catalogue type both are enabled
		 /*  if (val.value=="type") {
			$("#getCatalogueValuesDropDown_fieldTab").hide();
			$("#optionTransfer_fieldTab").hide();
			document.getElementById("optionTransfer2_fieldTab").options.length=0;
			$("#getCatalogueTypeDropDown_fieldTab").show();
			$("#buttonDiv1_fieldTab").show();
		}
		if (val.value=="value") {
			$("#getCatalogueTypeDropDown_fieldTab").hide();
			document.getElementById("catalogueTypezDropDown_fieldTab").options.length=0;
			$("#getCatalogueValuesDropDown_fieldTab").show();
		}   */
		
	}
	
	function populateCatalogueValuesList() {
		var types=""+$("#catalogueValuesDropDown_fieldTab").val();
		 $.post("creationTool_populateCatalogueValuesByType",{selectedType:types},function(result){
			 insertOptionsWithNoDefaultValue("optionTransfer1_fieldTab",jQuery.parseJSON(result));
		}); 
		
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="RemoveAll"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="Add"/>']").addClass(
				"btn btn-small btn-success fa fa-step-forward");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="Remove"/>']")
				.addClass("btn btn-danger");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="AddAll"/>']")
				.addClass("btn btn-sts");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="v"/>']")
				.addClass("btn btn-warning");
		$("#optionTransfer_fieldTab").find(
				"input[type='button'][value='<s:text name="^"/>']")
				.addClass("btn btn-success");
		$("#buttonDiv1_fieldTab").hide();
		$("#optionTransfer_fieldTab").show();
	}

	function insertOptionsWithNoDefaultValue(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}
	
	function insertOptionsWithNoDefaultValue_sectionDropDown(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			if(jsonArr[i].id == "<s:property value="dfc.dynamicSectionConfig.sectionCode"/>"){
				addOption(document.getElementById(ctrlName), jsonArr[i].name,jsonArr[i].id);
			}
		}
		
		for (var i = 0; i < jsonArr.length; i++) {
			if(jsonArr[i].id != "<s:property value="dfc.dynamicSectionConfig.sectionCode"/>"){
				addOption(document.getElementById(ctrlName), jsonArr[i].name,jsonArr[i].id);
			}
		}
	}
	
	
	function updateFormSubmit() {
		
		if (document.getElementById('optionTransfer2_fieldTab') != null) {
			document.getElementById('optionTransfer2_fieldTab').multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById('optionTransfer2_fieldTab').options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById('optionTransfer2_fieldTab').options[x].selected = true;
			}//select all option when u click save button
		}
		
		var componentType = $("#componentType_fieldTab").val();
		
		var firstRadioBtn_enableParent = $('#firstRadioBtn_enableParent').prop('checked');
	if(firstRadioBtn_enableParent == true){
		var falgForParentDependentField = true;
		var parent_dependency_id = $("#parentFieldId").val();
		var parent_dependency_key = $("#parentFieldValues_fieldTab").val();
		
		if(!isEmpty(parent_dependency_id)){
			if(!isEmpty(parent_dependency_key)){
				falgForParentDependentField = true;
				$("#parentDependencyKey_fieldTab").val(parent_dependency_key);
				$("#parentDependentId_fieldTab").val(parent_dependency_id);
			}else{
				alert("Please select the parent dependency key");
				falgForParentDependentField = false;
			}
		}else{
			alert("Please select the parent field");
			falgForParentDependentField = false;
		}
	}else{
		falgForParentDependentField = true;
	}
		
		
	if(falgForParentDependentField){
		switch (Number(componentType)) {
			
			case 1:
					var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var validation = $("#validationDropDown").val();
					
					if((section != "") && (componentName != "") && (maxLen != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && validation != -1){
						$("#updateForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
			    case 2:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != ""))){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#updateForm").submit();
							}else{
								alert("Please enter all the mandatory fields 1")
							}
						}else{
							$("#updateForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields 2")
					}
					
			        break;
			    case 3:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var data_format = $("#componentDataFormat").val();
					if((section != "") && (componentName != "") && (data_format != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true)){
						$("#updateForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
			        break;
			    case 4:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != "")) ){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#updateForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#updateForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
			    case 5:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var maxLen = $("#componentMaxLen").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					
					if((section != "") && (componentName != "") && (maxLen != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true)){
						$("#updateForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
			        break;
			    case  6:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != "")) ){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#updateForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#updateForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
			    case 7:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var formulaLabel = $("#formulaLabel").val();
					if(!isEmpty(formulaLabel)){
						if((section != " ") && (componentName != "") && (first_mobile == true || second_mobile == true)){
							$("#updateForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
			        break;
			    case  8: 
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true)){
						$("#updateForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
			    	break;
			    	
			    case  9:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					var first_catalogueType = $('#firstRadioBtn_catalogueType').prop('checked');
					var second_catalogueType = $('#secondRadioBtn_catalogueType').prop('checked');
					var catalogueTypezDropDown = $("#catalogueTypezDropDown_fieldTab").val();
					var catalogueValuesDropDown = $("#catalogueValuesDropDown_fieldTab").val();
					var optionTransfer2 = $("#optionTransfer2_fieldTab").val();
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) && (first_catalogueType == true || second_catalogueType == true) && ((catalogueTypezDropDown != null) && (catalogueTypezDropDown != "")  || (catalogueValuesDropDown != null && catalogueValuesDropDown != "")) ){
						if(catalogueValuesDropDown != "" && catalogueValuesDropDown != null){
							if(optionTransfer2 != null && optionTransfer2 != ""){
								$("#updateForm").submit();
							}else{
								alert("Please enter all the mandatory fields")
							}
						}else{
							$("#updateForm").submit();
						}
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;
					
			    case 12:
			    	var section = $("#sectionDropDown_fieldTab").val();
					var componentName = $("#componentName").val();
					var first_mobile = $('#firstRadioBtn_mbl').prop('checked');
					var second_mobile = $('#secondRadioBtn_mbl').prop('checked');
					var first_mandatory = $('#firstRadioBtn_mandatory').prop('checked');
					var second_mandatory = $('#secondRadioBtn_mandatory').prop('checked');
					
					if((section != "") && (componentName != "") && (first_mobile == true || second_mobile == true) && (first_mandatory == true || second_mandatory == true) ){
						$("#updateForm").submit();
					}else{
						alert("Please enter all the mandatory fields")
					}
					break;	
					
			    default:
			    	hideFields_fieldTab();
			    }
	}
		//$("#updateForm").submit();
	}
	
	function populateFieldsListDropDown_fieldTab(){
		jQuery.post("creationTool_getFieldsListForFormula.action",{},function(result){
			insertOptions("fieldsListDropDown_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function clearFormulaLabel(){
		$("#formulaLabel").val("");
		dependencyKeyArray.length = 0;
	}
	
	function generateFormula(){
		var componentCode = "{"+document.getElementById('fieldsListDropDown_fieldTab').value+"}";
		var expression = document.getElementById('expressionDropDown_fieldTab').value;
		var dependencyKey = "";
		
		if(!isEmpty(componentCode) && componentCode != "{}"){
			var temp = componentCode.replace(/{/g , "");
			temp = temp.replace(/}/g , "");
			dependencyKeyArray.push(temp);
		}
		
		for (var i = 0; i < dependencyKeyArray.length; i++) {
			if(i == dependencyKeyArray.length-1){
        	   dependencyKey = dependencyKey+dependencyKeyArray[i];
           }else{
        	   dependencyKey = dependencyKey+dependencyKeyArray[i]+",";
           }
        }
		$("#dependencyKey_fieldTab").val(dependencyKey); 
		
		var formula = $("#formulaLabel").val(); 
		formula = formula+componentCode+expression;
		
		var idsArray=['fieldsListDropDown_fieldTab','expressionDropDown_fieldTab'];
		//resetSelect2(idsArray);
		
		if(formula != "{}"){
		$("#formulaLabel").val(formula);
		}
		
	}
	
 function showOrHideParentField_RadioButtons(){
		if(!isEmpty('<s:property value="dfc.parentDepen.id"/>')){
			document.getElementById("firstRadioBtn_enableParent").checked=true;
			populateParentRelatedFields_fieldTab('enable');
		
		}else{
			document.getElementById("secondRadioBtn_enableParent").checked=true;
		
		}
	}
 
 function populateParentRelatedFields_fieldTab(val){
		
		if(val == "enable"){
			
			var elementExists = document.getElementById("parentFieldId");
			if(elementExists == null){
				createDropDown();
			}
			
		}else{
			jQuery('#getParentFields_fieldTab').html('');
			jQuery('#getParentFieldValues_fieldTab').html('');
			jQuery('#getParentFields_fieldTab').removeClass("flexform-item");
			jQuery('#getParentFieldValues_fieldTab').removeClass("flexform-item");
		
		}
	}
	
 function createDropDown() {
		
		$("#getParentFields_fieldTab").addClass("flexform-item");
		var className = "";
		
		var select = $('<select/>').attr({
			class : className,
			id : 'parentFieldId',
			name : 'dfc.parentDepen.id',
			onchange : 'populateCatalogueValuesOfSelectedParentField(this.value)'
		});

		var option = "<option value=''>Select </option>";
		$(select).append(option);
		
		jQuery.post("creationTool_getParentFieldsList_ForEditDropDown.action",{},function(result){
		var values =	jQuery.parseJSON(result);
			$.each(values, function(index, value) {
				if('<s:property value="dfc.parentDepen.id"/>' == value.id){
					var option = "<option value='" + value.id + "' selected=selected >"+ value.name + "</option>";
					$(select).append(option);
				}else{
					var option = "<option value='" + value.id + "'>"+ value.name + "</option>";
					$(select).append(option);
				}
			});
			
		});
		
		var label = $("<label>").text('Parent');
		$("#getParentFields_fieldTab").append(label);
		var form_element = $("<div/>").addClass("form-element");
		form_element.append(select);
		$("#getParentFields_fieldTab").append(form_element);
	 
		$("#parentFieldId").val('<s:property value="dfc.parentDepen.id"/>');
		
		populateCatalogueValuesOfSelectedParentField('<s:property value="dfc.parentDepen.id"/>');
	}
 
 function populateCatalogueValuesOfSelectedParentField(parentFieldId){
		
		if(!isEmpty(parentFieldId)){
			
			var elementExists = document.getElementById("parentFieldValues_fieldTab");
			if(elementExists == null){
				
				$("#getParentFieldValues_fieldTab").addClass("flexform-item");
				var className = "select2";
				
				var select = $('<select/>').attr({
					class : className,
					id : 'parentFieldValues_fieldTab',
					name : 'dfc.parentDependencyKey',
					multiple : "multiple"
				});

				var option = "<option value=''>Select </option>";
				$(select).append(option);
				
				jQuery.post("creationTool_populateCatalogueValuesOfSelectedParentField.action",{parentFieldId:parentFieldId},function(result){
					 var jsonArr = jQuery.parseJSON(result);
					
					 var parentFieldCatalogueValues = '<s:property value="dfc.parentDependencyKey"/>';
				
					 if(!isEmpty(parentFieldCatalogueValues)){
						 var values = parentFieldCatalogueValues.split(",");
						 $.each(values, function(i,e){
							 $.each(jsonArr, function(index, value) {
								 if(value.id == e.trim()){
									  var option = "<option value='" + value.id + "' selected=selected >"+ value.name + "</option>";
										$(select).append(option);
									}
							});
						 });
						
						 
						 $.each(jsonArr, function(index, value) {
							 if(!values.includes(value.id)){
								var option = "<option value='" + value.id + "'  >"+ value.name + "</option>";
								$(select).append(option);	
							}
						 });
					}
					 
					
					
				});
				
				var label = $("<label>").text('Parent Field Values');
				$("#getParentFieldValues_fieldTab").append(label);
				var form_element = $("<div/>").addClass("form-element");
				form_element.append(select);
				$("#getParentFieldValues_fieldTab").append(form_element);

			}else{
				jQuery.post("creationTool_populateCatalogueValuesOfSelectedParentField.action",{parentFieldId:parentFieldId},function(result){
					 insertOptions("parentFieldValues_fieldTab",jQuery.parseJSON(result));
				});
			}
	
		}else{
				insertOptions("parentFieldValues_fieldTab",'');
			}
		$('.select2').select2();
	}
 
</script>			


<!-- common -->
<s:form id="cancelForm" action="creationTool_grid"></s:form>
<s:form id="deleteForm" action="creationTool_deleteField">
					<s:hidden id="dfc_Id" name="dfc.id"  />
					<s:hidden id="dfc_componentType" name="dfc.componentType"  />
					<s:hidden id="ref_id" name="dfc.referenceId" />
</s:form>



	<div class="tab-content">
		
		<!-- Field Tab -->
		
		<div id="fieldTab" class="tab-pane fade in active">
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>Edit Fields</h2>
				</div>
				
				<s:form id="updateForm" action="creationTool_updateField" >
					<div class="flexform">

						<div class="flexform-item">
							<label for="txt">Section<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<select id="sectionDropDown_fieldTab" name="dfc.dynamicSectionConfig.sectionCode"></select>
							</div>
						</div>

						<div class="flexform-item"  >
							<label for="txt">Component Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<button type="button" class="save-btn btn btn-success" id="componentTypesSelection_btn"
									cssClass="form-control" onclick="showComponentTypesDiv();">
									<font color="#FFFFFF"> <b><s:text name="Select" /></b></font>
								</button>
								<p id="selectedComponentType_fieldTab"></p>
							</div>
						</div>

						<div class="flexform-item" id="getComponentName_fieldTab">
							<label for="txt">Name<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentName" name="dfc.componentName" cssClass="form-control" maxlength="45" />
							</div>
						</div>  
						
						<div class="flexform-item" id="getComponentValidation_fieldTab">
							<label for="txt">Validation<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select headerKey="-1" headerValue="Select" id="validationDropDown"
									list="#{'1':'Alphabet', '2':'Number', '3':'Email', '4':'Decimal', '5':'AlphaNumeric'}"
									name="dfc.validation" value="dfc.validation" />
							</div>
						</div>
						
						<div class="flexform-item" id="getComponentMaxLen_fieldTab">
							<label for="txt">Max Length<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentMaxLen" name="dfc.componentMaxLength"
									cssClass="form-control" maxlength="45" />
							</div>
						</div>

						<div class="flexform-item" id="getComponentDataFormat_fieldTab">
							<label for="txt">Date Format<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="componentDataFormat" name="dfc.dataFormat"
									cssClass="form-control" maxlength="45" />
							</div>
						</div>

						<div class="flexform-item" id="getIsMobileAvl_fieldTab">
							<label for="txt">Is Mobile Available<sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_mbl"  type="radio" name="mobile" value="yes" onclick="isMobileAvl_fieldTab(this);"> Yes<br> 
								<input id="secondRadioBtn_mbl" type="radio" name="mobile" value="no" onclick="isMobileAvl(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getIsReportAvl_fieldTab">
							<label for="txt">Is report Available<sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<input type="radio" name="report" value="yes" onclick="isReportAvl_fieldTab(this);"> Yes<br> 
								<input id="secondRadioBtn" type="radio" name="report" value="no" onclick="isReportAvl(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getMandatory_fieldTab">
							<label for="txt">Mandatory<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_mandatory" type="radio" name="mandatory" value="yes" onclick="isMandatory_fieldTab(this);"> Yes<br> 
								<input id="secondRadioBtn_mandatory" type="radio" name="mandatory" value="no" onclick="isMandatory_fieldTab(this); "> No<br>
							</div>
						</div>

						<div class="flexform-item" id="getCatalogueType_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_catalogueType" type="radio" name="catalogueType" value="type" onclick="chooseCatalogueTypeDropDown_fieldTab(this);">Catalogue Types<br> 
								<input id="secondRadioBtn_catalogueType" type="radio" name="catalogueType" value="value" onclick="chooseCatalogueTypeDropDown_fieldTab(this); ">Catalogue Values<br>
							</div>
						</div>	

						<div class="flexform-item" id="getCatalogueTypeDropDown_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input type="text" id="ExistingcatalogueType" />
								<select style="width: 100%;" id="catalogueTypezDropDown_fieldTab" name="dfc.catalogueType"></select>
							</div>
							
							<button type="button" class="btn-success"
								onclick="changeCatalogueTypezDropDown_fieldTab();">
								<font color="#FFFFFF"> <b><s:text name="change" /></b></font>
							</button>

							<%-- <button type="button" class="btn-danger"
								onclick="populateCatalogueTypezDropDown_fieldTab();">
								<font color="#FFFFFF"> <b><s:text name="Reset" /></b></font>
							</button> --%>
						</div>

						<div class="flexform-item" id="getCatalogueValuesDropDown_fieldTab">
							<label for="txt">Catalogue Type<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<select id="catalogueValuesDropDown_fieldTab" multiple="true" onchange="populateCatalogueValuesList();"></select>
								
								<button type="button" class="btn-success" onclick="changeCatalogueTypezDropDown_fieldTab();">
									<font color="#FFFFFF"> <b><s:text name="change" /></b></font>
								</button>
								
								<%-- <button type="button" class="btn-danger" onclick="populateCatalogueValuesDropDownReset_fieldTab();">
									<font color="#FFFFFF"> <b><s:text name="Reset" /></b></font>
								</button> --%>
								
							</div>
						</div>
						
						<div class="flexform-item" id="getFieldsListDropDown_fieldTab">
							<label for="txt">Fields<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="fieldsListDropDown_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""
								 cssClass="form-control  select2" list="{}"/>
							</div>
						</div>
						
						
						
						<div class="flexform-item" id="getExpressionDropDown_fieldTab">
							<label for="txt">Expression<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select headerKey="" headerValue="Select" id="expressionDropDown_fieldTab" cssClass="form-control  select2"
									list="#{'+':'+', '-':'-', '*':'*', '/':'/', '(':'(', ')':')', '%':'%'}"  />
							</div>
						</div>
						
						<div class="flexform-item" id="getControlsOfFormula_fieldTab">
							<label for="txt">Generate Formula<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<button type="button" class="save-btn btn btn-success"
									onclick="generateFormula();">
									<font color="#FFFFFF"> <b><s:text name="Generate" /></b>
									</font>
								</button>
								
								<button type="button" class="save-btn btn btn-success"
									onclick="clearFormulaLabel();">
									<font color="#FFFFFF"> <b><s:text name="Clear" /></b>
									</font>
								</button>
							</div>
						</div>
						
						<div class="flexform-item" id="getFormulaLabel_fieldTab">
							<label for="txt">Formula<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield id="formulaLabel" name="dfc.formula" cssClass="form-control"   />
								
							</div>
						</div>
						
						<!--kishore  -->
						
						<div class="flexform-item" id="getEnableParent_fieldTab">
							<label for="txt">Enable Parent<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<input id="firstRadioBtn_enableParent" type="radio" name="enableParent" value="enable" onclick="populateParentRelatedFields_fieldTab(this.value);"> Enable<br> 
								<input id="secondRadioBtn_enableParent" type="radio" name="enableParent" value="disable" onchange="populateParentRelatedFields_fieldTab(this.value); "> Disable<br>
							
							</div>
						</div>
						<div id="getParentFields_fieldTab" ></div>
						<div id="getParentFieldValues_fieldTab" ></div>
						<!--kishore  -->
						<%-- <div class="flexform-item" id="getParentFieldValues_fieldTab">
							<label for="txt">Parent field Values<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="parentFieldValues_fieldTab" headerValue="%{getText('txt.select')}"  headerKey=""
								 cssClass="form-control select2" list="{}"  multiple="true" />
							</div>
						</div> --%>
						
					</div>

					<br>
					<div id="buttonDiv1_fieldTab">

						<button type="button" class="save-btn btn btn-success"
							onclick="updateFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="Update" /></b>
							</font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b>
							</font>
						</button>
						
						<button type="button" class="btnSrch btn btn-danger"
							id="cancelButton" onclick="deleteFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="delete.button" /></b>
							</font>
						</button>

					</div>


					<br>
					<div class="appContentWrapper marginBottom" id="optionTransfer_fieldTab">

						<s:text name="RemoveAll" var="rmvall" />
						<s:text name="Remove" var="rmv" />
						<s:text name="Add" var="add" />
						<s:text name="AddAll" var="addall" />
						<s:optiontransferselect id="optionTransfer1_fieldTab"
							cssClass="form-control "
							cssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
							doubleCssClass="form-control" buttonCssClass="optTrasel"
							allowSelectAll="false"
							buttonCssStyle="font-weight:bold!important;"
							allowUpDownOnLeft="true" labelposition="top"
							allowUpDownOnRight="true" name="selectedCatalogueCode" list="{}"
							leftTitle="<b>List of Catalogue values<b>"
							rightTitle="<b>Selected Catalogue values<b>"
							headerKey="headerKey" doubleName="selectedCatalogueCode"
							doubleId="optionTransfer2_fieldTab" doubleList="{}"
							doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
							addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
							addToRightLabel="%{add}"  />

						<br>
						<div id="button">
							<div class="form-element">

								<button type="button" class="save-btn btn btn-success"
									onclick="updateFormSubmit();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>

								<button type="button" class="btnSrch btn btn-warning"
									id="cancelButton" onclick="cancelFormSubmit();">
									<font color="#FFFFFF"> <b><s:text
												name="cancel.button" /></b>
									</font>
								</button>

								<button type="button" class="btnSrch btn btn-danger" id="cancelButton" onclick="deleteFormSubmit();">
									<font color="#FFFFFF"> <b><s:text name="delete.button" /></b></font>
								</button>

							</div>
						</div>

					</div>
					
					<s:hidden id="ref_id" name="dfc.referenceId" />
					<s:hidden id="dfc_Id" name="dfc.id"  />
					<s:hidden id="componentType_fieldTab" name="dfc.componentType" />
					<s:hidden id="mobileAvl_fieldTab" name="dfc.isMobileAvail" />
					<s:hidden id="reportAvl_fieldTab" name="dfc.isReportAvail" />
					<s:hidden id="mandatoryOrNot_fieldTab" name="dfc.isRequired" />
					<s:hidden id="dependencyKey_fieldTab" name="dfc.dependencyKey" />
					
					<s:hidden id="parentDependencyKey_fieldTab" name="parentDependencyKey" />
					<s:hidden id="parentDependentId_fieldTab" name="parentDependentId" />
					
				</s:form>
			</div>

			<%-- <div class="appContentWrapper marginBottom" id="optionTransfer_fieldTab">

				<s:text name="RemoveAll" var="rmvall" />
				<s:text name="Remove" var="rmv" />
				<s:text name="Add" var="add" />
				<s:text name="AddAll" var="addall" />
				<s:optiontransferselect id="optionTransfer1"
					cssClass="form-control "
					cssStyle="width:500px;height:450px;overflow-x:auto;"
					doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
					doubleCssClass="form-control" buttonCssClass="optTrasel"
					allowSelectAll="false" buttonCssStyle="font-weight:bold!important;"
					allowUpDownOnLeft="false" labelposition="top"
					allowUpDownOnRight="false" name="selectedCatalogueCode" list="{}"
					leftTitle="<b>List of Catalogue values<b>"
					rightTitle="<b>Selected Catalogue values<b>" headerKey="headerKey"
					doubleName="selectedCatalogueCode" doubleId="optionTransfer2"
					doubleList="{}" doubleHeaderKey="doubleHeaderKey"
					addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
					addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />


				<div id="button">
					<div class="form-element">

						<button type="button" class="save-btn btn btn-success"
							onclick="componentFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>

						<button type="button" class="btnSrch btn btn-warning"
							id="cancelButton" onclick="cancelFormSubmit();">
							<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b>
							</font>
						</button>

					</div>
				</div>

			</div> --%>
			
			<div class="appContentWrapper marginBottom" id="componentTypesDiv" >
				<button id="textBoxButton" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('1');">
					<font color="#FFFFFF"> <b><s:text name="Text Box" /></b></font>
				</button>

				<button id="radioButton" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('2');">
					<font color="#FFFFFF"> <b><s:text name="Radio button" /></b></font>
				</button>

				<button id="datePicker" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('3');">
					<font color="#FFFFFF"> <b><s:text name="Date Picker" /></b></font>
				</button>

				<button id="dropDown" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('4');">
					<font color="#FFFFFF"> <b><s:text name="Drop down" /></b></font>
				</button>

				<button id="textArea" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('5');">
					<font color="#FFFFFF"> <b><s:text name="Text Area" /></b></font>
				</button>

				<button id="checkBox" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('6');">
					<font color="#FFFFFF"> <b><s:text name="Check Box" /></b></font>
				</button>

				<button id="dropDownMultiple" type="button"
					class="save-btn btn btn-success" cssClass="form-control"
					onclick="showComponentMandatoryFields('9');">
					<font color="#FFFFFF"> <b><s:text
								name="Drop-down Multiple" /></b></font>
				</button>

				<%-- <button id="addButton" type="button" class="save-btn btn btn-success" cssClass="form-control" onclick="populateComponent('10');">
													<font color="#FFFFFF"> <b><s:text name="Add Button" /></b></font>
												</button> --%>

				<button id="list" type="button" class="save-btn btn btn-success"
					cssClass="form-control" onclick="showComponentMandatoryFields('8');">
					<font color="#FFFFFF"> <b><s:text name="List" /></b></font>
				</button>
			</div>
			
		</div>
		<!-- <div id="menu2" class="tab-pane fade">
			<h3>Menu 2</h3>

		</div> -->
		
		
	</div>


</body>
</html>