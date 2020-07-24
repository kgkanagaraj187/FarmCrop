<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<head>
<META name="decorator" content="swithlayout">
<s:head />
<style type="text/css">
.alignTopLeft {
	float: left;
	width: 6em;
}

#serviceLocationDiv select {
	width: 260px !important;
}

#serviceLocationDiv input {
	width: 100px;
	height: 24px;
	/*background:#204190;border:1px solid #224395 !important;*/
	color: #fff;
	font-size: 12px;
	text-align: left;
	padding-left: 18px;
}

#serviceLocationDiv td {
	border: none !important;
}

#serviceLocationDiv>table TD>label {
	font-weight: bold !important;
}

.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th,
	.table>thead>tr>td, .table>thead>tr>th {
	padding: 3.5px;
}
</style>
</head>
<script>
var tenant='<s:property value="getCurrentTenantId()"/>';
var command ='<s:property value="command"/>';
	jQuery(document).ready(function() {		
		$("#buttonAdd1").on('click', function (event) {  
           //event.preventDefault();
            var el = $(this);
            el.prop('disabled', true);
           // setTimeout(function(){el.prop('disabled', false); }, 1000);
      });
		if(tenant=="chetna" || tenant=='livelihood'){
			jQuery("#pratibhaWarehouseDiv").hide();
			if('<s:property value="command"/>'=="create"){
				jQuery(".groupSelect").hide();
				jQuery("#serviceLocationDiv").hide();
				jQuery("#warehouseDiv").hide();
				jQuery("#ginnerDiv").hide();
				jQuery("#spinnerDiv").hide();
				}else{
					//$('#selectedAgentType').prop('disabled',true);
					var agentType='<s:property value="selectedAgentType"/>';
					if(agentType=="1" || agentType=="2"){
						jQuery(".groupSelect").show();
						jQuery("#serviceLocationDiv").show();
						jQuery("#warehouseDiv").hide();
						jQuery("#ginnerDiv").hide();
						jQuery("#spinnerDiv").hide();
					}else if(agentType=="3"){
						jQuery(".groupSelect").show();
						jQuery("#serviceLocationDiv").show();
						jQuery("#warehouseDiv").show();
						jQuery("#ginnerDiv").hide();
						jQuery("#spinnerDiv").hide();
					}else if(agentType=="4"){
						jQuery(".groupSelect").show();
						jQuery("#serviceLocationDiv").hide();
						jQuery("#warehouseDiv").hide();
						jQuery("#ginnerDiv").show();
						jQuery("#spinnerDiv").hide();
					}else if(agentType=="5"){
						jQuery(".groupSelect").show();
						jQuery("#serviceLocationDiv").hide();
						jQuery("#warehouseDiv").hide();
						jQuery("#ginnerDiv").hide();
						jQuery("#spinnerDiv").show();
					}
					
				}
			loadSection(jQuery("#selectedAgentType").val());
			
		}else if(tenant=="pratibha"){
			var ware=$('#warehouseName').val();
			$('#hiddenWarehouseName').val(ware);
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").show();
			jQuery("#warehouseDiv").hide();
			jQuery("#ginnerDiv").hide();
			jQuery("#spinnerDiv").hide();
			jQuery("#pratibhaWarehouseDiv").show();
			 if('<s:property value="command"/>'=="update"){
				 $('#selectedAgentType').attr("disabled",true);
				 if(ware!=''){
					$('#warehouseName').attr("disabled",true);
				}
			 }
			 var agentType='<s:property value="selectedAgentType"/>';
			 if(agentType=="1" || agentType=="2"){
				jQuery("#pratibhaWarehouseDiv").show();
				if(agentType=="1"){
					jQuery("#lgDiv").hide();
				}
				else{
					jQuery("#lgDiv").show();
				}
			}
			if(agentType=="3"){
					jQuery("#pratibhaWarehouseDiv").hide();
					jQuery("#lgDiv").hide();
					jQuery(".groupSelect").hide();
					if('<s:property value="command"/>'=="update"){
						$('#warehouseName').val('')
						$(".select2").select2();
					}
			}
			
		}
		else{
			jQuery("#pratibhaWarehouseDiv").hide();
			var ware=$('#warehouseName').val();
			$('#hiddenWarehouseName').val(ware);
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").show();
			jQuery("#warehouseDiv").hide();
			jQuery("#ginnerDiv").hide();
			jQuery("#spinnerDiv").hide();
			 if('<s:property value="command"/>'=="update"){
					if(ware!=''){
						$('#warehouseName').attr("disabled",true);
					}
					 }
		}
		/* if(tenant=="pratibha"){
			
		}
		else{
			
		} */
		$(".select2").select2();
		var creInfo=($('input:radio[name="agent.status"]:checked').val());
		if(creInfo==0){
			jQuery("#passwordDiv").hide();
			jQuery("#confPasswordDiv").hide();
		}
		else{
			jQuery("#passwordDiv").show();
			jQuery("#confPasswordDiv").show();
		}
		
		var chkStatus='<s:property value="agent.trainingExists"/>';
		if(chkStatus=="true"){
			$(".trainingStatus").show();
		}else{
			$(".trainingStatus").hide();
			
		}
		
		var selectedTrainings ='<s:property value="agent.selectedtrainings" />';
		
		if (selectedTrainings!= null && selectedTrainings.trim() != "") {
            var values = selectedTrainings.split("\\,");

            $.each(selectedTrainings.split(","), function (i, e) {
               $("#trainingId option[value='" + e.trim() + "']")
                        .prop("selected", true);
            });
            $("#trainingId").select2();
        }
		 $("#trainingCheck").click(function(){
	            if($(this).prop("checked") == true){
	               $(".trainingStatus").show();
	            }
	            else if($(this).prop("checked") == false){
	            	$(".trainingStatus").hide();
	            }
	        });
		 
	
		
	});
	
	function enableFarmerPhotoModal(idArr,type) {
		try {
			$("#mbody").empty();
			var mbody = "";
			mbody = "";
			mbody = "<div class='item active'>";
			mbody += '<img src="fieldStaff_populateImage.action?id='
					+ idArr + '&type='+type+'"/>';
			mbody += "</div>";
			$("#mbody").append(mbody);
			
			document.getElementById("enablePhotoModal").click();
		} catch (e) {
			alert(e);
		}
	}
	
	function loadSection(val){
		//alert(val);
		var agentType= val;
		//alert(agentType);
		if(agentType=="1" || agentType=="2"){
			if(tenant=='pratibha'){
				var ware=$('#warehouseName').val();
				if(ware!=''){
					$('#warehouseName').attr("disabled",true);
				}
				else{
					$('#warehouseName').attr("disabled",false);
				}
				jQuery("#pratibhaWarehouseDiv").show();
				if(agentType=="1"){
					jQuery("#lgDiv").hide();
				}
				else{
					jQuery("#lgDiv").show();
				}
				}
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").show();
			jQuery("#warehouseDiv").hide();
			jQuery("#ginnerDiv").hide();
			jQuery("#spinnerDiv").hide();
		}else if(agentType=="3"){
			if(tenant=='pratibha'){
				$('#warehouseName').val('');
				$(".select2").select2();
			jQuery("#pratibhaWarehouseDiv").hide();
			jQuery("#lgDiv").hide();
			}
			else{
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").show();
			jQuery("#warehouseDiv").show();
			jQuery("#ginnerDiv").hide();
			jQuery("#spinnerDiv").hide();
			}
		}else if(agentType=="4"){
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").hide();
			jQuery("#warehouseDiv").hide();
			jQuery("#ginnerDiv").show();
			jQuery("#spinnerDiv").hide();
		}else if(agentType=="5"){
			jQuery(".groupSelect").show();
			jQuery("#serviceLocationDiv").hide();
			jQuery("#warehouseDiv").hide();
			jQuery("#ginnerDiv").hide();
			jQuery("#spinnerDiv").show();
		}
		
	}
