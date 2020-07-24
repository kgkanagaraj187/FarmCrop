<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
	<script src="plugins/openlayers/OpenLayers.js"></script>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
</head>

<body>
		<script type="text/javascript">
	var branchId='<s:property value="branchId"/>';
	jQuery(document).ready(function(){
		var colNames='<s:property value="mainGridCols"/>';
		
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.updateView();
	     $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		
		$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
		loadGrid();
		 loadCustomPopup();

	});
	var data = new Array();
	var $overlay;
	var $modal;
	var $map;
	var $slider;
	var url = window.location.href;
	var temp = url;
	for(var i = 0 ; i < 1 ; i++) {
	temp = temp.substring(0, temp.lastIndexOf('/'));
	}
	var href = temp;
	var coordinateImg = "red_placemarker.png";
	var iconImage = temp + '/img/' + coordinateImg;
	
	function loadGrid(){
		
		var gridColumnNames = new Array();
		var gridColumnModels = new Array();
		
		var colNames='<s:property value="mainGridCols"/>';
		
		var d1=	jQuery('#daterange').val();
		
		var d2= d1.split("-");

		var value1= d2[0];
		
		var value2= d2[1];

	document.getElementById("hiddenStartDate").value=value1;

	document.getElementById("hiddenEndDate").value=value2;

		var startDate=new Date(document.getElementById("hiddenStartDate").value);
		var endDate=new Date(document.getElementById("hiddenEndDate").value);
		if (startDate>endDate){
			alert('<s:text name="date.range"/>');
		}
		
		$(colNames.split("%")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					gridColumnNames.push(cols[0]);
					gridColumnModels.push({name: cols[0],width:cols[1],sortable: false,align:cols[2],frozen:true});
				}
		});
		
		jQuery("#detail").jqGrid(
				{
					url:'paymentReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',	
				   	postData:{				 
						  "startDate" : function(){
							  return  document.getElementById("hiddenStartDate").value;
						  },
						  "endDate" : function(){
							  return document.getElementById("hiddenEndDate").value;
						  },
						 
						},	
				   	colNames:gridColumnNames,
				   	colModel:gridColumnModels,
				   	height: 301, 
				   	width: $("#baseDiv").width(),
				   	scrollOffset: 0,     
				   	sortname:'id',
				   	sortorder: "desc",
				   	rowNum:10,
				   	rowList : [10,25,50],
				   	viewrecords: true,  	
				   	subGrid: false,
				   	subGridOptions: {
					   "plusicon" : "glyphicon-plus-sign",
					   "minusicon" : "glyphicon-minus-sign",
					   "openicon" : "glyphicon-hand-right",
				   	},
				   	subGridRowExpanded: function(subgrid_id, row_id){
					   var subgrid_table_id, pager_id; 
					   subgrid_table_id = subgrid_id+"_t"; 
					   pager_id = "p_"+subgrid_table_id; 
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table>"); //<div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid({
						   url:'paymentReport_subGridDetail.action?id='+row_id,
						   pager: pager_id,
						   datatype: "json",	
						   colNames:subGridColumnNames,
						   colModel:subGridColumnModels,
						   scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    autowidth: true,
						    viewrecords: true
					   });
					   	jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				   },				
				
		        onSortCol: function (index, idxcol, sortorder) {
			        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
		                    && this.p.colModel[this.p.lastsort].sortable !== false) {
		                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
		            }
		        }
				});
				
				//jQuery("#detail").jqGrid('setFrozenColumns');
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
		
			    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
				jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
		
	}
	
	 function exportXLS(){
		if(isDataAvailable("#detail")){
			
			$("#form select").each(function(){
				var value = $(this).val();
				var name=this.name;
				if(!isEmpty(value)){
					data.push({name:name,value:value});
				}
			});
			var json = JSON.stringify(data);
			var dataa={
					filterList:json
			};
			
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'paymentReport_populateXLS.action?',postData:dataa});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	} 	
	
	
	function search(){
		data = new Array();
var colNames='<s:property value="mainGridCols"/>';
		
		var d1=	jQuery('#daterange').val();
		
		var d2= d1.split("-");

		var value1= d2[0];
		
		var value2= d2[1];

	document.getElementById("hiddenStartDate").value=value1;

	document.getElementById("hiddenEndDate").value=value2;

		var startDate=new Date(document.getElementById("hiddenStartDate").value);
		var endDate=new Date(document.getElementById("hiddenEndDate").value);
		if (startDate>endDate){
			alert('<s:text name="date.range"/>');
		}
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			if(!isEmpty(value)){
				data.push({name:name,value:value});
			}
		});
		var json = JSON.stringify(data);
		var dataa={
				filterList:json,
				startDate:document.getElementById("hiddenStartDate").value,
				endDate:document.getElementById("hiddenEndDate").value
		};
		
		jQuery("#detail").jqGrid('setGridParam',{url:'paymentReport_detail.action',page:1,postData:dataa}).trigger('reloadGrid');				
		}	
	
	 

	function loadCustomPopup() {
		$overlay = $('<div id="modOverlay"></div>');
		$modal = $('<div id="modalWin" class="ui-body-c"></div>');
		$slider = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
		$close = $('<button id="clsBtn" class="btnCls">X</button>');

		$modal.append($slider, $close);
		$('body').append($overlay);
		$('body').append($modal);
		$modal.hide();
		$overlay.hide();

		jQuery("#clsBtn").click(function() {
			$('#modalWin').css('margin-top', '-230px');
			$modal.hide();
			$overlay.hide();
			$('body').css('overflow', 'visible');
		});
	}
	function enablePopup(head, cont) {

		$(window).scrollTop(0);
		$('body').css('overflow', 'hidden');
		$(".bjqs").empty();
		var heading = '';
		var contentWidth = '100%';
		if (head != '') {
			heading += '<div style="height:10%;"><p class="bjqs-caption">'
					+ head + '</p></div>';
			contentWidth = '92%';
		}
		var content = "<div style='width:100%;height:" + contentWidth
				+ "'>" + cont + "</div>";
		$(".bjqs").append('<li>' + heading + content + '</li>')
		$(".bjqs-controls").css({
			'display' : 'block'
		});
		$('#modalWin').css('margin-top', '-260px');
		$modal.show();
		$overlay.show();
		$('#banner-fade').bjqs({
			height : 450,
			width : 600,
			showmarkers : false,
			usecaptions : true,
			automatic : true,
			nexttext : '',
			prevtext : '',
			hoverpause : false

		});

	}

	function showMap(latitude, longitude) {
		//String[] a = latLon.split(",");
		try {
			var heading = '<s:text name="coordinates"/>:' + latitude + ","
					+ longitude;
			var content = "<div id='map' class='smallmap'></div>";

			enablePopup(heading, content);

			loadLatLonMap('map', latitude, longitude, "");
		} catch (e) {
			console.log(e);
		}
	}

	function loadLatLonMap(mapDiv, latitude, longitude, landArea) {
		var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
		var tProjection = new OpenLayers.Projection("EPSG:900913");
		try {
			jQuery("#map").css("height", "400px");
			jQuery("#map").css("width",
					(jQuery(".rightColumnContainer").width()));
			jQuery("#" + mapDiv).html("");
			var mapObject = new OpenLayers.Map(mapDiv, {
				controls : []
			});
			var googleLayer2 = new OpenLayers.Layer.OSM();
			mapObject.addLayer(googleLayer2);
			mapObject.addControls([ new OpenLayers.Control.Navigation(),
					new OpenLayers.Control.Attribution(),
					new OpenLayers.Control.PanZoomBar(), ]);
			// new OpenLayers.Control.LayerSwitcher()]);
			mapObject.zoomToMaxExtent();
			var setCenter = true;
			// alert(latitude+"===="+longitude);
			if (latitude != null && latitude != '' && longitude != null
					&& longitude != '' && longitude != 0 && latitude != 0
					&& landArea.length <= 0) {
				mapObject.setCenter(new OpenLayers.LonLat(longitude,
						latitude).transform(fProjection, tProjection), 17,
						false, false);
				setCenter = false;
				var markers = new OpenLayers.Layer.Markers("Markers");
				var size = new OpenLayers.Size(21, 25);
				var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
				var icon = new OpenLayers.Icon(iconImage, size, offset);
				var mark1 = new OpenLayers.Marker(new OpenLayers.LonLat(
						longitude, latitude).transform(fProjection,
						tProjection), icon);
				
				markers.addMarker(mark1);
				mapObject.addLayer(markers);
			}

			if (landArea.length > 0) {

				var polygonList = [], multuPolygonGeometry, multiPolygonFeature;
				var vector = new OpenLayers.Layer.Vector('multiPolygon');
				var pointList = [];
				for (var i = 0; i < landArea.length; i++) {
					var landObj = landArea[i];
					//alert(landObj.lat+"===="+landObj.lon);			
					if (landObj.lat != null && landObj.lat != ''
							&& landObj.lon != null && landObj.lon != ''
							&& landObj.lon != 0 && landObj.lat != 0) {
						var point = new OpenLayers.Geometry.Point(
								landObj.lon, landObj.lat).transform(
								fProjection, tProjection);
						pointList.push(point);
						if (setCenter) {
							mapObject.setCenter(new OpenLayers.LonLat(
									landObj.lon, landObj.lat).transform(
									fProjection, tProjection), 18, false,
									false);
							setCenter = false;
						}
					}
				}
				var linearRing = new OpenLayers.Geometry.LinearRing(
						pointList);
				var polygon = new OpenLayers.Geometry.Polygon(
						[ linearRing ]);
				polygonList.push(polygon);
				multuPolygonGeometry = new OpenLayers.Geometry.MultiPolygon(
						polygonList);
				multiPolygonFeature = new OpenLayers.Feature.Vector(
						multuPolygonGeometry);
				vector.addFeatures(multiPolygonFeature);
				mapObject.addLayer(vector);
			}

			//alert(jQuery("#lftCol").height()+"----"+jQuery("#rgtCol").height());
			//jQuery("#map").css("height",(jQuery("#lftCol").height()-68))

			//jQuery("#map").css("height","400px");
			//jQuery("#map").css("width",(jQuery(".rightColumnContainer").width()));

			//alert(jQuery("#map").height());
		} catch (err) {
			console.log(err);
		}
	}
	
	
