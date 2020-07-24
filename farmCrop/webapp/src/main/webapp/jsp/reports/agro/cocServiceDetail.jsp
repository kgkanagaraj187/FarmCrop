<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery(".back-btn").click(function() {
			document.cancelform.submit();
		});

		jQuery("#update").click( function() {
			document.updateform.submit();
		});
		
		
		jQuery("#delete").click( function() {
			document.deleteform.submit();
		});
		
		var tenantId='<s:property value="getCurrentTenantId()"/>';
		
		
			$(".hideSoilPrepare").show();
		

	});
</script>
<s:hidden key="cultivation.id" id="id" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="command" />
	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
						<s:set var="recordIdentifier" value="%{false}" />
							<s:if test="cultivationList.size()>0">
							<div class="aPanel cost_info">
								<div class="aTitle">
									<h2>
										<s:property
											value="%{getLocaleProperty('info.costCultivation')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#costInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="#costInfo">
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop1" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation"
											status="status">
											<s:if
												test="%{#cultivation.landTotal!='' && #cultivation.landTotal!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop1">
													<s:set var="breakLoop1" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property
																	value="%{getLocaleProperty('landPreparation')}" /></b>
														</p>
														<tr class="odd">
															
																<th colspan="4"><s:text name="date" /></th>
																<%-- <th colspan="2" class="hideSoilPrepare"><s:text
																		name="ploughing" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2" class="hideSoilPrepare"><s:text
																		name="ridgeFurrow" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="4" class="hideSoilPrepare">
																<s:text name="soilPreparation" />(<s:property
																		value="%{getCurrencyType()}" />)</th> --%>
																<th colspan="4"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															
															<%-- 
																<th colspan="2"><s:text name="date" /></th>
																<th colspan="2" class="hideSoilPrepare"><s:text
																		name="ploughing" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2" class="hideSoilPrepare"><s:text
																		name="ridgeFurrow" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2"><s:text name="soilPreparation" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															 --%>
														</tr>
												</s:if>
												<tr>
														<td colspan="4"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd-MM-yyyy hh:mm:ss a" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.landPloughing)}" /></td>
														<td colspan="2" class="hideSoilPrepare"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.ridgeFurrow)}" /></td>
														<td colspan="4" class="hideSoilPrepare"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.soilPreparation)}" /></td> --%>
														<td colspan="4"><s:property
																value="%{#cultivation.landTotal}" /></td>
													<%-- 
														<td colspan="2"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd-MM-yyyy hh:mm:ss a" /></td>
														<td colspan="2" class="hideSoilPrepare"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.landPloughing)}" /></td>
														<td colspan="2" class="hideSoilPrepare"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.ridgeFurrow)}" /></td>
														<td colspan="2"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultivation.soilPreparation)}" /></td>
														<td colspan="2"><s:property
																value="%{#cultivation.landTotal}" /></td>
													 --%>
												</tr>
											</s:if>
										</s:iterator>
									</table>
									
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop2" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">

											<s:if
												test="%{#cultivation.totalSowing!='' && #cultivation.totalSowing!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop2">
													<s:set var="breakLoop2" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property value="%{getLocaleProperty('sowing')}" /></b>
														</p>
														<tr class="odd">
																<th colspan="6"><s:text name="date" /></th>
																<%-- <th colspan="3" class="hideSoilPrepare"><s:text
																		name="seedCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="sowingTreat" />(<s:property
																		value="%{getCurrencyType()}" />)</th> --%>
																<%-- <th colspan="2"><s:text name="labourCost-men"/></th>
											<th><s:text name="labourCost-women"/></th>  --%>
																<th colspan="6"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															<%-- 
																<th colspan="3"><s:text name="date" /></th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="seedCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="3" class="hideSoilPrepare"><s:text
																		name="sowingTreat" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
																<th colspan="2"><s:text name="labourCost-men"/></th>
											<th><s:text name="labourCost-women"/></th> 
																<th colspan="3"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															 --%>
														</tr>
												</s:if>
												<div class="dynamic-flexItem2">
													<tr>
															<td colspan="6"><s:date
																	name="%{#cultivation.expenseDate}"
																	format="dd/MM/yyyy hh:mm:ss a" /></td>
															<%-- <td colspan="3" class="hideSoilPrepare"><s:property
																	value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:property
																	value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingTreat)}" /></td> --%>
															<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostWomen)}"/></td> --%>
															<td colspan="6"><s:property
																	value="%{#cultivation.totalSowing}" /></td>
														<%-- 
															<td colspan="3"><s:date
																	name="%{#cultivation.expenseDate}"
																	format="dd/MM/yyyy hh:mm:ss a" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:property
																	value="%{getFormatted('{0,number,#0.00##}',#cultivation.seedCost)}" /></td>
															<td colspan="3" class="hideSoilPrepare"><s:property
																	value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingTreat)}" /></td>
															<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.sowingCostWomen)}"/></td>
															<td colspan="3"><s:property
																	value="%{#cultivation.totalSowing}" /></td>
														 --%>
													</tr>
												</div>
											</s:if>
										</s:iterator>
									</table>

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop3" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">

											<s:if
												test="%{#cultivation.totalGap!='' && #cultivation.totalGap!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop3">
													<s:set var="breakLoop3" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property
																	value="%{getLocaleProperty('gapFilling')}" /></b>
														</p>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<%-- <th><s:text name="labourCost-men"/></th>
			<th ><s:text name="labourCost-women"/></th> --%>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>
													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<%-- td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.gapCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.gapCostWomen)}"/></td> --%>
													<td colspan="6"><s:property
															value="%{#cultivation.totalGap}" /></td>
												</tr>
											</s:if>
										</s:iterator>
									</table>
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop4" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">

											<s:if
												test="%{#cultivation.totalWeed!='' && #cultivation.totalWeed!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop4">

													<s:set var="breakLoop4" value="%{false}" />

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property value="%{getLocaleProperty('weeding')}"/></b>
														</p>
														<%-- 					<tr style="background-color: #eaf2f8">
													<td colspan="12"><span style='font-weight: bold;'><s:text
																name="weeding" /></span></td>
												</tr> --%>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<%-- <th><s:text name="labourCost-men"/></th>
			<th><s:text name="labourCost-women"/></th> --%>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>
													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.weedingCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.weedingCostWomen)}"/></td> --%>
													<td colspan="6"><s:property
															value="%{#cultivation.totalWeed}" /></td>
												</tr>
											</s:if>
										</s:iterator>
									</table>
									
										<table class="table table-bordered aspect-detail">
											<s:set var="breakLoop5" value="%{true}" />
											<s:iterator value="cultivationList" var="cultivation">

												<s:if
													test="%{#cultivation.totalCulture!='' && #cultivation.totalCulture!=null }">
													<s:set var="recordIdentifier" value="%{true}" />
													<s:if test="#breakLoop5">
														<s:set var="breakLoop5" value="%{false}" />
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<b><s:property
																		value="%{getLocaleProperty('interCultural')}" /></b>
															</p>
															<tr>
																<th colspan="6"><s:text name="date" /></th>
																<%-- <th><s:text name="labourCost-men"/></th>
			<th><s:text name="labourCost-women"/></th> --%>
																<th colspan="6"><s:text name="totalCost" />(<s:property
																		value="%{getCurrencyType()}" />)</th>
															</tr>
													</s:if>
													<tr>

														<td colspan="6"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd/MM/yyyy hh:mm:ss a" /></td>
														<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.cultureCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.cultureCostWomen)}"/></td> --%>
														<td colspan="6"><s:property
																value="%{#cultivation.totalCulture}" /></td>
													</tr>
												</s:if>
											</s:iterator>
									</table>
									
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop6" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">

											<s:if
												test="%{#cultivation.totalIrrigation!='' && #cultivation.totalIrrigation!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop6">
													<s:set var="breakLoop6" value="%{false}" />

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property
																	value="%{getLocaleProperty('irrigation')}" /></b>
														</p>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<%-- <th><s:text name="labourCost-men"/></th>
			<th><s:text name="labourCost-women"/></th> --%>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>
													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.irrigationCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.irrigationCostWomen)}"/></td> --%>
													<td colspan="6"><s:property
															value="%{#cultivation.totalIrrigation}" /></td>
												</tr>
											</s:if>
										</s:iterator>
									</table>
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop15" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation"
											status="status">

											<s:if
												test="%{#cultivation.labourCost!='' && #cultivation.labourCost!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop15">
													<s:set var="breakLoop15" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property
																	value="%{getLocaleProperty('labourDetail')}" /></b>
														</p>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>
													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<td colspan="6"><s:property
															value="%{#cultivation.labourCost}" /></td>


												</tr>
											</s:if>
										</s:iterator>
									</table>
								
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop10" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">
											<s:if
												test="%{#cultivation.totalHarvest!='' && #cultivation.totalHarvest!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop10">
													<s:set var="breakLoop10" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b> <s:property
																	value="%{getLocaleProperty('harvesting')}" /></b>
														</p>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<%-- <th><s:text name="labourCost-men"/></th>
			<th><s:text name="labourCost-women"/></th> --%>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>

													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<%-- <td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.harvestCostMen)}"/></td>
			<td><s:property value="%{getFormatted('{0,number,#0.00##}',#cultivation.harvestCostWomen)}"/></td> --%>
													<td colspan="6"><s:property
															value="%{#cultivation.totalHarvest}" /></td>
												</tr>
											</s:if>
										</s:iterator>
									</table>
									
										<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop11" value="%{true}" />
										<s:iterator value="cultivationList" var="cultivation">
											<s:if
												test="%{#cultivation.totalExpense!='' && #cultivation.totalExpense!=null}">
												<s:set var="recordIdentifier" value="%{true}" />
												<s:if test="#breakLoop11">
													<s:set var="breakLoop11" value="%{false}" />
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<b><s:property
																	value="%{getLocaleProperty('otherExpenses')}" /></b>
														</p>
														<tr>
															<th colspan="6"><s:text name="date" /></th>
															<%-- <th colspan="2"><s:text name="packingMaterial" /></th>
											<th colspan="2"><s:text name="transport" /></th>
											<th colspan="2"><s:text name="miscellaneous" /></th> --%>
															<th colspan="6"><s:text name="totalCost" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
														</tr>
												</s:if>
												<tr>
													<td colspan="6"><s:date
															name="%{#cultivation.expenseDate}"
															format="dd/MM/yyyy hh:mm:ss a" /></td>
													<%-- <td colspan="2"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.packingMaterial)}" /></td>
										<td colspan="2"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.transport)}" /></td>
										<td colspan="2"><s:property
												value="%{getFormatted('{0,number,#0.00##}',#cultivation.miscellaneous)}" /></td> --%>
													<td colspan="6"><s:property
															value="%{#cultivation.totalExpense}" /></td>
												</tr>
											</s:if>

										</s:iterator>
									</table>
								
								</div>
							</div>
						</s:if>
						<s:if
							test='pesticidesList.size()>0 || fymsList.size()>0 || fertilizersList.size()>0'>
							<div class="aPanel quantity_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:property
											value="%{getLocaleProperty('info.quantityCultivation')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#costInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>

								<div class="aContent dynamic-form-con" id="#quantityInfo">

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop7" value="%{true}" />
										<s:if test='fertilizersList.size()>0'>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop7">
												<s:set var="breakLoop7" value="%{false}" />

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<b><s:property
																value="%{getLocaleProperty('fertilizer')}" /></b>
													</p>
													<tr>
									
										<th colspan="6"><s:text name="date" /></th>
										<s:set var="recordIdentifier" value="%{true}" />
										<%-- <th colspan="2" class="hideSoilPrepare"><s:text name="fertiType" /></th>
										<th colspan="2"><s:text name="qtyApplied" /></th> --%>
										<th colspan="6"><s:text name="costofFertilizer" />(<s:property value="%{getCurrencyType()}" />)</th>
										<%-- <th colspan="2" class="hideSoilPrepare"><s:text name="usageLevel" /></th> --%>
										

									</tr>
											</s:if>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:iterator value="fertilizersList" var="cultiDetail">
												<tr>
											
													
														<td colspan="6"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd/MM/yyyy hh:mm:ss a" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="%{#cultiDetail.fertilizerName}" /></td>
														<td colspan="2"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td> --%>
														<td colspan="6"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="usageLevelName" /></td> --%>
													

												</tr>
											</s:iterator>
										</s:if>
									</table>

									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop8" value="%{true}" />


										<s:if test='pesticidesList.size()>0'>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop8">
												<s:set var="breakLoop8" value="%{false}" />

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<b><s:property
																value="%{getLocaleProperty('pesticide')}" /></b>
													</p>
													<tr>
														
															<th colspan="6"><s:text name="date" /></th>
															<s:set var="recordIdentifier" value="%{true}" />
															<%-- <th colspan="2" class="hideSoilPrepare"><s:text
																	name="pesticideType" /></th>
															<th colspan="2"><s:text name="qtyApplied" /></th> --%>
															<th colspan="6"><s:text
																	name="costofPesticide" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
															<%-- <th colspan="2" class="hideSoilPrepare"><s:text
																	name="usageLevel" /></th> --%>

													

													</tr>
											</s:if>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:iterator value="pesticidesList" var="cultiDetail">
												<tr>
													
														<td colspan="6"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd/MM/yyyy hh:mm:ss a" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="%{#cultiDetail.fertilizerName}" /></td>
														<td colspan="2"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td> --%>
														<%-- <td colspan="6"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td> --%>
																<td colspan="6"><s:property
																value="%{#cultivation.totalPesticide}" /></td>
													<%-- 	<td colspan="2" class="hideSoilPrepare"><s:property
																value="usageLevelName" /></td> --%>
													

												</tr>
											</s:iterator>


										</s:if>
									</table>
									<table class="table table-bordered aspect-detail">
										<s:set var="breakLoop9" value="%{true}" />
										<s:if test='fymsList.size()>0'>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:if test="#breakLoop9">
												<s:set var="breakLoop9" value="%{false}" />
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<b> <s:property value="%{getLocaleProperty('fym')}" /></b>
													</p>
													<tr>
														
															<th colspan="6"><s:text name="date" /></th>
															<s:set var="recordIdentifier" value="%{true}" />
															<%-- <th colspan="2" class="hideSoilPrepare"><s:text
																	name="fymType" /></th>
															<th colspan="2"><s:text name="qtyApplied" /></th> --%>
															<th colspan="6"><s:text
																	name="costofFYM" />(<s:property
																	value="%{getCurrencyType()}" />)</th>
															<%-- <th colspan="2" class="hideSoilPrepare"><s:text
																	name="usageLevel" /></th> --%>
														
													</tr>
											</s:if>
											<s:set var="recordIdentifier" value="%{true}" />
											<s:iterator value="fymsList" var="cultiDetail">
												<tr>
													
														<td colspan="6"><s:date
																name="%{#cultivation.expenseDate}"
																format="dd/MM/yyyy hh:mm:ss a" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="%{#cultiDetail.fertilizerName}" /></td>
														<td colspan="2"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.qty)}" /></td> --%>
														<td colspan="6"><s:property
																value="%{getFormatted('{0,number,#0.00##}',#cultiDetail.cost)}" /></td>
														<%-- <td colspan="2" class="hideSoilPrepare"><s:property
																value="usageLevelName" /></td> --%>
													

												</tr>
											</s:iterator>
										</s:if>
									</table>

								</div>
							</div>
						</s:if>
<s:if test="cultivationList.size()<=0 && pesticidesList.size()=0 && fymsList.size()=0 && fertilizersList.size()=0">
<s:set var="recordIdentifier" value="%{true}" />
			No records found....
</s:if>
						<div class="yui-skin-sam">

							<sec:authorize ifAllGranted="service.coc.update">
									<span id="update" class=""><span class="first-child">
									<button type="button" class="edit-btn btn btn-success">
										<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
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
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>

	<br />

</s:form>



<s:form name="cancelform" action="cultivation_list.action?type=service">
    <s:hidden key="id" />
    <s:hidden key="currentPage"/>
</s:form>


<s:form name="updateform" action="cultivation_update.action?type=service">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>


<script type="text/javascript">
	
</script>