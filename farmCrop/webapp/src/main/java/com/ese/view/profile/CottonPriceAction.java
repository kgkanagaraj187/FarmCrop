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
import com.sourcetrace.esesw.entity.profile.CottonPrice;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class CottonPriceAction extends SwitchValidatorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4096590816038691686L;

	private static final Logger logger = Logger.getLogger(CottonPriceAction.class);
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;
	private String id;
	private String stapleLength;
	private String msp;
	private String cottonPriceDatas;
	private String seasonCode;

	public String data() throws Exception {
		Map<String, String> searchRecord = getJQGridRequestParam();
		CottonPrice filter = new CottonPrice();
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId"));
		}

		if (!StringUtil.isEmpty(searchRecord.get("seasonCode"))) {
			filter.setSeasonCode(searchRecord.get("seasonCode"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("stapleLength"))) {
			filter.setStapleLength(searchRecord.get("stapleLength"));
		}
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return sendJQGridJSONResponse(data);

	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		CottonPrice productPrice = (CottonPrice) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		HarvestSeason harvestSeason = clientService.findSeasonByCode(productPrice.getSeasonCode());
		if (StringUtil.isEmpty(getBranchId())) {
			rows.add(productPrice.getBranchId());
		}
		rows.add(!ObjectUtil.isEmpty(harvestSeason) ? harvestSeason.getName() : "");
		rows.add(getText("stapleLenEng"+getCatlogueValueByCode(productPrice.getStapleLength()).getDispName()));
		//rows.add(String.valueOf(productPrice.getPrice()));

		rows.add(String.valueOf(productPrice.getMsp()));
		jsonObject.put("id", productPrice.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	public void populateUpdate() {

		if (!StringUtil.isEmpty(getId())) {

			CottonPrice productPrice = farmerService.findProdPriceById(Long.valueOf(getId()));
			if (!StringUtil.isEmpty(productPrice)) {
			//	productPrice.setPrice(Double.valueOf(price));
				productPrice.setMsp(msp);
				productPrice.setUpdatedDate(new Date());
				farmerService.editProdPrice(productPrice);

				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));

			}

			sendAjaxResponse(getJsonObject());

		}
	}

	@SuppressWarnings("unchecked")
	public String getStapleLengthFilter() {
		StringBuffer season = new StringBuffer();
		season.append(":").append(FILTER_ALL).append(";");
		List<Object[]> seasonList = catalogueService
				.findCatalogueCodeAndDisNameByType(Integer.valueOf(getText("stapleLength")));

		for (Object[] obj : seasonList) {
			season.append(obj[0].toString()).append(":").append(getText("stapleLenEng"+obj[2].toString())).append(";");

		}
		String data = season.toString();
		return data.substring(0, data.length() - 1);

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

	public void populateSavePrice() {

		CottonPrice cottonPrice = new CottonPrice();

		CottonPrice coPrice = farmerService.findStapleByCottonPrice(stapleLength,seasonCode);
		if (ObjectUtil.isEmpty(coPrice)) {
			cottonPrice.setSeasonCode(seasonCode);
			cottonPrice.setMsp(msp);
			cottonPrice.setStapleLength(stapleLength);
			cottonPrice.setCreatedDate(new Date());
			cottonPrice.setUpdatedDate(new Date());
			cottonPrice.setBranchId(getBranchId());
			farmerService.saveCottonPrice(cottonPrice);
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
		} else {
			getJsonObject().put("msg", getText("stapleLengthExists"));
			getJsonObject().put("title", getText("title.error"));

		}

		sendAjaxResponse(getJsonObject());

	}

	public void populateDelete() {
		if (!StringUtil.isEmpty(getId())) {
			CottonPrice cottonPrice = farmerService.findProdPriceById(Long.valueOf(getId()));
			farmerService.removeCottonPrice(cottonPrice);
			getJsonObject().put("msg", getText("msg.deleted"));
			getJsonObject().put("title", getText("title.success"));
		}
		sendAjaxResponse(getJsonObject());

	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStapleLength() {
		return stapleLength;
	}

	public void setStapleLength(String stapleLength) {
		this.stapleLength = stapleLength;
	}


	public String getMsp() {
		return msp;
	}

	public void setMsp(String msp) {
		this.msp = msp;
	}

	public String getCottonPriceDatas() {
		return cottonPriceDatas;
	}

	public void setCottonPriceDatas(String cottonPriceDatas) {
		this.cottonPriceDatas = cottonPriceDatas;
	}

	public String List() {
		return LIST;
	}

	public Map<String, String> getStapleLengthList() {
		Map<String, String> warehouseMap = new LinkedHashMap<>();

		List<Object[]> stapleList = catalogueService
				.findCatalogueCodeAndDisNameByType(Integer.valueOf(getText("stapleLength")));

		if (!ObjectUtil.isEmpty(stapleList)) {
			stapleList.forEach(obj -> {
				warehouseMap.put(obj[0].toString(), getText("stapleLenEng"+obj[2].toString()));
			});
		}
		return warehouseMap;

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

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}
}
