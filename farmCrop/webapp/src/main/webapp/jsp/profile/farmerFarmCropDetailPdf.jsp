<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">



</head>
<script>
		jQuery(document).ready(function(){
			var tenant='<s:property value="getCurrentTenantId()"/>';
			var branchId='<s:property value="getBranchId()"/>';
			var type= '<%=request.getParameter("type")%>';

			 hideFieldss();
			
				jQuery.post("farmer_populateHideFn.action", function(result) {
					
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
									
								}
							});
						}
						else if(index=='destroyFileds'){
							$(value).each(function(k,v){
								$("."+v.name).remove();
							});
						}
					});
					$(".farmerDynamicField").removeClass("hide");
					
				});
				jQuery.post("farmer_populateHideFnFarm.action", {}, function(result) {
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
    					}
        			});
        			
        	    });
			
				jQuery.post("farmer_populateHideFnFarmCrop.action", {}, function(result) {
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
    					}
        			});
        			
        	    });
			
			populateInsertComponent();
		<%-- 	$("ul > li").removeClass("active");
			var tabIndex =<%if (request.getParameter("tabIndex") == null) {
				out.print("'#tabs-1'");
			} else {
				out.print("'" + request.getParameter("tabIndex") + "'");
			}%>;
		    var tabObj = $('a[href="' + tabIndex + '"]');
		    $(tabObj).closest("li").addClass('active');
		    $("div").removeClass("active in");
		    $(tabIndex).addClass('active in'); --%>
			
		    //below code to set tab as 2 when bread crumb value is sent.    
		   /*  var tabSelected = getUrlParameter('tabValue');
		    if (tabSelected == "tabs-2"){
			    $(tabIndex).removeClass("active in");
			    $('#tabs-2').addClass('active in');
			    $(tabObj).closest("li").removeClass('active');
			    tabObj = $('a[href="#tabs-2"]');
			    $(tabObj).closest("li").addClass('active');
		    } */
		    renderDynamicDetailFeilds();
		    if(tenant=='olivado'){
		    refreshAnimalTemplateList11();
		    loadFarmHistoryGridPdf();
		}
		  
		});
		try {
			//var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
		//	var tProjection = new OpenLayers.Projection("EPSG:900913");
			var url = window.location.href;
			var temp = url;
			for (var i = 0; i < 1; i++) {
				temp = temp.substring(0, temp.lastIndexOf('/'));
			}
		/* 	var href = temp;
			var coordinateImg = "red_placemarker.png";
			var iconImage = temp + '/img/' + coordinateImg; */
		} catch (err) {

		}
		   function populateInsertComponent()
           {
           	 jQuery.post("farmer_populateDynamicInsertField.action", {}, function(result) {
           		 var jsonData = jQuery.parseJSON(result);
           			
           		 $(jsonData).each(function(k, v) {
      				 var id = "#"+(v.componentName);
      				
      				 if(!isEmpty(v.afterInsert)){
      					 var insertEle = $('[name="'+v.afterInsert+'"]').parent();
      					 var compo =  jQuery(id).parent();
      					 console.log(compo);
      					 $(compo).insertAfter(insertEle);
      				 }
      				 if(!isEmpty(v.beforeInsert)){
      					 var insertEle = $('[name="'+v.beforeInsert+'"]').parent();
      					 console.log(insertEle);
      					 var compo =  jQuery(id).parent();
      					 console.log(compo);
      					 $(compo).insertBefore(insertEle);
      				 }
      			 });
           		
           	  });
           }

		function makeQrCode () {      
		    var elText = '<s:property value="farmer.farmerId" />';		   
		    $('#qrcode').qrcode(elText);
		}
	
	
		
		function hideFieldss(){
			 var app = $(".aPanel");
			 $(app).addClass("hide");
			 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
			 	 $(this).addClass("hide");
			 });
			  
			}
		
		function exportPDFCat(){
    		var printContents ;
    		$( ".pdff" ).each(function() {
    			printContents = printContents+$(this).html();
    			});
    		var originalContents = document.body.innerHTML;
    	
    	
    		
    		 var script = document.createElement('script');
         	  script.type = 'text/javascript';
         	  script.src = 'https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap';
         	
          
         	  document.body.appendChild(script);
         	  document.body.innerHTML = printContents;
      		console.log(printContents);
    		window.print();
    		document.body.innerHTML = originalContents;
    	}
		
		
		
		function hideByEleName(name){
			$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleName(name){
			$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).removeClass( "hide" );
		}

		function hideByEleClass(className){
			$("."+className).closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleClass(className){
			$("."+className).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
			$("."+className).parent().removeClass( "hide" );
		}

		function hideByEleId(id){
			$("#"+id).closest( ".dynamic-flexItem2" ).addClass( "hide" );
		}

		function showByEleId(id){
			$("#"+id).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
		}
		
		
	


		
	</script>

	<button onclick="event.preventDefault();exportPDFCat()">PDF</button>

	<div class="flex-view-layout pdff">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="error">
						<s:actionerror />
						<s:fielderror />
					</div>
					<div class="formContainerWrapper">

						<div class="aPanel farmer_info">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.farmer')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con">
								<s:if test='branchId==null'>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="app.branch" />
										</p>
										<p class="flexItem">
											<s:property value="%{getBranchName(farmer.branchId)}" />
											&nbsp;
										</p>
									</div>
								</s:if>
								<%-- 	<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="Created User Name/Mobile User Name" />
													</p>
													<p class="flexItem">
														<s:property value="farmer.createdUsername" />
													</p>
												</div>  --%>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('Created User Name/Mobile User Name')}" />
									</p>
									<p class="flexItem" name="farmer.createdUsername">
										<s:property value="farmer.createdUsername" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.enrollmentPlace" />
									</p>
									<p class="flexItem" name="farmer.enrollmentPlace">
										<s:property value="enrollmentMap[farmer.enrollmentPlace]" />
										&nbsp;
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.formFilledBy')}" />
									</p>
									<p class="flexItem" name="farmer.formFilledBy">
										<s:property value="farmer.formFilledBy" />
										&nbsp;
									</p>
								</div>


								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.assess')}" />
									</p>
									<p class="flexItem" name="farmer.assess">
										<s:property value="farmer.assess" />
										&nbsp;
									</p>
								</div>


								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.placeOfAsss')}" />
									</p>
									<p class="flexItem" name="farmer.placeOfAsss">
										<s:property value="farmer.placeOfAsss" />
										&nbsp;
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.objective')}" />
									</p>
									<p class="flexItem" name="farmer.objective">
										<s:property value="farmer.objective" />
										&nbsp;
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.farmerId')}" />
									</p>
									<p class="flexItem" name="farmer.farmerId">
										<s:property value="farmer.farmerId" />
										&nbsp;
									</p>
								</div>


								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:if
											test="currentTenantId=='pratibha' && getBranchId()=='organic'">
											<s:property value="%{getLocaleProperty('tracenet')}" />
										</s:if>
										<s:else>
											<s:property value="%{getLocaleProperty('farmerCode')}" />
										</s:else>
										<%-- <s:property
															value="%{getLocaleProperty('farmer.farmerCode')}" /> --%>
									</p>
									<p class="flexItem" name="farmer.farmerCode">
										<s:property value="farmer.farmerCode" />
									</p>
								</div>


								<div class="dynamic-flexItem2">
									<p class="flexItem" name="dateOfJoining">
										<s:text name="%{getLocaleProperty('farmer.dateOfJoin')}" />
									</p>
									<p class="flexItem">
										<s:property value="dateOfJoining" />
									</p>
								</div>

								<div class="dynamic-flexItem2 certified">
									<p class="flexItem">
										<s:text name="farmer.isCertified" />
									</p>
									<p class="flexItem" name="farmer.isCertifiedFarmer">
										<s:property
											value="isFarmerCertified[farmer.isCertifiedFarmer]" />
									</p>
								</div>
								<s:if test="currentTenantId!='livelihood'">
									<s:if test="farmer.isCertifiedFarmer==1">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="certification.type" />
											</p>
											<p class="flexItem" name="farmer.certificationType">
												<s:property
													value="certificationTypes[farmer.certificationType]" />
											</p>
										</div>

										<div class="dynamic-flexItem2 icsFieldSelect">
											<p class="flexItem">
												<s:text name="farmer.icsName" />
											</p>
											<p class="flexItem" name="farmer.icsName">
												<s:property value="farmer.icsName" />

											</p>
										</div>

										<div class="dynamic-flexItem2 icsFieldTxt">
											<p class="flexItem">
												<s:text name="farmer.icsName" />
											</p>
											<p class="flexItem" name="farmer.icsName">
												<s:property value="icsName" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="%{getLocaleProperty('farmer.icsCode')}" />
											</p>
											<p class="flexItem" name="farmer.icsCode">
												<s:property value="icsCode" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.icsUnitNumber" />
											</p>
											<p class="flexItem" name="farmer.icsUnitNo">
												<s:property value="icsUnit[farmer.icsUnitNo]" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.regNo" />
											</p>
											<p class="flexItem" name="farmer.icsTracenetRegNo">
												<s:property value="icsRegNo[farmer.icsTracenetRegNo]" />
											</p>
										</div>

										<%-- <div class="dynamic-flexItem2 farmCode">
														<p class="flexItem">
															<s:text name="farmer.codeIcs" />
														</p>
														<p class="flexItem" name="farmer.farmerCodeByIcs">
															<s:property value="farmer.farmerCodeByIcs" />
														</p>
													</div> --%>
										<s:if test="currentTenantId=='symrise'">
											<%-- <div class="dynamic-flexItem2 farmCode">
														<p class="flexItem">
															<s:text name="%{getLocaleProperty('farmer.certificateStandard.name')}" />
														</p>
														<p class="flexItem" name="farmer.farmerCodeByIcs">
															<s:property value="farmer.certificateStandard.name" />
														</p>
													</div> --%>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="%{getLocaleProperty('certification.level')}" />
												</p>
												<p class="flexItem" name="farmer.certificateStandardLevel">
													<s:text
														name="certificate%{farmer.certificateStandardLevel}" />
												</p>
											</div>
										</s:if>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.codeTracenet" />
											</p>
											<p class="flexItem" name="farmer.farmersCodeTracenet">
												<s:property value="farmer.farmersCodeTracenet" />
											</p>
										</div>


										<s:if test="currentTenantId=='olivado'">

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="Created User Name/Mobile User Name" />
												</p>
												<p class="flexItem">
													<s:property value="farmer.createdUsername" />
												</p>
											</div>
											<div class="dynamic-flexItem2 ggn">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('farmer.codeIcs')}" />
												</p>
												<p class="flexItem" name="farmer.farmerCodeByIcs">
													<s:property value="farmer.farmerCodeByIcs" />
												</p>
											</div>

											<div class="dynamic-flexItem2 ggnRegNo">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farmer.codeTracenet')}" />
												</p>
												<p class="flexItem" name="farmer.farmersCodeTracenet">
													<s:property value="farmer.farmersCodeTracenet" />
												</p>
											</div>

										</s:if>
									</s:if>
								</s:if>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.beneficiary" />
									</p>
									<p class="flexItem" name="farmer.isBeneficiaryInGovScheme">
										<s:if test="farmer.isBeneficiaryInGovScheme==1">
											<s:text name="Yes" />
										</s:if>
										<s:elseif test='farmer.isBeneficiaryInGovScheme=="2"'>
											<s:text name="No" />
										</s:elseif>
										<s:else>
											<s:text name="" />
										</s:else>
									</p>

								</div>

								<div class="dynamic-flexItem2">
									<s:if test="farmer.isBeneficiaryInGovScheme==1">
										<p class="flexItem">
											<s:text name="farmer.nameOfScheme" />
										</p>
										<p class="flexItem" name="farmer.nameOfTheScheme">

											<s:property value="farmer.nameOfTheScheme" />

										</p>
									</s:if>
								</div>


								<div class="dynamic-flexItem2">
									<s:if test="farmer.isBeneficiaryInGovScheme==1">
										<p class="flexItem">
											<s:text name="farmer.governmentDepartment" />
										</p>
										<p class="flexItem" name="farmer.govtDept">

											<s:property value="farmer.govtDept" />

										</p>
									</s:if>
								</div>


								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.regYear')}" />
									</p>
									<p class="flexItem" name="farmer.yearOfICS">
										<s:if test='farmer.yearOfICS!="-1"'>
											<s:property value="farmer.yearOfICS" />
										</s:if>
									</p>
								</div>
								<%-- <s:if test="farmer.isDisable!=null">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
													<s:property
															value="%{getLocaleProperty('isDisabled')}" />
													</p>
													<p class="flexItem" name="selectedDisabled">
														<s:text	name='%{"dynamicRadio"+farmer.isDisable}' />
													</p>
												</div>
												</s:if>	 --%>

							</div>
						</div>


						<div class="aPanel pers_info">
							<div class="aTitle">
								<h2>
									<s:text name="info.personal" />
									<div class="pull-right">
										<a class="aCollapse" href="#"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>

							<div class="aContent dynamic-form-con">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.firstName')}" />
									</p>
									<p class="flexItem" name="farmer.firstName">
										<s:property value="farmer.firstName" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.lastName')}" />
									</p>
									<p class="flexItem" name="farmer.lastName">
										<s:property value="farmer.lastName" />
									</p>
								</div>
								<s:if test="currentTenantId!='olivado'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.surName')}" />
										</p>
										<p class="flexItem" name="farmer.surName">
											<s:property value="farmer.surName" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem2 hideDob">
									<p class="flexItem">
										<s:text name="farmer.dateOfBirth" />
									</p>
									<p class="flexItem" name="calendar">
										<s:property value="dateOfBirth" />
									</p>
								</div>

								<s:if test="currentTenantId!='olivado'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.age1')}" />
										</p>
										<p class="flexItem" name="age">
											<s:property value="farmer.age" />
										</p>
									</div>
								</s:if>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.gender" />
									</p>
									<p class="flexItem" name="farmer.gender">
										<s:text name='%{farmer.gender}' />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="chetnaCategory" />
									</p>
									<p class="flexItem" name="farmer.category">
										<s:property value="farmer.category" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('castename')}" />
									</p>
									<p class="flexItem" name="farmer.casteName">
										<s:property value="farmer.casteName" />
									</p>
								</div>

								<s:if
									test="currentTenantId!='symrise' && currentTenantId!='griffith'">
									<div class="dynamic-flexItem2">
										<p class="flexItem" name="farmer.proofNo">
											<s:property value="%{getLocaleProperty('farmer.ProofNo')}" />
										</p>
										<p class="flexItem" name="farmer.idProof">
											<s:property value="farmer.proofNo" />
										</p>
									</div>
								</s:if>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="education" />
									</p>
									<p class="flexItem" name="farmer.education">
										<s:property value="educationList[farmer.education]" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="marital.Status" />
									</p>
									<p class="flexItem" name="farmer.maritalSatus">
										<s:property value="farmer.maritalSatus" />
									</p>
								</div>
								<s:if test='idProofEnabled==1'>

									<s:if test="currentTenantId !='efk'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.idProof" />
											</p>
											<p class="flexItem" name="farmer.idProof">
												<s:property value="proofList[farmer.idProof]" />
											</p>

										</div>
									</s:if>
									<s:if test="farmer.idProof=='99'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.otherProof" />
											</p>
											<p class="flexItem" name="farmer.idProof">
												<s:property value="farmer.otherIdProof" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farmer.ProofNo')}" />
											</p>
											<p class="flexItem" name="farmer.idProof">
												<s:property value="farmer.proofNo" />
											</p>

										</div>
									</s:if>
									<s:else>
										<div class="dynamic-flexItem2">
											<p class="flexItem" name="farmer.proofNo">
												<s:property value="%{getLocaleProperty('farmer.ProofNo')}" />
											</p>
											<p class="flexItem" name="farmer.idProof">
												<s:property value="farmer.proofNo" />
											</p>
										</div>
									</s:else>
									<s:if test='idProofEnabled==1 && IdImgAvil=="1"'>
										<div class="dynamic-flexItem2">
											<p class="flexItem" name="farmer.idProofImg">
												<s:property
													value="%{getLocaleProperty('farmer.idProofImg')}" />
											</p>
											<p class="flexItem" name="farmer.idProof">
												<button type='button' class='btn btn-sm pull-right photo'
													style='margin-right: 15%'
													onclick="enableFarmerPhotoModal(<s:property value="farmer.id"/>)">
													<i class='fa fa-picture-o' aria-hidden='true'></i>
												</button>
											</p>
										</div>
									</s:if>
								</s:if>
								<%-- <div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="%{getLocaleProperty('farmerCode')}" />
													</p>
													<p class="flexItem" name="farmer.farmerCode">
														<s:property value="farmer.farmerCode" />
													</p>
												</div> --%>
								<s:if test="currentTenantId!='sagi'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('farmer.adhaarNo')}" />
										</p>
										<p class="flexItem" name="farmer.adhaarNo">
											<s:property value="farmer.adhaarNo" />
										</p>
									</div>
								</s:if>

								<s:if test="currentTenantId=='atma'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.socialCategory" />
										</p>
										<p class="flexItem" name="farmer.socialCategory">
											<s:property value="socialCategoryList[farmer.socialCategory]" />
										</p>
									</div>
								</s:if>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.religion" />
									</p>
									<s:if test="farmer.religion=='99'">
										<p class="flexItem" name="farmer.religion">
											<s:property value="religionList[farmer.religion]" />
										</p>
									</s:if>
									<s:else>
										<p class="flexItem" name="farmer.religion">
											<s:property value="religionList[farmer.religion]" />
										</p>
									</s:else>
								</div>
								<s:if test="currentTenantId!='sagi'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.typeOfFamily" />
										</p>
										<p class="flexItem" name="farmer.typeOfFamily">
											<s:property value="familyTypeList[farmer.typeOfFamily]" />
										</p>
									</div>
									<s:if test="farmer.sangham=='01'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="houseHoldLandWet" />
											</p>
											<p class="flexItem" name="farmer.houseHoldLandWet">
												<s:property value="farmer.houseHoldLandWet" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="houseHoldLandDry" />
											</p>
											<p class="flexItem" name="farmer.houseHoldLandDry">
												<s:property value="farmer.houseHoldLandDry" />
											</p>
										</div>
									</s:if>
								</s:if>

								<div class="dynamic-flexItem2 prefWrk">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.preferenceWork')}" />
									</p>
									<p class="flexItem" name="farmer.prefWrk">
										<s:property value="farmer.prefWrk" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('status')}" />
									</p>
									<p class="flexItem" name="farmer.personalStatus">
										<s:property value="statusList[farmer.personalStatus]" />
									</p>
								</div>

							</div>
						</div>

						<div class="aPanel contact_info">
							<div class="aTitle">
								<h2>
									<s:text name="info.contact" />
									<div class="pull-right">
										<a class="aCollapse" href="#"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.address')}" />
									</p>
									<p class="flexItem" name="farmer.address">
										<s:property value="farmer.address" />
									</p>
								</div>
								<s:if test="currentTenantId=='olivado'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.surName')}" />
										</p>
										<p class="flexItem" name="farmer.surName">
											<s:property value="farmer.surName" />
										</p>
									</div>
								</s:if>
								<s:if
									test="currentTenantId=='welspun'|| currentTenantId=='simfed' ">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.pinCode')}" />
										</p>
										<p class="flexItem" name="farmer.pinCode">
											<s:property value="farmer.pinCode" />
										</p>
									</div>
								</s:if>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.mobileNumber')}" />
									</p>
									<p class="flexItem" name="farmer.mobileNumber">
										<s:property value="farmer.mobileNumber" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.email" />
									</p>
									<p class="flexItem" name="farmer.email">
										<s:property value="farmer.email" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="country.name" />
									</p>
									<p class="flexItem" name="selectedCountry">
										<s:property
											value="farmer.village.city.locality.state.country.code" />
										&nbsp-&nbsp
										<s:property
											value="farmer.village.city.locality.state.country.name" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('state.name')}" />
									</p>
									<p class="flexItem" name="selectedState">
										<s:property value="farmer.village.city.locality.state.code" />
										&nbsp-&nbsp
										<s:property value="farmer.village.city.locality.state.name" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('locality.name')}" />
									</p>
									<p class="flexItem" name="selectedLocality">
										<s:property value="farmer.village.city.locality.code" />
										&nbsp-&nbsp
										<s:property value="farmer.village.city.locality.name" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('city.name')}" />
									</p>
									<p class="flexItem" name="selectedCity">
										<s:property value="farmer.village.city.code" />
										&nbsp-&nbsp
										<s:property value="farmer.village.city.name" />
									</p>
								</div>

								<s:if test="gramPanchayatEnable==1">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('panchayat.name')}" />
										</p>
										<p class="flexItem" name="selectedPanchayat">
											<s:property value="farmer.village.gramPanchayat.code" />
											&nbsp-&nbsp
											<s:property value="farmer.village.gramPanchayat.name" />
										</p>
									</div>
								</s:if>
								<%-- <div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('village.name')}" />
													</p>
													<p class="flexItem" name="selectedVillage">
														<s:property value="farmer.village.code" />
														&nbsp-&nbsp
														<s:property value="farmer.village.name" />
													</p>
												</div> --%>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('village.name')}" />
									</p>
									<p class="flexItem" name="selectedVillage">
										<s:property value="farmer.village.code" />
										&nbsp-&nbsp
										<s:property value="farmer.village.name" />
									</p>
								</div>

								<s:if test="currentTenantId=='atma'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('profile.sangham')}" />
										</p>
										<p class="flexItem" name="selectedSangham">
											<s:property value="farmer.sangham" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('profile.samithi')}" />
									</p>
									<p class="flexItem" name="selectedSamithi">
										<s:property value="farmer.samithi.name" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('profile.groupPosition')}" />
									</p>
									<p class="flexItem" name="selectedGroupPosition">
										<s:property value="farmer.positionGroup" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('fpoGroup')}" />
									</p>
									<p class="flexItem" name="farmer.fpo">
										<s:property value="fpo[farmer.fpo]" />
									</p>
								</div>

								<%-- <div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('shg')}" />
													</p>
													<p class="flexItem" name="farmer.shg">
														 <s:property value="shgName" />  
														  <s:property value="shgList[%{farmer.shg}]" />  
														
													</p>
												</div> --%>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.phoneNumber')}" />
									</p>
									<p class="flexItem" name="farmer.phoneNumber">
										<s:property value="farmer.phoneNumber" />
									</p>
								</div>
								<s:if test="currentTenantId=='wilmar'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('selectedMasterData')}" />
										</p>
										<p class="flexItem" name="farmer.masterData">
											<s:property value="farmer.masterData" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.status" />
									</p>
									<p class="flexItem" name="farmer.status">
										<s:text name='%{"status"+farmer.status}' />
									</p>
								</div>
							</div>
						</div>

						<div class="aPanel family_Info">
							<div class="aTitle">
								<h2>
									<s:text name="info.family" />
									<div class="pull-right">
										<a class="aCollapse" href="#familyInfo"><i
											class="fa fa-chevron-down"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="familyInfo">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.noOfFamilyMembers.prop')}" />
									</p>
									<p class="flexItem" name="farmer.noOfFamilyMembers">
										<s:property value="farmer.noOfFamilyMembers" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.adultMale" />
									</p>
									<p class="flexItem" name="farmer.adultCountMale">
										<s:property value="farmer.adultCountMale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.adultFemale" />
									</p>
									<p class="flexItem" name="farmer.adultCountMale">
										<s:property value="farmer.adultCountFemale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.childMale" />
									</p>
									<p class="flexItem" name="farmer.childCountMale">
										<s:property value="farmer.childCountMale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.childFemale" />
									</p>
									<p class="flexItem" name="farmer.childCountMale">
										<s:property value="farmer.childCountFemale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.childCountSchollMale" />
									</p>
									<p class="flexItem" name="farmer.noOfSchoolChildMale">
										<s:property value="farmer.noOfSchoolChildMale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.childCountSchollFemale" />
									</p>
									<p class="flexItem" name="farmer.noOfSchoolChildMale">
										<s:property value="farmer.noOfSchoolChildFemale" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.houseHoldCnt')}" />
									</p>
									<p class="flexItem" name="farmer.noOfHouseHoldMem">
										<s:property value="farmer.noOfHouseHoldMem" />
									</p>
								</div>

								<%-- <div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.houseHoldCntMale" />
													</p>
													<p class="flexItem" name="farmer.maleCnt">
														<s:property value="farmer.maleCnt" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.houseHoldCntFemale" />
													</p>
													<p class="flexItem" name="farmer.maleCnt">
														<s:property value="farmer.femaleCnt" />
													</p>
												</div> 

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.adultMale')}" />
													</p>
													<p class="flexItem" name="farmer.adultCountMale">
														<s:property value="farmer.adultCountMale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.adultFemale')}" />
													</p>
													<p class="flexItem" name="farmer.adultCountMale">
														<s:property value="farmer.adultCountFemale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childCountSchollMale" />
													</p>
													<p class="flexItem" name="farmer.noOfSchoolChildMale">
														<s:property value="farmer.noOfSchoolChildMale" />
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.childCountSchollFemale" />
													</p>
													<p class="flexItem" name="farmer.noOfSchoolChildMale">
														<s:property value="farmer.noOfSchoolChildFemale" />
													</p>
												</div>--%>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farmer.headOfFamily" />
									</p>
									<p class="flexItem" name="farmer.headOfFamily">
										<s:property value="headOfFamilyList[farmer.headOfFamily]" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.totalHs')}" />
									</p>
									<p class="flexItem" name="farmer.totalHsldLabel">
										<s:property value="farmer.totalHsldLabel" />
									</p>
								</div>

								<s:if test="currentTenantId =='griffith'">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmer.drinkingWSProp')}" />
										</p>
										<p class="flexItem" name="farmer.drinkingWSProp">
											<s:property value="farmer.farmerEconomy.drinkingWaterSource" />
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmer.isLoanTakenLastYearProp')}" />
										</p>
										<p class="flexItem" name="farmer.isLoanTakenLastYearProp">
											<s:if test="farmer.loanTakenLastYear==1">
												<s:text name="yes" />
											</s:if>
											<s:else>
												<s:text name="no" />
											</s:else>
									</div>
								</s:if>
							</div>
						</div>



						<div class="aPanel insuranceInfoEnabled">
							<div class="aTitle">
								<h2>
									<s:text name="info.insurance" />
									<div class="pull-right">
										<a class="aCollapse" href="#insuranceInfo"><i
											class="fa fa-chevron-down"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="insuranceInfo">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farmer.life')}" />
									</p>
									<p class="flexItem" name="farmer.lifeInsurance">
										<s:property value="farmer.lifeInsurance" />
									</p>
								</div>

								<s:if test="farmer.lifeInsurance=='Yes'">
									<div class="dynamic-flexItem2">
										<p class="flexItem ">
											<s:text name="farmer.lifeAmt" />
										</p>
										<p class="flexItem" name="farmer.lifeInsAmount">
											<s:property value="farmer.lifeInsAmount" />
										</p>
									</div>
								</s:if>
								<div class="dynamic-flexItem2">
									<p class="flexItem ">
										<s:property value="%{getLocaleProperty('farmer.health')}" />
									</p>
									<p class="flexItem" name="farmer.healthInsurance">
										<s:text name="%{farmer.healthInsurance}" />
									</p>
								</div>

								<s:if test="currentTenantId =='efk'">
									<div class="dynamic-flexItem2">
										<p class="flexItem ">
											<s:text name="farmer.healthAmt" />
										</p>
										<p class="flexItem" name="farmer.healthInsAmount">
											<s:property value="farmer.healthInsAmount" />
										</p>
									</div>

								</s:if>
								<s:else>
									<s:if test="farmer.healthInsurance=='Yes'">
										<div class="dynamic-flexItem2">
											<p class="flexItem ">
												<s:text name="farmer.healthAmt" />
											</p>
											<p class="flexItem" name="farmer.healthInsAmount">
												<s:property value="farmer.healthInsAmount" />
											</p>
										</div>
									</s:if>
								</s:else>

								<div class="dynamic-flexItem2">
									<p class="flexItem ">
										<s:text name="farmer.cropInsuranceProp" />
									</p>
									<p class="flexItem" name="farmer.isCropInsured">
										<s:if test='farmer.isCropInsured=="1"'>
											<s:text name="Yes" />
										</s:if>
										<s:elseif test='farmer.isCropInsured=="2"'>
											<s:text name="No" />
										</s:elseif>
										<s:else>
											<s:text name="" />
										</s:else>
									</p>
								</div>

								<s:if test='farmer.isCropInsured=="1"'>
									<div class="dynamic-flexItem2">
										<p class="flexItem ">
											<s:text name="farmer.cropName" />
										</p>
										<p class="flexItem" name="farmer.farmerCropInsurance">
											<s:property value="farmer.farmerCropInsurance" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem ">
											<s:text name="%{getLocaleProperty('farmer.acresCroped')}" />
										</p>
										<p class="flexItem" name="farmer.acresInsured">
											<s:property value="farmer.acresInsured" />
										</p>
									</div>
								</s:if>
							</div>
						</div>




						 <div class="dynamicFieldsRender"></div> 
						<%-- 		<div class="formContainerWrapper dynamic-form-con">
										<div class="col-md-6">
										<div class="row">
											<h2>
												<s:text name="farmer.photo" />
											</h2>
											<div id="" class="farmer-photo">
												<s:if
													test='farmerImageByteString!=null && farmerImageByteString!=""'>
													<img border="0"
														src="data:image/png;base64,<s:property value="farmerImageByteString"/>">
												</s:if>
												<s:else>
													<img align="middle" border="0" src="img/no-image.png">
												</s:else>
											</div>
										</div>
									</div>
									
									<div class="col-md-6">
									<div class="row">
										<h2>
											<s:text name="farmer.photo" />
										</h2>
										<div id="" class="farmer-photo">
											<s:if
												test='farmerImageByteString!=null && farmerImageByteString!=""'>
												<img border="0"
													src="data:image/png;base64,<s:property value="farmerImageByteString"/>">
											</s:if>
											<s:else>
												<img align="middle" border="0" src="img/no-image.png">
											</s:else>
										</div>
										</div>
									</div>
									<div class="clear"></div>
									</div>
 --%>






					</div>
				</div>


				<div class="flexRight appContentWrapper">

					<div class="formContainerWrapper dynamic-form-con">
						<h2>
							<s:property value="%{getLocaleProperty('farmer.photo')}" />
						</h2>
						<div id="" class="farmer-photo">
							<s:if
								test='farmerImageByteString!=null && farmerImageByteString!=""'>
								<img border="0"
									src="data:image/png;base64,<s:property value="farmerImageByteString"/>">
							</s:if>
							<s:else>
								<img align="middle" border="0" src="img/no-image.png">
							</s:else>
						</div>
						<div>
							<div class="flexItem flex-layout flexItemStyle">
								<b><s:text name="farmer.latitude" />: &nbsp;</b>
								<s:property value="farmer.latitude" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><s:text
										name="farmer.longitude" />: &nbsp;</b>
								<s:property value="farmer.longitude" />
							</div>
						</div>
						<div id="qrcode"></div>
						<div class="clear"></div>
					</div>
					<s:if test="getFingerPrintEnabled()==1">
						<div class="formContainerWrapper dynamic-form-con">
							<h2>
								<s:text name="fingerPrint" />
							</h2>
							<div id="" class="farmer-photo">
								<s:if
									test='fingerPrintImageByteString!=null && fingerPrintImageByteString!=""'>
									<img border="0"
										src="data:image/png;base64,<s:property value="fingerPrintImageByteString"/>">
								</s:if>
								<s:else>
									<img align="middle" border="0" src="img/no-image.png">
								</s:else>
							</div>
							<div class="clear"></div>
						</div>
					</s:if>
					<div class="formContainerWrapper dynamic-form-con ">
						<s:if test="farmer.isCertifiedFarmer==1">
							<div class="aPanel assets_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:text name="info.assetOwnership" />
										<div class="pull-right">
											<a class="aCollapse" href="#assetInfo"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>

								<div class="aContent dynamic-form-con" id="assetInfo">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.consumerElectronicsProp" />
										</p>
										<p class="flexItem" name="farmer.consumerElectronics">
											<s:property value="farmer.consumerElectronics" />
										</p>
									</div>
									<div class="dynamic-flexItem2 vehicleProp">
										<p class="flexItem">
											<s:text name="farmer.vehicleProp" />
										</p>
										<p class="flexItem" name="farmer.vehicle">
											<s:property value="farmer.vehicle" />
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.cellPhoneProp" />
										</p>
										<p class="flexItem" name="farmer.cellPhone">
											<s:if test="farmer.cellPhone==1">
												<s:text name="Yes" />
											</s:if>
											<s:elseif test="farmer.cellPhone==2">
												<s:text name="No" />
											</s:elseif>
										</p>
									</div>

									<s:if test="farmer.cellPhone==1">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.smartPhones" />
											</p>
											<p class="flexItem" name="farmer.smartPhone">
												<s:if test="farmer.smartPhone==1">
													<s:text name="Yes" />
												</s:if>
												<s:elseif test="farmer.smartPhone==2">
													<s:text name="No" />
												</s:elseif>
											</p>

										</div>
									</s:if>

								</div>
								<div class="dynamic-flexItem2 agricultureImplements">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.agricultureImplements')}" />
									</p>
									<p class="flexItem" name="farmer.agricultureImplements">
										<s:property value="farmer.agricultureImplements" />
									</p>
								</div>

							</div>

							<div class="aPanel dwelling_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:text name="info.dwelingPlace" />
										<div class="pull-right">
											<a class="aCollapse" href="#dweelingInfo"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>

								<div class="aContent dynamic-form-con" id="dweelingInfo">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.housingOwnershipProp" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.housingOwnership">
											<s:if test="farmer.farmerEconomy.housingOwnership!=99">
												<td style="font-weight: bold;"><s:iterator
														value="housingOwnershipMap" status="rowstatus"
														var="element">
														<s:if
															test="#rowstatus.count == farmer.farmerEconomy.housingOwnership">
															<s:property value="value" />
														</s:if>
													</s:iterator></td>
											</s:if>

											<s:else>
												<td style="font-weight: bold;"><s:property
														value="farmer.farmerEconomy.housingOwnershipOther" /></td>

											</s:else>
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.electrifiedHouseDetailProp" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.electrifiedHouse">
											<s:iterator value="electrifiedHouseMap" status="rowstatus">
												<s:if
													test="#rowstatus.count == farmer.farmerEconomy.electrifiedHouse">
													<s:property value="value" />
												</s:if>
											</s:iterator>
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmer.housingTypeProp')}" />
										</p>

										<s:if test="farmer.farmerEconomy.housingType==99">

											<p class="flexItem" style="font-weight: bold;">
												<s:property value="farmer.farmerEconomy.otherHousingType" />
											</p>
										</s:if>
										<s:else>

											<p class="flexItem" name="farmer.farmerEconomy.housingType">
												<s:property
													value="housingTypeList[farmer.farmerEconomy.housingType]" />
											</p>
										</s:else>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.drinkingWSProp" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.drinkingWaterSource">
											<s:property value="farmer.farmerEconomy.drinkingWaterSource" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.cookingFuel" />
										</p>
										<p class="flexItem" name="farmer.farmerEconomy.cookingFuel">
											<s:property value="farmer.farmerEconomy.cookingFuel" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.qtyPerMonth" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.qtyCookingPerMonth">
											<s:property value="farmer.farmerEconomy.qtyCookingPerMonth" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.costPerMonth" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.costCookingPerMonth">
											<s:property value="farmer.farmerEconomy.costCookingPerMonth" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.toiletAvailable" />
										</p>
										<p class="flexItem"
											name="farmer.farmerEconomy.toiletAvailable">
											<s:if test='farmer.farmerEconomy.toiletAvailable!=null'>
												<s:text
													name='%{"ita-"+farmer.farmerEconomy.toiletAvailable}' />
											</s:if>
											<s:else>
												<s:text name='no' />
											</s:else>
										</p>
									</div>

									<s:if test="farmer.farmerEconomy.toiletAvailable==1">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmer.ifToiletAvailable" />
											</p>
											<p class="flexItem"
												name="farmer.farmerEconomy.toiletAvailable">
												<s:iterator value="toiletAvailableFromMap"
													status="rowstatus">
													<s:if
														test="#rowstatus.count == farmer.farmerEconomy.ifToiletAvailable">
														<s:property value="value" />
													</s:if>
												</s:iterator>
											</p>
										</div>
									</s:if>
								</div>
							</div>
						</s:if>
						<s:if test="farmerBankInfoEnabled==1">
							<div class="aPanel bank_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:text name="info.bank" />
										<div class="pull-right">
											<a class="aCollapse" href="#bankingInfo"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>

								
									<s:iterator value="farmer.bankInfo" status="status">
									
											<%-- 				<p class="flexItem bankName">
																<a href="#" data-toggle="modal"
																	data-target="#view-bankinfo<s:property value="%{#status.count}" />"
																	class="btn btn-sts linkbtn pull-right"><i
																	class="fa fa-eye" aria-hidden="true"></i> view</a>
															</p> --%>

											<div class="dynamic-flexItem2 bank_info" >
											<p class="flexItem">
													<s:text name="farmer.bankInfo.AccountTypeProp" />
												</p>
												<p class="flexItem">
													<s:property value="accType" />
												</p>
												

											</div>
												<div class="dynamic-flexItem2 bank_info">
											<p class="flexItem">
													<s:text name="farmer.bankInfo.accNo" />
												</p>
												<p class="flexItem">
													<s:property value="accNo" />
												</p>
												

											</div>
												<div class="dynamic-flexItem2 bank_info">
											<p class="flexItem">
													 <s:text	name="farmer.bankInfo.bankName" />
												</p>
												<p class="flexItem">
													<s:property value="bankName" />
												</p>
												

											</div>
												<div class="dynamic-flexItem2 bank_info">
											<p class="flexItem">
													<s:text name="farmer.bankInfo.branchName" />
												</p>
												<p class="flexItem">
													<s:property value="branchName" />
												</p>
												

											</div>
												<div class="dynamic-flexItem2 bank_info">
											<p class="flexItem">
													<s:text
																							name="farmer.bankInfo.sortCode" />
												</p>
												<p class="flexItem">
													<s:property value="sortCode" />
												</p>
												

											</div>

											<%-- 				<div
																id="view-bankinfo<s:property value="%{#status.count}" />"
																class="modal fade" role="dialog">
																<div class="modal-dialog">
																	<div class="modal-content">
																		<div class="modal-header">
																			<button type="button" class="close"
																				data-dismiss="modal">&times;</button>
																			<h4 class="modal-title">
																				<s:text name='info.bank' />
																			</h4>
																		</div>

																		<div class="modal-body">
																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.AccountTypeProp" />
																				</label> <label><s:property value="accType" /></label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text name="farmer.bankInfo.accNo" />
																				</label> <label><s:property value="accNo" /></label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.bankName" />
																				</label> <label> <s:property value="bankName" />
																				</label>
																			</div>

																			<div class="modal-form-item">
																				<label> <s:text
																						name="farmer.bankInfo.branchName" />
																				</label> <label> <s:property value="branchName" />
																				</label>
																			</div>
																			<s:if test="getCurrentTenantId()!='sticky' ">
																				<div class="modal-form-item">
																					<label> <s:text
																							name="farmer.bankInfo.sortCode" /></label> <label>
																						<s:property value="sortCode" />
																					</label>
																				</div>
																			</s:if>

																		</div>

																		<div class="modal-footer">
																			<button type="button" class="btn btn-default"
																				data-dismiss="modal">Close</button>
																		</div>
																	</div>
																</div>
															</div> --%>





										
									</s:iterator>



							</div>
						</s:if>
						<%-- 
										<div class="aPanel farmerHealthInfo irpFormHide"
											style="width: 100%">
											<div class="aTitle">
												<h2>
													<s:text name="info.healthAsses" />
													<div class="pull-right">
														<a class="aCollapse" href="#healthAssesInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con" id="healthAssesInfo">
												<s:iterator value="farmer.farmerHealthAsses" status="status">
													<div class="dynamic-flexItem2">
														<p class="flexItem bold">
															<s:property
																value="%{getCatlogueValueByCode(diabilityType).name}" />
														</p>
														<p class="flexItem">
															<a href="#" data-toggle="modal"
																data-target="#view-healthAsses<s:property value="%{#status.count}" />"
																class="btn btn-sts linkbtn pull-right"><i
																class="fa fa-eye" aria-hidden="true"></i> view</a>
														</p>

														<div
															id="view-healthAsses<s:property value="%{#status.count}" />"
															class="modal fade" role="dialog">
															<div class="modal-dialog">
																<div class="modal-content">
																	<div class="modal-header">
																		<button type="button" class="close"
																			data-dismiss="modal">&times;</button>
																		<h4 class="modal-title">
																			<s:text name='info.healthAsses' />
																		</h4>
																	</div>
																	<div class="modal-body">
																		<div class="modal-form-item">
																			<label> <s:text name="disabilityType" />
																			</label> <label> <s:property
																					value="%{getCatlogueValueByCode(diabilityType).name}" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="origin" />
																			</label> <label> <s:property
																					value="%{originTypes[origin]}" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="print.remarks" />
																			</label> <label> <s:property value="remark" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="professionalConsultant" />
																			</label> <label> <s:property
																					value="disabilityList[consultationStatus]" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="detailConsultation" />
																			</label> <label> <s:property
																					value="consulatationDetail" />
																			</label>
																		</div>

																	</div>
																</div>
															</div>
														</div>

													</div>
												</s:iterator>




											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farmer.assistiveDevice')}" />
												</p>


												<p class="flexItem" name="farmer.assistiveDeivce">

													<s:if test="farmer.assistiveDeivce==0">
														<s:text name="yes" />
													</s:if>

													<s:else>
														<s:text name="no" />
													</s:else>

												</p>

												<s:if test="farmer.assistiveDeivce==0">

													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.assistiveDeivceName')}" />
													</p>

													<p class="flexItem" name="farmer.assistiveDeivceName">
														<s:property value="farmer.assistiveDeivceName" />
													</p>

												</s:if>

												<s:if test="farmer.assistiveDeivce==1">

													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.assistiveDeviceReq')}" />
													</p>

													<p class="flexItem" name="assistiveDeviceReq">

														<s:if test="farmer.assistiveDeviceReq==0">
															<s:text name="yes" />
														</s:if>

														<s:else>
															<s:text name="no" />
														</s:else>

													</p>

												</s:if>
											</div>

											<div class="dynamic-flexItem2">

												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farmer.healthIssue')}" />
												</p>


												<p class="flexItem" name="farmer.healthIssue">

													<s:if test="farmer.healthIssue==0">
														<s:text name="yes" />
													</s:if>

													<s:else>
														<s:text name="no" />
													</s:else>

												</p>

												<s:if test="farmer.healthIssue==0">


													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.healthIssueDescribe')}" />
													</p>

													<p class="flexItem" name="farmer.healthIssueDescribe">
														<s:property value="farmer.healthIssueDescribe" />
													</p>

												</s:if>

											</div>

										</div> --%>
						<%-- 
										<div class="aPanel social_info irpFormHide"
											style="width: 100%">
											<div class="aTitle">
												<h2>
													<s:text name="info.social" />
													<div class="pull-right">
														<a class="aCollapse" href="#socialInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farmer.homeDiff" />
												</p>
												<p class="flexItem" name="farmer.homeDifficulty">
													<s:property value="farmer.homeDifficulty" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farmer.workDifff" />
												</p>
												<p class="flexItem" name="farmer.workDiffficulty">
													<s:property value="farmer.workDiffficulty" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farmer.communitiyDiff" />
												</p>
												<p class="flexItem" name="farmer.communitiyDifficulty">
													<s:property value="farmer.communitiyDifficulty" />
												</p>
											</div>

										</div>

										<div class="aPanel self_Assesment irpFormHide"
											style="width: 100%">
											<div class="aTitle">
												<h2>
													<s:text name="info.selfAsses" />
													<div class="pull-right">
														<a class="aCollapse" href="#selfAssesInfo"><i
															class="fa fa-chevron-down"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con" id="selfAssesInfo">
												<s:iterator value="farmer.farmerSelfAsses" status="status">
													<div class="dynamic-flexItem2">
														<p class="flexItem bold">
															<s:property
																value="%{getCatlogueValueByCode(activity).name}" />
														</p>
														<p class="flexItem">
															<a href="#" data-toggle="modal"
																data-target="#view-selfAsses<s:property value="%{#status.count}" />"
																class="btn btn-sts linkbtn pull-right"><i
																class="fa fa-eye" aria-hidden="true"></i> view</a>
														</p>
														<div
															id="view-selfAsses<s:property value="%{#status.count}" />"
															class="modal fade" role="dialog">
															<div class="modal-dialog">
																<div class="modal-content">
																	<div class="modal-header">
																		<button type="button" class="close"
																			data-dismiss="modal">&times;</button>
																		<h4 class="modal-title">
																			<s:text name='info.healthAsses' />
																		</h4>
																	</div>

																	<div class="modal-body">
																		<div class="modal-form-item">
																			<label> <s:text name="activity" />
																			</label> <label> <s:property
																					value="%{getCatlogueValueByCode(activity).name}" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="" />
																			</label> <label> <s:property
																					value="disabilityList[value]" />
																			</label>
																		</div>

																		<div class="modal-form-item">
																			<label> <s:text name="print.remarks" />
																			</label> <label> <s:property value="remark" />
																			</label>
																		</div>
																	</div>

																</div>
															</div>
														</div>
													</div>
												</s:iterator>
											</div>
										</div> --%>


						<div class="aPanel loan_info" style="width: 100%">
							<div class="aTitle">
								<h2>
									<s:text name="info.loan" />
									<div class="pull-right">
										<a class="aCollapse" href="#loanInfo"><i
											class="fa fa-chevron-down"></i></a>
									</div>
								</h2>
							</div>

							<div class="aContent dynamic-form-con" id="loanInfo">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farmer.isLoanTakenLastYearProp')}" />
									</p>
									<p class="flexItem" name="farmer.loanTakenLastYear">
										<%-- <s:if test="farmer.loanTakenLastYear==''">
															<s:text name="" />
														</s:if> --%>

										<s:if test="farmer.loanTakenLastYear==1">
											<s:text name="yes" />
										</s:if>

										<s:else>
											<s:text name="no" />
										</s:else>
									</p>
								</div>

								<s:if
									test="farmer.loanTakenLastYear!=0 && farmer.loanTakenLastYear!=2">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmer.loanTakenFromProp')}" />
										</p>
										<p class="flexItem" name="farmer.loanTakenFrom">
											<s:property value="loanTakenFromList[farmer.loanTakenFrom]" />
										</p>
									</div>
								</s:if>

								<s:if test="farmer.loanTakenLastYear==1">

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.amount')}" />
										</p>
										<p class="flexItem" name="farmer.loanAmount">
											<s:property value="farmer.loanAmount" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.purpose')}" />
										</p>
										<p class="flexItem" name="farmer.loanPupose">
											<s:property value="farmer.loanPupose" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.interestRate" />
										</p>
										<p class="flexItem" name="farmer.loanIntRate">
											<s:property value="farmer.loanIntRate" />
										</p>
									</div>

									<div class="dynamic-flexItem2 farmer.loanIntPeriod">
										<p class="flexItem">
											<s:text name="farmer.interestRatePeriod" />
										</p>
										<p class="flexItem" name="farmer.loanIntPeriod">
											<s:property value="farmer.loanIntPeriod" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmer.security')}" />
										</p>
										<p class="flexItem" name="farmer.loanSecurity">
											<s:property value="farmer.loanSecurity" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmer.loanRepaymentAmount')}" />
										</p>
										<p class="flexItem" name="farmer.loanRepaymentAmount">
											<s:property value="farmer.loanRepaymentAmount" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.loanRepaymentDate" />
										</p>
										<p class="flexItem" name="repaymentDate">
											<s:property value="loanRepaymentDate" />
										</p>
									</div>

									<div class="dynamic-flexItem2 hideLoanTakenScheme">
										<p class="flexItem">
											<s:text name="farmer.isLoanTakenScheme" />
										</p>
										<%-- <p class="flexItem" name="isLoanTakenScheme">
															<s:text name='%{"dynamicRadio"+farmer.isLoanTakenScheme}' />
														</p> --%>
										<p class="flexItem" name="isLoanTakenScheme">
											<s:if test='farmer.isLoanTakenScheme=="1"'>
												<s:text name="Yes" />
											</s:if>
											<s:elseif test='farmer.isCropInsured=="0"'>
												<s:text name="No" />
											</s:elseif>
											<s:else>
												<s:text name="" />
											</s:else>
										</p>
										<p class="flexItem" name="farmer.isLoanTakenScheme">
											<s:if test='farmer.isLoanTakenScheme=="1"'>
												<s:text name="Yes" />
											</s:if>
											<s:elseif test='farmer.isCropInsured=="0"'>
												<s:text name="No" />
											</s:elseif>
											<s:else>
												<s:text name="" />
											</s:else>
										</p>


									</div>


									<div class="dynamic-flexItem2 hideLoanTakenScheme ">
										<p class="flexItem" name="farmer.loanTakenScheme">
											<s:text name="farmer.loanTakenScheme" />
										</p>
										<p class="flexItem">
											<s:property value="farmer.loanTakenScheme" />
										</p>
									</div>

								</s:if>

							</div>
						</div>


						<s:if test="farmer.isCertifiedFarmer==1">

							<div class="aPanel houseHold_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:text name="info.houseHoldOccupation" />
										<div class="pull-right">
											<a class="aCollapse" href="#houseHold_info"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="houseHold_info">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.houseOccupationPrimary" />
										</p>
										<p class="flexItem">
											<s:property
												value="houseHoldOccupationList[farmer.houseOccupationPrimary]" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.houseOccupationSecondary" />
										</p>
										<p class="flexItem">
											<s:property
												value="houseHoldOccupationList[farmer.houseOccupationSecondary]" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.familyMember" />
										</p>
										<p class="flexItem">
											<s:property value="familyMemberList[farmer.familyMember]" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.grs" />
										</p>
										<p class="flexItem">
											<s:property value="grsList[farmer.grsMember]" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.paidShareCapitial" />
										</p>
										<p class="flexItem">
											<s:property
												value="paidShareCapitialList[farmer.paidShareCapitial]" />
										</p>
									</div>

								</div>
							</div>

							<div class="aPanel investigator_info" style="width: 100%">
								<div class="aTitle">
									<h2>
										<s:text name="info.investigatorOpinion" />
										<div class="pull-right">
											<a class="aCollapse" href="#investigator_info"><i
												class="fa fa-chevron-down"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="investigator_info">

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.investigatorOpinion" />
										</p>
										<p class="flexItem">
											<s:property value="farmer.investigatorOpinion" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.investigatorDate" />
										</p>
										<p class="flexItem">
											<s:property value="investigatorDate" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farmer.investigatorName" />
										</p>
										<p class="flexItem">
											<s:property
												value="investigatorNameList[farmer.investigatorName]" />
										</p>
									</div>

								</div>

							</div>

						</s:if>


						<%-- <div class="aTitle">
													<h2>
														<s:property
															value="%{getLocaleProperty('farmer.agricultureImplements')}" />
														<div class="pull-right">
															<a class="aCollapse" href="#agricultureImplements"><i
																class="fa fa-chevron-down"></i></a>
														</div>
													</h2>
												</div> --%>



					</div>

				</div>
			</div>
		</div>


	</div>
	<div>
	<s:iterator value="farms">




		<%-- 	<div class="dynamic-flexItem2">
							<p class="flexItem">
								<s:property value="%{getLocaleProperty('farm.farmName')}" />
							</p>
							<p class="flexItem" name="farm.farmName">
								<s:property value="farmName" />
								&nbsp;
							</p>
						</div> --%>
						
	
						
						
						
						
						
						
		<div class="flex-view-layout pdff">
			<div class="fullwidth">
				<div class="flexWrapper ">
					<div class="flexLeft appContentWrapper">
			
						<div class="formContainerWrapper">
							<div class="aPanel farmInfo">
								<div class="aTitle">
									<h2>
										<s:text name="farmdetail" />
										<div class="pull-right">
											<a class="aCollapse" href="#farmDetailsAccordian"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="farmDetailsAccordian">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.farmName')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmName" />
											&nbsp;
										</p>
									</div>

									<s:if test="currentTenantId=='cofBoard'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.photoId')}" />
											</p>
											<p class="flexItem" name="farm.photoId">
												<s:property value="photoId" />
												&nbsp;
											</p>
										</div>
									</s:if>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farm.farmerName" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmer.firstName" />
											&nbsp;
											<s:property value="farmer.lastName" />
										</p>
									</div>

									<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.farmName')}" />
								</p>
								<p class="flexItem" name="farm.farmNameDet">
									<s:property value="farm.farmName" />
									&nbsp;
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.farmerName" />
								</p>
								<p class="flexItem" name="farm.farmNameDet">
									<s:property value="farm.farmer.firstName" />
									&nbsp;
									<s:property value="farm.farmer.lastName" />
								</p>
							</div> --%>

									<div class="dynamic-flexItem2">
										<p class="flexItem surveyNo">
											<s:property value="%{getLocaleProperty('farm.surveyNo')}" />
										</p>
										<p class="flexItem" name="farm.farmDetailedInfo.surveyNumber">
											<s:property value="farmDetailedInfo.surveyNumber" />
										</p>
									</div>
									<s:if test="currentTenantId=='welspun'">

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('fieldStatus')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:if test="farm.farmIcsConv.icsType!=null">
													<s:text name='%{"icsStatus"+farmIcsConv.icsType}' />
												</s:if>
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId=='welspun'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('8A/7/12')}" />
											</p>
											<p class="flexItem"
												name="farm.farmDetailedInfo.processingActivity">
												<s:property
													value="processingActList[farmDetailedInfo.processingActivity]" />
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farm.farmAddress" />
										</p>
										<p class="flexItem" name="farm.farmDetailedInfo.farmAddress">
											<s:property value="farmDetailedInfo.farmAddress" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('farm.totalLand')}" />
											<s:if test="currentTenantId!='symrise'">(
									<s:property value="%{getAreaType()}" />
									)</s:if>

										</p>
										<p class="flexItem"
											name="farm.farmDetailedInfo.totalLandHolding">
											<s:property value="farmDetailedInfo.totalLandHolding" />
										</p>
									</div>
									<s:if test="currentTenantId=='fruitmaster'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="%{getLocaleProperty('farm.totalLand.kanal')}" />

											</p>
											<p class="flexItem" name="totalLandKanalName">
												<span id="totalLandKanal">0.00</span>
											</p>
										</div>



									</s:if>
									<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="%{getLocaleProperty('farm.totalLandHectare')}" />
								</p>
								<p class="flexItem" name="landHectValues">
								<div id="landHectValues"></div>


							</div> --%>

									<div class="dynamic-flexItem2 plantArea pratibhaHide">
										<p class="flexItem">
											<s:text
												name="%{getLocaleProperty('farm.proposedPlantingArea')}" />
											(
											<s:property value="%{getAreaType()}" />
											)
										</p>
										<p class="flexItem"
											name="farm.farmDetailedInfo.proposedPlantingArea">
											<s:property value="farmDetailedInfo.proposedPlantingArea" />
										</p>
									</div>
									<s:if test="currentTenantId=='fruitmaster'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text
													name="%{getLocaleProperty('farm.proposedPlantingArea.kanal')}" />

											</p>
											<p class="flexItem" name="proposedPlantingAreaName">
												<span id="proposedPlantingAreaKanal"></span>
											</p>
										</div>
									</s:if>

									<%-- <div class="dynamic-flexItem2 pratibhaHide">
								<p class="flexItem">
									<s:text
										name="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
								</p>
								<p class="flexItem" name="plantHectValues">
								<div id="plantHectValues"></div>
								</p>

							</div> --%>

									<div class="dynamic-flexItem2 ownLand">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('ownLand')}" />
										</p>
										<p class="flexItem" name="farm.ownLand">
											<s:property value="ownLand" />
										</p>
									</div>
									<s:if test="currentTenantId=='symrise'">
										<div class="dynamic-flexItem2 hide noOfWineOnPlotLabel">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.noOfWineOnPlot')}" />
											</p>
											<p class="flexItem" name="farm.ownLand">
												<s:property value="noOfWineOnPlot" />
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId!='olivado'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('leasedland')}" />
											</p>
											<p class="flexItem" name="farm.leasedLand">
												<s:property value="leasedLand" />
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('irriLand')}" />
										</p>
										<p class="flexItem" name="farm.irrigationLand">
											<s:property value="irrigationLand" />
										</p>
									</div>
									<s:if test="currentTenantId!='welspun'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.landOwned')}" />
											</p>
											<p class="flexItem" name="selectedFarmOwned">
												<s:property value="farmOwnedDetail" />
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.landGradient')}" />
										</p>
										<p class="flexItem" name="selectedGradient">
											<s:property value="landGradientDetail" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.appRoad')}" />
										</p>
										<p class="flexItem" name="selectedRoad">
											<s:property value="selectedRoadString" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.certYear')}" />
										</p>
										<p class="flexItem" name="farm.certYear">
											<s:property value="certYear" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farm.farmCode" />
										</p>
										<p class="flexItem" name="farm.farmCode">
											<s:property value="farmCode" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.topo')}" />
										</p>
										<p class="flexItem" name="farm.landTopology">
											<s:if
												test='farm.landTopology!="-1" || farm.landTopology!="null"|| farm.landTopology!=""'>
												<s:property value="landTopology" />
											</s:if>
										</p>
									</div>

									<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.landGradient')}" />
								</p>
								<p class="flexItem" name="selectedGradient">
									<s:property value="landGradientDetail" />
								</p>
							</div> --%>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farmRegNo')}" />
										</p>
										<p class="flexItem" name="farm.farmRegNumber">
											<s:property value="farmRegNumber" />
										</p>
									</div>
									<s:if
										test="currentTenantId!='simfed' && currentTenantId!='wub'">
										<div class="dynamic-flexItem2 waterHarvest">
											<p class="flexItem">
												<s:text name="farm.waterHarvest" />
											</p>
											<p class="flexItem" name="farm.waterHarvest">
												<s:property value="waterHarvests" />
											</p>
										</div>
										<s:if
											test="currentTenantId!='welspun'&& currentTenantId!='wub'">
											<div class="dynamic-flexItem2 avgStore">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('avgStore')}" />
												</p>
												<p class="flexItem" name="farm.avgStore">
													<s:property value="avgStore" />
												</p>
											</div>
											<div class="dynamic-flexItem2 tree">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('treeName')}" />
												</p>
												<p class="flexItem" name="farm.treeName">
													<s:property value="treeName" />
												</p>
											</div>
										</s:if>
									</s:if>
									<s:if
										test="currentTenantId!='cofBoard' && currentTenantId!='wilmar'">
										<div class="dynamic-flexItem2 distanceProcessingUnit ">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
											</p>
											<p class="flexItem" name="farm.distanceProcessingUnit">
												<s:property value="distanceProcessingUnit" />
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId=='cofBoard'">
										<div class="dynamic-flexItem2 distanceProcessingUnit">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
											</p>
											<p class="flexItem" name="farm.distanceProcessingUnit">
												<s:property value="distanceProcessingUnit" />
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId=='wilmar'">
										<div class="dynamic-flexItem2 tree">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
											</p>
											<p class="flexItem" name="farm.distanceProcessingUnit">
												<s:property value="distanceProcessingUnit" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('processingActivity')}" />
											</p>
											<p class="flexItem"
												name="farm.farmDetailedInfo.processingActivity">
												<s:property
													value="processingActList[farmDetailedInfo.processingActivity]" />
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2 organicStatusDiv">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('organicStatus')}" />
										</p>

										<%-- <p class="flexItem" name="farm.farmIcsConv.organicStatus">
									<s:property value="farm.organicStatus" />
								</p> --%>
										<%-- <p><s:property value="farm.farmIcsConv.organicStatus" /></p> --%>
										<p class="flexItem" name="farm.farmIcsConv.organicStatus">
											<s:if
												test="farmIcsConv.organicStatus==0 ||farmIcsConv.organicStatus==1 || farm.farmIcsConv.organicStatus==2">
												<%-- <s:text name="inprocess" /> --%>
												<s:property value="%{getLocaleProperty('inprocess')}" />
											</s:if>
											<s:else>
												<s:if test="farmIcsConv.organicStatus==3">
													<s:property value="%{getLocaleProperty('alrdyCertified')}" />
												</s:if>
												<s:else>
													<s:property value="%{getLocaleProperty('conversion')}" />
												</s:else>
											</s:else>
										</p>
									</div>

									<s:if test="currentTenantId=='pratibha'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.PlatNo" />
											</p>
											<p class="flexItem" name="farm.farmPlatNo">
												<s:property value="farmPlatNo" />
											</p>
										</div>
									</s:if>

									<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.regYear" />
								</p>
								<p class="flexItem" name="farm.regYear">
									<s:property value="farm.farmDetailedInfo.regYear" />
								</p>
							</div> --%>



								</div>
							</div>

							<div class="aPanel hide contactInfo">
								<div class="aTitle">
									<h2>
										<s:text name="info.contact" />
										<div class="pull-right">
											<a class="aCollapse" href="#farmLocationInfo"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="farmLocationInfo">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('locality.name')}" />
										</p>
										<p class="flexItem" name="selectedLocality">
											<s:property value="village.city.locality.name" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('city.name')}" />
										</p>
										<p class="flexItem" name="selectedCity">
											<s:property value="village.city.name" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('panchayat.name')}" />
										</p>
										<p class="flexItem" name="selectedPanchayat">
											<s:property value="village.gramPanchayat.name" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('village.name')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="village.name" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('samithi.name')}" />
										</p>
										<p class="flexItem" name="selectedSamithi">
											<s:property value="samithi.name" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('fpoGroup')}" />
										</p>
										<p class="flexItem" name="selectedFpo">
											<s:property value="fpo[fpo]" />
										</p>
									</div>

								</div>
							</div>
							<s:if test="currentTenantId!='griffith'">
								<div class="aPanel soilIrrigationInfo">
									<div class="aTitle">
										<h2>
											<s:property value="%{getLocaleProperty('info.soil')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#farmSoilAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con" id="farmSoilAccordian">
										<div class="dynamic-flexItem2 soilType">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.soilType')}" />
											</p>
											<p class="flexItem" name="selectedSoilType">
												<s:property value="soilTypeDetail" />
											</p>
										</div>

										<div class="dynamic-flexItem2 soilTexture">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.soilTexture')}" />
											</p>
											<p class="flexItem" name="selectedTexture">
												<s:property value="soilTextureDetail" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.soilFertilityStatus" />
											</p>
											<p class="flexItem" name="selectedSoilFertility">
												<s:property value="soilFertilityDetail" />
											</p>
										</div>

										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farm.farmIrrigationSource" />
											</p>
											<p class="flexItem" name="selectedIrrigation">
												<s:property value="farmIrrigationDetail" />
											</p>
										</div>
										<%-- <s:if test="farm.farmDetailedInfo.farmIrrigation==2"> --%>
										<div class="dynamic-flexItem2 hideIrrigationType">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.farmIrrigationType')}" />
											</p>
											<p class="flexItem" name="selectedIrrigationSource">
												<s:property value="irrigationSourceDetail" />
											</p>
										</div>
										<s:if test="currentTenantId=='welspun'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
												</p>
												<p class="flexItem" name="farm.NoOfCrotenTrees">
													<s:property value="NoOfCrotenTrees" />
												</p>
											</div>

											<div class="dynamic-flexItem2 tree">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('treeName')}" />
												</p>
												<p class="flexItem" name="farm.treeName">
													<s:property value="treeName" />
												</p>
											</div>
											<div class="dynamic-flexItem2 tree">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
												</p>
												<p class="flexItem" name="farm.distanceProcessingUnit">
													<s:property value="distanceProcessingUnit" />
												</p>
											</div>
											<div class="dynamic-flexItem2 avgStore">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('avgStore')}" />
												</p>
												<p class="flexItem" name="farm.avgStore">
													<s:property value="avgStore" />
												</p>
											</div>
										</s:if>
										<%-- </s:if> --%>
										<s:if test="farm.farmDetailedInfo.irrigationSource==99">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.irrigatedOther" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.irrigatedOther">
													<s:property value="farmDetailedInfo.irrigatedOther" />
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId!='welspun'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.methodOfIrrigation" />
												</p>
												<p class="flexItem" name="selectedMethodOfIrrigation">
													<s:property value="methodOfIrrigationDetail" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.boreWellRechargeStructure')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.boreWellRechargeStructure">
													<s:property
														value="borewellList[farmDetailedInfo.boreWellRechargeStructure]" />
												</p>
											</div>

											<s:if test="currentTenantId=='cofBoard'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('presenceOfBananaTrees')}" />
													</p>
													<p class="flexItem" name="farm.presenceBananaTrees">
														<s:property value="presenceOfBanana" />
													</p>
												</div>
											</s:if>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.inputSource')}" />
												</p>
												<p class="flexItem" name="farm.farmDetailedInfo.inputSource">
													<s:property value="farmDetailedInfo.InputSource" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('activitiesInCoconutFarming')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.activitiesInCoconutFarming">
													<s:property
														value="farmDetailedInfo.activitiesInCoconutFarming" />
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId=='olivado'">

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('8A/7/12')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.processingActivity">
													<s:property
														value="processingActList[farmDetailedInfo.processingActivity]" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.fullTimeWorkersCount">
													<s:property value="farmDetailedInfo.fullTimeWorkersCount" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('presenceOfBananaTrees')}" />
												</p>
												<p class="flexItem" name="farm.presenceBananaTrees">
													<s:property value="presenceOfBanana" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.partTimeWorkersCount')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.partTimeWorkersCount">
													<s:property value="farmDetailedInfo.partTimeWorkersCount" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.seasonalWorkersCount')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.seasonalWorkersCount">
													<s:property value="farmDetailedInfo.seasonalWorkersCount" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.parallelProduction')}" />
												</p>
												<p class="flexItem" name="farm.parallelProd">
													<s:property value="parallelProduction" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
												</p>
												<p class="flexItem"
													name="farm.farmDetailedInfo.lastDateOfChemicalApplication">
													<s:property
														value="farmDetailedInfo.lastDateOfChemicalApplication" />
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.lastDateofOrganicUnit')}" />
												</p>
												<p class="flexItem" name="farm.inputOrganicUnit">
													<s:property value="inputOrganicUnit" />
												</p>
											</div>
										</s:if>

									</div>
								</div>
							</s:if>
							<div class="aPanel landMarkInfo">
								<div class="aTitle">
									<h2>
										<s:text name="farm.landmark" />
										<div class="pull-right">
											<a class="aCollapse" href="#farmLandMarkAccordian"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="labourAccordian">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farm.landmark" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="landmark" />
										</p>
									</div>
								</div>
							</div>

							<div class="aPanel farmLabourInfo">
								<div class="aTitle">
									<h2>
										<s:text name="info.labour" />
										<div class="pull-right">
											<a class="aCollapse" href="#labourAccordian"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con"
									id="farmLandMarkAccordian">

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.fullTimeWorkersCount" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farm.partTimeWorkersCount')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.partTimeWorkersCount" />
										</p>
									</div>
									<s:if test="currentTenantId!='wilmar'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.seasonalWorkersCount')}" />
											</p>
											<p class="flexItem" name="farm.farmName">
												<s:property value="farmDetailedInfo.seasonalWorkersCount" />
											</p>
										</div>
									</s:if>

								</div>
							</div>

							<div class="aPanel conversionInfo">
								<div class="aTitle">
									<h2>

										<s:property value="%{getLocaleProperty('info.conversion')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#farmConversionAccordian"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con"
									id="farmConversionAccordian">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="farm.lastDateofChemicalApply" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property
												value="farmDetailedInfo.lastDateOfChemicalApplication" />
										</p>
									</div>
									<s:if test="currentTenantId=='welspun' ">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('farm.landOwned')}" />
											</p>
											<p class="flexItem" name="selectedFarmOwned">
												<s:property value="farmOwnedDetail" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.waterSourceList')}" />
											</p>
											<p class="flexItem" name="farm.waterSource">
												<s:property value="selectedWaterSource" />
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId!='iccoa' && currentTenantId!='susagri'">
										<s:if test="currentTenantId!='welspun' ">
											<s:if test="currentTenantId!='livelihood'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.conventionalLands')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farmDetailedInfo.conventionalLand" />
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.fallowLand')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farmDetailedInfo.fallowOrPastureLand" />
													</p>
												</div>
											</s:if>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.conventionalCrops')}" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:property value="farmDetailedInfo.conventionalCrops" />
												</p>
											</div>
										</s:if>
										<s:if
											test="currentTenantId!='simfed' && currentTenantId!='cofBoard' && currentTenantId!='wub'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farm.conventionalEstimatedYields')}" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:property
														value="farmDetailedInfo.conventionalEstimatedYield" />
												</p>
											</div>
										</s:if>
									</s:if>
								</div>
							</div>

							<div class="aPanel fieldHistoryInfo">
								<div class="aTitle">
									<h2>

										<s:property value="%{getLocaleProperty('info.conversions')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#fieldInfoAccordian"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con" id="fieldInfoAccordian">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.fieldName')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.fieldName" />
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.fieldCrop')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.fieldCrop" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.fieldArea')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.fieldArea" />
										</p>
									</div>


									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.inputApplied')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.inputApplied" />
										</p>
									</div>

									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property value="%{getLocaleProperty('farm.quantity')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.quantityApplied" />
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
										</p>
										<p class="flexItem" name="farm.farmName">
											<s:property value="farmDetailedInfo.lastDateOfChemicalString" />
										</p>
									</div>
								</div>
							</div>


							<s:if test="currentTenantId!='livelihood'">
								<div class="aPanel conversionStatus" style="width: 100%">
									<div class="aTitle">
										<h2>
											<s:property value="%{getLocaleProperty('info.ics')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#farmICSAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent " id="farmConversionAccordian">
										<s:iterator value="farmICSConversion">
											<s:if test="currentTenantId!='welspun'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="%{getLocaleProperty('conversionStatus')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:if test="icsType!=null">
															<s:text name='%{"icsStatus"+icsType}' />
														</s:if>
													</p>
												</div>
											</s:if>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farminspectionDate')}" />
												</p>
												<p class="flexItem" name="farm.farmName">

													<td><s:property value="inspectionDateString" /></td>
												</p>
											</div>
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="nameOfInspector" />
												</p>
												<p class="flexItem" name="farm.farmName">

													<s:property value="inspectorName" />
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="certType" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:property value="scope" />

													<%-- <s:property value="farm.farmIcsConv.scope" /> --%>
												</p>
											</div>

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('qualified')}" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:text name='%{"inspe"+qualified}' />

													<%-- <s:if test="icsType!=null">
										<s:text name='%{"inspe"+qualified}' />
									</s:if> --%>
												</p>
											</div>
											<s:iterator value="farm.farmICSConversion">
												<s:if test='qualified=="2"'>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('sanctionReason')}" />
														</p>
														<p class="flexItem" name="farm.farmName">

															<s:property value="sanctionreason" />
														</p>
													</div>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('durationOfSanction')}" />
														</p>
														<p class="flexItem" name="farm.farmName">

															<s:property value="sanctionDuration" />
														</p>
													</div>
												</s:if>
											</s:iterator>
										</s:iterator>
									</div>
								</div>
							</s:if>

							<s:if test="currentTenantId=='olivado'">


								<div class="aPanel auditInfo">
									<div class="aTitle">
										<h2>
											<s:text name="info.auditDetails" />
											<div class="pull-right">
												<a class="aCollapse" href="#auditDetailsAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con"
										id="auditDetailsAccordian">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.presenceHiredLabour')}" />
											</p>
											<p class="flexItem" name="farm.presenceHiredLabour">
												<s:property value="hiredLabours" />
											</p>
										</div>
										<%-- 				<div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.riskCategory')}" />
									</p>
									<p class="flexItem" name="farm.riskCategory">
										<s:property value="riskCategory" />
									</p>
								</div> --%>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('dateOfInternalAudit')}" />
											</p>
											<p class="flexItem" name="farm.dateOfInternalAudit">
												<s:property value="farmIcsConv.inspectionDateString" />
											</p>
										</div>
										<%-- 			<div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('nameOfInspector')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.inspectorName">
										<s:property value="farm.farmIcsConv.inspectorName" />
									</p>
								</div> --%>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('producerStatus')}" />
											</p>
											<p class="flexItem" name="farm.farmIcsConv.scope">
												<s:property value="scopeName" />
											</p>
										</div>
										<%-- <div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('status')}" />
									</p>
									<p class="flexItem" name="farm.farmIcsConv.icsType">
										<s:text name='%{"icsStatus"+farm.farmIcsConv.icsType}' />
									</p>
								</div> --%>
										<%-- <div class="dynamic-flexItem2 hide">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.leasedLand')}" />
									</p>
									<p class="flexItem" name="farm.leasedLand">
										<s:property value="farm.leasedLand" />
									</p>
								</div> --%>
									</div>
								</div>


								<div class="aPanel treeDetailsInfo">
									<div class="aTitle">
										<h2>
											<s:property value="%{getLocaleProperty('info.treeDetails')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#treeDetailsAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.treeDetails')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#treeDetailsAccordian"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="treeDetailsAccordian">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">
									<tr class="odd">
										<%-- <th width="5%"><s:text name="s.no" /></th> --%>
										<th width="30%"><s:text name="prodStatus" /></th>
										<th colspan=""><s:text name="cultivar" /></th>
										<th colspan=""><s:text name="years" /></th>
										<th colspan=""><s:text name="noOfTrees" /></th>
									</tr>
									<s:if test="treeDetails.size()>0">
										<s:iterator value="treeDetails">
											<tr class="odd">

												<th><s:property value="prodStatus" />
													<th><s:property value="variety" />
											
												<th><s:property value="years" />
											
												<th><s:property value="noOfTrees" />	
									
											</tr>
								</s:iterator>
							</s:if>
							
							<s:else>
								<tr>
									<td colspan="6" align="center"><s:text
													name="nomenu.assigned" /></td>
								</tr>
							</s:else>
						</table>
					</div>
				</div>
								</div>
								<div class="aPanel treeDetailsInfo">
									<div class="aTitle">
										<h2>
											<s:property
												value="%{getLocaleProperty('info.treeInformation')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#treeDetailAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con" id="treeDetailAccordian">
										<table width="95%" cellspacing="0"
											style="table-layout: fixed;"
											class="table table-bordered aspect-detail mainTable">
											<tr class="odd">
												<%-- <th width="5%"><s:text name="s.no" /></th> --%>
												<th colspan=""><s:text name="cultivar" /></th>
												<th colspan=""><s:text name="noOfTrees" /></th>
											</tr>

											<s:if test="treeDetailss.size()>0">
												<s:iterator value="treeDetailss">
													<tr class="odd">

														<th><s:property value="variety" />
														<th><s:property value="noOfTrees" />
													</tr>
												</s:iterator>
											</s:if>
										</table>
									</div>

								</div>
								<div class="aPanel treeCalcInfo">
									<div class="aTitle">
										<h2>
											<s:property
												value="%{getLocaleProperty('info.treeCalcDetails')}" />
											<div class="pull-right">
												<a class="aCollapse" href="#treeCalcDetailsAccordian"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="aContent dynamic-form-con"
										id="treeCalcDetailsAccordian">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.totalOrganicTrees')}" />
											</p>
											<p class="flexItem" name="farm.totalOrganicTrees">
												<s:property value="totalOrganicTrees" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.totalConventionalTrees')}" />
											</p>
											<p class="flexItem" name="farm.totalConventionalTrees">
												<s:property value="totalConventionalTrees" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.totalAvocadoTrees')}" />
											</p>
											<p class="flexItem" name="farm.totalAvocadoTrees">
												<s:property value="totalAvocadoTrees" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.hectarOrganicTrees')}" />
											</p>
											<p class="flexItem" name="farm.hectarOrganicTrees">
												<s:property value="hectarOrganicTrees" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.hectarConventionalTrees')}" />
											</p>
											<p class="flexItem" name="farm.hectarConventionalTrees">
												<s:property value="hectarConventionalTrees" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farm.hectarAvocadoTrees')}" />
											</p>
											<p class="flexItem" name="farm.hectarAvocadoTrees">
												<s:property value="hectarAvocadoTrees" />
											</p>
										</div>

									</div>
								</div>

								<%-- 
						<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.orgVariety')}" />
									<div class="pull-right">
										<a class="aCollapse" href="orgVartyAccordn"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="orgVartyAccordn">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">

									<s:if test="orgnaicVarietyList.size()>0">
										<s:iterator value="orgnaicVarietyList">
											<tr class="odd">

												<th><s:property value="fieldName" /> <th><s:property value="fieldValue" />
											
											</tr>
										</s:iterator>
									</s:if>
								</table>
							</div>

						</div>
						
						<div class="aPanel treeDetailsInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.convenVariety')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#convenVartyAccrd"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con" id="convenVartyAccrd">
								<table width="95%" cellspacing="0" style="table-layout: fixed;"
									class="table table-bordered aspect-detail mainTable">
									
									<s:if test="conventionalVarietyList.size()>0">
										<s:iterator value="conventionalVarietyList">
											<tr class="odd">

												<th><s:property value="fieldName" />
												
												<th><s:property value="fieldValue" />
											
											</tr>
										</s:iterator>
									</s:if>
					</table>
					</div>
					</div> --%>




							</s:if>



							<s:if test="enableSoliTesting==1">
								<div class="aPanel soilTesting" style="width: 100%">
									<div class="aTitle">
										<h2>
											<s:text name="info.soilTesting" />
											<div class="pull-right">
												<a class="aCollapse" href="#soilTestAcc"><i
													class="fa fa-chevron-right"></i></a>
											</div>
										</h2>
									</div>
									<div class="dynamic-flexItem">
										<p class="flexItem">
											<s:text name="soliTesting" />
										</p>
										<p class="flexItem">
											<s:if test='farm.soilTesting=="1" || farm.soilTesting=="2" '>
												<s:text name='%{"SOILTEST"+soilTesting}' />
											</s:if>
											<s:else>
												<s:property value="soilTesting" />
											</s:else>
										</p>
									</div>
									<s:if test='farm.soilTesting=="1"'>
										<s:if test="farm.docUpload.size()>0">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="fileName" />
												</p>
												<s:iterator value="farm.docUpload">
													<p class="flexItem" name="name">
														<s:property value="name" />
													</p>
												</s:iterator>
											</div>

											<div>
												<p class="flexItem">
													<s:text name="download" />
												</p>
												<s:iterator value="farm.docUpload">
													<button type="button" class="btn btn-default"
														aria-label="Left Align"
														onclick='downloadDocument(<s:property value="id" />)'>
														<span class="glyphicon glyphicon-download-alt"
															aria-hidden="true"></span>
													</button>
												</s:iterator>
											</div>
										</s:if>
									</s:if>
									<%-- <s:if test='farm.soilTesting=="1"'>
								<s:if test="farm.docUpload.size()>0">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="fileName" />
										</p>
										<p class="flexItem">
											<s:text name="download" />
										</p>
									</div>
									<s:iterator value="farm.docUpload">
										<tr>
											<td align="left"><s:property value="name" /></td>
											<td align="center">&nbsp;&nbsp;
												<button type="button" class="btn btn-default"
													aria-label="Left Align"
													onclick='downloadDocument(<s:property value="id" />)'>
													<span class="glyphicon glyphicon-download-alt"
														aria-hidden="true"></span>
												</button>

											</td>
										</tr>
									</s:iterator>
								</s:if>
							</s:if> --%>
								</div>
							</s:if>
						</div>
						<!-- <div class="dynamicFieldsRender"></div> -->
					</div>

					<div class="flexRight appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<s:if test="getCurrentTenantId()!='olivado'">
								<h2>
									<s:property value="%{getLocaleProperty('farm.photo')}" />
								</h2>
								<div id="" class="farmer-photo">
									<s:if test='farmImageFileName!=null && farmImageFileName!=""'>
										<img border="0"
											src="data:image/png;base64,<s:property value="farmImageFileName"/>">
									</s:if>
									<s:else>
										<img align="middle" border="0" src="img/no-image.png">
									</s:else>
								</div>
							</s:if>

							<div class="clear"></div>
							<s:hidden id="farmCordinates" value="%{farmJsonObjectList}" />
							<h2>
								<s:text name="info.digitalLocator" />
							</h2>
							<%-- <s:if test="latitude!=''|| longitude!=''"> --%>
							<div>
								<div>
									<img src="img/red_placemarker.png" width="32" height="32">
									<s:property value="%{getLocaleProperty('conventional')}" />
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img
										src="img/yellow_placemarker.PNG" width="32" height="32">
									<s:property value="%{getLocaleProperty('inconversion')}" />
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img
										src="img/green_placemarker.png" width="32" height="32">
									<s:property value="%{getLocaleProperty('organic')}" />
								</div>

							</div>
							<%-- </s:if> --%>
							<div id="<s:property value="id"/>" class="smallmap map"
								style="height: 250px"></div>

							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.latitude" />
								</p>
								<p class="flexItem">
									<s:property value="latitude" />
								</p>
							</div>
							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.longitude" />
								</p>
								<p class="flexItem">
									<s:property value="longitude" />
								</p>
							</div>

							<s:if
								test="currentTenantId=='pratibha' && farm.coordinates.size()>0">
								<div class="flexItem flex-layout flexItemStyle">
									<p class="flexItem">
										<s:text name="farm.time" />
									</p>
									<p class="flexItem">
										<s:date name="plotCapturingTime" format="dd/MM/yyyy" />
									</p>
								</div>
							</s:if>


						</div>

					</div>

				</div>

			</div>

		</div>
	</s:iterator>
	</div>
	<div>
	<s:iterator value="farmCrop">
		<div class="flex-view-layout pdff">
			<div class="fullwidth">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper">
						<div class="formContainerWrapper">
							<div class="aPanel farmInfo">
								<div class="aTitle">
									<h2>
										<s:property value="%{getLocaleProperty('info.farmCrops')}" />
										<div class="pull-right">
											<a class="aCollapse" href="#"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmcrops.farmName')}" />
										</p>
										<p class="flexItem" name="farmcrops.farmName">
											<s:property value="farm.farmName" />
										</p>
									</div>

									<s:if test="currentTenantId!='griffith'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmcrops.cropCategory.prop" />
											</p>
											<p class="flexItem" name="farmcrops.cropCategory.prop">
												<s:text name="cs%{cropCategory}" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:property
												value="%{getLocaleProperty('farmfarmcrops.cropSeason')}" />
										</p>
										<p class="flexItem" name="farmfarmcrops.cropSeason">
											<s:property value="cropSeason.name" />
											&nbsp;
										</p>
									</div>


									<s:if test="cropInfoEnabled==1">
										<s:if
											test="currentTenantId !='chetna' && currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId !='crsdemo'										
										&& currentTenantId !='welspun' && currentTenantId!='griffith' && currentTenantId!='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farmCrops.CultivationType" />
												</p>
												<p class="flexItem">
													<s:property value="cropCategoryList" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('cropName')}" />

										</p>
										<p class="flexItem"
											name="farmCrops.procurementVariety.procurementProduct.name">
											<s:property
												value="procurementVariety.procurementProduct.name" />
											&nbsp;
										</p>
									</div>
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="variety" />
										</p>
										<p class="flexItem" name="farmCrops.procurementVariety.name">
											<s:property value="procurementVariety.name" />
											&nbsp;
										</p>
									</div>
									<s:if test="currentTenantId=='griffith'">
										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('prodTrees')}" />
											</p>
											<p class="flexItem">
												<s:property value="prodTrees" />

											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId =='kpf'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('cultiArea')}" />
											</p>
											<p class="flexItem">
												<s:property value="cultiArea" />
												&nbsp;
											</p>
										</div>
										<div class="dynamic-flexItem2" id="sowDate">
											<p class="flexItem">
												<s:text name="farmcrops.sowingDate" />
											</p>
											<p class="flexItem">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId!='ecoagri'">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('cultiArea')}" />
													(
													<s:property value="%{getAreaType()}" />
													)
												</p>
												<p class="flexItem" name="farmCrops.cultiArea">
													<s:property value="cultiArea" />
													&nbsp;
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId =='fruitmaster'">

											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('cultiAreaKanal')}" />
												</p>
												<p class="flexItem" name="farmCrops.cultiArea">
													<span id="cultiAreaKanal"></span> &nbsp;
												</p>
											</div>
										</s:if>
										<s:if test="currentTenantId!='livelihood'">
											<div class="dynamic-flexItem2" id="sowDate">
												<p class="flexItem">
													<s:property
														value="%{getLocaleProperty('farmcrops.sowingDate')}" />
													<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)
													</s:if>
												</p>
												<p class="flexItem">
													<s:property value="sowingDate" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>


									<s:if
										test="currentTenantId !='chetna' && currentTenantId !='iccoa' && currentTenantId !='welspun' && currentTenantId !='wilmar' && currentTenantId !='ecoagri' && currentTenantId!='livelihood'">
										<s:if test="farmCrops.cropCategory == 0">
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('type')}" />
												</p>
												<p class="flexItem" name="farmCrops.type">
													<s:property value="type" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId !='simfed' && currentTenantId !='wub'  && currentTenantId!='movcd' && currentTenantId!='crsdemo' 
									&& currentTenantId !='gar' && currentTenantId !='welspun'&& currentTenantId !='wilmar' && currentTenantId!='griffith' && currentTenantId!='livelihood'">
										<div class="dynamic-flexItem2 seedSource">
											<p class="flexItem">
												<s:text name="seedSource" />
											</p>
											<p class="flexItem">
												<s:property value="seedSource" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<%-- 						<s:if test='currentTenantId=="iccoa"'>
									<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
											<s:property value="%{getLocaleProperty('satbleLength')}" />
												
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if> --%>
									<s:if
										test="currentTenantId!='pratibha' &&currentTenantId !='gsma' &&currentTenantId !='pgss' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
									&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar'&& currentTenantId !='welspun' 
									&& currentTenantId!='simfed'&& currentTenantId!='wub' && currentTenantId!='ecoagri' && currentTenantId!='livelihood' && currentTenantId!='ocp' && currentTenantId!='susagri'">
										<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('satbleLength')}" />
											</p>
											<p class="flexItem">
												<s:property value="stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId !='gsma'  && currentTenantId !='simfed' && currentTenantId !='wub' && currentTenantId!='movcd' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
										&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId !='griffith'
										&& currentTenantId !='ecoagri' && currentTenantId!='livelihood'">
										<div class="dynamic-flexItem2" id=seedQtyUsed>
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyUsed')}" />
											</p>
											<p class="flexItem">
												<s:property value="seedQtyUsed" />
												&nbsp;
											</p>
										</div>
										<s:if test="currentTenantId!='ocp'">
											<div class="dynamic-flexItem2" id=seedQtyUsed>
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('seedQtyCost')}" />
												</p>
												<p class="flexItem">
													<s:property value="seedQtyCost" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer' ||  currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="riskAssesment">
											<p class="flexItem">
												<s:text name="farmcrops.riskAssesment" />
											</p>
											<p class="flexItem">
												<s:text name="rs%{riskAssesment}" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="captureAssessment">
											<p class="flexItem">
												<s:text name="farmcrops.riskBufferZoneDistanse" />
											</p>
											<p class="flexItem">
												<s:property value="riskBufferZoneDistanse" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer'|| currentTenantId=='mhr' || currentTenantId=='chetna'">
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:text name="farmCrops.seedTreatmentDetails" />
											</p>
											<s:if test="farmCrops.seedTreatmentDetails=='99'">
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
													-
													<s:property value="otherSeedTreatmentDetails" />
													</td>
												</p>
											</s:if>
											<s:else>
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
												</p>
											</s:else>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId =='pratibha'">
											<div class="dynamic-flexItem2 ">
												<p class="flexItem">
													<s:text name="farmfarmcrops.estimatedYeild.quintals" />
												</p>
												<p class="flexItem" id="estimatedYieldQuintal"></p>
											</div>
											<div class="dynamic-flexItem2 " id="harvestDat">
												<p class="flexItem">
													<s:text name="harvestDate" />
													(DD-MM-YYYY)
												</p>
												<p class="flexItem">
													<s:property value="harvestDate" />

												</p>
											</div>
										</s:if>
										<s:else>
											<s:if
												test="currentTenantId !='welspun' && currentTenantId!='livelihood'">
												<div class="dynamic-flexItem2 ">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" />
													</p>
													<p class="flexItem" name="farmCrops.estYldSfx">
														<s:property value="estimatedYield" />

													</p>
												</div>
												<s:if
													test="currentTenantId!='griffith' && currentTenantId!='simfed' && currentTenantId!='ecoagri' && currentTenantId!='livelihood'">
													<div class="dynamic-flexItem2 ">
														<p class="flexItem">
															<s:text name="farmfarmcrops.estimatedYeild.tonnes" />
														</p>
														<p class="flexItem"
															name="farmfarmcrops.estimatedYeild.tonnes"
															"id="estimatedYieldMetTon"></p>


													</div>
													<s:if test="currentTenantId!='ocp'">
														<div class="dynamic-flexItem2 " id="harvestDat">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmcrops.harvestDate')}" />
																<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)</s:if>
															</p>
															<p class="flexItem">
																<s:property value="harvestDate" />

															</p>
														</div>
													</s:if>
												</s:if>
											</s:if>

										</s:else>


									</s:if>

									<s:if test="currentTenantId=='livelihood'">
										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('noOfTrees')}" />
											</p>
											<p class="flexItem" name="farmCrops.noOfTrees">
												<s:property value="noOfTrees" />

											</p>
										</div>
									</s:if>

									<s:if test="currentTenantId=='wilmar'">

										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('noOfTrees')}" />
											</p>
											<p class="flexItem" name="farmCrops.noOfTrees">
												<s:property value="noOfTrees" />

											</p>
										</div>

										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('prodTrees')}" />
											</p>
											<p class="flexItem" name="farmCrops.prodTrees">
												<s:property value="prodTrees" />

											</p>
										</div>
										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('affTrees')}" />
											</p>
											<p class="flexItem" name="farmCrops.affTrees">
												<s:property value="affTrees" />

											</p>
										</div>


										<div class="dynamic-flexItem2 ">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('yearOfPlanting')}" />
											</p>
											<p class="flexItem" name="farmCrops.interAcre">
												<s:property value="interAcre" />

											</p>
										</div>
										
										<div class="dynamic-flexItem2 ">
						<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
						<p class="flexItem" name="farmCrops.interAcre">
							<s:property value="latitude" />
						</p>
					</div>
					<div class="dynamic-flexItem2 ">
						<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
						<p class="flexItem" name="farmCrops.interAcre">
							<s:property value="longitude" />
						</p>
					</div>
										
									</s:if>
								</div>


								<!-- <div class="dynamicFieldsRender"></div> -->
							</div>












						</div>
					</div>


					<%-- <div class="flexRight appContentWrapper">
						<div class="formContainerWrapper dynamic-form-con">
							<s:hidden id="farmCropCordinates" value="%{jsonObjectList}" />
							<table class="table table-bordered aspect-detail fillform">
								<h2>
									<s:text name="info.digitalLocator" />
								</h2>
							
	
