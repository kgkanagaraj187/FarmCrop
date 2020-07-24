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
				<s:hidden key="farm.id" id="farmId" class="uId" />

				<s:form name="form">
					<s:hidden key="currentPage" />
					<s:hidden key="id" />
					<s:hidden key="farm.id" />
					<s:hidden value="dateOfInspection" />
					<s:hidden value="lastDateChemical" />
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

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.farmerName" />
								</p>
								<p class="flexItem" name="farm.farmer.firstName">
									<s:property value="farm.farmer.firstName" />
									&nbsp;
									<s:property value="farm.farmer.lastName" />
								</p>
							</div>

							<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.farmName')}" />
								</p>
								<p class="flexItem" name="farm.farmNameDet">
									<s:property value="farm.farmName" />
									&nbsp;
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.farmerName" />
								</p>
								<p class="flexItem" name="farm.farmNameDet">
									<s:property value="farm.farmer.firstName" />
									&nbsp;
									<s:property value="farm.farmer.lastName" />
								</p>
							</div> --%>

							<div class="dynamic-flexItem2">
								<p class="flexItem surveyNo">
									<s:property value="%{getLocaleProperty('farm.surveyNo')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.surveyNumber">
									<s:property value="farm.farmDetailedInfo.surveyNumber" />
								</p>
							</div>
							<s:if test="currentTenantId=='welspun'">

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('fieldStatus')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.icsType">
										<s:if test="farm.farmIcsConv.icsType!=null">
											<s:text name='%{"icsStatus"+farm.farmIcsConv.icsType}' />
										</s:if>
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
									<s:text name="farm.farmAddress" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.farmAddress">
									<s:property value="farm.farmDetailedInfo.farmAddress" />
								</p>
							</div>
							<s:if test="currentTenantId!='susagri'">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('farm.totalLand')}" />
										<s:if test="currentTenantId!='symrise'">(
									<s:property value="%{getAreaType()}" />
									)</s:if>

								</p>
								<p class="flexItem"
									name="farm.farmDetailedInfo.totalLandHolding">
									<s:property value="farm.farmDetailedInfo.totalLandHolding" />
								</p>
							</div>
							</s:if>
<s:if test="currentTenantId=='fruitmaster'">
<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('farm.totalLand.kanal')}" />
									
								</p>
								<p class="flexItem" name="totalLandKanalName">
									<span id="totalLandKanal">0.00</span>
								</p>
							</div>



