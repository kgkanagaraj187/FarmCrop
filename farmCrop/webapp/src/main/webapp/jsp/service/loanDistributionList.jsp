<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
	<META name="decorator" content="swithlayout">
	<style type="text/css">
		.disableBtn {
    border-color:#ccc!important;
    border-style:none!important;
    border-width:0 0px!important;
    cursor:default!important;
    border:none!important;
     background:url(../images/agro-theme/editBtn.png) no-repeat 6px 4px;  
    background-position:-258px 4px;
    background-repeat:no-repeat;
    background-color:#eff8d7!important;
    margin:-3px 0!important;
}
.disableBtn:hover {
    background-color:#eff8d7!important;
}
.disableBtn font {
    color:#567304!important;
}
.pageTitle{
display:none;
}
	</style>
</head>
<body>
<font color="red"> <s:actionerror /> <s:fielderror /></font>
<script type="text/javascript">
var isDeposit = true;
var procNames = 'distributionBalance';
var distNames = 'balance';


$(document).ready(function(){		
	//startTimer("agentBalance");
	var warehouseId = '<s:property value="warehouseId"/>';
	jQuery("#detail").jqGrid(
	{
		url:'loanDistribution_data.action',
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',	  
	   	datatype: "json",	
	   	colNames:[				   	       	  
					 '<s:property value="%{getLocaleProperty('Loan Date')}"/>	',
 					 '<s:property value="%{getLocaleProperty('AccountNo')}"/>	',
 					'<s:text name="Vendor"/>',
  					'<s:text name="Borrower Given Name"/>',
 					// '<s:text name="City/Town"/>',
 					'<s:text name="Loan Status"/>'
 					//'<s:text name="detail"/>'
	      	 ],
	   	colModel:[						
			
	   			{name:'loanDate',index:'loanDate',width:125,search:false,sortable:true},
		   		{name:'accountNo',index:'accountNo',width:125,search:false,sortable:true},
		   		{name:'vendor',index:'vendor',width:125,sortable:false,search:false},
			   	{name:'farmerName',index:'farmerName',width:125,sortable:false,search:false},
			  // 	{name:'village',index:'village',width:125,sortable:false,search:false},
			   	{name: 'loanStatus', index: 'loanStatus',sortable: false,search: false}
			   	//{name: 'detail', index: 'detail',sortable: false,search: false}
	   	],
	   	height: 301, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
	   	rowNum:10,
	   	rowList : [10,25,50],
	    sortname:'id',			  
	    sortorder: "desc",
	    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
	    onSelectRow: function(id){ 
			  document.detailform.id.value  =id;
	          document.detailform.submit();      
			},
        onSortCol: function (index, idxcol, sortorder) {
	        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
        }
    });
	
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
	loadColumnLimit();
	jQuery("#detail").setGridWidth($("#baseDiv").width());
	jQuery('#detail').jqGrid('setGridHeight',(windowHeight));
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
	
});	     


function isValidValue(val,numberReg){
	 var result = true;
	 var regExVal = val.match(numberReg);
	 if(regExVal=='' || regExVal==null || regExVal== undefined ){
	  result = false;
	 }
	 return result;
}
function enablePopUp(id,existBalance){	
	document.getElementById("enableModal").click();
	document.getElementById("id").innerHTML=id;
	document.getElementById("existBalance").innerHTML=existBalance;
}

