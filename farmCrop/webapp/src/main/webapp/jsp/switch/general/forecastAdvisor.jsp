<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">

function redirectForm()
{
	document.redirectform.action="forecastAdvisor_list.action";
	document.redirectform.submit();
}

function disablePopupAlert(){
	document.redirectform.action="forecastAdvisor_list.action";
	document.redirectform.submit();
}
function enableButton(){
	jQuery(".save-btn").prop('disabled',false);
}
function saveForeCastAdvisor(){
	var description=$("#descripeval").val();
	var selectedCrop=$("#selectedCrop").val();
	var maxiHumi=$("#maxiHumi").val();
	var miniHumi=$("#miniHumi").val();
	var maxiWind=$("#maxiWind").val();
	var miniWind=$("#miniWind").val();
	var maxiRain=$("#maxiRain").val();
	var miniRain=$("#miniRain").val();
	var maxiTemp=$("#maxiTemp").val();
	var miniTemp=$("#miniTemp").val();
	
	if(selectedCrop=="" &&  selectedCrop==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptyCrop')}" />';
		enableButton();
		hit=false;
		return false;
	}
	if(description=="" &&  description==0){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptydescription')}" />';
		enableButton();
		hit=false;
		return false;
	}
	if(maxiHumi=="" &&  maxiHumi==0 && miniHumi=="" &&  miniHumi==0 || maxiWind=="" &&  maxiWind==0 && miniWind=="" &&  miniWind==0 || miniRain=="" &&  miniRain==0 && maxiRain=="" &&  maxiRain==0 || maxiTemp=="" &&  maxiTemp==0 && miniTemp=="" &&  miniTemp==0 ){
		document.getElementById("validateError").innerHTML='<s:property value="%{getLocaleProperty('emptyvalue')}" />';
		enableButton();
		hit=false;
		return false;
	}
	
	$.post("forecastAdvisor_populateForeCastCreate.action",{selectedCrop:selectedCrop,description:description,maxiHumi:maxiHumi,miniHumi:miniHumi,maxiWind:maxiWind,miniWind:miniWind,maxiRain:maxiRain,miniRain:miniRain,miniTemp:miniTemp,maxiTemp:maxiTemp
	},function(data,result){
		if(result=='success')
		{
			document.getElementById("divMsg").innerHTML=result;
			document.getElementById("enableModal").click();
			
		}
	/* 	else
				{
			document.getElementById("divMsg").innerHTML=data;
			document.getElementById("enableModal").click();		
			
				} */
			
});
	

}
</script>

<button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>
	
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
					<s:text name="admin.forecastAdvisor" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="divMsg" align="center"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="disablePopupAlert()">
					<s:text name="closeBtn" />
				</button>
			</div>
		</div>

	</div>
</div>			

<s:form name="form" cssClass="fillform"
		action="forecastAdvisor_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="foreCastAdvisoryDetails.id" />
		</s:if>
		<s:hidden key="command" />
<div class="appContentWrapper marginBottom">
		
				<div class="formContainerWrapper">
					<div class="row">
					<div class="container-fluid">
						<div class="notificationBar">
						  <div class="error">
							<p class="notification">
								<span class="manadatory">*</span>
								<s:text name="reqd.field" />
							<div id="validateError" style="text-align: center;"></div>
							<div id="validatePriceError" style="text-align: center;"></div>
							</p>
                            </div>
						</div>
					</div>
				</div>

				<font color="red"> <s:actionerror /></font>
					<h2>
						<s:property value="%{getLocaleProperty('info.forecastAdvisor')}" />
					</h2>
					<div class="flexiWrapper filterControls">
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('crop')}" /> <span
							class="manadatory">*</span></label>
						<div class="form-element">
						<%-- <s:select id="selectedCrop" name="ForeCastAdvisoryDetails.forecastAdvisory.procurementProduct.name" list="procurementProductList" headerKey=""  theme="simple" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm select2" /> --%>
								 <s:select name="foreCastAdvisoryDetails.forecastAdvisory.procurementProduct.id" theme="simple" listKey="id"
									listValue="name" list="procurementProductList" headerKey=""
									headerValue="%{getText('txt.select')}"
									cssClass="form-control select2" id="selectedCrop" /> 
							</div>
						</div>
						
						
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('forecastadvisor')}" /> </label>
						<div class="form-element">
								<span  id="forecastadvisor"
								
										style="display: block; text-align: center">Humitidy</span>
							</div>
						</div>
						
							<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('minimum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.minimumHumi" theme="simple" maxlength="45"
									id="miniHumi" />

							</div>
						</div>
						
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('maximum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.maximumHumi" theme="simple" maxlength="45"
									id="maxiHumi" />

							</div>
						</div>
						
						
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('forecastadvisor')}" /> </label>
						<div class="form-element">
								<span  id="forecastadvisor "
										style="display: block; text-align: center">Wind Speed</span>
							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('minimum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.minimumWind" theme="simple" maxlength="45"
									id="miniWind" />

							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('maximum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.maximumWind" theme="simple" maxlength="45"
									id="maxiWind" />

							</div>
						</div>
						
							
						
						</div>
						</div>
						<div class="flexiWrapper filterControls">
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('forecastadvisor')}" /> </label>
						<div class="form-element">
								<span  id="forecastadvisor "
										style="display: block; text-align: center">Temperature</span>
							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('minimum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.minimumTemp" theme="simple" maxlength="45"
									id="miniTemp" />

							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('maximum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.maximumTemp" theme="simple" maxlength="45"
									id="maxiTemp" />

							</div>
						</div>
						
							
						<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('forecastadvisor')}" /></label>
							<span  id="forecastadvisor "
										style="display: block; text-align: center">Rainfall</span>
						<div class="form-element">
							
							</div>
						</div>
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('minimum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.minimumRain" theme="simple" maxlength="45"
									id="miniRain" />

							</div>
							</div>
						 <div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('maximum')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:textfield name="foreCastAdvisoryDetails.maximumRain" theme="simple" maxlength="45"
									id="maxiRain" />

							</div>
						</div> 
						</div>
	</div>
						
						
						<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
					<h2>
						<s:property value="%{getLocaleProperty('info.forecastAdvisor')}" />
					</h2>					
					
		<div class="flexiWrapper filterControls">
						<div class="flexi flexi10">
							<label for="txt"><s:property
									value="%{getLocaleProperty('Description')}" /><sup
								style="color: red;">*</sup></label>
								</div>
								<div class="flexi flexi10">
							<div class="form-element">
								<s:textarea name="foreCastAdvisoryDetails.forecastAdvisory.description" theme="simple" maxlength="255"
									id="descripeval" />

							</div>
						</div>
						</div>
						</div>
						</div>
						<div class="yui-skin-sam">
						<s:if test="command =='create'">
						<span class=""><span class="first-child">
								<button type="button" onclick="saveForeCastAdvisor()"
									class="save-btn btn btn-success">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span></span>
						</s:if>
						<s:else>
						
							
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span></s:else>	
				<span class="yui-button"><span class="first-child"><a
									id="cancelbutton" onclick="redirectForm();"
									class="cancel-btn btn btn-sts"> <font color="#FFFFFF">
											<s:text name="cancel.button" />
									</font>
								</a></span></span>
					

							
		
						</div>
						</s:form>
			<s:form name="redirectform" action="forecastAdvisor_list.action"
	method="POST">
</s:form>			
	
		
</body>
