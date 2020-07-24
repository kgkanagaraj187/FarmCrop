<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="swithlayout">
</head>
<body>
	<s:hidden key="filter.id" id="filterId" />
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="id" />
		<s:hidden key="filter.id" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr>

					<th colspan="6"><s:text name="info.trainingCompl" /></th>
				</tr>
				<tr>
					<td><strong><s:text name="%{getLocaleProperty('trainingName')}" /></strong></td>
					<%-- <td><s:property value="farmerTraining.trainingTopic.name" /></td> --%>
					<td><s:property value="trainingTopicName" /></td>
					<td><strong><s:text name="trainingAssistName" /></strong></td>
					<td><s:property value="filter.trainingAssistName" /></td>
				</tr>
				<tr>
					<td ><strong><s:text name="timeTakenForTraining" /></strong></td>
					<td><s:property value="filter.timeTakenForTraining" />&nbsp;(hours)</td>
					<td> <strong><s:text name="remark" /></strong></td>
					<td><s:property value="filter.remarks" /></td>
				</tr>
				<s:if test="currentTenantId=='pratibha'">	
				<tr>
				<td><strong><s:text name="%{getLocaleProperty('noFarmer')}" /></strong></td>
					<td><s:property value="farmerCount" /></td>
				</tr>
				</s:if>
				<s:if test="topicCategoryList.size()>0">
				<tr class="odd">
					<th ><s:text name="%{getLocaleProperty('categorycode')}" /></th>
					<th><s:text name="%{getLocaleProperty('categoryprinciple')}" /></th>
				</tr>
				<tr>
					<s:iterator value="topicCategoryList">
						<tr class="odd">
							<td><s:property value="code" /></td>
							<td><s:property value="name" /></td>
						</tr>
					</s:iterator>
				</tr>
				</s:if>
				<s:if test="topicList.size()>0">
				<tr class="odd">
					<th><s:text name="%{getLocaleProperty('category')}" /></th>
					<th><s:text name="%{getLocaleProperty('code')}" /></th>
					<th><s:text name="%{getLocaleProperty('principle')}" /></th>
					<th><s:text name="%{getLocaleProperty('des')}" /></th>
				</tr>
				<tr>
					<s:iterator value="topicList">
						<tr class="odd">
							<td><s:property value="topicCategory.name" /></td>
							<td><s:property value="code" /></td>
							<td><s:property value="principle" /></td>
							<td><s:property value="des" /></td>
						</tr>
					</s:iterator>
				</tr>
			</s:if>	
			</table>
		</div>
		<s:if test="farmersList.size()>0">
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="%{getLocaleProperty('farmerListattend')}" /></th>
				</tr>
				<tr class="odd">
					<%-- 		<th><s:text name="farmerCode"/> --%>
					
					<s:if test="currentTenantId=='pratibha'">
					<th><s:text name="farmerAgentName" />
						<s:if test="agentType=='02'">
						<th><s:text name="village" />
						</s:if>
					</s:if>
					<s:else>
					<th><s:text name="farmerName" />
					<th><s:text name="village" />
					</s:else>
					<s:if test="currentTenantId=='pratibha' && agentType!='06'">
					<s:if test="branchId =='organic'">
					<th><s:text name="LG" /></th>
					</s:if>
					<s:else>
					<th><s:text name="%{getLocaleProperty('samithi.name')}" /></th>
					</s:else>
					</s:if>
					<s:elseif test="currentTenantId!='pratibha'">
					<th><s:text name="%{getLocaleProperty('samithi.name')}" /></th>
					</s:elseif>
				</tr>
				<s:iterator value="farmersList">
					<tr class="odd">
						<%-- <td><s:property value="farmerCode"/></td> --%>
						<td><s:property value="farmerName" /></td>
						<s:if test="currentTenantId=='pratibha'">
						<s:if test="agentType=='02'">
						<td><s:property value="villageName" /></td>
						</s:if>
						</s:if>
						<s:else>
						<td><s:property value="villageName" /></td>
						</s:else>
						<s:if test="currentTenantId=='pratibha' && agentType!='06'">
							<td><s:property value="samithiName" /></td>
						</s:if><s:else>
					<td>	<s:property value="samithiName" /></td>
						</s:else>
					</tr>
				</s:iterator>
			</table>
		</div>
	</s:if>
	<div class="appContentWrapper marginBottom">


			<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="trainingMeterials" /></th>
				</tr>
				<s:if test='%{materialStr!=" "}'>
					<td><s:property value="materialStr" /></td>
				</s:if>
				<s:else>
					<tr>
						<td>--- -No Data Available- --</td>
					</tr>
				</s:else>
			</table>
		</div>

		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="trainingMathods" /></th>
				</tr>
				<s:if test='%{methodStr!=" "}'>
					<td><s:property value="methodStr" /></td>
				</s:if>
				<s:else>
					<tr>
						<td>--- -No Data Available- --</td>
					</tr>
				</s:else>

			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr>
					<th colspan="3"><s:text name="trainingObs" /></th>
				</tr>
				<s:if test='%{obsStr!=" "}'>
					<tr class="odd">
						<td><s:property value="obsStr" /></td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td>--- -No Data Available- --</td>
					</tr>
				</s:else>
			</table>
			
			<div class="yui-skin-sam">
			<%-- 	<sec:authorize ifAllGranted="profile.location.village.update">
				<span id="update" class=""><span class="first-child">
						<button type="button" class="edit-btn btn btn-success">
							<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
							</font>
						</button>
				</span></span>
			</sec:authorize>
			<sec:authorize ifAllGranted="profile.location.village.delete">
				<span id="delete" class=""><span class="first-child">
						<button type="button" class="delete-btn btn btn-warning">
							<FONT color="#FFFFFF"> <b><s:text name="delete.button" /></b>
							</font>
						</button>
				</span></span>
			</sec:authorize> --%>
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="back-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="back.button" />
						</font></b>
					</button></span></span>
		</div>
		</div>


		<br />
		
	</s:form>



	<s:form name="cancelform" action="trainingCompletionReport_list.action">
		<s:hidden key="currentPage" />
		<s:hidden name="filterMapReport" id="filterMapReport" />
		<s:hidden name="postdataReport" id="postdataReport" />
	</s:form>


	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var filterMapReport='<s:property value="filterMapReport"/>';
							var postdataReport= '<s:property value="postdataReport"/>';
							$('#update')
									.on(
											'click',
											function(e) {
												document.updateform.id.value = document
														.getElementById('villageId').value;
												document.updateform.currentPage.value = document.form.currentPage.value;
												document.updateform.submit();
											});

							$('#delete')
									.on(
											'click',
											function(e) {
												if (confirm('<s:text name="confirm.delete"/> ')) {
													document.deleteform.id.value = document
															.getElementById('villageId').value;
													document.deleteform.currentPage.value = document.form.currentPage.value;
													document.deleteform
															.submit();
												}
											});
							
						});
	</script>
</body>
</html>