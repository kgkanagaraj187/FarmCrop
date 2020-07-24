<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="dynamicFeildMenuConfig.id" id="id"/>
<s:hidden key="dynamicFeildMenuConfig.name"/>
<font color="red">
    <s:actionerror/>
</font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="dynamicFeildMenuConfig.id"/>
	<s:hidden key="command" />
	<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.menuConfig')}" /></h2>
					<%--  <s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(vendor.branchId)}" /></p>
					</div>
				</s:if>	 --%>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.name" /></p>
						<p class="flexItem"><s:property value="dynamicFeildMenuConfig.name" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.icon" /></p>
						<p class="flexItem"><s:property value="dynamicFeildMenuConfig.iconClass" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.txnType" /></p>
						<p class="flexItem"><s:property value="dynamicFeildMenuConfig.txnType" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.entity" /></p>
						<p class="flexItem">
						<s:text name='%{"entityType"+dynamicFeildMenuConfig.entity}' /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.isSeason" /></p>
						<p class="flexItem">
						<s:if test="dynamicFeildMenuConfig.isSeason==0">
						<s:text name="no"/>
						</s:if>
						<s:else>
						<s:text name="yes"/>
						</s:else>
						</p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="dynamicMenu.isSingleRecord" /></p>
						<p class="flexItem">
						<s:if test="dynamicFeildMenuConfig.isSingleRecord==0">
						<s:text name="no"/>
						</s:if>
						<s:else>
						<s:text name="yes"/>
						</s:else>
						</p>
					</div>
					
					
				</div>
				<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.section')}" />
				</h2>
		<table class="table">
		<tr>
			<th><s:text name="sec.code" /></th>
			<th><s:text name="sec.name" /></th>
			<th><s:text name="order" /></th>
		</tr>
		<s:iterator value="dynamicFeildMenuConfig.dynamicSectionConfigs" status="stat"
			var="sec">
			<tr class="odd">
				<td width="20%"><s:property value="%{#sec.section.sectionCode}" /></td>
				<td width="40%"><s:property value="%{#sec.section.sectionName}" /></td>
				<td width="40%"><s:property value="%{#sec.order}" /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
	
	<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.field')}" />
				</h2>
		<table class="table">
		<tr>
			<th><s:text name="field.code" /></th>
			<th><s:text name="field.name" /></th>
			<th><s:text name="order" /></th>
		</tr>
		<s:iterator value="dynamicFeildMenuConfig.dynamicFieldConfigs" status="stat"
			var="field">
			<tr class="odd">
				<td width="20%"><s:property value="%{#field.field.code}" /></td>
				<td width="40%"><s:property value="%{#field.field.componentName}" /></td>
				<td width="40%"><s:property value="%{#field.order}" /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
	
	
			
				
				<div class="yui-skin-sam">
				    <sec:authorize ifAllGranted="admin.menuCreationToolGrid.update">
				        <span id="update" class=""><span class="first-child">
				                <button type="button" class="edit-btn btn btn-success">
				                    <FONT color="#FFFFFF">
				                    <b><s:text name="edit.button" /></b>
				                    </font>
				                </button>
				            </span></span>
				    </sec:authorize><sec:authorize ifAllGranted="admin.menuCreationToolGrid.delete">
				             <span id="delete" class=""><span class="first-child">
				                <button type="button" class="delete-btn btn btn-warning">
				                    <FONT color="#FFFFFF">
				                    <b><s:text name="delete.button" /></b>
				                    </font>
				                </button>
				            </span></span></sec:authorize>
				   <span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts" >
				               <b><FONT color="#FFFFFF"><s:text name="back.button"/> 
				                </font></b></button></span></span>
				</div>
		</div>
	</div>
</div>
</div>
				
</s:form>

<s:form name="updateform" action="menuCreationToolGrid_update.action">
    <s:hidden key="id"/>
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="menuCreationToolGrid_delete.action">
    <s:hidden key="id"/>
   <s:hidden key="currentPage"/>
</s:form>

<s:form name="cancelform" action="menuCreationToolGrid_list.action">
    <s:hidden key="currentPage"/>
</s:form>

<script type="text/javascript">
    $(document).ready(function () {
        $('#update').on('click', function (e) {
            document.updateform.id.value = document.getElementById('id').value;
            document.updateform.submit();
        });

        $('#delete').on('click', function (e) {
            if (confirm('<s:text name="confirm.delete"/> ')) {
                document.deleteform.id.value = document.getElementById('id').value;
                document.deleteform.submit();
            }
        });
    });

</script>