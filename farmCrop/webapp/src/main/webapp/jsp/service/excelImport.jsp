<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<s:url id="fileDownload" action="excelImport_populateDownloadXLS"></s:url>
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
	<form id="importForm" action="excelImport_populateXLS" method="post"
		enctype="multipart/form-data">

		<table class="table table-bordered aspect-detail fillform"
			align="center" border="1" cellpadding="1" cellspacing="1">

			<tr>
				<th><s:text name="importTableHeading2" /></th>
			</tr>
			<tr align="center">
				<td>
					<div style="float: left; width: 100%">
						<div style="float: left; width: 65%; margin-left: 10px;">
							<input type="file" name="uploadFile" id="uploadFile" />
						</div>
						<input type="button" class="btn btn-info" onclick="importFiles();"
							value="Import">
							
						 <input type="button" class="btn btn-info"
							onclick="importFarmFiles();" value="Import Farm"> 
							
						<input type="button" class="btn btn-info" onclick="importFarmCropFiles();"
							value="Import Farm Crop">
						<input type="button" class="btn btn-info" onclick="importCOC();" value="Import COC">
						
						<input type="button" class="btn btn-info" onclick="importFarmEquipment();" value="Import Farm Equipment">
						
						<input type="button" class="btn btn-info" onclick="importFarmerCropHistory();" value="Import Farmer Crop History">
					</div>
				</td>
			</tr>
		</table>
	</form>
	<script>
	function uploadFile() {
		$("#ImportDiv").empty();
		$("#ImportDivActionMessage").empty();
	}

	function importFiles() {
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
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

	}
	
	function importFarmCropFiles() {
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
					/* $("#importForm").submit(); */
					  $('#importForm').attr('action', 'excelImport_populateCropData');
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

	}
	function importFarmFiles() {
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
					/* $("#importForm").submit(); */
					  $('#importForm').attr('action', 'excelImport_populateFarmData');
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

	}
	function importCOC(){
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
					/* $("#importForm").submit(); */
					  $('#importForm').attr('action', 'excelImport_populateCOCData');
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

	}
	function importFarmEquipment(){
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
					/* $("#importForm").submit(); */
					  $('#importForm').attr('action', 'excelImport_populateNewCropCotton');
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
	}
	
	function importFarmerCropHistory(){
		$("#ImportDiv").html("");
		$("#ImportDivActionMessage").html("");

		var filename = document.getElementById('uploadFile').value;
		var fileExt = filename.split('.').pop();
		var file = document.getElementById('uploadFile').files[0];
		if (file != undefined) {

			if (fileExt == 'xls' || fileExt == 'XLS') {
				if (document.getElementById('uploadFile').value.length > 0) {
					/* $("#importForm").submit(); */
					  $('#importForm').attr('action', 'excelImport_populateFarmerCropHistory');
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
	}
</script>
</body>

