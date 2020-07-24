<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
<script src="assets/common/openlayers/OpenLayers.js"></script>
</head>

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
<script
	src="plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
<script src="plugins/highcharts/charts.js"></script>
<script src="plugins/highcharts/data.js"></script>
<script src="plugins/highcharts/drilldown.js"></script>
<script src="plugins/highcharts/charts-3d.js"></script>
<script src="js/procurementReportCharts.js"></script>
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

	var enableTraceability = "<s:property value='enableTraceability'/>";
	var branchId='<s:property value="branchId"/>';
	jQuery(document).ready(function(){
		
		onLoadValues();
		loadCustomPopup();
		loadFields();
		if(tenantId=="kpf"||tenantId=="KPF"){
			var columnNames ;
			loadKPFGrid();
		}else{
			var colNames='<s:property value="mainGridCols"/>';
			loadGrid();
		}
		
			$("#reset").click(function(){
			var url = (window.location.href);
			window.location.href=url;
			$(".select2").val("");
			
		})	
		
	});
	var data = new Array();
	
	function loadGrid(){

		var gridColumnNames = new Array();
		var gridColumnModels = new Array();
		
		var subGridColumnNames = new Array();
		var subGridColumnModels = new Array();
		
		var colNames='<s:property value="mainGridCols"/>';
		var subColNames='<s:property value="subGridCols"/>';
		
		$(colNames.split("%")).each(function(k,val){
				if(!isEmpty(val)){
					var cols=val.split("#");
					gridColumnNames.push(cols[0]);
					//alert(cols[0]);
					//alert(cols[2]);
					gridColumnModels.push({name: cols[0],align: cols[2],sortable: false,frozen:true});
				}
		});
		
		$(subColNames.split("%")).each(function(k,val){
			if(!isEmpty(val)){	
				var cols=val.split("#");
				subGridColumnNames.push(cols[0]);
				subGridColumnModels.push({name: cols[0],align: cols[2],sortable: false,frozen:true});
			}
		});
		
		
		
		jQuery("#detail").jqGrid(
				{
					url:'procurementReport_detail.action?',
				   	pager: '#pagerForDetail',
				   	datatype: "json",
				   	mtype: 'POST',				   	
				   	colNames:gridColumnNames,
				   	colModel:gridColumnModels,
				   	height: 301, 
				   	width: $("#baseDiv").width(),
				   	scrollOffset: 19,     
				   	sortname:'id',
				   	shrinkToFit:true,
				   	sortorder: "desc",
				   	rowNum:10,
				   	rowList : [10,25,50],
				   	viewrecords: true,  	
				   	subGrid: true,
				   // footerrow: true,
				    /* loadComplete: function () {
	                    var $self = $(this),
                        sum = $self.jqGrid("getCol", "Total No of Bags", false, "sum");
                    	sum1 = $self.jqGrid("getCol", "Total Gross Weight", false, "sum");
                    	sum2 = $self.jqGrid("getCol", "Payment Amount", false, "sum");
                     	total = "Total :";
                     	alert(sum);
	                    $self.jqGrid("footerData", "set", {invdate: "Total:", Village: total});
	                    $self.jqGrid("footerData", "set", {invdate: "Total:", Season: sum1});
	                    $self.jqGrid("footerData", "set", {invdate: "Total:", Farmer: sum1});
	                     $self.jqGrid("footerData", "set", {invdate: "Total:", Payment Amount: sum2});  
	                    
	                }, */
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
						   url:'procurementReport_subGridDetail.action?id='+row_id,
						   pager: pager_id,
						   datatype: "json",	
						   colNames:subGridColumnNames,
						   colModel:subGridColumnModels,
						    sortname:'id',
						    height: '100%', 
						    sortorder: "desc",
						    scrollOffset: 0,
						    shrinkToFit:true,
						   // autowidth: true,
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
				jQuery("#detail").jqGrid('setFrozenColumns');
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
	
	function loadKPFGrid(){
	
		jQuery("#detail").jqGrid(
				{
					url:'procurementReport_detail.action?',
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
						  "farmerId" : function(){
							  return document.getElementById("farmerId").value;
						  },
						  "villageName" : function(){
							  return document.getElementById("villageName").value;
						  },
						   "seasonCode" : function(){
							   return document.getElementById("seasonCode").value;
						  }
						},	
				   	colNames:[
				   	   '<s:property value="%{getLocaleProperty('report.date')}" />' ,
				   	   '<s:property value="%{getLocaleProperty('report.month')}" />' ,
				   	   '<s:property value="%{getLocaleProperty('report.year')}" />' ,
				   		'<s:property value="%{getLocaleProperty('invoiceNo')}" />' ,
				    	'<s:property value="%{getLocaleProperty('profile.cooperative')}" />',
				    	'<s:property value="%{getLocaleProperty('agentName')}" />',
						'<s:property value="%{getLocaleProperty('cropType')}" />',
						'<s:text name="supplierType"/>',
						'<s:text name="supplierName"/>',
						'<s:text name="farmerName"/>',
						
						'<s:property value="%{getLocaleProperty('village.name')}" />',						
			  		  	//'<s:text name="totalNetwt"/>',
			  		  	//'<s:text name="totalGrossWeight"/>',
			  		  	 '<s:property value="%{getLocaleProperty('totalGrossWeight')}" />',
			  		  	'<s:property value="%{getLocaleProperty('txnAmount')}" />',
			  		  	
				   		<s:if test="currentTenantId!='kpf'|| currentTenantId!='wub'">
				  	
				  		 '<s:property value="%{getLocaleProperty('invoiceCost')}" />' ,
				  		 '<s:property value="%{getLocaleProperty('recieptNo')}" />' ,
				  		 </s:if>
				  		//'<s:text name="map"/>' 
		 	      	 ],
				   	colModel:[
						{name:'procurementDate',index:'procurementDate',width:250,sortable:false},
						{name:'procurementMonth',index:'procurementMonth',width:350,sortable:false},
						{name:'procurementYear',index:'procurementYear',width:250,sortable:false},
						{name:'invoiceNo',index:'invoiceNo',width:250,sortable:false},
						{name:'cooperative',index:'cooperative',width:350,sortable:false},
						{name:'agentName',index:'agentName',width:350,sortable:false},
						{name:'cropType',index:'cropType',width:350,sortable:false},
						{name:'supplierType',index:'supplierType',width:350,sortable:false},
						{name:'supplierName',index:'supplierName',width:350,sortable:false},
						{name:'farmerName',index:'farmerName',width:350,sortable:false},
						{name:'v.name',index:'v.name',width:350,sortable:false},						
				   		//{name:'totalNetwt',index:'totalNetwt',width:250,sortable:false,align:'right'},	
				   		{name:'totalGrossWeight',index:'totalGrossWeight',width:300,sortable:false,align:'right'},
				   		{name:'totalAmount',index:'totalAmount',width:300,sortable:false,align:'right'},
				   		<s:if test="currentTenantId!='kpf'|| currentTenantId!='wub'">
				   		{name:'invoiceCost',index:'invoiceCost',width:300,sortable:false,align:'right'},
				   		{name:'recieptNo',index:'recieptNo',width:400,sortable:false,align:'right'},
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
							url:'procurementReport_subGridDetail.action?id='+row_id,
						   	pager: pager_id,
						   	datatype: "json",	
						   	colNames:[	
						   	       '<s:text name="sno"/>',
									  '<s:text name="productName"/>',
						  		   	  '<s:text name="grade"/>',	
						  		   	'<s:property value="%{getLocaleProperty('unit')}" />',
						  		   	<s:if test='enableTraceability=="1"'>
							  		    '<s:text name="batchNo"/>',
							  			</s:if>
							  		  '<s:property value="%{getLocaleProperty('batchNo')}" />',
							  		  '<s:property value="%{getLocaleProperty('cstPrice')}" />',
						  			  '<s:property value="%{getLocaleProperty('grossWeight')}" />',
						  			
						  		   	//  '<s:text name="grossWeight"/>',
						  		   	'<s:property value="%{getLocaleProperty('totalAmt')}" />'
						  		    
				 	      	 ],
						   	colModel:[	
									{name:'sno',index:'sno',width:50,sortable:false},
									{name:'procurementProduct.name',index:'procurementProduct.name',width:150,sortable:false},
		   							{name:'grade',index:'grade',width:150,sortable:false},
		   							{name:'unit',index:'unit',width:100,sortable:false},
		   							<s:if test='enableTraceability=="1"'>
		   							{name:'batchNo',index:'batchNo',width:150,sortable:false},
						  			</s:if>
		   							{name:'cstPrice',index:'cstPrice',width:150,sortable:false},
		   							{name:'grossWeight',index:'grossWeight',width:150,sortable:false,align:'right'},
		   							
		   							{name:'totalAmt',index:'totalAmt',width:150,sortable:false,align:'right'}
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
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			if(d2.length>1){
				var value1= d2[0];
				var value2= d2[1];
				document.getElementById("hiddenStartDate").value=value1;
				document.getElementById("hiddenEndDate").value=value2;
			}
			var dataa={
					filterList:json,
					startDate:value1,
					endDate:value2
			};
			var json = JSON.stringify(data);
			
			var totalNetwt=$("#totalNetwt").text();
			var txnAmount=$("#txnAmount").text();
			var totalPayAmt=$("#totalPayAmt").text();
			headerData='<s:text name="totalNetwt"/>(Kgs)'+"###"+totalNetwt+"###"+'<s:text name="txnAmount"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+txnAmount+"###"+'<s:text name="txnAmt"/> (<s:property value="%{getCurrencyType().toUpperCase()}" />)'+"###"+totalPayAmt;
			jQuery("#detail").setGridParam ({postData: {rows : 0,headerFields:headerData}});

			jQuery("#detail").jqGrid('excelExport', {url: 'procurementReport_populateXLS.action?',postData:dataa});
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
		if(tenantId=='kpf'||tenantId=='wub')
		{
			document.getElementById('farmerId').selectedIndex=0;
			document.getElementById('villageName').selectedIndex=0;
			document.getElementById('seasonCode').selectedIndex=0;
		}
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
		$("#filterListData").val(json);
		
			var d1=	jQuery('#daterange').val();
			var d2= d1.split("-");
			if(d2.length>1){
				var value1= d2[0];
				var value2= d2[1];
				document.getElementById("hiddenStartDate").value=value1;
				document.getElementById("hiddenEndDate").value=value2;
			}
			var dataa={
					filterList:json,
					startDate:value1,
					endDate:value2
			};
		loadFields();
		jQuery("#detail").jqGrid('setGridParam',{url:'procurementReport_detail.action',page:1,postData:dataa}).trigger('reloadGrid');				
		}	
	
	
</script>
<script src="plugins/openlayers/OpenLayers.js"></script>

	<!-- <script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwN5CwZmhwRU1b0qxHHMVAkx2xwOY9_kU"
		type="text/javascript"></script> -->
	<script type="text/javascript">
//Variables relate to loading MAP
/* var fProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var tProjection   = new OpenLayers.Projection("EPSG:900913");
var url = window.location.href;
var temp = url;
for(var i = 0 ; i < 1 ; i++) {
	temp = temp.substring(0, temp.lastIndexOf('/'));
	}
var href = temp;
var coordinateImg = "red_placemarker.png";
var iconImage = temp + '/img/' + coordinateImg; */
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


/* function loadCustomPopup(){
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
 */
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

	
	function loadFields(){
 		
		var filterDatas=$("#filterListData").val();
		 var startDate=document.getElementById("hiddenStartDate").value;  
		  //alert("startDate:"+startDate);
		  var endDate=document.getElementById("hiddenEndDate").value;
		  //alert("endDate:"+endDate);
		  var branchId='<s:property value="branchId"/>';
		  
		var weightUnit = "<s:property value='%{getLocaleProperty(\"weightUnit\")}'/>";
		var drillDownMsg = "<s:property value='%{getLocaleProperty(\"drillDownMsg\")}'/>";
		var drilldownTooltip1 = "<s:property value='%{getLocaleProperty(\"drilldownTooltip1\")}'/>";
		var drilldownTooltip2 = "<s:property value='%{getLocaleProperty(\"drilldownTooltip2\")}'/>";
		var barChartLegend = "<s:property value='%{getLocaleProperty(\"procurementWeightChartLegend\")}'/>";
		var barChartLegendTxnAmt = "<s:property value='%{getLocaleProperty(\"procurementTxnAmtChartLegend\")}'/>";
		var barChartLegendPayAmt = "<s:property value='%{getLocaleProperty(\"procurementPayAmtChartLegend\")}'/>";
		
	  $.post("procurementReport_populateHeaderFields",{filterList:filterDatas,startDate:startDate,endDate:endDate,branch:branchId},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalNetwt").html(json[0].totalNetwt);
	 		$("#txnAmount").html(json[0].txnAmount);
	 		$("#totalPayAmt").html(json[0].totalPayAmt);
	 	});  	
	 	
	 	/* $.post("procurementReport_populateHeaderFields",{filterList:filterDatas},function(data){
	 		var json = JSON.parse(data);
	 		$("#totalNetwt").html(json[0].totalNetwt);
	 		$("#txnAmount").html(json[0].txnAmount);
	 		$("#totalPayAmt").html(json[0].totalPayAmt);
	 	});  */
	 	
	 //	getChartFieldData(filterDatas,weightUnit,drillDownMsg,drilldownTooltip1,drilldownTooltip2,barChartLegend,barChartLegendTxnAmt,barChartLegendPayAmt);	 	
	}
	
	  </script>	<%-- FILTER SECTION --%>
			
	
		<s:form id="form">
		<s:hidden id="filterListData" name="filterListData"/>
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
				<div class="filterEle">
					<label for="txt"><s:text name="startingDate" /></label>
					<div class="form-element">
						<input id="daterange" name="daterange" id="daterangepicker"
							class="form-control  input-sm" />
					</div>
				</div>
				<s:if test="tenantId=='kpf'||tenantId=='wub'">
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
						<label for="txt"><s:text name="farmer" /></label>
						<div class="form-element">
							<s:select name="filter.agroTransaction.farmerName" id="farmerId" list="farmersList"
								headerKey="" headerValue="%{getText('txt.select')}"
								class="form-control input-sm select2" />
						</div>
					</div>
					<div class="filterEle">
					<label for="txt"><s:text name="village" /></label>
					<div class="form-element">
						<s:select name="filter.village.name" id="villageName" list="villageList"
							headerKey="" headerValue="%{getText('txt.select')}"
							class="form-control input-sm select2" />
					</div>
				</div> 
				</s:if>
				<s:else>
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
					</s:else>
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
			<!-- <div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div> -->
		</s:form>
	
			
			<%-- GRID SECTION --%>
	<div class="appContentWrapper marginBottom">
		<div class="flex-layout reportData">
			<div class="flexItem-2">
				<div class="summaryBlocksWrapper flex-container ">
					<div class="report-summaryBlockItem">
						<span> <s:text name="totalNetwt"/>&nbsp;<span class="strong" id="totalNetwt"></span>&nbsp;kgs</span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="txnAmount"/>&nbsp;<span class="strong" id="txnAmount"></span>&nbsp; <s:property value="%{getCurrencyType().toUpperCase()}" /></span>
					</div>
					<div class="report-summaryBlockItem">
						<span><s:text name="txnAmt"/>&nbsp;<span class="strong" id="totalPayAmt"></span>&nbsp; <s:property value="%{getCurrencyType().toUpperCase()}" /></span>
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

	
</body>