<div id="<s:property value="id"/>" class="smallmap map"
								style="height: 200px"></div>
											
							
								

								<tr>
									<div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.latitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.latitude" />
						</p>
					</div><div class="flexItem flex-layout flexItemStyle">
						<p class="flexItem">
							<s:text name="farm.longitude" />
						</p>
						<p class="flexItem">
							<s:property value="farmCrops.farm.longitude" />
						</p>
					</div>
									
									
									</tr>

							</table>
							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.latitude" />
								</p>
								<p class="flexItem">
									<s:property value="farmCrops.farm.latitude" />
								</p>
							</div>
							<div class="flexItem flex-layout flexItemStyle">
								<p class="flexItem">
									<s:text name="farm.longitude" />
								</p>
								<p class="flexItem">
									<s:property value="farm.longitude" />
								</p>
							</div>

							<s:if
								test="currentTenantId=='pratibha' && farmCrops.farmCropsCoordinates.size()>0">
								<div class="flexItem flex-layout flexItemStyle">
									<p class="flexItem">
										<s:text name="%{getLocaleProperty('farmCrops.time')}" />
									</p>
									<p class="flexItem">
										<s:date name="plotCapturingTime" format="dd/MM/yyyy" />
									</p>
								</div>
							</s:if>

						</div>
					</div> --%>


				</div>
			</div>
		</div>
	</s:iterator>