</s:if>
							<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('farm.totalLandHectare')}" />
								</p>
								<p class="flexItem" name="landHectValues">
								<div id="landHectValues"></div>


							</div> --%>

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
							<s:if test="currentTenantId=='fruitmaster'">
			<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('farm.proposedPlantingArea.kanal')}" />
									
								</p>
								<p class="flexItem" name="proposedPlantingAreaName">
									<span id="proposedPlantingAreaKanal"></span>
								</p>
							</div></s:if>

							<%-- <div class="dynamic-flexItem2 pratibhaHide">
								<p class="flexItem">
									<s:text
										name="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
								</p>
								<p class="flexItem" name="plantHectValues">
								<div id="plantHectValues"></div>
								</p>

							</div> --%>

							<div class="dynamic-flexItem2 ownLand">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('ownLand')}" />
								</p>
								<p class="flexItem" name="farm.ownLand">
									<s:property value="farm.ownLand" />
								</p>
							</div>
							<s:if test="currentTenantId=='symrise'">
							<div class="dynamic-flexItem2 hide noOfWineOnPlotLabel">
									<p class="flexItem"> <s:property
											value="%{getLocaleProperty('farm.noOfWineOnPlot')}" />
									</p>
									<p class="flexItem" name="farm.ownLand">
										<s:property value="noOfWineOnPlot" />
									</p>
								</div>
								</s:if>
							<s:if test="currentTenantId!='olivado'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('leasedland')}" />
									</p>
									<p class="flexItem" name="farm.leasedLand">
										<s:property value="farm.leasedLand" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('irriLand')}" />
								</p>
								<p class="flexItem" name="farm.irrigationLand">
									<s:property value="farm.irrigationLand" />
								</p>
							</div>
							<s:if test="currentTenantId!='welspun'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.landOwned')}" />
									</p>
									<s:if test="currentTenantId =='livelihood'">
									<p class="flexItem" name="farm.farmDetailedInfo.farmOwned">
										<s:property value="farmOwnedDetail" />
									</p>
									</s:if><s:else>
									<p class="flexItem" name="selectedFarmOwned">
										<s:property value="farmOwnedDetail" />
									</p>
									</s:else>
								</div>
							</s:if>
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.landGradient')}" />
								</p>
								<p class="flexItem" name="selectedGradient">
									<s:property value="landGradientDetail" />
								</p>
							</div>
							
							
								<s:if test="currentTenantId!='susagri'">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.appRoad')}" />
								</p>
								<p class="flexItem" name="selectedRoad">
									<s:property value="selectedRoadString" />
								</p>
							</div>

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
							
							<s:if test="currentTenantId!='susagri'">
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
							</s:if>
							
							<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.landGradient')}" />
								</p>
								<p class="flexItem" name="selectedGradient">
									<s:property value="landGradientDetail" />
								</p>
							</div> --%>
							
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farmRegNo')}" />
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
							<s:if
								test="currentTenantId!='cofBoard' && currentTenantId!='wilmar' && currentTenantId!='welspun'">
								<div class="dynamic-flexItem2 distanceProcessingUnit ">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
									</p>
									<p class="flexItem" name="farm.distanceProcessingUnit">
										<s:property value="farm.distanceProcessingUnit" />
									</p>
								</div>
								</s:if>
							<s:if test="currentTenantId=='cofBoard'">
							<div class="dynamic-flexItem2 distanceProcessingUnit">
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
							</s:if>
							<div class="dynamic-flexItem2 organicStatusDiv">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('organicStatus')}" />
								</p>

								<%-- <p class="flexItem" name="farm.farmIcsConv.organicStatus">
									<s:property value="farm.organicStatus" />
								</p> --%>
								<%-- <p><s:property value="farm.farmIcsConv.organicStatus" /></p> --%>
								<p class="flexItem" name="farm.farmIcsConv.organicStatus">
									<s:if
										test="farm.farmIcsConv.organicStatus==0 || farm.farmIcsConv.organicStatus==1 || farm.farmIcsConv.organicStatus==2">
										<%-- <s:text name="inprocess" /> --%>
										<s:property value="%{getLocaleProperty('inprocess')}" />
									</s:if>
									<s:else>
										<s:if test="farm.farmIcsConv.organicStatus==3">
											<s:property value="%{getLocaleProperty('alrdyCertified')}" />
										</s:if>
										<s:else>
											<s:property value="%{getLocaleProperty('conversion')}" />
										</s:else>
									</s:else>
								</p>
							</div>

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
								<p class="flexItem" name="farm.village.name">
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
           <s:if test="currentTenantId!='griffith'">
					<div class="aPanel soilIrrigationInfo">
						<div class="aTitle">
							<h2>
								<s:property value="%{getLocaleProperty('info.soil')}" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmSoilAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con" id="farmSoilAccordian">
							<div class="dynamic-flexItem2 soilType">
								<p class="flexItem">
								<s:property value="%{getLocaleProperty('farm.soilType')}" />
								</p>
								<p class="flexItem" name="selectedSoilType">
									<s:property value="soilTypeDetail" />
								</p>
							</div>
							
							<s:if test="currentTenantId!='susagri'">
							<div class="dynamic-flexItem2 soilTexture">
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
	</s:if>
							
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.farmIrrigationSource')}" />
								</p>
								<p class="flexItem" name="selectedIrrigation">
									<s:property value="farmIrrigationDetail" />
								</p>
							</div>
							
							<%-- <s:if test="farm.farmDetailedInfo.farmIrrigation==2"> --%>
							<div class="dynamic-flexItem2 hideIrrigationType">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.farmIrrigationType')}" />
								</p>
								<p class="flexItem" name="selectedIrrigationSource">
									<s:property value="irrigationSourceDetail" />
								</p>
							</div>
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
							<%-- </s:if> --%>
							<s:if test="farm.farmDetailedInfo.irrigationSource==99">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farm.irrigatedOther" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.irrigatedOther">
										<s:property value="farm.farmDetailedInfo.irrigatedOther" />
									</p>
								</div>
							</s:if>
							<s:if test="currentTenantId!='welspun'">
							<%-- <s:if test="currentTenantId!='susagri'"> --%>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farm.methodOfIrrigation" />
									</p>
									<p class="flexItem" name="selectedMethodOfIrrigation">
										<s:property value="methodOfIrrigationDetail" />
									</p>
								</div>
								<%-- </s:if> --%>
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
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.inputSource')}" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.inputSource">
										<s:property value="selectedInputSource" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('activitiesInCoconutFarming')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.activitiesInCoconutFarming">
										<s:property
											value="farm.farmDetailedInfo.activitiesInCoconutFarming" />
									</p>
								</div>
							</s:if>
							<s:if test="currentTenantId=='olivado'">

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

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.fullTimeWorkersCount">
										<s:property value="farm.farmDetailedInfo.fullTimeWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('presenceOfBananaTrees')}" />
									</p>
									<p class="flexItem" name="farm.presenceBananaTrees">
										<s:property value="presenceOfBanana" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.partTimeWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.partTimeWorkersCount">
										<s:property value="farm.farmDetailedInfo.partTimeWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.seasonalWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.seasonalWorkersCount">
										<s:property value="farm.farmDetailedInfo.seasonalWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.parallelProduction')}" />
									</p>
									<p class="flexItem" name="farm.parallelProd">
										<s:property value="parallelProduction" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.lastDateOfChemicalApplication">
										<s:property
											value="farm.farmDetailedInfo.lastDateOfChemicalApplication" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.lastDateofOrganicUnit')}" />
									</p>
									<p class="flexItem" name="farm.inputOrganicUnit">
										<s:property value="farm.inputOrganicUnit" />
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
								<p class="flexItem" name="farm.landmark">
									<s:property value="farm.landmark" />
								</p>
							</div>
						</div>
					</div>

					<div class="aPanel farmLabourInfo">
						<div class="aTitle">
							<h2>
								<s:text name="info.labour" />
								<div class="pull-right">
									<a class="aCollapse" href="#labourAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con" id="farmLandMarkAccordian">

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.fullTimeWorkersCount">
									<s:property value="farm.farmDetailedInfo.fullTimeWorkersCount" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.partTimeWorkersCount')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.partTimeWorkersCount">
									<s:property value="farm.farmDetailedInfo.partTimeWorkersCount" />
								</p>
							</div>
							<s:if test="currentTenantId!='wilmar'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.seasonalWorkersCount')}" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.seasonalWorkersCount">
										<s:property value="farm.farmDetailedInfo.seasonalWorkersCount" />
									</p>
								</div>
							</s:if>

						</div>
					</div>
					<div class="aPanel conversionInfo">
						<div class="aTitle">
							<h2>

								<s:property value="%{getLocaleProperty('info.conversion')}" />
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
								<p class="flexItem" name="farm.farmDetailedInfo.lastDateOfChemicalApplication">
									<s:property
										value="farm.farmDetailedInfo.lastDateOfChemicalApplication" />
								</p>
							</div>
							<s:if test="currentTenantId=='welspun' ">
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
										<s:property
											value="%{getLocaleProperty('farm.waterSourceList')}" />
									</p>
									<p class="flexItem" name="farm.waterSource">
										<s:property value="selectedWaterSource" />
									</p>
								</div>
							</s:if>
							<s:if test="currentTenantId!='iccoa' && currentTenantId!='susagri'">
								<s:if test="currentTenantId!='welspun' ">
								
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farm.conventionalLands')}" />
										</p>
										<p class="flexItem" name="farm.farmDetailedInfo.conventionalLand">
											<s:property value="farm.farmDetailedInfo.conventionalLand" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.fallowLand')}" />
										</p>
										<p class="flexItem" name="farm.farmDetailedInfo.fallowOrPastureLand">
											<s:property value="farm.farmDetailedInfo.fallowOrPastureLand" />
										</p>
									</div>
								
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farm.conventionalCrops')}" />
										</p>
										<p class="flexItem" name="farm.farmDetailedInfo.conventionalCrops">
											<s:property value="farm.farmDetailedInfo.conventionalCrops" />
										</p>
									</div>
								</s:if>
									<s:if test="currentTenantId!='simfed' && currentTenantId!='cofBoard' && currentTenantId!='wub'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.conventionalEstimatedYields')}" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.conventionalEstimatedYield">
										<s:property
											value="farm.farmDetailedInfo.conventionalEstimatedYield" />
									</p>
								</div>
								</s:if>
							</s:if>
						</div>
					</div>

					<div class="aPanel fieldHistoryInfo">
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
								<p class="flexItem" name="farm.farmDetailedInfo.fieldName">
									<s:property value="farm.farmDetailedInfo.fieldName" />
								</p>
							</div>
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.fieldCrop')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.fieldCrop">
									<s:property value="farm.farmDetailedInfo.fieldCrop" />
								</p>
							</div>


							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.fieldArea')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.fieldArea">
									<s:property value="farm.farmDetailedInfo.fieldArea" />
								</p>
							</div>


							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.inputApplied')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.inputApplied">
									<s:property value="farm.farmDetailedInfo.inputApplied" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.quantity')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.quantityApplied">
									<s:property value="farm.farmDetailedInfo.quantityApplied" />
								</p>
							</div>
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
								</p>
								<p class="flexItem" name="farm.farmDetailedInfo.lastDateOfChemicalString">
									<s:property
										value="farm.farmDetailedInfo.lastDateOfChemicalString" />
								</p>
							</div>
						</div>
					</div>


					<s:if test="currentTenantId!='livelihood'">
					<div class="aPanel conversionStatus" style="width: 100%">
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
								<s:if test="currentTenantId!='welspun'">
								<div class="dynamic-flexItem2 conversionStatus">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('conversionStatus')}" />
										</p>
										<p class="flexItem" name="certificationType">
											<s:if test="icsType!=null">
												<s:property value="certificationType" />
												
											</s:if>
										</p>
									</div>
								</s:if>
								<%-- <s:if test="currentTenantId=='susagri'">
								<div class="dynamic-flexItem2 inspType">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('inspType')}" />
									</p>
									<p class="flexItem" name="inspType">

										<td><s:property value="inspType" /></td>
									</p>
								</div>
								</s:if> --%>
								<s:if test="currentTenantId!='susagri'">
								<div class="dynamic-flexItem2 conversionStatus">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farminspectionDate')}" />
									</p>
									<p class="flexItem" name="inspectionDateString">

										<td><s:property value="inspectionDateString" /></td>
									</p>
								</div>
								<div class="dynamic-flexItem2 conversionStatus">
									<p class="flexItem">
										<s:text name="nameOfInspector" />
									</p>
									<p class="flexItem" name="inspectorName">

										<s:property value="inspectorName" />
									</p>
								</div>
								
							<div class="dynamic-flexItem2 conversionStatus">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('certType')}" />
									</p>
									<p class="flexItem" name="scopeName">
										<s:property value="scopeName" />

										<%-- <s:property value="farm.farmIcsConv.scope" /> --%>
									</p>
								</div>

								<div class="dynamic-flexItem2 conversionStatus">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('qualified')}" />
									</p>
									<p class="flexItem" name="qualified">
										<s:text name='%{"inspe"+qualified}' />

										<%-- <s:if test="icsType!=null">
										<s:text name='%{"inspe"+qualified}' />
									</s:if> --%>
									</p>
								</div>
								</s:if>
								<s:iterator value="farm.farmICSConversion">
									<s:if test='qualified=="2"'>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('sanctionReason')}" />
											</p>
											<p class="flexItem" name="sanctionreason">

												<s:property value="sanctionreason" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('durationOfSanction')}" />
											</p>
											<p class="flexItem" name="sanctionDuration">

												<s:property value="sanctionDuration" />
											</p>
										</div>
									</s:if>
								</s:iterator>
							</s:iterator>
						</div>
					</div>
					</s:if>

					<s:if test="currentTenantId=='olivado'">
					
					
					 	<div class="aPanel auditInfo">
							<div class="aTitle">
								<h2>
									<s:text name="info.auditDetails" />
									<div class="pull-right">
										<a class="aCollapse" href="#auditDetailsAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="auditDetailsAccordian">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.presenceHiredLabour')}" />
									</p>
									<p class="flexItem" name="farm.presenceHiredLabour">
										<s:property value="hiredLabours" />
									</p>
								</div>
				<%-- 				<div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.riskCategory')}" />
									</p>
									<p class="flexItem" name="farm.riskCategory">
										<s:property value="riskCategory" />
									</p>
								</div> --%>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('dateOfInternalAudit')}" />
									</p>
									<p class="flexItem" name="farm.dateOfInternalAudit">
										<s:property value="farm.farmIcsConv.inspectionDateString" />
									</p>
								</div>
					<%-- 			<div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('nameOfInspector')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.inspectorName">
										<s:property value="farm.farmIcsConv.inspectorName" />
									</p>
								</div> --%>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('producerStatus')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.scope">
										<s:property value="scopeName" />
									</p>
								</div>
								<%-- <div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('status')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.icsType">
										<s:text name='%{"icsStatus"+farm.farmIcsConv.icsType}' />
									</p>
								</div> --%>
								<%-- <div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.leasedLand')}" />
									</p>
									<p class="flexItem" name="farm.leasedLand">
										<s:property value="farm.leasedLand" />
									</p>
								</div> --%>
							</div>
						</div>
						
						
						<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.treeDetails')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#treeDetailsAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="treeDetailsAccordian">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">
									<tr class="odd">
										<%-- <th width="5%"><s:text name="s.no" /></th> --%>
										<th width="30%"><s:text name="prodStatus" /></th>
										<th colspan=""><s:text name="cultivar" /></th>
										<th colspan=""><s:text name="years" /></th>
										<th colspan=""><s:text name="noOfTrees" /></th>
									</tr>
									<s:if test="farm.treeDetails.size()>0">
										<s:iterator value="farm.treeDetails">
											<tr class="odd">

												<th><s:property value="prodStatus" />
													<th><s:property value="variety" />
											
												<th><s:property value="years" />
											
												<th><s:property value="noOfTrees" />	
									
											</tr>
								</s:iterator>
							</s:if>
							
							<s:else>
								<tr>
									<td colspan="6" align="center"><s:text
													name="nomenu.assigned" /></td>
								</tr>
							</s:else>
						</table>
					</div>
				</div>
					<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
								<s:property value="%{getLocaleProperty('info.treeInformation')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#treeDetailAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
						   </div>
					<div class="aContent dynamic-form-con" id="treeDetailAccordian">
						<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">
							<tr class="odd">
								<%-- <th width="5%"><s:text name="s.no" /></th> --%>
								<th colspan=""><s:text name="cultivar" /></th>
								<th colspan=""><s:text name="noOfTrees" /></th>
							</tr>
					
							<s:if test="treeDetails.size()>0">
								<s:iterator value="treeDetails">
									<tr class="odd">

										<th><s:property value="variety" />
											
												<th><s:property value="noOfTrees" />	
									
											</tr>
								</s:iterator>
							</s:if>
						</table>
					</div>
							
				</div>
            <div class="aPanel treeCalcInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.treeCalcDetails')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#treeCalcDetailsAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="treeCalcDetailsAccordian">
							<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.totalOrganicTrees')}" />
									</p>
									<p class="flexItem" name="farm.totalOrganicTrees">
										<s:property value="farm.totalOrganicTrees" />
									</p>
							</div>
							<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.totalConventionalTrees')}" />
									</p>
									<p class="flexItem" name="farm.totalConventionalTrees">
										<s:property value="farm.totalConventionalTrees" />
									</p>
								</div>
							<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.totalAvocadoTrees')}" />
									</p>
									<p class="flexItem" name="farm.totalAvocadoTrees">
										<s:property value="farm.totalAvocadoTrees" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.hectarOrganicTrees')}" />
									</p>
									<p class="flexItem" name="farm.hectarOrganicTrees">
										<s:property value="farm.hectarOrganicTrees" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.hectarConventionalTrees')}" />
									</p>
									<p class="flexItem" name="farm.hectarConventionalTrees">
										<s:property value="farm.hectarConventionalTrees" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.hectarAvocadoTrees')}" />
									</p>
									<p class="flexItem" name="farm.hectarAvocadoTrees">
										<s:property value="farm.hectarAvocadoTrees" />
									</p>
								</div>	
							
              </div>
			</div>			

