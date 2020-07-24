<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>

<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout">
<!--<script type="text/javascript" src="yui/element/element-beta-min.js"></script>
<script type="text/javascript" src="yui/button/button-min.js"></script>-->
<style type="text/css">
redText1 {
	color: #CB171D;
	float: left;
}
</style>
</head>
<script type="text/javascript">


function onCreate(){	

	
	document.form.submit();
}

function selectedprofession(data){
	 if(data==4){
		jQuery(".otherProfession").show();
	}else if(data==-1){
		jQuery(".otherProfession").hide();
	}
	else{
		jQuery(".otherProfession").hide();
	}
}

$(document).ready(function(){
	
	selectedprofession(jQuery("#profession").val());
});

function onCancel(){	
	document.cancelform.submit();
}
</script>

<body>

<s:form name="form" cssClass="fillform" action="farmerFamily_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden name="farmerId" value="%{farmerId}" />
		<s:hidden name="farmerName" value="%{farmerName}" />

		<s:hidden name="tabIndexz" value="%{tabIndexz}" />
		<s:hidden name="tabIndex" />
		
		<s:hidden key="command" />
			<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="farmerFamily.id" />
		</s:if>
<div class="appContentWrapper marginBottom">
	<div class="error">
		<s:actionerror />
		<s:fielderror />
		<div style="color: #ff0000">
		<sup>*</sup>
		<s:text name="reqd.field" />
	</div> <span id="validateError"></span>
	</div>
			
			
			
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#familyDetailInfo" class="accrodianTxt"> <s:text
							name="%{getLocaleProperty('info.familyDetail')}" />
					</a>
				</h2>
			</div>
			<div id="farmerCropInfo" class="panel-collapse collapse in">
				<div class="flexform">
				<div class="flexform-item" >
								<label for="txt"><s:property value="%{getLocaleProperty('name')}" /><sup
								style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										id="nameValue" name="farmerFamily.name"
										theme="simple" maxlength="30" />
								</div>
					</div>
					
					<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('headOfFamily')}" />
							</label>
							<div class="form-element">
								<s:checkbox name="farmerFamily.headOfFamily" />
							</div>
						</div>
					<div class="flexform-item" >
					<label for="txt"><s:property value="%{getLocaleProperty('farmerFamilyGender')}" /><sup
								style="color: red;">*</sup></label>
					<div class="form-element">
			<td><s:radio list='genderType' name="farmerFamily.gender"/></td>
			</div></div>
			
			
			<div class="flexform-item" >
								<label for="txt"><s:property value="%{getLocaleProperty('farmerFamilyage')}" /><sup
								style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										id="ageValue" name="farmerFamily.age"
										theme="simple" maxlength="30" onkeypress="return isNumber(event)"/>
								</div>
					</div>
					
					
					<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('familyDetail.relationship')}" /><sup
								style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="familyDetailRelationship"
									name="farmerFamily.relation" headerKey="" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="catalogueRelationshipList" />
							</div>
						</div>
						
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('familyDetail.education')}" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="familyDetailEducation"
									name="farmerFamily.education" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="educationList" />
							</div>
						</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('familyDetail.disability')}" /></label>
						<div class="form-element">
							<td><s:radio list='disabilityType'
									name="farmerFamily.disability" /></td>
						</div>
					</div>

					<div class="flexform-item disablity" >
								<label for="txt"><s:property value="%{getLocaleProperty('familyDetail.disableDetail')}" /><sup
								style="color: red;">*</sup></label>
								<div class="form-element">
									<s:textfield cssClass="form-control input-sm"
										id="disableDetail" name="farmerFamily.disableDetail"
										theme="simple" maxlength="30" />
								</div>
					</div>

					<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('familyDetail.maritalStatus')}" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="familyDetailMarital"
									name="farmerFamily.maritalStatus" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="maritalSatuses" />
							</div>
						</div>
						
						
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('familyDetail.educationStatusBelow18')}" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="familyDetaileducationStatus"
									name="farmerFamily.educationStatus" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="educationStatusList" />
							</div>
						</div>
				
				
				</div>
				<div class="flexItem flex-layout flexItemStyle">
					<s:if test="command =='create'">
						<span id="button" class="yui-button"><span
							class="first-child">
								<button type="button" class="save-btn btn btn-success"
									id="sucessbtn">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span></span>
					</s:if>
					<s:else>
						<span id="button" class="yui-button"><span
							class="first-child">
								<button type="button" class="save-btn btn btn-success">
									<font color="#FFFFFF"> <b><s:text
												name="update.button" /></b>
									</font>
								</button>
						</span></span>
					</s:else>
					<span id="cancel" class="yui-button"><span
						class="first-child">
							<button type="button" onclick="onCancel()"
								class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
								</font></b>
							</button>
					</span></span>
				</div>
				</div>
			
</div>
</s:form>
<s:form name="cancelform" action="farmer_detail.action">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerFamily.farmer.id}" />
	<s:hidden name="tabIndex" value="#tabs-6" />
	<s:hidden name="currentPage" />
</s:form>

<%-- <font color="red"> <s:actionerror /> <s:fielderror /><s:actionmessage />
<sup>*</sup> <s:text name="reqd.field" />

<div style="float:right; color:#000000">
<tr>
			<td width="25%"><s:text name="Farmer : " /></td>
			<td width="25%" colspan="3"><s:if
				test='"update".equalsIgnoreCase(command)'>
				<s:property value="farmerFamily.farmer.farmerId" />
				<s:text name="-" />
				<s:property value="farmerFamily.farmer.firstName" />
				<s:text name=" "/>
				<s:property value="farmerFamily.farmer.lastName"/>
				<s:hidden name="farmerFamily.farmer.farmerId" />
			</s:if> <s:else>
				<s:property value="farmerSeqId" />
				<s:text name="-" />
				<s:property value="farmerName" />
				
			</s:else></td>
		</tr>
