var jsonData = "";
var jsonFarmerData = "";
var save = "";
var Refresh = "";
var currencyType =$("#currencyType").val();
var areaType=$("#areaType").val();
var drillup_districtCode;
var drillup_id;	
var codeForCropChart;
var drillUp_count = 0;
var locationCode_ForDrillUp = new Array();
var dataStyle = {
	normal : {
		label : {
			show : true,
			position : 'top',
			formatter : '{c}',
			textStyle : {
				color : '#333',
				fontFamily : 'roboto',
				fontSize : 14,
			}
		}
	}
};

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

function loadWarehouseStockChart(val) {
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateWarehouseStockChartData.action",
					{
						selectedWarehouse : val
					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");

						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartStock")
									.html(
											"<p id='donutStock' style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'><s:text name='noRecord'/></h4>");
						} else {
							jsonData = jQuery.parseJSON(result);

							barChartsStock(label);

						}
					});
}

function loadFarmerGroupPieChart() {
	jQuery
			.post(
					"dashboard_populateFarmerGroupChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartFarmer")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							jsonFarmerData = jQuery.parseJSON(result);
							DoughnutChartFarmer(label);
						}
					});

}

function loadCowVillagePieChart() {
	jQuery
			.post(
					"dashboard_populateCowCountByVillageBarChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartCowVillage")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							/*
							 * jsonCowData = jQuery.parseJSON(result);
							 * doughnutChartCowVillage(label);
							 */
							var jsonCowVgeData = jQuery.parseJSON(result);
							doughnutChartCowVillage(label, jsonCowVgeData);
						}
					});

}

function loadCowRSPieChart() {
	jQuery
			.post(
					"dashboard_populateCowCountByRSBarChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartCowRS")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							/*
							 * jsonCowData = jQuery.parseJSON(result);
							 * doughnutChartCowVillage(label);
							 */
							var jsonCowRSData = jQuery.parseJSON(result);
							doughnutChartCowRS(label, jsonCowRSData);
						}
					});

}

function loadCowDiseasePieChart() {
	jQuery
			.post(
					"dashboard_populateCowCountByDiseaseBarChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartCowDisease")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							/*
							 * jsonCowData = jQuery.parseJSON(result);
							 * doughnutChartCowVillage(label);
							 */
							var jsonCowDiseaseData = jQuery.parseJSON(result);
							doughnutChartCowDisease(label, jsonCowDiseaseData);
						}
					});

}

function loadDeviceChart() {
	jQuery
			.post(
					"dashboard_populateDeviceChartData.action",
					{},
					function(result) {

						if (result == "" || result == null || result == "[]") {
							$("#doughnutChartDevice")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Devices</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonDeviceData = jQuery.parseJSON(result);
							deviceChart(jsonDeviceData.onlineDeviceCount,
									jsonDeviceData.offlineDeviceCount,
									jsonDeviceData.inActiveDeviceCount,
									jsonDeviceData.disabledDeviceCount,
									jsonDeviceData.Label,
									jsonDeviceData.inactiveLabel,
									jsonDeviceData.activeLabel,
									jsonDeviceData.disabledLabel,
									jsonDeviceData.onlineLabel,
									jsonDeviceData.offlineLabel);
						}

					});
}

function loadCowMilkChart() {
	jQuery
			.post(
					"dashboard_populateCowMilkChartData.action",
					{},
					function(result) {
						if (result == "" || result == null || result == "[]") {
							$("#doughnutChartCowMilk")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Devices</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonCowMilkData = jQuery.parseJSON(result);

							cowMilkChart(jsonCowMilkData.totalMilkCount,
									jsonCowMilkData.totalNonMilkCount,
									jsonCowMilkData.Label,
									jsonCowMilkData.isMilkingCow,
									jsonCowMilkData.isNonMilkingCow);
						}

					});
}

function loadFarmerCountPieChart() {
	jQuery
			.post(
					"dashboard_populateFarmerCountChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutFarmerCountByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							DoughnutFarmerCountByBranch(jsonFarmerData, label);
						}
					});
}

function loadTotalAcreChart() {
	jQuery
			.post(
					"dashboard_populateTotalAcreChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						var labelProd = extractLabelValue(result,
								"LabelProduction");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {

							$("#totalAcreByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							DoughnutTotalAcre(jsonAcreData, label);
							DoughnutTotalAcreProduction(jsonAcreData, labelProd);
						}
					});

}

function loadTotalAcreByVillageChart() {
	jQuery
			.post(
					"dashboard_populateTotalAcreByVillageChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#totalAcreByVillage")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							DoughnutTotalAcreByVillage(jsonAcreData, label);

						}
					});

}

function loadSeedTypeChart() {
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateSeedChartData.action",
					{

					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#totalAcreByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							if (tenantId != "chetna") {
								DoughnutseedType(jsonAcreData, label);
							} else {
								barSeedType(jsonAcreData, label);
							}
							// DoughnutseedType(jsonAcreData, label);
						}
					});
}

function loadSeedSourceChart() {
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateSeedSourceChartData.action",
					{},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#totalAcreByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							/*
							 * if(tenantId!="chetna"){
							 * DoughnutSeedSource(jsonAcreData, label); } else{
							 */
							barChartSeedSource(jsonAcreData, label);
						}
					});
}

function loadGroupTxnChart(val) {
	$("#groupFilter").val($("#groupFilter option:first").val());
	var dateRange;
	if (!isEmpty(jQuery("#reportrange").val())) {
		var rangeSplit = jQuery("#reportrange").val().split("-");
		dateRange = dateFormatTransform(rangeSplit[0]) + "-"
				+ dateFormatTransform(rangeSplit[1]);
	}
	jQuery.post("dashboard_populateTxnChartData.action", {
		selectedYear : val,
		dateRange : dateRange
	}, function(result) {
		var jsonTxnData = jQuery.parseJSON(result);
		var saleData = "";
		var harvestData = "";
		var distributionData = "";
		var labelData = "";
		var label = "";
		var disLabel = "";
		var harvestLabel = "";
		var saleLabel = "";
		var procurementLabel = "";
		var procurementData = "";
		var enrollmentLabel = "";
		var enrollmentData = "";
		var procurementQtyData = "";
		var procurementQtyLabel = "";
		var unit = "";
		$.each(jsonTxnData, function(index, value) {

			if (index == 0) {
				saleData = value;
			} else if (index == 1) {
				harvestData = value;
			} else if (index == 2) {
				distributionData = value;
			} else if (index == 3) {
				procurementData = value;
			} else if (index == 4) {
				enrollmentData = value;
			}

			else if (index == 5) {
				labelData = value;
				var json = JSON.stringify(labelData);
				var jsonArr = $.parseJSON(json);
				$.each(jsonArr, function(index, value) {
					label = value.Label;
					disLabel = value.disLabel;
					harvestLabel = value.harvestLabel;
					saleLabel = value.saleLabel;
					procurementLabel = value.procurementLabel;
					enrollmentLabel = value.enrollmentLabel;
					unit = value.unit;
				});
			}
		});

		txnCharts(saleData, harvestData, distributionData, label, disLabel,
				harvestLabel, saleLabel, procurementLabel, enrollmentLabel,
				procurementData, enrollmentData, unit);
	});
}

function loadGroupNswitchTxnChart(val) {
	$("#groupFilter").val($("#groupFilter option:first").val());
	var dateRange;
	if (!isEmpty(jQuery("#reportrange").val())) {
		var rangeSplit = jQuery("#reportrange").val().split("-");
		dateRange = dateFormatTransform(rangeSplit[0]) + "-"
				+ dateFormatTransform(rangeSplit[1]);
	}

	jQuery.post("dashboard_populateNswitchTxnChartData.action", {
		selectedYear : val,
		dateRange : dateRange
	}, function(result) {
		var jsonTxnData = jQuery.parseJSON(result);
		var enrollmentLabel = "";
		var enrollmentData = "";

		$.each(jsonTxnData, function(index, value) {
			if (index == 0) {
				enrollmentData = value;
			} else if (index == 1) {
				labelData = value;
				var json = JSON.stringify(labelData);
				var jsonArr = $.parseJSON(json);
				$.each(jsonArr, function(index, value) {
					label = value.Label;
					enrollmentLabel = value.enrollmentLabel;
				});
			}
		});
		txnNswitchCharts(enrollmentLabel, enrollmentData);
	});
}

function formFilterText(idsArr) {
	var filterTxt = "";
	$(idsArr).each(function(k, v) {
		var val = $("#" + v).text();
		if (!isEmpty(val)) {
			filterTxt += $("#" + v).find('option').eq(0).text() + ": " + val;
		}
	})
	return filterTxt;
}

function farmerGroupByBranch(val) {
	
	var org;

	var year = $("#farmerGroupFilterlTimelineYearFilter").val();

	var idsArr = [ 'selectedStateFilter', 'selectedGenderFilter',
			'selectedFarmerGroupCropFilter', 'selectedCooperativeGroupFilter',
			'groupFilter', 'villageFilter' ];

	//var filterFormatedTxt = formFilterText(idsArr);

	jQuery
			.post(
					"dashboard_populateFarmerCountByGroupBarChartData.action",
					{
						selectedState : $("#selectedStateFilter").val(),
						selectedGender : $("#selectedGenderFilter").val(),
						selectedCrop : $("#selectedFarmerGroupCropFilter")
								.val(),
						selectedCooperative : $(
								"#selectedCooperativeGroupFilter").val(),
						selectedBranch : $("#groupFilter").val(),
						selectedYear : year,
						selectedVillage : $("#villageFilter").val(),
					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartStock")
							.html(
									"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
											+ label
											+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
						
							var jsonFarmerData = jQuery.parseJSON(result);
							farmerByGroupBarChart(jsonFarmerData, label);
						}
					});
}

function farmerGroupTraderByBranch(val) {
	var org;

	var year = $("#farmerGroupFilterlTimelineYearFilter").val();

	var idsArr = [ 'selectedStateFilter', 'selectedGenderFilter',
			'selectedFarmerGroupCropFilter', 'selectedCooperativeGroupFilter',
			'groupFilter', 'villageFilter' ];

	//var filterFormatedTxt = formFilterText(idsArr);

	jQuery
			.post(
					"dashboard_populateFarmerCountByGroupTraderBarChartData.action",
					{
						selectedState : $("#selectedTraderStateFilter").val(),
						selectedGender : $("#selectedGenderFilter").val(),
						selectedCrop : $("#selectedFarmerGroupCropFilter")
								.val(),
						selectedCooperative : $(
								"#selectedCooperativeGroupFilter").val(),
						selectedBranch : $("#groupTraderFilter").val(),
						selectedYear : year,
						selectedVillage : $("#selectedTradervillageFilter").val(),
					},
					function(result) {
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutChartStock")
									.html(
											"<p id='donutStock' style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'><s:text name='noRecord'/></h4>");
						} else {
							var label = extractLabelValue(result, "Label");
							save = extractLabelValue(result, "save");
							Refresh = extractLabelValue(result, "refresh");
							result = findAndRemove(result, "id", "Label");
							var jsonFarmerData = jQuery.parseJSON(result);
							farmerByGroupTraderBarChart(jsonFarmerData, label);
						}
					});
}

function farmerInspectionGroupByBranch(){
	var year = $("#farmerGroupFilterlTimelineYearFilter").val();
	var dateRange;
	if (!isEmpty(jQuery("#reportfarmerInspectionrange").val())) {
		var rangeSplit = jQuery("#reportfarmerInspectionrange").val().split("-");
		dateRange = dateFormatTransform(rangeSplit[0]) + "-"
				+ dateFormatTransform(rangeSplit[1]);
	}
	jQuery
	.post(
			"dashboard_populateFarmerCountByFarmInspectionBarChartData.action",
			{
				selectedState : $("#selectedStateFilter").val(),
				selectedGender : $("#selectedGenderFilter").val(),
				selectedCrop : $("#selectedFarmerGroupCropFilter")
						.val(),
				selectedCooperative : $(
						"#selectedCooperativeGroupFilter").val(),
				selectedBranch : $("#groupFilter").val(),
				selectedYear : year,
				selectedVillage : $("#villageFilter").val(),
				dateRange : dateRange
			},
			function(result) {
				if (result == "" || result == null || result == "[{}]") {
					$("#doughnutChartStock")
							.html(
									"<p id='donutStock' style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
											+ label
											+ "</b></p><h4 style='margin-top:50%;text-align:center'><s:text name='noRecord'/></h4>");
				} else {
					var label = extractLabelValue(result, "Label");
					save = extractLabelValue(result, "save");
					Refresh = extractLabelValue(result, "refresh");
					result = findAndRemove(result, "id", "Label");
					var jsonFarmerData = jQuery.parseJSON(result);
					farmerInspectionByGroupBarChart(jsonFarmerData, label);
				}
			});
}



function farmerDetailBarChart(val) { 
	var org;
	var year = $("#farmerDetailTimelineYearFilter").val();
    var selectedFinYear= $("#finalYearFilter").val();
	jQuery
			.post(
					"dashboard_populateFarmerDetailsBarChartData.action",
					{
						selectedCrop : $("#selectedCropFilter").val(),
						selectedCooperative : $("#selectedCooperativeFilter")
								.val(),
						selectedBranch : $("#farmerDetailFilter").val(),
						selectedYear : year,
						selectedGender : $("#farmerSelectedGenderFilter").val(),
						selectedFinYear:selectedFinYear,
						selectedStatus:$("#selectedstatusFilter").val()
					},
					function(result) {
						var label = extractLabelValue(result, "label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutFarmerCountByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							console.log("BBBB "+jsonFarmerData);
							farmerDetailsBarChart(jsonFarmerData, label);
						}
					});
}

function loadCowCostChart() {
	jQuery
			.post(
					"dashboard_populateCowCostChartData.action",

					function(result) {

						if (result == "" || result == null || result == "[]") {
							$("#doughnutChartCowCost")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Devices</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonCowCostData = jQuery.parseJSON(result);
							doughnutChartCowCost(jsonCowCostData.label,
									jsonCowCostData.housingCostLabel,
									jsonCowCostData.feedCostLabel,
									jsonCowCostData.treatmentCostLabel,
									jsonCowCostData.otherLabel,
									jsonCowCostData.totalHousingCost,
									jsonCowCostData.totalFeedCost,
									jsonCowCostData.totalTreatmentCost,
									jsonCowCostData.totalOtherCost);
						}
					});
}

function farmerCropBarChart(val) {
	var org;
	/*
	 * if(getCurrentBranch()==""){ org=$("#cropFilter").val(); } else{
	 * org=getCurrentBranch(); }
	 */
	var year = $("#farmerCropFilterTimelineYearFilter").val();
	var selectedBranch = $("#cropFilter").val();
	if (!isEmpty(getCurrentBranch())) {
		selectedBranch = getCurrentBranch();
	}

	jQuery
			.post(
					"dashboard_populateCropsDetailChartData.action",
					{

						selectedGender : $("#farmerCropSelectedGenderFilter")
								.val(),
						selectedCrop : $("#selectedFarmerCropFilter").val(),
						selectedCooperative : $(
								"#selectedCropCooperativeFilter").val(),
						selectedBranch : selectedBranch,
						selectedState : $("#farmerCropStateFilter").val(),
						selectedYear : year
					},
					function(result) {
						var label = extractLabelValue(result, "label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutFarmerCountByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							farmerCropsBarChart(jsonFarmerData, label);
						}

					});

}

