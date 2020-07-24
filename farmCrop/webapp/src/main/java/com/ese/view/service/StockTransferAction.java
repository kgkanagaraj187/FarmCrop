package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTAgentDetail;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.WebTransactionAction;



public class StockTransferAction
  extends WebTransactionAction
{
  private static final String PROCUREMENT_MTNR = "319";
  private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  
  private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
  
  private ILocationService locationService;
  private IProductDistributionService productDistributionService;
  private IUniqueIDGenerator idGenerator;
  private IClientService clientService;
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
  private HashMap<String, Object> mtnrPrintMap = new LinkedHashMap();
  
  private String seasonName;
  
  private String selectedFarmer;
  
  public StockTransferAction() {}
  
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
        proceurementMTNR.setMtnrTransferInfo(getTransferInfo(recAgent));
        proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        proceurementMTNR.setMtnrDate(DateUtil.setTimeToDate(proceurementMTNR.getMtnrDate()));
        proceurementMTNR.setBranchId(getBranchId());
        proceurementMTNR.setAgentRef(recAgent);
        pmt.setMtntDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        pmt.setMtntDate(DateUtil.setTimeToDate(pmt.getMtntDate()));
        pmt.setBranchId(getBranchId());
        pmt.setMtntTransferInfo(getTransferInfo(senderAgent));
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
            if (isDetailsExists(pmt))
            {
              String mtntReceiptNumber = productDistributionService.addProcurementMTNT(pmt);
              if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
                addActionError(getText("receiptno.empty"));
              }
            }
            
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
    loadCoOperativeList();
  }
  














  private void buildPMTDetailsAndCityWarehouseUpdateDetails(TripSheet tripSheet, ProcurementProduct product, PMT pmt)
  {
    if ((!ObjectUtil.isEmpty(tripSheet)) && (!ObjectUtil.isEmpty(product)))
    {
      List<Object[]> result = productDistributionService.listGradeInformationByTripSheetIdProductId(tripSheet.getId(), product.getId());
      if (result.size() > 0) {
        for (Object[] gradeInfoArray : result) {
          if (gradeInfoArray.length >= 5)
          {
            if (ObjectUtil.isListEmpty(pmt.getPmtDetails())) {
              pmt.setPmtDetails(new LinkedHashSet());
            }
            
            boolean flag = false;
            
            GradeMaster gradeMaster = findGradeMasterById(Long.valueOf(
              String.valueOf(gradeInfoArray[0])).longValue());
            long noOfBags = Long.parseLong(String.valueOf(gradeInfoArray[1]));
            double grossWeight = Double.parseDouble(String.valueOf(gradeInfoArray[2]));
            
            for (PMTDetail pmtDetail : pmt.getPmtDetails()) {
              if ((pmtDetail.getGradeMaster().getId() == gradeMaster.getId()) && 
                (pmtDetail.getProcurementProduct().getId() == product.getId())) {
                pmtDetail.setMtntNumberOfBags(pmtDetail.getMtntNumberOfBags() + noOfBags);
                
                pmtDetail.setMtntGrossWeight(pmtDetail.getMtntGrossWeight() + grossWeight);
                
                flag = true;
                break;
              }
            }
            
            if (!flag) {
              PMTDetail pmtDetail = new PMTDetail();
              pmtDetail.setGradeMaster(gradeMaster);
              pmtDetail.setProcurementProduct(product);
              pmtDetail.setMtntNumberOfBags(noOfBags);
              pmtDetail.setMtntGrossWeight(grossWeight);
              pmtDetail.setPmt(pmt);
              
              pmt.getPmtDetails().add(pmtDetail);
            }
            
            String[] buyerAgentInfo = tripSheet.getBuyerName().split("-");
            if ((!ObjectUtil.isEmpty(buyerAgentInfo)) && (buyerAgentInfo.length > 0)) {
              if (pmt.getPmtAgentDetails() == null) {
                pmt.setPmtAgentDetails(new ArrayList());
              }
              
              PMTAgentDetail pmtAgentDetail = new PMTAgentDetail();
              pmtAgentDetail.setCity(tripSheet.getCity());
              pmtAgentDetail.setProduct(product);
              pmtAgentDetail.setAgentId(buyerAgentInfo[0]);
              pmtAgentDetail.setGradeMaster(gradeMaster);
              pmtAgentDetail.setNoOfBags(noOfBags);
              pmtAgentDetail.setGrossWeight(grossWeight);
              pmt.getPmtAgentDetails().add(pmtAgentDetail);
            }
          }
        }
      }
    }
  }
  
  private GradeMaster findGradeMasterById(long id)
  {
    GradeMaster gradeMaster = null;
    if (!ObjectUtil.isListEmpty(getGradeMasterList())) {
      for (GradeMaster gm : getGradeMasterList()) {
        if (id == gm.getId()) {
          gradeMaster = gm;
          break;
        }
      }
    }
    return gradeMaster;
  }
  
  private boolean isDetailsExists(PMT pmt)
  {
    if (getActionErrors().size() == 0) {
      if (!ObjectUtil.isListEmpty(pmt.getPmtDetails()))
        return true;
      if (ObjectUtil.isListEmpty(pmt.getPmtDetails()))
      {
        addActionError(getText("empty.mtnt.details"));
        return false;
      }
    }
    




    return false;
  }
  











  private void loadCoOperativeList()
  {
    coOperativeMap = new LinkedHashMap();
    List<Warehouse> coOperatives = locationService.listCoOperativeByAgent(agentId);
    if (!ObjectUtil.isListEmpty(coOperatives)) {
      for (Warehouse coOperative : coOperatives) {
        coOperativeMap.put(String.valueOf(coOperative.getId()), coOperative.getName() + " - " + coOperative
          .getCode());
      }
    }
    recCoOperativeMap = new LinkedHashMap();
    
    coOperatives = locationService.listCoOperativeWithManagers();
    if (!ObjectUtil.isListEmpty(coOperatives)) {
      for (Warehouse coOperative : coOperatives) {
        if (!coOperativeMap.containsKey(String.valueOf(coOperative.getId()))) {
          recCoOperativeMap.put(String.valueOf(coOperative.getId()), coOperative
            .getName() + " - " + coOperative
            .getCode());
        }
      }
    }
  }
  





  private TransferInfo getTransferInfo(Agent agent)
  {
    TransferInfo transferInfo = new TransferInfo();
    transferInfo.setAgentId(agent.getProfileId());
    transferInfo.setAgentName(ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent
      .getPersonalInfo().getAgentName());
    String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
    transferInfo.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
    transferInfo.setTxnTime(DateUtil.setTimeToDate(transferInfo.getTxnTime()));
    transferInfo.setDeviceId(getText("N/A"));
    transferInfo.setDeviceName(getText("N/A"));
    transferInfo.setServicePointId(getText("N/A"));
    transferInfo.setServicePointName(getText("N/A"));
    return transferInfo;
  }
  
  private TransferInfo getTransferInfoDetail()
  {
    TransferInfo transferInfo = new TransferInfo();
    String nowDate = DateUtil.convertDateToString(new Date(), getGeneralDateFormat());
    transferInfo.setTxnTime(DateUtil.convertStringToDate(nowDate, getGeneralDateFormat()));
    transferInfo.setTxnTime(DateUtil.setTimeToDate(transferInfo.getTxnTime()));
    transferInfo.setDeviceId(getText("N/A"));
    transferInfo.setDeviceName(getText("N/A"));
    transferInfo.setServicePointId(getText("N/A"));
    transferInfo.setServicePointName(getText("N/A"));
    return transferInfo;
  }
  





  private Map<String, String[]> formGradeInputMapFromString(String inputString)
  {
    Map<String, String[]> returnMap = new LinkedHashMap();
    if (!StringUtil.isEmpty(inputString)) {
      String[] inputArray = inputString.split("\\|\\|");
      for (String gradeStr : inputArray) {
        String[] gradeDetailsArray = gradeStr.split("~~");
        if ((!ObjectUtil.isEmpty(gradeDetailsArray)) && (gradeDetailsArray.length == 3) && 
          (Double.valueOf(gradeDetailsArray[2].trim()).doubleValue() > 0.0D)) {
          returnMap.put(gradeDetailsArray[0], new String[] { gradeDetailsArray[1], gradeDetailsArray[2] });
        }
      }
    }
    
    return returnMap;
  }
  




  public String populateProducts()
  {
    List<CityWarehouse> cityWarehouses = new ArrayList();
    List<String> prods = new ArrayList();
    StringBuffer sb = new StringBuffer();
    if ((!StringUtil.isEmpty(selectedCoOperative)) && (ObjectUtil.isLong(selectedCoOperative))) {
      cityWarehouses = productDistributionService.listProductsByCoOperative(
        Long.parseLong(selectedCoOperative));
      
      for (int i = 0; i < cityWarehouses.size(); i++) {
        CityWarehouse cityWarehouse = (CityWarehouse)cityWarehouses.get(i);
        

        String val = cityWarehouse.getProcurementProduct().getName() + " (" + cityWarehouse.getProcurementProduct().getCode() + ")-" + cityWarehouse.getProcurementProduct().getId();
        if (!prods.contains(val))
        {










          double exNetQty = productDistributionService.findPrecurementProductStockNetWhtByWarehouseIdAndProductId(cityWarehouse
            .getCoOperative().getId(), cityWarehouse
            .getProcurementProduct().getId());
          
          if (exNetQty > 0.0D) {
            prods.add(val);
            sb.append(val);
            if (i + 1 != cityWarehouses.size()) {
              sb.append(",");
            }
          }
        }
      }
    }
    try
    {
      printAjaxResponse(sb.toString(), "text/html");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public String populateProducts1() {
    List<CityWarehouse> cityWarehouses = new ArrayList();
    List<String> prods = new ArrayList();
    StringBuffer sb = new StringBuffer();
    if ((!StringUtil.isEmpty(selectedFarmer)) && (ObjectUtil.isLong(selectedFarmer))) {
      cityWarehouses = productDistributionService.listProductsByFarmerId(
        Long.parseLong(selectedFarmer));
      
      for (int i = 0; i < cityWarehouses.size(); i++) {
        CityWarehouse cityWarehouse = (CityWarehouse)cityWarehouses.get(i);
        

        String val = cityWarehouse.getProcurementProduct().getName() + " (" + cityWarehouse.getProcurementProduct().getCode() + ")-" + cityWarehouse.getProcurementProduct().getId();
        if (!prods.contains(val))
        {
          double exNetQty = productDistributionService.findFarmerStockNetWgtByWarehouseIdAndFarmerId(cityWarehouse
            .getCoOperative().getId(), cityWarehouse
            .getFarmer().getFarmerId());
          
          if (exNetQty > 0.0D) {
            prods.add(val);
            sb.append(val);
            if (i + 1 != cityWarehouses.size()) {
              sb.append(",");
            }
          }
        }
      }
    }
    try
    {
      printAjaxResponse(sb.toString(), "text/html");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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
  





  public List<String> getReceiptNumberList()
  {
    List<String> receiptNumberList = productDistributionService.listPMTReceiptNumberByStatus(PMT.Status.MTNT.ordinal());
    return receiptNumberList;
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
    JSONObject products = new JSONObject();
    JSONArray productArray = new JSONArray();
    Map<String, Map<String, Object>> productsMap = new LinkedHashMap();
    
    if (ObjectUtil.isLong(selectedProduct))
    {

      List<ProcurementGrade> procurementGradeList = productDistributionService.listProcurementGradeByProcurementProductId(Long.parseLong(selectedProduct));
      
      for (ProcurementGrade procurementGrade : procurementGradeList) {
        try
        {
          if ((!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())) && 
            (!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()
            .getProcurementProduct())))
          {
            if (productsMap.containsKey(String.valueOf(procurementGrade
              .getProcurementVariety().getProcurementProduct().getId())))
            {
              Set<ProcurementGrade> procurementGradeSet = new LinkedHashSet();
              


              if (((Map)productsMap.get(String.valueOf(procurementGrade.getProcurementVariety().getProcurementProduct().getId()))).containsKey("procurementGrades"))
              {


                procurementGradeSet = (Set)((Map)productsMap.get(String.valueOf(procurementGrade.getProcurementVariety().getProcurementProduct().getId()))).get("procurementGrades");

              }
              else
              {
                ((Map)productsMap.get(String.valueOf(procurementGrade.getProcurementVariety().getProcurementProduct()))).put("procurementGrades", procurementGradeSet);
              }
              

              procurementGradeSet.add(procurementGrade);
            }
            else {
              Map<String, Object> productsInfoMap = new LinkedHashMap();
              Set<ProcurementGrade> procurementGradeSet = new LinkedHashSet();
              procurementGradeSet.add(procurementGrade);
              productsInfoMap.put("product", procurementGrade.getProcurementVariety()
                .getProcurementProduct());
              productsInfoMap.put("procurementGrades", procurementGradeSet);
              productsMap.put(String.valueOf(procurementGrade.getProcurementVariety()
                .getProcurementProduct().getId()), productsInfoMap);
            }
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      for (Map.Entry entry : productsMap.entrySet()) {
        Map<String, Object> productMap = (Map)entry.getValue();
        
        ProcurementProduct product = (ProcurementProduct)productMap.get("product");
        

        Set<ProcurementGrade> procurementGradeSet = (Set)productMap.get("procurementGrades");
        
        JSONObject productJSON = new JSONObject();
        
        productJSON.put("id", Long.valueOf(product.getId()));
        productJSON.put("code", product.getCode());
        productJSON.put("name", product.getName());
        
        JSONArray procurementGradesJSON = new JSONArray();
        for (ProcurementGrade procurementGrade : procurementGradeSet) {
          JSONObject procurementGradeJSON = new JSONObject();
          procurementGradeJSON.put("id", procurementGrade.getId());
          procurementGradeJSON.put("code", procurementGrade.getCode());
          procurementGradeJSON.put("name", procurementGrade.getName());
          procurementGradeJSON.put("numberOfBags", "0");
          procurementGradeJSON.put("grossWeight", "0");
          procurementGradeJSON.put("procurementVarietyCode", procurementGrade
            .getProcurementVariety().getCode());
          procurementGradeJSON.put("procurementVarietyName", procurementGrade
            .getProcurementVariety().getName());
          procurementGradeJSON.put("procurementVarietyId", procurementGrade
            .getProcurementVariety().getId());
          procurementGradeJSON.put("unit", procurementGrade
            .getProcurementVariety().getProcurementProduct().getUnit());
          
          if (ObjectUtil.isLong(selectedCoOperative))
          {
            CityWarehouse cityWarehouse = productDistributionService.findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
              Long.parseLong(selectedCoOperative), procurementGrade
              .getCode(), Long.valueOf(getSelectedProduct()).longValue());
            if ((!ObjectUtil.isEmpty(cityWarehouse)) && 
              (cityWarehouse.getGrossWeight() > 0.0D)) {
              procurementGradeJSON.put("numberOfBags", Long.valueOf(cityWarehouse
                .getNumberOfBags()));
              procurementGradeJSON.put("grossWeight", Double.valueOf(cityWarehouse
                .getGrossWeight()));
              procurementGradesJSON.add(procurementGradeJSON);
            }
          }
        }
        


        productJSON.put("procurementGrades", procurementGradesJSON);
        productArray.add(productJSON);
      }
      
      products.put("products", productArray);
    }
    try
    {
      printAjaxResponse(products, "text/html");
    }
    catch (Exception e) {
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
    loadCoOperativeList();
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
          if(!getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
          productMap.put("noOfBags", Long.valueOf(pmtDetail.getMtntNumberOfBags()));
          }
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
    System.out.println("populateProductTransfer");
    String result = "";
    if (!StringUtil.isEmpty(selectedProduct))
    {
      if (!StringUtil.isEmpty(selectedProduct))
      {
        Warehouse senderWarehouse = locationService.findWarehouseById(
          Long.valueOf(getSelectedCoOperative()).longValue());
        
        if (ObjectUtil.isEmpty(senderWarehouse)) {
          result = "senderWarehouse.unavailable";
        }
        Warehouse receiverWarehouse = locationService.findWarehouseById(
          Long.valueOf(getSelectedRecCoOperative()).longValue());
        
        if (ObjectUtil.isEmpty(receiverWarehouse)) {
          result = "receiverWarehouse.unavailable";
        }
        
        PMT proceurementMTNR = new PMT();
        PMT pmt = new PMT();
        







        proceurementMTNR.setMtnrTransferInfo(getTransferInfoDetail());
        proceurementMTNR.setMtnrDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        
        proceurementMTNR.setMtnrDate(DateUtil.setTimeToDate(proceurementMTNR.getMtnrDate()));
        proceurementMTNR.setBranchId(getBranchId());
        pmt.setMtntDate(DateUtil.convertStringToDate(startDate, getGeneralDateFormat()));
        pmt.setMtntDate(DateUtil.setTimeToDate(pmt.getMtntDate()));
        pmt.setBranchId(getBranchId());
        
        pmt.setStatusCode(PMT.Status.MTNT.ordinal());
        


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
              

              ProcurementGrade procurementGrade = productDistributionService.findProcurementGradeById(Long.valueOf(id));
              if (!ObjectUtil.isEmpty(procurementGrade)) {
                PMTDetail pmtDetail = new PMTDetail();
                pmtDetail.setPmt(proceurementMTNR);
                pmtDetail.setProcurementProduct(procurementProduct);
                pmtDetail.setProcurementGrade(procurementGrade);
                pmtDetail.setMtnrNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail.setMtnrGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail.setMtntNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail.setMtntGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail.setCoOperative(senderWarehouse);
                proceurementMTNR.getPmtDetails().add(pmtDetail);
                
                PMTDetail pmtDetail1 = new PMTDetail();
                pmtDetail1.setProcurementGrade(procurementGrade);
                pmtDetail1.setProcurementProduct(procurementProduct);
                pmtDetail1.setMtntNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail1.setMtntGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail1.setMtnrNumberOfBags(Long.valueOf(gradeValues[0]).longValue());
                pmtDetail1.setMtnrGrossWeight(Double.valueOf(gradeValues[1]).doubleValue());
                pmtDetail1.setPmt(pmt);
                pmtDetail1.setCoOperative(receiverWarehouse);
                
                pmt.getPmtDetails().add(pmtDetail1);
              }
            }
          }
          
          pmt.setCoOperative(senderWarehouse);
          pmt.setTruckId(truckId);
          pmt.setDriverName(driverId);
          pmt.setTrnType("STOCK_TRANSFER");
          pmt.setSeasonCode(clientService.findCurrentSeasonCode());
          pmt.setTotalLabourCost(0.0);
          pmt.setTransportCost(0.0);
          proceurementMTNR.setCoOperative(receiverWarehouse);
          proceurementMTNR.setTruckId(truckId);
          proceurementMTNR.setDriverName(driverId);
          proceurementMTNR.setStatusCode(PMT.Status.MTNR.ordinal());
          proceurementMTNR.setStatusMessage(PMT.Status.MTNR.toString());
          proceurementMTNR.setMtnrReceiptNumber(idGenerator.getMTNRReceiptNoSeq());
          proceurementMTNR.setTrnType("STOCK_TRANSFER");
          proceurementMTNR.setSeasonCode(clientService.findCurrentSeasonCode());
          
          
          TransferInfo transferInfo = new TransferInfo();
			transferInfo.setAgentId(getUsername());
			transferInfo.setAgentName(getUserFullName());
			transferInfo.setDeviceId(null);
			transferInfo.setDeviceName(null);
			transferInfo.setServicePointId(null);
			transferInfo.setServicePointName(null);
			transferInfo.setOperationType(ESETxn.ON_LINE);
			pmt.setMtntTransferInfo(transferInfo);
			
          if (!ObjectUtil.isListEmpty(proceurementMTNR.getPmtDetails())) {
            if (isDetailsExists(pmt))
            {
              String mtntReceiptNumber = productDistributionService.addProcurementMTNT(pmt);
              receiptNumber = mtntReceiptNumber;
              
              if (ObjectUtil.isEmpty(mtntReceiptNumber)) {
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
          
          String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNumber + 
                  "\\')\" class ='btn btn-default btnBorderRadius' onclick='printReceipt(\"" + receiptNumber + "\")'>" +
                  getText("printReceipt") + "</button>";
                 
          setMtnrDescription("<h5>"
                        + getText("receiptNumber") + " : " + receiptNumber+ "</h5>" + receiptHtml + "</br>");
          printAjaxResponse(getMtnrDescription(), "text/html");
          
        } else {
          setMtnrDescription(getText(result));
          printAjaxResponse(getMtnrDescription(), "text/html");
        }
      }
    }
  }
  



  public Map<String, String> getAgentList()
  {
    Map<String, String> returnMap = new LinkedHashMap();
    
    List<Agent> agentLists = agentService.listAgentByAgentType("02");
    if (!ObjectUtil.isListEmpty(agentLists)) {
      for (Agent agent : agentLists) {
        returnMap.put(agent.getPersonalInfo().getFirstName() + " " + agent
          .getPersonalInfo().getLastName() + " - " + agent.getProfileId(), agent
          .getPersonalInfo().getFirstName() + " " + agent
          .getPersonalInfo().getLastName() + " - " + agent
          .getProfileId());
      }
    }
    
    return returnMap;
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
  
  public String getSeasonName() {
    seasonName = (super.getCurrentSeasonsCode() + "-" + super.getCurrentSeasonsName());
    
    return seasonName;
  }
  
  public void setSeasonName(String seasonName)
  {
    this.seasonName = seasonName;
  }
  
  public Map<String, String> getFarmerMap()
  {
    return farmerMap;
  }
  
  public void setFarmerMap(Map<String, String> farmerMap)
  {
    this.farmerMap = farmerMap;
  }
  
  public String populateFarmers()
  {
    List<CityWarehouse> cityWarehouses = new ArrayList();
    List<String> prods = new ArrayList();
    StringBuffer sb = new StringBuffer();
    if ((!StringUtil.isEmpty(selectedCoOperative)) && (ObjectUtil.isLong(selectedCoOperative))) {
      cityWarehouses = productDistributionService.listFarmersByCoOperative(
        Long.parseLong(selectedCoOperative));
      for (int i = 0; i < cityWarehouses.size(); i++) {
        CityWarehouse cityWarehouse = (CityWarehouse)cityWarehouses.get(i);
        

        String val = cityWarehouse.getFarmer().getName() + " (" + cityWarehouse.getFarmer().getFarmerCode() + ")-" + cityWarehouse.getFarmer().getId();
        if (!prods.contains(val))
        {

          double exNetQty = productDistributionService.findFarmerStockNetWgtByWarehouseIdAndFarmerId(cityWarehouse
            .getCoOperative().getId(), cityWarehouse
            .getFarmer().getFarmerId());
          
          if (exNetQty > 0.0D) {
            prods.add(val);
            sb.append(val);
            if (i + 1 != cityWarehouses.size()) {
              sb.append(",");
            }
          }
        }
      }
    }
    try
    {
      printAjaxResponse(sb.toString(), "text/html");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public String getSelectedFarmer()
  {
    return selectedFarmer;
  }
  
  public void setSelectedFarmer(String selectedFarmer)
  {
    this.selectedFarmer = selectedFarmer;
  }
}