</script>
<body>

	<s:form name="form" action="%{type}_%{command}" method="post" enctype="multipart/form-data"
		cssClass="fillform">
		<s:hidden key="currentPage" />
		<s:hidden key="id" id="id" />
		<s:hidden key="command" />
		<s:hidden name="agent.txnMode" value="3" />
		<s:hidden key="selecteddropdwon" id="listname" />
		<s:hidden key="temp" id="temp" />
		<s:hidden key="type" id="type" />
		<s:hidden key="agent.id" id="agentId" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="agent.bodStatus" id="agent.bodStatus" />
			<s:if test="card != null">
				<s:hidden key="card.id" id="card.id" />
				<s:hidden key="card.cardId" />
				<s:hidden key="card.cardType" />
			</s:if>
			<s:if test="account != null">
				<s:hidden key="account.id" />
				<s:hidden key="account.accountNo" />
				<s:hidden key="account.accountType" />
			</s:if>
		</s:if>

		<div class="appContentWrapper marginBottom">
			<div class="error">
				<sup>*</sup>
				<s:text name="reqd.field" />
				<s:actionerror />
				<s:fielderror />
			</div>
			<div class="formContainerWrapper">
				<h2>
					<s:text name='%{"info"+type}' />
				</h2>
				<div class="flexform">
					<s:if test='"update".equalsIgnoreCase(command)'>
						<s:if test='branchId==null'>
							<div class="flexform-item">
								<label for="txt"><s:text name="app.branch" /> </label>
								<div class="form-element">
									<label><s:property
											value="%{getBranchName(agent.branchId)}" /></label>
								</div>
							</div>
						</s:if>
						<div class="flexform-item">
							<label><s:text name='%{"agentId"+type}' /></label>
							<div class="form-element">
								<s:textfield id="agentID" name="agent.profileId" readonly="true"
									cssClass="form-control " />
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="flexform-item">
							<label><s:text name='%{"agentId"+type}' /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="agent.profileId" maxlength="12"
									cssClass="form-control" />
							</div>
						</div>
					</s:else>
					<div class="flexform-item">
						<label><s:text name="personalInfo.firstName" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="firstName" name="agent.personalInfo.firstName"
								theme="simple" size="25" cssClass="upercls form-control"
								maxlength="35" />
						</div>
					</div>
					<div class="flexform-item">
						<label><s:text name="personalInfo.lastName" /></label>
						<div class="form-element">
							<s:textfield id="lastName" name="agent.personalInfo.lastName"
								theme="simple" size="25" cssClass="upercls form-control "
								maxlength="35" />
						</div>
					</div>
				<s:if test="currentTenantId=='chetna' || currentTenantId=='pratibha' || currentTenantId=='livelihood'">
					<div class="flexform-item">
						<label><s:text name="agent.agentType" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select id="selectedAgentType" headerKey=""
								headerValue="%{getText('txt.select')}" name="selectedAgentType"
								list="agentTypes" listKey="id" listValue="name" theme="simple"
								cssClass="form-control  select2" onChange="loadSection(this.value)"/>
						</div>
					</div> 
				</s:if>
					<div class="flexform-item">
						<label><s:text name="personalInfo.identityType" /></label>
						<div class="form-element">
							<s:select id="identityType" headerKey=""
								headerValue="%{getText('txt.select')}"
								name="agent.personalInfo.identityType" list="identityTypeList"
								listKey="key" listValue="value" theme="simple"
								cssClass="form-control  select2" />
						</div>
					</div>

					<div class="flexform-item">
						<label><s:text name="personalInfo.identityNumber" /></label>
						<div class="form-element">
							<s:textfield name="agent.personalInfo.identityNumber"
								theme="simple" size="25" maxlength="16"
								cssClass="form-control  " />
						</div>
					</div>
					<s:if test="currentTenantId=='ocp'">
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('agent.photo')}" /> <s:if
								test="currentTenantId=='olivado'">
								<sup id="mandatory" style="color: red;">*</sup>
							</s:if> <span style="font-size: 8px"> <s:text
									name="farmer.imageTypes" /> <font color="red"> <s:text
										name="imgSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:file name="agentImage" id="agentImage"
								onchange="checkImgHeightAndWidth(this)" cssClass="form-control" />
							<s:if test="command =='update'">

								<button type='button' class='btn btn-sm pull-right photo'
									style='margin-right: 15%'
									onclick="enableFarmerPhotoModal(<s:property value="agent.id"/>,1)">
									<i class='fa fa-picture-o' aria-hidden='true'></i>
								</button>
							</s:if>
						</div>
					</div>
					</s:if>
					

					<div class="flexform-item">
						<label><s:text name="personalInfo.sex" /></label>
						<div class="form-element">
							<s:radio list="genderType" listKey="key" listValue="value"
								name="agent.personalInfo.sex" theme="simple" />
						</div>
					</div>

					<div class="flexform-item">
						<label><s:text name="personalInfo.dateOfBirth" /></label>
						<div class="form-element">
							<s:textfield name="dateOfBirth" theme="simple" size="25"
								maxlength="16" id="calendar" cssClass="form-control " />
						</div>
					</div>

				</div>
			</div>

		 <s:if test='"cooperativeManager".equalsIgnoreCase(type)'>
				<div class="flexi flexi10 flexi flexi10-flex-full">
					<label> <s:text name="agent.cooperative" /> <s:if
							test="type=='cooperativeManager'">
							<sup style="color: red;">*</sup>
						</s:if></label>
					<div class="form-element">
						<s:select id="cooperative" headerKey=""
							headerValue="%{getText('txt.select')}" list="cooperativeList"
							listKey="name" listValue="name" theme="simple"
							name="selectedCooperative" onchange="listWareHouses(this)"
							cssClass="form-control " />
					</div>

				</div>
			</s:if>
			<s:if test="type=='fieldStaff'">
				<s:hidden name="agent.agentType.id" value="2" />
			</s:if>
			<s:else>
				<s:hidden name="agent.agentType.id" value="1" />
			</s:else> 

		</div>

		<div class="appContentWrapper marginBottom">
			<div class="formContainerWrapper">
				<h2>
					<s:text name="info.contact" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label><s:text name="contactInfo.address" /></label>
						<div class="form-element">
							<s:textarea name="agent.contactInfo.address1" theme="simple"
								size="25" maxlength="150" cssClass="form-control "
								cssStyle="height:40px" />
						</div>
					</div>
					<s:if test="currentTenantId!='ocp'">
						<div class="flexform-item">
							<label><s:text name="contactInfo.phoneNumber" /></label>
							<div class="form-element">
								<s:textfield name="agent.contactInfo.phoneNumber" theme="simple"
									size="25" maxlength="12" cssClass="form-control " />
							</div>
						</div>
					</s:if>

					<div class="flexform-item">
						<label><s:text name="agentcontactInfo.mobileNumber" /></label>
						<div class="form-element">
							<s:textfield name="agent.contactInfo.mobileNumber" theme="simple"
								size="25" maxlength="15" cssClass="form-control " />
						</div>
					</div>

					<div class="flexform-item">
						<label><s:text name="agentcontactInfo.email" /></label>
						<div class="form-element">
							<s:textfield name="agent.contactInfo.email" theme="simple"
								size="25" maxlength="45" cssClass="form-control " />
						</div>
					</div>

				</div>
			</div>
		</div>

		<div class="appContentWrapper marginBottom">
			<div class="formContainerWrapper">
				<h2>
					<s:text name="info.credential" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label><s:text name="agent.status.name" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:radio list="statuses" id="status" listKey="key"
								listValue="value" name="agent.status" theme="simple"
								value="StatusDefaultValue" onchange="listpassword(this)" />
						</div>
					</div>

					<%-- <div class="flexform-item">
						<label><s:text name="accountBalance" /></label>
						<div class="form-element">
							<s:textfield name="agent.accountRupee"
								onkeypress="return isNumber(event)" maxlength="9"
								cssClass="form-control " style="width:68%" />

							<s:textfield name="agent.accountPaise" type="text"
								onkeypress="return isNumber(event)" maxlength="2"
								cssClass="form-control " style="width:30%;margin-left:2%" />
						</div>
					</div> --%>

					<div class="flexform-item" id="passwordDiv">
						<label><s:text name="agent.passwordName" /> <sup
							style="color: red;">*</sup></label>

						<div class="form-element">
							<s:password showPassword="true" name="agent.password"
								id="password" theme="simple" size="15" value="%{agent.password}"
								cssClass="form-control " maxlength="6" />
						</div>
					</div>

					<div class="flexform-item" id="confPasswordDiv">
						<label><s:text name="agent.confPasswordName" /> <sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:password showPassword="true" name="agent.confirmPassword"
								id="password1" theme="simple" size="15"
								value="%{agent.confirmPassword}" cssClass="form-control "
								maxlength="6" />
						</div>
					</div>


				</div>
			</div>
		</div>
		
