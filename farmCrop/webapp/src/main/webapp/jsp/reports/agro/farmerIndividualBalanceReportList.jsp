<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
</head>

<style>
th.ui-th-column div{
        white-space:normal !important;
        height:auto !important;
        padding:2px;
        }
</style>


<body>
	<script type="text/javascript">
		var farmerId = '';
		var seasonCode = '';
		var balanceType = '1';
		var sDate = '';
		var eDate = '';
		var recordLimit = '<s:property value="exportLimit"/>';
			
		jQuery(document)
				.ready(
						
						function() {
							
							var url="farmerIndividualBalanceReport_data.action";
							
							//$("#balanceTypeId").val(316);
							//document.getElementById('fieldl').selectedIndex=0;
							//	document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
							//	document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
							//	sDate = document.getElementById("startDate").value;
							//	eDate = document.getElementById("endDate").value;
							
							//document.getElementById('balanceTypeId').selectedIndex = 0;
							 $('.applyBtn').click();
								
							 jQuery(".well").hide();
							//	document.getElementById('balanceType').selectedIndex=0;
							jQuery("#detail")
									.jqGrid(
											{
												url : url,
												pager : '#pagerForDetail',
												datatype : "json",
												mtype : 'POST',
												postData : {
												
													"farmerId" : function() {
														 return document.getElementById("farmerId").value;
													},
													"seasonCode" : function() {
														 return document.getElementById("seasonId").value;
													},
													
													
												},
												colNames : [
												
														'<s:text name="farmerIdGrid"/>',
														'<s:text name="farmerNameGrid"/>',
												
														'<s:text name="finalBalance"/>'
														],
														
												colModel : [ 
										  
												             {
													name : 'farmerId',
													index : 'farmerId',
													width : 250,
													sortable : false
												},
												 {
													name : 'farmerName',
													index : 'farmerName',
													width : 250,
													sortable : false
												},
											 {
													name : 'finalBalance',
													index : 'finalBalance',
													width : 250,
													sortable : false,
													align : 'right'
												}
												],
												height : 380,
												width : $("#baseDiv").width(),
												scrollOffset : 0,
												sortname : 'id',
												sortorder : "desc",
												rowNum : 10,
												rowList : [ 10, 25, 50 ],
												viewrecords : true,
												subGrid: false,
												   subGridOptions: {
												   "plusicon" : "glyphicon-plus-sign",
												   "minusicon" : "glyphicon-minus-sign",
												   "openicon" : "glyphicon-hand-right",
												   },
												   
												   subGridRowExpanded: function(subgrid_id, row_id){
													   
													   var subgrid_table_id, pager_id; 
													   subgrid_table_id = subgrid_id+"_t"; 
													   pager_id = "p_"+subgrid_table_id; 
													   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
													   //var ret = jQuery("#detail").jqGrid('getRowData',row_id);
													   var arrayVal = row_id.split("_"); 
													   
													   var coOperativeId ='';
													   var procurementProductId = '';
													   var farmerId = '';

													   if(arrayVal.length>0){
														   coOperativeId = arrayVal[0];
														   procurementProductId = arrayVal[1];
														   farmerId = arrayVal[2];
													   }
													  
													   jQuery("#"+subgrid_table_id).jqGrid(
													   {
															url:'farmerBalanceReport_data.action?id='+row_id,
														   	pager: pager_id,
														   	datatype: "json",
															mtype: 'POST',				   	
														   	postData:{	
														   	
																	"farmerId" : function() {
																		return farmerId;
																	},
																
																	"agentId" : function() {
																		return agentId;
																	},
														   	
														   		"startDate" : function(){			  
																	return  document.getElementById("hiddenStartDate").value;
																  },
																  "endDate" : function(){			  
																	return document.getElementById("hiddenEndDate").value;
																  }  
															},	
														   	colNames:[	
														   	       	  '<s:text name="txnTimeGrid"/> (<s:property value="%{getGeneralDateFormat().toUpperCase().concat(' HH:MM:SS')}" />)',	
														   	       	  '<s:text name="receiptNoGrid"/>',
														  		      '<s:text name="txnDescGrid"/>',
														  		  //    '<s:text name="payementMode"/>',
														  		      '<s:text name="initialBalanceGrid"/>',
														  		     '<s:text name="txnAmountGrid"/>',
														  		   '<s:text name="balanceAmountGrid"/>'
												 	      	 ],
														   	colModel:[	
																	{name:'txnTimeGrid',index:'txnTimeGrid',width:180,sortable:false},
																	{name:'receiptNoGrid',index:'receiptNoGrid',width:150,sortable:false},
										   							{name:'txnDescGrid',index:'txnDescGrid',width:150,sortable:false,align:'right'},
										   						//	{name:'payementMode',index:'paymentMode',width:150,sortable:false,align:'right'},
										   							{name:'initialBalanceGrid',index:'initialBalanceGrid',width:150,sortable:false,align:'right'},
										   							{name:'txnAmt',index:'txnAmt',width:150,sortable:false,align:'right'},
										   							{name:'balAmt',index:'balAmt',width:150,sortable:false,align:'right'}
														   	],
														   	scrollOffset: 0, 
														    sortname:'id',
														    height: '100%', 
														    sortorder: "desc",
														    autowidth: true,
														    rowNum:10,
															rowList : [10,25,50],
														    viewrecords: true				
													   });
													  // jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
													  jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
													    jQuery("#"+subgrid_id).parent().css("width","100%");
													    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
													    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
													    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
													    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
												},	
												 onSelectRow: function(id){ 
													
													  $("#farmerIdSeason").val(id);
											          document.detailform.submit();      
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

							$("#exportPdf").click(function() {
								if (isDataAvailable("#detail")) {
									jQuery("#detail").setGridParam ({postData: {rows : 0}});
									 jQuery("#detail").jqGrid('excelExport', {url: populateAction});
								} else {
									alert('<s:text name="export.nodata"/>');
								}
							});
							$("#exportXls").click(function() {
								if (isDataAvailable("#detail")) {
									jQuery("#detail").setGridParam ({postData: {rows : 0}});
								
									var url="farmerBalanceReport_populateXLS.action"+"?type=<s:property value="type"/>";
									 jQuery("#detail").jqGrid('excelExport', {url: url});
								} else {
									alert('<s:text name="export.nodata"/>');
								}
							});
							function reloadGrid() {
									
							
								
								
									
								jQuery("#detail").jqGrid('setGridParam',{url:url,page:1})
									.trigger('reloadGrid');				
							
								

								//	}
							}

							function clear() {
								
								document.form.submit();
							}

							jQuery("#clear").click(function() {
								clear();
							});
							function exportPdf() {
								document.form.action = "farmerBalanceReport_populatePDF.action";
								document.form.submit();
								document.form.target = "";
							}
							/* function exportXls() {
								document.form.action = "farmerBalanceReport_populateXLS.action";
								document.form.submit();
								document.form.target = "";
							} */

							/* jQuery("#minus")
									.click(
											function() {
												if (document
														.getElementById(document
																.getElementById("fieldl").selectedIndex) != null
														&& (document
																.getElementById(document
																		.getElementById("fieldl").selectedIndex).className == "" || document
																.getElementById(document
																		.getElementById("fieldl").selectedIndex).className == " ")) {
													removeFields();
													var divOption = document
															.getElementById("fieldl");
													if (divOption != null) {
														if (divOption.selectedIndex == 1) {
															//document.getElementById("startDate").value = document.getElementById("hiddenStartDate").value;
															//document.getElementById("endDate").value = document.getElementById("hiddenEndDate").value;
														}
													}
													divOption.selectedIndex = 0;
													reloadGrid();
												}
											}); */
							

						});

		function exportXLS() {
			var url="farmerBalanceReport_populateXLS.action"+"?type=<s:property value="type"/>";
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
					url : url
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}
	</script>
	<div id="divs" align="right">
</div>


<div class="sitemesh-container">
	<s:form name="form" action="farmerIndividualBalanceReport_list">
		<div class="container-fluid">
			<div class="row">
				<div class="form-inline pull-right">
					<s:hidden id="type" name="type" />
						<div class="form-group">
							<s:text name="filter" />
						</div>
						<div class="form-group">
							<s:select name="fieldl" id="fieldl" list="fields"
								class="form-control" headerKey=""
								headerValue="%{getText('txt.select')}" theme="simple" />
						</div>
					<div class="form-group">
						<button type="button" class="btn btn-success" aria-hidden="true"
							id="plus">
							<b class="fa fa-plus"></b>
						</button>
					</div>
					<div class="form-group">
						<button type="button" class="btn btn-danger" aria-hidden="true"
							id="minus">
							<b class="fa fa-minus"></b>
						</button>
					</div>

				</div>
				
			</div>
		</div>
		<div class="form-inline" id="filter-fields">

				<div class="well well-sm" id="wellId">
					<div class="form-inline">
					<div >
						<%-- <label for="filter.balanceType"><s:text name="balanceType" /></label>
						<s:select name="filter.balanceType" id="balanceTypeId"
													list="balanceTypeList" headerKey="" headerValue="%{getText('txt.select')}"
													cssClass="form-control input-sm" cssStyle="width:150px"/> --%>
					</div>
						<div id="searchbtn"> </div>
						<div class="left-space">
							<button type="button" class="btn btn-primary" aria-hidden="true"
								id="generate" title='<s:text name="Search" />'>
								<b class="fa fa-search"></b>
							</button>

							<button type="button" class="btn btn-warning" aria-hidden="true"
								id="clear" title='<s:text name="Clear" />'>
								<b class="fa fa-times"></b>
							</button>
						</div>
					</div>
					<div class="1 form-group hide top-space">
						<label for="season"><s:text name="season" /></label>
						<s:select name="filter.farmerId" id="seasonId"
														list="seasonList" headerKey="" headerValue="%{getText('txt.select')}"
														cssClass="form-control input-sm" cssStyle="width:150px"/>
					</div>
					
					
						<div class="2 form-group hide top-space">
							<label for="filtedr.farmer"><s:text name="farmer" /></label>
							<s:select name="filter.farmerId" id="farmerId"
														list="farmersList" headerKey="" headerValue="%{getText('txt.select')}"
														cssClass="form-control input-sm" cssStyle="width:150px"/>
						</div>
				
					
					
				</div>


			</div>
			</div>
	</s:form>

<div ></div>
		
		</div>





	<br />
	
	<br />

	<div style="width: 98%;padding-top: 30px" id="baseDiv">
		<table id="detail"></table>

		<div id="pagerForDetail"></div>
		<div id="pager_id"></div>
	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
<s:form name="detailform" action="farmerIndividualBalanceReport_detail">
	<s:hidden name="farmeridSeasonCode" id="farmerIdSeason"/>
	<s:hidden name="currentPage" />
</s:form>
</body>
