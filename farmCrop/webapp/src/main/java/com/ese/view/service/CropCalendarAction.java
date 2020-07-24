
package com.ese.view.service;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.WebTransactionAction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class CropCalendarAction extends WebTransactionAction {
    private static final long serialVersionUID = 1;
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private IPreferencesService preferncesService;
    @Autowired
    private IFarmerService farmerService;
    private String selectedProduct;
    private String cropType;
    private String crop;
    private String branchIdParma;
    private String searchPage;
    private CropCalendar cropCalendar;
    private String seasonCode;
    private Long seasonId;
    private List<String> fields = new ArrayList<String>();
    private Map<Integer, String> cropTypeList;
    private String calendarString;
    private String selectedSeason;
	private String season;
    private String selectedActivity;
    private String selectedVariety;
    private String selectedCrop;
    private String calendarName;
    private String txnDate;
    private CropCalendar filter;
    private String selectedCropCalendarId;
    private String selectedVarietyId;

    public String getSelectedProduct() {
        return this.selectedProduct;
    }

    public void setSelectedProduct(String selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getCropType() {
        return this.cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getCrop() {
        return this.crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public CropCalendar getCropCalendar() {
        return this.cropCalendar;
    }

    public void setCropCalendar(CropCalendar cropCalendar) {
        this.cropCalendar = cropCalendar;
    }
  

   public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

@SuppressWarnings("rawtypes")
    public String data() throws Exception {
	   Map searchRecord = this.getJQGridRequestParam();
       CropCalendar filter = new CropCalendar();
      
       if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))  && !"-1".equalsIgnoreCase(String.valueOf(searchRecord.get("seasonCode")))) {
    	  HarvestSeason season = productDistributionService.findHarvestSeasonBySeasonCode(searchRecord.get("seasonCode").toString());
    	  filter.setSeasonCode(season.getCode());
		}
       if (!StringUtil.isEmpty(searchRecord.get("crop"))) {
			ProcurementProduct procurementProduct = new ProcurementProduct();
			procurementProduct.setName(String.valueOf(searchRecord.get("crop")));
			filter.setProcurementProduct(procurementProduct);
		}
       if (!StringUtil.isEmpty(searchRecord.get("variety"))) {
			ProcurementVariety procurementVariety = new ProcurementVariety();
			procurementVariety.setName(String.valueOf(searchRecord.get("variety")));
			filter.setProcurementVariety(procurementVariety);
		}
       Map data = this.reportService.listWithEntityFiltering(this.getDir(), this.getSort(), this.getStartIndex(), this.getResults(), (Object)filter, this.getPage());
      return sendJQGridJSONResponse(data);
    }
 
    public JSONObject toJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        Object[] results = (Object[]) obj;
        
      HarvestSeason season = productDistributionService.findHarvestSeasonBySeasonCode(results[1].toString());
      rows.add(!ObjectUtil.isEmpty(season)	? season.getName() : "NA");
        ProcurementVariety variety = productDistributionService.findProcurementVariertyById(Long.valueOf(results[0].toString()));
        
        rows.add(!ObjectUtil.isEmpty(variety) && !ObjectUtil.isEmpty(variety.getProcurementProduct()) 
        		? variety.getProcurementProduct().getName() : "NA");
        rows.add(!ObjectUtil.isEmpty(variety) ? variety.getName() : "NA");
        
    	
        
        rows.add("<button type='button' class='btn btn-sts' onclick='redirectToCalendarView(\""	+ variety.getId() + "\",\""
				+ season.getCode() + "\" )'>" + "<i class='fa fa-calendar-check-o' aria-hidden='true'></i></button>");
        
        
        jsonObject.put("id", variety.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
    }

    public String detail() {
        return "detail";
    }

    public Map<Integer, String> getCropTypeList() {
        LinkedHashMap<Integer, String> cropListMap = new LinkedHashMap<Integer, String>();
        cropListMap.put(0, this.getLocaleProperty("calendarActivityType0"));
        cropListMap.put(1, this.getLocaleProperty("calendarActivityType1"));
        return cropListMap;
    }

    public void setCropTypeList(Map<Integer, String> cropTypeList) {
        this.cropTypeList = cropTypeList;
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

    public String create() throws ParseException {
    	
    
        return "create";
    }
   
  public Map<String, String> getHarvestSeasonList() {
        LinkedHashMap<String, String> returnMap = new LinkedHashMap<String, String>();
        List <HarvestSeason >harvestSeasonList =productDistributionService.listHarvestSeason();
        if (!ObjectUtil.isListEmpty((List)harvestSeasonList)) {
            for (HarvestSeason harvestSeason : harvestSeasonList) {
                returnMap.put(harvestSeason.getCode(), harvestSeason.getName());
            }
        }
        return returnMap;
    }
    public String getHarvestSeasonFilterList() {
	StringBuffer sb = new StringBuffer();
    List <HarvestSeason >harvestSeasonList =productDistributionService.listHarvestSeason();
	sb.append("-1:").append(FILTER_ALL).append(";");
	for (HarvestSeason harvestSeason : harvestSeasonList) {
		sb.append(harvestSeason.getCode()).append(":").append(harvestSeason.getName()).append(";");
	}
	String data = sb.toString();
	return data.substring(0, data.length() - 1);

    }
  

    public List<ProcurementProduct> getProductList() {
        List listProduct = this.productDistributionService.listProcurementProductByType(Procurement.productType.GOODS.ordinal());
        return listProduct;
    }

    public void populateVariety() throws Exception {
        if (!StringUtil.isEmpty((String)this.selectedProduct)) {
        	List<ProcurementVariety> varietiesList = this.productDistributionService.listProcurementVarietyByProcurementProductId(Long.valueOf(this.selectedProduct));
            JSONArray varietyArr = new JSONArray();

			varietiesList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				varietyArr.add(getJSONObject(obj.getId(), obj.getName()));
			});
            this.sendAjaxResponse(varietyArr);
        }
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
    
    public void populateCropCalendar(){
    	loadCurrentSeason();
		String receiptNumber;
    	Set<CropCalendarDetail> cropCalendarDetails = formCropCalendarDetails();
    	Season season = productDistributionService.findSeasonBySeasonCode(selectedSeason);

		CropCalendar cropCalendar = new CropCalendar();
		cropCalendar.setCropCalendarDetail(cropCalendarDetails);
		cropCalendar.setTxnDate(DateUtil.convertStringToDate(this.txnDate, getGeneralDateFormat()));
		cropCalendar.setName(!StringUtil.isEmpty(calendarName) ? calendarName :"");
		ProcurementVariety variety = productDistributionService.findProcurementVariertyById(Long.valueOf(selectedVariety));
		//cropCalendar.setCropCode(!StringUtil.isEmpty(selectedCrop) ? selectedCrop : "");
		cropCalendar.setProcurementProduct(!ObjectUtil.isEmpty(variety) && !ObjectUtil.isEmpty(variety.getProcurementProduct()) ? variety.getProcurementProduct() :null );
		cropCalendar.setProcurementVariety(!ObjectUtil.isEmpty(variety) ? variety :null );
		//cropCalendar.setVarietyCode(!StringUtil.isEmpty(selectedVariety) ? selectedVariety : "");
		cropCalendar.setSeasonCode(!StringUtil.isEmpty(selectedSeason) ? selectedSeason : "");
		cropCalendar.setActivityType(Integer.valueOf(selectedActivity));
		cropCalendar.setCreatedUser(getUsername());
		cropCalendar.setCreatedDate(DateUtil.convertStringToDate(this.txnDate, getGeneralDateFormat()));
		cropCalendar.setBranchId(getBranchId());
		cropCalendar.setRevisionNo(String.valueOf(DateUtil.getRevisionNumber()));
		productDistributionService.addCropCalendar(cropCalendar);
		productDistributionService.updateProcurementVarietyRevisionNo(cropCalendar.getProcurementVariety().getId(), DateUtil.getRevisionNumber());
	/*	JSONArray respArr = new JSONArray();
		respArr.add(getJSONObject("data", "success"));		
		sendAjaxResponse(respArr);*/
		
    }
    
    private void loadCurrentSeason() {

		ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		if (!ObjectUtil.isEmpty(preference)) {
			String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
			if (!StringUtil.isEmpty(currentSeasonCode)) {
				Season currentSeason = productDistributionService.findSeasonBySeasonCode(currentSeasonCode);
				if (!ObjectUtil.isEmpty(currentSeason))
					seasonId = currentSeason.getId();
			}
		}
	}
    
    private Set<CropCalendarDetail> formCropCalendarDetails() {
		Set<CropCalendarDetail> cropCalendarDetails = new LinkedHashSet<>();

		if (!StringUtil.isEmpty(getCalendarString())) {
			List<String> productsList = Arrays.asList(getCalendarString().split("@"));

			productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
				CropCalendarDetail cropCalendarDetail = new CropCalendarDetail();
				List<String> list = Arrays.asList(products.split("#"));
			
				cropCalendarDetail.setActivityMethod(list.get(0).toString());
				cropCalendarDetail.setNoOfDays(list.get(1).toString());
				cropCalendarDetails.add(cropCalendarDetail);
			});

		}

		return cropCalendarDetails;
	}

	public String getCalendarString() {
		return calendarString;
	}

	public void setCalendarString(String calendarString) {
		this.calendarString = calendarString;
	}

	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	public String getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(String selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
    
	public Map<String, String> getActivityMethodList() {

		Map<String, String> farmCatalougeList = new LinkedHashMap<>();
		farmCatalougeList = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("calendarActivityMethod").trim()));
		return farmCatalougeList;

	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public CropCalendar getFilter() {
		return filter;
	}

	public void setFilter(CropCalendar filter) {
		this.filter = filter;
	}

	public String getSelectedCropCalendarId() {
		return selectedCropCalendarId;
	}

	public void setSelectedCropCalendarId(String selectedCropCalendarId) {
		this.selectedCropCalendarId = selectedCropCalendarId;
	}

	public String getSelectedVarietyId() {
		return selectedVarietyId;
	}

	public void setSelectedVarietyId(String selectedVarietyId) {
		this.selectedVarietyId = selectedVarietyId;
	}
	
	
public void populateCalendarValues() throws Exception {
	if (!selectedVarietyId.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVarietyId)) 
			&& !selectedSeason.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedSeason))) {
		List<Object[]> listActivity = productDistributionService.listActivityByCalendarIdAndVarietyId(Long.valueOf(selectedVarietyId),selectedSeason);

		JSONArray activityArr = new JSONArray();
		for(Object[] obj : listActivity){
			JSONObject activityJSONObject = new JSONObject();
			FarmCatalogue catalogue = getCatlogueValueByCode(obj[0].toString());
			activityJSONObject.put("name", catalogue.getName());
			activityJSONObject.put("noOfDays", obj[1]);			
			activityArr.add(activityJSONObject);
		}
		sendAjaxResponse(activityArr);
	}
}
}