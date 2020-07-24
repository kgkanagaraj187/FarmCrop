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
	font-weight: bold;
}

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
				<td>
			<div class="center">
	            <img src="login_populateLogo.action?logoType=app_logo" />
	        </div>
				</td>
				<td align="center" class="fontBold padding10"><s:text
					name="printTitle" /></td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr class="rNoRow">
		<td><span class="fontBold firstTdSpan"><s:text name="printRecNo" /></span>&nbsp;<s:property
			value="paymentMap['recNo']" /></td>
		<td><span class="fontBold secondTdSpan"><s:text name="print.date" /></span>&nbsp;<s:property
			value="paymentMap['date']" />&nbsp;<span style="font-size:12px">(DD-MM-YYYY)</span></td>
	</tr>
	<tr class="rNoRow">
		<td colspan="2"><span class="fontBold firstTdSpan"><s:text name="print.paymentMode" /></span>&nbsp;<s:property
			value="paymentMap['pMode']" /></td>		
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2">
			<s:if test="paymentMap['isAgent']">
				<s:text name="print.agentDetails" />
			</s:if>
			<s:else>
				<s:text name="print.comDetails" />
			</s:else>			
		</td>
	</tr>
	<tr>
		<td><span class="fontBold firstTdSpan"><s:text name="print.agentId" /></span><span>&nbsp;<s:property
			value="paymentMap['agentId']" /></span></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.agentName" /></span>&nbsp;<s:property
			value="paymentMap['agentName']" /></td>
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2"><s:text name="print.farmer" /></td>
	</tr>
	<tr>
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:text name="print.farmerId" /></span>&nbsp;<s:property
			value="paymentMap['fId']" /></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text
			name="print.farmerName" /></span>&nbsp;<s:property
			value="paymentMap['fName']" /></td>
	</tr>
	<tr class="borderBottom">
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:property value="%{getLocaleProperty('print.village')}" /></span>&nbsp;<s:property
			value="paymentMap['village']" /></td>		
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.samithi" /></span>&nbsp;<s:property
			value="paymentMap['samithi']" /></td>		
	</tr>
</table>
<table border="0" width="100%">
	<tr>
		<td class="fontBold" colspan="4" align="right" width="80%"><s:if
			test="paymentMap['oBal']==0d">
			<s:text name="print.openingBalance" />
		</s:if> <s:elseif test="paymentMap['oBal']>0d">
			<s:text name="print.openingCreditBalance" />
		</s:elseif> <s:else>
			<s:text name="print.openingOutstandingBalance" />
		</s:else></td>
		<td align="right" width="20%"><s:property
			value="paymentMap['openingBal']" /></td>
	</tr>	
	<tr>
		<td class="fontBold" colspan="4" align="right"><s:text
			name="print.paymentAmount" /></td>
		<td align="right">(-)&nbsp;<s:property
			value="paymentMap['paymentAmout']" /></td>
	</tr>
	<tr>
		<td class="fontBold" colspan="4" align="right"><s:if
			test="paymentMap['fBal']==0d">
			<s:text name="print.finalBalance" />
		</s:if> <s:elseif test="paymentMap['fBal']>0d">
			<s:text name="print.finalCreditBalance" />
		</s:elseif> <s:else>
			<s:text name="print.finalOutstandingBalance" />
		</s:else></td>
		<td align="right" class="finalBalCls"><s:property value="paymentMap['finalBal']" /></td>
	</tr>
	<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div>_____________________________</div>
		</td>
		<td valign="bottom" align="right" colspan="3">
		<div>_____________________________</div>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2"><s:text name="print.farmerSignature" /></td>
		<td align="right" colspan="3">
			<s:if test="paymentMap['isAgent']">
				<s:text name="print.agentSignature" />
			</s:if>
			<s:else>
				<s:text name="print.comSignature" />
			</s:else>
		</td>
	</tr>
</table>
</div>
<div align="right"><s:text name="print.receiptDate" />&nbsp;<s:date
	name="%{new java.util.Date()}" format="dd-MM-yyyy HH:mm:ss" /></div>
<div id="prntCtrl"><input type="button"
	value="<s:text name="printBtn"/>" onclick="printReceipt();" />
	<input type="button"
	value="<s:text name="closeBtn"/>" onclick="closeWindow();" /></div>
</body>
</html>