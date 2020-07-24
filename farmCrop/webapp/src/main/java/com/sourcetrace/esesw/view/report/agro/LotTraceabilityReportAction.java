package com.sourcetrace.esesw.view.report.agro;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.view.profile.FarmerAction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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
import com.lowagie.text.pdf.codec.Base64.OutputStream;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig.LIST_METHOD;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransferDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfig;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerScheme;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates.typesOFCordinates;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

import sun.misc.BASE64Decoder;

public class LotTraceabilityReportAction extends BaseReportAction {
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	DecimalFormat formatter = new DecimalFormat("0.00");
	
	private String farmCode;
	private String farmerId;
	private String farmId;
	private String selectedLotNo;
	
	public String list() {
		return LIST;
	}

	public void populateLotNoList() {
		JSONArray lotNoArr = new JSONArray();
		List<String> lotNoList = productDistributionService.listLotNoFromFarmerTraceabilityData();
		if (!ObjectUtil.isEmpty(lotNoList)) {
			lotNoList.forEach(obj -> {
				lotNoArr.add(getJSONObject(String.valueOf(obj), String.valueOf(obj.toString())));
			});
		}
		sendAjaxResponse(lotNoArr);
	}
	
	public void populateWarehouseList(){
		
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Warehouse> warehouseList=locationService.listCoOperativeAndSamithi();
		if(warehouseList!=null && !ObjectUtil.isListEmpty(warehouseList)){
			warehouseList.stream().filter(f->f.getTypez()>1).forEach(a->{
				JSONObject warehouseArr=new JSONObject();
				warehouseArr.put("type", a.getTypez());
				warehouseArr.put("name", a.getName());
				warehouseArr.put("lati", a.getLatitude());
				warehouseArr.put("longi", a.getLongitude());
				jsonObjects.add(warehouseArr);
			});
		}
		printAjaxResponse(jsonObjects, "text/html");
	}
	
