package com.sourcetrace.esesw.view.report.agro;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturn;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturnDetails;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class WarehouseStockReturnReportAction  extends BaseReportAction implements IExporter
{

    /**
     * Global Variables
     */
    private static final long serialVersionUID = -1559094949467300703L;
    private static final Logger LOGGER = Logger.getLogger(WarehouseStockReturnReportAction.class);
    private List<String> fields = new ArrayList<String>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    private String warehouse;
    private String order;
    private ILocationService locationService;
    @Autowired
    private IProductDistributionService productDistributionService;
    private String vendor;
    private String receiptNo;
    private String returnType;
    private WarehouseStockReturnDetails filter;
    List<WarehousePayment> orderNoList = new ArrayList<WarehousePayment>();


    /**
     * 
     * @Warehouse Stock Return Report List
     */
    
    public String list()
    {
        LOGGER.info("List method loading....");
        Calendar currentDate = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        super.startDate = df.format(DateUtil.getFirstDateOfMonth(currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH)));
        super.endDate = df.format(currentDate.getTime());
        fields.add(getText("date"));
        fields.add(getText("warehouse"));
        fields.add(getText("orderNo"));
        fields.add(getText("vendor"));
        fields.add(getText("returnType"));
        System.out.println("fields"+fields.size());
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
        
    }
    
    /**
     *  @Warehouse Stock Return Report Detail
     */
    public String detail() throws Exception 
    {

        LOGGER.info("Detail method loading....");
        if (ObjectUtil.isEmpty(this.filter))
            this.filter = new WarehouseStockReturnDetails();

        if (!StringUtil.isEmpty(warehouse))
        {
            WarehouseStockReturn wsr = new WarehouseStockReturn();
            Warehouse w = new Warehouse();
            w.setCode(warehouse);
            wsr.setWarehouse(w);
            filter.setWarehouseStockReturn(wsr);
      }
       
        if (!StringUtil.isEmpty(order))
        {
            
            if(filter.getWarehouseStockReturn()!=null)
            {
                filter.getWarehouseStockReturn().setOrderNo(order);
            }
            else
            {
                WarehouseStockReturn wsr = new WarehouseStockReturn();
                wsr.setOrderNo(order);
                filter.setWarehouseStockReturn(wsr);
            }
        }
        
        if (!StringUtil.isEmpty(vendor))
        {
            WarehouseStockReturn wsr = new WarehouseStockReturn();
            Vendor vr = new Vendor();
            vr.setVendorId(vendor);
            wsr.setVendor(vr);
            filter.setWarehouseStockReturn(wsr);
            //filter.getWarehousePayment().getVendor().setVendorId(vendor);
        }
        if (!StringUtil.isEmpty(returnType))
        {
            
           if(filter.getWarehouseStockReturn()!=null){
               filter.getWarehouseStockReturn().setReturnType(returnType);
           }else{
               WarehouseStockReturn  wsr = new WarehouseStockReturn();
               wsr.setReturnType(returnType);;
               filter.setWarehouseStockReturn(wsr);
           }
        }

       Map data = reportService.listWithMultipleFiltering(getSord(), getSidx(), getStartIndex(),
               getLimit(), getsDate(), geteDate(), this.filter, getPage(), null);
       return sendJSONResponse(data);
    }
    

    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) 
    {
        LOGGER.info("toJSON method is loading....");
        JSONObject jsonObject = new JSONObject();
        WarehouseStockReturnDetails warehouseStockReturnDetails = (WarehouseStockReturnDetails) obj;
        JSONArray rows = new JSONArray();
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getReturnType());
        rows.add(DateUtil.getDateInFormat("dd/MM/yyyy",warehouseStockReturnDetails.getWarehouseStockReturn().getTrxnDate()));
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getVendor().getVendorName());
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getWarehouse().getName());
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getOrderNo());
        rows.add(warehouseStockReturnDetails.getProduct().getSubcategory().getName());
        rows.add(warehouseStockReturnDetails.getProduct().getName());
        rows.add(warehouseStockReturnDetails.getDamagedStock());
        rows.add(warehouseStockReturnDetails.getAmount());
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getTotalAmount());
        rows.add(warehouseStockReturnDetails.getWarehouseStockReturn().getRemarks());

       jsonObject.put("id", warehouseStockReturnDetails.getId());
       jsonObject.put("cell", rows);
       return jsonObject;
    }
    
    
    public Map<String, String> getWarehouseList()
    {

        Map<String, String> warehouseListMap = new LinkedHashMap<String, String>();
        List<Warehouse> warehouseList = locationService.listWarehouse();
        for (Warehouse obj : warehouseList) 
        {
            warehouseListMap.put(obj.getCode(), obj.getName() + " - " + obj.getCode());

        }
        return warehouseListMap;
    }
    
    public Map<String, String> getVendorList()
    {
        
        Map<String, String> vendorListMap = new LinkedHashMap<String, String>();
        List<Vendor> vendorList = productDistributionService.listVendor();
        for (Vendor obj : vendorList) 
        {
            vendorListMap.put(obj.getVendorId(), obj.getVendorName()+ " - " + obj.getVendorId());

        }
        
        return vendorListMap;
    }
    
    public List<String> getOrderNoList()
    {
     return productDistributionService.listOfOrderNo();
    }

    
    
    public List<String> getStockReturnTypeList()
    {
 
        return productDistributionService.listStockReturnType();
    }
    
    
    
    public void setFields(List<String> fields)
    {

        this.fields = fields;
    }
  
    public List<String> getFields()
    {

        return fields;
    }

    public void setWarehouse(String warehouse) {

        this.warehouse = warehouse;
    }

    public String getWarehouse() {

        return warehouse;
    }

    public void setOrder(String order) {

        this.order = order;
    }

    public String getOrder() {

        return order;
    }

    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    public ILocationService getLocationService() {

        return locationService;
    }

    public void setVendor(String vendor) {

        this.vendor = vendor;
    }

    public String getVendor() {

        return vendor;
    }

    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    public String getReceiptNo() {

        return receiptNo;
    }

    public void setReturnType(String returnType) {

        this.returnType = returnType;
    }
    
    public WarehouseStockReturnDetails getFilter() {
    
        return filter;
    }


    public void setFilter(WarehouseStockReturnDetails filter) {
    
        this.filter = filter;
    }

    public String getReturnType() {

        return returnType;
    }
    
    public IProductDistributionService getProductDistributionService() {
        
        return productDistributionService;
    }


    public void setProductDistributionService(IProductDistributionService productDistributionService) {
    
        this.productDistributionService = productDistributionService;
    }
    
    
    public void setOrderNoList(List<WarehousePayment> orderNoList) {
    
        this.orderNoList = orderNoList;
    }






@Override
public InputStream getExportDataStream(String exportType) throws IOException {

    // TODO Auto-generated method stub
    return null;
}
}

