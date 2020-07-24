<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(function() {

			jQuery(".back-btn").click(function() {
				document.cancelform.submit();

			});

			jQuery("#update").click(function() {
				document.updateform.submit();
			});
			
			var tenantId='<s:property value="getCurrentTenantId()"/>';
		});
	</script>
	<font color="red"> <s:actionerror /></font>
	<s:form name="form" cssClass="fillform">


		<s:hidden key="command" />
		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper margin-bottom">
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:property value="%{getLocaleProperty('info.farmer')}" />
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(farmDetail.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farmer.firstName')}" />
								</p>
								<p class="flexItem">
									<s:property value="farmDetail.farmer.firstName" />
									-
									<s:property value="farmDetail.farmer.farmerId" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farm.farmName" />
								</p>
								<p class="flexItem">

									<s:property value="farmDetail.farmName" />
									-
									<s:property value="farmDetail.farmCode" />

								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="village" />
								</p>
								<p class="flexItem">
									<s:property value="farmDetail.farmer.village.name" />
								</p>
							</div>

						</div>
						<s:if test="farmerIncomeList.size()==0">
						<div class="yui-skin-sam">
							<span id="cancel" class=""> <span class="first-child">
									<button type="button" class="back-btn btn btn-sts">
										<b><FONT color="#FFFFFF"><s:text name="back.button" />
										</font></b>
									</button>
							</span>
							</span>
						</div>
						</s:if>
					</div>

					<s:if test="farmerIncomeList.size()>0">
						<div class="appContentWrapper" style="width: 100%">
							<div class="formContainerWrapper dynamic-form-con">
								<h2>
									<s:text name="info.farmerIncome" />
								</h2>
								<table class="table table-bordered aspect-detail">
									<tr>
										<th><s:property
												value="%{getLocaleProperty('primaryIncome')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
										
										<%-- <th><s:property
												value="%{getLocaleProperty('cottonIncome')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th> --%>
										<th><s:property
												value="%{getLocaleProperty('interCropIncomeCrop')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
											<th>	<s:text name="incomeOther"/>(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
										<th><s:property value="%{getLocaleProperty('agriGross')}" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)
										</th>
									</tr>
									<s:iterator value="farmerIncomeList" id="bean">
										<tr>

											<td><s:property value="agriIncome" /></td>
											
											<%-- <td><s:property value="cottonIncome" /></td> --%>
											<td><s:property value="interCropIncome" /></td>
											<td><s:property value="otherIncome" /></td>
											<td><s:property value="grossAgriIncome" /></td>

										</tr>
									</s:iterator>
								</table>
							</div>
							<div class="yui-skin-sam">
								<sec:authorize ifAllGranted="service.farmerIncome.update">
									<span id="update" class=""><span class="first-child">
											<button type="button" class="edit-btn btn btn-success">
												<FONT color="#FFFFFF"> <b><s:text
															name="edit.button" /></b>
												</font>
											</button>
									</span></span>
								</sec:authorize>


								<span id="cancel" class=""> <span class="first-child">
										<button type="button" class="back-btn btn btn-sts">
											<b><FONT color="#FFFFFF"><s:text
														name="back.button" /> </font></b>
										</button>
								</span>
								</span>
							</div>
						</div>
					</s:if>
				</div>

			</div>
		</div>



	</s:form>

	<s:form name="cancelform"
		action="farmerIncome_list.action?type=service">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="updateform"
		action="farmerIncome_update.action?type=service">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>
</body>
