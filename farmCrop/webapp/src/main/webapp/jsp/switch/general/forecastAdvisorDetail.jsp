<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<s:form name="form" cssClass="fillform">
<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="foreCastAdvisoryDetails.id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
<div class="fullwidth">
<s:hidden key="foreCastAdvisoryDetails.id" id="foreCastAdvisoryDetailsId" />
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:property value="%{getLocaleProperty('info.forecastAdvisor')}" /></h2>
					 <s:if test='branchId==null'>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="app.branch" /></p>
						<p class="flexItem"><s:property value="%{getBranchName(ForeCastAdvisoryDetails.branchId)}" /></p>
					</div>
					</s:if>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="crop" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.forecastAdvisory.procurementProduct.name" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('humitityMin')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.minimumHumi" /></p>
					</div>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('humitityMax')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.maximumHumi" /></p>
					</div>
						
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('windMin')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.minimumWind" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('windMax')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.maximumWind" /></p>
					</div>
						
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('tempMin')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.minimumTemp" /></p>
					</div>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('tempMax')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.maximumTemp" /></p>
					</div>
						
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('rainMin')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.minimumRain" /></p>
					</div>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:property value="%{getLocaleProperty('rainMax')}" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.maximumRain" /></p>
					</div>
					
					</div>
					<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<%-- <s:text name='info.warehouseStockInfoDetail' /> --%>
								<s:property value="%{getLocaleProperty('info.description')}"/>
							</h2>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="Description" /></p>
						<p class="flexItem"><s:property value="ForeCastAdvisoryDetails.forecastAdvisory.description" /></p>
					
					</div>
					</div>
					
						<div class="yui-skin-sam">
	<sec:authorize
		ifAllGranted="admin.forecastAdvisor.update">
		<span id="update" class=""><span class="first-child">
		<button type="button" class="edit-btn btn btn-success"><FONT color="#FFFFFF">
		<b><s:text name="edit.button" /></b> </font></button>
		</span></span>
	</sec:authorize> 
	
	<span id="cancel" class=""><span class="first-child">
	<button type="button" class="back-btn btn btn-sts"><b><FONT
		color="#FFFFFF"><s:text name="back.button" /> </font></b></button>
	</span></span></div>			
					</div>
					</div>
					</div>
					</div>
					</s:form>
<s:form name="updateform" action="forecastAdvisor_update.action">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>

<s:form name="cancelform" action="forecastAdvisor_list.action">
	<s:hidden key="currentPage" />
</s:form>
<script type="text/javascript">
    $(document).ready(function () {
        $('#update').on('click', function (e) {
            document.updateform.id.value = document.getElementById('foreCastAdvisoryDetailsId').value;
            document.updateform.currentPage.value = document.form.currentPage.value;
            document.updateform.submit();
        });

        $('#delete').on('click', function (e) {
            if (confirm('<s:text name="confirm.delete"/> ')) {
                document.deleteform.id.value = document.getElementById('foreCastAdvisoryDetailsId').value;
                document.deleteform.currentPage.value = document.form.currentPage.value;
                document.deleteform.submit();
            }
        });
        
    });

</script>
</body>
