

<%@ include file="/jsp/common/report-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>

<head>
<!-- add this meta information to select layout  -->
<meta name="decorator" content="swithlayout">

<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.js"></script>

</head>
<body>
<script src="plugins/openlayers/OpenLayers.js"></script>
	<script type="text/javascript">
	
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
	
//var temp=0;
$(document).ready(function(){
	loadCustomPopup();
	onFilterData();
	jQuery(".well").hide();
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  audioControlHide();	
		});
	audioControlHide();	
	$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
    $("#daterange").data().daterangepicker.updateView();
     $("#daterange").data().daterangepicker.updateCalendars();
	$('.applyBtn').click();

	loadRegularGrid();
	loadNeedGrid();
	//document.getElementById('fieldl').selectedIndex=0;
	
	document.getElementById('farmerId').selectedIndex=0;
	document.getElementById('farmId').selectedIndex=0;
	<s:if test='branchId==null'>
	document.getElementById('branchIdParam').selectedIndex=0;
	</s:if>
	document.getElementById('createdUserName').selectedIndex=0;
	
	function onFilterData(){
		callAjaxMethod("periodicInspectionReport_populateSeasonList.action","seasonId");
		callAjaxMethod("periodicInspectionReport_populateFarmersList.action","farmerId");
		callAjaxMethod("periodicInspectionReport_populateFarmList.action","farmId");
		callAjaxMethod("periodicInspectionReport_populateUserList.action","createdUserName");
		callAjaxMethod("periodicInspectionReport_populateIcsList.action","icsName");
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
});	

 
function downloadAudioFile(id){
	document.getElementById("loadId").value=id;
	$('#audioFileDownload').submit();
}


function playAudioFiles(id, inspectionDate, farmerIdName, farmCodeName){

	audioControlShow();
	//<![CDATA[
	
	$("#jquery_jplayer_1").jPlayer("destroy");           
	
	$("#inspectionDate").html(inspectionDate);
	$("#farmerData").html(farmerIdName);
	$("#farmData").html(farmCodeName);
	
	$("#jquery_jplayer_1").jPlayer({
		ready: function () {
			$(this).jPlayer("setMedia", {
				mp3: "periodicInspectionServiceReport_populateAudioPlay?id="+id+"&dt="+new Date()
			}).jPlayer("play");
		},
		swfPath: "js/jplayer",
		solution:"html,flash",
		supplied: "mp3",
		wmode: "window",
		useStateClassSkin: true,
		autoBlur: true,
		smoothPlayBar: true,
		keyEnabled: true,
		remainingDuration: true,
		toggleDuration: true
	});
	//]]>
}

function audioControlShow(){
	jQuery("#audioDiv").show();
	jQuery("#audioDataDiv").show();
}
function audioControlHide(){
	$("#jquery_jplayer_1").jPlayer("stop");
	jQuery("#audioDiv").hide();
	jQuery("#audioDataDiv").hide();
}

