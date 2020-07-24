// Author : STSE110
package com.sourcetrace.owm;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ese.entity.sms.SMSHistory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.service.PreferencesService;
import com.sourcetrace.eses.service.SMSService;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans-*.xml" })
public class OwmForecastSmsTest {

	private static Logger LOGGER = Logger.getLogger(OwmForecastSmsTest.class);
	private static final String  OPEN_WEATHER_MAP_URL = "https://api.openweathermap.org/data/2.5/onecall?";
	private static final String LAT = "lat=";
	private static final String LON = "&lon=";
	private static final String appId = "&appid=";
	private static final String OWM_API_KEY = "16fd196adfb3bc56c72ee988978492dd";
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	private static OkHttpClient client = new OkHttpClient();
	private static String jsonResponse = "";
	
	
	private static final String COMMA_SEPARATOR = ",";
	
	private static String owmApiURL = "";
	private static String latitude = "76.97";
	private static String longitude = "11"; // Coordinates for Coimbatore City of India.
		
	private  ISMSService smsService = new SMSService();
	private  JsonDataObject jsonDataObject = new JsonDataObject();
	
	private static final String SMS_SUCCESS = "SMS SENT SUCCESS";
	private static Gson gson = new Gson();
	private static SMSHistory smsHistory;
	private String humidityValue = "";
	private String cloudCoverValue = "";
	private String temperatureValue = "";
	
	private IPreferencesService preferencesService = new PreferencesService();	
	@Test
	public void test() {
		try {
			owmApiURL = buildURLForOwmOnetimeAPI(latitude, longitude);
			if (!StringUtil.isEmpty(owmApiURL)) {
				jsonResponse = owmApiRequest(owmApiURL);
			}
			System.out.println("The Formed URL is :" + owmApiURL);
			System.out.println("The Json Response Got is :" + jsonResponse);
			LOGGER.info("JSON RESPONSE: " + jsonResponse);
			if (!StringUtil.isEmpty(jsonResponse)) {
				humidityValue = getOwmHumidityValue(jsonResponse);
				cloudCoverValue = getOwmCloudValue(jsonResponse);
				temperatureValue = getOwmTemperatureValue(jsonResponse);
				sendSms("+12345", "Today's Source Trace, Coimbatore Weather:\n Temperature: " + temperatureValue
						+ " C\n Humidity: " + humidityValue + "\n Cloud Cover: " + cloudCoverValue); // First parameter to be Mobile Number
			}
		} catch (Exception e) {
			LOGGER.info("Formation of OWM URL error:" + e.getMessage());
			fail("Not yet implemented");
		}

	}
	
	private static String buildURLForOwmOnetimeAPI(String latitude, String longitude) {
		StringBuilder sb = new StringBuilder("");
		sb.append(OPEN_WEATHER_MAP_URL).append(LAT).append(latitude).append(LON).append(longitude).append(appId)
				.append(OWM_API_KEY);
		return sb.toString();
	}
	
