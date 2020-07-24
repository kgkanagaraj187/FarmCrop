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
		<s:hidden key="id" />
		<s:hidden id="editedRowId" name="editedRowId" />
		<s:hidden id="stockArrJson" name="stockArrJson" />
		<s:hidden id="currentStockId" name="currentStock" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
		<s:hidden id="enableBatchNo" name="enableBatchNo" />
		<s:hidden id="firstEdit" name="firstEdit" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:property value="%{getLocaleProperty('info.warehouseStockInfo')}"/>
							</h2>
							<s:if test='branchId==null'>
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="app.branch" />
									</p>
									<p class="flexItem">
										<s:property value="%{getBranchName(filter.branchId)}" />
									</p>
								</div>
							</s:if>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="txnDate" />
								</p>
								<p class="flexItem">
									<s:property value="filter.transactionDate" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.orderNo" />
								</p>
								<p class="flexItem">
									<s:property value="filter.orderNo" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.vendorId" />
								</p>
								<p class="flexItem">
									<s:property value="filter.vendor.vendorName" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('warehouse')}"/>
								</p>
								<p class="flexItem">
									<s:property value="filter.warehouse.name" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="receiptNo" />
								</p>
								<p class="flexItem">
									<s:property value="filter.receiptNo" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="totalQty" />
								</p>
								<p class="flexItem">
									<s:property value="filter.totalQty" />
								</p>
							</div>

							<%-- <div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="totalAmt" />
								</p>
								<p class="flexItem">
									<s:property value="filter.totalAmount" />
								</p>
							</div>
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.tax" />
								</p>
								<p class="flexItem">
									<s:property value="filter.tax" />
								</p>
							</div>
							
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="warehouseProduct.finalAmt" />
								</p>
								<p class="flexItem">
									<s:property value="filter.finalAmount" />
								</p>
							</div> --%>

						</div>
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:property value="%{getLocaleProperty('info.warehouseStockInfoDetail')}"/>
							</h2>
							<table class="table table-bordered aspect-detail">
							</table>
							<tr class="odd">


							</tr>
							<div class="row">
								<div class="container-fluid">
								 <div class="notificationBar"> 
										<!-- <p class="bg-danger notification"> -->
											<sup style="color: red;">*
											
											<s:text name="reqd.field"/></sup>
											<!-- <p class="bg-danger notification"> -->
											
									
									</div>
									<sup style="color: red;">
											
											<span id="validateError" style="text-align: center" > </span></sup>
									<div>
									</div>
								</div>
							</div>
							<table class="table table-bordered aspect-detail"
								id="editingTable">

								<tr class="odd">
									<th><s:text name="categoryCodeName" /></th>
									<th><s:text name="productCodeName" /></th>
									<s:if test="enableBatchNo==1">
										<th><%-- <s:text name="batchNo" /> --%>
										<s:property value="%{getLocaleProperty('batchNo')}" />
										</th>
									</s:if>
									<th><s:text name="unit" /></th>
									<%-- <th><s:text name="warehouseProduct.costPrice" /></th> --%>
									<th><s:text name="currentGoodQty" /></th>
									<%-- <th><s:text name="currentDQty" /></th> --%>
									<th><s:text name="gootQty" /><sup style="color: red;">*</sup></th>
									<th><s:text name="damgesdQty" /></th>
									<%-- <th><s:text name="totQty"/></th>
			<th><s:text name="txnAmnt"/></th> --%>
									<th><s:text name="action" /></th>
								</tr>
								<tr class="odd">
									<td><div>
											<s:label id="editedCatId" />
										</div></td>
									<td><div>
											<s:label id="editedProdId" />
										</div></td>
									<s:if test="enableBatchNo==1">
										<td><div>
												<s:label id="editedBatchNoItd" />
											</div></td>
									</s:if>
									<td><div>
											<s:label id="editedUnitId" />
										</div></td>
									<%-- <td><div>
											<s:label id="editedCPId" />
										</div></td> --%>
									<td><div>
											<s:label id="currentQty" />
										</div></td>
									<%-- <td><div>
											<s:label id="currentDQty" />
										</div></td> --%>
									<td><div>
											<s:textfield name="goodQty" id="editedGoodQtyId"
												class="number" maxlength="7" />
										</div></td>
									<td><div>
											<s:textfield name="damegedQty" id="editedDamegedId"
												class="number" maxlength="7" />
										</div></td>
									<%-- <td><div><s:label id="editedTotQtyId"  ></div></td>
			<td><div><s:label id="editedTotAmntId" /></div></td> --%>
									<td><button type="button" class="btn btn-sm btn-success"
											aria-hidden="true" onclick="saveEditedValues()">
											<i class="fa fa-check"></i>
										</button></td>
								</tr>
							</table>

							<table class="table table-bordered aspect-detail"
								id="editedTable">

								<tr class="odd">
									<th><s:text name="categoryCodeName" /></th>
									<th><s:text name="productCodeName" /></th>
									<s:if test="enableBatchNo==1">
										<th><%-- <s:text name="batchNo" /> --%>
											<s:property value="%{getLocaleProperty('batchNo')}" />
											</th>
									</s:if>
									<th><s:text name="unit" /></th>
									<%-- <th><s:text name="costPrice" /></th> --%>
									<th><s:text name="currentGoodQty" /></th>
									<%-- <th><s:text name="currentDQty" /></th> --%>
									<th><s:text name="gootQty" /></th>
									<th><s:text name="damgesdQty" /></th>
									<%-- <th><s:text name="totQty"/></th> --%>
									<%-- <th><s:text name="txnAmnt"/></th> --%>
									<th><s:text name="action" /></th>
								</tr>
								<s:iterator value="payementDetailList" status="rowstatus">
									<tr class="odd">
										<td id='paymentId' style="display: none;"><s:property
												value="warehousePayment.id" /></td>
										<td id='avlStockId<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property value="avlStock" /></td>
										<td
											id='damagedAvlStockId<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property
												value="damagedAvlStock" /></td>
										<td
											id='warehouseProductId<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property
												value="warehouseProductId" /></td>
										<td id='idVal<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property value="id" /></td>
										<td id='catId<s:property value="#rowstatus.count"/>'><s:property
												value="product.subcategory.name" /></td> 
										<td id='prodId<s:property value="#rowstatus.count"/>'><s:property
												value="product.name" /></td>
										<s:if test="enableBatchNo==1">
											<td id='batchId<s:property value="#rowstatus.count"/>'><s:property
													value="batchNo" /></td>
										</s:if>
										<td id='unitId<s:property value="#rowstatus.count"/>'><s:property
												value="product.type.name" /></td>
										<%-- <td id='batchId<s:property value="#rowstatus.count"/>'><s:property value="totalAmount"/></td> --%>

										<%-- <td id='cpId<s:property value="#rowstatus.count"/>'><s:property
												value="costPrice" /></td> --%>
										<td align="right"><s:property value="avlStock" /></td>
										<%-- <td><s:property value="damagedAvlStock" /></td> --%>
										<td align="right" id='stockId<s:property value="#rowstatus.count"/>'><s:property
												value="stock" /></td>
										<td class="hide"
											id='stockIdPer<s:property value="#rowstatus.count"/>'><s:property
												value="stock" /></td>
										<td align="right" id='dstockId<s:property value="#rowstatus.count"/>'><s:property
												value="damagedStock" /></td>
										<td id='tstockId<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property value="totalStock" /></td>
										<td id='totAmtId<s:property value="#rowstatus.count"/>'
											style="display: none;"><s:property value="totalAmount" /></td>

										<td title="Edit"><button type="button" class="fa fa-pencil-square-o"
												onclick="editRow('<s:property value="#rowstatus.count"/>');"
												id="<s:property value="#rowstatus.count"/>"></button></td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="yui-skin-sam">
							<sec:authorize
								ifAllGranted="service.distribution.fieldStaff.update">
								<span><span class="first-child">
										<button type="button" onclick="updateWarehouseStock()"
											class="save-btn btn btn-success">
											<font color="#FFFFFF"> <b><s:text
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
						</div>
					</div>
				</div>
			</div>
		</div>



	</s:form>

	<s:form name="updateform" action="warehouseProduct_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="warehouseProduct_list.action">
		<s:hidden key="currentPage" />
	</s:form>


	<script type="text/javascript">
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
												if (confirm('<s:text name="confirm.delete"/> ')) {
													document.deleteform.id.value = document
															.getElementById('villageId').value;
													document.deleteform.currentPage.value = document.form.currentPage.value;
													document.deleteform
															.submit();
												}
											});
							$("#currentStockId").val('<s:property value="currentStock"/>');
							$("#editingTable").hide();
							$("#firstEdit").val("0");
							
						});
		$('.number').keypress(function(event) {
		    var $this = $(this);
		    if ((event.which != 46 || $this.val().indexOf('.') != -1) &&
		       ((event.which < 48 || event.which > 57) &&
		       (event.which != 0 && event.which != 8))) {
		           event.preventDefault();
		    }

		    var text = $(this).val();
		    if ((event.which == 46) && (text.indexOf('.') == -1)) {
		        setTimeout(function() {
		            if ($this.val().substring($this.val().indexOf('.')).length > 4) {
		                $this.val($this.val().substring(0, $this.val().indexOf('.') + 3));
		            }
		        }, 1);
		    }

		    if ((text.indexOf('.') != -1) &&
		        (text.substring(text.indexOf('.')).length > 3) &&
		        (event.which != 0 && event.which != 8) &&
		        ($(this)[0].selectionStart >= text.length - 2)) {
		            event.preventDefault();
		    }      
		});
		function saveEditedValues(){
			document.getElementById("validateError").innerHTML=' ';
			var rowId=$("#editedRowId").val();
			//$("#tstockId"+rowId).text($("#editedTotQtyId").text());
			//$("#totAmtId"+rowId).text($("#editedTotAmntId").text());
			
			var goodQty=$("#editedGoodQtyId").val();
			
			goodQty=parseFloat(goodQty);
			var damagedQty=$("#editedDamegedId").val();
			damagedQty=parseFloat(damagedQty);
			
			var hit=true;
			if(goodQty>=0){
				hit=true;
			}else if(goodQty==''||isNaN(goodQty)){
				hit=false;
				document.getElementById("validateError").innerHTML='<s:text name="invalid.fieldvalue.warehouseProduct.stock"/>';
			}
			/* else if(damagedQty==''||isNaN(damagedQty)){
				hit=false;
				document.getElementById("validateError").innerHTML='<s:text name="pleaseentermandatoryField"/>';
			} */
			
			if(hit){
				
				$("#stockId"+rowId).text($("#editedGoodQtyId").val());
				$("#dstockId"+rowId).text($("#editedDamegedId").val());
				$("#editingTable").hide();
			}
		}

		function editRow(id){
			
			$("#editingTable").hide();
			document.getElementById("validateError").innerHTML="";
			
			var avlStock=$("#avlStockId"+id).text();
			var avlDstock=$("#damagedAvlStockId"+id).text();
			avlStock=parseFloat(avlStock);
			var damagedAvlStock=$("#damagedAvlStockId"+id).text();
			damagedAvlStock=parseFloat(avlStock);
					
			$("#editedRowId").val(id);
			var catId="#catId"+id;
			var prodId="#prodId"+id;
			var cpId="#cpId"+id;
			var stockId="#stockId"+id;
			var perStockId="#stockIdPer"+id;
			var dstockId="#dstockId"+id;
			var tstockId="#tstockId"+id;
			var totAmtId="#totAmtId"+id;
			var batchId="#batchId"+id;
			var unitId="#unitId"+id;
		
			var goodStock=$(stockId).text();
			goodStock=parseFloat(goodStock);
			var goodStockPer=$(perStockId).text();
			goodStockPer=parseFloat(goodStockPer);
			var damagedStock=$(dstockId).text();
			damagedStock=parseFloat(damagedStock);
		
			var firstEdit=$("#firstEdit").val();
			if(firstEdit=="0"){
				if(goodStockPer > avlStock){
				document.getElementById("validateError").innerHTML="<s:text name="cantChangeTxn"/>";
				
			}else{
				$("#firstEdit").val("1");
				$("#editingTable").show();
			$("#editedCatId").text($(catId).text());
			$("#editedProdId").text($(prodId).text());
			$("#editedBatchNoId").text($(batchId).text());
			$("#editedUnitId").text($(unitId).text());
			$("#editedCPId").text($(cpId).text());
			//$("#editedTotQtyId").text($(tstockId).text());
			//$("#editedTotAmntId").text($(totAmtId).text());
			$("#editedGoodQtyId").val($(stockId).text());
			$("#editedDamegedId").val($(dstockId).text());
			$("#currentQty").text(avlStock);
			$("#currentDQty").text(avlDstock);
			}
		}else if(firstEdit=="1"){
			if(goodStockPer > avlStock){
				/* $("#editingTable").hide(); */
				document.getElementById("validateError").innerHTML="<s:text name="cantChangeTxn"/>";
				
			}else{
				$("#editingTable").show();
				$("#editedCatId").text($(catId).text());
				$("#editedProdId").text($(prodId).text());
				$("#editedBatchNoId").text($(batchId).text());
				$("#editedUnitId").text($(unitId).text());
				$("#editedCPId").text($(cpId).text());
				//$("#editedTotQtyId").text($(tstockId).text());
				//$("#editedTotAmntId").text($(totAmtId).text());
				$("#editedGoodQtyId").val($(stockId).text());
				$("#editedDamegedId").val($(dstockId).text());
				$("#currentQty").text(avlStock);
				$("#currentDQty").text(avlDstock);
			}
		}
			
		}
	function updateWarehouseStock(){
		 var table=document.getElementById("editedTable");
		 var rows = table.getElementsByTagName("tr");
		 var stockArr=[];
		 for(i=1;i<rows.length;i++){
			 var obj=new Object();
			 obj.id=$("#idVal"+i).text();
			 obj.stock=$("#stockId"+i).text();
			 obj.damagedStock=$("#dstockId"+i).text();
			 obj.totalStock=$("#tstockId"+i).text();
			 obj.totalAmount=$("#totAmtId"+i).text();
			 obj.paymentId=$("#paymentId").text();
			 obj.warehouseProductId=$("#warehouseProductId"+i).text();
			 obj.avlStock=$("#avlStockId"+i).text();
			 if($("#damagedAvlStockId"+i).text()==''){
				 obj.damagedAvlStock="0.0";
			 }else{
				 obj.damagedAvlStock=$("#damagedAvlStockId"+i).text();
			 }
			 
			 if($("#dstockId"+i).text()==''){
				 obj.damagedStock="0.0";
			 }else{
				 obj.damagedStock=$("#dstockId"+i).text();
			 }
			 if(obj.totalAmount==''){
				 obj.totalAmount = "0.0";
			 }
			 //alert(obj.damagedAvlStock);
			 
			 stockArr.push(obj);
		 }
		 var jsonArray=new Object();
	   		jsonArray=JSON.stringify(stockArr);
	   		$("#stockArrJson").val(jsonArray);
	   	 document.form.submit();
	}
	</script>
</body>
</html>