<%-- 	
		<div class="appContentWrapper marginBottom">
			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.training')}" />
				</h2>
				<div class="flexform">
					<div class="flexform-item">
						<label><s:text name="trainingExists" /></label>
						<div class="form-element">
							<label><s:checkbox name="agent.trainingExists"
									id="trainingCheck" /></label>
						</div>
					</div>

					<div class="flexform-item trainingStatus">
						<label><s:text name="topic" /></label>
						<div class="form-element">
							<s:select list="trainingList" name="agent.selectedtrainings"
								listKey="key" listValue="value" theme="simple" id="trainingId"
								cssClass="form-control input-sm select2" multiple="true" />
						</div>
					</div>
				</div>
			</div>
		</div>
	 --%>

		<s:if test='"update".equalsIgnoreCase(command) &&  card != null  '>
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>
						<s:text name="info.card" />
					</h2>
					<div class="flexform">
						<div class="flexform-item">
							<label><s:text name="agent.card.id" /></label>
							<div class="form-element">
								<label><s:property value="card.cardId" /></label>
							</div>
						</div>

						<div class="flexform-item">
							<label><s:text name="status.card.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="cardStat" name="card.status" list="cardStatusList"
									listKey="key" listValue="value" theme="simple"
									cssClass="form-control " />
							</div>
						</div>

						<div class="flexform-item">
							<label><s:text name="rewrite.card.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="cardWrite" name="card.cardRewritable"
									list="cardRewriteList" listKey="key" listValue="value"
									theme="simple" cssClass="form-control " />
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>

		<s:if
			test='"update".equalsIgnoreCase(command) &&  account != null  && type=="fieldStaff"'>
			<div class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
					<h2>
						<s:text name="info.account" />
					</h2>
					<div class="flexform">
						<div class="flexform-item">
							<label><s:text name="agent.account.id" /></label>
							<div class="form-element">
								<s:property value="account.accountNo" />
							</div>
						</div>

						<div class="flexform-item">
							<label><s:text name="agent.account.type" /></label>
							<div class="form-element">
								<s:text name='%{account.accountType}' />
							</div>
						</div>

						<div class="flexform-item">
							<label><s:text name="Agentstatus.account.name" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="accountStat" name="account.status"
									list="accountStatusList" listKey="key" listValue="value"
									theme="simple" cssClass="form-control " />
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>
	
		<div class="appContentWrapper marginBottom groupSelect">
			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.samithi')}" />
				</h2>
			</div>
			<div class="flexiWrapper" id="lgDiv">
				<div class="flexi flexi10 flexi flexi10-flex-full"
					style="width: 100%; padding-top: 10px;">


					<s:if test="type =='fieldStaff'">
	
						
						<div id="serviceLocationDiv">
				<%-- 	<label><s:property
								value="%{getLocaleProperty('profile.samithi')}" /><sup
							style="color: red;">*</sup></label> --%>
							<div id="dynOpt" class="col-xs-4">
								<s:if test='"update".equalsIgnoreCase(command)'>
									<div class="col-xs-10">
										<s:text name="%{getLocaleProperty('availableWarehouse')}"
											var="availableTitle" />

									</div>
									<div class="col-xs-10">
										<s:text name="selectedWarehouse" var="selectedTitle" />
										<s:text
											name="%{getLocaleProperty('availableWarehouse.yield')}"
											var="availableTitle" />
										<s:set var="availableTitle"
											value="%{getLocaleProperty('availableWarehouse')}" />
										<s:set var="selectedTitle"
											value="%{getLocaleProperty('selectedWarehouse')}" />
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
										allowUpDownOnLeft="false" labelposition="top"
										allowUpDownOnRight="false" name="availableName"
										list="availableWarehouse" leftTitle="%{availableTitle}"
										rightTitle="%{selectedTitle} %{reqdSymbol}"
										headerKey="headerKey" doubleName="selectedName"
										doubleId="select" doubleList="selectedWarehouse"
										doubleHeaderKey="doubleHeaderKey"
										addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
										addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />
								</s:if>
								<s:else>
									<s:set var="availableTitle"
										value="%{getLocaleProperty('availableWarehouse')}" />
									<s:set var="selectedTitle"
										value="%{getLocaleProperty('selectedWarehouse')}" />
									<s:text name="*" var="reqdSymbol">

									</s:text>
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
										allowUpDownOnLeft="false" labelposition="top"
										allowUpDownOnRight="false" name="availableName"
										list="availableWarehouse" leftTitle="%{availableTitle}"
										rightTitle="%{selectedTitle} %{reqdSymbol}"
										headerKey="headerKey" doubleName="selectedName"
										doubleId="select" doubleList="selectedWarehouse"
										doubleHeaderKey="doubleHeaderKey"
										addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
										addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />
								</s:else>
							</div>
						</div>
						
					</s:if>
					
					


				</div>
			</div>

			<div id="warehouseDiv">
						<div class="flexform-item" >
						<label for="txt"><s:property
								value="%{getLocaleProperty('procurementCenter')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedProcurementCenter"
								list="listProcurementCenter" headerKey=""
								headerValue="Select" lisykey="key"
								listValue="value" id="selectedProcurementCenter"
								cssClass="col-sm-6 form-control select2" />
						</div>
					</div>
						</div>
						<div id="ginnerDiv">
						<div class="flexform-item" >
						<label for="txt"> <s:property
							value="%{getLocaleProperty('to')}" /><sup style="color: red;">*</sup>
					</label>
						<div class="form-element">
							<s:select name="selectedGinningCenter" list="ginnerCenterList" headerKey=""
							headerValue="%{getText('txt.select')}" id="selectedGinningCenter"
							cssClass="select2 form-control" />
						</div>
					</div>
						
						</div>
						
				<div id="spinnerDiv">
						<div class="flexform-item" >
						<label for="txt"> <s:property
							value="%{getLocaleProperty('spinner')}" /><sup style="color: red;">*</sup>
					</label>
						<div class="form-element">
							<s:select name="selectedSpinner" list="spinnerList" headerKey=""
							headerValue="%{getText('txt.select')}" id="selectedSpinner"
							cssClass="select2 form-control" />
						</div>
					</div>
						
						</div>
				
					<div id="pratibhaWarehouseDiv">
						<div class="flexform-item" >
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="warehouseName"
								list="warehouseList" headerKey=""
								headerValue="Select" lisykey="key"
								listValue="value" id="warehouseName"
								cssClass="col-sm-6 form-control select2" />
								<!-- <input type="hidden" name="warehouseName" id="hiddenWarehouseName"/> -->
						</div>
					</div>
						</div>

		</div>

