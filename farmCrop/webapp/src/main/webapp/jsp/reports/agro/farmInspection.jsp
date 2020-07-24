
<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<link rel="stylesheet" href="plugins/selectize/css/selectize.bootstrap3.css">
<script src="plugins/selectize/js/standalone/selectize.min.js"></script>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<div class="appContentWrapper marginBottom">
<div class="error"><s:actionerror /><s:fielderror />

<sup>*</sup>
<s:text name="reqd.field" /></div>

<span id="validateError" style="color: red;"></span>

<s:form name="form" cssClass="fillform" action="periodicInspectionServiceReport_%{command}?type=service" method="post" enctype="multipart/form-data" id="target">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="periodicInspection.id" />
	</s:if>
	<s:hidden key="command" />
	<div class="formContainerWrapper">
	<div class="row">
		<div class="col-md-6">
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:text name="info.inspectionDet" /></th>
		</tr>
		
		<div class="flexiWrapper">
		<s:if test='branchId==null'>
		<tr class="odd">
			<td width="35%"><s:text name="app.branch" /></td>
			<td width="65%"><s:property value="%{getBranchName(periodicInspection.branchId)}" /></td>
		</tr>
		</s:if>	
		<s:if test='"update".equalsIgnoreCase(command)'>
		<tr class="odd">
			<td width="35%"><s:text name="info.inspection"/></td>
			<td width="65%">
			<s:property value="%{inspectionOfDate}"/>
			</td>
		</tr>
		 <s:if test="getCurrentTenantId()!='lalteer'">	
		<tr class="odd">
			<td width="35%"><s:text name="farmerIdName" /></td>
			<td width="65%">
			<s:property value="%{farmerIdName}" />
			</td>
		</tr>
		</s:if>
		<s:else>
		<tr class="odd">
			<td width="35%"><s:text name="farmerCodeName" /></td>
			<td width="65%">
			<s:property value="%{farmerCodeName}" />
			</td>
		</tr>
		</s:else>
		
		<tr class="odd">
			<td width="35%"><s:text name="farmIdName" /></td>
			<td width="65%">
			<s:property value="%{farmIdName}" />
			</td>
		</tr>
		
		<tr class="odd">
			<td width="30%"><s:text name="%{getLocaleProperty('cropName')}" /></td>
			<td width="40%"><s:property value="periodicInspection.cropCode" /></td>

		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="fieldstaffIdName" /></td>
			<td width="65%">
			<s:property value="%{mobileId}" />-<s:property value="%{mobileUserName}" />
			</td>
		</tr>
		
		</s:if>
		<s:if test="getCurrentTenantId()!='efk'">
			<tr class="odd">
						<td width="30%"><s:text name="farmerOpinion" /></td>
						<td width="30%"><s:textfield name="periodicInspection.farmerOpinion" /></td>
	
		   </tr>	
		</s:if>
		
	   <tr class="odd">
					<td width="30%"><s:text name="remarks" /></td>
					<td width="30%"><s:textfield name="periodicInspection.remarks" /></td>
	   </tr>
		
		<tr class="odd">
					<td width="35%"><s:text name="inspectionAudio" /></td>
					<td width="65%"><s:file name="inspectionAudio"
							id="inspectAudio" type="file" onchange="validateAudio()"/>
						<div style="font-size: 10px">
							<%-- <s:text name="inspection.audioTypes" /> --%>
						<font color="red"> <s:text name="inspection.audioTypes" /></font>
						</div></td>
				</tr>
		
	</table>
	
	<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="info.pest" /></th>
				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="pestOccurence" /></td>
					<td width="30%"><s:radio id="pestOcc" list="radioValueList"
								name="isPestNoticed"
								onchange="pestOnchange(this.value);" /></td>
				</tr>
				
				<tr class="pestDiv">
				<td width="30%"><s:text name="nameOfPest" /><sup
				style="color: red;">*</sup></td>
				
				<td width="30%">
				<s:if test="currentTenantId!='efk'">
					<s:select id="pestName" name="selectedPestName" list="pestMap" listKey="key" listValue="value"
						cssClass="form-control select2Multi" multiple="true" onchange="processPestOther(this)"/>
				</s:if>					
				<s:else>
				<s:textfield id="pestName" name="selectedPestName" cssClass="form-control  input-sm" maxlength="45" cssStyle="margin-top:1%" />
				</s:else>
					
									<div class="pestOther">
									<s:textfield id="pestOtherValue"
										name="selectedOtherPest"
										cssClass="form-control  input-sm" maxlength="45"
										cssStyle="margin-top:1%" />
									</div>
								
									<script type="text/javascript">									
									processPestOther(jQuery("#pestName").val());									
							   		</script>
				</td>
				</tr>
				 <s:if test="currentTenantId=='fruitmaster'">
				<tr class="pestSynmDiv">
				<td width="30%"><s:text name="pestSymptom" /></td>
				
				<td width="30%"><s:select id="pestSymName" name="selectedPestSymptom"
									list="pestSymptomsMap" listKey="key" listValue="value"
									cssClass="form-control select2Multi" multiple="true"/>
									
				</td>
				</tr></s:if>
				
				<tr class="pestETLDiv">
					<td width="30%"><s:text name="PestInfestationETL" /></td>
					<td width="30%">
					 	<s:radio id="pestETL"
								list="radioValueList"
								name="isPestETL" onchange="pestETLOnChange(this.value);"/>
					</td>
				</tr>

				<%-- <tr class="pestPbmSol">
					<td width="30%"><s:text name="pestProblemSolved" /></td>
					<td width="30%">
					<s:radio id="pestPbmSolve"
								list="radioValueList"
								name="isPestSolved"/></td>
				</tr> --%>
				
			</table>
			
	 	<table id="pestRecTble" class="table table-bordered aspect-detail">
			<input type="hidden" name="pestJson" id="pestJson"/>
		 <s:if test="currentTenantId!='efk'">
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion" 
					><s:property value="%{getLocaleProperty('pesticideRecommended')}" /></a><sup style="color: red;">*</sup></th>
			</tr>
			<tbody id="pestRecom">
			<tr>
			<div id="pestError" style="text-align: left; padding: 5px 0 0 0; color: red;"></div>
			</tr>
			
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:property value="%{getLocaleProperty('pesticideRecommended')}" />
						</div></td>
						
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="qty" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="aaaction" />
						</div></td>
				 </tr>
		   
		    <tr>
				<td style="width: 17%;">
				<div class="col-xs-16">
				
				<s:select id="pestRecName" name=""
									list="pestRecMap" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('txt.select')}"
									cssClass="form-control" onchange="addOtherValue()"/>
							
							<br/>
				<div class="hide" id="otherDiv"><s:textfield name="OtherPestValue" maxlength="20" id="otherPestValue"/><sup
				style="color: red;">*</sup></div>
				</div>
				</td>
						
				<td style="width: 17%;">
				<div class="col-xs-16">
							<s:textfield name="quantity" maxlength="20" id="pestQty" cssClass="form-control input-sm" onkeypress="return isNumber(event)" />
				</div>
				</td>
				
				<td style="width: 17%;">
				<div class="col-xs-16">
						<button type="button" onclick="appendRow()" id="appendBtnId" class="btn btn-sm btn-success"><i class="fa fa-check"></i></button>
				</div>
				</td>
				</tr>
				
				
			</tbody>
			<tfoot id="pestRecomented">
						<s:if test='"update".equalsIgnoreCase(command)'>
				<s:iterator value="pestRecomList" status="status">
			
					<tr id='pestRow<s:property value="%{#status.count}" />'>
						<td align="center"><div id='selectedPest1<s:property value="%{#status.count}" />'><s:text name="pestNameValue.split(',')[%{#status.index}]" /><input type="hidden" value='<s:property value="otherCatalogueValueName" />' id='selectedPestOth<s:property value="%{#status.count}" />'/></div></td>
						<td align="center"><div id='pestQtyVal<s:property value="%{#status.count}" />'><s:property value="quantityValue" /></div></td>
						<td align="center" class="hide"><div id='selectedPest<s:property value="%{#status.count}" />'><s:property value="catalogueValue" /></div></td>
						<td style="text-align:center;">
							<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteRow('<s:property value="%{#status.count}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
						</td>	
						<input type="hidden" value='<s:property value="id" />' id='pestRecId<s:property value="%{#status.count}" />'/>	
					</tr>
			</s:iterator>
					</s:if>				
		
			</tfoot>
		
		</s:if>
			<s:else>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:property value="%{getLocaleProperty('pesticideRecommended')}" />
						</div></td>
					
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield maxlength="20" id="pestRecName" cssClass="form-control input-sm" />
						</div></td>
				 </tr>
			</s:else>
		</table> 

			
			<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="info.disease" /></th>
				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="diseaseOccurence" /></td>
					<td width="30%"><s:radio id="diseasePbm"
								list="radioValueList"
								name="isDiseaseNoticed" onchange="diseaseChangeDiv(this.value);"
								/></td>
				</tr>
				
				<tr class="disDiv">
				<td width="30%"><s:text name="nameOfDisease" /><sup
				style="color: red;">*</sup></td>
				
				<td width="30%">
				<s:if test="currentTenantId!='efk'">
				<s:select id="diseaseName" name="selectedDiseaseName" list="diseaseMap" listKey="key" listValue="value" cssClass="form-control select2Multi" multiple="true"  onchange="processDiseaseOther(this);"/>
				</s:if>
				<s:else>
				<s:textfield id="diseaseName" name="selectedDiseaseName" cssClass="form-control  input-sm" maxlength="45" cssStyle="margin-top:1%" />
				</s:else>					
									<div class="diseaseOther">
									<s:textfield id="diseaseOtherValue"
										name="selectedOtherDisease"
										cssClass="form-control  input-sm" maxlength="45"
										cssStyle="margin-top:1%" />
								</div>
								
								<script type="text/javascript">									
								processDiseaseOther(jQuery("#diseaseName").val());									
							   </script>	
				</td>
				</tr>
				 <s:if test="currentTenantId=='fruitmaster'">
				<tr class="disSynmDiv">
				<td width="30%"><s:text name="diseaseSymptom" /></td>
				
				<td width="30%"><s:select id="diseaseSymName" name="selectedDiseaseSymptom"
									list="diseaseSymptomsMap" listKey="key" listValue="value"
									cssClass="form-control select2Multi" multiple="true"/>
									</td>
				</tr> 
				</s:if>

				<tr class="disETLDiv">
					<td width="30%"><s:text name="diseaseInfestationETL" /></td>
					<td width="30%">
					<s:radio id="diseaseETL"
								list="radioValueList"
								name="isDiseaseETL" onchange="diseasetETLOnChange(this.value);"
								/></td>
				</tr>
				
 		
				<%-- <tr class="disPbmSol">
					<td width="30%"><s:text name="diseaseProblemSolved" /></td>
					<td width="30%">
					<s:radio id="diseaseSolve"
								list="radioValueList"
								name="isDiseaseSolved"
								/></td>
				</tr> --%>
			</table>
			
			<table id="diseaseSetTbl"class="table table-bordered aspect-detail">
			<input type="hidden" name="diseaseJson" id="diseaseJson"/>
			<s:if test="currentTenantId!='efk'">
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion"><s:property value="%{getLocaleProperty('fungicideRecommended')}" /></a><sup style="color: red;">*</sup></th>
			</tr>
			
			<tbody id="fungiside">
			 
			 
			<tr>
			<div id="fungiError" style="text-align: left; padding: 5px 0 0 0; color: red;"></div>
			</tr>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:property value="%{getLocaleProperty('fungicideRecommended')}" />
						</div></td>
					

					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="qty" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="aaaction" />
						</div></td>
				 </tr>
				 <tr>
				 <td style="width: 17%;"><div class="col-xs-16">
							<s:select id="fungRecName" name=""
									list="fungRecMap" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('txt.select')}"
									cssClass="form-control" onchange="addFungOtherValue()"/>
									<div class="hide" id="otherFungDiv"><s:textfield name="otherFungValue" maxlength="20" id="otherFungValue"/><sup
				style="color: red;">*</sup></div>
				</div>
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield name="farm.longitude" maxlength="20" id="fungQty"
								cssClass="form-control input-sm" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<button type="button" onclick="appendFungicideRow()" id="appendFungBtnId" class="btn btn-sm btn-success"><i class="fa fa-check"></i></button>
						</div></td>
				</tr>
				
				
			</tbody>
			<tfoot id="fungicideRecomented">
						<s:if test='"update".equalsIgnoreCase(command)'>
				<s:iterator value="fungiList" status="status">
			
					<tr id='fungicideRow<s:property value="%{#status.count}" />'>
						<td align="center"><div id='selectedFung1<s:property value="%{#status.count}" />'><s:text name="fungisideValue.split(',')[%{#status.index}]" /><input type="hidden" value='<s:property value="otherCatalogueValueName" />' id='selectedFungOth<s:property value="%{#status.count}" />'/></div></td>
						<td align="center"><div id='fungQtyVal<s:property value="%{#status.count}" />'><s:property value="quantityValue" /></div></td>
						<td align="center" class="hide"><div id='selectedFung<s:property value="%{#status.count}" />'><s:text name="catalogueValue" /></div></td>
						<td style="text-align:center;">
							<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteFungRow('<s:property value="%{#status.count}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
						</td>	
						<input type="hidden" value='<s:property value="id" />' id='fungisideId<s:property value="%{#status.count}" />'/>	
					</tr>
			</s:iterator>
					</s:if>				
		
			</tfoot>
			</s:if>		
			<s:else>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:property value="%{getLocaleProperty('fungicideRecommended')}" />
						</div>
					</td>
					
					<td style="width: 17%;">
					<div class="col-xs-16">
						<s:textfield  maxlength="20" id="fungRecName" cssClass="form-control input-sm" />
					</div>
						
				</td>
				
				</tr>
			</s:else>
			
		</table> 
		
		
	</div>
	
	<div class="col-md-6">
	
	<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="info.crop" /></th>
				</tr>
				<s:if test="currentTenantId=='lalteer'">
				
				<tr>
				<td width="30%"><s:text name="survival" /></td>
				<td><s:select cssClass="form-control input-sm" id="cropId"
										name="periodicInspection.survivalPercentage" listKey="key" listValue="value"
										list="cropList" headerKey="-1"
										headerValue="%{getText('txt.select')}" />
										</td>
				</tr>
				</s:if>
				<s:else>
				<tr class="odd">
					<s:if test="getCurrentTenantId() != 'efk'">
							<td width="30%"><s:text name="%{getLocaleProperty('survival')}" /></td>
							</s:if>
							<s:else>
								<td width="30%"><s:text name="survival" /></td>
							</s:else>
					
					<td width="30%"><s:textfield name="periodicInspection.survivalPercentage" maxlength="30"/></td>

				</tr>
				</s:else>
				
				<tr>
				<td width="30%"><s:text name="currentStatusofGrowth" /><sup
				style="color: red;">*</sup></td>
				<td width="30%"><s:select cssClass="form-control input-sm" id="growthId"
										name="selectedGrowth" listKey="key" listValue="value"
										list="growthList" headerKey=""
										headerValue="%{getText('txt.select')}" />
										</td>
				</tr>
				
				<s:if test="currentTenantId!='lalteer' && currentTenantId!='efk' && currentTenantId!='kenyafpo'" >

				<tr class="odd">
					<td width="30%"><s:text name="averageHeightMeter" /></td>
					<td width="30%"><s:textfield name="periodicInspection.averageHeight" /></td>

				</tr>
				</s:if>
				<s:if test="enableMultiProduct==0 || currentTenantId!='lalteer' && currentTenantId!='kenyafpo'">
				<s:if test="currentTenantId!='efk'" >
				<tr class="odd">
					<td width="30%"><s:text name="averageGirthCm" /></td>
					<td width="30%"><s:textfield name="periodicInspection.averageGirth" /></td>
				</tr>
				</s:if>
				</s:if>
				<s:if test="currentTenantId!='efk'" >
				<tr class="odd">
				<td width="30%"><s:text name="activitieCarriedPostPreVisit" /></td>
				
				<td width="30%"><s:select id="postPerVist" name="selectedActivities"
									list="activitiesCarriedList" listKey="key" listValue="value"
									cssClass="form-control select2Multi" multiple="true" onchange="processActivities(this.value);" />
									<script type="text/javascript">									
									processActivities(jQuery("#postPerVist").val());									
												</script>
												
												
									</td>
				</tr>
				</s:if>
				<tr class="cheDiv">
					<td width="30%"><s:text name="chemicalName" /><sup
				style="color: red;">*</sup></td>
					<td width="30%"><s:textfield name="periodicInspection.chemicalName" id="cheName"/></td>
				</tr>
				
				<tr class="gapPlant">
					<td width="30%"><s:text name="plantsReplanted" /></td>
					<td width="30%"><s:textfield name="periodicInspection.noOfPlantsReplanned" id="noPlant" /></td>
				</tr>
				<!-- Date Picker -->
				
			 <tr class="gapPlant">
					<td width="30%"><s:text name="gapPlantDate" /><sup
				style="color: red;">*</sup></td>
					<td width="30%">
					<s:textfield name="gapPlanting" id="planting"
								readonly="true" theme="simple" cssClass="form-control input-sm" />
								</td>
				</tr>
				
				
				<tr class="interDiv">
				<td width="30%"><s:text name="interPloughing" /><sup
				style="color: red;">*</sup></td>
				
				<td width="30%"><s:select id="ploughId" name="selectedInterPloughing"
									list="interploughingType" listKey="key" listValue="value"
									cssClass="form-control select2Multi" multiple="true" headerKey="" headerValue="%{getText('txt.select')}"/>
									</td>
				</tr>
				
				<s:if test="currentTenantId=='lalteer'">
				<tr class="odd">
					<td width="30%"><s:text name="cropRotation" /></td>
					<td width="30%">
					<s:radio id="cropRot"
								list="radioValueList"
								name="periodicInspection.cropRotation"/>
					</td>
				</tr>
				
				<tr class="odd">
					<td width="30%"><s:text name="temp" /></td>
					<td width="30%"><s:textfield name="periodicInspection.temperature" default="NA" /></td>
				</tr>
				
				<tr class="odd">
					<td width="30%"><s:text name="rain" /></td>
					<td width="30%"><s:textfield name="periodicInspection.rain" default="NA" /></td>
				</tr>
				
				<tr class="odd">
					<td width="30%"><s:text name="humidity" /></td>
					<td width="30%"><s:textfield name="periodicInspection.humidity" default="NA" /></td>
				</tr>
				
				<tr class="odd">
					<td width="30%"><s:text name="windSpeed" /></td>
					<td width="30%"><s:textfield name="periodicInspection.windSpeed" default="NA" /></td>
				</tr>
				</s:if>
			</table>
			<table id="fertilizerTbl" class="table table-bordered aspect-detail">
			<input type="hidden" name="fertiJson" id="fertiJson"/>
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion" 
					><s:text name="info.ferti" /></a><sup
				style="color: red;">*</sup></th>
			</tr>
			<tbody id="fertilizer">
			
			<tr>
			<div id="fertiError" style="text-align: left; padding: 5px 0 0 0; color: red;"></div>
			</tr>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="fertilizerType" />
						</div></td>
					

					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="qty" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="aaaction" />
						</div></td>
				 </tr>
				 <tr>
				 <td style="width: 17%;"><div class="col-xs-16">
							<s:select id="fertilizerTypeId" name=""
									list="fertilizerType" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('txt.select')}"
									cssClass="form-control" onchange="addFertOtherValue()"/><br/><div class="hide" id="otherFertDiv"><s:textfield name="otherFertValue" maxlength="20" id="otherFertValue"/><sup
				style="color: red;">*</sup>
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield name="farm.longitude" maxlength="20" id="fertilizerQty"  onkeypress="return isNumber(event)"
								cssClass="form-control input-sm" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<button type="button" onclick="appendFertiRow()" id="appendFertiBtnId" class="btn btn-sm btn-success"><i class="fa fa-check"></i></button>
						</div></td>
				</tr>
				
				
			</tbody>
			<tfoot id="fertilizerSet">
						<s:if test='"update".equalsIgnoreCase(command)'>
				<s:iterator value="fertilizerList" status="status">
			
					<tr id='fertiRow<s:property value="%{#status.count}" />'>
						<td align="center"><div id='selectedFerti1<s:property value="%{#status.count}" />'><s:text name="fertilizerValue.split(',')[%{#status.index}]" /><input type="hidden" value='<s:property value="otherCatalogueValueName" />' id='selectedFertiOth<s:property value="%{#status.count}" />'/></div></td>
						<td align="center"><div id='fertiQtyVal<s:property value="%{#status.count}" />'><s:property value="quantityValue" /></div></td>
						<td align="center" class="hide"><div id='selectedFerti<s:property value="%{#status.count}" />'><s:text name="catalogueValue" /></div></td>
						<td style="text-align:center;">
							<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteFertiRow('<s:property value="%{#status.count}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
						</td>	
						<input type="hidden" value='<s:property value="id" />' id='fertiId<s:property value="%{#status.count}" />'/>	
					</tr>
			</s:iterator>
					</s:if>				
		
			</tfoot>
		</table> 
			
		<table id="manureTbl" class="table table-bordered aspect-detail">
			<input type="hidden" name="manureJson" id="manureJson"/>
			<tr>
				<th colspan="6"><a data-toggle="collapse"
					data-parent="#accordion" 
					><s:text name="info.manure" /></a><sup style="color: red;">*</sup></th>
			</tr>
			<tbody id="manure">
			<tr>
			<div id="manureError" style="text-align: left; padding: 5px 0 0 0; color: red;"></div>
			</tr>
				<tr>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="manureType" />
						</div></td>
					

					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="qty" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:text name="aaaction" />
						</div></td>
				 </tr>
				 <tr>
				 <td style="width: 17%;"><div class="col-xs-16">
							<s:select id="manureId" name=""
									list="manureMap" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('txt.select')}"
									cssClass="form-control"/>
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<s:textfield name="farm.longitude" maxlength="20" id="manureQty"
								cssClass="form-control input-sm" />
						</div></td>
					<td style="width: 17%;"><div class="col-xs-16">
							<button type="button" onclick="appendManureRow()" id="appendManureBtnId" class="btn btn-sm btn-success"><i class="fa fa-check"></i></button>
						</div></td>
				</tr>
				
				
			</tbody>
			<tfoot id="manureSet">
						<s:if test='"update".equalsIgnoreCase(command)'>
				<s:iterator value="manureList" status="status">
			
					<tr id='manureRow<s:property value="%{#status.count}" />'>
						<td align="center"><div id='selectedManure1<s:property value="%{#status.count}" />'><s:text name="manureValue.split(',')[%{#status.index}]" /></div></td>
						<td align="center"><div id='manureQtyVal<s:property value="%{#status.count}" />'><s:property value="quantityValue" /></div></td>
						<td align="center" class="hide"><div id='selectedManure<s:property value="%{#status.count}" />'><s:text name="catalogueValue" /></div></td>
						<td style="text-align:center;">
							<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteManureRow('<s:property value="%{#status.count}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
						</td>	
						<input type="hidden" value='<s:property value="id" />' id='manureId<s:property value="%{#status.count}" />'/>	
					</tr>
				</s:iterator>
					</s:if>
			</tfoot>
		</table> 
		</div>
		</div>
				 <div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success" ><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		</s:if> <s:else>
		<span id="savebutton" class=""><span class="first-child">
		<button id="submitButton" class="save-btn btn btn-success"
						type="button"  onclick="onSubmit();"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span></s:else>
		<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div> 
			
		
	<%-- <table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="info.intercrop" /></th>
				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="isInterCrop" /></td>
					<td width="30%">
					<s:radio id="isInter"
								list="radioValueList"
								name="isSelectedCrop" onchange="loadCropDiv(this.value);"/>
					</td></td>
				</tr>
			
				<tr class="cropNme">
					<td width="30%"><s:text name="interCropName" /><sup
				style="color: red;">*</sup></td>
					<td width="30%"><s:textfield name="periodicInspection.nameOfInterCrop" id="cropNameId" /></td>
				</tr>
				<tr class="yield">
					<td width="30%"><s:text name="yieldObtained" /></td>
					<td width="30%"><s:textfield name="periodicInspection.yieldObtained" id="yieldId"  onkeypress="return isNumber(event)" /></td>

				</tr>
				<tr class="expInc">
					<td width="30%"><s:text name="expenditureIncurred" /></td>
					<td width="30%"><s:textfield name="periodicInspection.expenditureIncurred" id="expenditureId"   onkeypress="return isNumber(event)"/></td>

				</tr>
				<tr class="income">
					<td width="30%"><s:text name="incomeGenerated" /></td>
					<td width="30%"><s:textfield name="periodicInspection.incomeGenerated" id="incomeId"  onkeypress="return isNumber(event)"/></td>

				</tr>

				<tr class="netProf">
					<td width="30%"><s:text name="netProfitOrLoss" /></td>
					<td width="30%"><s:textfield name="periodicInspection.netProfitOrLoss" id="netProfitId"  onkeypress="return isNumber(event)"/></td>

				</tr>
				
			</table>
			 --%>
			
			
			
			<%-- 
			<s:if test="periodicInspection.farmerVoice!=null && periodicInspection.farmerVoice.length!=0">
			
			<table>
			
				<tr>
					<th colspan="3"><s:text name="info.image" /></th>
				</tr>
				
				<tr>
				
				<td><div class="inputCon">
									<s:file name="farmerImage" id="farmerImage"
										onchange="checkImgHeightAndWidth(this)" />
									<div style="font-size: 10px">
										<s:text name="farmer.imageTypes" />
										<font color="red"> <s:text name="imgSizeMsg" /></font>
									</div>
								</div></td>
				
				</tr>
			
			
			
			</table>
			</s:if> --%>
    </div>
	
	</div>
	<br />


</s:form>
 <s:form name="cancelform" action="periodicInspectionServiceReport_list.action?type=service">
    <s:hidden key="currentPage"/>
</s:form> 
<script type="text/javascript">

$( "#planting" ).datepicker(

		{
			 minDate: new Date(2016,0,1),
			 maxDate: new Date(2030,0,1),
			 yearRange: '1910:2030' ,
			 changeYear: true,
			 changeMonth: true
		}

		);
		
jQuery(document).ready(function(){		
	
	$('.select2Multi').selectize({
		   plugins: ['remove_button'],
		   delimiter: ',',
		   persist: false,
		/*    create: function(input) {
		    return {
		     value: input,
		     text: input
		    }
		   } */
		  });

//InterCrop
var isInterCrop = '<s:property value="isSelectedCrop"/>';
if(isInterCrop=="Y"){
	$(".cropNme").show();
	$("#cropNameId").val('<s:property value="periodicInspection.nameOfInterCrop"/>');
	$(".yield").show();
	$("#yieldId").val('<s:property value="periodicInspection.yieldObtained"/>');
	$(".expInc").show();
	$("#expenditureId").val('<s:property value="periodicInspection.expenditureIncurred"/>');
	$(".income").show();
	$("#incomeId").val('<s:property value="periodicInspection.incomeGenerated"/>');
	$(".netProf").show();
	$("#netProfitId").val('<s:property value="periodicInspection.netProfitOrLoss"/>');
}else{
	$(".cropNme").hide();
	$("#cropNameId").val("");
	$(".yield").hide();
	$("#yieldId").val("");
	$(".expInc").hide();
	$("#expenditureId").val("");
	$(".income").hide();
	$("#incomeId").val("");
	$(".netProf").hide();
	$("#netProfitId").val("");
}

//PestOccured
var pestOcc = '<s:property value="isPestNoticed"/>';
if(pestOcc=="Y"){
	$(".pestDiv").show();
	$(".pestSynmDiv").show();
	$(".pestETLDiv").show();
	$(".pestPbmSol").show();
}else{
	
	$(".pestDiv").hide();
	$(".pestSynmDiv").hide();
	$(".pestETLDiv").hide();
	$(".pestPbmSol").hide();
	$("#pestRecTble").hide();
	if(document.getElementById("pestError") != null){
		document.getElementById("pestError").innerHTML = "";
	}
	
	$("input[name='isPestETL'][value='N']").attr("checked", true);
	$("input[name='isPestSolved'][value='N']").attr("checked", true);
	$('#pestName').val('');
	$('#pestSymName').val('');
}

//DiseaseOccured
var disOcc =  '<s:property value="isDiseaseNoticed"/>';
if(disOcc=="Y"){
	$(".disDiv").show();
	$(".disSynmDiv").show();
	$(".disETLDiv").show();
	$(".disPbmSol").show();
}else{
	$(".disDiv").hide();
	$(".disSynmDiv").hide();
	$(".disETLDiv").hide();
	$(".disPbmSol").hide();
	$("#diseaseSetTbl").hide();
	if(document.getElementById("fungiError") != null){
		document.getElementById("fungiError").innerHTML = "";
	}
	
	$("input[name='isDiseaseETL'][value='N']").attr("checked", true);
	$("input[name='isDiseaseSolved'][value='N']").attr("checked", true);
	$('#diseaseName').val("");
	$('#diseaseSymName').val("");
}


var prevVisit = '<s:property value="selectedActivities"/>';
if(prevVisit==""){
	$(".gapPlant").hide();
	$(".interDiv").hide();
	$(".cheDiv").hide();
	$("#manureTbl").hide();
}
addOtherValue();
addFungOtherValue();
addFertOtherValue();
});

function onSubmit(){
	submitFertilizerValues();
	document.form.submit();
	
	/* if(validateData());
	
	if(validateData()){
		
	} */
}

/*function validateData()
{
	var hit=true;
	var growth=$("#growthId").val();
	//var pestOcc = '<s:property value="isPestNoticed"/>';
	var pestOcc = $("input[name='isPestNoticed']:checked").val();
	var pestName=$("#pestName").val();
//	var pestETL = '<s:property value="isPestETL" />';
 	 var pestETL = $("input[name='isPestETL']:checked").val();

	var cheName=$("#cheName").val();
	var pestJson =jQuery("#pestJson").val(); 
	var fertiJson=jQuery("#fertiJson").val(); 
	//var disOcc =  '<s:property value="isDiseaseNoticed"/>';
	 var disOcc = $("input[name='isDiseaseNoticed']:checked").val();
	var diseaseName=$("#diseaseName").val();
//	var diseaseETL = '<s:property value="isDiseaseETL" />';
 	var diseaseETL = $("input[name='isDiseaseETL']:checked").val();
	var diseaseJson=jQuery("#diseaseJson").val(); 
	 var isInterCrop = $("input[name='isSelectedCrop']:checked").val();
//	var isInterCrop =  document.getElementsByName("isSelectedCrop");
	var cropNameId=jQuery("#cropNameId").val(); 
	 var selectedActivities = $("input[name='selectedActivities']:checked").val();
//	var selectedActivities =  '<s:property value="selectedActivities" />';
	var planting=jQuery("#planting").val(); 
	var ploughId=$("#ploughId").val(); 
	var manureJson=jQuery("#manureJson").val();
	if(growth=="" || growth=="-1")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.warnGrowth"/>';
		hit="false"
		return false;
		
	}
	
	
	else if(fertiJson=="" || fertiJson=="[]")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.fertiList"/>';
		hit="false"
		return false;
		
	}
	
	else if(pestOcc=="Y" && pestName==null)
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.pestName"/>';
		hit="false"
		return false;
    }
	
	
	else if(pestOcc=="Y"  && pestETL=="Y" && pestJson=="[]")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.pestRecSet"/>';
		hit="false"
		return false;
	}
	
	else if(disOcc=="Y" && diseaseName==null)
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.diseaseName"/>';
		hit="false"
		return false;
	}
	
	else if(disOcc=="Y" && diseaseETL=="Y" && diseaseJson=='[]')
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.fungiSet"/>';
		hit="false"
		return false;
	}
	
	
	else if(isInterCrop=="Y" && cropNameId=="")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.cropName"/>';
		hit="false"
		return false;
	}
	
	else if(cheName=="" || cheName=="[]")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.cheName"/>';
		hit="false"
		return false;
	}
	
	else if(ploughId==null  ||  ploughId=="" ||  ploughId=="[]")
	{
		document.getElementById("validateError").innerHTML='<s:text name="empty.interplough"/>';
		hit="false"
		return false;
	}
	
	else if(manureJson=="" || manureJson=="[]")
	{
	document.getElementById("validateError").innerHTML='<s:text name="empty.manure"/>';
	hit="false"
	return false;
	}

	return hit;
	
}
*/
function appendRow(){
	var pestNameVal ;
	var pestValue=$("#pestRecName").val();
	
	<s:if test="currentTenantId!='efk'">
		pestNameVal = $('#pestRecName').find(":selected").text();
	</s:if>
	<s:else>
		pestNameVal = $("#pestRecName").val();
	</s:else>
	var qty=$("#pestQty").val();
	var pestOther = $("#otherPestValue").val();
	var foot=document.getElementById("pestRecomented")
	var rows = foot.getElementsByTagName("tr");
	
	
	if(pestValue==99 && pestOther==''){
		$("#pestError").html("Please specify Other Value");
		return false;
	}
	
	if(pestValue==99 && pestOther!=''){
		pestNameVal=pestNameVal+":"+pestOther
		$("#pestRecName").val(pestNameVal);
	}
	
	if(pestValue=='-1' &&qty=='' ){
		$("#pestError").html("No Record Selected");
		return false;
	}else if(pestValue=='-1'&& qty!=''){
		$("#pestError").html("Please Select PestRecommented");
		return false;
	}else if(pestValue!='-1' && qty==''){
		$("#pestError").html("Please Enter Quantity");
		return false;
	}
	else if(pestValue!=''&& qty!=''){
		  
		var flag = 0;
			var tableFoot=document.getElementById("pestRecomented");
			var rows = tableFoot.getElementsByTagName("tr");
			for(i=1;i<=rows.length;i++){
				var obj=new Object();
				var selValue="#selectedPest"+i;
				var catVal=jQuery(selValue).text();
				if(catVal==pestValue){
					flag=1;
					$("#pestError").html("Item is Already exist");
					return false;
				}}
				if(flag==0){
				document.getElementById("pestError").innerHTML = "";	
				
	        	$("#pestRecomented").append(
				"<tr class='tableTr1' id='pestRow"+((rows.length)+1)+"'>"+
					"<td align='center'><div id='selectedPest1"+((rows.length)+1)+"'>"+pestNameVal+"</div></td>"+
					"<td align='center'><div id='pestQtyVal"+((rows.length)+1)+"'>"+qty+"</div></td>"+
					"<td align='center'class='hide'><div id='selectedPest"+((rows.length)+1)+"'>"+pestValue+"</div></td>"+
				'<td style="text-align:center;">'+
				' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
				+"</tr><input type='hidden' value='0' id='pestRecId"+((rows.length)+1)+"'>"
				);
	        	
		$('#otherDiv').addClass("hide");
				}
		//$("#otherPestValue").val('');
	}else{
		$("#pestError").html("");
		return false;
	} 
	<s:if test="currentTenantId!='efk'">
	resetPestData();
	</s:if>
}
function deleteRow(value){
	var rowId="pestRow"+value;
	$('#'+rowId).remove();
	}

function resetPestData(){
	jQuery("#pestRecName").val("-1");
	jQuery("#pestQty").val("");
	$("#appendBtnId").attr("onclick","appendRow()"); 
}

function appendFungicideRow(){
	var pestValue=$("#fungRecName").val();
	var pestNameVal = $('#fungRecName').find(":selected").text();
	var qty=$("#fungQty").val();
	var pestOther = $("#otherFungValue").val();
	var foot=document.getElementById("fungicideRecomented")
	var rows = foot.getElementsByTagName("tr");

	if(pestValue==99 && pestOther==''){
		$("#fungiError").html("Please specify Other Value");
		return false;
	}
	if(pestValue==99 && pestOther!=''){
		pestNameVal=pestNameVal+":"+pestOther
		$("#fungRecName").val(pestNameVal);
	}
	
	if(pestValue=='-1' &&qty=='' ){
		$("#fungiError").html("No Record Selected");
		return false;
	}else if(pestValue=='-1'){
		$("#fungiError").html("Please Select Fungicide");
		return false;
	}else if(qty==''){
		$("#fungiError").html("Please Enter Quantity");
		return false;
	}
	else if(pestValue!=''&& qty!=''){
			
		    var flag = 0;
			var tableFoot=document.getElementById("fungicideRecomented");
			var rows = tableFoot.getElementsByTagName("tr");
			for(i=1;i<=rows.length;i++){
				var obj=new Object();
				var selValue="#selectedFung"+i;
				var catVal=jQuery(selValue).text();
				if(catVal==pestValue){
					flag=1;
					$("#fungiError").html("Item is Already exist");
					return false;
				}}
				if(flag==0){
				document.getElementById("fungiError").innerHTML = "";	
	        	$("#fungicideRecomented").append(
				"<tr class='tableTr1' id='fungicideRow"+((rows.length)+1)+"'>"+
					"<td align='center'><div id='selectedFung1"+((rows.length)+1)+"'>"+pestNameVal+"</div></td>"+
					"<td align='center'><div id='fungQtyVal"+((rows.length)+1)+"'>"+qty+"</div></td>"+
					"<td align='center'class='hide'><div id='selectedFung"+((rows.length)+1)+"'>"+pestValue+"</div></td>"+
				'<td style="text-align:center;">'+
				' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteFungRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
				+"</tr><input type='hidden' value='0' id='fungisideId"+((rows.length)+1)+"'>"
		);
		$('#otherFungDiv').addClass("hide");}
		//$("#otherFungValue").val('');
	}else{
		$("#fungiError").html("");
		return false;
	} 
	
	resetFungData();
}
function deleteFungRow(value){
	var rowId="fungicideRow"+value;
	$('#'+rowId).remove();
}

function resetFungData(){
	jQuery("#fungRecName").val("-1");
	jQuery("#fungQty").val("");
	$("#appendFungBtnId").attr("onclick","appendFungicideRow()"); 
}


function appendManureRow(){
	var pestValue=$("#manureId").val();
	var pestNameVal = $('#manureId').find(":selected").text();
	var qty=$("#manureQty").val();
	var foot=document.getElementById("manureSet")
	var rows = foot.getElementsByTagName("tr");

	if(pestValue=='-1' &&qty=='' ){
		$("#manureError").html("No Record Selected");
		return false;
	}else if(pestValue=='-1'&& qty!=''){
		$("#manureError").html("Please Select Manure");
		return false;
	}else if(pestValue!='-1' && qty==''){
		$("#manureError").html("Please Enter Quantity");
		return false;
	}
	else if(pestValue!=''&& qty!=''){
	    var flag = 0;
		var tableFoot=document.getElementById("manureSet");
		var rows = tableFoot.getElementsByTagName("tr");
		for(i=1;i<=rows.length;i++){
			var obj=new Object();
			var selValue="#selectedManure"+i;
			var catVal=jQuery(selValue).text();
			if(catVal==pestValue){
				flag=1;
				$("#manureError").html("Item is Already exist");
				return false;
			}}
			if(flag==0){
			document.getElementById("manureError").innerHTML = "";			
	    	$("#manureSet").append(
				"<tr id='manureRow"+((rows.length)+1)+"'>"+
					"<td align='center'><div id='selectedManure1"+((rows.length)+1)+"'>"+pestNameVal+"</div></td>"+
					"<td align='center'><div id='manureQtyVal"+((rows.length)+1)+"'>"+qty+"</div></td>"+
					"<td align='center'  class='hide'><div id='selectedManure"+((rows.length)+1)+"'>"+pestValue+"</div></td>"+
				'<td style="text-align:center;">'+
				' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteManureRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
				+"<input type='hidden' value='0' id='manureId"+((rows.length)+1)+"'></tr>"
		);
			}	
	}else{
		$("#manureError").html("");
		return false;
	}
	resetManureData();
}
function deleteManureRow(value){
	var rowId="manureRow"+value;
	$('#'+rowId).remove();
}

function resetManureData(){
	jQuery("#manureId").val("-1");
	jQuery("#manureQty").val("");
	$("#appendManureBtnId").attr("onclick","appendManureRow()"); 
}

function appendFertiRow(){
	var pestValue=$("#fertilizerTypeId").val();
	var pestNameVal = $('#fertilizerTypeId').find(":selected").text();
	var qty=$("#fertilizerQty").val();
	var pestOther=$("#otherFertValue").val();
	var foot=document.getElementById("fertilizerSet")
	var rows = foot.getElementsByTagName("tr");
	
	if(pestValue==99 && pestOther==''){
		$("#fertiError").html("Please specify Other Value");
		return false;
	}
	if(pestValue==99 && pestOther!=''){
		pestNameVal=pestNameVal+":"+pestOther
		
	}
	if(pestValue=='-1' &&qty=='' ){
		$("#fertiError").html("No Record Selected");
		return false;
	}else if(pestValue=='-1'&& qty!=''){
		$("#fertiError").html("Please Select FertilizerRecommented");
		return false;
	}else if(pestValue!='-1' && qty==''){
		$("#fertiError").html("Please Enter Quantity");
		return false;
	}
	else if(pestValue!=''&& qty!=''){
		
	    var flag = 0;
		var tableFoot=document.getElementById("fertilizerSet");
		var rows = tableFoot.getElementsByTagName("tr");
		for(i=1;i<=rows.length;i++){
			var obj=new Object();
			var selValue="#selectedFerti"+i;
			var catVal=jQuery(selValue).text();
			if(catVal==pestValue){
				flag=1;
				$("#fertiError").html("Item is Already exist");
				return false;
			}}
			if(flag==0){
			document.getElementById("fertiError").innerHTML = "";	
		    $("#fertilizerSet").append(
				"<tr id='fertiRow"+((rows.length)+1)+"'>"+
					"<td align='center'><div id='selectedFerti1"+((rows.length)+1)+"'>"+pestNameVal+"</div></td>"+
					"<td align='center'><div id='fertiQtyVal"+((rows.length)+1)+"'>"+qty+"</div></td>"+
					"<td align='center' class='hide'><div id='selectedFerti"+((rows.length)+1)+"'>"+pestValue+"</div></td>"+
				'<td style="text-align:center;">'+
				' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteFertiRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
				+"<input type='hidden' value='0' id='fertiId"+((rows.length)+1)+"'> </tr>"
		);
		$('#otherFertDiv').addClass("hide");}
		
	} else{
		$("#fertiError").html("");
		return false;
	}
	resetFertiData();
}
function deleteFertiRow(value){
	
	var rowId="fertiRow"+value;
	$('#'+rowId).remove();
}

function resetFertiData(){
	jQuery("#fertilizerTypeId").val("-1");
	jQuery("#fertilizerQty").val("");
	$("#appendFertiBtnId").attr("onclick","appendFertiRow()"); 
}


function pestValue(){
	var pestRecomList=[];
	<s:if test="currentTenantId!='efk'"> 	 
	var tableFoot=document.getElementById("pestRecomented");
	 	 var rows = tableFoot.getElementsByTagName("tr");
		
		for(i=1;i<=rows.length;i++){
			var obj=new Object();
			var selValue="#selectedPest"+i;
			var qtyValue="#pestQtyVal"+i;
			var id="#pestRecId"+i;
			var othValue=jQuery("#selectedPestOth"+i).val();
			obj.catalogueValue=jQuery(selValue).text();
			obj.quantityValue=jQuery(qtyValue).text();
			var catVal=jQuery(selValue).text();
			if(catVal==99){
			obj.otherCatalogueValueName=$("#otherPestValue").val();
			}
			if(othValue!='' && othValue!=undefined){
				obj.otherCatalogueValueName=othValue;
				
			}
			obj.id=jQuery(id).val();
			
			pestRecomList.push(obj);
		}
		var jsonArray=new Object();
		jsonArray=JSON.stringify(pestRecomList);
		jQuery("#pestJson").val(jsonArray); 
	
	</s:if>
	<s:else>
		var obj = $("#pestRecName").val();
		$("#pestJson").val(obj)
		alert($("#pestJson").val())
	</s:else>
	}
	
function fungiValue(){
	var fungiList=[];
	<s:if test="currentTenantId!='efk'"> 	 
	var tableFoot=document.getElementById("fungicideRecomented");
	var rows = tableFoot.getElementsByTagName("tr");
	
	for(i=1;i<=rows.length;i++){
		var obj=new Object();
		var selValue="#selectedFung"+i;
		var qtyValue="#fungQtyVal"+i;
		var othValue=jQuery("#selectedFungOth"+i).val();
		var id="#fungisideId"+i;
		var catVal=jQuery(selValue).text();
		if(catVal==99){
			obj.otherCatalogueValueName=$("#otherFungValue").val();
			}
		if(othValue!='' && othValue!=undefined){
			obj.otherCatalogueValueName=othValue;
			
		}
		obj.catalogueValue=jQuery(selValue).text();
		obj.quantityValue=jQuery(qtyValue).text();
		obj.id=jQuery(id).val();
		
		fungiList.push(obj);
	}
	
	var jsonArray=new Object();
	jsonArray=JSON.stringify(fungiList);
	//jQuery("#diseaseJson").val(jsonArray); 
	</s:if>
	<s:else>
	var obj = $("#fungRecName").val();
	jQuery("#diseaseJson").val(obj); 
	alert("diseaseJson - "+$("#diseaseJson").val())
	</s:else>
}

function fertilizerValue(){
	 var tableFoot=document.getElementById("fertilizerSet");
	var rows = tableFoot.getElementsByTagName("tr");
	var fertilizerList=[];
	for(i=1;i<=rows.length;i++){
		var obj=new Object();
		var selValue="#selectedFerti"+i;
		var qtyValue="#fertiQtyVal"+i;
		var othValue=jQuery("#selectedFertiOth"+i).val();
		var id="#fertiId"+i;
		var catVal=jQuery(selValue).text();
		if(catVal==99){
			obj.otherCatalogueValueName=$("#otherFertValue").val();
		}
		
		if(othValue!='' && othValue!=undefined){
			obj.otherCatalogueValueName=othValue;
			
		}
		obj.catalogueValue=jQuery(selValue).text();
		obj.quantityValue=jQuery(qtyValue).text();
		obj.id=jQuery(id).val();
		
		fertilizerList.push(obj);
	}
	var jsonArray=new Object();
	jsonArray=JSON.stringify(fertilizerList);
	jQuery("#fertiJson").val(jsonArray);
}

function manureValue(){
	 var tableFoot=document.getElementById("manureSet");
	var rows = tableFoot.getElementsByTagName("tr");
	var manureList=[];
	for(i=1;i<=rows.length;i++){
		var obj=new Object();
		var selValue="#selectedManure"+i;
		var qtyValue="#manureQtyVal"+i;
		var id="#manureId"+i;
		
		obj.catalogueValue=jQuery(selValue).text();
		obj.quantityValue=jQuery(qtyValue).text();
		obj.id=jQuery(id).val();
		
		manureList.push(obj);
	}
	var jsonArray=new Object();
	jsonArray=JSON.stringify(manureList);
	jQuery("#manureJson").val(jsonArray);
}

function submitFertilizerValues(){
	
	pestValue();
	fertilizerValue();
	manureValue();
	fungiValue();
	
}

//Multiselect option for pestName
var selectedPestName =  '<s:property value="selectedPestName" />';
if(selectedPestName!=null && selectedPestName.trim()!=""){
 var values = selectedPestName.split("\\,");
 $(".pestOther").hide();
 $.each(selectedPestName.split(","), function(i,e){
	 if (e.trim() == 99) {
			$(".pestOther").show();
			$("#pestOtherValue").val('<s:property value="selectedOtherPest" />');
		} 
	 
     $("#pestName option[value='" + e.trim() + "']").prop("selected", true);
 });
}
 else{
	 $(".pestOther").hide();
} 

//Multiselect option for diseaseName
var selectedDiseaseName =  '<s:property value="selectedDiseaseName" />';
if(selectedDiseaseName!=null && selectedDiseaseName.trim()!=""){
var values = selectedDiseaseName.split("\\,");
$(".diseaseOther").hide();
$.each(selectedDiseaseName.split(","), function(i,e){
	 if (e.trim() == 99) {
			$(".diseaseOther").show();
			$("#diseaseOtherValue").val('<s:property value="selectedOtherDisease" />');
		} 
   $("#diseaseName option[value='" + e.trim() + "']").prop("selected", true);
});
} else{
	 $(".diseaseOther").hide();
} 

//Multiselect option for activitiesName
var selectedActivities =  '<s:property value="selectedActivities" />';
if(selectedActivities!=null && selectedActivities.trim()!=""){
var values = selectedActivities.split("\\,");
$(".gapPlant").hide();
$(".interDiv").hide(); 
$(".cheDiv").hide();
$("#manureTbl").hide();
$.each(selectedActivities.split(","), function(i,e){
	
	 if (e.trim() == '1') {
			$(".gapPlant").show();
			document.getElementById('planting').innerHTML='<s:text name="periodicinspection.gapPlantingDate"/>';
			 $("#noPlant").val('<s:property value="periodicInspection.noOfPlantsReplanned"/>');
		}
	 else if(e.trim() == '2'){
		 $(".interDiv").show();
		 var selectedInterPloughing =  '<s:property value="selectedInterPloughing" />';
		 if(selectedInterPloughing!=null && selectedInterPloughing.trim()!=""){
		 var values = selectedInterPloughing.split("\\,");

		 $.each(selectedInterPloughing.split(","), function(i,e){
		    $("#ploughId option[value='" + e.trim() + "']").prop("selected", true);
		 });
		 }
	 }
	 else if(e.trim() == '8'){
		 $(".cheDiv").show();
		 $("#cheName").val('<s:property value="periodicInspection.chemicalName"/>');
	 }
	 else if(e.trim() == '5'){
		 $("#manureTbl").show();
	 }
	 
   $("#postPerVist option[value='" + e.trim() + "']").prop("selected", true);
});
}

//Multiple option for PestSymptoms
var selectedPestSymptom =  '<s:property value="selectedPestSymptom" />';
if(selectedPestSymptom!=null && selectedPestSymptom.trim()!=""){
var values = selectedPestSymptom.split("\\,");

$.each(selectedPestSymptom.split(","), function(i,e){
   $("#pestSymName option[value='" + e.trim() + "']").prop("selected", true);
});
}

//Multiple Option for DiseaseSymptoms
var selectedDiseaseSymptom =  '<s:property value="selectedDiseaseSymptom" />';
if(selectedDiseaseSymptom!=null && selectedDiseaseSymptom.trim()!=""){
var values = selectedDiseaseSymptom.split("\\,");

$.each(selectedDiseaseSymptom.split(","), function(i,e){
   $("#diseaseSymName option[value='" + e.trim() + "']").prop("selected", true);
});
}

function processPestOther(obj){
	var selectedPestVal = $('#pestName').val();
	jQuery(".pestOther").hide();
	$.post("periodicInspectionServiceReport_populatePestSym",{selectedPestVal:selectedPestVal.toString()},function(result){
			insertOptions("pestSymName",JSON.parse(result));
		
	});
	 
	if(selectedPestVal!= null && selectedPestVal!=""){
		var otherVal =selectedPestVal.toString().trim();
		if(otherVal.includes(',')){
			var pestNameOther=otherVal.toString().split(",");
			$.each(pestNameOther, function(i,e){
				if(e.trim() == 99){
				jQuery(".pestOther").show();
				}
			});
		}
	}
}


function processDiseaseOther(obj){
	var selectedDiseaseVal = $('#diseaseName').val();
	jQuery(".diseaseOther").hide();
	$.post("periodicInspectionServiceReport_populateDiseSym",{selectedDiseaseVal:selectedDiseaseVal.toString()},function(result){
		insertOptions("diseaseSymName",JSON.parse(result));
		
	});
	if(selectedDiseaseVal!= null && selectedDiseaseVal!=""){
		var otherVal =selectedDiseaseVal.toString().trim();
		if(otherVal.includes(',')){
			var diseaseNameOther=otherVal.toString().split(",");
			$.each(diseaseNameOther, function(i,e){
				if(e.trim() == 99){
				jQuery(".diseaseOther").show();
				}
			});
		}
	}
}

function loadCropDiv(val){
	if(val=="Y"){
		$(".cropNme").show();
		$(".yield").show();
		$(".expInc").show();
		$(".income").show();
		$(".netProf").show();
	}else{
		$(".cropNme").hide();
		$("#cropNameId").val("");
		$(".yield").hide();
		$("#yieldId").val("");
		$(".expInc").hide();
		$("#expenditureId").val("");
		$(".income").hide();
		$("#incomeId").val("");
		$(".netProf").hide();
		$("#netProfitId").val("");
	}
	
}

function pestOnchange(val){
	if(val=="Y"){
		$(".pestDiv").show();
		$(".pestSynmDiv").show();
		$(".pestETLDiv").show();
		$(".pestPbmSol").show();
	}else{
		$(".pestDiv").hide();
		$(".pestSynmDiv").hide();
		$(".pestETLDiv").hide();
		$(".pestPbmSol").hide();
		$("#pestRecTble").hide();
		document.getElementById("pestError").innerHTML = "";
		$("input[name='isPestETL'][value='N']").attr("checked", true);
		$("input[name='isPestSolved'][value='N']").attr("checked", true);
		$('#pestName').val('');
		$('#pestSymName').val('');
	}
	
}

function diseaseChangeDiv(val){
	if(val=='Y'){
		$(".disDiv").show();
		$(".disSynmDiv").show();
		$(".disETLDiv").show();
		$(".disPbmSol").show();
	}else{
		$(".disDiv").hide();
		$(".disSynmDiv").hide();
		$(".disETLDiv").hide();
		$(".disPbmSol").hide();
		$("#diseaseSetTbl").hide();
		document.getElementById("fungiError").innerHTML = "";
		$("input[name='isDiseaseETL'][value='N']").attr("checked", true);
		$("input[name='isDiseaseSolved'][value='N']").attr("checked", true);
		$('#diseaseName').val("");
		$('#diseaseSymName').val("");
	}
}

function pestETLOnChange(val){
	if(val == 'Y'){
		$("#pestRecTble").show();
	}else{
		$("#pestRecTble").hide();
		$("#pestRecomented").empty();
		document.getElementById("pestError").innerHTML = "";
	}
}

function diseasetETLOnChange(val){
	if(val == 'Y'){
		$("#diseaseSetTbl").show();
	}else{
		$("#diseaseSetTbl").hide();
		$("#fungicideRecomented").empty();
		document.getElementById("fungiError").innerHTML = "";
	}
}

//pestETL
var pestETL = '<s:property value="isPestETL" />';
if(pestETL=="Y"){
	$("#pestRecTble").show();
	
}else{
	$("#pestRecTble").hide();
	$("#pestRecomented").empty();
	if(document.getElementById("pestError") != null){
		document.getElementById("pestError").innerHTML = "";
	}
	
}

//DiseaseETL
var diseaseETL = '<s:property value="isDiseaseETL" />';
if(diseaseETL=="Y"){
	$("#diseaseSetTbl").show();
	
}else{
	$("#diseaseSetTbl").hide();
	$("#fungicideRecomented").empty();
	if(document.getElementById("fungiError") != null){
		document.getElementById("fungiError").innerHTML = "";
	}
	
}

//ActivitiesCarried
function processActivities(val){
	var selectedVal = $('#postPerVist').val();
	$(".gapPlant").hide();
	$(".interDiv").hide();
	$(".cheDiv").hide();
	$("#manureTbl").hide();
	if(selectedVal!= null && selectedVal!=""){
		var value =selectedVal.toString().trim();
		if(value.includes(',')){
			var activityVal=value.toString().split(",");
			$.each(activityVal, function(i,e){
				if(e.trim() == 1){
					$(".gapPlant").show();
				}
				else if(e.trim() == 2){
					$(".interDiv").show();
				}else if(e.trim() == 8){
					$(".cheDiv").show();
				}else if(e.trim() == 5){
					$("#manureTbl").show();
				}
			});
		}else{
			//alert(value);
			if(value == 1){
				$(".gapPlant").show();
			}
			else if(value == 2){
				$(".interDiv").show();
			}else if(value == 8){
				$(".cheDiv").show();
			}else if(value == 5){
				$("#manureTbl").show();
			}
		}
	}
}

function insertOptions(ctrlName, jsonArr){
	document.getElementById(ctrlName).length = 0;
	addOption(document.getElementById(ctrlName), "Select", "0");
	for(var i=0;i<jsonArr.length;i++){
		addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
	}
}
function addOption(selectbox,text,value )
{
var optn = document.createElement("OPTION");
optn.text = text;
optn.value = value;
selectbox.options.add(optn);
}
function addOtherValue(){
	var pestVal=$('#pestRecName').val();
	if(pestVal==99){
		$('#otherDiv').removeClass("hide");
	}
}
function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
function addFungOtherValue(){
	var pestVal=$('#fungRecName').val();
	if(pestVal==99){
		$('#otherFungDiv').removeClass("hide");
	}
}
function addFertOtherValue(){
	var pestVal=$('#fertilizerTypeId').val();
	if(pestVal==99){
		$('#otherFertDiv').removeClass("hide");
	}
}

function validateAudio(){
	var isError = true;
	var file = document.getElementById('inspectAudio').files[0];
        var filename = document.getElementById('inspectAudio').value;
        var size = document.getElementById('inspectAudio').files[0].size;
        var fileExt = filename.split('.').pop();
        if (file != undefined) {
		
            if (fileExt !='mp3') {
            	alert('<s:text name="invalidVideoFileExtension"/>');
            	$('#submitButton').prop('disabled', true);
            	file.focus();
            
            	
              }
            else{
            	if(parseInt(size)>2097152){
            		alert('<s:text name="invalidVideoFileSize"/>');
            		$('#submitButton').prop('disabled', true);
                	file.focus();
            	}
            	else
	        	   $('#submitButton').prop('disabled', false);
	        }
            }
        
       
	return true;
}

</script>
</body>