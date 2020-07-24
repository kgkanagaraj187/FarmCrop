<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<html>
<head>

<!-- add this meta information to select layout  -->
<META name="decorator" content="swithlayout"> 
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
</head>
<body>


	<sec:authorize ifAllGranted="profile.surveyMaster.export">
		<div style="float: right; padding-right: 3px; padding-bottom: 3px;">
						<div class="dropdown">
				<sec:authorize ifAllGranted="profile.farmer.create">
					<button id="dLabel" class="btn btn-primary btn-sts" type="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
						<li role="presentation"><a href="#" onclick="exportXlsData()"
							tabindex="-1" role="menuitem"> <s:text name="excel" />
						</a></li>
						<li role="presentation"><a href="#" tabindex="-1"
							role="menuitem" onClick="exportPdfData()"> <s:text name="pdf" />
						</a></li>
					</ul>
					</sec:authorize>
				</div>
			</div>
		</div>
		<div style="clear: both"></div>
	</sec:authorize>
	<div class="error">
		<s:actionerror />
		<s:fielderror />
	</div>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden key="surveyMaster.id" id="id" />
		<s:hidden key="command" />

		
		<div class="fullwidth">
			<div class="flexWrapper">				
				<div class="flexLeft appContentWrapper">				 	
					<div class="aPanel surveyMaster_info" style="width: 100%">
					<div class="formContainerWrapper">
					<div class="formContainerWrapper dynamic-form-con">
					<h2>						
						<s:text name="survey.information" />
					</h2>
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="surveyMaster.code"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="surveyMaster.code" />
						</p>
					</div>
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="surveyMaster.name"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="surveyMaster.name" />
						</p>
					</div>
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="surveyMaster.surveyType"></s:text>
						</p>
						<p class="flexItem">
							<td><s:property value="surveyMaster.surveyType.name" /></td>
						</p>
					</div>
			
					
					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="surveyMaster.section.dataLevel"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="surveyMaster.dataLevel.name" />
						</p>
					</div>

					<div class="dynamic-flexItem2">
						<p class="flexItem">
							<s:text name="surveyMaster.section"></s:text>
						</p>
						<p class="flexItem">
							<s:property value="sectionNames" />
						</p>
					</div>


				</div>
			</div>
			</div>
			</div>
			</div>
			</div>		
		<br />

			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">	
				<div class="formContainerWrapper dynamic-form-con">			
				<div class="aPanel surveyMasterQuestion_info" style="width: 100%">
				    <h2>
						<s:text name="question.list" /></font>						
					</h2>
			<%-- <div class="flexItem" >
			<div class="dynamic-flexItem" style="width: 100%">
						<p class="flexItem"><s:text name="s.no" /></p>
						<p class="flexItem">
				<s:text	name="surveyMaster.section" /></p>
				<p class="flexItem">
				<s:text name="question.code" /></p>
				<p class="flexItem"><s:text
						name="question.name" /></p>
				<p class="flexItem"><s:text name="estimated.hours" /></p>
				<p class="flexItem"><s:text name="question.verify" /><s:if
						test="verifiedSurvey==0">
						<button id="butModify" type="button">
							<s:text name="question.verify.modifyBut" />
						</button>
					</s:if></p></div>
			
			<s:iterator value="questions" status="questionsCount" var="questions">
				<div class="dynamic-flexItem" style="width: 100%">
					<p class="flexItem"><s:property value="%{#questionsCount.count}" /></p>
					<p class="flexItem"><s:property value="#questions.section.name" /></p>
					<p class="flexItem"><s:property value="code" /></p>
					<p class="flexItem"><s:property value="name" /></p>
					<s:if
						test="#questions.section.code=='S05' || questions.section.code=='S06'">
						<p class="flexItem"><s:property value="defaultCatalogue.name" /></p>
					</s:if>
					<s:else>
						<p class="flexItem"><s:text name="-" /></p>
					</s:else>
					<p class="flexItem"><input type="checkbox" class="chkboxVerify"
						name="verifyQusIds" value='<s:property value="#questions.id" />' />
					</p>
				</div>
			</s:iterator>
		</div> --%>
		<table class="table" border="1">
		<tr style="font-weight: bold;">
				<td style="width: 50px"><s:text name="s.no"></s:text></td>
				<td style="font-weight: bold;"><s:text
						name="surveyMaster.section" /></td>
				<td style="font-weight: bold;"><s:text name="question.code" /></td>
				<td style="font-weight: bold; width: 600px"><s:text
						name="question.name" /></td>
				<td><s:text name="estimated.hours"></s:text></td>
				<td><s:text name="question.verify"></s:text> <s:if
						test="verifiedSurvey==0">
						<button id="butModify" type="button">
							<s:text name="question.verify.modifyBut" />
						</button>
					</s:if></td>
			</tr>
			<s:iterator value="questions" status="questionsCount" var="questions">
				<tr>
					<td><s:property value="%{#questionsCount.count}" /></td>
					<td><s:property value="#questions.section.name" /></td>
					<td><s:property value="code" /></td>
					<td><s:property value="name" /></td>
					<s:if
						test="#questions.section.code=='S05' || questions.section.code=='S06'">
						<td><s:property value="defaultCatalogue.name" /></td>
					</s:if>
					<s:else>
						<td><s:text name="-" /></td>
					</s:else>
					<td align="center"><input type="checkbox" class="chkboxVerify"
						name="verifyQusIds" value='<s:property value="#questions.id" />' />
					</td>
				</tr>
			</s:iterator></table></div></div></div></div>
		<br/>
		
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">				
				<div class="aPanel surveyMasterInfoText_info" style="width: 100%">
				    <h2>
						<s:text name="info.question" /></h2>						
			
			<div class="flexItem">
			<div class="dynamic-flexItem" style="width: 100%">
			
				<p class="flexItem"><s:text name="lang.code" /></p>
				<p class="flexItem"><s:text name="lang.name" /></p>
			</div>
			<s:iterator value="surveyMaster.languagePreferences" status="stat"
				var="langPref">
				<s:if
					test='#langPref.lang.equalsIgnoreCase("en")||#langPref.lang.equalsIgnoreCase(getLoggedInUserLanguage())||getLoggedInUserLanguage().equalsIgnoreCase("en")'>
				<div class="dynamic-flexItem" style="width: 100%">
					<p class="flexItem"><s:property value="%{#langPref.lang}" /></p>
					<p class="flexItem"><s:property value="%{#langPref.name}" /></p>
				</div>
				</s:if>
			</s:iterator>
		</div>
		</div>
		</div>
		</div>
		</div>	
		<br />

		<div class="yui-skin-sam">
			<sec:authorize ifAllGranted="profile.surveyMaster.update">
				<span id="update" class="yui-button"><span
					class="first-child">
						<button type="button" class="edit-btn btn btn-success" onclick="onUpdate();">
							<FONT color="#FFFFFF"><b> <s:text name="edit.button" /></b></font>
						</button>
				</span></span>
			</sec:authorize>

			<sec:authorize ifAllGranted="profile.surveyMaster.delete">
				<span id="delete" class="yui-button"><span
					class="first-child">
						<button type="button" class="delete-btn btn btn-warning" onclick="onDelete(this);">
							<FONT color="#FFFFFF"><b> <s:text name="delete.button" /></b></font>
						</button>
				</span></span>
			</sec:authorize>

			<span id="cancel" class="yui-button"> <span
				class="first-child"><button type="button" class="back-btn btn btn-sts">
						<b> <FONT color="#FFFFFF"><s:text name="back.button" /></font></b>
					</button></span></span>	
