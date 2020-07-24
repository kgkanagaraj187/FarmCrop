
package com.ese.view.service;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.traceability.BaleGeneration;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.WebTransactionAction;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class BaleGenerationAction extends WebTransactionAction {
	private static final long serialVersionUID = 1;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	private String branchIdParma;
	private String searchPage;
	private BaleGeneration baleGeneration;
	private String seasonCode;
	private Long seasonId;
	private List<String> fields = new ArrayList<String>();
	private BaleGeneration filter;
	private String warehouseId;
	private String heapCode;
    private String productTotalString;
    private String selectedDate;
    private String selectedGinningCode;
    private String selectedHeapCode;
    private String selectedGinnDate;
    private String selectedSea;
    
	@SuppressWarnings("rawtypes")
	public String data() throws Exception {
		return null;
	}

	public JSONObject toJSON(Object obj) {
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Object[] results = (Object[]) obj;

		jsonObject.put("id", "");
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public String detail() {
		return "detail";
	}

	public String getBranchIdParma() {
		return this.branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getSeasonCode() {
		return this.seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public List<String> getFields() {
		return this.fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public String create() throws ParseException, OfflineTransactionException, IOException {
		String result = "";
		if (productTotalString == null) {
			result = "input";
		} else {
			
			String[] productTotalArray = productTotalString.trim().split("\\|");
			for (int i = 0; i < productTotalArray.length; i++) {
				baleGeneration = new BaleGeneration();
				String[] productDetail = productTotalArray[i].split("#");
				baleGeneration.setGinning(locationService.findGinnerByGinningId(Long.valueOf(selectedGinningCode)));
				baleGeneration.setHeap(selectedHeapCode);
				baleGeneration.setStatus(0);
				baleGeneration.setBranchId(getBranchId());
				baleGeneration.setLotNo(productDetail[0].trim());
				baleGeneration.setPrNo(productDetail[1].trim());
				baleGeneration.setBaleWeight(Double.parseDouble(productDetail[2].trim()));
				baleGeneration.setSeason(getCurrentSeasonsCode());
				try {
					baleGeneration.setGenDate(DateUtil.convertStringToDate(selectedGinnDate,DateUtil.DATE));
					baleGeneration.setBaleDate(DateUtil.convertStringToDate(DateUtil.convertDateFormat(selectedDate, DateUtil.DATE_FORMAT_2, DateUtil.DATE),DateUtil.DATE));
				} catch (Exception e) {
					throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DATE_FORMAT);
				}
				HeapData heapData=!StringUtil.isEmpty(selectedHeapCode)?productDistributionService.findHeapDataByHeapCode(Long.valueOf(selectedGinningCode),selectedHeapCode,getCurrentTenantId(),getCurrentSeasonsCode()):null;
				if(!ObjectUtil.isEmpty(heapData)){
					GinningProcess ginningProcess=productDistributionService.findGinningProcessByDateHeapAndProduct(Long.valueOf(selectedGinningCode),
							selectedGinnDate,selectedHeapCode,heapData.getProcurementProduct().getId(),getCurrentTenantId(),getCurrentSeasonsCode());
					if(!ObjectUtil.isEmpty(ginningProcess)){
						ginningProcess.setFarmer(heapData.getFarmer());
						ginningProcess.setTotlintCotton(ginningProcess.getTotlintCotton()+(Double.parseDouble(productDetail[2].trim())/100));
						ginningProcess.setLintPer((ginningProcess.getTotlintCotton()/ginningProcess.getProcessQty())*100);
						ginningProcess.setBaleCount(ginningProcess.getBaleCount()+1);
						Set<BaleGeneration> balGen=new LinkedHashSet<>();
						balGen.add(baleGeneration);
						ginningProcess.setBaleGenerations(balGen);
						productDistributionService.updateGinningProcess(ginningProcess,getCurrentTenantId());
						baleGeneration.setGinningProcess(ginningProcess);
						productDistributionService.saveBaleGeneration(baleGeneration,getCurrentTenantId());
					}else{
						throw new OfflineTransactionException(ITxnErrorCodes.GINNING_UNAVAILABLE);
					}
				}
				
			}
			result = "redirect";
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object code, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", code);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSearchPage() {
		return this.searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public BaleGeneration getBaleGeneration() {
		return baleGeneration;
	}

	public void setBaleGeneration(BaleGeneration baleGeneration) {
		this.baleGeneration = baleGeneration;
	}

	public BaleGeneration getFilter() {
		return filter;
	}

	public void setFilter(BaleGeneration filter) {
		this.filter = filter;
	}

	public Map<Long, String> getGinnerCenterList() {
		return locationService.listCoOperativeAndSamithiByRevisionNo(0L, Warehouse.WarehouseTypes.GINNER.ordinal())
				.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
	}

	public void populateHeap() {
		JSONArray jsonArray = new JSONArray();
		if (!StringUtil.isEmpty(warehouseId)) {
			List<Object[]> heap = productDistributionService.listWarehouseProductByWarehouseId(warehouseId,getCurrentSeasonsCode());
			if (!ObjectUtil.isListEmpty(heap)) {
				for (Object[] obj : heap) {
					jsonArray.add(convertToJsonObject(obj[0], obj[1]));
				}
			}
		}
		sendAjaxResponse(jsonArray);
	}

	public void populateGinningDate() {
		JSONArray jsonArray = new JSONArray();
		if (!StringUtil.isEmpty(warehouseId) && !StringUtil.isEmpty(heapCode)) {
			List<Object[]> ginningDate = productDistributionService.listOfGinningDateByHeap(warehouseId, heapCode,getCurrentSeasonsCode());
			if (!ObjectUtil.isEmpty(ginningDate)) {
				for (Object[] obj : ginningDate) {
					jsonArray.add(convertToJsonObject(obj[2],DateUtil.convertDateFormat(obj[2].toString(),DateUtil.DATE, DateUtil.DATE_FORMAT_2)));
				}
			}
		}
		sendAjaxResponse(jsonArray);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject convertToJsonObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	
	public Map<String, String> getSeasonList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		List<HarvestSeason> harvestSeasonList = productDistributionService.listHarvestSeason();
		if (!ObjectUtil.isListEmpty(harvestSeasonList)) {
			for (HarvestSeason harvestSeason : harvestSeasonList) {
				returnMap.put(harvestSeason.getCode(), harvestSeason.getName());
			}
		}
		return returnMap;
	}
	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getHeapCode() {
		return heapCode;
	}

	public void setHeapCode(String heapCode) {
		this.heapCode = heapCode;
	}

	public String getProductTotalString() {
		return productTotalString;
	}

	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getSelectedGinningCode() {
		return selectedGinningCode;
	}

	public void setSelectedGinningCode(String selectedGinningCode) {
		this.selectedGinningCode = selectedGinningCode;
	}

	public String getSelectedHeapCode() {
		return selectedHeapCode;
	}

	public void setSelectedHeapCode(String selectedHeapCode) {
		this.selectedHeapCode = selectedHeapCode;
	}

	public String getSelectedGinnDate() {
		return selectedGinnDate;
	}

	public void setSelectedGinnDate(String selectedGinnDate) {
		this.selectedGinnDate = selectedGinnDate;
	}

	public String getSelectedSea() {
		return selectedSea;
	}

	public void setSelectedSea(String selectedSea) {
		this.selectedSea = selectedSea;
	}
	
	

}