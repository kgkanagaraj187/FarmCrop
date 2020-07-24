<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/form-assets.jsp"%>
<%@ include file="/jsp/common/detail-assets.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<META name="decorator" content="swithlayout">
</head>
<script type="text/javascript">
var dateRangeForGcReq = "";
var map=null;
$(document).ready(function(){
    /* populateFarmerList('farmer'); //To Load Farmer List in dropdown field. */    
    setDateFieldVals(); //Set Date Field Values.
    resetFarmerDropdown();
    hideGCWidgets();
    hideGetNdviBtns();
    setDateFieldValue();
    uncheckField("referenceIdCheckField");
});

function setDateFieldValue(){
	 var end = moment();
	$('input[name="dates"]').daterangepicker();
}

function resetFarmerDropdown(){
    $("#farmerForGcParcel").val("");
}

function hideGcMap(){
    $("#gcKmapId").hide();
}

function showGcMap(){
    $("#gcKmapId").show();
}

function populateAllFarmParcelIdForFarmer(name,farmerId){
    var elementObj = $("#"+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmerGeoFarms.action',
        async: false,
        type: 'post',
        data: {farmerId:farmerId},
        success: function(result) {
            insertHeaderOptionById(name);
            insertOptionsById(name,JSON.parse(result));
            elementObj.val("");
        }        

    });
    
}

function populateFarmCropsOfFarm(name,farmerId){
    var elementObj = $("."+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmerGeoFarms.action',
        async: false,
        type: 'post',
        data: {farmerId:farmerId},
        success: function(result) {            
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");
        }
    });
    
}


function insertOptionsByClass(ctrlName, jsonArr) {
     $.each(jsonArr, function(i, value) {
           $("."+ctrlName).append($('<option>',{
                value: value.id,
             text: value.name
     }));
});    
}

function insertHeaderOptionByClass(ctrlName) {
           $("."+ctrlName).append($('<option>',{
                value: "",
                text: "Select"
     }));        
}


function insertOptionsById(ctrlName, jsonArr) {
     $.each(jsonArr, function(i, value) {
          $("#"+ctrlName).append($('<option>',{
               value: value.id,
               text: value.name
     }));
});    
}

function insertHeaderOptionById(ctrlName) {
          $("#"+ctrlName).append($('<option>',{
               value: "",
               text: "Select"
     }));        
}

function populateFarmerList(name){
    var elementObj = $("#"+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmerList.action',
        async: false,
        type: 'post',
        data: {},
        success: function(result) {
            insertHeaderOptionById(name);
            insertOptionsById(name,JSON.parse(result));
            elementObj.val("");
        }
    });
}

function populateFarmParcelIdForFarmer(name,farmerId){
    var elementObj = $("."+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmsWithGcParcelIDs',
        async: false,
        type: 'post',
        data: {farmerIDGCParcel:farmerId},
        success: function(result) {
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");            
        }

    });
    emptyFarmCropsField();
    
}

function populateFarmForFarmer(name,farmerId){
    var elementObj = $("."+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmsOfSelectedFarmer',
        async: false,
        type: 'post',
        data: {farmerId:farmerId},
        success: function(result) {
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");            
        }

    });
    emptyFarmCropsField();
    hideGetNdviBtns();
}

function emptyFarmCropsField(){
    var farmCropsElementById =  $("#farmCropParcel");
    farmCropsElementById.empty();
    hideGCWidgets();
}

function populateFarmCropsByFarm(name,farmId){
    var elementObj = $("."+name);
    elementObj.empty();
    var cat = $.ajax({
        url: 'ndvi_populateFarmsCropsWithGcParcelIDs',
        async: false,
        type: 'post',
        data: {farmId:farmId},
        success: function(result) {
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");
        }

    });
    setFarmGCParcelID(farmId);    
}

