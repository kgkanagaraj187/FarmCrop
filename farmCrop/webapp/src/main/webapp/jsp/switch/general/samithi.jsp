<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>


<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
</head>
<style>
.aspect-detail TD:nth-child(2) {
	font-weight: normal;
}
</style>

<script src="js/dynamicComponents.js?v=1.17"></script>

<script type="text/javascript">
var tenant='<s:property value="getCurrentTenantId()"/>';
var isGramPanchayatEnable = '<s:property value="gramPanchayatEnable"/>';
	$(document).ready(function() {
		renderDynamicFeilds();
		if('<s:property value="command"/>'=="update"){
			setDynamicFieldUpdateValues();
		}
		if('<s:property value="getCurrentTenantId()"/>' == 'pratibha' && '<s:property value="getBranchId()"/>' == 'bci' ||'<s:property value="getBranchId()"/>' == 'organic'){
		
			$(".breadcrumb").find('li:not(:first)').remove();
			ss
			
		$(".breadcrumb").append('<li><a href="samithi_list.action">/ <s:property value="%{getLocaleProperty('profile.samithi.bci')}" /></a></li>');
		}
		
		$("#groupFormationCalendar").datepicker(
                {
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    onClose: function (dateText, inst) {
                        $(this).datepicker(
                                'setDate',
                                new Date(inst.selectedYear, inst.selectedMonth,
                                        1));
                    }
                });
		
		jsonString=$('#jsonString').val();
		if(jsonString!=null && jsonString!=''){
			var jsonObj=JSON.parse(jsonString);
			var accTypeText = "";
			for(var i=0;i<jsonObj.length;i++){
				var bankInformation=jsonObj[i];
				var id=(i+1);
				
				if(tenant=='chetna'){
				 $( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
						  "<td>" + bankInformation.accType + "</td>" +
				          "<td>" + bankInformation.accNo + "</td>" +
				          "<td>" + bankInformation.bankName + "</td>" +
				          "<td>" + bankInformation.branchName + "</td>" +
				          "<td>" + bankInformation.sortCode + "</td>" +
				          "<td class='hide'>" + bankInformation.accTypeCode + "</td>" +
				          //"<td>" + bankInformation.swiftCode + "</td>" +				        
				          "<td><button id='"+id+""+","+""+bankInformation.accTypeCode+""+","+""+bankInformation.bankNameCode+""+","+""+bankInformation.branchNameCode+"'class='btn  btn-sts' type='button' onclick='editBank(this);'><label>Edit</label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' type='button' onclick='deleteBank(this);'><label>Delete</label></button></td>" +
				        "</tr>" );
				}else{
					$( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
							  "<td>" + bankInformation.accType + "</td>" +
					          "<td>" + bankInformation.accNo + "</td>" +
					          "<td>" + bankInformation.bankName + "</td>" +
					          "<td>" + bankInformation.branchName + "</td>" +
					          "<td>" + bankInformation.sortCode + "</td>" +
					          "<td class='hide'>" + bankInformation.accTypeCode + "</td>" +
					          //"<td>" + bankInformation.swiftCode + "</td>" +				        
					          "<td><button id='"+id+""+","+""+bankInformation.accTypeCode+"'class='btn  btn-sts' type='button' onclick='editBank(this);'><label>Edit</label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' type='button' onclick='deleteBank(this);'><label>Delete</label></button></td>" +
					        "</tr>" );
				}
			}
		 }
	
		/* function submit() {
			document.form.submit;
		} */
		
	});
	
	function getTxnType(){
		return "381";
	}
	
	  function isNumber(evt) {
	 		
	 	    evt = (evt) ? evt : window.event;
	 	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	 	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	 	        return false;
	 	    }
	 	    return true;
	 	}
	  
	  function openModal(){
			$('#hidId').val('');
		}
	   function saveBankInformation() {
		    var valid = true;
		    var hidId = $('#hidId').val();
		    var accountNo = $('#accNo').val();
		    var bankName;
		    var accType = $('#accountType').val();
		    var sortCode = $('#sortCode').val().trim();
		    var bankNameVal;
		    var branchName;
		    var branchNameVal;
		    var bankNameText;
		    var accTypeText = $('#accountType').find(":selected").text();
		    var tenant = '<s:property value="getCurrentTenantId()"/>';
		    if (tenant == "chetna") {
		        bankNameVal = $('#bankNameList').val();
		        bankName = $('#bankNameList').find(":selected").text();
		        branchNameVal = $('#branchNameList').val();
		        branchName = $('#branchNameList').find(":selected").text();
		    } else {
		        bankName = $('#bankName').val();
		        branchName = $('#branchName').val();
		    }
		    // var swiftCode= $('#swiftCode').val();
		   
		    if (accType == "-1") {
		    	$(".bankError").text('<s:text name="empty.accType"/>');
		        valid = false;
		        return false;
		    } else if (accountNo == "") {
		    	$(".bankError").text('<s:text name="empty.accNo"/>');
		        valid = false;
		    } else if (!isNumber(accountNo)) {
		        $(".bankError").text('<s:text name="valid.accNo"/>');
		        valid = false;
		    } else if (sortCode == "") {
		    	$(".bankError").text('<s:text name="empty.sortCode"/>');
		        valid = false;

		    } else if (tenant != "chetna") {
		        if (bankName == "") {
		        	$(".bankError").text('<s:text name="empty.bankName"/>');
		            valid = false;
		        } else if (!isAlphabetsWithSpace(bankName)) {
		        	$(".bankError").text('<s:text name="valid.bankName"/>');
		        	//alert('<s:text name="valid.bankName"/>');
		            valid = false;
		        } else if (branchName == "") {
		        	$(".bankError").text('<s:text name="empty.branchName"/>');
		           // alert('<s:text name="empty.branchName"/>');
		            valid = false;
		        } else if (!isAlphabetsWithSpace(branchName)) {
		        	$(".bankError").text('<s:text name="valid.branchDetails"/>');
		            //alert('<s:text name="valid.branchDetails"/>');
		            valid = false;
		        }
		    } else if (tenant == "chetna") {

		        if (bankNameVal == "-1") {
		        	$(".bankError").text('<s:text name="select.bankName"/>');
		            //alert('<s:text name="select.bankName"/>');
		            valid = false;
		        }
		        if (branchNameVal == "-1") {
		        	$(".bankError").text('<s:text name="select.branchName"/>');
		            //alert('<s:text name="select.branchName"/>');
		            valid = false;
		        }

		    }

		    if (valid) {
		        document.getElementById("model-close-btn").click();
		        if (hidId == null || hidId == '') {
		        	//var accType = $('#accountType').val();
		            var rowCount = $('#bankTable tr').length;
		            var id = rowCount + 1;
		            var idd = id + "," + accType;
		            if (tenant == "chetna") {
		                idd = id + "," + accType + "," + bankNameVal + "," +
		                    branchNameVal;
		            }
		            $( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
		  			      //"<td>" + accType+"-"+accTypeText + "</td>" +  
		  			      "<td>" + accTypeText + "</td>" +
		  		          "<td>" + accountNo + "</td>" +
		  		          "<td>" + bankName + "</td>" +
		  		          "<td>" + branchName + "</td>" +
		  		          "<td>" + sortCode + "</td>" +
		  		         // "<td>" + swiftCode + "</td>" +
		  		             "<td class='hide'>" + accType + "</td>" +
		  		          "<td><button id='"+idd+"' type='button' class='btn  btn-sts' href='#' onclick='editBank(this);'><label><s:text name='edit.button'/></label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' href='#' onclick='deleteBank(this);'><label><s:text name='delete.button'/></label></button></td>" +
		  		        "</tr>" );
		        } else {
					var $tr=$('#tr_'+hidId+","+accType);
					if(tenant=="chetna"){
						 $tr=$('#tr_'+hidId+","+accType+","+bankNameVal+","+branchNameVal);
					}
				    var $td= $tr.children('td'); 
				    var accNoCell= $td.eq(1);  
					var bankNameCell= $td.eq(2);
					var branchNameCell= $td.eq(3); 
					var sortCodeCell= $td.eq(4);		
					var accTypeCell = $td.eq(0);
					var accTypeVal = $td.eq(5);
					
					accNoCell.text(accountNo);
					bankNameCell.text(bankName);
					branchNameCell.text(branchName);
					sortCodeCell.text(sortCode);
					accTypeCell.text(accTypeText);
					accTypeVal.text(accType);
					
		        }
		        refreshPopup();
		    }
		}
	   
	   function cancelBnkInfo(){
			refreshPopup();	
			document.getElementById("model-close-btn").click();
		}

	   function refreshPopup(){
			$(".bankError").empty();
			var accountNo= $('#accNo').val("");
			var sortCode= $('#sortCode').val("");
			//var swiftCode= $('#swiftCode').val("");
			var accType = $('#accountType').val(-1);
			//jQuery('#accountType').select2();
			var tenant='<s:property value="getCurrentTenantId()"/>';
			if(tenant=="chetna"){
				var bankNameVal=$('#bankNameList').val(-1);
				//jQuery('#bankNameList').select2();
				var branchNameVal=$('#branchNameList').val(-1);
				//jQuery('#branchNameList').select2();
			}else{
				var bankName= $('#bankName').val("");
				var branchName= $('#branchName').val("");
			}
		}
	   
	   function closeModal(){
			refreshPopup();	
			document.getElementById("model-close-btn").click();
		}
	   function editBank(evt){
			//alert("edit");
		   var $td= $(evt).closest('tr').children('td');
			var hiddenId = $(evt).attr('id');
			var accountNo= $td.eq(1).text(); 
			var bankName= $td.eq(2).text();
			var branchName= $td.eq(3).text();
			var sortCode= $td.eq(4).text();
			var accType = $td.eq(5).text();
			var tenant = '<s:property value="getCurrentTenantId()"/>';
			if(tenant=="chetna"){
				$('#bankNameList').val(hiddenId.split(",")[2]);
				//$('#bankNameList').select2();
				$('#branchNameList').val(hiddenId.split(",")[3]);
				//$('#branchNameList').select2();
			}else{
				$('#branchName').val(branchName);
				$('#bankName').val(bankName);
				
			}
			
			$('#accNo').val(accountNo);
			
			$('#sortCode').val(sortCode);
			//alert(hiddenId.split(",")[1]);
			$('#accountType').val(accType);
			//$('#accountType').select2();
			
			$('#hidId').val(hiddenId.split(",")[0]);
			
			document.getElementById("editBankDetailModal").click();
			
		}

		function deleteBank(evt){
			var result =confirm('<s:text name="txt.delete"/>');	
			if(result){
					var $tr= $(evt).closest('tr'); 
					$tr.remove();
					//evt.preventDefault();
				   }
				
				}
		
		/* function onSubmit(){
			
			beforeSave();
		} */
		
		function beforeSave(){
			processBank();
		}
		
		function processBank(){
			
			var jsonObj=[];
			$('#bankTable tbody tr').each(function() {
				var $td= $(this).children('td');  
			    var accountNo= $td.eq(1).text();  
				var bankName= $td.eq(2).text();
				var branchName= $td.eq(3).text(); 
				var sortCode= $td.eq(4).text(); 
				var accType = $td.eq(5).text();
				var accTypeCode =  $td.eq(5).text();
				var bankInformation = {};				
				bankInformation["accNo"] = accountNo;
				bankInformation["bankName"] = bankName;
				bankInformation["branchName"] = branchName;
				bankInformation["sortCode"] = sortCode;
				bankInformation["accType"] = accType;
				bankInformation["accTypeCode"] = accTypeCode;
			    jsonObj.push(bankInformation);
			 });
			$('#jsonString').val(JSON.stringify(jsonObj));
		}
	
	function listMunicipality(obj) {
		var selectedLocality = $('#localities').val();
		jQuery.post("samithi_populateCity.action", {
			id : obj.value,
			dt : new Date(),
			selectedLocality : obj.value
		}, function(result) {
			insertOptions("city", $.parseJSON(result));
			listPanchayat(document.getElementById("city"));
		});
	}

	function listPanchayat(obj) {
		try {
			var selectedCity = $('#city').val();

			jQuery.post("samithi_populatePanchayath.action", {
				id : obj.value,
				dt : new Date(),
				selectedCity : obj.value
			}, function(result) {
				insertOptions("panchayath", $.parseJSON(result));
				listVillage(document.getElementById("panchayath"));
			});
		} catch (e) {
		}
	}

	function listVillage(obj) {
		var selectedPanchayat = $('#panchayath').val();
		jQuery.post("samithi_populateVillage.action", {
			id : obj.value,
			dt : new Date(),
			selectedPanchayat : obj.value
		}, function(result) {
			insertOptions("village", $.parseJSON(result));
			//listSamithi(document.getElementById("village"));
		});
	}

	function listVillageByCity(obj) {
		var selectedCity = $('#city').val();
		jQuery.post("samithi_populateVillageByCity.action", {
			id : obj.value,
			dt : new Date(),
			selectedCity : obj.value
		}, function(result) {
			insertOptions("village", $.parseJSON(result));
			//listSamithi(document.getElementById("village"));
		});
	}

	function validate() {
		var currentTenant = '<s:property value="getCurrentTenantId()"/>';
		var isGramPanchayatEnable = '<s:property value="gramPanchayatEnable"/>';
		beforeSave();
		if(currentTenant=='kenyafpo'){
			var country=$("#country").val();
			var state=$("#state").val();
			var locality=$("#localities").val();
			var city=$("#city").val();
			var panchayat=$("#panchayath").val();
			var village=$("#village").val();
			var email=$("#email").val();
		    if (isEmpty(country)) {
		          jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.country')}" />");
		          return false;
		      }else if (isEmpty(state)) {
		          jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.state')}" />");
		          return false;
		      }else if (isEmpty(locality)) {
		          jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.locality')}" />");
		          return false;
		      } else if (isEmpty(city)) {
		          jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.city')}" />");
		          return false;
		      } else if(isGramPanchayatEnable=='1'){
					if (isEmpty(panchayat)) {
					      jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.gramPanchayat')}"/>");
					          return false;
					      }
				}else if (isEmpty(village)) {
		         jQuery(".ferror").text("<s:property value="%{getLocaleProperty('empty.village')}"/>");
		         return false;
		     }
		    
		    if(!isEmpty(email)){
		    	validateEmail(email)
		    }
		}
		
		if(currentTenant=='symrise' && $("#groupFormationCalendar").is(":visible") && $('#groupFormationCalendar').val()==''){
			alert("Please Choose Group Formation Date")
		}else
		if(addDynamicField()){
			document.form.submit();
		}
		
	}
	
	function listState(obj, countryId, stateId, disId, cityId,gpId, vId) {

		if (!isEmpty(obj)) {
			var selectedCountry = $('#' + countryId).val();
			jQuery.post("farmer_populateState.action", {
				selectedCountry : obj.value
			}, function(result) {
				insertOptions(stateId, $.parseJSON(result));
				listLocality(document.getElementById(stateId), stateId, disId,
						cityId,gpId,vId);
			});
			$('#' + disId).select2();
		}
	}
	function listLocality(obj, stateId, disId, cityId,gpId, vId) {
//alert("AAA");
		if (!isEmpty(obj)) {
			var selectedState = $('#' + stateId).val();
			jQuery.post("farmer_populateLocality.action", {
				id1 : obj.value,
				dt : new Date(),
				selectedState : obj.value
			}, function(result) {
					insertOptions(disId, $.parseJSON(result));
				listMunicipality(document.getElementById(disId),disId, cityId,gpId, vId);
			});
			$('#' + cityId).select2();
		}
	}

	function listMunicipality(obj, disId, cityId, gpId, vId) {
		if(isGramPanchayatEnable==1){
			if (!isEmpty(obj)) {
				var selectedLocality = $('#' + disId).val();
				jQuery.post("farmer_populateCity.action", {
					id1 : obj.value,
					dt : new Date(),
					selectedLocality : obj.value
				}, function(result) {
					insertOptions(cityId, $.parseJSON(result));
					listPanchayat(document.getElementById(cityId),cityId,gpId,vId);
				});
				
				$('#' + gpId).select2();
			}
			
		}else{
			if (!isEmpty(obj)) {
				var selectedLocality = $('#' + disId).val();
				jQuery.post("farmer_populateCity.action", {
					id1 : obj.value,
					dt : new Date(),
					selectedLocality : obj.value
				}, function(result) {
					insertOptions(cityId, $.parseJSON(result));
					listVillageByCity(document.getElementById(cityId),cityId,vId);
				});
				$('#' + vId).select2();
			}
		}
		
	}

	function listPanchayat(obj, cityId, gpId, vId) {
		if (isGramPanchayatEnable != '1') {
			listVillage(document.getElementById(cityId), cityId, gpId,
					vId);
			return true
		}

		if (!isEmpty(obj)) {
			try {
				var selectedCity = $('#' + cityId).val();

				jQuery.post("farmer_populatePanchayath.action", {
					id : obj.value,
					dt : new Date(),
					selectedCity : obj.value
				}, function(result) {
					insertOptions(gpId, $.parseJSON(result));
					listVillage(document.getElementById(gpId), cityId, gpId,vId);
				});
			} catch (e) {
			}
			$('#' + vId).select2();
		}
	}

	function listVillage(obj,cityId,gpId, vId) {
		if (!isEmpty(obj)) {
			if (isGramPanchayatEnable == '1') {
				var selectedPanchayat = $('#' + gpId).val();
				var enableGramPanch = document
						.getElementById("gramPanchayatEnable").value;
				jQuery.post("farmer_populateVillage.action", {
					id1 : obj.value,
					dt : new Date(),
					selectedPanchayat : obj.value,
					gramPanchayatEnable : enableGramPanch
				}, function(result) {
						insertOptions(vId, $.parseJSON(result));
					
				});
			}else {
				listVillageByCity(document.getElementById(cityId), cityId,
						vId);
			}
		}
	}



	function listVillageByCity(obj, cityId,vId) {
		if (!isEmpty(obj)) {
			var selectedCity = $('#' + cityId).val();
			jQuery.post("farmer_populateVillageByCity.action", {
				id : obj.value,
				dt : new Date(),
				selectedCity : obj.value
			}, function(result) {
					insertOptions(vId, $.parseJSON(result));
					//listFarmer(document.getElementById(vId));
				// listSamithi(document.getElementById("village"));
			});
			
			$('#' + vId).select2();
		}
		
		
	}
	
	function insertOptions(ctrlName, jsonArr) {
		document.getElementById(ctrlName).length = 0;
		addOption(document.getElementById(ctrlName), "Select", "");
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
	
	function validateEmail(emailField){
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

        if (reg.test(emailField.value) == false) 
        {
          //  alert('');
            jQuery(".ferror").text("<s:property value="%{getLocaleProperty('Invalid Email Address')}" />");
            return false;
        }

        return true;

}
</script>

<body>
	<s:form name="form" cssClass="fillform" action="samithi_%{command}">
		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden id="samithiId" key="warehouse.id" class="uId"/>
		
		<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />
		<s:hidden id="dynamicListArray" name="dynamicListArray" />
		<s:hidden id="txnTypez" name="txnTypez" />		
		
		<s:hidden id="errorId" key="errorMsg" />
		<s:hidden id="agentMapExitsId" key="agentMappingExist" />
		<s:hidden id="farmerMapExitsId" key="farmerMappingExist" />
		<s:hidden id="existingVillagesId" key="selectedVillageCodes" />
		<s:hidden id="existingCitysId" key="selectedCityids" />
		<s:hidden key="command" />
			<s:hidden id="jsonString" name="warehouse.jsonString" />


		<div class="appContentWrapper marginBottom">

			<div class="error ferror">
				<sup>*</sup>
				<s:text name="reqd.field" />
				<s:actionerror />
				<s:fielderror />
			</div>

			<div class="formContainerWrapper">
				<s:if
					test="getCurrentTenantId()=='pratibha' && getBranchId()=='bci' || getBranchId()=='organic'">
					<h2>
						<s:property value="%{getLocaleProperty('info.samithi.bci')}" />
					</h2>
				</s:if>
				<s:else>
					<h2>
						<s:property value="%{getLocaleProperty('info.samithi')}" />
					</h2>
				</s:else>
				<div class="flexiWrapper">
					<s:if test='"update".equalsIgnoreCase(command)'>
						<div class="flexform-item">
							<label for="txt"><s:text name="samithi.code" /><sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:property value="warehouse.code" />
								<s:hidden key="warehouse.code" />
							</div>
						</div>
					</s:if>
					<s:if test='branchId==null'>
						<div class="flexform-item">
							<label for="txt"><s:text name="app.branch" /> </label>
							<div class="form-element">
								<s:property value="%{getBranchName(warehouse.branchId)}" />
							</div>
						</div>
					</s:if>
					<s:if test="getCurrentTenantId()=='atma' ">
						<div class="flexform-item">
							<label for="txt"><s:text name="sangham.name" /> <sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select cssClass="form-control input-sm " name="sangham"
									id="sangham" list="SanghamList" listKey="key" listValue="value"
									headerKey="" headerValue="%{getText('txt.select')}"
									theme="simple" />
							</div>
						</div>
					</s:if>


					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('samithi.name')}" /> </label>
						<div class="form-element showAge">
							<s:textfield id="samithiName" name="warehouse.name"
								theme="simple" maxlength="45" cssClass="form-control" />
						</div>
					</div>

					<s:if
						test="getCurrentTenantId()!='chetna' && getCurrentTenantId()!='pratibha' ">
						
						
							<s:if
						test="getCurrentTenantId()=='symrise' ">
				
						<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.groupFormationDate')}" />
						</label>
						<div class="form-element">
						
						<%-- <s:if test='"update".equalsIgnoreCase(command) && getCurrentTenantId()=="symrise"'>
							<s:date name="warehouse.groupFormationDate" format="dd/MM/YYYY" />
							</s:if>
							<s:else> --%>
										
							<s:textfield name="groupFormationDate"
								id="groupFormationCalendar" readonly="true" theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								cssClass="date-picker form-control calender" />
								<%-- 	</s:else> --%>
									
						
						</div>
					</div>
					</s:if>
					<s:else>
					<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('dateOfFormation')}" /></label>
							<div class="form-element">
							<s:textfield data-date-format="dd/mm/yyyy"
									data-date-viewmode="years"
									cssClass="date-picker form-control input-sm" id="fromCalendar"
									name="formationDate" size="23" readonly="true" />
							</div>
						</div>
					</s:else>
						
					</s:if>

					<s:if test="isKpfBased==1">
					<s:if test="getCurrentTenantId()!='gsma' && getCurrentTenantId()!='ecoagri'">
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('group.type')}" /><sup
								style="color: red;">*</sup></label>
							<div class="form-element">
								<s:select id="groupType" list="mTypeList"
									name="warehouse.groupType" cssClass="form-control select2"
									headerKey="" headerValue="%{getText('txt.select')}" />
							</div>
						</div>
					</s:if></s:if>