function disablePopUp(){
	$('#pendingPopupErrMsg').html('');
	$('#popupBackground').hide();
	$('#productPopUp').hide();
	$('body').css('overflow','');
	resetPopUp();
	}
 function resetPopUp(){
	//To Clear the Pop Up
	document.getElementById("balanceR").value ="";
	document.getElementById("balanceP").value="";
}
 function loadColumnLimit(){

/* 	txnType = document.getElementById('txnType1').selectedIndex;
	if(txnType == 1){
		$("#addBtn_AgentCashFlow").hide();
		jQuery("#detail").jqGrid('hideCol',distNames);
		jQuery("#detail").jqGrid('showCol',procNames);
		
	}else{
		$("#addBtn_AgentCashFlow").show();
		jQuery("#detail").jqGrid('hideCol',procNames);
		jQuery("#detail").jqGrid('showCol',distNames);
	} */
	resetPopUp(); 
	/* To dynamically set width of the grid. */
	 jQuery("#detail").setGridWidth($("#baseDiv").width());
}  
 function loadGrid(){
	loadColumnLimit();
	var warehouseId = '<s:property value="warehouseId"/>';
	txnType = document.getElementById('txnType1').selectedIndex;
	jQuery("#detail").jqGrid('setGridParam',{url:"agentBalance_data.action?warehouseId="+warehouseId+"&txnType="+txnType,page:1}).trigger('reloadGrid');	
	
} 

 function popupFarmerData(val) {
 	try{
 		var jsonFarmerData = "";
			var str_array = val.split('~');
			
			
			if(str_array[1]==1||str_array[1]==2){
				jQuery("#accept").hide();
				jQuery("#reject").hide();
			}
			
			$("#mDatabody").empty();
			//alert(str_array);
			document.getElementById('temp').value=str_array[0];
			document.getElementById('pCode').value=str_array[2];
			var mDatabody="";
				$.ajax({
					 type: "POST",
			        async: false,
			        url: "loanDistribution_populateFarmerData.action",
			        data: {id : str_array[0]},
			        success: function(result) {
			        	var jsonData = $.parseJSON(result);
			        	console.log(jsonData);
			        	$.each(jsonData, function(index, value) {
			        		var tr = $("<tr/>");
			        		var td = $("<td/>");
				       	 	td.text(value.category);
			        		var td1 = $("<td/>");
				       	 	td1.text(value.product);
				       		var td2 = $("<td/>");
				       	 	td2.text(value.quantity);
				       		var td3 = $("<td/>");
				       		td3.text(value.rate);
				       		//document.getElementById('pCode').value=value.productCode;
				       		tr.append(td);
				       		tr.append(td1);
				       		tr.append(td2);
				       		tr.append(td3);
				       	 $("#mDatabody").append(tr);
			        	});
			        	
			        	
			        }
			       
				});
			document.getElementById("enableDataModal").click();	
		}
		catch(e){
			alert(e);
			}
	
	}
 function buttonDataCancel(){
	 location.reload();
 	document.getElementById("model-close-data-btn").click();
 	document.getElementById("detail-close-data-btn").click();
 	
 }
 function buttonLoanApprove(val){

		var temp=document.getElementById("temp").value;
var pCode=document.getElementById("pCode").value;

	 $.ajax({
		 type: "POST",
        async: false,
        url: "loanDistribution_updateLoanStatus.action",
        data: {id : temp,LStatus:val,productCode:pCode},
        success: function(result) {
        	 location.reload();
        	document.getElementById("model-close-data-btn").click();
    	 	document.getElementById("detail-close-data-btn").click();
        }
       
	});
	 	
	 }
</script>

	
	<%-- <table class="hide" width="40%" cellspacing="0" cellpadding="3">
		<tr class="odd">
 	 	<td><s:text name="agrotype"/></td>
			<td><s:select id="txnType1" name="txnType" list="txnTypeList" listKey="key" listValue="value" 
				theme="simple" onchange="loadGrid()"/><sup style="color: red;">*</sup></td>
		</tr>
		
	</table> --%>
	<div class="appContentWrapper marginBottom">
	<div class="grid-controll">
				<sec:authorize ifAllGranted="profile.farmer.create">
					<input type="BUTTON" id="add"
						value="<s:text name="create.button"/>"
						onclick="document.createform.submit()" class="btn btn-sts" />
				</sec:authorize>

			<%-- 	<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts" type="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
						<li role="presentation"><a href="#" onclick="exportXLS()"
							tabindex="-1" role="menuitem"> <s:text name="excel" />
						</a></li>
						
						 <s:if test='getCurrentTenantId()=="nswitch" && getUsername()=="sts"'>
							<li role="presentation"><a href="#" onclick="populateQrCode()"
							tabindex="-1" role="menuitem"> <s:text name="With Image" />
						</a></li>
						 </s:if>
						
						<s:if test='currentTenantId!="canda" && currentTenantId!="nswitch"'>
						<li role="presentation"><a href="#" tabindex="-1"
							role="menuitem" onClick="exportPDF()"> <s:text name="pdf" />
						</a></li>
						</s:if>
					</ul>
				</div> --%>
			</div>
