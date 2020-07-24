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
		<s:hidden name="distributionDetails" id="distributionDetailJson" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden name="editedRow" id="editedRow" />
		<s:hidden name="changeStockUpdate" id="changeStockUpdate" />
		<s:hidden name="distributionQuantiy" id="distributionTempQuantiy" />
		<s:hidden name="distributedQty" id="distributedQty" />
		<s:hidden name="status" value="0" id="status" />
		<s:hidden name="receiptNo" id="receiptNo" />
		<s:hidden name="finalAmountFlag" value='0' id="finalAmountFlag" />
		<s:hidden name="amountFlag" value='0' id="amountFlag" />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
				<tr class="odd">

					<th colspan="13"><s:text name='info.distribution' /></th>
				</tr>
				<tr class="odd">
					<s:if test='branchId==null'>

						<td><s:text name="app.branch" /></td>
						<td><s:property
								value="%{getBranchName(distribution.branchId)}" />&nbsp;</td>
					</s:if>
					<td><s:text name="txnDate" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.loanDate" />&nbsp;</td>

					<td><s:text name="farmer" /></td>
					<td style="font-weight: bold;"><s:property
						value="loanDistribution.farmer.firstName" />&nbsp;<s:property
						value="loanDistribution.farmer.lastName" />&nbsp; 
						<s:if test='loanDistribution.farmer.idproofNo!=null'>
						- <s:property
						value="loanDistribution.farmer.idproofNo" />&nbsp;
						</s:if>
						</td>
					<td class="hide"><s:text name="City/Town" /></td>
					<td class="hide" style="font-weight: bold;"><s:property
							value="loanDistribution.village.name" />&nbsp;</td>

				</tr>
				<tr class="odd">

					<%-- <td><s:text name="Account No" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.loanAccNo" />&nbsp;</td> --%>

					<td><s:text name="Vendor" /></td>
					<td style="font-weight: bold;"><s:property
							value="loanDistribution.vendor.vendorName" />&nbsp;</td>

					<td><s:text name="grossAmt" /></td>
					<td id="coc" style="font-weight: bold;"><s:property
							value="loanDistribution.totalCostToFarmer" />&nbsp;</td>
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
							<span id="validateError" style="text-align: center"></span> <span
								id="validatePriceError" style="text-align: center"></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<div class="appContentWrapper marginBottom" id="editTable">
			<table class="table table-bordered aspect-detail">
				<tr class="odd">
					<th colspan="13"><s:text name='info.distributionDetail' /></th>
				</tr>
				<tr class="odd">
					<th><s:text name="categoryName" /></th>
					<th><s:text name="productName" /></th>
					<th><s:text name="ratePerUnit" /><sup style="color: red;">*</sup></th>

					<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
				<th><s:text name="sellingPrice" /> <sup style="color: red;">*</sup></th>
				</s:if> --%>
					<th><s:text name="distQty" /><sup style="color: red;">*</sup></th>
					<%-- <th><s:text name="totalAmt"/></th>
				<th><s:text name="tax" /></th> --%>

					<th><s:text name="amount" /></th>

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

					<td class="hide"><s:hidden id="hidStock"></s:hidden></td>

					<td width="9%" style="font-weight: bold;"><s:label id="costPrice" /></td>


					<td class="alignCenter"><s:textfield class="decimalCheck"
							id="distributionStock" name="distributionStock" maxlength="8"
							onkeyup="calculateIndividualAmt()"
							cssStyle="text-align:right;padding-right:1px; width:138px!important;" />
					</td>


					<td width="9%" style="font-weight: bold;"><s:label
							id="totalAmount" />&nbsp;</td>
							
							
					<td>
						<button type="button" class="btn btn-sm btn-success"
							aria-hidden="true" onclick="saveEditedValues()">
							<i class="fa fa-check"></i>
						</button>
					</td>

				</tr>
			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail" id="editedTable">

				<tr class="odd">
					<th><s:text name="categoryName" /></th>
					<th><s:text name="productName" /></th>

					<th><s:text name="ratePerUnit" /></th>


					<th><s:text name="distQty" /></th>

					<th><s:text name="amount" /></th>


					<th><s:text name="action" /></th>
				</tr>
				</br>
				<s:iterator value="distributionDetailList" status="rowstatus">
					<tr class="odd">
						<td id='catName<s:property value="#rowstatus.count"/>'><s:property
								value="product.subcategory.name" /></td>

						<td id='prodName<s:property value="#rowstatus.count"/>'><s:property
								value="product.name" /></td>
						<td id='costPrice<s:property value="#rowstatus.count"/>'><s:property
								value="ratePerUnit" /></td>
						<td id='distQty<s:property value="#rowstatus.count"/>'><s:property
								value="quantity" /></td>


						<%--	<td id='amt<s:property value="#rowstatus.count"/>'>
							
							<s:property value="%{((100 - 10) / 2) * 100}" />
							
							</td> --%>

						<td class="hide"><s:hidden id="hidQty%{#rowstatus.count}"
								name="disQuantity" />
							<s:hidden id="hidSel%{#rowstatus.count}" name="sellingPrice" />
							<s:hidden id="changeStock%{#rowstatus.count}" value="0" />
							<s:hidden id="changeSel%{#rowstatus.count}" value="0" /></td>
						<td id='amt<s:property value="#rowstatus.count"/>'><s:property
								value="amount" /></td>



						<td id='productId<s:property value="#rowstatus.count"/>'
							style="display: none;"><s:property value="product.id" /></td>
						<td id='idVal<s:property value="#rowstatus.count"/>'
							style="display: none;"><s:property value="id" /></td>
						<td id='distributionIdVal<s:property value="#rowstatus.count"/>'
							style="display: none;"><s:property
								value="loanDistribution.id" /></td>
						<td><button type="button" class="fa fa-pencil-square-o"
								onclick="editRow('<s:property value="#rowstatus.count"/>');"
								id="<s:property value="#rowstatus.count"/>"></button></td>

					</tr>
				</s:iterator>
			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<div class="">
				<div class="yui-skin-sam">
					<sec:authorize
						ifAllGranted="service.loanDistributionService.update">
						<span><span class="first-child">
								<button type="button" onclick="updateDistribution()"
									class="save-btn btn btn-success">
									<font color="#FFFFFF"> <b><s:text
												name="update.button" /></b>
									</font>
								</button>
						</span></span>
					</sec:authorize>
					<span id="cancel" class=""><span class="first-child"><button
								type="button" class="cancel-btn btn btn-sts">
								<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
								</font></b>
							</button></span></span>
					<s:if test="approved==1">
						<button type="button" onclick="approveFun()"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"><s:text name="approve.button" /></b>
						</button>
					</s:if>
				</div>
			</div>
		</div>

	</s:form>

	<s:form name="updateform" action="loanDistribution_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="loanDistribution_list.action">
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

		
		$("#editTable").show();
		$("#editedRow").val(count);
		
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
		
		$("#costPrice").text($(costPrice).text());
		$("#category").text($(categoryId).text());
		$("#product1").text($(prodId).text());
		$("#proUnitValue").text(jQuery("#proUnitVal1").text());
		$("#batchNo").text($(batchNoId).text());
		$("#stock").text(chagedQty);
		var quantity=$("#exisQty").val();
		$("#selllingRupee").val($(selQty).text());
		$("#distributionStock").val($(distQty).text());
		$("#totalAmount").text($(amt).text());
		
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
		
	
		var price=$("#costPrice").text();
		if(price==''||isNaN(distributionQty)){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="pleaseentermandatoryField"/>';
			hit=false;
		}
		else if(price==0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="validAmount"/>';
			hit=false;
		}
		else if(distributionQty==0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="validqty"/>';
			hit=false;
		}
		else
		if(distributionQty>avaliableQtyStock){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="insufficientExistingStock"/>';
			hit=false;
		}else{
			document.getElementById("validateError").innerHTML=" ";
		}
		if(hit){
			document.getElementById("validateError").innerHTML=" ";
			$("#editTable").hide();
		var rowNo=$("#editedRow").val();
		$("#avlQty"+rowNo).text($("#stock").text());
		$("#selQty"+rowNo).text($("#selllingRupee").val());
		$("#distQty"+rowNo).text($("#distributionStock").val());
		$("#costPrice"+rowNo).text($("#costPrice").text());
		$("#amt"+rowNo).text($("#totalAmount").text());
		var tempdistributionQty=$("#hidQty"+rowNo).val();
		var tempSel=$("#hidSel"+rowNo).val();
	  if(distributionQty!=tempdistributionQty){
			$('#changeStock'+rowNo).val("1");
			
		}else{
			$('#changeStock'+rowNo).val("0");
		}
	  
	  if(price!=tempSel){
			$('#changeSel'+rowNo).val("1");
			
		}else{
			$('#changeSel'+rowNo).val("0");
		}
			
			if($("#changeStock"+rowNo).val()=='0'){
			
				$("#avlQty"+rowNo).text(parseFloat($("#stock").text()) - parseFloat($("#distributionStock").val()));
   				
   			}
		
//	alert("chane"+$('#changeStock'+rowNo).val());
		/* $("#taxPercent").text($("#taxPayied").val());
		$("#paiedAmount").text($("#paymentEdited").val()); */
		
		var tempQty=$("#distributionTempQuantiy").val();
		
		var avlQuantity=tempQty-distributionQty;
		
		var stock=$("#stock").text();
		var qty=parseInt(stock);
		//if(distributionQty>existDistQty)
			var finalStockstock= qty+avlQuantity;
	
		   calculateAmt();
		
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
		
				   var table=document.getElementById("editedTable");
				   var rows = table.getElementsByTagName("tr");
				   var distributionList=[];
		   		for(i=1;i<rows.length;i++){
		   			var obj=new Object();
		   			var catNameId="#catName"+i;
		   			var prodNameId="#prodName"+i;
		   			var exisQtyId="#exisQty"+i;
		   			var costPriceId="#costPrice"+i;
		   			var distQtyId="#distQty"+i;
		   			var amt="#amt"+i;
		   			var avlQtyId="#avlQty"+i;
		   			var id="#idVal"+i;
		   			var distributionIdVal="#distributionIdVal"+i;
		   			var productId="#productId"+i;
		   			obj.category=jQuery(catNameId).text();
		   			obj.productName=jQuery(prodNameId).text();

		   			obj.quantity=jQuery(distQtyId).text();
		   			obj.ratePerUnit=jQuery(costPriceId).text();
		   			obj.id=jQuery(id).text();

		   			obj.distributionId=$(distributionIdVal).text();

		   			obj.productId=jQuery(productId).text();
		   			obj.amount=jQuery(amt).text();
		   			obj.totalAmt=jQuery(amt).text();
		   			obj.totCost=$.trim($("#coc").text());
		   			distributionList.push(obj);

		   		}
		   		var jsonArray=new Object();
		   		jsonArray=JSON.stringify(distributionList);
		   		$("#distributionDetailJson").val(jsonArray);
		   	 document.form.submit();

	
	
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
		$("#finalAmountFlag").val(1);
		var table=document.getElementById("editedTable");
		   var rows = table.getElementsByTagName("tr");
		   var finalAmount=0;
		   for(i=1;i<rows.length;i++){
			   var amt =$("#amt"+i).text();
			   amt=parseFloat(amt);
			   finalAmount=finalAmount+amt;
			 }
		 $('#coc').html(finalAmount);
	}
	
	function calculateIndividualAmt(){
		var qty=$("#distributionStock").val();
		var costPrice=$("#costPrice").text();
		var amount=qty*costPrice;
		$("#totalAmount").text(amount);
	}
	
	
	</script>
</body>
</html>