<s:if test="getCurrentTenantId()=='chetna'">
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.totalMembers')}" /> </label>
						<div class="form-element showAge">
							<s:textfield id="totalMembers" name="warehouse.totalMembers"
								theme="simple" onkeypress="return isNumber(event)" maxlength="3"
								cssClass="form-control" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.groupFormationDate')}" />
						</label>
						<div class="form-element">
							<s:textfield name="groupFormationDate"
								id="groupFormationCalendar" readonly="true" theme="simple"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								cssClass="date-picker form-control calender" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.noOfMale')}" /> </label>
						<div class="form-element showAge">
							<s:textfield id="noOfMale" name="warehouse.noOfMale"
								theme="simple" onkeypress="return isNumber(event)" maxlength="3"
								cssClass="form-control" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.noOfFemale')}" /> </label>
						<div class="form-element showAge">
							<s:textfield id="noOfFemale" name="warehouse.noOfFemale"
								theme="simple" onkeypress="return isNumber(event)" maxlength="3"
								cssClass="form-control" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.presidentName')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield id="presidentName" name="warehouse.presidentName"
								theme="simple" maxlength="45" cssClass="upercls form-control" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.presidentMobileNumber')}" />
						</label>
						<div class="form-element">
							<s:textfield name="warehouse.presidentMobileNumber"
								theme="simple" maxlength="10" cssClass="form-control"
								onkeypress="return isNumber(event)" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.secretaryName')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield id="secretaryName" name="warehouse.secretaryName"
								theme="simple" maxlength="45" cssClass="upercls form-control" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.secretaryMobileNumber')}" />
						</label>
						<div class="form-element">
							<s:textfield name="warehouse.secretaryMobileNumber"
								theme="simple" maxlength="10" cssClass="form-control "
								onkeypress="return isNumber(event)" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.treasurer')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield id="treasurer" name="warehouse.treasurer"
								theme="simple" maxlength="45" cssClass="upercls form-control" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.treasurerMobileNumber')}" />
						</label>
						<div class="form-element">
							<s:textfield name="warehouse.treasurerMobileNumber"
								theme="simple" maxlength="10" cssClass="form-control "
								onkeypress="return isNumber(event)" />
						</div>
					</div>