<div style="width: 100%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="createform" action="loanDistribution_create">
	<s:hidden name="type" class="type"/>
	</s:form>
<s:form name="updateform" >
	<s:hidden name="id"/>
	<s:hidden name="balance"/>
	<s:hidden name="deposit"/>
	<s:hidden name="txnType"/>
	<s:hidden name="temp" id="temp" />
	<s:hidden name="pCode" id="pCode" />
	<s:hidden name="loanStatus" id="loanStatus" />
</s:form>
<s:form name="detailform" action="loanDistribution_detail">
		<s:hidden name="id" />
		<s:hidden name="currentPage" />
	</s:form>
<s:form name="redirectBalanceform">
	<s:hidden key="currentPage" />
</s:form>
<s:hidden id="id" name="id"/>

<button type="button" data-toggle="modal" data-target="#myModal" style="display:none" id="enableModal" data-backdrop="static" data-keyboard="false">Open Modal</button>

  <!-- Modal -->
<div class="modal fade" id="myModal" >
    <div class="modal-dialog modal-sm">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick="disablePopUp()">&times;</button>
          <h4 class="modal-title">  
            <h4 class="modal-title"><s:text name="cashDistTitle"></s:text> </h4></h4>
        </div>
         <div id="pendingPopupErrMsg" align="center" style="color: red"></div>
       <table align="center" style="margin-top:10px; ">
      
	<tr>
		<td>
			<s:text name="cashbalanceAmt"/> <br/> 
		</td>
		<td style="padding-left:10px; ">
			<s:textfield id="balanceR" name="balanceInR" size="-10px" cssStyle="width:60px" maxlength="9"/><s:label value="."></s:label><s:textfield id="balanceP" name="balanceInP" maxlength="2" cssStyle="width:30px"/>
			<div id ="id" style="display: none"></div>
			<div id ="existBalance" style="display: none"></div><br/>
		</td>
	</tr>
	<tr>
	 <td  align="right" colspan="2" style="padding-top:10px;padding-bottom:20px;  ">
	 <!-- <button type="button"  id="detectBtn_AgentCashFlow" class="btn btn-primary btn-sm" onclick="editAgentBalance(false);" >Withdraw</button>
	 <button type="button"  id="addBtn_AgentCashFlow"  class="btn btn-primary btn-sm" onclick="editAgentBalance(true);">Deposit</button>
	 -->		<a href="#" id="detectBtn_AgentCashFlow" class="popBtn" onclick="editAgentBalance(false);"><img src="./img/withdraw.png" title="Withdraw"/></a>
			<a href="#" id="addBtn_AgentCashFlow"  class="popBtn"  onclick="editAgentBalance(true);" style="height:39px"><img src="./img/deposit.png" title="Deposit"/></a>
		</td>
	</tr>
</table> 
      </div>
   
    </div>
  </div>
  <button type="button" id="enableDataModal"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideDataModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
	<div id="slideDataModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-data-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">Loan Details</h4>
				</div>

				<table class="table table-bordered table-responsive">
					<div class="modal-body">
						<thead>
							<tr>
								<!-- <td>Sno</td> -->
								<td>Category</td>
								<td>Product</td>
								<td>Quantity</td>
								<td>Price Per Unit</td>
								
								
							</tr>
						</thead>
						<tbody id="mDatabody">
						</tbody>
					</div>
				</table>

				<div class="modal-footer">
				<%-- <sec:authorize ifAllGranted="service.loanDistributionService.approval">
					<button type="button" id="accept"  class="btn btn-default"
						onclick="buttonLoanApprove(1)">
						<s:text name="Approve" />
					</button>
					<button type="button" id="reject" class="btn btn-default"
						onclick="buttonLoanApprove(2)">
						<s:text name="Reject" />
					</button>
					</sec:authorize> --%>
					<button type="button" class="btn btn-default"
						onclick="buttonDataCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
  
<%--
<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>
 --%>
</body>
