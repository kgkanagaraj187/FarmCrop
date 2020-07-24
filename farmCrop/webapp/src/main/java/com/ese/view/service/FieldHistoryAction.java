package com.ese.view.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.util.Base64Util;
import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IExporter;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.DynamicImageData.TYPES;
import com.sourcetrace.esesw.view.BaseReportAction;

public class FieldHistoryAction extends BaseReportAction implements IExporter{

	private static final long serialVersionUID = 1L;
	private String id;
	private static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private String xlsFileName;
	private InputStream fileInputStream;
	private String daterange;
	private FarmerDynamicData filter;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ICatalogueService catalogueService;
	private String tabIndexFarmer = "#tabs-14";
	private String farmerUniqueId;
	List<Object[]> dynamicData = new LinkedList<>();
	private Map<Integer, String> inspectionStatuslist = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> icsStatusList = new LinkedHashMap<Integer, String>();
	private String txnType;
	private String entityType;
	private String mainGridCols;
	private LinkedHashMap<String, Integer> fiedlList = new LinkedHashMap<>();
	private Map<String, List<DynamicFieldConfig>> parentMap = new HashMap<>();

	private List<Object[]> dynamicSectionConfigHeader;
	private FarmerDynamicData farmerDynamicData;
	private String selectedFarmer;
	private String selectedFarm;
	private String inspectionStatus;
	private String icsType;
	private String correctiveActionPlan;
	private String selectedId;
	private String farmList;
	private String farmer;
	private String group;
	private String noOfPalm;
	private String selectedVillage;
	private List<DynamicFieldReportJoinMap> dynamicFieldReportJoinMapList;
	private List<DynamicFieldReportConfig> dynamicFieldReportConfigList;
	private List<DynamicFeildMenuConfig> dynamicMenuConfigList;
	List<Object[]> farms = new ArrayList<Object[]>();
	private String condtion;
	private String command;
	private String season;
	Map<String, List<FarmerDynamicFieldsValue>> fdMap = new HashMap<>();
	DynamicFeildMenuConfig dm = new DynamicFeildMenuConfig();
	Map<String, DynamicFieldConfig> fieldConfigMap = new HashMap<>();
	List<Warehouse> samithi = new ArrayList<Warehouse>();
	private String seasonCode;
	private Integer seasonType;
	private String txType;
	Map<String, String> fieldTypeMap = new HashMap<>();
	Map<String, String> filterMap = new HashMap<>();
	private String filters;
	private String infoName;
	private String insDate;
	private String inspectorName;
	private String inspectorMobile;
	private String insType;
	private String scope;
	private String totLand;
	private String orgLand;
	private String totSite;
	private String farmerId;
	private String inspectionDateYear;
	private String typez;
	private String address;
	private String area;
	private String digitalSignatureByteString;
	private String agentSignatureByteString;
	Map<String, String> agentList = new LinkedHashMap<String, String>();
	@Autowired
	private ICertificationService certificationService;
	private Map<String, String> menuNames = new LinkedHashMap<String, String>();


	public String list() {
		Calendar currentDate = Calendar.getInstance();
		Calendar cal = (Calendar) currentDate.clone();
		cal.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 1);
		DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
		super.startDate = df.format(cal.getTime());
		super.endDate = df.format(currentDate.getTime());

