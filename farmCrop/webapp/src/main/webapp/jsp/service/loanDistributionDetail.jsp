
<script src="plugins/select2/select2.min.js"></script>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	
</script>
<body>
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="id" />
		<s:hidden key="loanDistribution.id" id="loanDistributionId" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr class="odd">

					<th colspan="13"><s:text name='info.distribution' /></th>
				</tr>
				<tr class="odd">
					<s:if test='branchId==null'>

						<td><s:text name="app.branch" /></td>
						<td><s:property
								value="%{getBranchName(distribution.branchId)}" />&nbsp;</td>
					</s:if>
					<td><s:text name="txnDate" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.loanDate" />&nbsp;</td>

					<td><s:text name="Borrower Given Name" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.farmer.firstName" />&nbsp;<s:property
							value="loanDistribution.farmer.lastName" />&nbsp; 
							<s:if test='loanDistribution.farmer.idproofNo!=null'>
							- <s:property
							value="loanDistribution.farmer.idproofNo" />&nbsp;
							</s:if>
							</td>
					
				</tr>
				<tr class="odd hide">
				<td><s:property value="%{getLocaleProperty('city.name')}" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.village.city.name" />&nbsp;</td>
				
				
				<td><s:text name="City/Town" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.village.name" />&nbsp;</td>
				
				</tr>
				<tr class="odd">

					<%-- <td><s:text name="Account No" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.loanAccNo" />&nbsp;</td> --%>

					<td><s:text name="Vendor" /></td>
					<td style="font-weight: bold;"><s:property value="loanDistribution.vendor.vendorName" />&nbsp;</td>

					<td><s:text name="grossAmt" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.totalCostToFarmer" />&nbsp;</td>
				</tr>
				

			</table>
		</div>
		<br />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr class="odd">
					<th colspan="13"><s:text name='info.distributionDetail' /></th>
				</tr>
				<tr class="odd">
					<th><s:text name="categoryName" /></th>
					<th><s:text name="productName" /></th>
					<th><s:text name="ratePerUnit" /></th>
					<th><s:text name="distQty" /></th>
					<th><s:text name="amount" /></th>
				</tr>
				</br>
				<s:iterator value="distributionDetailList" status="rowstatus">
					<tr class="odd">
						<td><s:property value="product.subcategory.name" /></td>
						<td><s:property value="product.name" /></td>
						<td><s:property value="ratePerUnit" /></td>
						<td><s:property value="quantity" /></td>
						<td><s:property value="amount" /></td>
					</tr>
				</s:iterator>
			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<div class="yui-skin-sam">
			 	<sec:authorize ifAllGranted="service.loanDistributionService.update">
					<span id="update" class=""><span class="first-child">
							<button type="button" class="edit-btn btn btn-success">
								<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize> 
				<sec:authorize ifAllGranted="service.loanDistributionService.delete">
					<span class=""><span class="first-child">
							<button type="button" class="delete-btn btn btn-warning">
								<FONT color="#FFFFFF"> <b><s:text
											name="delete.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize>
				<sec:authorize ifAllGranted="service.loanDistributionService.approval">
					<button type="button" id="accept"  class="btn btn-default"
						onclick="buttonLoanApprove(1)">
						<s:text name="Approve" />
					</button>
					<button type="button" id="reject" class="btn btn-default"
						onclick="buttonLoanApprove(2)">
						<s:text name="Reject" />
					</button>
					</sec:authorize>
				<span id="cancel" class=""><span class="first-child"><button
							type="button" class="back-btn btn btn-sts">
							<b><FONT color="#FFFFFF"><s:text name="back.button" />
							</font></b>
						</button></span></span>
			</div>
		</div>
	</s:form>

	<s:form name="updateform" action="loanDistribution_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="loanDistribution_list.action">
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="deleteform" action="loanDistribution_delete.action">
		<s:hidden key="id" />
	</s:form>


	<script type="text/javascript">
		$(document).ready(function() {
			$('#update').on('click',function(e) {
				document.updateform.id.value = document.getElementById('loanDistributionId').value;
				document.updateform.currentPage.value = document.form.currentPage.value;
				document.updateform.submit();
			});
			$('#delete').on('click',function(e) {
				if (confirm('<s:text name="confirm.delete"/> ')) {
					document.deleteform.id.value = document.getElementById('villageId').value;
					document.deleteform.currentPage.value = document.form.currentPage.value;
					document.deleteform.submit();
					}
			});
			$('#delete').on('click',function(e) {
				if (confirm('<s:text name="confirm.delete"/> ')) {
					document.deleteform.id.value = document.getElementById('villageId').value;
					document.deleteform.currentPage.value = document.form.currentPage.value;
					document.deleteform.submit();
					}
			});
			
			$('.delete-btn').on('click',function(e) {
				if (confirm('<s:text name="delete.rowGrid"/> ')) {
					document.deleteform.id.value =document.getElementById('distibutionId').value;
					document.deleteform.submit();
					}
			});
		});
		
		
		
		function buttonLoanApprove(val) {
			var temp = <s:property value="loanDistribution.id" />;
			var pCode = "";

			$.ajax({
				type : "POST",
				async : false,
				url : "loanDistribution_updateLoanStatus.action",
				data : {
					id : temp,
					LStatus : val,
					productCode : pCode
				},
				 success : function(result) {
					 cancelform.submit();
				} 

			});

		}
	</script>
</body>
</html>