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
import java.util.concurrent.atomic.AtomicInteger;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
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
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
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

public class FarmerIncomeReportAction extends BaseReportAction implements IExporter {
	private static final long serialVersionUID = 1L;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private InputStream fileInputStream;
	private String xlsFileName;
	private Cultivation filter;
	private Map<String, String> incomeReportFilters = new HashMap<>();
	private String farmerName;
	private String farmerCode;
	private String lastName;
	private String villageCode;
	private String villageName;
	private String branchIdParma;
	private String BranchIds;
	private String exportLimit;
	private String farmerCodeEnabled;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICatalogueService catalogueService;
	private String daterange;
	private IProductDistributionService productDistributionService;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	private IClientService clientService;
	private String seasonCode;
	private String stateName;
	private String fpo;
	private String icsName;
	private List<Farm> farmerIncomeList=new ArrayList<>();
	DecimalFormat formatter = new DecimalFormat("0.00");
	private String farmerIncomeDatas;
	@Autowired
	private IDeviceService deviceService;
	private String type;
	private String farmerId;
	private String seasonId;
	Map<String, String> farmerCodList = new LinkedHashMap<>();
	Map<String, String> farmerNameList = new LinkedHashMap<>();
	Map<String, String> farmerFarmerIdList = new LinkedHashMap<>();
	Map<String, String> fatherNameList = new LinkedHashMap<>();
	Map<String, String> villageMap = new LinkedHashMap<>();
	private String incomeStr;
	private String headerFields;
	private Farm farmDetail;
/*	double trap=0;
	double plant=0;
	double cover=0;
	double border=0;
	double inter=0;
	double main=0;*/
	public String list() {

		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		
		/*
		 * super.startDate = df
		 * .format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR),
		 * currentDate.get(Calendar.MONTH))); super.endDate =
		 * df.format(currentDate.getTime());
		 */
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());
		daterange = super.startDate + " - " + super.endDate;
		String branchId=getBranchId();
	//	List<Cultivation> farmerList = farmerService.listCultivation();
		List<Object[]> farmerList = farmerService.listCultivationByFarmerIncome(branchId);
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.stream().forEach(obj -> {
				
				if(!villageMap.containsKey(obj[1].toString())){
				villageMap.put(obj[2].toString(), obj[1].toString());
				}
				if(!farmerNameList.containsKey(obj[0].toString())){
					farmerNameList.put(obj[0].toString(), obj[0].toString());
					}
				
				if (!farmerFarmerIdList.containsKey(obj[3])) {
					farmerFarmerIdList.put(obj[3].toString(), obj[3].toString());
				}
				
		});
	}
	/*	List<Object[]> farmerList = farmerService.listFarmerInfo();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerList.stream().forEach(obj -> {
				
					if(!villageMap.containsKey(obj[11].toString())){
					villageMap.put(obj[11].toString(), obj[6].toString());
					}
				
					if( obj[2]!=null &&  !farmerCodList.containsKey(obj[2].toString())){
					farmerCodList.put(obj[2].toString(), obj[2].toString());
					}
				
				if (!farmerNameList.containsKey(obj[3])) {
					farmerNameList.put(obj[3].toString(), obj[3].toString());
				}
			
					if (!farmerFarmerIdList.containsKey(obj[1])) {
						farmerFarmerIdList.put(obj[1].toString(), obj[1].toString());
					}
					
					if (!fatherNameList.containsKey(obj[4]) && obj[4]!=null) {
						fatherNameList.put(obj[4].toString(), obj[4].toString());
					}
					
			});
		}*/
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}

		// incomeReportFilters.add(getText("date"));
		request.setAttribute(HEADING, getText(LIST));
		filter = new Cultivation();
		return LIST;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String data() throws Exception {

		this.filter = new Cultivation();
		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);
		}
		
		if (!StringUtil.isEmpty(villageCode)) {
			filter.setVillageId((villageCode));

		}
		if (!StringUtil.isEmpty(seasonCode)){
		filter.setCurrentSeasonCode(seasonCode);
		}
		
		

		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}
         
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				filter.setStateName(stateName);
			}
			if (!StringUtil.isEmpty(fpo)) {

				filter.setFpo(fpo);
				
			}
			if (!StringUtil.isEmpty(icsName)) {
				filter.setIcsName(icsName);
				
			}
		}

		/*Farmer farmer = new Farmer();

		Village village = new Village();
		State s = new State();
		if (!StringUtil.isEmpty(farmerName) || !StringUtil.isEmpty(lastName) || !StringUtil.isEmpty(farmerCode)
				|| !StringUtil.isEmpty(villageCode) || !StringUtil.isEmpty(branchIdParma)|| !StringUtil.isEmpty(farmerId)) {
			farmer = new Farmer();
		}

		if (!StringUtil.isEmpty(seasonCode)) {
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
			//Farmer farmerSeason = new Farmer();
			farmer.setSeasonCode(seasonCode);
			filter.setFarmer(farmer);
			}
			else{
				Farmer farmerSeason = new Farmer();
				farmerSeason.setSeasonCode(seasonCode);
				filter.setFarmer(farmerSeason);
			}
		}

		if (!StringUtil.isEmpty(branchIdParma)) {

			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				farmer.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				farmer.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			farmer.setBranchId(subBranchIdParma);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			farmer.setFirstName(farmerName);
		}
		
		if (!StringUtil.isEmpty(farmerId)) {
			farmer.setFarmerId((farmerId));

		}

		
		 * if (!StringUtil.isEmpty(lastName)) { farmer.setLastName(lastName);
		 * 
		 * }
		 

		if (!StringUtil.isEmpty(farmerCode)) {
			farmer.setFarmerCode(farmerCode);

		}

		
		 * if(!StringUtil.isEmpty(villageCode)){
		 * filter.getFarmer().setVillage(locationService.findVillageByName(
		 * villageCode.trim())); }
		 

		if (!StringUtil.isEmpty(villageCode)) {
			village.setCode(villageCode);
			farmer.setVillage(village);

		}
					

		}
		if (!ObjectUtil.isEmpty(farmer)) {
			filter.setFarmer(farmer);
		}
	*/
		
		
		super.filter = this.filter;
		Map data = readData("farmerIncome");
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		
		JSONObject jsonObject = new JSONObject();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled("0");
		if (!StringUtil.isEmpty(preferences)) {
			if (preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE) != null) {
				setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));
			}
		}
		Object[] datas = (Object[]) obj;
		JSONArray rows = new JSONArray();

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil
						.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[8].toString())))
								? getBranchesMap().get(getParentBranchMap().get(datas[8].toString()))
								: getBranchesMap().get(datas[8].toString()));
			}
			rows.add(getBranchesMap().get(datas[8].toString()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(datas[8].toString()));
			}
		}
		HarvestSeason season = farmerService.findSeasonNameByCode(datas[9].toString());
		rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
		/*
		 * if (!StringUtil.isEmpty(farmerCodeEnabled) &&
		 * farmerCodeEnabled.equalsIgnoreCase("1")) {
		 * rows.add(farm.getFarmer().getFarmerCode()); }
		 */
		Farmer farmer =farmerService.findFarmerByFarmerId(datas[3].toString());
		//rows.add(datas[4].toString());
		
		String firstName =  String.valueOf(datas[4]);
		String farmerId = String.valueOf(farmer.getId());
		String linkField = "<a href=farmer_detail.action?id="+farmerId+" target=_blank>"+firstName+"</a>";
		rows.add(!ObjectUtil.isEmpty(linkField) ? linkField : "");// Farmer
		// rows.add(farm.getFarmer().getLastName());
		rows.add(datas[2].toString());
		
		if (getBranchId() != null && getBranchId().equals("organic")) {
			
			rows.add(farmer.getIcsName());
		}
		/*
		 * rows.add((!StringUtil.isEmpty(farm.getFarmer().getDateOfJoining())?
		 * farm.getFarmer().getDateOfJoining().toString():""));
		 */
		rows.add((!StringUtil.isEmpty(farmer.getVillage()) ? farmer.getVillage().getName() : ""));
		double totalIncome = 0;
		double grossAgriIncome = 0;
	
		/*rows.add(datas[5].toString());
		rows.add(datas[6].toString());
		rows.add(datas[7].toString());*/
		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
		rows.add(!ObjectUtil.isEmpty(datas[5]) && !datas[5].equals("")
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[5].toString()), "##.00") : "0.00");
		grossAgriIncome += !ObjectUtil.isEmpty(datas[5]) && !datas[5].equals("") ? Double.valueOf((datas[5]).toString())
				: 0;
		rows.add(!ObjectUtil.isEmpty(datas[6]) && !datas[6].equals("")
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[6].toString()), "##.00") :"0.00");
		grossAgriIncome += !ObjectUtil.isEmpty(datas[6]) && !datas[6].equals("") ? Double.valueOf((datas[6]).toString())
				: 0;
		}
		double trap=0;
		double plant=0;
		double cover=0;
		double border=0;
		double inter=0;
		double main=0;
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
			
			List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(datas[1].toString());
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
		}
		
		rows.add(!ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("")
				? CurrencyUtil.getDecimalFormat(Double.valueOf(datas[7].toString()), "##.00") : "0.00");
		if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
			grossAgriIncome += !ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("") ? Double.valueOf((datas[7]).toString())
					: 0;
					rows.add(CurrencyUtil.getDecimalFormat(grossAgriIncome+main+inter+border+cover+plant+trap, "##.00"));	
		}
		else{
		
		grossAgriIncome += !ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("") ? Double.valueOf((datas[7]).toString())
				: 0;
				rows.add(CurrencyUtil.getDecimalFormat(grossAgriIncome, "##.00"));	
				
		}	
				
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
					rows.add(!ObjectUtil.isEmpty(farmer.getLoanTakenLastYear())
							? farmer.getLoanTakenLastYear() == 1 ? getText("yes") : getText("no") : "");
				}
		/*
		 * if (!getCurrentTenantId().equals("pratibha")) {
		 * rows.add((!StringUtil.isEmpty(farm.getFarmer().getVillage()) ?
		 * farm.getFarmer().getVillage().getCity().getName() : ""));
		 * rows.add((!StringUtil.isEmpty(farm.getFarmer().getVillage()) ?
		 * farm.getFarmer().getVillage().getCity().getLocality().getName() :
		 * "")); rows.add((!StringUtil.isEmpty(farm.getFarmer().getVillage()) ?
		 * farm.getFarmer().getVillage().getCity().getLocality().getState().
		 * getName() : "")); }
		 */
	/*	double grossAgriIncome = 0;
		Object[] cultivationCost = null;
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
			List<Object[]> agriIncome = farmerService.findCultivationCostBySeason(farm.getFarmer().getFarmerId(), farm.getFarmCode(),getCurrentSeasonsCode());
			if (!ObjectUtil.isListEmpty(agriIncome)) {
				cultivationCost = agriIncome.get(0);
			}
		}
		
		else{
			List<Object[]> agriIncome = farmerService.findCultivationCost(farm.getFarmer().getFarmerId(),
					farm.getFarmCode());
		if (!ObjectUtil.isListEmpty(agriIncome)) {
			cultivationCost = agriIncome.get(0);
		}
		}
		rows.add(!ObjectUtil.isEmpty(cultivationCost[2])
				? formatter.format(Double.valueOf(cultivationCost[2].toString())) : "");// agriIncome
		grossAgriIncome += !ObjectUtil.isEmpty(cultivationCost[2]) ? Double.valueOf((cultivationCost[2]).toString())
				: 0;
			Object cottonIncome = farmerService.findCottonIncomeByFarmerCode(farm.getFarmer().getFarmerId(),
					farm.getFarmCode());
			if (!ObjectUtil.isEmpty(cottonIncome)) {
				rows.add(String.valueOf(CurrencyUtil
						.getDecimalFormat(Double.parseDouble(String.valueOf(cottonIncome.toString())), "##.00")));
			} else {
				rows.add("");
			}
			grossAgriIncome += !ObjectUtil.isEmpty(cottonIncome)
					? Double.parseDouble(String.valueOf(cottonIncome.toString())) : 0;
			grossAgriIncome += !ObjectUtil.isEmpty(cultivationCost[1]) ? Double.valueOf((cultivationCost[1]).toString())
							: 0;
		

		
		
		 * grossAgriIncome += !ObjectUtil.isEmpty(cottonIncome) ?
		 * Double.valueOf(cottonIncome.toString()) : 0;
		 
		rows.add(!ObjectUtil.isEmpty(cultivationCost[1])
				? formatter.format(Double.valueOf(cultivationCost[1].toString())) : "");// interCrop
																						// income
			grossAgriIncome += !ObjectUtil.isEmpty(cultivationCost[3]) ? Double.valueOf((cultivationCost[3]).toString())
					: 0;
			rows.add(CurrencyUtil.getDecimalFormat(grossAgriIncome, "##.00"));// grossAgriIncome
		
		
		
		rows.add(!ObjectUtil.isEmpty(cultivationCost[3])
				? formatter.format(Double.valueOf(cultivationCost[3].toString())) : "");// other
		
			grossAgriIncome += !ObjectUtil.isEmpty(cultivationCost[3]) ? Double.valueOf((cultivationCost[3]).toString())
					: 0;
			rows.add(CurrencyUtil.getDecimalFormat(grossAgriIncome, "##.00"));// grossAgriIncome
		
		if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
			rows.add(!ObjectUtil.isEmpty(farm.getFarmer().getLoanTakenLastYear())
					? farm.getFarmer().getLoanTakenLastYear() == 1 ? getText("yes") : getText("no") : "");
		}
		if (!getCurrentTenantId().equals("pratibha")) {
			if ("1".equalsIgnoreCase(getIsInsured())) {
				if (!ObjectUtil.isEmpty(farm.getFarmer())) {
					rows.add(!StringUtil.isEmpty(farm.getFarmer().getLifeInsurance())
							? farm.getFarmer().getLifeInsurance().equalsIgnoreCase("1") ? getText("yes") : getText("no")
							: getText("no"));
					rows.add(!StringUtil.isEmpty(farm.getFarmer().getHealthInsurance())
							? farm.getFarmer().getHealthInsurance().equalsIgnoreCase("1") ? getText("yes")
									: getText("no")
							: getText("no"));
					if (!StringUtil.isEmpty(farm.getFarmer().getIsCropInsured())
							&& farm.getFarmer().getIsCropInsured() != null) {
						rows.add(farm.getFarmer().getIsCropInsured().equalsIgnoreCase("1") ? getText("yes")
								: getText("no"));
					} else {
						rows.add(getText("no"));
					}

				} else {
					rows.add(getText("no"));
					rows.add(getText("no"));
					rows.add(getText("no"));
				}
			}
		}
		// rows.add(cultivationCost[3].toString());
		
		 * rows.add(cultivationCost[1].toString());
		 * rows.add(cultivationCost[2].toString());
		 * rows.add(cultivationCost[3].toString());
		 
		*/

		//rows.add(farm.getFarmer().getBranchId());
		jsonObject.put("id", datas[0].toString());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public ILocationService getLocationService() {

		return locationService;
	}

	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	public IFarmerService getFarmerService() {

		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
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


	public Cultivation getFilter() {
		return filter;
	}

	public void setFilter(Cultivation filter) {
		this.filter = filter;
	}

	public String getFarmerName() {

		return farmerName;
	}

	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	public String getFarmerCode() {

		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {

		this.farmerCode = farmerCode;
	}

	public String getVillageCode() {

		return villageCode;
	}

	public void setVillageCode(String villageCode) {

		this.villageCode = villageCode;
	}

	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("FarmerIncomeReportList") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("farmerIncomeReportList"), fileMap, ".xls"));
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

	/*Double incomeCotton = new Double(0);
	Double totalCoc = new Double(0);
	Double interCropIncome = new Double(0);
	Double agriIncome = new Double(0);
	Double otherSourcesIncome = new Double(0);
	Double cottonQty = new Double(0);
	Double unitSalePrice = new Double(0);
	Double saleCottonIncome = new Double(0);*/

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(df.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		DecimalFormat format = new DecimalFormat("0.00");

		setFilter(new Cultivation());
		//filter.setFarmer(new Farmer());

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("farmerIncomeReport"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
	/*	style2.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);


		HSSFCellStyle style3 = wb.createCellStyle();

/*		style3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style3.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style3.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style3.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style3.setTopBorderColor(IndexedColors.BLACK.getIndex());*/
		HSSFCellStyle filterStyle = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFCellStyle style5 = wb.createCellStyle();
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
	//	Farmer farmer = new Farmer();

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerIncomeReport")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			if(!StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(farmerName) || !StringUtil.isEmpty(villageCode)
					|| !StringUtil.isEmpty(branchIdParma))
			{
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);
			}
			/*filterRow1 = sheet.createRow(rowNum++);

			cell = filterRow1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(getsDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow2 = sheet.createRow(rowNum++);

			cell = filterRow2.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow2.createCell(2);
			if (geteDate() != null) {

				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}*/
		if (!StringUtil.isEmpty(farmerName)) {

				//farmer = farmerService.findFarmerByFarmerName(farmerName);
				filter.setFarmerName(farmerName);
				filterRow3 = sheet.createRow(rowNum++);

				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmerName));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}

		/*		if (!StringUtil.isEmpty(farmerCode)) {
				filter.setFarmer(farmer);
				Farmer farmer2 = farmerService.findFarmerByFarmerCode(farmerCode);
				filter.getFarmer().setFarmerCode(farmerCode);
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerCode")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				cell.setCellValue(new HSSFRichTextString(farmer.getFarmerCode()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}*/

			if (!StringUtil.isEmpty(villageCode)) {
				filter.setFarmerName(villageCode);
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("villageName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(villageCode)
						? locationService.findVillageById(Long.valueOf(villageCode)).getName() : getText("NA"))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

			}

			if (!StringUtil.isEmpty(seasonCode)) {

				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cSeasonCode")));
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

			if (!StringUtil.isEmpty(branchIdParma)) {
				filter.setBranchId(branchIdParma);
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("BranchId")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				cell.setCellValue(new HSSFRichTextString((branchesMap.get(filter.getBranchId()))));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

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

			//filterRow3 = sheet.createRow(rowNum++);
			//filterRow4 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		mainGridRowHead.setHeight((short) 400);
		String mainGridColumnHeaders = "";
		ESESystem preferences = preferncesService.findPrefernceById("1");
		setFarmerCodeEnabled(preferences.getPreferences().get(ESESystem.ENABLE_FARMER_CODE));

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerIncomeColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerIncomeColumnHeading");
			}
		} else if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeaderBranchPratibha");
			}else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganiFarmerIncomeExportHeader");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIFarmerIncomeExportHeader");
			}
			
		}else{
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeader");
			}
		}

		int mainGridIterator = 0;
		
		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {
			
			if(cellHeader.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))		
			{		
				cellHeader=cellHeader.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());		
			}else if(cellHeader.trim().contains(ESESystem.AREA_TYPE_EXPORT))				
			{				
				cellHeader=cellHeader.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());				
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

		this.filter = new Cultivation();

	/*	Village village = new Village();
		State s = new State();
		if (!StringUtil.isEmpty(farmerName) || !StringUtil.isEmpty(farmerCode) || !StringUtil.isEmpty(villageCode)
				|| !StringUtil.isEmpty(branchIdParma)) {
			farmer = new Farmer();
		}

		if (!StringUtil.isEmpty(seasonCode)) {
			if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				farmer.setSeasonCode(seasonCode);
				filter.setFarmer(farmer);
			}else{
			Farmer farmerSeason = new Farmer();
			farmerSeason.setSeasonCode(seasonCode);
			filter.setFarmer(farmerSeason);
			}
		}*/

		/*if (!StringUtil.isEmpty(branchIdParma)) {

			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(branchIdParma);
				farmer.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(branchIdParma);
				branchList.add(branchIdParma);
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				farmer.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(subBranchIdParma) && !subBranchIdParma.equals("0")) {
			farmer.setBranchId(subBranchIdParma);
		}
*/
		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);

		}
		if (!StringUtil.isEmpty(villageCode)) {
			filter.setVillageId(villageCode);

		}
		if (!StringUtil.isEmpty(seasonCode)){
			filter.setCurrentSeasonCode(seasonCode);
			}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				filter.setStateName(stateName);
			}
			if (!StringUtil.isEmpty(fpo)) {

				filter.setFpo(fpo);
				
			}
			if (!StringUtil.isEmpty(icsName)) {
				filter.setIcsName(icsName);
				
			}
		}
		/*
		 * if (!StringUtil.isEmpty(lastName)) { farmer.setLastName(lastName);
		 * 
		 * }
		 */

		/*if (!StringUtil.isEmpty(farmerCode)) {
			farmer.setFarmerCode(farmerCode);

		}
*/
		/*
		 * if(!StringUtil.isEmpty(villageCode)){
		 * filter.getFarmer().setVillage(locationService.findVillageByName(
		 * villageCode.trim())); }
		 */

		/*if (!StringUtil.isEmpty(villageCode)) {
			village.setCode(villageCode);
			farmer.setVillage(village);

		}
		
		if (!StringUtil.isEmpty(farmerId)) {

			farmer.setFarmerId(farmerId);
			filter.setFarmer(farmer);
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {

			if (!StringUtil.isEmpty(stateName)) {
				s.setCode(stateName);
				farmer.setStateName(s.getCode());
				filter.setFarmer(farmer);
			}
			if (!StringUtil.isEmpty(fpo)) {

				farmer.setFpo(fpo);
				filter.setFarmer(farmer);
			}
			if (!StringUtil.isEmpty(icsName)) {
				farmer.setIcsName(icsName);
				filter.setFarmer(farmer);
			}
			if (!StringUtil.isEmpty(farmerId)) {

				farmer.setFarmerId(farmerId);
				filter.setFarmer(farmer);
			}

		}

		if (!ObjectUtil.isEmpty(farmer)) {
			filter.setFarmer(farmer);
		}
		
		*/
		if (!StringUtil.isEmpty(branchIdParma)) {
			filter.setBranchId(branchIdParma);
		}
		
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		}

		super.filter = this.filter;

		/*
		 * Map data = isMailExport() ? readData() : readExportData();
		 * 
		 * List<Farm> mainGridRows = (List<Farm>) data.get(ROWS);
		 */

		Map data = readData("farmerIncome");
		List<Object[]> mainGridRows = (List<Object[]>) data.get(ROWS);

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		Long serialNumber = 0L;

		/*Map<String, String> seasonMap = farmerService.listHarvestSeasons().stream()
				.collect(Collectors.toMap(HarvestSeason::getCode, HarvestSeason::getName));

		List<Cultivation> cultivationList = farmerService.listCultivation();
		List<CropHarvest> cropHarvestList = farmerService.listCropHarvest();*/

		for (Object[] datas : mainGridRows) {
			row = sheet.createRow(rowNum++);
			row.setHeight((short) 400);
			colNum = 0;

			serialNumber++;
			cell = row.createCell(colNum++);
			style5.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(style5);
			
			cell.setCellValue(serialNumber);
			
			
			
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[8].toString())))
									? getBranchesMap().get(getParentBranchMap().get(datas[8].toString()))
									: getBranchesMap().get(datas[8].toString())));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(getBranchesMap().get(datas[8].toString()));

			} else {
				if (StringUtil.isEmpty(branchIdValue)) {
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(
							new HSSFRichTextString(!StringUtil.isEmpty(datas[8].toString()) ? (branchesMap.get(datas[8].toString())) : ""));
				}
			}

			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			
			if (!ObjectUtil.isEmpty(datas[9].toString()) && !StringUtil.isEmpty(datas[9].toString())) {
				HarvestSeason season = farmerService.findSeasonNameByCode(datas[9].toString());
				cell.setCellValue(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			}

			
		
			Farmer farmers =farmerService.findFarmerByFarmerId(datas[3].toString());
			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(datas[4].toString()) ?datas[4].toString() : "N/A"));

			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(datas[2].toString())? datas[2].toString() : "N/A"));

			if (getBranchId() != null && getBranchId().equals("organic")) {
				
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue((!StringUtil.isEmpty(farmers.getIcsName())?farmers.getIcsName() : "N/A"));
			}
			cell = row.createCell(colNum++);
			cell.setCellStyle(style3);
			cell.setCellValue((!StringUtil.isEmpty(farmers.getVillage()) ? farmers.getVillage().getName() : ""));
			
			double totalIncome = 0;
			
				double grossAgriIncome = 0;
				if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue((!StringUtil.isEmpty(datas[5].toString()) && !datas[5].equals("")? datas[5].toString() : "N/A"));
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				grossAgriIncome += !ObjectUtil.isEmpty(datas[5]) && !datas[5].equals("") ? Double.valueOf((datas[5]).toString())
						: 0;
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue((!StringUtil.isEmpty(datas[6].toString()) && !datas[6].equals("")? datas[6].toString() : ""));
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				grossAgriIncome += !ObjectUtil.isEmpty(datas[6]) && !datas[6].equals("") ? Double.valueOf((datas[6]).toString())
						: 0;
				}
				double trap=0;
				double plant=0;
				double cover=0;
				double border=0;
				double inter=0;
				double main=0;
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					
					List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(datas[1].toString());
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
					cell.setCellValue(main);
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(inter);
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(border);
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(cover);
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(plant);
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(trap);
				
				}
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue((!StringUtil.isEmpty(datas[7].toString()) && !datas[7].equals("")? datas[7].toString() : ""));
				style4.setAlignment(CellStyle.ALIGN_RIGHT);
				cell.setCellStyle(style4);
				grossAgriIncome += !ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("") ? Double.valueOf((datas[7]).toString())
						: 0;
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
					/*grossAgriIncome += !ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("") ? Double.valueOf((datas[7]).toString())
							: 0;*/
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(CurrencyUtil.getDecimalFormat(grossAgriIncome+main+inter+border+cover+plant+trap, "##.00"));	
				}
				else{
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(grossAgriIncome);
				}
			if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
				
				
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
			
				cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(farmers.getLoanTakenLastYear())
						? farmers.getLoanTakenLastYear() == 1
								? getText("yes") : getText("no")
						: getText("NA"))));
			}
			// List<Object[]> agriIncome =
			// farmerService.findCultivationCost(null,null);

		/*	if (!ObjectUtil.isListEmpty(cultivationList)) {
				totalCoc = new Double(0);
				interCropIncome = new Double(0);
				agriIncome = new Double(0);
				otherSourcesIncome = new Double(0);
				cottonQty = new Double(0);
				unitSalePrice = new Double(0);
				saleCottonIncome = new Double(0);
				cultivationList.stream().filter(cultivation -> cultivation.getFarmerId().equals(farm[11].toString())
						&& cultivation.getFarmId().equals(farm[3].toString())).forEach(cultivation -> {
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
				
				
				
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(agriIncome);

				incomeCotton = new Double(0D);
				if (!ObjectUtil.isListEmpty(cropHarvestList)) {
					cropHarvestList.stream().filter(croHarvest -> croHarvest.getFarmerId().equals(farm[11].toString())
							&& croHarvest.getFarmCode().equals(farm[3].toString())).forEach(cropHarvest -> {

								cropHarvest.getCropHarvestDetails().forEach(chDetails -> {
									incomeCotton += chDetails.getSubTotal();
								});
							});
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(format.format(incomeCotton));
				} else {
					
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(incomeCotton);
					
				}
				
				
				double grossAgriIncome = 0;
				grossAgriIncome += agriIncome;
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
				//grossAgriIncome += !ObjectUtil.isEmpty(incomeCotton) ? Double.valueOf((incomeCotton).toString()) : 0;
				grossAgriIncome += !ObjectUtil.isEmpty(interCropIncome)
						? Double.valueOf((interCropIncome).toString()) : 0;
				}
				
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
					
					//grossAgriIncome += incomeCotton;
					grossAgriIncome += interCropIncome;
					grossAgriIncome += otherSourcesIncome;
				}
				
				
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(interCropIncome);
				
				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(otherSourcesIncome);

				cell = row.createCell(colNum++);
				cell.setCellStyle(style3);
				cell.setCellValue(new HSSFRichTextString((String.valueOf(grossAgriIncome))));
				if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(farm[12])
							? Integer.parseInt(StringUtil.isInteger(farm[12]) ? farm[12].toString() : "0") == 1
									? getText("yes") : getText("no")
							: getText("NA"))));
				}

				if (!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)
						) {
					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString((!StringUtil.isEmpty(farm[13])
							? farm[13].toString().equalsIgnoreCase("1") ? getText("yes") : getText("no")
							: getText("NA"))));

					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString((!StringUtil.isEmpty(farm[14])
							? farm[14].toString().equalsIgnoreCase("1") ? getText("yes") : getText("no")
							: getText("NA"))));

					cell = row.createCell(colNum++);
					cell.setCellStyle(style3);
					cell.setCellValue(new HSSFRichTextString((!StringUtil.isEmpty(farm[15])
							? farm[15].toString().equalsIgnoreCase("1") ? getText("yes") : getText("no")
							: getText("NA"))));
				}

				
			}*/

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
		String fileName = getLocaleProperty("farmerIncomeReportList") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

	}

