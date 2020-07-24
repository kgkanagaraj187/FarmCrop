<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test='agents.size()>0'>
	<table align="center">
	<tr>
	<td colspan="2"><s:text name="chooseAgentsToAssignPendingMTNTProducts" /></td>
	</tr>
		<tr>
			<td><s:text name="selectAgent" /></td>
			<td>
				<s:select id="agentsToAssignMTNT" headerKey="-1"
				headerValue="%{getText('txt.select')}" list="agents" listKey="profileId"
				listValue="profileId" theme="simple" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="button" class="popBtn" value="<s:text name="submit"/>"
				onclick="moveStock()" /> <input type="button"  class="popBtn"
				value="<s:text name="cancel"/>" onclick="disablePendingMTNTAlert()" />
			</td>
		</tr>
	</table>
</s:if>
<s:else>
	<table align="center">
		<tr>
			<td><s:text name="singleAgentMsg" /></td>			
		</tr>
		<tr>
			<td>
				<input type="button"  class="popBtn" value="<s:text name="submit"/>" onclick="discardStock()"/> 
				<input type="button"  class="popBtn" value="<s:text name="cancel"/>" onclick="disablePendingMTNTAlert()" />
			</td>
		</tr>
	</table>
</s:else>