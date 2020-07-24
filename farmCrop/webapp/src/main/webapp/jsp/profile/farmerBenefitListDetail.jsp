<%@ taglib prefix="s" uri="/struts-tags"%>

<tr>
	<th><s:text name="benefitDetails" /></th>
	<th><s:text name="kgs" /></th>
	<th><s:text name="scheme" /></th>
	<th><s:text name="datee" /></th>
	<th><s:text name="amt" /></th>
	<th><s:text name="contri" /></th>
</tr>
<s:iterator value="farm.farmerScheme" status="incr">
	<tr>
		<td align="right"><s:text
				name="benefitary.split(',')[%{#incr.index}]" /></td>
		<td align="right"><s:property value="noOfKgs" /></td>
		<td align="right"><s:text
				name="schemeTxt.split(',')[%{#incr.index}]" /></td>
		<td align="right"><s:date name="receivedDate" format="dd/MM/yyyy" /></td>
		<td align="right"><s:property value="receivedAmt" /></td>
		<td align="right"><s:property value="contributionAmt" /></td>

	</tr>
</s:iterator>
