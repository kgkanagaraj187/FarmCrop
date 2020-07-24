package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.CityWarehouseDetail;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransferDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTAgentDetail;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;



public class ColdStorageStockTransferAction
  extends WebTransactionAction
{
  private static final String PROCUREMENT_MTNR = "319";
  private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  
  private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
  @Autowired
  private ILocationService locationService;
  @Autowired
  private IProductDistributionService productDistributionService;
  @Autowired
  private IUniqueIDGenerator idGenerator;
  @Autowired
  private IClientService clientService;
  @Autowired
  private IFarmerService farmerService;
  private List<GradeMaster> gradeMasterList;
  private Map<String, String> productMap = new LinkedHashMap();
  private Map<String, String> coOperativeMap = new LinkedHashMap();
  private Map<String, String> recCoOperativeMap = new LinkedHashMap();
  private Map<String, String> farmerMap = new LinkedHashMap();
  
  private String gradeInputString;
  private String gradeMasterIdList;
  private String startDate;
  private String receiptNumber;
  private String mtnrDescription;
  private String truckId;
  private String driverId;
  private String selectedProduct;
  private String selectedCoOperative;
  private String selectedRecCoOperative;
  private String selectedWarehouse;
  private String selectedColdStorageName;
  private String selectedBatchNo;
  private String selectedBuyer;
  private String poNumber;
  private String invoiceNumber;
  private HashMap<String, Object> mtnrPrintMap = new LinkedHashMap();
  private String[] batchNo;
  private String seasonName;
  
  private String selectedFarmer;
  
  public ColdStorageStockTransferAction() {}
  
  public String create()
  {
    request.setAttribute("heading", getText("create"));
    System.out.println("create");
    if (!StringUtil.isEmpty(selectedProduct))
    {


      if (ObjectUtil.isEmpty(request.getSession().getAttribute(agentId + "_" + "319" + "_" + "isFormResubmit")))
      {

        reset();
        return "input";
      }
      if (!ObjectUtil.isLong(selectedRecCoOperative)) {
        reset();
        return "input";
      }
      PMT proceurementMTNR = new PMT();
      PMT pmt = new PMT();
      
      Agent senderAgent = agentService.findAgentByProfileId(agentId);
      Agent recAgent = agentService.findAgentByCoOperative(
        Long.parseLong(selectedRecCoOperative));
      

      if ((!ObjectUtil.isEmpty(recAgent)) && (!ObjectUtil.isEmpty(recAgent.getCooperative()))) {
       
        proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        proceurementMTNR.setMtnrDate(DateUtil.setTimeToDate(proceurementMTNR.getMtnrDate()));
        proceurementMTNR.setBranchId(getBranchId());
        proceurementMTNR.setAgentRef(recAgent);
        pmt.setMtntDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        pmt.setMtntDate(DateUtil.setTimeToDate(pmt.getMtntDate()));
        pmt.setBranchId(getBranchId());
      
        pmt.setStatusCode(PMT.Status.MTNT.ordinal());
        pmt.setAgentRef(senderAgent);
        

        ProcurementProduct procurementProduct = productDistributionService.findProcurementProductById(Long.valueOf(selectedProduct).longValue());
        if (!ObjectUtil.isEmpty(procurementProduct)) {
          Map<String, String[]> gradeInputMap = formGradeInputMapFromString(gradeInputString);
          proceurementMTNR.setPmtDetails(new LinkedHashSet());
          
          if ((!ObjectUtil.isEmpty(gradeInputMap)) && 
            (gradeInputMap.size() > 0)) {
            Set<PMTDetail> pmtDetails = new HashSet();
            pmt.setPmtDetails(pmtDetails);
            for (Map.Entry<String, String[]> entry : gradeInputMap.entrySet()) {
              String id = (String)entry.getKey();
              String[] gradeValues = (String[])entry.getValue();
              
              GradeMaster gradeMaster = productDistributionService.findGradeById(Long.valueOf(id).longValue());
              if (!ObjectUtil.isEmpty(gradeMaster)) {
                PMTDetail pmtDetail = new PMTDetail();
                pmtDetail.setPmt(proceurementMTNR);
                pmtDetail.setProcurementProduct(procurementProduct);
                pmtDetail.setGradeMaster(gradeMaster);
                pmtDetail.setMtnrNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail.setMtnrGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail.setCoOperative(senderAgent.getCooperative());
                proceurementMTNR.getPmtDetails().add(pmtDetail);
                
                PMTDetail pmtDetail1 = new PMTDetail();
                pmtDetail1.setGradeMaster(gradeMaster);
                pmtDetail1.setProcurementProduct(procurementProduct);
                pmtDetail1.setMtntNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail1.setMtntGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail1.setPmt(pmt);
                pmtDetail1.setCoOperative(recAgent.getCooperative());
                
                pmt.getPmtDetails().add(pmtDetail1);
              }
            }
          }
          
          pmt.setCoOperative(senderAgent.getCooperative());
          pmt.setTruckId(truckId);
          pmt.setDriverName(driverId);
          pmt.setTrnType("STOCK_TRANSFER");
          
          proceurementMTNR.setCoOperative(recAgent.getCooperative());
          proceurementMTNR.setTruckId(truckId);
          proceurementMTNR.setDriverName(driverId);
          proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
          proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
          proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
          proceurementMTNR.setTrnType("STOCK_TRANSFER");
          
          if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
       /*     if (isDetailsExists(pmt))
            {
              String mtntReceiptNumber = productDistributionService.addProcurementMTNT(pmt);
              if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
                addActionError(getText("receiptno.empty"));
              }
            }
            */
            productDistributionService.editPMTForMTNR(proceurementMTNR);




          }
          else
          {



            setMtnrDescription(getText("mtnrFailed") + " : " + 
              getText("grades.not.exists"));
          }
          resetTimer();
        } else {
          setMtnrDescription(getText("mtnrFailed") + " : " + 
            getText("product.not.exists"));
          resetTimer();
        }
      } else {
        setMtnrDescription(getText("mtnrFailed") + " : " + getText("agent.not.exists"));
        resetTimer();
      }
    } else {
      if (!StringUtil.isEmpty(agentId)) {
        request.setAttribute("agentId", agentId);
        Agent agent = agentService.findAgentByProfileId(agentId);
        if ((!ObjectUtil.isEmpty(agent)) && (2 == agent.getBodStatus())) {
          reset();
          return "input";
        }
      }
      return "input";
    }
    return "input";
  }
  


  private void reset()
  {
    DateFormat dateFormat = new SimpleDateFormat(getGeneralDateFormat());
    Calendar currentDate = Calendar.getInstance();
    startDate = dateFormat.format(currentDate.getTime());
    request.setAttribute("heading", getText("create"));
    setMtnrDescription(null);
   
  }
  


  public void populateColdStorageName() {
		List<WarehouseStorageMap> coldStorage = new ArrayList<>();
		if (!selectedWarehouse.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedWarehouse))
				&& !selectedWarehouse.equalsIgnoreCase("0")) {
			coldStorage = locationService.listWarehouseStorageMapByWarehouseID(Long.valueOf(selectedWarehouse));
		}
		JSONArray coldStorageArr = new JSONArray();
		if (!ObjectUtil.isEmpty(coldStorage)) {
			coldStorage.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
				FarmCatalogue catalogue = getCatlogueValueByCode(obj.getColdStorageName());
				coldStorageArr.add(getJSONObject(obj.getColdStorageName(), catalogue.getName()));
			});
		}
		sendAjaxResponse(coldStorageArr);
	}

  
  @SuppressWarnings("unchecked")
