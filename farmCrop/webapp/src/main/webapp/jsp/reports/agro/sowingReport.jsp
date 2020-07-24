<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


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
	<script type="text/javascript">
	var tenant='<s:property value="getCurrentTenantId()"/>';
	if(tenant=='chetna')
		{
		$(".icsName").show();
		}
	jQuery(document).ready(function(){
		loadCustomPopup();
		loadFields();
		jQuery(".well").hide();
		jQuery("#detail").jqGrid(
				{
					url:'sowingReport_detail.action',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				    styleUI : 'Bootstrap',
				   	mtype: 'POST',				   	
				   	postData:{				 
				   	 <s:if test='branchId==null'>
	 	 			    "branchIdParma" : function(){			  
		 					  return document.getElementById("branchIdParma").value;
		 	 			      } ,
	 	 	 			      </s:if>
		 	 			      
				   	  <s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
				      	"subBranchIdParma" : function(){
				    	  return document.getElementById("subBranchIdParam").value;
						},
					 </s:if>
				   		"farmerName":function(){
				               return document.getElementById("farmerNameId").value;
			                 },
			              
			            	   		"icsName" : function(){
									        return document.getElementById("icsName").value;
								},
			            "cropId":function(){
					               return document.getElementById("cropId").value;
				                 },
				        "samithi":function(){
						               return document.getElementById("samithiId").value;
					                 },
					     "season":function(){
							               return document.getElementById("seasonId").value;
						                 },
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
							/* <s:if test="farmerCodeEnabled==1">
							  '<s:text name="farmerCode"/>',
							 </s:if> */
				  		   	  '<s:property value="%{getLocaleProperty('farmer')}" />',
				  		    '<s:property value="%{getLocaleProperty('samithi')}" />',
				  		  '<s:property value="%{getLocaleProperty('village')}" /> ',
				  		    <s:if test="currentTenantId=='chetna'">
				 			 '<s:text name="icsName"/>',
				 			 </s:if>
				 			<s:if test="currentTenantId!='ecoagri'">
				  		      '<s:property value="%{getLocaleProperty('totalcultivationArea')}" /> (<s:property value="%{getAreaType().toUpperCase()}" />)',
				  		    </s:if>
				  		      <s:if test="currentTenantId=='pratibha'">
				  		  '<s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild.quintals')}" />', 
				  		      </s:if>
				  		<s:elseif test="currentTenantId!='ecoagri'">				  		  			
				  		    '<s:property value="%{getLocaleProperty('estimatedYield(kg)')}" />', 
				  		      </s:elseif>
				  		   
				  		    '<s:text name="season"/>',
				  		  //'<s:text name="latitude"/>',
					  		// '<s:text name="longitude"/>',
				  		  '<s:text name="farm.map"/>'	
				  		  
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
					   			        }]
					   			
					   			}},	   				   		
					   		</s:if>
					   		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					   		
					   			{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
					   		</s:if>
						/* <s:if test="farmerCodeEnabled==1">  
				   	        {name:'co.code',index:'co.code',sortable:false},
				   	    </s:if> */
						    {name:'farmerName',index:'farmerName',sortable:false},
							{name:'samithi',index:'samithi',sortable:false},
							{name:'village',index:'village',sortable:false},
							<s:if test="currentTenantId=='chetna'">
							{name:'icsName',index:'icsName',sortable:false},
						     </s:if>
							<s:if test="currentTenantId!='ecoagri'">
							{name:'totalArea',index:'totalArea',sortable:false,align:'right'},
						   
							{name:'estimatedYield',index:'estimatedYield',sortable:false,align:'right'},
							  </s:if>
							{name:'season',index:'season',sortable:false,align:'left'},
							//{name:'latitude',index:'latitude',width:133,sortable:false,align:'left'},
							//{name:'longitude',index:'longitude',width:133,sortable:false,align:'left'},
								{name:'map',index:'map',sortable:false,search:false,align:'center'}
						  
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   //autowidth:true,
				   shrinkToFit:true,
				   scrollOffset: 19,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   viewrecords: true,  	
				   subGrid: true,
				   subGridOptions: {
				   "plusicon" : "glyphicon-plus-sign",
				   "minusicon" : "glyphicon-minus-sign",
				   "openicon" : "glyphicon-hand-right",
				   },
				   
				   subGridRowExpanded: function(subgrid_id, row_id){
					   
					   var subgrid_table_id, pager_id; 
					   subgrid_table_id = subgrid_id+"_t"; 
					   pager_id = "p_"+subgrid_table_id; 
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
					  // var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   var arrayVal = row_id.split("_"); 
					   
					   var coOperativeId ='';
					   var procurementProductId = '';
					   var farmerId = '';

					   if(arrayVal.length>0){
						   coOperativeId = arrayVal[0];
						   procurementProductId = arrayVal[1];
						   farmerId = arrayVal[2];
					   }
					  
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'sowingReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",
							mtype: 'POST',				   	
						   	postData:{				 
								  
							},	
						   	colNames:[	
						   	       	  '<s:text name="farm"/>',
						   	       '<s:property value="%{getLocaleProperty('cropType')}" />' ,
						   	          '<s:text name="crop"/>',
						   	       '<s:text name="Unit"/>',
						   	       	  '<s:text name="variety"/>',
						  		    <s:if test="currentTenantId=='pratibha'">
							  		  '<s:property value="%{getLocaleProperty('farmfarmcrops.estimatedYeild.quintals')}" />', 
							  		      </s:if>
							  		      <s:else>
							  		    '<s:property value="%{getLocaleProperty('estimatedYield(kg)')}" />', 
							  		      </s:else>
						   	       <s:if test="currentTenantId=='wilmar'">
						   	    '<s:property value="%{getLocaleProperty('yearOfPlanting')}" />', 
						   	    </s:if>
					  		      <s:else>
						   	 '<s:text name="Sowing Date"/>',
					  		      </s:else>
						   	    <s:if test="currentTenantId!='ecoagri'">
						   	    '<s:property value="%{getLocaleProperty('cultivationArea')}" />' ,
						   	    </s:if>
						   	 <s:if test="enableCropCalendar==1">
						   	 '<s:property value="%{getLocaleProperty('Calendar')}" />' ,
						   	</s:if>
				 	      	 ],
						   	colModel:[	
									{name:'farmName',index:'farmName',sortable:false},
									{name:'cropCategory',index:'cropCategory',sortable:false},
									{name:'cropId',index:'cropId',sortable:false},
									{name:'unit',index:'unit',sortable:false},
									{name:'variety',index:'variety',sortable:false},
									{name:'estimatedYield',index:'estimatedYield',sortable:false,align:'right'},
									<s:if test="currentTenantId=='wilmar'">
									{name:'interAcre',index:'interAcre',sortable:false,align:'right'},
									 </s:if>
						  		      <s:else>
									 {name:'Date',index:'Date',sortable:false,align:'right'},
									  </s:else>
									  <s:if test="currentTenantId!='ecoagri'">
		   							{name:'cultivationArea',index:'cultivationArea',sortable:false,align:'right'},
		   						 </s:if>
		   						 <s:if test="enableCropCalendar==1">
		   							{name:'calendar',index:'map',sortable:false,search:false,align:'center'},
		   						 </s:if>
	
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    //autowidth: true,
						    shrinkToFit:true,
						    rowNum:10,
							rowList : [10,25,50],
						    viewrecords: true	
					   });
					  // jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
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
		
				
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false})
		
		jQuery("#generate").click( function() {
			reloadGrid();	
			
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
		
		function reloadGrid(){
			jQuery("#detail").jqGrid('setGridParam',{url:"sowingReport_detail.action",page:1}).trigger('reloadGrid');	
			loadFields();
		}

		function clear(){
			$("#seasonId").val("");
			
			$("#samithiId").val("");
			document.form.submit();
			
		}

		
		
		
	});
	
	function loadFields(){
		var season=document.getElementById("seasonId").value;
		var cropId=document.getElementById("cropId").value;
	 	var farmerId=document.getElementById("farmerNameId").value;
	 	var samithi=document.getElementById("samithiId").value;
	 	$.post("sowingReport_populateLoadFields",{farmerName:farmerId,cropId:cropId,season:season,samithi:samithi},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalFarmersId").html(json[0].farmerCount);
	 		$("#totalAreaSownId").html(json[0].cultivationArea);
	 		$("#totalSeedSownId").html(parseFloat(json[0].estiYield).toFixed(2));
	 		
	 	});
	}
	</script>
	<script src="plugins/openlayers/OpenLayers.js"></script>
	<!-- <script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
		type="text/javascript"></script> -->
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


	function showFarmMap(farmCode,farmName,latitude, longitude, landArea){
		
		var heading ='<s:text name="farmName"/>&nbsp;:&nbsp'+farmName;
		var content="<div id='map' class='smallmap'></div>";
		enablePopup(heading,content);
		
		 loadMap('map', latitude, longitude, landArea);
		//loadMap("farmMap",landArea);
		
          /*   var farmCoordinate = jQuery("#farmCordinates").val();
            var landArea = JSON.parse(farmCoordinate); */
           
            
	}
	  function loadMap(mapDiv, latitude, longitude, landArea){
          try{
          jQuery("#map").css("height", "440px");
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
		
	  function exportXLS(){
			
			 if (isDataAvailable("#detail")) {
			        jQuery("#detail").setGridParam({postData: {rows: 0}});
			        jQuery("#detail").jqGrid('excelExport', {url: 'sowingReport_populateXLS.action?'});
			}     else {
			        alert('<s:text name="export.nodata"/>');
			    }
			 
			 
		}
	  
	  function redirectToCalendarView(farmId,seasonCode){
			
			var heading ='<s:property value="%{getLocaleProperty('sowingActivity')}" />';
			var content="<div id='calendarAct' class='smallmap'></div>";
			enablePopup(heading,content);
			
			loadValues(farmId,seasonCode);
		}
		  
		function loadValues(farmId,seasonCode){
			var farm=farmId;	
			var season =seasonCode;
			if (!isEmpty(farm) && !isEmpty(season)) {
				$.post("sowingReport_populateCalendarValues", {
					selectedFarm : farm,
					selectedSeason : season
				}, function(data) {
					var jsonData = $.parseJSON(data);
					var table = $("<table/>").addClass("table table-bordered").attr({
						id : "cropActivity"
			});
			var thead = $("<thead/>");
			var tr = $("<tr/>");
			tr.append($("<th/>").append("Crop Name"));
			tr.append($("<th/>").append("Variety Name"));
			tr.append($("<th/>").append("Activity Method"));
			tr.append($("<th/>").append("Status"));	
			tr.append($("<th/>").append("Date"));
			tr.append($("<th/>").append("Remarks"));	
			table.append(thead);
			thead.append(tr);
			var tbody = $("<tbody/>");
				$.each(jsonData, function(index, value) {
					var tr2 = $("<tr/>");
					tr2.append($("<td/>").append(value.productName));
					tr2.append($("<td/>").append(value.varietyName));	
					tr2.append($("<td/>").append(value.activityName));		
					tr2.append($("<td/>").append(value.status));		
					tr2.append($("<td/>").append(value.date));		
					tr2.append($("<td/>").append(value.remarks));		
					tbody.append(tr2);
				});
			table.append(tbody);
			$('#calendarAct').append(table);
				});
			
			}
		}
	
</script>
	<s:form name="form" action="sowingReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class="filterEle">
						<label for="txt"><s:text name="season" /></label>
						<div class="form-element">
							<s:select name="filter.season" id="seasonId" list="seasonList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"> <s:text name="crop" /></label>
						<div class="form-element">
							<s:select name="filter.procurementVariety.procurementProduct.id"
								id="cropId" list="cropList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:property
								value="%{getLocaleProperty('samithi')}" /></label>
						<div class="form-element">
							<s:select name="samithi" id="samithiId" list="samithiSangham"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('farmer')}" /></label>
						<div class="form-element">
							<s:select name="filter.farm.farmer.id" id="farmerNameId"
								list="farmerNameList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle icsName" style="display: none;">
						<label for="txt"><s:text name="icsName" /></label>
						<div class="form-element">
							<s:select name="filter.farm.ics" id="icsName" list="icsList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>

					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class="filterEle">
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element ">
									<s:select name="branchIdParma" id="branchIdParma"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="select2" onchange="populateChildBranch(this.value)" />

								</div>
							</div>
						</s:if>

						<div class="filterEle">
							<label for="branchIdParam"><s:text name="app.subBranch" /></label>
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
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container">
					<div class="report-summaryBlockItem">
						<span><span class="strong" id="totalFarmersId"></span> <s:property value="%{getLocaleProperty('farmers')}" />&nbsp;<i class="fa fa-user"></i></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><span class="outletsCount" id="totalAreaSownId"></span>
							<s:property value="%{getLocaleProperty('toatArea')}" /> (<s:property value="%{getAreaType().toUpperCase()}" />)&nbsp;<img src="img/area.png"
							width="20px" height="20px"></span>
					</div>
					 
					 <s:if test="currentTenantId!='pratibha'">
					<div class="report-summaryBlockItem">
						<span><span class="outletsCount" id="totalSeedSownId"></span>
						<s:property value="%{getLocaleProperty('toatalEst')}" />&nbsp;<i class="fa fa-pagelines"
							aria-hidden="true"></i></span>
					</div>
					 </s:if>

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
					<li><a href="#" onclick="exportXLS()" role="tab"
						data-toggle="tab" aria-controls="dropdown2" aria-expanded="true"><i
							class="fa fa-table"></i> <s:text name="excel" /></a></li>
				</ul>
			</div>
		</div>

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>



	<div id="restartAlert" class="popStockAlert">
		<div class="popClose" onclick="disableExtendAlert()"></div>
		<div id="pendingRestartAlertErrMsg" class="popPendingMTNTAlertErrMsg"
			align="center"></div>
		<div id="defaultRestartAlert" style="display: block;">

			<table align="center" id="tableData" cellspacing="0" border="1"
				style="text-align: center">
			</table>
		</div>
	</div>
	<s:hidden name="exportLimit" id="exportLimit"></s:hidden>

</body>
