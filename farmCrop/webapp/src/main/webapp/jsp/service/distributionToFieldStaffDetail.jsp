<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>
<meta name="decorator" content="swithlayout">
</head>
<body>
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="distributiondetail.distribution.id" id="distFieldStaffId" />
		<s:hidden key="id" id="distibutionDetailId" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden id="enableBatchNo" name="enableBatchNo" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper">
							<div class="aPanel">
								<div class="aTitle">
									<h2>
									    <s:property value="%{getLocaleProperty('info.distributionToMobileUser')}" />
										
									</h2>
								</div>
							</div>

							<div class="aContent dynamic-form-con">
								<s:if test='branchId==null'>
									<div class="dynamic-flexItem">
										<p class="flexItem">
											<s:text name="app.branch" />
										</p>
										<p class="flexItem">
											<s:property
												value="%{getBranchName(distributiondetail.distribution.branchId)}" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="txnDate" />
									</p>
									<p class="flexItem">
										<s:property value="distributionTxnTime" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="updatedUser" />
									</p>
									<p class="flexItem">
										<s:property
											value="distributiondetail.distribution.updatedUserName" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="updatedDate" />
									</p>
									<p class="flexItem">
										<s:property value="updateTxnTime" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('warehouse')}" />
									</p>
									<p class="flexItem">
										<s:property
											value="distributiondetail.distribution.servicePointName" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('profile.agent')}" />
									</p>
									<p class="flexItem">
										<s:property value="distributiondetail.distribution.agentName" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="category" />
									</p>
									<p class="flexItem">
										<s:property
											value="distributiondetail.product.subcategory.name" />
									</p>
								</div>


								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="product" />
									</p>
									<p class="flexItem">
										<s:property value="distributiondetail.product.name" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="distribution.unit" />
									</p>
									<p class="flexItem">
										<s:property value="distributiondetail.product.unit" />
									</p>
								</div>

							<%-- 	<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="avilableQty" />
									</p>
									<p class="flexItem">
										<s:property value="WarehouseProductStock" />
									</p>
								</div> --%>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="distributionStock" />
									</p>
									<p class="flexItem">
										<s:property value="distributiondetail.quantity" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="seasonCodeName" />
									</p>
									<p class="flexItem">
										<s:property value="seasonCodeAndName" />
									</p>
								</div>

								<s:if test="enableBatchNo ==1">
									<div class="dynamic-flexItem">
										<p class="flexItem">
											<s:text name="warehouseProduct.batchNo" />
										</p>
										<p class="flexItem">
											<s:property value="distributiondetail.batchNo" />
										</p>
									</div>
								</s:if>

							</div>

						</div>

						<div class="yui-skin-sam">
							<sec:authorize
								ifAllGranted="service.distribution.fieldStaff.update">
								<span id="update" class=""><span class="first-child">
										<button type="button" class="edit-btn btn btn-success">
											<FONT color="#FFFFFF"> <b><s:text
														name="edit.button" /></b>
											</font>
										</button>
								</span></span>
							</sec:authorize>
							<sec:authorize ifAllGranted="service.distribution.fieldStaff.update">
					<span class=""><span class="first-child">
							<button type="button" class="delete-btn btn btn-warning">
								<FONT color="#FFFFFF"> <b><s:text
											name="delete.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize>

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


	<s:form name="updateform"
		action="distributionToFieldStaff_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="distributionToFieldStaff_list.action">
		<s:hidden key="currentPage" />
	</s:form>
<s:form name="deleteform" action="distributionToFieldStaff_delete.action">
		<s:hidden key="id" />
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
														.getElementById('distibutionDetailId').value;
												document.updateform.currentPage.value = document.form.currentPage.value;
												document.updateform.submit();
											});
						
							
							$('.delete-btn').on('click',function(e) {
								if (confirm('<s:text name="delete.rowGrid"/> ')) {
									document.deleteform.id.value =document.getElementById('distFieldStaffId').value;
									document.deleteform.submit();
									}
							});
						});
	</script>

</body>
</html>