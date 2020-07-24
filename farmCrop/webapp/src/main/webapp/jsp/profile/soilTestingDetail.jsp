<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
	
</script>
<td style="width: 17%;"><s:text name="farm.soilTesting" /></td>
<td style="width: 17%;"><s:iterator value="soilTestMap"
		status="rowstatus">
		<s:if test="#rowstatus.count == farm.soilTesting">
			<s:property value="value" />
		</s:if>
	</s:iterator></td>
	<s:if test="farm.soilTesting==1">
<tr>
	<td><s:text name="farm.officerSuggestions" /></td>
	<td><s:text name="farm.actionTaken" /></td>
</tr>
<s:iterator value="farmerSoilTestingList" var="values">
	<tr>
		<td><s:property value="officersSuggestion"/></td>
		<td><s:property value="takenAction"/></td>
	</tr>
</s:iterator>
</s:if>