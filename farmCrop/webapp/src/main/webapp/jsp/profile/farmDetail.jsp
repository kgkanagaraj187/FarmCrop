<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
	
</head>
<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<s:hidden key="farm.id" id="farmId"  class="uId"/>
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
						<div class="aContent dynamic-form-con" id="farmDetailsAccordian">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.farmName')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmName" />
									&nbsp;
								</p>
							</div>
							
									<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.season')}" />
								</p>
								<p class="flexItem" name="farm.season">
									<s:property value="seasonList[farm.season]" />
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
							<s:if test="currentTenantId!='wilmar' && currentTenantId!='olivado'">
							
							
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.surveyNo')}" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.surveyNumber">
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
							</div></s:if>



							<%-- 	<s:if test="currentTenantId!='crs'">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.totalLandHectare" />
								</p>
								<p class="flexItem" name="landHectValues">
								<div id="landHectValues"></div>


							</div>
							</s:if> --%>

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
									<s:property value="farm.farmDetailedInfo.proposedPlantingArea" />
								</p>
							</div>
							
							<%-- 	<s:if test="currentTenantId!='crs'">
							<div class="dynamic-flexItem2 pratibhaHide">
								<p class="flexItem">
									<s:text
										name="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
								</p>
								<p class="flexItem" name="plantHectValues">
								<div id="plantHectValues"></div>
								</p>

							</div></s:if> --%>

							<s:if test="currentTenantId=='welspun' || currentTenantId=='ecoagri'">
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
							</div></s:if>

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
									<s:property value="%{getLocaleProperty('farm.landGradient')}" />
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
							<s:if test="currentTenantId!='simfed' && currentTenantId!='wub'">
							<div class="dynamic-flexItem2 waterHarvest">
								<p class="flexItem">
									<s:text name="farm.waterHarvest" />
								</p>
								<p class="flexItem" name="farm.waterHarvest">
									<s:property value="waterHarvests" />
								</p>
							</div>
							<s:if test="currentTenantId!='welspun'&& currentTenantId!='wub'">
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
										<s:property value="%{getLocaleProperty('processingActivity')}" />
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

							<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.regYear" />
								</p>
								<p class="flexItem" name="farm.regYear">
									<s:property value="farm.farmDetailedInfo.regYear" />
								</p>
							</div> --%>

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
									<s:property value="%{getLocaleProperty('farm.inputApplied')}" />
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
					<s:if test="currentTenantId!='wilmar' && currentTenantId!='griffith' ">
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
					<%-- <div class="aPanel conversionInfo">
						<div class="aTitle">
							<h2>
								<s:text name="info.conversion" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmConversionAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con"
							id="farmConversionAccordian">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.lastDateofChemicalApply" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property
										value="farm.farmDetailedInfo.lastDateOfChemicalApplication" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalLands')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.conventionalLand" />
								</p>
							</div>


							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.fallowLand')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.fallowOrPastureLand" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalCrops')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.conventionalCrops" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalEstimatedYields')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property
										value="farm.farmDetailedInfo.conventionalEstimatedYield" />
								</p>
							</div>

						</div>
					</div> --%>

					<%-- <div class="aPanel conversionStatus" style="width: 100%">
						<div class="aTitle">
							<h2>
								<s:property value="%{getLocaleProperty('info.ics')}" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmICSAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con"
							id="farmConversionAccordian">
							<s:iterator value="farm.farmICSConversion">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="conversionStatus" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:text name='%{"icsStatus"+icsType}' />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('inspectionDate')}" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<td><s:property value="inspectionDateString" /></td>
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="nameOfInspector" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:property value="inspectorName" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('qualified')}" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:text name='%{"inspe"+qualified}' />
									</p>
								</div>
								<s:iterator value="farm.farmICSConversion">
									<s:if test='qualified=="2"'>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('sanctionReason')}" />
											</p>
											<p class="flexItem" name="farm.farmName">

												<s:property value="sanctionreason" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('durationOfSanction')}" />
											</p>
											<p class="flexItem" name="farm.farmName">

												<s:property value="sanctionDuration" />
											</p>
										</div>
									</s:if>
								</s:iterator>
							</s:iterator>
						</div>
					</div> --%>

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
							<%-- <s:if test='farm.soilTesting=="1"'>
								<s:if test="farm.docUpload.size()>0">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="fileName" />
										</p>
										<p class="flexItem">
											<s:text name="download" />
										</p>
									</div>
									<div>
									<s:iterator value="farm.docUpload">
										<tr>
											<td align="left"><s:property value="name" /></td>
											<td align="center">&nbsp;&nbsp;
												<button type="button" class="btn btn-default"
													aria-label="Left Align"
													onclick='downloadDocument(<s:property value="id" />)'>
													<span class="glyphicon glyphicon-download-alt"
														aria-hidden="true"></span>
												</button>

											</td>
										</tr>
									</s:iterator>
									</div>
								</s:if>
							</s:if> --%>
						</div>
					</s:if>


				
												</div>
												<div class="dynamicFieldsRender"></div>
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
							
							<%-- <span id="update" class=""><span class="first-child actStatus">
									<button type="button" class="edit-btn btn btn-success"
										onclick="onUpdateActPlan()">
										<FONT color="#FFFFFF"> <b><s:text
													name="followup.button" /></b>
										</font>
									</button>
							</span></span> --%>
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
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button>
						</span></span>
					</div>
				</div>

			</div>
			<div class="flexRight appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2>
						 <s:property
								value="%{getLocaleProperty('farm.photo')}" /> 
					</h2>
					<div id="" class="farmer-photo ">
						<s:if test='farmImageByteString!=null && farmImageByteString!=""'>
							<img border="0"
								src="data:image/png;base64,<s:property value="farmImageByteString"/>">
						</s:if>
						<s:else>
							<img align="middle" border="0" src="img/no-image.png">
						</s:else>
					</div>
						 <s:if test="getCurrentTenantId()=='welspun'">	
						<div class="flexItem flex-layout flexItemStyle">
							<b><s:text name="farmer.latitude" />: &nbsp;</b>
							<s:property value="farm.latitude" />
							 &nbsp;&nbsp;&nbsp;&nbsp;<b><s:text name="farmer.longitude" />: &nbsp;</b>
							<s:property value="farm.longitude" />	
							&nbsp;&nbsp;&nbsp;&nbsp;<b><s:text name="farm.photoCaptureTime" />: &nbsp;</b>
							<s:date name="farm.photoCaptureTime" />		
							</div>
						</s:if>		
					<div class="clear"></div>
					<s:hidden id="farmCordinates" value="%{jsonObjectList}" />
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
					
					</s:if>
				</div>

			</div>

		</div>
	</div>

