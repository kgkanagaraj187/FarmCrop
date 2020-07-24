<%@ include file="/jsp/common/report-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

</head>

<script type="text/javascript">
	
	function onCancel() {
		document.listForm.submit();
	}

	function listCropType(obj) {
		var selectedCropType = $(obj).val();
	 	var farm= $('#farmCode').val();
	 	$("#cropType option[value='"+selectedCropType+"']").prop("selected","selected");
	 	
	 	$.ajax({
	 		 type: "POST",
	         async: false,
	         url: "cropSaleEntryReport_populateCropName.action",
	         data: {id:obj.value,dt:new Date(),selectedCropType:obj.value,selectedFarm:farm},
	         success: function(result) {
	         	insertOptions("cropName",JSON.parse(result));
	     		//listCrops(document.getElementById("cropName"));
	         }
	 	});
	}

	function listCrops(obj) {
		var selectedCropName = $(obj).val();
	 	var selectedCropType = $('#cropType').val();
	 	var selectedFarm = $('#farmCode').val();
	 	

	 	$.ajax({
	 		 type: "POST",
	        async: false,
	        url: "cropSaleEntryReport_populateVarietyName.action",
	        data: {id:obj.value,dt:new Date(),selectedCropName:obj.value,selectedCropType:selectedCropType,selectedFarm:selectedFarm},
	        success: function(result) {
	     	   insertOptions("variety",JSON.parse(result));
	    			//listVariety(document.getElementById("variety").value); 	
	        }
	 	});
	}

	function listVariety(obj) {
		var selectedVariety = $('#variety').val();
		 
	 	$.ajax({
	 		 type: "POST",
	 		 async: false,
	       url: "cropSaleEntryReport_populateGradeName.action",
	       data: {id:obj.value,dt:new Date(),selectedVariety:obj.value},
	       success: function(result) {
	     	  insertOptions("grade",JSON.parse(result));
	       }
	 	}); 
	}

	function loadUnit() {
		var selectedCrop = document.getElementById("cropName").value;
		$.post("cropSaleEntryReport_populateUnit",{selectedCrop:selectedCrop},function(data){
			var jsonData = $.parseJSON(data);
			$.each(jsonData, function(index, value) {
				if(value.id=="unit"){
					document.getElementById("productUnit").innerHTML=value.name;
				}
			});
		});
		
		/* var selectedGrade = document.getElementById("grade").value;

		var availableUnit;

		$
				.post(
						"cropSaleEntryReport_populateGradeUnit",
						{
							selectedGrade : selectedGrade
						},
						function(data) {

							var jsonData = $.parseJSON(data);
							$
									.each(
											jsonData,
											function(index, value) {
												if (value.id == "unit") {
													document
															.getElementById("productUnit").innerHTML = value.name;
												}
											});
						}); */

	}

	/* function listGrade(obj){
		var selectedGrade = $('#grade').val();
		jQuery.post("cropSaleEntryReport_populatePrice.action",{id:obj.value,dt:new Date(),selectedGrade:obj.value},function(result){
			
			$('#sellPrice').val(JSON.parse(result));
		//	insertOptions("sellPrice",JSON.parse(result));
			
		});
	} */

	$(document).ready(function() {
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		refreshTemplateList1();
		
		addHarvestDetails();
		if(tenantId=="welspun"){
			refreshTemplateList2();
			addTransportDetails();
		}
		
	});

	function addHarvestDetails() {
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		var table = document.getElementById("cropSaleTblData1");
		var rows = table.getElementsByTagName("tr");
		$("#cropSaleTblData1 tbody").append(
				"<tr class='tableTr1'id='"+rows.length+1+"'>"+
				"<td align='center'><select class='tableTd1 txtFilterSelect filSelect form-control input-sm select2' id='cropType' style='width:100px'  onchange='listCropType(this)'>"+getQuestions1("cropType")+"</select></td>"+
				"<td align='center'><select class='tableTd2 txtFilterSelect filSelect form-control input-sm select2' id='cropName'  style='width:150px' onchange='listCrops(this);loadUnit();'>"+getQuestions1("cropNamesList")+"</select></td>"+
				"<td align='center'><select class='tableTd3 txtFilterSelect filSelect form-control input-sm select2' id='variety' style='width:150px' onchange='listVariety(this)'>"+getQuestions1("varietysList")+"</select></td>"+
				"<td align='center'><select class='tableTd4 txtFilterSelect filSelect form-control input-sm select2' id='grade' style='width:150px'>"+getQuestions1("gradesList")+"</select></td>"+
				"<td align='center'><div class='tableTd5' id='productUnit'/></td>"+
				"<td align='center'><input type='text' class='tableTd6' name='textVal' maxlength='5'/></td>"+
				"<td align='center'><input type='text' class='tableTd7' name='textVal' maxlength='5'/></td>"+
				"<td align='center'><input type='text' class='tableTd8' name='textVal' size='8'  id='sellPrice' maxlength='5'/></td>"+
				
				 
				//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
				//"<td class='tableTd8'><img id='"+rows.length+1+"' class='ic-del' onclick='Delete1(this);'/></td>"+
				"</tr>");

	}
	
	
	function getQuestions1(idOption) {


		var optionContent='';
		$("#"+idOption+" option").each(function(){
			optionContent+='<option value="'+$(this).val()+'">'+$(this).text()+'</option>';
		});
	
		return optionContent;
	}

	function save1() {
		var id=$("#supplyId").val();
		var columnNamesArray = new Array();
		var columnModelArray = new Array();
		var filterParams = new Array();
		var colCount=0;
		var flag=0;
		var paramObj = {};	
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		jQuery(".tableTr1").each(function(){
		
			jQuery(this).find(".tableTd1").each(function(){
				var columnName1 = jQuery.trim(jQuery(this).find("option:selected").val());
				paramObj['supplyDetailList['+colCount+'].cropType.code'] =columnName1;
			
				if(columnName1 == '-1'){
					flag=1;
				}
			});
		
			jQuery(this).find(".tableTd2").each(function(){
				var columnModelName1 = jQuery.trim($(this).find("option:selected").val());
				paramObj['supplyDetailList['+colCount+'].crop.code'] =columnModelName1;
				
				if(columnModelName1 == '-1'){
					flag=1;
				}
			});
			
			jQuery(this).find(".tableTd3").each(function(){
				var columnName2 = jQuery.trim(jQuery(this).find("option:selected").val());
				paramObj['supplyDetailList['+colCount+'].variety.id'] =columnName2;
				
				if(columnName2 == '-1'){
					flag=1;
				}
			});
			
			jQuery(this).find(".tableTd4").each(function(){
				var columnName3 = jQuery.trim(jQuery(this).find("option:selected").val());
				paramObj['supplyDetailList['+colCount+'].grade.code'] =columnName3;
			
				if(columnName3 == '-1'){
					flag=1;
				}
			});
		
			/* jQuery(this).find(".tableTd5").each(
					function() {
						var columnName = jQuery.trim($(this).val());
						paramObj['supplyDetailList[' + colCount
								+ '].unit'] = columnName;

						if (columnName == '') {
						
							flag = 1;
						}
						
					}); */
			
					
					 jQuery(this).find(".tableTd6").each(function(){
							var columnName4 = jQuery.trim($(this).val());
							paramObj['supplyDetailList['+colCount+'].batchLotNo'] =columnName4;
							
							if(columnName4 == ''){
								flag=1;
							}			
						}); 
					
					
			 jQuery(this).find(".tableTd7").each(function(){
				var columnName5 = jQuery.trim($(this).val());
				paramObj['supplyDetailList['+colCount+'].qty'] =columnName5;
				
				if(columnName5 == ''){
					flag=1;
				}			
			}); 
			 jQuery(this).find(".tableTd8").each(function(){
					var columnName6 = jQuery.trim($(this).val());
					paramObj['supplyDetailList['+colCount+'].price'] =columnName6;
					
					if(columnName6 == ''){
						flag=1;
					}			
				});
			/*  jQuery(this).find(".tableTd7").each(function(){
					var columnName6 = jQuery.trim($(this).val());
					paramObj['supplyDetailList['+colCount+'].subTotal'] =columnName6;
					
					if(columnName6 == ''){
						flag=1;
					}		
					
				}); */
			
			colCount++;
			
		});
	
		
	     var supplyDetailList=[];
		for(var i=0;i<colCount;i++){
			var objfI1=new Object();
			if(paramObj['supplyDetailList['+i+'].cropType.code']!=undefined){
					objfI1.cropType=paramObj['supplyDetailList['+i+'].cropType.code'];
					
			}
			else{
				objfI1.cropType="";
			}
			if(paramObj['supplyDetailList['+i+'].variety.id']!=undefined){
			objfI1.varietyId=paramObj['supplyDetailList['+i+'].variety.id'];
			
			}
			else{
				objfI1.varietyId="";
			}
			if(paramObj['supplyDetailList['+i+'].crop.code']!=undefined){
				objfI1.cropId=paramObj['supplyDetailList['+i+'].crop.code'];
				
				}
				else{
					objfI1.cropId="";
				}
			if(paramObj['supplyDetailList['+i+'].grade.code']!=undefined){
			objfI1.gradeId=paramObj['supplyDetailList['+i+'].grade.code'];
			}
			else{
				objfI1.gradeId="";
			}
			
			if(paramObj['supplyDetailList['+i+'].batchLotNo']!=undefined){
				objfI1.batchLotNo=paramObj['supplyDetailList['+i+'].batchLotNo'];
				}
				else{
					objfI1.batchLotNo="";
				}
			
			if(paramObj['supplyDetailList['+i+'].qty']!=undefined){
			objfI1.qty=paramObj['supplyDetailList['+i+'].qty'];
			}
			else{
				objfI1.qty="";
			}
			if(paramObj['supplyDetailList['+i+'].price']!=undefined){
				objfI1.price=paramObj['supplyDetailList['+i+'].price'];
				}
				else{
					objfI1.price="";
				}
		 	
		
				objfI1.subTotal=(paramObj['supplyDetailList['+i+'].qty'] * paramObj['supplyDetailList['+i+'].price']);
		
			
				supplyDetailList.push(objfI1);
			
		}
		
		var postData=new Object();
		postData.supplyDeatailJsonString=JSON.stringify(supplyDetailList);
		postData.supplyId=id;
		console.log('here '+JSON.stringify(postData));
		
		//$.post("farmInventory_populateInventry",{paramObj},function(data){
		//});
		if(flag ==0){
		 $.ajax({
	         url: 'cropSaleEntryReport_populateSupplyDetail.action',
	         type: 'post',
	         dataType: 'json',
	         success: function (data) {
		  refresh1();
			 refreshTemplateList1();
	             
	         },
	         data: postData
	     });
		}else
		{
			alert('<s:text name="empty.values"/>');
			}
	}

	function refresh1() {
		$(".tableTd1").val("");
		$(".tableTd2").val("");
		$(".tableTd3").val("");
		$(".tableTd4").val("");
	
		document.getElementById("productUnit").innerHTML=" ";
		$(".tableTd6").val("");
		$(".tableTd7").val("");
		$(".tableTd8").val("");
		$(".tableTd9").val("");
		
		$(".transportDetail").val("");
		$(".vehicleNo").val("");
		$(".receiptInfor").val("");
		$(".poNumber").val("");
		$(".paymentInfo").val("");
	}

	function butDelete1(val) {
		var templateId=val;
		if(confirm('<s:text name="confirm.delete"/>')){
		  $.post("cropSaleEntryReport_deleteCropSupplyDetail.action",{templateId:templateId},
		        	function(data,status){
	      		alert('<s:text name="msg.removed"/>');
	      		refreshTemplateList1();
	      	
		        	});
	      }
	}
	jQuery(function($) {
		$("#calendarDOJ").datepicker({
		});
	});

	function insertOptions(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), "Select", "0");
		for(var i=0;i<jsonArr.length;i++){
			addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
		}
	}
	function addOption(selectbox, text, value) {
		var optn = document.createElement("OPTION");
		optn.text = text;
		optn.value = value;
		selectbox.options.add(optn);
	}

	function populateCrop(val) {
		var type=val;
		$.post("cropSaleEntryReport_populateCrop",{cropTypeCode:type},function(result){
			
		});
	}
	function Delete1(evt) {
		var par = $(evt).parent().parent(); //tr
		par.remove();
	}
	function refreshTemplateList1() {
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		$('#tBodyTemplate1').empty();
		 $('#temp').empty();
		var id=$("#supplyId").val();
		
		 $('#temp').append("<option value=''>Select Template</option>");
		
		 $.getJSON('cropSaleEntryReport_populateCropSupplyDetailList.action',{cropSupplyId:id },function(jd){
			
			 var temps=jd.data;
		
			 var bodyContent='';
			 for(var i=0;i<temps.length;i++){
				 var temp=temps[i];
				
				 bodyContent+='<tr>';
				 bodyContent+='<td class="hide">'+temp.id+'</td>';
				 bodyContent+='<td align="center">'+temp.cropType+'</td>';
				 bodyContent+='<td align="center">'+temp.crop+'</td>';
				 bodyContent+='<td align="center">'+temp.variety+'</td>';
				 bodyContent+='<td align="center">'+temp.grade+'</td>';
				 bodyContent+='<td align="center">'+ temp.unit + '</td>';
				 bodyContent+='<td align="center">'+ temp.batch + '</td>';
				 bodyContent+='<td align="center">'+temp.quantity+'</td>';
				 bodyContent+='<td align="center">'+temp.price+'</td>';
				 bodyContent+='<td align="center">'+temp.subTotal+'</td>';
				 
				 bodyContent+='<td align="center"><button type="button" class="fa fa-trash" onclick="butDelete1('+temp.id+');"/></td>';
				 bodyContent+='</tr>';
				 $('#temp').append("<option value='"+temp.id+"'>"+temp.cropType+temp.crop+temp.variety+temp.grade+"</option>");
			}
			 
			 $('#tBodyTemplate1').html(bodyContent);
			 $('#temp').val('');
		 });
	}
	function refreshTemplateList2() {
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		$('#tBodyTemplate2').empty();
		 $('#temp').empty();
		var id=$("#supplyId").val();
		
		 $('#temp').append("<option value=''>Select Template</option>");
		
		 $.getJSON('cropSaleEntryReport_populateCropSupplyList.action',{supplyId:id },function(jd){
			
			 var temps=jd.data;
		
			 var bodyContent='';
			 for(var i=0;i<temps.length;i++){
				 var temp=temps[i];
				
				 bodyContent+='<tr>';
				 bodyContent+='<td class="hide">'+temp.id+'</td>';					
				 bodyContent+='<td align="center">'+temp.transportDetail+'</td>';
				 bodyContent+='<td align="center">'+temp.vechicleNo+'</td>';
				 bodyContent+='<td align="center">'+temp.receiptInfor+'</td>';
				 bodyContent+='<td align="center">'+temp.poNumber+'</td>';
				 bodyContent+='<td align="center">'+temp.paymentInfo+'</td>';
				
				 bodyContent+='</tr>';
				
			}
			 
			 $('#tBodyTemplate2').html(bodyContent);
			 $('#temp').val('');
		 });
	}
	
	function addTransportDetails() {
		var tenantId="<s:property value='getCurrentTenantId()'/>";
		var table = document.getElementById("cropSaleTblData2");
		var rows = table.getElementsByTagName("tr");
		$("#cropSaleTblData2 tbody").append(
				"<tr class='tableTr2'id='"+rows.length+1+"'>"+
				"<td align='center'><input type='text' class='transportDetail' name='transportDetail' /></td>"+
				"<td align='center'><input type='text' class='vehicleNo' name='vehicleNo' /></td>"+
				"<td align='center'><input type='text' class='receiptInfor' name='receiptInfor'/></td>"+
				"<td align='center'><input type='text' class='poNumber' name='poNumber'/></td>"+
				"<td align='center'><input type='text' class='paymentInfo'  name='paymentInfo'/></td>" +
				//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
				//"<td class='tableTd8'><img id='"+rows.length+1+"' class='ic-del' onclick='Delete1(this);'/></td>"+
				"</tr>");

	}
	
	function save2() {
		var id=$("#supplyId").val();
		$("#sucessbtn2").prop('disabled', true);
		var transportDetail=jQuery(".transportDetail").val();
		var vehicleNo=jQuery(".vehicleNo").val();
		var receiptInfor=jQuery(".receiptInfor").val();
		var poNumber=jQuery(".poNumber").val();
		var paymentInfo=jQuery(".paymentInfo").val();
		
		var dataa = {
				supplyId:id,
				transportDetail:transportDetail,
				vehicleNo:vehicleNo,
				receiptInfor:receiptInfor,
				poNumber:poNumber,
				paymentInfo:paymentInfo
		}
		
		$.ajax({
	        url: 'cropSaleEntryReport_populateCropSupplyData.action',
	        type: 'post',
	        dataType: 'json',
	        data: dataa,
	        success: function(data) {
	        	refresh2();
	        	refreshTemplateList2();	
	        },
	        error: function(data) {
	            alert("Some Error Occured , Please Try again");
	           
	        }

	    }); 
	$("#sucessbtn2").prop('disabled', false);
	}
	function refresh2() {
	
		$(".transportDetail").val("");
		$(".vehicleNo").val("");
		$(".receiptInfor").val("");
		$(".poNumber").val("");
		$(".paymentInfo").val("");
	}

	
	
	/* 
	 function listFarmer(obj){
	 var selectedVill = $('#villageId').val();
	 jQuery.post("farmer_populateState.action",{id:obj.value,dt:new Date(),selectedCountry:obj.value},function(result){
	 insertOptions("state",$.parseJSON(result));
	 listLocality(document.getElementById("state"));
	 });
	 } */
