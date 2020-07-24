<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>

<s:if test="dynamicFieldsList.size()>0">
	<s:iterator value="dynamicFieldsList" var="dynamicSection">
		<%-- <div class="appContentWrapper marginBottom farmerDynamicField <s:property value="sectionCode" />"
			id="dynamicInfo"> --%>
		<div class="appContentWrapper marginBottom farmerDynamicField"
			id="dynamicInfo">
			<div class="dynamicValidateError" id="dynamicValidateError"
				style="color: #ff0000"></div>

			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#<s:property value="sectionCode" />"
						class="accrodianTxt collapsed"> <s:hidden class="sectionId"
							value="%{#dynamicSection.sectionCode}" /> <s:property
							value="sectionName" />
					</a>
				</h2>
			</div>
			<div id="<s:property value="sectionCode" />"
				class="panel-collapse collapse in">
				<div class="flexform">
					<s:iterator value="%{#dynamicSection.dynamicFieldConfigs}"
						var="dynamicField">

						<div
							class="flexform-item <s:property value="%{#dynamicField.componentId}" />">
							<label> <s:property
									value="%{#dynamicField.componentName}" /> <s:if
									test="%{#dynamicField.isRequired==1}">
									<sup style="color: red;">*</sup>
								</s:if>
							</label>
							<div class="form-element">
								<s:if test='%{#dynamicField.componentType=="1"}'>

									<s:textfield class="text form-control"
										value="%{#dynamicField.dynamicValue}"
										id='%{#dynamicField.componentId}'
										name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />
									<s:hidden value="%{#dynamicField.isRequired}"
										class="isRequired" name='%{#dynamicField.componentName}' />

									<s:if test='%{#dynamicField.parentDepen.id!=""}'>
										<s:hidden value="%{#dynamicField.parentDepen.id}"
											id="%{#dynamicField.componentId}" class="dynamicDependIds"
											name='%{#dynamicField.componentName}' />

									</s:if>

								</s:if>

								<s:elseif test='%{#dynamicField.componentType=="2"}'>

									<s:if test="%{#dynamicField.componentName=='Ration Card Name'}">
										<s:radio id='%{#dynamicField.componentId}' class="radio"
											value="%{#dynamicField.dynamicValue}"
											name='%{#dynamicField.componentName}'
											list="#{0:'APL',1:'BPL'}" />

									</s:if>
									
									<s:elseif test="%{#dynamicField.componentName=='Migration'}">
									<s:radio id='%{#dynamicField.componentId}' class="radio"
											value="%{#dynamicField.dynamicValue}"
											name='%{#dynamicField.componentName}'
											list="#{0:'MALE',1:'FEMALE'}" />
									</s:elseif>
									
									<s:else>
										<s:radio id='%{#dynamicField.componentId}' class="radio"
											value="%{#dynamicField.dynamicValue}"
											name='%{#dynamicField.componentName}'
											list="#{0:'No',1:'Yes'}"
											onchange="getDynamicDependsId(%{#dynamicField.id},this.value)" />

									</s:else>

									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.isRequired}"
										class="isRequired" name='%{#dynamicField.componentName}' />

									<s:if test='%{#dynamicField.parentDepen.id!=""}'>
										<s:hidden value="%{#dynamicField.parentDepen.id}"
											id="%{#dynamicField.componentId}" class="dynamicDependIds"
											name='%{#dynamicField.componentName}' />

									</s:if>



								</s:elseif>


								<s:elseif test='%{#dynamicField.componentType=="3"}'>


									<s:textfield readonly="true" id='%{#dynamicField.componentId}'
										theme="simple" name='%{#dynamicField.componentName}'
										data-date-format="%{getGeneralDateFormat().toLowerCase()}"
										size="20" cssClass="txtDate date-picker form-control input-sm"
										value="%{#dynamicField.dynamicValue}" />


									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.isRequired}"
										class="isRequired" name='%{#dynamicField.componentName}' />


									<s:if test='%{#dynamicField.parentDepen.id!=""}'>
										<s:hidden value="%{#dynamicField.parentDepen.id}"
											id="%{#dynamicField.componentId}" class="dynamicDependIds"
											name='%{#dynamicField.componentName}' />

										<s:if test='%{#dynamicField.parentDepen.id!=""}'>
											<s:hidden value="%{#dynamicField.parentDepen.id}"
												id="%{#dynamicField.componentId}" class="dynamicDependIds"
												name='%{#dynamicField.componentName}' />

										</s:if>

									</s:if>

								</s:elseif>

								<s:elseif test='%{#dynamicField.componentType=="4"}'>
									<!-- Always  %{#dynamicField.dynamicValue} should be the class. we have used pop method to fetch value in jquery refer formDropDownComponent() for mor info-->
									<s:select id='%{#dynamicField.componentId}'
										cssClass="form-control dropDown %{#dynamicField.dynamicValue}"
										name='%{#dynamicField.componentName}'
										value="%{#dynamicField.dynamicValue}" list="{}" />

									<%-- 	<s:property value="%{#dynamicField.dynamicValue}"/> --%>

									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.isRequired}"
										class="isRequired" name='%{#dynamicField.componentName}' />

									<s:if test='%{#dynamicField.parentDepen.id!=""}'>
										<s:hidden value="%{#dynamicField.parentDepen.id}"
											id="%{#dynamicField.componentId}" class="dynamicDependIds"
											name='%{#dynamicField.componentName}' />

									</s:if>

								</s:elseif>

								<s:elseif test='%{#dynamicField.componentType=="6"}'>
									<s:checkbox id='%{#dynamicField.componentId}' class="checkBox"
										value="%{#dynamicField.dynamicValue}"
										name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.isRequired}"
										id='%{#dynamicField.componentName}' class="isRequired"
										name='%{#dynamicField.componentName}' />

									<s:if test='%{#dynamicField.parentDepen.id!=""}'>
										<s:hidden value="%{#dynamicField.parentDepen.id}"
											id="%{#dynamicField.componentId}" class="dynamicDependIds"
											name='%{#dynamicField.componentName}' />


										<s:if test='%{#dynamicField.parentDepen.id!=""}'>
											<s:hidden value="%{#dynamicField.parentDepen.id}"
												id="%{#dynamicField.componentId}" class="dynamicDependIds"
												name='%{#dynamicField.componentName}' />

										</s:if>

									</s:if>

								</s:elseif>

								<s:elseif test='%{#dynamicField.componentType=="5"}'>
									<s:textarea id='%{#dynamicField.componentId}'
										name='%{#dynamicField.componentName}' class="txtArea"
										value="%{#dynamicField.dynamicValue}" cols="8" rows="6" />

									<s:hidden value="%{#dynamicField.farmerDynamicValueId}"
										class="dynamicValueIds" name='%{#dynamicField.componentName}' />

									<s:hidden value="%{#dynamicField.isRequired}"
										class="isRequired" name='%{#dynamicField.componentName}' />

								</s:elseif>
								
								

							</div>
						</div>
					</s:iterator>
				</div>
			</div>

		</div>
	</s:iterator>
</s:if>
