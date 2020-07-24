<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>
<meta name="decorator" content="swithlayout">
</head>
<body>
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
	<s:hidden id="prevQty" name="prevQty"/>
		<s:hidden key="id" />
		<s:hidden name="distributiondetail.id" id="distibutionDetailId" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden id="fieldStaffStock" name="fieldStaffStock" />
		<s:hidden name="distributionDetails" id="distributionDetailJson" />
		<s:hidden name="status" value="0" id="status" />
		<s:hidden name="receiptNo" id="receiptNo" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="">
					<div class="appContentWrapper marginBottom">
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:text name='info.distributionToMobileUser' />
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property
											value="%{getBranchName(distributiondetail.distribution.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="txnDate" />
								</p>
								<p class="flexItem">
									<s:property value="distributionTxnTime" />
									&nbsp;
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('warehouse')}" />
								</p>
								<p class="flexItem">
									<s:property
										value="distributiondetail.distribution.servicePointId" />
									-
									<s:property
										value="distributiondetail.distribution.servicePointName" />
								</p>
							</div>

							<div id='warehouseCode' style="display: none;">
								<s:property
									value="distributiondetail.distribution.servicePointId" />
							</div>

							<div id='warehouseName' style="display: none;">
								<s:property
									value="distributiondetail.distribution.servicePointName" />
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('profile.agent')}" />
								</p>
								<p class="flexItem">
									<s:property value="distributiondetail.distribution.agentId" />
									-
									<s:property value="distributiondetail.distribution.agentName" />
								</p>
							</div>

							<div id='agentId' style="display: none;">
								<s:property value="distributiondetail.distribution.agentId" />
							</div>

							<div id='agentName' style="display: none;">
								<s:property value="distributiondetail.distribution.agentName" />
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="seasonCodeName" />
								</p>
								<p class="flexItem">
									<s:property value="seasonCodeAndName" />
								</p>
							</div>

							<div id='productId' style="display: none;">
								<s:property value="distributiondetail.product.id" />
							</div>
							<div id='proUnitVal' style="display: none;">
								<s:property value="distributiondetail.product.unit" />
							</div>

						</div>
					</div>
				</div>
				<div class="appContentWrapper marginBottom" id="editingTable">
					<table class="table table-bordered aspect-detail">
						<tr class="odd">
							<th><s:text name="category" /></th>
							<th><s:text name="product" /></th>
							<th><s:text name="distribution.unit" /></th>
							<s:if test="enableBatchNo ==1">
								<th><s:text name="warehouseProduct.batchNo" /></th>
							</s:if>
							<th><s:text name="avilableQty" /></th>
							<%-- <th><s:text name="existQty" /></th> --%>
							<th><s:text name="distributionStock" /><sup
								style="color: red;">*</sup></th>
							<%-- <th><s:text name="avilableQty" /></th> --%>
							<th><s:text name="action" /></th>
						</tr>

						<tr class="odd">
							<td><s:label id="subCategoryEdited" />&nbsp;</td>
							<td><s:label id="productEdited" />&nbsp;</td>
							<td><s:label id="proUnitValues" />&nbsp;</td>
							<s:if test="enableBatchNo ==1">
								<td><s:label id="batchNoEdited" />&nbsp;</td>
							</s:if>
							<td><s:label id="availableQtyEdited" /></td>
							<%-- <td ><s:label id="existQtyEdited"/>&nbsp;    </td> --%>
							<td><s:textfield name="distributionQtyEdited"
									id="distributionQtyEdited" class="decimalCheck" />&nbsp;</td>
							<%-- <td ><s:label id="availableQtyEdited"/>    </td> --%>
							<td><button type="button" class="btn btn-sm btn-success"
									aria-hidden="true" onclick="saveEditedValues()">
									<i class="fa fa-check"></i>
								</button></td>
						</tr>
					</table>
				</div>

				<div class="appContentWrapper marginBottom" id="editedTable">
					<div class="container-fluid">
						<div class="notificationBar">
							<p class="bg-danger notification">
								<sup>*</sup>
								<s:text name="reqd.field" />
								<span id="validateError" style="text-align: center;padding-left:40px; "></span> <span
									id="validatePriceError" style="text-align: center"></span>
							</p>
						</div>
					</div>
					<table class="table table-bordered aspect-detail">
						<tr class="odd">
							<th><s:text name="category" /></th>
							<th><s:text name="product" /></th>
							<th><s:text name="distribution.unit" /></th>
							<s:if test="enableBatchNo ==1">
								<th><s:text name="warehouseProduct.batchNo" /></th>
							</s:if>
							<th><s:text name="avilableQty" /></th>
							<%-- <th><s:text name="existQty" /></th> --%>
							<th><s:text name="distributionStock" /></th>
							<%-- 	<th><s:text name="avilableQty" /></th> --%>
							<th><s:text name="action" /></th>
						</tr>
						</br>
						<s:iterator value="distributionDetailList" status="rowstatus">
							<tr class="odd">
								<td id="subCategory"><s:property
										value="product.subcategory.name" />&nbsp;</td>
								<td id="product"><s:property value="product.name" />&nbsp;
								</td>
								<td id="proUnitValue"><s:property value="product.unit" />&nbsp;
								</td>
								<s:if test="enableBatchNo ==1">
									<td id="batchNo"><s:property value="batchNo" />&nbsp;</td>
								</s:if>
								<td id="avlQty"><s:property value="WarehouseProductStock" />
								</td>
								<td id="currentQty" style="display: none;"><s:property
										value="currentQuantity" />&nbsp;</td>
								<td class="hide"><s:hidden id="changeStock" value="0" /> <s:hidden
										id="finalStock" /></td>
								<td id="qty"><s:property value="quantity" />&nbsp;</td>
								<td class="hide"><s:hidden id="hidQty" name="quantity" />&nbsp;
								</td>
								<%-- <td id="avlQty"><s:property value="currentQuantity" />    </td> --%>
								<td><button type="button" class="fa fa-pencil-square-o"
										onclick="editRow('<s:property value="#rowstatus.count"/>');"
										id="<s:property value="#rowstatus.count"/>"></button></td>
							</tr>
						</s:iterator>
					</table>
					<div class="yui-skin-sam">
						<sec:authorize
							ifAllGranted="service.distribution.fieldStaff.update">
							<span><span class="first-child">
									<button type="button" onclick="updateDistribution()"
										class="edit-btn btn btn-success">
										<FONT color="#FFFFFF"> <b><s:text
													name="update.button" /></b>
										</font>
									</button>
							</span></span>
						</sec:authorize>

						<span id="cancel" class=""><span class="first-child"><button
									type="button" class="back-btn btn btn-sts">
									<b><FONT color="#FFFFFF"><s:text name="back.button" />
									</font></b>
								</button></span></span>
						<s:if test="approved==1">
							<button type="button" onclick="approveFun()"
								class="edit-btn btn btn-success">
								<FONT color="#FFFFFF"><s:text name="approve.button" />
							</button>
						</s:if>
					</div>
				</div>
			</div>
		</div>

	</s:form>


	<s:form name="updateform"
		action="distributionToFieldStaff_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="distributionToFieldStaff_list.action">
		<s:hidden key="currentPage" />
	</s:form>
	<script type="text/javascript">
	
	 $('#distributionQtyEdited').keypress(function(event) {
		  if ((event.which != 46 || $(this).val().indexOf('.') != -1) &&
		    ((event.which < 48 || event.which > 57) &&
		      (event.which != 0 && event.which != 8))) {
		    event.preventDefault();
		  }

		  var text = $(this).val();

		  if ((text.indexOf('.') != -1) &&
		    (text.substring(text.indexOf('.')).length > 3) &&
		    (event.which != 0 && event.which != 8) &&
		    ($(this)[0].selectionStart >= text.length - 3)) {
		    event.preventDefault();
		  }
		});
		 
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
	
	function editRow(count){
		if($("#status").val()==1){
			document.getElementById("validateError").innerHTML='<s:text name="approvedValCantnbedited"/>';
		}else{
			
			$("#editingTable").show();
	$("#subCategoryEdited").text($("#subCategory").text());
	$("#productEdited").text($("#product").text());
	$("#proUnitValues").text($("#proUnitValue").text());
	$("#batchNoEdited").text($("#batchNo").text());
	$("#existQtyEdited").text($("#existQty").text());
	$("#distributionQtyEdited").val($("#qty").text());
//	$("#tempdistributionStock").val($("#qty").text());
	//$("#availableQtyEdited").val("test"));
	var quantity=$("#qty").text();
	var avlQty=$("#avlQty").text();
	
	var test = parseFloat(quantity) + parseFloat(avlQty);
	document.getElementById("availableQtyEdited").innerHTML = avlQty;
		}
	}
	function saveEditedValues(){
		var existQty=$("#availableQtyEdited").text();
		existQty=parseFloat(existQty);
		var distributionQty=$("#distributionQtyEdited").val();
	
		distributionQty=parseFloat(distributionQty);
		//alert(existQty+"****"+distributionQty);
		var fieldStaffStockval = $("#fieldStaffStock").val();
		var quantity = $("#hidQty").val();
		var fieldStaffStock =  parseFloat(fieldStaffStockval) + (distributionQty - parseFloat(quantity));
		var preQty=existQty+parseFloat($("#hidQty").val());
		if(distributionQty>preQty){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="insufficientExistingStock"/>';
		}
		else if(isNaN(distributionQty)){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="pleaseentermandatoryField"/>';
		}	else if(fieldStaffStock < 0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="fieldStaffStockCannotEdited"/>';
		}
		else{
			document.getElementById("validateError").innerHTML='<s:text name=" "/>';
		$("#qty").text($("#distributionQtyEdited").val());
	var preQty=existQty+parseFloat($("#hidQty").val());
		
		$('#avlQty').text(preQty - distributionQty);
	//	alert($("#availableQtyEdited").text());
		$('#finalStock').val(existQty);
		 $("#hidQty").val($("#qty").text());
	
		$("#editingTable").hide();
		
		
		
		  if(distributionQty!=quantity){
				$('#changeStock').val("1");
				
			}else{
				$('#changeStock').val("0");
			}
		}
	}
	function updateDistribution(){
	 var table=document.getElementById("editedTable");
	 var rows = table.getElementsByTagName("tr");
	 var distributionDetailList=[];
	 for(i=1;i<rows.length;i++){
	 var obj=new Object();
	 obj.id=$("#distibutionDetailId").val();
	 var quantity=$("#qty").text();
	 obj.quantity=quantity.trim();
	 var finalStock = $('#finalStock').val();
	 var avlQty=$("#avlQty").text();
	 obj.avlQty=avlQty.trim();
	// obj.avlQty=parseFloat(finalStock.trim());
	//alert(avlQty);
	    obj.warehouseCode=$("#warehouseCode").text();
		obj.warehouseName=$("#warehouseName").text();
		obj.agentId=$("#agentId").text();
		obj.agentName=$("#agentName").text();
		obj.productId=jQuery("#productId").text();
		obj.status=$("#status").val();
		obj.receiptNo=$("#receiptNo").val();
		//obj.existingQuantity=$("#avlQty").text();
		if($("#changeStock").val()=='1'){
			//	alert("dd");
				 distributionDetailList.push(obj);
		}
	
	 }
		 var jsonArray=new Object();
   		jsonArray=JSON.stringify(distributionDetailList);
   		$("#distributionDetailJson").val(jsonArray);
   		
   		var existQty=$("#availableQtyEdited").text();
		existQty=parseFloat(existQty);
   		var distributionQty=$("#distributionQtyEdited").val();
   		var preQty=existQty+parseFloat($("#hidQty").val());
		distributionQty=parseFloat(distributionQty);
		var fieldStaffStockval = $("#fieldStaffStock").val();
		var quantity = $("#hidQty").val();
		var fieldStaffStock =  parseFloat(fieldStaffStockval) + (distributionQty - parseFloat(quantity));
		
		if(distributionQty>preQty){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="insufficientExistingStock"/>';
		}
		else if(isNaN(distributionQty)){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="pleaseentermandatoryField"/>';
		}	else if(fieldStaffStock < 0){
			document.getElementById("validateError").innerHTML=" ";
			document.getElementById("validateError").innerHTML='<s:text name="fieldStaffStockCannotEdited"/>';
		}
		else
		{
			document.form.submit();
		}
   	
   	 	
	  }
		$(document)
				.ready(
						function() {
							$('#update')
									.on(
											'click',
											function(e) {
												document.updateform.id.value = document
														.getElementById('distibutionDetailId').value;
												document.updateform.currentPage.value = document.form.currentPage.value;
												document.updateform.submit();
											});

							$('#delete')
									.on(
											'click',
											function(e) {
												if (confirm('<s:text name="distconfirm.delete"/> ')) {
													document.deleteform.id.value = document
															.getElementById('villageId').value;
													document.deleteform.currentPage.value = document.form.currentPage.value;
													document.deleteform
															.submit();
												}
											});
							$("#editingTable").hide();
							$("#receiptNo").val('<s:property value="distributiondetail.distribution.receiptNumber"/>');
							
							var extQty=$("#qty").text();
							$("#prevQty").val(extQty);
						});
		
		function approveFun(){
			document.getElementById("validateError").innerHTML=' ';
			document.getElementById("validateError").innerHTML='<s:text name="transactionApproved"/>';
			$("#status").val('1');
			$("#editingTable").hide();
			}	
		
	</script>

</body>
</html>