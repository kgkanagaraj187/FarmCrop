<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<font color="red"> <s:actionerror />
</font>
<div class="error">
	<s:fielderror />
</div>
<s:hidden key="agent.id" id="id" />
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="agent.id" />
	<s:hidden key="type" id="type" />
	<s:hidden key="command" />
	
	
<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name='%{"info"+type}' /></h2>
					<s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property
								value="%{getBranchName(agent.branchId)}" /></p>
					</div>
					</s:if>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name='%{"agentName"+type}' /></p>
						<p class="flexItem"><s:property value="agent.profileId" /></p>
					</div>
					<s:if test="agent.agentType.code!=05 && agent.agentType.code!=04">
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="%{getLocaleProperty('agent.samithi')}" /></p>
						<p class="flexItem"><s:property value="warehouseNames" /></p>
					</div>
					</s:if>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.firstName" /></p>
						<p class="flexItem"><s:property
							value="agent.personalInfo.firstName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.lastName" /></p>
						<p class="flexItem"><s:property
							value="agent.personalInfo.lastName" /></p>
					</div>
			<s:if test="currentTenantId=='pratibha' || currentTenantId=='livelihood'">
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agent.agentType" /></p>
						<p class="flexItem"><s:text
							name='%{selectedAgentType}' /></p>
					</div>
			</s:if>
					<s:if test="currentTenantId=='chetna'">
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agent.agentType" /></p>
						<p class="flexItem"><s:text
							name='%{selectedAgentType}' /></p>
					</div>
			
					<div class="dynamic-flexItem">
					<s:if test="agent.agentType.code==03">
						<p class="flexItem"><s:property
								value="%{getLocaleProperty('procurementCenter')}" /></p>
						</s:if><s:elseif test="agent.agentType.code==04">
						
						<p class="flexItem"><s:property
								value="%{getLocaleProperty('ginningCenter')}" /></p>
						
						</s:elseif>	<s:elseif test="agent.agentType.code==05">
							<p class="flexItem"><s:property
								value="%{getLocaleProperty('spinningCenter')}" /></p>
						
						</s:elseif>
								
						<p class="flexItem"><s:text
							name='%{selectedProcurementCenter}' /></p>
					</div>
					</s:if>
					<s:if test="currentTenantId=='pratibha' && agent.agentType.code!='06'">
					<div class="dynamic-flexItem">
					
						<p class="flexItem"><s:property
								value="%{getLocaleProperty('warehouse')}" /></p>
						<p class="flexItem"><s:text
							name='%{warehouseName}' /></p>
					</div>
					</s:if>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.sex" /></p>
						<p class="flexItem"><s:text name='%{agent.personalInfo.sex}' /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.identityType" /></p>
						<p class="flexItem"><s:text
							name='%{agent.personalInfo.identityType}' /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.identityNumber" /></p>
						<p class="flexItem"><s:property
							value="agent.personalInfo.identityNumber" /></p>
					</div>

						<s:if test="currentTenantId=='ocp'">
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property
								value="%{getLocaleProperty('agent.photo')}" />
								</p>
								<p class="flexItem">
									<button type='button' class='btn btn-sm pull-right photo'
										style='margin-right: 15%'
										onclick="enableFarmerPhotoModal(<s:property value="agent.id"/>,1)">
										<i class='fa fa-picture-o' aria-hidden='true'></i>
									</button>
								</p>
							</div>
						</s:if>


						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.dateOfBirth" /></p>
						<p class="flexItem"><s:property value="dateOfBirth" /></p>
					</div>
					
						<h2><s:text name="info.contact" /></h2>
				
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.address" /></p>
						<p class="flexItem">	<s:property value="agent.contactInfo.address1" /></p>
					</div>
					<s:if test="currentTenantId!='ocp'">
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.phoneNumber" /></p>
						<p class="flexItem"><s:property value="agent.contactInfo.phoneNumber" /></p>
					</div>
				</s:if>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agentcontactInfo.mobileNumber" /></p>
						<p class="flexItem"><s:property
							value="agent.contactInfo.mobileNumber" /></p>
					</div>
					
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agentcontactInfo.email" /></p>
						<p class="flexItem"><s:property value="agent.contactInfo.email" /></p>
					</div>
					
					
				
					<h2><s:text name="info.credential" /></h2>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agent.status.name" /></p>
						<p class="flexItem"><s:text name='%{"agent"+agent.status}' /></p>
					</div>
					<%-- <div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="accountBalance" /></p>
						<p class="flexItem"><s:property value="agent.accountRupee" />.<s:property
							value="agent.accountPaise" /></p>
					</div> --%>
					
		<%-- 			
					<h2><s:property value="%{getLocaleProperty('info.training')}" /></h2>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="topic" /></p>
						<p class="flexItem"><s:property value="selectedtrainings" /></p>
					</div>
					 --%>
					
					<s:if test="card != null">
					<h2><s:text name="info.card" /></h2>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="agent.card.id" /></p>
						<p class="flexItem"><s:property value="card.cardId" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="status.card.name" /></p>
						<p class="flexItem"><s:text name='%{"card"+card.status}' /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="rewrite.card.name" /></p>
						<p class="flexItem"><s:text
								name='%{"cardRewrite"+card.cardRewritable}' /></p>
					</div>
				
					</s:if>
					
					
					<s:if test="type=='fieldStaff'">
					<s:if test="account != null">
						<h2><s:text name="info.account" /></h2>
						<div class="dynamic-flexItem">
							<p class="flexItem"><s:text name="agent.account.id" /></p>
							<p class="flexItem"><s:property value="account.accountNo" /></p>
						</div>
						<div class="dynamic-flexItem">
							<p class="flexItem"><s:text name="agent.account.type" /></p>
							<p class="flexItem"><s:text name='%{account.accountType}' /></p>
						</div>
						
						<div class="dynamic-flexItem">
							<p class="flexItem"><s:text name="Agentstatus.account.name" /></p>
							<p class="flexItem"><s:text name='%{"account"+account.status}' /></p>
						</div>
			
				</s:if>
				</s:if>
				</div>
			</div>
		</div>
	</div>
