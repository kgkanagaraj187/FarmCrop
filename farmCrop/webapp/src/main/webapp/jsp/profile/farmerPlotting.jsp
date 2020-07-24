<%@ taglib uri="/struts-tags" prefix="s" %>
  <%@ include file="/jsp/common/detail-assets.jsp" %>
    <%@ include file="/jsp/common/grid-assets.jsp" %>
      <%@ include file="/jsp/common/report-assets.jsp" %>

        <head>
          <META name="decorator" content="swithlayout">
        </head>

        <body>
          <style>
            /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
            
            #map {
              height: 100%;
            }
            /* Optional: Makes the sample page fill the window. */
            
            html,
            body {
              height: 100%;
              margin: 0;
              padding: 0;
            }
            #description {
              font-family: Roboto;
              font-size: 15px;
              font-weight: 300;
            }
            #infowindow-content .title {
              font-weight: bold;
            }
            #infowindow-content {
              display: none;
            }
            #map #infowindow-content {
              display: inline;
            }
            .pac-card {
              margin: 10px 10px 0 0;
              border-radius: 2px 0 0 2px;
              box-sizing: border-box;
              -moz-box-sizing: border-box;
              outline: none;
              box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
              background-color: #fff;
              font-family: Roboto;
            }
            #pac-container {
              padding-bottom: 12px;
              margin-right: 12px;
            }
            .pac-controls {
              display: inline-block;
              padding: 5px 11px;
            }
            .pac-controls label {
              font-family: Roboto;
              font-size: 13px;
              font-weight: 300;
            }
            #pac-input {
              background-color: #fff;
              font-family: Roboto;
              font-size: 15px;
              font-weight: 300;
              margin-left: 12px;
              padding: 0 11px 0 13px;
              text-overflow: ellipsis;
              width: 400px;
            }
            #pac-input:focus {
              border-color: #4d90fe;
            }
            #title {
              color: #fff;
              background-color: #4d90fe;
              font-size: 16px;
              font-weight: 500;
              padding: 6px 12px;
            }
          </style>

          <div class="appContentWrapper marginBottom">
            <section class='reportWrap row'>

              <div class="error">
                <p class="notification">
                  <%-- <span class="manadatory">*</span>
                    <s:text name="reqd.field" /> --%>
                    <div id="validateError" style="text-align: center;"></div>
                </p>
              </div>

              <div class="gridly">


                <div class="col-md-3">
                  <div class="form-group">
                    <label for="group"><s:property value="%{getLocaleProperty('group')}" />
                    </label>
                    <s:select id="group" name="selectedGroup" headerKey="" headerValue="%{getText('txt.select')}" onChange="loadFarmer();" cssClass="form-control select2" list="groupList" listKey="key" listValue="value" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="farmer"><s:property value="%{getLocaleProperty('farmer')}" />
                    </label>
                    <s:select id="farmer" name="selectedFarmer" headerKey="" headerValue="%{getText('txt.select')}" onChange="loadFarm();" cssClass="form-control select2" list="{}" listKey="key" listValue="value" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="farm"><s:property value="%{getLocaleProperty('farm')}" />
                    </label>
                    <s:select id="farm" name="selectedFarm" headerKey="" headerValue="%{getText('txt.select')}" onChange="loadFarmMap();loadFarmCrops();" cssClass="form-control select2" list="{}" listKey="key" listValue="value" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="farmCrop"><s:property value="%{getLocaleProperty('farmCrop')}" />
                    </label>
                    <s:select id="farmCrop" name="selectedFarmCrop" onchange="loadFarmCropMap(this.value)" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control select2" list="{}" listKey="key" listValue="value" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="farmType"><s:property value="%{getLocaleProperty('farmType')}" />
                    </label>
                    <s:select id="farmType" name="selectedFarmType" list="farmTypeList" headerKey="" headerValue="%{getText('txt.select')}" cssClass="form-control select2" />
                  </div>
                </div>


                <div class="filterEle">
                  <label for="txt"></label>
                  <div class="form-element">
                    <span id="savebutton" class=""> <span class="first-child">
								<button class="save-btn btn btn-success" type="button"
									class="save-btn" onclick="submitPlotting();">
									<font color="#FFFFFF"> <b><s:text name="save.button" /></b>
									</font>
								</button>
						</span>
                    </span>
                  </div>
                </div>
              </div>
            </section>
            <div class="pac-card" id="pac-card">
              <div>
                <div id="type-selector" class="pac-controls">
                  <input type="radio" name="type" id="changetype-all" checked="checked">
                  <label for="changetype-all">All</label>

                  <input type="radio" name="type" id="changetype-establishment">
                  <label for="changetype-establishment">Establishments</label>
                  <input type="radio" name="type" id="changetype-address">
                  <label for="changetype-address">Addresses</label>
                  <input type="radio" name="type" id="changetype-geocode">
                  <label for="changetype-geocode">Geocodes</label>
                </div>
                <div id="strict-bounds-selector" class="pac-controls">
                  <input type="checkbox" id="use-strict-bounds" value="">
                  <label for="use-strict-bounds">Strict Bounds</label>
                </div>
              </div>
              <div id="pac-container">
                <input id="pac-input" type="text" placeholder="Enter a location">
              </div>
              <div class="pull-right">
                <button id="delete-button">
                  <i class="fa fa-trash"></i>Delete Selected Shape
                </button>
                <button id="delete-button-exist">
                  <i class="fa fa-trash"></i>Delete Existing Plot
                </button>
              </div>
            </div>
            <s:hidden id="area" />
            <s:hidden id="farmLatLon" />
            
            <input onclick="clearMarkers();" type=button value="Hide Markers" class="btn-primary">
			<input onclick="showMarkers();" type=button value="Show Markers" class="btn-info">
			<input onclick="deleteMarkers();" type=button value="Delete Markers" class="btn-warning">
            
            <div id="map"></div>
          </div>

          <script>
            var selectedShape;
            var mapLabel2;
            var coorArr = new Array();
            var drawingManager;
            var plotting;
            var new_paths;
            var alloverlays = [];
            var markers = [];
            var plotCOunt = 0;
    		var tenantId='<s:property value="getCurrentTenantId()"/>';
            jQuery(document).ready(function() {
              $("#map").css("height", (($(window).innerHeight()) - 80));
            });

            function loadFarmer() {
            	document.getElementById("validateError").innerHTML ="";
            	var group = $("#group").val();
              //alert("group:" + group);
              var idsArray = ['farmer', 'farm', 'farmCrop'];
              resetSelect2(idsArray);
              jQuery.post("farmerPlotting_populateFarmer.action", {
                selectedGroup: group
              }, function(result) {
                insertOptions("farmer", jQuery.parseJSON(result));
              });

            }

            function loadFarm() {
            	document.getElementById("validateError").innerHTML ="";
            	var farmer = $("#farmer").val();
              $("#farm").empty();
              var idsArray = ['farm', 'farmCrop'];
              resetSelect2(idsArray);

              jQuery.post("farmerPlotting_populateFarm.action", {
                selectedFarmer: farmer
              }, function(result) {
                insertOptions("farm", jQuery.parseJSON(result));
              });
              resetSelect2();
            }
            function resetSelect2(){
    			$(".select2").select2();
    		}
            function loadFarmCrops() {
              //alert("loadFarmCrops");
              var farm = $("#farm").val();
              var farmId = new Array();
              if (farm != '' && farm != null) {
                farmId = farm.split("~");

                var idsArray = ['farmCrop'];
                resetSelect2(idsArray);

                //alert("farmId:" + farmId);
                jQuery.post("farmerPlotting_populateFarmCrop.action", {
                  selectedFarm: farmId[0]
                }, function(result) {
                  insertOptions("farmCrop", jQuery.parseJSON(result));
                });
              
              }
            }

            function submitPlotting() {
            	document.getElementById("validateError").innerHTML ="";
            	var hit = true;
              var farmIdse = $("#farm").val();
              var farmId = new Array();
			  var group =$("#group").val();
			  var farmer =$("#farmer").val();
			    if (farmIdse != '' && farmIdse != null) {
			  farmId = farmIdse.split("~");
			    }
              var farmCropId = $("#farmCrop").val();
              //alert("farmCropId:" + farmCropId);
              var farmTypeId = $("#farmType").val();
              //alert("farmTypeId:" + farmTypeId);
              var selectedFarm = '';
              if (farmTypeId == '2') {
                selectedFarm = farmCropId;
              } else {
                selectedFarm = farmId[0];
              }

			if(isEmpty(group)){
					 document.getElementById("validateError").innerHTML = '<s:text name="empty.group"/>';
				     hit = false;
				     return false;
				}
			if(isEmpty(farmer)){
				 document.getElementById("validateError").innerHTML = '<s:text name="empty.farmer"/>';
			     hit = false;
			     return false;
			}
              if (farmIdse == "") {
                document.getElementById("validateError").innerHTML = '<s:text name="empty.farm"/>';
                hit = false;
                return false;
              }
			/* if(isEmpty(farmCropId)){
				 document.getElementById("validateError").innerHTML = '<s:text name="empty.farmcrop"/>';
			     hit = false;
			     return false;
			} */
              if (farmTypeId == "") {
                document.getElementById("validateError").innerHTML = '<s:text name="empty.farmType"/>';
                hit = false;
                return false;
              } else if (!isEmpty(farmIdse) && !isEmpty(farmCropId)) {
                //if(farmTypeId=='1'|| farmTypeId=='2'){
                if (farmTypeId == '1') {
                  document.getElementById("validateError").innerHTML ='<s:property value="%{getLocaleProperty('empty.culti')}" />' ;
                  hit = false;
                  return false;
                }
                /* else{
																					document.getElementById("validateError").innerHTML = '<s:text name="empty.farmplotArea"/>';
																					hit = false;
																					return false;
																				} */
              } else if (!isEmpty(farmIdse) && isEmpty(farmCropId)) {
                //if(farmTypeId=='3'){
                if (farmTypeId == '2') {
                  document.getElementById("validateError").innerHTML = '<s:text name="empty.farmcrop"/>';
                  hit = false;
                  return false;
                }
              } else {
                document.getElementById("validateError").innerHTML = '<s:text name="empty.farm"/>';
                hit = false;
                return false;
              }

              var latLonArr = new Array();

              $(coorArr).each(function(k, v) {
                var latLon = v.split(",");

                if (!isEmpty(latLon[0]) && !isEmpty(latLon[1])) {

                  latLonArr.push({
                    'farmId': farmId[0],
                    'latitude': latLon[0],
                    'longitude': latLon[1],
                    'orderNo': k + 1,

                  });
                }

              });
              if (latLonArr.length == "" || latLonArr.length == null) {
                document.getElementById("validateError").innerHTML = '<s:text name="empty.coordinates"/>';
                hit = false;
                return false;
              }

              if (hit && plotCOunt == 1) {
                //alert("hit");
               // alert(JSON.stringify(neighbouringDetails_Result()))
                 latLonArr.push({
                    'nearByLandDetails': neighbouringDetails_Result()
                  });
                
                var flag = true;
                var latLon = JSON.stringify(latLonArr);
                var data = {
                  latLon: latLon,
                  selectedFarm: selectedFarm,
                  area: $('#area').val(),
                  farmLatLon: $('#farmLatLon').val(),
                  farmCode: farmTypeId

                }
                $.ajax({
                  url: 'farmerPlotting_create.action',
                  type: 'post',
                  dataType: 'json',
                  data: data,
                  success: function(data, result) {

                  },
                });
                alert("Saved Successfully");
                resetForm();
                location.reload();
              } else if (plotCOunt > 1) {
                alert("Multiple Plot Exist");
              } else {
                resetForm();
              }

            }

            function resetForm() {
              $("#group").val("");
              $("#farmer").val("");
              $("#farm").val("");
              $("#farmCrop").val("");
              $("#farmType").val("");

            }

            function loadFarmMap() {

              var dataArr = new Array();
              var landarry = new Array();
              for (var i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
              }
              markers = [];
              deletPolygon();
              plotCOunt = 0;
              var farmIdd = $("#farm").val();

              if (farmIdd != '' && farmIdd != null) {
                var farmId = new Array();
                farmId = farmIdd.split("~");
                //alert("dd" + farmId[2]);
                if (farmId.length = 3 && farmId[1] != '' && farmId[2] != '' && farmId[1] != undefined && farmId[2] != undefined) {

                  dataArr.push({
                    latitude: parseFloat(farmIdd.split("~")[1]),
                    longitude: parseFloat(farmIdd.split("~")[2])

                  });
                  var data = {

                    selectedFarm: farmId[0].trim(),
                    selectedFarmType: "1"


                  }
                  $.ajax({
                    url: 'farmerPlotting_populateFarmCoordinates.action',
                    type: 'post',
                    dataType: 'json',
                    async: false,
                    data: data,
                    success: function(data, result) {
                      landarry = data;

                    },
                  });


                  loadMap(dataArr, landarry.coord, landarry.area);
                  if(landarry.neighbouringDetails_farm != undefined){
                  	addExistingMarkers(landarry.neighbouringDetails_farm);
                  }
				  
                }


              }
            }

            function loadFarmCropMap(jVal) {

              if (jVal != null && jVal != '') {
                var dataArr = new Array();
                var landarry = new Array();
                for (var i = 0; i < markers.length; i++) {
                  markers[i].setMap(null);
                }
                markers = [];
                deletPolygon();
                plotCOunt = 0;

                var data = {

                  selectedFarm: jVal.trim(),
                  selectedFarmType: "2"


                }
                $.ajax({
                  url: 'farmerPlotting_populateFarmCoordinates.action',
                  type: 'post',
                  dataType: 'json',
                  async: false,
                  data: data,
                  success: function(data, result) {
                    landarry = data;

                  },
                });


                loadMap(dataArr, landarry.coord, landarry.area);
                if(landarry.neighbouringDetails_farmCrops != undefined){
                	addExistingMarkers(landarry.neighbouringDetails_farmCrops);
                }
              }
            }

            function initMap() {

              var url = window.location.href;
              var temp = url;

              for (var i = 0; i < 1; i++) {
                temp = temp.substring(0, temp.lastIndexOf('/'));
              }
              var intermediateImg = "red_placemarker.png";
              var intermediatePointIcon = temp + '/img/' + intermediateImg;
				if(tenantId=='wilmar'){
					
					 map = new google.maps.Map(document.getElementById('map'), {
				         center: {
				           lat: 12.8797,
				           lng: 121.7740
				         },
				         zoom:6,
				         mapTypeId: google.maps.MapTypeId.HYBRID,
				       });
					
					 map.addListener('click', function(event) {
							
						 var myLatLng = event.latLng;
						/*  var latLng = {};
						 latLng["lat"] = myLatLng.lat();
						 latLng["lng"] = myLatLng.lng(); */
						  
						  addMarker(event.latLng);
						  
					 });
					 
				}else{
					 map = new google.maps.Map(document.getElementById('map'), {
				         center: {
				        	 lat : 11.0168,
								lng : 76.9558
				         },
				         zoom:3,
				         mapTypeId: google.maps.MapTypeId.HYBRID,
				       });
				}
             /*  map = new google.maps.Map(document.getElementById('map'), {
                center: {
                  lat: 12.8797,
                  lng: 121.7740
                },
                zoom:5,
                mapTypeId: google.maps.MapTypeId.HYBRID,
              });
 */
              var polyOptions = {
                strokeWeight: 0,
                fillOpacity: 0.45,
                editable: true,
                draggable: false
              };

              drawingManager = new google.maps.drawing.DrawingManager({
                drawingMode: google.maps.drawing.OverlayType.POLYGON,
                drawingControl: true,
                drawingControlOptions: {
                  position: google.maps.ControlPosition.TOP_CENTER,
                  /*  drawingModes : [ 'marker', 'circle', 'polygon', 'polyline',
							'rectangle' ]  */
                  drawingModes: ['marker', 'polygon']
                },
                markerOptions: {
                  icon: intermediatePointIcon,
                  draggable: true
                },
                polylineOptions: {
                  editable: true,
                  draggable: false
                },
                polygonOptions: polyOptions,
                circleOptions: {
                  fillColor: '#ffff00',
                  fillOpacity: 1,
                  strokeWeight: 5,
                  clickable: false,
                  editable: true,
                  zIndex: 1
                }
              });
              var polygonOptions = drawingManager.get('polygonOptions');
              polygonOptions.fillColor = '#50ff50';
              polygonOptions.strokeColor = '#50ff50';
              drawingManager.set('polygonOptions', polygonOptions);

              drawingManager.setMap(map);

              google.maps.event
                .addListener(
                  drawingManager,
                  'overlaycomplete',
                  function(e) {

                    if (e.type !== google.maps.drawing.OverlayType.MARKER) {
                      // Switch back to non-drawing mode after drawing a shape.
                      drawingManager.setDrawingMode(null);
                      drawingManager.setOptions({
                        drawingControl: false
                      });
                      // Add an event listener that selects the newly-drawn shape when the user
                      // mouses down on it.
                      var newShape = e.overlay;

                      newShape.type = e.type;
                      coorArr = new Array();
                      google.maps.event
                        .addListener(
                          newShape,
                          'click',
                          function(e) {
                            if (e.vertex !== undefined) {
                              if (newShape.type === google.maps.drawing.OverlayType.POLYGON) {
                                var path = newShape
                                  .getPaths()
                                  .getAt(
                                    e.path);
                                path
                                  .removeAt(e.vertex);
                                if (path.length < 3) {
                                  newShape
                                    .setMap(null);
                                }
                              }
                              if (newShape.type === google.maps.drawing.OverlayType.POLYLINE) {
                                var path = newShape
                                  .getPath();
                                path
                                  .removeAt(e.vertex);
                                if (path.length < 2) {
                                  newShape
                                    .setMap(null);
                                }
                              }
                            }
                            setSelection(newShape);
                          });

                      /* 	newShape
									.getPath().forEach(function(path, index){

										  google.maps.event.addListener(path, 'insert_at', function(){
											  changePolugon(newShape);
										  });

										  google.maps.event.addListener(path, 'remove_at', function(){

											  changePolugon(newShape);
										  });

										  google.maps.event.addListener(path, 'set_at', function(index, previous){
											  changePolugon(newShape);
											  
										  });

										}); */
                      setSelection(newShape);
                    }
                    var newShape = e.overlay;
                    newShape.type = e.type;

                    //if (newShape.type == google.maps.drawing.OverlayType.POLYGON) {
                    //alert("POLYGON");
                    var bounds = new google.maps.LatLngBounds();
                    var area = google.maps.geometry.spherical
                      .computeArea(newShape.getPath());
                    var metre = parseFloat(area).toFixed(2);

                    for (var i = 0; i < newShape.getPath()
                      .getLength(); i++) {
                      coorArr.push(newShape.getPath().getAt(i)
                        .toUrlValue(20));
                    }

                    google.maps.event.addListener(newShape.getPath(), 'insert_at', function() {
                      changePolugon(newShape);
                    });

                    google.maps.event.addListener(newShape.getPath(), 'remove_at', function() {

                      changePolugon(newShape);
                    });

                    google.maps.event.addListener(newShape.getPath(), 'set_at', function(index, previous) {
                      changePolugon(newShape);

                    });


                    plotCOunt = plotCOunt + 1;
                    console.log(coorArr);
                    //alert("coorArr:"+coorArr);
                    $(coorArr)
                      .each(
                        function(k, v) {
                          var latLon = v.split(",");
                          var coordinatesLatLng = new google.maps.LatLng(
                            latLon[0],
                            latLon[1]);
                          bounds
                            .extend(coordinatesLatLng);
                        });

                    var myLatlng = bounds.getCenter();

                    $('#farmLatLon').val(myLatlng);

                    //var acreConversion = (metre * 0.000247105);
                    var test = (metre * 0.000247105);
                    var acreConversion = parseFloat(test).toFixed(2);
                    //alert("acreConversion:"+acreConversion);

                    var test2 = (acreConversion / 2.4711);
                    var hectareConversion = parseFloat(test2).toFixed(2);
                    //var hectareConversion = (acreConversion / 2.4711);
                    //alert("hectareConversion:"+hectareConversion);
                    var arType = '<s:property value="%{getAreaType()}" />';
                    //alert("areaType:"+arType);
                    var textTpe = "";
                    if (arType == 'Acres') {
                      textTpe = acreConversion + "-" + arType;
                      $('#area').val(acreConversion);
                    } else {
                      textTpe = hectareConversion + "-" + arType;
                      //alert("textTpe:"+textTpe);
                      $('#area').val(hectareConversion);
                    }

                    mapLabel2 = new MapLabel({

                      text: textTpe,
                      position: myLatlng,
                      map: map,
                      fontSize: 14,
                      align: 'left'
                    });
                    mapLabel2.set('position', myLatlng);
                    //}
                  });

              var card = document.getElementById('pac-card');
              var input = document.getElementById('pac-input');
              var types = document.getElementById('type-selector');
              var strictBounds = document
                .getElementById('strict-bounds-selector');

              map.controls[google.maps.ControlPosition.TOP_RIGHT].push(card);

              var autocomplete = new google.maps.places.Autocomplete(input);

              // Bind the map's bounds (viewport) property to the autocomplete object,
              // so that the autocomplete requests use the current map bounds for the
              // bounds option in the request.
              autocomplete.bindTo('bounds', map);

              var infowindow = new google.maps.InfoWindow();
              var infowindowContent = document
                .getElementById('infowindow-content');
              infowindow.setContent(infowindowContent);
              var marker = new google.maps.Marker({
                map: map,
                anchorPoint: new google.maps.Point(0, -29)
              });
              markers.push(marker);
              autocomplete
                .addListener(
                  'place_changed',
                  function() {
                    infowindow.close();
                    marker.setVisible(false);
                    var place = autocomplete.getPlace();
                    if (!place.geometry) {
                      // User entered the name of a Place that was not suggested and
                      // pressed the Enter key, or the Place Details request failed.
                      window
                        .alert("No details available for input: '" + place.name + "'");
                      return;
                    }

                    // If the place has a geometry, then present it on a map.
                    if (place.geometry.viewport) {
                      map.fitBounds(place.geometry.viewport);
                    } else {
                      map.setCenter(place.geometry.location);
                      map.setZoom(17); // Why 17? Because it looks good.
                    }
                    marker.setPosition(place.geometry.location);
                    marker.setVisible(true);

                    var address = '';
                    if (place.address_components) {
                      address = [
                          (place.address_components[0] && place.address_components[0].short_name || ''), (place.address_components[1] && place.address_components[1].short_name || ''), (place.address_components[2] && place.address_components[2].short_name || '')
                        ]
                        .join(' ');
                    }
                  });

              // Sets a listener on a radio button to change the filter type on Places
              // Autocomplete.
              function setupClickListener(id, types) {
                var radioButton = document.getElementById(id);
                radioButton.addEventListener('click', function() {
                  autocomplete.setTypes(types);
                });
              }

              setupClickListener('changetype-all', []);
              setupClickListener('changetype-address', ['address']);
              setupClickListener('changetype-establishment', ['establishment']);
              setupClickListener('changetype-geocode', ['geocode']);

              document.getElementById('use-strict-bounds').addEventListener(
                'click',
                function() {
                  console.log('Checkbox clicked! New state=' + this.checked);
                  autocomplete.setOptions({
                    strictBounds: this.checked
                  });
                });

              // Clear the current selection when the drawing mode is changed, or when the
              // map is clicked.
              google.maps.event.addListener(drawingManager,
                'drawingmode_changed', clearSelection);
              google.maps.event.addListener(map, 'click', clearSelection);
              google.maps.event.addDomListener(document
                .getElementById('delete-button'), 'click',
                deleteSelectedShape);
            }

            function clearSelection() {
              if (selectedShape) {
                selectedShape.setEditable(false);
                selectedShape = null;
              }
            }

            function setSelection(shape) {
              clearSelection();
              selectedShape = shape;
              shape.setEditable(true);
            }

            /* 	function deleteSelectedShape() {
				alert("deleteSelectedShape");
				if (selectedShape) {
					alert("test");
					selectedShape.setMap(null);
					
				
					
				}
			} */

            function deleteSelectedShape() {

              if (selectedShape) {
                selectedShape.setEditable(false);

                selectedShape.setMap(null);
                mapLabel2.set('text', '');
                coorArr = [];
                plotCOunt = plotCOunt - 1;
                if (plotCOunt == 0) {
                  drawingManager.setOptions({
                    drawingControl: true
                  });

                  drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);
                }

              }

            }

            function loadMap(dataArr, landArea, area) {

              var url = window.location.href;
              var temp = url;
              coorArr = new Array();
              for (var i = 0; i < 1; i++) {
                temp = temp.substring(0, temp.lastIndexOf('/'));
              }
              var intermediateImg = "red_placemarker.png";
              var intermediatePointIcon = temp + '/img/' + intermediateImg;

              var infowindow = new google.maps.InfoWindow();

              var marker, i;
              $(dataArr).each(
                function(k, v) {
                  marker = new google.maps.Marker({
                    position: new google.maps.LatLng(v.latitude,
                      v.longitude),
                    icon: intermediatePointIcon,
                    map: map
                  });
                  markers.push(marker);
                  google.maps.event.addListener(marker, 'click', (function(marker, i) {
                    return function() {
                      infowindow.setContent(buildData(v));
                      infowindow.open(map, marker);
                    }
                  })(marker, i));
                  map.setCenter({
                    lat: v.latitude,
                    lng: v.longitude
                  });
                  map.setZoom(17);
                });
              var cords = new Array();
              var bounds = new google.maps.LatLngBounds();
              if (landArea.length > 0) {
                drawingManager.setOptions({
                  drawingControl: false
                });


                $(landArea).each(function(k, v) {
                  cords.push({
                    lat: parseFloat(v.lat),
                    lng: parseFloat(v.lon)
                  });
                  var coordinatesLatLng = new google.maps.LatLng(
                    parseFloat(v.lat),
                    parseFloat(v.lon));
                  coorArr.push(v.lat + "," + v.lon);
                  bounds
                    .extend(coordinatesLatLng);

                });
                plotting = new google.maps.Polygon({
                  paths: cords,
                  strokeColor: '#50ff50',
                  strokeOpacity: 0.8,
                  strokeWeight: 2,
                  fillColor: '#50ff50',
                  fillOpacity: 0.45,
                  editable: true,
                  draggable: false
                });
                plotting.setMap(map);
                plotCOunt = plotCOunt + 1;
                var overlay = {
                  overlay: plotting,
                  type: google.maps.drawing.OverlayType.POLYGON
                };

                google.maps.event.addDomListener(document
                  .getElementById('delete-button-exist'), 'click',
                  deletPolygon);
                drawingManager.setDrawingMode(null);
                map.fitBounds(bounds);
                var arType = '<s:property value="%{getAreaType()}" />';
                //alert("areaType:"+arType);
                var textTpe = "";

                textTpe = area + "-" + arType;
                $('#area').val(area);
                mapLabel2 = new MapLabel({

                  text: textTpe,
                  position: bounds.getCenter(),
                  map: map,
                  fontSize: 14,
                  align: 'left'
                });
                mapLabel2.set('position', bounds.getCenter());
                plotting.getPaths().forEach(function(path, index) {

                  google.maps.event.addListener(path, 'insert_at', function() {
                    changePolugon(plotting);
                  });

                  google.maps.event.addListener(path, 'remove_at', function() {

                    changePolugon(plotting);
                  });

                  google.maps.event.addListener(path, 'set_at', function(index, previous) {
                    changePolugon(plotting);

                  });

                });


                google.maps.event.addListener(plotting, 'rightclick', function(e) {
                  // Check if click was on a vertex control point
                  if (e.vertex == undefined) {
                    return;
                  } else {
                    removeVertex(e.vertex);
                  }
                });
              } else {
                drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);
              }


            }

            function changePolugon(newPlotting) {

              mapLabel2.set('text', '');
              new_paths = newPlotting.getPath();
              var bounds = new google.maps.LatLngBounds();
              coorArr = new Array();
              for (var i = 0; i < new_paths
                .getLength(); i++) {
                coorArr.push(new_paths.getAt(i)
                  .toUrlValue(20));
                var xy = new_paths.getAt(i);
                var coordinatesLatLng = new google.maps.LatLng(
                  parseFloat(xy.lat()),
                  parseFloat(xy.lng()));

                bounds
                  .extend(coordinatesLatLng);
              }
              console.log(coorArr);
              var area = google.maps.geometry.spherical
                .computeArea(new_paths);
              var metre = parseFloat(area).toFixed(2);
              var test = (metre * 0.000247105);
              var acreConversion = parseFloat(test).toFixed(2);
              var test2 = (acreConversion / 2.4711);
              var hectareConversion = parseFloat(test2).toFixed(2);
              var arType = '<s:property value="%{getAreaType()}" />';
              var textTpe = "";
              if (arType == 'Acres') {
                textTpe = acreConversion + "-" + arType;
                $('#area').val(acreConversion);
              } else {
                textTpe = hectareConversion + "-" + arType;
                $('#area').val(hectareConversion);
              }
              var myLatlng = bounds.getCenter();

              $('#farmLatLon').val(myLatlng);

              mapLabel2 = new MapLabel({

                text: textTpe,
                position: myLatlng,
                map: map,
                fontSize: 14,
                align: 'left'
              });
              mapLabel2.set('position', myLatlng);
            }

            function deletPolygon() {
              if (plotting != null) {
                plotting.setMap(null);
                mapLabel2.set('text', '');

                plotCOunt = plotCOunt - 1;

                if (plotCOunt == 0) {
                  drawingManager.setOptions({
                    drawingControl: true
                  });

                  drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);
                  plotting = null;
                }
              }


            }

            function removeVertex(vertex) {
              if (confirm("Are You sure to delete the point")) {
                var path = plotting.getPath();
                new_paths = plotting;
                path.removeAt(vertex);

              }
            }
            
            
            /* neighbouringDetails start  */
           
            var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
      		var labelIndex = 0;
      		var neighbouring_markers = [];
      		var neighbouring_result = {};
            
            function addMarker(position){
            	
            	var marker = new google.maps.Marker({
                    position: position,
                    map: map,
          		  	title: "",
                    draggable: false,
                   // label: labels[labelIndex++ % labels.length]
                  });
          		
            	 var infowindow = new google.maps.InfoWindow({
                     content: getHtmlForm(position)
                   });
                  
                 //google.maps.event.addListener(marker, "rightclick", function (point) {delMarker(marker,position)});
          		 marker.addListener('click', function() {
			          infowindow.open(map, marker);
			      });
          		 
          		 marker.addListener('rightclick', function() {
			          delMarker(marker,position)
			      });
          		  
          		  
          		neighbouring_markers.push(marker);
          		neighbouring_result[position.lat()+"_"+position.lng()] = neighbouringDetails_backgroundData();
            	
            }
            
            
            function delMarker(marker,position) {
    			var index = neighbouring_markers.indexOf(marker);
    			if (index != -1) {
    				var userOption = confirm("Confirm to delete marker");
    				if (userOption == true) {
    					neighbouring_markers.splice(index, 1);
    					marker.setMap(null);
    					try {
    						 delete neighbouring_result[position.lat()+"_"+position.lng()];
    					}catch(err) {
    						 delete neighbouring_result[position.lat+"_"+position.lng];
    					}
    				   
    				} 
    			}
    		}
          
            function getHtmlForm(position){
            	
          	  
          	  //$("#title_id").parent().parent().remove();
          	  var titleValue;
          	  var descValue;
          	  		try {
	          	  		var titleId = "title_"+position.lat()+"_"+position.lng();
	              		titleId = titleId.replace(/\./g,'z'); // removing dots (.) from lat and lng
	              	  	var desId = "desc_"+position.lat()+"_"+position.lng();
	              		desId = desId.replace(/\./g,'z');
	              		var lat = position.lat();
	              		var lng = position.lng();
					}
					catch(err) {
						var titleId = "title_"+position.lat+"_"+position.lng;
		          		titleId = titleId.replace(/\./g,'z'); // removing dots (.) from lat and lng
		          	  	var desId = "desc_"+position.lat+"_"+position.lng;
		          		desId = desId.replace(/\./g,'z');
		          		var lat = position.lat;
	              		var lng = position.lng;
	              		titleValue = position.title;
	              		descValue = position.description;
	              		
					}
          	  
          	 	
          	  
          		var parameters = {};
          		parameters["titleId"] = titleId;
          		parameters["desId"] = desId;
          		parameters["lat"] = lat;
          		parameters["lng"] = lng;
          		
          	  var container = jQuery('<div/>', {
          			class : "container-fluid"
          		});
          	  
          	  var form_group = jQuery('<div/>', {
        			class : "form-group"
        			});
          	  
          	  var textBox = jQuery('<input/>', {
          			id		:	titleId,
          			class	: "form-control",
          			type	: 'text',
          			placeholder : "Title"
          		});
          	  
          	  if(!isEmpty(titleValue)){
          		$(textBox).attr("value",titleValue);
          	  }
          	  
          	
      	  
      	   if(!isEmpty(descValue)){
      		 var textArea = '<textarea type="text" class="form-control" id='+desId+' >'+descValue+'</textarea>';
      	  }else{
      		var textArea = $('<textarea/>',{
      			id		:	desId,
      			class	: "form-control",
      			placeholder : "Description"
      		}); 
      	  }
          		
          	 var btn = $('<button/>',{
          			class	:	"btn btn-sts",
          			id 		:	"neighbouringDetails_add_btn",
          			text	:	"Submit",
          			onclick : "saveNeighbouringDetails("+JSON.stringify(parameters)+")",
          			style	:	"border-radius: 7px;margin-top: 10px;background-color: #2a3f54;"
          		});
          		
          		
          		
          		$(form_group).append($(textBox));
          		$(form_group).append("<br>");
          		$(form_group).append($(textArea));
          		
          		$(container).append($(form_group));
          		$(container).append($(btn));
          		
          		return $(container).html();
          		
            }
            
            function saveNeighbouringDetails(jsonPara){
            	
            	var title = $("#"+jsonPara.titleId).val();
            	var description = $("#"+jsonPara.desId).val();
            	
            	var data = neighbouringDetails_backgroundData();
            	data["title"] = title;
            	data["description"] = description;
            	data["lat"] = jsonPara.lat;
            	data["lng"] = jsonPara.lng;
            	neighbouring_result[jsonPara.lat+"_"+jsonPara.lng] = data;
            	//alert(JSON.stringify(neighbouring_result))
            	$("#"+jsonPara.titleId).parents(".gm-style-iw").parent().find(".gm-ui-hover-effect").click(); //for closing info window
            }
            
            function neighbouringDetails_backgroundData(){
            	var data = {};
            	data["title"] = "";
            	data["description"] = "";
            	data["lat"] = "";
            	data["lng"] = "";
            	return data;
            }
            
            function neighbouringDetails_Result(){
            	var neighbouringDetails = new Array();
            		$.each( neighbouring_result, function( key, value ) {
            			neighbouringDetails.push(value);
            		});
            		return neighbouringDetails;
           	 }
            
            function addExistingMarkers(neighbouringDetails){
            	
            	$.each( neighbouringDetails, function( key, json ) {
            		  //alert( key + ": " + json );
            		  var position = {};
            		  position["lat"] = Number(json.lat);
            		  position["lng"] = Number(json.lng);
            		var marker = new google.maps.Marker({
                        position: position,
                        map: map,
              		  	title: json.title,
                        draggable: false,
                       // label: labels[labelIndex++ % labels.length]
                      });
              		
                	 var infowindow = new google.maps.InfoWindow({
                         content: getHtmlForm(json)
                       });
                      
                     //google.maps.event.addListener(marker, "rightclick", function (point) {delMarker(marker,position)});
              		 marker.addListener('click', function() {
    			          infowindow.open(map, marker);
    			      });
              		 
              		 marker.addListener('rightclick', function() {
    			          delMarker(marker,json)
    			      });
              		  
              		  
              		neighbouring_markers.push(marker);
              		neighbouring_result[json.lat+"_"+json.lng] = json;
            	});
            	
            	
            }
            
            
            // Sets the map on all markers_nearbyfarm in the array.
            function setMapOnAll(map) {
              for (var i = 0; i < neighbouring_markers.length; i++) {
            	  neighbouring_markers[i].setMap(map);
      		  
              }
            }
            
            function clearMarkers() {
                setMapOnAll(null);
              }
            
            // Shows any markers currently in the array.
            function showMarkers() {
              setMapOnAll(map);
            }
            
         // Deletes all markers in the array by removing references to them.
            function deleteMarkers() {
          	  var userOption = confirm("Confirm to delete all the markers");
      			if (userOption == true) {
      				clearMarkers();
      				neighbouring_markers = [];
      				neighbouring_result = {};
      				
      			} 
          	
            }
            
            /* neighbouringDetails end  */
            
          </script>
          <script src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap&libraries=geometry,drawing,places"></script>
          <script src="js/maplabel-compiled.js?k=2.16"></script>
        </body>