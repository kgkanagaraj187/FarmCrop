package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.util.ESESystem;
import com.ese.util.Base64Util;
import com.google.zxing.BinaryBitmap;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICatalogueService;
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
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;


@Component
public class TraceabilityDataDownload implements ITxnAdapter {
	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IFarmerService farmerService;

	@Autowired
	private ILocationService locationService;

	@Autowired
	private IPreferencesService preferncesService;
	
	@Autowired
	private ICatalogueService catalogueService;

	private Map<String, String> seasonMap = new LinkedHashMap<String, String>();
	private Map<String, String> warehouseMap = new LinkedHashMap<String, String>();
	private Map<String, String> samithiMap = new LinkedHashMap<String, String>();
	private Map<String, String> masterDataMap = new LinkedHashMap<String, String>();
	protected HttpServletResponse response;
	private String farmerImageByteString;
	
	Map resp = new HashMap();

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String id = (String) reqData.get(TxnEnrollmentProperties.ID);
		FarmerTraceability ft=farmerService.findTraceabilityDataById(Long.parseLong(id));
		JSONArray farmArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Farmer farmer = farmerService.findFarmerByID(Long.valueOf(ft.getFarmerId()),head.getTenantId());
		//jsonObject.put("farmerLoc", farmer.getLatitude()+"~"+farmer.getLongitude());	
			jsonObject.put("FarmerName", ft.getFarmerName()+"~"+farmer.getLatitude()+"~"+farmer.getLongitude());
			jsonObject.put("VillageName", locationService.findVillageByIdAndTenant(Long.parseLong(ft.getVillage()),head.getTenantId()).getName());
			jsonObject.put("ics", catalogueService.findCatalogueByCode(ft.getIcs(),head.getTenantId()).getName());
			jsonObject.put("shg", locationService.findWarehouseByIdAndTenantId(Long.parseLong(ft.getShg()),head.getTenantId()).getName());
			jsonObject.put("lotNo", ft.getLotNo());
			
			Warehouse procCenter=locationService.findWarehouseByIdAndTenantId(Long.parseLong(ft.getProcurementCenter()),head.getTenantId());
			jsonObject.put("procurementCenter", procCenter.getName()+"~"+procCenter.getLatitude()+"~"+procCenter.getLongitude());
			if(ft.getGinning()!=null && !StringUtil.isEmpty(ft.getGinning())){
			Warehouse ginner=locationService.findWarehouseByIdAndTenantId(Long.parseLong(ft.getGinning()),head.getTenantId());
			jsonObject.put("ginning", ginner.getName()+"~"+ginner.getLatitude()+"~"+ginner.getLongitude());
			}
			if(ft.getSpinning()!=null && !StringUtil.isEmpty(ft.getSpinning())){
			Warehouse spinner=locationService.findWarehouseByIdAndTenantId(Long.parseLong(ft.getSpinning()),head.getTenantId());
			jsonObject.put("spinning", spinner.getName()+"~"+spinner.getLatitude()+"~"+spinner.getLongitude());
			}
			
		return jsonObject;
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
	
	
}
