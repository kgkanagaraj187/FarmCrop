<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="locality.id" id="localityId"/>
<s:hidden key="locality.name"/>
<font color="red">
    <s:actionerror/>
</font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="id" />
	<s:hidden key="currentPage"/>
	<s:hidden key="locality.id"/>
	<s:hidden key="command" />
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:property value="%{getLocaleProperty('info.locality')}" /></th>
		</tr>
		<s:if test='branchId==null'>
			<tr class="odd">
				<td width="35%"><s:text name="app.branch" /></td>
				<td width="65%"><s:property value="%{getBranchName(village.branchId)}" />&nbsp;</td>
			</tr>
		</s:if>
		<tr class="odd">
 	 	<td><s:text name="country.name"/></td>
 		 <td ><s:property value="countryCode"/>&nbsp;-&nbsp;<s:property value="countryName"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('state.name')}"/></td>
 		 <td ><s:property value="locality.state.code"/>&nbsp;-&nbsp;<s:property value="locality.state.name"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="locality.name" /></td>
			<td width="65%"><s:property  value="locality.name"/>&nbsp;</td>
		</tr>
		<tr class="odd">
			<td width="35%"><s:text name="locality.code" /></td>
			<td width="65%"><s:property  value="locality.code"/>&nbsp;</td>
		</tr>
	</table>
	<br />
<div class="yui-skin-sam">
    <sec:authorize ifAllGranted="profile.location.locality.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
    </sec:authorize><sec:authorize ifAllGranted="profile.location.locality.delete">
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="delete-btn btn btn-warning">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span></sec:authorize>
  			<span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts">
               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button></span></span>
</div>
</s:form>

<s:form name="updateform" action="locality_update.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="locality_delete.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="cancelform" action="locality_list.action">
    <s:hidden key="currentPage"/>
</s:form>
<script type="text/javascript">  
document.cancelform.currentPage.value = document.form.currentPage.value;

$(document).ready(function () {
    $('#update').on('click', function (e) {
    	document.updateform.id.value = document.getElementById('localityId').value;
		 document.updateform.currentPage.value = document.form.currentPage.value;
		document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
    	if(confirm( '<s:text name="confirm.delete"/> ')){
			 document.deleteform.id.value = document.getElementById('localityId').value;
			 document.deleteform.currentPage.value = document.form.currentPage.value;
			document.deleteform.submit();
		}
    });
});
</script>