<div class="yui-skin-sam">
				<s:if test="command =='create'">
					<span id="button" class=""><span class="first-child">							
							<button onclick="event.preventDefault();onSubmit();"
							class="btn btn-success" id="buttonAdd1">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
					</span></span>


					<span id="cancel" class=""><span class="first-child"><button
								type="button" class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
								</font></b>
							</button></span></span>

					<%-- <span class=""><span class="first-child"><a
			id="cancel.link"
			href="<%=request.getParameter("type")%>_list.action?type=<%=request.getParameter("type")%>"
			class="cancel-btn"> <font color="#FFFFFF"> <s:text
			name="cancel.button" /> </font> </a></span></span> --%>
				</s:if>
				<s:else>
					<span id="button1233" class=""><span class="first-child">
							<button type="button" onclick="onUpdateClick()"
								class="save-btn btn btn-success">
								<font color="#FFFFFF"> <b><s:text
											name="update.button" /></b>
								</font>
							</button>
					</span></span>


					<span id="cancel" class=""><span class="first-child"><button
								type="button" class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
								</font></b>
							</button></span></span>


					<%-- <script>
			//listServiceLocation(document.getElementById("servicePoint"));
			//listWareHouses(document.getElementById("servicePoint"));
		</script> --%>
				</s:else>
			</div>



		<script>
			function getTwentyYearsBack(){
				return new Date().getFullYear()-20;
			}

			jQuery(function($){
				$("#calendar").datepicker({
					defaultDate : new Date(getTwentyYearsBack(), 01, 00),
					autoclose: true
				});
			});
	</script>
	</s:form>
	<s:form name="cancelform" action="%{type}_list?type=%{type}">
		<s:hidden key="currentPage" />
	</s:form>
	<s:form name="serviceLocation"
		action="%{type}_populateServiceLocation.action">
	</s:form>
	<s:if test='pendingMTNTExists==true'>
		<script>
		var actionFrom = "%{type}";
		function submitForm(){
			
			onUpdateClick();
		}
	</script>
		<s:include value="procurementProductTransferPopup.jsp"></s:include>
	</s:if>


	<script type="text/javascript">