function populateFarmsCropsOfFarm(name,farmIdVal,farmForGcParcel,farmCropsGCParcel){
    var elementObj = $("."+name);
    elementObj.empty();
    var farmFieldObj = $("#"+farmIdVal);
    var farmId = farmFieldObj.val();
    var farmIds = farmId.split('~');
    var selectedFarmId = "";    
 	if(farmIds.length>=1){
 	selectedFarmId = farmIds[0];
 	disableElementById('registerParcelIdFarm');
    var cat = $.ajax({
        url: 'ndvi_populateFarmsCrops',
        async: false,
        type: 'post',
        data: {farmId:selectedFarmId},
        success: function(result) {
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");            
        }

    });    
 	}

    if(farmIds.length>=2&&!isEmpty(farmIds[1])){
    	setFarmGCParcelID(farmIds[1]);
    	// Set Chart Parcel ID OF GC.
    	setChartCropsGCParcelID(farmIds[1]);
 	}else if(farmIds.length>=2&&isEmpty(farmIds[1])) {
 		registerFarmWithGCParcelId(farmIds[0]); 
 	}
    if(!isEmpty(selectedFarmId)){
        loadFarmGCParcelId(selectedFarmId);
    }
}


function registerFarmWithGCParcelId(farmId){
	 var polygonGeometry = ""; 
	 var polygonStr;
	 disableElementById('registerParcelIdFarm');
	 var cat = $.ajax({
	        //url: 'ndvi_populateFarmCoordPolygon',
	        url : 'ndvi_populateFarmCoordinates',
	        async: false,
	        type: 'post',
	        data: {farmId:farmId,selectedFarmType:"1"},
	        success: function(result) {
	        	if(!isEmpty(result)){
					//polygonGeometry = result;
	        		//getGeocledianApiParcelIdForFarmFromAction("",polygonGeometry,"dateRangeValue");	        		
					//loadFarmGCParcelId(polygonGeometry);
					polygonStr = result;//result.polygon;
					getGCParcel(polygonStr,map,"farmForGcParcel","farmCropsGCParcel");					
	        	}
	        }

	    });  
 }

function setFarmCropsGCParcelID(val){
    
    var kmapElement = $("#kmapId1");                        
    if(!isEmpty(val)){
    kmap1.destroy();
    kmapElement.removeAttr( "parcelId" );
    //kmapElement.removeAttr( "autoplay" );
    kmapElement.attr("parcelId",val);
    //kmapElement.attr("autoplay","false");            
    kmap1 = new KMaps({
          mapControlId: 'kmapId1',
          style: { border: 'gray solid 3px' },
          timeline: {},
          provider: 'GoogleMaps.Hybrid'
        });
    console.debug(kmap1);
}
    showGcMap();
}

function setFarmGCParcelID(val){
    var kmapElement = $("#kmapId1");
    var valArr = null;
    var parcelId = null;
    if(!isEmpty(val)){
    kmapElement.removeAttr( "parcelId" );
    //kmapElement.removeAttr( "autoplay" );
    valArr = val.split("~");
    if(valArr.length>=2){
    parcelId = valArr[1];
    if(!isEmpty(parcelId)){
    kmap1.destroy();
    kmapElement.attr("parcelId",parcelId);
    //kmapElement.attr("autoplay","false");            
    kmap1 = new KMaps({
          mapControlId: 'kmapId1',
          style: { border: 'gray solid 3px' },
          timeline: {},
          provider: 'GoogleMaps.Hybrid'
        });
    console.debug(kmap1);
    showGcMap();
    }else{
    	hideGCWidgets();
    }
    }else{
    	hideGCWidgets();
    }
}
 // To Show Chart With Pracel ID and FUC Value
setChartCropsGCParcelID(val);
}


function setDateFieldVals(){
	dateRangeForGcReq = "";
	var startDate  =  new Date(); // two year before present date.
    var endDate =  new Date();
	var beforeYearCnt = 2;
	startDate = new Date(startDate.setFullYear(new Date().getFullYear()-beforeYearCnt));	
	dateRangeForGcReq = getGCDateFormat(startDate, endDate);	
	setDateRangeValField(dateRangeForGcReq);    
}   

function getGCDateFormat(startDate, endDate){
	dateRangeForGcReq = startDate.getMonth()+1;
	dateRangeForGcReq += "/"+startDate.getDate();
	dateRangeForGcReq += "/"+startDate.getFullYear() +" - ";
	dateRangeForGcReq += endDate.getMonth()+1;
	dateRangeForGcReq += "/"+endDate.getDate();
	dateRangeForGcReq += "/"+endDate.getFullYear();
	return dateRangeForGcReq;
}

