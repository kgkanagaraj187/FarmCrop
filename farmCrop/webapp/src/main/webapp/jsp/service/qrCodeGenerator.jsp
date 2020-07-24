<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<body>

	 <div class="error"><%-- <sup>*</sup> <s:text name="reqd.field" /> --%>
	<div id="validateError" style="text-align: center; padding: 5px 0 0 0"></div>
	</div> 

	<s:form name="form" cssClass="fillform">
		<s:hidden key="currentPage" />
			<div class="appContentWrapper marginBottom">

				<div class="formContainerWrapper">
					
					<h2><s:property value="%{getLocaleProperty('info.farmer')}" /></h2>
					<div class="flexiWrapper">
					
					<s:if test="currentTenantId=='kpf' || currentTenantId=='agro'|| currentTenantId=='simfed'|| currentTenantId=='wub'">
							<div class="flexform-item">
					<label for="txt"><s:text name="date" /><span
						class="manadatory">*</span> </label>
						<div class="form-element">
							<s:textfield name="startDate" id="startDate" readonly="true"
								theme="simple" data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								data-date-viewmode="years"
								cssClass="date-picker col-sm-3 form-control" size="20" />

					</div>
				</div>
				</s:if>
						
						<div class="flexform-item">
								<label for="txt"><s:property value="%{getLocaleProperty('city.name')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedCity" list="cityList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="city" onchange="listVillage(this)" />
						</div>
						</div>
						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('village.name')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedVillage" list="villageList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="village" onchange="listFarmer(this)" />
						</div>
						</div>

						
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('farm.farmerName')}"  /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedFarmer" list="farmerList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="farmer" onchange="listFarm(this)"/>
						</div>
						</div>
						
						<s:if test="currentTenantId=='kpf' || currentTenantId=='agro' || currentTenantId=='simfed'||currentTenantId=='wub' ">
						<div class="flexform-item">
						<label for="txt"> <s:property value="%{getLocaleProperty('farm.farmName')}"  /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedFarm" list="{}" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="farm"/>
						</div>
						</div>
						</s:if>
						<s:if test="currentTenantId!='kpf' && currentTenantId!='agro' && currentTenantId!='simfed'&& currentTenantId!='wub'">
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('warehouseProduct.product')}"/><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedProduct" list="productList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="product" onchange="listVariety(this)" />
						</div>
						</div>
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('procurement.variety')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedVariety" list="{}" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="variety" />
						</div>
						</div>
						
						<div class="flexform-item">
								<label for="txt"><s:property value="%{getLocaleProperty('cultTypee')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">`
							<s:select name="cultType" list="cultList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="cultype" />
						</div>
						</div>
						
							<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('lotNo')}" /><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:textfield name="lotNo" cssClass="form-control input-sm" id="lotNo" />
						</div>
						</div>
						</s:if>
							<s:if test="currentTenantId=='kpf' || currentTenantId=='agro'|| currentTenantId=='simfed'|| currentTenantId=='wub'">
						<div class="flexform-item">
								<label for="txt"> <s:property value="%{getLocaleProperty('warehouseProduct.warehouse')}"/><sup style="color: red;">*</sup></label>
						<div class="form-element">
							<s:select name="selectedWarehouse" list="ListWarehouse" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control input-sm" id="warehouse"  />
						</div>
						</div>
						</s:if>
						
					
					</div>

					<div class="yui-skin-sam" id="savebutton">
						<span class="yui-button"><span class="first-child">
						<button  id="saveBtn" type="button" class="save-btn btn btn-success" onclick="onSave();">
							<font color="#FFFFFF"> <b><s:text name="generateQr.button" /></b></font>
						</button>
						</span></span>
						
					</div>

				</div>
			</div>


	</s:form>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		$('#city').select2();
		$('#city').val(null).trigger('change');
		$('#lotNo').val('');
	});
	function listVillage(call){	
	
		jQuery.post("qrCodeGen_populateVillage.action",{id:call.value,dt:new Date(),selectedCity:call.value},function(result){
			$('#village').find('option').remove().end().append('<option value="">Select</option>').val('');
			$('#village').select2();
			$('#farmer').find('option').remove().end().append('<option value="">Select</option>').val('');
			$('#farmer').select2();
/* 			$('#product').find('option').remove().end().append('<option value="">Select</option>').val('');
			$('#product').select2(); */
/* 			$('#variety').find('option').remove().end().append('<option value="">Select</option>').val('');
			$('#variety').select2(); */
			insertOptions("village",$.parseJSON(result));
			//listFarmer();
		});	
	}

	function listFarmer(call){	
	jQuery.post("qrCodeGen_populateFarmer.action",{id:call.value,dt:new Date(),selectedVillage:call.value},function(result){
		$('#farmer').find('option').remove().end().append('<option value="">Select</option>').val('');
/* 		$('#product').find('option').remove().end().append('<option value="">Select</option>').val(''); */
/* 		$('#variety').find('option').remove().end().append('<option value="">Select</option>').val(''); */
			insertOptions("farmer",$.parseJSON(result));		
		});	
	}
	
	function listFarm(call){	
		jQuery.post("qrCodeGen_populateFarm.action",{id:call.value,dt:new Date(),selectedFarmer:call.value},function(result){
			$('#farm').find('option').remove().end().append('<option value="">Select</option>').val('');
				insertOptions("farm",$.parseJSON(result));		
			});	
		}
	
/* 	function listProduct(call){	
		$('#variety').find('option').remove().end().append('<option value="">Select</option>').val('');
		jQuery.post("qrCodeGen_populateProcurementProducts.action",{selectedFarmer:call.value},function(result){
				insertOptions("product",$.parseJSON(result));		
			});	
		} */
	
	function listVariety(call){	
		jQuery.post("qrCodeGen_populateProcurementVariety.action",{selectedProduct:call.value},function(result){
				insertOptions("variety",$.parseJSON(result));		
			});	
		}
	function onSave(){
		var selectedFarmer = document.getElementById('farmer').value;
		<s:if test="currentTenantId!='kpf' && currentTenantId!='agro' && currentTenantId!='simfed' && currentTenantId!='wub'">	
		
		var selectedProduct = document.getElementById('product').value;
		var selectedVariety = document.getElementById('variety').value;
		var cultType = document.getElementById('cultype').value;
		var lotNo = document.getElementById('lotNo').value;
		</s:if>
		<s:if test="currentTenantId=='kpf' || currentTenantId=='agro' || currentTenantId=='simfed'|| currentTenantId=='wub'">
		var startDate = $("#startDate").val();
		var selectedFarm = document.getElementById('farm').value;
		var selectedWarehouse = document.getElementById('warehouse').value;
		var selectedCity = document.getElementById('city').value;
		var selectedVillage = document.getElementById('village').value;
		</s:if>
		
		var hit=true;
		
		
		<s:if test="currentTenantId!='kpf' && currentTenantId!='agro' && currentTenantId!='simfed'&& currentTenantId!='wub'">
		if(selectedFarmer==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectFarmer')}" />');
			hit = false;
		}
		if(selectedProduct==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectProduct')}" />');
			hit = false;
		}
		if(cultType==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('empty.cultType')}" />');
			hit = false;
		}
		if(selectedVariety==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('empty.variety')}" />');
			hit = false;
		}
		
		if(lotNo==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('enterLotNo')}" />');
			hit = false;
		}
		</s:if>
		
		<s:if test="currentTenantId=='kpf' || currentTenantId=='agro'|| currentTenantId=='simfed'|| currentTenantId=='wub'">

	
		
		if(selectedWarehouse==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectedWarehouse')}" />');
			hit = false;
			}
		
		if(selectedFarm==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectedFarm')}" />');
			hit = false;
			}
		
		if(selectedFarmer==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectFarmer')}" />');
			hit = false;
		}
		
		if(selectedVillage==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectedVillage')}" />');
			hit = false;
		}
		
		if(selectedCity==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectedCity')}" />');
			hit = false;
		}
		
		if(startDate==''){
			$('#validateError').html('<s:property value="%{getLocaleProperty('selectedstartDate')}" />');
			hit = false;
			}
	
		</s:if>
		if(hit){
		document.form.action="qrCodeGen_create.action";
		document.form.submit();
		}
	}

	</script>

</body>
