<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
    <!-- add this meta information to select layout  -->
    <meta name="decorator" content="swithlayout">
</head>
<font color="red">
<s:actionerror/></font>
<s:form name="form" cssClass="fillform">
    <s:hidden key="currentPage"/>
    <s:hidden key="id" />
    <s:hidden key="farmCatalogue.id" id="catalogueId"/>
    <s:hidden key="command" />
    
    <div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.catalogue')}" /></h2>
					 <s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(farmCatalogue.branchId)}" /></p>
					</div>
				</s:if>	
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="catalogue.code" /></p>
						<p class="flexItem"><s:property value="farmCatalogue.code" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="catalogue.typez" /></p>
						<p class="flexItem"><s:property value="cataLogueMasterName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="catalogue.name" /></p>
						<p class="flexItem"><s:property value="farmCatalogue.name" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="status" /></p>
						<p class="flexItem"><s:property value="status" /></p>
					</div>
					
					
				</div>
				
				<div class="appContentWrapper marginBottom">
			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.language')}" />
				</h2>
		<table class="table">
		<tr>
			<th><s:text name="lang.code" /></th>
			<th><s:text name="lang.name" /></th>
			<th><s:text name="lang.info" /></th>
		</tr>
		<s:iterator value="farmCatalogue.languagePreferences" status="stat"
			var="langPref">
			<tr class="odd">
				<td width="20%"><s:property value="%{#langPref.lang}" /></td>
				<td width="40%"><s:property value="%{#langPref.name}" /></td>
				<td width="40%"><s:property value="%{#langPref.info}" /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
	</div>
								    <div class="yui-skin-sam">
        <sec:authorize ifAllGranted="profile.catalogue.update">
            <span id="update" class=""><span class="first-child">
                    <button type="button" class="edit-btn btn btn-success" >
                        <FONT color="#FFFFFF">
                        <b><s:text name="edit.button" /></b>
                        </font>
                    </button>
                </span></span>
            </sec:authorize> 
         
            <span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts">
                        <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                        </font></b></button></span></span> 
    </div>
			</div>
	
		
		</div>
		
	</div>
	
</div>

				
   

</s:form>

<s:form name="updateform" action="catalogue_update.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="catalogue_delete.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="cancelform" action="catalogue_list.action?survey=1">
    <s:hidden key="currentPage"/>
</s:form>


<script>
    jQuery(document).ready(function () {
        $('#update').on('click', function (e) {
            document.updateform.id.value = document.getElementById('catalogueId').value;
            document.updateform.currentPage.value = document.form.currentPage.value;
            document.updateform.submit();
        });

        $('#delete').on('click', function (e) {
            if (confirm('<s:text name="confirm.delete"/> ')) {
                document.deleteform.id.value = document.getElementById('catalogueId').value;
                document.deleteform.currentPage.value = document.form.currentPage.value;
                document.deleteform.submit();
            }
        });
    });

</script>