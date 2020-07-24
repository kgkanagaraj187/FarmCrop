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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
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
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
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

public class CBRReportAction extends BaseReportAction implements IExporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IFarmerService farmerService;
	protected Map<String, String> fields = new HashMap<>();
	private ILocationService locationService;
	private Farm filter;
	private String villageName;
	private String farmerCode;
	private String firstName;
	private Farmer farmer;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private IProductDistributionService productDistributionService;
	private String farmerCodeEnabled;
	private static int totalFarmer;
	private static String totalFarmerProfit;
	List<String> farmerCountList = new ArrayList<>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private IClientService clientService;
	private String branchIdParam; // Branch Id POST from JQGrid of jsp page
									// while filtering is stored in this
									// variable.
	private String icsName;
	private String samithiName;
	private String farmName;
	private String fatherName;
	private String seasonCode;
	private String exportLimit;
	private double totprofit;
	private String stateName;
	private String fpo;
	
	private Map<Integer, String> icsTypeList = new LinkedHashMap<Integer, String>();

	Map<String, String> seasonMap = new LinkedHashMap();

	List<Cultivation> cultivationList = new ArrayList<>();
	List<CropHarvest> cropHarvestList = new ArrayList<>();
	private String farmerId;
	Map<String, String> farmerCodList = new LinkedHashMap<>();
	Map<String, String> farmerNameList = new LinkedHashMap<>();
	Map<String, String> farmerFarmerIdList = new LinkedHashMap<>();
	Map<String, String> fatherNameList = new LinkedHashMap<>();

	Map<String, String> villageMap = new LinkedHashMap<>();

	@Autowired
	private ICatalogueService catalogueService;

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

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	@Autowired
	private IPreferencesService preferncesService;

	public String list() throws Exception {

		Calendar currentDate = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df
				.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH)));
		super.endDate = df.format(currentDate.getTime());
		List<Object[]> farmerList = farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.stream().forEach(obj -> {

				if (!villageMap.containsKey(obj[11].toString())) {
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

				if (!fatherNameList.containsKey(obj[4]) && obj[4] != null) {
					fatherNameList.put(obj[4].toString(), obj[4].toString());

				}
			});
		}
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}
		// fields.add(getText("date"));

		filter = new Farm();
		// request.setAttribute(HEADING, getText(LIST));
		return LIST;
	}

	public String data() throws Exception {

		setFilter(new Farm());
		Farmer f = new Farmer();

		if (!StringUtil.isEmpty(seasonCode)) {
			Farmer farmerSeason = new Farmer();
			farmerSeason.setSeasonCode(seasonCode);
			filter.setFarmer(farmerSeason);
		} /*
			 * else{ Farmer farmer2=new Farmer();
			 * farmer2.setSeasonCode(clientService.findCurrentSeasonCode());
			 * filter.setFarmer(farmer2); }
			 */

		if (!StringUtil.isEmpty(farmerCode)) {
			f.setFarmerId(farmerCode);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(firstName)) {
			f.setFirstName(firstName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			f.setLastName(fatherName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(villageName)) {
			Village v = new Village();
			v.setCode(villageName);
			f.setVillage(v);
			filter.setFarmer(f);

		}

		if (!StringUtil.isEmpty(branchIdParam)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParam);
				f.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParam);
				branchList.add(branchIdParam);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				f.setBranchesList(branchList);
			}
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			f.setBranchId(subBranchIdParma);
			filter.setFarmer(f);
		}

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
				|| getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
			if (StringUtil.isEmpty(getBranchId()) || !getBranchId().equalsIgnoreCase("bci")) {
				if (!StringUtil.isEmpty(icsName)) {
					f.setIcsName(icsName);
					filter.setFarmer(f);
				}
			}
		}
		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse W = new Warehouse();
			W.setName(samithiName);
			f.setSamithi(W);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(farmName)) {
			filter.setFarmName(farmName);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				State s = new State();
				s.setCode(stateName);
				f.setStateName(s.getCode());
				;
				filter.setFarmer(f);
			}
			if (!StringUtil.isEmpty(fpo)) {
				f.setFpo(fpo);
				filter.setFarmer(f);
			}

		}
		if (!StringUtil.isEmpty(farmerId)) {
			f.setFarmerId(farmerId);
			filter.setFarmer(f);
		}
		super.filter = filter;
		Map data = readData("cBRReportActionProp");

		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		DecimalFormat format = new DecimalFormat("0.00");
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		Object[] farm = (Object[]) obj;
		
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(!StringUtil.isEmpty(farm[8]) ? (branchesMap.get(farm[8])) : "");
		}
		if (!ObjectUtil.isEmpty(farm[9]) && !StringUtil.isEmpty(farm[9])) {
			rows.add(getSeasonMap().containsKey(farm[9]) ? getSeasonMap().get(farm[9]) : "");
		} else {
			rows.add("");
		}
		
		
		String firstName =  String.valueOf(farm[2]);
		String farmerId = String.valueOf(farm[1]);
		String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
		rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
		//rows.add((!StringUtil.isEmpty(farm[2]) ? farm[2].toString() : "N/A"));

		rows.add((!StringUtil.isEmpty(farm[4]) ? farm[4].toString() : "N/A"));

		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
			if (getBranchId() == null || !getBranchId().equalsIgnoreCase("bci"))

				rows.add((!StringUtil.isEmpty(farm[10]) ? farm[10].toString() : "N/A"));
		}

		rows.add((!StringUtil.isEmpty(farm[5]) ? farm[5].toString() : "N/A"));

		rows.add((!StringUtil.isEmpty(farm[6]) ? farm[6].toString() : "N/A"));
		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			//rows.add((!StringUtil.isEmpty(farm[7]) ? farm[7].toString() : ""));
			rows.add(!StringUtil.isEmpty(farm[7].toString()) ? Double.valueOf(farm[7].toString()) : "0.00");
			//rows.add(CurrencyUtil.getDecimalFormat( Double.valueOf(farm[7].toString()), "##.00"));
		}
		/*
		 * incomeCotton = new Double(0); if
		 * (!ObjectUtil.isListEmpty(getCropHarvestList())) {
		 * getCropHarvestList().stream().filter(croHarvest ->
		 * croHarvest.getFarmerId().equals(farm[11].toString()) &&
		 * croHarvest.getFarmCode().equals(farm[3].toString())).forEach(
		 * cropHarvest -> {
		 * 
		 * cropHarvest.getCropHarvestDetails().forEach(chDetails -> {
		 * incomeCotton += chDetails.getSubTotal(); }); });
		 * 
		 * rows.add(format.format(incomeCotton)); } else {
		 * 
		 * rows.add(incomeCotton); }
		 */

		if (!ObjectUtil.isListEmpty(getCultivationList())) {
			totalCoc = new Double(0);
			interCropIncome = new Double(0);
			agriIncome = new Double(0);
			otherSourcesIncome = new Double(0);
			cottonQty = new Double(0);
			unitSalePrice = new Double(0);
			saleCottonIncome = new Double(0);
			getCultivationList().stream()
					.filter(cultivation -> (cultivation.getFarmerId() != null
							&& cultivation.getFarmerId().equals(farm[11].toString()))
							&& (cultivation.getFarmId() != null && cultivation.getFarmId().equals(farm[3].toString()))
							&& (cultivation.getCurrentSeasonCode() != null
									&& cultivation.getCurrentSeasonCode().equals(String.valueOf(farm[9]))))
					.forEach(cultivation -> {
						totalCoc = (totalCoc + (StringUtil.isDouble(cultivation.getTotalCoc())
								? Double.valueOf(cultivation.getTotalCoc()) : 0D));
						interCropIncome = (interCropIncome + (StringUtil.isDouble(cultivation.getInterCropIncome())
								? Double.valueOf(cultivation.getInterCropIncome()) : 0D));
						agriIncome = (agriIncome + (StringUtil.isDouble(cultivation.getAgriIncome())
								? Double.valueOf(cultivation.getAgriIncome()) : 0D));
						otherSourcesIncome = (otherSourcesIncome
								+ (StringUtil.isDouble(cultivation.getOtherSourcesIncome())
										? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0D));
						cottonQty = (cottonQty + (StringUtil.isDouble(cultivation.getCottonQty())
								? Double.valueOf(cultivation.getCottonQty()) : 0D));
						unitSalePrice = (unitSalePrice + (StringUtil.isDouble(cultivation.getUnitSalePrice())
								? Double.valueOf(cultivation.getUnitSalePrice()) : 0D));
						saleCottonIncome = (saleCottonIncome + (StringUtil.isDouble(cultivation.getSaleCottonIncome())
								? Double.valueOf(cultivation.getSaleCottonIncome()) : 0D));
					});
			if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
			rows.add(((!ObjectUtil.isEmpty(agriIncome) ? format.format(agriIncome).toString() : "")));

			rows.add(((!ObjectUtil.isEmpty(interCropIncome) ? format.format(interCropIncome).toString() : "")));
			}
			double trap=0;
			double plant=0;
			double cover=0;
			double border=0;
			double inter=0;
			double main=0;
			if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
				List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(farm[3].toString());
				for (CropSupply cropSupply : cropSup) {
					List<CropSupplyDetails> cropSupplyDetails = new ArrayList();
					cropSupplyDetails.addAll(cropSupply.getCropSupplyDetails());
					for (CropSupplyDetails cropSupplyDetail : cropSupplyDetails) {
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
							main=main+cropSupplyDetail.getSubTotal();
						} 
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.INTERCROP.ordinal()) {
							inter=inter+cropSupplyDetail.getSubTotal();
						} 
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.BORDERCROP.ordinal()) {
							border=border+cropSupplyDetail.getSubTotal();
						} 
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.COVERCROP.ordinal()) {
							cover=cover+cropSupplyDetail.getSubTotal();
						}
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()) {
							plant=plant+cropSupplyDetail.getSubTotal();
						}
						if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.TRAPCROP.ordinal()) {
							trap=trap+cropSupplyDetail.getSubTotal();
							
						} 
					}
					
					
				}
				rows.add(main);
				rows.add(inter);
				rows.add(border);
				rows.add(cover);
				rows.add(plant);
				rows.add(trap);
				rows.add(((!ObjectUtil.isEmpty(otherSourcesIncome) ? format.format(otherSourcesIncome).toString() : "")));
				
			}
			double grossIncome = 0.0;
			Double totalProfit = 0.0;
			grossIncome += !ObjectUtil.isEmpty(interCropIncome) ? Double.valueOf((interCropIncome).toString()) : 0;
			grossIncome += !ObjectUtil.isEmpty(agriIncome) ? Double.valueOf(agriIncome.toString()) : 0;
			if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
				rows.add(CurrencyUtil.getDecimalFormat(otherSourcesIncome+main+inter+border+cover+plant+trap, "##.00"));
			}
			else{
			rows.add(((!ObjectUtil.isEmpty(format.format(grossIncome)) ? format.format(grossIncome) : "")));
			}
			rows.add(((!ObjectUtil.isEmpty(totalCoc) ? format.format(totalCoc) : "")));
			if (ObjectUtil.isEmpty(totalCoc)) {
				totalCoc = 0D;

			}
			if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
				
				totalProfit = Double.valueOf(CurrencyUtil.getDecimalFormat(otherSourcesIncome+main+inter+border+cover+plant+trap, "##.00")) - totalCoc;
			}
			else{
			if (grossIncome > 0.0 && !StringUtil.isEmpty(totalCoc)) {
				totalProfit = Double.valueOf(grossIncome) - totalCoc;
			}
			}

			rows.add(CurrencyUtil.getDecimalFormat(totalProfit, "##.00"));

			double profitPerAcre = 0.0;
			if (StringUtil.isEmpty(farm[7])) {
				farm[7] = "0";
			}

			if (StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))) {
				farm[7] = "0";
			}

			if (getCurrentTenantId().equalsIgnoreCase("pratibha") ) {
				if ( getBranchId()==null || !getBranchId().equalsIgnoreCase(ESESystem.PRATIBHA_ORGANIC_BRANCH_ID)){
				if (totalProfit > 0.0
						&& !StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))
						&& Double.parseDouble(farm[7].toString()) > 0.0) {
					profitPerAcre = totalProfit / Double.valueOf(farm[7].toString());

					rows.add(((!ObjectUtil.isEmpty(profitPerAcre) ? format.format(profitPerAcre).toString() : "0.00")));
				} else {
					rows.add("0.00");
				}
				}
			}

		}
		jsonObject.put("id", farm[0].toString());
		jsonObject.put("cell", rows);
		return jsonObject;

	}

	/**
	 * Gets the farmerCode list.
	 * 
	 * @return the farmerCode list
	 */
	/*
	 * public List<String[]> getFarmerCodeList() {
	 * 
	 * List<String[]> farmerCodeList = farmerService.listFarmerCode();
	 * 
	 * return farmerCodeList;
	 * 
	 * }
	 * 
	 *//**
		 * Gets the farmerName list.
		 * 
		 * @return the farmerName list
		 *//*
		 * public List<String[]> getFarmerNameList() {
		 * 
		 * List<String[]> farmerNameList = farmerService.listFarmerName();
		 * 
		 * return farmerNameList;
		 * 
		 * }
		 */

	public Map<String, String> getIcsList() {

		Map<String, String> icsList = new LinkedHashMap<String, String>();

		List<Object[]> ics = catalogueService.loadICSName();
		for (Object[] obj : ics) {
			icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return icsList;

	}

	public List<String> getSamithiNameList() {
		List<String> samithiMap = new ArrayList<>();
		samithiMap = locationService.listOfGroup().stream().map(s -> String.valueOf(s[1])).collect(Collectors.toList());
		return samithiMap;
	}

	/*
	 * public Map<String, String> getSamithiNameList() {
	 * 
	 * Map<String, String> samithiMap = new LinkedHashMap<>(); samithi =
	 * locationService.listSamithiesBasedOnType(); samithiMap =
	 * samithi.stream().collect(Collectors.toMap(obj ->
	 * String.valueOf(obj.getCode()), Warehouse::getName)); return samithiMap;
	 * 
	 * }
	 */

	public Map<String, String> getFarmNameList() {
		Map<String, String> farmListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmList = farmerService.listFarmInfo();
		farmList.stream().forEach(farm -> {
			if (!StringUtil.isEmpty(farm[2]) && !farmListMap.containsKey(farm[2])) {
				farmListMap.put(farm[2].toString(), farm[2].toString());
			}
		});
		return farmListMap;
	}

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 *//*
		 * public Map<String, String> getVillageList() {
		 * 
		 * Map<String, String> villageMap = new LinkedHashMap<String, String>();
		 * 
		 * List<Village> villageList = locationService.listVillage(); for
		 * (Village obj : villageList) { villageMap.put(obj.getCode(),
		 * obj.getName()); // villageMap.put(obj.getCode(), obj.getName() +
		 * " - " + // obj.getCode()); } return villageMap;
		 * 
		 * }
		 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public Farm getFilter() {

		return filter;
	}

	public void setFilter(Farm filter) {

		this.filter = filter;
	}

	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	public Farmer getFarmer() {

		return farmer;
	}

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

	public String getFirstName() {

		return firstName;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public String getXlsFileName() {

		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {

		this.xlsFileName = xlsFileName;
	}

	Double incomeCotton = new Double(0);
	Double totalCoc = new Double(0);
	Double interCropIncome = new Double(0);
	Double agriIncome = new Double(0);
	Double otherSourcesIncome = new Double(0);
	Double cottonQty = new Double(0);
	Double unitSalePrice = new Double(0);
	Double saleCottonIncome = new Double(0);

	@SuppressWarnings("unchecked")
	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);

		DecimalFormat format = new DecimalFormat("0.00");
		/*
		 * setsDate(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ?
		 * DateUtil.getDateWithoutTime(new Date()) :
		 * DateUtil.convertStringToDate( DateUtil.minusWeek(df.format(new
		 * Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
		 * DateUtil.DATABASE_DATE_FORMAT));
		 */
		boolean flag = true;

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportCBRTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		
		
	/*	style2.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	/*	style3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style3.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style3.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style3.setTopBorderColor(IndexedColors.BLACK.getIndex());*/

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
				filterRow6 = null, filterRow7 = null, filterRow8 = null, filterRow9 = null, filterRow10 = null,
				filterRow11 = null, filterRow12 = null;
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportCBRTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if(!StringUtil.isEmpty(firstName) || !StringUtil.isEmpty(fatherName) || !StringUtil.isEmpty(farmerCode) || 
					!StringUtil.isEmpty(samithiName) || !StringUtil.isEmpty(villageName) || !StringUtil.isEmpty(seasonCode) ||
					!StringUtil.isEmpty(farmName) || !StringUtil.isEmpty(stateName) || !StringUtil.isEmpty(icsName) || 
					!StringUtil.isEmpty(fpo) )
			{
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(firstName)) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(firstName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(fatherName)) {
				filterRow1 = sheet.createRow(rowNum++);

				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fatherName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				cell.setCellValue(new HSSFRichTextString(fatherName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(farmerCode)) {

				filterRow2 = sheet.createRow(rowNum++);

				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmerCode));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(samithiName)) {
				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("samithiName")));

				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(samithiName));

				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				/*
				 * cell = filterRow3.createCell(2); cell.setCellValue(new
				 * HSSFRichTextString((!ObjectUtil.isEmpty(samithiName) ?
				 * locationService.findVillageByCode(samithiName).getName() :
				 * getText("NA")))); filterFont.setBoldweight((short) 12);
				 * filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				 * filterStyle.setFont(filterFont);
				 * cell.setCellStyle(filterStyle);
				 */
			}

			if (!StringUtil.isEmpty(villageName)) {
				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(villageName)
						? locationService.findVillageByCode(villageName).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

			if (!StringUtil.isEmpty(seasonCode)) {

				filterRow2 = sheet.createRow(rowNum++);

				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				HarvestSeason seasonObj = farmerService.findHarvestSeasonByCode(seasonCode);
				if (!ObjectUtil.isEmpty(seasonObj))
					cell.setCellValue(seasonObj.getName());

				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (!StringUtil.isEmpty(farmName)) {
				filterRow7 = sheet.createRow(rowNum++);

				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

		}
		if (getCurrentTenantId().equals("chetna")) {

			if (!StringUtil.isEmpty(stateName)) {
				filterRow10 = sheet.createRow(rowNum++);
				cell = filterRow10.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("stateName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow10.createCell(2);
				State s = locationService.findStateByCode(stateName);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(s) ? s.getName() : "")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}
			if (!StringUtil.isEmpty(fpo)) {
				filterRow11 = sheet.createRow(rowNum++);
				cell = filterRow11.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("fpo")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow11.createCell(2);
				FarmCatalogue fc = getCatlogueValueByCode(fpo);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(fc) ? fc.getName() : ""));
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

		}

		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		mainGridRowHead.setHeight((short) 400);

		/*
		 * String mainGridColumnHeaders = ""; if
		 * (StringUtil.isEmpty(branchIdValue)) { mainGridColumnHeaders =
		 * getLocaleProperty("exportColumnHeaderBranchCBR"); } else {
		 * mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderCBR"); }
		 */

		String mainGridColumnHeaders = getLocaleProperty("exportColumnHeaderBranchCBR");
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportCbrColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportCbrColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCbrColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				mainGridColumnHeaders = getLocaleProperty("OrganicCbrExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				mainGridColumnHeaders = getLocaleProperty("BCICbrExportHeader");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportCbrColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportCbrColumnHeader");
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
			this.filter = new Farm();
		Farmer f = new Farmer();

		if (!StringUtil.isEmpty(seasonCode)) {
			Farmer farmerSeason = new Farmer();
			farmerSeason.setSeasonCode(seasonCode);
			filter.setFarmer(farmerSeason);
		} /*
			 * else{ Farmer farmerSeason=new Farmer();
			 * farmerSeason.setSeasonCode(clientService.findCurrentSeasonCode())
			 * ; filter.setFarmer(farmerSeason); }
			 */

		if (!StringUtil.isEmpty(farmerCode)) {
			f.setFarmerId(farmerCode);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(firstName)) {
			f.setFirstName(firstName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			f.setLastName(fatherName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(villageName)) {
			Village v = new Village();
			v.setCode(villageName);
			f.setVillage(v);
			filter.setFarmer(f);

		}

		if (!StringUtil.isEmpty(branchIdParam)) { // set filter of branch id and
													// check it.
			f.setBranchId(branchIdParam);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(icsName)) {
			f.setIcsName(icsName);
			filter.setFarmer(f);
		}
		if (!StringUtil.isEmpty(fpo)) {
			f.setFpo(fpo);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse W = new Warehouse();
			W.setName(samithiName);
			f.setSamithi(W);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(farmName)) {
			filter.setFarmName(farmName);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			f.setFarmerId(farmerId);
			filter.setFarmer(f);
		}
		super.filter = this.filter;
		Map dataz = readData("cBRReportActionProp");
		List<Object[]> list = (List) dataz.get(ROWS);

		if (ObjectUtil.isListEmpty(list))
			return null;

		// 0=Id,1=Farmer Id,2=Farmer name,3=Farm code,4=Farm Name,5=Group
		// Name,6=Village Name,7=Propose planting Area,8=BranchId,10=ICS Name

		Long serialNumber = 0L;

		for (Object[] farm : list) {
			flag = false;
			row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			colNum = 0;

			serialNumber++;
			cell = row.createCell(colNum++);
			cell.setCellValue(
					new HSSFRichTextString(String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style4);

			if (StringUtil.isEmpty(branchIdValue)) {
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(!StringUtil.isEmpty(farm[8]) ? (branchesMap.get(farm[8])) : "N/A"));
				cell.setCellStyle(style3);
			}

			cell = row.createCell(colNum++);
			if (!ObjectUtil.isEmpty(farm[9]) && !StringUtil.isEmpty(farm[9])) {
				cell.setCellValue(getSeasonMap().containsKey(farm[9]) ? getSeasonMap().get(farm[9]) : getText("N/A"));
				cell.setCellStyle(style3);
			}
			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(farm[2]) ? farm[2].toString() : "N/A"));

			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(farm[4]) ? farm[4].toString() : "N/A"));

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (getBranchId() == null || !getBranchId().equalsIgnoreCase("bci")) {
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue((!StringUtil.isEmpty(farm[10]) ? farm[10].toString() : "N/A"));
				}
			}

			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(farm[5]) ? farm[5].toString() : "N/A"));

			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(farm[6]) ? farm[6].toString() : "N/A"));
			if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue((!StringUtil.isEmpty(farm[7]) ? farm[7].toString() : ""));
				style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			}
			/*
			 * incomeCotton = new Double(0); if
			 * (!ObjectUtil.isListEmpty(getCropHarvestList())) {
			 * getCropHarvestList().stream().filter(croHarvest ->
			 * croHarvest.getFarmerId().equals(farm[11].toString()) &&
			 * croHarvest.getFarmCode().equals(farm[3].toString())).forEach(
			 * cropHarvest -> {
			 * 
			 * cropHarvest.getCropHarvestDetails().forEach(chDetails -> {
			 * incomeCotton += chDetails.getSubTotal(); }); }); cell =
			 * row.createCell(colNum++); cell.setCellStyle(style3);
			 * cell.setCellValue(format.format(incomeCotton)); } else { cell =
			 * row.createCell(colNum++); cell.setCellStyle(style3);
			 * cell.setCellValue(incomeCotton); }
			 */

			if (!ObjectUtil.isListEmpty(getCultivationList())) {
				totalCoc = new Double(0);
				interCropIncome = new Double(0);
				agriIncome = new Double(0);
				otherSourcesIncome = new Double(0);
				cottonQty = new Double(0);
				unitSalePrice = new Double(0);
				saleCottonIncome = new Double(0);
				double trap=0;
				double plant=0;
				double cover=0;
				double border=0;
				double inter=0;
				double main=0;
				getCultivationList().stream().filter(cultivation -> (cultivation.getFarmerId() != null
						&& cultivation.getFarmerId().equals(farm[11].toString()))
						&& (cultivation.getFarmId() != null && cultivation.getFarmId().equals(farm[3].toString()))
						&& (cultivation.getCurrentSeasonCode() != null
								&& cultivation.getCurrentSeasonCode().equals(String.valueOf(farm[9]))))
						.forEach(cultivation -> {
							totalCoc = (totalCoc + (StringUtil.isDouble(cultivation.getTotalCoc())
									? Double.valueOf(cultivation.getTotalCoc()) : 0D));
							interCropIncome = (interCropIncome + (StringUtil.isDouble(cultivation.getInterCropIncome())
									? Double.valueOf(cultivation.getInterCropIncome()) : 0D));
							agriIncome = (agriIncome + (StringUtil.isDouble(cultivation.getAgriIncome())
									? Double.valueOf(cultivation.getAgriIncome()) : 0D));
							otherSourcesIncome = (otherSourcesIncome
									+ (StringUtil.isDouble(cultivation.getOtherSourcesIncome())
											? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0D));
							cottonQty = (cottonQty + (StringUtil.isDouble(cultivation.getCottonQty())
									? Double.valueOf(cultivation.getCottonQty()) : 0D));
							unitSalePrice = (unitSalePrice + (StringUtil.isDouble(cultivation.getUnitSalePrice())
									? Double.valueOf(cultivation.getUnitSalePrice()) : 0D));
							saleCottonIncome = (saleCottonIncome
									+ (StringUtil.isDouble(cultivation.getSaleCottonIncome())
											? Double.valueOf(cultivation.getSaleCottonIncome()) : 0D));
						});
				
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){

				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(((!ObjectUtil.isEmpty(agriIncome) ? format.format(agriIncome).toString() : "")));
				style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);


				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(
						((!ObjectUtil.isEmpty(interCropIncome) ? format.format(interCropIncome).toString() : "")));

				}
				
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(farm[3].toString());
					for (CropSupply cropSupply : cropSup) {
						List<CropSupplyDetails> cropSupplyDetails = new ArrayList();
						cropSupplyDetails.addAll(cropSupply.getCropSupplyDetails());
						for (CropSupplyDetails cropSupplyDetail : cropSupplyDetails) {
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
								main=main+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.INTERCROP.ordinal()) {
								inter=inter+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.BORDERCROP.ordinal()) {
								border=border+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.COVERCROP.ordinal()) {
								cover=cover+cropSupplyDetail.getSubTotal();
							}
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()) {
								plant=plant+cropSupplyDetail.getSubTotal();
							}
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.TRAPCROP.ordinal()) {
								trap=trap+cropSupplyDetail.getSubTotal();
								
							} 
						}
						
						
					}
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(main) ? format.format(main).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(inter) ? format.format(inter).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(border) ? format.format(border).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(cover) ? format.format(cover).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(plant) ? format.format(plant).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(trap) ? format.format(trap).toString() : "")));
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							((!ObjectUtil.isEmpty(otherSourcesIncome) ? format.format(otherSourcesIncome).toString() : "")));
				
					
				}
				
				double grossIncome = 0.0;
				Double totalProfit = 0.0;
				grossIncome += !ObjectUtil.isEmpty(interCropIncome) ? Double.valueOf((interCropIncome).toString()) : 0;
				grossIncome += !ObjectUtil.isEmpty(agriIncome) ? Double.valueOf(agriIncome.toString()) : 0;
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString(
							(!ObjectUtil.isEmpty(format.format(otherSourcesIncome+main+inter+border+cover+plant+trap)) ? format.format(otherSourcesIncome+main+inter+border+cover+plant+trap) : "")));
				}
				else{
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(new HSSFRichTextString(
							(!ObjectUtil.isEmpty(format.format(grossIncome)) ? format.format(grossIncome) : "")));
				}
				

				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(((!ObjectUtil.isEmpty(totalCoc) ? format.format(totalCoc) : "")));
				if (ObjectUtil.isEmpty(totalCoc)) {
					totalCoc = 0D;

				}
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					
					totalProfit = Double.valueOf(CurrencyUtil.getDecimalFormat(otherSourcesIncome+main+inter+border+cover+plant+trap, "##.00")) - totalCoc;
				}
				else{

				if (grossIncome > 0.0 && !StringUtil.isEmpty(totalCoc)) {
					totalProfit = Double.valueOf(grossIncome) - totalCoc;
				}
				}
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(CurrencyUtil.getDecimalFormat(totalProfit, "##.00"));

				double profitPerAcre = 0.0;
				if (StringUtil.isEmpty(farm[7])) {
					farm[7] = "0";
				}

				if (StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))) {
					farm[7] = "0";
				}
				/*cell = row.createCell(colNum++);
				
				if(StringUtil.isEmpty(globalReport[4])){
					cell.setCellValue("");	
				}else{
					style4.setAlignment(alignStyle.ALIGN_RIGHT);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.valueOf(globalReport[4].toString()));
				}
				
				cell.setCellStyle(style4);
				
				*/
				
				/*cell = row.createCell(colNum++);
				cell.setCellStyle(style3);*/

				/*if (getCurrentTenantId().equalsIgnoreCase("pratibha") && (!getBranchId().equalsIgnoreCase("bci"))) {

					if (totalProfit > 0.0
							&& !StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))
							&& Double.parseDouble(farm[7].toString()) > 0.0) {
						profitPerAcre = totalProfit / Double.valueOf(farm[7].toString());

						cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(profitPerAcre)
								? format.format(profitPerAcre).toString() : "0.00")));

					} else {
						cell.setCellValue("0.00");
					}
				}*/
			}
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
		String fileName = getLocaleProperty("CBRReport") + fileNameDateFormat.format(new Date()) + ".xls";
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

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("CBRReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		if (!ObjectUtil.isEmpty(fileMap)) {
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("CBRReport"), fileMap, ".xls"));
		}
		return "xls";
	}

	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String getBranchIdParam() {
		return branchIdParam;
	}

	public void setBranchIdParam(String branchIdParam) {
		this.branchIdParam = branchIdParam;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public String getSamithiName() {
		return samithiName;
	}

	public void setSamithiName(String samithiName) {
		this.samithiName = samithiName;
	}

	public String getFarmName() {
		return farmName;
	}

	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}

	@Override
	public String populatePDF() throws Exception {
		List<String> filters = new ArrayList<String>();
		List<Object> fields = new ArrayList<Object>();
		List<List<Object>> entityObject = new ArrayList<List<Object>>();
		DecimalFormat format = new DecimalFormat("0.00");
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap(); // build branch map to get branch name form branch id.
		setMailExport(true);

		filters.add(getLocaleProperty("filter"));
		if (!StringUtil.isEmpty(firstName)) {
			filters.add((getLocaleProperty("farmerName") + " : " + firstName));
		}

		if (!StringUtil.isEmpty(fatherName)) {
			filters.add((getLocaleProperty("fatherName") + " : " + fatherName));
		}

		if (!StringUtil.isEmpty(farmerCode)) {
			filters.add((getLocaleProperty("farmerCode") + " : " + farmerCode));
		}
		if (!StringUtil.isEmpty(villageName)) {
			filters.add(getLocaleProperty("villageName") + " : " + (!ObjectUtil.isEmpty(villageName)
					? locationService.findVillageByCode(villageName).getName() : getText("NA")));
		}

		if (!StringUtil.isEmpty(seasonCode)) {
			HarvestSeason seasonObj = farmerService.findHarvestSeasonByCode(seasonCode);
			if (!ObjectUtil.isEmpty(seasonObj)) {
				filters.add(getLocaleProperty("cSeasonCode") + " : " + seasonObj.getName());
			}
		}

		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse w = locationService.findSamithiByName(samithiName);
			filters.add(getLocaleProperty("samithiName") + " : " + w.getName());
		}

		if (!StringUtil.isEmpty(farmName)) {

			filters.add(getLocaleProperty("farmName") + " : " + farmName);
		}
		if (!StringUtil.isEmpty(stateName)) {
			filters.add(getLocaleProperty("stateName") + " : " + (!ObjectUtil.isEmpty(stateName)
					? locationService.findStateByCode(stateName).getName() : getText("NA")));
		}
		if (!StringUtil.isEmpty(fpo)) {
			filters.add(getLocaleProperty("fpo") + " : " + (!ObjectUtil.isEmpty(fpo)
					? farmerService.findfarmcatalogueByCode(fpo).getName() : getText("NA")));
		}
		if (!StringUtil.isEmpty(icsName)) {
			filters.add(getLocaleProperty("icsName") + " : " + (!ObjectUtil.isEmpty(icsName)
					? farmerService.findfarmcatalogueByCode(icsName).getName() : getText("NA")));
		}
		setFilter(new Farm());
		Farmer f = new Farmer();
		if (!StringUtil.isEmpty(seasonCode)) {
			Farmer farmerSeason = new Farmer();
			farmerSeason.setSeasonCode(seasonCode);
			filter.setFarmer(farmerSeason);
			}

		if (!StringUtil.isEmpty(farmerCode)) {
			f.setFarmerId(farmerCode);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(firstName)) {
			f.setFirstName(firstName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(fatherName)) {
			f.setLastName(fatherName);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(villageName)) {
			Village v = new Village();
			v.setCode(villageName);
			f.setVillage(v);
			filter.setFarmer(f);

		}

		if (!StringUtil.isEmpty(branchIdParam)) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParam);
				f.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParam);
				branchList.add(branchIdParam);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				f.setBranchesList(branchList);
			}
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			f.setBranchId(subBranchIdParma);
			filter.setFarmer(f);
		}

		if (!StringUtil.isEmpty(icsName)) {
			f.setIcsName(icsName);
			filter.setFarmer(f);
		}
		if (!StringUtil.isEmpty(samithiName)) {
			Warehouse W = new Warehouse();
			W.setName(samithiName);
			f.setSamithi(W);
			filter.setFarmer(f);
		}
		if (!StringUtil.isEmpty(fpo)) {
			f.setFpo(fpo);
			filter.setFarmer(f);
		}
		if (!StringUtil.isEmpty(farmName)) {
			filter.setFarmName(farmName);
		}

		super.filter = this.filter;
		Map dataz = readData("cBRReportActionProp");
		List<Object[]> list = (List) dataz.get(ROWS);

		/*
		 * Map<String, String> seasonMap =
		 * farmerService.listHarvestSeasons().stream()
		 * .collect(Collectors.toMap(HarvestSeason::getCode,
		 * HarvestSeason::getName));
		 * 
		 * List<Cultivation> cultivationList = farmerService.listCultivation();
		 * List<CropHarvest> cropHarvestList = farmerService.listCropHarvest();
		 */
		if (ObjectUtil.isListEmpty(list))
			return null;

		for (Object[] farm : list) {
			fields = new ArrayList<Object>();
			if (StringUtil.isEmpty(branchIdValue)) {
				fields.add((!StringUtil.isEmpty(farm[8]) ? (branchesMap.get(farm[8])) : "N/A"));
			}

			if (!ObjectUtil.isEmpty(farm[9]) && !StringUtil.isEmpty(farm[9])) {
				fields.add(getSeasonMap().containsKey(farm[9]) ? getSeasonMap().get(farm[9]) : getText("N/A"));
			} else {
				fields.add("");
			}

			fields.add((!StringUtil.isEmpty(farm[2]) ? farm[2].toString() : "N/A"));

			fields.add((!StringUtil.isEmpty(farm[4]) ? farm[4].toString() : "N/A"));

			if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				if (getBranchId() == null || !getBranchId().equalsIgnoreCase("bci"))

					fields.add((!StringUtil.isEmpty(farm[10]) ? farm[10].toString() : "N/A"));
			}

			fields.add((!StringUtil.isEmpty(farm[5]) ? farm[5].toString() : "N/A"));

			fields.add((!StringUtil.isEmpty(farm[6]) ? farm[6].toString() : "N/A"));
			if (!getCurrentTenantId().equalsIgnoreCase("pratibha")) {
				fields.add((!StringUtil.isEmpty(farm[7]) ? farm[7].toString() : ""));
			}
			/*
			 * incomeCotton = new Double(0); if
			 * (!ObjectUtil.isListEmpty(getCropHarvestList())) {
			 * getCropHarvestList().stream().filter(croHarvest ->
			 * croHarvest.getFarmerId().equals(farm[11].toString()) &&
			 * croHarvest.getFarmCode().equals(farm[3].toString())).forEach(
			 * cropHarvest -> {
			 * 
			 * cropHarvest.getCropHarvestDetails().forEach(chDetails -> {
			 * incomeCotton += chDetails.getSubTotal(); }); });
			 * 
			 * fields.add(format.format(incomeCotton)); } else {
			 * 
			 * fields.add(incomeCotton); }
			 */

			if (!ObjectUtil.isListEmpty(getCultivationList())) {
				totalCoc = new Double(0);
				interCropIncome = new Double(0);
				agriIncome = new Double(0);
				otherSourcesIncome = new Double(0);
				cottonQty = new Double(0);
				unitSalePrice = new Double(0);
				saleCottonIncome = new Double(0);
				double trap=0;
				double plant=0;
				double cover=0;
				double border=0;
				double inter=0;
				double main=0;
				getCultivationList().stream()
						/*
						 * .filter(cultivation ->
						 * cultivation.getFarmerId().equals(farm[11].toString())
						 * && cultivation.getFarmId().equals(farm[3].toString())
						 * && cultivation.getCurrentSeasonCode().equals(String.
						 * valueOf(farm[9])))
						 */
						.filter(cultivation -> (cultivation.getFarmerId() != null
								&& cultivation.getFarmerId().equals(farm[11].toString()))
								&& (cultivation.getFarmId() != null
										&& cultivation.getFarmId().equals(farm[3].toString()))
								&& (cultivation.getCurrentSeasonCode() != null
										&& cultivation.getCurrentSeasonCode().equals(String.valueOf(farm[9]))))
						.forEach(cultivation -> {
							totalCoc = (totalCoc + (StringUtil.isDouble(cultivation.getTotalCoc())
									? Double.valueOf(cultivation.getTotalCoc()) : 0D));
							interCropIncome = (interCropIncome + (StringUtil.isDouble(cultivation.getInterCropIncome())
									? Double.valueOf(cultivation.getInterCropIncome()) : 0D));
							agriIncome = (agriIncome + (StringUtil.isDouble(cultivation.getAgriIncome())
									? Double.valueOf(cultivation.getAgriIncome()) : 0D));
							otherSourcesIncome = (otherSourcesIncome
									+ (StringUtil.isDouble(cultivation.getOtherSourcesIncome())
											? Double.valueOf(cultivation.getOtherSourcesIncome()) : 0D));
							cottonQty = (cottonQty + (StringUtil.isDouble(cultivation.getCottonQty())
									? Double.valueOf(cultivation.getCottonQty()) : 0D));
							unitSalePrice = (unitSalePrice + (StringUtil.isDouble(cultivation.getUnitSalePrice())
									? Double.valueOf(cultivation.getUnitSalePrice()) : 0D));
							saleCottonIncome = (saleCottonIncome
									+ (StringUtil.isDouble(cultivation.getSaleCottonIncome())
											? Double.valueOf(cultivation.getSaleCottonIncome()) : 0D));
						});
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				fields.add(((!ObjectUtil.isEmpty(agriIncome) ? format.format(agriIncome).toString() : "")));
				fields.add(((!ObjectUtil.isEmpty(interCropIncome) ? format.format(interCropIncome).toString() : "")));
				}
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(farm[3].toString());
					for (CropSupply cropSupply : cropSup) {
						List<CropSupplyDetails> cropSupplyDetails = new ArrayList();
						cropSupplyDetails.addAll(cropSupply.getCropSupplyDetails());
						for (CropSupplyDetails cropSupplyDetail : cropSupplyDetails) {
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.MAINCROP.ordinal()) {
								main=main+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.INTERCROP.ordinal()) {
								inter=inter+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.BORDERCROP.ordinal()) {
								border=border+cropSupplyDetail.getSubTotal();
							} 
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.COVERCROP.ordinal()) {
								cover=cover+cropSupplyDetail.getSubTotal();
							}
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.PLANTONBUND.ordinal()) {
								plant=plant+cropSupplyDetail.getSubTotal();
							}
							if (cropSupplyDetail.getCropType() == FarmCrops.CROPTYPE.TRAPCROP.ordinal()) {
								trap=trap+cropSupplyDetail.getSubTotal();
								
							} 
						}
						
						
					}
					fields.add(((!ObjectUtil.isEmpty(main) ? format.format(main).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(inter) ? format.format(inter).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(border) ? format.format(border).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(cover) ? format.format(cover).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(plant) ? format.format(plant).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(trap) ? format.format(trap).toString() : "")));
					fields.add(((!ObjectUtil.isEmpty(otherSourcesIncome) ? format.format(otherSourcesIncome).toString() : "")));
					
					
				}
				double grossIncome = 0.0;
				Double totalProfit = 0.0;
				grossIncome += !ObjectUtil.isEmpty(interCropIncome) ? Double.valueOf((interCropIncome).toString()) : 0;
				grossIncome += !ObjectUtil.isEmpty(agriIncome) ? Double.valueOf(agriIncome.toString()) : 0;
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					fields.add(((!ObjectUtil.isEmpty(format.format(otherSourcesIncome+main+inter+border+cover+plant+trap)) ? format.format(otherSourcesIncome+main+inter+border+cover+plant+trap) : "")));
				}
				else{

				fields.add(((!ObjectUtil.isEmpty(format.format(grossIncome)) ? format.format(grossIncome) : "")));
				}
				fields.add(((!ObjectUtil.isEmpty(totalCoc) ? format.format(totalCoc) : "")));
				if (ObjectUtil.isEmpty(totalCoc)) {
					totalCoc = 0D;

				}
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					
					totalProfit = Double.valueOf(CurrencyUtil.getDecimalFormat(otherSourcesIncome+main+inter+border+cover+plant+trap, "##.00")) - totalCoc;
				}
				else{
				if (grossIncome > 0.0 && !StringUtil.isEmpty(totalCoc)) {
					totalProfit = Double.valueOf(grossIncome) - totalCoc;
				}
				}
				fields.add(CurrencyUtil.getDecimalFormat(totalProfit, "##.00"));

				double profitPerAcre = 0.0;
				if (StringUtil.isEmpty(farm[7])) {
					farm[7] = "0";
				}

				if (StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))) {
					farm[7] = "0";
				}

				/*if (getCurrentTenantId().equalsIgnoreCase("pratibha") && (!getBranchId().equalsIgnoreCase("bci"))) {

					if (totalProfit > 0.0
							&& !StringUtil.isEmpty(StringUtil.trimIncludingNonbreakingSpace(farm[7].toString()))
							&& Double.parseDouble(farm[7].toString()) > 0.0) {
						profitPerAcre = totalProfit / Double.valueOf(farm[7].toString());

						fields.add(((!ObjectUtil.isEmpty(profitPerAcre) ? format.format(profitPerAcre).toString()
								: "0.00")));
					} else {
						fields.add("0.00");
					}
				}*/

				entityObject.add(fields);
			}
		}
		InputStream is = null;
		if (getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase("meridian")
				|| getCurrentTenantId().equalsIgnoreCase("pratibha")) {
			is = getPDFExportStreamForChetna(entityObject, filters);
		} else {
			is = getPDFExportStream(entityObject, filters);
		}
		setPdfFileName(getText("CBRReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		if (!ObjectUtil.isEmpty(fileMap)) {
			setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("CBRReport"), fileMap, ".pdf"));
		}
		return "pdf";
	}

	public InputStream getPDFExportStream(List<List<Object>> obj, List<String> filters)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		//BaseFont bf =BaseFont.createFont("c:/windows/fonts/arial.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getLocaleProperty("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
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
		title = new Paragraph(new Phrase(getText("exportCBRTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
		int size = filters.size();																							// title.
		if(size>1 ){
		for (String filter : filters) {
			document.add(new Paragraph(
					new Phrase(filter, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 4, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		}

		if (StringUtil.isEmpty(getBranchId())) { // Check if branch User is
			// logged in.
			columnHeaders = getLocaleProperty("exportCbrColumnHeaderBranch");
		} else {
			columnHeaders = getLocaleProperty("exportCbrColumnHeader");
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
			Long serialNo = 0L;

			for (List<Object> entityObj : obj) { // iterate over all list of
													// objects.
				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				for (Object entityField : entityObj) {
					// BEGIN OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.

					if (entityField != null) {
						 try 
					        {
						Float.parseFloat(entityField.toString());
						cell = new PdfPCell(new Phrase(
								StringUtil.isEmpty(entityField.toString()) ? getText("NA") : entityField.toString(),
								cellFont));
		            	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
					        }
						 catch  (NumberFormatException e) {
						cell = new PdfPCell(new Phrase(
								StringUtil.isEmpty(entityField.toString()) ? getText("NA") : entityField.toString(),
								cellFont));	
						table.addCell(cell);
						 }
					} else {
						cell = new PdfPCell(new Phrase("", cellFont));
						table.addCell(cell);
					}
					// END OF CODE FOR A PARTICULAR CELL IN A ROW OF TABLE TO
					// PDF FILE.
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
		return preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
	}

	/**
	 * Gets the Father Name list.
	 * 
	 * @return the fatherName list
	 */
	/*
	 * public List<String[]> getFatherNameList() {
	 * 
	 * List<String[]> fatherNameList = farmerService.listByFatherNameList();
	 * 
	 * return fatherNameList;
	 * 
	 * }
	 * 
	 *//**
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
		}
		return seasonMap;

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

	public Map<String, String> getFields() {
		fields.put("1", getText("cSeasonCode"));
		fields.put("2", getText("farmerName"));
		fields.put("3", getLocaleProperty("fatherName"));
		fields.put("4", getText("village"));
		if (getCurrentTenantId().equalsIgnoreCase("pratibha") && !getBranchId().equalsIgnoreCase("bci")) {
			fields.put("5", getText("icsName"));
		}
		fields.put("6", getText("samithiName"));
		fields.put("7", getText("farmName"));

		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			fields.put("9", getText("farmerCode"));
		}

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			fields.put("10", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			fields.put("6", getText("app.branch"));
		}
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public InputStream getPDFExportStreamForChetna(List<List<Object>> obj, List<String> filters)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {

		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		String columnHeaders = null; // to hold column headers from property
										// file.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getText("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
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
		title = new Paragraph(new Phrase(getText("ExportPDFTitle"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 15, Font.NORMAL, GrayColor.GRAYBLACK)))); // Add
																														// a
																														// blank
																														// line
																														// after
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
		 * if (getCurrentTenantId().equalsIgnoreCase("chetna")) { columnHeaders
		 * = getText("chetnaExportPDFHeading"); } else if
		 * (getCurrentTenantId().equalsIgnoreCase("meridian")) { columnHeaders =
		 * getText("meridianExportPDFHeading"); }
		 */
		/*
		 * if ((getIsMultiBranch().equalsIgnoreCase("1") &&
		 * (getIsParentBranch().equals("1") ||
		 * StringUtil.isEmpty(branchIdValue)))) { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportCbrColumnHeadingBranch"); } else if
		 * (!StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("ExportCbrColumnHeading"); } } else { if
		 * (StringUtil.isEmpty(branchIdValue)) { columnHeaders =
		 * getLocaleProperty("exportCbrColumnHeaderBranch"); } else {
		 * columnHeaders = getLocaleProperty("exportCbrColumnHeader"); } }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportCbrColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("ExportCbrColumnHeading");
			}
		} else if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {

			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportCbrColumnHeaderBranchPratibha");
			} else if (getBranchId().equalsIgnoreCase("organic")) {
				columnHeaders = getLocaleProperty("OrganicCbrExportHeader");
			} else if (getBranchId().equalsIgnoreCase("bci")) {
				columnHeaders = getLocaleProperty("BCICbrExportHeader");
			}

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				columnHeaders = getLocaleProperty("exportCbrColumnHeaderBranch");
			} else {
				columnHeaders = getLocaleProperty("exportCbrColumnHeader");
			}
		}

		if (!ObjectUtil.isEmpty(columnHeaders)) {
			PdfPCell cell = null; // cell for table.
			cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK);
		//	cellFont = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD, GrayColor.GRAYBLACK); // setting
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
				cell.setBackgroundColor(new BaseColor(255, 255, 224));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false); // To set wrapping of text in cell.
				// cell.setColspan(3); // To add column span.
				table.addCell(cell);
			}

			cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK); // setting
																							// font
																							// for
																							// cells.
			Long serialNo = 0L;

			for (List<Object> entityObj : obj) { // iterate over all list of
													// objects.

				serialNo++;

				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				table.addCell(cell);

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

			document.add(table); // Add table to document.
		}
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;

	}

	public void populateTotalValues() {

		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

		jsonObject.put("fCount", String.valueOf(!ObjectUtil.isEmpty(totalFarmer) ? totalFarmer : ""));
		jsonObject.put("tProfit", String.valueOf(!ObjectUtil.isEmpty(totalFarmerProfit) ? totalFarmerProfit : ""));

		jsonObjects.add(jsonObject);
		printAjaxResponse(jsonObjects, "text/html");
	}

	public Map<Integer, String> getIcsTypeList() {

		return icsTypeList;
	}

	public void setIcsTypeList(Map<Integer, String> icsTypeList) {

		this.icsTypeList = icsTypeList;
	}

	public String getStateName() {

		return stateName;
	}

	public void setStateName(String stateName) {

		this.stateName = stateName;
	}

	public String getFpo() {

		return fpo;
	}

	public void setFpo(String fpo) {

		this.fpo = fpo;
	}

	public Map<String, String> getSeasonMap() {
		if (seasonMap.size() <= 0) {
			seasonMap = farmerService.listHarvestSeasons().stream()
					.collect(Collectors.toMap(HarvestSeason::getCode, HarvestSeason::getName));
		}
		return seasonMap;
	}

	public List<Cultivation> getCultivationList() {
		if (ObjectUtil.isListEmpty(cultivationList)) {
				cultivationList = farmerService.listCultivation();
				if (ObjectUtil.isListEmpty(cultivationList)) {
					cultivationList.add(new Cultivation());
				}
			
		}
		return cultivationList;
	}

	public List<CropHarvest> getCropHarvestList() {
		if (ObjectUtil.isListEmpty(cropHarvestList)) {
			cropHarvestList = farmerService.listCropHarvest();
		}
		return cropHarvestList;
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

	public List<String> getFarmerCountList() {
		return farmerCountList;
	}

	public void setFarmerCountList(List<String> farmerCountList) {
		this.farmerCountList = farmerCountList;
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

}