function cowMilkChart(milkingCount, nonMilking, label, milkLabel, nonMilkLabel) {
	var doughnutChartCowMilk = echarts.init(document
			.getElementById('doughnutChartCowMilk'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'

			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 10,
			y : 260,
			data : [ milkLabel, nonMilkLabel ],
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
				top : 100
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				// verticalAlign: 'top',
				x : -30,
				y : 30,
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : '',
			feature : {
				magicType : {
					show : true,
					title : '',
					type : [ 'pie', 'funnel' ],
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : [ {
				value : milkingCount,
				name : milkLabel
			}, {
				value : nonMilking,
				name : nonMilkLabel
			} ]
		} ]
	};
	doughnutChartCowMilk.setOption(option);
}

function deviceChart(online, offline, inActive, disabled, label, inactiveLabel,
		activeLabel, disabledLabel, onlineLabel, offlineLabel) {
	var DoughnutChartDevice = echarts.init(document
			.getElementById('doughnutChartDevice'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : [ onlineLabel, offlineLabel, inactiveLabel, disabledLabel ],
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : '',
			feature : {
				magicType : {
					show : true,
					title : '',
					type : [ 'pie', 'funnel' ],
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : [ {
				value : online,
				name : onlineLabel
			}, {
				value : offline,
				name : offlineLabel
			}, {
				value : disabled,
				name : disabledLabel
			}, {
				value : inActive,
				name : inactiveLabel
			} ]
		} ]
	};
	DoughnutChartDevice.setOption(option);
}

function DoughnutChartFarmer(label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('doughnutChartFarmer'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function doughnutChartCowRS(label, jsonCowRSData) {
	var doughnutChartCowRS = echarts.init(document
			.getElementById('doughnutChartCowRS'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 16,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowRSData, function(index, value) {
					if (value.researchStation !== undefined) {
						res.push({
							name : value.researchStation,
						});
					}
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '14',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowRSData, function(index, value) {
					if (value.researchStation !== undefined)
						res.push({
							name : value.researchStation,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	doughnutChartCowRS.setOption(option);

}

function doughnutChartCowDisease(label, jsonCowDiseaseData) {
	var doughnutChartCowDisease = echarts.init(document
			.getElementById('doughnutChartCowDisease'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 16,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowDiseaseData, function(index, value) {
					if (value.diseaseName !== undefined) {
						res.push({
							name : value.diseaseName,
						});
					}
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '14',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowDiseaseData, function(index, value) {
					if (value.diseaseName !== undefined)
						res.push({
							name : value.diseaseName,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	doughnutChartCowDisease.setOption(option);

}

function doughnutChartCowVillage(label, jsonCowVgeData) {

	var doughnutChartCowVillage = echarts.init(document
			.getElementById('doughnutChartCowVillage'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowVgeData, function(index, value) {
					if (value.village !== undefined)
						res.push({
							name : value.village,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonCowVgeData, function(index, value) {
					if (value.village !== undefined)
						res.push({
							name : value.village,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	doughnutChartCowVillage.setOption(option);
}

function DoughnutFarmerCountByBranch(jsonFarmerData, label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('doughnutFarmerCountByBranch'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function DoughnutTotalAcreByVillage(jsonFarmerData, label) {
	var DoughnutTotalAcreByVillage = echarts.init(document
			.getElementById('totalAcreByVillage'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 5,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutTotalAcreByVillage.setOption(option);
}

function DoughnutTotalAcre(jsonFarmerData, label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('totalAcreByBranch'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 5,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.landholding
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function DoughnutTotalAcreProduction(jsonFarmerData, label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('totalProdAcreByBranch'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		/* color:["#5DA5DA","#FAA43A","#60BD68","#F17CB0","#B2912F","#B276B2","#DECF3F","#F15854"], */
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.proposed
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function DoughnutseedType(jsonFarmerData, label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('doughnutSeedType'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function DoughnutSeedSource(jsonFarmerData, label) {
	var DoughnutChartFarmer = echarts.init(document
			.getElementById('doughnutSeedSource'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartFarmer.setOption(option);
}

function populateWarehouseStock(value) {
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateWarehouseStockChartData.action",
					{
						selectedWarehouse : value
					},
					function(result) {
						if (result == "" || result == null || result == "[]") {
							$("#doughnutChartStock")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>Warehouse Stock</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var label = extractLabelValue(result, "Label");
							jsonData = jQuery.parseJSON(result);
							if (tenantId != "chetna") {
								doughnutChartsStock(label);
							} else {
								barChartsStock(label);
							}
						}
					});
}

function populateSeedType(value) {

	var selectedSeedType = jQuery("#selectedSeedType").val();
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateSeedChartData.action",
					{
						selectedGender : $("#selectedSeedTypeGenderFilter")
								.val(),
						selectedCrop : $("#selectedSeedTypeCropFilter").val(),
						selectedCooperative : $(
								"#selectedSeedTypeCooperativeFilter").val(),
						selectedSeedType : selectedSeedType

					},

					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#totalAcreByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							if (tenantId != "chetna") {
								DoughnutseedType(jsonAcreData, label);
							} else {
								barSeedType(jsonAcreData, label);
							}
						}
					});
}

function populateSeedSource(value) {
	var selectedSeedSource = jQuery("#selectedSeedSource").val();
	var tenantId = getCurrentTenantId();
	jQuery
			.post(
					"dashboard_populateSeedSourceChartData.action",
					{
						selectedGender : $("#selectedSeedSourceGenderFilter")
								.val(),
						selectedCrop : $("#selectedSeedSourceCropFilter").val(),
						selectedCooperative : $(
								"#selectedSeedSourceCooperativeFilter").val(),
						selectedVariety : $("#varietyFilter").val(),
						selectedState : $("#seedSrStateFilter").val()
					// selectedSeedSource : selectedSeedSource
					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#totalAcreByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonAcreData = jQuery.parseJSON(result);
							/*
							 * if(tenantId!="chetna"){
							 * DoughnutSeedSource(jsonAcreData, label); } else{
							 */
							barChartSeedSource(jsonAcreData, label);
						}
					});
}

function doughnutChartsStock(label) {
	var nameArr = "";
	var stockArr = "";
	var loopCounter = 0;

	var DoughnutChartStock = echarts.init(document
			.getElementById('doughnutChartStock'));
	var option = {
		renderAsImage : false,
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			itemGap : 15,
			data : (function() {
				var res = [];
				var len = 0;

				$.each(jsonData, function(index, value) {
					if (value.name !== undefined)
						res.push({
							name : value.name,
						});
				});
				return res;
			})(),
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
			}
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		toolbox : {
			show : true,
			title : save,
			feature : {
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					title : '',
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548,
							title : 'Hello'
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
	
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;

				$.each(jsonData, function(index, value) {
					if (value.name !== undefined)
						res.push({
							name : value.name,
							value : value.stock
						});
				});
				return res;
			})()
		} ]
	};
	DoughnutChartStock.setOption(option);
}

function barChart() {

	var BarChart = echarts.init(document.getElementById('barChart'));
	var option = {
		title : {
			text : 'Sales Data',
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			show : true
		},
		legend : {
			data : [ 'Sales' ],
			x : "left",
			y : "bottom"
		},
		xAxis : [ {
			type : 'category',
			data : [ "Shirts", "Sweaters", "Chiffon Shirts", "Pants",
					"High Heels", "Socks" ]
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			"name" : "Sales",
			"type" : "bar",
			"data" : [ 5, 20, 40, 10, 10, 20 ]
		} ],
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'pie', 'funnel' ],
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548
						}
					}
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
	};
	BarChart.setOption(option);

}

/* %%%%%%%%%%%%%%%%%%%%%%######$$$$$$$$$$$$$$$$#####%%%%%%%%%%%%%%%%%%%%%% */

/* %%%%%%%%%%%%%%%%%%%%%%######$$$$$$$$$$$$$$$$#####%%%%%%%%%%%%%%%%%%%%%% */

function txnCharts(saleData, harvestData, distributionData, label, disLabel,
		harvestLabel, saleLabel, procurementAmtLabel, enrollmentLabel,
		procurementAmtData, enrollmentData, unit) {

	var distributionArry = new Array();
	var harvestArry = new Array();
	var procurementArry = new Array();
	var enrollmentDataArray = new Array();
	var saleDataArray = new Array();
	$.each(distributionData, function(index, value) {
		if (value.Qty !== undefined) {
			distributionArry.push(value.Qty);
		} else {
			distributionArry.push(0);
		}

	});

	$.each(harvestData, function(index, value) {
		if (value.Qty !== undefined) {
			harvestArry.push(value.Qty);
		} else {
			harvestArry.push(0);
		}
	});

	$.each(procurementAmtData, function(index, value) {
		if (value.amt !== undefined) {
			procurementArry.push(value.amt);
		} else {
			procurementArry.push(0);
		}
	});

	$.each(enrollmentData, function(index, value) {
		if (value.nos !== undefined) {
			enrollmentDataArray.push(value.nos);
		} else {
			enrollmentDataArray.push(0);
		}

	});

	$.each(saleData, function(index, value) {
		if (value.Qty !== undefined) {
			saleDataArray.push(value.Qty);
		} else {
			saleDataArray.push(0);
		}
	});

	esecharts.chart('groupChart', {
		chart : {
			type : 'column'
		},
		title : {
			text : ''
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul',
					'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ],
			crosshair : true
		},
		yAxis : [ {
			type : 'value'
		} ],
		tooltip : {
			/*
			 * headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
			 * pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}:
			 * </td>' + '<td style="padding:0"><b>{point.y:.1f} Kg</b></td></tr>',
			 * footerFormat : '</table>',
			 */
			shared : true,
			useHTML : true
		},
		plotOptions : {
			column : {
				pointPadding : 0.2,
				borderWidth : 0
			}
		},
		series : [ {

			name : disLabel,
			data : distributionArry,
			tooltip : {
				valueSuffix : ' Kg'

			}

		}, {
			name : harvestLabel,
			data : harvestArry,
			tooltip : {
				valueSuffix : ' Kg'

			}

		}, {
			name : saleLabel,
			data : saleDataArray,
			tooltip : {
				valueSuffix : ' Kg'

			}

		}, /*
			 * { name : procurementAmtLabel, data : procurementArry },
			 */{
			name : enrollmentLabel,
			data : enrollmentDataArray,

		} ]
	});
}

function txnNswitchCharts(enrollmentLabel, enrollmentData) {

	var distributionArry = new Array();
	var harvestArry = new Array();
	var procurementArry = new Array();
	var enrollmentDataArray = new Array();
	var saleDataArray = new Array();

	$.each(enrollmentData, function(index, value) {
		if (value.nos !== undefined) {
			enrollmentDataArray.push(value.nos);
		} else {
			enrollmentDataArray.push(0);
		}

	});

	esecharts
			.chart(
					'groupChartNswitch',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						subtitle : {
							text : ''
						},
						xAxis : {
							categories : [ 'Jan', 'Feb', 'Mar', 'Apr', 'May',
									'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov',
									'Dec' ],
							crosshair : true
						},
						yAxis : [ {
							type : 'value'
						} ],
						tooltip : {
							headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
							pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
									+ '<td style="padding:0"><b>{point.y:.1f} {series.units}</b></td></tr>',
							footerFormat : '</table>',
							shared : true,
							useHTML : true
						},
						plotOptions : {
							column : {
								pointPadding : 0.2,
								borderWidth : 0
							}
						},
						series : [ {
							name : enrollmentLabel,
							data : enrollmentDataArray

						} ]
					});
}

function farmerByGroupBarChart(jsonFarmerData, label) {
	 
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	$('#farmerGroupBarChart').esecharts({
			    chart: {
			     // backgroundColor: '#3f3b53',
			      type: 'column',
			      marginLeft: 100,
			      marginRight: 100
			    },
			    legend: {
			      symbolHeight: 8,
			      symbolWidth: 8,
			      symbolRadius: 4,
			      margin: 15,
			      backgroundColor: '#FFFFFF',
			      layout: 'horizontal',
			      itemDistance: 25,
			      symbolMargin: 10,
			      itemStyle: {
			        color: 'black',
			        fontWeight: 'normal'
			      }
			    },
			    title: {
			      text: ''
			    },
			    xAxis : {
					type : 'category'
				},
				yAxis: {
			        title: {
			            text: '<b>Farmers Count</b>'
			        }
				},
			    tooltip: {
			    	enabled: false
			    	/*backgroundColor: '#FFFFFF',
			      borderColor: '#FFFFFF',
			      borderRadius: 0,
			      borderWidth: 5,
			      formatter: function() {
			        return ' <b>' + this.y + '</b><br><b>' + this.series.name + '</b>';
			      }*/
			    },
			    /*plotOptions: {
			      column: {
			        pointPadding: 0.2,
			        borderWidth: 0
			      }
			    },*/
			    plotOptions : {
					series : {
						borderWidth : 1,
						dataLabels : {
							enabled : true
						
						}
					}
				},
			    series: [ {
										
			    						name : "Group",
										data : (function() {
											var res = [];
											
											
											$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													res.push({	
														value : value.group,
														name : value.group,
														y : value.count,
														
													});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
												}else{
													dataLength = dataLength+1;
												}
											});
											
											/*$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													if (value.group !== undefined) {
														res.push({
															value : value.group,
															name : value.group,
															y : value.count,
														});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
													}
												}else{
													dataLength = dataLength+1;
												}
											});*/
											return res;
										})()
									} ]
			  }, function(chart) {
				  
					 var flag = "hideArrows";
						
					 
					  if(jsonFarmerData.length > 10){
						  flag = "showArrows";
					  } 
				 
				 if(flag == "showArrows"){
				   //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerGroupBarChart').add();
				   //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerGroupBarChart').add();
				   chart.xAxis[0].setExtremes(0,9);
				 
				   var drilldown = dataLength/10;
				   

				   if($("#arrow_farmerGroupBarChart").length > 0){
					   $("#arrow_farmerGroupBarChart").remove();
					}


					var row = jQuery('<div/>', {
						class : "col-xs-12",
						id	  : "arrow_farmerGroupBarChart"
					});
									    
					$("#farmerGroupBarChart").append($(row));
					$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerGroupBarChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
					$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerGroupBarChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

				   
				   $('.left_farmerGroupBarChart').click(function() {
				   	if(rightClick > 0){
				   	rightCount = rightCount-10;
				   	temp1 = rightCount;
						 temp2 = rightCount+9;
						 temp2 = Number(temp2);
						
						
						 var yValues =  new Array();
						 
						 
						 
						 
						 $.each(jsonFarmerData, function(index, value) {
							if(iteration2 >= temp1 ){
								yValues.push({
									value : value.group,
									name : value.group,
									y : value.count,
									 });
								 }
								iteration2 = iteration2+1;
						 	});
						 
						
						 
						chart.series[0].setData(eval(yValues), false, true);
						
						 if(yValues.length < 9){
							 chart.xAxis[0].setExtremes(0, yValues.length-1); 
						 }else{
							 chart.xAxis[0].setExtremes(0, 9); 
						 }
						 rightClick = rightClick - 1;
						 iteration2 = 1;
				   }
				   });
				   
				   $('.right_farmerGroupBarChart').click(function() {

						   if(rightClick < drilldown-1){
						    if(rightCount == 0){
						    	rightCount = rightCount+1;
						    	rightCount = ""+rightCount+rightCount;
						    	rightCount = Number(rightCount);
						    }else{
						    	rightCount = rightCount+10;
						    }
						   
						    	 temp1 = rightCount;
						    	 if(dataLength >=  Number(rightCount+9)){
						    		 temp2 = Number(rightCount+9);
						    		// temp2 = temp2+10;
						    	 }else{
						    		
						    		 var temp2  = Number(dataLength-(rightCount-1));
						    	 }
						    	 if(temp2 < temp1){
									temp2 = dataLength ;
						    	 }
								
						    	 var yValues =  new Array();
								
						    	  
						    	
										$.each(jsonFarmerData, function(index, value) {
											 if(iteration2 >= temp1 && iteration2 <= (temp2)){
													yValues.push({
														value : value.group,
														name : value.group,
														y : value.count,
														 });
													}
												iteration2 = iteration2+1;
										});
										
									
										
										
									
								 chart.series[0].setData(eval(yValues), false, true);
								
								 if(yValues.length <= 9){
									 chart.xAxis[0].setExtremes(0, (yValues.length)-1); 
								 }else{
									 chart.xAxis[0].setExtremes(0, 9); 
								 }
								
								 rightClick = rightClick+1;
								 iteration2 = 1;
							} 
					})
				}

			  });
}


function farmerInspectionByGroupBarChart(jsonFarmerData, label) {

	 
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	$('#farmerInspectionGroupBarChart').esecharts({
			    chart: {
			     // backgroundColor: '#3f3b53',
			      type: 'column',
			      marginLeft: 100,
			      marginRight: 100
			    },
			    legend: {
			      symbolHeight: 8,
			      symbolWidth: 8,
			      symbolRadius: 4,
			      margin: 15,
			      backgroundColor: '#FFFFFF',
			      layout: 'horizontal',
			      itemDistance: 25,
			      symbolMargin: 10,
			      itemStyle: {
			        color: 'black',
			        fontWeight: 'normal'
			      }
			    },
			    title: {
			      text: ''
			    },
			    xAxis : {
					type : 'category'
				},
				yAxis: {
			        title: {
			            text: '<b>Farmers Count</b>'
			        }
				},
			    tooltip: {
			    	enabled: false
			    	/*backgroundColor: '#FFFFFF',
			      borderColor: '#FFFFFF',
			      borderRadius: 0,
			      borderWidth: 5,
			      formatter: function() {
			        return ' <b>' + this.y + '</b><br><b>' + this.series.name + '</b>';
			      }*/
			    },
			    /*plotOptions: {
			      column: {
			        pointPadding: 0.2,
			        borderWidth: 0
			      }
			    },*/
			    plotOptions : {
					series : {
						borderWidth : 1,
						dataLabels : {
							enabled : true
						
						}
					}
				},
			    series: [ {
										
			    						name : "Mobile User",
										data : (function() {
											var res = [];
											
											
											$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													res.push({	
														value : value.group,
														name : value.group,
														y : value.count,
														
													});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
												}else{
													dataLength = dataLength+1;
												}
											});
											
											/*$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													if (value.group !== undefined) {
														res.push({
															value : value.group,
															name : value.group,
															y : value.count,
														});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
													}
												}else{
													dataLength = dataLength+1;
												}
											});*/
											return res;
										})()
									} ]
			  }, function(chart) {
				  
					 var flag = "hideArrows";
						
					 
					  if(jsonFarmerData.length > 10){
						  flag = "showArrows";
					  } 
				 
				 if(flag == "showArrows"){
				   //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerGroupBarChart').add();
				   //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerGroupBarChart').add();
				   chart.xAxis[0].setExtremes(0,9);
				 
				   var drilldown = dataLength/10;
				   

				   if($("#arrow_farmerGroupBarChart").length > 0){
					   $("#arrow_farmerGroupBarChart").remove();
					}


					var row = jQuery('<div/>', {
						class : "col-xs-12",
						id	  : "arrow_farmerGroupBarChart"
					});
									    
					$("#farmerGroupBarChart").append($(row));
					$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerGroupBarChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
					$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerGroupBarChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

				   
				   $('.left_farmerGroupBarChart').click(function() {
				   	if(rightClick > 0){
				   	rightCount = rightCount-10;
				   	temp1 = rightCount;
						 temp2 = rightCount+9;
						 temp2 = Number(temp2);
						
						
						 var yValues =  new Array();
						 
						 
						 
						 
						 $.each(jsonFarmerData, function(index, value) {
							if(iteration2 >= temp1 ){
								yValues.push({
									value : value.group,
									name : value.group,
									y : value.count,
									 });
								 }
								iteration2 = iteration2+1;
						 	});
						 
						
						 
						chart.series[0].setData(eval(yValues), false, true);
						
						 if(yValues.length < 9){
							 chart.xAxis[0].setExtremes(0, yValues.length-1); 
						 }else{
							 chart.xAxis[0].setExtremes(0, 9); 
						 }
						 rightClick = rightClick - 1;
						 iteration2 = 1;
				   }
				   });
				   
				   $('.right_farmerGroupBarChart').click(function() {

						   if(rightClick < drilldown-1){
						    if(rightCount == 0){
						    	rightCount = rightCount+1;
						    	rightCount = ""+rightCount+rightCount;
						    	rightCount = Number(rightCount);
						    }else{
						    	rightCount = rightCount+10;
						    }
						   
						    	 temp1 = rightCount;
						    	 if(dataLength >=  Number(rightCount+9)){
						    		 temp2 = Number(rightCount+9);
						    		// temp2 = temp2+10;
						    	 }else{
						    		
						    		 var temp2  = Number(dataLength-(rightCount-1));
						    	 }
						    	 if(temp2 < temp1){
									temp2 = dataLength ;
						    	 }
								
						    	 var yValues =  new Array();
								
						    	  
						    	
										$.each(jsonFarmerData, function(index, value) {
											 if(iteration2 >= temp1 && iteration2 <= (temp2)){
													yValues.push({
														value : value.group,
														name : value.group,
														y : value.count,
														 });
													}
												iteration2 = iteration2+1;
										});
										
									
										
										
									
								 chart.series[0].setData(eval(yValues), false, true);
								
								 if(yValues.length <= 9){
									 chart.xAxis[0].setExtremes(0, (yValues.length)-1); 
								 }else{
									 chart.xAxis[0].setExtremes(0, 9); 
								 }
								
								 rightClick = rightClick+1;
								 iteration2 = 1;
							} 
					})
				}

			  });

	
}


function farmerByGroupTraderBarChart(jsonFarmerData, label) {
	 
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	$('#farmerGroupTraderBarChart').esecharts({
			    chart: {
			     // backgroundColor: '#3f3b53',
			      type: 'column',
			      marginLeft: 100,
			      marginRight: 100
			    },
			    legend: {
			      symbolHeight: 8,
			      symbolWidth: 8,
			      symbolRadius: 4,
			      margin: 15,
			      backgroundColor: '#FFFFFF',
			      layout: 'horizontal',
			      itemDistance: 25,
			      symbolMargin: 10,
			      itemStyle: {
			        color: 'black',
			        fontWeight: 'normal'
			      }
			    },
			    title: {
			      text: ''
			    },
			    xAxis : {
					type : 'category'
				},
				yAxis: {
			        title: {
			            text: '<b>Farmers Count</b>'
			        }
				},
			    tooltip: {
			    	enabled: false
			    	/*backgroundColor: '#FFFFFF',
			      borderColor: '#FFFFFF',
			      borderRadius: 0,
			      borderWidth: 5,
			      formatter: function() {
			        return ' <b>' + this.y + '</b><br><b>' + this.series.name + '</b>';
			      }*/
			    },
			    /*plotOptions: {
			      column: {
			        pointPadding: 0.2,
			        borderWidth: 0
			      }
			    },*/
			    plotOptions : {
					series : {
						borderWidth : 1,
						dataLabels : {
							enabled : true
						
						}
					}
				},
			    series: [ {
										
			    						name : "Trader",
										data : (function() {
											var res = [];
											
											
											$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													res.push({	
														value : value.group,
														name : value.group,
														y : value.count,
														
													});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
												}else{
													dataLength = dataLength+1;
												}
											});
											
											/*$.each(jsonFarmerData, function(index, value) {
												if(iteration1 <= 10){
													if (value.group !== undefined) {
														res.push({
															value : value.group,
															name : value.group,
															y : value.count,
														});
														iteration1 = iteration1+1;
														dataLength = dataLength+1;
													}
												}else{
													dataLength = dataLength+1;
												}
											});*/
											return res;
										})()
									} ]
			  }, function(chart) {
				  
					 var flag = "hideArrows";
						
					 
					  if(jsonFarmerData.length > 10){
						  flag = "showArrows";
					  } 
				 
				 if(flag == "showArrows"){
				   //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerGroupBarChart').add();
				   //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerGroupBarChart').add();
				   chart.xAxis[0].setExtremes(0,9);
				 
				   var drilldown = dataLength/10;
				   

				   if($("#arrow_farmerGroupBarChart").length > 0){
					   $("#arrow_farmerGroupBarChart").remove();
					}


					var row = jQuery('<div/>', {
						class : "col-xs-12",
						id	  : "arrow_farmerGroupBarChart"
					});
									    
					$("#farmerGroupBarChart").append($(row));
					$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerGroupBarChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
					$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerGroupBarChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

				   
				   $('.left_farmerGroupBarChart').click(function() {
				   	if(rightClick > 0){
				   	rightCount = rightCount-10;
				   	temp1 = rightCount;
						 temp2 = rightCount+9;
						 temp2 = Number(temp2);
						
						
						 var yValues =  new Array();
						 
						 
						 
						 
						 $.each(jsonFarmerData, function(index, value) {
							if(iteration2 >= temp1 ){
								yValues.push({
									value : value.group,
									name : value.group,
									y : value.count,
									 });
								 }
								iteration2 = iteration2+1;
						 	});
						 
						
						 
						chart.series[0].setData(eval(yValues), false, true);
						
						 if(yValues.length < 9){
							 chart.xAxis[0].setExtremes(0, yValues.length-1); 
						 }else{
							 chart.xAxis[0].setExtremes(0, 9); 
						 }
						 rightClick = rightClick - 1;
						 iteration2 = 1;
				   }
				   });
				   
				   $('.right_farmerGroupBarChart').click(function() {

						   if(rightClick < drilldown-1){
						    if(rightCount == 0){
						    	rightCount = rightCount+1;
						    	rightCount = ""+rightCount+rightCount;
						    	rightCount = Number(rightCount);
						    }else{
						    	rightCount = rightCount+10;
						    }
						   
						    	 temp1 = rightCount;
						    	 if(dataLength >=  Number(rightCount+9)){
						    		 temp2 = Number(rightCount+9);
						    		// temp2 = temp2+10;
						    	 }else{
						    		
						    		 var temp2  = Number(dataLength-(rightCount-1));
						    	 }
						    	 if(temp2 < temp1){
									temp2 = dataLength ;
						    	 }
								
						    	 var yValues =  new Array();
								
						    	  
						    	
										$.each(jsonFarmerData, function(index, value) {
											 if(iteration2 >= temp1 && iteration2 <= (temp2)){
													yValues.push({
														value : value.group,
														name : value.group,
														y : value.count,
														 });
													}
												iteration2 = iteration2+1;
										});
										
									
										
										
									
								 chart.series[0].setData(eval(yValues), false, true);
								
								 if(yValues.length <= 9){
									 chart.xAxis[0].setExtremes(0, (yValues.length)-1); 
								 }else{
									 chart.xAxis[0].setExtremes(0, 9); 
								 }
								
								 rightClick = rightClick+1;
								 iteration2 = 1;
							} 
					})
				}

			  });
}


function farmerDetailsBarChart(jsonFarmerData, label, farmerCountLabel,
		acregeLabel, productionLabel) {
	var color = [ '#CBECB0', '#B2E389', '#609D2D' ];
console.log("XXXX "+JSON.stringify(jsonFarmerData));
	esecharts
			.chart(
					'farmerDetailsBarChart',
					{
						chart: {
					        type: 'column'
					    },
					    title: {
					        text: ''
					    },
					    xAxis : {type : 'category'},
						yAxis : [ {type : 'value'} ],

					    legend: {
					       enabled : false
					    },
					    tooltip: {
					       headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
						   pointFormat : '<span style="color:{getRandomColor()}">{point.name}</span>: <b>{point.y}</b> {point.units}<br/>'
						},
					    plotOptions: {
					        series: {
					           
					            dataLabels: {
					                enabled: true
					            }
					        }
					    },
						series : [ {
							name : label,
							// colorByPoint: getRandomColor(),
							// color:getRandomColor(),
							data : (function() {
								var res = [];
								var len = 0;
								var count = 0;
								if (getCurrentTenantId() != "fincocoa") {
									var units = new Array("", "Acres", "Kgs",
											"Kgs");
								} else {
									var units = new Array("", "Hectares",
											"Kgs", "Kgs");
								}

								$.each(jsonFarmerData, function(index, value) {

									if (value.values !== undefined) {
										res.push({
											//value : value.item,
											name : value.item,
											y : value.values,
											color : color[count++],
											units : units[index]

										// drilldown:value.group.replace("
										// ","_")
										});
									}
								});
								return res;
							})()
						} ]

					});

}

function doughnutChartCowCost(label, housingLabel, seedLabel, treatLabel,
		otherLable, housingCost, seedCost, treatCost, otherCost) {
	var doughnutChartCowCost = echarts.init(document
			.getElementById('doughnutChartCowCost'));

	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : [ getRandomColor() ],
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		legend : {
			orient : 'horizontal',
			x : 'left',
			y : 'bottom',
			data : [ housingLabel, seedLabel, treatLabel, otherLable ],
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder'
			},
		},
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : [ getRandomColor() ],
			},
		},
		toolbox : {
			show : true,
			title : '',
			feature : {
				magicType : {
					show : true,
					title : '',
					type : [ 'pie', 'funnel' ],
					option : {
						funnel : {
							x : '25%',
							width : '50%',
							funnelAlign : 'center',
							max : 1548
						}
					}
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'pie',
			radius : [ '50%', '70%' ],
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
				emphasis : {
					label : {
						show : true,
						position : 'center',
						textStyle : {
							fontSize : '30',
							fontWeight : 'bold'
						}
					}
				}
			},
			data : [ {
				value : housingCost,
				name : housingLabel
			}, {
				value : seedCost,
				name : seedLabel
			}, {
				value : treatCost,
				name : treatLabel
			}, {
				value : otherCost,
				name : otherLable
			} ]
		} ]
	};
	doughnutChartCowCost.setOption(option);
}

function farmerCropsBarChart(jsonFarmerData, label, farmerCountLabel,
		acregeLabel, productionLabel) {
	esecharts
			.chart(
					'farmercropBarChart',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						/*
						 * subtitle: { text: 'Click the columns to view
						 * versions. Source: <a
						 * href="http://netmarketshare.com">netmarketshare.com</a>.' },
						 */
						xAxis : {
							type : 'category'
						},
						yAxis : [ {
							type : 'value'
						} ],
						legend : {
							enabled : false
						},
						plotOptions : {
							series : {
								borderWidth : 1,
								dataLabels : {
									enabled : true
								/* format: '{point.y:.1f}%' */
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span style="color:{getRandomColor()}">{point.name}</span>: <b>{point.y}</b> {point.units}<br/>'
						},

						series : [ {
							name : 'Farmer Crop Details',
							colorByPoint : getRandomColor(),
							// color:getRandomColor(),
							data : (function() {
								var res = [];
								var len = 0;
								if (getCurrentTenantId() != "fincocoa") {
									var units = new Array("", "Acres", "Kgs",
											"Kgs");
								} else {
									var units = new Array("", "Hectares",
											"Kgs", "Kgs");
								}

								$.each(jsonFarmerData, function(index, value) {

									if (value.values !== undefined) {
										res.push({
											value : value.item,
											name : value.item,
											y : value.values,
											units : units[index]

										// drilldown:value.group.replace("
										// ","_")
										});
									}
								});
								return res;
							})()
						} ]
					/*
					 * drilldown: {
					 * 
					 * series:drillArray
					 *  }
					 */
					});

}

function farmerEconomyExpensesByOrgAndYear() {
	var org;
	/*
	 * if(getCurrentBranch()==""){
	 * org=$("#farmerEconomyExpenseBranchFilter").val(); } else{
	 * org=getCurrentBranch(); }
	 */
	var year = $("#farmerEconomyTimelineYearFilter").val();

	jQuery
			.post(
					"dashboard_populateFarmerEconomyExpenses.action",
					{
						selectedBranch : $("#farmerEconomyExpenseBranchFilter")
								.val(),
						selectedYear : year
					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#farmerEconomyExpensesChart")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							loadFarmerEconomyExpensesChart(jsonFarmerData,
									label, year);
						}

					});

}

function loadFarmerEconomyExpensesChart(jsonFarmerData, label, year) {
	var BarChart = echarts.init(document
			.getElementById('farmerEconomyExpenses'));
	option = {
		/*
		 * title : { text : label },
		 */
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{a} : Rs. {c}",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 12,
				fontStyle : 'italic',
				fontWeight : 'bold'

			}
		},
		legend : {
			orient : 'horizontal',
			y : 330,
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
			},
			data : [ jsonFarmerData[1].item, jsonFarmerData[2].item ]
		},
		color : [ getRandomColor() ],
		toolbox : {
			show : true,
			tooltip : {
				type : "1",
			},
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : [ (year - 1) + " - " + year ]
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			name : jsonFarmerData[1].item,
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'top',
						formatter : 'Rs. {c}',
						textStyle : {
							color : 'black',
							fontStyle : 'normal',
							fontWeight : 'bolder',
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (index == 1) {
						if (value.values !== undefined) {
							res.push({
								value : value.values
							});
						}
					}

				});
				return res;
			})()
		}, {
			name : jsonFarmerData[2].item,
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'top',
						formatter : 'Rs. {c}',
						textStyle : {
							color : 'black',
							fontStyle : 'normal',
							fontWeight : 'bolder',
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonFarmerData, function(index, value) {
					if (index == 2) {
						if (value.values !== undefined) {
							if (value.values !== undefined)
								res.push({
									value : value.values
								});
						}
					}

				});
				return res;
			})()
		} ]
	}
	BarChart.setOption(option);
}

function farmerDataStatByOrgAndYear() {
	var org;
	/*
	 * if(getCurrentBranch()==""){ org=$("#farmerDataStatBranchFilter").val(); }
	 * else{ org=getCurrentBranch(); }
	 */
	var year = $("#farmerDataStatTimelineYearFilter").val();

	jQuery
			.post(
					"dashboard_populateFarmerDataStatistics.action",
					{
						selectedBranch : $("#farmerDataStatBranchFilter").val(),
						selectedYear : year
					},
					function(result) {
						var label = extractLabelValue(result, "Label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#farmerEconomyExpensesChart")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							loadFarmerDataStatChart(jsonFarmerData, label, year);
						}

					});

}

function loadFarmerDataStatChart(jsonFarmerData, label, year) {
	var color = [ '#5F7B91', '#52738C', '#2E566F' ];
	esecharts
			.chart(
					'farmerDataStatistics',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						/*
						 * subtitle: { text: 'Click the columns to view
						 * versions. Source: <a
						 * href="http://netmarketshare.com">netmarketshare.com</a>.' },
						 */
						xAxis : {
							type : 'category'
						},
						yAxis : [ {
							type : 'value'
						} ],
						legend : {
							enabled : false
						},
						plotOptions : {
							series : {
								borderWidth : 1,
								dataLabels : {
									enabled : true
								/* format: '{point.y:.1f}%' */
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span style="color:{getRandomColor()}">{point.name}</span>: <b>{point.y}</b> {point.units}<br/>'
						},

						series : [ {
							name : label,
							// colorByPoint: getRandomColor(),
							// color:getRandomColor(),
							data : (function() {
								var res = [];
								var len = 0;
								var count = 0;
								$.each(jsonFarmerData, function(index, value) {
									if (value.values !== undefined) {
										res.push({
											// value : value.item,
											name : value.item,
											y : value.values,
											color : color[count++],

										// drilldown:value.group.replace("
										// ","_")
										});
									}
								});
								return res;
							})()
						} ]
					/*
					 * drilldown: {
					 * 
					 * series:drillArray
					 *  }
					 */
					});

}

function farmerICSDetailBarChart() {
	var season = jQuery("#icsSeasonFilter").val();
	var org = jQuery("#farmerICSDetailFilter").val();
	var gender = jQuery("#farmerIcsGenderFilter").val();
	var state = jQuery("#farmerIcsStateFilter").val();

	var icsFarmerBranchData = "";
	jQuery
			.post(
					"dashboard_populateFarmerICSDetailsBarChartData.action",
					{
						selectedBranch : org,
						selectedSeason : season,
						selectedGender : gender,
						selectedState : state
					},
					function(result) {
						if (result == "" || result == null || result == "[{}]") {
							$("#farmerICSDetailsBarChart")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b></b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
						} else {
							var label = extractLabelValue(result, "label");
							save = extractLabelValue(result, "save");
							Refresh = extractLabelValue(result, "refresh");
							result = findAndRemove(result, "id", "Label");
							var jsonFarmerData = jQuery.parseJSON(result);
							$.each(jsonFarmerData, function(index, value) {
								if (index == 0) {
									farmerIcsData = value;
								}
								if (index == 1) {
									icsFarmerGroupData = value;
								}
								if (index == 2) {
									icsFarmerBranchData = value;
								}
							});
							farmerICSDetailsBarChart(farmerIcsData,
									icsFarmerGroupData, icsFarmerBranchData,
									label);

						}

					});
}

function farmerICSDetailsBarChart(jsonFarmerData, icsFarmerGroupData,
		icsFarmerBranchData, label) {
	var dataArray = new Array();

	if (getCurrentBranch() != '') {
		var color = [ '#D1D28E', '#A0A44F', '#A0A44F', '#737532' ];
		$.each(jsonFarmerData, function(index, value) {
			var farmerGroupArr = new Array();

			if (value.values !== undefined) {

				$.each(icsFarmerGroupData, function(index, value1) {
					if (value.item == value1.item) {
						if (value1.icsFarmerGroup !== undefined)
							farmerGroupArr.push([ "Farmer Group",
									value1.icsFarmerGroup, value1.farmerCount,
									"Farmer Ics >" + value1.icsFarmerGroup ]);
					}

				});
				dataArray.push([
						"Farmer Ics",
						value.item,
						value.values,
						color[index],
						value.item,
						{
							"COLUMNS" : [ "CONTEXT", "X_BAR_LABEL",
									"X_BAR_VALUE", "X_BAR_COLOR",
									"TOOL_TIP_TITLE" ],
							"DATA" : farmerGroupArr,
						}

				]);
			}
		});
	} else {
		var colorBranch = [ '#D1D28E', '#A0A44F', '#A0A44F' ];
		var colorIcs = [ '#A9BFCA', '#7F9FB0', '#547F95', '#295F7B' ];

		$
				.each(
						icsFarmerBranchData,
						function(index, value) {

							var farmerIcsArr = new Array();
							var count = 0;

							$
									.each(
											jsonFarmerData,
											function(indexIcs, icsValue) {
												var farmerGroupArray = new Array();

												if (icsValue.values !== undefined) {
													$
															.each(
																	icsFarmerGroupData,
																	function(
																			index,
																			groupValue) {
																		// console.log("icsValu"+icsValue.item+"groupVal"+groupValue.item)
																		if (icsValue.item == groupValue.item
																				&& groupValue.branch == value.branch) {
																			// console.log(groupValue.icsFarmerGroup)
																			farmerGroupArray
																					.push([
																							"Farmer Group",
																							groupValue.icsFarmerGroup,
																							groupValue.farmerCount,
																							"Farmer Ics >"
																									+ groupValue.icsFarmerGroup ]);
																		}

																	});
													if (icsValue.branch == value.branch) {
														farmerIcsArr
																.push([
																		"Farmer Ics",
																		icsValue.item,
																		icsValue.values,
																		colorIcs[count++],
																		icsValue.item,
																		{
																			"COLUMNS" : [
																					"CONTEXT",
																					"X_BAR_LABEL",
																					"X_BAR_VALUE",
																					"X_BAR_COLOR",
																					"TOOL_TIP_TITLE" ],
																			"DATA" : farmerGroupArray,
																		}

																]);

													}
												}
											});
							dataArray
									.push([
											"Organization",
											value.branch,
											value.branchFarmer,
											colorBranch[index],
											value.branch,
											{
												"COLUMNS" : [ "CONTEXT",
														"X_BAR_LABEL",
														"X_BAR_VALUE",
														"X_BAR_COLOR",
														"TOOL_TIP_TITLE",
														"DRILL_DATA" ],
												"DATA" : farmerIcsArr,
											}

									]);
						});

	}

	$("#farmerICSDetailsBarChart").ddBarChart(
			{
				chartData : {
					"COLUMNS" : [ "CONTEXT", "X_BAR_LABEL", "X_BAR_VALUE",
							"X_BAR_COLOR", "TOOL_TIP_TITLE", "DRILL_DATA" ],
					"DATA" : dataArray

				},
				action : 'init',
				chartBarClass : "ui-state-focus ui-corner-top",
				chartBarHoverClass : "ui-state-highlight",
				callBeforeLoad : function() {
					$('#loading-Notification_static').fadeIn(500);
				},
				callAfterLoad : function() {
					$('#loading-Notification_static').stop().fadeOut(500);
				},
				tooltipSettings : {
					extraClass : "ui-widget ui-widget-content ui-corner-all"
				}
			});
}

function farmerHarvestSale() {
	var year = jQuery("#yearFiltertxn").val();
	var org = jQuery("#harvestSaleFilter").val();

	jQuery
			.post(
					"dashboard_populateCropHarvestAndSaleChart.action",
					{
						selectedBranch : org,
						selectedYear : year
					},
					function(result) {
						var label = extractLabelValue(result, "label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[{}]") {
							$("#doughnutFarmerCountByBranch")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							harvestSaleBarChart(jsonFarmerData, label);
						}
					});
}

function harvestSaleBarChart(jsonFarmerData, label) {
	var BarChart = echarts.init(document.getElementById('harvestSaleBarChart'));

	option = {
		title : {
			text : label
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} : {c}",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 12,
				fontStyle : 'italic',
				fontWeight : 'bold'

			}
		},
		legend : {
			orient : 'horizontal',
			x : 10,
			y : 260,
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 13,
				fontWeight : 'bolder',
				top : 100
			},
			data : [ "Crop Harvest" ]
		},
		toolbox : {
			show : true,
			tooltip : {
				type : "1",
			},
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : [ "Cotton Quantity" ]
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			name : 'Crop Harvest',
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'top',
						formatter : '{c}',
						textStyle : {
							color : 'black',
							fontStyle : 'normal',
							fontWeight : 'bolder',
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				var units = "";
				$.each(jsonFarmerData, function(index, value) {
					if (value.values !== undefined)
						res.push({
							value : value.values,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										formatter : '{c} ' + units,
										textStyle : {
											color : 'black',
											fontWeight : 'bolder',
											fontSize : 16,
										}
									}
								}
							}
						});

				});
				if (res.length == 4) {
					res[3].itemStyle.normal.color = "lightgreen";
				}
				return res;
			})()
		} /*
			 * , { name : 'Crop Sale', type : 'bar', itemStyle : { normal : {
			 * label : { show : true, position : 'top', formatter : '{c}',
			 * textStyle : { color : 'black', fontStyle : 'normal', fontWeight :
			 * 'bolder', } } } }, data : (function() { var res = []; var len =
			 * 0; $.each(jsonFarmerData, function(index, value) { if (index ==2) {
			 * if (value.values !== undefined) { if (value.values !== undefined)
			 * res.push({ value : value.values }); } }
			 * 
			 * }); return res; })() }
			 */]
	}
	BarChart.setOption(option);
}

