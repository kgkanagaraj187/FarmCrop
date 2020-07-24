<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
</head>
<style>
.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th,
	.table>thead>tr>td, .table>thead>tr>th {
	padding: 4px;
}

.table>tbody>tr>td:nth-of-type(2n+1) {
	width: 10%;
}
</style>

<s:form name="form" action="user_%{command}" method="post"
	cssClass="fillform" enctype="multipart/form-data">
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="user.id" id="id" />
	</s:if>
	<s:hidden key="command" />
	<s:hidden key="selecteddropdwon" id="listname" />
	<s:hidden key="temp" id="temp" />
	<div class="appContentWrapper marginBottom">

		<div class="error">
			<font color="red"> <s:actionerror /> <s:fielderror /></font> <sup>*</sup>
			<s:text name="reqd.field" />
		</div>
		<div class="formContainerWrapper">
			<h2>
				<s:text name="info.user" />
			</h2>
			<div class="flexform">
				<s:if test='isMultiBranch==1'>
					<div class="flexform-item">
						<s:if test='branchId==null'>
							<s:if test='"create".equalsIgnoreCase(command)'>
								<s:label theme="simple">
									<s:text name="app.branch" />
								</s:label>
								<div class="form-element">
									<s:select name="branchId_F" id="branchSelect" theme="simple"
										listKey="key" listValue="value" list="parentBranches"
										headerKey="-1" headerValue="%{getText('txt.select')}"
										onchange="populateSubBranch(this);populateRoleMulti(this)"
										cssClass="form-control input-sm select2" />
								</div>
							</s:if>
							<s:if test='"update".equalsIgnoreCase(command)'>
								<div class="flexform-item">
									<label for="txt"><s:text name="app.branch" /></label>
									<div class="form-element">
										<s:property value="%{getBranchName(user.parentBranchId)}" />
									</div>
								</div>
							</s:if>
						</s:if>
						<s:if
							test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							<s:if test='"create".equalsIgnoreCase(command)'>
								<div class="flexform-item">
									<label for="txt"><s:text name="app.subBranch" /></label>
									<div class="form-element">
										<s:select name="subBranchId_F" id="subBranchSelect"
											theme="simple" listKey="key" listValue="value"
											list="subBranchesMap" headerKey=""
											headerValue="%{getText('txt.select')}"
											onchange="populateRoleMulti(this)"
											cssClass="form-control input-sm select2" />
									</div>
								</div>
							</s:if>
							<s:if test='"update".equalsIgnoreCase(command)'>
								<div class="flexform-item">
									<label for="txt"><s:text name="app.subBranch" /></label>
									<div class="form-element">
										<s:property value="%{getBranchName(user.branchId)}" />
										<s:hidden name="user.branchId" />
									</div>
								</div>
							</s:if>
						</s:if>
					</div>
				</s:if>
				<s:else>
					<s:if test='branchId==null'>
						<s:if test='"create".equalsIgnoreCase(command)'>
							<div class="flexform-item">
								<label for="txt"><s:text name="app.subBranch" /></label>
								<div class="form-element">
									<s:select name="branchId_F" id="branchSelect" theme="simple"
										listKey="key" listValue="value" list="branchesMap"
										headerKey="-1" headerValue="%{getText('txt.select')}"
										onchange="populateRoles(this)"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
						<s:if test='"update".equalsIgnoreCase(command)'>
							<div class="flexform-item">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:property value="%{getBranchName(user.branchId)}" />
									<s:hidden name="user.branchId" />
								</div>
							</div>
						</s:if>
					</s:if>
				</s:else>
				<div class="flexform-item">
					<label> <s:text name="userProfile.username" /><sup
						style="color: red;"> * </sup></label>
					<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="form-element">
							<s:textfield size="25" readonly="true" name="user.username"
								theme="simple" cssClass="form-control input-sm" maxlength="45" />
						</div>
					</s:if>
					<s:else>
						<div class="form-element">
							<s:textfield name="user.username" theme="simple" size="25"
								maxlength="45" cssClass="form-control input-sm" />
						</div>
					</s:else>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:text name="personalInfo.firstName" />
						<sup style="color: red;">*</sup></label>
					<div class="form-element">
						<s:textfield id="firstName" name="user.personalInfo.firstName"
							theme="simple" size="25" maxlength="50"
							cssClass="upercls form-control input-sm" />
					</div>
				</div>
				
				<div class="flexform-item">
					<label for="txt"><s:text name="personalInfo.lastName" /></label>
					<div class="form-element">
						<s:textfield id="lastName" name="user.personalInfo.lastName"
							theme="simple" size="25" maxlength="35"
							cssClass="upercls form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:text name="user.lang" /> <sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select id="language" headerKey=""
							headerValue="%{getText('txt.select')}" name="user.language"
							list="languageMap" listKey="key" listValue="value" theme="simple"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('photo')}" /> <span
							style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:if test='userImageString!=null && userImageString!=" "'>
								<s:file name="userImage" id="userImage"
									onchange="checkImgHeightAndWidth(this)" />
							</s:if>
							<s:else>
								<s:file name="userImage" id="userImage"
									onchange="checkImgHeightAndWidth(this)" cssClass="form-control" />
							</s:else>
							<button type="button" class="aButtonClsWbg" id="remImg"
								onclick='deleteFile(<s:property value="user.id" />)'>
								<i class="fa fa-trash-o" aria-hidden="true"></i>
							</button>
						</div>
					</div>
				
			</div>
		</div>
	</div>
	
	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>
				<s:text name="info.contact" />
			</h2>
			<div class="flexform">
				<div class="flexform-item">
					<label for="txt"><s:text name="contactInfo.address" /></label>

					<div class="form-element">
						<s:textarea name="user.contactInfo.address1" theme="simple"
							maxlength="255" cssClass="form-control input-sm"
							style="height:40px;" />
					</div>
				</div>


				<div class="flexform-item">
					<label for="txt"><s:text name="contactInfo.phoneNumber" /></label>

					<div class="form-element">
						<s:textfield name="user.contactInfo.phoneNumber" theme="simple"
							maxlength="20" cssClass="form-control input-sm" />
					</div>
				</div>



				<div class="flexform-item">
					<label for="txt"><s:text name="contactInfo.mobileNumber" />
					</label>
					<div class="form-element">
						<s:textfield name="user.contactInfo.mobileNumber" theme="simple"
							maxlength="15" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item">
					<label for="txt"><s:text name="contactInfo.email" /> <sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:textfield name="user.contactInfo.email" theme="simple"
							maxlength="45" cssClass="form-control input-sm" />
					</div>
				</div>

			</div>
			<s:hidden name="user.personalInfo.identityType" value="04" />
			<s:hidden name="user.personalInfo.identityNumber" value="P101" />
			<s:hidden name="user.personalInfo.sex" value="MALE" />
			<s:hidden name="dateOfBirth" value="01/01/1993" />
			<%-- <s:hidden name="user.language" value="en" /> --%>
			<s:hidden name="user.contactInfo.zipCode" value="642001" />
			<s:hidden name="selectedCountry" value="India" />
			<s:hidden name="selectedState" value="Gujarat" />
			<s:hidden name="selectedLocality" value="Valsad" />
			<s:hidden name="user.contactInfo.city.name" value="Dharampur" />
		</div>
	</div>
	<div class="appContentWrapper marginBottom">
		<div class="formContainerWrapper">
			<h2>
				<s:text name="info.userCred" />
			</h2>
			<div class="flexform">
				<s:if test='"update".equalsIgnoreCase(command)'>
					<div class="flexform-item" id='passwordOptionDiv'>
						<label for="txt"><s:text name="user.changePassword" /></label>
						<div class="form-element">
							<s:checkbox key="user.changePassword" theme="simple"
								onclick="javascript:showPass(this.name);" />
						</div>
					</div>
				</s:if>
				<s:else>
					<div class="flexform-item" id='passwordOptionDiv'>
						<label for="txt"><s:text name="user.setPassword" /></label>
						<div class="form-element">
							<s:checkbox key="user.changePassword" theme="simple"
								onclick="javascript:showPass(this.name);" />
						</div>
					</div>
				</s:else>
				<div class="flexform-item">
					<label for="txt"><s:text name="user.enabled" /></label>
					<div class="form-element">
						<s:checkbox name="user.enabled" theme="simple" />
					</div>
				</div>
				<div class="flexform-item">
					<label for="txt"><s:text name="user.select.role" /> <sup
						style="color: red;">*</sup></label>
					<div class="form-element">
						<s:select name="user.role.id" id="selectRole" list="roles"
							listKey="id" listValue="name" theme="simple" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>

				<div class="flexform-item" id='pwd'>
					<label for="txt"> <s:text name="user.password" /> <sup
						style="color: red;"> *</sup></label>
					<div class="form-element">
						<s:password id="p" name="user.password" theme="simple"
							maxlength="16" cssClass="form-control input-sm" />
					</div>
				</div>

				<div class="flexform-item" id="pwd1">
					<label for="txt"> <s:text name="user.confirmpassword" /> <sup
						style="color: red;"> *</sup></label>
					<div class="form-element">
						<s:password id="p" name="user.confirmPassword" theme="simple"
							maxlength="16" cssClass="form-control input-sm" />
					</div>
				</div>
			</div>
		</div>
		<div class="margin-top-10">
			<s:if test="command =='create'">
				<span id="button1" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="onSubmit()" id="sucessbtn">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
				<span class=""><span class="first-child"><a
						id="cancel.link" href="user_list.action"
						class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
								<s:text name="cancel.button" />
						</font>
					</a></span></span>
			</s:if>
			<s:else>
				<span id="button1" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="onSubmit()" id="sucessbtn">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
				<span id="cancel" class=""><span class="first-child">
						<button type="button" class="cancel-btn btn btn-sts">
							<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
							</font></b>
						</button>
				</span></span>
			</s:else>
		</div>
	</div>
	</div>
	<script>
		function checkImgHeightAndWidth(val) {

			var _URL = window.URL || window.webkitURL;
			var img;
			var file = document.getElementById('userImage').files[0];

			if (file) {

				img = new Image();
				img.onload = function() {
					imgHeight = this.height;
					imgWidth = this.width;
				};
				img.src = _URL.createObjectURL(file);
			}
			$("#userImageExist").val("0");			
		}

		function getTwentyYearsBack() {
			return new Date().getFullYear() - 20;
		}

		jQuery(function($) {
			$("#calendar").datepicker({
				defaultDate : new Date(getTwentyYearsBack(), 01, 00)
			});
		});
	</script>

