package com.ese.view.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.util.Base64Util;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class CowAction extends SwitchValidatorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -816565105590819330L;
	private String researchStationId;
	Map<String, String> genderType = new LinkedHashMap<String, String>();
	private String dateOfBirth;
	private String serviceDate;
	private Cow cow;
	private String calfProdData;
	@Autowired
	private IFarmerService farmerService;
	private String id;
	private List<Calf> calfList;
	private String cowImageByteString;
	private String farmId;
	private String tabIndexz;
	public static final String RESEARCHSTATION_DETAIL = "researchStatDetail";
	public static final String FARM_DETAIL = "farmDetail";
	@Autowired
	private ICatalogueService catalogueService;
	private String catalougeValues;

	@Override
	public Object getData() {
		// TODO Auto-generated method stub

		genderType.put(Farmer.SEX_MALE, getText("MALE"));
		genderType.put(Farmer.SEX_FEMALE, getText("FEMALE"));

		return null;
	}

	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with

		Cow filter = new Cow();

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}

		if (!StringUtil.isEmpty(farmId)) {
			Farm farm = new Farm();
			farm.setId(Long.valueOf(farmId));
			filter.setFarm(farm);
			if (!ObjectUtil.isEmpty(farm.getFarmer())) {
				if (farm.getFarmer().getFarmerId() != null) {
					filter.setFarmerId(farm.getFarmer().getFarmerId());
				}
			}
		}

		if (!StringUtil.isEmpty(researchStationId)) {
			ResearchStation researchStation = new ResearchStation();
			researchStation.setId(Long.valueOf(researchStationId));
			;
			filter.setResearchStation(researchStation);
		}

		if (!StringUtil.isEmpty(searchRecord.get("cowId"))) {
			filter.setCowId(searchRecord.get("cowId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("tagNo"))) {
			filter.setTagNo(searchRecord.get("tagNo").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("lactationNo"))) {
			filter.setLactationNo(searchRecord.get("lactationNo").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("damId"))) {
			filter.setDamId(searchRecord.get("damId").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("genoType"))) {
			filter.setGenoType(searchRecord.get("genoType").trim());
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * To json.
	 * 
	 * @param record
	 *            the record
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object record) {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		if (record instanceof Cow) {

			Cow cow = (Cow) record;
			JSONArray rows = new JSONArray();

			rows.add(!StringUtil.isEmpty(cow.getCowId()) ? cow.getCowId() : "");
			rows.add(!StringUtil.isEmpty(cow.getTagNo()) ? cow.getTagNo() : "");
			rows.add(!StringUtil.isEmpty(cow.getLactationNo()) ? cow.getLactationNo() : "");
			/*
			 * rows.add(!StringUtil.isEmpty(cow.getDamId())?cow.getDamId():"");
			 * 
			 * rows.add(!StringUtil.isEmpty(cow.getGenoType())?cow.getGenoType()
			 * :"");
			 */

			jsonObject.put("id", cow.getId());
			jsonObject.put("cell", rows);
			return jsonObject;

		}
		return jsonObject;
	}

	public String create() {
		String result = null;
		command = CREATE;
		if (ObjectUtil.isEmpty(cow)) {

			result = CREATE;
		} else {

			if (!StringUtil.isEmpty(cow)) {

				Cow cowId = farmerService.findByCowId(cow.getCowId());
				if (!StringUtil.isEmpty(cowId)) {
					addActionError(getText("error.cowExists"));
					result = INPUT;
				} else {
					Set<Calf> calfSet = new HashSet<>();
					if (!StringUtil.isEmpty(dateOfBirth)) {
						cow.setDob(DateUtil.convertStringToDate(dateOfBirth, DateUtil.DATE_FORMAT));
					}
					cow.setCreatedDate(new Date());
					cow.setUpdatedDate(new Date());

					if (cow.getCowImage() != null) {
						cow.setPhoto(FileUtil.getBinaryFileContent(cow.getCowImage()));
						cow.setPhotoCaptureTime(new Date());
					}

					if (!StringUtil.isEmpty(calfProdData)) {
						String calfDataSpilt[] = calfProdData.split("@@@");
						for (int i = 0; i < calfDataSpilt.length; i++) {
							Calf calf = new Calf();
							String data[] = calfDataSpilt[i].split("###");

							calf.setCalfId(data[0]);
							calf.setBullId(data[1]);
							if (!StringUtil.isEmpty(data[2])) {
								calf.setServiceDate(DateUtil.convertStringToDate(data[2], DateUtil.DATE_FORMAT));
							}
							if (!StringUtil.isEmpty(data[3])) {
								calf.setLastDateCalving(DateUtil.convertStringToDate(data[3], DateUtil.DATE_FORMAT));
							}
							calf.setGender(!StringUtil.isEmpty(data[4]) ? data[4] : "");
							calf.setBirthWeight(!StringUtil.isEmpty(data[5]) ? Double.valueOf(data[5]) : 0.0);
							calf.setCalvingIntvalDays(!StringUtil.isEmpty(data[6]) ? Double.valueOf(data[6]) : 0.0);
							calf.setDeliveryProcess(data[7]);
							calfSet.add(calf);

						}
					}
					if (!StringUtil.isEmpty(researchStationId)) {
						ResearchStation researchStation = farmerService
								.findResearchStation(Long.valueOf(researchStationId));
						cow.setResearchStation(researchStation);
						cow.setElitType(Cow.ELITE_RESEARCH);
						result = RESEARCHSTATION_DETAIL;
					} else if (!StringUtil.isEmpty(farmId)) {
						Farm farm = farmerService.findFarmByfarmId(Long.valueOf(farmId));
						cow.setFarmerId(farm.getFarmer().getFarmerId());
						cow.setFarm(farm);
						cow.setElitType(Cow.ELITE_FARMER);
						result = FARM_DETAIL;
					}
					cow.setBranchId(getBranchId());
					cow.setCalfs(calfSet);
					farmerService.addCow(cow);

				}
			}

		}
		return result;
	}

	public String detail() {
		if (!StringUtil.isEmpty(this.id)) {
			calfList = new ArrayList<>();
			cow = farmerService.findCowByCowId(Long.valueOf(this.id));
			if (!ObjectUtil.isEmpty(cow)) {
				cow.setDamId(getCatalouge(cow.getDamId()));
				cow.setSireId(getCatalouge(cow.getSireId()));
				cow.setGenoType(getCatalouge(cow.getGenoType()));
				if (cow.getPhoto() != null) {
					setCowImageByteString(Base64Util.encoder(cow.getPhoto()));
				}
				List<Calf> calfListTemp = farmerService.listOfCalfs(cow.getId());
				calfListTemp.stream().filter(calf -> !ObjectUtil.isEmpty(calf)).forEach(calf -> {

					calf.setBullId(getCatalouge(calf.getBullId()));
					calf.setDeliveryProcess(getCatalouge(calf.getDeliveryProcess()));
					calfList.add(calf);
					// setFeedTotalAmt(CurrencyUtil.getDecimalFormat(feedTot,
					// "#.##"));
				});
			}

		}

		return DETAIL;
	}

	public String update() {
		String result = null;
		command = UPDATE;
		if (!StringUtil.isEmpty(id)) {
			calfList = new ArrayList<>();
			cow = farmerService.findCowByCowId(Long.valueOf(this.id));
			cow.setCowImageExist("0");
			if (cow.getPhoto() != null) {
				setCowImageByteString(Base64Util.encoder(cow.getPhoto()));
				cow.setCowImageExist("1");
			}
			if (!StringUtil.isEmpty(cow.getDob())) {
				dateOfBirth = DateUtil.convertDateToString(cow.getDob(), DateUtil.DATE_FORMAT);
			}

			List<Calf> calfListTemp = farmerService.listOfCalfs(cow.getId());
			calfListTemp.stream().filter(calf -> !ObjectUtil.isEmpty(calf)).forEach(calf -> {

				calf.setBullIdVal(getCatalouge(calf.getBullId()));
				calf.setDeliveryProcessVal(getCatalouge(calf.getDeliveryProcess()));
				calfList.add(calf);
				// setFeedTotalAmt(CurrencyUtil.getDecimalFormat(feedTot,
				// "#.##"));
			});
			result = REDIRECT;
		} else {
			Set<Calf> calfSet = new HashSet<>();

			Cow cowTemp = farmerService.findCowByCowId(cow.getId());
			if (!StringUtil.isEmpty(dateOfBirth)) {
				cowTemp.setDob(DateUtil.convertStringToDate(dateOfBirth, DateUtil.DATE_FORMAT));
			}
			cowTemp.setUpdatedDate(new Date());
			if (cow.getCowImage() != null) {
				cowTemp.setPhoto(FileUtil.getBinaryFileContent(cow.getCowImage()));
				cowTemp.setPhotoCaptureTime(new Date());
			}
			cowTemp.setDamId(cow.getDamId());
			cowTemp.setSireId(cow.getSireId());
			cowTemp.setGenoType(cow.getGenoType());
			cowTemp.setMilkFirstHundPerDay(cow.getMilkFirstHundPerDay());
			cowTemp.setTagNo(cow.getTagNo());
			cowTemp.setLactationNo(cow.getLactationNo());

			if (!StringUtil.isEmpty(calfProdData) && calfProdData.length() > 0) {

				String calfDataSpilt[] = calfProdData.split("@@@");
				for (int i = 0; i < calfDataSpilt.length; i++) {
					Calf calf = new Calf();
					String data[] = calfDataSpilt[i].split("###");

					calf.setCalfId(data[0]);
					calf.setBullId(data[1]);
					if (!StringUtil.isEmpty(data[2])) {
						calf.setServiceDate(DateUtil.convertStringToDate(data[2], DateUtil.DATE_FORMAT));
					}
					if (!StringUtil.isEmpty(data[3])) {
						calf.setLastDateCalving(DateUtil.convertStringToDate(data[3], DateUtil.DATE_FORMAT));
					}
					calf.setGender(!StringUtil.isEmpty(data[4]) ? data[4] : "");
					calf.setBirthWeight(!StringUtil.isEmpty(data[5]) ? Double.valueOf(data[5]) : 0.0);
					calf.setCalvingIntvalDays(!StringUtil.isEmpty(data[6]) ? Double.valueOf(data[6]) : 0.0);
					calf.setDeliveryProcess(data[7]);
					if (!StringUtil.isEmpty(data[8])) {
						calf.setId(Long.valueOf(data[8]));
					}

					calfSet.add(calf);

				}

			} else {
				calfList = farmerService.listOfCalfs(cow.getId());
				calfSet.addAll(calfList);

			}
			cowTemp.setCalfs(calfSet);

			if (!StringUtil.isEmpty(researchStationId)) {
				ResearchStation researchStation = farmerService.findResearchStation(Long.valueOf(researchStationId));
				cowTemp.setResearchStation(researchStation);
				cowTemp.setElitType(Cow.ELITE_RESEARCH);
				result = RESEARCHSTATION_DETAIL;
			} else if (!StringUtil.isEmpty(farmId)) {
				Farm farm = farmerService.findFarmByfarmId(Long.valueOf(farmId));
				cowTemp.setFarmerId(farm.getFarmer().getFarmerId());
				cowTemp.setFarm(farm);
				cowTemp.setElitType(Cow.ELITE_FARMER);
				result = FARM_DETAIL;
			}
			farmerService.updateCow(cowTemp);

		}

		return result;
	}

	public String delete() {
		String result = null;
		if (!StringUtil.isEmpty(id)) {
			Cow cow = farmerService.findCowByCowId(Long.valueOf(id));

			farmerService.removeCow(cow);
			if (!StringUtil.isEmpty(researchStationId)) {
				result = RESEARCHSTATION_DETAIL;
			} else {
				result = FARM_DETAIL;

			}

		}

		return result;

	}

	public String getCatalouge(String code) {
		setCatalougeValues("");
		if (!StringUtil.isEmpty(code)) {
			String[] codes = code.split(",");
			Arrays.asList(codes).stream().filter(codez -> !StringUtil.isEmpty(codez)).forEach(codez -> {
				FarmCatalogue catalogue = catalogueService.findCatalogueByCode(codez);
				if (!ObjectUtil.isEmpty(catalogue)) {
					if (!StringUtil.isEmpty(getCatalougeValues())) {
						setCatalougeValues(getCatalougeValues() + catalogue.getName() + ",");
					} else {
						setCatalougeValues(catalogue.getName() + ",");
					}
				}
			});
		}

		return !StringUtil.isEmpty(catalougeValues) ? StringUtil.removeLastComma(catalougeValues) : "";
	}

	public Map<String, String> getDamIdList() {

		List<FarmCatalogue> farmCatalougeList = catalogueService
				.findFarmCatalougeByType(Integer.valueOf(getText("damId").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
	}

	public Map<String, String> getSireIdList() {

		List<FarmCatalogue> farmCatalougeList = catalogueService
				.findFarmCatalougeByType(Integer.valueOf(getText("sireId").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
	}

	public Map<String, String> getBullIdList() {

		List<FarmCatalogue> farmCatalougeList = catalogueService
				.findFarmCatalougeByType(Integer.valueOf(getText("bullId").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
	}

	public Map<String, String> getGenoTypeList() {

		List<FarmCatalogue> farmCatalougeList = catalogueService
				.findFarmCatalougeByType(Integer.valueOf(getText("genotype").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
	}

	public Map<String, String> getDeliveryProcessList() {

		List<FarmCatalogue> farmCatalougeList = catalogueService
				.findFarmCatalougeByType(Integer.valueOf(getText("deliveryProcess").trim()));
		return farmCatalougeList.stream().collect(Collectors.toMap(FarmCatalogue::getCode, FarmCatalogue::getName));
	}

	public void prepare() throws Exception {
		String farmId = (String) request.getParameter("farmId");
		String researchStationId = (String) request.getParameter("researchStationId");
		String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = super.getText(BreadCrumb.BREADCRUMB, "");
		}
		if (!StringUtil.isEmpty(farmId)) {

			request.setAttribute(BreadCrumb.BREADCRUMB,
					BreadCrumb.getBreadCrumb(content) + farmId + "&" + getTabIndexz());

		} else if (!StringUtil.isEmpty(researchStationId)) {
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content) +researchStationId+ "&" + getTabIndexz());
		}else{
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content) + "&" + getTabIndexz());
		}

	}

	/**
	 * Gets the binary file content.
	 * 
	 * @param file
	 *            the file
	 * @return the binary file content
	 */
	public static byte[] getBinaryFileContent(File file) {

		byte[] fileContent = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];

			while (fis.read(b) != -1) {
				baos.write(b);
				b = new byte[1024];
			}

			fileContent = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	public String getResearchStationId() {

		return researchStationId;
	}

	public void setResearchStationId(String researchStationId) {
		this.researchStationId = researchStationId;
	}

	public Cow getCow() {
		return cow;
	}

	public void setCow(Cow cow) {
		this.cow = cow;
	}

	public String getCalfProdData() {
		return calfProdData;
	}

	public void setCalfProdData(String calfProdData) {
		this.calfProdData = calfProdData;
	}

	public Map<String, String> getGenderType() {
		return genderType;
	}

	public void setGenderType(Map<String, String> genderType) {
		this.genderType = genderType;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public List<Calf> getCalfList() {
		return calfList;
	}

	public void setCalfList(List<Calf> calfList) {
		this.calfList = calfList;
	}

	public String getCowImageByteString() {
		return cowImageByteString;
	}

	public void setCowImageByteString(String cowImageByteString) {
		this.cowImageByteString = cowImageByteString;
	}

	public String getFarmerDetailParams() {

		String idVal = null;

		if (!StringUtil.isEmpty(getFarmId())) {
			idVal = getFarmId();
		} else if (!StringUtil.isEmpty(researchStationId)) {
			idVal = researchStationId;
		}

		return "tabIndex=" + URLEncoder.encode(tabIndexz) + "&id=" + idVal + "&" + tabIndexz;

	}

	public String getCatalougeValues() {
		return catalougeValues;
	}

	public void setCatalougeValues(String catalougeValues) {
		this.catalougeValues = catalougeValues;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTabIndexz() {
		return tabIndexz;
	}

	public void setTabIndexz(String tabIndexz) {
		this.tabIndexz = tabIndexz;
	}

}
