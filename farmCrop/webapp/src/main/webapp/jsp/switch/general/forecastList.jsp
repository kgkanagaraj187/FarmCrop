<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<META name="decorator" content="swithlayout">
<html>
<head>
<style>
/* Always set the map height explicitly to define the size of the div * element that contains the map. */
#map {
	height: 100%;
}/*  /* Optional: Makes the sample page fill the window.
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
} */
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<ul class="nav nav-pills">
    <li class="active"><a data-toggle="pill" href="#home"><s:text name = '%{getLocaleProperty("Weather Forecast")}'/></a></li>
    <li><a data-toggle="pill" href="#imgMapLayer" onclick = "initMap();"><s:text name = '%{getLocaleProperty("Cloud Map")}'/></a></li>
    <li><a data-toggle="pill" href="#imgMapLayer2" onclick = "initMap2();"><s:text name = '%{getLocaleProperty("Rain Map")}'/></a></li>
</ul>

 <div class="tab-content">
    <div id="home" class="tab-pane fade in active">

<s:form name="forecastForm" cssClass="fillform" method="post"
	enctype="multipart/form-data">
	<div class="appContentWrapper marginBottom">
		<div class="flexItem ">
			<label for="txt"><s:property
					value="%{getLocaleProperty('city')}" /><sup style="color: red;">*</sup></label>
			<div class="form-element ">
				<s:select list="{}" id="city" onchange="populateForecastData(this);"
					class="form-control input-sm citys select2" name="city"
					headerKey="" headerValue="%{getText('txt.select')}" />

			</div>
		</div>
		</br> <s:property value="%{getLocaleProperty('weather.updatedDate')}" /><span id="localUpdateDate" style="text-align: right;"></span></br>
		</br><s:property value="%{getLocaleProperty('weather.city')}" /><span id="cityName"></span>
		<table class="table table-bordered table-striped "
			style="margin-top: 2%">
			<thead>
				<tr>
					<td><s:property value="%{getLocaleProperty('weather.date')}" />
					</td>
					<td align="center"><s:property
							value="%{getLocaleProperty('weather.temp')}" />
				<%-- 		<table>

							<tr>
								<td align="center"><s:property
										value="%{getLocaleProperty('weather.tempMax')}" /></td>
								<td style="padding-left: 20px;" align="center"><s:property
										value="%{getLocaleProperty('weather.tempMin')}" /></td>
							</tr>

						</table> --%></td>
					<td><s:property
							value="%{getLocaleProperty('weather.humidity')}" />           </td>
					<td><s:property
							value="%{getLocaleProperty('weather.windSpeed')}" />         </td>
              			<%-- 	<td><s:property
							value="%{getLocaleProperty('weather.soilMoisture')}" />         </td> --%>
					<td><s:property
							value="%{getLocaleProperty('weather.rainfall')}" /></td>
							<td><s:property
							value="%{getLocaleProperty('weather.lowSoilMoisture')}" /></td>
							<td><s:property
							value="%{getLocaleProperty('weather.highSoilMoisture')}" /></td>
				</tr>

			</thead>
			<tbody id="forecastInfoTbl">

			</tbody>
		</table>
	</div>
</s:form>
</div>

<div id="imgMapLayer" class="tab-pane fade">
<s:form name="imgMapLayerForm" cssClass="fillform" method="post"
	enctype="multipart/form-data">
	<%-- <div class="form-element">	
	<button type="button" class="btn btn-success"
							id="loadImageOverlays" onclick="setOverlayToMap();">
							<font color="#FFFFFF"> <b><s:text name="%{getLocaleProperty('loadOverlay')}" /></b></font>
	</button>
	</div> --%>
	<div id="map"  class="smallmap map" style="height:500px"></div>
</s:form>
</div>

<div id="imgMapLayer2" class="tab-pane fade">
	<div id="map2"  class="smallmap map" style="height:500px"></div>
