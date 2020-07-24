<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style type="text/css">
.view {
    display: table-cell;
    background-color:#d2dae3;
}
</style>
<s:head/>
</head>

<script>

jQuery(document).ready(function(){
	$("#trainingTopic").val('<s:property value="farmerTraining.trainingTopic.name"/>');
	//jQuery("#farmerTrainingType").val("-1").trigger("change");
	//jQuery("#trainingTopic").val("-1").trigger("change");
	jQuery("#farmerTrainingCode").val("");
	//document.getElementById("farmerTrainingCode").value=" ";
	$('input:checkbox').removeAttr('checked');
	<s:iterator value='farmerTraining.selectedTopicId.split(",")' var="ids">
		jQuery('#<s:property value="#ids.trim()"/>_topic_chk_box').click()
	</s:iterator>	
	
});

$(document).ready(function(){
	var tenant='<s:property value="getCurrentTenantId()"/>';
	if(tenant=='wilmar'){
		jQuery(".tSelectType").addClass("hide");
	}
	else{
		jQuery(".tSelectType").removeClass("hide");
	}

	$(".panel-body").find("input[type='button'][value='<s:text name="RemoveAll"/>']").addClass("btn btn-warning");
	
	$(".panel-body").find("input[type='button'][value='<s:text name="Add"/>']").addClass("btn btn-small btn-success fa fa-step-forward");
	$(".panel-body").find("input[type='button'][value='<s:text name="Remove"/>']").addClass("btn btn-danger");
	$(".panel-body").find("input[type='button'][value='<s:text name="AddAll"/>']").addClass("btn btn-sts");
	
	})

function submitForm(){
	selectOptions('availableMethodOptionList');
//	selectOptions('selectedTargetGroupOptionList');
	selectOptions('selectedMethodOptionList');
	selectOptions('availableTrainingMaterialOptionList');
	selectOptions('selectedTrainingMaterialOptionList');
	selectOptions('availableObservationsOptionList');
	selectOptions('selectedObservationsOptionList');
	document.form.submit();
}

function selectOptions(selectId){
	document.getElementById(selectId).multiple = true; //to enable all option to be selected
	for (var x = 0; x < document.getElementById(selectId).options.length; x++)//count the option amount in selection box
	{
	    document.getElementById(selectId).options[x].selected = true;
	}
}

</script>

<s:form name="form" cssClass="fillform" action="farmerTrainingSelection_%{command}">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="farmerTraining.id" />
	</s:if>
	<s:hidden key="command" />
	<s:hidden key="selecteddropdwon" id="listname"/>
	<s:hidden key="temp" id="temp"/>
	
	<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
				<div class="error"><s:actionerror /><s:fielderror />
