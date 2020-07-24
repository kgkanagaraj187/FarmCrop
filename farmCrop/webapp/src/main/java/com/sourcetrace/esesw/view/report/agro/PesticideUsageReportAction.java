package com.sourcetrace.esesw.view.report.agro;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionData;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ExportUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class PesticideUsageReportAction extends BaseReportAction implements IExporter {
	private static final Logger LOGGER = Logger.getLogger(PesticideUsageReportAction.class);
	private IFarmerService farmerService;
	private ILocationService locationService;
	private ICatalogueService catalogueService;
	private PeriodicInspection periodicInspection;
	private IPreferencesService preferncesService;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private String daterange;
	/*private List<String> fields = new ArrayList<String>();*/
	private Map<String, String> fields = new HashMap<>();
	private PeriodicInspection filter;
	private String seasonCode;
	private String farmerFatherId;
	private String farmerId;
	private String villageName;
	private String inspectionType;
	private InputStream fileInputStream;
	private String xlsFileName;
	private Farm farm;
	private String farmName;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		daterange = super.startDate + " - " + super.endDate;
		fields.put("1", getText("date"));
		fields.put("2", getText("farmName"));
		fields.put("3", getText("farmer"));
		fields.put("4", getText("Father"));
		fields.put("5", getText("village"));
		fields.put("6", getText("season"));
		request.setAttribute(HEADING, getText(LIST));
		setFilter(periodicInspection);
		return LIST;
	}

	public String data() throws Exception {
		PeriodicInspection periodicInspection = new PeriodicInspection();
		this.filter = periodicInspection;
		Farm farm=new Farm();

	/*	if (!StringUtil.isEmpty(inspectionType) && ObjectUtil.isEmpty(filter.getInspectionType())) {
			filter.setInspectionType(inspectionType);
		}*/
		
		filter.setInspectionType("1");
				
		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeasonCode(seasonCode);
		} else {
			filter.setSeasonCode(clientService.findCurrentSeasonCode());
		}
		
		if(!StringUtil.isEmpty(farmName)){
			
			farm.setFarmCode(farmName);
			filter.setFarm(farm);
		}
		
		if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = new Farmer();
			farmer.setFarmerId(farmerId);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
		}
		
		if (!StringUtil.isEmpty(farmerFatherId)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(farmerFatherId);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
		}
		if (!StringUtil.isEmpty(villageName)) {
			Village village = new Village();
			Farmer farmer = new Farmer();
			village.setCode(villageName);
			farmer.setVillage(village);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
		}
		super.filter = this.filter;
		return sendJSONResponse(readData());

	}

	public JSONObject toJSON(Object obj) {
		PeriodicInspection periodicInspection = (PeriodicInspection) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			rows.add(!ObjectUtil.isEmpty(genDate.format(periodicInspection.getInspectionDate()))
					? genDate.format(periodicInspection.getInspectionDate()).toString() : "");
			
			}
		
		
		rows.add(!StringUtil.isEmpty(periodicInspection.getFarm().getFarmName())
				? periodicInspection.getFarm().getFarmName(): "NA");
		if (!StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCode())
				&& periodicInspection.getFarm().getFarmer().getFarmerCode() != null) {
			rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
					? periodicInspection.getFarm().getFarmer().getFarmerCode() : "");
		} else
			rows.add("");
		rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? periodicInspection.getFarm().getFarmer().getFirstName(): "");
		/*rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getLastName())
						? periodicInspection.getFarm().getFarmer().getLastName() : "NA"
				: "NA");*/
		rows.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? periodicInspection.getFarm().getFarmer().getVillage().getName() != null
						? periodicInspection.getFarm().getFarmer().getVillage().getName() : "NA"
				: "NA");

		if (!ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo())) {
			/*rows.add(periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation().equals("-1")
					? getText("irrigationNa") : getText("irrigationColl") );
*/
		rows.add(!StringUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation()) && periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation()!=null?getText("irrigationColl") : getText("irrigationNa"));	
		
		}

		// rows.add("IrrigationStatus");
		  if(StringUtil.isEmpty(seasonCode) || seasonCode.equals(null))
	        seasonCode= clientService.findCurrentSeasonCode();
		  HarvestSeason season=farmerService.findHarvestSeasonByCode(seasonCode);
		StringBuilder borderCrops = new StringBuilder();
		StringBuilder interCrop = new StringBuilder();
		StringBuilder bcVal = new StringBuilder();
		StringBuilder dat = new StringBuilder();
		if (!ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmCrops())) {

			Set<FarmCrops> farmCrops = periodicInspection.getFarm().getFarmCrops();
			farmCrops.stream().filter(o -> (!StringUtil.isEmpty(String.valueOf(o.getCropCategory())) && String.valueOf(o.getCropCategory()).equals("0") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season))).forEach(o -> {

				if(!StringUtil.isEmpty(o.getCultiArea()) && o.getCultiArea()!=null && o.getCultiArea()!="" && !o.getCultiArea().equalsIgnoreCase("undefined"))
					bcVal.append(o.getCultiArea() + ",");
				if(!ObjectUtil.isEmpty(o.getSowingDate()) && o.getSowingDate()!=null)
					dat.append(df.format(o.getSowingDate()) + ",");
				

			});

			farmCrops.stream().filter(o -> String.valueOf(o.getCropCategory()).equals("2") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)).forEach(o -> {

				borderCrops.append(o.getProcurementVariety().getName() + "-"
						+ o.getProcurementVariety().getProcurementProduct().getName() + ",");

			});
			
			farmCrops.stream().filter(o -> String.valueOf(o.getCropCategory()).equals("1") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)).forEach(o -> {

				interCrop.append(o.getProcurementVariety().getName() + "-"
						+ o.getProcurementVariety().getProcurementProduct().getName() + ",");

			});

		}
		float totBc = 0;
		if (!StringUtil.isEmpty(bcVal.toString())) {
			String bc[] = bcVal.toString().split(",");
			for (String s : bc) {
				totBc += Float.parseFloat(s);
			}
		}
		rows.add(totBc);
		rows.add(!StringUtil.isEmpty(periodicInspection.getLandpreparationCompleted())?getText("landPrep" + periodicInspection.getLandpreparationCompleted()):getText("landPrep2"));
		/*String sowDat[] = dat.toString().split(",");
		rows.add(!StringUtil.isEmpty(sowDat[0]) ? sowDat[0] : "NA");*/
		rows.add(!StringUtil.isEmpty(dat.toString())?getText("YESNO1"):getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(borderCrops.toString()) ? getText("YESNO1"):getText("YESNO2"));
		//rows.add((!StringUtil.isEmpty(periodicInspection.getInterCrop())&& periodicInspection.getInterCrop()=='Y') ?getText("YESNO1"):getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(interCrop.toString()) ? getText("YESNO1") : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getChemicalSpray())
				? getText("YESNO" + periodicInspection.getChemicalSpray()) : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getMonoOrImida())
				? getText("YESNO" + periodicInspection.getMonoOrImida()) : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getSingleSprayOrCocktail())
				? getText("YESNO" + periodicInspection.getSingleSprayOrCocktail()) : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getRepetitionOfPest())
				? getText("YESNO" + periodicInspection.getRepetitionOfPest()) : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getNitrogenousFert())
				? getText("YESNO" + periodicInspection.getNitrogenousFert()) : getText("YESNO2"));
		rows.add(!StringUtil.isEmpty(periodicInspection.getCropSpacingLastYear())
				? periodicInspection.getCropSpacingLastYear() : "NA");
		rows.add(!StringUtil.isEmpty(periodicInspection.getCropSpacingCurrentYear())
				? periodicInspection.getCropSpacingCurrentYear() : "NA");
		jsonObject.put("id", periodicInspection.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		return null;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public PeriodicInspection getPeriodicInspection() {
		return periodicInspection;
	}

	public void setPeriodicInspection(PeriodicInspection periodicInspection) {
		this.periodicInspection = periodicInspection;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public PeriodicInspection getFilter() {
		return filter;
	}

	public void setFilter(PeriodicInspection filter) {
		this.filter = filter;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getFarmerFatherId() {
		return farmerFatherId;
	}

	public void setFarmerFatherId(String farmerFatherId) {
		this.farmerFatherId = farmerFatherId;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

/*	public Map<String, String> getFarmersList() {
		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listPeriodicInspectionFarmer();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
		farmerListMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return farmerListMap;

	}*/
	
/*	Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	public Map<String, String> getFarmerNameList() {

	
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[3])));
		farmerListMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return farmerListMap;

	}*/
	
	public void populateFarmerList(){
		JSONArray farmerArr = new JSONArray();
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(farmersList)) {
			farmersList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj[1].toString(), obj[3].toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}

/*
	public Map<String, String> getFarmerFartherNameList() {
		Map<String, String> fathersNameListMap = new LinkedHashMap<String, String>();
		List<Object> farmersList = farmerService.listPeriodicInsoectionFatherName();
		for (Object obj : farmersList) {
			if (!ObjectUtil.isEmpty(obj))
				fathersNameListMap.put(String.valueOf(obj), String.valueOf(obj));
		}
		return fathersNameListMap;
	}*/
	
	public void populateFarmerFatherList(){
		JSONArray farmerArr = new JSONArray();
		List<Object> farmersList = farmerService.listPeriodicInsoectionFatherName();
		if (!ObjectUtil.isEmpty(farmersList)) {
			farmersList.forEach(obj -> {
				farmerArr.add(getJSONObject(obj.toString(), obj.toString()));
			});
		}
		sendAjaxResponse(farmerArr);
	}

	/*public Map<String, String> getVillageList() {
		Map<String, String> villageMap = new LinkedHashMap<String, String>();
		List<Object[]> villages = farmerService.listPeriodicInspectionVillage();
		villageMap = villages.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
		villageMap = villageMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return villageMap;
	}*/
	
	public void populateVillageList (){
		JSONArray villageArr = new JSONArray();
		List<Object[]> villages = farmerService.listPeriodicInspectionVillage();
		  if (!ObjectUtil.isEmpty(villages)) {
			  villages.forEach(obj -> {
				  villageArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
		   });
		  }
		  sendAjaxResponse(villageArr);
	}
	
/*	public Map<String, String> getSeasonList() {
		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		}
		return seasonMap;
	}
*/
	public void populateSeasonList(){
		JSONArray seasonArr = new JSONArray();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		if (!ObjectUtil.isEmpty(seasonList)) {
			seasonList.forEach(obj -> {
				seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()));
			});
		}
		sendAjaxResponse(seasonArr);
	}
	public Map<Integer, String> getIrrigationSourceList() {
		Map<Integer, String> irrigationSourceList = new LinkedHashMap<Integer, String>();
		if (irrigationSourceList.size() == 0) {
			irrigationSourceList = getPropertyData("PestiirrigationSourceList");
			return irrigationSourceList;
		}
		return irrigationSourceList;
	}

	public Map<String, String> getPropertyMap(String text) {

		Map<String, String> propertyDataMap = null;
		String values = getLocaleProperty(text);
		if (!StringUtil.isEmpty(values)) {
			propertyDataMap = new LinkedHashMap();
			String[] valuesArray = values.split(",");
			for (String value : valuesArray) {
				String[] data = value.trim().split("\\~");
				propertyDataMap.put(data[0], data[1]);
			}
		}
		return propertyDataMap;

	}

	public String populateXLS() throws Exception {
		InputStream is = getPesticideUsageDataStream();

		setXlsFileName(getText("reportName") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getText("reportName") + DateUtil.getRevisionNoDateTimeMilliSec(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(
				getText("reportName") + DateUtil.getRevisionNoDateTimeMilliSec(), fileMap, ".xls"));
		return "xls";
	}

	private InputStream getPesticideUsageDataStream() throws IOException,  ParseException  {
		PeriodicInspection periodicInspection = new PeriodicInspection();
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		LinkedList<String> headersList = new LinkedList<String>();
		List<List<String>> dataList = new ArrayList<List<String>>();
		this.filter = periodicInspection;
		Farm farm=new Farm();
		
		  ESESystem preferences = preferncesService.findPrefernceById("1");
	        if (!ObjectUtil.isEmpty(preferences)) {
				DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
				Date sDate=df.parse(getStartDate());
				Date eDate=df.parse(getEndDate());
				filters.put(getText("startDate"), genDate.format(sDate));
		        filters.put(getText("EndingDate"), genDate.format(eDate));
				}
		
		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setSeasonCode(seasonCode);
			String sessionName = farmerService.findBySeasonCode(seasonCode);
			if (!StringUtil.isEmpty(sessionName)) {
				filters.put(getText("season"), sessionName);
			}
		} else {
			filter.setSeasonCode(clientService.findCurrentSeasonCode());
			String sessionName = farmerService.findBySeasonCode(clientService.findCurrentSeasonCode());
			if (!StringUtil.isEmpty(sessionName)) {
				filters.put(getText("season"), sessionName);
			}
		}
		if(!StringUtil.isEmpty(farmName)){
			
			farm.setFarmCode(farmName);
			filter.setFarm(farm);
			Farm frm=farmerService.findFarmByCode(farmName);
			filters.put(getText("farmName"), frm.getFarmName());
			
		}
		if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = new Farmer();
			farmer.setFarmerId(farmerId);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
			Object[] obj = farmerService.findFarmerAndFatherNameByFarmerId(farmerId);
			filters.put(getText("farmer"), String.valueOf(obj[2]));
		}
		if (!StringUtil.isEmpty(farmerFatherId)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(farmerFatherId);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
			filters.put(getText("father"), farmerFatherId);
		}
		if (!StringUtil.isEmpty(villageName)) {
			Village village = new Village();
			Farmer farmer = new Farmer();
			
			village.setCode(villageName);
			farmer.setVillage(village);
			farm.setFarmer(farmer);
			filter.setFarm(farm);
			filters.put(getText("village"), locationService.findVillageNameByCode(villageName));
		}
		
		filter.setInspectionType("1");
		
		super.filter = this.filter;
		Map datas = readData();

		/*String headers = getText("reportHeaders");*/
		String headers = getText("PesticideUsageReport");
		
		if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			if (StringUtil.isEmpty(branchIdValue)) {
				headers = getLocaleProperty("OrganicPesticideUsageReport");
			} 
			else if(getBranchId().equalsIgnoreCase("organic")){
				 headers = getLocaleProperty("OrganicPesticideUsageReport");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				headers = getLocaleProperty("BCIPesticideUsageReport");
			}
		
			
		}
		
		if (headers != null) {

			for (String name : headers.split(",")) {
				headersList.add(getLocaleProperty(name));
			}
		}

		for (Object record : (List) datas.get(ROWS)) {
			List<String> list = getDataForReport(record);

			dataList.add(list);

		}

		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);

		InputStream stream = ExportUtil.exportXLS(dataList, headersList, filters,
				getText("reportName"), getText("filter"), getText("reportName"),
				existingAssetLogin.getFile());

		return stream;
	}
	Long serialNumber = 0L;
	public List<String> getDataForReport(Object obj) {
		List<String> dataList = new ArrayList<>();
		PeriodicInspection periodicInspection = (PeriodicInspection) obj;
		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		
		serialNumber++;
		dataList.add(serialNumber.toString());
        if (!StringUtil.isEmpty(preferences)) {
			DateFormat genDate=new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			dataList.add(!ObjectUtil.isEmpty(genDate.format(periodicInspection.getInspectionDate()))
					? genDate.format(periodicInspection.getInspectionDate()).toString() : "");
        
        }
		
		dataList.add(!StringUtil.isEmpty(periodicInspection.getFarm().getFarmName())
				? periodicInspection.getFarm().getFarmName(): "NA");
		if (!StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getFarmerCode())
				&& periodicInspection.getFarm().getFarmer().getFarmerCode() != null) {
			dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
					? periodicInspection.getFarm().getFarmer().getFarmerCode() : "");
		} else
			dataList.add("");
		dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? periodicInspection.getFarm().getFarmer().getFirstName(): "");
		/*dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? !StringUtil.isEmpty(periodicInspection.getFarm().getFarmer().getLastName())
						? periodicInspection.getFarm().getFarmer().getLastName() : "NA"
				: "NA");*/
		dataList.add(!ObjectUtil.isEmpty(periodicInspection.getFarm())
				? periodicInspection.getFarm().getFarmer().getVillage().getName() != null
						? periodicInspection.getFarm().getFarmer().getVillage().getName() : "NA"
				: "NA");

		if (!ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo())) {
		/*	if (!ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo())) {
				dataList.add(periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation().equals("-1")
						? getText("irrigationNa") : getText("irrigationColl") );
			}*/
			
			dataList.add(!StringUtil.isEmpty(periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation()) && periodicInspection.getFarm().getFarmDetailedInfo().getFarmIrrigation()!=null?getText("irrigationColl") : getText("irrigationNa"));

		}

		// dataList.add("IrrigationStatus");
		if(StringUtil.isEmpty(seasonCode) || seasonCode.equals(null))
	        seasonCode= clientService.findCurrentSeasonCode();
		  HarvestSeason season=farmerService.findHarvestSeasonByCode(seasonCode);
		StringBuilder borderCrops = new StringBuilder();
		StringBuilder interCrop = new StringBuilder();
		StringBuilder bcVal = new StringBuilder();
		StringBuilder dat = new StringBuilder();
		if (!ObjectUtil.isEmpty(periodicInspection.getFarm().getFarmCrops())) {

			Set<FarmCrops> farmCrops = periodicInspection.getFarm().getFarmCrops();
			farmCrops.stream().filter(o -> (!StringUtil.isEmpty(String.valueOf(o.getCropCategory())) && String.valueOf(o.getCropCategory()).equals("0") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season))).forEach(o -> {

				

				if(!StringUtil.isEmpty(o.getCultiArea()) && o.getCultiArea()!=null && o.getCultiArea()!="" && !o.getCultiArea().equalsIgnoreCase("undefined"))
					bcVal.append(o.getCultiArea() + ",");
				if(!ObjectUtil.isEmpty(o.getSowingDate()) && o.getSowingDate()!=null)
					dat.append(df.format(o.getSowingDate()) + ",");
				

			});
			farmCrops.stream().filter(o -> String.valueOf(o.getCropCategory()).equals("2") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)).forEach(o -> {

				borderCrops.append(o.getProcurementVariety().getName() + "-"
						+ o.getProcurementVariety().getProcurementProduct().getName() + ",");

			});
			
			farmCrops.stream().filter(o -> String.valueOf(o.getCropCategory()).equals("1") && (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)).forEach(o -> {

				interCrop.append(o.getProcurementVariety().getName() + "-"
						+ o.getProcurementVariety().getProcurementProduct().getName() + ",");

			});
		}
		float totBc = 0;
		if (!StringUtil.isEmpty(bcVal.toString())) {
			String bc[] = bcVal.toString().split(",");
			for (String s : bc) {
				totBc += Float.parseFloat(s);
			}
		}
		dataList.add(String.valueOf(totBc));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getLandpreparationCompleted())?getText("landPrep" + periodicInspection.getLandpreparationCompleted()):getText("landPrep2"));
		/*String sowDat[] = dat.toString().split(",");
		dataList.add(!StringUtil.isEmpty(sowDat[0]) ? sowDat[0] : "NA");
		*/
		dataList.add(!StringUtil.isEmpty(dat.toString())?getText("YESNO1"):getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(borderCrops.toString()) ?getText("YESNO1"):getText("YESNO2") );
				
		dataList.add(!StringUtil.isEmpty(interCrop.toString()) ? getText("YESNO1") : getText("YESNO2"));
		/*dataList.add(
				!StringUtil.isEmpty(interCrop.toString()) ? StringUtil.removeLastComma(interCrop.toString()) : "NA");
		*///dataList.add((!StringUtil.isEmpty(periodicInspection.getInterCrop())&& periodicInspection.getInterCrop()=='Y') ?getText("YESNO1"):getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getChemicalSpray())
				? getText("YESNO" + periodicInspection.getChemicalSpray()) : getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getMonoOrImida())
				? getText("YESNO" + periodicInspection.getMonoOrImida()) : getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getSingleSprayOrCocktail())
				? getText("YESNO" + periodicInspection.getSingleSprayOrCocktail()) : getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getRepetitionOfPest())
				? getText("YESNO" + periodicInspection.getRepetitionOfPest()) : getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getNitrogenousFert())
				? getText("YESNO" + periodicInspection.getNitrogenousFert()) : getText("YESNO2"));
		dataList.add(!StringUtil.isEmpty(periodicInspection.getCropSpacingLastYear())
				? periodicInspection.getCropSpacingLastYear() : "NA");
		dataList.add(!StringUtil.isEmpty(periodicInspection.getCropSpacingCurrentYear())
				? periodicInspection.getCropSpacingCurrentYear() : "NA");

		return dataList;
	}

/*	public Map<String, String> getFarmList() {

		Map<String, String> farmListMap = new LinkedHashMap<String, String>();
		List<Farm> farmsList = farmerService.listFarm();
		for (Farm frm : farmsList)
			farmListMap.put(frm.getFarmCode(), frm.getFarmName());
		 farmListMap = farmerService.listFarmInfo().stream().collect(Collectors.toMap(obj->String.valueOf(obj[1]), obj->String.valueOf(obj[2])));
		 farmerService.listFarmInfo().stream().forEach(obj->{
				if(!farmListMap.containsKey(obj[2])){
					farmListMap.put(String.valueOf(obj[2]), String.valueOf(obj[2]));
				}
		 });
		return farmListMap;

	}*/

	public void populateFarmList (){
		   JSONArray farmArr = new JSONArray();
		    List<Object[]> farmList = farmerService.listFarmInfo();
		    if (!ObjectUtil.isEmpty(farmList)) {
		     farmList.forEach(obj -> {
		      farmArr.add(getJSONObject(obj[1].toString(), obj[2].toString()));
		     });
		    }
		    sendAjaxResponse(farmArr);
		 }

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public String getFarmName() {
		return farmName;
	}

	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	

}
