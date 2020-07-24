<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>



<style>
.accrodianTxt {
	color: black;
	font-style: bold;
}

.accrodianTxt:after {
	/* symbol for "opening" panels */
	font-family: 'Glyphicons Halflings';
	/* essential for enabling glyphicon */
	content: "\2212"; /* adjust as needed, taken from bootstrap.css */
	float: right; /* adjust as needed */
	color: black; /* adjust as needed */
}

.collapsed:after {
	/* symbol for "collapsed" panels */
	content: "\2b"; /* adjust as needed, taken from bootstrap.css */
}

.aspect-detail TD:nth-child(2) {
	font-weight: normal;
}
#tabs.ui-state-default a,#tabs.ui-state-default a:link,#tabs.ui-state-default a:visited {
    color:#fff!important;
}
</style>

<script>
$(function () {
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.remove"/>']").addClass("btn btn-danger");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.add"/>']").addClass("btn btn-small btn-success fa fa-step-forward");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.removeAll"/>']").addClass("btn btn-warning");
	jQuery("#dynOpt").find("input[type='button'][value='<s:text name="question.addAll"/>']").addClass("btn btn-sts");

	
});	
function onCancel(btn)
{
	document.cancelform.submit();
}

function submitForm(btn)
{
	
	
	selectAllOptionz('selectedSections');
	selectAllOptionz('selectedQuestions');
	selectAllOptionz('question');

	$('#surveyType').prop('disabled',false);
	
    document.form.submit();
}
function selectAllOptionz(selectId){
	document.getElementById(selectId).multiple = true; //to enable all option to be selected
	for (var x = 0; x < document.getElementById(selectId).options.length; x++)//count the option amount in selection box
	    document.getElementById(selectId).options[x].selected = true;
}

function disableTr(obj,bool){
	 $(obj).closest('tr').find('input, select, textarea').each(function(){
		    if($(this).prop("type")!='button'&&bool){
			   $(this).val('');
		    }
			$(this).prop('disabled', bool);
	 });
}