function populateTrainingChart(val){
	var selectedYear =val;
	var org;
	if (getCurrentBranch() == "") {
		org = $("#trainingBranchFilter").val();
	} else {
		org = getCurrentBranch();
	}
	jQuery
	.post(
			"dashboard_populateTrainingChart.action",
			{
				selectedBranch : org,
				selectedFinYear:selectedYear
			},
			function(result) {
				var label = "Trainings";
				if (result == "" || result == null || result == "[]") {
					$("#trainingChart")
							.html(
									"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
											+ label
											+ "</b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
				} else {
					var jsonData =  $.parseJSON(result);
					if(!isEmpty(jsonData)){
						loadTrainingChart(org,jsonData);
					   }
					
				}

			});
}
function loadTrainingChart(org,trainingData){
	esecharts.chart('trainingChart',{
				chart : {
					type : 'column',
					options3d : {
						enabled : false,
						alpha : 10,
						beta : 20,
						depth : 70
					}
				},

				title : {
					text : ''
				},
				subtitle : {
					text : ''
				},
				
				legend : {
					enabled : false
				},
				plotOptions : {
					allowPointSelect : true,
					cursor : 'pointer',
					series : {
						dataLabels : {
							enabled : true,
							format : '{point.y}  <b>({point.units})</b>',
						}
					}
				},
				xAxis : {
					type : 'category'
				},
				yAxis : {
					title : {
						text : ''
					}
				},
				tooltip : {
					headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
					pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>  ({point.units}) <br/>'
				},
				series : [
							{
								name : 'Training',
								data : (function() {
									var res = [];
									var len = 0;
									if(org!=null && org!=""){
										$.each(trainingData, function(index,
												value) {
											
											if (value.totTraining !== undefined && org==value.branchId)

												res.push({
													name : value.branchName,
													id : 'Training',
													y : value.totTraining,
													units : 'Nos'
												// color : getRandomColor(),
												});

										});
									}else{
									$.each(trainingData, function(index,
											value) {
										
										if (value.totTraining !== undefined)

											res.push({
												name : value.branchName,
												id : 'Training',
												y : value.totTraining,
												units : 'Nos'
											// color : getRandomColor(),
											});

									});
								}
									return res;
								})(),

								point : {
									events : {
										click : function(event) {
											
											loadTrainingDrillDwnCharts(this.name,trainingData,org);
										}
									}
								}
							},
					
							],

			});
	
}
function loadTrainingDrillDwnCharts(name,dataVal,org){
esecharts.chart('trainingChart',{
	chart : {
		type : 'column',
		options3d : {
			enabled : false,
			alpha : 10,
			beta : 20,
			depth : 70
		}
	},

	title : {
		text : ''
	},
	subtitle : {
		text : ''
	},
	legend : {
		enabled : false
	},
	plotOptions : {
		allowPointSelect : true,
		cursor : 'pointer',
		series : {
			dataLabels : {
				enabled : true,
				format : '{point.y}  <b>({point.units})</b>',
			}
		}
	},
	xAxis : {
		type : 'category'
	},
	yAxis : {
		title : {
			text : null
		}
	},
	tooltip : {
		headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
		pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>  ({point.units}) <br/>'
	},
	series : [
				{
					name : 'Warehouse',
					data : (function() {
						var result = [];
						var len = 0;
						$.each(dataVal, function(index,
								value) {
							if(value.branchName==name){
								var warVal=value.warehouseVal.split("$");
								$.each(warVal, function(k,v) {
									var spl=v.split("~");
									var xVal=spl[1];
									var aVal=spl[2];
									var yVal=spl[0].trim().split(",").length;
									if ( yVal !== undefined &&  yVal!=''){
										
										result.push({
											name : xVal,
											id : 'Warehouse',
											y :  yVal,
											units : 'Nos',
											warehouseId: aVal
										});
									}
									
									
								});
							}
						});
						
						return result;
						
					})(),

					point : {
						events : {
							click : function(event) {
								
								loadAgentTrainingDrillDwnCharts(this.warehouseId,dataVal,name,org);
							}
						}
					}
				},
		
				],

});
customTrainingBackButton(dataVal,org);

}
function loadAgentTrainingDrillDwnCharts(warehouseId,dataVal,name,org){
	var selectedfinYear = jQuery("#finaYearFilter").val();

	jQuery.post(
			"dashboard_populateAgentTrainings.action",
			{selectedWarehouse : warehouseId,selectedFinYear:selectedfinYear},
			function(data) {
				var label = "Field Staff & Center Manager";
				if (data == "" || data == null || data == "[]") {
					$("#trainingChart").html(
									"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
											+ label
											+ "</b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
				} else {
					var jsonData =  $.parseJSON(data);
					if(!isEmpty(jsonData)){
						esecharts.chart('trainingChart',{
							chart : {
								type : 'column',
								options3d : {
									enabled : false,
									alpha : 10,
									beta : 20,
									depth : 70
								}
							},
							title : {
								text : ''
							},
							subtitle : {
								text : ''
							},
							legend : {
								enabled : false
							},
							
						    /*plotOptions: {
						        column: {
						            pointPadding: 0.2,
						            size: '95%',
						            borderWidth: 0,
						            events: {
						                legendItemClick: function () {
						                    return false; 
						                }
						            }
						        },
						        allowPointSelect: false,
						        cursor : 'pointer',
								series : {
									dataLabels : {
										enabled : true,
										format : '{point.y}  <b>({point.units})</b>',
									}
								}
						    },*/
						    
						    
							plotOptions : {
								allowPointSelect : true,
								cursor : 'pointer',
								series : {
									dataLabels : {
										enabled : true,
										format : '{point.y}  <b>({point.units})</b>',
									}
								}
							},
							xAxis : {
								type : 'category'
							},
							yAxis : {
								title : {
									text : ''
								}
							},
							tooltip : {
								headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
								pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>  ({point.units}) <br/>'
							},
							series : [
										{
											name : 'FieldStaff',
											data : (function() {
												var result = [];
												var len = 0;
												$.each(jsonData, function(index,
														value) {
															if ( value.agentId !== undefined &&  value.agentId!=''){
																if(value.agentType=='05'){
																	result.push({
																	name : value.agentName,
																	id : 'agent',
																	y :  value.trainData,
																	units : 'Nos',
																	color:'#5fad86',
																	agentId: value.agentId,
																	trainData: value.trainingData,
																	trineeData:value.traineeData
																	
																});
																}
																	else{
																		result.push({
																		name : value.agentName,
																		id : 'agent',
																		y :  value.trainData,
																		units : 'Nos',
																		color:'#835fad',
																		agentId: value.agentId,
																		trainData: value.trainingData,
																		traineeData:value.traineeData
																	});
																	}
																
															}
															
															
														
													
												});
												
												return result;
												
											})(),

											point : {
												events : {
													click : function(event) {
														loadFarmerTrainingDrillDwnCharts(this.agentId,this.trainData,warehouseId,dataVal,name,this.traineeData,org);
													}
												}
											}
										},
								
										],
						});
						customTrainingAgentBackButton(dataVal,name,org);
					   }
				}
			});
	
}

function customTrainingAgentBackButton(dataVal,name,org) {

	 var chart = $('#trainingChart').esecharts();
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

	    var custombutton = chart.renderer.button('Back', 1100, 20, function(){
	    	loadTrainingDrillDwnCharts(name,dataVal,org)
	    },null,hoverState,pressedState).add();

}
function loadFarmerTrainingDrillDwnCharts(agentId,jsonData,warehouseId,dataVal,name,traineeData,org){
	esecharts.chart('trainingChart',{
		chart : {
			type : 'column',
			options3d : {
				enabled : false,
				alpha : 10,
				beta : 20,
				depth : 70
			}
		},

		title : {
			text : ''
		},
		subtitle : {
			text : ''
		},
		legend : {
			enabled : false
		},
		plotOptions : {
			allowPointSelect : true,
			cursor : 'pointer',
			series : {
				dataLabels : {
					enabled : true,
					format : '{point.y}  <b>({point.units})</b>',
				}
			}
		},
		xAxis : {
			type : 'category'
		},
		yAxis : {
			title : {
				text : null
			}
		},
		tooltip : {
			headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
			pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>  ({point.units}) <br/>'
		},
		series : [
					{
						name : 'Trainings',
						data : (function() {
							var result = [];
							var len = 0;
							var fLevel=jsonData.split("^");
							
							$.each(fLevel, function(key,val) {
								var actVal=val.split("=");
								var xAxisVal=actVal[0].split("-")[1];
								if(actVal[1]!=undefined && actVal[1]!=''){
									result.push({
										name :xAxisVal,
										id : 'FarmerTraining',
										y :  parseInt(actVal[1].trim()),
										units : 'Nos'
									});
								}
							});
							
							return result;
							
						})(),
					
						
					},
					{
						name : 'Trainees',
						data : (function() {
							var result = [];
							var len = 0;
							var fLevel=traineeData.split("^");
							
							$.each(fLevel, function(key,val) {
								var actVal=val.split("=");
								var xAxisVal=actVal[0].split("-")[1];
								if(actVal[1]!=undefined && actVal[1]!=''){
									result.push({
										name :xAxisVal,
										id : 'TraineeCount',
										y :  parseInt(actVal[1].trim()),
										units : 'Farmers',
											color:'#835fad',
									});
								}
							});
							
							return result;
							
						})(),
					
						
					},
			
					],
					
					

	});
	customTrainingCountBackButton(warehouseId,dataVal,name,org);
}

function customTrainingCountBackButton(warehouseId,dataVal,name,org) {
	var chart = $('#trainingChart').esecharts();
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
	var custombutton = chart.renderer.button(
			'Back',
			1100,
			10,
			function() {
				loadAgentTrainingDrillDwnCharts(warehouseId,dataVal,name,org);
			}, null, hoverState, pressedState).add();

}


function customTrainingBackButton(jsonData,org) {
	var chart = $('#trainingChart').esecharts();
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
	var custombutton = chart.renderer.button(
			'Back',
			1100,
			10,
			function() {
				loadTrainingChart(org,jsonData);
			}, null, hoverState, pressedState).add();

}

var totExpRevDatas = null;
var totCocExpDatas = null;
var totNetDatas = null;
var revProdDatas = null;
function cocSegregateValues() {
	var org;
	$("#farmerCocTitle").text("Farmer Cost of Cultivation");
	if (getCurrentBranch() == "") {
		org = $("#cocSegregateBranchFilter").val();
	} else {
		org = getCurrentBranch();
	}
	var year = $("#cocSegregateYearFilter").val();
	var season = $("#cocSeasonFilter").val();

	jQuery
			.post(
					"dashboard_populateCocSegregate.action",
					{
						selectedBranch : org,
						selectedSeason : season,
						selectedYear : year
					},
					function(result) {
						var label = "Farmer Cost of Cultivation";
						if (result == "" || result == null || result == "[]") {
							$("#cocSegregate")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							$.each(jsonFarmerData, function(index, value) {

								if (index == 0) {
									totCocExpDatas = value;
								} else if (index == 1) {
									totExpRevDatas = value;
								}

								/*	else if (index == 2) {
									totNetDatas = value;
								}*/ else if (index == 2) {
									revProdDatas = value;
								}
							});
							loadCocSegrateValues(totCocExpDatas,
									totExpRevDatas, revProdDatas);
						}

					});

}
function loadMobSowingSegrateValues(warehouseId,mobileUser,branch,warehouse){
	esecharts
	.chart(
			'sowingSegregateValues',
			{
				chart : {
					type : 'column',
					// backgroundColor:'rgba(255, 255, 255, 0.0)',

					options3d : {
						enabled : false,
						alpha : 10,
						beta : 20,
						depth : 70
					}
				},

				title : {
					text : 'Plotting Area For Sowing (Mobile User)'
				},
				subtitle : {
					text : ''
				},
				legend : {
					enabled : false
				},
				plotOptions : {
					/*
					 * column: { depth: 450 },
					 */
					allowPointSelect : true,
					cursor : 'pointer',
					series : {
						// borderWidth: 0,
						dataLabels : {
							enabled : true,
						format : '{point.y}  <b>({point.units})</b>',
						// format: '<b>{point.name}</b>: {point.y}',

						}
					}
				},
				xAxis : {
					type : 'category'
				},
				yAxis : {
					title : {
						text : null
					}
				},
				tooltip : {
					headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
					// pointFormat: '<span
					// style="color:{point.color}">{point.name}</span>:
					// <b>{point.y:.2f}%</b> of total<br/>'
					pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> ({point.units}) <br/>'
				},
				series : [
						{
							name : 'Area',
							data : (function() {
								var res = [];
								var len = 0;
								$.each(mobileUser, function(index,
										mobileUser) {
									if (warehouseId == mobileUser.warehouseCode)

										res.push({
											name :mobileUser.mobileUserCode +"-"+ mobileUser.mobileUserName,
											id : 'Acres',
											y : mobileUser.acres,
											units : 'Hectares'
										// color : getRandomColor(),
										});

								});
								return res;
							})(),

					
						},
						{
							name : 'Count',
							data : (function() {
								var res = [];
								var len = 0;
								$.each(mobileUser, function(index,
										mobileUser) {
									//if (value.revenue !== undefined)
									if (warehouseId == mobileUser.warehouseCode)
										res.push({
											name : mobileUser.mobileUserCode +"-"+mobileUser.mobileUserName,
											id : 'Count',
											y : mobileUser.count,
											units : 'Nos'
										// color : getRandomColor(),
										});

								});
								return res;
							})(),

						
						},
						],

			});
	customMobSowingBackButton(warehouse,mobileUser,branch);
}
function customMobSowingBackButton(warehouse,mobileUser,branch){
	var chart = $('#sowingSegregateValues').esecharts();
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

	var custombutton = chart.renderer.button(
			'Back',
			1100,
			10,
			function() {
				renderFarmerSowingSegregate(branch,warehouse,mobileUser);
				
			}, null, hoverState, pressedState).add();


}

function customSowingBackButton(branch,mobileUser,warehouse){
var chart = $('#sowingSegregateValues').esecharts();
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

var custombutton = chart.renderer.button(
		'Back',
		1100,
		10,
		function() {
			renderFarmerSowingSegregate(branch,mobileUser,warehouse);
			
		}, null, hoverState, pressedState).add();

}
function loadSowingSegrateValues(branchId,warehouse,mobileUser,branch){
	esecharts
	.chart(
			'sowingSegregateValues',
			{
				chart : {
					type : 'column',
					// backgroundColor:'rgba(255, 255, 255, 0.0)',

					options3d : {
						enabled : false,
						alpha : 10,
						beta : 20,
						depth : 70
					}
				},

				title : {
					text : 'Plotting Area For Sowing (Warehouse)'
				},
				subtitle : {
					text : ''
				},
				legend : {
					enabled : true
				},
				plotOptions : {
					/*
					 * column: { depth: 450 },
					 */
					allowPointSelect : true,
					cursor : 'pointer',
					series : {
						// borderWidth: 0,
						dataLabels : {
							enabled : true,
							format : '{point.y}  <b>({point.units})</b>',
						// format: '<b>{point.name}</b>: {point.y}',

						}
					}
				},
				xAxis : {
					type : 'category'
				},
				yAxis : {
					title : {
						text : null
					}
				},
				tooltip : {
					headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
					// pointFormat: '<span
					// style="color:{point.color}">{point.name}</span>:
					// <b>{point.y:.2f}%</b> of total<br/>'
					pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>({point.units}) <br/> '
				},
				series : [
						{
							name : 'Area',
							data : (function() {
								var res = [];
								var len = 0;
								$.each(warehouse, function(index,
										warehouse) {
									if (branchId == warehouse.branchId)

										res.push({
											name : warehouse.warehouseName,
											id :warehouse.profileId,
											y : warehouse.acres,
											units : 'Hectares'
										// color : getRandomColor(),
										});

								});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										loadMobSowingSegrateValues(this.id,mobileUser,branch,warehouse);
									
									}
								}
							}
						},
						{
							name : 'Count',
							data : (function() {
								var res = [];
								var len = 0;
								$.each(warehouse, function(index,
										warehouse) {
									//if (value.revenue !== undefined)
									if (branchId == warehouse.branchId)
										res.push({
											name : warehouse.warehouseName,
											id :warehouse.profileId,
											y : warehouse.count,
											units : 'Nos'
										// color : getRandomColor(),
										});

								});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										var data = null;
									
										loadMobSowingSegrateValues(this.id,mobileUser,branch,warehouse);
									}
								}
							}
						},
						],

			});
	customSowingBackButton(branch,mobileUser,warehouse);
}

function loadCocSegrateValues(totCocExpDatas, totExpRevDatas, 
		revProdDatas) {
	var seriesIcsFarmerArray = new Array();

	/*
	 * $.each(totExpRevDatas, function(index, value) { var farmerGroupArr=new
	 * Array(); $.each(totCocExpDatas, function(index, value1) { //
	 * alert("--"+value1.values) //alert(value1.season)
	 * 
	 * if(value1.season==value.season) { farmerGroupArr.push({ name :
	 * value1.items, id:value1.items, y : value1.values, // color :
	 * getRandomColor(), });
	 * 
	 * seriesIcsFarmerArray.push({ id : value1.season, name : value1.season,
	 * data:farmerGroupArr }); } }); });
	 * 
	 */

	esecharts
			.chart(
					'cocSegregate',
					{
						chart : {
							type : 'column',
							// backgroundColor:'rgba(255, 255, 255, 0.0)',

							options3d : {
								enabled : false,
								alpha : 10,
								beta : 20,
								depth : 70
							}
						},

						title : {
							text : ''
						},
						subtitle : {
							text : ''
						},
						legend : {
							enabled : true
						},
						plotOptions : {
							/*
							 * column: { depth: 450 },
							 */
							allowPointSelect : true,
							cursor : 'pointer',
							series : {
								// borderWidth: 0,
								dataLabels : {
									enabled : true,
									format : '{point.y}  <b>({point.units})</b>',
								// format: '<b>{point.name}</b>: {point.y}',

								}
							}
						},
						xAxis : {
							type : 'category'
						},
						yAxis : {
							title : {
								text : null
							}
						},
						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							// pointFormat: '<span
							// style="color:{point.color}">{point.name}</span>:
							// <b>{point.y:.2f}%</b> of total<br/>'
							pointFormat : '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>  ({point.units}) <br/>'
						},
						series : [
								{
									name : 'Total Expense',
									data : (function() {
										var res = [];
										var len = 0;
										$.each(totExpRevDatas, function(index,
												value) {
											if (value.expense !== undefined)

												res.push({
													name : value.season,
													id : 'Expense',
													y : value.expense,
													units : 'INR'
												// color : getRandomColor(),
												});

										});
										return res;
									})(),

									point : {
										events : {
											click : function(event) {
												
												loadCocExpDrillDwnCharts(
														totCocExpDatas,
														this.name);
											}
										}
									}
								},
								{
									name : 'Revenue',
									data : (function() {
										var res = [];
										var len = 0;
										$.each(totExpRevDatas, function(index,
												value) {
											if (value.revenue !== undefined)
												res.push({
													name : value.season,
													id : 'Revenue',
													y : value.revenue,
													units : 'INR'
												// color : getRandomColor(),
												});

										});
										return res;
									})(),

									point : {
										events : {
											click : function(event) {
												var data = null;
												loadCocRevDrillDwnCharts(
														revProdDatas, this.name);

											}
										}
									}
								},
								/*
								 * { type: 'spline', name: 'Average', data :
								 * (function() { var res = []; var len = 0;
								 * $.each(totRevDatas, function(index, value) {
								 * if (value.revenue !== undefined) res.push({
								 * name : monthsArray[value.month], y :
								 * value.revenue, // color : getRandomColor(),
								 * //drilldown : '5' });
								 * 
								 * }); return res; })(), marker: { lineWidth: 2,
								 * lineColor: esecharts.getOptions().colors[3],
								 * fillColor: 'white' } },
								 *//*{
									type : 'pie',
									name : 'Total Cultivation',

									data : (function() {
										var res = [];
										var len = 0;
										$.each(totNetDatas, function(index,
												value) {
											if (value.amounts !== undefined)
												res.push({
													name : value.labels,
													y : value.amounts,
													units : 'INR'
												// color : getRandomColor(),
												// drilldown : '5'
												});

										});
										return res;
									})(),
									center : [ 800, 30 ],
									size : 100,
									showInLegend : false,
									dataLabels : {
										enabled : true,
										format : '<b>{point.name}</b> {point.y}  <b>({point.units})</b>',

									}
								} */],

					});
}


function cocNetIncomePieCharts()
{

	esecharts.chart('cocNetIncomePieChart', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: null,
	        plotShadow: false,
	        type: 'pie'
	    },
	    title: {
	        text: 'Cultivation Net Income Details'
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.y} ({point.units})</b>'
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	                format: '<b>{point.name}</b>: </br>{point.y} ({point.units})',
	                style: {
	                    color: (esecharts.theme && esecharts.theme.contrastTextColor) || 'black'
	                }
	            }
	        }
	    },
	    series: [{
	        name: 'Cultivation NetIncome',
	        colorByPoint: true,
	    	data : (function() {
				var res = [];
				var len = 0;
				$.each(totNetDatas, function(index,
						value) {
					if (value.amounts !== undefined)
						res.push({
							name : value.labels,
							y : value.amounts,
							units : 'INR'
						// color : getRandomColor(),
						// drilldown : '5'
						});

				});
				return res;
			})(),
	    }]
	});
	
	customBackButton();
}


function farmerProdDetailBarChart(val) {
	var org;
	var season = $("#farmerProdSeason").val();
	var state = $("#stateFilter").val();
	var stapleLen = $("#stapleFilter").val();
	jQuery
			.post(
					"dashboard_populateFarmerProdDetailsBarChartData.action",
					{
						selectedSeason : season,
						selectedBranch : $("#farmerProdDetailFilter").val(),
						selectedState : state,
						selectedStapleLen : stapleLen

					},

					function(result) {
						var jsonFarmerData = jQuery.parseJSON(result);
						if (result == "" || result == null || result == "[]") {
							$("#productInfoTbl1")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ jsonFarmerData.Label
													+ "</b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							farmerProdDetailsBarChart(jsonFarmerData.label,
									jsonFarmerData.farmerCount,
									jsonFarmerData.totalProposedPlantArea,
									jsonFarmerData.totalEstimatedYield,
									jsonFarmerData.totQty,
									jsonFarmerData.totalYield);
						}
					});
}

function isEmpty(val) {
	if (val == null || val == undefined || val.toString().trim() == "") {
		return true;
	}
	return false;
}

function farmerProdDetailsBarChart(label, farmerCount, totalAcre, estYield,
		totQty, totYield) {
	jQuery("#totalFarmer").text(farmerCount);
	jQuery("#totalAcres").text(totalAcre);
	jQuery("#estYield").text(estYield);
	jQuery("#totQty").text(totQty);
	jQuery("#totYield").text(totYield);

	/*
	 * var tbodyRow = ""; jQuery('#productInfoTbl1 > tbody').html('');
	 * 
	 * $.each(jsonFarmerData, function(index, value) { if
	 * (!isEmpty(value.values)) { tbodyRow += '<tr bgcolor="#E7E7E7">' + '<td style="text-align:left;">' +
	 * value.item + '</td>' + '<td style="text-align:left;">' + value.values + '</td>' + '</tr>'; }
	 * });
	 * 
	 * jQuery('#productInfoTbl1 > tbody').html(tbodyRow);
	 */
}

function procurementDetailsChart() {
	var selectedProduct = jQuery("#productsList").val();
	var dateRange="";
	var tenantId = getCurrentTenantId();
	if(tenantId!='livelihood')
		{
	if (!isEmpty(jQuery("#reportrange").val())) {
		var rangeSplit = jQuery("#reportrange").val().split("-");
		dateRange = dateFormatTransform(rangeSplit[0]) + "-"
				+ dateFormatTransform(rangeSplit[1]);
	}
		}

	jQuery
			.post(
					"dashboard_populateProcurementDeatilsBarChart.action",
					{
						selectedProduct : selectedProduct,
						dateRange : dateRange
					},
					function(result) {
						var label = extractLabelValue(result, "label");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#procurementDetailsBarChart")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							procurementDetailsBarChart(jsonFarmerData, label);
						}
					});
}

function dateFormatTransform(date) {
	var d = new Date(date);
	return (twoDigitFormat(d.getMonth() + 1) + '/'
			+ twoDigitFormat(d.getDate()) + '/' + d.getFullYear());
}

function twoDigitFormat(number) {
	var myNumber = number;
	var formattedNumber = ("0" + myNumber).slice(-2);
	return (formattedNumber);
}

function procurementDetailsBarChart(jsonFarmerData, label) {

	esecharts
			.chart(
					'procurementDetailsBarChart',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						/*
						 * subtitle: { text: 'Click the columns to view
						 * versions. Source: <a
						 * href="http://netmarketshare.com">netmarketshare.com</a>.' },
						 */
						xAxis : {
							type : 'category'
						},
						yAxis : [ {
							type : 'value'
						} ],
						legend : {
							enabled : false
						},
						plotOptions : {
							series : {
								borderWidth : 1,
								dataLabels : {
									enabled : true
								/* format: '{point.y:.1f}%' */
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span style="color:{series.color}">{point.name}</span>: <b>{point.y}</b><br/>'
						},

						series : [ {
							name : 'Farmer Details',
							// colorByPoint: true,

							data : (function() {
								var res = [];
								var len = 0;
								$.each(jsonFarmerData, function(index, value) {

									if (value.values !== undefined) {
										res.push({
											value : value.item,
											color : getRandomColor(),
											name : value.item,
											y : value.values,

										});
									}
								});
								return res;
							})()
						} ]

					});
}

function barSeedType(jsonAcreData, label) {

	var barChartFarmer = echarts.init(document
			.getElementById('doughnutSeedTypez'));
	var option = {
		title : {
			text : label,
		},
		backgroundColor : new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [ {
			offset : 0,
			color : '#fbfcfe'
		}, {
			offset : 1,
			color : '#eceff5'
		} ]),
		tooltip : {
			trigger : 'item',
			formatter : "{c} ({d}%)",
			// show: true, //default true
			showDelay : 0,
			hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : 'black',
				decoration : 'none',
				fontFamily : 'Verdana, sans-serif',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},
		/*
		 * legend : { orient : 'horizontal', x : 'left', y : 'bottom', data :
		 * (function() { var res = []; var len = 0; $.each(jsonAcreData,
		 * function(index, value) { if (value.group !== undefined) res.push({
		 * name : value.group, }); }); return res; })(), textStyle : {
		 * fontFamily : 'Arial, Verdana', fontSize : 13, fontWeight : 'bolder', }, },
		 */
		xAxis : [ {
			type : 'category',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonAcreData, function(index, value) {
					if (value.group !== undefined) {
						res.push({

							value : value.group
						});
					} else {
						value: 0
					}
				});
				return res;
			})(),
			axisLabel : {
				show : true,
				interval : '0',
				margin : 1,
				rotate : 25,
				formatter : '{value}',
				textStyle : {
					color : 'black',
					fontStyle : 'normal',
					fontWeight : 'bolder',
					interval : 'auto',
				// fontStyle:'justify'
				}
			},
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : 'blue'
			},
		},
		color : [ "#07eadb" ],
		toolbox : {
			show : true,
			title : 'Save',
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		series : [ {
			name : label,
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : false
					},
					labelLine : {
						show : false
					}
				},
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(jsonAcreData, function(index, value) {
					if (value.group !== undefined)
						res.push({
							name : value.group,
							value : value.count,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										formatter : '{c} ',
										textStyle : {
											color : 'black',
											fontWeight : 'bolder',
											fontSize : 16,
										}
									}
								}
							}
						});
				});
				return res;
			})()
		} ]
	};
	barChartFarmer.setOption(option);

}

