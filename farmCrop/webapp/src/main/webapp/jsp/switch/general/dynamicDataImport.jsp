<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<html>
<head>
<META name="decorator" content="swithlayout">
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">



</head>
<script src="js/dynamicDataImport.js?vk=2"></script>
<link rel="stylesheet" href="plugins/select2/select2.min.css">
<script src="plugins/select2/select2.min.js"></script>



<body>

<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

table > thead > tr > th{
background-color: #2a3f54;
color : white;
}

td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
  	
}

tr:nth-child(even) {
    background-color: #dddddd;
}
</style>



<script type="text/javascript">
		
		$(document).ready(function() {
			populateFarmerFields();
			
			
			
		});
		
		
		
</script>





	<div class="appContentWrapper marginBottom ">
		<div class="formContainerWrapper">
			<div class="ferror" id="errorDiv" style="color: #ff0000">
				<s:actionerror />
				<s:fielderror />
			</div>
			<h2>
				<a data-toggle="collapse" data-parent="#accordion"
					href="#farmerInfo" class="accrodianTxt"> <s:property
						value="%{getLocaleProperty('dynamicReport.headerFields')}" />
				</a>
			</h2>
		</div>


		<section class="reportWrap row">
			<div class="gridly">

				<div class="filterEle" style="width: 20%;">
					<label for="txt">Farmer fields</label>
					<div class="form-element">
						<select class="select2" id="farmerFieldsDropDown"></select>
					</div>

				</div>

				<div class="filterEle" style="width: 20%;">
					<label for="txt">Type</label>
					<div class="form-element">
						<select class="select2" id="farmerFieldsTypeDropDown">
							<option value="">Select</option>
							<option value="DropDown">Drop Down</option>
							<option value="CheckBox">Check Box</option>
							<option value="RadioButton">Radio Button</option>
							<option value="TextBox">TextBox</option>
						</select>
					</div>
				</div>
				
				<div class="filterEle" >
					<label for="txt"></label>
					<div class="form-element">
						<button class="btn btn-warning" onclick="populateTable()">Add</button>
					</div>
				</div>
				
			</div>
		</section>
		
		<br><br>
		
		<div id="exportXlsDiv"></div>
		
		<table id="selectedFarmerFields">
			<thead>
				<tr></tr>
			</thead>
			<tbody>
				<tr></tr>
			</tbody>
		</table>
		
		
		<br><br>
		
		
		
		
		
	</div>

</body>
</html>