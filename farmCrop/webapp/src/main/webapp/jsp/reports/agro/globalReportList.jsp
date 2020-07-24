<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
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
		var farmerId = '';
		var seasonCode = '';
		var buyerInfo = '';
		var cropType = '';
		var icsName = '';
		var branchIdParma = '';
		var recordLimit = '<s:property value="exportLimit"/>';

		jQuery(document)
				.ready(
						function() {
							onFilterData();
							$('.applyBtn').click();
							document.getElementById('farmerId').selectedIndex = 0;
							document.getElementById('seasonId').selectedIndex = 0;

							jQuery("#detail")
									.jqGrid(
											{
												url : 'globalReport_data.action?',
												pager : '#pagerForDetail',
												datatype : "json",
												styleUI : 'Bootstrap',
												mtype : 'POST',
												postData : {
													"seasonCode" : function() {
														return document
																.getElementById("seasonId").value;
													},

													"farmerId" : function() {
														return document
																.getElementById("farmerId").value;
													},

												},

												colNames : [
												            
														'<s:text name="txnDateTime"/>',
														'<s:text name="season"/>',
														'<s:property value="%{getLocaleProperty('farmer.id')}"/>',
														'<s:property value="%{getLocaleProperty('farmer')}"/>',
														'<s:text name="group"/>',
														'<s:text name="disAmount"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)',
														'<s:text name="procAmount"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'

												],
												colModel : [

												{
													name : 'txnTime',
													index : 'txnTime',
													width : 100,
													
													sortable : false
												}, {
													name : 'season',
													index : 'season',
													
													sortable : false
												}, {
													name : 'farmerId',
													index : 'farmerId',
													
													sortable : false
												}, {
													name : 'samithi',
													index : 'samithi',
													
													sortable : false
												}, {
													name : 'farmerName',
													index : 'farmerName',
													
													sortable : false
												}, {
													name : 'disAmount',
													index : 'disAmount',
													align : 'right',
													
													sortable : false
												}, {
													name : 'procAmount',
													index : 'procAmount',
													align : 'right',
													
													sortable : false
												} ],
												height: 301, 
											    width: $("#baseDiv").width(), // assign parent div width
											    scrollOffset: 19,
											    shrinkToFit: true,
											   	rowNum:10,
											   	rowList : [10,25,50],
											    sortname:'id',			  
											    sortorder: "desc",
											    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
										        onSortCol: function (index, idxcol, sortorder) {
											        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
										                    && this.p.colModel[this.p.lastsort].sortable !== false) {
										                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
										            }
										        }
											});
							jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
							colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
						    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
						        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
						        var cmi = colModel[i], colName = cmi.name;

						        if (cmi.sortable !== false) {
						            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
						        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
						            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
						        }
						    });

							jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})

		
		jQuery("#generate").click( function() {
			reloadGrid();	
		});
							jQuery("#clear").click( function() {
								clear();
							});
							function reloadGrid() {
								jQuery("#detail").jqGrid('setGridParam', {
									url : "globalReport_data.action?",
									page : 1
								}).trigger('reloadGrid');
							}
							
							function clear() {
								resetReportFilter();
								document.form.submit();
							}
					
						});

		function exportXLS(){
			var count=jQuery("#detail").jqGrid('getGridParam', 'records');
			/* if(count>recordLimit){
				 alert('<s:text name="export.limited"/>');
			}
			else */ if(isDataAvailable("#detail")){
				jQuery("#detail").setGridParam ({postData: {rows : 0}});
				jQuery("#detail").jqGrid('excelExport', {url: 'globalReport_populateXLS.action'});
			}else{
			     alert('<s:text name="export.nodata"/>');
			}
		}	

		function exportPDF() {
			var count = jQuery("#detail").jqGrid('getGridParam', 'records');
			if (count > recordLimit) {
				alert('<s:text name="export.limited"/>');
			} else if (isDataAvailable("#detail")) {
				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'cropSaleReport_populatePDF.action'
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}
		function onFilterData(){
			callAjaxMethod("globalReport_populateSeasonList.action","seasonId");
			callAjaxMethod("globalReport_populateFarmerList.action","farmerId");
			
		}
		function callAjaxMethod(url,name){
			var cat = $.ajax({
				url: url,
				async: true, 
				type: 'post',
				success: function(result) {
					insertOptions(name,JSON.parse(result));
				}        	

			});
			
		}
	</script>

	<s:form name="form" action="globalReport_list">
	<div class="appContentWrapper marginBottom">
		
               <section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="season"><s:text name="season" /></label>
					<div class="form-element">
						<s:select name="filter.seasonCode" id="seasonId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2"/>
					</div>
				</div>

				<div class="filterEle">
					<label for="filter.farmerId"><s:property value="%{getLocaleProperty('farmer')}"/></label>
					<div class="form-element">
						<s:select name="filter.farmerId" id="farmerId" list="{}"
							headerKey="" headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2"/>
					</div>
				</div>

								<div class="filterEle" style="margin-top: 2%;margin-right: 0%;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
				</div>
				</section>
		
	</div>
</s:form>

<div class="appContentWrapper marginBottom">
<div class="flex-layout reportData">
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
					
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
		</div>
		<div style="width: 98%;" id="baseDiv">
			<table id="detail"></table>

			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
