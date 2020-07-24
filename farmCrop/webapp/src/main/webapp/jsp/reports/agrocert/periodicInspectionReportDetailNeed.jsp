<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
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
		var filterMapReport='<s:property value="filterMapReport"/>';
		var postdataReport= '<s:property value="postdataReport"/>';
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
<!-- <style>

.sliderImgCntrlContainer {
	position:absolute; 
	left:50; 
	bottom:22px;
    margin-left:0px;
}
.sliderImgCntrlContainer span {
	padding:5px;
	margin-right:5px;
	text-decoration:none;
	background:#415602;
	color:#fff;
	text-transform:uppercase;
	cursor:pointer;
	
}
.sliderImgCntrlContainer span a {
	text-decoration:none!important;	
	color:#fff!important;
}

.bx-wrapper img {
	width:100%;
	height:auto;
}
.bx-wrapper {
	margin-bottom:20px;
}
.bx-wrapper img.noImgCls {
	width:200px!important;
	margin-left:20px!important;
}
</style> -->
</head>

<font color="red"> <s:actionerror />
</font>
<div class="error">
	<s:fielderror />
</div>
<s:hidden key="periodicInspection.id" id="periodicInspectionId" />
<s:form name="form">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="periodicInspection.id" />
	<s:hidden key="command" />
	<div style="width: 100%;">
		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div style="width: 50%; float: left;">
							<table width="100%" cellspacing="0"
								class="table table-bordered aspect-detail fillform">
								<tr>
									<th colspan="3"><s:text name="info.inspection" /></th>
								</tr>
								<tr class="odd">
									<td><s:text name="inspectionDate" /></td>
									<td><s:date name="periodicInspection.inspectionDate"
											format="dd/MM/yyyy hh:mm:ss" /></td>

								</tr>
								<tr class="odd">
									<td><s:text name="farmerIdName" /></td>
									<td width="65%"><s:property
											value="periodicInspection.farm.farmer.farmerIdAndName" />&nbsp;</td>
								</tr>

								<tr class="odd">
									<td><s:text name="farmIdName" /></td>
									<td width="65%"><s:property
											value="periodicInspection.farm.farmIdAndName" />&nbsp;</td>
								</tr>

								<tr class="odd">
									<td width="30%"><s:text name="cropName" /></td>
									<td width="40%"><s:property
											value="periodicInspection.cropCode" /></td>

								</tr>

								<tr class="odd">
									<td><s:text name="fieldstaffIdName" /></td>
									<td width="40%"><s:property
											value="periodicInspection.createdUserName" />-<s:property
											value="%{mobileUserName}" /></td>
								</tr>

								<tr class="odd">
									<td><s:text name="purpose" /></td>
									<td width="65%"><s:property
											value="periodicInspection.purpose" />&nbsp;</td>
								</tr>
								<s:if test="currentTenantId!='efk'">
								<tr class="odd">
									<td><s:text name="fertilizerApp" /></td>
									<td width="65%"><s:if
											test="periodicInspection.isFertilizerApplied=='Y'">
											<s:text name="Yes" />
										</s:if> <s:else>
											<s:text name="No" />
										</s:else></td>
								</tr>

								<tr class="odd">
									<td><s:text name="safetyDisposal" /></td>
									<td width="65%"><s:if
											test="periodicInspection.isFieldSafetyProposal=='Y'">
											<s:text name="Yes" />
										</s:if> <s:else>
											<s:text name="No" />
										</s:else></td>
								</tr>
								</s:if>
								<tr class="odd">
									<td><s:text name="remarks" /></td>
									<td width="65%"><s:property
											value="periodicInspection.remarks" />&nbsp;</td>
								</tr>

								<tr class="odd">
									<td><s:text name="audioFile" /></td>
									<td width="65%"><s:if
											test="periodicInspection.farmerVoice!=null && periodicInspection.farmerVoice.length!=0">
											<s:text name="yes" />
										</s:if> <s:else>
											<s:text name="No" />
										</s:else></td>
								</tr>
							</table>
													
							<br />
							
							<div class="yui-skin-sam">
							<span id="cancel" class=""><span class="first-child"><button
										type="button" class="back-btn btn btn-sts">
										<b><FONT color="#FFFFFF"><s:text name="back.button" />
										</font></b>
									</button></span></span>
						</div>
							
						</div>
						<div class="col-md-6" style="position: relative; left: 50px;">

							<s:if test='periodicInspection.inspectionImageInfo!=null '>
								<ul class="bxslider"
									style="position: relative; width: auto !important;">

									<s:iterator var="insImages" status="headerstatus"
										value="inspectionImagesSet">

										<s:if
											test='#insImages.photo!=null && #insImages.photo.length > 0'>

											<li><s:hidden name="#insImages.id" id="photoId" />
												<div class="row">
													<label style="margin-left: 15px"><s:text
															name="date" /> : <s:date
															name="#insImages.photoCaptureTime"
															format="dd/MM/yyyy hh:mm:ss " /> </label>
												</div>
												<div class="row">
													<label style="margin-left: 15px"><s:text
															name="latitude" /> : <s:property value="latitude" /> <s:text
															name="longitude" /> : <s:property
															value="#insImages.longitude" /></label>
												</div>

												<div class="row">
													<img
														src="data:image/png;base64,<s:property value="#insImages.imageByteString"/>">
												</div></li>

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
										class="fl"></span> <span class="bx-controls-directionLst fl">last</span>
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

						<%-- <div class="yui-skin-sam">
							<span id="cancel" class=""><span class="first-child"><button
										type="button" class="back-btn btn btn-sts">
										<b><FONT color="#FFFFFF"><s:text name="back.button" />
										</font></b>
									</button></span></span>
						</div> --%>
					</div>
				</div>

			</div>
		</div>




	</div>
</s:form>

<s:form name="cancelform"
	action="periodicInspectionReport_list.action%{tabIndexNeed}">
	<s:hidden name="id" />
	<s:hidden key="currentPage" />
	<s:hidden name="tabIndexNeed" />
	<s:hidden name="tabIndex" value="%{tabIndexNeed}" />
	<s:hidden name="filterMapReport" id="filterMapReport" />
	<s:hidden name="postdataReport" id="postdataReport" />
</s:form>