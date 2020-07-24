/*
 * SMSAlertAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.sms.SMSHistory;
import com.ese.entity.sms.SMSHistoryDetail;
import com.ese.entity.sms.SmsGroupDetail;
import com.ese.entity.sms.SmsGroupHeader;
import com.ese.entity.sms.SmsTemplate;
import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class SMSAlertAction extends SwitchValidatorAction {

	private static final Logger LOGGER = Logger.getLogger(SMSAlertAction.class);
	private static final String COMMA_SEPARATOR = ",";
	private static final int INVALID_JOB_ID_LOOP_COUNT = 2;
	private SMSHistory smsHistory;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private IPreferencesService preferencesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IUserService userService;

	private static final String FIRST_NAME_PATTERN = "#First Name#";
	private static final String VILLAGE_NAME_PATTERN = "#Village Name#";
	private static final String GROUP_NAME_PATTERN = "#Group Name#";

	/*
	 * @Autowired private IReportService smsAlertReportService;
	 */

	private String selectedFarmerIds = "", selectedFieldStaffIds = "", selectedUserIds = "";
	private String groupName;
	private String groupId;

	private String mobileNos, message;

	private String templateId, templateName, templateContent;

	private String groupIds;

	private String receiverTypeOptionValues;

	private JSONObject jsonObject = new JSONObject();
	private JsonDataObject jsonDataObject = new JsonDataObject();
	private Gson gson = new Gson();

	private long smsHistoryDetailId;

	protected Map<Character, String> statusList = new LinkedHashMap<Character, String>();

	protected String statusSearchOptionValues;
	protected String statusSearchYesNoOptionValues;
	protected long id;
	private String selectedTemplate;
	private String selectedGroups;

	private static final String TAB = "TAB";
	private static final String FARMER_TAB = "FARMER_TAB";
	private static final String FS_TAB = "FS_TAB";
	private static final String USER_TAB = "USER_TAB";

	private String selectAllFarmers;
	private String selectAllFs;
	private String selectAllWebUsers;

	private String farmer_branchid;
	private String farmer_fpo;
	private String farmer_first_name;
	private String farmer_last_name;
	private String farmer_mobile;
	private String farmer_tehsil;
	private String farmer_village;
	private String farmer_status;
	private String farmer_district;
	private String farmer_state;
	private String farmer_cropNames;

	private String fs_branchId;
	private String fs_agentId;
	private String fs_SMSfirstName;
	private String fs_SMSlastName;
	private String fs_SMSmobileNumber;
	private String fs_status;

	private String wu_branchId;
	private String wu_username;
	private String wu_SMSfirstName;
	private String wu_SMSlastName;
	private String wu_SMSmobileNumber;
	private String wu_status;

	/**
	 * Creates the.
	 * 
	 * @return the string
	 */
	public String create() {

		LOGGER.info("Create");
		return INPUT;
	}

	public String list() {

		LOGGER.info("list");
		return LIST;
	}

	private String removeTimeZoneColon(String date) {

		StringBuffer sb = new StringBuffer();
		sb.append(date.substring(0, date.lastIndexOf(":")));
		sb.append(date.substring(date.lastIndexOf(":") + 1));
		return sb.toString();
	}

	private boolean isInvalidJobIdResponse(String response) {

		boolean isInvalidJob = false;
		try {
			JSONObject respObj = new JSONObject(response);
			JSONObject fastAlerts = respObj.getJSONObject(ISMSService.SMS_FASTALERTS);
			Object descriptionObj = fastAlerts.has(ISMSService.SMS_RES_DESCR)
					? fastAlerts.get(ISMSService.SMS_RES_DESCR) : null;
			String descrption = (!ObjectUtil.isEmpty(descriptionObj)) ? String.valueOf(descriptionObj) : null;
			if (!StringUtil.isEmpty(descrption) && ISMSService.SMS_INVALID_JOB_ID_DESC.equalsIgnoreCase(descrption)) {
				isInvalidJob = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isInvalidJob;
	}

	/*
	 * private Object getValue(JSONObject object, String key) { Object value =
	 * null; try { if (!ObjectUtil.isEmpty(object) && !StringUtil.isEmpty(key)
	 * && !ObjectUtil.isEmpty(object.get(key))) { value = (String)
	 * object.get(key); } } catch (JSONException e) { e.printStackTrace(); }
	 * return value; }
	 */

	@Override
	public Object getData() {

		receiverTypeOptionValues = "ALL:" + getText("filter.receiverType.all") + ";FARMER:"
				+ getText("filter.receiverType.farmer") + ";FIELD_STAFF:" + getText("filter.receiverType.fieldStaff")
				+ ";USER:" + getText("filter.receiverType.user") + ";OTHER:" + getText("filter.receiverType.other");
		buildStatusListMap();
		return smsHistory;
	}

	protected void buildStatusListMap() {

		statusList.put('Y', getText("active"));
		statusList.put('N', getText("inActive"));

		buildStatusSearchOptionValues();
	}

	/**
	 * Builds the status search option values.
	 */
	private void buildStatusSearchOptionValues() {

		StringBuffer sb = new StringBuffer();
		sb.append(":").append(getText("search.all"));
		for (Map.Entry<Character, String> entry : statusList.entrySet()) {
			sb.append(";").append(entry.getKey()).append(":").append(entry.getValue());
		}
		this.statusSearchOptionValues = sb.toString();
	}

	private void resetFields() {

		smsHistory = new SMSHistory();
	}

	// **********************************************
	/**
	 * Save group.
	 * 
	 * @return the string
	 */
	List<String> farmersIds = new ArrayList<>();
	List<String> fieldStaffIds = new ArrayList<>();
	List<String> userIds = new ArrayList<>();

	public String saveGroup() {

		String success = "0", message = "";
		if (!StringUtil.isEmpty(groupName)) {
			List<SmsGroupDetail> groupDetails = new ArrayList<SmsGroupDetail>();

			if (selectAllFarmers.equalsIgnoreCase("yes") || selectAllFs.equalsIgnoreCase("yes")
					|| selectAllWebUsers.equalsIgnoreCase("yes")) {
				if (selectAllFarmers.equalsIgnoreCase("yes")) {
					Farmer f = new Farmer();
					Locality ld = new Locality();
					State s = new State();
					Municipality m = new Municipality();
					if (!StringUtil.isEmpty(getFarmer_first_name())) {
						f.setFirstName(getFarmer_first_name().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_last_name())) {
						f.setLastName(getFarmer_last_name().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_mobile())) {
						f.setMobileNumber(getFarmer_mobile().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_state())) {
						s.setName(getFarmer_state());
						ld.setState(s);
						m.setLocality(ld);
						f.setCity(m);
					}

					if (!StringUtil.isEmpty(getFarmer_district())) {
						ld.setName(getFarmer_district());
						m.setLocality(ld);
						f.setCity(m);
					}

					if (!StringUtil.isEmpty(getFarmer_cropNames())) {
						f.setCropNames(getFarmer_cropNames().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_tehsil())) {
						f.setCityName(getFarmer_tehsil().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_village())) {
						f.setVillageName(getFarmer_village().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_status())) {
						f.setFarmer_status(getFarmer_status().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_branchid())) {
						f.setBranchId(getFarmer_branchid().trim());
					}

					if (!StringUtil.isEmpty(getFarmer_fpo())) {
						f.setFarmer_fpo(getFarmer_fpo().trim());
					}

					List<Long> farmersIdsObjectArray = farmerService.listFarmerPrimaryId(f);
					farmersIdsObjectArray.stream().forEach(i -> farmersIds.add(String.valueOf(i)));
				}

				if (selectAllFs.equalsIgnoreCase("yes")) {

					Agent agent = new Agent();
					PersonalInfo personalInfo = new PersonalInfo();
					agent.setPersonalInfo(personalInfo);

					if (!StringUtil.isEmpty(getFs_agentId())) {
						agent.setProfileId(getFs_agentId().trim());
					}

					if (!StringUtil.isEmpty(getFs_SMSfirstName())) {
						agent.getPersonalInfo().setFirstName(getFs_SMSfirstName().trim());
					}
					if (!StringUtil.isEmpty(getFs_SMSlastName())) {
						agent.getPersonalInfo().setLastName(getFs_SMSlastName().trim());
					}
					if (!StringUtil.isEmpty(getFs_SMSmobileNumber())) {
						agent.setMobileno(getFs_SMSmobileNumber().trim());
					}

					if (!StringUtil.isEmpty(getFs_status())) {
						agent.setFs_status(getFs_status().trim());
					}

					if (!StringUtil.isEmpty(getFs_branchId())) {
						agent.setBranchId(getFs_branchId().trim());
					}

					List<Long> profileIdsObjectArray = farmerService.listProfilePrimaryId(agent);
					profileIdsObjectArray.stream().forEach(i -> fieldStaffIds.add(String.valueOf(i)));
				}

				if (selectAllWebUsers.equalsIgnoreCase("yes")) {

					User user = new User();
					PersonalInfo personalInfo = new PersonalInfo();
					user.setPersonalInfo(personalInfo);

					if (!StringUtil.isEmpty(getWu_username())) {
						user.setUsername(getWu_username().trim());
					}

					if (!StringUtil.isEmpty(getWu_SMSfirstName())) {
						user.getPersonalInfo().setFirstName(getWu_SMSfirstName().trim());
					}
					if (!StringUtil.isEmpty(getWu_SMSlastName())) {
						user.getPersonalInfo().setLastName(getWu_SMSlastName().trim());
					}
					if (!StringUtil.isEmpty(getWu_SMSmobileNumber())) {
						user.setMobileno(getWu_SMSmobileNumber().trim());
					}

					if (!StringUtil.isEmpty(getWu_status())) {
						user.setWu_status(getWu_status().trim());
					}

					if (!StringUtil.isEmpty(getWu_branchId())) {
						user.setBranchId(getWu_branchId().trim());
					}

					List<Long> webuserIdsObjectArray = farmerService.listWebUsersPrimaryId(user);
					webuserIdsObjectArray.stream().forEach(i -> userIds.add(String.valueOf(i)));
				}
			} else {
				farmersIds = StringUtil.split(selectedFarmerIds, ",");
				fieldStaffIds = StringUtil.split(selectedFieldStaffIds, ",");
				userIds = StringUtil.split(selectedUserIds, ",");
			}

			if (farmersIds.size() == 0 && fieldStaffIds.size() == 0 && userIds.size() == 0) {
				success = "0";
				message = getText("message.emptyMembers");
			} else {
				farmersIds = new ArrayList<String>(new LinkedHashSet<String>(farmersIds));
				fieldStaffIds = new ArrayList<String>(new LinkedHashSet<String>(fieldStaffIds));
				userIds = new ArrayList<String>(new LinkedHashSet<String>(userIds));
				if (StringUtil.isEmpty(groupId)) {
					SmsGroupHeader groupHeaderx = smsService.findGroupByName(groupName);
					if (ObjectUtil.isEmpty(groupHeaderx)) {
						SmsGroupHeader groupHeader = new SmsGroupHeader();
						groupHeader.setName(groupName);
						groupHeader.setIsActive('Y');
						groupHeader.setCreationInfo(getUsername());
						groupHeader.setBranchId(getBranchId());
						SmsGroupHeader header = smsService.addSmsGroupHeader(groupHeader);
						smsService.editSmsGroupHeader(buildWithGroupDeatils(farmersIds, fieldStaffIds, userIds, header),
								getCurrentTenantId());
						success = "1";
						message = getText("message.saved");
					} else {
						if (!ObjectUtil.isEmpty(groupHeaderx)) {
							SmsGroupHeader groupHeader = groupHeaderx;
							groupHeader.setName(groupName);
							groupHeader.setIsActive('Y');
							groupHeader.setCreationInfo(getUsername());
							groupHeader.setBranchId(getBranchId());
							smsService.editSmsGroupHeader(
									buildWithGroupDeatils(farmersIds, fieldStaffIds, userIds, groupHeader),
									getCurrentTenantId());
							success = "1";
							message = getText("message.update");
						}
						/*
						 * success = "0"; message =
						 * getText("message.groupNameExist");
						 */
					}

				} else {
					if (ObjectUtil.isLong(groupId)) {
						SmsGroupHeader groupHeader = smsService.findGroupHeaderById(Long.parseLong(groupId));

						SmsGroupHeader groupHeaderx = smsService.findGroupByName(groupName);
						if (ObjectUtil.isEmpty(groupHeaderx) || groupHeaderx.getId() == groupHeader.getId()) {
							groupHeader.setName(groupName);
							groupHeader.setUpdationInfo(getUsername());
							smsService.editSmsGroupHeader(
									buildWithGroupDeatils(farmersIds, fieldStaffIds, userIds, groupHeader),
									getCurrentTenantId());
							success = "1";
							message = getText("message.saved");
						} else {
							success = "0";
							message = getText("message.groupNameExist");
						}
					}
				}
			}
		}
		try {
			jsonDataObject = new JsonDataObject(1L, success, message, "");
			smsService.removeBlindChilds("sms_group_detail", "GROUP_ID", getCurrentTenantId());
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private SmsGroupHeader buildWithGroupDeatils(List<String> farmersIds, List<String> fieldStaffIds,
			List<String> userIds, SmsGroupHeader header) {

		Set<SmsGroupDetail> groupDetails = new HashSet<SmsGroupDetail>();
		for (String id : farmersIds) {
			if (ObjectUtil.isLong(id)) {
				SmsGroupDetail groupDetail = new SmsGroupDetail();
				groupDetail.setSmsGroupHeader(header);
				groupDetail.setProfileId(id);
				groupDetail.setProfileType(SmsGroupDetail.PROFILE_TYPE_FARMER);
				groupDetails.add(groupDetail);
			}
		}
		for (String id : fieldStaffIds) {
			if (ObjectUtil.isLong(id)) {
				SmsGroupDetail groupDetail = new SmsGroupDetail();
				groupDetail.setSmsGroupHeader(header);
				groupDetail.setProfileId(id);
				groupDetail.setProfileType(SmsGroupDetail.PROFILE_TYPE_FIELD_STAFF);
				groupDetails.add(groupDetail);
			}
		}
		for (String id : userIds) {
			if (ObjectUtil.isLong(id)) {
				SmsGroupDetail groupDetail = new SmsGroupDetail();
				groupDetail.setSmsGroupHeader(header);
				groupDetail.setProfileId(id);
				groupDetail.setProfileType(SmsGroupDetail.PROFILE_TYPE_USER);
				groupDetails.add(groupDetail);
			}
		}
		header.setSmsGroupDetails(groupDetails);
		return header;
	}

	/**
	 * Remove group.
	 * 
	 * @return the string
	 */
	public String removeGroup() {

		SmsGroupHeader smsGroupHeader = smsService.findGroupHeaderById(Long.parseLong(groupId));
		smsService.removeGroup(smsGroupHeader);
		try {
			jsonDataObject = new JsonDataObject(1L, "1", getText("msg.removed"), "");
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Save template.
	 * 
	 * @return the string
	 */
	public String saveTemplate() {

		String success = "0", message = "";
		if (!StringUtil.isEmpty(templateName)) {
			if (StringUtil.isEmpty(templateId)) {
				SmsTemplate smsTemplatex = smsService.findSmsTemplateByName(templateName);
				if (ObjectUtil.isEmpty(smsTemplatex)) {
					SmsTemplate smsTemplate = new SmsTemplate();
					smsTemplate.setName(templateName);
					smsTemplate.setTemplate(templateContent);
					smsTemplate.setIsActive('Y');
					smsTemplate.setCreationInfo(getUsername());
					smsTemplate.setBranchId(getBranchId());
					smsService.addSmsTemplate(smsTemplate);
					success = "1";
					message = getText("message.saved");
				} else {
					success = "0";
					message = getText("message.templateNameExist");
				}
			} else {
				if (ObjectUtil.isLong(templateId)) {
					SmsTemplate smsTemplate = smsService.findSmsTemplateById(Long.parseLong(templateId));
					SmsTemplate smsTemplatex = smsService.findSmsTemplateByName(templateName);
					if (ObjectUtil.isEmpty(smsTemplatex) || smsTemplatex.getId() == smsTemplate.getId()) {
						smsTemplate.setName(templateName);
						smsTemplate.setTemplate(templateContent);
						smsTemplate.setUpdationInfo(getUsername());
						smsService.editSmsTemplate(smsTemplate);
						success = "1";
						message = getText("message.saved");
					} else {
						success = "0";
						message = getText("message.templateNameExist");
					}
				}
			}
		}
		try {
			jsonDataObject = new JsonDataObject(1L, success, message, "");
			JsonObject n = new JsonObject();
			jsonDataObject.setData(n);
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Find template.
	 * 
	 * @return the string
	 */
	public String findTemplate() {

		try {
			String success = "0", message = "";
			SmsTemplate smsTemplate = new SmsTemplate();
			if (!StringUtil.isEmpty(templateId) && ObjectUtil.isLong(templateId)) {
				smsTemplate = smsService.findSmsTemplateById(Long.parseLong(templateId));
				success = "1";
			} else {
				success = "0";
				message = getText("message.templateNotFound");
			}
			jsonObject.put("success", success);
			jsonObject.put("message", message);
			jsonDataObject = new JsonDataObject(1L, success, message, smsTemplate);
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * List template.
	 * 
	 * @return the string
	 */
	public String listTemplate() {

		try {
			List<SmsTemplate> smsTemplates = smsService.listSmsTemplates();
			jsonDataObject = new JsonDataObject(1L, "1", "", smsTemplates);
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void listTemplates() {

		try {
			List<SmsTemplate> smsTemplates = smsService.listSmsTemplates();
			List<org.json.simple.JSONObject> jsonObjects = new ArrayList<org.json.simple.JSONObject>();
			smsTemplates.stream().forEach(obj -> {
				org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
				jsonObject.put("templateName", obj.getName());
				jsonObject.put("message", obj.getTemplate());
				jsonObject.put("id", obj.getId());
				jsonObjects.add(jsonObject);
			});
			printAjaxResponse(jsonObjects, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listGroup() throws Exception {

		List<Object[]> smsGroupHeaders = smsService.listSmsGroupHeader();
		List<org.json.simple.JSONObject> jsonObjects = new ArrayList<org.json.simple.JSONObject>();

		smsGroupHeaders.stream().forEach(obj -> {
			org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
			jsonObject.put("groupName", obj[1]);
			jsonObject.put("id", obj[0]);
			jsonObjects.add(jsonObject);
		});

		printAjaxResponse(jsonObjects, "text/html");
	}

	public Map<String, String> getGroupList() {
		Map<String, String> groupList = new LinkedHashMap<>();
		List<Object[]> smsGroupHeaders = smsService.listSmsGroupHeader();

		groupList = smsGroupHeaders.stream()
				.collect(Collectors.toMap(objKey -> String.valueOf(objKey[0]), objVal -> String.valueOf(objVal[1])));
		return groupList;
	}

	/**
	 * Find group.
	 * 
	 * @return the string
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void findGroup() throws Exception {

		List<org.json.simple.JSONObject> jsonObjects = new ArrayList<org.json.simple.JSONObject>();
		if (!StringUtil.isEmpty(groupId) && ObjectUtil.isLong(groupId)) {
			SmsGroupHeader smsGroupHeader = smsService.findGroupHeaderById(Long.parseLong(groupId));
			if (!ObjectUtil.isEmpty(smsGroupHeader)) {
				org.json.simple.JSONObject groupNameJson = new org.json.simple.JSONObject();
				groupNameJson.put("groupName", smsGroupHeader.getName());
				jsonObjects.add(groupNameJson);

				Set<SmsGroupDetail> groupDetails = smsGroupHeader.getSmsGroupDetails();

				groupDetails.stream().filter(obj -> !ObjectUtil.isEmpty(obj)).forEach(obj -> {
					org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
					jsonObject.put("profileId", obj.getProfileId());
					jsonObject.put("profileType", obj.getProfileType());
					jsonObjects.add(jsonObject);
				});
			}
		}
		printAjaxResponse(jsonObjects, "text/html");
	}

	/**
	 * Remove template.
	 * 
	 * @return the string
	 */
	public String removeTemplate() {

		SmsTemplate smsTemplate = smsService.findSmsTemplateById(Long.parseLong(templateId));
		smsService.removeSmsTemplate(smsTemplate);
		try {
			jsonDataObject = new JsonDataObject(1L, "1", getText("msg.removed"), "");
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send sms.
	 * 
	 * @return the string
	 */
	public String sendSms() {

		String success = "0", statusMsg = "";
		try {
			LOGGER.info("send Sms");
			int smsType = SMSHistory.SMS_SINGLE;
			smsHistory = new SMSHistory();
			smsHistory.setReceiverMobNo(mobileNos);
			smsHistory.setMessage(message);
			if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())
					&& smsHistory.getReceiverMobNo().contains(COMMA_SEPARATOR)) {
				smsType = SMSHistory.SMS_BULK;
			}
			String providerResponse = null;
			int invalidJobIdCount = 0;
			// do {
			providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(), smsHistory.getMessage());

			boolean isAllowedToSave = false;
			String status = null;
			String descrption = null;

			LOGGER.info("Fast Alert Response : " + providerResponse);
			if (!StringUtil.isEmpty(providerResponse)) {
				JSONObject respObj = new JSONObject(providerResponse);
				if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
					// if (!respObj.toString().contains(ISMSService.ERROR_CODE))
					// {
					if (respObj.get(ISMSService.ERROR_CODE).toString() == "0") {
						isAllowedToSave = true;
						statusMsg = getText("smsAlert.success");
					} else {
						statusMsg = getText("messgae.error");
					}
				} else {

					if (!respObj.has(ISMSService.ERROR)) {
						isAllowedToSave = true;
						statusMsg = getText("smsAlert.success");
						if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)
								&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
							smsHistory.setUuid(respObj.getString("batch_id"));
						}
					} else {
						statusMsg = getText("messgae.error");
						if (!getCurrentTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)
								&& !getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
							smsHistory.setUuid(respObj.getString("batch_id"));
						}
					}
				}
			}

			if (isAllowedToSave) {

				// Logic to save request & responses

				status = ISMSService.SMS_STATUS_DELIVERED;
				descrption = status;

				ESESystem system = preferencesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
					// smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
					smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
				}
				smsHistory.setSmsType(smsType);
				smsHistory.setStatusz(status);
				smsHistory.setStatusMsg(descrption);
				smsHistory.setCreationInfo(getUsername());
				smsHistory.setSenderMobNo(mobileNos);

				smsService.addSMSHistory(smsHistory);

			} else {

				status = "error";
				descrption = providerResponse;
				// Logic to save request & responses

				ESESystem system = preferencesService.findPrefernceById("1");
				if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
					// smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
					smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
				}
				smsHistory.setSmsType(smsType);
				smsHistory.setStatusz(status);
				smsHistory.setStatusMsg(descrption);
				smsHistory.setSenderMobNo(mobileNos);
				// smsHistory.setUuid(jobId);
				smsHistory.setCreationInfo(getUsername());
				// smsHistory.setSmsHistoryDetails(new
				// LinkedHashSet<SMSHistoryDetail>());
				// Set<SMSHistoryDetail> smsHistoryDetails = new
				// LinkedHashSet<SMSHistoryDetail>();
				/*
				 * if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())) {
				 * String[] receiverMobileNos =
				 * smsHistory.getReceiverMobNo().split(COMMA_SEPARATOR); for
				 * (String number : receiverMobileNos) { // Build detail objects
				 * SMSHistoryDetail smsHistoryDetail = new SMSHistoryDetail();
				 * smsHistoryDetail.setCreationInfo(getUsername());
				 * smsHistoryDetail.setReceiverNo(number); // JSONObject object
				 * = messagesMap.get(number); String smsStatus = "Failure"; Date
				 * sentAt = null;
				 * 
				 * smsHistoryDetail.setStatusz(smsStatus);
				 * smsHistoryDetail.setSendAt(sentAt);
				 * smsHistoryDetail.setSmsHistory(smsHistory);
				 * smsHistoryDetail.setCreationInfo(getUsername());
				 * smsHistoryDetail.setReceiverType(SmsGroupDetail.
				 * PROFILE_TYPE_OTHER);
				 * smsHistory.getSmsHistoryDetails().add(smsHistoryDetail); } }
				 */
				// smsHistory.setSmsHistoryDetails(smsHistoryDetails);
				smsService.addSMSHistory(smsHistory);
				// success = "1";
				// statusMsg = "msg.msgSent";

			}

			/*
			 * if (isInvalidJobIdResponse(providerResponse)) {
			 * invalidJobIdCount++; } else { invalidJobIdCount =
			 * INVALID_JOB_ID_LOOP_COUNT; }
			 */

			// } while (invalidJobIdCount < INVALID_JOB_ID_LOOP_COUNT);

			if (getFieldErrors().size() == 0 && getActionErrors().size() == 0)
				resetFields();

			jsonDataObject = new JsonDataObject(1L, success, statusMsg, "");
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Send group sms.
	 * 
	 * @return the string
	 */
	public String sendGroupSms() {

		LOGGER.info("send Sms");
		String success = "0", statusMsg = "";
		try {
			if (!StringUtil.isEmpty(groupIds)) {
				List<String> grpIds = StringUtil.split(groupIds, ",");
				if (grpIds.size() > 0) {
					List<SmsGroupHeader> smsGroupHeaders = smsService.listGroupHeadersByIds(grpIds);

					String combainedNos = "";
					Set<SMSHistoryDetail> smsHistoryDetails = new LinkedHashSet<SMSHistoryDetail>();

					List<SmsGroupDetail> smsGroupDetails = smsService.listGroupDetailByGroupId(grpIds);
					List<String> litcode = smsGroupDetails.stream().map(SmsGroupDetail::getProfileId)
							.collect(Collectors.toList());
					System.out.println(litcode);

					LinkedHashMap<Long, Farmer> famerMap = new LinkedHashMap<>();
					List<Farmer> farmers = farmerService.listFarmerByIds(litcode);
					farmers.stream().forEach(farmer -> {
						famerMap.put(farmer.getId(), farmer);
					});
					for (SmsGroupHeader smsGroupHeader : smsGroupHeaders) {
						String dynamicMsgContent = "";
						Set<SmsGroupDetail> groupDetails = smsGroupHeader.getSmsGroupDetails();
						for (SmsGroupDetail smsGroupDetail : groupDetails) {
							String receiverNo = "";
							if (smsGroupDetail.getProfileType().equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_FARMER)) {
								/*
								 * Farmer farmer = farmerService
								 * .findFarmerById(Long.valueOf(smsGroupDetail.
								 * getProfileId()));
								 */
								if (!StringUtil.isEmpty(
										famerMap.get(Long.valueOf(smsGroupDetail.getProfileId())) != null ? famerMap
												.get(Long.valueOf(smsGroupDetail.getProfileId())).getMobileNumber()
												: "")) {
									combainedNos += famerMap.get(Long.valueOf(smsGroupDetail.getProfileId()))
											.getMobileNumber() + ",";
									receiverNo = famerMap.get(Long.valueOf(smsGroupDetail.getProfileId()))
											.getMobileNumber();
								}
								if (message.contains(FIRST_NAME_PATTERN) || message.contains(VILLAGE_NAME_PATTERN)
										|| message.contains(GROUP_NAME_PATTERN)) {
									dynamicMsgContent = message;
									if (message.contains(FIRST_NAME_PATTERN)) {
										dynamicMsgContent = dynamicMsgContent.replace(FIRST_NAME_PATTERN, famerMap
												.get(Long.valueOf(smsGroupDetail.getProfileId())).getFirstName());
									}
									if (message.contains(VILLAGE_NAME_PATTERN)) {
										dynamicMsgContent = dynamicMsgContent.replace(VILLAGE_NAME_PATTERN,
												famerMap.get(Long.valueOf(smsGroupDetail.getProfileId())).getVillage()
														.getName());
									}
									if (message.contains(GROUP_NAME_PATTERN)) {
										dynamicMsgContent = dynamicMsgContent.replace(GROUP_NAME_PATTERN,
												famerMap.get(Long.valueOf(smsGroupDetail.getProfileId())).getSamithi()
														.getName());
									}
								}

							} else if (smsGroupDetail.getProfileType()
									.equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_FIELD_STAFF)) {
								Agent agent = agentService.findAgent(Long.valueOf(smsGroupDetail.getProfileId()));
								if (!StringUtil.isEmpty(agent.getContactInfo().getMobileNumber())) {
									combainedNos += agent.getContactInfo().getMobileNumber() + ",";
									receiverNo = agent.getContactInfo().getMobileNumber();
								}
							} else if (smsGroupDetail.getProfileType()
									.equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_USER)) {
								User user = userService.findUser(Long.valueOf(smsGroupDetail.getProfileId()));
								if (!StringUtil.isEmpty(user.getContactInfo().getMobileNumber())) {
									combainedNos += user.getContactInfo().getMobileNumber() + ",";
									receiverNo = user.getContactInfo().getMobileNumber();
								}
							}
							if (!StringUtil.isEmpty(receiverNo)) {
								SMSHistoryDetail smsHistoryDetail = new SMSHistoryDetail();
								smsHistoryDetail.setCreationInfo(getUsername());
								smsHistoryDetail.setReceiverNo(receiverNo);
								smsHistoryDetail.setReceiverType(smsGroupDetail.getProfileType());
								smsHistoryDetail.setReceiverId(String.valueOf(smsGroupDetail.getProfileId()));
								smsHistoryDetail.setSmsHistory(smsHistory);
								smsHistoryDetail.setCreationInfo(getUsername());
								smsHistoryDetail.setGroupId(String.valueOf(smsGroupDetail.getSmsGroup().getId()));
								smsHistoryDetail.setMessage(dynamicMsgContent);
								smsHistoryDetails.add(smsHistoryDetail);
							}
						}
					}

					int smsType = SMSHistory.SMS_SINGLE;
					smsHistory = new SMSHistory();
					smsHistory.setReceiverMobNo(combainedNos);
					smsHistory.setMessage(message);
					if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())
							&& smsHistory.getReceiverMobNo().contains(COMMA_SEPARATOR)) {
						smsType = SMSHistory.SMS_BULK;
					}
					String providerResponse = null;
					int invalidJobIdCount = 0;
					// do {
					if (message.contains(FIRST_NAME_PATTERN) || message.contains(VILLAGE_NAME_PATTERN)
							|| message.contains(GROUP_NAME_PATTERN)) {
						JSONObject jsonObject = new JSONObject();
						int totalSmsAllow = 5000;
						int size = smsHistoryDetails.size()/totalSmsAllow+1;
						List<Set<SMSHistoryDetail>> theSets = new ArrayList<Set<SMSHistoryDetail>>(size);
						for (int i = 0; i < size; i++) {
						    theSets.add(new HashSet<SMSHistoryDetail>());
						}
						
						int index = 0;
						for (SMSHistoryDetail object : smsHistoryDetails) {
						    theSets.get(index++ % size).add(object);
						}
					    for(int i = 0; i < theSets.size(); i++)
					    {
					    	JSONArray jsonArray = new JSONArray();
					        Set<SMSHistoryDetail> s = theSets.get(i);
					        s.stream().forEach(smsHistoryDetail -> {
					        JSONObject jsonParameter = new JSONObject();
							try {
								jsonParameter.put("number", smsHistoryDetail.getReceiverNo());
								jsonParameter.put("text", smsHistoryDetail.getMessage());
								jsonArray.put(jsonParameter);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						});
						System.out.println(jsonArray.toString());
						if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
							
							providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(),
									jsonArray.toString());
						}
						else{
							jsonObject.put("messages", jsonArray);
							System.out.println(jsonObject.toString());
						}
					    }								
					} else {
						if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
							String[] numbers1 = smsHistory.getReceiverMobNo().split(",");
							int totalSmsAllow = 5000;
							String[][] chunked = splitArray(numbers1, totalSmsAllow);
							/*
							 * Arrays.stream(chunked) .map(Arrays::toString)
							 * .forEach(System.out::println);
							 */
							for (int i = 0; i < chunked.length; i++) {
								JSONArray jsonArray = new JSONArray();
								for (String a : chunked[i]) {
									JSONObject jsonParameter = new JSONObject();
									jsonParameter.put("Number", a);
									jsonParameter.put("Text", smsHistory.getMessage());
									jsonArray.put(jsonParameter);
								}

								providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(),
										jsonArray.toString());
							}
						} else {
							providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(),
									smsHistory.getMessage());
						}
					}
					boolean isAllowedToSave = false;
					if (!StringUtil.isEmpty(providerResponse)) {
						JSONObject respObj = new JSONObject(providerResponse);
						if (getCurrentTenantId().equalsIgnoreCase(ESESystem.KENYA_FPO)) {
							if (respObj.get(ISMSService.ERROR_CODE).toString() == "0") {
								isAllowedToSave = true;
								statusMsg = getText("smsAlert.success");
							} else {
								statusMsg = getText("messgae.error");
							}
						} else {
							if (!respObj.has(ISMSService.ERROR)) {
								isAllowedToSave = true;
								statusMsg = getText("smsAlert.success");
							} else {
								statusMsg = getText("messgae.error");
							}
						}
					}
					String status = null;
					String descrption = null;
					LOGGER.info("Fast Alert Response : " + providerResponse);
					if (!StringUtil.isEmpty(providerResponse)) {
						try {

							if (isAllowedToSave) {
								status = ISMSService.SMS_STATUS_DELIVERED;
								descrption = status;

								ESESystem system = preferencesService.findPrefernceById(ESESystem.SYSTEM_ESE);
								if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
									smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
									smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
								}
								smsHistory.setSmsType(smsType);
								smsHistory.setStatusz(status);
								smsHistory.setStatusMsg(descrption);
								// smsHistory.setUuid(jobId);
								smsHistory.setCreationInfo(getUsername());

								/*
								 * for (SMSHistoryDetail smsHistoryDetail :
								 * smsHistoryDetails) { String smsStatus = "";
								 * Date sentAt = new Date();
								 * smsHistoryDetail.setStatusz(smsStatus);
								 * smsHistoryDetail.setSendAt(sentAt); }
								 * smsHistory.setSmsHistoryDetails(
								 * smsHistoryDetails);
								 */
								smsService.addSMSHistory(smsHistory);
								// success = "1";
								// statusMsg = "msg.msgSent";
							} else {

								status = ISMSService.SMS_STATUS_FAIL;
								descrption = providerResponse;
								ESESystem system = preferencesService.findPrefernceById(ESESystem.SYSTEM_ESE);
								if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
									smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
									smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
								}
								smsHistory.setSmsType(smsType);
								smsHistory.setStatusz(status);
								smsHistory.setStatusMsg(descrption);
								smsHistory.setCreationInfo(getUsername());

								smsService.addSMSHistory(smsHistory);

							}

							// smsHistory.setUpdationInfo(getUsername());
							// smsService.editSMSHistory(smsHistory);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}

			if (getFieldErrors().size() == 0 && getActionErrors().size() == 0)
				resetFields();

			jsonDataObject = new JsonDataObject(1L, success, statusMsg, "");
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	private static List<Set<SMSHistoryDetail>> partitionSet(Set<SMSHistoryDetail> smsHistoryDetails, int     partitionSize)
	{
	    List<Set<SMSHistoryDetail>> list = new ArrayList<>();
	    int setSize = smsHistoryDetails.size();
	    Set<SMSHistoryDetail> smsHistoryDetail= new LinkedHashSet<SMSHistoryDetail>();
	    Iterator iterator = smsHistoryDetails.iterator();

	    while(iterator.hasNext())
	    {
	        Set newSet = new HashSet();
	        for(int j = 0; j <= partitionSize && iterator.hasNext(); j++)
	        {
	        	smsHistoryDetail.add((SMSHistoryDetail) iterator.next());
	            newSet.add(smsHistoryDetail);
	        }
	        list.add(newSet);
	    }
	    return list;
	}
	public static String[][] splitArray(String[] input, int chunkSize) {
		return IntStream.iterate(0, i -> i + chunkSize).limit((long) Math.ceil((double) input.length / chunkSize))
				.mapToObj(
						j -> Arrays.copyOfRange(input, j, j + chunkSize > input.length ? input.length : j + chunkSize))
				.toArray(String[][]::new);
	}
	
	
	

	/**
	 * Send selected contacts sms.
	 * 
	 * @return the string
	 */
	public String sendSelectedContactsSms() {

		LOGGER.info("send Sms");
		String success = "0", statusMsg = "";
		try {
			List<String> farmersIds = StringUtil.split(selectedFarmerIds, ",");
			List<String> fieldStaffIds = StringUtil.split(selectedFieldStaffIds, ",");
			List<String> userIds = StringUtil.split(selectedUserIds, ",");
			if (farmersIds.size() == 0 && fieldStaffIds.size() == 0 && userIds.size() == 0) {
				success = "0";
				statusMsg = getText("message.noContacts");
			} else {
				farmersIds = new ArrayList<String>(new LinkedHashSet<String>(farmersIds));
				fieldStaffIds = new ArrayList<String>(new LinkedHashSet<String>(fieldStaffIds));
				userIds = new ArrayList<String>(new LinkedHashSet<String>(userIds));
				String combainedNos = "";
				Set<SMSHistoryDetail> smsHistoryDetails = new LinkedHashSet<SMSHistoryDetail>();
				for (String farmerId : farmersIds) {
					Farmer farmer = farmerService.findFarmerById(Long.parseLong(farmerId));
					String receiverNo = "";
					if (!StringUtil.isEmpty(farmer.getMobileNumber())) {
						combainedNos += farmer.getMobileNumber() + ",";
						receiverNo = farmer.getMobileNumber();
						SMSHistoryDetail smsHistoryDetail = new SMSHistoryDetail();
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverNo(receiverNo);
						smsHistoryDetail.setSmsHistory(smsHistory);
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverId(String.valueOf(farmer.getId()));
						smsHistoryDetail.setReceiverType(SmsGroupDetail.PROFILE_TYPE_FARMER);
						smsHistoryDetails.add(smsHistoryDetail);
					}
				}
				for (String fieldStaffId : fieldStaffIds) {
					Agent agent = agentService.findAgent(Long.parseLong(fieldStaffId));
					String receiverNo = "";
					if (!StringUtil.isEmpty(agent.getContactInfo().getMobileNumber())) {
						combainedNos += agent.getContactInfo().getMobileNumber() + ",";
						receiverNo = agent.getContactInfo().getMobileNumber();
						SMSHistoryDetail smsHistoryDetail = new SMSHistoryDetail();
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverNo(receiverNo);
						smsHistoryDetail.setSmsHistory(smsHistory);
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverId(String.valueOf(agent.getId()));
						smsHistoryDetail.setReceiverType(SmsGroupDetail.PROFILE_TYPE_FIELD_STAFF);
						smsHistoryDetails.add(smsHistoryDetail);
					}
				}
				for (String userId : userIds) {
					User user = userService.findUser(Long.parseLong(userId));
					String receiverNo = "";
					if (!StringUtil.isEmpty(user.getContactInfo().getMobileNumber())) {
						combainedNos += user.getContactInfo().getMobileNumber() + ",";
						receiverNo = user.getContactInfo().getMobileNumber();
						SMSHistoryDetail smsHistoryDetail = new SMSHistoryDetail();
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverNo(receiverNo);
						smsHistoryDetail.setSmsHistory(smsHistory);
						smsHistoryDetail.setCreationInfo(getUsername());
						smsHistoryDetail.setReceiverId(String.valueOf(user.getId()));
						smsHistoryDetail.setReceiverType(SmsGroupDetail.PROFILE_TYPE_USER);
						smsHistoryDetails.add(smsHistoryDetail);
					}
				}

				int smsType = SMSHistory.SMS_SINGLE;
				smsHistory = new SMSHistory();
				smsHistory.setReceiverMobNo(combainedNos);
				smsHistory.setMessage(message);
				if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())
						&& smsHistory.getReceiverMobNo().contains(COMMA_SEPARATOR)) {
					smsType = SMSHistory.SMS_BULK;
				}
				String providerResponse = null;
				int invalidJobIdCount = 0;
				// do {
				providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(), smsHistory.getMessage());
				if (isInvalidJobIdResponse(providerResponse)) {
					invalidJobIdCount++;
				} else {
					invalidJobIdCount = INVALID_JOB_ID_LOOP_COUNT;
				}

				// } while (invalidJobIdCount < INVALID_JOB_ID_LOOP_COUNT);
				LOGGER.info("Fast Alert Response : " + providerResponse);
				if (!ObjectUtil.isEmpty(providerResponse)) {
					try {
						JSONObject respObj = new JSONObject(providerResponse);
						JSONObject fastAlerts = respObj.getJSONObject(ISMSService.SMS_FASTALERTS);

						Object statusObj = fastAlerts.has(ISMSService.SMS_RES_STATUS)
								? fastAlerts.get(ISMSService.SMS_RES_STATUS) : null;
						Object descriptionObj = fastAlerts.has(ISMSService.SMS_RES_DESCR)
								? fastAlerts.get(ISMSService.SMS_RES_DESCR) : null;
						Object uuidObj = fastAlerts.has(ISMSService.SMS_RES_UUID)
								? fastAlerts.get(ISMSService.SMS_RES_UUID) : null;
						Object smsMessagesObj = fastAlerts.has(ISMSService.SMS_MESSAGES)
								? fastAlerts.get(ISMSService.SMS_MESSAGES) : null;
						Object jobIdObj = fastAlerts.has(ISMSService.SMS_JOB_ID)
								? fastAlerts.get(ISMSService.SMS_JOB_ID) : null;

						String status = (!ObjectUtil.isEmpty(statusObj)) ? String.valueOf(statusObj) : null;
						String descrption = (!ObjectUtil.isEmpty(descriptionObj)) ? String.valueOf(descriptionObj)
								: null;

						String jobId = (!ObjectUtil.isEmpty(jobIdObj)) ? String.valueOf(jobIdObj) : null;

						JSONArray messages = (!ObjectUtil.isEmpty(smsMessagesObj)
								&& smsMessagesObj instanceof JSONArray) ? (JSONArray) smsMessagesObj : null;

						boolean isAllowedToSave = false;
						boolean isDnDFiltered = false;
						boolean isInvalidJobId = false;
						Map<String, JSONObject> messagesMap = new LinkedHashMap<String, JSONObject>();
						if (!ObjectUtil.isEmpty(messages)) {
							for (int i = 0; i < messages.length(); i++) {
								JSONObject msg = (JSONObject) messages.get(i);
								if (!ObjectUtil.isEmpty(msg)) {
									messagesMap.put(msg.getString(ISMSService.SMS_PAR_MSISDN), msg);
								}
							}
							status = ISMSService.SMS_STATUS_SUBMITTED;
							descrption = status;
							// Considered as success
							success = "1";
							statusMsg = getText("smsAlert.success");
							isAllowedToSave = true;
						} else {
							if (ISMSService.SMS_ERROR.equalsIgnoreCase(status)
									&& (ISMSService.SINGLE_SMS_DND_DESCRIPTION.equalsIgnoreCase(descrption)
											|| ISMSService.BULK_SMS_DND_DESCRIPTION.equalsIgnoreCase(descrption))) {
								// Considered as dnd filtered
								isAllowedToSave = true;
								isDnDFiltered = true;
								addFieldError("smsHistory.receiverMobNo", getText("receiverMobNo.dndFiltered"));
								success = "0";
								statusMsg = getText("receiverMobNo.dndFiltered");
							} else if (ISMSService.SMS_ERROR.equalsIgnoreCase(status)
									&& (ISMSService.SMS_INVALID_JOB_ID_DESC.equalsIgnoreCase(descrption))) {
								// Considered as Submited for invalid job id
								isAllowedToSave = true;
								isInvalidJobId = true;
								// status = ISMSService.SMS_INVALID_JOB_ID;
								// descrption =
								// ISMSService.SMS_INVALID_JOB_ID_DESC;
								status = ISMSService.SMS_STATUS_SUBMITTED;
								descrption = status;
								addActionError(descrption);
								success = "0";
								statusMsg = getText("smsAlert.success");// descrption;
							} else {
								// Considered as un expected error
								isAllowedToSave = false;
								addActionError(descrption);
								success = "0";
								statusMsg = descrption;
							}
						}

						if (isAllowedToSave) {
							// Logic to save request & responses

							ESESystem system = preferencesService.findPrefernceById(ESESystem.SYSTEM_ESE);
							if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
								smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
								smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
							}
							smsHistory.setSmsType(smsType);
							smsHistory.setStatusz(status);
							smsHistory.setStatusMsg(descrption);
							smsHistory.setUuid(jobId);
							smsHistory.setCreationInfo(getUsername());

							for (SMSHistoryDetail smsHistoryDetail : smsHistoryDetails) {
								// Build detail objects
								JSONObject object = messagesMap.get(smsHistoryDetail.getReceiverNo());
								String smsStatus = "";
								Date sentAt = null;
								if (!ObjectUtil.isEmpty(object)) {
									smsStatus = object.getString(ISMSService.SMS_RES_STATUS);
									sentAt = DateUtil.convertStringToDate(
											removeTimeZoneColon(object.getString(ISMSService.SMS_SENT_AT)),
											DateUtil.DATE_FORMAT_3);
								} else if (isDnDFiltered) {
									smsStatus = ISMSService.SMS_STATUS_DND_FILTERED;
									sentAt = new Date();
								} else if (isInvalidJobId) {
									smsStatus = ISMSService.SMS_STATUS_SUBMITTED;// ISMSService.SMS_INVALID_JOB_ID;
									sentAt = new Date();
								}
								smsHistoryDetail.setStatusz(smsStatus);
								smsHistoryDetail.setSendAt(sentAt);
							}
							smsHistory.setSmsHistoryDetails(smsHistoryDetails);
							smsService.addSMSHistory(smsHistory);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			if (getFieldErrors().size() == 0 && getActionErrors().size() == 0)
				resetFields();

			jsonDataObject = new JsonDataObject(1L, success, statusMsg, "");
			sendResponse(gson.toJson(jsonDataObject));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Mobile nos count.
	 * 
	 * @return the string
	 */
	public String populateMobileNumberCountByGroups() {

		try {
			long count = 0;
			if (!StringUtil.isEmpty(selectedGroups)) {
				List<String> groupsIds = StringUtil.split(selectedGroups, ",");
				count = smsService.findGroupsMobileNumbersCount(groupsIds);
			}

			jsonDataObject = new JsonDataObject(1L, "1", "", count);
			sendResponse(gson.toJson(jsonDataObject));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("rawtypes")
	public String data() throws Exception {

		LOGGER.info("ReportData");
		Map<String, String> searchRecord = getSearchAutoText();
		SMSHistoryDetail filter = new SMSHistoryDetail();

		// grid filter logic
		if (!StringUtil.isEmpty(searchRecord.get("receiverName"))) {
			filter.setReceiverName(searchRecord.get("receiverName"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("receiverMobNo"))) {
			filter.setReceiverNo(searchRecord.get("receiverMobNo"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("receiverType"))) {
			filter.setReceiverType(searchRecord.get("receiverType"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("receiverGroupName"))) {
			filter.setReceiverGroupName(searchRecord.get("receiverGroupName"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("user"))) {
			filter.setCreatedUser(searchRecord.get("user"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			filter.setStatusz(searchRecord.get("status"));
		}

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());
		return sendJQGridJSONResponse(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.action.ESEBaseAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject toJSON(Object obj) {

		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		SMSHistoryDetail smsHistoryDetail = (SMSHistoryDetail) obj;
		/*
		 * if (StringUtil.isEmpty(smsHistoryDetail.getReceiverId())) {
		 * rows.add(getText("na")); } else { if
		 * (smsHistoryDetail.getReceiverType()
		 * .equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_FARMER)) { Farmer
		 * farmer = farmerService
		 * .findFarmerById(Long.parseLong(smsHistoryDetail.getReceiverId())); if
		 * (!ObjectUtil.isEmpty(farmer)) { rows.add(farmer.getName() + " - " +
		 * farmer.getFarmerId()); } else { rows.add(getText("NA")); } } else if
		 * (smsHistoryDetail.getReceiverType()
		 * .equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_FIELD_STAFF)) { Agent
		 * agent = agentService
		 * .findAgent(Long.parseLong(smsHistoryDetail.getReceiverId())); if
		 * (!ObjectUtil.isEmpty(agent)) {
		 * rows.add(ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" :
		 * agent.getPersonalInfo().getFirstName() + "  " +
		 * agent.getPersonalInfo().getLastName()); } else {
		 * rows.add(getText("NA")); } } else if
		 * (smsHistoryDetail.getReceiverType()
		 * .equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_USER)) { User user =
		 * userService.findUser(Long.parseLong(smsHistoryDetail.getReceiverId())
		 * ); if (!ObjectUtil.isEmpty(user)) {
		 * rows.add(StringUtil.isEmpty(user.getUsername()) ? "" :
		 * user.getUsername()); } else { rows.add(getText("NA")); } } else if
		 * (smsHistoryDetail.getReceiverType()
		 * .equalsIgnoreCase(SmsGroupDetail.PROFILE_TYPE_OTHER)) {
		 * rows.add(getText("na")); } }
		 */
		rows.add(smsHistoryDetail.getReceiverNo());
		/*
		 * rows.add(smsHistoryDetail.getReceiverType()); if
		 * (StringUtil.isEmpty(smsHistoryDetail.getGroupId())) {
		 * rows.add(getText("na")); } else { SmsGroupHeader groupHeader =
		 * smsService
		 * .findGroupHeaderById(Long.parseLong(smsHistoryDetail.getGroupId()));
		 * rows.add(groupHeader.getName()); }
		 */
		rows.add(DateUtil.convertDateToString(smsHistoryDetail.getSendAt(), DateUtil.DATE_TIME_REPORT_FORMAT));
		rows.add(smsHistoryDetail.getSmsHistory().getUpdatedUser());
		rows.add("<div id=\"status_" + smsHistoryDetail.getId() + "\"> " + smsHistoryDetail.getStatusz() + " </div>");
		if (ISMSService.SMS_STATUS_SUBMITTED.equalsIgnoreCase(smsHistoryDetail.getStatusz())
				|| ISMSService.SMS_INVALID_JOB_ID.equalsIgnoreCase(smsHistoryDetail.getStatusz())
				|| ISMSService.SMS_SYSTEM_ERROR.equalsIgnoreCase(smsHistoryDetail.getStatusz())) {
			rows.add("<button id=\"dlvryStatusBtn_" + smsHistoryDetail.getId()
					+ "\" type=\"button\" class=\"btn btn-primary\" onclick=\"updateDelivery("
					+ smsHistoryDetail.getSmsHistory().getId() + "," + +smsHistoryDetail.getId() + ")\">"
					+ getText("updateStatus") + "</button>");
		} else {
			rows.add("<button id=\"dlvryStatusBtn_" + smsHistoryDetail.getId()
					+ "\" type=\"button\" class=\"btn btn-primary\" disabled=\"true\" onclick=\"updateDelivery("
					+ smsHistoryDetail.getSmsHistory().getId() + "," + +smsHistoryDetail.getId() + ")\">"
					+ getText("updateStatus") + "</button>");
		}
		jsonObject.put("id", smsHistoryDetail.getId());

		jsonObject.put("cell", rows);
		return jsonObject;
	}

	// ****************GETTERS & SETTERS****************************
	/**
	 * Gets the sms history.
	 * 
	 * @return the sms history
	 */
	public SMSHistory getSmsHistory() {

		return smsHistory;
	}

	/**
	 * Sets the sms history.
	 * 
	 * @param smsHistory
	 *            the new sms history
	 */
	public void setSmsHistory(SMSHistory smsHistory) {

		this.smsHistory = smsHistory;
	}

	/**
	 * Gets the selected farmer ids.
	 * 
	 * @return the selected farmer ids
	 */
	public String getSelectedFarmerIds() {

		return selectedFarmerIds;
	}

	/**
	 * Sets the selected farmer ids.
	 * 
	 * @param selectedFarmerIds
	 *            the new selected farmer ids
	 */
	public void setSelectedFarmerIds(String selectedFarmerIds) {

		this.selectedFarmerIds = selectedFarmerIds;
	}

	/**
	 * Gets the selected field staff ids.
	 * 
	 * @return the selected field staff ids
	 */
	public String getSelectedFieldStaffIds() {

		return selectedFieldStaffIds;
	}

	/**
	 * Sets the selected field staff ids.
	 * 
	 * @param selectedFieldStaffIds
	 *            the new selected field staff ids
	 */
	public void setSelectedFieldStaffIds(String selectedFieldStaffIds) {

		this.selectedFieldStaffIds = selectedFieldStaffIds;
	}

	/**
	 * Gets the selected user ids.
	 * 
	 * @return the selected user ids
	 */
	public String getSelectedUserIds() {

		return selectedUserIds;
	}

	/**
	 * Sets the selected user ids.
	 * 
	 * @param selectedUserIds
	 *            the new selected user ids
	 */
	public void setSelectedUserIds(String selectedUserIds) {

		this.selectedUserIds = selectedUserIds;
	}

	/**
	 * Gets the group name.
	 * 
	 * @return the group name
	 */
	public String getGroupName() {

		return groupName;
	}

	/**
	 * Sets the group name.
	 * 
	 * @param groupName
	 *            the new group name
	 */
	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	/**
	 * Gets the group id.
	 * 
	 * @return the group id
	 */
	public String getGroupId() {

		return groupId;
	}

	/**
	 * Sets the group id.
	 * 
	 * @param groupId
	 *            the new group id
	 */
	public void setGroupId(String groupId) {

		this.groupId = groupId;
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
	 * Gets the mobile nos.
	 * 
	 * @return the mobile nos
	 */
	public String getMobileNos() {

		return mobileNos;
	}

	/**
	 * Sets the mobile nos.
	 * 
	 * @param mobileNos
	 *            the new mobile nos
	 */
	public void setMobileNos(String mobileNos) {

		this.mobileNos = mobileNos;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {

		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {

		this.message = message;
	}

	/**
	 * Gets the template id.
	 * 
	 * @return the template id
	 */
	public String getTemplateId() {

		return templateId;
	}

	/**
	 * Sets the template id.
	 * 
	 * @param templateId
	 *            the new template id
	 */
	public void setTemplateId(String templateId) {

		this.templateId = templateId;
	}

	/**
	 * Gets the template name.
	 * 
	 * @return the template name
	 */
	public String getTemplateName() {

		return templateName;
	}

	/**
	 * Sets the template name.
	 * 
	 * @param templateName
	 *            the new template name
	 */
	public void setTemplateName(String templateName) {

		this.templateName = templateName;
	}

	/**
	 * Gets the template content.
	 * 
	 * @return the template content
	 */
	public String getTemplateContent() {

		return templateContent;
	}

	/**
	 * Sets the template content.
	 * 
	 * @param templateContent
	 *            the new template content
	 */
	public void setTemplateContent(String templateContent) {

		this.templateContent = templateContent;
	}

	/**
	 * Gets the group ids.
	 * 
	 * @return the group ids
	 */
	public String getGroupIds() {

		return groupIds;
	}

	/**
	 * Sets the group ids.
	 * 
	 * @param groupIds
	 *            the new group ids
	 */
	public void setGroupIds(String groupIds) {

		this.groupIds = groupIds;
	}

	/**
	 * Gets the receiver type option values.
	 * 
	 * @return the receiver type option values
	 */
	public String getReceiverTypeOptionValues() {

		return receiverTypeOptionValues;
	}

	/**
	 * Sets the receiver type option values.
	 * 
	 * @param receiverTypeOptionValues
	 *            the new receiver type option values
	 */
	public void setReceiverTypeOptionValues(String receiverTypeOptionValues) {

		this.receiverTypeOptionValues = receiverTypeOptionValues;
	}

	/**
	 * Gets the delivery status.
	 * 
	 * @return the delivery status
	 */
	public String getDeliveryStatus() {

		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		try {
			List<SMSHistory> smsHistoryList = new ArrayList<SMSHistory>();
			smsHistoryList.add(smsService.findSmsHistoryById(getId()));
			smsHistoryList = smsService.updateSMSDeliveryStatus(smsHistoryList);
			for (SMSHistory smsHistory1 : smsHistoryList) {
				for (SMSHistoryDetail smsHistoryDetail : smsHistory1.getSmsHistoryDetails()) {
					org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
					jsonObject.put("status", smsHistoryDetail.getStatusz());
					jsonObject.put("smsHistoryDetailId", smsHistoryDetail.getId());
					rows.add(jsonObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printAjaxResponse(rows, "");
		return null;
	}

	/**
	 * Gets the sms history detail id.
	 * 
	 * @return the sms history detail id
	 */
	public long getSmsHistoryDetailId() {

		return smsHistoryDetailId;
	}

	/**
	 * Sets the sms history detail id.
	 * 
	 * @param smsHistoryDetailId
	 *            the new sms history detail id
	 */
	public void setSmsHistoryDetailId(long smsHistoryDetailId) {

		this.smsHistoryDetailId = smsHistoryDetailId;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public Map<Long, String> getSmsTemplateList() {

		List<SmsTemplate> smsTemplates = smsService.listSmsTemplates();
		Map<Long, String> smsTemplateMap = new LinkedHashMap<>();
		smsTemplateMap = smsTemplates.stream().collect(Collectors.toMap(SmsTemplate::getId, SmsTemplate::getName));
		return smsTemplateMap;
	}

	public void populateTemplateMsg() {

		org.json.simple.JSONObject jsonObject2 = new org.json.simple.JSONObject();
		if (!StringUtil.isEmpty(selectedTemplate)) {
			SmsTemplate smsTemplate = smsService.findSmsTemplateById(Long.valueOf(selectedTemplate));
			jsonObject2.put("message", smsTemplate.getTemplate());
			sendAjaxResponse(jsonObject2);
		}
	}

	/*
	 * @SuppressWarnings("unchecked") public void
	 * populateMobileNumberCountByGroups() {
	 * if(!StringUtil.isEmpty(selectedGroups)){ String[]
	 * groupIds=selectedGroups.split(","); long[] ids =new
	 * long[groupIds.length]; for(int i=0;i<groupIds.length;i++){ ids[i] =
	 * Long.valueOf(groupIds[i]); }
	 * 
	 * Integer mobileNumberCount =
	 * smsService.listMappedMobileNumberCountByGroup(ids);
	 * org.json.simple.JSONObject jsonObject2 = new
	 * org.json.simple.JSONObject();
	 * jsonObject2.put("mobileNumberCount",mobileNumberCount);
	 * sendAjaxResponse(jsonObject2); } }
	 */

	public String getSelectedTemplate() {

		return selectedTemplate;
	}

	public void setSelectedTemplate(String selectedTemplate) {

		this.selectedTemplate = selectedTemplate;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String populateFarmersList() throws Exception {
		Locality ld = new Locality();
		State s = new State();
		Municipality m = new Municipality();
		Map data = new LinkedHashMap<>();
		Map<String, String> searchRecord = getJQGridRequestParam();
		Farmer farmer = new Farmer();
		if (!StringUtil.isEmpty(searchRecord.get("SMSSMSfirstName"))) {
			farmer.setFirstName(searchRecord.get("SMSSMSfirstName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSlastName"))) {
			farmer.setLastName(searchRecord.get("SMSlastName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSmobileNumber"))) {
			farmer.setMobileNumber(searchRecord.get("SMSmobileNumber").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("village"))) {
			Village village = new Village();
			farmer.setVillage(village);
			farmer.getVillage().setName(searchRecord.get("village"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("city"))) {
			Municipality city = new Municipality();
			farmer.setCity(city);
			farmer.getCity().setName(searchRecord.get("city"));
		}
		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("district"))) { Locality
		 * locality=new Locality(); farmer.getcity().getl Municipality city =
		 * new Municipality(); farmer.setCity(city);
		 * farmer.getCity().setName(searchRecord.get("city")); }
		 */
		if (!StringUtil.isEmpty(searchRecord.get("state"))) {
			s.setName(searchRecord.get("state"));
			ld.setState(s);
			m.setLocality(ld);
			farmer.setCity(m);
		}
		if (!StringUtil.isEmpty(searchRecord.get("district"))) {
			ld.setName(searchRecord.get("district"));
			m.setLocality(ld);
			farmer.setCity(m);
		}

		if (!StringUtil.isEmpty(searchRecord.get("crop"))) {
			farmer.setCropNames(searchRecord.get("crop"));
		}
		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				farmer.setFilterStatus("status");
				farmer.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else {
				farmer.setFilterStatus("status");
				farmer.setStatus(Farmer.Status.INACTIVE.ordinal());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			farmer.setBranchId(searchRecord.get("branchId").trim());
		}

		List<Object> farmersList = smsService.listFarmers(getSort(), getStartIndex(), getResults(), farmer);
		data.put(RECORDS, smsService.findFarmerMobileNumberCount(getSort(), getStartIndex(), getResults(), farmer));
		data.put(ROWS, farmersList);
		data.put(PAGE_NUMBER, getPage());
		data.put(TAB, FARMER_TAB);

		return sendFarmerJQGridJSONResponse(data);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String sendFarmerJQGridJSONResponse(Map data) throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam();

		org.json.simple.JSONObject gridData = new org.json.simple.JSONObject();
		gridData.put(PAGE, getPage());
		totalRecords = (Integer) data.get(RECORDS);
		gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getResults()))));
		gridData.put(RECORDS, totalRecords);
		List list = (List) data.get(ROWS);
		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		if (list != null) {
			branchIdValue = getBranchId();
			if (StringUtil.isEmpty(branchIdValue)) {
				buildBranchMap();
			}
			if (data.get(TAB).toString().equals(FARMER_TAB)) {
				for (Object record : list) {
					rows.add(toFarmerJSON(record));
				}
			} else if (data.get(TAB).toString().equals(FS_TAB)) {
				for (Object record : list) {
					rows.add(toFieldstaffJSON(record));
				}
			} else if (data.get(TAB).toString().equals(USER_TAB)) {
				for (Object record : list) {
					rows.add(toUserJSON(record));
				}
			}
		}
		if (totalRecords > 0) {
			gridData.put("userdata", userDataToJSON());
		} else {
			gridData.put("userdata", userDataToJSON());
		}
		gridData.put(ROWS, rows);
		//
		PrintWriter out = response.getWriter();
		out.println(gridData.toString());

		return null;
	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject toFarmerJSON(Object obj) {

		Object[] datas = (Object[]) obj;
		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		/*
		 * if (StringUtil.isEmpty(branchIdValue)) {
		 * rows.add(branchesMap.get(country.getBranchId())); }
		 */
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(StringUtil.isEmpty(datas[9]) ? getMainBranchName() : branchesMap.get(datas[9].toString().trim()));
		}
		rows.add(datas[1].toString());
		// rows.add(datas[2].toString());
		if (datas[2] == "" || datas[2] == null) {
			rows.add("");
		} else {
			rows.add(datas[2].toString());
		}
		// rows.add((!StringUtil.isEmpty(datas[2].toString()) &&
		// datas[2].toString()!=null) ? datas[2].toString():"");
		rows.add(datas[3].toString());
		rows.add(datas[4].toString());
		rows.add(datas[5].toString());
		rows.add(datas[6].toString());
		rows.add(datas[7].toString());
		rows.add(datas[10].toString());
		rows.add(getText("status" + datas[8].toString()));
		jsonObject.put("id", datas[0].toString());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject toFieldstaffJSON(Object obj) {

		Object[] datas = (Object[]) obj;
		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		/*
		 * if (StringUtil.isEmpty(branchIdValue)) {
		 * rows.add(branchesMap.get(country.getBranchId())); }
		 */
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(StringUtil.isEmpty(datas[6]) ? getMainBranchName() : branchesMap.get(datas[6].toString().trim()));
		}
		rows.add(datas[1].toString());
		rows.add(datas[2].toString());
		rows.add(datas[3].toString());
		rows.add(datas[4].toString());
		rows.add(getText("status" + datas[5].toString()));
		jsonObject.put("id", datas[0].toString());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public org.json.simple.JSONObject toUserJSON(Object obj) {

		Object[] datas = (Object[]) obj;
		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		org.json.simple.JSONArray rows = new org.json.simple.JSONArray();
		/*
		 * if (StringUtil.isEmpty(branchIdValue)) {
		 * rows.add(branchesMap.get(country.getBranchId())); }
		 */
		if (StringUtil.isEmpty(branchIdValue)) {
			rows.add(StringUtil.isEmpty(datas[6]) ? getMainBranchName() : branchesMap.get(datas[6].toString().trim()));
		}
		rows.add(datas[1].toString());
		rows.add(datas[2].toString());
		rows.add(datas[3].toString());
		rows.add(datas[4].toString());
		rows.add(getText("status" + datas[5].toString()));
		jsonObject.put("id", datas[0].toString());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public String populateFieldStaffList() throws Exception {

		Map data = new LinkedHashMap<>();
		Map<String, String> searchRecord = getJQGridRequestParam();
		Agent agent = new Agent();
		PersonalInfo personalInfo = new PersonalInfo();
		agent.setPersonalInfo(personalInfo);

		if (!StringUtil.isEmpty(searchRecord.get("agentId"))) {
			agent.setProfileId(searchRecord.get("agentId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("SMSfirstName"))) {
			agent.getPersonalInfo().setFirstName(searchRecord.get("SMSfirstName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSlastName"))) {
			agent.getPersonalInfo().setLastName(searchRecord.get("SMSlastName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSmobileNumber"))) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setMobileNumber(searchRecord.get("SMSmobileNumber").trim());
			agent.setContactInfo(contactInfo);
		}

		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				agent.setFilterStatus("status");
				agent.setStatus(Farmer.Status.ACTIVE.ordinal());
			} else {
				agent.setFilterStatus("status");
				agent.setStatus(Farmer.Status.INACTIVE.ordinal());
			}
		}

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			agent.setBranchId(searchRecord.get("branchId").trim());
		}

		List<Object> farmersList = smsService.listAgents(getSort(), getStartIndex(), getResults(), agent);
		data.put(RECORDS, smsService.findAgentMobileNumberCount(getSort(), getStartIndex(), getResults(), agent));
		data.put(ROWS, farmersList);
		data.put(PAGE_NUMBER, getPage());
		data.put(TAB, FS_TAB);

		return sendFarmerJQGridJSONResponse(data);

	}

	@SuppressWarnings("unchecked")
	public String populateUserList() throws Exception {

		Map data = new LinkedHashMap<>();
		Map<String, String> searchRecord = getJQGridRequestParam();
		// Agent agent = new Agent();
		User user = new User();
		PersonalInfo personalInfo = new PersonalInfo();
		user.setPersonalInfo(personalInfo);

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("agentId"))) {
		 * agent.setProfileId(searchRecord.get("agentId").trim()); }
		 */
		if (!StringUtil.isEmpty(searchRecord.get("username"))) {
			user.setUsername(searchRecord.get("username").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("SMSfirstName"))) {
			user.getPersonalInfo().setFirstName(searchRecord.get("SMSfirstName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSlastName"))) {
			user.getPersonalInfo().setLastName(searchRecord.get("SMSlastName").trim());
		}
		if (!StringUtil.isEmpty(searchRecord.get("SMSmobileNumber"))) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setMobileNumber(searchRecord.get("SMSmobileNumber").trim());
			user.setContactInfo(contactInfo);
		}

		if (!StringUtil.isEmpty(searchRecord.get("status"))) {
			if ("1".equals(searchRecord.get("status"))) {
				user.setTempStatus(String.valueOf(Farmer.Status.ACTIVE.ordinal()));
			} else {
				user.setTempStatus(String.valueOf(Farmer.Status.INACTIVE.ordinal()));
			}
		}
		/*
		 * else{ user.setTempStatus(Farmer.Status.ACTIVE.ordinal()); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			user.setBranchId(searchRecord.get("branchId").trim());
		}

		List<Object> farmersList = smsService.listUsers(getSort(), getStartIndex(), getResults(), user);

		data.put(RECORDS, smsService.findUserMobileNumberCount(getSort(), getStartIndex(), getResults(), user));
		data.put(ROWS, farmersList);
		data.put(PAGE_NUMBER, getPage());
		data.put(TAB, USER_TAB);

		return sendFarmerJQGridJSONResponse(data);

	}

	public String getSelectedGroups() {

		return selectedGroups;
	}

	public void setSelectedGroups(String selectedGroups) {

		this.selectedGroups = selectedGroups;
	}

	public void populateGroupMobileNumberByGroup() {

	}

	public void populateListTemplates() {

		List<SmsTemplate> smsTemplate = smsService.listSmsTemplates();
		List<org.json.simple.JSONObject> jsonObjects = new ArrayList<org.json.simple.JSONObject>();

		smsTemplate.stream().forEach(obj -> {
			org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
			jsonObject.put("smsTemplate", obj.getName());
			jsonObject.put("smsTemplateId", obj.getId());
			jsonObjects.add(jsonObject);
		});
		printAjaxResponse(jsonObjects, "text/html");
	}

	public void populateMessageHistory() {

	}

	public String getFarmer_branchid() {
		return farmer_branchid;
	}

	public void setFarmer_branchid(String farmer_branchid) {
		this.farmer_branchid = farmer_branchid;
	}

	public String getFarmer_fpo() {
		return farmer_fpo;
	}

	public void setFarmer_fpo(String farmer_fpo) {
		this.farmer_fpo = farmer_fpo;
	}

	public String getFarmer_first_name() {
		return farmer_first_name;
	}

	public void setFarmer_first_name(String farmer_first_name) {
		this.farmer_first_name = farmer_first_name;
	}

	public String getFarmer_last_name() {
		return farmer_last_name;
	}

	public void setFarmer_last_name(String farmer_last_name) {
		this.farmer_last_name = farmer_last_name;
	}

	public String getFarmer_mobile() {
		return farmer_mobile;
	}

	public void setFarmer_mobile(String farmer_mobile) {
		this.farmer_mobile = farmer_mobile;
	}

	public String getFarmer_tehsil() {
		return farmer_tehsil;
	}

	public void setFarmer_tehsil(String farmer_tehsil) {
		this.farmer_tehsil = farmer_tehsil;
	}

	public String getFarmer_village() {
		return farmer_village;
	}

	public void setFarmer_village(String farmer_village) {
		this.farmer_village = farmer_village;
	}

	public String getFarmer_status() {
		return farmer_status;
	}

	public void setFarmer_status(String farmer_status) {
		this.farmer_status = farmer_status;
	}

	public String getFs_branchId() {
		return fs_branchId;
	}

	public void setFs_branchId(String fs_branchId) {
		this.fs_branchId = fs_branchId;
	}

	public String getFs_agentId() {
		return fs_agentId;
	}

	public void setFs_agentId(String fs_agentId) {
		this.fs_agentId = fs_agentId;
	}

	public String getFs_SMSfirstName() {
		return fs_SMSfirstName;
	}

	public void setFs_SMSfirstName(String fs_SMSfirstName) {
		this.fs_SMSfirstName = fs_SMSfirstName;
	}

	public String getFs_SMSlastName() {
		return fs_SMSlastName;
	}

	public void setFs_SMSlastName(String fs_SMSlastName) {
		this.fs_SMSlastName = fs_SMSlastName;
	}

	public String getFs_SMSmobileNumber() {
		return fs_SMSmobileNumber;
	}

	public void setFs_SMSmobileNumber(String fs_SMSmobileNumber) {
		this.fs_SMSmobileNumber = fs_SMSmobileNumber;
	}

	public String getFs_status() {
		return fs_status;
	}

	public void setFs_status(String fs_status) {
		this.fs_status = fs_status;
	}

	public String getWu_branchId() {
		return wu_branchId;
	}

	public void setWu_branchId(String wu_branchId) {
		this.wu_branchId = wu_branchId;
	}

	public String getWu_username() {
		return wu_username;
	}

	public void setWu_username(String wu_username) {
		this.wu_username = wu_username;
	}

	public String getWu_SMSfirstName() {
		return wu_SMSfirstName;
	}

	public void setWu_SMSfirstName(String wu_SMSfirstName) {
		this.wu_SMSfirstName = wu_SMSfirstName;
	}

	public String getWu_SMSlastName() {
		return wu_SMSlastName;
	}

	public void setWu_SMSlastName(String wu_SMSlastName) {
		this.wu_SMSlastName = wu_SMSlastName;
	}

	public String getWu_SMSmobileNumber() {
		return wu_SMSmobileNumber;
	}

	public void setWu_SMSmobileNumber(String wu_SMSmobileNumber) {
		this.wu_SMSmobileNumber = wu_SMSmobileNumber;
	}

	public String getWu_status() {
		return wu_status;
	}

	public void setWu_status(String wu_status) {
		this.wu_status = wu_status;
	}

	public String getSelectAllFarmers() {
		return selectAllFarmers;
	}

	public void setSelectAllFarmers(String selectAllFarmers) {
		this.selectAllFarmers = selectAllFarmers;
	}

	public String getSelectAllFs() {
		return selectAllFs;
	}

	public void setSelectAllFs(String selectAllFs) {
		this.selectAllFs = selectAllFs;
	}

	public String getSelectAllWebUsers() {
		return selectAllWebUsers;
	}

	public void setSelectAllWebUsers(String selectAllWebUsers) {
		this.selectAllWebUsers = selectAllWebUsers;
	}

	public String getFarmer_district() {
		return farmer_district;
	}

	public void setFarmer_district(String farmer_district) {
		this.farmer_district = farmer_district;
	}

	public String getFarmer_state() {
		return farmer_state;
	}

	public void setFarmer_state(String farmer_state) {
		this.farmer_state = farmer_state;
	}

	public String getFarmer_cropNames() {
		return farmer_cropNames;
	}

	public void setFarmer_cropNames(String farmer_cropNames) {
		this.farmer_cropNames = farmer_cropNames;
	}

}
