<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>
<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

</head>
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	/*height: auto !important;*/
}
</style>
<body>
	<script type="text/javascript">
		var temp = 0;
		$(document)
				.ready(
						function() {
							onFilterData();
							jQuery(".well").hide();
							$("#daterange").data().daterangepicker.startDate = moment(
									document.getElementById("hiddenStartDate").value,
									"MM-DD-YYYY");
							$("#daterange").data().daterangepicker.endDate = moment(
									document.getElementById("hiddenEndDate").value,
									"MM-DD-YYYY");
							$("#daterange").data().daterangepicker.updateView();
							$("#daterange").data().daterangepicker
									.updateCalendars();
							$('.applyBtn').click();
							document.getElementById('farmerId').selectedIndex = 0;
							//document.getElementById('farmerFatherId').selectedIndex = 0;
							document.getElementById('villageName').selectedIndex = 0;
							document.getElementById('seasonId').selectedIndex = 0;
							loadGrid();

						});

		function loadGrid() {

			jQuery("#detail")
					.jqGrid(
							{
								url : 'pesticideUsageReport_data.action',
								pager : '#pagerForDetail',
								mtype : 'POST',
								styleUI : 'Bootstrap',
								postData : {
									"startDate" : function() {
										return document
												.getElementById("hiddenStartDate").value;
									},
									"endDate" : function() {
										return document
												.getElementById("hiddenEndDate").value;
									},

									"farmName" : function() {
										return document
												.getElementById("farmName").value;
									},

									"farmerId" : function() {
										return document
												.getElementById("farmerId").value;
									},
									/*"farmerFatherId" : function() {
										return document
												.getElementById("farmerFatherId").value;
									},*/
									"villageName" : function() {
										return document
												.getElementById("villageName").value;
									},
									"seasonCode" : function() {
										return document
												.getElementById("seasonId").value;
									},
									"inspectionType" : function() {
										return '1'
									}

								},
								datatype : "json",
								colNames : [
										'<s:text name="pestiinspectionDate"/>',
										'<s:text name="pestifarmName"/>',
										'<s:text name="farmerCode"/>',
										'<s:text name="farmerName"/>',
										//'<s:text name="fatherName"/>',
										'<s:text name="villageName"/>',
										'<s:text name="irrigationStatus"/>',
										'<s:text name="totBCArea"/>',
										'<s:text name="landpreparationCompleted"/>',
										'<s:text name="sowingDate"/>',
										'<s:text name="bordercrop"/>',
										'<s:text name="intercrop"/>',
										'<s:text name="pestichemicalSpray"/>',
										'<s:text name="monoImid"/>',
										'<s:text name="sprayCocktail"/>',
										'<s:text name="repeatPest"/>',
										'<s:text name="nitrogenFert"/>',
										'<s:text name="lastSpace"/>',
										'<s:text name="currentSpace"/>' ],
								colModel : [ {
									name : 'inspectionDate',
									index : 'inspectionDate',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'farmName',
									index : 'farmName',
									width : 150,
									sortable : false
								}, {
									name : 'farmerCode',
									index : 'farmerCode',
									width : 150,
									sortable : false
								}, {
									name : 'farmerName',
									index : 'farmerName',
									width : 150,
									sortable : false
								}, /*{
									name : 'fatherName',
									index : 'fatherName',
									width : 150,
									sortable : false,
									search : false
								},*/ {
									name : 'villageName',
									index : 'villageName',
									width : 200,
									sortable : false,
									search : false
								}, {
									name : 'irrigationStatus',
									index : 'irrigationStatus',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'totBCArea',
									index : 'totBCArea',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'landPrep',
									index : 'landPrep',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'sowingDate',
									index : 'sowingDate',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'bordercrop',
									index : 'bordercrop',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'intercrop',
									index : 'intercrop',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'chemicalSpray',
									index : 'chemicalSpray',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'monoImid',
									index : 'monoImid',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'sprayCocktail',
									index : 'sprayCocktail',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'repeatPest',
									index : 'repeatPest',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'nitrogenFert',
									index : 'nitrogenFert',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'lastSpace',
									index : 'lastSpace',
									width : 150,
									sortable : false,
									search : false
								}, {
									name : 'currentSpace',
									index : 'currentSpace',
									width : 150,
									sortable : false,
									search : false
								} ],
								height : 301,
								width : $("#baseDiv").width(), // assign parent div width
								scrollOffset : 0,
								rowNum : 10,
								rowList : [ 10, 25, 50 ],
								sortname : 'id',
								autowidth : true,
								shrinkToFit : false,
								sortorder : "desc",
								viewrecords : true, // for viewing noofrecords displaying string at the right side of the table
								beforeSelectRow : function(rowid, e) {
									var iCol = jQuery.jgrid
											.getCellIndex(e.target);
									if (iCol >= 8) {
										return false;
									} else {
										return true;
									}
								},
								onSelectRow : function(id) {
									document.detailform.id.value = id;
									document.detailform.submit();
								},
							});
			jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {
				edit : false,
				add : false,
				del : false,
				search : false,
				refresh : true
			}) // enabled refresh for reloading grid
			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
			$(
					'#gbox_' + jQuery.jgrid.jqID(jQuery("#detail")[0].id)
							+ ' tr.ui-jqgrid-labels th.ui-th-column').each(
					function(i) {
						var cmi = colModel[i], colName = cmi.name;

						if (cmi.sortable !== false) {
							$(this).find('>div.ui-jqgrid-sortable>span.s-ico')
									.show();
						} else if (!cmi.sortable && colName !== 'rn'
								&& colName !== 'cb' && colName !== 'subgrid') {
							$(this).find(
									'>div.ui-jqgrid-sortable.ui-jqgrid-htable')
									.css({
										cursor : 'default'
									});
						}
					});
			jQuery("#generate").click(function() {
				reloadGrid();
			});

			jQuery("#clear").click(function() {
				clear();
			});
			jQuery("#minus").click(function() {
				var flag = "edit";
				var index = jQuery("#fieldl option:selected").index();
				if (!jQuery("." + index).hasClass("hide")) {
					jQuery("." + index).addClass("hide");
					temp--;
				}

				jQuery("." + index + "> select").val("");
				jQuery("#fieldl").val("0");

				jQuery("#filter-fields").remove(jQuery("." + index));
				document.getElementById('fieldl').selectedIndex = 0;
				if (temp == 0) {
					jQuery(".well").hide();
				}
				$('.' + index).find('select').val('');
				resetReportFilter();
				reloadGrid();

			});

			jQuery("#plus").click(function() {
				if (jQuery("#fieldl option:selected").val() != '') {
					jQuery(".well").show();
					var index = jQuery("#fieldl option:selected").index();
					if (jQuery("." + index).hasClass("hide")) {
						temp++;
					}
					jQuery("." + index).removeClass("hide");

					jQuery("#searchbtn").append(jQuery("." + index));
				}

			});
		}
		
		function onFilterData(){
			callAjaxMethod("pesticideUsageReport_populateFarmList.action","farmName");
			callAjaxMethod("pesticideUsageReport_populateFarmerList.action","farmerId");
			callAjaxMethod("pesticideUsageReport_populateFarmerFatherList.action","farmerFatherId");
			callAjaxMethod("pesticideUsageReport_populateVillageList.action","villageName");
			callAjaxMethod("pesticideUsageReport_populateSeasonList.action","seasonId");
		}
		function callAjaxMethod(url,name){
			var cat = $.ajax({
				url: url,
				async: true, 
				type: 'post',
				success: function(result) {
					insertOptions(name,JSON.parse(result));
				}        	

			})
			
		}
		
		function reloadGrid() {
			var d1 = jQuery('#daterange').val();
			var d2 = d1.split("-");
			//	alert(d1);
			var value1 = d2[0];
			//alert(value1);
			var value2 = d2[1];
			//alert(value2);
			document.getElementById("hiddenStartDate").value = value1;

			document.getElementById("hiddenEndDate").value = value2;
			var startDate = new Date(
					document.getElementById("hiddenStartDate").value);
			var endDate = new Date(
					document.getElementById("hiddenEndDate").value);
			if (startDate > endDate) {
				alert('<s:text name="date.range"/>');
			} else {

				jQuery("#detail").jqGrid('setGridParam', {
					url : "pesticideUsageReport_data.action?",
					page : 1
				}).trigger('reloadGrid');

			}
		}
		function clear() {
			$("#seasonId").val("");
			resetReportFilter();
			document.form.submit();
		}
		function exportXLS() {
			/* if (isDataAvailable("#" + inspType)) {
				jQuery("#" + inspType).setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#" + inspType).jqGrid('excelExport', {
					url : 'pesticideUsageReport_populateXLS.action'
				});
			}  */if(isDataAvailable("#detail")){
				jQuery("#detail").setGridParam ({postData: {rows : 0}});
				jQuery("#detail").jqGrid('excelExport', {url: 'pesticideUsageReport_populateXLS.action'});
			}
			else {
				alert('<s:text name="export.nodata"/>');
			}

		}
	</script>

	<s:form name="form" action="pesticideUsageReport_list">
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">

				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="date" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control input-sm" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="farmName" /></label>
					<div class="form-element">
						<s:select name="farmName" id="farmName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>


				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="farmerName" /></label>
					<div class="form-element">
						<s:select name="farmerId" id="farmerId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem hide">
					<label for="filter.farmer.id"><s:text name="fatherName" /></label>
					<div class="form-element">
						<s:select name="farmerFatherId" id="farmerFatherId"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"><s:text name="villageName" /></label>
					<div class="form-element">
						<s:select name="villageName" id="villageName" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				<div class="reportFilterItem">
					<label for="filter.farmer.id"> <s:text name="seasonCode" /></label>
					<div class="form-element">
						<s:select name="seasonCode" id="seasonId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>

				<div class="reportFilterItem" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
			</div>
		</div>


		<div class="appContentWrapper marginBottom">
			<div class="flex-layout reportData">
				<!-- <div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Farmers&nbsp;<i
							class="fa fa-user"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Cultivation Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Others&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="strong">0</span> Total Cost&nbsp;<i
							class="fa fa-dollar"></i></span>
					</div>
				</div>
			</div> -->

				<div class="flexItem text-right flex-right">
					<div class="dropdown">
						<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
							type="button" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">
							<i class="fa fa-share"></i>
							<s:text name="export" />
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right"
							aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
							<%-- <li><a href="#" onclick="exportPDF();" role="tab"
								data-toggle="tab" aria-controls="dropdown1"
								aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
										name="pdf" /></a></li> --%>
							<li><a href="#" onclick="exportXLS()" role="tab"
								data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
									class="fa fa-table"></i> <s:text name="excel" /></a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="jqGridwrapper baseDiv">
				<table id="grid"></table>
				<div id="pager"></div>
			</div>
			<div style="width: 100%;" id="baseDiv">
				<table id="detail"></table>
				<div id="pagerForDetail"></div>
				<div id="pager_id"></div>
			</div>
		</div>
	</s:form>
	<div class="clear"></div>
	<s:form name="detailForm" action="pesticideUsageReport_list">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
