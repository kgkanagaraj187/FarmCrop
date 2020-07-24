<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<link
	href="plugins/DataTables-1.10.18/DataTables-1.10.18/css/jquery.dataTables.min.css"
	rel="stylesheet" type="text/css" />

<link
	href="plugins/DataTables-1.10.18/Responsive-2.2.2/css/responsive.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="plugins/DataTables-1.10.18/Buttons-1.5.4/css/buttons.dataTables.min.css"
	rel="stylesheet" type="text/css" />
<script
	src="plugins/DataTables-1.10.18/Responsive-2.2.2/js/dataTables.responsive.min.js"></script>

<script
	src="plugins/DataTables-1.10.18/DataTables-1.10.18/js/jquery.dataTables.min.js"></script>
<script
	src="plugins/DataTables-1.10.18/DataTables-1.10.18/js/jquery.multisortable.js"></script>

<script src="js/dynamicReportRelated/dataTablePlugin/sum().js"></script>
<script
	src="plugins/DataTables-1.10.18/Buttons-1.5.4/js/dataTables.buttons.min.js"></script>
<script
	src="plugins/DataTables-1.10.18/Buttons-1.5.4/js/buttons.flash.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/pdfmake.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/jszip.min.js"></script>
<script src="js/dynamicReportRelated/dataTablePlugin/vfs_fonts.js"></script>
<script
	src="plugins/DataTables-1.10.18/Buttons-1.5.4/js/buttons.html5.min.js"></script>
<script
	src="plugins/DataTables-1.10.18/Buttons-1.5.4/js/buttons.print.min.js"></script>

<script src="plugins/bootstrap_Multiselect/bootstrap-multiselect.js"></script>
<script
	src="plugins/DataTables-1.10.18/ColReorder-1.5.0/js/dataTables.colReorder.min.js"></script>

<link href="plugins/bootstrap_Multiselect/bootstrap-multiselect.css"
	rel="stylesheet" />
<link
	href="plugins/DataTables-1.10.18/ColReorder-1.5.0/css/colReorder.dataTables.min.css"
	rel="stylesheet" />
<link rel="stylesheet" href="plugins/select2/select2.min.css">
<script src="plugins/select2/select2.min.js"></script>
<head>
<META name="decorator" content="swithlayout">
</head>
<style>
.multiselect-container { width: 315px!important;height:100%!important; }
.column-left {
	width: 25%;
	height: 100%;
	background: white;
	float: left;
	border: 2px solid grey;
	border-radius: 5px;
}

#savedR {
	width: 89% !important
}

.dashboardPage {
	float: right;
	min-height: 100%;
	width: 75%;
	background: white;
}

.column-leftB {
	width: 100%;
	background: white;
	float: left;
	margin-top: 10px;
}

.dropdown-menu {
	border-radius: 0;
	padding: 0;
	background: white;
	border: none;
}

.column-left i {
	position: absolute;
	top: 12px;
	right: 5px;
	font-size: 12px;
}

.glyphicon {
	position: relative !important;
	top: 1px !important;
	display: inline-block !important;
	font-family: 'Glyphicons Halflings' !important;
	font-style: normal !important;
	font-weight: 400 !important;
	line-height: 1 !important;
	-webkit-font-smoothing: antialiased !important;
	-moz-osx-font-smoothing: grayscale !important;
}

#saved {
	height: 15%;
}

#multi {
	height: 84%;
	overflow-y: auto;
}

.column-right {
	padding: 5px;
	width: 100%;
	min-height: 80%;
	overflow: auto;
	overflow-x: auto;
	/*background:#858585;*/
	float: left;
	position: relative;
}

.flxrD {
	padding: 5px 10px;
}

.flxrD label {
	color: black !important;
}

.h1, .h2, .h3, .h4, .h5, .h6, h1, h2, h3, h4, h5, h6 {
	font-family: inherit;
	font-weight: 300;
	line-height: 0.9;
	color: inherit;
}

.h1, .h2, .h3, h1, h2, h3 {
	margin-top: 10px;
	margin-bottom: 5px;
}

.h2, h2 {
	font-size: 20px;
}

.btn {
	/* //font-family: sans-serif; */
	transition: all 0.3s ease 0s !important;
	border-radius: 2px;
}

.btn {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: 400;
	line-height: 1.42857143;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
}

body {
	font-family: Tahoma, Geneva, sans-serif !important;
}

label {
	color: black;
	font-size: 12px;
}



.input-group-btn {
	position: relative;
	font-size: 0;
	white-space: nowrap;
	background-color: #eee;
}

.table {
	width: 100%;
	border: 2px grey;
	border-radius: 5px;
	margin-bottom: 2px;
}

.filters {
	padding: 0 5px;
	color: #fff;
	background-color: #2a3f54;
}


/* 
.filterTd {
	padding: 4px !important;
	line-height: 1 !important;
	vertical-align: top !important;
	width: 70px !important;
} */
.btnApp {
	display: inline-flex;
	padding: 4px 4px;
	margin-bottom: 0;
	font-size: 11px;
	font-weight: 300;
	line-height: 1.42857143;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
}

select.floated {
	background-color: #fff;
	border: 1px solid #D5D5D5 !important;
	border-radius: 2 2 2 2 !important;
	color: #858585;
	padding: 2px 2px;
	height: auto;
	width: 25% !important;
}