function listState(call){	
	var callback = {
    	success: function (oResponse) 	{   
		var result=oResponse.responseText;
		var arry =populateValues(result);
		document.form.selectedState.length = 0;
		addOption(document.form.state, "Select", "");
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
			addOption(document.form.state, arry[i], arry[i]);
		}
		listLocality(document.getElementsByName('selectedState')[0]);
		}
	}
    var data = "selectedCountry="+call.value;	 
	var url='<%=request.getParameter("type")%>_populateState';	
	var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
}

function listLocality(call){	
	var callback = {
    	success: function (oResponse) 	{
		var result=oResponse.responseText;
		var arry =populateValues(result);
		document.form.localities.length = 0;
		addOption(document.form.localities, "Select", "");
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
			addOption(document.form.localities, arry[i], arry[i]);
		}
		listMunicipality(document.getElementsByName('selectedLocality')[0]);
		}
    }
   	var data = "selectedState="+call.value;	 
	var url='<%=request.getParameter("type")%>_populateLocality.action';	   
    var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
}
function listMunicipality(call){	
	var callback = {
    	success: function (oResponse) 	{
		var result=oResponse.responseText;
		var arry =populateValues(result);
		document.form.city.length = 0;
		addOption(document.form.city, "Select", "");
		
		for (var i=0; i < arry.length;i++){
			if(arry[i]!="")
			addOption(document.form.city, arry[i], arry[i]);
		}
		}
    }
   	var data = "selectedLocality="+call.value;	 
	var url='<%=request.getParameter("type")%>_populateCity.action';	   
    var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);	   
}


