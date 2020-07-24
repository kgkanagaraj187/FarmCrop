package com.sourcetrace.esesw.view.report.agro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.view.BaseReportAction;

@SuppressWarnings("serial")
public class IcsFarmerDairyReportAction extends BaseReportAction {

	@Autowired
	private IPreferencesService preferncesService;

	@Autowired
	private IFarmerService farmerService;

	private List<Object[]> dynamicSectionConfigHeader;

	private FarmerDynamicData filter;

	private String mainGridCols;
	private String exportLimit;
	private String daterange;
	private String filterList;
	private String gridIdentity;

	private Farm farm;
	List<Object[]> dynamicData = new LinkedList<>();
	private String farmerId;
	private String farmId;

	private List<Object[]> farmerList;
	private List<Object[]> farmList;
	private FarmerDynamicData icsFarmerDairy;
	
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		daterange = super.startDate + " - " + super.endDate;

		formMainGridCols();

		request.setAttribute(HEADING, getText(LIST));

		return LIST;
	}

	private void formMainGridCols() {
		dynamicSectionConfigHeader = farmerService.findDynamicSectionFieldsByTxnType("384");
		mainGridCols = getLocaleProperty("date") + "#" + getLocaleProperty("farmer") + "#" + getLocaleProperty("farm") + "#";
		//mainGridCols = getLocaleProperty("farmer") + "#" + getLocaleProperty("farm") + "#";

		if (!ObjectUtil.isEmpty(dynamicSectionConfigHeader)) {
			for (Object[] dsc : dynamicSectionConfigHeader) {
				String label = dsc[2].toString();
				mainGridCols += getLocaleProperty(label) + "#";
			}

		}
	}

	public String data() throws Exception {
		FarmerDynamicData fdfv = new FarmerDynamicData();
	
		setFilter(new FarmerDynamicData());
		super.filter = this.filter;

		Map data = readData();

		setGridIdentity(IReportDAO.MAIN_GRID);
		return sendJSONResponse(data);

	}
	
	public String detail() throws Exception {

		String view = "";
		if (id != null && !id.equals("")) {
			icsFarmerDairy = farmerService.findFarmerDynamicData(id);
			if (icsFarmerDairy == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
		//	command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		FarmerDynamicData farmerDynamicData = (FarmerDynamicData) obj;
		JSONArray rows = new JSONArray();

		DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
		
		rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate())? genDate.format(farmerDynamicData.getDate()):"");
		
	
		farm = farmerService.findFarmByFarmCode(farmerDynamicData.getReferenceId());
		if (!ObjectUtil.isEmpty(farm)) {
			rows.add(farm.getFarmer().getFirstName());
			rows.add(farm.getFarmName());
		}
		
		Map<String, String> valuesMap = new LinkedHashMap<>();
		farmerDynamicData.getFarmerDynamicFieldsValues().stream().forEach(dynamicFieldValues -> {
			valuesMap.put(dynamicFieldValues.getFieldName(), dynamicFieldValues.getFieldValue());
		});

		getDynamicData().stream().forEach(secCode -> {
			if (valuesMap.containsKey(secCode)) {
				rows.add(valuesMap.get(secCode));
			} else {
				rows.add("");
			}
		});
		/*
		 * farmerDynamicData.getFarmerDynamicFieldsValues().stream().forEach(
		 * dynamicFieldValues -> { if
		 * (getDynamicData().contains(dynamicFieldValues.getFieldName())) {
		 * rows.add(dynamicFieldValues.getFieldValue()); } });
		 */
		jsonObject.put("id", farmerDynamicData.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public List<Object[]> getDynamicSectionConfigHeader() {
		return dynamicSectionConfigHeader;
	}

	public void setDynamicSectionConfigHeader(List<Object[]> dynamicSectionConfigHeader) {
		this.dynamicSectionConfigHeader = dynamicSectionConfigHeader;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public List<String> getDynamicData() {
		List<String> secCodesList = new LinkedList<>();
		if (ObjectUtil.isListEmpty(dynamicData)) {
			dynamicData = farmerService.findDynamicSectionFieldsByTxnType("384");
		}
		dynamicData.stream().forEach(secCodes -> {
			secCodesList.add(String.valueOf(secCodes[1]));
		});
		return secCodesList;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public FarmerDynamicData getFilter() {
		return filter;
	}

	public void setFilter(FarmerDynamicData filter) {
		this.filter = filter;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public Map<String, String> getFarmersList() {

		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	
	}
	
	public Map<String, String> getFarmsList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}

}
