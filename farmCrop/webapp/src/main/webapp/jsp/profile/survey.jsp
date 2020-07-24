<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<link rel="stylesheet"
	href="plugins/DataTables-1.10.15/media/css/dataTables.bootstrap.min.css">
<link rel="stylesheet"
	href="plugins/DataTables-1.10.15/media/css/dataTables.jqueryui.css">
<%@ include file="/jsp/common/report-assets.jsp"%>
<%-- <%@ include file="/jsp/common/detail-assets.jsp"%> --%>
<%-- <%@ include file="/jsp/common/grid-assets.jsp"%>  --%>


<head>
<META name="decorator" content="swithlayout">
<!--<link href="css/main_table.css" rel="stylesheet" type="text/css" />-->

<s:head />
<style type="text/css">
.hideControls {
	display: none;
}

.checkbox {
    position: relative;
    /* display: block; */
    margin-top: 10px;
    margin-bottom: 10px;
    }

#tablePartsialSave tbody tr {
	height: 10px !important;
	min-height: 35px;
	/* or whatever height you need to make them all consistent */
}

#tablePartsialSave tbody tr td {
	height: 10px !important;
	min-height: 35px;
}

input[type="text"] {
	width: 120px;
}

.errorDivFields .errorDiv {
	color: red;
}

.red {
	color: red;
	
}

.greenDiv {
	color: green;
}

.mainTextBox {
	width: 85px !important;
}

.subTextBox {
	width: 30px !important;
}

.surveyTableHeader table {
	width: 100% !important;
}

.surveyTableHeader table thead tr th, .surveyTableHeader table tbody tr td
	{
	padding: 5px;
	font-weight: bold;
	border: solid 1px #c8d09a;
}

.surveyTableHeader table tbody tr td.dataTables_empty {
	font-weight: normal;
}

.panel-menu dt-panelmenu {
	width: 100%;
}
/* 

#tablePartsialSave_paginate {
	position: relative;
}
.pagination {
    display: inline-block;
}

.pagination a {
    color: black;
    float: left;
    padding: 8px 16px;
    text-decoration: none;
} */
/* 
.editInfoTable th {
	background: #D3DEB0 !important;
	border: 1px solid #939585 !important;
	color: #000 !important;
	padding: 4px;
	text-align: center;
}

.editInfoTable td {
	padding: 3px;
	border-bottom: solid 1px #939585;
} */
.submitDiv {
	width: 250px;
	/*margin: 10px auto;*/
}

.cancelDiv {
	margin: 10px auto;
}

#submitBtn, #cancelBtn {
	float: left;
	margin: 0 10px;
}
/* 
.plusSurvey {
	width: 28px;
	height: 20px;
	background: #567304
		url(assets/client/demo/images/agro-theme/filter-plus.png) no-repeat
		9px 4px !important;
	text-indent: -10000px;
	border: none;
	cursor: pointer;
}

.plusSurvey:hover {
	background: #799b06
		url(assets/client/demo/images/agro-theme/filter-plus.png) no-repeat
		9px 4px !important;
}

.minusSurvey {
	width: 28px;
	height: 20px;
	background: #567304
		url(assets/client/demo/images/agro-theme/filter-minus.png) no-repeat
		9px 8px !important;
	text-indent: -10000px;
	border: none;
	cursor: pointer;
}

.minusSurvey:hover {
	background: #799b06
		url(assets/client/demo/images/agro-theme/filter-minus.png) no-repeat
		9px 8px !important;
} */
.subForm {
	table-layout: fixed;
}

.subForm td {
	word-wrap: break-word;
}

table.subForm td input[type="text"] {
	width: 90%;
}

table.subForm td select {
	width: 90%;
}

table.subForm td textarea {
	width: 90% !important;
}

/* ul.bjqs {
	position: relative;
	list-style: none;
	padding: 0; /*margin:0;overflow:hidden;*/
display
:
 
none
;

	
background-color
:
 
white
;


}
p.bjqs-caption {
	background: rgba(35, 94, 43, 0.5);
}
*
/
</style>

</head>

