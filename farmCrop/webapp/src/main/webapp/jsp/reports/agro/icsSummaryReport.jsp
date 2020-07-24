<%@ include file="/jsp/common/grid-assets.jsp"%>
<%@ include file="/jsp/common/report-assets.jsp"%>

<head>
<meta name="decorator" content="swithlayout">
	<link rel="stylesheet" href="css/echarts-dashboard.css" />

</head>

<link rel="stylesheet" href="plugins/datepicker/css/datepicker.css">
<script src="plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet"
	href="plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
		<script src="plugins/echarts/echarts.min.js?v=2.0"></script>

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
	

		<%-- <s:form id="form">
		<s:hidden id="filterListData" name="filterListData"/>
		<div class="appContentWrapper marginBottom">
			<section class='reportWrap row'>
				<div class="gridly">
				
			
				
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
			
		</s:form> --%>
	
			
			<%-- GRID SECTION --%>

		<div class="appContentWrapper marginBottom">

				<div class="panel panel-default ">
					<div class="panel-heading">
						<span class="makeBold"><s:property
								value='getLocaleProperty("donutChart.farmerICSChart")' /></span>
						<div class="clear"></div>
					</div>
					<div class="panel-body">
						<div class="row">
							<s:hidden id="areaType" value="%{getAreaType()}" name="areaType" />
							<div class="col-md-6">
								<div style="overflow: auto">
									<div id="farmerByGroupChart_bar" class="chartOverflow"></div>
								</div>

							</div>

							<div class="col-md-6">
								<div style="overflow: auto">
									<div id="farmerByGroupChart_pie" class="chartOverflow"></div>
								</div>
							</div>

							<div class="col-md-6">
								<div style="overflow: auto">
									<div id="farmerByGroup_crop_3d" class="chartOverflow"></div>
								</div>
							</div>

						<!-- 	<div class="col-md-6">
								<div style="overflow: auto">
									<div id="farmerByGroup_estimated" class="chartOverflow"></div>
								</div>
							</div>
 -->
						</div>

						
					</div>
				</div>
			</div>
