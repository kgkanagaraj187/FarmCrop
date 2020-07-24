package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.ese.util.Base64Util;
import com.google.zxing.BinaryBitmap;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;


@Component
public class FarmerTraceDetailsDownload implements ITxnAdapter {
	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private ILocationService locationService;

	@Autowired
	private IPreferencesService preferncesService;

	private Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	private Map<String, String> warehouseMap = new LinkedHashMap<String, String>();
	private Map<String, String> samithiMap = new LinkedHashMap<String, String>();
	private Map<String, String> masterDataMap = new LinkedHashMap<String, String>();
	protected HttpServletResponse response;
	private String farmerImageByteString;
	private List<JSONObject> jsonObjectList;
	
	Map resp = new HashMap();

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		final String RECEIPT_DATE_FORMAT = "dd-MM-yyyy";
		DateFormat df = new SimpleDateFormat(RECEIPT_DATE_FORMAT);
		String farmCode="";
		String lotNo = "";
		String variety="";
		String pckdate="";
		String cldStrg="";
		String serialNo="";
		String noOfBags="";
		String prodType="";
		List<Object[]> objFarm;
		farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
		if(head.getTenantId().equalsIgnoreCase("griffith")){
			String paraArray=farmerService.findQrCodeParameterByKey(farmCode,head.getTenantId());
			String[] data = paraArray.split(",");
			String[] farmCodeTmp=data[0].toString().split(":");
			String[] lotNoTmp=data[1].toString().split(":");
			String[] varietyTmp=data[2].toString().split(":");
			String[] pckdateTmp=data[3].toString().split(":");
			String[] cldStrgTmp=data[4].toString().split(":");
			String[] serialNoTmp=data[5].toString().split(":");
			String[] noOfBagsTmp=data[6].toString().split(":");
			String[] prodTypeTmp=data[7].toString().split(":");
			farmCode=farmCodeTmp[1].toString();
			lotNo=lotNoTmp[1].toString();
			variety=varietyTmp[1].toString();
			pckdate=pckdateTmp[1].toString();
			cldStrg=cldStrgTmp[1].toString();
			serialNo=serialNoTmp[1].toString();
			noOfBags=noOfBagsTmp[1].toString();
			prodType=prodTypeTmp[1].toString();
			
		}
		
		objFarm = farmerService.findFarmerTraceDetailsByFarmCode(farmCode, head.getTenantId());
		
		
		JSONObject jsonObject = new JSONObject();
		for(Object[] obj : objFarm){
			jsonObject.put("FarmName", obj[0]);
			jsonObject.put("FarmerName", obj[2]);
			jsonObject.put("FarmerId", obj[14]);
			jsonObject.put("VillageName", obj[3]);
			jsonObject.put("MunicipalityName", obj[4]);
			jsonObject.put("LocalityName", obj[5]);
			jsonObject.put("StateName", obj[6]);
			jsonObject.put("CountryName", obj[7]);
			jsonObject.put("FarmSurveyNo",obj[8]);
			jsonObject.put("Latitude",obj[9]);
			jsonObject.put("Longitude",obj[10]);
			jsonObject.put("CropName",obj[11]);
			jsonObject.put("SowingDate", obj[12]);
			jsonObject.put("Certified",obj[13]);
			jsonObject.put("OrganicStatus",obj[15]);
			jsonObject.put("LastName",obj[16]);
			jsonObject.put("lotNo",lotNo);
			jsonObject.put("variety",variety);
			jsonObject.put("pckDate",pckdate);
			jsonObject.put("cldStrg",cldStrg);
			jsonObject.put("serialNo",serialNo);
			jsonObject.put("noOfBags",noOfBags);
			jsonObject.put("prodType",prodType);
			jsonObject.put("farmerCode",obj[18]);
			Farmer farmer = farmerService.findFarmerByID(Long.valueOf(obj[1].toString()),head.getTenantId());
			if (farmer.getImageInfo() != null && farmer.getImageInfo().getPhoto() != null
					&& farmer.getImageInfo().getPhoto().getImage() != null) {
				setFarmerImageByteString(Base64Util.encoder(farmer.getImageInfo().getPhoto().getImage()));
			}
			else{
				setFarmerImageByteString("");
			}
			jsonObject.put("farmerPhoto",getFarmerImageByteString());
			Farm farm = farmerService.findFarmByID(Long.valueOf(obj[17].toString()),head.getTenantId());
			if (farm.getActiveCoordinates()!= null && farm.getActiveCoordinates().getFarmCoordinates() !=null && !ObjectUtil.isEmpty(farm.getActiveCoordinates()) &&  !ObjectUtil.isListEmpty(farm.getActiveCoordinates().getFarmCoordinates())) {
				jsonObjectList = getFarmJSONObjects(farm.getActiveCoordinates().getFarmCoordinates());
			} else {
				jsonObjectList = new ArrayList();
			}
			jsonObject.put("farmPlot",jsonObjectList);
		}
		return jsonObject;
		
		
		//return resp;
	}

	
	
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getFormattedValue(String val) {
		if (!StringUtil.isEmpty(val)) {
			return val;
		}
		return "";
	}

	public String getFormattedValue(java.lang.Object val) {
		if (!StringUtil.isEmpty(val)) {
			return String.valueOf(val);
		}
		return "";
	}
	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(String string, java.lang.Object obj) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", string);
		jsonObject.put("name", obj);
		return jsonObject;
	}
	public void sendAjaxResponse(JSONArray jsonArray) {

		try {
			response.setContentType("text/JSON");
			PrintWriter out = response.getWriter();
			out.println(jsonArray.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void printAjaxResponse(Object value, String contentType) {

		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (!StringUtil.isEmpty(contentType)) {
				response.setContentType(contentType);
			}
			out = response.getWriter();
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String getFarmerImageByteString() {
		return farmerImageByteString;
	}



	public void setFarmerImageByteString(String farmerImageByteString) {
		this.farmerImageByteString = farmerImageByteString;
	}
	@SuppressWarnings("unchecked")
	private List<JSONObject> getFarmJSONObjects(Set<Coordinates> coordinates) {

		List<JSONObject> returnObjects = new ArrayList<JSONObject>();
		List<Coordinates> listCoordinates = new ArrayList<Coordinates>(coordinates);
		Collections.sort(listCoordinates);
		if (!ObjectUtil.isListEmpty(listCoordinates)) {
			for (Coordinates coordinateObj : listCoordinates) {
				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("orderNo",!ObjectUtil.isEmpty(coordinateObj.getOrderNo())
				// ? coordinateObj.getOrderNo():"");
				jsonObject.put("lat",
						!ObjectUtil.isEmpty(coordinateObj.getLatitude()) ? coordinateObj.getLatitude() : "");
				jsonObject.put("lon",
						!ObjectUtil.isEmpty(coordinateObj.getLongitude()) ? coordinateObj.getLongitude() : "");
				returnObjects.add(jsonObject);
			}
		}
		return returnObjects;
	}



	public List<JSONObject> getJsonObjectList() {
		return jsonObjectList;
	}



	public void setJsonObjectList(List<JSONObject> jsonObjectList) {
		this.jsonObjectList = jsonObjectList;
	}
	
	
	
}
