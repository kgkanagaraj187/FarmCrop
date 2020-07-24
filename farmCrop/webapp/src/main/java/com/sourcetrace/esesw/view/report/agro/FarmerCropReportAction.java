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
import java.util.concurrent.atomic.AtomicInteger;
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

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.util.ESESystem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sourcetrace.eses.dao.ESEDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */
@SuppressWarnings("unchecked")
public class FarmerCropReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private Map<String, String> fields = new HashMap<>();
	private ILocationService locationService;
	private FarmCrops filter;
	private IProductDistributionService productDistributionService;
	@Autowired
	private ICatalogueService catalogueService;

	private String farmerName; // Returns farmerId of Farmer Entity from JSP as
	private String fatherName; // key value of Select
	private IFarmerService farmerService;
	private String farmerCode; // Returns farmerCode of Farmer Entity from JSP
								// as Key value of select
	private String villageName; // Returns Village Code from JSP as key value as
								// key value of Select
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private IClientService clientService;
	private String branchIdParma;
	private String enableMultiProduct;
	private String daterange;
	private String exportLimit;
	private String farmerCodeEnabled;
	private String seasonCode;
	private String stateName;
	private String icsName;
	private String fpo;
	private String farmerId;
	Map<String, String> farmerCodList = new LinkedHashMap<>();
	Map<String, String> farmerNameList = new LinkedHashMap<>();
	Map<String, String> farmerFarmerIdList = new LinkedHashMap<>();
	Map<String, String> fatherNameList = new LinkedHashMap<>();
	Map<String, String> villageMap = new LinkedHashMap<>();
	private String samithiName;

	@Autowired
	private IPreferencesService preferncesService;

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
		List<Object[]> farmerList = farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.stream().forEach(obj -> {

				if (villageMap.containsKey(obj[11].toString())) {
					villageMap.put(obj[11].toString(), obj[6].toString());
				}

				if (obj[2] != null && !farmerCodList.containsKey(obj[2].toString())) {
					farmerCodList.put(obj[2].toString(), obj[2].toString());
				}

				if (!farmerNameList.containsKey(obj[3])) {
					farmerNameList.put(obj[3].toString(), obj[3].toString());
				}

				if (!farmerFarmerIdList.containsKey(obj[1])) {
					farmerFarmerIdList.put(obj[1].toString(), obj[1].toString());
				}

				if (obj[4] != null && !fatherNameList.containsKey(obj[4])) {
					fatherNameList.put(obj[4].toString(), obj[4].toString());
				}

			});
		}
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}
		setFilter(new FarmCrops());
		request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	/**
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	public String detail() throws Exception {
		setFilter(new FarmCrops());

		filter.setFarm(new Farm());
		Farmer farmer = new Farmer();
		Village village = new Village();
		HarvestSeason season = new HarvestSeason();

		if (!StringUtil.isEmpty(farmerId)) {
			farmer.setFarmerId(farmerId);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			farmer.setFirstName(farmerName);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			farmer.setLastName(fatherName);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(farmerCode)) {
			farmer.setFarmerCode(farmerCode);
			filter.getFarm().setFarmer(farmer);

		}

		if (!StringUtil.isEmpty(villageName)) {
			village.setCode(villageName);
			farmer.setVillage(village);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse W = new Warehouse();
			W.setName(samithiName);
			farmer.setSamithi(W);
			filter.getFarm().setFarmer(farmer);
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

		/*
		 * if (!StringUtil.isEmpty(seasonCode)) {
		 * farmer.setSeasonCode(seasonCode); filter.getFarm().setFarmer(farmer);
		 * }
		 */

		if (!StringUtil.isEmpty(seasonCode)) {
			season.setCode(seasonCode);
			filter.setCropSeason(season);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				State s = new State();
				s.setCode(stateName);
				farmer.setStateName(s.getCode());
				filter.getFarm().setFarmer(farmer);
			}
			if (!StringUtil.isEmpty(fpo)) {
				farmer.setFpo(fpo);
				filter.getFarm().setFarmer(farmer);
			}
			if (!StringUtil.isEmpty(icsName)) {
				farmer.setIcsName(icsName);
				filter.getFarm().setFarmer(farmer);
			}

		}
		/*
		 * else{ season.setCode(clientService.findCurrentSeasonCode());
		 * filter.setCropSeason(season); }
		 */
		super.filter = this.filter;

		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), this.filter, getPage(), null);
		return sendJSONResponse(data);
	}

	public JSONObject toJSON(Object obj) {

		Object[] data = (Object[]) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled("0");
		if (!StringUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}

		Double actualCottonSeedHarvested = null;
		Double actualLintCottonHarvested = null;
		Double actualHarvested = 0.0;
		Double yieldPerAcre = null;
		List<CropHarvest> cropHarvests = new ArrayList();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(data[0])))
						? getBranchesMap().get(getParentBranchMap().get(data[0])) : getBranchesMap().get(data[0]));
			}
			rows.add(getBranchesMap().get(data[0]));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(data[0]));
			}
		}if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
		rows.add(!ObjectUtil.isEmpty(data[1]) ? data[1] : "");
		}
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			rows.add(!ObjectUtil.isEmpty(data[18]) ? data[18].toString().substring(0, 10) : "");
		}
		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			rows.add(!ObjectUtil.isEmpty(data[2]) ? data[2] : getText("NA"));
		}
		/*
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer().getName()
		 * ) ? farmCrop.getFarm().getFarmer().getName() : "");
		 */

	/*	rows.add(!ObjectUtil.isEmpty(data[3]) ? data[3] : "");*/
		/*rows.add(!ObjectUtil.isEmpty(data[26]) ? data[26] : "");*/
		String firstName =  String.valueOf(data[3]);
		String farmerId = String.valueOf(data[26]);
		String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
		rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
		/*
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer().
		 * getLastName()) ? farmCrop.getFarm().getFarmer().getLastName() : "");
		 */
		// if (getCurrentTenantId().equals("pratibha") &&
		// getBranchId().equals("organic")) {
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (getBranchId() != null && getBranchId().equals("organic")) {
				rows.add(!ObjectUtil.isEmpty(data[4]) ? data[4] : getText("NA"));
			}
		}
		/*
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo()
		 * .getRegYear()) ?
		 * farmCrop.getFarm().getFarmDetailedInfo().getRegYear().toString() :
		 * getText("NA"));
		 */
		rows.add(!ObjectUtil.isEmpty(data[5]) ? data[5] : "");
		/*
		 * if (!getCurrentTenantId().equals("pratibha")) {
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer().
		 * getVillage().getCode()) ?
		 * farmCrop.getFarm().getFarmer().getVillage().getCity().getName() :
		 * ""); rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer().
		 * getVillage().getCity().getLocality()) ?
		 * farmCrop.getFarm().getFarmer().getVillage().getCity().getLocality().
		 * getName() : "");
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer().
		 * getVillage().getCity().getLocality().getState()) ?
		 * farmCrop.getFarm().getFarmer().getVillage().getCity().getLocality().
		 * getState().getName() : ""); }
		 */

		rows.add(!ObjectUtil.isEmpty(data[6]) ? data[6] : "");
		// if (!ObjectUtil.isEmpty(farmCrop)) {
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			rows.add(!ObjectUtil.isEmpty(data[9]) ? data[9] : "");
			FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(data[25]));
			rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
		}
		if (!ObjectUtil.isEmpty(data[7])
				&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
			rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
		} else {
			rows.add(getText("NA"));
		}
		rows.add(!ObjectUtil.isEmpty(data[27]) ? data[27] : "");
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
		rows.add(!ObjectUtil.isEmpty(data[24]) ? data[24].toString() : "");
		}
		/*
		 * if (getEnableMultiProduct().equalsIgnoreCase("1")) { if
		 * (!ObjectUtil.isEmpty(data[7]) &&
		 * Integer.parseInt(String.valueOf(data[7])) ==
		 * FarmCrops.CROPTYPE.MAINCROP.ordinal()) { //if
		 * (!ObjectUtil.isEmpty(farmCrop.getProcurementVariety().
		 * getProcurementProduct())) { String cropName = null; cropName =
		 * farmCrop.getProcurementVariety().getProcurementProduct().getName();
		 * rows.add(cropName); //} } else { rows.add(getText("NA")); } }
		 */
		// }
		if (getCurrentTenantId().equalsIgnoreCase("ecoagri")) {
			/*
			 * rows.add(!ObjectUtil.isEmpty(farmCrop.getFarm()) &&
			 * !ObjectUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo()) &&
			 * !StringUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo().
			 * getTotalLandHolding())?farmCrop.getFarm().getFarmDetailedInfo().
			 * getTotalLandHolding():"");
			 */
			rows.add(!ObjectUtil.isEmpty(data[9]) ? data[9] : "");
		} else {
			// rows.add(!StringUtil.isEmpty(farmCrop.getCultiArea())?farmCrop.getCultiArea():"");
			rows.add(!ObjectUtil.isEmpty(data[10]) ? data[10] : "");
		}

		/*
		 * }
		 * rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo()
		 * .getProposedPlantingArea()) ?
		 * farmCrop.getFarm().getFarmDetailedInfo().getProposedPlantingArea() :
		 * "");
		 * 
		 */

		/*
		 * if (getCurrentTenantId().equals("pratibha")) { if
		 * (!ObjectUtil.isEmpty(farmCrop) &&
		 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
		 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety().
		 * getProcurementProduct()) &&
		 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
		 * getProcurementProduct().getName())) {
		 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct().
		 * getName()); } else { rows.add("NA"); } }
		 */
		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
		if (!ObjectUtil.isEmpty(data[11]) && data[11] != null && !StringUtil.isEmpty(String.valueOf(data[11]))) {
			FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(data[11]));
			rows.add(!ObjectUtil.isEmpty(catalogue) ? catalogue.getName() : "");
		} else {
			rows.add(getText("NA"));
		}}

		if (getEnableMultiProduct().equalsIgnoreCase("0")) {
			if (!ObjectUtil.isEmpty(data[12]) && data[12] == null || isAlpha(data[12].toString())
					|| StringUtil.isEmpty(data[12])) {
				rows.add(!ObjectUtil.isEmpty(data[12]) ? String.valueOf(data[12]) : getText("NA"));
			} else {

				rows.add(!ObjectUtil.isEmpty(data[12])
						? String.valueOf(CurrencyUtil.getDecimalFormat(Double.valueOf(data[12].toString()), "##.00"))
						: getText("NA"));
				/*
				 * rows.add(!ObjectUtil.isEmpty(farmCrop.getStapleLength()) ?
				 * String.valueOf(
				 * CurrencyUtil.getDecimalFormat(Double.valueOf(farmCrop.
				 * getStapleLength()), "##.00")) : getText("NA"));
				 */
			}
		}

		/*
		 * if(!ObjectUtil.isEmpty(farmCrop.getCropSeason())){ farmCrops =
		 * farmCrop.getFarm().getFarmCrops(); farmCropsList.addAll(farmCrops);
		 * for(FarmCrops fc : farmCropsList){
		 * if(fc.getCropCode()!=farmCrop.getCropCode()){ interCrops = interCrops
		 * + fc.getCropName() +","; } } if(!StringUtil.isEmpty(interCrops)){
		 * interCrops=interCrops.substring(interCrops.length()-2,interCrops.
		 * length()); } rows.add(!StringUtil.isEmpty(interCrops) ? interCrops :
		 * getText("NA")); } else{ rows.add(getText("NA")); }
		 */
		/*
		 * if (farmCrop.getCropCategory() ==
		 * FarmCrops.CROPTYPE.INTERCROP.ordinal() &&
		 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
		 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
		 * getProcurementProduct().getName())) {
		 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct().
		 * getName()); } else { rows.add(getText("NA")); }
		 */

		if (!ObjectUtil.isEmpty(data[7])
				&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.INTERCROP.ordinal()) {
			rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
		} else {
			rows.add(getText("NA"));
		}

		if (getCurrentTenantId().equals("pratibha")) {

			/*
			 * if (farmCrop.getCropCategory() ==
			 * FarmCrops.CROPTYPE.BORDERCROP.ordinal() &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct().getName())) {
			 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName()); } else { rows.add(getText("NA")); }
			 */
			if (!ObjectUtil.isEmpty(data[7])
					&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.BORDERCROP.ordinal()) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
			} else {
				rows.add(getText("NA"));
			}

			/*
			 * if (farmCrop.getCropCategory() ==
			 * FarmCrops.CROPTYPE.COVERCROP.ordinal() &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct().getName())) {
			 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName()); } else { rows.add(getText("NA")); }
			 */

			if (!ObjectUtil.isEmpty(data[7])
					&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.COVERCROP.ordinal()) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
			} else {
				rows.add(getText("NA"));
			}

			/*
			 * if (farmCrop.getCropCategory() ==
			 * FarmCrops.CROPTYPE.PLANTONBUND.ordinal() &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct().getName())) {
			 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName()); } else { rows.add(getText("NA")); }
			 */
			if (!ObjectUtil.isEmpty(data[7])
					&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
			} else {
				rows.add(getText("NA"));
			}

			/*
			 * if (farmCrop.getCropCategory() ==
			 * FarmCrops.CROPTYPE.TRAPCROP.ordinal() &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !StringUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct().getName())) {
			 * rows.add(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName()); } else { rows.add(getText("NA")); }
			 */
			if (!ObjectUtil.isEmpty(data[7])
					&& Integer.parseInt(String.valueOf(data[7])) == FarmCrops.CROPTYPE.TRAPCROP.ordinal()) {
				rows.add(!ObjectUtil.isEmpty(data[8]) ? data[8] : "NA");
			} else {
				rows.add(getText("NA"));
			}

		}

		if (!getCurrentTenantId().equals("chetna") && !getCurrentTenantId().equals("iccoa")
				&& !getCurrentTenantId().equals("welspun") && !getCurrentTenantId().equals("pratibha")) {
			rows.add(!ObjectUtil.isEmpty(data[13])
					? String.valueOf(CurrencyUtil.getDecimalFormat(Double.valueOf(String.valueOf(data[13])), "##.00"))
					: getText("NA"));
		}
		if (getCurrentTenantId().equals("pratibha")) {
			// rows.add(!ObjectUtil.isEmpty(farmCrop)?Double.valueOf(farmCrop.getEstimatedYield())/1000:0.00);
			rows.add(!ObjectUtil.isEmpty(data[13]) ? Double.valueOf(String.valueOf(data[13])) / 100 : getText(""));
		}
		if (getCurrentTenantId().equals("iccoa")) {
			// rows.add(!ObjectUtil.isEmpty(farmCrop)?Double.valueOf(farmCrop.getEstimatedYield())/1000:0.00);
			rows.add(!ObjectUtil.isEmpty(data[13]) ? Double.valueOf(String.valueOf(data[13])) / 1000 : getText("NA"));
		}

		cropHarvests = farmerService.findCropHarvestByFarmCode(String.valueOf(data[14]));
		for (CropHarvest cropHarvest : cropHarvests) {
			List<CropHarvestDetails> cropHarvestDetails = new ArrayList();
			cropHarvestDetails.addAll(cropHarvest.getCropHarvestDetails());
			for (CropHarvestDetails cropHarvestDetail : cropHarvestDetails) {
				if (cropHarvestDetail.getCrop().getCode().equalsIgnoreCase(String.valueOf(data[16]))) {
					actualCottonSeedHarvested = Double.parseDouble(cropHarvestDetail.getCropHarvest().getTotalQty());
					actualHarvested = actualHarvested + cropHarvestDetail.getQty();
					actualLintCottonHarvested = (actualCottonSeedHarvested * 34) / 100;
				}
			}
		}

		if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested) && !ObjectUtil.isEmpty(data[15])
					&& !data[15].toString().equalsIgnoreCase("")
							? actualHarvested / Double.parseDouble(String.valueOf(data[15])) : null;
		} else {
			if (data[10] == null || StringUtil.isEmpty(String.valueOf(data[10]))
					|| String.valueOf(data[10]).equalsIgnoreCase("0")) {

				yieldPerAcre = (double) 0;
			}

			else {
				yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested) && !StringUtil.isEmpty(String.valueOf(data[10]))
						? actualHarvested / Double.parseDouble(String.valueOf(data[10])) : null;
			}
		}
		if (!getCurrentTenantId().equalsIgnoreCase("welspun") && !getCurrentTenantId().equalsIgnoreCase("gsma") && !getCurrentTenantId().equalsIgnoreCase("livelihood")) {
			if (!ObjectUtil.isEmpty(actualHarvested)) {
				rows.add(String.valueOf(actualHarvested));
			} else {
				rows.add(getText("NA"));
			}
		}
		if (getEnableMultiProduct().equalsIgnoreCase("0")) {
			if (!getCurrentTenantId().equals("chetna")) {
				if (!ObjectUtil.isEmpty(actualLintCottonHarvested)) {
					rows.add(String.valueOf(CurrencyUtil.getDecimalFormat(actualLintCottonHarvested, "##.00")));
				} else {
					rows.add(getText("NA"));
				}
			}

		}
		if (getCurrentTenantId().equals("livelihood")){
			rows.add(!ObjectUtil.isEmpty(data[19]) ? data[19].toString(): "");
			rows.add(!ObjectUtil.isEmpty(data[20]) ? data[20].toString(): "");
			rows.add(!ObjectUtil.isEmpty(data[21]) ? data[21].toString(): "");
			rows.add(!ObjectUtil.isEmpty(data[22]) ? data[22].toString(): "");
		}
		if (!getCurrentTenantId().equals("iccoa") && !getCurrentTenantId().equalsIgnoreCase("welspun")
				&& !getCurrentTenantId().equalsIgnoreCase("gsma") && !getCurrentTenantId().equalsIgnoreCase("livelihood")) {

			if (!ObjectUtil.isEmpty(yieldPerAcre) && !String.valueOf(yieldPerAcre).equalsIgnoreCase("Infinity")) {
				rows.add(String.valueOf(CurrencyUtil.getDecimalFormat(yieldPerAcre, "##.00")));
			} else {
				rows.add(getText("0.00"));
			}
		}
		// rows.add(!StringUtil.isEmpty(farmCrop.getFarm().getFarmer()) ?
		// farmCrop : "");

		jsonObject.put("id", String.valueOf(data[17]));
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */
	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	public FarmCrops getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param warehouseProduct2
	 *            the new filter
	 */
	public void setFilter(FarmCrops farmCrops2) {

		this.filter = farmCrops2;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		Farm farm = new Farm();
		Farmer farmer = new Farmer();
		setFilter(new FarmCrops());
		filter.setFarm(farm);
		filter.getFarm().setFarmer(farmer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("farmerCropReport"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
        HSSFCellStyle style4 = wb.createCellStyle();

		HSSFCellStyle filterStyle = wb.createCellStyle();

		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 22);

		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 12);

		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 10);

		HSSFFont filterFont = wb.createFont();
		filterFont.setFontHeightInPoints((short) 12);

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null,
				filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null, filterRow10 = null;
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

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerCropReport")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);

		/*
		 * if (isMailExport()) { rowNum++; rowNum++; filterRowTitle =
		 * sheet.createRow(rowNum++); cell = filterRowTitle.createCell(1);
		 * cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("filter")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * filterRow1 = sheet.createRow(rowNum++);
		 * 
		 * cell = filterRow1.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("StartingDate")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow1.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(filterDateFormat.format(getsDate())));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * filterRow2 = sheet.createRow(rowNum++);
		 * 
		 * cell = filterRow2.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("EndingDate")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow2.createCell(2); cell.setCellValue(new
		 * HSSFRichTextString(filterDateFormat.format(geteDate())));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * //if
		 * (!StringUtil.isEmpty(filter.getFarm().getFarmer().getFarmerCode())) {
		 * filterRow3 = sheet.createRow(rowNum++);
		 * 
		 * cell = filterRow3.createCell(1); cell.setCellValue(new
		 * HSSFRichTextString(getLocaleProperty("farmerCode")));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
		 * 
		 * cell = filterRow3.createCell(2); cell .setCellValue(new
		 * HSSFRichTextString(filter.getFarm().getFarmer().getFarmerCode()));
		 * filterFont.setBoldweight((short) 12);
		 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle); //}
		 * 
		 * filterRow4 = sheet.createRow(rowNum++); filterRow5 =
		 * sheet.createRow(rowNum++); }
		 */if (isMailExport()) {
			rowNum++;
			rowNum++;
			
			  filterRowTitle = sheet.createRow(rowNum++); cell =
			  filterRowTitle.createCell(1); cell.setCellValue(new
			  HSSFRichTextString(getLocaleProperty("filter")));
			  filterFont.setBoldweight((short) 12);
			 filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			

			filterRow1 = sheet.createRow(rowNum++);

			
			  cell = filterRow1.createCell(1); cell.setCellValue(new
			  HSSFRichTextString(getLocaleProperty("StartingDate")));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			  
			  cell = filterRow1.createCell(2); cell.setCellValue(new
			  HSSFRichTextString(filterDateFormat.format(getsDate())));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 
			  filterRow2 = sheet.createRow(rowNum++);
			  
			  cell = filterRow2.createCell(1); cell.setCellValue(new
			  HSSFRichTextString(getLocaleProperty("EndingDate")));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			  
			  cell = filterRow2.createCell(2); cell.setCellValue(new
			  HSSFRichTextString(filterDateFormat.format(geteDate())));
			  filterFont.setBoldweight((short) 12);
			  filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 

			/*
			 * if (!StringUtil.isEmpty(farmerName)) {
			 * 
			 * farmer = farmerService.findFarmerByFarmerId(farmerName);
			 * filter.getFarm().getFarmer().setFarmerId(farmerName);
			 * 
			 * filterRow3 = sheet.createRow(rowNum++);
			 * 
			 * cell = filterRow3.createCell(1); cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("farmerName")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow3.createCell(2); cell.setCellValue(new
			 * HSSFRichTextString(farmer.getFirstName() + " - " +
			 * farmer.getLastName())); filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * }
			 */
			
			
			if (!StringUtil.isEmpty(farmerName)) {

				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);

				farmer = farmerService.findFarmerByFarmerName(farmerName);

				cell.setCellValue(new HSSFRichTextString(farmer.getFirstName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(fatherName)) {

				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fatherName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(fatherName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmerCode)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmerCode));
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
				HarvestSeason season = farmerService.findSeasonNameByCode(seasonCode);
				cell.setCellValue(new HSSFRichTextString(season.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(villageName)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(villageName)
						? locationService.findVillageByCode(villageName).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			/*
			 * if (!StringUtil.isEmpty(branchIdParma)) { filterRow6 =
			 * sheet.createRow(rowNum++); cell = filterRow6.createCell(1);
			 * cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("BranchId")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow6.createCell(2); cell.setCellValue( new
			 * HSSFRichTextString((branchesMap.get(filter.getFarm().getFarmer().
			 * getBranchId())))); filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * }
			 */

			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {

					filterRow6 = sheet.createRow(rowNum++);
					filterRow7 = sheet.createRow(rowNum++);

					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					cell.setCellValue(new HSSFRichTextString((getBranchesMap().get(getSubBranchIdParma()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					filterRow6 = sheet.createRow(rowNum++);
					cell = filterRow6.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("BranchId")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow6.createCell(2);
					cell.setCellValue(
							new HSSFRichTextString((branchesMap.get(filter.getFarm().getFarmer().getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
			}

			if (getCurrentTenantId().equals("chetna")) {

				if (!StringUtil.isEmpty(stateName)) {
					filterRow8 = sheet.createRow(rowNum++);
					cell = filterRow8.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow8.createCell(2);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(stateName)
							? locationService.findStateByCode(stateName).getName() : getText("NA"))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(fpo)) {
					filterRow9 = sheet.createRow(rowNum++);
					cell = filterRow9.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fpo")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow9.createCell(2);
					FarmCatalogue fc = getCatlogueValueByCode(fpo);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}
				if (!StringUtil.isEmpty(icsName)) {
					filterRow10 = sheet.createRow(rowNum++);
					cell = filterRow10.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow10.createCell(2);
					FarmCatalogue fc = getCatlogueValueByCode(icsName);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				}

			}

			if (!StringUtil.isEmpty(samithiName)) {
				filterRow8 = sheet.createRow(rowNum++);
				cell = filterRow8.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("samithiName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow8.createCell(2);
				cell.setCellValue(new HSSFRichTextString(samithiName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		// String mainGridColumnHeaders = getText("exportColumnHeader");

		String mainGridColumnHeaders = "";
		int mainGridIterator = 0;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { mainGridColumnHeaders =
		 * getText("ExportColumnHeadingBranch"); } else if
		 * (!StringUtil.isEmpty(branchIdValue)) { mainGridColumnHeaders =
		 * getText("ExportColumnHeadingBranch"); } } else if
		 * (getIsMultiBranch().equalsIgnoreCase("1") &&
		 * getIsParentBranch().equals("0")) { mainGridColumnHeaders =
		 * getText("ExportColumnHeadingBranch1"); } else {
		 * if(getCurrentTenantId().equals("sagi") &&
		 * farmerCodeEnabled.equals("1")){ mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeadersagi"); } else if
		 * (farmerCodeEnabled.equals("1")) { mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeader"); } else {
		 * mainGridColumnHeaders = getLocaleProperty("exportColumnHeader1"); } }
		 * if (getCurrentTenantId().equals("pratibha")) { mainGridColumnHeaders
		 * = getText("ExportHeadingPra"); if (getBranchId().equals("bci")) {
		 * mainGridColumnHeaders = getText("ExportHeadingBci"); } }
		 * 
		 * if (getCurrentTenantId().equals("awi")) { mainGridColumnHeaders =
		 * getText("ExportHeadingAwi"); } if
		 * (getCurrentTenantId().equals("mcash")) { mainGridColumnHeaders =
		 * getText("ExportHeadingMcash"); }
		 * 
		 * if (getCurrentTenantId().equals("meridian")) { mainGridColumnHeaders
		 * = getText("exportColumnHeadermeridian"); }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerCropColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerCropColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganicFarmerCropExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCIFarmerCropExportHeader");
			}

		} else  {
			if (StringUtil.isEmpty(branchIdValue) && farmerCodeEnabled.equals("1") || getFarmerCodeEnabled().equalsIgnoreCase("1")) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue) && farmerCodeEnabled.equals("1") || getFarmerCodeEnabled().equalsIgnoreCase("1")) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerCropColumnHeader");
			}
			else {
				if (StringUtil.isEmpty(branchIdValue)&& farmerCodeEnabled.equals("0") || getFarmerCodeEnabled().equalsIgnoreCase("0")) {
					mainGridColumnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderWithoutFarmerCodeBrn");
				} else {
					mainGridColumnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderWithoutFarmerCode");
				}
			}
		}

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
			this.filter = new FarmCrops();
		if (!StringUtil.isEmpty(branchIdParma)) { // set filter of branch id and
													// check it.
			filter.setBranchId(branchIdParma);
		}

		Village village = new Village();
		HarvestSeason season = new HarvestSeason();
		Farmer farmerFilter = new Farmer();

		if (!StringUtil.isEmpty(farmerName)) {
			farmerFilter.setFirstName(farmerName);
			filter.getFarm().setFarmer(farmerFilter);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			farmerFilter.setLastName(fatherName);
			filter.getFarm().setFarmer(farmerFilter);
		}

		if (!StringUtil.isEmpty(farmerCode)) {
			farmerFilter.setFarmerCode(farmerCode);
			filter.getFarm().setFarmer(farmerFilter);

		}

		if (!StringUtil.isEmpty(villageName)) {
			village.setCode(villageName);
			farmerFilter.setVillage(village);
			filter.getFarm().setFarmer(farmerFilter);
		}

		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse W = new Warehouse();
			W.setName(samithiName);
			farmer.setSamithi(W);
			filter.getFarm().setFarmer(farmer);
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

		if (!StringUtil.isEmpty(seasonCode)) {
			season.setCode(seasonCode);
			filter.setCropSeason(season);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			farmerFilter.setFarmerId(farmerId);
			filter.getFarm().setFarmer(farmerFilter);

		}

		/*
		 * else{ season.setCode(clientService.findCurrentSeasonCode());
		 * filter.setCropSeason(season); }
		 */

		super.filter = this.filter;
		// Map data = isMailExport() ? readData() : readExportData();
		Map data = readData("farmCrops");

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		Long serialNo = 0L;
		String multiVal = getEnableMultiProduct();
		for (Object[] obj : mainGridRows) {

			Object[] datas = (Object[]) obj;

			// if (isMailExport() && flag) {
			// 1-branchId,2-seasonName,3-farmerId,4-farmerName,5-icsName,6-villageName,7-FarmName,8-cropCategory,9-cultiArea,10-type,11-stapleLength,12-estimatedYield,13-seedCotton
			// 14-lintCotton,15-actualSeedYield,16-farm.farmCode,17-farm.farmDetailedInfo.proposedPlantingArea,18-procurementVariety.procurementProduct.name,19-procurementVariety.procurementProduct.code,20-procurementVariety.procurementProduct

			// {
			//// Beginning of code:
			row = sheet.createRow(rowNum++);
			colNum = 0;

			serialNo++;
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((String.valueOf(serialNo))));
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style4);
			

			if ((multiVal.equalsIgnoreCase("1") && getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[1])))
									? getBranchesMap().get(getParentBranchMap().get(datas[1]))
									: getBranchesMap().get(datas[1])));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(getBranchesMap().get(datas[1]));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellValue(getBranchesMap().get(datas[1]));
				}
			}
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[2]) ? datas[2].toString() : "N/A")));
			}
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(datas[22]) ? datas[22].toString().substring(0, 10) : "N/A")));
			}
			if (farmerCodeEnabled.equals("1") || getFarmerCodeEnabled().equalsIgnoreCase("1")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[21]) ? datas[21].toString() : "N/A")));
				}
			
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(
					new HSSFRichTextString((!ObjectUtil.isEmpty(datas[21]) ? datas[21].toString() : "N/A")));
			}

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue( new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.getFarm(
			 * ).getFarmer().getFirstName()) ?
			 * farmCrop.getFarm().getFarmer().getFirstName() + "/" +
			 * farmCrop.getFarm().getFarmer().getLastName() : getText("NA"))));
			 */

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[4]) ? datas[4].toString() : "N/A")));

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue( new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.getFarm().
			 * getFarmer().getLastName()) ?
			 * farmCrop.getFarm().getFarmer().getLastName() : getText("NA"))));
			 */

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.getFarm( )) ?
			 * (farmCrop.getFarm().getFarmer().getFirstName()+" - "
			 * +farmCrop.getFarm().getFarmer().getLastName()) :
			 * getText("NA"))));
			 */if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
				if (getBranchId() != null && getBranchId().equals("organic")) {
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString((!ObjectUtil.isEmpty(datas[5]) ? datas[5].toString() : "N/A")));
				}
			}

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(
			 * (!ObjectUtil.isEmpty(farmCrop.getFarm().getFarmer().
			 * getDateOfJoining()) ?
			 * farmCrop.getFarm().getFarmer().getDateOfJoining(). toString() :
			 * getText("NA"))));
			 */

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[6]) ? datas[6].toString() : "N/A")));
			/*
			 * if (!getCurrentTenantId().equals("pratibha")) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(
			 * (!ObjectUtil.isEmpty(farmCrop.getFarm().getFarmer().getVillage().
			 * getCity()) ?
			 * farmCrop.getFarm().getFarmer().getVillage().getCity().getName() :
			 * getText("NA"))));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil
			 * .isEmpty(farmCrop.getFarm().getFarmer().getVillage().getCity().
			 * getLocality()) ?
			 * farmCrop.getFarm().getFarmer().getVillage().getCity().getLocality
			 * ().getName() : getText("NA"))));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil
			 * .isEmpty(farmCrop.getFarm().getFarmer().getVillage().getCity().
			 * getLocality().getState()) ?
			 * farmCrop.getFarm().getFarmer().getVillage().getCity().getLocality
			 * ().getState() .getName() : getText("NA")))); }
			 */

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[7]) ? datas[7].toString() : "")));
			
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[27]) ? datas[27].toString() : "N/A")));
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(datas[29]) ? getCatlogueValueByCode(datas[29].toString()).getName() : "")));
			}
			
			/*
			 * if (getEnableMultiProduct().equalsIgnoreCase("1")) { if
			 * (!ObjectUtil.isEmpty(farmCrop)) { cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String
			 * .valueOf(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName()))); } else { cell = row.createCell(colNum++);
			 * cell.setCellValue(new HSSFRichTextString(getText("NA"))); } }
			 */
			if (multiVal.equalsIgnoreCase("1")) {
				if (!ObjectUtil.isEmpty(datas[0]) || datas[0] != null) {
					if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
						if (!ObjectUtil.isEmpty(datas[20])) {
							String cropName = null;
							cropName = datas[18].toString();

							cell = row.createCell(colNum++);
							cell.setCellValue(cropName);
						}
					} else {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(getText("NA")));
					}
				}
			}
			
			if (getCurrentTenantId().equals("welspun")) {
		   	    cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[30])));
				}
			
			if (getCurrentTenantId().equals("agro")) {
		   	    cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[30])));
				}
			
			if (!getCurrentTenantId().equals("welspun") && !getCurrentTenantId().equals("kenyafpo")
					 && !getCurrentTenantId().equals("agro")) {	
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(!ObjectUtil.isEmpty(datas[30]) ? datas[30].toString() : "N/A")));
			}
			if (getCurrentTenantId().equals("kenyafpo")) {	
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(datas[30]) ? datas[30].toString() : "N/A")));
				}
		
			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(datas[28]) ? datas[28].toString() : "N/A")));
				
			}

			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[9]) ? datas[9].toString() : "")));
			style3.setAlignment(CellStyle.ALIGN_RIGHT);
			cell.setCellStyle(style3);
			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(
			 * (!StringUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo().
			 * getProposedPlantingArea()) ?
			 * farmCrop.getFarm().getFarmDetailedInfo().getProposedPlantingArea(
			 * ) : "")));
			 */

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(
			 * (!ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct()) ?
			 * farmCrop.getProcurementVariety().getProcurementProduct().
			 * getName() : getText("NA"))));
			 */

			/*
			 * if (getCurrentTenantId().equals("pratibha")) { cell =
			 * row.createCell(colNum++); if (!ObjectUtil.isEmpty(farmCrop) &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
			 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety().
			 * getProcurementProduct()) && !StringUtil
			 * .isEmpty(farmCrop.getProcurementVariety().getProcurementProduct()
			 * .getName())) { cell.setCellValue(new HSSFRichTextString(
			 * farmCrop.getProcurementVariety().getProcurementProduct().getName(
			 * ))); } else { cell.setCellValue(new
			 * HSSFRichTextString(getText("NA"))); } }
			 */

			/*
			 * if (!StringUtil.isEmpty(String.valueOf(farmCrop.getType()))) {
			 * cell = row.createCell(colNum++);
			 */
			/*
			 * FarmCatalogue catalogue =
			 * getCatlogueValueByCode(String.valueOf(farmCrop.getType()));
			 */
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			cell = row.createCell(colNum++);
			cell.setCellValue(new HSSFRichTextString(
					(!ObjectUtil.isEmpty(datas[10]) ? getCatlogueValueByCode(datas[10].toString()).getName() : "")));
			
			 } else { cell = row.createCell(colNum++); cell.setCellValue(new
			 HSSFRichTextString(getText("NA"))); }
			
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
			if (multiVal.equalsIgnoreCase("0")) {
				if (datas[11] == null || StringUtil.isEmpty(datas[11])) {
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString((!ObjectUtil.isEmpty(datas[11]) ? datas[11].toString() : "")));

				} else {
					cell = row.createCell(colNum++);
					cell.setCellValue(!ObjectUtil.isEmpty(datas[11].toString())
							&& StringUtil.isDouble(datas[11].toString())
									? String.valueOf(
											CurrencyUtil.getDecimalFormat(Double.valueOf(datas[11].toString()),
													"##.00"))
									: getText("NA"));

				}
			}
			}
			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.getType( )) ?
			 * String.valueOf(getText("type" + farmCrop.getType())) :
			 * getText("NA"))));
			 */
			/*
			 * cell = row.createCell(colNum++); if
			 * (getEnableMultiProduct().equalsIgnoreCase("0")) {
			 * cell.setCellValue(new HSSFRichTextString(String.valueOf(farmCrop.
			 * getStapleLength()))); } else { cell.setCellValue(new
			 * HSSFRichTextString(getText("NA"))); }
			 */

			/*
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.
			 * getStapleLength()) ? String.valueOf(farmCrop.getStapleLength()) :
			 * getText("NA"))));
			 */
		

			cell = row.createCell(colNum++);
			if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.INTERCROP.ordinal()) {
				cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[18])));
			} else {
				cell.setCellValue(new HSSFRichTextString(getText("NA")));
			}
			
			
			if (getCurrentTenantId().equals("pratibha")) {
				cell = row.createCell(colNum++);
				if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.BORDERCROP.ordinal()) {
					cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[18])));
				} else {
					cell.setCellValue(new HSSFRichTextString(getText("NA")));
				}

				cell = row.createCell(colNum++);

				if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.COVERCROP.ordinal()) {
					cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[18])));
				} else {
					cell.setCellValue(new HSSFRichTextString(getText("NA")));
				}
				cell = row.createCell(colNum++);
				if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()) {
					cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[18])));
				} else {
					cell.setCellValue(new HSSFRichTextString(getText("NA")));
				}
				cell = row.createCell(colNum++);

				if (Integer.valueOf(datas[8].toString()) == FarmCrops.CROPTYPE.TRAPCROP.ordinal()) {
					cell.setCellValue(new HSSFRichTextString(String.valueOf(datas[18])));
				} else {
					cell.setCellValue(new HSSFRichTextString(getText("NA")));
				}

			}
	
			/*
			 * if(farmCrop.getCropCategory()==FarmCrops.CROPTYPE.
			 * INTERCROP.ordinal()
			 * &&!ObjectUtil.isEmpty(farmCrop.getProcurementVariety())){ cell =
			 * row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(farmCrop.
			 * getProcurementVariety().getProcurementProduct().getName() ))); }
			 * else{
			 * 
			 * cell.setCellValue(new HSSFRichTextString(getText("NA"))); }
			 */

			// cell.setCellValue(new
			// HSSFRichTextString((!ObjectUtil.isEmpty(farmCrop.getCropName())
			// ? String.valueOf(farmCrop.getCropName()) :
			// getText("NA"))));
			if (!getCurrentTenantId().equals("chetna") && !getCurrentTenantId().equals("iccoa")
					&& !getCurrentTenantId().equals("welspun") && !getCurrentTenantId().equals("pratibha")) {
				cell = row.createCell(colNum++);
				style3.setAlignment(CellStyle.ALIGN_RIGHT);
			    cell.setCellStyle(style3);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(datas[12]) ? datas[12].toString() : "")));
			}
			if (getCurrentTenantId().equals("pratibha")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(!ObjectUtil.isEmpty(datas[12]) ? Double.valueOf(datas[12].toString()) / 100 : 0.00);
			}
			if (getCurrentTenantId().equals("iccoa")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(Double.valueOf(datas[12].toString()) / 1000);
			}
			Double actualCottonSeedHarvested = 0.0;
			Double actualLintCottonHarvested = 0.0;
			Double actualHarvested = 0.0;

			/*
			 * List<CropHarvest> cropHarvests = farmerService
			 * .findCropHarvestByFarmCode(farmCrop.getFarm().getFarmCode ());
			 * for (CropHarvest cropHarvest : cropHarvests) {
			 * List<CropHarvestDetails> cropHarvestDetails = new ArrayList();
			 * cropHarvestDetails.addAll(cropHarvest. getCropHarvestDetails());
			 * for (CropHarvestDetails cropHarvestDetail : cropHarvestDetails) {
			 * if (cropHarvestDetail.getCrop().getCode() ==
			 * farmCrop.getCropCode()) { actualCottonSeedHarvested = Double
			 * .parseDouble(cropHarvestDetail.getCropHarvest(). getTotalQty());
			 * actualLintCottonHarvested = (actualCottonSeedHarvested * 34) /
			 * 100; } } }
			 * 
			 * Double yieldPerAcre =
			 * !ObjectUtil.isEmpty(actualCottonSeedHarvested) ?
			 * actualCottonSeedHarvested / Double
			 * .parseDouble(farmCrop.getFarm().getFarmDetailedInfo().
			 * getProposedPlantingArea()) : null; cell =
			 * row.createCell(colNum++); if
			 * (!ObjectUtil.isEmpty(actualCottonSeedHarvested)) {
			 * cell.setCellValue(new HSSFRichTextString(String.valueOf(
			 * actualCottonSeedHarvested))); } else { cell.setCellValue(new
			 * HSSFRichTextString(getText("NA"))); }
			 */
			if (multiVal.equalsIgnoreCase("1")) {
				List<CropHarvest> cropHarvests = farmerService.findCropHarvestByFarmCode(datas[16].toString());
				for (CropHarvest cropHarvest : cropHarvests) {
					List<CropHarvestDetails> cropHarvestDetails = new ArrayList();
					cropHarvestDetails.addAll(cropHarvest.getCropHarvestDetails());
					for (CropHarvestDetails cropHarvestDetail : cropHarvestDetails) {
						if (cropHarvestDetail.getCrop().getCode().equalsIgnoreCase(datas[19].toString())) {
							actualCottonSeedHarvested = Double
									.parseDouble(cropHarvestDetail.getCropHarvest().getTotalQty());
							actualHarvested = actualHarvested + cropHarvestDetail.getQty();
							actualLintCottonHarvested = (actualCottonSeedHarvested * 34) / 100;
						}
					}
				}
			}
			Double yieldPerAcre = 0.0;
			if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (!ObjectUtil.isEmpty(datas[17])) {
					yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested) && !StringUtil.isEmpty(datas[17].toString())
							? actualHarvested / Double.parseDouble(datas[17].toString()) : 0.00;
				}
			} else {
				if (datas[9].toString().equalsIgnoreCase("0")) {
					yieldPerAcre = (double) 0;
				} else {
					yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested) && !StringUtil.isEmpty(datas[9].toString())
							? actualHarvested / Double.parseDouble(datas[9].toString()) : 0.00;

				}
			}
			/*
			 * Double yieldPerAcre =
			 * !ObjectUtil.isEmpty(actualCottonSeedHarvested) &&
			 * !StringUtil.isEmpty(farmCrop.getFarm().getFarmDetailedInfo().
			 * getProposedPlantingArea()) ? actualCottonSeedHarvested /
			 * Double.parseDouble(
			 * farmCrop.getFarm().getFarmDetailedInfo().getProposedPlantingArea(
			 * )) : 0.0;
			 */
			if (!getCurrentTenantId().equalsIgnoreCase("welspun") && !getCurrentTenantId().equalsIgnoreCase("gsma")&& !getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				cell = row.createCell(colNum++);
				if (!ObjectUtil.isEmpty(actualHarvested)) {
					cell.setCellValue(new HSSFRichTextString(String.valueOf(actualHarvested)));
					style3.setAlignment(CellStyle.ALIGN_RIGHT);
				    cell.setCellStyle(style3);
				} else {
					cell.setCellValue(new HSSFRichTextString(getText("NA")));
				}
			}
			if (multiVal.equalsIgnoreCase("0")) {
				if (!getCurrentTenantId().equals("chetna")) {
					cell = row.createCell(colNum++);
					if (!ObjectUtil.isEmpty(actualLintCottonHarvested) && !Double.isNaN(actualLintCottonHarvested)) {
						cell.setCellValue(new HSSFRichTextString(String.valueOf(actualLintCottonHarvested)));
					} else {
						cell.setCellValue(new HSSFRichTextString(getText("NA")));
					}
				}
			}

			if (!getCurrentTenantId().equals("iccoa") && !getCurrentTenantId().equals("welspun")
					&& !getCurrentTenantId().equalsIgnoreCase("gsma") && !getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				cell = row.createCell(colNum++);
				if (!ObjectUtil.isEmpty(yieldPerAcre) && !String.valueOf(yieldPerAcre).equalsIgnoreCase("Infinity")) {
					cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat(yieldPerAcre, "##.00")));
					style3.setAlignment(CellStyle.ALIGN_RIGHT);
					cell.setCellStyle(style3);
				} else {
					cell.setCellValue(new HSSFRichTextString("0.00"));
				}
			}
			
			if (getCurrentTenantId().equalsIgnoreCase("livelihood")) {
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(datas[23]) ? datas[23].toString() : "N/A")));
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(datas[24]) ? datas[24].toString() : "N/A")));
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(datas[25]) ? datas[25].toString() : "N/A")));
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(datas[26]) ? datas[26].toString() : "N/A")));
			}

			/*
			 * cell = row.createCell(colNum++); if
			 * (getEnableMultiProduct().equalsIgnoreCase("0") &&
			 * !ObjectUtil.isEmpty(actualLintCottonHarvested)) {
			 * cell.setCellValue(new HSSFRichTextString(String.valueOf(
			 * actualLintCottonHarvested))); } else { cell.setCellValue(new
			 * HSSFRichTextString(getText("NA"))); }
			 */
			/*
			 * cell = row.createCell(colNum++);
			 * if(!ObjectUtil.isEmpty(actualLintCottonHarvested)){
			 * cell.setCellValue(new HSSFRichTextString(String.valueOf(
			 * actualLintCottonHarvested))); }else{ cell.setCellValue(new
			 * HSSFRichTextString(getText("NA"))); }
			 */
			/*
			 * cell = row.createCell(colNum++); if
			 * (!ObjectUtil.isEmpty(yieldPerAcre)) { cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(yieldPerAcre))); } else {
			 * cell.setCellValue(new HSSFRichTextString(getText("NA"))); }
			 */

			// }
			/// End of code.

			/**
			 * if (filter.getFarm().getFarmer().getVillage().getId() > 0) {
			 * 
			 * cell = filterRow5.createCell(1); cell.setCellValue(new
			 * HSSFRichTextString(getLocaleProperty("village")));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * cell = filterRow5.createCell(2); cell.setCellValue(new
			 * HSSFRichTextString(procurement.getVillage().getName() + " - " +
			 * procurement.getVillage().getCode()));
			 * filterFont.setBoldweight((short) 12);
			 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 * filterStyle.setFont(filterFont); cell.setCellStyle(filterStyle);
			 * 
			 * if
			 * (StringUtil.isEmpty(filter.getAgroTransaction().getFarmerId())) {
			 * sheet.shiftRows(filterRow4.getRowNum() + 1,
			 * filterRow5.getRowNum() + 1, -1); } }
			 * 
			 * flag = false; }
			 * 
			 * row = sheet.createRow(rowNum++); colNum = 0;
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ?
			 * (!StringUtil.isEmpty(sdf.format(procurement
			 * .getAgroTransaction().getTxnTime())) ? sdf.format(procurement
			 * .getAgroTransaction().getTxnTime()) : "") : ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ?
			 * (!StringUtil.isEmpty(procurement.getAgroTransaction()
			 * .getFarmerId()) ? procurement.getAgroTransaction().getFarmerId()
			 * : "") : ""));
			 * 
			 * cell = row.createCell(colNum++); cell .setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ? (!StringUtil.isEmpty(procurement
			 * .getAgroTransaction().getFarmerName()) ? procurement
			 * .getAgroTransaction().getFarmerName() : "") : "")); cell =
			 * row.createCell(colNum++); cell .setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ? (!StringUtil.isEmpty(procurement
			 * .getMobileNumber()) ? procurement .getMobileNumber() :
			 * getText("NA")) : ""));
			 * 
			 * String coOp = ""; if
			 * (!ObjectUtil.isEmpty(procurement.getAgroTransaction()) &&
			 * !StringUtil.isEmpty(procurement.getAgroTransaction().getAgentId()))
			 * { Agent agent =
			 * agentService.findAgentByAgentId(procurement.getAgroTransaction()
			 * .getAgentId()); if (!ObjectUtil.isEmpty(agent) &&
			 * AgentType.COOPERATIVE_MANAGER.equalsIgnoreCase(agent.getAgentType()
			 * .getCode())) coOp = "*"; }
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ?
			 * (!StringUtil.isEmpty(procurement.getAgroTransaction()
			 * .getAgentName()) ?
			 * procurement.getAgroTransaction().getAgentName() + coOp : "") :
			 * ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString( !ObjectUtil.isEmpty(procurement.getVillage())
			 * ? procurement.getVillage() .getName() : ""));
			 * 
			 * Map<String, Object> details =
			 * getTotalProductDetails(procurement);
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(details.get(NO_OF_BAGS))));
			 * 
			 * /* cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) details
			 * .get(GROSS_WEIGHT), "##.000")));
			 * 
			 * /* cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) details
			 * .get(TARE_WEIGHT), "##.000")));
			 * 
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) details
			 * .get(NET_WEIGHT), "##.000")));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurement
			 * .getAgroTransaction()) ?
			 * CurrencyUtil.thousandSeparator(procurement
			 * .getAgroTransaction().getTxnAmount()) : ""));
			 * 
			 * HSSFRow subGridRowHead = sheet.createRow(rowNum++); String
			 * sunGridcolumnHeaders =
			 * getLocaleProperty("exportSubColumnHeader"); int subGridIterator =
			 * 1;
			 * 
			 * 
			 * for (ProcurementDetail procurementDetail :
			 * procurement.getProcurementDetails()) {
			 * 
			 * row = sheet.createRow(rowNum++); colNum = 1;
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurementDetail
			 * .getProcurementGrade()) ? procurementDetail.getProcurementGrade()
			 * .getProcurementVariety().getProcurementProduct().getName() :
			 * ""));
			 * 
			 * //String quality = procurementDetail.getQuality(); //if
			 * (getGradeMasterMap().containsKey(quality)) { //quality =
			 * getGradeMasterMap().get(quality).getName(); //}
			 * 
			 * cell = row.createCell(colNum++); //cell.setCellValue(new
			 * HSSFRichTextString(quality)); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurementDetail
			 * .getProcurementGrade()) ? procurementDetail.getProcurementGrade()
			 * .getProcurementVariety().getName() : ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(!ObjectUtil.isEmpty(procurementDetail
			 * .getProcurementGrade()) ?
			 * procurementDetail.getProcurementGrade().getName() : ""));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil
			 * .thousandSeparator(procurementDetail.getRatePerUnit())));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(String.valueOf(procurementDetail
			 * .getNumberOfBags())));
			 * 
			 * /*cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat(
			 * procurementDetail.getGrossWeight(), "##.000")));
			 * 
			 * /* cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat(
			 * procurementDetail.getTareWeight(), "##.000")));
			 * 
			 * cell = row.createCell(colNum++); cell.setCellValue(new
			 * HSSFRichTextString(CurrencyUtil.getDecimalFormat(
			 * procurementDetail.getNetWeight(), "##.000")));
			 * 
			 * 
			 */

			// }
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
		String fileName = getLocaleProperty("farmerCropReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

	}

	public String getFarmerName() {

		return farmerName;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/*
	 * public Map<String, String> getFarmerNameList() {
	 * 
	 * Map<String, String> farmersListMap = new LinkedHashMap<String, String>();
	 * List<Farmer> farmerList = farmerService.listFarmer(); for (Farmer obj :
	 * farmerList) { farmersListMap.put(obj.getFarmerId(), obj.getFirstName() +
	 * " - " + obj.getLastName());
	 * 
	 * } return farmersListMap; }
	 */

	/*
	 * public Map<String, String> getFarmerNameList() { Map<String, String>
	 * farmerListMap = new LinkedHashMap<String, String>(); List<Object[]>
	 * farmersList = farmerService.listFarmerInfo(); farmerListMap =
	 * farmersList.stream() .collect(Collectors.toMap(obj ->
	 * String.valueOf(obj[1]), obj -> (String.valueOf(obj[3])))); farmerListMap
	 * = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerListMap; }
	 */

	/*
	 * public List<String[]> getFatherNameList() {
	 * 
	 * List<String[]> fatherNameList = farmerService.listFatherName();
	 * 
	 * return fatherNameList; }
	 * 
	 * public List<String[]> getFarmerCodeList() {
	 * 
	 * 
	 * Map<String, String> farmersListMap = new LinkedHashMap<String, String>();
	 * //Note: commented to sort the dropdown. List<Farmer> farmerList =
	 * farmerService.listFarmer(); for (Farmer obj : farmerList) {
	 * farmersListMap.put(obj.getFarmerCode(), obj.getFarmerCode());
	 * 
	 * } return farmersListMap;
	 * 
	 * List<String[]> farmerCodeList = farmerService.listFarmerCode();
	 * 
	 * return farmerCodeList;
	 * 
	 * }
	 */

	/*
	 * public Map<String, String> getVillageList() {
	 * 
	 * Map<String, String> villageListMap = new LinkedHashMap<String, String>();
	 * List<Village> villageList = locationService.listVillage(); for (Village
	 * obj : villageList) { villageListMap.put(obj.getCode(), obj.getName()); }
	 * return villageListMap; }
	 */

	public String getFarmerCode() {

		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {

		this.farmerCode = farmerCode;
	}

	public String getVillageName() {

		return villageName;
	}

	public void setVillageName(String villageName) {

		this.villageName = villageName;
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

	public static SimpleDateFormat getFilenamedateformat() {

		return fileNameDateFormat;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getLocaleProperty("FarmerCropReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerCropReportList"), fileMap, ".xls"));
		return "xls";
	}

	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);

		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
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

	public boolean isAlpha(String name) {
		return name.matches("[a-zA-Z ]+");
	}

	@Override
	public String populatePDF() throws Exception {
		List<String> filters = new ArrayList<String>();
		List<Object> fields = new ArrayList<Object>();
		List<List<Object>> entityObject = new ArrayList<List<Object>>();
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.

		setMailExport(true);

		setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(IExporter.EXPORT_MANUAL)
				? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));

		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		Farm farm = new Farm();
		Farmer farmer = new Farmer();
		setFilter(new FarmCrops());
		filter.setFarm(farm);
		filter.getFarm().setFarmer(farmer);
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
		
		if (isMailExport()) {

			filters.add(getLocaleProperty("filter"));
			
			  filters.add(getLocaleProperty("StartingDate") + " : " +
			  filterDateFormat.format(getsDate()));
			  filters.add(getLocaleProperty("EndingDate") + " : " +
			  filterDateFormat.format(geteDate()));
			 

			/*
			 * if (!StringUtil.isEmpty(farmerName)) {
			 * 
			 * farmer = farmerService.findFarmerByFarmerId(farmerName);
			 * filter.getFarm().getFarmer().setFarmerId(farmerName);
			 * filters.add(getLocaleProperty("farmerName") + " : " +
			 * farmer.getFirstName() + " - " + farmer.getLastName());
			 * 
			 * }
			 */

			/*
			 * if (!StringUtil.isEmpty(farmerName)) {
			 * 
			 * farmer = farmerService.findFarmerByFarmerId(farmerName);
			 * filter.getFarm().getFarmer().setFarmerId(farmerName);
			 * filters.add(getLocaleProperty("farmerName") + " : " +
			 * farmer.getFirstName());
			 * 
			 * }
			 */

			if (!StringUtil.isEmpty(farmerName)) {
				farmer.setFirstName(farmerName);
				filter.getFarm().setFarmer(farmer);
				filters.add(getLocaleProperty("farmerName") + " : " + farmer.getFirstName());
			}

			if (!StringUtil.isEmpty(fatherName)) {

				// farmer = farmerService.findFarmerByFarmerId(fatherName);
				// filter.getFarm().getFarmer().setFarmerId(fatherName);
				filters.add(getLocaleProperty("fatherName") + " : " + fatherName);

			}
			if (farmerCodeEnabled.equals("1")) {
				if (!StringUtil.isEmpty(farmerCode)) {
					// farmer =
					// farmerService.findFarmerByFarmerCode(farmerCode);
					// filter.getFarm().getFarmer().setFarmerCode(farmerCode);
					filters.add(getLocaleProperty("farmerCode") + " : "
							+ (!ObjectUtil.isEmpty(farmerCode) ? farmer.getFarmerCode() : getText("NA")));
				}
			}

			if (!StringUtil.isEmpty(villageName)) {
				filter.getFarm().getFarmer().setVillage(locationService.findVillageByCode(villageName));
				filters.add(getLocaleProperty("villageName") + " : " + (!ObjectUtil.isEmpty(villageName)
						? locationService.findVillageByCode(villageName).getName() : getText("NA")));
			}
			
			if (!StringUtil.isEmpty(samithiName)) {
				Warehouse W = new Warehouse();
				W.setName(samithiName);
				farmer.setSamithi(W);
				filter.getFarm().setFarmer(farmer);
				filters.add(getLocaleProperty("samithiName") + " : " + (!ObjectUtil.isEmpty(samithiName)
						? samithiName  : getText("NA")));
			}
			if (!StringUtil.isEmpty(seasonCode)) {
				HarvestSeason season = farmerService.findSeasonNameByCode(seasonCode);

				filters.add(getLocaleProperty("seasonCode") + " : "
						+ (!ObjectUtil.isEmpty(season) ? season.getName() : getText("NA")));
			}

			/*
			 * if (!StringUtil.isEmpty(branchIdParma)) {
			 * filter.getFarm().getFarmer().setBranchId(branchIdParma);
			 * filters.add( getLocaleProperty("BranchId") + " : " +
			 * branchesMap.get(filter.getFarm().getFarmer().getBranchId())); }
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

				filters.add(getLocaleProperty("mainOrganization") + " : " + getBranchesMap().get(getBranchIdParma()));

			}

			if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
				filter.setBranchId(subBranchIdParma);
				filters.add(getLocaleProperty("subOrganization") + " : " + getBranchesMap().get(getSubBranchIdParma()));
			}

		}

		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new FarmCrops();
		Village village = new Village();
		HarvestSeason season = new HarvestSeason();

		if (!StringUtil.isEmpty(farmerName)) {
			farmer.setFirstName(farmerName);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			farmer.setLastName(fatherName);
			filter.getFarm().setFarmer(farmer);
		}

		if (!StringUtil.isEmpty(farmerCode)) {
			farmer.setFarmerCode(farmerCode);
			filter.getFarm().setFarmer(farmer);

		}

		if (!StringUtil.isEmpty(villageName)) {
			village.setCode(villageName);
			farmer.setVillage(village);
			filter.getFarm().setFarmer(farmer);
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

		/*
		 * if (!StringUtil.isEmpty(seasonCode)) {
		 * farmer.setSeasonCode(seasonCode); filter.getFarm().setFarmer(farmer);
		 * }
		 */

		if (!StringUtil.isEmpty(seasonCode)) {
			season.setCode(seasonCode);
			filter.setCropSeason(season);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				State s = new State();
				s.setCode(stateName);
				farmer.setStateName(s.getCode());
				filter.getFarm().setFarmer(farmer);
				filters.add(getLocaleProperty("stateName") + " : " + (!ObjectUtil.isEmpty(stateName)
						? locationService.findStateByCode(stateName).getName() : getText("NA")));
			}
			if (!StringUtil.isEmpty(fpo)) {
				farmer.setFpo(fpo);
				filter.getFarm().setFarmer(farmer);
				filters.add(getLocaleProperty("fpo") + " : "
						+ (!ObjectUtil.isEmpty(fpo) ? getCatlogueValueByCode(fpo).getName() : getText("NA")));
			}
			if (!StringUtil.isEmpty(icsName)) {
				farmer.setIcsName(icsName);
				filter.getFarm().setFarmer(farmer);
				filters.add(getLocaleProperty("icsName") + " : "
						+ (!ObjectUtil.isEmpty(icsName) ? getCatlogueValueByCode(icsName).getName() : getText("NA")));
			}

		}
		/*
		 * else{ season.setCode(clientService.findCurrentSeasonCode());
		 * filter.setCropSeason(season); }
		 */
		String multiVal = getEnableMultiProduct();
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		Long serialNo = 0L;

		// **//

		for (Object[] obj : mainGridRows) {
			Object[] datas = (Object[]) obj;
			fields = new ArrayList<Object>();
			serialNo++;
			if (isMailExport() && flag) {
				fields.add((String.valueOf(serialNo)));

				/*
				 * if (StringUtil.isEmpty(branchIdValue)) {
				 * fields.add((!StringUtil.isEmpty(branchesMap.get(farmCrop.
				 * getBranchId())) ? branchesMap.get(farmCrop.getBranchId()) :
				 * getText("NA"))); }
				 */

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						fields.add(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[0])))
										? getBranchesMap().get(getParentBranchMap().get(datas[0]))
										: getBranchesMap().get(datas[0]));
					}
					fields.add(getBranchesMap().get(datas[0]));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						fields.add(!StringUtil.isEmpty(branchesMap.get(datas[0]))
								? (branchesMap.get(datas[0])) : "");
					}
				}

				/*
				 * HarvestSeason season =
				 * farmerService.findSeasonNameByCode(farmCrop.getFarm().
				 * getFarmer().getSeasonCode());
				 */

				fields.add((!ObjectUtil.isEmpty(datas[1]) ?datas[1]
						: getText("NA")));
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
					fields.add(
							(!ObjectUtil.isEmpty(datas[18]) ? datas[18]: getText("NA")));
				}

				/*
				 * if (farmerCodeEnabled.equals("1")) {
				 * fields.add((!ObjectUtil.isEmpty(farmCrop.getFarm().getFarmer(
				 * ).getFarmerCode()) ?
				 * farmCrop.getFarm().getFarmer().getFarmerCode() :
				 * getText("NA"))); }
				 */

				/*
				 * fields.add((!ObjectUtil.isEmpty(farmCrop.getFarm().getFarmer(
				 * ).getFirstName()) ?
				 * farmCrop.getFarm().getFarmer().getFirstName() + "/" +
				 * farmCrop.getFarm().getFarmer().getLastName() :
				 * getText("NA")));
				 */
				
				/*if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
					fields.add(!StringUtil.isEmpty(datas[2])
							? datas[2] : getText("NA"));
				}*/
				/*fields.add((!ObjectUtil.isEmpty(datas[3])
						? datas[3] : getText("NA")));*/
				
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					fields.add((!ObjectUtil.isEmpty(datas[2])
							? datas[2] : getText("NA")));}
				
				if (farmerCodeEnabled.equals("1") || getFarmerCodeEnabled().equalsIgnoreCase("1")) {
					fields.add((!ObjectUtil.isEmpty(datas[2])
							? datas[2] : getText("NA")));}
				
				
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					fields.add((!ObjectUtil.isEmpty(datas[3])
							? datas[3] : getText("NA")));}

				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
					if (getBranchId() != null && getBranchId().equals("organic")) {
						fields.add((!ObjectUtil.isEmpty(datas[4])
								? datas[4] : getText("NA")));
					}
				}

				fields.add((!ObjectUtil.isEmpty(datas[5])
						? datas[5] : getText("NA")));

				fields.add(!StringUtil.isEmpty(datas[6]) ? datas[6] : "");

				/*
				 * if (getEnableMultiProduct().equalsIgnoreCase("1")) { if
				 * (!ObjectUtil.isEmpty(farmCrop) &&
				 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety()) &&
				 * !ObjectUtil.isEmpty(farmCrop.getProcurementVariety().
				 * getProcurementProduct()) && !StringUtil
				 * .isEmpty(farmCrop.getProcurementVariety().
				 * getProcurementProduct().getName())) {
				 * fields.add(farmCrop.getProcurementVariety().
				 * getProcurementProduct().getName()); } else {
				 * fields.add(getText("NA")); } }
				 */

				if (!ObjectUtil.isEmpty(datas[7])) {

					if (multiVal.equalsIgnoreCase("1")) {
						if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
							if (!ObjectUtil.isEmpty(datas[8])) {
								String cropName = null;
								cropName = datas[8].toString();

								fields.add(cropName);

							}
							

						} else {
							fields.add(getText("NA"));
						}

						fields.add(!StringUtil.isEmpty(datas[27])
								? datas[27] : getText("NA"));

					}
				}
				

				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
					fields.add(!StringUtil.isEmpty(datas[24])
							? datas[24] : getText("NA"));
				}

				/*
				 * fields.add((!StringUtil.isEmpty(farmCrop.getFarm().
				 * getFarmDetailedInfo().getProposedPlantingArea()) ?
				 * farmCrop.getFarm().getFarmDetailedInfo().
				 * getProposedPlantingArea() : getText("NA")));
				 */
				fields.add(!StringUtil.isEmpty(datas[10]) ? datas[10] : "NA");
				if (!StringUtil.isEmpty(String.valueOf(datas[11]))) {
					//FarmCatalogue catalogue = getCatlogueValueByCode(String.valueOf(farmCrop.getType()));
					fields.add(!ObjectUtil.isEmpty(datas[11]) ?getCatlogueValueByCode(datas[11].toString()).getName() : "NA");
				} else {
					fields.add(getText("NA"));
				}
				/*
				 * fields.add((!ObjectUtil.isEmpty(farmCrop.getType()) ?
				 * String.valueOf(getText("type" + farmCrop.getType())) :
				 * getText("NA")));
				 */
				/*
				 * if (!StringUtil.isEmpty(getEnableMultiProduct())) { if
				 * (getEnableMultiProduct().equalsIgnoreCase("0")) {
				 * fields.add((!ObjectUtil.isEmpty(farmCrop.getStapleLength()) ?
				 * String.valueOf(farmCrop.getStapleLength()) : getText("NA")));
				 * 
				 * }
				 * 
				 * }
				 */

				if (multiVal.equalsIgnoreCase("0")) {
					if (datas[12] == null || isAlpha(datas[12].toString())
							|| StringUtil.isEmpty(datas[12])) {

						fields.add((!ObjectUtil.isEmpty(datas[12])
								? String.valueOf(datas[12]) : getText("NA")));

					} else {
						fields.add(!ObjectUtil.isEmpty(datas[12])
								&& StringUtil.isDouble(datas[12])
										? String.valueOf(CurrencyUtil
												.getDecimalFormat(Double.valueOf(datas[12].toString()), "##.00"))
										: getText("NA"));

					}
				}

				if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.INTERCROP.ordinal()
						&& !ObjectUtil.isEmpty(datas[24])
						&& !StringUtil.isEmpty(datas[8])) {
					fields.add(datas[8]);

				} else {
					fields.add(getText("NA"));
				}
				
				
				if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()
						&& !ObjectUtil.isEmpty(datas[24]) && !StringUtil.isEmpty(datas[8])) {
					fields.add(datas[8]);
				} /*else {
					fields.add(getText("NA"));
				}
				*/
				

				if (getCurrentTenantId().equals("pratibha")) {

					if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.BORDERCROP.ordinal()
							&& !ObjectUtil.isEmpty(datas[24]) && !StringUtil
									.isEmpty(datas[8])) {
						fields.add(datas[8]);
					} else {
						fields.add(getText("NA"));
					}

					if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.COVERCROP.ordinal()
							&& !ObjectUtil.isEmpty(datas[24]) && !StringUtil
									.isEmpty(datas[8])) {
						fields.add(datas[8]);
					} else {
						fields.add(getText("NA"));
					}

					if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()
							&& !ObjectUtil.isEmpty(datas[24]) && !StringUtil.isEmpty(datas[8])) {
						fields.add(datas[8]);
					} else {
						fields.add(getText("NA"));
					}

					if (Integer.valueOf(datas[7].toString()) == FarmCrops.CROPTYPE.TRAPCROP.ordinal()
							&& !ObjectUtil.isEmpty(datas[24]) && !StringUtil.isEmpty(datas[8])) {
						fields.add(datas[8]);
					} else {
						fields.add(getText("NA"));
					}

				}

				if (!getCurrentTenantId().equals("chetna") && !getCurrentTenantId().equals("iccoa")
						&& !getCurrentTenantId().equals("welspun") && !getCurrentTenantId().equals("pratibha")) {
					fields.add(!ObjectUtil.isEmpty(datas[13])
							? String.valueOf(CurrencyUtil.getDecimalFormat(Double.valueOf(datas[13].toString()), "##.00"))
							: getText("NA"));
					

				}
				if (getCurrentTenantId().equals("pratibha")) {
					fields.add(!ObjectUtil.isEmpty(datas[13]) ? (Integer.valueOf(datas[13].toString()) / 100)
							: getText("N/A"));
				}
				if (getCurrentTenantId().equals("iccoa")) {
					fields.add(!ObjectUtil.isEmpty(datas[13]) ? (Integer.valueOf(datas[13].toString()) / 1000)
							: getText("NA"));
				}

				/*
				 * Double actualCottonSeedHarvested = null; Double
				 * actualLintCottonHarvested = null;
				 * 
				 * List<CropHarvest> cropHarvests = farmerService
				 * .findCropHarvestByFarmCode(farmCrop.getFarm().getFarmCode());
				 * for (CropHarvest cropHarvest : cropHarvests) {
				 * List<CropHarvestDetails> cropHarvestDetails = new
				 * ArrayList();
				 * cropHarvestDetails.addAll(cropHarvest.getCropHarvestDetails()
				 * ); for (CropHarvestDetails cropHarvestDetail :
				 * cropHarvestDetails) { if
				 * (cropHarvestDetail.getCrop().getCode() ==
				 * farmCrop.getCropCode()) { actualCottonSeedHarvested = Double
				 * .parseDouble(cropHarvestDetail.getCropHarvest().getTotalQty()
				 * ); actualLintCottonHarvested = (actualCottonSeedHarvested *
				 * 34) / 100; } } }
				 */

				Double actualCottonSeedHarvested = null;
				Double actualLintCottonHarvested = null;
				Double actualHarvested = 0.0;
				if (multiVal.equalsIgnoreCase("1")) {
					List<CropHarvest> cropHarvests = farmerService
							.findCropHarvestByFarmCode(datas[14].toString());
					for (CropHarvest cropHarvest : cropHarvests) {
						List<CropHarvestDetails> cropHarvestDetails = new ArrayList();
						cropHarvestDetails.addAll(cropHarvest.getCropHarvestDetails());
						for (CropHarvestDetails cropHarvestDetail : cropHarvestDetails) {
							if (cropHarvestDetail.getCrop().getCode().equalsIgnoreCase(datas[16].toString())) {
								actualCottonSeedHarvested = Double
										.parseDouble(cropHarvestDetail.getCropHarvest().getTotalQty());
								actualHarvested = actualHarvested + cropHarvestDetail.getQty();
								actualLintCottonHarvested = (actualCottonSeedHarvested * 34) / 100;
							}
						}
					}
				}
				Double yieldPerAcre = null;
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
					yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested)
							&& !StringUtil.isEmpty(datas[15].toString())
									? actualHarvested / Double.parseDouble(
											datas[15].toString())
									: null;
				} else {
					if (datas[10].toString().equalsIgnoreCase("0")) {
						yieldPerAcre = (double) 0;
					} else {
						yieldPerAcre = !ObjectUtil.isEmpty(actualHarvested)
								&& !StringUtil.isEmpty(datas[10].toString())
										? actualHarvested / Double.parseDouble(datas[10].toString()) : null;
					}
				}
				if (!getCurrentTenantId().equals("welspun") && !getCurrentTenantId().equalsIgnoreCase("gsma")) {
					if (!ObjectUtil.isEmpty(actualHarvested)) {
						fields.add(String.valueOf(actualHarvested));

					} else {
						fields.add(getText("NA"));
					}
				}
				if (multiVal.equalsIgnoreCase("0")) {
					if (!getCurrentTenantId().equals("chetna")) {

						if (!ObjectUtil.isEmpty(actualLintCottonHarvested)) {
							fields.add(String.valueOf(actualLintCottonHarvested));

						} else {
							fields.add(getText("NA"));
						}
					}
				}
				/*
				 * if (!ObjectUtil.isEmpty(actualLintCottonHarvested)) {
				 * fields.add(String.valueOf(actualLintCottonHarvested)); } else
				 * { fields.add(getText("NA")); }
				 */
				if (!getCurrentTenantId().equals("iccoa") && !getCurrentTenantId().equals("welspun")
						&& !getCurrentTenantId().equalsIgnoreCase("gsma")) {
					if (!ObjectUtil.isEmpty(yieldPerAcre) && !String.valueOf(yieldPerAcre).equalsIgnoreCase("Infinity") ) {
						fields.add(String.valueOf(CurrencyUtil.getDecimalFormat(yieldPerAcre, "##.00")));
					} else {
						fields.add(getText("0.0"));
					}
				}
			}
			entityObject.add(fields);
		}

		InputStream is = getPDFExportStreamData(entityObject, filters);
		setPdfFileName(getText("FarmerCropListFile") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("FarmerCropListFile"), fileMap, ".pdf"));
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
		String fileName = getText("FarmerCropListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);

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
		title = new Paragraph(new Phrase(getText("FarmerCropExportPDFTitle"), titleFont));
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
		 * (getCurrentTenantId().equals("chetna")) { columnHeaders =
		 * getLocaleProperty("ExportHeadingNoBranchChetna"); } else if
		 * (getCurrentTenantId().equals("mcash")) { columnHeaders =
		 * getText("ExportHeadingNoBranchMcash"); } else if
		 * (!StringUtil.isEmpty(getBranchId()) && farmerCodeEnabled.equals("1"))
		 * { columnHeaders = getLocaleProperty("exportColumnHeaderValues");
		 * }else if(getCurrentTenantId().equals("meridian")){ columnHeaders =
		 * getText("exportColumnHeadermeridian"); } else { columnHeaders =
		 * getText("exportColumnHeader"); } } if
		 * (getCurrentTenantId().equals("crsdemo")) { columnHeaders =
		 * getText("exportColumnHeadercrsfarmercrop"); }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportFarmerCropColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportFarmerCropColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderBranch");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders = getLocaleProperty("OrganicFarmerCropExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders = getLocaleProperty("BCIFarmerCropExportHeader");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue) && farmerCodeEnabled.equals("1")) {
				columnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderBranch");
			} else if (!StringUtil.isEmpty(branchIdValue) && farmerCodeEnabled.equals("1")) {
				columnHeaders = getLocaleProperty("exportFarmerCropColumnHeader");
			}
			else {
				if (StringUtil.isEmpty(branchIdValue)&& farmerCodeEnabled.equals("0")) {
					columnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderWithoutFarmerCodeBrn");
				} else {
					columnHeaders = getLocaleProperty("exportFarmerCropColumnHeaderWithoutFarmerCode");
				}
			}
		}

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			PdfPCell cell = null; // cell for table.
			cellFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
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
				cell.setBackgroundColor(new BaseColor(204,255,204));
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
					try
					{
						 Float.parseFloat(entityField.toString()); 
						 cell = new PdfPCell(new Phrase(
									StringUtil.isEmpty(entityField) ? getText("NA") : entityField.toString(), cellFont));
						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);						
					}
					
					// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
					catch  (NumberFormatException e){
					cell = new PdfPCell(new Phrase(
							StringUtil.isEmpty(entityField) ? getText("NA") : entityField.toString(), cellFont));
					table.addCell(cell);
					// END OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
					}
				}
			}

			document.add(table); // Add table to document.
		}
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
	}

	public String getEnableMultiProduct() {
		enableMultiProduct = preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
		return enableMultiProduct;
	}

	public void setEnableMultiProduct(String enableMultiProduct) {
		this.enableMultiProduct = enableMultiProduct;
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

	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public String getFarmerCodeEnabled() {
		return farmerCodeEnabled;
	}

	public void setFarmerCodeEnabled(String farmerCodeEnabled) {
		this.farmerCodeEnabled = farmerCodeEnabled;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public Map<String, String> getFields() {
		/**
		 * fields.add(getText("date")); fields.add(getText("farmerName"));
		 * fields.add(getLocaleProperty("fatherName")); if
		 * (!StringUtil.isEmpty(farmerCodeEnabled)&&
		 * farmerCodeEnabled.equalsIgnoreCase("1")) {
		 * fields.add(getText("farmerCode")); } fields.add(getText("village"));
		 * if (ObjectUtil.isEmpty(getBranchId())) {
		 * fields.add(getText("app.branch")); }
		 */

		// fields.put("1", getText("date"));
		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			fields.put("6", getText("farmerCode"));
		}
		fields.put("2", getText("farmerName"));
		fields.put("3", getText("fatherName"));
		fields.put("4", getText("village"));

		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { fields.put("7",
		 * getText("app.branch")); } else if (StringUtil.isEmpty(getBranchId()))
		 * { fields.put("5", getText("app.branch")); }
		 */

		if (getIsMultiBranch().equalsIgnoreCase("1")) {
			if (StringUtil.isEmpty(getBranchId())) {
				fields.put("7", getText("app.branch"));
			} else if (getIsParentBranch().equals("1")) {
				fields.put("7", getText("app.subBranch"));
			}

		} else if (StringUtil.isEmpty(getBranchId())) {

			fields.put("5", getText("app.branch"));
		}

		fields.put("8", getText("seasonCode"));
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			fields.put("9", getLocaleProperty("state.name"));

			fields.put("10", getLocaleProperty("cooperative"));

			fields.put("11", getLocaleProperty("icsName"));
		}
		return fields;
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();

		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
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

		warehouseMap = getFarmCatalougeMap(Integer.valueOf(getText("cooperativeType")));

		warehouseMap.put("99", "Others");

		return warehouseMap;

	}

	public Map<String, String> getIcsNameList() {
		Map<String, String> icsMap = new LinkedHashMap<>();

		icsMap = getFarmCatalougeMap(Integer.valueOf(getText("icsNameType")));

		icsMap.put("99", "Others");

		return icsMap;

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

	/*
	 * public List<String[]> getFarmerFarmerIdList() {
	 * 
	 * List<String[]> farmerIdList = farmerService.listFarmerId();
	 * 
	 * return farmerIdList;
	 * 
	 * }
	 */
	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
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

	public Map<String, String> getFarmerFarmerIdList() {
		return farmerFarmerIdList;
	}

	public void setFarmerFarmerIdList(Map<String, String> farmerFarmerIdList) {
		this.farmerFarmerIdList = farmerFarmerIdList;
	}

	public Map<String, String> getFatherNameList() {
		return fatherNameList;
	}

	public void setFatherNameList(Map<String, String> fatherNameList) {
		this.fatherNameList = fatherNameList;
	}

	public Map<String, String> getVillageMap() {
		return villageMap;
	}

	public void setVillageMap(Map<String, String> villageMap) {
		this.villageMap = villageMap;
	}

	public Map<String, String> getVillageList() {

		Map<String, String> villageListMap = new LinkedHashMap<String, String>();
		List<Village> villageList = locationService.listVillage();
		for (Village obj : villageList) {
			villageListMap.put(obj.getCode(), obj.getName());
		}
		return villageListMap;
	}

	public List<String> getSamithiNameList() {
		List<String> samithiMap = new ArrayList<>();
		samithiMap = locationService.listOfGroup().stream().map(s -> String.valueOf(s[1])).collect(Collectors.toList());
		return samithiMap;
	}

	public String getSamithiName() {
		return samithiName;
	}

	public void setSamithiName(String samithiName) {
		this.samithiName = samithiName;
	}

}
