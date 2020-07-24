<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<style>

.sticky-container{
    padding:0px;
    margin:0px;
    position:absolute;
    right:-132px;
    top:105px;
    width:210px;
    z-index: 1100;
    
}
.sticky li{
    list-style-type:none;
    background-color:#fff;
    color:#efefef;
    height:43px;
    padding:0px;
    margin:0px 0px 1px 0px;
    -webkit-transition:all 0.25s ease-in-out;
    -moz-transition:all 0.25s ease-in-out;
    -o-transition:all 0.25s ease-in-out;
    transition:all 0.25s ease-in-out;
    cursor:pointer;
}
.sticky li:hover{
    margin-left:-115px;
}
.sticky li img{
    float:left;
    margin:5px 4px;
    margin-right:5px;
}
.sticky li p{
       padding-top: 11px;
    margin: 0px;
    line-height: 16px;
    font-size: 16px;
    font-weight: bolder;
    font-style: italic;
    color:black;
}
.sticky li p a{
    text-decoration:none;
    color:#2C3539;
}
.sticky li p a:hover{
    text-decoration:underline;
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

<div class="appContentWrapper dashboardPage" style="height: 100% !Important;">
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
									<div class="crpIcnImg">
										<img src="img/crpIcnImg.png" />
									</div>
									<div class="crpTxt">
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
										<s:if test="currentTenantId!='awi'">
										<div class="column estHavstDateInfo">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('estHavstDate')}" /></strong> <span
													class="estHavstDate"></span>
											</p>
										</div>
                                 
										<div class="column totalLandInfo">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('totalLandHolding')}" /></strong> <span
													class="totalLandHold"></span>
											</p>
										</div>
										      </s:if>
									</li>
									<li>
										<div class="column">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('havstSeason')}" /></strong> <span
													class="cropSeason"></span>
											</p>
										</div>
										<s:if test="currentTenantId!='awi'">
										<div class="column estYieldInfo">
											<p class="">
												<strong><s:property
														value="%{getLocaleProperty('estYield')}" /></strong> <span
													class="estYield"></span>
											</p>
										</div>
										</s:if>
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
			<section class="reportWrap row">
				<div class="gridly">
					<div class="filterEle">
						<label for="lotNo"><s:property
								value="%{getLocaleProperty('lotNo')}" /></label>
						<div class="form-element">
							<s:select id="lotNo" name="lotNo"
								cssClass="form-control select2" list="{}"
								onchange="loadDefMap();loadFields()" theme="simple" multiple="true"
								cssStyle="width:200px !important" />
						</div>
					</div>
					
					<div class="filterEle">
						<label for="totalFarmers"><s:property
								value="%{getLocaleProperty('totalFarmers')}" /></label>
						<div class="form-element">
							<span class="strong" id="farmerCount"></span>
						</div>
					</div>
					
					<div class="filterEle">
						<label for="totalVillage"><s:property
								value="%{getLocaleProperty('totalVillage')}" /></label>
						<div class="form-element">
							<span class="strong" id="villageCount"></span>
						</div>
					</div>
						
				</div></section>
			
			<div class="column-right" style="height: 100% !Important; width:100% !Important">
				<div id="map"></div>
				<div class="chartIcnWrapper">
					<a href="lotTraceabilityReport_list.action" title="Go to Dashboard"> <i
						class="fa fa-bar-chart" onmouseover="this.href" aria-hidden="true"></i>
					</a>
				</div>
			</div>
