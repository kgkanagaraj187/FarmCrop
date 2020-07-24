<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script>
var editType= '<%=request.getParameter("edit")%>';
jQuery(document).ready(function ( ) {
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
	function submitBaleMove() {
		var sBales = [];
		var balIds = '';
		
		var id = '<s:property value="id" />';
		$('.baleMove:checked').each(function() {
			if (!isEmpty(this.id))
				balIds = balIds + this.id.split("_")[1] + ",";
		});
		$("#selectedBales").val(balIds);
			document.updateForm.submit();	
	}


	function addGmoDetail() {

		var rowCount = $('#gmo tr').length;
		$("#gmo").append($('<tr>').append($('<td>').append('<s:file id="file'+ (rowCount - 1)+'" onchange="setFileName('+ (rowCount - 1)+')" name="imgSet['
																+ (rowCount - 1)
																+ '].imageFile" 	cssClass="form-control" />	<s:hidden id="fileName'+ (rowCount - 1)+'" name="imgSet['+ (rowCount - 1)+'].fileName"/>'
																+ '<s:hidden id="fileType'+ (rowCount - 1)+'" name="imgSet['+ (rowCount - 1)+'].fileType"/>')));

	}
	
	function setFileName(idd){
		var file = document.getElementById('file'+idd).files[0];
		var filename =  document.getElementById('file'+idd).value;
		filename=filename.replace(/\s/g,'');
		filename = filename.substring(filename.lastIndexOf("\\") + 1);
		//filename = filename.split('\\').pop();
		var fileExt = filename.split('.').pop();
		$('#fileName'+idd).val(filename);
		$('#fileType'+idd).val(fileExt);
	
	}
	function validateImage() {
		var file = document.getElementById('gmoFile').files[0];
		var filename = document.getElementById('gmoFile').value;
		var fileExt = filename.split('.').pop();
		hit = true;
		if (file != undefined) {
			if (fileExt == 'jpg' || fileExt == 'jpeg' || fileExt == 'png'
					|| fileExt == 'JPG' || fileExt == 'JPEG'
					|| fileExt == 'PNG' || fileExt == 'xlsx'
					|| fileExt == 'xls' || fileExt == 'docx'
					|| fileExt == 'doc') {
				hit = true;
			} else {
				hit = false;
			}
		}
		return hit;
	}
	
	function removeTr(id){
		$(id).closest('tr').remove();
	}


	//$("input[name='credentialName']").attr("checked", false);
</script>

<font color="red"> <s:actionerror />
</font>
<s:form name="updateForm" cssClass="fillform"
	enctype='multipart/form-data' action="spinningReport_update?edit=1">
	<s:hidden key="currentPage" />
	<%-- 	<s:hidden key="id" id="id" name="id" /> --%>
	<s:hidden key="id" id="spinningId" name="id" />
	<s:hidden key="selectedBales" id="selectedBales" name="selectedBales" />
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
									<th><s:text name="moveBale" /></th>
								</tr>
								<s:iterator var="baleData" status="headerStatus"
									value="baleGeneration">
									<tr>
										<td><s:property value="%{#headerStatus.index+1}" /></td>
										<td><s:property value="#baleData.heapName" /></td>
										<td><s:property value="#baleData.lotNo" /></td>
										<td><s:property value="#baleData.prNo" /></td>
										<td><s:property value="#baleData.baleWeight" /></td>
										<s:if test='#baleData.status==0'>
											<td><s:checkbox id="moveBale_%{#baleData.id}"
													name="moveBale_%{#baleData.id}" class="baleMove"
													value="false" /></td>
										</s:if>
										<s:else>
											<td><s:checkbox id="moveBale_%{#baleData.id}"
													name="moveBale_%{#baleData.id}" class="baleMove"
													value="true" /></td>
										</s:else>
									</tr>
								</s:iterator>
							</table>
						</div>

					</div>

					<div>
						<div class="appContentWrapper marginBottom">
							<table id="gmo"
								class="table table-bordered aspect-detail mainTable"
								width="600px">

								<tr class="odd">
									<th bgcolor="#a8e3d6"><s:property value="%{getLocaleProperty('gmoFile')}" /></th>
									<th bgcolor="#a8e3d6">
										<button type="button" onclick="addGmoDetail()"
											cssclass="btn btn-sts" data-keyboard="false">
											<i class="fa fa-plus" aria-hidden="true"></i>
										</button>
									</td>
								</tr>

								<s:iterator value="imgSet" status="stat" var="img">
									<tr id="tr%{#stat.index}">

										<s:if test='imageByteString!=null && imageByteString!=""'>
											<td><s:property value="#img.fileName" /> <s:hidden
													name="imgSet[%{#stat.index}].fileName"
													value="%{#img.fileName}" /> <s:hidden
													name="imgSet[%{#stat.index}].fileType"
													value="%{#img.fileType}" /> <s:hidden
													name="imgSet[%{#stat.index}].imageByteString"
													value="%{#img.imageByteString}" /></td>
											<td>
												<%-- <s:hidden  name="imgSet[%{#stat.index}].fileType"value="#fileName" /> --%>
												<button type="button" onclick="removeTr(this)"
													class="btn btn-sts" data-keyboard="false">
													<i class="fa fa-remove" aria-hidden="true"></i>
												</button>
											</td>
										</s:if>

										<s:else>
											<td colspan="2"><div class="form-element">
													<s:file name="imgSet[%{#stat.index}].imageFile"
														id="file%{#stat.index}"
														onchange="setFileName(%{#stat.index})"
														cssClass="form-control" />
													<s:hidden id="fileName%{#stat.index}"
														name="imgSet[%{#stat.index}].fileName" />
													<s:hidden id="fileType%{#stat.index}"
														name="imgSet[%{#stat.index}].fileType" /></td>
										</s:else>


									</tr>

								</s:iterator>
							</table>
						</div>


					</div>
				</div>
			</div>



		</div>
	</div>
	<br />
	<div class="yui-skin-sam " style="display: block">
		<span id="save" class=""><span class="first-child">
				<button id="saveButton" type="button" onclick="submitBaleMove()"
					class="save-btn btn btn-success">
					<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
					</font>
				</button>
		</span></span> <span id="cancel" class=""><span
			class="first-child"><button type="button" id="cancelBut"
					class="back-btn btn btn-sts">
					<b><FONT color="#FFFFFF"><s:text name="back.button" />
					</font></b>
				</button></span></span>
	</div>
	</div>
	</div>
	</div>
	</div>

	<button type="button" id="enableModal"
		class="hide slide_open btn btn-sm btn-success" data-toggle="modal"
		data-target="#slideModal" data-backdrop="static" data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title">
						<s:property value="%{getLocaleProperty('info.spinningBaleMap')}" />
					</h4>
				</div>
				<div class="modal-body">
					<b><p style="text-align: center; font-size: 100%;">
							<s:property value="%{getLocaleProperty('mapSucess')}" />
						</p></b>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default">
						<s:text name="cancel" />
					</button>
				</div>
			</div>

		</div>

	</div>
</s:form>



<s:form name="cancelform">
	<s:hidden key="currentPage" />
</s:form>

