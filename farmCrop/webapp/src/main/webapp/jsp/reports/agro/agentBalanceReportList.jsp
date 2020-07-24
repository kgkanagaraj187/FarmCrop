<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<style type="text/css">
.ui-autocomplete {
	background: #D3DEB0;
	padding: 10px;
	border: 1px solid #939585;
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden
}

.ui-jqgrid-btable td {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-btable td:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid-btable tr {
	border: none !important;
}

.ui-jqgrid-htable th {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-htable th:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid .ui-jqgrid-htable TH.ui-th-ltr {
	border-bottom: solid 1px #567304 !important;
}

.ui-jqgrid .ui-jqgrid-btable {
	border-right: none !important;
}

.ui-jqgrid .ui-jqgrid-htable th div {
	padding-right: 0px !important;
}
</style>
</head>
<body>
<script type="text/javascript">
		var accountNumber = '';
		var agentId = '';
		var balanceType = '1';
		var sDate = '';
		var eDate = '';
		var recordLimit = '<s:property value="exportLimit"/>';

		jQuery(document)
				.ready(
						function() {
							jQuery(".well").hide();
							//document.getElementById('fieldl').selectedIndex = 0;
							document.getElementById("hiddenStartDate").value;
							document.getElementById("hiddenEndDate").value;
							sDate = document.getElementById("hiddenStartDate").value;
							eDate = document.getElementById("hiddenEndDate").value;
							document.getElementById('accountNumber').value = '';
							document.getElementById('agentId').selectedIndex = 0;
							document.getElementById('balanceType').selectedIndex = 0;

							jQuery("#detail")
									.jqGrid(
											{
												url : 'agentBalanceReport_detail.action',
												pager : '#pagerForDetail',
												datatype : "json",
												mtype : 'POST',
												postData : {
													"startDate" : function() {
														return sDate;
													},
													"endDate" : function() {
														return eDate;

													},
													"filter.agentId" : function() {
														return agentId;
													},
													"filter.accountNumber" : function() {
														return accountNumber;
													},
													"filter.balanceType" : function() {
														return balanceType;
													}
												},
												colNames : [
														'<s:property value="%{getLocaleProperty('profile.agent')}"/>	',
														'<s:property value="%{getLocaleProperty('agentbalagentName')}"/>	',
														'<s:text name="servicePointNameGrid"/>',
														'<s:text name="accountNumberGrid"/>',
														'<s:text name="finalBalanceGrid"/>',

												],
												colModel : [ {
													name : 'agentId',
													index : 'agentId',
													sortable : false
												}, {
													name : 'agentName',
													index : 'agentName',
													sortable : false
												}, {
													name : 'servicePointName',
													index : 'servicePointName',
													sortable : false
												}, {
													name : 'accountNumber',
													index : 'accountNumber',
													sortable : false
												}, {
													name : 'finalBalance',
													index : 'finalBalance',
													sortable : false,
													align : 'right'
												},

												],
												height : 301,
												width : $("#baseDiv").width(),
												scrollOffset : 0,
												sortname : 'id',
												sortorder : "desc",
												 shrinkToFit:true,
												rowNum : 10,
												rowList : [ 10, 25, 50 ],
												viewrecords : true,
												subGrid : true,
												subGridOptions : {
													"plusicon" : "glyphicon-plus-sign",
													"minusicon" : "glyphicon-minus-sign",
													"openicon" : "glyphicon-hand-right",
												},
												subGridRowExpanded : function(
														subgrid_id, row_id) {

													var subgrid_table_id, pager_id;
													subgrid_table_id = subgrid_id
															+ "_t";
													pager_id = "p_"
															+ subgrid_table_id;
													$("#" + subgrid_id)
															.html(
																	"<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
													var ret = jQuery("#detail")
															.jqGrid(
																	'getRowData',
																	row_id);
													jQuery(
															"#"
																	+ subgrid_table_id)
															.jqGrid(
																	{
																		url : 'agentBalanceReport_subGridDetail.action?profileId='
																				+ ret.agentId
																				+ "&filter.balanceType="
																				+ document
																						.getElementById("balanceType").value,
																		pager : pager_id,
																		datatype : "json",
																		colNames : [
																				'<s:text name="txnTimeGrid"/>',
																				'<s:text name="receiptNoGrid"/>',
																				'<s:text name="initialBalanceGrid"/>',
																				'<s:text name="txnAmountGrid"/>',
																				'<s:text name="balanceAmountGrid"/>'

																		],
																		colModel : [
																				{
																					name : 'txnTime',
																					index : 'txnTime',
																					sortable : false
																				},
																				{
																					name : 'receiptNo',
																					index : 'receiptNo',
																					sortable : false
																				},
																				{
																					name : 'initialBalance',
																					index : 'initialBalance',
																					sortable : false,
																					align : 'right'
																				},
																				{
																					name : 'txnAmount',
																					index : 'txnAmount',
																					sortable : false,
																					align : 'right'
																				},
																				{
																					name : 'balanceAmount',
																					index : 'balanceAmount',
																					sortable : false,
																					align : 'right'
																				} ],
																		scrollOffset : 0,
																		sortname : 'id',
																		height : '100%',
																		sortorder : "desc",
																		autowidth : true,
																		 shrinkToFit:true,
																		rowNum : 10,
																		rowList : [
																				10,
																				25,
																				50 ],
																		viewrecords : true
																	});
													jQuery(
															"#"
																	+ subgrid_table_id)
															.jqGrid(
																	'navGrid',
																	"#"
																			+ pager_id,
																	{
																		edit : false,
																		add : false,
																		del : false,
																		search : false,
																		refresh : false
																	})
													//jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
													jQuery("#" + subgrid_id)
															.parent().css(
																	"width",
																	"100%");
													jQuery("#" + subgrid_id)
															.parent()
															.css(
																	"background-color",
																	"#fff");
													jQuery("#" + subgrid_id)
															.find(
																	"#gview_"
																			+ subgrid_table_id
																			+ ",#"
																			+ subgrid_table_id
																			+ ",#gbox_"
																			+ subgrid_table_id
																			+ ",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable")
															.css("width",
																	"100%");
													jQuery("#" + subgrid_id)
															.find(
																	"#gview_"
																			+ subgrid_table_id
																			+ " td,#"
																			+ subgrid_table_id
																			+ " td,#gbox_"
																			+ subgrid_table_id
																			+ " td")
															.css("border",
																	"none");
													jQuery("#" + subgrid_id)
															.find(
																	".ui-jqgrid-hbox")
															.css(
																	"background-color",
																	"#fff");
												},

												onSortCol : function(index,
														idxcol, sortorder) {
													if (this.p.lastsort >= 0
															&& this.p.lastsort !== idxcol
															&& this.p.colModel[this.p.lastsort].sortable !== false) {
														$(
																this.grid.headers[this.p.lastsort].el)
																.find(
																		">div.ui-jqgrid-sortable>span.s-ico")
																.show();
													}
												}
											});

							colModel = jQuery("#detail").jqGrid('getGridParam',
									'colModel');
							$(
									'#gbox_'
											+ $.jgrid
													.jqID(jQuery("#detail")[0].id)
											+ ' tr.ui-jqgrid-labels th.ui-th-column')
									.each(
											function(i) {
												var cmi = colModel[i], colName = cmi.name;

												if (cmi.sortable !== false) {
													$(this)
															.find(
																	'>div.ui-jqgrid-sortable>span.s-ico')
															.show();
												} else if (!cmi.sortable
														&& colName !== 'rn'
														&& colName !== 'cb'
														&& colName !== 'subgrid') {
													$(this)
															.find(
																	'>div.ui-jqgrid-sortable')
															.css(
																	{
																		cursor : 'default'
																	});
												}
											});

							jQuery("#detail").jqGrid('navGrid',
									'#pagerForDetail', {
										edit : false,
										add : false,
										del : false,
										search : false,
										refresh : false
									})

							jQuery("#generate").click(function() {
								reloadGrid();
							});

							jQuery("#clear").click(function() {
								clear();
							});
							$("#exportPdf").click(function() {

								if (isDataAvailable("#detail")) {

									exportPdf();
								} else {
									alert('<s:text name="export.nodata"/>');
								}

							});
							$("#exportXls").click(function() {
								if (isDataAvailable("#detail")) {

									exportXls();
								} else {
									alert('<s:text name="export.nodata"/>');
								}

							});
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
										document
												.getElementById("hiddenStartDate").value);
								//	alert(startDate);
								var endDate = new Date(document
										.getElementById("hiddenEndDate").value);
								//	alert(endDate);

								if (startDate > endDate) {
									alert('<s:text name="date.range"/>');
								} else {

									sDate = document
											.getElementById("hiddenStartDate").value;
									eDate = document
											.getElementById("hiddenEndDate").value;
									accountNumber = document
											.getElementById("accountNumber").value;
									agentId = document
											.getElementById("agentId").value;
									balanceType = document
											.getElementById("balanceType").value;

									jQuery("#detail")
											.jqGrid(
													'setGridParam',
													{
														url : "agentBalanceReport_detail.action?",
														page : 1
													}).trigger('reloadGrid');
								}

							}

							function clear() {
								document.form.action = "agentBalanceReport_list.action";
								document.form.submit();
							}

							function exportPdf() {
								document.form.action = "agentBalanceReport_populatePDF.action";
								document.form.submit();
							}
							function exportXls() {
								document.form.action = "agentBalanceReport_populateXLS.action";
								document.form.submit();
							}
						});

		function exportXLS() {
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
					url : 'agentBalanceReport_populateXLS.action'
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}
	</script>


	<s:form name="form" action="agentBalanceReport_list">
		<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
				<div class="reportFilterItem">
					<label for="txt"><s:text name="startDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm" cssStyle="width:150px" />
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for="txt"><s:text name="accountNumberGrid" /></label>
					<div class="form-element">
						<s:textfield name="filter.accountNumber" id="accountNumber"
					cssClass="form-control input-sm" cssStyle="width:150px" />					
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for="txt"><s:property value="%{getLocaleProperty('profile.agent')}"/></label>
					<div class="form-element">
						<s:select name="filter.agentId" id="agentId" list="agentList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm" cssStyle="width:150px" />				
					</div>
				</div>
				
				<div class="reportFilterItem">
					<label for="txt"><s:text name="balanceType" /></label>
					<div class="form-element">
						<s:select name="filter.balanceType" id="balanceType"
					list="balanceTypeList" listKey="key" listValue="value"
					cssClass="form-control input-sm" cssStyle="width:150px" />			
					</div>
				</div>
				
						<div class="reportFilterItem" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
				
			</div>
		</div>

	
	</s:form>


	<div style="width: 100%;" id="baseDiv">
		<table id="detail"></table>

		<div id="pagerForDetail"></div>
		<div id="pager_id"></div>
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
</body>