function loadRegularGrid(){
		jQuery("#detail").jqGrid(
			{
			   	url:'periodicInspectionServiceReport_data.action',
			   	pager: '#pagerForDetail',
				mtype: 'POST',
				  styleUI : 'Bootstrap',
				postData:{	
				        "startDate" : function(){
                           return document.getElementById("hiddenStartDate").value;
                          },		
                        "endDate" : function(){
			               return document.getElementById("hiddenEndDate").value;
		                   },			       
			            "farmerId" : function(){
					        return document.getElementById("farmerId").value;
				           },
				        "farmerFatherId" : function(){
						        return document.getElementById("farmerFatherId").value;
					           },
				        "farmId" : function(){
					        return document.getElementById("farmId").value;
				            }, 
				            "seasonCode" : function(){
						        return document.getElementById("seasonId").value;
					            }, 
				        "createdUserName" : function(){
						        return document.getElementById("createdUserName").value;
					            },
					            <s:if test='branchId==null'>
					            "branchIdParma" : function(){
			 						  return document.getElementById("branchIdParam").value;
			 					  },
			 					  </s:if>
						"inspectionType" : function(){
				  		 return '1';
			    }
    	        },	
			   	datatype: "json",	
			   	colNames:[
				           
<s:if test='branchId==null'>
'<s:text name="%{getLocaleProperty('app.branch')}"/>',
</s:if>
<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>

'<s:text name="app.subBranch"/>',
</s:if>
                          '<s:text name="date"/>',
			  		   	  '<s:text name="seasonCode"/>',
			  		      '<s:property value="%{getLocaleProperty('farmerCodeName')}" />',
			  		      '<s:property value="%{getLocaleProperty('farmer.firstName')}"/>', 
			  		      '<s:text name="farm"/>',
			  		      '<s:text name="cropNames"/>',
			  		    <s:if test="currentTenantId=='pratibha' && branchId!='bci'">
				  		    '<s:text name="icsName"/>',
				  		    </s:if>
			  		      '<s:property value="%{getLocaleProperty('profile.agent')}"/>	',			  		     
			  		    '<s:property value="%{getLocaleProperty('currentStatusofGrowth')}"/>',
			  		    <s:if test="currentTenantId!='chetna'">
			  		    '<s:text name="pestOccurence"/>',
						  '<s:text name="diseaseOccurence"/>',
						  </s:if>
						  <s:if test="currentTenantId!='pratibha'&& currentTenantId!='chetna' && currentTenantId!='indev' && currentTenantId!='crsdemo'">
						  	'<s:text name="intercrop"/>',
						  </s:if>
						  	 <s:if test="currentTenantId=='avt'">
					  		    '<s:text name="Crop Protection"/>',
					  		  </s:if>
						  '<s:text name="farm.map"/>',				  	 
						  '<s:text name="audioFile"/>'
						  
						 
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
					{name:'inspectionDate',index:'inspectionDate',sortable:false},
					{name:'seasonCode',index:'seasonCode',sortable:false},
					{name:'farmerIdName',index:'farmerIdName',sortable:false},
					{name:'father',index:'father',sortable:false,search:false},
					{name:'farmId',index:'farmId',sortable:false},
					{name:'cropCode',index:'cropCode',sortable:false},
					<s:if test="currentTenantId=='pratibha' && branchId!='bci'">
					{name:'icsName',index:'icsName',sortable:false},
					</s:if>
					{name:'fieldstaffIdName',index:'fieldstaffIdName',sortable:false},
					{name:'currentStatusofGrowth',index:'currentStatusofGrowth',sortable:false},
					  <s:if test="currentTenantId!='chetna'">
					{name:'pestOccurence',index:'pestOccurence',sortable:false},
					{name:'diseaseOccurence',index:'diseaseOccurence',sortable:false},
					</s:if>
					 <s:if test="currentTenantId!='pratibha' && currentTenantId!='chetna' && currentTenantId!='indev'&& currentTenantId!='crsdemo'">
					{name:'intercrop',index:'intercrop',sortable:false},
				 </s:if>	
					<s:if test="currentTenantId=='avt'">
					 {name:'cropprotection',index:'cropprotection',sortable:false},
			  		  </s:if>
					{name:'map',index:'map',sortable:false,align:'center'},
					{name:'audioFile',index:'audioFile',sortable:false,align:'center'}
					
					
		   	],
			   	height: 301, 
			    width: $("#baseDiv").width(), // assign parent div width
			    scrollOffset: 0,
			   	rowNum:10,
			   	rowList : [10,25,50],
			    sortname:'id',			
			    autowidth:true,
				shrinkToFit:true,	
			    sortorder: "desc",
			    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table
			    beforeSelectRow: 
			        function(rowid, e) {
			            var iCol = jQuery.jgrid.getCellIndex(e.target);
			            if (iCol >=8){return false; }
			            else{ return true; }
			        },
			        onSelectRow: function(id){ 
						  document.detailform.id.value  =id;
						  document.detailform.tabIndex.value ='<s:property value="tabIndex"/>'; 
					      document.detailform.submit();      
						},
		    });
	jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
	//jQuery("#detail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.
			
			
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
		    jQuery("#generate").click( function() {
				reloadGrid();	
			});
			
			jQuery("#clear").click( function() {
				clear();
			});	
			//jQuery("#detail").jqGrid('setFrozenColumns');
}
function loadNeedGrid(){
	

jQuery("#needDetail").jqGrid(
		{
		   	url:'periodicInspectionServiceReport_data.action',
		   	pager: '#pagerForNeedDetail',
			mtype: 'POST',
			  styleUI : 'Bootstrap',
			postData:{  
		        	"startDate" : function(){
                      return document.getElementById("hiddenStartDate").value;
                     },		
                     "endDate" : function(){
                      return document.getElementById("hiddenEndDate").value;
                    },
				     "farmerId" : function(){
	                  return document.getElementById("farmerId").value;
                    },
                     "farmerFatherId" : function(){
  	                  return document.getElementById("farmerFatherId").value;
                      },
			         "farmId" : function(){
		              return document.getElementById("farmId").value;
	                },
	                "seasonCode" : function(){
				        return document.getElementById("seasonId").value;
			            }, 
	                 "createdUserName" : function(){
				      return document.getElementById("createdUserName").value;
			        },
			        <s:if test='branchId==null'>
			        "branchIdParma" : function(){
						  return document.getElementById("branchIdParam").value;
					  },
					  </s:if>
				     "inspectionType" : function(){
				     return '2';
			       }
    	        },	
		   	datatype: "json",	
		   	colNames:[				   	       	  
<s:if test='branchId==null'>
'<s:text name="app.branch"/>',
</s:if>
		  		   	  '<s:text name="inspectionDate"/>',
		  		      '<s:text name="seasonCode"/>',
		  		      '<s:text name="farmerCodeName"/>',
		  		      '<s:property value="%{getLocaleProperty('farmer.firstName')}"/>',
		  		      '<s:text name="farmIdName"/>',
		  		      '<s:text name="cropNames"/>',
		  		      '<s:text name="fieldstaffIdName"/>',			  		     
		  		      '<s:text name="purpose"/>',
		  		  	  '<s:text name="remarks"/>',
		  		  	 '<s:text name="farm.map"/>',
		  		      '<s:text name="audioFile"/>'
		      	 ],
		   	colModel:[	
<s:if test='branchId==null'>
{name:'branchId',index:'branchId',width:160,sortable:false},	   				   		
</s:if>	
				
					{name:'inspectionDate',index:'inspectionDate',width:120,sortable:false},
					{name:'seasonCode',index:'seasonCode',width:120,sortable:false},
					{name:'farmerIdNAme',index:'farmerIdNAme',width:150,sortable:false},
					{name:'father',index:'father',width:150,sortable:false},
					{name:'farmIdName',index:'farmIdName',width:150,sortable:false},
					{name:'cropCode',index:'cropCode',width:150,sortable:false},
					{name:'fieldstaffIdName',index:'fieldstaffIdName',width:150,sortable:false},
					{name:'purpose',index:'purpose',width:150,sortable:false},
					{name:'remarks',index:'remarks',width:120,sortable:false},
					{name:'map',index:'map',width:100,sortable:false,align:'center'},
					{name:'audioFile',index:'audioFile',width:100,sortable:false,align:'center'}
		   	],
		   	height: 301, 
		    width: $("#baseDiv").width(), // assign parent div width
		    scrollOffset: 0,
		   	rowNum:10,
		   	rowList : [10,25,50],
		    sortname:'id',			  
		    //autowidth:true,
			shrinkToFit:true,
		    sortorder: "desc",
		    viewrecords: true, // for viewing noofrecords displaying string at the right side of the table,

		    onSelectRow: function(id){ 
    			     document.updateform.id.value  =id;
    			    document.updateform.tabIndex.value = '<s:property value="tabIndex"/>'; 
    			     document.updateform.submit();      
					},
					
		    beforeSelectRow: 
		        function(rowid, e) {
		            var iCol = jQuery.jgrid.getCellIndex(e.target);
		            if (iCol >=6){return false; }
		            else{ return true; }
		        },

	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        }
	    });
	
		
		colModel = jQuery("#needDetail").jqGrid('getGridParam', 'colModel');
	    $('#gbox_' + jQuery.jgrid.jqID(jQuery("#needDetail")[0].id) +
	        ' tr.ui-jqgrid-labels th.ui-th-column').each(function (i) {
	        var cmi = colModel[i], colName = cmi.name;

	        if (cmi.sortable !== false) {
	            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
	        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
	            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
	        }
	    });
	    jQuery("#needDetail").jqGrid('navGrid','#pagerForNeedDetail',{edit:false,add:false,del:false,search:false,refresh:true}) // enabled refresh for reloading grid
		//jQuery("#needDetail").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false}); // enabling filters on top of the header.

	    jQuery("#generate").click( function() {
	    	reloadGrid();	
		});
		
		jQuery("#clear").click( function() {
			clear();
		});	
		
		
}
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
	var endDate=new Date(document.getElementById("hiddenEndDate").value);
	if (startDate>endDate){
		alert('<s:text name="date.range"/>');
	}else{
		
		jQuery("#detail").jqGrid('setGridParam',{url:"periodicInspectionReport_data.action?",page:1}).trigger('reloadGrid');	
		jQuery("#needDetail").jqGrid('setGridParam',{url:"periodicInspectionReport_data.action?",page:1}).trigger('reloadGrid');		
         }	
}
function clear(){
	$("#seasonId").val("");
	$("#farmerId").val("");
	$("#farmerFatherId").val("");
	$("#farmId").val("");
    $("#createdUserName").val("");
    $("#branchIdParam").val("");
	document.form.submit();
}

