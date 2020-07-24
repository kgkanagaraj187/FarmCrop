<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style type="text/css">
.flexform-item {
    display: inline-flex;
    width: 33.32% !important;
    /* border: solid 1px #ccc; */
    margin-bottom: -1px;
    margin-left: -1px;
}
</style>
</head>
<body>

	<div class="error"><sup>*</sup> <s:text name="reqd.field" />
	<div id="validateError" style="text-align: center; padding: 5px 0 0 0"></div>
	</div>

	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />
		<s:hidden key="id" id="id" />
		<s:hidden key="command" />
		<s:hidden key="selecteddropdwon" id="listname" />
		<s:hidden key="temp" id="temp" />
		<s:hidden name="paymentAmount" ></s:hidden>
			<div class="appContentWrapper marginBottom">

				<div class="formContainerWrapper">
					<h2>
						<s:text name="info.general" />
					</h2>
					<div class="flexiWrapper">
					<div class="flexform-item">
						<label for="txt"><s:text name="paymentMode" />
								<sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedPayment" list="paymentList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="payment" onchange="changeFields(this.value);"/>
						</div>
					</div>
					
					<div class="flexform-item">
								<label for="txt"><s:text name="date" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield name="startDate" id="startDate" readonly="true" cssClass="form-control input-sm" />
						</div>
					</div>
					
					<div class="flexform-item">
						
					</div>
					</div>
						<div class="agentFields">
							<h2>
						<s:text name="info.comOrAgent"/>
					</h2>
					<div class="flexiWrapper">
	
					<div class="flexform-item">
						<label for="txt"><s:text name="info.name"/><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedAgent" list="agentList" headerKey="" headerValue="%{getText('txt.select')}" 
							cssClass="form-control input-sm" id="agent" key="key" onchange="calAgentBal();"/>
						</div>
					</div>
					
					<div class="flexform-item">
						<label for="txt"><s:text name="info.balance"/></label>
						<div class="form-element">
								<s:label id="cashBalId"/>
						</div>
					</div>
					
					<div class="flexform-item">
					</div>
					
					</div>
						
						</div>
				
					
					<h2><s:text name="info.farmer"/></h2>
					<div class="flexiWrapper">
						
						
						<div class="flexform-item hide">
								<label for="txt"> <s:text name="season.name" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedSeason" headerKey="" headerValue="%{getText('txt.select')}" list="seasonList" theme="simple" id="season" disabled="true"/>
						</div>
						</div>
						
						<div class="flexform-item hide">
								<label for="txt"> <s:text name="pageNo" /></label>
						<div class="form-element">
							<s:textfield name="pageNo" theme="simple" id="pageNo" maxlength="8" />
						</div>
						</div>
						
						<div class="flexform-item">
								<label for="txt"><s:property value="%{getLocaleProperty('municipality.name')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedCity" list="cityList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="city" onchange="listVillage(this)" />
						</div>
						</div>
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('village.name')}" /></label>
						<div class="form-element">
							<s:select name="selectedVillage" list="villageList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="village" onchange="listFarmer(this.value)" />
						</div>
						</div>

						
						<div class="flexform-item">
								<label for="txt"> <s:text name="info.name" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedFarmer" list="farmerList" headerKey="" headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value"  cssClass="form-control input-sm" id="farmer" onchange="calFarmerBal()"/>
						</div>
						</div>
						
						<s:if test="enableLoanModule==1">
						
						<div class="flexform-item farmerLoanAmt">
								<label for="txt"><s:property value="%{getLocaleProperty('farmerLoanAmt')}" /></label>
						<div class="form-element">
							<s:label id="farmerLoanAmt"/>
						</div>
						</div>
						
						<div class="flexform-item farmerLoanBal">
								<label for="txt"><s:property value="%{getLocaleProperty('farmerLoanBal')}" /></label>
						<div class="form-element">
							<s:label id="farmerLoanBal"/>
						</div>
						</div>
						
						</s:if>
						<div class="flexform-item">
								<label for="txt"><s:text name="info.balance"/></label>
						<div class="form-element">
							<s:label id="cashBalIdFarmer"/>
						</div>
						</div>
						
					
						
						
						<div class="flexform-item">
								<label for="txt"><s:text name="amount"/><sup style="color: red;">*</sup></label>
					 	<div class="form-element">
							<s:textfield id="balanceR" name="balanceInR" cssStyle="width:110px" maxlength="6" onkeypress="return isNumber(event)"/>
		<div>.</div>
		<s:textfield id="balanceP" name="balanceInP" maxlength="2"
											onkeypress="return isNumber(event,this)"
											cssStyle="text-align:right;padding-right:1px; width:60px!important;" />
		
						</div>
						</div>
						
						<div class="flexform-item">
						<label for="txt"><s:text name="remarks" /></label>
						<div class="form-element">
								<s:textarea name="remarks" id="remarks" ></s:textarea>
							</div>
					</div>

					</div>

					<div class="yui-skin-sam" id="savebutton">
						<span class="yui-button"><span class="first-child">
						<button  id="saveBtn" type="button" class="save-btn btn btn-success" onclick="onSave();">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b></font>
						</button>
						</span></span>
						<span class="yui-button"><span class="first-child"><a id="cancelbutton" onclick="resetVal();" 
						class="cancel-btn btn btn-sts" type="button"><font
						color="#FFFFFF"> <s:text name="cancel.button" /> </font> </a></span></span>
					</div>

				</div>
			</div>


	</s:form>
	<script type="text/javascript">
	var enableLoanModule = "<s:property value='enableLoanModule'/>";
	var receiptNo = null;
	jQuery(document).ready(function(){
		//startTimer("payment");
		$(".farmerLoanAmt").addClass("hide");
		$(".farmerLoanBal").addClass("hide");
		receiptNo = '<s:property value="receiptNo"/>'
		if(receiptNo == null || receiptNo == ""){
			
		}else{
			var newRow = "<tr><td style='padding-left: 110px;'><s:text name="receiptNo" /></td><td>"+receiptNo+"</td><td></td></tr>";
			newRow+="<tr><td colspan='2' style='text-align:right'><a href=\"javascript:printReceipt(\'<s:property value="receiptNo"/>\')\"><s:text name="printReceipt"/></a></td></tr>";
			$('#restartAlert').prepend(newRow);
			jQuery("#restartAlert").css('height','130px'); 
		}
		$('body').append('<div id="popupBackground1" class="popupBackground"></div>');		
	});

	function changeFields(val){
		
		if(val=='Loan Repayment'){
			document.getElementById("farmerLoanAmt").innerHTML="0";
			document.getElementById("farmerLoanBal").innerHTML="0";
			document.getElementById("cashBalIdFarmer").innerHTML="0";
			$(".agentFields").addClass("hide");
			$(".farmerLoanAmt").removeClass("hide");
			$(".farmerLoanBal").removeClass("hide");
		}else{
			document.getElementById("farmerLoanAmt").innerHTML="0";
			document.getElementById("farmerLoanBal").innerHTML="0";
			document.getElementById("cashBalIdFarmer").innerHTML="0";
			$(".agentFields").removeClass("hide");
			$(".farmerLoanAmt").addClass("hide");
			$(".farmerLoanBal").addClass("hide");
		}
	}
	
	
	var printWindowCnt=0;
	var windowRef;
	function printReceipt(receiptNo){	
		jQuery("#receiptNo").val(receiptNo);	
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

	function listVillage(call){	
		/* var callback = {
	    success: function (oResponse) 	{
			var result=oResponse.responseText;
			var arry =populateValues(result);
			document.form.selectedVillage.length = 0;
			addOption(document.form.selectedVillage, "Select", "");
			for (var i=0; i < arry.length;i++){
				if(arry[i]!="")
				addOption(document.form.selectedVillage, arry[i], arry[i]);
				}
			listFarmer(document.getElementsByName('selectedFarmer')[0]);
			}
	    } 
	   	var data = "selectedCity="+call.value;	 
		var url='payment_populateVillage';*/	  
		jQuery.post("payment_populateVillage.action",{id:call.value,dt:new Date(),selectedCity:call.value},function(result){
			insertOptions("village",$.parseJSON(result));
			//listFarmer();
		});	
	}

	$( "#startDate" ).datepicker(

			{
			
		  // format: 'mm/dd/yyyy',
          endDate: '+0d',
           autoclose: true,
			beforeShow : function()
			{
			jQuery( this ).datepicker({ maxDate: 0 });
			},
			changeMonth: true,
			changeYear: true

			}

			);

	function listFarmer(call){	 
	    var val = call;
	   // alert(val);
	   var selectedPayment = $('#payment').val();
	   if(!isEmpty(selectedPayment) && enableLoanModule=='1' && selectedPayment=='Loan Repayment'){
		   jQuery.post("payment_populateFarmerWithLoan.action",{id:val,dt:new Date(),selectedVillage:val},function(result){
				insertOptions("farmer",$.parseJSON(result));		
			});	
		   
	   }else{
		   jQuery.post("payment_populateFarmer.action",{id:val,dt:new Date(),selectedVillage:val},function(result){
				insertOptions("farmer",$.parseJSON(result));		
			});	
	   }
	    
	}
	function onSave(){
		var hit= true;
		var errors = " ";
		var selectedArea = document.getElementById('city').value;
		var selectedVillage = document.getElementById('village').value;
		var selectedFarmer = document.getElementById('farmer').value;
		var selectedPayment = document.getElementById('payment').value;
		var selAgent = document.getElementById('agent').value;
		//var serialNumber = document.getElementById('serialNumber').value;
		var paymentDate = document.getElementById('startDate').value;
		var balanceR = document.getElementById("balanceR").value;
		var balanceP = document.getElementById("balanceP").value;
		var paymentAmount = balanceR +"."+balanceP;
		if(enableLoanModule=='1'){
			var farmerLoanAmt=document.getElementById("farmerLoanAmt").value;
			var farmerLoanBal=document.getElementById("farmerLoanBal").textContent;
		}
		
		if(selectedPayment == ""){
			errors ='<s:text name="selectPayment"/>';
			hit=false;
		}
	 if(paymentDate == "" || paymentDate == null){
			errors ='<s:text name="enterPaymentDate"/>';
			hit=false;
		}	
		 if(selectedPayment!="Loan Repayment" && selAgent =="" ||selAgent == null){
			errors ='<s:text name="selAgent"/>';
			hit=false;
		}
		 if(selectedArea == ""){
			errors ='<s:text name="selectArea"/>';
			hit=false;
		}
		 if(selectedVillage == ""){
			errors ='<s:text name="selectVillage"/>';
			hit=false;
		}
		 if(selectedFarmer == "" || selectedFarmer =='0'){
			errors ='<s:text name="selectFarmer"/>';
			hit=false;
		} 
		 
		 if((balanceR == "" || balanceR == null) && (balanceP == "" || balanceP == null)){
			errors='<s:text name="enterPaymentAmount" />';
			hit = false;
		} if((balanceR != "" && balanceR != null) && (isNaN(balanceR)===true || parseFloat(balanceR) < 0 ||isValidValue(balanceR,/^[0-9]+$/) == false ) ){
			errors='<s:text name="enterValidPaymentAmt" />';
			hit = false;
		} if(balanceP != "" && balanceP != null && (isNaN(balanceP) === true || parseFloat(balanceP) <0 || isValidValue(balanceP,/^[0-9]+$/) == false)){
			errors='<s:text name="enterValidPaymentAmt" />';
			hit = false;
		} if(((balanceP == "" || balanceP == null) && parseFloat(balanceR) == 0) || ((balanceR == "" || balanceR == null) && parseFloat(balanceP) ==0) ||
				(parseFloat(balanceP) == 0 && parseFloat(balanceR) ==0)){
			errors='<s:text name="enterValidPaymentAmt" />';
			hit = false;
		}
		
		if(hit){
			
		if(enableLoanModule=='1' && selectedPayment=='Loan Repayment'){
				
				$.post("payment_populateFarmerLoanAccount.action",{selectedFarmer:selectedFarmer},function(data){
				
				 	if(data == ""){
				 		
				 	processPayment();
			        	
					}
				
						});
			}else{
				$.post("payment_populateAccount.action",{dt:new Date(),agentId:$("#agent").val(),selectedFarmer:document.getElementById('farmer').value,paymentAmount:paymentAmount},function(data){
					$("#saveBtn").prop("disabled",true);
				 	if(data == null || data == ""){
				 		jQuery("#season").attr('disabled','');
				 		document.form.paymentAmount.value=paymentAmount;
				 		document.form.action = "payment_create.action?agentId="+$("#agent").val();
				 		document.form.submit();
					}else{
						errors= data
						jQuery("#validateError").html(errors);
					}
						});
			}
			
			
			
			}else{
				jQuery("#validateError").html(errors);
			}
	}
	function processPayment(){
		var selectedFarmer=$('#farmer').val();		
		var balanceR = $("#balanceR").val();
		var balanceP = $("#balanceP").val();
		var paymentAmount = balanceR +"."+balanceP;
		var startDate = $("#startDate").val();
		var remarks= $("#remarks").val();
		//alert(paymentAmount);
		var dataa = {
				selectedFarmer :selectedFarmer,
 				paymentAmount:paymentAmount,
 				startDate:startDate,
 				remarks:remarks
 		}
	$.ajax({
        url: 'payment_populateLoanRepayment.action',
        type: 'post',
        dataType: 'json',
        data: dataa,
        success: function(data) {
        	//document.getElementById("enableModal").click();	
        	window.location.href="payment_create.action";
        }
       
    }); 
	}
	function isValidValue(val,numberReg){
		 var result = true;
		 var regExVal = val.match(numberReg);
		 if(regExVal=='' || regExVal==null || regExVal== undefined ){
		  result = false;
		 }
		 return result;
	}

	/* function findBalance(call){	
		if(call.value == null || call.value == "" || call.value == undefined){
			$("#balance").text("");
			$("#credit").text("");
		}else{
			var selectedPayment = jQuery.trim(jQuery("#payment").val());
			$.post("payment_populateFarmerBalance.action",{dt:new Date(),selectedFarmer:call.value,selectedPayment:selectedPayment},function(data){	
				if(data == null || data == "" || data == undefined || data == 0){
					$("#balance").text(0);
					$("#credit").text("");
				}else{
					var bal = data.split(',');
					$("#balance").text(bal[0]);
					$("#credit").text(bal[1]);
				}
			});  
		} 
	} */

	function enableConfirmAlert(){
		$('body').css('overflow','hidden');
		$('#popupBackground1').css('width','100%');
		$('#popupBackground1').css('height',getWindowHeight());
		$('#popupBackground1').css('top','0');
		$('#popupBackground1').css('left','0');
		$('#popupBackground1').show();
		$('#confirmAlert').css({top:'50%',left:'50%',margin:'-'+($('#extendAlert').height() / 2)+'px 0 0 -'+($('#extendAlert').width() / 2)+'px'});
		$('#confirmAlert').show();
	}
	function disableConfirmAlert(){
		$('#popupBackground1').hide();
		$('#confirmAlert').hide();
		$('body').css('overflow','');
	}

	function submitPaymentForm(){	
		//jQuery("#season").attr('disabled','');	
		//document.updateform.paymentBalance.value=balance;
		document.form.action = "payment_create.action";
		document.form.submit();
	}

	/* function loadPaymentInformation(call){
		showRemarks(call);
		findBalance(document.getElementById("farmer"));
		var selectedPaymentMode = jQuery.trim(call.value);
		jQuery("#agentProcurementBalance").hide();
		jQuery("#agentDistributionBalance").hide();		
		if(selectedPaymentMode!=""){
			if(selectedPaymentMode=="Distribution Advance"){
				jQuery("#agentDistributionBalance").show();
			}else{
				jQuery("#agentProcurementBalance").show();
			}
		}		
	} */

	/* function showRemarks(call){
		if(call.value == "Others"){
			$('#note').show();
		}else{
			$('#note').hide();
		}
	}
 */
	function insertOptions(ctrlName, jsonArr){
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), "Select", "");
		for(var i=0;i<jsonArr.length;i++){
			addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
		}
	}
	function addOption(selectbox,text,value )
	{
	var optn = document.createElement("OPTION");
	optn.text = text;
	optn.value = value;
	selectbox.options.add(optn);
	}
	
	
	function calAgentBal(){
		var seleAgent = $("#agent").val();
		var cashBalValue;
		var creditBalValue;
		
		$.post("payment_populateAgentAccBalance",{seleAgent:seleAgent},function(data){
			if(data!=null && data!=""){
				//var dataArr = data.split(",");
				//cashBalValue=parseFloat(dataArr[0]).toFixed(2);
				//creditBalValue = parseFloat(dataArr[1]).toFixed(2);
				document.getElementById("cashBalId").innerHTML=data;
				}
		});
	}
	
	
	
	function calFarmerBal(){
		var selectedFarmer = $("#farmer").val();
		var cashBalValue;
		var creditBalValue;
		var selectedPaymentMode=$("#payment").val();
		//alert(selectedPaymentMode);
		if(selectedPaymentMode=='Loan Repayment'){
			$.post("payment_populateFarmerLoanBalance",{selectedId:selectedFarmer},function(data){
				if(data!=null && data!=""){
					console.log(data);
					if(!isEmpty(data)){
						var n=data.split(",");
						$("#farmerLoanAmt").text(n[0]);
						$("#farmerLoanBal").text(n[1]);
						$("#cashBalIdFarmer").text(n[2]);
					}else{
						document.getElementById("farmerLoanAmt").innerHTML="0";
						document.getElementById("farmerLoanBal").innerHTML="0";
						document.getElementById("cashBalIdFarmer").innerHTML="0";
						
					}
					
					}
			});
		}else{
		$.post("payment_populateFarmerAccBalance",{selectedFarmer:selectedFarmer},function(data){
			if(data!=null && data!=""){
			//	alert(data);
				//var dataArr = data.split(",");
				//cashBalValue=parseFloat(dataArr[0]).toFixed(2);
				//creditBalValue = parseFloat(dataArr[1]).toFixed(2);
				document.getElementById("cashBalIdFarmer").innerHTML=data;
				}
		});
		}
	}
	
	function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
	
	function resetVal(){
		document.getElementById("city").selectedIndex=0;
		document.getElementById("village").selectedIndex=0;
		document.getElementById("farmer").selectedIndex=0;
		document.getElementById("agent").selectedIndex=0;
		document.getElementById("pageNo").value="";
		document.getElementById("payment").selectedIndex=0;
		document.getElementById("startDate").value="";
		document.getElementById("cashBalId").innerHTML="";
		document.getElementById("cashBalIdFarmer").innerHTML="";
		$("#remarks").val("");
		$("#validateError").html("");
		document.getElementById("balanceR").value="";
		document.getElementById("balanceP").value="";
	}


	</script>
	
	<%-- <div id="confirmAlert" class="popPendingMTNTAlert">
	<div class="popClose" onclick="disableConfirmAlert()"></div>
	<div style="padding-top:10px;">
		<s:text name="payment.confirm"/>
	</div>	
	<div style="padding-top:20px;">
		<input type="button" class="cancel-btn btn btn-sts"" value="<s:text name="ok"/>" onclick="submitPaymentForm()"/>
	</div>
</div> --%>
<s:form action="payment_populatePrintHTML" id="receiptForm" method="POST" target="printWindow">
	<s:hidden id="receiptNo" name="receiptNo"></s:hidden>
</s:form>
<%--<jsp:include page="../common/web_transaction-assets.jsp"></jsp:include>--%>
<script type="text/javascript">
document.getElementById("city").selectedIndex=0;
document.getElementById("village").selectedIndex=0;
document.getElementById("farmer").selectedIndex=0;
document.getElementById("agent").selectedIndex=0;
document.getElementById("pageNo").value="";
document.getElementById("payment").selectedIndex=0;
//document.getElementById("serialNumber").value="";
document.getElementById("startDate").value="";
document.getElementById("balanceR").value="";
document.getElementById("balanceP").value="";
$("#remarks").val("");
$("#validateError").html("");
</script>
</body>