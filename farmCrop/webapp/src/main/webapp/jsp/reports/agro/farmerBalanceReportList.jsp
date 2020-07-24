<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">

<STYLE type="text/css">
.ui-autocomplete {
	background: #D3DEB0;
	padding: 10px;
	border: 1px solid #939585;
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden
}
</STYLE>
<style>
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
		var farmerId = '';
		var agentId = '';
		var balanceType = '1';
		var sDate = '';
		var eDate = '';
		//var seasonCode = '';
		var recordLimit = '<s:property value="exportLimit"/>';
			
		jQuery(document)
				.ready(
						
						
						
						function() {
							var url="farmerBalanceReport_detail.action"+"?type=<s:property value="type"/>";
							onFilterData();
								<s:if test='type=="farmer"'>
							document.getElementById('farmerId').selectedIndex = 0;
							 <s:if test="currentTenantId=='susagri'">
							 document.getElementById('samithi').selectedIndex=0;
							 </s:if>
							</s:if>
							<s:else>
							document.getElementById('agentId').selectedIndex = 0;
							</s:else>
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
													/* "startDate" : function(){			  
														return  document.getElementById("hiddenStartDate").value;
													  },
													  "endDate" : function(){			  
														return document.getElementById("hiddenEndDate").value;
													  }, */
													<s:if test='type=="farmer"'>
													"farmerId" : function() {
														return farmerId;
													},
													 <s:if test="currentTenantId=='susagri'">
													  "samithi" : function(){
														  return  document.getElementById('samithi').value;
														  },
													  </s:if>
												</s:if>
												<s:else>
													"agentId" : function() {
														return agentId;
													},
													
												</s:else>
												/* "seasonCode" : function() {
													 return document.getElementById("seasonId").value;
												}, */
												
												},
												colNames : [
													<s:if test='type=="farmer"'>
													'<s:property value="%{getLocaleProperty('farmerIdGrid')}" />',
														'<s:property value="%{getLocaleProperty('farmerNameGrid')}" />',
														 <s:if test="currentTenantId=='susagri'">
														'<s:property value="%{getLocaleProperty('samithi')}" />',
														</s:if>
													</s:if>
														<s:else>
														'<s:text name="agentIdGrid"/>',
														'<s:text name="agentGrid"/>',
														</s:else>
														 
														'<s:text name="finalBalance"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'
														
														],
														
												colModel : [ 
												<s:if test='type=="farmer"'>      
												{name : 'farmerId',index : 'farmerId',sortable : false},
											</s:if><s:else>
												  {name : 'agentId',index : 'agentId',sortable : false},												
												</s:else>
												{name : 'firstName',index : 'firstName',sortable : false}, 
												<s:if test='type=="farmer"'>   
												 <s:if test="currentTenantId=='susagri'">
													{name : 'samithi',index : 'samithi',sortable : false}, 
												</s:if>
												</s:if>
												{name : 'finalBalance',index : 'finalBalance',sortable : false,align : 'right'}
												],
												height : 380,
												width : $("#baseDiv").width(),
												scrollOffset : 19,
												sortname : 'id',
												sortorder : "desc",
												rowNum : 10,
												shrinkToFit:true,
												rowList : [ 10, 25, 50 ],
												viewrecords : true,
												subGrid: false,
												//subGrid: typeSub,
												   
												 <s:if test='type=="farmer"'>
												onSelectRow: function(id){ 
													//alert(id);
													   $("#farmerIdSeason").val(id);
													  // document.detailform.id.value  =id;
											          //document.detailform.submit();   
											          detailData();

													},
													
													
												 beforeSelectRow: 
												        function(rowid, e) {
												            var iCol = jQuery.jgrid.getCellIndex(e.target);
												            if (iCol ==1){return false; }
												            else{ return true; }
												        },
	
													</s:if>
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
							 jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true})
								//jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});
							colModel = jQuery("#detail").jqGrid('getGridParam','colModel');
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
							$('#detail').jqGrid('setGridHeight',(reportWindowHeight));
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
							
							function onFilterData(){
								callAjaxMethod("farmerBalanceReport_populateAgentList.action","agentId");
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
							var type='<s:property value="type"/>';
							if(type=="farmer"){
								farmerId = document.getElementById("farmerId").value;
							}else{
								agentId= document.getElementById("agentId").value;
							}
										jQuery("#detail").jqGrid('setGridParam',{url:url,page:1})
								.trigger('reloadGrid');		

								//	}
							}
							var printWindowCnt=1;
							var windowRef;
							
						function detailData(){
							
							var targetName="printWindow"+printWindowCnt;
							windowRef = window.open('',targetName);
							try{
								windowRef.referenceWindow = windowRef;		
							}catch(e){
							}
							jQuery("#receiptForm").attr("target",targetName);	
							jQuery("#receiptForm").submit();
							++printWindowCnt;
							
						}

							function clear() {
								//var typeStr=$("#type").val();
								var typeStr='<s:property value="type"/>';
								var url="farmerBalanceReport_list.action?type=";
								url=url.concat(typeStr);
								document.form.action =url;
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
						
							

						});

		function exportXLS() {
			var url="farmerBalanceReport_populateXLS.action"+"?type=<s:property value="type"/>";
			var count = jQuery("#detail").jqGrid('getGridParam', 'records');
	/* 		alert(count);
			if (count > recordLimit) {
				alert('<s:text name="export.limited"/>');
			} else  */if (isDataAvailable("#detail")) {
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
<s:form name="form" action="farmerBalanceReport_list">
	<div class="appContentWrapper marginBottom">
			
			<section class='reportWrap row'>
				<div class="gridly">
				
				<s:if test='type=="agent"'>
					<div class="filterEle">
						<label for="txt"><s:text name="agentGrid" /></label>
						<div class="form-element">
							<s:select name="filter.agentId" id="agentId"
														list="{}" headerKey="" headerValue="%{getText('txt.select')}"
														cssClass="form-control input-sm select2" />
						</div>
					</div>
				</s:if>
				<s:else>
						<div class="filterEle">
							<label for="txt"><s:property value="%{getLocaleProperty('farmer')}" /></label>
								<div class="form-element">
								<s:select name="filter.farmerId" id="farmerId"
														list="farmersList" headerKey="" headerValue="%{getText('txt.select')}"
														cssClass="form-control input-sm select2" />
						</div>
						</div>
						
								<s:if test="currentTenantId=='susagri'">
				<div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('samithi')}" /></label>
					<div class="form-element">
						<s:select name="samithi" id="samithi" list="samithiList"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
					</s:else>
			
					
					<div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
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
			</section>
			
		</div>

</s:form>
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
	

	<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
	</div>	
	
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
	<%-- <s:form name="detailform" action="farmerBalanceReport_farmerDetail">
	<s:hidden name="farmeridSeasonCode" id="farmerIdSeason"/>
	<s:hidden name="type" value="farmer"/>
	<s:hidden name="currentPage" />
	</s:form> --%>
	
	<s:form action="farmerBalanceReport_detailList" id="receiptForm"
	method="POST" target="printWindow">
	<s:hidden name="farmeridSeasonCode" id="farmerIdSeason"/>
	<s:hidden name="currentPage" />
	<s:hidden name="id" />
	</s:form>

	<%-- <s:form name="detailform" action="farmerBalanceReport_detailList">
	<s:hidden name="type" value="farmer"/> 
	<s:hidden name="currentPage" />
	<s:hidden name="id" />
	</s:form>  --%>
	
</body>
