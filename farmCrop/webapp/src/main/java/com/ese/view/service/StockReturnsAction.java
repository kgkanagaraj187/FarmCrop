package com.ese.view.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturn;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturnDetails;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class StockReturnsAction extends SwitchValidatorAction
{

    private static final long serialVersionUID = 2933253845440310599L;
    private static final Logger logger = Logger.getLogger(StockReturnsAction.class);
    private IProductDistributionService productDistributionService;
    private String selectedVendor;
    private String selectedOrderNo;
    private WarehousePayment warehousePayment;
    private IAccountService accountService;
    private String cashCreditValue;
    private String selectedType;
    List<WarehousePayment> orderNoList = new ArrayList<WarehousePayment>();
    List<WarehousePayment> warehouseList = new ArrayList<WarehousePayment>();
    List<WarehousePayment> allOrderNo = new ArrayList<WarehousePayment>();
    List<WarehousePayment> allWarehouse = new ArrayList<WarehousePayment>();
    private WarehousePaymentDetails warehousePaymentDetails;
    private String stockDescription;
    private String selectedWarehouse;
    private String returnDamagedProducts;
    private ILocationService locationService;
    Map<Integer, String> cashType = new LinkedHashMap<Integer, String>();
    private double totaAmountFinal;
    private String startDate;
    private long totalQtyFinal;
    private String remarks;
    private String paymentMode;
    private double paymentAmount;
    private String receiptNo;
    private IUniqueIDGenerator idGenerator;
    private IProductService productService;
    private WarehouseStockReturn warehouseStockReturn;
    private WarehouseStockReturnDetails warehouseStockReturnDetails;
    private static final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
    private Map<String, Object> stockReturnMap = new LinkedHashMap<String, Object>();

    public Map<String, Object> getStockReturnMap() {
		return stockReturnMap;
	}

	public void setStockReturnMap(Map<String, Object> stockReturnMap) {
		this.stockReturnMap = stockReturnMap;
	}

	@Override
    public Object getData() {

        // TODO Auto-generated method stub
        cashType.put(0, getText("cashType1"));
        cashType.put(1, getText("cashType2"));
        return warehousePayment;
    }
    
    public String create()
    {
        return INPUT;
        
    }
    
    public String populateStockProducts() 
    {

        //request.setAttribute(HEADING, getText("create"));
        try
        {
            String result = "";
            if (warehousePayment==null && StringUtil.isEmpty(returnDamagedProducts))
            {
                command = INPUT;
                request.setAttribute(HEADING, getText(INPUT));
    
            }
            else
            {
                String receiptnumberSeq = StringUtil.isEmpty(receiptNo) ? idGenerator
                        .getWarehouseStockEntryReceiptNumberSeq() : receiptNo;
                        receiptNo = receiptnumberSeq;
                String[] damagedProductArray=returnDamagedProducts.split("@@@");
                Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
                Vendor vendor = productService.findVendorIdById(Long.valueOf(selectedVendor));
                warehouseStockReturn=new WarehouseStockReturn();
                warehouseStockReturn.setOrderNo(selectedOrderNo);
                warehouseStockReturn.setVendor(vendor);
                warehouseStockReturn.setWarehouse(warehouse);
                warehouseStockReturn.setTotalDamagedStock(totalQtyFinal);
                warehouseStockReturn.setTotalAmount(totaAmountFinal);
                warehouseStockReturn.setReturnType(selectedType);
                if ("0".equals(paymentMode)) {
                    warehouseStockReturn.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
                    warehouseStockReturn.setPaymentAmount(paymentAmount);
                    
                } else if ("1".equals(paymentMode)) {
                    warehouseStockReturn.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);                   
                    warehouseStockReturn.setPaymentAmount(totaAmountFinal);
                }
             
                warehouseStockReturn.setTrxnDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
                warehouseStockReturn.setRemarks(remarks);
                warehouseStockReturn.setReceiptNo(receiptNo);
                warehouseStockReturn.setRevisionNo(DateUtil.getRevisionNumber());
                productDistributionService.addStockReturnsProduct(warehouseStockReturn);
                
                WarehousePayment warehousePayment=productDistributionService.findVendorAndOrderNo(Long.valueOf(selectedVendor), selectedOrderNo);
                double updateDamagedStock=(warehousePayment.getTotalDamagedStock())-(totalQtyFinal);
                warehousePayment.setTotalDamagedStock(updateDamagedStock);
                productDistributionService.editWarehouseDamagedStock(warehousePayment);
                
                  double damagedStockQty;
                  for(int i=0;i<damagedProductArray.length;i++)
                  {
                      System.out.println("damagedProductArray"+damagedProductArray[i]);
                      String splitData[]=damagedProductArray[i].split("###");
                      System.out.println("splitData"+splitData[i]);
                      if(splitData.length==3)
                      {
                          System.out.println("-----length------"+splitData.length);
                          String costPrice=splitData[0];
                          String damagedStock=splitData[1];
                          String damagedProducts=splitData[2];
                          //String amount = splitData[3];
                          double proAmount = 0.00;
                          proAmount = proAmount+(Double.valueOf(costPrice)* Double.valueOf(damagedStock));
                          
                          System.out.println("costPrice"+costPrice);
                          System.out.println("costPrice"+damagedStock);
                          System.out.println("costPrice"+damagedProducts);
                          Product product = productService.findProductById(Long.valueOf(damagedProducts.trim()));
                          
                          warehouseStockReturnDetails = new WarehouseStockReturnDetails();
                          
                          warehouseStockReturnDetails.setCostPrice(Double.valueOf(costPrice));
                          warehouseStockReturnDetails.setDamagedStock(Long.valueOf(damagedStock));
                          warehouseStockReturnDetails.setProduct(product);
                          warehouseStockReturnDetails.setAmount(proAmount);
                          warehouseStockReturn.setCreditBalance(proAmount);
                          warehouseStockReturnDetails.setWarehouseStockReturn(warehouseStockReturn);
                          productDistributionService.addStockReturnDeatils(warehouseStockReturnDetails);
                         warehousePaymentDetails=new WarehousePaymentDetails(); 
                         warehousePaymentDetails=productDistributionService.findWarehousePaymentIdAndProduct(warehousePayment.getId(),damagedProducts);
                         damagedStockQty=(warehousePaymentDetails.getDamagedStock())- (Long.valueOf(damagedStock));
                         warehousePaymentDetails.setDamagedStock(damagedStockQty);
                         productDistributionService.editWarehousePaymentDetails(warehousePaymentDetails);

                         /*WarehouseProduct warehouseProduct = productService.findCostPriceForProduct(product
                                 .getId(), warehouse.getId());
                         double currentStock=warehouseProduct.getStock()-Integer.parseInt(damagedStock);
                         warehouseProduct.setStock(currentStock);
                         productDistributionService.editWarehouseProducts(warehouseProduct);*/
                         productDistributionService.processWarehouseStockReturns(warehouseStockReturn);
                          
                     }
                  }
                   if (StringUtil.isEmpty(result)) {

                          String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNo
                                  + "\\')\" onclick='printReceipt(\"" + receiptNo + "\")'>"
                                  + getText("printReceipt") + "</button>";
                          setStockDescription(getText("receiptNumber") + " : " + receiptNo + " "
                                  + getText("stockEntrySucess") + "<br/>" +receiptHtml);

                          printAjaxResponse(getStockDescription(), "text/html");
                    }     
                  
                  
            }
        }    
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
        
      
    } 
    
    
    public String populateUnSoldStockProducts() 
    {
        try
        {
            String result = "";
            if (warehousePayment==null && StringUtil.isEmpty(returnDamagedProducts))
            {
                command = INPUT;
                request.setAttribute(HEADING, getText(INPUT));
    
            }
            else
            {
                String receiptnumberSeq = StringUtil.isEmpty(receiptNo) ? idGenerator
                        .getWarehouseStockEntryReceiptNumberSeq() : receiptNo;
                        receiptNo = receiptnumberSeq;
                String[] damagedProductArray=returnDamagedProducts.split("@@@");
                Warehouse warehouse = locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
                Vendor vendor = productService.findVendorIdById(Long.valueOf(selectedVendor));
                warehouseStockReturn=new WarehouseStockReturn();
                warehouseStockReturn.setOrderNo(selectedOrderNo);
                warehouseStockReturn.setVendor(vendor);
                warehouseStockReturn.setWarehouse(warehouse);
                warehouseStockReturn.setTotalDamagedStock(totalQtyFinal);
                warehouseStockReturn.setTotalAmount(totaAmountFinal);
                warehouseStockReturn.setReturnType(selectedType);
                if ("0".equals(paymentMode)) {
                    warehouseStockReturn.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
                    warehouseStockReturn.setPaymentAmount(paymentAmount);
                    
                } else if ("1".equals(paymentMode)) {
                    warehouseStockReturn.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);                    
                    warehouseStockReturn.setPaymentAmount(totaAmountFinal);
                }
             
                warehouseStockReturn.setTrxnDate(DateUtil.convertStringToDate(startDate, "MM/dd/yyyy"));
                warehouseStockReturn.setRemarks(remarks);
                warehouseStockReturn.setReceiptNo(receiptNo);
                warehouseStockReturn.setRevisionNo(DateUtil.getRevisionNumber());
                productDistributionService.addStockReturnsProduct(warehouseStockReturn);
                
                WarehousePayment warehousePayment=productDistributionService.warehousePaymentByVendorAndOrderNo(Long.valueOf(selectedVendor), selectedOrderNo);
                double updateStock=(warehousePayment.getTotalGoodStock())-(totalQtyFinal);
                warehousePayment.setTotalGoodStock(updateStock);
                productDistributionService.editWarehouseStock(warehousePayment);
                
                  double stockQty;
                  for(int i=0;i<damagedProductArray.length;i++)
                  {
                      String splitData[]=damagedProductArray[i].split("###");
                      if(splitData.length==3)
                      {
                          String costPrice=splitData[0];
                          String damagedStock=splitData[1];
                          String damagedProducts=splitData[2];
                          //String amount = splitData[3];
                          
                          double proAmount = 0.00;
                          proAmount = proAmount+(Double.valueOf(costPrice)* Double.valueOf(damagedStock));
                          
                          Product product = productService.findProductById(Long.valueOf(damagedProducts.trim()));
                          
                          warehouseStockReturnDetails = new WarehouseStockReturnDetails();
                          
                          warehouseStockReturnDetails.setCostPrice(Double.valueOf(costPrice));
                          warehouseStockReturnDetails.setDamagedStock(Long.valueOf(damagedStock));
                          warehouseStockReturnDetails.setProduct(product);
                          warehouseStockReturnDetails.setAmount(proAmount);
                          warehouseStockReturnDetails.setWarehouseStockReturn(warehouseStockReturn);
                          productDistributionService.addStockReturnDeatils(warehouseStockReturnDetails);
                         warehousePaymentDetails=new WarehousePaymentDetails(); 
                         warehousePaymentDetails=productDistributionService.findWarehousePaymentDetail(warehousePayment.getId(),damagedProducts);
                         stockQty=(warehousePaymentDetails.getStock())- (Long.valueOf(damagedStock));
                         warehousePaymentDetails.setStock(stockQty);

                         WarehouseProduct warehouseProduct = productService.findCostPriceForProduct(product
                                 .getId(), warehouse.getId());
                         double currentStock=warehouseProduct.getStock()-Integer.parseInt(damagedStock);
                         warehouseProduct.setStock(currentStock);
                         productDistributionService.editWarehouseProducts(warehouseProduct);
                         productDistributionService.editWarehousePaymentDetailsStock(warehousePaymentDetails);
                         productDistributionService.processWarehouseStockReturns(warehouseStockReturn);
                          
                     }
                  } 
                     if (StringUtil.isEmpty(result)) {

                          String receiptHtml = "<a href=\"javascript:printReceipt(\\'" + receiptNo
                                  + "\\')\" onclick='printReceipt(\"" + receiptNo + "\")'>"
                                  + getText("printReceipt") + "</button>";
                          setStockDescription(getText("receiptNumber") + " : " + receiptNo + " "
                                  + getText("stockEntrySucess") + "<br/>" +receiptHtml);

                          printAjaxResponse(getStockDescription(), "text/html");
                      }   
                  
                  
            }
        }    
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
        
      
    } 
    
    @SuppressWarnings("unchecked")
    public String populateProducts() 
    {
        List<WarehousePaymentDetails> warehousePaymentDetails = new ArrayList<WarehousePaymentDetails>();
        JSONObject products = new JSONObject();
        JSONArray productArray = new JSONArray();
        if (!StringUtil.isEmpty(selectedVendor) && !StringUtil.isEmpty(selectedOrderNo)) 
        {
            try
            {
                WarehousePayment warehousePayment=productDistributionService.findVendorAndOrderNo(Long.valueOf(selectedVendor),selectedOrderNo);
                if(!StringUtil.isEmpty(warehousePayment))
                { 
                    warehousePaymentDetails = productDistributionService.listWarehousePaymentDetails(warehousePayment.getId());
                    JSONArray paymentProductJSONArray = new JSONArray();

                for (int i = 0; i < warehousePaymentDetails.size(); i++)
                {
                          
                    WarehousePaymentDetails warehousePaymentDetails2 = warehousePaymentDetails.get(i);
                    
                    if(!StringUtil.isEmpty(warehousePaymentDetails2.getDamagedStock())&& warehousePaymentDetails2.getDamagedStock()!=0)
                    {
                        JSONObject productJSON = new JSONObject();
    
                        productJSON.put("id", warehousePaymentDetails2.getWarehousePayment().getVendor().getId());
                        JSONObject paymentProductJSON = new JSONObject();
    
                        paymentProductJSON.put("id", warehousePaymentDetails2.getWarehousePayment().getVendor().getId());
                        paymentProductJSON.put("subCategory", warehousePaymentDetails2.getProduct().getSubcategory().getName());
                        paymentProductJSON.put("product", warehousePaymentDetails2.getProduct().getName());
                        paymentProductJSON.put("productId", warehousePaymentDetails2.getProduct().getId());
                        paymentProductJSON.put("costPrice", Double.valueOf(warehousePaymentDetails2.getCostPrice()));
                        paymentProductJSON.put("currentDamagedStock",warehousePaymentDetails2.getDamagedStock());
                        paymentProductJSON.put("returnDamagedStock","0");    
                        paymentProductJSON.put("amount",(warehousePaymentDetails2.getCostPrice())*0);        
                        paymentProductJSONArray.add(paymentProductJSON);
                        productJSON.put("procurementGrades", paymentProductJSONArray);
                        productArray.add(productJSON);
                    }    
                }
                products.put("products", productArray);
                printAjaxResponse(products, "text/html");
            }
        }     
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
     }
        return null;
    
        
    }
   
    
    @SuppressWarnings("unchecked")
    public String populateProductsForUnSold()
    {
        List<WarehousePaymentDetails> warehousePaymentDetails = new ArrayList<WarehousePaymentDetails>();
        JSONObject products = new JSONObject();
        JSONArray productArray = new JSONArray();
        if (!StringUtil.isEmpty(selectedVendor) && !StringUtil.isEmpty(selectedOrderNo)) 
        {
            try
            {
                WarehousePayment warehousePayment=productDistributionService.warehousePaymentByVendorAndOrderNo(Long.valueOf(selectedVendor),selectedOrderNo);
                if(!StringUtil.isEmpty(warehousePayment))
                { 
                    warehousePaymentDetails = productDistributionService.listWarehousePaymentDetails(warehousePayment.getId());
                    JSONArray paymentProductJSONArray = new JSONArray();

                for (int i = 0; i < warehousePaymentDetails.size(); i++)
                {
                    
                    WarehousePaymentDetails warehousePaymentDetails2 = warehousePaymentDetails.get(i);
                    WarehouseProduct warehouseProduct = productService.findCostPriceForProduct(warehousePaymentDetails2.getProduct()
                            .getId(), warehousePayment.getWarehouse().getId());
                    if(!StringUtil.isEmpty(warehousePaymentDetails2.getStock())&& warehousePaymentDetails2.getStock()!=0)
                    {
                        JSONObject productJSON = new JSONObject();
    
                        productJSON.put("id", warehousePaymentDetails2.getWarehousePayment().getVendor().getId());
                        JSONObject paymentProductJSON = new JSONObject();
    
                        paymentProductJSON.put("id", warehousePaymentDetails2.getWarehousePayment().getVendor().getId());
                        paymentProductJSON.put("subCategory", warehousePaymentDetails2.getProduct().getSubcategory().getName());
                        paymentProductJSON.put("product", warehousePaymentDetails2.getProduct().getName());
                        paymentProductJSON.put("productId", warehousePaymentDetails2.getProduct().getId());
                        paymentProductJSON.put("costPrice", Double.valueOf(warehousePaymentDetails2.getCostPrice()));
                        paymentProductJSON.put("currentStock",warehouseProduct.getStock());
                        paymentProductJSON.put("returnStock","0");    
                        paymentProductJSON.put("amount",(warehousePaymentDetails2.getCostPrice())*0);        
                        paymentProductJSONArray.add(paymentProductJSON);
                        productJSON.put("procurementGrades", paymentProductJSONArray);
                        productArray.add(productJSON);
                    }    
                }
                products.put("products", productArray);
                printAjaxResponse(products, "text/html");
            }
        }     
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
     }
        return null;
    
        
    }
   
  
    public void setSelectedVendor(String selectedVendor) {

        this.selectedVendor = selectedVendor;
    }


    public String getSelectedVendor() {

        return selectedVendor;
    }


    public void setSelectedOrderNo(String selectedOrderNo) {

        this.selectedOrderNo = selectedOrderNo;
    }


    public String getSelectedOrderNo() {

        return selectedOrderNo;
    }


    public void setWarehousePayment(WarehousePayment warehousePayment) {

        this.warehousePayment = warehousePayment;
    }


    public WarehousePayment getWarehousePayment() {

        return warehousePayment;
    }

    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }


    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }


    public Map<Long, String> getSelectVendorList() {
    	
        List<WarehousePayment> vendorList = productDistributionService.selectVendorListByBranchId(getBranchId());

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (WarehousePayment warehousePayment : vendorList) {
            warehouseDropDownList.put(warehousePayment.getVendor().getId(),warehousePayment.getVendor().getVendorName());
/*            warehouseDropDownList.put(warehousePayment.getVendor().getId(), warehousePayment.getVendor().getVendorId()+"-"+warehousePayment.getVendor().getVendorName());
*/
        }
        return warehouseDropDownList;
        
    }
    
    
    public List<Warehouse> getWarehouses() {

        return productDistributionService.listWarehouse();
    }
    
   /* public Map<Long, String> getListWarehouse() {

        List<Warehouse> warehouseList = productDistributionService.listWarehouse();

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (Warehouse warehouse : warehouseList) {
            warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  "
                    + warehouse.getCode());
        }
        return warehouseDropDownList;
    }*/
    
    
    /*public List<WarehousePayment> getSelectedOrderNoList()
    {
        return productDistributionService.selectOrderNoList(selectedVendor);
        
    }*/
   
    public void populateOrderNoByVendor() throws Exception {
        
        if (!selectedVendor.equalsIgnoreCase("null")&& (!StringUtil.isEmpty(selectedVendor))) {
                orderNoList = productDistributionService.selectOrderNoList(Long.valueOf(selectedVendor));

            JSONArray orderArr = null;
            if (!ObjectUtil.isEmpty(orderNoList)) {
                orderArr = new JSONArray();
                for (WarehousePayment warehousePayment : orderNoList) {
                    orderArr.add(getJSONObject(warehousePayment.getOrderNo(), warehousePayment.getOrderNo()));
                }
            }
            sendAjaxResponse(orderArr);
        }
    }
    
 public void populateWarehouse() throws Exception {
        
        if ((!selectedVendor.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedVendor))) 
                && !selectedOrderNo.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedOrderNo))){
                warehouseList = productDistributionService.warehouseByvendorAndOrderNo(Long.valueOf(selectedVendor),selectedOrderNo); 

            JSONArray warehouseArr = null;
            if (!ObjectUtil.isEmpty(warehouseList)) {
                warehouseArr = new JSONArray();
                for (WarehousePayment warehousePayment : warehouseList) {
                    warehouseArr.add(getJSONObject(warehousePayment.getWarehouse().getId(),
/*                    warehousePayment.getWarehouse().getCode()+"-"+warehousePayment.getWarehouse().getName()));*/
                    warehousePayment.getWarehouse().getName()));
                }
            }
            sendAjaxResponse(warehouseArr);
        }
    }
 
 
 public void populateUnsoldOrderNoByVendor() throws Exception {
     
     if (!selectedVendor.equalsIgnoreCase("null")&& (!StringUtil.isEmpty(selectedVendor))) {
             orderNoList = productDistributionService.loadOrderNobasedOnVendorAndQty(Long.valueOf(selectedVendor));

         JSONArray orderArr = null;
         if (!ObjectUtil.isEmpty(orderNoList)) {
             orderArr = new JSONArray();
             for (WarehousePayment warehousePayment : orderNoList) {
                 orderArr.add(getJSONObject(warehousePayment.getOrderNo(), warehousePayment.getOrderNo()));
             }
         }
         sendAjaxResponse(orderArr);
     }
 }
 
 public String populatePrintHTML() {

     initializeDistributionPrintMap();
     if (!StringUtil.isEmpty(receiptNo)) {
         WarehouseStockReturn stockReturn = productDistributionService
                 .findStockReturnByRecNo(receiptNo);
         buildTransactionPrintMap(stockReturn);
     }
     return "html";
 }
 private void initializeDistributionPrintMap() {

     this.stockReturnMap = new HashMap<String, Object>();
     List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
     Map<String, Object> totalMap = new LinkedHashMap<String, Object>();
     this.stockReturnMap.put("recNo", "");
     this.stockReturnMap.put("vId", "");
     this.stockReturnMap.put("vName", "");
     this.stockReturnMap.put("date", "");
     this.stockReturnMap.put("productMapList", productMapList);
     this.stockReturnMap.put("finalAmount", "");
     this.stockReturnMap.put("totalAmt", "");
     this.stockReturnMap.put("totalQty", "");
     this.stockReturnMap.put("totalStockVal", "");
     // this.warehouseMap.put("vendorCash", "");
     // this.warehouseMap.put("vendorCredit", "");
     this.stockReturnMap.put("payMode", "");
     this.stockReturnMap.put("payAmt", "");
     this.stockReturnMap.put("credAmt", "");
     this.stockReturnMap.put("remarks", "");
 }
 
 
 private void buildTransactionPrintMap(WarehouseStockReturn stockReturn) {

     List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();
     if (!ObjectUtil.isEmpty(stockReturn)) {
            DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
        // ESEAccount eseAccount =
         // accountService.findAccountByVendorIdAndType(warehousePayment.getVendor().getVendorId(),ESEAccount.VENDOR_ACCOUNT);

         // if (!ObjectUtil.isEmpty(warehousePayment.getAgroTransaction())) {
         if (!StringUtil.isEmpty(stockReturn.getReceiptNo())) {
             this.stockReturnMap.put("recNo", stockReturn.getReceiptNo());
         }
         if (!ObjectUtil.isEmpty(stockReturn.getVendor())
                 && !StringUtil.isEmpty(stockReturn.getVendor().getVendorId())) {
             this.stockReturnMap.put("vId", stockReturn.getVendor().getVendorId());
         }
         if (!ObjectUtil.isEmpty(stockReturn.getVendor())
                 && !StringUtil.isEmpty(stockReturn.getVendor().getVendorName())) {
             this.stockReturnMap.put("vName", stockReturn.getVendor().getVendorName());
         }
         if (!ObjectUtil.isEmpty(stockReturn.getOrderNo())
                 && !StringUtil.isEmpty(stockReturn.getOrderNo())) {
             this.stockReturnMap.put("orderNo", stockReturn.getOrderNo());
         }
         if (!ObjectUtil.isEmpty(stockReturn.getTrxnDate())) {
             this.stockReturnMap.put("date", df.format(stockReturn.getTrxnDate()));
         }

         this.stockReturnMap.put("warehouseCode", stockReturn.getWarehouse().getCode());
         this.stockReturnMap.put("warehouseName", stockReturn.getWarehouse().getName());
        /* this.stockReturnMap
                 .put("totalAmt", CurrencyUtil.getDecimalFormat(totalQuantity, "##.00"));*/
        // this.stockReturnMap.put("totalTax", CurrencyUtil.thousandSeparator(tax));
         this.stockReturnMap.put("finalAmount", CurrencyUtil.thousandSeparator(stockReturn.getTotalAmount()));

         if (stockReturn.getPaymentMode().equals("CS"))
             this.stockReturnMap.put("payMode", "Cash");
         this.stockReturnMap.put("payAmt", CurrencyUtil.getDecimalFormat(stockReturn
                 .getPaymentAmount(), "##.00"));
         if (stockReturn.getPaymentMode().equals("CR"))
             this.stockReturnMap.put("payMode", "Credit");
         this.stockReturnMap.put("credAmt", CurrencyUtil.getDecimalFormat(stockReturn
                 .getTotalAmount(), "##.00"));
         this.stockReturnMap.put("remarks", stockReturn.getRemarks());
         // this.warehouseMap.put("vendorCash",
         // CurrencyUtil.getDecimalFormat(eseAccount.getCashBalance(),"##.00"));
         // this.warehouseMap.put("vendorCredit",
         // CurrencyUtil.getDecimalFormat(eseAccount.getCreditBalance(),"##.00"));
         // }

         this.stockReturnMap.put("paymentAmout", CurrencyUtil.thousandSeparator(stockReturn
                 .getPaymentAmount()));
         this.stockReturnMap.put("productMapList", productMapList);
         if (!ObjectUtil.isEmpty(stockReturn.getWarehouseStockReturnDetails())) {
             for (WarehouseStockReturnDetails stockReturnDetails : stockReturn
                     .getWarehouseStockReturnDetails()) {
                 Map<String, Object> productMap = new LinkedHashMap<String, Object>();
                 String productName = "";
                 String categoryName = "";
                 String proUnit = "";
                 if (!ObjectUtil.isEmpty(stockReturnDetails.getProduct()))
                     productName = !StringUtil.isEmpty(stockReturnDetails.getProduct()
                             .getName()) ? stockReturnDetails.getProduct().getName() : "";

                 if (!ObjectUtil.isEmpty(stockReturnDetails.getProduct().getSubcategory()))
                     categoryName = !StringUtil.isEmpty(stockReturnDetails.getProduct()
                             .getSubcategory().getName()) ? stockReturnDetails.getProduct()
                             .getSubcategory().getName() : "";

                 if (!ObjectUtil.isEmpty(stockReturnDetails.getProduct()))
                     proUnit = !StringUtil.isEmpty(stockReturnDetails.getProduct()
                             .getUnit()) ? stockReturnDetails.getProduct().getUnit() : "";

                 productMap.put("category", categoryName);
                 productMap.put("product", productName);
                 productMap.put("costPrice", CurrencyUtil.getDecimalFormat(
                		 stockReturnDetails.getCostPrice(), "##.00"));
                 productMap.put("badQty", !StringUtil.isEmpty(stockReturnDetails
                         .getDamagedStock()) ? stockReturnDetails.getDamagedStock() : "");
                 
                 

                 productMap.put("totalAmt", CurrencyUtil.thousandSeparator(stockReturnDetails.getAmount()));
                 
              
                
                 productMapList.add(productMap);
             }
         }

     }
 }
 
    
    @SuppressWarnings("unchecked")
    protected JSONObject getJSONObject(Object id, Object name) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        return jsonObject;
    }
    
    
    


    /**
     * Gets the current date.
     * @return the current date
     */
    public String getCurrentDate() {

        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        return df.format(currentDate.getTime());

    }

    public void setSelectedWarehouse(String selectedWarehouse) {

        this.selectedWarehouse = selectedWarehouse;
    }
    public String getSelectedWarehouse() {

        return selectedWarehouse;
    }

    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    public ILocationService getLocationService() {

        return locationService;
    }

    public void setReturnDamagedProducts(String returnDamagedProducts) {

        this.returnDamagedProducts = returnDamagedProducts;
    }

    public String getReturnDamagedProducts() {

        return returnDamagedProducts;
    }
    
    public Map<Integer, String> getCashType() {
        
        return cashType;
    }

    public void setCashType(Map<Integer, String> cashType) {
    
        this.cashType = cashType;
    }

    public void setStartDate(String startDate) {

        this.startDate = startDate;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setTotaAmountFinal(double totaAmountFinal) {

        this.totaAmountFinal = totaAmountFinal;
    }

    public double getTotaAmountFinal() {

        return totaAmountFinal;
    }
    public void setTotalQtyFinal(long totalQtyFinal) {

        this.totalQtyFinal = totalQtyFinal;
    }

    public long getTotalQtyFinal() {

        return totalQtyFinal;
    }

    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

    public String getRemarks() {

        return remarks;
    }

    public void setPaymentMode(String paymentMode) {

        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {

        return paymentMode;
    }

    public void setWarehouseStockReturn(WarehouseStockReturn warehouseStockReturn) {

        this.warehouseStockReturn = warehouseStockReturn;
    }

    public WarehouseStockReturn getWarehouseStockReturn() {

        return warehouseStockReturn;
    }

    public void setWarehouseStockReturnDetails(WarehouseStockReturnDetails warehouseStockReturnDetails) {

        this.warehouseStockReturnDetails = warehouseStockReturnDetails;
    }

    public WarehouseStockReturnDetails getWarehouseStockReturnDetails() {

        return warehouseStockReturnDetails;
    }

    public void setProductService(IProductService productService) {

        this.productService = productService;
    }

    public IProductService getProductService() {

        return productService;
    }

    public List<WarehousePayment> getOrderNoList() {
        
        if (!StringUtil.isEmpty(selectedVendor)) {
            orderNoList = productDistributionService.selectOrderNoList(Long.valueOf(selectedVendor));
        }
    
        return orderNoList;
    }

    public void setOrderNoList(List<WarehousePayment> orderNoList) {
    
    }

    public List<WarehousePayment> getWarehouseList() {
        
        if (!StringUtil.isEmpty(selectedVendor) && !StringUtil.isEmpty(selectedOrderNo) ) {
            warehouseList = productDistributionService.warehouseByvendorAndOrderNo(Long.valueOf(selectedVendor),selectedOrderNo);
        }
    
        return warehouseList;
    }

    public void setWarehouseList(List<WarehousePayment> warehouseList) {
    
        this.warehouseList = warehouseList;
    }


    public String getCashCreditValue() {
    
        return cashCreditValue;
    }

    public void setCashCreditValue(String cashCreditValue) {
    
        this.cashCreditValue = cashCreditValue;
    }

    public void populateVendorAccBalance() throws Exception {

        String cashAndCredit = "";
        if (!StringUtil.isEmpty(selectedVendor)) {
            Vendor vendor = productService.findVendorIdById(Long.valueOf(selectedVendor));
            if (!StringUtil.isEmpty(vendor.getVendorId())) {
                ESEAccount account = accountService.findAccountByVendorIdAndType(vendor.getVendorId(),
                        ESEAccount.VENDOR_ACCOUNT);
                if (!StringUtil.isEmpty(account)) {
                    cashCreditValue = (account.getCashBalance() + "," + account.getCreditBalance());
                    cashAndCredit = cashCreditValue;
                } else {
                    cashCreditValue = "";
                }
                cashAndCredit = cashCreditValue;
            }
        }
        sendAjaxResponse(cashAndCredit);

    }

    public String getSelectedType() {
    
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
    
        this.selectedType = selectedType;
    }

    public List<WarehousePayment> getAllOrderNo() {
        
        if (!StringUtil.isEmpty(selectedVendor)) {
        allOrderNo= productDistributionService.loadOrderNobasedOnVendorAndQty(Long.valueOf(selectedVendor));
        }
        return allOrderNo;
    }

    public void setAllOrderNo(List<WarehousePayment> allOrderNo) {
    
        this.allOrderNo = allOrderNo;
    }

    
   /* public Map<Long, String> getAllWarehouse() {

        List<WarehousePayment> warehouseList = productDistributionService.listAllWarehouse();

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (WarehousePayment warehousePayment : warehouseList) {
            warehouseDropDownList.put(warehousePayment.getWarehouse().getId(), warehousePayment.getWarehouse().getCode() + " -  "
                    + warehousePayment.getWarehouse().getName());
        }
        return warehouseDropDownList;
    }*/
    
    public Map<Long, String> getVendorList() {
        List<WarehousePayment> vendorList = productDistributionService.selectVendorList();

        Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
        for (WarehousePayment warehousePayment : vendorList) {
            warehouseDropDownList.put(warehousePayment.getVendor().getId(), warehousePayment.getVendor().getVendorName());
/*            warehouseDropDownList.put(warehousePayment.getVendor().getId(), warehousePayment.getVendor().getVendorId()+"-"+warehousePayment.getVendor().getVendorName());
*/        }
        return warehouseDropDownList;
        
    }

    public List<WarehousePayment> getAllWarehouse() {
        
        if (!StringUtil.isEmpty(selectedVendor) && !StringUtil.isEmpty(selectedOrderNo) ) {
            allWarehouse = productDistributionService.warehouseByvendorAndOrderNo(Long.valueOf(selectedVendor),selectedOrderNo);
        }
    
        return allWarehouse;
    }

    public void setAllWarehouse(List<WarehousePayment> allWarehouse) {
    
        this.allWarehouse = allWarehouse;
    }

    public IAccountService getAccountService() {
        
        return accountService;
    }

    public void setAccountService(IAccountService accountService) {
    
        this.accountService = accountService;
    }
    
    public WarehousePaymentDetails getWarehousePaymentDetails() {
        
        return warehousePaymentDetails;
    }

    public void setWarehousePaymentDetails(WarehousePaymentDetails warehousePaymentDetails) {
    
        this.warehousePaymentDetails = warehousePaymentDetails;
    }
    
    public String getStockDescription() {
        
        return stockDescription;
    }

    public void setStockDescription(String stockDescription) {
    
        this.stockDescription = stockDescription;
    }
    
    public IUniqueIDGenerator getIdGenerator() {
    
        return idGenerator;
    }

    public void setIdGenerator(IUniqueIDGenerator idGenerator) {
    
        this.idGenerator = idGenerator;
    }

    public String getReceiptNo() {
    
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
    
        this.receiptNo = receiptNo;
    }

    public double getPaymentAmount() {
        
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
    
        this.paymentAmount = paymentAmount;
    }
   }