/*	public Map<String, String> getFarmerNameList() {

		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[1]),
						// obj -> (String.valueOf(obj[3]) + "-" +
						// String.valueOf(obj[1]))));
						obj -> (String.valueOf(obj[3]))));
		farmerListMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return farmerListMap;
	}

	public List<String[]> getFatherNameList() {

		List<String[]> fatherNameList = farmerService.listFatherName();

		return fatherNameList;
	}*/

	/*
	 * public Map<String, String> getFarmerCodeList() {
	 * 
	 * Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
	 * List<Object[]> farmersList = farmerService.listFarmerInfo();
	 * 
	 * farmerListMap =
	 * farmersList.stream().filter(obj->!StringUtil.isEmpty(String.valueOf(obj[2
	 * ]))).collect(Collectors.toMap(obj -> String.valueOf(obj[2]), obj ->
	 * (String.valueOf(obj[2]))));
	 * 
	 * farmerListMap =
	 * farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,
	 * e2) -> e1, LinkedHashMap::new)); return farmerListMap;
	 * 
	 * }
	 */
	/*
	 * public List<String> getFarmerCodeList() { List<Object[]> farmersList =
	 * farmerService.listFarmerInfo(); List<String> farmerCodeList = new
	 * LinkedList<>(); for (Object[] obj : farmersList) {
	 * farmerCodeList.add(obj[2].toString()); } return farmerCodeList; }
	 */

	/*public List<String[]> getFarmerCodeList() {

		List<String[]> farmerCodeList = farmerService.listFarmerCode();

		return farmerCodeList;

	}*/

	/*public Map<String, String> getVillageList() {

		Map<String, String> villageListMap = new LinkedHashMap<String, String>();
		List<Village> villageList = locationService.listVillage();
		for (Village obj : villageList) {
			villageListMap.put(obj.getCode(), obj.getName());
		}
		return villageListMap;
	}*/

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getBranchIds() {
		return BranchIds;
	}

	public void setBranchIds(String branchId) {
		BranchIds = branchId;
	}

	
	
	

	public String getEnableMultiProduct() {
		return preferncesService.findPrefernceByName(ESESystem.ENABLE_MULTI_PRODUCT);
	}

	public String getIsInsured() {
		return preferncesService.findPrefernceByName(ESESystem.ENABLE_INSURANCE_INFO);
	}

	public String getLastName() {

		return lastName;
	}

	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

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

			/*
			 * seasonMap.put(String.valueOf(obj[0]), obj[1] + " - " + obj[0]);
			 */ seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
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

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
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

	public Map<String, String> getIncomeReportFilters() {
		/**
		 * incomeReportFilters.add(getText("cSeasonCode"));
		 * incomeReportFilters.add(getText("farmerName"));
		 * incomeReportFilters.add(getLocaleProperty("lastName"));
		 * incomeReportFilters.add(getText("village")); if
		 * (ObjectUtil.isEmpty(getBranchId())) {
		 * incomeReportFilters.add(getText("app.branch")); } if
		 * (!StringUtil.isEmpty(farmerCodeEnabled) &&
		 * farmerCodeEnabled.equalsIgnoreCase("1")) {
		 * incomeReportFilters.add(getText("farmerCode")); }
		 */

		incomeReportFilters.put("1", getText("cSeasonCode"));

		if (!StringUtil.isEmpty(farmerCodeEnabled) && farmerCodeEnabled.equalsIgnoreCase("1")) {
			incomeReportFilters.put("6", getText("farmerCode"));
		}

		incomeReportFilters.put("2", getText("farmerName"));
		// incomeReportFilters.put("3", getLocaleProperty("lastName"));
		incomeReportFilters.put("4", getText("village"));

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			incomeReportFilters.put("7", getText("app.branch"));
		} else if (StringUtil.isEmpty(getBranchId())) {
			incomeReportFilters.put("5", getText("app.branch"));
		}

		return incomeReportFilters;
	}

	public void setIncomeReportFilters(Map<String, String> incomeReportFilters) {
		this.incomeReportFilters = incomeReportFilters;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}
	
	@Override
	public String populatePDF() throws Exception {
		InputStream is = getPDFExportDataStream();
		setPdfFileName(getLocaleProperty("ListFileFarmerIncome") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(getPdfFileName(), is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getLocaleProperty("ListFileFarmerIncome"), fileMap, ".pdf"));
		return "pdf";

	}
	
	
	public InputStream getPDFExportDataStream()
			throws IOException, DocumentException, NoSuchFieldException, SecurityException {
		Font cellFont = null; // font for cells.
		Font titleFont = null; // font for title text.
		Paragraph title = null; // to add title for report
		Long serialNo = 0L;					// file.

		int mainGridColWidth = 0;
		int subGridColWidth = 0;

		List<Object[]> entityFieldsList = new ArrayList<Object[]>(); // to hold
																		// properties
																		// of
																		// entity
																		// object
																		// passed
																		// as
																		// list.
		SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Document document = new Document();
		setMailExport(true);
		int cols;

		String makeDir = FileUtil.storeXls(request.getSession().getId());
		String fileName = getLocaleProperty("ListFile") + fileNameDateFormat.format(new Date()) + ".pdf";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		PdfWriter.getInstance(document, fileOut);
		
		String serverFilePath = request.getSession().getServletContext().getRealPath("/");
		serverFilePath = serverFilePath.replace('\\', '/');		
		String arialFontFileLocation = serverFilePath+"/fonts/ARIAL.TTF";
		arialFontFileLocation = arialFontFileLocation.replace("//","/");
		BaseFont bf =BaseFont.createFont(arialFontFileLocation,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		document.open();

		PdfPTable table = null;
		PdfPCell cell = null;
		branchIdValue = getBranchId(); // set value for branch id.
		buildBranchMap();

		com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(exportLogo());
		logo.scaleAbsolute(100, 50);
		document.add(logo); // Adding logo in PDF file.
		// below of code for title text
		titleFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, GrayColor.GRAYBLACK);
		title = new Paragraph(new Phrase(getLocaleProperty("farmerIncomeReport"), titleFont));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(
				new Paragraph(new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		
		document.add(new Paragraph(
				new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		if (isMailExport()) {
			if (!StringUtil.isEmpty(seasonCode) || !StringUtil.isEmpty(farmerName) || !StringUtil.isEmpty(farmerId) || !StringUtil.isEmpty(villageCode) || !StringUtil.isEmpty(stateName)
					|| !StringUtil.isEmpty(fpo) || !StringUtil.isEmpty(icsName)  ) {
			
			document.add(new Paragraph(new Phrase(getLocaleProperty("filter"),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
		}
		if (!StringUtil.isEmpty(headerFields)) {
			String[] headerFieldsArr=headerFields.split("###");
			 
				  document.add(new Paragraph(
							new Phrase(headerFieldsArr[0] + " : " + headerFieldsArr[1],
									new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
		boolean flag = true;
		DecimalFormat df = new DecimalFormat("0.000");
		String mainGridColumnHeaders = ""; // line
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerIncomeColumnHeadingBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportFarmerIncomeColumnHeading");
			}
		} else if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeaderBranchPratibha");
			}else if(getBranchId().equalsIgnoreCase("organic")){
				mainGridColumnHeaders = getLocaleProperty("OrganiFarmerIncomeExportHeader");
			}else if(getBranchId().equalsIgnoreCase("bci")){
				mainGridColumnHeaders = getLocaleProperty("BCIFarmerIncomeExportHeader");
			}
			
		}else{
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportFarmerIncomeColumnHeader");
			}
		}
		int mainGridIterator = 0;
		table = new PdfPTable(mainGridColumnHeaders.split("\\,").length);
		cellFont = new Font(bf, 10, Font.BOLD, GrayColor.GRAYBLACK);
		mainGridColWidth = mainGridColumnHeaders.split("\\,").length;
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new Cultivation();

		if (!StringUtil.isEmpty(seasonCode)) {
			filter.setCurrentSeasonCode(seasonCode);
			HarvestSeason season = farmerService.findHarvestSeasonByCode(seasonCode);
			document.add(new Paragraph(new Phrase(getLocaleProperty("seasonCode") + ": " + season.getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		
		if (!StringUtil.isEmpty(farmerName)) {
			filter.setFarmerName(farmerName);
		//	Farmer frm = farmerService.findFarmerByFarmerId(farmerId);
			document.add(new Paragraph(new Phrase(getLocaleProperty("farmerName") + ": " + farmerName,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (!StringUtil.isEmpty(farmerId)) {
			filter.setFarmerId(farmerId);
		//	Farmer frm = farmerService.findFarmerByFarmerId(farmerId);
			document.add(new Paragraph(new Phrase(getLocaleProperty("farmerId") + ": " + farmerId,
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}

		if (!StringUtil.isEmpty(villageCode)) {
			filter.setVillageId(villageCode);
			document.add(new Paragraph(new Phrase(getLocaleProperty("village") + ": " + locationService.findVillageById(Long.valueOf(villageCode)).getName(),
					new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
			document.add(new Paragraph(
					new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
		}
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			if (!StringUtil.isEmpty(stateName)) {
				filter.setStateName(stateName);
				
				document.add(new Paragraph(new Phrase(getLocaleProperty("stateName") + ": " +  locationService.findStateByCode(stateName).getName() ,
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
			}
			if (!StringUtil.isEmpty(fpo)) {

				filter.setFpo(fpo);
				FarmCatalogue fc = getCatlogueValueByCode(fpo);
				document.add(new Paragraph(new Phrase(getLocaleProperty("fpo") + ": " + fc.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
				
			}
			if (!StringUtil.isEmpty(icsName)) {
				filter.setIcsName(icsName);
				FarmCatalogue fc = getCatlogueValueByCode(icsName);
				document.add(new Paragraph(new Phrase(getLocaleProperty("icsName") + ": " + fc.getName(),
						new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
				document.add(new Paragraph(
						new Phrase(" ", new Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK))));
				
			}
		}
		
		/*
		 * if (!StringUtil.isEmpty(farmerId)) { filter.setFarmerId(farmerId);
		 * Farmer frm=farmerService.findFarmerByFarmerId(farmerId);
		 * document.add(new Paragraph(new
		 * Phrase(getLocaleProperty("farmerName")+": "+frm.getName(),new
		 * Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK))));
		 * document.add( new Paragraph(new Phrase(" ", new
		 * Font(FontFamily.HELVETICA, 5, Font.NORMAL, GrayColor.GRAYBLACK)))); }
		 */

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (!StringUtil.isEmpty(branchIdParma)) {

				document.add(new Paragraph(
						new Phrase(getLocaleProperty("Organization") + " : " + getBranchesMap().get(getBranchIdParma()),
								new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				document.add(new Paragraph(new Phrase(
						getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
						new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));
			} else {
				if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
					document.add(new Paragraph(new Phrase(
							getLocaleProperty("Sub Organization") + " : " + getBranchesMap().get(getSubBranchIdParma()),
							new Font(FontFamily.HELVETICA, 10, Font.NORMAL, GrayColor.GRAYBLACK))));

				}

			}
		}
	
		super.filter = this.filter;
		Map data = readData("farmerIncome");
		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.000");

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

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

		cellFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
		List<Object[]> mainGridRows = (ArrayList) data.get(ROWS);
		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;
		else {
			for (Object[] datas : mainGridRows) {

				serialNo++;
				
				cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(serialNo), cellFont)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setNoWrap(false);
				table.addCell(cell);

				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(
								new Phrase(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(datas[8])))
										? getBranchesMap().get(getParentBranchMap().get(datas[8]))
										: getBranchesMap().get(datas[8]), cellFont));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(getBranchesMap().get(datas[8]), cellFont));
					table.addCell(cell);

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = new PdfPCell(new Paragraph(new Phrase(branchesMap.get(datas[8]), cellFont)));
						table.addCell(cell);
					}
				}
					if (!ObjectUtil.isEmpty(datas[9]) && !StringUtil.isEmpty(datas[9].toString())) {
						HarvestSeason season = farmerService.findSeasonNameByCode(datas[9].toString());
						cell = new PdfPCell(new Paragraph(new Phrase(season.getName(), cellFont)));
						table.addCell(cell);
					} 
					
					Farmer farmer =farmerService.findFarmerByFarmerId(datas[3].toString());
					cell = new PdfPCell(new Paragraph(new Phrase(datas[4].toString(), cellFont)));
					table.addCell(cell);
					cell = new PdfPCell(new Paragraph(new Phrase(datas[2].toString(), cellFont)));
					table.addCell(cell);
					if (getBranchId() != null && getBranchId().equals("organic")) {
						cell = new PdfPCell(new Paragraph(new Phrase(farmer.getIcsName(), cellFont)));
						table.addCell(cell);
					}
					cell = new PdfPCell(new Paragraph(new Phrase(farmer.getVillage().getName(), cellFont)));
					table.addCell(cell);
					double trap=0;
					double plant=0;
					double cover=0;
					double border=0;
					double inter=0;
					double main=0;
					double grossAgriIncome = 0;
					double totalIncome = 0;
					if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
						cell = new PdfPCell(new Paragraph(new Phrase(datas[5].toString(), cellFont)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
						grossAgriIncome += !ObjectUtil.isEmpty(datas[5]) && !datas[5].equals("") ? Double.valueOf((datas[5]).toString())
								: 0;
						cell = new PdfPCell(new Paragraph(new Phrase(datas[6].toString(), cellFont)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
						grossAgriIncome += !ObjectUtil.isEmpty(datas[6]) && !datas[6].equals("") ? Double.valueOf((datas[6]).toString())
								: 0;
					}
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
						List<CropSupply> cropSup=farmerService.findCropSupplyByFarmCode(datas[1].toString());
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
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(main), cellFont)));
						table.addCell(cell);
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(inter), cellFont)));
						table.addCell(cell);
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(border), cellFont)));
						table.addCell(cell);
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(cover), cellFont)));
						table.addCell(cell);
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(plant), cellFont)));
						table.addCell(cell);
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(trap), cellFont)));
						table.addCell(cell);
						/*rows.add(inter);
						rows.add(border);
						rows.add(cover);
						rows.add(plant);
						rows.add(trap);*/
					}
						cell = new PdfPCell(new Paragraph(new Phrase(datas[7].toString(), cellFont)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
						grossAgriIncome += !ObjectUtil.isEmpty(datas[7]) && !datas[7].equals("") ? Double.valueOf((datas[7]).toString())
								: 0;
						if (getCurrentTenantId().equalsIgnoreCase("pratibha")){
							cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(grossAgriIncome+main+inter+border+cover+plant+trap),cellFont)));
							table.addCell(cell);
						}
						else{
						
						
						
						cell = new PdfPCell(new Paragraph(new Phrase(String.valueOf(grossAgriIncome),cellFont)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
						}
						
						if (!getCurrentTenantId().equalsIgnoreCase("pratibha")){
							
							cell = new PdfPCell(new Paragraph(new Phrase(!ObjectUtil.isEmpty(farmer.getLoanTakenLastYear())
									? farmer.getLoanTakenLastYear() == 1 ? getText("yes") : getText("no") : "" , cellFont)));
							table.addCell(cell);
							
						}
			}
		}
		document.add(table); // Add table to document.

		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		document.close();
		fileOut.close();
		return stream;
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

		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> icsMap = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getLocaleProperty("icsName"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByAlpha(d.intValue());

			for (FarmCatalogue catalogue : farmCatalougeList) {
				if (!catalogue.getName().trim().contentEquals(ESESystem.OTHERS)) {
					icsMap.put(catalogue.getCode(), catalogue.getName());
				}
			}

		}
		icsMap.put("99", "Others");

		return icsMap;

	}
	
	public String populateData() throws Exception {

		this.filter = new Cultivation();
		/*Farmer farmer = null;
		Village village = new Village();
		Map<String, String> searchRecord = getJQGridRequestParam();
		if (!StringUtil.isEmpty(searchRecord.get("farmerName")) || !StringUtil.isEmpty(searchRecord.get("farmerCode")) ||
				!StringUtil.isEmpty(searchRecord.get("farmerId")) || !StringUtil.isEmpty(searchRecord.get("village")) || !StringUtil.isEmpty(searchRecord.get("branchId"))) {
			farmer = new Farmer();
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("farmerCode"))) {
			filter.setFarmerCode(searchRecord.get("farmerCode"));
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("farmerName"))) {
			farmer.setFirstName(searchRecord.get("farmerName")); 
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			village.setName(searchRecord.get("village"));
			farmer.setVillage(village);

		}
		
		if (!StringUtil.isEmpty(searchRecord.get("branchIdParma"))) {

			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchIdParma"));
				farmer.setBranchesList(branchList);
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchIdParma"));
				branchList.add(searchRecord.get("branchIdParma"));
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				farmer.setBranchesList(branchList);
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("subBranchIdParma")) && !searchRecord.get("subBranchIdParma").equals("0")) {
			farmer.setBranchId(searchRecord.get("subBranchIdParma"));
		}
		
		if (!ObjectUtil.isEmpty(farmer)) {
			filter.setFarmer(farmer);
		}
		*/
		/*super.filter = this.filter;
		setFilter(filter);
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), this.filter, getPage(), null);
		return sendJSONResponse(data);*/
		
		super.filter = this.filter;
		Map data = readData("farmerIncome");
		return sendJSONResponse(data);

	}
	
	public String detail()
	{	
		Cultivation culti=farmerService.findByCultivationId(id);
		double grossAgriIncome = 0;
		if (culti!=null && !ObjectUtil.isEmpty(culti)) {
			setFarmDetail(farmerService.findFarmByfarmId(culti.getFarmId()));
				grossAgriIncome = 0;
				Farm farm=new Farm();
				farm.setAgriIncome(formatter.format(Double.parseDouble(culti.getAgriIncome())));
				grossAgriIncome +=  !StringUtil.isEmpty(culti.getAgriIncome()) && culti.getAgriIncome().trim().length()>0 ? Double.valueOf(culti.getAgriIncome()):0.0;
				grossAgriIncome += !StringUtil.isEmpty(culti.getInterCropIncome()) && culti.getInterCropIncome().trim().length()>0 ? Double.valueOf(culti.getInterCropIncome()): 0;
				farm.setInterCropIncome(!StringUtil.isEmpty(culti.getInterCropIncome()) && culti.getInterCropIncome().trim().length()>0 ? culti.getInterCropIncome(): "0");
				farm.setOtherIncome(!StringUtil.isEmpty(culti.getOtherSourcesIncome())&&culti.getOtherSourcesIncome().trim().length()>0
							? formatter.format(Double.valueOf(culti.getOtherSourcesIncome())) : "0");
				grossAgriIncome += !StringUtil.isEmpty(culti.getOtherSourcesIncome()) && culti.getOtherSourcesIncome().trim().length()>0 ? Double.valueOf(culti.getOtherSourcesIncome())
							: 0;
				farm.setGrossAgriIncome(formatter.format(grossAgriIncome));	
					farmerIncomeList.add(farm);
		}	
		return DETAIL;
	}
	public String update()
	{
		if (!StringUtil.isEmpty(id)) {

			Cultivation culti=farmerService.findByCultivationId(id);
			farmerIncomeList = new ArrayList<>();
			double grossAgriIncome = 0;
			if (culti!=null && !ObjectUtil.isEmpty(culti)) {
				setFarmDetail(farmerService.findFarmByfarmId(culti.getFarmId()));
					grossAgriIncome = 0;
					Farm farm=new Farm();
					farm.setAgriIncome(formatter.format(Double.parseDouble(culti.getAgriIncome())));
					grossAgriIncome +=  !StringUtil.isEmpty(culti.getAgriIncome()) && culti.getAgriIncome().trim().length()>0 ? Double.valueOf(culti.getAgriIncome()):0.0;
					grossAgriIncome += !StringUtil.isEmpty(culti.getInterCropIncome()) && culti.getInterCropIncome().trim().length()>0 ? Double.valueOf(culti.getInterCropIncome()): 0;
					farm.setInterCropIncome(!StringUtil.isEmpty(culti.getInterCropIncome()) && culti.getInterCropIncome().trim().length()>0 ? culti.getInterCropIncome(): "0");
					farm.setOtherIncome(!StringUtil.isEmpty(culti.getOtherSourcesIncome())&&culti.getOtherSourcesIncome().trim().length()>0
								? formatter.format(Double.valueOf(culti.getOtherSourcesIncome())) : "0");
					grossAgriIncome += !StringUtil.isEmpty(culti.getOtherSourcesIncome()) && culti.getOtherSourcesIncome().trim().length()>0 ? Double.valueOf(culti.getOtherSourcesIncome())
								: 0;
					farm.setGrossAgriIncome(formatter.format(grossAgriIncome));
					farm.setCultivationId(String.valueOf(culti.getId()));
					farmerIncomeList.add(farm);
			}
			
				return UPDATE;
			

		} else {
				if (!StringUtil.isEmpty(farmerIncomeDatas) && farmerIncomeDatas.length() > 0) {
					String[] spiltArr = farmerIncomeDatas.split("@@@");
					for (int i = 0; i < spiltArr.length; i++) {
						if (spiltArr[i].length() > 0) {
							String[] farmerIncome = spiltArr[i].split("###");
							Cultivation cultivation = farmerService
									.findCultivationByCultivationId(Long.valueOf(farmerIncome[3]));
							cultivation.setAgriIncome(farmerIncome[0]);
							cultivation.setInterCropIncome(farmerIncome[2]);
							cultivation.setOtherSourcesIncome(farmerIncome[1]);
							farmerService.update(cultivation);
						}
					}
				}
		

			return REDIRECT;
		}
	
	}

	

	public List<Farm> getFarmerIncomeList() {
		return farmerIncomeList;
	}

	public void setFarmerIncomeList(List<Farm> farmerIncomeList) {
		this.farmerIncomeList = farmerIncomeList;
	}

	public String getFarmerIncomeDatas() {
		return farmerIncomeDatas;
	}

	public void setFarmerIncomeDatas(String farmerIncomeDatas) {
		this.farmerIncomeDatas = farmerIncomeDatas;
	}

	public IDeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(IDeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB+type);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+type))) {
				content = super.getText(BreadCrumb.BREADCRUMB+type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
			
		}else{
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}
	
	@Override
	public String getCurrentMenu() {
		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			if (type.equalsIgnoreCase("service")) {
				return getText("menu1.select");
			}
		}
		return getText("menu.select");

	}
	
/*	public List<String[]> getFarmerFarmerIdList() {

		List<String[]> farmerIdList = farmerService.listFarmerId();

		return farmerIdList;

	}*/

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

	public String getIncomeStr() {
		return incomeStr;
	}

	public void setIncomeStr(String incomeStr) {
		this.incomeStr = incomeStr;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public Farm getFarmDetail() {
		return farmDetail;
	}

	public void setFarmDetail(Farm farmDetail) {
		this.farmDetail = farmDetail;
	}
	
	
}
