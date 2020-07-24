<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="assets/common/openlayers/OpenLayers.js"></script>
<script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>
</head>

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
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
	<script type="text/javascript">
	var farmerId='';
	var villageName='';
	var sDate='';
	var eDate='';
	var seasonCode='';
	var tenantId="<s:property value='tenantId'/>";
	var selectedFieldStaff='';
	var enableTraceability = "<s:property value='enableTraceability'/>";
	var branchId='<s:property value="branchId"/>';
	jQuery(document).ready(function(){
		var isKpfBased = '<s:property value="%{getIsKpfBased()}" />';
		if(isKpfBased=="1"){
			$(".zipDiv").show();
		}else{
			$(".zipDiv").hide();
		}
		showFarmMap();
		onLoadValues();
		loadCustomPopup();
		onFilterData();
			var columnNames ;
			loadGrid();
			
		
			$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
	});
	var data = new Array();
	
	
	
	function loadGrid(){
	
		jQuery("#detail").jqGrid(
				{
					url:'supplierProcurementReport_detail.action?',
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
						 "seasonCode" : function(){
							   return document.getElementById("seasonCode").value;
						  },
						  "selectedFieldStaff" : function(){			  
								return document.getElementById("fieldStaff").value;
						  },
						},	
				   	colNames:[
				   	   '<s:property value="%{getLocaleProperty('report.date')}" />' ,
				   	   '<s:property value="%{getLocaleProperty('report.month')}" />' ,
				   	   '<s:property value="%{getLocaleProperty('report.year')}" />' ,
				   	 <s:if test="currentTenantId!='gsma'">
				   		'<s:property value="%{getLocaleProperty('invoiceNo')}" />' ,
				   		</s:if>
				   		'<s:text name="season"/>',
				    	'<s:property value="%{getLocaleProperty('profile.cooperative')}" />',
				    	'<s:property value="%{getLocaleProperty('agentName')}" />',
				    	 <s:if test="currentTenantId!='wilmar'">
				    	'<s:property value="%{getLocaleProperty('supplierStatus')}" />',
				    	</s:if>
						'<s:text name="supplierType"/>',
						 <s:if test="currentTenantId!='gsma'">
						'<s:property value="%{getLocaleProperty('supplierName')}" />', 
						</s:if>
						 /*'<s:text name="farmerName"/>',
						
						'<s:property value="%{getLocaleProperty('village.name')}" />',	 */					
			  		  	//'<s:text name="totalNetwt"/>',
			  		  	//'<s:text name="totalGrossWeight"/>',
			  		  	 '<s:property value="%{getLocaleProperty('totalGrossWeight')}" />',
			  		  	'<s:property value="%{getLocaleProperty('txnAmount')}" />',
			  		  	
			  		  <s:if test="isKpfBased!=1 && currentTenantId!='wilmar'">
				  		'<s:text name="invoiceCost"/>'  ,
				  		 '<s:property value="%{getLocaleProperty('recieptNo')}" />' ,
				  		 </s:if>
				  		<s:if test="isKpfBased==1">
				  		 '<s:property value="%{getLocaleProperty('totalLabourCost')}" />' ,
				  		 '<s:property value="%{getLocaleProperty('transportCost')}" />' ,
				  		'<s:property value="%{getLocaleProperty('taxAmt')}" />' ,
				  		'<s:property value="%{getLocaleProperty('otherCost')}" />' ,
				  		'<s:property value="%{getLocaleProperty('invoiceValue')}" />' ,
				  		 </s:if>
				  		//'<s:text name="map"/>' 
		 	      	 ],
				   	colModel:[
						{name:'procurementDate',index:'procurementDate',width:250,sortable:false},
						{name:'procurementMonth',index:'procurementMonth',width:350,sortable:false},
						{name:'procurementYear',index:'procurementYear',width:250,sortable:false},						
						 <s:if test="currentTenantId!='gsma'">
						 {name:'invoiceNo',index:'invoiceNo',width:250,sortable:false},
				   		</s:if>
						 
						{name:'season',index:'season',width:250,sortable:false},
						{name:'cooperative',index:'cooperative',width:350,sortable:false},
						{name:'agentName',index:'agentName',width:350,sortable:false},
						 <s:if test="currentTenantId!='wilmar'">
						{name:'supplierStatus',index:'supplierStatus',width:350,sortable:false},
						 </s:if>
						{name:'supplierType',index:'supplierType',width:350,sortable:false},
						 <s:if test="currentTenantId!='gsma'">
						{name:'supplierName',index:'supplierName',width:350,sortable:false},
						 </s:if>
						 /*{name:'farmerName',index:'farmerName',width:350,sortable:false},
						{name:'v.name',index:'v.name',width:350,sortable:false},		 */				
				   		//{name:'totalNetwt',index:'totalNetwt',width:250,sortable:false,align:'right'},	
				   		{name:'totalGrossWeight',index:'totalGrossWeight',width:300,sortable:false,align:'right'},
				   		{name:'totalAmount',index:'totalAmount',width:300,sortable:false,align:'right'},
				   		<s:if test="isKpfBased!=1 && currentTenantId!='wilmar'">
				   		{name:'invoiceCost',index:'invoiceCost',width:300,sortable:false,align:'right'},
				   		{name:'recieptNo',index:'recieptNo',width:400,sortable:false,align:'right'},
				   		
				   		</s:if>
				   		<s:if test="isKpfBased==1">
				   	{name:'totalLabourCost',index:'totalLabourCost',width:300,sortable:false,align:'right'},
			   		{name:'transportCost',index:'transportCost',width:400,sortable:false,align:'right'},
			   		{name:'taxAmt',index:'taxAmt',width:250,sortable:false,align:'right'},
			   		{name:'otherCost',index:'otherCost',width:250,sortable:false,align:'right'},
			   		{name:'invoiceValue',index:'invoiceValue',width:250,sortable:false,align:'right'},
				   	 </s:if>
				   		//{name:'map',index:'map',width:280,sortable:false,align:'center'}
					],
				   height: 301, 
				   width: $("#baseDiv").width(),
				   scrollOffset: 0,     
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
					   $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table> <div id='"+pager_id+"' class='scroll'></div>");
					   var ret = jQuery("#detail").jqGrid('getRowData',row_id);
					   jQuery("#"+subgrid_table_id).jqGrid(
					   {
							url:'supplierProcurementReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
						   	       '<s:text name="sno"/>',
						   	  
						   	  '<s:text name="farmerName"/>',
						   	'<s:property value="%{getLocaleProperty('groupName')}" />',
									  '<s:text name="productName"/>',
						  		   	  '<s:text name="grade"/>',	
						  		   	'<s:property value="%{getLocaleProperty('unit')}" />',
						  		  '<s:property value="%{getLocaleProperty('noOfBags')}" />',
						  		   	<s:if test='enableTraceability=="1"'>
							  		    '<s:text name="batchNo"/>',
							  			</s:if>
							  		  '<s:property value="%{getLocaleProperty('cstPrice')}" />',
						  			  '<s:property value="%{getLocaleProperty('grossWeight')}" />',
						  			
						  		   	//  '<s:text name="grossWeight"/>',
						  		   	'<s:property value="%{getLocaleProperty('totalAmt')}" />',
						  		  '<s:property value="%{getLocaleProperty('cropType')}" />',
						  		<s:if test="isKpfBased==1">
						  		 '<s:property value="%{getLocaleProperty('print')}" />'
						  		    </s:if>
				 	      	 ],
						   	colModel:[	
									{name:'sno',index:'sno',width:50,sortable:false},
									//{name:'farmerId',index:'farmerId',width:150,sortable:false},
		   							{name:'farmerName',index:'farmerName',width:100,sortable:false},
		   							{name:'groupName',index:'groupName',width:100,sortable:false},
									{name:'procurementProduct.name',index:'procurementProduct.name',width:150,sortable:false},
		   							{name:'grade',index:'grade',width:150,sortable:false},
		   							{name:'unit',index:'unit',width:100,sortable:false},
		   							{name:'noOfBags',index:'noOfBags',width:100,sortable:false},
		   							<s:if test='enableTraceability=="1"'>
		   							{name:'batchNo',index:'batchNo',width:150,sortable:false},
						  			</s:if>
		   							{name:'cstPrice',index:'cstPrice',width:150,sortable:false,align:'right'},
		   							{name:'grossWeight',index:'grossWeight',width:150,sortable:false,align:'right'},		   						
		   							{name:'totalAmt',index:'totalAmt',width:150,sortable:false,align:'right'},
		   							{name:'cropType',index:'cropType',width:150,sortable:false},
		   							<s:if test="isKpfBased==1">
		   							{name:'print',index:'print',width:100,sortable:false,align:'center'}
		   						  </s:if>
						   	],
						   	scrollOffset: 0, 
						    sortname:'id',
						    height: '100%', 
						    rowNum:10,
						    sortorder: "desc",
						    autowidth: true,
						    viewrecords: true				
					   });
					 //  jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
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
		
	}
	
	
	function exportXLS(){
		if(isDataAvailable("#detail")){
			
			$("#form select").each(function(){
				var value = $(this).val();
				var name=this.name;
				var label =$(this).parent().parent().find("label").html();
				if(!isEmpty(value)){
					data.push({name:name,value:value,label:label});
				}
			});
			console.log(data);
			var json = JSON.stringify(data);
			var dataa={
					filterList:json
			};
			
			jQuery("#detail").setGridParam ({postData: {rows : 0}});
			jQuery("#detail").jqGrid('excelExport', {url: 'supplierProcurementReport_populateXLS.action?',postData:dataa});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
	}	
	function onLoadValues(){
		//document.getElementById('fieldl').selectedIndex=0;
		 $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
	     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
	 	sDate = document.getElementById("hiddenStartDate").value;
		
		eDate = document.getElementById("hiddenEndDate").value;
	     $("#daterange").data().daterangepicker.updateView();
	      $("#daterange").data().daterangepicker.updateCalendars();
		$('.applyBtn').click();
		 document.getElementById('fieldStaff').selectedIndex=0;
		document.getElementById('seasonCode').selectedIndex=0;
		selectedFieldStaff = document.getElementById("fieldStaff").value;
	}
	
	
	function search(){
		data = new Array();
		$("#form select").each(function(){
			var value = $(this).val();
			var name=this.name;
			var label =$(this).parent().parent().find("label").html();
			if(!isEmpty(value)){
				data.push({name:name,value:value,label:label});
			}
		});
		var json = JSON.stringify(data);
		var dataa={
				filterList:json
		};
	
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			if(d2.length>1){
				var value1= d2[0];
				var value2= d2[1];
				document.getElementById("hiddenStartDate").value=value1;
				document.getElementById("hiddenEndDate").value=value2;
			}
		
		
		jQuery("#detail").jqGrid('setGridParam',{url:'supplierProcurementReport_detail.action',page:1,postData:dataa}).trigger('reloadGrid');				
		}	
	
	function enablePrinter(farmerId){
		 window.open("supplierProcurementReport_populatePrintz.action?selectedProcId="+farmerId.toString(), '_blank');
	}
	function openPDF(urlToPdfFile) {
		  window.open(urlToPdfFile, 'pdf');
		}	
	function populatePrint(){
		
	}
	
	function onFilterData(){
		callAjaxMethod("supplierProcurementReport_populateAgentList.action","fieldStaff");
		
		
	}
	function callAjaxMethod(url,name){
		var cat = $.ajax({
			url: url,
			async: true, 
			type: 'post',
			success: function(result) {
				insertOptions(name,JSON.parse(result));
			}        	

		});
		
	}	
	
</script>
<script src="plugins/openlayers/OpenLayers.js"></script>
<script
	src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.33&callback=initMap"></script>
	
	<script type="text/javascript">

try {
	var fProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
	var tProjection = new OpenLayers.Projection("EPSG:900913");
	var url = window.location.href;
	var temp = url;
	for (var i = 0; i < 1; i++) {
		temp = temp.substring(0, temp.lastIndexOf('/'));
	}
	var href = temp;
	var coordinateImg = "red_placemarker.png";
	var iconImage = temp + '/img/' + coordinateImg;
} catch (err) {

}

var $overlay;
var $modal;
var $slider;

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
		
		
	/* 	
		function loadFarmMap() { */
			try {
				
				var heading ='<s:text name="coordinates"/>&nbsp;:&nbsp'+latitude+","+longitude;
			
				 var content="<div id='map' class='smallmap map'></div>";
				
				enablePopup(heading,content);
				
				var landArea = JSON.parse(heading);
				alert(latitude+"****"+longitude+"****"+landArea);
				loadMap('map', latitude, longitude, landArea);
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
	function loadMap(mapDiv, latitude, longitude, landArea) {
		try {
			var bounds = new google.maps.LatLngBounds();
			if(!isEmpty(latitude)&&!isEmpty(longitude)){
				var marker;
				marker = new google.maps.Marker({
					position : new google.maps.LatLng(parseFloat(latitude),
							parseFloat(longitude)),
					map : map
				});
			}
			
			if(landArea.length>0){
				var cords = new Array();
				$(landArea).each(function(k,v){
					cords.push({lat:parseFloat(v.lat),lng:parseFloat(v.lon)});
				
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
					    map.setZoom(15);
					    google.maps.event.removeListener(listener);
					});
			}
			
		} catch (err) {
			console.log(err);
		}
	}
	
	   
	  </script>	<%-- FILTER SECTION --%>
	  
	  
		<s:form id="form">
		<div class="appContentWrapper marginBottom">

			<a id="link" class="" href="#" target="_blank"></a>

			
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control  input-sm" />
					</div>
				</div>
					<div class="filterEle">
					<label for="txt"><s:text name="season" /></label>
					<div class="form-element">
						<s:select name="seasonCode" id="seasonCode"
							list="seasonsList" headerKey=""
							headerValue="%{getText('txt.select')}"
							cssClass="form-control input-sm select2" />
					</div>
				</div>
				
				<div class="filterEle">
					<label for="txt"><s:property value="%{getLocaleProperty('profile.agent')}"/></label>
					<div class="form-element">
						<s:select name="selectedFieldStaff" id="fieldStaff"
							list="{}" headerKey=""
							headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
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
		 <s:if test="currentTenantId=='kpf' || currentTenantId=='simfed'|| currentTenantId=='wub'">
			<div class='zipDiv'><a style="color:blue;font-size:150%" href="img/KPFProcurement.zip">Click to download Procurement Label</a></div>
		</s:if>	
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
		

		<div style="width: 99%;" id="baseDiv">
			<table id="detail"></table>
			<div id="pagerForDetail"></div>
			<div id="pager_id"></div>
		</div>
		
	</div>
<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>
	
<!-- MODAL -->

</body>
