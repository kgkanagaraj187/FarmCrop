<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
</head>

<script src="plugins/bx-slider/js/jquery.bxslider.js"
	type="text/javascript"></script>

<script src="plugins/openlayers/OpenLayers.js"></script>
<script src="http://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>

<script type="text/javascript">
	jQuery(document).ready(function($) {
		var filterMapReport = '<s:property value="filterMapReport"/>';
		var postdataReport = '<s:property value="postdataReport"/>';
		var slider = $('.bxslider').bxSlider({
			pagerCustom : '#bx-pager',
			nextSelector : '#slider-next',
			prevSelector : '#slider-prev',
			infiniteLoop : false,
			hideControlOnEnd : false,
			startSlide : 0,

		});

		$(".bx-controls-directionLst").on("click", function() {
			var slideQty = $('#bx-pager').find('a').length;
			slider.goToSlide(slideQty - 1);
		});

		$(".bx-controls-directionFst").on("click", function() {
			slider.goToSlide(0);
		});

		var btns = $("#photoId").val();

		if (btns == undefined) {

			$(".sliderImgCntrlContainer").hide();
		}
	});
</script>

<body id="bdy">
	<div style="width: 100%;">
		<font color="red"><s:actionerror /></font>

		<s:form name="form">
			<s:hidden key="currentPage" />
			<s:hidden key="command" />

			<div class="row">
				<div class="flex-view-layout">
					<div class="fullwidth">
						<div class="flexWrapper">
							<div class="flexLeft appContentWrapper">
								<div class="col-md-6">
									<table class="table table-bordered aspect-detail fillform">
										<tr>
											<th colspan="3"><s:text name="info.inspection" /></th>
										</tr>
										<tr class="odd">
											<td width="30%"><s:text name="inspectionDate" /></td>
											<td width="40%"><s:date
													name="periodicInspection.inspectionDate"
													format="dd/MM/yyyy hh:mm:ss" /></td>
										</tr>
										<s:if test="getCurrentTenantId()!='lalteer'">
											<tr class="odd">
												<td width="30%"><s:text name="farmerIdName" /></td>
												<td width="40%"><s:property
														value="periodicInspection.farm.farmer.farmerIdAndName" /></td>

											</tr>
										</s:if>
										<s:else>
											<tr class="odd">
												<td width="30%"><s:text name="farmerCodeName" /></td>
												<td width="40%"><s:property
														value="periodicInspection.farm.farmer.farmerCodeAndName" /></td>

											</tr>
										</s:else>

										<tr class="odd">
											<td width="30%"><s:text name="farmIdName" /></td>
											<td width="40%"><s:property
													value="periodicInspection.farm.farmIdAndName" /></td>

										</tr>

										<tr class="odd">
											<td width="30%"><s:text name="cropName" /></td>
											<td width="40%"><s:property
													value="periodicInspection.cropCode" /></td>

										</tr>

										<tr class="odd">
											<td width="30%"><s:text name="fieldstaffIdName" /></td>
											<td width="40%"><s:property
													value="periodicInspection.createdUserName" />-<s:property
													value="%{mobileUserName}" /></td>

										</tr>

										<tr class="odd">
											<td width="35%"><s:text name="audioFile" /></td>
											<td width="65%"><s:if
													test="periodicInspection.farmerVoice!=null && periodicInspection.farmerVoice.length!=0">
													<s:text name="Yes" />
												</s:if> <s:else>
													<s:text name="No" />
												</s:else></td>
										</tr>
										<s:if
											test="getCurrentTenantId()!='efk' && getCurrentTenantId()!='avt'">
											<tr class="odd">
												<td width="30%"><s:text name="farmerOpinion" /></td>
												<td class="adrssClss break-word"><s:property
														value="periodicInspection.farmerOpinion" /></td>

											</tr>
										</s:if>
										<tr class="odd">
											<td width="30%"><s:text name="remarks" /></td>
											<td class="adrssClss break-word"><s:property
													value="periodicInspection.remarks" /></td>

										</tr>

									</table>
									<%-- <s:if test="getCurrentTenantId()!='chetna'"> --%>
									<table class="table table-bordered aspect-detail fillform">
										<tr>
											<th colspan="3"><s:text name="info.pest" /></th>
										</tr>
										<tr class="odd">
											<td width="30%"><s:text name="pestOccurence" /></td>
											<td width="40%"><s:if
													test="periodicInspection.pestProblemNoticed=='Y'">
													<s:text name="Yes" />
												</s:if> <s:else>
													<s:text name="No" />
												</s:else></td>

										</tr>
										<tr class="odd">
											<td width="30%"><s:text name="nameOfPest" /></td>
											<td width="40%"><s:property value="selectedPestName" /></td>
										</tr>
										<s:if test="currentTenantId=='fruitmaster'">
											<tr class="odd">
												<td width="30%"><s:text name="pestSymptom" /></td>
												<td width="40%"><s:property value="selectedPestSymptom" /></td>
											</tr>
										</s:if>

										<tr class="odd">
											<td width="30%"><s:text name="PestInfestationETL" /></td>
											<td width="40%"><s:if
													test="periodicInspection.pestInfestationETL=='Y'">
													<s:text name="Yes" />
												</s:if> <s:else>
													<s:text name="No" />
												</s:else></td>
										</tr>

										<s:if
											test="periodicInspection.pesticideRecommentedSet.size()>0">
											<s:if test="currentTenantId!='efk'">
												<tr>
													<td colspan="2">
														<div class="form-group">
															<div class="col-sm-12">
																<table width="100%" border="1">
																	<caption>
																		<s:property
																			value="%{getLocaleProperty('pesticideRecommended')}" />
																	</caption>
																	<tr>
																		<th><s:text name="tname" /></th>
																		<th><s:text name="tqty" /></th>
																		<%-- <th><s:text name="cocDone" /></th> --%>
																		<th><s:property
																				value="%{getLocaleProperty('uom')}" /></th>
																	</tr>
																	<s:iterator
																		value="periodicInspection.pesticideRecommentedSet"
																		status="incr">
																		<tr>
																			<td><s:text
																					name="pestNameValue.split(',')[%{#incr.index}]" /></td>
																			<td><s:property value="quantityValue" /></td>
																			<%-- <s:if test='cocDone=="Y"'>
																				<td><s:text name="Yes" /></td>
																			</s:if>
																			<s:else>
																				<td><s:text name="No" /></td>
																			</s:else> --%>
																			<td><s:text name="uom" /></td>
																		</tr>
																	</s:iterator>
																</table>
															</div>
														</div>
													</td>
												</tr>
											</s:if>
											<s:else>
												<tr>
													<td colspan="2">
														<div class="form-group">
															<div class="col-sm-12">
																<tr class="odd">
																	<td><s:property
																			value="%{getLocaleProperty('pesticideRecommended')}" /></td>
																	<td><s:iterator
																			value="periodicInspection.pesticideRecommentedSet"
																			status="incr">
																			<s:text
																				name="pestNameValue.split(',')[%{#incr.index}]" />
																		</s:iterator></td>

																</tr>
															</div>
														</div>
													</td>
												</tr>
											</s:else>
										</s:if>

										<%-- <tr class="odd">
					<td width="30%"><s:text name="monthOfPesticideApplication" /></td>
					<td width="40%"><s:property value="getYearMonthByPeriodicType(periodicInspection.monthOfPesticideApplication)" /></td>
				</tr> --%>
										<%-- <tr class="odd">
					<td width="30%"><s:text name="pestProblemSolved" /></td>
					<td width="40%">
					<s:if test="periodicInspection.pestProblemSolved=='Y'">
						<s:text name="Yes" />
					</s:if> 
					<s:else>
						<s:text name="No" />
					</s:else></td>
				</tr> --%>
									</table>
									<%-- </s:if> --%>
									<table class="table table-bordered aspect-detail fillform">
										<tr>
											<th colspan="3"><s:text name="info.disease" /></th>
										</tr>
										<tr class="odd">
											<td width="30%"><s:text name="diseaseOccurence" /></td>
											<td width="40%"><s:if
													test="periodicInspection.diseaseProblemNoticed=='Y'">
													<s:text name="Yes" />
												</s:if> <s:else>
													<s:text name="No" />
												</s:else></td>
										</tr>
										<tr class="odd">
											<td width="30%"><s:text name="nameOfDisease" /></td>
											<td width="40%"><s:property value="selectedDiseaseName" /></td>

										</tr>

										<s:if test="currentTenantId=='fruitmaster'">
											<tr class="odd">
												<td width="30%"><s:text name="diseaseSymptom" /></td>
												<td width="40%"><s:property
														value="selectedDiseaseSymptom" /></td>
											</tr>
										</s:if>
										<tr class="odd">
											<td width="30%"><s:text name="diseaseInfestationETL" /></td>
											<td width="40%"><s:if
													test="periodicInspection.diseaseInfestationETL=='Y'">
													<s:text name="Yes" />
												</s:if> <s:else>
													<s:text name="No" />
												</s:else></td>
										</tr>

										<s:if
											test="periodicInspection.fungicideRecommentedSet.size()>0">
											<s:if test="currentTenantId!='efk'">
												<tr>
													<td colspan="2">
														<div class="form-group">
															<div class="col-sm-12">
																<table width="100%" border="1">
																	<caption>
																		<s:property
																			value="%{getLocaleProperty('fungicideRecommended')}" />
																	</caption>
																	<tr>
																		<th><s:text name="tname" /></th>
																		<th><s:text name="tqty" /></th>
																		<th><s:property
																				value="%{getLocaleProperty('uom')}" /></th>
																	</tr>
																	<s:iterator
																		value="periodicInspection.fungicideRecommentedSet"
																		status="incr">
																		<tr>
																			<td><s:text
																					name="fungisideValue.split(',')[%{#incr.index}]" /></td>
																			<td><s:property value="quantityValue" /></td>
																			<td><s:text name="uom" /></td>
																		</tr>
																	</s:iterator>
																</table>
															</div>
														</div>
													</td>
												</tr>
											</s:if>
											<s:else>
												<tr>
													<td colspan="2">
														<div class="form-group">
															<div class="col-sm-12">
																<tr class="odd">
																	<td><s:property
																			value="%{getLocaleProperty('fungicideRecommended')}" /></td>
																	<td><s:iterator
																			value="periodicInspection.fungicideRecommentedSet"
																			status="incr">

																			<s:text
																				name="fungisideValue.split(',')[%{#incr.index}]" />


																		</s:iterator></td>

																</tr>
															</div>
														</div>
													</td>
												</tr>
											</s:else>
										</s:if>

										<%-- <tr class="odd">
					<td width="30%"><s:text name="diseaseProblemSolved" /></td>
					<td width="40%">
					<s:if test="periodicInspection.diseaseProblemSolved=='Y'">
						<s:text name="Yes" />
					</s:if> 
					<s:else>
						<s:text name="No" />
					</s:else></td>
				</tr> --%>
									</table>
								</div>


								<div class="col-md-6">
									<table class="table table-bordered aspect-detail fillform">
										<tr>
											<th colspan="3"><s:text name="info.crop" /></th>
										</tr>
										<s:if
											test="currentTenantId=='lalteerqa' || currentTenantId=='lalteer'">
											<tr class="odd">
												<td width="30%"><s:text name="survival" /></td>
												<td width="40%"><s:property
														value="periodicInspection.survivalPercentage" /></td>

											</tr>
										</s:if>
										<s:else>
											<s:if test="currentTenantId!='livelihood'">
												<tr class="odd">
													<td width="30%"><s:text
															name="%{getLocaleProperty('survival')}" /></td>
													<td width="40%"><s:property
															value="periodicInspection.survivalPercentage" /></td>

												</tr>
											</s:if>
										</s:else>
										<s:if test="currentTenantId!='livelihood'">
											<tr class="odd">
												<td width="30%"><s:text name="currentStatusofGrowth" /></td>
												<td width="40%"><s:property
														value="periodicInspection.currentStatusOfGrowth" /></td>

											</tr>
										</s:if>
										<s:if test="enableMultiProduct==0">
											<s:if test="getCurrentTenantId()!='pratibha'">
												<tr class="odd">
													<td><s:property
															value="%{getLocaleProperty('averageHeightMeter')}" /></td>
													<td><s:property
															value="periodicInspection.averageHeight" /></td>
												</tr>
												<tr class="odd">
													<td><s:property
															value="%{getLocaleProperty('averageGirthCm')}" /></td>
													<td><s:property
															value="periodicInspection.averageGirth" /></td>

												</tr>
											</s:if>
										</s:if>
										<s:if test="getCurrentTenantId()!='efk'">
											<tr class="odd">
												<td width="30%"><s:text
														name="activitieCarriedPostPreVisit" /></td>
												<td width="40%"><s:property value="selectedActivities" /></td>
											</tr>
										</s:if>
										<%-- <tr class="odd">
					<td width="30%"><s:text name="typeOfFertilizerApplied" /></td>
					<td width="40%"><s:property value="getCommaSeparatedValueFromSet(periodicInspection.fertilizerAppliedSet)"
						default="NA" /></td>
				</tr> --%>
										<s:if
											test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='efk' && getCurrentTenantId()!='avt'">
											<tr class="odd">
												<td width="30%"><s:text name="chemicalName" /></td>
												<td width="40%"><s:property
														value="periodicInspection.chemicalName" /></td>
											</tr>
										</s:if>
										<s:if test="getCurrentTenantId()=='avt'">
											<tr class="odd">
												<td><s:text name="cropProtectionPractice" /></td>
												<td><s:property
														value="periodicInspection.cropProtectionPractice" /></td>

											</tr>

											<tr class="odd">
												<td><s:text name="interPloughing" /></td>
												<td><s:property value="selectedInterPloughing" /></td>
											</tr>
										</s:if>
										<s:if
											test="getCurrentTenantId()!='efk' && getCurrentTenantId()!='avt'">
											<tr class="odd">
												<td width="30%"><s:text name="plantsReplanted" /></td>
												<td width="40%"><s:property
														value="periodicInspection.noOfPlantsReplanned" /></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="gapPlantDate" /></td>
												<td width="40%"><s:date
														name="periodicInspection.gapPlantingDate"
														format="dd/MM/yyyy" /></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="interPloughing" /></td>
												<td width="40%"><s:property
														value="selectedInterPloughing" /></td>
											</tr>
											<%-- <s:if test="currentTenantId=='agro' || currentTenantId=='pratibha' || currentTenantId=='lalteer' || currentTenantId=='crsdemo'"> --%>
											<tr class="odd">
												<td width="30%"><s:text name="cropRotation" /></td>
												<td width="40%"><s:if
														test='periodicInspection.cropRotation=="Y"'>
														<s:text name="Yes" />
													</s:if> <s:elseif test='periodicInspection.cropRotation=="N"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name="NA" />
													</s:else></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="temp" /></td>
												<td width="40%"><s:property
														value="periodicInspection.temperature" default="NA" /></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="rain" /></td>
												<td width="40%"><s:property
														value="periodicInspection.rain" default="NA" /></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="humidity" /></td>
												<td width="40%"><s:property
														value="periodicInspection.humidity" default="NA" /></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="windSpeed" /></td>
												<td width="40%"><s:property
														value="periodicInspection.windSpeed" default="NA" /></td>
											</tr>
										</s:if>
										<%-- </s:if> --%>
									</table>

									<table class="table table-bordered aspect-detail fillform">
										<s:if test="periodicInspection.fertilizerAppliedSet.size()>0">
											<tr>
												<th colspan="3"><s:text name="info.ferti" /></th>
											</tr>

											<tr>
												<td colspan="2">
													<div class="form-group">
														<div class="col-sm-12">
															<table width="100%" border="1">
																<caption>
																	<s:text name="fertilizerType" />
																</caption>
																<tr>
																	<th><s:text name="tname" /></th>
																	<th><s:text name="tqty" /></th>
																	<%-- 
																	<th><s:text name="cocDone" /></th> --%>
																	<th><s:property
																			value="%{getLocaleProperty('uom')}" /></th>
																</tr>
																<s:iterator
																	value="periodicInspection.fertilizerAppliedSet"
																	status="incr">
																	<tr>
																		<td><s:text
																				name="fertilizerValue.split(',')[%{#incr.index}]" /></td>
																		<td><s:property value="quantityValue" /></td>
																		<%-- <s:if test='cocDone=="Y"'>
																			<td><s:text name="Yes" /></td>
																		</s:if>
																		<s:else>
																			<td><s:text name="No" /></td>
																		</s:else> --%>
																		<td><s:text name="uom" /></td>
																	</tr>
																</s:iterator>
															</table>
														</div>
													</div>
												</td>
											</tr>
											<%-- <tr>
						<td><s:text name="monthOfFertilizerApplied" /></td>
						<td><s:property value="getYearMonthByPeriodicType(periodicInspection.monthOfFertilizerApplied)" /></td>
					</tr> --%>
										</s:if>
									</table>

									<table class="table table-bordered aspect-detail fillform">
										<s:if test="periodicInspection.typeOfManureSet.size()>0">
											<tr>
												<th colspan="3"><s:text name="info.manure" /></th>
											</tr>
											<tr>
												<td colspan="2">
													<div class="form-group">
														<div class="col-sm-12">
															<table width="100%" border="1">
																<caption>
																	<s:text name="manureType" />
																</caption>
																<tr>
																	<th><s:text name="name" /></th>
																	<th><s:property
																			value="%{getLocaleProperty('qty')}" /></th>
																	<%-- <th><s:text name="cocDone" /></th> --%>
																	<th><s:property
																			value="%{getLocaleProperty('uom')}" /></th>
																</tr>
																<s:iterator value="periodicInspection.typeOfManureSet"
																	status="incr">
																	<tr>
																		<td><s:text
																				name="manureValue.split(',')[%{#incr.index}]" /></td>
																		<td><s:property value="quantityValue" /></td>
																		<%-- <s:if test='cocDone=="Y"'>
																			<td><s:text name="Yes" /></td>
																		</s:if>
																		<s:else>
																			<td><s:text name="No" /></td>
																		</s:else> --%>
																		<td><s:text name="uom" /></td>
																	</tr>
																</s:iterator>
															</table>
														</div>
													</div>
												</td>
											</tr>
										</s:if>
									</table>
									<%-- <s:if test="getCurrentTenantId()!='pratibha'">
			<table class="table table-bordered aspect-detail fillform">
				<tr>
					<th colspan="3"><s:text name="info.intercrop" /></th>
				</tr>
				
				<tr class="odd">
					<td width="30%"><s:text name="isInterCrop" /></td>
					
					<td width="30%">
					<s:if test="periodicInspection.interCrop=='Y'">
						<s:text name="Yes" />
					</s:if> 
					<s:else>
						<s:text name="No" />
					</s:else>
					</td>
				</tr>

				<tr class="odd">
					<td width="30%"><s:text name="interCropName" /></td>
					<td width="40%"><s:property value="periodicInspection.nameOfInterCrop" /></td>
				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="yieldObtained" /></td>
					<td width="40%"><s:property value="periodicInspection.yieldObtained" /></td>

				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="expenditureIncurred" /></td>
					<td width="40%"><s:property value="periodicInspection.expenditureIncurred" /></td>

				</tr>
				<tr class="odd">
					<td width="30%"><s:text name="incomeGenerated" /></td>
					<td width="40%"><s:property value="periodicInspection.incomeGenerated" /></td>

				</tr>

				<tr class="odd">
					<td width="30%"><s:text name="netProfitOrLoss" /></td>
					<td width="40%"><s:property value="periodicInspection.netProfitOrLoss" /></td>

				</tr>
			</table>
			</s:if> --%>
									<s:if
										test="getCurrentTenantId()=='pratibha' && getBranchId()=='bci'">
										<table class="table table-bordered aspect-detail fillform">
											<tr>
												<th colspan="3"><s:text name="info.chemical" /></th>
											</tr>
											<tr class="odd">
												<td width="30%"><s:text name="landpreparationCompleted" /></td>
												<td width="40%"><s:if
														test='periodicInspection.landpreparationCompleted=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif
														test='periodicInspection.landpreparationCompleted=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>
											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="chemicalSpray" /></td>
												<td width="40%"><s:if
														test='periodicInspection.chemicalSpray=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif test='periodicInspection.chemicalSpray=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>

											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="monoOrImida" /></td>
												<td width="40%"><s:if
														test='periodicInspection.monoOrImida=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif test='periodicInspection.monoOrImida=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>

											</tr>


											<tr class="odd">
												<td width="30%"><s:text name="singleSprayOrCocktail" /></td>
												<td width="40%"><s:if
														test='periodicInspection.singleSprayOrCocktail=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif
														test='periodicInspection.singleSprayOrCocktail=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>

											</tr>

											<tr class="odd">
												<td width="30%"><s:text name="repetitionOfPest" /></td>
												<td width="40%"><s:if
														test='periodicInspection.repetitionOfPest=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif test='periodicInspection.repetitionOfPest=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>

											</tr>

											<tr class="odd">
												<td width="35%"><s:text name="nitrogenousFert" /></td>
												<td width="65%"><s:if
														test='periodicInspection.nitrogenousFert=="1"'>
														<s:text name="Yes" />
													</s:if> <s:elseif test='periodicInspection.nitrogenousFert=="2"'>
														<s:text name="No" />
													</s:elseif> <s:else>
														<s:text name=" " />
													</s:else></td>

											</tr>
											<tr class="odd">
												<td width="30%"><s:text name="cropSpacingLastYear" /></td>
												<td width="40%"><s:property
														value="periodicInspection.cropSpacingLastYear" /></td>

											</tr>
											<tr class="odd">
												<td width="30%"><s:text name="cropSpacingCurrentYear" /></td>
												<td width="40%"><s:property
														value="periodicInspection.cropSpacingCurrentYear" /></td>

											</tr>
											<tr class="odd">
												<td width="30%"><s:text name="weedingProp" /></td>
												<td width="40%"><s:property
														value="periodicInspection.weeding" /></td>

											</tr>
											<tr class="odd">
												<td width="30%"><s:text name="pickingProp" /></td>
												<td width="40%"><s:property
														value="periodicInspection.picking" /></td>

											</tr>
										</table>
									</s:if>


									<div style="width: auto; float: left;">

										<s:if test='periodicInspection.inspectionImageInfo!=null '>
											<ul class="bxslider"
												style="position: relative; width: auto !important;">

												<s:iterator var="insImages" status="headerstatus"
													value="inspectionImagesSet">

													<s:if
														test='#insImages.photo!=null && #insImages.photo.length > 0'>

														<li><s:hidden name="#insImages.id" id="photoId" />
															<tr>
																<label><s:text name="date" /> : <s:date
																		name="#insImages.photoCaptureTime"
																		format="dd/MM/yyyy hh:mm:ss " /></label>
															</tr>
															<tr>
																<label><s:text name="latitude" /> : <s:property
																		value="latitude" /> <s:text name="longitude" /> : <s:property
																		value="#insImages.longitude" /></label>
															</tr>

															<tr>
																<img
																	src="data:image/png;base64,<s:property value="#insImages.imageByteString"/>">
															</tr></li>

														<s:else>
															<img align="middle" width="150" height="150" border="0"
																class="noImgCls" src="img/no-image.png">
														</s:else>
													</s:if>
												</s:iterator>
											</ul>
											<div style="" class="sliderImgCntrlContainer">
												<span class="bx-controls-directionFst fl">first</span> <span
													id="slider-prev" class="fl"></span> <span id="slider-next"
													class="fl"></span> <span
													class="bx-controls-directionLst fl">last</span>
											</div>

										</s:if>
										<s:else>
											<div
												style="width: 450px; margin: 0 auto; height: 331px; background: #ccc; text-align: center;">
												<img src="images/noImg.png" class="noImgCls" />
											</div>
										</s:else>
										<div id="bx-pager">
											<s:iterator var="insImages" value="inspectionImagesSet"
												status="status">

												<s:if
													test='#insImages.photo!=null && #insImages.photo.length > 0'>
													<a data-slide-index="<s:property value="#status.index"/>"
														href="" />
													<img
														src="data:image/png;base64,<s:property value="#insImages.imageByteString"/>"
														width="50px" height="50px" />
													</a>
												</s:if>
											</s:iterator>
										</div>

									</div>
								</div>
								<div class="yui-skin-sam">
									<span id="cancel" class=""> <span class="first-child">
											<button type="button" onclick="onCancel();"
												class="back-btn btn btn-sts">
												<b><FONT color="#FFFFFF"><s:text
															name="back.button" /> </font></b>
											</button>
									</span>
									</span>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>


		</s:form>

		<s:form name="cancelform" action="periodicInspectionReport_list">
			<s:hidden key="currentPage" />
			<s:hidden name="filterMapReport" id="filterMapReport" />
			<s:hidden name="postdataReport" id="postdataReport" />
		</s:form>
	</div>

	<script type="text/javascript">
		function onCancel() {
			document.cancelform.submit();
		}
	</script>
</body>