</s:if>

<s:if test="getCurrentTenantId()=='kenyafpo'">

<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('warehouse.presidentName')}" /> 
						</label>
						<div class="form-element">
							<s:textfield id="presidentName" name="warehouse.presidentName"
								theme="simple" maxlength="45" cssClass="upercls form-control" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.presidentMobileNumber')}" />
						</label>
						<div class="form-element">
							<s:textfield name="warehouse.presidentMobileNumber"
								theme="simple" maxlength="10" cssClass="form-control"
								onkeypress="return isNumber(event)" />
						</div>
					</div>
					
						<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('warehouse.email')}" />
						</label>
						<div class="form-element">
							<s:textfield name="warehouse.email" id="email"
								theme="simple" maxlength="45" cssClass="form-control"
								onblur="validateEmail(this);" />
						</div>
					</div>

<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('country.name')}" /><sup
								style="color: red;">*</sup></label>
						<div class="form-element showAge">
								<s:select class="form-control  select2" name="selectedCountry"
								id="country" list="countriesLang" headerKey=""
								headerValue="%{getText('txt.select')}"
								onchange="listState(this,'country','state','localities','city','panchayath','village')" />
						</div>
					</div>
<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('state.name')}" /><sup
								style="color: red;">*</sup></label>
						<div class="form-element showAge">
							<s:select class="form-control  select2" list="states"
								headerKey="" theme="simple" name="selectedState"
								headerValue="%{getText('txt.select')}" Key="id" Value="name"
								id="state" onchange="listLocality(this,'state','localities','city','panchayath','village')" />
						</div>
					</div>
