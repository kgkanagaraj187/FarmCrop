//Vultus API Integration.
// Created on 16SEP2019
// (c) STS
var coordinatesForVultusField;
var vultusFieldRequestJSONStrSuffix = "{\"data\":{\"type\":\"Feature\",\"properties\":{\"label\":\"Tester4totamaccusamus\",\"crop\": \"Sample\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":";
var vultusFieldRequestJSONStrPrefix = "}}}";
var responseFormVultusFieldReq;
var vultusFieldItemRequestJSONStr;
var vultusAuthToken = "Bearer "
		+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1Y2FmMzUyM2U0MjQ3ZTQ3MWVmNDg3ODkiLCJzZXJ2aWNlIjoibml0cm9nZW4iLCJpYXQiOjE1NjU2ODU3MjIsImV4cCI6MTU2ODI3NzcyMn0.NovqDCIddKpI3RB_DdhOSOAbVwN7-pSwO6whJMebh7o";
var vultusRequestURL = "https://api.spatial.farm";
var vultusFieldURLPage = "field";
var vultusSubscriptionPage = "subscription";
var vultusFetchPageURLPart = "fetch";
var vultusIdURLPart = "id";
var vultusSatelliteURLPart = "satellite";
var vultusSatelliteLandSatVal = "landsat-pds";
var vultusSatelliteSentinelS2L1cVal = 'sentinel-s2-l1c';
var vultusIndexVal = "ndvi";
var vultusIndexURLPart = "index";
var vultusSubscriptionPlantHealthPage = "planthealth";
var vultusPlanthHealthSubscPrefix1 = "{\"data\":{\"timeSpan\":{\"startAt\":\"";
var vultusPlanthHealthSubscPrefix2 = "\",\"endAt\":\"";
var vultusPlanthHealthSubscPrefix3 = "\"},\"cloudTolerance\":{\"min\":0,\"max\":20},\"provider\":\"sentinel-s2-l1c\",\"models\":\"NDVI\"},\"relationship\":\"";
var vultusPlanthHealthSubscPrefix4 = "\"}";

function sendVultusFieldToAPI(vultusFieldItemRequestJSONStr) {

	jQuery.post("vultusApi_populatePlotGeoInfoKML.action", {
		vultusApiFieldReq : vultusFieldItemRequestJSONStr
	}, function(result) {
		console.log("*Hit");
		console.log(result);
	});
}

function getVultusFieldItemRequest(coordinatesForVultusField) {
	var coordinates = JSON.stringify(coordinatesForVultusField);
	var vultusFieldItemRequestJSONStr = vultusFieldRequestJSONStrSuffix;
	vultusFieldItemRequestJSONStr = vultusFieldItemRequestJSONStr + coordinates
			+ vultusFieldRequestJSONStrPrefix;
	return vultusFieldItemRequestJSONStr;
}

function sendVultusFieldItemRequest(vultusFieldItemRequestJSONStr) {

	var responseValue = null;
	var subsReqValue = null;
	var subsResValue = null;
	var vultusFieldRequestURL = vultusRequestURL + '/' + vultusFieldURLPage;
	console.log("*Hitter:2");
	console.log(vultusFieldItemRequestJSONStr);
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : vultusFieldRequestURL,
		"method" : "POST",
		"headers" : {
			"Content-Type" : "application/json",
			"Authorization" : vultusAuthToken,
			"Accept" : "*/*",
			"Cache-Control" : "no-cache"
		},
		"processData" : false,
		"data" : vultusFieldItemRequestJSONStr,
		success : function(responseData, textStatus, jqXHR) {
			console.log("in");
			responseValue = JSON.stringify(responseData);
			subsReqValue = getVultusPlanthHealthSubsc(responseValue);
			if (!isEmpty(subsReqValue)) {
				sendVultusFieldItemSubscReq(subsReqValue);
			}
		},
		error : function(responseData, textStatus, errorThrown) {
			alert('POST Request TO Vultus API Error.');
			responseValue = JSON.stringify(responseData);
			console.log("*hitter: Error");
			console.log(responseValue);
		}
	});
}

function getVultusPlanthHealthSubsc(responseValue){
	var startDate = getVultusStarDate();
	var endDate = getVultusEndDate();
	var startDateStr="";
	var endDateStr="";
	var jsonResponse = JSON.parse(responseValue);
	var fieldRequesApproveId = jsonResponse._id;
	var jsonPlantHealthReqStr = null;
	if(!isEmpty(startDate)&&!isEmpty(endDate)&&!isEmpty(fieldRequesApproveId)){
		startDateStr = getDateForVultusRequest(startDate);	            		
		endDateStr = getDateForVultusRequest(endDate);
		jsonPlantHealthReqStr = vultusPlantHealthReqJSON(startDateStr,endDateStr,fieldRequesApproveId);          		
	}
	return jsonPlantHealthReqStr;
}


