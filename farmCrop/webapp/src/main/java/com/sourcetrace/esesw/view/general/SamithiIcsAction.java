package com.sourcetrace.esesw.view.general;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.SamithiIcs;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IExporter;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.BaseReportAction;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class SamithiIcsAction extends BaseReportAction implements IExporter {
	
	protected String command;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private Map<String, String> seasonList = new LinkedHashMap<String, String>();
	private SamithiIcs samithi;
	private File farmerImage;
	private String samithiId;
	private String coOperativeId;
	private String date;
	private String fileName;
	private String samithiUniqueId;
	
	private SamithiIcs filter;
	public String create() throws Exception {

		if (samithi == null) {
			samithi = new SamithiIcs();

			command = "create";
			request.setAttribute(HEADING, getText(CREATE));
			return INPUT;
		} else {
			samithi.setImage(FileUtil.getBinaryFileContent(getFarmerImage()));
			samithi.setBranchId(getBranch());
			samithi.setDate(DateUtil.convertStringToDate(getDate(), getGeneralDateFormat()));
			samithi.setSeason(samithi.getSeason());
			samithi.setType(samithi.getType());
			samithi.setLandHolding(getFileName().toString());
			Warehouse warehouse = locationService.findSamithiById(Integer.valueOf(String.valueOf(request.getSession().getAttribute("samithiId"))));
			samithi.setWarehouse(warehouse);
			farmerService.save(samithi);
			setSamithiUniqueId(String.valueOf(warehouse.getId()));
		}
		return REDIRECT;
	}

	@SuppressWarnings("unchecked")
	public String data() throws Exception {
	filter = new SamithiIcs();
		if (!StringUtil.isEmpty(getSamithiId())) {
			request.getSession().setAttribute("samithiId", getSamithiId());
			setSamithiId(String.valueOf(request.getSession().getAttribute("samithiId")));
		}/* else if (!StringUtil.isEmpty(request.getSession().getAttribute("samithiId"))) {
			request.getSession().setAttribute("samithiId", request.getSession().getAttribute("samithiId"));
			setSamithiId(String.valueOf(request.getSession().getAttribute("samithiId")));
		}*/
		if (!StringUtil.isEmpty(samithiId)) {			
			
			Warehouse warehouse = locationService.findSamithiById(Long.valueOf(samithiId));
			filter.setWarehouse(warehouse);
			
		}
		

		super.filter = this.filter;
		Map data = readData();
		return sendJSONResponse(data);
	}
	
	 @SuppressWarnings("unchecked")
	    @Override
	    protected JSONObject toJSON(Object record) {

	        JSONObject jsonObject = new JSONObject();
	        JSONArray rows = new JSONArray();
	        if (record instanceof SamithiIcs) {
	        	SamithiIcs samithiIcs = (SamithiIcs) record;
	           // rows.add(samithiIcs.getIcsType());
	            rows.add(getLocaleProperty("ics" + samithiIcs.getType()));
	            HarvestSeason season=farmerService.findHarvestSeasonByCode(samithiIcs.getSeason());
	            rows.add(season.getName());
	            DateFormat genDate = new SimpleDateFormat(
						preferncesService.findPrefernceByName(ESESystem.GENERAL_DATE_FORMAT));
	            rows.add(String.valueOf(genDate.format(samithiIcs.getDate())));
	            rows.add(
	    				"<button class=\"fa fa-download\"\"aria-hidden=\"true\"\" onclick=\"popDownload('"
	    						+ samithiIcs.getId() + "')\"></button>");
	            jsonObject.put("id", samithiIcs.getId());
	            jsonObject.put("cell", rows);
	        }
			return jsonObject;
	 }
	public Map<String, String> getSeasonList() {

		List<Object[]> seasonLists = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonLists) {
			seasonList.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonList;

	}

	public Map<Integer, String> getIcsStatusList() {

		String[] icsStatus = getLocaleProperty("icsStatusList").split(",");
		for (int i = 0; i < icsStatus.length; i++)
			icsStatusList.put(i, icsStatus[i]);

		return icsStatusList;
	}

	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIcsStatusList(Map<Integer, String> icsStatusList) {
		this.icsStatusList = icsStatusList;
	}

	public void setSeasonList(Map<String, String> seasonList) {
		this.seasonList = seasonList;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public File getFarmerImage() {
		return farmerImage;
	}

	public void setFarmerImage(File farmerImage) {
		this.farmerImage = farmerImage;
	}

	public String getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(String samithiId) {
		this.samithiId = samithiId;
	}


	public SamithiIcs getSamithi() {
		return samithi;
	}

	public void setSamithi(SamithiIcs samithi) {
		this.samithi = samithi;
	}

	public String getCoOperativeId() {
		return coOperativeId;
	}

	public void setCoOperativeId(String coOperativeId) {
		this.coOperativeId = coOperativeId;
	}

	public String populateDownload() {

		try {
			SamithiIcs cy = farmerService.findSamithiIcsById(Long.valueOf(id));
			String fileName=cy.getLandHolding().replaceAll("\\s+","");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + fileName );
			response.getOutputStream().write(cy.getImage());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSamithiUniqueId() {
		return samithiUniqueId;
	}

	public void setSamithiUniqueId(String samithiUniqueId) {
		this.samithiUniqueId = samithiUniqueId;
	}


	
}
