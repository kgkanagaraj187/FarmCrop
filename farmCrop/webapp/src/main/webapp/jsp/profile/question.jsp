<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

<style>
/* 
.flexform-item {
  display: inline-flex;
  width: 50%;
  margin-bottom: -1px;
  margin-left: -1px; }
  */
.ui-widget-header {
	background: url("images/ui-bg_gloss-wave_55_5c9ccc_500x100.png")
		repeat-x scroll 50% 50% #5c9ccc !important;
}

.pickListClass td {
	border: none !important;
}

.error {
	color: #fd0000;
	font-size: 12px;
	margin-bottom: 10px;
	padding: 5px 10px 5px 0;
	text-align: left;
	width: auto;
}

.formulaDivContainer {
	width: 98%;
	background: #F6F6FA;
	padding: 1%;
}

.btC {
	border: solid 1px #939585;
	background: #cbf2a8; /* Old browsers */
	padding: 3px 8px;
}

#question {
	width: 300px;
}

#selectedQuestions {
	width: 300px;
}

#sortable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 100%;
}

#sortable li {
	margin: 0 3px 3px 3px;
	padding: 0.4em;
	padding-left: 1.5em; /*font-size: 1.4em;*/
	font-size: 12px;
	height: 40px;
}

#sortable li span {
	position: absolute;
	margin-left: -1.3em;
	cursor: pointer;
}
</style>
</head>

<body>

	<s:form name="form" cssClass="fillform" id="form"
		action="question_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden name="question.id" />
		</s:if>
		<s:hidden key="command" />

		<s:hidden id="dataLvl" key="dataLevel" />

		<div class="appContentWrapper marginBottom">

			<div class="error">
				<sup>*</sup>
				<s:text name="reqd.field" />
				<s:actionerror />
				<s:fielderror />
			</div>

			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('info.question')}" />
				</h2>
				<div class="flexform">
					<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="flexform-item">
							<label for="txt"><s:text name="question.code" /> </label>
							<div class="form-element">
								<s:property value="question.code" />
								<s:hidden key="question.code" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"><s:text name="question.serialNo" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:property value="question.serialNo" />
								<s:hidden name="question.serialNo" />
							</div>
						</div>

					</s:if>


					<div class="flexform-item">
						<label for="txt"> <s:text name="question.name" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield name="question.name" theme="simple"
								cssClass="upercls form-control" />

						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"> <s:text name="question.questionType" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.questionType"
								cssClass="col-sm-4 form-control select2" list="questionTypesMap"
								listKey="key" listValue="value" headerKey="" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="controlComponentsByQusType(this);listQuestions();"
								id="questionType" />

						</div>
					</div>

					<%-- <div class="flexform-item">
							<label for="txt">
							<s:text name="country.name" /><sup style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<s:select name="selectedCountry"
									cssClass="col-sm-4 form-control select2"
									list="countryList" listKey="id" listValue="name"
									headerKey="" theme="simple"
									headerValue="%{getText('txt.select')}" onchange="listState(this)" id="country"/>
								
							</div>
						</div> --%>

					<div class="flexform-item">
						<label for="txt"> <s:text name="question.section" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.section.id"
								cssClass="col-sm-4 form-control select2" list="qnSectionsMap"
								listKey="key" listValue="value" headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="listQuestions();" id="section" />

							<button type="button" id="addSectionDetail"
								class="addBankInfo slide_open btn btn-sm btn-success"
								data-toggle="modal" data-target="#sectionModal"
								onclick="buttonSectionCancel()">
								<i class="fa fa-plus" aria-hidden="true"></i>
							</button>
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="question.info" /> </label>
						<div class="form-element">
							<s:textfield cssClass="form-control input-sm"
								name="question.info" theme="simple" maxlength="45" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"> <s:text name="question.isMandatory" />
						</label>
						<div class="form-element">
							<s:radio id="mandatory" list="mandatoryTypes"
								name="question.mandatory" value="%{question.mandatory}"
								listKey="key" listValue="value" />
						</div>
					</div>

					<div class="flexform-item" id="dataColl">
						<label for="txt"> <s:text name="question.dataCollection" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.dataCollection"
								cssClass="col-sm-4 form-control select2"
								list="dataCollectionsMap" listKey="key" listValue="value"
								headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="showElementsByDataCollection();" id="dataCollection" />

						</div>
					</div>

					<div class="normal flexform-item">
						<label for="txt"> <s:text name="question.component" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.componentType"
								cssClass="col-sm-4 form-control select2"
								list="componentTypesMap" listKey="key" listValue="value"
								headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="showElementsByComponentType(false);"
								id="componentType" />
						</div>
					</div>

					<div class="flexform-item" id="collType">
						<label for="txt"> <s:text name="question.collectionType" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.collectionType"
								cssClass="col-sm-4 form-control select2"
								list="collectionTypesMap" listKey="key" listValue="value"
								headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="showElementsByComponentType(false);"
								id="collectionType" />
						</div>
					</div>


					<div class="normal flexform-item" id="valType">
						<label for="txt"> <s:text name="question.validationType" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select name="question.validationType"
								cssClass="col-sm-4 form-control select2"
								list="validationTypesMap" listKey="key" listValue="value"
								headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}"
								onchange="showElementsByValidationType()" id="validationType" />
						</div>
					</div>

					<div id="df" class="normal flexform-item">
						<label for="txt"> <s:text name="question.dataFormat" />
						</label>
						<div class="form-element">
							<s:select name="question.dataFormat"
								cssClass="col-sm-4 form-control select2" list="dataFromatList"
								headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="dataFormat" />
						</div>
					</div>

					<div class="normal flexform-item" id="maxLength">
						<label for="txt"> <s:text name="question.maxLength" />
						</label>
						<div class="form-element" id="max">
							<s:textfield name="question.maxLength" theme="simple"
								cssClass="upercls form-control" maxlength="5" />
						</div>
					</div>

					<div class="flexform-item" id="listMethodtr">
						<label for="txt"> <s:text name="question.listMethodName" />
						</label>
						<div class="form-element">
							<s:select name="question.listMethodName"
								cssClass="col-sm-4 form-control select2" list="listMethodsMap"
								headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="listMethod"
								onchange="listMethodChangeControl();" />
						</div>
					</div>
					
						<div class="flexform-item" id="minLength">
						<label for="txt"> <s:text name="question.minRange" />
						</label>
						<div class="form-element">
							<s:textfield id="minRangeField" name="question.minRange"
						theme="simple" maxlength="5" onkeypress="return isNumber(event)"
						onblur="setMaxLengthVal();" />
						</div>
					</div>
					<div class="flexform-item" id="maxRange">
						<label for="txt"> <s:text name="question.maxRange" />
						</label>
						<div class="form-element">
						<s:textfield id="maxRangeField" name="question.maxRange"
						theme="simple" maxlength="5" onkeypress="return isNumber(event)"
						onblur="setMaxLengthVal();" />
						</div>
					</div>
					
					<div class="flexform-item na">
						<label for="txt"> <s:text name="question.defaultValue" />
						</label>
						<div class="form-element">
							<s:select name="question.defaultValueCode"
								cssClass="col-sm-4 form-control select2" list="defaultValuesMap"
								listKey="key" listValue="value" headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="defaultValue" />
						</div>
					</div>

					<div class="flexform-item na">
						<label for="txt"> <s:text name="question.defaultUnit" />
						</label>
						<div class="form-element">
							<s:select name="question.defaultUnit"
								cssClass="col-sm-4 form-control select2" list="selectedUnits"
								listKey="key" listValue="value" headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="defaultUnit" />
						</div>
					</div>

					<div class="flexform-item na" id="otherCat">
						<label for="txt"> <s:text
								name="question.otherCatalogValue" />
						</label>
						<div class="form-element">
							<s:select name="question.otherCatalogValue"
								cssClass="col-sm-4 form-control select2"
								list="selectedAnswerKeys" listKey="key" listValue="value"
								headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="otherCatalogue" />
						</div>
					</div>

					<div class="flexform-item na" id="unitOtherCat">
						<label for="txt"> <s:text
								name="question.unitOtherCatalogValue" />
						</label>
						<div class="form-element">
							<s:select name="question.unitOtherCatalogValue"
								cssClass="col-sm-4 form-control select2" list="selectedUnits"
								listKey="key" listValue="value" headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="unitOtherCatalogue" />
						</div>
					</div>
			

					<div class="flexform-item dependency">
						<label for="txt"> <s:text name="question.dependencyKey" />
						</label> <sup style="color: red;">*</sup>
						<div class="form-element">
							<s:select name="question.dependencyKey"
								cssClass="col-sm-4 form-control select2" multiple="true"
								list="selectedAnswerKeys" listKey="key" listValue="value"
								headerKey=" " theme="simple"
								headerValue="%{getText('txt.select')}" id="dependencyKey" />
						</div>
					</div>