	@SuppressWarnings("unchecked")
	public void populateFarmsMap() throws Exception {

		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		List<Object[]> farmMapObjectList = new ArrayList<Object[]>();
		
		Farm farm = new Farm();
		Farmer farmer = new Farmer();

		farmer.setBranchId(getBranchId());
		if(selectedLotNo!=null && !StringUtil.isEmpty(selectedLotNo)){
		farmMapObjectList=farmerService.listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(selectedLotNo,getBranchId());
		if(farmMapObjectList!=null && !ObjectUtil.isListEmpty(farmMapObjectList)){
		farmMapObjectList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			if (!StringUtil.isEmpty(objArr[2]) && !StringUtil.isEmpty(objArr[3])) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", objArr[0]);

				jsonObject.put("latitude", objArr[1]);
				jsonObject.put("longtitude", objArr[2]);
				jsonObject.put("farmCode", objArr[3]);
				jsonObject.put("farmerName", objArr[4]);
				jsonObject.put("farmName", objArr[5]);
				jsonObject.put("farmId", objArr[6]);
				jsonObject.put("certified", objArr[7]);
				jsonObject.put("organicStatus", "0");
				jsonObjects.add(jsonObject);
			}
		});
		}
		
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	@SuppressWarnings("unchecked")
	public void populateLoadFields() throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
	
		Object[] count = farmerService.findFarmersCountFromLotTraceByLotNo(StringUtil.removeLastComma(selectedLotNo));
		if(count!=null && !ObjectUtil.isEmpty(count)){
		jsonObject.put("farmerCount", !ObjectUtil.isEmpty(count[0]) ? String.valueOf(count[0]) : "0");
		jsonObject.put("villageCount", count[1]);
		}
		jsonArray.add(jsonObject);
		response.setContentType("text/JSON");
		PrintWriter out = response.getWriter();
		if (jsonArray.size() > 0)
			out.println(jsonArray.toString());
		}
	
	
	public String getSelectedLotNo() {
		return selectedLotNo;
	}

	public void setSelectedLotNo(String selectedLotNo) {
		this.selectedLotNo = selectedLotNo;
	}
	Long cropId=0L;
	public void populateImg() {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		 cropId=farmerService.findCropIdByFarmCode(farmCode);
	
		if (!ObjectUtil.isEmpty(farmerId)) {
			List<Object[]> farmer = farmerService.listFarmerFarmInfoByVillageIdImg(cropId,
					Long.valueOf(farmerId),Long.valueOf(farmId));
			if (!ObjectUtil.isListEmpty(farmer)) {

				farmer.stream().forEach(obj -> {
					JSONObject jsonObject = null;
					jsonObject = new JSONObject();
					Object[] objArr = (Object[]) obj;
					jsonObject.put("id", objArr[0]);
					jsonObject.put("farmerId", ObjectUtil.isEmpty(objArr[1]) ? "-" : objArr[1].toString());
					jsonObject.put("farmerName", ObjectUtil.isEmpty(objArr[3]) ? "-" : objArr[3].toString());
					jsonObject.put("farmName", objArr[5]);
					jsonObject.put("latitude", objArr[7]);
					jsonObject.put("longtitude", objArr[8]);
					jsonObject.put("landmark", objArr[9]);

					jsonObject.put("village", objArr[12]);
					if(!StringUtil.isEmpty(objArr[10])&& objArr[10]!="")
					{
						jsonObject.put("totalLand", formatter.format(Double.valueOf(String.valueOf(objArr[10]))));
					}
					if(!StringUtil.isEmpty(objArr[11]) && objArr[11]!="")
					{
						jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[11]))));
					}
					

					if (!StringUtil.isEmpty(objArr[13]) && objArr[13] instanceof byte[]) {
						byte[] image = (byte[]) objArr[13];

						if (!StringUtil.isEmpty(image)) {
							jsonObject.put("image", "data:image/png;base64, " + Base64Util.encoder(image));
						} else {
							jsonObject.put("image", "img/no-image.png");
						}
					} else {
						jsonObject.put("image", "img/no-image.png");
					}
					
					if (getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && cropId!=0) {
						jsonObject.put("proposedLand", formatter.format(Double.valueOf(String.valueOf(objArr[18]))));
					}
					
					if(cropId!=0)
					{
						jsonObject.put("cropName", objArr[17]);
						if (!ObjectUtil.isEmpty(objArr[19])) {
							String replaceDot = objArr[19].toString().replace(".0", "");
							jsonObject.put("estHavstDate", String.valueOf(
									DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
						}
						jsonObject.put("estYield", formatter.format(Double.valueOf(String.valueOf(objArr[20]))));
					}
					
					if(!ObjectUtil.isEmpty(objArr[14]) && String.valueOf(objArr[14]).length()>0)
					{
						HarvestSeason harvestSeason=farmerService.findHarvestSeasonByCode(String.valueOf(objArr[14]));
						jsonObject.put("cropSeason", harvestSeason.getName());
					}
				
				//	
					jsonObject.put("branch", objArr[15]);

					if (!ObjectUtil.isEmpty(objArr[16])) {
						String replaceDot = objArr[16].toString().replace(".0", "");
						jsonObject.put("doj", String.valueOf(
								DateUtil.convertDateFormat(replaceDot, DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
					}
					if(getCurrentTenantId().equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)){
						FarmIcsConversion farmIcsConversion = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(farmId));
						
						if (!ObjectUtil.isEmpty(farmIcsConversion)) {
							jsonObject.put("inspDate",
									String.valueOf(DateUtil.convertDateFormat(String.valueOf(farmIcsConversion.getInspectionDate()),
											DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
							jsonObject.put("inspectedBy", String.valueOf(farmIcsConversion.getInspectorName()));
								FarmIcsConversion fIcs = farmerService.findFarmIcsConversionByFarmIdWithActive(Long.valueOf(farmId));
								if(!ObjectUtil.isEmpty(fIcs)&&fIcs!=null){
									jsonObject.put("icsType",fIcs.getOrganicStatus().equalsIgnoreCase("3") ? getLocaleProperty("alrdyCertified") : getLocaleProperty("inprocess"));
								}else{
									jsonObject.put("icsType","Conventional");
								}
							
							//jsonObject.put("icsType", String.valueOf(farmIcsConversion.getIcsType()));
						}
						else{
							jsonObject.put("icsType","Conventional");
						}
					}
					else{
					Object[] periodObj = farmerService.findPeriodicInsDateByFarmCode(farmCode);
					if (!ObjectUtil.isEmpty(periodObj)) {
						jsonObject.put("inspDate",
								String.valueOf(DateUtil.convertDateFormat(String.valueOf(periodObj[0]),
										DateUtil.TXN_DATE_TIME, DateUtil.DATE_FORMAT)));
						jsonObject.put("inspectedBy", String.valueOf(periodObj[1]));
					}
					}
					jsonObjects.add(jsonObject);
				});
			}

		}
		printAjaxResponse(jsonObjects, "text/html");

	}
}
