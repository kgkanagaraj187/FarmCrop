<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
<div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" /></div>
<s:form name="form" cssClass="fillform" action="trainingTopic_%{command}">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="trainingTopic.id" />
	</s:if>
	<s:hidden key="command" />
	
	<table class="table table-bordered aspect-detail" >
		<tr>
			<th colspan="2"><s:text name="info.trainingTopic" /></th>
		</tr>
		
		<s:if test='"update".equalsIgnoreCase(command)'>
		<s:if test='branchId==null'>
		<tr class="odd">
			<td width="35%"><s:text name="app.branch" /></td>
			<td width="65%"><s:property value="%{getBranchName(trainingTopic.branchId)}" /></td>
		</tr>
		</s:if>	
		</s:if>
		
		<%-- <tr class="odd">
		
			<td width="35%"><s:text name="trainingTopic.code" /><sup
				style="color: red;">*</sup></td>
			
			<td width="65%">
			<s:if test='"update".equalsIgnoreCase(command)'>
			<s:if test='branchId==null'>
		<tr class="odd">
			<td width="35%"><s:text name="app.branch" /></td>
			<td width="65%"><s:property value="%{getBranchName(trainingTopic.branchId)}" /></td>
		</tr>
		</s:if>	
					<s:property value="trainingTopic.code" />
						<s:hidden key="trainingTopic.code" />
				</s:if>
				<s:else>
					<s:textfield name="trainingTopic.code" theme="simple" maxlength="35"/>
				</s:else>
			</td>
		</tr> --%>
		
		<tr class="odd">
			<td width="35%"><s:text name="trainingTopic.name" /><sup
				style="color: red;">*</sup></td></td>
			<td width="65%"><s:textfield name="trainingTopic.name" theme="simple" maxlength="45"/>
		</tr>
		
	</table>
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="save.button" /></b> </font></button>
		</span></span>
		</s:if> <s:else>
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span></s:else>
		<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>
	</div>
</s:form>
<s:form name="cancelform" action="trainingTopic_list.action">
    <s:hidden key="currentPage"/>
</s:form>
</body>