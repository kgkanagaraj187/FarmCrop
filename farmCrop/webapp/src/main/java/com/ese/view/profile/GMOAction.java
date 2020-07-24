package com.ese.view.profile;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.GMO;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class GMOAction extends SwitchValidatorAction {

	/**
	* 
	*/
	private static final long serialVersionUID = -825381636802882613L;
	private static final Logger logger = Logger.getLogger(GMOAction.class);

	private IFarmerService farmerService;
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;

	private String id;
	private String noOfSamples;
	private String contaminPercent;
	private String noOfPositive;
	private String gmoDatas;

	public String data() throws Exception {
		@SuppressWarnings("unused")
		Map<String, String> searchRecord = getJQGridRequestParam();
		GMO filter = new GMO();
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))) {
			filter.setSeasonCode(searchRecord.get("seasonCode"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("type"))) {
			filter.setType(searchRecord.get("type"));
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return sendJQGridJSONResponse(data);
	}

	public String list() {
		return LIST;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		GMO gmo = (GMO) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		HarvestSeason harvestSeason = clientService.findSeasonByCode(gmo.getSeasonCode());
		if (StringUtil.isEmpty(getBranchId())) {
			rows.add(gmo.getBranchId());
		}
		rows.add(!ObjectUtil.isEmpty(harvestSeason) ? harvestSeason.getName() : "");
		rows.add(String.valueOf(gmo.getNoOfSamples()));
		rows.add(String.valueOf(gmo.getNoOfPositive()));

		rows.add(String.valueOf(gmo.getContaminationPercentage()));
		rows.add(getText("gmoType" + gmo.getType()));
		jsonObject.put("id", gmo.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public void populateGmoUpdate() {

		if (!StringUtil.isEmpty(getId())) {
			if (Double.valueOf(noOfPositive) > Double.valueOf(noOfSamples)) {
				getJsonObject().put("msg", getText("lessthanSamples"));
				getJsonObject().put("title", getText("title.error"));
			} else {
				GMO gmo = farmerService.findGMOById(Long.valueOf(getId()));
				if (!StringUtil.isEmpty(gmo)) {
					gmo.setNoOfPositive(Double.valueOf(noOfPositive));
					gmo.setNoOfSamples(Double.valueOf(noOfSamples));
					gmo.setContaminationPercentage(Double.valueOf(contaminPercent));
					farmerService.editGMO(gmo);

					getJsonObject().put("msg", getText("msg.updated"));
					getJsonObject().put("title", getText("title.success"));

				}
			}

			sendAjaxResponse(getJsonObject());

		}
	}

	public void populateDelete() {
		if (!StringUtil.isEmpty(getId())) {
			GMO gmo = farmerService.findGMOById(Long.valueOf(getId()));
			farmerService.removeGMO(gmo);
			getJsonObject().put("msg", getText("msg.deleted"));
			getJsonObject().put("title", getText("title.success"));
		}
		sendAjaxResponse(getJsonObject());

	}

	public void populateSaveGMO() {

		if (gmoDatas.length() > 0) {
			// String seasonCode = clientService.findCurrentSeasonCode();
			String datas[] = gmoDatas.split("@@@");
			/*
			 * int maxId = farmerService.findMaxTypeId(); ++maxId;
			 */
			for (int i = 0; i < datas.length; i++) {
				GMO gmo = new GMO();

				String data[] = datas[i].split("###");
				GMO gmoTemp = farmerService.findGMOType(data[3], data[4]);
				if (ObjectUtil.isEmpty(gmoTemp)) {
					gmo.setNoOfSamples(Double.valueOf(data[0]));
					gmo.setNoOfPositive(Double.valueOf(data[1]));
					gmo.setType(data[3]);
					gmo.setSeasonCode(data[4]);
					gmo.setCreatedDate(new Date());
					gmo.setBranchId(getBranchId());
					gmo.setContaminationPercentage(Double.valueOf(data[2]));
					farmerService.saveGMO(gmo);
					getJsonObject().put("msg", getText("msg.added"));
					getJsonObject().put("title", getText("title.success"));

				} else {
					getJsonObject().put("msg", getText("gmo.alreadyExists"));
					getJsonObject().put("title", getText("title.error"));
				}

			}
		}
		sendAjaxResponse(getJsonObject());

	}

	public String getSeasonFilter() {

		StringBuffer season = new StringBuffer();
		season.append(":").append(FILTER_ALL).append(";");
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();

		for (Object[] obj : seasonList) {
			season.append(obj[0].toString()).append(":").append(obj[1].toString()).append(";");

		}
		String data = season.toString();
		return data.substring(0, data.length() - 1);

	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub

		return null;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoOfSamples() {
		return noOfSamples;
	}

	public void setNoOfSamples(String noOfSamples) {
		this.noOfSamples = noOfSamples;
	}

	public String getContaminPercent() {
		return contaminPercent;
	}

	public void setContaminPercent(String contaminPercent) {
		this.contaminPercent = contaminPercent;
	}

	public IProductDistributionService getProductDistributionService() {
		return productDistributionService;
	}

	public void setProductDistributionService(IProductDistributionService productDistributionService) {
		this.productDistributionService = productDistributionService;
	}

	public String getNoOfPositive() {
		return noOfPositive;
	}

	public void setNoOfPositive(String noOfPositive) {
		this.noOfPositive = noOfPositive;
	}

	public String getGmoDatas() {
		return gmoDatas;
	}

	public void setGmoDatas(String gmoDatas) {
		this.gmoDatas = gmoDatas;
	}

	public String getCurrentSeason() {

		String val = "";
		return val = clientService.findCurrentSeasonCodeByBranchId(getBranchId());
	}

	public Map<String, String> getSeasonList() {

		Map<String, String> seasonMap = new LinkedHashMap<String, String>();
		List<Object[]> seasonList = farmerService.listSeasonCodeAndName();
		for (Object[] obj : seasonList) {

			seasonMap.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
		}
		return seasonMap;

	}

}
