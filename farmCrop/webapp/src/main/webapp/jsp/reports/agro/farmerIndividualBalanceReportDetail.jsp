<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

</head>

<s:hidden key="filter.id" id="agroTxnId" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="filter.id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2><s:property value="%{getLocaleProperty('info.balanceDetail')}" /></h2>
						</div>
					</div>
				</div>
			</div>
		</div>
<div class="appContentWrapper marginBottom">
	<table class="table table-bordered aspect-detail">
		
			<tr class="odd">
				<th><s:text name="txnTimeGrid" /> (<s:property
						value="%{getGeneralDateFormat().toUpperCase().concat(' HH:MM:SS')}" /></th>
				<th><s:text name="type" /></th>
				<th><s:text name="receiptNoGrid" /></th>
				<th><s:text name="item" /></th>
				<th><s:text name="itemQty" /></th>
				<th><s:text name="debitAmt" /></th>
				<th><s:text name="creditAmt" /></th>
				<th><s:text name="finalBalance" /></th>
			</tr>
			</br>
			<s:iterator value="agroTransactions" status="rowstatus">
				<tr class="odd">
					<td><s:property value="txnTime" /></td>
					<td><s:property value="txnDesc" /></td>
					<td><s:property value="receiptNo" /></td>
					<s:if test="txnType=='314'">
					  <s:if test="txnDesc!='DISTRIBUTION PAYMENT AMOUNT'">
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="item" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="quantity" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="subTotal" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td></td>
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="individulalFinalBal" /></td>
									</tr>
								</s:iterator>

							</table></td>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="finalBalance" /></td>
					
					</s:else>
						</s:if>
						<s:if test="txnType=='316'">
					 <s:if test="txnDesc!='PROCUREMENT PAYMENT'">
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="item" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="numberOfBags" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="subTotal" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td></td>
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="individulalFinalBal" /></td>
									</tr>
								</s:iterator>

							</table></td>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="finalBalance" /></td>
					
					</s:else>
					</s:if>
			<s:if test="txnType=='334'">
					   <td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="balAmount" /></td>
					</s:if>
				</tr>
			</s:iterator>
		</table>

	
	</div>
	<br />

</s:form>

<s:form name="updateform" action="country_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="country_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="country_list.action">
	<s:hidden key="currentPage" />
</s:form>
