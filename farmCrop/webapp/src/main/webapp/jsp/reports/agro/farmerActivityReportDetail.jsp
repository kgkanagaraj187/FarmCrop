<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<script type="text/javascript">
	
jQuery(document).ready(function() {
	var filterMapReport = '<s:property value="filterMapReport"/>';
	var postdataReport = '<s:property value="postdataReport"/>';
	jQuery(".back-btn").click(function() {
		document.cancelform.submit();
	});
});	
	
</script>

<s:hidden key="farmer.id" id="id" />

<font color="red"> <s:actionerror /></font>


<s:form name="form" cssClass="fillform">

	<s:hidden key="command" />

	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
						<s:if test="procurementList.size()>0">
							<div class="aPanel procurement_info">
								<div class="aTitle">
									<h2>
										<s:property value="%{getLocaleProperty('info.procurement')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#procurementInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>



								<div class="aContent dynamic-form-con" id="#procurementInfo">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop1" value="%{true}" />
										<s:iterator value="procurementList" var="procurement"
											status="status">
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop1">
												<s:set var="breakLoop1" value="%{false}" />

												<tr class="odd">
													<th colspan="2"><s:text name="date" /></th>
													<th colspan="2"><s:text name="totalAmt" /></th>
													<th colspan="2"><s:text name="paymentAmt" /></th>
												</tr>

											</s:if>
											<tr>
												<td colspan="2"><s:date
														name="%{#procurement.createdDate}"
														format="dd-MM-yyyy" /></td>

												<td colspan="2"><s:property
														value="%{#procurement.totalProVal}" /></td>

												<td colspan="2"><s:property
														value="%{#procurement.paymentAmount}" /></td>
											</tr>
											<tr>
												<th><s:text name="sno" /></th>
												<th><s:text name="product" /></th>
												<th><s:text name="variety" /></th>
												<th><s:text name="grade" /></th>
												<th><s:text name="totalNetWeight" /></th>
												<th><s:text name="noOfBags" /></th>
											</tr>
											<s:iterator value="procurementDetails" var="procurementDet"
												status="status">
												<tr>
													<th><s:property value="#status.count" /></th>
													<td><s:property
															value="%{#procurementDet.procurementProduct.name}" /></td>
													<td><s:property
															value="%{#procurementDet.procurementGrade.procurementVariety.name}" /></td>
													<td><s:property
															value="%{#procurementDet.procurementGrade.name}" /></td>
													<td><s:property value="%{#procurementDet.NetWeight}" /></td>
													<td><s:property
															value="%{#procurementDet.numberOfBags}" /></td>
												</tr>

											</s:iterator>



										</s:iterator>
									</table>
								</div>
							</div>
						</s:if>
					</div>



					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
						<s:if test="distributionList.size()>0">
							<div class="aPanel distribution_info">
								<div class="aTitle">
									<h2>
										<s:property value="%{getLocaleProperty('info.distribution')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#distributionInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>



								<div class="aContent dynamic-form-con" id="#distributionInfo">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop2" value="%{true}" />
										<%-- <s:iterator value="distributionList" var="distribution"
											status="status"> --%>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop2">
												<s:set var="breakLoop2" value="%{false}" />

												<tr class="odd">
													<th colspan="4"><s:text name="date" /></th>
													<th colspan="4"><s:text name="product" /></th>
													<th colspan="4"><s:text name="distributionQuantity" /></th>
													<th colspan="4"><s:text name="existQuantity" /></th>
													<th colspan="4"><s:text name="availQuantity" /></th>
													<th colspan="4"><s:text name="finalamount" /></th>
												</tr>

											</s:if>
												<s:iterator value="distributionDetails"
													var="distributionDet" status="status">
											<tr>
												<td colspan="4"><s:date name="%{#distributionDet.distribution.txnTime}"
														format="dd-MM-yyyy" /></td>

													<td colspan="4"><s:property
															value="%{#distributionDet.product}" /></td>
													<td colspan="4"><s:property
															value="%{#distributionDet.quantity}" /></td>
													<td colspan="4"><s:property
															value="%{#distributionDet.existingQuantity}" /></td>
													<td colspan="4"><s:property
															value="%{#distributionDet.currentQuantity}" /></td>
												

												<td colspan="4"><s:property
														value="%{#distributionDet.distribution.finalAmount}" /></td>

											</tr>
											</s:iterator>

										<%-- </s:iterator> --%>
									</table>
								</div>
							</div>
						</s:if>
					</div>

					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
						<s:if test="trainingList.size()>0">
							<div class="aPanel training_info">
								<div class="aTitle">
									<h2>
										<s:property value="%{getLocaleProperty('info.training')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#trainingInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>



								<div class="aContent dynamic-form-con" id="#trainingInfo">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop3" value="%{true}" />
										<s:iterator value="trainingList" var="training"
											status="status">
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop3">
												<s:set var="breakLoop3" value="%{false}" />

												<tr class="odd">
													<th colspan="4"><s:text name="trainingDate" /></th>
													<th colspan="4"><s:text name="assistantName" /></th>
													<th colspan="4"><s:text name="timeTaken" /></th>
													<th colspan="4"><s:text name="remarks" /></th>
												</tr>

											</s:if>
											<tr>
												<td colspan="4"><s:date
														name="%{#training.trainingDate}"
														format="dd-MM-yyyy" /></td>
											
												<td colspan="4"><s:property
														value="%{#training.trainingAssistName}" /></td>

												<td colspan="4"><s:property
														value="%{#training.timeTakenForTraining}" /></td>

												<td colspan="4"><s:property
														value="%{#training.remarks}" /></td>
											</tr>

										</s:iterator>
									</table>
								</div>
							</div>
						</s:if>
					</div>

					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
						<s:if test="periodicInspectionList.size()>0">
							<div class="aPanel periodicInspection_info">
								<div class="aTitle">
									<h2>
										<s:property
											value="%{getLocaleProperty('info.periodicInspection')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#periodicInspectionInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>



								<div class="aContent dynamic-form-con"
									id="#periodicInspectionInfo">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop4" value="%{true}" />
										<s:iterator value="periodicInspectionList"
											var="periodicInspection" status="status">
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop4">
												<s:set var="breakLoop4" value="%{false}" />

												<tr class="odd">
													<th colspan="4"><s:text name="inspectionDate" /></th>
													<th colspan="4"><s:text name="farmId" /></th>
													<th colspan="4"><s:text name="purpose" /></th>
													<th colspan="4"><s:text name="remarks" /></th>
												</tr>

											</s:if>
											<tr>
												<td colspan="4"><s:date name="inspectionDate" format="dd-MM-yyyy"/></td>
												<td colspan="4"><s:property value="farm.farmName" /></td>
												<td colspan="4"><s:property value="purpose" /></td>
												<td colspan="4"><s:property value="remarks" /></td>
											</tr>

										</s:iterator>
									</table>
								</div>
							</div>
						</s:if>
					</div>
					<s:if test="!dynamicMenuList.isEmpty()">
					<s:iterator value="dynamicMenuList" var="menus"	>
					<s:set var="spliited" value='key.split("~")[1]'/>
					<div class="formContainerWrapper">
							<div class="aPanel dynamic_menu">
								<div class="aTitle">
									<h2>
										<s:property value='key.split("~")[0]'/>
										<div class="pull-right">
											<a class="aCollapse" href="#<s:property value='key.split("~")[0]'/>"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>



								<div class="aContent dynamic-form-con" id="#<s:property value='key.split("~")[0]'/>">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop3" value="%{true}" />
										<s:iterator value="value" var="menu">
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop3">
												<s:set var="breakLoop3" value="%{false}" />

												<tr class="odd">
													<th colspan="4"><s:text name="date" /></th>
												<th colspan="4"><s:text name="agent" /></th>
												<s:iterator value="#menus.key.split('~')[1].split('\\\|')" var="records">
										         <th colspan="4" ><s:property/></th>
											
											</s:iterator>
												</tr>

											</s:if>
											<tr>
											
												<td colspan="4"><s:date
														name="%{#menu.date}"
														format="dd-MM-yyyy" /></td>
														
														<td colspan="4"><s:property value="createdUser"/></td>
												<s:iterator value="reportValues" var="records">
										         <td colspan="4" ><s:property/></td>
											
											</s:iterator>
												
											</tr>

										</s:iterator>
									</table>
								</div>
							</div>
						
					</div>
					</s:iterator>
</s:if>
					<div class="yui-skin-sam">

						<span id="cancel" class=""> <span class="first-child">
								<button type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button>
						</span>
						</span>
					</div>


				</div>
			</div>
		</div>
	</div>

</s:form>



<s:form name="cancelform" action="farmerActivityReport_list.action?">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
	<s:hidden name="filterMapReport" id="filterMapReport" />
	<s:hidden name="postdataReport" id="postdataReport" />
</s:form>

