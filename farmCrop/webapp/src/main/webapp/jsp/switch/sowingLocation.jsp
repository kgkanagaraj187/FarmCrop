<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<script src="js/farmer.js?v=3.10"></script>
<head>
<META name="decorator" content="swithlayout">
</head>
<style>
.sticky-container {
	padding: 0px;
	margin: 0px;
	position: absolute;
	right: -132px;
	top: 105px;
	width: 210px;
	z-index: 1100;
}

.sticky li {
	list-style-type: none;
	background-color: #fff;
	color: #efefef;
	height: 43px;
	padding: 0px;
	margin: 0px 0px 1px 0px;
	-webkit-transition: all 0.25s ease-in-out;
	-moz-transition: all 0.25s ease-in-out;
	-o-transition: all 0.25s ease-in-out;
	transition: all 0.25s ease-in-out;
	cursor: pointer;
}

.sticky li:hover {
	margin-left: -115px;
}

.sticky li img {
	float: left;
	margin: 5px 4px;
	margin-right: 5px;
}

.sticky li p {
	padding-top: 11px;
	margin: 0px;
	line-height: 16px;
	font-size: 16px;
	font-weight: bolder;
	font-style: italic;
	color: black;
}

.sticky li p a {
	text-decoration: none;
	color: #2C3539;
}

.sticky li p a:hover {
	text-decoration: underline;
}

