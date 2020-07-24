<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%-- <%@ include file="/jsp/common/report-assets.jsp"%> --%>
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	jQuery(document).ready(function() {
		//$('#startDate').datepicker('setDate', new Date());
		var test='<s:property value="%{getGeneralDateFormat()}" />';
		 $.fn.datepicker.defaults.format = test.toLowerCase();
		 $('#date').datepicker('setDate', new Date());
	});

	function validate() {
		var error=true;
		var img=$('#farmerImage').val();
		var date=$('#date').val();
		var type=$('#type').val();
		var season=$('#season').val();
		var last=document.getElementById('farmerImage').value;
		
		var fileExt = last.split('.').pop();
		var filename = last.substring(last.lastIndexOf("\\") + 1, last.length);
		//alert(filename);
		$('#fileN').val(filename);
		if(fileExt == 'jpg' || fileExt == 'JPG'||fileExt == 'jpeg' || fileExt == 'JPEG'||fileExt == 'png' || fileExt == 'PNG'){
			document.getElementById("validateError").innerHTML='<s:text name="Please Select DOC,XLS &PDF"/>';
    		error = false;
		}
		
		 if(isEmpty(img) || img==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please select a file"/>';
        		error = false;
	   			  } 
		 if(isEmpty(date) || date==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please Select Date"/>';
     		error = false;
	   			  } 
		 
		 if(isEmpty(type) || type==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please Select Type"/>';
  		error = false;
	   			  } 
		 if(isEmpty(season) || season==''){
	   			document.getElementById("validateError").innerHTML='<s:text name="Please Select season"/>';
		error = false;
	   			  } 
		
		if(error==true){
			document.form.submit();
		}
		
		
	}
	$("#startDate").datepicker({
		endDate : '+0d',
		autoclose : true,
		beforeShow : function() {
			jQuery(this).datepicker({
				maxDate : 0
			});
		},
		changeMonth : true,
		changeYear : true
	});
	  function onCancel() {
          document.cancelform.submit();
      }
</script>
<s:form name="cancelform" action="samithi_list.action">
	<s:hidden key="currentPage" />
</s:form>
<s:form name="form" cssClass="fillform" action="samithiIcs_create" method="post" enctype="multipart/form-data" >
<s:hidden name="fileName" id="fileN" />
			<div class="appContentWrapper marginBottom ">
				 <div class="error">
							<p class="notification">
								<%-- <span class="manadatory">*</span>
								<s:text name="reqd.field" /> --%>
							<div id="validateError" style="text-align: center;"></div>
							<div id="validatePriceError" style="text-align: center;"></div>
							</p>
                            </div>
			<div class="formContainerWrapper">
				<%-- <div class="ferror" id="errorDiv" style="color: #ff0000">
					<s:actionerror />
					<s:fielderror />
				</div> --%>
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmerInfo" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.scope')}" />
					</a>
				</h2>
			</div>
	<div class="flexform-item ">
		<label for="txt"><s:property
				value="%{getLocaleProperty('fieldStatus')}" /> </label>
		<div class="form-element">
			<s:select cssClass="form-control " id="type" name="samithi.type"
				list="icsStatusList" headerKey=""
				headerValue="%{getText('txt.select')}" />
		</div>
	</div>
	<div class="flexform-item ">
		<label for="txt"> <s:property
				value="%{getLocaleProperty('season')}" /> <sup
			style="color: red;">*</sup>
		</label>
		<div class="form-element">
			<%-- <s:select cssClass="form-control select2" id="cropSeasonValue"
									name="cropSeasonCode" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="cropSeasonsMap" /> --%>

			<s:select cssClass="form-control select2" id="season"
				name="samithi.season" headerKey="" listKey="key" listValue="value"
				headerValue="%{getText('txt.select')}" list="seasonList" />

		</div>
	</div>

	<div class="flexform-item ">
		<label for="txt"> <s:property
				value="%{getLocaleProperty('date')}" />
		</label>
		<div class="form-element">
			<%-- <s:textfield name="samithi.date" id="date" readonly="true"
				theme="simple"
				data-date-format="%{getGeneralDateFormat().toLowerCase()}"
				data-date-viewmode="years"
				cssClass="date-picker form-control input-sm" size="20" /> --%>
				<s:textfield id="date" data-provide="datepicker" readonly="true" name="date"
										theme="simple"
										data-date-format="%{getGeneralDateFormat().toLowerCase()}"
										
										cssClass="date-picker form-control input-sm" size="20" />
		</div>
	</div>

	<div class="flexform-item">
		<label for="txt"> <s:property
				value="%{getLocaleProperty('Scope Certificate')}" /> <sup id="mandatory"
			style="color: red;">*</sup> <span style="font-size: 8px"> </span>
		</label>
		<div class="form-element">
			<s:file name="farmerImage" id="farmerImage" cssClass="form-control" />

		</div>
	</div>
		<div class="margin-top-10">
			
				<span class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="validate()">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
	
<%-- 			<s:else>
				<span class=""><span class="first-child">
						<button type="button" onClick="validate()"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else> --%>
			<button type="button" onclick="onCancel();"
						class="cancel-btn btn btn-warning">
						<b> <FONT color="#FFFFFF"> <s:text name="cancel.button" />
						</FONT>
						</b>
					</button>
		</div></div>
</s:form>


</div>














