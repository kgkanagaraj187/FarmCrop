<%@ include file="/jsp/common/detail-assets.jsp"%>
<%@ include file="/jsp/common/grid-assets.jsp"%>


<head>

<meta name="decorator" content="swithlayout">
<link rel="stylesheet" href="plugins/openlayers/theme/default/style.css"
	type="text/css">
</head>
<body>

<style>

@media print {
 / styles go here /

 .farmerPro {
	width:70%
 }
 
 
}
	.farmerPro {
		width:87%
	 }
	
	.process_msg {
		display: none; /* Hidden by default */
		position: fixed; /* Stay in place */
		z-index: 1; /* Sit on top */
		padding-top: 100px; /* Location of the box */
		left: 0;
		top: 0;
		width: 100%; /* Full width */
		height: 100%; /* Full height */
		overflow: auto; /* Enable scroll if needed */
		background-color: rgb(0, 0, 0); /* Fallback color */
		background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
	}
	
	/* Modal Content */
	.process_msg-content {
		background-color: #fefefe;
		margin: auto;
		padding: 20px;
		border: 1px solid #888;
		width: 30%;
	}
	
	.process_msg-content_loading_msg {
		background-color: FFF;
		margin-left: 43%;
		/* padding: 10px;
	    border: 1px solid #888; */
		align: center;
		margin-top: 15%;
		color: white;
	}
	
	/* The Close Button */
	.close {
		color: #aaaaaa;
		float: right;
		font-size: 28px;
		font-weight: bold;
	}
	
	.close:hover, .close:focus {
		color: #000;
		text-decoration: none;
		cursor: pointer;
	}
	
	/* .head h2 {
  font-size: 14px;
  padding: 10px;
  background: #2A3F54;
  color:white;
  text-align:center;
  font-weight: bold;
  margin: 0 0 10px 0; } */
	
	
</style>
<script src="js/dynamicComponents.js?vk=12.2"></script>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script>

	jQuery(document).ready(function() {
		get_Google_map_img();
		convertasbinaryimage();
		console.log("kishore ----> "+"<s:property value="staticMapUrl"/>");
		var tenant='<s:property value="getCurrentTenantId()"/>';
		var branchId='<s:property value="getBranchId()"/>';
		var type= '<%=request.getParameter("type")%>';
		
		 hideFields();
		 showFields(type);
		
		var isCert = '<s:property value="farm.farmer.isCertifiedFarmer" />';
		var icsType='<s:property value="farm.farmIcsConv.icsType" />';
		loadFarmMap(isCert,icsType);
		
		
		
		
		var txnType = '<s:property value="getTxnType()"/>';
		var entityType = '<s:property value="entityType"/>';
		var seasonType = '<s:property value="seasonType"/>';
		$("#farmerDynamicDataId").val('<s:property value="getIcs_dynamic_data_id()"/>');
		
		renderDynamicDetailFeildsByTxnType();
		
		$(".farmDiv").hide();
		$(".farmerDiv").hide();
		$(".icsDetailDiv").hide();
		$(".groupDiv").hide();
		$(".farmCropDiv").hide();
		if (entityType == '4') {
			$(".farmDiv").show();
			$(".farmerDiv").show();
			//$(".groupDiv").show();
			$(".icsDetailDiv").show();
			$(".inspectClass").show();
			recordName = '<s:property value="farmList" />';
		} else if (entityType == '1' || entityType == '5') {
			$(".farmerDiv").show();
			recordName = '<s:property value="farmer" />';
			$(".inspectClass").hide();
		} else if (entityType == '2') {
			$(".farmerDiv").show();
			$(".farmDiv").show();
			$(".inspectClass").hide();
			$(".farmCropDiv").hide();
			recordName = '<s:property value="farmList" />';

		} else if (entityType == '3') {
			$(".groupDiv").show();
			$(".inspectClass").hide();
			recordName = '<s:property value="group" />';
		}else if (entityType == '6') {
			$(".farmerDiv").show();
			$(".farmDiv").show();
			$(".farmCropDiv").show();
			$(".inspectClass").hide();
			recordName = '<s:property value="farmCropList" />';

		} 
		
		
	});
	
	function getTxnType() {
		//var txnType = '<s:property value="getTxnType()"/>';
		return "387";
	}
	function getEntityType() {
		var entityType = '<s:property value="getIcs_entityType"/>';
		return entityType;
	}
	function getId() {
		var type = '<s:property value="getIcs_farm_id()"/>';
		return type;
	}
	
    
    function getBranchIdDyn(){
   		if('<s:property value="getBranchId()"/>'==null ||'<s:property value="getBranchId()"/>'=='' ){
   			return '<s:property value="farm.farmer.branchId"/>';
   		}else{
   			return '<s:property value="getBranchId()"/>';
   		}
   	}
	
	
	function hideFields(){
		 var app = $(".aPanel");
		 $(app).addClass("hide");
		 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
		 	 $(this).addClass("hide");
		 });
		  
		}
	
	function showFields(type){
		jQuery.post("farmerReport_populateHideFn.action", {type:type}, function(result) {
			
			var farmerFieldsArray = jQuery.parseJSON(result);
			$.each(farmerFieldsArray, function(index, value) {
				if(index=='activeFields'){
					$(value).each(function(k,v){
						if(v.id=='1'){
							showByEleName(v.name);
						}if(v.id=='2'){
							showByEleId(v.name);
						}if(v.id=='3'){
							showByEleClass(v.name);
						}if(v.id=='4'){
							$("."+v.name).removeClass("hide");
							
						}
					});
				}
				else if(index=='destroyFileds'){
					$(value).each(function(k,v){
						$("."+v.name).remove();
					});
				}
			});
			$(".farmerDynamicField").removeClass("hide");
			
		});
		
		
		
		jQuery.post("farmerReport_populateHideFnFarm.action", {}, function(result) {
			var farmerHideFields = jQuery.parseJSON(result);
			
			$.each(farmerHideFields, function(index, value) {
				if(value.type=='1'){
					console.log(value.typeName);
					showByEleName(value.typeName);
				}if(value.type=='2'){
					showByEleId(value.typeName);
				}if(value.type=='3'){
					showByEleClass(value.typeName);
				}if(value.type=='4'){
					$("."+value.typeName).removeClass("hide");
					console.log(value.typeName);
				} if(value.type=='5'){
					hideByEleClass(value.typeName);
				}
			});
			
	    });
		
		
		jQuery.post("farmerReport_populateHideFnFarmCrops.action", {}, function(result) {
			var farmCropHideFields = jQuery.parseJSON(result);
			
			$.each(farmCropHideFields, function(index, value) {
				if(value.type=='1'){
					console.log(value.typeName);
					showByEleName(value.typeName);
				}if(value.type=='2'){
					showByEleId(value.typeName);
				}if(value.type=='3'){
					showByEleClass(value.typeName);
				}if(value.type=='4'){
					$("."+value.typeName).removeClass("hide");
					console.log(value.typeName);
				} if(value.type=='5'){
					hideByEleClass(value.typeName);
				}
			});
			
	    });
		
		var app = $("#ics_pdf").find(".aPanel");
		 $(app).removeClass("hide");
		 $(app).not(".farmerDynamicField").find(".dynamic-flexItem2").each(function(){
		 	 $(this).removeClass("hide");
		 });
		  
		
		
	}
	
	
	function get_Google_map_img() {
		var latitude = '<s:property value="farm.latitude"/>';
		var longitude = '<s:property value="farm.longitude"/>';
		
		/* var cen = "8.7694674, 125.1987755";
		var mark = "8.7694674,125.1987755"; */

		latitude  = "8.769334922333424";
		longitude  = "125.19931554794312";
		
			//URL of Google Static Maps.
	        var staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap";
	 
	        //Set the Google Map Center.
	        staticMapUrl += "?center="+latitude+', '+longitude;
	 
	        //Set the Google Map Size.
	        staticMapUrl += "&size=800x350";
	 
	        //Set the Google Map Zoom.
	        staticMapUrl += "&zoom=6";
	 
	        //Set the Google Map Type.
	        staticMapUrl += "&maptype=google.maps.MapTypeId.HYBRID";
	 
	        //Loop and add Markers.
	       // for (var i = 0; i < markers.length; i++) {
	            staticMapUrl += "&markers="+latitude+', '+longitude; //For red color marker

		//staticMapUrl += "&markers=color%3Agreen%7C%20"+latitude+', '+longitude; //For green color marker





	      //  }
	 		
	            staticMapUrl  += "&client=gme-sourcetrace&signature=BhP2Pt8O0REwO9140FmV-BJZMn8="; //For red color marker
		//staticMapUrl  += "&client=gme-sourcetrace&signature=__LM_tEMxHdffDxuHjTuUe2bCqc="; //For green color marker

	        
	        var final_url = staticMapUrl.toString();
	         var imgMap = document.getElementById("google_map_static_img");
	        imgMap.src = staticMapUrl;
	         convertasbinaryimage();
			console.log(final_url)
	    }
	
function convertasbinaryimage() {
    html2canvas(document.getElementById("google_map_static_img"), {
        useCORS: true,
        onrendered: function (canvas) {
            var img = canvas.toDataURL("image/png");
           
            img = img.replace('data:image/png;base64,', '');
            var finalImageSrc = 'data:image/png;base64,' + img;
            $('#google_map_static_img').attr('src', finalImageSrc);
            console.log("---------------");
            console.log(finalImageSrc);
            console.log("---------------");
         }
    });
}
	
