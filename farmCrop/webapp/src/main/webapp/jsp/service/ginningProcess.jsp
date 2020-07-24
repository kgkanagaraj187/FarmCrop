<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<s:form name="form" cssClass="fillform" enctype="multipart/form-data">
		<font color="red"> <s:actionerror /></font>
		<!--start decorator body-->

		<div class="appContentWrapper marginBottom ">
			<div class="formContainerWrapper">
				<div class="row">
					<div class="container-fluid">
						<div class="notificationBar">
							<div class="error">
								<p class="notification">
									<span class="manadatory">*</span>
									<s:text name="reqd.field" />
								<div id="validateError" style="text-align: center;"></div>
								<div id="validatePriceError" style="text-align: center;"></div>
								</p>
							</div>
						</div>
					</div>
				</div>
				<h2>
					<s:property value="%{getLocaleProperty('process.ginning')}" />
				</h2>
				<div class="flexiWrapper filterControls">
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('date')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield name="processDate" id="processDate" readonly="true"
								theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years"
								cssClass="date-picker col-sm-3 form-control" size="20" />
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('ginning')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="ginningList"
								onchange="loadHeap(this.value)"
								cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('heap')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="heapList"
								cssClass="form-control input-sm select2"
								onchange="loadProduct(this.value)" />

						</div>
					</div>
					

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('product')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="productList"
								cssClass="form-control input-sm select2"
								onchange="loadData(this.value)" />
							<!-- <label for="txt" id="product" /> -->
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('ics.name')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
						<label for="txt" id="icsName" />
							<%-- <s:select list="{}" headerKey=""
								headerValue="%{getText('txt.select')}" id="icsList"
								cssClass="form-control input-sm select2"
								onchange="loadHeap(this.value)" /> --%>

						</div>
					</div>

				
				</div>
				<div class="flexiWrapper filterControls">
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('heapQty')}" />(<s:property value="%{getLocaleProperty('quintal')}"/>)<sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<label for="txt" id="heapQty" />
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('processQty')}" />(<s:property value="%{getLocaleProperty('quintal')}"/>)<sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="processQty" name="processQty" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isDecimal(event)"/>
						</div>
					</div>

			<%-- 		<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('lintCotton')}" />(<s:property value="%{getLocaleProperty('quintal')}"/>)<sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="lintCotton" name="lintCotton" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isDecimal(event)"/>
						</div>
					</div> --%>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('seedCotton')}" />(<s:property value="%{getLocaleProperty('quintal')}"/>)<sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield id="seedCotton" name="seedCotton" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isDecimal(event)"/>
						</div>
					</div>

					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('scrup')}" />(<s:property value="%{getLocaleProperty('quintal')}"/>)</label>
						<div class="form-element">
							<s:textfield id="scrup" name="scrup" maxLength="10"
								cssClass="col-sm-3 form-control" onkeypress="return isDecimal(event)"/>
						</div>
					</div>

				</div>
			</div>
		</div>


		<div class="appContentWrapper marginBottom hide">
			<div class="formContainerWrapper">
			<%-- 	<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('product')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
							<label for="txt" id="productCode" />
						</div>
					</div>
 --%>
					<div class="flexi flexi10">
						<label for="txt"><s:property
								value="%{getLocaleProperty('ics.name')}" /><sup
							style="color: red;">*</sup></label>
						<div class="form-element">
						<label for="txt" id="icsCode" />
						</div>
					</div>
				</div>
				</div>
				

		<div class="service-content-wrapper">
			<div class="service-content-section">
				<div class="appContentWrapper marginBottom">
					<div class="formContainerWrapper">
					<%-- 	<div class="formContainerWrapper prodRecTable">
							<h2>
								<s:text name="ginningProcess" />
							</h2>
							<div class="flexiWrapper filterControls">
								<div id="ginningProcessInfo"
									style="text-align: center; height: 20px; width: 100%;">
								</div>

								<div style="width: 98%;" id="baseDiv">
									<table id="detail" style="width: auto !Important;"></table>
								</div>

							</div>
						</div> --%>
						<div class="yui-skin-sam" id="savebutton">
							<%-- <sec:authorize
								ifAllGranted="service.ginning.create"> --%>

								<span id="save" class=""><span class="first-child">
										<button id="saveBtn" type="button" onclick="submitGinningProcess()"
											class="save-btn btn btn-success">
											<font color="#FFFFFF"> <b><s:text
														name="save.button" /></b>
											</font>
										</button>
								</span></span>

							<%-- </sec:authorize> --%>
							<span class="" style="cursor: pointer;"><span
								class="first-child"><a id="cancelBtnId"
									onclick="resetData();" class="cancel-btn btn btn-sts"> <font
										color="#FFFFFF"> <s:text name="cancel.button" />
									</font>
								</a></span> </span>
						</div>


					</div>
				</div>
			</div>
		</div>

	</s:form>
	<script>
		$(document).ready(function() {
			$('#processDate').val('<s:property value="currentDate" />');
			$('#heapQty').text("0");
			onFilterData();
		});
		function onFilterData() {
			callAjaxMethod("ginningProcess_populateGinningList.action",
					"ginningList");
		}
		function callAjaxMethod(url, name) {
			var cat = $.ajax({
				url : url,
				async : true,
				type : 'post',
				success : function(result) {
					insertOptions(name, JSON.parse(result));
				}
			});
		}
		function loadProduct(heapCode) {
			var ids=new Array();
			var ginningCode=$('#ginningList').val();
			 $('#heapQty').text('0');
			 $('#processQty').val('');
			 $('#seedCotton').val('');
			 $('#scrup').val('');
			 $('#icsName').text('');
			 $('#validateError').text('');
			 
			if (!isEmpty(ginningCode) && ginningCode != "0") {
				$.ajax({
					type : "POST",
					async : false,
					url : "ginningProcess_populateProductList.action",
					data : {
						selectedGinning : ginningCode,
						selectedHeap : heapCode
					},
					success : function(result) {
						insertOptions("productList", $.parseJSON(result));
					}
				});
			}
		}
		function loadICS(productCode) {
			var ids=new Array(); 
			 ids.push("icsList");
			ids.push("heapList");
			 removeOptions(ids);
			 $('#heapQty').text('0');
			 $('#processQty').val(''); 
			 $('#seedCotton').val('');
			 $('#scrup').val(''); 
			var ginningCode=$('#ginningList').val();
			if (!isEmpty(productCode) && productCode != "0" && !isEmpty(ginningCode) && ginningCode!="0") {
				var params=ginningCode+","+productCode;
				$.ajax({
					type : "POST",
					async : false,
					url : "ginningProcess_populateICSList.action",
					data : {
						params:params
					},
					success : function(result) {
						insertOptions("icsList", $.parseJSON(result));
					}
				});
			}
		}
		function loadHeap(ginningCode) {
			var ids=new Array();
			 ids.push("productList");
			 ids.push("heapList");
			 removeOptions(ids);
			 $('#heapQty').text('0');
			 $('#processQty').val('');
			 $('#seedCotton').val('');
			 $('#scrup').val('');
			 $('#icsName').text('');
			 $('#validateError').text('');
			if (!isEmpty(ginningCode) && ginningCode!="0") {
				$.ajax({
					type : "POST",
					async : false,
					url : "ginningProcess_populateHeapList.action",
					data : {
						ginningCode : ginningCode
					},
					success : function(result) {
						insertOptions("heapList", $.parseJSON(result));
					}
				});
			}
		}
		function loadData(productId) {
			$('#processQty').val('');
			 $('#seedCotton').val('');
			 $('#scrup').val(''); 
			 $('#heapQty').text('0');
			 var ginningCode=$('#ginningList').val();
			 var heapId=$('#heapList').val();
			if (!isEmpty(heapId) && heapId != "0") {
				$.ajax({
					type : "POST",
					async : false,
					url : "ginningProcess_populateDataValue.action",
					data : {
						selectedHeap : heapId,
						selectedGinning : ginningCode,
						selectedProduct : productId
						
					},
					success : function(result) {
						$('#heapQty').text(result.qty);
						$('#icsName').text(result.ics);
						$('#icsCode').text(result.icsCode);
					}
				});
			}
		}
		function submitGinningProcess(){
			
			var ginningCode=$('#ginningList').val();
			 var prodCode=$('#productList').val();
			var icsCode=$('#icsCode').text();
			var heapId=$('#heapList').val();
			var procsQty=$('#processQty').val();
			var seedQty=$('#seedCotton').val();
			var scrupQty=$('#scrup').val();
			var processDate=$('#processDate').val();
			var avilQty=$('#heapQty').text();  
			var params=ginningCode+"~"+heapId+"~"+prodCode+"~"+icsCode;
			var hit=true;
			hit=validateQuantity();
			if(hit){
			$.ajax({
				type: "POST",
				async: false,
				url: "ginningProcess_populateGinningProcess.action",
				data: {
					params:params,
					procsQty:procsQty,
					seedQty:seedQty,
					scrupQty:scrupQty,
					processDate:processDate
				},
				success: function(result){
					alert("Ginning Success");
				 }
				
			});
			resetData();
			}
		}
		function removeOptions(ids){
			 $(ids).each(function(k,v){
			  console.log(v);
			  $("#"+v).find('option').not(':first').remove();
			 });
			 $('.select2').select2();
			}
		function resetData(){
			var ids=new Array();
			 ids.push("ginningList");
			 ids.push("productList");
			 ids.push("icsList");
			 ids.push("heapList");
			 removeOptions(ids);
			 $('#heapQty').text('0');
			 $('#processQty').val('');
			 $('#seedCotton').val('');
			 $('#scrup').val('');
			 $('#validateError').text('');
			 window.location.href="ginningProcess_create.action";    
		}
		function isDecimal(evt) {
			
			 evt = (evt) ? evt : window.event;
			    var charCode = (evt.which) ? evt.which : evt.keyCode;
			    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
			        return false;
			    }
			    return true;
		}
		function validateQuantity(){
			var ginningCode=$('#ginningList').val();
			var heapId=$('#heapList').val();
			var prodCode=$('#productList').val();
			var procsQty=$('#processQty').val();
			var seedQty=$('#seedCotton').val();
			var scrupQty=$('#scrup').val();
			var processDate=$('#processDate').val();
			var avilQty=$('#heapQty').text();
			var totTxnQty=parseFloat(seedQty)+parseFloat(scrupQty);
			if(isEmpty(ginningCode) || ginningCode=="0"){
				jQuery("#validateError").text('<s:text name="ginning.empty"/>');
				return false;
			}
			if(isEmpty(heapId) || heapId==""){
				jQuery("#validateError").text('<s:text name="heap.empty"/>');
				return false;
			}
			if(isEmpty(prodCode) || prodCode==""){
				jQuery("#validateError").text('<s:text name="prod.empty"/>');
				return false;
			}
			if(parseFloat(avilQty)<=0){
				jQuery("#validateError").text('<s:text name="insufficientAvil.qty"/>');
				return false;
			}
			if(parseFloat(avilQty)<parseFloat(procsQty)){
				jQuery("#validateError").text('<s:text name="error.qty"/>');
				return false;
			}
			if(isEmpty(procsQty)|| parseFloat(procsQty)<=0){
				jQuery("#validateError").text('<s:text name="insufficientProcess.qty"/>');
				return false;
			}
			
			if(isEmpty(seedQty) || parseFloat(seedQty)<=0){
				jQuery("#validateError").text('<s:text name="insufficientSeed.qty"/>');
				return false;
			}
			if(parseFloat(procsQty)<parseFloat(seedQty)){
				jQuery("#validateError").text('<s:text name="seedProcessQtyError"/>');
				return false;
			}
			if(parseFloat(procsQty)<parseFloat(scrupQty)){
				jQuery("#validateError").text('<s:text name="scrupProcessQtyError"/>');
				return false;
			}
			if(parseFloat(procsQty)<totTxnQty){
				jQuery("#validateError").text('<s:text name="seedScrapExceeds.qty"/>');
				return false;
			}
			
			return true;
				
		}
	</script>
</body>