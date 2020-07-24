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

	$(document)
			.ready(
					function() {
						listDynamicFieldsBySectionCode_fieldTab();
						document.getElementById('selectedComponentType_fieldTab').innerHTML = "List";
						$("#ref_id").val(<s:property value="dfc.referenceId"/>);
						$("#dfc_Id").val(<s:property value="id"/>);
						$("#componentType_fieldTab").val("8");
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
					 	
							/* alert("dynamicSectionConfig - "+"<s:property value="dfc.dynamicSectionConfig.sectionCode"/>")
							//alert("code - "+"<s:property value="dfc.code"/>")
							alert("id - "+"<s:property value="dfc.id"/>")  */
						
						$("#dfc_Id").val(<s:property value="id"/>);
						$("#ref_id").val(<s:property value="dfc.referenceId"/>);
						$("#dfc_componentType").val(<s:property value="dfc.componentType"/>);

						populateSectionDropDown_fieldTab();
						$("#showListItems_optionTransfer").hide();
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="RemoveAll"/>']")
								.addClass("btn btn-warning");
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="Add"/>']")
								.addClass(
										"btn btn-small btn-success fa fa-step-forward");
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="Remove"/>']")
								.addClass("btn btn-danger");
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="AddAll"/>']")
								.addClass("btn btn-sts");
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="v"/>']")
								.addClass("btn btn-warning");
						$("#showListItems_optionTransfer")
								.find(
										"input[type='button'][value='<s:text name="^"/>']")
								.addClass("btn btn-success");
					
						hideDropDownsAndShowOptionTransfer(); //lattest method for invoke option transfer and hide unnecessary fields
						
					});

	/* function insertOptionsWithNoDefaultValue_sectionDropDown(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			
			if (jsonArr[i].id == "<s:property value="dfc.dynamicSectionConfig.sectionCode"/>") {
				addOption(document.getElementById(ctrlName), jsonArr[i].name,
						jsonArr[i].id);
			}
		}

	 	for (var i = 0; i < jsonArr.length; i++) {
			if (jsonArr[i].id != "<s:property value="dfc.dynamicSectionConfig.sectionCode"/>") {
				addOption(document.getElementById(ctrlName), jsonArr[i].name,
						jsonArr[i].id);
			}
		} 
	} */

	function cancelFormSubmit() {
		$("#cancelForm").submit();
	}

	function insertOptionsWithNoDefaultValue(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}

	function populateSectionDropDown_fieldTab() {
		jQuery.post("creationTool_populateSection.action", {},function(result) {
					//insertOptionsWithNoDefaultValue_sectionDropDown("sectionDropDown_fieldTab", jQuery.parseJSON(result));
					//insertOptions("sectionDropDown_fieldTab", jQuery.parseJSON(result));
					insertOptionsWithNoDefaultValue_sectionDropDown("sectionDropDown_fieldTab", jQuery.parseJSON(result));
		});
	}

	function populateListDropDown(obj) {
		jQuery.post("creationTool_getListComponents.action", {
			sectionCode : obj
		}, function(result) {
			insertOptions("listDropDown_listTab", jQuery.parseJSON(result));
		});
	}

	function showListItemsOptionTransfer(val) {
		var sectionCode = $("#sectionDropDown_fieldTab").val();
		var listId = val.value;
		jQuery.post(
				"creationTool_getLitsComponentsBySectionCodeAndListId.action",
				{
					sectionCode : sectionCode,
					id : listId
				}, function(result) {
					insertOptionsWithNoDefaultValue(
							"showListItems_optionTransfer2", jQuery
									.parseJSON(result));
				});
		$("#showListItems_optionTransfer").show();
	}

	/* function showListItemsOptionTransfer(val) {
		var sectionCode = $("#sectionDropDown_fieldTab").val();
		var listId = val.value;
		jQuery.post(
				"creationTool_getLitsComponentsBySectionCodeAndListId.action",
				{
					sectionCode : sectionCode,
					id : listId
				}, function(result) {
					insertOptionsWithNoDefaultValue(
							"showListItems_optionTransfer1", jQuery
									.parseJSON(result));
				});
		$("#showListItems_optionTransfer").show();
	} */

	function updateFormSubmit() {
		var listComponentsOrderId = [];
		if (document.getElementById('showListItems_optionTransfer2') != null) {
			document.getElementById('showListItems_optionTransfer2').multiple = true; //to enable all option to be selected
			for (var x = 0; x < document
					.getElementById('showListItems_optionTransfer2').options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById('showListItems_optionTransfer2').options[x].selected = true;
				listComponentsOrderId
						.push(document
								.getElementById('showListItems_optionTransfer2').options[x].value);
				//alert(document.getElementById('showListItems_optionTransfer2').options[x].value)
			}//select all option when u click save button
		}
		
		
		$("#referenceIdForListComponents").val("<s:property value="dfc.id"/>");
		$("#listComponentsOrder").val(listComponentsOrderId);
		$("#updateForm").submit();
		$("#updateListOrderForm").submit();
	}

	function deleteFormSubmit() {

		$("#dfc_Id").val(<s:property value="id"/>);
		$("#ref_id").val(<s:property value="dfc.referenceId"/>);
		$("#dfc_componentType").val(<s:property value="dfc.componentType"/>);

		$("#deleteForm").submit();
	}
	function hideDropDownsAndShowOptionTransfer(){
		jQuery.post(
				"creationTool_getLitsComponentsBySectionCodeAndListId.action",
				{
					sectionCode : '<s:property value="dfc.dynamicSectionConfig.sectionCode"/>',
					id : '<s:property value="dfc.id"/>'
				}, function(result) {
					insertOptionsWithNoDefaultValue(
							"showListItems_optionTransfer2", jQuery
									.parseJSON(result));
				});
		$("#showListItems_optionTransfer").show();
	}
	
	function listDynamicFieldsBySectionCode_fieldTab() {
		var sectionCode = "<s:property value="dfc.dynamicSectionConfig.sectionCode"/>";
		var myObj = [];
		jQuery.post("creationTool_listDynamicFieldsBySectionCode.action", {sectionCode:sectionCode},
			function(result) {
					
					var jsonArr = jQuery.parseJSON(result);
					jQuery.post("creationTool_getLitsComponentsBySectionCodeAndListId.action",
							{
								sectionCode : '<s:property value="dfc.dynamicSectionConfig.sectionCode"/>',
								id : '<s:property value="dfc.id"/>'
							}, function(result2) {
								
								
								for (var i = 0; i < jsonArr.length; i++) {
									var r1 = result;
									var r2 = result2;
									if(!r2.includes(jsonArr[i].id) ){
										if(!r2.includes(jsonArr[i].name)){
										item = {}
								        item ["id"] =  jsonArr[i].id;
								        item ["name"] = jsonArr[i].name;
										myObj.push(item);
										}
									}
								}
								
								insertOptionsWithNoDefaultValue("showListItems_optionTransfer1", myObj);
								
							});
					
					
					
				});
		
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
	
	function isMobileAvl_fieldTab(val) {
		if (val.value== "yes") {
			$("#mobileAvl_fieldTab").val("1");
		}else{
			$("#mobileAvl_fieldTab").val("0");
		}
		
	}
	
	function isMandatory_fieldTab(val) {
		if (val.value== "yes") {
			$("#mandatoryOrNot_fieldTab").val("1");
		}else{
			$("#mandatoryOrNot_fieldTab").val("0");
		}
		
	}
	
</script>

	<s:form id="cancelForm" action="creationTool_grid"></s:form>

	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>Edit List Fields Order</h2>
		</div>
	
<s:form id="updateForm" action="creationTool_updateField" >
			<div class="flexform "> 

			<div class="flexform-item ">
				<label for="txt">Section<sup style="color: red;">*</sup></label>
				<div class="form-element">
					<select id="sectionDropDown_fieldTab"
						name="dfc.dynamicSectionConfig.sectionCode"
						onchange="populateListDropDown(this.value);"></select>
				</div>
			</div>

			<div class="flexform-item">
				<label for="txt">Component Type<sup style="color: red;">*</sup></label>
				<div class="form-element">
					<p id="selectedComponentType_fieldTab"></p>
				</div>
			</div>


			<div class="flexform-item" id="getComponentName_fieldTab">
				<label for="txt">Name<sup style="color: red;">*</sup></label>
				<div class="form-element">
					<s:textfield id="componentName" name="dfc.componentName"
						cssClass="form-control" maxlength="45" />
				</div>
			</div>


			<div class="flexform-item" id="getIsMobileAvl_fieldTab">
				<label for="txt">Is Mobile Available<sup style="color: red;">*</sup></label>
				<div class="form-element">
					<input id="firstRadioBtn_mbl" type="radio" name="mobile"
						value="yes" onclick="isMobileAvl_fieldTab(this);"> Yes<br>
					<input id="secondRadioBtn_mbl" type="radio" name="mobile"
						value="no" onclick="isMobileAvl(this); "> No<br>
				</div>
			</div>

			<div class="flexform-item" id="getMandatory_fieldTab">
				<label for="txt">Mandatory<sup style="color: red;">*</sup></label>
				<div class="form-element">
					<input id="firstRadioBtn_mandatory" type="radio" name="mandatory"
						value="yes" onclick="isMandatory_fieldTab(this);"> Yes<br>
					<input id="secondRadioBtn_mandatory" type="radio" name="mandatory"
						value="no" onclick="isMandatory_fieldTab(this); "> No<br>
				</div>
			</div>

			<!-- <div class="flexform-item" id="getList_fieldTab">   
					<label for="txt">Choose List<sup style="color: red;">*</sup></label>
					<div class="form-element">
						<select id="listDropDown_listTab" name="dfc.referenceId"
							onchange="showListItemsOptionTransfer(this);"></select>
					</div>
			</div> -->			<!-- enable when list dropdown needed -->

			</div>
					<s:hidden id="ref_id" name="dfc.referenceId" />
					<s:hidden id="dfc_Id" name="dfc.id"  />
					<s:hidden id="componentType_fieldTab" name="dfc.componentType" />
					<s:hidden id="mobileAvl_fieldTab" name="dfc.isMobileAvail" />
					<s:hidden id="reportAvl_fieldTab" name="dfc.isReportAvail" />
					<s:hidden id="mandatoryOrNot_fieldTab" name="dfc.isRequired" />
					
				</s:form>			

	</div>

<s:form id="updateListOrderForm" action="creationTool_updateListOrder">
	<div class="appContentWrapper marginBottom" id="showListItems_optionTransfer">

		<s:text name="RemoveAll" var="rmvall" />
		<s:text name="Remove" var="rmv" />
		<s:text name="Add" var="add" />
		<s:text name="AddAll" var="addall" />
		<s:optiontransferselect id="showListItems_optionTransfer1"
			cssClass="form-control "
			cssStyle="width:500px;height:450px;overflow-x:auto;"
			doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
			doubleCssClass="form-control" buttonCssClass="optTrasel"
			allowSelectAll="false" buttonCssStyle="font-weight:bold!important;"
			allowUpDownOnLeft="true" labelposition="top"
			allowUpDownOnRight="true" name="" list="{}"
			leftTitle="<b>Available components in List<b>"
			rightTitle="<b>Selected components in List<b>" headerKey="headerKey"
			doubleName=""
			doubleId="showListItems_optionTransfer2" doubleList="{}"
			doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
			addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
			addToRightLabel="%{add}" />

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
					<font color="#FFFFFF"> <b><s:text name="cancel.button" /></b>
					</font>
				</button>

				<button type="button" class="btnSrch btn btn-danger"
						id="deleteButton" onclick="deleteFormSubmit();">
						<font color="#FFFFFF"> <b><s:text name="delete.button" /></b>
						</font>
				</button>

				</div>
		</div>

	</div>
	
	<s:hidden id="listComponentsOrder" name="listComponentsOrder"  />
	<s:hidden id="referenceIdForListComponents" name="referenceIdForListComponents"  />
	
</s:form>

<s:form id="deleteForm" action="creationTool_deleteField">
					<s:hidden id="dfc_Id" name="dfc.id"  />
					<s:hidden id="dfc_componentType" name="dfc.componentType"  />
					<s:hidden id="ref_id" name="dfc.referenceId" />
</s:form>


</body>
</html>