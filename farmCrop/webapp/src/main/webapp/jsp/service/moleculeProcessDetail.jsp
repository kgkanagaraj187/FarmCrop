<%@ include file="/jsp/common/detail-assets.jsp"%>

<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
</head>
<body>
	<script>
		var typezz = '';
		jQuery(document)
				.ready(
						function() {
							typezz =
	<%out.print("'" + request.getParameter("type") + "'");%>

							if (typezz == 'service') {
								$(".breadCrumbNavigation").html('');
								$(".breadCrumbNavigation").append(
										"<li><a href='#'>Service</a></li>");
								$(".breadCrumbNavigation")
										.append(
												"<li><a href='moleculeProcess_list.action?type=service'>Molecule</a></li>");
								$("#typez").val('service');
							} else {
								$(".breadCrumbNavigation").html('');
								$(".breadCrumbNavigation").append(
										"<li><a href='#'>Report</a></li>");
								$(".breadCrumbNavigation")
										.append(
												"<li><a href='moleculeProcess_list.action?type=report'>Molecule Report</a></li>");
								$("#typez").val('report');
							}
						});
	</script>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />

		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:property value="%{getLocaleProperty('Molecule Information')}" />
						</h2>
						<s:if test='branchId==null'>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="app.branch" />
								</p>
								<p class="flexItem">
									<s:property value="%{getBranchName(molecule.id)}" />
								</p>
							</div>
						</s:if>
						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="Lot No" />
							</p>
							<p class="flexItem">
								<s:property value="molecule.landHolding" />
							</p>
						</div>
						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="%{getLocaleProperty('status')}" />
							</p>
							<p class="flexItem">
								<s:property value="statusMsg" />
							</p>
						</div>



						<%--<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="harvestSeason.currentSeason" /></p>
						<p class="flexItem"><s:text name="status%{harvestSeason.currentSeason}" /></p>
					</div>--%>
						<s:if test="cropYieldDetailList.size>0">
							<!-- <div class="flex-view-layout"> -->
							<div class="fullwidth">
								<div class="flexWrapper">
									<!-- <div class="flexLeft appContentWrapper"> -->


									<h2>
										<s:text name="Europe Failed Molecule Information" />
									</h2>
									<div
										style="overflow: scroll; height: 150px; overflow: auto; width: 100%"
										class="flexWrapper">
										<table class="table table-bordered aspect-detail">

											<tr>

												<td><b><s:text name="Molecule Name" /></b></td>
												<td><b><s:text name="Molecule Value" /></b></td>
												<td><b><s:text name="Molecule ExpectedValue" /></b></td>
											</tr>
											<s:iterator value="cropYieldDetailList" status="state"
												var="bean">
												<tr class="odd">
													<td><s:property value="moleculeName" /></td>
													<td><s:property value="moleculeValue" /></td>
													<td><s:property value="moleculeExpectedValue" /></td>
												</tr>
											</s:iterator>
										</table>
									</div>
									<!-- </div> -->
									<!-- </div> -->
									<!-- </div> -->

								</div>
							</div>
						</s:if>


						<s:if test="cropYieldDetailListUs.size>0">
							<!-- <div class="flex-view-layout"> -->
							<div class="fullwidth">
								<div class="flexWrapper">
									<!-- <div class="flexLeft appContentWrapper"> -->


									<h2>
										<s:text name="US Failed Molecule Information" />
									</h2>
									<div
										style="overflow: scroll; height: 150px; overflow: auto; width: 100%"
										class="flexWrapper">

										<table class="table table-bordered aspect-detail">

											<tr>

												<td><b><s:text name="Molecule Name" /></b></td>
												<td><b><s:text name="Molecule Value" /></b></td>
												<td><b><s:text name="Molecule ExpectedValue" /></b></td>
											</tr>
											<s:iterator value="cropYieldDetailListUs" status="state"
												var="bean">
												<tr class="odd">
													<td><s:property value="moleculeName" /></td>
													<td><s:property value="moleculeValue" /></td>
													<td><s:property value="moleculeExpectedValue" /></td>
												</tr>
											</s:iterator>
										</table>
									</div>
									<!-- </div> -->
									<!-- </div> -->
									<!-- </div> -->

								</div>
							</div>
						</s:if>

					</div>

				</div>
			</div>
			<div class="yui-skin-sam">



				<span id="cancel" class=""><span class="first-child">
						<button type="button" class="back-btn btn btn-sts">
							<b><FONT color="#FFFFFF"><s:text name="back.button" />
							</font></b>
						</button>
				</span></span>
			</div>
		</div>
	</s:form>


	<s:form name="cancelform"
		action="moleculeProcess_list.action?type=report">
		<s:hidden key="currentPage" />
		<s:hidden name="postdata" id="postdata" />
	</s:form>

</body>