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

<script type="text/javascript">
function submitForm(){
	selectOptions('selectedDailyReportId');
	selectOptions('selectedconsolidatedReportId');
	document.form.submit();
}

function selectOptions(selectId){
	document.getElementById(selectId).multiple = true; //to enable all option to be selected
	for (var x = 0; x < document.getElementById(selectId).options.length; x++)//count the option amount in selection box
	    document.getElementById(selectId).options[x].selected = true;
}

$(function () {
	$(".panel-body").find("input[type='button'][value='<s:text name="RemoveAll"/>']").addClass("btn btn-warning");
	$(".panel-body").find("input[type='button'][value='<s:text name="Add"/>']").addClass("btn btn-small btn-success fa fa-step-forward");
	$(".panel-body").find("input[type='button'][value='<s:text name="Remove"/>']").addClass("btn btn-danger");
	$(".panel-body").find("input[type='button'][value='<s:text name="AddAll"/>']").addClass("btn btn-sts");
});	
</script>

<s:form name="form" cssClass="fillform" action="reportMailConfiguration_%{command}">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="reportMailConfiguration.id" id="id"/></s:if>
	<s:hidden key="command" />
	<s:hidden key="selecteddropdwon" id="listname"/>
	<s:hidden key="temp" id="temp"/>
	
	<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
<%--  <div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" /></div> --%>

 <div class="ferror" id="errorDiv" style="color: #ff0000">
					<s:actionerror />
					<s:fielderror />
				</div> 

					<h2><s:text name="info.reportMailConfiguration" /></h2>
	<table class="table table-bordered aspect-detail">
	
		<tr class="odd">
			<td width="35%"><s:text name="reportMailConfiguration.name" /><sup
				style="color: red;">*</sup></td>
			<td width="85%">
			<s:if test='"update".equalsIgnoreCase(command)'>
				<s:property value="reportMailConfiguration.name" />
				<s:hidden key="reportMailConfiguration.name" /></s:if>
			<s:else>
			<s:textfield name="reportMailConfiguration.name" theme="simple" maxlength="45"/></s:else></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="reportMailConfiguration.mailId" /><sup
				style="color: red;">*</sup></td>
			<td width="85%"><s:textfield name="reportMailConfiguration.mailId" theme="simple" maxlength="45"/></td>
		</tr>

		<tr class="odd">
		 	<td width="35%"><s:text name="reportMailConfiguration.status" /></td>
			<td width="35%"><s:select id="reportMailConfigurationStatus" name="reportMailConfiguration.status" 
				list="reportMailConfigurationStatus" listKey="key" listValue="value" cssClass="col-sm-4 form-control select2" headerValue="%{getText('txt.select')}"/>
			</td>
		</tr>

		<tr class="odd">
 	 		<td><s:text name="reportMailConfiguration.dailyReport"/><sup
				style="color: red;">*</sup></td>
 		 	<td><div class="panel-body" style="margin-left: 2%" id="dynOpt">
	 		 	<s:text name="availableDailyReport" var="availableDailyReportTitle" />
				<s:text name="selectedDailyReport" var="selectedDailyReportTitle" />
				<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control" id="dailyreport" 
				allowSelectAll="false" allowUpDownOnLeft="false" labelposition="top"
				allowUpDownOnRight="false" name="availableDailyReportName"
				list="availableDailyReports" listKey="key" listValue="value"
				leftTitle="%{availableDailyReportTitle}" rightTitle="%{selectedDailyReportTitle} %{reqdSymbol}"
				 headerKey="headerKey" doubleName="selectedDailyReportName"
				doubleId="selectedDailyReportId" doubleList="selectedDailyReports"
				doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="Remove All"
				addAllToRightLabel="Add All" addToLeftLabel="Remove"
				addToRightLabel="Add" cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;"/></div></td>
		</tr>
		

		<tr class="odd">
 	 		<td><s:text name="reportMailConfiguration.consolidatedReport"/><sup
				style="color: red;">*</sup></td>
 		 	<td><div class="panel-body" style="margin-left: 2%" id="dynOpt">
 		 		<s:text name="availableconsolidatedReport" var="availableconsolidatedReportTitle" />
				<s:text name="selectedconsolidatedReport" var="selectedconsolidatedReportTitle" />
				<s:optiontransferselect doubleCssStyle="width:300px;height:400px;overflow-x:auto;" doubleCssClass="form-control"  id="consolidatedReport" buttonCssClass="optTrasel"
				allowSelectAll="false" buttonCssStyle="font-weight:bold!important;" allowUpDownOnLeft="false" labelposition="top"
				allowUpDownOnRight="false" name="availableConsolidatedReportName"
				list="availableconsolidatedReports" 
				leftTitle="%{availableconsolidatedReportTitle}" rightTitle="%{selectedconsolidatedReportTitle} %{reqdSymbol}"
				 headerKey="headerKey" doubleName="selectedConsolidatedReportName"
				doubleId="selectedconsolidatedReportId" doubleList="selectedconsolidatedReports"
				doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="Remove All"
				addAllToRightLabel="Add All" addToLeftLabel="Remove"
				addToRightLabel="Add" cssClass="form-control" cssStyle="width:300px;height:400px;overflow-x:auto;" /></div></td>
		</tr>
		
		
</table>
</div>

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span  class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success" onclick="submitForm();"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		
	</s:if> <s:else>
		<span class=""><span class="first-child">
		<button type="button"  class="save-btn btn btn-success" onclick="submitForm();"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span>
	</s:else>
	
	 <span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div>
</div>
</div>
</div>
</div>
</s:form>

<s:form name="cancelform" action="reportMailConfiguration_list.action">
    <s:hidden key="currentPage"/>
</s:form>
