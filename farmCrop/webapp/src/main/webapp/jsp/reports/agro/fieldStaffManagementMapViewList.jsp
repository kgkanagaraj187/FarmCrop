<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>
	<div class="appContentWrapper marginBottom">
		<div class="col-md-8 margin-bottom10">
			<b><s:text name="startingDate" /> : <s:property
					value="startDate" /> | <s:text name="agentName" /> : <s:property
					value="filter.agentName" /></b>
			<s:hidden id="coordinates" name="coordinateValues" />
			<s:hidden id="lastLattiude" name="filter.latitude" />
			<s:hidden id="lastLongitude" name="filter.longitude" />
			<img height="20" width="20" src="img/start.png" alt="start point"></img>
			<s:text name="startingPoint" />
			<img height="20" width="20" src="img/blue_placemarker.png"
				alt="inter point"></img>
			<s:text name="intermediatePoint" />
			<img height="20" width="20" src="img/end.png" alt="end point">
			<s:text name="endPoint" />
			</img>
		</div>
		<div class="col-md-4 pull-right">
			<b>Total Distance Covered : </b> <b id="totDist"></b>
		</div>

		<div id="map" style="width: 100%; height: 500px"></div>
	</div>

	<style>
#floating-panel {
	position: absolute;
	top: 10px;
	left: 25%;
	z-index: 5;
	background-color: #fff;
	padding: 5px;
	border: 1px solid #999;
	text-align: center;
	font-family: 'Roboto', 'sans-serif';
	line-height: 30px;
	padding-left: 10px;
}

