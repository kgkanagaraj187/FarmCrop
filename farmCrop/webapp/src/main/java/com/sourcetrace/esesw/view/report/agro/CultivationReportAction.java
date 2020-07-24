/*
tabIndex * ProcurementReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */

public class CultivationReportAction extends BaseReportAction implements IExporter {
	private static final long serialVersionUID = -6010922051631846717L;
	private static final Logger LOGGER = Logger.getLogger(CultivationReportAction.class);
	private Map<String, String> fields = new LinkedHashMap<>();
	private Cultivation filter = new Cultivation();
	private IReportService cultivationReportService;
	private String farmerName;
	private String farmerCode;
	private String village;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private List<Cultivation> cultivationList;
	private List<CultivationDetail> cultivationDetails;
	private String xlsFileName;
	private InputStream fileInputStream;
	private String seasonCode;
	List<CultivationDetail> fertilizersList = new ArrayList<CultivationDetail>();
	List<CultivationDetail> pesticidesList = new ArrayList<CultivationDetail>();
	List<CultivationDetail> fymsList = new ArrayList<CultivationDetail>();

	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat fieldStaffDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private IProductService productService;
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;

	private IClientService clientService;
	private String branchIdParma;
	private String farmerId;
	private String daterange;
	private String fatherName;
	private String exportLimit;
	private String farmerCodeEnabled;
	private String sowDate;
	private String cropCode;
	private String type;
	private String command;
	private String manureDatas;
	private String pesticideDatas;
	private String fertilizerDatas;
	private String landTotalDatas;
	private String sowingDatas;
	private String gapDatas;
	private String weedingDatas;
	private String cultureDatas;
	private String irrigationDatas;
	private String festiTotalDatas;
	private String harvestDatas;
	private String otherExpenceDatas;
	private String totPestiDatas;
	private String totalManureDatas;
	private String fertiCultivationIds;
	private String pestiCultivationIds;
	private String manureCultivationIds;
	private String labourCostDatas;
	private String totalCoc;
	private String stateName;
	private String icsName;
	private String fpo;
	private String quty;
	private ICatalogueService catalogueService;
	Map<String, String> seasonMap = new LinkedHashMap();
	Map<String, Object[]> farmMap = new LinkedHashMap();
	Map<String, Object[]> farmCropMap = new LinkedHashMap();
	Map<String, String> villageMap = new LinkedHashMap<>();
	Map<String, String> farmerCodList = new LinkedHashMap<>();
	Map<String, String> farmerNameList = new LinkedHashMap<>();
	Map<String, String> fatherNameList = new LinkedHashMap<>();
	Map<String, String> farmerFarmerIdList = new LinkedHashMap<>();
	Map<String, Object[]> qtyMap = new LinkedHashMap();
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IFarmCropsService farmCropsService;

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public String getLandTotalDatas() {
		return landTotalDatas;
	}

	public void setLandTotalDatas(String landTotalDatas) {
		this.landTotalDatas = landTotalDatas;
	}

	public String getSowingDatas() {
		return sowingDatas;
	}

	public void setSowingDatas(String sowingDatas) {
		this.sowingDatas = sowingDatas;
	}

	public String getGapDatas() {
		return gapDatas;
	}

	public void setGapDatas(String gapDatas) {
		this.gapDatas = gapDatas;
	}

	public String getWeedingDatas() {
		return weedingDatas;
	}

	public void setWeedingDatas(String weedingDatas) {
		this.weedingDatas = weedingDatas;
	}

	public String getCultureDatas() {
		return cultureDatas;
	}

	public void setCultureDatas(String cultureDatas) {
		this.cultureDatas = cultureDatas;
	}

	public String getIrrigationDatas() {
		return irrigationDatas;
	}

	public void setIrrigationDatas(String irrigationDatas) {
		this.irrigationDatas = irrigationDatas;
	}

	public String getFestiTotalDatas() {
		return festiTotalDatas;
	}

	public void setFestiTotalDatas(String festiTotalDatas) {
		this.festiTotalDatas = festiTotalDatas;
	}

	public String getHarvestDatas() {
		return harvestDatas;
	}

	public void setHarvestDatas(String harvestDatas) {
		this.harvestDatas = harvestDatas;
	}

	public String getOtherExpenceDatas() {
		return otherExpenceDatas;
	}

	public void setOtherExpenceDatas(String otherExpenceDatas) {
		this.otherExpenceDatas = otherExpenceDatas;
	}

	public String getTotPestiDatas() {
		return totPestiDatas;
	}

	public void setTotPestiDatas(String totPestiDatas) {
		this.totPestiDatas = totPestiDatas;
	}

	public String getTotalManureDatas() {
		return totalManureDatas;
	}

	public void setTotalManureDatas(String totalManureDatas) {
		this.totalManureDatas = totalManureDatas;
	}

	public String getManureDatas() {
		return manureDatas;
	}