<div class="flexform-item odd dependency">
						<label for="txt"> <s:text name="question.parentQuestion.dependancy" />
						</label> <sup style="color: red;">*</sup>
						<div class="form-element">
						<s:select name="question.parentDepen" cssClass="col-sm-4 form-control select2"
						list="dependancyKeysList" listKey="key" listValue="value" multiple="true"
						 id="parentDepen"
						theme="simple" />
						</div>
					</div>
					<div class="flexform-item  dependency">
						<label for="txt"> <s:text name="question.parentQuestion" />
						</label> <sup style="color: red;">*</sup>
						<div class="form-element">
							<s:select name="question.parentQuestion.id"
								cssClass="col-sm-4 form-control select2" list="parentQuestions"
								listKey="key" listValue="value" headerKey="-1" theme="simple"
								headerValue="%{getText('txt.select')}" id="parentQuestion" />
						</div>
					
				</div>
				
				

				<div class="trOnce flexform-item">
					<label for="txt"> <s:text name="question.entityProperty" />
					</label>
					<div class="form-element">
						<%-- <label for="question.entityColumn" id="lblEnCol"><s:property
								value="question.section.dataLevel.name" /></label> --%>
						<s:select name="question.entityColumn"
							cssClass="col-sm-4 form-control select2" list="fieldNames"
							listKey="key" listValue="value" headerKey="" theme="simple"
							headerValue="%{getText('txt.select')}" id="entityColumn" />
					</div>
				</div>


					<div class="flexform-item" id="quesStat">
						<label for="txt"> <s:text name="question.status" />
						</label>
						<div class="form-element">
							<s:checkbox name="question.status" theme="simple" />
							<s:text name="active" />
						</div>
					</div>
				</div>
					
				<div class="appContentWrapper marginBottom answerKeysTr na normal">
			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('question.answerKeys')}" />
				</h2>
			</div>
			<div class="flexiWrapper">
				<div class="flexi flexi10 flexi flexi10-flex-full"
					style="width: 100%; padding-top: 10px;">

				<label for="txt"> <s:text name="question.answerKeys" />
						</label>
						
				
						<div class="edit">
							<div style="width: 100%">
								<div class="panel-body" style="margin-left: 15%" id="dynOption">
								<button type="button" id="addCatDetail"
								class="addBankInfo slide_open btn btn-sm btn-success"
								data-toggle="modal" data-target="#catModal"
								onclick="buttonAddCancel()">
				       	<s:text name="addCatalague"/>
							</button>
						
									<s:text name="question.availAnswerKeys" var="availAnswersTitle" />
									<s:text name="question.selectedAnswerKeys"
										var="selectedAnswersTitle" />
									<s:text name="RemoveAll" var="rmvall" />
									<s:text name="Remove" var="rmv" />
									<s:text name="Add" var="add" />
									<s:text name="AddAll" var="addall" />

									<s:optiontransferselect id="answerKey" cssClass="form-control"
										cssStyle="width:300px;height:400px;overflow-x:auto;"
										doubleCssStyle="width:300px;height:400px;overflow-x:auto;"
										doubleCssClass="form-control" buttonCssClass="optTrasel"
										allowSelectAll="false" allowUpDownOnLeft="false"
										labelposition="top" allowUpDownOnRight="false"
										name="question.availAnswerKeys" list="avilAnswerKeys"
										listKey="key" listValue="value" leftTitle="%{availAnswersTitle}"
										rightTitle="%{selectedAnswersTitle}" 
										headerKey="headerKey" doubleName="question.answerKeyCodes"
										doubleId="selectedAnswers" doubleList="selectedAnswerKeys"
										doubleHeaderKey="doubleHeaderKey"
										addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
										addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />


							</div>
							</div>
							
						
						</div>
					</div>
			
</div>
</div>
				<div class="appContentWrapper marginBottom" id="units">
			<div class="formContainerWrapper">
				<h2>
					<s:property value="%{getLocaleProperty('question.answerKeys')}" />
				</h2>
			</div>
			<div class="flexiWrapper">
				<div class="flexi flexi10 flexi flexi10-flex-full"
					style="width: 100%; padding-top: 10px;">
					<label for="txt"> <s:text name="question.units" />
					</label>

					<div class="edit">
						<div style="width: 100%">
							<div class="panel-body" style="margin-left: 15%" id="dynOptions">
								<s:text name="question.availUnits" var="availUnitsTitle" />
								<s:text name="question.selectedUnits" var="selectedUnitsTitle" />
								<s:text name="RemoveAll" var="rmvall" />
								<s:text name="Remove" var="rmv" />
								<s:text name="Add" var="add" />
								<s:text name="AddAll" var="addall" />


								<s:optiontransferselect cssClass="form-control"
									cssStyle="width:300px;height:400px;overflow-x:auto;"
									doubleCssStyle="width:300px;height:400px;overflow-x:auto;"
									doubleCssClass="form-control" buttonCssClass="optTrasel"
									allowSelectAll="false" allowUpDownOnLeft="false"
									labelposition="top" allowUpDownOnRight="false"
									name="question.availUnits" list="avilUnits" listKey="key"
									leftTitle="%{availUnitsTitle}"
									rightTitle="%{selectedUnitsTitle}" listValue="value"
									headerKey="headerKey" doubleName="question.unitCodes"
									doubleId="selectedUnits" doubleList="selectedUnits"
									doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
									addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
									addToRightLabel="%{add}" />

							</div>
						</div>
						</div>
					</div>

				</div>
				</div>
				
		