<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('locality.name')}" /><sup
								style="color: red;">*</sup></label>
						<div class="form-element showAge">
							<s:select cssClass="form-control  select2"
								name="selectedLocality" id="localities" list="listLocalities"
								headerKey="" headerValue="%{getText('txt.select')}" Key="id"
								Value="name" theme="simple"
								onchange="listMunicipality(this,'localities','city','panchayath','village')" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('city.name')}" /><sup
								style="color: red;">*</sup></label>
						<div class="form-element showAge">
							<s:select cssClass="form-control  select2" id="city"
								name="selectedCity" list="cities" headerKey=""
								headerValue="%{getText('txt.select')}" Key="id" Value="name"
								theme="simple"
								onchange="listPanchayat(this,'city','panchayath','village')" />
						</div>
					</div> 
					<s:if test="gramPanchayatEnable==1">
						<div class="flexform-item locationInfo enableGramPanchayat">
							<label for="txt"><s:property
									value="%{getLocaleProperty('panchayat.name')}" /> </label>
							<div class="form-element">
								<s:select cssClass="form-control  select2" id="panchayath"
									name="selectedPanchayat" list="panchayath" headerKey=""
									headerValue="%{getText('txt.select')}" Key="id" theme="simple"
									Value="name"
									onchange="listVillage(this,'city','panchayath','village')" />

								
							</div>
						</div>
					</s:if>
					<div class="flexform-item">
							<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /> <sup
							style="color: red;">*</sup> </label>
						<div class="form-element showAge">
							<s:select cssClass="form-control  select2" name="selectedVillage"
								id="village" list="villages" headerKey="" Key="id" Value="name"
								headerValue="%{getText('txt.select')}" theme="simple" />
						</div>
					</div> 
					
					
					
					
					
					
					</s:if>
				</div>

			</div>
		</div>