.chartIcnWrapper1 {
	padding: 10px;
	background: #a8e3d6;
	position: absolute;
	top: 15%;
	right: 0;
	z-index: 99999;
	transition: all .3s ease-in-out;
}
</style>
<body>


	<div class="appContentWrapper dashboardPage"
		style="height: 100% !Important;">
		<div class="mapWrapper">
			<div class="mapOverlay">
				<div class="popUpWrapper">
					<div class="popupWidget">
						<div class="close">
							<a href="#"> <i class="fa fa-times"></i>
							</a>
						</div>
						<div class="popupheader">
							<div class="frmrImageWrapper">
								<img border="0" id="farmerImage" />
							</div>
							<div class="lstEdtTxt"></div>
						</div>
						<div class="popupContent">
							<div class="frmrNameWrapper">
								<h4 class="farmerName"></h4>
								<h6 class="farmerId"></h6>
							</div>
							<div class="crpDetailsWrapper cropInfo">
								<div class="crpDetails column">
									<div class="crpIcnImg plottinghide">
										<img src="img/crpIcnImg.png" />
									</div>
									<div class="crpTxt plottinghide">
										<h4 class="cropNameTxt">
											<s:property value="%{getLocaleProperty('crop')}" />
										</h4>
										<h6 class="cropName"></h6>
									</div>
								</div>
								<div class="crpAreaDetails column">
									<div class="crpIcnImg">
										<img src="img/crpAreaIcnImg.png" />
									</div>
									<div class="crpTxt">
										<h4 class="cropAreaTxt">
											<s:property value="%{getLocaleProperty('totalAcres')}" />
										</h4>
										<h6 class="cropArea"></h6>
									</div>
								</div>

							</div>

							<div class="farmerDetailsWrapper column">
								<ul>
									<li>


										<div class="column">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('farmName')}" /></strong> <span
													class="farmName"></span>
											</p>
										</div>
										<div class="column">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('app.branch')}" /></strong> <span
													class="branch"></span>
											</p>
										</div>
									</li>

									<li>
										<div class="column">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('village')}" /></strong> <span
													class="village"></span>
											</p>
										</div>
										<div class="column estHavstDateInfo">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('havstSeason')}" /></strong> <span
													class="cropSeason"></span>
											</p>
										</div>

										<div class="column totalLandInfo">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('totalLandHolding')}" /></strong> <span
													class="totalLandHold"></span>
											</p>
										</div>
									</li>
									<li>
										<div class="column plottinghide harvestDate">
											<p class="">
												<s:property value="%{getLocaleProperty('estHavstDate')}" />
												</strong> <span class="estHavstDate"></span>
											</p>
										</div>
										<div class="column estYieldInfo plottinghide">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('sowingDate')}" /></strong> <span
													class="sowingDate"></span>
											</p>
										</div>
									</li>
									<s:if test="currentTenantId=='wilmar'">
										<li class="inspectInfo ">
											<div class="column">
												<p class="">
													<strong><s:property
															value="%{getLocaleProperty('insDate')}" /></strong> <span
														class="inspDate"></span>
												</p>
											</div>
											<div class="column">
												<p class="">
													<strong><s:property
															value="%{getLocaleProperty('inspectedBy')}" /></strong> <span
														class="inspectedBy"></span>
												</p>
											</div>
										</li>
										<li class="inspectInfo ">
											<div class="column">
												<p class="">
													<strong><s:property
															value="%{getLocaleProperty('organicStatus')}" /></strong> <span
														class="icsType"></span>
												</p>
											</div>
										</li>
									</s:if>
									<s:else>
										<s:if test="currentTenantId!='griffith'">
											<li class="inspectInfo ">
												<div class="column">
													<p class="">
														<strong><s:property
																value="%{getLocaleProperty('insDate')}" /></strong> <span
															class="inspDate"></span>
													</p>
												</div>
												<div class="column">
													<p class="">
														<strong><s:property
																value="%{getLocaleProperty('inspectedBy')}" /></strong> <span
															class="inspectedBy"></span>
													</p>
												</div>
											</li>
										</s:if>
									</s:else>

								</ul>

								<div class="viewmore">
									<a id="locationHref" href="#" onclick="redirectFarmer(this)">View
										More</a>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="column-left">
				<h4>
					Filter By <i class="fa fa-filter" aria-hidden="true"></i>
				</h4>
				<div class="dshbrdFilters">
					<div class="flxr">
						<label for="state"><s:property
								value="%{getLocaleProperty('state')}" /></label>
						<div class="form-element">
							<s:select id="state" name="selectedState" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" list="statesList" listKey="key"
								onchange="loadDefMapAll();loadFields();listLocality(this,'state','localities','division','','village')"
								listValue="value" theme="simple" />
						</div>
					</div>
					<div class="flxr">
						<label for="district"><s:property
								value="%{getLocaleProperty('district')}" /></label>
						<div class="form-element">
							<s:select id="localities" name="selectedLocality" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" list="listLocalities"
								listKey="key"
								onchange="loadDefMapAll();loadFields();listMunicipality(this,'localities','division','','village')"
								listValue="value" theme="simple" />
						</div>
					</div>
					<div class="flxr">
						<label for="taluk"><s:property
								value="%{getLocaleProperty('taluk')}" /></label>
						<div class="form-element">
							<s:select id="division" name="selectedTaluk"
								onChange="loadDefMapAll();loadFields();listVillageByCity(this,'division','village')"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" list="talukList" listKey="id"
								listValue="name" theme="simple" />
						</div>
					</div>
					<div class="flxr">
						<label for="village"><s:property
								value="%{getLocaleProperty('village')}" /></label>
						<div class="form-element">
							<s:select id="village" name="selectedVillage" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" list="villageList" listKey="id"
								onchange="listFarmer(this.value);loadDefMapAll();loadFields()"
								listValue="name" theme="simple"
								cssStyle="width:200px !important" />
						</div>
					</div>

					<%-- <div class="flxr">
						<label for="season"><s:property
								value="%{getLocaleProperty('cSeasonCode')}" /></label>
						<div class="form-element">
							<s:select id="season" name="selectedSeason" headerKey=""
								headerValue="%{getText('txt.select')}" value="CurrentSeason"
								cssClass="form-control select2" list="seasonList"
								onchange="loadDefMap();loadFields()" theme="simple"
								cssStyle="width:200px !important" />

						</div>
					</div> --%>

					<div class="flxr">
						<label for="farmer"><s:property
								value="%{getLocaleProperty('farmer')}" /></label>
						<div class="form-element">
							<s:select id="farmer" name="selectedFarmer" headerKey=""
								headerValue="%{getText('txt.select')}" value=""
								cssClass="form-control select2" list="{}"
								onchange="listFarm(this.value);loadDefMapAll();loadFields()"
								theme="simple" cssStyle="width:200px !important" />

						</div>
					</div>
                    <s:if test="currentTenantId!=='livelihood'">
					<div class="flxr farmFilter ">
						<label for="txt"><s:property
								value="%{getLocaleProperty('farm')}" /></label>
						<div class="form-element">
							<s:select name="selectedFarm" id="farm" list="{}" headerKey=""
								onChange="loadDefMapAll();loadFields()"
								headerValue="%{getText('txt.select')}"
								cssClass="form-control select2" theme="simple"
								cssStyle="width:200px !important" />
						</div>
					</div>
					</s:if>
					<s:if test="plottingTyp!=='1'">
						<div class="flxr cropFilter ">
							<label for="txt"><s:property
									value="%{getLocaleProperty('crop')}" /></label>
							<div class="form-element">
								<s:select name="selectedCrop" id="crop" list="{}" headerKey=""
									onChange="loadDefMapAll();loadFields()"
									headerValue="%{getText('txt.select')}"
									class="form-control input-sm" />
							</div>
						</div>
					</s:if>
                    <s:if test="currentTenantId!=='livelihood'">
					<div class="flxr ">
						<label for="status"><s:property
								value="%{getLocaleProperty('farmerStatus')}" /></label>
						<div class="form-element">
							<s:select id="status" name="selectedStatus" headerKey=""
								headerValue="%{getText('txt.select')}" value=""
								cssClass="form-control select2" list="{}"
								onchange="loadDefMapAll();loadFields()" theme="simple"
								cssStyle="width:200px !important" />

						</div>
					</div>
					</s:if>

					<%-- <div class="flxr organicStatus">
						<label for="organicStatus"><s:property
								value="%{getLocaleProperty('organicStatus')}" /></label>
						<div class="form-element">
							<s:select id="organicStatus" name="selectedOrganicStatus" headerKey=""
								headerValue="%{getText('txt.select')}" value=""
								cssClass="form-control select2" list="organicStatusList"
								onchange="loadDefMap();loadFields()" theme="simple"
								cssStyle="width:200px !important" />

						</div>
					</div> --%>

					<div class="flxrTxtGrpWrapper">
						<div class="flxrTxtGrp">
							<div class="noTxt">
								<span class="nos" id="farmerCount"></span>
								<s:property value="%{getLocaleProperty('totalFarmers')}" />
							</div>
							<div class="icn">
								<img src="img/farmer-icn.png" />
							</div>
						</div>
						<div class="flxrTxtGrp prodArea ">
							<div class="noTxt">
								<span class="nos" id="totalAcres"></span>
								<s:property value="%{getLocaleProperty('totalAcres')}" />
							</div>
							<div class="icn">
								<img src="img/area-icn.png" />
							</div>
						</div>
						<%-- 	<div class="flxrTxtGrp yield ">
							<div class="noTxt">
								<span class="nos" id="yield"></span>
								<s:property value="%{getLocaleProperty('estYield')}" />
							</div>
							<div class="icn">
								<img src="img/yld-estimation-icn.png" />
							</div>
						</div> --%>
					</div>
				</div>
			</div>

			<!-- 	<div id="map" style="height: 500px;"></div> -->
			<div class="column-right" style="height: 100% !Important;">

				<div id="map"></div>
				<div class="chartIcnWrapper">
					<a href="dashboard_list.action" title="Go to Dashboard"> <i
						class="fa fa-bar-chart" onmouseover="this.href" aria-hidden="true"></i>
					</a>

				</div>

				<div class="sticky-container">
					<ul class="sticky">
						<li><img src="img/red_placemarker.png" width="32" height="32">
							<p>
								<s:property value="%{getLocaleProperty('Conventional')}" />
							</p></li>
						<li><img src="img/yellow_placemarker.PNG" width="32"
							height="32">
							<p>
								<s:property value="%{getLocaleProperty('inprocess')}" />
							</p></li>
						<li><img src="img/green_placemarker.png" width="32"
							height="32">
							<p>
								<s:property value="%{getLocaleProperty('alrdyCertified')}" />
							</p></li>

					</ul>
				</div>
			</div>
		</div>
	</div>

	<script>
		var map;
	 	var markersArray = new Array();
		var isGramPanchayatEnable = 0;
		var longtitude="<s:property value='getLongtitude()'/>";
		var latitude="<s:property value='getLatitude()'/>"; 
		function initMap() {
			if(isEmpty(longtitude)|| isEmpty(latitude)|| latitude==''||longtitude==''){
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 11.0168,
				     lng : 76.9558
				},
				zoom :6,
				mapTypeId: google.maps.MapTypeId.HYBRID,
			});
			}
			else{
				map = new google.maps.Map(document.getElementById('map'), {
					center : {
					     lat : parseFloat(latitude),
						 lng : parseFloat(longtitude)
					},
					zoom :6,
					mapTypeId: google.maps.MapTypeId.HYBRID,
				});
			}
		}
		 var arrayVal;
		 var intermediateImg ;
		 var intermediatePointIcon;
		 $(document).ready(function () {
			var tenantId='<s:property value="getCurrentTenantId()"/>';
			var plottingTyp='<s:property value="getPlottingType()"/>';

			//alert("arrayVal="+arrayVal);
			//alert("plottingTyp:"+plottingTyp);
			
			loadDefMap();
			onFilterData();
			loadFields()
			var winHeight = window.innerHeight - $('.headerBar').height()-500;
		    //alert($('.footerWrapper').height());
		   
		   var dashboardHeight = ($('.dashboardPageWrapper').height()+20);
		  // alert($('.dashboardPageWrapper').height());
		    $('.dashboardPage').height(dashboardHeight);
		    $('.column-left').height(dashboardHeight);
		    $('.column-right').height(dashboardHeight);
		   
           	jQuery.post("sowingLocation_populateHideFields.action", {}, function(result) {
    			var farmerLocHideFields = jQuery.parseJSON(result);
    			
    			$.each(farmerLocHideFields, function(index, value) {
    				if(value.type=='0'){
						hideByEleClass(value.typeName);
 						$("."+value.typeName).remove();
					}
    				if(value.type=='1'){
    					showByEleClass(value.typeName);
					}
				/* 	 if(value.type=='4'){
 						hideByEleClass(value.typeName);
 						$("."+value.typeName).remove();
 					}
					 if(value.type=='5'){
	 						hideByEleId(value.typeName);
	 						$("."+value.typeName).remove();
	 					} */
    			});
    			
    	    });
		});
		
	      function showByEleClass(className){
          	$("."+className).closest( ".flexform-item" ).removeClass( "hide" );
          }

          function hideByEleId(id){
          	$("#"+id).closest( ".flexform-item" ).addClass( "hide" );
          }
          
          function hideByEleClass(className){
          	$("."+className).closest( ".flexform-item" ).addClass( "hide" );
          }
          
          function showByEleId(id){
          	$("#"+id).closest( ".flexform-item" ).removeClass( "hide" );
          }

  
		function loadFarms(obj) {
			//setMapOnAll(null);   
			jQuery.post("sowingLocation_populateFarmList.action", {
				selectedFarmer : selectedFarmer,
				dt : new Date()
			}, function(result) {
				insertOptions("farm", jQuery.parseJSON(result));
			});
		}