<div class="appContentWrapper marginBottom subform">
			<div class="formContainerWrapper">
				<h2>
							<s:text name="subForm" />
				</h2>
			</div>
			
			<div  class="flexform-item" id="gmpMsgDiv">
						<label style="color:red" for="txt"><s:text name="ques.selGMPMsg" />
								&nbsp;
								<s:property value="maxSubFormQuestions" />
							
						</label>
						
					</div>  
			
			<div class="flexiWrapper">
				<div class="flexi flexi10 flexi flexi10-flex-full"
					style="width: 100%; padding-top: 10px;">
								<div id="dynOpt" class="col-xs-4">
										<div class="col-xs-10">
											<s:text name="question.availQuestions" var="availQuestionsTitle" />
								<s:text name="question.selectedQuestions"
									var="selectedQuestionsTitle" />
								<s:text name="RemoveAll" var="rmvall" />
								<s:text name="Remove" var="rmv" />
								<s:text name="Add" var="add" />
								<s:text name="AddAll" var="addall" />
									</div>
							
									<div class="col-xs-10">
										<s:text name="*" var="reqdSymbol">
											<sup style="color: red;">*</sup>
										</s:text>
									</div>
									<div  id="dynOpt">
								<s:text name="question.availQuestions" var="availQuestionsTitle" />
								<s:text name="question.selectedQuestions"
									var="selectedQuestionsTitle" />
								<s:text name="RemoveAll" var="rmvall" />
								<s:text name="Remove" var="rmv" />
								<s:text name="Add" var="add" />
								<s:text name="AddAll" var="addall" />
 
								<s:optiontransferselect cssClass="form-control"  id="question"
									cssStyle="width:300px;height:400px;overflow-x:auto;"
									doubleCssStyle="width:300px;height:400px;overflow-x:auto;"
									doubleCssClass="form-control" buttonCssClass="optTrasel"
									allowSelectAll="false" allowUpDownOnLeft="false"
									labelposition="top" allowUpDownOnRight="false"
									name="question.availQuestionCodes" list="avilQuestions"
									listKey="key" leftTitle="%{availQuestionsTitle}"
									rightTitle="%{selectedQuestionsTitle}" listValue="value"
									headerKey="headerKey" doubleName="question.questionCodes"
									doubleId="selectedQuestions" doubleList="selectedQuestions"
									doubleListKey="id" doubleListValue="name"
									doubleHeaderKey="doubleHeaderKey" addAllToLeftLabel="%{rmvall}"
									addAllToRightLabel="%{addall}" addToLeftLabel="%{rmv}"
									addToRightLabel="%{add}" />


							</div>
							</div>
				
				</div>
			</div>
			</div>

			

				<div class="subform">
					<s:text name="info.QN" />
				</div>

				<div class="subform">
					<ul id="sortable">
				</div>


<div class="appContentWrapper formula marginBottom">
			<div class="formContainerWrapper">
				<h2>
							<s:text name="question.formula" />
				</h2>
			</div>
				
			<div class="flexiWrapper">
				<div class="flexi flexi10 flexi flexi10-flex-full"
					style="width: 100%; padding-top: 10px;">

					<table class="table">
						<tr>

							

							<td>
								<!-- <div class=""> -->
									<!-- <div class="" style="padding: 5px 0; background: #e8fac9;"> -->
										<s:select name="formulaSymbol" cssStyle="width:50px"
											list="symbols" id="symbol" theme="simple" />
										<button type="button" id="butSymbol" class="btC">Add
											Symbol</button>
										<s:select list="numericQuestions" listKey="key" 
											listValue="value" cssStyle="width: 308px;" id="formulaQues"
											theme="simple" />
										<button type="button" id="butQuestion" class="btC">
											Add Question</button>
									<!-- </div> -->
									<!-- <div class="" style="margin-top: 5px; background: #e8fac9;"> -->
										<s:select list="constentValues" listKey="key"
											listValue="value" id="constentValue" cssStyle="width: 296px;"
											theme="simple" />
										<button type="button" id="butConstent" class="btC">
											Add Constant</button>
										<s:textfield id="formulaNumber" cssStyle="width:100px"
											onkeypress="return isNumber(event)" theme="simple"
											maxlength="5" />
										<button type="button" id="butNumber" class="btC">Add
											Number</button>
									<!-- </div> -->
								<!-- </div> -->
						
							</td></tr>
							
							<tr>
							<td><s:text
										name="question.formulaEquation" />
							
								<!-- <div class="form-element"> -->
									<s:textfield name="question.formulaEquation"
										cssClass="upercls form-control" readonly="true" theme="simple"
										maxlength="15" id="formulaEq" />
								<!-- </div> -->
								
								
								<div style="float: right;">
									<button type="button" id="butErase" class="btC">Erase
									</button>
								</div>
								<div style="float: right;">
									<button type="button" id="butClear" class="btC">Clear
									</button>
								</div>

								<div class="clear"></div></td>
						</tr>
					</table>
				</div>
