<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
</head>
<style>
</style>
<script type="text/javascript">
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	var typezz='';
	$(document).ready(function() {
		typezz=<%out.print("'" + request.getParameter("type") + "'");%>
		if(typezz=='service'){
			$(".breadCrumbNavigation").html('');
			$(".breadCrumbNavigation").append("<li><a href='#'>Service</a></li>");
			$(".breadCrumbNavigation").append("<li><a href='moleculeProcess_list.action?type=service'>Molecule</a></li>");
			$("#typez").val('service');
		}
	});
	
	function validate() {
		var error=true;
		var currentTenant = '<s:property value="getCurrentTenantId()"/>';
		var img=$('#farmerImage').val();
		var lot=$('#samithiName').val();
		 if(isEmpty(img) || img==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please select a file"/>';
        		error = false;
	   			  } 
		 if(isEmpty(lot) || lot==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please Select Lot No"/>';
     		error = false;
	   			  } 
		if(error==true){
			document.form.submit();
		}
		
		
	}
</script>

<body>
	<s:form name="form" cssClass="fillform" action="moleculeProcess_create" method="post" enctype="multipart/form-data" >
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden key="command" />
		<div class="appContentWrapper marginBottom">
			 <div class="error">
							<p class="notification">
								<%-- <span class="manadatory">*</span>
								<s:text name="reqd.field" /> --%>
							<div id="validateError" style="text-align: center;"></div>
							<div id="validatePriceError" style="text-align: center;"></div>
							</p>
                            </div>

			<div class="formContainerWrapper">
				<div class="flexiWrapper">
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('Lot No')}" /><sup id="mandatory" style="color: red;">*</sup>
							<span style="font-size: 8px">
						</span> </label>
						<div class="form-element showAge">
							<%-- <s:textfield id="samithiName" name="molecule.landHolding"
								theme="simple" maxlength="45" cssClass="form-control" /> --%>
									<s:select name="molecule.landHolding" list="listLotCode" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" id="samithiName"
											cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					
								<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('moleculeTypeName')}" /><sup id="mandatory" style="color: red;">*</sup>
							<span style="font-size: 8px">
						</span> </label>
						<div class="form-element showAge">
							<%-- <s:textfield id="samithiName" name="molecule.landHolding"
								theme="simple" maxlength="45" cssClass="form-control" /> --%>
									<s:select name="molecule.type" list="moleculeTypeList" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" id="moleculeType"
											cssClass="form-control input-sm select2" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('File')}" /> 
								
								<sup id="mandatory" style="color: red;">*</sup>
							<span style="font-size: 8px">
						</span>
						</label>
						<div class="form-element">
							<s:file name="farmerImage" id="farmerImage"
								 cssClass="form-control" />
							
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="margin-top-10">
			<s:if test="command =='create'">
				<span class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="validate()">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<s:else>
				<span class=""><span class="first-child">
						<button type="button" onClick="validate()"
							class="save-btn btn btn-success">
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
	<s:form name="cancelform" action="moleculeProcess_list.action?type=service">
		<s:hidden key="currentPage" />
	</s:form>

</body>