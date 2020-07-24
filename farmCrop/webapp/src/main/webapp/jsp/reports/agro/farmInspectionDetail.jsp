<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
<style>
.ui-jqgrid-btable td:last-child {
	text-align: left !important;
}

/* Farmer detail screen design changes */
.borderCls {
	border: solid 1px #939585;
}

.borderCls th {
	padding-top: 5px;
	padding-bottom: 5px;
	text-align: center;
	border: 1px solid #939585 !important;
	color: #000000 !important;
}

.firstRowDivCls {
	width: 100%;
	min-height: 400px;
}

.secondRowDivCls {
	width: 100%;
	height: 110px
}

.thirdRowDivCls {
	width: 100%;
	height: 360px
}

.paddingTd { /*padding-top:5px;*/ /*padding-bottom:5px;*/
	width: 50%;
}

.firstRowDivCls

  

TD


:nth-child

 

(2),
TD


:nth-child

 

(4)
{
font-weight


:

 

bold


;
}
.firstColumnRow2 {
	width: 100%;
	min-height: 190px !important;
}

.firstColumnRow2

 

TD


:nth-child

 

(2),
TD


:nth-child

 

(4){
font-weight


:

 

bold


;
}
.secondRowDivCls

 

TD


:nth-child

 

(2)
{
font-weight


:

 

bold


;
}
.adrssClss {
	width: 200px;
}

.notFoundCls {
	text-align: center;
	font-weight: bold;
	padding-top: 25px
}

.disableBtn {
	border-color: #ccc !important;
	border-style: none !important;
	border-width: 0 0px !important;
	cursor: default !important;
	border: none !important;
	background: url(../images/agro-theme/editBtn.png) no-repeat 6px 4px;
	background-position: -258px 4px;
	background-repeat: no-repeat;
	background-color: #eff8d7 !important;
	margin: -3px 0 !important;
}

.disableBtn:hover {
	background-color: #eff8d7 !important;
}

.disableBtn font {
	color: #567304 !important;
}

.parent {
	display: table;
	width: 100%;
}

.child {
	display: table-cell;
	/*background: #eee;
			    border-left: 1px solid #ccc;*/
	width: 50%;
}

#farmerInfo td {
	font-weight: normal !important;
}

table>caption {
	text-align: left;
	font-weight: bold;
}

.table>tbody>tr>td:nth-of-type(2n+1) {
	width: 50%;
}

.sliderImgCntrlContainer {
	position: absolute;
	left: 50;
	bottom: 138px;
	margin-left: 3px;
}

.sliderImgCntrlContainer span {
	padding: 5px;
	margin-right: 5px;
	text-decoration: none;
	background: #415602;
	color: #fff;
	text-transform: uppercase;
	cursor: pointer;
}

.sliderImgCntrlContainer span a {
	text-decoration: none !important;
	color: #fff !important;
}

.bx-wrapper img {
	width: 100%;
	height: auto;
}

.bx-wrapper {
	margin-bottom: 20px;
}

.bx-wrapper img.noImgCls {
	width: 200px !important;
	margin-left: 20px !important;
}

</style>

</head>

<!--<script src="assets/common/jqGrid/js/jquery.bxslider.min.js" type="text/javascript" />-->

<script src="plugins/openlayers/OpenLayers.js"></script>
<script src="http://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>

