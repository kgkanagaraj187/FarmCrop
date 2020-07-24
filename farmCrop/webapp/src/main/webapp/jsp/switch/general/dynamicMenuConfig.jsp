<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>

<meta name="decorator" content="swithlayout">
</head>

<script type="text/javascript">
	$(document).ready(function() {
		$('#sectionDiv').on('click', function(e) {
			populateFields();
		});
/* if('<s:property value="command"/>'=='update'){
	populateFields();
} */

		/* if('<s:property value="command"/>'=='update'){
			$('.sectionSelect').hide();
			$('.fieldSelect').hide();
			
		} */
		
		menuNeeded();
	});

	$(function() {
		$("#dynOpt").find(
				"input[type='button'][value='<s:text name="RemoveAll"/>']")
				.addClass("btn btn-warning");
		$("#dynOpt").find("input[type='button'][value='<s:text name="Add"/>']")
				.addClass("btn btn-small btn-success fa fa-step-forward");
		$("#dynOpt").find(
				"input[type='button'][value='<s:text name="Remove"/>']")
				.addClass("btn btn-danger");
		$("#dynOpt").find(
				"input[type='button'][value='<s:text name="AddAll"/>']")
				.addClass("btn btn-sts");

		$("#dynOption").find(
				"input[type='button'][value='<s:text name="RemoveAll"/>']")
				.addClass("btn btn-warning");
		$("#dynOption").find(
				"input[type='button'][value='<s:text name="Add"/>']").addClass(
				"btn btn-small btn-success fa fa-step-forward");
		$("#dynOption").find(
				"input[type='button'][value='<s:text name="Remove"/>']")
				.addClass("btn btn-danger");
		$("#dynOption").find(
				"input[type='button'][value='<s:text name="AddAll"/>']")
				.addClass("btn btn-sts");

	})

	function populateFields() {
		//$('#field').empty();
		 var selVals = '';
		$('#selectedField option').each(function() {
			
			selVals += $(this).val() + ",";
		});
		
		//alert("selVals:" + selVals); 
		if(selVals == '{}'){

		$('#selectedField').empty();
		}
		var urlFormSecCodes = '';

		$("#selectedSections option").each(function() {
			urlFormSecCodes += $(this).val() + ",";
		});

		if (urlFormSecCodes !== '') {
			jQuery.post("menuCreationToolGrid_populateFields.action", {
				selectedName : urlFormSecCodes,
				selVals:selVals
			}, function(result) {
				if (result != null && result != '') {
					var jsonData = jQuery.parseJSON(result);
					$(jsonData).each(
							function(key, value) {
								$(value.dynamicField).each(
										function(k, v) {
											$("#availFields").append(
													"<option value='"+v.id+"'>"
															+ v.name
															+ "</option>");
										});
							});
				}
			});

		}

	}

	function onSubmit() {
		setOptionValues('selectedSections');
		setOptionValues('selectedField');
		
		var hit=true;
		
		document.form.submit();
	}

	function setOptionValues(id) {
		if (document.getElementById(id) != null) {
			document.getElementById(id).multiple = true; //to enable all option to be selected
			for (var x = 0; x < document.getElementById(id).options.length; x++)//count the option amount in selection box
			{ //alert( document.getElementById('optionTransfer2').options[x].value)
				document.getElementById(id).options[x].selected = true;
			}//select all option when u click save button
		}
	}
	function menuNeeded(obj){
		$('#parentMenuIdForSaveSubMenu').val("");
		$('#role_id_dropDown').val("");
		//alert( $('input[name=serviceM]:checked').val());
		
		if( $('input[name=reportM]:checked').val() == '1' ||  $('input[name=serviceM]:checked').val() == '1'){
			
			$('.serviceMenu').removeClass("hide");
			}else{
				$('.serviceMenu').addClass("hide");
			}
	}
</script>