</div></div>

	<script>
		var map;
		var markersArray = new Array();
		var longtitude="<s:property value='getLongtitude()'/>";
		var latitude="<s:property value='getLatitude()'/>";

		$(document).ready(function () {
			var tenantId='<s:property value="getCurrentTenantId()"/>';
			loadDefMap();
			onFilterData();
			loadFields()
			//initMap();
			var winHeight = window.innerHeight - $('.headerBar').height()-500;
		   
		   var dashboardHeight = ($('.dashboardPageWrapper').height()+20);
		    $('.dashboardPage').height(dashboardHeight);
		    $('.column-left').height(dashboardHeight);
		    $('.column-right').height(dashboardHeight);
           	$('.close').click(function() {
        	$(".mapOverlay").hide();
        		});
		});
		
		
		function initMap() {
			
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 19.4769,
				     lng : 79.5712
				},
				zoom :6,
				mapTypeId: google.maps.MapTypeId.HYBRID,
			});
			warehousesMap();
		}
		function warehousesMap(){
			//setMapOnAll(null); 
			var url = window.location.href;
		    var temp = url;
		    for (var i = 0; i < 1; i++) {
		      temp = temp.substring(0, temp.lastIndexOf('/'));
		    }
		    var infowindow = new google.maps.InfoWindow();
		    var marker, i;
		    var intermediateImg
		    var i=1;
		    $.ajax({
		        type: "POST",
		            async: false,
		            url: "lotTraceabilityReport_populateWarehouseList.action",
		            success: function(dataa) {
		            	var arrVal = JSON.parse(dataa);
						if (arrVal.length > 0) {
							$(arrVal).each(function(k, v) {
		    	if(v.lati!=NaN && v.lati !=undefined && v.longi!=NaN && v.longi !=undefined){   
		    		if(v.type==2){
		    	 intermediateImg = temp + '/img/Trace2.png';
		    		}
		    		else if(v.type==3){
		    			intermediateImg = temp + '/img/Trace3.png';
		    		}else if(v.type==4){
		    			intermediateImg = temp + '/img/Trace4.png';
		    		}
		    		marker = new google.maps.Marker({
		    			position : new google.maps.LatLng(v.lati,
		    					v.longi), 
		    					title: v.name,
		    					icon:intermediateImg,			
		    					map : map
				        });
		    		map.setCenter({
						lat : parseFloat(v.lati),
						lng : parseFloat(v.longi)
					});
				        map.setZoom(5);
				        markersArray.push(new google.maps.LatLng(v.lati,v.longi));
				        i=i+1;  
		    	}
							});
						}
		            }
		    });
		    marker.setMap(map);
		}
		
		
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
		
		function loadDefMap(){
			//setMapOnAll(null);   
			var geoJs = {};
			geoJs['type']="FeatureCollection";
			var feature = [];
			var selectedValues="";
			var dataArr = new Array();
		    $("#lotNo option:selected").each(function() {
		    	selectedValues += $(this).val() + ",";
		    });
			 $.ajax({
		        type: "POST",
		            async: false,
		            url: "lotTraceabilityReport_populateFarmsMap.action",
		            data:{selectedLotNo : selectedValues},
		            success: function(data) { 
				var arry = JSON.parse(data);
				if (arry.length > 0) {
					$(arry).each(function(k, v) {
						if(!isEmpty(v.latitude)&&!isEmpty(v.longtitude)){
							var url = window.location.href;
							var temp = url;
							 for(var i = 0 ; i < 1 ; i++) {
								  temp = temp.substring(0, temp.lastIndexOf('/'));
							 } 
										intermediateImg = "red_placemarker.png";
										 intermediatePointIcon = temp + '/img/'+intermediateImg;
							feature.push({
								"type": "Feature",    
								"properties": {   
									"name": "Coors Field",    
									"show_on_map": true,
									 
									"dta" : v,
									 "icon": intermediatePointIcon,
									},  
								 "geometry": {  
									 "type": "Point",
									 "icon": intermediatePointIcon,
									 "coordinates" : [parseFloat(v.longtitude),parseFloat(v.latitude)]
								 },
								"center" : {
										"lat" : parseFloat(v.latitude),
										"lng" : parseFloat(v.longitude)
									},
									
							});
							    	
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
								certified:v.certified,
								organicStatus:v.organicStatus
								
							});
						}
					});
				}
			geoJs["features"]=feature;
			loadGeoJSON(geoJs);
			}
		            });
			
		}
		function loadGeoJSON(jsData){
			//setMapOnAll(null);   
			map.data.setStyle(function(feature) {
			    var icon = null;
			    if (feature.getProperty('icon')) {
			      icon = feature.getProperty('icon');
			    }
			    return ({
			      icon: icon
			    });
			 });
		
			map.data.forEach(function(feature) {
				  
			    map.data.remove(feature);
			}); 
		
			 map.data.addGeoJson(jsData);
			 map.data.addListener('click', function(event) {
				 var v = event.feature.getProperty('dta');
					    	$.post('farmerLocation_populateImg', {
					    		farmerId : v.id,
					    		farmCode : v.farmCode,
					    		farmId : v.farmId,
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
												v.proposedLand);
										$(".cropName").text(
												v.cropName);
										$(".farmName").text(
												v.farmName);
										$(".totalLandHold").text(
												v.totalLand);
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
										
										$(".estYield").text(
												v.estYield);
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
					if(v.id!=null && v.id!=0)
						{
							$("#farmerIdHidn").val(v.id);
						}
				});
		}
		function resetForm(){
			$("#lotNo").val("");
			$(".select2").select2();
			setMapOnAll(null);
		}
		
		 function setMapOnAll(map) {
		        for (var i = 0; i < markersArray.length; i++) {
		        	markersArray[i].setMap(map);
		        }
		        markersArray = new Array();
		 }

		function getRandomColor() {
			var letters = '0123456789ABCDEF';
			var color = '#';
			for (var i = 0; i < 6; i++) {
				color += letters[Math.floor(Math.random() * 16)];
			}
			return color;
		}

		function loadMap(dataArr) {
			var intermediateImg;
			setMapOnAll(null);   
			var url = window.location.href;
			var temp = url;
			for(var i = 0 ; i < 1 ; i++) {
				  temp = temp.substring(0, temp.lastIndexOf('/'));
			 }
			var intermediatePointIcon;
			$(dataArr).each(function(k, v) {
							intermediateImg = "red_placemarker.png";
							 intermediatePointIcon = temp + '/img/'+intermediateImg;
							 if(v.latitude!=NaN && v.latitude !=undefined && v.longitude!=NaN && v.longitude !=undefined){ 
							 marker = new google.maps.Marker({
					position : new google.maps.LatLng(v.latitude,
							v.longitude),
					
					icon:intermediatePointIcon,
					center : {
						lat : parseFloat(v.latitude),
						lng : parseFloat(v.longitude)
					},
					map : map
									
				});
							
				markersArray.push(marker);
				$(".mapOverlay").hide();
				 google.maps.event
				.addListener(
						marker,
						'click',
						function(e) {
							var lotNo = $("#lotNo").val();
							if(v.id!=null && v.id!=0)
								{
									$("#farmerIdHidn").val(v.id);
								}
						});  
							 }
							 });
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
			$(".estYield").text('');
			$(".cropSeason").text('');
			$(".branch").text('');
			$(".cropArea").text('');
			$(".inspDate").text('');
			$(".inspectedBy").text('');
			$(".icsType").text('');
			$("#farmer").text(''); 
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
			var selectedValues="";
		    $("#lotNo option:selected").each(function() {
		    	selectedValues += $(this).val() + ",";
		    });
				 $.ajax({
				        type: "POST",
				            async: false,
				            url: "lotTraceabilityReport_populateLoadFields.action",
				            data:{selectedLotNo : selectedValues},
				            success: function(data) {
								var json = JSON.parse(data);
								$("#farmerCount").html(json[0].farmerCount);
								$("#villageCount").html(json[0].villageCount);
								

							}
				 }); 
		
			}

		function onFilterData() {
			callAjaxMethod("lotTraceabilityReport_populateLotNoList.action", "lotNo");
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
			window.open("farmer_detail.action?id="+farmerIdHidn);
		}
		function insertOptions(ctrlName, jsonArr) {
	        document.getElementById(ctrlName).length = 0;
	        for (var i = 0; i < jsonArr.length; i++) {
	            addOption(document.getElementById(ctrlName), jsonArr[i].name, jsonArr[i].id);
	        }
	       
	        var id="#"+ctrlName;
	        jQuery(id).select2();
	    }
		
	</script>
	<script async defer
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
	<s:hidden id="farmerIdHidn" name="farmerIdHidn" />
</body>