</div>

<s:form name="updateform" action="farm_update.action">
	<s:hidden name="id" value="%{farm.id}" />
	<s:hidden key="currentPage" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farm.farmer.sangham}" />
	<s:hidden name="tabIndexFarmer" value="%{tabIndex}" />
	<s:hidden id="isFollowUp" name="isFollowUp" />
</s:form>
<s:form name="updateActform" action="farm_updateActPlan.action">
	<s:hidden name="id" value="%{farm.id}" />
	<s:hidden key="currentPage" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farm.farmer.sangham}" />
	<s:hidden name="tabIndexFarmer" value="%{tabIndex}" />
	<s:hidden id="isFollowUp" name="isFollowUp" />
</s:form>
<s:form name="deleteform" action="farm_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farm.farmer.sangham}" />
	<s:hidden name="tabIndexFarmer" value="%{tabIndexFarmerZ}" />
	<s:hidden value="%{farm.farmer.id}" name="farmerUniqueId" />
</s:form>
<s:form name="cancelform" action="farmer_detail.action">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerUniqueId}" />
	<s:hidden name="tabIndex" value="%{tabIndexFarmerZ}" />
	<s:hidden name="currentPage" />
</s:form>
<s:form name="redirectform" action="farmInventory_list.action">
	<s:hidden name="farmId" value="%{farm.id}" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="redirectforms" action="animalHusbandary_list.action">
	<s:hidden name="farmId" value="%{farm.id}" />
	<s:hidden key="currentPage" />