</div>
	
	
	
	<div class="yui-skin-sam">
		<sec:authorize ifAllGranted="profile.agent.update">
		
			<span id="update" class=""><span class="first-child">
					<button type="button" class="edit-btn btn btn-success">
						<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
						</font>
					</button>
			</span></span>

		</sec:authorize>
		<sec:authorize ifAllGranted="profile.agent.delete">
			<span id="delete" class=""><span class="first-child">
					<button type="button" class="delete-btn btn btn-warning">
						<FONT color="#FFFFFF"> <b><s:text name="delete.button" /></b>
						</font>
					</button>
			</span></span>

		</sec:authorize>
		<span id="cancel" class=""><span class="first-child"><button
					type="button" class="back-btn btn btn-sts">
					<b><FONT color="#FFFFFF"><s:text name="back.button" /> </font></b>
				</button></span></span>
		<s:set name="loginUserRole" value="%{loginUserRole}" />

		<s:if
			test='"exec".equals(loginUserName)|| #loginUserRole.equals("Admin")'>
			
		<%-- <s:if test='(agent.bodStatus==1)'>
			<span id="reset" class=""><span class="first-child">
			<button type="button" onclick="onBODReset();" class="ic-clear">
			<b><FONT color="#FFFFFF"><s:text name="reset.BOD" /></FONT></b></button>
			</span> </span>
		</s:if>
		<s:else>		
			<span id="reset" class=""><span class="first-child">
			<button type="button" onclick="onEODReset();" class="reset-btn">
			<b><FONT color="#FFFFFF"><s:text name="reset.EOD" /></FONT></b></button>
			</span> </span>
		</s:else> --%>
		
			<s:if test='agent.bodStatus!=0'>
			
			<span id="reset" class=""><span class="first-child"><button
					type="button"  onclick="onBODReset();" class="back-btn btn btn-sts">
					<b><FONT color="#FFFFFF"><s:text name="reset.BOD" /> </font></b>
				</button></span></span>
			
			
			
				
			</s:if>
		</s:if>
		<span id="idchange" class="" style="display: none"><span
			class="first-child">
				<button type="button" onclick="idSeqMigrationCal()"
					class="cancel-btn">
					<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
					</font></b>
				</button>
		</span></span>
	</div> 
</s:form>



<s:form name="updateform" action="%{type}_update?type=%{type}">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="%{type}_delete?type=%{type}">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="%{type}_list?type=%{type}">
	<s:hidden key="currentPage" />
</s:form>
<s:form name="migrationform" action="%{type}_updateIdSeq?type=%{type}">

</s:form>
<s:form name="txnStatusResetForm" id="txnStatusResetForm"
	action="%{type}_reset?type=%{type}">
	<s:hidden name="id" />
	<s:hidden name="currentPage" />
</s:form>
<script type="text/javascript">
$(document).ready(function () {
    $('#update').on('click', function (e) {
        document.updateform.id.value = document.getElementById('id').value;
        document.updateform.currentPage.value = document.form.currentPage.value;
        document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
        if (confirm('<s:text name="confirm.delete"/> ')) {
            document.deleteform.id.value = document.getElementById('id').value;
            document.deleteform.currentPage.value = document.form.currentPage.value;
            document.deleteform.submit();
        }
    });
});

	function onBODReset() {
		if (confirm('<s:text name="confirm.resetBOD"/>'))
			document.txnStatusResetForm.submit();
	}
	function onEODReset() {
		if (confirm('<s:text name="confirm.resetEOD"/>'))
			document.txnStatusResetForm.submit();
	}
	function idSeqMigrationCal() {
		document.migrationform.action = "%{type}_updateIdSeq?type=%{type}";
		document.migrationform.submit();
	}
	function enableFarmerPhotoModal(idArr,type) {
		try {
			$("#mbody").empty();
			var mbody = "";
			mbody = "";
			mbody = "<div class='item active'>";
			mbody += '<img src="fieldStaff_populateImage.action?id='
					+ idArr + '&type='+type+'"/>';
			mbody += "</div>";
			$("#mbody").append(mbody);
			
			document.getElementById("enablePhotoModal").click();
		} catch (e) {
			alert(e);
		}
	}
	function buttonDPhotoCancel() {
		document.getElementById("model-close-DPhoto-btn").click();
	}
</script>