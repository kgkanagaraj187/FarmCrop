<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
    <!-- add this meta information to select layout  -->
    <meta name="decorator" content="swithlayout">
</head>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						controlComponentsByQusType('<s:property value="question.questionType" />');
						//showDependencyTypes();
						showElementsByDataCollection();
						showElementsByValidationType();
						showElementsByComponentType();
					});
</script>

<s:hidden key="question.id" id="questionId"/>
<font color="red">
<s:actionerror/></font>
<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.questionDetails')}" /></h2>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.code" /></p>
						<p class="flexItem"><s:property value="question.code" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.serialNo" /></p>
						<p class="flexItem"><s:property value="question.serialNo" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.name" /></p>
						<p class="flexItem"><s:property value="question.name" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.questionType" /></p>
						<p class="flexItem"><s:text
					name="questionTypes%{question.questionType}" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.section" /></p>
						<p class="flexItem"><s:property value="question.section.name" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.info" /></p>
						<p class="flexItem"><s:property value="question.info" /></p>
					</div>
					
					<s:if test='question.questionType != "3"'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.dataCollection" /></p>
						<p class="flexItem"><s:text
						name="dataCollectionType%{question.dataCollection}" /></p>
					</div>
					
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.componentType" /></p>
						<p class="flexItem"><s:text name="componentTypeMaster%{question.componentType}" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.collectionType" /></p>
						<p class="flexItem"><s:text name="collectionTypeMaster%{question.collectionType}" />&nbsp;</p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.validationType" /></p>
						<p class="flexItem"><s:text name="validationTypeMaster%{question.validationType}" /></p>
					</div>
					
					<div class="dynamic-flexItem" id="df">
						<p class="flexItem"><s:text name="question.dataFormat" /></p>
						<p class="flexItem"><s:property value="question.dataFormat" /></p>
					</div>
					
					<div class="dynamic-flexItem" id="listMethodtr">
						<p class="flexItem"><s:text name="question.listMethodName" /></p>
						<p class="flexItem"><s:property value="listMethod" /></p>
					</div>
					
					<s:if test='question.questionType == "1"'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.formulaEquation" /></p>
						<p class="flexItem"><s:property value="question.formulaEquation" /></p>
					</div>
					</s:if>
					
					<div class="dynamic-flexItem" id="maxLength">
						<p class="flexItem"><s:text name="question.maxLength" /></p>
						<p class="flexItem"><s:property value="question.maxLength" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.answerKeys" /></p>
						<p class="flexItem"><s:property value="answerKeys" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.units" /></p>
						<p class="flexItem"><s:property value="units" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.defaultValue" /></p>
						<p class="flexItem"><s:property value="defaultValues" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.defaultUnit" /></p>
						<p class="flexItem"><s:property value="defaultUnit" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.otherCatalogValue" /></p>
						<p class="flexItem"><s:property value="otherCatalogueValue" /></p>
					</div>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.unitOtherCatalogValue" /></p>
						<p class="flexItem"><s:property value="unitOtherCatalogueValue" /></p>
					</div>
				</s:if>	
					
					<%-- <div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.historical" /></p>
						<p class="flexItem"><s:text name="historical%{question.historical}" />&nbsp;</p>
					</div> --%>
					
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="question.status" /></p>
						<p class="flexItem"><s:text name="status%{question.status}" />&nbsp;</p>
					</div>
					
					<div class="dynamic-flexItem dependency" >
						<p class="flexItem"><s:text name="question.dependencyKey" /></p>
						<p class="flexItem"><s:property value="DependancyKey" /></p>
					</div>
					
					<div class="dynamic-flexItem dependency">
						<p class="flexItem"><s:text name="question.parentQuestion" /></p>
						<p class="flexItem"><s:property value="question.parentQuestion.code" /></p>
					</div>
					
					<div class="dynamic-flexItem trOnce" >
					<p class="flexItem"><s:text name="question.entityProperty" /></p>
					<p class="flexItem"><label for="question.entityColumn" id="lblEnCol">
					<s:property
						value="question.section.dataLevel.name" /></label>&nbsp;: <s:text
					name="%{question.entityColumn}" />&nbsp;</p>
					</div>
			
				</div>
				
		<s:if test='question.questionType == "3"'>
		<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('question.list')}" /></h2>
		<table cellspacing="0" cellpadding="0" class="sectionHeader table">
			
			<tr style="font-weight: bold;">
				<td style="width: 50px"><s:text name="s.no"></s:text></td>
				<td style="font-weight: bold;"><s:text name="question.section" /></td>
				<td style="font-weight: bold;"><s:text name="question.code" /></td>
				<td style="font-weight: bold; width: 600px"><s:text
						name="question.name" /></td>
				
			</tr>
			<s:iterator value="question.subFormQuestions" status="questionsCount"
				var="subFormQuestions">
				<tr>
					<td><s:property value="%{#questionsCount.count}" /></td>
					<td><s:property
							value="#subFormQuestions.parentQuestion.section.name" /></td>
					<td><s:property value="#subFormQuestions.childQuestion.code" /></td>
					<td><s:property value="#subFormQuestions.childQuestion.name" /></td>
					<s:if
						test="#questions.section.code=='S05' || questions.section.code=='S06'">
						<td><s:property value="defaultCatalogue.name" /></td>
					</s:if>
					
					
				</tr>
			</s:iterator>
		</table>
		</div>
		
	</s:if>
	
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
				<td width="20%"><s:property value="%{#langPref.lang}" /></td>
				<td width="40%"><s:property value="%{#langPref.name}" /></td>
				<td width="40%"><s:property value="%{#langPref.info}" /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
	</div>
				
				<div class="flexItem flex-layout flexItemStyle">
					<div class="button-group-container">
						<a  onclick="onUpdate();" class="btn btn-success"><s:text name="edit.button" /></a>
						<a onclick="onDelete();" class="btn btn-danger"><s:text name="delete.button" /></a>
						<a onclick="onCancel();" class="btn btn-sts"><s:text name="back.button" /></a>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</div>
					