function setDateRangeValField(dateRangeForGcReq){	
	$("#dateRangeValue").val(dateRangeForGcReq);
}

function initmap(){
	map = new google.maps.Map(document.getElementById('map'), {
    center: {
        lat: 12.8797,
        lng: 121.7740
      },
      zoom:6,
      mapTypeId: google.maps.MapTypeId.HYBRID,
    });
	
	
}

function showNdviFrmBtn(){
	hideElementById('ndviFarmCrpBtn');
	showElementById('ndviFarmBtn');
	enableElementById('registerParcelIdFarm');
	enableElementById('registerParcelIdFarmCrop');
}

function showNdviFrmCrpBtn(){
	hideElementById('ndviFarmBtn');
	showElementById('ndviFarmCrpBtn');
	enableElementById('registerParcelIdFarm');
	enableElementById('registerParcelIdFarmCrop');	
}

function hideGetNdviBtns(){
	hideElementById('ndviFarmBtn');
	hideElementById('ndviFarmCrpBtn');
}

function populateFarmsCropsOfFarmSelected(name,farmId){
    var elementObj = $("."+name);
    elementObj.empty();
    var farmIds = farmId.split('~');
    var selectedFarmId = "";    
 	if(farmIds.length>=1){
 	selectedFarmId = farmIds[0]; 	
    var cat = $.ajax({
        url: 'ndvi_populateFarmsCrops',
        async: false,
        type: 'post',
        data: {farmId:selectedFarmId},
        success: function(result) {
            insertHeaderOptionByClass(name);
            insertOptionsByClass(name,JSON.parse(result));
            elementObj.val("");            
        }

    });    
 	}
 	/* Uncomment below line to enable farm registration for Parcel with Cotton Crop Type.*/
 	//showNdviFrmBtn();  
}

function hideGcChart(){
    $("#chartApi1").addClass("hide");
}

function showGcChart(){
    $("#chartApi1").removeClass("hide");
}

function setChartCropsGCParcelID(val){	
	var widgetIndex = 0;	
    vmRoot.setParcelId(widgetIndex, val);
    hideGcChart();
    showGcChart();
}

function hideGCWidgets(){
	hideGcMap();
	hideGcChart();
}

function showReference(){
	$("#similarityEntityField").removeClass("hide");
	$("#similarityCheckField").addClass("hide");
	}

function uncheckField(fieldId){
	$("#"+fieldId).prop("checked", false);
}
</script>

<!--  Beginning of Kmaps Integration Code -->
    <link rel="stylesheet"
        href="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/leaflet/dist/leaflet.css" />
    <link
        href="http://netdna.bootstrapcdn.com/twitterbootstrap/2.3.2/css/bootstrap-combined.min.css"
        rel="stylesheet" />
    <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
    <link rel="stylesheet"
        href="plugins/mapNvdiAPIs/kmaps-gl_v3/testpage/css/kmaps-gl.css" />
    <link rel="stylesheet" href="plugins/mapNvdiAPIs/DateRangeLib/CSS/daterangepicker.css" />
    <link rel="stylesheet"
        href="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/vis/dist/vis.min.css" />
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/vis/dist/vis.min.js"></script>
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/leaflet/dist/leaflet.js"></script>
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/leaflet-providers/leaflet-providers.js"></script>
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/leaflet-wmts/leaflet-tilelayer-wmts.js"></script>
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/lodash/lodash.min.js"></script>
    <script
        src="plugins/mapNvdiAPIs/kmaps-gl_v3/bower_components/spin.js/spin.min.js"></script>
    <script src="plugins/mapNvdiAPIs/kmaps-gl_v3/kmaps-gl.min.js"></script>
    <script src="plugins/mapNvdiAPIs/DateRangeLib/moment.min.js"></script>
    <script src="getTraceDetails/js/moment.js"></script>
    <script src="plugins/mapNvdiAPIs/DateRangeLib/daterangepicker.js"></script>
    <link href="plugins/mapNvdiAPIs/gc-cropstatus-master/css/gc-cropstatus.css" rel="stylesheet" />
	<script type="text/javascript" src="plugins/mapNvdiAPIs/gc-cropstatus-master/js/gc-cropstatus-init.js"></script>
	<script type="text/javascript" src="plugins/mapNvdiAPIs/gc-cropstatus-master/js/d3.v3.min.js"></script>
	<link href="fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet">
        
    <style>
