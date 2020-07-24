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
		url:'agentBalance_data.action?warehouseId='+warehouseId+"&txnType="+document.getElementById('txnType1').selectedIndex,
	   	pager: '#pagerForDetail',
	   	mtype: 'POST',	  
	   	datatype: "json",	
	   	colNames:[				   	       	  
					 '<s:property value="%{getLocaleProperty('agentId')}"/>	',
 					 '<s:property value="%{getLocaleProperty('agentName')}"/>	',
  					'<s:text name="balance"/>',
 					 '<s:text name="distributionBalance"/>'<sec:authorize ifAllGranted="service.agentBalance.update">,
 					 '<s:text name="editCash"/>'</sec:authorize>
	      	 ],
	   	colModel:[						
			
	   			{name:'agentId',index:'agentId',width:125,sortable:true},
		   		{name:'agentName',index:'agentName',width:125,sortable:true},
			   	{name:'balance',index:'balance',width:125,sortable:false,search:false,align:"right"},
			   	{name:'distributionBalance',index:'distributionBalance',width:125,sortable:false,search:false,align:"right"}<sec:authorize ifAllGranted="service.agentBalance.update">,
		   		{name:'edit',index:'edit',width:125,sortable:false,search:false, align:'center'}</sec:authorize>
	   	],
	   	height: 301, 
	    width: $("#baseDiv").width(), // assign parent div width
	    scrollOffset: 0,
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
function editAgentBalance(isDeposit)
{	
	var existBalance = document.getElementById("existBalance").innerHTML;
	var hit = true;
	var balanceR = document.getElementById("balanceR").value;
	var balanceP = document.getElementById("balanceP").value;
	if((balanceR == "" || balanceR == null) && (balanceP == "" || balanceP == null)){
		document.getElementById('pendingPopupErrMsg').innerHTML='<s:text name="cashbalanceAmt" />';
		hit = false;
	}else if((balanceR != "" && balanceR != null) && (isNaN(balanceR)===true || parseFloat(balanceR) < 0 ||isValidValue(balanceR,/^[0-9]+$/) == false ) ){
		document.getElementById('pendingPopupErrMsg').innerHTML='<s:text name="invalidBalance" />';
		hit = false;
	}else if(balanceP != "" && balanceP != null && (isNaN(balanceP) === true || parseFloat(balanceP) <0 || isValidValue(balanceP,/^[0-9]+$/) == false)){
		document.getElementById('pendingPopupErrMsg').innerHTML='<s:text name="invalidBalance" />';
		hit = false;
	}else if(((balanceP == "" || balanceP == null) && parseFloat(balanceR) == 0) || ((balanceR == "" || balanceR == null) && parseFloat(balanceP) ==0) ||
			(parseFloat(balanceP) == 0 && parseFloat(balanceR) ==0)){
		document.getElementById('pendingPopupErrMsg').innerHTML='<s:text name="invalidBalance" />';
		hit = false;
	}
	var balance = balanceR +"."+balanceP;
	if(hit && (isDeposit == false)){
		if(parseFloat(balance)>parseFloat(existBalance)){
			document.getElementById("pendingPopupErrMsg").innerHTML='<s:text name="insufficientBalanceCash"/>';
			hit = false;
		}
	}
	if(hit){
		jQuery.post("agentBalance_editResubmitKey",{dt:new Date()},function(data){
			if("1" == data){
				
				document.updateform.id.value=document.getElementById("idd").innerHTML;
				document.updateform.balance.value=balance;
				document.updateform.deposit.value = isDeposit;
				document.updateform.txnType.value=document.getElementById('txnType1').selectedIndex;
				document.updateform.action="agentBalance_editAgentBalance.action?agentId="+$('#agentId').html().trim();
				disablePopUp();
				document.updateform.submit();
				
 			}else if(data.indexOf("j_username")!= -1){
	 	 	 	cancelForm();
	 	 	}else{
	 	 		jQuery("#validateError").html(data);
		 	}
		});
	
	}
}

function isValidValue(val,numberReg){
	 var result = true;
	 var regExVal = val.match(numberReg);
	 if(regExVal=='' || regExVal==null || regExVal== undefined ){
	  result = false;
	 }
	 return result;
}
function enablePopUp(id,existBalance,agentId){	
	document.getElementById("enableModal").click();
	document.getElementById("idd").innerHTML=id;
	document.getElementById("agentId").innerHTML=agentId;
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

	txnType = document.getElementById('txnType1').selectedIndex;
	if(txnType == 1){
		$("#addBtn_AgentCashFlow").hide();
		jQuery("#detail").jqGrid('hideCol',distNames);
		jQuery("#detail").jqGrid('showCol',procNames);
		
	}else{
		$("#addBtn_AgentCashFlow").show();
		jQuery("#detail").jqGrid('hideCol',procNames);
		jQuery("#detail").jqGrid('showCol',distNames);
	}
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
</script>

	
	<table class="hide" width="40%" cellspacing="0" cellpadding="3">
		<tr class="odd">
 	 	<td><s:text name="agrotype"/></td>
			<td><s:select id="txnType1" name="txnType" list="txnTypeList" listKey="key" listValue="value" 
				theme="simple" onchange="loadGrid()"/><sup style="color: red;">*</sup></td>
		</tr>
		
	</table>
	<div class="appContentWrapper marginBottom">
	
<div style="width: 100%;" id="baseDiv">
	<table id="detail"></table>
	<div id="pagerForDetail"></div>
</div>
</div>
<s:form name="updateform" >
	<s:hidden name="id"/>
	<s:hidden name="balance"/>
	<s:hidden name="deposit"/>
	<s:hidden name="txnType"/>
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
			<div id ="idd" style="display: none"></div>
			<div id ="existBalance" style="display: none"></div><br/>
			<div id ="agentId" style="display: none"></div><br/>
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
  
<%--
<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>
 --%>
</body>