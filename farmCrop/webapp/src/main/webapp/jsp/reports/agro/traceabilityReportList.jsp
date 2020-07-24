<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>
<script src="plugins/openlayers/OpenLayers.js"></script>
	<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33"></script>
<head>
<meta name="decorator" content="swithlayout">
<script src="assets/common/openlayers/OpenLayers.js"></script>
 
<style>
.trace th {
    background: #A8E3D6;
    border: solid 1px #fff;
        border-left-color: rgb(255, 255, 255);
        border-left-style: solid;
        border-left-width: 1px;
}
</style>
</head>
<body>
<script type="text/javascript">
var recordLimit='<s:property value="exportLimit"/>';
var map;
var dataArr = new Array();
var markersArray = new Array();
var dataArr = new Array();
var lati="0.00";
var longi="0.00";
var name="";
var markersArray = new Array();
jQuery(document).ready(function(){
	loadCustomPopup();
	onFilterData();
	loadGrid();
	$("#generate").click( function() {
		var ses=$('#season').val();
		$('#season').val(ses).trigger("change");
		var lot=$('#lotNo').val();
		$('#lotNo').val(lot).trigger("change");
		$("#detail").jqGrid('setGridParam',{url:"traceabilityReport_data.action",page:1}).trigger('reloadGrid');	
	});

	$("#clear").click( function() {
		$('#lotNo').val('');
		$('#prNo').val('');
		document.form.submit();

	});	
});
function onFilterData() {
	callAjaxMethod("traceabilityReport_populateSeasonList.action","season");
	callAjaxMethod("traceabilityReport_populateLotNoList.action","lotNo");
}
function callAjaxMethod(url, name) {
	var cat = $.ajax({
					url : url,
					async : true,
					type : 'post',
					success : function(result) {
						insertOptions(name, JSON.parse(result));
					}
			});
}
function loadGrid(){
	jQuery("#detail").jqGrid(
	{
		url:'traceabilityReport_data.action',
	 	pager: '#pagerForDetail',
	   	datatype: "json",	
	    styleUI : 'Bootstrap',
	   	mtype: 'POST',
		postData:{
			"lotNo": function(){
				var ln= $('#lotNo').val();
				if(!isEmpty(ln) || ln!=''){
				$("#detail").hideCol("prNo")
				}
				return ln;
			},
			"prNo": function(){
				return document.getElementById("prNo").value;
			},
			"season": function(){
				return $('#season').val();
			},
		},
	        	colNames:[
	        	        <s:if test='branchId==null'>
	  						'<s:text name="%{getLocaleProperty('app.branch')}"/>',
	  			 		</s:if>
	  			  		<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
	  						'<s:text name="app.subBranch"/>',
	  			 		</s:if>
	        	          '<s:property value="%{getLocaleProperty('tracenetCode')}" />',
	        	          '<s:property value="%{getLocaleProperty('farmerName')}" />',
	        	          '<s:property value="%{getLocaleProperty('village')}" />',
	        	          '<s:property value="%{getLocaleProperty('ics')}" />',
	        	          '<s:property value="%{getLocaleProperty('shg')}" />',
	        	          '<s:property value="%{getLocaleProperty('lotNo')}" />',
	        	          '<s:property value="%{getLocaleProperty('prNo')}" />',
	        	          '<s:property value="%{getLocaleProperty('procCenter')}" />',
	        	          '<s:property value="%{getLocaleProperty('ginning')}" />',
	        	          '<s:property value="%{getLocaleProperty('spinning')}" />',
	        	          '<s:property value="%{getLocaleProperty('map')}" />'
	        	         /*  '<s:property value="%{getLocaleProperty('genrateQR')}" />'  */
	        	          ],
			    colModel:[
						  <s:if test='branchId==null'>
							{name:'branchId',index:'branchId',sortable: false,search:true,stype: 'select',searchoptions: {
								value: '<s:property value="parentBranchFilterText"/>',
								dataEvents: [{
			            					type: "change",
			            					fn: function () {
			        	     						getSubBranchValues($(this).val())
								            	}
			        						}]
							}},	   			   		
						 </s:if>
						<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
							{name:'subBranchId',index:'subBranchId',sortable: false,search:true,stype: 'select',searchoptions: { value: '<s:property value="childBranchFilterText"/>' }},	   				   		
						</s:if>  
							{name:'tracenetCode',index:'tracenetCode',sortable:false,search:false},
							{name:'farmerName',index:'farmerName',sortable:false,search:false},
							{name:'village',index:'village',sortable:false,search:false},
							{name:'ics',index:'ics',sortable:false,search:false},
							{name:'shg',index:'shg',sortable:false,search:false},
							{name:'lotNo',index:'lotNo',sortable:false,search:false},
							{name:'prNo',index:'prNo',sortable:false,search:false},
							{name:'procCenter',index:'procCenter',sortable:false,search:false},
							{name:'ginning',index:'ginning',sortable:false,search:false},
							{name:'spinning',index:'spinning',sortable:false,search:false},
							{name:'mapCol',index:'mapCol',sortable:false,search:false}
							/* {name:'printLabel',index:'printLabel',sortable:false,search:false}  */
			              ],
				height: 301, 
			   	//width: $("#baseDiv").width(),
			   	autowidth:true,
			   	scrollOffset: 5,     
			   	sortname:'id',
			   	sortorder: "desc",
			   	rowNum:10,
			    shrinkToFit:true,
			   	rowList : [10,25,50],
			   	viewrecords: true,  	
			   	subGrid: false,
	        onSortCol: function (index, idxcol, sortorder) {
		        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                    && this.p.colModel[this.p.lastsort].sortable !== false) {
	                $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	            }
	        },
	});
 	colModel = jQuery("#detail").jqGrid('getGridParam', 'colModel');
    $('#gbox_' + $.jgrid.jqID(jQuery("#detail")[0].id) +
        ' tr.ui-jqgrid-labels th.ui-th-column trace').each(function (i) {
        var cmi = colModel[i], colName = cmi.name;
        if (cmi.sortable !== false) {
            $(this).find('>div.ui-jqgrid-sortable>span.s-ico').show();
        } else if (!cmi.sortable && colName !== 'rn' && colName !== 'cb' && colName !== 'subgrid') {
            $(this).find('>div.ui-jqgrid-sortable').css({cursor: 'default'});
        } 
    });	

    $('#detail').jqGrid('setGridHeight',(reportWindowHeight));
	
}

