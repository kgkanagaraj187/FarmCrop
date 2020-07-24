<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<div class="error">
<sup>*</sup> <s:text name="reqd.field" /></div>
<font color="red"> <s:actionerror /> <s:fielderror /></font>
<s:form name="form" cssClass="fillform"
	action="subCategoryService_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden name="subCategory.category.name" value="Vegetables"/>
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="subCategory.id" />
	</s:if>
	<s:hidden key="command" />
	<table  class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:property value="%{getLocaleProperty('info.category')}" /></th>
		</tr>
<%-- 
		<tr class="odd">
			<td><s:text name="category.code" /><sup style="color: red;">*</sup></td>

			<td><s:if test='"update".equalsIgnoreCase(command)'>
				<s:property value="subCategory.code" />
				<s:hidden key="subCategory.code" />
				<s:if test='branchId==null'>
				<tr class="odd">
					<td><s:text name="app.branch" /></td>
					<td><s:property value="%{getBranchName(subCategory.branchId)}" /></td>
				</tr>
			    </s:if>	
			</s:if> <s:else>
				<s:textfield id="code" name="subCategory.code" theme="simple"
					maxlength="8" />
				

			</s:else></td>
		</tr> --%>
		
		<s:if test='"update".equalsIgnoreCase(command)'>
		<s:if test='branchId==null'>
				<tr class="odd">
					<td><div class="col-xs-6"><s:text name="app.branch" /></div></td>
					<td><div class="col-xs-6"><s:property value="%{getBranchName(subCategory.branchId)}" /></div></td>
				</tr>
			    </s:if>	
			    </s:if>
			    

		<tr class="odd">
			<td><div class="col-xs-6"><s:text name="category.name" /><sup style="color: red;">*</sup></div></td>
			<td><div class="col-xs-6"><s:textfield id="name" name="subCategory.name"
				theme="simple" maxlength="50" cssClass="form-control input-sm"/></div></td>
		</tr>

	</table>
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" onclick="submit()" class="save-btn btn btn-success"><font
			color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>
		</span></span>
	</s:if> <s:else>
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF">
		<b><s:text name="update.button" /></b> </font></button>
		</span></span>
	</s:else> <span id="cancel" class=""><span class="first-child">
	<button type="button" class="cancel-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="cancel.button" /> </font></b></button>
	</span></span></div>
</s:form>
<s:form name="cancelform" action="subCategoryService_list.action">
	<s:hidden key="currentPage" />
</s:form>
<script>
function submit(){
	alert(document.getElementById("code").value);
	alert(document.getElementById("name").value);
	document.form.submit();
}

</script>
</body>