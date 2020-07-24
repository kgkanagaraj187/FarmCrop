<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>

<html>
<head>
<!-- add this meta information to select layout  -->
 <link href="plugins/pluginsLatest/bootstrap-4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
 <link href="plugins/pluginsLatest/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">
 <link href="plugins/pluginsLatest/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">
<META name="decorator" content="swithlayout">
</head>
<style>
/** top tiles  */
.tile_count {
  margin-bottom: 20px;
  margin-top: 20px; }

.tile_count .tile_stats_count {
  border-bottom: 1px solid #D9DEE4;
  padding: 0 10px 0 20px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  position: relative; }

@media (min-width: 992px) {
  footer {
    margin-left: 230px; } }

@media (min-width: 992px) {
  .tile_count .tile_stats_count {
    margin-bottom: 10px;
    border-bottom: 0;
    padding-bottom: 10px; } }

.tile_count .tile_stats_count:before {
  content: "";
  position: absolute;
  left: 0;
  height: 65px;
  border-left: 2px solid #ADB2B5;
  margin-top: 10px; }

@media (min-width: 992px) {
  .tile_count .tile_stats_count:first-child:before {
    border-left: 0; } }

.tile_count .tile_stats_count .count {
  font-size: 30px;
  line-height: 47px;
  font-weight: 600; }

@media (min-width: 768px) {
  .tile_count .tile_stats_count .count {
    font-size: 40px; } }

@media (min-width: 992px) and (max-width: 1100px) {
  .tile_count .tile_stats_count .count {
    font-size: 30px; } }

.tile_count .tile_stats_count span {
  font-size: 12px; }

@media (min-width: 768px) {
  .tile_count .tile_stats_count span {
    font-size: 13px; } }

.tile_count .tile_stats_count .count_bottom i {
  width: 12px; }

.ash{
color:#73879C;
}
/** /top tiles **/

/** tile stats **/
.tile-stats {
  position: relative;
  display: block;
  margin-bottom: 12px;
  border: 1px solid #E4E4E4;
  -webkit-border-radius: 5px;
  overflow: hidden;
  padding-bottom: 5px;
  -webkit-background-clip: padding-box;
  -moz-border-radius: 5px;
  -moz-background-clip: padding;
  border-radius: 5px;
  background-clip: padding-box;
  background: #FFF;
  -webkit-transition: all 300ms ease-in-out;
  transition: all 300ms ease-in-out; }

.tile-stats:hover .icon i {
  animation-name: tansformAnimation;
  animation-duration: .5s;
  animation-iteration-count: 1;
  color: rgba(58, 58, 58, 0.41);
  animation-timing-function: ease;
  animation-fill-mode: forwards;
  -webkit-animation-name: tansformAnimation;
  -webkit-animation-duration: .5s;
  -webkit-animation-iteration-count: 1;
  -webkit-animation-timing-function: ease;
  -webkit-animation-fill-mode: forwards;
  -moz-animation-name: tansformAnimation;
  -moz-animation-duration: .5s;
  -moz-animation-iteration-count: 1;
  -moz-animation-timing-function: ease;
  -moz-animation-fill-mode: forwards; }

.tile-stats .icon {
  width: 20px;
  height: 20px;
  color: #BAB8B8;
  position: absolute;
  right: 53px;
  top: 22px;
  z-index: 1; }

.tile-stats .icon i {
  margin: 0;
  font-size: 60px;
  line-height: 0;
  vertical-align: bottom;
  padding: 0; }

.tile-stats .count {
  font-size: 38px;
  font-weight: bold;
  line-height: 1.65857143; }

.tile-stats .count, .tile-stats h3, .tile-stats p {
  position: relative;
  margin: 0;
  margin-left: 10px;
  z-index: 5;
  padding: 0; }