</script>


 <style>
	.reportWrap .filterEle {
		float: left;
		cursor: pointer;
		margin-right: 1%;
		margin-left: 1%;
		margin-bottom: 5px;
		box-sizing: border-box;
	}
	.reportWrap .filterEle {
		width: 14%;
		height: auto;
		box-sizing: border-box;
	}
	
	.reportWrap .filterEle label{
		font-weight:bold;
		color:#000;
	}
	</style>
	
	
		<%-- FILTER SECTION --%>
		
		<s:form id="form">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('startingDate')}" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div>
				
					<s:iterator value="reportConfigFilters" status="status">
						<div class='filterEle'>
							<label for="txt"><s:property value='%{getLocaleProperty(label)}'/></label>
							<div class="form-element">
								<s:select name="%{field}" list="options"
									headerKey="" headerValue="%{getText('txt.select')}"
									class="form-control input-sm select2" />
									<s:set var="personName" value="%{method}"/>
							</div>
						</div>
					</s:iterator>
				 <div class='filterEle' style="margin-top: 2%;margin-right: 0%;">
						<button type="button" class="btn btn-success btn-sm" id="generate"
						aria-hidden="true" onclick="search()">
						<b class="fa fa-search"></b>
						</button>
						<button type="button" class="btn btn-danger btn-sm"
						aria-hidden="true" id="reset" onclick="reset()">
						<b class="fa fa-close"></b>
						</button>
					</div> 
				</div>
			</section>
			</div>
		</s:form>
		
		<%-- GRID SECTION --%>
		
		<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
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
						<%-- <li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li> --%>
						 <li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li> 
					</ul>
				</div>
			</div>
		</div>


		<div style="width: 100%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	 <s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
	 <button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
		<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content" id="DivIdToPrint">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body" id="mbody">
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
