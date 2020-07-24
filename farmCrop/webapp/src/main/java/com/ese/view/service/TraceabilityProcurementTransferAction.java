package com.ese.view.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.TransferDetails;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTFarmerDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.WebTransactionAction;

public class TraceabilityProcurementTransferAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private String selectedGroup;
	private String selectedVillage;
	private String date;
	private String selectedFrom;
	private String selectedTo;
	private String truckId;
	private String driver;
	private String transporter;
	private String jsonString;

	private String receiptNumber;
	private String mtnrDescription;

	private DecimalFormat df = new DecimalFormat("#.##");

	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IUniqueIDGenerator idGenerator;

	public String create() {
		return INPUT;
	}

	public String list() {
		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		return INPUT;
	}

	public String detail() throws Exception {
		return DETAIL;
	}

	public String data() throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void populateGroupList() {
		JSONArray jsonArr = new JSONArray();
		locationService.listOfGroup().stream().forEach(samithiObj -> {
			jsonArr.add(getJSONObject(samithiObj[2], samithiObj[1]));
		});
		sendAjaxResponse(jsonArr);
	}

	@SuppressWarnings("unchecked")
	public void populateICSByGroup() {
		JSONArray jsonArr = new JSONArray();
		/*
		 * farmerService.listIcsName().forEach(icsName -> {
		 * jsonArr.add(getJSONObject(icsName[0],
		 * getCatlogueValueByCode(String.valueOf(icsName[0])).getName())); });
		 */
		catalogueService.listCataloguesByType("27").stream().forEach(catalogue -> {
			jsonArr.add(getJSONObject(catalogue.getCode(), catalogue.getName()));
		});
		sendAjaxResponse(jsonArr);
	}

	public Map<Long, String> getProcurementCenterList() {
		return locationService
				.listCoOperativeAndSamithiByRevisionNo(0L, Warehouse.WarehouseTypes.PROCUREMENT_PLACE.ordinal())
				.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
	}

	public Map<Long, String> getGinnerCenterList() {
		return locationService.listCoOperativeAndSamithiByRevisionNo(0L, Warehouse.WarehouseTypes.GINNER.ordinal())
				.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
	}

	@SuppressWarnings("unchecked")
	public void populateTransferStockDetails() {
		ProcurementTraceabilityStockDetails procurementTraceabilityStockDetails = new ProcurementTraceabilityStockDetails();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		if (!StringUtil.isEmpty(getSelectedVillage()) && !StringUtil.isEmpty(getSelectedFrom())) {
			procurementTraceabilityStockDetails.setProcurementTraceabilityStock(new ProcurementTraceabilityStock());
			procurementTraceabilityStockDetails.getProcurementTraceabilityStock().setVillage(new Village());
			procurementTraceabilityStockDetails.getProcurementTraceabilityStock().setCoOperative(new Warehouse());
			procurementTraceabilityStockDetails.getProcurementTraceabilityStock()
					.setIcs(StringUtil.removeLastComma(getSelectedVillage()));
			procurementTraceabilityStockDetails.getProcurementTraceabilityStock().getCoOperative()
					.setId(Long.valueOf(getSelectedFrom()));
			procurementTraceabilityStockDetails.getProcurementTraceabilityStock().setSeason(getCurrentSeasonsCode());

			Map criteriaMap = new LinkedHashMap<>();
			criteriaMap.put(IReportDAO.PROJ_OTHERS, getLocaleProperty("ptOtherProps"));
			criteriaMap.put(IReportDAO.PROJ_GROUP, getLocaleProperty("ptGroupProps"));
			criteriaMap.put(IReportDAO.PROJ_SUM, getLocaleProperty("ptSumProps"));

			List<Object[]> transferList = productDistributionService
					.listProcurementTraceabilityDetailsByVillageAndProcurementCenter(
							procurementTraceabilityStockDetails, criteriaMap);

			jsonObject.put("draw", "1");
			jsonObject.put("recordsTotal", transferList.size());
			jsonObject.put("recordsFiltered", transferList.size());

			AtomicInteger sno = new AtomicInteger(1);
			transferList.stream().forEach(object -> {
				JSONArray data = new JSONArray();
				int sNo = sno.getAndIncrement();
				data.add("<input type='checkbox' class='form-control icheckbox' value='" + df.format(object[0]) + "'>");// id
				data.add(sNo);// SNO
				data.add("<input type='hidden' value='" + object[10] + "' id='farmer" + object[0] + "'>" + "<input type='hidden' value='" + object[1] + "' id='farmerName" + object[0] + "'>" +object[1]);// Farmer
																														// Name
				data.add(object[2]);// Farmer Code
				data.add(object[5]);//block
				data.add(object[4]);// Village
				data.add(object[6]);// SHG
				data.add(getCatlogueValueByCode(String.valueOf(object[7])).getName());// Cooperative
				
				data.add(object[13]);// Product
				if (object[12] != null) {
					ProcurementGrade pg = productDistributionService.findProcurementGradeByCode(object[12].toString());
					data.add(pg.getProcurementVariety().getName());//Variety
					data.add(pg.getName());//grade
				} else {
					data.add("");
					data.add("");
				}
				data.add("<input type='text' id='noBags" + object[0] + "' class='form-control' value='"
						+ df.format(object[8]) + "'>");// No
				// Of
				// bags
				data.add("<input type='text' id='netWeigh" + object[0] + "' class='form-control' value='"
						+ df.format(object[9]) + "'>");// Net
				// weight
				data.add("<input type='hidden' value='" + object[3] + "' id='ics" + object[0] + "'>"
						+ getCatlogueValueByCode(String.valueOf(object[3])).getName());// ICS
						
			/*	data.add("<input type='hidden' value='" + object[3] + "' id='weigt" + object[0] + "'>"
						+ df.format(object[6]));// ICS
				
				data.add("<input type='hidden' value='" + object[3] + "' id='bags" + object[0] + "'>"
						+ df.format(object[5]));// ICS
*/				jsonArr.add(data);
			});

		}
		jsonObject.put("data", jsonArr);
		printAjaxResponse(jsonObject, "text/html");
	}

	PMTDetail pmtDetailz = new PMTDetail();
	boolean flag = true;
	public void populateTransfer() {
		String result = "";
		StringBuilder errorMsg = new StringBuilder();
		if (!StringUtil.isEmpty(jsonString)) {
			Warehouse senderWarehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(getSelectedFrom()));
			Warehouse receiverWarehouse = locationService.findWarehouseWithoutTypeById(Long.valueOf(getSelectedTo()));
			Type listType1 = new TypeToken<List<TransferDetails>>() {
			}.getType();
			List<TransferDetails> transferDetailsList = new Gson().fromJson(jsonString, listType1);
			Set<PMTDetail> pmtDetailSet = new LinkedHashSet<>();
			Set<PMTFarmerDetail> pmtFarmerDetailSet = new LinkedHashSet<>();
			List<PMTDetail> detailsList = new LinkedList<>();
			transferDetailsList.stream().forEach(transferDetails -> {
				PMTDetail pmtDetail = new PMTDetail();
				ProcurementTraceabilityStockDetails procurementTraceabilityStock = productDistributionService
						.findProcurementTracabiltiyDetailsStockById(Long.valueOf(transferDetails.getId()));
				ProcurementGrade grade = productDistributionService.findProcurementGradeByCode(
						procurementTraceabilityStock.getProcurementTraceabilityStock().getGrade());
				pmtDetail.setProcurementProduct(
						procurementTraceabilityStock.getProcurementTraceabilityStock().getProcurementProduct());
				pmtDetail.setProcurementGrade(grade);

				if (Long.valueOf(transferDetails.getNoOfBags()) > procurementTraceabilityStock.getTotalNumberOfBags()) {
					flag=false;
					errorMsg.append("No Of Bags should be less than Available No Of Bags for Farmer :"+transferDetails.getFarmerName());
				} else {
					pmtDetail.setBags(transferDetails.getNoOfBags());
				}
				pmtDetail.setMtntNumberOfBags(Long.valueOf(transferDetails.getNoOfBags()));
				if(transferDetails.getNetWeight().equals("0")|| transferDetails.getNetWeight().equals("")){
					flag=false;
					errorMsg.append("Net weight should be greater than Zero");
				}else{
				if (Double.valueOf(transferDetails.getNetWeight()) > procurementTraceabilityStock.getTotalstock()) {
					flag=false;
					errorMsg.append("Net Weight should be less than Available Net Weight for Farmer : "+transferDetails.getFarmerName());
				} else {
					pmtDetail.setBags(transferDetails.getNetWeight());
				}
				pmtDetail.setMtntGrossWeight(Double.valueOf(transferDetails.getNetWeight()));
				pmtDetail.setMtntQuintalWeight(pmtDetail.getMtntGrossWeight() / 100);
				pmtDetail.setMtnrNumberOfBags(Long.valueOf(transferDetails.getNoOfBags()));
				pmtDetail.setMtnrGrossWeight(Double.valueOf(transferDetails.getNetWeight()) / 100);
				pmtDetail.setBranchId(getBranchId());
				pmtDetail.setIcs(transferDetails.getIcs());
				pmtDetail.setCoOperative(receiverWarehouse);
				pmtDetail.setStatus(0);
				pmtDetail.setFarmerId(transferDetails.getFarmer());
				detailsList.add(pmtDetail);

				PMTFarmerDetail pmtFarmerDetail = new PMTFarmerDetail();
				pmtFarmerDetail.setProcurementProduct(
						procurementTraceabilityStock.getProcurementTraceabilityStock().getProcurementProduct());
				pmtFarmerDetail.setProcurementGrade(grade);
				pmtFarmerDetail.setBags(transferDetails.getNoOfBags());
				pmtFarmerDetail.setMtntNumberOfBags(Long.valueOf(transferDetails.getNoOfBags()));
				pmtFarmerDetail.setMtntGrossWeight(Double.valueOf(transferDetails.getNetWeight()));
				pmtFarmerDetail.setMtnrNumberOfBags(Long.valueOf(transferDetails.getNoOfBags()));
				pmtFarmerDetail.setMtnrGrossWeight(Double.valueOf(transferDetails.getNetWeight()) / 100);
				pmtFarmerDetail.setBranchId(getBranchId());
				pmtFarmerDetail.setFarmer(transferDetails.getFarmer());
				pmtFarmerDetail.setCoOperative(receiverWarehouse);
				pmtFarmerDetail.setStatus(0);
				pmtFarmerDetail.setIcs(transferDetails.getIcs());
				pmtFarmerDetail.setSeason(getCurrentSeasonsCode());
				pmtFarmerDetailSet.add(pmtFarmerDetail);
				}
			});
			TransferInfo transerInfo = new TransferInfo();
			transerInfo.setAgentId(getUsername());
			transerInfo.setAgentName(getUserFullName());
			transerInfo.setDeviceId("NA");
			transerInfo.setDeviceName("NA");
			transerInfo.setServicePointId(senderWarehouse.getCode());
			transerInfo.setServicePointName(senderWarehouse.getWarehouseName());

			pmtDetailSet.addAll(detailsList);
			PMT pmt = new PMT();
			pmt.setCoOperative(senderWarehouse);
			pmt.setMtntTransferInfo(transerInfo);
			pmt.setMtntDate(DateUtil.convertStringToDate(date, getGeneralDateFormat()));
			pmt.setMtntDate(DateUtil.setTimeToDate(pmt.getMtntDate()));
			pmt.setBranchId(getBranchId());
			pmt.setStatusCode(PMT.Status.MTNT.ordinal());
			pmt.setTruckId(getTruckId());
			pmt.setDriverName(driver);
			pmt.setTransporter(transporter);
			pmt.setStatusCode(PMT.Status.MTNT.ordinal());
			pmt.setTrnType(PMT.TRN_TYPE_STOCK_TRNASFER);
			pmt.setSeasonCode(getCurrentSeasonsCode());
			pmt.setBranchId(getBranchId());
			pmt.setPmtDetails(pmtDetailSet);
			pmt.setPmtFarmerDetais(pmtFarmerDetailSet);

			if (flag && isDetailsExists(pmt)) {
				String mtntReceiptNumber = productDistributionService.addProcurementMTNT(pmt);

				receiptNumber = mtntReceiptNumber;

				if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
					result = getText("receiptno.empty");
				}
				productDistributionService.processPMTFarmerDetail(pmt);
				
				if (StringUtil.isEmpty(result)) {
					String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + "\\')\">" + "</button>";
					setMtnrDescription(getText("productTransferSucess") + "</br>" + getText("receiptNumber") + " : "
							+ receiptNumber + "<br/>" + receiptHtml);
					printAjaxResponse(getJSONObject("success",getMtnrDescription()), "text/html");
				//	printAjaxResponse(getMtnrDescription(), "text/html");
				} else {
					setMtnrDescription(getText(result));
					printAjaxResponse(getJSONObject("success",getMtnrDescription()), "text/html");
					//printAjaxResponse(getMtnrDescription(), "text/html");
				}
				
			}else{
				printAjaxResponse(getJSONObject("error",errorMsg.toString()), "text/html");
			}
		

		}
	}

	private boolean isDetailsExists(PMT pmt) {

		if (getActionErrors().size() == 0) {
			if (!ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
				return true;
			} else if (ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
				// LOGGER.info("METERIAL TRANSFER DETAIL SIZE IS EMPTY");
				addActionError(getText("empty.mtnt.details"));
				return false;
			}
			// } else if (ObjectUtil.isEmpty(pmt.getPmtAgentDetails())) {
			// // LOGGER.info("CITY WAREHOUSE DETAILS EMPTY");
			// addActionError(getText("empty.citywarehouse.details"));
			// return false;
			// }
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	public String getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public String getSelectedVillage() {
		return selectedVillage;
	}

	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSelectedFrom() {
		return selectedFrom;
	}

	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
	}

	public String getSelectedTo() {
		return selectedTo;
	}

	public void setSelectedTo(String selectedTo) {
		this.selectedTo = selectedTo;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
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

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getMtnrDescription() {
		return mtnrDescription;
	}

	public void setMtnrDescription(String mtnrDescription) {
		this.mtnrDescription = mtnrDescription;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

}