.tile-stats h3 {
  color: #BAB8B8; }

.tile-stats p {
  margin-top: 5px;
  font-size: 12px; }

.tile-stats > .dash-box-footer {
  position: relative;
  text-align: center;
  margin-top: 5px;
  padding: 3px 0;
  color: #fff;
  color: rgba(255, 255, 255, 0.8);
  display: block;
  z-index: 10;
  background: rgba(0, 0, 0, 0.1);
  text-decoration: none; }

.tile-stats > .dash-box-footer:hover {
  color: #fff;
  background: rgba(0, 0, 0, 0.15); }

.tile-stats > .dash-box-footer:hover {
  color: #fff;
  background: rgba(0, 0, 0, 0.15); }

table.tile_info {
  padding: 10px 15px; }

table.tile_info span.right {
  margin-right: 0;
  float: right;
  position: absolute;
  right: 4%; }

.tile:hover {
  text-decoration: none; }

.tile_header {
  border-bottom: transparent;
  padding: 7px 15px;
  margin-bottom: 15px;
  background: #E7E7E7; }

.tile_head h4 {
  margin-top: 0;
  margin-bottom: 5px; }

.tiles-bottom {
  padding: 5px 10px;
  margin-top: 10px;
  background: rgba(194, 194, 194, 0.3);
  text-align: left; }

/** /tile stats **/

/** bootstrap-progressbar  **/
.progress {
  border-radius: 0; }

.progress-bar-info {
  background-color: #3498DB; }

.progress-bar-success {
  background-color: #26B99A; }

.progress_summary .progress {
  margin: 5px 0 12px !important; }

.progress_summary .row {
  margin-bottom: 5px; }

.progress_summary .row .col-xs-2 {
  padding: 0; }

.progress_summary .more_info span {
  text-align: right;
  float: right; }

.progress_summary .data span {
  text-align: right;
  float: right; }

.progress_summary p {
  margin-bottom: 3px;
  width: 100%; }

.progress_title .left {
  float: left;
  text-align: left; }

.progress_title .right {
  float: right;
  text-align: right;
  font-weight: 300; }

.progress.progress_sm {
  border-radius: 0;
  margin-bottom: 18px;
  height: 10px !important; }

.progress.progress_sm .progress-bar {
  height: 10px !important; }

.dashboard_graph p {
  margin: 0 0 4px; }

ul.verticle_bars {
  width: 100%; }

ul.verticle_bars li {
  width: 23%;
  height: 200px;
  margin: 0; }

.progress.vertical.progress_wide {
  width: 35px; }

/** bootstrap-progressbar  **/

.blue {
  color: #3498DB; }

.purple {
  color: #9B59B6; }

.green {
  color: #1ABB9C; }

.aero {
  color: #9CC2CB; }

.red {
  color: #E74C3C; }

.dark {
  color: #34495E; }

.border-blue {
  border-color: #3498DB !important; }

.border-purple {
  border-color: #9B59B6 !important; }

.border-green {
  border-color: #1ABB9C !important; }

.border-aero {
  border-color: #9CC2CB !important; }

.border-red {
  border-color: #E74C3C !important; }

.border-dark {
  border-color: #34495E !important; }

.bg-white {
  background: #fff !important;
  border: 1px solid #fff !important;
  color: #73879C; }

.bg-green {
  background: #1ABB9C !important;
  border: 1px solid #1ABB9C !important;
  color: #fff; }

.bg-red {
  background: #E74C3C !important;
  border: 1px solid #E74C3C !important;
  color: #fff; }

.bg-blue {
  background: #3498DB !important;
  border: 1px solid #3498DB !important;
  color: #fff; }

.bg-orange {
  background: #F39C12 !important;
  border: 1px solid #F39C12 !important;
  color: #fff; }

.bg-purple {
  background: #9B59B6 !important;
  border: 1px solid #9B59B6 !important;
  color: #fff; }

.bg-blue-sky {
  background: #50C1CF !important;
  border: 1px solid #50C1CF !important;
  color: #fff; }

/** HighCharts  **/

.highcharts-figure, .highcharts-data-table table {
    min-width: 360px; 
    max-width: 800px;
    margin: 1em auto;
}

.highcharts-data-table table {
	font-family: Verdana, sans-serif;
	border-collapse: collapse;
	border: 1px solid #EBEBEB;
	margin: 10px auto;
	text-align: center;
	width: 100%;
	max-width: 500px;
}
.highcharts-data-table caption {
    padding: 1em 0;
    font-size: 1.2em;
    color: #555;
}
.highcharts-data-table th {
	font-weight: 600;
    padding: 0.5em;
}
.highcharts-data-table td, .highcharts-data-table th, .highcharts-data-table caption {
    padding: 0.5em;
}
.highcharts-data-table thead tr, .highcharts-data-table tr:nth-child(even) {
    background: #f8f8f8;
}
.highcharts-data-table tr:hover {
    background: #f1f7ff;
} 

.metrics {
    font-size: 42px;
    font-weight: normal !important;
    color: #1ABB9C;
}
.metrics span {
    font-size: 14px;
}
.info {
    font-size: 14px;
     color:#73879C;
    font-weight: normal !important;
}
.metricsspan {
    font-size: 14px;
    font-weight: normal !important;   
}
.chartHt {
  height: 160px; }
</style>
<body>
		
<script src="js/jquery-3.4.1.min.js"></script>
<script src="plugins/pluginsLatest/bootstrap-4.4.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="plugins/pluginsLatest/bootstrap-progressbar/bootstrap-progressbar.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
<link href="https://fonts.googleapis.com/css?family=Aclonica|Averia+Serif+Libre|Carter+One|Changa+One|Goblin+One|Merienda+One|Merriweather|Merriweather+Sans|Mogra|Paprika|Roboto|Roboto+Slab|Salsa|Sansita|Spicy+Rice|Viga" rel="stylesheet" />
	
<!-- Highcharts -->
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/data.js"></script>
<script src="https://code.highcharts.com/modules/drilldown.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<script src="https://code.highcharts.com/modules/series-label.js"></script>	
<script src="https://code.highcharts.com/modules/variable-pie.js"></script>
<script src="https://code.highcharts.com/highcharts-3d.js"></script>
<script src="https://code.highcharts.com/modules/cylinder.js"></script>

<script src="plugins/echarts/echarts.min.js?v=2.4"></script>
<script src="js/agrodashboard-charts.js?vk=120.2.54"></script>
          
       <!-- top tiles- -->
       				<div class="col-md-12">
					<div class="pageContainerDivWrapper">					
					<div id="OverallSummary" class="chartHt"> 															
					<table border="0" width="100%">
					<tr>
					<td width="15%"><div width="15%"><span id="TT1" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Users</div></div></td> 					
					<td width="15%"><div width="15%"><span id="TT2" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Mobile Users</div></div></td>
					<td width="15%"><div width="15%"><span id="TT3" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Devices</div></div></td>
					<td width="15%"><div width="15%"><span id="TT4" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Farmers</div></div></td>
					<td width="15%"><div width="15%"><span id="TT5" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Total Farms</div></div></td>
					<td width="15%"><div width="15%"><span id="TT6" class="metrics"></span>&nbsp;<hr  width="80%" align="left"><div class="info"><i class="fa fa-user"></i> Warehouses</div></div></td>
					</tr>
					</table>					
					</div>	
					</div></div>            
        <!-- /top tiles- -->   
            
<div class="row">
      <div class="col-md-12 col-sm-12 ">
           <div class="dashboard_graph">
              
<!-- CHART_1 -->
                <div class="row x_title">
                  <div class="col-md-6">
                    <h3>Transactions <small></small></h3>
                  </div>
                  <div class="col-md-6">                  
                    <div class="pull-right">
					<div class="dropdown pull-right drpdwnMnu">

						 <button onclick="filterDrop('dateFilter')" type="button"
							class="btn btn-sm btn-primary form-control filterBtn"><i class="glyphicon glyphicon-filter"></i>
						</button> 
						<div style="width: 1000%; background-color: white" class="dropdown-menu custDropMenu dateFilter">
                    			 <li><div class="filterRows">
											<div id="reportrangeDiv">
												<input type="text" class="form-control"id="reportrange1" data-date-format="dd/mm/yyyy" />
											</div>
										<div id="searchProcDiv">
											<button type="button" style="margin-top: 2%"
													onclick="loadGroupTxnChart()"class="btn btn-success">
													<i class="fa fa-search"></i><s:text name="search" />
											</button>
											<button type="button" style="margin-top: 2%"
													onclick="setDateRange();loadGroupTxnChart();"
													class="btn btn-success"><s:text name="clear" />
											</button>
										</div>
									</div></li>
						 </div></div></div>                   
               </div></div>
                <div class="col-md-9 col-sm-9 ">
                  <div id="chart_plot_01" class="demo-placeholder"></div>
                </div>
                
                <div class="col-md-3 col-sm-3  bg-white">
                  <div class="x_title">
                    <h2>Transaction Summary</h2>
                    <div class="clearfix"></div>
                  </div>

                  <div class="col-md-12 col-sm-12 ">
                    <div><p>Distribution</p>
                      <div class="">
                        <div class="progress progress_sm" style="width: 76%;">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="75" aria-valuemin="0" aria-valuemax="100"></div>
                        </div></div></div>
                    <div><p>Crop Harvest</p>
                      <div class="">
                        <div class="progress progress_sm" style="width: 76%;">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="5" aria-valuemin="0" aria-valuemax="100"></div>
                        </div></div></div>
                  </div>
                  <div class="col-md-12 col-sm-12 ">
                    <div><p>Crop Sale</p>
                      <div class="">
                        <div class="progress progress_sm" style="width: 76%;">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="55" aria-valuemin="0" aria-valuemax="100"></div>
                        </div></div></div>
                    <div><p>Enrollment</p>
                      <div class="">
                        <div class="progress progress_sm" style="width: 76%;">
                          <div class="progress-bar bg-green" role="progressbar" data-transitiongoal="95" aria-valuemin="0" aria-valuemax="100"></div>
                        </div></div></div>
                  </div>
                </div>
                <div class="clearfix"></div>
                
                
<!-- ---------------------- -->             
      
<!-- CHART_2 -->

			<div class="row x_title">
                  <div class="col-md-6">
                    <h3>Farmer Summary <small></small></h3>
                  </div>
                  <div class="col-md-6">                  
                    <div class="pull-right">
					<div class="dropdown pull-right drpdwnMnu">

						<button onclick="filterDrop('dateFilter2')" type="button"
							class="btn btn-sm btn-primary form-control filterBtn"><i class="glyphicon glyphicon-filter"></i>
						</button> 
						<div style="width: 1000%; background-color: white" class="dropdown-menu custDropMenu dateFilter2">
                    			 <li><div class="filterRows">
											<div id="reportrangeDiv">
												<input type="text" class="form-control" id="reportrange2" data-date-format="dd/mm/yyyy" />
											</div>
										<div id="searchProcDiv">
											<button type="button" style="margin-top: 2%"
													onclick="" class="btn btn-success">
													<i class="fa fa-search"></i><s:text name="search" />
											</button>
											<button type="button" style="margin-top: 2%"
													onclick="setDateRange();"
													class="btn btn-success"><s:text name="clear" />
											</button>
										</div>
									</div></li>
						 </div></div></div>                   
               </div></div>
<div class="col-md-9 col-sm-9 ">
        <div id="chart_plot_02" class="demo-placeholder"></div>
</div>
<!-- ---------------------- -->  

</div>
</div>
</div>

<script type="text/javascript">

$(document).ready(function() {
	var tenantId =getCurrentTenantId();
	populateMethod();
	setDateRange();
	formCharts();
				
    $('.progress .progress-bar').progressbar();
    
});
function setDateRange(){
	
	 var start = moment().subtract(29, 'days');
	    var end = moment();
				
	     function cb(start, end) {	        
	        $('#reportrange1').val(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	    } 
	      
	    $('#reportrange1').daterangepicker({
	        startDate: start,
	        endDate: end,
	        ranges: {
	            'Today': [moment(), moment()],
	            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
	            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
	            'This Month': [moment().startOf('month'), moment().endOf('month')],
	            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
	            'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
	         }
	    }, cb);

	cb(start, end);
}

function filterDrop(className){
	$("."+className).slideToggle("slow");
}

function getCurrentBranch() {
	return "<s:property value='branchId'/>";
}

function getCurrentTenantId() {
	return "<s:property value='getCurrentTenantId()'/>";
}

function formCharts(){
	var branchId =getCurrentBranch();
	loadGroupTxnChart();
	//alert(branchId);
	if(isEmpty(branchId)){
		loadFarmerSummaryChart();		
	}else{
		loadFarmerSummaryChartCountry(branchId);
	}
}

function populateMethod() {
	jQuery.post("agrodashboard_populateMethod.action", {},
			function(result) {
				var valuesArr = $.parseJSON(result);
				$.each(valuesArr, function(index, value) {										
					jQuery('#TT1').text(value.userCount);
					jQuery('#TT2').text(value.mobileUsersCount);				
					jQuery('#TT4').text(value.farmerCount);
					jQuery('#TT3').text(value.deviceCount);				
					jQuery('#TT6').text(value.warehouseCount);					
					jQuery('#TT5').text(value.farmCount);
				});
			});
}
</script>
	</body>