<body>
	<s:form id="surveyForm" action="" enctype="multipart/form-data"
		onsubmit="return false">

		<s:hidden name="selectedLevel" id="hiddenSelectedLevel" />
		<s:hidden name="surveyEdit"  id="surveyEdit" />
		<s:hidden name="selectedAgent" id="hiddenSelectedAgent" />
		<s:hidden name="farmerCropProdAnswers.categoryName"
			id="hiddenCategoryName" />
		<s:hidden name="farmerCropProdAnswers.dataCollectorName"
			id="hiddenDataCollectorName" />
		<s:hidden name="selectedSurveyMaster" id="hiddenSelectedSurveyMaster" />
		<s:hidden name="selectedSectionCode" id="hiddenSelectedSectionCode" />
		<s:hidden name="selectedDatalevelCode" id="hiddenSeletedDatalevelCode" />

		<s:hidden name="id" id="surveyId" />
		<s:hidden name="pageAction" id="pageAction" />
		<s:hidden name="farmerCropProdAnswers.cooperativeCode"
			id="hiddenCooperativeCode" />
		<s:hidden name="farmerCropProdAnswers.farmerId" id="hiddenFarmerId" />
		<s:hidden name="farmerCropProdAnswers.subFormJsonData"
			id="hiddenJSONdata" />
		<s:hidden name="farmerCropProdAnswers.quesCodes" id="quesCodes" />
		<s:iterator value="sectionPositionMap">
			<s:hidden
				name="farmerCropProdAnswers.farmersSectionAnswersList[%{value}].sectionCode"
				value="%{key}" />
		</s:iterator>

		<div class="appContentWrapper marginBottom  filter-background">
			<div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<div class="notificationBar">
							<div class="error">
								<p class="notification">
									<span class="manadatory">*</span>
									<s:text name="reqd.field" />
								<div id="validateError" style="text-align: center;"></div>

								<div class="greenDiv">
									<s:actionmessage />
								</div>
								<div class="errorDiv">
									<s:actionerror />
								</div>

								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="appContentWrapper marginBottom editTable">
				<div class="formContainerWrapper">
	                    <div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('editsurveyInfo')}" />
									<div class="pull-right"></div>
								</h2>
							</div>
			<div class="surveyTableHeader" id="divData">
				<table cellpadding="2" cellspacing="0" border="0" class="table"
					id="tablePartsialSave">
					<thead>
						<tr>
							<th><s:text name="id" /></th>
							<th><s:text name="date" /></th>
							<th><s:text name="survey" /></th>
							<th><s:text name="farmer" /></th>
							<th><s:text name="agent" /></th>
							<th><s:text name="actions" /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			</div>
			</div>
			</div>
			<div id="test" class="appContentWrapper marginBottom">
				<div class="formContainerWrapper">
			<div class="errorDivFields"></div>
			 <div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('surveyInfo')}" />
									<div class="pull-right"></div>
								</h2>
							</div>
			<div  class="flexiWrapper filterControls">

				<div class="flexi flexi6">
					<label for="txt"><s:property
							value="%{getLocaleProperty('survey')}" /> <sup
						style="color: red;">*</sup></label>

					<div class="form-element">
						<s:select class="form-control input-sm select2" id="surveyInput"
							headerKey="" headerValue="%{getText('txt.select')}" listKey="key" listValue="value"
							name="farmerCropProdAnswers.surveyCode" list="surveMasterMap"
							theme="simple" onchange="showContorlsBySection(this.value,true);" />

					</div>
				</div>

				<div class="flexi flexi6 odd resetClass hide"
					id="cooperativeTr">
					<label for="txt"><s:property
							value="%{getLocaleProperty('profile.samithi')}" /> <sup
						style="color: red;">*</sup></label>

					<div class="form-element">
						<s:select id="cooperativeInput" headerKey=""
							class="form-control input-sm select2" headerValue="%{getText('txt.select')}"
							list="cooperativeMap" theme="simple"
							name="farmerCropProdAnswers.cooperativeCode"
							onchange="populateFarmerByCooperative(this.value)" />
					</div>
				</div>
				<div class="flexi flexi6 odd resetClass hide" id="farmerTr">
					<label for="txt"><s:property
							value="%{getLocaleProperty('farmer')}" /> <sup
						style="color: red;">*</sup></label>

					<div class="form-element">
						<s:select id="farmerInput" headerKey=""
							class="form-control input-sm select2" headerValue="%{getText('txt.select')}"
							list="farmerMap" theme="simple"
							name="farmerCropProdAnswers.farmerId" />
					</div>
				</div>
				<div class="flexi flexi6">
					<label for="txt"><s:property value="%{getLocaleProperty('action')}" /></label>
					<td colspan="4" class="alignCenter">
						<table class="actionBtnWarpper">

							<td class="textAlignCenter">

								<button id="proceedBtn" class="save-btn btn btn-success">
									<font color="#FFFFFF"> <b><s:property	value="%{getLocaleProperty('btn.proceed')}" />
									</b>
									</font>
								</button>

								<button id="resetBtn" class="cancel-btn btn btn-sts"
									onclick="unloadQuestion();">
									<font color="#FFFFFF"> <b> <s:property	value="%{getLocaleProperty('btn.rest')}" />
									</b>
									</font>
								</button>

							</td>
						</table>
					</td>
				</div>


			</div>
			</div>
			</div>
		
		<div class="service-content-wrapper hide" id="editInfo">
			<div class="service-content-section">
				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
		<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('surveyInfo')}" />
									<div class="pull-right"></div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="surveyInfo">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
									<s:property	value="%{getLocaleProperty('farmerCropProdAnswers.survey')}" />"
										
									</p>
									<p class="flexItem">
										<s:property
											value="farmerCropProdAnswers.surveyCode.split('~')[0]" />
										-
										<s:property value="farmerCropProdAnswers.surveyName" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
									<s:property	value="%{getLocaleProperty('farmerCropProdAnswers.dataCollector')}" />"
										
									</p>
									<p class="flexItem">
										<s:property value="farmerCropProdAnswers.dataCollectorId" />
										-
										<s:property value="farmerCropProdAnswers.dataCollectorName" />
									</p>
								</div>
							</div>
	</div>
				</div>
						
		</div>
		
			</div>
						
							<div class="appContentWrapper marginBottom" id="questionContainer">
				<div class="formContainerWrapper">
	                    <div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('questions')}" />
									<div class="pull-right"></div>
								</h2>
							</div>
						<div >
							<s:if test='questions.size()>0'>
								<div id="questionsDiv">
									<table class="table" border="1" cellpadding="5px"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th width="5%"><s:property	value="%{getLocaleProperty('s.no')}" /></th>
												<th width="5%"><s:property	value="%{getLocaleProperty('section')}" /></th>
												<th width="5%"><s:property	value="%{getLocaleProperty('code')}" /></th>
												<th width="50%"><s:property	value="%{getLocaleProperty('question')}" /></th>
												<th width="5%" align="center">
													<div class="surveyInformationHeadingIcon"></div>
												</th>
												<th width="30%"><s:property	value="%{getLocaleProperty('answer')}" /></th>
												<th width="30%"><s:property	value="%{getLocaleProperty('na')}" /></th>
												<th width="30%"><s:property	value="%{getLocaleProperty('idontKnow')}" /></th>
												<th width="30%"><s:property	value="%{getLocaleProperty('image')}" /></th>
											</tr>
										</thead>
										<tbody>
											<s:iterator value="questions" var="questions"
												status="questionsStatus">
												<s:hidden id="dependencyKeyHidden_%{id}"
													value="%{dependencyKey}" />
												<s:if test='#questions.questionType>2'>
													<tr id='<s:property value="tableRowId"/>'
														class='<s:property value="tableRowClass"/>'>
														<td style="text-align: center;"><s:property
																value="#questionsStatus.index+1" />
															<button type="button" onclick="showSubFormTR(this)"
																class="glyphicon glyphicon-plus-sign plusSurvey"
																id="plusSurvey_<s:property value="#questions.id"/>"></button>

															<button type="button" onclick="hideSubFormTR(this)"
																class="glyphicon glyphicon-minus-sign minusSurvey hide"
																id="minusSurvey_<s:property value="#questions.id"/>"></button>
														</td>
														<td><s:property value="#questions.section.name" /></td>
														<td><s:property value="#questions.code" /></td>
														<td><s:property value="#questions.name" /></td>
														<td align="center"><s:if
																test='info!=null && !"".equalsIgnoreCase(#questions.info)'>
																<div class="surveyInformationEnableIcon"
																	onclick="showImgInfo('info_img_<s:property value="tableRowId"/>','Info Of Question <s:property value="#questions.code" />');"
																	title=""></div>
																<div class="hide"
																	id='info_img_<s:property value="tableRowId"/>'>
																	<s:property value="#questions.info" />
																</div>
															</s:if> <s:else>
																<div class="surveyInformationDisableIcon"></div>
															</s:else> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].questionCode"
																value="%{#questions.code}" /> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].questionName"
																value="%{#questions.name}" /> <s:hidden
																cssClass="compType"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].componentType"
																value="%{#questions.componentType}" /></td>
														<td>
															 <span class='red'><div class="errorAnswerDiv">
																<s:property
																	value="getQuestionFieldError(#questions.code)" />
															</div></span>
														</td>
														<td align="center"><s:checkbox theme="simple"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].nonConfirm"
																cssClass="na checkbox" onchange="handleNA(this);" /></td>
														<td align="center"><input type="checkbox"
															name="idontKnow" class="chkidk"
															onchange="handleIdk(this);" /> <s:hidden
																id="hidk_%{tableRowId}" cssClass="idk"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																value="CT136" disabled="true" /> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].verificationStatus" />
														</td>
														<td></td>
													</tr>
													<tr>
														<td colspan="9">
															<div style="overflow-x: auto; width: 100%;">
																<table border="1"
																	style="margin: auto; border: solid; width: 780%"
																	cellpadding="5px" cellspacing="0"
																	id="table_<s:property value="#questions.id"/>"
																	class=" table table_sub_<s:property value="tableRowId"/> hide subForm">

																	<tr>
																	
																	<s:hidden id='subFormQn_%{#questions.id}' />
																		<s:iterator value="#questions.subFormQuestions"
																			var="subForm" status="questionsStatus">
																			<s:if test='childQuestion.questionType!="1"'>
																				<td><s:property
																						value="#subForm.childQuestion.name" /><br> <s:if
																						test='childQuestion.info!=null && !"".equalsIgnoreCase(childQuestion.info)'>
																						<div class="surveyInformationEnableIcon"
																							onclick="showImgInfo('info_img_<s:property value="#subForm.childQuestion.code" />','Info Of Question <s:property value="#subForm.childQuestion.code" />');"
																							title=""></div>
																						<div class="hide"
																							id='info_img_<s:property value="#subForm.childQuestion.code" />'>
																							<s:property value="#subForm.childQuestion.info" />
																						</div>
																					</s:if> <s:else>
																						<div class="surveyInformationDisableIcon"></div>
																					</s:else></td>
																			</s:if>

																		</s:iterator>
																		<td width="5%">
																			<button type="button"
																				onclick="addRowSubForm(<s:property value="#questions.id"/>)"
																				class=" glyphicon glyphicon-plus-sign plus_<s:property value="#questions.id"/>"
																				id="plus"></button>
																		</td>
																	</tr>

																	<script>
																		var jsonVal = '<s:property value="#questions.subFormTr" escape="false"  />';
																		if(jsonVal!='' && jsonVal!='{}'){
																		jsonVal = JSON.parse(jsonVal)
																	var val = jsonVal.empty;
																	var noeEmpty = jsonVal.notempty;
																	$('#subFormQn_'+'<s:property value="#questions.id" />').val(val);
																	console.log(noeEmpty);
																	if(noeEmpty!='' && noeEmpty!=undefined){
																		jQuery('.table_sub_question_<s:property value="#questions.id"/>').append(noeEmpty);
																		jQuery('.table_sub_question_<s:property value="#questions.id"/>').removeClass('hide');
																		     $('#plusSurvey_<s:property value="#questions.id"/>').hide();
																		     $('#minusSurvey_<s:property value="#questions.id"/>').show();
																		    // $('#minusSurvey_'+ids[1]).removeClass("hide")
																	}else{
																		var count = jQuery('.table_sub_question_<s:property value="#questions.id"/> tr').length;
																	val = val.replace(/~/g, count);
															    	jQuery('.table_sub_question_<s:property value="#questions.id"/>').append(val);
																	$(".datePickerSub").datepicker({
																	 	changeMonth: true,
																			changeYear: true	
																		});
																	$('.chg').trigger("change");
																	
																	$('.select2Sub').select2();
																	}	
																		}
														           </script>
																</table>
															</div>
														</td>
													</tr>
												</s:if>
												<s:else>
													<tr id='<s:property value="tableRowId"/>'
														class='<s:property value="tableRowClass"/>'>
														<td style="text-align: center;"><s:property
																value="#questionsStatus.index+1" /></td>
														<td><s:property value="#questions.section.name" /></td>
														<td><s:property value="#questions.code" /></td>
														<td><s:if test="#questions.mandatory==0">
																<s:property value="#questions.name" />
																<sup>*</sup>
															</s:if> <s:else>
																<s:property value="#questions.name" />
															</s:else></td>
														<td align="center"><s:if
																test='info!=null && !"".equalsIgnoreCase(#questions.info)'>
																<div class="surveyInformationEnableIcon"
																	onclick="showImgInfo('info_img_<s:property value="tableRowId"/>','Info Of Question <s:property value="#questions.code" />');"
																	title=""></div>
																<div class="hide"
																	id='info_img_<s:property value="tableRowId"/>'>
																	<s:property value="#questions.info" />
																</div>
															</s:if> <s:else>
																<div class="surveyInformationDisableIcon"></div>
															</s:else> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].questionCode"
																value="%{#questions.code}" /> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].questionName"
																value="%{#questions.name}" /> <s:hidden
																cssClass="compType"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].componentType"
																value="%{#questions.componentType}" /></td>
														<td><s:if
																test="#questions.componentType == 1 and #questions.validationType != 2 and #questions.validationType != 5 and #questions.validationType != 6">
																<!-- TEXT_BOX Type  (Excluded Double Data Type)-->
																<s:if test="#questions.maxLength!=null">
																	<s:textfield
																		cssClass="%{componentClass}  form-control form-control"
																		title="%{#questions.info}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		maxlength="%{#questions.maxLength}" />
																</s:if>
																<s:else>
																	<s:textfield
																		cssClass="%{componentClass}  form-control form-control"
																		title="%{#questions.info}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer" />
																</s:else>
																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select
																			cssClass="%{componentClass}  select2 form-control form-control"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select
																			cssClass="%{componentClass}  form-control form-control"
																			id="unit_drop_down_others_%{#questions.code}"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}')" />
																		<s:textfield
																			id="unit_drop_down_others_%{#questions.code}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:if> <s:if
																test="#questions.componentType == 1 and (#questions.validationType == 2 or #questions.validationType == 5 or #questions.validationType == 6)">
																<!-- TEXT_BOX Type  with Double Data Type -->
																<s:textfield title="%{#questions.info}"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																	maxlength="%{#questions.doublesWholeNoMaxLength}"
																	cssClass="mainTextBox %{componentClass}  form-control" />
			               		 . <s:textfield title="%{#questions.info}"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer1"
																	maxlength="%{#questions.doublesFloatNoMaxLength}"
																	cssClass="subTextBox %{componentClass}  form-control" />
																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select cssClass="%{componentClass} select2  form-control"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select cssClass="%{componentClass} select2  form-control"
																			id="unit_drop_down_others_%{#questions.code}"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}')" />
																		<s:textfield
																			id="unit_drop_down_others_%{#questions.code}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:if> <s:elseif test="#questions.componentType == 3">
																<!-- RADIO_BUTTON Type -->
																<s:if test="#questions.defaultValues.size()>0">
																	<s:property value="#questions.defaultCatalogue.name" /> &nbsp;
				               </s:if>
																<s:if test="#questions.listMethodName==null">
																	<s:radio cssClass="%{componentClass} "
																		onchange="%{dependentJSFunction}"
																		list="#questions.answerKeys"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name"></s:radio>
																</s:if>
																<s:else>
																	<s:radio cssClass="%{componentClass}"

																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"></s:radio>
																</s:else>
																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select cssClass="%{componentClass}  select2 form-control"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select cssClass="%{componentClass} select2  form-control"
																			id="unit_drop_down_others_%{#questions.code}"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}')" />
																		<s:textfield
																			id="unit_drop_down_others_%{#questions.code}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
															 <span class='red'>	<div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 4">
																<!-- RADIO_BUTTON Other Type -->
																<s:if test="#questions.defaultValues.size()>0">
																	<s:property value="#questions.defaultCatalogue.name" /> &nbsp;
				               </s:if>
																<s:if test="#questions.listMethodName==null">
																	<s:radio id="radio_others_%{#questions.code}"
																		cssClass="%{componentClass}  checkBox%{code}"
																		onchange="showRadioButtonOthersTxt(this, '%{#questions.otherCatalogValue}');%{dependentJSFunction}"
																		list="#questions.answerKeys"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name"></s:radio>
																</s:if>
																<s:else>
																	<s:radio id="radio_others_%{#questions.code}"
																		cssClass="%{componentClass}  checkBox%{code}"
																		onchange="showRadioButtonOthersTxt(this, '%{#questions.otherCatalogValue}')"
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"></s:radio>
																</s:else>
                                                                <span
																	id="radio_others_<s:property value="#questions.code" />_othval"
																	class="hide"><s:property
																		value="#questions.otherCatalogValue" /></span>
																<s:textfield
																	id="radio_others_%{#questions.otherCatalogValue}_txt"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer2"
																	cssClass="hideControls %{componentClass}  form-control"
																	maxlength="45" />
																<script>
																   if($('#radio_others_<s:property value="#questions.code" /><s:property value="#questions.otherCatalogValue" />').is(':checked')){
														            	jQuery('#radio_others_<s:property value="#questions.otherCatalogValue" />_txt').show();
											                           }else{
											                        	   jQuery('#radio_others_<s:property value="#questions.otherCatalogValue" />_txt').hide();  
											                           }
					           </script>

																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select cssClass="%{componentClass} select2  form-control"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select cssClass="%{componentClass}  select2 form-control"
																			id="unit_drop_down_others_%{#questions.code}"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}')" />
																		<s:textfield
																			id="unit_radio_others_%{#questions.unitOtherCatalogValue}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_radio_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_radio_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
															 <span class='red'>	<div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 5">
																<!-- CHECK_BOX Type -->
																<s:if test="#questions.defaultValues.size()>0">
																	<s:property value="#questions.defaultCatalogue.name" /> &nbsp;
				               </s:if>
																<s:if test="#questions.listMethodName==null">
																	<s:checkboxlist style="position: relative;  margin-top: 10px; margin-bottom: 10px;"
																		cssClass="%{componentClass} checkBox%{code}"
																		onchange="%{dependentJSFunction}"
																		list="#questions.answerKeys"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name" />
																</s:if>
																<s:else>
																	<s:checkboxlist theme="simple" style="position: relative;  margin-top: 10px; margin-bottom: 10px;"
																		cssClass="%{componentClass} checkBox%{code} "
																	
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer" />
																</s:else>
																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 6">
																<!-- CHECK_BOX Others Type -->
																<s:if test="#questions.defaultValues.size()>0">
																	<s:property value="#questions.defaultCatalogue.name" /> &nbsp;
				               </s:if>
																<s:if test="#questions.listMethodName==null">
																	<s:checkboxlist theme="simple" id="check_others_%{#questions.code}"
																		cssClass="%{componentClass}  checkBox%{code} "
																		list="#questions.answerKeys"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name"
																		onchange="showCheckboxOthersTxt(this, '%{#questions.otherCatalogValue}');%{dependentJSFunction} " />
																</s:if>
																<s:else>
																	<s:checkboxlist theme="simple" id="check_others_%{#questions.code}"
																		cssClass="%{componentClass}  checkBox%{code} "
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		onchange="showCheckboxOthersTxt(this, '%{#questions.otherCatalogValue}');%{dependentJSFunction} " />
																</s:else>
																<s:hidden 	id="checkbox_others_%{#questions.code}_otherVal" value= "%{#questions.otherCatalogValue}"/>
                                                         			<s:textfield 
																	id="checkbox_others_%{#questions.otherCatalogValue}_txt"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer2"
																	cssClass="hide %{componentClass}  form-control"
																	maxlength="45" />
																<script>
																$('#checkbox_others_<s:property value="#questions.code" />').filter(':checked').each(function(){
																//	alert("cc"+$(this).val());
																	  if($(this).val() == '<s:property value="#questions.otherCatalogValue" />'){
															            	jQuery('#checkbox_others_<s:property value="#questions.otherCatalogValue" />_txt').show();
															            	jQuery('#checkbox_others_<s:property value="#questions.otherCatalogValue" />_txt').removeClass("hide");
												                           }
																	  });
																
					           </script>

																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 7">
																<!-- DROP_DOWN Type -->
																<s:if test="#questions.listMethodName==null">
																	<s:select cssClass="%{componentClass}  select2 form-control"
																		list="#questions.answerKeys" onchange="%{dependentJSFunction}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		headerKey="" headerValue="%{getText('txt.select')}"
																		listKey="code" listValue="name" />
																</s:if>
																<s:else>
																	<s:select cssClass="%{componentClass} select2 form-control"
																		list="getOptions(#questions.listMethodName)" 
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		headerKey="" headerValue="%{getText('txt.select')}" />
																</s:else>
																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select cssClass="%{componentClass} select2 form-control"
																			list="#questions.units"  onchange="%{dependentJSFunction}"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select cssClass="%{componentClass} select2 form-control"
																			id="unit_drop_down_others_%{#questions.code}" 
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}');%{dependentJSFunction}" />
																		<s:textfield
																			id="unit_drop_down_others_%{#questions.code}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
															 <span class='red'>	<div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 8">
																<s:if test="#questions.listMethodName==null">
																	<!-- DROP_DOWN_OTHERS Type -->
																	<s:select cssClass="%{componentClass} select2 form-control"
																		id="combo_drop_down_others_%{#questions.code}"
																		list="#questions.answerKeys" 
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name" headerKey=""
																		headerValue="%{getText('txt.select')}"
																		onchange="showOthersTxt(this, '%{#questions.otherCatalogValue}');%{dependentJSFunction}" />
																	<s:textfield
																		id="combo_drop_down_others_%{#questions.code}_txt"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer2"
																		cssClass="hideControls %{componentClass}  form-control"
																		maxlength="45" />
																	<script>
						            if(jQuery('#combo_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.otherCatalogValue" />'){
						            	jQuery('#combo_drop_down_others_<s:property value="#questions.code" />_txt').show();
			                           }	
						           </script>
																	<s:if test="#questions.units.size()>0">
																		<s:if
																			test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																			<s:select cssClass="%{componentClass} select2 form-control"
																				list="#questions.units"  onchange="%{dependentJSFunction}"
																				name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																				listKey="code" listValue="name" />
																		</s:if>
																		<s:else>
																			<s:select cssClass="%{componentClass} select2 form-control"
																				id="unit_drop_down_others_%{#questions.code}"
																				list="#questions.units"
																				name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																				listKey="code" listValue="name"
																				onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}');%{dependentJSFunction}" />
																			<s:textfield
																				id="unit_drop_down_others_%{#questions.code}_txt"
																				name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																				cssClass="hideControls %{componentClass}  form-control" />
																			<script>
									            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
									            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
						                           }	
									           </script>
																		</s:else>
																	</s:if>
																	<br />
																 <span class='red'>	<div class="errorAnswerDiv">
																		<s:property
																			value="getQuestionFieldError(#questions.code)" />
																	</div></span>
																</s:if>
																<s:else>
																	<s:select
																		id="combo_drop_down_others_%{#questions.code}"
																		cssClass="%{componentClass} select2 form-control"
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		headerKey="" headerValue="%{getText('txt.select')}"
																		onchange="showOthersTxt(this, '%{#questions.otherCatalogValue}')" />

																	<s:textfield
																		id="combo_drop_down_others_%{#questions.code}_txt"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer1"
																		cssClass="hideControls %{componentClass}  form-control"
																		maxlength="45" />
																	<script>
						              if(jQuery('#combo_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.otherCatalogValue" />'){
						            	jQuery('#combo_drop_down_others_<s:property value="#questions.code" />_txt').show();
			                           }	
						               //jQuery('#combo_drop_down_others_<s:property value="#questions.code" />_txt').val("");
						            </script>
																</s:else>
															</s:elseif> <s:elseif test="#questions.componentType == 13">
																<!-- MULTI_SELECT_DROP_DOWN_OTHERS Type -->

																<s:if test="#questions.listMethodName==null">
																	<s:select
																		cssClass="%{componentClass} select2 form-control multipleSelectClass"
																		id="multi_select_drop_down%{#questions.code}"
																		multiple="true" list="#questions.answerKeys"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name"
																		onchange="showMultiSelectOthersTxt(this, '%{#questions.otherCatalogValue}');%{dependentJSFunction}" />
																</s:if>
																<s:else>
																	<s:select
																		cssClass="%{componentClass}  select2 form-control multipleSelectClass"
																		id="multi_select_drop_down%{#questions.code}"
																		multiple="true"
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		onchange="showMultiSelectOthersTxt(this, '%{#questions.otherCatalogValue}')" />
																</s:else>

																<s:textfield
																	id="multi_select_drop_down%{#questions.code}_txt"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer2"
																	cssClass="hideControls %{componentClass}  form-control"
																	maxlength="45" />
																<span
																	id="multi_select_drop_down<s:property value="#questions.code" />_othval"
																	class="hide"><s:property
																		value="#questions.otherCatalogValue" /></span>
																<script>
					           var otherVal=$('#multi_select_drop_down<s:property value="#questions.code" />_txt').val();
					           if(otherVal!=null&&otherVal!=undefined&&otherVal!=''&&otherVal!='null'){
					            	jQuery('#multi_select_drop_down<s:property value="#questions.code" />_txt').show();
		                          }	else{
		                        	  jQuery('#multi_select_drop_down<s:property value="#questions.code" />_txt').hide();
		                          }
					           </script>
																<s:if test="#questions.units.size()>0">
																	<s:if
																		test="#questions.unitOtherCatalogValue==null or #questions.unitOtherCatalogValue==''">
																		<s:select cssClass="%{componentClass} select2 form-control"
																			list="#questions.units" onchange="%{dependentJSFunction}"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name" />
																	</s:if>
																	<s:else>
																		<s:select cssClass="%{componentClass} select2 form-control"
																			id="unit_drop_down_others_%{#questions.code}"
																			list="#questions.units"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer4"
																			listKey="code" listValue="name"
																			onchange="showUnitsOthersTxt(this, '%{#questions.unitOtherCatalogValue}');%{dependentJSFunction}" />
																		<s:textfield
																			id="unit_drop_down_others_%{#questions.code}_txt"
																			name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer6"
																			cssClass="hideControls %{componentClass}  form-control" />
																		<script>
								            if(jQuery('#unit_drop_down_others_<s:property value="#questions.code" />').val() == '<s:property value="#questions.unitOtherCatalogValue" />'){
								            	jQuery('#unit_drop_down_others_<s:property value="#questions.code" />_txt').show();
					                           }	
								           </script>
																	</s:else>
																</s:if>
																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 10">
																<!-- DATE_PICKER Type -->
																<s:textfield
																	cssClass="datePicker %{componentClass}  form-control"
																	title="%{#questions.info}"
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer" />
																<br />
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 12">
																<!-- MULTI Select DropDown Type -->
																<s:if test="#questions.listMethodName==null">
																	<s:select
																		cssClass="%{componentClass} select2 form-control multipleSelectClass"
																		id="multi_select_drop_down%{#questions.code}"
																		multiple="true" list="#questions.answerKeys" onchange="%{dependentJSFunction}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		listKey="code" listValue="name" />
																</s:if>
																<s:else>
																	<s:select
																		cssClass="%{componentClass} select2  form-control multipleSelectClass"
																		id="multi_select_drop_down%{#questions.code}"
																		multiple="true"
																		list="getOptions(#questions.listMethodName)"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer" />
																</s:else>
																<br />
															 <span class='red'>	<div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 2">
																<!-- GPS TEXT_BOX Type -->
																<s:if test="#questions.maxLength!=null">
																	<table cellpadding="5px">
																		<tr class="odd">
																			<td><s:property	value="%{getLocaleProperty('latitude')}" /></td>
																			<td><s:textfield
																					cssClass="%{componentClass}  form-control"
																					title="%{#questions.info}"
																					name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																					maxlength="%{#questions.maxLength}" /></td>
																		</tr>
																		<tr class="odd">
																			<td><s:property	value="%{getLocaleProperty('longitude')}" /></td>
																			<td><s:textfield
																					cssClass="%{componentClass}  form-control"
																					title="%{#questions.info}"
																					name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer1"
																					maxlength="%{#questions.maxLength}" /></td>
																		</tr>
																	</table>
																</s:if>
																<s:else>
																	<table>
																		<tr class="odd">
																				<td><s:property	value="%{getLocaleProperty('latitude')}" /></td>
																			<td><s:textfield
																					cssClass="%{componentClass}  form-control"
																					title="%{#questions.info}"
																					name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer" /></td>
																		</tr>
																		<tr class="odd">
																			<td><s:property	value="%{getLocaleProperty('longitude')}" /></td>
																			<td><s:textfield
																					cssClass="%{componentClass}  form-control"
																					title="%{#questions.info}"
																					name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer1" /></td>
																		</tr>
																	</table>
																</s:else>
																<br />
															 <span class='red'>	<div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code+'_lat')" />
																	<br />
																	<s:property
																		value="getQuestionFieldError(#questions.code+'_lon')" />
																</div></span>
															</s:elseif> <s:elseif test="#questions.componentType == 9">
																<!-- TEXT AREA -->
																<s:if test="#questions.maxLength!=null">
																	<s:textarea cssClass="%{componentClass}  form-control"
																		title="%{#questions.info}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		cols="30" rows="%{#questions.maxLength}" />
																</s:if>
																<s:else>
																	<s:textarea cssClass="%{componentClass}  form-control"
																		title="%{#questions.info}"
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																		cols="30" rows="100" />
																</s:else>
																 <span class='red'><div class="errorAnswerDiv">
																	<s:property
																		value="getQuestionFieldError(#questions.code)" />
																</div></span>
															</s:elseif></td>
														<td align="center"><s:checkbox theme="simple"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].nonConfirm"
																cssClass="na checkbox" onchange="handleNA(this);" /></td>
														<td align="center"><input type="checkbox"
															name="idontKnow" class="chkidk"
															onchange="handleIdk(this);" /> <s:hidden
																id="hidk_%{tableRowId}" cssClass="idk"
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].ansList[0].answer"
																value="CT136" disabled="true" /> <s:hidden
																name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].verificationStatus" />
														</td>
														<td><s:set name="valIF"
																value="%{qusAnsPositionMap[#questions.code]}" /> <s:if
																test="farmerCropProdAnswers.farmersSectionAnswersList[sectionPositionMap[#questions.section.code]].farmersQuestionAnswersList[qusAnsPositionMap[#questions.code]].image != null">
																<font color="blue">
																	<div
																		id="errorimage<s:property value='#questionsStatus.index'/>"></div>
																</font>
																<img border="0"
																	id="view_remove<s:property value='qusAnsPositionMap[#questions.code]'/>"
																	style="width: 150px; height: 133px;"
																	src="data:image/png;base64,<s:property value="farmerCropProdAnswers.farmersSectionAnswersList[sectionPositionMap[#questions.section.code]].farmersQuestionAnswersList[qusAnsPositionMap[#questions.code]].photoByteString"/>">
																<!--<button class='imageTag' type="button"  onclick="getVerifiactionImage(<s:property value='farmerCropProdAnswers.farmersSectionAnswersList[sectionPositionMap[#questions.section.code]].farmersQuestionAnswersList[qusAnsPositionMap[#questions.code]].id'/>,this)" id="view_remove<s:property value='qusAnsPositionMap[#questions.code]'/>"><s:text name="viewImage"/></button> 
                         -->
																<button class='imageTag' type="button"
																	onclick="removeVerifiactionImage('remove<s:property value='qusAnsPositionMap[#questions.code]'/>')"
																	id="remove<s:property value='qusAnsPositionMap[#questions.code]'/>">
																	<s:property	value="%{getLocaleProperty('removeButton')}" />
																</button>
																<br></br>
																<div style="display: none;"
																	id="file_remove<s:property value='qusAnsPositionMap[#questions.code]'/>">
																	<s:file
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].imageByteArray"
																		id="image%{qusAnsPositionMap[#questions.code]}" />
																	<s:hidden
																		name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].PhotoByteString"
																		id="byte_remove%{qusAnsPositionMap[#questions.code]}" />
																	<font color="red"><s:property	value="%{getLocaleProperty('imageTypes')}" /><br></br>
																		<s:property	value="%{getLocaleProperty('imgSizeMsg')}" /></font>
																</div>
															</s:if> <s:else>
																<font color="blue">
																	<div
																		id="errorimage<s:property value='#questionsStatus.index'/>"></div>
																</font>
																<s:file
																	name="farmerCropProdAnswers.farmersSectionAnswersList[%{sectionPositionMap[#questions.section.code]}].farmersQuestionAnswersList[%{qusAnsPositionMap[#questions.code]}].imageByteArray"
																	id="image%{qusAnsPositionMap[#questions.code]}" />
																<div>
																	<font color="red"><s:property	value="%{getLocaleProperty('imageTypes')}" /><br></br>
																		<s:property	value="%{getLocaleProperty('imgSizeMsg')}" /></font>
																</div>
															</s:else></td>
													</tr>
												</s:else>
											</s:iterator>
											<tr>
												<td colspan="3" align="center"><s:property	value="%{getLocaleProperty('comment')}" /></td>
												<td colspan="6" align="center"><s:textfield
														cssClass="form-control"
														name="farmerCropProdAnswers.comment" /></td>
											</tr>
											<tr id="trSaveStatus">
												<td colspan="3" align="center"><s:text
														name="saveStatus" /></td>
												<td colspan="6" align="center"><s:radio
														list="saveStatuses" listKey="key"
														value="%{farmerCropProdAnswers.saveStatus}"
														listValue="value" name="farmerCropProdAnswers.saveStatus" /></td>
											</tr>
										</tbody>
									</table>
								</div>
								<br />


								<div class="yui-skin-sam" id="savebutton">
									<s:if test="btnEnabled == 1">
										<sec:authorize ifAllGranted="service.survey.create">

											<span id="save" class=""><span class="first-child">
													<button type="button" id="submitBtn"
														class="save-btn btn btn-success">
														<font color="#FFFFFF"> <b><s:text
																	name="save.button" /></b>
														</font>
													</button>

											</span></span>

										</sec:authorize>
									</s:if>
									<span class="" style="cursor: pointer;"><span
										class="first-child"><a id="cancelBtn"
											class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
													<s:property	value="%{getLocaleProperty('cancel.button')}" />
											</font>
										</a></span></span>
								</div>



							</s:if>
						</div>
						</div>
						</div>
		

	</s:form>


	<s:form id="cancelForm" name="cancelForm" action="">
	</s:form>
	<script
		src="plugins/DataTables-1.10.15/media/js/jquery.dataTables.min.js"></script>
	<script
		src="plugins/DataTables-1.10.15/media/js/dataTables.jqueryui.min.js"></script>

	<script>
	var pageAction='<s:property value="pageAction" />';
	var jsonData='<s:property value="farmerCropProdAnswers.jsonData" escape="false" />';
	var jsonDataSubForm='<s:property value="farmerCropProdAnswers.jsonDataForSubAnswers" escape="false" />';
	var pageReturn='<s:property value="pageReturn" />';
	var language='<s:property value="loggedInUserLanguage" />';
	var jsonDataSub = '<s:property value="farmerCropProdAnswers.subFormJsonData" escape="false" />';
	var displayColumnJSONArray = {};
	var $overlay;
	var $modal;
	var $slider;
	var quesCodes ;
	 var subFormError ;
	 var editFull ='';
	jQuery(document).ready(function(){
		 editFull = '<s:property value="surveyEdit"/>';
	
		if(jsonDataSub!=''){
			 validationData(jsonDataSub);
		}
		loadCustomPopup();
		
		
    	$("textarea").each(function(){
    		  var maxLength = $(this).attr("rows");
    		  $(this).attr("rows","5");
    		  $(this).attr("maxlength",maxLength);
     	});
		

	
if(pageAction=='edit'||pageAction=='editFull'){
	$('#test').hide();
	$('#editInfo').removeClass('hide');
    $('#editInfo').show();
}else{
	 $('#test').show();
		$('#editInfo').addClass('hide');
     $('#editInfo').hide();
}
    if(pageAction=='editFull'){
            $('#trSaveStatus').hide();
            $('a[href$="survey_list.action"]').text('Edit Survey');
            $('a[href$="survey_list.action"]').attr("href",'surveyEdit_list.action');
            $('.pageTitle').find('.h4').find('label').text('Edit Survey');
        }

        if(jsonData!=null&&jsonData!=''&&jsonData!=undefined){
            jsonData=$.parseJSON(jsonData);
        	console.log(jsonData);
            var mapObj=jsonData.mapObj;
            var checkList=jsonData.checkBoxlist;
            subFormError = jsonData.errorCodesSubForm;
            if(subFormError!==undefined && subFormError!==null&&subFormError!==''){
    		    
            	$.each(subFormError, function(k, v) {
            		 $('#'+k).append(v);
            	});
            }
            
            if(checkList!==undefined && checkList!==null&&checkList!==''){
                $.each(checkList, function(k, v) {
                       var ansArr=v.split(",");
             	    $.each(ansArr, function(i,e){
            	    	$(".checkBox"+k+"[value='" + e + "']").prop("checked", true);
            	    	$(".radio_others_"+k+e).prop("checked", true);
            	    	var otherCat = $("#checkbox_others_"+k+"_otherVal").val();
            	  	    	alert("ccat"+otherCat);
            	        if(otherCat != "undefined"){
        	    	        if(otherCat!=null&&otherCat!=''&&otherCat===e){
        	    	        	$("#checkbox_others_"+otherCat+"_txt").show();
        	    	        	$("#checkbox_others_"+otherCat+"_txt").removeClass("hide");
        	    	        	$("#checkbox_others_"+otherCat+"_txt").val(ansArr[ansArr.length-1]);
        	    	        }
            	        }
            	         
            	    });
            	    
            	});
                }
            if(mapObj!==undefined && mapObj!==null&&mapObj!==''){
            $.each(mapObj, function(k, v) {
            	//alert("vv"+v);
                var ansArr=v.split(",");
        	    $.each(ansArr, function(i,e){
        	        $("#multi_select_drop_down"+k+" option[value='" + e + "']").prop("selected", true);
        	        var othVal=$('#multi_select_drop_down'+k+'_othval').text();
        	        if(othVal != "undefined"){
    	    	        if(othVal!=null&&othVal!=''&&othVal===e){
    	    	        	$("#multi_select_drop_down"+k+"_txt").show();
    	    	        	$("#multi_select_drop_down"+k+"_txt").val(ansArr[ansArr.length-1]);
    	    	        }
        	        }
        	      
        	    });
        	});
            }
            var list=jsonData.list;
            if(list!==undefined && list!==null&&list!==''){
            for(var i=0;i<list.length;i++){
                $('#question_'+list[i]).hide();
            }
            }
            var objects=jsonData.objects;
            if(objects!==undefined && objects!==null&&objects!==''){
               var idkCodes=objects[0];
            for(var i=0;i<idkCodes.length;i++){
                $('#question_'+idkCodes[i]).find('.chkidk').prop('checked',true);
                checkIdkCoumn($('#question_'+idkCodes[i]).find('.chkidk'));
            }
            var naCodes=objects[1];
            for(var i=0;i<naCodes.length;i++){
                $('#question_'+naCodes[i]).find('.na').prop('checked',true);
                checkNAColumn($('#question_'+naCodes[i]).find('.na'));
            }
            }
            }

     

     
     //  makeMultiSelect(".multipleSelectClass");
     if(editFull!='' && editFull=='1'){
    	 $('.editTable').hide();
     }else{
    	 var table = $('#tablePartsialSave').DataTable( {
    	        ajax: "./survey_populateParsialTable",
    	       /*  "fnDrawCallback": function (oSettings) { 
    	            if ($('#tablePartsialSave tr').length < 5) {
    	                $('.dataTables_paginate').hide();
    	            }
    	        }, */
    	        "language": {
    	            "emptyTable": '<s:property value="%{getLocaleProperty('noData')}" />'
    	          },
    	        "lengthMenu": [ 5,10,15 ],
    	         "pageLength": 5
    	        
    	    } );
     }
     
     
 $('.select2').select2();
	   /* if('<s:property value="surveyFailure"/>'!='true'){
	    var OTable = createIdDataTable('#tablePartsialSave', './survey_populateParsialTable',language);
	    $('#tablePartsialSave').on( 'draw.dt', function () {
              var recordCount=OTable.fnGetData().length;
              if(recordCount<1||pageAction=='editFull'){
                  $('#divData').hide();
              }else{
                  $('#divData').show();
              }
	    });
	}else{
		$('#divData').hide();
	}  */
		jQuery("#proceedBtn").click(function(){
			jQuery(this).attr("disabled",true);
		
			resetErrorDivs();
			var surveyInputValue = jQuery.trim(jQuery("#surveyInput").val());
			var isError = false;			
			
		//	jQuery("#surveyErrorDiv").html('&nbsp');
			if(surveyInputValue==""){
				isError = true;
				jQuery(".errorDivFields").append('<font color="red"><s:property	value="%{getLocaleProperty('empty.survey')}" /></font>');
			}
			if(!isError && !validateContorls() ){			
				var sectionSurveyMasterCodeArry=jQuery("#surveyInput").val().split('~');    	 
		        var surveyMasterCode=sectionSurveyMasterCodeArry[0];
		        var surveySectionCode=sectionSurveyMasterCodeArry[1];
		        if(pageAction==null||pageAction==''||pageAction=='create'){
		        $('#pageAction').val('create');
		      //  jQuery("#hiddenSelectedAgent").val(jQuery("#agentInput").val());	
		        jQuery("#hiddenSelectedSurveyMaster").val(surveyMasterCode);
		        jQuery("#hiddenSelectedSectionCode").val(surveySectionCode);
		        }
				//jQuery("#surveyForm").attr("action","survey_listQuestionsBySurveyMaster");
				//jQuery("#agentInput").removeAttr("disabled");
				jQuery("#surveyInput").removeAttr("disabled");
				jQuery("#cooperativeInput").removeAttr("disabled");
				
				jQuery("#farmerInput").removeAttr("disabled");
			
				$('#hiddenCooperativeCode').attr('disabled','disabled');
				$('#hiddenFarmerId').attr('disabled','disabled');
				$('#surveyForm').attr('action','survey_listQuestionsBySurveyMaster.action');
				//alert($('#familyMembers').val());
				$('#questionContainer').show();
				$('#questionContainer').removeClass('hide');
				document.getElementById("surveyForm").submit();
				
	
			}else{
				jQuery(this).attr("disabled",false);
			}

		});
		
		jQuery("#submitBtn").click(function() {
		    $('#submitBtn').css("pointer-events", "none");
		    //$('#submitBtn').attr('disabled','disabled');
		    resetErrorDivs();
		    if (!validateContorls() && !checkImage()) {
		        jQuery.post("survey_populateSurveySubmit", {
		            dt: new Date()
		        }, function(resp) {
		            //jQuery("#levelInput").removeAttr("disabled");
		            //jQuery("#entityInput").removeAttr("disabled");			
		            //jQuery("#hiddenCategoryName").val(jQuery("#levelInput option:selected").text());
		            //jQuery("#hiddenSectionName").val(jQuery("#entityInput option:selected").text());
		            var a = $('#infraTypeInput').val();
		            jQuery("#surveyInput").removeAttr("disabled");
		            jQuery("#cooperativeInput").removeAttr("disabled");
		              jQuery("#farmerInput").removeAttr("disabled");
		          
		            var sectionSurveyMasterCodeArry = jQuery("#surveyInput").val().split('~');
		            if (pageAction == null || pageAction == '' || pageAction == 'create' || '<s:property value="surveyFailure"/>'=='true') {
		                //jQuery("#hiddenSelectedSurveyMaster").val(jQuery("#surveyInput option:selected").text());
		              
		                jQuery("#hiddenSelectedSurveyMaster").val(sectionSurveyMasterCodeArry[0]);
		               
		            }
		        	
		            $('#pageAction').val(pageAction);
		            jQuery("#hiddenSelectedSectionCode").val(sectionSurveyMasterCodeArry[1]);
		            jQuery("#surveyForm").attr("action", "survey_create.action");
		            //alert(displayColumnJSONArray);
		            var finalObj = {};
		            $('.subForm').each(function() {
			          
		                if (!$(this).hasClass("hide")) {
		                	 // alert($(this).attr("id")+','+$(this).hasClass("hide"));
		                    var id = $(this).attr("id");
		                    // alert(id);
		                    var questionId = id.split("_")[1];
		                    var count = 0;
		                    //  alert("key:"+questionId);
		                    var finalTr = {};
		                    $(this).find('tr').each(function() {
		                        if (count > 0) {
		                            $(this).find('td').each(function() {
		                                var value = '';
		                                var quesId = $(this).attr("id");
		                                
		                                value = '';
		                                if ($(this).find('.1').val() != undefined && !$(this).find('.1').hasClass('hide')) {
		                                    
		                                    if ($(this).find('.1').is('input:radio')) {
		                                        value = $(this).find("input[class*='1']:checked").val();
		                                        if(value==undefined){
			                                        value='';
		                                        }
		                                    } else if ($(this).find('.1').is('input:checkbox')) {

		                                    	$(this).find("input[class*='1']:checked").each(function(){    
			                                    	if(value==''){
			                                    		value=$(this).val();
			                                    	}else{
			                                    		value = value+','+$(this).val();
			                                    	}        
		                                            
		                                         });
		                                    	if(value==undefined){
			                                        value='';
		                                        }
		                                    }else{
		                                    	value = $(this).find('.1').val();
		                                    	if(value==undefined){
			                                        value='';
		                                        }
		                                    }
		                                   
		                                }
		                                if ($(this).find('.2').val() != undefined && !$(this).find('.2').hasClass('hide')) {
		                                    value = value + "~" + $(this).find('.2').val();
		                                }
		                                if ($(this).find('.3').val() != undefined && !$(this).find('.3').hasClass('hide')) {
		                                    value = value + "~" + $(this).find('.3').val();
		                                }
		                                //alert("trttt"+finalTr[quesId]);
		                                if (quesId != undefined) {
		                                    if (finalTr[quesId.split('_')[0]] == undefined) {
		                                        finalTr[quesId.split('_')[0]] = count + '-' + value;
		                                    } else {
		                                        var val = finalTr[quesId.split('_')[0]];
		                                        val = val + ':' + count + '-' + value;
		                                        finalTr[quesId.split('_')[0]] = val;
		                                    }
		                                }
		                            });
		                        }
		                        count++;
		                    })
		                    console.log(JSON.stringify(finalTr));
		                    finalObj[questionId] = finalTr;
		                    //alert(JSON.stringify(finalTr));
		                }
		            });
		            console.log(JSON.stringify(finalObj));
		            if (JSON.stringify(finalObj) != '{}') {
		                $('#hiddenJSONdata').val(JSON.stringify(finalObj));
		            }else{
		            	  $('#hiddenJSONdata').val('');
		            }
		        //alert($('#surveyForm_farmerCropProdAnswers_farmersSectionAnswersList_0__farmersQuestionAnswersList_0__answersList_0__answer').val());
		            //jQuery("#surveyForm").submit();
		            document.getElementById("surveyForm").value = 'survey_create.action';
		        
		            if (pageAction == 'create' || '<s:property value="surveyFailure"/>'=='true') {
		                $('#hiddenCooperativeCode').attr('disabled', 'disabled');
		                  $('#hiddenFarmerId').attr('disabled', 'disabled');
		                $('#surveyId').val('');
		                 
		            } else {
		                $('#cooperativeInput').attr('disabled', 'disabled');
		                 $('#farmerInput').attr('disabled', 'disabled');
		               
		            }
		            //alert( $('#quesCodes').val());
		            //$('#quesCodes').val(quesCodes);
		          
		            $('#submitBtn').css("pointer-events", "none");
		           document.getElementById("surveyForm").submit();
		        });
		    } else {
		        $('#submitBtn').css("pointer-events", "auto");
		    }
		});
	
	/* 	<s:if test='"true".equalsIgnoreCase(surveyFailure)'>
		
	//	alert("1");
		//jQuery("#agentInput").attr("disabled",true);
		jQuery("#surveyInput").attr("disabled",true);
	
		jQuery("#proceedBtn").hide();
		jQuery("#resetBtn").hide();
		populateSurveyByAgent(document.getElementById("agentInput"));
	     showContorlsBySection('<s:property value="farmerCropProdAnswers.surveyCode"/>',true);
		//alert($('#cooperativeInput').val());
	  
		    var farmersSelected = '<s:property value="farmerCropProdAnswers.farmerIdList"/>';
		       var farmersSelectedArray = farmersSelected.split(",");			
		   for(var i=0;i<farmersSelectedArray.length;i++){
			jQuery("#farmerInput option[value='"+farmersSelectedArray[i].trim()+"']").attr("selected",true);
		    }
		
		</s:if> */
		<s:if test='selectedLevel!=null'>
			//jQuery("#levelInput").attr("disabled",true);
			//jQuery("#entityInput").attr("disabled",true);
		
		    jQuery("#cooperativeInput").attr("disabled",true);
			jQuery("#surveyInput").attr("disabled",true);
			jQuery("#farmerInput").attr("disabled",true);
			$('#questionContainer').show();
			$('#questionContainer').removeClass('hide');
			  
			jQuery("#proceedBtn").hide();
			$('#surveyInput').val('<s:property value="farmerCropProdAnswers.surveyCode"/>');					
			showContorlsBySection('<s:property value="farmerCropProdAnswers.surveyCode"/>',false);
			var farmersSelected = '<s:property value="farmerCropProdAnswers.farmerId"/>';
			
			var coopSelected = '<s:property value="farmerCropProdAnswers.cooperativeCode"/>';
			 if(!isEmpty(farmersSelected)){
				 $("#farmerInput option").each(function(){
					    $(this).prop('selected', false);
					});
				 var values = farmersSelected.split("\\,");
				  $.each(farmersSelected.split(","), function(i,e){
					
					  $("#farmerInput option[value='" + e.trim() + "']").prop("selected", true);
				  });
				  $("#farmerInput").select2();
			 }
			 if(!isEmpty(coopSelected)){
				 $("#cooperativeInput option").each(function(){
					    $(this).prop('selected', false);
					});
				 var values = coopSelected.split("\\,");
				  $.each(coopSelected.split(","), function(i,e){
					
					  $("#cooperativeInput option[value='" + e.trim() + "']").prop("selected", true);
				  });
				  $("#cooperativeInput").select2();
			 }
			
			/* var farmersSelectedArray = farmersSelected.split(",");			
			for(var i=0;i<farmersSelectedArray.length;i++){
				 $("#farmerInput option[value='" + farmersSelectedArray[i].trim() + "']").prop("selected", true);
				//jQuery("#farmerInput option[value='"+farmersSelectedArray[i].trim()+"']").attr("selected",true);
			} */
		
		</s:if>
		/* <s:else>
	
		    resetCombo("surveyInput");
	    	addOption(document.getElementById("surveyInput"), "<s:text name="txt.select"/>", "");
		</s:else> */

		<s:if test='actionMessages.size()>0'>
			jQuery(".greenDiv").hide(5000, function() {
				jQuery(this).remove();
			});
		</s:if>

		
		/*Loading dependent question based on field error from server*/
		<s:if test='fieldErrors.size()==0'>
			hideDependentQuestionIfParentExistsWhenInitialPageLoad();			
		</s:if>
		<s:else>
			loadDependentQuestionBasedOnParentQuestionValueWhenContainsFieldError();			
		</s:else>

        $("table#tablePartsialSave").on("click", ".delete", function() {
            var id = $(this).parents('tr').find('td:first').text();
            if(id==$('#surveyId').val()){
                //alert("<s:property value="%{getLocaleProperty('survey.remove"/>');
            }else{
            if(confirm("<s:property value="%{getLocaleProperty('delete.record')}"/>")){
               $.getJSON('./survey_removeSurvey?id=' + id, function(jd) {
                    var success=jd.success;
                    if(success=='1'){
                     	table.ajax.reload();
                      }
              });
            }
            }
        });

        $(".datePicker").datepicker({
	    	changeMonth: true,
			changeYear: true	
		});

    
        loadDependentQuestionBasedOnParentQuestionValueWhenContainsFieldError();

		$('#cancelBtn').on('click',function(){
			<s:if test='"1".equalsIgnoreCase(surveyEdit)'>
			document.cancelForm.action='surveyReport_list.action?surveyEdit=true'
	        document.cancelForm.submit();
			</s:if>
			<s:else>
           if(pageAction!=null&&pageAction=='edit'){
        	   document.cancelForm.action='survey_list.action'
               document.cancelForm.submit(); 
           }else{
        	   unloadQuestion();
           }
           </s:else>
		});

		
	});

     function processEntity(obj){
        
    	 resetCombo("entityInput");
    	 showContorlsBySection('',true);
         addOption(document.getElementById("entityInput"), "<s:text name="txt.select"/>", "");
         var selectedInput = obj.value.trim();
         if(selectedInput!=''){
             jQuery.post("survey_populateEntity",{dt:new Date(),selectedLevel:selectedInput},function(resp){                 
                 addOptionsToCombo("entityInput",JSON.parse(resp));
             });
         }
     }

     
     function getValue(key, array) {
         for (var el in array) {
             if (array[el].hasOwnProperty(key)) {   
                 return array[el][key];
             }
         }
    }
     
  
     function populateFarmerOraganization(surveyCode){
    	 resetCombo("cooperativeInput");
    	 addOption(document.getElementById("cooperativeInput"), "<s:text name="txt.select"/>", "");
    	 var selectedInput = surveyCode.trim(); 
    	 var coop = '<s:property value="farmerCropProdAnswers.cooperativeCode"/>';
    	
    	 if(selectedInput!=''){
             jQuery.post("survey_populateFarmerOrganization",{dt:new Date(),selectedSurveyMaster:selectedInput,selectedAgent:jQuery.trim(jQuery("#agentInput").val())},function(resp){                 
            	
            	 addOptionsToComboSelected("cooperativeInput",JSON.parse(resp),coop);
              
             });
         }
    
     }

     function populateFarmer(surveyCode){
    	 resetCombo("farmerInput");
    	 addOption(document.getElementById("farmerInput"), "<s:text name="txt.select"/>", "");
    	 var coop = '<s:property value="farmerCropProdAnswers.farmerId"/>';
    	 var selectedInput = surveyCode.trim(); 
    	 if(selectedInput!=''){
             jQuery.post("survey_populateFarmer",{dt:new Date(),selectedSurveyMaster:selectedInput,selectedAgent:jQuery.trim(jQuery("#agentInput").val())},function(resp){                 
            	 addOptionsToComboSelected("farmerInput",JSON.parse(resp),coop);
             });
         }
     }

     function resetCombo(controlId){
    	 document.getElementById(controlId).length=0;
     }

     function addOptionsToCombo(contorlId, jsonArray){
    	 for(var i=0;i<jsonArray.length;i++){
        	 addOption(document.getElementById(contorlId), jsonArray[i].value, jsonArray[i].key);
         }
     }
     function addOptionsToComboSelected(contorlId, jsonArray, valElement){
    	 var change = '0';
    	
   	 for(var i=0;i<jsonArray.length;i++){
    		 var arry  = jsonArray[i];
    		
    		 var optn = document.createElement("OPTION");
    		 optn.text = arry.value;
    		 optn.value =  arry.key;
    		 if( optn.value==valElement){
    			 
    			 optn.selected = true;
    			 change='1';
    		 }
    		 document.getElementById(contorlId).options.add(optn);
    		 
         }
    	
    	 if(change=='1'){
    		 $('#'+contorlId).trigger( "change" );
    	 }
     }
     function showContorlsBySection(sectionCodeObj,reset){
    	 $("#proceedBtn").attr("disabled",false);
    	 resetErrors();
    	 var sectionCode = '', sectionCodeVal = '';
    	 var surveyCode = '';
    	 var surveyType = '';
    	
      	 if(sectionCodeObj!=''){
	    	var sectioCodeArry=sectionCodeObj.split('~');
	    	surveyCode=sectioCodeArry[0];    	 
	        sectionCode=sectioCodeArry[1];
	        surveyType = sectioCodeArry[2];
	       
	       // jQuery("#hiddenSectionCode").val(jQuery.trim(sectionCode));
     	 }
         jQuery(".resetClass").hide();
     
         if(sectionCode!='')
         {
        	 var jsonData1; 
         	 // Show Cooperative Combo or Community Combo		
         	 if(reset)
         	 {
             	 //alert(sectionCode);
             	 var sectionArr = sectionCode.split(',');
                 for(var k=0;k<sectionArr.length;k++){
                 	sectionCode=sectionArr[k];
                 }

                 var firstLetter = sectionCode.slice(0, 1);
				 var result = sectionCode.substring(1, sectionCode.length);
				
				 
				 if(sectionCode.indexOf("D01")>-1 )
				 {
					
					 //populateFarmerOraganization(surveyCode);   
            	 	 jQuery("#cooperativeTr").show(); 
                		 jQuery("#cooperativeTr").removeClass('hide'); 
            	 	
            	 	 if(surveyType=='3'){
            	 		// alert("1");
            	 	jQuery("#cooperativeInput option[value='']").remove();
                    jQuery("#cooperativeInput").select2({
                        multiple: true
                    });
            	 	 }
				 }

					 if(sectionCode.indexOf("D02")>-1 )
					 {
						
						 //populateFarmerOraganization(surveyCode);   
	            	 	 jQuery("#cooperativeTr").show(); 
	            	 	 jQuery("#farmerTr").show(); 
	            		 jQuery("#cooperativeTr").removeClass('hide'); 
	            	 	 jQuery("#farmerTr").removeClass('hide'); 
	            	 	 if(surveyType=='3'){
	            	 		
	            	 	jQuery("#farmerInput option[value='']").remove();
	            	 	 jQuery("#cooperativeInput").select2({
	 	                        multiple: false
	 	                    });
	                    jQuery("#farmerInput").select2({
	                        multiple: true
	                    });
	            	 	 }
					 }
					
         	 }  else{
         		 
         		 if(sectionCode.indexOf("D01")>-1 )
				 {
					 //populateFarmerOraganization(surveyCode);   
            	 	 jQuery("#cooperativeTr").show(); 
            	 	 jQuery("#cooperativeTr").removeClass('hide'); 
            	 	
            	 	 if(surveyType=='3'){
            	 		jQuery("#cooperativeInput option[value='']").remove();
 	            	 	 jQuery("#cooperativeInput").select2({
 	                        multiple: true
 	                    });
 	            	 	 }
				 }
         		 if(sectionCode.indexOf("D02")>-1 )
				 {
					 //populateFarmerOraganization(surveyCode);   
            	 	 jQuery("#cooperativeTr").show(); 
            	 	 jQuery("#cooperativeTr").removeClass('hide'); 
            	 	 jQuery("#farmerTr").removeClass('hide'); 
            	 	 jQuery("#farmerTr").show(); 
            	 	 jQuery("#cooperativeInput").select2({
	                        multiple: false
	                    });
            	 	 if(surveyType=='3'){
            	 		 
            	 		jQuery("#farmerInput option[value='']").remove();
 	            	 	 jQuery("#farmerInput").select2({
 	                        multiple: true
 	                    });
 	            	 	 }
				 }
				
         	 }       	 
         }

         
        
         
     }

     function populateFarmerByCooperative(data){
    	// alert(data);
    	 resetErrors();
         var sectionCode = '';
         var surveyType = '';
    	 var surveyObj=jQuery("#surveyInput").val();
    	 var coop = '<s:property value="farmerCropProdAnswers.farmerId"/>';
    	/*  if(coop=='' && '<s:property value="farmerCropProdAnswers.farmerIdList"/>'!=''){
    		 coop = '<s:property value="farmerCropProdAnswers.farmerIdList"/>';
    	 } */
      	if(surveyObj!=''){
	    	var sectioCodeArry=surveyObj.split('~');   	 
	        sectionCode=sectioCodeArry[1];
	        surveyType = sectioCodeArry[2];
	        jQuery("#farmerInput").select2('val','');
     	}else{
     		 jQuery("#farmerInput").select2({
                 multiple: false
             });
     	}
      	
       		 resetCombo("farmerInput");
       		 if(!$('#farmerInput').prop("multiple")){
       	
        	 addOption(document.getElementById("farmerInput"), "<s:text name="txt.select"/>", "");
       		 }
       		
     		 	 var selectedInput = data.trim(); 
        	
        	 if(selectedInput!=''){
                 jQuery.post("survey_populateFarmerByCooperative",{dt:new Date(),selectedCooperative:jQuery.trim(data)},function(resp){                 
                	 addOptionsToComboSelected("farmerInput",JSON.parse(resp),coop);
                 });
             };
     	
     	
     }

     function Controls(){
    	 jQuery("#farmerInput").selectedIndex=0;
    	 jQuery("#cooperativeInput").selectedIndex=0;
    
     }

     function validateContorls(){
         var isError = false;   
         var sectionSurveyMasterCode=jQuery("#surveyInput").val();
         if(jQuery.trim(sectionSurveyMasterCode)=='')
        	 sectionSurveyMasterCode='~';
         var sectionCode=sectionSurveyMasterCode.split('~')[1];
         jQuery(".errorDivFields").html('&nbsp');      
        
        // jQuery("#cooperativeErrorDiv").html('&nbsp');
         if(!jQuery("#cooperativeTr").is(":hidden")){
        	 var cooperativeSelectedVal = jQuery.trim(jQuery("#cooperativeInput").val());
        	 if(pageAction==null||pageAction==''||pageAction=='create'){
	             if(cooperativeSelectedVal==""){
	            	 isError = true;
	            	 jQuery(".errorDivFields").append('<font color="red"><s:text name="empty.cooperative"/></font>');
	             }else{
	            	 
	            	 if(!jQuery("#farmerTr").is(":hidden")){
	                     var farmerSelectedVal = jQuery("#farmerInput").val();
	                     if(farmerSelectedVal==null || farmerSelectedVal==""){
	                    	isError = true;
	         				jQuery(".errorDivFields").append('<font color="red"><s:text name="empty.farmer"/></font>');
	                     }
	                 }
	             }
        	 }
         }
         
        
        
         return isError;
     }

     function resetErrorDivs(){
    	/*  jQuery("#errorDiv").html('&nbsp;');
    	 jQuery("#farmerErrorDiv").html('&nbsp;'); */
    	 jQuery(".errorDivFields").html('&nbsp;');    	 
     }

     function moveToError(errorDiv){    	 
    	 jQuery(window).scrollTop(jQuery("#"+errorDiv).offset().top);
     }

	 function showOthersTxt(othersCombo1, othersCombo2){
		// alert(jQuery(othersCombo1).val());
		// alert(othersCombo2);
		 if(jQuery(othersCombo1).val() == othersCombo2){
			 jQuery("#"+jQuery(othersCombo1).attr("id")+"_txt").show();
		 }else{
			 jQuery("#"+jQuery(othersCombo1).attr("id")+"_txt").val('');
			 jQuery("#"+jQuery(othersCombo1).attr("id")+"_txt").hide();
		 }
	 }

	 function showUnitsOthersTxt(unitOthersCombo,unitOtherCatCode){
		 //alert(unitOthersCombo,unitOtherCatCode);
		 if(jQuery(unitOthersCombo).val() == unitOtherCatCode){
			 jQuery("#"+jQuery(unitOthersCombo).attr("id")+"_txt").removeClass('hide');
			 jQuery("#"+jQuery(unitOthersCombo).attr("id")+"_txt").show();
		 }else{
			 jQuery("#"+jQuery(unitOthersCombo).attr("id")+"_txt").val('');
			 jQuery("#"+jQuery(unitOthersCombo).attr("id")+"_txt").hide();
		 }
	 }


	 function showMultiSelectOthersTxt(othersMultiCombo, othersCatCode){
			 var con = jQuery(othersMultiCombo).val();
			 var arry = con.toString().split(",");
			 var otherValueExists =false;
				
			for (var i=0; i <arry.length;i++){
					if(arry[i]==othersCatCode){
						otherValueExists =true;
					}
			}	
					
			 if(otherValueExists){			
				 jQuery("#"+jQuery(othersMultiCombo).attr("id")+"_txt").show();
			 }else{
				 jQuery("#"+jQuery(othersMultiCombo).attr("id")+"_txt").val('');
				 jQuery("#"+jQuery(othersMultiCombo).attr("id")+"_txt").hide();
			 }
		 }

	
	 function showRadioButtonOthersTxt(othersRadio, othersCatCode){		
		 if(jQuery(othersRadio).val() == othersCatCode){			 
			 jQuery("#radio_others_"+othersCatCode+"_txt").show();
		 }else{
			 jQuery("#radio_others_"+othersCatCode+"_txt").val('');
			 jQuery("#radio_others_"+othersCatCode+"_txt").hide();			 
		 }
	 }

	 function showCheckboxOthersTxt(othersCheckbox, othersCatCode){		

		 var otherValueExists =false;
		
		 jQuery("input[name='"+jQuery(othersCheckbox).attr("name")+"']:checked").each(function(){					
			 if(jQuery(this).val()==othersCatCode){	
						otherValueExists =true;
			}
		 });		 
		
		 if(otherValueExists){			
			 jQuery("#checkbox_others_"+othersCatCode+"_txt").removeClass("hide");
		 
			  jQuery("#checkbox_others_"+othersCatCode+"_txt").show();
		 }else{
			 jQuery("#checkbox_others_"+othersCatCode+"_txt").val('');
			 jQuery("#checkbox_others_"+othersCatCode+"_txt").addClass("hide");
			 jQuery("#checkbox_others_"+othersCatCode+"_txt").hide();			
		 }
		
	 }
	 
	 function unloadQuestion(){
	 	jQuery("#questionsDiv").html("&nbsp;");
	 	jQuery("#proceedBtn").show();
	    $('#questionContainer').addClass('hide');
	 	jQuery("#surveyInput").select2("val", "");
		jQuery("#cooperativeInput").select2("val", "");
	
		jQuery("#farmerInput").select2("val", "");
		jQuery("#surveyInput").removeAttr("disabled");	
	
		jQuery("#cooperativeInput").removeAttr("disabled");
		jQuery("#farmerInput").removeAttr("disabled");	
			jQuery(".resetClass").hide();
		$('.subForm').find('input').each(function(){
		      
		      $(this).val('');
		});
		$('.subForm').find('select').each(function(){
		      
		      $(this).prop('selectedIndex', 0);
		});
		$('#quesCodes').val('');
		$('#hiddenJSONdata').val('');
	 	resetErrors();
	 	resetErrorDivs();
	 	$("#proceedBtn").attr("disabled",false);
		$("#farmerTr").hide();
		$("#farmerTr").addClass("hide");
		$("#cooperativeTr").addClass("hide");
		$("#cooperativeTr").hide();
	
	 }    

	 function resetErrors(){

		 jQuery(".errorDivFields").html('&nbsp;'); 
	 }

	 function hideDependentQuestionIfParentExistsWhenInitialPageLoad(){
		 jQuery(".dependent").each(function(){
			var classes = jQuery(this).attr("class").split(" ");
			for(var i=0;i<classes.length;i++){
				if (classes[i].slice(0,13) == 'dependent_tr_'){
					var parentId = classes[i].slice(13);
					if(jQuery(".parent_tr_"+parentId).length>0){
						jQuery(this).hide();
					}
				}
			}
		});
	 }

	 function loadDependentQuestionBasedOnParentQuestionValueWhenContainsFieldError(){
		jQuery(".parent").each(function(){
			var parentTrRowId = jQuery(this).attr("id");
			var parentQuestionId = parentTrRowId.substr(9);				
			var dependencyKeyHiddenValue =  jQuery("#dependencyKeyHidden_"+parentQuestionId).val();
			showDependentQuestion(jQuery(".parent_component_"+parentQuestionId),parentQuestionId,dependencyKeyHiddenValue,false);
		});
	 }
	 
	 function showDependentQuestion(obj, parentId, dependencyKey,isReset){
		 var selectedValue = [];
		 var commSepVale='';
		 if(jQuery(obj).is("input:radio") || jQuery(obj).is("input:checkbox")){
			 if(jQuery(obj).is("input:checkbox")){
				 var name = jQuery(obj).attr("name");
				 $("input[name='"+name+"']").filter(':checked').each( function () {
					 selectedValue.push($(this).val())
				 });
			 }else{
				 selectedValue.push(jQuery(obj).filter(':checked').val());
			 }
		 
		 }else if(jQuery(obj).is("select")){
			 selectedValue.push(jQuery(obj).val());
			
		 }
		  var arr = [];
	         if (dependencyKey.indexOf(',') >= 0) {
	             arr = dependencyKey.split(',');
	             
	             for (var i = 0; i <  arr.length; i++) {
	            	 jQuery(".dependent_tr_"+parentId+'_'+arr[i].trim()).find('input, select, textarea').each(function(){
	            		if( isReset){
	     			    if ($(this).is(".multipleSelectClass")){
	     			      makeMultiSelect($(this));
	     	    		  //$(this).multiselect('disable');
	     	     	    }
	     			    if($(this).is(':radio') || $(this).is(':checkbox')){
	     			    	$(this).prop('checked', false)
	     				
	     		      }else if($(this).is(':hidden')){
	     			    	$(this).prop('checked', false)
	     				
	     		      }else{
	     		    	  $(this).val('');
	     		      }
	            		}
	     				
	     		 });
	            	
	            	 jQuery(".dependent_tr_"+parentId+'_'+arr[i].trim()).hide();
	             }
		 }else{
	             arr.push(dependencyKey);
	             jQuery(".dependent_tr_"+parentId+'_'+dependencyKey.trim()).find('input, select, textarea').each(function(){
	            		if( isReset){
	     			    if ($(this).is(".multipleSelectClass")){
	     			      makeMultiSelect($(this));
	     	    		  //$(this).multiselect('disable');
	     	     	    }
	     			    if($(this).is(':radio') || $(this).is(':checkbox')){
	     			    	$(this).prop('checked', false)
	     				
	     		      }else if($(this).is(':hidden')){
	     			    	$(this).prop('checked', false)
	     				
	     		      }else{
	     		    	  $(this).val('');
	     		      }
	            		}
	     				
	     		 });
	               jQuery(".dependent_tr_"+parentId+'_'+dependencyKey.trim()).hide();
	         }

	         if(selectedValue.length>0){
	         for (var i = 0; i <  arr.length; i++) {
	       
	        	 if(selectedValue.indexOf(arr[i].trim()) >= 0	 ){

			 var clss = ".dependent_tr_"+parentId+"_"+arr[i].trim();
		  jQuery(clss).css('display', 'table-row');
		  $('#dependencyKeyHidden_'+parentId).val(arr[i].trim());
		  jQuery(clss).removeAttr('style');
			jQuery(clss).show();
		  if(jQuery(clss).hasClass('parent')){
			
				var parentTrRowId = jQuery(clss).attr("id");
				var parentQuestionId = parentTrRowId.substr(9);				
				var dependencyKeyHiddenValue =  jQuery("#dependencyKeyHidden_"+parentQuestionId).val();
				//  alert("11"+parentQuestionId);
				showDependentQuestion(jQuery(".parent_component_"+parentQuestionId),parentQuestionId,dependencyKeyHiddenValue,false);
				}
		  
		  }
		 
		 
		 
		 
	         }
		 }
	 }

	 function resetDependency(parentId,depe){
		 jQuery(".dependent_component_to_"+parentId+"_"+depe).each(function(){
			 if(jQuery(this).is("input:text")){
				 jQuery(this).val("");
			 }else if(jQuery(this).is("select")){
				 if(jQuery(this).is(".multipleSelectClass")){
					jQuery(this).val("");
				 }else{
				    jQuery(this)[0].selectedIndex=0;
				 }
			 }else if(jQuery(this).is("input:radio")){
				 jQuery(this).removeAttr("checked");
			 }
			 // To undisplay third level questions from first level question if invalid answer
			 if(jQuery(".dependent_tr_"+parentId+"_"+depe).hasClass("parent")){
				 var parentTrRowId = jQuery(".dependent_tr_"+parentId+"_"+depe).attr("id");
				 var parentQuestionId = parentTrRowId.substr(9).split('_')[0];	
				 var dependencyKeyHiddenValue =  jQuery("#dependencyKeyHidden_"+parentQuestionId).val();
				 showDependentQuestion(jQuery(".parent_component_"+parentQuestionId),parentQuestionId,depe,false);
			 }
		 });
	 }
	 function handleNA(evt){
		 $(evt).closest('tr').find('td:nth-child(6), td:nth-child(8)').find('input, select, textarea').each(function(){
			    if ( $(this).is( ".multipleSelectClass" ) ){
	    		  $(this).multiselect(evt.checked ? 'disable' : 'enable');
	     	    }
			    if (!$(this).is( ".idk" ) ){
			    	 if($(this).is(':radio') || $(this).is(':checkbox')){
					    	$(this).prop('checked', false)
						
				      }else if($(this).is(':hidden')){
					    	
						
				      }else{
				    	  $(this).val('');
				      }
				 $(this).prop('disabled', evt.checked);
				 hideDepeOnNaAndIDK(this);
			    }
		 });

		 var id=$(evt).closest('tr').attr('id').split('_')[1];
		 if(evt.checked){
	       $('#minusSurvey_'+id).click();
	       
		 }
		 $('#plusSurvey_'+id).prop('disabled', evt.checked);
	   }
	 function hideDepeOnNaAndIDK(obj){
		 if($(obj).is('[class^="parent_component_"]')){
				
				var parentTrRowId = $(obj).attr("id");
				var classname = jQuery(obj).attr("class").split("parent_component_");
				if(classname.length > 0){
				$( "[class*='dependent_tr_'"+classname[0].trim()+"]" ).each ( function () {
					var obbj = $(this).find("[class*='dependent_component_to_'"+classname[0].trim()+"]");
				  if($(obbj).is(':radio')){
	     			    	$(obbj).prop('checked', false)
	     				
	     		      }else if($(obbj).is(':hidden')){
	     			    	
	     				
	     		      }else{
	     		    	  $(obbj).val('');
	     		      }
					$(this).hide();
				});
				}
				//showDependentQuestion(jQuery(".parent_component_"+parentQuestionId),parentQuestionId,dependencyKeyHiddenValue);
			
	    }
		 
	 }
	 function checkNAColumn(obj){
		 var id=$(obj).closest('tr').attr('id').split('_')[1];
		 $(obj).closest('tr').find('td:nth-child(6), td:nth-child(8)').find('input, select, textarea').each(function(){
			   if ( $(this).is( ".multipleSelectClass" ) ){
				  makeMultiSelect($(this));
	    		  $(this).multiselect('disable');
	     	    }
			   if($(this).is(':radio') || $(this).is(':checkbox')){
			    	$(this).prop('checked', false)
				
		      }else if($(this).is(':hidden')){
			    	
				
		      }else{
		    	  $(this).val('');
		      }
				$(this).prop('disabled', true);
				 hideDepeOnNaAndIDK(this);
				// $(this).trigger("change");
		 });
		   $('#minusSurvey_'+id).click();
		   
			 $('#plusSurvey_'+id).prop('disabled', true);
	   }
	 
   function handleIdk(evt){
	 $(evt).closest('td').find('.idk').prop('disabled', !evt.checked);
	 var id=$(evt).closest('tr').attr('id').split('_')[1];
	 if(evt.checked){
       $('#minusSurvey_'+id).click();
       
	 }
	 $('#plusSurvey_'+id).prop('disabled', evt.checked);
	 $(evt).closest('tr').find('td:nth-child(6), td:nth-child(7)').find('input, select, textarea').each(function(){
	    	 if ( $(this).is( ".multipleSelectClass" ) ){
	    		 makeMultiSelect($(this));
	    		 $(this).multiselect(evt.checked ? 'disable' : 'enable');
	     	 }
	    	 if($(this).is(':radio') || $(this).is(':checkbox')){
			    	$(this).prop('checked', false)
				
		      }else if($(this).is(':hidden')){
			    	
				
		      }else{
		    	  $(this).val('');
		      }
			$(this).prop('disabled', evt.checked);
			 hideDepeOnNaAndIDK(this);
	 });
   }
   function checkIdkCoumn(obj){
		 $(obj).closest('td').find('.idk').prop('disabled', false);
		 var id=$(obj).closest('tr').attr('id').split('_')[1];
		 $(obj).closest('tr').find('td:nth-child(6)').find('input, select, textarea').each(function(){
			    if ($(this).is(".multipleSelectClass")){
			      makeMultiSelect($(this));
	    		  $(this).multiselect('disable');
	     	    }
			    if($(this).is(':radio') || $(this).is(':checkbox')){
			    	$(this).prop('checked', false)
				
		      }else if($(this).is(':hidden')){
			    	
				
		      }else{
				$(this).val('');
		      }
				$(this).prop('disabled', true);
				 hideDepeOnNaAndIDK(this);
		 });
		 $(obj).closest('tr').find('td:nth-child(7)').find('input').each(function(){
			    if ($(this).is(".multipleSelectClass")){
			      makeMultiSelect($(this));
	    		  $(this).multiselect('disable');
	     	    }
				$(this).prop('disabled', true);
		 });

		
		       $('#minusSurvey_'+id).click();
		   
			 $('#plusSurvey_'+id).prop('disabled', true);
	   }

   function makeMultiSelect(element){
	   $(element).multiselect({
			classes:"multiselect-mystyle",
			checkAllText:"<s:property value="%{getLocaleProperty('checkAllText')}" />",
			uncheckAllText:"<s:property value="%{getLocaleProperty('uncheckAllText')}" />",
			noneSelectedText:"<s:property value="%{getLocaleProperty('noneSelectedText')}" />",
			selectedText:"<s:property value="%{getLocaleProperty('selectedText')}" />",
		});		
   }


   function checkImage(){
	   var size = '<s:property value="questions.size" />';
	   var count=0;
	   var fileCount = 0;
	   var isError = false;
	   for(i=0;i<parseInt(size);i++){
		    var name= [];
		    if(document.getElementById('image'+i)!=null){
			var file=document.getElementById('image'+i).files[0];	
			var name=document.getElementById('image'+i).value.split('\\');
			var filename = name[parseInt(name.length)-1];	
			var fileExt=filename.split('.').pop();	
			if(file != undefined){
			if(fileExt=='jpg' || fileExt=='jpeg' || fileExt=='png'||fileExt=='JPG'||fileExt=='JPEG'||fileExt=='PNG')		
		    { 	
				if(file.size>100001){
					  isError = true;
	  				 jQuery("#errorimage"+i).html("<s:property value="%{getLocaleProperty('fileSizeExceeds')}" />");
					}
				}else{
					 isError = true;
	  				 jQuery("#errorimage"+i).html("<s:property value="%{getLocaleProperty('invalidFileExtension')}" />");	
				}
			
			}
       }
	   }
	   
	   return isError;// Prevent browser from visiting `#`
	   
   }
   function getVerifiactionImage(id,obj){
	   var str =$(obj).attr("id").split("_")[1]; 
	   var coupeCompletionReport = $('#byte_'+str).val();
		$('#baseDiv').show();
	    var heading ="<s:property value="%{getLocaleProperty('heading')}" />";
	   
	    	var content="<img id='imagePopup' src='#' width='100%' height='100%'/>";
       
		enablePopup(heading,content,coupeCompletionReport);
	   
   }

 
   function removeVerifiactionImage(obj){
	    $('#'+obj).val('');
	    $('#'+obj).hide();
	   $('#byte_'+obj).val('');
	   $('#view_'+obj).hide();
	   $('#file_'+obj).show();
	   
  }
   function showSubFormTR(obj){
	    var id = $(obj).attr("id");
	   var ids = id.split('_');
	     $('.table_sub_question_'+ids[1]).removeClass('hide');
	     $(obj).hide();
	     $('#minusSurvey_'+ids[1]).show();
	     $('#minusSurvey_'+ids[1]).removeClass("hide")
 }

   function hideSubFormTR(obj){
	    var id = $(obj).attr("id");
	    var ids = id.split('_');
	    $('#plusSurvey_'+ids[1]).show();
	    $(obj).hide();
	    $('.table_sub_question_'+ids[1]).addClass('hide');
	  
	   
}


   function loadCustomPopup(){
   	$overlay = $('<div id="modOverlay"></div>');
   	$modal = $('<div id="modalWin" class="ui-body-c" style="position: fixed; top:50%; left:50%;"></div>');
   	$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
   	$close = $('<button id="clsBtn" class="btnCls">X</button>');
   	
   	$modal.append($slider, $close);
   	$('body').append($overlay);
   	$('body').append($modal);
   	$modal.hide();
   	$overlay.hide();

   	jQuery("#clsBtn").click(function () {
       	$('#modalWin').css('margin-top','-230px');	
   		$modal.hide();
   		$overlay.hide();			
   		$('body').css('overflow','visible');
   	});
   }


   function enablePopup(head, cont,str){
   	$(window).scrollTop(0); 
   	$('body').css('overflow','hidden');
   	$(".bjqs").empty();		
   	var heading='';
   	var contentWidth='100%';
   	if(head!=''){
   		heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
   		contentWidth='92%';
   	}
   	var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
   	$(".bjqs").append('<li>'+heading+content+'</li>')
   	if(str != ''){
   	 $('#imagePopup').prop("src", "data:image/jpeg;base64,"+str);
   	}
   	$(".bjqs-controls").css({'display':'block'});
   	$('#modalWin').css('margin-top','-260px');
   	$modal.show();
   	$overlay.show();
   	jQuery('#banner-fade').bjqs({
           height      : 450,
           width       : 600,
           showmarkers : false,
           usecaptions : true,
           automatic : true,
           nexttext :'',
           prevtext :'',
           hoverpause : false                                           

       });
   }

   function addRowSubForm(quesId) {
	   console.log(quesId);
       var val = $('#subFormQn_'+quesId).val();
       var count = jQuery('.table_sub_' + quesId + ' tr').length;
      console.log(val);
       val = val.replace(/~/g, count);
       console.log(val);
       jQuery('.table_sub_question_' +quesId).append(val);
       $(".datePickerSub").datepicker({
	    	changeMonth: true,
			changeYear: true	
		});
       $('.chg').trigger("change");
	    
   }

   function removeRowSubForm(obj) {
       $($(obj).closest("tr")).remove()
   }

   function setValueById(obj, value) {
	     $(obj).removeClass('hide');
	     console.log("id"+$(obj).closest('td').attr("id"));
	     if ($(obj).is('input:radio')) {
	         $(obj).each(function() {
	             if (value == $(this).val()) {
	                 $(this).prop('checked', 'checked');
	             }
	         });
	     } else if ($(obj).is('input:checkbox')) {
	         // alert('val'+value);
	         var arr = [];
	         if (value.indexOf(',') >= 0) {
	             arr = value.split(',');
	         } else {
	             arr.push(value);
	         }
	         $(obj).each(function() {
	             // alert($(this).val()+','+ arr.indexOf($(this).val()));  
	             if (arr.indexOf($(this).val()) >= 0) {
	                 $(this).prop('checked', 'checked');
	             }
	         });
	     } else {
	         var arr = [];
	         if (value.indexOf(',') >= 0) {
	             $.each(value.split(","), function(i, e) {
	                 $(obj).find("option[value='" + e + "']").prop("selected", true);
	             });
	         } else {
		       console.log("vv"+value);
	             $(obj).val(value);
	         }
	     }
	 }

   function validationData(jsonDataSub){
	   console.log("dt"+jsonDataSub);
	   if (jsonDataSub != '{}' && jsonDataSub != '') {
           var formJSONData = $('#hiddenJSONdata').val();
           var obj = $.parseJSON(jsonDataSub);
           console.log("on" + obj);
           $.each(obj, function(key, value) {
               console.log(value);
               $.each(value, function(key1, value1) {
                   var valTr = [];
                   if (value1.indexOf(':') >= 0) {
                       valTr = value1.split(':');
                   } else {
                       valTr.push(value1);
                   }
                   // console.log("valtr"+valTr);
                   for (var j = 0; j < valTr.length; j++) {
                       var valueTr = valTr[j].split('-',-1)[1];
                       var actualtRow = valTr[j].split('-',-1)[0];
                       var valTd = [];
                      
                       if (valueTr.indexOf('~') >= 0) {
                           valTd = valueTr.split('~',-1);
                       } else {
                           valTd.push(valueTr);
                       }
                       console.log("valtd" + valTd);
                       if (document.getElementById(key1 + '_' + (actualtRow)) == null) {
                          // alert("kkk"+key1 + '_' + (actualtRow));
                           addRowSubForm(key);
                       }
                       //  alert(key1 + '_' + (i + 1));
                       $('#table_' + key).removeClass('hide');
                       $('#plusSurvey_'+key).hide();
              	     $('#minusSurvey_'+key).show();
                       for (var k = 0; k < valTd.length; k++) {
                           setValueById($('#table_' + key).find('#' + key1 + '_' + (actualtRow)).find('.' + (k + 1)), valTd[k]);
                       }
                   }
               });
           });
       }
   }

   function isNumber(evt) {
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	    	      return false;
	    return true;
	}


   function isDecimal(evt,obj) {
   	
   	    var value=obj.value;
   	    var charCode = (evt.which) ? evt.which : event.keyCode
    		 if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode!=46)
    		        return false;
    		    if(value.indexOf('.')!=-1 && charCode==46)
    		         return false;
    		    return true;
   }
   
   function showImgInfo(imgInfoId,title){
	   $('#baseDiv').show();
	   var cont = $("#"+imgInfoId).text();	  
	   enableInfoPopup(title, cont);
   }
   
   
   function enableInfoPopup(head, cont){
	   	//$(window).scrollTop(0); 
	   	$('body').css('overflow','hidden');
	   	$(".bjqs").empty();		
	   	var heading='';
	   	var contentWidth='100%';
	   	if(head!=''){
	   		heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
	   		contentWidth='92%';
	   	}
	   	var content="<div style='width:100%;height:"+contentWidth+"; overflow-y: scroll; height:410px; overflow-x: auto;'>"+cont+"</div>";	
	   	$(".bjqs").append('<li>'+heading+content+'</li>')	   
	   	$(".bjqs-controls").css({'display':'block'});
	   	$('#modalWin').css('margin-top','-260px');
	   	$modal.show();
	   	$overlay.show();
	   	jQuery('#banner-fade').bjqs({
	           height      : 450,
	           width       : 600,
	           showmarkers : false,
	           usecaptions : true,
	           automatic : true,
	           nexttext :'',
	           prevtext :'',
	           hoverpause : false                                           

	       });
	   }
</script>
</body>