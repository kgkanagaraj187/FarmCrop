<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<s:hidden key="farmerTraining.id" id="farmerTrainingId" />
<font color="red"> <s:actionerror /></font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="farmerTraining.id" />
	<s:hidden key="command" />

	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					
				<h2>
					<s:text name="info.farmerTrainingDetails" />
				</h2>
				<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:property value="%{getLocaleProperty('farmerTrainingSelection.code')}" />
					</p>
					<p class="flexItem">
						<s:property value="farmerTraining.code"/>
					</p>
				</div>
				<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:property value="%{getLocaleProperty('farmerTrainingSelection.status')}" />
					</p>
					<p class="flexItem">
					<s:if test="farmerTraining.status!='-1'">
						<s:text name='%{getLocaleProperty("statusValue"+farmerTraining.status)}' />
					</s:if>	
				
					</p>
				</div>
				<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:property value="%{getLocaleProperty('trainingTopic')}" />
					</p>
					<p class="flexItem">
						<s:property value="farmerTraining.trainingTopic.name" />
					</p>
				</div>
				
				<div class="dynamic-flexItem" id="tSelectType">
					<p class="flexItem">
						<s:text name="farmerTrainingSelection.type" />
					</p>
					<p class="flexItem">
					<s:if test='farmerTraining.selectionType!="-1"'>
						<s:property value="farmerTraining.selectionType"/>
					</s:if>
					</p>
				</div>
				
				<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:text name="topic.list" />
					</p>
					<p class="flexItem">
						<s:property value="farmerTraining.trainingTopicActivitySet" />
					</p>
				</div>
			
			<%-- 	<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:text name="targetGroup.name" />
					</p>
					<p class="flexItem">
						<s:property value="farmerTraining.targetGroupSet" />
					</p>
				</div>
				 --%>
				<div class="dynamic-flexItem">
					<p class="flexItem">
						<s:property value="%{getLocaleProperty('trainingMethod')}" />
					</p>
					<p class="flexItem">
						<s:property value="farmerTraining.trainingMethodSet" />
					</p>
				</div>
				<s:if test='farmerTraining.status=="1"||farmerTraining.status=="2"'>
					<div class="dynamic-flexItem">
					<p class="flexItem">
							<s:property value="%{getLocaleProperty('farmerTrainingSelection.status')}" />
					</p>
					<p class="flexItem">
						<s:text name='%{getLocaleProperty("statusValue"+farmerTraining.status)}' />
					</p>
				</div>
				</s:if>
			</div>
			<div class="yui-skin-sam">
<sec:authorize ifAllGranted="service.farmerTrainingSelection.update">
        <span id="update" class=""><span class="first-child">
                <button type="button" class="edit-btn btn btn-success">
                    <FONT color="#FFFFFF">
                    <b><s:text name="edit.button" /></b>
                    </font>
                </button>
            </span></span>
            </sec:authorize>
<sec:authorize ifAllGranted="service.farmerTrainingSelection.delete">
             <span id="delete" class=""><span class="first-child">
                <button type="button" class="delete-btn btn btn-warning">
                    <FONT color="#FFFFFF">
                    <b><s:text name="delete.button" /></b>
                    </font>
                </button>
            </span></span>
</sec:authorize>
<span id="cancel" class="">
<span class="first-child"><button type="button" class="back-btn btn btn-sts">
               <b><FONT color="#FFFFFF"><s:text name="back.button"/>
                </font></b></button>
</span></span> 
</div>
		</div>
	</div>
</div>	

	
</s:form>
<s:form name="updateform" action="farmerTrainingSelection_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="deleteform" action="farmerTrainingSelection_delete.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
<s:form name="cancelform" action="farmerTrainingSelection_list.action">
	<s:hidden key="currentPage" />
</s:form>
<script type="text/javascript">  

$(document).ready(function () {
	var tenant='<s:property value="getCurrentTenantId()"/>';
	if(tenant=='wilmar'){
	$("#tSelectType").addClass("hide");
	}
	else{
		$("#tSelectType").removeClass("hide");
	}
    $('#update').on('click', function (e) {
        document.updateform.id.value = document.getElementById('farmerTrainingId').value;
        document.updateform.currentPage.value = document.form.currentPage.value;
        document.updateform.submit();
    });

    $('#delete').on('click', function (e) {
        if (confirm('<s:text name="confirm.delete"/> ')) {
            document.deleteform.id.value = document.getElementById('farmerTrainingId').value;
            document.deleteform.currentPage.value = document.form.currentPage.value;
            document.deleteform.submit();
        }
    });
});


</script>
