<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="gramPanchayat.id" id="gramPanchayatId"/>
<font color="red"><s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="id" />
	<s:hidden key="gramPanchayat.id"/>
	<s:hidden key="command" />
	<s:hidden key="currentPage"/>
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:property value="%{getLocaleProperty('info.grampanchayat')}" /></th>
		</tr>
		
		
		<tr class="odd">
 	 	<td><s:text name="country.name"/></td>
 		 <td ><s:property value="gramPanchayat.city.locality.state.country.code"/>&nbsp;-&nbsp;<s:property value="gramPanchayat.city.locality.state.country.name"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('state.name')}"/></td>
 		 <td ><s:property value="gramPanchayat.city.locality.state.code"/>&nbsp;-&nbsp;<s:property value="gramPanchayat.city.locality.state.name"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('locality.name')}"/></td>
 		 <td ><s:property value="gramPanchayat.city.locality.code"/>&nbsp;-&nbsp;<s:property value="gramPanchayat.city.locality.name"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
 	 	<td><s:property value="%{getLocaleProperty('city.name')}" /></td>
 		 <td ><s:property value="gramPanchayat.city.code"/>&nbsp;-&nbsp;<s:property value="gramPanchayat.city.name"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:property value="%{getLocaleProperty('grampanchayat.code')}" /></td>
			<td width="65%"><s:property  value="gramPanchayat.code"/>&nbsp;</td>
		</tr>
		
		
		<tr class="odd">
			<td width="35%"><s:property value="%{getLocaleProperty('grampanchayat.name')}" /></td>
			<td width="65%"><s:property  value="gramPanchayat.name"/>&nbsp;</td>
		</tr>
		
	</table>
	<br />
<div class="yui-skin-sam">
    <sec:authorize ifAllGranted="profile.location.gramPanchayat.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
    </sec:authorize>
    <sec:authorize ifAllGranted="profile.location.gramPanchayat.delete">
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="delete-btn btn btn-warning">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span></sec:authorize>
  <span id="cancel" class=""><span class="first-child"><button type="button"  class="back-btn btn btn-sts">
               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button></span></span>
</div>
</s:form>

<s:form name="updateform" action="gramPanchayat_update.action">
     <s:hidden key="id"/>
     <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="gramPanchayat_delete.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="cancelform" action="gramPanchayat_list.action">
    <s:hidden key="currentPage"/>
</s:form>


<script type="text/javascript">  



$(document).ready(function () {
    $('#update').on('click', function (e) {
    	document.updateform.id.value = document.getElementById('gramPanchayatId').value;
		 document.updateform.currentPage.value = document.form.currentPage.value;
		 document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
    	if(confirm( '<s:text name="confirm.delete"/> ')){
			 document.deleteform.id.value = document.getElementById('gramPanchayatId').value;
			 document.deleteform.currentPage.value = document.form.currentPage.value;
			document.deleteform.submit();
		}
    });
});



</script>