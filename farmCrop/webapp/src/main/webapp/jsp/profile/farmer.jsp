<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>

<script src="js/farmer.js?v=4.9"></script>
<script src="js/dynamicComponents.js?v=20.23"></script>

<style>
.hide {
  display: none; }
  
 .wizard-wrapper {
          display: -webkit-box;
          display: -ms-flexbox;
          display: flex; }
.wizard-icon {
          font-size: 2.5rem;
          margin-right: 1.1rem;
          -webkit-transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
          transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, -webkit-box-shadow 0.15s ease;
          transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease;
          transition: color 0.15s ease, background-color 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease, -webkit-box-shadow 0.15s ease; }
.wizard-icon .svg-icon svg g [fill] {
            -webkit-transition: fill 0.3s ease;
            transition: fill 0.3s ease;
            fill: #B5B5C3; }
.wizard-icon .svg-icon svg:hover g [fill] {
            -webkit-transition: fill 0.3s ease;
            transition: fill 0.3s ease; }
.wizard-label {
          display: -webkit-box;
          display: -ms-flexbox;
          display: flex;
          -webkit-box-orient: vertical;
          -webkit-box-direction: normal;
          -ms-flex-direction: column;
          flex-direction: column;
          -webkit-box-pack: center;
          -ms-flex-pack: center;
          justify-content: center; }
.wizard-label .wizard-title {
            color: #181C32;
            font-weight: 500;
            font-size: 1.1rem; }
.wizard-label .wizard-desc {
            color: #7E8299; }
.collapse-icon-custom{
margin-top: -35px;
}
</style>

<script>
var tenant='<s:property value="getCurrentTenantId()"/>';
var isGramPanchayatEnable = '<s:property value="gramPanchayatEnable"/>';
var command ='<s:property value="command"/>';
jQuery(document).ready(function() {

	var tenant='<s:property value="getCurrentTenantId()"/>';
	//alert(tenant);
	var type= '<%=request.getParameter("type")%>';
	$(".type").val(type);
	 if(type=='2'){
			$(".breadCrumbNavigation").html('');
			$(".breadCrumbNavigation").append("<li><a href='#'>Profile</a></li>");
			//$(".breadCrumbNavigation").append("<li><a href='#'>IRP</a></li>");
			$(".breadCrumbNavigation").append("<li><a href='farmer_list.action?type=2'>IRP</a></li>");
			$("#type").val(type);
	}
	 
	if(tenant=="iccoa" || tenant=="susagri"){
		$("input[name='farmer.isCertifiedFarmer'][value='1']").attr("checked", true);
		$(".certified").removeClass("hide");
		$("#certificationType").val("1");
		
		var targetObj =  $(".education");
	 	var srcObj = $(".farmerHealthIns");
	 	$(srcObj).insertAfter(targetObj);
	 	
		
	}
	
	if(tenant=="farmAgg"){
		$("input[name='farmer.isCertifiedFarmer'][value='1']").attr("checked", true);
		$(".certified").removeClass("hide");
		$("#certificationType").val("1");
	
	}
	
	
	
	
	/* else if(tenant=="pgss"||tenant=="PGSS"){		
		var targetObj =  $("#prefWrk").parent().parent();		
	 	var srcObj = $("#Source_Of_Obtaining_Tool").parent().parent();		
	 	$(targetObj).insertAfter(srcObj);
	} */
	$("#calendar").change(function(v){
		calculateAge();
	});
	if('<s:property value="command"/>'=="update"){
		calTotalHshld();
	}
	if(tenant=="kenyafpo"){
		$("input[name='farmer.isCertifiedFarmer'][value='0']").attr("checked", true);
		$(".certified").removeClass("hide");
		$("#certificationType").val("0");
		$('.dwelling_info').removeClass("hide");
	}
	dynamicDependsId();
	hideFields();
	renderDynamicFeilds();
	$('.select2').select2();//for dynamic multi drop down style
	
	$.ajax({
        type: "POST",
            async: false,
            url: "farmer_populateHideFn.action",
            data:{type:type},
            success: function(result) {
          var farmerFieldsArray = jQuery.parseJSON(result);
             $.each(farmerFieldsArray, function(index, value) {
           
              if(index=='activeFields'){
                $(value).each(function(k,v){
                 if(v.id=='1'){
                  showByEleName(v.name);
                 }if(v.id=='2'){
                  showByEleId(v.name);
                 }if(v.id=='3'){
                  showByEleClass(v.name);
                 }if(v.id=='4'){
                  $("."+v.name).removeClass("hide");
                 } if(v.id=='5'){
                  hideByEleClass(v.name);
                 }
                });
              }
              else if(index=='destroyFileds'){
                $(value).each(function(k,v){
                 $("."+v.name).remove();
                });
              } 
              
           });
      }
    });
    $(".farmerDynamicField").removeClass("hide");
    /* $(".isCertifiedFarmer").addClass("hide"); */
	
	if('<s:property value="command"/>'=="update"){
		setDynamicFieldUpdateValues();
	}
	if('<s:property value="command"/>'=="create"){
	if(tenant=="livelihood"){
		$('#country').val('Philippines').trigger('change');
		$('.farmerCode').addClass("hide");
		
	}else if(tenant=="griffith"){
		$('.farmer_info').addClass("hide");
		$('.farmerCode').addClass("hide");
		
	}
	else{
		$('.farmerCode').removeClass("hide");
	}
	}
	
	
	if(tenant=="livelihood"){
		$('.farmerCode').addClass("hide");	
	}else if(tenant=="griffith"){
		$('.farmer_info').addClass("hide");
		$('.farmerCode').addClass("hide");
		
	}	else{
		$('.farmerCode').removeClass("hide");
	}
    if(tenant=='olivado')
    {
        jQuery(".ggn").addClass("hide");
    	jQuery(".ggnRegNo").addClass("hide");
    	jQuery(".contactName").removeClass("hide");
    }
    else
    {
    	jQuery(".ggn").addClass("hide");
    	jQuery(".ggnRegNo").addClass("hide");
    	jQuery(".contactName").addClass("hide");
    }
	
	
    	jQuery(".proofNoInput").addClass("hide");
    	jQuery("#proofNoInputVal").addClass("hide");
    	jQuery(".proofIdOther").addClass("hide");
    	jQuery("#proofIdOtherVal").addClass("hide");
    	
    	var idProff='<s:property value="farmer.idProof"/>';
    	processIdProof(idProff);
   
    	<s:if test="currentTenantId =='efk'">
	    	jQuery(".idType").removeClass("hide");
	        $("#proofNoInputVal").removeClass("hide");
        </s:if>
        if('<s:property value="command"/>'=="update"){
    		
    		if(tenant=="griffith"){
    			$('.farmer_info').removeClass("hide");
    			$('.farmerCode').removeClass("hide");
    			  $('#farmerCode').attr('readonly', true);
    		}	
    	}
        
       
        
        if('<s:property value="getBranchId()"/>'=='Individual'){
        	
    		$(".individual").removeClass("hide");
    	//	jQuery("#nameLab").html("<s:property value="%{getLocaleProperty('farmer.nameInv')}"/>")
    		
    		jQuery("#nameLab").html("").html("<s:property value="%{getLocaleProperty('farmer.nameInv')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#adarNo").html("").html("<s:property value="%{getLocaleProperty('adhaarInv')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#dateName").html("").html("<s:property value="%{getLocaleProperty('farmer.dateOfBirth')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#telephone").html("").html("<s:property value="%{getLocaleProperty('farmer.mobileno')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#phNumber").html("").html("<s:property value="%{getLocaleProperty('farmer.phNum')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#addInfo").html("").html("<s:property value="%{getLocaleProperty('farmer.address')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#cnName").html("").html("<s:property value="%{getLocaleProperty('country.nameInv')}"/>");	
    		jQuery("#stName").html("").html("<s:property value="%{getLocaleProperty('stNameInv')}"/>");
    		jQuery("#locName").html("").html("<s:property value="%{getLocaleProperty('locNameInv')}"/>");
    		jQuery("#ctName").html("").html("<s:property value="%{getLocaleProperty('ctNameInv')}"/>");
    		jQuery("#gpName").html("").html("<s:property value="%{getLocaleProperty('gpNameInv')}"/>");
    		jQuery("#villName").html("").html("<s:property value="%{getLocaleProperty('villNameInv')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		
    		var targetObj =  $(".adhaar");
    		var srcObj = $(".mobileMandatory");
    	 	$(srcObj).insertAfter(targetObj);
    	 	
    	 	var targetObj =  $(".mobileMandatory");
    		var srcObj = $(".phNumber");
    	 	$(srcObj).insertAfter(targetObj);
    	 	
    		
    	}
        
    if('<s:property value="getBranchId()"/>'=='Corporate'){
        	
    	jQuery("#adarNo").html("").html("<s:property value="%{getLocaleProperty('adhaarCorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#nameLab").html("").html("<s:property value="%{getLocaleProperty('farmer.nameCorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#cnName").html("").html("<s:property value="%{getLocaleProperty('country.nameCorp')}"/>");
    		jQuery("#dateName").html("").html("<s:property value="%{getLocaleProperty('farmer.dateofcorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		
    		jQuery("#stName").html("").html("<s:property value="%{getLocaleProperty('stNameCorp')}"/>");
    		jQuery("#locName").html("").html("<s:property value="%{getLocaleProperty('locNameCorp')}"/>");
    		jQuery("#ctName").html("").html("<s:property value="%{getLocaleProperty('ctNameCorp')}"/>");
    		jQuery("#gpName").html("").html("<s:property value="%{getLocaleProperty('gpNameCorp')}"/>");
    		jQuery("#villName").html("").html("<s:property value="%{getLocaleProperty('villNameCorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#telephone").html("").html("<s:property value="%{getLocaleProperty('farmer.mobilenoCorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#addInfo").html("").html("<s:property value="%{getLocaleProperty('farmer.addressCorp')}"/>").append($("<sup style='color: red;'/>").html("*"));
    		jQuery("#emailName").append($("<sup style='color: red;'/>").html("*"));
    		
    		var targetObj =  $(".adhaar");
    		var srcObj = $(".mobileMandatory");
    	 	$(srcObj).insertAfter(targetObj);
      }  
        
        
//	jQuery(".idTypeOther").addClass("hide");
	if(command=="update")
	{
		
	var idp=$("#idProof").val();
	//alert(idp);
		processIdProof(idp);
	}

	 var selectedWrkDiff =  '<s:property value="farmer.workDiffficulty" />';
	 if(!isEmpty(selectedWrkDiff)){
		 var values = selectedWrkDiff.split("\\,");
		  $.each(selectedWrkDiff.split(","), function(i,e){
			  $("#workDiffficulty option[value='" + e.trim() + "']").prop("selected", true);
		  });
	 }
	 
	 var selectedHomeDiff =  '<s:property value="farmer.homeDifficulty" />';
	 if(!isEmpty(selectedHomeDiff)){
		 var values = selectedHomeDiff.split("\\,");
		  $.each(selectedHomeDiff.split(","), function(i,e){
			  $("#homeDifficulty option[value='" + e.trim() + "']").prop("selected", true);
		  });
	 }
	 
	 var selectedCommDiff =  '<s:property value="farmer.communitiyDifficulty" />';
	 if(!isEmpty(selectedCommDiff)){
		 var values = selectedCommDiff.split("\\,");
		  $.each(selectedCommDiff.split(","), function(i,e){
			  $("#communitiyDifficulty option[value='" + e.trim() + "']").prop("selected", true);
		  });
	 }
	 
	// Multi selected for  Agricultural Implements

	 var selectedImplements =  '<s:property value="farmer.agricultureImplements" />';
	 if(!isEmpty(selectedImplements)){
		 var values = selectedImplements.split("\\,");
		  $.each(selectedImplements.split(","), function(i,e){
			  //alert(e.trim());
			  $("#agricultureImplements option[value='" + e.trim() + "']").prop("selected", true);
		  });
	 }
	
	var tenant='<s:property value="getCurrentTenantId()"/>';
	if(tenant=='blri')
	{
		$('.divHideBlri').addClass("hide");
	}

	
    changeInsuranceDiv();
    /* jQuery(".toiletAvailableRow1").addClass("hide"); 
    jQuery("#toiletAvailableRow2").addClass("hide"); */
    jQuery(".toiletAvailable").addClass("hide");
    var val='<s:property value="farmer.farmerEconomy.toiletAvailable"/>';
	if(val=="1")
	{
		jQuery(".toiletAvailable").removeClass("hide");
		/* jQuery(".toiletAvailableRow1").removeClass("hide");
		jQuery("#toiletAvailableRow2").removeClass("hide"); */
		}
	else{
	    jQuery(".toiletAvailable").addClass("hide");
/* 		jQuery(".toiletAvailableRow1").addClass("hide");
		jQuery("#toiletAvailableRow2").addClass("hide"); */
	}
	
/*	jQuery(".proofNoInput").addClass("hide");
	jQuery("#proofNoInputVal").addClass("hide");
	jQuery(".proofIdOther").addClass("hide");
	jQuery("#proofIdOtherVal").addClass("hide");
	jQuery(".idType").addClass("hide");
    jQuery(".idTypeOther").addClass("hide");*/
	
    var val='<s:property value="farmer.smartPhone"/>';
    if(val=="1")
    	{
    	jQuery(".cellPhone").removeClass("hide");
    	}else{
    	jQuery(".cellPhone").addClass("hide");
    	}
	var tenant='<s:property value="getCurrentTenantId()"/>';
    var command ='<s:property value="command"/>';
   // alert(command);

      if(command=="update")
    	{
    	var idp=jQuery("#idProof").val();
    		processIdProof(idp);
    	}
     if(tenant=='chetna'){
    	
/*     	 	if('<s:property value="farmer.lifeInsurance"/>'==2){
        		$('#life2').attr("checked","checked");
        		$('.lifeDiv').addClass("hide");
        	}else if('<s:property value="farmer.lifeInsurance"/>'==1){
        		$('#life1').attr("checked","checked");
        		$('.lifeDiv').removeClass("hide");
        		
        		
        	} 
        	if('<s:property value="farmer.healthInsurance"/>'==2){
        		$('#health2').attr("checked","checked");
        		$('.healthDiv').addClass("hide");
        		
        	}else if('<s:property value="farmer.healthInsurance"/>'==1){
        		$('#health1').attr("checked","checked");
        		$('.healthDiv').removeClass("hide");    	
        		
        	} */
   	
        	
        	
    	 // Multiselect option for Cooking Fuel
		 var selectedCookingFuel =  '<s:property value="farmer.farmerEconomy.cookingFuel" />';
		 if(selectedCookingFuel!=null && selectedCookingFuel.trim()!=""){
		  var values = selectedCookingFuel.split("\\,");
		  
		  $.each(selectedCookingFuel.split(","), function(i,e){
			if (e.trim() == 99) {
					jQuery(".cookingFuelOther").removeClass("hide");
					jQuery("#cookingFuelOtherVal").val('<s:property value="farmer.farmerEconomy.cookingFuelSourceOther" />');
				} else {
					jQuery("#cookingFuelOtherVal").val("");
					jQuery(".cookingFuelOther").addClass("hide");
				}
			  
		      $("#cookFuel option[value='" + e.trim() + "']").prop("selected", true);
		  });
		  $("#cookFuel").select2();
		 }
		 else{
			 jQuery(".cookingFuelOther").addClass("hide");
		 }
		 
	
		 
		 // Multiselect Government Department
		  $("#calender").datepicker(
			//defaultDate : new Date(getTwentyYearsBack(), 01, 00)
	 			 //dateFormat: 'mm/dd/yy',
			{
				//maxDate: '0',
				//beforeShow : function()
				//{
				//jQuery( this ).datepicker({ maxDate: 0 });
				//},
				dateFormat: 'dd-mm-yy',
				changeMonth: true,
				changeYear: true
			}
			); 

		
		/*  $("#beneficiaryDepartment").addClass("select2Multi");
		 $('.select2Multi').selectize({
 			 plugins: ['remove_button'],
 			 delimiter: ',',
 			 persist: false, 		
 			}); */
		 
   } 
     
     var selectedGovtDept = '<s:property value="farmer.govtDept" />';
	 if(selectedGovtDept!=null && selectedGovtDept.trim()!=""){
	 		var depts = selectedGovtDept.split("\\,"); 	 		
	 		$.each(selectedGovtDept.split(","), function(i,e){
	 			$("#beneficiaryDepartment option[value='" + e.trim() + "']").prop("selected", true);
	 			  }); 
	 		$("#beneficiaryDepartment").select2();
	 }

      if(tenant!='pgss'){
    	 /* $(".hideAge").addClass("hide");
    	 $(".showAge").removeClass("hide");
    	 $(".hideLoanTakenScheme").addClass("hide");
    	 $(".hideMandatory").removeClass("hide");   */
    }
     else{
    	 /* $(".showAge").addClass("hide");
    	 $(".hideAge").removeClass("hide");
    	 jQuery(".loan_info").removeClass("hide");
    	 $(".hideLoanTakenScheme").removeClass("hide");
    	 $(".hideMandatory").addClass("hide");  */
     } 
    
     
  // Multiselect option for Cooking Fuel
	 var selectedCookingFuel =  '<s:property value="farmer.farmerEconomy.cookingFuel" />';
	 if(selectedCookingFuel!=null && selectedCookingFuel.trim()!=""){
	  var values = selectedCookingFuel.split("\\,");
	  
	  $.each(selectedCookingFuel.split(","), function(i,e){
		if (e.trim() == 99) {
				jQuery(".cookingFuelOther").removeClass("hide");
				jQuery("#cookingFuelOtherVal").val('<s:property value="farmer.farmerEconomy.cookingFuelSourceOther" />');
			} else {
				jQuery("#cookingFuelOtherVal").val("");
				jQuery(".cookingFuelOther").addClass("hide");
			}
		  
	      $("#cookFuel option[value='" + e.trim() + "']").prop("selected", true);
	  });
	  $("#cookFuel").select2();
	 }
	 else{
		 jQuery(".cookingFuelOther").addClass("hide");
	 }
	 
	   
	 
    	function calculateAge()
		{
			if($('#calendar').val()!=null&&$('#calendar').val()!='undefined'&&$('#calendar').val()!=''){
				/*  var dob = $('#calendar').val().split('-')[2];
		         var today = new Date(); 
		         alert(dob);
		         $('#age').text(parseInt(today.getFullYear()) - parseInt(dob));
		         alert(parseInt(today.getFullYear()) - parseInt(dob));
		         $('#ageHide').val(parseInt(today.getFullYear()) - parseInt(dob)); */
		         
		         
		         var age =  $('#calendar').val().replace(/[^\w\s]/gi, '');
		         dob = age.substring(4,8);
		         
		         var today = new Date(); 
		         $('#age').val(parseInt(today.getFullYear()) - parseInt(dob));
		         $('#farmerAge').text(parseInt(today.getFullYear()) - parseInt(dob));
		         $('#ageHide').val(parseInt(today.getFullYear()) - parseInt(dob));
		         
				}else{
						   $('#age').val(0);
						   $('#farmerAge').text(0);
					        // alert(parseInt(today.getFullYear()) - parseInt(dob));
					         $('#ageHide').val(0);
					}
		}
	
	
    	
	$('.isAlpahaNumeric').keypress(function (e) {
	    var regex = new RegExp("^[a-zA-Z0-9]+$");
	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	    if (regex.test(str)) {
	        return true;
	    }

	    e.preventDefault();
	    return false;
	});
	var isBeneficiaryInGovScheme='<s:property value="farmer.isBeneficiaryInGovScheme"/>';
	if(isBeneficiaryInGovScheme=="1"){
/* 		$(".nameOfSchemeDiv").removeClass("hide");
		$(".governmentDepartment").removeClass("hide"); */
		jQuery(".beneficiaryScheme").removeClass("hide");
	}else{
		jQuery(".beneficiaryScheme").addClass("hide");
		/* $(".nameOfSchemeDiv").addClass("hide");
		$(".governmentDepartment").addClass("hide"); */
	}
	//benificiaryDiv($('input:radio[name="farmer.loanTakenLastYear"]:checked'));
	//disableCertificationTypeBySize(true);
	processEnrollmentPlace(jQuery("#enrollmentPlace").val())
	//cropOnChange($("#cropIns").val());

		//$('#buttonAdd').on('click',function(e){
		//validateImage();
		//document.getElementById("harvestedDate").value = document.getElementById("calendar").value;
		//document.form.action = "farmer_create.action";
		//document.form.submit();
		//});


		jsonString=$('#jsonString').val();
		if(jsonString!=null && jsonString!=''){
			var jsonObj=JSON.parse(jsonString);
			var accTypeText = "";
			for(var i=0;i<jsonObj.length;i++){
				var bankInformation=jsonObj[i];
				var id=(i+1);
				/* if(bankInformation.accType==1){
					accTypeText = "Personal Savings";
				}
				else if(bankInformation.accType==2){
					accTypeText = "Joint Savings";
				}
				else if(bankInformation.accType==3){
					accTypeText = "Jan Dhan";
				}
				else{
					accTypeText = "Other";
				} */
				
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
				}else if(tenant=='farmAgg'){
					$( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
							  "<td>" + bankInformation.accType + "</td>" +
							  "<td>" + bankInformation.accName + "</td>" +
					          "<td>" + bankInformation.accNo + "</td>" +
					          "<td>" + bankInformation.bankName + "</td>" +
					          "<td>" + bankInformation.branchName + "</td>" +
					          "<td>" + bankInformation.swiftCode + "</td>" +	
					          "<td>" + bankInformation.sortCode + "</td>" +	
					          "<td class='hide'>" + bankInformation.accTypeCode + "</td>" +
					          "<td><button id='"+id+""+","+""+bankInformation.accTypeCode+"'class='btn  btn-sts' type='button' onclick='editBank(this);'><label>Edit</label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' type='button' onclick='deleteBank(this);'><label>Delete</label></button></td>" +
					        "</tr>" );
				}
				else {
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
		// alert($('input:radio[name="farmer.isCertifiedFarmer"]:checked').val());

		certificationDiv(jQuery("#farmerCertified").val())
		loanTakenFromDiv($('input:radio[name="farmer.loanTakenLastYear"]:checked'));
		assistiveDeviceDiv($('input:radio[name="farmer.assistiveDevice"]:checked'));
		healthIssueDiv($('input:radio[name="farmer.healthIssue"]:checked'));
		

		/*if(tenant=="chetna"){
			
			 var selectedConsumerEle =  '<s:property value="farmer.consumerElectronics" />';
			 if(selectedConsumerEle!=null&&selectedConsumerEle.trim()!=""){
			  var values = selectedConsumerEle.split("\\,");
			  
			  $.each(selectedConsumerEle.split(","), function(i,e){
				  if (e.trim() == 99) {
						jQuery(".consumerElecOther").removeClass("hide");
					} else {
						jQuery(".consumerElecOther").addClass("hide");
					}
			      $("#consumerElectronics option[value='" + e.trim() + "']").prop("selected", true);
			  });
			 }	
			   $("#consumerElectronics").select2();
		} else{
		var val=$("#consumerElectronics").val();
		
		if (val == 99) {
			jQuery(".consumerElecOther").removeClass("hide");
		} else {    
			jQuery("#consumerElecOtherVal").val("");
			jQuery(".consumerElecOther").addClass("hide");
		}
	} */
		
		// Multi selected for Vechicle
		
		 var selectedVehicle =  '<s:property value="farmer.vehicle" />';
		 var vehicleOtherVal  = '<s:property value="farmer.vehicleOther" />';
		 if(selectedVehicle!=null&&selectedVehicle.trim()!=""){
		  var values = selectedVehicle.split("\\,");
		  var selectedValues = new Array();
		  $.each(selectedVehicle.split(","), function(i,e){
			  if (e.trim() == 99) {
					jQuery(".vehicleOther").removeClass("hide");
				} else {
					jQuery(".vehicleOther").addClass("hide");
				}
			  selectedValues[i] = e.trim();
		       //$("#vehicle option[value='" + e.trim() + "']").prop("selected", true); 
		  });
			 //alert(vehicleOtherVal);
		  	$("#vehicle").select2('val',selectedValues);
		  	 $("#vehicleOtherVal").val(vehicleOtherVal);
		 }	
		 
		 
		// Multi selected for Consumer Electronics
			
		 var selectedElectronics =  '<s:property value="farmer.consumerElectronics" />';
		 var consumerElecOtherVal  = '<s:property value="farmer.consumerElectronicsOther" />';
		 if(selectedElectronics!=null&&selectedElectronics.trim()!=""){
		  var values = selectedElectronics.split("\\,");
		  var selectedValues = new Array();
		  $.each(selectedElectronics.split(","), function(i,e){
			  if (e.trim() == 99) {
					jQuery(".consumerElecOther").removeClass("hide");
					//jQuery("#consumerElecOtherVal").val('<s:property value="farmer.consumerElectronicsOther" />');
				} else {
				//jQuery("#consumerElecOtherVal").val("");
					jQuery(".consumerElecOther").addClass("hide");
				}
			  selectedValues[i] = e.trim();
		       //$("#consumerElectronics option[value='" + e.trim() + "']").prop("selected", true); 
		  });
			 //alert(consumerElecOtherVal);
		  	$("#consumerElectronics").select2('val',selectedValues);
		  	 $("#consumerElecOtherVal").val(consumerElecOtherVal);
		 }	

		 
		// Multi selected for Family Members
		 var selectedFamilyMember =  '<s:property value="farmer.familyMember" />';
		 if(selectedFamilyMember!=null&&selectedFamilyMember.trim()!=""){
		  var values = selectedFamilyMember.split("\\,");
		
		  $.each(selectedFamilyMember.split(","), function(i,e){
			  if (e.trim() == "99") {
					jQuery(".familyMemberOther").removeClass("hide");
				} else {
					jQuery(".familyMemberOther").addClass("hide");
				}
		      $("#familyMember option[value='" + e.trim() + "']").prop("selected", true);
		  });
		 }	
		 		 
		 
		// Multi selected for Drinking Water
			
		 var selectedDrinkingWs =  '<s:property value="farmer.farmerEconomy.drinkingWaterSource" />';
		 var drinkingWSOtherVal =  '<s:property value="farmer.farmerEconomy.drinkingWaterSourceOther" />';
		 if(selectedDrinkingWs!=null && selectedDrinkingWs.trim()!=""){
		  var values = selectedDrinkingWs.split("\\,");
		  var selectedValues = new Array();
		  $.each(selectedDrinkingWs.split(","), function(i,e){
			  if (e.trim() == 99) {
					jQuery(".drinkingWSOther").removeClass("hide");
					//jQuery("#drinkingWSOtherVal").val('<s:property value="farmer.farmerEconomy.drinkingWaterSourceOther" />');
				} else {
					//jQuery("#drinkingWSOtherVal").val("");
					jQuery(".drinkingWSOther").addClass("hide");
				}
			  selectedValues[i] = e.trim();
			  
		  });
		  	$("#drinkingWS").select2('val',selectedValues);
		  	$("#drinkingWSOtherVal").val(drinkingWSOtherVal);
		 }	
		
		 // MultiSelect Crop Insurance
		 
		   var farmerCropInsurance = '<s:property value="farmer.farmerCropInsurance" />';
		  
           if (farmerCropInsurance != null && farmerCropInsurance.trim() != "") {
               var values = farmerCropInsurance.split("\\,");
				
               $.each(farmerCropInsurance.split(","), function (i, e) {
                   $("#cropNames option[value='" + e.trim() + "']")
                           .prop("selected", true);
                   
               });
               $("#cropNames").select2();
           }
           
		
		//end --//
// Multi selected for Family Members
		var tenant='<s:property value="getCurrentTenantId()"/>';
		/* if(tenant=='atma' ){
			hideValuesForApmas(document.getElementById("sangham"));	
		} */
		
		if(tenant=='atma' ){
			$('#incomeDetailId').addClass("hide");  
		}
		else{
			$('#incomeDetailId').removeClass("hide");
		}
		
		processOtherValue('<s:property value="farmer.loanPupose"/>',$('#loanPurpose'),false);
		processOtherValue('<s:property value="farmer.loanSecurity"/>',$('#loanSecurity'),false);
	
		calculateAge();
		
		 //  alert('<s:property value="getCurrentTenantId()"/>');
		   if('<s:property value="getCurrentTenantId()"/>'=='pratibha' || '<s:property value="getCurrentTenantId()"/>'=='welspun'  ){
		
		    	if('<s:property value="getBranchId()"/>'=='organic'){
		    	//	$('input:radio[name="farmer.isCertifiedFarmer"]').val(1);
		    		value = 1;
		    		$("input[name='farmer.isCertifiedFarmer'][value=" + value + "]").prop('checked', true);
		    		certificationDiv(null);
		    	}
			   	else{
					certificationDiv($('input:radio[name="farmer.isCertifiedFarmer"]:checked').val());
			    } 
		    }

	        $("#buttonAdd1").on('click', function (event) {  
	              event.preventDefault();
	              var el = $(this);
	              el.prop('disabled', true);
	              setTimeout(function(){el.prop('disabled', false); }, 1000);
	        });
	        
	        
	        populateInsertComponent();
	        loanTakenSchemeDiv($('input:radio[name="farmer.isLoanTakenScheme"]:checked'));
	        processDisType($('input:radio[name="selectedDisability"]:checked'));
	        //processDisabled($('input:radio[name="selectedDisabled"]:checked'));
	        
	if(command=='update'){
		 var  pgrup=  '<s:property value="farmer.positionGroup" />';
		  	$("#groupPosition").select2('val',pgrup);
	}
	
	if(tenant!='pgss')
    {
   
	certificationDiv(null);
    } 

	
	if(tenant == "efk"){
		var healthInsuranceAnswer =  '<s:property value="farmer.healthInsurance"/>';
		if(!isEmpty(healthInsuranceAnswer)){
			if(healthInsuranceAnswer == "NHIF"){
				document.getElementById("health_NHIF").checked=true;
				$('.healthDiv').show();
			}else if(healthInsuranceAnswer == "Other"){
				document.getElementById("health_Other").checked=true;
				$('.healthDiv').show();
			}
			
		}
	}
	
	var age = '<s:property value="farmer.age" />';
	$("#age").val(age);
	
});

function getTxnType(){
	var type= '<%=request.getParameter("type")%>';
	 if(type=='2'){
		 return "3008A";
	 }
	return "308";
}


function hideFields(){
	 var app = $(".appContentWrapper");
	 $(app).addClass("hide");
	 $(app).not(".farmerDynamicField").find(".flexform-item").each(function(){
	 	 $(this).addClass("hide");
	 });
	  
	}
	
function getBranchIdDyn(){
	return '<s:property value="getBranchId()"/>';
}


function loanTakenSchemeDiv(evt)
{

	var val=$(evt).val();
	if(val=='1'){
		jQuery(".loanTakenSchemeHide").addClass("hide");
		}
	else{
		jQuery(".loanTakenSchemeHide").addClass("hide");
	}
}


function populateInsertComponent()
{
	 jQuery.post("farmer_populateDynamicInsertField.action", {}, function(result) {
		 var jsonData = jQuery.parseJSON(result);
			//alert(jsonData)
			 $(jsonData).each(function(k, v) {
				 var id ="#"+(v.componentId);
				 var insertEle = $('[name="'+v.afterInsert+'"]').parent().parent();
				 if(!isEmpty(v.afterInsert)){
					 var compo =  jQuery(id).parent().parent();
					 $(compo).insertAfter(insertEle);
				 }
				 if(!isEmpty(v.beforeInsert)){
					 var compo =  jQuery(id).parent().parent();
					 $(compo).insertBefore(insertEle);
				 }
			 });
			 });
	 
}


 function processIdProof(value) {
	 if (value == "-1" || value=='') {
			 jQuery(".idType").addClass("hide");
		        jQuery(".proofIdOther").addClass("hide");
		        jQuery("#proofIdOtherVal").addClass("hide");
		        jQuery("#proofIdOtherVal").val("");
		        jQuery(".proofNoInput").addClass("hide");
		        jQuery("#proofNoInputVal").val("");
		
	    } else if (value == "99") {
	        jQuery(".idType").removeClass("hide");
	        jQuery(".proofIdOther").removeClass("hide");
	        jQuery("#proofIdOtherVal").removeClass("hide");
	        jQuery(".proofNoInput").removeClass("hide");
	        jQuery("#proofNoInputVal").removeClass("hide");
	    } else {
	    	 jQuery(".idType").removeClass("hide");
	        jQuery(".proofNoInput").removeClass("hide");
	        jQuery("#proofNoInputVal").removeClass("hide");
	        jQuery(".proofIdOther").addClass("hide");
	        jQuery("#proofIdOtherVal").addClass("hide");
	        jQuery("#proofIdOtherVal").val("");
	        //jQuery("#proofNoInputVal").val("");
	    }
	}
 function saveTaluk() {
	    jQuery(".cerror").empty();
	//    var ccountry=getElementValueById("ccountry");
	//    var cstate=getElementValueById("cstate");
	    var clocalities = getElementValueById("clocalities");
	    var cityName = getElementValueById("cityName");
	   /*  if (isEmpty(ccountry)) {
	        jQuery(".cerror").text("<s:property value="%{getLocaleProperty('empty.country')}" />");
	        return false;
	    }    
	    else if (isEmpty(cstate)) {
	        jQuery(".cerror").text("<s:property value="%{getLocaleProperty('empty.state')}" />");
	        return false;
	    } */
	     if (isEmpty(clocalities)) {
	        jQuery(".cerror").text("<s:property value="%{getLocaleProperty('empty.district')}" />");
	        return false;
	    } else if (isEmpty(cityName)) {
	        jQuery(".cerror").text("<s:property value="%{getLocaleProperty('error.city')}"/>");
	        return false;
	    }

	    jQuery.post("farmer_populateCitySave.action", {
	        selectedLocality: clocalities,
	        selectedCity: cityName
	    }, function(result) {
	     var validateMsg=JSON.stringify(result);
	     var validate=jQuery.parseJSON(validateMsg);
	     var val = validate.msg;
	     if(val=='0'){
	       jQuery(".cerror").text("<s:property value="%{getLocaleProperty('city.exist')}"/>");
	     }else{
	        listMunicipality(document.getElementById('localities'), 'localities', 'city', 'panchayath', '');
	        resetTaulkInfo();
	     }
	    });
	}

function savePanchayat(){

	jQuery(".perror").empty();
	 var pcity = getElementValueById("pcity");
	 var pname = getElementValueById("panchayatName");
	 
	 if (isEmpty(pcity)) {
		 jQuery(".perror").text("<s:property value="%{getLocaleProperty('empty.city')}"/>");
	        return false;
	    } else if (isEmpty(pname)) {
	        jQuery(".perror").text("<s:property value="%{getLocaleProperty('empty.name')}"/>");
	        return false;
	    }
	 
	 /*    var dataa = {
	 	        selectedCity: pcity,
	 	        selectedPanchayat:pname
		} */
	    
/* 	 $.ajax({
		 url:'farmer_populatePanchayatSave.action',
	     type: 'post',
	     dataType: 'json',
	     data: dataa,
	     success: function(result) {
	    	 showPopup(result.msg,result.title);
	    	 listPanchayat(document.getElementById("panchayath"), city, panchayath, village);
	    	 resetPanchaytInfo();
	     },
	     error: function(data) {
	     	alert("Some Error Occured , Please Try again");
	     	//window.location.href="farmer_create.action";
	     }
	}); */

	    jQuery.post("farmer_populatePanchayatSave.action", {
	        
	        selectedCity: pcity,
	        selectedPanchayat:pname
	    }, function(result) {
	     var validateMsg=JSON.stringify(result);
	     var validate=jQuery.parseJSON(validateMsg);
	     var val = validate.msg;
	     if(val=='0'){
	       jQuery(".perror").text("<s:property value="%{getLocaleProperty('panchayat.exist')}"/>");
	     }else{
	    	 listPanchayat(document.getElementById('panchayath'), 'city', 'panchayath', 'village');
	    	 resetPanchaytInfo();
	     }
	     
	    });
}

function saveVillage() {
    
	jQuery(".verror").empty();
    var vcountry = getElementValueById("vcountry");
    var vstate = getElementValueById("vstate");
    var vlocality = getElementValueById("vlocalities");
    var vcity = getElementValueById("vcity");
    var vname = getElementValueById("villageName");
    var vPanchayat =getElementValueById("vPanchayat"); 
    var vcode = getElementValueById("villageCode");
    if(isGramPanchayatEnable=='1'){
     if (isEmpty(vPanchayat)) {
      jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.gramPanchayat')}"/>");
          return false;
      }else if (isEmpty(vname)) {
         jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.villagename')}"/>");
         return false;
     }
    }
    else{
    if (isEmpty(vcountry)) {
          jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.country')}" />");
          return false;
      }else if (isEmpty(vstate)) {
          jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.state')}" />");
          return false;
      }else if (isEmpty(vlocality)) {
          jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.locality')}" />");
          return false;
      } else if (isEmpty(vcity)) {
          jQuery(".verror").text("<s:property value="%{getLocaleProperty('empty.city')}" />");
          return false;
      } else if (isEmpty(vname)) {
         jQuery(".verror").text("<s:property value="%{getLocaleProperty('error.village')}"/>");
         return false;
     }
    }

    jQuery.post("farmer_populateVillageSave.action", {
        selectedVillage: vname,
        selectedCity: vcity,
        selectedPanchayat:vPanchayat,
        selectedVillageCode:vcode
    }, function(result) {
     var validateMsg=JSON.stringify(result);
     var validate=jQuery.parseJSON(validateMsg);
     var val = validate.msg;
     if(val=='0'){
       jQuery(".verror").text("<s:property value="%{getLocaleProperty('village.exist')}"/>");
     }else{
         listVillage(document.getElementById('city'), 'city', 'panchayath', 'village');
            resetVillageInfo();
     }
     
    });
}

function saveSamithi(){
	var samithiName= getElementValueById("createSamithi");
	var formationDate= getElementValueById("formationDate");
	
	if(isEmpty(samithiName)){
		 jQuery(".gerror").text("<s:property value="%{getLocaleProperty('empty.samithiName')}"/>");
	        return false;
	}
	
	
	 var dataa = {
			 selectedSamithi: samithiName,
	 	       formationDate:formationDate
		}
	    
	 $.ajax({
		 url:'farmer_populateSamithiSave.action',
	     type: 'post',
	     dataType: 'json',
	     data: dataa,
	     success: function(result) {
	    	 jQuery("#createSamithi").val("");
	    	 jQuery("#formationDate").val("");
	    	 $('#samithiModal').modal('toggle');
	    	 
	    	 jQuery.post("farmer_populateSamithi.action", {}, function(result) {
	    		 insertOptions('samithii', $.parseJSON(result));
	    	  });
	    	 
	     },
	     error: function(data) {
	     	alert("Some Error Occured , Please Try again");
	     }
	});
	
}

function certificationDiv(obj) {
	 var name="farmer.isCertifiedFarmer";
	 var certType="";
	 if(isEmpty(obj)){
	  certType=$("#farmerCertified option:selected").val();
	 }else{
	  certType = (obj.value);
	 }
	 if(isEmpty(certType)){
	  certType='0';
	 }
	    if (parseInt(certType) == 1) {
	     jQuery(".isCertifiedFarmer").show();
	    }
	    else{
	     jQuery(".isCertifiedFarmer").hide();
	    } 

	}

function benificiaryDiv(obj){
	var certType="";
	if(isEmpty(obj)){
		certType=$('input:radio[name="armer.isBeneficiaryInGovScheme"]:checked').val();
	}else{
		certType = (obj.value);
	}
	if(isEmpty(certType)){
		certType='0';
	}
    if (parseInt(certType) == 1) {
    	jQuery(".beneficiaryScheme").removeClass("hide");
    }
    else{
    	jQuery(".beneficiaryScheme").addClass("hide");
    } 

}
/*

 function lifeInsureAmt(val){
		
	if(val==1){
		$('#life1').attr("checked","checked");
		alert("1:" +$('.lifeDiv').hasClass("hide"));
		$('.lifeDiv').removeClass("hide");
		alert("2:" +$('.lifeDiv').hasClass("hide"));
			//$('.lifeDiv').removeClass("hide");
		}
		else{
			$('#life2').attr("checked","checked");
    		$('.lifeDiv').addClass("hide");
			$('#liftAmt').val("");
			$('.lifeDiv').addClass("hide");
			
		}
	}	 
	
	function val){
		if(val==1){
			$('#health1').attr("checked","checked");
    		$('.healthDiv').removeClass("hide"); 
			
			$('.healthDiv').removeClass("hide");
		}
		else{
			$('#health2').attr("checked","checked");
    		$('.healthDiv').addClass("hide");
			$('#healthAmt').val("");
			$('.healthDiv').addClass("hide");
			
		}
	} 
*/
function onCancel(){
	//document.listForm.submit();
	var type=$(".type").val();
	if(!isEmpty(type)&&type.toString()=='2'){
		window.location.href="farmer_list.action?type=2";
	}else{
		window.location.href="farmer_list.action";
	}
}
function validateFarmer() {
	
	
    jQuery(".error").empty();
    jQuery(".cerror").empty();
    jQuery(".ferror").empty();
    
    
    
  var isBeneficiaryInGovScheme= $('input[name="farmer.isBeneficiaryInGovScheme"]:checked').val();
   // var isBeneficiaryInGovScheme=document.getElementsByName('farmer.isBeneficiaryInGovScheme');
    if (isEmpty(getElementValueById("firstName"))) {
        jQuery(".error").append(
            "<ul><li><s:text name='empty.farmerName'/></li><ul>");
        jQuery("#firstName").focus();
        return false;
    } else if (isEmpty(getElementValueById("village"))) {
        jQuery(".cerror").append(
            "<ul><li><s:text name='empty.village'/></li><ul>");
        jQuery("#village").focus();
        return false;
    }
    else if (isEmpty(getElementValueById("samithii"))) {
        jQuery(".cerror").append(
            "<ul><li><s:text name='empty.samithi'/></li><ul>");
        jQuery("#samithii").focus();
        return false;
    }
    
    else if (isBeneficiaryInGovScheme=='1' && isEmpty(getElementValueById("nameOfScheme")) ) {
        jQuery(".ferror").append(
            "<ul><li><s:text name='empty.nameOfTheScheme'/></li><ul>");
        jQuery("#nameOfScheme").focus();
        $(".nameOfSchemeDiv").removeClass("hide");
		$(".governmentDepartment").removeClass("hide");
        return false;
    }
    return true;
}

function formPlugin() {
    try {
        var fields = "<s:property value='farmerPluginFields'/>";
        $(fields.split(",")).each(function(k, v) {
            className = "." + v;
            $(className).addClass("hide");
        });
    } catch (e) {
        console.log(e);
    }
}
function processHousingType(val) {              
    if (val == 99) {
        jQuery(".housingTypeOther").removeClass("hide");
    } else {
        jQuery("#housingTypeOtherVal").val("");
        jQuery(".housingTypeOther").addClass("hide");
    }
}

function openModal(){
	$('#hidId').val('');
}
function beforeSave(){
	processBank();
}


function beforeUpdate(){
	processBank();
	
}
function saveBankInformation() {
    var valid = true;
    var hidId = $('#hidId').val();
    var accountNo = $('#accNo').val();
    var bankName;
    var accType = $('#accountType').val();
    
    var tenant = '<s:property value="getCurrentTenantId()"/>';
    if (tenant != "sticky") {
    var sortCode = $('#sortCode').val().trim();
    }
    var bankNameVal;
    var branchName;
    var branchNameVal;
    var bankNameText;
    var accTypeText = $('#accountType').find(":selected").text();
   var accName;
   var swiftCode;
    if (tenant == "chetna") {
        bankNameVal = $('#bankNameList').val();
        bankName = $('#bankNameList').find(":selected").text();
        branchNameVal = $('#branchNameList').val();
        branchName = $('#branchNameList').find(":selected").text();
    }
    else {
        bankName = $('#bankName').val();
        branchName = $('#branchName').val();
    }
    
     if (tenant == "farmAgg") {
    	accName=$('#accName').val();
    	swiftCode=$('#swiftCode').val();
    }
    // var swiftCode= $('#swiftCode').val();
    if(tenant == "farmAgg"){
    	 if (accountNo == "") {
           	$(".bankError").text('<s:text name="empty.accNo"/>');
               valid = false;
           } else if (!isNumber(accountNo)) {
               $(".bankError").text('<s:text name="valid.accNo"/>');
               valid = false;
           }else if (bankName == "") {
             	$(".bankError").text('<s:text name="empty.bankName"/>');
                valid = false;
            }
    	
    	
    	
          
    }else{
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

    } 
    else if (tenant != "chetna") {
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
            if (tenant == "sticky") {
            	$( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
        			      //"<td>" + accType+"-"+accTypeText + "</td>" +  
        			      "<td>" + accTypeText + "</td>" +
        		          "<td>" + accountNo + "</td>" +
        		          "<td>" + bankName + "</td>" +
        		          "<td>" + branchName + "</td>" +
        		          "<td class='hide'>" + sortCode + "</td>" +
        		         // "<td>" + swiftCode + "</td>" +
        		             "<td class='hide'>" + accType + "</td>" +
        		          "<td><button id='"+idd+"' type='button' class='btn  btn-sts' href='#' onclick='editBank(this);'><label><s:text name='edit.button'/></label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' href='#' onclick='deleteBank(this);'><label><s:text name='delete.button'/></label></button></td>" +
        		        "</tr>" );
            	
            } else  if (tenant == "farmAgg") {
                $( "#bankTable tbody" ).append( "<tr id='tr_"+id+"'>" +
        			      //"<td>" + accType+"-"+accTypeText + "</td>" +  
        			      "<td>" + accTypeText + "</td>" +
        			      "<td>" + accName + "</td>" +
        		          "<td>" + accountNo + "</td>" +
        		          "<td>" + bankName + "</td>" +
        		          "<td>" + branchName + "</td>" +
        		          "<td>" + swiftCode + "</td>"+
        		           "<td>" + sortCode + "</td>" +
        		          "<td class='hide'>" + accType + "</td>" +
        		          "<td><button id='"+idd+"' type='button' class='btn  btn-sts' href='#' onclick='editBank(this);'><label><s:text name='edit.button'/></label></button>&nbsp;&nbsp;&nbsp;<button id='"+id+"' class='btn btn-sm btn-warning' href='#' onclick='deleteBank(this);'><label><s:text name='delete.button'/></label></button></td>" +
        		        "</tr>" );
                  }
            
            else{
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
            }
        } else {
        	//alert("kg");
			var $tr=$('#tr_'+hidId+","+accType);
			if(tenant=="chetna"){
				 $tr=$('#tr_'+hidId+","+accType+","+bankNameVal+","+branchNameVal);
			}
			
			var $td= $tr.children('td'); 
			var accNoCell;
			var bankNameCell;
			var branchNameCell;
			var sortCodeCell;
			var accTypeCell;
			var accTypeVal;
			
			
			if (tenant == "farmAgg") {
        			      accTypeCell = $td.eq(0);
        			      accNameCell= $td.eq(1);  
        			      accNoCell= $td.eq(2);  
          			      bankNameCell= $td.eq(3);
          			      branchNameCell= $td.eq(4); 
          			      swiftCodeCell= $td.eq(5);		
          			      sortCodeCell= $td.eq(6);	
          			      accTypeVal = $td.eq(7);
                  }else{
                	  
          		     accNoCell= $td.eq(1);  
          			 bankNameCell= $td.eq(2);
          			 branchNameCell= $td.eq(3); 
          			 sortCodeCell= $td.eq(4);		
          			 accTypeCell = $td.eq(0);
          			 accTypeVal = $td.eq(5);
                  }
		   
			
			if (tenant == "farmAgg") {
				swiftCodeCell.text(swiftCode);
				accNameCell.text(accName);
			}
			
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

function processHousingOwnership(val) {				
	if (val == 99) {
		jQuery(".housingOwnershipOther").removeClass("hide");
	} else {
		jQuery("#housingOwnershipOtherVal").val("");
		jQuery(".housingOwnershipOther").addClass("hide");
	}
}
function onSubmit(){
	
	var hit=true;
	var tenant='<s:property value="getCurrentTenantId()"/>';
	if(document.getElementById("calendar")!=null ){
	document.getElementById("dateOfBirth").value = document.getElementById("calendar").value;
	}

	//document.getElementById("loanRepaymentDate").value = document.getElementById("repaymentDate").value;

	/* if( tenant=='atma' ){
		formSourceOfIncome();
		
	}
	 */
	 
	  
	//healthAssesDetails();
	//selfAssesDetails();
	
	
	calculateAge();
	
	//validateImage();
	/* if( tenant=='welspun' || 'livelihood'){
		validateImage();
		
	} */
	//beforeSave();
	//disableCertificationTypeBySize(true);
	
	if(addDynamicField()){
		
	$("#target").submit();
	}
	
	//$("#target").submit();
}

function disableCertificationTypeBySize(flag){
	if(flag){
		if(jQuery("#certificationType > option").size()==1){
			jQuery("#certificationType").attr("disabled",flag);
			
		}
	}else{
		jQuery("#certificationType").attr("disabled",flag);
	}
}

function refreshPopup(){
	//$(".close").addClass("hide");
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
	
	if(tenant=="farmAgg"){
		$('#accName').val("");
		$('#swiftCode').val("");
	}
	
}
function cancelBnkInfo(){
	refreshPopup();	
	document.getElementById("model-close-btn").click();
}


function closeModal(){
	refreshPopup();	
	document.getElementById("model-close-btn").click();
}


function editBank(evt){
	var accountNo;
	var accName;
	var bankName;
	var branchName;
	var sortCode;
	var swiftCode;
	var accType;
	var accTypeCode;
	
	var $td= $(evt).closest('tr').children('td');
	var hiddenId = $(evt).attr('id');
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	if(tenant=="farmAgg"){
		  accName= $td.eq(1).text(); 
		   accountNo= $td.eq(2).text();  
			 bankName= $td.eq(3).text();
			 branchName= $td.eq(4).text();
			 swiftCode= $td.eq(5).text();
			 sortCode= $td.eq(6).text();
			 accType = $td.eq(7).text();
			 accTypeCode =  $td.eq(7).text();
	}else{
		
		 accountNo= $td.eq(1).text(); 
		 bankName= $td.eq(2).text();
		 branchName= $td.eq(3).text();
		 sortCode= $td.eq(4).text();
		 accType = $td.eq(5).text();
	}
	
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
	
	if(tenant=="farmAgg"){
		$('#accName').val(accName);
		$('#swiftCode').val(swiftCode);
	}
	
	$('#hidId').val(hiddenId.split(",")[0]);
	
	document.getElementById("editBankDetailModal").click();
	
}

function deleteBank(evt){
	//	var result = confirm("Do you want to delete the selected record?");
	var result =confirm('<s:text name="txt.delete"/>');	
	if(result){
			var $tr= $(evt).closest('tr'); 
			$tr.remove();
			evt.preventDefault();
		   }
		
		}

function processBank(){
	var jsonObj=[];
	$('#bankTable tbody tr').each(function() {
		var $td= $(this).children('td');
		var accountNo;
		var accName;
		var bankName;
		var branchName;
		var sortCode;
		var swiftCode;
		var accType;
		var accTypeCode;
		
		if(tenant=="farmAgg"){
			 var accName= $td.eq(1).text();  
			 var accountNo= $td.eq(2).text();  
				var bankName= $td.eq(3).text();
				var branchName= $td.eq(4).text(); 
				var swiftCode= $td.eq(5).text(); 
				var sortCode= $td.eq(6).text(); 
				var accType = $td.eq(7).text();
				var accTypeCode =  $td.eq(7).text();
		}else{
			
	    var accountNo= $td.eq(1).text();  
		var bankName= $td.eq(2).text();
		var branchName= $td.eq(3).text(); 
		var sortCode= $td.eq(4).text(); 
		//var swiftCode= $td.eq(5).text(); 
		var accType = $td.eq(5).text();
		var accTypeCode =  $td.eq(5).text();

		}
		
		var bankInformation = {};				
		bankInformation["accNo"] = accountNo;
		bankInformation["bankName"] = bankName;
		bankInformation["branchName"] = branchName;
		bankInformation["sortCode"] = sortCode;
		bankInformation["accType"] = accType;
		bankInformation["accTypeCode"] = accTypeCode;
	
		if(tenant=="farmAgg"){
		bankInformation["accName"] = accName;
		bankInformation["swiftCode"] = swiftCode;	
		}
		//bankInformation["swiftCode"] = swiftCode;			
		/* if(accType === "Personal Savings"){
			bankInformation["accType"] = 1;	
		}
		else if(accType === "Joint Savings"){
			bankInformation["accType"] = 2;	
		}
		else if(accType === "Jan Dhan"){
			bankInformation["accType"] = 3;	
		}
		else{
			bankInformation["accType"] = 99;
		}	 */
		if (!isEmpty(accountNo) && !isEmpty(bankName)){
	    jsonObj.push(bankInformation);
		}
	 });
	$('#jsonString').val(JSON.stringify(jsonObj));
}


function getFormattedValue(val){
	   if(val==null||val==undefined||val.toString().trim()==""){
	    return " ";
	   }
	   return val;
	 }
	 
	 	 
	 function getDynamicDependsId(dynamicId,val1)
	 { 	
		    var tableBody = jQuery(".appContentWrapper");
			$.each(tableBody, function(index, value) {
			$(this).find('.dynamicDependIds').each(function(index,val){
				var fieldName=$(this).attr("id");
				var fieldVal=$(this).val();
				console.log("--"+fieldName);
				var id="#"+fieldName;
				var clashide="."+fieldName;
				if(parseInt(dynamicId)==parseInt(fieldVal))
				{
					if(val1=="1")
					{
						$(id).removeClass("hide");
						$(clashide).removeClass("hide");
					}
					else
					{
						$(id).addClass("hide");
						$(clashide).addClass("hide");
					}
					
				}
			
			});
			
		 });
	 }
	
	
	 function dynamicDependsId()
	 {
		 var radioArr = new Array();
		  var tableBody = jQuery(".appContentWrapper");
			$.each(tableBody, function(index, value) {
				
			 $(this).find('.radio:checked').each(function(){
					var isRequired = 0;
					radioName=$(this).attr("name");
					radioVal=$(this).val();
					
					if(radioVal=="1"){
					 	 //validateDynaicFeld+=getFormattedValue(validateName)+"###";
						  radioArr.push(getFormattedValue(radioVal));
					}
					
				}); 
				
				
			 $(this).find('.dynamicDependIds').each(function(index,val){
				var fieldName=$(this).attr("id");
				var fieldVal=$(this).val();
				var id="#"+fieldName;
				var clashide="."+fieldName;
				if(radioArr.indexOf('1')>-1)
				{
					$(id).removeClass("hide");
					$(clashide).removeClass("hide");
				}
				else
				{
					$(id).addClass("hide");
					$(clashide).addClass("hide");
				}
				
			}); 
			
			
		 });
		
	 }

function selfAssesDetails(){
	var tableBody = jQuery("#selfAssesDetails tr");
	var selfAssesArray = new Array();
	$.each(tableBody, function(index, value) {
		var activity = jQuery(this).find(".activityVal").text();
		var assesValue = jQuery(this).find(".assesValue").text();
		var selfAssesRemark = jQuery(this).find(".selfAssesRemark").text();
		var id=jQuery(this).find(".id").text();
		var farmerId=jQuery(this).find(".farmerId").text();
		if(!isEmpty(activity)||!isEmpty(assesValue)||!isEmpty(selfAssesRemark))
				selfAssesArray.push({activity:activity,value:assesValue,remark:selfAssesRemark});
		
		
	});
	var json = JSON.stringify(selfAssesArray);
	jQuery("#selfAssesmentJSON").val(json);
}

function healthAssesDetails(){
	var tableBody = jQuery("#healthAssesDetails tr");
	var healthAssesArray = new Array();
	$.each(tableBody, function(index, value) {
		var dType = jQuery(this).find(".dTypeVal").text();
		var origin = jQuery(this).find(".originVal").text();
		var professionalConsultation = jQuery(this).find(".professionalConsultation").text();
		var detailConsultation=jQuery(this).find(".detailConsultation").text();
		var remarks = jQuery(this).find(".remarks").text();
		var id=jQuery(this).find(".id").text();
		var farmerId=jQuery(this).find(".farmerId").text();
		if(!isEmpty(dType)||!isEmpty(origin)||!isEmpty(professionalConsultation)||!isEmpty(detailConsultation)||!isEmpty(remarks))
			healthAssesArray.push({diabilityType:dType,origin:origin,remark:remarks,consultationStatus:professionalConsultation,consulatationDetail:detailConsultation});
	});
	var json = JSON.stringify(healthAssesArray);
	jQuery("#healthAssesmentJSON").val(json);
}

function addAssesment(){
	
	var dType = getElementValueByText("disabilityType",'disabilityType');	
	var origin = getElementValueByText("originTypes",'originTypes');	
	var remarks = getElementValueById("disabilityRemark",'disabilityRemark');	
	var professionalConsultation=getRadioValueByName('professionalConsultant');
	var detailConsultation=getElementValueById("disabilityDetailConsultion");	
	
	var dTypeVal = getElementValueById("disabilityType");	
	var originVal = getElementValueById("originTypes");	
	
	var tableSize = $("#healthAssesDetails tr").length;
	
	if(isEmpty(dTypeVal)){
		return false;
	}
	
	if(!isEmpty(dTypeVal)||!isEmpty(originVal)||!isEmpty(remarks)||!isEmpty(professionalConsultation)||!isEmpty(detailConsultation))
	{
		var tr ="<tr id='healthAssesTable"+tableSize+"'>";
		tr+=(td(dType,'dType'));
		tr+=(td(origin,'origin'));
		tr+=(td(remarks,'remarks'));
		if(professionalConsultation=='1'){
			tr+=(td("No",''));
		}else{
			tr+=(td("Yes",''));
		}
		tr+=(td(professionalConsultation,'professionalConsultation hide'));
		tr+=(td(detailConsultation,'detailConsultation'));
		tr+=(td(dTypeVal,'hide dTypeVal'));
		tr+=(td(originVal,'hide originVal'));
		tr+=(td('<button onclick="editAssesment(healthAssesTable'+tableSize+')" type=button class="btn btn-sts btn-sm"><i class="fa fa-edit" area-hidden="true"/></button><button onclick="removeAssesment(healthAssesTable'+tableSize+')" type=button class="btn btn-danger btn-sm"><i class="fa fa-trash" area-hidden="true"/></button>'));
		tr+="</tr>";
		$('#healthAssesDetails').append(tr);
	}
	
	resetAssesment();
}

function addSelfAssesment(){
	var activity = getElementValueByText("activityList",'activityList');	
	var assesValue=getRadioValueByName('assesValue');
	var selfAssesRemark = getElementValueById("selfAssesRemark");	
	
	var activityVal = getElementValueById("activityList");	
	var tableSize = $("#healthAssesDetails tr").length;
	
	if(isEmpty(activityVal)){
		return false;
	}
	
	if(!isEmpty(activityVal)||!isEmpty(selfAssesRemark)||!isEmpty(assesValue)){
		var tr ="<tr id='selfAssesTable"+tableSize+"'>";
		tr+=(td(activity,'activity'));
		if(assesValue=='1'){
			tr+=(td("No",''));
		}else{
			tr+=(td("Yes",''));
		}
		tr+=(td(assesValue,'assesValue hide'));
		tr+=(td(selfAssesRemark,'selfAssesRemark'));
		tr+=(td(activityVal,'hide activityVal'));
		tr+=(td('<button onclick="editSelfAssesment(selfAssesTable'+tableSize+')" type=button class="btn btn-sts btn-sm"><i class="fa fa-edit" area-hidden="true"/></button><button onclick="removeAssesment(selfAssesTable'+tableSize+')" type=button class="btn btn-danger btn-sm"><i class="fa fa-trash" area-hidden="true"/></button>'));
		$('#selfAssesDetails').append(tr);
	}	
	resetSelfAssesment();
}

function updateAssesment(row){
	var dType = getElementValueByText("disabilityType",'disabilityType');	
	var origin = getElementValueByText("originTypes",'originTypes');	
	var remarks = getElementValueById("disabilityRemark",'disabilityRemark');	
	var professionalConsultation=getRadioValueByName('professionalConsultant');
	var detailConsultation=getElementValueById("disabilityDetailConsultion");	
	
	var dTypeVal = getElementValueById("disabilityType");	
	var originVal = getElementValueById("originTypes");	
	
	var tableSize = (jQuery(row).attr("id"));
	if(!isEmpty(dTypeVal)||!isEmpty(originVal)||!isEmpty(remarks)||!isEmpty(professionalConsultation)||!isEmpty(detailConsultation))
	{
		jQuery(row).empty();
		jQuery(row).append(td(dType,'dType'));
		jQuery(row).append(td(origin,'origin'));
		jQuery(row).append(td(remarks,'remarks'));
		if(professionalConsultation=='1'){
			jQuery(row).append(td("No",''));
		}else{
			jQuery(row).append(td("Yes",''));
		}
		jQuery(row).append(td(professionalConsultation,'professionalConsultation hide'));
		jQuery(row).append(td(detailConsultation,'detailConsultation'));
		jQuery(row).append(td(dTypeVal,'hide dTypeVal'));
		jQuery(row).append(td(originVal,'hide originVal'));
		jQuery(row).append(td('<button onclick="editAssesment('+tableSize+')" type=button class="btn btn-sts btn-sm"><i class="fa fa-edit" area-hidden="true"/></button><button onclick="removeAssesment('+tableSize+')" type=button class="btn btn-danger btn-sm"><i class="fa fa-trash" area-hidden="true"/></button>'));
	}
	
	resetAssesment();
}

function updateSelfAssesment(row){
	var activity = getElementValueByText("activityList",'activityList');	
	var assesValue=getRadioValueByName('assesValue');
	var selfAssesRemark = getElementValueById("selfAssesRemark");	
	
	var activityVal = getElementValueById("activityList");	
	var tableSize = (jQuery(row).attr("id"));
	if(!isEmpty(activity)||!isEmpty(selfAssesRemark)||!isEmpty(assesValue)){
		jQuery(row).empty();
		jQuery(row).append(td(activity,'activity'));
		if(assesValue=='1'){
			jQuery(row).append(td("No",''));
		}else{
			jQuery(row).append(td("Yes",''));
		}
		jQuery(row).append(td(assesValue,'assesValue hide'));
		jQuery(row).append(td(selfAssesRemark,'selfAssesRemark'));
		jQuery(row).append(td(activityVal,'hide activityVal'));
		jQuery(row).append(td('<button onclick="editSelfAssesment('+tableSize+')" type=button class="btn btn-sts btn-sm"><i class="fa fa-edit" area-hidden="true"/></button><button onclick="removeAssesment('+tableSize+')" type=button class="btn btn-danger btn-sm"><i class="fa fa-trash" area-hidden="true"/></button>'));
	}	
	resetSelfAssesment();
}

function editAssesment (rowId){
	/* var rowId = "#"+rowId; 
	console.log(rowId); */
	$.each(jQuery(rowId), function(index, value) {
		 var dType = jQuery(this).find(".dTypeVal").text();
		 var origin = jQuery(this).find(".originVal").text();
		 var remarks = jQuery(this).find(".remarks").text();
		 var professionalConsultation = jQuery(this).find(".professionalConsultation").text();
		 var detailConsultation=jQuery(this).find(".detailConsultation").text();
		 
		 setEleValueById("disabilityType",dType);
		 setEleValueById("originTypes",origin);
		 setEleValueById("disabilityRemark",remarks);
		 setEleValueById("disabilityDetailConsultion",detailConsultation);
		 $('input[type=radio][name=professionalConsultant][value='+professionalConsultation+']').prop('checked',true);
		 
		 jQuery("#addAssesmentBtn").addClass("hide");
		 jQuery("#editAssesmentBtn").removeClass("hide");
		 
		 jQuery("#editAssesmentBtn").click(function(){
			 updateAssesment(rowId);
		 })
		 
		 jQuery(".select2").select2(); 
	});
} 


function editSelfAssesment(rowId){
	$.each(jQuery(rowId), function(index, value) {
		 var activityVal = jQuery(this).find(".activityVal").text();
		 var assesValue  = jQuery(this).find(".assesValue").text();
		 var selfAssesRemark = jQuery(this).find(".selfAssesRemark").text();
		
		 
		 setEleValueById("activityList",activityVal);
		 setEleValueById("selfAssesRemark",selfAssesRemark);
		 $('input[type=radio][name=assesValue][value='+assesValue+']').prop('checked',true);
		 
		 jQuery("#addSelfAsses").addClass("hide");
		 jQuery("#editSelfAssesmentBtn").removeClass("hide");
		 
		 jQuery("#editSelfAssesmentBtn").click(function(){
			 updateSelfAssesment(rowId);
		 })
		 
		 jQuery(".select2").select2();
	});
}

function removeAssesment(row){
	jQuery(row).remove();
}

function setEleValueById(id,val){
	if(!isEmpty(id)&&!isEmpty(val)){
		jQuery("#"+id).val(val.trim());
	}
}

function resetAssesment(){
	$("#disabilityType").val("");
	$("#originTypes").val("");
	$("#disabilityRemark").val("");
	$("#disabilityDetailConsultion").val("");
	$('input[type=radio][name=professionalConsultant]').prop('checked', false);
	jQuery(".select2").select2();
	jQuery("#addAssesmentBtn").removeClass("hide");
	jQuery("#editAssesmentBtn").addClass("hide");
}

function resetSelfAssesment(){
	$("#activityList").val("");
	$("#selfAssesRemark").val("");
	$('input[type=radio][name=assesValue]').prop('checked', false);
	jQuery(".select2").select2();
	jQuery("#addSelfAsses").removeClass("hide");
	jQuery("#editSelfAssesmentBtn").addClass("hide");
}

function td(val,classz){
	  return "<td class='"+classz+"'>"+getFormattedValue(val)+"</td>";
}

function getFormattedValue(val){
	   if(val==null||val==undefined||val.toString().trim()==""){
	    return " ";
	   }
	   return val;
}

jQuery(function ($) {
    $(".calender").datepicker(
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
});

function calTotalHshld(){
	var totHs = 0;	
	var val1=parseFloat($("#adultMale").val());
	var val2=parseFloat($("#adultFemale").val());
	var val3=parseFloat($("#childMale").val());
	var val4=parseFloat($("#childFemale").val());
	var val5=parseFloat($("#childCountonSiteMale").val());
	var val6=parseFloat($("#childCountonSiteFemale").val());
	if(isNaN(val1)){
		val1 = 0;
	}if(isNaN(val2)){
		val2 = 0;
	}if(isNaN(val3)){
		val3 = 0;
	}if(isNaN(val4)){
		val4 = 0;
	}if(isNaN(val5)){
		val5 = 0;
	}if(isNaN(val6)){
		val6 = 0;
	}
	totHs = val1+val2+val3+val4;
	$('#houseHoldId').text(totHs);
	$("#totalHsldLabel").val(totHs)
}
function healthInsureAmt(val){
	if(val=='1'){
	$("#healthAmt").val("");
	$('.healthDiv').show();
	}
	else{
		$("#healthAmt").val("");
		$('.healthDiv').hide();
	}
	
}

function enableFarmerPhotoModal(idArr,type) {
	try {
		$("#mbody").empty();
		var mbody = "";
		mbody = "";
		mbody = "<div class='item active'>";
		mbody += '<img src="farmer_populateImage.action?id='
				+ idArr + '&type='+type+'"/>';
		mbody += "</div>";
		$("#mbody").append(mbody);
		
		document.getElementById("enablePhotoModal").click();
	} catch (e) {
		alert(e);
	}
}

function validateFarmerCode(val){
	//alert("AAAA :"+val );
	if(!isEmpty(val)){
		$.ajax({
	        type: "POST",
	            async: false,
	            url: "farmer_validateFarmerCode.action",
	            data:{farmerCode:val},
	            success: function(result) {
	            	if(result.status==1){
	            		 alert('Farmer Code Already Exist');
	            	}
	      }
	    });
	}
	
}

function isAlphabet(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : 0));
	if (charCode > 32 && (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		return false;
	}
	return true;
}

</script>

<head>
<META name="decorator" content="swithlayout">
</head>

<body>

	<style>
.select2-container .select2-selection--single {
	height: 34px;
	border: 1px solid #d5d5d5;
}

.select2-container--default .select2-selection--single .select2-selection__rendered
	{
	line-height: 34px;
}
</style>

	<s:form name="form" cssClass="fillform" action="farmer_%{command}"
		method="post" enctype="multipart/form-data" id="target">

		<s:hidden key="currentPage" />
		<s:hidden key="id" />
		<s:hidden id="jsonString" name="farmer.jsonString" />
		<s:hidden id="dateOfBirth" name="dateOfBirth" />
		<s:hidden id="loanRepaymentDate" name="loanRepaymentDate" />
		<s:hidden name="agriTestJson" id="agriTestJson" />
		<s:hidden id="income" name="farmer.AGRICULTURE_ACTIVITIES" />
		<s:hidden id="sourceIncomeId" name="sourceIncomeId" />
		<s:hidden id="healthAssesmentJSON" name="healthAssesmentJSON" />
		<s:hidden id="selfAssesmentJSON" name="selfAssesmentJSON" />
		<s:hidden id="totalHsldLabel" name="farmer.totalHsldLabel" />
		<s:hidden name="type" class="type" />

		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="farmer.id" class="uId" />
		</s:if>
		<s:hidden key="command" />

		<s:hidden id="farmerDynamicDatas" name="farmerDynamicDatas" />
		<s:hidden id="farmerDynamicValIds" name="farmerDynamicValIds" />

		<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />
		<s:hidden id="dynamicListArray" name="dynamicListArray" />
 <div class="ferror" id="errorDiv" class=" hide alert alert-danger" >
					<s:actionerror theme="bootstrap" />
					<s:fielderror theme="bootstrap" />
				</div> 
		<div id="accordion" class="custom-accordion pers_info" >
			<div class="card-header card mb-1 shadow-none">
			 <a href="#persInfo" class="text-dark" data-toggle="collapse" aria-expanded="true"  aria-controls="collapseOne">
                                                    <div class="card-header" id="headingOne">
                                                        <h6 class="m-0">
                                                        <div class="wizard-wrapper">
															<div class="wizard-icon">
															<span class="svg-icon svg-icon-2x">
																	<!--begin::Svg Icon | path:/metronic/theme/html/demo1/dist/assets/media/svg/icons/General/User.svg-->
																	<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
																			<polygon points="0 0 24 0 24 24 0 24"></polygon>
																			<path d="M12,11 C9.790861,11 8,9.209139 8,7 C8,4.790861 9.790861,3 12,3 C14.209139,3 16,4.790861 16,7 C16,9.209139 14.209139,11 12,11 Z" fill="#000000" fill-rule="nonzero" opacity="0.3"></path>
																			<path d="M3.00065168,20.1992055 C3.38825852,15.4265159 7.26191235,13 11.9833413,13 C16.7712164,13 20.7048837,15.2931929 20.9979143,20.2 C21.0095879,20.3954741 20.9979143,21 20.2466999,21 C16.541124,21 11.0347247,21 3.72750223,21 C3.47671215,21 2.97953825,20.45918 3.00065168,20.1992055 Z" fill="#000000" fill-rule="nonzero"></path>
																		</g>
																	</svg>
																	<!--end::Svg Icon-->
																</span>
															
															</div>
																<div class="wizard-label">
																	
																<h3 class="wizard-title"><s:property value="%{getLocaleProperty('info.personal')}" /></h3>
																<div class="wizard-desc">Setup Farmer Basic Information Details</div>
															</div>
														</div>   
                                                            <i class="mdi mdi-minus float-right accor-plus-icon collapse-icon-custom"></i>
                                                        </h6>
                                                    </div>
                                                </a>
			
			</div>

			<div id="persInfo" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
				<div class="card-body">

				<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group firstName">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.firstName')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
							<s:textfield id="firstName" name="farmer.firstName" maxlength="50" onkeypress="return isAlphabet(event)" cssClass="upercls form-control" />
						</div>
					</div>
                                                </div>
                                                <div class="col-md-4">
                                                   	<div class="form-group lastName">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.lastName')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
						<s:textfield id="lastName" name="farmer.lastName" maxlength="50" cssClass="upercls form-control" />
						</div>
					</div>
                                                </div>
                                                
                                                
                                       <div class="col-md-4">
                                                   	<div class="form-group surName">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.surName')}" />
						</label>
						<div class="">
							<s:textfield id="surName" name="farmer.surName" maxlength="50" cssClass="upercls form-control" />
						</div>
					</div>
                                                </div>               
                                                
                                                
                   			 </div>	

				<div class="row">
				 <div class="col-md-4">
                       <div class="form-group gender">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.gender')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
							<s:select list="genderType" listKey="key" listValue="value" cssClass="form-control" id="gender" name="farmer.gender"/>
						</div>
					</div>
            </div>
				 <div class="col-md-4">
                       <div class="form-group dateOfBirth">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.dateOfBirth')}" />
						</label>
						<div class="">
							<s:textfield value='%{dateOfBirth}' readonly="true"
								name="calendar" onchange="calculateAge()" id="calendar"
								data-date-format="%{getGeneralDateFormat().toLowerCase()}"
								size="20" cssClass="date-picker form-control" />
						</div>
					</div>
            </div>
				 <div class="col-md-4">
                       <div class="form-group age">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farmer.age1')}" />
						</label>
						<div class="">
							<s:textfield id="age" name="farmer.age"
								readonly="true" onkeypress="return isNumber(event)" maxlength="3"
								cssClass="form-control" />
						</div>
					</div>
            </div>
				</div>

</div></div></div>
<div id="accordion" class="custom-accordion contact_info" >
			<div class="card-header card mb-1 shadow-none">
			 <a href="#contInfo" class="text-dark" data-toggle="collapse" aria-expanded="true"  aria-controls="collapseTwo">
                                                    <div class="card-header" id="headingTwo">
                                                        <h6 class="m-0">
                                                        <div class="wizard-wrapper">
															<div class="wizard-icon">
															<span class="svg-icon svg-icon-2x">
																	<!--begin::Svg Icon | path:/metronic/theme/html/demo1/dist/assets/media/svg/icons/General/User.svg-->
																	<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path d="M16.3740377,19.9389434 L22.2226499,11.1660251 C22.4524142,10.8213786 22.3592838,10.3557266 22.0146373,10.1259623 C21.8914367,10.0438285 21.7466809,10 21.5986122,10 L17,10 L17,4.47708173 C17,4.06286817 16.6642136,3.72708173 16.25,3.72708173 C15.9992351,3.72708173 15.7650616,3.85240758 15.6259623,4.06105658 L9.7773501,12.8339749 C9.54758575,13.1786214 9.64071616,13.6442734 9.98536267,13.8740377 C10.1085633,13.9561715 10.2533191,14 10.4013878,14 L15,14 L15,19.5229183 C15,19.9371318 15.3357864,20.2729183 15.75,20.2729183 C16.0007649,20.2729183 16.2349384,20.1475924 16.3740377,19.9389434 Z" fill="#000000"></path>
																			<path d="M4.5,5 L9.5,5 C10.3284271,5 11,5.67157288 11,6.5 C11,7.32842712 10.3284271,8 9.5,8 L4.5,8 C3.67157288,8 3,7.32842712 3,6.5 C3,5.67157288 3.67157288,5 4.5,5 Z M4.5,17 L9.5,17 C10.3284271,17 11,17.6715729 11,18.5 C11,19.3284271 10.3284271,20 9.5,20 L4.5,20 C3.67157288,20 3,19.3284271 3,18.5 C3,17.6715729 3.67157288,17 4.5,17 Z M2.5,11 L6.5,11 C7.32842712,11 8,11.6715729 8,12.5 C8,13.3284271 7.32842712,14 6.5,14 L2.5,14 C1.67157288,14 1,13.3284271 1,12.5 C1,11.6715729 1.67157288,11 2.5,11 Z" fill="#000000" opacity="0.3"></path>
																		</g>
																	</svg>
																	<!--end::Svg Icon-->
																</span>
															
															</div>
																<div class="wizard-label">
																	
																<h3 class="wizard-title"><s:property value="%{getLocaleProperty('info.cont')}" /></h3>
																<div class="wizard-desc">Setup Farmer Contact & Location Details</div>
															</div>
														</div>   
                                                            <i class="mdi mdi-minus float-right accor-plus-icon collapse-icon-custom"></i>
                                                        </h6>
                                                    </div>
                                                </a>
			
			</div>

			<div id="contInfo" class="collapse show" aria-labelledby="headingTwo" data-parent="#accordion">
				<div class="card-body">

				<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group individual phNumber">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.phoneNumber')}" />
						</label>
						<div class="">
						<s:textfield cssClass="form-control " name="farmer.phoneNumber"
							 maxlength="12" onkeypress="return isNumber(event)" />
						</div>
					</div>
                                                </div>
                                                <div class="col-md-4">
                                                   	<div class="form-group mobileNumber">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.mobileNumber')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
						<s:textfield name="farmer.mobileNumber"
								maxlength="15" cssClass="form-control "
								onkeypress="return isNumber(event)" />
						</div>
					</div>
                                                </div>
                                                
                                                
                                       <div class="col-md-4">
                                                   	<div class="form-group email">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.email')}" />
						</label>
						<div class="">
							<s:textfield cssClass="form-control" name="farmer.email" size="90" maxlength="45" />
						</div>
					</div>
                                                </div>               
                                                
                                                
                   			 </div>	

				<div class="row">
				 <div class="col-md-4">
                       <div class="form-group locationInfo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('country.name')}" />
						</label>
						<div class="">
							<s:select class="form-control select2" name="selectedCountry"
								id="country" list="countriesLang" headerKey=""
								headerValue="%{getText('txt.select')}"
								onchange="listState(this,'country','state','localities','city','panchayath','village')" />
						</div>
					</div>
            </div>
				 <div class="col-md-4">
                       <div class="form-group locationInfo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('state.name')}" />
						</label>
						<div class="">
							<s:select class="form-control select2" list="states"
								headerKey="" name="selectedState"
								headerValue="%{getText('txt.select')}" Key="id" Value="name"
								id="state" onchange="listLocality(this,'state','localities','city','panchayath','village')" />
						</div>
					</div>
            </div>
				 <div class="col-md-4">
                       <div class="form-group locationInfo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('locality.name')}" />
						</label>
						<s:select cssClass="form-control select2"
								name="selectedLocality" id="localities" list="listLocalities"
								headerKey="" headerValue="%{getText('txt.select')}" Key="id"
								Value="name" onchange="listMunicipality(this,'localities','city','panchayath','village')" />
					</div>
            </div>
				</div>
				
				
				<div class="row">
				 <div class="col-md-4">
                       <div class="form-group locationInfo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('city.name')}" />
						</label>
						<div class="">
							<s:select cssClass="form-control select2" id="city"
								name="selectedCity" list="cities" headerKey=""
								headerValue="%{getText('txt.select')}" Key="id" Value="name"								
								onchange="listPanchayat(this,'city','panchayath','village')" />
						</div>
					</div>
            </div>
            <s:if test="gramPanchayatEnable==1">
				 <div class="col-md-4">
                       <div class="form-group locationInfo enableGramPanchayat">
						<label for="txt"><s:property
									value="%{getLocaleProperty('panchayat.name')}" />
						</label>
						<div class="">
							<s:select cssClass="form-control select2" id="panchayath"
									name="selectedPanchayat" list="panchayath" headerKey=""
									headerValue="%{getText('txt.select')}" Key="id"	Value="name"
									onchange="listVillage(this,'city','panchayath','village')" />
						</div>
					</div>
            </div></s:if>
				 <div class="col-md-4">
                       <div class="form-group locationInfo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<s:select cssClass="form-control select2" name="selectedVillage"
								id="village" list="villages" headerKey="" Key="id" Value="name"
								headerValue="%{getText('txt.select')}" />
					</div>
            </div>
				</div>
		<div class="row">
				 <div class="col-md-4">
                       <div class="form-group group">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('profile.samithi')}" /> <sup
							style="color: red;">*</sup>
						</label>
						<div class="">
							<s:select name="selectedSamithi" cssClass="form-control select2"
								id="samithii" list="samithi" listKey="key" listValue="value"
								headerKey="" headerValue="%{getText('txt.select')}" />
						</div>
					</div>
            </div>
            </div>
            
      </div></div>      </div>
            
       <div id="accordion" class="custom-accordion farmer_info" >
			<div class="card-header card mb-1 shadow-none">
			 <a href="#farmerInfo" class="text-dark" data-toggle="collapse" aria-expanded="true"  aria-controls="collapseThree">
                                                    <div class="card-header" id="headingThree">
                                                        <h6 class="m-0">
                                                        <div class="wizard-wrapper">
															<div class="wizard-icon">
															<span class="svg-icon svg-icon-2x">
																	<!--begin::Svg Icon | path:/metronic/theme/html/demo1/dist/assets/media/svg/icons/General/User.svg-->
																		<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path d="M12,21 C7.02943725,21 3,16.9705627 3,12 C3,7.02943725 7.02943725,3 12,3 C16.9705627,3 21,7.02943725 21,12 C21,16.9705627 16.9705627,21 12,21 Z M14.1654881,7.35483745 L9.61055177,10.3622525 C9.47921741,10.4489666 9.39637436,10.592455 9.38694497,10.7495509 L9.05991526,16.197949 C9.04337012,16.4735952 9.25341309,16.7104632 9.52905936,16.7270083 C9.63705011,16.7334903 9.74423017,16.7047714 9.83451193,16.6451626 L14.3894482,13.6377475 C14.5207826,13.5510334 14.6036256,13.407545 14.613055,13.2504491 L14.9400847,7.80205104 C14.9566299,7.52640477 14.7465869,7.28953682 14.4709406,7.27299168 C14.3629499,7.26650974 14.2557698,7.29522855 14.1654881,7.35483745 Z" fill="#000000"></path>
																		</g>
																	</svg>
																	<!--end::Svg Icon-->
																</span>
															
															</div>
																<div class="wizard-label">
																	
																<h3 class="wizard-title"><s:property value="%{getLocaleProperty('ICS Information')}" /></h3>
																<div class="wizard-desc">Get Farmer ICS Details</div>
															</div>
														</div>   
                                                            <i class="mdi mdi-minus float-right accor-plus-icon collapse-icon-custom"></i>
                                                        </h6>
                                                    </div>
                                                </a>
			
			</div>     
        <div id="farmerInfo" class="collapse show" aria-labelledby="headingThree data-parent="#accordion">
				<div class="card-body">

				<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group certified">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.isCertified')}" />
						</label>
						<div class="">
						<s:select id="farmerCertified" list="isFarmerCertified"
								name="farmer.isCertifiedFarmer" value="FarmerCertifiedDefaultValue"
								onchange="certificationDiv(this);" cssClass="form-control"/>
						</div>
					</div>
                                                </div>
                                                <div class="col-md-4">
                                                   	<div class="form-group isCertifiedFarmer icsFields hide icsName ">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.icsName')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
						<s:textfield id="icsName" name="farmer.icsName" cssClass="upercls form-control" maxlength="90" />
						</div>
					</div>
                                                </div>
                                                
                                                
                                       <div class="col-md-4">
                                                   	<div class="form-group isCertifiedFarmer icsFields hide icsCode">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.icsCode')}" />
						</label>
						<div class="">
							<s:textfield id="icsCode" name="farmer.icsCode"	maxlength="35" cssClass="upercls form-control" />
						</div>
					</div>
                                                </div>               
                                                
                      </div>	<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group isCertifiedFarmer certificationType hide cerType">
						<label for="txt"> <s:text name="certification.type" /> <sup
								style="color: red;">*</sup>
						</label>
						<div class="">
						<s:select cssClass="form-control select2"
									id="certificationType" name="farmer.certificationType"
									list="certificationTypes" headerKey="-1"
									headerValue="%{getText('txt.select')}" />
						</div>
					</div>
                                                </div>                          
                   			 </div>	

	
            
      </div></div></div> 
      
      <div id="accordion" class="custom-accordion other_info" >
			<div class="card-header card mb-1 shadow-none">
			 <a href="#otherInfo" class="text-dark" data-toggle="collapse" aria-expanded="true"  aria-controls="collapseFour">
                                                    <div class="card-header" id="headingFour">
                                                        <h6 class="m-0">
                                                        <div class="wizard-wrapper">
															<div class="wizard-icon">
															<span class="svg-icon svg-icon-2x">
																	<!--begin::Svg Icon | path:/metronic/theme/html/demo1/dist/assets/media/svg/icons/General/User.svg-->
																			<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1">
																		<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
																			<rect x="0" y="0" width="24" height="24"></rect>
																			<path d="M19,11 L21,11 C21.5522847,11 22,11.4477153 22,12 C22,12.5522847 21.5522847,13 21,13 L19,13 C18.4477153,13 18,12.5522847 18,12 C18,11.4477153 18.4477153,11 19,11 Z M3,11 L5,11 C5.55228475,11 6,11.4477153 6,12 C6,12.5522847 5.55228475,13 5,13 L3,13 C2.44771525,13 2,12.5522847 2,12 C2,11.4477153 2.44771525,11 3,11 Z M12,2 C12.5522847,2 13,2.44771525 13,3 L13,5 C13,5.55228475 12.5522847,6 12,6 C11.4477153,6 11,5.55228475 11,5 L11,3 C11,2.44771525 11.4477153,2 12,2 Z M12,18 C12.5522847,18 13,18.4477153 13,19 L13,21 C13,21.5522847 12.5522847,22 12,22 C11.4477153,22 11,21.5522847 11,21 L11,19 C11,18.4477153 11.4477153,18 12,18 Z" fill="#000000" fill-rule="nonzero" opacity="0.3"></path>
																			<circle fill="#000000" opacity="0.3" cx="12" cy="12" r="2"></circle>
																			<path d="M12,17 C14.7614237,17 17,14.7614237 17,12 C17,9.23857625 14.7614237,7 12,7 C9.23857625,7 7,9.23857625 7,12 C7,14.7614237 9.23857625,17 12,17 Z M12,19 C8.13400675,19 5,15.8659932 5,12 C5,8.13400675 8.13400675,5 12,5 C15.8659932,5 19,8.13400675 19,12 C19,15.8659932 15.8659932,19 12,19 Z" fill="#000000" fill-rule="nonzero"></path>
																		</g>
																	</svg>
																	<!--end::Svg Icon-->
																</span>
															
															</div>
																<div class="wizard-label">
																	
																<h3 class="wizard-title"><s:property value="%{getLocaleProperty('Other Information')}" /></h3>
																<div class="wizard-desc">Get Other Details</div>
															</div>
														</div>   
                                                            <i class="mdi mdi-minus float-right accor-plus-icon collapse-icon-custom"></i>
                                                        </h6>
                                                    </div>
                                                </a>
			
			</div>     
        <div id="otherInfo" class="collapse show" aria-labelledby="headingFour data-parent="#accordion">
				<div class="card-body">

				<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group noOfFamilyMembers">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.noOfFamilyMembers.prop')}" />
						</label>
						<div class="">
						<s:textfield id="familyMembers" name="farmer.noOfFamilyMembers"
								maxlength="2" cssClass="upercls form-control" onkeypress="return isNumber(event)"/>
						</div>
					</div>
                                                </div>
                                                <div class="col-md-4">
                                                   	<div class="form-group housingOwnershipProp ">
						<label for="txt"> <s:text
								name="farmer.housingOwnershipProp" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="">
							<s:select id="housingOwnership"
								name="farmer.farmerEconomy.housingOwnership"
								list="housingOwnershipMap" listKey="key" listValue="value"
								headerKey="-1" headerValue="%{getText('txt.select')}"
								onChange="processHousingOwnership(this.value);"
								cssClass="form-control" tabindex="10" />
							<div class="form-element housingOwnershipOther hide">
								<s:textfield id="housingOwnershipOtherVal"
									name="farmer.farmerEconomy.housingOwnershipOther"
									cssClass="form-control" maxlength="45" tabindex="11"
									cssStyle="margin-top:1%" />
							</div>

							<script type="text/javascript">									
							    		processHousingOwnership(jQuery("#housingOwnership").val());									
									</script>
						</div>
					</div>
                                                </div>
                                                
                                                
                                       <div class="col-md-4">
                                                   	<div class="form-group housingTypeProp">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.housingTypeProp')}" />
						</label>
						<div class="">
						<s:select id="housingType"
								name="farmer.farmerEconomy.housingType" list="housingTypeList"
								listKey="key" listValue="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								onChange="processHousingType(this.value);"
								class="form-control  select2" tabindex="10" />
								
					<div class="form-element housingTypeOther hide">
								<s:textfield id="housingTypeOtherVal"
									name="farmer.farmerEconomy.otherHousingType"
									cssClass="form-control " maxlength="45" tabindex="11"
									cssStyle="margin-top:1%" />
							</div>
							<script type="text/javascript">									
							    		processHousingType(jQuery("#housingType").val());									
									</script>
						</div>
					</div>
                                                </div>               
                                                
                      </div>	<div class="row">
                               
                                                <div class="col-md-4">
                                                   <div class="form-group drinkingWSProp">
						<label for="txt"> <s:text name="farmer.drinkingWSProp" />
						</label>
						<div class="">
						<s:select id="drinkingWS"
								name="farmer.farmerEconomy.drinkingWaterSource"
								list="drinkingWSMap" listKey="key" listValue="value"
								multiple="true" cssClass="select2 form-control select2-multiple" tabindex="10"
								onChange="processDrinkingWS(this.value)" />
							<div class="form-element drinkingWSOther hide">

								<s:textfield id="drinkingWSOtherVal"
									name="farmer.farmerEconomy.drinkingWaterSourceOther"
									cssClass="form-control " maxlength="45" tabindex="11"
									cssStyle="margin-top:1%" />
							</div>
						 <script type="text/javascript">									
							    		processDrinkingWS(jQuery("#drinkingWS").val());									
									</script>
						</div>
					</div>
                                                </div>  
                                                <div class="col-md-4">
                                                   <div class="form-group lifeInsur">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.life')}" />
						</label>
						<div class="">
						<s:select id="life" list="listInsurance"
								name="farmer.lifeInsurance"
								value="FarmerLifeInsuranceDefaultValue"
								onchange="lifeInsureAmt(this.value);" cssClass="form-control select2" />
					
						</div>
					</div>
                                                </div>    
                                                <div class="col-md-4">
                                                   <div class="form-group isLoanTakenLastYearProp">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farmer.isLoanTakenLastYearProp')}" />
						</label>
						<div class="">
						<s:select id="LoanTakenLastYear" list="isLoanTakenLastYear"
								name="farmer.loanTakenLastYear"
								value="%{FarmerLoanDefaultValue}"
								onchange="loanTakenFromDiv(this);" cssClass="form-control select2"  />
						
						
						</div>
					</div>
                                                </div>                            
                   			 </div>	

	
            
      </div></div></div>    
      <div class="dynamicFieldsRender"></div>
      <div class="button-items float-right">
        
                                            <s:if test="command =='create'">
                                            <button type="submit" id="buttonAdd1" onclick="event.preventDefault();onSubmit();"class="btn btn-success waves-effect waves-light">
                                                <i class="ri-check-line align-middle mr-2"></i> Save
                                            </button></s:if>
                                            <s:else>
                                            <button type="submit" id="buttonUpdate" onclick="event.preventDefault();onSubmit();" class="btn btn-primary waves-effect waves-light">
                                                <i class="ri-error-warning-line align-middle mr-2"></i> Update
                                            </button></s:else>
                                            <button type="button" onclick="onCancel();" class="btn btn-danger waves-effect waves-light">
                                                <i class="ri-close-line align-middle mr-2"></i> Cancel
                                            </button>
                                        </div>      

	</s:form>

	<s:hidden id="gramPanchayatEnable" name="gramPanchayatEnable"></s:hidden>
	<s:hidden id="icsDropDown" name="icsDropDown"></s:hidden>

</body>