</script>
<body>
	
	<s:form name="form" cssClass="fillform"
		action="cropSaleEntryReport_list">
		<s:hidden key="currentPage" />
		<s:hidden key="filter.id" id="supplyId" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="cropHarvest.id" />
		</s:if>
		<s:hidden key="command" />

		<div class="flex-view-layout">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
					<div class="error">
		<s:actionerror />
		<s:fielderror />
		<sup>*</sup>
		<s:text name="reqd.field" />
	</div>

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
									<s:text name="dateOfSale" />
								</p>
								<p class="flexItem">
									<s:date name="filter.dateOfSale" format="dd/MM/yyyy"/>
								</p>
							</div>
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmerName" />
								</p>
								<p class="flexItem">
									<s:property value="filter.farmerName" /> - <s:property value="filter.farmerId" /> 
								</p>
							</div>
							
							
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmName" />
								</p>
								<p class="flexItem">
									<s:property value="filter.farmName" /> - <s:property value="filter.farmCode" />
								</p>
							</div>
							<s:if test="currentTenantId=='chetna'">
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="batch" />
									</p>
									<p class="flexItem">
										<s:property value="batchNo" />
									</p>
								</div>
								
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('transporterName')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.transportDetail" />
								</p>
							</div>
						
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('buyerName')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.buyerInfo.customerName" />
								</p>
							</div>
							
						<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('vechileNo')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.vehicleNo" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('receipt')}" />
								</p>
								<p class="flexItem">
									<s:property value="filter.invoiceDetail" />
								</p>
							</div>
						</s:if>
							<s:if test='branchId==null'>
							<div class="dynamic-flexItem">
								<p class="flexItem">
								<s:text name="branch" />
								</p>
								<p class="flexItem">
									<s:property value="filter.branchId" />
								</p>
							</div>
							</s:if>
						</div>
						<div class="formContainerWrapper dynamic-form-con">
						<table>
		<tr>
			<td>
	  <s:select name="cropType" cssClass="hide" list="cropTypeList"	headerKey="-1" headerValue="%{getText('txt.select')}" 
		theme="simple" id="cropType"  onchange="populateCrop(this.value)"/>
		
