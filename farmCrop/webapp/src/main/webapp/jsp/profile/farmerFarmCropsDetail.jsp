<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
<script src="plugins/openlayers/OpenLayers.js"></script>
<link href="plugins/jplayer/jplayer.blue.monday.min.css"
	rel="stylesheet" type="text/css" />
	
</head>

<script src="js/dynamicComponents.js?v=19.14"></script>

<div style="float: right; margin-right: 1.5%" class="hide">
	<table>
		<tr>
			<td style="font-weight: bold;"><i class="fa fa-user"
				aria-hidden="true" style="color: blue"></i>&nbsp;&nbsp;&nbsp;<s:text
					name="Farmer Name : " /></td>
			<td><s:property value="farmCrops.farm.farmer.name" /></td>
		</tr>
		<tr>
			<td style="font-weight: bold;"><i class="fa fa-pagelines"
				aria-hidden="true" style="color: green"></i>&nbsp;&nbsp;&nbsp;<s:text
					name="Farm Name: " /></td>
			<td><s:property value="farmCrops.farm.farmName" /></td>
		</tr>
	</table>
</div>


<div class="flexbox-container">

	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden key="command" />
			<s:hidden key="farmCrops.id"  class='uId' />
		<s:hidden name="farmId" value="%{farmId}" />
		<s:hidden id="estimatedYieldMetricTonnes"
			name="estimatedYieldMetricTonnes" value="%{farmCrops.estimatedYield}" />
		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper">
							<div class="aPanel">
						     <div class="ferror" id="errorDiv" style="color: #ff0000">
							<s:actionerror />
							<s:fielderror />
						   </div>
								<div class="aTitle">
									<h2>
                                       <s:property value="%{getLocaleProperty('info.farmCrops')}" />		
								          <div class="pull-right">
											<a class="aCollapse" href="#"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmcrops.farmName')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.farm.farmName" />
										</p>
									</div>
									
									 
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmcrops.cropCategory.prop" />
											</p>
											<p class="flexItem">
												<s:text name="cs%{farmCrops.cropCategory}" />
												&nbsp;
											</p>
										</div>
									
									<div class="dynamic-flexItem2">
										<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmfarmcrops.cropSeason')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.cropSeason.name" />
											&nbsp;
										</p>
									</div>
									
									
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId !='chetna' && currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId !='crsdemo'										
										&& currentTenantId !='welspun' && currentTenantId!='griffith' && currentTenantId!='ecoagri' && currentTenantId!='livelihood'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farmCrops.CultivationType" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.cropCategoryList" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('cropName')}" />
											
										</p>
										<p class="flexItem">
											<s:property
												value="farmCrops.procurementVariety.procurementProduct.name" />
											&nbsp;
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="variety" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.procurementVariety.name" />
											&nbsp;
										</p>
									</div>
									<s:if test="currentTenantId=='griffith' || currentTenantId=='livelihood'">
									<div class="dynamic-flexItem2 livelihoodMain">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('prodTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.prodTrees" />

												</p>
									</div>
									</s:if>
									<s:if test="currentTenantId=='livelihood'">
									
										<div class="dynamic-flexItem2 livelihoodMain">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('affTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.affTrees" />

												</p>
									</div>
									</s:if>
									
									<s:if test="currentTenantId =='kpf'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
											<s:property value="%{getLocaleProperty('cultiArea')}" />
											</p>
											<p class="flexItem" >
												<s:property value="farmCrops.cultiArea" />
												&nbsp;
											</p>
										</div>
										<div class="dynamic-flexItem2" id="sowDate">
											<p class="flexItem">
												<s:text name="farmcrops.sowingDate" />
											</p>
											<p class="flexItem">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId!='ecoagri' || currentTenantId=='livelihood'">
											<div class="dynamic-flexItem2 livelihoodMain">
											
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('cultiArea')}" /> 
													<s:if test="currentTenantId =='susagri'"> 
										(<s:property	value="%{getLocaleProperty('hectare')}" />)
										</s:if>
										<s:else>
										(<s:property value="%{getAreaType()}" />)
										</s:else>
												</p>
												<p class="flexItem" name="farmCrops.cultiArea">
													<s:property value="farmCrops.cultiArea" />
													&nbsp;
												</p>
											</div></s:if>
											<s:if test="currentTenantId =='fruitmaster'">
									
										<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('cultiAreaKanal')}" />
												</p>
												<p class="flexItem" name="farmCrops.cultiArea">
														<span id="cultiAreaKanal"></span>
													&nbsp;
												</p>
											</div>
									</s:if>
										<s:if test="currentTenantId!='livelihood'">
											<div class="dynamic-flexItem2" id="sowDate">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmcrops.sowingDate')}" />
													<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)
													</s:if>
												</p>
												<p class="flexItem">
													<s:property value="sowingDate" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									
									
								<s:if test="currentTenantId !='chetna' && currentTenantId !='iccoa' && currentTenantId !='welspun' && currentTenantId !='wilmar' && currentTenantId !='ecoagri' && currentTenantId!='livelihood'  && currentTenantId !='avt' && currentTenantId !='susagri'
								&& currentTenantId !='kenyafpo'"> 
										<s:if test="farmCrops.cropCategory == 0">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('type')}" />
												</p>
												<p class="flexItem" name="farmCrops.type">
													<s:property value="farmCrops.type" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<s:if test="currentTenantId!='kpf' && currentTenantId !='simfed' && currentTenantId !='wub'  && currentTenantId!='movcd' && currentTenantId!='crsdemo' 
									&& currentTenantId !='gar' && currentTenantId !='welspun'&& currentTenantId !='wilmar' && currentTenantId!='griffith' && currentTenantId!='livelihood'
									&& currentTenantId !='kenyafpo'">
										<div class="dynamic-flexItem2 seedSource">
											<p class="flexItem">
												<s:text name="seedSource" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedSource" />
												&nbsp;
											</p>
										</div>
									</s:if>
			<%-- 						<s:if test='currentTenantId=="iccoa"'>
									<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
											<s:property value="%{getLocaleProperty('satbleLength')}" />
												
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if> --%>
									<s:if test="currentTenantId!='pratibha' &&currentTenantId !='gsma' &&currentTenantId !='pgss' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
									&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar'&& currentTenantId !='welspun' 
									&& currentTenantId!='simfed'&& currentTenantId!='wub' && currentTenantId!='ecoagri' && currentTenantId!='livelihood' && currentTenantId!='ocp' && currentTenantId!='susagri'
									&& currentTenantId!='avt' && currentTenantId !='kenyafpo'">
										<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('satbleLength')}" />
											</p>
											<p class="flexItem" >
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId !='gsma'  && currentTenantId !='simfed' && currentTenantId !='wub' && currentTenantId!='movcd' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
										&& currentTenantId!='crsdemo' && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId !='griffith'
										&& currentTenantId !='ecoagri' && currentTenantId!='avt' && currentTenantId !='kenyafpo'">
										<div class="dynamic-flexItem2 livelihoodMain" id=seedQtyUsed>
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyUsed')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedQtyUsed" />
												&nbsp;
											</p>
										</div>
										<s:if test="currentTenantId!='ocp' && currentTenantId!='livelihood'">
										<div class="dynamic-flexItem2" id=seedQtyUsed>
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyCost')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedQtyCost" />
												&nbsp;
											</p>
										</div>
										</s:if>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer' ||  currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="riskAssesment">
											<p class="flexItem">
												<s:text name="farmcrops.riskAssesment" />
											</p>
											<p class="flexItem">
												<s:text name="rs%{farmCrops.riskAssesment}" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="captureAssessment">
											<p class="flexItem">
												<s:text name="farmcrops.riskBufferZoneDistanse" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.riskBufferZoneDistanse" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer'|| currentTenantId=='mhr' || currentTenantId=='chetna'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmCrops.seedTreatmentDetails" />
											</p>
											<s:if test="farmCrops.seedTreatmentDetails=='99'">
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
													-
													<s:property value="farmCrops.otherSeedTreatmentDetails" />
													</td>
												</p>
											</s:if>
											<s:else>
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
												</p>
											</s:else>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId =='pratibha'">
										<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:text name="farmfarmcrops.estimatedYeild.quintals" />
												</p>
												<p class="flexItem" id="estimatedYieldQuintal"></p>
											</div>
											<div class="dynamic-flexItem2 " id="harvestDat">
												<p class="flexItem">
													<s:text name="farmcrops.harvestDate" />
													(DD-MM-YYYY)
												</p>
												<p class="flexItem">
													<s:property value="harvestDate" />

												</p>
											</div>
										</s:if>
										<s:else>
											<div class="dynamic-flexItem2 livelihoodMain">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" />
												</p>
												<p class="flexItem" name="farmCrops.estYldSfx">
													<s:property value="farmCrops.estimatedYield" />

												</p>
											</div>
										<s:if test="currentTenantId!='griffith' && currentTenantId!='simfed' && currentTenantId!='ecoagri' && currentTenantId!='livelihood'">
											<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:text name="farmfarmcrops.estimatedYeild.tonnes" />
												</p>
												<p class="flexItem" id="estimatedYieldMetTon"></p>


											</div>
											<s:if test="currentTenantId!='ocp' || currentTenantId!='livelihood'">
												<div class="dynamic-flexItem2 " id="harvestDat">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmcrops.harvestDate')}" />
													<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)</s:if>
												</p>
												<p class="flexItem">
													<s:property value="harvestDate" />

												</p>
											</div></s:if></s:if>
										
