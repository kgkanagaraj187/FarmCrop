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

	$(document).ready(function() {
		styleOfOptionTransferButtons();
		availableFieldsForList();
	});	
	
	function styleOfOptionTransferButtons(){
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
	}
	function availableFieldsForList(){
		jQuery.post("creationTool_availableFieldsForList.action",{},function(result){
			insertOptionsWithNoDefaultValue("optionTransfer1_fieldTab",jQuery.parseJSON(result));
		});
	}
	
	function insertOptionsWithNoDefaultValue(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}
	
	function updateFieldsReferenceId() {
		
		if (document.getElementById('optionTransfer2_fieldTab') != null) {
			document.getElementById('optionTransfer2_fieldTab').multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById('optionTransfer2_fieldTab').options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById('optionTransfer2_fieldTab').options[x].selected = true;
			}//select all option when u click save button
		}
		
		var selected_Fields = $("#optionTransfer2_fieldTab").val();
		selected_Fields = selected_Fields.toString();
		
		$.post("creationTool_updateFieldsReferenceId.action",{selList:selected_Fields},function(){
			$("#cancelForm").submit(); 
		});
	}
	
	function cancelFormSubmit() {
		$("#cancelForm").submit(); 
	}
	
</script>



<div class="appContentWrapper marginBottom">
	<div class="formContainerWrapper">
		<h2>Add Fields into List</h2>
	</div>
</div>

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
							allowUpDownOnRight="true" name="" list="{}"
							leftTitle="<b>List of Available Fields<b>"
							rightTitle="<b>Selected Fields<b>"
							headerKey="headerKey" doubleName=""
							doubleId="optionTransfer2_fieldTab" doubleList="{}"
							doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
							addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
							addToRightLabel="%{add}"  />

						<br>
						<div id="button">
							<div class="form-element">

								<button type="button" class="save-btn btn btn-success"
									onclick="updateFieldsReferenceId();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>

								<button type="button" class="btnSrch btn btn-warning"
									id="cancelButton" onclick="cancelFormSubmit();">
									<font color="#FFFFFF"> <b><s:text
												name="cancel.button" /></b>
									</font>
								</button>

								

							</div>
						</div>

					</div>
					


<s:form id="cancelForm" action="creationTool_grid"></s:form>


</body>
</html>