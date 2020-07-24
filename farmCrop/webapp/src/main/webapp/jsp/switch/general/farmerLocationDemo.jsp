<%@ taglib uri="/struts-tags" prefix="s"%>
<head>
<META name="decorator" content="swithlayout">
</head>
<body>


	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/highcharts-3d.js"></script>
	<script src="//code.highcharts.com/maps/modules/map.js"></script>
	<script src="https://code.highcharts.com/maps/modules/data.js"></script>

	<script src="https://code.highcharts.com/maps/modules/drilldown.js"></script>
	<script src="https://code.highcharts.com/maps/modules/exporting.js"></script>
	<script
		src="https://code.highcharts.com/maps/modules/offline-exporting.js"></script>
	<script
		src="https://code.highcharts.com/mapdata/countries/in/in-all.js"></script>
	<link
		href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css"
		rel="stylesheet">
	<table style="width: 100%">
		<tr>
			<td valign="top">
				<div id="container"
					style="height: 500px; min-width: 510px; max-width: 800px; margin: 20 auto"></div>

			</td>
			<td>
				<table>
					<tr>
						<td>
							<div id="farmerDonutChart"></div>

						</td>
					</tr>
					<tr>
						<td>
							<div id="barcharts"
								style="height: 300px; min-width: 410px; max-width: 1000px; margin: 0 auto"></div>

						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<script>
	function populateFarmerDataByState(mapKey)
	{
		jQuery
		.post(
				"farmerLocationDemo_populateFarmerDetailsByStateChart.action",
				{
					selectedState : mapKey

				},
				function(result) {
					//var label = extractLabelValue(result, "label");
					//save = extractLabelValue(result, "save");
					//Refresh = extractLabelValue(result, "refresh");
					//result = findAndRemove(result, "id", "Label");
					if (result == "" || result == null
							|| result == "{}") {
						$("#barcharts")
								.html(
										"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Farmer Details</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
					} else {
						var farmerDatas = jQuery.parseJSON(result);
						loadBarcharts(farmerDatas.label,
								farmerDatas.farmerCount,
								farmerDatas.farmCount,
								farmerDatas.totalAcres);

					}
				});

		
	}
	
		function loadBarcharts(label,farmerCount,farmCount,totalAcres) {
			Highcharts.chart('barcharts', {
				 chart: {
				        type: 'column',
				        options3d: {
				            enabled: true,
				            alpha: 10,
				            beta: 25,
				            depth: 70
				        }
				    },
				    title: {
				        text: 'Farmer Datas'
				    },
				  /*   subtitle: {
				        text: 'Notice the difference between a 0 value and a null point'
				    }, */
				    plotOptions: {
				        column: {
				            depth: 25
				        }
				    },
				    xAxis: {
				        categories:['Farmer','Farms','Total Acres'],
				        labels: {
				            skew3d: true,
				            style: {
				                fontSize: '16px'
				            }
				        }
				    },
				    yAxis: {
				        title: {
				            text: null
				        }
				    },
				    series: [{
				        name: 'Farmer Details',
				        data: [farmerCount,farmCount,totalAcres]
				    }]
			});


		}

		function farmerDonutChart(label, farmerCount, farmCount, totalAcres) {
			Highcharts.chart('farmerDonutChart', {
				 chart: {
				        type: 'pie',
				        options3d: {
				            enabled: true,
				            alpha: 45
				        }
				    },
				    title: {
				        text: label
				    },
				    /* subtitle: {
				        text: '3D donut in Highcharts'
				    }, */
				    plotOptions: {
				        pie: {
				            innerSize: 100,
				            depth: 45
				        }
				    },
				    series: [{
				        name: 'Count',
				        data: [
				            ['Farmer', farmerCount],
				            ['Farm', farmCount],
				            ['Total Acres', totalAcres],
				           
				        ]
				    }]
			});

		}

		function loadCharts(mapKey) {
			
			jQuery
					.post(
							"farmerLocationDemo_populateFarmerDetailsByStateChart.action",
							{
								selectedState : mapKey

							},
							function(result) {
								//var label = extractLabelValue(result, "label");
								//save = extractLabelValue(result, "save");
								//Refresh = extractLabelValue(result, "refresh");
								//result = findAndRemove(result, "id", "Label");
								if (result == "" || result == null
										|| result == "{}") {
									$("#farmerDonutChart")
											.html(
													"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Farmer Details</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
								} else {
									var farmerDatas = jQuery.parseJSON(result);
									farmerDonutChart(farmerDatas.label,
											farmerDatas.farmerCount,
											farmerDatas.farmCount,
											farmerDatas.totalAcres);

								}
							});

		}

		var data = Highcharts.geojson(Highcharts.maps['countries/in/in-all']), separators = Highcharts
				.geojson(Highcharts.maps['countries/in/in-all'], 'mapline'),
		// Some responsiveness
		small = $('#container').width() < 400;

		//Set drilldown pointers
		$.each(data, function(i) {
			this.drilldown = this.properties['hc-key'];
			this.value = i; // Non-random bogus data
		});

		//Instantiate the map
		Highcharts
				.mapChart(
						'container',
						{
							chart : {
								events : {
									drilldown : function(e) {
										if (!e.seriesOptions) {
											var chart = this, mapKey = 'countries/in/'
													+ e.point.drilldown
													+ '-all',
											// Handle error, the timeout is cleared on success
											fail = setTimeout(
													function() {
														if (!Highcharts.maps[mapKey]) {
															chart
																	.showLoading('<i class="icon-frown"></i> Failed loading '
																			+ e.point.name);
															fail = setTimeout(
																	function() {
																		chart
																				.hideLoading();
																	}, 1000);
														}
													}, 3000);

											// Show the spinner
											// chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>'); // Font Awesome spinner

											var array = [];
											mapKey.split('/').forEach(
													function(value) {
														array.push(value
																.split('-'));
													});
											var result = array[2][1];
											loadCharts(result);
											populateFarmerDataByState(result);
											// Load the drilldown map
											/*   $.getScript('https://code.highcharts.com/mapdata/' + mapKey + '.js', function () {

											      data = Highcharts.geojson(Highcharts.maps[mapKey]);

											      // Set a non-random bogus value
											      $.each(data, function (i) {
											          this.value = i;
											      });

											      // Hide loading and add series
											      chart.hideLoading();
											      clearTimeout(fail);
											      chart.addSeriesAsDrilldown(e.point, {
											          name: e.point.name,
											          data: data,
											          dataLabels: {
											              enabled: true,
											              format: '{point.name}'
											          }
											      });
											  }); */
										}

										this.setTitle(null, {
											text : e.point.name
										});
									},
									drillup : function() {
										this.setTitle(null, {
											text : ''
										});
									}
								}
							},

							title : {
								text : 'Farmer Location Map'
							},

							subtitle : {
								text : '',
								floating : true,
								align : 'right',
								y : 50,
								style : {
									fontSize : '16px'
								}
							},

							legend : small ? {} : {
								layout : 'vertical',
								align : 'right',
								verticalAlign : 'middle'
							},

							colorAxis : {
								min : 0,
								minColor : '#E6E7E8',
								maxColor : '#005645'
							},

							mapNavigation : {
								enabled : true,
								buttonOptions : {
									verticalAlign : 'bottom'
								}
							},

							plotOptions : {
								map : {
									states : {
										hover : {
											color : '#EEDD66'
										}
									}
								}
							},

							series : [ {
								data : data,
								name : 'IND',
								dataLabels : {
									enabled : true,
									format : '{point.properties.postal-code}'
								}
							}, {
								type : 'mapline',
								data : separators,
								color : 'silver',
								enableMouseTracking : false,
								animation : {
									duration : 500
								}
							} ],

							drilldown : {
								activeDataLabelStyle : {
									color : '#FFFFFF',
									textDecoration : 'none',
									textOutline : '1px #000000'
								},
								drillUpButton : {
									relativeTo : 'spacingBox',
									position : {
										x : 0,
										y : 60
									}
								}
							}
						});

		function extractLabelValue(result, values) {

			json = $.parseJSON(result);
			var val = "";
			$.each(json, function(index, value) {
				$.each(value, function(key, value) {
					if (key == values) {
						val = value;
					}
				});
			});
			return val;
		}

		function findAndRemove(results, property, values) {
			var json = $.parseJSON(results);
			$.each(json, function(index, value) {
				$.each(value, function(key, value) {
					if (key == values) {
						delete json[0][key];
					}
				});
			});
			results = JSON.stringify(json);
			return results;
		}
	</script>



</body>