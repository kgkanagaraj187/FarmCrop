<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->

<meta name="decorator" content="swithlayout">
</head>


<body>
	<div class="error">
		<s:actionerror />
		<s:fielderror />
		<sup>*</sup>
		<s:text name="reqd.field" />
	</div>
	<s:form name="form" cssClass="fillform" action="state_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="state.id" />
		</s:if>
		<s:hidden key="command" />
		<table class="table table-bordered aspect-detail">
			<tr>
				<th colspan="2"><s:property
						value="%{getLocaleProperty('info.state')}" /></th>
				<%-- 	<th colspan="2"><s:text name="info.state" /></th> --%>
			</tr>

			<tr class="odd">
				<td ><div class="col-xs-6">
						
						<s:text name="state.country" />
						<sup style="color: red;">*</sup>
					</div>
				</td>
				<td ><div class="col-xs-6">
						<s:select name="state.country.name" list="countries"
							Key="name" headerKey="" 
							headerValue="%{getText('txt.select')}" Value="name"
							theme="simple" cssClass="form-control input-sm select2" />
					</div>
				</td>
			</tr>

			<s:if test='"update".equalsIgnoreCase(command)'>
				<tr class="odd">
					<td width="35%">
						<div class="col-xs-6">
							<s:text name="state.code" />
							<sup style="color: red;">*</sup>
						</div>
					</td>
					<td width="65%">
						<div class="col-xs-4">
							<s:property value="state.code" />
						</div> <s:hidden key="state.code" />
					</td>
				</tr>

				<s:if test='branchId==null'>
					<tr class="odd">
						<td width="35%">
							<div class="col-xs-6">
								<s:text name="app.branch" />
							</div>
						</td>
						<td width="65%">
							<div class="col-xs-6">
								<s:property value="%{getBranchName(state.branchId)}" />
							</div>
						</td>
					</tr>
				</s:if>
			</s:if>

			<tr class="odd">
				<td width="35%">
						<div class="col-xs-6">
						<s:text name="state.name" />
					<sup style="color: red;">*</sup>
						</div>
				</td>
				</td>
				<td width="65%">
					<div class="col-xs-6">
						<s:textfield name="state.name" theme="simple" maxlength="35"
						 cssClass="form-control input-sm"/>
					</div>
			</tr>
		</table>
		<br />

		<div class="yui-skin-sam">
			<s:if test="command =='create'">
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>

			</s:if>
			<s:else>
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else>
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="cancel-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
						</font></b>
					</button></span></span>
		</div>
	</s:form>
	<s:form name="cancelform" action="state_list.action">
		<s:hidden key="currentPage" />
	</s:form>

</body>