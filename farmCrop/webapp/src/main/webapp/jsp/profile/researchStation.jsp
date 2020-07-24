<%@ taglib uri="/struts-tags" prefix="s"%>
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
	<s:form name="form" cssClass="fillform" action="researchStation_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden name="researchStation.id" />
		</s:if>
		<s:hidden key="command" />
		<table class="table table-bordered aspect-detail">
			<tr>
				<th colspan="2"><s:property value="%{getLocaleProperty('info.researchStation')}" />
			</tr>


			<s:if test='"update".equalsIgnoreCase(command)'>
				<tr class="odd">
					<td width="35%"><div class="col-xs-6">
							<s:text name="researchStation.researchStationId" />
						</div></td>

					<td width="65%">
						<div class="col-xs-6">
							<s:property value="researchStation.researchStationId" />
						</div> <s:hidden key="researchStation.researchStationId" />
					</td>
				</tr>
				<s:if test='branchId==null'>
					<tr class="odd">
						<td width="35%"><div class="col-xs-6">
								<s:text name="app.branch" />
							</div></td>
						<td width="65%"><div class="col-xs-6">
								<s:property value="%{getBranchName(researchStation.branchId)}" />
							</div></td>
					</tr>
				</s:if>

			</s:if>
			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="researchStation.name" />
						<sup style="color: red;">*</sup>
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:textfield cssClass="form-control input-sm"
							name="researchStation.name" theme="simple" maxlength="20" />
					</div></td>
			</tr>
			
			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="country.name" />
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:select name="selectedCountry" list="countries" listKey="name"
							listValue="name" headerKey=""  headerValue="%{getText('txt.select')}" theme="simple"
							id="country" onchange="listState(this)"
							cssClass="col-sm-4 form-control select2" />
					</div></td>
			</tr>
			<tr class="odd">
				<td><div class="col-xs-6">
						<s:property value="%{getLocaleProperty('state.name')}"/>
						<sup style="color: red;"></sup>
					</div></td>
				<td><div class="col-xs-6">
						<s:select name="selectedState" list="states" listKey="name"
							listValue="name" headerKey=""  headerValue="%{getText('txt.select')}" theme="simple"
							id="state" onchange="listLocality(this)"
							cssClass="col-sm-4 form-control select2" />
					</div></td>
			</tr>
			
			<tr class="odd" id="locality">
				<td><div class="col-xs-6">
						<s:property value="%{getLocaleProperty('locality.name')}"/>
					</div></td>
				<td class="odd"><div class="col-xs-6">
						<s:select name="researchStation.city.name" id="localites"
							list="localities" listKey="name" listValue="name" headerKey=""
						 headerValue="%{getText('txt.select')}" theme="simple" onchange="populateTaluks(this)"
							cssClass="col-sm-4 form-control select2" />
					</div></td>
			</tr>
			
			<tr class="odd" >
				<td><div class="col-xs-6">
						<s:property value="%{getLocaleProperty('city.name')}" />
					</div></td>
				<td class="odd"><div class="col-xs-6">
						<s:select name="researchStation.municipality.name" id="taluk"
							list="municipalities" listKey="name" listValue="name" headerKey=""
							 headerValue="%{getText('txt.select')}" theme="simple"
							cssClass="col-sm-4 form-control select2" />
					</div></td>
			</tr>

			
			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="researchStation.pointPerson" />
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:textfield cssClass="form-control input-sm"
							name="researchStation.pointPerson" theme="simple" maxlength="25" />
					</div></td>
			</tr>


			<%-- <tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="researchStation.division" />
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:textfield cssClass="form-control input-sm"
							name="researchStation.division" theme="simple" maxlength="10" />
					</div></td>
			</tr> --%>


			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="researchStation.designation" />
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:textfield cssClass="form-control input-sm"
							name="researchStation.designation" theme="simple" maxlength="25"/>
					</div></td>
			</tr>


			<tr class="odd">
				<td width="35%"><div class="col-xs-6">
						<s:text name="researchStation.researchStationAddress" />
					</div></td>
				<td width="65%"><div class="col-xs-6">
						<s:textarea name="researchStation.researchStationAddress" maxLength="200"
							cssClass="form-control input-sm" />
					</div></td>
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
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
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
	<s:form name="cancelform" action="researchStation_list.action">
		<s:hidden key="currentPage" />
	</s:form>
	<script type="text/javascript">
	function listState(obj){
		var selectedCountry = $('#country').val();
		jQuery.post("researchStation_populateState.action",{id:obj.value,dt:new Date(),selectedCountry:obj.value},function(result){
			insertOptions("state",JSON.parse(result));
			listLocality(document.getElementById("state"));
		});
	}
	
	function listLocality(obj){
		var selectedState = $('#state').val();
		jQuery.post("researchStation_populateLocality.action",{id:obj.value,dt:new Date(),selectedState:obj.value},function(result){
			insertOptions("localites",JSON.parse(result));
			//populateTaluks(document.getElementById("localites"));
		});
	}
 function populateTaluks(obj){
	 var selectedDistrict=$("#localites").val();
	 jQuery.post("researchStation_populateTaluks",{selectedDistrict:selectedDistrict},function(result){
		 insertOptions("taluk",JSON.parse(result));
	 });
 }
 


</script>
</body>