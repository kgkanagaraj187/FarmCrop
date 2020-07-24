<%@ include file="/jsp/common/detail-assets.jsp"%>
<script src="plugins/select2/select2.min.js"></script>
<script src="js/dynamicComponents.js?v=20.22"></script>
<link rel="stylesheet" href="plugins/select2/select2.min.css">
<head>
<style>
.flexform-item .form-element1 {
  width: 58%;
  padding: 5px 4px;
  display: flex;
  align-items: center;
  border-left: solid 1px #ccc; }

</style>
</head>
<meta name="decorator" content="swithlayout">

<body>
	<s:form name="form" cssClass="fillform" action="farmCrops_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden name="farmerId" value="%{farmerId}" />
		<s:hidden name="farmfarmerName" value="%{farmfarmerName}" />
		<s:hidden name="farmerfarmName" value="%{farmerfarmName}" />
<s:hidden name="branch" value="%{branch}" />
		<s:hidden name="farmId" value="%{farmId}" />
		<s:hidden name="farmCrops.farm.id" value="%{farmId}" />
		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" />
		<s:hidden id="farmerDynamicDatas" name="farmerDynamicDatas" />
		<s:hidden id="farmerDynamicValIds" name="farmerDynamicValIds" />
		<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />
		<s:hidden id="dynamicListArray" name="dynamicListArray" />	
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="farmCrops.id" class="uId" />
		</s:if>
		<s:hidden key="command" />

		<div class="appContentWrapper marginBottom">
			<div class="ferror" id="errorDiv" style="color: #ff0000">
				<s:actionerror />
				<s:fielderror />
			</div>
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmerCropInfo" class="accrodianTxt"><s:property value="%{getLocaleProperty('info.farmCrops')}" /> 
					</a>
				</h2>
			</div>
			<div id="farmerCropInfo" class="panel-collapse collapse in">
				<div class="flexform">
					<div class="flexform-item">
						<label for="txt"> <s:property value="%{getLocaleProperty('farm.name')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<s:if test='"update".equalsIgnoreCase(command)'>
							<div class="form-element">
								<s:property value="farmerfarmName" />
							</div>
						</s:if>
						<s:else>
							<div class="form-element">
								<s:select list="farmList" name="selectedFarm"
									cssClass="form-control" />
							</div>
						</s:else>
					</div>


					<div class="flexform-item cropCatField">
						<label for="txt"> <s:text
								name="farmcrops.cropCategory.prop" />
						</label>
						<s:if test="currentTenantId =='pratibha' && currentTenantId =='livelihood'" >
						<div class="form-elements">
							<s:radio name="farmCrops.cropCategory"
								onchange="cropDiv(this,'onchange');"
								value="defaultCropCategoryValue" list="cropCategories"
								id="cropCat" />
						</div>
						</s:if>
						<s:else>
						
						<div class="form-element">
							<s:radio name="farmCrops.cropCategory"
								onchange="cropDiv(this,'onchange');"
								value="defaultCropCategoryValue" list="cropCategories"
								id="cropCat" />
						</div>
						</s:else>
					</div>

					
						<div class="flexform-item "  >
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farmcrops.cropSeason')}" />
									<s:if test="currentTenantId !='wilmar'" >
									<sup style="color: red;">*</sup></s:if>
							</label>
							<div class="form-element">
								<%-- <s:select cssClass="form-control select2" id="cropSeasonValue"
									name="cropSeasonCode" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="cropSeasonsMap" /> --%>
									
									 <s:select cssClass="form-control select2" id="cropSeasonValue"
									name="cropSeasonCode" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="harvestseasonsLang" /> 
									
							</div>
						</div>
					

					<s:if test="cropInfoEnabled==1">
						<s:if test="currentTenantId !='chetna' 
						&& currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId !='crsdemo' && currentTenantId !='welspun' && currentTenantId !='griffith' && currentTenantId !='ecoagri' && currentTenantId !='livelihood'">
							<div class="flexform-item">
								<label for="txt"> <s:property
										value="%{getLocaleProperty('farmCrops.CultivationType')}" /><sup
									style="color: red;"></sup>
								</label>
								<div class="form-element">
									<s:select name="selectedCropCategoryList"
										list="cropCategoryList" listKey="key" listValue="value"
										headerKey="" headerValue="%{getText('txt.select')}"
										theme="simple" id="cropCategory"
										cssClass="col-sm-4 form-control select2" />
								</div>
							</div>
						</s:if>
					</s:if>
					
					
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farmcropName')}" /> <sup
								style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<%-- <s:select cssClass="form-control select2" id="farmCropsMasters"
									name="selectedCrop" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listProcurementProduct"
									onchange="listVariety(this)" /> --%>
									
									<s:select cssClass="form-control select2" id="farmCropsMasters"
									name="selectedCrop" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listProcurementProductLang"
									onchange="listVariety(this)" />
							</div>
						</div>

					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('variety')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select cssClass="form-control select2" id="farmVarietyMaster"
								name="selectedVariety" headerKey="-1"
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" list="listProcurementVarietyMap" />
						</div>
					</div>
					<s:if test="currentTenantId=='griffith'|| currentTenantId=='livelihood'">
						<div class="flexform-item mainCropDet livelihoodMain">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('prodTrees')}" /><sup
							style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<s:textfield id="prodTrees" name="farmCrops.prodTrees" theme="simple" maxlength="20">
								</s:textfield>
							</div>
						</div>
						</s:if>
						<s:if test="currentTenantId=='livelihood'">
						<div class="flexform-item mainCropDet livelihoodMain">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('affTrees')}" />
							</label>
							<div class="form-element">
								<s:textfield id="affTrees" name="farmCrops.affTrees" theme="simple" maxlength="10">
								</s:textfield>
							</div>
						</div>
							<div class="flexform-item mainCropDet livelihoodInter">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('noOfTrees')}" />
							</label>
							<div class="form-element">
								<s:textfield id="noOfTrees" name="farmCrops.noOfTrees" theme="simple" maxlength="10">
								</s:textfield>
							</div>
						</div>
						</s:if>
					<%-- <s:if test="currentTenantId =='nei'">
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farmcrops.cropSeason')}" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="cropSeasonValue"
									name="cropSeasonCode" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="cropSeasonsMap" />
							</div>
						</div>
					</s:if> --%>

					<s:if test="cropInfoEnabled==1">
						<s:if test="currentTenantId !='ecoagri'"> 
							<div class="flexform-item">
								<label for="txt"> <s:property
										value="%{getLocaleProperty('cultiArea')}" />
										<s:if test="currentTenantId =='susagri'"> 
										(<s:property	value="%{getLocaleProperty('hectare')}" />)
										</s:if>
										<s:else>
										<s:if test="currentTenantId !='griffith'"> 
										(<s:property value="%{getAreaType()}" />)</s:if>
										</s:else>
										<s:if test="currentTenantId =='welspun' || currentTenantId =='griffith'">
										<sup style="color: red;">*</sup></s:if>
								</label>
								<div class="form-element livelihoodMain">
									<s:textfield id="cultiArea" name="farmCrops.cultiArea"
										theme="simple" maxlength="35" onkeyup="calculateActualSeedCotton();">
									</s:textfield>
								</div>
							</div></s:if>
						<s:if test="currentTenantId !='livelihood'"> 
						<div class="flexform-item" id="sowingDate">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farmcrops.sowingDate')}" />
								(MM/DD/YYYY)
								<s:if test="currentTenantId =='welspun'">
								<sup style="color: red;">*</sup></s:if>
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm" name="sowingDate"
									id="calendarSowingDate" theme="simple" listValue="value" onchange="getDate(this.value);" />
							</div>
						</div>
						</s:if>
					</s:if>

					<s:if test="currentTenantId !='chetna' && currentTenantId !='iccoa' && currentTenantId !='wilmar' 
					&& currentTenantId !='welspun' && currentTenantId !='ecoagri' && currentTenantId !='livelihood' && currentTenantId !='avt' && currentTenantId !='susagri'
					&& currentTenantId !='kenyafpo'">
						<div class="flexform-item" id="typetr">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('type')}" /><s:if test="currentTenantId =='griffith'">
										<sup style="color: red;">*</sup></s:if>
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2 prefixBox" id="type"
									name="farmCrops.type" headerKey="-1"
									headerValue="%{getText('txt.select')}"
									onchange="showOther(this.value)" listKey="key"
									listValue="value" list="listType" />
							</div>
							<s:if test="currentTenantId !='griffith'">
							<div class="typeOtherDiv">
								<s:textfield id="typeOther" name="farmCrops.otherType"
									theme="simple" cssClass="form-control suffixBox" />
							</div>
							</s:if>
						</div>
					</s:if>

					<s:if
						test="currentTenantId !='kpf'  && currentTenantId !='simfed' && currentTenantId !='wub'  && currentTenantId !='movcd' && currentTenantId !='wilmar' && currentTenantId !='crsdemo'  && currentTenantId !='gar'
						&& currentTenantId !='welspun' && currentTenantId!='griffith' && currentTenantId!='livelihood' && currentTenantId!='kenyafpo'">
						<div class="flexform-item ">
							<label for="txt"> <s:property value="%{getLocaleProperty('seedSource')}" /><s:if test="currentTenantId !='ecoagri' && currentTenantId !='iccoa'"><sup style="color: red;">*</sup></s:if>
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="seedSourceMaster"
									name="farmCrops.seedSource" headerKey=""
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listSeedSource" />
							</div>
						</div>

					</s:if>

					<s:if
						test="currentTenantId=='lalteer'|| currentTenantId=='mhr' || currentTenantId=='chetna' ">
						<div class="flexform-item" id="seedTreatmentDetailsId">
							<label for="txt"> <s:text
									name="farmCrops.seedTreatmentDetails" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="seedTreatmentDetails"
									 name="farmCrops.seedTreatmentDetails"
									headerKey="-1" headerValue="%{getText('txt.select')}"
									listKey="key" listValue="value" list="seedTreatmentDetailsList" onchange="processSeedTreatmentDetails(this.value)"/>
								<div class="seedTreatmentDetailsOther">
									<s:textfield id="seedTreatmentId"
										name="farmCrops.otherSeedTreatmentDetails" maxlength="45"
										theme="simple" cssClass="suffixBox form-control" />

								</div>
								<script type="text/javascript">									
								processSeedTreatmentDetails(jQuery("#seedTreatmentDetails").val());									
									</script>
							</div>
						</div>
					</s:if>

					<s:if test="currentTenantId=='lalteer'|| currentTenantId=='mhr' ">
						<div class="flexform-item" id="riskAssesment">
							<label for="txt"> <s:text name="farmcrops.riskAssesment" />
							</label>
							<div class="form-element">
								<s:radio name="farmCrops.riskAssesment" id="risskAssmnt"
									onchange="riskDiv(this);" value="defaultRiskAssessmentValue"
									list="riskAssType" />
							</div>
						</div>
					</s:if>

					<s:if test="currentTenantId=='mhr'">
						<div class="flexform-item">
							<label for="txt"> <s:text
									name="farmcrops.riskBufferZoneDistanse" />
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm" id="bufferZoneId"
									name="farmCrops.riskBufferZoneDistanse"
									onkeypress="return isNumber(event)" theme="simple"
									maxlength="10"></s:textfield>
							</div>
						</div>
					</s:if>

			<%-- 		<s:if test='enableMultiProduct==0 '>
						
							<div class="flexform-item" id="stableLnt">
								<label for="txt"><s:text name="satbleLength" /> </label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										id="stapleLengthValue" name="farmCrops.stapleLength"
										theme="simple" maxlength="30" />
								</div>
							</div>
						

					</s:if> --%>
				

					<s:if
						test="currentTenantId !='kpf' && currentTenantId !='gsma' && currentTenantId !='simfed' && currentTenantId !='wub'  && currentTenantId !='movcd' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
						&& currentTenantId !='crsdemo' && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId!='griffith' && currentTenantId!='ecoagri' && currentTenantId!='avt'
						&& currentTenantId !='kenyafpo'">
						<div class="flexform-item" id="stableLnt">
							<label for="txt"><s:property
									value="%{getLocaleProperty('seedQtyUsed')}" /> </label>
							<div class="form-element flexdisplay">
								<s:textfield cssClass="form-control prefixBox livelihoodMain"
									name="farmCrops.seedQtyUsedPfx" theme="simple" maxlength="7"
									onkeypress="return isNumber(event)" />
							<s:if test="currentTenantId!='livelihood'">
								<s:textfield id="seedQtyUsedSfx"
									cssClass="form-control suffixBox"
									name="farmCrops.seedQtyUsedSfx" theme="simple" maxlength="2"
									onkeypress="return isNumber(event)" />
								</s:if>
							</div>
							
						</div>
						<s:if test="currentTenantId!='ocp' && currentTenantId!='livelihood'">
						<div class="flexform-item" >
							<label for="txt"><s:property
									value="%{getLocaleProperty('seedQtyCost')}" />
									<s:if test="currentTenantId=='susagri'">
									 (<s:property value="%{getLocaleProperty('currencyrupee')}"  />) 
									</s:if>
									<s:else>
									 (<s:property value="%{getCurrencyType().toUpperCase()}" />) 
									 </s:else>
									 </label>
							<div class="form-element flexdisplay">
								<s:textfield cssClass="form-control prefixBox"
									id="seedQtyCostPfx" name="farmCrops.seedQtyCostPfx"
									theme="simple" maxlength="7"
									onkeypress="return isNumber(event)" />

								<s:textfield id="seedQtyCostSfx"
									cssClass="form-control suffixBox"
									name="farmCrops.seedQtyCostSfx" theme="simple" maxlength="2"
									onkeypress="return isNumber(event)" />
							</div>
						</div>
						</s:if>
					</s:if>
					<s:if test="currentTenantId =='griffith' || currentTenantId =='ecoagri' || currentTenantId=='livelihood'">
							
							<s:if test="currentTenantId !='ecoagri' && currentTenantId!='livelihood'">
							<div class="flexform-item" >
								<label for="txt"><s:property value="%{getLocaleProperty('satbleLength')}" /><sup
								style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										name="farmCrops.stapleLength"
										theme="simple" maxlength="30" />
								</div>
					        </div></s:if>
					        <div class="flexform-item" >
								<label for="txt"><s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" /> <sup
							style="color: red;">*</sup>
								</label>
								<div class="form-element livelihoodMain">
									<s:textfield id="estYldPfx" name="farmCrops.estYldPfx"
										onkeypress="return isNumber(event)"
										onkeyup="convertKgToMetric()" maxlength="7"
										cssClass="form-control prefixBox" />
								<s:if test="currentTenantId!='livelihood'">
									<s:textfield id="estYldSfx" name="farmCrops.estYldSfx"
										onkeypress="return isNumber(event)"
										onkeyup="convertKgToMetric()" theme="simple" maxlength="3"
										cssClass="form-control suffixBox" /></s:if>
								</div>
							</div>
                        </s:if>
	<s:if test="currentTenantId!='pratibha' 
					&& currentTenantId !='kpf' && currentTenantId !='gsma' && currentTenantId !='simfed' && currentTenantId !='wub' && currentTenantId !='pgss' && currentTenantId !='wilmar' && currentTenantId !='iccoa'
					 && currentTenantId !='crsdemo' && currentTenantId !='agro'  && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId!='griffith' && && currentTenantId!='ecoagri'">
					<div class="flexform-item" id="stableLnt">
								<label for="txt"><s:property value="%{getLocaleProperty('satbleLength')}" /></label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										id="stapleLengthValue" name="farmCrops.stapleLength"
										theme="simple" maxlength="30" />
								</div>
					</div>
						</s:if>	
					

					<%-- <s:if test="currentTenantId =='kpf'">
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farmcrops.estimatedYeild')}" /> <sup
								style="color: red;">*</sup> </label>
							<div class="form-element flexdisplay">
								<s:textfield id="estYldPfx" name="farmCrops.estYldPfx"
									onkeypress="return isNumber(event)"
									onkeyup="convertKgToMetric()" maxlength="7"
									cssClass="form-control prefixBox" />

								<s:textfield id="estYldSfx" name="farmCrops.estYldSfx"
									onkeypress="return isNumber(event)"
									onkeyup="convertKgToMetric()" theme="simple" maxlength="3"
									cssClass="form-control suffixBox" />

								<s:label id="kgToMetric" name="kgToMetricValue" style="width:2%" />
							</div>
						</div>
					</s:if> --%>

					<s:if test="cropInfoEnabled==1">
						<s:if test="currentTenantId =='pratibha'">
						<div class="flexform-item" >
							<label for="txt"><s:text name="farmfarmcrops.estimatedYeild.quintals" />  <sup style="color: red;">*</sup></label>
							<div class="form-element ">
								<s:textfield cssClass="form-control prefixBox"
									id="estYldPfx" name="farmCrops.estYldPfx"
									theme="simple" maxlength="7"
									onkeypress="return isNumber(event)" />

								<s:textfield id="estYldSfx"
									cssClass="form-control suffixBox"
									name="farmCrops.estYldSfx" theme="simple" maxlength="2"
									onkeypress="return isNumber(event)" />
							</div>
						</div>
						
						<div class="flexform-item" id="harvestDate">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farmcrops.harvestDate')}" />
								(MM/DD/YYYY)
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm" name="harvestDate" readonly='true'
									id="calendarHarvestDate"  theme="simple" />
							</div>
						</div>
						</s:if>
						<s:else>
						<s:if test="currentTenantId !='welspun' && currentTenantId !='griffith' && currentTenantId !='ecoagri' && currentTenantId !='livelihood'">
							<div class="flexform-item" id="stableLnt">
								<label for="txt"><s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" /> 
								</label>
								<div class="form-element">
									<s:textfield id="estYldPfx" name="farmCrops.estYldPfx"
										onkeypress="return isNumber(event)"
										onkeyup="convertKgToMetric()" maxlength="7"
										cssClass="form-control prefixBox" />

									<s:textfield id="estYldSfx" name="farmCrops.estYldSfx"
										onkeypress="return isNumber(event)"
										onkeyup="convertKgToMetric()" theme="simple" maxlength="3"
										cssClass="form-control suffixBox" />
								</div>
							</div>
							<s:if test="currentTenantId !='simfed'">
							<div class="flexform-item" id="stableLnt1">
								<label for="txt"><s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild.tonnes')}" />
								<div class="form-element">
									<s:label id="plantTonnesValues" />
								</div>
							</div>
							</s:if>
							</s:if>
							</s:else>
						
					</s:if>
					<s:if test="currentTenantId=='wilmar'">
					<div class="flexform-item mainCropDet">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('noOfTrees')}" />
							</label>
							<div class="form-element">
								<s:textfield id="noOfTrees" name="farmCrops.noOfTrees" theme="simple" maxlength="10">
								</s:textfield>
							</div>
						</div>
						<div class="flexform-item mainCropDet">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('prodTrees')}" />
							</label>
							<div class="form-element">
								<s:textfield id="prodTrees" name="farmCrops.prodTrees" theme="simple" maxlength="10">
								</s:textfield>
							</div>
						</div>
						<div class="flexform-item mainCropDet">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('affTrees')}" />
							</label>
							<div class="form-element">
								<s:textfield id="affTrees" name="farmCrops.affTrees" theme="simple" maxlength="10">
								</s:textfield>
							</div>
						</div>
						<div class="flexform-item mainCropDet">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('yearOfPlanting')}" />
							</label>
							<div class="form-element">
								<s:select list="yearList" listKey="key" listValue="value" headerValue="Select" headerKey=""
								cssClass="form-control select2" name="farmCrops.interAcre"
								theme="simple" id="interAcre" />
							</div>
						</div>
						
					</s:if>
					
				</div>