.unselectable {
	background-color: #ddd;
	cursor: not-allowed;
}

#filtRowBt {
	position: relative;
	display: flex;
	flex-flow: row wrap;
	/*  // align-items:flex-start;
  */
	align-content: space-around;
	justify-content: flex-start;
}

.filterTd {
	padding: 2px;
	flex-basis: 30%;
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
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
	background-color: #fefefe;
	margin: auto;
	padding: 20px;
	border: 1px solid #888;
	width: 30%;
}

.modal-content_loading_msg {
	background-color: FFF;
	margin-left: 43%;
	/* padding: 10px;
    border: 1px solid #888; */
	align: center;
	margin-top: 15%;
	color: white;
}

/* The Close Button */
.close {
	color: #aaaaaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
}

.fa-plus:before {
	content: "\f067";
	font-family: 'FontAwesome';
	font-size: 14px;
}

.glyphicon-save:before {
	content: "\e166";
	font-size: 20px;
}

.glyphicon-pencil:before {
	font-size: 20px !important;
}

.glyphicon-play:before {
	content: "\e072";
	font-size: 20px;
}

.glyphicon-floppy-disk:before {
	font-size: 20px !important;
}

.glyphicon-floppy-disk:before {
	font-size: 20px !important;
}

.glyphicon-plus:before {
	font-size: 20px !important;
}

.select2-container .select2-selection--single {
	box-sizing: border-box;
	cursor: pointer;
	display: block;
	height: 30px !important;
	user-select: none;
	-webkit-user-select: none;
}
.accrodianTxt {
	color: black;
	font-style: bold;
}

.accrodianTxt:after {
	/* symbol for "opening" panels */
	font-family: 'Glyphicons Halflings';
	/* essential for enabling glyphicon */
	content: "\2212"; /* adjust as needed, taken from bootstrap.css */
	float: right; /* adjust as needed */
	color: black; /* adjust as needed */
}
.collapsed:after {
	/* symbol for "collapsed" panels */
	content: "\2b"; /* adjust as needed, taken from bootstrap.css */
}
</style>