function barChartSeedSource(jsonFarmerData, label) {
	var color = [ '#AD7A5F', '#A15C4C', '#691F20' ];
	esecharts
			.chart(
					'doughnutSeedSource',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						/*
						 * subtitle: { text: 'Click the columns to view
						 * versions. Source: <a
						 * href="http://netmarketshare.com">netmarketshare.com</a>.' },
						 */
						xAxis : {
							type : 'category'
						},
						yAxis : [ {
							type : 'value'
						} ],
						legend : {
							enabled : false
						},
						plotOptions : {
							series : {
								borderWidth : 1,
								dataLabels : {
									enabled : true
								/* format: '{point.y:.1f}%' */
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span style="color:{getRandomColor()}">{point.name}</span>: <b>{point.y}</b><br/>'
						},

						series : [ {
							name : label,
							// colorByPoint: getRandomColor(),
							// color:getRandomColor(),
							data : (function() {
								var res = [];
								var len = 0;
								var count = 0;
								$.each(jsonFarmerData, function(index, value) {

									if (value.group !== undefined) {
										res.push({
											name : value.group,
											y : value.count,
											color : color[count++],

										// drilldown:value.group.replace("
										// ","_")
										});
									}
								});
								return res;
							})()
						} ]

					});

}

function barChartsStock(label) {

	var color = [ '#BECEE4', '#98B4DB', '#5E82BB', '#5873AE', '#005D92',
			'#005499' ];
	esecharts
			.chart(
					'doughnutChartStock',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : ''
						},
						/*
						 * subtitle: { text: 'Click the columns to view
						 * versions. Source: <a
						 * href="http://netmarketshare.com">netmarketshare.com</a>.' },
						 */
						xAxis : {
							type : 'category'
						},
						yAxis : [ {
							type : 'value'
						} ],
						legend : {
							enabled : false
						},
						plotOptions : {
							series : {
								borderWidth : 1,
								dataLabels : {
									enabled : true,
									format: '{point.y} Kgs' 
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span style="color:{getRandomColor()}">{point.name}</span>: <b>{point.y}</b> Kgs<br/>'
						},

						series : [ {
							name : label,
							// colorByPoint: getRandomColor(),
							// color:getRandomColor(),
							data : (function() {
								var res = [];
								var len = 0;
								var count = 0;
								$.each(jsonData, function(index, value) {

									if (value.name !== undefined) {
										res.push({
											value : value.stock,
											name : value.name,
											y : value.stock,
											color : color[count++],

										// drilldown:value.group.replace("
										// ","_")
										});
									}
								});
								return res;
							})()
						} ]

					});
}

function loadKPFGroupTxnChart(val) {
	$("#groupFilter").val($("#groupFilter option:first").val());
	jQuery.post("dashboard_populateKPFTxnChartData.action", {
		selectedYear : val
	}, function(result) {
		var jsonTxnData = jQuery.parseJSON(result);
		var labelData = "";
		var label = "";
		var procurementLabel = "";
		var procurementData = "";
		var enrollmentLabel = "";
		var enrollmentData = "";
		var procurementQtyLabel = "";
		var procurementQtyData = "";
		var procurementGroupData = "";
		$.each(jsonTxnData, function(index, value) {
			if (index == 0) {
				procurementData = value;
			}
			if (index == 1) {
				procurementQtyData = value;
			}
			if (index == 2) {
				procurementGroupData = value;
			} else if (index == 3) {
				labelData = value;
				var json = JSON.stringify(labelData);
				var jsonArr = $.parseJSON(json);
				$.each(jsonArr, function(index, value) {
					label = value.Label;
					procurementLabel = value.procurementLabel;
					procurementQtyLabel = value.procurementQtyLabel;
					// enrollmentLabel = value.enrollmentLabel;
				});
			}
		});
		txnKPFCharts(label, procurementLabel, procurementData,
				procurementQtyData, procurementQtyLabel, procurementGroupData);
	});
}

function txnKPFCharts(label, procurementAmtLabel, procurementAmtData,
		procurementQtyData, procurementQtyLabel, procurementGroupData) {
	// var GroupChart = echarts.init(document.getElementById('groupChart'));
	var valueAmtArr = new Array();
	var monthsArray = new Array();
	var seriesAmtArray = new Array();
	var seriesQtyArray = new Array();
	var monthsFullNameArray = new Array();
	monthsArray.push("");
	monthsArray.push("JAN ");
	monthsArray.push("FEB");
	monthsArray.push("MAR");
	monthsArray.push("APR");
	monthsArray.push("MAY");
	monthsArray.push("JUN");
	monthsArray.push("JUL");
	monthsArray.push("AUG");
	monthsArray.push("SEP");
	monthsArray.push("OCT");
	monthsArray.push("NOV");
	monthsArray.push("DEC");

	monthsFullNameArray.push("");
	monthsFullNameArray.push("January");
	monthsFullNameArray.push("February");
	monthsFullNameArray.push("March");
	monthsFullNameArray.push("April");
	monthsFullNameArray.push("May");
	monthsFullNameArray.push("June");
	monthsFullNameArray.push("July");
	monthsFullNameArray.push("August");
	monthsFullNameArray.push("September");
	monthsFullNameArray.push("October");
	monthsFullNameArray.push("November");
	monthsFullNameArray.push("December");

	$.each(procurementGroupData, function(index, value) {
		var bFlag = true;
		var bQtyFlag = true;
		var valueAmtArr = new Array();
		var valueQtyArr = new Array();

		for (var i = 0; i < seriesAmtArray.length; i++) {
			if (seriesAmtArray[i].id == value.month) {
				// alert("-aaa-"+value.month)
				bQtyFlag = false;
			}
		}

		if (bQtyFlag) {
			// alert("-bbb-"+value.month)
			valueQtyArr.push([ value.fpo, value.qty ]);
			seriesAmtArray.push({
				id : value.month,
				data : valueQtyArr,
				name : monthsFullNameArray[value.month] + " [Quantity (Kgs)",
			});
		} else {
			for (var i = 0; i < seriesAmtArray.length; i++) {
				if (seriesAmtArray[i].id == value.month) {
					// / alert("-ccc-"+value.month)
					valueQtyArr = seriesAmtArray[i].data;
					valueQtyArr.push([ value.fpo, value.qty ]);
					seriesAmtArray[i].data = valueQtyArr;
				}
			}
		}
		for (var i = 0; i < seriesAmtArray.length; i++) {
			if (seriesAmtArray[i].name == monthsArray[value.month]) {
				bFlag = false;
			}
		}

		if (bFlag) {
			valueAmtArr.push([ value.fpo, value.amt ]);
			seriesAmtArray.push({
				name : monthsArray[value.month],
				id : monthsArray[value.month],
				data : valueAmtArr
			});
		} else {
			for (var i = 0; i < seriesAmtArray.length; i++) {
				if (seriesAmtArray[i].name == monthsArray[value.month]) {
					valueAmtArr = seriesAmtArray[i].data;
					valueAmtArr.push([ value.fpo, value.amt ]);
					seriesAmtArray[i].data = valueAmtArr;
				}
			}
		}
	});

	esecharts.chart('groupChart', {
		chart : {
			type : 'column'
		},
		title : {
			text : ''
		},
		xAxis : {
			type : 'category',

		// categories:[ 'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL',
		// 'AUG','SEP', 'OCT', 'NOV', 'DEC' ]
		},
		yAxis : {
			min : 0,
			type : 'value',
		/*
		 * title: { text: 'Total fruit consumption' },
		 */
		/*
		 * stackLabels: { style: { fontWeight: 'bold', color: (esecharts.theme &&
		 * esecharts.theme.textColor) || 'gray' } }
		 */
		},
		/*
		 * legend: { align: 'right', x: -70, verticalAlign: 'top', y: -10,
		 * floating: true, backgroundColor: (esecharts.theme &&
		 * esecharts.theme.background2) || 'white', borderColor: '#CCC',
		 * borderWidth: 1, shadow: false },
		 */

		/*
		 * tooltip: { headerFormat: '<b>{point.x}</b><br/>', pointFormat:
		 * '{series.name}: {point.y}<br/>Total: {point.stackTotal}' },
		 */
		plotOptions : {
			column : {
				stacking : 'normal'
			/*
			 * dataLabels: { enabled: true, color: (esecharts.theme &&
			 * esecharts.theme.dataLabelsColor) || 'white' }
			 */
			}
		},
		series : [ {
			name : procurementAmtLabel,
			data : (function() {
				var res = [];
				var len = 0;
				$.each(procurementAmtData, function(index, value) {

					if (!isEmpty(value.amt)) {
						res.push({
							// value : value.amt,
							name : monthsArray[value.month],
							y : value.amt,
							// color : getRandomColor(),
							drilldown : monthsArray[value.month]
						});
					} else {
						res.push({
							value : ''
						});
					}
				});
				return res;
			})()

		}, {
			name : procurementQtyLabel,
			data : (function() {
				var res = [];
				var len = 0;
				$.each(procurementQtyData, function(index, value) {
					if (!isEmpty(value.qty)) {
						res.push({
							name : monthsArray[value.month],
							y : value.qty,
							drilldown : value.month,
							id : value.month
						});
					} else {
						res.push({
							value : ''
						});
					}
				});
				return res;
			})()
		}, ],

		drilldown : {
			series : seriesAmtArray
		}
	});

}

function populateSowingHarvest() {
	var season = jQuery("#farmerSowingHarvstSeason").val();
	var org = jQuery("#branchFilter").val();
	var gender = jQuery("#sowingHavstGenderFilter").val();
	var state = jQuery("#sowingHavstStateFilter").val();
	var icsFarmerBranchData = "";
	if (org != '' && org !== undefined) {
		$("#sowingheader").text("Branch: " + org);
	}

	jQuery
			.post(
					"dashboard_populateSowingHavstBarChart.action",
					{
						selectedBranch : org,
						selectedSeason : season,
						selectedState : state,
						selectedGender : gender
					},
					function(result) {
						var label = extractLabelValue(result, "label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#sowingHarvest")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {

							var jsonSowingHavstData = jQuery.parseJSON(result);
							sowingHarvestBarChart(jsonSowingHavstData, label);
						}
					});
	if (getCurrentBranch() == '') {
		listState(org);

	}

}

function listState(obj) {
	jQuery.post("dashboard_populateStateList.action", {
		id : obj.value,
		dt : new Date(),
		selectedBranch : obj
	}, function(result) {
		$(".stateName").empty();
		insertOptionsByClass1('stateName', JSON.parse(result));
	});
}

function insertOptionsByClass1(ctrlName, jsonArr) {
	$("." + ctrlName).append($('<option>', {
		value : "",
		text : 'Select State'
	}));

	$.each(jsonArr, function(i, value) {
		$("." + ctrlName).append

		$("." + ctrlName).append($('<option>', {
			value : value.id,
			text : value.name
		}));
	});

	$(".select2").select2();
}
function sowingHarvestBarChart(sowingHarvestData, label) {
	var color = [ '#C9DE5F', '#89AC38', '#7A993C', '#67842D' ];
	var BarChart = echarts.init(document.getElementById('sowingHarvest'));
	$("#sowingTitle").text(label);
	var option = {
		/*
		 * title : { text : label },
		 */

		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Open Sans',
				fontSize : 12,

			},
		},
		animation : true,
		toolbox : {
			show : true,
			tooltip : {
				type : "1",
			},
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(sowingHarvestData, function(index, value) {
					if (value.item !== undefined)
						res.push({
							value : value.item
						});

				});
				return res;
			})(),
			/*
			 * axisLine:{ show:false }, axisTick: { show:false },
			 */
			splitLine : {
				show : false
			},
			axisLabel : {
				show : true,
				interval : '0',
				margin : 6,
				rotate : 0,
				formatter : '{value}',
				textStyle : {
					color : '#111',
					fontFamily : 'roboto',
					fontSize : 14,
				// fontStyle: 'bold'
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			// axisLine:{show:false}
			splitLine : {
				show : false
			},
		} ],
		series : [ {
			name : label,
			type : 'bar',
			data : (function() {
				var res = [];
				var len = 0;

				$.each(sowingHarvestData, function(index, value) {
					if (value.values !== undefined)
						res.push({
							// value:value.values > 999 ?
							// (value.values/1000).toFixed(1) + 'k' :
							// value.values,
							value : value.values,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#333',
											fontFamily : 'roboto',
											fontSize : 16,
										// fontStyle: 'bold'
										}
									}
								}
							}
						});

				});

				for (i = 0; i < res.length; i++) {
					res[i].itemStyle.normal.color = color[i];
				}

				return res;
			})(),
		/*
		 * markLine: { data: [{ type: 'average', name: 'average value' }] }
		 */
		},

		]
	};
	BarChart.setOption(option);
}

function populateAreaProdByOrg() {

	var season = jQuery("#areaProdByOrgSeasonFilter").val();
	var state = jQuery("#areaProdStateFilter").val();
	var gender = jQuery("#areaProdGenderFilter").val();
	jQuery
			.post(
					"dashboard_populateAreaProdByOrgBarChart.action",
					{
						selectedSeason : season,
						selectedState : state,
						selectedGender : gender

					},
					function(result) {
						var label = extractLabelValue(result, "label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#areaUnderProdByOrg")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var areaProdData = jQuery.parseJSON(result);
							areaUnderProdByOrg(areaProdData, label);
						}
					});

}

function areaUnderProdByOrg(areaProdData, label) {
	var color = [ '#AD7A5F', '#A15C4C', '#691F20' ];
	var BarChart = echarts.init(document.getElementById('areaUnderProdByOrg'));
	$("#areaProdTitle").text(label);
	var option = {

		/*
		 * title : { text : label },
		 */

		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				color : 'black',
				// decoration : 'none',
				fontFamily : 'Merienda One',
				fontSize : 12,
			// fontStyle: 'normal'
			}
		},
		animation : true,
		toolbox : {
			show : true,
			tooltip : {
				type : "1",
			},
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(areaProdData, function(index, value) {
					if (value.item !== undefined)
						res.push({
							value : value.item
						});

				});
				return res;
			})(),
			splitLine : {
				show : false
			},
			axisLabel : {
				show : true,
				interval : '0',
				margin : 6,
				rotate : 0,
				formatter : '{value}',
				textStyle : {
					color : '#333',
					fontFamily : 'roboto',
					fontSize : 14,
				// fontStyle: 'bold'
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			splitLine : {
				show : false
			},
		} ],
		series : [ {
			name : label,
			type : 'bar',
			data : (function() {
				var res = [];
				var len = 0;

				$.each(areaProdData, function(index, value) {
					if (value.values !== undefined)
						res.push({
							value : value.values,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										textStyle : {
											color : '#333',
											fontFamily : 'roboto',
											fontSize : 16,
										// fontStyle: 'bold'
										}
									}
								}
							}
						});

				});

				for (i = 0; i < res.length; i++) {
					res[i].itemStyle.normal.color = color[i];
				}

				return res;
			})(),
		/*
		 * markLine: { data: [{ type: 'average', name: 'average value' }] }
		 */
		},

		]
	};
	BarChart.setOption(option);
}

function populateAreaProdByIcs() {
	var season = jQuery("#areaProdSeasonFilter").val();
	jQuery
			.post(
					"dashboard_populateAreaProdByIcsBarChart.action",
					{
						selectedSeason : season
					},
					function(result) {
						var label = extractLabelValue(result, "label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");

						if (result == "" || result == null || result == "[{}]") {
							$("#areaUnderProdByIcs")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:50%;text-align:center'>No Data Available.</h4>");
						} else {
							var areaProdIcsData = jQuery.parseJSON(result);
							areaUnderProdByIcs(areaProdIcsData, label);
						}
					});
}

function areaUnderProdByIcs(areaProdIcsData, label) {

	var BarChart = echarts.init(document.getElementById('areaUnderProdByIcs'));
	var option = {

		title : {
			text : label
		},

		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				fontFamily : 'Arial, Verdana',
				fontSize : 16,
				fontWeight : 'bolder',
				color : [ getRandomColor() ],
			},
		},
		animation : true,
		color : [ getRandomColor() ],
		toolbox : {
			show : true,
			tooltip : {
				type : "1",
			},
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',

			data : (function() {
				var res = [];
				var len = 0;

				$.each(areaProdIcsData, function(index, value) {
					if (value.item !== undefined)
						res.push({
							value : value.item
						});

				});
				return res;
			})(),
			splitLine : {
				show : true
			},
			axisLabel : {
				show : true,
				interval : '0',
				margin : 2,
				rotate : 0,
				formatter : '{value}',
				textStyle : {
					color : 'black',
					fontStyle : 'normal',
					fontWeight : 'bolder',
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			splitLine : {
				show : false
			},

		} ],
		series : [ {
			name : label,
			type : 'bar',
			data : (function() {
				var res = [];
				var len = 0;

				$.each(areaProdIcsData, function(index, value) {
					if (value.values !== undefined)
						res.push({
							value : value.values,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										textStyle : {
											color : 'black',
											fontWeight : 'bolder',
											fontSize : 14,
										}
									}
								}
							}
						});

				});

				for (i = 0; i < res.length; i++) {
					res[i].itemStyle.normal.color = getRandomColor();
				}

				return res;
			})(),
		/*
		 * markLine: { data: [{ type: 'average', name: 'average value' }] }
		 */
		},

		]
	};
	BarChart.setOption(option);
}

function populateGinnerQtySold() {
	var season = jQuery("#ginnerQtySeasonFilter").val();
	var branch = jQuery("#ginnerQtyBranchFilter").val();

	jQuery
			.post(
					"dashboard_populateGinnerQtyBarChart.action",
					{
						selectedSeason : season,
						selectedBranch : branch
					},
					function(result) {
						var label = extractLabelValue(result, "label");

						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[]") {
							$(".hideNoDataGinner").hide();
							$(".showNoDataGinner").show();
							$("#ginnerTitle").text('Ginner Quantity Sold');
							$("#ginnerInfo")
									.html(
											"<p style='font-family:roboto;font-size:18px;margin-top:0%'><b>"
													+ "</b></p><h4 style='margin-top:20%;text-align:center;color:#333'>No Data Available.</h4>");

						} else {
							var ginnerQtyData = jQuery.parseJSON(result);
							ginnerQtyDataSold(ginnerQtyData, label);
							$(".hideNoDataGinner").show();
							$(".showNoDataGinner").hide();
						}
					});
}

function ginnerQtyDataSold(ginnerQtyData, label) {
	$("#ginnerTitle").text(label);
	var tbodyRow = "";
	var sno = 0;
	jQuery('#ginnerInfoTbl > tbody').html('');
	$.each(ginnerQtyData, function(index, value) {
		if (value.values !== undefined) {
			++sno;
			tbodyRow += '<tr class="">';
			tbodyRow += '<td  align="center">' + sno + '</td>';
			if (getCurrentBranch() == '') {
				tbodyRow += '<td  align="center">' + value.branchId + '</td>';

			}
			tbodyRow += '<td>' + value.item + '</td>';
			tbodyRow += '<td  align="center">' + value.values + '</td>';

			tbodyRow += '<td><p class="addressTD">' + value.address
					+ '</p></td>';
		}
	});
	$('#ginnerInfoTbl').html(tbodyRow);

	/*
	 * var color = [ '#F3950D', '#E62163', '#4E74C0' ]; var BarChart =
	 * echarts.init(document.getElementById('ginnerQtySold'));
	 * $("#ginnerTitle").text(label); var option = { - title : { text : label },
	 * 
	 * noDataLoadingOption : { text : label + "\n" + noData, effect : 'bar',
	 * textStyle : { fontFamily : 'Arial, Verdana', fontSize : 16, fontWeight :
	 * 'bolder', color : 'blue' }, }, animation : true, toolbox : { show : true,
	 * tooltip : { type : "1", }, feature : { magicType : { show : true, type : [
	 * 'line', 'bar' ], title : '' }, restore : { show : true, title : Refresh, },
	 * saveAsImage : { show : true, title : save, } } }, calculable : true,
	 * xAxis : [ { type : 'category', data : (function() { var res = []; var len =
	 * 0; $.each(ginnerQtyData, function(index, value) { if (value.item !==
	 * undefined) res.push({ value : value.item });
	 * 
	 * }); return res; })(), splitLine: {show: false}, axisLabel : { show :
	 * true, interval : '0', margin : 2, rotate : 0, formatter : '{value}',
	 * textStyle : { color : 'black', fontStyle : 'normal', fontWeight :
	 * 'bolder', } }, } ], yAxis : [ { type : 'value', splitLine: {show: false},
	 * axisLabel : {rotate : 30} } ], series : [ { name : label, type : 'bar',
	 * data : (function() { var res = []; var len = 0;
	 * 
	 * $.each(ginnerQtyData, function(index, value) { if (value.values !==
	 * undefined) res.push({ value : value.values, itemStyle : { normal : {
	 * label : { show : true, position : 'top', textStyle : { color : 'black',
	 * fontWeight : 'bolder', fontSize : 14, } } } } });
	 * 
	 * });
	 * 
	 * for (i = 0; i < res.length; i++) { res[i].itemStyle.normal.color =
	 * color[i]; }
	 * 
	 * return res; })(),
	 * 
	 * markLine: { data: [{ type: 'average', name: 'average value' }] } }, ] };
	 * BarChart.setOption(option);
	 */
}

function populateGMOCharts() {
	var season = jQuery("#gmoSeasonFilter").val();
	var branch = jQuery("#gmoBranchFilter").val();
	$("#selectedBranchGmo").val(branch);
	if (branch != '' && branch !== undefined) {
		$("#gmoheader").text("Branch: " + branch);

	}

	jQuery
			.post(
					"dashboard_populateGMOBarChart.action",
					{
						selectedSeason : season,
						selectedBranch : branch
					},
					function(result) {
						var label = extractLabelValue(result, "label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[]") {

							$("#gmoTitle").text('GMO Contamination Percentage');
							$("#gmoBarCharts")
									.html(
											"<p style='font-family:roboto;font-size:18px;margin-top:0%'><b>"
													+ '<s:property value="%{getLocaleProperty("gmoBarCharts")}" />'
													+ "</b></p><h4 style='margin-top:20%;text-align:center;color:#333'>No Data Available.</h4>");
						} else {

							var gmoData = jQuery.parseJSON(result);
							gmoDatas(gmoData, label);

						}
					});
}

function gmoDatas(gmoData, label) {
	var BarChart = echarts.init(document.getElementById('gmoBarCharts'));
	$("#gmoTitle").text(label);

	var color = [ '#D08949', '#AC7240', '#7F5C34' ];
	var option = {

		/*
		 * title : { text : label },
		 */

		tooltip : {
			trigger : 'axis'
		},

		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',

			textStyle : {
				color : '#333',
				fontFamily : 'roboto',
				fontSize : 14,
			// fontStyle: 'bold'
			},
		},

		toolbox : {
			show : true,
			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		grid : {
			y : 50,
			y2 : 30,
			x2 : 20
		},
		xAxis : [ {
			type : 'category',
			splitLine : {
				show : false
			},
			axisLabel : {
				textStyle : {
					color : '#333',
					fontFamily : 'roboto',
					fontSize : 14,

				}
			},
			data : (function() {
				var res = [];
				var len = 0;
				$.each(gmoData, function(index, value) {
					if (value.item !== undefined)
						res.push({
							value : value.item
						});

				});
				return res;
			})(),
		/* data : [ 'Raw Cotton', 'Leaf', 'Seed' ] */
		}, {
			type : 'category',
			splitLine : {
				show : false
			},
			axisLine : {
				show : false
			},

			data : [],

		/* data : [ 'Raw Cotton', 'Leaf', 'Seed' ] */
		} ],
		yAxis : [ {
			type : 'value',
			splitLine : {
				show : false
			},
			axisLabel : {

			}
		} ],
		series : [ {
			name : 'GMO Percentage',
			type : 'bar',
			itemStyle : {
				normal : {
					color : '#FF0',
					label : {
						show : true,
						formatter : function(p) {
							if (p.value !== undefined) {
								return p.value > 0 ? (p.value + ' %') : '';
							} else {
								return 0;
							}
						}
					}
				}
			},
			data : (function() {
				var res = [];
				var len = 0;

				$.each(gmoData, function(index, value) {
					if (value.values !== undefined)
						res.push({
							value : value.values,
							itemStyle : {
								normal : {
									label : {
										show : true,
										position : 'top',
										textStyle : {
											color : 'black',
											fontFamily : 'roboto',
											fontSize : 16,
										// fontStyle: 'bold'
										}
									}
								}
							}
						});

				});

				for (i = 0; i < res.length; i++) {
					res[i].itemStyle.normal.color = color[i];
				}

				return res;
			})(),
		},

		{
			name : 'GMO Percentage',
			type : 'bar',
			xAxisIndex : 1,
			itemStyle : {
				normal : {
					color : 'rgba(185,185,185,0.5)',
				/*
				 * label : { show : true, formatter : function(p) { return
				 * p.value > 0 ? (p.value) : ''; } }
				 */
				}
			},
			data : (function() {
				var res = [];
				var len = 0;

				$.each(gmoData, function(index, value) {
					if (value.percentage !== undefined)
						res.push({
							value : value.percentage,

						});

				});

				/*
				 * for (i = 0; i < res.length; i++) {
				 * res[i].itemStyle.normal.color = color[i]; }
				 */
				return res;
			})(),
		}

		]
	};
	BarChart.setOption(option);
}

function populateCottonPriceCharts() {
	var season = jQuery("#cPriceSeasonFilter").val();
	var staple = jQuery("#stapleLenFilter").val();
	var stapleText = $("#stapleLenFilter option:selected").text();
	if (staple != '') {
		$("#cottonPriceheader").text("Staple Length: " + stapleText);

	}
	jQuery
			.post(
					"dashboard_populateCottonPriceBarChart.action",
					{
						selectedSeason : season,
						selectedStapleLen : staple
					},
					function(result) {
						var label = extractLabelValue(result, "label");
						save = extractLabelValue(result, "save");
						Refresh = extractLabelValue(result, "refresh");
						result = findAndRemove(result, "id", "Label");
						if (result == "" || result == null || result == "[]") {

							$("#cPriceTitle").text('Cotton Sale Price');
							$("#CottonPriceBarCharts")
									.html(
											"<p style='font-family:roboto;font-size:18px;margin-top:0%'><b>"
													+ '<s:property value="%{getLocaleProperty("gmoBarCharts")}" />'
													+ "</b></p><h4 style='margin-top:20%;text-align:center;color:#333'>No Data Available.</h4>");
						} else {
							var cottonPriceData = jQuery.parseJSON(result);
							cottonPriceDatas(cottonPriceData, label);

						}
					});
}

