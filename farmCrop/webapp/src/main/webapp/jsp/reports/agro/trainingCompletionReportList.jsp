<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>


<head>
<meta name="decorator" content="swithlayout">
<script src="plugins/openlayers/OpenLayers.js"></script>
<script src="plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
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
	<script src="plugins/imageSlider/js/jquery.slides.min.js"></script>
	<script type="text/javascript">
var $overlay;
var $modal;
var $slider;
var temp=0;
	jQuery(document).ready(function(){
		onFilterData(); 
		loadCustomPopup();
			$("#dialog").hide();
			jQuery(".well").hide();
			getFilterData();
			if(filterDataReport==''){
		//document.getElementById('fieldl').selectedIndex=0;
		document.getElementById('agentCode').selectedIndex=0;
		document.getElementById('learningGroupCode').selectedIndex=0;
		document.getElementById('farmerFilter').selectedIndex=0;
		//document.getElementById('trainingCode').selectedIndex=0;
		/* <s:if test="currentTenantId=='pratibha'"> */
		
		/* </s:if> */
			}
		$("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	    $("#daterange").data().daterangepicker.updateView();
	    $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		
		$('.applyBtn').click();
		 
		$overlay = $('<div id="modOverlay" ></div>');
		$modal = $('<div id="modalWin" class="ui-body-c"></div>');
		$slider = $('<div id="banner-fade" style="margin:10 auto;"><ul class="bjqs"></ul></div>')
		$close = $('<button id="clsBtn" class="btnCls">X</button>');
		
		$modal.append($slider, $close);
		$('body').append($overlay);
		$('body').append($modal);
		$modal.hide();
		$overlay.hide();
		
		jQuery("#detail").jqGrid(
				{
					url:'trainingCompletionReport_data.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',
				    styleUI : 'Bootstrap',
					postData:{	
						"startDate" : function(){
		                     return document.getElementById("hiddenStartDate").value;           
		                    },		
		                    "endDate" : function(){
				               return document.getElementById("hiddenEndDate").value;
			                 },			 
						  "agent" : function(){
							  return document.getElementById("agentCode").value;
						  },
						  "learningGroup" : function(){
							  return document.getElementById("learningGroupCode").value;
						  },
						  "postdata" :  function(){	
		  	 		   			return  decodeURI(postdataReport);
		  	 				  },
		  	 				  "farmerFilter" : function(){
								  return document.getElementById("farmerFilter").value;
							  },
		  	 				 /* "trainingCode" : function(){
								  return document.getElementById("trainingCode").value;
							  }, */
							 /*  <s:if test="currentTenantId=='pratibha'"> */
							
							  /* </s:if> */
						  <s:if test='branchId==null'>
						  "branchIdParma" : function(){
							  return document.getElementById("branchIdParma").value;
						  },
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
							 '<s:text name="trainingDate"/> (<s:property value="%{getGeneralDateFormat().toUpperCase()}" />)',
							  '<s:text name="%{getLocaleProperty('farmerTrainingCode')}"/>',
							  '<s:text name="%{getLocaleProperty('trainingTopic')}"/>',
				  		   	  '<s:text name="agentName"/>',
				  		    '<s:text name="%{getLocaleProperty('farmerAttended')}"/>',
				  		    //  '<s:text name="%{getLocaleProperty('village')}"/>',
				  		   	<s:if test="currentTenantId=='pratibha'">
				  		    '<s:text name="%{getLocaleProperty('trainee')}"/>',
				  		    </s:if>
				  		 
				  		   	  '<s:text name="%{getLocaleProperty('Group')}"/>',
				  		      //'<s:text name="%{getLocaleProperty('farmerAttended')}"/>',
				  		      '<s:text name="remarks"/>',				  		   
				  		     '<s:text name="image"/>',
				  		   '<s:text name="map"/>'
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
						{name:'trainingDate',index:'trainingDate',width:100,sortable:false},
						{name:'farmerTrainingCode',index:'farmerTrainingCode',sortable:false},
						{name:'trainingTopic',index:'trainingTopic',sortable:false},
						{name:'agentCode',index:'agentCode',sortable:false},
						{name:'farmerAttended',index:'farmerAttended',width:250,sortable:false},
					//	{name:'village',index:'village',width:250,sortable:false},
						<s:if test="currentTenantId=='pratibha'">
						{name:'trainee',index:'trainee',sortable:false},
						</s:if>
						
						{name:'learningGroup',index:'learningGroup',sortable:false},
						//{name:'farmerAttended',index:'farmerAttended',sortable:false},
						{name:'remarks',index:'remarks',sortable:false},					
						{name:'image',index:'image',align:"center",sortable:false},
						{name:'map',index:'map',sortable:false}
				   		
				   	],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset:25,     
				   sortname:'id',
				   sortorder: "desc",
				   rowNum:10,
				   rowList : [10,25,50],
				   shrinkToFit:true,
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
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
						   
							url:'trainingCompletionReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",
						    styleUI : 'Bootstrap',
						   	colNames:[	
						  		   	  
						  		    '<s:text name="%{getLocaleProperty('code')}"/>',
						  		    '<s:text name="%{getLocaleProperty('principle')}"/>',
						  		   	 		  		  	 
						  		   	  '<s:text name="des"/>'
				 	      	 ],
						   	colModel:[										
		   							{name:'code',index:'code',sortable:false},
		   							{name:'principle',index:'principle',sortable:false},
		   							{name:'des',index:'des',sortable:false}
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    sortorder: "asc",
						    //autowidth: true,
						    shrinkToFit:true,
						    rowNum:10,
							rowList : [10,25,50],
						    viewrecords: true					
					   });
					   jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false});
					  // jQuery("#"+subgrid_table_id).jqGrid('navGrid',{edit:false,add:false,del:false,search:false,refresh:false}) //,"#"+pager_id
					    jQuery("#"+subgrid_id).parent().css("width","100%");
					    jQuery("#"+subgrid_id).parent().css("background-color","#fff");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+",#"+subgrid_table_id+",#gbox_"+subgrid_table_id+",.ui-jqgrid-bdiv,.ui-jqgrid-hdiv,.ui-jqgrid-hbox,.ui-jqgrid-htable").css("width","100%");
					    jQuery("#"+subgrid_id).find("#gview_"+subgrid_table_id+" td,#"+subgrid_table_id+" td,#gbox_"+subgrid_table_id+" td").css("border","none");
					    jQuery("#"+subgrid_id).find(".ui-jqgrid-hbox").css("background-color","#fff");	
				},				
				  onSelectRow: function(id){ 
					  document.updateform.id.value  =id;
					  getSelectData();
					  var postdataReport =  JSON.stringify($('#detail').getGridParam('postData'));
					  document.updateform.postdataReport.value =postdataReport;
			          document.updateform.submit();      
					},
					beforeSelectRow: function(rowid, e) {
					    var iCol = jQuery.jgrid.getCellIndex(e.target);
					   if (iCol >4){return false; }
					    else{ return true; }
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
			jQuery("#detail").jqGrid('setGridParam',{url:"trainingCompletionReport_data.action?",page:1}).trigger('reloadGrid');	
		}
		}

		function clear(){
	
			$("#agentCode").val("");
			$("#learningGroupCode").val("");
			$("#branchIdParma").val("");
			$("#farmerFilter").val("");
			//$("#trainingCode").val("");
			
			//document.form.action = "trainingCompletionReport_list.action";
			document.form.submit();
		}
		 jQuery("#clsBtn").click(function () {
		    	$('#modalWin').css('margin-top','-650px');	
				$modal.hide();
				$overlay.hide();			
				$('body').css('overflow','visible');
				
			});

		    $(window).bind('resize', function() {
				$("#detail").setGridWidth($("#baseDiv").width());
				$(".ui-jqgrid-btable").css({"width":$("#baseDiv").width()})
				$(".ui-jqgrid-htable").css({"width":$("#baseDiv").width()}) 
	    	 }).trigger('resize');
		    
		    
		    jQuery("#minus").click( function() {
				clear();
			});
			jQuery("#plus").click( function() {
				showFields();
			});
			
			postdataReport='';
	});