<script type="text/javascript">
	jQuery(document).ready(function($) {
		$(".bx-controls-directionLst").on("click", function() {
			var slideQty = $('#bx-pager').find('a').length;
		});

		$(".bx-controls-directionFst").on("click", function() {
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
			<s:hidden key="periodicInspection.id" id="inspectionId" />
			<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
			<div class="row">
			
				<div class="col-md-6">
					<table class="table table-bordered aspect-detail fillform">
						<tr>
							<th colspan="2"><s:text name="info.inspection" /></th>
						</tr>
						<tr class="odd">
							<td><s:text name="inspectionDate" /></td>
							<td><%-- <s:date
									name="periodicInspection.inspectionDate"
									format="dd/MM/yyyy hh:mm:ss" /> --%>
									
								<s:property value="inspectDate"/>
									</td>
						</tr>
						<s:if test="getCurrentTenantId()!='lalteer'">
							<tr class="odd">
								<td><s:text name="farmerIdName" /></td>
								<td><s:property
										value="periodicInspection.farm.farmer.farmerIdAndName" /></td>

							</tr>
						</s:if>
						<s:else>
							<tr class="odd">
								<td><s:text name="farmerCodeName" /></td>
								<td><s:property
										value="periodicInspection.farm.farmer.farmerCodeAndName" /></td>

							</tr>
						</s:else>

						<tr class="odd">
							<td><s:text name="farmIdName" /></td>
							<td><s:property
									value="periodicInspection.farm.farmIdAndName" /></td>

						</tr>
						
						<tr class="odd">
							<td width="30%"><s:text name="cropName" /></td>
							<td width="40%"><s:property value="periodicInspection.cropCode" /></td>

						</tr>

						<tr class="odd">
							<td><s:text name="fieldstaffIdName" /></td>
							<td><s:property
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
						<s:if test="getCurrentTenantId()!='efk' && getCurrentTenantId()!='avt'">
						<tr class="odd">
							<td><s:text name="farmerOpinion" /></td>
							 <td class="break-word"><s:property
									value="periodicInspection.farmerOpinion" /></td> 
						</tr>
						</s:if>
						<tr class="odd">
							<td><s:text name="remarks" /></td>
							<td class="break-word"><s:property
									value="periodicInspection.remarks" /></td>
									

						</tr>

						<%-- <tr class="odd">
							<td width="35%"><s:text name="inspectionAudio" /></td>
							<td width="65%"><s:if
									test="periodicInspection.farmerVoice!=null && periodicInspection.farmerVoice.length!=0">
									<s:text name="yes" />
								</s:if> <s:else>
									<s:text name="No" />
								</s:else></td>
						</tr>
 --%>
					</table>
			<%-- 	<s:if test="getCurrentTenantId()!='chetna'"> --%>
					<table class="table table-bordered aspect-detail fillform">
						<tr>
							<th colspan="3"><s:text name="info.pest" /></th>
						</tr>
						<tr class="odd">
							<td><s:text name="pestOccurence" /></td>
							<td><s:if
									test="periodicInspection.pestProblemNoticed=='Y'">
									<s:text name="Yes" />
								</s:if> <s:else>
									<s:text name="No" />
								</s:else></td>

						</tr>
						<s:if test="periodicInspection.pestProblemNoticed=='Y'">
							<tr class="odd">
								<td><s:text name="nameOfPest" /></td>
								<td><s:property value="selectedPestName" /></td>
							</tr>

							<s:if test="currentTenantId=='fruitmaster'">
							<tr class="odd">
								<td><s:text name="pestSymptom" /></td>
								<td><s:property value="selectedPestSymptom" /></td>
							</tr> 
							</s:if>
							<tr class="odd">
								<td><s:text name="PestInfestationETL" /></td>
								<td><s:if
										test="periodicInspection.pestInfestationETL=='Y'">
										<s:text name="Yes" />
									</s:if> <s:else>
										<s:text name="No" />
									</s:else></td>
							</tr>

							<s:if test="periodicInspection.pesticideRecommentedSet.size()>0">
								<s:if test="currentTenantId!='efk'">
								<tr>
									<td colspan="2">
										<div class="form-group">
											<div class="col-sm-12">
												<table width="100%" border="1">
													<caption>
														<s:property value="%{getLocaleProperty('pesticideRecommended')}" />
													</caption>
													<tr>
														<th><s:text name="tname" /></th>
														<th><s:text name="tqty" /></th>
														<th><s:property value="%{getLocaleProperty('uom')}" /></th>
													</tr>
													<s:iterator
														value="periodicInspection.pesticideRecommentedSet"
														status="incr">
														<tr>
															<td><s:text
																	name="pestNameValue.split(',')[%{#incr.index}]" /></td>
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
																	value="%{getLocaleProperty('pesticideRecommended')}" /></td>
															<td><s:iterator
																	value="periodicInspection.pesticideRecommentedSet"
																	status="incr">
																	<s:text name="pestNameValue.split(',')[%{#incr.index}]" />
																</s:iterator></td>

														</tr>
													</div>
														</div>
														</td>
													</tr>
												</s:else>
							</s:if>

							<%-- <tr class="odd">
					<td><s:text name="monthOfPesticideApplication" /></td>
					<td><s:property value="getYearMonthByPeriodicType(periodicInspection.monthOfPesticideApplication)" /></td>
				</tr> --%>
							<%-- <tr class="odd">
								<td><s:text name="pestProblemSolved" /></td>
								<td><s:if
										test="periodicInspection.pestProblemSolved=='Y'">
										<s:text name="Yes" />
									</s:if> <s:else>
										<s:text name="No" />
									</s:else></td>
							</tr> --%>
						</s:if>
					</table>
					<%-- </s:if> --%>

					<table class="table table-bordered aspect-detail fillform">
						<tr>
							<th colspan="3"><s:text name="info.disease" /></th>
						</tr>
						<tr class="odd">
							<td><s:text name="diseaseOccurence" /></td>
							<td><s:if
									test="periodicInspection.diseaseProblemNoticed=='Y'">
									<s:text name="Yes" />
								</s:if> <s:else>
									<s:text name="No" />
								</s:else></td>
						</tr>
						<s:if test="periodicInspection.diseaseProblemNoticed=='Y'">
							<tr class="odd">
								<td><s:text name="nameOfDisease" /></td>
								<td><s:property value="selectedDiseaseName" /></td>
							</tr>


					<s:if test="currentTenantId=='fruitmaster'">
							<tr class="odd">
								<td><s:text name="diseaseSymptom" /></td>
								<td><s:property value="selectedDiseaseSymptom" /></td>
							</tr> </s:if>

							<tr class="odd">
								<td><s:text name="diseaseInfestationETL" /></td>
								<td><s:if
										test="periodicInspection.diseaseInfestationETL=='Y'">
										<s:text name="Yes" />
									</s:if> <s:else>
										<s:text name="No" />
									</s:else></td>
							</tr>

							<s:if test="periodicInspection.fungicideRecommentedSet.size()>0">
								<s:if test="currentTenantId!='efk'">
								<tr>
									<td colspan="2">
										<div class="form-group">
											<div class="col-sm-12">
												<table width="100%" border="1">
													<caption>
														<s:property value="%{getLocaleProperty('fungicideRecommended')}" />
													</caption>
													<tr>
														<th><s:text name="tname" /></th>
														<th><s:text name="tqty" /></th>
														<th><s:property value="%{getLocaleProperty('uom')}" /></th>
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
															<td><s:property value="%{getLocaleProperty('fungicideRecommended')}" /></td>
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
								<td><s:text name="diseaseProblemSolved" /></td>
								<td><s:if
										test="periodicInspection.diseaseProblemSolved=='Y'">
										<s:text name="Yes" />
									</s:if> <s:else>
										<s:text name="No" />
									</s:else></td>
							</tr> --%>
						</s:if>
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
								<td><s:text name="survival" /></td>
								<td><s:property
										value="periodicInspection.survivalPercentage" /></td>

							</tr>
							
						</s:if>
						<s:else>
							<tr class="odd">
							
							<td><s:text name="%{getLocaleProperty('survival')}" /></td>
							
								<td><s:property
										value="periodicInspection.survivalPercentage" /></td>

							</tr>
						</s:else>
						<tr class="odd">
							<td><s:text name="currentStatusofGrowth" /></td>
							<td><s:property
									value="periodicInspection.currentStatusOfGrowth" /></td>

						</tr>
						<s:if
							test="enableMultiProduct==0 ||currentTenantId!='lalteerqa' || currentTenantId!='lalteer'">

							<%-- <tr class="odd">
								<td><s:text name="averageHeightMeter" /></td>
								<td><s:property
										value="periodicInspection.averageHeight" /></td>

							</tr> --%>
						</s:if>
						<s:if
							test="enableMultiProduct==0 || !currentTenantId='lalteerqa' || !currentTenantId='lalteer'">
							<tr class="odd">
								<td><s:text name="averageGirthCm" /></td>
								<td><s:property
										value="periodicInspection.averageGirth" /></td>
							</tr>
						</s:if>
						<s:if test="getCurrentTenantId()!='efk'">
						<tr class="odd">
							<td><s:text name="activitieCarriedPostPreVisit" /></td>
							<td><s:property value="selectedActivities" /></td>
						</tr>
						</s:if>
						<s:if test="getCurrentTenantId()=='avt'">
							<tr class="odd">
							<td><s:text name="cropProtectionPractice" /></td>
							<td><s:property
									value="periodicInspection.cropProtectionPractice" /></td>

						</tr>
						</s:if>
						<%-- <tr class="odd">
					<td><s:text name="typeOfFertilizerApplied" /></td>
					<td><s:text value="getCommaSeparatedValueFromSet(periodicInspection.fertilizerAppliedSet)"
						default="NA" /></td>
				</tr> --%>
						
						<s:if test="selActiviesValue.indexOf('8') != -1">

							<tr class="odd">
								<td><s:text name="chemicalName" /></td>
								<td><s:property
										value="periodicInspection.chemicalName" /></td>
							</tr>
						</s:if>

						<s:if test="selActiviesValue.indexOf('1')!= -1">
							<tr class="odd">
								<td><s:text name="gapPlantDate" /></td>
								<td><s:date
										name="periodicInspection.gapPlantingDate" format="dd/MM/yyyy" /></td>
							</tr>
							
							<tr class="odd">
								<td><s:text name="plantsReplanted" /></td>
								<td><s:property
									value="periodicInspection.noOfPlantsReplanned" /></td>
							</tr>
						
						</s:if>
						<s:if test="selActiviesValue.indexOf('2')!=-1">
							<tr class="odd">
								<td><s:text name="interPloughing" /></td>
								<td><s:property value="selectedInterPloughing" /></td>
							</tr>
						</s:if>
						<s:if
							test="currentTenantId=='lalteerqa' || currentTenantId=='lalteer' || currentTenantId=='pratibha' ">
							<tr class="odd">
								<td><s:text name="cropRotation" /></td>
								<td><s:if
										test='periodicInspection.cropRotation=="Y"'>
										<s:text name="Yes" />
									</s:if> <s:elseif test='periodicInspection.cropRotation=="N"'>
										<s:text name="No" />
									</s:elseif> <s:else>
										<s:text name="NA" />
									</s:else></td>
							</tr>

							<tr class="odd">
								<td><s:text name="temp" /></td>
								<td><s:property
										value="periodicInspection.temperature" default="NA" /></td>
							</tr>

							<tr class="odd">
								<td><s:text name="rain" /></td>
								<td><s:property value="periodicInspection.rain"
										default="NA" /></td>
							</tr>

							<tr class="odd">
								<td><s:text name="humidity" /></td>
								<td><s:property
										value="periodicInspection.humidity" default="NA" /></td>
							</tr>

							<tr class="odd">
								<td><s:text name="windSpeed" /></td>
								<td><s:property
										value="periodicInspection.windSpeed" default="NA" /></td>
							</tr>
						</s:if>
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
													<th><s:property value="%{getLocaleProperty('uom')}" /></th>
												</tr>
												<s:iterator value="periodicInspection.fertilizerAppliedSet"
													status="incr">
													<tr>
														<td><s:text
																name="fertilizerValue.split(',')[%{#incr.index}]" /></td>
														<td><s:property value="quantityValue" /></td>
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
								<td><s:property
										value="getYearMonthByPeriodicType(periodicInspection.monthOfFertilizerApplied)" /></td>
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
													<th><s:text name="qty" /></th>
													<th><s:property value="%{getLocaleProperty('uom')}" /></th>
												</tr>
												<s:iterator value="periodicInspection.typeOfManureSet"
													status="incr">
													<tr>
														<td><s:text
																name="manureValue.split(',')[%{#incr.index}]" /></td>
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
					</table>

					<%-- <table class="table table-bordered aspect-detail fillform">
						<tr>
							<th colspan="3"><s:text name="info.intercrop" /></th>
						</tr>

						<tr class="odd">
							<td><s:text name="isInterCrop" /></td>

							<td><s:if
									test="periodicInspection.interCrop=='Y'">
									<s:text name="Yes" />
								</s:if> <s:else>
									<s:text name="No" />
								</s:else></td>
						</tr>

						<s:if test="periodicInspection.interCrop=='Y'">
							<tr class="odd">
								<td><s:text name="interCropName" /></td>
								<td><s:property
										value="periodicInspection.nameOfInterCrop" /></td>
							</tr>
							<tr class="odd">
								<td><s:text name="yieldObtained" /></td>
								<td><s:property
										value="periodicInspection.yieldObtained" /></td>

							</tr>
							<tr class="odd">
								<td><s:text name="expenditureIncurred" /></td>
								<td><s:property
										value="periodicInspection.expenditureIncurred" /></td>

							</tr>
							<tr class="odd">
								<td><s:text name="incomeGenerated" /></td>
								<td><s:property
										value="periodicInspection.incomeGenerated" /></td>

							</tr>

							<tr class="odd">
								<td><s:text name="netProfitOrLoss" /></td>
								<td><s:property
										value="periodicInspection.netProfitOrLoss" /></td>

							</tr>
						</s:if>
					</table> --%>
					<div style="width: 50%; float: left;">
						<s:if test='periodicInspection.inspectionImageInfo!=null '>
							<ul class="bxslider"
								style="position: relative; width: auto !important;">
								<s:iterator var="insImages" status="headerstatus"
									value="inspectionImagesSet">

									<s:if
										test='#insImages.photo!=null && #insImages.photo.length > 0'>
										<li><s:hidden name="#insImages.id" id="photoId" />
											<div class="row">
												<label><s:text name="date" /> : <s:date
														name="#insImages.photoCaptureTime"
														format="dd/MM/yyyy hh:mm:ss " /> </label>
											</div>
											<div class="row">
												<label><s:text name="latitude" /> : <s:property
														value="latitude" /> <s:text name="longitude" /> : <s:property
														value="#insImages.longitude" /></label>
											</div>

											<div class="row">
												<img
													src="data:image/png;base64,<s:property value="#insImages.imageByteString"/>">
											</div></li>
									</s:if>
									<s:else>
										<img align="middle" width="150" height="150" border="0"
											class="noImgCls" src="img/no-image.png">
									</s:else>
								</s:iterator>
							</ul>
							<!-- <div style="" class="sliderImgCntrlContainer">
								<span class="bx-controls-directionFst fl">first</span> <span
									id="slider-prev" class="fl"></span> <span id="slider-next"
									class="fl"></span> <span class="bx-controls-directionLst fl">last</span>
							</div> -->
						</s:if>
						<s:else>
							<div
								style="width: 450px; margin: 0 auto; height: 331px; background: #ccc; text-align: center;">
								<img src="images/noImg.png" class="noImgCls" />
							</div>
						</s:else>
						<%-- <div id="bx-pager">
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
						</div> --%>
					</div>
				</div>
			</div>
<div class="yui-skin-sam">

				<sec:authorize ifAllGranted="service.farmInspection.update">
					<span id="update" class=""><span class="first-child">
							<button type="button" class="edit-btn btn btn-success">
								<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize>

		<%-- 		<sec:authorize ifAllGranted="service.farmInspection.delete">
					<span id="delete" class=""><span class="first-child">
							<button type="button" class="delete-btn btn btn-warning">
								<FONT color="#FFFFFF"> <b><s:text
											name="delete.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize> --%>

				<span id="cancel" class=""> <span class="first-child">
						<button type="button" class="back-btn btn btn-sts">
							<b><FONT color="#FFFFFF"><s:text name="back.button" />
							</font></b>
						</button>
				</span>
				</span>
			</div>
			<br>
</div>
</div>
</div>
</div>

		
		</s:form>

		<s:form name="cancelform"
			action="periodicInspectionServiceReport_list?type=service">
			<s:hidden key="currentPage" />
		</s:form>

		<s:form name="updateform"
			action="periodicInspectionServiceReport_update.action?type=service">
			<s:hidden key="id" />
			<s:hidden key="currentPage" />
		</s:form>
		<s:form name="deleteform"
			action="periodicInspectionServiceReport_delete.action?type=service">
			<s:hidden key="id" />
			<s:hidden key="currentPage" />
		</s:form>

		<script type="text/javascript">
			$(document)
					.ready(
							function() {
								$('#update')
										.on(
												'click',
												function(e) {
													document.updateform.id.value = document
															.getElementById('inspectionId').value;
													document.updateform.currentPage.value = document.form.currentPage.value;
													document.updateform
															.submit();
												});

								$('#delete')
										.on(
												'click',
												function(e) {
													if (confirm('<s:text name="confirm.delete"/> ')) {
														document.deleteform.id.value = document
																.getElementById('inspectionId').value;
														document.deleteform.currentPage.value = document.form.currentPage.value;
														document.deleteform
																.submit();
													}
												});
							});
		</script>
	</div>
</body>

