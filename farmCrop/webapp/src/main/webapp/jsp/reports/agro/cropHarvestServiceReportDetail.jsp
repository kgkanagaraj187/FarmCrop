<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<s:hidden key="cropHarvest.id" id="cropHarvestId" />
	<font color="red"> <s:actionerror /></font>
	<s:form name="form" cssClass="fillform">


		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div align="right">
							<b><s:text name="season" /> : </b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>

						<div class="formContainerWrapper dynamic-form-con">

							<h2>
								<s:text name="info.cropHarvestReport" />
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(cropHarvest.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="harvestDate" />
								</p>
								<p class="flexItem">
									<s:date name="cropHarvest.harvestDate" format="dd/MM/yyyy" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmerId" />
								</p>
								<p class="flexItem">
									<s:property value="cropHarvest.farmerId" />
									-
									<s:property value="cropHarvest.farmerName" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmCode" />
								</p>
								<p class="flexItem">
									<s:property value="cropHarvest.farmCode" />
									-
									<s:property value="cropHarvest.farmName" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="receiptNo" />
								</p>
								<p class="flexItem">
									<s:property value="cropHarvest.receiptNo" />
								</p>
							</div>
							<s:if test="currentTenantId=='chetna'">
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="storageIn" />
									</p>

									<p class="flexItem">
										<s:if test="cropHarvest.storageIn=='99'">
											<s:property value="storageId[cropHarvest.storageIn]" />-<s:property
												value="cropHarvest.otherStorageInType" />
										</s:if>
										<s:else>
											<s:property value="storageId[cropHarvest.storageIn]" />
										</s:else>
									</p>
								</div>
								
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="packedIn" />
									</p>

									<p class="flexItem">
										<s:if test="cropHarvest.packedIn=='99'">
											<s:property value="packId[cropHarvest.packedIn]" />-<s:property
												value="cropHarvest.otherPackedInType" />
										</s:if>
										<s:else>
											<s:property value="packId[cropHarvest.packedIn]" />
										</s:else>
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="totalQty" />
								</p>
								<p class="flexItem">
									<s:set var="kgQty" value="0" />
									<s:set var="quintalQty" value="0" />
									<s:set var="quantity" value="0" />
									<s:iterator value="cropHarvest.cropHarvestDetails"
										var="cropDetail">
										<s:if test="currentTenantId=='pratibha'">
											<s:if
												test="#cropDetail.crop.unit.equalsIgnoreCase('kgs') || #cropDetail.crop.unit.equalsIgnoreCase('kg')">
												<s:set var="kgQty" value="%{#cropDetail.qty+#kgQty}" />
											</s:if>
											<s:if
												test="#cropDetail.crop.unit.equalsIgnoreCase('Quintals') || #cropDetail.crop.unit.equalsIgnoreCase('Quintal')">
												<s:set var="quintalQty"
													value="%{#cropDetail.qty+#quintalQty}" />
											</s:if>
										</s:if>
										<s:else>
											<s:set var="quantity" value="%{#cropDetail.qty+#quantity}" />
										</s:else>
									</s:iterator>
									<s:if test="currentTenantId=='pratibha'">
										<s:set var="metricTon"
											value="getText('{0,number,###0.000}',{(#kgQty/1000)+(#quintalQty/10)})" />
										<td width="65%"><s:property value="%{#metricTon}" />&nbsp;(MT)</td>
									</s:if>
									<s:else>
										<s:set var="metricTon" value="%{#quantity/1000}" />
										<td width="65%">
										<%-- <s:property value="%{#metricTon}" />  --%>
										<s:property value="%{#metricTon}" />MT&nbsp;
										</td>
									</s:else>
								</p>
							</div>
							
						</div>
						<div class="formContainerWrapper dynamic-form-con">
							<s:if test="cropHarvest.cropHarvestDetails.size>0">
								<h2>
									<s:text name="info.cropDetails" />
								</h2>
								<table class="table table-bordered aspect-detail">

									<tr class="odd">
										<th><s:text name="cropType" /></th>
										<th><b><s:text name="crop" /></b></th>
										<th><b><s:text name="variety" /></b></th>
										<th><b><s:text name="grade" /></b></th>
										<th><b><s:text name="unit" /></b></th>
										<th><b><s:text
													name="%{getLocaleProperty('quantitykg')}" /></b></th>
										<s:if test="currentTenantId=='pratibha'">
											<th><b><s:text
														name="%{getLocaleProperty('quantitykg')}" /></b>&nbsp;in MT</th>
										</s:if>
										<%-- <s:if test="currentTenantId=='chetna'">
											<th><b><s:text name="prices" /></b></th>
										</s:if>
										<s:else>
											<th><b><s:text
														name="%{getLocaleProperty('price/kg')}" /></b></th>
										</s:else>
										<th><b><s:text name="subTotal" /></b></th> --%>
									</tr>

									<s:iterator value="cropHarvest.cropHarvestDetails">
										<tr>

											<td>
											<s:if test="currentTenantId=='pratibha'">
											<s:if test="cropType==0">
													<s:text name="Main Crop"></s:text>
												</s:if> <s:elseif test="cropType==1">
													<s:text name="Inter Crop"></s:text>
												</s:elseif> 
												<s:elseif test="cropType==2">
													<s:text name="Border Crop"></s:text>
												</s:elseif> 
												<s:elseif test="cropType==3">
													<s:text name="Cover Crop"></s:text>
												</s:elseif>
													<s:elseif test="cropType==4">
													<s:text name="Plant On Bund Crop"></s:text>
												</s:elseif>
												
													<s:elseif test="cropType==5">
													<s:text name="Trap Crop"></s:text>
												</s:elseif>
												
												</s:if>
											<s:else>	
											<s:if test="cropType==0">
													<s:text name="Main Crop"></s:text>
												</s:if> <s:elseif test="cropType==1">
													<s:text name="Inter Crop"></s:text>
												</s:elseif> 
												
												
												<s:else>
													<s:text name="Border"></s:text>
												</s:else></s:else></td>
                                              

											<td><s:property value="crop.name" /></td>
											<td><s:property value="variety.name" /></td>
											<td><s:property value="grade.name" /></td>
											<td><s:property value="crop.unit" /></td>
											<td><s:property
													value="getText('{0,number,###0.000}',{qty})" /></td>
											<s:if test="currentTenantId=='pratibha'">
												<s:if
													test="crop.unit.equalsIgnoreCase('kgs') || crop.unit.equalsIgnoreCase('kg')">
													<td><s:set var="qty"
															value="getText('{0,number,###0.000}',{qty/1000})" /> <s:property
															value="%{#qty}" /></td>
												</s:if>
												<s:if
													test="crop.unit.equalsIgnoreCase('quintals') || crop.unit.equalsIgnoreCase('Quintal')">
													<td><s:set var="qty"
															value="getText('{0,number,###0.000}',{qty/10})" /> <s:property
															value="%{#qty}" /></td>
												</s:if>
											</s:if>
											<%-- <td><s:property
													value="getText('{0,number,#,##0.00}',{price})" /></td>
											<td><s:property
													value="getText('{0,number,#,##0.00}',{subTotal})" /></td> --%>
										</tr>
									</s:iterator>
								</table>
							</s:if>
						</div>
						<div class="yui-skin-sam">

							<span id="update" class=""><span class="first-child"><button
										type="button" onclick="onUpdate();"
										class="edit-btn btn btn-success">
										<b><FONT color="#FFFFFF"><s:text name="edit.button" />

										</font></b>
									</button></span></span> <span id="delete" class=""><span class="first-child"><button
										type="button" onclick="onDelete();"
										class="delete-btn btn btn-warning">
										<b><FONT color="#FFFFFF"><s:text
													name="delete.button" /> </font></b>
									</button></span></span> <span id="cancel" class=""><span class="first-child"><button
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



	</s:form>

	<s:form name="updateform"
		action="cropHarvestServiceReport_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="deleteform"
		action="cropHarvestServiceReport_delete.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>
	<s:form name="listForm" id="listForm"
		action="cropHarvestServiceReport_list">
		<s:hidden name="currentPage" />
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