function listpassword(call){
		var status=call.value;
		jQuery("#passwordDiv").hide();
		jQuery("#confPasswordDiv").hide();
		if (status==1){
			jQuery("#passwordDiv").show();
			jQuery("#confPasswordDiv").show();
		}
}

function listServiceLocation(call){
	try{
	   	var data = call.value;	
	   	if(jQuery.trim(data)!="-1"){  
		    var tempId = document.getElementById('agent.id').value;        
		    var url =  "<%=request.getParameter("type")%>_populateServiceLocation.action";
	        $.post(url,{servicePointName:data,id:tempId},function(result){           
	             $("#serviceLocationDiv").html(result);
	        });
	   	}
	   	else{
	   		$("#serviceLocationDiv").html("");
	   	}
   	}
   	catch(e){			
	}
}

function listWareHouses(call){
	try{
	   	var data = call.value;	
	   	if(jQuery.trim(data)!="-1"){  
		    var tempId = document.getElementById('agentId').value;   
		    document.getElementById('test').innerHTML = tempId;
		    var url =  "<%=request.getParameter("type")%>_populateWareHouse.action";
	        $.post(url,{cooperativeName:data,id:tempId,dt:new Date()},function(result){           
	        	$("#serviceLocationDiv").html("");
		        $("#serviceLocationDiv").html(result);
	        });
	   	}
	   	else{
	   		$("#serviceLocationDiv").html("");
	   	}
   	}
   	catch(e){			
	}
}

