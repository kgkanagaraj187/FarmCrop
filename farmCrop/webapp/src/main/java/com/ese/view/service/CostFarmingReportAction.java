package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.order.entity.txn.CostFarming;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.report.agro.WarehouseStockReportAction;

@SuppressWarnings("unchecked")
public class CostFarmingReportAction extends BaseReportAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WarehouseStockReportAction.class);
	private Map<String, String> fields = new LinkedHashMap<String, String>();
	

	private CostFarming filter;
	private IFarmerService farmerService;
	@Autowired
	private IPreferencesService preferncesService;

	//private String branchIdParma;
	private String daterange;
	private String exportLimit;
	private String researchId;
	private String farmerId;
	private String farmId;
	private String cowId;
	private String receiptNo;
	private String elitype;
	private List<CostFarming> costFarmingList;
	private List<CostFarming> costFarmingFilter = new LinkedList<>();
	private CostFarming costFarming;
	
	/**
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;

		setFilter(costFarming);
		request.setAttribute("fields", fields);
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	public String data() throws Exception {
		super.filter = this.filter;
		Map data = readData("costfarming");
		return sendJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {

		Object[] costFarm = (Object[]) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(branchesMap.get(costFarm[1]));
		}
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(!ObjectUtil.isEmpty(genDate.format(costFarm[3]))
					? genDate.format(costFarm[3]).toString() : "");
		}
		Cow cow=farmerService.findCowByCowId(Long.valueOf(String.valueOf(costFarm[0])));
		if(!ObjectUtil.isEmpty(cow))
		{
			rows.add(ObjectUtil.isEmpty(cow.getResearchStation()) ? "NA" : cow.getResearchStation().getName());
			rows.add(ObjectUtil.isEmpty(cow.getFarm()) ? "NA" :cow.getFarm().getFarmer().getFarmerId() +"-"+cow.getFarm().getFarmer().getFirstName());
			rows.add(ObjectUtil.isEmpty(cow.getFarm()) ? "NA" : cow.getFarm().getFarmName());
			if (!ObjectUtil.isEmpty(cow) && !StringUtil.isEmpty(cow.getId())) {
				rows.add(StringUtil.isEmpty(cow.getCowId()) ? "NA" : cow.getCowId());
			}
		}	
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[5])) ? "NA" : String.valueOf(costFarm[5]));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[6])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[6].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[7])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[7].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[8])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[8].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[9])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[9].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[10])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[10].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[11])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[11].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[12])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[12].toString()), "##.00"));
		rows.add(StringUtil.isEmpty(String.valueOf(costFarm[13])) ? "NA"
				: CurrencyUtil.getDecimalFormat(Double.valueOf(costFarm[13].toString()), "##.00"));

		jsonObject.put("id", String.valueOf(costFarm[0]));
		jsonObject.put("cell", rows);
		return jsonObject;
	}
	
	public String detail()
	{
		if(!StringUtil.isEmpty(id))
		{
			costFarmingList=farmerService.findByCowList(Long.valueOf(id));
			setCostFarming(costFarmingList.get(0));
			
		}
		
		return DETAIL;
	}

	public CostFarming getFilter() {
		return filter;
	}

	public void setFilter(CostFarming filter) {
		this.filter = filter;
	}

	
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/*public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}*/

	public boolean isAlpha(String name) {
		return name.matches("[a-zA-Z ]+");
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFarmersList() 
	{
		try{
		return getCostFarmingFilter().stream().filter(costFarming->!ObjectUtil.isEmpty(costFarming.getCow().getFarm())).collect(Collectors.toMap(costFarming->costFarming.getCow().getFarm().getFarmer().getFirstName(),costFarming->costFarming.getCow().getFarm().getFarmer().getFirstName()));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, String> getResearchList() {

		List<Object[]> researchList = farmerService.findByResearchStationList();

		return researchList.stream().filter(obj -> !ObjectUtil.isEmpty(obj))
				.collect(Collectors.toMap(objKey -> String.valueOf(objKey[0]), objVal -> String.valueOf(objVal[1])));

	}

	public Map<Long, String> getCowList() {
		return getCostFarmingFilter().stream().collect(
                Collectors.toMap(costFarming -> costFarming.getCow().getId(), costFarming -> costFarming.getCow().getCowId()));
	}

	public Map<String, String> getReceiptList() {

		return getCostFarmingFilter().stream().collect(
                Collectors.toMap(CostFarming::getReceiptNo,CostFarming::getReceiptNo));
	}

	public Map<String, String> getTypeList() {

		Map<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("0", getText("elitType0"));
		typeMap.put("1", getText("elitType1"));
		return typeMap;

	}

	public List<CostFarming> getCostFarmingFilter() {
		if (costFarmingFilter.size() <= 0) {
			costFarmingFilter = farmerService.listCostFarming();
		}
		return costFarmingFilter;
	}

	public String getResearchId() {
		return researchId;
	}

	public void setResearchId(String researchId) {
		this.researchId = researchId;
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

	public String getCowId() {
		return cowId;
	}

	public void setCowId(String cowId) {
		this.cowId = cowId;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getElitype() {
		return elitype;
	}

	public void setElitype(String elitype) {
		this.elitype = elitype;
	}

	public void setCostFarmingFilter(List<CostFarming> costFarmingFilter) {
		this.costFarmingFilter = costFarmingFilter;
	}

	public List<CostFarming> getCostFarmingList() {
		return costFarmingList;
	}

	public void setCostFarmingList(List<CostFarming> costFarmingList) {
		this.costFarmingList = costFarmingList;
	}

	public CostFarming getCostFarming() {
		return costFarming;
	}

	public void setCostFarming(CostFarming costFarming) {
		this.costFarming = costFarming;
	}

	public Map<String, String> getFields() {
		fields.put("1",getText("collDate"));
		fields.put("2",getText("cow.researchStationName"));
		fields.put("3",getText("farmerName"));
		fields.put("4",getText("receiptInfor"));
		fields.put("5",getText("cowId"));
		fields.put("6",getText("elitType"));
		if (ObjectUtil.isEmpty(getBranchId())) {
			fields.put("7",getText("app.branch"));
		}
		return fields;
	}
	
	
}
