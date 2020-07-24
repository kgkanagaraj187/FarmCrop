<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		var dailyReport = document.getElementById('dailyReportFormat').value;
		var dailyReportFormattedString = dailyReport.replace(/,/g, '<br/>');
		document.getElementById("dailyReportval").innerHTML = "<br>"+dailyReportFormattedString;
	
		var consolidatedReport = document.getElementById('consolidatedReportFormat').value;
		var consolidatedReportFormattedString = consolidatedReport.replace(/,/g, '<br/>');
		document.getElementById("consolidatedReportval").innerHTML = "<br>"+consolidatedReportFormattedString;
	});     
</script>
<s:hidden key="reportMailConfiguration.id" id="id"/>
<font color="red">
    <s:actionerror/>
</font>
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage"/>
	<s:hidden key="id" />
	<s:hidden key="reportMailConfiguration.id" id="id"/>
	<s:hidden key="command" />
	<s:hidden name="dailyReportDetail" id="dailyReportFormat"/>
	<s:hidden name="consolidatedReportDetail" id="consolidatedReportFormat"/>
	<div class="flex-view-layout">
	<div class="fullwidth">
		<div class="flexWrapper">
			<div class="flexLeft appContentWrapper">
				<div class="formContainerWrapper dynamic-form-con">
					<h2><s:text name="info.reportMailConfigurationDetails" /></h2>
					 
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="reportMailConfiguration.name" /></p>
						<p class="flexItem"><s:property value="reportMailConfiguration.name"/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="reportMailConfiguration.mailId" /></p>
						<p class="flexItem"><s:property value="reportMailConfiguration.mailId"/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="reportMailConfiguration.type" /></p>
						<p class="flexItem"><s:text name='%{"status"+reportMailConfiguration.status}'/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="reportMailConfiguration.dailyReport" /></p>
						<p class="flexItem"><span style="width:530px;" class="break-word" id="dailyReportval">
				</span></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="reportMailConfiguration.consolidatedReport" /></p>
						<p class="flexItem"><span style="width:530px;" class="break-word" id="consolidatedReportval">
				</span></p>
					</div>
					
				</div>
				<div class="yui-skin-sam">
				   <sec:authorize ifAllGranted="service.reportMailConfig.update">
				        <span id="update" class=""><span class="first-child">
				                <button type="button" class="edit-btn btn btn-success">
				                    <FONT color="#FFFFFF">
				                    <b><s:text name="edit.button" /></b>
				                    </font>
				                </button>
				            </span></span>
				    </sec:authorize>
				    <sec:authorize ifAllGranted="service.reportMailConfig.delete">
				             <span id="delete" class=""><span class="first-child">
				                <button type="button" class="delete-btn btn btn-warning">
				                    <FONT color="#FFFFFF">
				                    <b><s:text name="delete.button" /></b>
				                    </font>
				                </button>
				            </span></span></sec:authorize>
				   <span id="cancel" class=""><span class="first-child"><button type="button" class="back-btn btn btn-sts" >
				               <b><FONT color="#FFFFFF"><s:text name="back.button"/> 
				                </font></b></button></span></span>
				</div>
		</div>
	</div>
</div>
</div>
				
</s:form>

<s:form name="cancelform" action="reportMailConfiguration_list.action">
    <s:hidden key="currentPage"/>
</s:form>
<s:form name="deleteform" action="reportMailConfiguration_delete.action">
    <s:hidden key="currentPage"/>
    <s:hidden key="id"/>
</s:form>
<s:form name="updateform" action="reportMailConfiguration_update.action">
    <s:hidden key="currentPage"/>
    <s:hidden key="id"/>
</s:form>

<script type="text/javascript">
    $(document).ready(function () {
        $('#update').on('click', function (e) {
            document.updateform.id.value = document.getElementById('id').value;
            document.updateform.currentPage.value = document.form.currentPage.value;
            document.updateform.submit();
        });

        $('#delete').on('click', function (e) {
            if (confirm('<s:text name="confirm.delete"/> ')) {
                document.deleteform.id.value = document.getElementById('id').value;
                document.deleteform.currentPage.value = document.form.currentPage.value;
                document.deleteform.submit();
            }
        });
    });

</script>