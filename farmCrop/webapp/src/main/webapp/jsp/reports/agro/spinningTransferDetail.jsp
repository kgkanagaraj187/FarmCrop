<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<font color="red"> <s:actionerror />
</font>
<script type="text/javascript">
var editType= '<%=request.getParameter("edit")%>';
jQuery(document).ready(function ( ) {
	if(editType=='1')
		$('#save').removeClass("hide");
	else
		$('#save').addClass("hide");
	$("#cancelBut").click(function() {
		if(editType=='1'){
		document.cancelform.action="spinningReport_list?edit=1";
		document.cancelform.submit();
		}
	else{
		document.cancelform.action="spinningReport_list?edit=0";
		document.cancelform.submit();
	}
	});

});
function downloadFile(id){
	document.getElementById("loadId").value=id;
	$('#audioFileDownload').submit();
}
function onEdit(){
	if(editType=='1'){
	document.detailForm.action="spinningReport_detailUpdate?edit=1";
	document.detailForm.submit();
	}
else{
	document.detailForm.action="spinningReport_detailUpdate?edit=0";
	document.detailForm.submit();
}
}
</script>
<s:form id="audioFileDownload" action="spinningReport_populateDownload">
	<s:hidden id="loadId" name="id" />
</s:form>
<s:form name="detailForm" cssClass="fillform"
	enctype='multipart/form-data'>
	<s:hidden key="currentPage" />
	<s:hidden key="id" id="spinningId" name="id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:property value="%{getLocaleProperty('info.spinning')}" />
						</h2>
						<s:if test='branchId==null'>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="app.branch" />
								</p>
								<p class="flexItem">
									<s:property value="%{getBranchName(baleGeneration.branchId)}" />
								</p>
							</div>
						</s:if>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="ginning" /></p>
						<p class="flexItem"><s:property value="spg.ginning.name"/></p>
					</div>
						<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="spinning" /></p>
						<p class="flexItem"><s:property value="spg.spinning.name"/></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="txnDate" /></p>
						<p class="flexItem"><s:property value="txnDate" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="invoiceNo" /></p>
						<p class="flexItem"><s:property value="spg.invoiceNo" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="truckNo" /></p>
						<p class="flexItem"><s:property value="spg.truckNo" /></p>
					</div>  
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="type" /></p>
						<p class="flexItem"><s:property value="spg.typeName" /></p>
					</div>
					<div class="dynamic-flexItem">
						<p class="flexItem"><s:text name="heapName" /></p>
						<p class="flexItem"><s:property value="heapName" /></p>
					</div>
					</div>
					<div>
						 <div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:property value="%{getLocaleProperty('info.baleGeneration')}" />
						</h2>
							<table class="table table-bordered aspect-detail mainTable">
								<tr class="odd">
									<th><s:text name="serialNo" /></th>
									<th><s:text name="heapName" /></th>
									<th><s:text name="lotNo" /></th>
									<th><s:text name="prNo" /></th>
									<th><s:text name="baleWeight" />(<s:property
											value="%{getLocaleProperty('kg')}" />)</th>
								</tr>
								<s:iterator var="baleData" status="headerStatus"
									value="baleGeneration">
									<tr>
										<td><s:property value="%{#headerStatus.index+1}" /></td>
										<td><s:property value="#baleData.heapName" /></td>
										<td><s:property value="#baleData.lotNo" /></td>
										<td><s:property value="#baleData.prNo" /></td>
										<td><s:property value="#baleData.baleWeight" /></td>

									</tr>
								</s:iterator>
							</table>
						</div>
						
					</div> 

					<div>
						<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:property value="%{getLocaleProperty('info.gmoFile')}" />
						</h2>

								<table id="gmo" class="table table-bordered aspect-detail mainTable" width="600px">
								
								<tr class="odd">
									<th colspan="2"> <s:property
													value="%{getLocaleProperty('gmoFile')}" /></th>
									
								</tr>
									
									<s:iterator value="imgSet" status="stat" var="img">
										<tr id="tr<s:property value="#stat.index"/>">
											<td><s:property value="#img.fileName" />
											</td><td>
												<button type="button"
													onclick="downloadFile(<s:property value="#img.id"/>)"
													class="btn btn-sts" data-keyboard="false">
													<i class="fa fa-download" aria-hidden="true"></i>
												</button> <%-- 	</s:if> --%></td>

										</tr>

									</s:iterator>

									<%-- 	<tr>
								<td><label for="txt"> <s:property
											value="%{getLocaleProperty('gmo')}" /> <font color="red"><span
											style="font-size: 8px"><br> <s:text
													name="spinning.gmo" /> </span></font>
								</label></td>
								<td><div class="form-element">
										<s:file name="gmoFile" id="gmoFile" cssClass="form-control" />
									</div>
									<button type="button" id="addGmoDetail" class="btn btn-sts"
										data-keyboard="false">
										<i class="fa fa-plus" aria-hidden="true"></i>
									</button></td>

							</tr> --%>
								</table>
							</div>
					
				</div>
				</div>
			</div>
			

			
					
			
			<br />
			<div class="yui-skin-sam " style="display: block">
				<span id="save" class=""><span class="first-child">
						<button id="saveBtn" type="button"
							onclick="onEdit()"
							class="edit-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="edit.button" /></b>
							</font>
						</button>
				</span></span> 
			
				
				
				<span><span
					class="first-child"><button type="button"
							class="back-btn btn btn-sts" id="cancelBut">
							<b><FONT color="#FFFFFF"><s:text name="back.button" />
							</font></b>
						</button></span></span>
			</div>
		</div>
	</div>
	</div>
	</div>
</s:form>



 <s:form name="cancelform">
	<s:hidden key="currentPage" />
</s:form> 

