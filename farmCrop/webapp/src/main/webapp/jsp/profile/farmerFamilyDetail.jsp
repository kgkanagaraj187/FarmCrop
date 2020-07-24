<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>

<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
<!--<script type="text/javascript" src="yui/element/element-beta-min.js"></script>
<script type="text/javascript" src="yui/button/button-min.js"></script>-->
<style type="text/css">
redText1 {
	color: #CB171D;
	float: left;
}
</style>
</head>
<body>

	<font color="red"> <s:actionerror /> <s:fielderror /> <sup>*</sup>
		<s:text name="reqd.field" /></font>

	<s:form name="form" cssClass="fillform"
		action="farmerFamily_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden name="farmerId" value="%{farmerId}" />
		<s:hidden name="farmerName" value="%{farmerName}" />

		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" />

		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper">
							<div class="aPanel">
								<div class="aTitle">
									<h2>
										<s:text name="info.familyDetails" />
										<div class="pull-right">
											<a class="aCollapse" href="#"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>

								<div class="aContent dynamic-form-con">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('name')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.name" />
										</p>
									</div>


							<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('headOfFamily')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.headOfFamily" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmerFamilyGender')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.gender" />
										</p>
									</div>




									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('age')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.age" />
										</p>
									</div>




									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.relationship')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.relation" />
										</p>
									</div>





									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.education')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.education" />
										</p>
									</div>






									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.disability')}" />
										</p>

									 	<p class="flexItem">
										<s:property value="disability" />
											</p>
									</div>

									<s:if test="farmerFamily.disability==1">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.disableDetail')}" />
										</p>

									 	<p class="flexItem">
										<s:property value="farmerFamily.disableDetail" />
											</p>
									</div>

									</s:if>

					<%-- 				<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('name')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.name" />
										</p>
									</div>
 --%>





									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.maritalStatus')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.maritalStatus" />
										</p>
									</div>



									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('familyDetail.educationStatusBelow18')}" />
										</p>
										<p class="flexItem">
											<s:property value="farmerFamily.educationStatus" />
										</p>
									</div>

								</div>


							</div>
						</div>

						<div class="flexItem flex-layout flexItemStyle">
							<div class="yui-skin-sam">
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
											<button type="button" onclick="onDelete();"
												class="delete-btn btn btn-warning">
												<FONT color="#FFFFFF"> <b><s:text
															name="delete.button" /></b>
												</font>
											</button>
									</span></span>
								</sec:authorize>
								<span id="cancel" class=""><span class="first-child">
										<button type="button" onclick="onCancel();"
											class="back-btn btn btn-sts">
											<b><FONT color="#FFFFFF"><s:text
														name="back.button" /> </font></b>
										</button>
								</span></span>
							</div>

						</div>




					</div>

				</div>
			</div>
		</div>
	</s:form>

	<s:form name="updateForm" id="updateForm" action="farmerFamily_update">
		<s:hidden key="id" />
		<s:hidden name="farmerId" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:form name="deleteForm" id="deleteForm" action="farmerFamily_delete">
		<s:hidden key="id" />
		<s:hidden name="farmerId" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:form name="cancelform" action="farmer_detail.action">
		<s:hidden name="farmerId" />
		<s:hidden name="id" value="%{farmerFamily.farmer.id}" />
		<s:hidden name="tabIndex" value="#tabs-6" />
		<s:hidden name="currentPage" />
	</s:form>

	<script>
		function onUpdate() {
			//	alert("update");
			document.updateForm.submit();
		}

		function onDelete() {
			var val = confirm('<s:text name="confirmDelete"/>');
			if (val)
				document.deleteForm.submit();
		}

		function onCancel() {
			document.cancelForm.submit();
		}
	</script>
</body>



</html>