		daterange = super.startDate + " - " + super.endDate;
		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		entityType = dy.getEntity();
		/*
		 * filters = joinList.stream().filter(u -> u.getFilterId() != null)
		 * .map(p -> String.valueOf(p.getFilterName() + "-" +
		 * p.getFilterId())).collect(Collectors.joining(","));
		 */
		formMainGridCols();
		//System.out.println(getEntityType());
		return LIST;
	}
	
	
	public String data() throws Exception {

		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		entityType = dy.getEntity();
		Map entityMap = new HashMap<>();

		Map<String, String> condMapc = new HashMap<>();

		if (filter != null && filter.getReferenceId() != null) {

		} else {
			setFilter(new FarmerDynamicData());
		}
		
		if (!StringUtil.isEmpty(id)) {
			condMapc.put("f.id", id);
		}

		/*if (!StringUtil.isEmpty(farmerId)) {
			Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
			if (farmer.getIsCertifiedFarmer() == 1) {
				List<Object[]> farmList = farmerService
						.listFarmFieldsByFarmerIdAndNonOrganic(Long.valueOf(farmerId));
				
				JSONArray farmArr = new JSONArray();
				farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
				});
				sendAjaxResponse(farmArr);
			} else {

				List<Object[]> farmList = farmerService
						.listFarmFieldsByFarmerIdNonCertified(Long.valueOf(farmerId));
				JSONArray farmArr = new JSONArray();
				farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
				});
				sendAjaxResponse(farmArr);

			}

		}
*/

		if (!StringUtil.isEmpty(farmList)) {
			condMapc.put("farmList", farmList);
		}

		if (!StringUtil.isEmpty(seasonCode)) {

			filter.setSeason(seasonCode);
		}
		if (!condMapc.isEmpty()) {
			entityMap.put(IClientService.PROJECTIONS_CONDITION, condMapc);
			entityMap.put(IReportDAO.JOIN_MAP, getDynamicFieldReportJoinMapList());
			entityMap.put(IReportDAO.ENTITY, entityType);
		}
		
		//filter.setReferenceId(id);
		filter.setTxnType(getTxnType());
		filter.setEntityMap(entityMap);
		super.filter = filter;

		Map data = readData();

		return sendJSONResponse(data);

	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		FarmerDynamicData farmerDynamicData = (FarmerDynamicData) obj;
		DynamicFeildMenuConfig dy = getDynamicMenuConfigList().get(0);
		entityType = dy.getEntity();
		if (!ObjectUtil.isEmpty(farmerDynamicData)) {
			String entu = "";
			rows.add(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");

			rows.add(getAgentList().get(farmerDynamicData.getCreatedUser()));

			if (dy.getIsSeason() == 1) {

				HarvestSeason season = getHarvestSeason(farmerDynamicData.getSeason());
				rows.add(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			}

			if (!StringUtil.isEmpty(entityType)) {
				int rType = Integer.parseInt(entityType);
				/*
				 * if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()) { Farmer
				 * farmer = getFarmer(farmerDynamicData.getReferenceId());
				 * rows.add(farmer.getFirstName());
				 * rows.add(farmer.getVillage().getName());
				 * rows.add(farmer.getSamithi().getName()); } else if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.FARM.ordinal() || rType ==
				 * DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()) {
				 */
				Map criteriaMap = new HashMap<>();
				if (rType == DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()
						|| rType == DynamicFeildMenuConfig.EntityTypes.TRAINING.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Farmer.class);
				} else if (rType == DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()
						|| rType == DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Farm.class);
				} else if (rType == DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()) {
					criteriaMap.put(IClientService.ENTITY, Warehouse.class);
				}

				StringBuilder list = new StringBuilder();
				StringBuilder alias = new StringBuilder();

				getDynamicFieldReportJoinMapList().stream().forEach(join -> {
					alias.append(join.getProperties() + ",");
				});

				criteriaMap.put(IClientService.CRITERIA_ALIAS, StringUtil.removeLastComma(alias.toString()));

				getDynamicFieldReportConfigList().stream()
						.filter(dReportConfig -> ObjectUtil.isEmpty(dReportConfig.getGroupProp())
								|| !dReportConfig.getGroupProp().equals("1"))
						.forEach(dReportConfig -> {
							list.append(dReportConfig.getField() + ",");
						});

				criteriaMap.put(IClientService.PROJECTIONS_LIST, StringUtil.removeLastComma(list.toString()));
				List<Object> valueListc = clientService.getMappedValues(criteriaMap,
						farmerDynamicData.getReferenceId());
				if (valueListc.size() > 0 && valueListc.get(0) instanceof String) {
					rows.add(valueListc.get(0));
					entu = String.valueOf(valueListc.get(0));
				} else if (valueListc.size() > 0 && valueListc.get(0) instanceof Object[]) {
					Object[] objList = (Object[]) valueListc.get(0);
					for (Object obje : objList) {

						rows.add(String.valueOf(obje));

					}
					entu = String.valueOf(objList[0]);
				}

				/*
				 * } else if (rType ==
				 * DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()) {
				 * Warehouse warehouse =
				 * getWarehouse(farmerDynamicData.getReferenceId());
				 * rows.add(warehouse.getName()); }
				 */
			}

			Map<String, String> valuesMap = new LinkedHashMap<>();
			farmerDynamicData.getFarmerDynamicFieldsValues().stream().forEach(dynamicFieldValues -> {

				if (!StringUtil.isEmpty(dynamicFieldValues.getComponentType()) && Arrays
						.asList(String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.DROP_DOWN.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.RADIO.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.MULTIPLE_SELECT.ordinal()),
								String.valueOf(DynamicFieldConfig.COMPONENT_TYPES.CHECKBOX.ordinal()))
						.contains(dynamicFieldValues.getComponentType())) {
					valuesMap.put(dynamicFieldValues.getFieldName(),
							getCatlogueValueByCode(dynamicFieldValues.getFieldValue()).getName());
				} else {
					valuesMap.put(dynamicFieldValues.getFieldName(), dynamicFieldValues.getFieldValue());
				}

			});
			dy.getDynamicFieldConfigs().stream()
					.filter(u -> u.getField().getIsReportAvail() != null && u.getField().getIsReportAvail().equals("1"))
					.forEach(dynamicFieldReportConfig -> {
						if (valuesMap.containsKey(dynamicFieldReportConfig.getField().getCode())) {
							rows.add(valuesMap.get(dynamicFieldReportConfig.getField().getCode()));
						} else {
							rows.add("");
						}
					});
			if (txType != null && txType.equalsIgnoreCase("report")) {
				rows.add("<button class='xlsIcon' title='" + getText("dynExport", new String[] { entu }) + " "
						+ dy.getLangName(getLoggedInUserLanguage()) + " for the "
						+ DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT)
						+ "' onclick='exportXLS(\"" + farmerDynamicData.getId() + "\")'></button>");
			}
			jsonObject.put("id", farmerDynamicData.getId());
			jsonObject.put("cell", rows);
		}
		return jsonObject;
	
	}
	
	public void populateFarm() throws Exception {

		if (getCurrentTenantId().equalsIgnoreCase("wilmar")) {
			/*
			 * if(command == "create"){ if (!StringUtil.isEmpty(selectedFarmer))
			 * { List<Object[]> farmList =
			 * farmerService.listFarmFieldsByFarmerIdAndNonOrganic(Long.valueOf(
			 * selectedFarmer)); JSONArray farmArr = new JSONArray();
			 * farmList.stream().filter(obj ->
			 * !ObjectUtil.isEmpty(obj)).forEach(obj -> {
			 * farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
			 * }); sendAjaxResponse(farmArr); } } else{
			 */
			if (!StringUtil.isEmpty(farmerId)) {
				Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
				if (farmer.getIsCertifiedFarmer() == 1) {
					List<Object[]> farmList = farmerService
							.listFarmFieldsByFarmerIdAndNonOrganic(Long.valueOf(farmerId));
					
					JSONArray farmArr = new JSONArray();
					farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
						farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
					});
					sendAjaxResponse(farmArr);
				} else {

					List<Object[]> farmList = farmerService
							.listFarmFieldsByFarmerIdNonCertified(Long.valueOf(farmerId));
					JSONArray farmArr = new JSONArray();
					farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
						farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
					});
					sendAjaxResponse(farmArr);

				}

			}
		} else {
			if (!StringUtil.isEmpty(selectedFarmer)) {
				List<Object[]> farmList = farmerService.listFarmFieldsByFarmerId(Long.valueOf(selectedFarmer));
				JSONArray farmArr = new JSONArray();
				farmList.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					farmArr.add(getJSONObject(obj[0].toString(), obj[2].toString()));
				});
				sendAjaxResponse(farmArr);
			}
		}
	}
	public List<DynamicFieldReportJoinMap> getDynamicFieldReportJoinMapList() {
		if (ObjectUtil.isListEmpty(dynamicFieldReportJoinMapList)) {
			dynamicFieldReportJoinMapList = new ArrayList<>();
			/*
			 * dynamicFieldReportJoinMapList = farmerService
			 * .findDynamicFieldReportJoinMapByEntityAndTxn(getEntityType(),
			 * getTxnType()).stream() .collect(Collectors.toList());
			 */
			dynamicFieldReportJoinMapList = farmerService.findDynamicFieldReportJoinMapByEntity(getEntityType())
					.stream().collect(Collectors.toList());
		}
		return dynamicFieldReportJoinMapList;
	}
	
	public String detail() throws Exception {
		if (id != null && !id.equals("")) {
			farmerDynamicData = farmerService.findFarmerDynamicData(id);
			seasonType = farmerDynamicData.getIsSeason();
			entityType = farmerDynamicData.getEntityId();
			farmerDynamicData.setTxnDate(!StringUtil.isEmpty(farmerDynamicData.getDate())
					? DateUtil.convertDateToString(farmerDynamicData.getDate(), DateUtil.DATE_TIME_FORMAT) : "");
			if (!ObjectUtil.isEmpty(farmerDynamicData) && farmerDynamicData.getEntityId().equalsIgnoreCase("4")) {

				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setGroup(String.valueOf(farm.getFarmer().getSamithi().getName()));
					setSelectedVillage(farm.getFarmer().getVillage().getName());
				}
				farmerDynamicData.setInspectionStatus(farmerDynamicData.getConversionStatus());

				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())) {
					if (farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {
						farmerDynamicData.setInspectionStatus(getLocaleProperty("Approved"));
					} else {
						farmerDynamicData.setInspectionStatus(getLocaleProperty("Declined"));
					}
				}
				if (farmerDynamicData.getConversionStatus() != null
						&& !StringUtil.isEmpty(farmerDynamicData.getConversionStatus())
						&& farmerDynamicData.getConversionStatus().equalsIgnoreCase("1")) {
					if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("0")) {
						setIcsType(getLocaleProperty("farm.ics1"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("1")) {
						setIcsType(getLocaleProperty("farm.ics2"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("2")) {
						setIcsType(getLocaleProperty("farm.ics3"));
					} else if (!StringUtil.isEmpty(farmerDynamicData.getIcsName())
							&& farmerDynamicData.getIcsName().equalsIgnoreCase("3")) {
						setIcsType(getLocaleProperty("farm.organic"));
					}

				} else {
					setCorrectiveActionPlan(!StringUtil.isEmpty(farmerDynamicData.getCorrectiveActionPlan())
							? farmerDynamicData.getCorrectiveActionPlan() : "");
				}
				if (!ObjectUtil.isEmpty(farmerDynamicData.getFarmIcs())) {
					setInsDate(DateUtil.convertDateToString(farmerDynamicData.getFarmIcs().getInspectionDate(),
							getGeneralDateFormat()));
					setInspectorName(farmerDynamicData.getFarmIcs().getInspectorName());
					setInspectorMobile(farmerDynamicData.getFarmIcs().getInspectorMobile());
					setInsType(!StringUtil.isEmpty(farmerDynamicData.getFarmIcs().getInsType())
							? getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getInsType()).getName() : "");
					// CertificateCategory cc =
					// certificationService.findCertificateCategoryByCode(farmerDynamicData.getFarmIcs().getScope());
					setScope(getCatlogueValueByCode(farmerDynamicData.getFarmIcs().getScope()).getName());
					setTotLand(farmerDynamicData.getFarmIcs().getTotalLand());
					setOrgLand(farmerDynamicData.getFarmIcs().getOrganicLand());
					setTotSite(farmerDynamicData.getFarmIcs().getTotalSite());
					setSeason(farmerDynamicData.getFarmIcs().getSeason());
				}

			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("2")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farm farm = farmerService.findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setAddress(farm != null ? String.valueOf(farm.getFarmer().getAddress()+ "," + farm.getFarmer().getCity().getName()+ "," +farm.getFarmer().getVillage().getName()) : "");
					setArea(farm.getFarmDetailedInfo().getProposedPlantingArea());
					setFarmer(String.valueOf(farm.getFarmer().getFirstName()));
					setFarmList(String.valueOf(farm.getFarmName()));
					setFarmerUniqueId(String.valueOf(farm.getFarmer().getId()));
					setSelectedVillage(farm.getFarmer().getVillage().getName());
				}
				if (farmerDynamicData.getDymamicImageData() != null
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData())
						&& farmerDynamicData.getDymamicImageData().size() > 0
						&& !ObjectUtil.isEmpty(farmerDynamicData.getDymamicImageData().iterator().next().getImage())) {
					
					if(farmerDynamicData.getDymamicImageData() != null){
						
						farmerDynamicData.getDymamicImageData().stream().forEach(i -> {
							if(i.getTypez() == TYPES.digitalSignature.ordinal()){
								setDigitalSignatureByteString(Base64Util.encoder(i.getImage()));
								
							}
							
							if(i.getTypez() == TYPES.agentSignature.ordinal()){
								setAgentSignatureByteString(Base64Util.encoder(i.getImage()));
								
							}
						});
			
					}
					
					
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("1")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("5")) {
				if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())
						&& farmerDynamicData.getReferenceId().contains(",")) {
					List<Farmer> frmr = farmerService
							.listFarmerByIds(Arrays.asList(farmerDynamicData.getReferenceId().split(",")));
					setFarmer(String.valueOf(frmr.stream().filter(u -> u.getFirstName() != null)
							.map(p -> String.valueOf(p.getFirstName() + (p.getLastName()!=null ? ("-" + p.getLastName()) : "")))
							.collect(Collectors.joining(","))));
					
				} else if (!StringUtil.isEmpty(farmerDynamicData.getReferenceId())) {
					Farmer frmr = farmerService.findFarmerById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setFarmer(String.valueOf(frmr.getFirstName()));
				}
			} else if (!ObjectUtil.isEmpty(farmerDynamicData)
					&& farmerDynamicData.getEntityId().equalsIgnoreCase("3")) {
				if (!StringUtil.isEmail(farmerDynamicData.getReferenceId())) {
					Warehouse sam = locationService.findSamithiById(Long.valueOf(farmerDynamicData.getReferenceId()));
					setGroup(sam.getName());
				}
			}

			if (farmerDynamicData.getIsSeason() == 1) {
				HarvestSeason season = getSeason(farmerDynamicData.getSeason());
				setSeason(!ObjectUtil.isEmpty(season) ? season.getName() : "");
			}

			return DETAIL;
		}
		return LIST;
	}
	
	private HarvestSeason getSeason(String seasonCode) {

		HarvestSeason season = getHarvestSeason(seasonCode);
		return season;

	}
	

	private void formMainGridCols() {
		mainGridCols = "";
		List<DynamicFeildMenuConfig> dy = farmerService.listDynamicMenus();

		mainGridCols += getLocaleProperty("firstName") + "|" + getLocaleProperty("lastName") + "|"
				+ getLocaleProperty("farmerCode") + "|" + getLocaleProperty("procurement") + "|";
		mainGridCols += dy.stream().map(p -> String.valueOf(p.getLangName(getLoggedInUserLanguage())))
				.collect(Collectors.joining("|"));

	}

	
	
	public Map<String, String> getAgentList() {
		
		if(!StringUtil.isEmpty(getTxnType()) && agentList.isEmpty()){
		List<Object[]> fd  =farmerService.ListFarmerDynamicDataAgentByTxnType(getTxnType(),getBranchId());	
		agentList  = fd.stream().filter(f -> f != null && !ObjectUtil.isEmpty(f) && f[0] != null && f[1] != null ).collect(Collectors.toMap(u -> u[0].toString(), u -> (u[0].toString() +" - "+ u[1].toString())));
		}
			return agentList;
	}


	@Override
	public InputStream getExportDataStream(String exportType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getDaterange() {
		return daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public FarmerDynamicData getFilter() {
		return filter;
	}

	public void setFilter(FarmerDynamicData filter) {
		this.filter = filter;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public ILocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	public ICatalogueService getCatalogueService() {
		return catalogueService;
	}

	public void setCatalogueService(ICatalogueService catalogueService) {
		this.catalogueService = catalogueService;
	}

	public List<Object[]> getDynamicData() {
		return dynamicData;
	}

	public void setDynamicData(List<Object[]> dynamicData) {
		this.dynamicData = dynamicData;
	}

	public Map<Integer, String> getInspectionStatuslist() {
		return inspectionStatuslist;
	}

	public void setInspectionStatuslist(Map<Integer, String> inspectionStatuslist) {
		this.inspectionStatuslist = inspectionStatuslist;
	}

	public Map<Integer, String> getIcsStatusList() {
		return icsStatusList;
	}

	public void setIcsStatusList(Map<Integer, String> icsStatusList) {
		this.icsStatusList = icsStatusList;
	}

	public String getTxnType() {
		/*if (request.getParameter("txnType") != null && !StringUtil.isEmpty(request.getParameter("txnType"))) {
			txnType = request.getParameter("txnType");
		}*/
		return "2002";
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getMainGridCols() {
		return mainGridCols;
	}

	public void setMainGridCols(String mainGridCols) {
		this.mainGridCols = mainGridCols;
	}



	public LinkedHashMap<String, Integer> getFiedlList() {
		return fiedlList;
	}

	public void setFiedlList(LinkedHashMap<String, Integer> fiedlList) {
		this.fiedlList = fiedlList;
	}

	public Map<String, List<DynamicFieldConfig>> getParentMap() {
		return parentMap;
	}

	public void setParentMap(Map<String, List<DynamicFieldConfig>> parentMap) {
		this.parentMap = parentMap;
	}

	public List<Object[]> getDynamicSectionConfigHeader() {
		return dynamicSectionConfigHeader;
	}

	public void setDynamicSectionConfigHeader(List<Object[]> dynamicSectionConfigHeader) {
		this.dynamicSectionConfigHeader = dynamicSectionConfigHeader;
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	public String getSelectedFarmer() {
		return selectedFarmer;
	}

	public void setSelectedFarmer(String selectedFarmer) {
		this.selectedFarmer = selectedFarmer;
	}

	public String getSelectedFarm() {
		return selectedFarm;
	}

	public void setSelectedFarm(String selectedFarm) {
		this.selectedFarm = selectedFarm;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getIcsType() {
		return icsType;
	}

	public void setIcsType(String icsType) {
		this.icsType = icsType;
	}

	public String getCorrectiveActionPlan() {
		return correctiveActionPlan;
	}

	public void setCorrectiveActionPlan(String correctiveActionPlan) {
		this.correctiveActionPlan = correctiveActionPlan;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public String getFarmList() {
		return farmList;
	}

	public void setFarmList(String farmList) {
		this.farmList = farmList;
	}

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getNoOfPalm() {
		return noOfPalm;
	}

	public void setNoOfPalm(String noOfPalm) {
		this.noOfPalm = noOfPalm;
	}

	public List<Object[]> getFarms() {
		return farms;
	}

	public void setFarms(List<Object[]> farms) {
		this.farms = farms;
	}

	public String getCondtion() {
		return condtion;
	}

	public void setCondtion(String condtion) {
		this.condtion = condtion;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public Map<String, List<FarmerDynamicFieldsValue>> getFdMap() {
		return fdMap;
	}

	public void setFdMap(Map<String, List<FarmerDynamicFieldsValue>> fdMap) {
		this.fdMap = fdMap;
	}

	public DynamicFeildMenuConfig getDm() {
		return dm;
	}

	public void setDm(DynamicFeildMenuConfig dm) {
		this.dm = dm;
	}

	public Map<String, DynamicFieldConfig> getFieldConfigMap() {
		return fieldConfigMap;
	}

	public void setFieldConfigMap(Map<String, DynamicFieldConfig> fieldConfigMap) {
		this.fieldConfigMap = fieldConfigMap;
	}

	public List<Warehouse> getSamithi() {
		return samithi;
	}

	public void setSamithi(List<Warehouse> samithi) {
		this.samithi = samithi;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Integer getSeasonType() {
		return seasonType;
	}

	public void setSeasonType(Integer seasonType) {
		this.seasonType = seasonType;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public Map<String, String> getFieldTypeMap() {
		return fieldTypeMap;
	}

	public void setFieldTypeMap(Map<String, String> fieldTypeMap) {
		this.fieldTypeMap = fieldTypeMap;
	}

	public Map<String, String> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(Map<String, String> filterMap) {
		this.filterMap = filterMap;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getInspectorMobile() {
		return inspectorMobile;
	}

	public void setInspectorMobile(String inspectorMobile) {
		this.inspectorMobile = inspectorMobile;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTotLand() {
		return totLand;
	}

	public void setTotLand(String totLand) {
		this.totLand = totLand;
	}

	public String getOrgLand() {
		return orgLand;
	}

	public void setOrgLand(String orgLand) {
		this.orgLand = orgLand;
	}

	public String getTotSite() {
		return totSite;
	}

	public void setTotSite(String totSite) {
		this.totSite = totSite;
	}

	public String getInspectionDateYear() {
		return inspectionDateYear;
	}

	public void setInspectionDateYear(String inspectionDateYear) {
		this.inspectionDateYear = inspectionDateYear;
	}

	public String getTypez() {
		return typez;
	}

	public void setTypez(String typez) {
		this.typez = typez;
	}

	

	public ICertificationService getCertificationService() {
		return certificationService;
	}

	public void setCertificationService(ICertificationService certificationService) {
		this.certificationService = certificationService;
	}

	public Map<String, String> getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(Map<String, String> menuNames) {
		this.menuNames = menuNames;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static SimpleDateFormat getFilenamedateformat() {
		return fileNameDateFormat;
	}
	
	public List<DynamicFeildMenuConfig> getDynamicMenuConfigList() {
		if (ObjectUtil.isListEmpty(dynamicMenuConfigList)) {
			dynamicMenuConfigList = new ArrayList<>();
			dynamicMenuConfigList = farmerService.findDynamicMenusByType(getTxnType()).stream()
					.collect(Collectors.toList());
			dynamicMenuConfigList.forEach(u -> {
				u.setName(u.getLangName(getLoggedInUserLanguage()));
			});
		}
		return dynamicMenuConfigList;
	}
	
	public List<DynamicFieldReportConfig> getDynamicFieldReportConfigList() {
		if (ObjectUtil.isListEmpty(dynamicFieldReportConfigList)) {
			dynamicFieldReportConfigList = new ArrayList<>();
			/*
			 * dynamicFieldReportConfigList = farmerService
			 * .findDynamicFieldReportConfigByEntityAndTxn(getEntityType(),
			 * getTxnType()).stream() .collect(Collectors.toList());
			 */

			dynamicFieldReportConfigList = farmerService.findDynamicFieldReportConfigByEntity(getEntityType()).stream()
					.collect(Collectors.toList());
		}
		return dynamicFieldReportConfigList;
	}


	public String getFarmerId() {
		return farmerId;
	}


	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}


	public String getSelectedVillage() {
		return selectedVillage;
	}


	public void setSelectedVillage(String selectedVillage) {
		this.selectedVillage = selectedVillage;
	}


	

	public String getTabIndexFarmer() {
		return tabIndexFarmer;
	}


	public void setTabIndexFarmer(String tabIndexFarmer) {
		this.tabIndexFarmer = tabIndexFarmer;
	}


	public String getFarmerUniqueId() {
		return farmerUniqueId;
	}


	public void setFarmerUniqueId(String farmerUniqueId) {
		this.farmerUniqueId = farmerUniqueId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getDigitalSignatureByteString() {
		return digitalSignatureByteString;
	}


	public void setDigitalSignatureByteString(String digitalSignatureByteString) {
		this.digitalSignatureByteString = digitalSignatureByteString;
	}


	public String getAgentSignatureByteString() {
		return agentSignatureByteString;
	}


	public void setAgentSignatureByteString(String agentSignatureByteString) {
		this.agentSignatureByteString = agentSignatureByteString;
	}
	
	

	


}
