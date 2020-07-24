<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="assets/client/demo/css/agro-theme.css" />
<style>
body {
	background: none !important;
}

@media print {
	#prntCtrl {
		display: none;
	}
	.parentTbl td,th {
		width: 20%;
	}
}

.alnRht {
	text-align: right;
}

.fontBold {
	font-weight: bold;
}

.subTable td,th {
	border-right: 1px solid black;
	border-bottom: 1px solid black;
}

.subTable td:first-child,th:first-child {
	border-left: 1px solid black;
}

.subTable tr:first-child>th {
	border-top: 1px solid black;
}

/* .subTable tr:last-child>td {
	font-weight: bold;
}
 */
.subTable td {
	padding-right: 5px;
}

.subTable td,th {
	width: 20% !important;
}

#prntCtrl {
	text-align: center;
	padding: 10px;
}

.logoImg {
	border: 1px solid black;
}

.signatureLine {
	height: 80px;
}

.fstTbl td {
	width: 33%;
}
.borderTop{
	border-top: 1px solid #a8a8a8;
}
.borderBottom{
	border-bottom: 1px solid #a8a8a8;
}
.borderTop td {
	border-top: 1px solid #a8a8a8;
}

.borderBottom td {
	border-bottom: 1px solid #a8a8a8;
}

.padding10 {
	padding-top: 10px;
	padding-bottom: 10px;
}

.subTable td:first-child {
	padding-left: 5px;
}

td {
	padding: 3px;
}

.firstTdSpan{
	float:left;
	width:150px;
	border:0px solid red;
	text-align: right;
    width: 135px;
}
.secondTdSpan{
	float:left;
	width:150px;
	border:0px solid red;
	text-align: right;
    width: 70px;
}
.rNoRow td{
	padding-top:10px;
}

.grayHeader > th, .grayHeader > td{
	background-color:#E8E8E8 ;
}
.finalBalCls{
	border-top:1px solid black;
	border-bottom:1px solid black;
	font-weight:bold;
}

</style>
</head>
<script type="text/javascript">
		var referenceWindow;
		function printReceipt(){
			window.print();
		}
		function closeWindow(){
			try{				
				referenceWindow.close();
			}catch(e){				
				this.close();
			}
		}
		</script>
<body>
<div style="border: 1px solid black; padding: 20px;">
<table border="0" width="100%" class="fstTbl" cellspacing="0">
	<tr>
		<td colspan="2" align="center">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td >
			        <div class="center">
	            <img src="login_populateLogo.action?logoType=app_logo" style="width:130px;height:100px;"/>
	        </div>
				
				</td>
				 <s:if test="productReturnMap['isAgent']">
					<tr>	
						<td align="center" class="fontBold padding10">
							<s:text name="printProductReturnTitleAgent" /></td>
						<td>&nbsp;</td>
					</tr>
				</s:if>
				<s:else>
					<td align="center" class="fontBold padding10">
						<s:text name="printProductReturnTitleFarmer" /></td>
					<td>&nbsp;</td>
				</s:else>				 
				 
				 
			</tr>
		</table>
		</td>
	</tr>	
	<tr class="rNoRow">
		<td><span class="fontBold firstTdSpan"><s:text name="printRecNo" /></span>&nbsp;<s:property
			value="productReturnMap['recNo']" /></td>
		<td><span class="fontBold secondTdSpan"><s:text name="print.date" /></span>&nbsp;<s:property
			value="productReturnMap['date']" />&nbsp;<span style="font-size:12px">(DD-MM-YYYY)</span></td>
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2">
			<s:if test="productReturnMap['isAgent']">
				<s:text name="print.agentDetails1" />
			</s:if>
			<s:else>
				<s:if test="currentTenantId!='pratibha'">
					<s:text name="print.comDetails" />
				</s:if>
				<s:else>
					<s:text name="print.comDetailsForPratibha" />
				</s:else>
			</s:else>			
		</td>
	</tr>
	<s:if test="productReturnMap['isAgent']">
	<tr>
		<td><span class="fontBold firstTdSpan"><s:text name="print.agentId" /></span><span>&nbsp;<s:property
			value="productReturnMap['agentId']" /></span></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.agentName" /></span>&nbsp;<s:property
			value="productReturnMap['agentName']" /></td>
	</tr>
	</s:if>
	<s:else>
			<td><span class="fontBold firstTdSpan"><s:text name="print.code" /></span><span>&nbsp;<s:property
			value="productReturnMap['warehouseCode']" /></span></td>
		 <td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.name" /></span>&nbsp;<s:property
			value="productReturnMap['warehouseName']" /></td>
	</s:else>
	<s:if test="!productReturnMap['isAgent']">
	<tr class="borderTop">
		<td class="fontBold" colspan="2"><s:text name="print.farmer" /></td>
	</tr>
	<tr>
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:text name="print.farmerId" /></span>&nbsp;<s:property
			value="productReturnMap['fId']" /></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text
			name="print.farmerName" /></span>&nbsp;<s:property
			value="productReturnMap['fName']" /></td>
	</tr>
	<tr class="borderBottom">
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:property value="%{getLocaleProperty('print.village')}" /></span>&nbsp;<s:property
			value="productReturnMap['village']" /></td>
		<td><%-- <span class="fontBold secondTdSpan"><s:property value="%{getLocaleProperty('print.samithi')}" /></span>&nbsp;<s:property
			value="productReturnMap['samithi']" /> --%></td> 
	</tr>
	</s:if>
