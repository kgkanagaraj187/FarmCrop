<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<font color="red"><s:actionerror /> <s:fielderror /> <sup>*</sup>
	<s:text name="reqd.field" /> </font>
<s:form name="form" cssClass="fillform" action="locality_%{command}">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="locality.id" />
	</s:if>
	<s:hidden key="command" />
	<s:hidden key="temp" id="temp" />
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:property
					value="%{getLocaleProperty('info.locality')}" /></th>
		</tr>
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:if test='branchId==null'>
				<tr class="odd">
					<td width="35%"><div class="col-xs-6">
							<s:text name="app.branch" />
						</div></td>
					<td width="65%"><div class="col-xs-6">
							<s:property value="%{getBranchName(locality.branchId)}" />
						</div></td>
				</tr>
			</s:if>
		</s:if>
	
			<tr class="odd">
			<td><div class="col-xs-3"><s:text name="country.name" /><sup
				style="color: red;">*</sup></td>
			<td><div class="col-xs-6"><s:select name="selectedCountry" list="countries"
				Key="name" Value="name" headerKey="" headerValue="%{getText('txt.select')}"
				theme="simple" id="country" onchange="listState(this)" cssClass="form-control input-sm select2"/></div></td>
		</tr>
	
		<tr class="odd">
			<td><div class="col-xs-6"><s:property value="%{getLocaleProperty('state.name')}"/><sup style="color: red;">*</sup></div></td>
			<td><div class="col-xs-6"><s:select name="selectedState" list="states" Key="code"
				Value="name" headerKey="" headerValue="%{getText('txt.select')}" theme="simple"
				id="state" cssClass="form-control input-sm select2"/></div></td>
		</tr>
		<s:if test='"update".equalsIgnoreCase(command)'>
			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="locality.code" />
						<sup style="color: red;">*</sup>
					</div></td>
				<td width="65%">
					<div class="col-xs-6">
						<s:property value="locality.code" />
					</div> <s:hidden key="locality.code" />
				</td>
			</tr>
		</s:if>
		<tr class="odd">
			<td width="35%"><div class="col-xs-6">
					<s:text name="locality.name" />
					<sup style="color: red;">*</sup>
				</div></td>
			</td>
			<td width="65%"><div class="col-xs-6">
					<s:textfield name="locality.name" theme="simple" maxlength="35"
						cssClass="form-control input-sm" />
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
<s:form name="cancelform" action="locality_list.action">
	<s:hidden key="currentPage" />
</s:form>

<script type="text/javascript">
function listState(obj){
	
	var selectedCountry = $('#country').val();
	jQuery.post("municipality_populateState.action",{id:obj.value,dt:new Date(),selectedCountry:obj.value},function(result){
		insertOptions("state",JSON.parse(result));
		listLocality(document.getElementById("state"));
	});
}
</script>