<%-- 
						<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.orgVariety')}" />
									<div class="pull-right">
										<a class="aCollapse" href="orgVartyAccordn"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="orgVartyAccordn">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">

									<s:if test="orgnaicVarietyList.size()>0">
										<s:iterator value="orgnaicVarietyList">
											<tr class="odd">

												<th><s:property value="fieldName" /> <th><s:property value="fieldValue" />
											
											</tr>
										</s:iterator>
									</s:if>
								</table>
							</div>

						</div>
						
						<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.convenVariety')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#convenVartyAccrd"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="convenVartyAccrd">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">
									
									<s:if test="conventionalVarietyList.size()>0">
										<s:iterator value="conventionalVarietyList">
											<tr class="odd">

												<th><s:property value="fieldName" />
												
												<th><s:property value="fieldValue" />
											
											</tr>
										</s:iterator>
									</s:if>
					</table>
					</div>
					</div> --%>
					
					


						</s:if>
												


					<s:if test="enableSoliTesting==1">
						<div class="aPanel soilTesting" style="width: 100%">
							<div class="aTitle">
								<h2>
									<s:text name="info.soilTesting" />
									<div class="pull-right">
										<a class="aCollapse" href="#soilTestAcc"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="dynamic-flexItem">
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
								</s:if>
							</s:if> --%>
						</div>
					</s:if>
				</div>
				<div class="dynamicFieldsRender"></div>
			</div>

			<div class="flexRight appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
				 <s:if test="getCurrentTenantId()!='olivado'">
					<h2>
						 <s:property
								value="%{getLocaleProperty('farm.photo')}" /> 
					</h2>
					<div id="" class="farmer-photo">
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
                 </s:if>

					<div class="clear"></div>
					<s:hidden id="farmCordinates" value="%{jsonObjectList}" />
					<h2>
						<s:text name="info.digitalLocator" />
					</h2>
					<s:if test="farm.latitude!=''|| farm.longitude!=''">
						<div>
							<div>
								<img src="img/red_placemarker.png" width="32" height="32">
								<s:property value="%{getLocaleProperty('conventional')}" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img
									src="img/yellow_placemarker.PNG" width="32" height="32">
								<s:property value="%{getLocaleProperty('inconversion')}" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img
									src="img/green_placemarker.png" width="32" height="32">
								<s:property value="%{getLocaleProperty('organic')}" />
							</div>

						</div>
					</s:if>
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

					<s:if
						test="currentTenantId=='pratibha' && farm.coordinates.size()>0">
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

	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="appContentWrapper fullwidth">
				<s:if test="harvest.size()>0">
					<s:if test="enableMultiProduct=='1' ">
						<s:iterator value="harvestDetails">

							<table class="table table-bordered aspect-detail">
								<tr>
									<th colspan="12" style="background-color: #A8E3D6"><s:text
											name="info.cropHarvest" /></th>
								</tr>

								<tr>
									<th><s:text name="farmharvestDate" />
									
									<th><s:text name="crop" /></th>
									<th><s:text name="varietyName" /></th>
									<th><s:text name="grade" /></th>
									<th><s:text name="Unit" /></th>
									<th><s:text name="farmcrops.cropSeason" /></th>
									<th><s:text name="qty" /></th>
									<th><s:text name="farmprice" /></th>
									<th><s:text name="farmsubTotal" /></th>
								</tr>
								<s:set var="totalQty" value="%{0}" />
								<s:set var="totalPrice" value="%{0}" />
								<s:set var="totalSubTotal" value="%{0}" />
								<s:set var="cropName1" value="%{''}" />
								<s:set var="cropName2" value="%{''}" />
								<s:set var="cropName1" value="%{crop.name}" />
								<tr>
									<s:if test="%{#cropName2!=''}">
										<s:if test="%{#cropName1!=#cropName2}">
											<%-- <tr>
												<td></td>
												<td></td>
												<td></td>
												<td style="font-weight: bold;"><s:text
														name="farmtotalQuantity" /></td>
												<td><s:property
														value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalQty})}" /></td>
												<!-- <td><s:property value ="%{'' + getText('{0,number,#,##0.00}',{#attr.totalPrice})}" /></td>-->
												<td style="font-weight: bold;"><s:text
														name="totalAmount" /></td>
												<td><s:property
														value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalSubTotal})}" /></td>
											</tr> --%>
							
							</table>

							<table class="table table-bordered aspect-detail">
								<tr>
									<th colspan="12" style="background-color: #A8E3D6"><s:text
											name="info.cropHarvest" /></th>
								</tr>
								<tr>
									<th><s:text name="farmharvestDate" />
									
									<th><s:text name="crop" /></th>
									<th><s:text name="varietyName" /></th>
									<th><s:text name="grade" /></th>
									<th><s:text name="Unit" /></th>
									<th><s:text name="farmcrops.cropSeason" /></th>
									<th><s:text name="qty" /></th>
									<th><s:text name="farmprice" /></th>
									<th><s:text name="farmsubTotal" /></th>
								</tr>
								<s:set var="totalQty" value="%{0}" />
								<s:set var="totalPrice" value="%{0}" />
								<s:set var="totalSubTotal" value="%{0}" />
								<s:set var="cropName1" value="%{''}" />
								<s:set var="cropName2" value="%{''}" />
								</s:if>
								</s:if>
								<td><s:date name="cropHarvest.harvestDate"
										format="dd/MM/yyyy" /></td>
								<td><s:property value="crop.name" /></td>
								<s:set var="cropName2" value="%{crop.name}" />
								<td><s:property value="variety.name" /></td>
								<td><s:property value="Grade.name" /></td>
								<td><s:property value="crop.unit" /></td>
								<td><s:property
										value="getSeasonByCode(cropHarvest.seasonCode)" /></td>

								<td><s:property
										value="getText('{0,number,#,##0.00}',{qty})" /></td>
								<s:set var="totalQty" value="%{qty + #attr.totalQty}" />
								<td><s:property
										value="getText('{0,number,#,##0.00}',{price})" /></td>
								<s:set var="totalPrice" value="%{price + #attr.totalPrice}" />
								<td><s:property
										value="getText('{0,number,#,##0.00}',{subTotal})" /></td>
								<s:set var="totalSubTotal"
									value="%{subTotal + #attr.totalSubTotal}" />

								</tr>

								<%-- <tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td style="font-weight: bold;"><s:text
											name="farmtotalQuantity" /></td>
									<td><s:property
											value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalQty})}" /></td>
									<!-- <td><s:property value ="%{'' + getText('{0,number,#,##0.00}',{#attr.totalPrice})}" /></td> -->
									<td style="font-weight: bold;"><s:text name="totalPrice" /></td>
									<td><s:property
											value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalSubTotal})}" /></td>
								</tr> --%>
							</table>

						</s:iterator>
					</s:if>
					<s:else>

						<table class="table table-bordered aspect-detail">
							<tr>
								<th colspan="12" style="background-color: #A8E3D6"><s:text
										name="info.cropHarvest" /></th>
							</tr>

							<tr>
								<th><s:text name="farmharvestDate" />
								
								<th><s:text name="crop" /></th>
								<th><s:text name="varietyName" /></th>
								<th><s:text name="grade" /></th>
								<th><s:text name="Unit" /></th>
								<th><s:text name="farmcrops.cropSeason" /></th>
								<th><s:text name="qty" /></th>
								<%-- <s:if
								test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
									<th><s:text name="farmprice" /></th>
									<th><s:text name="farmsubTotal" /></th>
								</s:if> --%>
							</tr>
							<s:set var="totalQty" value="%{0}" />
							<s:set var="totalPrice" value="%{0}" />
							<s:set var="totalSubTotal" value="%{0}" />
							<s:set var="cropName1" value="%{''}" />
							<s:set var="cropName2" value="%{''}" />
							<s:iterator value="harvestDetails">
								<s:set var="cropName1" value="%{crop.name}" />
								<tr>
									<s:if test="%{#cropName2!=''}">
										<s:if test="%{#cropName1!=#cropName2}">
											<%-- <tr>
												<td></td>
												<td></td>
												<td></td>
												<td style="font-weight: bold;"><s:text
														name="farmtotalQuantity" /></td>
												<td><s:property
														value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalQty})}" /></td>
												<!-- <td><s:property value ="%{'' + getText('{0,number,#,##0.00}',{#attr.totalPrice})}" /></td>-->
												<td style="font-weight: bold;"><s:text
														name="totalAmount" /></td>
												<td><s:property
														value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalSubTotal})}" /></td>
											</tr> --%>
						
						</table>

						<table class="table table-bordered aspect-detail">
							<tr>
								<th colspan="12" style="background-color: #A8E3D6"><s:text
										name="info.cropHarvest" /></th>
							</tr>
							<tr>
								<th><s:text name="farmharvestDate" />
								
								<th><s:text name="crop" /></th>
								<th><s:text name="varietyName" /></th>
								<th><s:text name="grade" /></th>
								<th><s:text name="Unit" /></th>
								<th><s:text name="farmcrops.cropSeason" /></th>
								<th><s:text name="qty" /></th>
								<s:if
									test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
									<th><s:text name="farmprice" /></th>
									<th><s:text name="farmsubTotal" /></th>
								</s:if>
							</tr>
							<s:set var="totalQty" value="%{0}" />
							<s:set var="totalPrice" value="%{0}" />
							<s:set var="totalSubTotal" value="%{0}" />
							<s:set var="cropName1" value="%{''}" />
							<s:set var="cropName2" value="%{''}" />
							</s:if>
							</s:if>
							<td><s:date name="cropHarvest.harvestDate"
									format="dd/MM/yyyy" /></td>
							<td><s:property value="crop.name" /></td>
							<s:set var="cropName2" value="%{crop.name}" />
							<td><s:property value="variety.name" /></td>
							<td><s:property value="Grade.name" /></td>
							<td><s:property value="crop.unit" /></td>
							<td><s:property
									value="getSeasonByCode(cropHarvest.seasonCode)" /></td>

							<td><s:property value="getText('{0,number,#,##0.00}',{qty})" /></td>
							<s:set var="totalQty" value="%{qty + #attr.totalQty}" />
							<%-- <s:if
								test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
								<td><s:property
										value="getText('{0,number,#,##0.00}',{price})" /></td>
								<s:set var="totalPrice" value="%{price + #attr.totalPrice}" />
								<td><s:property
										value="getText('{0,number,#,##0.00}',{subTotal})" /></td>
								<s:set var="totalSubTotal"
									value="%{subTotal + #attr.totalSubTotal}" />
							</s:if> --%>

							</tr>
							</s:iterator>
							<%-- <tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td style="font-weight: bold;"><s:text name="farmtotalQuantity" /></td>
								<td><s:property
										value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalQty})}" /></td>
								<!-- <td><s:property value ="%{'' + getText('{0,number,#,##0.00}',{#attr.totalPrice})}" /></td> -->
								<td style="font-weight: bold;"><s:text name="totalPrice" /></td>
								<td><s:property
										value="%{'' + getText('{0,number,#,##0.00}',{#attr.totalSubTotal})}" /></td>
							</tr> --%>
						</table>
					</s:else>
				</s:if>



				<s:if test="harvestSupply.size()>0">
					<s:set var="consolidatedTotalAmount" value="%{0}" />
					<s:set var="consolidatedTotalQuantity" value="%{0}" />
					<s:set var="totalAmount" value="%{0}" />
					<s:if test="enableMultiProduct==1 ">



						<table class="table table-bordered aspect-detail fillform">
							<tr>
								<th colspan="14" style="background-color: #A8E3D6"><a>
										<s:text name="info.cropSupply" />
								</a></th>
							</tr>
							<tbody id="farmSaleAccordian">
								<tr>
									<td><s:text name="farmdateOfSale" /></td>
									<td><s:text name="farmbuyerInfo" /></td>
									<td><s:text name="crop" /></td>
									<td><s:text name="varietyName" /></td>
									<td><s:text name="grade" /></td>
									<td><s:text name="unit" /></td>
									<td><s:text name="season" /></td>
									<td><s:text name="farmprice" /></td>
									<td><s:text name="qty" /></td>
									<td><s:text name="farmlotNo" /></td>
									<td><s:text name="transportDetail" /></td>
									<td><s:text name="farmreceiptInfor" /></td>
									<td><s:text name="farmpaymentInfo" /></td>
									<td><s:text name="totalSaleValu" /></td>
								</tr>

								<s:iterator value="cropSupplyDetails">

									<tr>
										<td><s:date name="cropSupply.dateOfSale"
												format="dd/MM/yyyy" /></td>
										<td><s:property value="cropSupply.buyerInfo.customerName" /></td>
										<td><s:property value="crop.name" /></td>
										<td><s:property value="variety.name" /></td>
										<td><s:property value="grade.name" /></td>
										<td><s:property value="crop.unit" /></td>
										<td><s:property
												value="getSeasonByCode(cropSupply.currentSeasonCode)" /></td>
										<td><s:property value="price" /></td>
										<td><s:property
												value="getText('{0,number,#,##0.00}',{qty})" /></td>
										<td><s:property value="batchLotNo" /></td>

										<td><s:property value="cropSupply.transportDetail" /></td>
										<td><s:property value="cropSupply.invoiceDetail" /></td>
										<td><s:property value="cropSupply.paymentInfo" /></td>
										<s:set var="totalAmt" value="%{qty * price}" />
										<s:set var="consolidatedTotalAmount"
											value="%{#totalAmt+ #attr.consolidatedTotalAmount}" />
										<s:set var="consolidatedTotalQuantity"
											value="%{qty + #attr.consolidatedTotalQuantity}" />
										<td><s:property
												value="getText('{0,number,#,##0.00}',{#totalAmt})" /></td>
									</tr>
								</s:iterator>
								<%-- <tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td style="font-weight: bold;"><s:text
											name="farmtotalQuantity" /></td>
									<td><s:property
											value="%{'' + getText('{0,number,#,##0.00}',{#attr.consolidatedTotalQuantity})}" /></td>
									<td></td>
									<td></td>
									<td></td>
									<td style="font-weight: bold;"><s:text name="totalAmount" /></td>
									<td><s:property
											value="%{'' + getText('{0,number,#,##0.00}',{#attr.consolidatedTotalAmount})}" /></td>
								</tr> --%>
							</tbody>
						</table>

					</s:if>
					<s:else>
						<table class="table table-bordered aspect-detail fillform">
							<tr>
								<th colspan="14" style="background-color: #A8E3D6"><s:text
										name="info.cropSupply" /></th>
							</tr>

							<tr>
								<th><s:text name="farmdateOfSale" /></th>
								<th><s:text name="farmbuyerInfo" /></th>
								<th><s:text name="crop" /></th>
								<th><s:text name="varietyName" /></th>
								<th><s:text name="grade" /></th>
								<td><s:text name="unit" /></td>
								<td><s:text name="season" /></td>
								<th><s:text name="farmprice" /></th>
								<th><s:text name="qty" /></th>
								<th><s:text name="farmlotNo" /></th>
								<th><s:text name="transportDetail" /></th>
								<th><s:text name="farmreceiptInfor" /></th>
								<th><s:text name="farmpaymentInfo" /></th>
								<th><s:text name="totalSaleValu" /></th>
							</tr>

							<s:iterator value="cropSupplyDetails">


								<tr>
									<td><s:date name="cropSupply.dateOfSale"
											format="dd/MM/yyyy" /></td>
									<td><s:property value="cropSupply.buyerInfo.customerName" /></td>
									<td><s:property value="crop.name" /></td>
									<td><s:property value="variety.name" /></td>
									<td><s:property value="grade.name" /></td>
									<td><s:property value="crop.unit" /></td>
									<td><s:property
											value="getSeasonByCode(cropSupply.currentSeasonCode)" /></td>
									<td><s:property value="price" /></td>
									<td><s:property
											value="getText('{0,number,#,##0.00}',{qty})" /></td>
									<td><s:property value="batchLotNo" /></td>

									<td><s:property value="cropSupply.transportDetail" /></td>
									<td><s:property value="cropSupply.invoiceDetail" /></td>
									<td><s:property value="cropSupply.paymentInfo" /></td>
									<s:set var="totalAmt" value="%{qty * price}" />
									<s:set var="consolidatedTotalAmount"
										value="%{#totalAmt+ #attr.consolidatedTotalAmount}" />
									<s:set var="consolidatedTotalQuantity"
										value="%{qty + #attr.consolidatedTotalQuantity}" />
									<td><s:property
											value="getText('{0,number,#,##0.00}',{#totalAmt})" /></td>
								</tr>
							</s:iterator>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td style="font-weight: bold;"><s:text
										name="farmtotalQuantity" /></td>
								<td><s:property
										value="%{'' + getText('{0,number,#,##0.00}',{#attr.consolidatedTotalQuantity})}" /></td>
								<td></td>
								<td></td>
								<td></td>
								<td style="font-weight: bold;"><s:text name="totalAmount" /></td>
								<td><s:property
										value="%{'' + getText('{0,number,#,##0.00}',{#attr.consolidatedTotalAmount})}" /></td>
							</tr>

						</table>
					</s:else>
				</s:if>



			</div>
		</div>

	</div>
