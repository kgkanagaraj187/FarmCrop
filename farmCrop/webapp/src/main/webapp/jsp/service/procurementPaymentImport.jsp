<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<script>
	function uploadFile() {
		$("#ImportDiv").empty();
		$("#ImportDivActionMessage").empty();
	}

	function processProcurement(){
		  
		  
		  $("#ImportDiv").html("");
			$("#ImportDivActionMessage").html("");

			var filename = document.getElementById('uploadFile').value;
			var fileExt = filename.split('.').pop();
			var file = document.getElementById('uploadFile').files[0];
			if (file != undefined) {

				if (fileExt == 'xls' || fileExt == 'XLS' || fileExt == 'csv' || fileExt == 'CSV') {
					if (document.getElementById('uploadFile').value.length > 0) {
						/* $("#importForm").submit(); */
						  $('#importForm').attr('action', 'procurementPaymentImport_processProcurement');
					 $("#importForm").submit();
						 
							
					} else {
						alert('<s:text name="empty.fileSecletion"/>');
					}

				} else {
					alert('<s:text name="invalid.file.extension"/>');
				}

			} else {

				alert('<s:text name="empty.fileSecletion"/>');
			}
			//  document.getElementById("enableModal").click();
}
	
	function disablePopupAlert(){
		/* $('#pendingRestartAlertErrMsg').html('');
		$('#popupBackground').hide();
		$('#restartAlert').hide();
		$('body').css('overflow',''); */
		document.redirectform.action="procurementPaymentImport_fileUpload.action";
		document.redirectform.submit();
	}
</script>
<body>
	<s:url id="fileDownload" action="procurementPaymentImport_populateDownloadXLS"></s:url>
	<h4 style="margin-left: 900px;">
		<s:text name="downloadSampleFile" />
		-
		<s:a href="%{fileDownload}">
			<s:text name="downloadSampleFileLinkName" />
		</s:a>
	</h4>
	<!-- Import Div -->
	<div id="ImportDiv" style="color: red;" align="center">
		<s:fielderror />
		<s:actionerror />
		<br />
	</div>
	<div id="ImportDivActionMessage"
		style="color: green; font-weight: bold;" align="center">
		<s:actionmessage />
	</div>

	<!-- Import type contents -->
	<form id="importForm" action="procurementPaymentImport_populateXLS" method="post"
		enctype="multipart/form-data">

		<table class="table table-bordered aspect-detail fillform"
			align="center" border="1" cellpadding="1" cellspacing="1">

			<tr>
				<th><s:property value='%{getLocaleProperty(importTableHeading)}' /></th>
			</tr>
			<tr align="center">
				<td>
					<div style="float: left; width: 100%">
						<div style="float: left; width: 65%; margin-left: 10px;">
							<input type="file" name="uploadFile" id="uploadFile" />
						</div>
						<input type="button" class="btn btn-info" onclick="processProcurement();" value="Import Procurement Payment">
					</div>
				</td>
			</tr>
		</table>
		
	</form>
	<button type="button" data-toggle="modal" data-target="#myModal"
	style="display: none" id="enableModal" class="modal hide"
	data-backdrop="static" data-keyboard="false">Open Modal</button>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-sm">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					onclick="disablePopupAlert()">&times;</button>
				<h4 class="modal-title">
				<s:property value="%{getLocaleProperty('info.procurementPayment')}" />
				</h4>
			</div>
			<div class="modal-body">
				<b><p style="text-align: center; font-size: 120%;">
								<s:property value="%{getLocaleProperty('importSuccessfully')}" />
							</p></b>
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
	
</body>

