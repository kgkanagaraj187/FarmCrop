<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>

	<s:form name="form" cssClass="fillform" action="customer_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden name="customer.id" />
		</s:if>
		<s:hidden key="command" />
			<div class="appContentWrapper marginBottom">

				 <div class="error">
					<sup>*</sup>
					<s:text name="reqd.field" />
					<s:actionerror />
					<s:fielderror />
				</div> 

				<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.customer')}" />
					</h2>
					<div class="flexiWrapper">
						<s:if test='"update".equalsIgnoreCase(command)'>
							<div class="flexi flexi10">
								<label for="txt"><s:text name="customer.customerId" />
									</label>
								<div class="form-element">
									<s:property value="customer.customerId" />
									<s:hidden key="customer.customerId" />
								</div>
							</div>
							<%-- <s:if test='branchId==null'>
								<div class="flexi flexi10">
									<label for="txt"><s:text name="app.branch" />
									</label>
									<div class="form-element">
										<s:property value="%{getBranchName(customer.branchId)}" />
									</div>
								</div>
							</s:if> --%>
						</s:if>
						<div class="flexi flexi10">
							<label for="txt"><s:text name="customer.customerName" />
								<sup style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="customer.customerName" theme="simple" maxlength="45" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('customer.customerType')}" /></label>
							<div class="form-element">
								<s:select name="customer.customerType"
									cssClass="col-sm-4 form-control select2"
									list="listOfCustomerTypes" listKey="key" listValue="value"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>
						</div>
						
					<h2>
						Location Information
					</h2>
					<div class="flexiWrapper">
						<div class="flexi flexi10">
							<label for="txt"> <s:text name="country.name" />
							</label>
							<div class="form-element">
								<s:select name="selectedCountry" list="countries" Key="code"
									Value="name" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									id="country" onchange="listState(this)"
									cssClass="col-sm-4 form-control select2" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('state.name')}" /> <sup
								style="color: red;"></sup>
							</label>
							<div class="form-element">
								<s:select name="selectedState" list="states" Key="code"
									Value="name" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									id="state" onchange="listLocality(this)"
									cssClass="col-sm-4 form-control select2" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('locality.name')}" /> </label>
							<div class="form-element">
								<s:select name="customer.city.name" id="localites"
									list="localities" Key="code" Value="name" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									onchange="populateTaluks(this)"
									cssClass="col-sm-4 form-control select2" />
							</div>
						</div>


						<div class="flexi flexi10">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('city.name')}" />
							</label>
							<div class="form-element">
								<s:select name="customer.municipality.name" id="taluk"
									list="municipalities" Key="code" Value="name" headerKey=""
									headerValue="%{getText('txt.select')}" theme="simple"
									cssClass="col-sm-4 form-control select2" />
							</div>
						</div>


						


						<div class="flexi flexi10">
							<label for="txt"> <s:text name="customer.customerAddress" />
							</label>
							<div class="form-element">
								<s:textarea name="customer.customerAddress" maxLength="255"
									cssClass="form-control input-sm" />
							</div>
						</div>
						</div>
					
						<h2>Personal Information</h2>
					<div class="flexiWrapper">
						<div class="flexi flexi10">
							<label for="txt"> <s:text name="customer.personName" />
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="customer.personName" theme="simple" maxlength="90" />
							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('customer.customerSegment')}" />
							</label>
							<div class="form-element">
								<s:select name="customer.customerSegment"
									cssClass="col-sm-4 form-control select2"
									list="listOfCustomerSegment" listKey="key" listValue="value"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"> <s:text name="customer.mobileNumber" />
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="customer.mobileNumber" theme="simple" maxlength="10" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"> <s:text name="customer.email" />
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="customer.email" theme="simple" maxlength="45" />
							</div>
						</div>

						<div class="flexi flexi10">
							<label for="txt"> <s:text name="customer.landLine" />
							</label>
							<div class="form-element">
								<s:textfield cssClass="form-control input-sm"
									name="customer.landLine" theme="simple" maxlength="10" />
							</div>
						</div>

					</div>
</div>
</br>
					<div class="yui-skin-sam">
						<s:if test="command =='create'">
							<span id="button" class=""><span class="first-child">
									<button type="button" class="save-btn btn btn-success">
										<font color="#FFFFFF"> <b><s:text
													name="save.button" /></b>
										</font>
									</button>
							</span></span>
						</s:if>
						<s:else>
							<span id="button" class=""><span class="first-child">
									<button type="button" class="save-btn btn btn-success">
										<font color="#FFFFFF"> <b><s:text
													name="save.button" /></b>
										</font>
									</button>
							</span></span>
						</s:else>
						<span id="cancel" class=""><span class="first-child"><button
									type="button" class="cancel-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text
												name="cancel.button" /> </font></b>
								</button></span></span>
					</div>

				</div>
		


	</s:form>
	<s:form name="cancelform" action="customer_list.action">
		<s:hidden key="currentPage" />
	</s:form>
	<script type="text/javascript">
		function listState(obj) {
			var selectedCountry = $('#country').val();
			jQuery.post("customer_populateState.action", {
				id : obj.value,
				dt : new Date(),
				selectedCountry : obj.value
			}, function(result) {
				insertOptions("state", JSON.parse(result));
				listLocality(document.getElementById("state"));
			});
		}

		function listLocality(obj) {
			var selectedState = $('#state').val();
			jQuery.post("customer_populateLocality.action", {
				id : obj.value,
				dt : new Date(),
				selectedState : obj.value
			}, function(result) {
				insertOptions("localites", JSON.parse(result));
				populateTaluks(document.getElementById("localites"));
			});
		}
		function populateTaluks(obj) {
			var selectedDistrict = $("#localites").val();
			jQuery.post("customer_populateTaluks", {
				selectedDistrict : selectedDistrict
			}, function(result) {
				insertOptions("taluk", JSON.parse(result));
			});
		}
	</script>
</body>