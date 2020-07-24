/*
\\tabIndex * ProcurementReportAction.java
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
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FilterFieldData;
import com.ese.entity.util.LoanLedger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.DynamicReportConfigFilter;
import com.sourcetrace.eses.order.entity.txn.FarmerBalanceReport;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ExportUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Asset;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodicInspectionReportAction.
 */
public class FarmerLoanStatementReportAction extends BaseReportAction implements IExporter {

	private static final long serialVersionUID = 1L;
	private static final String QUANTITY = "quantity";
	private static final String TOTAL_AMT="totalAmt";
	private String mainGridCols;
	private static String subGridCols;
	
	private List<Object[]> farmList;
	
	private List<Object[]> farmerList;
	private String exportLimit;
	private String gridIdentity;
	private Map<String, String> warehouseMap = new HashMap<>();
	private String filterList;
	private String seasonCode;
	private String xlsFileName;
	private InputStream fileInputStream;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);
	private ESEAccount filter;
	private LoanLedger filters;
	private Map<String, String> fpoMap = new HashMap<>();
	private Map<String, String> farmerMap = new HashMap<>();
	private List<DynamicReportConfigFilter> reportConfigFilters = new LinkedList<>();
	protected List<String> fields = new ArrayList<String>();
	Map<Integer, String> balanceTypeList = new HashMap<Integer, String>();
	int serialNo = 1;
	private String daterange;
	private String tenantId;
	
	private String village;
	private String farmerId;
	private String groupId;
	
	
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String headerFields;

	private String branch;
	private String accountId;
	public String getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(String headerFields) {
		this.headerFields = headerFields;
	}

	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());		
		daterange = super.startDate + " - " + super.endDate;
		request.setAttribute(HEADING, getText(LIST));		
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setExportLimit(preferences.getPreferences().get(ESESystem.EXPORT_RECORD_LIMIT));
		}
		
		filter = new ESEAccount();
		return LIST;

		
	}
	
	public String subGridDetail() throws Exception {
		this.gridIdentity = "subGrid";
		
		filter = new ESEAccount();
		LoanLedger loanLedger = new LoanLedger();
		filter.setId(Long.valueOf(id));
		filter.setType(3);
		loanLedger.setAccount(filter);
		//filters.setBranchId(getBranchId());
		super.filter = filter;
		Map data = readData();
		return sendJSONResponse(data);
		
	}

	public String detail() throws Exception {
		this.gridIdentity = "detail";
		if(ObjectUtil.isEmpty(filter)){
		filter = new ESEAccount();
		}

		if (!StringUtil.isEmpty(getFarmerId())) {
			filter.setProfileId(getFarmerId());
		}
		
		if (!StringUtil.isEmpty(getGroupId())) {
			filter.setProfileId(getGroupId());
		}
		
		if(!StringUtil.isEmpty(getVillage())){
			filter.setVillage(getVillage());
		}
	
		filter.setBranchId(getBranchId());
		super.filter = this.filter;
		Map data = readData("farmerLoanStatement");
		return sendJSONResponse(data);
		
	}

	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (gridIdentity.equalsIgnoreCase("detail")) {
			Object[] datas = (Object[]) obj;
			
			Farmer f = null;
			if (datas.length > 0) {
				rows.add(!ObjectUtil.isEmpty( datas[2]) ? datas[2].toString() :""); // Acc No
				if (!StringUtil.isEmpty(datas[3])) {
					
					f = farmerService.findFarmerByFarmerId(String.valueOf(datas[3].toString()));
				if(!ObjectUtil.isEmpty(f)){
					rows.add(!StringUtil.isEmpty(f.getLastName())? f.getFirstName() +" "+f.getLastName() : f.getFirstName() );
					rows.add(!StringUtil.isEmpty(f.getFarmerCode())? f.getFarmerCode() : "");
					rows.add(!ObjectUtil.isEmpty(f.getVillage())?f.getVillage().getName() : "");
				}else{
					rows.add("NA");
					rows.add("NA");
					rows.add("NA");
				}
				
				}else{
					rows.add("NA");
					rows.add("NA");
					rows.add("NA");
				}
				
				ESEAccount account = farmerService.findEseAccountByFarmerId(datas[3].toString());
				
				rows.add(CurrencyUtil.getDecimalFormat(account.getLoanAmount(), "###.00"));
				rows.add(CurrencyUtil.getDecimalFormat(account.getLoanAmount() - account.getOutstandingLoanAmount(), "###.00"));
				rows.add(CurrencyUtil.getDecimalFormat(account.getOutstandingLoanAmount(), "###.00"));
				jsonObject.put("id", datas[1]);
				//jsonObject.put("accountId", datas[4]);
				jsonObject.put("cell", rows);
			}
		} else{
			LoanLedger loanLedger = (LoanLedger) obj;
			
			if (!ObjectUtil.isEmpty(loanLedger)) {
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
					rows.add(!ObjectUtil.isEmpty(loanLedger)
							? (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime()))
									? genDate.format(loanLedger.getTxnTime()) : "")
							: "");
				}
				rows.add(!StringUtil.isEmpty(loanLedger.getReceiptNo()) ?loanLedger.getReceiptNo() : "" );
				rows.add(!StringUtil.isEmpty(loanLedger.getLoanDesc()) ?loanLedger.getLoanDesc() : "" );
				rows.add(CurrencyUtil.getDecimalFormat(loanLedger.getActualAmount(), "###.00"));
				
				jsonObject.put("id", loanLedger.getId());
				
				jsonObject.put("cell", rows);
			}
		}
		return jsonObject;
	}

	
	public Map<String, String> getGroupTypeList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listGroupInfoByProcurementWithTypez();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> (String.valueOf(obj[1])+"-"+String.valueOf(obj[2]))));

		}
		return farmerMap;
	}
	public Map<String, String> getFarmersList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = farmerService.listFarmerInfoByProcurementWithTypez();
		List<String> testList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {

			farmerMap = farmerList.stream()
					.collect(Collectors.toMap(obj -> (String.valueOf(obj[0])), obj -> !StringUtil.isEmpty(String.valueOf(obj[3])) 
							? (String.valueOf(obj[1])+" "+String.valueOf(obj[3])+"-"+String.valueOf(obj[2])) 
									: String.valueOf(obj[1])+"-"+String.valueOf(obj[2])));

		}
		return farmerMap;
	}

	public Map<String, String> getFarmerFirstNameList() {
		Map<String, String> farmerMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmerList)) {
			farmerList = farmerService.listFarmerInfo();
		}
		farmerList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmerMap.put(String.valueOf(objArr[3]), String.valueOf(objArr[3]));
		});

		return farmerMap;
	}

	public Map<String, String> getFarmsList() {
		Map<String, String> farmMap = new LinkedHashMap<String, String>();
		if (ObjectUtil.isListEmpty(farmList)) {
			farmList = farmerService.listFarmInfo();
		}
		farmList.stream().forEach(obj -> {
			Object[] objArr = (Object[]) obj;
			farmMap.put(String.valueOf(objArr[1]), String.valueOf(objArr[2]));
		});

		return farmMap;
	}

	

	

	

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}

	public String getSubGridCols() {
		return subGridCols;
	}

	public void setSubGridCols(String subGridCols) {
		this.subGridCols = subGridCols;
	}

	
	public String getGridIdentity() {
		return gridIdentity;
	}

	public void setGridIdentity(String gridIdentity) {
		this.gridIdentity = gridIdentity;
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

	public List<DynamicReportConfigFilter> getReportConfigFilters() {
		return reportConfigFilters;
	}

	public void setReportConfigFilters(List<DynamicReportConfigFilter> reportConfigFilters) {
		this.reportConfigFilters = reportConfigFilters;
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

	
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Map<String, String> getWarehouseMap() {
		if (warehouseMap.size() <= 0) {
			warehouseMap = locationService.listWarehouse().stream()
					.collect(Collectors.toMap(warehouse -> String.valueOf(warehouse.getId()), Warehouse::getName));
		}
		return warehouseMap;
	}

	public Map<String, String> getFpoMap() {
		if (fpoMap.size() <= 0) {
			fpoMap = farmerService.listCatelogueType(getText("fpoType")).stream()
					.collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
		}
		return fpoMap;
	}

	public Map<String, String> getFarmerMap() {
		if (farmerMap.size() <= 0) {
			farmerService.listFarmerInfo().stream().forEach(farmer -> {
				farmerMap.put(String.valueOf(farmer[1]), String.valueOf(farmer[6]));
			});
		}
		return farmerMap;
	}

	private Map<String, Object> getTotalLoanDistributionDetails(LoanDistribution loanDistribution) {

		
		double qty = 0;
		double totalAmt=0;
		Map<String, Object> details = new HashMap<String, Object>();
		if (!ObjectUtil.isListEmpty(loanDistribution.getLoanDistributionDetail())) {
			for (LoanDistributionDetail detail : loanDistribution.getLoanDistributionDetail()) {
				qty = qty + detail.getQuantity();
				totalAmt=totalAmt + detail.getTotalAmt();
			}
		}
		details.put(QUANTITY, qty);
		details.put(TOTAL_AMT, totalAmt);
		return details;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	

	public void setWarehouseMap(Map<String, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setFpoMap(Map<String, String> fpoMap) {
		this.fpoMap = fpoMap;
	}

	public void setFarmerMap(Map<String, String> farmerMap) {
		this.farmerMap = farmerMap;
	}

	
	public Map<String, String> getVillageList() {

		Map<String, String> farmerMap = new LinkedHashMap<>();
		List<Object[]> farmerList = locationService.listOfVillageInfoByProcurement();
		List<String> villagesList = new ArrayList<>();
		if (!ObjectUtil.isEmpty(farmerList)) {
			farmerMap = farmerList.stream().collect(Collectors.toMap(u -> u[0].toString(),u -> u[1].toString()));
			

		}
		return farmerMap;
	}

	
	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
    
	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ESEAccount getFilter() {
		return filter;
	}

	public void setFilter(ESEAccount filter) {
		this.filter = filter;
	}

	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}


	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	
	public String getExportLimit() {
		return exportLimit;
	}

	public void setExportLimit(String exportLimit) {
		this.exportLimit = exportLimit;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public LoanLedger getFilters() {
		return filters;
	}

	public void setFilters(LoanLedger filters) {
		this.filters = filters;
	}
	
	 @SuppressWarnings( { "unchecked", "unchecked" })
	    public String subList() throws Exception {

	        JSONObject gridData = new JSONObject();
	        JSONArray rows = new JSONArray();
	        ESEAccount account = farmerService.findEseAccountById(Long.valueOf(id));
	       List<Object[]> listLoanLedger = farmerService
	                .listLoanLedgerByEseAccountId(account.getProfileId());

	        List<Object[]> listLoanLedgerLimitList = farmerService.listLoanLedgerByEseAccountId(account.getProfileId(), getStartIndex(), getLimit());

	        if (!ObjectUtil.isListEmpty(listLoanLedgerLimitList)) {

	            for (Object[] object : listLoanLedgerLimitList) {
	                LoanLedger loanLedgerObj = new LoanLedger();
	                String txnTime=object[0].toString();
	                
	                Date date = sdf.parse(txnTime);
	                loanLedgerObj.setTxnTime(date);
	                loanLedgerObj.setReceiptNo(String.valueOf(object[1]));
	                loanLedgerObj.setLoanDesc(String.valueOf(object[2]));
	                loanLedgerObj.setActualAmount(Double.valueOf(object[3].toString()));
	                rows.add(toJSON(loanLedgerObj, IReportDAO.SUB_GRID));
	            }
	        }

	        gridData.put(PAGE, getPage());

	        totalRecords = listLoanLedger.size();
	        gridData.put(TOTAL, java.lang.Math.ceil(totalRecords
	                / Double.valueOf(Integer.toString(getLimit()))));
	        gridData.put(IReportDAO.START_INDEX, getStartIndex());
	        gridData.put(IReportDAO.LIMIT, listLoanLedger.size());
	        gridData.put(IReportDAO.RECORD_COUNT, listLoanLedger.size());
	        gridData.put(ROWS, rows);
	        PrintWriter out = response.getWriter();
	        out.println(gridData.toString());
	        return null;
	    }
	
	 @SuppressWarnings("unchecked")
	    public JSONObject toJSON(Object obj, String grid) {
		 LoanLedger loanLedger = (LoanLedger) obj;
		 ESESystem preferences = preferncesService.findPrefernceById("1");
	        JSONObject jsonObject = new JSONObject();
	        JSONArray rows = new JSONArray();
	        if (!ObjectUtil.isEmpty(loanLedger)) {
				if (!StringUtil.isEmpty(preferences)) {
					DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
					rows.add(!ObjectUtil.isEmpty(loanLedger)
							? (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime()))
									? genDate.format(loanLedger.getTxnTime()) : "")
							: "");
				}
				rows.add(!StringUtil.isEmpty(loanLedger.getReceiptNo()) ?loanLedger.getReceiptNo() : "" );
				rows.add(!StringUtil.isEmpty(loanLedger.getLoanDesc()) ?loanLedger.getLoanDesc() : "" );
				
				rows.add(CurrencyUtil.getDecimalFormat(loanLedger.getActualAmount(), "###.00"));
				
				jsonObject.put("id", loanLedger.getId());
				
				jsonObject.put("cell", rows);
			}
		 return jsonObject;
	 }
	 
	 
	 public String populateXLS() throws Exception {

			InputStream is = getFarmerLoanStatementDataStream();
			setXlsFileName(getLocaleProperty("farmerLoanStatementReportList") + fileNameDateFormat.format(new Date()));
			Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
			fileMap.put(xlsFileName, is);
			setFileInputStream(
					FileUtil.createFileInputStreamToZipFile(getLocaleProperty("farmerLoanStatementReportList"), fileMap, ".xls"));

			return "xls";
		}


		private InputStream getFarmerLoanStatementDataStream() throws IOException, ParseException {
			LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
			LinkedList<String> headersList = new LinkedList<String>();
			LinkedList<String> subGridHeadersList = new LinkedList<String>();
			List<List<String>> dataList = new ArrayList<List<String>>();
			List<List<String>> subGridDataList = new ArrayList<List<String>>();
			Map<Long, List<List<String>>> subListMap = new LinkedHashMap<Long, List<List<String>>>();
			
			
			ESESystem preferences = preferncesService.findPrefernceById("1");
			this.gridIdentity = "detail";
			if(ObjectUtil.isEmpty(filter)){
			filter = new ESEAccount();
			}

			if (!StringUtil.isEmpty(getFarmerId())) {
				filter.setProfileId(getFarmerId());
				Farmer farmer = farmerService.findFarmerByFarmerId(filter.getProfileId());
				filters.put(getLocaleProperty("farmerName"), farmer.getFirstName());
			}
			
			if (!StringUtil.isEmpty(getGroupId())) {
				filter.setProfileId(getGroupId());
				Farmer farmer = farmerService.findFarmerByFarmerId(filter.getProfileId());
				filters.put(getLocaleProperty("groupName"), farmer.getFirstName());
			}
			
			if(!StringUtil.isEmpty(getVillage())){
				filter.setVillage(getVillage());
				Village village= locationService.findVillage(Long.valueOf(getVillage()));
				filters.put(getLocaleProperty("villageName"), village.getName());
			}
			/*if (!StringUtil.isEmpty(villageName)) {
				Village v =new Village();
				Farmer f = new Farmer();
				v.setName(villageName);
				f.setVillage(v);
				filter.setProfileId(f.getFarmerId());
			}
			*/
			filter.setBranchId(getBranchId());
			super.filter = this.filter;
			Map datas = readData("farmerLoanStatement");
			
			String headers=getLocaleProperty("exportColumnFarmerLoanStatementHeader");
				String subGridHeaders = getLocaleProperty("exportFarmerLoanStatementReportSubColumnHeader");
			if (headers != null) {

				for (String name : headers.split(",")) {
					
					if(name.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))				
					{				
						name=name.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());				
					}else if(name.trim().contains(ESESystem.AREA_TYPE_EXPORT))						
					{						
						name=name.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());						
					}			
					
					
					headersList.add(getLocaleProperty(name));
				}
			}

			if (subGridHeaders != null) {
				for (String name : subGridHeaders.split(",")) {
					
					if(name.trim().contains(ESESystem.CURRENCY_SYMPOL_EXPORT))				
					{				
						name=name.trim().replace(ESESystem.CURRENCY_SYMPOL_EXPORT, getCurrencyType());				
					}else if(name.trim().contains(ESESystem.AREA_TYPE_EXPORT))						
					{						
						name=name.trim().replace(ESESystem.AREA_TYPE_EXPORT, getAreaType());						
					}			
					
					
					subGridHeadersList.add(getLocaleProperty(name));
				}
			}
			
			
			for (Object record : (List) datas.get(ROWS)) {
				List<String> list = getDataForReport(record);
				Object[] dataa = (Object[]) record;

				this.gridIdentity = "subGrid";
				List<LoanLedger> sublist = new ArrayList<>();
				 List<Object[]> listLoanLedgerLimitList = farmerService.listLoanLedgerByEseAccountId(dataa[3].toString());

			        if (!ObjectUtil.isListEmpty(listLoanLedgerLimitList)) {

			            for (Object[] object : listLoanLedgerLimitList) {
			                LoanLedger loanLedgerObj = new LoanLedger();
			                String txnTime=object[0].toString();
			                
			                Date date = sdf.parse(txnTime);
			                loanLedgerObj.setTxnTime(date);
			                loanLedgerObj.setReceiptNo(String.valueOf(object[1]));
			                loanLedgerObj.setLoanDesc(String.valueOf(object[2]));
			                loanLedgerObj.setActualAmount(Double.valueOf(object[3].toString()));
			                sublist.add(loanLedgerObj);
			            }
			            
			        }

				for (Object record1 : (List) sublist) {
					List<String> subList = getSubGridDataForReport(record1);
					if (subListMap.containsKey(Long.valueOf(list.get(0)))) {
						subListMap.get(Long.valueOf(list.get(0))).add(subList);
					} else {
						List<List<String>> newList = new ArrayList<List<String>>();
						newList.add(subList);
						subListMap.put(Long.parseLong(list.get(0)), newList);
					}
				}

				/*list.remove(list.size() -1);*/
				dataList.add(list);

			}

			Asset existingAssetLogin = clientService.findAssetsByAssetCode(Asset.APP_LOGO);
			String title = getText("farmerLoanStatementReportList");
			String reportName = getText("exportFarmerLoanStatementTitle");
			InputStream stream = ExportUtil.exportXLSWithSubGrid(dataList, headersList, filters, title, getText("filter"),
					reportName, existingAssetLogin.getFile(), subGridHeadersList, subListMap);

			return stream;

		}
		
		public List<String> getDataForReport(Object obj) {
			List<String> dataList = new ArrayList<>();

			Object[] datas = (Object[]) obj;
			
			Farmer f = null;
			if (datas.length > 0) {
				dataList.add(datas[1].toString());//id
				dataList.add(!ObjectUtil.isEmpty(datas[2]) ? datas[2].toString() : ""); // Acc No
				if (!ObjectUtil.isEmpty(datas[3])) {
					
					f = farmerService.findFarmerByFarmerId(String.valueOf(datas[3].toString()));
				if(!ObjectUtil.isEmpty(f)){
					dataList.add(!StringUtil.isEmpty(f.getLastName())? f.getFirstName() +" "+f.getLastName() : f.getFirstName() );
					dataList.add(!StringUtil.isEmpty(f.getFarmerCode())? f.getFarmerCode() : "");
					dataList.add(!ObjectUtil.isEmpty(f.getVillage())?f.getVillage().getName() : "");
				}else{
					dataList.add("NA");
					dataList.add("NA");
					dataList.add("NA");
				}
				
				}else{
					dataList.add("NA");
					dataList.add("NA");
					dataList.add("NA");
				}
				
				ESEAccount account = farmerService.findEseAccountByFarmerId(datas[3].toString());
				
				dataList.add(CurrencyUtil.getDecimalFormat(account.getLoanAmount(), "###.0"));
				dataList.add(CurrencyUtil.getDecimalFormat(account.getLoanAmount() - account.getOutstandingLoanAmount(), "###.0"));
				dataList.add(CurrencyUtil.getDecimalFormat(account.getOutstandingLoanAmount(), "###.0"));
				
			}
		
			return dataList;
		}
		
		public List<String> getSubGridDataForReport(Object obj) throws ParseException {
			
			
			List<String> subGridDataList = new ArrayList<>();
		
			 LoanLedger loanLedger = (LoanLedger) obj;
			 ESESystem preferences = preferncesService.findPrefernceById("1");
		      
		        if (!ObjectUtil.isEmpty(loanLedger)) {
					if (!StringUtil.isEmpty(preferences)) {
						DateFormat genDate = new SimpleDateFormat(getGeneralDateFormat());
						subGridDataList.add(!ObjectUtil.isEmpty(loanLedger)
								? (!StringUtil.isEmpty(genDate.format(loanLedger.getTxnTime()))
										? genDate.format(loanLedger.getTxnTime()) : "")
								: "");
					}
					subGridDataList.add(!StringUtil.isEmpty(loanLedger.getReceiptNo()) ?loanLedger.getReceiptNo() : "" );
					subGridDataList.add(!StringUtil.isEmpty(loanLedger.getLoanDesc()) ?loanLedger.getLoanDesc() : "" );
					
					subGridDataList.add(CurrencyUtil.getDecimalFormat(loanLedger.getActualAmount(), "###.0"));
		        }
			return subGridDataList;
		}
		
		public int getPicIndex(HSSFWorkbook wb) throws IOException {

			int index = -1;

			byte[] picData = null;
			picData = clientService.findLogoByCode(Asset.APP_LOGO);

			if (picData != null)
				index = wb.addPicture(picData, HSSFWorkbook.PICTURE_TYPE_JPEG);

			return index;
		}

		@Override
		public InputStream getExportDataStream(String exportType) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getVillage() {
			return village;
		}

		public void setVillage(String village) {
			this.village = village;
		}

}