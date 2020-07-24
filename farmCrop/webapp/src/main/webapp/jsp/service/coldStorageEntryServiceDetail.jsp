<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			var supplier = '<s:property value="supplierEnabled"/>';
			if (supplier == "1") {
				jQuery(".supplierDiv").show();
			} else {
				jQuery(".supplierDiv").hide();
			}

			var regFarmer = '<s:property value="procurement.procMasterType"/>';
			var farmer = '<s:property value="procurement.farmer"/>';
			if ((regFarmer == "-1" || regFarmer == "99") && farmer != '') {
				jQuery(".reg").removeClass('hide');
				jQuery(".unreg").hide();
			} else {
				jQuery(".unreg").removeClass('hide');
				jQuery(".reg").hide();
			}
		});
	</script>

	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:text name="info.procurementDetails" />
						</h2>
						<s:if test="procurement.agroTransaction.agentName!=null">
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="agent" />
								</p>
								<p class="flexItem">
									<s:property value="procurement.agroTransaction.agentName" />
								</p>
							</div>
						</s:if>
						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="warehouse" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.warehouseName" />
							</p>
						</div>

						<div class="reg hide dynamic-flexItem">
							<p class="flexItem">
								<s:text name="IsRegistered" />
							</p>
							<p class="flexItem">
								<s:text name="yes" />
							</p>
						</div>

						<div class="reg hide dynamic-flexItem">
							<p class="flexItem">
								<s:text name="Farmer Name" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.farmer.farmerCodeAndName" />
							</p>
						</div>

						<div class="unreg hide dynamic-flexItem">
							<p class="flexItem">
								<s:text name="IsRegistered" />
							</p>
							<p class="flexItem">
								<s:text name="no" />
							</p>
						</div>

						<div class="unreg dynamic-flexItem">
							<p class="flexItem">
								<s:text name="Farmer Name" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.agroTransaction.farmerName" />
							</p>
						</div>

						<div class="unreg dynamic-flexItem">
							<p class="flexItem">
								<s:text name="Mobile Number" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.mobileNumber" />
							</p>
						</div>

						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:property
									value="%{getLocaleProperty('profile.location.municipality')}" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.village.city.name" />
							</p>
						</div>

						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('village.name')}" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.village.name" />
							</p>
						</div>
						<s:if test="currentTenantId!='efk'">
							<s:if test="procurement.procMasterType==99">
								<s:if test="currentTenantId!='crsdemo'">
									<div class="dynamic-flexItem">
										<p class="flexItem">
											<s:text name="supplier" />
										</p>
										<p class="flexItem">
											<s:property value="procurement.agroTransaction.farmerName" />
										</p>
									</div>

									<div class="dynamic-flexItem">
										<p class="flexItem">
											<s:text name="supplierType" />
										</p>
										<p class="flexItem">
											<s:property value="supplierMaster" />
										</p>
									</div>
								</s:if>
								<s:else>
									<div class="dynamic-flexItem" class="supplierDiv">
										<p class="flexItem">
											<s:text name="supplier" />
										</p>
										<p class="flexItem">
											<s:property value="supplierType" />
										</p>
									</div>
									<div class="dynamic-flexItem" class="supplierDiv">
										<p class="flexItem">
											<s:text name="supplierType" />
										</p>
										<p class="flexItem">
											<s:property value="supplierMaster" />
										</p>
									</div>
								</s:else>
							</s:if>
						</s:if>
						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="createdUser" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.createdUser" />
							</p>
						</div>
						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="createdDate" />
							</p>
							<p class="flexItem">
								<s:date name="procurement.createdDate" format="dd/MM/yyyy" />
							</p>
						</div>

						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="updatedUser" />
							</p>
							<p class="flexItem">
								<s:property value="procurement.updatedUser" />
							</p>
						</div>

						<div class="dynamic-flexItem">
							<p class="flexItem">
								<s:text name="updatedDate" />
							</p>
							<p class="flexItem">
								<s:date name="procurement.updatedDate" format="dd/MM/yyyy" />
							</p>
						</div>

						<s:if test="getCurrentTenantId()=='awi'">
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="roadMapCode" />
								</p>
								<p class="flexItem">
									<s:property value="procurement.roadMapCode" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="vehicleNo" />
								</p>
								<p class="flexItem">
									<s:property value="procurement.vehicleNum" />
								</p>
							</div>

							<s:if test="procurement.farmerAttnce==0">
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="farmerAttendance" />
									</p>
									<p class="flexItem">
										<s:text name="PRESENT" />
									</p>
								</div>
							</s:if>
							<s:else>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="farmerAttendance" />
									</p>
									<p class="flexItem">
										<s:text name="ABSENT" />
									</p>
								</div>

								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="substituteName" />
									</p>
									<p class="flexItem">
										<s:property value="procurement.substituteName" />
									</p>
								</div>
							</s:else>
						</s:if>
					</div>

				</div>
				<div class="appContentWrapper" style="width: 100%">
					<div class="formContainerWrapper">
						<h2>
							<s:text name="procProduct.det" />
						</h2>
						<table class="table table-bordered table-hover fillform">
							<tr>
								<th style="width: 15%" align="center"><s:text
										name="product" /></th>
								<th style="width: 15%" align="center"><s:text
										name="variety" /></th>
								<s:if test="currentTenantId!='lalteer'">
									<th style="width: 15%" align="center"><s:text
											name="procGrade" /></th>
								</s:if>
								<th style="width: 15%" align="center"><s:text name="unit" /></th>
								<%-- <th style="width: 15%" align="center"><s:text name="pricePerUnit" /></th> --%>
								<th style="width: 15%" align="center"><s:text
										name="noofBags" /></th>
								<th style="width: 15%" align="center"><s:text
										name="NetWeight" /></th>
								<s:if test="currentTenantId=='lalteer'">
									<th style="width: 15%" align="center"><s:text
											name="dryLoss" /></th>
									<th style="width: 15%" align="center"><s:text
											name="gradingLoss" /></th>
								</s:if>
								<th style="width: 15%" align="center"><s:text name="Price" /></th>
								<th style="width: 15%" align="center"><s:text
										name="SubTotal" /></th>

								<%-- <th style="width: 15%" align="center"><s:text name="Total Amount" /></th> --%>
							</tr>
							<s:iterator value="procurement.procurementDetails" var="values">
								<tr>
									<td style="width: 15%"><s:property
											value="procurementGrade.procurementVariety.procurementProduct.name" /></td>
									<td style="width: 15%"><s:property
											value="procurementGrade.procurementVariety.name" /></td>
									<%-- 				<td style="width: 15%"><s:property
						value="procurementGrade.procurementVariety.name" />-<s:property
						value="procurementGrade.procurementVariety.code" /></td> --%>
									<s:if test="currentTenantId!='lalteer'">
										<td style="width: 15%"><s:property
												value="procurementGrade.name" /></td>
										<%-- 								<td style="width: 15%"><s:property value="procurementGrade.name" />-<s:property
						value="procurementGrade.code" /></td> --%>
									</s:if>
									<td style="width: 15%"><s:property
											value="procurementGrade.procurementVariety.procurementProduct.unit" /></td>
									<%-- <td style="width: 15%" align="right"><s:property value="ratePerUnit" /></td> --%>
									<td style="width: 15%" align="right"><s:property
											value="numberOfBags" /></td>
									<td style="width: 15%" align="right"><s:property
											value="grossWeight" /></td>
									<s:if test="currentTenantId=='lalteer'">
										<td style="width: 15%" align="right"><s:property
												value="dryLoss" /></td>
										<td style="width: 15%" align="right"><s:property
												value="gradingLoss" /></td>
									</s:if>
									<td style="width: 15%" align="right"><s:property
											value="ratePerUnit" /></td>
									<td style="width: 15%" align="right"><s:property
											value="subTotal" /></td>

									<%-- <td style="width: 15%" align="right"><s:property value="subTotal" /></td> --%>
								</tr>
							</s:iterator>
						</table>
						<div class="formContainerWrapper">
							<h2>
								<s:property value="%{getLocaleProperty('info.payment')}" />
							</h2>
						</div>
						<div class="flexiWrapper filterControls">
							<table class="table table-bordered aspect-detail">
								<%-- <td><label for="txt"><s:property
										value="%{getLocaleProperty('farmerBalAmount')}" />(<s:property
										value="%{getCurrencyType().toUpperCase()}" />)</label></td>
							<td> <div id="farmerCashBal" style="font-weight: bold;"></div></td> --%>
								<td><label for="txt"><s:property
											value="%{getLocaleProperty('enterAmount')}" />(<s:property
											value="%{getCurrencyType().toUpperCase()}" />)</label></td>
								<td><p class="flexItem">
										<s:property value="procurement.paymentAmount" />
									</p></td>
								<!-- <td class="cashTypeDiv"><input type="text"
								class="fullwidth textAlignRight" name="paymentRupee" value=""
								id="paymentRupee" maxlength="10"
								onkeypress="return isDecimal(event)"
								style="text-align: right; padding-right: 1px; width: 90% !important;"></td> -->
							</table>
						</div>

						<s:if test="currentTenantId == 'efk'">
							<!-- <div class="appContentWrapper marginBottom"> -->
							<div class="formContainerWrapper">
								<h2>
									<s:property
										value="%{getLocaleProperty('Procurement.transpotation.Details')}" />
								</h2>


								<div class="flexiWrapper filterControls">
									<table class="table table-bordered aspect-detail">
										<td><label for="txt"><s:property
													value="%{getLocaleProperty('procurement.transport.vehicleList')}" /></label></td>

										<td><s:property value="vehicleType" /></td>


										<td><label for="txt"><s:property
													value="%{getLocaleProperty('procurement.transport.cost')}" />(<s:property
													value="%{getCurrencyType().toUpperCase()}" />)</label></td>

										<td><s:property value="procurement.transportCost" /></td>
									</table>
								</div>

							</div>
							<!-- </div> -->
						</s:if>


						<div class="yui-skin-sam">
							<sec:authorize ifAllGranted="service.procurementService.update">
								<span id="update" class=""><span class="first-child">
										<button type="button" onclick="onUpdate();"
											class="edit-btn btn btn-success">
											<FONT color="#FFFFFF"> <b><s:text
														name="edit.button" /></b>
											</font>
										</button>
								</span></span>
							</sec:authorize>

							<sec:authorize ifAllGranted="service.procurementService.update">
								<span id="delete" class=""><span class="first-child">
										<button type="button" class="delete-btn btn btn-warning"
											onclick="onDelete()">
											<FONT color="#FFFFFF"> <b><s:text
														name="delete.button" /></b>
											</font>
										</button>
								</span></span>
							</sec:authorize>


							<span id="cancel" class=""><span class="first-child"><button
										type="button" id="back" onclick="onCancel();"
										class="back-btn btn btn-sts">

										<b><FONT color="#FFFFFF"><s:text name="back.button" />
										</font></b>
									</button></span></span>

						</div>
					</div>

				</div>
			</div>
		</div>
	</div>




	<s:form name="updateform" action="procurementProduct_update.action">
		<s:hidden id="id" name="id" />
		<s:hidden key="currentPage" />
	</s:form>
	<s:form name="deleteform" action="procurementProduct_delete.action">
		<s:hidden name="id" value="%{id}" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="listForm" id="listForm"
		action="procurementProduct_list.action">
		<s:hidden key="currentPage" />
	</s:form>


	<script type="text/javascript">
		function onUpdate() {
			document.updateform.submit();
		}

		function onDelete() {

			var val = confirm('<s:text name="confirm.delete"/>');
			if (val)
				
				document.deleteform.submit();
		}

		function onCancel() {
			document.listForm.submit();
		}
	</script>
</body>