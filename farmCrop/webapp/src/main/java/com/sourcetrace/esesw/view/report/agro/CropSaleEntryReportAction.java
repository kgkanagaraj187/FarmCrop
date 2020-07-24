package com.sourcetrace.esesw.view.report.agro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.IExporter;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class CropSaleEntryReportAction extends SwitchValidatorAction {

	/**
	* 
	*/
	private static final long serialVersionUID = 7954363062272216310L;
	private static final Logger LOGGER = Logger.getLogger(CropSaleEntryReportAction.class);
	private List<String> crops = new ArrayList<String>();

	private CropSupply filter;

	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	private String saleDate;
	private String farmerName;
	private String command;
	private String farmName;
	private String supplyDeatailJsonString;
	private String cropType;
	private String cropTypeCode;
	private String cropId;
	private String varietyId;
	private long cropSupplyId;
	private double qty;
	private long supplyId;
	private long templateId;
	private double price;
	private String farmCode;
	private String id;
	private String currentPage;
	private ILocationService locationService;
	private IFarmerService farmerService;
	private String farmerId;
	private String farmId;
	private String village;
	private String xlsFileName;
	private String buyerInfo;
	private Customer customer;
	private String totalSaleQty;
	private String totalSalePrice;
	private IClientService clientService;
	private InputStream fileInputStream;
	private String coOperative;
	private String productAvailableUnit;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private IProductService productService;
	private IProductDistributionService productDistributionService;

	@Autowired
	private IAccountService accountService;
	private IPreferencesService preferncesService;
	Map<Integer, String> cashType = new LinkedHashMap<Integer, String>();

	private String selectedBuyerValue;
	private String cashCreditValue;
	private String paymentMode;
	private String creditAmount;

	private ESEAccount account;
	Map<String, String> cooperative = new LinkedHashMap<String, String>();

	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private CropSupply cropSupply;
	private List<CropSupplyDetails> cropSaleDetailsList;
	public static final String NO_RECORD = "No_Records_Present";
	public static final String INSUFFICIENT_BAL = "There is no sufficient balance in buyer account";
	private String selectedVillage;
	private String selectedFarmer;
	private String selectedFarm;
	private String selectedCropType;
	private String selectedCropName;
	private String selectedVariety;
	private String selectedBuyer;
	private String batchNo;
	private String sellPrice;
	private String productTotalString;
	private boolean mailExport;
	private String dateOfSale;
	protected static final String FILTERDATE = "MM/dd/yyyy";
	private Properties errors;
	protected String sidx;
	protected String sord;
	protected int rows;
	protected Date sDate = null;
	protected Date eDate = null;
	private String selectedCrop;
	private File cropSaleImage;
	private File cropSaleImage1;
	private CropSupplyImages cropSupplyImages;
	private String cropSaleDdataJsonString;
	
    private String vehicleNo;
	private String transportDetail;
	private String receiptInfor;
	private String paymentInfo;
	private String poNumber;
	
	
	@Autowired
	private ICatalogueService catalogueService;

	public String getProductTotalString() {
		return productTotalString;
	}

	public void setProductTotalString(String productTotalString) {
		this.productTotalString = productTotalString;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public List<FarmCrops> getVarietysList() {
		return varietysList;
	}

	public void setVarietysList(List<FarmCrops> varietysList) {
		this.varietysList = varietysList;
	}

	public List<ProcurementGrade> getGradesList() {
		return gradesList;
	}

	public void setGradesList(List<ProcurementGrade> gradesList) {
		this.gradesList = gradesList;
	}

	public List<FarmCrops> getCropNamesList() {
		return cropNamesList;
	}

	public void setCropNamesList(List<FarmCrops> cropNamesList) {
		this.cropNamesList = cropNamesList;
	}

	public List<Farm> getFarms() {
		return farms;
	}

	public void setFarms(List<Farm> farms) {
		this.farms = farms;
	}

	private String searchPage;

	public String getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(String searchPage) {
		this.searchPage = searchPage;
	}

	private String quantity;
	private String totalPrice;
	private String transportName;
	private String vechicleNo;
	private String invoiceNo;
	private String payment;
	private String totalQty;
	List<Farmer> farmers = new ArrayList<>();

	public List<Farmer> getFarmers() {
		return farmers;
	}

	public void setFarmers(List<Farmer> farmers) {
		this.farmers = farmers;
	}

	List<FarmCrops> varietysList = new ArrayList<FarmCrops>();
	private String selectedGrade;
	List<ProcurementGrade> gradesList = new ArrayList<ProcurementGrade>();
	List<FarmCrops> cropNamesList = new ArrayList<FarmCrops>();
	List<Farm> farms = new ArrayList<>();

	/**
	 * List.
	 * 
	 * @return the string
	 * @see com.sourcetrace.esesw.view.BaseReportAction#list()
	 */
	public String list() {

		filter = new CropSupply();
		return LIST;
	}

	public String data() throws Exception {

		// super.filter = this.filter;

		// return sendJSONResponse(readData());
		return sendJQGridJSONResponse(buildFilterDataMap());
	}

	@SuppressWarnings("unchecked")
	private Map buildFilterDataMap() {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		CropSupply filter = new CropSupply();

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("harvestDate"))) {
		 * filter.setBranchId(searchRecord.get("harvestDate").trim()); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("farmerId"))) {
			filter.setFarmerId(searchRecord.get("farmerId").trim());
			filter.setFarmerName(searchRecord.get("farmerId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("farmCode"))) {
			filter.setFarmCode(searchRecord.get("farmCode").trim());
			filter.setFarmName(searchRecord.get("farmCode").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("receiptNo"))) {
			filter.setReceiptInfor(searchRecord.get("receiptNo").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			filter.setVillageName(searchRecord.get("village").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("currentSeasonCode"))) {
			filter.setCurrentSeasonCode(searchRecord.get("currentSeasonCode").trim());
		}

		/*
		 * Farmer farmer =
		 * farmerService.findFarmerByFarmerId(cropHarvest.getFarmerId());
		 * 
		 * if (!StringUtil.isEmpty(searchRecord.get("v.name"))) { Village
		 * village = new Village();
		 * village.setName(searchRecord.get("v.name").trim());
		 * filter.farmer.setVillage(village); }
		 */
		filter.setSearchPage(searchPage);
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return data;

	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.BaseReportAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		Double totPrice = new Double(0.0);
		Double totQty = new Double(0.0);

		JSONObject jsonObject = new JSONObject();
		if (obj instanceof CropSupplyDetails) {
			CropSupplyDetails cropSupplyDetails = (CropSupplyDetails) obj;
			JSONArray rows = new JSONArray();
			rows.add(cropSupplyDetails.getCrop().getName());
			rows.add(cropSupplyDetails.getVariety().getName());
			rows.add(cropSupplyDetails.getGrade().getName());
			rows.add(CurrencyUtil.getDecimalFormat(cropSupplyDetails.getPrice(), "##.00"));
			rows.add(CurrencyUtil.getDecimalFormat(cropSupplyDetails.getQty(), "##.00"));
			rows.add(CurrencyUtil.getDecimalFormat(cropSupplyDetails.getSubTotal(), "##.00"));
			jsonObject.put("id", cropSupplyDetails.getId());
			jsonObject.put("cell", rows);
		} else {
			CropSupply cropSupply = (CropSupply) obj;
			JSONArray rows = new JSONArray();
			ESESystem preferences = preferncesService.findPrefernceById("1");
			DateFormat genDate = new SimpleDateFormat(preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT));

			if (!ObjectUtil.isEmpty(cropSupply.getDateOfSale())) {
				String date = "";
				if (!ObjectUtil.isEmpty(cropSupply.getDateOfSale())) {
					date = genDate.format(cropSupply.getDateOfSale());
				}
				if ((getIsMultiBranch().equalsIgnoreCase("1")
						&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(!StringUtil
								.isEmpty(getBranchesMap().get(getParentBranchMap().get(cropSupply.getBranchId())))
										? getBranchesMap().get(getParentBranchMap().get(cropSupply.getBranchId()))
										: getBranchesMap().get(cropSupply.getBranchId()));
					}
					rows.add(getBranchesMap().get(cropSupply.getBranchId()));
				} else {
					if (StringUtil.isEmpty(branchIdValue)) {
						rows.add(branchesMap.get(cropSupply.getBranchId()));
					}
				}

				rows.add(date); // Harvest Date
				HarvestSeason season = farmerService.findSeasonNameByCode(cropSupply.getCurrentSeasonCode());
				if (!ObjectUtil.isEmpty(season)) {
					rows.add(season.getName());// Season Code

				} else {
					rows.add("");
				}
				if (cropSupply.getFarmerId() != "") {
					Farmer farmer = farmerService.findFarmerByFarmerId(cropSupply.getFarmerId());
					if (!ObjectUtil.isEmpty(farmer)) {
						rows.add(farmer.getVillage().getCode() + "-" + farmer.getVillage().getName());
					} else {
						rows.add("");
					}

				}

				rows.add(cropSupply.getFarmerName());// farmer name
				rows.add(cropSupply.getFarmName());
				// rows.add(cropSupply.getFarmerName() + "-" +
				// cropSupply.getFarmerId());// farmer name
				// rows.add(cropSupply.getFarmName() + "-" +
				// cropSupply.getFarmCode());// farm name
				rows.add(cropSupply.getInvoiceDetail());
				// rows.add(cropSupply.getCropSupplyDetails().iterator().next().getQty());
				// Iterate over crop supply Details list to get total price.
				Double qtyKg = 0.0, qtyQuin = 0.0;
				for (CropSupplyDetails cropSupplyDetails : cropSupply.getCropSupplyDetails()) {
					totPrice += cropSupplyDetails.getSubTotal();
					if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
						if (!ObjectUtil.isEmpty(cropSupplyDetails.getCrop())
								&& !StringUtil.isEmpty(cropSupplyDetails.getCrop().getUnit())) {
							if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kg")
									|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kgs")) {
								qtyKg += cropSupplyDetails.getQty();
							}
							if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("Quintals")
									|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("Quintal")) {
								qtyQuin += cropSupplyDetails.getQty();
							}
						}

					} else {
						totQty += cropSupplyDetails.getQty();
					}
				}
				// rows.add(cropSupply.getTotalSaleValu()); //Commented to fix
				// bug 743.
				if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
					totQty = (qtyKg / 1000) + (qtyQuin / 10);
				}
				rows.add(CurrencyUtil.getDecimalFormat(totQty, "##.00"));
				rows.add(CurrencyUtil.getDecimalFormat(totPrice, "##.00"));
				jsonObject.put("id", cropSupply.getId());
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	public String detail() {
		List<CropSupplyDetails> cropSupplyDetailTempList = new ArrayList<>();
		cropSaleDetailsList = new ArrayList<>();
		int totQty = 0;
		double totQtyMt = 0.0;
		double totPrice = 0.00;
		try {
			if (!StringUtil.isEmpty(id)) {
				getCurrentSeasonsCode();
				getCurrentSeasonsName();
				cropSupply = farmerService.findcropSupplyId(id);
				Farmer farmer = farmerService.findFarmerByFarmerId(cropSupply.getFarmerId());
				cropSupply.setVillageName(farmer.getVillage().getVillageName());
				cropSupplyDetailTempList = farmerService.findCropSupplyDetailId(cropSupply.getId());
				if (cropSupplyDetailTempList.size() > 0) {

					for (CropSupplyDetails cropSupplyDetails : cropSupplyDetailTempList) {
						cropSupplyDetails.setCropTypeStr(getText("ct" + cropSupplyDetails.getCropType()));
						if (getCurrentTenantId().equalsIgnoreCase("pratibha")) {
							if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kgs")
									|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("kg")) {
								totQtyMt += (cropSupplyDetails.getQty() / 1000);
							}
							if (cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("Quintals")
									|| cropSupplyDetails.getCrop().getUnit().equalsIgnoreCase("Quintal")) {
								totQtyMt += (cropSupplyDetails.getQty() / 1000);
							}
						} else {
							totQty += cropSupplyDetails.getQty();
						}
						totPrice += cropSupplyDetails.getSubTotal();
						cropSaleDetailsList.add(cropSupplyDetails);
						setBatchNo(cropSupplyDetails.getBatchLotNo());
					}
					cropSupply.setTotalQuantity(totQtyMt);
					cropSupply.setTotalQty(totQty);
					cropSupply.setTotalSaleValu(totPrice);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DETAIL;
	}
	public String populatedetailImage() {
	try {
		setId(id);
		CropSupplyImages imageDetail = farmerService.loadCropSupplyImages(Long.valueOf(id));
		byte[] image = imageDetail.getPhoto();
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
	
	public void populateImageId()  {
		String cropSupplyImgId = "";
		String tempVal="";
		List<CropSupplyImages> cropSupplyImagesList = farmerService.listCropSupplyImages(Long.valueOf(id));
		if (!ObjectUtil.isListEmpty(cropSupplyImagesList)) {
			for (CropSupplyImages cropSupplyImages : cropSupplyImagesList) {
				if (cropSupplyImages.getPhoto() != null && cropSupplyImages.getPhoto().length > 0) {
					cropSupplyImgId += String.valueOf(cropSupplyImages.getId())  + ",";
				}
				 tempVal = cropSupplyImgId.substring(0, cropSupplyImgId.length() - 1);
			}
	}
		sendAjaxResponse(String.valueOf(tempVal));
	}
	/*  *//**
			 * Update.
			 * 
			 * @return the string
			 * @throws Exception
			 *             the exception
			 *//*
			 * public String update() throws Exception { String result="";
			 * 
			 * if (!StringUtil.isEmpty(id)) {
			 * cropSupply=farmerService.findcropSupplyId(id); if (cropSupply ==
			 * null) { addActionError(NO_RECORD); return REDIRECT; }
			 * setCurrentPage(getCurrentPage()); id = null;
			 * 
			 * result="edit";
			 * 
			 * // request.setAttribute(HEADING, getText(UPDATE)); } else { if
			 * (selectedGrade != null) { Set<CropSupplyDetails>
			 * cropSuplyDetailsSet = new HashSet<>();
			 * cropSupply.setDateOfSale(dateOfSale); Customer buyer=new
			 * Customer(); buyer.setId(Long.valueOf(selectedBuyer));
			 * cropSupply.setBuyerInfo(buyer);
			 * cropSupply.setFarmerId(selectedFarmer);
			 * 
			 * String farmValue[]=selectedFarm.split("-");
			 * cropSupply.setFarmName(farmValue[0]);
			 * cropSupply.setFarmCode(farmValue[1]);
			 * cropSupply.setTransportDetail(transportName);
			 * cropSupply.setReceiptInfor(invoiceNo);
			 * cropSupply.setPaymentInfo(paymentValue);
			 * cropSupply.setVehicleNo(vechicleNo);
			 * cropSupply.setTotalQty(Integer.valueOf(totalQty));
			 * CropSupplyDetails cropSupplyDetails=new CropSupplyDetails();
			 * ProcurementVariety variety=new ProcurementVariety();
			 * variety.setId(Long.valueOf(selectedVariety));
			 * cropSupplyDetails.setVariety(variety); ProcurementProduct
			 * crop=new ProcurementProduct();
			 * crop.setId(Long.valueOf(selectedCropName));
			 * cropSupplyDetails.setCrop(crop); ProcurementGrade grade=new
			 * ProcurementGrade(); grade.setId(Long.valueOf(selectedGrade));
			 * cropSupplyDetails.setGrade(grade);
			 * cropSupplyDetails.setBatchLotNo(batchNo);
			 * cropSupplyDetails.setQty(Double.valueOf(quantity));
			 * cropSupplyDetails.setPrice(Double.valueOf(sellPrice));
			 * 
			 * cropSuplyDetailsSet.add(cropSupplyDetails);
			 * cropSupply.setCropSupplyDetails(cropSuplyDetailsSet);
			 * farmerService.saveCropSupply(cropSupply);
			 * 
			 * 
			 * 
			 * request.setAttribute(HEADING, getText(LIST));
			 * 
			 * return LIST; } } return result;
			 * 
			 * }
			 */

	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			getCurrentSeasonsCode();
			getCurrentSeasonsName();

			filter = productService.findCropSupplyById(Long.valueOf(id));
			dateOfSale = sdf.format(filter.getDateOfSale());
			List<CropSupplyDetails> cropSupplyDetailTempList = new ArrayList<>();
			cropSupplyDetailTempList = farmerService.findCropSupplyDetailId(Long.parseLong(id));
			if (!ObjectUtil.isListEmpty(cropSupplyDetailTempList) && cropSupplyDetailTempList.size() > 0) {
				for (CropSupplyDetails cropSupplyDetails : cropSupplyDetailTempList) {
					setBatchNo(cropSupplyDetails.getBatchLotNo());
				}
			}

			if (filter == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			id = null;
			// setCommand(UPDATE);
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			if (filter != null) {
				CropSupply temp = productService.findCropSupplyById(filter.getId());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				temp.setFarmCode(filter.getFarmCode());
				temp.setFarmerId(filter.getFarmerId());
				temp.setFarmerName(filter.getFarmerName());
				temp.setFarmName(filter.getFarmName());
				temp.setCooperative(coOperative);
				temp.setDateOfSale(temp.getDateOfSale());
				temp.setReceiptInfor(filter.getReceiptInfor());
				temp.setTotalQty(filter.getTotalQty());
				// temp.setCode(country.getCode());
				productService.updateCropSupply(temp);
			}
			request.setAttribute(HEADING, getText("cropsaleERlist"));
			return LIST;
		}
		return super.execute();
	}

	public String delete() {
		if (!StringUtil.isEmpty(id)) {
			CropSupply cropSupply = farmerService.findcropSupplyId(id);
			farmerService.removeCropSupply(cropSupply);

		}
		return LIST;
	}

	public String createImage() {
		String result = "redirect";

		CropSupply cropSup = farmerService.findcropSupplyId(String.valueOf(getId()));
		
		File[] dynamicData = {getCropSaleImage(),getCropSaleImage1()};
		for (File productData : dynamicData) {
			File value = productData;
			if(value!=null){
			CropSupplyImages cropSupplyImages = new CropSupplyImages();
			cropSupplyImages.setCropSupply(cropSup);
			byte[] farmPhotoData = FileUtil.getBinaryFileContent(value);
			if (farmPhotoData != null) {
				cropSupplyImages.setPhoto(farmPhotoData);

			}
			farmerService.save(cropSupplyImages);
			}
		} 
		return result;

	}

	public void populateSave() {
		Double finalBuyerAccCashBal = (double) 0;
		Double finalBuyerAccCreditBal = (double) 0;

		Double finalCompAccCashBal = (double) 0;
		Double finalCompAccCreditBal = (double) 0;

		Set<CropSupplyDetails> cropSupplyDetailSet = new HashSet<>();
		cropSupply = new CropSupply();
		cropSupply.setFarmerId(selectedFarmer);
		Farm farm = farmerService.findFarmByCode(selectedFarm);
		if (!ObjectUtil.isEmpty(farm)) {
			cropSupply.setFarmCode(farm.getFarmCode());
			cropSupply.setFarmName(farm.getFarmName());
		}

		cropSupply.setFarmerName(farm.getFarmer().getName());
		cropSupply.setCooperative(getCoOperative());
		Customer buyer = new Customer();
		buyer.setId(Long.valueOf(selectedBuyer));
		cropSupply.setBuyerInfo(buyer);
		cropSupply.setTransportDetail(transportName);
		cropSupply.setVehicleNo(vechicleNo);
		cropSupply.setInvoiceDetail(invoiceNo);
		cropSupply.setReceiptInfor(invoiceNo);
		cropSupply.setBranchId(getBranchId());
		cropSupply.setTotalSaleValu(Double.valueOf(totalSalePrice));
		cropSupply.setDateOfSale(DateUtil.convertStringToDate(saleDate, getGeneralDateFormat()));
		cropSupply.setDateOfSale(DateUtil.setTimeToDate(cropSupply.getDateOfSale()));

		if ("0".equals(paymentMode)) {
			cropSupply.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
			cropSupply.setPaymentInfo(payment);
		} else if ("1".equals(paymentMode)) {
			cropSupply.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
			cropSupply.setPaymentInfo(creditAmount);
		}

		Customer customer = clientService.findCustomer(Long.valueOf(selectedBuyer));
		if (!ObjectUtil.isEmpty(customer)) {
			ESEAccount buyerAccount = accountService.findAccountByProfileIdAndProfileType(customer.getCustomerId(),
					ESEAccount.CLIENT_ACCOUNT);

			ESEAccount companyAccount = accountService.findAccountByProfileId(ESEAccount.BASIX_ACCOUNT);
			if (companyAccount != null) {

				Double buyerAccCashBal = buyerAccount.getCashBalance();
				Double buyerAccCreditBal = buyerAccount.getCreditBalance();
				Double compAccCashBal = companyAccount.getCashBalance();
				Double compAccCreditBal = companyAccount.getCreditBalance();

				if ("0".equals(paymentMode)) {
					finalBuyerAccCashBal = buyerAccCashBal - Double.valueOf(payment);

					finalBuyerAccCreditBal = buyerAccCreditBal
							- (Double.valueOf(totalSalePrice) - Double.valueOf(payment));

					finalCompAccCashBal = compAccCashBal + Double.valueOf(payment);
					if (!buyerAccCreditBal.equals(finalBuyerAccCreditBal)
							&& (Double.parseDouble(payment) > Double.parseDouble(totalSalePrice))) // finalBuyerAccCreditBal>0
						finalCompAccCreditBal = compAccCreditBal
								- (Double.valueOf(payment) + buyerAccCreditBal - finalBuyerAccCreditBal);
					else if (!buyerAccCreditBal.equals(finalBuyerAccCreditBal)
							&& (Double.parseDouble(payment) < Double.parseDouble(totalSalePrice))) // finalBuyerAccCreditBal>0
						finalCompAccCreditBal = compAccCreditBal
								+ (Double.valueOf(totalSalePrice) - Double.valueOf(payment));
					else
						finalCompAccCreditBal = compAccCreditBal;
				} else if ("1".equals(paymentMode)) {
					finalBuyerAccCashBal = buyerAccCashBal;
					finalBuyerAccCreditBal = buyerAccCreditBal - Double.valueOf(totalSalePrice);

					finalCompAccCashBal = compAccCashBal;
					finalCompAccCreditBal = compAccCreditBal;
				}

				// compAccCashBal = compAccCashBal + Double.valueOf(payment);

				accountService.updateCashBal(buyerAccount.getId(), finalBuyerAccCashBal < 0 ? 0 : finalBuyerAccCashBal,
						finalBuyerAccCreditBal);
				accountService.updateCashBal(companyAccount.getId(), finalCompAccCashBal, finalCompAccCreditBal);

				/*
				 * accountService.updateESEAccountCashBalById(buyerAccount.getId
				 * (), buyerAccCashBal);
				 * accountService.updateESEAccountCashBalById(companyAccount.
				 * getId(), compAccCashBal);
				 */

			}
		}

		String[] productArray = productTotalString.split("\\|");
		for (String productData : productArray) {

			String[] productDataArray = productData.split("\\#");

			CropSupplyDetails cropSupplyDetails = new CropSupplyDetails();
			cropSupplyDetails.setCropType(Integer.valueOf(productDataArray[0]));
			ProcurementProduct crop = new ProcurementProduct();
			crop.setId(Long.valueOf(productDataArray[1]));
			cropSupplyDetails.setCrop(crop);
			ProcurementVariety variety = new ProcurementVariety();
			variety.setId(Long.valueOf(productDataArray[2]));
			cropSupplyDetails.setVariety(variety);
			ProcurementGrade grade = new ProcurementGrade();
			grade.setId(Long.valueOf(productDataArray[3]));
			cropSupplyDetails.setGrade(grade);
			cropSupplyDetails.setBatchLotNo(productDataArray[5]);
			cropSupplyDetails.setQty(Double.valueOf(productDataArray[6]));
			cropSupplyDetails.setPrice(Double.valueOf(productDataArray[4]));
			cropSupplyDetails.setSubTotal(cropSupplyDetails.getQty() * cropSupplyDetails.getPrice());
			cropSupplyDetails.setCropSupply(cropSupply);
			cropSupplyDetailSet.add(cropSupplyDetails);

		}
		cropSupply.setCropSupplyDetails(cropSupplyDetailSet);
		if (!ObjectUtil.isEmpty(getCurrentSeasonsCode()) && !StringUtil.isEmpty(getCurrentSeasonsCode())) {
			cropSupply.setCurrentSeasonCode(getCurrentSeasonsCode());
		}
		farmerService.saveCropSupply(cropSupply);
		sendAjaxResponse(String.valueOf(cropSupply.getId()));

	}

	public String create() throws Exception {
		String result = "create";
		Double finalBuyerAccCashBal = (double) 0;
		Double finalBuyerAccCreditBal = (double) 0;

		Double finalCompAccCashBal = (double) 0;
		Double finalCompAccCreditBal = (double) 0;

		if (productTotalString == null) {
			cashType.put(0, getText("cashType1"));
			cashType.put(1, getText("cashType2"));
			getCurrentSeasonsCode();
			getCurrentSeasonsName();

			result = "create";
		} else {

			Set<CropSupplyDetails> cropSupplyDetailSet = new HashSet<>();
			cropSupply = new CropSupply();
			cropSupply.setFarmerId(selectedFarmer);
			Farm farm = farmerService.findFarmByCode(selectedFarm);
			if (!ObjectUtil.isEmpty(farm)) {
				cropSupply.setFarmCode(farm.getFarmCode());
				cropSupply.setFarmName(farm.getFarmName());
			}

			cropSupply.setFarmerName(farm.getFarmer().getName());
			cropSupply.setCooperative(getCoOperative());
			Customer buyer = new Customer();
			buyer.setId(Long.valueOf(selectedBuyer));
			cropSupply.setBuyerInfo(buyer);
			cropSupply.setTransportDetail(transportName);
			cropSupply.setVehicleNo(vechicleNo);
			cropSupply.setInvoiceDetail(invoiceNo);
			cropSupply.setReceiptInfor(invoiceNo);
			cropSupply.setBranchId(getBranchId());
			cropSupply.setTotalSaleValu(Double.valueOf(totalSalePrice));
			cropSupply.setDateOfSale(DateUtil.convertStringToDate(saleDate, getGeneralDateFormat()));
			cropSupply.setDateOfSale(DateUtil.setTimeToDate(cropSupply.getDateOfSale()));

			if ("0".equals(paymentMode)) {
				cropSupply.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
				cropSupply.setPaymentInfo(payment);
			} else if ("1".equals(paymentMode)) {
				cropSupply.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
				cropSupply.setPaymentInfo(creditAmount);
			}

			Customer customer = clientService.findCustomer(Long.valueOf(selectedBuyer));
			if (!ObjectUtil.isEmpty(customer)) {
				ESEAccount buyerAccount = accountService.findAccountByProfileIdAndProfileType(customer.getCustomerId(),
						ESEAccount.CLIENT_ACCOUNT);

				ESEAccount companyAccount = accountService.findAccountByProfileId(ESEAccount.BASIX_ACCOUNT);
				if (companyAccount != null) {

					Double buyerAccCashBal = buyerAccount.getCashBalance();
					Double buyerAccCreditBal = buyerAccount.getCreditBalance();
					Double compAccCashBal = companyAccount.getCashBalance();
					Double compAccCreditBal = companyAccount.getCreditBalance();

					if ("0".equals(paymentMode)) {
						finalBuyerAccCashBal = buyerAccCashBal - Double.valueOf(payment);

						finalBuyerAccCreditBal = buyerAccCreditBal
								- (Double.valueOf(totalSalePrice) - Double.valueOf(payment));

						finalCompAccCashBal = compAccCashBal + Double.valueOf(payment);
						if (!buyerAccCreditBal.equals(finalBuyerAccCreditBal)
								&& (Double.parseDouble(payment) > Double.parseDouble(totalSalePrice))) // finalBuyerAccCreditBal>0
							finalCompAccCreditBal = compAccCreditBal
									- (Double.valueOf(payment) + buyerAccCreditBal - finalBuyerAccCreditBal);
						else if (!buyerAccCreditBal.equals(finalBuyerAccCreditBal)
								&& (Double.parseDouble(payment) < Double.parseDouble(totalSalePrice))) // finalBuyerAccCreditBal>0
							finalCompAccCreditBal = compAccCreditBal
									+ (Double.valueOf(totalSalePrice) - Double.valueOf(payment));
						else
							finalCompAccCreditBal = compAccCreditBal;
					} else if ("1".equals(paymentMode)) {
						finalBuyerAccCashBal = buyerAccCashBal;
						finalBuyerAccCreditBal = buyerAccCreditBal - Double.valueOf(totalSalePrice);

						finalCompAccCashBal = compAccCashBal;
						finalCompAccCreditBal = compAccCreditBal;
					}

					// compAccCashBal = compAccCashBal +
					// Double.valueOf(payment);

					accountService.updateCashBal(buyerAccount.getId(),
							finalBuyerAccCashBal < 0 ? 0 : finalBuyerAccCashBal, finalBuyerAccCreditBal);
					accountService.updateCashBal(companyAccount.getId(), finalCompAccCashBal, finalCompAccCreditBal);

					/*
					 * accountService.updateESEAccountCashBalById(buyerAccount.
					 * getId(), buyerAccCashBal);
					 * accountService.updateESEAccountCashBalById(companyAccount
					 * .getId(), compAccCashBal);
					 */

				}
			}

			String[] productArray = productTotalString.split("\\|");
			for (String productData : productArray) {

				String[] productDataArray = productData.split("\\#");

				CropSupplyDetails cropSupplyDetails = new CropSupplyDetails();
				cropSupplyDetails.setCropType(Integer.valueOf(productDataArray[0]));
				ProcurementProduct crop = new ProcurementProduct();
				crop.setId(Long.valueOf(productDataArray[1]));
				cropSupplyDetails.setCrop(crop);
				ProcurementVariety variety = new ProcurementVariety();
				variety.setId(Long.valueOf(productDataArray[2]));
				cropSupplyDetails.setVariety(variety);
				ProcurementGrade grade = new ProcurementGrade();
				grade.setId(Long.valueOf(productDataArray[3]));
				cropSupplyDetails.setGrade(grade);
				cropSupplyDetails.setBatchLotNo(productDataArray[5]);
				cropSupplyDetails.setQty(Double.valueOf(productDataArray[6]));
				cropSupplyDetails.setPrice(Double.valueOf(productDataArray[4]));
				cropSupplyDetails.setSubTotal(cropSupplyDetails.getQty() * cropSupplyDetails.getPrice());
				cropSupplyDetails.setCropSupply(cropSupply);
				cropSupplyDetailSet.add(cropSupplyDetails);

			}
			cropSupply.setCropSupplyDetails(cropSupplyDetailSet);
			if (!ObjectUtil.isEmpty(getCurrentSeasonsCode()) && !StringUtil.isEmpty(getCurrentSeasonsCode())) {
				cropSupply.setCurrentSeasonCode(getCurrentSeasonsCode());
			}
			farmerService.saveCropSupply(cropSupply);
			sendAjaxResponse(String.valueOf(cropSupply.getId()));
		}

		return result;

	}

	public void populateBuyerAccBalance() throws Exception {

		String cashAndCredit = "";
		if (!StringUtil.isEmpty(selectedBuyerValue)) {
			Customer customer = clientService.findCustomer(Long.valueOf(selectedBuyerValue));
			if (!StringUtil.isEmpty(customer.getCustomerId())) {
				account = accountService.findAccountByProfileIdAndProfileType(customer.getCustomerId(),
						ESEAccount.CLIENT_ACCOUNT);
				// account =
				// accountService.findAccountByVendorIdAndType(customer.getCustomerId(),
				// ESEAccount.CLIENT_ACCOUNT);
				if (!StringUtil.isEmpty(account)) {
					cashCreditValue = (account.getCashBalance() + "," + account.getCreditBalance());
					cashAndCredit = cashCreditValue;
				} else {
					cashCreditValue = "";
				}
				cashAndCredit = cashCreditValue;
			}
		}
		response.getWriter().print(cashAndCredit);

	}

	/**
	 * Gets the farmerName list.
	 * 
	 * @return the farmerName list
	 */
	public List<String[]> getFarmerNameList() {

		List<String[]> farmerNameList = farmerService.listFarmerName();

		return farmerNameList;

	}

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */
	public Map<Long, String> getVillageList() {

		Map<Long, String> villageMap = new LinkedHashMap<Long, String>();

		List<Village> villageList = locationService.listVillage();
		for (Village obj : villageList) {
			// villageMap.put(obj.getId(), obj.getName());
			villageMap.put(obj.getId(), obj.getName() + " - " + obj.getCode());
		}
		return villageMap;

	}

	/**
	 * Populate populateFarmer.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateFarmer() throws Exception {

		farmers = new ArrayList<Farmer>();
		if (!selectedVillage.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVillage))) {

			farmers = farmerService.listOfFarmers(selectedVillage);
		}
		JSONArray farmerArr = new JSONArray();
		if (!ObjectUtil.isEmpty(farmers)) {

			// farmerArr.add(farmers.stream().forEach(obj->getJSONObject(obj.getFarmerId(),
			// obj.getName()+""+obj.getFarmerId()));
			for (Farmer farmer : farmers) {
				if (farmer.getFarmerCode() == null) {
					farmerArr.add(getJSONObject(farmer.getFarmerId(), farmer.getName()));
				} else {
					farmerArr.add(getJSONObject(farmer.getFarmerId(), farmer.getName() + "-" + farmer.getFarmerCode()));
				}

			}
		}
		sendAjaxResponse(farmerArr);
	}

	/**
	 * populateCropName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateCropName() throws Exception {
		List<Object[]> cropNamesList = new ArrayList<>();
		if (!selectedCropType.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedCropType)
				&& !selectedFarm.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedFarm)) {

			// Farm farm=farmerService.findFarmByCode(selectedFarm);
			cropNamesList = farmerService.listOfCropNamesByCropTypeAndFarm(selectedCropType, selectedFarm);
		}

		JSONArray cropsArr = new JSONArray();
		if (!ObjectUtil.isEmpty(cropNamesList)) {
			for (Object[] crops : cropNamesList) {

				cropsArr.add(getJSONObject(crops[0], crops[1]));
			}
		}
		sendAjaxResponse(cropsArr);
	}

	/**
	 * populateVarietyName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateVarietyName() throws Exception {
		/*
		 * if (!selectedCropName.equalsIgnoreCase("null") &&
		 * (!StringUtil.isEmpty(selectedCropName))) { varietysList =
		 * farmerService.listOfVariety(selectedCropName); } JSONArray varietyArr
		 * = new JSONArray(); if (!ObjectUtil.isEmpty(varietysList)) { for
		 * (ProcurementVariety variety : varietysList) {
		 * varietyArr.add(getJSONObject(variety.getId(), variety.getName())); }
		 * } sendAjaxResponse(varietyArr);
		 */

		if (!selectedCropType.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedCropType)
				&& !selectedFarm.equalsIgnoreCase("null") && !StringUtil.isEmpty(selectedFarm)
				&& !selectedCropName.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCropName))) {
			varietysList = farmerService.listOfVarietyByCropTypeFarmCodeAndCrop(selectedCropType, selectedFarm,
					selectedCropName);
		}
		JSONArray varietyArr = new JSONArray();
		if (!ObjectUtil.isEmpty(varietysList)) {
			for (FarmCrops variety : varietysList) {
				varietyArr.add(getJSONObject(variety.getProcurementVariety().getId(),
						variety.getProcurementVariety().getName()));
			}
		}
		sendAjaxResponse(varietyArr);
	}

	/**
	 * populateGradeName.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateGradeName() throws Exception {
		if (!selectedVariety.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVariety))) {
			gradesList = farmerService.listOfGrade(selectedVariety);
		}

		JSONArray gradeArr = new JSONArray();
		if (!ObjectUtil.isEmpty(gradesList)) {
			for (ProcurementGrade grade : gradesList) {
				gradeArr.add(getJSONObject(grade.getId(), grade.getName()));
			}
		}
		sendAjaxResponse(gradeArr);
	}

	public Map<Integer, String> getVarietyList() {
		Map<Integer, String> varities = new HashMap<Integer, String>();
		List<ProcurementVariety> procurementVarieties = productService.listProcurementVariety();
		for (ProcurementVariety procurementVariety : procurementVarieties) {
			varities.put(Integer.valueOf(String.valueOf(procurementVariety.getId())), procurementVariety.getName());
		}
		return varities;
	}

	/**
	 * 
	 * getGradeList
	 * 
	 * @return
	 */

	public Map<Integer, String> getGradeList() {
		Map<Integer, String> varities = new HashMap<Integer, String>();
		List<ProcurementGrade> procurementgrades = productService.listProcurementGrade();
		for (ProcurementGrade procurementgrade : procurementgrades) {
			varities.put(Integer.valueOf(String.valueOf(procurementgrade.getId())), procurementgrade.getName());
		}
		return varities;
	}

	/**
	 * populatePrice
	 * 
	 * @throws Exception
	 */

	public void populatePrice() throws Exception {
		Double gradePrice = 0.0;
		if (!selectedGrade.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedGrade))) {
			gradePrice = farmerService.findGradePrice(selectedGrade);
		}
		JSONArray gradeArr = new JSONArray();
		if (!ObjectUtil.isEmpty(gradePrice)) {

			gradeArr.add(CurrencyUtil.getDecimalFormat(gradePrice, "##.00"));

		}
		sendAjaxResponse(gradeArr);
	}

	/**
	 * Gets the farmers list.
	 * 
	 * @return the farmers list
	 */
	public Map<String, String> getFarmersList() {

		Map<String, String> farmerListMap = new LinkedHashMap<String, String>();

		List<Farmer> farmersList = farmerService.listFarmer();

		for (Farmer obj : farmersList) {
			farmerListMap.put(obj.getFarmerId(),
					obj.getFirstName() + " " + obj.getLastName() + " - " + obj.getFarmerId());
		}
		return farmerListMap;

	}

	/**
	 * Gets the farm list.
	 * 
	 * @return the farm list
	 */
	public Map<String, String> getFarmList() {

		Map<String, String> farmListMap = new LinkedHashMap<String, String>();

		List<Farm> listFarms = farmerService.listFarm();
		for (Farm obj : listFarms) {
			farmListMap.put(obj.getFarmCode(), obj.getFarmName() + " - " + obj.getFarmCode());
		}
		return farmListMap;

	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Populate Farm.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateFarm() throws Exception {

		if (!selectedFarmer.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedFarmer))) {
			String farmerId[] = selectedFarmer.split("-");

			farms = farmerService.listFarmerFarmByFarmerId(selectedFarmer);
		}
		JSONArray farmsArr = new JSONArray();
		if (!ObjectUtil.isEmpty(farms)) {
			for (Farm farm : farms) {
				farmsArr.add(getJSONObject(farm.getFarmCode(), farm.getFarmName() + "-" + farm.getFarmCode()));
			}
		}
		sendAjaxResponse(farmsArr);
	}
	/**
	 * Gets the filter.
	 * 
	 * @return the filter
	 */

	public CropSupply getFilter() {

		return filter;
	}

	/**
	 * Sets the filter.
	 * 
	 * @param filter
	 *            the new filter
	 */
	public void setFilter(CropSupply filter) {

		this.filter = filter;
	}

	/**
	 * Sets the crops.
	 * 
	 * @param crops
	 *            the new crops
	 */
	public void setCrops(List<String> crops) {

		this.crops = crops;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
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
	 * Gets the farmer name.
	 * 
	 * @return the farmer name
	 */
	public String getFarmerName() {

		return farmerName;
	}

	/**
	 * Sets the farmer name.
	 * 
	 * @param farmerName
	 *            the new farmer name
	 */
	public void setFarmerName(String farmerName) {

		this.farmerName = farmerName;
	}

	/**
	 * Gets the farm name.
	 * 
	 * @return the farm name
	 */
	public String getFarmName() {

		return farmName;
	}

	/**
	 * Sets the farm name.
	 * 
	 * @param farmName
	 *            the new farm name
	 */
	public void setFarmName(String farmName) {

		this.farmName = farmName;
	}

	/**
	 * Gets the crop type.
	 * 
	 * @return the crop type
	 */
	public String getCropType() {

		return cropType;
	}

	/**
	 * Sets the crop type.
	 * 
	 * @param cropType
	 *            the new crop type
	 */
	public void setCropType(String cropType) {

		this.cropType = cropType;
	}

	/**
	 * Gets the qty.
	 * 
	 * @return the qty
	 */
	public double getQty() {

		return qty;
	}

	/**
	 * Sets the qty.
	 * 
	 * @param qty
	 *            the new qty
	 */
	public void setQty(double qty) {

		this.qty = qty;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public double getPrice() {

		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
	public void setPrice(double price) {

		this.price = price;
	}

	/**
	 * Gets the crops.
	 * 
	 * @return the crops
	 */
	public List<String> getCrops() {

		return crops;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id
	 * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Gets the crop type list.
	 * 
	 * @return the crop type list
	 */
	public Map<Integer, String> getCropTypeList() {

		Map<Integer, String> cropListMap = new LinkedHashMap<Integer, String>();
		cropListMap.put(0, getText("ct0"));
		if (getCurrentTenantId().equalsIgnoreCase("chetna")) {
			cropListMap.put(1, getText("ct1"));
		}
		 if(getCurrentTenantId().equalsIgnoreCase("pratibha")){
			 cropListMap.put(1, getText("cs1"));
			 cropListMap.put(2, getText("cs2"));
			 cropListMap.put(3, getText("cs3"));
			 cropListMap.put(4, getText("cs4"));
			 cropListMap.put(5, getText("cs5"));
			 }
		/* cropListMap.put(2, getText("ct2")); */
		return cropListMap;

	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
	}

	/**
	 * Sets the farm id.
	 * 
	 * @param farmId
	 *            the new farm id
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/*
	 * public Map<String, String> getBuyersList() {
	 * 
	 * Map<String, String> buyerListMap = new HashMap<String, String>();
	 * 
	 * List<String> buyersList=new ArrayList<>();
	 * 
	 * 
	 * buyersList = clientService.listOfCustomers();
	 * 
	 * for (Customer buyer : buyersList) {
	 * buyerListMap.put(buyer.getCustomerId(), buyer.getCustomerId() + " - " +
	 * buyer.getCustomerName()); }
	 * 
	 * 
	 * return buyersList; }
	 */

	/**
	 * Populate xls.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateXLS() throws Exception {

		InputStream is = getExportDataStream(IExporter.EXPORT_MANUAL);
		setXlsFileName(getText("cropSupplyReport") + fileNameDateFormat.format(new Date()));
		Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
		fileMap.put(xlsFileName, is);
		setFileInputStream(FileUtil.createFileInputStreamToZipFile(getText("cropSupplyReport"), fileMap, ".xls"));
		return "xls";
	}

	/**
	 * Gets the excel export input stream.
	 * 
	 * @param manual
	 *            the manual
	 * @return the excel export input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */

	public InputStream getExportDataStream(String exportType) throws IOException {

		setMailExport(IExporter.EXPORT_MANUAL.equalsIgnoreCase(exportType) ? true : false);
		setsDate(IExporter.EXPORT_DAILY.equalsIgnoreCase(exportType) ? DateUtil.getDateWithoutTime(new Date())
				: DateUtil.convertStringToDate(
						DateUtil.minusWeek(dateFormat.format(new Date()), 1, DateUtil.DATABASE_DATE_FORMAT),
						DateUtil.DATABASE_DATE_FORMAT));
		boolean flag = true;
		DateFormat filterDateFormat = new SimpleDateFormat(FILTERDATE);
		DecimalFormat df = new DecimalFormat("0.000");
		DecimalFormat df1 = new DecimalFormat("0.00");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(getLocaleProperty("exportProcurementTitle"));
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

		HSSFRow row, titleRow, filterRowTitle, filterRow1, filterRow2, filterRow3, filterRow4 = null, filterRow5 = null;
		HSSFCell cell;

		int imgRow1 = 0;
		int imgRow2 = 4;
		int imgCol1 = 0;
		int imgCol2 = 1;
		int titleRow1 = 2;
		int titleRow2 = 3;

		int rowNum = 3;
		int colNum = 0;

		sheet.addMergedRegion(new CellRangeAddress(imgRow1, imgRow2, imgCol1, imgCol2));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, titleRow1, titleRow2));

		titleRow = sheet.createRow(rowNum++);
		cell = titleRow.createCell(2);
		cell.setCellValue(new HSSFRichTextString(getLocaleProperty("exportCropSaleTitle")));
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

			filterRow1 = sheet.createRow(rowNum++);

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
			cell.setCellValue(new HSSFRichTextString(filterDateFormat.format(geteDate())));
			filterFont.setBoldweight((short) 12);
			filterFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			filterStyle.setFont(filterFont);
			cell.setCellStyle(filterStyle);

			filterRow4 = sheet.createRow(rowNum++);
			filterRow5 = sheet.createRow(rowNum++);
		}
		rowNum++;

		HSSFRow mainGridRowHead = sheet.createRow(rowNum++);
		String mainGridColumnHeaders = getLocaleProperty("saleExportColumnHeader");
		int mainGridIterator = 0;

		for (String cellHeader : mainGridColumnHeaders.split("\\,")) {

			cell = mainGridRowHead.createCell(mainGridIterator);
			cell.setCellValue(new HSSFRichTextString(cellHeader));
			style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style2);
			font2.setBoldweight((short) 12);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font2);
			sheet.setColumnWidth(mainGridIterator, (15 * 550));
			mainGridIterator++;
		}
		if (ObjectUtil.isEmpty(this.filter))
			this.filter = new CropSupply();

		super.filter = this.filter;

		Map data = readData("cropSupply");

		List<Object[]> dfata = (ArrayList) data.get(ROWS);

		if (!ObjectUtil.isEmpty(dfata)) {
			for (Object[] obj : dfata) {
				row = sheet.createRow(rowNum++);
				colNum = 0;

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[1].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[2].toString() + "-" + obj[3].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[4].toString() + "-" + obj[5].toString()));

				if (obj[3].toString() != "") {
					Farmer farmer = farmerService.findFarmerByFarmerId(obj[2].toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(farmer.getVillage().getName()));
				}
				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[8].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(getText("ct" + obj[6].toString())));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[9].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[10].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(obj[11].toString()));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) obj[12], "##.00")));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) obj[13], "##.00")));

				cell = row.createCell(colNum++);
				cell.setCellValue(new HSSFRichTextString(CurrencyUtil.getDecimalFormat((Double) obj[14], "###.00")));

				// CropSupply cropSupply =
				// productService.findCropSupplyById(Long.valueOf(obj[8].toString()));

				if (obj[15] != null) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							ObjectUtil.isEmpty(obj[15].toString()) ? getText("NA") : obj[15].toString()));
				}
				if (obj[16] != null) {
					cell = row.createCell(colNum++);
					cell.setCellValue(new HSSFRichTextString(
							ObjectUtil.isEmpty(obj[16].toString()) ? getText("NA") : obj[16].toString()));
				}

			}

		}
		int pictureIdx = getPicIndex(wb);
		HSSFClientAnchor anchor = new HSSFClientAnchor(400, 10, 655, 200, (short) 0, 0, (short) 0, 0);
		anchor.setAnchorType(1);
		HSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		String id = ObjectUtil.isEmpty(request) ? String.valueOf(DateUtil.getRevisionNumber())
				: request.getSession().getId();
		String makeDir = FileUtil.storeXls(id);
		String fileName = getLocaleProperty("exportProcurementTitle") + fileNameDateFormat.format(new Date()) + ".xls";
		FileOutputStream fileOut = new FileOutputStream(makeDir + fileName);
		wb.write(fileOut);
		InputStream stream = new FileInputStream(new File(makeDir + fileName));
		fileOut.close();

		return stream;

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

	public static SimpleDateFormat getFilenamedateformat() {

		return fileNameDateFormat;
	}

	/**
	 * Gets the pic index.
	 * 
	 * @param wb
	 *            the wb
	 * @return the pic index
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int getPicIndex(HSSFWorkbook wb) throws IOException {

		int index = -1;

		byte[] picData = null;
		picData = clientService.findLogoByCode(Asset.APP_LOGO);
		if (picData != null)
			index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

		return index;
	}

	public String getBuyerInfo() {

		return buyerInfo;
	}

	public void setBuyerInfo(String buyerInfo) {

		this.buyerInfo = buyerInfo;
	}

	public SimpleDateFormat getDateFormat() {

		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {

		this.dateFormat = dateFormat;
	}

	public IClientService getClientService() {

		return clientService;
	}

	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	public Customer getCustomer() {

		return customer;
	}

	public void setCustomer(Customer customer) {

		this.customer = customer;
	}

	/**
	 * Gets the village.
	 * 
	 * @return the village
	 */
	public String getVillage() {

		return village;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the new village
	 */
	public void setVillage(String village) {

		this.village = village;
	}

	public Map<Long, String> getBuyersList() {
		Map<Long, String> listOfBuyers = new HashMap<Long, String>();
		List<Customer> customers = clientService.listOfCustomers();
		for (Customer customer : customers) {
			listOfBuyers.put(customer.getId(), customer.getCustomerName());
		}
		return listOfBuyers;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getSelectedCropType() {
		return selectedCropType;
	}

	public void setSelectedCropType(String selectedCropType) {
		this.selectedCropType = selectedCropType;
	}

	public String getSelectedCropName() {
		return selectedCropName;
	}

	public void setSelectedCropName(String selectedCropName) {
		this.selectedCropName = selectedCropName;
	}

	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getSelectedBuyer() {
		return selectedBuyer;
	}

	public void setSelectedBuyer(String selectedBuyer) {
		this.selectedBuyer = selectedBuyer;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSelectedGrade() {
		return selectedGrade;
	}

	public void setSelectedGrade(String selectedGrade) {
		this.selectedGrade = selectedGrade;
	}

	/**
	 * getCropList
	 * 
	 * @return
	 */
	public Map<String, String> getCropList() {

		Map<String, String> cropTypeNameList = new LinkedHashMap<String, String>();
		List<CropHarvestDetails> nameList = productService.listOfCrops();
		for (CropHarvestDetails cropHarvestDetails : nameList) {
			cropTypeNameList.put(String.valueOf(cropHarvestDetails.getCrop().getId()),
					cropHarvestDetails.getCrop().getCode() + "-" + cropHarvestDetails.getCrop().getName());

		}
		return cropTypeNameList;

	}

	/**
	 * printAjaxResponse
	 */

	protected void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType))
				response.setContentType(contentType);
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * populateCrop
	 */

	public void populateCrop() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementProduct> procurementProducts = null;
		if (!StringUtil.isEmpty(cropTypeCode)) {
			procurementProducts = productService.listProcurmentProductsByType(cropTypeCode);
		}
		if (!ObjectUtil.isEmpty(procurementProducts)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementProduct procurementProduct : procurementProducts) {
				jsonObject.put(procurementProduct.getId(), procurementProduct.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	/**
	 * populateVariety
	 */

	public void populateVariety() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementVariety> procurementVarieties = null;
		if (!StringUtil.isEmpty(cropId)) {
			procurementVarieties = productService.listProcurmentVarirtyByProcurementProductId(cropId);
		}
		if (!ObjectUtil.isEmpty(procurementVarieties)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementVariety procurementVarietiy : procurementVarieties) {
				jsonObject.put(procurementVarietiy.getId(), procurementVarietiy.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	/**
	 * populateGrade
	 */

	public void populateGrade() {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<ProcurementGrade> procurementGrades = null;
		if (!StringUtil.isEmpty(varietyId)) {
			procurementGrades = productService.listProcurmentGradeByVarityId(varietyId);
		}
		if (!ObjectUtil.isEmpty(procurementGrades)) {
			JSONObject jsonObject = new JSONObject();
			for (ProcurementGrade procurementGrade : procurementGrades) {
				jsonObject.put(procurementGrade.getId(), procurementGrade.getName());
				jsonObjects.add(jsonObject);
			}
		}
		printAjaxResponse(jsonObjects, "text/html");

	}

	/*
	 * public void populateGradeUnit() throws Exception {
	 * 
	 * String result = "";
	 * 
	 * JSONArray productArr = new JSONArray(); if
	 * (!StringUtil.isEmpty(selectedGrade)) { ProcurementGrade grade =
	 * productService.findGradeUnitByGradeId(Long.valueOf(selectedGrade));
	 * 
	 * if (!ObjectUtil.isEmpty(grade) && grade.getType() != null) {
	 * productAvailableUnit = String.valueOf(grade.getType().getName()); result
	 * = productAvailableUnit; }
	 * 
	 * result = productAvailableUnit == null ? "" : productAvailableUnit; }
	 * productArr.add(getJSONObject("unit", result));
	 * 
	 * sendAjaxResponse(productArr); }
	 */

	@SuppressWarnings("unchecked")
	public void populateCropSupplyDetailList() {
		JSONArray array = new JSONArray();
		List<CropSupplyDetails> cropSupplyDetails = productService.listCropSupplyDetails(cropSupplyId);
		if (!ObjectUtil.isEmpty(cropSupplyDetails)) {
			for (CropSupplyDetails supplyDetails : cropSupplyDetails) {
				JSONObject obj = new JSONObject();
				obj.put("cropType", getText("ct" + supplyDetails.getCropType()));
				obj.put("crop", supplyDetails.getCrop().getName());
				obj.put("variety", supplyDetails.getVariety().getName());
				obj.put("grade", !ObjectUtil.isEmpty(supplyDetails.getGrade()) ? supplyDetails.getGrade().getName() :"");
				if (supplyDetails.getCrop().getUnit() != null
						&& !StringUtil.isEmpty(supplyDetails.getCrop().getUnit())) {
					obj.put("unit", supplyDetails.getCrop().getUnit());
				} else {
					obj.put("unit", " ");
				}
				if (supplyDetails.getBatchLotNo() != null && !StringUtil.isEmpty(supplyDetails.getBatchLotNo())) {
					obj.put("batch", supplyDetails.getBatchLotNo());
				} else {
					obj.put("batch", " ");
				}
				obj.put("quantity", supplyDetails.getQty());
				obj.put("price", supplyDetails.getPrice());
				obj.put("subTotal", supplyDetails.getSubTotal());
				
				
				obj.put("id", supplyDetails.getId());

				array.add(obj);
			}
			JSONObject mainObj = new JSONObject();
			mainObj.put("data", array);
			printAjaxResponse(mainObj, "text/html");
		}
	}

	public void populateSupplyDetail() {
		JSONObject jsonObject = new JSONObject();
		String msg = "";
		Type listType1 = new TypeToken<List<CropSupplyDetails>>() {
		}.getType();

		List<CropSupplyDetails> cropSaleDetailsList = new Gson().fromJson(supplyDeatailJsonString, listType1);
		if (!ObjectUtil.isEmpty(cropSaleDetailsList)) {
			for (int j = 0; j < cropSaleDetailsList.size(); j++) {
				CropSupplyDetails newCropSaleDetails = new CropSupplyDetails();
				CropSupplyDetails cropSaleDetails = productService.findCropSupplyDetailsbySupplyIdandtherItems(supplyId,
						cropSaleDetailsList.get(j).getCropType(), cropSaleDetailsList.get(j).getCropId(),
						cropSaleDetailsList.get(j).getVarietyId(), cropSaleDetailsList.get(j).getGradeId());
				if (cropSaleDetails != null && !ObjectUtil.isEmpty(cropSaleDetails)) {
					// Double valeu =
					// cropHarvestDetails.getPrice()+cropHarvestDetailsList.get(j).getPrice(),cropHarvestDetails.getQty()+cropHarvestDetailsList.get(j).getQty()+cropHarvestDetails.getSubTotal()+cropHarvestDetailsList.get(j).getSubTotal();
					cropSaleDetails.setBatchLotNo(cropSaleDetailsList.get(j).getBatchLotNo());
					cropSaleDetails.setPrice(cropSaleDetailsList.get(j).getPrice());
					// cropSaleDetails.setQty(cropSaleDetailsList.get(j).getQty());
					// Commented as per bug#401
					// if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID))
					cropSaleDetails.setQty(cropSaleDetailsList.get(j).getQty());
					/*
					 * else cropSaleDetails.setQty(cropSaleDetails.getQty()+
					 * cropSaleDetailsList.get(j).getQty());
					 */
					double subTot = cropSaleDetails.getQty() * cropSaleDetails.getPrice();
					cropSaleDetails.setSubTotal(subTot);
					
					productService.updateCropSaleDetails(cropSaleDetails);
				} else {

					CropSupply cropSupply = productService.findCropSupplyById(supplyId);
					newCropSaleDetails.setCropType(cropSaleDetailsList.get(j).getCropType());
					ProcurementProduct crop = productService
							.findProcurementProductById(cropSaleDetailsList.get(j).getCropId());
					ProcurementVariety procurementVariety = productService
							.findProcurementVarietyById(cropSaleDetailsList.get(j).getVarietyId());
					ProcurementGrade procurementGrade = productService
							.findProcurementGradeById(cropSaleDetailsList.get(j).getGradeId());
					newCropSaleDetails.setCrop(crop);
					newCropSaleDetails.setVariety(procurementVariety);
					newCropSaleDetails.setGrade(procurementGrade);
					newCropSaleDetails.setBatchLotNo(cropSaleDetailsList.get(j).getBatchLotNo());
					newCropSaleDetails.setPrice(cropSaleDetailsList.get(j).getPrice());
					newCropSaleDetails.setQty(cropSaleDetailsList.get(j).getQty());
					double subTot = newCropSaleDetails.getQty() * newCropSaleDetails.getPrice();
					newCropSaleDetails.setSubTotal(subTot);
					
					newCropSaleDetails.setCropSupply(cropSupply);
					productService.saveCropSupplyDetails(newCropSaleDetails);
				}

				// inventryArr.add(getJSONObject(state.getId(),
				// state.getName()));
			}
			jsonObject.put("msg", "success");
		}

		printAjaxResponse(jsonObject, "text/html");
	}

	public String deleteCropSupplyDetail() {

		productService.removeCropSupplyDetails(templateId);
		try {
			JSONObject js = new JSONObject();
			js.put("msg", getText("msg.removed"));
			printAjaxResponse(js, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Map<String, String> getCooperative() {
		AtomicInteger i = new AtomicInteger(0);
		Map<String, String> cooperative = new LinkedHashMap<>();
		FarmCatalogueMaster farmCatalougeMaster = catalogueService
				.findFarmCatalogueMasterByName(getText("cooperative"));
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			cooperative = farmCatalougeList.stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		return cooperative;
	}

	public String getFarmCode() {
		return farmCode;
	}

	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}

	public long getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(long supplyId) {
		this.supplyId = supplyId;
	}

	public String getSupplyDeatailJsonString() {
		return supplyDeatailJsonString;
	}

	public void setSupplyDeatailJsonString(String supplyDeatailJsonString) {
		this.supplyDeatailJsonString = supplyDeatailJsonString;
	}

	public String getCropTypeCode() {
		return cropTypeCode;
	}

	public void setCropTypeCode(String cropTypeCode) {
		this.cropTypeCode = cropTypeCode;
	}

	public String getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public long getCropSupplyId() {
		return cropSupplyId;
	}

	public void setCropSupplyId(long cropSupplyId) {
		this.cropSupplyId = cropSupplyId;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getVechicleNo() {
		return vechicleNo;
	}

	public void setVechicleNo(String vechicleNo) {
		this.vechicleNo = vechicleNo;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public List<CropSupplyDetails> getCropSaleDetailsList() {
		return cropSaleDetailsList;
	}

	public void setCropSaleDetailsList(List<CropSupplyDetails> cropSaleDetailsList) {
		this.cropSaleDetailsList = cropSaleDetailsList;
	}

	public CropSupply getCropSupply() {
		return cropSupply;
	}

	public void setCropSupply(CropSupply cropSupply) {
		this.cropSupply = cropSupply;
	}

	public String getTotalSaleQty() {
		return totalSaleQty;
	}

	public void setTotalSaleQty(String totalSaleQty) {
		this.totalSaleQty = totalSaleQty;
	}

	public String getTotalSalePrice() {
		return totalSalePrice;
	}

	public void setTotalSalePrice(String totalSalePrice) {
		this.totalSalePrice = totalSalePrice;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * 
	 */

	public void setMailExport(boolean mailExport) {

		this.mailExport = mailExport;
	}

	public Date getsDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(startDate)) {
			try {
				sDate = df.parse(startDate);
			} catch (ParseException e) {
				LOGGER.error("Error parsing start date" + e.getMessage());
			}
		}
		return sDate;
	}

	public void setsDate(Date sDate) {

		this.sDate = sDate;
	}

	public String getProperty(String key) {

		if (mailExport) {
			return getText(key);
		}
		// Scheduler
		if (ObjectUtil.isEmpty(errors)) {
			errors = new Properties();
			try {
				errors.load(getClass().getResourceAsStream(getClass().getSimpleName() + "_en.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errors.getProperty(key);
	}

	public boolean isMailExport() {

		return mailExport;
	}

	public Date geteDate() {

		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		if (!StringUtil.isEmpty(endDate)) {
			try {
				eDate = df.parse(endDate);
				eDate.setTime(eDate.getTime() + 86399000);
			} catch (ParseException e) {
				LOGGER.error("Error parsing end date" + e.getMessage());
			}

		}
		return eDate;
	}

	/**
	 * Read data.
	 * 
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	public Map readData() {

		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), null);
		return data;
	}

	/**
	 * Gets the projection properties.
	 * 
	 * @param token
	 *            the token
	 * @return the projection properties
	 */
	@SuppressWarnings("rawtypes")
	public Map getProjectionProperties(String token) {

		Map<String, String> projectionProperties = new HashMap<String, String>();
		projectionProperties.put(IReportDAO.PROJ_GROUP, getText(token + IReportDAO.PROJ_GROUP));
		projectionProperties.put(IReportDAO.PROJ_SUM, getText(token + IReportDAO.PROJ_SUM));
		projectionProperties.put(IReportDAO.PROJ_OTHERS, getText(token + IReportDAO.PROJ_OTHERS));
		return projectionProperties;
	}

	/**
	 * Read data.
	 * 
	 * @param projectionToken
	 *            the projection token
	 * @return the map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map readData(String projectionToken) {

		Map<String, String> projectionProperties = !StringUtil.isEmpty(projectionToken)
				? getProjectionProperties(projectionToken) : null;
		Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(), getLimit(),
				getsDate(), geteDate(), filter, getPage(), projectionProperties);
		return data;
	}

	/**
	 * Read export data.
	 * 
	 * @return the map
	 */
	public Map readExportData() {

		Map data = reportService.listWithMultipleFiltering("desc", "id", getStartIndex(), getLimit(), getsDate(),
				new Date(), filter, getPage(), null);
		return data;
	}

	/**
	 * Gets the sord.
	 * 
	 * @return the sord
	 */
	public String getSord() {

		return sord;
	}

	/**
	 * Sets the sord.
	 * 
	 * @param sord
	 *            the new sord
	 */
	public void setSord(String sord) {

		this.sord = sord;
	}

	/**
	 * Gets the sidx.
	 * 
	 * @return the sidx
	 */
	public String getSidx() {

		return sidx;
	}

	/**
	 * Sets the sidx.
	 * 
	 * @param sidx
	 *            the new sidx
	 */
	public void setSidx(String sidx) {

		this.sidx = sidx;
	}

	/**
	 * Gets the limit.
	 * 
	 * @return the limit
	 */
	public int getLimit() {

		return rows;
	}

	public String getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(String dateOfSale) {
		this.dateOfSale = dateOfSale;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public Map<Integer, String> getCashType() {
		return cashType;
	}

	public void setCashType(Map<Integer, String> cashType) {
		this.cashType = cashType;
	}

	public String getSelectedBuyerValue() {
		return selectedBuyerValue;
	}

	public void setSelectedBuyerValue(String selectedBuyerValue) {
		this.selectedBuyerValue = selectedBuyerValue;
	}

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getCoOperative() {
		return coOperative;
	}

	public void setCoOperative(String coOperative) {
		this.coOperative = coOperative;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getProductAvailableUnit() {
		return productAvailableUnit;
	}

	public void setProductAvailableUnit(String productAvailableUnit) {
		this.productAvailableUnit = productAvailableUnit;
	}

	public void populateUnit() throws Exception {

		String result = "";

		JSONArray productArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedCrop)) {
			ProcurementProduct procurementProduct = productService.findUnitByCropId(Long.valueOf(selectedCrop));

			if (!ObjectUtil.isEmpty(procurementProduct) && procurementProduct.getTypes() != null) {
				productAvailableUnit = String.valueOf(procurementProduct.getTypes().getName());
				result = productAvailableUnit;
			}

			result = productAvailableUnit == null ? "" : productAvailableUnit;
		}
		productArr.add(getJSONObject("unit", result));

		sendAjaxResponse(productArr);
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public File getCropSaleImage() {
		return cropSaleImage;
	}

	public void setCropSaleImage(File cropSaleImage) {
		this.cropSaleImage = cropSaleImage;
	}

	public File getCropSaleImage1() {
		return cropSaleImage1;
	}

	public void setCropSaleImage1(File cropSaleImage1) {
		this.cropSaleImage1 = cropSaleImage1;
	}

	public CropSupplyImages getCropSupplyImages() {
		return cropSupplyImages;
	}

	public void setCropSupplyImages(CropSupplyImages cropSupplyImages) {
		this.cropSupplyImages = cropSupplyImages;
	}
	
	@SuppressWarnings("unchecked")
	public void populateCropSupplyList() {
		JSONArray array = new JSONArray();
		
		CropSupply cropSupply = productService.findCropSupplyById(supplyId);
		if (!ObjectUtil.isEmpty(cropSupply)) {
		
				JSONObject obj = new JSONObject();
					obj.put("transportDetail", !StringUtil.isEmpty(cropSupply.getTransportDetail()) ? cropSupply.getTransportDetail() : "");
					obj.put("vechicleNo", !StringUtil.isEmpty(cropSupply.getVehicleNo()) ? cropSupply.getVehicleNo() :"");
					obj.put("receiptInfor", !StringUtil.isEmpty(cropSupply.getInvoiceDetail()) ? cropSupply.getInvoiceDetail() : "");
					obj.put("poNumber", !StringUtil.isEmpty(cropSupply.getPoNumber()) ? cropSupply.getPoNumber():"");
					obj.put("paymentInfo", !StringUtil.isEmpty(cropSupply.getPaymentInfo())? cropSupply.getPaymentInfo() :"");
					
			
				
				obj.put("id", cropSupply.getId());

				array.add(obj);
			
			JSONObject mainObj = new JSONObject();
			mainObj.put("data", array);
			printAjaxResponse(mainObj, "text/html");
		}
	}

	public void populateCropSupplyData() {
		
		CropSupply cropSupply = productService.findCropSupplyById(supplyId);
		if (!ObjectUtil.isEmpty(cropSupply)) {
			
			if(!StringUtil.isEmpty(transportDetail)){
				cropSupply.setTransportDetail(transportDetail);
			}
			
			if(!StringUtil.isEmpty(vehicleNo)){
				cropSupply.setVehicleNo(vehicleNo);
			}
			if(!StringUtil.isEmpty(receiptInfor)){
				cropSupply.setInvoiceDetail(receiptInfor);
			}
			if(!StringUtil.isEmpty(poNumber)){
				cropSupply.setPoNumber(poNumber);
			}
			if(!StringUtil.isEmpty(paymentInfo)){
				cropSupply.setPaymentInfo(paymentInfo);
			}
			productService.updateCropSupply(cropSupply);
			
			JSONArray respArr = new JSONArray();
			respArr.add(getJSONObject("data", "success"));
		
			sendAjaxResponse(respArr);
		}
		
	}

	public String getCropSaleDdataJsonString() {
		return cropSaleDdataJsonString;
	}

	public void setCropSaleDdataJsonString(String cropSaleDdataJsonString) {
		this.cropSaleDdataJsonString = cropSaleDdataJsonString;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTransportDetail() {
		return transportDetail;
	}

	public void setTransportDetail(String transportDetail) {
		this.transportDetail = transportDetail;
	}

	public String getReceiptInfor() {
		return receiptInfor;
	}

	public void setReceiptInfor(String receiptInfor) {
		this.receiptInfor = receiptInfor;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

}
