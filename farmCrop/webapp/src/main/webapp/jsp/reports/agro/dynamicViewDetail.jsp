<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
<script src="plugins/openlayers/OpenLayers.js"></script>
<link href="plugins/jplayer/jplayer.blue.monday.min.css"
	rel="stylesheet" type="text/css" />

</head>

<script src="js/dynamicComponents.js?v=19.20"></script>

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
<s:form id="audioFileDownload"
	action="dynamicViewReport_populateDownloadCropYield">
	<s:hidden id="loadId" name="id" />
</s:form>

<!-- 	<div id= "page">
	<div class = "container">
	<a href="http://www.google.com"></a>
	</div>
	</div> -->
<div class="flexbox-container">
	<div class="appContentWrapper marginBottom ">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="pill" id="tabs8"
				onclick="getFarmerDynamic()" href="#tabs-1"><s:text
						name="Farmer Details" /></a></li>
			<%-- <li ><a data-toggle="pill" onclick="getFarmDynamic()" id="tabs7" href="#tabs-2"><s:text name="Farm Details" /></a></li> --%>
			<li><a data-toggle="pill" onclick="getFarmCropDynamic()" href="#tabs-3"><s:property value="%{getLocaleProperty('crop')}" /></a></li>
			<li><a data-toggle="pill"
				onclick="getPhysicalCheckDynamicDetails()" href="#tabs-5"><s:text
						name="Physical Check" /></a></li>
			<li><a data-toggle="pill" onclick="getMoleculeDetails()"
				href="#tabs-9"><s:text name="Pesticide Residue" /></a></li>
			<li><a data-toggle="pill" href="#tabs-4"><s:text
						name="Pesticide Sprayed" /></a></li>
			<%-- 	 <li ><a data-toggle="pill"  href="#tabs-10"><s:property value="%{getLocaleProperty('Summary')}" /></a></li> --%>
			<%-- <li><a data-toggle="pill" href="#tabs-6"><s:text name="Fertilizer Information" /></a></li>
		<li><a data-toggle="pill" href="#tabs-7"><s:text name="Weed Management" /></a></li>
		<li><a data-toggle="pill" href="#tabs-8"><s:text name="Water Management" /></a></li> --%>
			<li><a data-toggle="pill" href="#tabs-11"><s:property
						value="%{getLocaleProperty('lotHistory')}" /></a></li>
		</ul>
	</div>
	<div class="tab-content">
		<div id="tabs-3" class="tab-pane fade">
			<div class="flex-view-layout">
				<div class="fullwidth">
					<div class="flexWrapper">

						<%-- <s:form name="form" cssClass="fillform">
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
							<div class="aPanel farmCrops">
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
										<p class="flexItem ">
											<s:property value="%{getLocaleProperty('farmcrops.farmName')}" />
										</p>
										<p class="flexItem" name="farmcrops.farmName">
											<s:property value="farmCrops.farm.farmName" />
										</p>
									</div>
									
									 <s:if test="currentTenantId!='griffith'">  
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmcrops.cropCategory.prop" />
											</p>
											<p class="flexItem" name="farmcrops.cropCategory.prop">
												<s:text name="cs%{farmCrops.cropCategory}" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem ">
										<s:property value="%{getLocaleProperty('farmfarmcrops.cropSeason')}" />
										</p>
										<p class="flexItem" name="farmfarmcrops.cropSeason">
											<s:property value="farmCrops.cropSeason.name" />
											&nbsp;
										</p>
									</div>
									
									
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId !='chetna' && currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId !='crsdemo'										
										&& currentTenantId !='welspun' && currentTenantId!='griffith' && currentTenantId!='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem ">
													<s:text name="farmCrops.CultivationType" />
												</p>
												<p class="flexItem" name="farmCrops.CultivationType">
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
										<p class="flexItem" name="farmCrops.procurementVariety.procurementProduct.name">
											<s:property
												value="farmCrops.procurementVariety.procurementProduct.name" />
											&nbsp;
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="variety" />
										</p>
										<p class="flexItem" name="farmCrops.procurementVariety.name">
											<s:property value="farmCrops.procurementVariety.name" />
											&nbsp;
										</p>
									</div>
									<s:if test="currentTenantId=='griffith'">
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('prodTrees')}" />
												</p>
												<p class="flexItem" name="farmCrops.prodTrees">
													<s:property value="farmCrops.prodTrees" />

												</p>
									</div>
									</s:if>
									<s:if test="currentTenantId =='kpf'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
											<s:property value="%{getLocaleProperty('cultiArea')}" />
											</p>
											<p class="flexItem" name="farmCrops.cultiArea">
												<s:property value="farmCrops.cultiArea" />
												&nbsp;
											</p>
										</div>
										<div class="dynamic-flexItem2" id="sowDate">
											<p class="flexItem">
												<s:text name="farmcrops.sowingDate" />
											</p>
											<p class="flexItem" name="sowingDate">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId!='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('cultiArea')}" /> (<s:property value="%{getAreaType()}" />)
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
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmcrops.sowingDate')}" />
													<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)
													</s:if>
												</p>
												<p class="flexItem" name="farmcrops.sowingDate">
													<s:property value="sowingDate" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									
									
								<s:if test="currentTenantId !='chetna' && currentTenantId !='iccoa' && currentTenantId !='welspun' 
								&& currentTenantId !='wilmar' && currentTenantId !='ecoagri' && currentTenantId!='livelihood'"> 
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
									&& currentTenantId !='gar' && currentTenantId !='welspun'&& currentTenantId !='wilmar' && currentTenantId!='griffith' && currentTenantId!='livelihood'">
										<div class="dynamic-flexItem2 seedSource">
											<p class="flexItem">
												<s:text name="seedSource" />
											</p>
											<p class="flexItem" name="farmCrops.seedSource">
												<s:property value="farmCrops.seedSource" />
												&nbsp;
											</p>
										</div>
									</s:if>
			
									<s:if test="currentTenantId!='pratibha' &&currentTenantId !='gsma' &&currentTenantId !='pgss' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
									&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar'&& currentTenantId !='welspun' 
									&& currentTenantId!='simfed'&& currentTenantId!='wub' && currentTenantId!='ecoagri' && currentTenantId!='livelihood' && currentTenantId!='ocp' && currentTenantId!='susagri'">
										<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('satbleLength')}" />
											</p>
											<p class="flexItem" name="farmCrops.stapleLength">
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId !='gsma'  && currentTenantId !='simfed' && currentTenantId !='wub' && currentTenantId!='movcd' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
										&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId !='griffith'
										&& currentTenantId !='ecoagri' && currentTenantId!='livelihood'">
										<div class="dynamic-flexItem2" id=seedQtyUsed>
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyUsed')}" />
											</p>
											<p class="flexItem" name="farmCrops.seedQtyUsed">
												<s:property value="farmCrops.seedQtyUsed" />
												&nbsp;
											</p>
										</div>
										<s:if test="currentTenantId!='ocp'">
										<div class="dynamic-flexItem2" id=seedQtyUsed>
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyCost')}" />
											</p>
											<p class="flexItem" name="farmCrops.seedQtyCost">
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
												<p class="flexItem" name="farmcrops.harvestDate">
													<s:property value="harvestDate" />

												</p>
											</div>
										</s:if>
										<s:else>
										<s:if test="currentTenantId !='welspun' && currentTenantId!='livelihood'">
											<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" />
												</p>
												<p class="flexItem" name="farmCrops.estimatedYield">
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
											<s:if test="currentTenantId!='ocp'">
												<div class="dynamic-flexItem2 " id="harvestDat">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmcrops.harvestDate')}" />
													<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)</s:if>
												</p>
												<p class="flexItem" name="farmcrops.harvestDate">
													<s:property value="harvestDate" />

												</p>
											</div></s:if></s:if></s:if>
										
</s:else>
                                           
										
									</s:if>

								<s:if test="currentTenantId=='livelihood'">
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('noOfTrees')}" />
												</p>
												<p class="flexItem" name="farmCrops.noOfTrees">
													<s:property value="farmCrops.noOfTrees" />

												</p>
									</div>
								</s:if>
								
								<s:if test="currentTenantId=='wilmar'">
								
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('noOfTrees')}" />
												</p>
												<p class="flexItem" name="farmCrops.noOfTrees">
													<s:property value="farmCrops.noOfTrees" />

												</p>
									</div>
									
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('prodTrees')}" />
												</p>
												<p class="flexItem" name="farmCrops.prodTrees">
													<s:property value="farmCrops.prodTrees" />

												</p>
									</div>
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('affTrees')}" />
												</p>
												<p class="flexItem" name="farmCrops.affTrees">
													<s:property value="farmCrops.affTrees" />

												</p>
									</div>
									
										
									<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('yearOfPlanting')}" />
												</p>
												<p class="flexItem" name="farmCrops.interAcre">
													<s:property value="farmCrops.interAcre" />

												</p>
									</div>
								</s:if>
								</div>
								
								
								<div class="dynamicFarmCrops"></div>
							</div>
	
						</div>
					</div>
					
					
					 <div class="flexRight appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<s:hidden id="farmCropCordinates" value="%{jsonObjectList}" />
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
									
									<tr>
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
									
									
									</tr>
								
							</table>
			
							
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
	</s:form> --%>
						<div class="flexLeft appContentWrapper">
							<div class="formContainerWrapper">
								<div class="aTitle">
									<h2>
										<s:property value="%{getLocaleProperty('info.farmCrops')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
							</div>
							<s:iterator value="farmCrops">
								<div class="flex-view-layout pdff">
									<div class="fullwidth">
										<div class="flexWrapper">
											<div class="flexLeft appContentWrapper">
												<div class="formContainerWrapper">
													<div class="aPanel farmCrops">

														<div class="aContent dynamic-form-con">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farmcrops.farmName')}" />
																</p>
																<p class="flexItem" name="farmcrops.farmName">
																	<s:property value="farm.farmName" />
																</p>
															</div>


															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property value="%{getLocaleProperty('Crop Size')}" />
																</p>
																<p class="flexItem" name="farmCrops.cultiArea">
																	<s:property
																		value="farm.farmDetailedInfo.totalLandHolding" />
																	&nbsp;
																</p>
															</div>


															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farmfarmcrops.cropSeason')}" />
																</p>
																<p class="flexItem" name="farmfarmcrops.cropSeason">
																	<s:property value="cropSeason.name" />
																	&nbsp;
																</p>
															</div>





															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="%{getLocaleProperty('cropName')}" />

																</p>
																<p class="flexItem"
																	name="farmCrops.procurementVariety.procurementProduct.name">
																	<s:property
																		value="procurementVariety.procurementProduct.name" />
																	&nbsp;
																</p>
															</div>
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="variety" />
																</p>
																<p class="flexItem"
																	name="farmCrops.procurementVariety.name">
																	<s:property value="procurementVariety.name" />
																	&nbsp;
																</p>
															</div>

														</div>


														<!-- <div class="dynamicFieldsRender"></div> -->
													</div>












												</div>
											</div>


											<%-- <div class="flexRight appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<s:hidden id="farmCropCordinates" value="%{jsonObjectList}" />
							<table class="table table-bordered aspect-detail fillform">
								<h2>
									<s:text name="info.digitalLocator" />
								</h2>
							
	