</div>
	</s:form>


	<s:form name="cancelform" action="surveyMaster_list.action">
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="deleteform" action="surveyMaster_delete.action">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
	</s:form>

	<s:form name="updateform" action="surveyMaster_update.action">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
	</s:form>

	<s:form name="exportform">
		<s:hidden id="id" name="id" />
	</s:form>
	<script>
		/* YAHOO.util.Event
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
									jQuery(p_oEvent.target).attr("disabled",
											true);
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
						}); */
		
		function onUpdate() {
			document.updateform.id.value = document
					.getElementById('id').value;
			document.updateform.currentPage.value = document.form.currentPage.value;
			document.updateform.submit();
		}
		
		function onDelete(p_oEvent) {
			if (confirm('<s:text name="confirm.delete"/> ')) {
				jQuery(p_oEvent.target).attr("disabled",
						true);
				document.deleteform.id.value = document
						.getElementById('id').value;
				document.deleteform.currentPage.value = document.form.currentPage.value;
				document.deleteform.submit();
			}
		}
		function exportPdfData() {
			var selectedLanguage = $("#webUserLanguage").val();
			document.exportform.id.value = document.getElementById('id').value;
			document.exportform.action = 'surveyMaster_exportPdf.action?lang='
					+ selectedLanguage;
			document.exportform.submit();

		}
		function exportXlsData() {
			var selectedLanguage = $("#webUserLanguage").val();

			document.exportform.id.value = document.getElementById('id').value;
			document.exportform.action = 'surveyMaster_exportXls.action?lang='
					+ selectedLanguage;
			document.exportform.submit();
		}

		var exVerQusIds = '<s:property value="verifyQusIds" />';
		var loggedInuserLang = '<s:property value="loggedInUserLanguage" />';
		$(document)
				.ready(
						function(e) {
							$("#webUserLanguage").val(loggedInuserLang);
							exVerQusIds = JSON.parse(exVerQusIds);
							for (var i = 0; i < exVerQusIds.length; i++) {
								$('input[value="' + exVerQusIds[i] + '"]')
										.prop("checked", true);
							}
							$('.chkboxVerify').prop('disabled', true);
							var butToggle = false;
							$('#butModify')
									.on(
											'click',
											function(e) {
												$('.chkboxVerify').prop(
														'disabled', butToggle);
												if (butToggle) {
													var surveyId = $('#id')
															.val();
													var verifyQusIds = '';
													$
															.each(
																	$("input[name='verifyQusIds']:checked"),
																	function() {
																		verifyQusIds += 'verifyQusIds='
																				+ ($(this)
																						.val())
																				+ '&';
																	});
													$('#butModify').prop(
															'disabled', true);
													$
															.post(
																	"surveyMaster_createVerifyQuestions.action?surveyId="
																			+ surveyId
																			+ '&'
																			+ verifyQusIds,
																	function(
																			data) {
																		if (whatIsIt(data) == 'Object') {
																			if (data != null
																					&& data.success == '1') {
																				alert('<s:text name="question.verify.successMsg" />');
																			} else {
																				alert('<s:text name="save.not" />');
																			}
																		} else {
																			alert('<s:text name="fail.wrong" />');
																		}
																		$(
																				'#butModify')
																				.prop(
																						'disabled',
																						false);
																	});
												}
												butToggle = !butToggle;
											});

						});
	</script>
</body>
</html>