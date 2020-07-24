<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>
	<s:hidden key="cropHarvest.id" id="cropHarvestId" />
	<font color="red"> <s:actionerror /></font>
	<s:form name="form" cssClass="fillform">

		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div align="right">
							<b><s:text name="season" /> : </b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>

						<div class="formContainerWrapper dynamic-form-con">

							<h2>
								<s:text name="info.cropSale" />
							</h2>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="CropdateOfSale" />
								</p>
								<p class="flexItem">
									<s:date name="cropSupply.dateOfSale" format="dd/MM/yyyy" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmerName" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.farmerName" />
									&nbsp;-
									<s:property value="cropSupply.farmerId" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmName" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.farmName" />
									&nbsp;-
									<s:property value="cropSupply.farmCode" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('village.name')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.villageName" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="totalQty" />
								</p>
								<p class="flexItem">
								<s:set var="quantity" value="0" />
								<s:set var="kgQty" value="0" />
								<s:set var="quintalQty" value="0" />
								<s:iterator value="cropSaleDetailsList" var="cropSupply">
								<s:if test="currentTenantId=='pratibha'">
								<s:if
													test="crop.unit.equalsIgnoreCase('kgs') || crop.unit.equalsIgnoreCase('kg')">
								<s:set var="kgQty"   value="Qty"  />
								</s:if>
								<s:if
													test="crop.unit.equalsIgnoreCase('quintals') || crop.unit.equalsIgnoreCase('Quintal')">
													<s:set var="metricQty"   value="Qty"  />
													</s:if>
								</s:if>
								<s:else>
											<s:set var="quantity"  value="Qty"  />
										</s:else>
								</s:iterator>
								<s:if test="currentTenantId=='pratibha'">
										<s:set var="metricTon"
											value="getText('{0,number,###0.000}',{(#kgQty/1000)+(#metricQty/10)})" />
										<td width="65%"><s:property value="%{metricTon}" />&nbsp;(MT)</td>
								</s:if>
								<s:else>
										<s:set var="metricTon" value="%{#quantity/1000}" />
										<td width="65%">
										<%-- <s:property value="%{#metricTon}" />  --%>
										<s:property value="%{#metricTon}" />MT&nbsp;
										</td>
									</s:else></p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('totalPrice')}" />
								</p>
								<p class="flexItem">
								<%-- <s:property value="cropSupply.totalSaleValu" /> --%>
								<s:property value="getText('{0,number}',{cropSupply.totalSaleValu})" />
								</p>
							</div>
					<s:if test="currentTenantId=='chetna'">
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('transporterName')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.transportDetail" />
								</p>
							</div>
						
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('buyerName')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.buyerInfo.customerName" />
								</p>
							</div>
							
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('vechileNo')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.vehicleNo" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('receipt')}" />
								</p>
								<p class="flexItem">
									<s:property value="cropSupply.invoiceDetail" />
								</p>
							</div>
						</s:if>
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('photo')}" />
								</p>
								<p class="flexItem">
								<button type='button'  style='margin-right:2%' onclick=popupWindow('<s:property value="cropSupply.id" />')><i class='fa fa-picture-o' aria-hidden='true'></i></button>
								</p>
							</div>
						
						</div>
					</div>
				</div>
			</div>
		</div>
		<s:if test="cropSaleDetailsList.size>0">
			<div class="flex-view-layout">
				<div class="fullwidth">
					<div class="flexWrapper">
						<div class="flexLeft appContentWrapper">

							<div class="formContainerWrapper dynamic-form-con">

								<h2>
									<s:text name="info.cropSaleDetails" />
								</h2>

								<table class="table table-bordered aspect-detail">

									<tr>
										<td width="20%"><b><s:text name="cropName" /></b></td>
										<td><b><s:text name="cropType" /></b></td>
										<td><b><s:text name="variety" /></b></td>
										<td><b><s:text name="grade" /></b></td>
										<td><b><s:text name="unit" /></b></td>
										<td><b><s:text name="batch" /></b></td>
										<td><b><s:text name="cropsale.price" /></b></td>
										<td><b><s:property
													value="%{getLocaleProperty('quantity.name')}" /></b></td>
										<s:if test="currentTenantId=='pratibha'">
											<td><b><s:property
														value="%{getLocaleProperty('quantity.name')}" />&nbsp;in
													MT</b></td>
										</s:if>
										<td><b><s:text
													name="%{getLocaleProperty('totalPrice')}" /></b></td>
									</tr>
									<s:iterator value="cropSaleDetailsList" status="state"
										var="bean">
										<tr class="odd">
											<td><s:property value="crop.name" /></td>
											<td><s:property value="cropTypeStr" /></td>
											<td><s:property value="variety.name" /></td>
											<td><s:property value="grade.name" /></td>
											<td><s:property value="crop.unit" /></td>
											<td><s:property value="batchLotNo" /></td>
											<td><s:property value="price" /></td>
											<%-- <td><s:property value="qty" /></td> --%>
											
											<td><s:property value="getText('{0,number}',{qty})" /></td>
											
											<s:if test="currentTenantId=='pratibha'">
												<s:if
													test="crop.unit.equalsIgnoreCase('kgs') || crop.unit.equalsIgnoreCase('kg')">
													<td><s:set var="qty"
															value="getText('{0,number,###0.000}',{qty/1000})" /> <s:property
															value="%{#qty}" /></td>
												</s:if>
												<s:if
													test="crop.unit.equalsIgnoreCase('quintals') || crop.unit.equalsIgnoreCase('Quintal')">
													<td><s:set var="qty"
															value="getText('{0,number,###0.000}',{qty/10})" /> <s:property
															value="%{#qty}" /></td>
												</s:if>
											</s:if>
											 <%-- <td><s:property value="subTotal" /></td> --%> 
											<td><s:property value="getText('{0,number}',{subTotal})" /></td>
										</tr>
									</s:iterator>
								</table>
							</div>
							<div class="yui-skin-sam">
								<sec:authorize
									ifAllGranted="service.report.cropSaleEntryReport.update">
									<span id="update" class=""><span class="first-child">
											<button type="button" onclick="onUpdate();"
												class="edit-btn btn btn-success">
												<FONT color="#FFFFFF"> <b><s:text
															name="edit.button" /></b>
												</font>
											</button>
									</span></span>
								</sec:authorize>

								<sec:authorize
									ifAllGranted="service.report.cropSaleEntryReport.delete">
									<span id="delete" class=""><span class="first-child">
											<button type="button" class="delete-btn btn btn-warning"
												onclick="onDelete();">
												<FONT color="#FFFFFF"> <b><s:text
															name="delete.button" /></b>
												</font>
											</button>
									</span></span>
								</sec:authorize>

								<span id="cancel" class=""><span class="first-child"><button
											type="button" id="back" onclick="onCancel();"
											class="back-btn btn btn-sts">

											<b><FONT color="#FFFFFF"><s:text
														name="back.button" /> </font></b>
										</button></span></span>

							</div>
						</div>

					</div>
				</div>
			</div>
		</s:if>







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
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mbody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
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

	<s:form name="updateform" action="cropSaleEntryReport_update.action">
		<s:hidden id="id" name="id" />
		<s:hidden key="currentPage" />
	</s:form>
	<s:form name="deleteform" action="cropSaleEntryReport_delete.action">
		<s:hidden name="id" value="%{id}" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="listForm" id="listForm"
		action="cropSaleEntryReport_list.action">
		<s:hidden key="currentPage" />
	</s:form>



	<script type="text/javascript">
		function onUpdate() {
			document.updateform.submit();
		}

		function onDelete() {

			var val = confirm('<s:text name="confirm.delete"/>');
			if (val)
				document.deleteform.submit();
		}

		function onCancel() {
			document.listForm.submit();
		}
		
		function popupWindow(idArr){
			var id =idArr;
			$.post("cropSaleEntryReport_populateImageId",{id:id},function(data){
				if(!isEmpty(data)){
					try{
						var str_array = data.split(',');
						
						$("#mbody").empty();
						
						var mbody="";
						
						for(var i = 0; i < str_array.length; i++){
							var str_imageCode = str_array[i].split('/');
							if(i==0){
								mbody="";
								mbody="<div class='item active'>";
								mbody+='<img src="cropSaleEntryReport_populatedetailImage.action?id='+str_imageCode[0]+'"/>';
								mbody+="</div>";
							}else{
								mbody="";
								mbody="<div class='item'>";
								mbody+='<img src="cropSaleEntryReport_populatedetailImage.action?id='+str_imageCode[0]+'"/>';
								mbody+="</div>";
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
			/* try{ 
				var str_array = idArr.toString().split(',');
				$("#mbody").empty();
				var mbody="";
				
				for(var i = 0; i < str_array.length; i++){
					if(i==0){
						mbody="";
						mbody="<div class='item active'>";
						mbody+='<img src="cropSaleEntryReport_detailImage.action?id='+str_array[i]+'"/>';
						mbody+="</div>";
					}else{
						mbody="";
						mbody="<div class='item'>";
						mbody+='<img src="cropSaleEntryReportt_detailImage.action?id='+str_array[i]+'"/>';
						mbody+="</div>";
					}
					$("#mbody").append(mbody);
				 }
				
				//$("#mbody").first().addClass( "active" );
				
				document.getElementById("enablePhotoModal").click();	
			}
			catch(e){
				alert(e);
				}
		} */
		
		function buttonEdcCancel(){
			//refreshPopup();
			document.getElementById("model-close-edu-btn").click();	
		 }
	</script>



</body>