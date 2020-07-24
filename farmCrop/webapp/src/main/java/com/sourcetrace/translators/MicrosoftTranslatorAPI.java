package com.sourcetrace.translators;

import java.io.IOException;

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

public final class MicrosoftTranslatorAPI {
	//Languages supported in Microsoft translator
	public static String afrikaans="af";
	public static String arabic="ar";
	public static String bangla="bn";
	public static String bosnianLatin="bs";
	public static String bulgarian="bg";
	public static String cantoneseTraditional="yue";
	public static String catalan="ca";
	public static String chineseSimplified="zh-Hans";
	public static String chineseTraditional="zh-Hant";
	public static String croatian="hr";
	public static String czech="cs";
	public static String danish="da";
	public static String dutch="nl";
	public static String english="en";
	public static String estonian="et";
	public static String fijian="fj";
	public static String filipino="fil";
	public static String finnish="fi";
	public static String french="fr";
	public static String german="de";
	public static String greek="el";
	public static String gujarati="gu";
	public static String haitianCreole="ht";
	public static String hebrew="he";
	public static String hindi="hi";
	public static String hmongDaw="mww";
	public static String hungarian="hu";
	public static String icelandic="is";
	public static String indonesian="id";
	public static String irish="ga";
	public static String italian="it";
	public static String japanese="ja";
	public static String kannada="kn";
	public static String kiswahili="sw";
	public static String klingon="tlh";
	public static String klingonPlqaD="tlh-Qaak";
	public static String korean="ko";
	public static String latvian="lv";
	public static String lithuanian="lt";
	public static String malagasy="mg";
	public static String malay="ms";
	public static String malayalam="ml";
	public static String maltese="mt";
	public static String maori="mi";
	public static String marathi="mr";
	public static String norwegian="nb";
	public static String persian="fa";
	public static String polish="pl";
	public static String portugueseBrazil="pt-br";
	public static String portuguesePortugal="pt-pt";
	public static String punjabi="pa";
	public static String queretaroOtomi="otq";
	public static String romanian="ro";
	public static String russian="ru";
	public static String samoan="sm";
	public static String serbianCyrillic="sr-Cyrl";
	public static String serbianLatin="sr-Latn";
	public static String slovak="sk";
	public static String slovenian="sl";
	public static String spanish="es";
	public static String swedish="sv";
	public static String tahitian="ty";
	public static String tamil="ta";
	public static String telugu="te";
	public static String thai="th";
	public static String tongan="to";
	public static String turkish="tr";
	public static String ukrainian="uk";
	public static String urdu="ur";
	public static String vietnamese="vi";
	public static String welsh="cy";
	public static String yucatecMaya="yua";

	
	public static final String ENDPOINT_URL = "/translate?api-version=3.0&to=";
	protected static OkHttpClient client = new OkHttpClient();
	public static final String SUBSCRIPTION_REGION = "centralindia";
	public static final String CONTENT_TYPE = "application/json; charset=UTF-8";
		
	public final static String translateText(String subscriptionKey, String apiEndpoint, String fromLang, String toLang,
			String originalStr) throws IOException {
		String endPointUrl = apiEndpoint + ENDPOINT_URL + toLang + "," + fromLang;
		String translatedText = "";
		translatedText = msTranslatePost(endPointUrl, subscriptionKey, originalStr);
		return translatedText;

	}
	
	
	// This function performs a POST request.
	public static String msTranslatePost(String endpointURL, String subscriptionKey, String strToTranslate) throws IOException {
		MediaType mediaType = MediaType.parse("application/json");
		strToTranslate = replaceSpecialCharsFromStr(strToTranslate);
		RequestBody body = RequestBody.create(mediaType, "[{\n\t\"Text\": \"" + strToTranslate + "\"\n}]");
		Request request = new Request.Builder().url(endpointURL).post(body)
				.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey).addHeader("Content-type", "application/json")
				.addHeader("Ocp-Apim-Subscription-Region", SUBSCRIPTION_REGION)
				.addHeader("Content-Type", CONTENT_TYPE)
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
	
	public static String msTranslatedWord(String responseBody) {
		JsonParser parser = new JsonParser();
		JsonArray jsonResArrayObj = new JsonArray();
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonObj = null;
		JsonElement json = null;
		JsonElement jsonTranslatedElement = null;
		String TRANSLATION_JSON_ARRAY = "translations";
		String TRANSLATION_JSON_ARRAY_OBJ = "text";
		String retVal = "";

		if (!StringUtil.isEmpty(responseBody)) {
			json = parser.parse(responseBody);
			jsonResArrayObj = json.getAsJsonArray();
			jsonObj = (JsonObject) jsonResArrayObj.get(0);
			jsonArray = (JsonArray) jsonObj.get(TRANSLATION_JSON_ARRAY);
			if (!ObjectUtil.isEmpty(jsonArray)) {
				jsonTranslatedElement = jsonArray.get(0);
				try {
					retVal = jsonTranslatedElement.getAsJsonObject().get(TRANSLATION_JSON_ARRAY_OBJ).toString();
				} catch (Exception e) {
					System.out.print("Error With Microsoft Translator: " + e.getMessage());
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
