<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

</head>
<body>

<script type="text/javascript">
var survey = '';
        $(document).ready(function(){
        	$(".otrCatTypes").hide();
        	$("#rmvCatTypeBtn").hide();
	        $("#buttonAdd").on('click', function (event) {  
	              event.preventDefault();
	              var el = $(this);
	              el.prop('disabled', true);
	              setTimeout(function(){el.prop('disabled', false); }, 1000);
	        });
        });
     function otrFarmCatTyz(val){
	    		var val= $("#typez").val();
	    			$("#typez").val(-1);
	    	 		$(".otrCatTypes").show();
	    	 		$("#addCatTypeBtn").hide();
	    	 		$("#rmvCatTypeBtn").show();
	    	 	
	    	 }
	        function removeOtrFarmCatTyz(){
	        	$(".otrCatTypes").hide();
	        	$("#typez").val(-1);
	        	$("#rmvCatTypeBtn").hide();
	        	$("#addCatTypeBtn").show();
	        }
	        
	        $('#saveCatalogueNameBtn').click(function(e){
				//alert("inside save");
				
				
			 });
	        function saveCatTypz() {
	        	saveCatalogueType();
	        }
	        
	        
	        function onSubmit(){
	        	document.form.submit();
	        }
	       /*  function saveCatalogueType(){
	        
	        	
	        	var catalogueName= $('#catalogueName').val();
	        	if(catalogueName==""){
					document.getElementById("validateError").innerHTML='<s:text name="empty.catType"/>';
					valid=false;
				}
				else{
					jQuery.post("catalogue_populateCatalogueType.action",{catalogueName:catalogueName},function(result){
						if(result!=0)
						{
							insertOptions("typez",JSON.parse(result));
							document.getElementById("model-close-edu-btn").click();	
						}
						else
						{
							document.getElementById("validateError").innerHTML='<s:text name="empty.catType"/>';

		
						}
					});
				}
	        } */
	      /*   $("#addCatalogueType").on('click', function (){
				 $('#catalogueName').val('');
				document.getElementById("validateError").innerHTML="";
				//alert("inside add");
		    }); */
	        
	        
	        function buttonCancelCatalogueName(){
	    		//refreshPopup();
	    		
	    		document.getElementById("model-close-edu-btn").click();	
	         }

	    	function closeButton(){
	    		refreshPopup();
	    		refreshEduPopup();
	    	}
			
        </script>

<s:form name="form" cssClass="fillform" action="catalogue_%{command}" method="post" enctype="multipart/form-data">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:if test='"update".equalsIgnoreCase(command)'>
	<s:hidden key="farmCatalogue.id" />
	</s:if>
	<s:hidden key="command" />
				<div class="appContentWrapper marginBottom">
					<div class="error">
					<sup>*</sup>
					<s:text name="reqd.field" />
					<s:actionerror />
					<s:fielderror />
				</div>
				<div class="formContainerWrapper">
				<h2>
				<s:property value="%{getLocaleProperty('info.catalogue')}" />
				</h2>
						<div class="flexiWrapper">
							
							<div class="flexform-item">
									<label for="txt"><s:text name="catalogue.typez" /></label>
									<div class="form-element">
										<s:select id="typez" name="farmCatalogue.typez" list="typezList"
							headerKey="" theme="simple"
							headerValue="%{getText('txt.select')}"
							cssClass="form-control select2" />
									</div>
							</div>
							
							<div class="flexform-item">
									<label for="txt"><s:text name="catalogue.name" /><sup style="color: red;">*</sup></label>
									<div class="form-element">
										<s:textfield name="farmCatalogue.name" theme="simple"
							cssClass="form-control input-sm" maxlength="250" />
									</div>
							</div>
							
						<%-- 	<div class="flexform-item">
									<label for="txt"><s:text name="catalogue.dispName" /></label>
									<div class="form-element">
									<s:textfield name="farmCatalogue.dispName" theme="simple"
							cssClass="form-control input-sm" maxlength="250" />
									</div>
							</div> --%>
							
							<div class="flexform-item">
									<label for="txt"><s:text name="status" /><sup style="color: red;">*</sup></label>
									<div class="form-element">
									<s:radio list="statusMap" name="farmCatalogue.status" value="statusDeafaultVal"/>
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
					<s:iterator value="farmCatalogue.languagePreferences" status="stat"
						var="langPref">
						<tr class="odd">
							<td width="20%"><s:property value="%{#langPref.lang}" /><sup style="color: red;">*</sup></td>
							<td width="40%"><s:textfield value="%{#langPref.name}"
									name="farmCatalogue.languagePreferences[%{#stat.index}].name"
									id="langPrefName" theme="simple" /></td>
							<td width="40%"><s:textfield value="%{#langPref.info}"
									name="farmCatalogue.languagePreferences[%{#stat.index}].info"
									theme="simple" /> <s:hidden
									name="farmCatalogue.languagePreferences[%{#stat.index}].id" /> <s:hidden
									name="farmCatalogue.languagePreferences[%{#stat.index}].code" /> <s:hidden
									name="farmCatalogue.languagePreferences[%{#stat.index}].shortName" />
								<s:hidden
									name="farmCatalogue.languagePreferences[%{#stat.index}].lang" /> <s:hidden
									name="farmCatalogue.languagePreferences[%{#stat.index}].type" /></td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</div>
						
							<div class="yui-skin-sam">
			<s:if test="command =='create'">
				<span id="button" class=""><span class="first-child">
						<button type="button" id="buttonAdd" onClick="event.preventDefault();onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<s:else>
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success">
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

				</div>
					</div>	
				
	<br />


</s:form>
<s:form name="cancelform" action="catalogue_list.action?survey=1">
    <s:hidden key="currentPage"/>
</s:form>
</body>