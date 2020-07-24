<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<script src="js/jssor.slider.mini.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function($) {
		var slider = $('.bxslider').bxSlider({
			pagerCustom : '#bx-pager',
			nextSelector : '#slider-next',
			prevSelector : '#slider-prev',
			/*nextText: '>>',
			prevText: '<<',*/
			infiniteLoop : false,
			hideControlOnEnd : false,
			startSlide : 0
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
<style>
.sliderImgCntrlContainer {
	position: absolute;
	left: 50;
	bottom: 22px;
	margin-left: 0px;
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

<body id="bdy">
	<div style="width: 100%;">
		<font color="red"><s:actionerror /></font>
		<s:hidden key="periodicInspection.id" id="periodicInspectionId" />
		<s:form name="form">
			<s:hidden key="currentPage" />
			<s:hidden key="id" />
			<s:hidden key="command" />
			<s:hidden key="periodicInspection.id" id="inspectionId" />
			<s:hidden key="currentPage" />

	<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
			<div style="width: 100%;">
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
			        		<td width="40%"><s:property value="periodicInspection.cropCode" /></td>

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

						<tr class="odd">
							<td><s:text name="fertilizerApp" /></td>
							<td width="65%"><s:if
									test="periodicInspection.isFertilizerApplied=='Y'">
									<s:text name="Yes" />
								</s:if> <s:else>
									<s:text name="No" />
								</s:else></td>
						</tr>
						
						<s:if test="periodicInspection.isFertilizerApplied=='Y'">
							<tr class="odd">
								<td><s:text name="safetyDisposal" /></td>
								<td width="65%"><s:if
										test="periodicInspection.isFieldSafetyProposal=='Y'">
										<s:text name="Yes" />
									</s:if> 
									 <s:else>
										<s:text name="No" />
									</s:else>
									</td>
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
				</div>
				<div class="col-md-6" style="position:relative; left:50px;">

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

									<s:else>
										<img align="middle" width="150" height="150" border="0"
											class="noImgCls" src="img/no-image.png">
									</s:else>
								</s:if>
							</s:iterator>
						</ul>
						<div style="" class="sliderImgCntrlContainer">
						<s:if
								test='#insImages.photo!=null && #insImages.photo.length > 0'>
							<span class="bx-controls-directionFst fl">first</span> <span
								id="slider-prev" class="fl"></span> <span id="slider-next"
								class="fl"></span> <span class="bx-controls-directionLst fl">last</span>
								</s:if>
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
				<div style="clear: both;"></div>
				<br />
				<div class="yui-skin-sam">

					<sec:authorize ifAllGranted="service.farmInspection.update">
						<span id="update" class=""><span class="first-child">
								<button type="button" class="edit-btn btn btn-success">
									<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
									</font>
								</button>
						</span></span>
					</sec:authorize>

					<sec:authorize ifAllGranted="service.farmInspection.delete">
						<span id="delete" class=""><span class="first-child">
								<button type="button" class="delete-btn btn btn-warning">
									<FONT color="#FFFFFF"> <b><s:text
												name="delete.button" /></b>
									</font>
								</button>
						</span></span>
					</sec:authorize>

					<span id="cancel" class=""> <span class="first-child">
							<button type="button" class="back-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="back.button" />
								</font></b>
							</button>
					</span>
					</span>
				</div></div>
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
			action="periodicInspectionServiceReport_updateNeed.action?type=service">
			<s:hidden key="id" />
			<s:hidden key="currentPage" />
		</s:form>
		<s:form name="deleteform"
			action="periodicInspectionServiceReport_delete.action?type=service">
			<s:hidden key="id" />
			<s:hidden key="currentPage" />
		</s:form>

		<script type="text/javascript">
			$(document).ready(function() {
				$('#update').on('click',function(e) {
						document.updateform.id.value = document.getElementById('inspectionId').value;
						document.updateform.currentPage.value = document.form.currentPage.value;
						document.updateform.submit();
					});

				$('#delete').on('click',function(e) {
						if (confirm('<s:text name="confirm.delete"/> ')) {
						document.deleteform.id.value = document.getElementById('inspectionId').value;
						document.deleteform.currentPage.value = document.form.currentPage.value;
						document.deleteform.submit();
						}
					});
			});
		</script>
	</div>
</body>