<script type="text/javascript">
	var farmerId='';
	var villageName='';
	var sDate='';
	var eDate='';
	var seasonCode='';
	var tenantId="<s:property value='tenantId'/>";
	var codeForCropChart='';
	var branchId='<s:property value="branchId"/>';
	var areaType=$("#areaType").val();
	//alert(branchId);
	jQuery(document).ready(function(){
		
		if(!isEmpty(branchId)){
			farmerByGroupChart_bar(branchId);
			
		}else{
			farmerByGroupChart_bar('');
		}
	});
	
	function farmerByGroupChart_bar(branch){	
		jQuery.post("icsSummaryReport_populateFarmersByLocation.action",{selectedBranch:branch},function(result) {
			json = $.parseJSON(result);
			if(!isEmpty(json)){
			$.each(json, function(index, value) {
				renderfarmerByGroupChart(value.branch,value.country,value.state,value.locality,value.municipality,value.gramPanchayat,value.village,value.farmerDetails,value.getGramPanchayatEnable);
			});
		   }
		});

	}	
	function renderfarmerByGroupChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable){
		
		var branchForPieChart;
		var iteration1 = 1;
		var iteration2 = 1;
		var dataLength = 0;
		var rightCount = 0;
		var rightClick = 0;
		var locationTitleArray;
		if(isEmpty(branchId)){
			locationTitleArray = "Farmer By Group";
		}
		else{
		locationTitleArray="Farmer By Group" ;
		}
		
		var unit = "Hectare";
		
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
		
		var chart = new	esecharts.chart('farmerByGroupChart_bar', {
		    chart: {
		        type: 'column',
		        marginLeft: 100,
		        marginRight: 100,
		             	
		            
		            	
		        
		    },
		    title: {
		    	
		    	
		        text:locationTitleArray
		        	
		    },
		   
		    xAxis: {
		        type: 'category',
		       
		    },
		    yAxis: {
		        title: {
		            text: "Farmers Count"
		        }

		    },
		    legend: {
		        enabled: false
		    },
		    plotOptions: {
		    	 
			        series: {
			           allowPointSelect: false,
			            dataLabels: {
			                enabled: true,
			                format: '{point.y}'
			            }
			        }
		    },

		    tooltip: {
		        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
		    },

		    series: [{
		        name: 'Location',
		        colorByPoint: true,
		        data : (function() {
							var res = [];
							if(isEmpty(branchId)){
								$.each(branch, function(index, branch) {
									
										res.push({
											name :branch.branchName,
											y : branch.count,
											drilldown:branch.branchId,
											id : branch.branchId
										 });
								
								});
								farmerByGroupChart_pie("B",branchForPieChart,unit,titleForFarmAreaChart);
							}else{
								$.each(country, function(index, country) {
								
										res.push({
												name :country.countryName,
												y : country.count,
												drilldown:country.countryCode,
												id : country.countryCode
											 });
											
								});
								farmerByGroupChart_pie('first_level_Branch_Login',branchId,unit,titleForFarmAreaChart);
							}
							
							return res;
						})(),
							point : {
									events : {
										click : function(event) {
									
											if(isEmpty(branchId)){
								
												populate_Group(this.id,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branchId);
											}else{
											    populate_GroupByICS(this.id,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branchId);
											}
										}
									}
								}
					 }],
			
			drilldown: {},
		
		  }, function(chart) {});
	}
	function farmerByGroupChart_pie(locationCode,branch,unit,titleForFarmAreaChart){
	//alert("LocationCode :"+locationCode);
		var iteration1 = 1;
		var iteration2 = 1;
		var dataLength = 0;

		var rightCount = 0;
		var rightClick = 0;
		jQuery.post("icsSummaryReport_getFarmDetailsAndProposedPlantingArea.action",{locationLevel:locationCode,selectedBranch:branch},function(farmDetailsAndAcre) {
			farmDetailsAndAcre_json = $.parseJSON(farmDetailsAndAcre);
			
		
		
		var chart = new esecharts.chart('farmerByGroupChart_pie', {
		    chart: {
		        type: 'bar',
		        marginLeft: 100,
		        marginRight: 100,
		        
		    },
		    title: {
		        text: titleForFarmAreaChart,
		    },
		    tooltip: {
		        pointFormat: '{series.name}: <b>{point.y} </b> '+areaType
		    },
		   
		    xAxis: {
		        type: 'category',
		       /* title: {
		            text: '<b>Branch</b>'
		        }*/
		    },
		    yAxis: {
		        title: {
		            text: '<b>'+areaType+'</b>'
		        }

		    },
		    legend: {
		        enabled: false
		    },
		    plotOptions: {
		    	 
		        series: {
		           allowPointSelect: false,
		            dataLabels: {
		                enabled: true,
		                format: '{point.y} '+areaType
		            }
		        }
	    },
		    series: [{
		        name: 'Area',
		        colorByPoint: true,
		        data : (function() {
							var res = [];
								
							$.each(farmDetailsAndAcre_json, function(index, value) {
								 
								if(iteration1 <= 5){
									
									
									res.push({
										name :value.locationName,
										y : Number(value.Area),
										//url :'procurementProductEnroll_list.action?q='+value.locationName
									//	id : value.locationCode
									 });
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
							
							});
							
							
							
							
							return res;
						})()
					 }]
		 }, function(chart) { 
			 var flag = "hideArrows";
			 
			  if(farmDetailsAndAcre_json.length > 5){
				  flag = "showArrows";
			  }
			  if(flag == "showArrows"){
			    //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerByGroupChart_pie').add();
			   // chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerByGroupChart_pie').add();
			    chart.xAxis[0].setExtremes(0,4);
			  
			    var drilldown = dataLength/5;
			    
			 
			    if($("#arrow_farmerByGroupChart_pie").length > 0){
			    	   $("#arrow_farmerByGroupChart_pie").remove();
			    	}

			    	
			    	var row = jQuery('<div/>', {
			    		class : "col-xs-12",
			    		id	  : "arrow_farmerByGroupChart_pie"
			    	});
			    					    
			    	$("#farmerByGroupChart_pie").append($(row));
			    	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerByGroupChart_pie "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
			    	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerByGroupChart_pie"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')
			    	 
			    
			    $('.left_farmerByGroupChart_pie').click(function() {
			    	if(rightClick > 0){
			    		if(rightClick == 1){
			    			rightCount = rightCount-6;
			    		}else{
			    			rightCount = rightCount-5;
			    		}
			    	
				    	temp1 = rightCount;
			    	
					
					
					 var yValues1 =  new Array();
					
					 iteration2 = 1;
					 $.each(farmDetailsAndAcre_json, function(index, value) {
						 if(iteration2 >= temp1 ){
							if(yValues1.length <= 4){
								yValues1.push({
									name :value.locationName,
									y : Number(value.Area),
									//url :'procurementProductEnroll_list.action?q='+value.locationName
								//	id : value.locationCode
								 });
							}
						}
							iteration2 = iteration2+1;
			    	});
					 
					 iteration2 = 1;
					
					chart.series[0].setData(eval(yValues1), false, true);
					
					chart.xAxis[0].setExtremes(0, 3); 
					 rightClick = rightClick - 1;
					 iteration2 = 1;
			    }
			    });
			    
			    $('.right_farmerByGroupChart_pie').click(function() {
			    	
			    	
			    	if(rightClick < drilldown-1){
					    if(rightCount == 0){
					    	rightCount = rightCount+6;
					    	rightCount = Number(rightCount);
					    }else{
					    	rightCount = rightCount+5;
					    }
					   
					    	 temp1 = rightCount;
					    	 if(dataLength >=  Number(rightCount+4)){
					    		 temp2 = Number(rightCount+4);
					    		 //temp2 = temp2+10;
					    	 }else{
					    		var temp2  = Number(dataLength-(rightCount-1));
					    	 }
					    	
					    	 if(temp2 < temp1){
								temp2 = dataLength ;
					    	 }
							
					    	 var yValues1 =  new Array();
					    	
					    	 iteration2 = 1;
					    	
					    	 $.each(farmDetailsAndAcre_json, function(index, value) {
					    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
					    			 yValues1.push({
											name :value.locationName,
											y : Number(value.Area),
											//url :'procurementProductEnroll_list.action?q='+value.locationName
										//	id : value.locationCode
										 });
									}
								 iteration2 = iteration2+1;	
					    	});
					    	  iteration2 = 1;
					    	 
					    	 chart.series[0].setData(eval(yValues1), false, true);
							 
							 if(yValues1.length < 5){
								 chart.xAxis[0].setExtremes(0, yValues1.length-1); 
							 }else{
								 chart.xAxis[0].setExtremes(0, 3); 
							 }
							 rightClick = rightClick+1;
						} 
			    
			    	
				})
		  }
			});
	 });
		
		var titleForFarmerGroupCropChart ;
		
		
		titleForFarmerGroupCropChart = titleForFarmAreaChart.replace("Farm Area By ","Farm Crop Area By");
		titleForFarmerGroupCropChart = titleForFarmAreaChart.replace("Farm Area","Farm Crop Area");
		populateFarmerGroupCropChart(titleForFarmerGroupCropChart);
	}
	function populate_Group(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
		
			var branchForPieChart;
			var iteration1 = 1;
			var iteration2 = 1;
			var dataLength = 0;
			var rightCount = 0;
			var rightClick = 0;
			//codeForCropChart=branchId;
			//alert(codeForCropChart);
			var locationTitleArray='Farmer By Group';
		var unit = "Hectare";
			
			var titleForFarmAreaChart = locationTitleArray;
			titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
			esecharts
			.chart(
					'farmerByGroupChart_bar',
					{
						chart : {
							type : 'column',
							// backgroundColor:'rgba(255, 255, 255, 0.0)',
							  marginLeft: 100,
						        marginRight: 100,
							/*options3d : {
								enabled : false,
								alpha : 10,
								beta : 20,
								depth : 70
							}*/
						},

						 title: {
						    	
						    	
						        text:locationTitleArray
						        	
						    },
						   
						    xAxis: {
						        type: 'category',
						       
						    },
						    yAxis: {
						        title: {
						            text: "Farmers Count"
						        }

						    },
						    legend: {
						        enabled: false
						    },
						    plotOptions: {
						    	 
							        series: {
							           allowPointSelect: false,
							            dataLabels: {
							                enabled: true,
							                format: '{point.y}'
							            }
							        }
						    },

						    tooltip: {
						        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
						       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
						    },

						series : [
								{
									name : 'Location',
									colorByPoint: true,
									colorByPoint: true,
									data : (function() {
										var res = [];
										var len = 0;
									
									
															$.each(country, function(index, country) {
																if(branchId == country.branchId){
																	res.push({
																		name :country.countryName,
																		y : country.count,
																		drilldown:country.countryCode,
																		id : country.countryCode
																	});
																}
															});
															//alert(id);
															farmerByGroupChart_pie('first_level_Branch_Login',branchId,unit,titleForFarmAreaChart);
										return res;
									})(),
									point : {
										events : {
											click : function(event) {
												populate_GroupByICS(this.id,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
												
											}
										}
									}
							
								}
								],

					});
			//customMobSowingBackButton(warehouse,mobileUser,branch);
		//customBackrenderfarmerByGroupChart(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable);

		}
		function customBackrenderfarmerByGroupChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable) {
			 var chart = $('#farmerByGroupChart_bar').esecharts();
			    normalState = new Object();
			    normalState.stroke_width = null;
			    normalState.stroke = null;
			    normalState.fill = null;
			    normalState.padding = null;
			    normalState.r = null;

			    hoverState = new Object();
			    hoverState = normalState;

			    pressedState = new Object();
			    pressedState = normalState;

			    var custombutton = chart.renderer.button('Back', 100, 20, function(){
			    	renderfarmerByGroupChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable);
			    },null,hoverState,pressedState).add();
		}

		function populate_GroupByICS(countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
	//alert("GroupCode:  "+countryCode);
			var branchForPieChart;
			var iteration1 = 1;
			var iteration2 = 1;
			var dataLength = 0;
			var rightCount = 0;
			var rightClick = 0;
			codeForCropChart=countryCode;
			var locationTitleArray='Farmer By ICS';
			var unit = "Hectare";
				var titleForFarmAreaChart = locationTitleArray;
				titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
			//alert(tenantId);

			esecharts
			.chart(
					'farmerByGroupChart_bar',
					{
						chart : {
							type : 'column',
							// backgroundColor:'rgba(255, 255, 255, 0.0)',
							  marginLeft: 100,
						        marginRight: 100,
							/*options3d : {
								enabled : false,
								alpha : 10,
								beta : 20,
								depth : 70
							}*/
						},

						 title: {
						    	
						    	
						        text:locationTitleArray
						        	
						    },
						   
						    xAxis: {
						        type: 'category',
						       
						    },
						    yAxis: {
						        title: {
						            text: "Farmers Count"
						        }

						    },
						    legend: {
						        enabled: false
						    },
						    plotOptions: {
						    	 
							        series: {
							           allowPointSelect: false,
							            dataLabels: {
							                enabled: true,
							                format: '{point.y}'
							            }
							        }
						    },

						    tooltip: {
						        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
						       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
						    },

						series : [
								{
									name : 'Location',
									colorByPoint: true,
									data : (function() {
										var res = [];
										var len = 0;
									
										$.each(state, function(index, state) {
											if(countryCode == state.samithiCode){
												res.push({
													name :state.icsType,
													y : state.count,
													drilldown:state.samithiName,
													id : state.samithiCode
												});
											}
										});
										farmerByGroupChart_pie(countryCode,branchId,unit,titleForFarmAreaChart);				
													/*	return res2;
												})()
											});*/
							    	//	});
										return res;
									})(),

									point : {
										events : {
											click : function(event) {
												
												   // populate_localityInLocation(this.id,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
												
											}
										}
									}
								}
								],

					});
					codeForCropChart="";
			//customMobSowingBackButton(warehouse,mobileUser,branch);
			customBackpopulate_Group(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)

		}

		function customBackpopulate_Group(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
			 var chart = $('#farmerByGroupChart_bar').esecharts();
			    normalState = new Object();
			    normalState.stroke_width = null;
			    normalState.stroke = null;
			    normalState.fill = null;
			    normalState.padding = null;
			    normalState.r = null;

			    hoverState = new Object();
			    hoverState = normalState;

			    pressedState = new Object();
			    pressedState = normalState;

			    var custombutton = chart.renderer.button('Back', 500, 20, function(){
			    	populate_Group(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
			    },null,hoverState,pressedState).add();
		}
		
		function populateFarmerGroupCropChart(titleForFarmerGroupCropChart){
			var dataForChart = new Array();
			var iteration1 = 1;
			var iteration2 = 1;
			var dataLength = 0;

			var rightCount = 0;
			var rightClick = 0;
			//alert(codeForCropChart);
		//	codeForCropChart = "";
			if(codeForCropChart == "first_level_Branch_Login"){
				codeForCropChart = "";
			}
			
			jQuery.post("icsSummaryReport_populateFarmerLocationCropChart.action",{codeForCropChart:codeForCropChart,selectedBranch:branchId},function(farmerByGroup_crop) {
				
			var	farmerByGroup_crop_Data = $.parseJSON(farmerByGroup_crop);
			$.each(farmerByGroup_crop_Data, function(index, value) {
					//if(iteration1 <= 3){
						dataForChart.push({	
							"name" : value.productName,
							"y" : Number(Number(value.Area).toFixed(3)),
							
							
						});
						//iteration1 = iteration1+1;
						dataLength = dataLength+1;
					/* }else{
						dataLength = dataLength+1;
					} */
			});
			
				esecharts.chart('farmerByGroup_crop_3d', {
					    chart: {
					        type: 'pie',
					        marginLeft: 100,
					        marginRight: 100,
					    },
					    title: {
					        text: titleForFarmerGroupCropChart
					    },
					  
					    xAxis: {
					        type: 'category',
					        title: {
						        text: 'Products'
						    },
					        labels: {
					            rotation: -45,
					            style: {
					                fontSize: '13px',
					                fontFamily: 'Verdana, sans-serif'
					            }
					        }
					    },
					    yAxis: {
					        min: 0,
					        title: {
					            text: 'Acre'
					        }
					    },
					    legend: {
					        enabled: true
					    },
					    tooltip: {
					        pointFormat: '<b>{point.y} '+areaType+'</b>'
					    },
					   
					    plotOptions: {
					       /*  pie: {
					        	innerSize: 70,
					            depth: 45
					        }, */
					        series: {
					            borderWidth: 0,
					            dataLabels: {
					                enabled: true,
					                format: '{point.name} : <span style="color: {point.color};">{point.y}</span> '+areaType
					            	
					            }
					        }
					    },
					    
					  
					    series: [{
					       
					        data:dataForChart,
					      
					        showInLegend: true
					    }],
					   
					 }, function(chart) {	});
			}); 
			
		
			
			
		}


</script>

<s:hidden name="startDate" id="hiddenStartDate"></s:hidden>
	<s:hidden name="endDate" id="hiddenEndDate"></s:hidden>

	
</body>