</s:form>
<s:form name="cancelform" action="user_list.action">
	<s:hidden key="currentPage" />
</s:form>

<script type="text/javascript">

<s:if test="user.changePassword==true">

	$(".hidePass").show();
	</s:if>
	<s:else>
	$(".hidePass").hide();
	</s:else>

	function showPass(element) {
		var group = document.getElementsByName(element);
		if (group[0].checked) {

			$(".hidePass").show();
		} else {
			$(".hidePass").hide();
		}
	}














	<s:if test="user.changePassword==true">
	document.getElementById('pwd').className = "flexform-item";
	document.getElementById('pwd1').className = "flexform-item";
	</s:if>
	<s:else>
	document.getElementById('pwd').className = "hide";
	document.getElementById('pwd1').className = "hide";
	</s:else>

	function showPass(element) {
		var group = document.getElementsByName(element);
		if (group[0].checked) {
			document.getElementById('pwd').className = "flexform-item";
			document.getElementById('pwd1').className = "flexform-item";
		} else {
			document.getElementById('pwd').className = "hide";
			document.getElementById('pwd1').className = "hide";
		}
	}

	function onSubmit() {
		 if(validateImage()){
			
			$("#target").submit();
			 document.form.submit();
		} 
		 $("#sucessbtn").prop('disabled', true);
	}
	
	function validateImage(){
		

		var file=document.getElementById('userImage').files[0];
		var filename=document.getElementById('userImage').value;
		var fileExt=filename.split('.').pop();			

		if(file != undefined){
			if(fileExt=='jpg' || fileExt=='jpeg' || fileExt=='png'||fileExt=='JPG'||fileExt=='JPEG'||fileExt=='PNG')
			{ 	
				if(file.size>51200){
					alert('<s:text name="fileSizeExceeds"/>');	
					hit=false;
					file.focus();
					enableButton();
					return false;		
				}//else if(imgWidth >260){
					//alert('<s:text name="imageWidthMsg"/>');
					//file.focus();
					//return false;	
				//}else if(imgHeight> 70){
					//alert('<s:text name="imageHeightMsg"/>');
					//file.focus();
					//return false;	
				//}
				
			}else{
			
				alert('<s:text name="invalidFileExtension"/>')	
				file.focus();
				return false;				
				}
		}
	return true;
	}
	
	 function enableButton(){
		jQuery(".save-btn").prop('disabled',false);
		} 


	function capitalizeName() {
		var arr = [ "firstName", "lastName", "middleName" ];
		for (var i = 0; i < arr.length; i++) {
			var txt1 = document.getElementById(arr[i]).value;
			if (txt1 != null || txt != "") {
				capital(arr[i], txt1);
			}
		}
	}
	function capital(id, txt) {
		$(document.getElementById(id)).val(
				txt.replace(/^(.)|\s(.)/g, function($1) {
					return $1.toUpperCase();
				}));
	}

	function populateRoles(obj) {
		var branchId = $(obj).val();
		reloadSelect('#selectRole', 'user_populateRoles.action?branchId_F='
				+ branchId);
	}
	
	function populateRoleMulti(obj) {
		var branchId = $(obj).val();
		if(isEmpty(branchId)){
			branchId = $("#branchSelect").val();
		}
		reloadSelect('#selectRole', 'user_populateRoles.action?branchId_F='
				+ branchId);
	}
	
	function populateSubBranch(obj){
		var branchId = $(obj).val();
		jQuery.post("user_populateSubBranches.action",{branchId_F:branchId},function(result){
				insertOptions("subBranchSelect",$.parseJSON(result));
		});
		
	}
	function deleteFile(id) {
		
		 if (confirm('<s:text name="confirmToDelete"/>')) {
			 $("#userImageExist").val("1");
			 $("#userImageString").val("");
			 $("#userImage").val("");
			 $('#image').attr('src', 'img/no-image.png');
		 }
		 
	}
</script>