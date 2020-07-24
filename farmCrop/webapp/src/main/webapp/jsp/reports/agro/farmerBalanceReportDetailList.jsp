<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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

var sDate = '';
var eDate = '';

jQuery(document).ready(function(){
	$(".breadCrumbNavigation").html('');
	$(".breadCrumbNavigation").append("<li><a href='#'>Report</a></li>");
	$(".breadCrumbNavigation").append("<li><a href='farmerBalanceReport_detailList.action?type=farmer'> Balance Sheet | Farmer Balance Report</a></li>");
	
	$("#daterangeBal").daterangepicker(
			 {
			     
				 format: 'YYYY/MM/DD',
			      startDate:  moment( document.getElementById("hiddenStartDate").value,  "YYYY-MM-DD" ),
			     endDate: moment( document.getElementById("hiddenEndDate").value,  "YYYY-MM-DD" )
			 });
	
	

	 $('.applyBtn').click();
	
/* 	$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "YYYY-MM-DD" );
    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "YYYY-MM-DD" );
    $("#daterange").data().daterangepicker.updateView();
    $("#daterange").data().daterangepicker.updateCalendars();
	$('.applyBtn').click(); */
	
	

	
	jQuery("#generate").click(function() {
		
		reloadGrid();
	});
	
	jQuery("#clear").click( function() {
		clear();
	});
	
	function reloadGrid() {
		var d1=	jQuery('#daterangeBal').val();
		var d2= d1.split("-");
	  
		var value1= d2[0];
		
		var value2= d2[1];
		
		document.receiptForm.hiddenStartDate.value=value1.trim();
		document.receiptForm.hiddenEndDate.value=value2.trim();
		document.receiptForm.submit();
		
 	
		//jQuery("#detail").jqGrid('setGridParam',{url:url,page:1}).trigger('reloadGrid');	
	}
	
	function clear(){
		resetReportFilter();
		document.form.submit();
		
	}
});

function loadGrid(){
	jQuery("#detail")
	.jqGrid(
			{
				url : 'farmerBalanceReport_detailList.action',
				pager : '#pagerForDetail',
				datatype : "json",
				mtype : 'POST',
				postData : {
					"startDate" : function(){			  
						return  document.getElementById("hiddenStartDate").value;
					  },
					  "endDate" : function(){			  
						return document.getElementById("hiddenEndDate").value;
					  },
				}
			});
	}
</script>


<s:hidden key="filter.id" id="agroTxnId" />
<font color="red"> <s:actionerror /></font>


<s:form name="form" action="farmerBalanceReport_detail">
	<div class="appContentWrapper marginBottom">
			
			<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:text name="date" /></label>
					<div class="form-element">
						<input id="daterangeBal" 
											class="form-control input-sm "  />
					</div>
				</div>
				
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
<s:form name="form" cssClass="fillform">
	<s:hidden key="currentPage" />
	<s:hidden key="id" />
	<s:hidden key="filter.id" />
	<s:hidden key="command" />
	<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							 <h2><s:property value="%{getLocaleProperty('info.balanceDetail')}" /><div class="pull-right"> <s:property value="farmerName" /></div></h2>
							
							 
							 
						</div>
					</div>
				</div>
			</div>
		</div>
<div class="appContentWrapper marginBottom">
	<table class="table table-bordered aspect-detail">
		
			<tr class="odd">
				<th><s:text name="txnTimeGrid" /> (<s:property
						value="%{getGeneralDateFormat().toUpperCase().concat(' HH:MM:SS')}" /></th>
				<th><s:property value="%{getLocaleProperty('txnDesc')}" /></th>
				<th><s:text name="receiptNoGrid" /></th>
				<th><s:text name="initialBalanceGrid" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
				<th><s:text name="txnAmountGrid" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
				<th><s:text name="balanceAmountGrid" />(<s:property value="%{getCurrencyType().toUpperCase()}" />)</th>
				<%-- <th><s:text name="creditAmt" /></th>
				<th><s:text name="finalBalance" /></th> --%>
			</tr>
			</br>
			<s:iterator value="agroTransactions" status="rowstatus">
				<tr class="odd">
					<td><s:property value="txnTime" /></td>
					<td><s:property value="txnDesc" /></td>
					<td><s:property value="receiptNo" /></td>
					<td><s:property value="tempIntBalance" /></td>
					<td><s:property value="temptxnAmount" /></td>
					<td><s:property value="tempBalAmt" /></td>
					<%-- <s:if test="txnType=='314'">
					  <s:if test="txnDesc!='DISTRIBUTION PAYMENT AMOUNT'">
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="item" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="quantity" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="subTotal" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td></td>
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="distributionDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="individulalFinalBal" /></td>
									</tr>
								</s:iterator>

							</table></td>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="finalBalance" /></td>
					
					</s:else>
						</s:if>
						<s:if test="txnType=='316'">
					 <s:if test="txnDesc!='PROCUREMENT PAYMENT'">
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="item" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="numberOfBags" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="subTotal" /></td>
									</tr>
								</s:iterator>

							</table></td>

						<td></td>
						<td><table class="table table-bordered aspect-detail">
								<s:iterator value="procurementDetailList">
									<tr class="odd" style="width: 5px">
										<td><s:property value="individulalFinalBal" /></td>
									</tr>
								</s:iterator>

							</table></td>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="finalBalance" /></td>
					
					</s:else>
					</s:if>
			<s:if test="txnType=='334'">
					   <td></td>
						<td></td>
						<td></td>
					     <td><s:property value="txnAmount" /></td>
						 <td><s:property value="balAmount" /></td>
					</s:if> --%>
				</tr>
			</s:iterator>
		</table>

	
	</div>
	<br />

</s:form>

<s:form action="farmerBalanceReport_detailList" id="receiptForm"
	method="POST" >
	<s:hidden name="farmeridSeasonCode" id="farmerIdSeason"/>
	<s:hidden name="currentPage" />
		<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	<s:hidden name="id" />
	</s:form>
	
</body>