<s:select name="crop"  cssClass="hide" list="cropList" headerKey="-1" headerValue="%{getText('txt.select')}" style="width: 136px"
			theme="simple" id="cropLists" onchange="populateVariety(this.value)" />
			
<s:select name="variety" cssClass="hide" list="varietyList" headerKey="-1" headerValue="%{getText('txt.select')}" 
			theme="simple" id="varietyLists" onchange="populateGrade(this.value)"/>
			
<s:select name="grade" cssClass="hide" list="gradeList" headerKey="-1" headerValue="%{getText('txt.select')}" 
			theme="simple" id="gradeLists" />
			
		<%-- 	<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
		onclick="addHarvestDetails()" /> --%>
		
</td>
</tr>
</table>		
	<table id="cropSaleTblData1" class="table table-bordered aspect-detail">		
		
		<thead>
		
			<tr>
				<th ><s:text name="cropType"/></th>
				<th ><s:text name="cropName"/></th>
				<th><s:text name="variety"/></th>
				<th><s:text name="grade"/></th>
				<th><s:text name="unit"/></th>
				<th><s:text name="batch"/></th>
				<th><s:text name="%{getLocaleProperty('quantity.name')}"/></th>
				<th><s:text name="cropsale.price"/></th>
			
		 	<!-- <th>Sub Total</th>  -->
				
			</tr>
		</thead>
		<tbody id="tableBody1">
		</tbody>
	</table>
	
	
	
	<div class="yui-skin-sam">

		<span id="button1" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"  onclick="save1();" class="save-btn"><font
			color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>

		</span></span>
		
		
		</div>
	<table  id="tableTemplate1" style="margin-top:20px; " class="table table-bordered aspect-detail">
	
	<thead>
		<tr>
			<th colspan="15"><s:text name="info.cropSaleDetails" /></th>
		</tr>
		
		<tr class="border-less">
			<th class="hide">Id</th>
			<th><s:text name="cropType"/></th>
				<th><s:text name="cropName"/></th>
				<th><s:text name="variety"/></th>
				<th><s:text name="grade"/></th>
				<th><s:text name="unit"/></th>
				<th><s:text name="batch"/></th>
				<th><s:text name="%{getLocaleProperty('quantity.name')}"/></th>
				<th><s:text name="cropsale.price"/></th>
				<th><s:text name="subTotal"/></th>
			
			<th><s:text name="delete"/></th>
		</tr>
	</thead>
	<tbody id="tBodyTemplate1">
	</tbody>