	// This function performs a POST request.
	public static String owmApiRequest(String endpointURL) throws IOException {
		Request request = new Request.Builder().url(endpointURL).get().addHeader("Content-type", "application/json")
				.addHeader("Content-Type", CONTENT_TYPE).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	// This function prettifies the json response.
	public static String prettify(String json_text) {
	    JsonParser parser = new JsonParser();
	    JsonElement json = parser.parse(json_text);
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    return gson.toJson(json);
	}
	
	public static String replaceSpecialCharsFromStr(String inputTxt) {
		String outputStr = "";
		if (!StringUtil.isEmpty(inputTxt)) {
			outputStr = inputTxt.replace("\"", "\\\"");
			outputStr = outputStr.replace("'", "\'");
		}
		return outputStr;
	}
	
	public static String getUnquotedText(String strVal) {
		String retVal = "";
		if (strVal.contains("\"") && strVal.length() >= 2) {
			strVal = strVal.substring(1);
			strVal = StringUtil.removeLastChar(strVal, '\"');
		}
		retVal = strVal;
		return retVal;
	}
	
	public String sendSms(String mobileNos, String message) {

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
			providerResponse = sendOwmSMS(smsType, smsHistory.getReceiverMobNo(), smsHistory.getMessage());

			boolean isAllowedToSave = false;
			String status = null;
			String descrption = null;

			LOGGER.info("Fast Alert Response : " + providerResponse);
			if (!StringUtil.isEmpty(providerResponse)) {
				JSONObject respObj = new JSONObject(providerResponse);

				if (!respObj.has(ISMSService.ERROR)) {
					isAllowedToSave = true;
					statusMsg = SMS_SUCCESS;

				}


					status = "error";
					descrption = providerResponse;
					// Logic to save request & responses					
					smsHistory.setSmsRoute("TEXT LOCAL IN");
					smsHistory.setSmsType(smsType);
					smsHistory.setStatusz(status);
					smsHistory.setStatusMsg(descrption);
					smsHistory.setSenderMobNo(mobileNos);
					smsHistory.setCreationInfo(getUserName());					

				

				jsonDataObject = new JsonDataObject(1L, success, statusMsg, "");
				LOGGER.info("JSON Success For SMS: " + gson.toJson(jsonDataObject));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getUserName(){
		return "tester";
	}
	
	
	public String sendOwmSMS(int smsType, String receiverMobNo, String message) {
		final StringBuffer stringBuffer = new StringBuffer();
		try {

			String user = "username=" + "dalitadivasi.bruti@gmail.com";
			String template = "From Bruti: ";
			String hash = "&hash="+"78eb14a74320b623027e7d42e736ecdc0a27d6e0612353ca4ce69eccfff243c";

			String messagez = "&message="+template + message;
			String sender = "&sender="+"BRUTID";
			String numbers = "&numbers="+receiverMobNo;
			String typez = "0";
			String testStatus = "true";

			if (testStatus.equals("true")) {
				testStatus = "false";
			}
			String test = "&test=" + testStatus;
			if (true) {
				URL url = null;
				String data = "";
				if (smsType == SMSHistory.SMS_SINGLE) {
					url = new URL("http://api.textlocal.in/send/?");
				} else {
					url = new URL("http://api.textlocal.in/send/?");
				}

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				data = user + hash + numbers + messagez + sender + test;
				conn.setDoOutput(true);
				conn.setRequestMethod(ISMSService.POST);
				conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
				conn.getOutputStream().write(data.getBytes(ISMSService.CHARACTER_SET));
				final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String line;
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line);
				}
				rd.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	public static String getOwmHumidityValue(String json_text) {
		JsonElement humidityValue = null;
	    JsonParser parser = new JsonParser();
	    JsonElement json = parser.parse(json_text);
	    humidityValue = json.getAsJsonObject().get("current").getAsJsonObject().get("humidity");
	    return humidityValue.getAsString();
	}

	public static String getOwmCloudValue(String json_text) {
		JsonElement cloudValue = null;
	    JsonParser parser = new JsonParser();
	    JsonElement json = parser.parse(json_text);
	    cloudValue = json.getAsJsonObject().get("current").getAsJsonObject().get("clouds");
	    return cloudValue.getAsString();
	}
	
	public static String getOwmTemperatureValue(String json_text) {
		JsonElement tempValue = null;
		String retVal = "";
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(json_text);
		tempValue = json.getAsJsonObject().get("current").getAsJsonObject().get("temp");
		retVal = getDividedByQuotientStrValue(tempValue.getAsString(), 10);
		return retVal;
	}
	
	public static String getDividedByQuotientStrValue(String strVal, int divisor) {
		Double doubleVal = 0.0d;
		if (StringUtil.isDouble(strVal)) {
			doubleVal = Double.parseDouble(strVal);
			doubleVal = (doubleVal / divisor);
		}
		return doubleVal.toString();
	}
}
