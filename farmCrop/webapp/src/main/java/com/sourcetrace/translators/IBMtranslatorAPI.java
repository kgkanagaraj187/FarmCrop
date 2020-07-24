package com.sourcetrace.translators;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public final class IBMtranslatorAPI {
	
	protected static OkHttpClient client = new OkHttpClient();
	//IBM Supported languages
	public static String arabic  = "ar";
	public static String bengali  = "bn";
	public static String bulgarian  = "bg";
	public static String catalan  = "ca";
	public static String chineseSimplified  = "zh";
	public static String chineseTraditional  = "zh-TW";
	public static String croatian  = "hr";
	public static String czech  = "cs";
	public static String danish  = "da";
	public static String dutch  = "nl";
	public static String english  = "en";
	public static String estonian  = "et";
	public static String finnish  = "fi";
	public static String french  = "fr";
	public static String german  = "de";
	public static String greek  = "el";
	public static String gujarati  = "gu";
	public static String hebrew  = "he";
	public static String hindi  = "hi";
	public static String hungarian  = "hu";
	public static String irish  = "ga";
	public static String indonesian  = "id";
	public static String italian  = "it";
	public static String japanese  = "ja";
	public static String korean  = "ko";
	public static String latvian  = "lv";
	public static String lithuanian  = "li";
	public static String malay  = "ms";
	public static String malayalam  = "ml";
	public static String maltese  = "mt";
	public static String norwegianBokmal  = "nb";
	public static String polish  = "pl";
	public static String portuguese  = "pt";
	public static String romanian  = "ro";
	public static String russian  = "ru";
	public static String slovak  = "sk";
	public static String slovenian  = "sl";
	public static String spanish  = "es";
	public static String swedish  = "sv";
	public static String tamil  = "ta";
	public static String telugu  = "te";
	public static String thai  = "th";
	public static String turkish  = "tr";
	public static String urdu  = "ur";
	public static String vietnamese  = "vi";
	
	public static final String IBM_URL = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/0ada1e27-7c2d-42d8-8408-cdb891425886";
		
	public final static String translateText(String subscriptionKey, String apiEndpoint, String fromLang, String toLang,
			String originalStr) throws IOException {
		String endPointUrl = apiEndpoint;
		String translatedText = "";
		translatedText = ibmTranslatePostFromLang(endPointUrl, subscriptionKey, originalStr,fromLang,toLang);
		return translatedText;

	}
	
	
	// This function performs a POST request.
	public static String ibmTranslatePost(String endpointURL, String subscriptionKey, String strToTranslate) throws IOException {
		MediaType mediaType = MediaType.parse("application/json,text/plain");
		strToTranslate = replaceSpecialCharsFromStr(strToTranslate);
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"Text\": \"" + strToTranslate + "\",\"model_id\": \""+english+"-"+strToTranslate+"\"\n}");
		Request request = new Request.Builder().url(endpointURL).post(body)
				.method("POST", body)				
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Basic "+subscriptionKey)
				.addHeader("Content-Type", "text/plain")
				.build();				
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
	
	
	// This function performs a POST request.
	public static String ibmTranslatePostFromLang(String endpointURL, String subscriptionKey, String strToTranslate,String fromLang,String toLang) throws IOException {
		MediaType mediaType = MediaType.parse("application/json,text/plain");
		strToTranslate = replaceSpecialCharsFromStr(strToTranslate);
		RequestBody body = RequestBody.create(mediaType, "{\n\t\"text\": [\n\"" + strToTranslate + "\"\n],\n \"model_id\": \""+fromLang+"-"+toLang+"\"\n}");
		Request request = new Request.Builder().url(endpointURL)
				.method("POST", body)				
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Basic "+subscriptionKey)				
				.build();				
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	public static String ibmTranslatedWord(String responseBody) {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = null;
		JsonArray jsonArray = new JsonArray();
		JsonElement json = null;
		JsonElement jsonTranslatedElement = null;
		String TRANSLATION_JSON_ARRAY = "translations";
		String TRANSLATION_JSON_ARRAY_OBJ = "translation";
		String retVal = "";

		if (!StringUtil.isEmpty(responseBody)) {
			json = parser.parse(responseBody);
			jsonObj = json.getAsJsonObject();
			jsonArray = (JsonArray) jsonObj.get(TRANSLATION_JSON_ARRAY);
			if (!ObjectUtil.isEmpty(jsonArray)) {
				jsonTranslatedElement = jsonArray.get(0);
				try {
					retVal = jsonTranslatedElement.getAsJsonObject().get(TRANSLATION_JSON_ARRAY_OBJ).toString();
				} catch (Exception e) {
					System.out.print("Error With IBM Translator: " + e.getMessage());
				}
			}
		}
		retVal = getUnquotedText(retVal);
		return retVal;
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
}