function cottonPriceDatas(cottonPriceData, label) {
	var color = [ '#AEAB7E', '#AEAC7C', '#727141' ];
	var BarChart = echarts
			.init(document.getElementById('CottonPriceBarCharts'));
	$("#cPriceTitle").text(label);
	var option = {

		/*
		 * title : { text : label },
		 */
		tooltip : {
			trigger : 'item',
			formatter : "{c} (INR/Kg)",
			// show: true, //default true
			// showDelay : 0,
			// hideDelay : 50,
			transitionDuration : 1,
			backgroundColor : '#F7F7F7',
			borderColor : '#F7F7F7',
			borderRadius : 8,
			borderWidth : 2,
			padding : 10, // [5, 10, 15, 20]
			position : function(p) {
				return [ p[0] + 5, p[1] - 10 ];
			},
			textStyle : {
				color : '#333',
				decoration : 'none',
				fontFamily : 'roboto',
				fontSize : 15,
				fontStyle : 'italic',
				fontWeight : 'bold'
			}
		/*
		 * formatter: function (params,ticket,callback) { console.log(params)
		 * var res = "";//params[0].name; for (var i = 0, l = params.length; i <
		 * l; i++) { res +=params[i].value; } setTimeout(function (){
		 * callback(ticket, res); }, 1000) return 'loading'; }
		 */
		},

		noDataLoadingOption : {
			text : label + "\n" + noData,
			effect : 'bar',
			textStyle : {
				color : 'black',
				// decoration : 'none',
				fontFamily : 'Merienda One',
				fontSize : 12,
			// fontStyle: 'normal'
			}
		},
		animation : true,
		toolbox : {
			show : true,

			feature : {
				magicType : {
					show : true,
					type : [ 'line', 'bar' ],
					title : ''
				},
				restore : {
					show : true,
					title : Refresh,
				},
				saveAsImage : {
					show : true,
					title : save,
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(cottonPriceData, function(index, value) {
					if (value.item !== undefined)
						res.push({
							value : value.item
						});

				});
				return res;
			})(),
			splitLine : {
				show : false
			},
			axisLabel : {
				show : true,
				interval : '0',
				margin : 6,
				rotate : 0,
				formatter : '{value}',
				textStyle : {
					color : '#333',
					fontFamily : 'roboto',
					fontSize : 14,
				// fontStyle: 'bold'
				}
			},
		} ],
		yAxis : [ {
			type : 'value',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(cottonPriceData, function(index, value) {
					if (value.msp !== undefined)
						res.push({
							value : value.msp
						});

				});
				return res;
			})(),
			splitLine : {
				show : false
			},
		} ],
		series : [
				{
					name : label,
					type : 'line',
					data : (function() {
						var res = [];
						var len = 0;
						var arrLen = 0;
						$.each(cottonPriceData, function(index, value) {
							if (value.msp !== undefined) {
								res.push({
									value : value.msp,
									itemStyle : {
										normal : {
											label : {
												show : false,
												position : '0',
												formatter : "MSP",
												textStyle : {
													color : '#333',
													fontFamily : 'roboto',
													fontSize : 16,
												// fontStyle: 'bold'
												}
											}
										}
									}

								});

							}

						});
						return res;
					})(),
					markLine : {
						data : [ {
							type : 'min',
							name : 'average value',
							itemStyle : {
								normal : {

									label : {
										show : true,
										formatter : "{c}(INR/Kg)                                 ",
										textStyle : {
											color : '#333',
											fontFamily : 'roboto',
											fontSize : 15,
											align : 'center'
										}
									}
								}
							}
						} ]
					}
				},

				{
					name : label,
					type : 'bar',
					data : (function() {
						var res = [];
						var len = 0;
						$.each(cottonPriceData, function(index, value) {
							if (value.price !== undefined)
								res.push({
									value : value.price,
									itemStyle : {
										normal : {
											label : {
												show : true,
												formatter : "{c} (INR/Kg) ",
												position : 'top',
												textStyle : {
													color : '#333',
													fontFamily : 'roboto',
													fontSize : 15,
												// fontStyle: 'bold'
												}
											}
										}
									}
								});

						});
						for (i = 0; i < res.length; i++) {
							res[i].itemStyle.normal.color = color[i];
						}
						return res;
					})(),

				},

		]
	};
	BarChart.setOption(option);
}

function populateProcurementCharts() {
	var org;
	$("#farmerProcurementTitle").text("Procurement Transactions");
	var dateRange;
	if (!isEmpty(jQuery("#reportProcurementRange").val())) {
		var rangeSplit = jQuery("#reportProcurementRange").val().split("-");
		dateRange = dateFormatTransform(rangeSplit[0]) + "-"
				+ dateFormatTransform(rangeSplit[1]);
	}	
	
	var totQtyDatas = null;
	var totAmtDatas = null;
	var procurementDatas = null;
	var totPaymntDatas = null;
	var branch=$("#branchProcFilter").val();
	var state=$("#selectedStateProcFilter").val();
	var season=$("#filterProcSeason").val();
	if (!isEmpty(getCurrentBranch())) {
		branch=getCurrentBranch();
	}
	jQuery
			.post(
					"dashboard_populateProcurementCharts.action",
					{
						selectedBranch:branch,
						selectedState:state,
						selectedSeason:season,
						dateRange : dateRange
					},
					function(result) {
						var label = "Procurement";
						if (result == "" || result == null || result == "[]") {
							$("#procurementTxnChart")
									.html(
											"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
													+ label
													+ "</b></p><h4 style='margin-top:20%;text-align:center'>No Data Available.</h4>");
							
							$("#procurementProdPieChart")
							.html(
									"<p style='font-family:sans-serif;font-size:18px;margin-top:0%'><b>"
											
											+ "</b></p><h4 style='margin-top:20%;text-align:right'></h4>");
						} else {
							var jsonFarmerData = jQuery.parseJSON(result);
							$.each(jsonFarmerData, function(index, value) {

								if (index == 0) {
									procurementDatas = value;
								} else if (index == 1) {
									totQtyDatas = value;
								}

								else if (index == 2) {
									totAmtDatas = value;
								} else if (index == 3) {
									totPaymntDatas = value;
								}
							});
							loadProcurementCharts(procurementDatas,
									totQtyDatas, totAmtDatas, totPaymntDatas);
						}

					});

}

function loadCocExpDrillDwnCharts(data, seasonName) {
	esecharts
			.chart(
					'cocSegregate',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : 'Production Expense Details'
						},

						xAxis : {
							type : 'category'
						},
						yAxis : {
							title : {
								text : ''
							}

						},
						legend : {
							enabled : true
						},
						plotOptions : {
							series : {
								//borderWidth : 0,
								dataLabels : {
									enabled : true,
									format : '{point.y} <b>({point.units})</b>'
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span >{point.name}</span>: <b>{point.y}</b><b>({point.units})</b><br/>'
						},

						series : [ {
							name : seasonName,
							// colorByPoint: true,
							data : (function() {
								var res = [];
								var len = 0;
								$.each(data, function(index, value) {
									 if(seasonName==value.season){
									if (value.items !== undefined)
										res.push({
											name : value.items,
											y : value.values,
											units : 'INR'
										// color : getRandomColor(),
										// drilldown : value.season
										});
									 }
								});
								return res;
							})(),
						} ]
					});
	customBackButton();
}

function loadCocRevDrillDwnCharts(data, seasonName) {

	esecharts
			.chart(
					'cocSegregate',
					{
						chart : {
							type : 'column'
						},
						title : {
							text : 'Production Revenue Details'
						},

						xAxis : {
							type : 'category'
						},
						yAxis : {
							title : {
								text : ''
							}

						},
						legend : {
							enabled : true
							
						},
						plotOptions : {
							series : {
								borderWidth : 0,
								dataLabels : {
									enabled : true,
									format : '{point.y} <b>({point.units})</b>'
								}
							}
						},

						tooltip : {
							headerFormat : '<span style="font-size:11px">{series.name}</span><br>',
							pointFormat : '<span >{point.name}</span>: <b>{point.y}</b><b>({point.units})</b><br/>'
						},

						series : [ {
							name : seasonName,
							// colorByPoint: true,
							data : (function() {
								var res = [];
								var len = 0;
								$.each(data, function(index, value) {
									 if(seasonName==value.season){
									if (value.product !== undefined)
										res.push({
											name : value.product,
											y : value.values,
											color : '#5C5C61',
											units : 'INR'
										// drilldown : value.season
										});
									 }
								});
								return res;
							})(),
						} ]
					});
	customBackButton();

}

function customBackButton() {
	var chart = $('#cocSegregate').esecharts();
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
	var custombutton = chart.renderer.button(
			'Back',
			100,
			10,
			function() {
				loadCocSegrateValues(totCocExpDatas, totExpRevDatas,
						 revProdDatas);
			}, null, hoverState, pressedState).add();

}

function loadProcurementCharts(procurementDatas, totQtyDatas, totAmtDatas,
		totPaymntDatas) {
	
	esecharts
			.chart(
					'procurementTxnChart',
					{
						chart : {
							plotBackgroundColor : null,
							plotBorderWidth : null,
							plotShadow : false,
							type : 'pie'
						},
						title : {
							text : 'Procurement Transaction Details'
						},

						xAxis : {
							type : 'category'
						},
						yAxis : {
							title : {
								text : ''
							}

						},
						tooltip : {
							pointFormat : '{series.name}: <b>{point.units}</b><b>{point.y:.2f}</b> '
						},
						plotOptions : {
							pie : {
								allowPointSelect : true,
								cursor : 'pointer',
								dataLabels : {
									enabled : true,
									format : '<b>{point.name}</b>: {point.units}{point.y:.2f} ',
									style : {
										color : (esecharts.theme && esecharts.theme.contrastTextColor)
												|| 'black'
									}
								}
							}
						},
						series : [ {
							name : 'Procurment Transaction',
							colorByPoint : true,

							data : (function() {
								var res = [];
								var len = 0;
								
								$.each(procurementDatas,
										function(index, value) {
									var unit = "";
									if(index!=1){
										unit = value.units
									}
											if (value.values !== undefined)
												if(value.items != "Total Quantity"){
												res.push({
													name : value.items,
													id:value.items,
													y : value.values,
													units:unit,
													// color : getRandomColor(),
													drilldown : value.items

												});
												}
											
										});
								
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										var data = null; 
										var unit=null;
										if (this.name == 'Total Amount') {
											data = totAmtDatas;
											unit=currencyType;
										} else if (this.name == 'Total Quantity') {
											data = totQtyDatas;
											
										} else if (this.name == 'Total Payment') {
											data = totPaymntDatas;
											unit=currencyType;
										}
										loadProcurementBarCharts(data,this.name,unit);
										
									}
								}
							}

						} ],

					/*
					 * drilldown : {
					 * 
					 * series : [ { dataLabels : { enabled : true, format : '<b>{point.y}', },
					 * type : 'column', name : "Total Amount", id : "Total
					 * Amount", data : (function() { var res = []; var len = 0;
					 *  $ .each( totAmtDatas, function(index, value) { if
					 * (value.values !== undefined)
					 * 
					 * res .push({ name : value.productName, y : value.values, //
					 * color // : // getRandomColor(), });
					 * 
					 * }); return res; })(), },
					 *  { dataLabels : { enabled : true, format : '<b>{point.y}', },
					 * type : 'column', name : "Total Quantity", id : "Total
					 * Quantity", data : (function() { var res = []; var len =
					 * 0;
					 *  $ .each( totQtyDatas, function(index, value) { if
					 * (value.values !== undefined)
					 * 
					 * res .push({ name : value.productName, y : value.values, //
					 * color // : // getRandomColor(), });
					 * 
					 * }); return res; })(), }, { dataLabels : { enabled : true,
					 * format : '<b>{point.y}', }, type : 'column', name :
					 * "Total Payment", id : "Total Payment", data : (function() {
					 * var res = []; var len = 0;
					 *  $ .each( totPaymntDatas, function(index, value) { if
					 * (value.values !== undefined)
					 * 
					 * res .push({ name : value.productName, y : value.values, //
					 * color // : // getRandomColor(), });
					 * 
					 * }); return res; })(), }
					 *  ] }
					 */

					});
	loadProcurementBarCharts(totQtyDatas,'Total Quantity','kgs');
}

/*function loadProcurementBarCharts(data,seriesName)
{
	esecharts.chart('procurementProdPieChart', {
		chart : {
			plotBackgroundColor : null,
			plotBorderWidth : null,
			plotShadow : false,
			type : 'pie'
		},
	    title: {
	        text: 'Procurment Stock Details'
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.y} </b>'
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	            	format : '<b>{point.name}</b>: {point.y} Kgs'
	            },
	            showInLegend: false
	        }
	    },
	    series: [{
	        name: seriesName,
	        colorByPoint: true,
	    //    type: 'pie',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(data,
						function(index, value) {
							if (value.values !== undefined)

								res.push({
									name : value.productName,
									y : value.values,
									// color : getRandomColor(),
									//drilldown : value.items

								});

						});
				return res;
			})(),
	    }]
	});
}

*/
function loadProcurementBarCharts(data,seriesName,unit)
{
	esecharts.chart('procurementProdPieChart', {
		chart : {
			plotBackgroundColor : null,
			plotBorderWidth : null,
			plotShadow : false,
			type : 'pie'
		},
	    title: {
	        text: 'Procurment Stock Details'
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.y} </b>'
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	            	format : '<b>{point.name}</b>: {point.y}  '+unit
	            },
	            showInLegend: false
	        }
	    },
	    series: [{
	        name: seriesName,
	        colorByPoint: true,
	    //    type: 'pie',
			data : (function() {
				var res = [];
				var len = 0;
				$.each(data,
						function(index, value) {
					    var unit = "";
					    unit = value.units
							if (value.values !== undefined)
								
								res.push({
									name : value.productName,
									y : value.values,
									units:unit,
									// color : getRandomColor(),
									//drilldown : value.items

								});

						});
				return res;
			})(),
	    }]
	});
}

function farmerByLocationChart_bar(branch){
	
	/*var branch=$("#branchFilter").val();
	
	if (!isEmpty(getCurrentBranch())) {
		branch=getCurrentBranch();
	}
	
	branch = 'ICFA01';*/
	
	
	jQuery.post("farmer_populateFarmersByLocation.action",{selectedBranch:branch},function(result) {
		json = $.parseJSON(result);
		if(!isEmpty(json)){
		$.each(json, function(index, value) {
			renderFarmerByLocationChart(value.branch,value.country,value.state,value.locality,value.municipality,value.gramPanchayat,value.village,value.farmerDetails,value.getGramPanchayatEnable);
		});
	   }
	});
	
	
	
	
}	

function renderFarmerByLocationChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable){
	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	var locationTitleArray;
	if(isEmpty(getCurrentBranch())){
		locationTitleArray = "Farmer By Branch";
	}
	else{
	locationTitleArray="Farmer By Country" ;
	}
	
	//alert(tenantId);
	/*if(getGramPanchayatEnable == 0 && !isEmpty(getCurrentBranch())){
		if(tenantId!='wilmar' && tenantId!='fruitmaster'){
			locationTitleArray = ["Farmer By Country","Farmer By State","Farmer By District","Farmer By Taluk","Farmer By Village","Farmer Details"];	
		}else if(tenantId=='fruitmaster'){
			//alert("AAAA");
			locationTitleArray = ["Grower By Country","Grower By State","Grower By District","Grower By Tehsil","Grower By Village","Grower Details"];	
		}else{
			locationTitleArray = ["Farmer By Country","Farmer By Region","Farmer By Provinces","Farmer By Municipality","Farmer By Barangay","Farmer Details"];	
		}
		
	}else if(getGramPanchayatEnable == 1 && !isEmpty(getCurrentBranch())){
		if(tenantId!='wilmar' && tenantId!='fruitmaster'){
			locationTitleArray = ["Farmer By Country","Farmer By State","Farmer By District","Farmer By Taluk","Farmer By Village","Farmer Details"];	
		}else if(tenantId=='fruitmaster'){
			locationTitleArray = ["Grower By Country","Grower By State","Grower By District","Grower By Tehsil","Grower By Village","Grower Details"];	
		}else{
			locationTitleArray = ["Farmer By Country","Farmer By Region","Farmer By Provinces","Farmer By Municipality","Farmer By Gram panchayath","Farmer By Barangay","Farmer Details"];	
		}
	}else if(getGramPanchayatEnable == 0 && isEmpty(getCurrentBranch())){
		if(tenantId!='wilmar' && tenantId!='fruitmaster'){
			locationTitleArray = ["Farmer By Country","Farmer By State","Farmer By District","Farmer By Taluk","Farmer By Village","Farmer Details"];	
		}else if(tenantId=='fruitmaster'){
			locationTitleArray = ["Grower By Country","Grower By State","Grower By District","Grower By Tehsil","Grower By Village","Grower Details"];	
		}else{
			locationTitleArray = ["Farmer By Branch","Farmer By Country","Farmer By Region","Farmer By Provinces","Farmer By Municipality","Farmer By Barangay","Farmer Details"];	
		}
	}else if(getGramPanchayatEnable == 1 && isEmpty(getCurrentBranch())){
		if(tenantId!='wilmar' && tenantId!='fruitmaster'){
			locationTitleArray = ["Farmer By Country","Farmer By State","Farmer By District","Farmer By Taluk","Farmer By Village","Farmer Details"];	
		}else if(tenantId=='fruitmaster'){
			locationTitleArray = ["Grower By Country","Grower By State","Grower By District","Grower By Tehsil","Grower By Village","Grower Details"];	
		}else{
			locationTitleArray = ["Farmer By Branch","Farmer By Country","Farmer By Region","Farmer By Provinces","Farmer By Municipality","Farmer By Gram panchayath","Farmer By Barangay","Farmer Details"];	
		}
	}*/
	var unit = "Hectare";
	
	var titleForFarmAreaChart = locationTitleArray;
	titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	
	var chart = new	esecharts.chart('farmerByLocationChart_bar', {
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
						if(isEmpty(getCurrentBranch())){
							$.each(branch, function(index, branch) {
								//if(iteration1 <= 10){
									res.push({
										name :branch.branchName,
										y : branch.count,
										drilldown:branch.branchId,
										id : branch.branchId
									 });
									/*	iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}*/
							});
							farmerByLocationChart_pie("B",branchForPieChart,unit,titleForFarmAreaChart);
						}else{
							$.each(country, function(index, country) {
								//if(iteration1 <= 10){
									res.push({
											name :country.countryName,
											y : country.count,
											drilldown:country.countryCode,
											id : country.countryCode
										 });
										/*iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}*/
							});
							farmerByLocationChart_pie('first_level_Branch_Login',getCurrentBranch(),unit,titleForFarmAreaChart);
						}
						
						return res;
					})(),
						point : {
								events : {
									click : function(event) {
								
										if(isEmpty(getCurrentBranch())){
							
											populate_countryInLoction(this.id,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										}else{
										    populate_stateInLocation(this.id,getCurrentBranch(),country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,getCurrentBranch());
										}
									}
								}
							}
				 }],
		
		drilldown: {/*
						series:  (function() {
				    				
							var res = [];
							if(isEmpty(getCurrentBranch())){
									$.each(branch, function(index, branch) {
						    			res.push({	
						            		name:branch.branchName,
						            		id: branch.branchId,
								            data : (function() {
														var res2 = [];
														
														$.each(country, function(index, country) {
															
															
															if(branch.branchId == country.branchId){
																res2.push({
																	name :country.countryName,
																	y : country.count,
																	drilldown:country.countryCode,
																	id : country.countryCode
																});
															}
														});
													return res2;
											})()
										});
						    		});
							}
							
							
				    				$.each(country, function(index, country) {
						    			res.push({	
						            		name: country.countryName,
						            		id: country.countryCode,
								            data : (function() {
														var res2 = [];
														$.each(state, function(index, state) {
															if(country.countryCode == state.countryCode){
																res2.push({
																	name :state.stateName,
																	y : state.count,
																	drilldown:state.stateCode,
																	id : state.stateCode
																});
															}
														});
													return res2;
											})()
										});
						    		});
				    				
				    				
				    				$.each(state, function(index, state) {
						    			res.push({	
						            		name: state.stateName,
								            id: state.stateCode,
								            data : (function() {
														var res2 = [];
														$.each(locality, function(index, locality) {
															if(state.stateCode == locality.stateCode){
																res2.push({
																	name :locality.localityName,
																	y : locality.count,
																	drilldown:locality.localityCode,
																	id : locality.localityCode
																});
															}
														});
													return res2;
											})()
										});
						    		});
				    				
				    				
				    				$.each(locality, function(index, locality) {
						    			res.push({	
						            		name: locality.localityName,
								            id: locality.localityCode,
								            data : (function() {
														var res2 = [];
														$.each(municipality, function(index, municipality) {
															if(locality.localityCode == municipality.localityCode){
																res2.push({
																	name :municipality.municipalityName,
																	y : municipality.count,
																	drilldown:municipality.municipalityCode,
																	id : municipality.municipalityCode
																});
															}
														});
													return res2;
											})()
										});
						    		});
				    				
				    				
				    				if(gramPanchayat != undefined){
				    					$.each(municipality, function(index, municipality) {
							    			res.push({	
							            		name: municipality.municipalityName,
									            id: municipality.municipalityCode,
									            data : (function() {
															var res2 = [];
															$.each(gramPanchayat, function(index, gramPanchayat) {
																if(municipality.municipalityCode == gramPanchayat.municipalityCode){
																	res2.push({
																		name :gramPanchayat.gramPanchayatName,
																		y : gramPanchayat.count,
																		drilldown:gramPanchayat.gramPanchayatCode,
																		id : gramPanchayat.gramPanchayatCode
																	});
																}
															});
														return res2;
												})()
											});
							    		});
				    					
				    					$.each(gramPanchayat, function(index, gramPanchayat) {
							    			res.push({	
							            		name: gramPanchayat.gramPanchayatName,
									            id: gramPanchayat.gramPanchayatCode,
									            data : (function() {
															var res2 = [];
															$.each(village, function(index, village) {
																if(gramPanchayat.gramPanchayatCode == village.gramPanchayatCode){
																	res2.push({
																		name :village.villageName,
																		y : village.count,
																		drilldown:village.villageCode,
																		id : village.villageCode
																	});
																}
															});
														return res2;
												})()
											});
							    		});
				    					
				    				}else{
				    					
				    					$.each(municipality, function(index, municipality) {
							    			res.push({	
							            		name: municipality.municipalityName,
									            id: municipality.municipalityCode,
									            data : (function() {
															var res2 = [];
															$.each(village, function(index, village) {
																if(municipality.municipalityCode == village.municipalityCode){
																	if(iteration1 <= 10){
																	res2.push({
																		name :village.villageName,
																		y : village.count,
																		drilldown:village.villageCode,
																		id : village.villageCode
																	});
																	iteration1 = iteration1+1;
																	dataLength = dataLength+1;
															}else{
																dataLength = dataLength+1;
															}
																}
															});
														return res2;
												})()
											});
							    		});
				    					alert(this.id);
				    					populate_villageInLoction(this.id,village,titleForFarmAreaChart);
				    				}
				    				
				    				$.each(village, function(index, village) {
						    			res.push({	
						            		name: village.villageName,
								            id:village.villageCode,
								            colorByPoint: true,
								            data : (function() {
														var res2 = [];
														$.each(farmerDetails, function(index, farmerDetails) {
															if(village.villageCode == farmerDetails.villageCode){
																res2.push({
																	name :"Total Count",
																	y : Number(farmerDetails.totalCount),
																});
																
																res2.push({
																	name :"Active",
																	y : Number(farmerDetails.active),
																});
																
																res2.push({
																	name :"InActive",
																	y : Number(farmerDetails.inActive),
																});
																
																res2.push({
																	name :"Certified",
																	y : Number(farmerDetails.certified),
																});
																
																res2.push({
																	name :"NonCertified",
																	y : Number(farmerDetails.nonCertified),
																});
															}
														});
														
													return res2;
											})()
										});
						    		});
				    	return res;
					})(),
					point : {
		events : {
			click : function(event) {
				if(isEmpty(getCurrentBranch())){
					loadSowingSegrateValues(this.id,warehouse,mobileUser,branch);
					}
					else{
						loadMobSowingSegrateValues(this.id,mobileUser,branch,warehouse);
						}
				alert(this.id);
				populate_villageInLoction(this.id,village,titleForFarmAreaChart);
			}
		}
	}
	*/},
	
	  }, function(chart) {});
}
function populate_countryInLoction(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
//alert(titleForFarmAreaChart);
	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=branchId;
	//alert(codeForCropChart);
	var locationTitleArray='Farmer By Country';
var unit = "Hectare";
	
	var titleForFarmAreaChart = locationTitleArray;
	titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	esecharts
	.chart(
			'farmerByLocationChart_bar',
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
													farmerByLocationChart_pie(branchId,branchId,unit,titleForFarmAreaChart);
								return res;
							})(),
							point : {
								events : {
									click : function(event) {
										    populate_stateInLocation(this.id,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										
									}
								}
							}
					
						}
						],

			});
	//customMobSowingBackButton(warehouse,mobileUser,branch);
customBackrenderFarmerByLocationChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable);

}
function customBackrenderFarmerByLocationChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	renderFarmerByLocationChart(branch,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable);
	    },null,hoverState,pressedState).add();
}

function populate_stateInLocation(countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=countryCode;
	var locationTitleArray='Farmer By State';
	var unit = "Hectare";
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	//alert(tenantId);

	esecharts
	.chart(
			'farmerByLocationChart_bar',
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
							/*	$.each(municipality, function(index, municipality) {
					    			res.push({	
					            		name: municipality.municipalityName,
							            id: municipality.municipalityCode,
							            data : (function() {
													var res2 = [];*/
								$.each(state, function(index, state) {
									if(countryCode == state.countryCode){
										res.push({
											name :state.stateName,
											y : state.count,
											drilldown:state.stateCode,
											id : state.stateCode
										});
									}
								});
								farmerByLocationChart_pie(countryCode,branchId,unit,titleForFarmAreaChart);				
											/*	return res2;
										})()
									});*/
					    	//	});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										
										    populate_localityInLocation(this.id,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										
									}
								}
							}
						}
						],

			});
	//customMobSowingBackButton(warehouse,mobileUser,branch);
	customBackpopulate_countryInLoction(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)

}

function customBackpopulate_countryInLoction(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	populate_countryInLoction(branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
	    },null,hoverState,pressedState).add();
}



function populate_localityInLocation(stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){


	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=stateid;
	var locationTitleArray='Farmer By District';
	var unit = "Hectare";
		
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");

	esecharts
	.chart(
			'farmerByLocationChart_bar',
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
							/*	$.each(municipality, function(index, municipality) {
					    			res.push({	
					            		name: municipality.municipalityName,
							            id: municipality.municipalityCode,
							            data : (function() {
													var res2 = [];*/
								$.each(locality, function(index, locality) {
									if(stateid == locality.stateCode){
										
										res.push({	name :locality.localityName,
										y : locality.count,
										drilldown:locality.localityCode,
										id : locality.localityCode
										});
									}
								});
								farmerByLocationChart_pie(stateid,branchId,unit,titleForFarmAreaChart);			
											/*	return res2;
										})()
									});*/
					    	//	});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										
										    populate_municipalityInLocation(this.id,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										
									}
								}
							}
						}
						],

			});
	//customMobSowingBackButton(warehouse,mobileUser,branch);

	customBackpopulate_stateInLocation(countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)

}

function customBackpopulate_stateInLocation(countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	populate_stateInLocation(countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
	    },null,hoverState,pressedState).add();
}
function populate_municipalityInLocation(localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=localityid;
	var locationTitleArray='Farmer By Taluk';
	var unit = "Hectare";
		
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	//alert(tenantId);

	esecharts
	.chart(
			'farmerByLocationChart_bar',
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
							/*	$.each(municipality, function(index, municipality) {
					    			res.push({	
					            		name: municipality.municipalityName,
							            id: municipality.municipalityCode,
							            data : (function() {
													var res2 = [];*/
								$.each(municipality, function(index, municipality) {
									if(localityid == municipality.localityCode){
										res.push({
											name :municipality.municipalityName,
											y : municipality.count,
											drilldown:municipality.municipalityCode,
											id : municipality.municipalityCode
										});
									}
								});
								farmerByLocationChart_pie(localityid,branchId,unit,titleForFarmAreaChart);		
											/*	return res2;
										})()
									});*/
					    	//	});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										
										    populate_villageInLocation(this.id,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										
									}
								}
							}
						}
						],

			});
	//customMobSowingBackButton(warehouse,mobileUser,branch);
	customBackpopulate_localityInLocation(stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)
}

function customBackpopulate_localityInLocation(stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	populate_localityInLocation(stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
	    },null,hoverState,pressedState).add();
}

function populate_villageInLocation(municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){

	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=municipalityId;
	var locationTitleArray='Farmer By Village';
	var unit = "Hectare";
		
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	//alert(tenantId);

	esecharts
	.chart(
			'farmerByLocationChart_bar',
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
							/*	$.each(municipality, function(index, municipality) {
					    			res.push({	
					            		name: municipality.municipalityName,
							            id: municipality.municipalityCode,
							            data : (function() {
													var res2 = [];*/
								
								$.each(village, function(index, village) {
									if(municipalityId == village.municipalityCode){
										if(iteration1 <= 5){
										res.push({
											name :village.villageName,
											y : village.count,
										//	drilldown:village.villageCode,
											id : village.villageCode
										});
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
										}
										else{
											dataLength = dataLength+1;
										}
									}
								});
								farmerByLocationChart_pie(municipalityId,branchId,unit,titleForFarmAreaChart);				
											/*	return res2;
										})()
									});*/
					    	//	});
								return res;
							})(),

							point : {
								events : {
									click : function(event) {
										    populate_farmerDetailsInLocation(this.id,municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
										
									}
								}
							},
							 showInLegend: true
						}
						],

			}, function(chart) { 
				 var flag = "hideArrows";
		
				 if(iteration1 > 5){
					  flag = "showArrows";
				  } 
			
				  if(flag == "showArrows"){
				    chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerByLocationChart_bar').add();
				    chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerByLocationChart_bar').add();
				    chart.xAxis[0].setExtremes(0,4);
				  
				    var drilldown = dataLength/5;
				    
				 
				    $('.left_farmerByLocationChart_bar').click(function() {
				    	if(rightClick > 0){
				    		if(rightClick == 1){
				    			rightCount = rightCount-6;
				    		}else{
				    			rightCount = rightCount-5;
				    		}
				    	
					    	temp1 = rightCount;
						
						 var yValues =  new Array();
						 
						 iteration2 = 1;
						 $.each(village, function(index, village) {
							 if(municipalityId == village.municipalityCode){
							 if(iteration2 >= temp1 ){
									yValues.push({
										name :village.villageName,
										y : village.count,
										drilldown:village.villageCode,
										id : village.villageCode
									});
								}
								iteration2 = iteration2+1;
							 }
							});
					
						 iteration2 = 1;
						 
						chart.series[0].setData(eval(yValues), false, true);
						
						chart.xAxis[0].setExtremes(0, 3); 
						 rightClick = rightClick - 1;
						 iteration2 = 1;
				    }
				    });
				    
				    $('.right_farmerByLocationChart_bar').click(function() {
				 
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
								
						    	 var yValues =  new Array();
							
						    	 iteration2 = 1;
						    	 $.each(village, function(index, village) {
						    		 if(municipalityId == village.municipalityCode){
							    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
												yValues.push({
													name :village.villageName,
													y : village.count,
												//	drilldown:village.villageCode,
													id : village.villageCode
												});
											}
							    		 iteration2 = iteration2+1;
						    		 }
										
									});
						    	 
						    	  iteration2 = 1;
								
									
								 chart.series[0].setData(eval(yValues), false, true);
								
								 if(yValues.length < 5){
									 chart.xAxis[0].setExtremes(0, yValues.length-1); 
								 }else{
									 chart.xAxis[0].setExtremes(0, 3); 
								 }
								
								 rightClick = rightClick+1;
							} 
					})
			  }
				}
			);
	customBackpopulate_municipalityInLocation(localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)

}