function downloadPDF(){
	
	var modal = document.getElementById('loading_msg');
	modal.style.display = "inline-block";
	/* var farmer = '<s:property value="farmer.firstName" />';
	
	
	html2canvas($("#whole_pdf"), {
	    
		onrendered: function(canvas) {
	    	
			if(canvas.width > canvas.height){
				doc = new jsPDF('p', 'mm', [canvas.width, canvas.height]);
				}
				else{
				doc = new jsPDF('p', 'mm', [canvas.height, canvas.width]);
				}
			
			var whole_pdf = canvas.toDataURL("image/png");
	    	
	    	//var doc = new jsPDF("l", "pt", "a3");
	   	// doc.deletePage(1);
	   	 
	   	 
	   	// doc.addPage(Math.floor($('#whole_pdf').width()), Math.floor($('#whole_pdf').height()));
	   	// doc.addImage(whole_pdf, 'PNG', 0 , 0 );
	   	doc.addImage(whole_pdf, 'png', 10, 10, canvas.width, canvas.height);
	   	 
	   	 doc.save(farmer+".pdf");
	 	
		 modal.style.display = "none";
	    	
	    	
	    }
	});
	 */
	
	 var farmerPage;
	var farmPage;
	var farmCropsPage;
	var google_map_page;
	var icsPage;
	
	 
	
	html2canvas($("#farmer_pdf"), {
	    onrendered: function(canvas) {
	    	farmerPage = canvas.toDataURL("image/png");
	    	
	    	
	    	html2canvas($("#farm_pdf"), {
	    	    onrendered: function(canvas) {
	    	    	farmPage = canvas.toDataURL("image/png");
	    	    	
	    	    
	    	    	
	    	    	html2canvas($("#google_map_page"), {
	    	    	    onrendered: function(canvas) {
	    	    	    	google_map_page = canvas.toDataURL("image/png");
	    	    	    	
	    	    	    	
	    	    	    	html2canvas($("#farm_crops_pdf"), {
	    	    	    	    onrendered: function(canvas) {
	    	    	    	    	farmCropsPage = canvas.toDataURL("image/png");
	    	    	    	    	
	    	    	    	    	
	    	    	    	    	html2canvas($("#ics_pdf"), {
	    	    	    	    	    onrendered: function(canvas) {
	    	    	    	    	    	icsPage = canvas.toDataURL("image/png");
	    	    	    	    	    	createPdf(farmerPage,farmPage,google_map_page,farmCropsPage,icsPage);
	    	    	    	    	    	
	    	    	    	    	    	
	    	    	    	    	    }
	    	    	    	    	});
	    	    	    	    	
	    	    	    	    	
	    	    	    	    }
	    	    	    	});
	    	    	    	
	    	    	    	
				    	    }
				    	});
						
	    	    	
	    	    }
	    	});
	    	
	    	
	    }
	}); 
}




function createPdf(farmerPage,farmPage,google_map_page,farmCropsPage,icsPage){
	
	var modal = document.getElementById('loading_msg');
	var farmer = '<s:property value="farmer.firstName" />';
	
	
	var doc = new jsPDF("p", "pt", "a4");
	 doc.deletePage(1);
	 
	 
	 doc.addPage(Math.floor($('#farmer_pdf').width()), Math.floor($('#farmer_pdf').height()));
	 doc.addImage(farmerPage, 'PNG', 0 , 0 );
	 
	 
	 doc.addPage(Math.floor($('#farm_pdf').width()), Math.floor($('#farm_pdf').height()));
	 doc.addImage(farmPage, 'PNG', 0, 0);
	 
	  doc.addPage(Math.floor($('#google_map_page').width()), Math.floor($('#google_map_page').height()));
	  doc.addImage(google_map_page, 'PNG', 0, 0);
	 
	 doc.addPage(Math.floor($('#farm_crops_pdf').width()), Math.floor($('#farm_crops_pdf').height()));
	 doc.addImage(farmCropsPage, 'PNG', 0, 0);
	 
	 doc.addPage(Math.floor($('#ics_pdf').width()), Math.floor($('#ics_pdf').height()));
	 doc.addImage(icsPage, 'PNG', 0, 0);
	
		 doc.save(farmer+".pdf");
	
		 modal.style.display = "none";
}


function loadFarmMap(isCert,icsType) {
	try {
		var farmCoordinate = jQuery("#farmCordinates").val();
		
		var landArea = JSON.parse(farmCoordinate);
	
		loadMap('map', '<s:property value="farm.latitude"/>',
				'<s:property value="farm.longitude"/>', landArea,isCert,icsType);
		
	} catch (err) {
		console.log(err);
	}
}

var map;
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


function loadMap(mapDiv, latitude, longitude, landArea,isCert,icsType) {
	var intermediateImg;
	 var intermediatePointIcon;
	 var temp = "";
	 if(isCert==0){
			intermediateImg = "red_placemarker.png";
			 intermediatePointIcon = temp + '/img/'+intermediateImg;
		}else{
			if(icsType!=null &&icsType!='' ){
				if(isCert==1 && icsType==3 ) {
					intermediateImg = "green_placemarker.png";
					 intermediatePointIcon = temp + '/img/'+intermediateImg;
				}else {
					intermediateImg = "yellow_placemarker.PNG";
					 intermediatePointIcon = temp + '/img/'+intermediateImg;
				}
			}
			
			
		}
	 
	try {
		var bounds = new google.maps.LatLngBounds();
		if(!isEmpty(latitude)&&!isEmpty(longitude)){
			var marker;
			marker = new google.maps.Marker({
				position : new google.maps.LatLng(parseFloat(latitude),
						parseFloat(longitude)),
						icon:intermediatePointIcon,
				map : map
			});
		}
		
		if(landArea.length>0){
			var cords = new Array();
			$(landArea).each(function(k,v){
				cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
			/* 	marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(v.lat),
							parseFloat(v.lon)),
					map : map
				}); */
			});
			
			
			
			 var plotting = new google.maps.Polygon({
		          paths: cords,
		          strokeColor: '#FF0000',
		          strokeOpacity: 0.8,
		          strokeWeight: 2,
		          fillColor: '#E7D874',
		          fillOpacity: 0.35
		        });
			 plotting.setMap(map);
			
			 bounds.extend(new google.maps.LatLng(parseFloat(landArea[landArea.length-1].lat),parseFloat(landArea[landArea.length-1].lon)));
			 map.fitBounds(bounds);
			 
			 var listener = google.maps.event.addListener(map, "idle", function () {
				    map.setZoom(16);
				    google.maps.event.removeListener(listener);
				});
		}
		
	} catch (err) {
		console.log(err);
	}
}


function hideByEleName(name){
	$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).addClass( "hide" );
}

function showByEleName(name){
	$('[name="'+name+'"]').closest( ".dynamic-flexItem2" ).removeClass( "hide" );
}

function hideByEleClass(className){
	$("."+className).closest( ".dynamic-flexItem2" ).addClass( "hide" );
}

function showByEleClass(className){
	$("."+className).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
	$("."+className).parent().removeClass( "hide" );
}

function hideByEleId(id){
	$("#"+id).closest( ".dynamic-flexItem2" ).addClass( "hide" );
}

function showByEleId(id){
	$("#"+id).closest( ".dynamic-flexItem2" ).removeClass( "hide" );
}

function getBranchIdDyn(){
	if('<s:property value="getBranchId()"/>'==null ||'<s:property value="getBranchId()"/>'=='' ){
		return '<s:property value="farmer.branchId"/>';
	}else{
		return '<s:property value="getBranchId()"/>';
	}
}

function printPage(){
/* 	
	$(".dynamicFieldsRender").find(".formContainerWrapper").find("table").find("tbody").find("tr").find("th").attr({
		"width":"200"
	}) */
	
	var latitude = '<s:property value="farm.latitude" />';
	var longitude = '<s:property value="farm.longitude" />';
	
    var title = document.title;
    document.title = "";
    
	var farmer = document.getElementById('farmer_pdf').innerHTML;
	var emptyDiv1 = '<div style="height:450px;"></div>';
	var farm = document.getElementById('farm_pdf').innerHTML;
	var emptyDiv2 = '<div style="height:200px;"></div>';
	var googleMap = document.getElementById('google_map_page').innerHTML;
	if(!isEmpty(latitude) && !isEmpty(longitude)){
		var emptyDiv3 = '<div style="height:500px;"></div>';
	}else{
		var emptyDiv3 = '<div style="height:800px;"></div>';
	}
	var farm_crops_pdf = document.getElementById('farm_crops_pdf').innerHTML;
	var emptyDiv4 = '<div style="height:700px;"></div>';
	//var ics_static_fields = document.getElementById('ics_static_fields').innerHTML;
	//var ics_dynamic_fields = document.getElementsByClassName('dynamicFieldsRender')[0].innerHTML;
	var ics_pdf = document.getElementById('ics_pdf').innerHTML;
	
	
	var originalContents = document.body.innerHTML;
	document.body.innerHTML = "";
	
	$(document.body).append(farmer);
	$(document.body).append(emptyDiv1);
	$(document.body).append(farm);
	$(document.body).append(emptyDiv2);
	$(document.body).append(googleMap);
	$(document.body).append(emptyDiv3);
	$(document.body).append(farm_crops_pdf);
	$(document.body).append(emptyDiv4);
	$(document.body).append(ics_pdf);
	
	
	window.print();
	document.body.innerHTML = originalContents;
	renderDynamicDetailFeildsByTxnType();
	
	document.title = title;  
}

</script>



	<!-- MODAL -->
	<div id="loading_msg" class="process_msg">

		<!-- Modal content -->
		<div class="process_msg-content_loading_msg">
			<h3>Processing ...</h3>
		</div>

	</div>
	<!--  -->

	<div style="text-align:right;">
		<button onclick="downloadPDF()" class="btn btn-warning">Download
			PDF</button>
		<button onclick="printPage()" class="btn btn-danger">Print</button>
	</div>