<script>
	var table;
	var expArray = {};
	$(document).ready(
			function() {
				$('.select2').select2();
				expArray[1] = '=';
				expArray[2] = '<>';
				expArray[3] = '>';
				expArray[4] = '>=';
				expArray[5] = '<';
				expArray[6] = '<=';
				expArray[7] = 'between';
				expArray[8] = '=""';
				expArray[9] = '<>""';
				expArray[10] = "like";
				$('.multiselect-container').css('width',
						$('.column-left').width()).css('height',
						parseInt($('.column-left').height()) / 2);
				var modal = document.getElementById('myModal');
				modal.style.display = "none";
				$('#fields').multiselect({
					includeSelectAllOption : true,
					enableClickableOptGroups : true,
					filterPlaceholder : 'Search Fields',
					maxHeight : $('.column-left').height() / 2,
					enableCaseInsensitiveFiltering: true,
					enableFiltering : true,
					filterBehavior : 'text',
					 buttonWidth: '100%',
					onChange: addOpt,
					enableCollapsibleOptGroups: true,
					collapseOptGroupsByDefault: true,
					enableClickableOptGroups: true,
					buttonContainer: '<div id="example-enableCollapsibleOptGroups-collapsed-container" />'
				});
				
				setTimeout( function(){ 
				    $('#example-enableCollapsibleOptGroups-collapsed-container .caret-container').click();
				}  , 1000 );
				
				$('.bmultBtn').click();
				$('.bmultBtn').hide();
				$('#fields').parent().find('ul')
						.attr('style', 'display:block;');
				// $(function(){ $('ul.multiselect-container').multisortable(); });
				$('ul.multiselect-container').multisortable({
					selectedClass : "selected",
					/* 			click: function(e){ console.log("I'm selected."); }
					 */stop : function(e, info) {
						console.log(info.item);
					}
				});
				jQuery.post("dynamicReport_populateAvailableColumns.action",
						{}, function(availabeColumns) {
							insertOptions("fields", jQuery
									.parseJSON(availabeColumns));
						});
				$('#filt').html('');
				$('#filt').append($('<option>', {
					value : '',
					text : 'Select Fiilter Field'
				}));	
				populateSaved();

			});
	
	function addOpt(){
		$('#filt').html('');
		$('#filt').append($('<option>', {
			value : '',
			text : 'Select Fiilter Field'
		}));
		$('#avgF').html($('#filt').html());
     	$('#countF').html($('#filt').html());
     	$('#sumF').html($('#filt').html());
     	$('#groupByf').html($('#filt').html());
		$(".multiselect-container li.active input").each(function() {
			var spltt = $(this).val().split('~');
			  var ddpVal =  spltt[4] + '~' + spltt[3] + "~"+ spltt[2] + "~" + spltt[5];
	            	$('#filt').append(
							$('<option>', {
								value : ddpVal,
								text : spltt[2]
							}));
					if(spltt[3] =='3' ||spltt[3] =='2'  ){
						$('#sumF').append(
								$('<option>', {
									value : spltt[4],
									text : spltt[2]
								}));
						
						
						
					}
					$('#groupByf').append(
							$('<option>', {
								value : spltt[4],
								text : spltt[2]
							}));
							
								$('#avgF').html($('#sumF').html());
	                         	$('#countF').html($('#groupByf').html());
	           
		});
		
				
	}
	function addOption(selectbox, text, value) {
		$(selectbox).append(
				"<option value=\"" + value + "\">" + text + "</option>");
		$(selectbox).multiselect('rebuild');

	}

	function insertOptions(ctrlName, jsonArr) {
		var myselect = $('#' + ctrlName);
		var data = [];
		for (var i = 0; i < jsonArr.length; i++) {
			var backend_Value = {
				"id" : jsonArr[i].id,
				"field" : jsonArr[i].field,
				"table" : jsonArr[i].table,
				"alias" : jsonArr[i].filter,
				"label" : jsonArr[i].label,
				"isGroupBy" : jsonArr[i].isGroupBy,
				"filter" : jsonArr[i].filter,
				"dataType" : jsonArr[i].dataType
			};

			backend_Value = jsonArr[i].table + "~" + jsonArr[i].field + "~"
					+ jsonArr[i].label + "~" + jsonArr[i].dataType + "~"
					+ jsonArr[i].filter + "~" + jsonArr[i].id;

			var tableN = jsonArr[i].tableName.split(" ").join("");
			if ($('#' + ctrlName + ' optgroup[label=' + tableN + ']').html() == null) {
				$('#' + ctrlName).append(
						'<optgroup label="'+tableN+'"></optgroup>');
				$('#' + ctrlName + ' optgroup[label=' + tableN + ']').append(
						"<option value='"+backend_Value+"'>" + jsonArr[i].label
								+ "</option>");
			} else {
				$('#' + ctrlName + ' optgroup[label=' + tableN + ']').append(
						"<option value='"+backend_Value+"'>" + jsonArr[i].label
								+ "</option>");
			}
		}
		$('#' + ctrlName).multiselect('rebuild');

	}
	function generateQuery(val) {
		var labels = "";
		var fields = '';
		var filFields = '';
		var entity = '';
		var gridColumnNames = new Array();
		var othF = {};
		var sum = $('#sumF').val();
		var count = $('#countF').val();
		var avg = $('#avgF').val();
		
		if(sum!=null && sum!=''){
		$.each(sum.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'SUM('+v+'),';
		});
		}
		if(avg!=null && avg!=''){
		$.each(avg.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'AVG('+v+'),';
		});
		}
		if(count!=null && count!=''){
		$.each(count.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'COUNT('+v+'),';
		});
		}
		$(".multiselect-container li.active input").each(
				function() {
					var spltt = $(this).val().split('~');
					if(spltt!='undefined'&&spltt!='multiselect-all'){
					if (entity.indexOf(spltt[0].trim()) < 0) {
						entity = entity + spltt[0] + ","
					}
					
					if(othF.hasOwnProperty(spltt[4])){
				
							$.each(othF[spltt[4]].split(","), function(k, v) {
								if(v!=null && v!=''){
							var oibj = {};
							oibj['title'] = v.replace(spltt[4],spltt[2]);
							oibj['defaultContent'] = '0';
							oibj['orderable'] = true;
							gridColumnNames.push(oibj);
							console.log("v.trim() "+ v.trim());
							console.log("v.trim().endsWith(",") "+v.trim().endsWith(","));
							fields = fields + spltt[1] +",";
							filFields=filFields + v +",";
							//filFields=filFields.replace("f.","t.");
							labels = labels + v.replace(spltt[4],spltt[2]) + ",";
								}
						});
					}else{
						fields = fields + spltt[1] + ",";
						labels = labels + spltt[2] + ",";
						filFields=filFields + spltt[4] + ",";
						//filFields=filFields.replace("f.","t.");
						var oibj = {};
						oibj['title'] = spltt[2];
						oibj['defaultContent'] = '0';
						oibj['orderable'] = true;
						gridColumnNames.push(oibj);
					}
					}
				});
	
			
		var item_filter_data = {};
		var jsonObj_filter_data = [];
		$(".filter")
				.each(
						function(index) {
							var filteV = $(this).attr('id').split('|');
							if ($('#filt option[value^="' + filteV[0].trim()
									+ '"]').length > 0) {
								var uniqClasss = $(this).closest("div")
										.removeClass("unselectable").attr(
												'class').split(' ').pop();
								var item_filter_data = {};
								item_filter_data["field"] = filteV[0].trim();
								item_filter_data["expression"] = filteV[1]
										.trim();
								if (filteV[2] != undefined) {
									if (filteV[2].trim().indexOf('~') > 0) {
										item_filter_data["value"] = filteV[2]
												.trim().split('~')[0]
												+ " and "
												+ filteV[2].trim().split('~')[1];
									} else {
										item_filter_data["value"] = filteV[2]
												.trim();
									}
								} else {
									item_filter_data["value"] = '';
								}
								item_filter_data["andOr"] = $(
										'.ar' + uniqClasss).val();
								item_filter_data["fieldDataType"] = filteV[3]
										.trim();
								jsonObj_filter_data.push(item_filter_data);
							} else {
								$(this).closest("div").remove();
							}
						});
		entity = entity.substring(0, entity.length - 1);
		fields = fields.substring(0, fields.length - 1);
		labels = labels.substring(0, labels.length - 1);
		filFields = filFields.substring(0, filFields.length - 1);
		if ($.fn.dataTable.isDataTable('#grid')) {
			table.destroy();
			$('#grid').empty(); // empty in case the columns change
		}
		var groupByField = $('#groupByf').val();
		if(groupByField==null){
			groupByField='';
		}
		
		$.ajax({
			 type: "POST",
	         async: false,
	         url: "dynamicReport_validateJoins.action",
	         data: {entity: entity},
	         success: function(result) {
	        	 var jsonDeviceData = jQuery.parseJSON(result);
	        	 if(jsonDeviceData.stat==0){
	        		 loadGrid("dynamicReport_executeQueryAndgetValuesDT.action",entity,fields,labels,groupByField,JSON.stringify(jsonObj_filter_data),gridColumnNames,filFields);
	        	 }else{
	        		alert("Invalid Joins")
	        	 }
	         }
		});
		

	}

	
	function loadGrid(url,entity,fields,labels,groupByField,filter_data,gridColumnNames,filFields) {
		table = $('#grid')
				.DataTable(
						{
							// data: dataSet,
							"ajax" : {
								"url" : url,
								"data": {entity: entity,selectedFields:fields,selectedlabels:labels,groupByField:groupByField.toString(),filter_data:filter_data,filFields:filFields},
								"type" : "POST"
							},
							"searching" : true,

							language : {
								sLoadingRecords : '<span style="width:100%;"><img src="img/ajax-loader.gif"></span>'
							},
							columns : gridColumnNames,
							bAutoWidth : false,
							//responsive : true,
							colReorder : true,
							scrollX : true,
							//stateSave : true,
							dom : 'Bfrtip',
							buttons : [
									{
										extend : 'excel',
										filename : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										title : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										footer : true,
										exportOptions : {
											columns : "thead th:not(.details-control)"
										}

									},
									{
										extend : 'pdf',
										filename : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										title : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										exportOptions : {
											columns : "thead th:not(.details-control)"
										}

									},
									{
										extend : 'csv',
										filename : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										title : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										exportOptions : {
											columns : "thead th:not(.details-control)"
										}

									},
									{
										extend : 'print',
										filename : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										title : $('.breadCrumbNavigation li')
												.last().text().replace(/\s/g,
														''),
										exportOptions : {
											columns : "thead th:not(.details-control)"
										}

									} ]
						});

		$("#grid").addClass("dataTableTheme");
	}
	function populateExp(obj) {
		var field = obj.split("~");
		var dt = field[1];
		var conditions = [];
		$('#exp').empty();
		if (dt == "3" || dt == "2") {
			conditions = [ '1|Equal to', '2|Not Equal to', '3|Greater than',
					'4|Greater than or equal to', '5|Less than',
					'6|Less than or equal to', '7|Between' ];
		} else if (dt == "4") {
			conditions = [ '1|Equal to', '2|Not Equal to', '3|Greater than',
					'4|Greater than or equal to', '5|Less than',
					'6|Less than or equal to', '7|Between', '8|Is empty',
					'9|Is not empty' ];
		} else {
			conditions = [ '1|Equal to', '2|Not Equal to', '3|Greater than',
					'4|Greater than or equal to', '5|Less than', '10|contains',
					'6|Less than or equal to', '8|Is empty', '9|Is not empty' ];
		}

		createDropDown_FilterExpression(conditions, 'exp');

	}

	function createDropDown_FilterExpression(arr, obj) {

		var select = $('#' + obj);

		var option = "<option value=''>Select </option>";
		$(select).append(option);

		for (var i = 0; i < arr.length; i++) {

			var option = "<option value='" + arr[i].split("|")[0] + "'>"
					+ arr[i].split("|")[1] + "</option>";
			$(select).append(option);

		}

	}

	function enableDateRangePicker(expression, row_DOM) {

		var first_td = $('#' + row_DOM).find('td')[0];
		var field = $('#filt').val();

		var fieldName = field.split("~");

		if (fieldName[1] == "4") {
			if (expression == "7") {

				var id = $('#' + row_DOM).prop('id');
				var row = document.getElementById(id);
				row.deleteCell(2);

				var textBox1 = $('<input/>').attr({
					type : 'text',
					class : 'datePickerTextBox val1'

				});

				var textBox2 = $('<input/>').attr({
					type : 'text',
					class : 'datePickerTextBox val2'
				});

				newCell = row.insertCell(2);
				var htmlStr = $(textBox1).prop('outerHTML') + "&nbsp to &nbsp"
						+ $(textBox2).prop('outerHTML');
				newCell.innerHTML = htmlStr;

				$('.datePickerTextBox').datepicker({
					autoclose : true,
					todayHighlight : true,
					//format : 'yyyy-mm-dd',
					dateFormat : 'yy-mm-dd',
				}).datepicker('update', new Date());

			} else if (expression == '9' || expression == '8') {

				var id = $('#' + row_DOM).prop('id');
				var row = document.getElementById(id);
				$('#' + id + ' td:eq(2)').html('');

			} else {

				var id = $('#' + row_DOM).prop('id');
				var row = document.getElementById(id);
				row.deleteCell(2);

				var textBox1 = $('<input/>').attr({
					type : 'text',
					class : 'datePickerTextBox val1'

				});

				newCell = row.insertCell(2);
				var htmlStr = $(textBox1).prop('outerHTML');
				newCell.innerHTML = htmlStr;

				$('.datePickerTextBox').datepicker({
					autoclose : true,
					todayHighlight : true,
					//format : 'yyyy-mm-dd',
					dateFormat : 'yy-mm-dd',
				}).datepicker('update', new Date());

			}
		} else {
			if (expression == '8' || expression == '9') {

				var id = $('#' + row_DOM).prop('id');
				var row = document.getElementById(id);
				$(row).find("td:eq(2)").html('');

			} else {
				if (expression == "7") {

					var id = $('#' + row_DOM).prop('id');
					var row = document.getElementById(id);
					row.deleteCell(2);

					var textBox1 = $('<input/>').attr({
						type : 'text',
						class : 'val1'

					});

					var textBox2 = $('<input/>').attr({
						type : 'text',
						class : 'val2'

					});

					newCell = row.insertCell(2);
					var htmlStr = $(textBox1).prop('outerHTML')
							+ "&nbsp and  &nbsp"
							+ $(textBox2).prop('outerHTML');
					newCell.innerHTML = htmlStr;

				} else {

					var id = $('#' + row_DOM).prop('id');
					var row = document.getElementById(id);
					row.deleteCell(2);

					var textBox1 = $('<input/>').attr({
						type : 'text',
						class : 'val1'
					});

					newCell = row.insertCell(2);
					var htmlStr = $(textBox1).prop('outerHTML');
					newCell.innerHTML = htmlStr;
				}
			}
		}

	}

	function addRow(obb) {
		
		var field = $('#filt').val().split("~");
		var valuee = $('.val1').val();
		var valuee2 = $('.val2').val();
		var exp = $('#exp').val();
		var expT = $("#exp option:selected").text();
		if (field == '') {
			alert(" Please Select Filter Field");
			return false;
		}

		if (exp == '') {
			alert(" Please Select Condition");
			return false;
		}
		if (exp != '8' && exp != '9' && valuee == '') {
			alert(" Please Enter Value");
			return false;
		}
		if (exp == '7' && (valuee2 == '' || valuee2 == undefined)) {
			alert(" Please Enter Both Values");
			return false;
		}
		if (valuee == undefined) {
			valuee = "";
		} else if (valuee2 != undefined && valuee2 != '') {
			valuee = valuee + "~" + valuee2;
		}
		var label = field[2].trim();
		var fl = field[0].trim();

		label = label + " " + expT + " " + valuee;
		var disp = fl + "|" + expArray[exp] + "|"
				+ valuee.split('~').join(" and ") + '|' + field[1].trim();

		if ($('#editFIlt').val() != '') {
			var td = $('#' + $('#editFIlt').val());

			$(td).removeClass("unselectable");
			var add_btn = $(td).find('button[type="button"]');
			$(add_btn).text(label);
			$(add_btn).attr("id", disp);
		} else {
			var add_btn = $('<button/>').attr({
				type : 'button',
				class : 'btnApp btn-sts filter floated ',
				id : disp
			});

			$(add_btn).text(label);
			if ($('.filter').html() != null && $('.filter').html() != '') {
				var div = $("<div/>", {
					css : "width:20px",
					class : "filterTd "
							+ $("#filtRowBt div").last().attr('class').split(
									' ').pop()

				});
				$("#filtRowBt div")
						.last()
						.append(
								'<select class="select2   floated ar'
										+ $("#filtRowBt div").last().attr(
												'class').split(' ').pop()
										+ '" ><option value="AND">AND </option><option value="OR">OR </option></select>');
				//$( "#filtRowBt div" ).last().append(div);
			}
			var span = $("<span />", {
				class : "floated glyphicon glyphicon-trash",
				onclick : "delRow('" + fl.replace(".", "")
						+ $('.filter').length + "',this)",
				style : "cursor:pointer"
			});

			var edi = $("<span />", {
				class : "floated glyphicon glyphicon-edit",
				onclick : "editRow('" + $('#filt').val() + "|" + exp + "|"
						+ valuee + "',this,'t" + fl.replace(".", "")
						+ $('.filter').length + "')",
				style : "cursor:pointer;margin:2px"
			});
			var div = $("<div/>", {
				css : "width:20px",
				class : "filterTd " + fl.replace(".", "") + $('.filter').length

			});
			$(div).attr("id", 't' + fl.replace(".", "") + $('.filter').length);
			$(div).append(add_btn).append(span).append(edi);
			$('#filtRowBt').append(div);

		}
		$('#filt').val('Select Fiilter Field');
		$('.val1').val('');
		$('.val2').val('');
		$('#exp').val('');
		$('#editFIlt').val('');
	}
	function delRow(obj, but) {
		$('.' + obj).remove();
		$(but).remove();

	}

	function editRow(obj, but, td) {
		var filt = obj.split("|")[0];
		var exp = obj.split("|")[1];
		var val = obj.split("|")[2];
		var val1;
		if (val.indexOf("~") > 0) {
			val = val.split("~")[0];
			val1 = val.split("~")[1];
		}

		$('#filt').val(filt).trigger("change");
		$('#exp').val(exp).trigger("change");
		$('.val1').val(val);
		$('#editFIlt').val(td);
		$(but).parent().addClass("unselectable");

	}

	function findExp(val1) {
		var res;
		$.each(expArray, function(k, v) {
			if (val1 ===  v) {
				res =  k;
			}
		});
		return res;
	}

	function toggleDiv() {

		if ($('.column-left').is(":visible")) {
			$('.dashboardPage').css("width", "100%");
			$('.column-left').hide();
		} else {
			$('.dashboardPage').css("width", "75%");
			$('.column-left').show();
		}

	}
