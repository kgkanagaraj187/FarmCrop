<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<script src="plugins/openlayers/OpenLayers.js"></script>

<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<STYLE type="text/css">
.ui-autocomplete {
	background: #D3DEB0;
	padding: 10px;
	border: 1px solid #939585;
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden
}
</STYLE>
<style>
.ui-jqgrid-btable td {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-btable td:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid-btable tr {
	border: none !important;
}

.ui-jqgrid-htable th {
	border: none !important;
	border-right: solid 1px #567304 !important;
}

.ui-jqgrid-htable th:last-child { /*border-right:none!important;*/
	
}

.ui-jqgrid .ui-jqgrid-htable TH.ui-th-ltr {
	border-bottom: solid 1px #567304 !important;
}

.ui-jqgrid .ui-jqgrid-btable {
	border-right: none !important;
}

.ui-jqgrid .ui-jqgrid-htable th div {
	padding-right: 0px !important;
}
</style>
</head>
<body>
	<script type="text/javascript">
	jQuery(document).ready(function(){
		onFilterData();
		loadCustomPopup();	
		getFilterData();
		jQuery(".well").hide();		  
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
		 $('.applyBtn').click();
		
		 if(filterDataReport==''){
		//document.getElementById('fieldl').selectedIndex=0;
		 
		document.getElementById('farmerId').selectedIndex=0;
		 }
		jQuery("#detail").jqGrid(
				{
				   	url:'farmerInspectionIcsReport_data.action?',
				   	pager: '#pagerForDetail',				   
				   	datatype: "json",	
				    styleUI : 'Bootstrap',
					postData:{				 
						"startDate" : function(){
		                     return document.getElementById("hiddenStartDate").value;           
		                    },		
		                    "endDate" : function(){
				               return document.getElementById("hiddenEndDate").value;
			                 },
						  "filter.farmerId" : function(){			  
								return document.getElementById("farmerId").value;
						  },
						  "filter.farmId" : function(){			  
								return document.getElementById("farmId").value;
					  	  },
					  	"postdata" :  function(){	
	  	 		   			return  decodeURI(postdataReport);
	  	 				  },
					  	<s:if test='branchId==null'>
						  "branchIdParma" : function(){
							  return document.getElementById("branchIdParma").value;
						  }
						  </s:if>
							<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								  "subBranchIdParma" : function(){
									  return document.getElementById("subBranchIdParam").value;
								  },
								  </s:if>
						},	
					colNames:[	
					          
								<s:if test='branchId==null'>
								'<s:text name="app.branch"/>',
					         	</s:if>
						        <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
								'<s:text name="app.subBranch"/>',
					         	</s:if>
								 <s:if test="farmerCodeEnabled==1">
									'<s:text name="farmerIdCode"/>',
								</s:if>
								'<s:text name="farmerNames"/>',
								<s:if test="currentTenantId=='pratibha'">
								<s:if test="getBranchId()!='bci'">
								'<s:text name="%{getLocaleProperty('tracenetcode')}"/>',
								</s:if>
							   </s:if>
							   <s:if test="currentTenantId!='pratibha'"> 
								'<s:text name="farmIdCode"/>',
								</s:if>
								'<s:text name="%{getLocaleProperty('farmNames')}"/>',
								'<s:text name="answeredDate"/> ',
								'<s:text name="map"/> '
			 	      		 ],
			 	     
				   	colModel:[
				   	          
					           <s:if test='branchId==null'>
						   		{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
						   			value: '<s:property value="parentBranchFilterText"/>',
						   			dataEvents: [ 
						   			          {
						   			            type: "change",
						   			            fn: function () {
						   			            	console.log($(this).val());
						   			             	getSubBranchValues($(this).val())
						   			            }
						   			        }
						   			    ]
						   			
						   			}},	   				   		
						   		</s:if>
						   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						   		</s:if>
						   		<s:if test="farmerCodeEnabled==1">
									{name:'farmerId',index:'farmerId',sortable:false},
								</s:if>
								{name:'farmerName',index:'farmerName',sortable:false},
								<s:if test="currentTenantId=='pratibha'">
								<s:if test="getBranchId()!='bci'">
								{name:'tracenetcode',index:'tracenetcode',sortable:false},
								</s:if>
						     	</s:if>
						    	<s:if test="currentTenantId!='pratibha'"> 
							   {name:'farmId',index:'farmId',sortable:false},
							   </s:if>
								{name:'farmName',index:'farmName',sortable:false},			
								{name:'answeredDate',index:'answeredDate',sortable:false},
								{name:'map',index:'map',sortable:false,align:'center'}
				   			 ],
				   	height: 380,
			 		width: $("#baseDiv").width(),
			 		scrollOffset: 19,
			 		rowNum:10,
			 		rowList : [10,25,50],
			 		shrinkToFit:true,
			 		viewrecords: true,
			 		sortname:'answeredDate',
			 		sortorder: "desc",
			 		 beforeSelectRow: 
					        function(rowid, e) {
					    	var iCol = jQuery.jgrid.getCellIndex(e.target);
					    	var branchId = '<s:property value="getBranchId()"/>';
					            if(branchId==null||branchId==""){
					            	if (iCol ==2 || iCol ==6){return false; }
							            else{ return true; }
					            }else{
					            	 if (iCol ==1 || iCol ==5){return false; }
							            else{ return true; }
					            }
					           
					        },

				   	onSelectRow: function(id){ 
						  document.updateform.id.value  =id;
						  getSelectData();
						  var postdataReport =  JSON.stringify($('#detail').getGridParam('postData'));
						  document.updateform.postdataReport.value =postdataReport;
				          document.updateform.submit();      
						},
					        onSortCol: function (index, idxcol, sortorder) {
						        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
					                    && this.p.colModel[this.p.lastsort].sortable !== false) {
					                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
					            }
					        }
					   });
			colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
		    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#detail")[0].id) +
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
			jQuery("#detail").jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			  groupHeaders:[
			    <s:if test="farmerCodeEnabled==1">
				{startColumnName: 'farmerId', numberOfColumns: 2, titleText: '<s:text name="farmerCropProdAnswers.farmer"/>'},
				</s:if>
				<s:else>
				{startColumnName: 'farmerName', numberOfColumns: 1, titleText: '<s:text name="farmerCropProdAnswers.farmer"/>'},
				</s:else>
				{startColumnName: 'farmId', numberOfColumns: 2, titleText: '<s:text name="farmerCropProdAnswers.farm"/>'}
			  ]
			});
				
			jQuery("#generate").click(function() {
				reloadGrid();	
			});
			
			jQuery("#clear").click(function() {
				clear();

		});	

		function reloadGrid(){
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			//	alert(d1);
			var value1= d2[0];
			//alert(value1);
			var value2= d2[1];
			//alert(value2);
		document.getElementById("hiddenStartDate").value=value1;
		
		document.getElementById("hiddenEndDate").value=value2;
		
			var startDate=new Date(document.getElementById("hiddenStartDate").value);
		//	alert(startDate);
			var endDate=new Date(document.getElementById("hiddenEndDate").value);
		//	alert(endDate);
			if (startDate>endDate){
				alert('<s:text name="date.range"/>');
			}else{
		jQuery("#detail").jqGrid('setGridParam',{url:"farmerInspectionIcsReport_data.action?",page:1})
			.trigger('reloadGrid');	
			
		}
		}

		function clear(){
			resetReportFilter();
			document.form.submit();
		}

		function getQuery(){
			var queryString="startDate="+document.getElementById("startDate").value+
			"&endDate="+document.getElementById("endDate").value;
			return queryString;
		}

		postdataReport='';
	});
	
	function exportXLS() {
	    if (isDataAvailable("#detail")) {
	        jQuery("#detail").setGridParam({postData: {rows: 0}});
	        jQuery("#detail").jqGrid('excelExport', {url: 'farmerInspectionIcsReport_populateXLS.action?'});
	}     else {
	        alert('<s:text name="export.nodata"/>');
	    }
	}

	function onFilterData(){
		callAjaxMethod("farmerInspectionIcsReport_populateFarmerList.action","farmerId");
		callAjaxMethod("farmerInspectionIcsReport_populateFarmList.action","farmId")
	}
	function callAjaxMethod(url,name){
		var cat = $.ajax({
			url: url,
			async: false, 
			type: 'post',
			success: function(result) {
				insertOptions(name,JSON.parse(result));
			}        	

		})
		
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


	function showFarmMap(latitude, longitude){
		
		var heading ='<s:text name="coordinates"/>&nbsp;:&nbsp'+latitude+","+longitude;
		var content="<div id='map' class='smallmap'></div>";
		enablePopup(heading,content);
		//var landArea = latitude, longitude;
		loadLatLonMap('map', latitude, longitude, "");
		//loadMap("farmMap",landArea);
		
	      /*   var farmCoordinate = jQuery("#farmCordinates").val();
	        var landArea = JSON.parse(farmCoordinate); */
	       
	        
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

	<s:form name="form" id="filter" action="farmerInspectionIcsReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:text name="answeredDate" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm " />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="filter.farmerId" id="farmerId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt"><s:text name="farm" /></label>
						<div class="form-element">
							<s:select name="filter.farmId" id="farmId" list="{}"
								headerKey="" headerValue="%{getText('txt.select')}"
								cssClass="form-control input-sm select2" />
						</div>
					</div>

					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParam" id="branchIdParam"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="input-sm form-control select2"
										onchange="populateChildBranch(this.value)" />
								</div>
							</div>
						</s:if>
						<div class="filterEle">
							<label for="txt"><s:text name="app.subBranch" /></label>
							<div class="form-element">
								<s:select name="subBranchIdParam" id="subBranchIdParam"
									list="subBranchesMap" headerKey="" headerValue="Select"
									cssClass="input-sm form-control select2" />
							</div>
						</div>
					</s:if>
					<s:else>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParma" id="branchIdParma"
										list="branchesMap" headerKey=""
										headerValue="%{getText('txt.select')}"
										cssClass="form-control input-sm select2" />
								</div>
							</div>
						</s:if>
					</s:else>
					<div class="filterEle" style="margin-top: 24px;">
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

	</s:form>
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

			<%-- <div class="flexItem text-right flex-right">
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
						<li><a href="#" onclick="exportPDF();" role="tab"
							data-toggle="tab" aria-controls="dropdown1"
							aria-expanded="false"><i class="fa fa-file-pdf-o"></i> <s:text
									name="pdf" /></a></li>
						<li><a href="#" onclick="exportXLS()" role="tab"
							data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
								class="fa fa-table"></i> <s:text name="excel" /></a></li>
					</ul>
				</div>
			</div> --%>
		</div>


		<div style="width:99%;" id="baseDiv">
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
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>

	<s:form name="updateform" action="farmerInspectionIcsReport_detail">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
		<s:hidden name="filterMapReport" id="filterMapReport" />
		<s:hidden name="postdataReport" id="postdataReport" />
	</s:form>
</body>
