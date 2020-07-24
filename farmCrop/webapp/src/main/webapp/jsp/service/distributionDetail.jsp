
<script src="plugins/select2/select2.min.js"></script>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/detail-assets.jsp"%>
<html>
<head>
<meta name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
	
</script>
<body>
	<font color="red"><s:actionerror /></font>
	<s:form name="form" cssClass="fillform">
		<s:hidden key="id" />
		<s:hidden key="distribution.id" id="distibutionId" />
		<s:hidden key="command" />
		<s:hidden key="currentPage" />
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
							value="distribution.transactionTime" />&nbsp;</td>

					<td><s:text name="updatedUser" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.updatedUserName" />&nbsp;</td>

					<td><s:text name="updatedTime" /></td>
					<td style="font-weight: bold;"><s:property value="updatedDate" />&nbsp;</td>

					<s:if
						test='!distribution.servicePointName.equalsIgnoreCase("agentStock")'>

						<td><s:text name="stockType" /></td>
						<td style="font-weight: bold;"><s:property
								value="%{getLocaleProperty('warehouseStock')}" />&nbsp;</td>
				</tr>
				<tr class="odd">

					<td><s:property value="%{getLocaleProperty('warehouse')}" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.servicePointName" /></td>
					<%-- 				<td   style="font-weight: bold;"><s:property value="distribution.servicePointId" />-<s:property value="distribution.servicePointName" /></td>	
 --%>
					<td><s:property value="%{getLocaleProperty('mobileuser')}" /></td>
					<td><s:text name="NA" /></td>
					</s:if>

					<s:else>
						<td><s:text name="stockType" /></td>
						<td style="font-weight: bold;"><s:property
								value="%{getLocaleProperty('mobileUserStock')}" />&nbsp;</td>
				</tr>
				<tr class="odd">

					<td><s:property value="%{getLocaleProperty('warehouse')}" /></td>
					<s:if test="currentTenantId=='pratibha'">
						<td><s:property value="distribution.warehouseName" /></td>
					</s:if>
					<s:else>
						<td><s:text name="NA" /></td>
					</s:else>
					<td><s:property value="%{getLocaleProperty('profile.agent')}" />
					</td>
					<td style="font-weight: bold;"><s:property
							value="distribution.agentName" />-<s:property
							value="distribution.agentId" /></td>

					</s:else>


					<td><s:text name="farmerType" /></td>
					<td style="font-weight: bold;"><s:property value="farmerType" />&nbsp;</td>

					<td><s:text name="farmer" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.farmerName" />&nbsp;</td>
					<s:if test="currentTenantId!='gsma'">
						<s:if test="getFarmerType()=='Registered'">
							<td><s:text name="farmer.lastName" /></td>
							<td style="font-weight: bold;"><s:property
									value="distribution.fatherName" />&nbsp;</td>
						</s:if>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
					</s:else>
				</tr>
				<tr class="odd">

					<td><s:text name="village.name" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.village.name" />&nbsp;</td>
					<s:if test="currentTenantId!='gsma'">
						<td><s:text name="farmer.mobileNumber" /></td>
						<td style="font-weight: bold;"><s:property
								value="distribution.mobileNumber" /></td>
					</s:if>
					<td>
						<%-- <s:text name="freeDist" /> --%> <s:property
							value="%{getLocaleProperty('freeDist')}" />
					</td>
					<td style="font-weight: bold;"><s:text
							name="distribution.freeDistribution" /></td>

					<td><s:text name="totalQty" /></td>
					<td style="font-weight: bold;"><s:property value="quantiy" />&nbsp;</td>

					<td><s:text name="grossAmt" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.totalAmount" />&nbsp;</td>
				</tr>
				<tr class="odd">

					<td><s:text name="tax" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.tax" />&nbsp;</td>

					<td><s:text name="finalAmt" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.finalAmount" />&nbsp;</td>

					<td><s:text name="payementAmount" /></td>
					<td style="font-weight: bold;"><s:property
							value="distribution.paymentAmount" />&nbsp;</td>

					<%-- <td ><s:text name="credit" /></td>
			<td  style="font-weight: bold;"><s:property value="credit" />&nbsp;</td>
			 --%>
					<s:property value="distributionimage" />
					<td><s:text name="seasonCode" /></td>
					<td style="font-weight: bold;"><s:property
							value="seasonCodeAndName" />&nbsp;</td>
					<s:if test="distImgAvil ==1">
						<td><s:text name="%{getLocaleProperty('photo')}" /></td>
						<td><button type='button' style='margin-right: 2%'
								onclick="popupWindow('<s:property value="distribution.id" />')">
								<i class='fa fa-picture-o' aria-hidden='true'></i>
							</button></td>

					</s:if>
				</tr>

			</table>
		</div>
		<br />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered aspect-detail">
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
					<%-- <th><s:text name="existingQty"/></th> --%>
					<th><s:text name="cstPrice" /></th>
					<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
						<th><s:text name="sellingPrice" /></th>
					</s:if> --%>

					<th><s:text name="distQty" /></th>
					<s:if test="distribution.freeDistribution=='Normal Distribution'">
						<th><s:text name="amount" /></th>
					</s:if>
					<%-- <th><s:text name="avlQty"/></th> --%>
				</tr>
				</br>
				<s:iterator value="distributionDetailList" status="rowstatus">
					<tr class="odd">
						<td><s:property value="product.subcategory.name" /></td>

						<td><s:property value="product.name" /></td>
						<td><s:property value="product.unit" /></td>
						<s:if test="enableBatchNo ==1">
							<td id="batchNo"><s:property value="batchNo" />&nbsp;</td>
						</s:if>
						<%-- </s:if>
			<s:else>
			<td><s:text name="NA"/></td>
			</s:else> --%>
						<td><s:property value="avlQty" /></td>
						<%-- 	<td id='<s:property value="#rowstatus.count"/>'><s:property value="existingQuantity" /></td> --%>
						<td><s:property value="costPrice" /></td>
						<%-- <s:if test="distribution.freeDistribution=='Normal Distribution'">
							<td><s:property value="sellingPrice" /></td>
						</s:if> --%>
						<td><s:property value="disQuantity" /></td>
						<s:if test="distribution.freeDistribution==0">
							<td><s:property value="amount" /></td>
						</s:if>
						<%-- <td><s:property value="avlQty"/></td> --%>

					</tr>
				</s:iterator>
			</table>
		</div>



		<div class="appContentWrapper marginBottom">
			<div class="yui-skin-sam">
				<sec:authorize ifAllGranted="service.distribution.farmer.update">
					<span id="update" class=""><span class="first-child">
							<button type="button" class="edit-btn btn btn-success">
								<FONT color="#FFFFFF"> <b><s:text name="edit.button" /></b>
								</font>
							</button>
					</span></span>
				</sec:authorize>
				<sec:authorize ifAllGranted="service.distribution.farmer.update">
					<span class=""><span class="first-child">
							<button type="button" class="delete-btn btn btn-warning">
								<FONT color="#FFFFFF"> <b><s:text
											name="delete.button" /></b>
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
	</s:form>

	<button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead"></h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<div class="carousel-inner" role="listbox" id="mbody"></div>

						<a class="left carousel-control" href="#myCarousel" role="button"
							data-slide="prev"> <span
							class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a> <a class="right carousel-control" href="#myCarousel"
							role="button" data-slide="next"> <span
							class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
							<span class="sr-only">Next</span>
						</a>

					</div>
				</div>
					<div class="flexItem flex-layout flexItemStyle">
				<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
				<label for="txt" align="left" id="lat"></label><span>    </span>
