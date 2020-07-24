<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<s:hidden key="cropCalendar.id" id="cropCalendarId" />
	<font color="red"> <s:actionerror /></font>
	<s:form name="form" cssClass="fillform">


		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div align="right">
							<b><s:text name="season" /> : </b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>

						<div class="formContainerWrapper dynamic-form-con">

							<h2>
								<s:property
							value="%{getLocaleProperty('info.CropCalendar')}" />
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(cropCalendar.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
								<s:property
							value="%{getLocaleProperty('calendarDate')}" />
								</p>
								<p class="flexItem">
									<s:date name="cropCalendar.createdDate" format="dd/MM/yyyy" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
							<s:property
							value="%{getLocaleProperty('calendarName')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropCalendar.name" />
									
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
								<s:property
							value="%{getLocaleProperty('crop')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropCalendar.procurementProduct.code" />
									-
									<s:property value="cropCalendar.procurementProduct.name" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property
							value="%{getLocaleProperty('variety')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropCalendar.procurementVariety.code" />
									-
									<s:property value="cropCalendar.procurementVariety.name" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
								<s:property
							value="%{getLocaleProperty('activityType.name')}" />
								</p>
								<p class="flexItem">
								<s:if test="cropCalendar.activityType==0">
													<s:text name="Main Crop"></s:text>
												</s:if> <s:elseif test="cropCalendar.activityType==1">
													<s:text name="Inter Crop"></s:text>
												</s:elseif> <s:else>
													<s:text name="Border"></s:text>
												</s:else>
								
									
								</p>
							</div>
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property
							value="%{getLocaleProperty('seasonCode')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropCalendar.seasonCode" />
								</p>
							</div>
							
							
							
						</div>
						<div class="formContainerWrapper dynamic-form-con">
							<s:if test="cropCalendar.cropCalendarDetail.size>0">
								<h2>
								<s:property
							value="%{getLocaleProperty('info.cropCalendarDetails')}" />
								</h2>
								<table class="table table-bordered aspect-detail">

									<tr class="odd">
										<th><b><s:property	value="%{getLocaleProperty('activityMethodName')}" /></b></th>
										<th><b><s:property	value="%{getLocaleProperty('noOfDays')}" /></b></th>
										
									</tr>

									<s:iterator value="cropCalendar.cropCalendarDetail">
										<tr>											
											<td><s:property value="activityMethod" /></td>
											<td><s:property value="noOfDays" /></td>
										
										</tr>
									</s:iterator>
								</table>
							</s:if>
						</div>
						<div class="yui-skin-sam">

							<span id="update" class=""><span class="first-child"><button
										type="button" onclick="onUpdate();"
										class="edit-btn btn btn-success">
										<b><FONT color="#FFFFFF"><s:text name="edit.button" />

										</font></b>
									</button></span></span> <span id="delete" class=""><span class="first-child"><button
										type="button" onclick="onDelete();"
										class="delete-btn btn btn-warning">
										<b><FONT color="#FFFFFF"><s:text
													name="delete.button" /> </font></b>
									</button></span></span> <span id="cancel" class=""><span class="first-child"><button
										type="button" id="back" onclick="onCancel();"
										class="back-btn btn btn-sts">

										<b><FONT color="#FFFFFF"><s:text name="back.button" />
										</font></b>
									</button></span></span>
						</div>

					</div>
				</div>
			</div>
		</div>



	</s:form>

	<s:form name="updateform" action="cropCalendar_update.action">
	<s:hidden id="id" name="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="deleteform" action="cropCalendar_delete.action">
		<s:hidden name="id" value="%{id}" />
		<s:hidden key="currentPage" />
	</s:form>
	<s:form name="listForm" id="listForm" action="cropCalendar_list.action">
		<s:hidden name="currentPage" />
	</s:form>




	<script type="text/javascript">
		function onUpdate() {
			document.updateform.submit();
		}

		function onDelete() {

			var val = confirm('<s:text name="confirm.delete"/>');
			if (val)
				document.deleteform.submit();
		}

		function onCancel() {
			document.listForm.submit();
		}
	</script>



</body>