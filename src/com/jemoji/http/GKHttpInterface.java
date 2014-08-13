
package com.jemoji.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jemoji.utils.LOG;
import com.jemoji.utils.StringUtils;
import com.jemoji.utils.Utility;
import com.loopj.android.http.RequestParams;

@SuppressWarnings("unchecked")
public class GKHttpInterface {

	public static String genFile(String key, String type, String file, final GKJsonResponseHandler block){
		HashMap<String, String> paramMap = new HashMap<String, String>();
		RequestParams params = new RequestParams(paramMap);
		
		return GKRestClient.instance().get(key, type, file, params, new MyAsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int code, String res) {
				checkResponse(code, res, block);
			}

			@Override
			public void onFailure(int code, Throwable throwable, String s) {
				checkError(code, s, throwable, block);
			}
		});
	}
		
	////////////////////////////// Post ////////////////////////////////////
	private static final String PUSH_MESSAGE = "http://api.jpush.cn:8800/v2/push";
	
	public static String pushMessage(String phone, String msg_content, final GKJsonResponseHandler block){
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		Random ran = new Random();
		int sendno = ran.nextInt(1000);
		int receiverType = 2;
		String receiverValue = phone;
		String masterSecret = "94f3c7822e35414eab1c303e"; //极光推送portal 上分配的 appKey 的验证串(masterSecret)
		String input = String.valueOf(sendno) + receiverType + receiverValue + masterSecret;
		String verificationCode = StringUtils.toMD5(input);
		
		paramMap.put("sendno", sendno + "");
		paramMap.put("app_key", "ed958eeb9ae8245862f41aff");
		paramMap.put("receiver_type", "2");//tag消息
		paramMap.put("receiver_value", phone);
		paramMap.put("verification_code", verificationCode);
		paramMap.put("msg_type", "2");
		paramMap.put("msg_content", msg_content);
		paramMap.put("platform", "android");
		
		
		MyRequestParams params = new MyRequestParams(paramMap);
		
		return GKRestClient.instance().post(PUSH_MESSAGE, params, new MyAsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int code, String res) {
				LOG.v(LOG.TAG_API, String.format("response:%s", res));

				checkResponse3(res, block);
			}

			@Override
			public void onFailure(int code, Throwable throwable, String s) {
				checkError(code, s, throwable, block);
			}
		});
	}
//	public static String like(long eid, boolean like,  final GKJsonResponseHandler block){
//		HashMap<String, String> paramMap = new HashMap<String, String>();
//		String url = String.format(LIKE_, eid, like ? 1 : 0);
//		
//		MyRequestParams params = new MyRequestParams(Utility.getSignParams(paramMap));
//		return GKRestClient.instance().post(url, params, new MyAsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int code, String res) {
//				LOG.v(LOG.TAG_API, String.format("response:%s", res));
//
//				checkResponse3(res, block);
//			}
//
//			@Override
//			public void onFailure(int code, Throwable throwable, String s) {
//				checkError(code, s, throwable, block);
//			}
//		});
//	}
	
	private static final int URL_FORMAT_EXCEPTION = 2;
	private static final int JSON_PARSE_EXCEPTION = 1;
	private static final int UNKONWN_EXCEPTION = 0;
	
	public static String toJson(Map<String,String> map){
		Set<Map.Entry<String, String>> entrys = map.entrySet();
		Map.Entry<String, String> entry = null;
	    String key = "";
	    String value = "";
	    StringBuffer jsonBuffer = new StringBuffer();
	    jsonBuffer.append("{");    
	    for(Iterator<Map.Entry<String, String>> it = entrys.iterator();it.hasNext();){
	    	entry =  (Map.Entry<String, String>)it.next();
	    	key = entry.getKey();
	        value = entry.getValue();
	        jsonBuffer.append(String.format("\"%s\":\"%s\"", key, value));
	        if(it.hasNext()){
	             jsonBuffer.append(",");
	        }
	    }
	    jsonBuffer.append("}");
	    return jsonBuffer.toString();
	}
	
	private static void checkResponse(int code, String res, GKJsonResponseHandler block){
		if (block != null) {
			block.onResponse(200, res, null);
		}
	}
	
	private static void checkResponse3(String res, GKJsonResponseHandler block) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		res = res.substring(res.indexOf("{"), res.length());
		try {
			Object result = null;
			String first = (res == null ? "" : res).trim().charAt(0) + "";
			
			if("{".equals(first)){
				Map<String, Object> maps = objectMapper.readValue(res == null ? "" : res, Map.class);
				
				
				result = maps;
			}else if ("[".equals(first)){
				ArrayList<Map> list = objectMapper.readValue(res == null ? "" : res, ArrayList.class);
				result = list;
			}else{
				result = res;
			}

			if (block != null) {
				block.onResponse(200, result, null);
			}
		} catch (JsonParseException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		} catch (JsonMappingException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		} catch (IOException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		}
	}
	
	private static void checkError(int code, String res, Throwable throwable, GKJsonResponseHandler block){
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Map<String, Object> maps = (Utility.Strings.isEmptyString(res) ? new HashMap<String, Object>() : objectMapper.readValue(res, Map.class));

			if (block != null) {
				block.onResponse(code, maps, throwable);
			}
		} catch (JsonParseException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		} catch (JsonMappingException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		} catch (IOException e) {
			block.onResponse(JSON_PARSE_EXCEPTION, null, e);
		}
	}
	
	private static void checkError(String s, Throwable throwable, GKJsonResponseHandler block){
		if (block != null) {
			block.onResponse(UNKONWN_EXCEPTION, s, throwable);
		}
	}
}