/* 	var imgID = "";
	function popupWindow(id, imageId) {
		
		$("#dialog").show();
		$('#slides').slidesjs({
	    
	        navigation: false
	      });

		
		try{
		
			$(".slidesjs-control").empty();
			$(".slidesjs-pagination").empty();
	
			var str_array = imageId.split(',');
		
			for(var i = 0; i < str_array.length; i++){
				var str_imageCode = str_array[i].split('/');
				$(".slidesjs-control").append('<img class="slidesjs-slide" src="trainingCompletionReport_detailImage.action?id='+str_imageCode[0]+'" slidesjs-index="0"/>');
				
				$(".slidesjs-pagination").append('<li class="slidesjs-pagination-item"></li>');
			
			 }
			
			$( "#dialog" ).dialog({
				  title: "Photo",
				  height: 300,
				  width: 420
				});
		}

		catch(e){
			alert(e);
			}
		
	} */
	
	 var imgID = "";
	function popupWindow(id, imageId) {
		
		try{
			var str_array = imageId.split(',');
			
			$("#mbody").empty();
			
			var mbody="";
			
			for(var i = 0; i < str_array.length; i++){
				var str_imageCode = str_array[i].split('/');
				
				if(i==0){
					mbody="";
					mbody="<div class='item active'>";
					mbody+='<img src="trainingCompletionReport_detailImage.action?id='+str_imageCode[0]+'"/>';
					mbody+="</div>";
				}else{
					mbody="";
					mbody="<div class='item'>";
					mbody+='<img src="trainingCompletionReport_detailImage.action?id='+str_imageCode[0]+'"/>';
					mbody+="</div>";
				}
				$("#mbody").append(mbody);
			 }
			
			
			
			document.getElementById("enableModal").click();	
		}
		catch(e){
			
			alert(e);
			}
		
	}
	
	function exportXLS(){
		if(isDataAvailable("#detail")){
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'trainingCompletionReport_populateXLS.action'});
		}else
		     alert('<s:text name="export.nodata"/>');
	}
