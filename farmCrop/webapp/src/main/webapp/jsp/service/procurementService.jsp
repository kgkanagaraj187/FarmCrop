<%@ include file="/jsp/common/report-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<script type="text/javascript">
var tenant='<s:property value="getCurrentTenantId()"/>';
var enableSupplier = "<s:property value='enableSupplier'/>";
function isEmpty(val){
	  if(val==null||val==undefined||val.toString().trim()==""){
	   	return true;
	  }
	  return false;
	}
	jQuery(document).ready(function() {
		
		refreshTemplateList1();
		addHarvestDetails();
		if(tenant=='lalteer')
		{
			jQuery("#grade").hide();
			jQuery("#price").hide();
			jQuery("#dryLos").show();
			jQuery("#grading").show();
			jQuery("#netWght").show();
			jQuery("#grossWeight").show();
			
			
			
		}
		else
			{
			jQuery("#dryLos").hide();
			jQuery("#grading").hide();
			jQuery("#netWght").hide();
			}
		
		
		var regFarmer = '<s:property value="procurement.procMasterType"/>';
		var farmer = '<s:property value="procurement.farmer"/>';
		if ((regFarmer == "-1" || regFarmer == "99") && farmer != '') {
			jQuery(".reg").removeClass('hide');
			jQuery(".unreg").hide();
		} else {
			jQuery(".unreg").removeClass('hide');
			jQuery(".reg").hide();
		}
		

		if(tenant == "efk"){
			var vehicleTypeAnswer = '<s:property value="procurement.vehicleType"/>';
			if (vehicleTypeAnswer != null && vehicleTypeAnswer.trim() != "") {
			       $.each(vehicleTypeAnswer.split(","), function (i, e) {
			        $("#vehicleType option[value='" + e.trim() + "']").prop("selected", true);
			     });
			       $("#vehicleType").select2();
			}
		}
		
	});

	
	function isDecimal(evt) {
		
		 evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
		        return false;
		    }
		    return true;
	}

	function isNumber(evt) {
		
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;
	}
	function onCancel() {
		document.listForm.submit();
	}

	function listProVarierty(value) {
	
		resetPrice();

		var selectedPro = value;

		$.post("procurementProduct_populateEditVariety", {
			selectedPro : selectedPro
		}, function(data) {
			$('#varietyList').empty();
			
			var result = data;
			result = result.replace("{", "").replace("}", "");
			var farmersArr = result.split(", ");
			//addOption(document.getElementById("varietyList"),'<s:text name="txt.select"/>','');
			for (var i = 0; i < farmersArr.length; i++) {
				var arr = farmersArr[i].split("=");
				if (arr != "") {
					addOption(document.getElementById("varietyList"), arr[1],
							arr[0]);
				}

			}
			
			//$("#varietyList").val(selectedPro);
			//$('#varietyList').trigger('change');

		});

	}
	
	function resetPrice() {
		jQuery("#pricePerUnitLabel").html("0.00");
	}

	function listProGrade(value) {
		resetPrice();
		var selectedVar = value;
		$.post("procurementProduct_populateEditGrade", {
			selectedVar : selectedVar
		}, function(data) {
			$('#gradeList').empty();
			
			var result = data;
			result = result.replace("{", "").replace("}", "");
			var farmersArr = result.split(", ");
			addOption(document.getElementById("gradeList"),'<s:text name="txt.select"/>','');
			for (var i = 0; i < farmersArr.length; i++) {
				var arr = farmersArr[i].split("=");
				if (arr != "") {
					addOption(document.getElementById("gradeList"), arr[1],
							arr[0]);
					
						}
			}

		});

	}

	function getPrice(grade) {
		
		var selectedProduct = jQuery('#productHide').val();
		var selectedVariety = jQuery.trim(jQuery("#varietyList").val());
		var selectedGradeValue = grade;
		$.post("procurementProduct_populatePrice", {
			procurementProductId : selectedProduct,
			procurementVarId : selectedVariety,
			procurementGradeId : selectedGradeValue
		}, function(data) {

			document.getElementById('pricePerUnitLabel').value = "";
			jQuery("#pricePerUnitLabel").html(parseFloat(data).toFixed(2));
			
			/* document.getElementById('subTotal').value = "";
			jQuery("#subTotal").html(parseFloat(data).toFixed(2)); */

		});

	}

	function getGrade() {
		var selectedVar = call.value;
		$.post("procurementProduct_populateGrade", {
			selectedVar : selectedVar
		}, function(data) {

			document.getElementById('gradeLabel').value = "";

			jQuery("#gradeLabel").html(data);

		});

	}
/* 	function netWeight(){	
		var dryLoss=$("#dryLoss").val();
		var gradingLoss=$("#gradingLoss").val();
		var grossWeight=$("#grossWeight").val();
		
		
	     
	} */
	