</div>
<div class="flexItem flex-layout flexItemStyle">
				<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
				<label for="txt" align="right" id="lon"></label>
			</div> 
		
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
	</div>
		</div>
	</div>
	<s:form name="updateform" action="distribution_update.action">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="cancelform" action="distribution_list.action">
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="deleteform" action="distribution_delete.action">
		<s:hidden key="id" />
	</s:form>


	<script type="text/javascript">
		$(document).ready(function() {
			$('#update').on('click',function(e) {
				document.updateform.id.value = document.getElementById('distibutionId').value;
				document.updateform.currentPage.value = document.form.currentPage.value;
				document.updateform.submit();
			});
			$('#delete').on('click',function(e) {
				if (confirm('<s:text name="confirm.delete"/> ')) {
					document.deleteform.id.value = document.getElementById('villageId').value;
					document.deleteform.currentPage.value = document.form.currentPage.value;
					document.deleteform.submit();
					}
			});
			$('#delete').on('click',function(e) {
				if (confirm('<s:text name="confirm.delete"/> ')) {
					document.deleteform.id.value = document.getElementById('villageId').value;
					document.deleteform.currentPage.value = document.form.currentPage.value;
					document.deleteform.submit();
					}
			});
			
			$('.delete-btn').on('click',function(e) {
				if (confirm('<s:text name="delete.rowGrid"/> ')) {
					document.deleteform.id.value =document.getElementById('distibutionId').value;
					document.deleteform.submit();
					}
			});
		});
		 function popupWindow(imgArr){

			var id =imgArr;
		//	alert(id);
			$.post("distribution_populateImageId",{id:id},function(data){
				if(!isEmpty(data)){
					try{
						var str_array = data.split(',');
					//	alert(str_array);
						$("#mbody").empty();
						
						var mbody="";
						
						for(var i = 0; i < str_array.length; i++){
							var str_imageCode = str_array[i].split('/');
							if(i==0){
								mbody="";
								mbody="<div class='item active'>";
								mbody+='<img src="distribution_populatedetailImage.action?id='+str_imageCode[0]+'"/>';
								mbody+="</div>";
								jQuery.post("distribution_populateLatAndLon.action", {id:str_imageCode[0]},
										function(result) {
											//var valuesArr = $.parseJSON(result);
											var json = JSON.parse(result);
									 		$("#lat").html(json[0].lat);
									 		$("#lon").html(json[0].lon);
									 		//$("#currentQty").html(json[0].currentQty);
										});
							}else{
								mbody="";
								mbody="<div class='item'>";
								mbody+='<img src="distribution_populatedetailImage.action?id='+str_imageCode[0]+'"/>';
								mbody+="</div>";
								jQuery.post("distribution_populateLatAndLon.action", {id:str_imageCode[0]},
										function(result) {
											//var valuesArr = $.parseJSON(result);
											var json = JSON.parse(result);
									 		$("#lat").html(json[0].lat);
									 		$("#lon").html(json[0].lon);
									 		//$("#currentQty").html(json[0].currentQty);
										});
							}
							$("#mbody").append(mbody);
						}
						
						
						document.getElementById("enableModal").click();	
					}
					catch(e){
						
						alert(e);
						}
					
				}
				else{
				alert("No Image");
				}
		});
		
		} 
		
		
		function buttonEdcCancel(){
			document.getElementById("model-close-edu-btn").click();	
		 }
	</script>
</body>
</html>