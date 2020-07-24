<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<style type="text/css">
.none{
	display: none;
}
</style>
<font color="red"> <s:actionerror /> </font>
<div class="error"><s:fielderror /></div>
<s:hidden key="user.id" id="userId"/>
<s:form name="form" cssClass="fillform"  enctype="multipart/form-data">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="user.id"/>
	<s:hidden key="command" />
	
   <div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name="info.user" /></h2>
					 <s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(vendor.branchId)}" /></p>
					</div>
				</s:if>	
					<div class="dynamic-flexItem">
						<p class="flexItem"> <s:text name="userProfile.username" /></p>
						<p class="flexItem"><s:property value="user.username"/></p>
					</div>
					
			</div>
			
			<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name="info.personal" /></h2>
					<div class="dynamic-flexItem">
						<s:if test='userImageString!=null && userImageString!=""'>
							<img width="50" height="50" border="0"
								src="data:image/png;base64,<s:property value="userImageString"/>">
						</s:if>
						<s:else>
							<img align="middle" width="150" height="100" border="0"
								src="img/no-image.png">
						</s:else>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.firstName" /></p>
						<p class="flexItem"><s:property value="user.personalInfo.firstName"/></p>
					</div>
					
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="personalInfo.lastName" /></p>
						<p class="flexItem"><s:property value="user.personalInfo.lastName"/></p>
					</div>
					
				
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="user.lang" /></p>
						<p class="flexItem"><s:text name='%{user.language}'/></p>
					</div>
			</div>
			<div class="formContainerWrapper dynamic-form-con">
			<h2><s:text name="info.contact" /></h2>
				
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.address" /></p>
						<p class="flexItem"><s:property value="user.contactInfo.address1"/></p>
				</div>
				
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.phoneNumber" /></p>
						<p class="flexItem"><s:property value="user.contactInfo.phoneNumber"/></p>
				</div>
				
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.mobileNumber" /></p>
						<p class="flexItem"><s:property value="user.contactInfo.mobileNumber"/></p>
				</div>
				
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="contactInfo.email" /></p>
						<p class="flexItem"><s:property value="user.contactInfo.email"/></p>
				</div>
				
				
			</div>
			<div class="formContainerWrapper dynamic-form-con">
				<h2><s:text name="info.userCred"/></h2>
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="user.role" /></p>
						<p class="flexItem"><s:property value="roleName"/></p>
				</div>
				<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="user.status" /></p>
						<p class="flexItem"><s:property value="status"/></p>
				</div>
			</div>	
			<div class="yui-skin-sam">
    <sec:authorize ifAllGranted="profile.user.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
    </sec:authorize><sec:authorize ifAllGranted="profile.user.delete">
    <s:if test='!"exec".equalsIgnoreCase(user.getUsername())'>
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="delete-btn btn btn-sts">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span>
            </s:if>
            </sec:authorize>
  			<span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts">
               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
               </font></b></button></span></span>
</div>
		</div>
	</div>
</div>
</div>

					

<br/>
	

</s:form>

<s:form name="updateform" action="user_update.action">
    <s:hidden key="id"/>
     <s:hidden key="currentPage"/>
</s:form>

<s:form name="deleteform" action="user_delete.action">
    <s:hidden key="id"/>
     <s:hidden key="currentPage"/>
</s:form>

<s:form name="cancelform" action="user_list.action">
    <s:hidden key="currentPage"/>
</s:form>


<script type="text/javascript">  
$(document).ready(function () {
    $('#update').on('click', function (e) {
    	document.updateform.id.value = document.getElementById('userId').value;
		 document.updateform.currentPage.value = document.form.currentPage.value;
		document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
    	 if(confirm( '<s:text name="confirm.delete"/> ')){
			 document.deleteform.id.value = document.getElementById('userId').value;
			 document.deleteform.currentPage.value = document.form.currentPage.value;
			document.deleteform.submit();
		}
    });
});

</script>