#kmapId1map {
    width: 100%;
    height: 500px;
}

.play_pause {
    display: none !important;
}

#kmap1map {
    width: 100%;
    height: 300px;
}
.filterLabel{
color: black !important;
font-weight: bold;
}
.flxr label {
    color: black !important;
}

.btnPanel{

}

.btnLabel{
color: white !important;
font-weight: bold;
margin-left: 4px;
margin-right: 4px;
}

.leftPadded{
padding-left: -12px;
margin-right: -15px;
}

.chartCenter{
margin-top: 45px;
padding-bottom : 10%;
align-content: center;
}

.similarityFormat{
font-weight: bold;
font-size: 20px;
padding-bottom: 15px;
}
</style>
<!--  End of Kmaps Integration Code -->

<body>

    <%-- <div class="row">  <!-- Commented for making new registration Packets -->
        <div class="flxr">
            <div class="nvdiFilters">
                <h4>
                    <s:property value="%{getLocaleProperty('ndvi.geoInfo')}" />
                    <i class="fa fa-bar-chart" aria-hidden="true"></i>
                </h4>
                <div class="form-element">
                    <label class="filterLabel" for="txt"><s:text name="farmer"/></label>
                    <s:select class="form-control input-sm select2 slideNot"
                        list="farmerListMap" headerKey=""
                        headerValue="%{getText('txt.select')}" name="farmerId"
                        id="farmer" listKey="key" listValue="value"
                        onchange="populateAllFarmParcelIdForFarmer('farms',this.value);" />
                </div>
                
                <div class="form-element">
                    <label class="filterLabel" for="txt"><s:text name="farm"/></label>
                    <s:select class="form-control input-sm select2 slideNot"
                        list="farmListMap" headerKey=""
                        headerValue="%{getText('txt.select')}" name="geoFarms"
                        id="farms" listKey="key" listValue="value" />
                </div>
            </div>
            </div>
            
            <div class="appContentWrapper marginBottom">
                <div id="kmapId1" parcelId="33817" autoPlay="false"
                    autoPlayDelay="6" productKey="vitality"
                    products="vitality,variations,visible"
                    accessToken="911313c3-26a7-d95e-b42e-2c270548a5a6"
                    preloadImages="false"
                    hostURL="https://vs1.geocledian.com/agknow/api/v3/parcels"></div>
            </div>        
    </div> --%>
    <div id="baseDiv" style="width: 900px"></div>
    <!-- GeoCledian API Integration For Body Code begin-->
	<div class="appContentWrapper marginBottom"
		style="padding-left: 20px; padding-right: 20px;padding-bottom: 20px;">
		<ul class="nav nav-pills">
			<li class="active"><a data-toggle="tab" href="#ndviMap"><s:text
						name="ndviMap" /></a></li>

			<li><a data-toggle="tab" href="#ndviInfo"><s:text
						name="ndviInfo" /></a></li>
			<li><a data-toggle="tab" href="#similarityInfo"><s:text
						name="similarityInfo" /></a></li>
		</ul>
		<div class="tab-content">
			<!-- NDVI Map Tab -->
			<div id="ndviMap" class="tab-pane fade in active">
				<label class="filterLabel" style="font-size: 20px"> <s:text
						name="ndvi.geoInfo" /> <i class="fa fa-bar-chart"
					aria-hidden="true"></i>
				</label>
				<section class="reportWrap row" style="margin-left: 1px;padding-bottom: 50px;margin-right: 0px;">
				<div class="gridly">
					<div id="filterFields">
						<div class="filterEle" style="width: 200px">
							<label id="dateRange" class="filterLabel" for="txt"><s:text
									name="date.label" /></label>
							<div class="form-element">
								<input name="dates" id="dateRangeValue" type="text"
									readonly="true" style="width: 200px" />
							</div>
						</div>
						<div class="filterEle">
							<label class="filterLabel" for="txt"><s:text
									name="farmer" /></label>
							<div class="form-element">
								<s:select class="form-control input-sm select2 slideNot"
									list="farmersWithGCParcelIds" headerKey=""
									headerValue="%{getText('txt.select')}" name="farmerId"
									id="farmerForGcParcel" listKey="key" listValue="value"
									onchange="populateFarmForFarmer('farmParcel',this.value);" />
							</div>
						</div>
						<div class="filterEle">
							<label class="filterLabel" for="txt"><s:text name="farm" /></label>
							<div class="form-element">
								<s:select
									class="form-control input-sm farmParcel select2 slideNot"
									list="{}" headerKey="" headerValue="%{getText('txt.select')}"
									name="farmId" id="farmForGcParcel"
									onchange="populateFarmsCropsOfFarmSelected('farmCropParcel',this.value);" />
							</div>
						</div>
						<div class="filterEle">
							<label class="filterLabel" for="txt"><s:text
									name="farmCrops" /></label>
							<div class="form-element">
								<s:select
									class="form-control input-sm farmCropParcel select2 slideNot"
									list="{}" headerKey="" headerValue="%{getText('txt.select')}"
									name="farmerCropsGCParcel" id="farmCropsGCParcel"
									onchange="showNdviFrmCrpBtn();" />
							</div>
						</div>
						
						<div id="similarityEntityField" class="hide">
						<label class="filterLabel" for="txt"><s:text
									name="Reference Name" /></label>
								<div id="entityCheckBox" class="form-element"  style="width:100px">
									<s:textfield class="form-control input-sm farmCropParcel slideNot" id="entityField" />
								</div>
						</div>
						
						<div id="similarityCheckField">
						<label class="filterLabel" for="txt"><s:text
									name="Reference Name" /></label>
								<div id="entityCheckBox" class="form-element"  style="width:100px">
								<input type="checkbox" id="referenceIdCheckField" onclick="showReference();" />
								</div>
						</div>
							
						<div class="filterEle"
							style="padding-left: -12px; margin-right: -15px;">
							<div id="ndviFarmBtn" class="form-element"
								style="display: table; margin-top: 26px;">
								<button class="btn btn-success" id="registerParcelIdFarm"
									onclick="populateFarmsCropsOfFarm('farmCropParcel','farmForGcParcel','farmForGcParcel','farmCropsGCParcel');showAnalysisProgressbarDiv();">
									<label class="btnLabel"><s:text name="getNdviValueFarm" /></label>
								</button>
							</div>
							<div id="ndviFarmBtn" class="form-element"
								style="display: table; margin-top: 26px;">
								<div id="ndviFarmCrpBtn" class="form-element"
									style="display: table; margin-top: 27px;">
									<button class="btn btn-success" id="registerParcelIdFarmCrop"
										onclick="loadFarmCropMapForGC('farmCropsGCParcel');showAnalysisProgressbarDiv();"
										style="margin-bottom: 0px; padding-bottom: 13px; border-top-width: -12px; margin-top: -48px;">
										<label class="btnLabel"><s:text
												name="getNdviValueFarmCrop" /></label>
									</button>
								</div>
							</div>
						</div>
					</div>

					<div id="filterFields">
						<div class="filterEle"
							style="width: 200px; border-bottom-style: solid; border-bottom-width: 0px; margin-bottom: -20px;">
							<label style="padding-left: 0px;"><b>NDVI-LEGEND</b></label>
							<div id="mapLegendInfo" style="padding: 0px; display: flex;">
								<div id="mapLegendContent" class="has-text-justified"
									style="min-width: 150px; border-right: solid 1px #000 !important; margin: 0px 10px; padding-right: 40px;">
									<b>Vitality</b><br> <img
										src="https://agknow.geocledian.com/agknow/api/v3/parcels/8237/vitality/sentinel2/1890185.png?key=911313c3-26a7-d95e-b42e-2c270548a5a6&legend=true"
										title="The brown value means no living green vegetation (NDVI value <= 0.1)and dark green means dense living green vegetation (NDVI value >= 0.9)"
										style="width: 100%;"><br> <span
										style="padding-right: 0px; padding-left: 0px;"><label
										style="padding-right: 55px; font-weight: bold;">0.1</label> <label
										style="padding-left: 0px; font-weight: bold;">0.9</label></span>
								</div>

								<div id="mapLegendContent" class="has-text-justified"
									style="min-width: 150px; border-right: solid 1px #000 !important; margin: 0px 10px; padding-right: 40px;">
									<b>Variations</b><br> <img
										src="https://agknow.geocledian.com/agknow/api/v3/parcels/8238/variations/landsat8/722136.png?key=911313c3-26a7-d95e-b42e-2c270548a5a6&legend=true"
										title="The dark blue value stands for the minimum NDVI value in the parcel and dark red indicates the maximum NDVI values within the parcel for the measurement date"
										style="width: 100%;"><br> <span
										style="padding-right: 0px; padding-left: 0px;"><label
										style="padding-right: 45px; font-weight: bold;">min</label> <label
										style="padding-left: 0px; font-weight: bold;">max</label></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				</section>			
			<div id="gcKmapId">
				<div class="btnPanel">
					<div id="video-button-controls">
						<button class="btn btn-success" id="startVideoBtn"
							onclick="kmap1.startVideo();">
							<label class="btnLabel"><s:text name="startVideo" /></label>
						</button>
						<button class="btn btn-warning" id="stopVideoBtn"
							onclick="kmap1.stopVideo();">
							<label class="btnLabel"><s:text name="stopVideo" /></label>
						</button>
					</div>
				</div>
				<div class="marginBottom">
					<div id="kmapId1" parcelId="8180" autoPlay="false"
						autoPlayDelay="6" productKey="vitality"
						products="vitality,variations,visible"
						accessToken="911313c3-26a7-d95e-b42e-2c270548a5a6"
						preloadImages="false"
						hostURL="https://agknow.geocledian.com/agknow/api/v3/parcels">
					</div>
				</div>
				</div>
				</div>
				<!-- NDVI Info Tab -->				
				<div id="ndviInfo" class="tab-pane fade">
				<div id="cropPercentileDiv">				
					<table id="cropPercentileTable">
						<th width="25%"><s:text name="sensedDate" /></th>
						<th width="25%"><s:text name="percentileOfCroppedArea" /></th>
						<th width="25%"><s:text name="cpiValueIndexImage" /></th>
						<th width="15%"><s:text name="label.seasonName" /></th>
						<tbody id="cropPercentileTbody"></tbody>
					</table>
				</div>
				<%--  <label><b><s:text name="cpiValueIndex"/></b></label>     --%>
				<div class="hide" id="analysisProgressBar">
					<label for="tableLoad"><s:text
							name="analysingTxt" /></label>
					<progress id="tableLoad" max="100"></progress>
				</div>
				<div id="chartApi1"  class="hide chartCenter">
				<div id="gc-app">
				<gc-cropstatus chartid="cropstatus1"
					gc-apikey="911313c3-26a7-d95e-b42e-2c270548a5a6"
					gc-host="agknow.geocledian.com" gc-parcel-id="8318"
					sdate="2018-10-09" mode="donut"></gc-cropstatus>
			</div>
			</div>
			</div>
			<div id="similarityInfo" class="tab-pane fade similarityFormat">
				<div id="cropPercentileDiv">
					<p class="similarityFormat">
						<label class="similarityFormat"><s:text name="similarityValue.txt" /></label></br>
						<label id="similarityValue" class="similarityFormat"></label></b>
					</p>
				</div>
			</div>
			</div>
			</div>
			<!-- GeoCledian API Integration For Body Code end -->

			<script
				src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap&libraries=geometry,drawing,places"></script>
			<script
				src="plugins/mapNvdiAPIs/kmaps-gl_v3/testpage/js/scripts-sync.js"></script>
			<script src="plugins/mapNvdiAPIs/geocledianApi.js"
				type="text/javascript"></script></body>        
</html>
