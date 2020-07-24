<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>

<script src="plugins/openlayers/OpenLayers.js"></script>

<!-- <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
type="text/javascript"></script> -->

<head>
<meta name="decorator" content="swithlayout">
</head>
<style>
.ui-jqgrid .ui-jqgrid-htable th div {
	height: auto;
	overflow: hidden;
	padding-right: 4px;
	position: relative;
	white-space: normal !important;
}

th.ui-th-column div {
	white-space: normal !important;
	height: auto !important;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	/*height: auto !important;*/
}
</style>
<body>
	<script>
	var tenant = '<s:property value="getCurrentTenantId()"/>';
	$(document).ready(function(){
		jQuery(".well").hide();
		onFilterData();
		loadGrid();
		loadCustomPopup();
		
		$("#dialog").hide();
		
		jQuery("#generate").click( function() {
			jQuery("#detail").jqGrid('setGridParam',{url:'sensitizingReport_data.action',page:1})
			.trigger('reloadGrid');		
		});
		
		
		jQuery("#clear").click( function() {
			window.location.href="sensitizingReport_list.action";
		});	
	});
	
	function reloadGrid(flag){
		jQuery("#detail").jqGrid('setGridParam',{url:'sensitizingReport_data.action',page:1})
		.trigger('reloadGrid');		
	}
	
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
	
		function loadGrid(){
			
			jQuery("#detail").jqGrid(
					{
					   	url:'sensitizingReport_data.action',
					   	pager: '#pagerForDetail',	  
					   	datatype: "json",	
					   	mtype: 'POST',
					   	styleUI : 'Bootstrap',
						postData:{
							"selectedSamithi" : function(){			  
								return  document.getElementById("samithi").value;
							  },
						},
					   	colNames:[
					   		<s:if test='branchId==null'>
					   			'<s:text name="app.branch"/>',
					   	     </s:if>
					   	     <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
							 </s:if>
					  		 '<s:text name="profile.samithi"/>',
					  		<s:if test="currentTenantId!='olivado'">
					  		'<s:property value="%{getLocaleProperty('village')}"/>',
					  		</s:if>
					  		 '<s:property value="%{getLocaleProperty('farmercnt')}"/>',
					  		'<s:text name="remarks"/>',
					  		'<s:text name="farm.map"/>',
					  		'<s:text name="farm.photo"/>',
					  		
					      	 	 ],
					   	colModel:[	
							<s:if test='branchId==null'>
						   		{name:'branchId',index:'branchId',sortable: false,search:false,stype: 'select',searchoptions: {
						   			value: '<s:property value="parentBranchFilterText"/>',
						   			dataEvents: [ 
						   			          {
						   			            type: "change",
						   			            fn: function () {
						   			            	console.log($(this).val());
						   			             	getSubBranchValues($(this).val())
						   			            }
						   			        }]
						   			
						   			}},	   				   		
						   		</s:if>
						   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						   			{name:'subBranchId',index:'subBranchId',sortable: false,search:false,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						   		</s:if>
					   		{name:'samithi',index:'samithi',sortable:false,search:false},
					   		<s:if test="currentTenantId!='olivado'">
					   		{name:'village',index:'village',sortable:false,search:false},
					   		</s:if>
					   		{name:'count',index:'count',sortable:false,align:'right',search:false},
					   		{name:'remarks',index:'remarks',sortable:false,align:'center',search:false},
					   		{name:'map',index:'map',sortable:false,align:'center',search:false},
					   		{name:'photo',index:'photo',sortable:false,align:'center',search:false},
					   			 ],
					   			 
					   	height: 301, 
					    width: $("#baseDiv").width(), // assign parent div width
					    scrollOffset: 1,
					   	rowNum:10,
					   	shrinkToFit:true,
					   	rowList : [10,25,50],
					    sortname:'id',			  
					    sortorder: "desc",
					    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
				        onSortCol: function (index, idxcol, sortorder) {
					        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
				                    && this.p.colModel[this.p.lastsort].sortable !== false) {
				                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
				            }
				        }
					});
					
					jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
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
			
		}
		

		function onFilterData(){
			callAjaxMethod("sensitizingReport_populateGroupList.action","samithi");
		}
		function callAjaxMethod(url,name){
			var cat = $.ajax({
				url: url,
				async: true, 
				type: 'post',
				success: function(result) {
					insertOptions(name,JSON.parse(result));
				}        	

			})
			
		}
		
		function showFarmMap(latitude, longitude){
			try{
			var heading ='<s:text name="coordinates"/>:'+latitude+","+longitude;
			var content="<div id='map' class='smallmap'></div>";
			
			enablePopup(heading,content);
			
			loadMap('map', latitude, longitude,"");
			}catch (e) {
				console.log(e);
			}
		}
		 function loadMap(mapDiv, latitude, longitude, landArea){
             try{
             jQuery("#map").css("height", "400px");
             jQuery("#map").css("width", (jQuery(".rightColumnContainer").width()));
             jQuery("#" + mapDiv).html("");
             var mapObject = new OpenLayers.Map(mapDiv, {controls : []});
             var googleLayer2 = new OpenLayers.Layer.OSM();
             mapObject.addLayer(googleLayer2);
             mapObject.addControls([new OpenLayers.Control.Navigation(), new OpenLayers.Control.Attribution(), new OpenLayers.Control.PanZoomBar(), ]);
             // new OpenLayers.Control.LayerSwitcher()]);
             mapObject.zoomToMaxExtent();
             var setCenter = true;
             // alert(latitude+"===="+longitude);
             if (latitude != null && latitude != '' && longitude != null && longitude != '' && longitude != 0 && latitude != 0 && landArea.length <= 0){

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

             if (landArea.length > 0){

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
		  
		  
		//Variable relate to loading Custom Popup
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
		  
		  function buttonEdcCancel(){
				//refreshPopup();
				document.getElementById("model-close-edu-btn").click();	
		     }
	
		  
		  var imgID = "";
			function popupWindow(ids) {
				
				try{
					var str_array = ids.split(',');
					$("#mbody").empty();
					
					var mbody="";
					
					for(var i = 0; i < str_array.length; i++){
						//<img class="slidesjs-slide" src="sensitizingReport_populateImage.action?id='+str_array[i]+'" slidesjs-index="0"/>');
						
						if(i==0){
							mbody="";
							mbody="<div class='item active'>";
							mbody+='<img src="sensitizingReport_populateImage.action?id='+str_array[i]+'"/>';
							mbody+="</div>";
						}else{
							mbody="";
							mbody="<div class='item'>";
							mbody+='<img src="sensitizingReport_populateImage.action?id='+str_array[i]+'"/>';
							mbody+="</div>";
						}
						$("#mbody").append(mbody);
					 }
					
					//$("#mbody").first().addClass( "active" );
					
					document.getElementById("enableModal").click();	
				}
				catch(e){
					alert(e);
					}
				
			}
			
			function exportXLS(){
				var count=jQuery("#detail").jqGrid('getGridParam', 'records');
				/* if(count>recordLimit){
					 alert('<s:text name="export.limited"/>');
				}
				else */ if(isDataAvailable("#detail")){
					jQuery("#detail").setGridParam ({postData: {rows : 0}});
					jQuery("#detail").jqGrid('excelExport', {url: 'sensitizingReport_populateXLS.action'});
				}else{
				     alert('<s:text name="export.nodata"/>');
				}
			}		
	</script>
	
	<div class="appContentWrapper marginBottom">
			
				<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:property
						value="%{getLocaleProperty('profile.samithi')}" /></label>
					<div class="form-element">
						<s:select name="samithiName" id="samithi" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}" 
					cssClass="form-control select2" />
							
					</div>
				</div>
				<div  class="filterEle" style="margin-top: 2%;margin-right: 0%;">
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
			</section>

	</div>
	
	
	<div class="appContentWrapper marginBottom">
			<div class="flex-layout reportData">
				<!-- <div class="flexItem-2">
					<div class="summaryBlocksWrapper flex-container">
						<div class="report-summaryBlockItem">
							<span><span class="strong">0</span> Farmers&nbsp;<i
								class="fa fa-user"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong">0</span> Cultivation Cost&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong">0</span> Others&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
						<div class="report-summaryBlockItem">
							<span><span class="strong">0</span> Total Cost&nbsp;<i
								class="fa fa-dollar"></i></span>
						</div>
					</div>
				</div> -->
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

						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div>



		</div>

			<div style="width: 99%;" id="baseDiv">
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>
	
		</div>
		
			<button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>

	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mbody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonEdcCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	
</body>