function vultusPlantHealthReqJSON(startDateStr,endDateStr,fieldRequesApproveId){
	var jsonPlantHealthReqStr = vultusPlanthHealthSubscPrefix1;
	jsonPlantHealthReqStr += startDateStr;
	jsonPlantHealthReqStr += vultusPlanthHealthSubscPrefix2;
	jsonPlantHealthReqStr += endDateStr;
	jsonPlantHealthReqStr += vultusPlanthHealthSubscPrefix3;
	jsonPlantHealthReqStr += fieldRequesApproveId;
	jsonPlantHealthReqStr += vultusPlanthHealthSubscPrefix4;
	return jsonPlantHealthReqStr;
}

function getVultusStarDate(){
	var d1=	jQuery('#daterange').val();
	var d2= d1.split("-");
	var startDate = null;
	if(d2.length>1){
		var value1= d2[0];
		var value2= d2[1];
	}
	startDate = new Date(Date.parse(value1));
	return startDate;
}

function getVultusEndDate(){
	var d1=	jQuery('#daterange').val();
	var d2= d1.split("-");
	var endDate = null;
	if(d2.length>1){
		var value1= d2[0];
		var value2= d2[1];
	}
	endDate = new Date(Date.parse(value2));
	return endDate;
}

function getDateForVultusRequest(dateObj){
	var timeVal = "T00:00:00.000Z";
	var monthVal = ("0" + (dateObj.getMonth()+1)).slice(-2);
	var dateVal = ("0" + (dateObj.getDate())).slice(-2);
	var dateObjStr  = dateObj.getFullYear()+"-"+monthVal+"-"+dateVal;
	dateObjStr = dateObjStr + timeVal;
	return dateObjStr;
}


function sendVultusFieldItemSubscReq(vultusFieldItemSubsReq){
	
	 var responseValue = null;
	 var fetchReqIdValue = null;
	 var vultusFieldItemSubsReqURL = vultusRequestURL+'/'+vultusSubscriptionPage+'/'+vultusSubscriptionPlantHealthPage;
	 console.log("*Hitter:24");
	 console.log(vultusFieldItemSubsReq);            	 
	 $.ajax({
		    "async": true,
		    "crossDomain": true,            		            			  
		    "url": vultusFieldItemSubsReqURL,
		    "method": "POST",
		    "headers": {
		    "Content-Type": "application/json",
		    "Authorization": vultusAuthToken,
		    "Accept": "*/*",
		    "Cache-Control": "no-cache"           		    
		     },
		     "processData": false,
		     "data": vultusFieldItemSubsReq,
		    success: function (responseData, textStatus, jqXHR) {
		        console.log("in");
		        responseValue = JSON.stringify(responseData);
		        subsReqValue = getVultusPlanthHealthSubsc(responseValue);
		        if(!isEmpty(subsReqValue)){
		        	console.log("*Hitter:");
		        	console.log(responseValue);
		        	fetchReqIdValue = getVultusPlanthHealthId(responseValue);
		        	if(!isEmpty(fetchReqIdValue)){
		        		alert("*Hit:"+fetchReqIdValue);
		        		sendVultusFieldFetchReq(fetchReqIdValue,true);           		        		
		        	}
		        }
		    },
		    error: function (responseData, textStatus, errorThrown) {
		        alert('POST Request TO Vultus API Error.');
		        responseValue = JSON.stringify(responseData);
		        console.log("*hitter: Error");
		        console.log(responseValue);            		        
		    }
		});            	 
}

function getVultusPlanthHealthId(responseValue){    		
	var jsonResponse = JSON.parse(responseValue);
	var fieldRequesApproveId = jsonResponse.relationship;
	return fieldRequesApproveId;
	}