</s:else>
                                           
										
									</s:if>

								<s:if test="currentTenantId=='livelihood'">
									<div class="dynamic-flexItem2 livelihoodInter">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('noOfTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.noOfTrees" />

												</p>
									</div>
								</s:if>
								
								<s:if test="currentTenantId=='wilmar'">
								
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('noOfTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.noOfTrees" />

												</p>
									</div>
									
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('prodTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.prodTrees" />

												</p>
									</div>
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('affTrees')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.affTrees" />

												</p>
									</div>
									
										
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('yearOfPlanting')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.interAcre" />

												</p>
									</div>
								</s:if>
								</div>
								
								
								<div class="dynamicFieldsRender"></div>
							</div>
							
					
							
							
							
							
							
							
							
							
							
							<div class="flexItem flex-layout flexItemStyle">
								<div class="">
								
								
								
									<sec:authorize ifAllGranted="profile.farmer.update">
										<span id="update" class=""><span class="first-child">
												<button type="button" class="edit-btn btn btn-success">
													<FONT color="#FFFFFF"> <b><s:text
																name="edit.button" /></b>
													</font>
												</button>
										</span></span>
									</sec:authorize>
								
									<sec:authorize ifAllGranted="profile.farmer.delete">
										<span id="delete" class=""><span class="first-child">
												<button type="button" class="delete-btn btn btn-warning">
													<FONT color="#FFFFFF"> <b><s:text
																name="delete.button" /></b>
													</font>
												</button>
										</span></span>
									</sec:authorize>
							
							
							
									<span id="cancel" class=""><span class="first-child">
											<button type="button" class="back-btn btn btn-sts">
												<b><FONT color="#FFFFFF"><s:text
															name="back.button" /> </font></b>
											</button>
									</span></span>
								</div>
							</div>
						</div>
					</div>
					
					
					<div class="flexRight appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<s:hidden id="farmCropCordinates" value="%{jsonObjectList}" />
							<s:hidden id="farmCordinates" value="%{jsonFarmObjectList}" />
							<table class="table table-bordered aspect-detail fillform">
								<h2>
									<s:text name="info.digitalLocator" />
								</h2>
								<tbody id="farmMapAccordian" class="panel-collapse in">
									<tr>
										<td style="border-right: solid 1px #d2d695 !important;">
											
													<div id="map" class="smallmap" style="height:500px"></div>
										</td>
										</tr>
										</tbody>
									
									<%-- <tr>
									<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.latitude" />
						</p>
					</div><div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.longitude" />
						</p>
					</div>
									
									
									</tr> --%>
								
							</table>
							<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.latitude" />
						</p>
					</div><div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.longitude" />
						</p>
					</div>	
							
				 <s:if test="currentTenantId=='pratibha' && farmCrops.farmCropsCoordinates.size()>0" > 
					<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
						  <s:text name="%{getLocaleProperty('farmCrops.time')}" />
						</p>
						<p class="flexItem">
							<s:date name="farmCrops.plotCapturingTime" format="dd/MM/yyyy"/>
						</p>
					</div>
					</s:if> 
					
						</div>
					</div>
				
					
				</div>
			</div>
		</div>
	</s:form>
</div>
<s:form name="updateform" action="farmCrops_update.action">
	<s:hidden name="farmId" value="%{farmId}" />
	<s:hidden name="farmerId" value="%{farmCrops.farm.farmer.id}" />
	<s:hidden name="farmerUniqueId" value="%{farmCrops.farm.farmer.id}" />
	<s:hidden name="tabIndexz" value="%{tabIndexz}" />
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="farmCrops_delete.action">
	<s:hidden name="farmId" value="%{farmId}" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="tabIndexFarm" value="%{tabIndexz}" />
	<s:hidden name="tabIndexz" value="%{tabIndexz}" />
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="farmer_detail.action%{tabIndexz}">
	<s:hidden name="farmId" value="%{farmId}" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farmCrops.farm.farmer.sangham}" />
	<s:hidden name="tabIndex" value="#tabs-3" />
	<s:hidden key="id" value="%{farmCrops.farm.farmer.id}" />
	<s:hidden key="currentPage" />
</s:form>

<s:form name="listForm" id="listForm" action="farmer_detail.action">
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="id" value="%{farmCrops.farm.farmer.id}" />
	<s:hidden name="tabIndexFarmer" />
	<s:hidden name="tabIndex" value="#tabs-2" />
	<s:hidden name="currentPage" />
</s:form>
<script type="text/javascript"
	src="plugins/jplayer/jquery.jplayer.min.js"></script>


<script type="text/javascript">
var tenant = '<s:property value="getCurrentTenantId()"/>';
var cultiArea='<s:property value="farmCrops.cultiArea" />';
	jQuery(document).ready(function() {
						//$("#map").css("height",$(document).height());	
						if (tenant=='fruitmaster'){
							$("#cultiAreaKanal").text(parseFloat(cultiArea)*8);
						}
						var cropCategory = '<s:property value="farmCrops.cropCategory"/>';
						if(tenant=='livelihood'){
							if(cropCategory==1){
								$('.livelihoodInter').removeClass('hide');
								$('.livelihoodMain').addClass('hide');
							}
							else {
								//$('.livelihoodInter').addClass('hide');
								$('.livelihoodMain').removeClass('hide');
							}
						
						}
						if(tenant=='welspun'){
							$('.livelihoodMain').addClass('hide');
						}
		
						var url = window.location.href;
						url = url + '?id='
								+ '<s:property value="farmCrops.id"/>';

						$(".lanMenu").each(
								function() {
									var url1 = $(this).attr("href") + '&url='
											+ encodeURIComponent(url);
									$(this).attr("href", url1);
								});

						var stag = $('a[href="farm_detail.action?id=&#tabs-3"]');
						$(stag).attr("href", "javascript:onCancel();");

						var stag = $('a[href="farmer_detail.action"]');
						$(stag).attr("href", "javascript:onFarmList();");
						if (cropCategory == 1 || cropCategory == 2) {
							$('#sowDate').addClass("hide");
							$('#harvestDat').addClass("hide");
						}
						var cropPlugin = '<s:property value="cropInfoEnabled"/>';
						
						if (tenant!='welspun' && tenant!='griffith' && tenant!='simfed' && tenant!='ecoagri'){
							$("#estimatedYieldMetTon").text($("#estimatedYieldMetricTonnes").val());
						}
							var estYieldVal = $("#estimatedYieldMetricTonnes")
									.val();
							var totalValu = parseFloat(estYieldVal);
							if (cropPlugin == "1") {
								 if(tenant=='pratibha' && tenant!='griffith' && tenant!='livelihood'){ 
								document.getElementById('estimatedYieldQuintal').innerHTML = (totalValu / 100)
										.toFixed(6);
							}
								 else if (tenant!='welspun' && tenant!='griffith'&& tenant!='simfed' && tenant!='ecoagri' && tenant!='livelihood' && tenant!='wilmar'){
									 document.getElementById('estimatedYieldMetTon').innerHTML = (totalValu / 1000)
										.toFixed(6);
								 }
							}
						//}
						loadFarmCropMap();
						var cropCategory = '<s:property value="farmCrops.cropCategory"/>';
						var riskAssementVal = '<s:property value="farmCrops.riskAssesment"/>';
						if (cropCategory == "1") {
							$("#cropSeason").hide();
							$("#stapleLength").hide();
							$('#captureAssessment').hide();
							$('#riskAssesment').hide();
						} else {
							if (riskAssementVal == 1) {
								$('#captureAssessment').show();
							} else {
								$('#captureAssessment').hide();
							}
						}

						$('#update')
								.on(
										'click',
										function(e) {
											document.updateform.currentPage.value = document.form.currentPage.value;
											document.updateform.submit();
										});

						$('#delete')
								.on(
										'click',
										function(e) {
											if (confirm('<s:text name="confirm.delete"/> ')) {
												document.deleteform.currentPage.value = document.form.currentPage.value;
												document.deleteform.submit();
											}
										});
						/*   if(tenant=='griffith'){
							$('#sowDate').addClass("hide");
						} */  
						renderDynamicDetailFeilds();
					});

	function onFarmList() {
		document.listForm.submit();
	}
	
	 function getTxnType(){
 		
 	 
 	return "357";
 	}
	 
	 function getBranchIdDyn(){
	     	return '<s:property value="farmCrops.farm.farmer.branchId"/>';
	    	}
	 
	function onCancel() {
		document.cancelform.submit();
	}
</script>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script type="text/javascript">
	//Variables relate to loading MAP
	try {
		var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
		var tProjection = new OpenLayers.Projection("EPSG:900913");
		var url = window.location.href;
		var temp = url;
		for (var i = 0; i < 1; i++) {
			temp = temp.substring(0, temp.lastIndexOf('/'));
		}
		var href = temp;
		var coordinateImg = "red_placemarker.png";
		var iconImage = temp + '/img/' + coordinateImg;
	} catch (err) {

	}

	function loadFarmCropMap() {
		try {
			var farmCropCordinates = jQuery("#farmCropCordinates").val();
			//alert("farmCropCordinates"+farmCropCordinates);
			//alert("farmCordinates"+farmCordinates);
			var landArea = JSON.parse(farmCropCordinates);
			if (tenant=='livelihood'){
			var farmCordinates = jQuery("#farmCordinates").val();
			var landFarmArea = JSON.parse(farmCordinates);
			loadMapWithFarm('map', '<s:property value="farmCrops.latitude"/>','<s:property value="farmCrops.longitude"/>', landArea,landFarmArea);
			}else{
				loadMap('map', '<s:property value="farmCrops.latitude"/>','<s:property value="farmCrops.longitude"/>', landArea);
			}
			
			addExistingMarkers(landArea);
		} catch (err) {
			console.log(err);
		}
	}

	var map;
	function initMap() {
		
		var lat="<s:property value="farmCrops.latitude"/>";
		var lon="<s:property value="farmCrops.longitude"/>";
		if(isEmpty(lat) || isEmpty(lon)|| lon==''||lon==''){
			
			var longtitude="<s:property value='getLon()'/>";
			var latitude="<s:property value='getLat()'/>";
			if(!isEmpty(longtitude)|| !isEmpty(latitude)|| latitude!=''||longtitude!=''){
				map = new google.maps.Map(document.getElementById('map'), {
					center : {
						lat : parseFloat(latitude),
						 lng : parseFloat(longtitude)
					},
					zoom :8,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				});
				}
			else{
			 map = new google.maps.Map(document.getElementById('map'), {
					center : {
						lat : 11.0168,
						lng : 76.9558
					},
					zoom : 8,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				}); 
			}
			}
			else{
		map = new google.maps.Map(document.getElementById('map'), {
		
			 center: {
				 lat : parseFloat(lat),
				 lng : parseFloat(lon)
		         },
		         zoom:8,
		         mapTypeId: google.maps.MapTypeId.HYBRID,
		});
	}
	}
	
	function loadMap(mapDiv, latitude, longitude, landArea) {
		try {
			var bounds = new google.maps.LatLngBounds();
			if(!isEmpty(latitude)&&!isEmpty(longitude)){
				var marker;
				marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(latitude),
							parseFloat(longitude)),
					map : map
				});
			}
			
			if(landArea.length>0){
				var cords = new Array();
				$(landArea).each(function(k,v){
					if(v.lat != null && v.lat != undefined && v.lon != null && v.lon != undefined ){
						cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
					}
				/* 	marker = new google.maps.Marker({
						position : new google.maps.LatLng(parseFloat(v.lat),
								parseFloat(v.lon)),
						map : map
					}); */
				});
				
				//alert(JSON.stringify(cords))
				
				 var plotting = new google.maps.Polygon({
			          paths: cords,
			          strokeColor: '#FF0000',
			          strokeOpacity: 0.8,
			          strokeWeight: 2,
			          fillColor: '#E7D874',
			          fillOpacity: 0.35
			        });
				 plotting.setMap(map);
				
				/*  bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
				 map.fitBounds(bounds);
				 
				 var listener = google.maps.event.addListener(map, "idle", function () {
					    map.setZoom(6);
					    google.maps.event.removeListener(listener);
					}); */
					
				 loc = new google.maps.LatLng(cords[0].lat, cords[0].lng);
				 bounds.extend(loc);
				 map.fitBounds(bounds);      // auto-zoom
				 //map.panToBounds(bounds);    // auto-center
				 
				  var arType = '<s:property value="%{getAreaType()}" />';
				  var cultiArea = '<s:property value="%{farmCrops.cultiArea}" />';
				 
				 mapLabel2 = new MapLabel({

	                  text: cultiArea+" "+arType,
	                  position: bounds.getCenter(),
	                  map: map,
	                  fontSize: 14,
	                  align: 'left'
	                });
	                mapLabel2.set('position', bounds.getCenter());
				 
				 
			}
			
		} catch (err) {
			console.log(err);
		}
	}
	
	function loadMapWithFarm(mapDiv, latitude, longitude, landArea,landFarmArea) {
		try {
			var bounds = new google.maps.LatLngBounds();
			if(!isEmpty(latitude)&&!isEmpty(longitude)){
				var marker;
				marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(latitude),
							parseFloat(longitude)),
					map : map
				});
			}
			 var cords = new Array();
			var plotting = [];
			
			if(landArea.length>0){
				if(landFarmArea.length>0){	
				cords =  [];
				
				$(landFarmArea).each(function(k,v){
					if(v.lat != undefined && v.lon != undefined){
						cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
					}
				});
				if (tenant!='livelihood'){
				plotting.push(new google.maps.Polygon({
		            paths: cords,
		            strokeColor: '#FF0000',
		            strokeOpacity: 0.8,
		            strokeWeight: 2,
		            fillColor: '#ed553e',
		            fillOpacity: 0.35
		        }));
				plotting[plotting.length-1].setMap(map);
				}
				}
				if(landArea.length>0){
				cords =  [];
				}
				$(landArea).each(function(k,v){
					if(v.lat != null && v.lat != undefined && v.lon != null && v.lon != undefined ){
						cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
					}
				});
				
				//alert(JSON.stringify(cords))
				if (tenant!='livelihood'){
				plotting.push(new google.maps.Polygon({
            paths: cords,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#a9e84a',
            fillOpacity: 0.35
        }));
				plotting[plotting.length-1].setMap(map);
				}
				
				// plotting.setMap(map);
				
				/*  bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
				 map.fitBounds(bounds);
				 
				 var listener = google.maps.event.addListener(map, "idle", function () {
					    map.setZoom(6);
					    google.maps.event.removeListener(listener);
					}); */
					
				 loc = new google.maps.LatLng(cords[0].lat, cords[0].lng);
				 bounds.extend(loc);
				 map.fitBounds(bounds);      // auto-zoom
				 //map.panToBounds(bounds);    // auto-center
				 
				  var arType = '<s:property value="%{getAreaType()}" />';
				  var cultiArea = '<s:property value="%{farmCrops.cultiArea}" />';
				 
				 mapLabel2 = new MapLabel({

	                  text: cultiArea+" "+arType,
	                  position: bounds.getCenter(),
	                  map: map,
	                  fontSize: 14,
	                  align: 'left'
	                });
	                mapLabel2.set('position', bounds.getCenter());
				 
				 
			}
			
		} catch (err) {
			console.log(err);
		}
	}
	
	 function addExistingMarkers(jsonData){
		 var neighbouringDetails;
		$.each( jsonData, function( k, v ) {
			
			 if(v.neighbouringDetails != undefined){
				  neighbouringDetails = v.neighbouringDetails;
				  
			 }
		 });
		
		
     	$.each( neighbouringDetails, function( key, json ) {
     		  //alert( key + ": " + json );
     		  var position = {};
     		  position["lat"] = Number(json.lat);
     		  position["lng"] = Number(json.lng);
     		var marker = new google.maps.Marker({
                 position: position,
                 map: map,
       		  	title: json.title,
                 draggable: false,
                // label: labels[labelIndex++ % labels.length]
               });
     		var contentString = '<div id="content">'+ '<div id="siteNotice">'+'</div>'+'<h1 id="firstHeading" class="firstHeading">'+json.title+'</h1>'+ '<div id="bodyContent">'+'<p>'+json.description+'</p>'+ '</div>'+'</div>';
         	 var infowindow = new google.maps.InfoWindow({
         		  content: contentString
                });
               
              //google.maps.event.addListener(marker, "rightclick", function (point) {delMarker(marker,position)});
       		 marker.addListener('click', function() {
			          infowindow.open(map, marker);
			      });
       		 
       		
       		  
       		  
       		//neighbouring_markers.push(marker);
       		//neighbouring_result[json.lat+"_"+json.lng] = json;
     	});
     	
     	
     }
	
</script>
<script src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
<script src="js/maplabel-compiled.js?k=2.16"></script>