/*
 * PaymentAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cxf.jaxrs.client.WebClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.util.IntegerUtil;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.ProductDistributionService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.WebTransactionAction;

import net.glxn.qrgen.QRCode;

public class QRCodeGeneratorAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	private IFarmerService farmerService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private ICatalogueService catalogueService;
	@Autowired
	private IProductDistributionService productDistributionService;
	private String id;
	private String selectedCity;
	private String selectedVillage;
	private String selectedFarmer;
	private String selectedProduct;
	private String selectedVariety;
	private String cultType;
	private String lotNo;
	private String fileName;
	private InputStream fileInputStream;
	private String selectedFarm;
	private String startDate;
	private String selectedWarehouse;

	List<String> villageList = new ArrayList<String>();
	List<String> farmerList = new ArrayList<String>();
	List<String> productList = new ArrayList<String>();
	List<String> cityList = new ArrayList<String>();

	DecimalFormat formatter = new DecimalFormat("#.##");
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String LOCALHOST = "http://localhost:8090/tserv/rsk";
    private static final String format = "application/json";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);

	/**
	 * Gets the city list.
	 * 
	 * @return the city list
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCityList() {

		List<Municipality> cities = locationService.listCity();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality city : cities) {
				cityList.add(city.getName() + "-" + city.getCode());
			}
		}
		return cityList;

	}
	public Map<Long, String> getListWarehouse() {

		List<Warehouse> warehouseList = locationService.listWarehouse();

		Map<Long, String> warehouseDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : warehouseList) {
			warehouseDropDownList.put(warehouse.getId(), warehouse.getName() + " -  " + warehouse.getCode());
		}
		return warehouseDropDownList;
	}
	
	
	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */
	public List<String> getVillageList() {

		if (!StringUtil.isEmpty(selectedCity)) {
			String cityCode[] = selectedCity.split("-");
			List<Village> listVillage = locationService
					.listVillagesByCityCode(cityCode[1].toString().trim());
			if (!ObjectUtil.isListEmpty(listVillage)) {
				for (Village village : listVillage) {
					villageList.add(village.getName() + "-" + village.getCode());
				}
			}
		}
		return villageList;
	}

	/**
	 * Populate village.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateVillage() throws Exception {
		JSONArray villageArr = new JSONArray();

		if (!ObjectUtil.isEmpty(selectedCity) && (!StringUtil.isEmpty(selectedCity))) {
			String cityCode[] = selectedCity.split("-");
			List<Village> listVillage = locationService.listVillagesByCityCode(cityCode[1].toString().trim());
			if (!ObjectUtil.isListEmpty(listVillage)) {
				for (Village village : listVillage) {
					villageArr.add(getJSONObject(village.getCode(),
							village.getName() + "-" + village.getCode()));
				}
			}
		}
		sendAjaxResponse(villageArr);
		return null;
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Populate farmer.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateFarmer() throws Exception {
		JSONArray farmerArr = new JSONArray();

		if (!ObjectUtil.isEmpty(selectedVillage)
				&& (!StringUtil.isEmpty(selectedVillage) && (!selectedVillage.equals("0")))) {
			//String[] villageCode = selectedVillage.split("-");
			List<Object[]> listFarmer = farmerService
					.listFarmerCodeIdNameByVillageCode(selectedVillage);
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerArr.add(getJSONObject(farmer[0].toString().trim(),
							farmer[2].toString().trim()));
				}
			}
		}
		sendAjaxResponse(farmerArr);
		return null;
	}
	
	public  String populateFarm() throws Exception {
		JSONArray farmArr = new JSONArray();
		if (!StringUtil.isEmpty(selectedFarmer)) {
			List<Farm> listFarm = farmerService.listFarmerFarmByFarmerId(selectedFarmer);
			if (!ObjectUtil.isListEmpty(listFarm)) {
				for (Farm farm : listFarm) {
					farmArr.add(getJSONObject(farm.getId(),farm.getFarmName()));
				}
			}
		}
		sendAjaxResponse(farmArr);
		return null;
	}

	/**
	 * Gets the farmer list.
	 * 
	 * @return the farmer list
	 */
	public List<String> getFarmerList() {

		if (!StringUtil.isEmpty(selectedVillage)) {
			String[] villageCode = selectedVillage.split("-");
			List<Object[]> listFarmer = farmerService
					.listFarmerCodeIdNameByVillageCode(villageCode[1].toString().trim());
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerList.add(farmer[2].toString().trim() + " " + farmer[3].toString().trim() + "-"
							+ farmer[1].toString().trim());
				}
			}
		}
		return farmerList;
	}
	
	/**
	 * Sets the selected city.
	 * 
	 * @param selectedCity
	 *            the new selected city
	 */
	public void setSelectedCity(String selectedCity) {

		this.selectedCity = selectedCity;
	}

	/**
	 * Gets the selected city.
	 * 
	 * @return the selected city
	 */
	public String getSelectedCity() {

		return selectedCity;
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
	 * Sets the selected village.
	 * 
	 * @param selectedVillage
	 *            the new selected village
	 */
	public void setSelectedVillage(String selectedVillage) {

		this.selectedVillage = selectedVillage;
	}

	/**
	 * Gets the selected village.
	 * 
	 * @return the selected village
	 */
	public String getSelectedVillage() {

		return selectedVillage;
	}

	/**
	 * Sets the selected farmer.
	 * 
	 * @param selectedFarmer
	 *            the new selected farmer
	 */
	public void setSelectedFarmer(String selectedFarmer) {

		this.selectedFarmer = selectedFarmer;
	}

	/**
	 * Gets the selected farmer.
	 * 
	 * @return the selected farmer
	 */
	public String getSelectedFarmer() {

		return selectedFarmer;
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
	 * Sets the farmer list.
	 * 
	 * @param farmerList
	 *            the new farmer list
	 */
	public void setFarmerList(List<String> farmerList) {

		this.farmerList = farmerList;
	}

	/**
	 * Sets the city list.
	 * 
	 * @param cityList
	 *            the new city list
	 */
	public void setCityList(List<String> cityList) {

		this.cityList = cityList;
	}

	/**
	 * Sets the village list.
	 * 
	 * @param villageList
	 *            the new village list
	 */
	public void setVillageList(List<String> villageList) {

		this.villageList = villageList;
	}

	public List<String> getProductList() {

		List<ProcurementProduct> listPriduct = productDistributionService.listProcurementProduct();
			if (!ObjectUtil.isListEmpty(listPriduct)) {
				for (ProcurementProduct product : listPriduct) {
					productList.add(product.getName() + "-" + product.getCode());
				}
		}
		return productList;
	}
	
	public List<String> getVarietyList() {

		if (!ObjectUtil.isEmpty(selectedProduct) && (!StringUtil.isEmpty(selectedProduct))) {
			String cityCode[] = selectedProduct.split("-");
			List<Object[]> listPriduct = productService.listProcurementVarietyByCode(cityCode[0].toString().trim());
			if (!ObjectUtil.isListEmpty(listPriduct)) {
				for (Object[] product : listPriduct) {
					productList.add(product[1] + "-" + product[0]);
				}
			}
		}
		return productList;
	}
	
	public String populateProcurementVariety() throws Exception {
		JSONArray villageArr = new JSONArray();

		if (!ObjectUtil.isEmpty(selectedProduct) && (!StringUtil.isEmpty(selectedProduct))) {
			String cityCode[] = selectedProduct.split("-");
			List<Object[]> listPriduct = productService.listProcurementVarietyByCode(cityCode[1].toString());
			if (!ObjectUtil.isListEmpty(listPriduct)) {
				for (Object[] product : listPriduct) {
					villageArr.add(getJSONObject(product[0].toString(),
							product[0].toString() + "-" + product[1].toString()));
				}
			}
		}
		sendAjaxResponse(villageArr);
		return null;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

	public String populateProcurementProducts() throws Exception {
		JSONArray villageArr = new JSONArray();

		if (!ObjectUtil.isEmpty(selectedFarmer) && (!StringUtil.isEmpty(selectedFarmer))) {
		//	String cityCode[] = selectedFarmer.split("-");
			List<ProcurementProduct> listPriduct = productService
					.listProcurmentProductsByFarmer(selectedFarmer);
			if (!ObjectUtil.isListEmpty(listPriduct)) {
				for (ProcurementProduct product : listPriduct) {
					villageArr.add(getJSONObject(product.getCode(),
							product.getName() + "-" + product.getCode()));
				}
			}
		}
		sendAjaxResponse(villageArr);
		return null;
	}

	public String create() throws Exception {
		if (getCurrentTenantId().equalsIgnoreCase(ESESystem.AGRO_TENANT) || getCurrentTenantId().equalsIgnoreCase(ESESystem.SIMFED_TENANT_ID)){
			setFileName(getText("qrCode") + fileNameDateFormat.format(new Date()));
			if(!StringUtil.isEmpty(selectedFarm)){
			Farm farm = farmerService.findFarmByfarmId(Long.valueOf(selectedFarm));
			String survey=!ObjectUtil.isEmpty(farm.getFarmDetailedInfo()) ? farm.getFarmDetailedInfo().getSurveyNumber() : "";
		
			Warehouse warehouse=locationService.findWarehouseById(Long.valueOf(selectedWarehouse));
			String location=warehouse.getLocation();
			String url = request.getRequestURL().toString();
			 URL aURL = new URL(url);
			 String path=aURL.getPath();
			 String fullPath[]= path.split("/", 0);			
			 String urll=aURL.getProtocol()+"://"+aURL.getAuthority()+"/"+fullPath[1];		 
			 String tenant=getCurrentTenantId();
			 String timestamp=DateUtil.getRevisionNoWithMillSec();
			String message =urll+"/getTraceDetails/traceDetails.html"+"?traceDetails=%"+farm.getFarmCode()+"%"+tenant+"%"; 
			/*String message =urll+"/getTraceDetails/traceDetails.html"+"?traceDetails=%"+farm.getFarmCode()+"%"+tenant+"%/"+'\n'+
					'\n'+
					"Enjoy Organic as per NPOP Standards!!"+ '\n'+
                    "Harvest On :"+ startDate+'\n'+
                    "Farm No:"+ survey +'\n'+
                   // "Krishi Pragati Foundation Centre" +'\n'+
                     "Warehouse:"+  location+ '\n'+'\n'
                   // "www.go4fresh.in";
                     ; */
			setFileName(getText("qrCode") + fileNameDateFormat.format(new Date()));
		ByteArrayOutputStream stream = QRCode.from(message).withErrorCorrection(ErrorCorrectionLevel.L).withHint(EncodeHintType.MARGIN, 2).withSize(250, 250).stream();
		setFileInputStream(new ByteArrayInputStream(stream.toByteArray()));
		return "code";
		}
		}
		else if (!StringUtil.isEmpty(selectedFarmer) && (!StringUtil.isEmpty(selectedProduct))) {
			// String url = request.getRequestURL().toString();
			/*
			 * InetAddress ipaddress =
			 * InetAddress.getByName(request.getServerName()); String uri =
			 * request.getScheme() + "://" + ipaddress.getHostAddress() +
			 * ("http".equals(request.getScheme()) && request.getServerPort() ==
			 * 80 || "https".equals(request.getScheme()) &&
			 * request.getServerPort() == 443 ? "" : ":" +
			 * request.getServerPort() ) +
			 * "/qrCodeGen_populateImage?selectedFarmer="+selectedFarmer+
			 * "&selectedProduct="+selectedProduct;
			 */
			Map<String,String> dataMap = new HashMap();
			//String[] product = selectedProduct.split("-");
		//	String cityCode[] = selectedFarmer.split("-");
		//	String variety[] = selectedVariety.split("-");
			Farmer farmer = farmerService.findFarmerByFarmerId(selectedFarmer);
			String[] selectedProducts = selectedProduct.split("-");
			ProcurementProduct pp = productService.findProcurementProductByCode(selectedProducts[1]);
			ProcurementVariety pv = productService.findProcurementVarietyByCode(selectedVariety.toString().trim());
			FarmCatalogue fc = catalogueService.findCatalogueByCode(cultType);
			Village v = farmer.getVillage();
			Warehouse q =  farmer.getSamithi();
			String data = "Farmer : " + farmer.getFarmerCodeAndName() + "\n\nVillage : " + v!=null ? v.getName() : "" + "\n";
			data += "\nFarmer group : " + q.getName();
			data+="\nProduct Name : "+pp.getName();
			setFileName(getText("qrCode") + fileNameDateFormat.format(new Date()));
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences) && preferences.getPreferences().get(ESESystem.QR_CODE_TEXT)!=null) {
				dataMap.put("farmerKey",farmer.getFirstName()+" "+farmer.getLastName()==null ? "" : farmer.getLastName());
				dataMap.put("productKey",pp.getName());
				dataMap.put("varietyKey",pv.getName());
				dataMap.put("samithiKey",q!=null ? q.getName() : "");
				dataMap.put("villageKey",v!=null ? v.getName():"");
				dataMap.put("cultTypeKey",fc!=null ? fc.getName() : "");
				String gram = preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT);
						if(gram!=null && gram.equals("1")){
							dataMap.put("districtKey",v!=null ? v.getGramPanchayat().getCity().getLocality().getName():"");
							dataMap.put("stateKey",v!=null ? v.getGramPanchayat().getCity().getLocality().getState().getName():"");
						}else{
							dataMap.put("districtKey",v!=null ? v.getCity().getLocality().getName() : "");
							dataMap.put("stateKey",v!=null ? v.getCity().getLocality().getState().getName() : "");
						}
			
				dataMap.put("lotNoKey",getLotNo());
				data = preferences.getPreferences().get(ESESystem.QR_CODE_TEXT);
				for(Entry<String, String> dataValue: dataMap.entrySet()){
					data = data.replaceAll("\\{"+dataValue.getKey()+"\\}", dataValue.getValue());
				}
				/*try{
		String url  =  "https://www.google.com/maps/preview/@"+farmer[7].toString()+","+farmer[8].toString()+",8z";
				data+= "<a href=\""+url+"\"> Farmer Location </a>";
				}catch (Exception e) {
					
				}*/
				
			}
               //QRCode for Geo Location
		//	ByteArrayOutputStream stream = QRCode.from("geo:13.0827,80.2707+q="+data).withErrorCorrection(ErrorCorrectionLevel.L).withHint(EncodeHintType.MARGIN, 2).withSize(250, 250).stream();
			ByteArrayOutputStream stream = QRCode.from(data).withErrorCorrection(ErrorCorrectionLevel.L).withHint(EncodeHintType.MARGIN, 2).withSize(250, 250).stream();
			setFileInputStream(new ByteArrayInputStream(stream.toByteArray()));
			return "code";
		}
		
		return fileName;

	}

	public void populateImage() throws Exception {
		if (!StringUtil.isEmpty(selectedFarmer) && (!StringUtil.isEmpty(selectedProduct))) {
			String cityCode[] = selectedFarmer.split("-");
			Farmer f = farmerService.findFarmerByFarmerId(cityCode[1].toString().trim());
			String productAr[] = selectedProduct.split("-");
			ProcurementProduct product = productService.findProcurementProductByCode(productAr[1].toString().trim());
			String disp = f.getFarmerIdAndName() + product.getName();
			response.setContentType("text/html");
			response.setContentLength(disp.length());
			PrintWriter out = response.getWriter();
			out.println(disp);
			out.close();
			out.flush();

		}

	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public String getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(String selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
public Map<String,String> getCultList(){
	
	Map<String, String> cultTypeList = new LinkedHashMap<>();
	if(!StringUtil.isEmpty(getText("cultType")) && IntegerUtil.isInteger(getText("cultType"))){
		List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(Integer.valueOf(getText("cultType")));
		for (FarmCatalogue fc : farmCatalougeList) {
			if (!fc.getName().trim().contentEquals(ESESystem.OTHERS)) {
				cultTypeList.put(fc.getCode(), fc.getName());
			}

		}
		
	}
	return cultTypeList;
	
}
	public String getSelectedVariety() {
		return selectedVariety;
	}

	public void setSelectedVariety(String selectedVariety) {
		this.selectedVariety = selectedVariety;
	}

	public String getCultType() {
		return cultType;
	}

	public void setCultType(String cultType) {
		this.cultType = cultType;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}
	
	
	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSelectedWarehouse() {
		return selectedWarehouse;
	}

	public void setSelectedWarehouse(String selectedWarehouse) {
		this.selectedWarehouse = selectedWarehouse;
	}
    
	
	
}
