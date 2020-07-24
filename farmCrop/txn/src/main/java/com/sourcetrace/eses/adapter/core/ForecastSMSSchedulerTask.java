package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.sms.ForecastSMSHistory;
import com.ese.entity.sms.ForecastSMSHistoryDetail;
import com.ese.entity.sms.SMSHistory;
import com.ese.entity.sms.SMSHistoryDetail;
import com.ese.entity.sms.SmsGroupDetail;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;

@Component
public class ForecastSMSSchedulerTask {
	public static Logger LOGGER = Logger.getLogger(ForecastSMSSchedulerTask.class);
	@Autowired
	private IFarmerService farmerService;
	private ForecastSMSHistory smsHistory;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private IPreferencesService preferencesService;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private static final String COMMA_SEPARATOR = ",";
	private static final int INVALID_JOB_ID_LOOP_COUNT = 2;

	public void process() {
		List<Object[]> advisoryDatas = farmerService.findAdvisoryList();
		String success = "0", statusMsg = "";
		String providerResponse = null;
		int smsType = SMSHistory.SMS_SINGLE;
		Farm farm = null;
		boolean isAllowedToSave = false;
		String status = null;
		String descrption = null;
		try {

			for (Object[] advisoryData : advisoryDatas) {
				List<Object[]> currentFcastData = farmerService
						.findForeCastList(Long.valueOf(String.valueOf(advisoryData[0])));

				for (Object[] forecastDatas : currentFcastData) {
					farm = farmerService.findFarmByfarmId(Long.valueOf(String.valueOf(forecastDatas[0])));
					System.out.println("mobile no:" + farm.getFarmer().getMobileNumber());
					if (!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer())
							&& !StringUtil.isEmpty(farm.getFarmer().getMobileNumber())) {
						if (Double.valueOf(String.valueOf(forecastDatas[1])) >= Double
								.valueOf(String.valueOf(advisoryData[1]))
								|| Double.valueOf(String.valueOf(forecastDatas[2])) >= Double// temp
										.valueOf(String.valueOf(advisoryData[2]))
										&& Double.valueOf(String.valueOf(forecastDatas[3])) >= Double
												.valueOf(String.valueOf(advisoryData[3]))
								|| Double.valueOf(String.valueOf(forecastDatas[3])) >= Double
										.valueOf(String.valueOf(advisoryData[4]))// wind
										&& Double.valueOf(String.valueOf(forecastDatas[4])) >= Double
												.valueOf(String.valueOf(advisoryData[5])) // humdity
								|| Double.valueOf(String.valueOf(forecastDatas[4])) >= Double
										.valueOf(String.valueOf(advisoryData[6]))) {
							
							smsHistory=new ForecastSMSHistory();
							smsHistory.setReceiverMobNo(farm.getFarmer().getMobileNumber());
							smsHistory.setMessage(String.valueOf(advisoryData[7]));
							if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())
									&& smsHistory.getReceiverMobNo().contains(COMMA_SEPARATOR)) {
								smsType = SMSHistory.SMS_BULK;
							}
							providerResponse = smsService.sendSMS(smsType, smsHistory.getReceiverMobNo(),
									smsHistory.getMessage());
							
							if (!StringUtil.isEmpty(providerResponse)) {
								JSONObject respObj = new JSONObject(providerResponse);
								if (!respObj.has(ISMSService.ERROR)) {
									isAllowedToSave = true;
									statusMsg = "Success";
									//smsHistory.setUuid(respObj.getString("batch_id"));
								} else {
									statusMsg = "ERROR";
									smsHistory.setUuid(respObj.getString("batch_id"));
								}
							}

							if (isAllowedToSave) {

								// Logic to save request & responses

								status = ISMSService.SMS_STATUS_DELIVERED;
								descrption = status;

								ESESystem system = preferencesService.findPrefernceById("1");
								if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
									smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
									smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
								}
								smsHistory.setFarmerId(farm.getFarmer().getId());
								smsHistory.setFarmId(farm.getId());
								smsHistory.setAdvisoryId(Long.valueOf(String.valueOf(advisoryData[6])));
								smsHistory.setSmsType(smsType);
								smsHistory.setStatusz(status);
								smsHistory.setStatusMsg(descrption);
								smsHistory.setCreationInfo(getUsername());
								smsHistory.setForecastSMSHistoryDetails(new LinkedHashSet<ForecastSMSHistoryDetail>());
								// Set<SMSHistoryDetail> smsHistoryDetails = new
								// LinkedHashSet<SMSHistoryDetail>();
								if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())) {
									String[] receiverMobileNos = smsHistory.getReceiverMobNo().split(COMMA_SEPARATOR);
									for (String number : receiverMobileNos) {
										// Build detail objects
										ForecastSMSHistoryDetail smsHistoryDetail = new ForecastSMSHistoryDetail();
										smsHistoryDetail.setCreationInfo(getUsername());
										smsHistoryDetail.setReceiverNo(number);
										// JSONObject object =
										// messagesMap.get(number);
										String smsStatus = "Delivered";
										Date sentAt = null;

										smsHistoryDetail.setStatusz(smsStatus);
										smsHistoryDetail.setSendAt(sentAt);
										smsHistoryDetail.setForecastSMSHistory(smsHistory);
										smsHistoryDetail.setCreationInfo(getUsername());
										smsHistoryDetail.setReceiverType(SmsGroupDetail.PROFILE_TYPE_OTHER);
										smsHistory.getForecastSMSHistoryDetails().add(smsHistoryDetail);
									}
								}
								// smsHistory.setSmsHistoryDetails(smsHistoryDetails);
								smsService.addForecastSMSHistory(smsHistory);
								// success = "1";
								// statusMsg = "msg.msgSent";

							} else {

								status = "error";
								descrption = status;
								// Logic to save request & responses

								ESESystem system = preferencesService.findPrefernceById("1");
								if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
									smsHistory.setSenderMobNo(system.getPreferences().get(ESESystem.SMS_SENDER_ID));
									smsHistory.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
								}
								smsHistory.setSmsType(smsType);
								smsHistory.setStatusz(status);
								smsHistory.setStatusMsg(descrption);
								// smsHistory.setUuid(jobId);
								smsHistory.setCreationInfo(getUsername());
								smsHistory.setForecastSMSHistoryDetails(new LinkedHashSet<ForecastSMSHistoryDetail>());
								// Set<SMSHistoryDetail> smsHistoryDetails = new
								// LinkedHashSet<SMSHistoryDetail>();
								if (!StringUtil.isEmpty(smsHistory.getReceiverMobNo())) {
									String[] receiverMobileNos = smsHistory.getReceiverMobNo().split(COMMA_SEPARATOR);
									for (String number : receiverMobileNos) {
										// Build detail objects
										ForecastSMSHistoryDetail smsHistoryDetail = new ForecastSMSHistoryDetail();
										smsHistoryDetail.setCreationInfo(getUsername());
										smsHistoryDetail.setReceiverNo(number);
										// JSONObject object =
										// messagesMap.get(number);
										String smsStatus = "Failure";
										Date sentAt = null;

										smsHistoryDetail.setStatusz(smsStatus);
										smsHistoryDetail.setSendAt(sentAt);
										smsHistoryDetail.setForecastSMSHistory(smsHistory);
										smsHistoryDetail.setCreationInfo(getUsername());
										smsHistoryDetail.setReceiverType(SmsGroupDetail.PROFILE_TYPE_OTHER);
										smsHistory.getForecastSMSHistoryDetails().add(smsHistoryDetail);
									}
								}
								// smsHistory.setSmsHistoryDetails(smsHistoryDetails);
								smsService.addForecastSMSHistory(smsHistory);
								// success = "1";
								// statusMsg = "msg.msgSent";

							}
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public String getUsername() {

		if (ObjectUtil.isEmpty(request)) {
			request = ReflectUtil.getCurrentHttpRequest();
		}
		Object biObj = ObjectUtil.isEmpty(request) ? null : request.getSession().getAttribute("user");
		return ObjectUtil.isEmpty(biObj) ? null : biObj.toString();

	}

}
