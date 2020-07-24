<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>

	<div class="error"><sup>*</sup> <s:text name="reqd.field" />
	<div id="validateError" style="text-align: center; padding: 5px 0 0 0"></div>
	</div>

	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />
			<div class="appContentWrapper marginBottom">

				<div class="formContainerWrapper">
					
					<h2><s:text name="info.farmer"/></h2>
					<div class="flexiWrapper">
						
						<div class="flexform-item">
								<label for="txt"><s:property value="%{getLocaleProperty('answerDate')}" /></label>
						<div class="form-element">
							<s:textfield name="feedBackDate" id="calendar"
								readonly="true" theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								cssClass="form-control input-sm" />
						</div>
						</div>
						
						
						
						<div class="flexform-item">
								<label for="txt"><s:property value="%{getLocaleProperty('samithi')}" /></label>
						<div class="form-element">
							<s:select name="selectedGrp" list="grpList" onchange="listFarmer(this)" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="grp" />
						</div>
						</div>
						
					<%-- 	<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('village')}" /></label>
						<div class="form-element">
							<s:select name="selectedVillage" list="villageList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="village" onchange="listFarmer(this)" />
						</div>
						</div> --%>

						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('farmerHindi')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedFarmer" list="farmerList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="farmer"/>
						</div>
						</div>
						
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('ques1')}" /></label>
						<div class="form-element">
							<s:textfield name="filter.question1" cssClass="form-control" id="ques1"/>
						</div>
						</div>
						
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('ques2')}" /></label>
						<div class="form-element">
							<s:textfield name="filter.question2" cssClass="form-control" id="ques2"/>
						</div>
						</div>
						
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('ques3')}" /></label>
						<div class="form-element">
							<s:textfield name="filter.question3" cssClass="form-control" id="ques3"/>
						</div>
						</div>
						
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('ques4')}" /></label>
						<div class="form-element">
							<s:textfield name="filter.question4" cssClass="form-control" id="ques4"/>
						</div>
						</div>
					
					</div>

					<div class="yui-skin-sam" id="savebutton">
						<span class="yui-button"><span class="first-child">
						<button  id="saveBtn" type="button" class="save-btn btn btn-success" onclick="onSave();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b></font>
						</button>
						</span></span>
						
					</div>

				</div>
			</div>


	</s:form>
	<script type="text/javascript">
	
	jQuery(document).ready(function(){
		
		$( "#calendar" ).datepicker(

				{
				
			  // format: 'mm/dd/yyyy',
	          endDate: '+0d',
	           autoclose: true,
				beforeShow : function()
				{
				jQuery( this ).datepicker({ maxDate: 0 });
				},
				changeMonth: true,
				changeYear: true

				}

				);
		
		$("#calendar").datepicker().datepicker("setDate", new Date());
		
	});
	
	
	function listFarmer(call){	
	jQuery.post("farmerFeedBackService_populateFarmer.action",{id:call.value,dt:new Date(),selectedGrp:call.value},function(result){
			insertOptions("farmer",$.parseJSON(result));		
		});	
	}
	
	
	function onSave(){
		var selectedFarmer = document.getElementById('farmer').value;
		var selQuesOne = $("#ques1").val();
		var selQuesTwo = $("#ques2").val();
		var selQuesThree = $("#ques3").val();
		var selQuesFour = $("#ques4").val();
		var hit=true;
		if(selectedFarmer==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectFarmer')}" />');
			hit = false;
		}else if(selQuesOne==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('emptyQues1')}" />');
			hit = false;
		}else if(selQuesTwo==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('emptyQues2')}" />');
			hit = false;
		}else if(selQuesThree==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('emptyQues3')}" />');
			hit = false;
		}else if(selQuesFour==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('emptyQues4')}" />');
			hit = false;
		}
		
		
		if(hit){
		document.form.action="farmerFeedBackService_create.action";
		document.form.submit();
		}
	}

	</script>

</body>