<div id="whole_pdf">
	<!-- Farmer page start -->

	<div id="farmer_pdf">

		

		<div class="flex-view-layout">
			<div class="fullwidth fullheight">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper marginBottom">

						<div class="head">
							<div class="aTitle">
								<h2>Farmer profile</h2>
							</div>
						</div>

						
						<table>
							<tbody>
								<tr>
									
										<table>
											<tbody>
												<tr>
													<td  valign="top" class="farmerPro">
														<div class="formContainerWrapper">

															<div class="aPanel farmer_info" >
											<div class="aTitle">
												<h2>
													<s:property value="%{getLocaleProperty('info.farmer')}" />
													<div class="pull-right">
														<a class="aCollapse" href="#"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con">
												<s:if test='branchId==null'>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="app.branch" />
														</p>
														<p class="flexItem">
															<s:property value="%{getBranchName(farmer.branchId)}" />
															&nbsp;
														</p>
													</div>
												</s:if>
										 	<%-- 	<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="Created User Name/Mobile User Name" />
													</p>
													<p class="flexItem">
														<s:property value="farmer.createdUsername" />
													</p>
												</div>  --%>
												
											 	<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('Created User Name/Mobile User Name')}" />
													</p>
													<p class="flexItem" name="farmer.createdUsername">
														<s:property value="farmer.createdUsername" />
													</p>
												</div> 

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.enrollmentPlace" />
													</p>
													<p class="flexItem" name="farmer.enrollmentPlace">
														<s:property value="enrollmentMap[farmer.enrollmentPlace]" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.formFilledBy')}" />
													</p>
													<p class="flexItem" name="farmer.formFilledBy">
														<s:property value="farmer.formFilledBy" />
														&nbsp;
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.assess')}" />
													</p>
													<p class="flexItem" name="farmer.assess">
														<s:property value="farmer.assess" />
														&nbsp;
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.placeOfAsss')}" />
													</p>
													<p class="flexItem" name="farmer.placeOfAsss">
														<s:property value="farmer.placeOfAsss" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmer.objective')}" />
													</p>
													<p class="flexItem" name="farmer.objective">
														<s:property value="farmer.objective" />
														&nbsp;
													</p>
												</div>

												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:if
															test="currentTenantId=='pratibha' && getBranchId()=='organic'">
															<s:property value="%{getLocaleProperty('tracenet')}" />
														</s:if>
														<s:else>
															<s:property value="%{getLocaleProperty('farmerCode')}" />
														</s:else>
														<%-- <s:property
															value="%{getLocaleProperty('farmer.farmerCode')}" /> --%>
													</p>
													<p class="flexItem" name="farmer.farmerCode">
														<s:property value="farmer.farmerCode" />
													</p>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem" name="dateOfJoining">
														<s:text name="farmer.dateOfJoin" />
													</p>
													<p class="flexItem">
														<s:property value="dateOfJoining" />
													</p>
												</div>

												<div class="dynamic-flexItem2 certified">
													<p class="flexItem">
														<s:text name="farmer.isCertified" />
													</p>
													<p class="flexItem" name="farmer.isCertifiedFarmer">
														<s:property
															value="isFarmerCertified[farmer.isCertifiedFarmer]" />
													</p>
												</div>
												<s:if test="farmer.isCertifiedFarmer==1">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="certification.type" />
														</p>
														<p class="flexItem" name="farmer.certificationType">
															<s:property
																value="certificationTypes[farmer.certificationType]" />
														</p>
													</div>

													<div class="dynamic-flexItem2 icsFieldSelect">
														<p class="flexItem">
															<s:text name="farmer.icsName" />
														</p>
														<p class="flexItem" name="farmer.icsName">
															<s:property value="farmer.icsName" />

														</p>
													</div>

													<div class="dynamic-flexItem2 icsFieldTxt">
														<p class="flexItem">
															<s:text name="farmer.icsName" />
														</p>
														<p class="flexItem" name="farmer.icsName">
															<s:property value="icsName" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.icsCode" />
														</p>
														<p class="flexItem" name="farmer.icsCode">
															<s:property value="icsCode" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.icsUnitNumber" />
														</p>
														<p class="flexItem" name="farmer.icsUnitNo">
															<s:property value="icsUnit[farmer.icsUnitNo]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.regNo" />
														</p>
														<p class="flexItem" name="farmer.icsTracenetRegNo">
															<s:property value="icsRegNo[farmer.icsTracenetRegNo]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.codeIcs" />
														</p>
														<p class="flexItem" name="farmer.farmerCodeByIcs">
															<s:property value="farmer.farmerCodeByIcs" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.codeTracenet" />
														</p>
														<p class="flexItem" name="farmer.farmersCodeTracenet">
															<s:property value="farmer.farmersCodeTracenet" />
														</p>
													</div>


													<s:if test="currentTenantId=='olivado'">

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="Created User Name/Mobile User Name" />
															</p>
															<p class="flexItem">
																<s:property value="farmer.createdUsername" />
															</p>
														</div>
														<div class="dynamic-flexItem2 ggn">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.codeIcs')}" />
															</p>
															<p class="flexItem" name="farmer.farmerCodeByIcs">
																<s:property value="farmer.farmerCodeByIcs" />
															</p>
														</div>

														<div class="dynamic-flexItem2 ggnRegNo">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.codeTracenet')}" />
															</p>
															<p class="flexItem" name="farmer.farmersCodeTracenet">
																<s:property value="farmer.farmersCodeTracenet" />
															</p>
														</div>

													</s:if>
												</s:if>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farmer.beneficiary" />
													</p>
													<p class="flexItem" name="farmer.isBeneficiaryInGovScheme">
														<s:if test="farmer.isBeneficiaryInGovScheme==1">
															<s:text name="Yes" />
														</s:if>
														<s:elseif test='farmer.isBeneficiaryInGovScheme=="2"'>
															<s:text name="No" />
														</s:elseif>
														<s:else>
															<s:text name="" />
														</s:else>
													</p>

												</div>

												<div class="dynamic-flexItem2">
													<s:if test="farmer.isBeneficiaryInGovScheme==1">
														<p class="flexItem">
															<s:text name="farmer.nameOfScheme" />
														</p>
														<p class="flexItem" name="farmer.nameOfTheScheme">

															<s:property value="farmer.nameOfTheScheme" />

														</p>
													</s:if>
												</div>


												<div class="dynamic-flexItem2">
													<s:if test="farmer.isBeneficiaryInGovScheme==1">
														<p class="flexItem">
															<s:text name="farmer.governmentDepartment" />
														</p>
														<p class="flexItem" name="farmer.govtDept">

															<s:property value="farmer.govtDept" />

														</p>
													</s:if>
												</div>


												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farmer.regYear')}" />
													</p>
													<p class="flexItem" name="farmer.yearOfICS">
														<s:if test='farmer.yearOfICS!="-1"'>
															<s:property value="farmer.yearOfICS" />
														</s:if>
													</p>
												</div>
												<%-- <s:if test="farmer.isDisable!=null">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
													<s:property
															value="%{getLocaleProperty('isDisabled')}" />
													</p>
													<p class="flexItem" name="selectedDisabled">
														<s:text	name='%{"dynamicRadio"+farmer.isDisable}' />
													</p>
												</div>
												</s:if>	 --%>

											</div>
										</div>
				
														</div>
				
				
				
				
													</td>
				
													<td >
				
														<div id="" class="farmer-photo" style="padding: 10px;float: right;">
															<s:if
																test='farmerImageByteString!=null && farmerImageByteString!=""'>
																<img border="0" 
																	src="data:image/png;base64,<s:property value="farmerImageByteString"/>" width="100%">
																	
	
															</s:if>
															<s:else>
																<img align="middle" border="0" src="img/no-image.png">
															</s:else>
														</div>
				
													</td>

												</tr>
											</tbody>
										</table>
								</tr>

								<tr>

									<td  >
										<div class="formContainerWrapper">
											<div class="aPanel pers_info">
												<div class="aTitle">
													<h2>
														<s:text name="info.personal" />
														<div class="pull-right">
															<a class="aCollapse" href="#"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>

												<div class="aContent dynamic-form-con">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.firstName')}" />
														</p>
														<p class="flexItem" name="farmer.firstName">
															<s:property value="farmer.firstName" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.lastName')}" />
														</p>
														<p class="flexItem" name="farmer.lastName">
															<s:property value="farmer.lastName" />
														</p>
													</div>
													<s:if test="currentTenantId!='olivado'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.surName')}" />
															</p>
															<p class="flexItem" name="farmer.surName">
																<s:property value="farmer.surName" />
															</p>
														</div>
													</s:if>
													<div class="dynamic-flexItem2 hideDob">
														<p class="flexItem">
															<s:text name="farmer.dateOfBirth" />
														</p>
														<p class="flexItem" name="calendar">
															<s:property value="dateOfBirth" />
														</p>
													</div>

													<s:if test="currentTenantId!='olivado'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('farmer.age1')}" />
															</p>
															<p class="flexItem" name="age">
																<s:property value="farmer.age" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.gender" />
														</p>
														<p class="flexItem" name="farmer.gender">
															<s:text name='%{farmer.gender}' />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="chetnaCategory" />
														</p>
														<p class="flexItem" name="farmer.category">
															<s:property value="farmer.category" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="castename" />
														</p>
														<p class="flexItem" name="farmer.casteName">
															<s:property value="farmer.casteName" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="education" />
														</p>
														<p class="flexItem" name="farmer.education">
															<s:property value="educationList[farmer.education]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="marital.Status" />
														</p>
														<p class="flexItem" name="farmer.maritalSatus">
															<s:property value="farmer.maritalSatus" />
														</p>
													</div>
													<s:if test='idProofEnabled==1'>

														<s:if test="currentTenantId !='efk'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="farmer.idProof" />
																</p>
																<p class="flexItem" name="farmer.idProof">
																	<s:property value="proofList[farmer.idProof]" />
																</p>

															</div>
														</s:if>
														<s:if test="farmer.idProof=='99'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="farmer.otherProof" />
																</p>
																<p class="flexItem" name="farmer.idProof">
																	<s:property value="farmer.otherIdProof" />
																</p>
															</div>
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farmer.ProofNo')}" />
																</p>
																<p class="flexItem" name="farmer.idProof">
																	<s:property value="farmer.proofNo" />
																</p>

															</div>
														</s:if>
														<s:else>
															<div class="dynamic-flexItem2">
																<p class="flexItem" name="farmer.proofNo">
																	<s:property
																		value="%{getLocaleProperty('farmer.ProofNo')}" />
																</p>
																<p class="flexItem" name="farmer.idProof">
																	<s:property value="farmer.proofNo" />
																</p>
															</div>
														</s:else>
														<s:if test='idProofEnabled==1 && IdImgAvil=="1"'>
															<div class="dynamic-flexItem2">
																<p class="flexItem" name="farmer.idProofImg">
																	<s:property
																		value="%{getLocaleProperty('farmer.idProofImg')}" />
																</p>
																<p class="flexItem" name="farmer.idProof">
																	<button type='button'
																		class='btn btn-sm pull-right photo'
																		style='margin-right: 15%'
																		onclick="enableFarmerPhotoModal(<s:property value="farmer.id"/>)">
																		<i class='fa fa-picture-o' aria-hidden='true'></i>
																	</button>
																</p>
															</div>
														</s:if>
													</s:if>
													
													<s:if test="currentTenantId!='sagi'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.adhaarNo" />
															</p>
															<p class="flexItem" name="farmer.adhaarNo">
																<s:property value="farmer.adhaarNo" />
															</p>
														</div>
													</s:if>

													<s:if test="currentTenantId=='atma'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.socialCategory" />
															</p>
															<p class="flexItem" name="farmer.socialCategory">
																<s:property
																	value="socialCategoryList[farmer.socialCategory]" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.religion" />
														</p>
														<s:if test="farmer.religion=='99'">
															<p class="flexItem" name="farmer.religion">
																<s:property value="religionList[farmer.religion]" />
															</p>
														</s:if>
														<s:else>
															<p class="flexItem" name="farmer.religion">
																<s:property value="religionList[farmer.religion]" />
															</p>
														</s:else>
													</div>
													<s:if test="currentTenantId!='sagi'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farmer.typeOfFamily" />
															</p>
															<p class="flexItem" name="farmer.typeOfFamily">
																<s:property value="familyTypeList[farmer.typeOfFamily]" />
															</p>
														</div>
														<s:if test="farmer.sangham=='01'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="houseHoldLandWet" />
																</p>
																<p class="flexItem" name="farmer.houseHoldLandWet">
																	<s:property value="farmer.houseHoldLandWet" />
																</p>
															</div>

															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="houseHoldLandDry" />
																</p>
																<p class="flexItem" name="farmer.houseHoldLandDry">
																	<s:property value="farmer.houseHoldLandDry" />
																</p>
															</div>
														</s:if>
													</s:if>

													<div class="dynamic-flexItem2 prefWrk">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.preferenceWork')}" />
														</p>
														<p class="flexItem" name="farmer.prefWrk">
															<s:property value="farmer.prefWrk" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('status')}" />
														</p>
														<p class="flexItem" name="farmer.personalStatus">
															<s:property value="statusList[farmer.personalStatus]" />
														</p>
													</div>

												</div>
											</div>
										</div>
									</td>

								</tr>

								<tr>
									<td>
										<div class="formContainerWrapper">

											<div class="aPanel contact_info">
												<div class="aTitle">
													<h2>
														<s:text name="info.contact" />
														<div class="pull-right">
															<a class="aCollapse" href="#"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.address')}" />
														</p>
														<p class="flexItem" name="farmer.address">
															<s:property value="farmer.address" />
														</p>
													</div>
													<s:if test="currentTenantId=='olivado'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.surName')}" />
															</p>
															<p class="flexItem" name="farmer.surName">
																<s:property value="farmer.surName" />
															</p>
														</div>
													</s:if>
													<s:if test="currentTenantId=='welspun'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farmer.pinCode')}" />
															</p>
															<p class="flexItem" name="farmer.pinCode">
																<s:property value="farmer.pinCode" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.mobileNumber')}" />
														</p>
														<p class="flexItem" name="farmer.mobileNumber">
															<s:property value="farmer.mobileNumber" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.email" />
														</p>
														<p class="flexItem" name="farmer.email">
															<s:property value="farmer.email" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="country.name" />
														</p>
														<p class="flexItem" name="selectedCountry">
															<s:property
																value="farmer.village.city.locality.state.country.code" />
															&nbsp-&nbsp
															<s:property
																value="farmer.village.city.locality.state.country.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('state.name')}" />
														</p>
														<p class="flexItem" name="selectedState">
															<s:property
																value="farmer.village.city.locality.state.code" />
															&nbsp-&nbsp
															<s:property
																value="farmer.village.city.locality.state.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('locality.name')}" />
														</p>
														<p class="flexItem" name="selectedLocality">
															<s:property value="farmer.village.city.locality.code" />
															&nbsp-&nbsp
															<s:property value="farmer.village.city.locality.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('city.name')}" />
														</p>
														<p class="flexItem" name="selectedCity">
															<s:property value="farmer.village.city.code" />
															&nbsp-&nbsp
															<s:property value="farmer.village.city.name" />
														</p>
													</div>

													<s:if test="gramPanchayatEnable==1">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('panchayat.name')}" />
															</p>
															<p class="flexItem" name="selectedPanchayat">
																<s:property value="farmer.village.gramPanchayat.code" />
																&nbsp-&nbsp
																<s:property value="farmer.village.gramPanchayat.name" />
															</p>
														</div>
													</s:if>
													

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('village.name')}" />
														</p>
														<p class="flexItem" name="selectedVillage">
															<s:property value="farmer.village.code" />
															&nbsp-&nbsp
															<s:property value="farmer.village.name" />
														</p>
													</div>

													<s:if test="currentTenantId=='atma'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('profile.sangham')}" />
															</p>
															<p class="flexItem" name="selectedSangham">
																<s:property value="farmer.sangham" />
															</p>
														</div>
													</s:if>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('profile.samithi')}" />
														</p>
														<p class="flexItem" name="selectedSamithi">
															<s:property value="farmer.samithi.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('profile.groupPosition')}" />
														</p>
														<p class="flexItem" name="selectedGroupPosition">
															<s:property value="farmer.positionGroup" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('fpoGroup')}" />
														</p>
														<p class="flexItem" name="farmer.fpo">
															<s:property value="fpo[farmer.fpo]" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmer.phoneNumber')}" />
														</p>
														<p class="flexItem" name="farmer.phoneNumber">
															<s:property value="farmer.phoneNumber" />
														</p>
													</div>
													<s:if test="currentTenantId=='wilmar'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('selectedMasterData')}" />
															</p>
															<p class="flexItem" name="farmer.masterData">
																<s:property value="farmer.masterData" />
															</p>
														</div>
													</s:if>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmer.status" />
														</p>
														<p class="flexItem" name="farmer.status">
															<s:text name='%{"status"+farmer.status}' />
														</p>
													</div>
												</div>
											</div>

										</div>

									</td>
								</tr>

							</tbody>

						</table>
						
					</div>
				</div>
			</div>
		</div>

	</div>

	<!-- Farmer Page Finished -->


	<!-- Farm Page start -->

	<div id="farm_pdf">

		

		<div class="flex-view-layout">
			<div class="fullwidth fullheight">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper marginBottom">

						<div class="head">
							<div class="aTitle">
								<h2>Farm profile</h2>
							</div>
						</div>
	
							<table>
								<tbody>
	
									<tr>
	
										<td>

										<div class="formContainerWrapper">
											<div class="aPanel farmInfo">
												<div class="aTitle">
													<h2>
														<s:text name="farmdetail" />
														<div class="pull-right">
															<a class="aCollapse" href="#farmDetailsAccordian"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con"
													id="farmDetailsAccordian">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('farm.farmName')}" />
														</p>
														<p class="flexItem" name="farm.farmName">
															<s:property value="farm.farmName" />
															&nbsp;
														</p>
													</div>
													<s:if test="currentTenantId=='welspun'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('8A/7/12')}" />
															</p>
															<p class="flexItem"
																name="farm.farmDetailedInfo.processingActivity">
																<s:property
																	value="processingActList[farm.farmDetailedInfo.processingActivity]" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farm.farmerName" />
														</p>
														<p class="flexItem" name="farm.farmerName">
															<s:property value="farm.farmer.firstName" />
															&nbsp;
															<s:property value="farm.farmer.lastName" />
														</p>
													</div>
													<s:if test="currentTenantId!='wilmar'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.surveyNo" />
															</p>
															<p class="flexItem"
																name="farm.farmDetailedInfo.surveyNumber">
																<s:property value="farm.farmDetailedInfo.surveyNumber" />
															</p>
														</div>
													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farm.farmAddress" />
														</p>
														<p class="flexItem"
															name="farm.farmDetailedInfo.farmAddress">
															<s:property value="farm.farmDetailedInfo.farmAddress" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="%{getLocaleProperty('farm.totalLand')}" />
															(
															<s:property value="%{getAreaType()}" />
															)

														</p>
														<p class="flexItem"
															name="farm.farmDetailedInfo.totalLandHolding">
															<s:property
																value="farm.farmDetailedInfo.totalLandHolding" />
														</p>
													</div>
													<%-- 	<s:if test="currentTenantId!='crs'">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.totalLandHectare" />
								</p>
								<p class="flexItem" name="landHectValues">
								<div id="landHectValues"></div>


							</div>
							</s:if> --%>

													<div class="dynamic-flexItem2 plantArea pratibhaHide">
														<p class="flexItem">
															<s:text
																name="%{getLocaleProperty('farm.proposedPlantingArea')}" />
															(
															<s:property value="%{getAreaType()}" />
															)
														</p>
														<p class="flexItem"
															name="farm.farmDetailedInfo.proposedPlantingArea">
															<s:property
																value="farm.farmDetailedInfo.proposedPlantingArea" />
														</p>
													</div>
													<%-- 	<s:if test="currentTenantId!='crs'">
							<div class="dynamic-flexItem2 pratibhaHide">
								<p class="flexItem">
									<s:text
										name="%{getLocaleProperty('farm.proposedPlantingHectare')}" />
								</p>
								<p class="flexItem" name="plantHectValues">
								<div id="plantHectValues"></div>
								</p>

							</div></s:if> --%>

													<s:if test="currentTenantId=='welspun'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('ownLand')}" />
															</p>
															<p class="flexItem" name="farm.ownLand">

																<s:property value="farm.ownLand" />
															</p>
														</div>
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('leasedland')}" />
															</p>
															<p class="flexItem" name="farm.leasedLand">

																<s:property value="farm.leasedLand" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('irriLand')}" />
															</p>
															<p class="flexItem" name="farm.irrigationLand">

																<s:property value="farm.irrigationLand" />
															</p>
														</div>
													</s:if>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farm.landOwned')}" />
														</p>
														<p class="flexItem" name="selectedFarmOwned">
															<s:property value="farmOwnedDetail" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farm.landGradient')}" />
														</p>
														<p class="flexItem" name="selectedGradient">
															<s:property value="landGradientDetail" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farm.appRoad" />
														</p>
														<p class="flexItem" name="selectedRoad">
															<s:property value="selectedRoadString" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('farm.certYear')}" />
														</p>
														<p class="flexItem" name="farm.certYear">
															<s:property value="farm.certYear" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farm.farmCode" />
														</p>
														<p class="flexItem" name="farm.farmCode">
															<s:property value="farm.farmCode" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('farm.topo')}" />
														</p>
														<p class="flexItem" name="farm.landTopology">
															<s:if
																test='farm.landTopology!="-1" || farm.landTopology!="null"|| farm.landTopology!=""'>
																<s:property value="farm.landTopology" />
															</s:if>
														</p>
													</div>

													<s:if test="currentTenantId=='efk'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farm.waterSourceList')}" />
															</p>
															<p class="flexItem" name="farm.waterSource">
																<s:property value="selectedWaterSource" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farm.localNameOfCrotenTree')}" />
															</p>
															<p class="flexItem" name="farm.localNameOfCrotenTree">
																<s:property value="farm.localNameOfCrotenTree" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
															</p>
															<p class="flexItem" name="farm.NoOfCrotenTrees">
																<s:property value="farm.NoOfCrotenTrees" />
															</p>
														</div>

													</s:if>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farmRegNo" />
														</p>
														<p class="flexItem" name="farm.farmRegNumber">
															<s:property value="farm.farmRegNumber" />
														</p>
													</div>
													<div class="dynamic-flexItem2 waterHarvest">
														<p class="flexItem">
															<s:text name="farm.waterHarvest" />
														</p>
														<p class="flexItem" name="farm.waterHarvest">
															<s:property value="waterHarvests" />
														</p>
													</div>
													<s:if test="currentTenantId!='welspun'">
														<div class="dynamic-flexItem2 avgStore">
															<p class="flexItem">
																<s:text name="avgStore" />
															</p>
															<p class="flexItem" name="farm.avgStore">
																<s:property value="farm.avgStore" />
															</p>
														</div>
														<div class="dynamic-flexItem2 tree">
															<p class="flexItem">
																<s:text name="treeName" />
															</p>
															<p class="flexItem" name="farm.treeName">
																<s:property value="farm.treeName" />
															</p>
														</div>
													</s:if>
													<s:if test="currentTenantId=='wilmar'">
														<div class="dynamic-flexItem2 tree">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
															</p>
															<p class="flexItem" name="farm.distanceProcessingUnit">
																<s:property value="farm.distanceProcessingUnit" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('processingActivity')}" />
															</p>
															<p class="flexItem"
																name="farm.farmDetailedInfo.processingActivity">
																<s:property
																	value="processingActList[farm.farmDetailedInfo.processingActivity]" />
															</p>
														</div>

														<div class="dynamic-flexItem2 organicStatusDiv">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('organicStatus')}" />
															</p>
															<p class="flexItem" name="farm.farmIcsConv.organicStatus">
																<s:property value="farm.organicStatus" />
															</p>
														</div>
													</s:if>
													<s:if test="currentTenantId=='pratibha'">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.PlatNo" />
															</p>
															<p class="flexItem" name="farm.farmPlatNo">
																<s:property value="farm.farmPlatNo" />
															</p>
														</div>
													</s:if>

													<%-- <div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.regYear" />
								</p>
								<p class="flexItem" name="farm.regYear">
									<s:property value="farm.farmDetailedInfo.regYear" />
								</p>
							</div> --%>

												</div>
											</div>

											<div class="aPanel hide contactInfo">
												<div class="aTitle">
													<h2>
														<s:text name="info.contact" />
														<div class="pull-right">
															<a class="aCollapse" href="#farmLocationInfo"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con" id="farmLocationInfo">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('locality.name')}" />
														</p>
														<p class="flexItem" name="selectedLocality">
															<s:property value="farm.village.city.locality.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('city.name')}" />
														</p>
														<p class="flexItem" name="selectedCity">
															<s:property value="farm.village.city.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('panchayat.name')}" />
														</p>
														<p class="flexItem" name="selectedPanchayat">
															<s:property value="farm.village.gramPanchayat.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('village.name')}" />
														</p>
														<p class="flexItem" name="farm.farmName">
															<s:property value="farm.village.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('samithi.name')}" />
														</p>
														<p class="flexItem" name="selectedSamithi">
															<s:property value="farm.samithi.name" />
														</p>
													</div>

													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:property value="%{getLocaleProperty('fpoGroup')}" />
														</p>
														<p class="flexItem" name="selectedFpo">
															<s:property value="fpo[farm.fpo]" />
														</p>
													</div>

												</div>
											</div>
											<s:if
												test="currentTenantId!='wilmar' && currentTenantId!='griffith' ">
												<div class="aPanel soilIrrigationInfo">
													<div class="aTitle">
														<h2>
															<s:text name="info.soil" />
															<div class="pull-right">
																<a class="aCollapse" href="#farmSoilAccordian"><i
																	class="fa fa-chevron-right"></i></a>
															</div>
														</h2>
													</div>
													<div class="aContent dynamic-form-con"
														id="farmSoilAccordian">
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.soilType" />
															</p>
															<p class="flexItem" name="selectedSoilType">
																<s:property value="soilTypeDetail" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.soilTexture" />
															</p>
															<p class="flexItem" name="selectedTexture">
																<s:property value="soilTextureDetail" />
															</p>
														</div>

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.soilFertilityStatus" />
															</p>
															<p class="flexItem" name="selectedSoilFertility">
																<s:property value="soilFertilityDetail" />
															</p>
														</div>


														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.farmIrrigationSource" />
															</p>
															<p class="flexItem" name="selectedIrrigation">
																<s:property value="farmIrrigationDetail" />
															</p>
														</div>
														<s:if test="isIrrigation==1">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="farm.farmIrrigationType" />
																</p>
																<p class="flexItem" name="selectedIrrigationSource">
																	<s:property value="irrigationSourceDetail" />
																</p>
															</div>
														</s:if>
														<s:if test="farm.farmDetailedInfo.irrigationSource==99">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="farm.irrigatedOther" />
																</p>
																<p class="flexItem" name="selectedIrrigationSource">
																	<s:property
																		value="farm.farmDetailedInfo.irrigatedOther" />
																</p>
															</div>
														</s:if>
														<s:if test="currentTenantId=='welspun'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
																</p>
																<p class="flexItem" name="farm.NoOfCrotenTrees">
																	<s:property value="farm.NoOfCrotenTrees" />
																</p>
															</div>

															<div class="dynamic-flexItem2 tree">
																<p class="flexItem">
																	<s:property value="%{getLocaleProperty('treeName')}" />
																</p>
																<p class="flexItem" name="farm.treeName">
																	<s:property value="farm.treeName" />
																</p>
															</div>
															<div class="dynamic-flexItem2 tree">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
																</p>
																<p class="flexItem" name="farm.distanceProcessingUnit">
																	<s:property value="farm.distanceProcessingUnit" />
																</p>
															</div>
															<div class="dynamic-flexItem2 avgStore">
																<p class="flexItem">
																	<s:property value="%{getLocaleProperty('avgStore')}" />
																</p>
																<p class="flexItem" name="farm.avgStore">
																	<s:property value="farm.avgStore" />
																</p>
															</div>
														</s:if>
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="farm.methodOfIrrigation" />
															</p>
															<p class="flexItem" name="selectedMethodOfIrrigation">
																<s:property value="methodOfIrrigationDetail" />
															</p>
														</div>
														<s:if test="currentTenantId!='welspun'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:property
																		value="%{getLocaleProperty('farm.boreWellRechargeStructure')}" />
																</p>
																<p class="flexItem"
																	name="farm.farmDetailedInfo.boreWellRechargeStructure">
																	<s:property
																		value="borewellList[farm.farmDetailedInfo.boreWellRechargeStructure]" />
																</p>
															</div>
														</s:if>
													</div>
												</div>
											</s:if>

											<div class="aPanel landMarkInfo">
												<div class="aTitle">
													<h2>
														<s:text name="farm.landmark" />
														<div class="pull-right">
															<a class="aCollapse" href="#farmLandMarkAccordian"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con" id="labourAccordian">
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="farm.landmark" />
														</p>
														<p class="flexItem" name="farm.farmName">
															<s:property value="farm.landmark" />
														</p>
													</div>
												</div>
											</div>
											<%-- <div class="aPanel conversionInfo">
						<div class="aTitle">
							<h2>
								<s:text name="info.conversion" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmConversionAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con"
							id="farmConversionAccordian">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.lastDateofChemicalApply" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property
										value="farm.farmDetailedInfo.lastDateOfChemicalApplication" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalLands')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.conventionalLand" />
								</p>
							</div>


							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property value="%{getLocaleProperty('farm.fallowLand')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.fallowOrPastureLand" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalCrops')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property value="farm.farmDetailedInfo.conventionalCrops" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.conventionalEstimatedYields')}" />
								</p>
								<p class="flexItem" name="farm.farmName">
									<s:property
										value="farm.farmDetailedInfo.conventionalEstimatedYield" />
								</p>
							</div>

						</div>
					</div> --%>

											<%-- <div class="aPanel conversionStatus" style="width: 100%">
						<div class="aTitle">
							<h2>
								<s:property value="%{getLocaleProperty('info.ics')}" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmICSAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con"
							id="farmConversionAccordian">
							<s:iterator value="farm.farmICSConversion">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="conversionStatus" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:text name='%{"icsStatus"+icsType}' />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('inspectionDate')}" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<td><s:property value="inspectionDateString" /></td>
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="nameOfInspector" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:property value="inspectorName" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('qualified')}" />
									</p>
									<p class="flexItem" name="farm.farmName">

										<s:text name='%{"inspe"+qualified}' />
									</p>
								</div>
								<s:iterator value="farm.farmICSConversion">
									<s:if test='qualified=="2"'>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('sanctionReason')}" />
											</p>
											<p class="flexItem" name="farm.farmName">

												<s:property value="sanctionreason" />
											</p>
										</div>
										<div class="dynamic-flexItem2">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('durationOfSanction')}" />
											</p>
											<p class="flexItem" name="farm.farmName">

												<s:property value="sanctionDuration" />
											</p>
										</div>
									</s:if>
								</s:iterator>
							</s:iterator>
						</div>
					</div> --%>

											<s:if test="enableSoliTesting==1">
												<div class="aPanel">
													<div class="aTitle">
														<h2>
															<s:text name="info.soilTesting" />
															<div class="pull-right">
																<a class="aCollapse" href="#soilTestAcc"><i
																	class="fa fa-chevron-right"></i></a>
															</div>
														</h2>
													</div>
													<div class="dynamic-flexItem2">
														<p class="flexItem">
															<s:text name="soliTesting" />
														</p>
														<p class="flexItem">
															<s:if
																test='farm.soilTesting=="1" || farm.soilTesting=="2" '>
																<s:text name='%{"SOILTEST"+farm.soilTesting}' />
															</s:if>
															<s:else>
																<s:property value="farm.soilTesting" />
															</s:else>
														</p>
													</div>
													<s:if test='farm.soilTesting=="1"'>
														<s:if test="farm.docUpload.size()>0">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="fileName" />
																</p>
																<s:iterator value="farm.docUpload">
																	<p class="flexItem" name="name">
																		<s:property value="name" />
																	</p>
																</s:iterator>
															</div>

															<div>
																<p class="flexItem">
																	<s:text name="download" />
																</p>
																<s:iterator value="farm.docUpload">
																	<button type="button" class="btn btn-default"
																		aria-label="Left Align"
																		onclick='downloadDocument(<s:property value="id" />)'>
																		<span class="glyphicon glyphicon-download-alt"
																			aria-hidden="true"></span>
																	</button>
																</s:iterator>
															</div>
														</s:if>
													</s:if>
													<%-- <s:if test='farm.soilTesting=="1"'>
								<s:if test="farm.docUpload.size()>0">
									<div class="dynamic-flexItem2">
										<p class="flexItem">
											<s:text name="fileName" />
										</p>
										<p class="flexItem">
											<s:text name="download" />
										</p>
									</div>
									<div>
									<s:iterator value="farm.docUpload">
										<tr>
											<td align="left"><s:property value="name" /></td>
											<td align="center">&nbsp;&nbsp;
												<button type="button" class="btn btn-default"
													aria-label="Left Align"
													onclick='downloadDocument(<s:property value="id" />)'>
													<span class="glyphicon glyphicon-download-alt"
														aria-hidden="true"></span>
												</button>

											</td>
										</tr>
									</s:iterator>
									</div>
								</s:if>
							</s:if> --%>
												</div>
											</s:if>



										</div>

									</td>

									</tr>
								
								
								<tr>
									<td>
										<div class="formContainerWrapper">
												
												<div class="aPanel soilIrrigationInfo">
						<div class="aTitle">
							<h2>
								<s:property value="%{getLocaleProperty('info.soil')}" />
								<div class="pull-right">
									<a class="aCollapse" href="#farmSoilAccordian"><i
										class="fa fa-chevron-right"></i></a>
								</div>
							</h2>
						</div>
						<div class="aContent dynamic-form-con" id="farmSoilAccordian">
							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.soilType" />
								</p>
								<p class="flexItem" name="selectedSoilType">
									<s:property value="soilTypeDetail" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.soilTexture" />
								</p>
								<p class="flexItem" name="selectedTexture">
									<s:property value="soilTextureDetail" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.soilFertilityStatus" />
								</p>
								<p class="flexItem" name="selectedSoilFertility">
									<s:property value="soilFertilityDetail" />
								</p>
							</div>

							<div class="dynamic-flexItem2">
								<p class="flexItem">
									<s:text name="farm.farmIrrigationSource" />
								</p>
								<p class="flexItem" name="selectedIrrigation">
									<s:property value="farmIrrigationDetail" />
								</p>
							</div>
							<%-- <s:if test="farm.farmDetailedInfo.farmIrrigation==2"> --%>
							<div class="dynamic-flexItem2 hideIrrigationType">
								<p class="flexItem">
									<s:property
										value="%{getLocaleProperty('farm.farmIrrigationType')}" />
								</p>
								<p class="flexItem" name="selectedIrrigationSource">
									<s:property value="irrigationSourceDetail" />
								</p>
							</div>
							<s:if test="currentTenantId=='welspun'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.NoOfCrotenTrees')}" />
									</p>
									<p class="flexItem" name="farm.NoOfCrotenTrees">
										<s:property value="farm.NoOfCrotenTrees" />
									</p>
								</div>

								<div class="dynamic-flexItem2 tree">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('treeName')}" />
									</p>
									<p class="flexItem" name="farm.treeName">
										<s:property value="farm.treeName" />
									</p>
								</div>
								<div class="dynamic-flexItem2 tree">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.distanceProcessingUnit')}" />
									</p>
									<p class="flexItem" name="farm.distanceProcessingUnit">
										<s:property value="farm.distanceProcessingUnit" />
									</p>
								</div>
								<div class="dynamic-flexItem2 avgStore">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('avgStore')}" />
									</p>
									<p class="flexItem" name="farm.avgStore">
										<s:property value="farm.avgStore" />
									</p>
								</div>
							</s:if>
							<%-- </s:if> --%>
							<s:if test="farm.farmDetailedInfo.irrigationSource==99">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farm.irrigatedOther" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.irrigatedOther">
										<s:property value="farm.farmDetailedInfo.irrigatedOther" />
									</p>
								</div>
							</s:if>
							<s:if test="currentTenantId!='welspun'">
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:text name="farm.methodOfIrrigation" />
									</p>
									<p class="flexItem" name="selectedMethodOfIrrigation">
										<s:property value="methodOfIrrigationDetail" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.boreWellRechargeStructure')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.boreWellRechargeStructure">
										<s:property
											value="borewellList[farm.farmDetailedInfo.boreWellRechargeStructure]" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('farm.inputSource')}" />
									</p>
									<p class="flexItem" name="farm.farmDetailedInfo.inputSource">
										<s:property value="selectedInputSource" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('activitiesInCoconutFarming')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.activitiesInCoconutFarming">
										<s:property
											value="farm.farmDetailedInfo.activitiesInCoconutFarming" />
									</p>
								</div>
							</s:if>
							<s:if test="currentTenantId=='olivado'">

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property value="%{getLocaleProperty('8A/7/12')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.processingActivity">
										<s:property
											value="processingActList[farm.farmDetailedInfo.processingActivity]" />
									</p>
								</div>

								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.fullTimeWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.fullTimeWorkersCount">
										<s:property value="farm.farmDetailedInfo.fullTimeWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('presenceOfBananaTrees')}" />
									</p>
									<p class="flexItem" name="farm.presenceBananaTrees">
										<s:property value="presenceOfBanana" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.partTimeWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.partTimeWorkersCount">
										<s:property value="farm.farmDetailedInfo.partTimeWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.seasonalWorkersCount')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.seasonalWorkersCount">
										<s:property value="farm.farmDetailedInfo.seasonalWorkersCount" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.parallelProduction')}" />
									</p>
									<p class="flexItem" name="farm.parallelProd">
										<s:property value="parallelProduction" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
									</p>
									<p class="flexItem"
										name="farm.farmDetailedInfo.lastDateOfChemicalApplication">
										<s:property
											value="farm.farmDetailedInfo.lastDateOfChemicalApplication" />
									</p>
								</div>
								<div class="dynamic-flexItem2">
									<p class="flexItem">
										<s:property
											value="%{getLocaleProperty('farm.lastDateofOrganicUnit')}" />
									</p>
									<p class="flexItem" name="farm.inputOrganicUnit">
										<s:property value="farm.inputOrganicUnit" />
									</p>
								</div>
							</s:if>

						</div>
					</div>
												
												
										</div>
									</td>
								</tr>
								
								
								
								<tr>
								
								
								<td>
								<div class="formContainerWrapper">
										<div class="aPanel farmLabourInfo">
										<div class="aTitle">
											<h2>
												<s:text name="info.labour" />
												<div class="pull-right">
													<a class="aCollapse" href="#labourAccordian"><i
														class="fa fa-chevron-right"></i></a>
												</div>
											</h2>
										</div>
										<div class="aContent dynamic-form-con" id="farmLandMarkAccordian">
				
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.fullTimeWorkersCount" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:property value="farm.farmDetailedInfo.fullTimeWorkersCount" />
												</p>
											</div>
				
											<div class="dynamic-flexItem2">
												<p class="flexItem">
													<s:text name="farm.partTimeWorkersCount" />
												</p>
												<p class="flexItem" name="farm.farmName">
													<s:property value="farm.farmDetailedInfo.partTimeWorkersCount" />
												</p>
											</div>
											<s:if test="currentTenantId!='wilmar'">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:text name="farm.seasonalWorkersCount" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.seasonalWorkersCount" />
													</p>
												</div>
											</s:if>
				
										</div>
								</div>
								</div>
								</td>
								
								</tr>
								
							
							<tr>
								<td>
								
									<div class="formContainerWrapper">
									
											<div class="aPanel fieldHistoryInfo">
											<div class="aTitle">
												<h2>
					
													<s:property value="%{getLocaleProperty('info.conversions')}" />
													<div class="pull-right">
														<a class="aCollapse" href="#fieldInfoAccordian"><i
															class="fa fa-chevron-right"></i></a>
													</div>
												</h2>
											</div>
											<div class="aContent dynamic-form-con" id="fieldInfoAccordian">
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farm.fieldName')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.fieldName" />
													</p>
												</div>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farm.fieldCrop')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.fieldCrop" />
													</p>
												</div>
					
					
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farm.fieldArea')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.fieldArea" />
													</p>
												</div>
					
					
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farm.inputApplied')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.inputApplied" />
													</p>
												</div>
					
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('farm.quantity')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property value="farm.farmDetailedInfo.quantityApplied" />
													</p>
												</div>
												<div class="dynamic-flexItem2">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farm.lastDateofChemicalApply')}" />
													</p>
													<p class="flexItem" name="farm.farmName">
														<s:property
															value="farm.farmDetailedInfo.lastDateOfChemicalString" />
													</p>
												</div>
											</div>
										</div>
									</div>
								
								</td>
							</tr>
								
								
								
								<tr>
									
									<td>
									
										<div class="formContainerWrapper">

											<div class="aPanel conversionStatus" style="width: 100%">
												<div class="aTitle">
													<h2>
														<s:property value="%{getLocaleProperty('info.ics')}" />
														<div class="pull-right">
															<a class="aCollapse" href="#farmICSAccordian"><i
																class="fa fa-chevron-right"></i></a>
														</div>
													</h2>
												</div>
												<div class="aContent dynamic-form-con"
													id="farmConversionAccordian">
													<s:iterator value="farm.farmICSConversion">
														<s:if test="currentTenantId!='welspun'">
															<div class="dynamic-flexItem2">
																<p class="flexItem">
																	<s:text name="conversionStatus" />
																</p>
																<p class="flexItem" name="farm.farmName">
																	<s:if test="icsType!=null">
																		<s:text name='%{"icsStatus"+icsType}' />
																	</s:if>
																</p>
															</div>
														</s:if>
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property
																	value="%{getLocaleProperty('farminspectionDate')}" />
															</p>
															<p class="flexItem" name="farm.farmName">
															<s:property value="inspectionDateString" />
															</p>
														</div>
														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="nameOfInspector" />
															</p>
															<p class="flexItem" name="farm.farmName">

																<s:property value="inspectorName" />
															</p>
														</div>

														 <%-- <div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:text name="certType" />
															</p>
															<p class="flexItem" name="farm.farmName">
																<s:property value="scopeName" />

																<s:property value="farm.farmIcsConv.scope" />
															</p>
														</div> --%> 

														<div class="dynamic-flexItem2">
															<p class="flexItem">
																<s:property value="%{getLocaleProperty('qualified')}" />
															</p>
															<p class="flexItem" name="farm.farmName">
																<s:text name='%{"inspe"+qualified}' />

																<%-- <s:if test="icsType!=null">
										<s:text name='%{"inspe"+qualified}' />
									</s:if> --%>
															</p>
														</div>
														<s:iterator value="farm.farmICSConversion">
															<s:if test='qualified=="2"'>
																<div class="dynamic-flexItem2">
																	<p class="flexItem">
																		<s:property
																			value="%{getLocaleProperty('sanctionReason')}" />
																	</p>
																	<p class="flexItem" name="farm.farmName">

																		<s:property value="sanctionreason" />
																	</p>
																</div>
																<div class="dynamic-flexItem2">
																	<p class="flexItem">
																		<s:property
																			value="%{getLocaleProperty('durationOfSanction')}" />
																	</p>
																	<p class="flexItem" name="farm.farmName">

																		<s:property value="sanctionDuration" />
																	</p>
																</div>
															</s:if>
														</s:iterator>
													</s:iterator>
												</div>
											</div>

										</div>

									</td>
								
								</tr>
								
								
								
								
								
								
							</tbody>

						</table>

					</div>
				</div>
			</div>
		</div>

	</div>

	<!-- Farm page end  -->

	<!-- map start  -->

	<div id="google_map_page">
		<div class="appContentWrapper marginBottom">


			<table  width="100%">

				<tbody>

					<tr>
						<td>

							<div class="clear"></div> <s:hidden id="farmCordinates"
								value="%{jsonObjectList}" />

							<div class="formContainerWrapper" style="text-align: center;">

								<h2>
									<s:text name="info.digitalLocator" />
								</h2>
								<s:if test="farm.latitude!=''|| farm.longitude!=''">
									<%-- <div>
										<div>
											<img src="img/red_placemarker.png" width="32" height="32">
											<s:property value="%{getLocaleProperty('conventional')}" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<img src="img/yellow_placemarker.PNG" width="32" height="32">
											<s:property value="%{getLocaleProperty('inconversion')}" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<img src="img/green_placemarker.png" width="32" height="32">
											<s:property value="%{getLocaleProperty('organic')}" />
										</div>

									</div> --%>
								</s:if>
								<!-- <div id="map" class="smallmap map"  style="height:500px;"></div> -->
								<img id="google_map_static_img" src="<s:property value="staticMapUrl"/>">
								
							
								
								<div class="flexItem flex-layout flexItemStyle">
									<p class="flexItem">
										<s:text name="farm.latitude" />
									</p>
									<p class="flexItem">
										<s:property value="farm.latitude" />
									</p>
								</div>
								<div class="flexItem flex-layout flexItemStyle">
									<p class="flexItem">
										<s:text name="farm.longitude" />
									</p>
									<p class="flexItem">
										<s:property value="farm.longitude" />
									</p>
								</div>
							</div>
						</td>

					</tr>

				</tbody>

			</table>

		</div>
	</div>

	<!-- map end -->

	<!-- Farm crops start  -->
	<div id="farm_crops_pdf">




		<div class="flex-view-layout">
			<div class="fullwidth fullheight">
				<div class="flexWrapper">
					<div class="flexLeft appContentWrapper marginBottom">

						<div class="head">
							<div class="aTitle">
								<h2>Farm Crops</h2>
							</div>
						</div>

						<div class="formContainerWrapper">
							<div class="aPanel farmCrops">
								<div class="aTitle">
									<h2>
										<s:text name="info.farmCrops" />
										<div class="pull-right">
											<a class="aCollapse" href="#"><i
												class="fa fa-chevron-right"></i></a>
										</div>
									</h2>
								</div>
								<div class="aContent dynamic-form-con">
									<div class="dynamic-flexItem2" name="farmcrops.farmName">
										<p class="flexItem">
											<s:text name="farmcrops.farmName" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.farm.farmName" />
										</p>
									</div>


									<div class="dynamic-flexItem2" name="farmcrops.cropCategory.prop">
										<p class="flexItem">
											<s:text name="farmcrops.cropCategory.prop" />
										</p>
										<p class="flexItem">
											<s:text name="cs%{farmCrops.cropCategory}" />
											&nbsp;
										</p>
									</div>

									<div class="dynamic-flexItem2" name="farmfarmcrops.cropSeason">
										<p class="flexItem">
											<s:text name="farmfarmcrops.cropSeason" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.cropSeason.name" />
											&nbsp;
										</p>
									</div>


									<s:if test="cropInfoEnabled==1">
										<s:if
											test="currentTenantId !='chetna' && currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId !='crsdemo'										
										&& currentTenantId !='welspun'">
											<div class="dynamic-flexItem2" name="farmCrops.CultivationType">
												<p class="flexItem">
													<s:text name="farmCrops.CultivationType" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.cropCategoryList" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<div class="dynamic-flexItem2" name="farmCrops.procurementVariety.procurementProduct.name">
										<p class="flexItem">
											<s:text name="%{getLocaleProperty('cropName')}" />

										</p>
										<p class="flexItem">
											<s:property
												value="farmCrops.procurementVariety.procurementProduct.name" />
											&nbsp;
										</p>
									</div>
									<div class="dynamic-flexItem2" name="farmCrops.procurementVariety.name">
										<p class="flexItem">
											<s:text name="variety" />
										</p>
										<p class="flexItem">
											<s:property value="farmCrops.procurementVariety.name" />
											&nbsp;
										</p>
									</div>
									<s:if test="currentTenantId =='kpf'||currentTenantId =='simfed'||currentTenantId =='wub'">
										<div class="dynamic-flexItem2" name="farmCrops.cultiArea">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('cultiArea')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.cultiArea" />
												&nbsp;
											</p>
										</div>
										<div class="dynamic-flexItem2" id="sowDate" name="farmcrops.sowingDate">
											<p class="flexItem">
												<s:text name="farmcrops.sowingDate" />
											</p>
											<p class="flexItem">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">

										<div class="dynamic-flexItem2" name="farmCrops.cultiArea">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('cultiArea')}" />
												(
												<s:property value="%{getAreaType()}" />
												)
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.cultiArea" />
												&nbsp;
											</p>
										</div>

										<div class="dynamic-flexItem2" id="sowDate" name="sowingDate">
											<p class="flexItem">
												<s:property
													value="%{getLocaleProperty('farmcrops.sowingDate')}" />
												<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)
													</s:if>
											</p>
											<p class="flexItem">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>

									</s:if>

									<s:if
										test="currentTenantId !='chetna' && currentTenantId !='iccoa' && currentTenantId !='welspun' && currentTenantId !='wilmar'">
										<s:if test="farmCrops.cropCategory == 0">
											<div class="dynamic-flexItem2" name="farmCrops.type">
												<p class="flexItem">
													<s:property value="%{getLocaleProperty('type')}" />
												</p>
												<p class="flexItem">
													<s:property value="farmCrops.type" />
													&nbsp;
												</p>
											</div>
										</s:if>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId!='movcd' && currentTenantId!='crsdemo'  && currentTenantId!='wub' 
									&& currentTenantId !='gar' && currentTenantId !='welspun'&& currentTenantId !='wilmar' && currentTenantId!='griffith' && currentTenantId!='simfed'">
										<div class="dynamic-flexItem2 seedSource" name="farmCrops.seedSource">
											<p class="flexItem">
												<s:text name="seedSource" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedSource" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<%-- 						<s:if test='currentTenantId=="iccoa"'>
									<div class="dynamic-flexItem2" id="stapleLength">
											<p class="flexItem">
											<s:property value="%{getLocaleProperty('satbleLength')}" />
												
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if> --%>
									<s:if
										test="currentTenantId!='pratibha' &&currentTenantId !='pgss' && currentTenantId !='wilmar' && currentTenantId !='iccoa' 
									&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar'&& currentTenantId !='welspun'">
										<div class="dynamic-flexItem2" id="stapleLength" name="farmCrops.stapleLength">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('satbleLength')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.stapleLength" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId =='chetna'">
										<div class="dynamic-flexItem2" name="farmcrops.sowingDate">
											<p class="flexItem">
												<s:text name="farmcrops.sowingDate" />
												(DD-MM-YYYY)
											</p>
											<p class="flexItem">
												<s:property value="sowingDate" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId!='kpf' && currentTenantId!='movcd' && currentTenantId !='wilmar' && currentTenantId !='iccoa' && currentTenantId!='wub'
										&& currentTenantId!='crsdemo'&& currentTenantId !='agro' && currentTenantId !='gar' && currentTenantId !='welspun' && currentTenantId!='simfed'">
										<div class="dynamic-flexItem2" id=seedQtyUsed name="farmCrops.seedQtyUsed">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyUsed')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedQtyUsed" />
												&nbsp;
											</p>
										</div>
										<div class="dynamic-flexItem2" id=seedQtyUsed name="farmCrops.seedQtyCost">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('seedQtyCost')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.seedQtyCost" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer' ||  currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="riskAssesment" name="farmcrops.riskAssesment">
											<p class="flexItem">
												<s:text name="farmcrops.riskAssesment" />
											</p>
											<p class="flexItem">
												<s:text name="rs%{farmCrops.riskAssesment}" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if test="currentTenantId=='mhr' ">
										<div class="dynamic-flexItem2" id="captureAssessment" name="farmcrops.riskBufferZoneDistanse">
											<p class="flexItem">
												<s:text name="farmcrops.riskBufferZoneDistanse" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.riskBufferZoneDistanse" />
												&nbsp;
											</p>
										</div>
									</s:if>
									<s:if
										test="currentTenantId=='lalteer'|| currentTenantId=='mhr' || currentTenantId=='chetna'">
										<div class="dynamic-flexItem2" name="farmCrops.seedTreatmentDetails">
											<p class="flexItem">
												<s:text name="farmCrops.seedTreatmentDetails" />
											</p>
											<s:if test="farmCrops.seedTreatmentDetails=='99'">
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
													-
													<s:property value="farmCrops.otherSeedTreatmentDetails" />
													</td>
												</p>
											</s:if>
											<s:else>
												<p class="flexItem">
													<s:property
														value="seedTreatmentDetailsList[farmCrops.seedTreatmentDetails]" />
												</p>
											</s:else>
										</div>
									</s:if>
									<s:if test="cropInfoEnabled==1">
										<s:if test="currentTenantId =='pratibha'">
											<div class="dynamic-flexItem2 " name="farmfarmcrops.estimatedYeild.quintals">
												<p class="flexItem">
													<s:text name="farmfarmcrops.estimatedYeild.quintals" />
												</p>
												<p class="flexItem" id="estimatedYieldQuintal"></p>
											</div>
											<div class="dynamic-flexItem2 " id="harvestDat" name="farmcrops.harvestDate">
												<p class="flexItem">
													<s:text name="farmcrops.harvestDate" />
													(DD-MM-YYYY)
												</p>
												<p class="flexItem">
													<s:property value="harvestDate" />

												</p>
											</div>
										</s:if>
										<s:else>
											<s:if test="currentTenantId !='welspun'">
												<div class="dynamic-flexItem2 " name="farmfarmcrops.estimatedYeild">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmfarmcrops.estimatedYeild')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmCrops.estimatedYield" />

													</p>
												</div>
												<s:if test="currentTenantId!='griffith'">
													<div class="dynamic-flexItem2 " name="farmfarmcrops.estimatedYeild.tonnes">
														<p class="flexItem">
															<s:text name="farmfarmcrops.estimatedYeild.tonnes" />
														</p>
														<p class="flexItem" id="estimatedYieldMetTon"></p>


													</div>
													<div class="dynamic-flexItem2 " id="harvestDat" name="farmcrops.harvestDate">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('farmcrops.harvestDate')}" />
															<s:if test="currentTenantId !='wilmar'">
													(DD-MM-YYYY)</s:if>
														</p>
														<p class="flexItem">
															<s:property value="harvestDate" />

														</p>
													</div>
												</s:if>
											</s:if>

										</s:else>


									</s:if>




									<s:if test="currentTenantId=='wilmar'">

										<div class="dynamic-flexItem2 " name="farmCrops.noOfTrees">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('noOfTrees')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.noOfTrees" />

											</p>
										</div>

										<div class="dynamic-flexItem2 " name="farmCrops.prodTrees">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('prodTrees')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.prodTrees" />

											</p>
										</div>
										<div class="dynamic-flexItem2 " name="farmCrops.affTrees">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('affTrees')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.affTrees" />

											</p>
										</div>


										<div class="dynamic-flexItem2 " name="farmCrops.interAcre">
											<p class="flexItem">
												<s:property value="%{getLocaleProperty('yearOfPlanting')}" />
											</p>
											<p class="flexItem">
												<s:property value="farmCrops.interAcre" />

											</p>
										</div>
									</s:if>
								</div>
								
							</div>

						</div>




					</div>
				</div>
			</div>
		</div>


	</div>
