package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

@SuppressWarnings("serial")
public class ProcurementTraceabilityReportAction extends BaseReportAction {

	private ProcurementTraceabilityDetails procurementTraceabilityDetails;
	private ProcurementTraceabilityDetails filter;

	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IProductService productService;
	
	private String farmerName;
	private String exportLimit;
	private String daterange;
	private String branchIdParma;
	private String icsString;

	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private String imagesId;
	private String product;
	private String warehouse;
	private String ics;
	private String village;
	private String city;
	private String fCooperative;
	private String selectedSeason;
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

		request.setAttribute(HEADING, getText(LIST));
		filter = new ProcurementTraceabilityDetails();
		return LIST;
	}

	public String data() throws Exception {

		setFilter(new ProcurementTraceabilityDetails());
		Farmer f = new Farmer();
		ProcurementTraceability p = new ProcurementTraceability();
		ProcurementProduct pp = new ProcurementProduct();
		if(selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)){
			p.setSeason(selectedSeason);
		}
		else{
		p.setSeason(getCurrentSeasonsCode());
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			p.setBranchId(branchIdParma);
			filter.setProcurementTraceability(p);
			// filter.getProcurementTraceability().setBranchId(branchIdParma);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			f.setFirstName(farmerName);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}

		if (!StringUtil.isEmpty(product)) {
			pp.setName(product);
			filter.setProcurementProduct(pp);
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
			filter.getProcurementTraceability().setBranchId(subBranchIdParma);
		}

		if (!StringUtil.isEmpty(warehouse)) {
			Warehouse w = new Warehouse();
			w.setCode(warehouse);
			p.setWarehouse(w);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(ics)){
			if(ObjectUtil.isEmpty(f))
				f=new Farmer();
			f.setIcsName(ics);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(village)){
			if(ObjectUtil.isEmpty(f))
				f=new Farmer();
			Village v= new Village();
			v.setCode(village);
			f.setVillage(v);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(city)){
			if(ObjectUtil.isEmpty(city))
				f=new Farmer();
			Municipality c=new Municipality();
			c.setCode(city);
			f.setCity(c);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(fCooperative)){
			if(ObjectUtil.isEmpty(fCooperative))
				f=new Farmer();
			f.setFpo(fCooperative);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		filter.setProcurementTraceability(p);
		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		DecimalFormat formatter = new DecimalFormat("0.00");
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (obj instanceof ProcurementTraceabilityDetails) {
			
			ProcurementTraceabilityDetails procurementTraceDet = (ProcurementTraceabilityDetails) obj;

			if (!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())) {
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap()
								.get(procurementTraceDet.getProcurementTraceability().getBranchId())))
										? getBranchesMap().get(getParentBranchMap()
												.get(procurementTraceDet.getProcurementTraceability().getBranchId()))
										: getBranchesMap()
												.get(procurementTraceDet.getProcurementTraceability().getBranchId()));
					}
					rows.add(getBranchesMap().get(procurementTraceDet.getProcurementTraceability().getBranchId()));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(procurementTraceDet.getProcurementTraceability().getBranchId()));
					}
				}
				if(procurementTraceDet.getProcurementTraceability().getSeason()!=null && !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getSeason())){
				HarvestSeason hs=farmerService.findSeasonNameByCode(procurementTraceDet.getProcurementTraceability().getSeason());
					rows.add(hs!=null && !ObjectUtil.isEmpty(hs)?hs.getName():"");
				}
				else
					rows.add("");
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
							? (!StringUtil.isEmpty(genDate
									.format(procurementTraceDet.getProcurementTraceability().getProcurementDate()))
											? genDate.format(procurementTraceDet.getProcurementTraceability()
													.getProcurementDate())
											: "")
							: "");
				}

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())
						? procurementTraceDet.getProcurementTraceability().getFarmer().getFirstName() : "");
				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()) && !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getVillage())
						? procurementTraceDet.getProcurementTraceability().getFarmer().getVillage().getName():"");
				
				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()) && !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getCity())
						? procurementTraceDet.getProcurementTraceability().getFarmer().getCity().getName():"");
				rows.add(!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo())?!ObjectUtil.isEmpty(catalogueService.findCatalogueByCode(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo()))?catalogueService.findCatalogueByCode(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo()).getName():"":"");

				if (!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())) {
					FarmCatalogue catalogue = getCatlogueValueByCode(
							procurementTraceDet.getProcurementTraceability().getFarmer().getIcsName());
					if (!ObjectUtil.isEmpty(catalogue)) {
						icsString = catalogue.getName();
					}
				}

				rows.add(!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getIcsName())
						? icsString : "");

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getWarehouse())
						? procurementTraceDet.getProcurementTraceability().getWarehouse().getName() : "");

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementProduct())
						? procurementTraceDet.getProcurementProduct().getName() : "");

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade())
						? procurementTraceDet.getProcuremntGrade().getName() : "");

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade()) && !ObjectUtil.isEmpty( procurementTraceDet.getProcuremntGrade().getProcurementVariety()) ? procurementTraceDet.getProcuremntGrade().getProcurementVariety().getName() : "");
				
				rows.add(!StringUtil.isEmpty(procurementTraceDet.getPrice())
						? procurementTraceDet.getPrice() : "");

				rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getReceiptNo())
						? procurementTraceDet.getProcurementTraceability().getReceiptNo() : "");

				
				
				//rows.add(!StringUtil.isEmpty(procurementTraceDet.getUnit()) ? procurementTraceDet.getUnit() : "");

				rows.add(!StringUtil.isEmpty(procurementTraceDet.getNumberOfBags())
						? procurementTraceDet.getNumberOfBags() : "");
				
				rows.add(!StringUtil.isEmpty(procurementTraceDet.getNetWeight()) ? procurementTraceDet.getNetWeight()+"-"+procurementTraceDet.getUnit()
				: "");
				//rows.add(procurementTraceDet.getTotalPrice());
				rows.add(procurementTraceDet.getPremiumPrice());
				rows.add(procurementTraceDet.getTotalPricepremium());
				/*rows.add(!StringUtil.isEmpty(procurementTraceDet.getPremiumPrice())
						? procurementTraceDet.getPremiumPrice() : "");*/

				/*rows.add(!StringUtil.isEmpty(procurementTraceDet.getTotalPrice()) ? procurementTraceDet.getTotalPrice()
						: "");*/

				/*rows.add(!StringUtil.isEmpty(procurementTraceDet.getTotalPricepremium())
						? procurementTraceDet.getTotalPricepremium() : "");*/

				rows.add((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getTrash()))
								? procurementTraceDet.getProcurementTraceability().getTrash() : "");

				rows.add((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getMoisture()))
								? procurementTraceDet.getProcurementTraceability().getMoisture() : "");

				rows.add((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getStapleLen()))
								? procurementTraceDet.getProcurementTraceability().getStapleLen() : "");

				rows.add((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getKowdi_kapas()))
								? procurementTraceDet.getProcurementTraceability().getKowdi_kapas() : "");

				rows.add((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getYellow_cotton()))
								? procurementTraceDet.getProcurementTraceability().getYellow_cotton() : "");

				if (!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability()) && !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getStripImage())) {
					imagesId = "";
					imagesId += String.valueOf(procurementTraceDet.getProcurementTraceability().getId()) + ",";

					rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
							+ StringUtil.removeLastComma(imagesId) + "')\"></button>");
				} else {
					rows.add("<button class='no-imgIcn'></button>");
				}

			}
				else{
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap()
									.get(procurementTraceDet.getProcurementTraceability().getBranchId())))
											? getBranchesMap().get(getParentBranchMap()
													.get(procurementTraceDet.getProcurementTraceability().getBranchId()))
											: getBranchesMap()
													.get(procurementTraceDet.getProcurementTraceability().getBranchId()));
						}
						rows.add(getBranchesMap().get(procurementTraceDet.getProcurementTraceability().getBranchId()));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							rows.add(branchesMap.get(procurementTraceDet.getProcurementTraceability().getBranchId()));
						}
					}
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								? (!StringUtil.isEmpty(genDate
										.format(procurementTraceDet.getProcurementTraceability().getProcurementDate()))
												? genDate.format(procurementTraceDet.getProcurementTraceability()
														.getProcurementDate())
												: "")
								: "");
					}
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getWarehouse())
							? procurementTraceDet.getProcurementTraceability().getWarehouse().getName() : "");
					
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()) && !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getVillage())
							? procurementTraceDet.getProcurementTraceability().getFarmer().getVillage().getName():"");
					
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())
							? procurementTraceDet.getProcurementTraceability().getFarmer().getFirstName() : "");
					
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())
							? procurementTraceDet.getProcurementTraceability().getFarmer().getFarmerCode() : "");
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementProduct())
							? procurementTraceDet.getProcurementProduct().getName() : "");

					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade())
							&& !ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade().getProcurementVariety())
									? procurementTraceDet.getProcuremntGrade().getProcurementVariety().getName() : "");
					
					rows.add(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade())
							? procurementTraceDet.getProcuremntGrade().getName() : "");
					
					rows.add(procurementTraceDet.getTotalPricepremium());
					
					rows.add(!StringUtil.isEmpty(procurementTraceDet.getNetWeight())
							? procurementTraceDet.getNetWeight() : "");
					rows.add(!StringUtil.isEmpty(procurementTraceDet.getNumberOfBags())
							? procurementTraceDet.getNumberOfBags() : "");
					if (!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability()) && !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getStripImage())) {
						imagesId = "";
						imagesId += String.valueOf(procurementTraceDet.getProcurementTraceability().getId()) + ",";

						rows.add("<button class=\"fa fa-picture-o\"\"aria-hidden=\"true\"\" onclick=\"popupWindow('"
								+ StringUtil.removeLastComma(imagesId) + "')\"></button>");
					} else {
						rows.add("<button class='no-imgIcn'></button>");
					}				
				}
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					rows = getLivelihoodTenantId(rows, procurementTraceDet);
				}
			}
			jsonObject.put("id", procurementTraceDet.getId());
			jsonObject.put("cell", rows);
			
			
		}
		return jsonObject;
	}

	public Map<String, String> getFarmerNameList() {
		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();
		List<Object[]> farmersList = farmerService.listFarmerInfo();
		/*farmerListMap = farmersList.stream()
				.collect(Collectors.toMap(obj -> String.valueOf(obj[1]), obj -> String.valueOf(obj[3])));
*/
		farmersList.stream().filter(p-> p[1]!=null && !ObjectUtil.isEmpty(p[1]) && p[3]!=null && !ObjectUtil.isEmpty(p[3])).forEach(f->{
			farmerListMap.put(String.valueOf(f[1]),  String.valueOf(f[3]));
		});
		Map<String, String> orderedFarmerMap = farmerListMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		return orderedFarmerMap;
	}

	public Map<String, String> getProductList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();

		List<ProcurementProduct> productList = productDistributionService.listProcurementProduct();
		if (!ObjectUtil.isListEmpty(productList)) {
			for (ProcurementProduct product : productList) {

				returnMap.put(product.getName(), product.getName());
			}
		}
		return returnMap;
	}
	
	public Map<String, String> getIcsList() {
		Map<String, String> icsList = new LinkedHashMap<String, String>();

		List<Object[]> ics = catalogueService.loadICSName();
		for (Object[] obj : ics) {
			icsList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return icsList;
	}
	
	public Map<String, String> getVillageList(){
		Map<String,String> villageMap = new LinkedHashMap<>();
		List<Object[]> villageList=farmerService.listProcurementTraceabilityVillage();
		if(!ObjectUtil.isListEmpty(villageList)){
			for(Object[] o:villageList)
				villageMap.put(o[0].toString(), o[1].toString());
		}
		return villageMap;
	}
	
	public Map<String, String> getSeasonList(){
		Map<String, String> seasonMap = new LinkedHashMap<>();
		List<HarvestSeason> seasonList=farmerService.listHarvestSeasons();
		if(!ObjectUtil.isListEmpty(seasonList)){
			for(HarvestSeason o:seasonList)
				seasonMap.put(o.getCode(),o.getName());
		}
		return seasonMap;
	}
	
	public Map<String, String> getCityList(){
		Map<String, String> cityMap = new LinkedHashMap<>();
		List<Object[]> cityList=farmerService.listProcurementTraceabilityCity();
		if(!ObjectUtil.isListEmpty(cityList)){
			for(Object[] o:cityList)
				cityMap.put(o[0].toString(), o[1].toString());
		}
		return cityMap;
	}
	public Map<String, String> getFCooperativeList(){
		Map<String, String>fCoopMap=new LinkedHashMap<>();
		List<Object> fCoopLsit=farmerService.listFarmerFpo();
		if(!ObjectUtil.isListEmpty(fCoopLsit)){
			for(Object o:fCoopLsit)
				if(!ObjectUtil.isEmpty(o) && !StringUtil.isEmpty(o.toString())){
					FarmCatalogue f= catalogueService.findCatalogueByCode(o.toString());
					fCoopMap.put(o.toString(),f.getName());
				}
		}
		return fCoopMap;
	}

	public Map<String, String> getWarehouseList() {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();

		List<Object[]> wareList = locationService.listOfWarehouses();
		if (!ObjectUtil.isListEmpty(wareList)) {
			for (Object[] obj : wareList) {

				returnMap.put(obj[0].toString(), obj[1].toString());
			}
		}
		return returnMap;
	}

	public String populateImage() {

		try {
			setId(id);
			Object imageDetail = clientService.findProcurementTraceabilityImgById(Long.valueOf(id));
			byte[] image = (byte[]) imageDetail;
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
    public void populateFarmerMethod() throws IOException
    {
    	JSONArray jsonArray = new JSONArray();
    	JSONObject jsonObject = new JSONObject();
    	selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
    	Object[] datas=productService.findTotalQtyAndAmt(farmerName,product,warehouse,branchIdParma,ics,village,city,fCooperative,selectedSeason,DateUtil.convertStringToDate(startDate, DateUtil.DATE_FORMAT),DateUtil.convertStringToDate(endDate, DateUtil.DATE_FORMAT));
    	jsonObject.put("totalQty", !StringUtil.isEmpty(datas[0])?datas[0]:"0.0");
		jsonObject.put("totalAmt",  !StringUtil.isEmpty(datas[1])?datas[1]:"0.0");
		jsonObject.put("totalNoOfBags",  !StringUtil.isEmpty(datas[2])?datas[2]:"0.0");
		
		jsonArray.add(jsonObject);
    	
		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
    	
    }

	public String populateXLS() throws Exception {
		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("procurementTraceability") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(
				FileUtil.createFileInputStreamToZipFile(getText("procurementTraceability"), fileMap, ".xls"));
		return "xls";
	}

	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(getGeneralDateFormat());
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportProcurementTraceabilityTitle"));
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();

		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style2 = wb.createCellStyle();
		HSSFCellStyle style3 = wb.createCellStyle();
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
				filterRow6 = null, filterRow7 = null,filterRow8 = null,filterRow9 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
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
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportProcurementTraceabilityTitle")));
		cell.setCellStyle(style1);
		font1.setBoldweight((short) 22);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		if (isMailExport()) {
			rowNum++;
			rowNum++;
			filterRowTitle = sheet.createRow(rowNum++);
			cell = filterRowTitle.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("filter")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
			filterRow6 = sheet.createRow(rowNum++);
			filterRow7 = sheet.createRow(rowNum++);
			selectedSeason=selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)?selectedSeason:getCurrentSeasonsCode();
			if ((getIsMultiBranch().equalsIgnoreCase("1")
					&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
				if (StringUtil.isEmpty(branchIdValue)) {
					filterRow4 = sheet.createRow(rowNum++);
					cell = filterRow4.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("mainOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow4.createCell(2);
					cell.setCellValue(new HSSFRichTextString(
							getBranchesMap().get(getParentBranchMap().get(filter.getProcurementTraceability().getBranchId()))));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					filterRow5 = sheet.createRow(rowNum++);
					cell = filterRow5.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow5.createCell(2);
					cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

				} else {
					if (!StringUtil.isEmpty(getBranchesMap().get(getSubBranchIdParma()))) {
						filterRow6 = sheet.createRow(rowNum++);
						cell = filterRow6.createCell(1);
						cell.setCellValue(new HSSFRichTextString(getLocaleProperty("subOrganization")));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);

						cell = filterRow6.createCell(2);
						cell.setCellValue(new HSSFRichTextString(getBranchesMap().get(getSubBranchIdParma())));
						filterFont.setBoldweight((short) 12);
						filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						filterStyle.setFont(filterFont);
						cell.setCellStyle(filterStyle);
					}
				}

			} else {
				if (!StringUtil.isEmpty(branchIdParma)) {
					filterRow7 = sheet.createRow(rowNum++);
					cell = filterRow7.createCell(1);
					cell.setCellValue(new HSSFRichTextString(getText("organization")));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);

					cell = filterRow7.createCell(2);
					BranchMaster branch = clientService.findBranchMasterByBranchId(branchIdParma);
					cell.setCellValue(new HSSFRichTextString((!ObjectUtil.isEmpty(branch) ? branch.getName() : "")));
					// cell.setCellValue(new HSSFRichTextString(branchIdParma));
					filterFont.setBoldweight((short) 12);
					filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					filterStyle.setFont(filterFont);
					cell.setCellStyle(filterStyle);
				}

			}
			
			filterRow8 = sheet.createRow(rowNum++);

			cell = filterRow8.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("StartingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			cell = filterRow8.createCell(2);
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(getsDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow9 = sheet.createRow(rowNum++);

			cell = filterRow9.createCell(1);
			cell.setCellValue(new HSSFRichTextString(getLocaleProperty("EndingDate")));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			if (!ObjectUtil.isEmpty(geteDate())) {
				cell = filterRow9.createCell(2);
				cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(farmerName)) {
				filterRow1 = sheet.createRow(rowNum++);
				cell = filterRow1.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("farmerName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow1.createCell(2);
				Farmer farmer = farmerService.findFarmerByFarmerId(farmerName);
				cell.setCellValue(new HSSFRichTextString(farmer.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(warehouse)) {
				filterRow2 = sheet.createRow(rowNum++);
				cell = filterRow2.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("procurementCenter")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow2.createCell(2);
				Warehouse w= locationService.findWarehouseByCode(warehouse);
				cell.setCellValue(new HSSFRichTextString(w.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
						
			if (!StringUtil.isEmpty(product)) {
				filterRow3 = sheet.createRow(rowNum++);
				cell = filterRow3.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("product")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow3.createCell(2);
				cell.setCellValue(new HSSFRichTextString(product));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
						
			if (!StringUtil.isEmpty(village)) {
				filterRow4 = sheet.createRow(rowNum++);
				cell = filterRow4.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("village")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow4.createCell(2);
				Village v = locationService.findVillageByCode(village);
				cell.setCellValue(new HSSFRichTextString(v.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
					
			if (!StringUtil.isEmpty(ics)) {
				filterRow5 = sheet.createRow(rowNum++);
				cell = filterRow5.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("icsName")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow5.createCell(2);
				FarmCatalogue catalogue = getCatlogueValueByCode(ics);
				cell.setCellValue(new HSSFRichTextString(catalogue.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(fCooperative)) {
				filterRow6 = sheet.createRow(rowNum++);
				cell = filterRow6.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("cooperative.name")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow6.createCell(2);
				FarmCatalogue catalogue = getCatlogueValueByCode(fCooperative);
				cell.setCellValue(new HSSFRichTextString(catalogue.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			
			if (!StringUtil.isEmpty(city)) {
				filterRow7 = sheet.createRow(rowNum++);
				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("city")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				Municipality catalogue = locationService.findMunicipalityByCode(city);
				cell.setCellValue(new HSSFRichTextString(catalogue.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
			if (!StringUtil.isEmpty(selectedSeason)) {
				filterRow7 = sheet.createRow(rowNum++);
				cell = filterRow7.createCell(1);
				cell.setCellValue(new HSSFRichTextString(getLocaleProperty("season")));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);

				cell = filterRow7.createCell(2);
				HarvestSeason hs=farmerService.findSeasonNameByCode(selectedSeason);
				cell.setCellValue(new HSSFRichTextString(hs.getName()));
				filterFont.setBoldweight((short) 12);
				filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				filterStyle.setFont(filterFont);
				cell.setCellStyle(filterStyle);
			}
			}
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = "";

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnProcurementTraceabilityBranch");
			} else if (!StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("ExportColumnProcurementTraceability");
			}
		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				mainGridColumnHeaders = getLocaleProperty("exportProcurementTraceabilityColumnHeaderBranch");
			} else {
				mainGridColumnHeaders = getLocaleProperty("exportProcurementTraceabilityColumnHeader");
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
				style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cell.setCellStyle(style2);
				font2.setBoldweight((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				style2.setFont(font2);
				sheet.setColumnWidth(mainGridIterator, (15 * 550));
				mainGridIterator++;
			} else {
				if (!cellHeader.equalsIgnoreCase("Organization")) {
					cell = mainGridRowHead.createCell(mainGridIterator);
					cell.setCellValue(new HSSFRichTextString(cellHeader));
					style2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
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

		setFilter(new ProcurementTraceabilityDetails());
		
		Farmer f = new Farmer();
		ProcurementTraceability p = new ProcurementTraceability();
		ProcurementProduct pp = new ProcurementProduct();
		
		if(selectedSeason!=null && !StringUtil.isEmpty(selectedSeason)){
			p.setSeason(selectedSeason);
		}
		else{
		p.setSeason(getCurrentSeasonsCode());
		}
		if (!StringUtil.isEmpty(branchIdParma)) {
			p.setBranchId(branchIdParma);
			filter.setProcurementTraceability(p);
			// filter.getProcurementTraceability().setBranchId(branchIdParma);
		}

		if (!StringUtil.isEmpty(farmerName)) {
			f.setFirstName(farmerName);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}

		if (!StringUtil.isEmpty(product)) {
			pp.setName(product);
			filter.setProcurementProduct(pp);
		}
		
		if(!StringUtil.isEmpty(fCooperative)){
			if(ObjectUtil.isEmpty(fCooperative))
				f=new Farmer();
			f.setFpo(fCooperative);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
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
			filter.getProcurementTraceability().setBranchId(subBranchIdParma);
		}
		
		if (!StringUtil.isEmpty(warehouse)) {
			Warehouse w = new Warehouse();
			w.setCode(warehouse);
			p.setWarehouse(w);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(ics)){
			if(ObjectUtil.isEmpty(f))
				f=new Farmer();
			f.setIcsName(ics);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(village)){
			if(ObjectUtil.isEmpty(f))
				f=new Farmer();
			Village v= new Village();
			v.setCode(village);
			f.setVillage(v);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(city)){
			if(ObjectUtil.isEmpty(city))
				f=new Farmer();
			Municipality c=new Municipality();
			c.setCode(city);
			f.setCity(c);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		if(!StringUtil.isEmpty(fCooperative)){
			if(ObjectUtil.isEmpty(fCooperative))
				f=new Farmer();
			f.setFpo(fCooperative);
			p.setFarmer(f);
			filter.setProcurementTraceability(p);
		}
		filter.setProcurementTraceability(p);
		super.filter = this.filter;

		Map data = isMailExport() ? readData() : readExportData();

		List<ProcurementTraceabilityDetails> mainGridRows = (List<ProcurementTraceabilityDetails>) data.get(ROWS);

		Long serialNumber = 0L;

		if (ObjectUtil.isListEmpty(mainGridRows))
			return null;

		DecimalFormat formatter = new DecimalFormat("0.00");
		DecimalFormat qtyFormat = new DecimalFormat("0.000");

		for (ProcurementTraceabilityDetails procurementTraceDet : mainGridRows) {
			if (isMailExport() && flag) {

				row = sheet.createRow(rowNum++);
				colNum = 0;

				serialNumber++;
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						String.valueOf(serialNumber) != null ? String.valueOf(serialNumber) : ""));
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
					if ((getIsMultiBranch().equalsIgnoreCase("1")
							&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(
									new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap()
											.get(procurementTraceDet.getProcurementTraceability().getBranchId())))
													? getBranchesMap().get(getParentBranchMap().get(
															procurementTraceDet.getProcurementTraceability().getBranchId()))
													: getBranchesMap().get(procurementTraceDet.getProcurementTraceability()
															.getBranchId())));
						}
						cell = row.createCell(colNum++);
						cell.setCellValue(
								getBranchesMap().get(procurementTraceDet.getProcurementTraceability().getBranchId()));

					} else {
						if (StringUtil.isEmpty(branchIdValue)) {
							cell = row.createCell(colNum++);
							cell.setCellValue(new HSSFRichTextString(
									!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getBranchId())
											? (branchesMap
													.get(procurementTraceDet.getProcurementTraceability().getBranchId()))
											: ""));
						}
					}
					
					ESESystem preferences = preferncesService.findPrefernceById("1");

					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());

						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(!ObjectUtil
								.isEmpty(procurementTraceDet.getProcurementTraceability())
										? (!StringUtil.isEmpty(genDate
												.format(procurementTraceDet.getProcurementTraceability()
														.getProcurementDate()))
																? genDate.format(
																		procurementTraceDet.getProcurementTraceability()
																				.getProcurementDate())
																: "")
										: ""));
					}
					
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
									&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getWarehouse()))
											? procurementTraceDet.getProcurementTraceability().getWarehouse().getName()
											: ""));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getVillage())
									? procurementTraceDet.getProcurementTraceability().getFarmer().getVillage().getName() : ""));
					
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())
									? procurementTraceDet.getProcurementTraceability().getFarmer().getFirstName() : ""));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
									&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()))
											? procurementTraceDet.getProcurementTraceability().getFarmer().getFarmerCode() : ""));
					

					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementProduct())
									? procurementTraceDet.getProcurementProduct().getName() : ""));

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade()) && !ObjectUtil.isEmpty( procurementTraceDet.getProcuremntGrade().getProcurementVariety()) ? procurementTraceDet.getProcuremntGrade().getProcurementVariety().getName() : ""));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade())
							? procurementTraceDet.getProcuremntGrade().getName() : ""));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(String.valueOf(procurementTraceDet.getTotalPricepremium())));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(String.valueOf(procurementTraceDet.getNetWeight())));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString(String.valueOf(procurementTraceDet.getNumberOfBags()) ));
	
					
				}else{
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(
								new HSSFRichTextString(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap()
										.get(procurementTraceDet.getProcurementTraceability().getBranchId())))
												? getBranchesMap().get(getParentBranchMap().get(
														procurementTraceDet.getProcurementTraceability().getBranchId()))
												: getBranchesMap().get(procurementTraceDet.getProcurementTraceability()
														.getBranchId())));
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(
							getBranchesMap().get(procurementTraceDet.getProcurementTraceability().getBranchId()));

				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						cell = row.createCell(colNum++);
						cell.setCellValue(new HSSFRichTextString(
								!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getBranchId())
										? (branchesMap
												.get(procurementTraceDet.getProcurementTraceability().getBranchId()))
										: ""));
					}
				}
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				cell = row.createCell(colNum++);
				if(procurementTraceDet.getProcurementTraceability().getSeason()!=null && !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getSeason())){
					HarvestSeason hs=farmerService.findSeasonNameByCode(procurementTraceDet.getProcurementTraceability().getSeason());
					
					cell.setCellValue(new HSSFRichTextString(hs!=null && !ObjectUtil.isEmpty(hs)?hs.getName():""));
					}
					else
						cell.setCellValue(new HSSFRichTextString(""));
				
				}

				ESESystem preferences = preferncesService.findPrefernceById("1");

				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());

					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(!ObjectUtil
							.isEmpty(procurementTraceDet.getProcurementTraceability())
									? (!StringUtil.isEmpty(genDate
											.format(procurementTraceDet.getProcurementTraceability()
													.getProcurementDate()))
															? genDate.format(
																	procurementTraceDet.getProcurementTraceability()
																			.getProcurementDate())
															: "")
									: ""));
				}

				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()))
										? procurementTraceDet.getProcurementTraceability().getFarmer().getName() : ""));
				
				if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
					cell = row.createCell(colNum++);
					cell.setCellValue(
							new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
									&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer()))
											? procurementTraceDet.getProcurementTraceability().getFarmer().getFarmerCode() : ""));
					
				}

				if (!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
						&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer())) {
					FarmCatalogue catalogue = getCatlogueValueByCode(
							procurementTraceDet.getProcurementTraceability().getFarmer().getIcsName());
					if (!ObjectUtil.isEmpty(catalogue)) {
						icsString = catalogue.getName();
					}
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getVillage())
									? procurementTraceDet.getProcurementTraceability().getFarmer().getVillage().getName() : ""));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getCity())
									? procurementTraceDet.getProcurementTraceability().getFarmer().getCity().getName() : ""));
					if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo())
									? !ObjectUtil.isEmpty(catalogueService.findCatalogueByCode(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo()))?catalogueService.findCatalogueByCode(procurementTraceDet.getProcurementTraceability().getFarmer().getFpo()).getName() : "":""));
					}
				}
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						!StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getFarmer().getIcsName())
								? icsString : ""));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getWarehouse()))
										? procurementTraceDet.getProcurementTraceability().getWarehouse().getName()
										: ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementProduct())
								? procurementTraceDet.getProcurementProduct().getName() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade())
						? procurementTraceDet.getProcuremntGrade().getName() : ""));

				

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(!ObjectUtil.isEmpty(procurementTraceDet.getProcuremntGrade()) && !ObjectUtil.isEmpty( procurementTraceDet.getProcuremntGrade().getProcurementVariety()) ? procurementTraceDet.getProcuremntGrade().getProcurementVariety().getName() : ""));
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(procurementTraceDet.getPrice())));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getReceiptNo()))
										? procurementTraceDet.getProcurementTraceability().getReceiptNo() : ""));

				
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(String.valueOf(procurementTraceDet.getNumberOfBags()) ));
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(procurementTraceDet.getNetWeight()+"-"+procurementTraceDet.getUnit()
				));
				/*cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(String.valueOf(procurementTraceDet.getTotalPrice())));*/
				if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(String.valueOf(procurementTraceDet.getPremiumPrice())));
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(String.valueOf(procurementTraceDet.getTotalPricepremium())));
			/*	cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString(String.valueOf(procurementTraceDet.getPremiumPrice())));*/
				
				
				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getTrash()))
										? procurementTraceDet.getProcurementTraceability().getTrash() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getMoisture()))
										? procurementTraceDet.getProcurementTraceability().getMoisture() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(
						new HSSFRichTextString((!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())
								&& !StringUtil.isEmpty(procurementTraceDet.getProcurementTraceability().getStapleLen()))
										? procurementTraceDet.getProcurementTraceability().getStapleLen() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability()) && !StringUtil
								.isEmpty(procurementTraceDet.getProcurementTraceability().getKowdi_kapas()))
										? procurementTraceDet.getProcurementTraceability().getKowdi_kapas() : ""));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(
						(!ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability()) && !StringUtil
								.isEmpty(procurementTraceDet.getProcurementTraceability().getYellow_cotton()))
										? procurementTraceDet.getProcurementTraceability().getYellow_cotton() : ""));
				}
			}
		  }
		}

		for (int i = 0; i <= colNum; i++) {
			sheet.autoSizeColumn(i);
		}

		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(100, 150, 900, 100, (short) 0, 0, (short) 0, 4);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		// picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("procurementTraceability") + fileNameDateFormat.format(new Date()) + ".xls";
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

	public ProcurementTraceabilityDetails getProcurementTraceabilityDetails() {
		return procurementTraceabilityDetails;
	}

	public void setProcurementTraceabilityDetails(ProcurementTraceabilityDetails procurementTraceabilityDetails) {
		this.procurementTraceabilityDetails = procurementTraceabilityDetails;
	}

	public ProcurementTraceabilityDetails getFilter() {
		return filter;
	}

	public void setFilter(ProcurementTraceabilityDetails filter) {
		this.filter = filter;
	}

	public String getBranchIdParma() {
		return branchIdParma;
	}

	public void setBranchIdParma(String branchIdParma) {
		this.branchIdParma = branchIdParma;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
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

	public String getImagesId() {
		return imagesId;
	}

	public void setImagesId(String imagesId) {
		this.imagesId = imagesId;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getIcsString() {
		return icsString;
	}

	public void setIcsString(String icsString) {
		this.icsString = icsString;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public String getIcs() {
		return ics;
	}

	public void setIcs(String ics) {
		this.ics = ics;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFCooperative() {
		return fCooperative;
	}

	public void setFCooperative(String fCooperative) {
		this.fCooperative = fCooperative;
	}


	public String getSelectedSeason() {
		return selectedSeason;
	}

	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}
	
	public JSONArray getLivelihoodTenantId(JSONArray rows, ProcurementTraceabilityDetails procurementTraceDet) {
		ProcurementTraceability ptcbt = null;
		String latitude = "", longitude = "";
		if (!ObjectUtil.isEmpty(procurementTraceDet)
				&& !ObjectUtil.isEmpty(procurementTraceDet.getProcurementTraceability())) {
			ptcbt = procurementTraceDet.getProcurementTraceability();
			if (!StringUtil.isEmpty(ptcbt.getLatitude()) && !StringUtil.isEmpty(ptcbt.getLongitude())) {
				latitude = ptcbt.getLatitude();
				longitude = ptcbt.getLongitude();
				rows.add("<button class=\"fa faMap\"\"aria-hidden=\"true\"\" onclick=\"popupMapWindow(" + latitude
						+ "," + longitude + "," + "''" + ")" + "\"></button>");
			}
		}
		return rows;
	}

}
