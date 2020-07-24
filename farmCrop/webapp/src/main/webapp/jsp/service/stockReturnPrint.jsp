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

.subTable tr:last-child>td {
 	<%--font-weight: bold; --%>
}

.subTable td {
	padding-right: 5px;
}

.subTable td,th {
	width: 10% !important;
}

#prntCtrl {
	text-align: center;
	padding: 10px;
}

.logoImg {
	<%--border: 1px solid black; --%>
	width: 50%;
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
	<%--padding-top:10px;--%>
}

.grayHeader > th, .grayHeader > td{
	background-color:#E8E8E8 ;
}
.finalBalCls{
	border-top:1px solid black;
	border-bottom:1px solid black;
	font-weight:bold;
}
.imgWidth{
	width : 10Px;
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
				<td>
			<div class="center">
	            <img src="login_populateLogo.action?logoType=app_logo" />
	        </div>
				</td>
				<td align="left" class="fontBold padding10">
				<s:text name="printTitle" /></td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr class="clear"></tr>
	
	<tr class="rNoRow">
		<td><span class="fontBold"><s:text name="printRecNo" /></span>&nbsp;<s:property
			value="stockReturnMap['recNo']" /></td>
		<td align="left"><span class="fontBold secondTdSpan"><s:text name="print.date" /></span>&nbsp;<s:property
			value="stockReturnMap['date']" />&nbsp;<span style="font-size:12px">(DD-MM-YYYY)</span></td>
	</tr>
	
	<tr class="borderTop">
		<td class="fontBold" colspan="2"><s:text name="print.comDetails" /></td>
	</tr>
	<tr class="clear"></tr>
	<tr>
		<td><span class="fontBold firstTdSpan"><s:text name="print.code" /></span><span>&nbsp;<s:property
			value="stockReturnMap['warehouseCode']" /></span></td>
		 <td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.name" /></span>&nbsp;<s:property
			value="stockReturnMap['warehouseName']" /></td>
	</tr>	
	
	<tr class="borderTop">
		<td class="fontBold" colspan="2"><s:text name="print.vendor" /></td>
	</tr>
	<tr>
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:text name="print.vendorId" /></span>&nbsp;<s:property
			value="stockReturnMap['vId']" /></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text
			name="print.vendorName" /></span>&nbsp;<s:property
			value="stockReturnMap['vName']" /></td>
	</tr>
	
	<tr>
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:text name="print.orderNo" /></span>&nbsp;<s:property
			value="stockReturnMap['orderNo']" /></td>
		
	</tr>
</table>
<table border="0" width="100%" cellspacing="0" class="padding10">
	<tr>
		<td class="fontBold"><s:text
			name="print.productDetails" /></td>
	</tr>	
	<tr>
		<td>
		<table width="100%" border="0" class="subTable" cellpadding="0"
			cellspacing="0">
			<tr class="grayHeader">
				<th width="10%"><s:text name="print.categoryName" /></th>				
				<th width="10%"><s:text name="print.productName" /></th>
				<th width="10%"><s:text name="print.costPrice" /></th>
				<th width="10%"><s:text name="print.dam" /></th>
				<th width="10%"><s:text name="print.amt" /></th>
			</tr>
			<s:if test='stockReturnMap.productMapList.size()>0'>
				<s:iterator value="stockReturnMap.productMapList" var="gListMap">
					<tr>
						<td style="word-break: break-all;"><s:property value="category" /></td>
						<td style="word-break: break-all;"><s:property value="product" /></td>						
						<td class="alnRht"><s:property value="costPrice" /></td>
						<td class="alnRht"><s:property value="badQty" /></td>
						<td class="alnRht"><s:property value="totalAmt" /></td>
					</tr>
				</s:iterator>
				<!--<s:iterator status="stat" value="(5-warehouseMap.productMapList.size()).{ #this }" >
				   <tr>
						<td style="word-break: break-all;">&nbsp;</td>						
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
					</tr>
				</s:iterator>
				<tr class="grayHeader">
				    <td class="fontBold"><s:text name="total" /></td>	
					<td class="fontBold"><s:text name="total" /></td>					
					<td class="alnRht"><s:property
						value="warehouseMap['totalInfo'].totalQuantity" /></td>
					<td class="alnRht"><s:property
						value="warehouseMap['totalInfo'].totalPricePerUnit" /></td>
					<td class="alnRht"><s:property
						value="warehouseMap['totalInfo'].totalAmount" /></td>
				</tr>
			--></s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center"><s:text
						name="noRecordsFoundForPrint" /></td>
				</tr>
			</s:else>
		</table>
		</td>
	</tr>	
</table>
<table border="0" width="100%" >
	<tr>
		<td class="fontBold" colspan="7" align="right"><s:text
			name="print.finalAmount" /></td>
		<td align="right" class="finalBalCls" width="10%"><s:property
			value="stockReturnMap['finalAmount']" /></td>
	</tr>
	<!--<tr>
		<td class="fontBold" colspan="4" align="right"><s:if
			test="distributionMap['fBal']==0d">
			<s:text name="print.finalBalance" />
		</s:if> <s:elseif test="distributionMap['fBal']>0d">
			<s:text name="print.finalCreditBalance" />
		</s:elseif> <s:else>
			<s:text name="print.finalOutstandingBalance" />
		</s:else></td>
		<td align="right" class="finalBalCls"><s:property value="distributionMap['finalBal']" /></td>
	</tr>
	--><!--<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div>___________________________</div>
		</td>
		<td valign="bottom" align="right" colspan="3">
		<div>___________________________</div>
		</td>
	
	</tr>
	<tr>
		<td align="left" colspan="2"><s:text name="print.farmerSignature" /></td>
		<td align="right" colspan="3">
			<s:if test="distributionMap['isAgent']">
				<s:text name="print.agentSignature" />
			</s:if>
			<s:else>
				<s:text name="print.comSignature" />
			</s:else>
		</td>
	</tr>
--></table>
<table border="0" width="100%" cellspacing="0" class="padding10">
	<tr>
		<td class="fontBold"><s:text
			name="print.paymentDetail" /></td>
	</tr>	
	<tr>
		<td>
		<table width="100%" border="0" class="subTable" cellpadding="0"
			cellspacing="0">
			<s:if test="stockReturnMap['payMode']=='Cash'">
				<tr class="grayHeader">
					<th><s:text name="print.PaymentMode" /></th>
					<th><s:text name="print.payAmt" /></th>
					<th><s:text name="print.remarks" /></th>				
				</tr>
				<tr>
					<td align="left"><s:property value="stockReturnMap['payMode']" /></td>
					<td class="alnRht"><s:property value="stockReturnMap['payAmt']" /></td>
					<td align="left"><s:property value="stockReturnMap['remarks']" /></td>
			   </tr>
			</s:if>
			<s:if test="stockReturnMap['payMode']=='Credit'">
				<tr class="grayHeader">
					<th><s:text name="print.PaymentMode" /></th>
					<th><s:text name="print.payFinAmt" /></th>
					<th><s:text name="print.remarks" /></th>				
			   </tr>
			   <tr>
					<td align="left"><s:property value="stockReturnMap['payMode']" /></td>
					<td class="alnRht"><s:property value="stockReturnMap['credAmt']" /></td>
					<td align="left"><s:property value="stockReturnMap['remarks']" /></td>
			   </tr>
			</s:if>
		</table>
		</td>
	</tr>
	
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
	</tr>	
</table>
</div>
<div align="right"><s:text name="print.receiptDate" />&nbsp;<s:property
	value="stockReturnMap['date']"/></div>
<div id="prntCtrl"><input type="button"
	value="<s:text name="printBtn"/>" onclick="printReceipt();" />
	<input type="button"
	value="<s:text name="closeBtn"/>" onclick="closeWindow();" /></div>
</body>
</html>