#floating-panel {
	margin-left: -100px;
}
</style>
	<script>
		var map;
		var panorama;
		

		var url = window.location.href;
		var temp = url;
		
		for(var i = 0 ; i < 1 ; i++) {
			  temp = temp.substring(0, temp.lastIndexOf('/'));
		 }
		var startingPointImg = "start.png";
		var endPointImg = "end.png";
		var intermediateImg = "blue_placemarker.png";
		var startingPointIcon = temp + '/img/'+startingPointImg;
		var endPointIcon = temp + '/img/'+endPointImg;
		var intermediatePointIcon = temp + '/img/'+intermediateImg;
		var dist=0;
		var totalDistance = 0;
		jQuery(document).ready(function(){
			$("#map").css("height",$(document).height());	
			//initMap();
		});
	var totalDistancez=0.0;
	
	function toRad(Value) {
	    /** Converts numeric degrees to radians */
	    return Value * Math.PI / 180;
	}
		
	 	 function setDistance(lat1, lon1, lat2, lon2,dist) {
			 var polyCoords = [new google.maps.LatLng(lat1,lon1),
			                   new google.maps.LatLng(lat2, lon2)];
			 
			 var bounds = new google.maps.LatLngBounds();
			 for (var i = 0; i < polyCoords.length; i++) {
				    bounds.extend(polyCoords[i]);
				  }

			var myLatlng = bounds.getCenter();
			
			var distance=0.0;
		 	

			var R = 6371e3; // metres
			var a = toRad(lat1);
			var b = toRad(lat2);
			var c = toRad(lat2-lat1);
			var d = toRad(lon2-lon1);

			var az = Math.sin(c/2) * Math.sin(c/2) +
			        Math.cos(a) * Math.cos(b) *
			        Math.sin(d/2) * Math.sin(d/2);
			var cz = 2 * Math.atan2(Math.sqrt(az), Math.sqrt(1-az));

			var d = ((R * cz)/1000).toFixed(1);
		//	alert(d);
			
			 /*   var mapLabel2 = new MapLabel({
			    text: d+'KM',
			    position: myLatlng,
			    map: map,
			    fontSize:14,
			    align: 'left'
			  });
			  mapLabel2.set('position', myLatlng);   */
			  if(d>0){
			  totalDistancez=parseFloat(totalDistancez)+parseFloat(d);
			 // alert(totalDistancez);
			  $("#totDist").html(totalDistancez.toFixed(1)+"KM"); 
			  }
		}  
	/* 	function setDistance(objs) {
			var dist =0;
			 for(var i=1;i<=objs.length-1;i++){
					
				 if(!isEmpty(objs[i].lat)&&!isEmpty(objs[i].lng)){
					 var end=[new google.maps.LatLng(objs[i].lat,objs[i].lng)];
						var start=[new google.maps.LatLng(objs[i-1].lat,objs[i-1].lng)];
				var service = new google.maps.DistanceMatrixService();
					    service.getDistanceMatrix({
					        origins:start,
					        destinations: end,
					        travelMode: google.maps.TravelMode.DRIVING,
					        unitSystem: google.maps.UnitSystem.METRIC,
					        avoidHighways: false,
					        avoidTolls: false
					    }, function (response, status) {
					        if (status == google.maps.DistanceMatrixStatus.OK && response.rows[0].elements[0].status != "ZERO_RESULTS") {
					        	console.log(JSON.stringify(response.rows[0].elements[0].distance));
					        	dist = parseFloat(dist) + parseFloat(response.rows[0].elements[0].distance.value);
					        
					        	$("#totDist").html((dist/1000).toFixed(1)+"KM"); 
					        } 
					    });
						   
				 }
				 }
			
		} */
		
		function initMap() {
			bounds = new google.maps.LatLngBounds();
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 11.0168,
					lng : 76.9558
				},
				zoom : 2,
				mapTypeId: google.maps.MapTypeId.HYBRID,
				streetViewControl: true
			});
				
			 

			var bounds = new google.maps.LatLngBounds();
			
			var coordinates = jQuery("#coordinates").val();
			//calculateDistance(coordinates,'totDist');
			if(!isEmpty(coordinates)){
				var lineSymbol = {
	   			          path: google.maps.SymbolPath.FORWARD_OPEN_ARROW
	   			    };
				 
				var icons = {starting:{icon: startingPointIcon}};
				var latLonArr = new Array();
				
				var coordinatesArr = coordinates.split("#");
				var linePoints = new Array();
				var lastLat = 0;
				var lastLon = 0;
				$(coordinatesArr).each(function(key,value){
					var latLonSplt = value.split("|");
					if(!isEmpty(latLonSplt[0])&&!isEmpty(latLonSplt[1])){
						linePoints.push({lat:parseFloat(latLonSplt[0]),lng:parseFloat(latLonSplt[1]),dt:latLonSplt[2],serialNo:latLonSplt[3],device:latLonSplt[5],
							agent:latLonSplt[4],address:latLonSplt[6],dist:latLonSplt[7]});
						
						lastLat=parseFloat(latLonSplt[0]);
						lastLon=parseFloat(latLonSplt[1]);
					}
				});
				updatedLatLon = {lat: lastLat, lng: lastLon};
				
				var coordSize = (linePoints.length)-1;
				var size = 0;
				 if(linePoints.length<=1){
					var infowindow = new google.maps.InfoWindow();
					$(linePoints).each(function(k, v) {
						var srcLat =  v.lat;
						var srcLon =  v.lng;
						
						marker = new google.maps.Marker({
							position : new google.maps.LatLng(v.lat,v.lng),
							center:new google.maps.LatLng(v.lat,v.lng),
							icon: startingPointIcon,
							map : map
						});
						
						google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
							infowindow.setContent(buildDataGrid(v));
							infowindow.open(map, marker);
						}})(marker, i));
					});
					
				}else{
				$(linePoints).each(function(k, v) {
					var infowindow = new google.maps.InfoWindow();
					if(k==0){
				 		
						var nextVal = k+1;
						var srcLat =  v.lat;
						var srcLon =  v.lng;
						var destLat = linePoints[nextVal].lat;
						var destLon = linePoints[nextVal].lng;
						
						srcLocation = new google.maps.LatLng(srcLat, srcLon);
				 		dstLocation = new google.maps.LatLng(destLat,destLon);
				 		var distance = "";//google.maps.geometry.spherical.computeDistanceBetween(srcLocation, dstLocation)
				 		//totalDistance = parseFloat(totalDistance) +parseFloat((distance/1000).toFixed(1)); 
				 		//dist = (distance/1000).toFixed(1);
						
				 		//if(dist>=0.1)
				 		
				 		{
							marker = new google.maps.Marker({
								position : new google.maps.LatLng(v.lat,v.lng),
								center:new google.maps.LatLng(v.lat,v.lng),
								icon: startingPointIcon,
								map : map
							});
							//console.log(v.dist);
							setDistance(srcLat,srcLon, destLat, destLon,v.dist);
							
							google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
								infowindow.setContent(buildDataGrid(v));
								infowindow.open(map, marker);
							}})(marker, i));
				 		}				 		
				 	}  else if(k==coordSize){
						marker = new google.maps.Marker({
							position : new google.maps.LatLng(v.lat,v.lng),
							center:new google.maps.LatLng(v.lat,v.lng),
							icon: endPointIcon,
							map : map
						});
				        google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
								infowindow.setContent(buildDataGrid(v));
								infowindow.open(map, marker);
						}})(marker, i));
				 	}  else{
				 		var nextVal = k+1;
						var srcLat =  v.lat;
						var srcLon =  v.lng;
						var destLat = linePoints[nextVal].lat;
						var destLon = linePoints[nextVal].lng;
						
						srcLocation = new google.maps.LatLng(srcLat, srcLon);
				 		dstLocation = new google.maps.LatLng(destLat,destLon);
				 		var distance = "";//google.maps.geometry.spherical.computeDistanceBetween(srcLocation, dstLocation)
				 		//totalDistance = parseFloat(totalDistance) +parseFloat((distance/1000).toFixed(1)); 
				 		//dist = (distance/1000).toFixed(1);
				 		//if(v.dist>=0.1)
				 			setDistance(srcLat,srcLon, destLat, destLon,v.dist);
				 			if(nextVal%5==0)
				 		{
					 		marker = new google.maps.Marker({
								position : new google.maps.LatLng(v.lat,v.lng),
								center:new google.maps.LatLng(v.lat,v.lng),
								icon:intermediatePointIcon,
								map : map
							});
					 		
					         google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
					        	 infowindow.setContent(buildDataGrid(v));
									infowindow.open(map, marker);
								}})(marker, i));
				 		}
				 		
				 	}
				});
				}
				
				$("#kms").text(totalDistance.toFixed(1)+" KM");
				var linePath = new google.maps.Polyline({
                    path: linePoints,
                    geodesic: true,
                    strokeColor: '#28F81D',
                    strokeOpacity: 1.0,
                    strokeWeight:2,
                    icons: [
						
						{	
						      icon: lineSymbol,
						      offset: '5%'
					   	},
						{	
						      icon: lineSymbol,
						      offset: '12.5%'
						},
	   		          	{
	    		            icon: lineSymbol,
	    		            offset: '25%'
	   		          	},
	   		          	{
        		            icon: lineSymbol ,
        		            offset: '37.5%'
	       		        },
	       		     	
	   		         	{
	    		            icon: lineSymbol,
	    		            offset: '50%'
	   		          	},
	   		         	
	   		          	{
        		            icon: lineSymbol,
        		            offset: '62.5%'
	       		        },
	       		     	
	   		         	{
	    		            icon: lineSymbol,
	    		            offset: '76%'
	   		          	},
	   		         	
	   		          	{
        		            icon: lineSymbol,
        		            offset: '87.5%'
	       		        },
	       		     
	   		          	{
	    		            icon: lineSymbol,
	    		            offset: '100%'
	   		          	}
                   ],
                  });

				 linePath.setMap(map);
				
				 bounds.extend(new google.maps.LatLng(parseFloat(linePoints[0].lat),parseFloat(linePoints[0].lng)));
				 map.fitBounds(bounds);
				 
				 var listener = google.maps.event.addListener(map, "idle", function () {
					    map.setZoom(15);
					    google.maps.event.removeListener(listener);
					});
			
			//mapLocation(linePoints);
		}
			function calculateDistance(latslon,id){
				//	alert(latslon);
					var latbg =latslon.split(':');
					var distnace = 0;
					//alert(JSON.stringify(latbg.length));
					$.each( latbg, function( index, value ) {
						if(index>0 && value!='' ){
							destLat  = value;
							sourcelat = latbg[index-1];
							var start=[new google.maps.LatLng(sourcelat.split("~")[0], sourcelat.split("~")[1])];
							var end=[new google.maps.LatLng(destLat.split("~")[0], destLat.split("~")[1])];
						//	alert(start);
							var service = new google.maps.DistanceMatrixService();
						    service.getDistanceMatrix({
						        origins:start,
						        destinations: end,
						        travelMode: google.maps.TravelMode.DRIVING,
						        unitSystem: google.maps.UnitSystem.METRIC,
						        avoidHighways: false,
						        avoidTolls: false
						    }, function (response, status) {
						        if (status == google.maps.DistanceMatrixStatus.OK && response.rows[0].elements[0].status != "ZERO_RESULTS") {
						        	distnace = parseFloat(distnace) + parseFloat(response.rows[0].elements[0].distance.value);
						        	$('#'+id).html((distnace/1000).toFixed(1)+"KM"); 
						        	
						 
						        } 
						    });
						  
						}
						
					});
				//	alert("dis"+distnace);
					
				}
			function calculatewaypoint(data) {
				var waypts = [];
				var gaps = newIncLength = 21;
			
				var passedData = {};
				var timeout = 0;
				var bounds;
				//var map;

			  for (var i = 0; i < data.length; i++) {
			    waypts.push({
			      location: parseFloat(data[i].lat) + ',' + parseFloat(data[i].lng),
			      stopover: false
			    });
			    if (data.length >= 21) {
			      if (i == newIncLength) {
			        newIncLength = newIncLength + gaps;
			        (function(i, passedData, waypts, origin, dest) {
			          var id = '';
			          id = drawingRoute(passedData, timeout, waypts, origin, dest);
			          timeout = timeout + 1000;
			        })(i, passedData, waypts, waypts[0], waypts[waypts.length - 1]);
			        waypts = [];
			      }
			      if (i == (data.length - 1)) {
			        (function(i, passedData, waypts, origin, dest) {
			          var id = '';
			          id = drawingRoute(passedData, timeout, waypts, origin, dest);
			          timeout = timeout + 1000;
			        })(i, passedData, waypts, waypts[0], waypts[waypts.length - 1]);
			        waypts = [];
			      }
			    }
			  }
			}

			function drawingRoute(timeout, wayptss, origin, dest) {
				
				 var directionsDisplay;
				 var directionsService = new google.maps.DirectionsService();
				/*  directionsDisplay = new google.maps.DirectionsRenderer({
					    suppressMarkers: true
					}); */
				//alert(JSON.stringify(origin));
			  setTimeout(function() {
			    directionsService.route({
			      origin: origin,
			      destination:dest,
			      waypoints: wayptss,
			      optimizeWaypoints: false,
			      travelMode: 'DRIVING'
			    }, function(response, status) {
			      console.log(response)
			      if (status === 'OK') {
			        var directionsDisplay = new google.maps.DirectionsRenderer({preserveViewport: true,  suppressMarkers: true});
			        directionsDisplay.setMap(map);
			        directionsDisplay.setDirections(response);
			        bounds.union(response.routes[0].bounds);
			        map.fitBounds(bounds);
			      }
			    })
			  }, timeout);
			}
			function mapLocation(linePoints) {
				 var directionsDisplay;
				 var directionsService = new google.maps.DirectionsService();
				 directionsDisplay = new google.maps.DirectionsRenderer();
				 bounds = new google.maps.LatLngBounds();
				 directionsDisplay.setMap(map);
				 var coordSize = (linePoints.length)-1;
					var infowindow = new google.maps.InfoWindow();
				
				 var startLayt = new google.maps.LatLng(linePoints[0].lat, linePoints[0].lng);
				var endLon= new google.maps.LatLng(linePoints[coordSize].lat, linePoints[coordSize].lng);
				 var wayObj = new Object();
				 var waypts = new Array();
				 marker = new google.maps.Marker({
						position : startLayt,
						startLayt,
						icon: startingPointIcon,
						map : map
					});
			 google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
					infowindow.setContent(buildDataGrid(linePoints[0]));
					infowindow.open(map, marker);
			}})(marker, 0));
			 
			 marker = new google.maps.Marker({
					position : endLon,
					endLon,
					icon: endPointIcon,
					map : map
				});
		 google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
				infowindow.setContent(buildDataGrid(linePoints[coordSize]));
				infowindow.open(map, marker);
		}})(marker, coordSize));
				//  if(coordSize<=25){
					var objName ='obj' ;
					setDistance(linePoints);
					//alert(coordSize);
					 for(var i=0;i<=coordSize;i++){
						
						 if(!isEmpty(linePoints[i].lat)&&!isEmpty(linePoints[i].lng)){
							
								// setDistance(linePoints[i].lat,linePoints[i].lng,linePoints[i-1].lat,linePoints[i-1].lng)
								
							 var location = new google.maps.LatLng(linePoints[i].lat, linePoints[i].lng);
							
							 waypts.push({
								 location: location, 
								 stopover: true
							 });
								/* if(i==0)
								{
								alert("ss"+JSON.stringify(waypts));
								}*/
							 if(i%20==0 && i>0){
								// wayObj[objName] =waypts;
								 objName=objName+i;
							 	/*  var start = waypts[0].location
									var end = waypts[waypts.length-1].location
								 drawingRoute(i, waypts, start, end);
								 objName=objName+i; */
								 
							 	 //alert("ff"+JSON.stringify(waypts));
								 waypts = new Array();
								 waypts.push({
									 location: location, 
									 stopover: true
								 });
							 }else{
								 wayObj[objName] =waypts;
								 
							 }
							 var icon;
						   marker = new google.maps.Marker({
									position : location,
									center:location,
									icon: intermediatePointIcon,
									map : map
								});  
						 google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
								infowindow.setContent(buildDataGrid(linePoints[i]));
								infowindow.open(map, marker);
						}})(marker, i));
							// waypts.push(new google.maps.LatLng(linePoints[i].lat, linePoints[i].lng));
						 }
					
						
						
					 }
					// alert(JSON.stringify(wayObj));
					 
			/* 	 }else {
					 var coordinatesFinalArr = new Array();
					 var size = ((linePoints.length)/25);
					 size = Math.ceil(size);
					 var splitArr = new Array();
					 var startIndex = 0;
						for(var i=1;i<=size;i++){
							var endIndex = (i*25);
							splitArr[i-1] = linePoints.slice(startIndex,endIndex);
							startIndex = endIndex; 
						}
						var modSize = splitArr.length;
						for(i=0;i<modSize;i++){
							for(j=0;j<splitArr[i].length;j++){
								if(j%modSize==0){
									coordinatesFinalArr.push(splitArr[i][j]);
								}
							}
						}
						
						for(var i=1;i<coordinatesFinalArr.length;i++){
							 if(!isEmpty(coordinatesFinalArr[i].lat)&&!isEmpty(coordinatesFinalArr[i].lng)){
								 var location = new google.maps.LatLng(coordinatesFinalArr[i].lat, coordinatesFinalArr[i].lng);
								 waypts.push({
									 location: location, 
									 stopover: true
								 });
								 
							 }
						 }
				 } */
				 for (var key in wayObj) {
					 if (wayObj.hasOwnProperty(key)) {
						 var wayPtts = wayObj[key];
						 var ptsSize = (wayPtts.length)-1;
						 var start = wayPtts[0].location
						 //alert( wayPtts[0].location);
							var end = wayPtts[ptsSize].location
							var bounds = new google.maps.LatLngBounds();
						 	 
						        bounds.extend(start);
						        bounds.extend(end);
						        map.fitBounds(bounds);
						        
					        var request = {
					                origin: start,
					                destination: end,
					                travelMode: google.maps.TravelMode.WALKING,
					                optimizeWaypoints: false,
					               waypoints: wayPtts
					            };
					      
					        directionsService.route(request, function (response, status) {
					            if (status == google.maps.DirectionsStatus.OK) {
					            	 var directionsDisplay = new google.maps.DirectionsRenderer({preserveViewport: true,suppressMarkers: true});
					            	    directionsDisplay.setMap(map);
					            	    directionsDisplay.setDirections(response);
					            	    // combine the resulting bounds
					            	    bounds.union(response.routes[0].bounds);
					            	    // zoom the map to show the whole route
					            	    map.fitBounds(bounds);
					            	 
					            	    
					                directionsDisplay.setDirections(response);
					                directionsDisplay.setMap(map); 
					            } else {
					                alert("Directions Request from " + start.toUrlValue(6) + " to " + end.toUrlValue(6) + " failed: " + status);
					            }
					        });
					    }
					 
				 } 
				/*  var bounds = new google.maps.LatLngBounds();
			        bounds.extend(start);
			        bounds.extend(end);
			        map.fitBounds(bounds);
			        
		        var request = {
		                origin: start,
		                destination: end,
		                travelMode: google.maps.TravelMode.WALKING,
		                optimizeWaypoints: false,
		               waypoints: waypts
		            };
		      
		        directionsService.route(request, function (response, status) {
		            if (status == google.maps.DirectionsStatus.OK) {
		                directionsDisplay.setDirections(response);
		                directionsDisplay.setMap(map);
		            } else {
		                alert("Directions Request from " + start.toUrlValue(6) + " to " + end.toUrlValue(6) + " failed: " + status);
		            }
		        }); */
			} 
			var counter=0;
			function buildDataGrid(v){
				var geocoder = new google.maps.Geocoder;
				var content = "<table class='table table-responsive table-hover table-bordered'>";
				
				content += "<tr>";
				content += '<td colspan="4"><b><s:property value="%{getLocaleProperty('geoPoints.Info')}" /></b></td>';
				content += "</tr>"
			
				content += "<tr>";
				content += td('<s:property value="%{getLocaleProperty('dateTime')}" />');
				content += td('<s:property value="%{getLocaleProperty('agentName')}" />');
				content += td('<s:property value="%{getLocaleProperty('deviceName')}" />');
				content += td('<s:property value="%{getLocaleProperty('customer.address')}" />');
				content += "</tr>";
				
				counter++;
				var address = "";
				var latlng = {lat: parseFloat(v.lat), lng: parseFloat(v.lng)};
				geocoder.geocode({'location': latlng}, function(results, status) {
					if (status === 'OK') {
						if (results[0]) {
							address = results[1].formatted_address;
							$(".address"+counter).text(address);
						}
					}
				});
				
				
				content += "<tr>";
				content += td(v.dt);
				content += td(v.agent);
				content += td(v.device);
				content += "<td class='address"+counter+"'></td>";
				content += "</tr>";
				
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
	/*function locateMap(stations){
		var startingPointImg = "start.png";
		var endPointImg = "end.png";
		var intermediateImg = "blue_placemarker.png";
		
		var startingPointIcon = temp + '/img/'+startingPointImg;
		var endPointIcon = temp + '/img/'+endPointImg;
		var intermediatePointIcon = temp + '/img/'+intermediateImg;

		 var service = new google.maps.DirectionsService;
			var infowindow = new google.maps.InfoWindow();
		var lngs = stations.map(function(station) { return station.lng; });
	    var lats = stations.map(function(station) { return station.lat; });
	    map.fitBounds({
	        west: Math.min.apply(null, lngs),
	        east: Math.max.apply(null, lngs),
	        north: Math.min.apply(null, lats),
	        south: Math.max.apply(null, lats),
	    });

	    // Show stations on the map as markers
	    /* for (var i = 0; i < stations.length; i++) {
	    	
	    	var icon;
		
		 if(i==0){
			 icon = startingPointIcon;
			 
		 }else if(i == stations.length-1){
			 icon = endPointIcon;
		 }else{
			// alert(intermediatePointIcon);
			 icon=intermediatePointIcon;
		 }
		 if(i>0){
		 setDistance(stations[i].lat,stations[i].lng,stations[i-1].lat,stations[i-1].lng)
		 }
		 marker =    new google.maps.Marker({
	            position: stations[i],
	            map: map,
	            title: stations[i].name,
	            icon:icon
	        });
		 
		 google.maps.event.addListener(marker, 'click',(function(marker, i) {return function() {
				infowindow.setContent(buildDataGrid(stations[i]));
				infowindow.open(map, marker);
		}})(marker, i));
	    }

	    // Divide route to several parts because max stations limit is 25 (23 waypoints + 1 origin + 1 destination)
	    for (var i = 0, parts = [], max = 25 - 1; i < stations.length; i = i + max)
	        parts.push(stations.slice(i, i + max + 1));

	    // Service callback to process service results
	    var service_callback = function(response, status) {
	        if (status != 'OK') {
	            console.log('Directions request failed due to ' + status);
	            return;
	        }
	        var renderer = new google.maps.DirectionsRenderer;
	        renderer.setMap(map);
	        renderer.setOptions({ suppressMarkers: true, preserveViewport: true });
	        renderer.setDirections(response);
	    };

	    // Send requests to service to get route (for stations count <= 25 only one request will be sent)
	    for (var i = 1; i < parts.length; i++) {
	        // Waypoints does not include first station (origin) and last station (destination)
	        var waypoints = [];
	        for (var j = 1; j < parts[i].length - 1; j++){
	        	
	            waypoints.push({location: parts[i][j], stopover: false});
	        }
	        console.log(JSON.stringify(waypoints));
	        // Service options
	        var service_options = {
	            origin: parts[i][0],
	            destination: parts[i][parts[i].length - 1],
	            waypoints: waypoints,
	            travelMode: 'DRIVING',
	            optimizeWaypoints:true
	        };
	        // Send request
	        service.route(service_options, service_callback);
	    }
	  } */
	}
			
		</script>
		<script async defer
			src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
		<%-- <script src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap&libraries=geometry"></script> --%>

		<%-- <script src="js/maps.js?v=3.28"></script>	 --%>
		<script src="js/maplabel-compiled.js?k=1.16"></script>
	</body>
		<%-- function mapLocation(linePoints) {
			 var directionsDisplay;
			 var directionsService = new google.maps.DirectionsService();
			 directionsDisplay = new google.maps.DirectionsRenderer();
			 directionsDisplay.setMap(map);
			 var coordSize = (linePoints.length)-1;
			 var start = new google.maps.LatLng(linePoints[0].lat, linePoints[0].lng);
			 var end = new google.maps.LatLng(linePoints[coordSize].lat, linePoints[coordSize].lng);
			 
			 var waypts = new Array();
			  if(coordSize<=25){
				 for(var i=1;i<coordSize;i++){
					 if(!isEmpty(linePoints[i].lat)&&!isEmpty(linePoints[i].lng)){
						 var location = new google.maps.LatLng(linePoints[i].lat, linePoints[i].lng);
						 waypts.push({
							 location: location, 
							 stopover: true
						 });
						// waypts.push(new google.maps.LatLng(linePoints[i].lat, linePoints[i].lng));
					 }
				 }
			 }else {
				 var coordinatesFinalArr = new Array();
				 var size = ((linePoints.length)/25);
				 size = Math.ceil(size);
				 var splitArr = new Array();
				 var startIndex = 0;
					for(var i=1;i<=size;i++){
						var endIndex = (i*25);
						splitArr[i-1] = linePoints.slice(startIndex,endIndex);
						startIndex = endIndex; 
					}
					var modSize = splitArr.length;
					for(i=0;i<modSize;i++){
						for(j=0;j<splitArr[i].length;j++){
							if(j%modSize==0){
								coordinatesFinalArr.push(splitArr[i][j]);
							}
						}
					}
					
					for(var i=1;i<coordinatesFinalArr.length;i++){
						 if(!isEmpty(coordinatesFinalArr[i].lat)&&!isEmpty(coordinatesFinalArr[i].lng)){
							 var location = new google.maps.LatLng(coordinatesFinalArr[i].lat, coordinatesFinalArr[i].lng);
							 waypts.push({
								 location: location, 
								 stopover: true
							 });
							 
						 }
					 }
			 }
			 var bounds = new google.maps.LatLngBounds();
		        bounds.extend(start);
		        bounds.extend(end);
		        map.fitBounds(bounds);
		        
	        var request = {
	                origin: start,
	                destination: end,
	                travelMode: google.maps.TravelMode.WALKING,
	                optimizeWaypoints: false,
	               waypoints: waypts
	            };
	      
	        directionsService.route(request, function (response, status) {
	            if (status == google.maps.DirectionsStatus.OK) {
	                directionsDisplay.setDirections(response);
	                directionsDisplay.setMap(map);
	            } else {
	                alert("Directions Request from " + start.toUrlValue(6) + " to " + end.toUrlValue(6) + " failed: " + status);
	            }
	        });
		}
		var counter=0;
		function buildDataGrid(v){
			var geocoder = new google.maps.Geocoder;
			var content = "<table class='table table-responsive table-hover table-bordered'>";
			
			content += "<tr>";
			content += '<td colspan="4"><b><s:property value="%{getLocaleProperty('geoPoints.Info')}" /></b></td>';
			content += "</tr>"
		
			content += "<tr>";
			content += td('<s:property value="%{getLocaleProperty('dateTime')}" />');
			content += td('<s:property value="%{getLocaleProperty('agentName')}" />');
			content += td('<s:property value="%{getLocaleProperty('deviceName')}" />');
			content += td('<s:property value="%{getLocaleProperty('customer.address')}" />');
			content += "</tr>";
			
			counter++;
			var address = "";
			var latlng = {lat: parseFloat(v.lat), lng: parseFloat(v.lng)};
			geocoder.geocode({'location': latlng}, function(results, status) {
				if (status === 'OK') {
					if (results[0]) {
						address = results[1].formatted_address;
						$(".address"+counter).text(address);
					}
				}
			});
			
			
			content += "<tr>";
			content += td(v.dt);
			content += td(v.agent);
			content += td(v.device);
			content += "<td class='address"+counter+"'></td>";
			content += "</tr>";
			
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

		
	</script>
	<script
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap&libraries=geometry"></script>

	<script src="js/maps.js?v=3.28"></script>	
	<script src="js/maplabel-compiled.js?k=1.16"></script>
</body> --%>