<div id="<s:property value="id"/>" class="smallmap map"
								style="height: 200px"></div>
											
							
								

								<tr>
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
									
									
									</tr>

							</table>
							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.latitude" />
								</p>
								<p class="flexItem">
									<s:property value="farmCrops.farm.latitude" />
								</p>
							</div>
							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.longitude" />
								</p>
								<p class="flexItem">
									<s:property value="farm.longitude" />
								</p>
							</div>

							<s:if
								test="currentTenantId=='pratibha' && farmCrops.farmCropsCoordinates.size()>0">
								<div class="flexItem flex-layout flexItemStyle">
									<p class="flexItem">
										<s:text name="%{getLocaleProperty('farmCrops.time')}" />
									</p>
									<p class="flexItem">
										<s:date name="plotCapturingTime" format="dd/MM/yyyy" />
									</p>
								</div>
							</s:if>

						</div>
					</div> --%>


										</div>
									</div>
								</div>
							</s:iterator>
						</div>
						<div class="flexRight appContentWrapper">
							<div class="formContainerWrapper dynamic-form-con">
								<s:hidden id="farmCropCordinates" value="%{jsonObjectList}" />
								<table class="table table-bordered aspect-detail fillform">
									<h2>
										<s:text name="info.digitalLocator" />
									</h2>
									<tbody id="farmMapAccordian" class="panel-collapse in">
										<tr>
											<td style="border-right: solid 1px #d2d695 !important;">

												<div id="map" class="smallmap" style="height: 500px"></div>
											</td>
										</tr>
									</tbody>

								</table>


								<s:if
									test="currentTenantId=='pratibha' && farmCrops.farmCropsCoordinates.size()>0">
									<div class="flexItem flex-layout flexItemStyle">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('farmCrops.time')}" />
										</p>
										<p class="flexItem">
											<s:date name="farmCrops.plotCapturingTime"
												format="dd/MM/yyyy" />
										</p>
									</div>
								</s:if>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="tabs-4" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">

				<div id="baseDiv1">
					<table id="detail4"></table>
					<!-- kishore -->
					<div id="pagerForDetail4"></div>
				</div>
			</div>
		</div>
		<div id="tabs-5" class="tab-pane fade">
			<!-- 	<div class="appContentWrapper marginBottom">
			
				<div id="baseDiv5">
					<table id="detail5"></table>
					kishore
					<div id="pagerForDetail5"></div>
				</div>
	</div> -->
			<!--  <div class="appContentWrapper marginBottom"> -->

			<div class="appContentWrapper marginBottom">

				<div class="formContainerWrapper">
					<div class="aPanel farmCrops">
						<div class="aTitle">
							<h2>
								<s:property
									value="%{getLocaleProperty('Farmer Information')}" />
								<s:property value="infoName" />
							</h2>

						</div>
						<div class="aContent dynamic-form-con">
							<div class="dynamic-flexItem2 farmCrops">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('date')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmerDynamicData.txnDate" />
								</p>
							</div>
							<s:if test="currentTenantId=='wilmar'">
								<div class="dynamic-flexItem2 farmerDiv">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('stateName')}" />
									</p>
									<p class="flexItem">
										<s:property value="selectedState" />
									</p>
								</div>
							</s:if>
							<s:else>
								<div class="dynamic-flexItem2 farmCrops">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('village')}" />
									</p>
									<p class="flexItem">
										<s:property value="selectedVillage" />
									</p>
								</div>
							</s:else>
							<div class="dynamic-flexItem2 farmCrops">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.farmer')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmer" />
								</p>
							</div>
							<div class="dynamic-flexItem2 farmDiv">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.farm')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmList" />
								</p>
							</div>
							<div class="dynamic-flexItem2 farmCropDiv">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('variety')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmCropList.name" />
								</p>
							</div>

							<div class="dynamic-flexItem2 farmCropDiv">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farmCrop')}" />
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

							<div class="dynamic-flexItem2 farmCrops">
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
									<s:property value="%{getLocaleProperty('farmer.address')}" />
								</p>
								<p class="flexItem">
									<s:property value="address" />
								</p>
							</div>


							<div class="dynamic-flexItem2 icsDetailDiv">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.area')}" />
								</p>
								<p class="flexItem">
									<s:property value="area" />
								</p>
							</div>
							<div class="dynamic-flexItem2 areaDetail">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.area')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmAcerage" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.insDate')}" />
								</p>
								<p class="flexItem">
									<s:property value="insDate" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectorName')}" />
								</p>
								<p class="flexItem">
									<s:property value="inspectorName" />
								</p>
							</div>


							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectorMobile')}" />
								</p>
								<p class="flexItem">
									<s:property value="inspectorMobile" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.insType')}" />
								</p>
								<p class="flexItem">
									<s:property value="insType" />
								</p>
							</div>


							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.scope')}" />
								</p>
								<p class="flexItem">
									<s:property value="scope" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.totLand')}" />
								</p>
								<p class="flexItem">
									<s:property value="totLand" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.orgLand')}" />
								</p>
								<p class="flexItem">
									<s:property value="orgLand" />
								</p>
							</div>

							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.totSite')}" />
								</p>
								<p class="flexItem">
									<s:property value="totSite" />
								</p>
							</div>
							<div class="dynamic-flexItem2 inspectClass">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('dynamicCertification.inspectionStatus')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmerDynamicData.inspectionStatus" />
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
								<s:if test="currentTenantId!='wilmar'">
									<div class="dynamic-flexItem2 icsDetailDiv">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('dynamicCertification.correctiveActionPlan')}" />
										</p>
										<p class="flexItem">
											<s:property value="correctiveActionPlan" />
										</p>

									</div>
								</s:if>
							</s:else>

							<div class="dynamic-flexItem2 farmCrops">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farmer.latitude')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmerDynamicData.latitude" />
								</p>
							</div>


							<div class="dynamic-flexItem2 farmCrops">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farmer.longitude')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmerDynamicData.longitude" />
								</p>
							</div>
							<s:if test="farmerDynamicData.isScore==1">
								<div class="dynamic-flexItem2 locationClass">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('Total Percentage')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerDynamicData.totalScore" />
									</p>
								</div>
							</s:if>

							<s:if test="farmerDynamicData.isScore==2">
								<div class="dynamic-flexItem2 locationClass">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('Status')}" />
									</p>
									<p class="flexItem">
										<s:property value="ScoreVal" />

									</p>
								</div>

								<div class="dynamic-flexItem2 locationClass">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('Year')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerDynamicData.conversionStatus" />
									</p>
								</div>

								<div class="dynamic-flexItem2 locationClass">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('Follow Up Date')}" />
									</p>
									<p class="flexItem">
										<s:property value="insDate" />
									</p>
								</div>

								<div class="dynamic-flexItem2 locationClass">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('Follow Up Agent')}" />
									</p>
									<p class="flexItem">

										<s:property value="farmerDynamicData.followUpUser" />
									</p>
								</div>
							</s:if>


							<s:if test="getDigitalSignatureEnabled()==1">
								<div class="dynamic-flexItem2 icsDetailDiv">
									<p class="flexItem">
										<s:if test="entityType==4">
											<s:property value="%{getLocaleProperty('digitalSignature')}" />
										</s:if>
										<s:else>
											<s:property value="%{getLocaleProperty('digitalSignatures')}" />
										</s:else>
									</p>

									<p class="flexItem">
										<s:if
											test='digitalSignatureByteString!=null && digitalSignatureByteString!=""'>
											<img border="0" height="50px"
												src="data:image/png;base64,<s:property value="digitalSignatureByteString"/>">
										</s:if>

									</p>
								</div>
							</s:if>

							<div class="dynamic-flexItem2 icsDetailDiv">
								<p class="flexItem">
									<s:if test="entityType==4">
										<s:property value="%{getLocaleProperty('agentSignature')}" />
									</s:if>
									<s:else>
										<s:property value="%{getLocaleProperty('agentSignatures')}" />
									</s:else>

								</p>
								<p class="flexItem">
									<s:if
										test='agentSignatureByteString!=null && agentSignatureByteString!=""'>
										<img border="0" height="50px"
											src="data:image/png;base64,<s:property value="agentSignatureByteString"/>">
									</s:if>

								</p>
							</div>
							<div class="dynamic-flexItem2 farmCrops">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('Created User')}" />
								</p>
								<p class="flexItem">

									<s:property value="createdUsername" />
								</p>
							</div>

						</div>
					</div>
				</div>
			</div>
			<div class="appContentWrapper marginBottom">
				<div class="dynamicFarmCrops"></div>

			</div>
		</div>

		<div id="tabs-9" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">

				<div id="baseDiv9">
					<table id="detail9"></table>
					<!-- kishore -->
					<div id="pagerForDetail9"></div>
				</div>
			</div>
		</div>

		<!-- <div id="tabs-6" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
			
				<div id="baseDiv1">
					<table id="detail6"></table>
					kishore
					<div id="pagerForDetail6"></div>
				</div>
	</div></div>
		<div id="tabs-7" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
			
				<div id="baseDiv1">
					<table id="detail7"></table>
					kishore
					<div id="pagerForDetail7"></div>
				</div>
	</div></div>
		<div id="tabs-8" class="tab-pane fade ">
			<div class="appContentWrapper marginBottom">
			
				<div id="baseDiv1">
					<table id="detail8"></table>
					kishore
					<div id="pagerForDetail8"></div>
				</div>
	</div></div> -->




		<div id="tabs-10" class="tab-pane fade">
			<div class="flexbox-container">

				<s:form name="form" cssClass="fillform">
					<s:hidden key="currentPage" />
					<s:hidden key="id" />
					<s:hidden key="command" />
					<s:hidden key="farmCrops.id" class='uId' />
					<s:hidden name="farmId" value="%{farmId}" />
					<s:hidden id="estimatedYieldMetricTonnes"
						name="estimatedYieldMetricTonnes"
						value="%{farmCrops.estimatedYield}" />
					<div class="flex-view-layout">
						<div class="fullwidth">
							<div class="flexWrapper">
								<div class="flexLeft appContentWrapper">
									<div class="formContainerWrapper">
										<div class="aPanel farmCrops">
											<div class="aTitle">
												<h2>
													<s:property value="%{getLocaleProperty('Summary')}" />
													<div class="pull-right">
														<a class="aCollapse" href="#"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="%{getLocaleProperty('Commodity Name')}" />

													</p>
													<p class="flexItem"
														name="farmCrops.procurementVariety.procurementProduct.name">
														<s:property
															value="farmCrops.procurementVariety.procurementProduct.name" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('Name of the Farmer')}" />
													</p>
													<p class="flexItem" name="farmer.firstName">
														<s:property value="farmer.firstName" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:if
															test="currentTenantId=='pratibha' && getBranchId()=='organic'">
															<s:property value="%{getLocaleProperty('tracenet')}" />
														</s:if>
														<s:else>
															<s:property value="%{getLocaleProperty('farmerCode')}" />
														</s:else>
														<%-- <s:property
															value="%{getLocaleProperty('farmer.farmerCode')}" /> --%>
													</p>
													<p class="flexItem" name="farmer.farmerCode">
														<s:property value="farmer.farmerCode" />
													</p>
												</div>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="variety" />
													</p>
													<p class="flexItem"
														name="farmCrops.procurementVariety.name">
														<s:property value="farmCrops.procurementVariety.name" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('village.name')}" />
													</p>
													<p class="flexItem" name="selectedVillage">
														<s:property value="farmer.village.code" />
														&nbsp-&nbsp
														<s:property value="farmer.village.name" />
													</p>
												</div>
											</div>
										</div>

									</div>
								</div>


								<div class="flexRight appContentWrapper">
									<div class="formContainerWrapper dynamic-form-con">

										<h2>
											<s:property value="%{getLocaleProperty('farmer.photo')}" />
										</h2>
										<div id="" class="farmer-photo">
											<s:if
												test='farmerImageByteString!=null && farmerImageByteString!=""'>
												<img border="0"
													src="data:image/png;base64,<s:property value="farmerImageByteString"/>">
											</s:if>
											<s:else>
												<img align="middle" border="0" src="img/no-image.png">
											</s:else>
										</div>

										<div class="clear"></div>
										<s:hidden id="farmCordinates" value="%{jsonFarmObjectList}" />
										<table class="table table-bordered aspect-detail fillform">
											<h2>
												<s:text name="info.digitalLocator" />
											</h2>
											<tbody id="farmMapAccordian1" class="panel-collapse in">
												<tr>
													<td style="border-right: solid 1px #d2d695 !important;">

														<div id="map1" class="smallmap" style="height: 500px"></div>
													</td>
												</tr>
											</tbody>

											<tr>
												<div class="flexItem flex-layout flexItemStyle">
													<p class="flexItem">
														<s:text name="farm.latitude" />
													</p>
													<p class="flexItem">
														<s:property value="farm.latitude" />
													</p>
												</div>
												<div class="flexItem flex-layout flexItemStyle">
													<p class="flexItem">
														<s:text name="farm.longitude" />
													</p>
													<p class="flexItem">
														<s:property value="farm.longitude" />
													</p>
												</div>


											</tr>

										</table>

									</div>

								</div>


							</div>
						</div>
					</div>
				</s:form>
			</div>
		</div>
		<div id="tabs-11" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">

				<div id="baseDiv11">
					<table id="detail11"></table>
					<!-- kishore -->
					<div id="pagerForDetail11"></div>
				</div>
			</div>
		</div>


		<div id="tabs-2" class="tab-pane fade">

			<div class="flex-view-layout">
				<div class="fullwidth">
					<div class="flexWrapper">
						<div class="flexLeft appContentWrapper">
							<s:hidden key="farm.id" id="farmId" class="uId" />
							<s:form name="form">
								<s:hidden key="currentPage" />
								<s:hidden key="id" />
								<s:hidden key="farm.id" />
								<s:hidden value="dateOfInspection" />
								<s:hidden key="farm.lockExist" id="locked" />
								<s:hidden key="command" />
								<s:hidden id="landInAcres" name="landInAcres"
									value="%{farm.farmDetailedInfo.totalLandHolding}" />
								<s:hidden id="plantingArea" name="plantingArea"
									value="%{farm.farmDetailedInfo.proposedPlantingArea}" />
							</s:form>
							<div class="error verror">
								<s:actionerror />
								<s:fielderror />
							</div>
							<div class="formContainerWrapper">
								<div class="aPanel farmInfo">
									<div class="aTitle">
										<h2>
											<s:text name="farmdetail" />
											<div class="pull-right">
												<a class="aCollapse" href="#farmDetailsAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con"
										id="farmDetailsAccordian">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.farmName')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmName" />
												&nbsp;
											</p>
										</div>
										<s:if test="currentTenantId=='cofBoard'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farm.photoId')}" />
												</p>
												<p class="flexItem" name="farm.photoId">
													<s:property value="farm.photoId" />
													&nbsp;
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId=='welspun'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('8A/7/12')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.processingActivity">
													<s:property
														value="processingActList[farm.farmDetailedInfo.processingActivity]" />
												</p>
											</div>
										</s:if>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.farmerName" />
											</p>
											<p class="flexItem">
												<s:property value="farm.farmer.firstName" />
												&nbsp;
												<s:property value="farm.farmer.lastName" />
											</p>
										</div>
										<s:if test="currentTenantId!='wilmar'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farm.surveyNo')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.surveyNumber">
													<s:property value="farm.farmDetailedInfo.surveyNumber" />
												</p>
											</div>
										</s:if>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.farmAddress" />
											</p>
											<p class="flexItem" name="farm.farmDetailedInfo.farmAddress">
												<s:property value="farm.farmDetailedInfo.farmAddress" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="%{getLocaleProperty('farm.totalLand')}" />
												(
												<s:property value="%{getAreaType()}" />
												)

											</p>
											<p class="flexItem"
												name="farm.farmDetailedInfo.totalLandHolding">
												<s:property value="farm.farmDetailedInfo.totalLandHolding" />
											</p>
										</div>
										<s:if test="currentTenantId=='fruitmaster'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="%{getLocaleProperty('farm.totalLand.kanal')}" />

												</p>
												<p class="flexItem" name="totalLandKanalName">
													<span id="totalLandKanal"></span>
												</p>
											</div>
										</s:if>


										<div class="dynamic-flexItem2 plantArea pratibhaHide">
											<p class="flexItem">
												<s:text
													name="%{getLocaleProperty('farm.proposedPlantingArea')}" />
												(
												<s:property value="%{getAreaType()}" />
												)
											</p>
											<p class="flexItem"
												name="farm.farmDetailedInfo.proposedPlantingArea">
												<s:property
													value="farm.farmDetailedInfo.proposedPlantingArea" />
											</p>
										</div>



										<s:if
											test="currentTenantId=='welspun' || currentTenantId=='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('ownLand')}" />
												</p>
												<p class="flexItem" name="farm.ownLand">

													<s:property value="farm.ownLand" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('leasedland')}" />
												</p>
												<p class="flexItem" name="farm.leasedLand">

													<s:property value="farm.leasedLand" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('irriLand')}" />
												</p>
												<p class="flexItem" name="farm.irrigationLand">

													<s:property value="farm.irrigationLand" />
												</p>
											</div>
										</s:if>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.landOwned')}" />
											</p>
											<p class="flexItem" name="selectedFarmOwned">
												<s:property value="farmOwnedDetail" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.appRoad" />
											</p>
											<p class="flexItem" name="selectedRoad">
												<s:property value="selectedRoadString" />
											</p>
										</div>
										<s:if test="currentTenantId!='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farm.certYear')}" />
												</p>
												<p class="flexItem" name="farm.certYear">
													<s:property value="farm.certYear" />
												</p>
											</div>
										</s:if>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.farmCode" />
											</p>
											<p class="flexItem" name="farm.farmCode">
												<s:property value="farm.farmCode" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.topo')}" />
											</p>
											<p class="flexItem" name="farm.landTopology">
												<s:if
													test='farm.landTopology!="-1" || farm.landTopology!="null"|| farm.landTopology!=""'>
													<s:property value="farm.landTopology" />
												</s:if>
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.landGradient')}" />
											</p>
											<p class="flexItem" name="selectedGradient">
												<s:property value="landGradientDetail" />
											</p>
										</div>

										<s:if test="currentTenantId=='efk'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.waterSourceList')}" />
												</p>
												<p class="flexItem" name="farm.waterSource">
													<s:property value="selectedWaterSource" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.localNameOfCrotenTree')}" />
												</p>
												<p class="flexItem" name="farm.localNameOfCrotenTree">
													<s:property value="farm.localNameOfCrotenTree" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
												</p>
												<p class="flexItem" name="farm.NoOfCrotenTrees">
													<s:property value="farm.NoOfCrotenTrees" />
												</p>
											</div>

										</s:if>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmRegNo" />
											</p>
											<p class="flexItem" name="farm.farmRegNumber">
												<s:property value="farm.farmRegNumber" />
											</p>
										</div>
										<s:if
											test="currentTenantId!='simfed' && currentTenantId!='wub'">
											<div class="dynamic-flexItem2 waterHarvest">
												<p class="flexItem">
													<s:text name="farm.waterHarvest" />
												</p>
												<p class="flexItem" name="farm.waterHarvest">
													<s:property value="waterHarvests" />
												</p>
											</div>
											<s:if
												test="currentTenantId!='welspun'&& currentTenantId!='wub'">
												<div class="dynamic-flexItem2 avgStore">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('avgStore')}" />
													</p>
													<p class="flexItem" name="farm.avgStore">
														<s:property value="farm.avgStore" />
													</p>
												</div>
												<div class="dynamic-flexItem2 tree">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('treeName')}" />
													</p>
													<p class="flexItem" name="farm.treeName">
														<s:property value="farm.treeName" />
													</p>
												</div>
											</s:if>
										</s:if>
										<s:if test="currentTenantId=='cofBoard'">
											<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
												</p>
												<p class="flexItem" name="farm.distanceProcessingUnit">
													<s:property value="farm.distanceProcessingUnit" />
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId=='wilmar'">
											<div class="dynamic-flexItem2 tree">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
												</p>
												<p class="flexItem" name="farm.distanceProcessingUnit">
													<s:property value="farm.distanceProcessingUnit" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('processingActivity')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.processingActivity">
													<s:property
														value="processingActList[farm.farmDetailedInfo.processingActivity]" />
												</p>
											</div>

											<div class="dynamic-flexItem2 organicStatusDiv">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('organicStatus')}" />
												</p>
												<p class="flexItem" name="farm.farmIcsConv.organicStatus">
													<s:property value="farm.organicStatus" />
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId=='pratibha'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.PlatNo" />
												</p>
												<p class="flexItem" name="farm.farmPlatNo">
													<s:property value="farm.farmPlatNo" />
												</p>
											</div>
										</s:if>


									</div>
								</div>

								<div class="aPanel hide contactInfo">
									<div class="aTitle">
										<h2>
											<s:text name="info.contact" />
											<div class="pull-right">
												<a class="aCollapse" href="#farmLocationInfo"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con" id="farmLocationInfo">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('locality.name')}" />
											</p>
											<p class="flexItem" name="selectedLocality">
												<s:property value="farm.village.city.locality.name" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('city.name')}" />
											</p>
											<p class="flexItem" name="selectedCity">
												<s:property value="farm.village.city.name" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('panchayat.name')}" />
											</p>
											<p class="flexItem" name="selectedPanchayat">
												<s:property value="farm.village.gramPanchayat.name" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('village.name')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.village.name" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('samithi.name')}" />
											</p>
											<p class="flexItem" name="selectedSamithi">
												<s:property value="farm.samithi.name" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('fpoGroup')}" />
											</p>
											<p class="flexItem" name="selectedFpo">
												<s:property value="fpo[farm.fpo]" />
											</p>
										</div>

									</div>
								</div>

								<div class="aPanel hide fieldHistoryInfo">
									<div class="aTitle">
										<h2>

											<s:property value="%{getLocaleProperty('info.conversions')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#fieldInfoAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con" id="fieldInfoAccordian">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.fieldName')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmDetailedInfo.fieldName" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.fieldCrop')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmDetailedInfo.fieldCrop" />
											</p>
										</div>


										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.fieldArea')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmDetailedInfo.fieldArea" />
											</p>
										</div>


										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.inputApplied')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmDetailedInfo.inputApplied" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.quantity')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.farmDetailedInfo.quantityApplied" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property
													value="farm.farmDetailedInfo.lastDateOfChemicalString" />
											</p>
										</div>
									</div>
								</div>
								<s:if
									test="currentTenantId!='wilmar' && currentTenantId!='griffith' ">
									<div class="aPanel soilIrrigationInfo">
										<div class="aTitle">
											<h2>
												<s:text name="info.soil" />
												<div class="pull-right">
													<a class="aCollapse" href="#farmSoilAccordian"><i
														class="fa fa-chevron-right"></i></a>
												</div>
											</h2>
										</div>
										<div class="aContent dynamic-form-con" id="farmSoilAccordian">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.soilType" />
												</p>
												<p class="flexItem" name="selectedSoilType">
													<s:property value="soilTypeDetail" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.soilTexture')}" />
												</p>
												<p class="flexItem" name="selectedTexture">
													<s:property value="soilTextureDetail" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.soilFertilityStatus" />
												</p>
												<p class="flexItem" name="selectedSoilFertility">
													<s:property value="soilFertilityDetail" />
												</p>
											</div>


											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.farmIrrigationSource" />
												</p>
												<p class="flexItem" name="selectedIrrigation">
													<s:property value="farmIrrigationDetail" />
												</p>
											</div>
											<s:if test="isIrrigation==1">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farm.farmIrrigationType" />
													</p>
													<p class="flexItem" name="selectedIrrigationSource">
														<s:property value="irrigationSourceDetail" />
													</p>
												</div>
											</s:if>
											<s:if test="farm.farmDetailedInfo.irrigationSource==99">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farm.irrigatedOther" />
													</p>
													<p class="flexItem" name="selectedIrrigationSource">
														<s:property value="farm.farmDetailedInfo.irrigatedOther" />
													</p>
												</div>
											</s:if>
											<s:if test="currentTenantId=='welspun'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
													</p>
													<p class="flexItem" name="farm.NoOfCrotenTrees">
														<s:property value="farm.NoOfCrotenTrees" />
													</p>
												</div>

												<div class="dynamic-flexItem2 tree">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('treeName')}" />
													</p>
													<p class="flexItem" name="farm.treeName">
														<s:property value="farm.treeName" />
													</p>
												</div>
												<div class="dynamic-flexItem2 tree">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
													</p>
													<p class="flexItem" name="farm.distanceProcessingUnit">
														<s:property value="farm.distanceProcessingUnit" />
													</p>
												</div>
												<div class="dynamic-flexItem2 avgStore">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('avgStore')}" />
													</p>
													<p class="flexItem" name="farm.avgStore">
														<s:property value="farm.avgStore" />
													</p>
												</div>
											</s:if>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.methodOfIrrigation" />
												</p>
												<p class="flexItem" name="selectedMethodOfIrrigation">
													<s:property value="methodOfIrrigationDetail" />
												</p>
											</div>
											<s:if test="currentTenantId!='welspun'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.boreWellRechargeStructure')}" />
													</p>
													<p class="flexItem"
														name="farm.farmDetailedInfo.boreWellRechargeStructure">
														<s:property
															value="borewellList[farm.farmDetailedInfo.boreWellRechargeStructure]" />
													</p>
												</div>
											</s:if>
											<s:if test="currentTenantId=='cofBoard'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('presenceOfBananaTrees')}" />
													</p>
													<p class="flexItem" name="farm.presenceBananaTrees">
														<s:property value="presenceOfBanana" />
													</p>
												</div>
											</s:if>
										</div>
									</div>
								</s:if>

								<div class="aPanel landMarkInfo">
									<div class="aTitle">
										<h2>
											<s:text name="farm.landmark" />
											<div class="pull-right">
												<a class="aCollapse" href="#farmLandMarkAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con" id="labourAccordian">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.landmark" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farm.landmark" />
											</p>
										</div>
									</div>
								</div>


								<s:if test="enableSoliTesting==1">
									<div class="aPanel">
										<div class="aTitle">
											<h2>
												<s:text name="info.soilTesting" />
												<div class="pull-right">
													<a class="aCollapse" href="#soilTestAcc"><i
														class="fa fa-chevron-right"></i></a>
												</div>
											</h2>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="soliTesting" />
											</p>
											<p class="flexItem">
												<s:if test='farm.soilTesting=="1" || farm.soilTesting=="2" '>
													<s:text name='%{"SOILTEST"+farm.soilTesting}' />
												</s:if>
												<s:else>
													<s:property value="farm.soilTesting" />
												</s:else>
											</p>
										</div>
										<s:if test='farm.soilTesting=="1"'>
											<s:if test="farm.docUpload.size()>0">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="fileName" />
													</p>
													<s:iterator value="farm.docUpload">
														<p class="flexItem" name="name">
															<s:property value="name" />
														</p>
													</s:iterator>
												</div>

												<div>
													<p class="flexItem">
														<s:text name="download" />
													</p>
													<s:iterator value="farm.docUpload">
														<button type="button" class="btn btn-default"
															aria-label="Left Align"
															onclick='downloadDocument(<s:property value="id" />)'>
															<span class="glyphicon glyphicon-download-alt"
																aria-hidden="true"></span>
														</button>
													</s:iterator>
												</div>
											</s:if>
										</s:if>

									</div>
								</s:if>



							</div>
							<div class="dynamicFarms"></div>
							<div class="flexItem flex-layout flexItemStyle">
								<div class="">
									<sec:authorize ifAllGranted="profile.farmer.update">

										<span id="update" class=""><span class="first-child">
												<button type="button" class="edit-btn btn btn-success"
													onclick="onUpdate()">
													<FONT color="#FFFFFF"> <b><s:text
																name="edit.button" /></b>
													</font>
												</button>
										</span></span>


									</sec:authorize>
									<sec:authorize ifAllGranted="profile.farmer.delete">

										<span id="delete" class=""><span class="first-child">
												<button type="button" onclick="onDelete()"
													class="delete-btn btn btn-warning">
													<FONT color="#FFFFFF"> <b><s:text
																name="delete.button" /></b>
													</font>
												</button>
										</span></span>

									</sec:authorize>
									<span id="cancel" class=""><span class="first-child">
											<button type="button" class="back-btn btn btn-sts"
												onclick="onCancel()">
												<b><FONT color="#FFFFFF"><s:text
															name="back.button" /> </font></b>
											</button>
									</span></span>
								</div>
							</div>

						</div>
						<div class="flexRight appContentWrapper">
							<div class="formContainerWrapper dynamic-form-con">
								<h2>
									<s:property value="%{getLocaleProperty('farm.photo')}" />
								</h2>
								<div id="" class="farmer-photo ">
									<s:if
										test='farmImageByteString!=null && farmImageByteString!=""'>
										<img border="0"
											src="data:image/png;base64,<s:property value="farmImageByteString"/>">
									</s:if>
									<s:else>
										<img align="middle" border="0" src="img/no-image.png">
									</s:else>
								</div>
								<div class="clear"></div>
								<%-- <s:hidden id="farmCordinates" value="%{jsonObjectList}" />
					<h2>
						<s:text name="info.digitalLocator" />
					</h2>
					<div id="map" class="smallmap map" style="height: 500px"></div>
					<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
						<p class="flexItem">
							<s:property value="farm.latitude" />
						</p>
					</div>
					<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
						<p class="flexItem">
							<s:property value="farm.longitude" />
						</p>
					</div>
					
					<s:if test="currentTenantId=='pratibha' && farm.coordinates.size()>0" >
					
					<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.time" />
						</p>
						<p class="flexItem">
							<s:date name="farm.plotCapturingTime" format="dd/MM/yyyy" />
						</p>
					</div>
					
					</s:if> --%>
							</div>

						</div>

					</div>
				</div>

			</div>

		</div>

		<div id="tabs-1" class="tab-pane fade in active">
			<div class="appContentWrapper marginBottom">

				<s:form name="form">
					<s:hidden key="currentPage" />
					<s:hidden key="farmer.id" id="farmerId" class='uId' />
					<s:hidden key="command" />
					<s:hidden key="farmerAndContractStatus" />
					<div class="flex-view-layout">
						<div class="fullwidth">
							<div class="flexWrapper">
								<div class="flexLeft appContentWrapper">
									<div class="error">
										<s:actionerror />
										<s:fielderror />
									</div>
									<div class="formContainerWrapper">

										<div class="aPanel farmer_info">
											<div class="aTitle">
												<h2>
													<s:property value="%{getLocaleProperty('info.farmer')}" />
													<div class="pull-right">
														<a class="aCollapse" href="#"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con">
												<s:if test='branchId==null'>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="app.branch" />
														</p>
														<p class="flexItem">
															<s:property value="%{getBranchName(farmer.branchId)}" />
															&nbsp;
														</p>
													</div>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('Created User Name/Mobile User Name')}" />
													</p>
													<p class="flexItem" name="farmer.createdUsername">
														<s:property value="farmer.createdUsername" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.enrollmentPlace" />
													</p>
													<p class="flexItem" name="farmer.enrollmentPlace">
														<s:property value="enrollmentMap[farmer.enrollmentPlace]" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.formFilledBy')}" />
													</p>
													<p class="flexItem" name="farmer.formFilledBy">
														<s:property value="farmer.formFilledBy" />
														&nbsp;
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.assess')}" />
													</p>
													<p class="flexItem" name="farmer.assess">
														<s:property value="farmer.assess" />
														&nbsp;
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.placeOfAsss')}" />
													</p>
													<p class="flexItem" name="farmer.placeOfAsss">
														<s:property value="farmer.placeOfAsss" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.objective')}" />
													</p>
													<p class="flexItem" name="farmer.objective">
														<s:property value="farmer.objective" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.farmerId')}" />
													</p>
													<p class="flexItem" name="farmer.farmerId">
														<s:property value="farmer.farmerId" />
														&nbsp;
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:if
															test="currentTenantId=='pratibha' && getBranchId()=='organic'">
															<s:property value="%{getLocaleProperty('tracenet')}" />
														</s:if>
														<s:else>
															<s:property value="%{getLocaleProperty('farmerCode')}" />
														</s:else>
														<%-- <s:property
															value="%{getLocaleProperty('farmer.farmerCode')}" /> --%>
													</p>
													<p class="flexItem" name="farmer.farmerCode">
														<s:property value="farmer.farmerCode" />
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem" name="dateOfJoining">
														<s:text name="%{getLocaleProperty('farmer.dateOfJoin')}" />
													</p>
													<p class="flexItem">
														<s:property value="dateOfJoining" />
													</p>
												</div>

												<div class="dynamic-flexItem2 certified">
													<p class="flexItem">
														<s:text name="farmer.isCertified" />
													</p>
													<p class="flexItem" name="farmer.isCertifiedFarmer">
														<s:property
															value="isFarmerCertified[farmer.isCertifiedFarmer]" />
													</p>
												</div>
												<s:if test="currentTenantId!='livelihood'">
													<s:if test="farmer.isCertifiedFarmer==1">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="certification.type" />
															</p>
															<p class="flexItem" name="farmer.certificationType">
																<s:property
																	value="certificationTypes[farmer.certificationType]" />
															</p>
														</div>

														<div class="dynamic-flexItem2 icsFieldSelect">
															<p class="flexItem">
																<s:text name="farmer.icsName" />
															</p>
															<p class="flexItem" name="farmer.icsName">
																<s:property value="farmer.icsName" />

															</p>
														</div>

														<div class="dynamic-flexItem2 icsFieldTxt">
															<p class="flexItem">
																<s:text name="farmer.icsName" />
															</p>
															<p class="flexItem" name="farmer.icsName">
																<s:property value="icsName" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="%{getLocaleProperty('farmer.icsCode')}" />
															</p>
															<p class="flexItem" name="farmer.icsCode">
																<s:property value="icsCode" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.icsUnitNumber" />
															</p>
															<p class="flexItem" name="farmer.icsUnitNo">
																<s:property value="icsUnit[farmer.icsUnitNo]" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.regNo" />
															</p>
															<p class="flexItem" name="farmer.icsTracenetRegNo">
																<s:property value="icsRegNo[farmer.icsTracenetRegNo]" />
															</p>
														</div>


														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text
																	name="%{getLocaleProperty('certification.level')}" />
															</p>
															<p class="flexItem"
																name="farmer.certificateStandardLevel">
																<s:text
																	name="certificate%{farmer.certificateStandardLevel}" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.codeTracenet" />
														</p>
														<p class="flexItem" name="farmer.farmersCodeTracenet">
															<s:property value="farmer.farmersCodeTracenet" />
														</p>
													</div>


													<s:if test="currentTenantId=='olivado'">

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="Created User Name/Mobile User Name" />
															</p>
															<p class="flexItem">
																<s:property value="farmer.createdUsername" />
															</p>
														</div>
														<div class="dynamic-flexItem2 ggn">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.codeIcs')}" />
															</p>
															<p class="flexItem" name="farmer.farmerCodeByIcs">
																<s:property value="farmer.farmerCodeByIcs" />
															</p>
														</div>

														<div class="dynamic-flexItem2 ggnRegNo">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.codeTracenet')}" />
															</p>
															<p class="flexItem" name="farmer.farmersCodeTracenet">
																<s:property value="farmer.farmersCodeTracenet" />
															</p>
														</div>

													</s:if>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.beneficiary" />
													</p>
													<p class="flexItem" name="farmer.isBeneficiaryInGovScheme">
														<s:if test="farmer.isBeneficiaryInGovScheme==1">
															<s:text name="Yes" />
														</s:if>
														<s:elseif test='farmer.isBeneficiaryInGovScheme=="2"'>
															<s:text name="No" />
														</s:elseif>
														<s:else>
															<s:text name="" />
														</s:else>
													</p>

												</div>

												<div class="dynamic-flexItem2">
													<s:if test="farmer.isBeneficiaryInGovScheme==1">
														<p class="flexItem">
															<s:text name="farmer.nameOfScheme" />
														</p>
														<p class="flexItem" name="farmer.nameOfTheScheme">

															<s:property value="farmer.nameOfTheScheme" />

														</p>
													</s:if>
												</div>


												<div class="dynamic-flexItem2">
													<s:if test="farmer.isBeneficiaryInGovScheme==1">
														<p class="flexItem">
															<s:text name="farmer.governmentDepartment" />
														</p>
														<p class="flexItem" name="farmer.govtDept">

															<s:property value="farmer.govtDept" />

														</p>
													</s:if>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.regYear')}" />
													</p>
													<p class="flexItem" name="farmer.yearOfICS">
														<s:if test='farmer.yearOfICS!="-1"'>
															<s:property value="farmer.yearOfICS" />
														</s:if>
													</p>
												</div>

											</div>
										</div>


										<div class="aPanel pers_info">
											<div class="aTitle">
												<h2>
													<s:text name="info.personal" />
													<div class="pull-right">
														<a class="aCollapse" href="#"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>

											<div class="aContent dynamic-form-con">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.firstName')}" />
													</p>
													<p class="flexItem" name="farmer.firstName">
														<s:property value="farmer.firstName" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.lastName')}" />
													</p>
													<p class="flexItem" name="farmer.lastName">
														<s:property value="farmer.lastName" />
													</p>
												</div>
												<s:if test="currentTenantId!='olivado'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.surName')}" />
														</p>
														<p class="flexItem" name="farmer.surName">
															<s:property value="farmer.surName" />
														</p>
													</div>
												</s:if>
												<div class="dynamic-flexItem2 hideDob">
													<p class="flexItem">
														<s:text name="farmer.dateOfBirth" />
													</p>
													<p class="flexItem" name="calendar">
														<s:property value="dateOfBirth" />
													</p>
												</div>

												<s:if test="currentTenantId!='olivado'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('farmer.age1')}" />
														</p>
														<p class="flexItem" name="age">
															<s:property value="farmer.age" />
														</p>
													</div>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.gender" />
													</p>
													<p class="flexItem" name="farmer.gender">
														<s:text name='%{farmer.gender}' />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="chetnaCategory" />
													</p>
													<p class="flexItem" name="farmer.category">
														<s:property value="farmer.category" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('castename')}" />
													</p>
													<p class="flexItem" name="farmer.casteName">
														<s:property value="farmer.casteName" />
													</p>
												</div>

												<s:if
													test="currentTenantId!='symrise' && currentTenantId!='griffith'">
													<div class="dynamic-flexItem2">
														<p class="flexItem" name="farmer.proofNo">
															<s:property
																value="%{getLocaleProperty('farmer.ProofNo')}" />
														</p>
														<p class="flexItem" name="farmer.idProof">
															<s:property value="farmer.proofNo" />
														</p>
													</div>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="education" />
													</p>
													<p class="flexItem" name="farmer.education">
														<s:property value="educationList[farmer.education]" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="marital.Status" />
													</p>
													<p class="flexItem" name="farmer.maritalSatus">
														<s:property value="farmer.maritalSatus" />
													</p>
												</div>
												<s:if test='idProofEnabled==1'>

													<s:if test="currentTenantId !='efk'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.idProof" />
															</p>
															<p class="flexItem" name="farmer.idProof">
																<s:property value="proofList[farmer.idProof]" />
															</p>

														</div>
													</s:if>
													<s:if test="farmer.idProof=='99'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.otherProof" />
															</p>
															<p class="flexItem" name="farmer.idProof">
																<s:property value="farmer.otherIdProof" />
															</p>
														</div>
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.ProofNo')}" />
															</p>
															<p class="flexItem" name="farmer.idProof">
																<s:property value="farmer.proofNo" />
															</p>

														</div>
													</s:if>
													<s:else>
														<div class="dynamic-flexItem2">
															<p class="flexItem" name="farmer.proofNo">
																<s:property
																	value="%{getLocaleProperty('farmer.ProofNo')}" />
															</p>
															<p class="flexItem" name="farmer.idProof">
																<s:property value="farmer.proofNo" />
															</p>
														</div>
													</s:else>
													<s:if test='idProofEnabled==1 && IdImgAvil=="1"'>
														<div class="dynamic-flexItem2">
															<p class="flexItem" name="farmer.idProofImg">
																<s:property
																	value="%{getLocaleProperty('farmer.idProofImg')}" />
															</p>
															<p class="flexItem" name="farmer.idProof">
																<button type='button'
																	class='btn btn-sm pull-right photo'
																	style='margin-right: 15%'
																	onclick="enableFarmerPhotoModal(<s:property value="farmer.id"/>)">
																	<i class='fa fa-picture-o' aria-hidden='true'></i>
																</button>
															</p>
														</div>
													</s:if>
												</s:if>

												<s:if test="currentTenantId!='sagi'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="%{getLocaleProperty('farmer.adhaarNo')}" />
														</p>
														<p class="flexItem" name="farmer.adhaarNo">
															<s:property value="farmer.adhaarNo" />
														</p>
													</div>
												</s:if>

												<s:if test="currentTenantId=='atma'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.socialCategory" />
														</p>
														<p class="flexItem" name="farmer.socialCategory">
															<s:property
																value="socialCategoryList[farmer.socialCategory]" />
														</p>
													</div>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.religion" />
													</p>
													<s:if test="farmer.religion=='99'">
														<p class="flexItem" name="farmer.religion">
															<s:property value="religionList[farmer.religion]" />
														</p>
													</s:if>
													<s:else>
														<p class="flexItem" name="farmer.religion">
															<s:property value="religionList[farmer.religion]" />
														</p>
													</s:else>
												</div>
												<s:if test="currentTenantId!='sagi'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.typeOfFamily" />
														</p>
														<p class="flexItem" name="farmer.typeOfFamily">
															<s:property value="familyTypeList[farmer.typeOfFamily]" />
														</p>
													</div>
													<s:if test="farmer.sangham=='01'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="houseHoldLandWet" />
															</p>
															<p class="flexItem" name="farmer.houseHoldLandWet">
																<s:property value="farmer.houseHoldLandWet" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="houseHoldLandDry" />
															</p>
															<p class="flexItem" name="farmer.houseHoldLandDry">
																<s:property value="farmer.houseHoldLandDry" />
															</p>
														</div>
													</s:if>
												</s:if>

												<div class="dynamic-flexItem2 prefWrk">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.preferenceWork')}" />
													</p>
													<p class="flexItem" name="farmer.prefWrk">
														<s:property value="farmer.prefWrk" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('status')}" />
													</p>
													<p class="flexItem" name="farmer.personalStatus">
														<s:property value="statusList[farmer.personalStatus]" />
													</p>
												</div>

											</div>
										</div>

										<div class="aPanel contact_info">
											<div class="aTitle">
												<h2>
													<s:text name="info.contact" />
													<div class="pull-right">
														<a class="aCollapse" href="#"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.address')}" />
													</p>
													<p class="flexItem" name="farmer.address">
														<s:property value="farmer.address" />
													</p>
												</div>
												<s:if test="currentTenantId=='olivado'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.surName')}" />
														</p>
														<p class="flexItem" name="farmer.surName">
															<s:property value="farmer.surName" />
														</p>
													</div>
												</s:if>
												<s:if
													test="currentTenantId=='welspun'|| currentTenantId=='simfed' ">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.pinCode')}" />
														</p>
														<p class="flexItem" name="farmer.pinCode">
															<s:property value="farmer.pinCode" />
														</p>
													</div>
												</s:if>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.mobileNumber')}" />
													</p>
													<p class="flexItem" name="farmer.mobileNumber">
														<s:property value="farmer.mobileNumber" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.email" />
													</p>
													<p class="flexItem" name="farmer.email">
														<s:property value="farmer.email" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="country.name" />
													</p>
													<p class="flexItem" name="selectedCountry">
														<s:property
															value="farmer.village.city.locality.state.country.code" />
														&nbsp-&nbsp
														<s:property
															value="farmer.village.city.locality.state.country.name" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('state.name')}" />
													</p>
													<p class="flexItem" name="selectedState">
														<s:property
															value="farmer.village.city.locality.state.code" />
														&nbsp-&nbsp
														<s:property
															value="farmer.village.city.locality.state.name" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('locality.name')}" />
													</p>
													<p class="flexItem" name="selectedLocality">
														<s:property value="farmer.village.city.locality.code" />
														&nbsp-&nbsp
														<s:property value="farmer.village.city.locality.name" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('city.name')}" />
													</p>
													<p class="flexItem" name="selectedCity">
														<s:property value="farmer.village.city.code" />
														&nbsp-&nbsp
														<s:property value="farmer.village.city.name" />
													</p>
												</div>

												<s:if test="gramPanchayatEnable==1">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('panchayat.name')}" />
														</p>
														<p class="flexItem" name="selectedPanchayat">
															<s:property value="farmer.village.gramPanchayat.code" />
															&nbsp-&nbsp
															<s:property value="farmer.village.gramPanchayat.name" />
														</p>
													</div>
												</s:if>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('village.name')}" />
													</p>
													<p class="flexItem" name="selectedVillage">
														<s:property value="farmer.village.code" />
														&nbsp-&nbsp
														<s:property value="farmer.village.name" />
													</p>
												</div>

												<s:if test="currentTenantId=='atma'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('profile.sangham')}" />
														</p>
														<p class="flexItem" name="selectedSangham">
															<s:property value="farmer.sangham" />
														</p>
													</div>
												</s:if>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('profile.samithi')}" />
													</p>
													<p class="flexItem" name="selectedSamithi">
														<s:property value="farmer.samithi.name" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('profile.groupPosition')}" />
													</p>
													<p class="flexItem" name="selectedGroupPosition">
														<s:property value="farmer.positionGroup" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('fpoGroup')}" />
													</p>
													<p class="flexItem" name="farmer.fpo">
														<s:property value="fpo[farmer.fpo]" />
													</p>
												</div>



												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.phoneNumber')}" />
													</p>
													<p class="flexItem" name="farmer.phoneNumber">
														<s:property value="farmer.phoneNumber" />
													</p>
												</div>
												<s:if test="currentTenantId=='wilmar'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('selectedMasterData')}" />
														</p>
														<p class="flexItem" name="farmer.masterData">
															<s:property value="farmer.masterData" />
														</p>
													</div>
												</s:if>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.status" />
													</p>
													<p class="flexItem" name="farmer.status">
														<s:text name='%{"status"+farmer.status}' />
													</p>
												</div>
											</div>
										</div>

										<div class="aPanel family_Info">
											<div class="aTitle">
												<h2>
													<s:text name="info.family" />
													<div class="pull-right">
														<a class="aCollapse" href="#familyInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con" id="familyInfo">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.noOfFamilyMembers.prop')}" />
													</p>
													<p class="flexItem" name="farmer.noOfFamilyMembers">
														<s:property value="farmer.noOfFamilyMembers" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.adultMale" />
													</p>
													<p class="flexItem" name="farmer.adultCountMale">
														<s:property value="farmer.adultCountMale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.adultFemale" />
													</p>
													<p class="flexItem" name="farmer.adultCountMale">
														<s:property value="farmer.adultCountFemale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childMale" />
													</p>
													<p class="flexItem" name="farmer.childCountMale">
														<s:property value="farmer.childCountMale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childFemale" />
													</p>
													<p class="flexItem" name="farmer.childCountMale">
														<s:property value="farmer.childCountFemale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childCountSchollMale" />
													</p>
													<p class="flexItem" name="farmer.noOfSchoolChildMale">
														<s:property value="farmer.noOfSchoolChildMale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childCountSchollFemale" />
													</p>
													<p class="flexItem" name="farmer.noOfSchoolChildMale">
														<s:property value="farmer.noOfSchoolChildFemale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.houseHoldCnt')}" />
													</p>
													<p class="flexItem" name="farmer.noOfHouseHoldMem">
														<s:property value="farmer.noOfHouseHoldMem" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.headOfFamily" />
													</p>
													<p class="flexItem" name="farmer.headOfFamily">
														<s:property value="headOfFamilyList[farmer.headOfFamily]" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.totalHs')}" />
													</p>
													<p class="flexItem" name="farmer.totalHsldLabel">
														<s:property value="farmer.totalHsldLabel" />
													</p>
												</div>

												<s:if test="currentTenantId =='griffith'">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.drinkingWSProp')}" />
														</p>
														<p class="flexItem" name="farmer.drinkingWSProp">
															<s:property
																value="farmer.farmerEconomy.drinkingWaterSource" />
														</p>
													</div>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.isLoanTakenLastYearProp')}" />
														</p>
														<p class="flexItem" name="farmer.isLoanTakenLastYearProp">
															<s:if test="farmer.loanTakenLastYear==1">
																<s:text name="yes" />
															</s:if>
															<s:else>
																<s:text name="no" />
															</s:else>
													</div>
												</s:if>
											</div>
										</div>



										<div class="aPanel insuranceInfoEnabled">
											<div class="aTitle">
												<h2>
													<s:text name="info.insurance" />
													<div class="pull-right">
														<a class="aCollapse" href="#insuranceInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con" id="insuranceInfo">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.life')}" />
													</p>
													<p class="flexItem" name="farmer.lifeInsurance">
														<s:property value="farmer.lifeInsurance" />
													</p>
												</div>

												<s:if test="farmer.lifeInsurance=='Yes'">
													<div class="dynamic-flexItem2">
														<p class="flexItem ">
															<s:text name="farmer.lifeAmt" />
														</p>
														<p class="flexItem" name="farmer.lifeInsAmount">
															<s:property value="farmer.lifeInsAmount" />
														</p>
													</div>
												</s:if>
												<div class="dynamic-flexItem2">
													<p class="flexItem ">
														<s:property value="%{getLocaleProperty('farmer.health')}" />
													</p>
													<p class="flexItem" name="farmer.healthInsurance">
														<s:text name="%{farmer.healthInsurance}" />
													</p>
												</div>

												<s:if test="currentTenantId =='efk'">
													<div class="dynamic-flexItem2">
														<p class="flexItem ">
															<s:text name="farmer.healthAmt" />
														</p>
														<p class="flexItem" name="farmer.healthInsAmount">
															<s:property value="farmer.healthInsAmount" />
														</p>
													</div>

												</s:if>
												<s:else>
													<s:if test="farmer.healthInsurance=='Yes'">
														<div class="dynamic-flexItem2">
															<p class="flexItem ">
																<s:text name="farmer.healthAmt" />
															</p>
															<p class="flexItem" name="farmer.healthInsAmount">
																<s:property value="farmer.healthInsAmount" />
															</p>
														</div>
													</s:if>
												</s:else>

												<div class="dynamic-flexItem2">
													<p class="flexItem ">
														<s:text name="farmer.cropInsuranceProp" />
													</p>
													<p class="flexItem" name="farmer.isCropInsured">
														<s:if test='farmer.isCropInsured=="1"'>
															<s:text name="Yes" />
														</s:if>
														<s:elseif test='farmer.isCropInsured=="2"'>
															<s:text name="No" />
														</s:elseif>
														<s:else>
															<s:text name="" />
														</s:else>
													</p>
												</div>

												<s:if test='farmer.isCropInsured=="1"'>
													<div class="dynamic-flexItem2">
														<p class="flexItem ">
															<s:text name="farmer.cropName" />
														</p>
														<p class="flexItem" name="farmer.farmerCropInsurance">
															<s:property value="farmer.farmerCropInsurance" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem ">
															<s:text name="%{getLocaleProperty('farmer.acresCroped')}" />
														</p>
														<p class="flexItem" name="farmer.acresInsured">
															<s:property value="farmer.acresInsured" />
														</p>
													</div>
												</s:if>
											</div>
										</div>


										<div class="dynamicFarmers"></div>



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
										<h2>
											<s:property value="%{getLocaleProperty('farmer.photo')}" />
										</h2>
										<div id="" class="farmer-photo">
											<s:if
												test='farmerImageByteString!=null && farmerImageByteString!=""'>
												<img border="0"
													src="data:image/png;base64,<s:property value="farmerImageByteString"/>">
											</s:if>
											<s:else>
												<img align="middle" border="0" src="img/no-image.png">
											</s:else>
										</div>
									<%-- 	<div>
											<div class="flexItem flex-layout flexItemStyle">
												<b><s:text name="farmer.latitude" />: &nbsp;</b>
												<s:property value="farmer.latitude" />
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><s:text
														name="farmer.longitude" />: &nbsp;</b>
												<s:property value="farmer.longitude" />
											</div>
										</div> --%>
										<div id="qrcode"></div>
										<div class="clear"></div>
									</div>
									<s:if test="getFingerPrintEnabled()==1">
										<div class="formContainerWrapper dynamic-form-con">
											<h2>
												<s:text name="fingerPrint" />
											</h2>
											<div id="" class="farmer-photo">
												<s:if
													test='fingerPrintImageByteString!=null && fingerPrintImageByteString!=""'>
													<img border="0"
														src="data:image/png;base64,<s:property value="fingerPrintImageByteString"/>">
												</s:if>
												<s:else>
													<img align="middle" border="0" src="img/no-image.png">
												</s:else>
											</div>
											<div class="clear"></div>
										</div>
									</s:if>
									<div class="formContainerWrapper dynamic-form-con ">
										<s:if test="farmer.isCertifiedFarmer==1">
											<div class="aPanel assets_info" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:text name="info.assetOwnership" />
														<div class="pull-right">
															<a class="aCollapse" href="#assetInfo"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div>

												<div class="aContent dynamic-form-con" id="assetInfo">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.consumerElectronicsProp" />
														</p>
														<p class="flexItem" name="farmer.consumerElectronics">
															<s:property value="farmer.consumerElectronics" />
														</p>
													</div>
													<div class="dynamic-flexItem2 vehicleProp">
														<p class="flexItem">
															<s:text name="farmer.vehicleProp" />
														</p>
														<p class="flexItem" name="farmer.vehicle">
															<s:property value="farmer.vehicle" />
														</p>
													</div>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.cellPhoneProp" />
														</p>
														<p class="flexItem" name="farmer.cellPhone">
															<s:if test="farmer.cellPhone==1">
																<s:text name="Yes" />
															</s:if>
															<s:elseif test="farmer.cellPhone==2">
																<s:text name="No" />
															</s:elseif>
														</p>
													</div>

													<s:if test="farmer.cellPhone==1">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.smartPhones" />
															</p>
															<p class="flexItem" name="farmer.smartPhone">
																<s:if test="farmer.smartPhone==1">
																	<s:text name="Yes" />
																</s:if>
																<s:elseif test="farmer.smartPhone==2">
																	<s:text name="No" />
																</s:elseif>
															</p>

														</div>
													</s:if>

												</div>
												<div class="dynamic-flexItem2 agricultureImplements">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.agricultureImplements')}" />
													</p>
													<p class="flexItem" name="farmer.agricultureImplements">
														<s:property value="farmer.agricultureImplements" />
													</p>
												</div>

											</div>

											<div class="aPanel dwelling_info" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:text name="info.dwelingPlace" />
														<div class="pull-right">
															<a class="aCollapse" href="#dweelingInfo"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div>

												<div class="aContent dynamic-form-con" id="dweelingInfo">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.housingOwnershipProp" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.housingOwnership">
															<s:if test="farmer.farmerEconomy.housingOwnership!=99">
																<td style="font-weight: bold;"><s:iterator
																		value="housingOwnershipMap" status="rowstatus"
																		var="element">
																		<s:if
																			test="#rowstatus.count == farmer.farmerEconomy.housingOwnership">
																			<s:property value="value" />
																		</s:if>
																	</s:iterator></td>
															</s:if>

															<s:else>
																<td style="font-weight: bold;"><s:property
																		value="farmer.farmerEconomy.housingOwnershipOther" /></td>

															</s:else>
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.electrifiedHouseDetailProp" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.electrifiedHouse">
															<s:iterator value="electrifiedHouseMap"
																status="rowstatus">
																<s:if
																	test="#rowstatus.count == farmer.farmerEconomy.electrifiedHouse">
																	<s:property value="value" />
																</s:if>
															</s:iterator>
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.housingTypeProp')}" />
														</p>

														<s:if test="farmer.farmerEconomy.housingType==99">

															<p class="flexItem" style="font-weight: bold;">
																<s:property
																	value="farmer.farmerEconomy.otherHousingType" />
															</p>
														</s:if>
														<s:else>

															<p class="flexItem"
																name="farmer.farmerEconomy.housingType">
																<s:property
																	value="housingTypeList[farmer.farmerEconomy.housingType]" />
															</p>
														</s:else>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.drinkingWSProp" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.drinkingWaterSource">
															<s:property
																value="farmer.farmerEconomy.drinkingWaterSource" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.cookingFuel" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.cookingFuel">
															<s:property value="farmer.farmerEconomy.cookingFuel" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.qtyPerMonth" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.qtyCookingPerMonth">
															<s:property
																value="farmer.farmerEconomy.qtyCookingPerMonth" />
														</p>
													</div>


													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.costPerMonth" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.costCookingPerMonth">
															<s:property
																value="farmer.farmerEconomy.costCookingPerMonth" />
														</p>
													</div>


													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.toiletAvailable" />
														</p>
														<p class="flexItem"
															name="farmer.farmerEconomy.toiletAvailable">
															<s:if test='farmer.farmerEconomy.toiletAvailable!=null'>
																<s:text
																	name='%{"ita-"+farmer.farmerEconomy.toiletAvailable}' />
															</s:if>
															<s:else>
																<s:text name='no' />
															</s:else>
														</p>
													</div>

													<s:if test="farmer.farmerEconomy.toiletAvailable==1">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.ifToiletAvailable" />
															</p>
															<p class="flexItem"
																name="farmer.farmerEconomy.toiletAvailable">
																<s:iterator value="toiletAvailableFromMap"
																	status="rowstatus">
																	<s:if
																		test="#rowstatus.count == farmer.farmerEconomy.ifToiletAvailable">
																		<s:property value="value" />
																	</s:if>
																</s:iterator>
															</p>
														</div>
													</s:if>
												</div>
											</div>
										</s:if>
										<s:if test="farmerBankInfoEnabled==1">
											<div class="aPanel bank_info" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:text name="info.bank" />
														<div class="pull-right">
															<a class="aCollapse" href="#bankingInfo"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div>

												<div class="aContent dynamic-form-con bankingInfo"
													id="bankingInfo">

													<s:iterator value="farmer.bankInfo" status="status">
														<div class="dynamic-flexItem2">
															<p class="flexItem bold" name="farmer.bankinfo">
																<s:property value="bankName" />
															</p>
															<p class="flexItem bankName">
																<a href="#" data-toggle="modal"
																	data-target="#view-bankinfo<s:property value="%{#status.count}" />"
																	class="btn btn-sts linkbtn pull-right"><i
																	class="fa fa-eye" aria-hidden="true"></i> view</a>
															</p>

															<div
																id="view-bankinfo<s:property value="%{#status.count}" />"
																class="modal fade" role="dialog">
																<div class="modal-dialog">
																	<div class="modal-content">
																		<div class="modal-header">
																			<button type="button" class="close"
																				data-dismiss="modal">&times;</button>
																			<h4 class="modal-title">
																				<s:text name='info.bank' />
																			</h4>
																		</div>

																		<div class="modal-body">
																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.AccountTypeProp" />
																				</label> <label><s:property value="accType" /></label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text name="farmer.bankInfo.accNo" />
																				</label> <label><s:property value="accNo" /></label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.bankName" />
																				</label> <label> <s:property value="bankName" />
																				</label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.branchName" />
																				</label> <label> <s:property value="branchName" />
																				</label>
																			</div>
																			<s:if test="getCurrentTenantId()!='sticky' ">
																				<div class="modal-form-item">
																					<label> <s:text
																							name="farmer.bankInfo.sortCode" /></label> <label>
																						<s:property value="sortCode" />
																					</label>
																				</div>
																			</s:if>

																		</div>

																		<div class="modal-footer">
																			<button type="button" class="btn btn-default"
																				data-dismiss="modal">Close</button>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</s:iterator>



												</div>
											</div>
										</s:if>


										<div class="aPanel loan_info" style="width: 100%">
											<div class="aTitle">
												<h2>
													<s:text name="info.loan" />
													<div class="pull-right">
														<a class="aCollapse" href="#loanInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>

											<div class="aContent dynamic-form-con" id="loanInfo">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.isLoanTakenLastYearProp')}" />
													</p>
													<p class="flexItem" name="farmer.loanTakenLastYear">
														<%-- <s:if test="farmer.loanTakenLastYear==''">
															<s:text name="" />
														</s:if> --%>

														<s:if test="farmer.loanTakenLastYear==1">
															<s:text name="yes" />
														</s:if>

														<s:else>
															<s:text name="no" />
														</s:else>
													</p>
												</div>

												<s:if
													test="farmer.loanTakenLastYear!=0 && farmer.loanTakenLastYear!=2">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.loanTakenFromProp')}" />
														</p>
														<p class="flexItem" name="farmer.loanTakenFrom">
															<s:property
																value="loanTakenFromList[farmer.loanTakenFrom]" />
														</p>
													</div>
												</s:if>

												<s:if test="farmer.loanTakenLastYear==1">

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('farmer.amount')}" />
														</p>
														<p class="flexItem" name="farmer.loanAmount">
															<s:property value="farmer.loanAmount" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.purpose')}" />
														</p>
														<p class="flexItem" name="farmer.loanPupose">
															<s:property value="farmer.loanPupose" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.interestRate" />
														</p>
														<p class="flexItem" name="farmer.loanIntRate">
															<s:property value="farmer.loanIntRate" />
														</p>
													</div>

													<div class="dynamic-flexItem2 farmer.loanIntPeriod">
														<p class="flexItem">
															<s:text name="farmer.interestRatePeriod" />
														</p>
														<p class="flexItem" name="farmer.loanIntPeriod">
															<s:property value="farmer.loanIntPeriod" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.security')}" />
														</p>
														<p class="flexItem" name="farmer.loanSecurity">
															<s:property value="farmer.loanSecurity" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.loanRepaymentAmount')}" />
														</p>
														<p class="flexItem" name="farmer.loanRepaymentAmount">
															<s:property value="farmer.loanRepaymentAmount" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.loanRepaymentDate" />
														</p>
														<p class="flexItem" name="repaymentDate">
															<s:property value="loanRepaymentDate" />
														</p>
													</div>

													<div class="dynamic-flexItem2 hideLoanTakenScheme">
														<p class="flexItem">
															<s:text name="farmer.isLoanTakenScheme" />
														</p>
														<%-- <p class="flexItem" name="isLoanTakenScheme">
															<s:text name='%{"dynamicRadio"+farmer.isLoanTakenScheme}' />
														</p> --%>
														<p class="flexItem" name="isLoanTakenScheme">
															<s:if test='farmer.isLoanTakenScheme=="1"'>
																<s:text name="Yes" />
															</s:if>
															<s:elseif test='farmer.isCropInsured=="0"'>
																<s:text name="No" />
															</s:elseif>
															<s:else>
																<s:text name="" />
															</s:else>
														</p>
														<p class="flexItem" name="farmer.isLoanTakenScheme">
															<s:if test='farmer.isLoanTakenScheme=="1"'>
																<s:text name="Yes" />
															</s:if>
															<s:elseif test='farmer.isCropInsured=="0"'>
																<s:text name="No" />
															</s:elseif>
															<s:else>
																<s:text name="" />
															</s:else>
														</p>


													</div>


													<div class="dynamic-flexItem2 hideLoanTakenScheme ">
														<p class="flexItem" name="farmer.loanTakenScheme">
															<s:text name="farmer.loanTakenScheme" />
														</p>
														<p class="flexItem">
															<s:property value="farmer.loanTakenScheme" />
														</p>
													</div>

												</s:if>

											</div>
										</div>


										<s:if test="farmer.isCertifiedFarmer==1">

											<div class="aPanel houseHold_info" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:text name="info.houseHoldOccupation" />
														<div class="pull-right">
															<a class="aCollapse" href="#houseHold_info"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con" id="houseHold_info">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.houseOccupationPrimary" />
														</p>
														<p class="flexItem">
															<s:property
																value="houseHoldOccupationList[farmer.houseOccupationPrimary]" />
														</p>
													</div>


													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.houseOccupationSecondary" />
														</p>
														<p class="flexItem">
															<s:property
																value="houseHoldOccupationList[farmer.houseOccupationSecondary]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.familyMember" />
														</p>
														<p class="flexItem">
															<s:property value="familyMemberList[farmer.familyMember]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.grs" />
														</p>
														<p class="flexItem">
															<s:property value="grsList[farmer.grsMember]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.paidShareCapitial" />
														</p>
														<p class="flexItem">
															<s:property
																value="paidShareCapitialList[farmer.paidShareCapitial]" />
														</p>
													</div>

												</div>
											</div>

											<div class="aPanel investigator_info" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:text name="info.investigatorOpinion" />
														<div class="pull-right">
															<a class="aCollapse" href="#investigator_info"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con"
													id="investigator_info">

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.investigatorOpinion" />
														</p>
														<p class="flexItem">
															<s:property value="farmer.investigatorOpinion" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.investigatorDate" />
														</p>
														<p class="flexItem">
															<s:property value="investigatorDate" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.investigatorName" />
														</p>
														<p class="flexItem">
															<s:property
																value="investigatorNameList[farmer.investigatorName]" />
														</p>
													</div>

												</div>

											</div>

										</s:if>

									</div>

								</div>
							</div>
						</div>
					</div>
				</s:form>

			</div>
		</div>

		<s:form action="dynamicViewReport_populateMoleculeXLS" method="POST"
			id="moleculeForm">
			<s:hidden id="lCode" name="lotCode"></s:hidden>


		</s:form>

	</div>
</div>

<script type="text/javascript"
	src="plugins/jplayer/jquery.jplayer.min.js"></script>


<script type="text/javascript">
var tenant = '<s:property value="getCurrentTenantId()"/>';
var cultiArea='<s:property value="farmCrops.cultiArea" />';
var txnType="308";
var lotCode='<s:property value="getLotCode()"/>';
var $overlay;
var $modal;
var $slider;
var grparr=new Array();
var type='<%=request.getParameter("type")%>';
	jQuery(document).ready(function() {
		 //$(".farmerDiv").show();
		 
		loadCustomPopup();
		$(".farmerDiv").show();
		fetchType = '<s:property value="fetchType"/>';
		var lotCode='<s:property value="getLotCode()"/>';
						//$("#map").css("height",$(document).height());	
				
						if (tenant=='fruitmaster'){
							$("#cultiAreaKanal").text(parseFloat(cultiArea)*8);
						}
						var cropCategory = '<s:property value="farmCrops.cropCategory"/>';
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
						
		
				/* 		var stag = $('a[href="farmer_detail.action"]');
						$(stag).attr("href", "javascript:onFarmList();"); */
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
								 if(tenant=='pratibha' && tenant!='griffith'){ 
								document.getElementById('estimatedYieldQuintal').innerHTML = (totalValu / 100)
										.toFixed(6);
							}
								 else if (tenant!='welspun' && tenant!='griffith'&& tenant!='simfed' && tenant!='ecoagri'){
									 document.getElementById('estimatedYieldMetTon').innerHTML = (totalValu / 1000)
										.toFixed(6);
								 }
							}
						
						
						var cropCategory = '<s:property value="farmCrop.cropCategory"/>';
						var riskAssementVal = '<s:property value="farmCrop.riskAssesment"/>';
						//var farm = '<s:property value="farmCrop.farm.id"/>';
						//var farmCrops = '<s:property value="farmCrop.id"/>';
						//var farmerIds ='<s:property value="farmCrops.farm.farmer.id"/>';
						var branch ='<s:property value="farmer.branchId"/>';
						var farmerId ='<s:property value="farmerId"/>';
						
						var res = farmerId.split("-");
					//	alert(res[0]);
						
						var season ='<s:property value="seasons.code"/>';
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
						hideFields();
					//	renderDynamicDetailFeilds("dynamicFarmers");
						//loadDynamicPhysicalQuality(res[0]);
						loadDynamicPest(res[0],season);
						loadMolecule(lotCode);
						//loadDynamicFertilizer(farm);
						//loadDynamicWeed(farm);
						//loadDynamicWater(farm);
						//loadDynamicFarm(farm);
						//loadDynamicFarmer(farmerId);
					loadViewLabour(res[0]);
							
							jQuery.post("dynamicViewReport_populateHideFn.action", {type:type}, function(result) {
								
								var farmerFieldsArray = jQuery.parseJSON(result);
								$.each(farmerFieldsArray, function(index, value) {
									if(index=='activeFields'){
										$(value).each(function(k,v){
											if(v.id=='1'){
												showByEleName(v.name);
											}if(v.id=='2'){
												showByEleId(v.name);
											}if(v.id=='3'){
												showByEleClass(v.name);
											}if(v.id=='4'){
												$("."+v.name).removeClass("hide");
												
											}
										});
									}
									else if(index=='destroyFileds'){
										$(value).each(function(k,v){
											$("."+v.name).remove();
										});
									}
								});
								$(".farmerDynamicField").removeClass("hide");
								
							});
							jQuery.post("dynamicViewReport_populateHideFnFarm.action", {}, function(result) {
			        			var farmerHideFields = jQuery.parseJSON(result);
			        			
			        			$.each(farmerHideFields, function(index, value) {
			        				if(value.type=='1'){
			    						console.log(value.typeName);
			    						showByEleName(value.typeName);
			    					}if(value.type=='2'){
			    						showByEleId(value.typeName);
			    					}if(value.type=='3'){
			    						showByEleClass(value.typeName);
			    					}if(value.type=='4'){
			    						$("."+value.typeName).removeClass("hide");
			    						console.log(value.typeName);
			    					} if(value.type=='5'){
			    						hideByEleClass(value.typeName);
			    					}
			        			});
			        			
			        	    });
							loadFarmsMap();
							 jQuery.post("dynamicViewReport_populateHideFnFarmCrop.action", {}, function(result) {
			        			var farmerHideFields = jQuery.parseJSON(result);
			        			
			        			$.each(farmerHideFields, function(index, value) {
			        				if(value.type=='1'){
			    						console.log(value.typeName);
			    						showByEleName(value.typeName);
			    					}if(value.type=='2'){
			    						showByEleId(value.typeName);
			    					}if(value.type=='3'){
			    						showByEleClass(value.typeName);
			    					}if(value.type=='4'){
			    						$("."+value.typeName).removeClass("hide");
			    						console.log(value.typeName);
			    					} if(value.type=='5'){
			    						hideByEleClass(value.typeName);
			    					}
			        			});
			        			
			        	    });
							loadFarmCropMap();
							
							 $(".breadCrumbNavigation").find('li:not(:first)').remove();
								
								$(".breadCrumbNavigation").append("<li><a href='dynamicViewReport_list.action?id=10'> <s:text name='Report | Traceability Report'/> </a></li> ");
									
							$('#addCat').attr("href", "#");
				   			$("#addCat").click(function(){
				   			    createFormSubmit();
				   			});
				   		/* 	$(".farmDiv").hide();
							$(".farmerDiv").hide();
							$(".icsDetailDiv").hide();
							$(".groupDiv").hide();
							$(".farmCropDiv").hide();
							$(".areaDetail").hide();
					 if (entityType == '1' || entityType == '5') {
								
								recordName = '<s:property value="farmer" />';
								$(".inspectClass").hide();
							} */
					
					});

 	function onFarmList() {
		document.listForm.submit();
	} 
	function popDownload(id){
		document.getElementById("loadId").value=id;
		$('#audioFileDownload').submit(); 
		
	}

	/* function getFarmDynamic(){
		 $('.dynamicFarmers').empty();
		 $('.dynamicFarms').empty();
	 $('.dynamicFarmCrops').empty();
			 txnType="359"
			
		renderDynamicDetailFeilds("dynamicFarms");
	}
	function getFarmerDynamic(){
		 $('.dynamicFarmers').empty();
		 $('.dynamicFarms').empty();
	 $('.dynamicFarmCrops').empty();
		 txnType="308"
			
	//renderDynamicDetailFeilds("dynamicFarmers");
}*/
	function getFarmCropDynamic(){
		 $('.dynamicFarmers').empty();
		// $('.dynamicFarms').empty();
	 $('.dynamicFarmCrops').empty();
	 txnType="357"
	
	//renderDynamicDetailFeilds("dynamicFarmCrops");
} 

	function getPhysicalCheckDynamicDetails(){
		 $('.dynamicFarmCrops').empty();
	txnType="2019"
		renderDynamicDetailFeilds("dynamicFarmCrops");
	}
	
	function getMoleculeDetails(){
		 $('.dynamicFarmers').empty();
		 $('.dynamicFarmCrops').empty();
		 
	}
	 function getTxnType(){
 			
 	return txnType;
 	}
	 
	 function getBranchIdDyn(){
	     	return '<s:property value="farmCrops.farm.farmer.branchId"/>';
	    	}
	 
	function onCancel() {
		document.cancelform.submit();
	}
	function hideByEleName(name){
     	$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).addClass( "hide" );
     }
 

 function showByEleName(name){
 	$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).removeClass( "hide" );
 }

 function hideByEleClass(className){
 	$("."+className).closest( ".dynamic-flexItem2" ).addClass( "hide" );
 }

 function showByEleClass(className){
 	$("."+className).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
 }

 function hideByEleId(id){
 	$("#"+id).closest( ".dynamic-flexItem2" ).addClass( "hide" );
 }

 function showByEleId(id){
 	$("#"+id).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
 }
 function hideFields(){
	 var app = $(".aPanel");
	 $(app).addClass("hide");
	 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
	 	 $(this).addClass("hide");
	 });
	  
	}
 
 
 function exportMoleculeXLS(){
//alert("AAA");
var lotCode='<s:property value="getLotCode()"/>';
if(!isEmpty(lotCode)){
	$.ajax({
	    url: 'dynamicViewReport_populateMoleculeData.action',
	    type: 'post',
	    dataType: 'json',
	    data: {lotCode:lotCode},
	    success: function(result) {
	    	
	    	if(result.data==1){
	    	
	    		$('#moleculeForm').submit();
	    	
	    		
	  /*   		 $('#moleculeForm').submit(function() { 
	    			//alert("AAA");
	    			 $.ajax({
	    				 data: $(this).serialize(), // get the form data
	    			        type: $(this).attr('method'), // GET or POST
	    			        url: $(this).attr('action'), // the file to call
	    			        success: function(response) {
	    			        	 var result = response;
	    			        	alert(result);
	    			        	 console.log(result);
	    			        	//alert('<s:text name="success"/>'); 
	    			        },
	    			        error: function(data) {
	    			        	 alert('<s:text name="export.nodata"/>');
	    			        }

	    			    }); 
	    			 
	    			 return false; 
	    			});
	    		  */
	    		 
	    		 
	    	}else{
	    		 alert('<s:text name="export.nodata"/>');
	    	}
	    },
	    error: function(data) {
	    	 alert('<s:text name="export.nodata"/>');
	    },
	    asyns : false

	}); 

}else{
	 alert('<s:text name="export.nodata"/>');
}

	

	 
	 
	}
 
 function renderDynamicDetailFeilds(divclass) {
		var json = "";
		var jsonData = "";
		var dataa = {
			txnTypez : 2019,
			branch:'<s:property value="farmer.branchId"/>'
			
		}

		$.ajax({
			type : "POST",
			async : false,
			data : dataa,
			url : "farmer_populateDynamicFields.action",
			success : function(result) {
				json = result;
			}
		});

		$(json)
				.each(
						function(k, v) {
							$(v.sections)
									.each(
											function(key, val) {
												var aPanel = $("<div/>").addClass(
														"aPanel");
												var aTitle = $("<div/>").addClass(
														"aTitle").html(
														"<h2>" + val.secName
																+ "</h2>");
												var aContent = $("<div/>")
														.addClass(
																"aContent dynamic-form-con "
																		+ val.secCode);
												aPanel.append(aTitle);
												aPanel.append(aContent);

												var formContainerWrapper = $(
														"<div/>").addClass(
														"formContainerWrapper");
												formContainerWrapper.append(aPanel);
												$("."+divclass).append(
														formContainerWrapper);
											});

							$(v.fields)
									.each(
											function(key, val) {

												if (val.compoType != 8) {
													var dFlex = $("<div/>")
															.addClass(
																	"dynamic-flexItem2");
													var p1 = $("<p/>").addClass(
															'flexItem').text(
															val.compoName);
													var p2 = $(
															"<p style='word-wrap: break-word' />")
															.addClass(
																	'flexItem '
																			+ val.compoCode);
													dFlex.append(p1);
													dFlex.append(p2);
													$("." + val.sectionId).append(
															dFlex);

													if (!isEmpty(val.afterInsert)) {
														var insertEle = $(
																'[name="'
																		+ val.afterInsert
																		+ '"]')
																.parent().parent();
														$(dFlex).insertAfter(
																insertEle);
													}

													if (val.parentDepenCode != ''
															&& val.parentDepenKey != '') {
														if (val.parentDepenKey
																.indexOf(",") >= 0
																&& val.parentDepenCode
																		.indexOf(",") < 0) {
															var values = val.parentDepenKey
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var classN = val.parentDepenCode
																						+ "_"
																						+ e
																								.trim();
																				dFlex
																						.addClass("hide"
																								+ " par"
																								+ val.parentDepenCode);
																				dFlex
																						.addClass(classN);
																			});
														} else if (val.parentDepenKey
																.indexOf(",") >= 0
																&& val.parentDepenCode
																		.indexOf(",") >= 0) {
															var values = val.parentDepenKey
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var depCode = val.parentDepenCode
																						.split(",");
																				$
																						.each(
																								depCode,
																								function(
																										j,
																										f) {
																									var classN = f
																											.trim()
																											+ "_"
																											+ e
																													.trim();
																									dFlex
																											.addClass("hide"
																													+ " par"
																													+ f
																															.trim());
																									dFlex
																											.addClass(classN);
																								});
																			});
														} else if (val.parentDepenKey
																.indexOf(",") < 0
																&& val.parentDepenCode
																		.indexOf(",") >= 0) {
															var values = val.parentDepenCode
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var classN = e
																						.trim()
																						+ "_"
																						+ val.parentDepenKey;
																				dFlex
																						.addClass("hide"
																								+ " par"
																								+ e
																										.trim());
																				dFlex
																						.addClass(classN);
																			});
														} else {
															var classN = val.parentDepenCode
																	+ "_"
																	+ val.parentDepenKey;
															dFlex
																	.addClass("hide"
																			+ " par"
																			+ val.parentDepenCode);
															dFlex.addClass(classN);
														}
													}

												} else {

													var table = $("<table>")
															.addClass(
																	"table table-bordered");
													var tr = $("<tr/>").attr({
														id : "tDynmaicRow" + val.id
													});
													table.append(tr);

													if (val.parentDepenCode != ''
															&& val.parentDepenKey != '') {
														if (val.parentDepenKey
																.indexOf(",") >= 0
																&& val.parentDepenCode
																		.indexOf(",") < 0) {
															var values = val.parentDepenKey
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var classN = val.parentDepenCode
																						+ "_"
																						+ e
																								.trim();
																				table
																						.addClass("hide"
																								+ " par"
																								+ val.parentDepenCode);
																				table
																						.addClass(classN);
																			});
														} else if (val.parentDepenKey
																.indexOf(",") >= 0
																&& val.parentDepenCode
																		.indexOf(",") >= 0) {
															var values = val.parentDepenKey
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var depCode = val.parentDepenCode
																						.split(",");
																				$
																						.each(
																								depCode,
																								function(
																										j,
																										f) {
																									var classN = f
																											.trim()
																											+ "_"
																											+ e
																													.trim();
																									table
																											.addClass("hide"
																													+ " par"
																													+ f
																															.trim());
																									table
																											.addClass(classN);
																								});
																			});
														} else if (val.parentDepenKey
																.indexOf(",") < 0
																&& val.parentDepenCode
																		.indexOf(",") >= 0) {
															var values = val.parentDepenCode
																	.split(",");
															$
																	.each(
																			values,
																			function(
																					i,
																					e) {
																				var classN = e
																						.trim()
																						+ "_"
																						+ val.parentDepenKey;
																				table
																						.addClass("hide"
																								+ " par"
																								+ e
																										.trim());
																				table
																						.addClass(classN);
																			});
														} else {
															var classN = val.parentDepenCode
																	+ "_"
																	+ val.parentDepenKey;
															table
																	.addClass("hide"
																			+ " par"
																			+ val.parentDepenCode);
															table.addClass(classN);
														}
													}
													$("." + val.sectionId).append(
															table);
												}
											});

							$(v.groups).each(
									function(key, val) {
										// alert(val.refId);
										if (val.compoType != 10) {
											$("#tDynmaicRow" + val.refId).append(
													$("<td/>").addClass(
															val.compoCode).html(
															val.compoName));
										}
									});
							$(v.sections)
									.each(
											function(key, val) {

												if ($('.' + val.secCode).children().length == 0) {

													$('.' + val.secCode)
															.closest(
																	'.formContainerWrapper')
															.remove();
												}

											});
							setDynamicFieldDetailValuesFarmers();

						});
	}
 function setDynamicFieldDetailValuesFarmers() {
		
		var dataa = {};
		var url = "";
		var jsonData = "";
		var farmerId='<s:property value="farmerId"/>';
		
		res=farmerId.split("-");
		 var farmerDynamicDataId = '<s:property value="farmerDynamicDataId"/>';	
		//alert(farmerDynamicDataId);
		//alert(jQuery(".uId").val());
		dataa = {
			selectedObject : res[0],
			txnTypez : getTxnType(),
			id:farmerDynamicDataId
		}
		
		url = "dynamicViewReport_populateDynmaicFieldValuesByRefId.action";

		$.ajax({
			type : "POST",
			async : false,
			data : dataa,
			url : url,
			success : function(result) {
				jsonData = result;

			}
		});

		$(jsonData)
				.each(
						function(k, v) {

							$(v.fields)
									.each(
											function(key, val) {
												$("." + val.code)
														.text(val.dispName);
												$("." + val.code)
														.text(val.dispName);
												if(val.name.indexOf(",")<0){
													var classN = val.code + "_"
															+ val.name;

													try {
														$('.' + classN).removeClass(
																"hide");
													} catch (e) {

													}
													}else{
														$((val.name.split(","))).each(function(k, v) {
															var classN = val.code + "_"
															+ v.trim();

													try {
														$('.' + classN).removeClass(
																"hide");
													} catch (e) {

													}
														});
													}
												/*alert(val.isActPlan)
												if (val.isActPlan !=null) {
													var ids = val.photoIds;
													$("." + val.code)
															.append(
																	"<button type='button' class='btn btn-sm pull-right photo photoModel' style='margin-right:15%' onclick=enablePhotoModal('"
																			+ ids
																			+ "')><i class='fa fa-picture-o' aria-hidden='true'></i></button>")
															.append(
																	"<button type='button' class='hide btn btn-sm pull-right photoPdf' style='margin-right:2%' onclick=letImg('"
																			+ ids
																			+ "','"
																			+ val.code
																			+ "')></button>");

												}*/
												if (val.photoCompoAvailable == "1") {
													var ids = val.photoIds;
													$("." + val.code)
															.append(
																	"<button type='button' class='btn btn-sm pull-right photo photoModel' style='margin-right:15%' onclick=enablePhotoModal('"
																			+ ids
																			+ "')><i class='fa fa-picture-o' aria-hidden='true'></i></button>")
															.append(
																	"<button type='button' class='hide btn btn-sm pull-right photoPdf' style='margin-right:2%' onclick=letImg('"
																			+ ids
																			+ "','"
																			+ val.code
																			+ "')></button>");

												} else if (val.photoCompoAvailable == "2") {
													var ids = val.photoIds;

													$("." + val.code)
															.append(
																	"<button type='button' class='btn btn-sm pull-right' style='margin-right:15%' onclick=playAudioFiles('vid"
																			+ val.code
																			+ "','"
																			+ val.componentType
																			+ "' )><i class='fa fa-play-circle-o' aria-hidden='true'></i></button>")
															.append(
																	" <video class='hide' id='vid"
																			+ val.code
																			+ "' preload='none' controls ><source  src='farmer_populateVideoPlay?imgId="
																			+ ids
																			+ "' type='video/mp4'></video>");

												} else if (val.componentType == "14") {

													$('#' + val.code).text(
															val.dispName);
													$('#weatherVal' + val.code)
															.val(val.name);
												} else if (val.componentType == "9") {
													if (!isEmpty(val.name)) {
														var values = val.name
																.split(",");
														$
																.each(
																		values,
																		function(i,
																				e) {
																			var classN = val.code
																					+ "_"
																					+ e
																							.trim();

																			$(
																					'.'
																							+ classN)
																					.removeClass(
																							"hide");

																		});
													}

												}
											});

							var tempRefId = new Array();

							$(v.groups)
									.each(
											function(key, val) {
												if (tempRefId.indexOf(val.refId
														+ "-" + val.typez) == '-1') {
													tempRefId.push(val.refId + "-"
															+ val.typez);
													var rowId = ("#tDynmaicRow" + val.refId);
													var newRow = $("<tr/>");
													$(rowId)
															.find("td")
															.each(
																	function(k, v) {

																		var classzName = $(
																				this)
																				.attr('class').split(' ')[0]
																				+ "t"
																				+ val.typez;
																		var td = $(
																				"<td/>")
																				.attr(
																						{
																							class : classzName
																						});
																		newRow
																				.append(td);

																	});
															$(rowId).parent()
															.append(newRow);

												}
											});

						});

		$(jsonData)
				.each(
						function(k, v) {
							var classzName = "";
							$(v.groups)
									.each(
											function(key, val) {
												classzName = "." + val.code + "t"
														+ val.typez;
												$(classzName).text(val.dispName);
												if (val.photoCompoAvailable == "1") {
													var ids = val.photoIds;

													$(classzName)
															.append(
																	"<button type='button' class='btn btn-sm pull-right photo photoModel' style='margin-right:2%' onclick=enablePhotoModal('"
																			+ ids
																			+ "')><i class='fa fa-picture-o' aria-hidden='true'></i></button>")
															.append(
																	"<button type='button' class='hide btn btn-sm pull-right photoPdf' style='margin-right:2%' onclick=letImg('"
																			+ ids
																			+ "','"
																			+ val.code
																			+ "')></button>");
												}
											});
						});
	}
 