</script>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script async defer
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
<!-- <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
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
var $overlays;
var $modals;
var $sliders;

function loadCustomPopup(){
	$overlays = $('<div id="modOverlay"></div>');
	$modals = $('<div id="modalWin" class="ui-body-c gmap3"></div>');
	$sliders = $('<div id="banner-fade" style="margin:0 auto;"><ul class="bjqs"></ul></div>')
	$close = $('<button id="clsBtn" class="btnCls">X</button>');
	
	$modals.append($sliders, $close);
	$('body').append($overlays);
	$('body').append($modals);
	$modals.hide();
	$overlays.hide();

	jQuery("#clsBtn").click(function () {
    	$('#modalWin').css('margin-top','-230px');	
		$modals.hide();
		$overlays.hide();			
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
	$modals.show();
	$overlays.show();
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


	function showFarmMap(latitude, longitude){	
		var heading ='<s:text name="coordinates"/>&nbsp;:&nbsp'+latitude+","+longitude;
		var content="<div id='map' class='smallmap'></div>";
		enablePopup(heading,content);
		//loadMap('map', latitude, longitude);
		initMap();
		 loadMap(latitude, longitude);
		//loadMap("farmMap",landArea);
		
          /*   var farmCoordinate = jQuery("#farmCordinates").val();
            var landArea = JSON.parse(farmCoordinate); */
           
            
	}
	
	function initMap() {
		map = new google.maps.Map(document.getElementById('map'), {
			center : {
				lat : 11.0168,
				lng : 76.9558
			},
			zoom : 3,
			mapTypeId: google.maps.MapTypeId.HYBRID,
		});
		
		
	}
	function loadMap(latitude, longitude){
		//setMapOnAll(null);
		intermediateImg = "red_placemarker.png";
		 intermediatePointIcon = temp + '/img/'+intermediateImg;
		marker = new google.maps.Marker({
			position : new google.maps.LatLng(latitude,
					longitude),
			
			icon:intermediatePointIcon,
			map : map
					
		});
		//markersArray.push(marker);
	}
	
 	 /*   function loadMap(mapDiv, latitude, longitude){
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

      
          }
          catch (err){
              console.log(err);
              }
          } */
          function setMapOnAll(map) {
		        for (var i = 0; i < markersArray.length; i++) {
		        	markersArray[i].setMap(map);
		        }
		        markersArray = new Array();
		 }
         
         
	  function onFilterData(){
			callAjaxMethod("trainingCompletionReport_populateAgentList.action","agentCode");
			callAjaxMethod("trainingCompletionReport_populateLearningGroupList.action","learningGroupCode");
			callAjaxMethod("trainingCompletionReport_populateFarmerList.action","farmerFilter");
			//callAjaxMethod("trainingCompletionReport_populateTrainingCodeList.action","trainingCode");
			/* <s:if test="currentTenantId=='pratibha'"> */
			
			/* </s:if> */
		}
		function callAjaxMethod(url,name){
			var cat = $.ajax({
				url: url,
				async: false, 
				type: 'post',
				success: function(result) {
					insertOptions(name,JSON.parse(result));
				}        	

			});
			
		}	
</script>
	<!-- <div id="dialog" title="Basic dialog">
		<div>
			<div class="container">
				<div id="slides">
					<img src="trainingCompletionReport_detailImageData.action?id=6">
					<img src="img/avatar-xl.jpg"> <a href="#"
						class="slidesjs-previous slidesjs-navigation"><i
						class="fa fa-chevron-circle-left"></i></a> <a href="#"
						class="slidesjs-next slidesjs-navigation"><i
						class="fa fa-chevron-circle-right"></i></a>

				</div>
			</div>
		</div>
	</div> -->
	
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
	
	
	<s:form name="form" id="filter" action="trainingCompletionReport_list">
	<div class="appContentWrapper marginBottom">
				<section class='reportWrap row'>
				<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:text name="date" /></label>
					<div class="form-element">
				<input id="daterange" name="daterange" id="daterangepicker"
					class="form-control input-sm " />
					</div>
				</div>
				
				<div class="filterEle">
					<label for="txt"><s:text name="agentName" /></label>
					<div class="form-element">
					<s:select name="agent" id="agentCode" list="{}" listKey="key"
					listValue="value" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"  />
					</div>
				</div>
				
		<%-- 		<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('TrainingCode')}" /></label>
					<div class="form-element">
					<s:select name="trainingCode" id="trainingCode" list="{}" listKey="key"
					listValue="value" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"  />
					</div>
				</div> --%>
				
				
				<div class="filterEle">
					<label for="txt"> <s:property value="%{getLocaleProperty('Group')}" /></label>
					<div class="form-element">
					<s:select name="learningGroup" id="learningGroupCode"
					list="{}" listKey="key" listValue="value"
					headerKey="" headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"  />
					</div>
				</div>
				
				
				
				<%-- <s:if test="currentTenantId=='pratibha'"> --%>
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('farmerId')}"/></label>
					<div class="form-element">
					<s:select name="famrerFilter" id="farmerFilter" list="{}" listKey="key"
					listValue="value" headerKey=""
					headerValue="%{getText('txt.select')}"
					cssClass="form-control input-sm select2"  />
					</div>
				</div>
				<%-- </s:if> --%>
				
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
				
				
				<div class="filterEle" style="margin-top: 2%;margin-right: 0%;">
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
			</section>
	</div>

		
	</s:form>
	
	<div class="appContentWrapper marginBottom">
		
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

	
	<s:form name="updateform" action="trainingCompletionReport_detail">
		<s:hidden key="id" />
		<s:hidden key="currentPage" />
		<s:hidden name="filterMapReport" id="filterMapReport" />
		<s:hidden name="postdataReport" id="postdataReport" />
	</s:form>

		
	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>

</body>