</div>
<s:if test="currentTenantId=='olivado'">
<div>
	<div class="flex-view-layout pdff">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
						<div class="aPanel farmInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.animal')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con"></div>

<table id="tableTemplate11" class="table table-bordered aspect-detail"
	style="margin-top: 2%">
	<thead>
		<tr class="border-less">
			<th class="hide">Id</th>
			<%-- <th><s:text name="title.farm"></s:text></th> --%>
			<th><s:text name="animalHusbandary.farmAnimal"></s:text></th>
			<th><s:text name="animalHusbandary.animalCount"/></th>
			<s:if test="currentTenantId!='olivado'">
			<th><s:text name="animalHusbandary.animalCount1"/></th>
			<th><s:text name="animalHusbandary.animalHousing"/></th>
			<%-- <th><s:text name="animalHusbandary.animrev"></s:text></th> --%>
			<th><s:property value="%{getLocaleProperty('animalHusbandary.animrev')}" /></th>
			<th><s:text name="animalHusbandary.breed"></s:text></th>
			</s:if>
			<s:if test="currentTenantId=='chetna'">
			<th><s:text name="animalHusbandary.manureCollected"/></th>
			<th><s:text name="animalHusbandary.urineCollected"/></th>
			</s:if>
			
		</tr>
	</thead> 
	<tbody id="tBodyTemplate11">
	</tbody>
