<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<html>
<head>
<META name="decorator" content="swithlayout">
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>



<style>

div.dataTables_wrapper {
        width: 100%;
        margin: 0 auto;
    }

.table>caption+thead>tr:first-child>th, .table>colgroup+thead>tr:first-child>th, .table>thead:first-child>tr:first-child>th, .table>caption+thead>tr:first-child>td, .table>colgroup+thead>tr:first-child>td, .table>thead:first-child>tr:first-child>td {
     background: #2a3f54;
    color: #fff;
}

.dataTableTheme > thead > tr > th {
     background: #2a3f54;
    color: #fff;
}

.dataTableTheme > tbody > tr:nth-child(odd), .table > tfoot > tr:nth-child(odd), .table > thead > tr:nth-child(odd) {
    background-color: rgba(168,227,214, 0.2);
}

button.dt-button.buttons-excel.buttons-html5 {
   /*  background: yellow; */
   background: #d9534f;
    border-color: #005bbf;
    color: #ffffff;
}

button.dt-button.buttons-pdf.buttons-html5 {
   /*  background: yellow; */
   background: #398439;
    border-color: #005bbf;
    color: #ffffff;
}


.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
   
}

/* Modal Content */
.modal-content {
    background-color: #fefefe;
    margin: auto;
    padding: 20px;
    border: 1px solid #888;
    width: 30%;
}

.modal-content_loading_msg{
 background-color: FFF;
    margin-left: 43%;
    /* padding: 10px;
    border: 1px solid #888; */
    align:center;
    margin-top:15%;
    color : white;
    
}

/* The Close Button */
.close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}




.filterDivStyle > #filterTable_savedReports_outPut > thead > tr >td{
     background : #dcf5d6;
}


#insideFilterDiv > #tableId > thead > tr >td{
     background : #dcf5d6;
}

#insideFilterDiv > #tableId > tbody > tr >td{
     background : #dcf5d6;
}

.addBtnColor{
 background : #04b790;
}

.executeBtnColor{
 background : #ef9c06f7;
}

.updateBtnColor{
 background : #d8110a;
}

</style>


<script src="js/dynamicReportRelated/newDynamicReport.js?vk=12.2"></script>

<!--  data table -->

<script src="js/dynamicReportRelated/dataTablePlugin/jquery.dataTables.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/dataTables.buttons.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/buttons.flash.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/pdfmake.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/jszip.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/vfs_fonts.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/buttons.html5.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/buttons.print.min.js"></script>


<script src="js/dynamicReportRelated/bootstrapDatePicker/bootstrap-datepicker.js"></script>

 <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.css" rel="stylesheet" type="text/css" />
 <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css" /> 
 <link rel="stylesheet" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css" > 
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.5.1/css/buttons.dataTables.min.css" >


<body>
	
	
	


	
	
		<script type="text/javascript">
		
		$(document).ready(function() {
			
			
			populateAvailableColumns();
			var id = 'optionTransfer_select';
			$("#"+id).find("input[type='button'][value='<s:text name="RemoveAll"/>']").addClass("btn btn-warning");
			$("#"+id).find("input[type='button'][value='<s:text name="Add"/>']").addClass("btn btn-small btn-success fa fa-step-forward");
			$("#"+id).find("input[type='button'][value='<s:text name="Remove"/>']").addClass("btn btn-danger");
			$("#"+id).find("input[type='button'][value='<s:text name="AddAll"/>']").addClass("btn btn-sts");
			$("#"+id).find("input[type='button'][value='<s:text name="v"/>']").addClass("btn btn-warning");
			$("#"+id).find("input[type='button'][value='<s:text name="^"/>']").addClass("btn btn-success");
			
			$('.select2').select2();
			$("#filterDiv").hide();
			populateSavedReports();
		});
		
		
		
	</script>





	<div  class="appContentWrapper marginBottom ">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="pill" href="#tabs-1"><s:property
						value="%{getLocaleProperty('title.dynamicReportCreate')}" /></a></li>

			<li><a id="pillTab2" data-toggle="pill" href="#tabs-2"><s:property value="%{getLocaleProperty('title.savedDynamicReport')}" /></a></li>
		</ul>
	</div>

	<div class="tab-content">
		<div id="tabs-1" class="tab-pane fade in active">
			
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

