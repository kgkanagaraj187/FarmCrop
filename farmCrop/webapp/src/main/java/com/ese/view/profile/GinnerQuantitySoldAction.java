package com.ese.view.profile;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.GinnerQuantitySold;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class GinnerQuantitySoldAction extends SwitchValidatorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8848856278882482232L;

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final Logger logger = Logger.getLogger(GinnerQuantitySoldAction.class);

	private IFarmerService farmerService;
	private String ginnerName;
	private String ginnerQty;
	private String id;
	private String ginnerAddress;
	DecimalFormat formatter = new DecimalFormat("0.00");
	public String data() throws Exception {
		@SuppressWarnings("unused")
		Map<String, String> searchRecord = getJQGridRequestParam();
		GinnerQuantitySold filter = new GinnerQuantitySold();
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))) {
			filter.setSeasonCode(searchRecord.get("seasonCode"));
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		GinnerQuantitySold ginnerQtySold = (GinnerQuantitySold) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		HarvestSeason harvestSeason = clientService.findSeasonByCode(ginnerQtySold.getSeasonCode());
		if (StringUtil.isEmpty(getBranchId())) {
			rows.add(ginnerQtySold.getBranchId());
		}
		rows.add(!ObjectUtil.isEmpty(harvestSeason) ? harvestSeason.getName() : "");

		rows.add(ginnerQtySold.getName());
		rows.add(formatter.format(ginnerQtySold.getQuantity()));
		rows.add(ginnerQtySold.getAddress());
		jsonObject.put("id", ginnerQtySold.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public void populateUpdate() {

		if (!StringUtil.isEmpty(getId())) {
			GinnerQuantitySold ginnerQtySold = farmerService.findGinnerQtySoldById(Long.valueOf(getId()));
			if (!StringUtil.isEmpty(ginnerQtySold)) {
				ginnerQtySold.setName(ginnerName);
				ginnerQtySold.setQuantity(Double.valueOf(ginnerQty));
				ginnerQtySold.setAddress(ginnerAddress);
				farmerService.editGinnerQtySold(ginnerQtySold);

				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
		}
	}

	public void populateDelete() {
		if (!StringUtil.isEmpty(getId())) {
			GinnerQuantitySold ginnerQtySold = farmerService.findGinnerQtySoldById(Long.valueOf(getId()));
			farmerService.removeGinnerQtySold(ginnerQtySold);
			getJsonObject().put("msg", getText("msg.deleted"));
			getJsonObject().put("title", getText("title.success"));
		}
		sendAjaxResponse(getJsonObject());

	}

	public void populateSave() {
		String seasonCode = clientService.findCurrentSeasonCode();
		GinnerQuantitySold ginnerQuantitySold = new GinnerQuantitySold();
		ginnerQuantitySold.setName(ginnerName);
		ginnerQuantitySold.setQuantity(Double.valueOf(ginnerQty));
		ginnerQuantitySold.setBranchId(getBranchId());
		ginnerQuantitySold.setSeasonCode(seasonCode);
		ginnerQuantitySold.setCreatedDate(new Date());
		ginnerQuantitySold.setAddress(ginnerAddress);
		farmerService.saveGinnerQtySold(ginnerQuantitySold);
		getJsonObject().put("msg", getText("msg.added"));
		getJsonObject().put("title", getText("title.success"));
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

	public String getGinnerName() {
		return ginnerName;
	}

	public void setGinnerName(String ginnerName) {
		this.ginnerName = ginnerName;
	}

	public String getGinnerQty() {
		return ginnerQty;
	}

	public void setGinnerQty(String ginnerQty) {
		this.ginnerQty = ginnerQty;
	}

	public String getGinnerAddress() {
		return ginnerAddress;
	}

	public void setGinnerAddress(String ginnerAddress) {
		this.ginnerAddress = ginnerAddress;
	}

}
