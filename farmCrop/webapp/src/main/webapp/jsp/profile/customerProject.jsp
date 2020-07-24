<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
function listCertificateStandards(call){
	var callback = {
	    success: function (oResponse) 	{
			var result=oResponse.responseText;
			var certificateStandardComboObj = document.getElementById("certificateStandardsList");
			certificateStandardComboObj.length = 0;
			addOption(certificateStandardComboObj, "Select", "-1");			
			var standards = YAHOO.lang.JSON.parse(result);
			for(var count=0;count<standards.length;count++){
				var standard = standards[count];
				var key = jQuery.trim(standard.csId.toString());
				var value = jQuery.trim(standard.csName.toString());
				if(key!="" && value!=""){
					addOption(certificateStandardComboObj, value, key);
				}			
			}
	    }	   
	}
	var data = "certificateCategoryId="+call.value;	 
	var url='customerProject_populateCertificateStandards.action';	   
    var conn = YAHOO.util.Connect.asyncRequest("POST",url,callback,data);		
}

function onCancel(){
		document.listForm.submit();
}
</script>
<body>
<div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" /></div>
<s:form name="form" cssClass="fillform" action="customerProject_%{command}">
	<s:hidden key="currentPage"/>	
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden name="customerProject.id" />	
	</s:if>
	<s:hidden name="customerId" />
	<s:hidden name="tabIndex" />
	<s:hidden name="command" />
	<s:hidden name="heading" />
	<s:hidden name="currentPage" />
	<s:hidden name="customerUniqueId"/>
	
	<table width="95%" cellspacing="0">
		<tr>
			<th colspan="2"><s:text name="info.customer" /></th>
		</tr>
		
		
	<s:if test='"update".equalsIgnoreCase(command)'>
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.customerProjectCode" /></td>
			
			<td width="65%">		
					<s:property value="customerProject.codeOfProject" />
					<s:hidden key="customerProject.codeOfProject" />
			</td>
		</tr>
		</s:if>
		
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.nameOfProject" /></td>
			<td width="65%"><s:textfield name="customerProject.nameOfProject" theme="simple" maxlength="35"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.numberOfProject" /></td>
			<td width="65%"><s:textfield name="customerProject.numberOfProjects" theme="simple" maxlength="4"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
			<tr class="odd">
			<td width="35%"><s:text name="customerProject.reportNo" /></td>
			<td width="65%"><s:textfield name="customerProject.reportNo" theme="simple" maxlength="35"/><sup
				style="color: red;">*</sup></td>
		</tr>
			<tr class="odd">
			<td width="35%"><s:text name="customerProject.unitNo" /></td>
			<td width="65%"><s:textfield name="customerProject.unitNo" theme="simple" maxlength="35"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.nameOfUnit" /></td>
			<td width="65%"><s:textfield name="customerProject.nameOfUnit" theme="simple" maxlength="35"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.locationOfUnit" /></td>
			<td width="65%"><s:textfield name="customerProject.locationOfUnit" theme="simple" maxlength="35"/><sup
				style="color: red;">*</sup></td>
		</tr>
		<!-- 
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.typeOfHolding" /></td>
			<td width="65%"><s:radio list="holdingTypeList"
				listKey="key" listValue="value" name="selectedHoldingType"
				theme="simple" /></td>
		</tr>
		 -->
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.certificateCategory" /></td>
			<td width="65%"><s:select list="certificateCategories"
				 headerKey="-1" headerValue="%{getText('txt.select')}" name="customerProject.certificateStandard.category.id"
				 listKey="id" listValue="name" onchange="listCertificateStandards(this)"
				/><sup
				style="color: red;">*</sup>				
				</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.certificateStandard" /></td>
			<td width="65%"><s:select list="certificateStandards"
				 headerKey="-1" headerValue="%{getText('txt.select')}" name="customerProject.certificateStandard.id" id="certificateStandardsList"
				 listKey="id" listValue="name"
				/><sup
				style="color: red;">*</sup>				
				</td>
		</tr> 
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.inspectionType" /></td>
			<td width="65%"><s:select list="inspectionTypeList"
				 headerKey="-1" headerValue="%{getText('txt.select')}" name="selectedInspectionType"
				/><sup
				style="color: red;">*</sup>				
				</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.icsStatus" /></td>
			<td width="65%"><s:select list="icsList"
				 headerKey="-1" headerValue="%{getText('txt.select')}" name="customerProject.icsStatus"
				/><sup
				style="color: red;">*</sup>				
				</td>
		</tr>
		<%--
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.unitComposition" /></td>
			<td width="65%"><s:radio list="unitCompoTypeList"
				listKey="key" listValue="value" name="selectedUnitCompoType"
				theme="simple" /></td>
		</tr>
		 --%>
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.numberOfFarmers" /></td>
			<td width="65%"><s:textfield name="customerProject.numberOfFarmers" theme="simple" maxlength="4"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="customerProject.area" /></td>
			<td width="65%"><s:textfield name="customerProject.area" theme="simple" maxlength="12"/><sup
				style="color: red;">*</sup></td>
		</tr>
		
	</table>
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		</s:if> <s:else>
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span></s:else>
		
		
		<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn" onclick="onCancel();">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div>
</s:form>

<s:form name="listForm" id="listForm" action="customer_detail.action%{tabIndex}">
    <s:hidden key="currentPage"/>
    <s:hidden name="tabIndex" />
	<s:hidden name="currentPage" />
	<s:hidden name="id" value="%{customerUniqueId}"/>
</s:form>
</body>