</table>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>




<div>
	<div class="flex-view-layout pdff">
		<div class="fullwidth">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper">
					<div class="formContainerWrapper">
						<div class="aPanel farmInfo">
							<div class="aTitle">
								<h2>
									<s:property value="%{getLocaleProperty('info.farmHistory')}" />
									<div class="pull-right">
										<a class="aCollapse" href="#"><i
											class="fa fa-chevron-right"></i></a>
									</div>
								</h2>
							</div>
							<div class="aContent dynamic-form-con"></div>
<table id="farmHistoryDetail1"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</s:if>
<script>
 
function loadFarmHistoryGridPdf(){

    jQuery("#farmHistoryDetail1").jqGrid(
    {
    url:'farmHistory_data.action?id='+ document.getElementById('farmerId').value,
            //pager: '#farmHistoryPagerForDetail',
            mtype: 'POST',
            datatype: "json",
                   colNames:[
							<s:if test="currentTenantId=='awi'">
								'<s:text name="year"/>',
							</s:if>
							<s:else>
                            	'<s:text name="year"/>',
                            </s:else>
							'<s:property value="%{getLocaleProperty('crops')}" />',
							
							'<s:property value="%{getLocaleProperty('farmerLandDetails.seedlings')}" />',
							'<s:property value="%{getLocaleProperty('estimatedAcreage')}" />',
							'<s:property value="%{getLocaleProperty('farmerLandDetails.noOfTrees')}" />',
							'<s:property value="%{getLocaleProperty('pestdiseases')}" />',
							'<s:property value="%{getLocaleProperty('pestdiseasesControl')}" />',

            ],
            colModel:[
			<s:if test="currentTenantId=='awi'">
				{name:'year', index:'year', width:125, sortable:true},
			</s:if>
			<s:else>
	            {name:'year', index:'year', width:125, sortable:true},
	        </s:else>
	            {name:'crops', index:'crops', width:125, sortable:true},
	            {name:'seedlings', index:'seedlings', width:125, sortable:true},
	            {name:'estimatedAcreage', index:'estimatedAcreage', width:125, sortable:true},
	            {name:'noOfTrees', index:'noOfTrees', width:125, sortable:true},
	            {name:'pestdiseases', index:'pestdiseases', width:125, sortable:true},
	            {name:'pestdiseasesControl', index:'pestdiseasesControl', width:125, sortable:true}
                    //{name:'fdi.regYear',index:'fdi.regYear',width:125,sortable:true},
            ],
            height: 301,
            width: $("#baseDiv").width(), // assign parent div width
            scrollOffset: 0,
            rowNum:10,
            rowList : [10, 25, 50],
            sortname:'id',
            sortorder: "desc",
            viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
            beforeSelectRow:
            function(rowid, e) {
            var iCol = jQuery.jgrid.getCellIndex(e.target);
            if (iCol >= 4){return false; }
            else{ return true; }
            },

            onSortCol: function (index, idxcol, sortorder) {
            if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
                    && this.p.colModel[this.p.lastsort].sortable !== false) {
            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
            }
            }
    });
   // jQuery("#farmHistoryDetail1").jqGrid('navGrid', '#farmHistoryPagerForDetail', {edit:false, add:false, del:false, search:false, refresh:true}) // enabled refresh for reloading grid
            //jQuery("#farmHistoryDetail1").jqGrid('filterToolbar', {stringResult: true, searchOnEnter : false}); // enabling filters on top of the header.

