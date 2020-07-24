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
		var selectedCropType = $('#cropType').val();
		var farmCode = $('#farmCode').val();
		jQuery.post("cropHarvestServiceReport_populateCropName.action", {
			id : obj.value,
			dt : new Date(),
			selectedCropType : obj.value,
			selectedFarm : farmCode
		}, function(result) {
			insertOptions("cropName", JSON.parse(result));
			listCrops(document.getElementById("cropName"));
		});
	}

	
	
	
	
	function listCrops(obj) {
		var selectedCropName = $('#cropName').val();
		var selectedCropType = $('#cropType').val();
		
		var selectedFarm = $('#farmCode').val();
		jQuery.post("cropHarvestServiceReport_populateVarietyName.action", {
			id : obj.value,
			dt : new Date(),
			selectedCropType:selectedCropType,
			selectedFarm:selectedFarm,
			selectedCropName : obj.value
		}, function(result) {
			document.getElementById("variety").length = 0;
			addOption(document.getElementById("variety"), '<s:text name="txt.select"/>', "0");
			listVariety(document.getElementById("variety").value);
			var jsonArr =  JSON.parse(result)
			for (var i = 0; i < jsonArr.length; i++) {
				addOption(document.getElementById("variety"), jsonArr[i].name.split('~')[0],
   						jsonArr[i].id);
			}
		});
	}

	
	function listVariety(obj) {
		var selectedVariety = $('#variety').val();
		jQuery.post("cropHarvestServiceReport_populateGradeName.action", {
			id : obj.value,
			dt : new Date(),
			selectedVariety : obj.value
		}, function(result) {
			insertOptions("grade", JSON.parse(result));

		});
	}
	
	function loadUnit(){
	
		var selectedCrop = document.getElementById("cropName").value;
		$.post("cropHarvestServiceReport_populateUnit",{selectedCrop:selectedCrop},function(data){
			var jsonData = $.parseJSON(data);
			$.each(jsonData, function(index, value) {
				if(value.id=="unit"){
					document.getElementById("productUnit").innerHTML=value.name;
				}
			});
		});
	}

	/* 	function listGrade(obj){
	 var selectedGrade = $('#grade').val();
	 jQuery.post("cropHarvestServiceReport_populatePrice.action",{id:obj.value,dt:new Date(),selectedGrade:obj.value},function(result){
	
	 $('#sellPrice').val(JSON.parse(result));
	 //	insertOptions("sellPrice",JSON.parse(result));
	
	 });
	 } */

	$(document).ready(function() {
		refreshTemplateList1();
		addHarvestDetails();
	});
	 $('#sellPrice').keypress(function(event) {
		  if ((event.which != 46 || $(this).val().indexOf('.') != -1) &&
		    ((event.which < 48 || event.which > 57) &&
		      (event.which != 0 && event.which != 8))) {
		    event.preventDefault();
		  }

		  var text = $(this).val();

		  if ((text.indexOf('.') != -1) &&
		    (text.substring(text.indexOf('.')).length > 2) &&
		    (event.which != 0 && event.which != 8) &&
		    ($(this)[0].selectionStart >= text.length - 2)) {
		    event.preventDefault();
		  }
		});
		 
	function addHarvestDetails() {
		var table = document.getElementById("tblData1");
		var rows = table.getElementsByTagName("tr");
		$("#tblData1 tbody")
				.append(
						"<tr class='tableTr1'id='"+rows.length+1+"'>"+
						"<td align='center'><select class='tableTd1 txtFilterSelect filSelect form-control input-sm select2' id='cropType' style='width:100px'  onchange='listCropType(this)'>"+getQuestions1("cropTypeCl")+"</select></td>"+
						"<td align='center'><select class='tableTd2 txtFilterSelect filSelect form-control input-sm select2' id='cropName'  style='width:100px' onchange='listCrops(this);loadUnit()'>"+getQuestions1("cropNamesList")+"</select></td>"+
						"<td align='center'><select class='tableTd3 txtFilterSelect filSelect form-control input-sm select2' id='variety' style='width:100px' onchange='listVariety(this)'>"+getQuestions1("varietysList")+"</select></td>"+
						"<td align='center'><select class='tableTd4 txtFilterSelect filSelect form-control input-sm select2' id='grade' style='width:100px'>"+getQuestions1("gradesList")+"</select></td>"+
						"<td align='center'><div id='productUnit'/></td>"+
						 "<td align='center'><input type='text' class='tableTd6' name='textVal' id='quantity' maxlength='10'/></td>"
								+ /* "<td align='center'><input type='text' class='tableTd7' name='textVal'  id='sellPrice' maxlength='7' size='10'/></td>"
								+  */"<input type='hidden' class='tableTd8' name='textVal' maxlength='5'/>"
								+
						
	

								//"<td align='center'><select class='tableTd5 txtFilterSelect filSelect select2' >"+getQuestions1("inventoryItem4")+"</select></td>"+
								//"<td class='tableTd8'><img id='"+rows.length+1+"' class='ic-del' onclick='Delete1(this);'/></td>"+
								"</tr>");

		
		$('#quantity').keypress(function(event) {
			  if ((event.which != 46 || $(this).val().indexOf('.') != -1) &&
			    ((event.which < 48 || event.which > 57) &&
			      (event.which != 0 && event.which != 8))) {
			    event.preventDefault();
			  }

			  var text = $(this).val();

			  if ((text.indexOf('.') != -1) &&
			    (text.substring(text.indexOf('.')).length > 2) &&
			    (event.which != 0 && event.which != 8) &&
			    ($(this)[0].selectionStart >= text.length - 2)) {
			    event.preventDefault();
			  }
			});

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
		var id = $("#harvestId").val();
		var columnNamesArray = new Array();
		var columnModelArray = new Array();
		var filterParams = new Array();
		var colCount = 0;
		var flag = 0;
		var paramObj = {};
		jQuery(".tableTr1").each(
				function() {

					jQuery(this).find(".tableTd1").each(
							function() {
								var columnName1 = jQuery.trim(jQuery(this)
										.find("option:selected").val());
								paramObj['harvestDetailList[' + colCount
										+ '].cropType.code'] = columnName1;

								if (columnName1 == '-1') {
									flag = 1;
								}
							});

					jQuery(this).find(".tableTd2").each(
							function() {
								var columnModelName1 = jQuery.trim($(this)
										.val());
								paramObj['harvestDetailList[' + colCount
										+ '].crop.code'] = columnModelName1;

								if (columnModelName1 == '-1') {
									flag = 1;
								}
							});

					jQuery(this).find(".tableTd3").each(
							function() {
								var columnName2 = jQuery.trim(jQuery(this)
										.find("option:selected").val());
								paramObj['harvestDetailList[' + colCount
										+ '].variety.id'] = columnName2;

								if (columnName2 == '-1') {
									flag = 1;
								}
							});

					jQuery(this).find(".tableTd4").each(
							function() {
								var columnName3 = jQuery.trim(jQuery(this)
										.find("option:selected").val());
								paramObj['harvestDetailList[' + colCount
										+ '].grade.code'] = columnName3;

								if (columnName3 == '-1') {
									flag = 1;
								}
							});

					/* jQuery(this).find(".tableTd5").each(
							function() {
								var columnName = jQuery.trim($(this).val());
								paramObj['harvestDetailList[' + colCount
										+ '].qty'] = columnName;

								if (columnName == '') {
									flag = 1;
								}
							}); */
					
					jQuery(this).find(".tableTd6").each(
							function() {
								var columnName4 = jQuery.trim($(this).val());
								paramObj['harvestDetailList[' + colCount
										+ '].qty'] = columnName4;

								if (columnName4 == '') {
									flag = 1;
								}
							});
					jQuery(this).find(".tableTd7").each(
							function() {
								var columnName5 = jQuery.trim($(this).val());
								paramObj['harvestDetailList[' + colCount
										+ '].price'] = columnName5;

								if (columnName5 == '') {
									flag = 1;
								}
							});
					/*  jQuery(this).find(".tableTd7").each(function(){
							var columnName6 = jQuery.trim($(this).val());
							paramObj['harvestDetailList['+colCount+'].subTotal'] =columnName6;
							
							if(columnName6 == ''){
								flag=1;
							}		
							
						}); */
					colCount++;

				});

		var harvestDetailList = [];
		for (var i = 0; i < colCount; i++) {
			var objfI1 = new Object();
			if (paramObj['harvestDetailList[' + i + '].cropType.code'] != undefined) {
				objfI1.cropType = paramObj['harvestDetailList[' + i
						+ '].cropType.code'];

			} else {
				objfI1.cropType = "";
			}
			if (paramObj['harvestDetailList[' + i + '].variety.id'] != undefined) {
				objfI1.varietyId = paramObj['harvestDetailList[' + i
						+ '].variety.id'];

			} else {
				objfI1.varietyId = "";
			}
			if (paramObj['harvestDetailList[' + i + '].crop.code'] != undefined) {
				objfI1.cropId = paramObj['harvestDetailList[' + i
						+ '].crop.code'];

			} else {
				objfI1.cropId = "";
			}
			if (paramObj['harvestDetailList[' + i + '].grade.code'] != undefined) {
				objfI1.gradeId = paramObj['harvestDetailList[' + i
						+ '].grade.code'];
			} else {
				objfI1.gradeId = "";
			}
			if (paramObj['harvestDetailList[' + i + '].qty'] != undefined) {
				objfI1.qty = paramObj['harvestDetailList[' + i + '].qty'];
			} else {
				objfI1.qty = "";
			}
			/* if (paramObj['harvestDetailList[' + i + '].price'] != undefined) {
				objfI1.price = paramObj['harvestDetailList[' + i + '].price'];
			} else {
				objfI1.price = "";
			} */
			/* 	 if(paramObj['harvestDetailList['+i+'].subTotal']!=undefined){
					objfI1.subTotal=paramObj['harvestDetailList['+i+'].subTotal'];
					} 
					else{
						objfI1.subTotal="";
					} */
			harvestDetailList.push(objfI1);

		}

		var postData = new Object();
		postData.harvestDeatailJsonString = JSON.stringify(harvestDetailList);
		postData.harvestId = id;
		console.log('here ' + JSON.stringify(postData));

		//$.post("farmInventory_populateInventry",{paramObj},function(data){
		//});
		if (flag == 0) {
			$.ajax({
				url : 'cropHarvestServiceReport_populateHarvestDetail.action',
				type : 'post',
				dataType : 'json',
				success : function(data) {
					refresh1();
					refreshTemplateList1();

				},
				data : postData
			});
		} else {
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
	}

	function butDeleteHarvest(val) {
		if (confirm('<s:text name="confirm.delete"/>')) {
			 $.post("cropHarvestServiceReport_deleteCropHarvestDetail.action",{templateId:val}, function(data, status) {
				alert('<s:text name="msg.removed"/>');
				refreshTemplateList1();

			}); 
		}
	}
	jQuery(function($) {
		$("#calendarDOJ").datepicker({});
	});

	function insertOptions(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), '<s:text name="txt.select"/>', "0");
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
		$.post("cropHarvestServiceReport_populateCrop", {
			cropTypeCode : type
		}, function(result) {

		});
	}
	function Delete1(evt) {
		var par = $(evt).parent().parent(); //tr
		par.remove();
	}
	function refreshTemplateList1() {
		 var tenant='<s:property value="getCurrentTenantId()"/>';
		$('#tBodyTemplate1').empty();
		$('#temp').empty();
		var id = $("#harvestId").val();

		$('#temp').append("<option value=''>Select Template</option>");

		$
				.getJSON(
						'cropHarvestServiceReport_populateCropHarvestDetailList.action',
						{
							cropHarvestId : id
						},
						function(jd) {

							var temps = jd.data;

							var bodyContent = '';
							for (var i = 0; i < temps.length; i++) {
								var temp = temps[i];

								bodyContent += '<tr>';
								bodyContent += '<td class="hide">' + temp.id
										+ '</td>';
								bodyContent += '<td align="center">'
										+ temp.cropType + '</td>';
								bodyContent += '<td align="center">'
										+ temp.crop + '</td>';
								bodyContent += '<td align="center">'
										+ temp.variety + '</td>';
								bodyContent += '<td align="center">'
										+ temp.grade + '</td>';
								bodyContent += '<td align="center">'
											+ temp.unit + '</td>';
								bodyContent += '<td align="center">'
										+ temp.quantity + '</td>';
								/* bodyContent += '<td align="center">'
										+ temp.price + '</td>'; */
								/* bodyContent += '<td align="center">'
										+ temp.subTotal + '</td>'; */
										 bodyContent+='<td align="center"><button type="button" class="fa fa-trash" onclick="butDeleteHarvest('+temp.id+');"/></td>';
								bodyContent += '</tr>';
								$('#temp').append(
										"<option value='"+temp.id+"'>"
												+ temp.cropType + temp.crop
												+ temp.variety + temp.grade
												+ "</option>");
							}

							$('#tBodyTemplate1').html(bodyContent);
							$('#temp').val('');
						});
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
		action="cropHarvestServiceReport_list">
		<s:hidden key="currentPage" />
		<s:hidden key="cropHarvest.id" id="harvestId" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="cropHarvest.id" />
			<s:if test='branchId==null'>
				<tr class="odd">
					<td width="35%"><s:text name="app.branch" /></td>
					<td width="65%"><s:property
							value="%{getBranchName(cropHarvest.branchId)}" /></td>
				</tr>
			</s:if>
		</s:if>
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
							<s:actionerror />
							<s:fielderror />
							
							<b><s:text name="season" /> : </b>
							<s:property value="currentSeasonsName" />
							-
							<s:property value="currentSeasonsCode" />
						</div>


						<div class="formContainerWrapper dynamic-form-con">

							<h2>
								<s:text name="info.cropHarvestReport" />
							</h2>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="harvestDate" />
								</p>
								<p class="flexItem">
									<s:date name="cropHarvest.harvestDate" format="dd/MM/yyyy" />
								</p>
							</div>

							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmerId" />
								</p>
								<p class="flexItem">
									<s:property value="cropHarvest.farmerName" />
								</p>
							</div>
							<div class="dynamic-flexItem">
								<p class="flexItem">
									<s:text name="farmName" />
								</p>
								<p class="flexItem">
									<s:property value="cropHarvest.farmName" />
									-
									<s:property value="cropHarvest.farmCode" />
								</p>
							</div>

							<s:if test="currentTenantId=='chetna'">
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="storageIn" />
									</p>
									<p class="flexItem">
										<s:if test="cropHarvest.storageIn=='99'">
											<s:property value="storageId[cropHarvest.storageIn]" />-<s:property
												value="cropHarvest.otherStorageInType" />
										</s:if>
										<s:else>
											<s:property value="storageId[cropHarvest.storageIn]" />
										</s:else>
									</p>
								</div>
								
								<div class="dynamic-flexItem">
									<p class="flexItem">
										<s:text name="packedIn" />
									</p>
									<p class="flexItem">
										<s:if test="cropHarvest.packedIn=='99'">
											<s:property value="packId[cropHarvest.packedIn]" />-<s:property
												value="cropHarvest.otherPackedInType" />
										</s:if>
										<s:else>
											<s:property value="packId[cropHarvest.packedIn]" />
										</s:else>
									</p>
								</div>
								
							</s:if>
						
						</div>
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:text name="info.cropDetails" />
							</h2>

							<table >
								<tr>
									<td><s:select name="cropType" cssClass="hide"
											list="cropTypeList" headerKey="-1"
											headerValue="%{getText('txt.select')}" theme="simple" 
											id="cropTypeCl" onchange="populateCrop(this.value)" /> 
											<s:select
											name="crop" cssClass="hide" list="cropList" headerKey="-1"
											headerValue="%{getText('txt.select')}" theme="simple"
											id="cropLists" onchange="populateVariety(this.value)" /> <s:select
											name="variety" cssClass="hide" list="varietyList"
											headerKey="-1" headerValue="%{getText('txt.select')}"
											theme="simple" id="varietyLists"
											onchange="populateGrade(this.value)" /> <s:select
											name="grade" cssClass="hide" list="gradeList" headerKey="-1"
											headerValue="%{getText('txt.select')}" theme="simple"
											id="gradeLists" /> <%-- 	<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
		onclick="addHarvestDetails()" /> --%></td>
								</tr>
							</table>
							<table id="tblData1" class="table table-bordered aspect-detail">

								<thead>

									<tr>
										<th style="text-align: center"><s:text
												name="%{getLocaleProperty('cropType')}" /></th>
										<th style="text-align: center"><s:text name="crop" /></th>
										<th style="text-align: center"><s:text name="variety" /></th>
										<th style="text-align: center"><s:text name="grade" /></th>
										<th style="text-align: center"><s:text name="unit" /></th>
										<th style="text-align: center"><s:text name="%{getLocaleProperty('quantitykg')}" /></th>
										<%-- <th style="text-align: center"><s:text name="%{getLocaleProperty('price/kg')}" /></th> --%>
										<!-- <th>Sub Total</th>  -->

									</tr>
								</thead>
								<tbody id="tableBody1">
								</tbody>
							</table>
							<div class="yui-skin-sam">
			<span id="button1" class=""><span class="first-child">
					<button type="button" class="save-btn btn btn-success" onclick="save1()">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
					</button>
			</span></span>

		</div>
							
									<table id="tableTemplate1" border="1"
			class="table table-bordered aspect-detail" style="margin-top: 10px;">
			<thead>
				<tr class="border-less">
					<th class="hide">Id</th>
					<th style="text-align: center"><s:text name="%{getLocaleProperty('Crop type')}"/></th>
					<th style="text-align: center"><s:text name="crop" /></th>
					<th style="text-align: center"><s:text name="variety" /></th>
					<th style="text-align: center"><s:text name="grade" /></th>
					<th style="text-align: center"><s:text name="unit" /></th>
					<th style="text-align: center"><s:text name="%{getLocaleProperty('quantitykg')}" /></th>
					<%-- <th style="text-align: center"><s:text name="%{getLocaleProperty('price/kg')}" /></th>
					<th style="text-align: center"><s:text name="subTotal" /></th> --%>
					<th style="text-align: center"><s:text name="delete.button" /></th>
				</tr>
			</thead>
			<tbody id="tBodyTemplate1">
			</tbody>
		</table>
						</div>
							<div class="yui-skin-sam">
		<%-- 			<s:if test="command =='create'">
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span> 
			</s:if>
			<s:else>
				<span id="button" class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else>
 --%>
		<span id="cancel" class=""><span class="first-child"><button
					type="button" onclick="onCancel();" class="back-btn btn btn-sts">
					<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
					</font></b>
				</button></span></span>

	</div>
					</div>
				</div>
			</div>
		</div>



		<br />
	</s:form>


	<s:form name="listForm" id="listForm"
		action="cropHarvestServiceReport_list">
		<s:hidden name="currentPage" />
	</s:form>

	<s:hidden id="farmCode" name="cropHarvest.farmCode" />
</body>