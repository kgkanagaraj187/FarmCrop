<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<script>

$(document).ready(function(){
	
	selectedAnimalList(jQuery("#farmAnimal").val());
	selectedFooderList(jQuery("#fooder").val());
	selectedAnimalHousingList(jQuery("#animalHousing").val());
	selectedRevenuList(jQuery("#revenue").val());
	


function selectedAnimalList(data){
	 if(data==99){
		jQuery(".otherFarmAnimal").show();
		jQuery("#otherFarmAnimalValue").val();
	}else{
		jQuery(".otherFarmAnimal").hide();
		jQuery("#otherFarmAnimalValue").val("");
	}
	
}

function selectedFooderList(data){
	 if(data==99){
		jQuery(".otherFooter").show();
		jQuery("#otherFodderValue").val();
	}else{
		jQuery(".otherFooter").hide();
		jQuery("#otherFodderValue").val("");
	}
	
}

function selectedAnimalHousingList(data){
	 if(data==99){
		jQuery(".otherAnimalHousing").show();
		jQuery("#otherAnimalHousingValue").val();
	}else{
		jQuery(".otherAnimalHousing").hide();
		jQuery("#otherAnimalHousingValue").val("");
	}
	
}

function selectedRevenuList(data){
	 if(data==99){
		jQuery(".otherRevenu").show();
		jQuery("#otherRevenueValue").val();
	}else{
		jQuery(".otherRevenu").hide();
		jQuery("#otherRevenueValue").val("");
	}
	
}

$(document).ready(function () {
    //called when key is pressed in textbox
  	$("#noOfDaysToGrow").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#errMsg").html("Digits Only").show().fadeOut("slow");
               return false;
     	}
   	});
});


});

function onCancel(){	
	document.cancelform.submit();
}
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
				<s:property value="animalHusbandary.farm.farmer.farmerId" />		
				<s:hidden name="animalHusbandary.farm.farmerId" />
			</s:if>
			<s:else>
				<s:property value="farmerId" />
			</s:else>
		</td>
		<td style="padding-right:5px;text-align:right;">
			<b><s:text name="Farmer Name : " /></b>
			<s:if test='"update".equalsIgnoreCase(command)'>
				<s:property value="animalHusbandary.farm.farmer.name" />
			</s:if> 
			<s:else>
				<s:property value="farmerName" />				
			</s:else>
		</td>
		<td style="padding-right:5px;text-align:right;">
			<b><s:text name="Farm Name : " /></b>
			<s:if test='"update".equalsIgnoreCase(command)'>
				       <s:property value="animalHusbandary.farm.farmName" />
			           <s:hidden name="animalHusbandary.farm.farmCode" />
			       </s:if> 
			        <s:else>
						<s:property value="farmName" />
				  </s:else>
		</td>
	</tr>
	</table>
</div>


</div>
<s:form name="form" cssClass="fillform" action="animalHusbandary_%{command}">
<s:hidden name="farmId" />
<s:hidden name="tabIndex" />
<s:hidden key="currentPage"/>
<s:hidden key="id" />
<s:hidden key="farm.id" />
<s:hidden id="farmerId" name="farmerId" />
<s:hidden id="farmerName" name="farmerName" />
<s:hidden id="farmName" name="farmName" />
<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="animalHusbandary.id" />
</s:if>
<s:hidden key="command" />

<table width="95%" cellspacing="0">
	<tr><th colspan="2"><s:text name="info.animalHusbandary" /></th></tr>
	
	<tr class="odd">
		<td><s:text name="animalHusbandary.farmAnimal" /><sup style="color: red;">*</sup></td>
		<td>
		
		<s:select name="selectedFarmAnimal" list="farmAnimalList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="farmAnimal" onchange="selectedAnimalList(this.value);" /></td>		
			
		</td>
	</tr>
	
	<tr class="otherFarmAnimal">
		<td width="35%"><s:text name="animalHusbandary.otherFarmAnimal" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.otherFarmAnimal" theme="simple" id="otherFarmAnimalValue"/>
	</tr>
		
	<tr class="odd">
		<td width="35%"><s:text name="animalHusbandary.animalCount" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.animalCount" theme="simple"  maxlength="5"  id="noOfDaysToGrow"/>
	</tr>
	
	<!--<tr class="odd">
		<td><s:text name="animalHusbandary.feedingUsed" /></td>
		<td><s:select name="selectedFeedingUsed" list="feedingUsedList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="feedingUsed" /><sup
			style="color: red;">*</sup></td>
	</tr>
	
	-->
	<tr class="odd">
		<td><s:text name="animalHusbandary.fodder" /></td>
		<td><s:select name="animalHusbandary.fodder" list="fooderList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="fooder" onchange="selectedFooderList(this.value);"/>
	</tr>
	
	<tr class="otherFooter">
		<td width="35%"><s:text name="animalHusbandary.otherFodder" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.otherFodder" theme="simple" id="otherFodderValue"/>
	</tr>
	
	<tr class="odd">
		<td><s:text name="animalHusbandary.animalHousing" /></td>
		<td><s:select name="selectedAnimalHousing" list="animalHousingList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="animalHousing" onchange="selectedAnimalHousingList(this.value);"/>
	</tr>
	
	<tr class="otherAnimalHousing">
		<td width="35%"><s:text name="animalHusbandary.otherAnimalHousing" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.otherAnimalHousing" theme="simple" id="otherAnimalHousingValue" />
	</tr>
	
	<tr class="odd">
		<td><s:text name="animalHusbandary.revenue" /></td>
		<td><s:select name="animalHusbandary.revenue" list="revenueList"
			headerKey="-1" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
			theme="simple" id="revenue" onchange="selectedRevenuList(this.value);" />
	</tr>
	
	<tr class="otherRevenu">
		<td width="35%"><s:text name="animalHusbandary.otherRevenue" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.otherRevenue" theme="simple" id="otherRevenueValue"/>
	</tr>
	
	<!--<tr class="odd">
		<td width="35%"><s:text name="animalHusbandary.production" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.production" theme="simple" maxlength="255"/></td>
	</tr>
	
	<tr class="odd">
		<td width="35%"><s:text name="animalHusbandary.fymEstimatedOutput" /></td>
		<td width="65%"><s:textfield name="animalHusbandary.fymEstimatedOutput" theme="simple" maxlength="255"/></td>
	</tr>
	
--></table>
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
	<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn">
           <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
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