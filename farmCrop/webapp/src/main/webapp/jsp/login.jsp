<%@ taglib uri="/struts-tags" prefix="s"%>
<%-- <%@ taglib uri="/ese-tags" prefix="e"%> --%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>
<head>
<META name="decorator" content="loginlayout">
</head>

<body>
  <div class="authentication-page-content p-4 d-flex align-items-center min-vh-100">
                            <div class="w-100">
                                <div class="row justify-content-center">
                                    <div class="col-lg-9">
                                    
                                      <div>
                                                    <a href="#" class="logo"><img src="login_populateLogo.action?logoType=app_logo" alt="logo"></a>
                                                </div>
	<div class="p-2 mt-5">
		<h4 class="sts">Sign in to your account</h4>
		<s:if test="branchEnabled==1">
			<p>Select Branch, Enter Username and Password to Log-in
			</p>
		</s:if>
		<s:else>
			<p>Please Enter your Name and Password to Login.</p>
		</s:else>
		<s:form name="loginform" class="form-login form-horizontal" action="login">
	<font style="color: red;"> <s:actionerror theme="bootstrap"/>
			</font>
			
			<div class="loginError" style="color: red;">
				<div id="check" class="hide">
					
				</div>
			</div> 
			<div id="validateError" class=" hide alert alert-danger" style="text-align: center;"></div>
			
				<s:if test="isMultibranchApp==0">
					<s:if test="branchEnabled==1">
						<div class="form-group">
							<s:select id="branch" theme="bootstrap" name="branchId"
								list="branches" listKey="key" listValue="value" headerKey=""
								headerValue="%{mainBranchName}" cssClass="form-control inputTxt select2" />
						</div>
					</s:if>

					<div class="form-group auth-form-group-custom mb-4">
						  <i class="ri-user-2-line auti-custom-input-icon"></i>
                            <label for="username">Username</label><s:textfield id="userName"
								size="20" key="j_username" placeholder="Enter username"
								cssClass="form-control" required="required" />
							
						
					</div>
					<div class="form-group auth-form-group-custom mb-4">
						  <i class="ri-lock-2-line auti-custom-input-icon"></i>
                             <label for="userpassword">Password</label> <s:password id="password"
								size="20" key="j_password" showPassword="true"
							 placeholder="Enter password"
								cssClass="form-control"
								required="required" /> <%-- <a
							class="forgot" href="#"> <s:text name="forgotPassword" /> --%>
						</a>
				
					</div>
				</s:if>
				<s:else>
				<div class="form-group">
							<s:select id="branch" theme="bootstrap" name="branchId"
								list="branches" listKey="key" listValue="value" headerKey=""
								headerValue="%{mainBranchName}" cssClass="form-control inputTxt select2" />
						</div>
					<div class="form-group auth-form-group-custom mb-4">
						  <i class="ri-user-2-line auti-custom-input-icon"></i>
                            <label for="username">Username</label> <s:textfield id="userName"
								size="20" key="j_username" placeholder="Enter username"
								cssClass="form-control" required="required" />
						
						
					</div>
					<div class="form-group auth-form-group-custom mb-4">
						   <i class="ri-lock-2-line auti-custom-input-icon"></i>
                                                        <label for="userpassword">Password</label><s:password id="password"
								size="20" key="j_password" showPassword="true"							
								cssClass="form-control"  placeholder="Enter password"
								required="required" /> <%-- <a
							class="forgot" href="#"> <s:text name="forgotPassword" /> --%>
						</a>
						
					</div>
				</s:else>
				
				
				
				<div class="form-actions ">
							<label for="remember" class="custom-checkbox-inline"> <s:checkbox
							id="agree" cssClass="grey remember" style="" key="agree"
							 onclick="checkClick()" /> <s:text
							name="license.agree" />
					</label> 
							
						</div>
						
						<div class="mt-4 text-center">
                          <button id="btnLogin" name="login" class="btn btn-primary w-md waves-effect waves-light"
							type="button" onclick="onCreate()">
							<s:text name="login.button" />
							<i class="fa fa-arrow-circle-right"></i>
						</button>
                          </div>
				
			
			
		</s:form>
	</div>

	<%-- <div class="box-forgot">
		<h4 class="sts">
			<s:text name="forgotPassword" />
		</h4>
		<p>Enter User Name or Email address to Reset your Password.</p>
		<form>
			<div class="form-group">
				<span class="input-icon username"> <s:textfield id="name"
						maxlength="50" type="email" cssClass="form-control sts-input"
						name="username" placeholder="Username/Email Id" /> <i
					class="fa fa-user"></i>
				</span> <span class="input-icon email"> <input type="email"
					class="form-control sts-input" name="email" placeholder="Email">
					<i class="fa fa-envelope"></i>
				</span>
			</div>

			<div class="form-group">
				<label class="radio-inline credentialRadio"> <s:radio
						list="credentialList" id="credential" listKey="key"
						listValue="value" theme="simple" name="credentialName" />
				</label>

			</div>
			
			<div class="form-group">
							<s:radio id="credential" list="credentialList" listKey="key"
						listValue="value" theme="simple" name="credentialName"/>
			</div>
			
			

			<div class="form-group form-actions">
				<button id="back" type="button"  class="btn btn-sts go-back pull-left" /> 				
					<s:text name="Back" />
					<i class="fa fa-arrow-circle-left"></i> 
				</button>
				<button id="reset" type="button" class="btn btn-sts pull-right btn-primary login-btn" onclick="validateUser();" />
				<s:text name="sendEmail" />
				<i class="fa fa-arrow-circle-right"></i>
				</button>
			</div>
		</form>
	</div> --%>

	<!-- <div id="license" class="modal fade" tabindex="-1" style="top:-14px; left:0; bottom:inherit;" >
		<div class="modal-dialog-lg">
			<div class="modal-content" style="background-color:white;">
				<div class="modal-header" >
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title sts">License Agreement</h4>
				</div>
				<div class="modal-body" align="left" >
					<p>LICENSEE AGREES TO DEFEND, INDEMNIFY, AND HOLD HARMLESS
						SOURCETRACE, ITS OFFICERS, MEMBERS, EMPLOYEES, CONTRACTORS,
						AFFILIATES AND ITS LICENSORS FROM AND AGAINST ANY DAMAGES, COSTS,
						LIABILITIES, SETTLEMENT AMOUNTS AND/OR EXPENSES (INCLUDING
						ATTORNEYS FEES) INCURRED IN CONNECTION WITH ANY CLAIM, LAWSUIT OR
						ACTION BY ANY PARTY OR THIRD PARTY THAT ARISES OR RESULTS FROM
						(angel) THE USE OR DISTRIBUTION OF THE SOLUTION OR ANY PORTION
						THEREOF, OR (beer) ANY BREACH OF LICENSEES REPRESENTATIONS,
						WARRANTIES AND COVENANTS SET FORTH IN THIS AGREEMENT</p>
					<p>DISCLAIMER OF WARRANTIES AND LIMITATION OF LIABILITY.</p>
					<p>DISCLAIMER OF WARRANTIES. LICENSEE AGREES THAT TO THE
						MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, SOURCETRACE AND ITS
						SUPPLIERS PROVIDE THE SOLUTION AND SUPPORT SERVICES (IF ANY) AS IS
						AND WITH ALL FAULTS, AND HEREBY DISCLAIM ALL OTHER WARRANTIES AND
						CONDITIONS, EITHER EXPRESS, IMPLIED OR STATUTORY, INCLUDING, BUT
						NOT LIMITED TO, ANY (IF ANY) IMPLIED WARRANTIES, DUTIES OR
						CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A PARTICULAR
						PURPOSE, OF ACCURACY OR COMPLETENESS OF RESPONSES, OF RESULTS, OF
						WORKMANLIKE EFFORT, OF LACK OF VIRUSES, AND OF LACK OF NEGLIGENCE,
						ALL WITH REGARD TO THE SOLUTION, AND THE PROVISION OF OR FAILURE
						TO PROVIDE SUPPORT SERVICES. ALSO, THERE IS NO WARRANTY OR
						CONDITION OF TITLE, QUIET ENJOYMENT, QUIET POSSESSION,
						CORRESPONDENCE TO DESCRIPTION OR NON-INFRINGEMENT WITH REGARD TO
						THE SOLUTION. THE ENTIRE RISK AS TO THE QUALITY OR ARISING OUT OF
						USE OR PERFORMANCE OF THE SOLUTION AND SUPPORT SERVICES, IF ANY,
						REMAINS WITH LICENSEE.
					<p>
					<p>EXCLUSION OF INCIDENTAL, CONSEQUENTIAL AND CERTAIN OTHER
						DAMAGES. TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, IN NO
						EVENT SHALL SOURCETRACE OR ITS SUPPLIERS BE LIABLE FOR ANY
						SPECIAL, INCIDENTAL, INDIRECT, PUNITIVE OR CONSEQUENTIAL DAMAGES
						WHATSOEVER (INCLUDING, BUT NOT LIMITED TO, DAMAGES FOR LOSS OF
						PROFITS OR CONFIDENTIAL OR OTHER INFORMATION, FOR BUSINESS
						INTERRUPTION, FOR PERSONAL INJURY, FOR LOSS OF PRIVACY, FOR
						FAILURE TO MEET ANY DUTY INCLUDING OF GOOD FAITH OR OF REASONABLE
						CARE, FOR NEGLIGENCE, AND FOR ANY OTHER PECUNIARY OR OTHER LOSS
						WHATSOEVER) ARISING OUT OF OR IN ANY WAY RELATED TO THE USE OF OR
						INABILITY TO USE THE SOLUTION, THE PROVISION OF OR FAILURE TO
						PROVIDE SUPPORT SERVICES, OR OTHERWISE UNDER OR IN CONNECTION WITH
						ANY PROVISION OF THIS AGREEMENT, EVEN IN THE EVENT OF THE FAULT,
						TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY, BREACH OF CONTRACT
						OR BREACH OF WARRANTY OF SOURCETRACE OR ANY SUPPLIER, AND EVEN IF
						SOURCETRACE OR ANY SUPPLIER HAS BEEN ADVISED OF THE POSSIBILITY OF
						SUCH DAMAGES.</p>
					<p>LIMITATION OF LIABILITY AND REMEDIES. NOTWITHSTANDING ANY
						DAMAGES THAT LICENSEE MIGHT INCUR FOR ANY REASON WHATSOEVER
						(INCLUDING, WITHOUT LIMITATION, ALL DAMAGES REFERENCED ABOVE AND
						ALL DIRECT OR GENERAL DAMAGES), LICENSEE AGREES THAT THE ENTIRE
						LIABILITY OF SOURCETRACE AND ALL OF ITS SUPPLIERS UNDER ANY
						PROVISION OF THIS AGREEMENT AND LICENSEES EXCLUSIVE REMEDY FOR ALL
						OF THE FOREGOING SHALL BE LIMITED TO THE GREATER OF THE ACTUAL FEE
						PAID BY LICENSEE TO SOURCETRACE UNDER THIS AGREEMENT OR U.S. $5.
						THE FOREGOING LIMITATIONS, EXCLUSIONS AND DISCLAIMERS SHALL APPLY
						TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, EVEN IF ANY
						REMEDY FAILS ITS ESSENTIAL PURPOSE.</p>
					<p>LICENSEE WARRANTS THAT IT SHALL HOLD IN THE STRICTEST CONFIDENCE
						THIS COMPUTER PROGRAM AND ANY RELATED MATERIALS OR INFORMATION
						INCLUDING, BUT NOT LIMITED TO, ANY TECHNICAL DATA, RESEARCH,
						PRODUCT PLANS OR KNOW-HOW PROVIDED BY SOURCETRACE TO LICENSEE,
						EITHER DIRECTLY OR INDIRECTLY IN WRITING, ORALLY OR BY INSPECTION
						OF MATERIALS, INCLUDING THE SOFTWARE, RELATED LICENSED MATERIALS,
						AND OTHER APPLICATION SOFTWARE, SYSTEMS, OR MATERIALS, PROVIDED BY
						SOURCETRACE ('SOURCETRACES CONFIDENTIAL INFORMATION'). LICENSEE
						WARRANTS THAT IT SHALL NOT DISCLOSE ANY OF SOURCETRACES
						CONFIDENTIAL INFORMATION TO THIRD PARTIES AND LICENSEE WARRANTS
						THAT IT SHALL TAKE REASONABLE MEASURES TO PROTECT THE SECRECY OF
						AND AVOID DISCLOSURE AND UNAUTHORIZED USE OF SOURCETRACES
						CONFIDENTIAL INFORMATION. LICENSEE SHALL IMMEDIATELY NOTIFY
						SOURCETRACE IN THE EVENT OF ANY UNAUTHORIZED OR SUSPECTED USE OR
						DISCLOSURE OF SOURCETRACES CONFIDENTIAL INFORMATION.</p>
					<p>SourceTrace DATAGREEN Copyright ©2020 SourceTrace.</p>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary btn-sts" data-dismiss="modal">Dismiss</button>
				</div>
			</div>
		</div>
	</div> -->
