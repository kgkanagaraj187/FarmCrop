<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<style type="text/css">
.view {
	display: table-cell;
	background-color: #d2dae3;
}
</style>

</head>
<body>
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
		<s:hidden name="distributionDetails" id="distributionDetailJson"/>
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden name="editedRow" id="editedRow"/>
		<s:hidden name="changeStockUpdate" id="changeStockUpdate"/>
		<s:hidden name="distributionQuantiy" id="distributionTempQuantiy"/>
		<s:hidden name="distributedQty" id="distributedQty"/>
		<s:hidden name="status" value="0" id="status"/> 
		<s:hidden name="receiptNo"  id="receiptNo"/> 
		<s:hidden name="finalAmountFlag" value='0' id="finalAmountFlag"/> 
		<s:hidden name="amountFlag" value='0' id="amountFlag"/> 
		<div class="appContentWrapper marginBottom">
		<table class="table table-bordered aspect-detail">
			<tr class="odd">

				<th colspan="13"><s:text name='info.distribution' /></th>
			</tr>
			<tr class="odd">
				<s:if test='branchId==null'>

					<td width="35%"><s:text name="app.branch" /></td>
					<td width="65%"><s:property
							value="%{getBranchName(distribution.branchId)}" />&nbsp;</td>
				</s:if>
				<td width="35%"><s:text name="txnDate" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.transactionTime" />&nbsp;</td>

				<s:if test='distribution.servicePointName!=null && !distribution.servicePointName.equalsIgnoreCase("")'>
					
					<td><s:text name="stockType" /></td>
					<td  width="65%" style="font-weight: bold;"><s:text name="warehouseStock"/>&nbsp;</td>
					<td width="35%"><s:text name="warehouse" /></td>
					<td  width="65%" style="font-weight: bold;"><s:property value="distribution.servicePointName" /></td>
					<td id='warehouseCode' style="display: none;"><s:property value="distribution.servicePointId"/></td>
					<td id='warehouseName' style="display: none;"><s:property value="distribution.servicePointName"/></td>
					
					<td width="35%"><s:text name="mobileuser" /></td>
					<td width="35%"><s:text name="NA" /></td>
				</s:if>
				<s:else>
					<td><s:text name="stockType" /></td>
					<td  width="65%" style="font-weight: bold;"><s:text name="mobileUserStock"/>&nbsp;</td>
					
					<td width="35%"><s:text name="warehouse" /></td>
					<td width="35%"><s:text name="NA" /></td>
					
					<td width="35%"><s:text name="mobileuser" /></td>
					<td  width="65%" style="font-weight: bold;"><s:property value="distribution.agentName" />-<s:property value="distribution.agentId" /></td>
					<td id='agentId' style="display: none;"><s:property value="distribution.agentId"/></td>
					<td id='agentName' style="display: none;"><s:property value="distribution.agentName"/></td>
				</s:else>
				<td width="35%"><s:text name="farmerType" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="farmerType" />&nbsp;</td>
			</tr>
			<tr class="odd">
				<td width="35%"><s:text name="farmer" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.farmerName" />&nbsp;</td>

				<td width="35%"><s:text name="farmer.lastName" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.fatherName" />&nbsp;</td>

				<td width="35%"><s:text name="village.name" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.village.name" />&nbsp;</td>

				<td width="35%"><s:text name="farmer.mobileNumber" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.mobileNumber" /></td>

				<td width="35%"><s:text name="freeDist" /></td>
				<td width="65%" style="font-weight: bold;"><s:text name="distribution.freeDistribution"/></td>

				<td width="35%"><s:text name="totalQty" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="quantiy" />&nbsp;</td>
			</tr>

			<tr class="odd">
				<td width="35%"><s:text name="grossAmt" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.totalAmount" />&nbsp;</td>
			
			

				<td width="35%"><s:text name="tax" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.tax" />&nbsp;</td>

				<td width="35%"><s:text name="finalAmt" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.finalAmount" />&nbsp;</td>

				<td width="35%"><s:text name="payementAmount" /></td>
				<td width="65%" style="font-weight: bold;"><s:property
						value="distribution.paymentAmount" />&nbsp;</td>

				<%-- <td width="35%"><s:text name="credit" /></td>
				<td width="65%" style="font-weight: bold;" id="credit"><s:property
						value="credit" />&nbsp;</td> --%>

				<td width="35%"><s:text name="seasonCode" /></td>
			<td width="65%" style="font-weight: bold;"><s:property value="seasonCodeAndName" />&nbsp;</td>
			</tr>
		</table>
		</div>
		<br />
			<div class="appContentWrapper marginBottom">
			<div class="row">
				<div class="container-fluid">
					<div class="notificationBar">
						<p class="bg-danger notification">
							<sup>*</sup>
							<s:text name="reqd.field" />
							<span id="validateError" style="text-align: center;padding-left:100px; " ></span> <span
								id="validatePriceError" style="text-align: center"></span>
						</p>
					</div>
				</div>
			</div>
			</div>
			<div class="appContentWrapper marginBottom" id="editTable">
		<table class="table table-bordered aspect-detail" >
			<tr class="odd">
				<th colspan="13"><s:text name='info.distributionDetail' /></th>
			</tr>
			<tr class="odd">
				<th><s:text name="categoryName" /></th>
				<th><s:text name="productName" /></th>
				<th><s:text name="distribution.unit" /></th>
				<s:if test="enableBatchNo ==1">
				<th><s:text name="warehouseProduct.batchNo" /></th>
				 </s:if>
				
				<th><s:text name="avlQty" /></th>
				<th><s:text name="cstPrice" /></th>
				<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
				<th><s:text name="sellingPrice" /> <sup style="color: red;">*</sup></th>
				</s:if> --%>
				<th><s:text name="distQty" /><sup style="color: red;">*</sup></th>
				<%-- <th><s:text name="totalAmt"/></th>
				<th><s:text name="tax" /></th> --%>
				<s:if test="distribution.freeDistribution=='Normal Distribution'">
				<th><s:text name="amount" /></th>
				</s:if>
				<th><s:text name="action" /></th>
			</tr>
			</br>
			<tr class="odd">
					<td><div>
						<s:label id="category" />
					</div></td>

				<td><div>
						<s:label id="product1" />
					</div></td>
					
				<td><div>
						<s:label id="proUnitValue" />
					</div>
				
				</td>
				<s:if test="enableBatchNo ==1">
				<td><div>
						<s:label id="batchNo" />
					</div></td>
				
				</s:if>
				<td><div class="avlStkTxt">
						<s:label id="stock" />
					</div></td>
			   <td class="hide"><s:hidden id="hidStock"></s:hidden></td>
				<td><s:label id="costPrice" /></td>
	<%-- 	<s:if test="distribution.freeDistribution=='Normal Distribution'">
				<td><s:textfield id="selllingRupee" name="sellingRupee"  onkeyup="calculateIndividualAmt()"
					 maxlength="10" class="decimalCheck" /></td>
			</s:if> --%>
				<td class="alignCenter"><s:textfield class="decimalCheck" id="distributionStock" onkeyup="calculateIndividualAmt()"
						name="distributionStock" maxlength="8"
						cssStyle="text-align:right;padding-right:1px; width:138px!important;" />
				</td>
				<s:if test="distribution.freeDistribution=='Normal Distribution'">
				<td width="9%" style="font-weight: bold;"><s:label id="totalAmount" />&nbsp;</td>
				</s:if>
				<%-- <td><s:textfield id="tax" name="tax" onkeyup="calculateAmt()"
					 maxlength="10" class="decimalCheck" /></td>
				<td><s:label id="totalPriceLabel" /></td> --%>
				<td>
					<button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="saveEditedValues()"><i class="fa fa-check"></i> </button>
				</td>

			</tr>
		</table>
		</div>
		<div class="appContentWrapper marginBottom">
		<table class="table table-bordered aspect-detail" id="editedTable">

			<tr class="odd">
				<th><s:text name="categoryName" /></th>
				<th><s:text name="productName" /></th>
				<th><s:text name="distribution.unit"/></th>
				<s:if test="enableBatchNo ==1">
			<th><s:text name="warehouseProduct.batchNo"/></th>
			</s:if>
				<th><s:text name="avlQty" /></th>
				<%-- <th><s:text name="existingQty" /></th> --%>
				<th><s:text name="cstPrice" /></th>
				<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
				<th><s:text name="sellingPrice" /></th>
				</s:if> --%>
				<th><s:text name="distQty" /></th>
				<%-- <th><s:text name="totalAmt" /></th>
				<th><s:text name="tax" /></th> --%>
				
				<th><s:text name="amount" /></th>
				<%-- <th><s:text name="avlQty" /></th> --%>
				<th><s:text name="action" /></th>
			</tr>
			</br>
			<s:iterator value="distributionDetailList" status="rowstatus">
				<tr class="odd">
					<td id='catName<s:property value="#rowstatus.count"/>'><s:property
							value="product.subcategory.name" /></td>

					<td id='prodName<s:property value="#rowstatus.count"/>'><s:property
							value="product.name" /></td>
							
					<td id='proUnitVal<s:property value="#rowstatus.count"/>'><s:property
							value="product.unit" /></td>
							
					<s:if test="enableBatchNo ==1">
						<td id='batchNo<s:property value="#rowstatus.count"/>'><s:property value="batchNo" />&nbsp;</td>
					</s:if>
					<td id='avlQty<s:property value="#rowstatus.count"/>'><s:property
							value="avlQty" /></td>
					 <td id='exisQty<s:property value="#rowstatus.count"/>' style="display: none;"><s:property
							value="avlQty" /></td>
					<td id='costPrice<s:property value="#rowstatus.count"/>'><s:property
							value="costPrice" /></td>
							<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
					<td id='selQty<s:property value="#rowstatus.count"/>'><s:property
							value="sellingPrice" /></td>
						</s:if> --%>
					<td id='distQty<s:property value="#rowstatus.count"/>'><s:property
							value="disQuantity" /></td>
							
				<td class="hide" ><s:hidden id="hidQty%{#rowstatus.count}" name="disQuantity" /><s:hidden id="hidSel%{#rowstatus.count}" name="sellingPrice" /><s:hidden  id="changeStock%{#rowstatus.count}"
						value="0"	/><s:hidden  id="changeSel%{#rowstatus.count}"
						value="0"	/></td>
					<td id='amt<s:property value="#rowstatus.count"/>'><s:property
							value="amount" /></td>
					<%-- <td id='avlQty<s:property value="#rowstatus.count"/>'><s:property
							value="avlQty" /></td> --%>
					<td id='productId<s:property value="#rowstatus.count"/>' style="display: none;"><s:property value="product.id"/></td>
					<td id='idVal<s:property value="#rowstatus.count"/>' style="display: none;"><s:property value="id"/></td>
					<td id='distributionIdVal<s:property value="#rowstatus.count"/>' style="display: none;"><s:property value="distribution.id"/></td>
					<td><button type="button" class="fa fa-pencil-square-o"
							onclick="editRow('<s:property value="#rowstatus.count"/>');"
							id="<s:property value="#rowstatus.count"/>"></button></td>
					
				</tr>
			</s:iterator>
	</table>
		</div>
		<div class="appContentWrapper marginBottom">
		<table class="table table-bordered aspect-detail" id="taxTable">
			<tr class="odd">
				<td><s:text name="tax" /></td>
			 	<td ><s:textfield name="taxPercent" id='taxPercent' class="decimalCheck" maxlength="9" onkeyup="calculateAmt()"/></td> 
			 	<s:if test="distribution.freeDistribution=='Normal Distribution'">
			 	<td><s:text name="finalAmt" /></td>
			 	<td ><s:label id="finalAmount" /></td>
				<td><s:text name="payementAmount" /></td>
				<td ><s:textfield name="totalAmount" class="decimalCheck" id="paiedAmount" maxlength="10" onclick="setAmountFlag();"/></td>
				</s:if>
		</tr>
		</table>
		<div class="">
		<div class="yui-skin-sam">
		<sec:authorize ifAllGranted="service.distribution.farmer.update">
			<span ><span class="first-child">
			<button type="button"  onclick="updateDistribution()" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
				name="update.button" /></b> </font></button>
			</span></span>
 		</sec:authorize> 
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="cancel-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
						</font></b>
					</button></span></span>
			<s:if test="approved==1">
				<button type="button" onclick="approveFun()" class="save-btn btn btn-success"><font color="#FFFFFF"><s:text
				name="approve.button" /></b></button>
			</s:if>
		</div>
		</div>
		</div>
		
	</s:form>

	<s:form name="updateform" action="distribution_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="distribution_list.action">
		<s:hidden key="currentPage" />
	</s:form>


	<script type="text/javascript">
	jQuery(document).ready(function(){
		var tax='<s:property value="taxPercent"/>';
		var finalAmount='<s:property value="finalAmount"/>';
		var subToatal='<s:property value="subToatal"/>';
		var payment='<s:property value="payemnt"/>';
		$("#taxPercent").val(tax);
		$("#paiedAmount").val(payment);
		$("#finalAmount").text(finalAmount);
		$("#editTable").hide();
		$("#taxPercent").prop("readonly", false);
		$("#paiedAmount").prop("readonly", false);
		$("#receiptNo").val('<s:property value="distribution.receiptNumber"/>');
		
	
	});
	
		
	function approveFun(){
		var finalAmount=$("#finalAmount").text();
		finalAmount=parseFloat(finalAmount);
		var paymentAmount=$("#paiedAmount").val();
		paymentAmount=parseFloat(paymentAmount);
		if(finalAmount!=paymentAmount){
			document.getElementById("validateError").innerHTML='<s:text name="equalAmount"/>';
		}else{
		document.getElementById("validateError").innerHTML=' ';
		document.getElementById("validateError").innerHTML='<s:text name="transactionApprove"/>';
		$("#status").val('1');
		$("#taxPercent").prop("readonly", true);
		$("#paiedAmount").prop("readonly", true);
		$("#editTable").hide();
		$('#changeStockUpdate').val("1");
		updateDistribution();
		}	
	}	
	
	function  setAmountFlag(){
		$("#finalAmountFlag").val(1);
	}
	function editRow(count){
		if($("#status").val()==1){
			document.getElementById("validateError").innerHTML='<s:text name="approvedValCantnbedited"/>';
		}else{
		$("#taxPercent").prop("readonly", false);
		$("#paiedAmount").prop("readonly", false);
		
		$("#editTable").show();
		$("#editedRow").val(count);
		$("#cooperative").text($("#warehouse").text());
		
		$("#distributionTempQuantiy").val($("#distQty"+count).text());
		var categoryId="#catName"+count;
		var prodId="#prodName"+count;
		var batchNoId="#batchNo"+count;
		var exisQty="#exisQty"+count;
		var costPrice="#costPrice"+count;
		var selQty="#selQty"+count;
		var distQty="#distQty"+count;
		var amt="#amt"+count;
		var avlQty="#avlQty"+count;
		var chagedQty = parseFloat($(avlQty).text())+ parseFloat($(distQty).text());
		$("#hidStock").val(chagedQty);
	//	alert($("#hidStock").val());
		/* var tax ="#tax";*/
		//var totalAmt="#totalAmt"+count; 
		
		$("#category").text($(categoryId).text());
		
		$("#product1").text($(prodId).text());
		$("#proUnitValue").text(jQuery("#proUnitVal1").text());
		$("#batchNo").text($(batchNoId).text());
		$("#stock").text($(avlQty).text());
		var quantity=$("#exisQty").val();
		//alert("quantity:"+quantity);
		$("#costPrice").text($(costPrice).text());
		$("#selllingRupee").val($(selQty).text());
		$("#distributionStock").val($(distQty).text());
		//$("#tempdistributionStock").val($(distQty).text());
		$("#totalAmount").text($(amt).text());
		
		/* $("#taxPayied").val($("#taxPercent").text());
		$("#paymentEdited").val($("#paiedAmount").text()); */
		//loadCategory();
		}
	}
	
	function saveEditedValues(){
		var hit=true;
		var distributionQty=$("#distributionStock").val();
		
		var row=$("#editedRow").val();
		var avaliableQty=$("#avlQty"+row).text();
		
		var avaliableQtyStock=$("#hidStock").text();
		avaliableQty =parseFloat(avaliableQty);
		avaliableQtyStock =parseFloat(avaliableQtyStock);
		distributionQty=parseFloat(distributionQty);
	//	var price=$("#selllingRupee").val();
		if(isNaN(distributionQty)){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="pleaseentermandatoryField"/>';
			hit=false;
		}
		/* else if(price==0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="validAmount"/>';
			hit=false;
		} */
		else if(distributionQty==0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="validqty"/>';
			hit=false;
		}
		else
		if(distributionQty>avaliableQty){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="insufficientExistingStock"/>';
			hit=false;
		}else
		if(hit){
			document.getElementById("validateError").innerHTML=" ";
			
			$("#editTable").hide();
		var rowNo=$("#editedRow").val();
		$("#avlQty"+rowNo).text($("#stock").text());
		$("#selQty"+rowNo).text($("#selllingRupee").val());
		$("#distQty"+rowNo).text($("#distributionStock").val());
		$("#amt"+rowNo).text($("#totalAmount").text());
		var tempdistributionQty=$("#hidQty"+rowNo).val();
		var tempSel=$("#hidSel"+rowNo).val();
		calculateAmt();
	  if(distributionQty!=tempdistributionQty){
			$('#changeStock'+rowNo).val("1");
			
		}else{
			$('#changeStock'+rowNo).val("0");
		}
	  
	 /*  if(price!=tempSel){
			$('#changeSel'+rowNo).val("1");
			
		}else{
			$('#changeSel'+rowNo).val("0");
		} */
			
		/* 	if($("#changeStock"+rowNo).val()=='0'){
			
				$("#avlQty"+rowNo).text(parseFloat($("#stock").text()) - parseFloat($("#distributionStock").val()));
   				
   			} */
		
//	alert("chane"+$('#changeStock'+rowNo).val());
		/* $("#taxPercent").text($("#taxPayied").val());
		$("#paiedAmount").text($("#paymentEdited").val()); */
		
		var tempQty=$("#distributionTempQuantiy").val();
		
		var avlQuantity=tempQty-distributionQty;
		
		var stock=$("#stock").text();
		var qty=parseInt(stock);
		//if(distributionQty>existDistQty)
			var finalStockstock= qty+avlQuantity;
	
			
		
		$("#cooperative").text('');
		$("#category").text('');
		$("#product1").text('');
		$("#product1").text('');
		$("#stock").text('');
		$("#costPrice").text('');
		$("#selllingRupee").val('');
		$("#distributionStock").val('');
		$("#totalPriceLabel").text('');
		$("#totalAmount").text('');
		$("#taxPayied").val('');
		$("#paymentEdited").val('');
		}
	}
	
	function updateDistribution(){
		var creditAmt=$("#credit").text();
		creditAmt=parseFloat(creditAmt);
		var finalAmount=$("#finalAmount").text();
		finalAmount=parseFloat(finalAmount);
		var finalAmountFlag=$("#finalAmountFlag").val();
		var amountFlag=$("#amountFlag").val();
		var paymentAmount=$("#paiedAmount").val();
		paymentAmount=parseFloat(paymentAmount);
		
		//finalAmount=finalAmount-creditAmt;
		if((isNaN(finalAmount))&&(isNaN(paymentAmount))){
			
				   var table=document.getElementById("editedTable");
				   var rows = table.getElementsByTagName("tr");
				   var distributionList=[];
		   		for(i=1;i<rows.length;i++){
		   			var obj=new Object();
		   			var catNameId="#catName"+i;
		   			var prodNameId="#prodName"+i;
		   			var exisQtyId="#exisQty"+i;
		   			var costPriceId="#costPrice"+i;
		   			//var selQty="#selQty"+i;
		   			var distQtyId="#distQty"+i;
		   			//var amt="#amt"+i;
		   			var avlQtyId="#avlQty"+i;
		   			var id="#idVal"+i;
		   			var distributionIdVal="#distributionIdVal"+i;
		   			var productId="#productId"+i;
		   			/* var tax="#tax"+i;
		   			var amount="#totalAmt"+i; */
		   			
		   			obj.category=jQuery(catNameId).text();
		   			obj.productName=jQuery(prodNameId).text();
		   			obj.existingQuantity=parseFloat(jQuery(avlQtyId).text())+parseFloat(jQuery(distQtyId).text());
		   			obj.costPrice=jQuery(costPriceId).text();
		   			//obj.sellingPrice=jQuery(selQty).text();
		   			obj.disQuantity=jQuery(distQtyId).text();
		   			//obj.subTotal=jQuery(amt).text();
		   			obj.currentQuantity=jQuery(avlQtyId).text();
		   			obj.id=jQuery(id).text();
		   			if((jQuery("#taxPercent").val()=="")){
		   				obj.tax=0.00
		   			}else{
		   				obj.tax=jQuery("#taxPercent").val();
		   			}
		   			//obj.tax=(if (jQuery("#taxPercent").val()=="")?0.00:jQuery("#taxPercent").val());
		   			obj.distributionId=$(distributionIdVal).text();
		   			//obj.amount=$("#paiedAmount").val();
		   			obj.warehouseCode=$("#warehouseCode").text();
		   			obj.warehouseName=$("#warehouseName").text();
		   			obj.agentId=$("#agentId").text();
		   			obj.agentName=$("#agentName").text();
		   			obj.productId=jQuery(productId).text();
		   			obj.status=$("#status").val();
		   			obj.changeStock=$("#changeStock"+i).val();
		   			obj.receiptNo=$("#receiptNo").val();
		   			obj.finalAmount=finalAmount;
		   			/* if($("#changeStock"+i).val()=='0' || $("#changeSel"+i).val()=='1'){
		   				obj.currentQuantity = parseFloat(obj.currentQuantity) - parseFloat(jQuery(distQtyId).text());
		   				obj.existingQuantity=parseFloat(obj.currentQuantity)+parseFloat(jQuery(distQtyId).text());
		   			} */
		   		/* 	alert(obj.currentQuantity );
		   			alert( $("#changeSel"+i).val() ); */
		   			if($("#changeStock"+i).val()!='1' || $("#changeSel"+i).val()=='1'){
		   				obj.existingQuantity = $('#exisQtyId').val()
		   			}
		   		//	alert($("#changeStock"+i).val());
		   			if($("#changeStock"+i).val()=='1' || $("#changeSel"+i).val()=='1' || $("#changeStockUpdate").val()=='1'){
		   		//	alert("dd");
		   			distributionList.push(obj);
		   		}
		   		}
		   		var jsonArray=new Object();
		   		jsonArray=JSON.stringify(distributionList);
		   		$("#distributionDetailJson").val(jsonArray);
		   	 document.form.submit();
			
		}else{
			/* if(finalAmount!=paymentAmount){
				document.getElementById("validateError").innerHTML='<s:text name="equalAmount"/>';
			}else{ */
			   var table=document.getElementById("editedTable");
			   var rows = table.getElementsByTagName("tr");
			   var distributionList=[];
	   		for(i=1;i<rows.length;i++){
	   			var obj=new Object();
	   			var catNameId="#catName"+i;
	   			var prodNameId="#prodName"+i;
	   			var exisQtyId="#exisQty"+i;
	   			var costPriceId="#costPrice"+i;
	   			var selQty="#selQty"+i;
	   			var distQtyId="#distQty"+i;
	   			var amt="#amt"+i;
	   			var avlQtyId="#avlQty"+i;
	   			var id="#idVal"+i;
	   			var distributionIdVal="#distributionIdVal"+i;
	   			var productId="#productId"+i;
	   			var hidQty="#hidQty"+i;
	   			/* var tax="#tax"+i;
	   			var amount="#totalAmt"+i; */
	   			
	   			obj.category=jQuery(catNameId).text();
	   			obj.productName=jQuery(prodNameId).text();
	   			obj.existingQuantity=parseFloat(jQuery(avlQtyId).text())+parseFloat(jQuery(distQtyId).text());
	   			obj.costPrice=jQuery(costPriceId).text();
	   			//obj.sellingPrice=jQuery(selQty).text();
	   			obj.disQuantity=parseFloat(jQuery(distQtyId).text());
	   			obj.disExistQuantity=parseFloat(jQuery(distQtyId).text()) - parseFloat(jQuery(hidQty).val());
	   			obj.subTotal=jQuery(amt).text();
	   			obj.currentQuantity=jQuery(avlQtyId).text();
	   			obj.id=jQuery(id).text();
	   			if((jQuery("#taxPercent").val()=="")){
	   				obj.tax=0.00
	   			}else{
	   				obj.tax=jQuery("#taxPercent").val();
	   			}
	   			obj.distributionId=$(distributionIdVal).text();
	   			obj.amount=$("#paiedAmount").val();
	   			obj.warehouseCode=$("#warehouseCode").text();
	   			obj.warehouseName=$("#warehouseName").text();
	   			obj.agentId=$("#agentId").text();
	   			obj.agentName=$("#agentName").text();
	   			obj.productId=jQuery(productId).text();
	   			obj.status=$("#status").val();
	   			obj.changeStock=$("#changeStock"+i).val();
	   			obj.receiptNo=$("#receiptNo").val();
	   			obj.finalAmount=finalAmount;
	   			
	   			obj.qtyflag=0;
	   		  if($("#changeStock"+i).val()=='1' || $("#changeSel"+i).val()=='1'){
	   				//alert("dd");
	   				obj.qtyflag=1;
	   			//distributionList.push(obj);
	   		} 
	   		if(finalAmountFlag=='1'||amountFlag=='1'){
				
				obj.amountFlag=1;
			}
	   		distributionList.push(obj);
	   		}
	   		var jsonArray=new Object();
	   		jsonArray=JSON.stringify(distributionList);
	   		$("#distributionDetailJson").val(jsonArray);
	   	 document.form.submit();
		//}
		}
	
	
	}
	function loadCategory(){
		var cooprative=$("#cooperative").text();
		
		var tempWarehouse=cooprative.split("-");
		var selectdWarehouse=tempWarehouse[1].concat("-",tempWarehouse[0])
		
	 $.post("distribution_populateWarehouseCategory", {selectedCooperative:selectdWarehouse}, function (result) {
	    	insertOptions("category",JSON.parse(result));
	    	listProduct();
	    });
		
		//resetProductData();	   
	}
	
	function listProduct(){	
		
		var selectedCooperative =$("#cooperative").text();
		var category=$("#category").val();
		var tempWarehouse=selectedCooperative.split("-");
		var selectdWarehouse=tempWarehouse[1].concat("-",tempWarehouse[0])
		//alert(selectedCooperative);
		$.ajax({
			 type: "POST",
	         async: "false",
	         url: "distribution_populateProduct",
	         data: {selectedCategory:category,selectedCooperative:selectdWarehouse},
	         success: function(result) {
	        	 insertOptions("product1",JSON.parse(result));
	         }
		});
		
		
		//reset();
	}
	
	$('.decimalCheck').keypress(function(event) {
	    var $this = $(this);
	    if ((event.which != 46 || $this.val().indexOf('.') != -1) &&
	       ((event.which < 48 || event.which > 57) &&
	       (event.which != 0 && event.which != 8))) {
	           event.preventDefault();
	    }

	    var text = $(this).val();
	    if ((event.which == 46) && (text.indexOf('.') == -1)) {
	        setTimeout(function() {
	            if ($this.val().substring($this.val().indexOf('.')).length > 3) {
	                $this.val($this.val().substring(0, $this.val().indexOf('.') + 3));
	            }
	        }, 1);
	    }

	    if ((text.indexOf('.') != -1) &&
	        (text.substring(text.indexOf('.')).length > 4) &&
	        (event.which != 0 && event.which != 8) &&
	        ($(this)[0].selectionStart >= text.length - 4)) {
	            event.preventDefault();
	    }      
	});
	function setAmtFlag(){
		$("#amountFlag").val(1);
	}
	function calculateAmt(){
	//	alert("===")
		$("#finalAmountFlag").val(1);
		var table=document.getElementById("editedTable");
		   var rows = table.getElementsByTagName("tr");
		   var finalAmount=0;
		   for(i=1;i<rows.length;i++){
			   var amt =$("#amt"+i).text();
			   amt=parseFloat(amt);
			   finalAmount=finalAmount+amt;
			  
			 }
		   $("#finalAmount").text(finalAmount);
	/* 	   $("#finalAmount").text(finalAmount);
		  
		
		//var qty=$("#distributionStock").val();
		//var costPrice=$("#costPrice").text(); */
		var tax=$("#taxPercent").val();
		
		tax=parseFloat(tax);
		
		if(!isNaN(tax)){
		var taxAmount=(tax/100 * finalAmount);
		var result=finalAmount+taxAmount;
		result= parseFloat(result).toFixed(2);
		$("#finalAmount").text(result);
		}else{
			$("#finalAmount").text(finalAmount);
		}
		
		/* var distributionQty=$("#distributionStock").val();
		var avaliableQty=$("#stock").text();
		$("#stock").text(avaliableQty-distributionQty); */
		
	}
	
	function calculateIndividualAmt(){
		var qty=$("#distributionStock").val();
		var costPrice=0;
	//	alert($("#selllingRupee").val());
		/* if($("#selllingRupee")!=null){
			costPrice = 	$("#selllingRupee").val();
		} */
		if($("#costPrice")!=null){
			costPrice = 	$("#costPrice").text();
		}
		var amount=qty*costPrice;
		/* var tax=$("#taxPercent").val();
		
		var finalAmount=tax/100 * amount;
		finalAmount+=amount */
		$("#totalAmount").text(amount);
		//$("#totalPriceLabel").text(finalAmount);
		
		 
		 
		/* var distributionQty=$("#distributionStock").val();
		var avaliableQty=$("#stock").text();
		$("#stock").text(avaliableQty-distributionQty); */
		calculateAvlQty();
	}
	
	function calculateAvlQty(){
		var qty=$("#distributionStock").val();
		
		  var row=$("#editedRow").val();
		var avaliableQty=$("#hidStock").val();
		avaliableQty =parseFloat(avaliableQty);
		var stock=avaliableQty-qty;
		$("#stock").text(stock);
	}
	</script>
</body>
</html>