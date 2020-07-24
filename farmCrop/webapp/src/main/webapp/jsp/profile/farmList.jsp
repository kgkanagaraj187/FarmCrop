'<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<META name="decorator" content="swithlayout">
<style type="text/css">
#farmMap {
	   height:100%;
	   width:100%;
	}
</style>
</head>
<div id='warn' class="error"><s:actionerror /></div>

<script src="plugins/openlayers/OpenLayers.js"></script>
<!-- <script src="http://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script> -->
<script type="text/javascript">
$(document).ready(function()
  {	
	alert("hfgyh");
	loadCustomPopup();
	jQuery("#detail").jqGrid(
		{
			url: 'farm_data.action',
			pager: '#pagerForDetail',
			mtype: 'POST',
			datatype: 'json',
					
			colNames:[

					  '<s:text name="farm.farmCode"/>',
					  '<s:text name="farm.farmName"/>',
					  '<s:text name="farm.farmerName"/>',
					  '<s:text name="farm.photo"/>',
					  '<s:text name="farm.map"/>'
		 			  ],

			colModel:[
			   		{name:'farmCode',index:'farmCode',width:125,sortable:true},
			   		{name:'farmName',index:'farmName',width:125,sortable:true},
			   		{name:'f.firstName',index:'f.firstName',width:125,sortable:true},
			   		{name:'photo',index:'photo',width:125,sortable:false,search:false,align:'center'},
			   		{name:'map',index:'map',width:125,sortable:false,search:false,align:'center'}
				 ],

			height: 255,
			width: $("#baseDiv").width(), 
		   	rowNum:10,
		   	rowList : [10,25,50],
		   	scrollOffset: 0,
		    sortname:'id',			  
		    sortorder: "desc",
		    viewrecords: true,
		    beforeSelectRow: 
		        function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >=3){return false; }
		            else{ return true; }
		        },
		    onSelectRow: function(id){ 
			  document.updateform.id.value=id;
			  document.updateform.id.value=id;
	          document.updateform.submit();      
			},		

			 onSortCol: function (index, idxcol, sortorder) {		      
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		        	        		        	
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();                
	            }
	        }					
		}
		jQuery("#detail").jqGrid('setGridParam',{url:"farm_data.action"}).trigger('reloadGrid');			
		); 

	        jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	        jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

	        


		colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		
		   $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
		       ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
		
		       var cmi = colModel[i], colName = cmi.name;
		       if (cmi.sortable !== false) {
		
		           $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
		       } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
		
		           $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
		       }
		   });	 
	
 });

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
var iconImage = temp + '/img/'+coordinateImg;

// Variable relate to loading Custom Popup
var $overlay;
var $modal;
var $slider;

function loadCustomPopup(){
	$overlay = $('<div id="modOverlay"></div>');
	$modal = $('<div id="modalWin" class="ui-body-c"></div>');
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
	$('#modalWin').css('margin-top','-260px');
	$modal.show();
	$overlay.show();
	$('#banner-fade').bjqs({
        height      : 450,
        width       : 600,
        showmarkers : false,
        usecaptions : true,
        automatic : true,
        nexttext :'',
        prevtext :'',
        hoverpause : false                                           

    });
}

function loadMap(mapDiv,latitude,longitude,landArea){
	jQuery("#"+mapDiv).html("");
	var mapObject = new OpenLayers.Map(mapDiv, {
		controls : []});
	var googleLayer2= new OpenLayers.Layer.OSM();
    );	  
    
    mapObject.addLayer(googleLayer2);
    mapObject.addControls([new OpenLayers.Control.Navigation(),
 	   				new OpenLayers.Control.Attribution(),
 	   				new OpenLayers.Control.PanZoomBar(),]);
 	   				// new OpenLayers.Control.LayerSwitcher()]);
	mapObject.zoomToMaxExtent();
	var setCenter=true;
	if(latitude!=null && latitude!='' && longitude!=null && longitude!='' && longitude!=0 && latitude!=0){
		mapObject.setCenter(new OpenLayers.LonLat(longitude, latitude).transform(
			fProjection, tProjection), 13, false, false);
		setCenter=false;
		
		var markers = new OpenLayers.Layer.Markers("Markers");
		var size = new OpenLayers.Size(21, 25);
		var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
		var icon = new OpenLayers.Icon(iconImage,size, offset);
		var mark1 = new OpenLayers.Marker(new OpenLayers.LonLat(longitude,latitude).transform(
     				fProjection, tProjection), icon);
		markers.addMarker(mark1);
		mapObject.addLayer(markers);

	}

	if(landArea.length>0){
		var polygonList = [],   multuPolygonGeometry,     multiPolygonFeature; 
		var vector = new OpenLayers.Layer.Vector('multiPolygon');
		var pointList = [];
		for(var i=0;i<landArea.length;i++){
			var landObj=landArea[i];
		    if(landObj.lat!=null && landObj.lat!='' && landObj.lon!=null && landObj.lon!='' && landObj.lon!=0 && landObj.lat!=0){
		    	 var point = new OpenLayers.Geometry.Point(landObj.lon, landObj.lat);
			        point.transform(fProjection, tProjection);
			        pointList.push(point);
			        if(setCenter){
			        	mapObject.setCenter(new OpenLayers.LonLat(landObj.lon, landObj.lat).transform(
			        			fProjection, tProjection), 13, false, false);
			        	setCenter=false;
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

	//alert(jQuery("#lftCol").height()+"----"+jQuery("#rgtCol").height());
	jQuery("#map").css("height",(jQuery("#lftCol").height()-68))
	//alert(jQuery("#map").height());
}

function showFarmPhoto(farmId,farmCode,farmName,farmPhotoCaptureTime){		
	
	var heading='<s:text name="farm.code"/>&nbsp;:&nbsp'+farmCode;
	if(farmPhotoCaptureTime!=''){
		heading+='&nbsp;&nbsp;<s:text name="farm.photoCapturedTime"/>&nbsp;:&nbsp;'+farmPhotoCaptureTime;
	}
	var content="<img src='farm_getFarmPhoto.action?id="+farmId+"&dt="+new Date()+"' width='100%' height='100%'/>";
	enablePopup(heading,content);
}

function showFarmMap(farmCode,farmName,farmLatitude, farmLongitude , landArea){
	var heading ='<s:text name="farm.code"/>&nbsp;:&nbsp'+farmCode+'&nbsp;&nbsp;<s:text name="farm.latitude"/>&nbsp;:&nbsp;'+farmLatitude+'&nbsp;&nbsp;<s:text name="farm.longitude"/>&nbsp;:&nbsp;'+farmLongitude; 
	var content="<div id='farmMap' class='smallmap'></div>";
	enablePopup(heading,content);
	loadMap("farmMap",farmLatitude,farmLongitude,landArea);
}
       
</script>

<sec:authorize ifAllGranted="profile.farmer.create">
	<input type="BUTTON" id="add" value="<s:text name="add.button"/>"
		onclick="document.createform.submit()" />
</sec:authorize>

<div style="width: 99%;" id="baseDiv">
<table id="detail"></table>
<div id="pagerForDetail"></div>
</div>

<s:form name="createform" action="farm_create">
</s:form>

<s:form name="redirectform" action="farmer_create">
</s:form>

<s:form name="updateform" action="farm_detail">
	<s:hidden key="id" />
	<s:hidden key="currentPage" />
</s:form>
