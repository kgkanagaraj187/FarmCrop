package com.sourcetrace.esesw.view.report.agro;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
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
import com.sourcetrace.esesw.entity.profile.HarvestData;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class BciFieldMonitoringReportAction extends BaseReportAction implements IExporter {
	private static final Logger LOGGER = Logger.getLogger(BciFieldMonitoringReportAction.class);
	private IFarmerService farmerService;
	private ILocationService locationService;
	private ICatalogueService catalogueService;
	private IClientService clientService;
	private IPreferencesService preferncesService;
	private List<String> fields = new ArrayList<String>();
	private Farm farm;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private String daterange;
	private Farm filter;
	private String seasonCode;
	private String fatherName;
	private String farmerCode;
	private String farmerName;
	private String villageName;
	private String farmName;
	private InputStream fileInputStream;
	private String xlsFileName;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	public String list() {
		fields.add(getText("farmName"));
		fields.add(getText("farmerName"));
		fields.add(getText("fatherName"));
		fields.add(getText("villageName"));
		fields.add(getText("season"));
		request.setAttribute(HEADING, getText(LIST));
		setFilter(farm);
		return LIST;
	}

	public String data() throws Exception {
		Farm farm = new Farm();
		this.filter = farm;
		if (StringUtil.isEmpty(seasonCode)) {
			seasonCode = clientService.findCurrentSeasonCode();
		}
		if (!StringUtil.isEmpty(farmName)) {
			filter.setFarmCode(farmName);
		}
		if (!StringUtil.isEmpty(farmerName)) {
			Farmer farmer = new Farmer();
			farmer.setFarmerId(farmerName);
			filter.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(fatherName)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(fatherName);
			filter.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(villageName)) {
			Farmer farmer = new Farmer();
			Village village = new Village();
			village.setCode(villageName);
			farmer.setVillage(village);
			filter.setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(getBranchId()) || getBranchId() != null) {

			if (!ObjectUtil.isEmpty(filter.getFarmer())) {
				filter.getFarmer().setBranchId(getBranchId());
			} else {
				Farmer farmer = new Farmer();
				farmer.setBranchId(getBranchId());
				filter.setFarmer(farmer);
			}
		}
		/*
		 * if (!StringUtil.isEmpty(seasonCode)) {
		 * filter.setCurrentSeasonCode(seasonCode); }
		 */
		super.filter = this.filter;
		return sendJSONResponse(readData());

	}

	public JSONObject toJSON(Object obj) {
		Farm farm = (Farm) obj;

		JSONObject jsonObject = new JSONObject();

		JSONArray rows = new JSONArray();

		String farmer;
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		rows.add(!StringUtil.isEmpty(farm.getFarmName()) ? farm.getFarmName() : "NA");
		if (!ObjectUtil.isEmpty(farm.getFarmer())) {
			rows.add(!StringUtil.isEmpty(farm.getFarmer().getFarmerCode()) ? farm.getFarmer().getFarmerCode() : "");
			rows.add(!StringUtil.isEmpty(farm.getFarmer().getFirstName()) ? farm.getFarmer().getFirstName() : "");
			// rows.add(!StringUtil.isEmpty(farm.getFarmer().getLastName())?farm.getFarmer().getLastName():"");
			rows.add(!ObjectUtil.isEmpty(farm.getFarmer().getVillage()) ? farm.getFarmer().getVillage().getName()
					: "NA");
		}
		if (StringUtil.isEmpty(seasonCode) || seasonCode.equals(null))
			seasonCode = clientService.findCurrentSeasonCode();
		// Long seasonId=farmerService.findSeasonBySeasonCode(seasonCode);
		HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
		StringBuffer bcVal = new StringBuffer();
		StringBuffer dat = new StringBuffer();

		if (!ObjectUtil.isEmpty(farm)) {
			Set<FarmCrops> farmCrops = farm.getFarmCrops();
			if (!ObjectUtil.isListEmpty(farmCrops)) {
				farmCrops.stream()
						.filter(o -> (!StringUtil.isEmpty(String.valueOf(o.getCropCategory()))
								&& String.valueOf(o.getCropCategory()).equals("0")
								&& (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)))
						.forEach(o -> {
							if (!StringUtil.isEmpty(o.getCultiArea()) && o.getCultiArea() != null
									&& !o.getCultiArea().equalsIgnoreCase("undefined"))
								bcVal.append(o.getCultiArea() + ",");
							if (o.getSowingDate() != null)
								dat.append(df.format(o.getSowingDate()) + ",");
						});
			}
		}
		float totBc = 0;
		if (!StringUtil.isEmpty(bcVal.toString())) {
			String bc[] = bcVal.toString().split(",");
			for (String s : bc) {
				totBc += Float.parseFloat(s);
			}
		}
		rows.add(totBc);
		Farmer frm = farmerService.findFarmerById(farm.getFarmer().getId());
		rows.add(getText("basicInfo" + frm.getBasicInfo()));
		int count = farmerService.findLandPreparationDetailsByFarmCode(farm.getFarmCode(), season.getCode());
		if (count > 0)
			rows.add(getText("yes"));
		else
			rows.add(getText("no"));
		/*
		 * String sowDat[]=dat.toString().split(",");
		 * rows.add(!StringUtil.isEmpty(sowDat[0])?sowDat[0]:"NA");
		 */
		StringBuffer seeDetail = new StringBuffer();
		rows.add(!StringUtil.isEmpty(dat.toString()) ? getText("yes") : getText("no"));
		if (!ObjectUtil.isListEmpty(farm.getFarmCrops()) && farm.getFarmCrops() != null) {
			farm.getFarmCrops().stream()
					.filter(o -> (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season))
					.forEach(o -> {
						seeDetail.append("success");
					});
		}
		if (!StringUtil.isEmpty(seeDetail.toString())) {
			rows.add(getText("yes"));
		} else {
			rows.add(getText("no"));
		}
		/*
		 * if(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) &&
		 * !StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigationSource())
		 * ){ //rows.add(StringUtil.removeLastComma(seedDet.toString()));
		 * rows.add(farm.getFarmDetailedInfo().getIrrigationSource().equals("-1"
		 * )?getText("irrigationNa"):getIrrigationSourceList().get(Integer.
		 * parseInt(farm.getFarmDetailedInfo().getIrrigationSource())));
		 * 
		 * } else{ rows.add(getText("irrigationNa")); }
		 */

		if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {
			rows.add(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())
					&& farm.getFarmDetailedInfo().getFarmIrrigation() != null ? getText("irrigationColl")
							: getText("irrigationNa"));

		}

		// PeriodicInspection
		// periodicInspection=farmerService.findperiodicInspectionByBranchIdAndFarmId(getBranchId(),farm.getFarmCode());
		for (Entry<String, String> s : getWeedingMap().entrySet()) {
			Integer occurenceCount = 0;
			occurenceCount = farmerService.findWeedingStatusByCode(getBranchId(), s.getKey(), farm.getFarmCode(),
					seasonCode);
			if (occurenceCount == 0 || occurenceCount == null) {
				rows.add(getText("no"));
			} else {
				rows.add(getText("yes"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			int occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 3l, seasonCode);
			if (occurs > 0) {
				rows.add(getText("yes"));
			} else {
				rows.add(getText("no"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			Integer occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 1l, seasonCode);
			if (occurs == 0 || occurs == null) {
				rows.add(getText("no"));
			} else {
				rows.add(getText("yes"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			Integer occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 2l, seasonCode);
			if (occurs == 0 || occurs == null) {
				rows.add(getText("no"));
			} else {
				rows.add(getText("yes"));
			}

		}

		for (Entry<String, String> s : getPickMap().entrySet()) {
			Integer occurenceCount = 0;
			occurenceCount = farmerService.findPickingStatusByCode(getBranchId(), s.getKey(), farm.getFarmCode(),
					seasonCode);
			if (occurenceCount == 0 || occurenceCount == null) {
				rows.add(getText("no"));
			} else {
				rows.add(getText("yes"));
			}
		}

		jsonObject.put("id", farm.getId());
		jsonObject.put("cell", rows);

		return jsonObject;

	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
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

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	/*Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	public Map<String, String> getSeasonList() {

	
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		}
		return seasonMap;

	}
	Map<String, String> fathersNameListMap = new LinkedHashMap<String, String>();
	public Map<String, String> getFatherNameList() {
		

		List<Object> farmersList = farmerService.listPeriodicInsoectionFatherName();

		for (Object obj : farmersList) {
			if (!ObjectUtil.isEmpty(obj))
				fathersNameListMap.put(String.valueOf(obj), String.valueOf(obj));
		}
		return fathersNameListMap;
	}
	Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	public Map<String, String> getFarmerNameList() {

	
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[3])));
		farmerListMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return farmerListMap;

	}
	Map<String, String> villageMap = new LinkedHashMap<String, String>();
	public Map<String, String> getVillageList() {
		
		List<Object[]> villages = farmerService.listPeriodicInspectionVillage();
		villageMap = villages.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
		villageMap = villageMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return villageMap;
	}*/

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public Farm getFilter() {
		return filter;
	}

	public void setFilter(Farm filter) {
		this.filter = filter;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
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

	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public Map<Integer, String> getIrrigationSourceList() {
		Map<Integer, String> irrigationSourceList = new LinkedHashMap<Integer, String>();
		if (irrigationSourceList.size() == 0) {
			irrigationSourceList = getPropertyData("BCIirrigationSourceList");
			return irrigationSourceList;
		}
		return irrigationSourceList;
	}
	LinkedHashMap<String, String> UsageMap = new LinkedHashMap<>();
	public Map<String, String> getUsagegMap(String brandId) {
		if (UsageMap.size() > 0) {
			return UsageMap;
		}

		
		List<FarmCatalogue> catList = catalogueService.findCatalogueByTypezAndBranch(getText("usageLevelTypez"),
				brandId);
		/* List<FarmCatalogue> catList =
		 catalogueService.findCatalogueByTypezAndBranch("56",brandId);*/

		if (!ObjectUtil.isListEmpty(catList)) {
			for (FarmCatalogue catalogue : catList) {
				UsageMap.put(catalogue.getCode(), catalogue.getName());
			}
		}
		return UsageMap;
	}

	Map<String, String> weedingMap = new HashMap<>();

	public Map<String, String> getWeedingMap() {
		// Map<String, String> pestMap = new HashMap<>();
		if (weedingMap.size() > 0) {
			return weedingMap;
		}

		if (!StringUtil.isEmpty(getBranchId()) && getBranchId().equalsIgnoreCase("bci")) {
			String preferences = preferncesService.findPrefernceByName("WEEDING");
			if (!StringUtil.isEmpty(preferences)) {
				weedingMap = getPropertyMap(preferences);
			} else {
				ESESystem eseRec = preferncesService.findPrefernceByOrganisationId("bci");
				List<FarmCatalogue> farmCatalogur = catalogueService.findCatalogueByTypezAndBranch(getText("weedingType"),
						eseRec.getName());
				for (FarmCatalogue fc : farmCatalogur) {
					if (!StringUtil.isEmpty(preferences)) {
						preferences = preferences + "," + fc.getCode() + "~" + fc.getName();
					} else {
						preferences = fc.getCode() + "~" + fc.getName();
					}
				}
				if (!StringUtil.isEmpty(preferences)) {
					weedingMap.put("WEEDING", preferences);
				}
				preferncesService.editOrganisationPreference(weedingMap, eseRec);
				weedingMap = getPropertyMap(preferences);
			}
		} else {
			List<FarmCatalogue> catList = catalogueService
					.findFarmCatalougeByAlpha(Integer.valueOf(getText("weedingType")));
			for (FarmCatalogue catalogue : catList) {
				weedingMap.put(catalogue.getCode(), catalogue.getName());
			}
		}
		return weedingMap;
	}
	Map<String, String> pickMap = new HashMap<>();
	public Map<String, String> getPickMap() {
		if (pickMap.size() > 0) {
			return pickMap;
		}

		
		if (!StringUtil.isEmpty(getBranchId()) && getBranchId().equalsIgnoreCase("bci")) {
			String preferences = preferncesService.findPrefernceByName("PICKING");

			if (!StringUtil.isEmpty(preferences)) {
				pickMap = getPropertyMap(preferences);
			} else {
				ESESystem eseRec = preferncesService.findPrefernceByOrganisationId("bci");
				List<FarmCatalogue> farmCatalogur = catalogueService.findCatalogueByTypezAndBranch(getText("picking"),
						eseRec.getName());
				for (FarmCatalogue fc : farmCatalogur) {
					if (!StringUtil.isEmpty(preferences)) {
						preferences = preferences + "," + fc.getCode() + "~" + fc.getName();
					} else {
						preferences = fc.getCode() + "~" + fc.getName();
					}
				}
				if (!StringUtil.isEmpty(preferences)) {
					pickMap.put("PICKING", preferences);
				}
				preferncesService.editOrganisationPreference(pickMap, eseRec);
				pickMap = getPropertyMap(preferences);
			}
		} else {
			List<FarmCatalogue> catList = catalogueService
					.findFarmCatalougeByAlpha(Integer.valueOf(getText("picking")));
			for (FarmCatalogue catalogue : catList) {
				pickMap.put(catalogue.getCode(), catalogue.getName());
			}
		}

		return pickMap;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
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
		InputStream is = getBciFieldInspectionDataStream();

		setXlsFileName(getText("bcixlsreportName") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getText("bcixlsreportName") + DateUtil.getRevisionNoDateTimeMilliSec(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(
				getText("bcixlsreportName") + DateUtil.getRevisionNoDateTimeMilliSec(), fileMap, ".xls"));

		return "xls";
	}

	public InputStream getBciFieldInspectionDataStream() throws IOException {
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		LinkedList<String> headersList = new LinkedList<String>();
		List<List<String>> dataList1 = new ArrayList<List<String>>();
		Farm farm = new Farm();
		this.filter = farm;
		if (!StringUtil.isEmpty(farmName)) {
			Farm frm = farmerService.findFarmByfarmId(farmName);
			filters.put(getText("farmName"), frm.getFarmName());
			filter.setFarmCode(farmName);

		}

		if (StringUtil.isEmpty(seasonCode)) {
			seasonCode = clientService.findCurrentSeasonCode();
			String seasonName = farmerService.findBySeasonCode(seasonCode);
			if (!StringUtil.isEmpty(seasonName)) {
				filters.put(getText("seasonCode"), seasonName);
			}
		} else {
			String seasonName = farmerService.findBySeasonCode(seasonCode);
			if (!StringUtil.isEmpty(seasonName)) {
				filters.put(getText("seasonCode"), seasonName);
			}
		}
		if (!StringUtil.isEmpty(farmerName)) {
			Farmer farmer = new Farmer();
			Farmer frmr = farmerService.findFarmerByFarmerId(farmerName);
			farmer.setFarmerId(farmerName);
			filters.put(getText("farmerName"), frmr.getFirstName());
			filter.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(fatherName)) {
			Farmer farmer = new Farmer();
			farmer.setLastName(fatherName);
			filters.put(getText("fatherName"), fatherName);
			filter.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(villageName)) {
			Farmer farmer = new Farmer();
			Village village = new Village();
			village.setCode(villageName);
			farmer.setVillage(village);
			Village vill = locationService.findVillageByCode(villageName);
			filters.put(getText("villageName"), vill.getName());
			filter.setFarmer(farmer);
		}
		if (!StringUtil.isEmpty(getBranchId()) || getBranchId() != null) {
			if (!ObjectUtil.isEmpty(filter.getFarmer())) {
				filter.getFarmer().setBranchId(getBranchId());
			} else {
				Farmer farmer = new Farmer();
				farmer.setBranchId(getBranchId());
				filter.setFarmer(farmer);
			}
		}
		super.filter = this.filter;
		Map datas = readData("bciFieldMonitoring");

		/* String headers=getText("reportHeader"); */
		String headers = getText("BCIbciFieldMonitoringReportHeader");

		/*if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (getBranchId().equalsIgnoreCase("organic")) {
				headers = getLocaleProperty("OrganicbciFieldMonitoringReportHeader");
			} else  {
				headers = getLocaleProperty("BCIbciFieldMonitoringReportHeader");
			}

		}*/

		if (headers != null) {

			for (String name : headers.split(",")) {
				headersList.add(getLocaleProperty(name));
			}
		}
		List<Object[]> mainGridRows = (List<Object[]>) datas.get(ROWS);
		List<PeriodicInspection> periodicInspectionList = farmerService.listPeriodicInspection();
		List<CultivationDetail> cultivationDetailList = farmerService.listCultivationDetail();
		if (StringUtil.isEmpty(seasonCode) || seasonCode.equals(null))
			seasonCode = clientService.findCurrentSeasonCode();
		// Long seasonId=farmerService.findSeasonBySeasonCode(seasonCode);
		HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
		StringBuffer bcVal = new StringBuffer();
		StringBuffer dat = new StringBuffer();
		Long serialNumber = 0L;
		for (Object[] obj : mainGridRows) {
			List<String> datasList = new ArrayList<>();
			serialNumber++;
			datasList.add(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : "");
			datasList.add(ObjectUtil.isEmpty(obj[1]) ? "NA" : obj[1].toString());
			if (!ObjectUtil.isEmpty(obj[5])) {
				datasList.add(ObjectUtil.isEmpty(obj[2]) ? "NA" : obj[2].toString());
				datasList.add(ObjectUtil.isEmpty(obj[3]) ? "NA" : obj[3].toString());
				datasList.add(ObjectUtil.isEmpty(obj[4]) ? "NA" : obj[4].toString());
			}
			
			if (!StringUtil.isEmpty(String.valueOf(obj[11].toString()))
					&& String.valueOf(obj[11].toString()).equals("0") && false) {
				if (!StringUtil.isEmpty(obj[11].toString()) && obj[11].toString() != null
						&& !obj[11].toString().equalsIgnoreCase("undefined"))
					bcVal.append(obj[11].toString() + ",");
				if (obj[13].toString() != null)
					dat.append(df.format(obj[13].toString()) + ",");

			}

			float totBc = 0;
			if (!StringUtil.isEmpty(bcVal.toString())) {
				String bc[] = bcVal.toString().split(",");
				for (String s : bc) {
					totBc += Float.parseFloat(s);
				}
			}
			datasList.add(String.valueOf(totBc));
			// Farmer
			// frm=farmerService.findFarmerById(Long.valueOf(obj[5].toString()));
			if (obj[10]!= null)
				datasList.add(getText("basicInfo" + obj[10].toString()));
			/*
			 * int count =
			 * farmerService.findLandPreparationDetailsByFarmCode(obj[6].
			 * toString(), season.getCode()); if (count > 0)
			 * datasList.add(getText("yes")); else datasList.add(getText("no"));
			 */
			StringBuffer count = new StringBuffer();
			periodicInspectionList.stream()
					.filter(periodicInspection -> periodicInspection.getFarmId().equals(obj[6].toString())
							&& periodicInspection.getSeasonCode().equals(obj[12]!=null?obj[12].toString():""))
					.forEach(periodicInspection -> {
						count.append("1");
					});
			// occurenceCount=farmerService.findWeedingStatusByCode(obj[7].toString(),s.getKey(),obj[6].toString(),seasonCode);
			if (!StringUtil.isEmpty(count)) {
				datasList.add(getText("no"));
			} else {
				datasList.add(getText("yes"));
			}

			/*
			 * String sowDat[]=dat.toString().split(",");
			 * dataList.add(!StringUtil.isEmpty(sowDat[0])?sowDat[0]:"NA");
			 */
			datasList.add(!StringUtil.isEmpty(dat.toString()) ? getText("yes") : getText("no"));
			StringBuffer seeDetail = new StringBuffer();
			if (!ObjectUtil.isListEmpty(farm.getFarmCrops()) && farm.getFarmCrops() != null) {
				farm.getFarmCrops().stream()
						.filter(o -> (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season))
						.forEach(o -> {
							seeDetail.append("success");
						});
			}
			if (!StringUtil.isEmpty(seeDetail.toString())) {
				datasList.add(getText("yes"));
			} else {
				datasList.add(getText("no"));
			}
			/*
			 * if(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) &&
			 * !StringUtil.isEmpty(farm.getFarmDetailedInfo().
			 * getIrrigationSource())){
			 * //dataList.add(StringUtil.removeLastComma(seedDet.toString()));
			 * dataList.add(farm.getFarmDetailedInfo().getIrrigationSource().
			 * equals("-1")?getText("irrigationNa"):getIrrigationSourceList().
			 * get(Integer.parseInt(farm.getFarmDetailedInfo().
			 * getIrrigationSource())));
			 * 
			 * } else{ dataList.add(getText("irrigationNa")); }
			 */

			if (!ObjectUtil.isEmpty(obj[15].toString())) {
				datasList.add(!StringUtil.isEmpty(obj[14]) && obj[14] != null ? getText("irrigationColl")
						: getText("irrigationNa"));

			}

			// PeriodicInspection
			// periodicInspection=farmerService.findperiodicInspectionByBranchIdAndFarmId(getBranchId(),farm.getFarmCode());
			/*
			 * for(Entry<String, String> s : getWeedingMap().entrySet()) {
			 * Integer occurenceCount =0;
			 * occurenceCount=farmerService.findWeedingStatusByCode(obj[7].
			 * toString(),s.getKey(),obj[6].toString(),seasonCode);
			 * if(occurenceCount==0 || occurenceCount==null){
			 * datasList.add(getText("no")); } else{
			 * datasList.add(getText("yes")); } }
			 */

			for (Entry<String, String> s : getWeedingMap().entrySet()) {
				StringBuffer occurenceCount = new StringBuffer();
				periodicInspectionList.stream()
						.filter(periodicInspection -> periodicInspection.getBranchId().equals(obj[7].toString())
								&& periodicInspection.getWeeding().equals(s.getKey())
								&& periodicInspection.getFarmId().equals(obj[6].toString())
								&& periodicInspection.getSeasonCode().equals(obj[12]!=null?obj[12].toString():""))
						.forEach(periodicInspection -> {
							occurenceCount.append("1");
						});
				// occurenceCount=farmerService.findWeedingStatusByCode(obj[7].toString(),s.getKey(),obj[6].toString(),seasonCode);
				if (!StringUtil.isEmpty(occurenceCount)) {
					datasList.add(getText("no"));
				} else {
					datasList.add(getText("yes"));
				}
			}

			for (Entry<String, String> s : getUsagegMap(obj[7].toString()).entrySet()) {
				// int occurs = 0;
				StringBuffer occurs = new StringBuffer();
				cultivationDetailList.stream().filter(
						cultivationDetail -> cultivationDetail.getCultivation().getBranchId().equals(obj[7].toString())
								&& cultivationDetail.getCultivation().getFarmId().equals(obj[6].toString())
								&& cultivationDetail.getUsageLevel()!=null
								&& cultivationDetail.getUsageLevel().equals(s.getKey())
								&& cultivationDetail.getType().equals(3l)
								&& cultivationDetail.getCultivation().getCurrentSeasonCode().equals(obj[12].toString()))
						.forEach(cultivationDetail -> {
							occurs.append("1");
						});
				// occurs=farmerService.findUsageCountforFertilizerFromCultivationDetails(obj[7].toString(),s.getKey(),obj[6].toString(),3l,seasonCode);

				if (!StringUtil.isEmpty(occurs)) {
					datasList.add(getText("no"));
				} else {
					datasList.add(getText("yes"));
				}
				
			}

			for (Entry<String, String> s : getUsagegMap(obj[7].toString()).entrySet()) {
				
			
				StringBuffer occurs = new StringBuffer();
				cultivationDetailList.stream().filter(
						cultivationDetail -> cultivationDetail.getCultivation().getBranchId().equals(obj[7].toString())
								&& cultivationDetail.getCultivation().getFarmId().equals(obj[6].toString())
								&& cultivationDetail.getUsageLevel()!=null
								&& cultivationDetail.getUsageLevel().equals(s.getKey())
								&& cultivationDetail.getType().equals(1l)
								&& cultivationDetail.getCultivation().getCurrentSeasonCode().equals(obj[12].toString()))
						.forEach(cultivationDetail -> {
							occurs.append("1");
						});
				// occurs=farmerService.findUsageCountforFertilizerFromCultivationDetails(obj[7].toString(),s.getKey(),obj[6].toString(),3l,seasonCode);

				if (!StringUtil.isEmpty(occurs)) {
					datasList.add(getText("no"));
				} else {
					datasList.add(getText("yes"));
				}
			}

			for (Entry<String, String> s : getUsagegMap(obj[7].toString()).entrySet()) {
				
				 
				StringBuffer occurs = new StringBuffer();
				cultivationDetailList.stream().filter(
						cultivationDetail -> cultivationDetail.getCultivation().getBranchId().equals(obj[7].toString())
								&& cultivationDetail.getCultivation().getFarmId().equals(obj[6].toString())
								&& cultivationDetail.getUsageLevel()!=null
								&& cultivationDetail.getUsageLevel().equals(s.getKey())
								&& cultivationDetail.getType().equals(2l)
								&& cultivationDetail.getCultivation().getCurrentSeasonCode().equals(obj[12].toString()))
						.forEach(cultivationDetail -> {
							occurs.append("1");
						});
				// occurs=farmerService.findUsageCountforFertilizerFromCultivationDetails(obj[7].toString(),s.getKey(),obj[6].toString(),3l,seasonCode);

				if (!StringUtil.isEmpty(occurs)) {
					datasList.add(getText("no"));
				} else {
					datasList.add(getText("yes"));
				}
			}

			for (Entry<String, String> s : getPickMap().entrySet()) {
				/*
				 * Integer occurenceCount = 0; occurenceCount =
				 * farmerService.findPickingStatusByCode(obj[7].toString(),
				 * s.getKey(), obj[6].toString(), seasonCode); if
				 * (occurenceCount == 0 || occurenceCount == null) {
				 * datasList.add(getText("no")); } else {
				 * datasList.add(getText("yes")); }
				 */
				StringBuffer occurenceCount = new StringBuffer();
				periodicInspectionList.stream()
						.filter(periodicInspection -> periodicInspection.getBranchId().equals(obj[7].toString())
								&& periodicInspection.getPicking()!=null
								&& periodicInspection.getPicking().equals(s.getKey())
								&& periodicInspection.getFarmId()!=null
								&& periodicInspection.getFarmId().equals(obj[6].toString())
								&& periodicInspection.getSeasonCode().equals(obj[12].toString()))
						.forEach(periodicInspection -> {
							occurenceCount.append("1");
						});
				// occurenceCount=farmerService.findWeedingStatusByCode(obj[7].toString(),s.getKey(),obj[6].toString(),seasonCode);
				if (!StringUtil.isEmpty(occurenceCount)) {
					datasList.add(getText("no"));
				} else {
					datasList.add(getText("yes"));
				}
			}

			dataList1.add(datasList);
		}
		Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);

		InputStream stream = ExportUtil.exportXLS(dataList1, headersList, filters, getText("bcifmreportName"),
				getText("filter"), getText("bcifmreportName"), existingAssetLogin.getFile());
		return stream;

	}

	Long serialNumber = 0L;

	public List<String> getDataForReport(Object obj) {
		List<String> dataList = new ArrayList<>();
		Farm farm = (Farm) obj;
		String farmer;
		serialNumber++;
		dataList.add(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : "");
		dataList.add(!StringUtil.isEmpty(farm.getFarmName()) ? farm.getFarmName() : "NA");
		if (!ObjectUtil.isEmpty(farm.getFarmer())) {
			dataList.add(!StringUtil.isEmpty(farm.getFarmer().getFarmerCode()) ? farm.getFarmer().getFarmerCode() : "");
			dataList.add(!StringUtil.isEmpty(farm.getFarmer().getFirstName()) ? farm.getFarmer().getFirstName() : "");
			// dataList.add(!StringUtil.isEmpty(farm.getFarmer().getLastName())?farm.getFarmer().getLastName():"");
			dataList.add(!ObjectUtil.isEmpty(farm.getFarmer().getVillage()) ? farm.getFarmer().getVillage().getName()
					: "NA");
		}
		if (StringUtil.isEmpty(seasonCode) || seasonCode.equals(null))
			seasonCode = clientService.findCurrentSeasonCode();
		// Long seasonId=farmerService.findSeasonBySeasonCode(seasonCode);
		HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
		StringBuffer bcVal = new StringBuffer();
		StringBuffer dat = new StringBuffer();

		if (!ObjectUtil.isEmpty(farm)) {
			Set<FarmCrops> farmCrops = farm.getFarmCrops();
			if (!ObjectUtil.isListEmpty(farmCrops)) {
				farmCrops.stream()
						.filter(o -> (!StringUtil.isEmpty(String.valueOf(o.getCropCategory()))
								&& String.valueOf(o.getCropCategory()).equals("0")
								&& (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season)))
						.forEach(o -> {
							if (!StringUtil.isEmpty(o.getCultiArea()) && o.getCultiArea() != null
									&& !o.getCultiArea().equalsIgnoreCase("undefined"))
								bcVal.append(o.getCultiArea() + ",");
							if (o.getSowingDate() != null)
								dat.append(df.format(o.getSowingDate()) + ",");

						});
			}
		}
		float totBc = 0;
		if (!StringUtil.isEmpty(bcVal.toString())) {
			String bc[] = bcVal.toString().split(",");
			for (String s : bc) {
				totBc += Float.parseFloat(s);
			}
		}
		dataList.add(String.valueOf(totBc));
		Farmer frm = farmerService.findFarmerById(farm.getFarmer().getId());
		dataList.add(getText("basicInfo" + frm.getBasicInfo()));
		int count = farmerService.findLandPreparationDetailsByFarmCode(farm.getFarmCode(), season.getCode());
		if (count > 0)
			dataList.add(getText("yes"));
		else
			dataList.add(getText("no"));
		/*
		 * String sowDat[]=dat.toString().split(",");
		 * dataList.add(!StringUtil.isEmpty(sowDat[0])?sowDat[0]:"NA");
		 */
		dataList.add(!StringUtil.isEmpty(dat.toString()) ? getText("yes") : getText("no"));
		StringBuffer seeDetail = new StringBuffer();
		if (!ObjectUtil.isListEmpty(farm.getFarmCrops()) && farm.getFarmCrops() != null) {
			farm.getFarmCrops().stream()
					.filter(o -> (!ObjectUtil.isEmpty(o.getCropSeason())) && o.getCropSeason().equals(season))
					.forEach(o -> {
						seeDetail.append("success");
					});
		}
		if (!StringUtil.isEmpty(seeDetail.toString())) {
			dataList.add(getText("yes"));
		} else {
			dataList.add(getText("no"));
		}
		/*
		 * if(!ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) &&
		 * !StringUtil.isEmpty(farm.getFarmDetailedInfo().getIrrigationSource())
		 * ){ //dataList.add(StringUtil.removeLastComma(seedDet.toString()));
		 * dataList.add(farm.getFarmDetailedInfo().getIrrigationSource().equals(
		 * "-1")?getText("irrigationNa"):getIrrigationSourceList().get(Integer.
		 * parseInt(farm.getFarmDetailedInfo().getIrrigationSource())));
		 * 
		 * } else{ dataList.add(getText("irrigationNa")); }
		 */

		if (!ObjectUtil.isEmpty(farm.getFarmDetailedInfo())) {
			dataList.add(!StringUtil.isEmpty(farm.getFarmDetailedInfo().getFarmIrrigation())
					&& farm.getFarmDetailedInfo().getFarmIrrigation() != null ? getText("irrigationColl")
							: getText("irrigationNa"));

		}

		// PeriodicInspection
		// periodicInspection=farmerService.findperiodicInspectionByBranchIdAndFarmId(getBranchId(),farm.getFarmCode());
		for (Entry<String, String> s : getWeedingMap().entrySet()) {
			Integer occurenceCount = 0;
			occurenceCount = farmerService.findWeedingStatusByCode(getBranchId(), s.getKey(), farm.getFarmCode(),
					seasonCode);
			if (occurenceCount == 0 || occurenceCount == null) {
				dataList.add(getText("no"));
			} else {
				dataList.add(getText("yes"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			int occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 3l, seasonCode);
			;
			if (occurs > 0) {
				dataList.add(getText("yes"));
			} else {
				dataList.add(getText("no"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			Integer occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 1l, seasonCode);
			;
			if (occurs == 0 || occurs == null) {
				dataList.add(getText("no"));
			} else {
				dataList.add(getText("yes"));
			}
		}

		for (Entry<String, String> s : getUsagegMap(farm.getFarmer().getBranchId()).entrySet()) {
			Integer occurs = 0;
			occurs = farmerService.findUsageCountforFertilizerFromCultivationDetails(getBranchId(), s.getKey(),
					farm.getFarmCode(), 2l, seasonCode);
			;
			if (occurs == 0 || occurs == null) {
				dataList.add(getText("no"));
			} else {
				dataList.add(getText("yes"));
			}

		}

		for (Entry<String, String> s : getPickMap().entrySet()) {
			Integer occurenceCount = 0;
			occurenceCount = farmerService.findPickingStatusByCode(getBranchId(), s.getKey(), farm.getFarmCode(),
					seasonCode);
			if (occurenceCount == 0 || occurenceCount == null) {
				dataList.add(getText("no"));
			} else {
				dataList.add(getText("yes"));
			}
		}
		return dataList;
	}

	/*public Map<String, String> getFarmList() {

		Map<String, String> farmListMap = new LinkedHashMap<String, String>();
		
		 * List<Farm> farmsList = farmerService.listFarm(); for(Farm
		 * frm:farmsList) farmListMap.put(frm.getFarmCode(), frm.getFarmName());
		 
		farmListMap = farmerService.listFarmInfo().stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[2])));
		farmerService.listFarmInfo().stream().forEach(obj->{
		if(!farmListMap.containsKey(obj[2])){
			farmListMap.put(String.valueOf(obj[2]), String.valueOf(obj[2]));
		}
	});
		return farmListMap;

	}*/

	public String getFarmName() {
		return farmName;
	}

	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}
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
	public void populatFarmerList (){
		 JSONArray farmerArr = new JSONArray();
		 List<Object[]> farmersList = farmerService.listFarmerInfo();
		  if (!ObjectUtil.isEmpty(farmersList)) {
			  farmersList.forEach(obj -> {
				  farmerArr.add(getJSONObject(obj[1].toString(), obj[3].toString()));
		   });
		  }
		  sendAjaxResponse(farmerArr);
	}
	public void populateFatherNameList(){
		 JSONArray fatherArr = new JSONArray();
		 List<Object> fatherList = farmerService.listPeriodicInsoectionFatherName();
		  if (!ObjectUtil.isEmpty(fatherList)) {
			  fatherList.forEach(obj -> {
				  fatherArr.add(getJSONObject(obj.toString(), obj.toString()));
		   });
		  }
		  sendAjaxResponse(fatherArr);
	}
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
	public void populateSeasonList (){
		 JSONArray seasonArr = new JSONArray();
		  List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		  if (!ObjectUtil.isEmpty(seasonList)) {
		   seasonList.forEach(obj -> {
		    seasonArr.add(getJSONObject(obj[0].toString(), obj[1].toString()+"-"+obj[0].toString()));
		   });
		  }
		  sendAjaxResponse(seasonArr);
	}
}