	public void setManureDatas(String manureDatas) {
		this.manureDatas = manureDatas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public IReportService getCultivationReportService() {

		return cultivationReportService;
	}

	public void setCultivationReportService(IReportService cultivationReportService) {

		this.cultivationReportService = cultivationReportService;
	}

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
		ESESystem preferences = preferncesService.findPrefernceById("1");

		List<Object[]> farmerList = new ArrayList<>();
			farmerList = farmerService.listFarmerInformationByCultivation();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.stream().forEach(obj -> {

				if (!villageMap.containsKey(obj[4].toString())) {
					villageMap.put(obj[4].toString(), obj[2].toString());
				}
/*
				if (obj[3] != null && !farmerCodList.containsKey(obj[3].toString())) {
					farmerCodList.put(obj[3].toString(), obj[3].toString());
				}*/

				if (!farmerNameList.containsKey(obj[0])) {
					farmerNameList.put(obj[0].toString(), obj[0].toString());
				}
				if (!farmerFarmerIdList.containsKey(obj[3])) {
					farmerFarmerIdList.put(obj[3].toString(), obj[3].toString());
				}
				/*if (!fatherNameList.containsKey(obj[1]) && obj[1] != null) {
					fatherNameList.put(obj[1].toString(), obj[1].toString());
				}*/

			});
		}
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}

		request.setAttribute(HEADING, getText(LIST));
		filter = new Cultivation();
		return LIST;
	}

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String data() throws Exception {

		setFilter(new Cultivation());

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setCurrentSeasonCode(seasonCode);
		} 
		/*
		 * else { if (!StringUtil.isEmpty(getBranchId())) {
		 * filter.setCurrentSeasonCode(clientService.findCurrentSeasonCode() );
		 * seasonCode = clientService.findCurrentSeasonCode(); } }
		 */
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}
		if (!StringUtil.isEmpty(cropCode)) {
			filter.setCropCode(cropCode);
		}
		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);
		}

		if (!StringUtil.isEmpty(village)) {
			Village v = locationService.findVillageByCode(village);
			filter.setVillageId(String.valueOf(v.getId()));
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}
		}
		filter.setTxnType("2");// 1 - Income 2 - Expense
		super.filter = this.filter;
		Map data = readData("cultivation");
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		Object[] datas = (Object[]) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		// setFarmerCodeEnabled("0");
		if (!StringUtil.isEmpty(preferences) && StringUtil.isEmpty(farmerCodeEnabled)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[2])))
						? getBranchesMap().get(getParentBranchMap().get(datas[2])) : getBranchesMap().get(datas[2]));
			}
			rows.add(getBranchesMap().get(datas[2]));
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(datas[2]));
			}
		}

		// HarvestSeason season =
		// farmerService.findSeasonNameByCode(datas[6].toString());
		rows.add(!ObjectUtil.isEmpty(getSeasonMap().get(datas[5].toString())) ? getSeasonMap().get(datas[5].toString())
				: "");
		// Farmer farmer =
		// farmerService.findFarmerByFarmerId(datas[1].toString());
		Object[] farmer = farmerService.findFarmerCodeNameVillageSamithibyFarmerId(datas[1].toString());
		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			if (getBranchId() == null || !getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
				rows.add(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer[0]) ? farmer[0].toString() : "");
			}
		}
		
		// Farm farm = farmerService.findFarmByfarmId(datas[0].toString());
		Object[] farm = getFarmMap().get(datas[0].toString());

		rows.add(farm!=null && !ObjectUtil.isEmpty(farm) && farm[3]!=null && !ObjectUtil.isEmpty(farm[3]) ? farm[3] : "");
		// rows.add(!ObjectUtil.isEmpty(farm) ? farm.getFarmer().getLastName() :
		// "");
		rows.add(farm!=null &&!ObjectUtil.isEmpty(farm) &&farm[5]!=null && !ObjectUtil.isEmpty(farm[5]) ? farm[5] : "");

		/*
		 * if (ObjectUtil.isEmpty(farmCrop)) { ProcurementProduct
		 * procurementProduct = productDistributionService
		 * .findProcurementProductByCode(!ObjectUtil.isEmpty(datas[2]) ?
		 * datas[2].toString() : "");
		 * 
		 */
		/*
	

		rows.add(!ObjectUtil.isEmpty(farm) ? farm[12] : "");
		/*
		 * if (!getCurrentTenantId().equals("pratibha")) {
		 * 
		 * rows.add(!ObjectUtil.isEmpty(farm) ?
		 * farm.getFarmer().getVillage().getCity().getName() : "");
		 * rows.add(!ObjectUtil.isEmpty(farm) ?
		 * farm.getFarmer().getVillage().getCity().getLocality().getName() :
		 * ""); rows.add(!ObjectUtil.isEmpty(farm) ?
		 * farm.getFarmer().getVillage().getCity().getLocality().getState().
		 * getName() : ""); }
		 */
	
		Village village = locationService.findVillageById(Long.valueOf(datas[7].toString()));
		rows.add(!ObjectUtil.isEmpty(village) ? village.getName() : "");
		
		rows.add(datas[8]!=null && !ObjectUtil.isEmpty(datas[8])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[8].toString()), "##.00") : "0.00");// land
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {																							// prepartion
		rows.add(datas[21]!=null && !ObjectUtil.isEmpty(datas[21])
				?CurrencyUtil.getDecimalFormat(Double.valueOf(datas[21].toString()), "##.00") : "0.00"); // Land preparation labour cost
		}
		rows.add(datas[9]!=null && !ObjectUtil.isEmpty(datas[9])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[9].toString()), "##.00") : "0.00");// total Sowing
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[22]!=null && !ObjectUtil.isEmpty(datas[22])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[22].toString()), "##.00") : "0.00");// total Sowing labour cost
		}
		rows.add(datas[10]!=null && !ObjectUtil.isEmpty(datas[10])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[10].toString()), "##.00") : "0.00");// total Gap filling and Thinning
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[23]!=null && !ObjectUtil.isEmpty(datas[23])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[23].toString()), "##.00") : "0.00");// total Gap filling and Thinning labour cost
		}
		rows.add(datas[11]!=null && !ObjectUtil.isEmpty(datas[11])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[11].toString()), "##.00") : "0.00");// total weed
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[24]!=null && !ObjectUtil.isEmpty(datas[24])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[24].toString()), "##.00") : "0.00");// total weed labour cost
		}
		rows.add(datas[12]!=null && !ObjectUtil.isEmpty(datas[12])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[12].toString()), "##.00") : "0.00");// total culture
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[25]!=null && !ObjectUtil.isEmpty(datas[25])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[25].toString()), "##.00") : "0.00");// total culture labour cost
		}
																										
		rows.add(datas[13]!=null && !ObjectUtil.isEmpty(datas[13])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[13].toString()), "##.00") : "0.00");// total irrigation
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[26]!=null && !ObjectUtil.isEmpty(datas[26])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[26].toString()), "##.00") : "0.00");// total irrigation labour cost
		}
																										
		rows.add(datas[14]!=null && !ObjectUtil.isEmpty(datas[14])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[14].toString()), "##.00") : "0.00");// total fertilizer
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[27]!=null && !ObjectUtil.isEmpty(datas[27])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[27].toString()), "##.00") : "0.00");// total fertilizer Labour cost
		}

		rows.add(datas[15]!=null && !ObjectUtil.isEmpty(datas[15])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[15].toString()), "##.00") : "0.00");// total pesticide 
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[28]!=null && !ObjectUtil.isEmpty(datas[28])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[28].toString()), "##.00") : "0.00");// total pesticide	labour cost	
		}

		rows.add(datas[16]!=null && !ObjectUtil.isEmpty(datas[16])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[16].toString()), "##.00") : "0.00"); //Total Manure
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[29]!=null && !ObjectUtil.isEmpty(datas[29])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[29].toString()), "##.00") : "0.00"); //Total manure labour cost
		}
			// d ex=df.format(cultivation.getOtherExpeness());

		rows.add(datas[17]!=null && !ObjectUtil.isEmpty(datas[17])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[17].toString()), "##.00") : "0.00");// total harvest
		if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
		rows.add(datas[30]!=null && !ObjectUtil.isEmpty(datas[30])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[30].toString()), "##.00") : "0.00");// total harvest labour cost
		
		rows.add(datas[31]!=null && !ObjectUtil.isEmpty(datas[31])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[31].toString()), "##.00") : "0.00");
		rows.add(datas[32]!=null && !ObjectUtil.isEmpty(datas[32])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[32].toString()), "##.00") : "0.00");
		rows.add(datas[33]!=null && !ObjectUtil.isEmpty(datas[33])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[33].toString()), "##.00") : "0.00");
	}
		rows.add(datas[18]!=null && !ObjectUtil.isEmpty(datas[18])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[18].toString()), "##.00") : "0.00");
		rows.add(datas[20]!=null && !ObjectUtil.isEmpty(datas[20])
					? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[20].toString()), "##.00") : "0.00");// labourcost
		rows.add(datas[19]!=null && !ObjectUtil.isEmpty(datas[19])
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[19].toString()), "##.00") : "0.00");
		jsonObject.put("id", datas[4].toString());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public String detail() {
		if (this.id != null && !this.id.equals("")) {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));
			if (StringUtil.isEmpty(seasonCode)) {
				seasonCode = clientService.findCurrentSeasonCode();
			} 

			cultivationDetails = new ArrayList<CultivationDetail>();
			// Farm farm=farmerService.findFarmByCode(this.id);
			// cultivationList=farmerService.findByCultivationId(this.id);
			// //Note:
			// commented to fix bug 1077.
			/*
			 * cultivationList =
			 * farmerService.findCostOfCultivationsByFarmerCodeAndSeason(
			 * farmerService.findByCultivationId(this.id).get(0).getFarmerId(),
			 * farmerService.findByCultivationId(this.id).get(0).getFarmId(),
			 * seasonCode);
			 */
			/*
			 * cultivationList =
			 * farmerService.findCOCByFarmerIdFarmIdCropCodeSeason(
			 * farmerService.findByCultivationId(this.id).get(0).getFarmerId(),
			 * farmerService.findByCultivationId(this.id).get(0).getFarmId(),
			 * farmerService.findByCultivationId(this.id).get(0).getCropCode(),
			 * seasonCode);
			 */

			Cultivation coc = farmerService.findByCultivationId(this.id);
			
			if (!ObjectUtil.isEmpty(coc)) {
				cultivationList = farmerService.findCOCByFarmerIdFarmIdSeason(coc.getFarmerId(), coc.getFarmId(),
						seasonCode);

				for (Cultivation cultivation : cultivationList) {

					List<CultivationDetail> cultivationDetail = farmerService
							.findCultivationDetailsByCultivationIdAndSession(cultivation.getId());
					// totalCostOfCultivation+=cultivation.getTotalCost();
					cultivation.setTotLandPreCst(String.valueOf((cultivation.getLandTotal()!=null && !StringUtil.isEmpty(cultivation.getLandTotal())?Double.parseDouble(cultivation.getLandTotal()):0.00)+(cultivation.getLandLabourCost()!=null && !StringUtil.isEmpty(cultivation.getLandLabourCost())?Double.parseDouble(cultivation.getLandLabourCost()):0.00)));
					cultivation.setTotSowCst(String.valueOf((cultivation.getTotalSowing()!=null && !StringUtil.isEmpty(cultivation.getTotalSowing())?Double.parseDouble(cultivation.getTotalSowing()):0.00)+(cultivation.getSowingLabourCost()!=null && !StringUtil.isEmpty(cultivation.getSowingLabourCost())?Double.parseDouble(cultivation.getSowingLabourCost()):0.00)));
					cultivation.setTotGapCst(String.valueOf((cultivation.getTotalGap()!=null && !StringUtil.isEmpty(cultivation.getTotalGap())?Double.parseDouble(cultivation.getTotalGap()):0.00)+(cultivation.getGapLabourCost()!=null && !StringUtil.isEmpty(cultivation.getGapLabourCost())?Double.parseDouble(cultivation.getGapLabourCost()):0.00)));
					cultivation.setTotWeedCst(String.valueOf((cultivation.getTotalWeed()!=null && !StringUtil.isEmpty(cultivation.getTotalWeed())?Double.parseDouble(cultivation.getTotalWeed()):0.00)+(cultivation.getWeedLabourCost()!=null && !StringUtil.isEmpty(cultivation.getWeedLabourCost())?Double.parseDouble(cultivation.getWeedLabourCost()):0.00)));
					cultivation.setTotInputCst(String.valueOf((cultivation.getTotalCulture()!=null && !StringUtil.isEmpty(cultivation.getTotalCulture())?Double.parseDouble(cultivation.getTotalCulture()):0.00)+(cultivation.getCultureLabourCost()!=null && !StringUtil.isEmpty(cultivation.getCultureLabourCost())?Double.parseDouble(cultivation.getCultureLabourCost()):0.00)));
					cultivation.setTotIrriCst(String.valueOf((cultivation.getTotalIrrigation()!=null && !StringUtil.isEmpty(cultivation.getTotalIrrigation())?Double.parseDouble(cultivation.getTotalIrrigation()):0.00)+(cultivation.getIrriLabourCost()!=null && !StringUtil.isEmpty(cultivation.getIrriLabourCost())?Double.parseDouble(cultivation.getIrriLabourCost()):0.00)));
					cultivation.setTotFerCst(String.valueOf((cultivation.getTotalFertilizer()!=null && !StringUtil.isEmpty(cultivation.getTotalFertilizer())?Double.parseDouble(cultivation.getTotalFertilizer()):0.00)+(cultivation.getFertLabourcost()!=null && !StringUtil.isEmpty(cultivation.getFertLabourcost())?Double.parseDouble(cultivation.getFertLabourcost()):0.00)));
					cultivation.setTotPestCst(String.valueOf((cultivation.getTotalPesticide()!=null && !StringUtil.isEmpty(cultivation.getTotalPesticide())?Double.parseDouble(cultivation.getTotalPesticide()):0.00)+(cultivation.getPestLabourCost()!=null && !StringUtil.isEmpty(cultivation.getPestLabourCost())?Double.parseDouble(cultivation.getPestLabourCost()):0.00)));
					cultivation.setTotHarvCst(String.valueOf((cultivation.getTotalHarvest()!=null && !StringUtil.isEmpty(cultivation.getTotalHarvest())?Double.parseDouble(cultivation.getTotalHarvest()):0.00)+(cultivation.getHarvestLabourCost()!=null && !StringUtil.isEmpty(cultivation.getHarvestLabourCost())?Double.parseDouble(cultivation.getHarvestLabourCost()):0.00)));
					cultivation.setTotManureCst(String.valueOf((cultivation.getTotalManure()!=null && !StringUtil.isEmpty(cultivation.getTotalManure())?Double.parseDouble(cultivation.getTotalManure()):0.00)+(cultivation.getManureLabourCost()!=null && !StringUtil.isEmpty(cultivation.getManureLabourCost())?Double.parseDouble(cultivation.getManureLabourCost()):0.00)));
					if (!ObjectUtil.isListEmpty(cultivationDetail)) {
						cultivationDetails.addAll(cultivationDetail);
					}

					for (CultivationDetail cultiDetail : cultivationDetail) {
						cultiDetail.setCultivation(cultivation);
						if (cultiDetail.getType() == 1) {
							fertilizersList.add(cultiDetail);
						}
						if (cultiDetail.getType() == 2) {
							pesticidesList.add(cultiDetail);
						}
						if (cultiDetail.getType() == 3) {
							fymsList.add(cultiDetail);
						}

					}
				}
			}
			return "detail";
		} else {
			addActionError(NO_RECORD);
			return REDIRECT;
		}
	}

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */

	/*
	 * public Map<String, String> getVillageList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); // List<Object[]> farmerList = //
	 * farmerService.listFarmerInfoByCultivation();
	 * 
	 * List<String> villagesList = new ArrayList<>();
	 * if(!ObjectUtil.isEmpty(farmerList)){ villagesList =
	 * farmerList.stream().map(obj->String.valueOf(obj[0])).distinct().
	 * collect(Collectors.toList());
	 * farmerMap=villagesList.stream().collect(Collectors.toMap(String::
	 * toString, String::toString));
	 * 
	 * }
	 * 
	 * List<Object[]> farmerList = farmerService.listOfVillageByCultivation();
	 * farmerMap = farmerList.stream() .collect(Collectors.toMap(obj ->
	 * String.valueOf(obj[0]), obj -> String.valueOf(obj[1]))); farmerMap =
	 * farmerMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerMap; }
	 */
	/**
	 * Gets the farmerCode list.
	 * 
	 * @return the farmerCode list
	 */
	/*
	 * public List<String[]> getFarmerCodList() {
	 * 
	 * 
	 * List<String[]> farmerCodList = farmerService.listFarmerId();
	 * 
	 * return farmerCodList;
	 * 
	 * }
	 */

	/*
	 * public Map<String, String> getFarmerCodList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByCultivation(); List<String> testList = new
	 * ArrayList<>(); if (!ObjectUtil.isEmpty(farmerList)) { testList =
	 * farmerList.stream().map(obj ->
	 * String.valueOf(obj[3])).distinct().collect(Collectors.toList());
	 * farmerMap = testList.stream().collect(Collectors.toMap(String::toString,
	 * String::toString));
	 * 
	 * } return farmerMap; }
	 */
	/**
	 * Gets the farmerName list.
	 * 
	 * @return the farmerName list
	 */
	/*
	 * public Map<String, String> getFarmerNameList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByCultivation(); if
	 * (!ObjectUtil.isEmpty(farmerList)) { farmerList.stream().forEach(obj -> {
	 * if (!farmerMap.containsKey(obj[0])) { farmerMap.put(obj[0].toString(),
	 * obj[0].toString()); } }); } return farmerMap; }
	 */

	public Map<String, String> getCropNameList() {
		Map<String, String> cropNameList = new LinkedHashMap<>();
		Map<String, String> cropNameMap = new LinkedHashMap<>();
		List<Object[]> cropList = productDistributionService.listProcurementProductByCultivation();
		if (!ObjectUtil.isEmpty(cropList)) {
			cropNameList = cropList.stream()
					.collect(Collectors.toMap(obj -> String.valueOf(obj[0]), obj -> String.valueOf(obj[1])));
			cropNameList.entrySet().stream().sorted(Map.Entry.<String, String>comparingByValue())
					.forEachOrdered(x -> cropNameMap.put(x.getKey(), x.getValue()));
		}
		return cropNameMap;

	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("CultivationReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cultivationReport"), fileMap, ".xls"));
		return "xls";
	}

	public void populateFarmerData() throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (!StringUtil.isEmpty(seasonCode)) {

			List<Object[]> farmerList = farmerService.listFarmerInfoByCultivation(seasonCode);

			JSONArray village = new JSONArray();

			JSONArray farmerName = new JSONArray();

			JSONArray farmerFarmerId = new JSONArray();
			if (!ObjectUtil.isEmpty(farmerList)) {
				JSONObject villageMap = new JSONObject();
				JSONObject farmerCodList = new JSONObject();
				JSONObject farmerNameList = new JSONObject();
				JSONObject farmerFarmerIdList = new JSONObject();
				JSONObject fatherNameList = new JSONObject();

				farmerList.stream().forEach(obj -> {
					if (!villageMap.containsKey(obj[4].toString())) {
						villageMap.put(obj[4].toString(), obj[2].toString());
					}

					if (!farmerNameList.containsKey(obj[0])) {
						farmerNameList.put(obj[0].toString(), obj[0].toString());
					}
					if (!farmerFarmerIdList.containsKey(obj[3])) {
						farmerFarmerIdList.put(obj[3].toString(), obj[3].toString());
					}

					village.add(villageMap);

					farmerName.add(farmerNameList);

					farmerFarmerId.add(farmerFarmerIdList);

				});
				jsonObject.put("village", villageMap);

				jsonObject.put("farmerNameList", farmerNameList);

				jsonObject.put("farmerFarmerIdList", farmerFarmerIdList);
			}

		}
		sendAjaxResponse(jsonObject);
	}

	@SuppressWarnings("unchecked")
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);

		setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));

		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportCultivationReportTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle cellStyle3 = wb.createCellStyle();

		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1 = null, filterRow2 = null, filterRow3 = null,
				filterRow4 = null, filterRow5 = null, filterRow6 = null, filterRow7 = null, filterRow8 = null,
				filterRow9 = null, filterRow10 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 0;
		int titleRow1 = 2;
		int titleRow2 = 5;

		int rowNum = 2;
		int colNum = 0;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		sheet.setDefaultColumnWidth(13);

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportCultivationReportTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if(!StringUtil.isEmpty(farmerName) || !StringUtil.isEmpty(village) || !StringUtil.isEmpty(seasonCode) ||
					!StringUtil.isEmpty(cropCode) || !StringUtil.isEmpty(stateName) || !StringUtil.isEmpty(icsName) ||
					!StringUtil.isEmpty(fpo) || !StringUtil.isEmpty(branchIdParma))
			{
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			//filterRow1 = sheet.createRow(rowNum++);
			if (!StringUtil.isEmpty(farmerName)) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmerName.toString()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if (!StringUtil.isEmpty(village)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(village)
						? locationService.findVillageByCode(village).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(seasonCode)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("seasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue((farmerService.findSeasonNameByCode(seasonCode)).toString());
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (!StringUtil.isEmpty(cropCode)) {
				ProcurementProduct procurementProduct = productDistributionService
						.findProcurementProductByCode(!ObjectUtil.isEmpty(cropCode) ? cropCode.toString() : "");

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cropCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(procurementProduct) ? procurementProduct.getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(stateName)) {

				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				State s = locationService.findStateByCode(stateName);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(icsName)) {
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(icsName);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(fpo)) {
				filterRow7 = sheet.createRow(rowNum++);
				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fpoGroup")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				FarmCatalogue fc = farmerService.findfarmcatalogueByCode(fpo);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(branchIdParma)) {

				filterRow8 = sheet.createRow(rowNum++);
				cell = filterRow8.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("BranchId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow8.createCell(2);
				cell.setCellValue(new HSSFRichTextString(branchIdParma));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			// filterRow6 = sheet.createRow(rowNum++);
			// filterRow7 = sheet.createRow(rowNum++);
		}

		rowNum++;
		setFarmerCodeEnabled(preferncesService.findPrefernceByName(ESESystem.ENABLE_FARMER_CODE));

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportCultivationColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportCultivationColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCultivationColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganicCultivationExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCICultivationExportHeader");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCultivationColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportCultivationColumnHeader");
			}
		}

		int mainGridIterator = 0;
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
			} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
				cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
			}
			if (StringUtil.isEmpty(branchIdValue)) { // Add branch header to
														// export sheet if main
														// branch logged in.
				cell = mainGridRowHead.createCell(mainGridIterator);

				cell.setCellValue(new HSSFRichTextString(cellHeader));
				style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font2);
				sheet.setColumnWidth(mainGridIterator, (15 * 550));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase(getText("app.branch"))) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(style2);
					font2.setBoldweight((short) 12);
					font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style2.setFont(font2);
					sheet.setColumnWidth(mainGridIterator, (15 * 550));
					mainGridIterator++;
				}
			}

		}
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new Cultivation();

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setCurrentSeasonCode(seasonCode);
		} 

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}

		if (!StringUtil.isEmpty(cropCode)) {
			filter.setCropCode(cropCode);
		}

		if (!StringUtil.isEmpty(village)) {
			Village v = locationService.findVillageByCode(village);
			filter.setVillageId(String.valueOf(v.getId()));
		}

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				// State s = locationService.findStateByCode(stateName);
				filter.setStateName(stateName);
			}

			if (!StringUtil.isEmpty(fpo)) {
				filter.setFpo(fpo);
			}

			if (!StringUtil.isEmpty(icsName)) {

				filter.setIcsName(icsName);
			}
		}

		filter.setTxnType("2");


		/*
		 * if (!ObjectUtil.isEmpty(filter.getFarmerName()) &&
		 * !StringUtil.isEmpty(filter.getFarmerName()) && flag) { cell =
		 * filterRow3.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("farmerName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow3.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(farm.getFarmer().getFirstName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * flag = false; }
		 * 
		 * if (!getCurrentTenantId().equals("indev")) { if
		 * (!ObjectUtil.isEmpty(filter.getCropCode()) &&
		 * !StringUtil.isEmpty(filter.getCropCode()) && flag) {
		 * 
		 * cell = filterRow4.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("crop")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * ProcurementProduct procurementProduct = productDistributionService
		 * .findProcurementProductByCode(!ObjectUtil.isEmpty(datas[2]) ?
		 * datas[2].toString() : "");
		 * 
		 * cell = filterRow4.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(procurementProduct.getName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); } }
		 * 
		 * if (!ObjectUtil.isEmpty(filter.getCurrentSeasonCode()) &&
		 * !StringUtil.isEmpty(filter.getCurrentSeasonCode())) { cell =
		 * filterRow4.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("cSeasonCode")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow4.createCell(2); HarvestSeason season =
		 * farmerService.findSeasonNameByCode(datas[6].toString());
		 * cell.setCellValue(new HSSFRichTextString(season.getName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); }
		 * 
		 * if (!ObjectUtil.isEmpty(filter.getVillageId()) &&
		 * !StringUtil.isEmpty(filter.getVillageId())) { cell =
		 * filterRow5.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("villageName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow5.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(farm.getFarmer().getVillage().getName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { cell =
		 * filterRow6.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("mainOrganization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow6.createCell(2); // cell.setCellValue(new //
		 * HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
		 * cell.setCellValue(new HSSFRichTextString(
		 * getBranchesMap().get(getParentBranchMap().get(filter.getBranchId())))
		 * ); filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("subOrganization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString((getBranchesMap().get(filter.getBranchId()))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * } else { if
		 * (!StringUtil.isEmpty(getBranchesMap().get(filter.getBranchId()))) {
		 * cell = filterRow7.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("subOrganization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString((getBranchesMap().get(filter.getBranchId()))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); } }
		 * 
		 * } else {
		 * 
		 * if (!StringUtil.isEmpty(branchIdParma)) { filterRow7 =
		 * sheet.createRow(rowNum++); cell = filterRow7.createCell(1);
		 * cell.setCellValue(new HSSFRichTextString(getText("organization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(2); BranchMaster branch =
		 * clientService.findBranchMasterByBranchId(branchIdParma);
		 * cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch)
		 * ? branch.getName() : ""))); // cell.setCellValue(new
		 * HSSFRichTextString(branchIdParma)); filterFont.setBoldweight((short)
		 * 12); filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); }
		 * 
		 * } if (getCurrentTenantId().equals("chetna")) {
		 * 
		 * if (!ObjectUtil.isEmpty(filter.getStateName()) &&
		 * !StringUtil.isEmpty(filter.getStateName())) { cell =
		 * filterRow8.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("stateName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow8.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(farm.getFarmer().getVillage().getCity().
		 * getLocality().getState().getName()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * } if (!ObjectUtil.isEmpty(filter.getFpo()) &&
		 * !StringUtil.isEmpty(filter.getFpo())) { cell =
		 * filterRow9.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("cooperative")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow9.createCell(2); FarmCatalogue fc =
		 * farmerService.getCatlogueValueByCode(fpo); cell.setCellValue(new
		 * HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * } if (!ObjectUtil.isEmpty(filter.getIcsName()) &&
		 * !StringUtil.isEmpty(filter.getIcsName())) { cell =
		 * filterRow10.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("icsName")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow10.createCell(2); FarmCatalogue fc =
		 * farmerService.getCatlogueValueByCode(icsName); cell.setCellValue(new
		 * HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * } }
		 */

		super.filter = this.filter;
		Map data = readData("cultivation");

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		Long serialNumber = 0L;

		for (Object[] obj : mainGridRows) {

			Object[] datas = (Object[]) obj;

			Farm farm = farmerService.findFarmByfarmId(datas[0].toString());

			row = sheet.createRow(rowNum++);
			colNum = 0;

			serialNumber++;
			cell = row.createCell(colNum++);
			style4.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style4);
			cell.setCellValue(serialNumber);

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!ObjectUtil.isEmpty(preferences)) {
				if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
					setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
				}
			}

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

				if (StringUtil.isEmpty(branchIdValue)) {

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[2])))
									? getBranchesMap().get(getParentBranchMap().get(datas[2]))
									: getBranchesMap().get(datas[2])));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(datas[2])));
			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(branchesMap.get(datas[2])));
				}
			}
			HarvestSeason season = farmerService.findSeasonNameByCode(datas[5].toString());
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(season) ? season.getName() : getText("NA"))));
			if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
				if (getBranchId() == null || !getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)) {
				cell = row.createCell(colNum++);
				Farmer farmer = farmerService.findFarmerByFarmerId(datas[1].toString());
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(farmer) ? farmer.getFarmerCode() : getText("NA"))));
			}
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getFirstName())
							? farm.getFarmer().getFirstName() : getText("NA"))));

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString( (!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getLastName()) ?
			 * farm.getFarmer().getLastName() : getText("NA"))));
			 */

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer())
					&& (!StringUtil.isEmpty(farm.getFarmName())) ? farm.getFarmName() : getText("NA"))));
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getVillage())
							? farm.getFarmer().getVillage().getName() : getText("NA"))));

			/*
			 * if (!getCurrentTenantId().equals("pratibha")) {
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString( (!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity()) ?
			 * farm.getFarmer().getVillage().getCity().getName() :
			 * getText("NA"))));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity().
			 * getLocality()) ?
			 * farm.getFarmer().getVillage().getCity().getLocality().getName() :
			 * getText("NA"))));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity().
			 * getLocality().getState()) ?
			 * farm.getFarmer().getVillage().getCity().getLocality().getState().
			 * getName() : getText("NA"))));
			 * 
			 * }
			 */
			/*
		
			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmDetailedInfo().
			 * getProposedPlantingArea()) ?
			 * farm.getFarmDetailedInfo().getProposedPlantingArea() :
			 * getText("NA"))));
			 */
			
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)&& (!getCurrentTenantId().equalsIgnoreCase("gar")) && (!getCurrentTenantId().equalsIgnoreCase("agro")) && (!getCurrentTenantId().equalsIgnoreCase("iffco") &&  !getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID))){
			cell = row.createCell(colNum++);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[7]) ? datas[7].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[7].toString()));
			}
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[8].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[21]) ? datas[21].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[21].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[9].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[22]) ? datas[22].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[22].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[10].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[23]) ? datas[23].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[23].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[11]) ? datas[11].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[11].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[24]) ? datas[24].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[24].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[12]) ? datas[12].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[12].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[25]) ? datas[25].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[25].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[13]) ? datas[13].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[13].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[26]) ? datas[26].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[26].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[14].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[27]) ? datas[27].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[27].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[15]) ? datas[15].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[15].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[28]) ? datas[28].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[28].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[16]) ? datas[16].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[16].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[29]) ? datas[29].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[29].toString()));
			}
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[17]) ? datas[17].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[17].toString()));
			
			if (getBranchId() != null && getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {	
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[30]) ? datas[30].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[30].toString()));
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[31]) ? datas[31].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[31].toString()));
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[32]) ? datas[32].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[32].toString()));
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[33]) ? datas[33].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[33].toString()));
			}
				/*
				 * cell = row.createCell(colNum++); cell.setCellValue( new
				 * HSSFRichTextString((!ObjectUtil.isEmpty(datas[17]) ?
				 * datas[17].toString() : "0.00")));
				 */
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[18]) ? datas[18].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[18].toString()));
			
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[20]) ? datas[20].toString() : "0.00")));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.valueOf(datas[20].toString()));
			
			cell = row.createCell(colNum++);
			cellStyle3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(cellStyle3);
			/*cell.setCellValue(!ObjectUtil.isEmpty(datas[19].toString())&&StringUtil.isDouble(datas[19].toString())
					? String.valueOf(CurrencyUtil
							.getDecimalFormat(Double.valueOf(datas[19].toString()), "##.00"))
					: getText("NA"));*/
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(Double.valueOf(datas[19].toString()));
			
			
		}

		/*for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}*/

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("cropHarvestReport") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String update() throws Exception {

		if (this.id != null && !this.id.equals("")) {

			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

			if (StringUtil.isEmpty(seasonCode)) {
				seasonCode = clientService.findCurrentSeasonCode();
			}

			/*
			 * cultivationList =
			 * farmerService.findCOCByFarmerIdFarmIdCropCodeSeason(
			 * farmerService.findByCultivationId(this.id).get(0).getFarmerId(),
			 * farmerService.findByCultivationId(this.id).get(0).getFarmId(),
			 * farmerService.findByCultivationId(this.id).get(0).getCropCode(),
			 * seasonCode);
			 * 
			 * filter =
			 * farmerService.findCultivationByCultivationId(Long.valueOf(this.id
			 * ));
			 * 
			 * if (filter == null) { addActionError(NO_RECORD); return REDIRECT;
			 * } if(!StringUtil.isEmpty(filter.getCropCode())){
			 * ProcurementProduct crop =
			 * farmerService.findCropByCropCode(filter.getCropCode());
			 * filter.setCropCode(ObjectUtil.isEmpty(crop)?"":crop.getName());
			 * 
			 * } cultivationDetails = new ArrayList<CultivationDetail>();
			 */
			cultivationDetails = new ArrayList<CultivationDetail>();

			Cultivation coc = farmerService.findByCultivationId(this.id);
			if (!ObjectUtil.isEmpty(coc)) {
				/*
				 * cultivationList =
				 * farmerService.findCOCByFarmerIdFarmIdCropCodeSeason(coc.
				 * getFarmerId(), coc.getFarmId(), coc.getCropCode(),
				 * seasonCode);
				 */
				cultivationList = farmerService.findCOCByFarmerIdFarmIdSeason(coc.getFarmerId(), coc.getFarmId(),
						seasonCode);

				for (Cultivation cultivation : cultivationList) {

					List<CultivationDetail> cultivationDetail = farmerService
							.findCultivationDetailsByCultivationIdAndSession(cultivation.getId());
					if (!ObjectUtil.isListEmpty(cultivationDetail)) {
						cultivationDetails.addAll(cultivationDetail);
					}

					for (CultivationDetail cultiDetail : cultivationDetail) {
						if (cultiDetail.getType() == 1) {
							fertilizersList.add(cultiDetail);
						}
						if (cultiDetail.getType() == 2) {
							pesticidesList.add(cultiDetail);
						}
						if (cultiDetail.getType() == 3) {
							fymsList.add(cultiDetail);
						}

					}

				}
			}
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			setCurrentPage(getCurrentPage());
			manureCostupdate();
			fertiCostUpdate();
			pestiCostUpdate();
			landTotalUpdate();
			sowingUpdate();
			gapUpate();
			weedingUpdate();
			cultureUpdate();
			irrigationUpdate();
			// fertiTotalUpdate();
			harvestUpdate();
			otherExpenceUpdate();
			// pestiTotalUpdate();
			// manureTotalUpdate();
			labourCostUpdate();
			request.setAttribute(HEADING, getText(LIST));
			return REDIRECT;
		}

		return super.execute();
	}

	private void labourCostUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(labourCostDatas) && labourCostDatas.length() > 0) {
			String[] totLabourSpilt = labourCostDatas.split("@@@");
			for (int i = 0; i < totLabourSpilt.length; i++) {

				if (totLabourSpilt[i].length() > 0) {
					String[] totalLabour = totLabourSpilt[i].split("###");
					Cultivation labourCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalLabour[1]));
					labourCultivation.setLabourCost(totalLabour[0]);

					double totCoc = getDouble(labourCultivation.getLandTotal())
							+ getDouble(labourCultivation.getTotalSowing())
							+ getDouble(labourCultivation.getTotalCulture())
							+ getDouble(labourCultivation.getTotalExpense())
							+ getDouble(labourCultivation.getTotalGap())
							+ getDouble(labourCultivation.getTotalHarvest())
							+ getDouble(labourCultivation.getTotalFertilizer())
							+ getDouble(labourCultivation.getTotalIrrigation())
							+ getDouble(labourCultivation.getTotalManure())
							+ getDouble(labourCultivation.getTotalPesticide())
							+ getDouble(labourCultivation.getTotalWeed())
							+ getDouble(labourCultivation.getLabourCost());
					labourCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(labourCultivation);

				}
			}

		}
	}

	private void manureTotalUpdate() {

		if (!StringUtil.isEmpty(totalManureDatas) && totalManureDatas.length() > 0) {
			String[] totManureSpilt = totalManureDatas.split("@@@");
			for (int i = 0; i < totManureSpilt.length; i++) {

				if (totManureSpilt[i].length() > 0) {
					String[] totalManure = totManureSpilt[i].split("###");
					Cultivation totalManureCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalManure[1]));
					totalManureCultivation.setTotalManure(totalManure[0]);
					double totCoc = getDouble(totalManureCultivation.getLandTotal())
							+ getDouble(totalManureCultivation.getTotalSowing())
							+ getDouble(totalManureCultivation.getTotalCulture())
							+ getDouble(totalManureCultivation.getTotalExpense())
							+ getDouble(totalManureCultivation.getTotalGap())
							+ getDouble(totalManureCultivation.getTotalHarvest())
							+ getDouble(totalManureCultivation.getTotalFertilizer())
							+ getDouble(totalManureCultivation.getTotalIrrigation())
							+ getDouble(totalManureCultivation.getTotalManure())
							+ getDouble(totalManureCultivation.getTotalPesticide())
							+ getDouble(totalManureCultivation.getTotalWeed())
							+ getDouble(totalManureCultivation.getLabourCost());
					totalManureCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(totalManureCultivation);

				}
			}

		}

		// TODO Auto-generated method stub

	}

	private void pestiTotalUpdate() {
		if (!StringUtil.isEmpty(totPestiDatas) && totPestiDatas.length() > 0) {
			String[] totPestiSpilt = totPestiDatas.split("@@@");
			for (int i = 0; i < totPestiSpilt.length; i++) {

				if (totPestiSpilt[i].length() > 0) {
					String[] totalPesti = totPestiSpilt[i].split("###");
					Cultivation totalPestiCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalPesti[1]));

					totalPestiCultivation.setTotalPesticide(totalPesti[0]);
					double totCoc = getDouble(totalPestiCultivation.getLandTotal())
							+ getDouble(totalPestiCultivation.getTotalSowing())
							+ getDouble(totalPestiCultivation.getTotalCulture())
							+ getDouble(totalPestiCultivation.getTotalExpense())
							+ getDouble(totalPestiCultivation.getTotalGap())
							+ getDouble(totalPestiCultivation.getTotalHarvest())
							+ getDouble(totalPestiCultivation.getTotalFertilizer())
							+ getDouble(totalPestiCultivation.getTotalIrrigation())
							+ getDouble(totalPestiCultivation.getTotalManure())
							+ getDouble(totalPestiCultivation.getTotalPesticide())
							+ getDouble(totalPestiCultivation.getTotalWeed())
							+ getDouble(totalPestiCultivation.getLabourCost());
					totalPestiCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(totalPestiCultivation);

				}
			}

		}

	}

	private void otherExpenceUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(otherExpenceDatas) && otherExpenceDatas.length() > 0) {
			String[] totOtherExpSpilt = otherExpenceDatas.split("@@@");
			for (int i = 0; i < totOtherExpSpilt.length; i++) {

				if (totOtherExpSpilt[i].length() > 0) {
					String[] totalOtherExp = totOtherExpSpilt[i].split("###");
					Cultivation otherExpCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalOtherExp[4]));

					otherExpCultivation.setTotalExpense(totalOtherExp[3]);
					double totCoc = getDouble(otherExpCultivation.getLandTotal())
							+ getDouble(otherExpCultivation.getTotalSowing())
							+ getDouble(otherExpCultivation.getTotalCulture())
							+ getDouble(otherExpCultivation.getTotalExpense())
							+ getDouble(otherExpCultivation.getTotalGap())
							+ getDouble(otherExpCultivation.getTotalHarvest())
							+ getDouble(otherExpCultivation.getTotalFertilizer())
							+ getDouble(otherExpCultivation.getTotalIrrigation())
							+ getDouble(otherExpCultivation.getTotalManure())
							+ getDouble(otherExpCultivation.getTotalPesticide())
							+ getDouble(otherExpCultivation.getTotalWeed())
							+ getDouble(otherExpCultivation.getLabourCost());
					otherExpCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(otherExpCultivation);

				}
			}

		}

	}

	private void harvestUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(harvestDatas) && harvestDatas.length() > 0) {
			String[] totHarvestSpilt = harvestDatas.split("@@@");
			for (int i = 0; i < totHarvestSpilt.length; i++) {

				if (totHarvestSpilt[i].length() > 0) {
					String[] totalHarvest = totHarvestSpilt[i].split("###");
					Cultivation harvestCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalHarvest[1]));

					harvestCultivation.setTotalHarvest(totalHarvest[0]);
					double totCoc = getDouble(harvestCultivation.getLandTotal())
							+ getDouble(harvestCultivation.getTotalSowing())
							+ getDouble(harvestCultivation.getTotalCulture())
							+ getDouble(harvestCultivation.getTotalExpense())
							+ getDouble(harvestCultivation.getTotalGap())
							+ getDouble(harvestCultivation.getTotalHarvest())
							+ getDouble(harvestCultivation.getTotalFertilizer())
							+ getDouble(harvestCultivation.getTotalIrrigation())
							+ getDouble(harvestCultivation.getTotalManure())
							+ getDouble(harvestCultivation.getTotalPesticide())
							+ getDouble(harvestCultivation.getTotalWeed())
							+ getDouble(harvestCultivation.getLabourCost());
					harvestCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(harvestCultivation);

				}
			}

		}

	}

	private void fertiTotalUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(festiTotalDatas) && festiTotalDatas.length() > 0) {
			String[] totFertiSpilt = festiTotalDatas.split("@@@");
			for (int i = 0; i < totFertiSpilt.length; i++) {

				if (totFertiSpilt[i].length() > 0) {
					String[] totalFerti = totFertiSpilt[i].split("###");
					Cultivation totalFertiCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(totalFerti[1]));

					totalFertiCultivation.setTotalFertilizer(totalFerti[0]);

					double totCoc = getDouble(totalFertiCultivation.getLandTotal())
							+ getDouble(totalFertiCultivation.getTotalSowing())
							+ getDouble(totalFertiCultivation.getTotalCulture())
							+ getDouble(totalFertiCultivation.getTotalExpense())
							+ getDouble(totalFertiCultivation.getTotalGap())
							+ getDouble(totalFertiCultivation.getTotalHarvest())
							+ getDouble(totalFertiCultivation.getTotalFertilizer())
							+ getDouble(totalFertiCultivation.getTotalIrrigation())
							+ getDouble(totalFertiCultivation.getTotalManure())
							+ getDouble(totalFertiCultivation.getTotalPesticide())
							+ getDouble(totalFertiCultivation.getTotalWeed())
							+ getDouble(totalFertiCultivation.getLabourCost());
					totalFertiCultivation.setTotalCoc(String.valueOf(totCoc));

					farmerService.update(totalFertiCultivation);

				}
			}

		}

	}

	private void irrigationUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(irrigationDatas) && irrigationDatas.length() > 0) {
			String[] irrigationSpilt = irrigationDatas.split("@@@");
			for (int i = 0; i < irrigationSpilt.length; i++) {

				if (irrigationSpilt[i].length() > 0) {
					String[] irrigation = irrigationSpilt[i].split("###");
					Cultivation irrigationCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(irrigation[1]));

					irrigationCultivation.setTotalIrrigation(irrigation[0]);
					double totCoc = getDouble(irrigationCultivation.getLandTotal())
							+ getDouble(irrigationCultivation.getTotalSowing())
							+ getDouble(irrigationCultivation.getTotalCulture())
							+ getDouble(irrigationCultivation.getTotalExpense())
							+ getDouble(irrigationCultivation.getTotalGap())
							+ getDouble(irrigationCultivation.getTotalHarvest())
							+ getDouble(irrigationCultivation.getTotalFertilizer())
							+ getDouble(irrigationCultivation.getTotalIrrigation())
							+ getDouble(irrigationCultivation.getTotalManure())
							+ getDouble(irrigationCultivation.getTotalPesticide())
							+ getDouble(irrigationCultivation.getTotalWeed())
							+ getDouble(irrigationCultivation.getLabourCost());
					irrigationCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(irrigationCultivation);

				}
			}

		}

	}

	private void cultureUpdate() {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(cultureDatas) && cultureDatas.length() > 0) {
			String[] cultureSpilt = cultureDatas.split("@@@");
			for (int i = 0; i < cultureSpilt.length; i++) {

				if (cultureSpilt[i].length() > 0) {
					String[] culture = cultureSpilt[i].split("###");
					Cultivation cultureCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(culture[1]));
					cultureCultivation.setTotalCulture(culture[0]);
					double totCoc = getDouble(cultureCultivation.getLandTotal())
							+ getDouble(cultureCultivation.getTotalSowing())
							+ getDouble(cultureCultivation.getTotalCulture())
							+ getDouble(cultureCultivation.getTotalExpense())
							+ getDouble(cultureCultivation.getTotalGap())
							+ getDouble(cultureCultivation.getTotalHarvest())
							+ getDouble(cultureCultivation.getTotalFertilizer())
							+ getDouble(cultureCultivation.getTotalIrrigation())
							+ getDouble(cultureCultivation.getTotalManure())
							+ getDouble(cultureCultivation.getTotalPesticide())
							+ getDouble(cultureCultivation.getTotalWeed())
							+ getDouble(cultureCultivation.getLabourCost());
					cultureCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(cultureCultivation);

				}
			}

		}
	}

	private void weedingUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(weedingDatas) && weedingDatas.length() > 0) {
			String[] weedingSpilt = weedingDatas.split("@@@");
			for (int i = 0; i < weedingSpilt.length; i++) {

				if (weedingSpilt[i].length() > 0) {
					String[] weeding = weedingSpilt[i].split("###");
					Cultivation weedingCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(weeding[1]));

					weedingCultivation.setTotalWeed(weeding[0]);
					double totCoc = getDouble(weedingCultivation.getLandTotal())
							+ getDouble(weedingCultivation.getTotalSowing())
							+ getDouble(weedingCultivation.getTotalCulture())
							+ getDouble(weedingCultivation.getTotalExpense())
							+ getDouble(weedingCultivation.getTotalGap())
							+ getDouble(weedingCultivation.getTotalHarvest())
							+ getDouble(weedingCultivation.getTotalFertilizer())
							+ getDouble(weedingCultivation.getTotalIrrigation())
							+ getDouble(weedingCultivation.getTotalManure())
							+ getDouble(weedingCultivation.getTotalPesticide())
							+ getDouble(weedingCultivation.getTotalWeed())
							+ getDouble(weedingCultivation.getLabourCost());
					weedingCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(weedingCultivation);

				}
			}

		}

	}

	private void gapUpate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(gapDatas) && gapDatas.length() > 0) {
			String[] gapSpilt = gapDatas.split("@@@");
			for (int i = 0; i < gapSpilt.length; i++) {

				if (gapSpilt[i].length() > 0) {
					String[] gap = gapSpilt[i].split("###");
					Cultivation gapCultivation = farmerService.findCultivationByCultivationId(Long.valueOf(gap[1]));
					gapCultivation.setTotalGap(gap[0]);
					double totCoc = getDouble(gapCultivation.getLandTotal())
							+ getDouble(gapCultivation.getTotalSowing()) + getDouble(gapCultivation.getTotalCulture())
							+ getDouble(gapCultivation.getTotalExpense()) + getDouble(gapCultivation.getTotalGap())
							+ getDouble(gapCultivation.getTotalHarvest())
							+ getDouble(gapCultivation.getTotalFertilizer())
							+ getDouble(gapCultivation.getTotalIrrigation())
							+ getDouble(gapCultivation.getTotalManure()) + getDouble(gapCultivation.getTotalPesticide())
							+ getDouble(gapCultivation.getTotalWeed()) + getDouble(gapCultivation.getLabourCost());
					gapCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(gapCultivation);

				}
			}

		}

	}

	private void sowingUpdate() {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(sowingDatas) && sowingDatas.length() > 0) {
			String[] sowingSpilt = sowingDatas.split("@@@");
			for (int i = 0; i < sowingSpilt.length; i++) {

				if (sowingSpilt[i].length() > 0) {
					String[] sowing = sowingSpilt[i].split("###");
					Cultivation sowingCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(sowing[3]));

					sowingCultivation.setTotalSowing(sowing[2]);
					double totCoc = getDouble(sowingCultivation.getLandTotal())
							+ getDouble(sowingCultivation.getTotalSowing())
							+ getDouble(sowingCultivation.getTotalExpense())
							+ getDouble(sowingCultivation.getTotalGap())
							+ getDouble(sowingCultivation.getTotalCulture())
							+ +getDouble(sowingCultivation.getTotalHarvest())
							+ getDouble(sowingCultivation.getTotalFertilizer())
							+ getDouble(sowingCultivation.getTotalIrrigation())
							+ getDouble(sowingCultivation.getTotalManure())
							+ getDouble(sowingCultivation.getTotalPesticide())
							+ getDouble(sowingCultivation.getTotalWeed())
							+ getDouble(sowingCultivation.getLabourCost());
					sowingCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(sowingCultivation);

				}
			}

		}
	}

	private void landTotalUpdate() {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(landTotalDatas) && landTotalDatas.length() > 0) {
			String[] landTotalSpilt = landTotalDatas.split("@@@");
			for (int i = 0; i < landTotalSpilt.length; i++) {

				if (landTotalSpilt[i].length() > 0) {
					String[] landTotal = landTotalSpilt[i].split("###");
					Cultivation landTotalCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(landTotal[3]));
					landTotalCultivation.setLandTotal(landTotal[2]);
					landTotalCultivation.setSoilPreparation(landTotal[4]);
					double totCoc = getDouble(landTotalCultivation.getLandTotal())
							+ getDouble(landTotalCultivation.getTotalSowing())
							+ getDouble(landTotalCultivation.getTotalCulture())
							+ getDouble(landTotalCultivation.getTotalExpense())
							+ getDouble(landTotalCultivation.getTotalGap())
							+ getDouble(landTotalCultivation.getTotalHarvest())
							+ getDouble(landTotalCultivation.getTotalFertilizer())
							+ getDouble(landTotalCultivation.getTotalIrrigation())
							+ getDouble(landTotalCultivation.getTotalManure())
							+ getDouble(landTotalCultivation.getTotalPesticide())
							+ getDouble(landTotalCultivation.getTotalWeed())
							+ getDouble(landTotalCultivation.getLabourCost());
					landTotalCultivation.setTotalCoc(String.valueOf(totCoc));
					farmerService.update(landTotalCultivation);

				}
			}

		}

	}

	public void manureCostupdate() {
		double totalManureCost = 0.0;
		if (!StringUtil.isEmpty(manureDatas) && manureDatas.length() > 0) {
			String[] spiltManure = manureDatas.split("@@@");
			for (int j = 0; j < spiltManure.length; j++) {
				if (spiltManure[j].length() > 0) {
					String[] cocManureDatas = spiltManure[j].split("###");
					Cultivation manureCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(cocManureDatas[5]));

					CultivationDetail manureCocDetails = new CultivationDetail();
					manureCocDetails.setFertilizerType(String.valueOf(cocManureDatas[0]));
					manureCocDetails
							.setQty(Double.valueOf(StringUtil.isEmpty(cocManureDatas[1]) ? "0" : cocManureDatas[1]));
					manureCocDetails
							.setCost(Double.valueOf(StringUtil.isEmpty(cocManureDatas[2]) ? "0" : cocManureDatas[2]));
					manureCocDetails.setType(CultivationDetail.MANURE_TYPE);
					manureCocDetails.setUsageLevel(cocManureDatas[3]);
					manureCocDetails.setCultivation(manureCultivation);
					manureCocDetails.setId(Long.valueOf(cocManureDatas[4]));
					manureCocDetails.setUom(cocManureDatas[6]);

					totalManureCost = Double.valueOf(StringUtil.isEmpty(cocManureDatas[2]) ? "0" : cocManureDatas[2]);
					locationService.updateCultivationDetailCost(manureCocDetails, "3");
					locationService.updateCultivationCost(manureCultivation.getId(), String.valueOf(totalManureCost),
							"3");
				}

			}
		}

	}

	public void fertiCostUpdate() {
		double totalFertiCost = 0.0;
		if (!StringUtil.isEmpty(fertilizerDatas) && fertilizerDatas.length() > 0) {
			String[] spiltFertilizer = fertilizerDatas.split("@@@");

			for (int j = 0; j < spiltFertilizer.length; j++) {

				if (spiltFertilizer[j].length() > 0) {

					String[] cocFertiDatas = spiltFertilizer[j].split("###");
					Cultivation fertiCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(cocFertiDatas[5]));
					CultivationDetail fertiCocDetails = new CultivationDetail();
					fertiCocDetails.setFertilizerType(String.valueOf(cocFertiDatas[0]));
					fertiCocDetails
							.setQty(Double.valueOf(StringUtil.isEmpty(cocFertiDatas[1]) ? "0" : cocFertiDatas[1]));
					fertiCocDetails
							.setCost(Double.valueOf(StringUtil.isEmpty(cocFertiDatas[2]) ? "0" : cocFertiDatas[2]));
					fertiCocDetails.setType(CultivationDetail.FERTILIZER_TYPE);
					fertiCocDetails.setUsageLevel(cocFertiDatas[3]);
					fertiCocDetails.setId(Long.valueOf(cocFertiDatas[4]));
					fertiCocDetails.setCultivation(fertiCultivation);
					fertiCocDetails.setUom(cocFertiDatas[6]);

					totalFertiCost = Double.valueOf(StringUtil.isEmpty(cocFertiDatas[2]) ? "0" : cocFertiDatas[2]);
					locationService.updateCultivationDetailCost(fertiCocDetails, "1");
					locationService.updateCultivationCost(fertiCultivation.getId(), String.valueOf(totalFertiCost),
							"1");

				}

			}

		}

	}

	public void pestiCostUpdate() {
		double pestiTotalCost = 0.0;
		if (!StringUtil.isEmpty(pesticideDatas) && pesticideDatas.length() > 0) {
			String[] spiltPesticide = pesticideDatas.split("@@@");
			Set<CultivationDetail> cultivationDetailSet = new HashSet<CultivationDetail>();
			for (int i = 0; i < spiltPesticide.length; i++) {
				if (spiltPesticide[i].length() > 0) {

					String[] cocPestiDatas = spiltPesticide[i].split("###");
					Cultivation pestiCultivation = farmerService
							.findCultivationByCultivationId(Long.valueOf(cocPestiDatas[5]));
					CultivationDetail pestiCocDetails = new CultivationDetail();
					pestiCocDetails.setFertilizerType(String.valueOf(cocPestiDatas[0]));
					pestiCocDetails
							.setQty(Double.valueOf(StringUtil.isEmpty(cocPestiDatas[1]) ? "0" : cocPestiDatas[1]));
					pestiCocDetails
							.setCost(Double.valueOf(StringUtil.isEmpty(cocPestiDatas[2]) ? "0" : cocPestiDatas[2]));
					pestiCocDetails.setType(CultivationDetail.PESTICIDE_TYPE);
					pestiCocDetails.setUsageLevel(cocPestiDatas[3]);
					pestiCocDetails.setId(Long.valueOf(cocPestiDatas[4]));
					pestiCocDetails.setCultivation(pestiCultivation);
					pestiCocDetails.setUom(cocPestiDatas[6]);
					// cultivationDetailSet.add(pestiCocDetails);
					pestiTotalCost = Double.valueOf(StringUtil.isEmpty(cocPestiDatas[2]) ? "0" : cocPestiDatas[2]);
					locationService.updateCultivationDetailCost(pestiCocDetails, "2");
					locationService.updateCultivationCost(pestiCultivation.getId(), String.valueOf(pestiTotalCost),
							"2");
				}

			}
		}

	}

	public String populateData() throws Exception {

		setFilter(new Cultivation());
		Map<String, String> searchRecord = getJQGridRequestParam();
		if (!StringUtil.isEmpty(searchRecord.get("cSeasonCode"))) {

			filter.setCurrentSeasonCode(searchRecord.get("cSeasonCode"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("cropProductName"))) {

			filter.setCropName(searchRecord.get("cropProductName"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("farmerName"))) {
			filter.setFarmerName(searchRecord.get("farmerName"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmName"))) {
			filter.setFarmName(searchRecord.get("farmName"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			filter.setVillageName(searchRecord.get("village"));
		}

		if (!StringUtil.isEmpty(searchRecord.get(fatherName))) {
			filter.setFatherName(searchRecord.get(fatherName));
		}

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId"));
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId"));
				branchList.add(searchRecord.get("branchId"));
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchId")) && !searchRecord.get("subBranchId").equals("0")) {
			filter.setBranchId(searchRecord.get("subBranchId"));
		}

		filter.setTxnType("2");// 1 - Income 2 - Expense
		super.filter = this.filter;
		Map data = readData("cultivation");
		return sendJSONResponse(data);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Cultivation getFilter() {

		return filter;
	}

	public void setFilter(Cultivation filter) {

		this.filter = filter;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public String getFarmerName() {

		return farmerName;
	}

	public String getFarmerCode() {

		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {

		this.farmerCode = farmerCode;
	}

	public void setVillage(String village) {

		this.village = village;
	}

	public String getVillage() {

		return village;
	}

	public List<Cultivation> getCultivationList() {

		return cultivationList;
	}

	public void setCultivationList(List<Cultivation> cultivationList) {

		this.cultivationList = cultivationList;
	}

	public List<CultivationDetail> getCultivationDetails() {

		return cultivationDetails;
	}

	public void setCultivationDetails(List<CultivationDetail> cultivationDetails) {

		this.cultivationDetails = cultivationDetails;
	}

	public String getXlsFileName() {

		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public IProductService getProductService() {

		return productService;
	}

	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	@Override
	public String populatePDF() throws Exception {
		List<String> filters = new ArrayList<String>();
		List<Object> fields = new ArrayList<Object>();
		List<List<Object>> entityObject = new ArrayList<List<Object>>();
		DecimalFormat format = new DecimalFormat("0.00");
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!ObjectUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}
		Document document = new Document();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("procurementStockReportList") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);
		document.open();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		; // resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		setMailExport(true);

		setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));

		filters.add(getLocaleProperty("filter"));

		if (!StringUtil.isEmpty(farmerId)) {
			filters.add((getLocaleProperty("farmerId") + " : " + farmerId));
		}

		if (!StringUtil.isEmpty(farmerName)) {
			filters.add((getLocaleProperty("farmerName") + " : " + farmerName));
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filters.add((getLocaleProperty("fatherName") + " : " + fatherName));
		}
		if (!StringUtil.isEmpty(village)) {
			filters.add(getLocaleProperty("village") + " : " + (!ObjectUtil.isEmpty(village)
					? locationService.findVillageByCode(village).getName() : getText("NA")));
		}

		if (!StringUtil.isEmpty(cropCode)) {
			ProcurementProduct procurementProduct = productDistributionService
					.findProcurementProductByCode(!ObjectUtil.isEmpty(cropCode) ? cropCode.toString() : "");
			filters.add((getLocaleProperty("crop") + " : " + procurementProduct.getName()));
		}

		/*
		 * if (!StringUtil.isEmpty(branchIdParma)) {
		 * filters.add(getLocaleProperty("branchIdParma") + " : " +
		 * branchIdParma); }
		 */

		if (!StringUtil.isEmpty(branchIdParma)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				filter.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
			}

			filters.add((getLocaleProperty("mainOrganization") + " : " + getBranchesMap().get(getBranchIdParma())));
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			filter.setBranchId(subBranchIdParma);

			filters.add((getLocaleProperty("subOrganization") + " : " + getBranchesMap().get(getSubBranchIdParma())));
		}

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { cell =
		 * filterRow6.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("mainOrganization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow6.createCell(2); //cell.setCellValue(new
		 * HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
		 * cell.setCellValue(new
		 * HSSFRichTextString(getBranchesMap().get(getParentBranchMap().get(
		 * filter.getBranchId())))); filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("subOrganization")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow7.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * }
		 * 
		 * }
		 */

		if (!StringUtil.isEmpty(seasonCode)) {
			HarvestSeason seasons = farmerService.findSeasonNameByCode(seasonCode.toString());

			filters.add(getLocaleProperty("cSeasonCode") + " : " + seasons.getName());
		}

		// Beginning of setting filter values.
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new Cultivation();

		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filter.setFatherName(fatherName);
		}

		if (!StringUtil.isEmpty(cropCode)) {
			filter.setCropCode(cropCode);
		}

		if (!StringUtil.isEmpty(village)) {
			Village vill = locationService.findVillageByCode(village);
			filter.setVillageId(String.valueOf(vill.getId()));
		}

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setCurrentSeasonCode(seasonCode);
		} /*
			 * else {
			 * filter.setCurrentSeasonCode(clientService.findCurrentSeasonCode()
			 * ); }
			 */

		filter.setTxnType("2");

		super.filter = this.filter;
		// End of setting filter values.
		Map data = readData("cultivation");
		// Map data = isMailExport() ? readData() : readExportData();
		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);
		// List<Cultivation> mainGridRows = (List<Cultivation>) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		// for (Cultivation cultivation : mainGridRows) {
		// fields = new ArrayList<Object>();
		for (Object[] obj : mainGridRows) {

			Object[] datas = (Object[]) obj;
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					fields.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[3])))
							? getBranchesMap().get(getParentBranchMap().get(datas[3]))
							: getBranchesMap().get(datas[3]));
				}
				fields.add(getBranchesMap().get(datas[3]));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					fields.add(!StringUtil.isEmpty(branchesMap.get(datas[3])) ? (branchesMap.get(datas[3])) : "");
				}
			}

			/*
			 * if (StringUtil.isEmpty(branchIdValue)) { // Check if non-main
			 * branch // has logged in as for // farmer.
			 * 
			 * fields.add(!StringUtil.isEmpty(branchesMap.get(cultivation.
			 * getBranchId())) ? (branchesMap.get(cultivation.getBranchId())) :
			 * ""); }
			 */
			HarvestSeason season = farmerService.findSeasonNameByCode(datas[6].toString());
			fields.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
				Farmer farmer = farmerService.findFarmerByFarmerId(datas[1].toString());
				fields.add((!ObjectUtil.isEmpty(farmer) ? farmer.getFarmerCode() : getText("NA")));
			}
			Farm farm = farmerService.findFarmByfarmId(datas[0].toString());
			fields.add((!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getFirstName())
					? farm.getFarmer().getFirstName() : getText("NA")));

			/*
			 * fields.add((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getLastName()) ?
			 * farm.getFarmer().getLastName() : getText("NA")));
			 */
			fields.add((!ObjectUtil.isEmpty(farm) && (!StringUtil.isEmpty(farm.getFarmName())) ? farm.getFarmName()
					: getText("NA")));
			ProcurementProduct procurementProduct = productDistributionService
					.findProcurementProductByCode(!ObjectUtil.isEmpty(datas[2]) ? datas[2].toString() : "");
			if (!getCurrentTenantId().equals("indev")) {
				fields.add((!ObjectUtil.isEmpty(procurementProduct) ? procurementProduct.getName() : getText("NA")));
			}
			fields.add((!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getVillage())
					? farm.getFarmer().getVillage().getName() : getText("NA")));
			/*
			 * if (!getCurrentTenantId().equals("pratibha")) {
			 * fields.add((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity()) ?
			 * farm.getFarmer().getVillage().getCity().getName() :
			 * getText("NA"))); fields.add((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity().
			 * getLocality()) ?
			 * farm.getFarmer().getVillage().getCity().getLocality().getName() :
			 * getText("NA"))); fields.add((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmer().getVillage().getCity().
			 * getLocality().getState()) ?
			 * farm.getFarmer().getVillage().getCity().getLocality().getState().
			 * getName() : getText("NA"))); }
			 */

			Object[] farmCrop = getFarmCropMap().containsKey(datas[0].toString() + "-" + datas[2].toString())
					? getFarmCropMap().get(datas[0].toString() + "-" + datas[2].toString()) : null;

			fields.add((!ObjectUtil.isEmpty(farmCrop) ? farmCrop[3].toString() : " "));

			/*
			 * fields.add((!ObjectUtil.isEmpty(farm) &&
			 * !ObjectUtil.isEmpty(farm.getFarmDetailedInfo().
			 * getProposedPlantingArea()) ?
			 * farm.getFarmDetailedInfo().getProposedPlantingArea() :
			 * getText("NA")));
			 */
			fields.add((!ObjectUtil.isEmpty(datas[7]) ? datas[7].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[8]) ? datas[8].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[10]) ? datas[10].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[11]) ? datas[11].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[12]) ? datas[12].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[13]) ? datas[13].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[14]) ? datas[14].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[15]) ? datas[15].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[16]) ? datas[16].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[17]) ? datas[17].toString() : getText("NA")));
			fields.add((!ObjectUtil.isEmpty(datas[18]) ? datas[18].toString() : getText("NA")));

		}

		entityObject.add(fields);

		InputStream is = getPDFExportStreamData(entityObject, filters);
		setPdfFileName(getText("CultiListFile") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("CultiListFile"), fileMap, ".pdf"));
		return "pdf";
	}

	public InputStream getPDFExportStreamData(List<List<Object>> obj, List<String> filters)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("CultiListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		document.open();

		PdfPTable table = null;

		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		// resizing logo image size.

		document.add(logo); // Adding logo in PDF file.

		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getText("CultiExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
																														// title.
		int size = filters.size();																							// title.
		if(size>1 ){
		for (String filter : filters) {
			document.add(new Paragraph(
					new Phrase(filter, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		}
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getText("ExportColumnHeading"); } else { columnHeaders =
		 * getText("ExportColumnHeading1"); } } else if
		 * ((getIsMultiBranch().equalsIgnoreCase("1")) &&
		 * (getIsParentBranch().equals("0"))) { columnHeaders =
		 * getText("ExportColumnHeadingNo"); } else { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("exportColumnHeaderBranch"); } else { columnHeaders
		 * = getLocaleProperty("exportColumnHeaderCultivation"); } } 
		 * 
		 * if (getCurrentTenantId().equals("chetna")) { columnHeaders =
		 * getText("ExportHeadingNoBranchChetna"); } if
		 * (getCurrentTenantId().equals("pratibha")) { if
		 * (StringUtil.isEmpty(getBranchId())) { columnHeaders =
		 * getText("ExportHeadingPra"); } if
		 * (!StringUtil.isEmpty(getBranchId())) { columnHeaders =
		 * getText("ExportHeadingPratibhaBranch"); } } if
		 * (getCurrentTenantId().equals("crsdemo")) { columnHeaders =
		 * getLocaleProperty("exportColumnHeadercultivationcrs"); ; }
		 */

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportCultivationColumnHeadingBranch"); } else if
		 * (!StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportCultivationColumnHeading"); } } else { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("exportCultivationColumnHeaderBranch"); } else {
		 * columnHeaders = getLocaleProperty("exportCultivationColumnHeader"); }
		 * }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportCultivationColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportCultivationColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportCultivationColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders = getLocaleProperty("OrganicCultivationExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders = getLocaleProperty("BCICultivationExportHeader");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportCultivationColumnHeaderBranch");
			} else {
				columnHeaders = getLocaleProperty("exportCultivationColumnHeader");
			}
		}

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			PdfPCell cell = null; // cell for table.
			cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
																							// font
																							// for
																							// header
																							// cells
																							// of
																							// table.
			table = new PdfPTable(columnHeaders.split("\\,").length); // Code
																		// for
																		// setting
																		// table
																		// column
																		// Numbers.

			table.setWidthPercentage(100); // Set Table Width.
			table.getDefaultCell().setUseAscender(true);
			table.getDefaultCell().setUseDescender(true);
			for (String cellHeader : columnHeaders.split("\\,")) {

				if (cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());
				} else if (cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT)) {
					cellHeader = cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());
				}

				cell = new PdfPCell(new Phrase(cellHeader, cellFont));
				cell.setBackgroundColor(new BaseColor(144,238,144));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(3); // To add column span.
				table.addCell(cell);
			}

			cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																							// font
																							// for
																							// cells.
			for (List<Object> entityObj : obj) { // iterate over all list of
													// objects.
				for (Object entityField : entityObj) {
					// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
					cell = new PdfPCell(new Phrase(
							StringUtil.isEmpty(entityField.toString()) ? getText("NA") : entityField.toString(),
							cellFont));

					table.addCell(cell);
					// END OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
				}
			}

			/*
			 * for (List<Object> entityObj : obj) { // iterate over all list of
			 * // objects. for (Object entityField : entityObj) { // BEGIN OF
			 * CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO // PDF FILE. cell
			 * = new PdfPCell(new Phrase(
			 * StringUtil.isEmpty(entityField.toString()) ? getText("NA") :
			 * getText("ra"), cellFont));
			 * 
			 * table.addCell(cell); // END OF CODE FOR A PARTICULAR CELL IN A
			 * ROW OF TABLE TO // PDF FILE. } }
			 */

			document.add(table); // Add table to document.
		}
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB + type);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB + type))) {
				content = super.getText(BreadCrumb.BREADCRUMB + type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));

		} else {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content)
					|| (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public List<String> getFarmerIdList() {
		List<Farmer> farmerCodList = farmerService.listFarmer();
		List<String> farmerIdList = new ArrayList<String>();
		for (Farmer farmer : farmerCodList) {
			farmerIdList.add(farmer.getFarmerId());
		}
		// To sort the list of farmer id value in drop down.
		java.util.Collections.sort(farmerIdList);
		return farmerIdList;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * Gets the Father Name list.
	 * 
	 * @return the fatherName list
	 */

	/*
	 * public Map<String, String> getFatherNameList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByCultivation(); List<String> testList = new
	 * ArrayList<>(); if (!ObjectUtil.isEmpty(farmerList)) { testList =
	 * farmerList.stream().filter(obj -> !StringUtil.isEmpty(obj[1].toString()))
	 * .map(obj ->
	 * String.valueOf(obj[1])).distinct().collect(Collectors.toList());
	 * farmerMap = testList.stream().collect(Collectors.toMap(String::toString,
	 * String::toString));
	 * 
	 * } return farmerMap; }
	 */

	/**
	 * 
	 * Gets Season List
	 * 
	 * @return
	 */
	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
			// seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
		}
		return seasonMap;

	}

	public String getSeasonFilter() {

		StringBuffer season = new StringBuffer();
		season.append(":").append(FILTER_ALL).append(";");
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			season.append(obj[0].toString()).append(":").append(obj[1].toString()).append(";");

		}
		String data = season.toString();
		return data.substring(0, data.length() - 1);

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

	public List<CultivationDetail> getFertilizersList() {
		return fertilizersList;
	}

	public void setFertilizersList(List<CultivationDetail> fertilizersList) {
		this.fertilizersList = fertilizersList;
	}

	public List<CultivationDetail> getPesticidesList() {
		return pesticidesList;
	}

	public void setPesticidesList(List<CultivationDetail> pesticidesList) {
		this.pesticidesList = pesticidesList;
	}

	public List<CultivationDetail> getFymsList() {
		return fymsList;
	}

	public void setFymsList(List<CultivationDetail> fymsList) {
		this.fymsList = fymsList;
	}

	public String getSowDate() {
		return sowDate;
	}

	public void setSowDate(String sowDate) {
		this.sowDate = sowDate;
	}

	public String getFarmerCodeEnabled() {
		return farmerCodeEnabled;
	}

	public void setFarmerCodeEnabled(String farmerCodeEnabled) {
		this.farmerCodeEnabled = farmerCodeEnabled;
	}

	public Map<String, String> getFields() {
		fields.put("1", getText("expenseDate"));
		fields.put("2", getText("cSeasonCode"));
		fields.put("3", getText("farmerName"));
		fields.put("4", getText("fatherName"));
		fields.put("5", getText("village"));

		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			fields.put("7", getText("farmerCode"));
		}

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("8", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("8", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("6", getText("app.branch"));
		}
		fields.put("9", getText("cultivationcropName"));

		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getCropCode() {
		return cropCode;
	}

	public void setCropCode(String cropCode) {
		this.cropCode = cropCode;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getFertilizerDatas() {
		return fertilizerDatas;
	}

	public void setFertilizerDatas(String fertilizerDatas) {
		this.fertilizerDatas = fertilizerDatas;
	}

	public String getPesticideDatas() {
		return pesticideDatas;
	}

	public void setPesticideDatas(String pesticideDatas) {
		this.pesticideDatas = pesticideDatas;
	}

	public String getFertiCultivationIds() {
		return fertiCultivationIds;
	}

	public void setFertiCultivationIds(String fertiCultivationIds) {
		this.fertiCultivationIds = fertiCultivationIds;
	}

	public String getPestiCultivationIds() {
		return pestiCultivationIds;
	}

	public void setPestiCultivationIds(String pestiCultivationIds) {
		this.pestiCultivationIds = pestiCultivationIds;
	}

	public String getManureCultivationIds() {
		return manureCultivationIds;
	}

	public void setManureCultivationIds(String manureCultivationIds) {
		this.manureCultivationIds = manureCultivationIds;
	}

	public String getTotalCoc() {
		return totalCoc;
	}

	public void setTotalCoc(String totalCoc) {
		this.totalCoc = totalCoc;
	}

	public Double getDouble(String obj) {

		return Double.valueOf(!StringUtil.isEmpty(obj) ? obj : "0.0");
	}

	String catalougeValues = "";

	public String getCatalouge(String code) {

		if (!StringUtil.isEmpty(code)) {
			// String[] codes = code.split(",");
			FarmCatalogue catalogue = getCatlogueValueByCode(code);
			if (!ObjectUtil.isEmpty(catalogue)) {
				catalougeValues = catalogue.getName();
			}
		}

		return !StringUtil.isEmpty(catalougeValues) ? StringUtil.removeLastComma(catalougeValues) : "";
	}

	public String getLabourCostDatas() {
		return labourCostDatas;
	}

	public void setLabourCostDatas(String labourCostDatas) {
		this.labourCostDatas = labourCostDatas;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public Map<String, String> getStateList() {

		Map<String, String> stateMap = new LinkedHashMap<String, String>();

		List<State> stateList = locationService.listOfStates();
		for (State obj : stateList) {
			stateMap.put(obj.getCode(), obj.getName());

		}
		return stateMap;

	}

	public Map<String, String> getWarehouseList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();

		warehouseMap = getFarmCatalougeMap(Integer.valueOf(getLocaleProperty("cooperativeType")));

		warehouseMap.put("99", "Others");

		return warehouseMap;

	}

	public Map<String, String> getIcsNameList() {

		Map<String, String> icsMap = new LinkedHashMap<>();

		icsMap = getFarmCatalougeMap(Integer.valueOf(getText("icsNameType")));

		icsMap.put("99", "Others");

		return icsMap;

	}

	public Map<String, String> getSeasonMap() {
		if (seasonMap.size() <= 0) {
			seasonMap = farmerService.listHarvestSeasons().stream()
					.collect(Collectors.toMap(HarvestSeason::getCode, HarvestSeason::getName));
		}
		return seasonMap;
	}

	public Map<String, Object[]> getFarmMap() {
		if (farmMap.size() <= 0) {
			farmerService.listFarmerFarmInfo().stream().forEach(farm -> {
				if (!StringUtil.isEmpty(farm[1]) && !StringUtil.isEmpty(farm[5])) {
					if (!farmMap.containsKey(String.valueOf(farm[6]))) {
						farmMap.put(String.valueOf(farm[6]), farm);
					}
				}
			});

		}
		return farmMap;
	}

	public Map<String, Object[]> getFarmCropMap() {
		// 0=FarmCrops id, 1=Farm Id, 2=Farm Code, 3=culti Area, 4= procurement
		// variety code,5=Procurement Product Code,6=Procurement Product Name
		if (farmCropMap.size() <= 0) {
			farmCropsService.listFarmCropsFarmInfo().stream().forEach(obj -> {
				if (!StringUtil.isEmpty(obj[2]) && !StringUtil.isEmpty(obj[5])) {
					if (!farmCropMap.containsKey(String.valueOf(obj[2]) + "-" + String.valueOf(obj[5]))) {
						farmCropMap.put(String.valueOf(obj[2]) + "-" + String.valueOf(obj[5]), obj);
					}
				}
			});
		}
		return farmCropMap;
	}

	public IDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}

	/*
	 * public Map<String, String> getFarmerFarmerIdList() { Map<String, String>
	 * farmerMap = new LinkedHashMap<>(); List<Object[]> farmerList =
	 * farmerService.listFarmerInfoByCultivation(); if
	 * (!ObjectUtil.isEmpty(farmerList)) { farmerList.stream().forEach(obj -> {
	 * if (!farmerMap.containsKey(obj[3])) { farmerMap.put(obj[3].toString(),
	 * obj[3].toString()); } }); } return farmerMap; }
	 */

	public Map<String, String> getVillageMap() {
		return villageMap;
	}

	public void setVillageMap(Map<String, String> villageMap) {
		this.villageMap = villageMap;
	}

	public Map<String, String> getFarmerCodList() {
		return farmerCodList;
	}

	public void setFarmerCodList(Map<String, String> farmerCodList) {
		this.farmerCodList = farmerCodList;
	}

	public Map<String, String> getFarmerNameList() {
		return farmerNameList;
	}

	public void setFarmerNameList(Map<String, String> farmerNameList) {
		this.farmerNameList = farmerNameList;
	}

	public Map<String, String> getFatherNameList() {
		return fatherNameList;
	}

	public void setFatherNameList(Map<String, String> fatherNameList) {
		this.fatherNameList = fatherNameList;
	}

	public Map<String, String> getFarmerFarmerIdList() {
		return farmerFarmerIdList;
	}

	public void setFarmerFarmerIdList(Map<String, String> farmerFarmerIdList) {
		this.farmerFarmerIdList = farmerFarmerIdList;
	}

	public String getQuty() {
		return quty;
	}

	public void setQuty(String quty) {
		this.quty = quty;
	}

	public Map<String, String> getUomList() {
		Map<String, String> uomMap = new HashMap<>();
		List<Object[]> subUomList = catalogueService.findCatalogueCodeAndNameByType(Product.listUnitBasedOnUOM);
		for (Object[] obj : subUomList) {
			uomMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));

		}
		return uomMap;
	}

	@Override
	public String getCurrentMenu() {
		String type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			if (type.equalsIgnoreCase("service")) {
				return getText("menu1.select");
			}
		}
		return getText("menu.select");

	}

}