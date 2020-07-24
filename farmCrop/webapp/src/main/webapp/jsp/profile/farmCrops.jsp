<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>

<s:form name="form" cssClass="fillform" action="farmCropsMaster_%{command}" method="POST"  enctype="multipart/form-data">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
		<s:if test='"update".equalsIgnoreCase(command)'>
		<s:hidden key="farmCropsMaster.id" />
	</s:if>
	<s:hidden key="command" />
		<div class="appContentWrapper marginBottom farmer_info">
			<div class="formContainerWrapper">
			<div class="ferror" id="errorDiv" style="color: #ff0000">
					<s:actionerror />
					<s:fielderror />
				</div>
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmerInfo" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.farmCropsMaster')}" />
					</a>
				</h2>
			</div>
			
					<div class="flexiWrapper" >
					<div class="flexi flexi10">
				
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmCropsMaster.name')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
							<s:textfield name="farmCropsMaster.name" theme="simple" maxlength="35"/>
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('kmlFile')}" /> <span
							style="font-size: 8px"> <s:text name="kml.fileTypes" />
								<font color="red"> <s:text name="kmlSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:file name="farmCropsMaster.kmlFile" id="kmlFile" cssClass="form-control" />
						</div>
					</div>
					
			</div>
</div>


		
	<div class="flexItem flex-layout flexItemStyle">
			<div class="button-group-container">
				<s:if test="command =='create'">
					<button type="submit" id="buttonAdd1"						
						class="save-btn btn btn-sts">
						<font color="#FFFFFF"> <b><div>
									<s:text name="save.button" /></b>
						</font>
					</button>&nbsp;&nbsp;&nbsp;
					</s:if>
				<s:else>
					<button type="submit" id="buttonUpdate"						
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
<s:form name="cancelform" action="farmCropsMaster_list.action">
	<s:hidden key="currentPage" />
</s:form>
<script type="text/javascript">
function onSubmit(btn){
	jQuery(btn).attr("disabled",true);

	var hit=0;
	var file=document.getElementById('kmlFile').files[0];		
	var filename=document.getElementById('kmlFile').value;
	var fileExt=filename.split('.').pop();	
	if(file != undefined){
		if(fileExt=='kml' || fileExt=='KML' || fileExt=='kmz'||fileExt=='KMZ')
		{ 					
			if(file.size>5000001){
				alert('<s:text name="kmlFileSizeExceeds"/>');	
				hit=1;
			}
		}else{
			alert('<s:text name="invalidFileExtension"/>');
			hit=1;			
			}
	}
	if(hit===0){
		
		document.form.submit();
		}else{
			jQuery(btn).attr("disabled",false);
			return false;
		}
}

function onCancel() {

		window.location.href="farmCropsMaster_list.action";
     
}
</script>
</body>