<s:if test="getCurrentTenantId()=='chetna'">
		<div class="appContentWrapper marginBottom bank_info">

			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion" href="#bankInfo"
						class="accrodianTxt collapsed"> <s:text name="info.bank" />
					</a>
				</h2>
			</div>
			<div class="table-responsive" id="bankInfo">
				<div class="pull-right">
					<input type="button" value="<s:text name="add.button"/>"
						id="addBankDetail"
						class="addBankInfo slide_open secondaryBtn btn-success"
						data-toggle="modal" data-target="#slide" data-backdrop="static"
						onclick="openModal();" data-keyboard="false"> <input
						type="button" style="display: none" id="editBankDetailModal"
						class="addBankInfo slide_open" data-toggle="modal"
						data-target="#slide" data-backdrop="static" data-keyboard="false">
				</div>
				<table id="bankTable"
					class="table table-bordered aspect-detail fixedTable">
					<thead>
						<tr class="border-less">
							<th width="20%"><s:text
									name="warehouse.bankInfo.AccountTypeProp" /></th>
							<th width="15%"><s:text name="warehouse.bankInfo.accNo" /></th>
							<th width="15%"><s:text name="warehouse.bankInfo.bankName" /></th>
							<th width="20%"><s:text name="warehouse.bankInfo.branchName" /></th>
							<th width="15%"><div>
									<s:text name="warehouse.bankInfo.sortCode" /></th>
							<th width="20%"><s:text name="action" /></th>

						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>

		</div>