</div>
</div>



				<div id="sectionModal" class="modal fade" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" id="model-close-ques-btn" class="close"
									data-dismiss="modal">&times;</button>

								<h4 style="border-bottom: solid 1px #567304;">
									<s:property value="%{getLocaleProperty('info.Section')}" />
								</h4>
							</div>


							<div class="text-danger cerror"></div>
							<table class="table table-bordered aspect-detail fixedTable">

								<tr>
									<td><s:text name="section" /></td>

									<td>
										<div class="inputCon">
											<input type="text" class="form-control" id="sectionName"
												name="section">
										</div>
									</td>

								</tr>

			

					<tr>
						<th><s:text name="lang.code" /></th>
						<th><s:text name="lang.name" /></th>
						<th><s:text name="lang.info" /></th>
					</tr>
					<tbody id="sectLangPref">
						<s:iterator value="surveySection.languagePreferenceList" status="stat"
							var="langPref">
							<tr class="odd">
								<td width="20%"><s:property value="%{#langPref.lang}" /><sup style="color: red;">*</sup></td>
								<td width="40%"><s:textfield value="%{#langPref.name}"
										name="surveySection.languagePreferenceList[%{#stat.index}].name"
										id="langPrefName" theme="simple" cssClass="sectionName"/></td>
								<td width="40%"><s:textfield value="%{#langPref.info}"
										name="surveySection.languagePreferenceList[%{#stat.index}].info"
										theme="simple" cssClass="sectionInfo"/>
										 <s:hidden
										name="surveySection.languagePreferenceList[%{#stat.index}].id"/>
										 <s:hidden
										name="surveySection.languagePreferenceList[%{#stat.index}].code" /> 
										<s:hidden
										name="surveySection.languagePreferenceList[%{#stat.index}].shortName" cssClass="shortName"/>
										<s:hidden
										name="surveySection.languagePreferenceList[%{#stat.index}].lang" cssClass="lang"/> 
										<s:hidden
										name="surveySection.languagePreferenceList[%{#stat.index}].type" cssClass="type"/></td>
							</tr>
						</s:iterator>
					</tbody>
							</table>

							<div class="modal-footer">
								<button type="button" class="btn btn-sts" id="saveSectionDetail"
									onclick="saveSectionInformation()">
									<s:text name="save.button" />
								</button>
								<button class="cancel-btn btn btn-sts" id="buttonEdcCancel"
									onclick="buttonSectionCancel()" type="button">
									<font color="#FFFFFF"> <s:text name="Cancel" />
									</font>
								</button>
							</div>
						</div>
					</div>
				</div>
				
				<div id="catModal" class="modal fade" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" id="model-close-cat-btn" class="close"
									data-dismiss="modal">&times;</button>

								<h4 style="border-bottom: solid 1px #567304;">
									<s:property value="%{getLocaleProperty('info.catalogue')}" />
								</h4>
							</div>


							<div class="text-danger caterror" align="center"></div>
							<table class="table table-bordered aspect-detail fixedTable">

								<tr>
									<td><s:text name="catalogue.name" /></td>

									<td>
										<div class="inputCon">
											<input type="text" class="form-control" id="catNaame"
												name="catalogue.name">
										</div>
									</td>

								</tr>

			

					<tr>
						<th><s:text name="lang.code" /></th>
						<th><s:text name="lang.name" /></th>
						<th><s:text name="lang.info" /></th>
					</tr>
					<tbody id="catLangPref">
						<s:iterator value="catLang" status="stat"
							var="langPref">
							<tr class="odd">
								<td width="20%"><s:property value="%{#langPref.lang}" /><sup style="color: red;">*</sup></td>
								<td width="40%"><s:textfield value="%{#langPref.name}"
										name="catLang[%{#stat.index}].name"
										id="catlangPrefName" theme="simple" cssClass="catsectionName"/></td>
								<td width="40%"><s:textfield value="%{#langPref.info}"
										name="catLang[%{#stat.index}].info"
										theme="simple" cssClass="catsectionInfo"/>
										 <s:hidden
										name="catLang[%{#stat.index}].id"/>
										 <s:hidden
										name="catLang[%{#stat.index}].code" /> 
										<s:hidden
										name="catLang[%{#stat.index}].shortName" cssClass="catshortName"/>
										<s:hidden
										name="catLang[%{#stat.index}].lang" cssClass="catlang"/> 
										<s:hidden
										name="catLang[%{#stat.index}].type" cssClass="cattype"/></td>
							</tr>
						</s:iterator>
					</tbody>
							</table>

							<div class="modal-footer">
								<button type="button" class="btn btn-sts" id="saveCatDetail"
									onclick="saveCatInformation()">
									<s:text name="save.button" />
								</button>
								<button class="cancel-btn btn btn-sts" id="buttonEdcCancel"
									onclick="buttonAddCancel()" type="button">
									<font color="#FFFFFF"> <s:text name="Cancel" />
									</font>
								</button>
							</div>
						</div>
					</div>
				</div>
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
					<s:iterator value="question.languagePreferences" status="stat"
						var="langPref">
						<tr class="odd">
							<td width="20%"><s:property value="%{#langPref.lang}" /><sup style="color: red;">*</sup></td>
							<td width="40%"><s:textfield value="%{#langPref.name}"
									name="question.languagePreferences[%{#stat.index}].name"
									id="langPrefName" theme="simple" /></td>
							<td width="40%"><s:textfield value="%{#langPref.info}"
									name="question.languagePreferences[%{#stat.index}].info"
									theme="simple" /> <s:hidden
									name="question.languagePreferences[%{#stat.index}].id" /> <s:hidden
									name="question.languagePreferences[%{#stat.index}].code" /> <s:hidden
									name="question.languagePreferences[%{#stat.index}].shortName" />
								<s:hidden
									name="question.languagePreferences[%{#stat.index}].lang" /> <s:hidden
									name="question.languagePreferences[%{#stat.index}].type" /></td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</div>

		<div class="yui-skin-sam">
			<s:if test="command =='create'">
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="submitForm(this);">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<s:else>
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="submitForm(this);">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else>
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="cancel-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
						</font></b>
					</button></span></span>
		</div>




	</s:form>
	<s:form name="cancelform" action="question_list.action">
		<s:hidden key="currentPage" />
	</s:form>
	<script type="text/javascript">
var command='<s:property value="command" />';
var otherCatVal='<s:property value="question.otherCatalogValue" />';
$(function () {
	
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.remove"/>']").addClass("ic-remove");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.add"/>']").addClass("ic-add");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.removeAll"/>']").addClass("ic-removeAll");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.addAll"/>']").addClass("ic-addAll");
});	
function submitForm(btn){
	jQuery(btn).attr("disabled",true);
	selectAllOptions('selectedAnswers');
	selectAllOptions('selectedUnits');
	$('#collectionType').prop("disabled",false);
	if('<s:property value="surveyMapped" />' === "true"){
		showElementsOnEdit(false);
	}
	
	selectAllOptions('selectedQuestions');
	selectAllOptions('question');
    document.form.submit();
}
function selectAllOptions(selectId){
	document.getElementById(selectId).multiple = true; //to enable all option to be selected
	for (var x = 0; x < document.getElementById(selectId).options.length; x++)//count the option amount in selection box
	    document.getElementById(selectId).options[x].selected = true;
}


function saveCatInformation(){
	
	 var sectionId= $('#catNaame').val();
	 $('.caterror').html('');
		if(sectionId==""){
			$('.caterror').html('<s:text name="empty.name"/>');
			valid=false;
			return false;
		}
		
		var tableBody = jQuery("#catLangPref tr");
		var dataArr = new Array();
		
		selectAllOptions('selectedAnswers');
		var answers = $('#selectedAnswers').val();
		alert("aan"+answers);
		$.each(tableBody, function(index, value) {
			var sectionName = jQuery(this).find(".catsectionName").val();
			var sectionInfo = jQuery(this).find(".catsectionInfo").val();
			var shortName = jQuery(this).find(".catshortName").val();
			var lang=jQuery(this).find(".catlang").val();
			var type=jQuery(this).find(".cattype").val();
			dataArr.push({name:sectionName,info:sectionInfo,shortName:shortName,lang:lang,type:type});
		});
		var preferenceJSON = JSON.stringify(dataArr);
		$("#saveCatDetail").prop('disabled', true);
		$.ajax({
			 type: "POST",
	      async: false,
	      url: "question_populateCat.action",
	      data: {selectedSection:sectionId,preferenceJSON:preferenceJSON,answers:JSON.stringify(answers)},
	      success: function(result) {
	    	  console.log(result);
	    	  $("#saveCatDetail").prop('disabled', false);
	    	   document.getElementById("answerKey").length = 0;
	    	   var jsonArr = JSON.parse(result);
	    	     for (var i = 0; i < jsonArr.length; i++) {
	    	        addOption(document.getElementById("answerKey"), jsonArr[i].name, jsonArr[i].id);
	    	    }
	    	//  insertOption("answerKey",JSON.parse(result));
				buttonAddCancel();	
				$('#catNaame').val('');
				$(".catsectionName").val('');
				$(".catsectionInfo").val('');
				$(".sectionName").val('');
				
	      }
		});
		
}
function copyOptions(sourceSelect,distSelect){
	var headerKey=$(distSelect+" option:first").val();
	var headerValue=$(distSelect+" option:first").text();
	$(distSelect).empty();
	$(distSelect).append('<option value="'+headerKey+'">'+headerValue+'</option>');
	$(sourceSelect+" option").each(function(){
		$(distSelect).append('<option value="'+$(this).val()+'">'+$(this).text()+'</option>');
	});
}


