package com.sourcetrace.esesw.view.report.agro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.WebTransactionAction;

import net.glxn.qrgen.QRCode;

public class FarmerFeedBackServiceAction extends WebTransactionAction {

	private static final long serialVersionUID = 1L;
	private IFarmerService farmerService;
	private ILocationService locationService;
	@Autowired
	private ICertificationService certificationService;

	private String id;
	private String selectedVillage;
	private String selectedFarmer;
	private String feedBackDate;
	private String selectedGrp;

	List<String> villageList = new ArrayList<String>();
	List<String> farmerList = new ArrayList<String>();
	List<String> grpList = new ArrayList<String>();
	public FarmerFeedbackEntity filter;

	DecimalFormat formatter = new DecimalFormat("#.##");
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	

	/**
	 * Gets the village list.
	 * 
	 * @return the village list
	 */
	public List<String> getVillageList() {

		List<Village> listVillage = locationService.listVillage();
		if (!ObjectUtil.isListEmpty(listVillage)) {
			for (Village village : listVillage) {
				villageList.add(village.getName() + "-" + village.getCode());
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

		
			
			List<Village> listVillage = locationService.listVillage();
			if (!ObjectUtil.isListEmpty(listVillage)) {
				for (Village village : listVillage) {
					villageArr.add(getJSONObject(village.getName() + "-" + village.getCode(),
							village.getName() + "-" + village.getCode()));
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

		if (!ObjectUtil.isEmpty(selectedGrp)
				&& (!StringUtil.isEmpty(selectedGrp))) {
			String[] villageCode = selectedGrp.split("-");
			List<Object[]> listFarmer = farmerService
					.listFarmerBySamithi(Long.valueOf(villageCode[0].toString()));
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerArr.add(getJSONObject(
							farmer[1].toString().trim() + " " + farmer[2].toString().trim() + "-"
									+ farmer[0].toString().trim(),
							farmer[1].toString().trim() + " " + farmer[2].toString().trim() + "-"
									+ farmer[0].toString().trim()));
				}
			}
		}
		sendAjaxResponse(farmerArr);
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
			List<Object[]> listFarmer = farmerService.listFarmerCodeIdNameByVillageCode(villageCode[1].toString().trim());
			if (!ObjectUtil.isListEmpty(listFarmer)) {
				for (Object[] farmer : listFarmer) {
					farmerList.add(farmer[2].toString().trim() + " " + farmer[3].toString().trim() + "-"
							+ farmer[0].toString().trim());
				}
			}
		}
		return farmerList;
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
	 * Sets the village list.
	 * 
	 * @param villageList
	 *            the new village list
	 */
	public void setVillageList(List<String> villageList) {

		this.villageList = villageList;
	}

	public String create() throws Exception {
		
		
		
		if (!StringUtil.isEmpty(selectedFarmer) && !ObjectUtil.isEmpty(filter)) {
			//FarmerFeedbackEntity entity = new FarmerFeedbackEntity();
			
			filter.setAnsweredDate(DateUtil.convertStringToDate(feedBackDate, getGeneralDateFormat()));
			filter.setFarmerId(selectedFarmer);
			String cityCode[] = selectedFarmer.split("-");
			Farmer farmer = farmerService.findFarmerByFarmerId(cityCode[1].toString().trim());
			if(farmer!=null){
			filter.setFarmerName(farmer.getFirstName()+" "+farmer.getLastName());
			filter.setVillage(farmer.getVillage());
			filter.setWarehouse(farmer.getSamithi());			
			certificationService.addFarmerFeedback(filter);
			selectedFarmer=null;
			selectedGrp=null;
			selectedVillage=null;
			feedBackDate=DateUtil.convertDateToString(new Date(),  getGeneralDateFormat());
			filter=new FarmerFeedbackEntity();
			}
			
		}
		return INPUT;

	}

	
	
	public List<String> getGrpList() {
		List<Warehouse> grp = locationService.listOfSamithi();
		if(!StringUtil.isEmpty(grp)){
			for(Warehouse warehouse : grp){
				grpList.add(warehouse.getId()+"-"+warehouse.getName());
			}
			
		}
		
		return grpList;
	}

	public void setGrpList(List<String> grpList) {
		this.grpList = grpList;
	}

	public String getFeedBackDate() {
		return feedBackDate;
	}

	public void setFeedBackDate(String feedBackDate) {
		this.feedBackDate = feedBackDate;
	}

	public FarmerFeedbackEntity getFilter() {
		return filter;
	}

	public void setFilter(FarmerFeedbackEntity filter) {
		this.filter = filter;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public String getSelectedGrp() {
		return selectedGrp;
	}

	public void setSelectedGrp(String selectedGrp) {
		this.selectedGrp = selectedGrp;
	}

	

}
