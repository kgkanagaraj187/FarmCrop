<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>



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
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 300px;
        width : 600px;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }

      /* The popup bubble styling. */
      .popup-bubble {
        /* Position the bubble centred-above its parent. */
        position: absolute;
        top: 0;
        left: 0;
        transform: translate(-50%, -100%);
        /* Style the bubble. */
        background-color: white;
        padding: 5px;
        border-radius: 5px;
        font-family: sans-serif;
        overflow-y: auto;
        max-height: 60px;
        box-shadow: 0px 2px 10px 1px rgba(0,0,0,0.5);
      }
      /* The parent of the bubble. A zero-height div at the top of the tip. */
      .popup-bubble-anchor {
        /* Position the div a fixed distance above the tip. */
        position: absolute;
        width: 100%;
        bottom: /* TIP_HEIGHT= */ 8px;
        left: 0;
      }
      /* This element draws the tip. */
      .popup-bubble-anchor::after {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        /* Center the tip horizontally. */
        transform: translate(-50%, 0);
        /* The tip is a https://css-tricks.com/snippets/css/css-triangle/ */
        width: 0;
        height: 0;
        /* The tip is 8px high, and 12px wide. */
        border-left: 6px solid transparent;
        border-right: 6px solid transparent;
        border-top: /* TIP_HEIGHT= */ 8px solid white;
      }
      /* JavaScript will position this div at the bottom of the popup tip. */
      .popup-container {
        cursor: auto;
        height: 0;
        position: absolute;
        /* The max width of the info window. */
        width: 200px;
      }
    </style>