function calcNetWeight(){	
		
		var dryLoss = jQuery("#dryLoss").val();
		var gradeLoss = jQuery("#gradingLoss").val();
		var grossWeight=$("#grossWeight").val();
		var totalLoss = parseFloat(dryLoss).toFixed(2)+ parseFloat(gradingLoss).toFixed(2);
		var totalNetWeight=parseFloat(grossWeight).toFixed(2) - parseFloat(totalLoss).toFixed(2);
		
		
		if(parseFloat(grossWeight)<=parseFloat(dryLoss) || parseFloat(grossWeight)<=parseFloat(gradeLoss) ||  parseFloat(grossWeight)<=parseFloat(gradeLoss)+parseFloat(dryLoss)){
			netWeight = 0;
			$("#netWeight").text(totalNetWeight);
			jQuery("#validateError").text('<s:property value="%{getLocaleProperty('weight.product')}" /> ');
			jQuery("#dryLoss").val('0.00');
			jQuery("#dryLoss").val('0.00');
		}
		
		if(grossWeight>0){
			if(!isEmpty(dryLoss)){
				grossWeight = grossWeight - dryLoss;
				totalNetWeight=grossWeight.toFixed(2);
			}
			
			if(!isEmpty(gradeLoss)){
				grossWeight = grossWeight - gradeLoss;
				totalNetWeight=grossWeight.toFixed(2);
			}
		    
			$("#netWeight").val(totalNetWeight);
			document.getElementById("netWeight").disabled = true;
		}
		
	}
	
	function addHarvestDetails() {
		var table = document.getElementById("tblData1");
		var rows = table.getElementsByTagName("tr");
		if(tenant!='pratibha'){
		$("#tblData1 tbody")
				.append(
						"<tr class='tableTr1'id='"+rows.length+1+"'>"
						+ "<td align='center'><label id='productLabel'></label></td>"
						+ "<td align='center'><label id='varietyLabel'></label></td>"
						+ "<td align='center' id='grade'><select style='width:150px'  class='tableTd2 txtFilterSelect filSelect form-controm input-sm' id='gradeList'  style='width:100px' >"
						+ "</select></td>"
						//+ "<td align='center' id='price'><label id='pricePerUnitLabel'/></td>"
					   + "<td align='center'><input type='text' class='tableTb11' id='batchNo' name='textVal' maxlength='5'/></td>"

						+ "<td align='center'><label id='proUnitVal'></label></td>"
						
						+ "<td align='center'><input type='text' class='tableTd3' id='bags' name='textVal' maxlength='5' onkeypress='return isNumber(event)' /></td>"
						//+ "<td align='center'><input type='text' class='tableTd10' id='fruitBags' name='textVal' maxlength='5' onkeypress='return isNumber(event)' /></td>"
						+ "<td align='center'><input type='text' class='tableTd4' id='grossWeight' name='textVal' maxlength='6' onkeypress='return isDecimal(event)' onchange='calcNetWeight();'/></td>"
						+ "<td align='center' id='dryLos'><input type='text' class='tableTd5' id='dryLoss' name='dryLoss' maxlength='5' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
						+ "<td align='center' id='grading'><input type='text' class='tableTd6' id='gradingLoss' name='gradingLoss' maxlength='4' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
						+ "<td align='center' id='netWght'><input type='text' class='tableTd7' id='netWeight' name='netWeight'  disabled/></td>"
						+ "<td align='center' id='ratePerUnit'><input type='text' class='tableTd8' id='ratePerUnit' name='ratePerUnit'  disabled/></td>"
						+ "<td align='center' id='subTotal'><input type='text' class='tableTd9' id='subTotal' name='subTotal'  disabled/></td>"
						+"<td align='center'><button type='button' class='btn btn-sm btn-success' aria-hidden='true' onclick=save1() title='<s:text name="Save" />'><i class='fa fa-check'></i></button></td>"
						+				
						//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
						//"<td class='tableTd8'><img id='"+rows.length+1+"' class='ic-del' onclick='Delete1(this);'/></td>"+
						"</tr>");
		}
		else if(tenant=='awi'){
			alert("inside");
			$("#tblData1 tbody")
			.append(
					"<tr class='tableTr1'id='"+rows.length+1+"'>"
							+ "<td align='center'><label id='productLabel'></label></td>"
							+ "<td align='center'><label id='varietyLabel'></label></td>"
							+ "<td align='center' id='grade'><select style='width:150px'  class='tableTd2 txtFilterSelect filSelect form-controm input-sm' id='gradeList'  style='width:100px' >"
							+ "</select></td>"
							+ "<td align='center'><label id='proUnitVal'></label></td>"
							
							+ "<td align='center'><input type='text' class='tableTd3' id='bags' name='textVal' maxlength='5' onkeypress='return isNumber(event)' /></td>"
							+ "<td align='center'><input type='text' class='tableTd10' id='fruitBags' name='textVal' maxlength='5' onkeypress='return isNumber(event)' /></td>"
							
							+ "<td align='center'><input type='text' class='tableTd4' id='grossWeight' name='textVal' maxlength='6' onkeypress='return isDecimal(event)' onchange='calcNetWeight();'/></td>"
							+ "<td align='center' id='dryLos'><input type='text' class='tableTd5' id='dryLoss' name='dryLoss' maxlength='5' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
							+ "<td align='center' id='grading'><input type='text' class='tableTd6' id='gradingLoss' name='gradingLoss' maxlength='4' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
							+ "<td align='center' id='netWght'><input type='text' class='tableTd7' id='netWeight' name='netWeight'  disabled/></td>"
							+ "<td align='center' id='ratePerUnit'><input type='text' class='tableTd8' id='ratePerUnit' name='ratePerUnit'  disabled/></td>"
							+ "<td align='center' id='subTotal'><input type='text' class='tableTd9' id='subTotal' name='subTotal'  disabled/></td>"
							+"<td align='center'><button type='button' class='btn btn-sm btn-success' aria-hidden='true' onclick=save1() title='<s:text name="Save" />'><i class='fa fa-check'></i></button></td>"
							+				
							"</tr>");
			}
		else{
			$("#tblData1 tbody")
			.append(
					"<tr class='tableTr1'id='"+rows.length+1+"'>"
							+ "<td align='center'><label id='productLabel'></label></td>"
							+ "<td align='center'><label id='varietyLabel'></label></td>"
							+ "</select></td>"
							+ "<td align='center' id='grade'><select style='width:150px'  class='tableTd2 txtFilterSelect filSelect form-controm input-sm' id='gradeList'  style='width:100px' >"
							+ "</select></td>"
							//+ "<td align='center' id='price'><label id='pricePerUnitLabel'/></td>"
							+ "<td align='center'><label id='proUnitVal'></label></td>"
							+ "<td align='center'><input type='text' class='tableTd4' id='grossWeight' name='textVal' maxlength='6' onkeypress='return isDecimal(event)' onchange='calcNetWeight();'/></td>"
							+ "<td align='center' id='dryLos'><input type='text' class='tableTd5' id='dryLoss' name='dryLoss' maxlength='5' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
							+ "<td align='center' id='grading'><input type='text' class='tableTd6' id='gradingLoss' name='gradingLoss' maxlength='4' onkeypress='return isDecimal(event)' onchange='calcNetWeight();' /></td>"
							+ "<td align='center' id='netWght'><input type='text' class='tableTd7' id='netWeight' name='netWeight'  disabled/></td>"
							+ "<td align='center' id='ratePerUnit'><input type='text' class='tableTd8' id='ratePerUnit' name='ratePerUnit'  disabled/></td>"
							+ "<td align='center' id='subTotal'><input type='text' class='tableTd9' id='subTotal' name='subTotal'  disabled/></td>"
							+"<td align='center'><button type='button' class='btn btn-sm btn-success' aria-hidden='true' onclick=save1() title='<s:text name="Save" />'><i class='fa fa-check'></i></button></td>"
							+				
							//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
							//"<td class='tableTd8'><img id='"+rows.length+1+"' class='ic-del' onclick='Delete1(this);'/></td>"+
							"</tr>");
		}

	}
	function getQuestions1(idOption) {

		var optionContent = '';
		$("#" + idOption + " option").each(
				function() {
					optionContent += '<option value="' + $(this).val() + '">'
							+ $(this).text() + '</option>';
				});

		return optionContent;
	}

	function save1() {	
		
		document.getElementById("validateError").innerHTML="";
		var procurementDetailId=$("#procurementDetailId").val();
		var bags=$("#bags").val();
		var fruitBags=$("#fruitBags").val();
		var dryLoss=$("#dryLoss").val();
		var gradingLoss=$("#gradingLoss").val();
		var grossWeight=$("#grossWeight").val();
		var netWeight=$("#netWeight").val();
		//var paymentAmount = $("#payamount").val();
		var batchNo=$("#batchNo").val();
	    ///alert(batchNo);
		//$.post("farmInventory_populateInventry",{paramObj},function(data){
		//});
		 if(dryLoss==""){
				dryLoss=0;
				
			}
			if(gradingLoss==""){
				gradingLoss=0;
			}
		var lossTotal=  parseFloat(dryLoss) + parseFloat(gradingLoss);
		//lossTotal=lossTotal.toFixed(2);
	//alert(lossTotal);
	   if(batchNo=="")
		 {
			 document.getElementById("validateError").innerHTML ='<s:property value="%{getLocaleProperty('emptyBatchNo')}" />';
			jQuery("#bacthNo").focus();
			 return false;
		 }
		  if(grossWeight<=0)
		 {
			 document.getElementById("validateError").innerHTML ='<s:property value="%{getLocaleProperty('empty.grossWeight')}" />';
			jQuery("#grossWeight").focus();
			 return false;
		}
		 else  if(parseFloat(bags) >= parseFloat(grossWeight))
		 {
			// alert("AAA");
		 
			 document.getElementById("validateError").innerHTML = '<s:property value="%{getLocaleProperty('invalidNetWeightCalculation')}" />';
			jQuery("#grossWeight").focus();
			 return false;
		 }
		 else if(grossWeight < lossTotal ) 
		 {	
			// alert("NNN");
		
			 document.getElementById("validateError").innerHTML= '<s:property value="%{getLocaleProperty('invalidNetWeightCalculation')}" />'
			 return false;
		 }
		var result =  parseFloat(grossWeight - lossTotal).toFixed(3);	
		//document.getElementById("netWeightLabel").innerHTML = result;	 
		var error=document.getElementById("validateError").innerHTML;
		document.getElementById("procurementDetailArray").value=bags+
		"###"+grossWeight+"###"+procurementDetailId+"###"+dryLoss+"###"+gradingLoss+"###"+fruitBags+"###"+batchNo;
		var procurementDetail=document.getElementById("procurementDetailArray").value;
		 if(error=="")
			 {
			// alert("VVVV");
			 $("#sucessbtn").prop('disabled', true);
				$.post("procurementProduct_populateProcurementDetails.action", {
				procurementDetailArray : procurementDetail
				//paymentAmount:paymentAmount
				}, function(data, status) {
				refreshTemplateList1();
				refresh1();
						
			});
		 }
		
		 $("#tblData1").hide();
		
		  			
	}
	
	function savePaymentAmt(){		
		var transport_cost = $("#transportCost").val();
		var vehicleType = ""+$("#vehicleType").val();
		document.getElementById("validateError").innerHTML="";

		var error=document.getElementById("validateError").innerHTML;
		var paymentAmount = $("#payamount").val();
		var id = $("#procurementId").val();
		// $("#sucessbtn").prop('disabled', true);
		if(error==""){
			$.post("procurementProduct_populatePaymentAmt.action", {
			paymentAmount:paymentAmount,
			id:id,
			transport_cost:transport_cost,
			vehicleType:vehicleType
			
			
			}, function(data, status) {
			//refreshTemplateList1();
			refresh1();
			document.listForm.submit();
		});
		}else{
			alert(error)
		}
		//document.listForm.submit();
			//document.form.submit();
	}

	function refresh1() {		
		$(".tableTd1").val("");
		$(".tableTd2").val("");
		$(".tableTd3").val("");
		$(".tableTd4").val("");
		$(".tableTd5").val("");
		$(".tableTd6").val("");
		$(".tableTd7").val("");
		$(".tableTd8").val("");
		$(".tableTd9").val("");
		$(".tableTd10").val("");
		$(".tableTd11").val("");
		$("#netWeight").val("");
	}


	function onEdit(val) {
		//alert(jQuery("#variety"+val+"").text());
		//$('#varietyList').empty();
		jQuery("#tblData1").show();
			
			 var productVal = jQuery("#product"+val+"").text();
			jQuery("#productLabel").text(productVal); 	
			 var varietyValues = jQuery("#variety"+val+"").text();
			 jQuery("#varietyLabel").text(varietyValues);
			var varietyVal = jQuery("#variety"+val+"").text()+" # "+jQuery("#varietyCode"+val+"").text();	
			/* alert(varietyVal);
			jQuery("#varietyList").val(varietyVal); 
			document.getElementById("varietyList").disabled = true; */
			
			var proUnitVal =jQuery("#productUnit"+val+"").text();// document.getElementById("productUnit").innerHTML;
			jQuery("#proUnitVal").text(proUnitVal); 	
			
			 var gradeVal=jQuery("#grade"+val+"").text()+" - "+jQuery("#gradeCode"+val+"").text();
			 //alert("gradeVal:"+gradeVal);
			 
			setGrade(varietyVal,gradeVal);
		    //jQuery("#gradeList").val(gradeVal);
			document.getElementById("gradeList").disabled = true; 
			
			
 			jQuery("#batchNo").val(jQuery("#batchNo"+val+"").text());
			jQuery("#bags").val(jQuery("#bags"+val+"").text());
			jQuery("#fruitBags").val(jQuery("#fruitBags"+val+"").text());
			jQuery("#grossWeight").val(parseFloat(jQuery("#grossWeight"+val+"").text()).toFixed(2));
			jQuery("#dryLoss").val(jQuery("#dryLoss"+val+"").text());
			jQuery("#gradingLoss").val(jQuery("#gradingLoss"+val+"").text());
			jQuery("#netWeight").val(parseFloat(jQuery("#netWeight"+val+"").text()).toFixed(2));
			var subTotal =jQuery("#subTotal"+val+"").text();// document.getElementById("productUnit").innerHTML;
			jQuery("#subTotal").text(subTotal); 
			
			var ratePerUnit =jQuery("#ratePerUnit"+val+"").text();// document.getElementById("productUnit").innerHTML;
			jQuery("#ratePerUnit").text(ratePerUnit); 
			
			jQuery("#procurementDetailId").val(val);
			getPrice(gradeVal);
			
			//var num=parseFloat(jQuery("#grossWeightGmTxt").val());
			// alert(num.toFixed(2))
			
			
			
		}
		
		function setGrade(value,gradeVal){
			resetPrice();
			var selectedVar = value;
			$.post("procurementProduct_populateEditGrade", {
				selectedVar : selectedVar
			}, function(data) {
				var result = data;
				var arry = populateValues(result);
				document.form.gradeList.length = 0;
				addOption(document.getElementById("gradeList"),
						'<s:text name="txt.select"/>', "");
				for (var i = 0; i < arry.length; i++) {
					if (arry[i] != "") {
						addOption(document.getElementById("gradeList"), arry[i],
								arry[i]);
					}
				}
				jQuery("#gradeList").val(gradeVal);
				$('#gradeList').trigger('change');
			});
		}
		
		jQuery(function($) {
			$("#calendarDOJ").datepicker({});
		});
	function insertOptions(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), "Select", "0");
		for (var i = 0; i < jsonArr.length; i++) {
			addOption(document.getElementById(ctrlName), jsonArr[i].name,
					jsonArr[i].id);
		}
	}
	function addOption(selectbox, text, value) {
		var optn = document.createElement("OPTION");
		optn.text = text;
		optn.value = value;
		selectbox.options.add(optn);
	}
	function populateCrop(val) {
		var type = val;
		$.post("cropSaleEntryReport_populateCrop", {
			cropTypeCode : type
		}, function(result) {

		});
	}
	function Delete1(evt) {
		var par = $(evt).parent().parent(); //tr
		par.remove();
	}
	function refreshTemplateList1() {

		$('#tBodyTemplate1').empty();
		$('#temp').empty();
		var id = $("#procurementId").val();

		$('#temp').append("<option value=''>Select Template</option>");

		$.getJSON('procurementProduct_populateProcurementDetailList.action',{procurementId : id},function(jd) {
							var temps = jd.data;
							var productId="";
							var productName="";
							var varietyId="";
							var variety="";
							var productUnit="";
							var price="";
							var subTotal="";
							var batchNo="";
							var bodyContent = '';
							for (var i = 0; i < temps.length; i++) {
								var temp = temps[i];
								productId=temp.productId;
								productName=temp.product;
								varietyCode=temp.varietyCode
								variety=temp.variety
								productUnit=temp.productUnit
								ratePerUnit = temp.ratePerUnit;
								subTotal = temp.subTotal;
								batchNo=temp.batchNo;
								jQuery("#productHide").val(productId);
								bodyContent += '<tr id="'+temp.id+'">';
								bodyContent += '<td style="width: 20%" class="hide">' + temp.id+ '</td>';
								bodyContent += '<td style="width: 20%" class="hide" id="varietyCode'+temp.id+'">' + temp.varietyCode+ '</td>';
								bodyContent += '<td style="width: 25%" class="hide" id="gradeCode'+temp.id+'">' + temp.gradeCode+ '</td>';
								bodyContent += '<td style="width: 10%" class="hide" id="productId'+temp.id+'">'+ temp.productId + '</td>';
								bodyContent += '<td style="width: 10%" align="center" id="product'+temp.id+'">'+ temp.product + '</td>';								
								bodyContent += '<td style="width: 10%" align="center" id="variety'+temp.id+'">'+ temp.variety + '</td>';
								if(tenant!='lalteer')
									{
									bodyContent += '<td style="width: 10%" align="center" id="grade'+temp.id+'">'+ temp.grade + '</td>';
									//bodyContent += '<td style="width: 10%" align="center" id="price'+temp.id+'">'+ temp.price + '</td>';

									}
								bodyContent += '<td style="width: 10%" align="center" id="batchNo'+temp.id+'">'+ temp.batchNo + '</td>';

								bodyContent += '<td style="width: 10%" align="center" id="productUnit'+temp.id+'">'+ temp.productUnit + '</td>';
								if(tenant!='pratibha'){
								bodyContent += '<td style="width: 10%" align="center" id="bags'+temp.id+'">'+ temp.bags + '</td>';
							}
								if(tenant=='awi'){
									bodyContent += '<td style="width: 10%" align="center" id="fruitBags'+temp.id+'">'+ temp.fruitBags + '</td>';
								}
								bodyContent += '<td style="width: 10%" align="center" id="grossWeight'+temp.id+'">'+ temp.grossWeight + '</td>';
								if(tenant=='lalteer')
								{
									bodyContent += '<td style="width: 10%" align="center" id="dryLoss'+temp.id+'">'+ temp.dryLoss + '</td>';
									bodyContent += '<td style="width: 10%" align="center" id="gradingLoss'+temp.id+'">'+ temp.gradingLoss+ '</td>';
									bodyContent += '<td style="width: 10%" align="center" id="netWeight'+temp.id+'">'+ temp.netWeight + '</td>';
								}
								bodyContent += '<td style="width: 10%" align="center" id="ratePerUnit'+temp.id+'">'+ temp.ratePerUnit + '</td>';
								bodyContent += '<td style="width: 10%" align="center" id="subTotal'+temp.id+'">'+ temp.subTotal + '</td>';
								bodyContent += '<td style="width: 10%" align="center"><button type="button" class="fa fa-pencil" onclick="onEdit('+ temp.id+ ');"/></td>';
								bodyContent += '</tr>';
								if(tenant!='pratibha'){
								$('#temp').append("<option value='"+temp.id+"'>"+ temp.variety + temp.grade+ temp.price + temp.bags+ "</option>");
								}
								else if(tenant=='awi'){
									$('#temp').append("<option value='"+temp.id+"'>"+ temp.variety + temp.grade+ temp.price + temp.bags+temp.fruitBags+ "</option>");
								}
								else{
									$('#temp').append("<option value='"+temp.id+"'>"+ temp.variety + temp.grade+ temp.price + "</option>");
								}
							}
							//jQuery("#productLabel").text(productName);
							
							jQuery("#productHide").val(productId);
							
							//listProVarierty(productId);
							//listProGrade(varietyCode);
							$('#tBodyTemplate1').html(bodyContent);
							$('#temp').val('');
						});
	}
	 function isDecimal(evt) {
 		
    	 evt = (evt) ? evt : window.event;
    	    var charCode = (evt.which) ? evt.which : evt.keyCode;
    	    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
    	        return false;
    	    }
    	    return true;
    }
	
