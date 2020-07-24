<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>
<link rel="stylesheet"
	href="plugins/selectize/css/selectize.bootstrap3.css">
<script src="plugins/selectize/js/standalone/selectize.min.js?v=2.0"></script>
<script src="js/dynamicComponents.js?v=20.23"></script>
<head>

<META name="decorator" content="swithlayout">
</head>
<style>
.datepicker-dropdown {
	width: 300px;
}
</style>
<script src="js/dynamicComponents.js?v=20.21"></script>
<script>
$("#calendarDOC").datepicker({
	format : "mm-yyyy",
	startView : "months",
	minViewMode : "months"
});



$("#calendarFormat").datepicker({
	format : "mm-yyyy",
	startView : "months",
	minViewMode : "months"
});
    var validationForCertifiedFarmer = false;
    var cerf = '<s:property value="farmerId" />';
    var hit=true;
    var enableSoil = '<s:property value="enableSoliTesting"/>';
    var tenant='<s:property value="getCurrentTenantId()"/>';
    var isGramPanchayatEnable='<s:property value="gramPanchayatEnable"/>';
    $(document).ready(function () {    
    	/* setQualified = function(){};
    	getBranchIdDyn = function(){};
    	calculateLandHect = function(){};
		calculatePlantHect = function(){}; */
    	
    	//$('#frmLease').val("");  
        //renderDynamicFeilds();

    	/* if(tenant=="farmAgg"){
    		alert("hi");
    		  $(".farmAgg").addClass("hide");
    	
    	} */
    	
    	setQualified('<s:property value="farm.farmIcsConv.qualified"/>');
                    /* 	if(tenant=="awi"||tenant=="AWI"){
                			callAWI();
                		}else */ 
                    	
                    	var farmerId = '<s:property value="farmerId" />';
                    	
                    	if(!isEmpty(farmerId)){
                    		$.post("farm_detailCheckForCertifiedFarmer", {selectedFarmerId: cerf}, function (data) {
                              	//alert(data);
                    			if ("YES" == data) {
                                	
                                }else{
                                	
                                }
                            });
                    	}
                    	hideFields();
                    	 
                    	$("#organicStatus").val("In process");
                    	document.getElementById('organicStatus').disabled = true;
                    	
                    	jQuery.post("farm_populateHideFn.action", {}, function(result) {
                			var farmerHideFields = jQuery.parseJSON(result);
                			
                			$.each(farmerHideFields, function(index, value) {
                				if(value.type=='1'){
            						console.log(value.typeName);
            						showByEleName(value.typeName);
            					}if(value.type=='2'){
            						showByEleId(value.typeName);
            					}if(value.type=='3'){
            						showByEleClass(value.typeName);
            					}if(value.type=='4'){
            						$("."+value.typeName).removeClass("hide");
            						console.log(value.typeName);
            					} if(value.type=='5'){
            						hideByEleClass(value.typeName);
            						$("."+value.typeName).remove();
            					}
                			});
                			
                	    });
                    	if('<s:property value="command"/>'!="updateActPlan"){
                    	renderDynamicFeilds();
                    	}else{
                    		renderDynamicFeildsWithActPlan();
                    	}
               		 if('<s:property value="command"/>'=="update"){
            				setDynamicFieldUpdateValues();
            			}
                    	jQuery(".farmOtherDiv").hide();
                    	jQuery(".bene").hide(); 
                    	jQuery(".isFfsbene").hide();
                    	
                    	jQuery(".millCrop").hide();
                    	jQuery(".millCount").hide();
                    	
                    	 var val='<s:property value="farm.farmDetailedInfo.farmerFieldSchool"/>';
                				if(val=="1"){
                					$('.bene').show();
                					$('.isFfsbene').show();
                				}
                				else{
                					$('.bene').hide();
                					$('.isFfsbene').hide();
                					
                				}
                				
                		var val='<s:property value="farm.farmDetailedInfo.milletCultivated"/>';
                		if(val==1){
                			$('.millCrop').show();
                			$('.milletCrop').show();
                			$('.millCount').show();
                			$('.milletCount').show();
                		}else{
                			$('.millCrop').hide();
                			$('.milletCrop').hide();
                			$('.millCount').hide();
                			$('.milletCount').hide();
                		}
   
                        if(tenant=="symrise"){

                		$( $(".surveyNo")).after($(".distanceProcessingUnit"), $(".ownLand"), $(".soilType"),$(".soilTexture"));

                          jQuery(".gpsInfo").addClass("hide");
                         // $( $(".surveyNo")).after($(".distanceProcessingUnit")); 
                         // $( $(".soilType")).after($(".farmRegNo")); 
                          jQuery(".noOfWineOnPlot").removeClass("hide");
                          //$( $(".noOfWineOnPlot")).before($(".ownLand"));
                        }
                		var val='<s:property value="farm.soilTesting"/>';
                		if (val==1)
                  		  {
                			$('#wDoc').show();
                   		 }else{
                   			$('#wDoc').hide();
                   		 }
            			
                    	var isEdit = '<s:property value="command" />';
                    	
                    	if(isEdit=='update'){
                    		showFarmOther($('#farmOwned').val());
                    		//var soilType = $('#soilTypeName').val();
                    		
                    		 //irrigationType($('#methodIrrigation').val());
                    		//$("#methodIrrigation").val();
                    		if(tenant!='pratibha' &&  tenant!='griffith'){
                    			
                    		calculateLandHect();
                    		calculatePlantHect();
                    		
                    		}
                    		if(tenant=='symrise'){
                    		calculateNoOfWineOnPlot();
                    		}
                    		if(tenant=='chetna'){
                    			 var selectedWaterHarvest =  '<s:property value="farm.waterHarvest" />';
                    		
                    			 if(selectedWaterHarvest!=null&&selectedWaterHarvest.trim()!=""){
                    			  var values = selectedWaterHarvest.split("\\,");
                    			  var selectedValues = new Array();
                    			  $.each(selectedWaterHarvest.split(","), function(i,e){
                    				  selectedValues[i] = e.trim();
                    			  });
                    			  	$("#waterHarvest").select2('val',selectedValues);
                    			 }
                    		}
                    		
                    	}
                    	
                    	
                    	
                    	$("#expendDetail").val('');
                    	$("#expendVal").val('');
                    	$('#expendVal').keypress(function(event) {
                    	    var $this = $(this);
                    	    if ((event.which != 46 || $this.val().indexOf('.') != -1) &&
                    	       ((event.which < 48 || event.which > 57) &&
                    	       (event.which != 0 && event.which != 8))) {
                    	           event.preventDefault();
                    	    }

                    	    var text = $(this).val();
                    	    if ((event.which == 46) && (text.indexOf('.') == -1)) {
                    	        setTimeout(function() {
                    	            if ($this.val().substring($this.val().indexOf('.')).length > 3) {
                    	                $this.val($this.val().substring(0, $this.val().indexOf('.') + 3));
                    	            }
                    	        }, 1);
                    	    }

                    	    if ((text.indexOf('.') != -1) &&
                    	        (text.substring(text.indexOf('.')).length > 2) &&
                    	        (event.which != 0 && event.which != 8) &&
                    	        ($(this)[0].selectionStart >= text.length - 2)) {
                    	            event.preventDefault();
                    	    }      
                    	});
                    	
                    	var size='<s:property value="updatefarmerLandDetailsList.size()"/>';
                    	$("#landDltUpdateRowCount").val(size);
                    	 $(".frmrld").hide();
                    	 $("#addressesTable").hide();
                    	 $("#addressesTable").find('.checktext').keyup(function(){
                    	    
                    		 
                    	
                    	}); 
                        var farmerUniqueId =
    <%if (request.getParameter("farmerUniqueId") == null) {
				out.print("''");
			} else {
				out.print("'" + request.getParameter("farmerUniqueId") + "'");
			}%>
                        ;
                        jQuery("#farmerUniqueId").val(farmerUniqueId);
                        if(tenant!='pratibha'){

                        checkForCertifiedFarmer();
                        //$(".delBtnCon").addClass('noFile');
                        if (cerf != null || cerf != "") {
                            //alert(cerf);
                            $.post("farm_detailCheckForCertifiedFarmer", {
                                selectedFarmerId: cerf
                            }, function (data) {
                                if ("YES" == data) {
                                    //alert("A");
                                    columnsForCertifiedFarmer();
                                    //alert("validate");
                                    validationForCertifiedFarmer = true;
                                } else {
                                	
                                    //alert("B");
                                    columnsForUnCertifiedFarmer();
                                    validationForCertifiedFarmer = false;
                              	
                                }
                               /*  var deleteSelFile = document
                                        .getElementById('remImg')==null ? undefined : document
                                                .getElementById('remImg').files[0];
                                if (deleteSelFile == undefined) {
                                    $("#remImg").hide();
                                    $(".farmImage").addClass('farmImage');
                                } else {
                                    $("#remImg").show();
                                    $(".delBtnCon").removeClass('noFile');
                                    $(".farmImage").removeClass('farmImage');
                                } */

                               /*  $("#remImg").click(
                                        function () {
                                            var controll = $("#farmImage");
                                            controll.replaceWith(controll.val(
                                                    '').clone(true));
                                            $("#remImg").hide();
                                            $(".farmImage").addClass(
                                                    'farmImage');
                                            //alert("here");
                                        }); */

                            });
                        }
                        }else{
                        	if('<s:property value="getBranchId()"/>'=='bci'){
                        		jQuery("#ics").hide();
                        	}
                        }
                        addInventry1();
                        var group = document
                                .getElementsByName('farm.farmDetailedInfo.sameAddressofFarmer');
                        if (group[0].checked)
                            document.getElementById('addressTxt').disabled = true;
                        else
                            document.getElementById('addressTxt').disabled = false;

                        //jQuery(".rainFedDiv").hide();
                        jQuery(".otherValueDiv").hide();
                        jQuery(".other").hide();
                      
                       
                        //selectedMethodOfIrrigation("#methodIrrigation").val());
                        //Multi Select Drop Down

                        //Gradient drop down

                        var selectedRoad = '<s:property value="selectedRoad" />';
                        if (selectedRoad != null && selectedRoad.trim() != "") {
                            var values = selectedRoad.split("\\,");

                            $.each(selectedRoad.split(","), function (i, e) {
                                $("#gradient option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                            $("#gradient").select2();
                        }
                        
                        // Land Gradient drop down
                        
                       var selectedGradient = '<s:property value="selectedGradient" />';
                        if (selectedGradient != null && selectedGradient.trim() != "") {
                            var values = selectedGradient.split("\\,");

                            $.each(selectedGradient.split(","), function (i, e) {
                                $("#land option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                            $("#land").select2();
                        }


                        //Soil Type drop down

                        var selectedSoil = '<s:property value="selectedSoilType" />';

                        if (selectedSoil != null && selectedSoil.trim() != "") {
                            var values = selectedSoil.split("\\,");
                       
                            $.each(selectedSoil.split(","), function (i, e) {
                          
                                $("#soilType option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                            $("#soilType").select2();
                        }
                        
                        
                        var selectedMethodOfIrrigation = '<s:property value="selectedMethodOfIrrigation" />';
                        if (selectedMethodOfIrrigation != null && selectedMethodOfIrrigation.trim() != "") {
                            var values = selectedMethodOfIrrigation.split("\\,");

                            $.each(selectedMethodOfIrrigation.split(","), function (i, e) {
                                $("#methodIrrigation option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                            $("#methodIrrigation").select2();
                        }
                        

                        //Soil Texture drop down

                        var selectedTex = '<s:property value="selectedTexture" />';
                        if (selectedTex != null && selectedTex.trim() != "") {
                            var values = selectedTex.split("\\,");

                            $.each(selectedTex.split(","), function (i, e) {
                                $("#texture option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                            $("#texture").select2();
                        }

                        //farmIrrigation source drop down

                        var selectedIrrigation = '<s:property value="selectedIrrigation" />';
                        if (selectedIrrigation != null
                                && selectedIrrigation.trim() != "") {
                            var values = selectedIrrigation.split("\\,");

                            $.each(selectedIrrigation.split(","),function (i, e) {
                                        $("#farmIrrigation option[value='" + e.trim() + "']")
                                                .prop("selected", true);
                                    });
                            $("#farmIrrigation").select2();
                        }
                        if(selectedIrrigation==-1 || selectedIrrigation == null || selectedIrrigation == "")
                       	{
                        	$("#irrigationType").hide();
                        	$("#irrigationTypeLabel").hide();
                       	}
                        
                        
                        // Irrigation Source drop down 
                        
                          var selectedIrrigationSource = '<s:property value="selectedIrrigationSource" />';
                        if (selectedIrrigationSource != null
                                && selectedIrrigationSource.trim() != "") {
                        	// alert(selectedIrrigationSource);
                            var values = selectedIrrigationSource.split("\\,");
							//alert(values);
                            $.each(selectedIrrigationSource.split(","),function (i, e) {
                                        $("#irrigationSource option[value='" + e.trim() + "']")
                                                .prop("selected", true);
                                    });
                        }
                        
                        // Multi selected for IFS Practice
                  		 var selectedifs =  '<s:property value="farm.ifs" />';
                  		 if(selectedifs!=null&&selectedifs.trim()!=""){
                  		  var values = selectedifs.split("\\,");
                  		  
                  		  $.each(selectedifs.split(","), function(i,e){
                  			  if (e.trim() == "99") {
                  					jQuery(".ifsOther").show();
                  				} else {
                  					jQuery(".ifsOther").hide();
                  				}
                  		      $("#ifs option[value='" + e.trim() + "']").prop("selected", true);
                  		  });
                  		 }	
                  		 
                  	// Multi selected for Soil Conservation
                  		 var selectedSoilConservation =  '<s:property value="farm.soilConservation" />';
                  		 if(selectedSoilConservation!=null&&selectedSoilConservation.trim()!=""){
                  		  var values = selectedSoilConservation.split("\\,");
                  		  
                  		  $.each(selectedSoilConservation.split(","), function(i,e){
                  			  if (e.trim() == "99") {
                  					jQuery(".soilConservationOther").show();
                  				} else {
                  					jQuery(".soilConservationOther").hide();
                  				}
                  		      $("#soilConservation option[value='" + e.trim() + "']").prop("selected", true);
                  		  });
                  		 }	
                  		 
                  	// Multi selected for Water Conservation
                  		 var selectedWaterConservation =  '<s:property value="farm.waterConservation" />';
                  		 if(selectedWaterConservation!=null&&selectedWaterConservation.trim()!=""){
                  		  var values = selectedWaterConservation.split("\\,");
                  		  
                  		  $.each(selectedWaterConservation.split(","), function(i,e){
                  			  if (e.trim() == "99") {
                  					jQuery(".waterConservationOther").show();
                  				} else {
                  					jQuery(".waterConservationOther").hide();
                  				}
                  		      $("#waterConservation option[value='" + e.trim() + "']").prop("selected", true);
                  		  });
                  		 }	
                     
                  	// Multi selected for Service Center
                  		var selectedServiceCentres =  '<s:property value="farm.serviceCentres" />';
                		 if(selectedServiceCentres!=null&&selectedServiceCentres.trim()!=""){
                		  var values = selectedServiceCentres.split("\\,");
                		  
                		  $.each(selectedServiceCentres.split(","), function(i,e){
                			  if (e.trim() == "99") {
                					jQuery(".serviceCentresOther").show();
                				} else {
                					jQuery(".serviceCentresOther").hide();
                				}
                		      $("#serviceCentres option[value='" + e.trim() + "']").prop("selected", true);
                		  });
                		 }	
                  		 
                  	//Multi selected for Traning and Program

                        var selectedTrainingProgram = '<s:property value="farm.trainingProgram" />';
                        if (selectedTrainingProgram != null && selectedTrainingProgram.trim() != "") {
                            var values = selectedTrainingProgram.split("\\,");

                            $.each(selectedTrainingProgram.split(","), function (i, e) {
                            	 if (e.trim() == "99") {
                   					jQuery(".trainingProgramOther").show();
                   				} else {
                   					jQuery(".trainingProgramOtherOther").hide();
                   				}
                                $("#trainingProgram option[value='" + e.trim() + "']")
                                        .prop("selected", true);
                            });
                        }
     		if(tenant!='pratibha' && tenant!='griffith'){
                		calculateLandHect();
                		calculatePlantHect();
                		}
                         selectedIrrigationValue(jQuery("#farmIrrigation").val());
                         irrigationOtherValue(jQuery("#irrigationSource").val());
                         
                		$('.select2Multi').selectize({
                			 plugins: ['remove_button'],
                			 delimiter: ',',
                			 persist: false,
                		/* 	 create: function(input) {
                			  return {
                			   value: input,
                			   text: input
                			  }
                			 } */
                			});
                		
                		if(tenant=='indev'||tenant=='chetna'||tenant=='mhr'){
                    		jQuery("#ics").hide();
                    	}
                		
                		
                		$("#calendarInsp").datepicker(
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
                		
                		
                  

             		$("#calendarLastDateChemical").datepicker(
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
             		
             		$("#calendarAudit").datepicker(
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
            		jQuery(".totalOrganic").removeClass('hide');
            		$('#tableTemplate').hide();
            		refreshTreeDetails();
            	
            		
                });
                
                    
                    function hideByEleName(name){
                    	$('[name="'+name+'"]').closest( ".flexform-item" ).addClass( "hide" );
                    }
                    

                    function showByEleName(name){
                    	$('[name="'+name+'"]').closest( ".flexform-item" ).removeClass( "hide" );
                    }

                    function hideByEleClass(className){
                    	$("."+className).closest( ".flexform-item" ).addClass( "hide" );
                    }
                    function getTxnType(){
                		
            			return "359";
            		}
                    function showByEleClass(className){
                    	$("."+className).closest( ".flexform-item" ).removeClass( "hide" );
                    }

                    function hideByEleId(id){
                    	$("#"+id).closest( ".flexform-item" ).addClass( "hide" );
                    }

                    function showByEleId(id){
                    	$("#"+id).closest( ".flexform-item" ).removeClass( "hide" );
                    }
    function isAlpahaNumeric(e){
    	var regex = new RegExp("^[a-zA-Z0-9\b]+$");
	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	    if (regex.test(str)) {
	        return true;
	    }

	    e.preventDefault();
	    return false;
    }
    
    function soilTesting(evt)
	{
		var val=$(evt).val();
		if(val==1)
			{
			jQuery("#wDoc").show();
			}
		else{
			jQuery("#wDoc").hide();
		}
	}
    
    

    function checkForCertifiedFarmer() {
        var farmer = document.getElementById('farmerId').value;
        //alert(farmer);
        if (farmer != null && farmer != "") {
            $.post("farm_detailCheckForCertifiedFarmer", {
                selectedFarmerId: farmer
            }, function (data) {
                if ("YES" == data) {
                    columnsForCertifiedFarmer();
                    validationForCertifiedFarmer = true;
                    
                    	jQuery(".organicStatusDiv").show();
                    	organicStatus();
                    	jQuery(".certYear").show();
            		
                } else {
                	
                    columnsForUnCertifiedFarmer();
                    validationForCertifiedFarmer = false;
                    jQuery(".organicStatusDiv").hide();
                    jQuery(".certYear").hide();
                	
                }
            });
        } else
            columnsForUnCertifiedFarmer();
    }
	
    function callAWI(){
    	jQuery("#farmLocationInfo").removeClass( "hide" );
    }   


    function hideFields(){
    	 var app = $(".appContentWrapper");
    	 $(app).addClass("hide");
    	 $(app).not(".farmerDynamicField").find(".flexform-item").each(function(){
    	 	 $(this).addClass("hide");
    	 });
    	  
    	}
    function columnsForCertifiedFarmer() {
        //document.getElementById('tr3').className="hide";
        //document.getElementById('tr5').className="odd";
        //document.getElementById('tr6').className="odd";
        //document.getElementById('tr7').className="odd";
        //document.getElementById('tr8').className="odd";
        // document.getElementById('tr9').className="odd";
        // document.getElementById('tr10').className="odd";
        //document.getElementById('tr11').className="odd";
        //document.getElementById('tr12').className="odd";
        //document.getElementById('tr13').className="odd";
        //document.getElementById('table2').className="odd";
          if(tenant!="welspun"){
         jQuery(".farmLabourInfo").removeClass("hide");
         jQuery(".conversionInfo").removeClass("hide");
         jQuery(".conversionStatus").removeClass("hide");
           }
          if(tenant=="avt"){
           jQuery(".farmLabourInfo").addClass("hide");
           jQuery(".conversionInfo").addClass("hide");
           jQuery(".conversionStatus").addClass("hide");
         }
           
        if(tenant=='iccoa')
        {
        
    	 jQuery(".farmLabourInfo").addClass("hide");
    	        
        }
         if(tenant=="wilmar"){
         jQuery(".soilIrrigationInfo").removeClass("hide");
         jQuery(".conversionInfo").addClass("hide");
         jQuery(".fieldHistoryInfo").removeClass("hide");
         }
         if(tenant=="symrise"){
         jQuery(".farmLabourInfo").addClass("hide");
         jQuery(".conversionInfo").addClass("hide");
         jQuery(".soilIrrigationInfo").addClass("hide");
         jQuery(".conversionStatus").addClass("hide");}
         
    }
    
    function ffsSchool(evt){
    	var val=$(evt).val();
		if(val==1){
			$('.bene').show();
			$('.isFfsbene').show();
			
		}
		else{
			$('.bene').hide();
			$('.isFfsbene').hide();
			
		}
	}
    
    function millestcultivation(evt){
    	var val=$(evt).val();
		if(val==1){
			$('.millCrop').show();
			$('.milletCrop').show();
			$('.millCount').show();
			$('.milletCount').show();
		}else{
			$('.millCrop').hide();
			$('.milletCrop').hide();
			$('.millCount').hide();
			$('.milletCount').hide();
		}
    }
    

    function columnsForUnCertifiedFarmer() {
        //document.getElementById('tr3').className="odd";
        //	document.getElementById('tr5').className="hide";
        //document.getElementById('tr6').className="hide";
        //document.getElementById('tr7').className="hide";
        //document.getElementById('tr8').className="hide";
        //document.getElementById('tr9').className="hide";
        //document.getElementById('tr10').className="hide";
        //document.getElementById('tr11').className="hide";
        //document.getElementById('tr12').className="hide";
        // document.getElementById('tr13').className="hide";
        //document.getElementById('table2').className="hide";
         jQuery(".farmLabourInfo").addClass("hide");
         jQuery(".conversionInfo").addClass("hide");
         jQuery(".conversionStatus").addClass("hide");
         if(tenant=="wilmar"){
         jQuery(".soilIrrigationInfo").addClass("hide");
         jQuery(".fieldHistoryInfo").removeClass("hide");

         }
         if(tenant=="livelihood"){
        	 jQuery(".farmLabourInfo").removeClass("hide");
        	 jQuery(".conversionInfo").removeClass("hide");
             
         }
         
    }

    function sameAsFarmerAddress(element) {
        var group = document.getElementsByName(element);
        var farmer;
        if (group[0].checked) {
            farmer = '<s:property value="farmerId" />';
            if (farmer == null || farmer == "") {
                farmer = document.getElementById('farmer').value;
            }
            $.post("farm_detailFarmAddressSameAsFarmer", {
                selectedFarmerId: farmer
            }, function (data) {
                if (data != "") {
                    document.getElementById('addressTxt').value = data;
                    document.getElementById('addressTxt').disabled = true;
                }
            });
        } else {
            document.getElementById('addressTxt').value = "";
            document.getElementById('addressTxt').disabled = false;
        }
    }
    function organicStatus(element) {     
    	 var command=$("#command").val();
    	var farmer;     
            farmer = '<s:property value="farmerId" />';
            var id = '<s:property value="farmId" />';
            //alert(id);
            if (farmer == null || farmer == "") {
                farmer = document.getElementById('farmer').value;
            }
         /*    if(command.toLowerCase()!="update"){ */
            $.post("farm_organicStatusFarm", {
                selectedFarmerId: farmer,
                id:id
            }, function (data) {
                if (data != "") {
                    document.getElementById('organicStatus').value = data;
                    document.getElementById('organicStatus').disabled = true;
                }
            });
            /* }else{
            	 document.getElementById('organicStatus').disabled = true;
            } */
    }
    function selectedIrrigationValue(data) {
        if (isEmpty(data)&& data==null) {
            //	jQuery("#otherValueDiv").hide();            
            jQuery("#otherValue").hide();
            jQuery("#farmIrrigationType").hide();
            //jQuery("#farmIrrigationType").show();
              
            
             		$(".other").hide();
                 	$("#otherValueDiv").hide();
                 	$("#otherValue").val("");
                     $("#otherValue").hide();
             	
            	
            }
           // document.getElementById('irrigationSource').selectedIndex = ""; 
        //} 
    else if (data == 1) {
        	if($("#farmIrrigation").val()!="1,2"){
        	$("#irrigationType").hide();
        	$("#irrigationTypeLabel").hide();
            jQuery("#otherValueDiv").hide();
            jQuery("#otherValue").val("");
            jQuery("#otherValue").hide();
            jQuery(".other").hide();
            document.getElementById('irrigationSource').selectedIndex = "";
            jQuery("#farmIrrigationType").hide();
            
        	}
        	else if($("#farmIrrigation").val()!="1") {
        		$("#irrigationType").show();
        		$("#farmIrrigationType").show();
            	$("#irrigationTypeLabel").show();
                jQuery("#otherValueDiv").hide();
                jQuery("#otherValue").val("");
                jQuery("#otherValue").hide();
                jQuery(".other").hide();
                document.getElementById('irrigationSource').selectedIndex = "";
        	}       	

        } else if (data == 2) {	

        	$("#irrigationType").show();
        	$("#irrigationTypeLabel").show();
        	$("#farmIrrigationType").show();
        	$("#irrigationSource").show();
        	$("#farmIrrigationType").show();
        	if($("#irrigationSource option:selected").val()!=-1)
        	{
        		var val = $("#irrigationSource option:selected").val();
        		if(val==99)
        		{
        			document.getElementById('irrigationSource').selectedValue = val;
        			jQuery("#otherValue").show();
        		}else{
        			document.getElementById('irrigationSource').selectedValue = val;
        		}
        	}
        	else{
        		document.getElementById('irrigationSource').selectedIndex = "";
        	}
        }
        else{
        	if(data==""){
        		$("#irrigationType").hide();
            	$("#irrigationTypeLabel").hide();
            	
            	document.getElementById('irrigationSource').selectedIndex = "";
            	
            	$(".other").hide();
            	$("#otherValueDiv").hide();
            	$("#otherValue").val("");
                $("#otherValue").hide();
        	}
        	else if(data!="1,2"){ 
        		$("#irrigationType").show();
            	$("#irrigationTypeLabel").show();
            	
                //document.getElementById('irrigationSource').selectedIndex = "";
                
                if($("#irrigationSource option:selected").val()==99){
                	$(".other").show();
                	$("#otherValueDiv").show();
                    $("#otherValue").show();               	
            	}else{
            		$(".other").hide();
                	$("#otherValueDiv").hide();
                	$("#otherValue").val("");
                    $("#otherValue").hide();
            	}
        	}
        	else{
        		$("#irrigationType").show();
            	$("#irrigationTypeLabel").show();
        	}
        }
      
    }

    function irrigationOtherValue(data) {
        if (data == 99) {
            jQuery("#otherValueDiv").show();
            jQuery("#otherValue").show();
            //jQuery(".other").show();
        } else {
            jQuery("#otherValueDiv").hide();
            jQuery("#otherValue").hide();
            //	jQuery("#otherValue").val("");
            //jQuery(".other").hide();
        }
    }
    if(enableSoil=='1'){
    function validateDocument(){
    	var allowedFiles = [".pdf",".xls",".xlxs",".doc",".docx",".jpg"];
		var maxFileSize= 1048576;/* maximum File Size: 1048576 bytes=1MB*/	
		var isError = true;
		var wDocsave1 = $("#wDocsave1");
		var lblError= $("#lblError");
		var filesizeWD1;
		
		filesizeWD1=document.getElementById('wDocsave1').files[0];
		
		if( filesizeWD1== undefined){
			 filesizeWD1=0;
			}else{
				filesizeWD1=filesizeWD1.size;
			}
			
		
		var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
		if (!regex.test(wDocsave1.val().toLowerCase())&& wDocsave1.val()!=""){
			lblError.html("Please upload files having extensions: " + allowedFiles.join(', ') + " only.");
			isError=false;
			return false;	
		}
		else if(filesizeWD1>maxFileSize){
			 lblError.html("File size is too large");
			 isError=false;
			 return false;	
		}else {
			lblError.html('');
		}
		 return isError;
    }
    }
	

    function validateImage() {

        var file = document.getElementById('farmImage').files[0];
        var filename = document.getElementById('farmImage').value;
        var fileExt = filename.split('.').pop();
		
        if (file != undefined) {

            if (fileExt == 'jpg' || fileExt == 'jpeg' || fileExt == 'png'
                    || fileExt == 'JPG' || fileExt == 'JPEG'
                    || fileExt == 'PNG') {
                if (file.size > 51200) {
                    alert('<s:text name="fileSizeExceeds"/>');
                    file.focus();
                    return false;
                }//else if(imgWidth >260){
                //alert('<s:text name="imageWidthMsg"/>');
                //file.focus();
                //return false;	
                //}else if(imgHeight> 70){
                //alert('<s:text name="imageHeightMsg"/>');
                //file.focus();
                //return false;	
                //}
                //document.getElementById('farmImageExist').value = "1";
            } else {
                alert('<s:text name="invalidFileExtension"/>')
                file.focus();
                return false;
            }
        }

        return true;
    }

    function checkImgHeightAndWidth(val) {

        var _URL = window.URL || window.webkitURL;
        var img;
        var file = document.getElementById('farmImage').files[0];

        if (file) {

            img = new Image();
            img.onload = function () {
                imgHeight = this.height;
                imgWidth = this.width;
            };
            img.src = _URL.createObjectURL(file);
        }
    }
    function deleteFile(id) {
    	//var img =document.getElementById('farmImageFileName').value;
    	//alert(img);
    	//if(document.getElementById('image').value==''){
       /*  if(document.getElementById('farmImageFileName').value==''){
    		return false;
    	} */
    	
        if (confirm('<s:text name="confirmToDelete"/>')) {

            if (id != undefined) {

                $.post("farm_deletefile.action", {
                    fileid: id
                }, function (data, status) {
                    if (status === '<s:text name="success"/>') {
                        $('#image').attr("src", 'img/no-image.png');
                        $('#farmImageFileName').hide();
                        $("#farmImage").css({
                            "display": "block",
                            "float": "left"
                        });
                        $(".delBtnCon").addClass('noFile');
                        document.getElementById('farmImage').value = "";
                        document.getElementById('farmImageExist').value = "0";
                        alert('<s:text name="imgDelSuccessfully"/>');
                        $('#remImg').hide();
                    }
                });
            } else if (id == undefined) {

                $('#image').attr("src", 'img/no-image.png');
                $('#farmImageFileName').hide();
                $("#farmImage").css({
                    "display": "block",
                    "float": "left"
                });
                $(".delBtnCon").addClass('noFile');
                document.getElementById('farmImage').value = "";
                document.getElementById('farmImageExist').value = "0";
                alert('<s:text name="imgDelSuccessfully"/>');
               // $('#remImg').hide();
            }
        }
    }

    function checkImgHeightAndWidth(value) {
        $("#remImg").show();
        $(".farmImage").removeClass('farmImage');
        }

    jQuery(function ($) {
        $("#calendar").datepicker({});
    });
    jQuery(function ($) {
        $("#calendarBOC1").datepicker({});
    });
    jQuery(function ($) {
        $("#calendarBOC2").datepicker({});
    });
    jQuery(function ($) {
        $("#calendarBOC3").datepicker({});
    });

    jQuery(function ($) {

        /* $("#calendarDOC").datepicker({
            maxDate: '0',
            beforeShow: function () {
                jQuery(this).datepicker({
                    maxDate: 0
                });
            },
            changeMonth: true,
            changeYear: true
        }); */
    	
    });

    jQuery(function ($) {
        $("#calendarDOJ").datepicker(
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

    function addSoilDetail() {
        //alert("inside add");
        $('#soilTypeName').val('');
        document.getElementById("validateError").innerHTML = "";
        
    }

    function addSoilTexDetail() {
        //alert("inside add");
        $('#soilTexName').val('');
        document.getElementById("validateSoilTexError").innerHTML = "";

    }

    function buttonSoilCance() {
        //refreshPopup();
        document.getElementById("model-close-soil-btn").click();
    }

    function buttonSoilTexCancels() {
        //refreshPopup();
        document.getElementById("model-close-soilTex-btn").click();
    }

    function saveSoilTexInfor() {
      var valid = true;
        var soilTex = $('#soilTexName').val();
        var alreadySelected = $("#texture").val();
        if (soilTex == "") {
            document.getElementById("validateSoilTexError").innerHTML = '<s:text name="empty.soilTex"/>';
            valid = false;
        }
        if(valid){
        jQuery.post("farm_populateSoilTex.action",{soilTexName: soilTex},
                function (result) {
        			if (result != 0) {
                    	insertOptions("texture", JSON.parse(result));
                        document.getElementById("model-close-soilTex-btn").click();
                   	} else {
                                document.getElementById("validateSoilTexError").innerHTML = '<s:text name="soilTex.exists"/>';
                            }
        			if(alreadySelected!=""){
        		        $.each(alreadySelected.toString().split(","), function (i, e) {
        		            $("#texture option[value='" + e.trim() + "']")
        		                    .prop("selected", true);
        		        });
        	        }
                        });
    }
    }

    function saveSoilTypeInfor() {
    	var valid = true;
        var soilType = $('#soilTypeName').val();
        var alreadySelected = $("#soilType").val();
        if (soilType == "") {
            document.getElementById("validateError").innerHTML = '<s:text name="empty.soilType"/>';
            valid = false;
        }
        if(valid){
        	jQuery.post("farm_populateSoilType.action",{soilTypeName: soilType},function (result) {
        		
        		
       console.log(result);
       if (result != 0) {
                	insertOptions("soilType",JSON.parse(result));
                    document.getElementById("model-close-soil-btn")
                            .click();
                } else {
                	document.getElementById("validateError").innerHTML = '<s:text name="soilType.exists"/>';
                }
                if(alreadySelected!=null&&alreadySelected!=""){
                	$.each(alreadySelected.toString().split(","), function (i, e) {
        	            $("#soilType option[value='" + e.trim() + "']")
        	                    .prop("selected", true);
        	        });
                }
            });    }
        var selectedSoil = '<s:property value="selectedSoilType" />';

        if (selectedSoil != null && selectedSoil.trim() != "") {
            var values = selectedSoil.split("\\,");
       
            $.each(selectedSoil.split(","), function (i, e) {
          
                $("#soilType option[value='" + e.trim() + "']")
                        .prop("selected", true);
            });
        }
    }
    
    function processIfs(val) {	
    	
		 var selectedIfs =$("#ifs").val();
		 if(selectedIfs!=null && selectedIfs!="")
		 {
			 var ifsTrim=selectedIfs.toString().trim();
			 if(ifsTrim.includes('99'))
			 {
				  jQuery(".ifsOther").show();
				}
				else
				{
					
					jQuery(".ifsOther").hide();
				}
	
		 }	
		 else
			 {
				 jQuery(".ifsOther").hide();
			 }
		
		
		}
   
   function processSoilConservation(val) {			
		
		 var selectedSoilConservation =$("#soilConservation").val();
		 if(selectedSoilConservation!=null && selectedSoilConservation!="")
		 {
			 var soilConservationTrim=selectedSoilConservation.toString().trim();
			 if(soilConservationTrim.includes('99'))
			 {
				 jQuery(".soilConservationOther").show();
						
			}
				else
				{
					
					jQuery(".soilConservationOther").hide();
				}
	
		 }	
		 else
			 {
				 jQuery(".soilConservationOther").hide();
			 }
		
		}
function processWaterConservation(val) {
		
		 var selectedWaterConservation =$("#waterConservation").val();
		 if(selectedWaterConservation!=null && selectedWaterConservation!="")
		 {
			 var waterConservationTrim=selectedWaterConservation.toString().trim();
			 if(waterConservationTrim.includes('99'))
			 {
				
					jQuery(".waterConservationOther").show();
				
				}
				else
				{
					
					jQuery(".waterConservationOther").hide();
				}
	
		 }	
		 else
			 {
				 jQuery(".waterConservationOther").hide();
			 }
		
		
		
		}
function saveAnimalHusbandary(){
	/* var paramObj = {};	
 	var farmId = $("#farm").val();

	var faimingtype= jQuery("#enrollmentPlace").val();
	//alert(faimingtype);
	var farmtype ='<s:text name="farmer.farmingtypeseasonland1"/>';
	var irrigatedLand = jQuery("#totalLandone").val();
	//alert(irrigatedLand);
	var irrigatedFarmingLand = jQuery("#landIfs").val();
	var farmtype1='<s:text name="farmer.farmingtypeseasonland2"/>';
	var fedtotaland = jQuery("#totalLandRain").val();
	var fedtotalics =jQuery("#landIfsRain").val();
	
//	var irrigatedDetail=irrigatedLand+"%"+irrigatedFarmingLand+"%"+ fedtotaland+"%"+fedtotalics;

	/* console.log("----------"+irrigatedLand);
	console.log("----------"+rainFeedDetail); */
	//alert("check1");
/* 	 jQuery.post("farm_populateFarmingtype.action", {irrigateDetail:irrigatedDetail,rainFeedDetail:rainFeedDetail}, function (result) {
		
	});  */
	 
	/* var rowCount = $('#tableTemplate2 tr').length;
	var id=rowCount+1;
	
    $( "#tableTemplate2 tbody" ).append(        		       
      	"<tr class='tableTr2'>"+
      	"<td>" + faimingtype + "<input type='hidden' value='"+faimingtype+"' id='faimingtype"+(id)+"' /></td>" +
      	 "<td>" + farmtype + "<input type='hidden' value='"+farmtype+"' id='farmtype"+(id)+"' /></td>" +
         "<td>" + irrigatedLand + "<input type='hidden' value='"+irrigatedLand+"' id='irrigatedLand"+(id)+"' /></td>" +
         "<td>" + irrigatedFarmingLand + "<input type='hidden' value='"+irrigatedFarmingLand+"' id='irrigatedFarmingLand"+(id)+"' /></td>" +        
         "<td><button id='"+id+"' class='btn deleteBank btn-warning' href='#' onclick='deleteBank(this);'><label><s:text name='delete.button'/></label></button></td>" +
        "</tr>"+
		"<tr class='tableTr2'>"+
		"<td>" + faimingtype + "<input type='hidden' value='"+faimingtype+"' id='faimingtype1"+(id)+"' /></td>" +
		 "<td>" + farmtype1 + "<input type='hidden' value='"+farmtype1+"' id='farmtype1"+(id)+"' /></td>" +
         "<td>" + fedtotaland + "<input type='hidden' value='"+fedtotaland+"' id='fedtotaland"+(id)+"' /></td>" +
         "<td>" + fedtotalics + "<input type='hidden' value='"+fedtotalics+"' id='fedtotalics"+(id)+"' /></td>" +        
         "<td><button id='"+id+"' class='btn deleteBank btn-warning' href='#' onclick='deleteBank(this);'><label><s:text name='delete.button'/></label></button></td>" +
        	"</tr>"    
        	
    ); */ 
}  


function savefarming(){/* 
	//var tableFoot=document.getElementById("tableTemplate2");
   //var rows = tableFoot.getElementsByClassName("tableTr2");
 
 
    var FarmerseasonJsonString=[];
//alert(rows.length);
    for(i=2;i<=rows.length;i++){
     var objfI1=new Object();
/*      var irrigatedLand=jQuery("#irrigatedLand"+i).val();
     alert("**irrigatedLand**"+irrigatedLand);
     var irrigatedFarmingLand=jQuery("#irrigatedFarmingLand"+i).val();
     var fedtotaland=jQuery("#fedtotaland"+i).val();
     var fedtotalics=jQuery("#fedtotalics"+i).val();
     alert("***fedtotalics***"+fedtotalics);
     var faimingtype=jQuery("#faimingtype"+i).val(); */
    /*  if (objfI1 !=='' && objfI1 !== 'undefined' && objfI1 !== null){   
     objfI1.irrigatedTotLand=jQuery("#irrigatedLand"+i).val();
     objfI1.irrigatedIfsPractices=jQuery("#irrigatedFarmingLand"+i).val();
     objfI1.rainfedTotLand=jQuery("#fedtotaland"+i).val();
     objfI1.ranifedIfsPractices=jQuery("#fedtotalics"+i).val();
     objfI1.seasonCode=jQuery("#faimingtype"+i).val();
    	 
     FarmerseasonJsonString.push(objfI1);
     }
    }
  
     var jsonArray=new Object();
     jsonArray=JSON.stringify(FarmerseasonJsonString);
     jQuery("#FarmerseasonJsonString").val(jsonArray);
 	
     console.log(jsonArray); */ 
    }

function processServiceCentres(val) {			
	
	 var selectedServiceCentres =$("#serviceCentres").val();
	 if(selectedServiceCentres!=null && selectedServiceCentres!="")
	 {
		 var serviceCentresTrim=selectedServiceCentres.toString().trim();
		 if(serviceCentresTrim.includes('99'))
		 {
				  jQuery(".serviceCentresOther").show();
			}
			else
			{
				
					jQuery(".serviceCentresOther").hide();
			}

	 }	
	 else
		 {
			 jQuery(".serviceCentresOther").hide();
		 }
	
	}
	
function processTrainingProgram(val) {
	
	 var selectedTrainingProgram =$("#trainingProgram").val();
	 if(selectedTrainingProgram!=null && selectedTrainingProgram!="")
	 {
		 var trainingProgramTrim=selectedTrainingProgram.toString().trim();
		 if(trainingProgramTrim.includes('99'))
		 {
			
				jQuery(".trainingProgramOther").show();
			
			}
			else
			{
				
				jQuery(".trainingProgramOther").hide();
			}

	 }	
	 else
		 {
			 jQuery(".trainingProgramOther").hide();
		 }
	
	
	
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
function addInventry1(){
	/* var table = document.getElementById("tblData1");
    var rows = table.getElementsByTagName("tr");
	$("#tblData1 tbody").append(
			"<tr class='tableTr1'>"+
			"<td align='center'>"+'<s:text name="farmer.farmingtypeseasonland1"/>'+"</td>"+
			"<td align='center'><input type='text' class='tableTd2 form-control ' name='textVal1' maxlength='5' id='totalLandone'/><div id='msg1'></div></td>"+
			"<td align='center'><input type='text' class='tableTd3 form-control ' name='textVal2' maxlength='5' id='landIfs'/><div id='msg2'></div></td>"+
			"</tr>"+
			"<tr class='tableTr1'>"+
			"<td align='center'>"+'<s:text name="farmer.farmingtypeseasonland2"/>'+"</td>"+
			"<td align='center'><input type='text' class='tableTd4 form-control ' name='textVal3' maxlength='5' id='totalLandRain'/><div id='msg3'></div></td>"+
			"<td align='center'><input type='text' class='tableTd5 form-control ' name='textVal4' maxlength='5' id='landIfsRain'/><div id='msg4'></div></td>"+
			"</tr>" 
		
	); */
	
}
$(document).ready(function () {
    //called when key is pressed in textbox
  	$(".tableTd2").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#msg1").html("Digits Only").show();
               return false;
     	}
   	});
    
  
  	$(".tableTd4").keypress(function (e) {
     	//if the letter is not digit then display error and don't type anything
     	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        	//display error message
        	$("#msg3").html("Digits Only").show();
               return false;
     	}
   	});
  	
  	 var  url = window.location.href;
  	  var command=$("#command").val();
  	if(url.indexOf('farmerUniqueId=') < 0){
	   //if(url.indexOf('farmerId=') < 0){ 
      if(command.toLowerCase()=="update"){
  	
	     url = url+'?id='+'<s:property value="farm.id"/>'+'&farmerUniqueId='+'<s:property value="farm.farmer.id"/>'+'&farmerId='+'<s:property value="farm.farmer.farmerId"/>';
  	 }else{
  		  url = url+'?id='+'<s:property value="farm.id"/>'+'&farmerUniqueId='+'<s:property value="farmerUniqueId"/>'+'&farmerId='+'<s:property value="farmerId"/>';
     }
      $( ".lanMenu" ).each(function() {
	    	 var url1 = $(this).attr("href") +'&url='+ encodeURIComponent(url);
	    	  $( this ).attr( "href",url1);
	    	});
	     }

    var stag = $('a[href="farmer_detail.action?id=&tabValue=tabs-2"]');
	 $(stag).attr("href", "javascript:onCancel();");
     
  	
});


function refreshFarming(){
	$(".tableTd2").val("");
	$(".tableTd3").val("");
	$(".tableTd4").val("");
	$(".tableTd5").val("");

}	
var results = [];
function addRowFarmingSystem(tableID) 
{ 
	var harvestSeasonVal=$("#harvestSeason").val();
	if(harvestSeasonVal==-1){
		alert("Please select the Season");
	}else{
		/* 
	 	if(results.length ==0){
	 		alert(results);
			 results.push(harvestSeasonVal);
		} 
		 for (var i = 0; i < results.length - 1; i++) {
		
		if (harvestSeasonVal ==results[i]) {
			alert("Farming Type already added");
		}else{
			results.push(harvestSeasonVal);
			alert(results);
		}
		
		
	} 
		 */
		
	$(".frmrld").show();
	 $("#addressesTable").show();
	  var table = document.getElementById(tableID);
	 // $('#addressesTable').append( '<tr><td style="center" colspan="3">dcdfdfdfdf</td></tr>' );
     var rowCount= table.rows.length;
      var counts;
      if(rowCount=='1'){
      	counts = rowCount - 1;
      }
      else{
      	counts=$("#rowIdentifier").val();
      }
      var row = table.insertRow(rowCount);
     	var cell1 = row.insertCell(0);
      cell1.innerHTML= "Irrigated Land";
    	var cell2 = row.insertCell(1);
      var irrigatedLand = document.createElement("input");
      irrigatedLand.type = "text";
      irrigatedLand.name = "farmerLandDetailsList[" + counts + "].irrigatedLand";
      var att = document.createAttribute("class");       // Create a "class" attribute
      att.value = "checkText";                           // Set the value of the class attribute
      irrigatedLand.setAttributeNode(att);
      cell2.appendChild(irrigatedLand);
      
      var cell3 = row.insertCell(2);
      var irrigatedFarmingLand = document.createElement("input");
      irrigatedFarmingLand.type = "text";
      irrigatedFarmingLand.name = "farmerLandDetailsList[" + counts + "].irrigatedFarmingLand";
      cell3.appendChild(irrigatedFarmingLand);
      
      var cell4 = row.insertCell(3);
		var element4 = document.createElement("input");
		element4.type = "checkbox";
		element4.name="chkbox[]";
		element4.value=rowCount;
		cell4.appendChild(element4);
      
      addRainFedlandRow(tableID,counts);
      $("#rowIdentifier").val(++counts);
	}
}

function addRainFedlandRow(tableID,counts){
	var  counts=counts;
	var table = document.getElementById(tableID);
	
    var rowCount= table.rows.length;
   
    var row = table.insertRow(rowCount);
   
    var cell1 = row.insertCell(0);
    cell1.innerHTML= "Rain fed land";
	
	var cell2 = row.insertCell(1);
    var fedtotaland = document.createElement("input");
    fedtotaland.type = "text";
    fedtotaland.name = "farmerLandDetailsList[" + counts + "].fedtotaland";
    cell2.appendChild(fedtotaland);
    
    var cell3 = row.insertCell(2);
    var fedtotalics = document.createElement("input");
    fedtotalics.type = "text";
    fedtotalics.name = "farmerLandDetailsList[" + counts + "].fedtotalics";
    cell3.appendChild(fedtotalics);

    var cell4 = row.insertCell(3);
   	var tempSeasonCode = document.createElement("input");
	tempSeasonCode.setAttribute("type", "hidden");
	tempSeasonCode.setAttribute("name", "farmerLandDetailsList[" + counts + "].tempSeasonCode");
	tempSeasonCode.setAttribute("value", $("#harvestSeason").val());
    cell4.appendChild(tempSeasonCode);
  
}


function validateTableData(){
var table = document.getElementById("addressesTable");
var flag=true;
if(table!=null)
{
var rowCount= table.rows.length;
var rowLimit=$("#rowIdentifier").val();
for(var i=0;i<=rowLimit-1;i++){
	var irrigatedLandVal=$("input[name~='farmerLandDetailsList["+i+"].irrigatedLand']").val();
	if(irrigatedLandVal !=undefined && irrigatedLandVal !=''){
		
	 if(isNaN(irrigatedLandVal)){
		
		flag=false;
		$("#irrigatedLandError").html("Please Enter the Valid Total Irrigated  Land (in Acres)");
		break;
		} else{
			$("#irrigatedLandError").html("");
		}
	}else{
		$("#irrigatedLandError").html("");
	}
}
// validation for RainfeedLand
for(var i=0;i<=rowLimit-1;i++){
	var fedtotalandVal=$("input[name~='farmerLandDetailsList["+i+"].fedtotaland']").val();
	if(fedtotalandVal !=undefined && fedtotalandVal !=''){
		if(isNaN(fedtotalandVal)){
		flag=false;
		$("#rainFedlandError").html("Please Enter the Valid Total Rain fed Land (in Acres)");
		break;
		} else{
			$("#rainFedlandError").html("");
		}
	}else{
		$("#rainFedlandError").html("");
	}
}

}
return flag;
}

function validateUpdateTable(){
	var table = document.getElementById("updatableTbl");
	var flag=true;
	if(table!=null)
	{
	var rowCount= table.rows.length;
	
	var rowLimit=$("#landDltUpdateRowCount").val();
	
	for(var i=0;i<=rowLimit-1;i++){
		var irrigatedLandVal=$("input[name~='updatefarmerLandDetailsList["+i+"].irrigatedTotLand']").val();
		
		if(irrigatedLandVal !=undefined && irrigatedLandVal !=''){
			
		 if(isNaN(irrigatedLandVal)){
			
			flag=false;
			$("#irrigatedLandError").html("Please Enter the Valid Total Irrigated  Land (in Acres)");
			break;
			} else{
				$("#irrigatedLandError").html("");
			}
		}else{
			$("#irrigatedLandError").html("");
		}
		
	}
	// validation for RainfeedLand
	for(var i=0;i<=rowLimit-1;i++){
		var fedtotalandVal=$("input[name~='updatefarmerLandDetailsList["+i+"].rainfedTotLand']").val();
		if(fedtotalandVal !=undefined && fedtotalandVal !=''){
			fedtotalandVal=parseFloat(fedtotalandVal);
			if(isNaN(fedtotalandVal)){
			
			flag=false;
			$("#rainFedlandError").html("Please Enter the Valid Total Rain fed Land (in Acres)");
			break;
			} else{
				$("#rainFedlandError").html("");
			}
		}else{
			$("#rainFedlandError").html("");
		}
	
	}

	}
	return flag;
	
	}




function clearRow(tableId){
var table = document.getElementById(tableId);

try {
var table = document.getElementById(tableId);
var rowCount = table.rows.length;

for(var i=1; i<rowCount; i++) {
	var row = table.rows[i];
	var chkbox = row.cells[3].childNodes[0];
	if(null != chkbox && true == chkbox.checked) {
		if(rowCount <= 1) {
			
			//break;
		}
		table.deleteRow(i);
		table.deleteRow(i);
		rowCount--;
		i--;
	}


}

}catch(e) {
	//alert(e);
}

var table = document.getElementById(tableId);
var rowCount = table.rows.length;
if(rowCount==1){
	$("#addressesTable").hide();
}
}
function deleteRowSelected(rowCount){
var farmerLandId=$("#farmerLandDltId").val();
$.post("farm_populateDeleteFarmerland",{farmerLandId:farmerLandId},function(result){
	var table = document.getElementById("updatableTbl");
	
	$(".frLandDetail"+rowCount).hide();
	//table.hideRow(rowCount);
	//table.deleteRow(rowCount);
	
	//table.deleteRow(rowCount);
	var newRowCount=$("#farmerLandDltUpdateRowCount").val();
	$("#farmerLandDltUpdateRowCount").val(newRowCount-2);
	$("#landDltUpdateRowCount").val(newRowCount-2);
	if(newRowCount==1){
		$("#updatableTbl").hide();
	}
	
});
}
function addFramerLandId(val){
	$("#farmerLandDltId").val(val);
}

function soilTestsVisibility(){
	var soilTestsClass = $("#tblSoilData").attr('class');
	if(soilTestsClass == "panel-collapse collapse"){		
		 $("#tblSoilData").addClass("hide");
	}
	else{
		$("#tblSoilData").removeClass("hide");
	}
	
}


function appendRow(){
	var expendDetail=$("#expendDetail").val();
	var expendVal=$("#expendVal").val();
	var expendTfoot=document.getElementById("expenditure")
	var rows = expendTfoot.getElementsByTagName("tr");

	if(expendDetail!=''||expendVal!=''){
		
		$("#expenditure").append(
				"<tr class='tableTr1' id='expenditureRow"+((rows.length)+1)+"'>"+
					"<td align='center'><div id='expndDtl"+((rows.length)+1)+"'>"+expendDetail+"</div></td>"+
					"<td align='center'><div id='expndValue"+((rows.length)+1)+"'>"+expendVal+"</div></td>"+
				'<td style="text-align:center;">'+
				
				'<button type="button" class="btn btn-sm btn-success" aria-hidden="true" onclick="editExpndRow('+((rows.length)+1)+')"><i class=" fa fa-pencil-square-o"></i></button>'+
				
				' <button type="button" class="btn btn-sm btn-danger" aria-hidden="true" onclick="deleteExpndRow('+((rows.length)+1)+')"><i class="fa fa-trash" aria-hidden="true"></i></button></td>'+
				+"</tr><input type='hidden' value='0' id='expenditureId"+((rows.length)+1)+"'>"
		);
	}
	resetExpndData();
}

function editExpndRow(value){
	var expndDtlId="#expndDtl"+value;
	var expndValueId="#expndValue"+value;
	jQuery("#expendDetail").val(jQuery(expndDtlId).text());
	jQuery("#expendVal").val(jQuery(expndValueId).text());
	resetExpndData
	$("#appendBtnId").attr("onclick","editedExpndRows("+value+")");
}
function editedExpndRows(value){
	var expendDetail=jQuery("#expendDetail").val();
	var expendVal=jQuery("#expendVal").val();
	
	var expndDtlId="#expndDtl"+value;
	var expndValueId="#expndValue"+value;
	jQuery(expndValueId).text(expendVal);
	jQuery(expndDtlId).text(expendDetail);
	
	
	resetExpndData();
	 $("#appendBtnId").attr("onclick","appendRow()"); 
}
function deleteExpndRow(value){
	var expndDtlId="#expndDtl"+value;
	var expndValId="#expndVal"+value;
	
	jQuery(expndDtlId).text('');
	jQuery(expndValId).text('');
	jQuery("#expenditureRow"+value).attr("class","hide");
	
	
}

function resetExpndData(){
	jQuery("#expendDetail").val('');
	jQuery("#expendVal").val('');
	
	$("#appendBtnId").attr("onclick","appendRow()"); 
}

function listMunicipality(obj){
	var selectedLocality = $('#localities').val();
	jQuery.post("farm_populateCity.action",{id:obj.value,dt:new Date(),selectedLocality:obj.value},function(result){
		insertOptions("city",$.parseJSON(result));
		if(isGramPanchayatEnable=='1'){
			listPanchayat(document.getElementById("city"));
		}else{
			listVillageByCity(document.getElementById("city"));
		}
	});
}

function listPanchayat(obj){
	try{
	var selectedCity = $('#city').val();
	
	jQuery.post("farm_populatePanchayath.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
		insertOptions("panchayath",$.parseJSON(result));
		listVillage(document.getElementById("panchayath"));
	});
	}catch(e){
	}
}

function listVillage(obj){
	if(isGramPanchayatEnable=='1'){
		var selectedPanchayat = $('#panchayath').val();
		var enableGramPanch=document.getElementById("gramPanchayatEnable").value;
		jQuery.post("farm_populateVillage.action",{id:obj.value,dt:new Date(),selectedPanchayat:obj.value,gramPanchayatEnable:enableGramPanch},function(result){
			insertOptions("village",$.parseJSON(result));
		//	listSamithi(document.getElementById("village"));
		});
	}else{
		listVillageByCity(document.getElementById("city"));
	}
}

function listVillageByCity(obj){
		var selectedCity = $('#city').val();
		jQuery.post("farm_populateVillageByCity.action",{id:obj.value,dt:new Date(),selectedCity:obj.value},function(result){
			insertOptions("village",$.parseJSON(result));
			//listSamithi(document.getElementById("village"));
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

function listVariety(obj){
	var selectedCrop = $('#farmCropsMasters').val();
	jQuery.post("farm_populateVariety.action",{id:obj.value,dt:new Date(),selectedCrop:obj.value},function(result){
		insertOptions("farmVarietyMaster",$.parseJSON(result));
	});
}
function saveTreeDetails(obj){
	var flag=0;
	var paramObj = {};	
	refreshTreeTemplateList(obj);
		
	
		
}

function refreshTreeDetails(){
	$(".tableTd27").val("");
	$(".tableTd28").val("");
	$(".tableTd29").val("");
	$(".tableTd30").val("");
}
var totalOrgTrees=0.0;
var totalConvTrees=0.0;
var noOfTrees =0.0;
var bodyContentTree="";
var varietyValTxt="";
var varietyId="";
function refreshTreeTemplateList(obj){
	  var bodyContent = "";
	 
	  var flag=0;
	  var rowCount = $('#treeData tr').length;
	  rowCount++;
	 var prodStatus= $(".tableTd27 option:selected").text();
	  var farmVarietyMaster= $(".tableTd28 option:selected").text();
	  var farmVarietyMasterVal= $(".tableTd28 option:selected").val();
	  var years =$(".tableTd29 option:selected").text();
	  var yearsVal =$(".tableTd29 option:selected").val();
	  var noOfTrees=$('.tableTd30').val();
	  var prodStatusId= $(".tableTd27 option:selected").val();
	  if(prodStatus == ''||prodStatus == "Select"){
			alert('<s:text name="Please Select the ProductStatus"/>');
			flag=1;
			
		}
	  
	  if(farmVarietyMasterVal == ''||farmVarietyMasterVal == '-1'){
			alert('<s:text name="Please Select the Cultivar"/>');
			flag=1;
			
		} 
	  
	  if(yearsVal == ''){
			alert('<s:text name="Please Select the Year"/>');
			flag=1;
			
		} 
	  
	  if(noOfTrees == ''){
			alert('<s:text name="Please Enter the Number of Trees"/>');
			flag=1;
			
		} 
	  
	  
	  if(flag==0){
		    var idd = $('#treeDataDetail').children('tr').length+1;
		    bodyContent+='<tr id='+rowCount+' class="tableTr1">';
		    bodyContent+='<td class="prodStatus"   align="left">'+$(".tableTd27 option:selected").text()+'</td>';
		    bodyContent+='<td class="farmVarietyMaster"  align="left">'+$(".tableTd28 option:selected").text()+'</td>';
		    bodyContent+='<td class="years"  align="left">'+$(".tableTd29 option:selected").text()+'</td>';
		    bodyContent+='<td class="noOfTrees"   align="left">'+$('.tableTd30').val()+'</td>';
		    bodyContent+='<td align="left"><a href="#" class="fa fa-trash" onclick="butTreeDelete('+rowCount+');"/></td>';
		    
		    bodyContent+='<td class="hide"   align="left">'+$(".tableTd27 option:selected").val()+'</td>';
		    bodyContent+='<td class="hide"  align="left">'+$(".tableTd28 option:selected").val()+'</td>';
		    bodyContent+='<td class="hide"  align="left">'+$(".tableTd29 option:selected").val()+'</td>';
		    bodyContent+='</tr>';
		 $('#treeData').append(bodyContent);
		 
		

		    $('.tableTd27').val('');
		    $('.tableTd28').val('');
		    $('.tableTd29').val('');
		    $('.tableTd30').val('');
		  
		   
		  }
	 
	 
	}
	
function butTreeDelete(indx){
	if(confirm('<s:text name="confirm.delete"/>')){
	var index=indx-1;
	$("#treeData tr:eq("+index+")").remove();
		 
	}
}


/*  function addTreeDetails(){
	var idd = $('#treeDataDetail').children('tr').length;
	} 
 */




</script>
<body>

	<div class="error">
		<%-- <s:actionerror />
		<s:fielderror /> --%>
		<%-- <sup>*</sup>
		<s:text name="reqd.field" /> --%>
		<div style="float: right; color: #000000">
			<s:hidden name="farmerUniqueId" id="farmerUniqueId" />
			<table class="table table-bordered aspect-detail">
				<tr>
					<td style="padding-right: 5px; text-align: right;"><b><s:property
								value="%{getLocaleProperty('farmer.name')}" /></b> <s:if
							test='"update".equalsIgnoreCase(command)'>


							<s:property value="farm.farmer.farmerId" /> - <s:property
								value="farm.farmer.name" />
						</s:if> <s:else>
							<s:property value="farmerName" />
						</s:else></td>
				</tr>
			</table>
		</div>
	</div>

	<div id="irrigatedLandError"
		style="text-align: left; padding: 5px 0 0 0; color: red;"></div>
	<div id="rainFedlandError"
		style="text-align: left; padding: 5px 0 0 0; color: red;"></div>


	<s:form name="form" cssClass="fillform" action="farm_%{command}"
		role="form" method="post" enctype="multipart/form-data">
		<s:hidden key="currentPage" />

		<s:hidden id="farmerId" name="farmerId" />
		<s:hidden id="farmerUniqueId" name="farmerUniqueId" />
		<s:hidden id="farmerName" name="farmerName" />
		<s:hidden id="plantDetailString" name="plantDetailString" />
		<s:hidden name="tabIndex" />
		<s:hidden id="dateOfInspection" name="dateOfInspection" />
		<s:hidden id="benefitaryString" name="benefitaryString" />
		<s:hidden id="sangham" name="sangham" />
		<s:hidden id="prodEditArray" name="prodEditArray" />
		<s:hidden id="rowIdentifier" name="rowIdentifier" />
		<s:hidden id="farmerLandDltId" name="farmerLandDltId" />
		<s:hidden id="farmerLandDltUpdateRowCount"
			name="farmerLandDltUpdateRowCount" />
		<s:hidden id="status" name="farm.farmIcsConv.status" />
		<s:hidden id="season" name="farm.farmIcsConv.season" />
		<s:hidden id="insMobile" name="farm.farmIcsConv.inspectorMobile" />
		<s:hidden id="totalLand" name="farm.farmIcsConv.totalLand" />
		<s:hidden id="organicLand" name="farm.farmIcsConv.organicLand" />
		<s:hidden id="totalSite" name="farm.farmIcsConv.totalSite" />
		<s:hidden id="insType" name="farm.farmIcsConv.insType" />
		<s:hidden id="jsonStr" name="treeDetailJsonString" />
		<s:hidden id="jsonString" name="farm.jsonString" />
		<s:hidden id="dynamicFieldsArray" name="dynamicFieldsArray" />
		<s:hidden id="dynamicListArray" name="dynamicListArray" />

		<s:if test='"update".equalsIgnoreCase(command)'>
			<s:hidden key="farm.id" class="uId" />
		</s:if>
		<s:if test='"updateActPlan".equalsIgnoreCase(command)'>
			<s:hidden key="farm.id" class="uId" />
		</s:if>
		<s:hidden key="command" id="command" />
		<s:hidden id="treeDetailToJson" name="treeDetailToJson" />
		<div class="appContentWrapper marginBottom farmInfo">
			<div class="formContainerWrapper">
				<div class="ferror" id="errorDiv" style="color: #ff0000">
					<s:actionerror />
					<s:fielderror />
				</div>

				<h2>
					<a data-toggle="collapse" data-parent="#accordion" href="#farmInfo"
						class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.farm')}" />
					</a>
				</h2>
			</div>
			
			<div id="farmInfo" class="panel-collapse collapse in">
				<div class="flexform">
				
					<div class="flexform-item ">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.farmName')}" /><sup
							style="color: red;">*</sup>
						</label>
						<div class="form-element">
							<s:textfield name="farm.farmName" theme="simple" maxlength="35"
								cssClass="form-control  checktexts" />
						</div>
					</div>
					
							<div class="flexform-item "  >
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.season')}" />
									
									<sup style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<%-- <s:select cssClass="form-control select2" id="cropSeasonValue"
									name="cropSeasonCode" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="cropSeasonsMap" /> --%>
									
									 <s:select cssClass="form-control select2" id="farmSeason"
									name="farm.season" headerKey="-1" listKey="key"
									listValue="value" headerValue="%{getText('txt.select')}"
									list="seasonList" /> 
									
							</div>
						</div>
					<s:if test="currentTenantId=='welspun'">
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('fieldStatus')}" /> </label>
							<div class="form-element">
								<s:select cssClass="form-control " id="icsType"
									name="farm.farmIcsConv.icsType" list="icsStatusList"
									headerKey="" headerValue="%{getText('txt.select')}" />
							</div>
						</div>
					</s:if>
					<s:if test="currentTenantId=='welspun'">
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('8A/7/12')}" /> </label>

							<div class="form-element">
								<s:radio list="processingActList" listKey="key"
									listValue="value"
									name="farm.farmDetailedInfo.processingActivity" theme="simple" />
							</div>
						</div>
					</s:if>
					
					<div class="flexform-item surveyNo">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm.surveyNo')}" /> </label>
						<div class="form-element">
							<s:textfield name="farm.farmDetailedInfo.surveyNumber"
								theme="simple" maxlength="35" cssClass="form-control " />
						</div>
					</div>
				

					<s:if test="currentTenantId=='griffith'">
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalLand')}" /><s:if
									test="currentTenantId!='symrise'"> (<s:property
									value="%{getAreaType()}" />)</s:if><s:if
									test="currentTenantId!='olivado' && currentTenantId!='symrise'">
									<sup style="color: red;">*</sup>
								</s:if>

							</label>
							<%-- <s:if
								test='"update".equalsIgnoreCase(command) && currentTenantId=="pratibha"'>
								<div class="form-element">
									<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
										id="totalLand" cssClass="form-control " theme="simple"
										maxlength="12" onchange="calculateLandHect();" readonly="true"
										onkeypress="return isDecimal(event)" />

								</div>
							</s:if>
							<s:elseif test="currentTenantId=='symrise'">
							<div class="form-element">
									<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
										id="totalLandd" cssClass="form-control " theme="simple"
										maxlength="12" onchange="calculateNoOfWineOnPlot();"
										onkeypress="return isDecimal(event)" />

								</div>
							</s:elseif>
							<s:else> --%>
								<div class="form-element">
									<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
										id="totalLand" cssClass="form-control " theme="simple"
										maxlength="12" onchange="calculateLandHect();"
										onkeypress="return isDecimal(event)" />

								</div>
							<%-- </s:else> --%>
						</div>

					</s:if>
					<%-- 				<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.totalLandHectare')}" />
						</label>
						<div class="form-element">
							<s:label id="landHectValues" name="farm.totalLandHectare" />
						</div>
					</div> --%>
					
					<div class="flexform-item plantArea">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.proposedPlantingArea')}" /> (<s:property
								value="%{getAreaType()}" />) <s:if
								test="currentTenantId=='olivado'">
								<sup style="color: red;">*</sup>
							</s:if>
						</label>
						<div class="form-element">
							<s:textfield name="farm.farmDetailedInfo.proposedPlantingArea"
								id="plantingArea" cssClass="form-control " theme="simple"
								maxlength="12" onchange="calculatePlantHect();"
								onkeypress="return isDecimal(event)" />
						</div>
					</div>
				
					<%-- <div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
						</label>
						<div class="form-element">
							<s:label id="plantHectValues" name="farm.proposedPlantingHectare" />
						</div>
					</div> --%>

					<div class="flexform-item ownLand">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('ownLand')}" />
						</label>
						<div class="form-element">
							<s:textfield name="farm.ownLand" id="ownArea"
								cssClass="form-control " theme="simple" maxlength="12"
								onkeypress="return isDecimal(event)" onchange="calculateNoOfWineOnPlot();" />
						</div>
					</div>
						
                     
                     <s:if test="currentTenantId=='symrise'">
                     <div class="flexform-item noOfWineOnPlot">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.noOfWineOnPlot')}" />
						</label>
						<div class="form-element">
							<s:label id="noOfWineOnPlot"/>
						</div>
					</div>
					</s:if>
					<s:if test="currentTenantId!='olivado'">
						<div class="flexform-item fggfarm">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('leasedland')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.leasedLand" id="leasedArea"
									cssClass="form-control " theme="simple" maxlength="12"
									onkeypress="return isDecimal(event)" />
							</div>
						</div>
					</s:if>

					<div class="flexform-item ">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('irriLand')}" />
						</label>
						<div class="form-element">
							<s:textfield name="farm.irrigationLand" id="irriArea"
								cssClass="form-control " theme="simple" maxlength="12"
								onkeypress="return isDecimal(event)" />
						</div>
					</div>
					
					<s:if
						test="currentTenantId!='welspun' && currentTenantId!='griffith'">
						<div class="flexform-item">
							<label for="txt"> <%-- <s:text name="farm.landOwned" /> --%>
								<s:property value="%{getLocaleProperty('farm.landOwned')}" /><s:if
								test="currentTenantId =='livelihood'||currentTenantId =='agro'  ">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<s:if test="currentTenantId=='livelihood'">
								<div class="form-element">
									<s:select list="farmOwnedList" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" cssClass="form-control  select2"
									name="farm.farmDetailedInfo.farmOwned" theme="simple" id="farmOwned"
									onchange="showFarmOther(this.value)" />
								</div>
							</s:if>
							<s:else>
								<div class="form-element">
								<s:select list="farmOwnedList" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" cssClass="form-control  select2"
									name="selectedFarmOwned" theme="simple" id="farmOwned"
									onchange="showFarmOther(this.value)" />
								</div>
							</s:else>
						</div>
						<div class="flexform-item farmOtherDiv">

							<div class="form-element">

								<s:textfield id="farmOther" name="farm.farmOther"
									cssClass="form-control " theme="simple" />
							</div>
						</div>
					</s:if>
					<%-- <div class="flexform-item">
						<label for="txt">  <s:text name="farm.landGradient" />
							<s:property value="%{getLocaleProperty('farm.landGradient')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedGradient" list="landGradientList"
								listKey="key" listValue="value" theme="simple" id="land"
								cssClass="form-control input-sm select2" multiple="true" />
						</div>
					</div> --%>
					<div class="flexform-item">
						<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.sameAddressofFarmer')}" />
						</label>
						<div class="form-element">
							<s:checkbox key="farm.farmDetailedInfo.sameAddressofFarmer"
								cssClass="" name="farm.farmDetailedInfo.sameAddressofFarmer"
								theme="simple"
								onclick="javascript:sameAsFarmerAddress(this.name);"
								id="chckAddress" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.farmAddress')}" /><s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if>
						</label>
						<div class="form-element">
							<s:textarea id="addressTxt"
								name="farm.farmDetailedInfo.farmAddress"
								cssClass="form-control " cssStyle="height:50px" theme="simple" />
						</div>
					</div>
					<s:if test="currentTenantId!='susagri'">
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm.appRoad')}" /> </label>
						<div class="form-element">
							<s:select name="selectedRoad" list="approadList" listKey="key"
								listValue="value" theme="simple" id="gradient"
								cssClass="form-control input-sm select2" multiple="true" />
						</div>
					</div>
					
					<div class="flexform-item certYear">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm.certYear')}" /> </label>
						<div class="form-element">
							<s:select list="yearList" listKey="key" listValue="value"
								cssClass="form-control select2" name="farm.certYear"
								theme="simple" id="certYear" />
						</div>
					</div>
					</s:if>
             <s:if test="currentTenantId!='griffith'">
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.photo')}" /> <s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if><span
							style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:file name="farm.farmImage" id="farmImage"
								cssClass="form-control" onchange="checkImgHeightAndWidth(this)" />
									<s:if test="currentTenantId =='livelihood'">
								<s:if test="command =='update' && farm.Photo!=null">
								<button type='button' class='btn btn-sm pull-right photo'
									style='margin-right: 15%'
									onclick="enableFarmPhotoModal(<s:property value="farm.id"/>)">
									<i class='fa fa-picture-o' aria-hidden='true'></i>
								</button>
							</s:if>
							</s:if>
						</div>
					</div>
				</s:if>
					<%-- <div class="flexform-item">
						<label for="txt"> <s:text name="farm.photo" /> <span
							style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:if
								test='farmImageByteString!=null && farmImageByteString!=" "'>
								<s:textfield name="farm.farmImageFileName"
									id="farmImageFileName" theme="simple" maxlength="35" />
								<s:file name="farm.farmImage" id="farmImage"
									cssStyle="display:none;"
									onchange="checkImgHeightAndWidth(this)" />
							</s:if>
							<s:else>
								<s:textfield name="farm.farmImageFileName"
									id="farmImageFileName" theme="simple" maxlength="35" />
								<s:file cssClass="form-control" name="farm.farmImage"
									id="farmImage" onchange="checkImgHeightAndWidth(this)" />&nbsp;
			               </s:else>
							<button type="button" class="aButtonClsWbg" id="remImg"
								onclick='deleteFile(<s:property value="farm.id" />)'>
								<i class="fa fa-trash-o" aria-hidden="true"></i>
							</button>

							<div></div>
							<script type="text/javascript">
			                  <s:if test='farmImageByteString==null || farmImageByteString==" "'>
			                  //$('#remImg').hide();
			                  </s:if>
			               </script>
						</div>
					</div> --%>
					<%-- <div class="flexform-item">
						<div class="form-element">
							<s:if
								test='farmImageByteString!=null && farmImageByteString!=" "'>
								<img width="50" height="50" border="0" id="image"
									src="data:image/png;base64,<s:property value="farmImageByteString"/>">
							</s:if>
							<s:else>
								<img width="50" height="50" id="remImgs" border="0"
									src="img/no-image.png">
							</s:else>
							<s:hidden name="farm.farmImageExist" id="farmImageExist" />
						</div>
					</div> --%>

					<s:if test="currentTenantId!='griffith'">
						<div class="flexform-item farmRegNo">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farmRegNo')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.farmRegNumber" cssClass="form-control "
									theme="simple" onkeypress="return isAlpahaNumeric(event)"
									maxlength="40" />
							</div>
						</div>
					</s:if>

					<%-- <div class="flexform-item">
						<label for="txt"> <s:text name="farm.regYear" />
						<sup
							style="color: red;">*</sup>
						
						</label>
						<div class="form-element">
							<s:textfield name="regYearString" id="calendarDOJ"
								readonly="true" theme="simple" cssClass="form-control " />
						</div>
					</div> --%>
					<s:if test="currentTenantId!='susagri'">
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm.topo')}" /><s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if> </label>
						<div class="form-element">
							<s:select list="topologyList" listKey="key" listValue="value"
								cssClass="form-control select2" headerKey="-1"
								headerValue="%{getText('txt.select')}" name="farm.landTopology"
								theme="simple" id="landTopo" />
						</div>
					</div>
					</s:if>
					
					<div class="flexform-item">
						<label for="txt"> <%--  <s:text name="farm.landGradient" /> --%>
							<s:property value="%{getLocaleProperty('farm.landGradient')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedGradient" list="landGradientList"
								listKey="key" listValue="value" theme="simple" id="land"
								cssClass="form-control input-sm select2" multiple="true" />
						</div>
					</div>

					<s:if test="currentTenantId=='efk'">
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.waterSourceList')}" />
							</label>
							<div class="form-element">
								<s:select name="farm.waterSource" list="waterSourceList"
									listKey="key" listValue="value" theme="simple"
									cssClass="form-control input-sm select2" multiple="true" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.localNameOfCrotenTree')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.localNameOfCrotenTree"
									cssClass="form-control " theme="simple" maxlength="40" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.NoOfCrotenTrees"
									cssClass="form-control " theme="simple"
									onkeypress="return isNumber(event)" maxlength="40" />
							</div>
						</div>

					</s:if>

					<s:if
						test="currentTenantId!='kpf' && currentTenantId !='wub' && currentTenantId!='simfed' && currentTenantId!='welspun' && currentTenantId!='iffco'">
						<div class="flexform-item waterHarvest">
							<label for="txt"><s:text name="farm.waterHarvest" /></label>
							<div class="form-element">
								<s:select name="waterHarvest" list="waterHarvestList"
									listKey="key" listValue="value" theme="simple"
									id="waterHarvest" cssClass="form-control input-sm select2"
									multiple="true" />
							</div>
						</div>
						<div class="flexform-item avgStore">
							<label for="txt"><s:property
									value="%{getLocaleProperty('avgStore')}" /></label>
							<div class="form-element">
								<s:textfield name="farm.avgStore" cssClass="form-control "
									theme="simple" onkeypress="return isDecimal(event)"
									maxlength="35" />
							</div>
						</div>
						<div class="flexform-item tree">
							<label for="txt"><s:property
									value="%{getLocaleProperty('treeName')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.treeName" cssClass="form-control "
									theme="simple" maxlength="35" />
							</div>
						</div>
					</s:if>
			            <s:if test="currentTenantId!='cofBoard' && currentTenantId!='wilmar' && currentTenantId!='welspun'">
						<div class="flexform-item distanceProcessingUnit">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.distanceProcessingUnit" id="distanceProcessingUnit"
									cssClass="form-control " theme="simple" maxlength="10"  onkeypress="return isDecimal(event)"/>
							</div>
						</div>
					</s:if>
					<s:if test="currentTenantId=='cofBoard'">
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.distanceProcessingUnit"
									cssClass="form-control " theme="simple" maxlength="10"  onkeypress="return isDecimal(event)"/>
							</div>
						</div>
						</s:if>
					<s:if test="currentTenantId=='wilmar'">
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.distanceProcessingUnit"
									cssClass="form-control " theme="simple" maxlength="10" />
							</div>
						</div>
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('processingActivity')}" /> </label>
							<div class="form-element">
								<s:radio list="processingActList" listKey="key"
									listValue="value"
									name="farm.farmDetailedInfo.processingActivity" theme="simple" />
							</div>
						</div>
					</s:if>
					<div class="flexform-item organicStatusDiv">
						<label for="txt"><s:property
								value="%{getLocaleProperty('organicStatus')}" /> </label>
						<div class="form-element">
							<s:textfield id="organicStatus"
								name="farm.farmIcsConv.organicStatus" cssClass="form-control "
								theme="simple" />
						</div>
					</div>


					<s:if test="currentTenantId=='pratibha'">
						<div class="flexform-item">
							<label for="txt"><s:text name="farm.PlatNo" /> </label>
							<div class="form-element">
								<s:textfield name="farm.farmPlatNo" cssClass="form-control "
									theme="simple" onkeypress="return isAlpahaNumeric(event)"
									maxlength="35" />
							</div>
						</div>

					</s:if>
					<s:if test="currentTenantId!='griffith'  && currentTenantId!='susagri'">
						 <div class="flexform-item">
							
							<label for="txt">
								<s:property	value="%{getLocaleProperty('farm.totalLand')}" /> <s:if test="currentTenantId!='griffith'">(<s:property	value="%{getAreaType()}" />)</s:if>
								<sup style="color: red;">*</sup>
							</label>
							<s:if
								test='"update".equalsIgnoreCase(command) && currentTenantId=="griffith"'>
								<div class="form-element">
									<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
										id="totalLand" cssClass="form-control " theme="simple"
										maxlength="12" onchange="calculateLandHect();" readonly="true"
										onkeypress="return isDecimal(event)" />

								</div>
							</s:if>
							<s:else> 
								<div class="form-element">
									<s:textfield name="farm.farmDetailedInfo.totalLandHolding"
										id="totalLand" cssClass="form-control " theme="simple"
										maxlength="12" onchange="calculateLandHect();"
										onkeypress="return isDecimal(event)" />

								</div>
				 		</s:else>
						</div> 
						<%-- <div class="flexform-item">
							<label for="txt"> <s:text name="farm.landOwned" />
								<s:property value="%{getLocaleProperty('farm.landOwned')}" /><sup style="color: red;">*</sup>
							</label>
							<s:if test="currentTenantId=='symrise'">
							<div class="form-element">
								<s:select list="farmOwnedList" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" cssClass="form-control  select2"
									name="selectedFarmOwned" theme="simple" id="farmOwned"
									onchange="calculateNoOfWineOnPlot()" />
							</div>
							</s:if>
							<s:else>
							<div class="form-element">
								<s:select list="farmOwnedList" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" cssClass="form-control  select2"
									name="selectedFarmOwned" theme="simple" id="farmOwned"
									onchange="showFarmOther(this.value)" />
							</div>
							</s:else>
						</div>
						
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.photo')}" /> <span
							style="font-size: 8px"> <s:text name="farmer.imageTypes" />
								<font color="red"> <s:text name="imgSizeMsg" /></font>
						</span>
						</label>
						<div class="form-element">
							<s:file name="farm.farmImage" id="farmImage"
								cssClass="form-control" onchange="checkImgHeightAndWidth(this)" />
						</div>
					</div> --%>

					</s:if>
					<%-- <div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.farmIrrigation')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedIrrigationSource"
								list="irrigationSourceList" headerKey="-1" headerValue="Select"
								listKey="key" listValue="value" theme="simple"
								id="irrigationSource"
								onchange="irrigationOtherValue(this.value);"
								cssClass="form-control " />
						</div>
					</div> --%>

				</div>
			</div>
		</div>

		<div class="appContentWrapper marginBottom contactInfo hide ">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmLocation" class="accrodianTxt"> <s:text
							name="info.cont" />
					</a>
				</h2>
			</div>
			<div id="farmLocation" class="panel-collapse collapse in">
				<div class="flexform">
					<div class="flexform-item">
						<label> <s:property
								value="%{getLocaleProperty('locality.name')}" />
						</label>
						<div class="form-element">
							<s:select cssClass="form-control  select2"
								name="selectedLocality" id="localities" list="listLocalities"
								headerKey="" headerValue="%{getText('txt.select')}"
								theme="simple" onchange="listMunicipality(this)" is="district" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('city.name')}" />
						</label>
						<div class="form-element">
							<s:select cssClass="form-control  select2" id="city"
								name="selectedCity" list="cities" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="id"
								listValue="name" theme="simple" onchange="listPanchayat(this)" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('panchayat.name')}" />
						</label>
						<div class="form-element">
							<s:select cssClass="form-control  select2" id="panchayath"
								name="selectedPanchayat" list="panchayat" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="id"
								listValue="name" theme="simple" onchange="listVillage(this)" />
						</div>
					</div>

					<div class="flexform-item">

						<label for="txt"><s:property
								value="%{getLocaleProperty('village.name')}" /> <sup
							style="color: red;">*</sup> </label>
						<div class="form-element">
							<s:select cssClass="form-control select2" name="selectedVillage"
								id="village" list="villages" listKey="id" listValue="name"
								headerKey="" headerValue="%{getText('txt.select')}"
								theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('profile.samithi')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedSamithi" cssClass="form-control  select2"
								id="samithii" list="samithi" listKey="key" listValue="value"
								headerKey="" headerValue="%{getText('txt.select')}" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('fpoGroup')}" />
						</label>
						<div class="form-element">
							<s:select cssClass="form-control  select2" id="fpoGroup"
								list="fpo" name="selectedFpo" headerKey=""
								headerValue="%{getText('txt.select')}" requiredLabel="" />
						</div>
					</div>
				</div>
			</div>
		</div>
<s:if test="currentTenantId!='griffith'">
		<div class="appContentWrapper marginBottom soilIrrigationInfo">
			<div class="formContainerWrapper">


				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmSoilAccordian" class="accrodianTxt collapsed"><s:property
							value="%{getLocaleProperty('info.soil')}" /> </a>
				</h2>
			</div>
			<div id="validateSoilTexError"
				style="text-align: center; padding: 5px 0 0 0; color: red;"></div>
			<div id="farmSoilAccordian" class="panel-collapse collapse in">
				<div class="flexform">

					<div class="flexform-item soilType">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.soilType')}" />
						</label>
						<div class="form-element inputCon">
							<s:select name="selectedSoilType" list="soilTypeList"
								listKey="key" listValue="value" theme="simple" id="soilType"
								cssClass="form-control input-sm select2" multiple="true" />
							&nbsp;

							<button type="button" onclick="addSoilDetail();"
								class="addBankInfo slide_open" data-toggle="modal"
								data-target="#slideSoil">
								<i class="fa fa-plus" aria-hidden="true"></i>
						</div>
					</div>
					
					<s:if test="currentTenantId!='susagri'">
					<div class="flexform-item soilTexture">
						<label for="txt">
						<s:property
								value="%{getLocaleProperty('farm.soilTexture')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedTexture" list="soilTextureList"
								listKey="key" listValue="value" theme="simple" id="texture"
								cssClass="form-control input-sm select2" multiple="true" />
							&nbsp;
							<button type="button" value="ADD" onclick="addSoilTexDetail()"
								class="addBankInfo slide_open" data-toggle="modal"
								data-target="#slideSoilTex">
								<i class="fa fa-plus" aria-hidden="true"></i>
						</div>
					</div>		

					<div class="flexform-item">
						<label for="txt"><%-- <s:text name="farm.soilFertilityStatus" /> --%>
						<s:property value="%{getLocaleProperty('farm.soilFertilityStatus')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedSoilFertility" list="soilFertilityList"
								headerKey="-1" headerValue="%{getText('txt.select')}"
								listKey="key" listValue="value" theme="simple"
								id="soilFertility" class="form-control  select2" />
						</div>
					</div>
					</s:if>

					<div class="flexform-item">
						<label for="txt"><%-- <s:text name="farm.farmIrrigationSource" /> --%>
						<s:property value="%{getLocaleProperty('farm.farmIrrigationSource')}" /><s:if
								test="currentTenantId =='farmAgg'">
								<sup style="color: red;">*</sup>
							</s:if>
						</label>
						<div class="form-element">
							<s:select name="selectedIrrigation" list="farmIrrigationList"
								listKey="key" listValue="value" theme="simple"
								id="farmIrrigation" cssClass="form-control select2"
								multiple="true" onchange="selectedIrrigationValue(this.value);" />
						</div>
					</div>
					
					

					<div class="flexform-item" id="farmIrrigationType">
						<label for="txt"> <s:property
								value="%{getLocaleProperty('farm.farmIrrigationType')}" />
						</label>
						<div class="form-element">
							<s:select name="selectedIrrigationSource"
								list="irrigationSourceList" headerKey="-1" headerValue="Select"
								listKey="key" listValue="value" theme="simple"
								id="irrigationSource"
								onchange="irrigationOtherValue(this.value);"
								cssClass="form-control" />
						</div>
					</div>

					<div class="flexform-item" id="otherValueDiv">
						<label for="txt"> <s:text name="farm.irrigatedOther" />
						</label>
						<div class="form-element">
							<s:textfield name="farm.farmDetailedInfo.irrigatedOther"
								id="otherValue" theme="simple" maxlength="35"
								cssClass="form-control input-sm" />

						</div>
					</div>
					<s:if test="currentTenantId=='welspun'">
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.NoOfCrotenTrees"
									cssClass="form-control " theme="simple"
									onkeypress="return isDecimal(event)" maxlength="10" />
							</div>
						</div>

						<div class="flexform-item tree">
							<label for="txt"><s:property
									value="%{getLocaleProperty('treeName')}" /></label>
							<div class="form-element">
								<s:textfield name="farm.treeName" cssClass="form-control "
									theme="simple" onkeypress="return isDecimal(event)"
									maxlength="10" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.distanceProcessingUnit"
									cssClass="form-control " theme="simple"
									onkeypress="return isDecimal(event)" maxlength="10" />
							</div>
						</div>
						<div class="flexform-item avgStore">
							<label for="txt"><s:property
									value="%{getLocaleProperty('avgStore')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.avgStore" cssClass="form-control "
									theme="simple" onkeypress="return isDecimal(event)"
									maxlength="10" />
							</div>
						</div>


					</s:if>


					<%-- 
					<div class="flexform-item" id="irrigationTypeLabel">
						<label for="txt"><s:text name="farm.farmIrrigation" /> </label>
						<div class="form-element" id="irrigationType">
							<s:select name="selectedIrrigationSource"
								list="irrigationSourceList" headerKey="-1" headerValue="Select"
								listKey="key" listValue="value" theme="simple"
								id="irrigationSource"
								onchange="irrigationOtherValue(this.value);"
								cssClass="form-control " />
						</div>
					</div>
					 --%>



					<div class="flexform-item">
						<label for="txt"><s:text name="farm.methodOfIrrigation" />
						</label>
						<div class="form-element">
							<s:select name="selectedMethodOfIrrigation"
								list="methodOfIrrigationList" listKey="key" listValue="value"
								theme="simple" id="methodIrrigation"
								cssClass="form-control select2" multiple="true" />


						</div>
					</div>
					<s:if test="currentTenantId!='welspun'">
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.boreWellRechargeStructure')}" />
							</label>
							<div class="form-element">
								<s:radio list="borewellList" listKey="key" listValue="value"
									name="farm.farmDetailedInfo.boreWellRechargeStructure"
									theme="simple" />
							</div>
						</div>
					</s:if>
					<s:if test="currentTenantId=='cofBoard'">
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('presenceOfBananaTrees')}" /> </label>
							<div class="form-element">
								<s:radio list="bananaTreesList" listKey="key" listValue="value"
									name="farm.presenceBananaTrees" theme="simple" />
							</div>
						</div>
						</s:if>
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('activitiesInCoconutFarming')}" /></label>
						<div class="form-element">
							<s:textfield
								name="farm.farmDetailedInfo.activitiesInCoconutFarming"
								id="activitiesInCoconut" theme="simple" maxlength="35"
								cssClass="form-control input-sm" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm.inputSource')}" /> </label>
						<div class="form-element">
							<s:select list="inputSourceList" listKey="key" listValue="value"
								headerKey="-1" headerValue="%{getText('txt.select')}"
								cssClass="form-control select2"
								name="farm.farmDetailedInfo.inputSource" theme="simple"
								id="methodOfInputSource" />
						</div>
					</div>
						

					<s:if test="currentTenantId=='olivado'">

						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('8A/7/12')}" /> <sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:radio list="processingActList" listKey="key"
									listValue="value"
									name="farm.farmDetailedInfo.processingActivity" theme="simple" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" /> <sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fullTimeWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('presenceOfBananaTrees')}" /><sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:radio list="bananaTreesList" listKey="key" listValue="value"
									name="farm.presenceBananaTrees" theme="simple" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.partTimeWorkersCount')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.partTimeWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.seasonalWorkersCount')}" /> </label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.seasonalWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.parallelProduction')}" /> </label>
							<div class="form-element">
								<s:radio list="parallelProductionList" listKey="key"
									listValue="value" name="farm.parallelProd" theme="simple" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" /></label>
							<div class="form-element">
								<s:textfield data-date-format="mm/yyyy"
									data-date-viewmode="months"
									cssClass="date-picker form-control input-sm" id="calendarDOC"
									name="farm.farmDetailedInfo.lastDateOfChemicalApplication"
									size="23" readonly="true" />

							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.lastDateofOrganicUnit')}" /></label>
							<div class="form-element">
								<s:textfield data-date-format="mm/yyyy"
									data-date-viewmode="months"
									cssClass="date-picker form-control input-sm"
									id="calendarFormat" name="farm.inputOrganicUnit" size="23"
									readonly="true" />

							</div>
						</div>
					</s:if>

				</div>
			</div>
		</div>
</s:if>
		<s:if test="currentTenantId=='olivado'">
			<div class="appContentWrapper marginBottom auditInfo">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#auditDetailsAccordian" class="accrodianTxt"> <s:text
								name="info.auditDetails" />
						</a>
					</h2>
				</div>
				<div id="auditDetailsAccordian" class="panel-collapse collapse in">
					<div class="flexform">
						<table class="table">
							<tbody>
								<tr>
									<td><s:property
											value="%{getLocaleProperty('farm.presenceHiredLabour')}" /><sup
										style="color: red;">*</sup></td>
					<%-- 				<td><s:property
											value="%{getLocaleProperty('farm.riskCategory')}" /><sup
										style="color: red;">*</sup></td> --%>
									<td><s:property
											value="%{getLocaleProperty('dateOfInternalAudit')}" /></td>
									<%-- <td><s:property
											value="%{getLocaleProperty('nameOfInspector')}" /></td> --%>
									<td><s:property
											value="%{getLocaleProperty('producerStatus')}" /></td> 
									<%-- <td><s:property value="%{getLocaleProperty('status')}" /></td> --%>

								</tr>
								<tr>
									<td><s:radio list="hiredLabourList" listKey="key"
											listValue="value" name="farm.presenceHiredLabour"
											theme="simple" /></td>
									<%-- <td><s:radio list="riskCategoryList" listKey="key"
											listValue="value" name="farm.riskCategory" theme="simple" /></td> --%>

									<td><s:textfield
											name="farm.farmIcsConv.inspectionDateString"
											id="calendarInsp" cssClass="form-control " readonly="true" /></td>
				<%-- 					<td><s:textfield name="farm.farmIcsConv.inspectorName"
											cssClass="form-control " /></td> --%>
									<td><s:select cssClass="form-control " id="certType"
											name="farm.farmIcsConv.scope" list="certList" headerKey=""
											headerValue="%{getText('txt.select')}" /></td>

									<%-- <td><s:select cssClass="form-control " id="icsType"
											name="farm.farmIcsConv.icsType" list="icsStatusList"
											headerKey="" headerValue="%{getText('txt.select')}" /></td> --%>

								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</s:if>

		<s:if test="currentTenantId!='olivado' && currentTenantId!='griffith' && currentTenantId!='susagri'">
			<div class="appContentWrapper marginBottom farmLabourInfo">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#farmLabourAccordian" class="accrodianTxt"> <s:text
								name="info.labour" />
						</a>
					</h2>
				</div>
				<div id="farmLabourAccordian" class="panel-collapse collapse in">
					<div class="flexform">
						<div class="flexform-item">
							<label for="txt"><s:property
								value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" /><s:if
								test="currentTenantId =='livelihood' || currentTenantId =='farmAgg'">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fullTimeWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
								value="%{getLocaleProperty('farm.partTimeWorkersCount')}" /><s:if
								test="currentTenantId =='farmAgg'">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.partTimeWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
								value="%{getLocaleProperty('farm.seasonalWorkersCount')}" /><s:if
								test="currentTenantId =='livelihood'  || currentTenantId =='farmAgg'">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.seasonalWorkersCount"
									theme="simple" maxlength="4" cssClass="form-control " onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>
		<s:if test="currentTenantId!='olivado' && currentTenantId!='griffith' && currentTenantId!='farmAgg'">
			<div class="appContentWrapper marginBottom conversionInfo">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#farmConversionAccordian" class="accrodianTxt"> <s:property
								value="%{getLocaleProperty('info.conversion')}" />
						</a>
					</h2>
				</div>





				<div id="farmConversionAccordian" class="panel-collapse collapse in">
					<div class="flexform">
						<div class="flexform-item">
							<label for="txt"><s:property value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" /><s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if> </label>
							<div class="form-element">
								<s:textfield data-date-format="mm/yyyy"
									data-date-viewmode="months"
									cssClass="date-picker form-control input-sm" id="calendarDOC"
									name="farm.farmDetailedInfo.lastDateOfChemicalApplication"
									size="23" readonly="true" />



							</div>
						</div>
						<s:if test="currentTenantId=='welspun'">
							<div class="flexform-item">
								<label for="txt"> <%-- <s:text name="farm.landOwned" /> --%>
									<s:property value="%{getLocaleProperty('farm.landOwned')}" />
								</label>
								<div class="form-element">
									<s:select list="farmOwnedList" headerKey="-1"
										headerValue="%{getText('txt.select')}" listKey="key"
										listValue="value" cssClass="form-control  select2"
										name="selectedFarmOwned" theme="simple" id="farmOwned"
										onchange="showFarmOther(this.value)" />
								</div>
							</div>
							<div class="flexform-item farmOtherDiv">
								<label for="txt"> <s:property
										value="%{getLocaleProperty('other')}" />
								</label>
								<div class="form-element">
									<s:textfield id="farmOther" name="farm.farmOther"
										cssClass="form-control " theme="simple" />
								</div>
							</div>
							<div class="flexform-item">
								<label for="txt"> <s:property
										value="%{getLocaleProperty('farm.waterSourceList')}" />
								</label>
								<div class="form-element">
									<s:select name="farm.waterSource" list="waterSourceList"
										headerKey="-1" headerValue="%{getText('txt.select')}"
										listKey="key" listValue="value" theme="simple"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.conventionalLands')}" /><s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.conventionalLand"
									theme="simple" id="conventionalLand" maxlength="12"
									cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.fallowLand')}" /><s:if
								test="currentTenantId =='livelihood'">
								<sup style="color: red;">*</sup>
							</s:if>
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fallowOrPastureLand"
									theme="simple" id="pastureLand" maxlength="12"
									cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.conventionalCrops')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.conventionalCrops"
									theme="simple" id="conventionalCrops" maxlength="12"
									cssClass="form-control " />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.conventionalEstimatedYields')}" />
							</label>
							<div class="form-element">
								<s:textfield
									name="farm.farmDetailedInfo.conventionalEstimatedYield"
									theme="simple" id="estimatedYield" maxlength="12"
									cssClass="form-control " />
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>
		<s:if test="currentTenantId!='olivado'">
			<div class="appContentWrapper marginBottom fieldHistoryInfo">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#fieldInfoAccordian" class="accrodianTxt"> <s:property
								value="%{getLocaleProperty('info.conversions')}" />
						</a>
					</h2>
				</div>


				<div id="fieldInfoAccordian" class="panel-collapse collapse in">
					<div class="flexform">

						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.fieldName')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fieldName"
									theme="simple" id="fieldName" maxlength="45"
									cssClass="form-control "
									onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>

						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.fieldCrop')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fieldCrop"
									theme="simple" id="fieldCrop" maxlength="12"
									cssClass="form-control "
									onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.area')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.fieldArea"
									theme="simple" id="fieldArea" maxlength="12"
									cssClass="form-control" onkeypress="return isDecimal(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.inputApplied')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.inputApplied"
									theme="simple" id="inputApplied" maxlength="150"
									cssClass="form-control "
									onkeypress="return isAlpahaNumeric(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.quantity')}" />
							</label>
							<div class="form-element">
								<s:textfield name="farm.farmDetailedInfo.quantityApplied"
									theme="simple" id="quantity" maxlength="12"
									cssClass="form-control " onkeypress="return isDecimal(event)" />
							</div>
						</div>
						<div class="flexform-item">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" /></label>
							<div class="form-element">

								<s:textfield
									name="farm.farmDetailedInfo.lastDateOfChemicalString"
									id="calendarLastDateChemical" readonly="true" theme="simple"
									cssClass="date-picker form-control" />

							</div>
						</div>
					</div>
				</div>
			</div>
		</s:if>

		<s:if test="currentTenantId!='olivado' && currentTenantId!='griffith' && currentTenantId!='livelihood' && currentTenantId!='farmAgg' ">
			<div class="appContentWrapper marginBottom conversionStatus">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#farmICSAccordian" class="accrodianTxt"> <s:property
								value="%{getLocaleProperty('info.ics')}" />
						</a>
					</h2>
				</div>
				<div id="farmICSAccordian" class="panel-collapse collapse in">
					<div class="flexform">
						<table class="table">
							<tbody>
								<tr>
								<s:if test="currentTenantId=='susagri'">
										<td><s:property value="%{getLocaleProperty('inspType')}" /><sup style="color: red;">*</sup></td>
									</s:if>
									<s:if test="currentTenantId!='pratibha'">
										<td><s:property value="%{getLocaleProperty('certType')}" /><sup style="color: red;">*</sup></td>
									</s:if>
									<s:if test="currentTenantId!='welspun'">
										<td><s:text name="%{getLocaleProperty('conversionStatus')}" /><sup
											style="color: red;">*</sup></td>
									</s:if>
									<td><s:text name="inspectionDate" /> <s:if
											test="currentTenantId=='iccoa'">
											<sup style="color: red;">*</sup>
										</s:if></td>
									<td><s:text name="nameOfInspector" /></td>
									<td><s:text name="%{getLocaleProperty('qualified')}" /></td>
									<td class="qualNo hide"><s:text name="sanctionReason" /></td>
									<td class="qualNo hide"><s:text name="durationOfSanction" /></td>
								</tr>
								<tr>
									<s:if test="currentTenantId=='susagri'">
									<td><s:select cssClass="form-control " id="inspType"
												name="inspType" list="inspTypeList" headerKey=""
												headerValue="%{getText('txt.select')}" /></td>
									</s:if>
									<s:if test="currentTenantId!='pratibha'">
										<td><s:select cssClass="form-control " id="certType"
												name="farm.farmIcsConv.scope" list="certList" headerKey=""
												headerValue="%{getText('txt.select')}" /></td>
									</s:if>
									
									<s:if test="currentTenantId!='welspun'">
										<td><s:select cssClass="form-control " id="icsType"
												name="farm.farmIcsConv.icsType" list="icsStatusList"
												headerKey="" headerValue="%{getText('txt.select')}" /></td>
									</s:if>

									<td><s:textfield
											name="farm.farmIcsConv.inspectionDateString"
											id="calendarInsp" cssClass="form-control " readonly="true" /></td>
									<td><s:textfield name="farm.farmIcsConv.inspectorName"
											cssClass="form-control " /></td>
									<td><s:radio id="sTesing" list="qualifiedTestMap"
											name="farm.farmIcsConv.qualified" listKey="key"
											onchange="setQualified(this.value)" listValue="value"
											theme="simple" /></td>
									<td class="qualNo hide"><s:textfield
											name="farm.farmIcsConv.sanctionreason"
											cssClass="form-control " /></td>
									<td class="qualNo hide"><s:textfield
											name="farm.farmIcsConv.sanctionDuration"
											cssClass="form-control " /></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</s:if>
		<s:if test="currentTenantId!='griffith'">
		<div class="appContentWrapper marginBottom gpsInfo">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#farmGPSAccordian" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('gps.prop')}" />
					</a>
				</h2>
			</div>
			<div id="farmGPSAccordian" class="panel-collapse collapse in">
				<div class="flexform">
					<table class="table">
						<tbod>
						<tr>
							<td style="width: 17%;"><div class="col-xs-16">
									<s:text name="farm.latitude" />
								</div></td>
							<td style="width: 17%;"><div class="col-xs-16">
									<s:textfield name="farm.latitude" maxlength="20"
										cssClass="form-control " />
								</div></td>
							<td style="width: 17%;"><div class="col-xs-16">
									<s:text name="farm.longitude" />
								</div></td>
							<td style="width: 17%;"><div class="col-xs-16">
									<s:textfield name="farm.longitude" maxlength="20"
										cssClass="form-control " />
								</div></td>
							<s:if test="currentTenantId!='efk' && currentTenantId!='gsma' && currentTenantId!='ecoagri' ">
								<td style="width: 17%;"><div class="col-xs-16">
										<s:if test="currentTenantId!='olivado' ">
											<s:property value="%{getLocaleProperty('farm.landmark')}" />
										</s:if>
										<s:else>
											<s:property value="%{getLocaleProperty('farm.leasedLand')}" />
										</s:else>
									</div></td>
								<td style="width: 17%;"><div class="col-xs-16">

										<s:if test="currentTenantId!='olivado'">
											<s:textfield name="farm.landmark" maxlength="100"
												cssClass="form-control " />
										</s:if>
										<s:else>
											<s:textfield id="frmLease" name="farm.leasedLand"
												maxlength="100" cssClass="form-control " />
										</s:else>
									</div></td>
							</s:if>
						</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
</s:if>
		<%-- 	<s:if test="currentTenantId=='olivado'">
			<div class="appContentWrapper marginBottom treeDetailsInfo">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#treeDetailsAccordian" class="accrodianTxt"> <s:text
								name="info.treeDetails" />
						</a>
					</h2>
				</div>
				   <div class="flexform-item totalOrganic">
							<label for="txt"> <s:text name="cropType" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control" id="surveyNo"
									name="farm.treeName" list="cropTypeList" headerKey="-1"
									headerValue="%{getText('txt.select')}" />
							</div>
					</div>

				<div id="treeDetailsAccordian" class="panel-collapse collapse in">
					<div class="flexform">

							<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('variety')}" /> <sup
								style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="farmVarietyMaster"
									name="selectedVariety" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listOfProcurementVariety" />
							</div>
						</div>
						<div class="flexform-item"  style="width: 490px;">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.years')}" /> </label>
							<div class="form-element">
								<s:select list="listYears" listKey="key" listValue="value"
									cssClass="form-control select2" headerKey="-1"
									headerValue="%{getText('txt.select')}" name="farm.treeDetail.years"
									theme="simple" id="years" />
							</div>
						</div>
						<div class="flexform-item"  style="width: 455px;">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.noOfTrees')}" /><sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:textfield name="farm.treeDetail.noOfTrees" cssClass="form-control "
									theme="simple" onkeypress="return isNumber(event)"
									maxlength="10" onchange="showTotalOrganicFuerte(this.value)" />
							</div>
						</div>
						<div class="pull-right">
							<input type="button" value="<s:text name="add.button"/>"
								id="addBankDetail"
								class="addBankInfo slide_open secondaryBtn btn-success"
								data-toggle="modal" data-target="#slide" data-backdrop="static"
								onclick="addTrees" data-keyboard="false"> <input
								type="button" style="display: none" id="editBankDetailModal"
								class="addBankInfo slide_open" data-toggle="modal"
								data-target="#slide" data-backdrop="static"
								data-keyboard="false">
						</div>
						<table id="tblData1" class="table table-bordered aspect-detail"
							style="margin-top: 2%">
							<thead>
								<tr>
									<th><s:text name="prodStatus"></s:text></th>
									<th><s:text name="cultivar"></s:text></th>
									<th><s:text name="years" /></th>
									<th><s:text name="noOfTrees" /></th>
									<th><s:text name="action" /></th>
								</tr>
							</thead>
							<tbody id="treeDataDetail">
								<tr>

									<td><s:select cssClass="tableTd27 form-control input-sm"
											id="prodStatus" name="farm.treeDetail.prodStatus"
											headerKey="" headerValue="%{getText('txt.select')}"
											listKey="key" listValue="value" list="cropTypeList" /></td>
									<td><s:select cssClass="tableTd28 form-control input-sm"
											id="farmVarietyMaster" name="selectedVariety" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" list="listOfProcurementVariety" /></td>
									<td><s:select cssClass="tableTd29 form-control input-sm"
											id="years" name="farm.treeDetail.years" headerKey=""
											headerValue="%{getText('txt.select')}" listKey="key"
											listValue="value" list="listYears" /></td>
									<td><s:textfield class="tableTd30 form-control input-sm"
											name="farm.treeDetail.noOfTrees" maxlength="5"
											onkeypress="return isNumber(event)" id="noOfTrees" /></td>
									<td>
										<button type="button" id="addTreeDetail"
											class="addTreeInfo slide_open btn btn-sts"
											onclick="saveTreeDetails(this);">
											<i class="fa fa-plus" aria-hidden="true"></i>
										</button>
									</td>
								</tr>



							</tbody>
						</table>
						<table id="tableData" class="table table-bordered aspect-detail"
							style="margin-top: 2%">
							<thead>
								<tr>
									<th><s:text name="prodStatus"></s:text></th>
									<th><s:text name="cultivar"></s:text></th>
									<th><s:text name="years" /></th>
									<th><s:text name="noOfTrees" /></th>
									<th><s:text name="action" /></th>
								</tr>
					
							</thead>
							<tbody id="treeData">
							<s:if test="farm.treeDetails.size()>0">
								<s:iterator value="farm.treeDetails" status="status">

									<tr class="tableTr1"
										id="<s:property value="%{#status.index+1}"/>">
										<td class=catalogueType width="35%"><s:property
												value="catalogueType" /></td>
		
		
										<td class="count" width="35%"><s:property value="count" /></td>
		

										<td><s:property value="prodStatus" />
										<td><s:property value="variety" />
										<td><s:property value="years" />
										<td><s:property value="noOfTrees" />
										<td>
											<button type="button" class="fa fa-trash"
												onclick="butTreeDelete(<s:property value="%{#status.index+1}"/>)" />
										</td>
										
										<td class="hide"><s:property value="prodStatusId" /></td>
												<td class="hide"><s:property value="varietyId" /></td>
												<td class="hide"><s:property value="yearsId" /></td>

									</tr>

								</s:iterator>
							</s:if>	
							</tbody>
						</table>
										<table id="tableTemplate"
							class="table table-bordered aspect-detail" style="margin-top: 2%">
							<thead id="tableHD">
								<tr class="border-less">
								  <th><s:text name="prodStatus"></s:text></th>
									<th><s:text name="variety"></s:text></th>
									<th><s:text name="years" /></th>
									<th><s:text name="noOfTrees" /></th>
									<th><s:text name="delete"></s:text></th>
								</tr>
							</thead>
							<tbody id="tBodyTemplate">
							</tbody>
						</table>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicFuerte')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicFuerte" name="farm.totalOrganicFuerte" />
							</div>
						</div>


						<div id="totalOrgDiv" class="flexform-item totalOrganic">

</div> 
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicHass')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicHass" name="farm.totalOrganicHass"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicAvocadoTrees"
									name="farm.totalOrganicAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalFuerte')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalFuerte"
									name="farm.totalConventionalFuerte"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalHass')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalHass"
									name="farm.totalConventionalHass"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalAvocadoTrees"
									name="farm.totalConventionalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalAvocadoTrees" name="farm.totalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hecOrganicAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hecOrganicAvocadoTrees"
									name="farm.hecOrganicAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hecConventionalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hecConventionalAvocadoTrees"
									name="farm.hecConventionalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hectarageAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hectarageAvocadoTrees"
									name="farm.hectarageAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>

					</div>
				</div>
			</div>
		</s:if>
 --%>

		<div class="appContentWrapper marginBottom treeDetailsInfo">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#treeDetailsAccordian" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.treeDetails')}" />

					</a>
				</h2>
			</div>
			<%--    <div class="flexform-item totalOrganic">
							<label for="txt"> <s:text name="cropType" />
							</label>
							<div class="form-element">
								<s:select cssClass="form-control" id="surveyNo"
									name="farm.treeName" list="cropTypeList" headerKey="-1"
									headerValue="%{getText('txt.select')}" />
							</div>
					</div> --%>

			<div id="treeDetailsAccordian" class="panel-collapse collapse in">
				<div class="flexform">

					<%-- 	<div class="flexform-item">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('variety')}" /> <sup
								style="color: red;">*</sup>
							</label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="farmVarietyMaster"
									name="selectedVariety" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listOfProcurementVariety" />
							</div>
						</div>
						<div class="flexform-item"  style="width: 490px;">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.years')}" /> </label>
							<div class="form-element">
								<s:select list="listYears" listKey="key" listValue="value"
									cssClass="form-control select2" headerKey="-1"
									headerValue="%{getText('txt.select')}" name="farm.treeDetail.years"
									theme="simple" id="years" />
							</div>
						</div>
						<div class="flexform-item"  style="width: 455px;">
							<label for="txt"><s:property
									value="%{getLocaleProperty('farm.noOfTrees')}" /><sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:textfield name="farm.treeDetail.noOfTrees" cssClass="form-control "
									theme="simple" onkeypress="return isNumber(event)"
									maxlength="10" onchange="showTotalOrganicFuerte(this.value)" />
							</div>
						</div>
						<div class="pull-right">
							<input type="button" value="<s:text name="add.button"/>"
								id="addBankDetail"
								class="addBankInfo slide_open secondaryBtn btn-success"
								data-toggle="modal" data-target="#slide" data-backdrop="static"
								onclick="addTrees" data-keyboard="false"> <input
								type="button" style="display: none" id="editBankDetailModal"
								class="addBankInfo slide_open" data-toggle="modal"
								data-target="#slide" data-backdrop="static"
								data-keyboard="false">
						</div> --%>
					<table id="tblData1" class="table table-bordered aspect-detail"
						style="margin-top: 2%">
						<thead>
							<tr>
								<th><s:text name="prodStatus"></s:text></th>
								<th><s:text name="cultivar"></s:text></th>
								<th><s:text name="years" /></th>
								<th><s:text name="noOfTrees" /></th>
								<th><s:text name="action" /></th>
							</tr>
						</thead>
						<tbody id="treeDataDetail">
							<tr>

								<td><s:select cssClass="tableTd27 form-control input-sm"
										id="prodStatus" name="farm.treeDetail.prodStatus" headerKey=""
										headerValue="%{getText('txt.select')}" listKey="key"
										listValue="value" list="cropTypeList" /></td>
								<td><s:select cssClass="tableTd28 form-control input-sm"
										id="farmVarietyMaster" name="selectedVariety" headerKey=""
										headerValue="%{getText('txt.select')}" listKey="key"
										listValue="value" list="listOfProcurementVariety" /></td>
								<td><s:select cssClass="tableTd29 form-control input-sm"
										id="years" name="farm.treeDetail.years" headerKey=""
										headerValue="%{getText('txt.select')}" listKey="key"
										listValue="value" list="listYears" /></td>
								<td><s:textfield class="tableTd30 form-control input-sm"
										name="farm.treeDetail.noOfTrees" maxlength="5"
										onkeypress="return isNumber(event)" id="noOfTrees" /></td>
								<td>
									<button type="button" id="addTreeDetail"
										class="addTreeInfo slide_open btn btn-sts"
										onclick="saveTreeDetails(this);">
										<i class="fa fa-plus" aria-hidden="true"></i>
									</button>
								</td>
							</tr>



						</tbody>
					</table>
					<table id="tableData" class="table table-bordered aspect-detail"
						style="margin-top: 2%">
						<thead>
							<tr>
								<th><s:text name="prodStatus"></s:text></th>
								<th><s:text name="cultivar"></s:text></th>
								<th><s:text name="years" /></th>
								<th><s:text name="noOfTrees" /></th>
								<th><s:text name="action" /></th>
							</tr>

						</thead>
						<tbody id="treeData">
							<s:if test="farm.treeDetails.size()>0">
								<s:iterator value="farm.treeDetails" status="status">

									<tr class="tableTr1"
										id="<s:property value="%{#status.index+1}"/>">
										<%-- <td class=catalogueType width="35%"><s:property
												value="catalogueType" /></td>
		
		
										<td class="count" width="35%"><s:property value="count" /></td>
		 --%>

										<td><s:property value="prodStatus" />
										<td><s:property value="variety" />
										<td><s:property value="years" />
										<td><s:property value="noOfTrees" />
										<td>
											<button type="button" class="fa fa-trash"
												onclick="butTreeDelete(<s:property value="%{#status.index+1}"/>)" />
										</td>

										<td class="hide"><s:property value="prodStatusId" /></td>
										<td class="hide"><s:property value="varietyId" /></td>
										<td class="hide"><s:property value="yearsId" /></td>

									</tr>

								</s:iterator>
							</s:if>
						</tbody>
					</table>
					<%-- 				<table id="tableTemplate"
							class="table table-bordered aspect-detail" style="margin-top: 2%">
							<thead id="tableHD">
								<tr class="border-less">
								  <th><s:text name="prodStatus"></s:text></th>
									<th><s:text name="variety"></s:text></th>
									<th><s:text name="years" /></th>
									<th><s:text name="noOfTrees" /></th>
									<th><s:text name="delete"></s:text></th>
								</tr>
							</thead>
							<tbody id="tBodyTemplate">
							</tbody>
						</table> --%>
					<%-- <div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicFuerte')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicFuerte" name="farm.totalOrganicFuerte" />
							</div>
						</div>
 --%>

					<%-- <div id="totalOrgDiv" class="flexform-item totalOrganic">

</div> 
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicHass')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicHass" name="farm.totalOrganicHass"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalOrganicAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalOrganicAvocadoTrees"
									name="farm.totalOrganicAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalFuerte')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalFuerte"
									name="farm.totalConventionalFuerte"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalHass')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalHass"
									name="farm.totalConventionalHass"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalConventionalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalConventionalAvocadoTrees"
									name="farm.totalConventionalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.totalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="totalAvocadoTrees" name="farm.totalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hecOrganicAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hecOrganicAvocadoTrees"
									name="farm.hecOrganicAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hecConventionalAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hecConventionalAvocadoTrees"
									name="farm.hecConventionalAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div>
						<div class="flexform-item totalOrganic">
							<label for="txt"> <s:property
									value="%{getLocaleProperty('farm.hectarageAvocadoTrees')}" />
							</label>
							<div class="form-element">
								<span id="hectarageAvocadoTrees"
									name="farm.hectarageAvocadoTrees"
									style="display: block; text-align: center" />

							</div>
						</div> --%>

				</div>
			</div>
		</div>

		<div class="appContentWrapper marginBottom farmSysInfo ">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#integratedFarmingSystems" class="accrodianTxt"> <s:text
							name="info.integratedFarmingSystems" />
					</a>
				</h2>
			</div>
			<div id="integratedFarmingSystems" class="panel-collapse collapse in">
				<div class="flexform">
					<div class="flexform-item">
						<label for="txt"><s:text name="farm.ifs" /> </label>
						<div class="form-element">
							<s:select id="ifs" name="farm.ifs" listKey="key"
								listValue="value" list="ifsList"
								onChange="processIfs(this.value);"
								cssClass="form-control  select2Multi" multiple="true" />

							<s:textfield id="ifsOtherVal" name="farm.ifsOther"
								cssClass="form-control ifsOther" maxlength="30" tabindex="11"
								cssStyle="margin-top:0.9%" />
							<script type="text/javascript">									
			   processIfs(jQuery("#ifs").val());									
			   		
			</script>
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.vegetableName" /> </label>
						<div class="form-element">
							<s:textarea name="farm.vegetableName" theme="simple"
								maxlength="255" cssStyle="height:70px" cssClass="form-control " />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.kitchenGarden" /> </label>
						<div class="form-element">
							<s:textarea name="farm.kitchenGarden" theme="simple"
								maxlength="255" cssStyle="height:70px" cssClass="form-control " />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.backYardPoultry" /> </label>
						<div class="form-element">
							<s:textarea name="farm.backYardPoultry" theme="simple"
								maxlength="255" cssStyle="height:70px" cssClass="form-control " />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.soilConservation" />
						</label>
						<div class="form-element">
							<s:select id="soilConservation" name="farm.soilConservation"
								listKey="key" listValue="value" list="soilConservationList"
								cssClass="form-control  select2Multi" multiple="true"
								onChange="processSoilConservation(this.value);" tabindex="10" />
							<s:textfield id="soilConservationOtherVal"
								name="farm.soilConservationOther"
								cssClass="form-control  soilConservationOther" maxlength="30"
								tabindex="11" cssStyle="margin-top:0.9%" />
							<script type="text/javascript">									
			   processSoilConservation(jQuery("#soilConservation").val());									
			   		
			</script>
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.waterConservation" />
						</label>
						<div class="form-element flexdisplay">
							<s:select id="waterConservation" name="farm.waterConservation"
								listKey="key" listValue="value" list="waterConservationList"
								cssClass="form-control  select2Multi" multiple="true"
								onChange="processWaterConservation(this.value);" tabindex="10" />
							<s:textfield id="waterConservationOtherVal"
								name="farm.waterConservationOther"
								cssClass="form-control  waterConservationOther" maxlength="30"
								tabindex="11" cssStyle="margin-top:0.9%" />
							<script type="text/javascript">									
			   processWaterConservation(jQuery("#waterConservation").val());									
			   		
			</script>
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farm.serviceCentres" /> </label>
						<div class="form-element">
							<s:select id="serviceCentres" name="farm.serviceCentres"
								listKey="key" listValue="value" list="serviceCentresList"
								cssClass="form-control  select2Multi" multiple="true"
								onChange="processServiceCentres(this.value);" tabindex="10" />
							<s:textfield id="serviceCentresOtherVal"
								name="farm.serviceCentresOther"
								cssClass="form-control  serviceCentresOther" maxlength="30"
								tabindex="11" cssStyle="margin-top:0.9%" />
							<script type="text/javascript">									
			   processServiceCentres(jQuery("#serviceCentres").val());									
			 
			</script>
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farm.ffs" /> </label>
						<div class="form-element">
							<s:select cssClass="form-control " id="ffschool" headerKey="-1"
								headerValue="%{getText('txt.select')}"
								name="farm.farmDetailedInfo.farmerFieldSchool" list="ffsList"
								listKey="key" listValue="value" onchange="ffsSchool(this);" />
						</div>
					</div>

					<div class="flexform-item">
						<label for="txt"><s:text name="farm.benifit" /> </label>
						<div class="form-element isFfsbene">
							<s:textfield id="isFfs"
								name="farm.farmDetailedInfo.isFFSBenifited"
								cssClass="form-control " theme="simple" />
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farm.trainingProgram" /> </label>
						<div class="form-element">
							<s:select id="trainingProgram" name="farm.trainingProgram"
								listKey="key" listValue="value" list="trainingProgramList"
								cssClass="form-control  select2Multi" multiple="true"
								onChange="processTrainingProgram(this.value);" tabindex="10" />
							<s:textfield id="trainingProgramOtherVal"
								name="farm.trainingProgramOther"
								cssClass="form-control  trainingProgramOther" maxlength="30"
								tabindex="11" cssStyle="margin-top:0.9%" />
							<script type="text/javascript">									
			   processTrainingProgram(jQuery("#trainingProgram").val());									
			   		
			</script>
						</div>
					</div>


					<div class="flexform-item">
						<label for="txt"><s:text name="farm.cultivatedMillet" />
						</label>
						<div class="form-element">
							<s:radio list="milletList" listKey="key" listValue="value"
								name="farm.farmDetailedInfo.milletCultivated"
								onchange="millestcultivation(this);" theme="simple" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"><s:text name="farm.milletCrop" /> </label>
						<div class="form-element milletCrop">
							<s:textfield name="farm.farmDetailedInfo.milletCropType"
								cssClass="form-control " theme="simple" />
						</div>
					</div>
					<div class="flexform-item">
						<label for="txt"><s:text name="farm.milletCount" /> </label>
						<div class="form-element milletCount">
							<s:textfield name="farm.farmDetailedInfo.milletCropCount"
								maxlength="5" cssClass="form-control " theme="simple" />
						</div>
					</div>

				</div>
			</div>
		</div>
		<%--  <div class="appContentWrapper marginBottom soilTesting">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#soilTestAccordian" class="accrodianTxt"> <s:property
							value="%{getLocaleProperty('info.soil')}" />
					</a>
				</h2>
			</div>
			<div id="soilTestAccordian" class="panel-collapse collapse in">
				<div class="flexform">
					<jsp:include page="soilTesting.jsp"></jsp:include>
				</div>
			</div>
		</div> --%>


		<%--<div class="appContentWrapper marginBottom benefit">
			<div class="formContainerWrapper">
				<h2>
					<a data-toggle="collapse" data-parent="#accordion"
						href="#benefitDetail" class="accrodianTxt"> 
						<s:text name="info.benefit" />
					</a>
				</h2>
			</div>
			<div id="benefitDetail" class="panel-collapse collapse in">
				<div class="flexform">
					<jsp:include page="farmerBenefitDetails.jsp">
						<jsp:param name="sangham" value="${sangham}" />
						<jsp:param name="productsInfoArray" value="${productsInfoArray}" />
					</jsp:include>
				</div>
			</div>
		</div> --%>


		<s:if test='enableSoliTesting=="1"'>
			<div class="appContentWrapper marginBottom soilTesting">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#uploadSoilFile" class="accrodianTxt"> <s:text
								name="info.soliUp" />
						</a>
					</h2>
				</div>

				<div id="uploadSoilFile" class="panel-collapse collapse in">

					<div class="flexform">
						<table class="table">
							<tbody>
								<tr>
									<div class="flexform-item">
										<td style="width: 17%;"><label for="txt"><font
												color="black"><s:text name="soliTesting" /></font></label></td>
										<td style="width: 17%;"><s:radio id="sTesing"
												list="soilList" name="farm.soilTesting" listKey="key"
												listValue="value" onchange="soilTesting(this);"
												theme="simple" /></td>

										<td><div id="wDoc">
												<s:file name="farm.docUploadList[0].docFile" id="wDocsave1" />
												<s:fielderror fieldName="farm.docUploadList[0].docFile" />
												<s:text name="farm.fileTypes" />
												<font color="red" style="font-size: 10px"> <s:text
														name="fileSizeMsg" /></font> <span id="lblError"
													style="color: red;"></span>
											</div></td>
									</div>
								</tr>
							</tbody>
						</table>
					</div>

				</div>
			</div>
		</s:if>
		<s:if test="command =='create'">
			<div class="appContentWrapper marginBottom crop_info">
				<div class="formContainerWrapper">
					<h2>
						<a data-toggle="collapse" data-parent="#accordion"
							href="#cropAccordian" class="accrodianTxt"> <s:text
								name="info.procurementProducts" />
						</a>
					</h2>
				</div>

				<div id="cropAccordian" class="panel-collapse collapse in">
					<div class="flexform">
						<div class="flexform-item">
							<label for="txt"><s:text name="crop" /></label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="farmCropsMasters"
									name="selectedCrop" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listProcurementProduct"
									onchange="listVariety(this)" />
							</div>
						</div>
						<div class="flexform-item farmVarietyMaster">
							<label for="txt"><s:text name="variety" /><sup
								style="color: red;">*</sup> </label>
							<div class="form-element">
								<s:select cssClass="form-control select2" id="farmVarietyMaster"
									name="selectedVariety" headerKey="-1"
									headerValue="%{getText('txt.select')}" listKey="key"
									listValue="value" list="listProcurementVarietyMap" />
							</div>
						</div>

					</div>
				</div>

			</div>
		</s:if>
		<div class="clear"></div>
		<div class="dynamicFieldsRender"></div>


		<div class="">
			<div class="flexItem flex-layout flexItemStyle">
				<div class="button-group-container">
					<s:if test="command =='create'">
						<button type="button" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
							</font>
						</button>
					</s:if>
					<s:else>
						<button type="button" onclick="onSubmit();"
							class="save-btn btn btn-success">
							<font color="#FFFFFF"> <b><s:text name="update.button" /></b>
							</font>
						</button>
					</s:else>
					<button type="button" onclick="onCancel();"
						class="cancel-btn btn btn-warning">
						<b> <FONT color="#FFFFFF"> <s:text name="cancel.button" />
						</FONT>
						</b>
					</button>
				</div>
			</div>
		</div>


	</s:form>

	<div id="slideSoil" class="modal fade" role="dialog">
		<div></div>
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-soil-btn" class="close"
						data-dismiss="modal">&times;</button>
					<h4 style="border-bottom: solid 1px #567304;">
						<s:text name="info.soiltype1" />
					</h4>
				</div>
				<div id="validateError"
					style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

				<div class="modal-body">
					<table id="economyTable" class="table table-bordered aspect-detail">
						<tr>
							<td><div class="col-xs-8">
									<s:text name="farm.soilType" />
									<sup style="color: red;">*</sup>
								</div></td>
							<td><div class="col-xs-8">
									<input type="text" id="soilTypeName" name="soilTypeName"
										style="padding: 5px; width: 94%;" maxlength="20">
								</div></td>
						</tr>
						<tr>
							<td colspan="2">

								<button class="save-btn btn btn-success" id="saveSoilDetail"
									type="button" onclick="saveSoilTypeInfor();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button> <span class=""><span class="first-child">
										<button class="cancel-btn btn btn-sts" id="buttonSoilCancel"
											onclick="buttonSoilCance()" type="button">
											<font color="#FFFFFF"> <s:text name="cancel.button" />
											</font>
										</button>
								</span></span>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>

	</div>

	<div id="slideSoilTex" class="modal fade" role="dialog">
		<div></div>
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-soilTex-btn" class="close"
						data-dismiss="modal">&times;</button>
					<h4 style="border-bottom: solid 1px #567304;">
						<s:text name="info.soiltype2" />
					</h4>
				</div>
				<div id="validateSoilTexError"
					style="text-align: center; padding: 5px 0 0 0; color: red;"></div>

				<div class="modal-body">
					<table id="economyTable" class="table table-bordered aspect-detail">
						<tr>
							<td><div class="col-xs-8">
									<s:property
								value="%{getLocaleProperty('farm.soilTexture')}" />
									<sup style="color: red;">*</sup>
								</div></td>
							<td><div class="col-xs-8">
									<input type="text" id="soilTexName" name="soilTexName"
										style="padding: 5px; width: 94%;" maxlength="20">
								</div></td>
						</tr>
						<tr>
							<td colspan="2"><span class="" id="button_create"><span
									class="first-child">
										<button class="save-btn btn btn-success"
											id="saveSoilTexDetail" type="button"
											onclick="saveSoilTexInfor();">
											<font color="#FFFFFF"> <b><s:text
														name="save.button" /></b>
											</font>
										</button>
								</span></span> <span class=""><span class="first-child">
										<button class="cancel-btn btn btn-sts"
											id="buttonSoilTexCancel" onclick="buttonSoilTexCancels()"
											type="button">
											<font color="#FFFFFF"> <s:text name="cancel.button" />
											</font>
										</button>
								</span></span></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>

	<s:form name="listForm" id="listForm" action="farmer_detail.action">
		<s:hidden name="farmerId" value="%{farmerId}" />
		<s:hidden name="id" value="%{farmerUniqueId}" />
		<s:hidden name="tabIndexFarmer" />
		<s:hidden name="tabIndex" value="%{tabIndexFarmerZ}" />
		<s:hidden name="currentPage" />
	</s:form>

	<script type="text/javascript">
	$("#calendarDOC").datepicker({
		format : "mm-yyyy",
		startView : "months",
		minViewMode : "months"
	});
	
	$("#calendarFormat").datepicker({
		format : "mm-yyyy",
		startView : "months",
		minViewMode : "months"
	});

	 var test='<s:property value="%{getTestDate()}" />';
	 var tenant='<s:property value="getCurrentTenantId()"/>';
	 var farmerCertified ='<s:property value="farm.farmer.isCertifiedFarmer"/>';
		var farmerId = '<s:property value="farmerId" />';
	 $.fn.datepicker.defaults.format = test.toLowerCase();
        function onCancel() {
            document.listForm.submit();
        }

        function onSubmit() {
            //document.getElementById("dateOfInspection").value = document.getElementById("calendar").value;
        // alert(<s:property value="farm.farmer"/>);
            var error = true;
            var certType=$("#certType").val();
 			var icsType=$("#icsType").val();
 			var farmOwned=$("#farmOwned").val();
 			var treeDetailList=new Array();
 			 var insType=$("#inspType").val();
 			 
 			
 			 
 			
 		
 			 if(tenant =='farmAgg'){
 				 
 				 var selectedIrrigation=$("#farmIrrigation").val();
 			if(isEmpty(selectedIrrigation) || selectedIrrigation==''|| selectedIrrigation=='-1'){
					jQuery("#errorDiv").html("Please Select Irrigation Source");
          		error = false;
				}
 			}
 			
 			if(tenant!='griffith'  && tenant!='susagri' && tenant!='welspun' && tenant!='kenyafpo' && tenant!='farmAgg'){
 				if(isEmpty(farmOwned) || farmOwned==''|| farmOwned=='-1'){
 					jQuery("#irrigatedLandError").html("Please Select LandOwnership");
              		error = false;
 				}	
 				}
 			
        	if(!isEmpty(farmerId)){
        	
        		$.post("farm_detailCheckForCertifiedFarmer", {selectedFarmerId: cerf}, function (data) {
        			if (data=='YES') {
        				<s:if test="currentTenantId!='susagri' && currentTenantId!='welspun' && currentTenantId!='olivado' && currentTenantId!='symrise' && currentTenantId!='griffith' && currentTenantId!='livelihood'&& currentTenantId!='avt'&& currentTenantId!='farmAgg'">
        				<s:if test="currentTenantId!='pratibha'  ">
						 if(isEmpty(certType) || certType==''){          		 
		        	   			jQuery("#irrigatedLandError").html("Please Select Certification Type");
		                  		error = false;
		                  		
		       	   			  }/* else{
			     	   				jQuery("#irrigatedLandError").html("");
		     	   			  } */
						 <s:if test="currentTenantId=='susagri'">
						 if(isEmpty(icsType) || icsType==''){          		 
			      	   			jQuery("#irrigatedLandError").html("Please Select Certification Status");
			                		error = false;
			                		
			     	   			  }
						 if(isEmpty(insType) || insType==''){          		 
			      	   			jQuery("#irrigatedLandError").html("Please Select Inspection");
			                		error = false;
			                		
			     	   			  }
						 </s:if>
						 <s:else> 
						 if(isEmpty(icsType) || icsType==''){          		 
			      	   			jQuery("#irrigatedLandError").html("Please Select Conversion Status");
			                		error = false;
			                		
			     	   			  }
			     	   			  
						 </s:else>
						 </s:if>
						<s:else> 
						 if(isEmpty(icsType) || icsType==''){          		 
			      	   			jQuery("#irrigatedLandError").html("Please Select Conversion Status");
			                		error = false;
			                		
			     	   			  }
						 </s:else>
						 </s:if>
        			}
        			if(error){
        				<s:if test="currentTenantId!='griffith'">
        				validateImage();
        				</s:if>
                 	   
                 	  var tableBody = jQuery("#treeData");
                 	  //alert("table"+tableBody);
                 	 var len =  tableBody.find('tr').length;
                  	 tableBody.find('tr').each(function (el) {
                  		 
 			         	    var prodStatus = $(this).find("td").eq(5).html();    
 			         	   var cultivar = $(this).find("td").eq(6).html();    
 			         	  var years = $(this).find("td").eq(7).html();    
 			         	 var noOfTrees = $(this).find("td").eq(3).html();    
 			         	 var objfI1=new Object();
 			       		objfI1.prodStatus=prodStatus;
 			       		objfI1.variety=cultivar;
 			       		objfI1.years=years;
 			       		objfI1.noOfTrees=noOfTrees;
 			       		treeDetailList.push(objfI1);

  
          	   
          	});
          	
           			if(len>0){
                   	$('#jsonStr').val(JSON.stringify(treeDetailList));
                	    
                	} 
           	              
                       var sanghamVal = '<s:property value="sangham"/>';
                     
                       var command=$("#command").val();
                       if(command.toLowerCase()=="update"){
                       	
                       var flagUpadet=validateUpdateTable();
                       
                      var flag = validateTableData();
                      if(enableSoil=='1'){
                        	 var isError =validateDocument();
                        	 
                        	if(flag && isError==true && addDynamicField()){
                       		 document.form.submit();
                       		}
                         	  }else{
                      		if(flagUpadet==true&&flag==true && addDynamicField()){
                      		 document.form.submit();
                   		}
                         	  }
                       }else{
                       	var flag= validateTableData();
                       	  if(enableSoil=='1'){
                      	 var isError =validateDocument();
                      	if(flag && isError==true && addDynamicField()){
                     		 document.form.submit();
                     		}
                       	  }
                       	  else{
                       		  if(flag && addDynamicField()){
                              		 document.form.submit();
                              		} 
                       	  }
                      }
                      
                   }
                });
        	}
        }
        function formExpenditure(){
      	 	 var tableFoot=document.getElementById("expenditure");
      		var rows = tableFoot.getElementsByTagName("tr");
      		var expenditureList=[];
      		for(i=1;i<=rows.length;i++){
      			var objExpn=new Object();
      			var expndDtlId="#expndDtl"+i;
      			var expndValId="#expndValue"+i;
      			var expenditureInfoId="#expenditureId"+i;
      			
      			objExpn.name=jQuery(expndDtlId).text();
      			objExpn.value=jQuery(expndValId).text();
      			objExpn.id=jQuery(expenditureInfoId).val();
      			
      			expenditureList.push(objExpn);
      		}
      		var jsonExpndArray=new Object();
      		jsonExpndArray=JSON.stringify(expenditureList);
      		jQuery("#expenditureJson").val(jsonExpndArray); 
      		
      	}
        function formsoilTesting(){
    	 	 var tableFoot=document.getElementById("tblSoilData");
    		var rows = tableFoot.getElementsByTagName("tr");
    		var soilList=[];
    		for(i=1;i<=rows.length;i++){
    			var objfI1=new Object();
    			var sugesstionId="#suggestion"+i;
    			var actionTakenId="#actionTaken"+i;
    			var soilId="#soilTestId"+i;
    			
    			objfI1.officersSuggestion=jQuery(sugesstionId).text();
    			objfI1.takenAction=jQuery(actionTakenId).text();
    			objfI1.id=jQuery(soilId).val();
    			
    			soilList.push(objfI1);
    		}
    		var jsonArray=new Object();
    		jsonArray=JSON.stringify(soilList);
    		jQuery("#soilTestJson").val(jsonArray); 
    		console.log(jsonArray); 
    	}
        
        
        jQuery(function ($) {
        	$("#calendar").datepicker({}); 
        	
         });
        
        function calculateLandHect(){
        	var getValue=$("#totalLand").val();
        	if(getValue==""){
        		$("#landHectValues").empty();
        	}
        	else{
        		var landHecVal = $("#totalLand").val()*0.40468564224;
        		
            	//document.getElementById('landHectValues').innerHTML=landHecVal.toFixed(5);
        	}
        }
        
        function calculateNoOfWineOnPlot(){
        	var getAreaPlot=$("#totalLandd").val();
        	var getEstProductionPlot=$("#distanceProcessingUnit").val();
        	var noOfWineOnPlot =0.00;
        	if(getAreaPlot!="" && getEstProductionPlot!="" ){
        	noOfWineOnPlot = getAreaPlot*getEstProductionPlot/0.01;
        	}
        	document.getElementById('noOfWineOnPlot').innerHTML=noOfWineOnPlot.toFixed(2);
        }
        
        function calculatePlantHect(){
        	if(tenant!='griffith'){
        		var value=$("#plantingArea").val();
            	var getValue=$("#totalLand").val();
            	if(value=="")
            		{
            		$("#plantHectValues").empty();
            		}
            	else if(getValue!="" && value!="" && value>getValue)
            		{
            		document.getElementById('errorDiv').innerHTML="Proposed Planting Area can not be greater than the Total Land Holding";
            		}      		
            	else
            		{
            	//var plantHect = $("#plantingArea").val()*0.40468564224;

            	//document.getElementById('plantHectValues').innerHTML=plantHect.toFixed(5);
            		}
        	}
        	
        }
        
     function showFarmOther(val){
    	if(val==99){
    	 jQuery(".farmOtherDiv").show();
    	 }else{
    		 jQuery(".farmOtherDiv").hide();
    		 $("#farmOther").val('');
    	 }
     }
     
     function setQualified(val){


     	if(val=='2'){
     	 jQuery(".qualNo").removeClass('hide');
     	 }else{
     		jQuery(".qualNo").find("input").val("");
        	 jQuery(".qualNo").addClass('hide');
        	 }
      }
     
     function isNumber(evt) {
 		
 	    evt = (evt) ? evt : window.event;
 	    var charCode = (evt.which) ? evt.which : evt.keyCode;
 	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
 	        return false;
 	    }
 	    return true;
 	}
     
     function getBranchIdDyn(){
    		return '<s:property value="branch"/>';
    	}

     function enableFarmPhotoModal(idArr) {
    		try {
    			$("#mbody").empty();
    			var mbody = "";
    			mbody = "";
    			mbody = "<div class='item active'>";
    			mbody += '<img src="farm_populateImage.action?id='
    					+ idArr+'"/>';
    			mbody += "</div>";
    			$("#mbody").append(mbody);
    			
    			document.getElementById("enablePhotoModal").click();
    		} catch (e) {
    			alert(e);
    		}
    	}

    </script>
	<!--<div id="popupBackground"></div>
<div id="restartAlert" class="popPendingMTNTAlert">
<div class="popClose" onclick="disableExtendAlert()"></div>
<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
    align="center"></div>
<div id="defaultRestartAlert" style="display: block;">
<table align="center">
    <tr>
            <td><s:text name="farm.totalArea" /></td>
            <td style="margin: 30px;"><s:property value="totalArea" /></td>
    </tr>
    <tr>
            <td style="padding-top: 20px; padding-left: 55px;"><input
                    type="button" id="restart" class="popBtn" value="<s:text name="ok"/>"
                    onclick="disableExtendAlert()" /></td>
    </tr>
</table>
</div>
</div>

    -->
	<%--<s:if test='%{totalArea!=null}'>
    <script>

        $(document).ready(function(){
                enableRestartAlert();
        });

</script>

</s:if> --%>

	<%-- <div>
<tr>
<td>
sdds
</td>
<td>
<s:property value="sangham"/> 
</td>
</tr>


</div> --%>
	<s:hidden id="gramPanchayatEnable" name="gramPanchayatEnable"></s:hidden>
</body>