</table>
<table border="1" width="100%" cellspacing="0" class="padding10">
	<tr>
		<td class="fontBold" align="center"><s:text
			name="print.productDetails" /></td>
	</tr>	
	<tr>
		<td>
		<table width="100%" border="0" class="subTable" cellpadding="0"
			cellspacing="0">
			<tr class="grayHeader">
			    <th width="10%"><s:text name="print.categoryName" /></th>				
				<th width="10%"><s:text name="print.productName" /></th>
				<s:if test="productReturnMap['enableBatchNo']==1">
				<th width="10%"><s:text name="batchNo" /></th>
					</s:if>
				<th width="10%"><s:text name="print.quantity" /></th>
				<s:if test="!productReturnMap['isAgent']">
				<th width="10%"><s:text name="print.sellingPrice" /></th>
				<th width="10%"><s:text name="print.amount" /></th>								
				</s:if>
			</tr>
			<s:if test='productReturnMap.productMapList.size()>0'>
				<s:iterator value="productReturnMap.productMapList" var="gListMap">
					<tr>
					    <td style="text-align: center;" ><s:property value="category" /></td>
						<td style="text-align: center;"><s:property value="product" /></td>	
						<s:if test="productReturnMap['enableBatchNo']==1">
						<td style="text-align: center;"><s:property value="batchNo" /></td>
						</s:if>
						<td style="text-align: center;"><s:property value="quantity" /></td>
						<s:if test="!productReturnMap['isAgent']">
						<td style="text-align: center;"><s:property value="sellingPrice" /></td>			
						<td style="text-align: center;"><s:property value="amount" /></td>		
						</s:if>
					</tr>
				</s:iterator>
		</s:if>
			<s:else>
				<tr>
					<td colspan="5" align="center"><s:text
						name="noRecordsFoundForPrint" /></td>
				</tr>
			</s:else>
		</table>
		</td>
	</tr>	
</table>	