<%-- <span class="makeBold"><s:property value='getLocaleProperty("dynamicReport.headerFields")' /></span><br/><br/> --%>

		<div class="panel-collapse collapse in">
			<div class="flexform">

				<div id="optionTransfer_select">

					<s:text name="RemoveAll" var="rmvall" />
					<s:text name="Remove" var="rmv" />
					<s:text name="Add" var="add" />
					<s:text name="AddAll" var="addall" />
					<s:optiontransferselect id="optionTransfer_select_availableColumns"
						cssClass="form-control "
						cssStyle="width:500px;height:450px;overflow-x:auto;"
						doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
						doubleCssClass="form-control" buttonCssClass="optTrasel"
						allowSelectAll="false"
						buttonCssStyle="font-weight:bold!important;"
						allowUpDownOnLeft="true" labelposition="top"
						allowUpDownOnRight="true" name="" list="{}" 
						leftTitle="<b>Available Fields<b>"
						rightTitle="<b>Selected Fields<b>" headerKey="headerKey"
						doubleName="" doubleId="optionTransfer_select_selectedColumns"
						doubleList="{}" doubleHeaderKey="doubleHeaderKey"
						addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
						addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />

				</div>

			</div>
		</div>
		<br> <br>
		
		<%-- <span class="makeBold"><s:property value='getLocaleProperty("dynamicReport.filterFields")' /></span><br/><br/>
		
		<div class="panel-collapse collapse in">
			<div class="flexform">

				<div id="optionTransfer_where">

					<s:text name="RemoveAll" var="rmvall" />
					<s:text name="Remove" var="rmv" />
					<s:text name="Add" var="add" />
					<s:text name="AddAll" var="addall" />
					<s:optiontransferselect id="optionTransfer_where_availableColumns"
						cssClass="form-control "
						cssStyle="width:500px;height:450px;overflow-x:auto;"
						doubleCssStyle="width:500px;height:450px;overflow-x:auto;"
						doubleCssClass="form-control" buttonCssClass="optTrasel"
						allowSelectAll="false"
						buttonCssStyle="font-weight:bold!important;"
						allowUpDownOnLeft="true" labelposition="top"
						allowUpDownOnRight="true" name="" list="{}"
						leftTitle="<b>Available Fields<b>"
						rightTitle="<b>Selected Fields<b>" headerKey="headerKey"
						doubleName="" doubleId="optionTransfer_where_selectedColumns"
						doubleList="{}" doubleHeaderKey="doubleHeaderKey" 	doubleOnchange="validateFilterFields()"  onchange="validateFilterFields()"
						addAllToLeftOnclick="validateFilterFields();" addAllToRightOnclick="validateFilterFields();"
						addToRightOnclick ="validateFilterFields()" 	addToLeftOnclick ="validateFilterFields()"
						addAllToLeftLabel="%{rmvall}" addAllToRightLabel="%{addall}"
						addToLeftLabel="%{rmv}" addToRightLabel="%{add}" />

				</div>	

			</div>
		</div> --%>
	</div>


<div id="filterDiv" class="appContentWrapper marginBottom">
	<div class='formContainerWrapper'>
		<h2>Filters</h2>
	</div>
	
	<section class="reportWrap row">
		<div class="gridly">
				
		</div>
	</section>
	
</div>
<div id="tableDiv" ></div>



<br/><br/>


<div id="jQueryGridTable"></div>



	<div class="form-element" id="buttonsDiv">
		<%-- <button type="button" class="btn btn-warning" id="enableFilterFields_btn" onclick="enableFilterFields()">
			<i class="fa fa-pencil"></i>
			<s:property value="%{getLocaleProperty('dynamicReport.EnableFilters')}" />
		</button> --%>
		<button type="button" class="btn bg-primary text-white" onclick="generateQuery('0')" >
			<i class="fa fa-plus"></i>
			<s:property value="%{getLocaleProperty('dynamicReport.Execute')}" />
		</button>
		<%-- <button type="button" class="btn btn-danger btn-sm" onclick="refresh()">
			<s:property value="%{getLocaleProperty('dynamicReport.Refresh')}" />
		</button> --%>
		<button type="button" class="save-btn btn btn-success" onclick="populateModalForSave();">
			<i class="glyphicon glyphicon-save" aria-hidden="true"></i>
			<s:property value="%{getLocaleProperty('dynamicReport.Save')}" />
		</button>
		<%-- <button type="button" class="btn btn-sts btn-sm" onclick="clearAllOfTheChanges()">
			<i class="fa fa-pencil"></i>
			<s:property value="%{getLocaleProperty('dynamicReport.clear')}" />
		</button> --%>
	</div>

	<div id="myModal" class="modal">

		<!-- Modal content -->
		<div class="modal-content">
			<span class="close">&times;</span>

			<table>
				<tr>
					<td><h4>
							Title
							<h4></td>
					<td><input type="text" id="title" /></td>
				</tr>

				<tr>
					<td><h4>
							Description
							<h4></td>
					<td><input type="text" id="des" /></td>
				</tr>

				<tr>
					<td>
						<button type="button" class="save-btn btn btn-success"
							onclick="saveReport();">
							<i class="glyphicon glyphicon-save" aria-hidden="true"></i>
							<s:property value="%{getLocaleProperty('dynamicReport.Save')}" />
						</button>

					</td>

				</tr>

			</table>


		</div>

	</div>
	
	
	
		
		</div>

		<div id="tabs-2" class="tab-pane fade">
			<div id="savedReports"></div>
			<div id="savedReports_outPut" ></div>
		</div>

	</div>

	<!--  -->
	<div id="loading_msg" class="modal">

		<!-- Modal content -->
		<div class="modal-content_loading_msg">
			<h3>Processing ...</h3>
		</div>

	</div>


	<!--  -->
</body>
</html>