function customBackpopulate_municipalityInLocation(localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	populate_municipalityInLocation(localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);
	    },null,hoverState,pressedState).add();
}

function populate_farmerDetailsInLocation(villageCode,municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch){
	var tenantId = getCurrentTenantId();
	var branchForPieChart;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	codeForCropChart=villageCode;
	var locationTitleArray='Farmer Details';
	var unit = "Hectare";
		
		var titleForFarmAreaChart = locationTitleArray;
		titleForFarmAreaChart =  titleForFarmAreaChart.replace("Farmer By", "Farm Area By ");
	//alert(tenantId);

	esecharts
	.chart(
			'farmerByLocationChart_bar',
			{
				chart : {
					type : 'column',
					// backgroundColor:'rgba(255, 255, 255, 0.0)',
					  marginLeft: 100,
				        marginRight: 100,
					options3d : {
						enabled : false,
						alpha : 10,
						beta : 20,
						depth : 70
					}
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
				      	var res2 = [];
						$.each(farmerDetails, function(index, farmerDetails) {
							if(villageCode == farmerDetails.villageCode){
								res2.push({
									name :"Total Count",
									y : Number(farmerDetails.totalCount),
								});
								
								/*res2.push({
									name :"Active",
									y : Number(farmerDetails.active),
								});
								
								res2.push({
									name :"InActive",
									y : Number(farmerDetails.inActive),
								});*/
								
								res2.push({
									name :"Certified",
									y : Number(farmerDetails.certified),
								});
								
								res2.push({
									name :"NonCertified",
									y : Number(farmerDetails.nonCertified),
								});
							}
						});
						
					return res2;
	})(),

	
}
						],

			});
	customBackpopulate_villageInLocation(municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch);

}
function customBackpopulate_villageInLocation(municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch) {
	 var chart = $('#farmerByLocationChart_bar').esecharts();
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
	    	populate_villageInLocation(municipalityId,localityid,stateid,countryCode,branchId,country,state,locality,municipality,gramPanchayat,village,farmerDetails,getGramPanchayatEnable,unit,titleForFarmAreaChart,branch)
	    },null,hoverState,pressedState).add();
}