function isNumber(evt) {
	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function controlComponentsByQusType(element){
	
	if($(element).val() == 0){ //Normal
		
		  $('.normal').show();
		  $('.formula').hide();
		  $('.dependency').hide();
		  $('.subform').hide();
		  //disableTr('.normal',false);
		  //disableTr('.formula',true);
		  //disableTr('.dependency',true);
		  $('.save-btn').prop("disabled",false);
		  $('#collectionType').prop("disabled",false);
	}else if($(element).val() == 1){ //Formula
	
		  $('.normal').hide();
		  $('.formula').show();
		  $('.dependency').hide();
		  $('.subform').hide();
		  //disableTr('.normal',true);
		  //disableTr('.formula',false);
		  //disableTr('.dependency',true);
		  $('.save-btn').prop("disabled",false);
		  $('#collectionType').val('1');
		  $('#collectionType').prop("disabled",true);
	}else if($(element).val() == 2){ //Dependency
		
		  $('.normal').show();
		  $('.formula').hide();
		  $('.dependency').show();
		  $('.subform').hide();
		  //disableTr('.normal',false);
		  //disableTr('.formula',true);
		  //disableTr('.dependency',false);
		  $('.save-btn').prop("disabled",false);
		  $('#collectionType').prop("disabled",false);
		  copyOptions('#selectedAnswers','#dependencyKey');
	}else if($(element).val() == 3){ //Subform
		
		  $('.normal').hide();
		  $('.formula').hide();
		  $('.dependency').hide();
		  $('.subform').show();
		  //disableTr('.normal',false);
		  //disableTr('.formula',true);
		 // disableTr('.dependency',true);
		  $('.save-btn').prop("disabled",false);
		  //$('#collectionType').prop("disabled",false);
		  $('#dataColl').hide();
		  $('#collType').hide();
			
		 
	}else{
		  $('.formula').hide();
		  $('.normal').hide();
		  $('.dependency').hide();
		  $('.subform').hide();
		  //disableTr('.normal',true);
		  //disableTr('.formula',true);
		  //disableTr('.dependency',true);
		  $('.save-btn').prop("disabled",true);
		  $('#collectionType').prop("disabled",false);
	}
	
}

function listQuestions()
{		

	var sectionId=$('#section').val();
	
	var quesType = $('#questionType').val();
	$('#question').empty();
	var selVals=new Array();
	$('#selectedQuestions option').each(function(){
	selVals.push($(this).val());
	});	 
	$('#selectedQuestions').empty();
	
	reloadSortable();	
	$.getJSON('question_populateQuestions.action?sectionId='+sectionId,function(data){
		$('#question').empty();
		 if(data!=null&&data!=''){
			 $.each(data, function(k, v) {
				 $("#question").append("<option value='"+k+"'>"+v+"</option>");
		    });
         }
         
		 
	});
}

/* function disableTr(obj,bool){
	 $(obj).closest('tr').find('input, select, textarea').each(function(){
		    if($(this).prop("type")!='button'&&bool){
			   $(this).val('');
		    }
			$(this).prop('disabled', bool);
	 });
} */

function showDependencyTypes(){
	var depType = $('input[name="question.dependencyType"]:checked').val();
	if(depType=='0'){ //Parent
		$('.trParent').show();
		$('.trChild').hide();
		$('#parentQuestion').prop('disabled',true);
		$('#dependencyKey').prop('disabled',false);
	}else if(depType=='1'){ //Child
		$('.trParent').hide();
		$('.trChild').show();
		$('#parentQuestion').prop('disabled',false);
		$('#dependencyKey').prop('disabled',true);
	}else{
		$('.trParent').hide();
		$('.trChild').hide();
		$('#parentQuestion').prop('disabled',true);
		$('#dependencyKey').prop('disabled',true);
	}
}
function showElementsByValidationType(){
	var valType=parseInt($('#validationType').val());
	var compType = parseInt($('#componentType').val());
	if([2,3].indexOf(valType) > -1){
		$('#df').prop('disabled',false);
		$('#df').show();
	}else{
		$('#df').prop('disabled',true);
		$('#df').hide();
	}

	if(valType == 2){
		$('#max').append('<sup id="temp" style="color: red;">*<s:text name="warning"/></sup>');
	}else{
		$('#temp').remove();
	}

	if(compType == 1 && valType != 0){
  		 $('#units').prop('disabled',false);
     		 $('#units').show();
     		 $('#unitOtherCat *').prop('disabled',false);
     		 $('#unitOtherCat').show();
     		 if(valType==1){  //To show if numeric type is selected
         		 $('#minRange').show();
         		 $('#maxRange').show();
         		 }else{
         			 if(valType==2){     				
     				 $("#maxlen").prop('disabled',false);
     				$("#maxLengthParam").prop('disabled',true);
      				 $("#maxlen").removeClass('hide');
      				$("#maxLength").removeClass('hide');
      			     }else{
      				$("#maxlen").prop('disabled',true);
      				$("#maxLengthParam").prop('disabled',false);
      				$("#maxlen").addClass('hide');
      				$("#maxLength").addClass('hide');
      			 }
         		 $('#minRange').prop('disabled',true);
           		 $('#minRange').hide();
           		 $('#maxRange').prop('disabled',true);
          		 $('#maxRange').hide();
         		 }
  	  }else{
  		    $('#units').prop('disabled',true);
    		 $('#units').hide();
    		$('#unitOtherCat *').prop('disabled',true);
    		 $('#unitOtherCat').hide();
  	}
}

function showElementsByDataCollection(){
	var dataCollection=$('#dataCollection').val();
	
	if(dataCollection=='1'){
		$('.trOnce').show();
		//disableTr('.trOnce',false);
	}else{
		$('.trOnce').hide();
		//disableTr('.trOnce',true);
	}
}
function showElementsOnEdit(flag){
	
	var displar = '';
	if(flag){
		displar="none"
	}
	
		 $('#section').prop('disabled',flag);
		 $('#questionType').prop('disabled',flag);
		 $('#dataCollection').prop('disabled',flag);
		 $('#componentType').prop('disabled',flag);
		 $('#collectionType').prop('disabled',flag);
		 $('#validationType').prop('disabled',flag);
		 $('#dataFormat').prop('disabled',flag);
    	 $('#listMethod').prop('disabled',flag);
		 $('.ic-remove').css("pointer-events", displar);
		 $('.ic-removeAll').css("pointer-events", displar);
		 $('.ic-add').css("pointer-events", displar);
		 $('.ic-addAll').css("pointer-events", displar);
		 $('#defaultValue').prop('disabled',flag);
		 $('#quesStat *').prop('disabled',flag);
	
	
}
function showElementsByComponentType(edit){
	
		var compType = parseInt($('#componentType').val());
		var valType=parseInt($('#validationType').val());
		 
		if([0,1,2,9,10,11].indexOf(compType) > -1){
		   
		   $('.na').prop('disabled',true);
   		   $('.na').hide();
   		
    	}else{
    
			$('.na').prop('disabled',false);
			$('.na').show();
    	}
    	

    	if([4,8,13,6].indexOf(compType) > -1){
			$('#otherCat *').prop('disabled',false);
   		    $('#otherCat').show();
     	}else{
     		$('#otherCat *').prop('disabled',true);
			$('#otherCat').hide();
		}

		if([1,2,9].indexOf(compType) > -1){
			$('#form_question_maxLength').prop('disabled',false);
   		    $('#maxLength').show();
     	}else{
     		$('#form_question_maxLength').prop('disabled',true);
			$('#maxLength').hide();
		}
		if([3,7].indexOf(compType) > -1){
		 
			if(!edit){
		   	copyOptions('#selectedAnswers','#dependencyKey');
			}
		}else{
			$('#dependencyKey').children('option:not(:first)').remove();
		}

		
		if([1,2,9,10,11].indexOf(compType) > -1){
			$('#listMethodtr').prop('disabled',true);
			$('#listMethodtr').hide();
		}else{
			$('#listMethodtr').prop('disabled',false);
			$('#listMethodtr').show();
		}

		if([3,4,7,8,12,13].indexOf(compType) > -1){
			$('#valType *').prop('disabled',true);
   		    $('#valType').hide();
     	}else{
     		$('#valType *').prop('disabled',false);
   		    $('#valType').show();
		}
		
		if(compType == 1 && valType != 0){
	  		 $('#units').prop('disabled',false);
	     		 $('#units').show();
	     		 $('#unitOtherCat *').prop('disabled',false);
	     		 $('#unitOtherCat').show();
	     		 if(valType==1){  //To show if numeric type is selected
	         		 $('#minRange').show();
	         		 $('#maxRange').show();
	         		 }else{
	         			 if(valType==2){     				
	     				 $("#maxlen").prop('disabled',false);
	     				$("#maxLengthParam").prop('disabled',true);
	      				 $("#maxlen").removeClass('hide');
	      				$("#maxLength").removeClass('hide');
	      			     }else{
	      				$("#maxlen").prop('disabled',true);
	      				$("#maxLengthParam").prop('disabled',false);
	      				$("#maxlen").addClass('hide');
	      				$("#maxLength").addClass('hide');
	      			 }
	         		 $('#minRange').prop('disabled',true);
	           		 $('#minRange').hide();
	           		 $('#maxRange').prop('disabled',true);
	          		 $('#maxRange').hide();
	         		 }
	  	  }else{
	  		    $('#units').prop('disabled',true);
	    		 $('#units').hide();
	    		$('#unitOtherCat *').prop('disabled',true);
	    		 $('#unitOtherCat').hide();
	  	}			  
	
	}

function listMethodChangeControl(){
	var selVal=$('#listMethod').val();
	if(selVal==null||selVal==''){
		$('.answerKeysTr').show();
		//disableTr('.answerKeysTr',false);
	}else{
		$('.answerKeysTr').hide();
		//disableTr('.answerKeysTr',true);
		var distSelect='#otherCatalogue';
		var headerKey=$(distSelect+" option:first").val();
		var headerValue=$(distSelect+" option:first").text();
		$(distSelect).empty();
		$(distSelect).append('<option value="'+headerKey+'">'+headerValue+'</option>');
		$(distSelect).append('<option value="$listOtherKey">'+'<s:text name="otherText" />'+'</option>');
		if(command=='update'){
			$(distSelect).val(otherCatVal);
			command='save';
		}
	}
}
function orderQuestions(){
	$('#selectedQuestions').empty();
	$("#sortable li").each(function(){
		var text=$(this).text();
		var arr = text.split(' : ');
		var qusCode=arr[0];
		var qusName=arr[1];
		$('#selectedQuestions').append("<option value='"+arr[0]+"'>"+text+"</option>");
	});
}

function reloadSortable(){
	$("#sortable").empty();
	 $("#selectedQuestions option").each(function(){
			$( "#sortable" ).append('<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s" title="<s:text name="tipsTitle" />"></span>'+$(this).text()+'</li>');
	  });
}

function resetCombo(controlId){
	document.getElementById(controlId).length=0;
}

function addOptionsToCombo(contorlId, jsonArray){
	for(var i=0;i<jsonArray.length;i++){
  		addOption(document.getElementById(contorlId), jsonArray[i].value, jsonArray[i].key);
	}
}

function addOption(selectbox,text,value )
{
 	var optn = document.createElement("OPTION");
    optn.text = text;
    optn.value = value;
    selectbox.options.add(optn);
}
$(document).ready(function(){
	//$("#componentType option[value='5']").remove();
	//$("#componentType option[value='6']").remove();
	//$("#componentType option[value='11']").remove();
	//showDependencyTypes();
 	//$("#questionType option[value='3']").remove();
	//$("#dataCollection option[value='1']").remove();
	showElementsByDataCollection();
	 if('<s:property value="question.parentDepen" />' !=''){
		 var depe = '<s:property value="question.parentDepen" />' ;
		
		 var standards = depe.split(',');
			for(var count=0;count<standards.length;count++){
				$('#parentDepen option[value='+standards[count].trim()+']').attr('selected','selected');
			}
	 }
	showElementsByValidationType();
	listMethodChangeControl();
	 reloadSortable();
	if('<s:property value="surveyMapped" />' === "true"){
	    showElementsOnEdit(true);
	}else{
		$( "#sortable" ).sortable({
			 update:orderQuestions
		 });
		 $( "#sortable" ).disableSelection();
		showElementsOnEdit(false);
	}
	showElementsByComponentType(true);
	 $(' #dynOpt input.ic-remove,#dynOpt input.ic-add,#dynOpt input.ic-removeAll,#dynOpt input.ic-addAll').on('click',function(e){
		 var selCount = $("#selectedQuestions option").length;
		 //alert(selCount);
  		 $("#selLabel").text(selCount);
		 reloadSortable();
     });
	controlComponentsByQusType($('#questionType'));
	$('#dynOption input.optTrasel ').on('click',function(e){
	   	 copyOptions('#selectedAnswers','#otherCatalogue');
	   	 var compType = parseInt($("#componentType").val());
	   	 if([3,7].indexOf(compType) > -1){
	   	 copyOptions('#selectedAnswers','#dependencyKey');
	   	 }
    });

	$('#dynOptions input.optTrasel').on('click',function(e){
		  copyOptions('#selectedUnits','#defaultUnit');
		  copyOptions('#selectedUnits','#unitOtherCatalogue');
    });

	$('#section').on('change',function(e){
		var sectionId=$('#section').val();
		var quesType = $('#questionType').val();
		$('#formulaEq').val('');
		if(sectionId!='-1'){
		$.getJSON('question_populateNumericQuestions.action?sectionId='+sectionId,function(jd){
			var jsonArray=jd.maps;
			populateSelect('#formulaQues',jsonArray,'#nohk','#nohv');
			$('#lblEnCol').text(jd.dataLevel);
		});
    if(sectionId!='-1' && quesType!='' &&  quesType==3){
    	listQuestions();
      }
		reloadSelect('#parentQuestion','question_populateParentQuestions.action?sectionId='+sectionId+'&id='+'<s:property value="question.id"/>');
		//reloadSelect('#entityColumn','question_populateEntityColumns.action?sectionId='+sectionId);
		}
	});

	
	
	$('#butSymbol').on('click',function(e){
	      var symbol=$('#symbol').val();
	      var exText= $('#formulaEq').val();
	      $('#formulaEq').val(exText+symbol);
	});

	$('#butQuestion').on('click',function(e){
	      var ques=$('#formulaQues').val();
	      var exText= $('#formulaEq').val();
	      $('#formulaEq').val(exText+'{'+ques+'}');
	});

	$('#butConstent').on('click',function(e){
	      var constentValue=$('#constentValue').val();
	      if(constentValue!='$YEAR'){
	       constentValue='##'+constentValue+'##';
	      }
	      var exText= $('#formulaEq').val();
	      $('#formulaEq').val(exText+constentValue);
	});
	
	$('#butNumber').on('click',function(e){
      var numberValue=$('#formulaNumber').val();
      var exText= $('#formulaEq').val();
      $('#formulaEq').val(exText+numberValue);
	});

	$('#butClear').on('click',function(e){
		$('#formulaEq').val('');
	});

	$('#butErase').on('click',function(e){
		var exText= $('#formulaEq').val();
		exText=exText.slice(0,-1);
		$('#formulaEq').val(exText);
	});

	 var selCount = $("#selectedQuestions option").length;
		 $("#selLabel").text(selCount);
		 
			$("#dynOpt")
			.find(
					"input[type='button'][value='<s:text name="RemoveAll"/>']")
			.addClass("btn btn-warning");

	$("#dynOpt")
			.find(
					"input[type='button'][value='<s:text name="Add"/>']")
			.addClass(
					"btn btn-small btn-success fa fa-step-forward");
	$("#dynOpt")
			.find(
					"input[type='button'][value='<s:text name="Remove"/>']")
			.addClass("btn btn-danger");
	$("#dynOpt")
			.find(
					"input[type='button'][value='<s:text name="AddAll"/>']")
			.addClass("btn btn-sts");
	
	$("#dynOption")
	.find(
			"input[type='button'][value='<s:text name="RemoveAll"/>']")
	.addClass("btn btn-warning");
	
	$("#dynOption")
	.find(
			"input[type='button'][value='<s:text name="Add"/>']")
	.addClass(
			"btn btn-small btn-success fa fa-step-forward");
	$("#dynOption")
	.find(
			"input[type='button'][value='<s:text name="Remove"/>']")
	.addClass("btn btn-danger");
	$("#dynOption")
	.find(
			"input[type='button'][value='<s:text name="AddAll"/>']")
	.addClass("btn btn-sts");
	
	$("#dynOptions")
	.find(
			"input[type='button'][value='<s:text name="RemoveAll"/>']")
	.addClass("btn btn-warning");
	
	$("#dynOptions")
	.find(
			"input[type='button'][value='<s:text name="Add"/>']")
	.addClass(
			"btn btn-small btn-success fa fa-step-forward");
	$("#dynOptions")
	.find(
			"input[type='button'][value='<s:text name="Remove"/>']")
	.addClass("btn btn-danger");
	$("#dynOptions")
	.find(
			"input[type='button'][value='<s:text name="AddAll"/>']")
	.addClass("btn btn-sts");
	
});

function moveOptions1(objSourceElement, objTargetElement, toSort, chooseFunc) {
    var aryTempSourceOptions = new Array();
    var aryTempTargetOptions = new Array();
    var x = 0;
    var maxCpunt = '<s:property value="maxSubFormQuestions"/>';

	//alert("ADD ALL: "+objSourceElement.length);
if(objSourceElement.length<=maxCpunt)
	{
    	//looping through source element to find selected options
	    for (var i = 0; i < objSourceElement.length; i++) {
	        if (chooseFunc(objSourceElement.options[i])) {
	            //need to move this option to target element
	            var intTargetLen = objTargetElement.length++;
	            objTargetElement.options[intTargetLen].text =   objSourceElement.options[i].text;
	            objTargetElement.options[intTargetLen].value =  objSourceElement.options[i].value;
	        }
	        else {
	            //storing options that stay to recreate select element
	            var objTempValues = new Object();
	            objTempValues.text = objSourceElement.options[i].text;
	            objTempValues.value = objSourceElement.options[i].value;
	            aryTempSourceOptions[x] = objTempValues;
	            x++;
	        }
	    }

	    //sorting and refilling target list
	    for (var i = 0; i < objTargetElement.length; i++) {
	        var objTempValues = new Object();
	        objTempValues.text = objTargetElement.options[i].text;
	        objTempValues.value = objTargetElement.options[i].value;
	        aryTempTargetOptions[i] = objTempValues;
	    }
	    
	    if (toSort) {
	        aryTempTargetOptions.sort(sortByText);
	    }    
	    
	    for (var i = 0; i < objTargetElement.length; i++) {
	        objTargetElement.options[i].text = aryTempTargetOptions[i].text;
	        objTargetElement.options[i].value = aryTempTargetOptions[i].value;
	        objTargetElement.options[i].selected = false;
	    }   
	    
	    //resetting length of source
	    objSourceElement.length = aryTempSourceOptions.length;
	    //looping through temp array to recreate source select element
	    for (var i = 0; i < aryTempSourceOptions.length; i++) {
	        objSourceElement.options[i].text = aryTempSourceOptions[i].text;
	        objSourceElement.options[i].value = aryTempSourceOptions[i].value;
	        objSourceElement.options[i].selected = false;
	    }
	}
	else
	{
		//alert("qwerrty ");
		//$("#selGMPMsg").text("Cannot add more than 250 Sections");
	}
}
function moveAllOptions(objSourceElement, objTargetElement, toSort, notMove1, notMove2) {
    var test1 = compile(notMove1);
    var test2 = compile(notMove2);

    if((objSourceElement.id == "question") && (objTargetElement.id == "selectedQuestions"))
    {
	    //alert("ADD ALL IF");
	    // When ADD ALL button clicked
	    moveOptions1(objSourceElement, objTargetElement, toSort, 
	        function(opt) {
	            return (!test1(opt.value) && !test2(opt.value));
	        }
	    );
    }
    else
    {
    	//alert("ADD ALL, REMOVE ALL ELSE");
    	// When REMOVE ALL button clicked
    	moveOptions(objSourceElement, objTargetElement, toSort, 
	        function(opt) {
	            return (!test1(opt.value) && !test2(opt.value));
	        }
	    );
    }
}

function moveSelectedOptions(objSourceElement, objTargetElement, toSort, notMove1, notMove2) {
    var test1 = compile(notMove1);
    var test2 = compile(notMove2);
    //alert("==="+objSourceElement.id+"..."+objTargetElement.id+"===")
     if((objSourceElement.id == "question") && (objTargetElement.id == "selectedQuestions"))
    {
    	//alert("ADD IF");
	    // When ADD button clicked
	    moveOptions2(objSourceElement, objTargetElement, toSort, 
	        function(opt) {
	            return (opt.selected && !test1(opt.value) && !test2(opt.value));
	        }
	    );
    }
    else
    {
    	//alert("ADD ALL, REMOVE ELSE");
	    // When REMOVE button clicked
    	moveOptions(objSourceElement, objTargetElement, toSort, 
	        function(opt) {
	            return (opt.selected && !test1(opt.value) && !test2(opt.value));
	        }
	    );
    }
}
function populateDependancyKey(obj)
{		

	var parent=$(obj).val();
	$('#parentDepen').empty();
	addOption(document.form.parentDepen, "<s:property value="selectName" escape="false"/>", "");
	$.getJSON('question_populateDependancyKey.action?parentId='+parent,function(data){
			
		 if(data!=null&&data!=''){
			 $.each(data, function(k, v) {
				 $("#parentDepen").append("<option value='"+k+"'>"+v+"</option>");
		    });
         }
         
		 
	});
}


function saveSectionInformation(){
	
	 var sectionId= $('#sectionName').val();

		if(sectionId==""){
			document.getElementById("validateError").innerHTML='<s:text name="empty.section"/>';
			valid=false;
			return false;
		}
		
		var tableBody = jQuery("#sectLangPref tr");
		var dataArr = new Array();
		
		
		
		$.each(tableBody, function(index, value) {
			var sectionName = jQuery(this).find(".sectionName").val();
			var sectionInfo = jQuery(this).find(".sectionInfo").val();
			var shortName = jQuery(this).find(".shortName").val();
			var lang=jQuery(this).find(".lang").val();
			var type=jQuery(this).find(".type").val();
			if(sectionName==''){
				sectionName = sectionId;
			}
			dataArr.push({name:sectionName,info:sectionInfo,shortName:shortName,lang:lang,type:type});
		});
		var preferenceJSON = JSON.stringify(dataArr);
		$("#saveSectionDetail").prop('disabled', true);
		$.ajax({
			 type: "POST",
	      async: false,
	      url: "question_populateSection.action",
	      data: {selectedSection:sectionId,preferenceJSON:preferenceJSON},
	      success: function(result) {
	    	  console.log(result);
	    	  $("#saveSectionDetail").prop('disabled', false);
	    	  insertOption("section",JSON.parse(result));
				buttonSectionCancel();	
				$('#sectionName').val('');
				$(".sectionInfo").val('');
				$(".shortName").val('');
			//	$(".type").val('');
			//	$(".lang").val('');
	      }
		});
		
}

function insertOption(ctrlName, jsonArr) {
    document.getElementById(ctrlName).length = 0;
     for (var i = 0; i < jsonArr.length; i++) {
        addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
    }
   
    var id="#"+ctrlName;
    jQuery(id).select2();
}

function buttonSectionCancel(){
	document.getElementById("model-close-ques-btn").click();	
}

function buttonAddCancel(){
	document.getElementById("model-close-cat-btn").click();	
}

function moveOptions2(objSourceElement, objTargetElement, toSort, chooseFunc) {
    var aryTempSourceOptions = new Array();
    var aryTempTargetOptions = new Array();
    var aryTemp1TargetOptions = new Array();
    var x = 0,y=0;
    var maxCpunt = '<s:property value="maxSubFormQuestions"/>';
    maxCpunt=10;
    for (var i = 0; i < objSourceElement.length; i++) {
    	 if (chooseFunc(objSourceElement.options[i])) {
        	var objTemp1Values = new Object();
        	objTemp1Values.text = objSourceElement.options[i].text;
        	objTemp1Values.value = objSourceElement.options[i].value;
        	aryTemp1TargetOptions[y] = objTemp1Values;
         	y++;
    	 }
    }
	var addedValues=aryTemp1TargetOptions.length + objTargetElement.length;
	//looping through source element to find selected options
	//alert("addedValues: "+addedValues);	
	if(addedValues<=maxCpunt)
    {
	    for (var i = 0; i < objSourceElement.length; i++) {
	        if (chooseFunc(objSourceElement.options[i])) {
	            //need to move this option to target element
	            var intTargetLen = objTargetElement.length++;
	            objTargetElement.options[intTargetLen].text =   objSourceElement.options[i].text;
	            objTargetElement.options[intTargetLen].value =  objSourceElement.options[i].value;
	        }
	        else {
	            //storing options that stay to recreate select element
	            var objTempValues = new Object();
	            objTempValues.text = objSourceElement.options[i].text;
	            objTempValues.value = objSourceElement.options[i].value;
	            aryTempSourceOptions[x] = objTempValues;
	            x++;
	        }
	    }
	    //}
	    //var toBeAdded=objTargetElement.length;
	    //alert("------"+toBeAdded);
             
	    //sorting and refilling target list
	    for (var i = 0; i < objTargetElement.length; i++) {
	        var objTempValues = new Object();
	        objTempValues.text = objTargetElement.options[i].text;
	        objTempValues.value = objTargetElement.options[i].value;
	        aryTempTargetOptions[i] = objTempValues;
	    }
	    if (toSort) {
	        aryTempTargetOptions.sort(sortByText);
	    }    

	    //if(toBeAdded<254){
		    for (var i = 0; i < objTargetElement.length; i++) {
		        objTargetElement.options[i].text = aryTempTargetOptions[i].text;
		        objTargetElement.options[i].value = aryTempTargetOptions[i].value;
		        objTargetElement.options[i].selected = false;
		    }   
	    //}    
	    //resetting length of source
	    objSourceElement.length = aryTempSourceOptions.length;
	    //looping through temp array to recreate source select element
	    for (var i = 0; i < aryTempSourceOptions.length; i++) {
	        objSourceElement.options[i].text = aryTempSourceOptions[i].text;
	        objSourceElement.options[i].value = aryTempSourceOptions[i].value;
	        objSourceElement.options[i].selected = false;
	    }
	}
	else
	{
		//alert("qwerrty ");
		//$("#selGMPMsg").text("Cannot add more than 250 Sections");
	}
}


function setMaxLengthVal(){
	var minRangeLengthVal = parseInt($("#minRangeField").val()).toString().length;
	var maxRangeLengthVal = parseInt($("#maxRangeField").val()).toString().length;
	var maxLengthVal = 0;
	if(minRangeLengthVal>maxRangeLengthVal){
		maxLengthVal = minRangeLengthVal; 
	}else{
		maxLengthVal = maxRangeLengthVal;
	}
	//$("#maxLengthField").val(maxLengthVal);
	$("#maxLengthParam").val(maxLengthVal);		
}

</script>
</body>