<body>

	<s:form name="form" cssClass="fillform"
		action="menuCreationToolGrid_%{command}">

		<s:hidden key="currentPage" />
		<s:hidden key="id" />

		<s:if test='"update".equalsIgnoreCase(command)'>

			<s:hidden key="dynamicFeildMenuConfig.id" />
		</s:if>

		<s:hidden key="command" />
		<div class="appContentWrapper marginBottom">

			<div class="error">
				<sup>*</sup>
				<s:text name="reqd.field" />
				<s:actionerror />
				<s:fielderror />
			</div>

			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.menuConfig')}" />
				</h2>


				<div class="flexform-item">
					<label for="txt"><s:text name="dynamicMenu.name" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:textfield name="dynamicFeildMenuConfig.name" theme="simple"
							maxlength="45" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:text name="dynamicMenu.icon" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:textfield name="dynamicFeildMenuConfig.iconClass"
							theme="simple" maxlength="45" cssClass="form-control input-sm" />
					</div>
				</div>
				
			<%-- 	<div class="flexform-item">
					<label for="txt"><s:text name="dynamicMenu.txnType" /><sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:textfield name="dynamicFeildMenuConfig.txnType"
							theme="simple" maxlength="45" cssClass="form-control input-sm" />
					</div>
				</div>
 --%>
				<div class="flexform-item">
					<label for="txt"><s:text name="dynamicMenu.entity" /></label>
					<div class="form-element">
						<s:select id="entityType" name="dynamicFeildMenuConfig.entity"
							list="entityType" theme="simple" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="flexform-item">
						<label for="txt"> Service Needed?
						</label>
						<div class="form-element">
						<s:radio list="yesNoList" listKey="key"
											listValue="value" name="serviceM"  onchange="javascript:menuNeeded(this);"  id="serviceM" theme="simple" />
					</div>	
					</div>	
				<div class="flexform-item ">
						<label for="txt"> Report Needed?
						</label>
						<div class="form-element">
				<s:radio list="yesNoList" listKey="key"
											listValue="value" name="reportM"  id="reportM" onchange="javascript:menuNeeded(this);"  theme="simple" />
					
					
						</div>
