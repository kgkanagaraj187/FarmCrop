<%@ taglib prefix="s" uri="/struts-tags"%>

<tr>
	<th><s:text name="farm.farmerPlants" /></th>
	<th><s:text name="farm.plantedPlants" /></th>
	<th><s:text name="farm.livePlants" /></th>
</tr>
<s:iterator value="farm.farmerPlants">
	<tr>

		<td align="right"><s:property value="plants" /></td>
		<td align="right"><s:property value="noOfPlants" /></td>
		<td align="right"><s:property value="noOfLive" /></td>

	</tr>
</s:iterator>
