<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<script>



function selectedInventryItem(data){
	 if(data==99){
		jQuery(".otherInventryItem").show();
		jQuery("#otherValue").val();
	}else{
		jQuery(".otherInventryItem").hide();
		jQuery("#otherValue").val("");
	}
	
}

function onCancel(){	
	document.cancelform.submit();
}

$(document).ready(function(){
	
	selectedInventryItem(jQuery("#inventoryItem").val());
});
</script>
<div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" />

<div style="float:right; color:#000000">
	<table style="border:0px solid red;">
	<tr>
		<td style="padding-right:5px;text-align:right;">
			<b><s:text name="Farmer ID : " /></b>
			<s:if test='"update".equalsIgnoreCase(command)'>
				<s:property value="farmInventory.farm.farmer.farmerId" />	
				<s:hidden name="farmInventory.farm.farmerId" />
			</s:if>
			<s:else>
				<s:property value="farmerId" />
			</s:else>
		</td>
		<td style="padding-right:5px;text-align:right;">
			<b><s:text name="Farmer.name " /></b>
			<s:if test='"update".equalsIgnoreCase(command)'>
				<s:property value="farmInventory.farm.farmer.name"/>
			</s:if> 
			<s:else>
				<s:property value="farmerName" />				
			</s:else>
		</td>
		<td style="padding-right:5px;text-align:right;">
			<b><s:text name="Farm Name : " /></b>
			<s:if test='"update".equalsIgnoreCase(command)'>
				       <s:property value="farmInventory.farm.farmName" />
			           <s:hidden name="farmInventory.farm.farmCode" />
			       </s:if> 
			        <s:else>
						<s:property value="farmName" />
				  </s:else>
		</td>
	</tr>
	</table>
</div>


</div>
<s:form name="form" cssClass="fillform" action="farmInventory_%{command}">
<s:hidden name="farmId" />
<s:hidden name="tabIndex" />
<s:hidden key="currentPage"/>
<s:hidden key="id" />
<s:hidden key="farm.id" />
<s:hidden id="farmerId" name="farmerId" />
<s:hidden id="farmerName" name="farmerName" />
<s:hidden id="farmName" name="farmName" />
<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="farmInventory.id" />
</s:if>
<s:hidden key="command" />

<table width="95%" cellspacing="0">
	<tr><th colspan="2"><s:text name="info.farmInventory" /></th></tr>
	
	<tr class="odd">
		<td><s:text name="farmInventory.inventoryItem" /></td>
		<td>
		
		<s:select name="selectedFarmInventoryItem" list="inventoryItemsList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="inventoryItem" onchange="selectedInventryItem(this.value);" /><sup style="color: red;">*</sup>
		</td>
	</tr>
	
	<tr class="otherInventryItem">
		<td width="35%"><s:text name="farmInventory.otherInventoryItem" /></td>
		<td width="65%"><s:textfield name="farmInventory.otherInventoryItem" theme="simple"id="otherValue"/><sup style="color: red;">*</sup></td>
	</tr>
		
	<tr class="odd">
		<td width="35%"><s:text name="farmInventory.itemCount" /></td>
		<td width="65%"><s:textfield name="farmInventory.itemCount" theme="simple" maxlength="4"/><sup style="color: red;">*</sup></td>
	</tr>
	
</table>
<br/>

	<div class="yui-skin-sam">
	<s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
	</s:if>
	<s:else>
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
	</s:else>
	<span id="cancel" class=""><span class="first-child">
		<button type="button" class="cancel-btn"><b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
           </font></b></button></span></span>
	</div>
</s:form>

<s:form name="cancelform" action="farm_detail.action%{tabIndex}">
	   <s:hidden name="farmId" />
	   <s:hidden name="id" value="%{farmId}"/>
	   <s:hidden name="tabIndex" />
	   <s:hidden key="currentPage"/>
</s:form>
</body>