public void populateProduct() {
		
		if (!StringUtil.isEmpty(selectedWarehouse) && !StringUtil.isEmpty(selectedColdStorageName)) {
			List<Object[]> productList=productDistributionService.listProductByCityWarehouseAndColdStorage(Long.valueOf(selectedWarehouse),selectedColdStorageName);

		
		JSONArray productArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(productList)) {
			productList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {			
				productArr.add(getJSONObject(obj[0], obj[1]));
			});
		}
		sendAjaxResponse(productArr);
	}
  }
  @SuppressWarnings("unchecked")
  public void populateBatchNo() {
		
		if (!StringUtil.isEmpty(selectedWarehouse) && !StringUtil.isEmpty(selectedColdStorageName) && !StringUtil.isEmpty(selectedProduct)) {
			List<String> batchNoList=productDistributionService.listBatchNoByCityWarehouseAndColdStorageAndProduct(Long.valueOf(selectedWarehouse),selectedColdStorageName,Long.valueOf(selectedProduct));

		
		JSONArray batchNoArr = new JSONArray();
		if (!ObjectUtil.isListEmpty(batchNoList)) {
			for(String arr : batchNoList){
			/*	if (this.getCurrentTenantId().equalsIgnoreCase("griffith")){
					batchNoArr.add(getJSONObject(arr, farmerService.findFarmerDynamicFieldsValueById(arr).getFieldValue()));
				}else{*/
					batchNoArr.add(getJSONObject(arr, arr));
				/*}*/
			}
			
		}
		sendAjaxResponse(batchNoArr);
	}
}
  
  @SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object code, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", code);
		jsonObject.put("name", name);
		return jsonObject;
	}





 
  private boolean isDetailsExists(ColdStorageStockTransfer coldStorageStockTransfer)
  {
    if (getActionErrors().size() == 0) {
      if (!ObjectUtil.isListEmpty(coldStorageStockTransfer.getColdStorageStockTransferDetail()))
        return true;
      if (ObjectUtil.isListEmpty(coldStorageStockTransfer.getColdStorageStockTransferDetail()))
      {
        addActionError(getText("empty.coldStorageStockTransfer.details"));
        return false;
      }
    }
        return false;
  }
  


  private Set<ColdStorageStockTransferDetail> formColdStorageStockTransferDetails() {
	  Set<ColdStorageStockTransferDetail> coldStorageStockTransferDetails = new LinkedHashSet<>();
	  
	  if (!StringUtil.isEmpty(gradeInputString)) {
		  List<String> productsList = Arrays.asList(gradeInputString.split("\\|\\|"));
		  productsList.stream().filter(obj -> !StringUtil.isEmpty(obj)).forEach(products -> {
			  ColdStorageStockTransferDetail transferDetail = new ColdStorageStockTransferDetail();
			  List<String> list = Arrays.asList(products.split("~~"));
			  ProcurementVariety variety = productDistributionService
						.findProcurementVariertyById((Long.valueOf(list.get(0))));
			  transferDetail.setProcurementVariety(!ObjectUtil.isEmpty(variety) ? variety : null);
			  transferDetail.setProcurementProduct(!ObjectUtil.isEmpty(variety) ? variety.getProcurementProduct() : null);
			  transferDetail.setBlockName(list.get(1).toString());
			  transferDetail.setFloorName(list.get(2).toString());
			  transferDetail.setBayNumber(list.get(3).toString());
			  transferDetail.setNoOfBags(Long.valueOf(list.get(4)));
			  transferDetail.setQty(Double.valueOf(list.get(5)));
			  Farmer farmerList = farmerService.findFarmerById(Long.valueOf(list.get(6)));
			  transferDetail.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
			  transferDetail.setBatchNo(list.get(7).toString());
			  coldStorageStockTransferDetails.add(transferDetail);
		  });
	  }
	  
	  
	return coldStorageStockTransferDetails;
	  
  }

  private Map<String, String[]> formGradeInputMapFromString(String inputString)
  {
    Map<String, String[]> returnMap = new LinkedHashMap();
    if (!StringUtil.isEmpty(inputString)) {
      String[] inputArray = inputString.split("\\|\\|");
      for (String gradeStr : inputArray) {
        String[] gradeDetailsArray = gradeStr.split("~~");
        if ((!ObjectUtil.isEmpty(gradeDetailsArray)) && (Double.valueOf(gradeDetailsArray[5].trim()).doubleValue() > 0.0D)) {
          returnMap.put(gradeDetailsArray[0], new String[] { gradeDetailsArray[1], gradeDetailsArray[2] , gradeDetailsArray[3], gradeDetailsArray[4],gradeDetailsArray[5],gradeDetailsArray[6]});
        }
      }
    }
    
    return returnMap;
  }
  


  public void populateMTNTDetails()
    throws Exception
  {
    String result = "";
    if (!StringUtil.isEmpty(receiptNumber)) {
      PMT procurementMTNT = productDistributionService.findPMTByReceiptNumber(receiptNumber, PMT.Status.MTNT
        .ordinal());
      if (!ObjectUtil.isEmpty(procurementMTNT))
      {






        result = (!ObjectUtil.isEmpty(procurementMTNT.getMtntDate()) ? DateUtil.convertDateToString(procurementMTNT.getMtntDate(), getGeneralDateFormat()) : "") + "$$" + (!ObjectUtil.isEmpty(procurementMTNT.getCoOperative()) ? procurementMTNT.getCoOperative().getName() : "") + "$$" + procurementMTNT.getTruckId() + "$$" + procurementMTNT.getDriverName() + "@@";
        
        Set<PMTDetail> pmtDetails = procurementMTNT.getPmtDetails();
        if (!ObjectUtil.isListEmpty(pmtDetails)) {
          StringBuilder gradeIdSb = new StringBuilder();
          StringBuilder noOfBagsSb = new StringBuilder();
          StringBuilder grossWtSb = new StringBuilder();
          for (PMTDetail pmtDetail : pmtDetails) {
            gradeIdSb = gradeIdSb.append(pmtDetail.getGradeMaster().getId() + "~~");
            noOfBagsSb = noOfBagsSb.append(pmtDetail.getMtntNumberOfBags() + "~~");
            grossWtSb = grossWtSb.append(pmtDetail.getMtntGrossWeight() + "~~");
          }
          
          result = result + gradeIdSb + "||" + noOfBagsSb.toString() + "||" + grossWtSb.toString();
        }
      }
    }
    response.getWriter().print(result);
    response.setContentType("text/html");
  }
  



  public void populateSubmit()
    throws Exception
  {
    String result = "";
    if (StringUtil.isEmpty(agentId)) {
      result = "agentid.empty";
    } else {
      Agent agent = agentService.findAgentByProfileId(agentId);
      if ((ObjectUtil.isEmpty(agent)) || (ObjectUtil.isEmpty(agent.getCooperative()))) {
        result = "agent.not.exists";
      } else if (1 != agent.getStatus()) {
        result = "agent.inactive";
      }
    }
    if (StringUtil.isEmpty(result))
    {
      request.getSession().setAttribute(agentId + "_" + "319" + "_" + "isFormResubmit", "No");
    }
    

    printAjaxResponse(getText(result), "text/html");
  }
  




  public List<Warehouse> getWarehouses()
  {
    return locationService.listWarehouse();
  }
  





  public void setLocationService(ILocationService locationService)
  {
    this.locationService = locationService;
  }
  




  public void setProductDistributionService(IProductDistributionService productDistributionService)
  {
    this.productDistributionService = productDistributionService;
  }
  




  public void setStartDate(String startDate)
  {
    this.startDate = startDate;
  }
  




  public String getStartDate()
  {
    return startDate;
  }
  




  public void setGradeMasterList(List<GradeMaster> gradeMasterList)
  {
    this.gradeMasterList = gradeMasterList;
  }
  




  public List<GradeMaster> getGradeMasterList()
  {
    if (ObjectUtil.isListEmpty(gradeMasterList)) {
      gradeMasterList = productDistributionService.listGradeMaster();
    }
    return gradeMasterList;
  }
  




  public String getGradeMasterIdList()
  {
    if ((ObjectUtil.isEmpty(gradeMasterIdList)) && (!ObjectUtil.isListEmpty(gradeMasterList))) {
      StringBuilder gradeMasterIdSb = new StringBuilder();
      for (GradeMaster gradeMaster : gradeMasterList)
        gradeMasterIdSb.append(gradeMaster.getId() + "~~");
      gradeMasterIdList = gradeMasterIdSb.toString();
    }
    return gradeMasterIdList;
  }
  





  public JSONObject getProcurementProductJSON()
  {
    JSONObject products = new JSONObject();
    JSONArray productArray = new JSONArray();
    
    Map<String, Map<String, Object>> productsMap = new LinkedHashMap();
    List<GradeMaster> gradeMasterList = productDistributionService.listGradeMaster();
    for (GradeMaster gradeMaster : gradeMasterList) {
      if (!ObjectUtil.isEmpty(gradeMaster.getProduct())) {
        if (productsMap.containsKey(String.valueOf(gradeMaster.getProduct().getId()))) {
          Set<GradeMaster> gradeMasters = new LinkedHashSet();
          
          if (((Map)productsMap.get(String.valueOf(gradeMaster.getProduct().getId()))).containsKey("grades"))
          {
            gradeMasters = (Set)((Map)productsMap.get(String.valueOf(gradeMaster.getProduct().getId()))).get("grades");
          } else {
            ((Map)productsMap.get(String.valueOf(gradeMaster.getProduct().getId()))).put("grades", gradeMasters);
          }
          
          gradeMasters.add(gradeMaster);
        } else {
          Map<String, Object> productsInfoMap = new LinkedHashMap();
          Set<GradeMaster> gradeMasters = new LinkedHashSet();
          gradeMasters.add(gradeMaster);
          productsInfoMap.put("product", gradeMaster.getProduct());
          productsInfoMap.put("grades", gradeMasters);
          productsMap.put(String.valueOf(gradeMaster.getProduct().getId()), productsInfoMap);
        }
      }
    }
    

    for (Map.Entry entry : productsMap.entrySet()) {
      Map<String, Object> productMap = (Map)entry.getValue();
      ProcurementProduct product = (ProcurementProduct)productMap.get("product");
      
      Set<GradeMaster> gradeMasters = (Set)productMap.get("grades");
      
      JSONObject productJSON = new JSONObject();
      
      productJSON.put("id", Long.valueOf(product.getId()));
      productJSON.put("code", product.getCode());
      productJSON.put("name", product.getName());
      
      JSONArray gradesJSON = new JSONArray();
      for (GradeMaster gradeMaster : gradeMasters) {
        JSONObject gradeMasterJSON = new JSONObject();
        gradeMasterJSON.put("id", Long.valueOf(gradeMaster.getId()));
        gradeMasterJSON.put("code", gradeMaster.getCode());
        gradeMasterJSON.put("name", gradeMaster.getName());
        gradesJSON.add(gradeMasterJSON);
      }
      productJSON.put("grades", gradesJSON);
      productArray.add(productJSON);
    }
    
    products.put("products", productArray);
    return products;
  }
  





  public String populateStock()
  {
	  FarmCatalogue farmCatalogue = null;
	    JSONObject products = new JSONObject();
	    JSONArray productArray = new JSONArray();
	    if(!StringUtil.isEmpty(selectedCoOperative)	&& !StringUtil.isEmpty(selectedProduct)&& !StringUtil.isEmpty(selectedColdStorageName)
	    		&& !StringUtil.isEmpty(selectedBatchNo))  {
	    	
	    	//selectedBatchNo=Arrays.toString(batchNo);
	    	
	    	batchNo= selectedBatchNo.split(",");
	      /*List<CityWarehouseDetail> cityWarehouseDetail = productDistributionService.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
	                Long.parseLong(selectedCoOperative), Long.valueOf(getSelectedProduct()).longValue(),selectedColdStorageName,batchNo);*/
	      List<Object[]> cityWarehouseDetail = productDistributionService.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
	                Long.parseLong(selectedCoOperative), Long.valueOf(getSelectedProduct()).longValue(),selectedColdStorageName,StringUtil.removeLastComma(selectedBatchNo));
	            JSONObject productJSON = new JSONObject();
	            JSONArray procurementGradesJSON = new JSONArray();
	           /* for (CityWarehouseDetail detail : cityWarehouseDetail) {
	                JSONObject procurementGradeJSON = new JSONObject();
	            //   ProcurementGrade grade = productService.findProcurementGradeByCode(detail.getCityWarehouse().getQuality());
	                ProcurementVariety variety = productService.findProcurementVarietyByCode(detail.getCityWarehouse().getQuality());
	                procurementGradeJSON.put("procurementProdId", detail.getCityWarehouse().getProcurementProduct().getId());
	                procurementGradeJSON.put("procurementProdName", detail.getCityWarehouse().getProcurementProduct().getName());
	                procurementGradeJSON.put("unit", variety.getProcurementProduct().getUnit());
	                procurementGradeJSON.put("procurementVarietyName", variety.getName());
	                procurementGradeJSON.put("varietyId", variety.getId());
	               // procurementGradeJSON.put("gradeName", grade.getName());
	                //procurementGradeJSON.put("gradeId", grade.getId());
	                procurementGradeJSON.put("existingNoOfBags", detail.getCityWarehouse().getNumberOfBags());
	                procurementGradeJSON.put("existingNoOfQty", detail.getCityWarehouse().getGrossWeight());
	                 farmCatalogue = getCatlogueValueByCode(detail.getBlockName());
	                procurementGradeJSON.put("blockCode", detail.getBlockName());
	                procurementGradeJSON.put("blockName", farmCatalogue.getName());
	                farmCatalogue = getCatlogueValueByCode(detail.getFloorName());
	                procurementGradeJSON.put("floorCode", detail.getFloorName());
	                procurementGradeJSON.put("floorName", farmCatalogue.getName());
	                farmCatalogue = getCatlogueValueByCode(detail.getBayNumber());
	                procurementGradeJSON.put("bayNo", detail.getBayNumber());
	                procurementGradeJSON.put("bayNum", farmCatalogue.getName());
	               
	                procurementGradeJSON.put("farmerId", detail.getCityWarehouse().getFarmer().getId());
	                procurementGradeJSON.put("farmerName", detail.getCityWarehouse().getFarmer().getFirstName());
	                procurementGradeJSON.put("id", detail.getId());
	                procurementGradesJSON.add(procurementGradeJSON);

	            }
*/
	            productJSON.put("procurementGrades", procurementGradesJSON);
	            productArray.add(productJSON);

	       // }
	        products.put("products", productArray);
	    }
	   
	    try {
	        // sendResponse(products);
	        printAjaxResponse(products, "text/html");
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return null;
	    }
  




  private void resetTimer()
  {
    AgroTimerTask myTask = (AgroTimerTask)request.getSession().getAttribute(agentId + "_timer");
    reStartTxn = true;
    myTask.cancelTimer(false);
    request.setAttribute("agentId", agentId);
    request.getSession().removeAttribute(agentId + "_" + "319" + "_" + "isFormResubmit");
    
    Calendar currentDate = Calendar.getInstance();
    startDate = DateUtil.convertDateToString(currentDate.getTime(), getGeneralDateFormat());
    request.setAttribute("heading", getText("create"));
   
  }
  





  public String getCurrentDate()
  {
    Calendar currentDate = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
    return df.format(currentDate.getTime());
  }
  





  public String populatePrintHTML()
  {
    initializePrintMap();
    if (!StringUtil.isEmpty(receiptNumber))
    {
      PMT procurementMTNR = productDistributionService.findPMTByReceiptNumber(receiptNumber);
      buildTransactionPrintMap(procurementMTNR);
    }
    return "html";
  }
  



  private void initializePrintMap()
  {
    mtnrPrintMap = new HashMap();
    List<Map<String, Object>> productMapList = new ArrayList();
    Map<String, Object> totalMap = new LinkedHashMap();
    mtnrPrintMap.put("recNo", "");
    mtnrPrintMap.put("date", "");
    mtnrPrintMap.put("season", "");
    mtnrPrintMap.put("agentId", "");
    mtnrPrintMap.put("agentName", "");
    mtnrPrintMap.put("coop", "");
    mtnrPrintMap.put("recCoop", "");
    
    mtnrPrintMap.put("product", "");
    mtnrPrintMap.put("productMapList", productMapList);
    mtnrPrintMap.put("totalInfo", totalMap);
    mtnrPrintMap.put("isAgent", Boolean.valueOf(false));
    mtnrPrintMap.put("truckId", "");
    mtnrPrintMap.put("driverName", "");
  }
  




  private void buildTransactionPrintMap(PMT procurementMTNR)
  {
    List<Map<String, Object>> productMapList = new ArrayList();
    if (!ObjectUtil.isEmpty(procurementMTNR)) {
      long noOfBagsSum = 0L;
      double netWeightSum = 0.0D;
      DateFormat df = new SimpleDateFormat(getGeneralDateFormat());
      
      if (!StringUtil.isEmpty(procurementMTNR.getMtntReceiptNumber())) {
        mtnrPrintMap.put("recNo", procurementMTNR.getMtntReceiptNumber());
      }
      if (!ObjectUtil.isEmpty(procurementMTNR.getMtntDate())) {
        mtnrPrintMap.put("date", df.format(procurementMTNR.getMtntDate()));
      }
      
      HarvestSeason season;
      if (!ObjectUtil.isEmpty(procurementMTNR.getSeasonCode())) {
        season = clientService.findSeasonByCode(procurementMTNR.getSeasonCode());
        mtnrPrintMap.put("season", season.getCode() + " - " + season.getName());
      }
      











      if (!ObjectUtil.isEmpty(procurementMTNR.getCoOperative())) {
        mtnrPrintMap.put("recCoop", procurementMTNR.getCoOperative().getCode() + " - " + procurementMTNR.getCoOperative().getName());
      }
      
      if (!ObjectUtil.isEmpty(((PMTDetail)procurementMTNR.getPmtDetails().iterator().next()).getCoOperative())) {
        mtnrPrintMap.put("coop", ((PMTDetail)procurementMTNR.getPmtDetails().iterator().next()).getCoOperative().getCode() + " - " + ((PMTDetail)procurementMTNR.getPmtDetails().iterator().next()).getCoOperative().getName());
      }
      




      mtnrPrintMap.put("tructId", procurementMTNR.getTruckId());
      mtnrPrintMap.put("driverName", procurementMTNR.getDriverName());
      mtnrPrintMap.put("productMapList", productMapList);
      if (!ObjectUtil.isListEmpty(procurementMTNR.getPmtDetails())) {
        for (PMTDetail pmtDetail : procurementMTNR.getPmtDetails()) {
          if ((StringUtil.isEmpty(String.valueOf(mtnrPrintMap.get("product")))) && 
            (!ObjectUtil.isEmpty(pmtDetail.getProcurementProduct()))) {
            mtnrPrintMap.put("product", !StringUtil.isEmpty(pmtDetail
              .getProcurementProduct().getName()) ? pmtDetail
              .getProcurementProduct().getName() : "");
          }
          Map<String, Object> productMap = new LinkedHashMap();
          
          productMap.put("variety", pmtDetail.getProcurementGrade().getProcurementVariety().getName());
          productMap.put("grade", pmtDetail.getProcurementGrade().getName());
          productMap.put("noOfBags", Long.valueOf(pmtDetail.getMtntNumberOfBags()));
          productMap.put("netWeight", CurrencyUtil.getDecimalFormat(Double.valueOf(pmtDetail
            .getMtntGrossWeight()), "##.000"));
          
          noOfBagsSum += pmtDetail.getMtntNumberOfBags();
          netWeightSum += pmtDetail.getMtntGrossWeight();
          
          productMapList.add(productMap);
        }
      }
      Map<String, Object> totalMap = new LinkedHashMap();
      totalMap.put("noOfBags", Long.valueOf(noOfBagsSum));
      totalMap.put("netWeight", CurrencyUtil.getDecimalFormat(Double.valueOf(netWeightSum), "##.000"));
      mtnrPrintMap.put("totalInfo", totalMap);
    }
  }
  




  public void setGradeInputString(String gradeInputString)
  {
    this.gradeInputString = gradeInputString;
  }
  




  public String getGradeInputString()
  {
    return gradeInputString;
  }
  




  public void setReceiptNumber(String receiptNumber)
  {
    this.receiptNumber = receiptNumber;
  }
  




  public String getReceiptNumber()
  {
    return receiptNumber;
  }
  




  public void setGradeMasterIdList(String gradeMasterIdList)
  {
    this.gradeMasterIdList = gradeMasterIdList;
  }
  




  public void setIdGenerator(IUniqueIDGenerator idGenerator)
  {
    this.idGenerator = idGenerator;
  }
  




  public IUniqueIDGenerator getIdGenerator()
  {
    return idGenerator;
  }
  




  public void setMtnrDescription(String mtnrDescription)
  {
    this.mtnrDescription = mtnrDescription;
  }
  




  public String getMtnrDescription()
  {
    return mtnrDescription;
  }
  




  public void setTruckId(String truckId)
  {
    this.truckId = truckId;
  }
  




  public String getTruckId()
  {
    return truckId;
  }
  




  public void setDriverId(String driverId)
  {
    this.driverId = driverId;
  }
  




  public String getDriverId()
  {
    return driverId;
  }
  




  public void setSelectedProduct(String selectedProduct)
  {
    this.selectedProduct = selectedProduct;
  }
  




  public String getSelectedProduct()
  {
    return selectedProduct;
  }
  




  public void setProductMap(Map<String, String> productMap)
  {
    this.productMap = productMap;
  }
  




  public Map<String, String> getProductMap()
  {
    return productMap;
  }
  




  public HashMap<String, Object> getMtnrPrintMap()
  {
    return mtnrPrintMap;
  }
  




  public void setMtnrPrintMap(HashMap<String, Object> mtnrPrintMap)
  {
    this.mtnrPrintMap = mtnrPrintMap;
  }
  




  public Map<String, String> getCoOperativeMap()
  {
    return coOperativeMap;
  }
  




  public void setCoOperativeMap(Map<String, String> coOperativeMap)
  {
    this.coOperativeMap = coOperativeMap;
  }
  




  public Map<String, String> getRecCoOperativeMap()
  {
    return recCoOperativeMap;
  }
  




  public void setRecCoOperativeMap(Map<String, String> recCoOperativeMap)
  {
    this.recCoOperativeMap = recCoOperativeMap;
  }
  




  public String getSelectedCoOperative()
  {
    return selectedCoOperative;
  }
  




  public void setSelectedCoOperative(String selectedCoOperative)
  {
    this.selectedCoOperative = selectedCoOperative;
  }
  




  public String getSelectedRecCoOperative()
  {
    return selectedRecCoOperative;
  }
  




  public void setSelectedRecCoOperative(String selectedRecCoOperative)
  {
    this.selectedRecCoOperative = selectedRecCoOperative;
  }
  




  public Map<Long, String> getListWarehouse()
  {
    List<Warehouse> warehouseList = locationService.listWarehouse();
    
    Map<Long, String> warehouseDropDownList = new LinkedHashMap();
    for (Warehouse warehouse : warehouseList) {
      warehouseDropDownList.put(Long.valueOf(warehouse.getId()), warehouse.getName());
    }
    

    return warehouseDropDownList;
  }
  



  public void populateProductTransfer()
  {
	  Customer buyer =null;
    String result = "";
    if (!StringUtil.isEmpty(selectedProduct))
    {
      if (!StringUtil.isEmpty(selectedProduct))
      {
        Warehouse warehouse = locationService.findWarehouseById(
          Long.valueOf(getSelectedWarehouse()).longValue());
        
     ColdStorageStockTransfer coldStorageStockTransfer= new ColdStorageStockTransfer();
       
     coldStorageStockTransfer.setTransferDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));  
     coldStorageStockTransfer.setWarehouse(!ObjectUtil.isEmpty(warehouse) ? warehouse : null);
     coldStorageStockTransfer.setColdStorageName(!StringUtil.isEmpty(selectedColdStorageName) ? selectedColdStorageName : "");
    // coldStorageStockTransfer.setBatchNo(!StringUtil.isEmpty(selectedBatchNo) ? selectedBatchNo : "");
	    if(!StringUtil.isEmpty(selectedBuyer)){
	    	  buyer = clientService.findCustomer(Long.valueOf(selectedBuyer));
	    }   
     coldStorageStockTransfer.setBuyer(!ObjectUtil.isEmpty(buyer) ? buyer : null);
     coldStorageStockTransfer.setTruckId(!StringUtil.isEmpty(truckId) ? truckId : "");
     coldStorageStockTransfer.setDriverName(!StringUtil.isEmpty(driverId) ? driverId : "");
     coldStorageStockTransfer.setPoNumber(!StringUtil.isEmpty(poNumber) ? poNumber : "");
     coldStorageStockTransfer.setInvoiceNumber(!StringUtil.isEmpty(invoiceNumber) ? invoiceNumber : "");

        ProcurementProduct procurementProduct = productDistributionService.findProcurementProductById(Long.valueOf(selectedProduct).longValue());
        if (!ObjectUtil.isEmpty(procurementProduct)) {
        	/** Form Procurement Detail Object */
    		Set<ColdStorageStockTransferDetail> coldStorageStockTransferDetails = formColdStorageStockTransferDetails();
        	/*  Map<String, String[]> gradeInputMap = formGradeInputMapFromString(gradeInputString);
          coldStorageStockTransfer.setColdStorageStockTransferDetail(new LinkedHashSet());
          
          if ((!ObjectUtil.isEmpty(gradeInputMap)) && 
            (gradeInputMap.size() > 0)) {
            Set<ColdStorageStockTransferDetail> coldStorageStockTransferDetails = new HashSet();
           // coldStorageStockTransfer.setColdStorageStockTransferDetail(coldStorageStockTransferDetails);
            for (Map.Entry<String, String[]> entry : gradeInputMap.entrySet()) {
              String id = (String)entry.getKey();
              String[] gradeValues = (String[])entry.getValue();
              

             // ProcurementGrade procurementGrade = productDistributionService.findProcurementGradeById(Long.valueOf(id));
              ProcurementVariety variety = productDistributionService.findProcurementVariertyById(Long.valueOf(id));
              if (!ObjectUtil.isEmpty(variety)) {
            	  ColdStorageStockTransferDetail coldStorageStockTransferDetail = new ColdStorageStockTransferDetail();
            	  coldStorageStockTransferDetail.setProcurementProduct(procurementProduct);
            	  coldStorageStockTransferDetail.setProcurementVariety(variety);
            	  coldStorageStockTransferDetail.setBlockName(gradeValues[0]);
            	  coldStorageStockTransferDetail.setFloorName(gradeValues[1]);
            	  coldStorageStockTransferDetail.setBayNumber(gradeValues[2]);
            	  coldStorageStockTransferDetail.setNoOfBags(Long.valueOf(gradeValues[3]));
            	  coldStorageStockTransferDetail.setQty(Double.valueOf(gradeValues[4]));
            	  Farmer farmerList = farmerService.findFarmerById(Long.valueOf(gradeValues[5]));
            	  coldStorageStockTransferDetail.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
            	  coldStorageStockTransferDetails.add(coldStorageStockTransferDetail);
            	  
              }
            }
           
          }*/
    		 coldStorageStockTransfer.setColdStorageStockTransferDetail(coldStorageStockTransferDetails);
          coldStorageStockTransfer.setCreatedUserName(getUsername());
          coldStorageStockTransfer.setCreatedDate(DateUtil.convertStringToDate(this.startDate, getGeneralDateFormat()));
          coldStorageStockTransfer.setBranchId(getBranchId());
          coldStorageStockTransfer.setRevisionNo(String.valueOf(DateUtil.getRevisionNumber()));
          
                   if (!ObjectUtil.isListEmpty(coldStorageStockTransfer.getColdStorageStockTransferDetail())) {
            if (isDetailsExists(coldStorageStockTransfer))
            {
              String transferReceiptNumber = productDistributionService.addColdStorageStockTransfer(coldStorageStockTransfer);
              receiptNumber = transferReceiptNumber;
              
              if (ObjectUtil.isEmpty(transferReceiptNumber)) {
                result = getText("receiptno.empty");
              }
              
            }
            
          }
          else {
            setMtnrDescription(getText("mtnrFailed") + " : " + getText("grades.not.exists"));
          }
        }
        


        if (StringUtil.isEmpty(result))
        {

         // String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + "\\')\" onclick='printReceipt(\"" + receiptNumber + "\")'>" + getText("printReceipt") + "</button>";
          setMtnrDescription(getLocaleProperty("coldStorageStockTransferSucess") + "</br>" + 
            getText("receiptNumber") + " : " + receiptNumber);
          
          printAjaxResponse(getMtnrDescription(), "text/html");
        } else {
          setMtnrDescription(getText(result));
          printAjaxResponse(getMtnrDescription(), "text/html");
        }
      }
    }
  }
  


  public String list()
  {
    request.setAttribute("heading", getText("create.page"));
    
    return "redirect";
  }
  
  public IClientService getClientService()
  {
    return clientService;
  }
  
  public void setClientService(IClientService clientService)
  {
    this.clientService = clientService;
  }
  