<table border="1" width="100%" >
	<s:if test="!productReturnMap['isAgent']">
	<tr >
		<td class="fontBold" colspan="4" align="right" width="80%"><s:if
			test="productReturnMap['oBal']==0d">
			<s:text name="print.openingBalance" />
		</s:if> <s:elseif test="productReturnMap['oBal']>0d">
			<s:text name="print.openingOutstandingBalance" />
		</s:elseif>
		 <s:else>
		 <s:text name="print.openingCreditBalance" />
			
		</s:else></td>
		<td align="right" width="20%"><s:property
			value="productReturnMap['openingBal']" /></td>
	</tr>
	<%-- <tr>
		<td class="fontBold" colspan="4" align="right"><s:text
			name="print.txnAmt" /></td>
		<td align="right">(-)&nbsp;<s:property
			value="productReturnMap['distributionAmt']" /></td>
	</tr>
	
	<tr>
		<td class="fontBold" colspan="4" align="right"><s:text
			name="print.finalAmount" /></td>
		<td align="right">(-)&nbsp;<s:property
			value="productReturnMap['distributionFinAmt']" /></td>
	</tr>
	--%>
	<tr>
		<td class="fontBold" colspan="4" align="right"><s:text
			name="print.txnAmt" /></td>
		<td align="right">(-)&nbsp;<s:property
			value="productReturnMap['distributionAmt']" /></td>
	</tr>
	</s:if>
	</table>



<table border="0" width="100%" cellspacing="0" class="padding10">
	<%-- <s:if test="!productReturnMap['isAgent']">
	<tr>
		<td class="fontBold borderBottom" align="center"><s:text
			name="print.paymentDetail" /></td>
	</tr>	
	<tr>
	<td class="fontBold " align="right"><s:text name="print.paidAmount" /></td>
	<td class="alnRht"><s:property value="productReturnMap['distributionAmt']" /></td>
		<td>
		<table width="100%" border="0" class="subTable" cellpadding="0"
			cellspacing="0">
			<s:if test="productReturnMap['payMode']=='Cash'">
			<tr class="grayHeader">
					<th><s:text name="print.PaymentMode" /></th>
					<th><s:text name="print.payAmt" /></th>
				</tr>
				<tr>
					<td align="left"><s:property value="productReturnMap['payMode']" /></td>
					<td class="alnRht"><s:property value="productReturnMap['payAmt']" /></td>
			   </tr>
			</s:if>
			<s:if test="productReturnMap['payMode']=='Credit'">
			<tr class="grayHeader">
					<th><s:text name="print.PaymentMode" /></th>
					<th><s:text name="print.creditpaymentAmount" /></th>
			   </tr>
			   <tr>
					<td align="left"><s:property value="productReturnMap['payMode']" /></td>
					<td class="alnRht"><s:property value="productReturnMap['distributionAmt']" /></td>
			   </tr>
			</s:if>
		</table>
		</td>
	</tr>
	</s:if> --%>
			
	<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div>___________________________</div>
		</td>
		<!--<td valign="bottom" align="right" colspan="3">
		<div>___________________________</div>
		</td>
	-->
	</tr>
	<tr>
		<s:if test="productReturnMap['isAgent']">
		<td align="left" colspan="2"><s:text name="print.agentSignature1" /></td>
		</s:if>
		<s:else>
		<td align="left" colspan="2"><s:text name="print.farmerSignature" /></td>
		</s:else>
	
		<!--<td align="right" colspan="3">
			<s:if test="productReturnMap['isAgent']">
				<s:text name="print.agentSignature" />
			</s:if>
			<s:else>
				<s:text name="print.comSignature" />
			</s:else>
		</td>
	--></tr>	
</table>

<s:else>
			
	<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div>___________________________</div>
		</td>
		<!--<td valign="bottom" align="right" colspan="3">
		<div>___________________________</div>
		</td>
	-->
	</tr>
	<tr>
		<td align="left" colspan="2"><s:text name="print.farmerSignature" /></td>
		<!--<td align="right" colspan="3">
			<s:if test="productReturnMap['isAgent']">
				<s:text name="print.agentSignature" />
			</s:if>
			<s:else>
				<s:text name="print.comSignature" />
			</s:else>
		</td>
	--></tr>	
</s:else>




</div>
<div align="right"><s:text name="print.receiptDate" />&nbsp;<s:property
			value="productReturnMap['date']" /></div>
<div id="prntCtrl"><input type="button"
	value="<s:text name="printBtn"/>" onclick="printReceipt();" />
	<input type="button"
	value="<s:text name="closeBtn"/>" onclick="closeWindow();" /></div>
</body>
</html>