/*     colModel = jQuery("#farmHistoryDetail1").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#farmHistoryDetail1")[0].id) +
            ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
    var cmi = colModel[i], colName = cmi.name;
    if (cmi.sortable !== false) {
    $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
    } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
    $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
    }
    }); */
    windowHeight = windowHeight-100;
    $('#farmHistoryDetail1').jqGrid('setGridHeight',(windowHeight));
    }
function refreshAnimalTemplateList11(){
	
	 $('#tBodyTemplate11').empty();
	 $('#temp').empty();
	 //var farmId = $("#farm").val();
	
	 $('#temp').append("<option value=''>Select Template</option>");
	 
	 var farmerId = "<s:property value='farmer.id'/>";
	
	 $.getJSON('animalHusbandary_populateAnimalInventryList.action',{id:farmerId},function(jd){
		
		 var temps=jd.data;
		 var bodyContent='';
		 for(var i=0;i<temps.length;i++){
			 var temp=temps[i];
			 bodyContent+='<tr>';
			 bodyContent+='<td class="hide">'+temp.id+'</td>';
			/*  bodyContent+='<td align="center">'+temp.farmName+'</td>'; */
			 bodyContent+='<td align="center">'+temp.item1+'</td>';
			 bodyContent+='<td align="center">'+temp.cout+'</td>';
			 <s:if test="currentTenantId!='olivado'">
			 bodyContent+='<td align="center">'+temp.item2+'</td>';
			 bodyContent+='<td align="center">'+temp.item3+'</td>';
			 //bodyContent+='<td align="center">'+temp.cout+'</td>';
			bodyContent+='<td align="center">'+temp.item4+'</td>';
			bodyContent+='<td align="center">'+temp.item23+'</td>';
			</s:if>
			<s:if test="currentTenantId=='chetna'">
			bodyContent+='<td align="center">'+temp.manureCollected+'</td>';
			bodyContent+='<td align="center">'+temp.urineCollected+'</td>';
			</s:if>
			 
			 bodyContent+='</tr>';
			 $('#temp').append("<option value='"+temp.id+"'>"+temp.inventoryItem1+temp.inventoryItem2+temp.inventoryItem3+temp.inventoryItem4+"</option>");
		}
		 
		 $('#tBodyTemplate11').html(bodyContent);
		 $('#temp').val('');
	 });
}
 
			function initMap() {
				 var farmerId='<s:property value="farmer.id" />';
				 var isCert = '<s:property value="farmer.isCertifiedFarmer" />';
					jQuery.post("farmer_populateFarm.action", {id:farmerId}, function(result) {
						 var farm = jQuery.parseJSON(result);
						$.each(farm, function(index, value) {
							$(value).each(function(k,v){
								 map = new google.maps.Map(document.getElementById(v.id), {
										center : {
											lat : 10.4859,
											lng :  124.7614
										},
										zoom : 3,
										mapTypeId: google.maps.MapTypeId.HYBRID,
									}); 
								//loadFarmMap(isCert);
				try {
					var farmCoordinate = jQuery("#farmCordinates").val();
					var landArea = JSON.parse(farmCoordinate);
				 	loadMap('map', v.lat,
							v.lon, landArea,isCert,v.icsType); 
					//addExistingMarkers(landArea);
				} catch (err) {
					console.log(err);
				}
						});
					}); 
					});
				/*  map = new google.maps.Map(document.getElementById('map1463'), {
					center : {
						lat : 11.0168,
						lng : 76.9558
					},
					zoom : 3,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				}); 
				 map = new google.maps.Map(document.getElementById('map1465'), {
						center : {
							lat : 11.0168,
							lng : 76.9558
						},
						zoom : 3,
						mapTypeId: google.maps.MapTypeId.HYBRID,
					});  */

			}
			
			
			function loadMap(mapDiv, latitude, longitude, landArea,isCert,icsType) {
		
				var url = window.location.href;
				var temp = url;
				for (var i = 0; i < 1; i++) {
					temp = temp.substring(0, temp.lastIndexOf('/'));
				}
				var intermediateImg;
				 var intermediatePointIcon;
				 if(isCert==0){
						intermediateImg = "red_placemarker.png";
						 intermediatePointIcon = temp + '/img/'+intermediateImg;
					}else{
				 		if(icsType!=null &&icsType!='' ){
							if(isCert==1 && icsType==3 ) {
								intermediateImg = "green_placemarker.png";
								 intermediatePointIcon = temp + '/img/'+intermediateImg;
							}else {
							
								intermediateImg = "yellow_placemarker.PNG";
								 intermediatePointIcon = temp + '/img/'+intermediateImg;
							}
						} 
						
						
					}
				
				try {
					var bounds = new google.maps.LatLngBounds();
					if(!isEmpty(latitude)&&!isEmpty(longitude)){
						var marker;
						marker = new google.maps.Marker({
							position : new google.maps.LatLng(parseFloat(latitude),
									parseFloat(longitude)),
									icon:intermediatePointIcon,
							map : map
						});
					}
					
					
					
					if(landArea.length>0){
						var cords = new Array();
						$(landArea).each(function(k,v){
							if(v.lat != undefined && v.lon != undefined){
								cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
							}
							
						/* 	marker = new google.maps.Marker({
								position : new google.maps.LatLng(parseFloat(v.lat),
										parseFloat(v.lon)),
								map : map
							}); */
						});
						
						
						 var plotting = new google.maps.Polygon({
					          paths: cords,
					          strokeColor: '#FF0000',
					          strokeOpacity: 0.8,
					          strokeWeight: 2,
					          fillColor: '#E7D874',
					          fillOpacity: 0.35
					        });
						 plotting.setMap(map);
						
						 /* bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
						 map.fitBounds(bounds);
						 
						 var listener = google.maps.event.addListener(map, "idle", function () {
							    map.setZoom(16);
							    google.maps.event.removeListener(listener);
							}); */
							
							loc = new google.maps.LatLng(cords[0].lat, cords[0].lng);
							 bounds.extend(loc);
							 map.fitBounds(bounds);      // auto-zoom
							 //map.panToBounds(bounds);    // auto-center
							 
							  var arType = '<s:property value="%{getAreaType()}" />';
							  var cultiArea = '<s:property value="%{farm.farmDetailedInfo.proposedPlantingArea}" />';
							
							 mapLabel2 = new MapLabel({

				                  text: cultiArea+" "+arType,
				                  position: bounds.getCenter(),
				                  map: map,
				                  fontSize: 14,
				                  align: 'left'
				                });
				                mapLabel2.set('position', bounds.getCenter());
							 
					}
					
				} catch (err) {
					console.log(err);
				}
				
				
				
			}
			</script>

<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>


<!--Farm List -->