function populateQuestions(){
	$('#question').empty();
	var selVals=new Array();
	$('#selectedQuestions option').each(function(){
    selVals.push($(this).val());
	});
	
	//alert(selVals); 
	$('#selectedQuestions').empty();
	reloadSortable();
	var urlFormSecCodes='';
    var surveyType=$('#surveyType').val();
	$("#selectedSections option").each(function(){
		urlFormSecCodes+='sectionCodes='+this.value+'&';
	});
	if(urlFormSecCodes!==''){
		$.getJSON('surveyMaster_populateQuestions.action?'+urlFormSecCodes+'&surveyType='+surveyType+'&selVals='+selVals,function(jd){
            if(jd!=null&&jd!=''){
            	 $.each(jd, function(k, v) {
        			 $("#question").append("<option value='"+k+"'>"+v+"</option>");
        	     });
             }
		});
		$.getJSON('surveyMaster_populateSelectedQuestions.action?'+urlFormSecCodes+'&surveyType='+surveyType+'&selVals='+selVals,function(jd){
            if(jd!=null&&jd!=''){
            	 $.each(jd, function(k, v) {
        			 $("#selectedQuestions").append("<option value='"+k+"'>"+v+"</option>");
        	     });
             }
		});
	}
	disableSurveyType();
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

function disableSurveyType(){
	var secCount=$('#selectedSections > option').length;
	//alert(secCount);
	if(secCount>0){
		$('#surveyType').prop('disabled',true);
	}else{
		$('#surveyType').prop('disabled',false);
	}
}


	
	function populateIcoDataList()
	{
		resetCombo("infrasMasterName");
	    var dtlvltyp=$("input:radio[name='infraCommunityOthers']:checked").val();
	    
	    $('#section').empty();
		$('#selectedSections').empty();
		$('#question').empty();
		$('#selectedQuestions').empty();
		$("#sortable").empty();
		
		if(dtlvltyp==="I")
		{
			$("#infrasMasterName").attr("disabled",false);
			jQuery.post("surveyMaster_populateInfraTypeDataList",{infraCommOther:dtlvltyp},function(resp){                 
		    	addOptionsToCombo("infrasMasterName",JSON.parse(resp));
			});
		}
		else if(dtlvltyp==="C")
		{
			$("#infrasMasterName").attr("disabled",true);
		    var dataLevelId = 5;
			$.getJSON("surveyMaster_populateSectionsByCommunity.action?dataLevelId="+dataLevelId,function(data){		
				 $.each(data, function(k, v) {
					 $("#section").append("<option value='"+k+"'>"+v+"</option>");
			    });
			});
		}
		else
		{
			$("#infrasMasterName").attr("disabled",true);
			$.getJSON("surveyMaster_populateSectionsByOthers.action",function(data){		
				$.each(data, function(k, v) {
					 $("#section").append("<option value='"+k+"'>"+v+"</option>");
			    });
			});
		}
	}
	function populateSections(infraTypeCode)
	{
		$('#section').empty();
		$('#selectedSections').empty();
		$('#question').empty();
		$('#selectedQuestions').empty();
		$("#sortable").empty();
		
		$.getJSON("surveyMaster_populateInfraTypeSectionsByDataLevelCode.action?infraTypeCode="+infraTypeCode,function(data){		
			 $.each(data, function(k, v) {
				 $("#section").append("<option value='"+k+"'>"+v+"</option>");
		    });
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
	 var date = new Date();
	 var currentMonth = date.getMonth();
	 var currentDate = date.getDate();
	 var currentYear = date.getFullYear()+50;

	
	
	$("#calendarFrom").datepicker({
		maxDate: new Date(currentYear, currentMonth, currentDate),
		minDate :0,
		autoclose:true
	});

	 $('#calendarTo').datepicker({
		 maxDate: new Date(currentYear, currentMonth, currentDate),
		 minDate :0,
		 autoclose:true
		 });

	 $('#divSection').on('click',function(e){
		 populateQuestions();
    });

	 $('#divQuestion').on('click',function(e){
		 reloadSortable();
     });
	 reloadSortable();
	 if('<s:property value="surveyMapped" />' === "true"){
		 disableTr('.edit',true);
			$("#surveyAge").attr("disabled","disabled").trigger("chosen:updated");
			$('#butAdd').hide();
		 //$('#surveyAge_chosen').attr("disabled","disabled");
	 }

	 if('<s:property value="command"/>'=='create')
	 {
		 $('#infraCommunityOthersO').attr('checked', true);
		 $("#infrasMasterName").attr("disabled",true);
	 }
	 else
	 {
		 
	 	 if('<s:property value="surveyMaster.infraCommunityOthers"/>'==="I")
		 {
			 var dtlvltyp = '<s:property value="surveyMaster.infraCommunityOthers"/>'; 
			 var infraTypeCode = '<s:property value="surveyMaster.infraMasterName"/>';
			 $('#infraCommunityOthersI').attr('checked', true);
			 $("#infrasMasterName").attr("disabled",false);
			
			 //alert(dtlvltyp+" "+infraTypeCode);

			 resetCombo("infrasMasterName");
			 
			 $.post("surveyMaster_populateInfraTypeDataList",{infraCommOther:dtlvltyp},function(resp){                 
		    	 addOptionsToCombo("infrasMasterName",JSON.parse(resp));

		    	 $('#infrasMasterName').val('IMP0001');
		    	 $.each(JSON.parse(resp), function(k, v) 
				 {
					 if(v.value === '<s:property value="surveyMaster.infraMasterName"/>')
					 {
						 $("#infrasMasterName").prop('selectedIndex', k);
					 }
			     });
			 });
		 }
	 	 else if('<s:property value="surveyMaster.infraCommunityOthers"/>'==="C")
		 {
	 		 $('#infraCommunityOthersC').attr('checked', true);
			 $("#infrasMasterName").attr("disabled",true);
			
		 }
	 	 else
	 	 {
	 		 $('#infraCommunityOthersO').attr('checked', true);
			 $("#infrasMasterName").attr("disabled",true);
			
	 	 }
	 }
});	     


function addSurveyAgeGroup(){
	enableSurveyAgeGroupAlert();
}


function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
    	      return false;
    return true;
}

</script>

<head>
<META name="decorator" content="swithlayout">
</head>

<body>

	<style>
.select2-container .select2-selection--single {
	height: 34px;
	border: 1px solid #d5d5d5;
}

.select2-container--default .select2-selection--single .select2-selection__rendered
	{
	line-height: 34px;
}
</style>

	<s:form name="form" cssClass="fillform" action="surveyMaster_%{command}"
		method="post" enctype="multipart/form-data" id="target">

		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden id="jsonString" name="farmer.jsonString" />
		<s:hidden id="dateOfBirth" name="dateOfBirth" />
		<s:hidden id="loanRepaymentDate" name="loanRepaymentDate" />
		<s:hidden name="agriTestJson" id="agriTestJson" />
		<s:hidden id="income" name="farmer.AGRICULTURE_ACTIVITIES" />
		<s:hidden id="sourceIncomeId" name="sourceIncomeId" />
		<s:hidden id="healthAssesmentJSON" name="healthAssesmentJSON" />
		<s:hidden id="selfAssesmentJSON" name="selfAssesmentJSON" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="surveyMaster.id" />
		</s:if>
		<s:hidden key="command" />
		<s:hidden id="farmerDynamicDatas" name="farmerDynamicDatas" />

		<s:hidden id="farmerDynamicValIds" name="farmerDynamicValIds" />
		<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />

		<div class="appContentWrapper marginBottom farmer_info">
			<div class="formContainerWrapper">
			<div class="ferror" id="errorDiv" style="color: #ff0000">
				<s:actionerror />
				<s:fielderror />
			</div>
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#surveyMasterInfo" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.surveyMaster')}" />
					</a>
				</h2>
			</div>
			
			
			<div id="surveyMasterInfo" class="panel-collapse collapse in">
				<div class="flexform">


					<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="flexform-item surveyMasterCode">
							<label for="txt"> <s:text name="surveyMaster.code" />
							</label>
							<div class="form-element">
								<s:property value="surveyMaster.code" />
							</div>
							<s:hidden name="surveyMaster.code" />
						</div>
					</s:if>


					<div class="flexform-item surveyMasterName">
						<label for="txt"> <s:text name="surveyMaster.name" /> <sup style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield name="surveyMaster.name" theme="simple"
								maxlength="150" />
							
						</div>
					</div>
					<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="flexform-item ">
							<label for="txt"> <s:text name="surveyMaster.section.dataLevel" />
							</label>
							<div class="form-element">
								<s:property value="surveyMaster.dataLevel.name" />
							</div>
							<s:hidden name="surveyMaster.dataLevel.code" />
						</div>
					</s:if>
					<s:else>
					<div class="flexform-item ">
						<label for="txt"> <s:text name="surveyMaster.section.dataLevel" /> <sup style="color: red;">*</sup>
						</label>
						<div class="form-element">
						<s:select listKey="key" listValue="value" list="dataLevelMap"
								name="surveyMaster.dataLevel.code" cssClass="form-control" headerKey="-1"
								headerValue="%{getText('txt.select')}" id="dataLevel"								
								/>
						
							
						</div>
					</div>
</s:else>

					<div class="flexform-item surveyMasterDescription">
						<label for="txt"> <s:text name="surveyMaster.description" />
						</label>
						<div class="form-element">
							<s:textfield size="150" name="surveyMaster.description"
								theme="simple" maxlength="150" />
						</div>
					</div>


					<div class="flexform-item surveyMasterSurveyType">
						<label for="txt"> <s:text name="surveyMaster.surveyType" /> <sup style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:select listKey="key" listValue="value" list="surveyTypes"
								name="surveyMaster.surveyType.id" cssClass="form-control" headerKey="-1"
								headerValue="%{getText('txt.select')}" id="surveyType"								
								onchange="noOfYearsControl();populateQuestions();" />
							<label
								style="color: red; size: 20px;" id="surveyTypeWarning"
								class="hide"><s:text name="surveyTypeWarningText" /></label>
						</div>
					</div>

				
					
																
						<div class="flexform-item surveyMasterStatus">
							<label for="txt"><s:text name="surveyMaster.status" /></label>
							<div class="form-element">
								<s:checkbox name="surveyMaster.statusB" theme="simple" />
								<s:text name="active" />
							</div>							
						</div>
				

				<div class="flexiWrapper">
				<div class="flexi flexi10 flexi flexi10-flex-full" style="width: 100%; padding-top: 10px;">						
					 
					<div id="dynOpt" class="">
					<label>
						<s:text name="surveyMaster.Section" /> <sup style="color: red;">*</sup>
					</label>
				
					<div class="form-element">
						<div style="width: 100%">
							<div class="pickListClass" id="divSection"
								style="float: left; width: 98%">
								<s:text name="surveyMaster.availSections"
									var="availSectionsTitle" />
								<s:text name="surveyMaster.selectedSections"
									var="selectedSectionsTitle" />
								<s:text name="question.add" var="add" />
								<s:text name="question.addAll" var="addAll" />
								<s:text name="question.remove" var="remove" />
								<s:text name="question.removeAll" var="removeAll" />
								<s:optiontransferselect id="section" cssClass="form-control "
									cssStyle="width:500px;height:250px;overflow-x:auto;"
									doubleCssStyle="width:500px;height:250px;overflow-x:auto;"
									doubleCssClass="form-control" buttonCssClass="optTrasel" allowSelectAll="false"
									buttonCssStyle="font-weight:bold!important;"
									allowUpDownOnLeft="false" labelposition="top"
									allowUpDownOnRight="false" name="surveyMaster.availSections"
									list="avilSections" listKey="key" listValue="value"
									leftTitle="%{availSectionsTitle}"
									rightTitle="%{selectedSectionsTitle}" headerKey="headerKey"
									doubleName="surveyMaster.sectionCodes"
									doubleId="selectedSections" doubleList="selectedSections"
									doubleHeaderKey="doubleHeaderKey"
									addAllToLeftLabel="%{removeAll}" addAllToRightLabel="%{addAll}"
									addToLeftLabel="%{remove}" addToRightLabel="%{add}" />
							</div>
						</div>
					</div>
					<div class="flexi flexi10 flexi flexi10-flex-full" style="width: 100%; padding-top: 10px;">						
						
						<div id="dynOpt" class="">
						<label>
							<s:text name="surveyMaster.questions" /> <sup style="color: red;">*</sup>
						</label>
					<div class="form-element">					
						<div class="form-element">
							<div style="width: 100%">
								<div class="pickListClass" id="divQuestion"
									style="float: left; width: 98%">
									<s:text name="surveyMaster.availQuestions"
										var="availQuestionsTitle" />
									<s:text name="surveyMaster.selectedQuestions"
										var="selectedQuestionsTitle" />
									<s:text name="question.add" var="add" />
									<s:text name="question.addAll" var="addAll" />
									<s:text name="question.remove" var="remove" />
									<s:text name="question.removeAll" var="removeAll" />
									<s:optiontransferselect id="question" cssClass="form-control "
									cssStyle="width:500px;height:250px;overflow-x:auto;"
									doubleCssStyle="width:500px; height:250px;overflow-x:auto;"
										buttonCssClass="optTrasel" allowSelectAll="false"
										buttonCssStyle="font-weight:bold!important;"
										allowUpDownOnLeft="false" labelposition="top"
										allowUpDownOnRight="false"
										name="surveyMaster.availQuestionCodes" list="avilQuestions"
										listKey="key" listValue="value"
										leftTitle="%{availQuestionsTitle}"
										rightTitle="%{selectedQuestionsTitle}" headerKey="headerKey"
										doubleName="surveyMaster.questionCodes"
										doubleId="selectedQuestions" doubleList="selectedQuestions"
										doubleHeaderKey="doubleHeaderKey"
										addAllToLeftLabel="%{removeAll}"
										addAllToRightLabel="%{addAll}" addToLeftLabel="%{remove}"
										addToRightLabel="%{add}" />
								</div>
								
							</div>
						</div>
					</div>
					</div>
					
					</div>	
					</div>			
				</div>
				</div>
				</div>
		</div>
		</div>
		<div class="appContentWrapper marginBottom pers_info">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion" href="#infoQn"
						class="accrodianTxt"> <s:text name="info.QN" />
					</a>

				</h2>
			</div>

				<div id="infoQn" class="panel-collapse collapse in">
				<div class="form-element">
					<ul id="sortable">
					</ul>
				</div>				
				</div>
				</div>
					<div class="appContentWrapper marginBottom pers_info">
						<div class="formContainerWrapper">
							<h2>
								<a data-toggle="collapse" data-parent="#accordion"
									href="#qnOrder" class="accrodianTxt"> <s:text
										name="lang.code" /> <s:text name="lang.name" /> <label
									style="color: red; size: 20px;"><s:text
											name="lang.name.info" /></label>
								</a>

							</h2>
						</div>


					<div id="qnOrder" class="panel-collapse collapse in">
						
							<s:iterator value="surveyMaster.languagePreferencesList"
								status="stat" var="langPref">
								<div class="flexform" style="width: 50%">

								<s:if
									test='#langPref.lang.equalsIgnoreCase("en")||#langPref.lang.equalsIgnoreCase(getLoggedInUserLanguage())||getLoggedInUserLanguage().equalsIgnoreCase("en")'>

									<div class="flexform-item languageInfo" style="width: 100%">
										<label for="txt"><s:property value="%{#langPref.lang}" /></label>
										<div class="form-element">
											<s:textfield value="%{#langPref.name}"
												name="surveyMaster.languagePreferencesList[%{#stat.index}].name"
												theme="simple" maxlength="150" />
											<!-- <sup style="color: red;">*</sup> -->
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].id" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].code" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].shortName" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].lang" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].type" />
										</div>
										</div>
								</s:if>
								<s:else>									
										<div class="flexform-item hide">
											<label for="txt"> <s:property
													value="%{#langPref.lang}" /></label>										
										<div class="form-element hide">
											<s:textfield value="%{#langPref.name}"
												name="surveyMaster.languagePreferencesList[%{#stat.index}].name"
												theme="simple" maxlength="150" />
											<sup style="color: red;">*</sup>
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].id" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].code" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].shortName" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].lang" />
											<s:hidden
												name="surveyMaster.languagePreferencesList[%{#stat.index}].type" />
										</div>
									</div>
								</s:else>
								</div>
							</s:iterator>

						</div>					

				</div>
				


		<div class="flexItem flex-layout flexItemStyle">
			<div class="button-group-container">
				<s:if test="command =='create'">
					<button type="submit" id="buttonAdd1"
						onclick="event.preventDefault();submitForm(this);"
						class="save-btn btn btn-sts">
						<font color="#FFFFFF"> <b><div>
									<s:text name="save.button" /></b>
						</font>
					</button>&nbsp;&nbsp;&nbsp;
					</s:if>
				<s:else>
					<button type="submit" id="buttonUpdate"
						onclick="event.preventDefault();submitForm(this);"
						class="save-btn btn btn-success">
						<font color="#FFFFFF"> <b><div>
									<s:text name="update.button" /></b>
						</font>
					</button>&nbsp;&nbsp;&nbsp;
				</s:else>
				<button type="button" onclick="onCancel();"
					class="cancel-btn btn btn-warning">
					<b> <FONT color="#FFFFFF">
							<div>
								<s:text name="cancel.button" />
							</div>
					</FONT>
					</b>
				</button>
			</div>
		</div>


		

	</s:form>
<s:form name="cancelform" action="surveyMaster_list.action">
		<s:hidden key="currentPage" />
	</s:form>


</body>