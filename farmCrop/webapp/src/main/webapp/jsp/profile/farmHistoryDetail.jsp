<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script src="js/dynamicComponents.js?v=1.19"></script>
<script>
	$(document).ready(function () {
		//renderDynamicDetailFeilds();
	});
	
	function onCancel() {
		document.cancelform.submit();
	}
	
</script>

<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="farmerLandDetails.id" />
	<s:hidden key="command" />


	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">

					<div class="formContainerWrapper">

						<div class="aPanel">
							<div class="aTitle">

									<h2>
										<s:property value="%{getLocaleProperty('info.farmHistoryDetails')}" />
									</h2>
									
							</div>
							<div class="aContent dynamic-form-con">


		
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.year')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.year" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.crops')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.crops" />
									</p>
								</div>
								
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.seedlings')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.seedlings" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.estimatedAcreage')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.estimatedAcreage" />
									</p>
								</div>
							
							   <div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.noOfTrees')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.noOfTrees" />
									</p>
								</div>
								
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.pestdiseases')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.pestdiseases" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.pestdiseasesControl')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.pestdiseasesControl" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.fertilizationMethod')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.fertilizationMethod" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.inputs')}" />
									</p>
									<p class="flexItem">
										   <s:property value="distance[farmerLandDetails.inputs]" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.withBuffer')}" />
									</p>
									<p class="flexItem">
										 <s:property value="distance[farmerLandDetails.withBuffer]" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.withoutBuffer')}" />
									</p>
									<p class="flexItem">
										<s:property value="distance[farmerLandDetails.withoutBuffer]" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmerLandDetails.date')}" />
									</p>
									<p class="flexItem">
										<s:property value="farmerLandDetails.date" />
									</p>
								</div>
								
							</div>

						</div>
				</div>

<div class="dynamicFieldsRender"></div>
					<div class="yui-skin-sam">
					<%-- <sec:authorize  access="hasAnyRole('profile.samithi.update','profile.samithi.bci.update')">
							<span id="update" class=""><span class="first-child">
									<button type="button" class="edit-btn btn btn-success">
										<FONT color="#FFFFFF"> <b><s:text
													name="edit.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('profile.samithi.delete','profile.samithi.bci.delete')">
							<span id="delete" class=""><span class="first-child">
									<button type="button" class="delete-btn btn btn-warning">
										<FONT color="#FFFFFF"> <b><s:text
													name="delete.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize> --%>
						<span id="cancel" class=""><span class="first-child"><button
									type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button></span></span>
					</div>
				</div>
			</div>
		</div>
	</div>


</s:form>

<%-- <s:form name="updateform" action="samithi_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="samithi_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form> --%>
<s:form name="cancelform" action="farmer_detail.action">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerUniqueId}" />
	<s:hidden name="tabIndex" value="%{tabIndexFarmerZ}" />
	<s:hidden name="currentPage" />
</s:form>