</div>

</font>
<div><s:form name="form" id="form"
	action="farmerFamily_%{command}" cssClass="fillform"
	enctype="multipart/form-data">
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden name="farmerFamily.id" />
	</s:if>

	<s:hidden name="farmerId" />
	<s:hidden name="tabIndex" />
	<s:hidden name="command" />
	<s:hidden name="heading" />
	<s:hidden name="currentPage" />
	<table width="500" cellspacing="0">
		<tr>
			<th colspan="2"><s:text name="info.personal" /></th>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="farmerFamilyName" /></td>
			<td width="65%"><s:textfield name="farmerFamily.name" maxlength="35"/></td>
		</tr>
		
		<tr class="odd">
			<td><s:text name="farmerFamilyAge" /></td>		
			<td><s:textfield name="farmerFamily.age" maxLength="2" value="%{ageValidate(farmerFamily.age)}" id="age" /></td>			
		</tr>
		
		<tr class="odd">
			<td><s:text name="farmerFamilyGender" /></td>
			<td><s:radio list='genderType' name="farmerFamily.gender"/></td>
		</tr>	
		
		<tr class="odd">
			<td><s:text name="farmerFamilyRelation" /></td>
			<td><s:select name="farmerFamily.relation" id="relation"
			    headerKey="-1" headerValue="%{getText('txt.select')}" list='relations' /></td>
		</tr>
		
		<tr class="odd">
			<td><s:text name="farmerFamilyEducation" /></td>
			<td><s:select name="farmerFamily.education" id="education"
				headerKey="-1" 	headerValue="%{getText('txt.select')}" list='educations' /></td>
		</tr>
		<tr class="odd">
			<td><s:text name="farmerFamilyProfession" /></td>
			<td><s:select name="farmerFamily.profession" id="profession"
				headerKey="-1" 	headerValue="%{getText('txt.select')}" list='profession' onchange="selectedprofession(this.value);" /></td>
		</tr>
		
		<tr class="otherProfession">
			<td><s:text name="other" /></td>
			<td><s:textfield name="farmerFamily.otherProfession" id="otherValue" /></td>	
		</tr>
		<!--<s:if test='headFamily == null'>
			<tr class="odd">
				<td><s:text name="headOfFamily" /></td>
				<td><s:checkbox name="farmerFamily.headOfFamily" />
			</tr>
		</s:if>
		<s:elseif test='headOfTheFamily == currentFamily'>
			<tr class="odd">
				<td><s:text name="headOfFamily" /></td>
				<td><s:checkbox name="farmerFamily.headOfFamily" />
			</tr>
		</s:elseif>
		<tr class="odd">
			<td><s:text name="activity" /></td>
			<td><s:textfield name="farmerFamily.activity" maxlength="255"/>
		</tr>
		<tr class="odd">
			<td><s:text name="wageEarner" /></td>
			<td><s:checkbox name="farmerFamily.wageEarner" />
		</tr>-->
	</table>
	<br />

	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span  class=""><span class="first-child">
		<button type="button" onclick="onCreate();" class="save-btn"><font
			color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>
		</span></span>
	</s:if> <s:else>
		<span  class=""><span class="first-child">
		<button type="button" onclick="onCreate();" class="save-btn"><font color="#FFFFFF">
		<b><s:text name="save.button" /></b> </font></button>
		</span></span>
	</s:else> <span id="cancel" class=""> <span class="first-child">
	<button type="button" onclick="onCancel();" class="cancel-btn"><b>
	<FONT color="#FFFFFF"> <s:text name="cancel.button" /> </FONT> </b></button>
	</span> </span></div>
</s:form> <s:form name="listForm" id="listForm"
	action="farmer_detail.action%{tabIndex}">
	<s:hidden name="farmerId" />
	<s:hidden name="id" value="%{farmerId}" />
	<s:hidden name="tabIndex" />
	<s:hidden name="currentPage" />
</s:form> 
</div> --%>

<script type="text/javascript">


function onCreate(){	

	
	document.form.submit();
}

function selectedprofession(data){
	 if(data==4){
		jQuery(".otherProfession").show();
	}else if(data==-1){
		jQuery(".otherProfession").hide();
	}
	else{
		jQuery(".otherProfession").hide();
	}
}

$(document).ready(function(){
	var type=$('input:radio[name="farmerFamily.disability"]:checked').val();
	if(type=='1')
		$('.disablity').removeClass("hide");
	else
		$('.disablity').addClass("hide");
     $("input[name='farmerFamily.disability']:radio").change(function () {
    	 if($(this).val()=='1')
    		 $('.disablity').removeClass("hide");
    	 if($(this).val()=='2')
    		 $('.disablity').addClass("hide");
    	 	$('#disableDetail').val("");
     });
 /*     
	if('<s:property value="command"/>'=="update"){
		//alert("2: "+$("input[name='farmerFamily.disability']").val());
		if(($("input[name='farmerFamily.disability']").val())==1){
			$('.disablity').removeClass("hide");
		}
		if(($("input[name='farmerFamily.disability']").val())==2){
	    		 $('.disablity').addClass("hide");
		}
	} */
	selectedprofession(jQuery("#profession").val());
});

function onCancel(){	
	document.cancelform.submit();
}
function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
</script>

</body>
</html>