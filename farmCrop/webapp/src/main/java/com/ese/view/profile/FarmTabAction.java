package com.ese.view.profile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmElement;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class FarmTabAction extends SwitchValidatorAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(FarmTabAction.class);
	public static final int SELECT = -1;
	public static final int OTHER = 99;

	private String id;
	private String farmId;
	private int selectedMachinaryItem;
	private int selectedPolyItem;
	private Farm farm;
	private FarmElement farmElement;
	private IFarmerService farmerService;

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	List<FarmElement> farmElementList = new ArrayList<FarmElement>();
	private String farmElementJsonString = "";
	private JsonDataObject jsonDataObject = new JsonDataObject();
	private Gson gson = new Gson();
	JSONObject jsonObject = new JSONObject();
	private String templateId;
	
	private ICatalogueService catalogueService;

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {
		return farmElement;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {

		return LIST;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {
		return "farmDetail";
	}

	public String saveInventory() throws Exception {

		return null;

	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Gets the farm.
	 * 
	 * @return the farm
	 */
	public Farm getFarm() {

		return farm;
	}

	/**
	 * Sets the farm.
	 * 
	 * @param farm
	 *            the new farm
	 */
	public void setFarm(Farm farm) {

		this.farm = farm;
	}

	/**
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
	}

	/**
	 * Sets the farm id.
	 * 
	 * @param farmId
	 *            the new farm id
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/**
	 * Gets the farm inventory list.
	 * 
	 * @return the farm inventory list
	 */
	public List<FarmElement> getFarmElementList() {

		return farmElementList;
	}

	/**
	 * Sets the farm inventory list.
	 * 
	 * @param farmInventoryList
	 *            the new farm inventory list
	 */
	public void setFarmElementList(List<FarmElement> farmElementList) {

		this.farmElementList = farmElementList;
	}

	public void populateInventry() throws Exception {

		String msg = "";
		Type listType1 = new TypeToken<List<FarmElement>>() {
		}.getType();
		System.out.println("farmElementJsonString" + farmElementJsonString + "----" + farmElementJsonString);
		List<FarmElement> farmElementList = new Gson().fromJson(farmElementJsonString, listType1);
		if (!ObjectUtil.isEmpty(farmElementList)) {
			for (int j = 0; j < farmElementList.size(); j++) {
				FarmElement element = new FarmElement();
				Farm farm = farmerService.findFarmById(Long.valueOf(farmId));
				FarmElement fm = farmerService.findFarmElementItem(Long.valueOf(farmId),
						farmElementList.get(j).getMachStr());
				if (fm != null && !ObjectUtil.isEmpty(fm)) {
					String val = String.valueOf(
							Integer.valueOf(fm.getCount()) + Integer.valueOf(farmElementList.get(j).getMachCount()));
					fm.setCount(val);
					farmerService.updateFarmElement(fm);
				} else {
					String invString = farmElementList.get(j).getMachStr();
					FarmCatalogue cat = catalogueService.findCatalogueByCode(invString);
					element.setCatalogueId(cat);
					element.setCount(farmElementList.get(j).getMachCount());
					element.setFarm(farm);
					element.setCatalogueType("5");
					farmerService.addFarmElement(element);
				}
				FarmElement fm1 = farmerService.findFarmElementItem(Long.valueOf(farmId),
						farmElementList.get(j).getHouseStr());
				if (fm1 != null && !ObjectUtil.isEmpty(fm1)) {
					String val = String.valueOf(
							Integer.valueOf(fm1.getCount()) + Integer.valueOf(farmElementList.get(j).getHouseCnt()));
					fm1.setCount(val);
					farmerService.updateFarmElement(fm1);
				} else {
					String polyString = farmElementList.get(j).getHouseStr();
					FarmCatalogue catVal = catalogueService.findCatalogueByCode(polyString);
					element.setCatalogueId(catVal);
					element.setCount(farmElementList.get(j).getHouseCnt());
					element.setFarm(farm);
					element.setCatalogueType("6");
					farmerService.addFarmElement(element);
				}
			}
			jsonObject.put("msg", "success");
		}

		sendAjaxResponse(jsonObject);
	}

	/**
	 * Gets the farm inven json string.
	 * 
	 * @return the farm inven json string
	 */
	public String getFarmElementJsonString() {

		return farmElementJsonString;
	}

	/**
	 * Sets the farm inven json string.
	 * 
	 * @param farmInvenJsonString
	 *            the new farm inven json string
	 */
	public void setFarmElementJsonString(String farmElementJsonString) {

		this.farmElementJsonString = farmElementJsonString;
	}

	public String populateInventryList() throws Exception {
		JSONArray array = new JSONArray();
		String machType = "5";
		List<FarmElement> element = farmerService.listMachinaryList(Long.valueOf(farmId), machType);

		for (FarmElement fe : element) {
			// Catalogue cat =
			// catalogueService.findCatalogueById(Long.valueOf(fm.getInventoryItem()));
			JSONObject obj = new JSONObject();
			obj.put("id", fe.getId());
			if (fe.getCatalogueId() != null) {
				obj.put("item", fe.getCatalogueId().getName());
			} else {
				obj.put("item", "");
			}

			if (fe.getCount() != null) {
				obj.put("ct", fe.getCount());
			} else {
				obj.put("ct", "");
			}
			array.add(obj);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("data", array);
		sendResponse(mainObj);
		return null;
	}

	public String populatePolyList() throws Exception {
		JSONArray array = new JSONArray();
		String machType = "6";
		List<FarmElement> element = farmerService.listMachinaryList(Long.valueOf(farmId), machType);

		for (FarmElement fe : element) {
			// Catalogue cat =
			// catalogueService.findCatalogueById(Long.valueOf(fm.getInventoryItem()));
			JSONObject obj = new JSONObject();
			obj.put("id1", fe.getId());
			if (fe.getCatalogueId() != null) {
				obj.put("item1", fe.getCatalogueId().getName());
			} else {
				obj.put("item1", "");
			}

			if (fe.getCount() != null) {
				obj.put("ct1", fe.getCount());
			} else {
				obj.put("ct1", "");
			}
			array.add(obj);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("data", array);
		sendResponse(mainObj);
		return null;
	}

	/**
	 * Gets the json data object.
	 * 
	 * @return the json data object
	 */
	public JsonDataObject getJsonDataObject() {

		return jsonDataObject;
	}

	/**
	 * Sets the json data object.
	 * 
	 * @param jsonDataObject
	 *            the new json data object
	 */
	public void setJsonDataObject(JsonDataObject jsonDataObject) {

		this.jsonDataObject = jsonDataObject;
	}

	/**
	 * Gets the gson.
	 * 
	 * @return the gson
	 */
	public Gson getGson() {

		return gson;
	}

	/**
	 * Sets the gson.
	 * 
	 * @param gson
	 *            the new gson
	 */
	public void setGson(Gson gson) {

		this.gson = gson;
	}

	public String deleteMachinaryItem() {

		FarmElement farmElement = farmerService.findFarmElementById(Long.valueOf(templateId));
		farmerService.removeFarmElement(farmElement);
		try {
			JSONObject js = new JSONObject();
			js.put("msg", getText("msg.removed"));
			sendAjaxResponse(js);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deletePolyItem() {

		FarmElement farmElement = farmerService.findFarmElementById(Long.valueOf(templateId));
		farmerService.removeFarmElement(farmElement);
		try {
			JSONObject js = new JSONObject();
			js.put("msg", getText("msg.removed"));
			sendAjaxResponse(js);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTemplateId() {

		return templateId;
	}

	public void setTemplateId(String templateId) {

		this.templateId = templateId;
	}

	public ICatalogueService getCatalogueService() {

		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {

		this.catalogueService = catalogueService;
	}

	public int getSelectedMachinaryItem() {
		return selectedMachinaryItem;
	}

	public void setSelectedMachinaryItem(int selectedMachinaryItem) {
		this.selectedMachinaryItem = selectedMachinaryItem;
	}

	public int getSelectedPolyItem() {
		return selectedPolyItem;
	}

	public void setSelectedPolyItem(int selectedPolyItem) {
		this.selectedPolyItem = selectedPolyItem;
	}

	public FarmElement getFarmElement() {
		return farmElement;
	}

	public void setFarmElement(FarmElement farmElement) {
		this.farmElement = farmElement;
	}

}