</s:form>

<s:form id="audioFileDownload" action="farm_populateDownload.action">
	<s:hidden id="loadId" name="id" />
</s:form>
<s:form id="documentDownload" action="farm_populateDownloadFile">
	<s:hidden id="fileId" name="documentFileId" />
</s:form>

	<script src="js/dynamicComponents.js?v=20.35"></script>
<script>
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	var id='<s:property value="id"/>';
		var totalLand='<s:property value="farm.farmDetailedInfo.totalLandHolding" />';
	

	jQuery(document)
			.ready(
					function() {
						/* if(window.location.href.indexOf("?id=") < 0) {
							var urll =window.location.href +"?id="+id;
							 window.location.replace(urll);
							}	 
							 */
							 /* alert(tenant)
							 if(tenant!="symrise"){	
								 $('.actStatus').hide(); 
							 }
						var actStatuss = '<s:property value="actStatuss"/>';
						alert(actStatuss)
						if(actStatuss==1||actStatuss==2){
						$('.actStatus').hide();
						} */

						if(tenant=="fruitmaster"){				
	
							$("#totalLandKanal").text(parseFloat(totalLand)*8);
							
						}
							jQuery(".plantArea").show();
					
						if(tenant=="pratibha"){
                			$(".pratibhaHide").hide();
                		}
						/* if(tenant=="welspun"){
                			$(".welspunHide").hide();
                		} */
						loadFarmMap();

						var selectedVehicle = '<s:property value="farm.farmDetailedInfo.farmIrrigation" />';

						if (selectedVehicle != null
								&& selectedVehicle.trim() != "") {
							var value = selectedVehicle.toString().trim();
							if (value.includes(',')) {
								var values1 = value.toString().split(",");
								$.each(values1, function(i, e) {
									if (e.trim() == 2) {
										jQuery(".otherValueDiv").show();
									} else {
										jQuery(".otherValueDiv").hide();
									}
								});
							} else {
								if (value == 1) {
									jQuery(".otherValueDiv").hide();
								} else if (value == 2) {
									jQuery(".otherValueDiv").show();
								}
							}

						}

						
							/* var landHecVal = $("#landInAcres").val() * 0.40468564224;
							document.getElementById('landHectValues').innerHTML = landHecVal
									.toFixed(5); */

							var plantHect = $("#plantingArea").val() * 0.40468564224;
							/* document.getElementById('plantHectValues').innerHTML = plantHect
									.toFixed(5); */
					
						
									hideFields();
			                    	 
			                    	jQuery.post("farm_populateHideFn.action", {}, function(result) {
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
						
						var irrigationTypeVal = '<s:property value="irrigationSourceDetail" />';
						if(isEmpty(irrigationTypeVal)){
							hideByEleName('farm.farmIrrigation');
						}
						if(tenant=='wilmar'){
							var farmer = '<s:property value="farm.farmer.isCertifiedFarmer" />';
							if(farmer==0){
								jQuery(".organicStatusDiv").hide();
							}else{
								jQuery(".organicStatusDiv").show();
							}
						}
						
						  renderDynamicDetailFeilds();

					});

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
	function loadFarmMap() {		
		try {
			var farmCoordinate = jQuery("#farmCordinates").val();
			var landArea = JSON.parse(farmCoordinate);			
			loadMap('map', '<s:property value="farm.latitude"/>',
					'<s:property value="farm.longitude"/>', landArea);
			addExistingMarkers(landArea);
		} catch (err) {
			console.log(err);
		}
	}
	var map;
	function initMap() {
		map = new google.maps.Map(document.getElementById('map'), {
			center : {
				lat : 9.081999,
				lng : 8.675277
			},
			zoom : 3,
			mapTypeId: google.maps.MapTypeId.HYBRID,
		});
	}
	  function getTxnType(){
  		
	    	 return "359";
	    	 }
	  
	  function getBranchIdDyn(){
	     	return '<s:property value="farm.farmer.branchId"/>';
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
					if(v.lat != undefined && v.lon != undefined){
					cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
					
					}
				/* 	marker = new google.maps.Marker({
						position : new google.maps.LatLng(parseFloat(v.lat),
								parseFloat(v.lon)),
						map : map
					}); */
				});
				
				
				
				 var plotting = new google.maps.Polygon({
			          paths: cords,
			          strokeColor: '#FF0000',
			          strokeOpacity: 0.8,
			          strokeWeight: 2,
			          fillColor: '#E7D874',
			          fillOpacity: 0.35
			        });
				 plotting.setMap(map);
				
				 /* bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
				 map.fitBounds(bounds);
				 
				 var listener = google.maps.event.addListener(map, "idle", function () {
					  	map.setZoom(20);
					    google.maps.event.removeListener(listener);
					}); */
					
					
				 loc = new google.maps.LatLng(cords[0].lat, cords[0].lng);
				 bounds.extend(loc);
				 map.fitBounds(bounds);      // auto-zoom
				 //map.panToBounds(bounds);    // auto-center
				 
				   var arType = '<s:property value="%{getAreaType()}" />';
					  var cultiArea = '<s:property value="%{farm.farmDetailedInfo.totalLandHolding}" />';
					
				 
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

	//Variable relate to loading Custom Popup
	var $overlay;
	var $modal;
	var $slider;
	function loadCustomPopup() {
		$overlay = $('<div id="modOverlay"></div>');
		$modal = $('<div id="modalWin" class="ui-body-c"></div>');
		$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
		$close = $('<button id="clsBtn" class="btnCls">X</button>');
		$modal.append($slider, $close);
		$('body').append($overlay);
		$('body').append($modal);
		$modal.hide();
		$overlay.hide();
		jQuery("#clsBtn").click(function() {
			$('#modalWin').css('margin-top', '-230px');
			$modal.hide();
			$overlay.hide();
			$('body').css('overflow', 'visible');
		});
	}

	function onCancel() {
		document.cancelform.submit();
	}
	function onUpdate() {
		document.updateform.id.value = document.getElementById('farmId').value;
		document.updateform.currentPage.value = document.form.currentPage.value;
		document.updateform.submit();
	}
	
	function onUpdateActPlan() {
		document.updateActform.id.value = document.getElementById('farmId').value;
		document.updateActform.isFollowUp.value = "1";
		document.updateActform.currentPage.value = document.form.currentPage.value;
		document.updateActform.submit();
	}
	
	function hideFields(){
		 var app = $(".aPanel");
		 $(app).addClass("hide");
		 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
		 	 $(this).addClass("hide");
		 });
		  
		}

	function onDelete() {
		if (confirm('<s:text name="confirm.deleteFarm"/> ')) {
			document.deleteform.id.value = document.getElementById('farmId').value;
			document.deleteform.currentPage.value = document.form.currentPage.value;
			document.deleteform.submit();
		}
	}
	
	function downloadDocument(id){
		$("#fileId").val(id);	
		//alert("ldsdg:"+$("#fileId").val(id));
		$('#documentDownload').submit();
	}
	
	function addExistingMarkers(jsonData){
		
		var neighbouringDetails;
		$.each( jsonData, function( k, v ) {
			
			 if(v.neighbouringDetails_farm != undefined){
				  neighbouringDetails = v.neighbouringDetails_farm;
				  
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
<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
	<script src="js/maplabel-compiled.js?k=2.16"></script>