</div>
</div>
</div>
</div>

	<script type="text/javascript">
		var currentTenantId = '<s:property value="#session.tenantId" />';

		function resetPassword() {
		}

		function validateUser() {
			var hit = true;
			var nameVal = /^[a-zA-Z0-9 ]+$/;
			var mailVal = /\S+@\S+\.\S+/;
			var userName = $('#name').val();
			var userValue = $('input[name="credentialName"]:checked').val();
			if (userName === "" || userName === null || userName === undefined) {
				alert('<s:text name="emptyName"/>');
				hit = false;
			} else if (userValue === "" || userValue === null || userValue==undefined
					|| userName === userValue) {
				alert('<s:text name="selectCredType"/>');
				hit = false;
			} else if (userValue === "1" && !nameVal.test(userName)) {
				alert('<s:text name="enterValidUserName"/>');
				hit = false;
			} else if (userValue === "2" && !mailVal.test(userName)) {
				alert('<s:text name="enterValidMail"/>');
				hit = false;
			}

			if (hit) {
				resetCredential();
				$.post("email_validateUser", {
					resetValue : userName,
					userValue : userValue,
					dt : new Date()
				}, function(data) {
					if (data === "" || data === null || data === undefined) {
						alert('<s:text name="errorWhileProcessing"/>');
					} else {
						alert(data);
					}
					$('#btnLogin').prop("disabled", true);
					document.loginform.action = "login_execute";
					document.loginform.submit();
				});
			}

			 else{
			            	  $('#name').val('');
			            }
		}
		function resetPopup() {
			document.loginform.action = '';
			$('#btnLogin').prop("disabled", true);
			document.loginform.submit();
		}
		function resetCredential() {
			$('#name').val('');
			$("input[name='credentialName']").attr("checked", false);
		}

		function checkClick() {
			$("#check").hide();
		}

		function onCreate() {
			var error="";
			
			var userName = document.getElementById('userName').value;
			var password = document.getElementById('password').value;
			//$("#actionError").html(" ");
			if ((userName === "" || userName === null)
					&& (password === "" || password === null)) {
				 $("#validateError").removeClass('hide');
			
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('userName.password.reqd')}" />';
				return false;
			} else if (userName === "" || userName === null) {
				 $("#validateError").removeClass('hide');		
			
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('username.reqd')}" />';
				return false;
			} else if (password === "" || password === null) {
				 $("#validateError").removeClass('hide');
			
				document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('password.reqd')}" />';
				return false;
			} else {
				document.loginform.action = "j_spring_security_check";
				if (document.getElementById("agree").checked) {
					$("#check").hide();
					$('#btnLogin').prop("disabled", true);
					document.loginform.submit();
				} else {
					//$("#check").show();
					//$('#actionError').removeClass('no-display');
					 $("#validateError").removeClass('hide');
					document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('not.agree')}" />';
				    return false;
					/* $("#check").removeClass('hide');
					$("#check").removeAttr('style');
					$('.errorMessage').hide(); */
				}
			}

			if (typeof (Storage) !== "undefined") {
				localStorage.cachedBranch = $('#branch').val();
			}
		}

		$(document).ready(
				function() {
					resetCredential();
					$("#check").hide();
					$("#username").focus();
				//	$('#validateError').hide();
					/* // alert(currentTenantId);
					if (currentTenantId == null
							|| currentTenantId == 'undefined'
							|| currentTenantId == '') {
						if (localStorage.currentTenantId
								&& localStorage.currentTenantId != '') {
							if (localStorage.currentTenantId !== 'agro') {
								location.replace("login_execute_"
										+ localStorage.currentTenantId);
							}
						}
					} else {
						if (typeof (Storage) !== "undefined") {
							if (!localStorage.currentTenantId) {
								localStorage.currentTenantId = '';
							}
							localStorage.currentTenantId = currentTenantId;
						}
					}

					if (typeof (Storage) !== "undefined") {
						if (localStorage.cachedBranch) {
							if (localStorage.cachedBranch != null
									&& localStorage.cachedBranch != '') {
								$('#branch').val(localStorage.cachedBranch);
							}
						}
					} */
				});
	</script>
</body>