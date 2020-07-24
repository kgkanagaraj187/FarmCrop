<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

<head>
<meta name="decorator" content="swithlayout">

<style type="text/css">
#map {
	height: 100%;
	width: 100%;
}
</style>
<script src="assets/common/openlayers/OpenLayers.js"></script>
<script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>
</head>
<body>
	

	<script>
	

		$(document)
				.ready(
						function() {
							
							loadCustomPopup();
							audioControlHide();
							loadGrid();
							jQuery(".well").hide();
							//document.getElementById('fieldl').selectedIndex = 0;
							$("#daterange").data().daterangepicker.startDate = moment(
									document.getElementById("hiddenStartDate").value,
									"MM-DD-YYYY");
							$("#daterange").data().daterangepicker.endDate = moment(
									document.getElementById("hiddenEndDate").value,
									"MM-DD-YYYY");
							$("#daterange").data().daterangepicker.updateView();
							$("#daterange").data().daterangepicker
									.updateCalendars();
							$('.applyBtn').click();

							

							document.getElementById('farmerId').selectedIndex = 0;
							document.getElementById('researchStationId').selectedIndex = 0;
							document.getElementById('elitType').selectedIndex = 0;
							document.getElementById('cowId').selectedIndex = 0;
							$('#daterange').val()

						});

		var recordLimit = '<s:property value="exportLimit"/>';

		function loadGrid() {

			jQuery("#detail")
					.jqGrid(
							{
								url : 'cowInspectionReport_data.action',
								pager : '#pagerForDetail',
								datatype : "json",
								mtype : 'POST',
								styleUI : 'Bootstrap',
								postData : {

									"startDate" : function() {
										return document
												.getElementById("hiddenStartDate").value;
									},
									"endDate" : function() {
										return document
												.getElementById("hiddenEndDate").value;
									},

									"filter.cowId" : function() {
										return document.getElementById("cowId").value;
									},
									"filter.farmerId" : function() {
										return document
												.getElementById("farmerId").value;
									},
									"filter.researchStation.id" : function() {
										return document
												.getElementById("researchStationId").value;
									},
									"filter.elitType" : function() {
										return document
												.getElementById("elitType").value;
									},

								},
								colNames : [
										'<s:text name="currentInspDate"/>',
										'<s:text name="lastInspDate"/>',
										'<s:text name="cow.cowId"/>',
										'<s:text name="cow.researchStationName"/>',
										'<s:text name="farmerId"/>',
										'<s:text name="farmId"/>',
										'<s:text name="inspectionNo"/>',
										'<s:text name="intervalDays"/>',
										'<s:text name="isMilkingCow"/>',
										 '<s:text name="map"/>',	
										'<s:text name="audioFile"/>',

								],
								datatype : "json",
								colModel : [ {
									name : 'currentInspDate',
									index : 'currentInspDate',
									width : 200,
									sortable : false
								}, {
									name : 'lastInspDate',
									index : 'lastInspDate',
									width : 200,
									sortable : false
								}, {
									name : 'cowId',
									index : 'cowId',
									width : 200,
									sortable : false
								}, {
									name : 'farmerId',
									index : 'farmerId',
									width : 150,
									sortable : false
								}, {
									name : 'farmId',
									index : 'farmId',
									width : 150,
									sortable : false
								}, {
									name : 'researchStationName',
									index : 'researchStationName',
									width : 200,
									sortable : false
								}, {
									name : 'inspectionNo',
									index : 'inspectionNo',
									width : 150,
									sortable : false
								}, {
									name : 'intervalDays',
									index : 'intervalDays',
									width : 150,
									sortable : false
								}, 
								
								{
									name : 'isMilkingCow',
									index : 'isMilkingCow',
									width : 150,
									sortable : false
								}, 
								{
									name:'map',
									index:'map',
									width:125,
									sortable:false,
									search:false,
									align:'center'
									},

								{
									name : 'audioFile',
									index : 'audioFile',
									width : 150,
									sortable : false
								},

								],
								height : 301,
								width : $("#baseDiv").width(),
								autowidth : true,
								shrinkToFit : false,
								scrollOffset : 0,
								sortname : 'id',
								sortorder : "asc",
								rowNum : 10,
								rowList : [ 10, 25, 50 ],
								viewrecords : true,
								beforeSelectRow : function(rowid, e) {
									var iCol = jQuery.jgrid
											.getCellIndex(e.target);
									if (iCol > 8) {
										return false;
									} else {
										return true;
									}
								},
								onSelectRow : function(id) {
									document.detailForm.id.value = id;
									document.detailForm.submit();
								},

								onSortCol : function(index, idxcol, sortorder) {
									if (this.p.lastsort >= 0
											&& this.p.lastsort !== idxcol
											&& this.p.colModel[this.p.lastsort].sortable !== false) {
										$(this.grid.headers[this.p.lastsort].el)
												.find(
														">div.ui-jqgrid-sortable>span.s-ico")
												.show();
									}
								}
								
								

							});

			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
			$(
					'#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id)
							+ ' tr.ui-jqgrid-labels th.ui-th-column').each(
					function(i) {
						var cmi = colModel[i], colName = cmi.name;

						if (cmi.sortable !== false) {
							$(this).find('>div.ui-jqgrid-sortable>span.s-ico')
									.show();
						} else if (!cmi.sortable && colName !== 'rn'
								&& colName !== 'cb' && colName !== 'subgrid') {
							$(this).find('>div.ui-jqgrid-sortable').css({
								cursor : 'default'
							});
						}
					});

			jQuery("#detail").jqGrid('navGrid', '#pagerForDetail', {
				edit : false,
				add : false,
				del : false,
				search : false,
				refresh : false
			})

			jQuery("#generate").click(function() {
				reloadGrid();
			});

			jQuery("#clear").click(function() {
				clear();
			});

			function reloadGrid() {
				var d1 = jQuery('#daterange').val();
				var d2 = d1.split("-");
				//	alert(d1);
				var value1 = d2[0];
				//alert(value1);
				var value2 = d2[1];
				//alert(value2);
				document.getElementById("hiddenStartDate").value = value1;

				document.getElementById("hiddenEndDate").value = value2;

				var startDate = new Date(document
						.getElementById("hiddenStartDate").value);
				//	alert(startDate);
				var endDate = new Date(
						document.getElementById("hiddenEndDate").value);
				//	alert(endDate);
				if (startDate > endDate) {
					alert('<s:text name="date.range"/>');
				} else {
					jQuery("#detail").jqGrid('setGridParam', {
						url : "cowInspectionReport_data.action?",
						page : 1
					}).trigger('reloadGrid');
				}
			}
			function clear() {
				resetReportFilter();
				document.form.submit();
			}

		}

		function downloadAudioFile(id) {

			document.getElementById("loadId").value = id;
			$('#audioFileDownload').submit();
		}

		function playAudioFiles(id, inspectionDate, cowId, farmerIdName) {
			audioControlShow();
			//<![CDATA[

			$("#jquery_jplayer_1").jPlayer("destroy");

			$("#inspectionDate").html(inspectionDate);
			$("#farmerData").html(cowId);
			$("#cowData").html(cowId);

			$("#jquery_jplayer_1")
					.jPlayer(
							{
								ready : function() {
									$(this)
											.jPlayer(
													"setMedia",
													{
														mp3 : "cowInspectionReport_populateAudioPlay?id="
																+ id
																+ "&dt="
																+ new Date()
													}).jPlayer("play");
								},
								swfPath : "js/jplayer",
								solution : "html,flash",
								supplied : "mp3",
								wmode : "window",
								useStateClassSkin : true,
								autoBlur : true,
								smoothPlayBar : true,
								keyEnabled : true,
								remainingDuration : true,
								toggleDuration : true
							});
			//]]>
		}

		function audioControlShow() {
			jQuery("#audioDiv").show();
			jQuery("#audioDataDiv").show();
		}
		function audioControlHide() {
			$("#jquery_jplayer_1").jPlayer("stop");
			jQuery("#audioDiv").hide();
			jQuery("#audioDataDiv").hide();
		}

		function exportXLS() {
			var count = jQuery("#detail").jqGrid('getGridParam', 'records');

			if (count > recordLimit) {
				alert('<s:text name="export.limited"/>');
			} else if (isDataAvailable("#detail")) {
				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'cowInspectionReport_populateXLS.action'
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}

		function exportPDF() {
			var count = jQuery("#detail").jqGrid('getGridParam', 'records');
			if (count > recordLimit) {
				alert('<s:text name="export.limited"/>');
			} else if (isDataAvailable("#detail")) {
				jQuery("#detail").setGridParam({
					postData : {
						rows : 0
					}
				});
				jQuery("#detail").jqGrid('excelExport', {
					url : 'cowInspectionReport_populatePDF.action'
				});
			} else {
				alert('<s:text name="export.nodata"/>');
			}
		}
		
	</script>
	
	<script src="plugins/openlayers/OpenLayers.js"></script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
		type="text/javascript"></script>
	<script type="text/javascript">
//Variables relate to loading MAP
var fProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var tProjection   = new OpenLayers.Projection("EPSG:900913");
var url = window.location.href;
var temp = url;
for(var i = 0 ; i < 1 ; i++) {
	temp = temp.substring(0, temp.lastIndexOf('/'));
	}
var href = temp;
var coordinateImg = "red_placemarker.png";
var iconImage = temp + '/img/' + coordinateImg;

// Variable relate to loading Custom Popup
var $overlay;
var $modal;
var $slider;

function loadCustomPopup(){
	$overlay = $('<div id="modOverlay"></div>');
	$modal = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
	$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
	$close = $('<button id="clsBtn" class="btnCls">X</button>');
	
	$modal.append($slider, $close);
	$('body').append($overlay);
	$('body').append($modal);
	$modal.hide();
	$overlay.hide();

	jQuery("#clsBtn").click(function () {
    	$('#modalWin').css('margin-top','-230px');	
		$modal.hide();
		$overlay.hide();			
		$('body').css('overflow','visible');
	});
}


function enablePopup(head, cont){
	$(window).scrollTop(0); 
	$('body').css('overflow','hidden');
	$(".bjqs").empty();		
	var heading='';
	var contentWidth='100%';
	if(head!=''){
		heading+='<div style="height:8%;"><p class="bjqs-caption">'+head+'</p></div>';
		contentWidth='92%';
	}
	

	var content="<div style='width:100%;height:"+contentWidth+"'>"+cont+"</div>";	
	$(".bjqs").append('<li>'+heading+content+'</li>')
	$(".bjqs-controls").css({'display':'block'});
	$('#modalWin').css('margin-top','-200px');
	$modal.show();
	$overlay.show();
	$('#banner-fade').bjqs({
        height      : 482,
        width       : 600,
        showmarkers : false,
        usecaptions : true,
        automatic : true,
        nexttext :'',
        prevtext :'',
        hoverpause : false                                           

    });
	
}



function showCowMap(cowId,latitude, longitude){
	
	var heading ='<s:text name="cow.cowId"/>&nbsp;:&nbsp'+cowId;
	var content="<div id='map' class='smallmap'></div>";
	enablePopup(heading,content);
	 loadMap('map', latitude, longitude);
	//loadMap("farmMap",landArea);
	
      /*   var farmCoordinate = jQuery("#farmCordinates").val();
        var landArea = JSON.parse(farmCoordinate); */
       
        
}
  function loadMap(mapDiv, latitude, longitude){
      try{
      jQuery("#map").css("height", "440px");
      jQuery("#map").css("width", (jQuery(".rightColumnContainer").width()));
      jQuery("#" + mapDiv).html("");
      var mapObject = new OpenLayers.Map(mapDiv, {controls : []});
      var googleLayer2 = new OpenLayers.Layer.Google("Google Hybrid", {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 30});
      mapObject.addLayer(googleLayer2);
      mapObject.addControls([new OpenLayers.Control.Navigation(), new OpenLayers.Control.Attribution(), new OpenLayers.Control.PanZoomBar(), ]);
      // new OpenLayers.Control.LayerSwitcher()]);
      mapObject.zoomToMaxExtent();
      var setCenter = true;
      // alert(latitude+"===="+longitude);
      if (latitude != null && latitude != '' && longitude != null && longitude != '' && longitude != 0 && latitude != 0){

      mapObject.setCenter(new OpenLayers.LonLat(longitude, latitude).transform(fProjection, tProjection), 17, false, false);
      setCenter = false;
      var markers = new OpenLayers.Layer.Markers("Markers");
      var size = new OpenLayers.Size(21, 25);
      var offset = new OpenLayers.Pixel( - (size.w / 2), - size.h);
      var icon = new OpenLayers.Icon(iconImage, size, offset);
      var mark1 = new OpenLayers.Marker(new OpenLayers.LonLat(longitude, latitude).transform(fProjection, tProjection), icon);
      markers.addMarker(mark1);
      mapObject.addLayer(markers);

      }

/*           if (landArea.length > 0){

      var polygonList = [], multuPolygonGeometry, multiPolygonFeature;
      var vector = new OpenLayers.Layer.Vector('multiPolygon');
      var pointList = [];
      for (var i = 0; i < landArea.length; i++){
      var landObj = landArea[i];
      //alert(landObj.lat+"===="+landObj.lon);			
      if (landObj.lat != null && landObj.lat != '' && landObj.lon != null && landObj.lon != '' && landObj.lon != 0 && landObj.lat != 0){
      var point = new OpenLayers.Geometry.Point(landObj.lon, landObj.lat).transform(fProjection, tProjection);
      pointList.push(point);
      if (setCenter){
      mapObject.setCenter(new OpenLayers.LonLat(landObj.lon, landObj.lat).transform(fProjection, tProjection), 18, false, false);
      setCenter = false;
      }
      }
      }
      var linearRing = new OpenLayers.Geometry.LinearRing(pointList);
      var polygon = new OpenLayers.Geometry.Polygon([linearRing]);
      polygonList.push(polygon);
      multuPolygonGeometry = new OpenLayers.Geometry.MultiPolygon(polygonList);
      multiPolygonFeature = new OpenLayers.Feature.Vector(multuPolygonGeometry);
      vector.addFeatures(multiPolygonFeature);
      mapObject.addLayer(vector);
      }
*/
      //alert(jQuery("#lftCol").height()+"----"+jQuery("#rgtCol").height());
      //jQuery("#map").css("height",(jQuery("#lftCol").height()-68))

      //jQuery("#map").css("height","400px");
      //jQuery("#map").css("width",(jQuery(".rightColumnContainer").width()));

      //alert(jQuery("#map").height());
      }
      catch (err){
      console.log(err);
      }
  }
</script>



<div class="appContentWrapper marginBottom">
			<div class="reportFilterWrapper filterControls">
					
				<div class="reportFilterItem">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input
								id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm" style="width: 150px" />
					</div>
				</div>
				
			<div class="reportFilterItem">
					<label for="txt"><s:text name="cow.cowId" /></label>
					<div class="form-element">
						<s:select name="filter.cowId" id="cowId" listKey="key"
								listValue="value" list="cowList" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" cssStyle="width:150px" />
					</div>
				</div>
			
			<div class="reportFilterItem">
					<label for="txt"><s:text name="farmerId" /></label>
					<div class="form-element">
						<s:select name="filter.farmerId" id="farmerId" list="farmerList"
								headerKey="" headerValue="%{getText('txt.select')}"
								listKey="key" listValue="value"
								cssClass="form-control input-sm select2" cssStyle="width:150px" />
					</div>
			</div>
				
			<div class="reportFilterItem">
					<label for="txt"><s:text name="cow.researchStationName" /></label>
					<div class="form-element">
						<s:select name="researchStationId" id="researchStationId"
								list="researchStationList" headerKey=""
								headerValue="%{getText('txt.select')}" listKey="key"
								listValue="value" cssClass="form-control input-sm select2"
								cssStyle="width:150px" />
					</div>
			</div>
			
			<div class="reportFilterItem">
					<label for="txt"><s:text name="elitType" /></label>
					<div class="form-element">
						<s:select name="filter.elitType" id="elitType"
								list="elitTypeList" key="key" value="value" headerKey=""
								headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" cssStyle="width:150px" />
					</div>
			</div>
			
			
				<div class="reportFilterItem" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
			</div>
	</div>

<div class="flexItem text-right flex-right">
				<div class="dropdown">
					<button id="dLabel" class="btn btn-primary btn-sts smallBtn"
						type="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						<i class="fa fa-share"></i>
						<s:text name="export" />
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right"
						aria-labelledby="myTabDrop1" id="myTabDrop1-contents">
					<%-- 	<li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li> --%>
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>
			
		<div id="audioDataDiv"
			style="width: 700px; margin-top: 5px; border: 0px solid #cccccc;">
			<table>
				<tr>
					<td width="45%"><b><s:text name="inspectionDate" /></b></td>
					<td><span id="inspectionDate">--</span></td>
				</tr>
				</br>
				<tr>
					<td width="45%"><b><s:text name="cow.cowId" /></b></td>
					<td><span id="farmerData">--</span></td>
				</tr>

			</table>
		</div>


		<div>
			<div id="audioDiv">
				<div style="width: 45%;">
					<div id="jquery_jplayer_1" class="jp-jplayer"></div>
					<div id="jp_container_1" class="jp-audio" role="application"
						aria-label="media player">
						<div class="jp-type-single">
							<div class="jp-gui jp-interface">
								<div class="jp-controls">
									<button class="jp-play" role="button" tabindex="0">play</button>
									<button class="jp-stop" role="button" tabindex="0">stop</button>
								</div>
								<div class="jp-progress">
									<div class="jp-seek-bar">
										<div class="jp-play-bar"></div>
									</div>
								</div>
								<div class="jp-volume-controls">
									<button class="jp-mute" role="button" tabindex="0">mute</button>
									<button class="jp-volume-max" role="button" tabindex="0">max
										volume</button>
									<div class="jp-volume-bar">
										<div class="jp-volume-bar-value"></div>
									</div>
								</div>
								<div style="left: 456px; position: absolute; top: 0px;">
									<button type="button" class="fa fa-times"
										onclick="audioControlHide();" />
								</div>
								<div class="jp-time-holder">
									<div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
									<div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
									<div class="jp-toggles">
										<button class="jp-repeat" role="button" tabindex="0">repeat</button>
									</div>
								</div>
							</div>
							<div class="jp-details hide">
								<div class="jp-title" aria-label="title">&nbsp;</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div style="width: 99%; margin-top: 50px;" id="baseDiv">

			<table id="detail"></table>
			<div id="pagerForDetail"></div>
		</div>
		<div></div>
		<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
		<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
		<s:hidden name="exportLimit" id="exportLimit"></s:hidden>
	<s:form name="detailForm" action="cowInspectionReport_detail">
		<s:hidden name="id" />
	</s:form>

	<s:form id="audioFileDownload"
		action="cowInspectionReport_populateDownload">
		<s:hidden id="loadId" name="id" />
	</s:form>
</body>