<s:form name="updateForm" id="updateForm" action="question_update">		
    <s:hidden name="id" value="%{question.id}" />  	  
    <s:hidden key="currentPage" />
</s:form>
<s:form name="deleteForm"  id="deleteForm" action="question_delete">
    <s:hidden key="id" value="%{question.id}"/>
    <s:hidden key="currentPage" />
</s:form>

<s:form name="listForm" action="question_list.action">
		<s:hidden key="currentPage" />
	</s:form>


<script type="text/javascript">

function onUpdate(){
    document.updateForm.submit();

    }
    
function onDelete(){

    var val = confirm('<s:text name="confirm.delete"/>');
    if (val)
            document.deleteForm.submit();
    }

    function onCancel(){
    document.listForm.submit();
    }

	function controlComponentsByQusType(element) {
		if (element == 0) { //Normal
			$('.normal').show();
			$('.formula').hide();
			$('.dependency').hide();
			$('.subform').hide();

		} else if (element == 1) { //Formula
			$('.normal').hide();
			$('.formula').show();
			$('.dependency').hide();
			$('.subform').hide();

		} else if (element == 2) { //Dependency
			$('.normal').show();
			$('.formula').hide();
			$('.dependency').show();
			$('.subform').hide();
		} else if (element == 3) { //Sub Form
			$('.normal').show();
			$('.formula').hide();
			$('.dependency').hide();
			$('.subform').show();
		} else {
			$('.formula').hide();
			$('.normal').hide();
			$('.dependency').hide();
			$('.subform').hide();

		}

	}

	function showElementsByDataCollection() {
		var dataCollection = '<s:property value="question.dataCollection" />';
		if (dataCollection == '1') {
			$('.trOnce').show();
		} else {
			$('.trOnce').hide();
		}
	}

	function showElementsByValidationType() {
		var valType = parseInt('<s:property value="question.validationType" />');
		var compType = parseInt('<s:property value="question.componentType" />');
		if ([ 2, 3 ].indexOf(valType) > -1) {
			$('#df').show();
		} else {
			$('#df').hide();
		}
		if (compType == 1 && valType != 0) {
			$('#units').show();
			$('#unitOtherCat').show();
		} else {
			$('#units').hide();
			$('#unitOtherCat').hide();
		}

	}

	function showElementsByComponentType() {

		var compType = parseInt('<s:property value="question.componentType" />');
		var valType = parseInt('<s:property value="question.validationType" />');
		if ([ 0, 1, 2, 9, 10, 11 ].indexOf(compType) > -1) {
			$('.na').hide();

		} else {
			$('.na').show();

		}

		if ([ 1, 2, 9 ].indexOf(compType) > -1) {
			$('#maxLength').show();
		} else {
			$('#maxLength').hide();
		}

		if ([ 1, 2, 9, 10, 11 ].indexOf(compType) > -1) {
			$('#listMethodtr').hide();
		} else {
			$('#listMethodtr').show();
		}
		if ([ 4, 8, 13 ].indexOf(compType) > -1) {
			$('#otherCat *').prop('disabled', false);
			$('#otherCat').show();
		} else {
			$('#otherCat *').prop('disabled', true);
			$('#otherCat').hide();
		}

		if ([ 3, 4, 7, 8, 12, 13 ].indexOf(compType) > -1) {
			$('.valType *').prop('disabled', true);
			$('.valType').hide();
		} else {
			$('.valType *').prop('disabled', false);
			$('.valType').show();
		}
		if (compType == 1 && valType != 0) {
			$('#units').show();
			$('#unitOtherCat').show();
		} else {
			$('#units').hide();
			$('#unitOtherCat').hide();
		}

	}

	YAHOO.util.Event
			.addListener(
					window,
					"load",
					function() {
						function onUpdate(p_oEvent) {
							document.updateform.id.value = document
									.getElementById('id').value;
							document.updateform.currentPage.value = document.form.currentPage.value;
							document.updateform.submit();
						}
						var button = new YAHOO.widget.Button("update", {
							onclick : {
								fn : onUpdate
							}
						});

						function onDelete(p_oEvent) {
							if (confirm('<s:text name="confirm.delete"/> ')) {
								jQuery(p_oEvent.target).attr("disabled", true);
								document.deleteform.id.value = document
										.getElementById('id').value;
								document.deleteform.currentPage.value = document.form.currentPage.value;
								document.deleteform.submit();
							}
						}

						var button = new YAHOO.widget.Button("delete", {
							onclick : {
								fn : onDelete
							}
						});
					});
</script>