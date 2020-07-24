<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="assets/client/demo/css/agro-theme.css" />
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FieldStaff Distribution Report</title>
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
<SCRIPT type="text/javascript">
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
	</SCRIPT>
</head>
<body>
<div style="border: 1px solid black; padding: 20px;">
<table border="0" width="100%" class="fstTbl" cellspacing="0">
	<tr>
		<td colspan="2" align="center">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td >
			        <div class="center">
	            <img src="login_populateLogo.action?logoType=app_logo" />
	        </div>
				
				</td>
				<td align="center" class="fontBold padding10">
				<s:text name="printTitleAgent" /></td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr class="rNoRow">
		<td><span class="fontBold firstTdSpan"><s:text name="printRecNo" /></span>&nbsp;<s:property
			value="distributionMap['recNo']" /></td>
		<td><span class="fontBold secondTdSpan"><s:text name="print.date" /></span>&nbsp;<s:property
			value="distributionMap['date']" />&nbsp;<span style="font-size:12px">(DD-MM-YYYY)</span></td>
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2">
			<s:if test="distributionMap['isAgent']">
				<s:text name="print.agentDetails" />
			</s:if>
			<s:else>
				<s:text name="print.comDetails" />
			</s:else>			
		</td>
	</tr>
	<tr>
		<td><span class="fontBold firstTdSpan"><s:text name="print.agentId" /></span><span>&nbsp;<s:property
			value="distributionMap['agentId']" /></span></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.agentName" /></span>&nbsp;<s:property
			value="distributionMap['agentName']" /></td>
	</tr>
	<tr class="borderTop">
		<td class="fontBold" colspan="2"><s:text name="print.agentDetails" /></td>
	</tr>
	<tr>
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:text name="print.farmerId" /></span>&nbsp;<s:property
			value="distributionMap['fId']" /></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text
			name="print.farmerName" /></span>&nbsp;<s:property
			value="distributionMap['fName']" /></td>
	</tr>
	<tr class="borderBottom">
		<td style="word-break: break-all;"><span class="fontBold firstTdSpan"><s:property value="%{getLocaleProperty('print.village')}" /></span>&nbsp;<s:property
			value="distributionMap['village']" /></td>
		<td style="word-break: break-all;"><span class="fontBold secondTdSpan"><s:text name="print.samithi" /></span>&nbsp;<s:property
			value="distributionMap['samithi']" /></td>
	</tr>
</table>
<table border="0" width="100%" cellspacing="0" class="padding10">
	<tr>
		<td class="fontBold" align="center"><s:text
			name="print.productDetails" /></td>
	</tr>	
	<tr>
		<td>
		<table width="100%" border="0" class="subTable" cellpadding="0"
			cellspacing="0">
			<tr class="grayHeader">
				<th><s:text name="print.productName" /></th>				
				<th><s:text name="print.quantity" /></th>
				<th><s:text name="print.pricePerUnit" /></th>
				<th><s:text name="print.amount" /></th>
			</tr>
			<s:if test='distributionMap.productMapList.size()>0'>
				<s:iterator value="distributionMap.productMapList" var="gListMap">
					<tr>
						<td style="word-break: break-all;"><s:property value="product" /></td>						
						<td class="alnRht"><s:property value="quantity" /></td>
						<td class="alnRht"><s:property value="pricePerUnit" /></td>
						<td class="alnRht"><s:property value="amount" /></td>
					</tr>
				</s:iterator>
				<s:iterator status="stat" value="(5-distributionMap.productMapList.size()).{ #this }" >
				   <tr>
						<td style="word-break: break-all;">&nbsp;</td>						
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
						<td class="alnRht">&nbsp;</td>
					</tr>
				</s:iterator>
				<tr class="grayHeader">
					<td class="fontBold"><s:text name="total" /></td>					
					<td class="alnRht"><s:property
						value="distributionMap['totalInfo'].totalQuantity" /></td>
					<td class="alnRht"><s:property
						value="distributionMap['totalInfo'].totalPricePerUnit" /></td>
					<td class="alnRht"><s:property
						value="distributionMap['totalInfo'].totalAmount" /></td>
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
</table>
<table border="0" width="100%">
	<tr>
		<td class="fontBold" colspan="4" align="right" width="80%"> 
			 </td>
		<td align="right" width="20%"></td>
	</tr>
	<tr>
		<td class="fontBold" colspan="4" align="right"> </td>
		<td align="right"> </td>
	</tr>
	<tr>
		<td class="fontBold" colspan="4" align="right"> </td>
		<td align="right"> </td>
	</tr>
	<tr>
		<td class="fontBold" colspan="4" align="right"> </td>
		<td align="right" class="finalBalCls"> </td>
	</tr>
	<tr class="signatureLine">
		<td valign="bottom" align="left" colspan="2">
		<div>___________________________</div>
		</td>
		<td valign="bottom" align="right" colspan="3">
		<div>___________________________</div>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2"><s:text name="print.agentSignature" /></td>
		<td align="right" colspan="3">
			<s:if test="distributionMap['isAgent']">
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