</script>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script type="text/javascript">
var id ='<s:property value="id"/>';
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
		var dataArr = new Array();
		try {
			var farmCropCordinates = jQuery("#farmCropCordinates").val();
			if(!isEmpty(farmCropCordinates)){
				var landArea = JSON.parse(farmCropCordinates);
				
				if (landArea.length > 0) {
					$(landArea).each(function(k, v) {
							dataArr.push({
								 
								jsonObjec:v.jsonObjectList,
								//certified:v.certified,
								//organicStatus:v.organicStatus
							});
					});
				}
				loadMap(dataArr);
				
				//addExistingMarkers(landArea);
			}
			
		} catch (err) {
			console.log(err);
		}
	}
	
	 function loadFarmsMap() {
		try {
			var farmCropCordinates = jQuery("#farmCordinates").val();
		if(!isEmpty(farmCropCordinates)){
			var landArea = JSON.parse(farmCropCordinates);
			
			loadMap1('map1', '<s:property value="farm.latitude"/>','<s:property value="farm.longitude"/>', '');
			
			addExistingMarkers(landArea);
		}
			
		} catch (err) {
			console.log(err);
		}
	} 

	var map;
	function initMap() {
		map = new google.maps.Map(document.getElementById('map'), {
			/* center : {
				lat : 11.0168,
				lng : 76.9558
			},
			zoom : 10,
			mapTypeId: google.maps.MapTypeId.HYBRID, */
			 center: {
		           lat: 20.5937,
		           lng: 78.9629
		         },
		         zoom:4,
		         mapTypeId: google.maps.MapTypeId.HYBRID,
		});
		
		map1 = new google.maps.Map(document.getElementById('map1'), {
			/* center : {
				lat : 11.0168,
				lng : 76.9558
			},
			zoom : 10,
			mapTypeId: google.maps.MapTypeId.HYBRID, */
			 center: {
		           lat: 20.5937,
		           lng: 78.9629
		         },
		         zoom:4,
		         mapTypeId: google.maps.MapTypeId.HYBRID,
		});
	}
	
	function loadMap(dataArr) {
		var markersArray = new Array();
		try {
			$(dataArr).each(function(k, v) {
			/* loadMaps('map', '<s:property value="farmCrops.latitude"/>',
					'<s:property value="farmCrops.longitude"/>', v.jsonObjectList); */
					try {
						
						
						if(v.jsonObjec.length>0){
							var cords = new Array();
							$(v.jsonObjec).each(function(k,v){
								
								cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
								if(k==0){
									var url = window.location.href;
									var temp = url;
									for(var i = 0 ; i < 1 ; i++) {
										  temp = temp.substring(0, temp.lastIndexOf('/'));
									 }
							 	  	var intermediateImg;
									//setMapOnAll(null);   
									var intermediatePointIcon;
									
									intermediateImg = "red_placemarker.png";
									 intermediatePointIcon = temp + '/img/'+intermediateImg;   
						 	marker = new google.maps.Marker({
									position : new google.maps.LatLng(parseFloat(v.lat),
											parseFloat(v.lon)),
											icon:intermediatePointIcon,
									map : map
								});  
							var infowindow = new google.maps.InfoWindow();
							 marker.addListener('mouseover', function() {
									infowindow.setContent(buildDataOnMouseHover(v.name));
									infowindow.open(map, this);
								});
								}
								//var name=v.name;
							});
							
							var bounds = new google.maps.LatLngBounds();
							 var plotting = new google.maps.Polygon({
						          paths: cords,
						          strokeColor: '#FF0000',
						          strokeOpacity: 0.8,
						          strokeWeight: 2,
						          fillColor: '#E7D874',
						          fillOpacity: 0.35
						        });
							 plotting.setMap(map);
							 markersArray.push(plotting);
							 markersArray.push(marker);
							
							 loc = new google.maps.LatLng(cords[0].lat, cords[0].lng);
							 
							 bounds.extend(loc);
							 map.fitBounds(bounds);      // auto-zoom
							
							/*   bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
							 map.fitBounds(bounds); */
							  
						 	/*  var listener = google.maps.event.addListener(map, "idle", function () {
								    map.setZoom(15);
								    google.maps.event.removeListener(listener);
								   
								});   */
						
						
						}
					} catch (err) {
						console.log(err);
					}
			});
	
		} catch (err) {
			console.log(err);
		}
	}
	function td(val){
		  return "<td>"+getFormattedValue(val)+"</td>";
	}
	function getFormattedValue(val){
		   if(val==null||val==undefined||val.toString().trim()==""){
		    return " ";
		   }
		   return val;
	}
	 function buildDataOnMouseHover(name)
		{
		 var content = "<table class='table table-responsive table-hover table-bordered' style='background:none;'>";
			
			
			content += "<tr>"
			content += td('<s:property value="%{getLocaleProperty('farmName')}" />');
			content += td(name);
			content += "</tr>"
				content += "</table>";
			return content;
		}
	
	function loadMap1(mapDiv, latitude, longitude, landArea) {
		try {
			var bounds = new google.maps.LatLngBounds();
			if(!isEmpty(latitude)&&!isEmpty(longitude)){
				var marker;
				marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(latitude),
							parseFloat(longitude)),
					map : map1
				});
			}
			if(!isEmpty(landArea)){
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
			}
			
			
		} catch (err) {
			console.log(err);
		}
	}
	
	 function addExistingMarkers(jsonData){
		 var neighbouringDetails;
		 if(!isEmpty(jsonData)){
			 $.each( jsonData, function( k, v ) {
					
				 if(v.neighbouringDetails != undefined){
					  neighbouringDetails = v.neighbouringDetails;
					  
				 }
			 });
			
		 }
		
		
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
	 
	 function getPesticidePostdata(){
		 var farm = '<s:property value="farmCrops.farm.farmer.id"/>';	
		 var postData = {};
			
		 //postData[name] = " = '"+farm+"'";
		
			return JSON.stringify(postData);
		}

	 function loadDynamicPest(farmerId,season){
		//alert(farm)	;
		 jQuery("#detail4").jqGrid(
				    {
				    url:'dynamicViewReport_pesticideSprayedData.action',
				            pager: '#pagerForDetail4',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"farmersId" : function(){	
				    	   			return  farmerId;
				    			  } ,
				    			  "season" : function(){	
					    	   			return  season;
					    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "22";
				    			  } ,
				    			   "pesticideFilterList" : function(){
									  return getPesticidePostdata();
								  }, 
				    	   	},
				                    colNames:[
										
											'<s:property value="%{getLocaleProperty('Date')}" />',
											'<s:property value="%{getLocaleProperty('createdUser')}" />',
											'<s:property value="%{getLocaleProperty('farmerName')}" />',
											'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
											'<s:property value="%{getLocaleProperty('Pesticide Mixed')}" />',
											'<s:property value="%{getLocaleProperty('Name of the product')}" />',
											'<s:property value="%{getLocaleProperty('Physical state of the product')}" />',
											'<s:property value="%{getLocaleProperty('Quantity sprayed')}" />',	
											'<s:property value="%{getLocaleProperty('UOM')}" />',
											'<s:property value="%{getLocaleProperty('Cost of Pesticide')}" />',
											'<s:property value="%{getLocaleProperty('Ingredient')}" />',
											'<s:property value="%{getLocaleProperty('Recommended Pesticide')}" />',	
											'<s:property value="%{getLocaleProperty('Other Pesticide Product')}" />',	
											
				                           
				            ],
				            colModel:[
						
					            {name:'date', index:'date', width:125, sortable:false},
					            {name:'createdUser', index:'createdUser', width:125, sortable:false},
					            {name:'farmerName', index:'farmName', width:125, sortable:false},
					            {name:'farmName', index:'farmName', width:125, sortable:false},
					            {name:'pesticideMixed', index:'pesticideMixed', width:125, sortable:false},
					            {name:'nameOfTheProduct', index:'nameOfTheProduct', width:125, sortable:false},
					            {name:'physicalStateOfTheProduct', index:'physicalStateOfTheProduct', width:125, sortable:false},
					            {name:'quantitySprayed', index:'quantitySprayed', width:125, sortable:false},
					            {name:'uom', index:'uom', width:125, sortable:false},
					            {name:'costOfPesticide', index:'costOfPesticide', width:125, sortable:false},
					            {name:'ingredient', index:'ingredient', width:125, sortable:false},
					            {name:'recommendedPesticide', index:'recommendedPesticide', width:125, sortable:false},				          
					            {name:'otherPesticideProduct', index:'otherPesticideProduct', width:125, sortable:false},
					            
				            ], 
				        	//colNames:gridColumnNames,
						   //	colModel:gridColumnModels,
				            height: 301,
				            width: $("#baseDiv").width(), // assign parent div width
				            scrollOffset : 0,
				            rowNum:10,
				            rowList : [10, 25, 50],
				            sortname:'id',
				            sortorder: "desc",
				           // shrinkToFit : true,
				           // autowidth: true,
				            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				       
					      /*        onSelectRow: function(id){
					            	 window.open("dynmaicCertificationReport_detail?txnType=22&detail=1&id="+id);
					            
					            },  */
				          /*   onSortCol: function (index, idxcol, sortorder) {
				            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				            } */
				    });
				    jQuery("#detail4").jqGrid('navGrid', '#pagerForDetail4', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
				         //   jQuery("#detail4").jqGrid('filterToolbar', {stringResult: false, searchOnEnter : false}); // enabling filters on top of the header.

				    colModel = jQuery("#detail4").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail4")[0].id) +
				            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				    var cmi = colModel[i], colName = cmi.name;
				    if (cmi.sortable !== false) {
				    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				    }
				    });
				//    windowHeight = windowHeight-100;
				   // $('#detail4').jqGrid('setGridHeight',(windowHeight));
		 }
	 function loadDynamicFertilizer(farm){
		  jQuery("#detail6").jqGrid(
				    {
				    url:'dynamicViewReport_data.action',
				            pager: '#pagerForDetail6',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"farmId" : function(){	
				    	   			return  farm;
				    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "2004";
				    			  } 
				    	   	},
				                   colNames:[
										
											'<s:property value="%{getLocaleProperty('Date')}" />',
											'<s:property value="%{getLocaleProperty('createdUser')}" />',
											'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
											
				                           
				            ],
				            colModel:[
						
					            {name:'date', index:'date', width:125, sortable:true},
					            {name:'createdUser', index:'createdUser', width:125, sortable:true},
					            {name:'farmName', index:'farmName', width:125, sortable:true},
				                  
				            ],
				            height: 301,
				            width: $("#baseDiv").width(), // assign parent div width
				            scrollOffset: 0,
				            rowNum:10,
				            rowList : [10, 25, 50],
				            sortname:'id',
				            sortorder: "desc",
				            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				       
					             onSelectRow: function(id){
					            	 window.open("dynmaicCertificationReport_detail?txnType=2004&detail=1&id="+id);
					            
					            }, 
				            onSortCol: function (index, idxcol, sortorder) {
				            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				            }
				    });
				    jQuery("#detail6").jqGrid('navGrid', '#pagerForDetail6', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
				            jQuery("#detail6").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

				    colModel = jQuery("#detail6").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail6")[0].id) +
				            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				    var cmi = colModel[i], colName = cmi.name;
				    if (cmi.sortable !== false) {
				    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				    }
				    });
				//    windowHeight = windowHeight-100;
				    $('#detail6').jqGrid('setGridHeight',(windowHeight));
		 }
	 function loadDynamicWeed(farm){
		  jQuery("#detail7").jqGrid(
				    {
				    url:'dynamicViewReport_data.action',
				            pager: '#pagerForDetail7',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"farmId" : function(){	
				    	   			return  farm;
				    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "5";
				    			  } 
				    	   	},
				                   colNames:[
										
											'<s:property value="%{getLocaleProperty('Date')}" />',
											'<s:property value="%{getLocaleProperty('createdUser')}" />',
											'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
											
				                           
				            ],
				            colModel:[
						
					            {name:'date', index:'date', width:125, sortable:true},
					            {name:'createdUser', index:'createdUser', width:125, sortable:true},
					            {name:'farmName', index:'farmName', width:125, sortable:true},
				                  
				            ],
				            height: 301,
				            width: $("#baseDiv").width(), // assign parent div width
				            scrollOffset: 0,
				            rowNum:10,
				            rowList : [10, 25, 50],
				            sortname:'id',
				            sortorder: "desc",
				            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				       
					             onSelectRow: function(id){
					            	 window.open("dynmaicCertificationReport_detail?txnType=5&detail=1&id="+id);
					            
					            }, 
				            onSortCol: function (index, idxcol, sortorder) {
				            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				            }
				    });
				    jQuery("#detail7").jqGrid('navGrid', '#pagerForDetail7', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
				            jQuery("#detail7").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

				    colModel = jQuery("#detail7").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail7")[0].id) +
				            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				    var cmi = colModel[i], colName = cmi.name;
				    if (cmi.sortable !== false) {
				    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				    }
				    });
				  //  windowHeight = windowHeight-100;
				    $('#detail7').jqGrid('setGridHeight',(windowHeight));
		 }
	 function loadDynamicWater(farm){
		  jQuery("#detail8").jqGrid(
				    {
				    url:'dynamicViewReport_data.action',
				            pager: '#pagerForDetail8',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"farmId" : function(){	
				    	   			return  farm;
				    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "2005";
				    			  } 
				    	   	},
				                   colNames:[
										
											'<s:property value="%{getLocaleProperty('Date')}" />',
											'<s:property value="%{getLocaleProperty('createdUser')}" />',
											'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
											
				                           
				            ],
				            colModel:[
						
					            {name:'date', index:'date', width:125, sortable:true},
					            {name:'createdUser', index:'createdUser', width:125, sortable:true},
					            {name:'farmName', index:'farmName', width:125, sortable:true},
				                  
				            ],
				            height: 301,
				            width: $("#baseDiv").width(), // assign parent div width
				            scrollOffset: 0,
				            rowNum:10,
				            rowList : [10, 25, 50],
				            sortname:'id',
				            sortorder: "desc",
				            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				       
					             onSelectRow: function(id){
					            	 window.open("dynmaicCertificationReport_detail?txnType=2005&detail=1&id="+id);
					            
					            }, 
				            onSortCol: function (index, idxcol, sortorder) {
				            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				            }
				    });
				    jQuery("#detail8").jqGrid('navGrid', '#pagerForDetail8', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
				            jQuery("#detail8").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

				    colModel = jQuery("#detail8").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail8")[0].id) +
				            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				    var cmi = colModel[i], colName = cmi.name;
				    if (cmi.sortable !== false) {
				    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				    }
				    });
				 //   windowHeight = windowHeight-100;
				    $('#detail8').jqGrid('setGridHeight',(windowHeight));
		 }
	 
	 
	 function loadMolecule(){
		 jQuery("#detail9").jqGrid(
					{
					   	url:'dynamicViewReport_moleculeData.action',
					   	pager: '#pagerForDetail9',	  
					   	datatype: "json",	
					   	mtype: 'POST',
					   
					    postData:{
							  "lotCode" : function(){
									  return lotCode;
							  }
						},
					   	colNames:[
						        
									'<s:text name="Date"/>',
					  		   	  '<s:text name="Lot No"/>',
					  		   	'<s:property value="%{getLocaleProperty('EU/US')}" />',
					  		    '<s:property value="%{getLocaleProperty('Status')}" />',
					  		  '<s:property value="%{getLocaleProperty('File')}" />',
					  		 //'<s:property value="%{getLocaleProperty('Status')}" />'
					  		 /*  <s:if test="isKpfBased==1">
					  		  <s:if test="currentTenantId!='gsma''">
					  		  '<s:property value="%{getLocaleProperty('samithi.type')}" />'
					  		</s:if>
					  		    </s:if> */
					      	 	 ],
					   	colModel:[
									
						   	{name:'cropYieldDate',index:'cropYieldDate',width:125,sortable:true,search:false},
					   		{name:'landHolding',index:'landHolding',width:125,sortable:true,search:false},
					   		{name:'US',index:'US',width:125,sortable:true,search:false},
					   		{name:'status',index:'status',width:125,sortable:true,search:false},
					   		{name:'image',index:'image',width:125,sortable:false,search:false},
					   		//{name:'status',index:'status',width:125,sortable:true,search:false}
					   	 /* <s:if test="isKpfBased==1">
					   	 <s:if test="currentTenantId!='gsma''">
					   
					   		{name:'groupType',index:'groupType',width:125,sortable: false, width :125, search:true, stype: 'select', searchoptions: { value: ':<s:text name="allanswer"/>;4:<s:text name="masterType4"/>;11:<s:text name="masterType11"/>' }}
					   		</s:if>	</s:if> */
					   			 ],
					   			height: 301,
					            width: $("#baseDiv").width(), // assign parent div width
					            scrollOffset: 0,
					            rowNum:10,
					            rowList : [10, 25, 50],
					            sortname:'id',
					            sortorder: "desc",
					            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
					       
					            onSortCol: function (index, idxcol, sortorder) {
					            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
					                    && this.p.colModel[this.p.lastsort].sortable !== false) {
					            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
					            }
					            }
					});
		 jQuery("#detail9").jqGrid('navGrid', '#pagerForDetail9', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
         jQuery("#detail9").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

 colModel = jQuery("#detail9").jqGrid('getGridParam', 'colModel');
 $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail9")[0].id) +
         ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
 var cmi = colModel[i], colName = cmi.name;
 if (cmi.sortable !== false) {
 $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
 } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
 $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
 }
 });
	 }
	 

	 function loadDynamicPhysicalQuality(farmCrops){
	/* 
		 jQuery.post("dynmaicCertificationReport_detail.action", {id:5727,txnType:2019}, function(distribution) {
			alert(distribution);
		 }); */
		 
		 jQuery("#detail5").jqGrid(
				    {
				    url:'dynmaicCertificationReport_detail.action',
				            pager: '#pagerForDetail5',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"id" : function(){	
				    	   			return  farmCrops;
				    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "2019";
				    			  } ,
				    			   "id":  function(){	
					    	   			return  id;
				    			  } 
				           	}
				    });
		 
		  /* jQuery("#detail5").jqGrid(
				    {
				    url:'dynamicViewReport_data.action',
				            pager: '#pagerForDetail5',
				            mtype: 'POST',
				            datatype: "json",
				           	postData:{
				    	   		"farmId" : function(){	
				    	   			return  farmCrops;
				    			  } ,
				    			  "txnType" :  function(){	
				    	   			return  "2019";
				    			  } ,
				    			  "id":  function(){	
					    	   			return  id;
				    			  }
				    	   	},
				                   colNames:[
										
											'<s:property value="%{getLocaleProperty('Date')}" />',
											'<s:property value="%{getLocaleProperty('createdUser')}" />',
											'<s:property value="%{getLocaleProperty('farm.farmName')}" />',
											
				                           
				            ],
				            colModel:[
						
					            {name:'date', index:'date', width:125, sortable:true},
					            {name:'createdUser', index:'createdUser', width:125, sortable:true},
					            {name:'farmName', index:'farmName', width:125, sortable:true},
				                  
				            ],
				            height: 301,
				            width: $("#baseDiv").width(), // assign parent div width
				            scrollOffset: 0,
				            rowNum:10,
				            rowList : [10, 25, 50],
				            sortname:'id',
				            sortorder: "desc",
				            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				       
					             onSelectRow: function(id){
					            	 window.open("dynmaicCertificationReport_detail?txnType=2019&detail=1&id="+id);
					            
					            }, 
				            onSortCol: function (index, idxcol, sortorder) {
				            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				            }
				    });
				    jQuery("#detail5").jqGrid('navGrid', '#pagerForDetail5', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
				            jQuery("#detail5").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

				    colModel = jQuery("#detail5").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail5")[0].id) +
				            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				    var cmi = colModel[i], colName = cmi.name;
				    if (cmi.sortable !== false) {
				    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				    }
				    }); */
				 
				   // windowHeight = windowHeight-100;
				   // $('#detail5').jqGrid('setGridHeight',(windowHeight));
		 }
	 
	 
	 
	 function loadViewLabour(farmCrops){
		 var gridColumnNames = new Array();
			var gridColumnModels = new Array();
			
			var colNames='<s:property value="mainGridCols"/>';
			var colModels='<s:property value="mainGridColNames"/>';
			var footCol='<s:property value="footerSumCols"/>';
			var footTot='<s:property value="footerTotCol"/>';
			$(colNames.split("%")).each(function(k,val){
					if(!isEmpty(val)){
						var cols=val.split("#");
						gridColumnNames.push(cols[0]);
						//gridColumnModels.push({name: cols[0],width:cols[1],sortable: false,frozen:true});
					}
			});
			
			$(colModels.split("%")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					gridColumnModels.push({name: cols[0],width:cols[1],sortable: false,frozen:true});
				}
		});
		 jQuery("#detail11").jqGrid(
				    {
				    	
				    	url:'dynamicViewReport_cropDetail.action?',
					   	pager: '#pagerForDetail11',
					   	datatype: "json",
					   	mtype: 'POST',
					   	postData:{	
					   	 "crop_id" :  function(){
							  return '<s:property value="farmCrops.farm.farmer.id"/>';
						  },
							    "filterList" : function(){
								  return getPostdata();
							  },  
							   "lotCode" :  function(){
								  return lotCode;
							  },
							  
							  "view_id" :  function(){
								  return 12;
							  },
							 /* <s:if test='branchId==null'>
				              "branchIdParma":function(){
				            		return $("#branchIdParma").val();
					         },
					         </s:if>
					         <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							  "subBranchIdParma" : function(){
								  return document.getElementById("subBranchIdParam").value;
							  },
							  </s:if> */
							},
					   	colNames:[
					   		<s:if test='branchId==null'>
							'<s:text name="%{getLocaleProperty('app.branch')}"/>',
					  </s:if>
					  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							'<s:text name="app.subBranch"/>',
					  </s:if>
							'<s:property value="%{getLocaleProperty('physicalQualityCheckDate')}" />',
							'<s:property value="%{getLocaleProperty('lotNo')}" />',
							'<s:property value="%{getLocaleProperty('coldStorageDate')}" />',
							'<s:property value="%{getLocaleProperty('coldStorageName')}" />',
							'<s:property value="%{getLocaleProperty('warehouse')}" />',
							'<s:property value="%{getLocaleProperty('Chamber')}" />',
							'<s:property value="%{getLocaleProperty('floorName')}" />',
							'<s:property value="%{getLocaleProperty('bayNumber')}" />',
							'<s:property value="%{getLocaleProperty('noofBags')}" />',
							'<s:property value="%{getLocaleProperty('quantityy')}" />'
					   	          ],
					   	colModel:[
					   		<s:if test='branchId==null'>
					   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
					   			value: '<s:property value="parentBranchFilterText"/>',
					   			dataEvents: [ 
						   			          {
						   			            type: "change",
						   			            fn: function () {
						   			            	console.log($(this).val());
						   			             	getSubBranchValues($(this).val())
						   			            }
						   			        }]
						   			
						   			}},	   			   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   		
					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if> 
{name:'date',index:'date',width:130,sortable:false},
	{name:'lotNo',index:'lotNo',width:130,sortable:false},
	{name:'cdate',index:'cdate',width:130,sortable:false},
	{name:'cname',index:'cname',width:130,sortable:false},
	{name:'warehouse',index:'warehouse',width:130,sortable:false},
	{name:'Chamber',index:'Chamber',width:130,sortable:false},
	{name:'floor',index:'floor',width:130,sortable:false},
	{name:'Bay',index:'Bay',width:130,sortable:false},
	{name:'Bag',index:'Bag',width:130,sortable:false},
	{name:'Quantity',index:'Quantity',width:130,sortable:false}
	],
					   	height: 301, 
					   	width: $("#baseDiv").width(),
					   	scrollOffset: 0,     
					   	sortname:'id',
					   	sortorder: "desc",
					   	rowNum:10,
					   	rowList : [10,25,50],
					   	viewrecords: true,
					   	onSelectRow: function(id){ 
					   		var rows =jQuery('#detail11').getRowData(id);
					   	
								console.log(JSON.stringify(rows));
								getColdStorageTransferDetails(JSON.parse(JSON.stringify(rows.lotNo)),
										JSON.parse(JSON.stringify(rows.cname)),JSON.parse(JSON.stringify(rows.warehouse)),
										JSON.parse(JSON.stringify(rows.Chamber)),JSON.parse(JSON.stringify(rows.floor)),
										JSON.parse(JSON.stringify(rows.Bay))
								);
						},	
					   	footerrow: true,
						 loadComplete: function(data){
							 if (data.footersum != '' && data.footersum != undefined && !isEmpty(data.footersum) && JSON.stringify(data.footersum)!='{}') {
								 var footerss = data.footersum;
							 var columnNames = footCol.split("#");
							  var $self = $(this); 
							  var options = {};
							  options[footTot] = "Total";
						      for (var i = 0; i < columnNames.length-1; i++) {
						            totArry= footerss[columnNames[i]];
						            if(fetchType=='2'){
						            	options[columnNames[i]] = totArry.toFixed(3);
						            }else{
									options[columnNames[i]] = totArry;
						      }
									$self.footerData('set', options);
						        }  
							 }
						      
						 },
			        onSortCol: function (index, idxcol, sortorder) {
				        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
			                    && this.p.colModel[this.p.lastsort].sortable !== false) {
			                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
			            }
			        }
					});
					
					//jQuery("#detail").jqGrid('setFrozenColumns');
					colModel = jQuery("#detail11").jqGrid('getGridParam', 'colModel');
				    $('#gbox_' + $.jgrid.jqID(jQuery("#detail11")[0].id) +
				        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
				        var cmi = colModel[i], colName = cmi.name;
				
				        if (cmi.sortable !== false) {
				            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
				        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
				            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
				        }
				    });	
			
				    $('#detail11').jqGrid('setGridHeight',(reportWindowHeight));
					jQuery("#detail11").jqGrid('navGrid','#pagerForDetail11',{edit:false,add:false,del:false,search:false,refresh:false})
					
					var gh='<s:property value="groupHeader"/>';
					$(gh.split("#")).each(function(k,val){  
						if(!isEmpty(val)){
							var cols=val.split("~");
							grparr.push({
							startColumnName:cols[0],
							numberOfColumns:cols[1],
							titleText:cols[2]
							});
						}
					});
			 		jQuery("#detail11").jqGrid('setGroupHeaders', {
	 			    	useColSpanStyle: true,  			    	
						  groupHeaders:grparr				
					});	
			
				
		 }
	 function loadCustomPopup(){
			$overlay = $('<div id="modOverlay"></div>');
			$modal = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
			$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
			$close = $('<button id="clsBtn" class="btnCls">X</button>');
			
			$modal.append($slider, $close);
			$('body').append($overlay);
			$('body').append($modal);
			$modal.hide();
			$overlay.hide();

			jQuery("#clsBtn").click(function () {
		    	$('#modalWin').css('margin-top','-230px');	
				$modal.hide();
				$overlay.hide();			
				$('body').css('overflow','visible');
			});
		}

	 function enablePopup(head, cont){
			$(window).scrollTop(0); 
			$('body').css('overflow','hidden');
			$(".bjqs").empty();		
			var heading='';
			var contentWidth='100%';
			if(head!=''){
				heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
				contentWidth='92%';
			}
			var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
			$(".bjqs").append('<li>'+heading+content+'</li>')
			$(".bjqs-controls").css({'display':'block'});
			$('#modalWin').css('margin-top','-200px');
			$modal.show();
			$overlay.show();
			$('#banner-fade').bjqs({
		        height      : 482,
		        width       : 600,
		        showmarkers : false,
		        usecaptions : true,
		        automatic : true,
		        nexttext :'',
		        prevtext :'',
		        hoverpause : false                                           

		    });
		}

	 function getColdStorageTransferDetails(lotNo,coldStorageCode,warehouseCode,chamberCode,floorCode,bayCode){
		 var heading ='<s:property value="%{getLocaleProperty('Cold Storage Stock Transfer Details')}" />';
			var content="<div id='coldStorageTransferData' class='smallmap'></div>";
			enablePopup(heading,content);
			var rowid = $('#detail11').jqGrid('getGridParam', 'selrow');	
			
		//alert(rowid);
		 var selectedLotCode=lotNo;
		 var coldStorageArry=coldStorageCode.split("-");
		 var coldSrotageCode=coldStorageArry[1].toString();
		 
		 var warehouseArry=warehouseCode.split("-");
		 var warehouseCode=warehouseArry[1].toString();
		 
		 var chamberArry=chamberCode.split("-");
		 var chamberCode=chamberArry[1].toString();
		 
		 var floorArry=floorCode.split("-");
		 var floorCode=floorArry[1].toString();
		 
		 var bayArry=bayCode.split("-");
		 var bayCode=bayArry[1].toString();
		 if(!isEmpty(selectedLotCode)){
				$.post("dynamicViewReport_populateColdStorageTransferDetailsData", {
					lotCode : selectedLotCode,
					coldStorageCode:coldSrotageCode,
					warehouseCode:warehouseCode,
					chamberCode:chamberCode,
					floorCode:floorCode,
					bayCode:bayCode,
					
				}, function(data) {
					console.log(data);
					var jsonData = JSON.parse(data);
					
					var table = $("<table/>").addClass("table table-bordered").attr({
						id : "coldStorageTransferDataList"
			});
			var thead = $("<thead/>");
			var tr = $("<tr/>");
			tr.append($("<th/>").append("Created Date"));
			tr.append($("<th/>").append("Receipt No"));		
			tr.append($("<th/>").append("Buyer"));	
			tr.append($("<th/>").append("Truck Id"));
			tr.append($("<th/>").append("Driver Name"));	
			tr.append($("<th/>").append("Po Number"));
			tr.append($("<th/>").append("Invoice Number"));			
			tr.append($("<th/>").append("No of Bags"));
			tr.append($("<th/>").append("Quantity"));
			table.append(thead);
			thead.append(tr);
			var tbody = $("<tbody/>");
				$.each(jsonData, function(index, value) {
					var tr2 = $("<tr/>");
					tr2.append($("<td/>").append(value.createdDate));	
					tr2.append($("<td/>").append(value.receiptNo));						
					tr2.append($("<td/>").append(value.buyer));		
					tr2.append($("<td/>").append(value.truckNo));		
					tr2.append($("<td/>").append(value.driverName));	
					tr2.append($("<td/>").append(value.poNo));	
					tr2.append($("<td/>").append(value.invoiceNo));	
					tr2.append($("<td/>").append(value.noOfBags));	
					tr2.append($("<td/>").append(value.qty));	
					tbody.append(tr2);
				});
			table.append(tbody);
			$('#coldStorageTransferData').append(table);
				
				});
			}
	 }
	 
	
	 function getPostdata(){
			var postData = {};
			//if(fetchType=='3'){
			$(".filterClass").each(function(){
				var name = $(this).attr('name');
				var value = $(this).val();
					if(value!=''){
					if($(this).hasClass("3")){
						postData[name] = " = '"+value+"'";
					}else if($(this).hasClass("4")){
						var dateVal  =value.split(" - ");
						postData[name] = " between  '"+ dateVal[0].trim() +"' and '"+ dateVal[1].trim()+" 23:59:59"+"'";
					}else if($(this).hasClass("1")){
						postData[name] = $(this).parent().find('#cond').val()+ " '"+value+"'";
					}else if($(this).hasClass("6")){
						postData[name] = " LIKE '%"+value+"%'";
					}else if($(this).hasClass("7")){
						postData[name] = " = FIND_IN_SET('"+value+"',"+name+")";
					}
					
				}
			});
			postData['branch_id'] = ' is not null';
			/* }else{
				$(".filterClass").each(function(){
					var name = $(this).attr('name');
					var value = $(this).val();
						if(value!=''){
						if($(this).hasClass("3")){
							postData[name] = "1~"+value;
						}else if($(this).hasClass("4")){
							var dateVal  =value.split(" - ");
							postData[name] = "2~"+ dateVal[0].trim() +"|"+ dateVal[1].trim();
						}else if($(this).hasClass("6")){
							postData[name] = "8~"+value
						}
						
					}
				});
				postData['branchId'] = '9~null';
			} */
			return JSON.stringify(postData);
		}
</script>
<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
<script src="js/maplabel-compiled.js?k=2.16"></script>