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
		<s:hidden key="id" id="productReturnDetailId"/>
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden id="enableBatchNo" name="enableBatchNo" />
		<table class="table table-bordered aspect-detail">
			<tr class="odd">

				<th colspan="13"><s:text name='info.distributionToMobileUser'/></th>
			</tr>
			<tr>
			<s:if test='branchId==null'>
				
			<td><s:text name="app.branch" /></td>
			<td><s:property value="%{getBranchName(productReturndetail.productReturn.branchId)}" />&nbsp;</td>
							
			<td><s:text name="txnDate" /></td>
			<td style="font-weight: bold;"><s:property value="productReturnTxnTime" />&nbsp;</td>
			
			<td><s:text name="updatedUser" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.productReturn.updatedUserName" />&nbsp;</td>
			</s:if>
			<s:else>
			<td><s:text name="txnDate" /></td>
			<td style="font-weight: bold;"><s:property value="productReturnTxnTime" />&nbsp;</td>
			<td><s:text name="updatedUser" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.productReturn.updatedUserName" />&nbsp;</td>
			<td></td>
			<td></td>
			</s:else>
			
			
			</tr>
			<tr>
					<td><s:text name="updatedTime" /></td>
					<td style="font-weight: bold;"><s:property value="productReturndetail.productReturn.updateTime" /></td>
					<td><s:text name="warehouse" /></td>
					<td style="font-weight: bold;"><s:property value="productReturndetail.productReturn.servicePointName" /></td>
<%-- 					<td style="font-weight: bold;"><s:property value="distributiondetail.distribution.servicePointId" />-<s:property value="distributiondetail.distribution.servicePointName" /></td>
 --%>					<td><s:text name="mobileuser" /></td>
					<td style="font-weight: bold;"><s:property value="productReturndetail.productReturn.agentName" /></td>
<%-- 					<td style="font-weight: bold;"><s:property value="distributiondetail.distribution.agentId" />-<s:property value="distributiondetail.distribution.agentName" /></td>
 --%>					</tr>
			<tr>
					
			<td><s:text name="category" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.product.subcategory.name" />&nbsp;</td>
			
			<td><s:text name="product" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.product.name" />&nbsp;</td>
			
			<td><s:text name="distribution.unit" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.product.unit" />&nbsp;</td>
			
			
			
					
			</tr>
			<tr >
			<td ><s:text name="avilableQty" /></td>
			<td style="font-weight: bold;"><s:property value="WarehouseProductStock" />&nbsp;</td>
			<td ><s:text name="distributionStock" /></td>
			<td  style="font-weight: bold;"><s:property value="productReturndetail.quantity" />&nbsp;</td>
			<%-- <td ><s:text name="avilableQty" /></td>
			<td  style="font-weight: bold;"><s:property value="distributiondetail.currentQuantity" /></td> --%>
			<td><s:text name="seasonCodeName" /></td>
			<td style="font-weight: bold;"><s:property value="seasonCodeAndName" /></td>
			
			</tr>
			<tr>
			
			<s:if test="enableBatchNo ==1">
			<td><s:text name="warehouseProduct.batchNo" /></td>
			<td style="font-weight: bold;"><s:property value="productReturndetail.batchNo" />&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			</s:if>
			
			</tr>
			
		</table>
		<div class="yui-skin-sam">
			 <sec:authorize ifAllGranted="service.productReturn.fieldStaff.update">
				<span id="update" class=""><span class="first-child">
						<button type="button" class="edit-btn btn btn-success">
							<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
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
	</s:form>

	<s:form name="updateform" action="productReturnFromFieldStaff_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>
	
	<s:form name="cancelform" action="productReturnFromFieldStaff_list.action">
		<s:hidden key="currentPage" />
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
														.getElementById('productReturnDetailId').value;
												document.updateform.currentPage.value = document.form.currentPage.value;
												document.updateform.submit();
											});

							$('#delete')
									.on(
											'click',
											function(e) {
												if (confirm('<s:text name="confirm.delete"/> ')) {
													document.deleteform.id.value = document
															.getElementById('villageId').value;
													document.deleteform.currentPage.value = document.form.currentPage.value;
													document.deleteform
															.submit();
												}
											});
						});
		
	
	</script>
</body>
</html>