function resetALl(){
	$(".multiselect-container li.active input").removeAttr('checked');
	$('#filtRowBt').html('');
	
	if ($.fn.dataTable.isDataTable('#grid')) {
			table.destroy();
			$('#grid').empty(); // empty in case the columns change
		}
}
	function saveQuery(obj) {
		var labels = "";
		var fields = '';
		var filFields = '';
		var entity = '';
		var id = '';
		var gridColumnNames = new Array();
		var othF = {};
		var sum = $('#sumF').val();
		var count = $('#countF').val();
		var avg = $('#avgF').val();
		
		if(sum!=null && sum!=''){
		$.each(sum.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'SUM('+v+'),';
		});
		}
		if(avg!=null && avg!=''){
		$.each(avg.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'AVG('+v+'),';
		});
		}
		if(count!=null && count!=''){
		$.each(count.toString().split(","), function(k, v) {
			var vall = othF[v];
			if(vall == undefined){
				vall='';
			}
			othF[v] = vall+'COUNT('+v+'),';
		});
		}
		$(".multiselect-container li.active input").each(
				function() {
					var spltt = $(this).val().split('~');
					if(spltt!='undefined'&&spltt!='multiselect-all'){
					if (entity.indexOf(spltt[0].trim()) < 0) {
						entity = entity + spltt[0] + ","
					}
					
					if(othF.hasOwnProperty(spltt[4])){
				
							$.each(othF[spltt[4]].split(","), function(k, v) {
								if(v!=null && v!=''){
							var oibj = {};
							oibj['title'] = v.replace(spltt[4],spltt[2]);
							oibj['defaultContent'] = '0';
							oibj['orderable'] = true;
							gridColumnNames.push(oibj);
							console.log("v.trim() "+ v.trim());
							console.log("v.trim().endsWith(",") "+v.trim().endsWith(","));
							fields = fields + spltt[1] +",";
							filFields=filFields + v +",";
							//filFields=filFields.replace("f.","t.");
							labels = labels + v.replace(spltt[4],spltt[2]) + ",";
								}
						});
					}else{
						fields = fields + spltt[1] + ",";
						labels = labels + spltt[2] + ",";
							id = id + spltt[5] + ",";
						filFields=filFields + spltt[4] + ",";
						//filFields=filFields.replace("f.","t.");
						var oibj = {};
						oibj['title'] = spltt[2];
						oibj['defaultContent'] = '0';
						oibj['orderable'] = true;
						gridColumnNames.push(oibj);
					}
					}
				});
	
			
		var item_filter_data = {};
		var jsonObj_filter_data = [];
		$(".filter")
				.each(
						function(index) {
							var filteV = $(this).attr('id').split('|');
							if ($('#filt option[value^="' + filteV[0].trim()
									+ '"]').length > 0) {
								var uniqClasss = $(this).closest("div")
										.removeClass("unselectable").attr(
												'class').split(' ').pop();
								var item_filter_data = {};
								item_filter_data["field"] = filteV[0].trim();
								item_filter_data["expression"] = filteV[1]
										.trim();
								if (filteV[2] != undefined) {
									if (filteV[2].trim().indexOf('~') > 0) {
										item_filter_data["value"] = filteV[2]
												.trim().split('~')[0]
												+ " and "
												+ filteV[2].trim().split('~')[1];
									} else {
										item_filter_data["value"] = filteV[2]
												.trim();
									}
								} else {
									item_filter_data["value"] = '';
								}
								item_filter_data["andOr"] = $(
										'.ar' + uniqClasss).val();
								item_filter_data["fieldDataType"] = filteV[3]
										.trim();
								jsonObj_filter_data.push(item_filter_data);
							} else {
								$(this).closest("div").remove();
							}
						});
		entity = entity.substring(0, entity.length - 1);
		fields = fields.substring(0, fields.length - 1);
		labels = labels.substring(0, labels.length - 1);
		filFields = filFields.substring(0, filFields.length - 1);
		if ($.fn.dataTable.isDataTable('#grid')) {
			table.destroy();
			$('#grid').empty(); // empty in case the columns change
		}
		var groupByField = $('#groupByf').val();
		if(groupByField==null){
			groupByField='';
		}
		alert(entity);
		if (obj == '1') {
			var title = $("#title").val();
			var des = $("#des").val();

			if (isEmpty(title) || isEmpty(des)) {
				alert("Please enter title and description")
				return;
			}
			$('#repId').val('');
		}
		jQuery.post("dynamicReport_saveDynamicReport.action", {
			reportTitle : title,
			reportDescription : des,
			entity : entity,
			id : $('#repId').val(),
			selectedFields : fields.toString(),
			selectedlabels : labels.toString(),
			filFields:filFields.toString(),
			//	groupByField : groupByField,
			header_fields : id,
			filter_data : JSON.stringify(jsonObj_filter_data)

		}, function(availabeColumns) {
			var modal = document.getElementById('myModal');
			modal.style.display = "none";
		/* 	$('#grid').DataTable().destroy();
			$("#grid").empty();
			*/
			alert("Report saved Successfully");
			resetALl();
			$('#savedR').val('');			
			populateSaved();
		}); 
	}

	function populateModalForSave() {
		/*if(isEmpty(finalOutPut_query)){
			alert("please create report before save")
			return;
		}else{*/
		var modal = document.getElementById('myModal');
		modal.style.display = "block";

		var span = document.getElementsByClassName("closeMod")[0];
		span.onclick = function() {
			modal.style.display = "none";
		}
		/*}*/

	}
	function populateSaved() {
		var query = "select * from saved_dynamic_report order by id desc";
		jQuery
				.post(
						"dynamicReport_populateSavedReports.action",
						{
							query : query,
							selectedFields : 'id,Title,Description,query,header_fields,filter_data,entity,fields_selected,GROUP_BY_FIELD'
						}, function(gridData) {

							json = $.parseJSON(gridData);
							$('#savedR').html('');
							$('#savedR').append(
									"<option value=''>Select</option>");
							for (var i = 0; i < json.length; i++) {
								item = {}
								var rowId = json[i]["id"];
								var title = json[i]["Title"];
								$('#savedR').append(
										"<option value='"+rowId+"'>" + title
												+ "</option>");

							}
						});
	}
	function renderSavedReport(jsData) {

		var jsonArr = jsData.fieldList;
		var filterData = jsData.filterData;
		var group = jsData.groupBy;
		var fielMap = {};

		for (var i = 0; i < jsonArr.length; i++) {
		
			backend_Value = jsonArr[i].table + "~" + jsonArr[i].field + "~"
					+ jsonArr[i].label + "~" + jsonArr[i].dataType + "~"
					+ jsonArr[i].filter + "~" + jsonArr[i].id;

			$("input[type=checkbox][value='" + backend_Value + "']").prop(
					"checked", true);
			$("input[type=checkbox][value='" + backend_Value + "']").closest(
					"li").addClass("active selected");

			var spltt = backend_Value.split('~');
			
			
			fielMap[jsonArr[i].filter] = jsonArr[i];
		}
addOpt();
$('#filt').trigger("change");
		var filte = $.parseJSON(filterData);
		if(!isEmpty(filte)) {
			 $("#filterDiv").collapse('show');	
		}
		jQuery
				.each(
						filte,
						function(index, tableRow_filter) {
							var f1 = tableRow_filter.field;
							var fieldJso = fielMap[f1.trim()];
							var exp = tableRow_filter.expression;
							var value = tableRow_filter.value;
							var expV = findExp(exp);
						
							var disp = f1 + "|" + exp + "|" + value + "|"
									+ fieldJso.dataType;

		var label = fieldJso.label + " " + $("#exp option[value='"+expV+"']").text() + " "
									+ value;
							var add_btn = $('<button/>').attr({
								type : 'button',
								class : 'btnApp btn-sts filter floated ',
								id : disp
							});
							var editBt = fieldJso.filter + '~'
									+ fieldJso.dataType + "~" + fieldJso.label
									+ "|" + fieldJso.dataType + "|" + value;
							$(add_btn).text(label);
							if ($('.filter').html() != null
									&& $('.filter').html() != '') {
								var div = $("<div/>", {
									css : "width:20px",
									class : "filterTd "
											+ $("#filtRowBt div").last().attr(
													'class').split(' ').pop()

								});
								$("#filtRowBt div")
										.last()
										.append(
												'<select class="select2   floated ar'
														+ $("#filtRowBt div")
																.last()
																.attr('class')
																.split(' ')
																.pop()
														+ '" ><option value="AND">AND </option><option value="OR">OR </option></select>');
								//$( "#filtRowBt div" ).last().append(div);
							}
							var span = $("<span />", {
								class : "floated glyphicon glyphicon-trash",
								onclick : "delRow('" + f1.replace(".", "")
										+ $('.filter').length + "',this)",
								style : "cursor:pointer"
							});

							var edi = $("<span />", {
								class : "floated glyphicon glyphicon-edit",
								onclick : "editRow('" + editBt + "',this,'t"
										+ f1.replace(".", "")
										+ $('.filter').length + "')",
								style : "cursor:pointer;margin:2px"
							});
							var div = $("<div/>", {
								css : "width:20px",
								class : "filterTd " + f1.replace(".", "")
										+ $('.filter').length

							});
							$(div).attr(
									"id",
									't' + f1.replace(".", "")
											+ $('.filter').length);
							$(div).append(add_btn).append(span).append(edi);
							$('#filtRowBt').append(div);

						});

	}
	function execSaved(ff) {
		var loadd = false;
		if ($.fn.dataTable.isDataTable('#grid') ){
			if(confirm("Existing Report will be resetted and loaded again")){
				loadd=true;
			}else{
				loadd = false
			}
		}else{
			loadd=true;
		}
		if(loadd){
		resetALl();
		var rep = $('#savedR').val();
		if (rep == '') {
			alert("Please select saved report");
			return false;
		}
		$('#repId').val(rep);
		jQuery
				.post(
						"dynamicReport_executeSavedReport.action",
						{
							id : rep
						},
						function(gridData) {
							var gr = JSON.parse(gridData);
							var gridColumnNames = new Array();
							$.each(gr.colName.split(","), function(i, v) {
								var oibj = {};
								oibj['title'] = v;
								oibj['defaultContent'] = '0';
								oibj['orderable'] = true;
								gridColumnNames.push(oibj);
							});
							var query = gr.query.replace(/"/g, '\\"');
							table = $('#grid')
									.DataTable(
											{
												data : JSON.parse(gridData).aaData,
												"searching" : true,

												language : {
													sLoadingRecords : '<span style="width:100%;"><img src="img/ajax-loader.gif"></span>'
												},
												columns : gridColumnNames,
												bAutoWidth : false,
												//responsive : true,
												colReorder : true,
												scrollX : true,
												//stateSave : true,
												dom : 'Bfrtip',
												buttons : [
														{
															extend : 'excel',
															filename : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															title : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															footer : true,
															exportOptions : {
																columns : "thead th:not(.details-control)"
															}

														},
														{
															extend : 'pdf',
															filename : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															title : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															exportOptions : {
																columns : "thead th:not(.details-control)"
															}

														},
														{
															extend : 'csv',
															filename : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															title : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															exportOptions : {
																columns : "thead th:not(.details-control)"
															}

														},
														{
															extend : 'print',
															filename : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															title : $(
																	'.breadCrumbNavigation li')
																	.last()
																	.text()
																	.replace(
																			/\s/g,
																			''),
															exportOptions : {
																columns : "thead th:not(.details-control)"
															}

														} ]
											});
							renderSavedReport(gr);
						});
		}
	}
</script>
<body>
	<li class="divToggle"><a href="#" onclick="toggleDiv()"> <i
			class="fa fa-bars"></i>
	</a></li>
	<s:hidden id="repId"></s:hidden>
	<div class="flexbox-item flexbox-item-grow dashboardPageWrapper" style="height: 100% !Important;">
	<div class="appContentWrapper column-left">
		<div id="saved" class="flexiWrapper appContentWrapper marginBottom">
			<label style="color: black; font-weight: 700; font-size: 13px">Saved
				Reports</label>
			<s:select class="form-control" name="selectedFields" id="savedR"
				list="{}" />
			<span type="button" class="glyphicon glyphicon-play"
				onclick="execSaved(this.value)" title="Execute Saved Report"
				style="cursor: pointer"> </span>
		</div>
		<div id="multi" class="appContentWrapper marginBottom">
			<s:select multiple="true" name="selectedFields" id="fields" list="{}" />

		</div>
		<div id="clear"></div>

	</div>
	<div class="appContentWrapper dashboardPage">
	<div class="column-right">
	<span class="glyphicon glyphicon-floppy-disk" style="cursor: pointer"
				onclick="populateModalForSave()" title="Save As New Report">
			</span>&nbsp;&nbsp;&nbsp; <span
				class="glyphicon  glyphicon glyphicon-pencil"
				style="cursor: pointer" title="Update already saved report"
				onclick="saveQuery('0')"> </span> <span type="button"
				title="Execute The Report" class="glyphicon glyphicon-play newExec"
				onclick="generateQuery('0')" style="cursor: pointer"> </span>
	<div class="appContentWrapper marginBottom pers_info">
	
<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion" href="#filterDiv"
						class="accrodianTxt collapsed"> <s:text name="Filters" />
					</a>

				</h2>
			</div>
		<div id="filterDiv"
			class="panel-collapse collapse "
			style="">
			

			<table class="table" width="90%" align="center">
				<tr>
					<td class="">Group By Field</td>
					<td class="">Sum</td>
					<td class="">Average</td>
					<td class="">Count
					<td>
				</tr>
				<tr>
					<td><s:select id="groupByf" list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control select2" tabindex="10" multiple="true" />
						<%-- 		<s:select id="groupByf" class="form-control  select2"
							headerkey="Select" headerValue="" multiple="true"
							 list="{}" /> --%></td>
					<td><s:select id="sumF" class="form-control  select2"
							headerkey="Select" headerValue="" list="{}" multiple="true" /></td>
					<td><s:select id="avgF" class="form-control  select2"
							headerkey="Select" headerValue="" multiple="true" list="{}" /></td>
					<td><s:select id="countF" class="form-control  select2"
							multiple="true" headerkey="Select" headerValue="" list="{}" /></td>
				</tr>
			</table>
			<table class="table" width="90%" align="center">
				<tr>
					<td class="">Field</td>
					<td class="">Condition</td>
					<td class="">Value</td>
					<td class="">Action
					<td>
				</tr>
				<tr id="filtRow">
					<td class=""><s:select id="filt" class="form-control  select2"
							headerkey="Select" headerValue=""
							onchange="populateExp(this.value)" list="{}" /> <s:hidden
							id="editFIlt" /></td>
					<td class=""><s:select id="exp" headerkey="Select"
							class="form-control  select2" headerValue=""
							onchange="enableDateRangePicker(this.value,'filtRow');" list="{}" /></td>
					<td class=""></td>
					<td class=""><span id='close' class="glyphicon glyphicon-plus"
						style="cursor: pointer" onclick='addRow(); return false;'></span>
					<td>
				</tr>
			</table>
			<table class="" width="100%" align="center">
				<tr id="filtRowBt">
				</tr>
			</table>

		</div>
		</div>
			<!-- <div id="jQueryGridTable"> -->
			<table style="width: 100%" id="grid" class="table"></table>
			<!-- </div> -->
		</div>

	</div>
	</div>
	<div id="myModal" class="modal">

		<!-- Modal content -->
		<div class="modal-content">
			<span class="closeMod">&times;</span>

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
							onclick="saveQuery('1');">
							<i class="glyphicon glyphicon-save" aria-hidden="true"></i>
							<s:property value="%{getLocaleProperty('dynamicReport.Save')}" />
						</button>


					</td>

				</tr>

			</table>


		</div>

	</div>
</body>