<%-- <sup>*</sup>
<s:text name="reqd.field" /> --%></div>
					<h2><s:property value="%{getLocaleProperty('info.farmerTrainingSelection')}" /></h2>
	<table class="table table-bordered aspect-detail">
	
		<tr class="odd">
			<td width="35%"><s:property value="%{getLocaleProperty('farmerTrainingSelection.code')}" /><sup
				style="color: red;">*</sup></td>
			<td width="65%">
			<s:if test='"update".equalsIgnoreCase(command)'>
					<s:property value="farmerTraining.code" />
						<s:hidden key="farmerTraining.code" />
				</s:if>
			<s:else>
			<s:textfield name="farmerTraining.code" theme="simple" maxlength="35"/></s:else></td>
		</tr>
		
		
	<%-- 	<tr class="tSelectType">
		 <td width="25%"><s:property value="%{getLocaleProperty('farmerTrainingSelection.type')}" /></td>
		  <td width="25%">
		 		<s:select id="farmerTrainingType" name="farmerTraining.selectionType" list="farmerTrainingType"   headerKey="-1" headerValue="%{getText('txt.select')}" theme="simple" cssClass="form-control input-sm select2"/>
		</td>
		
		</tr>	 --%>

		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('trainingTopic')}" /><sup style="color: red;">*</sup></td>
 		 <td><s:select name="farmerTraining.trainingTopic.name" list="trainingTopics" listKey="name" listValue="name"
 		 headerKey="-1" headerValue="%{getText('txt.select')}" theme="simple" id="trainingTopic" cssClass="form-control input-sm select2" /></td>
		</tr>

		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('topic.list')}" /><sup
				style="color: red;">*</sup></td>
 		 <td >
 		 
			<table class="table table-bordered">
			<thead>
			 <tr>
				<th><s:text name="topic.select"/></th>
				<th><s:text name="%{getLocaleProperty('topic.type')}"/></th>
				<th><s:text name="%{getLocaleProperty('topic.code')}"/></th>
				<th><s:text name="%{getLocaleProperty('topic.principle')}"/></th>
				<th><s:text name="%{getLocaleProperty('topic.name')}"/></th>
			</thead>
			<tbody>			
				<s:iterator value="topicList">
					<tr>
						<td><s:checkbox id="%{id}_topic_chk_box" name="farmerTraining.selectedTopicId" fieldValue="%{id}"></s:checkbox></td>
						<td><s:property value="topicCategory.name"/> </td>
						<td><s:property value="code"/> </td>
						<td><s:property value="principle"/> </td>
						<td><s:property value="des"/> </td>
					</tr>
				</s:iterator>
			</tbody>
			</table>
			
		</td>
		</tr>

		<%-- <tr class="odd">
 	 	<td><s:text name="targetGroup.name"/><sup
				style="color: red;">*</sup></td>
 		 <td>
			<s:text name="availableGroups" var="availableTitle" />    
			<s:text name="selectedGroups" var="selectedTitle" /> 
			<s:text name="RemoveAll" var="rmvall" />
			<s:text name="Remove" var="rmv"/>
			<s:text name="Add" var="add"/>
			<s:text name="AddAll" var="addall"/>
			 <div class="panel-body" style="margin-left: 2%" id="dynOpt">
 		 	<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" doubleList="selectedTargetGroups" list="availableTargetGroups" 
 		 	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle} %{reqdSymbol}"
 		 	name="farmerTraining.availableTargetId" doubleName="farmerTraining.selectedTargetId" listKey="id" listValue="name" 
 		 	buttonCssClass="optTrasel" allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top" allowUpDownOnRight="false"
 		 	addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}" addToRightLabel="%{add}"
 		 	doubleListKey="id" doubleListValue="name" doubleId="selectedTargetGroupOptionList" id="availableTargetGroupOptionList"
 		 	cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"></s:optiontransferselect>
 		 	</div>	 
 		 	</td>
		</tr> --%>

		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('trainingMethod')}" /><sup
				style="color: red;">*</sup></td>
 		 <td>
 			<s:text name="%{getLocaleProperty('availableMethods')}" var="availableTitle" />    
			<s:text name="%{getLocaleProperty('selectedMethods')}" var="selectedTitle" />
			<s:text name="%{getLocaleProperty('RemoveAll')}" var="rmvall" />
			<s:text name="%{getLocaleProperty('Remove')}" var="rmv"/>
			<s:text name="%{getLocaleProperty('Add')}" var="add"/>
			<s:text name="%{getLocaleProperty('AddAll')}" var="addall"/>
			 <div class="panel-body" style="margin-left: 2%" id="dynOpt">
 		 	<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" doubleList="selectedTrainingMethods" list="availableTrainingMethods" 
 		 	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle} %{reqdSymbol}"
 		 	name="farmerTraining.availableMethodId" doubleName="farmerTraining.selectedMethodId" listKey="id" listValue="name" 
 		 	buttonCssClass="optTrasel" allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top" allowUpDownOnRight="false"
			addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}" addToRightLabel="%{add}"
 		 	doubleListKey="id" doubleListValue="name" doubleId="selectedMethodOptionList" id="availableMethodOptionList"  
 		 	cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"></s:optiontransferselect>
 		 	</div>
 		</td>
		</tr>
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('trainingMaterial')}" /></td>
 		 <td>
 			<s:text name="%{getLocaleProperty('availableMaterials')}" var="availableTitle" />    
			<s:text name="%{getLocaleProperty('selectedMaterials')}" var="selectedTitle" />
			<s:text name="%{getLocaleProperty('RemoveAll')}" var="rmvall" />
			<s:text name="%{getLocaleProperty('Remove')}" var="rmv"/>
			<s:text name="%{getLocaleProperty('Add')}" var="add"/>
			<s:text name="%{getLocaleProperty('AddAll')}" var="addall"/>
			 <div class="panel-body" style="margin-left: 2%" id="dynOpt">
 		 	<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" doubleList="selectedTrainingMaterials" list="availableTrainingMaterials" 
 		 	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle} %{reqdSymbol}"
 		 	name="farmerTraining.availableTrainingMaterialId" doubleName="farmerTraining.selectedTrainingMaterialId" listKey="id" listValue="name" 
 		 	buttonCssClass="optTrasel" allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top" allowUpDownOnRight="false"
			addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}" addToRightLabel="%{add}"
 		 	doubleListKey="id" doubleListValue="name" doubleId="selectedTrainingMaterialOptionList" id="availableTrainingMaterialOptionList"  
 		 	cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"></s:optiontransferselect>
 		 	</div>
 		</td>
		</tr>
		
		
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('trainingObservation')}" /></td>
 		 <td>
 			<s:text name="%{getLocaleProperty('availableObservations')}" var="availableTitle" />    
			<s:text name="%{getLocaleProperty('selectedObservations')}" var="selectedTitle" />
			<s:text name="%{getLocaleProperty('RemoveAll')}" var="rmvall" />
			<s:text name="%{getLocaleProperty('Remove')}" var="rmv"/>
			<s:text name="%{getLocaleProperty('Add')}" var="add"/>
			<s:text name="%{getLocaleProperty('AddAll')}" var="addall"/>
			 <div class="panel-body" style="margin-left: 2%" id="dynOpt">
 		<%--  	<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" doubleList="selectedTrainingObservations" list="availableTrainingObservations" 
 		 	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle} %{reqdSymbol}"
 		 	name="farmerTraining.availableObservationsId" doubleName="farmerTraining.selectedObservationsId" listKey="id" listValue="name" 
 		 	buttonCssClass="optTrasel" allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top" allowUpDownOnRight="false"
			addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}" addToRightLabel="%{add}"
 		 	doubleListKey="id" doubleListValue="name" doubleId="selectedObservationsOptionList" id="availableObservationsOptionList"  
 		 	cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"></s:optiontransferselect>
 		 	 --%>
 		 	<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" doubleList="selectedTrainingObservations" list="availableTrainingObservations" 
 		 	leftTitle="%{availableTitle}" rightTitle="%{selectedTitle} %{reqdSymbol}"
 		 	name="farmerTraining.availableObservationsId" doubleName="farmerTraining.selectedObservationsId" listKey="id" listValue="name" 
 		 	buttonCssClass="optTrasel" allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top" allowUpDownOnRight="false"
			addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}" addToRightLabel="%{add}"
 		 	doubleListKey="id" doubleListValue="name" doubleId="selectedObservationsOptionList" id="availableObservationsOptionList"  
 		 	cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"></s:optiontransferselect>
 		 	</div>
 		</td>
		</tr>
		
		<%-- <s:if test='"update".equalsIgnoreCase(command)'>
		<s:if test='farmerTraining.status==1||farmerTraining.status==2'> --%>
		<tr id="status">
		 <td width="25%"><s:property value="%{getLocaleProperty('farmerTrainingSelection.status')}" /></td>
			<td width="25%"> <div class="col-xs-6"><s:select id="farmerTrainingStatus" name="farmerTraining.status" list="farmerTrainingStatus" listKey="key" listValue="value" cssClass="form-control input-sm"/></div>
			</td>
		</tr>	
		<%-- </s:if>
		</s:if> --%>
		
</table>
</div>

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span  class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success" onclick="submitForm()"><font color="#FFFFFF"> <b><s:property value="%{getLocaleProperty('save.button')}" /></b> </font></button>
		</span></span>
		
	</s:if> <s:else>
		<span class=""><span class="first-child">
		<button type="button"  class="save-btn btn btn-success" onclick="submitForm()"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span>
	</s:else>
	
	 <span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:property value="%{getLocaleProperty('cancel.button')}" />
                </font></b></button></span></span>
	</div>
</div>
</div>
</div>
</div>


	
</s:form>

<s:form name="cancelform" action="farmerTrainingSelection_list.action">
    <s:hidden key="currentPage"/>
</s:form>