</div>

<div class="flexItem flex-layout flexItemStyle">
	<div class="">
		<sec:authorize ifAllGranted="profile.farmer.update">

			<span id="update" class=""><span class="first-child">
					<button type="button" class="edit-btn btn btn-success"
						onclick="onUpdate()">
						<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
						</font>
					</button>
			</span></span>
		<%-- 	<span id="update" class=""><span class="first-child actStatus">
									<button type="button" class="edit-btn btn btn-success"
										onclick="onUpdateActPlan()">
										<FONT color="#FFFFFF"> <b><s:text
													name="followup.button" /></b>
										</font>
									</button>
							</span></span> --%>
	<%-- 		<s:if test='farm.status=="0" && currentTenantId =="livelihood"'>
				<span id="approve" class=""><span class="first-child">
						<button type="button" class="edit-btn btn btn-success">
							<FONT color="#FFFFFF"> <b><s:text
										name="approve.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if> --%>
		</sec:authorize>
		<sec:authorize ifAllGranted="profile.farmer.delete">

			<span id="delete" class=""><span class="first-child">
					<button type="button" onclick="onDelete()"
						class="delete-btn btn btn-warning">
						<FONT color="#FFFFFF"> <b><s:text name="delete.button" /></b>
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



<s:form name="updateform" action="farm_update.action">
	<s:hidden name="id" value="%{farm.id}" />
	<s:hidden key="currentPage" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farm.farmer.sangham}" />
	<s:hidden name="tabIndexFarmer" value="%{tabIndex}" />
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
<s:form name="cancelform" action="farmer_details.action">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerId}" />
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
<s:form name="approveform" action="farm_approve.action">
	<s:hidden name="id" value="%{farm.id}" />
	<s:hidden key="currentPage" />
	<s:hidden name="farmerId" value="%{farmerId}" />
	<s:hidden name="sangham" value="%{farm.farmer.sangham}" />
	<s:hidden name="tabIndexFarmer" value="%{tabIndex}" />
