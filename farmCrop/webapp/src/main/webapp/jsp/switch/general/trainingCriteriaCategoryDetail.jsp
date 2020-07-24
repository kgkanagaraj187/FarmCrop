<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="trainingCriteriaCategory.id" id="trainingCriteriaCategoryId"/>
<font color="red">
    <s:actionerror/></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="trainingCriteriaCategory.id"/>
	<s:hidden key="command" />
	<table class="table table-bordered aspect-detail">
		<tr>
			<th colspan="2"><s:text name="info.trainingCriteriaCategory" /></th>
		</tr>
		
		<s:if test='branchId==null'>
			<tr class="odd">
				<td width="35%"><s:text name="app.branch" /></td>
				<td width="65%"><s:property value="%{getBranchName(trainingCriteriaCategory.branchId)}" />&nbsp;</td>
			</tr>
		</s:if>
		
		<tr class="odd">
			<td width="35%"><s:text name="trainingCriteriaCategory.code" /></td>
			<td width="65%"><s:property value="trainingCriteriaCategory.code"/>&nbsp;</td>
		</tr>
		
		<tr class="odd">
			<td width="35%"><s:text name="trainingCriteriaCategory.name" /></td>
			 <div style="width:430px;" class="break-word">
			<td width="65%"><s:property value="trainingCriteriaCategory.name" />&nbsp;</td>
			</div>
		</tr>
		
	</table>
	<br />
<div class="yui-skin-sam">
    <sec:authorize ifAllGranted="profile.trainingMaster.topicCriteriaCategory.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
    </sec:authorize> <sec:authorize ifAllGranted="profile.trainingMaster.topicCriteriaCategory.delete">
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

<s:form name="updateform" action="trainingCriteriaCategory_update.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="trainingCriteriaCategory_delete.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="cancelform" action="trainingCriteriaCategory_list.action">
    <s:hidden key="currentPage"/>
</s:form>


<script type="text/javascript">  
$(document).ready(function () {
    $('#update').on('click', function (e) {
        document.updateform.id.value = document.getElementById('trainingCriteriaCategoryId').value;
        document.updateform.currentPage.value = document.form.currentPage.value;
        document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
        if (confirm('<s:text name="confirm.delete"/> ')) {
            document.deleteform.id.value = document.getElementById('trainingCriteriaCategoryId').value;
            document.deleteform.currentPage.value = document.form.currentPage.value;
            document.deleteform.submit();
        }
    });
});

</script>