function sendVultusFieldFetchReq(vultusFieldFetchReq,initialHit){   
	
  	 var responseValue = null;
  	 var subsReqValue = null;
  	 var vultusFieldFetchReqURL = vultusRequestURL+'/'+vultusFetchPageURLPart+'/'+vultusSubscriptionPlantHealthPage+'/'+vultusIdURLPart+'/'+vultusFieldFetchReq+"/"+vultusSatelliteURLPart+"/"+vultusSatelliteSentinelS2L1cVal+"/"+vultusIndexURLPart+"/"+vultusIndexVal;
  	 var kmlFileURLArrays = null;
  	 var timeOutInMilliSeconds = 10000; // milliseconds
  	 console.log("*Hitter:222");
  	 console.log(vultusFieldFetchReq);            	 
  	 $.ajax({
  		    "async": true,
  		    "crossDomain": true,            		            			  
  		    "url": vultusFieldFetchReqURL,
  		    "method": "GET",
  		    "headers": {
  		    "Content-Type": "application/json",
  		    "Authorization": vultusAuthToken,
  		    "Accept": "*/*",
  		    "Cache-Control": "no-cache"
  		     },
  		     "processData": false,
  		     "data": "",
  		    success: function (responseData, textStatus, jqXHR) {
  		    	if(!initialHit){
  		    	console.log("in");
  		        responseValue = JSON.stringify(responseData);
  		        // subsReqValue = getVultusPlanthHealthSubsc(responseValue);
  		      	kmlFileURLArrays = getVultusPlanthHealthKMLFilesUrl(responseValue);
  		      	setKMLFilesGmapOverlay(kmlFileURLArrays);
  		      	console.log("*Hit:waited");
  		    	}else{
		        	setTimeout(function() {sendVultusFieldFetchReq(vultusFieldFetchReq,false); }, timeOutInMilliSeconds); // wait
																															// code
																															// goes
																															// here
  		        }
  		    },
  		    error: function (responseData, textStatus, errorThrown) {
  		    	alert('GET Request TO Vultus API Error.');
  		        responseValue = JSON.stringify(responseData);
  		        console.log("*hitter: Error");
  		        console.log(responseValue);              		       
  		    }
  		});            	 
  }
  	 
  	function getVultusPlanthHealthKMLFilesUrl(responseValue){    		
		var jsonResponse = JSON.parse(responseValue);
		var fileOuput = jsonResponse.output;
		var kmlFilesArray = fileOuput.kml;
		return kmlFilesArray;
		}
  	
  	function setKMLFilesGmapOverlay(kmlFileURLArrays){
  		if(!isEmpty(kmlFileURLArrays)){
  			kmlFileURLArrays.forEach(element => {
  			  console.log("*Hit:");
  			  console.log(element);               			              	
  			 var staticKMLLayer = new google.maps.KmlLayer({
                 url: element,  // NDVI
                 map: map
               });       
  			});                         
  		}
  	}
  	
  	
    function getVultusFieldId(coorArr,coordinatesForVultusField,vultusFieldItemRequestJSONStr){
   	 var vultusFieldIdSubscriptionReq = null;
   	 coordinatesForVultusField = getCoordinateArrayForField(coorArr);
        vultusFieldItemRequestJSONStr = getVultusFieldItemRequest(coordinatesForVultusField);
        sendVultusFieldItemRequest(vultusFieldItemRequestJSONStr);                 
   }
    
    function setNewShape(newShape){
   	 var initCoordinate; // Starting Point Coordinate, Initial coordinate.
        var interCoordinate; // Inbetween Coordinate Points.
        coorArr = new Array();
        
        initCoordinate = valueOnsetForInitCoordinateExchange(newShape);

        for (var i = 0; i < newShape.getPath()
          .getLength(); i++) {
        	interCoordinate = valueOnsetForCoordinateExchange(newShape,i);                    	

          coorArr.push(interCoordinate);
        }
        coorArr.push(initCoordinate);
   }
    
    function valueOnsetForCoordinateExchange(newShape,i){
		var initCoordinate;
        var interCoordinate="";
        var coordinateTransfer = new Array();
        var tmpCoordinate = "";
		initCoordinate = newShape.getPath().getAt(i)
        .toUrlValue(20);
    	coordinateTransfer = initCoordinate.split(",");            	
    	if(coordinateTransfer.length>=2){    	 
  tmpCoordinate = parseFloat(coordinateTransfer[0]);
  coordinateTransfer[0]= parseFloat(coordinateTransfer[1]);
  coordinateTransfer[1] = tmpCoordinate;
    	}            	
    	return coordinateTransfer;
	}
    
    function valueOnsetForInitCoordinateExchange(newShape){
		var initCoordinate;
        var interCoordinate="";
        var coordinateTransfer = new Array();
        var tmpCoordinate = "";
		initCoordinate = newShape.getPath().getAt(0)
        .toUrlValue(20);
    	coordinateTransfer = initCoordinate.split(",");            	
    	if(coordinateTransfer.length>=2){    	 
  tmpCoordinate = parseFloat(coordinateTransfer[0]);
  coordinateTransfer[0]= parseFloat(coordinateTransfer[1]);
  coordinateTransfer[1] = tmpCoordinate;
    	}            	
    	return coordinateTransfer;
	}
    
    
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
    
    function changePolygon(newPlotting) {

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
        //getVultusFieldId(coorArr,coordinatesForVultusField,vultusFieldItemRequestJSONStr);
        setMapApi(coorArr,coordinatesForVultusField,vultusFieldItemRequestJSONStr);
        //sendVultusFieldToAPI(vultusFieldItemRequestJSONStr);
        console.log("*Hitter:5");
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