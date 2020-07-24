<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style>
	.txtLength {
		width:500px!important
	}
</style>
</head>
<body>
<div class="error">
<sup>*</sup>
<s:text name="reqd.field" />
<s:actionerror /><s:fielderror />
</div>
<s:form name="form" cssClass="fillform" action="topicCriteria_%{command}">	
	<s:hidden name="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden name="topic.id" />
	</s:if>
	<s:hidden name="command" />
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:text name="info.topicCriteria" /></th>
		</tr>
		
		<%-- <tr class="odd">
			<td width="35%"><s:text name="topic.code" /><sup style="color: red;">*</sup></td>			
			<td width="65%">
				<s:if test='"update".equalsIgnoreCase(command)'>
					<s:property value="topic.code" />
					<s:hidden key="topic.code" />
				</s:if>
				<s:else>
					<s:textfield name="topic.code" maxlength="35"/>
				</s:else>
			</td>
		</tr> --%>
		<s:hidden key="topic.code" />
		<s:if test='"update".equalsIgnoreCase(command)'>
		<s:if test='branchId==null'>
		<tr class="odd">
			<td width="35%"><s:text name="app.branch" /></td>
			<td width="65%"><s:property value="%{getBranchName(topic.branchId)}" /></td>
		</tr>
		</s:if>	
		</s:if>
		
		<tr class="odd">
			<td width="35%"><s:text name="topiccriteriatopic.principle" /><sup style="color: red;">*</sup></td>
			<td width="65%">
			<div class="col-xs-4">
				<s:textfield name="topic.principle"  theme="simple" maxlength="255" cssClass="form-control input-sm"/>
			</div></td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="topic.des" /><sup style="color: red;">*</sup></td>
			<td width="65%">
			<div class="col-xs-4">
				<s:textfield name="topic.des"  theme="simple" maxlength="255"  cssClass="form-control input-sm"/>
			</div>
			</td>
		</tr>
		
		<tr class="odd">
			<td class="odd"><s:text name="topic.topicCategory" /><sup style="color: red;">*</sup></td>
			<td class="odd">
			<div class="col-xs-4">
				<s:select name="topic.topicCategory.id" list="topicCategoryList" listKey="id" listValue="name" headerKey="" theme="simple" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm select2"></s:select>
			</div>
			</td>
		</tr>
	</table>
	<br />

	<div class="yui-skin-sam">
		<s:if test="command =='create'">
			<!-- Save Button -->
			<span id="button" class=""><span class="first-child">
				<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"><b><s:text name="save.button" /></b></font></button>
			</span></span>
		</s:if> 
		<s:else>
			<!-- Update Button -->
			<span id="button" class=""><span class="first-child">
				<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"><b><s:text name="update.button" /></b></font></button>
			</span></span>
		</s:else>
		<!-- Cancel Button -->
		<span id="cancel" class=""><span class="first-child">
			<button type="button" onclick="document.cancelform.submit();"class="cancel-btn btn btn-sts"><b><FONT color="#FFFFFF"><s:text name="cancel.button"/></font></b></button>
		</span></span>
	</div>
</s:form>
<s:form name="cancelform" action="topicCriteria_list.action">
    <s:hidden key="currentPage"/>
</s:form>
</body>