</table>
	<br />
<br/>

<s:if test="currentTenantId=='welspun'">
<table id="cropSaleTblData2" class="table table-bordered aspect-detail">		
		
		<thead>
		<tr>
			<th colspan="15"><s:text name="info.cropSale" /></th>
		</tr>
			<tr>
				
			
					<th><s:text name="transporterName" /></th>
				<th><s:text name="vechileNo" /></th>
				<th><s:text name="receipt" /></th>
				<th><s:text name="%{getLocaleProperty('poNumber')}"/></th>
				<th><s:text name="%{getLocaleProperty('paymentInfo')}"/></th>
				
		 	<!-- <th>Sub Total</th>  -->
				
			</tr>
		</thead>
		<tbody id="tableBody2">
		</tbody>
	</table>
	
	
	
	<div class="yui-skin-sam">

		<span id="button1" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success" id="sucessbtn2"  onclick="save2();" class="save-btn"><font
			color="#FFFFFF"> <b><s:text name="save.button" /></b> </font></button>

		</span></span>
		
		
		</div>
		
		<table  id="tableTemplate2" style="margin-top:20px; " class="table table-bordered aspect-detail">
	
	<thead>
		
		
		<tr class="border-less">
			<th class="hide">Id</th>
		
				
				<th><s:text name="transporterName" /></th>
				<th><s:text name="vechileNo" /></th>
				<th><s:text name="receipt" /></th>
				<th><s:text name="%{getLocaleProperty('poNumber')}"/></th>
				<th><s:text name="%{getLocaleProperty('paymentInfo')}"/></th>
			
			
		</tr>
	</thead>
	<tbody id="tBodyTemplate2">
	</tbody>
</table>	




</s:if>


	<div class="yui-skin-sam"><s:if test="command =='create'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="add.button" /></b> </font></button>
		</span></span>
		</s:if> <s:elseif test="command =='update'">
		<span id="button" class=""><span class="first-child">
		<button type="button" class="save-btn btn btn-success"><font color="#FFFFFF"> <b><s:text
			name="update.button" /></b> </font></button>
		</span></span></s:elseif>


		<span id="cancel" class=""><span class="first-child"><button type="button"  onclick="onCancel();" class="back-btn btn btn-sts">
              <b><FONT color="#FFFFFF"><s:text name="cancel.button"/>
                </font></b></button></span></span>

	</div>
					</div>
				</div>
			</div>
		</div>


</div>

	
	</s:form>

	<s:hidden id="farmCode" name="filter.farmCode" />
	<s:form name="listForm" id="listForm" action="cropSaleEntryReport_list">
		<s:hidden name="currentPage" />
	</s:form>

</body>