function showFarmMap(latitude, longitude){
	
	var heading ='<s:text name="coordinates"/>&nbsp;:&nbsp'+latitude+","+longitude;
	var content="<div id='map' class='smallmap'></div>";
	enablePopup(heading,content);
	//var landArea = latitude, longitude;
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
      var googleLayer2 = new OpenLayers.Layer.OSM();
      mapObject.addLayer(googleLayer2);
      mapObject.addControls([new OpenLayers.Control.Navigation(), new OpenLayers.Control.Attribution(), new OpenLayers.Control.PanZoomBar(), ]);
      // new OpenLayers.Control.LayerSwitcher()]);
      mapObject.zoomToMaxExtent();
      var setCenter = true;
      // alert(latitude+"===="+longitude);
      if (latitude != null && latitude != '' && longitude != null && longitude != '' && longitude != 0 && latitude != 0 ){

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

/*       if (landArea.length > 0){

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
      } */

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

  
 
</script>
<body>
    <s:form name="form" action="periodicInspectionReport_list">
		<div class="appContentWrapper marginBottom hide">
				<div class="reportFilterWrapper filterControls">
					<div class="reportFilterItem">
						<label for="txt"><s:text name="startingDate" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm" />
						</div>
					</div>
					<div class="reportFilterItem">
						<label for="txt"><s:text name="season" /></label>
						<div class="form-element">
							<s:select name="seasonCode" id="seasonId" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"/>
						</div>
					</div>
					<div class="reportFilterItem">
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="farmerId" id="farmerId" list="{}"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
						</div>
					</div>
					 
					<div class="reportFilterItem">
						<label for="txt"><s:property value="%{getLocaleProperty('fatherName')}" /></label>
						<div class="form-element">
							<s:select name="farmerFatherId" id="farmerFatherId"
					list="{}" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
						</div>
					</div>
					

				
			<div class="reportFilterItem" style="margin-top: 24px;">
					<button type="button" class="btn btn-success btn-sm"
						id="generate" aria-hidden="true">
						<b class="fa fa-search"></b>
					</button>
					<button type="button" class="btn btn-danger btn-sm" aria-hidden="true"
						 id="clear">
						<b class="fa fa-close"></b>
					</button>
				</div>
					</div>
					
					<div class="reportFilterWrapper filterControls">
					<div class="reportFilterItem">
						<label for="txt"><s:text name="farm" /></label>
						<div class="form-element">
							<s:select name="farmId" id="farmId" list="{}" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="reportFilterItem">
						<label for="txt"><s:text name="agent" /></label>
						<div class="form-element">
							<s:select name="createdUserName" id="createdUserName"
					list="{}" headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"/>
						</div>
					</div>
					
					<s:if
					test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					<s:if test='branchId==null'>
						<div class="reportFilterItem">
							<label for="txt"><s:text name="app.branch" /></label>
							<div class="form-element ">
								<s:select name="branchIdParam" id="branchIdParam"
									list="parentBranches" headerKey="" headerValue="Select"
									cssClass="select2" onchange="populateChildBranch(this.value)" />

							</div>
						</div>
					</s:if>
					
					<div class="reportFilterItem">
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
				 
					<div class="reportFilterItem">
					<label for="txt"><s:text name="app.branch" /></label>
					<div class="form-element">
						<s:select name="branchIdParam" id="branchIdParam"
							list="branchesMap" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				</s:if>
				</s:else>
					
					
					<div class="reportFilterItem">
						<label for="txt"><s:text name="icsName" /></label>
						<div class="form-element">
						<s:select name="icsName" id="icsName" class="form-control input-sm select2" list="{}"
									headerKey="" headerValue="%{getText('txt.select')}"
									listKey="key" listValue="value"></s:select>
						</div>
					</div>
					
					
					</div>
					</div>
					</s:form>
	<div id="baseDiv" style="width:97%"> 	</div>
	<div class="appContentWrapper marginBottom">
		<ul class="nav nav-pills">
			<li class="active"><a href="#tabs-1" data-toggle="pill"><s:text
						name="info.regular" /></a></li>
			<li><a href="#tabs-2" data-toggle="pill"><s:text
						name="info.need" /></a></li>
		</ul>
	</div>
      <div class="tab-content">
			<div id="tabs-1" class="tab-pane fade in active">
			<div class="appContentWrapper marginBottom">
				<div style="width: 99%;" id="baseDiv">
				<input type="BUTTON" class="btn btn-sts" value="<s:text name="exportXls"/>" style="margin-right:20px;position:absolute; right:-22px; top:-33px;" onclick="exportXLS('detail');" title="Export XLS"/>
				<br>
					<table id="detail"></table>
					<div id="pagerForDetail"></div>
				</div>
			</div>	
			</div>

			<div id="tabs-2" class="tab-pane fade">
			<div class="appContentWrapper marginBottom">
				<div style="width: 99%;" id="baseDiv">
				<input type="BUTTON" class="btn btn-sts" value="<s:text name="exportXls"/>" style="margin-right:20px;position:absolute; right:-22px; top:-33px;" onclick="exportXLS('needDetail');" title="Export XLS"/>
					<table id="needDetail"></table>
					<div id="pagerForNeedDetail"></div>
				</div>
			</div>
			</div>
		</div>

		
		

	<!-- Audio Player Realter Code Start  -->
	<div id="audioDataDiv"
		style="width: 700px; margin-top: 5px; border: 0px solid #cccccc;">
		<table>
			<tr>
				<td width="45%"><b><s:text name="inspectionDate" /></b></td>
				<td><span id="inspectionDate">--</span></td>
			</tr>
			<tr>
				<td width="45%"><b><s:text name="farmerIdName" /></b></td>
				<td><span id="farmerData">--</span></td>
			</tr>
			<tr>
				<td width="45%"><b><s:text name="farmIdName" /></b></td>
				<td><span id="farmData">--</span></td>
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
	<div class="clear"></div>
	<!-- Audio Player Realter Code End  -->
	<s:form name="detailform" action="periodicInspectionServiceReport_detail.action?type=service">
		<s:hidden key="id" />
		<s:hidden name="tabIndex" />
		<s:hidden key="currentPage" />
	</s:form>

	<s:form name="updateform" action="periodicInspectionServiceReport_detailNeed.action?type=service">
		<s:hidden key="id" />
		<s:hidden name="tabIndex" />
		<s:hidden key="currentPage" />
	</s:form>
		<s:form id="audioFileDownload"
		action="periodicInspectionServiceReport_populateDownload">
		<s:hidden id="loadId" name="id" />
	</s:form>
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
</body>