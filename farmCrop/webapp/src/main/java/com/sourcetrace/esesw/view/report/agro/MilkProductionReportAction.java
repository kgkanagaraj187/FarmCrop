package com.sourcetrace.esesw.view.report.agro;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.IExporter;

public class MilkProductionReportAction  extends BaseReportAction implements IExporter
{
	
	
	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 7154920881882030180L;
	 private CowInspection filter;
	 private CowInspection cowInspection;
	 private List<CowInspection> cowProductionList;
	public CowInspection getFilter() {
		return filter;
	}


	public void setFilter(CowInspection filter) {
		this.filter = filter;
	}

	private Map<String, String> fields = new LinkedHashMap<String, String>();
	private IFarmerService farmerService;

	public IFarmerService getFarmerService() {
		return farmerService;
	}


	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}


	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String list()
	{
		 Calendar currentDate = Calendar.getInstance();
	     Calendar cal = (Calendar) currentDate.clone();
	     cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
	     DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
	     super.startDate = df.format(cal.getTime());
	     super.endDate = df.format(currentDate.getTime());
	    
	    request.setAttribute(HEADING, getText(LIST));
	    setFilter(cowInspection);
		return LIST;
	}
	
	public String data() throws Exception {
		super.filter = this.filter;
		Map data = readData("cowProduction");
		return sendJSONResponse(data);
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj)
	{
		
		Object[] milkProd = (Object[]) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(branchesMap.get(milkProd[1]));
		}
		if(!ObjectUtil.isEmpty(milkProd[0]))
		{
			
			Cow cow=farmerService.findByCowId(milkProd[0].toString());
			rows.add(!StringUtil.isEmpty(cow.getCowId())?cow.getCowId():"");
			rows.add(!ObjectUtil.isEmpty(cow.getResearchStation())?cow.getResearchStation().getName():"");
			rows.add(!ObjectUtil.isEmpty(cow.getFarm())?cow.getFarm().getFarmer().getFirstName():"");
		}
		rows.add(!ObjectUtil.isEmpty(milkProd[3])? milkProd[3].toString(): "");
		rows.add(!ObjectUtil.isEmpty(milkProd[4])? CurrencyUtil.getDecimalFormat(Double.valueOf(milkProd[4].toString()), "##.00"): "");
		rows.add(!ObjectUtil.isEmpty(milkProd[5])? CurrencyUtil.getDecimalFormat(Double.valueOf(milkProd[5].toString()), "##.00"): "");
		rows.add(!ObjectUtil.isEmpty(milkProd[6])? CurrencyUtil.getDecimalFormat(Double.valueOf(milkProd[6].toString()), "##.00"): "");
		rows.add(!ObjectUtil.isEmpty(milkProd[7])? CurrencyUtil.getDecimalFormat(Double.valueOf(milkProd[7].toString()), "##.00"): "");
		jsonObject.put("id", milkProd[0]);
	    jsonObject.put("cell", rows);
		return jsonObject;
	}
	
	
	public String detail()
	{
		if(!StringUtil.isEmpty(id))
		{
			cowProductionList=farmerService.findByCowList(id);
			setCowInspection(cowProductionList.get(0));
		}
		
		return DETAIL;
	}
	    
	
	public Map<String,String> getFarmerList()
	{
		Map<String,String> farmerMap=new HashMap<>();
		List<Object[]> farmerList=farmerService.findByCowInspFarmer();
		for(Object[] object:farmerList)
		{
			farmerMap.put(String.valueOf(object[0]), String.valueOf(object[1]));
		}
		return farmerMap;
	}
	
	
	
	public Map<String,String> getResearchStationList()
	{
		Map<String,String> researchStatMap=new HashMap<>();
		List<Object[]> researchStatList=farmerService.findByResearchStation();
		for(Object[] object:researchStatList)
		{
			researchStatMap.put(String.valueOf(object[0]), String.valueOf(object[1]));
		}
		return researchStatMap;
	}
	
	
	public Map<String,String> getCowList()
	{
		try{
		Map<String,String> cowMap=new HashMap<>();
		List<String> cowListTemp=farmerService.findByCowList();
		for(String object:cowListTemp)
		{
			cowMap.put(object, object);
		}
		return cowMap;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
		
			
	public Map<String, String> getFields() {
		fields.put("1",getText("cowId"));
		fields.put("2",getText("cow.researchStationName"));
		fields.put("3",getText("farmerName"));
		if (ObjectUtil.isEmpty(getBranchId())) {
			fields.put("4",getText("app.branch"));
		}
		return fields;
	}
	
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}


	public CowInspection getCowInspection() {
		return cowInspection;
	}


	public void setCowInspection(CowInspection cowInspection) {
		this.cowInspection = cowInspection;
	}


	public List<CowInspection> getCowProductionList() {
		return cowProductionList;
	}


	public void setCowProductionList(List<CowInspection> cowProductionList) {
		this.cowProductionList = cowProductionList;
	}


}