function reset(){
	
}
function exportXLS(){
	var count=jQuery("#detail").jqGrid('getGridParam', 'records');
	if(count>recordLimit){
		 alert('<s:text name="export.limited"/>');
	}
	else if(isDataAvailable("#detail")){
		jQuery("#detail").setGridParam ({postData: {rows : 0}});
		
		jQuery("#detail").jqGrid('excelExport', {url: 'traceabilityReport_populateXLS.action'});
	}else{
	     alert('<s:text name="export.nodata"/>');
	}
}
function showMap(recId){
	var heading ='<s:text name="farmName"/>&nbsp;:&nbspTraceability Map';
	var content="<div id='map' class='smallmap'></div>";
	enablePopup(heading,content);
	initMap();
	$.ajax({
		type: "POST",
		async: false,
		url: "traceabilityReport_populateCoordinates.action",
		data: {
			params:recId
		},
		success: function(data){
			var stringData = JSON.stringify(data);
			//alert("stringData: "+stringData);
        	var trData=stringData.trim();
        	 var json = JSON.parse(trData);	
        	$.each(json, function(index, value) {
        		dataArr.push({
        			lati:parseFloat(value.split("~")[1]),
        			longi:parseFloat(value.split("~")[2]),
        			name: parseFloat(value.split("~")[0])
        		});
        	});
		 }
		
	});
	dataArr.sort();
	loadMap(new Array(), dataArr);
}
function loadMap(dataArr,landArea) {
	setMapOnAll(null); 
	var url = window.location.href;
    var temp = url;
    for (var i = 0; i < 1; i++) {
      temp = temp.substring(0, temp.lastIndexOf('/'));
    }
    var intermediateImg = temp + '/img/green_placemarker.png';
    var infowindow = new google.maps.InfoWindow();
    var marker, i;
    $(landArea).each(function(k, v) {
    	if(v.lati!=NaN && v.lati !=undefined && v.longi!=NaN && v.longi !=undefined){   
    		marker = new google.maps.Marker({
    			position : new google.maps.LatLng(v.lati,
    					v.longi), 
    					title: v.name,
    					icon:intermediateImg,			
    					map : map
		        });
    		map.setCenter({
				lat : v.lati,
				lng : v.longi
			});
		        map.setZoom(5);
		        markersArray.push(new google.maps.LatLng(v.lati,v.longi));
    	}
    });
    for (var i = 0, n = markersArray.length; i <= n; i++) {
        var coordinates = new Array();
        for (var j = i; j < i+3 && j < n; j++) {
            coordinates[j-i] = markersArray[j];
        }
        var symbolTwo = {
                path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                strokeColor: '#FFFF00',
                fillColor: '#FFFF00',
                fillOpacity: 1
              };
        var polyline = new google.maps.Polyline({
            path: coordinates,
            strokeColor: '#DC143C',
            strokeOpacity: 1.0,
            strokeWeight: 2,
            geodesic: true,
            icons: [{
                icon: {path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW},
                offset: '10%'
            },
            {
            	icon: symbolTwo,
                offset: '50%'
            }
            ]
        });
         polyline.setMap(map);
    }
}
function setMapOnAll(map) {
	    for (var i = 0; i < markersArray.length; i++) {
	    	markersArray[i].setMap(map);
	    }
	    markersArray = new Array();
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
	
/* function makeLabel(args){
	document.qrForm.action="traceabilityReport_populateQRCode.action?qrArgs="+args;
	document.qrForm.submit();
}
 */
</script>
<s:form name="qrForm" cssClass="fillform">
</s:form>
<s:form id="form" action="traceabilityReport_list">
	<div class="appContentWrapper marginBottom">
		<section class='reportWrap row'>
			<div class="gridly">
				<div class="filterEle">
					<label for="txt"><s:text name="lotNo" /></label>
					<div class="form-element">
						<%-- <s:textfield id="lotNo" name="lotNo" maxlength="15" theme="simple" cssClass="form-control"/> --%>
						<s:select name="lotNo" id="lotNo"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" multiple="true"/>
						
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="prNo" /></label>
					<div class="form-element">
						<s:textfield id="prNo" name="prNo" maxlength="15" theme="simple" cssClass="form-control" />
					</div>
				</div>
				<div class="filterEle">
					<label for="txt"><s:text name="season" /></label>
					<div class="form-element">
						<s:select name="season" id="season"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="input-sm form-control select2" />
					</div>
				</div>
			
				
			</div>
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
			</section>
		</div>
		<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<div class="flexItem text-right flex-right">
			<div class="dropdown">
				<%-- <button id='dMap' class="btn btn-primary btn-sts" type="button" aria-expanded="true"  onclick="loadMap()">
				<i class="fa fa-truck"></i>
				<s:text name="Track" />
				</button> --%>
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
			<table id="detail" class="ttrace"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>

	</div>
	</s:form>
</body>