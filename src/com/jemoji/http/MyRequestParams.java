package com.jemoji.http;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jemoji.utils.Utility;

public class MyRequestParams {
	Map<String, String> mParams = new LinkedHashMap<String, String>();
	
	public MyRequestParams(){}
	
	public MyRequestParams(Map<String, String> params){
		mParams.putAll(params);
	}
	
	public void put(String key, String value){
		mParams.put(key, value);
	}

	@Override
	public String toString() {
		String result = Utility.generateUrlParams(mParams);
		return result;
	}
	
	public Map<String, String> getParams(){
		return mParams;
	}
}