</s:form>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script src="js/dynamicComponents.js?v=23.333"></script>
<script>
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	var id='<s:property value="id"/>';
	var totalLand='<s:property value="farm.farmDetailedInfo.totalLandHolding" />';
	//alert(totalLand);
	jQuery(document)
			.ready(
					function() {
						if(tenant!="symrise"){
							$('.actStatus').hide();	
						}
						var actStatuss = '<s:property value="actStatuss"/>';
						if(actStatuss==1||actStatuss==2){
						$('.actStatus').hide();
						}
						 if(window.location.href.indexOf("?id=") < 0) {
						 var urll =window.location.href +"?id="+id;
						 window.location.replace(urll);
						} 
						if(tenant=="fruitmaster"){				
							
							$("#totalLandKanal").text(parseFloat(totalLand)*8);
							
						}
					     if(tenant=="symrise"){
                           jQuery(".soilIrrigationInfo").addClass("hide");
                           $(".soilType").insertAfter(".ownLand").after($(".soilTexture"));

                		}
					     
					     if(tenant=="farmAgg"){
					    	  jQuery(".conversionInfo").addClass("hide");	                         

	                		}
					     
					     
	/* 				     if(tenant=="avt"){ 
					           jQuery(".farmLabourInfo").addClass("hide");
					           jQuery(".conversionStatus").addClass("hide");
					           jQuery(".landMarkInfo").removeClass("hide"); 
					         }
 */
						
						jQuery(".plantArea").show();
						
						if(tenant=="pratibha"){
                			$(".pratibhaHide").hide();
                		}
						/* if(tenant=="welspun"){
                			$(".welspunHide").hide();
                		} */
						if(tenant=="wilmar"){
                			$(".hideIrrigationType").hide();
                		}
						var isCert = '<s:property value="farm.farmer.isCertifiedFarmer" />';
						var icsType='<s:property value="farm.farmIcsConv.icsType" />';
						loadFarmMap(isCert,icsType);
						//loadFarmMap();

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

						var selectedIrrigationType = '<s:property value="farm.farmDetailedInfo.farmIrrigation" />';
						if(selectedIrrigationType=='1'){
							jQuery(".hideIrrigationType").hide();
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
						
						/* var irrigationTypeVal = '<s:property value="irrigationSourceDetail" />';
						if(isEmpty(irrigationTypeVal)){
							hideByEleName('selectedIrrigation');
						} */
			                    	
										var farmer = '<s:property value="farm.farmer.isCertifiedFarmer" />';
										//alert(farmer);
										if(farmer==0){
											 document.getElementsByClassName("organicStatusDiv")[0].style.visibility = 'hidden';
										}else{
											//$(".organicStatusDiv").show();
											// document.getElementsByClassName("organicStatusDiv")[0].style.visibility = 'hidden';
										}
								
										   renderDynamicDetailFeilds();
										   if(tenant=="susagri"){
										    	 $(".inspType").removeClass("hide");
										     }
										   $('#approve')
											.on(
													'click',
													function(e) {
														if (confirm('<s:text name="confirm.approveFarm"/> ')) {
															document.approveform.id.value = document
																	.getElementById('farmId').value;
															document.approveform.currentPage.value = document.form.currentPage.value;
															document.approveform
																	.submit();

														}
													});
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
     function getTxnType(){
    		
    	 return "359";
    	 }
     
     function getBranchIdDyn(){
     	return '<s:property value="farm.farmer.branchId"/>';
    	}
     function showByEleId(id){
     	$("#"+id).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
     }
	function loadFarmMap(isCert,icsType) {
		try {
			var farmCoordinate = jQuery("#farmCordinates").val();
			var landArea = JSON.parse(farmCoordinate);			
			loadMap('map', '<s:property value="farm.latitude"/>',
					'<s:property value="farm.longitude"/>', landArea,isCert,icsType);
			addExistingMarkers(landArea);
		} catch (err) {
			console.log(err);
		}
	}
	
	var map;
	function initMap() {
		var lat="<s:property value="farm.latitude"/>";
		var lon="<s:property value="farm.longitude"/>";
		if(isEmpty(lat) || isEmpty(lon)|| lon==''||lon==''){
			
			var longtitude="<s:property value='getLon()'/>";
			var latitude="<s:property value='getLat()'/>";
			if(!isEmpty(longtitude)|| !isEmpty(latitude)|| latitude!=''||longtitude!=''){
				map = new google.maps.Map(document.getElementById('map'), {
					center : {
						lat : parseFloat(latitude),
						 lng : parseFloat(longtitude)
					},
					zoom :6,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				});
				}
			else{
			 map = new google.maps.Map(document.getElementById('map'), {
					center : {
						lat : 11.0168,
						lng : 76.9558
					},
					zoom : 3,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				}); 
			}
			}
			else{
				map = new google.maps.Map(document.getElementById('map'), {
					center : {
					     lat : parseFloat(lat),
						 lng : parseFloat(lon)
					},
					zoom :15,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				});
			}
	}
	
	
	function hideFields(){
		 var app = $(".aPanel");
		 $(app).addClass("hide");
		 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
		 	 $(this).addClass("hide");
		 });
		  
		}
	
	function loadMap(mapDiv, latitude, longitude, landArea,isCert,icsType) {
		var intermediateImg;
		 var intermediatePointIcon;
		 
		 if(isCert==0){
				intermediateImg = "red_placemarker.png";
				 intermediatePointIcon = temp + '/img/'+intermediateImg;
			}else{
				if(icsType!=null &&icsType!='' ){
					if(isCert==1 && icsType==3 ) {
						intermediateImg = "green_placemarker.png";
						 intermediatePointIcon = temp + '/img/'+intermediateImg;
					}else {
						intermediateImg = "yellow_placemarker.PNG";
						 intermediatePointIcon = temp + '/img/'+intermediateImg;
					}
				}
				
				
			}
		
		try {
			var bounds = new google.maps.LatLngBounds();
			if(!isEmpty(latitude)&&!isEmpty(longitude)){
				var marker;
				marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(latitude),
							parseFloat(longitude)),
							icon:intermediatePointIcon,
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
					    map.setZoom(16);
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
	
/* 	function onUpdateActPlan() {
		document.updateActform.id.value = document.getElementById('farmId').value;
		document.updateActform.isFollowUp.value = "1";
		document.updateActform.currentPage.value = document.form.currentPage.value;
		document.updateActform.submit();
	}
 */
	function onDelete() {
		if (confirm('<s:text name="confirm.delete"/> ')) {
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