</s:if>

	<div class="dynamicFieldsRender"></div>
		<div id="slide" class="modal fade" role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" id="model-close-btn" onclick="closeModal();"
							class="close" data-dismiss="modal">&times;</button>
						<h4 style="border-bottom: solid 1px #567304;">
							<s:text name="info.bank" />
						</h4>
					</div>

					<div class="modal-body">
						<div class="modal-form">
							<input type="hidden" name="hidId" id="hidId" />
							<div class="bankError" style="color: red"></div>
							<div class="modal-form-item">
								<label><s:text name="warehouse.bankInfo.AccountTypeProp" />
									<sup style="color: red;">*</sup></label>
								<s:select class="form-control" id="accountType" listKey="key"
									listValue="value" name="accType" list="accountTypeList"
									headerKey="-1" headerValue="%{getText('txt.select')}" />
							</div>

							<div class="modal-form-item">
								<label><s:text name="warehouse.bankInfo.accNo" /> <sup
									style="color: red;">*</sup></label> <input type="text"
									class="form-control" id="accNo" name="accNo" maxlength="35">
							</div>
							<s:if test="currentTenantId!='chetna'">
								<div class="modal-form-item">
									<label><s:text name="warehouse.bankInfo.bankName" /> <sup
										style="color: red;">*</sup></label> <input type="text"
										class="form-control" id="bankName" name="bankName"
										maxlength="35">
								</div>

								<div class="modal-form-item">
									<label><s:text name="warehouse.bankInfo.branchName" />
										<sup style="color: red;">*</sup></label> <input type="text"
										class="form-control" id="branchName" name="branchName">
								</div>
							</s:if>
							<s:else>
								<div class="modal-form-item">
									<label><s:text name="warehouse.bankInfo.bankName" /> <sup
										style="color: red;">*</sup></label>
									<s:select class="form-control" style="width:200px"
										id="bankNameList" listKey="key" listValue="value"
										name="bankName" list="bankNameList" headerKey="-1"
										headerValue="%{getText('txt.select')}" />
								</div>

								<div class="modal-form-item">
									<label><s:text name="warehouse.bankInfo.branchName" />
										<sup style="color: red;">*</sup></label>
									<s:select class="form-control" style="width:200px"
										id="branchNameList" listKey="key" listValue="value"
										name="branchName" list="branchNameList" headerKey="-1"
										headerValue="%{getText('txt.select')}" />
								</div>


							</s:else>
							<div class="modal-form-item">
								<label><s:text name="warehouse.bankInfo.sortCode" /> <sup
									style="color: red;">*</sup></label> <input type="text"
									class="form-control" id="sortCode" name="sortCode"
									maxlength="35">
							</div>
						</div>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-sts" id="saveBankDetail"
							onclick="saveBankInformation(this)">
							<s:text name="save.button" />
						</button>
						<button type="button" class="btn btn-primary"
							onclick="cancelBnkInfo();" id="buttonCancel" data-dismiss="modal">
							<s:text name="cancel" />
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="margin-top-10">
			<s:if test="command =='create'">
				<span class=""><span class="first-child">
						<button type="button" class="save-btn btn btn-success"
							onclick="validate()">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
				</span></span>
			</s:if>
			<s:else>
				<span class=""><span class="first-child">
						<button type="button" onClick="validate()"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
				</span></span>
			</s:else>
			<span id="cancel" class=""><span class="first-child"><button
						type="button" class="cancel-btn btn btn-sts">
						<b><FONT color="#FFFFFF"><s:text name="cancel.button" />
						</font></b>
					</button></span></span>
		</div>

		<s:hidden id="municipalityIds" name="municipalityIds"></s:hidden>
	</s:form>
	<s:form name="cancelform" action="samithi_list.action">
		<s:hidden key="currentPage" />
	</s:form>

</body>