public String getSelectedWarehouse() {
	return selectedWarehouse;
}

public void setSelectedWarehouse(String selectedWarehouse) {
	this.selectedWarehouse = selectedWarehouse;
}

public String getSelectedColdStorageName() {
	return selectedColdStorageName;
}

public void setSelectedColdStorageName(String selectedColdStorageName) {
	this.selectedColdStorageName = selectedColdStorageName;
}

public String getSelectedBatchNo() {
	return selectedBatchNo;
}

public void setSelectedBatchNo(String selectedBatchNo) {
	this.selectedBatchNo = selectedBatchNo;
}
public Map<Long, String> getBuyersList() {
	Map<Long, String> listOfBuyers = new HashMap<Long, String>();
	List<Customer> customers = clientService.listOfCustomers();
	for (Customer customer : customers) {
		listOfBuyers.put(customer.getId(), customer.getCustomerName());
	}
	return listOfBuyers;
}

public String getSelectedBuyer() {
	return selectedBuyer;
}

public void setSelectedBuyer(String selectedBuyer) {
	this.selectedBuyer = selectedBuyer;
}

public String getPoNumber() {
	return poNumber;
}

public void setPoNumber(String poNumber) {
	this.poNumber = poNumber;
}

public String getInvoiceNumber() {
	return invoiceNumber;
}