function farmerByLocationChart_pie(locationCode,branch,unit,titleForFarmAreaChart){
	//alert(branch);
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	jQuery.post("farmer_getFarmDetailsAndProposedPlantingArea.action",{locationLevel:locationCode,selectedBranch:branch},function(farmDetailsAndAcre) {
		farmDetailsAndAcre_json = $.parseJSON(farmDetailsAndAcre);
		
	
	
	var chart = new esecharts.chart('farmerByLocationChart_pie', {
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
	    /* plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	                format: '<b>{point.name}</b>: {point.y} Acre',
	                style: {
	                   // color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                },
	                connectorColor: 'silver'
	            }
	        }
	    } */
	   /* plotOptions: {
	        pie: {
	        	name:" ",
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">{point.y} Acre</span> ',
	               // format: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold">{point.y}</span>',
	                 style: {
			            fontSize: '16px'
			        } 
	            }
	        },
	        series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function() {
                           
                          //  location.href = this.options.url;
                        }
                    }
                }
            }
	    },*/

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
		    //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerByLocationChart_pie').add();
		   // chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerByLocationChart_pie').add();
		    chart.xAxis[0].setExtremes(0,4);
		  
		    var drilldown = dataLength/5;
		    
		 
		    if($("#arrow_farmerByLocationChart_pie").length > 0){
		    	   $("#arrow_farmerByLocationChart_pie").remove();
		    	}

		    	
		    	var row = jQuery('<div/>', {
		    		class : "col-xs-12",
		    		id	  : "arrow_farmerByLocationChart_pie"
		    	});
		    					    
		    	$("#farmerByLocationChart_pie").append($(row));
		    	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerByLocationChart_pie "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
		    	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerByLocationChart_pie"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')
		    	 
		    
		    $('.left_farmerByLocationChart_pie').click(function() {
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
		    
		    $('.right_farmerByLocationChart_pie').click(function() {
		    	
		    	
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
	
	var titleForFarmerLocationCropChart ;
	
	
	titleForFarmerLocationCropChart = titleForFarmAreaChart.replace("Farm Area By ","Farm Crop Area By");
	titleForFarmerLocationCropChart = titleForFarmAreaChart.replace("Farm Area","Farm Crop Area");
	populateFarmerLocationCropChart(titleForFarmerLocationCropChart);
}


function populateFarmerLocationCropChart(titleForFarmerLocationCropChart){
	var dataForChart = new Array();
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	if(codeForCropChart == "first_level_Branch_Login"){
		codeForCropChart = "";
	}
	
	jQuery.post("farmer_populateFarmerLocationCropChart.action",{codeForCropChart:codeForCropChart},function(farmerByLocation_crop) {
		
	var	farmerByLocation_crop_Data = $.parseJSON(farmerByLocation_crop);
	$.each(farmerByLocation_crop_Data, function(index, value) {
			if(iteration1 <= 3){
				dataForChart.push({	
					"name" : value.productName,
					"y" : Number(Number(value.Area).toFixed(3)),
					
					
				});
				iteration1 = iteration1+1;
				dataLength = dataLength+1;
			}else{
				dataLength = dataLength+1;
			}
	});
	
		esecharts.chart('farmerByLocation_crop_3d', {
			    chart: {
			        type: 'pie',
			        marginLeft: 100,
			        marginRight: 100,
			    },
			    title: {
			        text: titleForFarmerLocationCropChart
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
			        pie: {
			        	innerSize: 70,
			            depth: 45
			        },
			        series: {
			            borderWidth: 0,
			            dataLabels: {
			                enabled: true,
			                format: '{point.name} : <span style="color: {point.color};">{point.y}</span> '+areaType
			            	
			            }
			        }
			    },
			    
			    /*plotOptions: {
			        pie: {
			        	name:" ",
			           // allowPointSelect: true,
			            cursor: 'pointer',
			            dataLabels: {
			                enabled: true,
			                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold"> ₹ {point.y} </span> ',
			            }
			        }
			    },*/
			    series: [{
			       
			        data:dataForChart,
			        /*dataLabels: {
			            enabled: true,
			            rotation: -90,
			            color: '#FFFFFF',
			            align: 'right',
			            format: '{point.y:.1f}', // one decimal
			            y: 10, // 10 pixels down from the top
			            style: {
			               // fontSize: '13px',
			                fontFamily: 'Verdana, sans-serif'
			            }
			        }*/
			        showInLegend: true
			    }],
			   
			 }, function(chart) { 
				 var flag = "hideArrows";
					
				  if(farmerByLocation_crop_Data.length > 3){
					  flag = "showArrows";
				  } 
			  
			  if(flag == "showArrows"){
				//chart.renderer.button('< <p class="farmerByLocation_crop_3d_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerByLocation_crop_3d').add();
			    //$(".left_WarehouseToMobileUser_AgentChart").hide();
			  
				//chart.renderer.button('<p class="farmerByLocation_crop_3d_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerByLocation_crop_3d').add();
			    chart.xAxis[0].setExtremes(0,9);
			  
			    var drilldown = dataLength/3;
			    
			   
			   if($("#arrow_farmerByLocation_crop_3d").length > 0){
				   $("#arrow_farmerByLocation_crop_3d").remove();
			   }
			   
			   var row = jQuery('<div/>', {
					class : "col-xs-12",
					id	  : "arrow_farmerByLocation_crop_3d"
				});
			    
			    $("#farmerByLocation_crop_3d").append($(row));
			    $(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerByLocation_crop_3d "><span class="glyphicon glyphicon-chevron-left"></span></button></div>');   
			    $(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerByLocation_crop_3d"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>');
			    
			   
			    $('.left_farmerByLocation_crop_3d').click(function() {

			    	if(rightClick > 0){
			    		if(rightClick == 1){
			    			rightCount = rightCount-3;
			    		}else{
			    			rightCount = rightCount-3;
			    		}
			    	
				    	temp1 = rightCount;
			    	
					
					
					 var yValues1 =  new Array();
					
					 iteration2 = 1;
					 $.each(farmerByLocation_crop_Data, function(index, value) {
						 if(iteration2 >= temp1 ){
							if(yValues1.length <= 2){
								yValues1.push({
									"name" : value.productName,
									"y" : Number(Number(value.Area).toFixed(3)),
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
					 
					 
					 
					 if(rightClick != 0){
						 $('.farmerByLocation_crop_3d_backward').text('< Back  '+Number(rightClick))
					}else{
						 $(".left_farmerByLocation_crop_3d").hide();
					 }
					 $('.farmerByLocation_crop_3d_forward').text('Next  '+Number(rightClick+2)+' >');
					 
					 $('.right_farmerByLocation_crop_3d').show();
			    }
			    	
			    });
			    
			    $('.right_farmerByLocation_crop_3d').click(function() {
			    	
			    	if(rightClick < drilldown-1){
					   if(rightCount == 0){
					    	rightCount = rightCount+4;
					    	rightCount = Number(rightCount);
					    }else{
					    	rightCount = rightCount+3;
					    }
					 
					    	 temp1 = rightCount;
					    	 if(dataLength >=  Number(rightCount+2)){
					    		 temp2 = Number(rightCount+2);
					    		 //temp2 = temp2+10;
					    	 }else{
					    		var temp2  = Number(dataLength-(rightCount-1));
					    	 }	
					    	
					    	 if(temp2 < temp1){
								temp2 = dataLength ;
					    	 }
							
					    	 var yValues1 =  new Array();
					    	
					    	 iteration2 = 1;
					    	
					    	 $.each(farmerByLocation_crop_Data, function(index, value) {
					    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
					    			 yValues1.push({
					    				 "name" : value.productName,
					 					"y" : Number(Number(value.Area).toFixed(3)),
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
			    	
			    	
			    	
			    	if(rightClick < drilldown-1){
			    		$('.farmerByLocation_crop_3d_forward').text('Next  '+Number(rightClick+2)+' >');
			    	}else{
			    		$('.right_farmerByLocation_crop_3d').hide();
			    	}
			    	
			    	 $(".left_farmerByLocation_crop_3d").show();
			    	$('.farmerByLocation_crop_3d_backward').text('< Back  '+Number(rightClick))
			    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
			    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
			    	
			    })
		}
			});
	}); 
	
	estimatedAndActualYield();
	
	
}


function estimatedAndActualYield(){
	
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	if(isEmpty(codeForCropChart)){
		codeForCropChart = getCurrentBranch();
	}
	jQuery.post("farmer_estimatedAndActualYield.action",{codeForCropChart:codeForCropChart},function(result) {
		var json = $.parseJSON(result);
		
		esecharts.chart('farmerByLocation_estimated', {
		    chart: {
		        type: 'bar',
		        marginLeft: 100,
		        marginRight: 100
		    },
		    title: {
		        text: 'Estimated Vs Actual Yield'
		    },
		    /*subtitle: {
		        text: 'Source: WorldClimate.com'
		    },*/
		    xAxis: {
		        type: 'category',
		        title: {
		            text: '<b>Crops</b>'
		        }
		    },
		   /* yAxis: {
		       // min: 0,
		        title: {
		            text: 'harvest'
		        }
		    },*/
		    tooltip: {
		        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		            '<td style="color:{series.color};padding:0"><b>{point.y} Kgs</b></td></tr>',
		        footerFormat: '</table>',
		        shared: true,
		        useHTML: true
		    },
		    /* plotOptions: {
		        column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		        }
		    },*/
		    plotOptions: {
		    	 
		        series: {
		           allowPointSelect: false,
		            dataLabels: {
		                enabled: true,
		                format: '{point.y} Kgs',
		                
		            }
		        }
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'top',
	        x: -30,
	        y: 80,
	        floating: true,
	        borderWidth: 1,
	        backgroundColor: ((esecharts.theme && esecharts.theme.legendBackgroundColor) || '#FFFFFF'),
	        shadow: true
	    },
	   /* tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
	    },*/
		    series: [{
		        name: 'Estimated',
		        //colorByPoint: true,
		        data : (function() {
							var res = [];
							
							$.each(json, function(index, value) {
								
								if(iteration1 <= 5){
									res.push({
										name :value.productName,
										y : Number(value.estimated),
										
									 });
										iteration1 = iteration1+1;
										//dataLength = dataLength+1;
								}else{
									//dataLength = dataLength+1;
								}
								
							});
							iteration1 = 1;
							return res;
						})()
					 },
					 {
					        name: 'Actual',
					      //  colorByPoint: true,
					        data : (function() {
										var res = [];
												
										$.each(json, function(index, value) {
											if(iteration1 <= 5){
												res.push({
													name :value.productName,
													y : Number(value.actual),
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
			 
			  if(json.length > 5){
				  flag = "showArrows";
			  }
			  if(flag == "showArrows"){
			    //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_farmerByLocation_estimated').add();
			    //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_farmerByLocation_estimated').add();
			    chart.xAxis[0].setExtremes(0,4);
			  
			    var drilldown = dataLength/5;
			    
			    if($("#arrow_farmerByLocation_estimated").length > 0){
			    	   $("#arrow_farmerByLocation_estimated").remove();
			    	}


			    	var row = jQuery('<div/>', {
			    		class : "col-xs-12",
			    		id	  : "arrow_farmerByLocation_estimated"
			    	});
			    					    
			    	$("#farmerByLocation_estimated").append($(row));
			    	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_farmerByLocation_estimated "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
			    	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_farmerByLocation_estimated"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

			    
			 
			    $('.left_farmerByLocation_estimated').click(function() {
			    	if(rightClick > 0){
			    		if(rightClick == 1){
			    			rightCount = rightCount-6;
			    		}else{
			    			rightCount = rightCount-5;
			    		}
			    	
				    	temp1 = rightCount;
			    	
					
					
					 var yValues1 =  new Array();
					 var yValues2 =  new Array();
					 iteration2 = 1;
					 $.each(json, function(index, value) {
						 if(iteration2 >= temp1 ){
							if(yValues1.length <= 4){
								yValues1.push({
									name :value.productName,
									y : Number(value.estimated),
								 });
							}
								
						
							 
								}
							iteration2 = iteration2+1;
			    		});
					 
					 iteration2 = 1;
					 
					 $.each(json, function(index, value) {
						 if(iteration2 >= temp1 ){
								if(yValues2.length <= 4){
							 yValues2.push({
									name :value.productName,
									y : Number(value.actual),
								 });
								}
								}
							iteration2 = iteration2+1;
			    		});
					 iteration2 = 1;
					
					chart.series[0].setData(eval(yValues1), false, true);
					chart.series[1].setData(eval(yValues2), false, true);
					chart.xAxis[0].setExtremes(0, 3); 
					 rightClick = rightClick - 1;
					 iteration2 = 1;
			    }
			    });
			    
			    $('.right_farmerByLocation_estimated').click(function() {
			    	
			    	
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
					    	 var yValues2 =  new Array();
					    	 iteration2 = 1;
					    	 $.each(json, function(index, value) {
								
					    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
					    			 yValues1.push({
											name :value.productName,
											y : Number(value.estimated),
										 });
									}
								 iteration2 = iteration2+1;
					    		 });
					    	 
					    	 iteration2 = 1;
					    	 
					    	 $.each(json, function(index, value) {
					    	 if(iteration2 >= temp1 && iteration2 <= (temp2)){
				    			 yValues2.push({
										name :value.productName,
										y : Number(value.actual),
									 });
								}
				    		 iteration2 = iteration2+1;
							});
					    	 iteration2 = 1;
							
					    	 chart.series[0].setData(eval(yValues1), false, true);
							 chart.series[1].setData(eval(yValues2), false, true);
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
}

function getChartData(chartType){
	var chart1_data = new Map();
	var chart2_data = new Map();
	var distribution_max = new Map();
	/* var current_max = new Map();
	var existing_max = new Map(); */
	var selectedBranch = $("#distributionBranchFilter").val();
	var selectedSeason = $("#distributionseasonFilter").val();
	
	var chartContainer1 = "distributionByWarehouse";
	/*if(chartType == "AGENT_ID"){
		chartContainer1 = "distributionByWarehouse_agent";
	}else if(chartType == "FARMER_ID"){
		chartContainer1 = "distributionByWarehouse_farmer";
	}else if(chartType == ""){
		
	}*/
	
	var chartContainer2 = "distributedProductsByWarehouse";
/*	if(chartType == "AGENT_ID"){
		chartContainer1 = "distributedProductsByWarehouse_agent";
	}else if(chartType == "FARMER_ID"){
		chartContainer1 = "distributedProductsByWarehouse_farmer";
	}*/
	
	
	if(isEmpty(selectedBranch) && isEmpty(selectedSeason)){
		var myNode2 = document.getElementById(chartContainer1);
		myNode2.innerHTML = "<p style='height: 300px; width: 700px;padding:200px;'></p>";
	}
	jQuery.post("farmerDistributionReport_getChartData.action", {branch:selectedBranch,seasonCode:selectedSeason,chartType:chartType},function(result) {
		json = $.parseJSON(result);
			if(!isEmpty(json)){
				$.each(json, function(index, value) {
					
					if(distribution_max.get(value.id) == undefined){
						distribution_max.set(value.id,value.distributionQuantity);
					}else{
						var distribution_temp = Number(distribution_max.get(value.id))+Number(value.distributionQuantity);
						distribution_max.delete(value.id);
						distribution_max.set(value.id,distribution_temp);
					}
					
					/* if(current_max.get(value.id) == undefined){
						current_max.set(value.id,value.currentQuantity);
					}else{
						var current_temp = Number(current_max.get(value.id))+Number(value.currentQuantity);
						current_max.delete(value.id);
						current_max.set(value.id,current_temp);
					}
					
					if(existing_max.get(value.id) == undefined){
						existing_max.set(value.id,value.existingQuantity);
					}else{
						var existing_temp = Number(existing_max.get(value.id))+Number(value.existingQuantity);
						existing_max.delete(value.id);
						existing_max.set(value.id,existing_temp);
					} */
					
				});
				
				$.each(json, function(index, value) {
					chart1_data.set(value.id,value.warehouse+"-"+distribution_max.get(value.id));	
					chart2_data.set(value.id,value.existingQuantity+"%"+distribution_max.get(value.id)+"$"+value.currentQuantity+"#"+value.warehouse);
				});
				
				populateDistributionByWarehouse(chart1_data,chart2_data,chartType);
			}else{
				var myNode = document.getElementById(chartContainer1);
				myNode.innerHTML = "<p style='height: 300px; width: 700px;padding:200px;'>No data available</p>";
				
				var myNode2 = document.getElementById(chartContainer2);
				myNode2.innerHTML = "<p style='height: 300px; width: 700px;padding:200px;'></p>";
				
			}
		
		});
	
}

//function 4

function populateDistributionByWarehouse(chart1_data,chart2_data,chartType){
	var chartContainer1 = "distributionByWarehouse";
	var chartName = chartType;
	
	
	if(chartType == ""){
		chartName =	$("#distributionDetailFilter").val();
		
	}
	if(chartType == ""){
		chartName = "Agent and Farmer";
	}else if(chartType == "FARMER_ID"){
		chartName = "Farmer";
	}else if(chartType == "AGENT_ID"){
		chartName = "Agent";
	}
	
	//setChartTheme();
	//var data = [{ name: 'One', y: 10, id: 0 },{ name: 'Two', y: 10, id: 1 },{ name: 'three', y: 10, id: 1 },{ name: 'four', y: 10, id: 1 },];
	 
	var data = (function() {
		var res = [];
		var i = 0;
		var colors = ['#2b908f', '#90ee7e', '#f45b5b', '#7798BF', '#aaeeee', '#ff0066',
			      '#eeaaee', '#55BF3B', '#DF5353', '#7798BF', '#aaeeee'];
    	for (var [key, value] of chart1_data) {
    		 var val = String(value);
			 var index = val.indexOf("-");
			 res.push({
				  name:val.substr(0,index),
				  y : Number(val.substr(index+1,val.length)),
				  id:key,
				  color:colors[i]
				 });
			 if(i == 10){
				 i = 0;
			 }else{
				 i = i+1; 
			 }
			 
			 
		}
		return res;
	})();
	
	
	var chart = new	esecharts.chart(chartContainer1, {
		 plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">{point.y} Kgs</span> ',
		               // format: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold">{point.y}</span>',
		                /*style: {
				            fontSize: '16px'
				        }*/
		            }
		        }
		    },
	    title: {
	        text:"<b>Distribution done by "+chartName+" </b>"
	    },
 	    /* tooltip: {
	pointFormat: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold"> Distribution : {point.y}</span>',	    
	backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0'
		      }
		   }, */
 	 
		    tooltip: {
		        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} </b>Kgs of total<br/>',
		        backgroundColor: 'rgba(0, 0, 0, 0.85)',
			      style: {
			         color: '#F0F0F0',
			         fontSize: '16px'
			      }
			  
		    }, 
		    
		    /* tooltip: {
		        
		        style: {
		            fontSize: '16px'
		        },
		        pointFormat: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold">{point.y}%</span>',
		      
		    }, */

		   
	    series:[
	            {
	               "data": data,
	                type: 'pie',
	                animation: false,
	                point:{
	                    events:{
	                        click: function (event) {
	                           $("#chartcontainer2").fadeToggle("fast");
	                           $("#chartcontainer2").fadeToggle("slow");
	                        	//alert(this.id);
	                        	
	                        	populateDistributedProductsByWarehouse(this.id,chart2_data,chartType);
	                        }
	                    }
	                }          
	            }
	         ],
	    
	    
	});
	$("#distributedProductsByWarehouse").hide();
}

//function 5



function populateDistributedProductsByWarehouse(id,chart2_data,chartType){
	$("#distributedProductsByWarehouse").show();
	var chartContainer2 = "distributedProductsByWarehouse";
	
	
	var data = chart2_data.get(id);
	var index1 = data.indexOf("%");
	var index2 = data.indexOf("$");
	var index3 = data.indexOf("#");
	
	var existing =  parseInt(Number(data.substr(0,index1)));
	var distribution =  parseInt(Number(data.substr(index1+1,(index2-(index1+1)))));
	var current =  parseInt(Number(data.substr(index2+1,(index3-(index2+1)))));
	var warehouse = data.substr(index3+1,data.length);
		
	/* var distribution_percentage = parseInt((distribution/existing)*100);
	var current_percentage = parseInt((current/existing)*100);
	var existing_percentage = parseInt((existing/existing)*100); */
	
	
	var distribution_max = new Map();
	var amountDetails = new Map();
	var amountDetails_result = new Map();
	var res = [];
	jQuery.post("farmerDistributionReport_getChartData.action", {warehouse_chart:id,chartType:chartType},function(result) {
		json = $.parseJSON(result);
			$.each(json, function(index, value) {
				if(distribution_max.get(value.product) == undefined){
					distribution_max.set(value.product,value.distributionQuantity);
				}else{
					var distribution_temp = Number(distribution_max.get(value.product))+Number(value.distributionQuantity);
					distribution_max.delete(value.product);
					distribution_max.set(value.product,distribution_temp);
				}
				
				if(amountDetails.get(value.product) == undefined){
					amountDetails.set(value.product,value.TOTAL_AMOUNT+","+value.FINAL_AMOUNT+","+value.PAYMENT_AMT);
				}else{
					
					var values = amountDetails.get(value.product);
					
					var valuesArr = values.split(",");
					var TOTAL_AMOUNT = Number(valuesArr[0])+Number(value.TOTAL_AMOUNT);
					var FINAL_AMOUNT =  Number(valuesArr[1])+Number(value.FINAL_AMOUNT);
					var PAYMENT_AMT =  Number(valuesArr[2])+Number(value.PAYMENT_AMT);
					amountDetails.delete(value.product);
					amountDetails.set(value.product,TOTAL_AMOUNT+","+FINAL_AMOUNT+","+PAYMENT_AMT);
				}
				
			});
			
			for (var [key, value2] of amountDetails) {
				var valuesArr = value2.split(",");
				var TOTAL_AMOUNT = valuesArr[0].trim();
				var FINAL_AMOUNT =  valuesArr[1].trim();
				var PAYMENT_AMT =  valuesArr[2].trim();
			
				
				/*if(TOTAL_AMOUNT != null){
					alert(TOTAL_AMOUNT)
				}*/
				
				if( TOTAL_AMOUNT == "null" || isNaN(TOTAL_AMOUNT)){
					TOTAL_AMOUNT = " 0 ";
				}
				if( FINAL_AMOUNT == "null" || isNaN(FINAL_AMOUNT)){
					FINAL_AMOUNT = " 0 ";
				}
				if( PAYMENT_AMT == "null" || isNaN(PAYMENT_AMT)){
					PAYMENT_AMT = " 0 ";
				}
				//console.log("TOTAL_AMOUNT : "+TOTAL_AMOUNT+", FINAL_AMOUNT: "+FINAL_AMOUNT+", PAYMENT_AMT : "+PAYMENT_AMT)
				//var s = "<table><tr><th>Company</th><th>Contact</th><th>Country</th></tr><tr><td>Alfreds Futterkiste</td><td>Maria Anders</td><td>Germany</td></tr></table>"
			//	amountDetails_result.set(key,"TOTAL_AMOUNT : "+TOTAL_AMOUNT+", FINAL_AMOUNT: "+FINAL_AMOUNT+", PAYMENT_AMT : "+PAYMENT_AMT);
				amountDetails_result.set(key,"<b>Total amount</b> : "+TOTAL_AMOUNT+"<br/> <b>Final amount</b> : "+FINAL_AMOUNT+"<br/>  <b> Payment amount</b> : "+PAYMENT_AMT);
				//	amountDetails_result.set(key,s)
					amountDetails.delete(key);
			} 
			
		/*	for (var [key, value2] of distribution_max) {
				res.push([key,value2]); 
			} */
			
			//setChartTheme();
			
			// Create the chart
			
		var chart = new	esecharts.chart(chartContainer2, {
			    chart: {
			        type: 'column',
			        
			    },
			    title: {
			        text: warehouse
			    },
			   
			    xAxis: {
			        type: 'category'
			    },
			    yAxis: {
			        title: {
			            text: 'Distributed Quantity'
			        }

			    },
			    legend: {
			        enabled: false
			    },
			    plotOptions: {
			        series: {
			            borderWidth: 0,
			            dataLabels: {
			                enabled: true,
			                format: '{point.y} Kgs'
			            }
			        }
			    },

			    tooltip: {
			        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
			        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> Kgs <br/> <br/> <br/> {point.custom}'
			    	/* pointFormat: '{series.name}: {point.y} <br>Amt details <br/> {point.custom}'*/
			    },

			    "series": [
			        {
			            "name": " ",
			            "colorByPoint": true,
			            "data":  (function() {
							var result = [];
							for (var [key, value2] of distribution_max) {
								
								result.push({	
									"name" : key,
									"y" : value2,
									"custom":amountDetails_result.get(key)
								});
							} 
								
					return result;
					
				})()
			        }
			    ]
			    
			});
		
		});

	
	
	var aTags1 = document.getElementsByTagName("text");
	var searchText1 = "esecharts.com";
	var found1;

	for (var i = 0; i < aTags1.length; i++) {
	  if (aTags1[i].textContent == searchText1) {
	    found1 = aTags1[i];
	     $(found1).hide();
	    break;
	  }
	}
	

}

function getRandomColor() {
	var letters = '0123456789ABCDEF';
	var color = '#';
	for (var i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}


function populateDistribution_WarehouseToMobileUserChart(){
	var	selectedBranch;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	if (!isEmpty(getCurrentBranch())) {
		selectedBranch = getCurrentBranch();
	}
	
	jQuery.post("dashboard_warehouseToMobileUserChart.action", {
		selectedBranch : selectedBranch
	}, function(json) {
		WarehouseChartData = $.parseJSON(json);
		esecharts.chart('WarehouseToMobileUser_WarehouseChart', {
		
			  chart: {
			        type: 'column',
			        marginLeft: 100,
			        marginRight: 100,
			  },
			  
			  title: {
				  text:' Warehouse to Mobile User'
			  },
			  
			  xAxis: {
			        type: 'category',
			        title: {
			            text:  "<b>Warehouse</b>"
			        }
			    },
			    yAxis: {
			        title: {
			            text: "Amount"
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
				                format: currencyType +''+'{point.y}'
				            }
				        }
			    },

			    tooltip: {
			        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
			       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y}</b> <br>Click to view the distribution details of <span style="color:{point.color}">{point.name}</span>'
			    },
		 series: [{
		       // type: 'column',
		      //  allowPointSelect: true,
			 	name: ' ',
		        colorByPoint : true,
		       data: (function() {
					var res = [];
					
					$.each(WarehouseChartData, function(index, value) {
						if(iteration1 <= 10){
							
							res.push({	
								name : value.servicePointName,
								y : Number(value.amount),
								id:value.servicePointId
							});
							
							iteration1 = iteration1+1;
							dataLength = dataLength+1;
							
						}else{
							dataLength = dataLength+1;
						}
					});
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	 //alert(this.id)
                	 populateWarehouseToMobileUser_AgentChart(this.id,selectedBranch,this.name);
                	 populate_ProductChartByWarehouseToMobileUser(this.id,selectedBranch,this.name);
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
		
		 
			  if(WarehouseChartData.length > 10){
				  flag = "showArrows";
			  } 
		  
		  if(flag == "showArrows"){
		    //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_WarehouseToMobileUser_WarehouseChart').add();
		    //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_WarehouseToMobileUser_WarehouseChart').add();
		    chart.xAxis[0].setExtremes(0,9);
		  
		    var drilldown = dataLength/10;
		    
		 
		    if($("#arrow_WarehouseToMobileUser_WarehouseChart").length > 0){
		    	   $("#arrow_WarehouseToMobileUser_WarehouseChart").remove();
		    	}


		    	var row = jQuery('<div/>', {
		    		class : "col-xs-12",
		    		id	  : "arrow_WarehouseToMobileUser_WarehouseChart"
		    	});
		    					    
		    	$("#WarehouseToMobileUser_WarehouseChart").append($(row));
		    	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_WarehouseToMobileUser_WarehouseChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
		    	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_WarehouseToMobileUser_WarehouseChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

		    
		    $('.left_WarehouseToMobileUser_WarehouseChart').click(function() {
		    	if(rightClick > 0){
		    	rightCount = rightCount-10;
		    	temp1 = rightCount;
				 temp2 = rightCount+9;
				 temp2 = Number(temp2);
				
				
				 var yValues =  new Array();
				 
				 
				 
				 
				 $.each(WarehouseChartData, function(index, value) {
					if(iteration2 >= temp1 ){
						yValues.push({
							name : value.servicePointName,
							y : Number(value.amount),
							id:value.servicePointId
							 });
						 }
						iteration2 = iteration2+1;
				 	});
				 
				
				 
				chart.series[0].setData(eval(yValues), false, true);
				
				 if(yValues.length < 9){
					 chart.xAxis[0].setExtremes(0, yValues.length-1); 
				 }else{
					 chart.xAxis[0].setExtremes(0, 9); 
				 }
				 rightClick = rightClick - 1;
				 iteration2 = 1;
		    }
		    });
		    
		    $('.right_WarehouseToMobileUser_WarehouseChart').click(function() {
		 
				   if(rightClick < drilldown-1){
				    if(rightCount == 0){
				    	rightCount = rightCount+1;
				    	rightCount = ""+rightCount+rightCount;
				    	rightCount = Number(rightCount);
				    }else{
				    	rightCount = rightCount+10;
				    }
				   
				    	 temp1 = rightCount;
				    	 if(dataLength >=  Number(rightCount+9)){
				    		 temp2 = Number(rightCount+9);
				    		// temp2 = temp2+10;
				    	 }else{
				    		
				    		 var temp2  = Number(dataLength-(rightCount-1));
				    	 }
				    	 if(temp2 < temp1){
							temp2 = dataLength ;
				    	 }
						
				    	 var yValues =  new Array();
						
				    	  
				    	
								$.each(WarehouseChartData, function(index, value) {
									 if(iteration2 >= temp1 && iteration2 <= (temp2)){
											yValues.push({
												name : value.servicePointName,
												y : Number(value.amount),
												id:value.servicePointId
												 });
											}
										iteration2 = iteration2+1;
								});
								
							
								
								
							
						 chart.series[0].setData(eval(yValues), false, true);
						
						 if(yValues.length <= 9){
							 chart.xAxis[0].setExtremes(0, yValues.length-1); 
						 }else{
							 chart.xAxis[0].setExtremes(0, 9); 
						 }
						
						 rightClick = rightClick+1;
						 iteration2 = 1;
					} 
			})
	  }
		});
});
}


function populateWarehouseToMobileUser_AgentChart(warehouseCode,selectedBranch,title){
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	jQuery.post("dashboard_warehouseToMobileUser_AgentChart.action", {
		selectedBranch : selectedBranch,
		selectedWarehouse : warehouseCode
	}, function(warehouseToMobileUser_AgentChart) {
	var	warehouseToMobileUser_AgentChartData = $.parseJSON(warehouseToMobileUser_AgentChart);	
esecharts.chart('WarehouseToMobileUser_AgentChart', {
	

	chart: {
        type: 'pie',
        marginLeft: 100,
        marginRight: 100,
 },
			title: {
		        text: title + '--> Mobile User'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        //	name:" ",
		           // allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold"> '+currencyType+'{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y} </b><br/>',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		       data: (function() {
					var res = [];
					
					$.each(warehouseToMobileUser_AgentChartData, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.agentName,
								"y" : Number(value.amount),
								"id":value.agentId
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(warehouseToMobileUser_AgentChartData, function(index, value) {
							res.push({	
								"name" : value.agentName,
								"y" : Number(value.amount),
								"id":value.agentId
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	 populateWarehouseToMobileUser_ProductChart(this.id,selectedBranch,warehouseCode,this.name);
                	
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
			
		  if(warehouseToMobileUser_AgentChartData.length > 5){
			  flag = "showArrows";
		  } 
	  
	  if(flag == "showArrows"){
		chart.renderer.button('< <p class="WarehouseToMobileUser_AgentChart_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_WarehouseToMobileUser_AgentChart').add();
	    $(".left_WarehouseToMobileUser_AgentChart").hide();
	  
		chart.renderer.button('<p class="WarehouseToMobileUser_AgentChart_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_WarehouseToMobileUser_AgentChart').add();
	    chart.xAxis[0].setExtremes(0,9);
	  
	    var drilldown = dataLength/5;
	    
	 
	    $('.left_WarehouseToMobileUser_AgentChart').click(function() {

	    	if(rightClick > 0){
	    		if(rightClick == 1){
	    			rightCount = rightCount-6;
	    		}else{
	    			rightCount = rightCount-5;
	    		}
	    	
		    	temp1 = rightCount;
	    	
			
			
			 var yValues1 =  new Array();
			
			 iteration2 = 1;
			 $.each(warehouseToMobileUser_AgentChartData, function(index, value) {
				 if(iteration2 >= temp1 ){
					if(yValues1.length <= 4){
						yValues1.push({
							"name" : value.agentName,
							"y" : Number(value.amount),
							"id":value.agentId
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
			 
			 
			 
			 if(rightClick != 0){
				 $('.WarehouseToMobileUser_AgentChart_backward').text('< Back  '+Number(rightClick))
			}else{
				 $(".left_WarehouseToMobileUser_AgentChart").hide();
			 }
			 $('.WarehouseToMobileUser_AgentChart_forward').text('Next  '+Number(rightClick+2)+' >');
			 
			 $('.right_WarehouseToMobileUser_AgentChart').show();
	    }
	    	
	    });
	    
	    $('.right_WarehouseToMobileUser_AgentChart').click(function() {
	    	
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
			    	
			    	 $.each(warehouseToMobileUser_AgentChartData, function(index, value) {
			    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
			    			 yValues1.push({
			    				 "name" : value.agentName,
									"y" : Number(value.amount),
									"id":value.agentId
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
	    	
	    	
	    	
	    	if(rightClick < drilldown-1){
	    		$('.WarehouseToMobileUser_AgentChart_forward').text('Next  '+Number(rightClick+2)+' >');
	    	}else{
	    		$('.right_WarehouseToMobileUser_AgentChart').hide();
	    	}
	    	
	    	 $(".left_WarehouseToMobileUser_AgentChart").show();
	    	$('.WarehouseToMobileUser_AgentChart_backward').text('< Back  '+Number(rightClick))
	    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
	    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    	
	    })
}
	});
		
	});
}

function populateWarehouseToMobileUser_ProductChart(agentId,selectedBranch,warehouseCode,title){
	
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	jQuery.post("dashboard_warehouseToMobileUser_ProductChart.action", {
		selectedBranch : selectedBranch,
		selectedWarehouse : warehouseCode,
		agentId : agentId
	}, function(warehouseToMobileUser_ProductChartByMobileUser) {
	var	warehouseToMobileUser_ProductChartByMobileUserData = $.parseJSON(warehouseToMobileUser_ProductChartByMobileUser);
esecharts.chart('WarehouseToMobileUser_ProductChart', {
			
	chart: {
        type: 'pie',
        marginLeft: 100,
        marginRight: 100,
 },
	
			title: {
		        text: title+'--> Product'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">'+currencyType+'{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span> <br> Amount : <b> '+currencyType+'{point.y} </b><br/> <br/> {point.custom}',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		       // allowPointSelect: true,
		       data: (function() {
					var res = [];
					
					$.each(warehouseToMobileUser_ProductChartByMobileUserData, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"custom":"Quantity : "+value.quantity+" "+value.unit
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(warehouseToMobileUser_ProductChartByMobileUserData, function(index, value) {
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"custom":"Quantity : "+value.quantity+" "+value.unit
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	// populateWarehouseToMobileUser_ProductChart(this.id,selectedBranch,warehouseCode);
                	
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
			
		  if(warehouseToMobileUser_ProductChartByMobileUserData.length > 5){
			  flag = "showArrows";
		  } 
	  
	  if(flag == "showArrows"){
		chart.renderer.button('< <p class="warehouseToMobileUser_ProductChartByMobileUser_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToMobileUser_ProductChartByMobileUser').add();
	    $(".left_warehouseToMobileUser_ProductChartByMobileUser").hide();
	  
		chart.renderer.button('<p class="warehouseToMobileUser_ProductChartByMobileUser_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToMobileUser_ProductChartByMobileUser').add();
	    chart.xAxis[0].setExtremes(0,9);
	  
	    var drilldown = dataLength/5;
	    
	 
	    $('.left_warehouseToMobileUser_ProductChartByMobileUser').click(function() {

	    	if(rightClick > 0){
	    		if(rightClick == 1){
	    			rightCount = rightCount-6;
	    		}else{
	    			rightCount = rightCount-5;
	    		}
	    	
		    	temp1 = rightCount;
	    	
			
			
			 var yValues1 =  new Array();
			
			 iteration2 = 1;
			 $.each(warehouseToMobileUser_ProductChartByMobileUserData, function(index, value) {
				 if(iteration2 >= temp1 ){
					if(yValues1.length <= 4){
						yValues1.push({
							"name" : value.productNAme,
							"y" : Number(value.amount),
							"id":value.productCode,
							"custom":"Quantity : "+value.quantity+" "+value.unit
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
			 
			 
			 
			 if(rightClick != 0){
				 $('.warehouseToMobileUser_ProductChartByMobileUser_backward').text('< Back  '+Number(rightClick))
			}else{
				 $(".left_warehouseToMobileUser_ProductChartByMobileUser").hide();
			 }
			 $('.warehouseToMobileUser_ProductChartByMobileUser_forward').text('Next  '+Number(rightClick+2)+' >');
			 
			 $('.right_warehouseToMobileUser_ProductChartByMobileUser').show();
	    }
	    	
	    });
	    
	    $('.right_warehouseToMobileUser_ProductChartByMobileUser').click(function() {
	    	
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
			    	
			    	 $.each(warehouseToMobileUser_ProductChartByMobileUserData, function(index, value) {
			    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
			    			 yValues1.push({
			    				 "name" : value.productNAme,
									"y" : Number(value.amount),
									"id":value.productCode,
									"custom":"Quantity : "+value.quantity+" "+value.unit
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
	    	
	    	
	    	
	    	if(rightClick < drilldown-1){
	    		$('.warehouseToMobileUser_ProductChartByMobileUser_forward').text('Next  '+Number(rightClick+2)+' >');
	    	}else{
	    		$('.right_warehouseToMobileUser_ProductChartByMobileUser').hide();
	    	}
	    	
	    	 $(".left_warehouseToMobileUser_ProductChartByMobileUser").show();
	    	$('.warehouseToMobileUser_ProductChartByMobileUser_backward').text('< Back  '+Number(rightClick))
	    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
	    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    	
	    })
}
	});
		
	});
	
}


function populateMobileUserToFarmer_AgentChart(val){
	var season = $("#distributionseasonFilter").val();
	var selectedYear=$("#finYearFilter").val();
	jQuery.post("dashboard_populateMobileUserToFarmer_AgentChart.action",{selectedSeason : season,selectedFinYear:selectedYear},function(result) {
		json = $.parseJSON(result);
		if(!isEmpty(json)){
		$.each(json, function(index, value) {
			renderWarehouseMobileUserToFarmer_AgentChart(value.warehouse,value.mobileUser,season,selectedYear);
		});
	   }
	});
}

function renderWarehouseMobileUserToFarmer_AgentChart(warehouse,mobileUser,season,selectedYear){


	var tenantId = getCurrentTenantId();
	//var locationTitleArray;
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;
	var rightCount = 0;
	var rightClick = 0;
	var chart = new	esecharts.chart('mobileUserToFarmer_AgentChart', {
	    chart: {
	        type: 'column',
	        marginLeft: 100,
	        marginRight: 100,
	        events: {
	            drilldown: function(e) {
	            	codeForCropChart = e.point.id;
	            	locationCode =  e.point.id; 
	            	locationCode_ForDrillUp[drillUp_count]=e.point.id; 
	            	drillUp_count = drillUp_count+1;
	       
	            },
	            drillup: function(e) {
	            	
	            	if(drillUp_count != 0 && drillUp_count != 1){
	            		var locationCode = locationCode_ForDrillUp[drillUp_count-2];
	            	}
	            	drillUp_count = drillUp_count-1;
	            	codeForCropChart = locationCode;
	            	
	            	
	            }
	        }
	      
	    },
	    title: {
	    	text:'MobileUser To Farmer'
	        	
	    },
	   
	    xAxis: {
	    	 type: 'category',
	       
	    },
	    yAxis: {
	        title: {
	            text: "Amount"
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
	                format: currencyType +''+'{point.y}'
	            }
	        }
    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>'+currencyType+'{point.y}</b>'
	    },
	    series: [{
			 name: ' ',
		        colorByPoint : true,
		        allowPointSelect: true,
		       data: (function() {
					var res = [];
					
					$.each(warehouse, function(index, value) {
						if(iteration1 <= 10){
							res.push({	
								name : value.warehouseName,
								y : Number(value.amount),
								id:value.warehouseCode
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
			return res;
		})(),
		 point:{
          events:{
              click: function (event) {
            	 /* populate_renderMobileUserToFarmer_AgentChart(this.id,warehouse,mobileUser,season,selectedYear); */
             	
             	
              }
          }
      },
      showInLegend: true
		 }
	    ]
	    },
	    function(chart) {
	    	 
			 var flag = "hideArrows";
				  if(warehouse.length > 10){
					  flag = "showArrows";
				  } 
			  
			  if(flag == "showArrows"){
			   // chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_mobileUserToFarmer_AgentChart').add();
			    //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_mobileUserToFarmer_AgentChart').add();
			    chart.xAxis[0].setExtremes(0,9);
			  
			    var drilldown = dataLength/10;
			    
			    if($("#arrow_mobileUserToFarmer_AgentChart").length > 0){
			    	   $("#arrow_mobileUserToFarmer_AgentChart").remove();
			    	}


			    	var row = jQuery('<div/>', {
			    		class : "col-xs-12",
			    		id	  : "arrow_mobileUserToFarmer_AgentChart"
			    	});
			    					    
			    	$("#mobileUserToFarmer_AgentChart").append($(row));
			    	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_mobileUserToFarmer_AgentChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
			    	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_mobileUserToFarmer_AgentChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')

			 
			    $('.left_mobileUserToFarmer_AgentChart').click(function() {
			    	if(rightClick > 0){
			    	rightCount = rightCount-10;
			    	temp1 = rightCount;
					 temp2 = rightCount+9;
					 temp2 = Number(temp2);
					
					
					 var yValues =  new Array();
					 
					 
						$.each(warehouse, function(index, value) {
							if(iteration2 >= temp1 ){
								yValues.push({	
									name : value.warehouseName,
									y : Number(value.amount),
									id:value.warehouseCode
									
								});
							}
								iteration2 = iteration2+1;
						});
					 
					 /*$.each(agentChartData, function(index, value) {
						if(iteration2 >= temp1 ){
							yValues.push({
								name : value.agentName,
								y : Number(value.amount),
								id:value.agentId
								 });
							 }
							iteration2 = iteration2+1;
					 	});*/
					 
					
					 
					chart.series[0].setData(eval(yValues), false, true);
					
					 if(yValues.length < 9){
						 chart.xAxis[0].setExtremes(0, yValues.length-1); 
					 }else{
						 chart.xAxis[0].setExtremes(0, 9); 
					 }
					 rightClick = rightClick - 1;
					 iteration2 = 1;
			    }
			    });
			    
			    $('.right_mobileUserToFarmer_AgentChart').click(function() {
			
					   if(rightClick < drilldown-1){
						  
						   if(rightCount == 0){
					    	rightCount = rightCount+1;
					    	rightCount = ""+rightCount+rightCount;
					    	rightCount = Number(rightCount);
					    }else{
					    	rightCount = rightCount+10;
					    }
					   
					    	 temp1 = rightCount;
					    	 
					    	 if(dataLength >=  Number(rightCount+9)){
					    		 temp2 = Number(rightCount+9);
					    		// temp2 = temp2+10;
					    	 }else{
					    		
					    		 var temp2  = Number(dataLength-(rightCount-1));
					    	 }
					    	 if(temp2 < temp1){
								temp2 = dataLength ;
					    	 }
							
					    	 var yValues =  new Array();
							
									/*$.each(agentChartData, function(index, value) {
										 if(iteration2 >= temp1 && iteration2 <= (temp2)){
												yValues.push({
													name : value.agentName,
													y : Number(value.amount),
													id:value.agentId
													 });
												}
											iteration2 = iteration2+1;
									});
									*/
					    		$.each(warehouse, function(index, value) {
					    			if(iteration2 >= temp1 && iteration2 <= (temp2)){
					    				yValues.push({	
											name : value.warehouseName,
											y : Number(value.amount),
											id:value.warehouseCode
											
										});
									}
										iteration2 = iteration2+1;
								});	
					    	
									
							
								
							 chart.series[0].setData(eval(yValues), false, true);
							
							 if(yValues.length <= 9){
								 chart.xAxis[0].setExtremes(0, yValues.length-1); 
							 }else{
								 chart.xAxis[0].setExtremes(0, 9); 
							 }
							
							 rightClick = rightClick+1;
							 iteration2 = 1;
						} 
				})
		  }
			
	    });
}

function populate_renderMobileUserToFarmer_AgentChart(warehouseId,warehouse,mobileUser,season,selectedYear){
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	var selectedBranch = getCurrentBranch();
	esecharts
	.chart(
			'mobileUserToFarmer_AgentChart',
			{
			    chart: {
			        type: 'column',
			        marginLeft: 100,
			        marginRight: 100,
			        events: {/*
			            drilldown: function(e) {
			            	codeForCropChart = e.point.id;
			            	locationCode =  e.point.id; 
			            	locationCode_ForDrillUp[drillUp_count]=e.point.id; 
			            	drillUp_count = drillUp_count+1;
			       
			            },
			            drillup: function(e) {
			            	
			            	if(drillUp_count != 0 && drillUp_count != 1){
			            		var locationCode = locationCode_ForDrillUp[drillUp_count-2];
			            	}
			            	drillUp_count = drillUp_count-1;
			            	codeForCropChart = locationCode;
			            	
			            	
			            }
			        */}
			      
			    },
			    title: {
			    	text:'Mobile user to farmer'
			        	
			    },
			   
			    xAxis: {
			    	 type: 'category',
			       
			    },
			    yAxis: {
			        title: {
			            text: ""
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
			               // format: currencyType +''+'{point.y}'
			            }
			        }
		    },

			    tooltip: {
			        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
			       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
			    },
			    series: [{
					 name: ' ',
				        colorByPoint : true,
				       // allowPointSelect: true,
				       data: (function() {
							var res = [];
							//alert(warehouseId);
							
							$.each(mobileUser, function(index, value) {
								if(warehouseId == value.warehouseCode){
									res.push({	
										name : value.agentName,
										y : Number(value.amount),
										id:value.agentId
										
									});
										
								}
							});
					return res;
				})(),
				 point:{
		          events:{
		              click: function (event) {
		            	  populate_ProductChartByMobileUserToFarmer(this.id,selectedBranch,this.name,season,selectedYear);
		             	
		             	
		              }
		          }
		      },
		      showInLegend: true
				 }
			    ]
			    });
	customBackButtonMobileUserToFarmer_AgentChart(warehouse,mobileUser,season);

}

function customBackButtonMobileUserToFarmer_AgentChart(warehouse,mobileUser,season) {
	 var chart = $('#mobileUserToFarmer_AgentChart').esecharts();
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
	    	renderWarehouseMobileUserToFarmer_AgentChart(warehouse,mobileUser,season);
	    },null,hoverState,pressedState).add();
}
	
function populateMobileUserToFarmer_FarmerChart(agentId,branch,title){
	
	jQuery.post("dashboard_populateMobileUserToFarmer_FarmerChart.action", {
		selectedBranch : branch,
		agentId : agentId
	}, function(result) {
		
esecharts.chart('mobileUserToFarmer_FarmerChart', {
			
			title: {
		        text: title+' --> Farmers'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">₹{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y} </b><br/>',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		       data: (function() {
					var res = [];
					json = $.parseJSON(result);
						$.each(json, function(index, value) {
							res.push({	
								"name" : value.farmerName,
								"y" : Number(value.amount),
								"id":value.farmerId
								
							});
						});
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	 populateMobileUserToFarmer_ProductChart(this.id,branch,agentId,this.name);
                	
                 }
             }
         },
         showInLegend: true
		 }]
	});
		
	});
	
}

function populateMobileUserToFarmer_ProductChart(farmerId,branch,agent,title){
	
	jQuery.post("dashboard_populateMobileUserToFarmer_ProductChart.action", {
		selectedBranch : branch,
		agentId : agent,
		farmerId :farmerId
	}, function(result) {
		
esecharts.chart('mobileUserToFarmer_ProductChart', {
			
			title: {
		        text: title+' --> Product'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y} </b><br/>',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		       data: (function() {
					var res = [];
					json = $.parseJSON(result);
						$.each(json, function(index, value) {
							res.push({	
								"name" : value.productNAme,
								"y" : Number(value.amount),
								"id":value.productCode
								
							});
						});
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	
                	
                 }
             }
         },
         showInLegend: true
		 }]
	});
		
		
	});
}

function populateWarehouseToFarmer_WarehouseChart(){
	
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	
	jQuery.post("dashboard_populateWarehouseToFarmer_WarehouseChart.action", {
		selectedBranch : getCurrentBranch()
		
	}, function(WarehouseToFarmer) {
		
		WarehouseToFarmerData = $.parseJSON(WarehouseToFarmer);
esecharts.chart('warehouseToFarmer_warehouseChart', {
	
	 chart: {
	        type: 'column',
	        marginLeft: 100,
	        marginRight: 100,
	  },
	
	  title:{
		 text:'Warehouse to Farmer ' 
	  },
	  xAxis: {
	        type: 'category',
	        title: {
	            text:  "<b>Warehouse</b>"
	        }
	    },
	    yAxis: {
	        title: {
	            text: "Amount"
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
		                format: currencyType +''+'{point.y}'
		            }
		        }
	    },

	    tooltip: {
	    	
	    	headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y}</b> <br/>Click to view the distribution details of <span style="color:{point.color}">{point.name}</span>'
	    },	 
		 series: [{
			 	name: ' ',
		        colorByPoint : true,
		       // allowPointSelect: true,
		       data: (function() {
					var res = [];
					
						
					$.each(WarehouseToFarmerData, function(index, value) {
						if(iteration1 <= 10){
							res.push({	
								"name" : value.servicePointName,
								"y" : Number(value.amount),
								"id":value.servicePointId
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
					/*$.each(json, function(index, value) {
						
							if(iteration1 != 1 && iteration1 != 2  && iteration1 != 3  ){
								res.push({	
									"name" : value.servicePointName,
									"y" : Number(value.amount),
									"id":value.servicePointId
									
								});
							}
								
							
							
								iteration1 = iteration1+1;
								
						
					});*/
					
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	 populateWarehouseToFarmer_FarmerChart(this.id,this.name);
                	 populate_ProductChartByWarehouseToFarmer(this.id,getCurrentBranch(),this.name);
                 }
             }
         },
         showInLegend: true
		 }]
		 
}, function(chart) { 
	 var flag = "hideArrows";
		
	 
	  if(WarehouseToFarmerData.length > 10){
		  flag = "showArrows";
	  } 
 
 if(flag == "showArrows"){
   //chart.renderer.button('<', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_warehouseChart').add();
   //chart.renderer.button('>', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToFarmer_warehouseChart').add();
   chart.xAxis[0].setExtremes(0,9);
 
   var drilldown = dataLength/10;
   
   if($("#arrow_warehouseToFarmer_warehouseChart").length > 0){
	   $("#arrow_warehouseToFarmer_warehouseChart").remove();
	}


	var row = jQuery('<div/>', {
		class : "col-xs-12",
		id	  : "arrow_warehouseToFarmer_warehouseChart"
	});
					    
	$("#warehouseToFarmer_warehouseChart").append($(row));
	$(row).append('<div  class="col-xs-6 leftArrow"><button  type="button" class="btn   left_warehouseToFarmer_warehouseChart "><span class="glyphicon glyphicon-chevron-left"></span></button></div>')   
	$(row).append('<div  class="col-xs-6 rightArrow"><button  type="button" class="btn    right_warehouseToFarmer_warehouseChart"><span class="glyphicon glyphicon-chevron-right"></span> </button></div>')


   $('.left_warehouseToFarmer_warehouseChart').click(function() {
   	if(rightClick > 0){
   	rightCount = rightCount-10;
   	temp1 = rightCount;
		 temp2 = rightCount+9;
		 temp2 = Number(temp2);
		
		
		 var yValues =  new Array();
		 
		 
		 
		 
		 $.each(WarehouseToFarmerData, function(index, value) {
			if(iteration2 >= temp1 ){
				yValues.push({
					name : value.servicePointName,
					y : Number(value.amount),
					id:value.servicePointId
					 });
				 }
				iteration2 = iteration2+1;
		 	});
		 
		
		 
		chart.series[0].setData(eval(yValues), false, true);
		
		 if(yValues.length < 9){
			 chart.xAxis[0].setExtremes(0, yValues.length-1); 
		 }else{
			 chart.xAxis[0].setExtremes(0, 9); 
		 }
		 rightClick = rightClick - 1;
		 iteration2 = 1;
   }
   });
   
   $('.right_warehouseToFarmer_warehouseChart').click(function() {

		   if(rightClick < drilldown-1){
		    if(rightCount == 0){
		    	rightCount = rightCount+1;
		    	rightCount = ""+rightCount+rightCount;
		    	rightCount = Number(rightCount);
		    }else{
		    	rightCount = rightCount+10;
		    }
		   
		    	 temp1 = rightCount;
		    	 if(dataLength >=  Number(rightCount+9)){
		    		 temp2 = Number(rightCount+9);
		    		// temp2 = temp2+10;
		    	 }else{
		    		
		    		 var temp2  = Number(dataLength-(rightCount-1));
		    	 }
		    	 if(temp2 < temp1){
					temp2 = dataLength ;
		    	 }
				
		    	 var yValues =  new Array();
				
		    	  
		    	
						$.each(WarehouseToFarmerData, function(index, value) {
							 if(iteration2 >= temp1 && iteration2 <= (temp2)){
									yValues.push({
										name : value.servicePointName,
										y : Number(value.amount),
										id:value.servicePointId
										 });
									}
								iteration2 = iteration2+1;
						});
						
					
						
						
					
				 chart.series[0].setData(eval(yValues), false, true);
				
				 if(yValues.length <= 9){
					 chart.xAxis[0].setExtremes(0, yValues.length-1); 
				 }else{
					 chart.xAxis[0].setExtremes(0, 9); 
				 }
				
				 rightClick = rightClick+1;
				 iteration2 = 1;
			} 
	})
}
});
	});
}

function populateWarehouseToFarmer_FarmerChart(warehouse,title){
	
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	jQuery.post("dashboard_populateWarehouseToFarmer_FarmerChart.action", {
		selectedBranch : getCurrentBranch(),
		selectedWarehouse : warehouse
		
	}, function(warehouseToFarmer_FarmerChart) {
		warehouseToFarmer_FarmerChart_data = $.parseJSON(warehouseToFarmer_FarmerChart);
		
			var chart = new	esecharts.chart('warehouseToFarmer_FarmerChart', {
			
				 chart: {
				        type: 'pie',
				        marginLeft: 100,
				        marginRight: 100,
				 },
				
			title: {
		        text: title+' --> Farmers'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">'+currencyType+'{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b> '+currencyType+'{point.y} </b><br/>',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		        colorByPoint : true,
		       data: (function() {
					var res = [];
					
					
					$.each(warehouseToFarmer_FarmerChart_data, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.farmerName,
								"y" : Number(value.amount),
								"id":value.farmerId
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(warehouseToFarmer_FarmerChart_data, function(index, value) {
							res.push({	
								"name" : value.farmerName,
								"y" : Number(value.amount),
								"id":value.farmerId
								
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	 populateWarehouseToFarmer_ProductChart(warehouse,this.id,this.name);
                	 
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
		
		  if(warehouseToFarmer_FarmerChart_data.length > 5){
			  flag = "showArrows";
		  } 
	  
	  if(flag == "showArrows"){
		chart.renderer.button('< <p class="warehouseToFarmer_FarmerChart_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    $(".left_warehouseToFarmer_FarmerChart").hide();
	  
		chart.renderer.button('<p class="warehouseToFarmer_FarmerChart_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToFarmer_FarmerChart').add();
	    chart.xAxis[0].setExtremes(0,9);
	  
	    var drilldown = dataLength/5;
	    
	 
	    $('.left_warehouseToFarmer_FarmerChart').click(function() {

	    	if(rightClick > 0){
	    		if(rightClick == 1){
	    			rightCount = rightCount-6;
	    		}else{
	    			rightCount = rightCount-5;
	    		}
	    	
		    	temp1 = rightCount;
	    	
			
			
			 var yValues1 =  new Array();
			
			 iteration2 = 1;
			 $.each(warehouseToFarmer_FarmerChart_data, function(index, value) {
				 if(iteration2 >= temp1 ){
					if(yValues1.length <= 4){
						yValues1.push({
							"name" : value.farmerName,
							"y" : Number(value.amount),
							"id":value.farmerId
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
			 
			 
			 
			 if(rightClick != 0){
				 $('.warehouseToFarmer_FarmerChart_backward').text('< Back  '+Number(rightClick))
			}else{
				 $(".left_warehouseToFarmer_FarmerChart").hide();
			 }
			 $('.warehouseToFarmer_FarmerChart_forward').text('Next  '+Number(rightClick+2)+' >');
			 
			 $('.right_warehouseToFarmer_FarmerChart').show();
	    }
	    	
	    });
	    
	    $('.right_warehouseToFarmer_FarmerChart').click(function() {
	    	
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
			    	
			    	 $.each(warehouseToFarmer_FarmerChart_data, function(index, value) {
			    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
			    			 yValues1.push({
			    				 "name" : value.farmerName,
									"y" : Number(value.amount),
									"id":value.farmerId
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
	    	
	    	
	    	
	    	if(rightClick < drilldown-1){
	    		$('.warehouseToFarmer_FarmerChart_forward').text('Next  '+Number(rightClick+2)+' >');
	    	}else{
	    		$('.right_warehouseToFarmer_FarmerChart').hide();
	    	}
	    	
	    	 $(".left_warehouseToFarmer_FarmerChart").show();
	    	$('.warehouseToFarmer_FarmerChart_backward').text('< Back  '+Number(rightClick))
	    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
	    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    	
	    })
 }
	});
		
	});
	
}

function populateWarehouseToFarmer_ProductChart(warehouse,farmerId,title){
	
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	
	jQuery.post("dashboard_populateWarehouseToFarmer_ProductChart.action", {
		selectedBranch : getCurrentBranch(),
		selectedWarehouse : warehouse,
		farmerId : farmerId
		
	}, function(productChart) {
	var	productChartData = $.parseJSON(productChart);
esecharts.chart('warehouseToFarmer_ProductChart', {
			
			chart: {
		        type: 'pie',
		        marginLeft: 100,
		        marginRight: 100,
		 },
	
			title: {
		        text: title+' --> Product'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		           // allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">'+currencyType+'{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span> <br> Amount : <b> '+currencyType+'{point.y} </b><br/> <br/> {point.custom}',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        
		        allowPointSelect: true,
		        colorByPoint : true,
		       data: (function() {
					var res = [];
					
					$.each(productChartData, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(productChartData, function(index, value) {
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
								
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	// populateWarehouseToFarmer_ProductChart(warehouse,this.id);
                	
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
			
			  if(productChartData.length > 5){
				  flag = "showArrows";
			  } 
		  
		  if(flag == "showArrows"){
			chart.renderer.button('< <p class="warehouseToFarmer_ProductChart_byfarmer_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_ProductChart_byfarmer').add();
		    $(".left_warehouseToFarmer_ProductChart_byfarmer").hide();
		  
			chart.renderer.button('<p class="warehouseToFarmer_ProductChart_byfarmer_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToFarmer_ProductChart_byfarmer').add();
		    chart.xAxis[0].setExtremes(0,9);
		  
		    var drilldown = dataLength/5;
		    
		 
		    $('.left_warehouseToFarmer_ProductChart_byfarmer').click(function() {

		    	if(rightClick > 0){
		    		if(rightClick == 1){
		    			rightCount = rightCount-6;
		    		}else{
		    			rightCount = rightCount-5;
		    		}
		    	
			    	temp1 = rightCount;
		    	
				
				
				 var yValues1 =  new Array();
				
				 iteration2 = 1;
				 $.each(productChartData, function(index, value) {
					 if(iteration2 >= temp1 ){
						if(yValues1.length <= 4){
							yValues1.push({
								"name" : value.farmerName,
								"y" : Number(value.amount),
								"id":value.farmerId
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
				 
				 
				 
				 if(rightClick != 0){
					 $('.warehouseToFarmer_ProductChart_byfarmer_backward').text('< Back  '+Number(rightClick))
				}else{
					 $(".left_warehouseToFarmer_ProductChart_byfarmer").hide();
				 }
				 $('.warehouseToFarmer_ProductChart_byfarmer_forward').text('Next  '+Number(rightClick+2)+' >');
				 
				 $('.right_warehouseToFarmer_ProductChart_byfarmer').show();
		    }
		    	
		    });
		    
		    $('.right_warehouseToFarmer_ProductChart_byfarmer').click(function() {
		    	
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
				    	
				    	 $.each(productChartData, function(index, value) {
				    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
				    			 yValues1.push({
				    				 "name" : value.farmerName,
										"y" : Number(value.amount),
										"id":value.farmerId
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
		    	
		    	
		    	
		    	if(rightClick < drilldown-1){
		    		$('.warehouseToFarmer_ProductChart_byfarmer_forward').text('Next  '+Number(rightClick+2)+' >');
		    	}else{
		    		$('.right_warehouseToFarmer_ProductChart_byfarmer').hide();
		    	}
		    	
		    	 $(".left_warehouseToFarmer_ProductChart_byfarmer").show();
		    	$('.warehouseToFarmer_ProductChart_byfarmer_backward').text('< Back  '+Number(rightClick))
		    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
		    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
		    	
		    })
	 }
		});
		
	});
	
}

function populate_ProductChartByWarehouseToMobileUser(warehouse,branch,title){

var iteration1 = 1;
var iteration2 = 1;
var dataLength = 0;

var rightCount = 0;
var rightClick = 0;
	jQuery.post("dashboard_productChartByWarehouseToMobileUser.action", {
		selectedBranch : branch,
		selectedWarehouse : warehouse
		
	}, function(warehouseToMobileUser_ProductChart_byWarehouse) {
		
	var	warehouseToMobileUser_ProductChart_byWarehouseData = $.parseJSON(warehouseToMobileUser_ProductChart_byWarehouse);
		
		esecharts.chart('WarehouseToMobileUser_ProductChart', {
					
			chart: {
		        type: 'pie',
		        marginLeft: 100,
		        marginRight: 100,
		 },
			
					title: {
				        text: title+'--> Product'
				    },

				    xAxis: {
				    	type: 'category'
				    },
				    
				    plotOptions: {
				        pie: {
				        	name:" ",
				            allowPointSelect: true,
				            cursor: 'pointer',
				            dataLabels: {
				                enabled: true,
				                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">'+currencyType+'{point.y} </span> ',
				            }
				        }
				    },

			    tooltip: {
			        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
			        pointFormat: '<span style="color:{point.color}">{point.name}</span> <br> Amount : <b> '+currencyType+'{point.y} </b><br/> <br/> {point.custom}',
			        backgroundColor: 'rgba(0, 0, 0, 0.85)',
				      style: {
				         color: '#F0F0F0',
				         fontSize: '16px'
				      }
				 }, 
				 
				 series: [{
				        type: 'pie',
				       // allowPointSelect: true,
				       data: (function() {
							var res = [];
							
							$.each(warehouseToMobileUser_ProductChart_byWarehouseData, function(index, value) {
								if(iteration1 <= 5){
									res.push({	
										"name" : value.productName,
										"y" : Number(value.amount),
										"custom":"Quantity : "+value.quantity+" "+value.unit
									});
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
							});
							
								/*$.each(warehouseToMobileUser_ProductChart_byWarehouseData, function(index, value) {
									res.push({	
										"name" : value.productName,
										"y" : Number(value.amount),
										"custom":"Quantity : "+value.quantity+" "+value.unit
									});
								});*/
					return res;
				})(),
				 point:{
		             events:{
		                 click: function (event) {
		                   
		                	// alert(this.id)
		                	// populateWarehouseToMobileUser_ProductChart(this.id,selectedBranch,warehouseCode);
		                	
		                 }
		             }
		         },
		         showInLegend: true
				 }]
			}, function(chart) { 
				 var flag = "hideArrows";
					
				  if(warehouseToMobileUser_ProductChart_byWarehouseData.length > 5){
					  flag = "showArrows";
				  } 
			  
			  if(flag == "showArrows"){
				chart.renderer.button('< <p class="warehouseToMobileUser_ProductChart_byWarehouse_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToMobileUser_ProductChart_byWarehouse').add();
			    $(".left_warehouseToMobileUser_ProductChart_byWarehouse").hide();
			  
				chart.renderer.button('<p class="warehouseToMobileUser_ProductChart_byWarehouse_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToMobileUser_ProductChart_byWarehouse').add();
			    chart.xAxis[0].setExtremes(0,9);
			  
			    var drilldown = dataLength/5;
			    
			 
			    $('.left_warehouseToMobileUser_ProductChart_byWarehouse').click(function() {

			    	if(rightClick > 0){
			    		if(rightClick == 1){
			    			rightCount = rightCount-6;
			    		}else{
			    			rightCount = rightCount-5;
			    		}
			    	
				    	temp1 = rightCount;
			    	
					
					
					 var yValues1 =  new Array();
					
					 iteration2 = 1;
					 $.each(warehouseToMobileUser_ProductChart_byWarehouseData, function(index, value) {
						 if(iteration2 >= temp1 ){
							if(yValues1.length <= 4){
								yValues1.push({
									"name" : value.productName,
									"y" : Number(value.amount),
									"custom":"Quantity : "+value.quantity+" "+value.unit
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
					 
					 
					 
					 if(rightClick != 0){
						 $('.warehouseToMobileUser_ProductChart_byWarehouse_backward').text('< Back  '+Number(rightClick))
					}else{
						 $(".left_warehouseToMobileUser_ProductChart_byWarehouse").hide();
					 }
					 $('.warehouseToMobileUser_ProductChart_byWarehouse_forward').text('Next  '+Number(rightClick+2)+' >');
					 
					 $('.right_warehouseToMobileUser_ProductChart_byWarehouse').show();
			    }
			    	
			    });
			    
			    $('.right_warehouseToMobileUser_ProductChart_byWarehouse').click(function() {
			    	
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
					    	
					    	 $.each(warehouseToMobileUser_ProductChart_byWarehouseData, function(index, value) {
					    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
					    			 yValues1.push({
					    				 "name" : value.productName,
											"y" : Number(value.amount),
											"custom":"Quantity : "+value.quantity+" "+value.unit
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
			    	
			    	
			    	
			    	if(rightClick < drilldown-1){
			    		$('.warehouseToMobileUser_ProductChart_byWarehouse_forward').text('Next  '+Number(rightClick+2)+' >');
			    	}else{
			    		$('.right_warehouseToMobileUser_ProductChart_byWarehouse').hide();
			    	}
			    	
			    	 $(".left_warehouseToMobileUser_ProductChart_byWarehouse").show();
			    	$('.warehouseToMobileUser_ProductChart_byWarehouse_backward').text('< Back  '+Number(rightClick))
			    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
			    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
			    	
			    })
		}
			});
				
			
		
	});
}

function populate_ProductChartByMobileUserToFarmer(agent,branch,title,season,selectedYear){
	//parentMethod
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;
	var selectedYear=$("#finYearFilter").val();
	jQuery.post("dashboard_productChartByMobileUserToFarmer.action", {
		selectedBranch : branch,
		agentId : agent,
		selectedSeason:season,
		selectedFinYear:selectedYear
	}, function(mobileUserToFarmer_ProductChart) {
		mobileUserToFarmer_ProductChartData = $.parseJSON(mobileUserToFarmer_ProductChart);
esecharts.chart('mobileUserToFarmer_ProductChart', {
			
			chart: {
		        type: 'pie',
		        marginLeft: 100,
		        marginRight: 100,
		 },
	
			title: {
		        text: title+' --> Product'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span> <br> Amount : <b> '+currencyType+'{point.y} </b><br/> <br/> {point.custom}',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		       data: (function() {
					var res = [];
					
					
					$.each(mobileUserToFarmer_ProductChartData, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.productNAme,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(mobileUserToFarmer_ProductChartData, function(index, value) {
							res.push({	
								"name" : value.productNAme,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	
                	
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
			
		  if(mobileUserToFarmer_ProductChartData.length > 5){
			  flag = "showArrows";
		  } 
	  
	  if(flag == "showArrows"){
		chart.renderer.button('< <p class="mobileUserToFarmer_ProductChart_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_mobileUserToFarmer_ProductChart').add();
	    $(".left_mobileUserToFarmer_ProductChart").hide();
	  
		chart.renderer.button('<p class="mobileUserToFarmer_ProductChart_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_mobileUserToFarmer_ProductChart').add();
	    chart.xAxis[0].setExtremes(0,9);
	  
	    var drilldown = dataLength/5;
	    
	 
	    $('.left_mobileUserToFarmer_ProductChart').click(function() {

	    	if(rightClick > 0){
	    		if(rightClick == 1){
	    			rightCount = rightCount-6;
	    		}else{
	    			rightCount = rightCount-5;
	    		}
	    	
		    	temp1 = rightCount;
	    	
			
			
			 var yValues1 =  new Array();
			
			 iteration2 = 1;
			 $.each(mobileUserToFarmer_ProductChartData, function(index, value) {
				 if(iteration2 >= temp1 ){
					if(yValues1.length <= 4){
						yValues1.push({
							"name" : value.productNAme,
							"y" : Number(value.amount),
							"id":value.productCode,
							"custom":"Quantity : "+value.quantity+" "+value.unit
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
			 
			 
			 
			 if(rightClick != 0){
				 $('.mobileUserToFarmer_ProductChart_backward').text('< Back  '+Number(rightClick))
			}else{
				 $(".left_mobileUserToFarmer_ProductChart").hide();
			 }
			 $('.mobileUserToFarmer_ProductChart_forward').text('Next  '+Number(rightClick+2)+' >');
			 
			 $('.right_mobileUserToFarmer_ProductChart').show();
	    }
	    	
	    });
	    
	    $('.right_mobileUserToFarmer_ProductChart').click(function() {
	    	
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
			    	
			    	 $.each(mobileUserToFarmer_ProductChartData, function(index, value) {
			    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
			    			 yValues1.push({
			    				 "name" : value.productNAme,
									"y" : Number(value.amount),
									"id":value.productCode,
									"custom":"Quantity : "+value.quantity+" "+value.unit
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
	    	
	    	
	    	
	    	if(rightClick < drilldown-1){
	    		$('.mobileUserToFarmer_ProductChart_forward').text('Next  '+Number(rightClick+2)+' >');
	    	}else{
	    		$('.right_mobileUserToFarmer_ProductChart').hide();
	    	}
	    	
	    	 $(".left_mobileUserToFarmer_ProductChart").show();
	    	$('.mobileUserToFarmer_ProductChart_backward').text('< Back  '+Number(rightClick))
	    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
	    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    	
	    })
}
	});
		
		
	});

	
}


function populate_ProductChartByWarehouseToFarmer(warehouse,branch,title){
	var iteration1 = 1;
	var iteration2 = 1;
	var dataLength = 0;

	var rightCount = 0;
	var rightClick = 0;

	
	jQuery.post("dashboard_productChartByWarehouseToFarmer.action", {
		selectedBranch : getCurrentBranch(),
		selectedWarehouse : warehouse
		
	}, function(productChart_warehouse) {
	var	productChart_warehouseData = $.parseJSON(productChart_warehouse);
esecharts.chart('warehouseToFarmer_ProductChart', {
	 chart: {
	        type: 'pie',
	        marginLeft: 100,
	        marginRight: 100,
	 },		
	
			title: {
		        text: title+' --> Product'
		    },

		    xAxis: {
		    	type: 'category'
		    },
		    
		    plotOptions: {
		        pie: {
		        	name:" ",
		           // allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: <span style="font-size:1em; color: {point.color}; font-weight: bold">'+currencyType+'{point.y} </span> ',
		            }
		        }
		    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	        pointFormat: '<span style="color:{point.color}">{point.name}</span> <br> Amount : <b> '+currencyType+'{point.y} </b><br/> <br/> {point.custom}',
	        backgroundColor: 'rgba(0, 0, 0, 0.85)',
		      style: {
		         color: '#F0F0F0',
		         fontSize: '16px'
		      }
		 }, 
		 
		 series: [{
		        type: 'pie',
		        allowPointSelect: true,
		        colorByPoint : true,
		       data: (function() {
					var res = [];
					
					$.each(productChart_warehouseData, function(index, value) {
						if(iteration1 <= 5){
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
								
							});
								iteration1 = iteration1+1;
								dataLength = dataLength+1;
						}else{
							dataLength = dataLength+1;
						}
					});
					
						/*$.each(productChart_warehouseData, function(index, value) {
							res.push({	
								"name" : value.productName,
								"y" : Number(value.amount),
								"id":value.productCode,
								"custom":"Quantity : "+value.quantity+" "+value.unit
								
							});
						});*/
			return res;
		})(),
		 point:{
             events:{
                 click: function (event) {
                   
                	// alert(this.id)
                	// populateWarehouseToFarmer_ProductChart(warehouse,this.id);
                	
                 }
             }
         },
         showInLegend: true
		 }]
	}, function(chart) { 
		 var flag = "hideArrows";
			
		  if(productChart_warehouseData.length > 5){
			  flag = "showArrows";
		  } 
	  
	  if(flag == "showArrows"){
		chart.renderer.button('< <p class="warehouseToFarmer_ProductChart_bywarehouse_backward"  >Back  </p>', chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_ProductChart_bywarehouse').add();
	    $(".left_warehouseToFarmer_ProductChart_bywarehouse").hide();
	  
		chart.renderer.button('<p class="warehouseToFarmer_ProductChart_bywarehouse_forward" >Next 2 </p> >', chart.plotLeft + chart.plotWidth + 30, chart.plotHeight + chart.plotTop).addClass('right_warehouseToFarmer_ProductChart_bywarehouse').add();
	    chart.xAxis[0].setExtremes(0,9);
	  
	    var drilldown = dataLength/5;
	    
	 
	    $('.left_warehouseToFarmer_ProductChart_bywarehouse').click(function() {

	    	if(rightClick > 0){
	    		if(rightClick == 1){
	    			rightCount = rightCount-6;
	    		}else{
	    			rightCount = rightCount-5;
	    		}
	    	
		    	temp1 = rightCount;
	    	
			
			
			 var yValues1 =  new Array();
			
			 iteration2 = 1;
			 $.each(productChart_warehouseData, function(index, value) {
				 if(iteration2 >= temp1 ){
					if(yValues1.length <= 4){
						yValues1.push({
							"name" : value.productName,
							"y" : Number(value.amount),
							"id":value.productCode,
							"custom":"Quantity : "+value.quantity+" "+value.unit
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
			 
			 
			 
			 if(rightClick != 0){
				 $('.warehouseToFarmer_ProductChart_bywarehouse_backward').text('< Back  '+Number(rightClick))
			}else{
				 $(".left_warehouseToFarmer_ProductChart_bywarehouse").hide();
			 }
			 $('.warehouseToFarmer_ProductChart_bywarehouse_forward').text('Next  '+Number(rightClick+2)+' >');
			 
			 $('.right_warehouseToFarmer_ProductChart_bywarehouse').show();
	    }
	    	
	    });
	    
	    $('.right_warehouseToFarmer_ProductChart_bywarehouse').click(function() {
	    	
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
			    	
			    	 $.each(productChart_warehouseData, function(index, value) {
			    		 if(iteration2 >= temp1 && iteration2 <= (temp2)){
			    			 yValues1.push({
			    				 "name" : value.productName,
									"y" : Number(value.amount),
									"id":value.productCode,
									"custom":"Quantity : "+value.quantity+" "+value.unit
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
	    	
	    	
	    	
	    	if(rightClick < drilldown-1){
	    		$('.warehouseToFarmer_ProductChart_bywarehouse_forward').text('Next  '+Number(rightClick+2)+' >');
	    	}else{
	    		$('.right_warehouseToFarmer_ProductChart_bywarehouse').hide();
	    	}
	    	
	    	 $(".left_warehouseToFarmer_ProductChart_bywarehouse").show();
	    	$('.warehouseToFarmer_ProductChart_bywarehouse_backward').text('< Back  '+Number(rightClick))
	    	//chart.renderer.button('Level '+Number(rightClick+1)+' >');
	    //	chart.renderer.button('< Back '+rightClick, chart.plotLeft - 60, chart.plotHeight + chart.plotTop).addClass('left_warehouseToFarmer_FarmerChart').add();
	    	
	    })
}
	});
		
	});
	

	
}

function sowingSegregateValues(branch){

	var season = $("#sowingseasonFilter").val();
	jQuery.post("dashboard_populateSowingSegregate.action",{selectedBranch:branch,selectedSeason : season},function(result) {
		json = $.parseJSON(result);
		if(!isEmpty(json)){
		$.each(json, function(index, value) {
			renderFarmerSowingSegregate(value.branch,value.warehouse,value.mobileUser);
		});
	   }
	});
	
	
	}


function farmerByLocationChart_barFactive(status){
	if (!isEmpty(getCurrentBranch())) {
		branch = getCurrentBranch();
	}
	
	jQuery.post("farmer_populateFarmersByLocationandStatus.action",{selectedStatus:status},function(result) {
		json = $.parseJSON(result);
		if(!isEmpty(json)){
		$.each(json, function(index, value) {
			renderFarmerByLocationChart(value.branch,value.country,value.state,value.locality,value.municipality,value.gramPanchayat,value.village,value.farmerDetails,value.getGramPanchayatEnable);
		});
	   }
	});
	
	
}	
function renderFarmerSowingSegregate(branch,warehouse,mobileUser){
	//alert(branch);
	var iteration1 = 1;
	var dataLength =0;
	var tenantId = getCurrentTenantId();
	//var locationTitleArray;
	var chart = new	esecharts.chart('sowingSegregateValues', {
	    chart: {
	        type: 'column',
	        marginLeft: 100,
	        marginRight: 100,
	        events: {
	            drilldown: function(e) {
	            	codeForCropChart = e.point.id;
	            	locationCode =  e.point.id; 
	            	locationCode_ForDrillUp[drillUp_count]=e.point.id; 
	            	drillUp_count = drillUp_count+1;
	       
	            },
	            drillup: function(e) {
	            	
	            	if(drillUp_count != 0 && drillUp_count != 1){
	            		var locationCode = locationCode_ForDrillUp[drillUp_count-2];
	            	}
	            	drillUp_count = drillUp_count-1;
	            	codeForCropChart = locationCode;
	            	
	            	
	            }
	        }
	      
	    },
	    title: {
	        text:'Plotting Area for Sowing'
	        	
	    },
	   
	    xAxis: {
	        type: 'category',
	       
	    },
	   /* yAxis: {
	        title: {
	            text: "Area"
	        }

	    },*/
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	 
		        series: {
		           allowPointSelect: false,
		            dataLabels: {
		                enabled: true,
		                format : '{point.y}  <b>({point.units})</b>',

		            }
		        }
	    },

	    tooltip: {
	        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
	       	pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>({point.units}) <br/>'
	    },
	    series: [{
	        name: 'Area',
	        colorByPoint: true,
	        data : (function() {
	        	//var res = [];
						var res = [];
						if(isEmpty(getCurrentBranch())){
							$.each(branch, function(index, branch) {
								if(iteration1 <= 10){
								
									res.push({
										name :branch.branchName,
										y : Number(branch.acres),
										id :branch.branchId,
										units : 'Hectare'
									 }
									
								);
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
								
							});
						
						}
						else{
							$.each(warehouse, function(index, warehouse) {
								if(iteration1 <= 10){
									res.push({
											name :warehouse.warehouseName,
											y : warehouse.acres,
											drilldown:warehouse.warehouseCode,
											id : warehouse.profileId,
											units : 'Hectare'
										 }
									);
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
							});
						}
						return res;
					})(),
					point : {
						events : {
							click : function(event) {
								if(isEmpty(getCurrentBranch())){
								loadSowingSegrateValues(this.id,warehouse,mobileUser,branch);
								}
								else{
									loadMobSowingSegrateValues(this.id,mobileUser,branch,warehouse);
									}
							
							}
						}
					}
					
				 },
	    
	    {
	        name: 'Count',
	        colorByPoint: true,
	        data : (function() {
	        	//var res = [];
						var res = [];
						if(isEmpty(getCurrentBranch())){
							$.each(branch, function(index, branch) {
								if(iteration1 <= 10){
									res.push({
										name :branch.branchId,
										y : Number(branch.count),
										//drilldown:'2',
										id : branch.branchId,
										units : 'Nos'
									 }
								);
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
							});
							
						}
						else{
							$.each(warehouse, function(index, warehouse) {
								if(iteration1 <= 10){
									res.push({
											name :warehouse.warehouseName,
											y : warehouse.count,
											drilldown:warehouse.warehouseCode,
											id : warehouse.profileId,
											units : 'Nos'
										 });
										iteration1 = iteration1+1;
										dataLength = dataLength+1;
								}else{
									dataLength = dataLength+1;
								}
							});
						}
						return res;
					})(),
					point : {
								events : {
									click : function(event) {
										if(isEmpty(getCurrentBranch())){
											loadSowingSegrateValues(this.id,warehouse,mobileUser,branch);
											}
											else{
												loadMobSowingSegrateValues(this.id,mobileUser,branch,warehouse);
												}
										
									}
								}
							}
				 }
	    
	    ],
					drilldown: {}
	});
}