function onSubmit(){
	//$("#buttonAdd1").attr("disabled", true);
	
	try{
		if(document.getElementById('select') != null ){
			document.getElementById('select').multiple = true; //to enable all option to be selected
		    for (var x = 0; x < document.getElementById('select').options.length; x++)//count the option amount in selection box
		    {
		        document.getElementById('select').options[x].selected = true;		       
		    }//select all option when u click save button
		}
		if(document.getElementById('optrnsfr') != null ){
			document.getElementById('optrnsfr').multiple = true; //to enable all option to be selected
		    for (var x = 0; x < document.getElementById('optrnsfr').options.length; x++)//count the option amount in selection box
		    {
		        document.getElementById('optrnsfr').options[x].selected = true;		       
		    }//select all option when u click save button
		}

			$('#warehouseName').attr("disabled",false);
		
		
		capitalizeName();		
	    document.form.submit();	   	 
	}catch(e){
	}
}

function capitalizeName(){
	var arr=["firstName", "lastName"];
	for(var i=0;i<arr.length;i++){
		var txt1 = document.getElementById(arr[i]).value;
		if(txt1!=null || txt!=""){
			capital(arr[i],txt1);
		}
	}
}
function capital(id,txt){
	$(document.getElementById(id)).val(txt.replace(/^(.)|\s(.)/g, function($1){ 
		   return $1.toUpperCase( ); 
		   }));
}

