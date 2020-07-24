/*
 * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.DynamicReportProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("unchecked")
public class ICSSummaryReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	


	private String gridIdentity;
	private Map<String, String> warehouseMap = new HashMap<>();
	private String filterList;
	private String seasonCode;

	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	
	int serialNo = 1;
	private String daterange;
	private String tenantId;	
	private String farmerId;
	private String name;
	
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String headerFields;
	private String isKpfEnabled;
	private String branch;
	private String selectedBranch;
	private String gramPanchayatEnable;
	private String codeForCropChart;
	private String locationLevel;
	private String selectedSeason;
	
	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));
		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		return LIST;
		
		
	}

	public void populateFarmersByLocation() {
		List<JSONObject> farmersByLocation = new ArrayList<JSONObject>();
		List<JSONObject> branch = new ArrayList<JSONObject>();
		List<JSONObject> gramPanchayat = new ArrayList<JSONObject>();
		List<JSONObject> village = new ArrayList<JSONObject>();
		
		List<Object[]> farmersByBranch  = null;
		
		farmersByBranch = farmerService.farmersByBranch();
		/*if(StringUtil.isEmpty(selectedSeason)){
			farmersByBranch = farmerService.farmersByBranch();
		if (StringUtil.isEmpty(getSelectedBranch())) {
			farmersByBranch = farmerService.farmersByBranch();
		}else{
			farmersByBranch = farmerService.farmersByBranch(getSelectedBranch());
		}
		}else{
			if (StringUtil.isEmpty(getSelectedBranch())) {
			 farmersByBranch = farmerService.farmersByBranchandSeason(selectedSeason);
		}else{
			farmersByBranch = farmerService.farmersByBranchandSeason(getSelectedBranch(),selectedSeason);
		}
		}*/
		

		farmersByBranch.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("branchName", objArr[1]);
			jsonObj.put("branchId", objArr[2]);
			branch.add(jsonObj);
		});
		
		
		List<Object[]> farmersByCountry = null;
		/*if(StringUtil.isEmpty(selectedSeason)){*/
		if (StringUtil.isEmpty(getSelectedBranch())) {
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1")) {
			farmersByCountry = farmerService.farmersByCountry(EMPTY);
		} else {
			farmersByCountry = farmerService.farmersByGroup(getSelectedBranch());
		}
		}
		else {
			farmersByCountry = farmerService.farmersByGroup(getSelectedBranch());
		}
		/*}else{
			if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1")) {
				farmersByCountry = farmerService.farmersByCountryAndSeason(EMPTY,selectedSeason);
				} else {
				farmersByCountry = farmerService.farmersByGroupAndSeason(getSelectedBranch(),selectedSeason);
			}
		}*/
		List<JSONObject> country = new ArrayList<JSONObject>();
		farmersByCountry.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("countryName", objArr[1]);
			jsonObj.put("countryCode", objArr[2]);
			jsonObj.put("branchId", objArr[3]);
			jsonObj.put("countryId", objArr[4]);
			country.add(jsonObj);
		});

		List<Object[]> farmersByICS = null;
		/*if(StringUtil.isEmpty(selectedSeason)){*/
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByICS = farmerService.farmersByState(EMPTY);
		else
			farmersByICS = farmerService.findFarmerCountByGroupICS(getSelectedBranch());
		/*}else{
			farmersByICS = farmerService.findFarmerCountByGroupICS(getSelectedBranch(),selectedSeason);
		}*/
		
		
		List<JSONObject> state = new ArrayList<JSONObject>();
		farmersByICS.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("icsType", objArr[1]);
			jsonObj.put("samithiName", objArr[2]);
			jsonObj.put("samithiCode", objArr[3]);
			jsonObj.put("icsCode", objArr[4]);
			jsonObj.put("samithiId", objArr[5]);
			state.add(jsonObj);
		});

		List<Object[]> farmersByLocality;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByLocality = farmerService.farmersByLocality(EMPTY);
		else
			farmersByLocality = farmerService.farmersByLocality(getSelectedBranch());
		List<JSONObject> locality = new ArrayList<JSONObject>();
		farmersByLocality.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("localityName", objArr[1]);
			jsonObj.put("localityCode", objArr[2]);
			jsonObj.put("stateCode", objArr[3]);
			locality.add(jsonObj);
		});

		List<Object[]> farmersByMunicipality;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmersByMunicipality = farmerService.farmersByMunicipality(EMPTY);
		else
			farmersByMunicipality = farmerService.farmersByMunicipality(getSelectedBranch());
		List<JSONObject> municipality = new ArrayList<JSONObject>();
		farmersByMunicipality.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", objArr[0]);
			jsonObj.put("municipalityName", objArr[1]);
			jsonObj.put("municipalityCode", objArr[2]);
			jsonObj.put("localityCode", objArr[3]);
			municipality.add(jsonObj);
		});

	
			List<Object[]> farmersByVillage;
			if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
				farmersByVillage = farmerService.farmersByVillageWithOutGramPanchayat(EMPTY);
			else
				farmersByVillage = farmerService.farmersByVillageWithOutGramPanchayat(getSelectedBranch());
			farmersByVillage.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("count", objArr[0]);
				jsonObj.put("villageName", objArr[1]);
				jsonObj.put("villageCode", objArr[2]);
				jsonObj.put("municipalityCode", objArr[3]);
				village.add(jsonObj);
			});
		

		List<Object[]> farmerDetailsByVillage;
		if (getIsMultiBranch().equalsIgnoreCase("1") && getIsParentBranch().equalsIgnoreCase("1"))
			farmerDetailsByVillage = farmerService.farmerDetailsByVillage(EMPTY);
		else
			farmerDetailsByVillage = farmerService.farmerDetailsByVillage(getSelectedBranch());
		List<JSONObject> farmerDetails = new ArrayList<JSONObject>();
		farmerDetailsByVillage.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("totalCount", objArr[0]);
			jsonObj.put("active", objArr[1] == null ? 0 : objArr[1]);
			jsonObj.put("inActive", objArr[2] == null ? 0 : objArr[2]);
			jsonObj.put("certified", objArr[3] == null ? 0 : objArr[3]);
			jsonObj.put("nonCertified", objArr[4] == null ? 0 : objArr[4]);
			jsonObj.put("villageCode", objArr[5] == null ? 0 : objArr[5]);
			farmerDetails.add(jsonObj);
		});

		JSONObject final_jsonObject = new JSONObject();
		if (StringUtil.isEmpty(getSelectedBranch())) {
			final_jsonObject.put("branch", branch);
		}else{
			final_jsonObject.put("branch", getSelectedBranch());
		}
		final_jsonObject.put("country", country);
		final_jsonObject.put("state", state);
		final_jsonObject.put("locality", locality);
		final_jsonObject.put("municipality", municipality);
		
		final_jsonObject.put("village", village);
		final_jsonObject.put("farmerDetails", farmerDetails);
	

		farmersByLocation.add(final_jsonObject);

		printAjaxResponse(farmersByLocation, "text/html");
	}
	public void populateFarmerLocationCropChart() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> cropChartData = farmerService.populateFarmerLocationCropChartByGroup(getCodeForCropChart(),getSelectedBranch(),selectedSeason);
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.SUSAGRI)){
		cropChartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmCount", objArr[0]);
			
			String area="0.00";
			DecimalFormat df = new DecimalFormat("0.00");
			if( objArr[1] != null && !StringUtil.isEmpty(objArr[1].toString())){
				 area = (df.format(Double.valueOf(objArr[1].toString())));
			}

			jsonObj.put("Area", area);
			jsonObj.put("productName", objArr[2]);
			jsonObj.put("productCode", objArr[3]);
			jsonObj.put("yield",objArr[4]);
			jsonObj.put("Yield", "yield");
			jsonObj.put("Metricton", "Kgs");
			jsonList.add(jsonObj);
		});
		}
		else{
			cropChartData.stream().forEach(obj -> {
				Object[] objArr = (Object[]) obj;
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("farmCount", objArr[0]);

				DecimalFormat df = new DecimalFormat("0.00");
				String area = (df.format(Double.valueOf(objArr[1].toString())));

				jsonObj.put("Area", area);
				jsonObj.put("productName", objArr[2]);
				jsonObj.put("productCode", objArr[3]);
				jsonObj.put("yield",objArr[4]);
				jsonObj.put("Yield", "yield");
				jsonObj.put("Metricton", "MT");
				jsonList.add(jsonObj);
			});
		}
		printAjaxResponse(jsonList, "text/html");
	}

	public void populateFarmerBycrop() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> cropChartData = farmerService.populateFarmerCropCountChartByGroup(selectedSeason,getCodeForCropChart(),getSelectedBranch());

		cropChartData.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("farmerCount", objArr[0]);
			jsonObj.put("productName", objArr[1]);
			jsonObj.put("productCode", objArr[2]);
			
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}
	public void estimatedAndActualYield() {

		List<Object[]> estimatedYield = farmerService.estimatedYield(getCodeForCropChart());
		List<Object[]> actualYield = farmerService.actualYield(getCodeForCropChart());
		Map<String, String> estimated_map = new HashMap<String, String>();
		Map<String, String> actual_map = new HashMap<String, String>();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();

		for (Object[] objects : actualYield) {
			String val = (String) actual_map.get((String) objects[1]);
			if (StringUtil.isEmpty(val)) {
				actual_map.put((String) objects[1], objects[0] + "," + objects[2]);
			} else {
				String existingVal[] = val.split(",");
				double actual = Double.parseDouble(String.valueOf(existingVal[1] == null ? "0" : existingVal[1]))
						+ Double.parseDouble(objects[2] == null ? "0" : String.valueOf(objects[2]));
				actual_map.put((String) objects[1], objects[0] + "," + actual);
			}
		}

		for (Object[] objects : actualYield) {
			for (Object[] objects2 : estimatedYield) {
				if (((String) objects[1]).equalsIgnoreCase((String) objects2[1])) {

					String val = (String) estimated_map.get((String) objects2[1]);
					if (StringUtil.isEmpty(val)) {
						estimated_map.put((String) objects2[1], objects2[0] + "," + objects2[2]);
					} else {
						String existingVal[] = val.split(",");
						double estimated = Double
								.parseDouble(String.valueOf(existingVal[1] == null ? "0" : existingVal[1]))
								+ Double.parseDouble(objects2[2] == null ? "0" : String.valueOf(objects2[2]));
						estimated_map.put((String) objects2[1], objects2[0] + "," + estimated);
					}
				}
			}
		}

		for (Map.Entry<String, String> entry : actual_map.entrySet()) {
			// System.out.println("Key = " + entry.getKey() +", Value = " +
			// entry.getValue());
			String estimatedYield_val = (String) estimated_map.get((String) entry.getKey());

			String actualValue[] = (entry.getValue()).split(",");
			String estimatedValue[] = estimatedYield_val.split(",");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("productName", actualValue[0]);
			jsonObj.put("productCode", entry.getKey());
			jsonObj.put("estimated", estimatedValue[1]);
			jsonObj.put("actual", actualValue[1]);
			jsonList.add(jsonObj);

		}

	

		printAjaxResponse(jsonList, "text/html");
	}
	public void getFarmDetailsAndProposedPlantingArea() {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Object[]> farmDetailsAndProposedPlantingArea = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			farmDetailsAndProposedPlantingArea = farmerService.getFarmDetailsAndProposedPlantingAreaByGroup(getLocationLevel(),
					getSelectedBranch(), getGramPanchayatEnable());
	

		farmDetailsAndProposedPlantingArea.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("FarmCount", objArr[0] == null ? 0 : objArr[0]);

			DecimalFormat df = new DecimalFormat("0.00");
			String totalArea = (df.format(Double.valueOf(objArr[1].toString())));

			jsonObj.put("Area", objArr[1] == null ? 0 : totalArea);
			jsonObj.put("locationName", objArr[2]);
			// jsonObj.put("locationCode", objArr[3]);
			jsonList.add(jsonObj);
		});
		printAjaxResponse(jsonList, "text/html");
	}

	public String detail() throws Exception {
		return null;
		
	}

	public JSONObject toJSON(Object obj) {
		return null;
		}



	public String populateXLS() throws Exception {
		return null;
		
	}

	
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		return null;}



	@SuppressWarnings("rawtypes")
	public InputStream getKpfExportDataStream(String exportType) throws IOException {
		return null;
		
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public int getPicIndex(XSSFWorkbook workbook) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = workbook.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	
	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		}
		return warehouseMap;
	}

	
	

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	
	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}

	
	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	
	
	

	
	public void populateHeaderFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Object[] datas=null;
		String hqlQuery=null;
		String hqlQueryTrim="";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isEmpty(filterList)) {
			Type listType1 = new TypeToken<List<FilterFieldData>>() {
			}.getType();
			List<FilterFieldData> filtersList = new Gson().fromJson(filterList, listType1);
			for(FilterFieldData filterFieldData:filtersList)
			{
				if(filterFieldData.getName().equalsIgnoreCase("seasonCode"))
				{
					hqlQuery+= " AND p."+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
				else
				{
					hqlQuery+= " AND "+filterFieldData.getName()+"=:"+filterFieldData.getLabel();
					params.put(filterFieldData.getLabel(), filterFieldData.getValue());
				}
			}
			
		}
		if(!StringUtil.isEmpty(hqlQuery))
		{
			hqlQueryTrim=hqlQuery.replace("null", "");
		}
		//datas = farmerService.findTotalAmtAndweightByProcurement(hqlQueryTrim,params);
		datas = farmerService.findTotalAmtAndweightByProcurementWithDate(hqlQueryTrim,params,DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),
			    DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT),branch);
		
		jsonObject.put("totalNetwt", !StringUtil.isEmpty(datas[0]) ? datas[0] : "0.0");
		jsonObject.put("txnAmount", !StringUtil.isEmpty(datas[2]) ? datas[2] : "0.0");
		jsonObject.put("totalPayAmt", !StringUtil.isEmpty(datas[1]) ? datas[1] : "0.0");
		jsonArray.add(jsonObject);

		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());

	}
	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}

	public String getIsKpfEnabled() {
		return isKpfEnabled;
	}

	public void setIsKpfEnabled(String isKpfEnabled) {
		this.isKpfEnabled = isKpfEnabled;
	}
	


	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectedBranch() {
		return selectedBranch;
	}

	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}

	public String getGramPanchayatEnable() {
		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {
		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public String getCodeForCropChart() {
		return codeForCropChart;
	}

	public void setCodeForCropChart(String codeForCropChart) {
		this.codeForCropChart = codeForCropChart;
	}

	public String getLocationLevel() {
		return locationLevel;
	}

	public void setLocationLevel(String locationLevel) {
		this.locationLevel = locationLevel;
	}
	public String getAreaType() {
		String result = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences) && preferences.getPreferences().get(ESESystem.HECTARE) != null) {
			result = preferences.getPreferences().get(ESESystem.HECTARE);
			result = result.contains("-") ? result.split("-")[1] : result;

		}
		return !StringUtil.isEmpty(result) ? result : ESESystem.HECTARE;

	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}
	
}