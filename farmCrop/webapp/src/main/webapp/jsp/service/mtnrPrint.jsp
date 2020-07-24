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
    width: 104px;
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
				<td width="10px">
			<div class="center">
	            <img src="login_populateLogo.action?logoType=app_logo" />
	        </div>
				</td>
				<td align="center" class="fontBold padding10"><s:text
					name="printTitlemtnr" /></td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr class="rNoRow">
		<td><span class="fontBold firstTdSpan"><s:text name="printRecNo" /></span>&nbsp;<s:property
			value="mtnrPrintMap['recNo']" /></td>
		<td><span class="fontBold secondTdSpan"><s:text name="print.date" /></span>&nbsp;<s:property
			value="mtnrPrintMap['date']" />&nbsp;<span style="font-size:12px">(<s:property value="%{getGeneralDateFormat().toUpperCase()}" />)</span></td>
	</tr>
	<tr>
		<td><span class="fontBold firstTdSpan"><s:text name="print.truckId" /></span><span>&nbsp;<s:property
			value="mtnrPrintMap['tructId']" /></span></td>
		<td><span class="fontBold secondTdSpan"><s:text name="print.driverName" /></span><span>&nbsp;<s:property
			value="mtnrPrintMap['driverName']" /></span></td>
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2">
			<s:if test="mtnrPrintMap['isAgent']">
				<s:text name="print.agentDetails" />
			</s:if>
			<s:else>
				<s:text name="print.comDetails" />
			</s:else>			
		</td>
	</tr>
	<tr class="borderBottom">
		<td><span class="fontBold firstTdSpan"><s:text name="print.agentId" /></span><span>&nbsp;<s:property
			value="mtnrPrintMap['agentId']" /></span></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.agentName" /></span>&nbsp;<s:property
			value="mtnrPrintMap['agentName']" /></td>
	</tr>		
</table>
<table border="0" width="100%" cellspacing="0" class="padding10">
	<tr>
		<td class="fontBold" align="center"><s:text
			name="print.productDetails" /></td>
	</tr>
	<%-- <tr>
		<td><span class="fontBold"><s:text
			name="print.productName" /></span>&nbsp;<s:property
			value="mtnrPrintMap['product']" /></td>
	</tr> --%>
	<tr>
		<td>
		<table width="100%" border="1" class="subTable" cellpadding="0"
			cellspacing="0">
			<tr class="grayHeader">
				<th><s:text name="product" /></th>
				<th><s:text name="variety" /></th>
				<th><s:property value="%{getLocaleProperty('print.grade')}" /></th>
				<s:if test="currentTenantId!='pratibha'">
				<th><s:text name="noofBags" /></th></s:if>
				<th><s:text name="grossWeight" /></th>				
			</tr>
			<s:if test='mtnrPrintMap.productMapList.size()>0'>
				<s:iterator value="mtnrPrintMap.productMapList" var="gListMap">
					<tr>
						<td style="word-break: break-all;"><s:property value="product" /></td>
						<td style="word-break: break-all;"><s:property value="variety" /></td>
						<td style="word-break: break-all;"><s:property value="grade" /></td>
						<s:if test="currentTenantId!='pratibha'">
						<td class="alnRht"><s:property value="noOfBags" /></td>
						</s:if>
						<td class="alnRht"><s:property value="netWeight" /></td>						
					</tr>
				</s:iterator>
				<s:iterator status="stat" value="(10-mtnrPrintMap.productMapList.size()).{ #this }" >
				   <tr>
						<td style="word-break: break-all;">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>						
					</tr>
				</s:iterator>
				<tr class="grayHeader">
					<td class="alnRht">&nbsp;</td>
					<td class="alnRht">&nbsp;</td>
					<td class="fontBold"><s:text name="print.total" /></td>
					<s:if test="currentTenantId!='pratibha'">
					<td class="alnRht"><s:property
						value="mtnrPrintMap['totalInfo'].noOfBags" /></td></s:if>
					<td class="alnRht"><s:property
						value="mtnrPrintMap['totalInfo'].netWeight" /></td>					
				</tr>
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
	<%--<tr class="borderBottom">
		<td><span class="fontBold"><s:text
			name="print.paymentAmount" /></span>&nbsp;<s:property
			value="procurementMap['paymentAmout']" /></td>
	</tr>
	 --%>
</table>
<s:if test="currentTenantId!='movcd' && currentTenantId!='pratibha' && currentTenantId!='agro'">
<table border="0" width="100%">	
	<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div style="display:none;">_____________________________</div>
		</td>
		<td valign="bottom" align="right" colspan="3">
		<div>_____________________________</div>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2"  style="display:none;"><s:text name="print.farmerSignature" /></td>
		<td align="right" colspan="3">
			<s:if test="productMapList['isAgent']">
				<s:text name="print.agentSignature" />
			</s:if>
			<s:else>
				<s:text name="print.comSignature" />
			</s:else>
		</td>
	</tr>
</table>
</s:if>
</div>
<%-- <div align="right"><s:text name="print.receiptDate" />&nbsp;<s:date
	name="%{new java.util.Date()}" format="%{getGeneralDateFormat().concat(' HH:mm:ss')}" /></div> --%>
<div id="prntCtrl"><input type="button"
	value="<s:text name="printBtn"/>" onclick="printReceipt();" />
	<input type="button"
	value="<s:text name="closeBtn"/>" onclick="closeWindow();" /></div>
</body>
</html>