function listFarmer(value) {
			
				if (!isEmpty(value)) {
					$.post("sowingLocation_populateFarmer", {
						selectedVillage : value
					}, function(data) {
						insertOptions("farmer",$.parseJSON(data));
					});
				}
			
		}
function listFarm(value) {	
	if (!isEmpty(value)) {
		$.post("sowingLocation_populateFarm", {
			selectedFarmer : value
		}, function(data) {
			insertOptions("farm",$.parseJSON(data));
		});
	}

}		
/* 		function loadFilterMap()
		{
			var dataArr = new Array();
			var village = $("#village").val();
			var taluk = $("#division").val();
			var crop = $("#crop").val();
			var season = $("#season").val();
			$.post('farmerLocation_populateFilterMap', {
				selectedVillage : village,
				selectedTaluk : taluk,
				selectedCrop : crop,
				selectedSeason:season
			}, function(data) {
				var arry = JSON.parse(data);
				if (arry.length > 0) {
					$(arry).each(function(k, v) {
						if(!isEmpty(v.latitude)&&!isEmpty(v.longtitude)){
							dataArr.push({
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,
								//totalLand:v.totalLand,
								//proposedLand:v.proposedLand,
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode
							});
						}
					});
				}
			alert(dataArr)
				loadMap(dataArr);
			});
		} */
		
		function loadDefMap(){	
			setMapOnAll(null);
			var dataArr = new Array();
			var state = $("#state").val();
			var locality = $("#localities").val();
			var village = $("#village").val();
			var taluk = $("#division").val();
			var crop = $("#crop").val();
			var farmer = $("#farmer").val();
			var farm = $("#farm").val();
			var status = $("#status").val();
			
			$.post('sowingLocation_populateFarmCropMap', {
				selectedState : state,
				selectedLocality : locality,
				selectedVillage : village,
				selectedTaluk : taluk,
				selectedCrop : crop,
				selectedFarmer:farmer,
				selectedStatus:status,
				seelctedFarm:farm,				
			}, function(data) {
				var arry = JSON.parse(data);				
				arrayVal= arry;           //Global variable
				
				if (arry.length > 0) {
					$(arry).each(function(k, v) {												
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,
								//totalLand:v.totalLand,
								//proposedLand:v.proposedLand,
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp
								//certified:v.certified,
								//organicStatus:v.organicStatus
							});
						});
				}
				loadMap(dataArr);
			});				
			
		}
		
		function loadDefMapAll(){
			//alert("arrayVal:"+arrayVal);
			setMapOnAll(null);   
			var dataArr = new Array();
			var state = $("#state").val();
			var locality = $("#localities").val();
			var village = $("#village").val();
			var taluk = $("#division").val();
			var crop = $("#crop").val();
			var farmer = $("#farmer").val();
			var farm = $("#farm").val();
			var status = $("#status").val();
						  							
				if (arrayVal.length > 0) {
					$(arrayVal).each(function(k, v) {						
						if(state==v.stateId){
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(locality==v.districtId){							
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(taluk==v.talukId){								
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(village==v.villageId){							
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(crop==v.cropId){								
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(farmer==v.farmerId){								
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
						if(farm==v.farmId){								
							dataArr.push({															
								latitude : parseFloat(v.latitude),
								longitude : parseFloat(v.longtitude),
								farmerName:v.farmerName,
								farmName:v.farmName,
								village:v.village,
								landmark:v.landmark,								
								samithi:v.samithi,
								img:v.image,
								id:v.id,
								farmCode:v.farmCode,
								farmId:v.farmId,
								farmerId:v.farmerId, 
								jsonObjectList:v.jsonObjectList,
								fTyp:v.fTyp								
							});
						}
					});
				} 
				//initmap();
				loadMap(dataArr);						
		}
		
		function resetForm(){
			$("#division").val("-1");
			$("#village").val("");
			$(".select2").select2();
			setMapOnAll(null);
			//loadDefMap();
		}
		
		 function setMapOnAll(map) {
		        for (var i = 0; i < markersArray.length; i++) {
		        	markersArray[i].setMap(map);
		        }
		        markersArray = new Array();
		 }

		function loadVillage() {
			var talukId = $("#division").val();
			jQuery.post("sowingLocation_populateVillageByCity.action", {
				selectedTaluk : talukId
			}, function(result) {
				insertOptions("village", $.parseJSON(result));
			});
		}

		/* function loadFarmsMap() {
			try {
				
				var village = $("#village").val();
				var taluk = $("#division").val();
				if (village == "" || village == null || village == "undefined") {
					alert('<s:text name="village.empty"/>');
					return false;
				}
				if (taluk == "" || taluk == null || taluk == "undefined") {
					alert('<s:text name="taluk.empty"/>');
					return false;
				}

				var dataArr = new Array();
				
				$.post('farmerLocation_populateFarmsMap', {
					selectedVillage : village
				}, function(data) {
					var arry = JSON.parse(data);
					if (arry.length > 0) {
						setMapOnAll(null);   
						$(arry).each(function(k, v) {
							if(!isEmpty(v.latitude)&&!isEmpty(v.longtitude)){
								dataArr.push({
									latitude : parseFloat(v.latitude),
									longitude : parseFloat(v.longtitude),
									farmerName:v.farmerName,
									farmName:v.farmName,
									village:v.village,
									landmark:v.landmark,
									totalLand:v.totalLand,
									proposedLand:v.proposedLand,
									samithi:v.samithi,
									img:v.image,
									id:v.id
								});
							}
						});
					}
	
					loadMap(dataArr);
				});

			} catch (e) {
				console.log(e);
			}
		} */
		
		function getRandomColor() {
			var letters = '0123456789ABCDEF';
			var color = '#';
			for (var i = 0; i < 6; i++) {
				color += letters[Math.floor(Math.random() * 16)];
			}
			return color;
		}
		
		function loadMap(dataArr) {			
			try {
				$(dataArr).each(function(k, v) {
				/* loadMaps('map', '<s:property value="farmCrops.latitude"/>',
						'<s:property value="farmCrops.longitude"/>', v.jsonObjectList); */	
					var intermediateImg="";
					var intermediatePointIcon="";
					var color="";
					var url = window.location.href;
					var temp = url;
					for(var i = 0 ; i < 1 ; i++) {
						  temp = temp.substring(0, temp.lastIndexOf('/'));
					 }
			        if(v.fTyp==1){
			         intermediateImg = "red_placemarker.png";
					 intermediatePointIcon = temp + '/img/'+intermediateImg;
					 color='#FF0000';
					}else if(v.fTyp==2){
					 intermediateImg = "green_placemarker.png";
					 intermediatePointIcon = temp + '/img/'+intermediateImg;	 
					 color='#00b300';
					}

						try {
														
							if(v.jsonObjectList.length>0){
								var cords = new Array();
								$(v.jsonObjectList).each(function(k,v){
									//alert(v.lat);
									cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
									if(k==0){

								 		marker = new google.maps.Marker({
										position : new google.maps.LatLng(parseFloat(v.lat),
												parseFloat(v.lon)),
												icon:intermediatePointIcon,
										map : map
									});  
									}
									
								});								
																
							     var polyOptions = {
							    		   paths: cords,
										      strokeColor:color,
									          strokeOpacity: 0.8,
									          strokeWeight: 2,
									          fillColor: '#E7D874',
									          fillOpacity: 0.35
							            }
							     var plotting =  new google.maps.Polygon(polyOptions);
							     plotting.setMap(map);								
								 markersArray.push(plotting);
								 markersArray.push(marker);
									$(".mapOverlay").hide();
									google.maps.event
									.addListener(
											marker,
											'click',
											function(e) {
												
												var village = $("#village").val();
												var taluk = $("#division").val();
												var crop = $("#crop").val();
												var season = $("#season").val();
												var farmerType=v.fTyp;
												 $("#fType").val(farmerType);												
												    	$.post('sowingLocation_populateImg', {
												    		farmCropId : v.id,
												    		farmCode : v.farmCode,
												    		farmId : v.farmId,
												    		selectedVillage : village,
															selectedTaluk : taluk,
															selectedCrop : crop,
															selectedStatus:farmerType
														}, function(data) {
												    	var arry = JSON.parse(data);
												    	resetCropDetails();
												    	
														if (arry.length > 0) {
															$(arry).each(function(k, v) {
																$(".mapOverlay").show();
																	if(!isEmpty(v)){
																	if (v.doj !== undefined)
																		{	$(".lstEdtTxt").text(
																				"Enrolled on "
																				+ v.doj);
																		}
																
																	$(".farmerName").text(
																			v.farmerName);
																	$(".farmerId").text(
																			v.farmerId);
																	$(".cropArea").text(
																			v.totalLand);
																	$(".cropName").text(
																			v.cropName);
																	$(".farmName").text(
																			v.farmName);
																	$(".group").text(v.samithi);
																	$(".inspDate").text(
																			v.inspDate);
																	$(".inspectedBy").text(
																			v.inspectedBy);
																	$(".village").text(
																			v.village);
																	if (v.estHavstDate !== undefined && v.estHavstDate!='null' )
																		{
																		$(".estHavstDate").text(
																				v.estHavstDate);
																		}
																	
																	$(".sowingDate").text(
																			v.sowingDate);
																	$(".cropSeason").text(
																			v.cropSeason);
																	$(".branch").text(v.branch);
																	$(".cropArea").text(v.totalLand);
																	$(".icsType").text(
																			v.icsType);
																		if(v.image!=null)
																    	{
																    			$("#farmerImage").attr('src',v.image);
																    	}
																    	else
																		{
																			$("#farmerImage").attr('src',"img/no-image.png");
																		}
																}
															});
														}
												});
												    	if(v.fTyp!=null && v.fTyp!=0){
												    		if(v.fTyp=="1"){				
																$('.plottinghide').addClass("hide");
															}else{				
																$('.harvestDate').addClass("hide");
															}																												
														}										
												
												
												if(v.farmerId!=null && v.farmerId!=0)
													{
													
														$("#farmerIdHidn").val(v.farmerId);
																											
													}

											}); 
							
									
								
								 bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
								 map.fitBounds(bounds);
								 
								 var listener = google.maps.event.addListener(map, "idle", function () {
									    map.setZoom(15);
									    google.maps.event.removeListener(listener);
									   
									});
							
							}
						} catch (err) {
							console.log(err);
						}
				});
				$('.close').click(function() {
					$(".mapOverlay").hide();
					});
			} catch (err) {
				console.log(err);
			}
		}
		
		
		
		function resetCropDetails()
		{
			$(".lstEdtTxt").text('');
			$(".farmerName").text('');
			$(".farmerId").text('');
			$(".cropArea").text('');
			$(".cropName").text('');
			$(".farmName").text('');
			$(".group").text('');
			$(".village").text('');
			$(".estHavstDate").text('');
			$(".sowingDate").text('');
			$(".cropSeason").text('');
			$(".branch").text('');
			$(".cropArea").text('');
			$(".inspDate").text('');
			$(".inspectedBy").text('');
			$(".icsType").text('');
		}
		
		 function buildDataOnMouseHover(v)
			{
			 var content = "<table class='table table-responsive table-hover table-bordered' style='background:none;'>";
				
				content += "<tr>"
				content += td('<s:property value="%{getLocaleProperty('farmer')}" />');
				content += td(v.farmerName);
				content += "</tr>"
				content += "<tr>"
				content += td('<s:property value="%{getLocaleProperty('farmName')}" />');
				content += td(v.farmName);
				content += "</tr>"
					content += "</table>";
				return content;
			}
		
	/* 	function buildData(v){
			
			
			var content = "<table class='table table-responsive table-hover table-bordered'>";
			
			content += "<tr>";
			content += '<td colspan="3"><b><s:property value="%{getLocaleProperty('title.farmer')}" /></b></td>';
			content += "</tr>"
			
			var img = "";
				$.ajax({
					url:'farmerLocation_populateImg.action?farmerId='+v.id+'',
					type: 'post',
				    dataType: 'json',
				    async:false,
				    success: function(result) {
				    	img = "<img src='"+result.image+"' width='150' height='150' alt='Farmer Image'>";
				    }
				});
				
			
			content += "<tr>";
				content += td('<s:property value="%{getLocaleProperty('farmer')}" />');
				content += td(v.farmerName);
				if(!isEmpty(img)){
					content += "<td rowspan='5'>"+img+"</td>";
				}else{
					content += "<td rowspan='5'>"+"<img width='150' height='150' alt='Farmer Image'>"+"</td>";
				}
			content += "</tr>";
			
			content += "<tr>";
				content += td('<s:property value="%{getLocaleProperty('Farm')}" />');
				content += td(v.farmName);
			content += "</tr>";
			
			content += "<tr>";
			content += td('<s:property value="%{getLocaleProperty('profile.location.village')}" />');
			content += td(v.village);
			content += "</tr>";
			
			content += "<tr>";
			content += td('<s:property value="%{getLocaleProperty('farmer.samithi')}" />');
			content += td(v.samithi);
			content += "</tr>";
			
			if(v.totalLand==null||v.totalLand==""){
				content += "<tr>";
				content += td('<s:property value="%{getLocaleProperty('farm.area')}" />');
				content += td('0.00');
				content += "</tr>";
			}else{
				content += "<tr>";
				content += td('<s:property value="%{getLocaleProperty('farm.area')}" />');
				content += td(v.totalLand);
				content += "</tr>";
			}
			
			content += "</table>";
			return content;
		} */
		
		function td(val){
			  return "<td>"+getFormattedValue(val)+"</td>";
		}
		
		function getFormattedValue(val){
			   if(val==null||val==undefined||val.toString().trim()==""){
			    return " ";
			   }
			   return val;
		}
		
		function loadFields() {
			var state = document.getElementById("state").value;
			var locality = document.getElementById("localities").value;
			var taluk = document.getElementById("division").value;
			var village = document.getElementById("village").value;
			var crop = $("#crop").val();
			var farmer = $("#farmer").val();
			var status = $("#status").val();
			var farm = $("#farm").val();
			$.post("sowingLocation_populateLoadFields", {
				selectedState : state,
				selectedLocality : locality,
				selectedTaluk : taluk,
				selectedVillage : village,
				selectedCrop : crop,
				selectedFarmer:farmer,
				selectedFarm:farm,
				selectedStatus:status
			}, function(data) {
				var json = JSON.parse(data);
				$("#farmerCount").html(json[0].farmerCount);
				$("#totalAcres").html(json[0].totalAcres);
				$("#yield").html(json[0].yield);

			});
		}
		

		function onFilterData() {

			callAjaxMethod("sowingLocation_populateCropList.action", "crop");
			callAjaxMethod("sowingLocation_populateStatusList.action", "status");
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
		
		function redirectFarmer(){
			var farmerIdHidn = jQuery("#farmerIdHidn").val();
			/* var url */
			window.open("farmer_detail.action?id="+farmerIdHidn);
		}
		
	</script>
	<script async defer
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
	<s:hidden id="farmerIdHidn" name="farmerIdHidn" />
	<s:hidden id="fType" name="fType" />
</body>
