<%@ taglib prefix="s" uri="/struts-tags"%>
<script>	
	jQuery(document).ready(function(){
		$('#soilDetailInfo').hide();
		$("#tblSoilData").hide();
		if($('#soilTesting1').is(':checked')) { suggestionTableShow(jQuery("#soilTesting1").val()); }
		else{ suggestionTableShow(jQuery("#soilTesting2").val()); }
		
	});

	function suggestionTableShow(flag){
		if(flag==1){
			$('#suggestionDiv').removeClass("hide");
			$('#suggestionValueDiv').removeClass("hide");
			$('#tblSoilData').show();
		}
		else{
			$('#suggestionDiv').addClass("hide");
			$('#suggestionValueDiv').addClass("hide");
			$('#tblSoilData').hide();
		}
	}
	
	function addDetails(){
		
		var sugesstionFromOfficers=jQuery("#officerSuggestions").val();
		var actionTaken=jQuery("#actionTaken").val();
		
		var tableFoot=document.getElementById("tblSoilData");
		var rows = tableFoot.getElementsByTagName("tr");
		
		
		if(sugesstionFromOfficers!=''||actionTaken!=''){
			$('#soilDetailInfo').show();
			$("#tblSoilData").append(
					"<tr class='tableTr1' id='soiltestRow"+((rows.length)+1)+"'>"+
						"<td align='center'><div id='suggestion"+((rows.length)+1)+"'>"+sugesstionFromOfficers+"</div></td>"+
						"<td align='center'><div id='actionTaken"+((rows.length)+1)+"'>"+actionTaken+"</div></td>"+
					'<td style="text-align:center;">'+
					
					'<button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="editRow('+((rows.length)+1)+')"><i class=" fa fa-pencil-square-o"></i></button>'+
					
					' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
					+"</tr><input type='hidden' value='0' id='soilTestId"+((rows.length)+1)+"'>"
			);
		}
		resetSoilData();
	}
	
	function editedRow(value){
		var sugesstionFromOfficers=jQuery("#officerSuggestions").val();
		var actionTaken=jQuery("#actionTaken").val();
		
		var sugesstionId="#suggestion"+value;
		var actionTakenId="#actionTaken"+value;
		
		jQuery(sugesstionId).text(sugesstionFromOfficers);
		jQuery(actionTakenId).text(actionTaken);
		
		resetSoilData();
		 $("#addRow").attr("onclick","addDetails()"); 
	}
	
	function editRow(value){
		var sugesstionId="#suggestion"+value;
		var actionTakenId="#actionTaken"+value;
		
		jQuery("#officerSuggestions").val(jQuery(sugesstionId).text());
		jQuery("#actionTaken").val(jQuery(actionTakenId).text());
		
		$("#addRow").attr("onclick","editedRow("+value+")");
	}
	
	function deleteRow(value){
		var sugesstionId="#suggestion"+value;
		var actionTakenId="#actionTaken"+value;
		
		jQuery(sugesstionId).text('');
		jQuery(actionTakenId).text('');
		jQuery("#soiltestRow"+value).attr("class","hide");
		
		
	}
	function resetSoilData(){
		jQuery("#officerSuggestions").val('');
		jQuery("#actionTaken").val('');
		
		$("#addRow").attr("onclick","addDetails()"); 
	}
</script>

<%-- <tr>
	<td><div>
			<s:text name="farm.soilTesting" />
		</div></td>
	<td><div>

			<s:radio id="soilTesting" list="soilTestMap" name="farm.soilTesting"
				listKey="key" listValue="value" theme="simple"
				onchange="suggestionTableShow(this.value);" />
		</div></td>
</tr> --%>

<div class="flexi flexi10">
	<label for="txt">
		<s:text name="farm.soilTesting" />
	</label>
	
	<div class="form-element">
		<s:radio id="soilTesting" list="soilTestMap" name="farm.soilTesting"
				listKey="key" listValue="value" theme="simple"
				onchange="suggestionTableShow(this.value);" />
	</div>
</div>
<table class="table table-bordered">
<tr id="suggestionDiv" class="hide">
	<th><s:text name="farm.officerSuggestions" /></th>
	<th><s:text name="farm.actionTaken" /></th>
	<th><s:text name="action" /></th>
</tr>

<tr id="suggestionValueDiv" class="hide">
	<td><s:textfield id="officerSuggestions" name="suggestions"
			maxlength="150" cssClass="form-control input-sm"/></td>
	<td><s:textfield id="actionTaken" name="actionTake" maxlength="150" cssClass="form-control input-sm"/></td>


	<td class="textAlignCenter">
		<button id="addRow" onclick="addDetails()" aria-hidden="true"
			class="btn btn-sm btn-success" type="button">
			<i class="fa fa-check"></i>
		</button>
		<button onclick="resetSoilData()" aria-hidden="true "
			class="btn btn-sm btn-danger" type="button">
			<i class="fa fa-trash"></i>
		</button>
	</td>
</tr>

<tr id="soilDetailInfo" class="tableTemplate">
	<th><s:text name="farm.officerSuggestions" /></th>
	<th><s:text name="farm.actionTaken" /></th>
	<th><s:text name="action" /></th>
</tr>

<tfoot id="tblSoilData" class="panel-collapse collapse">
	
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:iterator value="farmerSoilTestingList" status="status">
			<tr id='soiltestRow<s:property value="%{#status.count}" />'>
				<td align="center"><div id='suggestion<s:property value="%{#status.count}" />'><s:property value="officersSuggestion" /></div></td>
				<td align="center"><div id='actionTaken<s:property value="%{#status.count}" />'><s:property value="takenAction" /></div></td>
				<td style="text-align:center;">
					<button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="editRow('<s:property value="%{#status.count}" />')"><i class=" fa fa-pencil-square-o"></i></button>
					<button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteRow('<s:property value="%{#status.count}" />')"><i class="fa fa-trash" aria-hidden="true"></i></button>
				</td>	
				<input type="hidden" value='<s:property value="id" />' id='soilTestId<s:property value="%{#status.count}" />'/>	
			</tr>
		</s:iterator>
	</s:if>
</tfoot>
</table>