</script>
<body>
	<div class="error">
		<s:actionerror />
		<s:fielderror />
		<sup>*</sup>
		<s:text name="reqd.field" />
	</div>
	<div id="validateError" style="color: red; padding-bottom: 20px;"
		align="center"></div>
	<s:form name="form" cssClass="fillform"
		action="procurementProduct_list">
		<s:hidden key="currentPage" />
		<s:hidden key="id" id="procurementId" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="cropHarvest.id" />
		</s:if>
		<s:hidden key="command" />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered fillform">
				<tr>
					<th colspan="4"><s:text name="info.procurement" /></th>
				</tr>
				<tr>
					<s:if test="procurement.agroTransaction.agentName!=null">
						<td style="width: 25%"><s:text name="agent" /></td>
						<td style="width: 25%"><s:property
								value="procurement.agroTransaction.agentId" />-<s:property
								value="procurement.agroTransaction.agentName" /></td>
					</s:if>

					<td style="width: 25%"><s:text name="warehouse" /></td>
					<td style="width: 25%"><s:property
							value="procurement.warehouseName" /></td>
					
				</tr>
				<tr>
				
						<td style="width: 25%"><s:text name="IsRegistered" /></td>
						<td style="width: 25%" id='isRegistered'><span class="reg hide"><s:text name="yes" /></span><span class="unreg hide"><s:text name="no" /></span></td>
									
						<td style="width: 25%"><s:property
									value="%{getLocaleProperty('farmerCodeAndName')}" /></td>
						<td style="width: 25%"><s:property value="procurement.farmer.farmerCodeAndName" /></td>
					
				
				</tr>
				
				<tr>
					<td style="width: 25%"><s:property
							value="%{getLocaleProperty('profile.location.municipality')}" /></td>
					<td style="width: 25%"><s:property
							value="procurement.village.city.code" />-<s:property
							value="procurement.village.city.name" /></td>

					<td style="width: 25%"><s:property
							value="%{getLocaleProperty('village.name')}" /></td>
					<td style="width: 25%"><s:property
							value="procurement.village.code" />-<s:property
							value="procurement.village.name" /></td>
				</tr>
				<tr>
				<s:if test="currentTenantId!='efk'">
							<s:if test="procurement.procMasterType==99">
								<s:if test="currentTenantId!='crsdemo'">
								<td style="width: 25%"><s:text name="supplier" /></td>
								<td style="width: 25%"><s:property value="procurement.agroTransaction.farmerName" /></td>
								
								<td style="width: 25%"><s:text name="supplierType" /></td>
								<td style="width: 25%">	<s:property value="supplierMaster" /></td>
								
								</s:if><s:else>
								<td style="width: 25%"><s:text name="supplier" /></td>
								<td style="width: 25%"><s:property value="supplierType" /></td>
								
								<td style="width: 25%"><s:text name="supplierType" /></td>
								<td style="width: 25%">	<s:property value="supplierMaster" /></td>
								
								</s:else>
							</s:if>
				</s:if>

				</tr>
				<tr>
				
				<td style="width: 25%"><s:text name="createdUser" /></td>
					<td style="width: 25%"><s:property
							value="procurement.createdUser" /></td>
							
							<td style="width: 25%"><s:text name="createdDate" /></td>
					<td style="width: 25%"><s:date name="procurement.createdDate"
							format="dd/MM/yyyy" /></td>
				</tr>
				<tr>
					<td style="width: 25%"><s:text name="updatedUser" /></td>
					<td style="width: 25%"><s:property
							value="procurement.updatedUser" /></td>
					<td style="width: 25%"><s:text name="updatedDate" /></td>
					<td style="width: 25%"><s:date name="procurement.updatedDate"
							format="dd/MM/yyyy" /></td>
				</tr>

				
				<s:if test="getCurrentTenantId()=='awi'">
					<tr>
						<td style="width: 25%"><s:text name="roadMapCode" /></td>
						<td style="width: 25%"><s:property
								value="procurement.roadMapCode" /></td>

						<td style="width: 25%"><s:text name="vehicleNo" /></td>
						<td style="width: 25%"><s:property
								value="procurement.vehicleNum" /></td>
					</tr>

					<tr>
						<s:if test="procurement.farmerAttnce==0">
							<td style="width: 25%"><s:text name="farmerAttendance" /></td>
							<td style="width: 25%"><s:text name="PRESENT" /></td>
						</s:if>
						<s:else>
							<tr>
								<td style="width: 25%"><s:text name="farmerAttendance" /></td>
								<td style="width: 25%"><s:text name="ABSENT" /></td>
								<td style="width: 25%"><s:text name="substituteName" /></td>
								<td style="width: 25%"><s:property
										value="procurement.substituteName" /></td>
							</tr>
						</s:else>
					</tr>
				</s:if>
			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<table id="tblData1" class="table table-bordered aspect-detail">

				<thead>

					<tr>
						<th style="width: 15%"><s:text name="product" /><sup
							style="color: red;">*</sup></th>
						<th style="width: 20%"><s:text name="variety" /><sup
							style="color: red;">*</sup></th>
						<s:if test="getCurrentTenantId()!='lalteer'">
							<th style="width: 20%"><s:text name="grade" /> <sup
								style="color: red;">*</sup></th>
							<%-- <th style="width: 10%"><s:text name="pricePerUnit" /></th> --%>
						</s:if>
						<th style="width: 10%" align="center"><s:text name="procurement.batchNo" /></th>
						<th style="width: 20%"><s:text name="unit" /></th>
						<s:if test="getCurrentTenantId()!='pratibha'">
						<th style="width: 10%"><s:property value="%{getLocaleProperty('noofBags')}" /> </th>
						</s:if>
						<s:if test="getCurrentTenantId()=='awi'">
						<th style="width: 10%"><s:text name="noofFruitBags" /></th>
						</s:if>
						<s:if test="getCurrentTenantId()!='lalteer'">
							<th style="width: 10%"><s:property value="%{getLocaleProperty('grossWeights')}" /> <sup
								style="color: red;">*</sup></th>
						</s:if>
						<s:if test="getCurrentTenantId()=='lalteer'">
						
							<th style="width: 10%"><s:property
									value="%{getLocaleProperty('NetWeight')}" /> <sup
								style="color: red;">*</sup></th>
							<th style="width: 30%"><s:text name="dryLoss" /></th>
							<th style="width: 30%"><s:text name="gradingLoss" /></th>
							<%-- <th style="width: 30%"><s:text name="subTotal"/></th> --%>
							</s:if>
							<th style="width: 20%"><s:property value="%{getLocaleProperty('price')}" /><sup
							style="color: red;">*</sup></th>
							<th style="width: 20%"><s:property value="%{getLocaleProperty('subTotal')}" /><sup
							style="color: red;">*</sup></th>
							<th style="width: 30%"><s:text name="action" /></th>
						
						<!-- <th>Sub Total</th>  -->

					</tr>
				</thead>
				<tbody id="tableBody1">
				</tbody>
			</table>
		</div>
		<%-- <div class="yui-skin-sam">

			<span id="button1" class=""><span class="first-child">
					<button type="button" class="save-btn btn btn-success"
						onclick="save1();" class="save-btn">
						<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
						</font>
					</button>

			</span></span>


		</div> --%>
		<div class="appContentWrapper marginBottom">
			<table id="tableTemplate1" style="margin-top: 20px;"
				class="table table-bordered aspect-detail">

				<thead>
					<tr>
						<th colspan="9"><s:text name="info.procurementDetails" /></th>
					</tr>

					<tr class="border-less">
						<th class="hide">Id</th>
						<th style="width: 15%"><s:text name="product" /></th>
						<th style="width: 15%"><s:text name="variety" /><sup
							style="color: red;"></th>
						<s:if test="getCurrentTenantId()!='lalteer' ">
							<th style="width: 20%"><s:text name="grade" /></th>
							<%-- <th style="width: 20%"><s:text name="pricePerUnit" /></th> --%>
						</s:if>					
						<th style="width: 15%" align="center"><s:text name="procurement.batchNo" /></th>
						<th style="width: 15%"><s:text name="unit" />
						<s:if test="getCurrentTenantId()!='pratibha'">
						<th style="width: 20%"><s:property
									value="%{getLocaleProperty('noofBags')}" /></th></s:if>
						<s:if test="getCurrentTenantId()=='awi'">
						<th style="width: 20%"><s:text name="noofFruitBags" /></th></s:if>
						<s:if test="getCurrentTenantId()!='lalteer'">
							<th style="width: 20%"><s:property
									value="%{getLocaleProperty('grossWeights')}" /></th>
						</s:if>
						<s:if test="getCurrentTenantId()=='lalteer'">
							<th style="width: 20%"><s:text name="NetWeight" /></th>
							<th style="width: 10%"><s:text name="dryLoss" /></th>
							<th style="width: 15%"><s:text name="gradingLoss" /></th>
						</s:if>
			
						 <th style="width: 15%"><s:property
									value="%{getLocaleProperty('price')}" /></th> 
						 <th style="width: 15%"><s:property
									value="%{getLocaleProperty('subTotal')}" /></th> 
						<th style="width: 25%"><s:text name="action" /></th>
					</tr>
				</thead>
				<tbody id="tBodyTemplate1">
				</tbody>
			</table>
			
			<table class="table table-bordered aspect-detail">
			<thead>
			<tr>
						<th colspan="8"><s:property value="%{getLocaleProperty('info.payment')}" /></th>
					</tr>
					<tr>
					<td><label for="txt"><s:property
											value="%{getLocaleProperty('enterAmount')}" />(<s:if test="currentTenantId=='pratibha'"><s:property value="%{getLocaleProperty('priceUnit')}"/> </s:if><s:else><s:property value="%{getCurrencyType().toUpperCase()}" /></s:else>)</label></td>
											
											<td><s:textfield id="payamount" name="procurement.paymentAmount"
								theme="simple" maxlength="30" cssClass="upercls form-control" /></td>
								</tr></thead>
			</table>
		
			
			
			<s:if test="currentTenantId == 'efk'">
				<table class="table table-bordered aspect-detail">
					<thead>
						<tr>
							<th colspan="8"><s:property value="%{getLocaleProperty('Procurement.transpotation.Details')}" /></th>
						</tr>
						
						<tr>
							<td>
							<label for="txt">
							<s:property value="%{getLocaleProperty('procurement.transport.vehicleList')}" /></label>
							</td>

							<td>
								<s:select name="procurement.vehicleType" list="vehicleList" id="vehicleType"
									listKey="key" listValue="value" theme="simple"
									cssClass="form-control input-sm select2" multiple="true" />
							</td>
						
							<td>
							<label for="txt">
							<s:property value="%{getLocaleProperty('procurement.transport.cost')}" />(<s:if test="currentTenantId=='pratibha'"><s:property value="%{getLocaleProperty('priceUnit')}"/> </s:if><s:else><s:property value="%{getCurrencyType().toUpperCase()}" /></s:else>)</label>
							</td>

							<td>
								<s:textfield id="transportCost" name="procurement.transportCost" onkeypress="return isDecimal(event)" theme="simple" maxlength="30" cssClass="upercls form-control" />
							</td>
						</tr>
					</thead>
				</table>
			</s:if>
			
			
			<br />
			
			
			<div class="yui-skin-sam">
			
			<sec:authorize ifAllGranted="service.procurementService.update">
								<span id="update" class=""><span class="first-child">
										<button type="button" onclick="savePaymentAmt();"
											class="edit-btn btn btn-success">
											<FONT color="#FFFFFF"> <b><s:text
														name="save.button" /></b>
											</font>
										</button>
								</span></span>
							</sec:authorize>
							
			<span id="cancel" class=""><span class="first-child">
					<button type="button" id="back" onclick="onCancel();"
						class="back-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="back.button" />
						</font></b>
					</button>
			</span></span>
		</div>
		</div>
		

		
	</s:form>

	<s:hidden id="farmCode" name="filter.farmCode" />
	<s:form name="listForm" id="listForm" action="procurementProduct_list">
		<s:hidden name="currentPage" />
	</s:form>
	
	
		<s:hidden id="id" name="id" />
		<s:hidden key="currentPage" />
	
	
	<s:hidden id="procurementDetailArray" name="procurementDetailArray"></s:hidden>
	<s:hidden id="procurementDetailId" name="procurementDetailId" />
	<s:hidden id="productHide" name="productHide" />


</body>
