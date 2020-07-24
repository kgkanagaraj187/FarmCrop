<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>


<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<body>
<script type="text/javascript">

$(document).ready(function () {
    //called when key is pressed in textbox
  	$("#mobileNo").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#errMsgMobNo").html("Digits Only").show().fadeOut("slow");
               return false;
     	}
   	});
  
  	$("#landlineNo").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#errMsgLandNo").html("Digits Only").show().fadeOut("slow");
               return false;
     	}
   	});
  	
  	$("#contactPersonName").keypress(function (e) {
  	    //if the letter is not alphabet then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 65 || e.which > 90) && (e.which < 97 || e.which > 122)) {
        	//display error message
       		$("#errMsgContactPName").html("Alphabets Only").show().fadeOut("slow");
               return false;
    	}
    })
});
	
</script>



<s:form name="form" cssClass="fillform" action="masterData_%{command}">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="masterData.id" />
		<s:hidden key="masterData.masterType"/>
	</s:if>
	<s:hidden key="command" />
	
			<div class="appContentWrapper marginBottom">
				<div class="error"><s:actionerror /><s:fielderror />
				<s:if test="hasActionErrors()">
				  <div style="color:red;">
					<s:text name="cannotDeleteMasterDataHasTxn"/>
				      <s:actionerror/>
				   </div>
				</s:if>
				
				<sup>*</sup>
				<s:text name="reqd.field" /></div>
				<div class="formContainerWrapper">
					<h2><s:text name="info.masterData" /></h2>
					
					
						
			<div class="flexiWrapper">
						<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.code" /></label>
								<div class="form-element">
									<s:property value="masterData.code"  />
								</div>
								 <s:hidden key="masterData.code" />
						</div>
						</s:if>
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.type" /><sup style="color: red;">*</sup></label>
								
								<div class="form-element">
									<s:if test="'update'.equalsIgnoreCase(command)">
										<s:property value="masterData.masterType"/>
									</s:if>	
									<s:else>
									<s:select name="masterData.masterType" list="masterTypeList" headerKey="-1" headerValue="%{getText('txt.select')}" class="form-control" theme="simple" id="masterType" />
									</s:else>
								</div>
						</div>
					
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.name" /><sup style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield name="masterData.name" theme="simple" maxlength="45" class="form-control"/>
								</div>
						</div>
						
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.contactPersonName" /></label>
								<div class="form-element">
									<s:textfield name="masterData.contactPersonName" theme="simple"  maxlength="90" class="form-control"/>
								</div>
						</div>
						
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.mobileNo" /></label>
								<div class="form-element">
									<s:textfield name="masterData.mobileNo" theme="simple" maxlength="10" class="form-control"/>
								</div>
						</div>
						
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.landlineNo" /></label>
								<div class="form-element">
								<s:textfield name="masterData.landlineNo" theme="simple" maxlength="10" class="form-control"/>
								</div>
						</div>
						
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.emailAddress" /></label>
								<div class="form-element">
								<s:textfield name="masterData.emailAddress" theme="simple" maxlength="90" class="form-control"/>
								</div>
						</div>
						
						<div class="flexi flexi10">
								<label for="txt"><s:text name="masterData.address" /></label>
								<div class="form-element">
								<s:textarea name="masterData.address" theme="simple" maxlength="255" />
								</div>
						</div>
						
					</div>
						<div class="yui-skin-sam"><s:if test="command =='create'">
							<span id="button" class=""><span class="first-child">
							<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
								name="save.button" /></b> </font></button>
							</span></span>
							</s:if> <s:else>
							<span id="button" class=""><span class="first-child">
							<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
								name="save.button" /></b> </font></button>
							</span></span></s:else>
							<span id="cancel" class=""><span class="first-child"><button type="button" class="cancel-btn btn btn-sts">
					              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
					                </font></b></button></span></span>
						</div>
					</div>	
				</div>	
	<br />

	
</s:form>
<s:form name="cancelform" action="masterData_list.action">
    <s:hidden key="currentPage"/>
</s:form>
</body>