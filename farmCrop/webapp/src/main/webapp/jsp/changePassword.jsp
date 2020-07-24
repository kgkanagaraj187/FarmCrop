<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<META name="decorator" content="swithlayout">
		<%@ include file="/jsp/common/form-assets.jsp" %>
		<style>
			.errorMessage {
				align: left;
				margin: 0 auto;
				padding: 0 auto;
				color: red;
				list-style: none;
			}
		</style>
		
	</head>
	<body>
	<div class="error"><s:actionerror /><s:fielderror />
<sup>*</sup>
<s:text name="reqd.field" /></div>
<div class="errorMsg" style="color:red"></div>

			<s:form id="form" name="form" cssClass="form" action="changePassword_update.action" method="post" onsubmit="return false;">
				<s:hidden name="action" value="update"/>
				<s:hidden name="user.username" value="%{username}"/>
				<table class="table table-bordered aspect-detail">
		
					<tr class="odd">
						<td width="35%"><s:text name="changePassword.user.oldPassword"/><sup
							style="color: red;">*</sup></td>
						<td width="65%"><s:password name="user.oldPassword" theme="simple"  maxlength="45" id="oldPassword"/>
					    </td>
					</tr>
		
					<tr class="odd">
						<td width="35%"><s:text name="changePassword.user.password"/><sup
							style="color: red;">*</sup></td>
						<td width="65%"><s:password name="user.password" theme="simple"  maxlength="45" id="newPassword"/>
					   
					</tr>
					
					<tr class="odd">
						<td width="35%"><s:text name="changePassword.user.confirmPassword"/><sup
							style="color: red;">*</sup></td>
						<td width="65%"><s:password name="user.confirmPassword" theme="simple"  maxlength="45" id="confirmPassword"/>
					 
					</tr>
		
				</table>
				
					<br />

				<div class="yui-skin-sam">
					<span id="button" class=""><span class="first-child">
							<button  type="button" class="save-btn btn btn-success">
								<font color="#FFFFFF"> <b><s:text name="button.save" /></b>
								</font>
							</button>
					</span></span> <span id="cancel" class=""><span
						class="first-child"><button type="button" class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="button.cancel" />
								</font></b>
							</button></span></span>
				</div>

	</s:form>
	
		<s:form id="cancelForm" action="home_list">
		</s:form>
		<script>
			jQuery(document).ready(function(){
				jQuery(".save-btn").click(function(){
					var oldPassword=jQuery("#oldPassword").val();
					var newPassword=jQuery("#newPassword").val();
					var confirmPassword=jQuery("#confirmPassword").val();
					jQuery(".errorMsg").text('');
					$(".errorMessage").empty();
					if(oldPassword==null||oldPassword.trim()==''){
						jQuery(".errorMsg").text('<s:text name="empty.oldPassword"/>');
					}
					else if(newPassword==null||newPassword.trim()==''){
						jQuery(".errorMsg").text('<s:text name="empty.newPassword"/>');
					}
					else if(confirmPassword==null||confirmPassword.trim()==''){
						jQuery(".errorMsg").text('<s:text name="empty.confirmPassword"/>');
					}
					else if(confirm('<s:text name="changePassword.confirm.action"/> '+'\n'+'<s:text name="changePassword.note1"/>'+'\n'+'<s:text name="changePassword.note2"/>')){
						document.getElementById('form').submit();
					}
				});	
				
				jQuery(".cancel-btn").click(function(){
					document.getElementById('cancelForm').submit();
				});	
			});
		</script>
	</body>
</html>
