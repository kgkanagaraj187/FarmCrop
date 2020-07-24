<%@ include file="/jsp/common/report-assets.jsp"%>

<%@ include file="/jsp/common/detail-assets.jsp"%>
<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>

<script type="text/javascript">
var tenant='<s:property value="getCurrentTenantId()"/>';
function isEmpty(val){
	  if(val==null||val==undefined||val.toString().trim()==""){
	   	return true;
	  }
	  return false;
	}
	jQuery(document).ready(function() {
		refreshTemplateList1();
		addHarvestDetails();
		
		$("#tblData1").hide();
		
	});



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

	function listActivityMethod(value) {

		var selectedMethod = value;

		$.post("cropCalendar_populateEditActivityMethod", {
			selectedMethod : selectedMethod
		}, function(data) {
			$('#activityMethod').empty();
			
			var result = data;
			result = result.replace("{", "").replace("}", "");
			var farmersArr = result.split(", ");
			//addOption(document.getElementById("varietyList"),'<s:text name="txt.select"/>','');
			for (var i = 0; i < farmersArr.length; i++) {
				var arr = farmersArr[i].split("=");
				if (arr != "") {
					addOption(document.getElementById("activityMethod"), arr[1],
							arr[0]);
				}

			}
	

		});

	}
	
	


	function addHarvestDetails() {
		var table = document.getElementById("tblData1");
		var rows = table.getElementsByTagName("tr");
		$("#tblData1 tbody")
				.append(
						"<tr class='tableTr1'id='"+rows.length+1+"'>"
							
								+ "<td align='center'><select style='width:150px' class='tableTd1 txtFilterSelect filSelect form-controm input-sm' id='activityMethod' >"
								+ "</select></td>"								
								+ "<td align='center'><input type='text' class='tableTd2' id='noOfDays' name='textVal' maxlength='3' onkeypress='return isNumber(event)' /></td>"
								+"<td align='center'><button type='button' class='btn btn-sm btn-success' aria-hidden='true' onclick=save1() title='<s:text name="Save" />'><i class='fa fa-check'></i></button></td>"
								+				
								
								"</tr>");

	}
	

	function save1() {	
		
		document.getElementById("validateError").innerHTML="";
		var cropCalendarDetailId=$("#cropCalendarDetailId").val();
		var activityMethod=$("#activityMethod").val();
		var noOfDays=$("#noOfDays").val();
		
		 if(isEmpty(activityMethod))
		 {
			 document.getElementById("validateError").innerHTML ='<s:property value="%{getLocaleProperty('empty.activityMethod')}" />';
			jQuery("#activityMethod").focus();
			 return false;
		}
		 if(isEmpty(noOfDays))
		 {
			 document.getElementById("validateError").innerHTML = '<s:property value="%{getLocaleProperty('empty.noOfDays')}" />';
			jQuery("#noOfDays").focus();
			 return false;
		 }	
		var error=document.getElementById("validateError").innerHTML;
		document.getElementById("cropCalendarDetailArray").value=activityMethod+"###"+noOfDays+"###"+cropCalendarDetailId;
		var cropCalendarDetail=document.getElementById("cropCalendarDetailArray").value;
		 if(error=="")
			 {
		
			 $("#sucessbtn").prop('disabled', true);
				$.post("cropCalendar_populateCropCalendarDetails.action", {
					cropCalendarDetailArray : cropCalendarDetail
				}, function(data, status) {
				refreshTemplateList1();
				refresh1();
						
			});
		 }
		
		 $("#tblData1").hide();
		
		  			
	}

	function refresh1() {		
		$(".tableTd1").val("");
		$(".tableTd2").val("");
		
	}


	function onEdit(val) {
		//alert(jQuery("#variety"+val+"").text());
		//$('#varietyList').empty();
		jQuery("#tblData1").show();
			
			var activityMethodval = jQuery("#activityMethodCode"+val+"").text();
			//alert(activityMethodval);
		listActivityMethod(activityMethodval);
			//jQuery("#activityMethod").val(jQuery("#activityMethodCode"+val+"").text());
			
		document.getElementById("activityMethod").disabled = true; 
		/* 	var productVal = jQuery("#product"+val+"").text();
			 
			jQuery("#productLabel").text(productVal); 	
			var productId = jQuery("#productId"+val+"").text();
			listProVarierty(productId);
			 */
			jQuery("#noOfDays").val(jQuery("#noOfDays"+val+"").text());
			
			jQuery("#cropCalendarDetailId").val(val);
			
			
			
		}

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


	function refreshTemplateList1() {

		$('#tBodyTemplate1').empty();
		$('#temp').empty();
		var id = $("#cropCalendarId").val();

		$('#temp').append("<option value=''>Select Template</option>");

		$.getJSON('cropCalendar_populateCropCalendarDetailList.action',{cropCalendarId : id},function(jd) {
							var temps = jd.data;
							
							var bodyContent = '';
							for (var i = 0; i < temps.length; i++) {
								var temp = temps[i];								
								bodyContent += '<tr id="'+temp.id+'">';
								bodyContent += '<td style="width: 20%" class="hide">' + temp.id+ '</td>';	
								bodyContent += '<td style="width: 20%" class="hide" id="activityMethodCode'+temp.id+'">' + temp.activityMethodCode + '</td>';
								bodyContent += '<td style="width: 20%" class="hide" id="activityMethodType'+temp.id+'">' + temp.activityMethodType + '</td>';
								bodyContent += '<td style="width: 10%" align="center" id="activityMethod'+temp.id+'">'+ temp.activityMethod + '</td>';								
								bodyContent += '<td style="width: 10%" align="center" id="noOfDays'+temp.id+'">'+ temp.noOfDays + '</td>';
								
								bodyContent += '<td style="width: 10%" align="center"><button type="button" class="fa fa-pencil" onclick="onEdit('+ temp.id+ ');"/></td>';
								bodyContent += '</tr>';
							
							}							
							$('#tBodyTemplate1').html(bodyContent);
							$('#temp').val('');
						});
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
	<s:form name="form" cssClass="fillform" action="cropCalendar_list">
		<s:hidden key="currentPage" />
		<s:hidden key="cropCalendar.id" id="cropCalendarId" />
		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="cropCalendar.id" />
		</s:if>
		<s:hidden key="command" />
		<div class="appContentWrapper marginBottom">
			<table class="table table-bordered fillform">
				<tr>
					<th colspan="4"><s:property
							value="%{getLocaleProperty('info.CropCalendar')}" /></th>
				</tr>
				<tr>
				<td style="width: 25%"><s:property
							value="%{getLocaleProperty('calendarDate')}" /></td>
						<td style="width: 25%">	<s:date name="cropCalendar.createdDate" format="dd/MM/yyyy" /></td>
				
				
						<td style="width: 25%"><s:property
							value="%{getLocaleProperty('calendarName')}" /></td>
						<td style="width: 25%"><s:property value="cropCalendar.name" /></td> 
				</tr>
				<tr>
				<td style="width: 25%"><s:property
							value="%{getLocaleProperty('activityType.name')}" /></td>
					<td style="width: 25%"><s:if test="cropCalendar.activityType==0">
													<s:text name="Main Crop"></s:text>
												</s:if> <s:elseif test="cropCalendar.activityType==1">
													<s:text name="Inter Crop"></s:text>
												</s:elseif> <s:else>
													<s:text name="Border"></s:text>
												</s:else></td>

					<td style="width: 25%"><s:property
							value="%{getLocaleProperty('seasonCode')}" /></td>
					<td style="width: 25%"><s:property value="cropCalendar.seasonCode" /></td>
				
				</tr>
				<tr>
					<td style="width: 25%"><s:property
							value="%{getLocaleProperty('crop')}" /></td>
					<td style="width: 25%"><s:property value="cropCalendar.procurementProduct.code" />
									-
									<s:property value="cropCalendar.procurementProduct.name" /></td>

					<td style="width: 25%"><s:property
							value="%{getLocaleProperty('variety')}" /></td>
					<td style="width: 25%"><s:property value="cropCalendar.procurementVariety.code" />
									-
									<s:property value="cropCalendar.procurementVariety.name" /></td>
				</tr>
				
				<%-- <tr>
					<td style="width: 25%"><s:text name="updatedUser" /></td>
					<td style="width: 25%"><s:property
							value="procurement.updatedUser" /></td>
					<td style="width: 25%"><s:text name="createdDate" /></td>
					<td style="width: 25%"><s:date name="procurement.createdDate"
							format="dd/MM/yyyy" /></td>
				</tr> --%>

				<%-- <tr>

					<td style="width: 25%"><s:text name="updatedDate" /></td>
					<td style="width: 25%"><s:date name="procurement.updatedDate"
							format="dd/MM/yyyy" /></td>
				</tr> --%>
				
			</table>
		</div>
		<div class="appContentWrapper marginBottom">
			<table id="tblData1" class="table table-bordered aspect-detail">

				<thead>

					<tr>
						<th style="width: 15%;text-align: center;"><s:property value="%{getLocaleProperty('activityMethodName')}" /><sup style="color: red;">*</sup></th>
						<th style="width: 20%;text-align: center;"><s:property value="%{getLocaleProperty('noOfDays')}" /><sup style="color: red;">*</sup></th>
						<th style="width: 30%;text-align: center;"><s:property value="%{getLocaleProperty('action')}" /></th>
					</tr>
				</thead>
				<tbody id="tableBody1">
				</tbody>
			</table>
		</div>
		
		<div class="appContentWrapper marginBottom">
			<table id="tableTemplate1" style="margin-top: 20px;"
				class="table table-bordered aspect-detail">

				<thead>
					<tr>
						<th colspan="8"><s:property value="%{getLocaleProperty('info.cropCalendarDetails')}" /></th>
					</tr>

					<tr class="border-less">
						<th class="hide">Id</th>
						<th style="width: 15%;text-align: center;"><s:property value="%{getLocaleProperty('activityMethodName')}" /><sup style="color: red;"></th>
						<th style="width: 15%;text-align: center;"><s:property value="%{getLocaleProperty('noOfDays')}" /><sup style="color: red;"></th>						
						<th style="width: 25%;text-align: center;"><s:text name="action" /></th>
					</tr>
				</thead>
				<tbody id="tBodyTemplate1">
				</tbody>
			</table>
			<br />
			<div class="yui-skin-sam">
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


	<s:form name="listForm" id="listForm" action="cropCalendar_list">
		<s:hidden name="currentPage" />
	</s:form>
	<s:hidden id="cropCalendarDetailArray" name="cropCalendarDetailArray"></s:hidden>
	<s:hidden id="cropCalendarDetailId" name="cropCalendarDetailId" />
	
</body>
