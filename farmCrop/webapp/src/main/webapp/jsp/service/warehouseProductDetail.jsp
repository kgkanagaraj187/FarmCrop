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
		<s:hidden key="id" />

		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden id="enableBatchNo" name="enableBatchNo" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
						<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<%-- <s:text name='info.warehouseStockInfo' /> --%>
								<s:property value="%{getLocaleProperty('info.warehouseStockInfo')}"/>
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(filter.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="txnDate" />
								</p>
								<p class="flexItem">
									<s:property value="filter.transactionDate" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('warehouseProduct.orderNo')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.orderNo" />
								</p>
							</div>
							
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="receiptNo" />
								</p>
								<p class="flexItem">
									<s:property value="filter.receiptNo" />
								</p>
							</div>
							
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('warehouseProduct.vendorId')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.vendor.vendorName" />
								</p>
							</div>
							

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<%-- <s:text name="warehouse" /> --%>
									<s:property value="%{getLocaleProperty('warehouse')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.warehouse.name" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="Total Quantity" />
								</p>
								<p class="flexItem">
									<s:property value="filter.totalQty" />
								</p>
							</div>

							<%-- <div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="totalAmt" />
								</p>
								<p class="flexItem">
									<s:property value="filter.totalAmount" />
									<s:property value="totalAmount" />
								</p>
							</div>
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.tax" />
								</p>
								<p class="flexItem">
									<s:property value="filter.tax" />
								</p>
							</div>
							
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.finalAmt" />
								</p>
								<p class="flexItem">
									<s:property value="filter.finalAmount" />
									<s:property value="finalAmount" />
								</p>
							</div> --%>
						</div>
						<div class="appContentWrapper" style="width: 100%">
						<div class="formContainerWrapper ">
							<h2>
								<%-- <s:text name='info.warehouseStockInfoDetail' /> --%>
								<s:property value="%{getLocaleProperty('info.warehouseStockInfoDetail')}"/>
							</h2>
							<table class="table table-bordered table-hover fillform">
								<tr class="odd">
								
									<th width="12%"><s:text name="categoryCodeName" /></th>
									<th width="12%"><s:text
										name="%{getLocaleProperty('warehouseProduct.product')}" /></th>
										
									<s:if test="enableBatchNo==1">
										<th width="12%"><s:text name="%{getLocaleProperty('warehouseProduct.batchNo')}" /></th>
									</s:if>
									
									<th width="12%" ><s:text name="unit" /></th>
									
									<%-- <th><s:text name="warehouseProduct.costPrice" /></th> --%>
									<th width="12%"><s:text name="currentGoodQty"  /></th>
									<%-- <th><s:text name="currentDQty" /></th> --%>
									<th width="12%"><s:text name="gootQty"  /></th>
									<th width="12%"><s:text name="damgesdQty"   /></th>
									<%-- <th><s:text name="totQty"/></th> --%>
									<%-- <th><s:text name="txnAmnt"/></th>
			<th><s:text name="totoAmount"/></th> --%>
								</tr>
								<s:iterator value="payementDetailList" status="rowstatus">
									<tr class="odd">
									
										<td><s:property value="product.subcategory.name" /></td>
										<td><s:property value="product.name" /></td>
										<s:if test="enableBatchNo==1">
											<td><s:property value="batchNo" /></td>
										</s:if>
									
										
										<td><s:property value="product.unit" /></td>									
										<%-- <td><s:property value="costPrice" /></td> --%>
										<td align="right" ><s:property value="avlStock" /></td>
										
										<%-- <td><s:property value="damagedAvlStock" /></td> --%>

										<td align="right" ><s:property value="stock" /></td>

										<td align="right" > <s:property value="damagedStock"  /> </td>
										
										<%-- 	<td><s:property value="totalStock"/></td> --%>
										<%-- <td><s:property value="totalAmount"/></td>
			<td><s:property value="totalAmount"/></td> --%>

									</tr>
								</s:iterator>
							</table>
						</div>
						</div>
						<div class="yui-skin-sam">
						
							<sec:authorize ifAllGranted="service.warehouseProduct.update">
								<span id="update" class=""><span class="first-child">
										<button type="button" class="edit-btn btn btn-success">
											<FONT color="#FFFFFF"> <b><s:text
														name="edit.button" /></b>
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

	<s:form name="updateform" action="warehouseProduct_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="warehouseProduct_list.action">
		<s:hidden key="currentPage" />
	</s:form>


	<script type="text/javascript">
		$(document).ready(function() {
			$('#update').on('click', function(e) {

				document.updateform.submit();
			});

			$('#delete').on('click', function(e) {
				if (confirm('<s:text name="confirm.delete"/> ')) {
					document.cancelform.submit();
				}
			});
		});
	</script>
</body>
</html>