<body>
	<script type="text/javascript">
	var tenant='<s:property value="getCurrentTenantId()"/>';
	var map, popup, Popup;
	jQuery(document).ready(function(){
	
		loadGrid();
		loadCustomPopup();
		populateFarmerMethod();
		  $("#daterange").data().daterangepicker.startDate = moment( document.getElementById("hiddenStartDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.endDate = moment( document.getElementById("hiddenEndDate").value,  "MM-DD-YYYY" );
		     $("#daterange").data().daterangepicker.updateView();
		      $("#daterange").data().daterangepicker.updateCalendars();
			$('.applyBtn').click();
			var d1=	jQuery('#daterange').val();
			
	});
	
	function loadGrid(){
		

		jQuery("#detail").jqGrid(
		{
		   	url:'procurementTraceabilityReport_data.action',
		 	pager: '#pagerForDetail',
		   	datatype: "json",	
		    styleUI : 'Bootstrap',
		   	mtype: 'POST',		
		   	postData:{
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
			   "startDate" : function(){
	              return document.getElementById("hiddenStartDate").value;           
	             },		
	       "endDate" : function(){
	          return document.getElementById("hiddenEndDate").value;
	           },  
		   		 "farmerName" : function(){			  
				      return document.getElementById("farmerId").value;
			     	},
			     	"product" : function(){			  
					      return document.getElementById("productId").value;
				     	},
				     	<s:if test="currentTenantId=='chetna'">
				     "ics": function(){
				    	 return document.getElementById("ics").value;
				     },
				     </s:if>
				     "village": function(){
				    	 return document.getElementById("village").value;
				     },
				     <s:if test="currentTenantId=='chetna'">
			     	"city": function(){
			     		return document.getElementById("city").value;
			     	},
			     	"fCooperative": function(){
			     		return document.getElementById("fCooperative").value;
			     	},
			     	</s:if>
			     	 "warehouse" : function(){			  
					      return document.getElementById("warehouseId").value;
	 			          },
	 			         <s:if test="currentTenantId=='chetna'">
	 			    "selectedSeason" : function(){
	 			    	return document.getElementById("selectedSeason").value;
	 			    },
	 			    </s:if>
			    
		   	}, 
		   	    colNames:[		
				<s:if test="currentTenantId=='chetna'"> 
		   	        <s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					
						'<s:text name="app.subBranch"/>',    
					</s:if>  
					  '<s:property value="%{getLocaleProperty('season')}"/>	',
					  '<s:property value="%{getLocaleProperty('procurementDate')}"/>',
					  '<s:property value="%{getLocaleProperty('farmerName')}"/>	',
					  '<s:property value="%{getLocaleProperty('village')}"/>',
					  '<s:property value="%{getLocaleProperty('city')}"/>',
					  '<s:property value="%{getLocaleProperty('cooperative.name')}"/>',
					  '<s:property value="%{getLocaleProperty('icsName')}"/>	',
					  '<s:property value="%{getLocaleProperty('cooperative')}"/>	',
					  '<s:property value="%{getLocaleProperty('p.name')}"/>',
					  '<s:property value="%{getLocaleProperty('g.name')}"/>',
					  '<s:property value="%{getLocaleProperty('v.name')}"/>',
					  '<s:property value="%{getLocaleProperty('mspRate')}"/>',
					  '<s:property value="%{getLocaleProperty('receiptNo')}"/>	',
					  
					  //'<s:property value="%{getLocaleProperty('unit')}"/>',
					  '<s:property value="%{getLocaleProperty('noofBags')}"/>',
					  '<s:property value="%{getLocaleProperty('netWeight')}"/>',
					 // '<s:property value="%{getLocaleProperty('totalProVal')}"/>&nbsp;<s:property value="%{getCurrencyType().toUpperCase()}" />',
					  '<s:property value="%{getLocaleProperty('premiumPrice')}"/>',
					  '<s:property value="%{getLocaleProperty('totalProWithPremium')}"/>&nbsp;<s:property value="%{getCurrencyType().toUpperCase()}" />',
					  //'<s:property value="%{getLocaleProperty('totalPrice')}"/>',
					  //'<s:property value="%{getLocaleProperty('totalPricePremium')}"/>',
					  '<s:property value="%{getLocaleProperty('trash')}"/>',
					  '<s:property value="%{getLocaleProperty('moisture')}"/>',
					  '<s:property value="%{getLocaleProperty('stapleLengt')}"/>',
					  '<s:property value="%{getLocaleProperty('kowdiKapas')}"/>',
					  '<s:property value="%{getLocaleProperty('yellowCotton')}"/>',
					  '<s:property value="%{getLocaleProperty('photo')}"/>',
				</s:if>
				<s:else>
					 <s:if test='branchId==null'>
						'<s:text name="app.branch"/>',
					</s:if>
					<s:if test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
					
						'<s:text name="app.subBranch"/>',    
					</s:if>
						'<s:property value="%{getLocaleProperty('procurementDate')}"/>',
						'<s:property value="%{getLocaleProperty('cooperative')}"/>	',
						 '<s:property value="%{getLocaleProperty('village')}"/>',
						 '<s:property value="%{getLocaleProperty('farmerName')}"/>	',
						 '<s:property value="%{getLocaleProperty('farmerCode')}"/>	',
						'<s:property value="%{getLocaleProperty('p.name')}"/>',
						 '<s:property value="%{getLocaleProperty('v.name')}"/>',
						 '<s:property value="%{getLocaleProperty('g.name')}"/>',
						 '<s:property value="%{getLocaleProperty('totalProWithPremium')}"/>&nbsp;<s:property value="%{getCurrencyType().toUpperCase()}" />',
						 '<s:property value="%{getLocaleProperty('netWeight')}"/>',
						 '<s:property value="%{getLocaleProperty('noofBags')}"/>',
						 '<s:property value="%{getLocaleProperty('photo')}"/>',
						 
				</s:else>
				<s:if test='currentTenantId.equalsIgnoreCase("livelihood")'>
				 '<s:property value="%{getLocaleProperty('location')}"/>'
				</s:if>
		      	 ],   
		   	colModel:[	
			<s:if test="currentTenantId=='chetna'"> 
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
		   		{name:'season',index:'season',sortable:false,frozen:true},
		   		{name:'procurementDate',index:'procurementDate',sortable:false,frozen:true},
		   		{name:'farmerName',index:'farmerName',sortable:false},
		   		{name:'village',index:'village',sortable:false},
		   		{name:'block',index:'block',sortable:false},
		   		{name:'coName',index:'coName',sortable:false},
		   		{name:'icsName',index:'icsName',sortable:false},
		   		{name:'warehouse',index:'warehouse',sortable:false},
		   		{name:'p.name',index:'p.name',sortable:false},
		   		{name:'g.name',index:'g.name',sortable:false},
		   		{name:'v.name',index:'v.name',sortable:false},
		   		{name:'mspRate',index:'mspRate',sortable:false},
		   		{name:'receiptNo',index:'receiptNo',sortable:false},
		   		
		   		//{name:'unit',index:'unit',sortable:false},
		   		{name:'noOfBags',index:'noOfBags',sortable:false},
		   		{name:'netWeight',index:'netWeight',sortable:false},
		   		//{name:'totalProVal',index:'totalProVal',sortable:false},
		   		{name:'premiumPrice',index:'premiumPrice',sortable:false},
		   		{name:'totalProWithPremium',index:'totalProWithPremium',sortable:false},
		   		//{name:'totalPrice',index:'totalPrice',sortable:false},
		   		//{name:'totalPricePremium',index:'totalPricePremium',sortable:false},
		   		{name:'trash',index:'trash',sortable:false},
		   		{name:'moisture',index:'moisture',sortable:false},
		   		{name:'stapleLen',index:'stapleLen',sortable:false},
		   		{name:'kowdi_kapas',index:'kowdi_kapas',sortable:false},
		   		{name:'yellow_cotton',index:'yellow_cotton',sortable:false},
		   		{name:'photo',index:'photo',sortable:false,align:'center',search:false},
		   	</s:if>
		   	<s:else>
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
		   			{name:'procurementDate',index:'procurementDate',sortable:false,frozen:true},
		   			{name:'warehouse',index:'warehouse',sortable:false},
		   			{name:'village',index:'village',sortable:false},
		   			{name:'farmerName',index:'farmerName',sortable:false},
		   			{name:'farmerCode',index:'farmerCode',sortable:false},
		   			{name:'p.name',index:'p.name',sortable:false},
		   			{name:'v.name',index:'v.name',sortable:false},
		   			{name:'g.name',index:'g.name',sortable:false},
		   			{name:'totalProWithPremium',index:'totalProWithPremium',sortable:false},
		   			{name:'netWeight',index:'netWeight',sortable:false},
		   			{name:'noOfBags',index:'noOfBags',sortable:false},
		   			{name:'photo',index:'photo',sortable:false,align:'center',search:false},
		   </s:else>
		   <s:if test='currentTenantId.equalsIgnoreCase("livelihood")'>
		            {name:'location',index:'locaton',sortable:false,align:'center',search:false}
		   </s:if>
			  	],
		     
		   
		   height: 301, 
		   width: $("#baseDiv").width(),
		   scrollOffset: 19,
		   rowNum:10,
		   shrinkToFit:false,
		   rowList : [10,25,50],
		   viewrecords: true,  

	        

		   sortname:'id',
		   sortorder: "desc",
		   subGrid: false,
		   subGridOptions: {
		   "plusicon" : "glyphicon-plus-sign",
		   "minusicon" : "glyphicon-minus-sign",
		   "openicon" : "glyphicon-hand-right",
		   },
		    
				
			
	    onSortCol: function (index, idxcol, sortorder) {
	        if (this.p.lastsort >= 0 && this.p.lastsort !== idxcol
	                && this.p.colModel[this.p.lastsort].sortable !== false) {
	            $(this.grid.headers[this.p.lastsort].el).find(">div.ui-jqgrid-sortable>span.s-ico").show();
	        }
	    }
		});	
		

		$('#detail').jqGrid('setGridHeight',(reportWindowHeight));	
		jQuery("#detail").jqGrid('navGrid','#pagerForDetail',{edit:false,add:false,del:false,search:false,refresh:false});
		
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
	    
	    
	    jQuery("#generate").click( function() {
			reloadGrid();	
		}); 
		 
		jQuery("#clear").click( function() {
			resetReportFilter();
			clear();
		});
		

		function reloadGrid(){
			

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
			}else{
				jQuery("#detail").jqGrid('setGridParam',{url:"procurementTraceabilityReport_data.action",page:1}).trigger('reloadGrid');	
			}	
			populateFarmerMethod();
			//jQuery("#detail").jqGrid('setGridParam',{url:"procurementTraceabilityReport_data.action",page:1}).trigger('reloadGrid');	
		}

		
		function clear(){
			//resetReportFilter();
			document.form.submit();
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
	  
	  function buttonMapEdcCancel(){			
			document.getElementById("mapModel-close-edu-btn").click();	
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
						mbody+='<img src="procurementTraceabilityReport_populateImage.action?id='+str_array[i]+'"/>';
						mbody+="</div>";
					}else{
						mbody="";
						mbody="<div class='item'>";
						mbody+='<img src="procurementTraceabilityReport_populateImage.action?id='+str_array[i]+'"/>';
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
			jQuery("#detail").jqGrid('excelExport', {url: 'procurementTraceabilityReport_populateXLS.action?'});
		}else{
		     alert('<s:text name="export.nodata"/>');
		}
		
	}	
	
	function populateFarmerMethod() {
		
		var branchIdParma='<s:property value="getBranchId()"/>';
		var startDate=document.getElementById("hiddenStartDate").value;  
		var endDate=document.getElementById("hiddenEndDate").value;
		var farmerName=document.getElementById("farmerId").value;
		var product=document.getElementById("productId").value;
		var warehouse=document.getElementById("warehouseId").value;
		var village=document.getElementById("village").value;
		if(tenant=="chetna"){
		var taluk=document.getElementById("city").value;
		var fpo=document.getElementById("fCooperative").value;
		var season=document.getElementById("selectedSeason").value;
		var ics=document.getElementById("ics").value;
		}
		
		
		$.post("procurementTraceabilityReport_populateFarmerMethod",{ics:ics,village:village,city:taluk,fCooperative:fpo,farmerName:farmerName,product:product,warehouse:warehouse,branchIdParma:branchIdParma,startDate:startDate,endDate:endDate,selectedSeason:season},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalQty").html(json[0].totalQty);
	 		$("#totalAmt").html(json[0].totalAmt);
	 		$("#totalNoOfBags").html(json[0].totalNoOfBags);
	 		
	 	});
	}
	 	
	function initMap() {
		  map = new google.maps.Map(document.getElementById('map'), {
		    center: {lat: 11, lng: 79},
		    zoom: 10,
		  });

		  Popup = createPopupClass();
		  popup = new Popup(
		      new google.maps.LatLng(11, 79),
		      document.getElementById('content'));
		  popup.setMap(map);		  
		}
	
	function createPopupClass() {
		  function Popup(position, content) {
		    this.position = position;

		    content.classList.add('popup-bubble');
		   
		    var bubbleAnchor = document.createElement('div');
		    bubbleAnchor.classList.add('popup-bubble-anchor');
		    bubbleAnchor.appendChild(content);
		    
		    this.containerDiv = document.createElement('div');
		    this.containerDiv.classList.add('popup-container');
		    this.containerDiv.appendChild(bubbleAnchor);
		    
		    google.maps.OverlayView.preventMapHitsAndGesturesFrom(this.containerDiv);
		  }  
		  Popup.prototype = Object.create(google.maps.OverlayView.prototype);

		  /** Called when the popup is added to the map. */
		  Popup.prototype.onAdd = function() {
		    this.getPanes().floatPane.appendChild(this.containerDiv);
		  };

		  /** Called when the popup is removed from the map. */
		  Popup.prototype.onRemove = function() {
		    if (this.containerDiv.parentElement) {
		      this.containerDiv.parentElement.removeChild(this.containerDiv);
		    }
		  };

		  /** Called each frame when the popup needs to draw itself. */
		  Popup.prototype.draw = function() {
		    var divPosition = this.getProjection().fromLatLngToDivPixel(this.position);

		    // Hide the popup when it is far out of view.
		    var display =
		        Math.abs(divPosition.x) < 4000 && Math.abs(divPosition.y) < 4000 ?
		        'block' :
		        'none';

		    if (display === 'block') {
		      this.containerDiv.style.left = divPosition.x + 'px';
		      this.containerDiv.style.top = divPosition.y + 'px';
		    }
		    if (this.containerDiv.style.display !== display) {
		      this.containerDiv.style.display = display;
		    }
		  };

		  return Popup;
		}
	
	function showPopupMap(lat,lng,title){
		var myLatLng = {lat: lat, lng: lng};
		var marker = new google.maps.Marker({
	      position: myLatLng,
	      map: map,
	      title: ''
	    });
		map.setCenter(myLatLng);
		map.setZoom(10);
	}
		
	function initMapModalItem(lat,lng) {
		  Popup = createPopupClass();
		  popup = new Popup(
		      new google.maps.LatLng(lat, lng),
		      document.getElementById('content'));		  
		  popup.setMap(map);
		}
		
		function popupMapWindow(lat,lng,title) {
			try{
				var mMapbody="";
				initMapModalItem(lat,lng);		
				showPopupMap(lat,lng,title);
				document.getElementById("enableMapModal").click();	
			}
			catch(e){
				alert(e);
				}
		}
	</script>

	<s:form name="form" action="procurementTraceabilityReport_list">
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
					<div class='filterEle'>
						<label for="txt"><s:text name="date" /></label>
						<div class="form-element">
							<input id="daterange" name="daterange" id="daterangepicker"
								class="form-control input-sm" />
						</div>
					</div>
				
					<div class='filterEle'>
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="farmerName" id="farmerId" list="farmerNameList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>


					<s:if
						test='isMultiBranch=="1"&&(getIsParentBranch()==1||branchId==null)'>
						<s:if test='branchId==null'>
							<div class='filterEle'>
								<label for="txt"><s:text name="app.branch" /></label>
								<div class="form-element">
									<s:select name="branchIdParma" id="branchIdParma"
										list="parentBranches" headerKey="" headerValue="Select"
										cssClass="input-sm form-control select2"
										onchange="populateChildBranch(this.value)" />
								</div>
							</div>
						</s:if>
						<div class='filterEle'>
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
							<div class='filterEle'>
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

					<div class="filterEle">
						<label for="txt">
								<s:property
						value="%{getLocaleProperty('cooperative')}" /></label>
						<div class="form-element">
							<s:select name="warehouse" id="warehouseId" list="warehouseList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>

					<div class="filterEle">
						<label for="txt">
								<s:text name="product" /></label>
						<div class="form-element">
							<s:select name="product" id="productId" list="productList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<s:if test="currentTenantId!='livelihood'">
					<div class="filterEle">
						<label for="txt">
								<s:text name="%{getLocaleProperty('icsName')}" /></label>
						<div class="form-element">
							<s:select name="ics" id="ics" list="icsList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div></s:if>

					<div class="filterEle">
						<label for="txt"><s:property
							value="%{getLocaleProperty('villageName')}" />
								</label>
						<div class="form-element">
							<s:select name="village" id="village" list="villageList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<s:if test="currentTenantId!='livelihood'">
					<div class="filterEle">
						<label for="txt">
								<s:property value="%{getLocaleProperty('city')}" /></label>
						<div class="form-element">
							<s:select name="city" id="city" list="cityList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('cooperative.name')}" /></label>
						<div class="form-element">
							<s:select name="fCooperative" id="fCooperative"
								list="fCooperativeList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
				
					<div class="filterEle">
						<label for="txt"><s:property value="%{getLocaleProperty('season')}" /></label>
						<div class="form-element">
							<s:select name="selectedSeason" id="selectedSeason"
								list="seasonList" headerKey=""
								headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
						</s:if>
					
					<div class='filterEle' style="margin-top: 2%; margin-right: 0%;">
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


	<%-- GRID SECTION --%>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
<s:if test="currentTenantId=='chetna'"> 
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span> <s:text name="totalQty" />&nbsp;<span class="strong"
							id="totalQty"></span>&nbsp;kgs
						</span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="finalAmt" />&nbsp;<span class="strong"
							id="totalAmt"></span>&nbsp; <s:property
								value="%{getCurrencyType().toUpperCase()}" /></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="totalNoOfBags" />&nbsp;<span
							class="strong" id="totalNoOfBags"></span></span>
					</div>

				</div>
			</div>
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


	<div style="width: 99%;" id="baseDiv">
		<table id="detail"></table>
		<div id="pagerForDetail"></div>
	</div>

	<button type="button" id="enableModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
	<button type="button" id="enableMapModal"
		class="hide slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#mapSlideModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
	</button>
	
	<div id="mapSlideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content" style="height:450px;width:650px;margin-left: -150px;">
				<div class="modal-header">
					<button type="button" id="mapModel-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead"></h4>
				</div>
				<div class="modal-body" style="height:350px;width:650px;">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<div class="carousel-inner" role="listbox" id="mMapbody">
						<div id='map'></div><div id='content'></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonMapEdcCancel();">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>


	<div id="slideModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-edu-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead"></h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<div class="carousel-inner" role="listbox" id="mbody"></div>

						<a class="left carousel-control" href="#myCarousel" role="button"
							data-slide="prev"> <span
							class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a> <a class="right carousel-control" href="#myCarousel"
							role="button" data-slide="next"> <span
							class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
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


	<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>

</script><script
		src="https://maps.googleapis.com/maps/api/js?client=gme-sourcetrace&v=3.28&libraries=geometry,drawing,places&callback=initMap"></script>
</body>