public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
}

public String[] getBatchNo() {
	return batchNo;
}

public void setBatchNo(String[] batchNo) {
	this.batchNo = batchNo;
}

public String populateStockByLotCode()
{
	  FarmCatalogue farmCatalogue = null;
	    JSONObject products = new JSONObject();
	    JSONArray productArray = new JSONArray();
	    if(!StringUtil.isEmpty(selectedCoOperative)	&& !StringUtil.isEmpty(selectedProduct)&& !StringUtil.isEmpty(selectedColdStorageName)
	    		&& !StringUtil.isEmpty(selectedBatchNo))  {
	    	
	    	//selectedBatchNo=Arrays.toString(batchNo);
	    	
	    	batchNo= selectedBatchNo.split(",");
	      /*List<CityWarehouseDetail> cityWarehouseDetail = productDistributionService.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
	                Long.parseLong(selectedCoOperative), Long.valueOf(getSelectedProduct()).longValue(),selectedColdStorageName,batchNo);*/
	      List<Object[]> cityWarehouseDetail = productDistributionService.listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
	                Long.parseLong(selectedCoOperative), Long.valueOf(getSelectedProduct()).longValue(),selectedColdStorageName,StringUtil.removeLastComma(selectedBatchNo));
	            JSONObject productJSON = new JSONObject();
	            JSONArray procurementGradesJSON = new JSONArray();
	           for (Object[] detail : cityWarehouseDetail) {
	                JSONObject procurementGradeJSON = new JSONObject();
	               
	                procurementGradeJSON.put("id", detail[0].toString());
	                procurementGradeJSON.put("farmerId", detail[1].toString());
	                procurementGradeJSON.put("farmerName", detail[2].toString());
	                procurementGradeJSON.put("procurementProdId", detail[3].toString());
	                procurementGradeJSON.put("procurementProdName", detail[4].toString());
	                procurementGradeJSON.put("unit", detail[5].toString());
	                procurementGradeJSON.put("varietyId", detail[6].toString());
	                procurementGradeJSON.put("procurementVarietyName", detail[7].toString());
	                
	               // procurementGradeJSON.put("gradeName", grade.getName());
	                //procurementGradeJSON.put("gradeId", grade.getId());
	               
	                 
	                procurementGradeJSON.put("blockCode", detail[8].toString());
	                procurementGradeJSON.put("blockName", detail[9].toString());
	               
	                procurementGradeJSON.put("floorCode", detail[10].toString());
	                procurementGradeJSON.put("floorName", detail[11].toString());
	                
	                procurementGradeJSON.put("bayNo", detail[12].toString());
	                procurementGradeJSON.put("bayNum", detail[13].toString());
	                procurementGradeJSON.put("existingNoOfBags", detail[14].toString());
	                procurementGradeJSON.put("existingNoOfQty", detail[15].toString());
	                procurementGradeJSON.put("batchNo", detail[16].toString());
	                procurementGradesJSON.add(procurementGradeJSON);

	            }

	            productJSON.put("procurementGrades", procurementGradesJSON);
	            productArray.add(productJSON);

	       // }
	        products.put("products", productArray);
	    }
	   
	    try {
	        // sendResponse(products);
	        printAjaxResponse(products, "text/html");
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return null;
	    }




  
}