</div>
				
				<div class="flexform-item serviceMenu hide" >
					<label for="txt">Role_Id (ese_role_ent,ese_role_menu)<sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="role_id_dropDown" list="roles" name="role" headerKey="0" headerValue="Select" onchange="getRoleId(this);" ></s:select>
						</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:text name="isSeason" /></label>
					<div class="form-element">
						<s:radio list="isSeasonList" listKey="key" listValue="value"
								name="dynamicFeildMenuConfig.isSeason" theme="simple" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:text name="isSingleRecord" /></label>
					<div class="form-element">
						<s:radio list="isSeasonList" listKey="key" listValue="value"
								name="dynamicFeildMenuConfig.isSingleRecord" theme="simple" />
					</div>
				</div>
				<div class="flexform-item" >
					<label for="txt"><s:text name="agentType" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							 <s:select id="agent_type_dropDown" list="agentType" name="dynamicFeildMenuConfig.agentType" headerKey="0" headerValue="Select" ></s:select>
						</div>
				</div>
				<div class="flexform-item">
					<label for="txt"><s:text name="webTxnType" /></label>
					<div class="form-element">
						<s:select id="txnType" name="dynamicFeildMenuConfig.txnType"
							list="txnTypeList" theme="simple" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:text name="mbeTxnType" /></label>
					<div class="form-element">
						<s:select id="mTxnType" name="mobTxnType"
							list="mTxnTypeList" theme="simple" headerKey="" 
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				
				

	
				<div class="appContentWrapper marginBottom sectionSelect">
					<div class="formContainerWrapper">
						<h2>
							<s:property value="%{getLocaleProperty('info.section')}" />
						</h2>
					</div>
					<div class="flexiWrapper">


						<div id="sectionDiv">

							<div id="dynOpt" class="col-xs-4">

								<div class="col-xs-10">
									<s:text name="%{getLocaleProperty('availableSection')}"
										var="availableTitle" />

								</div>

								<div class="col-xs-10">
									<s:text name="selectedSect" var="selectedTitle" />
									<s:text name="%{getLocaleProperty('availableSect')}"
										var="availableTitle" />
									<s:set var="availableTitle"
										value="%{getLocaleProperty('availableSection')}" />
									<s:set var="selectedTitle"
										value="%{getLocaleProperty('selectedSection')}" />
								</div>

								<div class="col-xs-10">
									<s:text name="*" var="reqdSymbol">
										<sup style="color: red;">*</sup>
									</s:text>
								</div>

								<s:text name="RemoveAll" var="rmvall" />
								<s:text name="Remove" var="rmv" />
								<s:text name="Add" var="add" />
								<s:text name="AddAll" var="addall" />

								<s:optiontransferselect id="optrnsfr" cssClass="form-control "
									cssStyle="width:500px;height:450px;overflow-x:auto;"
									doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
									doubleCssClass="form-control" buttonCssClass="optTrasel"
									allowSelectAll="false" 
									buttonCssStyle="font-weight:bold!important;"
									allowUpDownOnLeft="true" labelposition="top"
									allowUpDownOnRight="true" name="availableName"
									list="availableSection" leftTitle="%{availableTitle}"
									rightTitle="%{selectedTitle} %{reqdSymbol}"
									headerKey="headerKey" doubleName="selectedName"
									doubleId="selectedSections" doubleList="selectedSections"
									doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
									addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
									addToRightLabel="%{add}" />



							</div>
						</div>

					</div>

				</div>



				<div class="appContentWrapper marginBottom fieldSelect">
					<div class="formContainerWrapper">
						<h2>
							<s:property value="%{getLocaleProperty('info.field')}" />
						</h2>
					</div>
					<div class="flexiWrapper">


						<div id="FieldDiv">

							<div id="dynOption" class="col-xs-4">

								<div class="col-xs-10">
									<s:text name="%{getLocaleProperty('availableField')}"
										var="availableTitle" />

								</div>

								<div class="col-xs-10">
									<s:text name="selectedField" var="selectedTitle" />
									<s:text name="%{getLocaleProperty('availableFieldData')}"
										var="availableTitle" />
									<s:set var="availableTitle"
										value="%{getLocaleProperty('availableField')}" />
									<s:set var="selectedTitle"
										value="%{getLocaleProperty('selectedField')}" />
								</div>

								<div class="col-xs-10">
									<s:text name="*" var="reqdSymbol">
										<sup style="color: red;">*</sup>
									</s:text>
								</div>

								<s:text name="RemoveAll" var="rmvall" />
								<s:text name="Remove" var="rmv" />
								<s:text name="Add" var="add" />
								<s:text name="AddAll" var="addall" />

								<s:optiontransferselect id="availFields"
									cssClass="form-control "
									cssStyle="width:500px;height:450px;overflow-x:auto;"
									doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
									doubleCssClass="form-control" buttonCssClass="optTrasel"
									allowSelectAll="false"
									buttonCssStyle="font-weight:bold!important;"
									allowUpDownOnLeft="true" labelposition="top"
									allowUpDownOnRight="true" name="availableFieldName"
									list="availableField" leftTitle="%{availableTitle}"
									rightTitle="%{selectedTitle} %{reqdSymbol}"
									headerKey="headerKey" doubleName="selectedFieldName"
									doubleId="selectedField" doubleList="selectedField"
									doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
									addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
									addToRightLabel="%{add}" />
							</div>
						</div>

					</div>

				</div>

			</div>
			<div class="yui-skin-sam">
				<s:if test="command =='create'">
					<span id="button" class=""><span class="first-child">
							<button type="button" onclick="onSubmit()"
								class="save-btn btn btn-success">
								<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
								</font>
							</button>
					</span></span>
				</s:if>
				<s:else>
					<span id="button" class=""><span class="first-child">
							<button type="button" onclick="onSubmit()" class="save-btn btn btn-success">
								<font color="#FFFFFF"> <b><s:text
											name="update.button" /></b>
								</font>
							</button>
					</span></span>
				</s:else>
				<span id="cancel" class=""><span class="first-child"><button
							type="button" class="cancel-btn btn btn-sts">
							<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
							</font></b>
						</button></span></span>
			</div>

		</div>
	</s:form>
	<s:form name="cancelform" action="menuCreationToolGrid_list.action">
		<s:hidden key="currentPage" />
	</s:form>
</body>