function buttonDPhotoCancel() {
	document.getElementById("model-close-DPhoto-btn").click();
}

function onUpdateClick(){
	try{
		if(document.getElementById('select') != null ){
			document.getElementById('select').multiple = true; //to enable all option to be selected
		    for (var x = 0; x < document.getElementById('select').options.length; x++)//count the option amount in selection box
		    {
		        document.getElementById('select').options[x].selected = true;		       
		    }//select all option when u click save button
		}
		/* if(document.getElementById('optrnsfr') != null ){
			document.getElementById('optrnsfr').multiple = true; //to enable all option to be selected
		    for (var x = 0; x < document.getElementById('optrnsfr').options.length; x++)//count the option amount in selection box
		    {
		        document.getElementById('optrnsfr').options[x].selected = true;		       
		    }//select all option when u click save button
		} */
		//$('#selectedAgentType').prop('disabled',false);
		capitalizeName();
		
		$('#warehouseName').attr("disabled",false);
		 $('#selectedAgentType').attr("disabled",false);
		
	    document.form.submit(); 
	}catch(e){ 
		console.log(e);
	}
}

$(function () { 
	$("#dynOpt").find("input[type='button'][value='<s:text name="RemoveAll"/>']").addClass("btn btn-warning");
	//$("#dynOpt input[value='Remove']").addClass("btn btn-warning");
	$("#dynOpt").find("input[type='button'][value='<s:text name="Add"/>']").addClass("btn btn-small btn-success fa fa-step-forward");
	$("#dynOpt").find("input[type='button'][value='<s:text name="Remove"/>']").addClass("btn btn-danger");
	$("#dynOpt").find("input[type='button'][value='<s:text name="AddAll"/>']").addClass("btn btn-sts");
	})

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
</script>
</body>