// Geo Cledian API JS integration file.
// Created on 16SEP2019
// (c) STS

//https://agknow.geocledian.com/agknow/api/v3/parcels?key=911313c3-26a7-d95e-b42e-2c270548a5a6&crop=cotton&planting=2018-01-01&harvest=2020-12-31&geometry=POLYGON ((77.60078250962806 11.833125268792969,77.61108219224525 11.852614128401338,77.62069522935462 11.835813469987125,77.60078250962806 11.833125268792969))
var geoCledianKey = "911313c3-26a7-d95e-b42e-2c270548a5a6";
var geoCledianParcelReqURL = "https://agknow.geocledian.com/agknow/api/v3/parcels?key=";
var geoCledianNVDIPngReqURLpart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
var geoCledianNVDIPngReqURLpart2 = "/vitality?key=";
var geoCledianNVDIPngReqURLpart3 = "&statistics=true";

var geoCledianNVDIPngURLpart1 = "https://agknow.geocledian.com/agknow/api/v3";
var geoCledianNVDIPngURLpart2 = "?key=";

var geoCledianNVDIImgReqURLPart1 = "https://geocledian.com/agknow/parcels/";
var geoCledianNVDIImgReqURLPart2 = "/vitality/";
var isParcelRequested = false;

var geoCledianNDVICroppedURLPart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
var geoCledianNDVICroppedURLPart2 = "/status?key=";
var geoCledianNDVICroppedURLPart3 = "&sdate=";

var tableTagID = "cropPercentileTbody";  //Table Body Id tbody element Id.

var geoCledianPhenologyURLPart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
var geoCledianPhenologyURLPart2 = "/phenology?key=";
var geoCledianPhenologyURLPart3 = "&startdate=";
var geoCledianPhenologyURLPart4 = "&enddate=";
var maxPhenology = "";
var auc = "";  // Area Under Curve value (AUC)
var fractionUnderCrop = ""; // Fraction Under Crop (FUC)
var cpiValue = ""; // Crop Performance Index (CPI)
var ndviValArr = new Array();
const WET_SEASON = "WET SEASON";
const DRY_SEASON = "DRY SEASON";
var analysisProgressBarDivId = "analysisProgressBar";
var entityFieldId = "entityField";
const ERROR_DATE_NA = "Error Data N/A For Today's Date: ";
const HYPEN_VAL = "-";
const ZERO_FLOAT_STR = "0.00";
// Similarity Features Variables.
	var geoCledianSimilarityAnalysisReqURLPart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
	var geoCledianSimilarityAnalysisReqURLPartPart2 =  "/similarity";
	var geoCledianSimilarityAnalysisReqURLPartPart3 =  "?key=";
	var geoCledianSimilarityAnalysisReqURLPartPart4 = "&radius=";
	var geoCledianSimilarityAnalysisReqURLPartPart5 = "&startdate=";
	var geoCledianSimilarityAnalysisReqURLPartPart6 = "&enddate=";
	var geoCledianSimilarityAnalysisReqURLPartPart7 = "&crop=";
	var geoCledianSimilarityAnalysisReqURLPartPart8 = "&entity=";
	var geoCledianSimilarityAnalysisReqURLPartPart9 = "&interval=";
	var similarityRadius = 250000;
	var similarityInterval = 7;
	var geoCledianSimilarityAnalysisReqURLClassify = "&classification=true";
	var similarityValue = "similarityValue";
	const SIMILARITY_ERROR_RESULT = "Similarity analysis could not be done. Please try again.";
	
	const SIMILARITY_CORRELATION_TRESHOLD = 0.8;


function getParcelRequestForPlot(isRequested, planting, harvest, crop,
		geometry, map, elementId) {
	var geoCledianParcelReqURLForPlot = geoCledianParcelReqURL + geoCledianKey;
	if (!isRequested) {
		geoCledianParcelReqURLForPlot += "&crop=" + crop;
		geoCledianParcelReqURLForPlot += "&entity=" + crop;
		geoCledianParcelReqURLForPlot += "&planting=" + planting;
		geoCledianParcelReqURLForPlot += "&harvest=" + harvest;
		geoCledianParcelReqURLForPlot += "&geometry=" + geometry;
		console.log(geoCledianParcelReqURLForPlot);		
		sendAjaxReqForParcelTifOverlayMap(geoCledianParcelReqURLForPlot, map,
				elementId);
	}
}