<div class="dynamicFieldsRender"></div>
				<div class="flexItem flex-layout flexItemStyle">
					<s:if test="command =='create'">
						<span id="button" class="yui-button"><span
							class="first-child">
								<button type="button" class="save-btn btn btn-success"
									id="sucessbtn">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span></span>
					</s:if>
					<s:else>
						<span id="button" class="yui-button"><span
							class="first-child">
								<button type="button" class="save-btn btn btn-success">
									<font color="#FFFFFF"> <b><s:text
												name="update.button" /></b>
									</font>
								</button>
						</span></span>
					</s:else>
					<span id="cancel" class="yui-button"><span
						class="first-child">
							<button type="button" onclick="onCancel()"
								class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
								</font></b>
							</button>
					</span></span>
				</div>

			</div>
		</div>

	</s:form>

	<s:form name="cancelform" action="farmer_detail.action%{tabIndexz}">
		<s:hidden name="farmerId" />
		<s:hidden name="farmId" />
		<s:hidden name="id" value="%{farmerId}" />
		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" value="%{tabIndexz}" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="listForm" id="listForm" action="farmer_detail.action">
		<s:hidden name="farmerId" />
		<s:hidden name="id" value="%{farmerId}" />
		<s:hidden name="tabIndexFarmer" />
		<s:hidden name="tabIndex" value="#tabs-2" />
		<s:hidden name="currentPage" />
	</s:form>
	<script type="text/javascript">
	var tenant='<s:property value="getCurrentTenantId()"/>';
	function onCancel(){
		document.cancelform.submit();
	}
	function submitCreate(){
		if(addDynamicField()){
		//document.form.submit();
			}
	}
	function onFarmList(){
		document.listForm.submit();
	}
	function isNumber(evt) {
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
	
	function calculateLintCotton(){
		var getValue=$("#seedCott").val();
		
		if(getValue==""){
    		$(".lintCottonValues").empty();
    	}
    	else{
    		var lintCott = $("#seedCott").val()/3;
        	//document.getElementById('lintCottonValues').innerHTML=lintCott.toFixed(2);
        	jQuery(".lintCottonValues").text(lintCott.toFixed(2));
        	jQuery(".lintCottonValues").val(lintCott.toFixed(2));
    	}
	}
	function calculateActualSeedCotton(){
		
		var getValue1=$("#seedCott").val();
		
		var getValue2=$("#cultiArea").val();
		
		if(getValue1!="" && getValue2!=""){
var actualSeedCotton = $("#seedCott").val()/$("#cultiArea").val();
			
			jQuery(".actualSeed").text(actualSeedCotton.toFixed(2));
        	jQuery(".actualSeed").val(actualSeedCotton.toFixed(2));
			
		}else{
			$(".actualSeed").empty();
			//document.getElementById('actualSeed').innerHTML=actualSeedCotton.toFixed(2);
		}
		
	}
	
	/* function hideOtherSeedTreatment(){
		if($("#seedTreatmentDetails")!=-1){
			$("#seedTreatment").hide();
		}
	} */
	
	function processSeedTreatmentDetails(val)
	{
		
		if (val == 99) {
			jQuery(".seedTreatmentDetailsOther").show();
			jQuery("#seedTreatmentId").val('<s:property value="farmCrops.otherSeedTreatmentDetails" />');
		} else {
			jQuery("#seedTreatmentId").val("");
			jQuery(".seedTreatmentDetailsOther").hide();
		}	
	}
	
	function cropDiv(evt,status){
		// var rst =jQuery("#riskHidden").val();
		var val=$(evt).val();
		console.log(evt);
		if(tenant=='pratibha'){
			if(val==0 || val==3 || val==4){
				jQuery("#stableLnt").show();
				jQuery("#stableLnt1").show();
				jQuery("#cropSeason").show();
				jQuery("#typetr").show();
				jQuery("#riskAssesment").show();
				var riskAssementVal='<s:property value="farmCrops.riskAssesment"/>';
				if(riskAssementVal==1){
					jQuery("#captureAssessment").show();
					}
					else{
						jQuery("#captureAssessment").hide();
					}
				if(status == "onchange")
					jQuery("#seedSourceMaster").val("-1");
				
				jQuery("#seedTreatmentDetailsId").show();
				jQuery("#sowingDate").show();
			}
			else{
				if(status == "onchange")
					  jQuery("#seedSourceMaster").val("-1");
				if(tenant!="iccoa"){
					jQuery("#stableLnt").hide();
					jQuery("#stableLnt1").hide();
				}
				jQuery("#cropSeason").hide();		
				jQuery("#typetr").hide();
				jQuery("#seedTreatmentDetails").val("-1");
				jQuery("#riskAssesment").hide();
				jQuery("#captureAssessment").hide();
				jQuery("#bufferZoneId").val("");
				if(tenant!="iccoa")
					jQuery("#stapleLengthValue").val("");
				jQuery("#seedTreatment").hide();
				jQuery("#seedTreatmentDetails").hide();
				jQuery("#seedTreatmentDetailsId").hide();
				if(tenant=="welspun")
				jQuery("#sowingDate").hide();
			}
		}else if(tenant=='livelihood'){
			if(val==1){
				$('.livelihoodInter').removeClass('hide');
				$('.livelihoodMain').addClass('hide');
			}
			else {
				//$('.livelihoodInter').addClass('hide');
				$('.livelihoodMain').removeClass('hide');
			}
		
		}else if(tenant=='griffith'){
				//$('.livelihoodInter').addClass('hide');
				//$('.livelihoodMain').addClass('hide');
			
		}
		else{
		 if(val==0){
			//alert("if");
			
				jQuery("#stableLnt").show();
				jQuery("#stableLnt1").show();
			
			
			jQuery("#cropSeason").show();
			jQuery("#typetr").show();
			jQuery("#riskAssesment").show();
			var riskAssementVal='<s:property value="farmCrops.riskAssesment"/>';
			if(riskAssementVal==1){
				//jQuery("#risskAssmnt").show();
				jQuery("#captureAssessment").show();
				}
				else{
					jQuery("#captureAssessment").hide();
				}
			if(status == "onchange")
				jQuery("#seedSourceMaster").val("-1");
			
			//jQuery("#seedTreatmentDetails").show();
			jQuery("#seedTreatmentDetailsId").show();
			jQuery("#sowingDate").show();
			/* jQuery("#seedCotton").show();
			jQuery("#lintCotton").show();
			jQuery("#actualSeedYield").show();
			jQuery("#estimatedYield").show();
			jQuery("#estimatedCotton").show();
			jQuery("#interType").hide();
			jQuery("#interAcre").hide();
			jQuery("#totalCropHarv").hide();
			jQuery("#grossIncome").hide(); */
			
			
		}
		else{
			//alert("else");
			if(status == "onchange")
				  jQuery("#seedSourceMaster").val("-1");
			if(tenant!="iccoa"){
				jQuery("#stableLnt").hide();
				jQuery("#stableLnt1").hide();
			}
			
			
			jQuery("#cropSeason").hide();		
			jQuery("#typetr").hide();
			jQuery("#seedTreatmentDetails").val("-1");
			//jQuery("#seedTreatmentId").val("");
			jQuery("#riskAssesment").hide();
			jQuery("#captureAssessment").hide();
			jQuery("#bufferZoneId").val("");
			//jQuery("#cropSeasonValue").val("-1");
			//jQuery("#type").val("-1");
			if(tenant!="iccoa")
				jQuery("#stapleLengthValue").val("");
			jQuery("#seedTreatment").hide();
			jQuery("#seedTreatmentDetails").hide();
			jQuery("#seedTreatmentDetailsId").hide();
			if(tenant=="welspun")
			jQuery("#sowingDate").hide();
			/* jQuery("#seedCotton").hide();
			jQuery("#lintCotton").hide();
			jQuery("#actualSeedYield").hide();
			jQuery("#estimatedYield").hide();
			jQuery("#estimatedCotton").hide();
			jQuery("#interType").show();
			jQuery("#interAcre").show();
			jQuery("#totalCropHarv").show();
			jQuery("#grossIncome").show(); */
		}
		}

	} 
	
	 function riskDiv(evt){
		 //var v=document.getElementById("risskAssmnt").value();
		
		var value=$(evt).val();
		$("#riskHidden").val(value);
		if(value==1){
			jQuery("#captureAssessment").show();
			jQuery("#riskAssesment").show();
		    jQuery("#bufferZoneId").show();
		}
		else{
			jQuery("#captureAssessment").hide();
			jQuery("#bufferZoneId").val("");
			
		}
	} 
	
	
	jQuery(document).ready(function(){
		var cropCategory='<s:property value="farmCrops.cropCategory"/>';
		
		 var command='<s:property value="command"/>';
		var  url = window.location.href;
	
		renderDynamicFeilds();
		$(".farmerDynamicField").removeClass("hide");
		 if('<s:property value="command"/>'=="update"){
				setDynamicFieldUpdateValues();
			}
		// alert(url.indexOf('farmerId='));
		 if(url.indexOf('farmerId=') < 0){ 
			 
		 
	      if(command.toLowerCase()=="update"){
	    	//  alert("1");
	    	   url = url+'?id='+'<s:property value="farmCrops.id"/>'+'&farmId='+'<s:property value="farmCrops.farm.id"/>'+'&farmerId='+'<s:property value="farmCrops.farm.farmer.framerId"/>'+'&tabIndexz='+'#tabs-4';
	  	 }else{
	  	//	alert("2");
	  	 
	  	   url = url+'?farmId='+'<s:property value="farmId"/>'+'&farmerId='+'<s:property value="farmerId"/>'+'&tabIndexz='+'#tabs-4';
	     }
		
	  
	    
	     $( ".lanMenu" ).each(function() {
	    	 var url1 = $(this).attr("href") +'&url='+ encodeURIComponent(url);
	    	  $( this ).attr( "href",url1);
	    	});
	     

	     var stag = $('a[href="farm_detail.action?id=&#tabs-3"]');
		 $(stag).attr("href", "javascript:onCancel();");
		 
		 var stag = $('a[href="farmer_detail.action"]');
		 $(stag).attr("href", "javascript:onFarmList();");
	}
	
		$('.select2').select2();
		jQuery(".typeOtherDiv").hide(); 	
		convertKgToMetric();
		
		showOther($('#type').val());
		
		var command ='<s:property value="command"/>';
		var cropPlugin = '<s:property value="cropInfoEnabled"/>';
		if(command=="create")
			 {
			
			 jQuery(".seedTreatmentDetailsOther").hide();
			 }
		 
		 else if(command=="update")
	    	{
	    	var idp=$('#seedTreatmentDetails').val();
	    	processSeedTreatmentDetails(idp);
	    	}
		 
		 
		$("#sucessbtn").prop('disabled', false);
		$('#button').on('click',function(e){
			var tenant='<s:property value="getCurrentTenantId()"/>';
			var cropPlugin = '<s:property value="cropInfoEnabled"/>';
			//alert("af");
			/* $("#sucessbtn").prop('disabled', true); */
			 if(tenant!='kpf'  && tenant!='simfed' && tenant!='wub' && tenant!='gar' && tenant!='wilmar' && tenant!='iccoa'&& tenant!='crsdemo'&& tenant!='agro' && tenant!='welspun' && tenant!='pratibha'){
			if($("#seedQtyCostPfx").val()==""){
				$("#seedQtyCostPfx").val('0');
			}
			if($("#seedQtyCostSfx").val()==""){
				$("#seedQtyCostSfx").val('0');
			}
			
			if($("#seedQtyUsedSfx").val()==""){
				$("#seedQtyUsedSfx").val('0');
			}
			 }
			
					 if(tenant=='kpf' || tenant=='simfed'|| tenant=='wub'){
						 if($("#estYldPfx").val()==""){
								$("#estYldPfx").val('0');
							}
								
							if($("#estYldSfx").val()==""){
								$("#estYldSfx").val('0');
							} 
					 }
				if(cropPlugin=="1"){
					if(tenant!='welspun' && tenant!='pratibha'){
						if($("#estYldPfx").val()==""){
							$("#estYldPfx").val('0');
						}
							
						if($("#estYldSfx").val()==""){
							$("#estYldSfx").val('0');
						} 
							
					}
				}
			 
				if(addDynamicField()){
			document.form.submit();
				}
			});
		
		var riskAssementVal='<s:property value="farmCrops.riskAssesment"/>';
		$('#seedTreatment').hide();
		//$('#seedTreatmentId').val("");
		//if(riskAssementVal==1&&croVal==0){
			if(riskAssementVal==1){
			$('#captureAssessment').show();
		}
			else{
				$('#captureAssessment').hide();
			}
		var otherSeedTrtDetail='<s:property value="farmCrops.otherSeedTreatmentDetails"/>';
	
		var cropCat = $('input:radio[name="farmCrops.cropCategory"]:checked');
		cropDiv(cropCat,"onload");
		if(tenant!='griffith'){
		var type='<s:property value="farmCrops.cropCategory"/>';
		if(type=='0'){
			$('#typetr').show();
		}else if(type=='1'){
			$('#typetr').hide();
		}
		}
		else{
			$('#typetr').show();
		}
		
		
		
		convertKgToMetric();
		
	});
	function getTxnType(){
		return "357";
	}
	
	 function getBranchIdDyn(){
	     	return '<s:property value="branch"/>';
	    	}
	 

	
	/* function addSeedTreatmentDetails(){
		var getTable= document.getElementById("seedTreatmentDetails");
		
		
		if(getTable){
			$("#seedTreatmentDetails").val(-1);
			jQuery("#seedTreatment").show();
			jQuery("#seedTreatmentId").show();
			
		}
		else{
		
			jQuery("#seedTreatment").hide();
		}
	  } */
	  
	  
	/* function removeSeedTreatmentDetails(){
		var getDetail=document.getElementById("seedTreatmentId");
			if(getDetail){
			//jQuery("#seedTreatmentId").hide();
		//	jQuery("#rmv").hide();
			jQuery("#seedTreatment").hide();
			jQuery("#seedTreatmentId").val("");
			
		}
	} */
	
	 function hideByEleName(name){
     	$('[name="'+name+'"]').closest( ".flexform-item" ).addClass( "hide" );
     }
	
	function insertOptions(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), "Select", "0");
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
		
		 var id="#"+ctrlName;
	     jQuery(id).select2();
	}

	function addOption(selectbox, text, value) {
		var optn = document.createElement("OPTION");
		optn.text = text;
		optn.value = value;
		selectbox.options.add(optn);
	}

	function listVariety(obj){
		var selectedCrop = $('#farmCropsMasters').val();
		jQuery.post("farmCrops_populateVariety.action",{id:obj.value,dt:new Date(),selectedCrop:obj.value},function(result){
			insertOptions("farmVarietyMaster",$.parseJSON(result));
		});
	}
	function getDate(val){
		//alert(val);
		//$('#calendarHarvestDate').datepicker('destroy');
		$( "#calendarHarvestDate" ).datepicker('setDate',null);
		
		 $( "#calendarHarvestDate" ).datepicker({
			 minDate:+0
				});		
		$('#calendarHarvestDate').datepicker('option', 'minDate', new Date(val));
		/*  $( "#calendarHarvestDate" ).datepicker({
			minDate: new Date(val)
			});  */
	}
	$( "#calendarSowingDate" ).datepicker({
		maxDate: '0',
		beforeShow : function(){
			jQuery( this ).datepicker({ maxDate: 0 });
		},
		changeMonth: true,
		changeYear: true
	});
	
	$( "#calendarHarvestDate" ).datepicker({
		minDate: '0',
		beforeShow : function(){
			jQuery( this ).datepicker({ minDate: 0 });
		},
		changeMonth: true,
		changeYear: true
	});
	
	
	
	
	 if(document.getElementById("sowingDate")!=null)
		document.getElementById("sowingDate").value='<s:property value="currentDate" />'; 
		function isDecimal(evt) {
			
			 evt = (evt) ? evt : window.event;
			    var charCode = (evt.which) ? evt.which : evt.keyCode;
			    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
			        return false;
			    }
			    return true;
		}
		
		 function convertKgToMetric(){
			 
				var prefff =  ($("#estYldPfx").val()=='') ? 0 : $("#estYldPfx").val();
				var siff =  ($("#estYldSfx").val()=='') ? 0 : $("#estYldSfx").val();
				var totalValu = parseFloat(prefff+"."+siff);
		        	var plantHect =(totalValu/1000).toFixed(6);
		        	
		        	if(document.getElementById('plantTonnesValues')!=null)
		 				{
		        	document.getElementById('plantTonnesValues').innerHTML=plantHect;
						 }
		        }
		 
		 var tenant='<s:property value="getCurrentTenantId()"/>';
		
	function showOther(val){
		if(val=="99"){
			 jQuery(".typeOtherDiv").show(); 
			
		}
	}

		 function isDecimal(evt) {
				
			 evt = (evt) ? evt : window.event;
			    var charCode = (evt.which) ? evt.which : evt.keyCode;
			    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
			        return false;
			    }
			    if(charCode==46){
			    	if(flag==1)
			    		return false;
			    	else
			    		flag=1;
			    }
			    return true;
		}
</script>
</body>