<!-- farm crops end -->

<!-- ICS certification start -->

<div id="ics_pdf">

	<s:hidden key="farmerDynamicData.id" id="farmerDynamicDataId"
		class="uId" />
	<s:hidden key="currentPage" />

	<div class="flex-view-layout">
		<div class="fullwidth fullheight">
			<div class="flexWrapper">
				<div class="flexLeft appContentWrapper marginBottom">

						<div id="ics_static_fields">
				
									<div class="head">
										<div class="aTitle">
											<h2>ICS</h2>
										</div>
									</div>


	
									<div class="formContainerWrapper">
										<div class="aPanel">
											<div class="aTitle">
												<h2>
													<s:property value="%{getLocaleProperty('info.dynamicCertification')}" />
													<%-- <s:property value="infoName" /> --%>
													<div class="pull-right">
																				<a class="aCollapse" href="#"><i
																					class="fa fa-chevron-right"></i></a>
																			</div>
												</h2>

											</div>
											<div class="aContent dynamic-form-con">
												<div class="dynamic-flexItem2 dateDiv farmerDiv">
													<p class="flexItem">
														<s:property value="%{getLocaleProperty('date')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmerDynamicData.txnDate" />
													</p>
												</div>
												<div class="dynamic-flexItem2 farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('village')}" />
													</p>
													<p class="flexItem">
														<s:property value="selectedVillage" />
													</p>
												</div>
												<div class="dynamic-flexItem2 ">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.farmer')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmer_ics" />
													</p>
												</div>
												<div class="dynamic-flexItem2 farmDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.farm')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmList" />
													</p>
												</div>
												<div class="dynamic-flexItem2 farmCropDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('variety')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmCropList.name" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 farmCropDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('farmCrop')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmCropList.procurementProduct.name" />
													</p>
												</div>

												<div class="dynamic-flexItem2 groupDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.group')}" />
													</p>
													<p class="flexItem">
														<s:property value="group" />
													</p>
												</div>

												<div class="dynamic-flexItem2 seasonClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.season')}" />
													</p>
													<p class="flexItem">
														<s:property value="season" />
													</p>
												</div>
												<%-- <div class="dynamic-flexItem2 icsDetailDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.farmer')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmer" />
													</p>
												</div>
												
												
												<div class="dynamic-flexItem2 icsDetailDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.farm')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmList" />
													</p>
												</div> --%>
												
												
												<div class="dynamic-flexItem2 inspectClass">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.insDate')}" />
													</p>
													<p class="flexItem">
														<s:property value="insDate" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 inspectClass">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.inspectorName')}" />
													</p>
													<p class="flexItem">
														<s:property value="inspectorName" />
													</p>
												</div>
												
												
												<div class="dynamic-flexItem2 inspectClass">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.inspectorMobile')}" />
													</p>
													<p class="flexItem">
														<s:property value="inspectorMobile" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.insType')}" />
													</p>
													<p class="flexItem">
														<s:property value="insType" />
													</p>
												</div>
												
												
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.scope')}" />
													</p>
													<p class="flexItem">
														<s:property value="scope" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.totLand')}" />
													</p>
													<p class="flexItem">
														<s:property value="totLand" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.orgLand')}" />
													</p>
													<p class="flexItem">
														<s:property value="orgLand" />
													</p>
												</div>
												
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.totSite')}" />
													</p>
													<p class="flexItem">
														<s:property value="totSite" />
													</p>
												</div>
												<div class="dynamic-flexItem2 inspectClass farmerDiv">
													<p class="flexItem">
														<s:property
															value="%{getLocaleProperty('dynamicCertification.inspectionStatus')}" />
													</p>
													<p class="flexItem">
														<s:property value="farmerDynamicData.inspectionStatus" />
													</p>
												</div>

												<s:if test="farmerDynamicData.conversionStatus==1">
													<div class="dynamic-flexItem2 icsDetailDiv farmerDiv">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('dynamicCertification.icsType')}" />
														</p>
														<p class="flexItem">
															<s:property value="icsType" />
														</p>

													</div>

												</s:if>
												<s:else>

													<div class="dynamic-flexItem2 icsDetailDiv farmerDiv">
														<p class="flexItem">
															<s:property
																value="%{getLocaleProperty('dynamicCertification.correctiveActionPlan')}" />
														</p>
														<p class="flexItem">
															<s:property value="correctiveActionPlan" />
														</p>

													</div>
												</s:else>
											</div>
										</div>
									</div>
		
	
						</div>
		
		
						<div class="dynamicFieldsRender" ></div>
						
						<s:if test="getDigitalSignatureEnabled()==1">
							<div class="dynamic-flexItem2 inspectClass" style="float:right">
								<p class="flexItem">
									<s:if test='farmerDynamicData.digSignByteString!=null && farmerDynamicData.digSignByteString!=""'>
													<img border="0" height="50px" style="float:right"
														src="data:image/png;base64,<s:property value="farmerDynamicData.digSignByteString"/>">
									</s:if>
												
								</p>
								<p class="flexItem">
										<s:property	value="%{getLocaleProperty('digitalSignature')}" />
								</p>
							</div>
						</s:if>
	
				</div>
			</div>
		</div>
	</div>			
</div>




<!-- ICS certification end -->

</div>
 

<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.4.1/jspdf.debug.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.4.1/jspdf.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>


</body>