</div>
</div>
<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.28"></script>
<script>	

	var overlays = [];
	var overlayIndex = 0;
	var bounds;
	var srcImage;
	var cloudMapOverlayIdx = 0;
	var rainMapOverlayIdx = 1;

	ImgMapOverlay.prototype = new google.maps.OverlayView();

	$(document).ready(function() {
		onFilterData();
	});

	function initMap() {
		var map = getGoogleMapObject('map');
		bounds = getIndiaMapOverlayBounds();
		setMapElementBounds(bounds,map);
		// The photograph (PNG; Image file) to be shown in map, URL.
		setImagesourceFromLocalWS();
		setOverlayToCloudMap(map); //Else use setOverlayToInitMap(map); method to load map by button
	}/** @constructor */

	function initMap2() {
		var map = getGoogleMapObject('map2');
		bounds = getIndiaMapOverlayBounds();
		setMapElementBounds(bounds,map);
		// The photograph (PNG; Image file) to be shown in map, URL.
		setImagesourceFromLocalWS();
		setOverlayToRainMap(map); //Else use setOverlayToInitMap(map); method to load map by button
	}/** @constructor */

	function getGoogleMapObject(mapId) {
		var mapObj;
		mapObj = new google.maps.Map(document.getElementById(mapId), {
			zoom : 10,
			center : {
				lat : 23.63936,
				lng : 68.14712
			},
			zoomControl : false,
			scrollwheel: false,
		    navigationControl: false,
		    mapTypeControl: false,
		    scaleControl: false,
		    draggable: false,
			mapTypeId : 'satellite'
		});
		return mapObj;
	}
	
	function setMapElementBounds(bounds,map){
		 var ne = bounds.getNorthEast();
		 var sw = bounds.getSouthWest();

		 var k = 5.0;

		 var n = ne.lat() - k;
		 var e = ne.lng() - k;
		 var s = sw.lat() + k;
		 var w = sw.lng() + k;

		 var neNew = new google.maps.LatLng( n, e );
		 var swNew = new google.maps.LatLng( s, w );
		 var boundsNew = new google.maps.LatLngBounds( swNew, neNew );

		 console.log( "bo=" + bounds.toUrlValue() );
		 console.log( "bn=" + boundsNew.toUrlValue() );

		 map.fitBounds( boundsNew );
		 map.panToBounds( boundsNew );
		// map.setCenter(boundsNew.getCenter());
		 //alert("*Hit"+boundsNew.getCenter());
	}

	function getIndiaMapOverlayBounds() {
		var bounds;
		bounds = new google.maps.LatLngBounds(new google.maps.LatLng(10, 55),
				new google.maps.LatLng(30, 75));
		return bounds;
	}

	function setOverlayToInitMap(map) {
		overlays.push(new ImgMapOverlay(bounds, srcImage[overlayIndex], map));
	}

	function incrementOverlayIndex() {
		if ((srcImage.length - 1) > overlayIndex) {
			overlayIndex++;
		} else {
			overlayIndex = 0;
		}
	}

	function setOverlayToMap(map) {
		incrementOverlayIndex();
		overlays.push(new ImgMapOverlay(bounds, srcImage[overlayIndex], map));
	}

	function ImgMapOverlay(bounds, image, map) { // Initialize all properties. 			
		this.bounds_ = bounds;
		this.image_ = image;
		this.map_ = map;
		this.div_ = null;
		this.setMap(map);
	}

	ImgMapOverlay.prototype.onAdd = function() {
		var div = document.createElement('div');
		div.style.borderStyle = 'none';
		div.style.borderWidth = '0px';
		div.style.position = 'absolute';
		var img = document.createElement('img');
		img.src = this.image_;
		img.style.width = '100%';
		img.style.height = '100%';
		img.style.position = 'absolute';
		div.appendChild(img);
		this.div_ = div; // Add the element to the "overlayLayer" pane.
		var panes = this.getPanes();
		panes.overlayLayer.appendChild(div);
	};

	ImgMapOverlay.prototype.draw = function() {

		var overlayProjection = this.getProjection();

		var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_
				.getSouthWest());
		var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_
				.getNorthEast());

		var div = this.div_;
		div.style.left = sw.x + 'px';
		div.style.top = ne.y + 'px';
		div.style.width = (ne.x - sw.x) + 'px';
		div.style.height = (sw.y - ne.y) + 'px';
	};

	ImgMapOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
		this.div_ = null;
	};

	function onFilterData() {
		callAjaxMethod("forecast_populateCityList.action", "citys");
	}

	function callAjaxMethod(url, name) {
		var cat = $.ajax({
			url : url,
			async : false,
			type : 'post',
			success : function(result) {
				insertOptionsByClass(name, JSON.parse(result));
			}

		});

	}

	function insertOptionsByClass(ctrlName, jsonArr) {
		$.each(jsonArr, function(i, value) {
			$("." + ctrlName).append($('<option>', {
				value : value.id,
				text : value.name
			}));
		});
	}

	function populateForecastData(obj) {
		jQuery.post("forecast_populateForeCast.action", {
			selectedCity : obj.value
		}, function(result) {
			if (result != "" || result != null) {
				var jsonForecastData = jQuery.parseJSON(result);
				var tbodyRow = "";
				var sno = 0;
				jQuery('#forecastInfoTbl > tbody').html('');
				$.each(jsonForecastData, function(index, value) {
					$("#localUpdateDate").text(value.updatedDate);
					$("#cityName").text(value.city);
					if (value.date !== undefined) {
						tbodyRow += '<tr class="">';
						tbodyRow += '<td >' + value.date + '</td>';
						/* tbodyRow += '<td align="center"><table><tr><td >'
								+ value.temp + '</td>'; */
						tbodyRow += '<td style="padding-left:20px; ">'
								+ value.temp + '</td>';
						tbodyRow += '<td>' + value.humidity + '</td>';
						tbodyRow += '<td>' + value.windSpeed + '</td>';
						/* 	tbodyRow += '<td>' + value.highSoilMoist+'</td>'; */

						tbodyRow += '<td>' + value.rainFall + '</td>';
						tbodyRow += '<td>' + value.lowSoilMoisture + '</td>';
						tbodyRow += '<td>' + value.highSoilMoisture + '</td>';

					}
				});
				$('#forecastInfoTbl').html(tbodyRow);
			}
		});

	}

	function setImagesourceFromURL() {
		srcImage = [];
		srcImage[0] = 'http://maps.customweather.com/image?client=sourcetrace&client_password=9Tq2myF&image_width=1000&layers=base,rainfall_day_0,state_borders&=&bbox=65,10,85,30&FORMAT=image/png';
		srcImage[1] = 'http://maps.customweather.com/image?client=sourcetrace&client_password=9Tq2myF&image_width=1000&layers=base,wind_day_0,state_borders&=&bbox=65,10,85,30&FORMAT=image/png';
	}

	function setImagesourceFromLocalWS() {
		srcImage = [];
		var cloudImage = '';
		var rainImage = '';
		try {
			var url = window.location.href;
			var temp = url;
			for (var i = 0; i < 1; i++) {
				temp = temp.substring(0, temp.lastIndexOf('/'));
			}
			var href = temp;
			var mapImg = "weather_cloud.png";
			cloudImage = temp + '/img/' + mapImg;
			mapImg = "weather_rain.png";
			rainImage = temp + '/img/' + mapImg;
		} catch (err) {
			//alert("Error: "+err);
		}
		srcImage[0] = cloudImage;
		srcImage[1] = rainImage;
	}

	function setOverlayToCloudMap(map) {
		overlays.push(new ImgMapOverlay(bounds, srcImage[cloudMapOverlayIdx],
				map));
	}

	function setOverlayToRainMap(map) {
		overlays.push(new ImgMapOverlay(bounds, srcImage[rainMapOverlayIdx],
				map));
	}
</script>
</html>