function sendAjaxReqForParcel(parcelReqUrl) {
	var responseValue = null;
	var parcelId = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : parcelReqUrl,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			parcelId = getParcelId(responseValue);
			if (!isEmpty(parcelId)) {
				setTimeout(function() {
					getNVDIPngImgURL(parcelId);
					console.log("Geocledian: Img Src Value after 1 min");
				}, 60000);
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}

function getParcelId(responseValue) {
	var jsonResponse = JSON.parse(responseValue);
	var parcelId = jsonResponse.id;
	return parcelId;
}

function getNVDIPngImgURL(parcelId) {
	var nvdiPNGReqURL = getNVDIpngImgReqURL(parcelId);
	getNVDIPngSrcURL(nvdiPNGReqURL, parcelId);
}

function getNVDIPngSrcURL(nvdiPNGReqURL, parcelId) {
	var responseValue = null;
	var pngImgSrcURL = null;
	var fullPngImgSrcUrl = "";

	$
			.ajax({
				"async" : true,
				"crossDomain" : true,
				"url" : nvdiPNGReqURL,
				"method" : "GET",
				"headers" : {
					"Content-Type" : "application/json",
					"Accept" : "**",
					"Cache-Control" : "no-cache"
				},
				"processData" : false,
				success : function(responseData, textStatus, jqXHR) {
					console.log(responseData);
					responseValue = JSON.stringify(responseData);
					pngImgSrcURL = getPNGSrcFromResPkt(responseValue);
					if (!isEmpty(pngImgSrcURL)) {
						fullPngImgSrcUrl = getFullPngImgSrcUrl(pngImgSrcURL);
						if (!isEmpty(fullPngImgSrcUrl)) {
							enableNvdiPhotoModal("dialog", fullPngImgSrcUrl,
									parcelId, map);
							console.log("Geocledian: Png Img Src Value");
							console.log(fullPngImgSrcUrl);
						}
					}
				},
				error : function(responseData, textStatus, errorThrown) {
					alert('POST Request TO Geocledian API Parcel Request For PNG Src URL Error.');
					responseValue = JSON.stringify(responseData);
				}
			});
}

function getFullPngImgSrcUrl(pngImgSrcURL) {
	var fullPngImgUrl = geoCledianNVDIPngURLpart1;
	fullPngImgUrl += pngImgSrcURL;
	fullPngImgUrl += geoCledianNVDIPngURLpart2;
	fullPngImgUrl += geoCledianKey;
	return fullPngImgUrl;
}

function getOverlayTifImgSrcUrl(pngImgSrcURL) {
	var fullPngImgUrl = geoCledianNVDIImgReqURLPart1;
	fullPngImgUrl += pngImgSrcURL;
	fullPngImgUrl += geoCledianNVDIPngURLpart2;
	fullPngImgUrl += geoCledianKey;
	return fullPngImgUrl;
}

function getNVDIpngImgReqURL(parcelId) {
	var nvdiImagPngReqURL = geoCledianNVDIPngReqURLpart1;
	nvdiImagPngReqURL += parcelId;
	nvdiImagPngReqURL += geoCledianNVDIPngReqURLpart2;
	nvdiImagPngReqURL += geoCledianKey;
	nvdiImagPngReqURL += geoCledianNVDIPngReqURLpart3;
	console.log(nvdiImagPngReqURL);
	return nvdiImagPngReqURL;
}

function getPNGSrcFromResPkt(responseValue) {
	var jsonResponse = JSON.parse(responseValue);
	var pngUrlRes = jsonResponse.content;
	var contentURLVar = null;
	var pngURLSrc = "";
	if (!isEmpty(pngUrlRes) && pngUrlRes.length >= 1) {
		contentURLVar = pngUrlRes[0];
		if (!isEmpty(contentURLVar)) {
			pngURLSrc = contentURLVar.png;
		}
	}
	return pngURLSrc;
}

function isEmpty(val) {
	if (val == null || val == undefined || val.toString().trim() == "") {
		return true;
	}
	return false;
}

function getGeoCledianStarDate(datePickerId) {
	var d1 = jQuery('#' + datePickerId).val();
	var d2 = d1.split(HYPEN_VAL);
	var startDate = null;
	var startDateStr = "";
	if (d2.length > 1) {
		var value1 = d2[0];
		var value2 = d2[1];
	}
	startDate = new Date(Date.parse(value1));
	startDateStr += startDate.getFullYear() + HYPEN_VAL;
	startDateStr += (startDate.getMonth() + 1) + HYPEN_VAL;
	startDateStr += startDate.getDate();
	return startDateStr;
}

function getGeoCledianEndDate(datePickerId) {
	var d1 = jQuery('#' + datePickerId).val();
	var d2 = d1.split(HYPEN_VAL);
	var endDate = null;
	var endDateStr = "";
	if (d2.length > 1) {
		var value1 = d2[0];
		var value2 = d2[1];
	}
	endDate = new Date(Date.parse(value2));
	endDateStr += endDate.getFullYear() + HYPEN_VAL;
	endDateStr += (endDate.getMonth() + 1) + HYPEN_VAL;
	endDateStr += endDate.getDate();
	return endDateStr;
}

function polygonParamVal(coorArr) {
	var coordinateVals = "";
	var ploygonVal1 = "POLYGON ((";
	var polygonVal2 = "))";

	$(coorArr).each(function(k, v) {
		var latLon = v;

		if (!isEmpty(latLon[0]) && !isEmpty(latLon[1])) {
			coordinateVals += latLon[0] + " " + latLon[1] + ",";
		}

	});
	console.log(coordianteVals);
	coordinateVals = coordinateVals.substring(0, coordinateVals
			.lastIndexOf(','));
	coordinateVals = ploygonVal1 + coordinateVals;
	coordinateVals = coordinateVals + polygonVal2;
	return coordinateVals;
}

function enableNvdiPhotoModal(modalId, imgSrcUrl, parcelId) {
	var mbody = "";
	var geoCledianParcelTitle = "Geocledian Parcel ID:";
	var opt = {
		autoOpen : false,
		modal : true,
		width : 400,
		height : 300
	};

	$("#" + modalId).empty();
	mbody = "";
	mbody = "<div class='item active'>";
	mbody += '<img src="' + imgSrcUrl + '" alt = "' + imgSrcUrl
			+ '" class="transparent"/>';
	mbody += '<label>' + geoCledianParcelTitle + '</label>';
	mbody += '<label>' + parcelId + '</label>';
	mbody += "</div>";
	$("#" + modalId).append(mbody);
	$("#" + modalId).dialog(opt).dialog("open");
}

function overlayGeocledianParcelOnGoogleMap(parcelId, map) {
	var raster, imgURL, imOv;
	var geoCledianAgkUrl = "https://geocledian.com/agknow/api/v3";
	var tifFormat = "tiff";
	var pngFormat = "png";
	var imgParcelReqUrlPart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
	var imgParcelReqUrlPart2 = "/vitality";
	var imgParcelReqUrlTifFull = imgParcelReqUrlPart1 + parcelId
			+ imgParcelReqUrlPart2;

	$.getJSON(imgParcelReqUrlTifFull, "key=" + geoCledianKey, function(data) {
		raster = data["content"][0];
		imgURL = geoCledianAgkUrl + raster[tifFormat];
		bounds = raster["bounds"]

		var imageMapType = new google.maps.ImageMapType({
			getTileUrl : imgURL,
			tileSize : bounds
		});

		map.overlayMapTypes.push(imageMapType);
	});
}

function sendAjaxReqForParcelTifOverlayMap(parcelReqUrl, map, elementId) { // Returns
																			// Parcel
																			// ID
																			// from
																			// Geocledian
																			// Server
																			// is
																			// request
																			// got
																			// success.
	var responseValue = null;
	var parcelId = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : parcelReqUrl,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			parcelId = getParcelId(responseValue);
			if (!isEmpty(parcelId)) {
				showGCParcelIdField();
				console.log("Geocledian: Parcel ID Generated :" + parcelId);
				setParcelIdForElement(elementId, parcelId);
			} else {
				enableFarmerPlottingPageButtons();
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}

function testTifImageOverlayOnMap(map) {
	var raster, imgUrl, imOv;
	$
			.getJSON(
					"https://geocledian.com/agknow/parcels/3243/vitality/12738",
					"key=39553fb7-7f6f-4945-9b84-a4c8745bdbec", function(data) {
						raster = data["content"][0];
						imgUrl = "https://geocledian.com/agknow/api/v3"
								+ raster["tif"];
						bounds = raster["bounds"]

						var imageMapType = new google.maps.ImageMapType({
							getTileUrl : imgUrl,
							tileSize : bounds
						});

						map.overlayMapTypes.push(imageMapType);
					});
}

function setNewShape(newShape) {
	var initCoordinate; // Starting Point Coordinate, Initial coordinate.
	var interCoordinate; // Inbetween Coordinate Points.
	var coorArr = new Array();

	initCoordinate = valueOnsetForInitCoordinateExchange(newShape);

	for (var i = 0; i < newShape.getPath().getLength(); i++) {
		interCoordinate = valueOnsetForCoordinateExchange(newShape, i);

		coorArr.push(interCoordinate);
	}
	coorArr.push(initCoordinate);
	return coorArr;
}

function valueOnsetForInitCoordinateExchange(newShape) {
	var initCoordinate;
	var interCoordinate = "";
	var coordinateTransfer = new Array();
	var tmpCoordinate = "";
	initCoordinate = newShape.getPath().getAt(0).toUrlValue(20);
	coordinateTransfer = initCoordinate.split(",");
	if (coordinateTransfer.length >= 2) {
		tmpCoordinate = parseFloat(coordinateTransfer[0]);
		coordinateTransfer[0] = parseFloat(coordinateTransfer[1]);
		coordinateTransfer[1] = tmpCoordinate;
	}
	return coordinateTransfer;
}

function valueOnsetForCoordinateExchange(newShape, i) {
	var initCoordinate;
	var interCoordinate = "";
	var coordinateTransfer = new Array();
	var tmpCoordinate = "";
	initCoordinate = newShape.getPath().getAt(i).toUrlValue(20);
	coordinateTransfer = initCoordinate.split(",");
	if (coordinateTransfer.length >= 2) {
		tmpCoordinate = parseFloat(coordinateTransfer[0]);
		coordinateTransfer[0] = parseFloat(coordinateTransfer[1]);
		coordinateTransfer[1] = tmpCoordinate;
	}
	return coordinateTransfer;
}

function getGeoCledianParcelRequest(isRequested, planting, harvest, crop,
		geometry, map, elementId) {
	var geoCledianParcelReqURLForPlot = geoCledianParcelReqURL + geoCledianKey;
	if (!isRequested) {
		geoCledianParcelReqURLForPlot += "&crop=" + crop;
		geoCledianParcelReqURLForPlot += "&entity=" + crop;
		geoCledianParcelReqURLForPlot += "&planting=" + planting;
		geoCledianParcelReqURLForPlot += "&harvest=" + harvest;
		geoCledianParcelReqURLForPlot += "&geometry=" + geometry;
		console.log(geoCledianParcelReqURLForPlot);
		sendAjaxReqForParcelTifOverlayMap(geoCledianParcelReqURLForPlot, map,
				elementId);
	} else {
		enableFarmerPlottingPageButtons();
		hideGCParcelIdField();
		showPlotLimitAlert();
	}
}

function getGeocledianApiParcelId(crop, coorArr, map, elementId) {
	var planting = "";
	var harvest = "";
	var geometry = "";
	if (isEmpty(crop)) {
		crop = getDefaultCrop();
	}
	planting = getGeoCledianStarDate("dateRangeValue");
	harvest = getGeoCledianEndDate("dateRangeValue");
	geometry = polygonParamVal(coorArr);
	getGeoCledianParcelRequest(isParcelRequested, planting, harvest, crop,
			geometry, map, elementId);
	isParcelRequested = true;
}

function getDefaultCrop() {
	return "cotton";
}

function setParcelIdForElement(elementId, parcelId) {
	var textElement = $("#" + elementId);
	textElement.val(parcelId);
	enableFarmerPlottingPageButtons();
}

function enableFarmerPlottingPageButtons() {
	$("#savePlotBtn").attr("disabled", false);
	$("#gcReqBtn").attr("disabled", false);
}

function geoCledianTiffImgRequest(parcelId) {
	var raster, imgURL, imOv;
	var geoCledianAgkUrl = "https://geocledian.com/agknow/api/v3";
	var tifFormat = "tiff";
	var pngFormat = "png";
	var imgParcelReqUrlPart1 = "https://agknow.geocledian.com/agknow/api/v3/parcels/";
	var imgParcelReqUrlPart2 = "/vitality";
	var imgParcelReqUrlTifFull = imgParcelReqUrlPart1 + parcelId
			+ imgParcelReqUrlPart2;

	$.getJSON(imgParcelReqUrlTifFull, "key=" + geoCledianKey, function(data) {
		console.log("Tiff Image For Plot Generated");
	});
}

// Misc Codes
function hideGCParcelIdField() {
	clearGeoCleParId();
	hideGCParcelFetchBtnFast();
	$("#geoParcelIdField").hide('slow');
}

function clearGeoCleParId() {
	$("#geoCleParId").val("");
}

function hideGCParcelFetchBtnFast() {
	$('#geoCledianParcelFetchBtn').hide();
}

function showPlotLimitAlert() {
	alert("Geocledian Limit For Parcel Done. Please Reset page to send again...")
}

function showGCParcelIdField() {
	$("#geoParcelIdField").show('slow');
}

// ** Geo Cledian Value Added **//

function loadFarmMapForGC(farmFieldId, cropFieldId) {

	var dataArr = new Array();
	var landarry = new Array();
	var gcCoordPatternArry = new Array();
	var gcCoordPatternStr = "";
	var initialGCCoord = "";
	var crop = "";
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(null);
	}
	var markers = new array();

	plotCOunt = 0;
	var farmIdd = $("#" + farmFieldId).val();

	if (farmIdd != '' && farmIdd != null) {
		var farmId = new Array();
		farmId = farmIdd.split("~");
		if (farmId.length = 3 && farmId[1] != '' && farmId[2] != ''
				&& farmId[1] != undefined && farmId[2] != undefined) {

			dataArr.push({
				latitude : parseFloat(farmIdd.split("~")[1]),
				longitude : parseFloat(farmIdd.split("~")[2])

			});
			var data = {

				selectedFarm : farmId[0].trim(),
				selectedFarmType : "1"
			}
			crop = getFarmCropNameFromField(cropFieldId);
			$.ajax({
				url : 'ndvi_populateFarmCoordinates.action',
				type : 'post',
				dataType : 'json',
				async : false,
				data : data,
				success : function(data, result) {
					landarry = result;
					gcCoordPatternStr = getGeoClPolygonStr(landarry);
					getGeocledianApiParcelIdForFarm(crop, gcCoordPatternStr,
							farmFieldId, cropFieldId);
				}
			});
		}
	}
}

function getGeoClPolygonStr(landarry) {
	var gcCoordPatternArry = new Array();
	var gcCoordPatternStr = "";
	var initialGCCoord = "";
	var initialCoordStr = "";
	var ploygonVal1 = "POLYGON ((";
	var polygonVal2 = "))";
	if (!isEmpty(landarry)) {
		gcCoordPatternArry = landarry.coord;
		if (!isEmpty(gcCoordPatternArry) && gcCoordPatternArry.length > 0) {
			initialGCCoord = gcCoordPatternArry[0];
			initialCoordStr = initialGCCoord.lat + " " + initialGCCoord.lon
					+ ",";
			if (gcCoordPatternArry.length >= 1) {
				for (var k = 1; k < gcCoordPatternArry.length; k++) {
					gcCoordPatternStr += gcCoordPatternArry[k].lat + " "
							+ gcCoordPatternArry[k].lon + ",";
				}
			}
		}
	}
	gcCoordPatternStr = initialCoordStr + (gcCoordPatternStr) + initialCoordStr;
	console.log(gcCoordPatternStr);
	gcCoordPatternStr = gcCoordPatternStr.substring(0, gcCoordPatternStr
			.lastIndexOf(','));
	gcCoordPatternStr = ploygonVal1 + gcCoordPatternStr + polygonVal2;
	return gcCoordPatternStr;
}

function getGeocledianApiParcelIdForFarm(crop, geometry, farmPlotFieldId,
		cropFieldId) {
	var planting = "";
	var harvest = "";
	if (isEmpty(crop)) {
		crop = getDefaultCrop();
	}
	planting = getGeoCledianStarDate("dateRangeValue");
	harvest = getGeoCledianEndDate("dateRangeValue");
	geometry = setGeometryParameterValue(geometry);
	getGeoCledianParcelRequestForFarmPlotCoords(planting, harvest, crop,
			geometry, farmPlotFieldId);
}

function setGeometryParameterValue(geometry) { // 1sgmp
	var polygonVal1 = "POLYGON ((";
	var polygonVal2 = "))";
	var coordVal = new Array();
	var coordArray = new Array();
	var geoParamVal = "";
	for (var k = 0; k < geometry.length; k++) {
		coordVal = geometry[k];
		coordArray = coordVal;
		geoParamVal += coordArray[0];
		geoParamVal += " ";
		geoParamVal += coordArray[1];
		geoParamVal += ",";
	}
	geoParamVal = geoParamVal.substring(0, geoParamVal.lastIndexOf(','));
	geoParamVal = polygonVal1 + geoParamVal + polygonVal2;
	console.log(geoParamVal);
	return geoParamVal;
}

function getGeoCledianParcelRequestForFarmPlotCoords(planting, harvest, crop,
		geometry, farmPlotFieldId) {
	var geoCledianParcelReqURLForPlot = geoCledianParcelReqURL + geoCledianKey;
	geoCledianParcelReqURLForPlot += "&crop=" + crop;
	geoCledianParcelReqURLForPlot += "&entity=" + crop;
	geoCledianParcelReqURLForPlot += "&planting=" + planting;
	geoCledianParcelReqURLForPlot += "&harvest=" + harvest;
	geoCledianParcelReqURLForPlot += "&geometry=" + geometry;
	console.log("GeoCledian Parcel ID Request URL:");
	console.log(geoCledianParcelReqURLForPlot);
	disableInputsForGC();
	sendAjaxReqForParcelTifOverlayMapForFarmObj(geoCledianParcelReqURLForPlot,
			farmPlotFieldId);
}

function getFarmCropNameFromField(fieldId) { // Field ID in farmPlotting Page
												// = farmCrop.
	var farmCropName = $("#" + fieldId + " :selected").text();
	if (farmCropName == "Select") {
		farmCropName = "";
	}
	return farmCropName;
}

function sendAjaxReqForParcelTifOverlayMapForFarmObj(parcelReqUrl, farmFieldId) { // Returns
																					// Parcel
																					// ID
																					// from
																					// Geocledian
																					// Server
																					// is
																					// request
																					// got
																					// success.
	var responseValue = null;
	var parcelId = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : parcelReqUrl,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			parcelId = getParcelId(responseValue);
			if (!isEmpty(parcelId)) {
				console.log("Geocledian: Parcel ID Generated :" + parcelId);
				// checkFarmHasGCParcelId(farmPlotFieldId);
				setParcelIdForFarm("farmForGcParcel", parcelId);
			} else {
				console.log("Error: Couldn't Generate parcel ID:");
				enableElementById('registerParcelIdFarm');
				hideAnalysisProgressbarDiv();
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}

// ** New Methods to be added in Action Class **//
function checkFarmHasGCParcelId(farmPlotFieldId) {

	var hasParcelId = "";
	var gcCoordPatternStr = "";

	var farmIdd = $("#" + farmPlotFieldId).val();

	var data = {

		selectedFarm : farmIdd
	}
	$.ajax({
		url : 'ndvi_populateCheckFarmHasGcParcelId.action',
		type : 'post',
		dataType : 'json',
		async : false,
		data : data,
		success : function(data, result) {
			hasParcelId = data;
			if (hasParcelId === "false") {
				saveFarmWithGCParcelId(farmPlotFieldId, parcelId);
			}
		}
	});
}

function saveFarmWithGCParcelId(farmPlotFieldId, parcelId) {

	var hasParcelId = "";
	var gcCoordPatternStr = "";

	var farmIdd = $("#" + farmPlotFieldId).val();

	var data = {

		selectedFarm : farmIdd,
		parcelId : parcelId
	}
	$.ajax({
		url : 'farmerPlotting_populateFarmWithParcelId.action',
		type : 'post',
		dataType : 'json',
		async : false,
		data : data,
		success : function(data, result) {
			console.log("Plot Id Saved Successfully");
		}
	});
}

function getGeocledianApiParcelIdForFarmFromAction(crop, geometry,
		dateRangeField) {
	var planting = "";
	var harvest = "";
	if (isEmpty(crop)) {
		crop = getDefaultCrop();
	}
	planting = getGeoCledianStarDate(dateRangeField);
	harvest = getGeoCledianEndDate(dateRangeField);
	getGeoCledianParcelRequestForPlot(planting, harvest, crop, geometry);
}

function getGeoCledianParcelRequestForPlot(planting, harvest, crop, geometry) {
	var geoCledianParcelReqURLForPlot = geoCledianParcelReqURL + geoCledianKey;

	geoCledianParcelReqURLForPlot += "&crop=" + crop;
	geoCledianParcelReqURLForPlot += "&entity=" + crop;
	geoCledianParcelReqURLForPlot += "&planting=" + planting;
	geoCledianParcelReqURLForPlot += "&harvest=" + harvest;
	geoCledianParcelReqURLForPlot += "&geometry=" + geometry;
	console.log(geoCledianParcelReqURLForPlot);
	// sendAjaxReqForParcelTifOverlayMapOfPlot(geoCledianParcelReqURLForPlot);
}

function sendAjaxReqForParcelTifOverlayMapOfPlot(parcelReqUrl) { // Returns
																	// Parcel ID
																	// from
																	// Geocledian
																	// Server is
																	// request
																	// got
																	// success.
	var responseValue = null;
	var parcelId = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : parcelReqUrl,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			parcelId = getParcelId(responseValue);
			if (!isEmpty(parcelId)) {
				showGCParcelIdField();
				console.log("Geocledian: Parcel ID Generated New:" + parcelId);
				setParcelIdForElement(elementId, parcelId);
			} else {
				enableFarmerPlottingPageButtons();
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}

function loadFarmGCParcelId(farmIdd) {

	var dataArr = new Array();
	var landarry = new Array();
	var gcCoordPatternArry = new Array();
	var gcCoordPatternStr = "";
	var initialGCCoord = "";
	var markers = [];
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(null);
	}

	if (farmIdd != '' && farmIdd != null) {
		var farmId = new Array();
		farmId = farmIdd.split("~");
		if (farmId.length = 3 && farmId[1] != '' && farmId[2] != ''
				&& farmId[1] != undefined && farmId[2] != undefined) {

			dataArr.push({
				latitude : parseFloat(farmIdd.split("~")[1]),
				longitude : parseFloat(farmIdd.split("~")[2])

			});
			var data = {

				selectedFarm : farmId[0].trim(),
				selectedFarmType : "1"
			}
			$.ajax({
				url : 'ndvi_populateFarmCoordinates.action',
				type : 'post',
				dataType : 'json',
				async : false,
				data : data,
				success : function(data, result) {
					landarry = data;
					// gcCoordPatternStr = getGeoClPolygonStr(landarry);
					// getGeocledianApiParcelIdForFarm(crop,gcCoordPatternStr,farmFieldId,cropFieldId);
					// getGeocledianApiParcelIdForFarmFromAction(crop,gcCoordPatternStr,"dateRangeValue");

				}
			});
		}
	}
}

function testMessage() {
	alert("*Hit: Test Message");
}

function getCoordPatternStruct(landArr, map, farmFieldId, farmCropFieldId) {
	var landArea = landArr.coord;
	var coorArr = new Array();
	var cords = new Array();
	var bounds = new google.maps.LatLngBounds();
	console.log(coorArr);
	console.log(landArea);
	$(landArea).each(
			function(k, v) {
				cords.push({
					lat : parseFloat(v.lat),
					lng : parseFloat(v.lon)
				});
				var coordinatesLatLng = new google.maps.LatLng(
						parseFloat(v.lat), parseFloat(v.lon));
				coorArr.push(v.lat + "," + v.lon);
				bounds.extend(coordinatesLatLng);

			});
	plotting = new google.maps.Polygon({
		paths : cords,
		strokeColor : '#50ff50',
		strokeOpacity : 0.8,
		strokeWeight : 2,
		fillColor : '#50ff50',
		fillOpacity : 0.45,
		editable : true,
		draggable : false
	});
	setShapeForGCRequest(plotting, coorArr, map, farmFieldId, farmCropFieldId);
}

function getGCParcel(geometry, map, farmFieldId, farmCropFieldId) {
	getCoordPatternStruct(geometry, map, farmFieldId, farmCropFieldId);
	// getGeocledianParcelId(geometry);
}

function setShapeForGCRequest(newShape, coorArr, map, farmFieldId,
		farmCropFieldId) {
	farmCoorArr = coorArr;
	coorArr = setNewShape(newShape);
	// showGCParcelFetchBtn();
	getGeocledianParcelId(coorArr, farmFieldId, farmCropFieldId);
}

function getGeocledianParcelId(coorArr, map, farmFieldId, farmCropFieldId) {
	var farmCrop = $("#farmCropsGCParcel :selected").text();
	if (farmCrop == "Select") {
		farmCrop = "";
	}
	$("#gcReqBtn").attr("disabled", true);
	$("#savePlotBtn").attr("disabled", true);
	getGeocledianApiParcelIdForFarm(farmCrop, coorArr, farmFieldId,
			farmCropFieldId);
}

function setParcelIdForFarm(farmFieldId, parcelId) {

	var hasParcelId = "";
	var gcCoordPatternStr = "";

	var farmIdd = $("#" + farmFieldId).val();
	var farmIdVal = new Array();
	farmIdVal = farmIdd.split("~");
	var selectedFarmId = farmIdVal[0];
	console.log(farmIdVal);
	var data = {

		selectedFarm : selectedFarmId,
		gcParcelId : parcelId
	}
	$.ajax({
		url : 'ndvi_populateSetFarmGcParcelId.action',
		type : 'post',
		dataType : 'json',
		async : false,
		data : data,
		success : function(data, result) {
			setFarmGCParcelID(parcelId);
			alert("Note: Saved Parcel Id Successfully For Farm...");
		}
	});
	reloadPage();
}

function setFarmGCParcelID(val) {

	var kmapElement = $("#kmapId1");
	if (!isEmpty(val)) {
		kmap1.destroy();
		kmapElement.removeAttr("parcelId");
		// kmapElement.removeAttr( "autoplay" );
		kmapElement.attr("parcelId", val);
		// kmapElement.attr("autoplay","false");
		kmap1 = new KMaps({
			mapControlId : 'kmapId1',
			style : {
				border : 'gray solid 3px'
			},
			timeline : {},
			provider : 'GoogleMaps.Hybrid'
		});
		console.debug(kmap1);
	}
	showGcMap();
	enableElementById('registerParcelIdFarm');
}

/**
 * New Set Of Codes.
 */

function loadFarmCropMapForGC(cropFieldId) {

	var dataArr = new Array();
	var landarry = new Array();
	var gcCoordPatternArry = new Array();
	var gcCoordPatternStr = "";
	var initialGCCoord = "";
	var crop = "";
	var markers = [];
	var farmCropParcelId = "";
	var isParcelNotRegisteredFarmCrop = false; // Flag for registered parcels.
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(null);
	}

	plotCOunt = 0;
	var cropIdd = $("#" + cropFieldId).val();
	if (cropIdd != '' && cropIdd != null) {
		var farmId = new Array();
		farmCropId = cropIdd.split("~");
		if (farmCropId.length === 1) {
			isParcelNotRegisteredFarmCrop = true;
		} else if (farmCropId.length > 1) {
			farmCropParcelId = farmCropId[1].trim();
			setFarmCropGCParcelID(farmCropParcelId);
			if (farmCropId[1].trim() == "") {
				isParcelNotRegisteredFarmCrop = true;
			}
			clearTableContent(tableTagID);
			if(!isParcelNotRegisteredFarmCrop){
			getParcelStartDateEndDate(farmCropParcelId);
			getGeocledianSimilarityValueForParcel(farmCropParcelId,getFarmCropNameFromField(cropFieldId));
			}
		}
		if (isParcelNotRegisteredFarmCrop) {
			disableElementById('registerParcelIdFarmCrop');
			var data = {
				farmCropId : farmCropId[0].trim(),
				selectedFarmType : "1"
			}
			crop = getFarmCropNameFromField(cropFieldId);
			$.ajax({
				url : 'ndvi_populateFarmCropCoordinates.action',
				type : 'post',
				dataType : 'json',
				async : false,
				data : data,
				success : function(data, result) {
					landarry = data;
					gcCoordPatternStr = getCoordPatternStructObj(landarry); // getGeoClPolygonStr(landarry);
					getGeocledianApiParcelIdForFarmCrop(crop,
							gcCoordPatternStr, cropFieldId);
				}
			});

		}
		//Below Code to get Farm With Crop Parcel ID Chart.
		if(!isEmpty(farmCropParcelId)){
		setChartCropsGCParcelID(farmCropParcelId);
		}
	}
}

function getGeocledianApiParcelIdForFarmCrop(crop, geometry, cropFieldId) {
	var planting = "";
	var harvest = "";
	if (isEmpty(crop)) {
		crop = getDefaultCrop();
	}
	planting = getGeoCledianStarDate("dateRangeValue");
	harvest = getGeoCledianEndDate("dateRangeValue");
	geometry = setGeometryParameterValue(geometry);
	getGeoCledianParcelRequestForFarmCropPlotCoords(planting, harvest, crop,
			geometry);
}

function getGeoCledianParcelRequestForFarmCropPlotCoords(planting, harvest,
		crop, geometry) {
	var geoCledianParcelReqURLForPlot = geoCledianParcelReqURL + geoCledianKey;
	geoCledianParcelReqURLForPlot += "&crop=" + crop;
	geoCledianParcelReqURLForPlot += "&entity=" + crop;
	geoCledianParcelReqURLForPlot += "&planting=" + planting;
	geoCledianParcelReqURLForPlot += "&harvest=" + harvest;
	geoCledianParcelReqURLForPlot += "&geometry=" + geometry;
	console.log("GeoCledian Parcel ID Request URL:");
	console.log(geoCledianParcelReqURLForPlot);
	disableInputsForGC();
	sendAjaxReqForParcelTifOverlayMapForFarmCropObj(geoCledianParcelReqURLForPlot);
}

function sendAjaxReqForParcelTifOverlayMapForFarmCropObj(parcelReqUrl) { // Returns
																			// Parcel
																			// ID
																			// from
																			// Geocledian
																			// Server
																			// is
																			// request
																			// got
																			// success
																			// For
																			// Farm
																			// Crop.
	var responseValue = null;
	var parcelId = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : parcelReqUrl,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			parcelId = getParcelId(responseValue);
			if (!isEmpty(parcelId)) {
				console.log("Geocledian: Parcel ID Generated :" + parcelId);
				// checkFarmHasGCParcelId(farmPlotFieldId);
				setParcelIdForFarmCrop("farmCropsGCParcel", parcelId);
			} else {
				console.log("Error: Couldn't Generate parcel ID:");
				enableElementById('registerParcelIdFarmCrop');
				hideAnalysisProgressbarDiv();
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}

function setParcelIdForFarmCrop(farmCropFieldId, parcelId) {

	var hasParcelId = "";
	var gcCoordPatternStr = "";

	var farmIdd = $("#" + farmCropFieldId).val();
	var farmIdVal = new Array();
	farmIdVal = farmIdd.split("~");
	var selectedFarmCropId = farmIdVal[0];
	console.log(farmIdVal);
	var data = {
		farmCropId : selectedFarmCropId,
		gcParcelId : parcelId
	}
	$.ajax({
		url : 'ndvi_populateSetFarmCropGcParcelId.action',
		type : 'post',
		dataType : 'json',
		async : false,
		data : data,
		success : function(data, result) {
			setFarmCropGCParcelID(parcelId);
			alert("*Note Saved Parcel Id Successfully...");
		}
	});
	reloadPage();
}

function setFarmCropGCParcelID(val) {

	var kmapElement = $("#kmapId1");
	if (!isEmpty(val)) {
		kmap1.destroy();
		kmapElement.removeAttr("parcelId");
		// kmapElement.removeAttr( "autoplay" );
		kmapElement.attr("parcelId", val);
		// kmapElement.attr("autoplay","false");
		kmap1 = new KMaps({
			mapControlId : 'kmapId1',
			style : {
				border : 'gray solid 3px'
			},
			timeline : {},
			provider : 'GoogleMaps.Hybrid'
		});
		console.debug(kmap1);
	}
	showGcMap();
	enableElementById('registerParcelIdFarmCrop');
}

function getCoordPatternStructObj(landArr) {
	var landArea = landArr.coord;
	var coorArr = new Array();
	var cords = new Array();
	var retVal;
	var bounds = new google.maps.LatLngBounds();
	console.log(coorArr);
	console.log(landArea);
	$(landArea).each(
			function(k, v) {
				cords.push({
					lat : parseFloat(v.lat),
					lng : parseFloat(v.lon)
				});
				var coordinatesLatLng = new google.maps.LatLng(
						parseFloat(v.lat), parseFloat(v.lon));
				coorArr.push(v.lat + "," + v.lon);
				bounds.extend(coordinatesLatLng);

			});
	plotting = new google.maps.Polygon({
		paths : cords,
		strokeColor : '#50ff50',
		strokeOpacity : 0.8,
		strokeWeight : 2,
		fillColor : '#50ff50',
		fillOpacity : 0.45,
		editable : true,
		draggable : false
	});
	retVal = setShapeForGCRequestPolygon(plotting, coorArr);
	return retVal;
}

function setShapeForGCRequestPolygon(newShape, coorArr) {
	var coorArr = setNewShape(newShape);
	return coorArr;
}

function disableElementById(elementId) {
	var elementObj = $("#" + elementId);
	elementObj.attr("disabled", true);
}

function enableElementById(elementId) {
	var elementObj = $("#" + elementId);
	elementObj.attr("disabled", false);
}

function showElementById(elementId) {
	var elementObj = $("#" + elementId);
	elementObj.show();
}

function hideElementById(elementId) {
	var elementObj = $("#" + elementId);
	elementObj.hide();
}

function reloadPage() {
	location.reload();
}

function disableInputsForGC() { // For Diable Dropdown values when parcel
								// registration or getting them
	disableElementById('farmerForGcParcel');
	disableElementById('farmForGcParcel');
	disableElementById('farmCropsGCParcel');

}

// Function to set Date Foramtting Value to be returned
function setDateForamtting() {
	Date.prototype.yyyymmdd = function() {
		var mm = this.getMonth() + 1; // getMonth() is zero-based
		var dd = this.getDate();

		return [ this.getFullYear(), (mm > 9 ? '' : '0') + mm,
				(dd > 9 ? '' : '0') + dd ].join('');
	};
}

// Get Regular Date Format
function getRegularDateFormat(sDate) {
	var tDate = new Date(sDate);
	setDateForamtting();
	return tDate.yyyymmdd();
}

// URL String to request for Cropped Area
function getCroppedAreaURLForParcel(parcelId, sDateStr) {
	var urlStr = "";
	urlStr += geoCledianNDVICroppedURLPart1;
	urlStr += parcelId;
	urlStr += geoCledianNDVICroppedURLPart2;
	urlStr += geoCledianKey;
	urlStr += geoCledianNDVICroppedURLPart3;
	urlStr += sDateStr;	
	return urlStr;
}

// Function to send AJAX request to Geocledian Server for Cropped Area
function getCroppedAreaInfo(croppedAreaReqURL) {
	var responseValue = null;
	var sensingDate = "";
	var areaUnderCrops = 0;
	var totalArea = 0;
	var percentileOfAreaCropped = 0;
	var responseJsonData = null;
	var jsonStr = "";
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : croppedAreaReqURL,
		"method" : "GET",		
		"processData" : false,

		success : function(responseData, textStatus, jqXHR) {
			jsonStr = JSON.stringify(responseData);
			responseJsonData = JSON.parse(jsonStr);
			areaUnderCrops = responseJsonData.crop_status.area_under_crops;
			totalArea = responseJsonData.summary.area;
			sensingDate = responseJsonData.crop_status.sensing_date;
			percentileOfAreaCropped = getCroppedAreaPercentileInfo(totalArea, areaUnderCrops);			
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});

}

// Function to calculate Percential of Area Cropped From Total Area value.
function getCroppedAreaPercentileInfo(totalArea, areaUnderCrops) {
	var percentileOfAreaCropped = 0;	
	if (!isEmpty(totalArea) && !isEmpty(areaUnderCrops)) {
		percentileOfAreaCropped = ((areaUnderCrops / totalArea) * 100);
	}
	return percentileOfAreaCropped;
}

//Get next Sixty Days After start date
function getNextSixtySenseDate(sDate) {
	var sNextDate = getNextSixtyDayDate(sDate);
	setDateForamtting();
	return sNextDate.yyyymmdd();
}

//Function to get next date of a date Object
function getNextDayDate(todayDate) {
	const
	tommorrow = new Date(todayDate);
	tommorrow.setDate(tommorrow.getDate() + 1);
	return tommorrow;
}

//Function to get next Sixty date of a date Object
function getNextSixtyDayDate(todayDate) {
	const
	tommorrow = new Date(todayDate);
	tommorrow.setDate(tommorrow.getDate() + 60);
	return tommorrow;
}

//Get next start date
function getNextDaySenseDate(sDate) {
	var sNextDate = getNextDayDate(sDate);
	setDateForamtting();
	return sNextDate.yyyymmdd();
}

//Function to send AJAX request to Geocledian Server for Cropped Area over a Time Line
function getCroppedAreaInfoTimeline(parcelId, sDate, pStartDate, pEndDate,croppedAreaReqURL,previousSensedDate) {
	var responseValue = null;
	var sensingDate = "";
	var areaUnderCrops = 0;
	var totalArea = 0;
	var percentileOfAreaCropped = 0;
	var responseJsonData = null;
	var isEndDateReached = false;	
	var sDateStr = null;
	var jsonStr = "";
	var cropName = "";
	var seasonName = "";	
	
	try {		
		if(croppedAreaReqURL==null){
		sDateStr = getRegularDateFormat(sDate);
		croppedAreaReqURL = getCroppedAreaURLForParcel(parcelId, sDateStr);				
		}
		$.ajax({
			"async" : true,
			"crossDomain" : true,
			"url" : croppedAreaReqURL,
			"method" : "GET",
			"processData" : false,

			success : function(responseData, textStatus, jqXHR) {
				jsonStr = JSON.stringify(responseData);
				responseJsonData = JSON.parse(jsonStr);
				areaUnderCrops = responseJsonData.crop_status.area_under_crops;
				totalArea = responseJsonData.summary.area;
				sensingDate = responseJsonData.crop_status.sensing_date;
				cropName = responseJsonData.summary.crop;									
				fractionUnderCrop = getFractionUnderCrop(responseJsonData);
				// Next Line Formulae For CPI Value Calculation.
				//cpiValue = fractionUnderCrop+auc+maxPhenology; // CPI Value without Normalization.
				cpiValue = fractionUnderCrop+maxPhenology; // CPI Value with Normalization.
				percentileOfAreaCropped = getCroppedAreaPercentileInfo(
						totalArea, areaUnderCrops);
				filterBySeasonDate(previousSensedDate, sensingDate, tableTagID, percentileOfAreaCropped, cpiValue, seasonName, sDate, pStartDate, pEndDate,parcelId,fractionUnderCrop);
			},
			error : function(responseData, textStatus, errorThrown) {
				alert('POST Request TO Geocledian API Parcel Request Error.');
				responseValue = JSON.stringify(responseData);
			}
		});
	} catch (error) {
		console.log("Error:" + error);
	}

}

function getFractionUnderCrop(responseJsonData){
	var fractionUnderCrop="";
	if(!isEmpty(responseJsonData)){
	fractionUnderCrop = responseJsonData.crop_status.fraction_under_crops;
	}
	return fractionUnderCrop;
}

// Send Todays Sensing Date to get Start Date End Date.
function getParcelStartDateEndDate(parcelId) {
	var responseValue = null;
	var sensingDate = "";
	var areaUnderCrops = 0;
	var totalArea = 0;
	var percentileOfAreaCropped = 0;
	var responseJsonData = null;
	var pStartDate = null;
	var pEndDate = null;
	var croppedAreaReqURL = null;
	var sDateStr = getTodaysDate();
	var jsonStr = "";
	var phenologyURL = "";
	var errorJsonParsed = null;
	croppedAreaReqURL = getCroppedAreaURLForParcel(parcelId, sDateStr);
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : croppedAreaReqURL,
		"method" : "GET",		
		"processData" : false,

		success : function(responseData, textStatus, jqXHR) {
			try {
				jsonStr = JSON.stringify(responseData);
				responseJsonData = JSON.parse(jsonStr);
				pStartDate = getStartDateFromResponse(responseJsonData);
				pEndDate = getEndDateFromResponse(responseJsonData);
				phenologyURL = getCropAreaPhenologyURLForParcel(parcelId,
						pStartDate, pEndDate);
				if (!isEmpty(phenologyURL)) {
					getPhenologyInfo(phenologyURL, parcelId, pStartDate,
							pStartDate, pEndDate);
				}
			} catch (error) {
				jsonStr = JSON.stringify(responseData);
				errorJsonParsed = JSON.parse(jsonStr);
				console.log("Error: " + jsonStr);
				jsonStr = errorJsonParsed.Error;				
				hideAnalysisProgressbarDiv();
				appendRowValue(tableTagID, ERROR_DATE_NA + jsonStr, ZERO_FLOAT_STR, ZERO_FLOAT_STR, HYPEN_VAL);
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});

}

//Function to get Start Date from Summary Object
function getStartDateFromResponse(parsedJSONObj) {
	var startDate = "";	
	startDate = parsedJSONObj.summary.startdate;
	return startDate;
}


// Function to get End Date from Summary Object
function getEndDateFromResponse(parsedJSONObj) {
	var endDate = "";
	endDate = parsedJSONObj.summary.enddate;
	return endDate;
}


//Function to get todays Date
function getTodaysDate() {
	var tDate = new Date();
	setDateForamtting();
	return tDate.yyyymmdd();
}

//Function compare two Objects
function isSDateGtEndDate(sDate, endDate) {
	var sensingDate = new Date(sDate);
	var endDate = new Date(endDate);
	if (sensingDate >= endDate) {
		return true;
	} else {
		return false;
	}
}

//Function to get next date of a date Object
function getNextSixtyDaysDateAsString(todayDate) {	
	const
	tommorrow = new Date(todayDate);
	var dateStr = "";
	var dateVal = "";
	var monthVal = "";
	var fullYearVal = "";	
	tommorrow.setDate(tommorrow.getDate()+60);
	fullYearVal = tommorrow.getFullYear();
	dateVal = tommorrow.getDate();
	if(dateVal<=9){
		dateVal = "0"+dateVal;
	}
	monthVal = (tommorrow.getMonth()+1);
	if(monthVal<=9){
		monthVal = "0"+monthVal;
	}		
	dateStr = fullYearVal+HYPEN_VAL+monthVal+HYPEN_VAL+dateVal;
	return dateStr;
}

//Function to append Table For For Percentile:
function appendRowValue(tableTagID,sensedDate,percentileVal){	
	percentileVal = percentileVal.toFixed(2); 
	$("#"+tableTagID).append("<tr><td width='25%'>"+sensedDate+"</td><td width='25%'>"+percentileVal+"</td></tr>");
}

function clearTableContent(tableTagID) {	
	$("#" + tableTagID ).html("");	
}


//Function to append Table For For Percentile AND Fraction Under Crop AND CPI:
function appendRowValue(tableTagID,sensedDate,percentileVal,cpiValue,seasonName){	

		try {
		percentileVal = percentileVal.toFixed(2);
		cpiValue = cpiValue.toFixed(1);
		var color = "";
		if (cpiValue > 0 && cpiValue <= 0.5) {
			color = "RED";
		} else if (cpiValue > 0.5 && cpiValue < 1.2) {
			color = "ORANGE";
		} else if (cpiValue >= 1.2 && cpiValue <= 2.0) {
			color = "GREEN";
		}
	} catch (error) {
		console.log("Error with Append Row:" + error);
		color = "RED";
	}	
	$("#" + tableTagID).append(
			"<tr><td width='25%'>" + sensedDate + "</td><td width='25%'>"
					+ percentileVal + "</td><td width='25%' style='color:"
					+ color + "; font-weight:bold;'>" + cpiValue
					+ "</td><td width='15%'>" + seasonName + "</td></tr>");
}

//URL String to request for Crop Area Phenology
function getCropAreaPhenologyURLForParcel(parcelId, sDateStr,eDateStr) {
	var urlStr = "";
	urlStr += geoCledianPhenologyURLPart1;
	urlStr += parcelId;
	urlStr += geoCledianPhenologyURLPart2;
	urlStr += geoCledianKey;
	urlStr += geoCledianPhenologyURLPart3;
	urlStr += getRegularDateFormat(sDateStr);
	urlStr += geoCledianPhenologyURLPart4;
	urlStr += getRegularDateFormat(eDateStr);
	return urlStr;
}

//Function to get 'max'-Phenology Data from URL
function getPhenologyInfo(phenologyURL,parcelId, pStartDate, pStartDate,pEndDate) {
	var errorJsonParsed = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : phenologyURL,
		"method" : "GET",		
		"processData" : false,

		success : function(responseData, textStatus, jqXHR) {
			try {
				jsonStr = JSON.stringify(responseData);
				responseJsonData = JSON.parse(jsonStr);
				maxPhenology = getMaxPhenologyFromResponse(responseJsonData);
				auc = getAUCPhenologyFromResponse(responseJsonData);
				getCroppedAreaInfoTimeline(parcelId, pStartDate, pStartDate,
						pEndDate, null, null);
			} catch (error) {
				jsonStr = JSON.stringify(responseData);
				errorJsonParsed = JSON.parse(jsonStr);
				console.log("Error: " + jsonStr);
				jsonStr = errorJsonParsed.Error;				
				hideAnalysisProgressbarDiv();
				appendRowValue(tableTagID, ERROR_DATE_NA + jsonStr, ZERO_FLOAT_STR, ZERO_FLOAT_STR, HYPEN_VAL);
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Parcel Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});

}

//Function to get 'max'-Phenology Data from JSON
function getMaxPhenologyFromResponse(parsedJSONObj) {
	var max = "";
	max = parsedJSONObj.phenology.statistics.max;	
	return max;
}


//Function to get 'AUC'-Phenology Data from JSON
function getAUCPhenologyFromResponse(parsedJSONObj) {
	var auc = "";
	auc = parsedJSONObj.phenology.statistics.area_under_curve;	
	return auc;
}

//Get Date Obj From Date String 
function getDateFromStr(sDate) {
	var dateObj = new Date(sDate);
	return dateObj;
}


//Function compare two Date Objects
function isDateoneGtEqDatetwo(dateOneStr, dateTwoStr) {
	if (dateOneStr != null && dateTwoStr != null) {
		var dateOneObj = new Date(dateOneStr);
		var dateTwoObj = new Date(dateTwoStr);
		if (dateOneObj >= dateTwoObj) {
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}
}

//Function compare Date lies within Months 1 and 2
function isDateBtwMonths(monthNum1, monthNum2, dateStr) {
	var isBetweenMonths;
	if (dateStr != null) {
		var dateObj = new Date(dateStr);
		var dateMonth = dateObj.getMonth();
		if(monthNum1<monthNum2){
			isBetweenMonths = (dateMonth >= monthNum1) && (dateMonth <= monthNum2);
		} else {
			isBetweenMonths = (((dateMonth >= monthNum1) && (dateMonth <= 11)) || (dateMonth >= 0)
					&& (dateMonth <= monthNum2));
		}		
		if (isBetweenMonths) {
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}
}

function isDateBetweenNigerianRiceDrySeason(dateStr) {
	var month1 = 10; //November Month
	var month2 = 1;  //Feburary Month
	if (isDateBtwMonths(month1, month2, dateStr)) {
		return true;
	}
	return false;
}

function isDateBetweenNigerianRiceWetSeason(dateStr) {
	var month1 = 3; //April Month
	var month2 = 9; //October Month
	if (isDateBtwMonths(month1, month2, dateStr)) {
		return true;
	}
	return false;
}

function filterBySeasonDate(previousSensedDate, sensingDate, tableTagID, percentileOfAreaCropped, cpiValue, seasonName, sDate, pStartDate, pEndDate, parcelId, fractionUnderCrop){
	var seasonName = "";
	const WET_SEASON = "WET SEASON";
	const DRY_SEASON = "DRY SEASON";
	var sDateStr = null;
	var croppedAreaReqURL = null;
	var ndviValObj = null;	
	
	if (!isDateoneGtEqDatetwo(previousSensedDate, sensingDate)) {
		if(isDateBetweenNigerianRiceWetSeason(sensingDate)||isDateBetweenNigerianRiceDrySeason(sensingDate)){
			if(isDateBetweenNigerianRiceWetSeason(sensingDate)){
				seasonName = WET_SEASON;
			}else if(isDateBetweenNigerianRiceDrySeason(sensingDate)){
				seasonName = DRY_SEASON;
			}else{
				seasonName = HYPEN_VAL;
			}
			ndviValObj = {sensingDate : sensingDate,percentileOfAreaCropped:percentileOfAreaCropped,cpiValue:cpiValue,seasonName:seasonName,fractionUnderCrop : fractionUnderCrop};
			ndviValArr.push(ndviValObj);
			//appendRowValue(tableTagID,sensingDate,percentileOfAreaCropped,cpiValue,seasonName);
		}
				sDateStr = getNextSixtySenseDate(sensingDate);
				croppedAreaReqURL = getCroppedAreaURLForParcel(parcelId,
						sDateStr);
				previousSensedDate = getDateFromStr(sensingDate);
				sDate = getNextSixtyDaysDateAsString(sensingDate);
				getCroppedAreaInfoTimeline(parcelId, sDate, pStartDate,
						pEndDate, croppedAreaReqURL, previousSensedDate);				
		}	
}


//Below Code to show values in Table for CPI of Dry and Wet Seasons.
$(document)
		.ajaxStop(
				function(event, request, settings) {
					var secondSeasonMaxArrIndex = 0;
					var nextSeasonFoundFlag = false;
					var nextSeasonType = "";
					if (ndviValArr.length >= 1) {
						ndviValArr.sort(function(a, b) {
							return b.fractionUnderCrop - a.fractionUnderCrop
						});
						appendRowValue(tableTagID, ndviValArr[0].sensingDate,
								ndviValArr[0].percentileOfAreaCropped,
								ndviValArr[0].cpiValue,
								ndviValArr[0].seasonName);						
						nextSeasonType = getNextSeasonSorted(ndviValArr);						
						for (i = 1; i < ndviValArr.length; i++) {
							if (ndviValArr[i].seasonName == nextSeasonType
									&& !nextSeasonFoundFlag) {
								secondSeasonMaxArrIndex = i;
								nextSeasonFoundFlag = true;
							}
						}
						if (secondSeasonMaxArrIndex != 0) {
							appendRowValue(
									tableTagID,
									ndviValArr[secondSeasonMaxArrIndex].sensingDate,
									ndviValArr[secondSeasonMaxArrIndex].percentileOfAreaCropped,
									ndviValArr[secondSeasonMaxArrIndex].cpiValue,
									ndviValArr[secondSeasonMaxArrIndex].seasonName);
						}
						ndviValArr = new Array();
						hideAnalysisProgressbarDiv();
					}
				});


function getNextSeasonSorted(ndviValArr) {
	if (ndviValArr[0].seasonName == WET_SEASON) {
		return DRY_SEASON;
	} else if (ndviValArr[0].seasonName == DRY_SEASON) {
		return WET_SEASON;
	}
}

function showAnalysisProgressbarDiv(){
	$("#"+analysisProgressBarDivId).removeClass("hide");
}

function hideAnalysisProgressbarDiv(){
	$("#"+analysisProgressBarDivId).addClass("hide");
}


// 	Similarity Feature Functions Begins.

function getGeocledianSimilarityValueForParcel(parcelId,crop) {
	var startDateStr = "";
	var endDateStr = "";
	var entity = "";
	if (isEmpty(crop)) {
		crop = getDefaultCrop();
	}
	startDateStr = getGeoCledianStarDate("dateRangeValue");
	startDateStr = getRegularDateFormat(startDateStr);
	endDateStr = getGeoCledianEndDate("dateRangeValue");
	endDateStr = getRegularDateFormat(endDateStr);
	//getGeoCledianSimilarityAnalysis(parcelId, startDateStr, endDateStr, crop);
	getGeoCledianSimilarityAnalysisEntityField(parcelId, startDateStr, endDateStr, crop, entityFieldId);
}


function getGeoCledianSimilarityAnalysis(parcelId, startDateStr, endDateStr, crop) {
	var geoCledianSimilarityAnalysisReqURL = geoCledianSimilarityAnalysisReqURLPart1 + parcelId;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart2;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart3 + geoCledianKey;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart4 + similarityRadius;	
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart5 + startDateStr;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart6 + endDateStr;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart7 + crop;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart8 + crop;	
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart9 + similarityInterval;
	//geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLClassify;
	
	console.log("GeoCledian Similarity Analysis Request URL:");
	console.log(geoCledianSimilarityAnalysisReqURL);	
	sendAjaxReqForSimilarityAnalysisForCrop(geoCledianSimilarityAnalysisReqURL,crop);
}


function sendAjaxReqForSimilarityAnalysisForCrop(geoCledianSimilarityAnalysisReqURL,crop) {
	var responseValue = null;
	var similarityConfirmityValue = null;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : geoCledianSimilarityAnalysisReqURL,
		"method" : "GET",
		"headers" : {
			"Content-Type" : "application/json",
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		success : function(responseData, textStatus, jqXHR) {
			responseValue = JSON.stringify(responseData);
			similarityConfirmityValue = getSimilarityCorrelationValue(responseValue);
			if (!isEmpty(similarityConfirmityValue)) {
				console.log("Geocledian: Confirmity Similarity Value :" + similarityConfirmityValue);				
				setCorelationalSimilarityValue(similarityValue, similarityConfirmityValue,crop); //**Similarity HTML ID
			} else {
				console.log("Error: Couldn't Get Similarity Confirmity Value:");
				enableElementById('registersimilarityValueFarm');
				setCorelationalSimilarityValue(similarityValue, SIMILARITY_ERROR_RESULT,crop);
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Geocledian API Similarity Corelational Value Request Error.');
			responseValue = JSON.stringify(responseData);
		}
	});
}


function getSimilarityConfirmityValue(responseValue) {
	var jsonResponse = JSON.parse(responseValue);
	var similarityConfirmityValue = null;
	try{
	similarityConfirmityValue = jsonResponse.classification.conformity;
	}catch (error) {
		console.log("Error:" + error);
	}
	return similarityConfirmityValue;
}

function getCorelationalSimilarityValue(responseValue) {
	var jsonResponse = JSON.parse(responseValue);
	var similarityCorelationalValue = null;
	try{
		jsonResponse.similarity.correlation;
	}catch (error) {
		console.log("Error:" + error);
	}
	return similarityCorelationalValue;
}

function setCorelationalSimilarityValue(similarityDisplayId, similarityValue,
		crop) {
	var similarityMsg = "Similarity with Crop '" + crop + "' : ";
	var similarityWidget = $("#" + similarityDisplayId);
	similarityWidget.html('');
	similarityWidget.html(similarityMsg + similarityValue);
}

// code to get NDVI Similarity entity from field

function getGeoCledianSimilarityAnalysisEntityField(parcelId, startDateStr, endDateStr, crop, entityFieldId) {
	var geoCledianSimilarityAnalysisReqURL = geoCledianSimilarityAnalysisReqURLPart1 + parcelId;
	var entityVal = $("#"+entityFieldId).val();
	if(isEmpty(entityVal)){
		entityVal = crop;
	}
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart2;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart3 + geoCledianKey;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart4 + similarityRadius;	
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart5 + startDateStr;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart6 + endDateStr;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart7 + crop;
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart8 + entityVal;	
	geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLPartPart9 + similarityInterval;
	//geoCledianSimilarityAnalysisReqURL += geoCledianSimilarityAnalysisReqURLClassify;
	
	console.log("GeoCledian Similarity Analysis Request URL:");
	console.log(geoCledianSimilarityAnalysisReqURL);	
	sendAjaxReqForSimilarityAnalysisForCrop(geoCledianSimilarityAnalysisReqURL,crop);
}

// Get Similarity Correlation from Response

function getSimilarityCorrelationValue(responseValue) {
	var jsonResponse = JSON.parse(responseValue);
	var similarityCorrelation = null;
	var retVal = "False";
	similarityCorrelation = jsonResponse.similarity.correlation;
	try {
		if (similarityCorrelation >= SIMILARITY_CORRELATION_TRESHOLD) {
			retVal = "True";
		}
	} catch (error) {
		console.log("Error:" + error);
	}
	return retVal;
}