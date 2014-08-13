package com.jemoji.http;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jemoji.image.RequestManager;
import com.jemoji.utils.LOG;
import com.loopj.android.http.RequestParams;

public class GKRestClient {
    private static GKRestClient instance;
    
    public static GKRestClient instance(){
    	if(instance == null) instance = new GKRestClient();
    	
    	return instance;
    }
    
    private GKRestClient(){}
    
    public String get(String url,final String type, final String file, RequestParams params,final MyAsyncHttpResponseHandler responseHandler){
		RequestQueue mQueue = RequestManager.getAPIRequestQueue();
		
		String full = String.format("%s?%s", URLs.getAbsoluteUrl(url), params.toString());
		LOG.d(LOG.TAG_API, full);
		
		FileRequest request = new FileRequest(full, file, new Listener<File>() {
			@Override
			public void onResponse(File file) {
				responseHandler.onSuccess(200, file.getAbsolutePath());
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				NetworkResponse response = arg0.networkResponse;
				String parsed = null;
				if (response != null) {
					try {
						parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
					} catch (UnsupportedEncodingException e) {
						parsed = new String(response.data);
					}
				}
				responseHandler.onFailure(response == null ? 0 : response.statusCode, arg0, parsed);
			}
		});
		request.setTag(full);
		
		mQueue.add(request);
		mQueue.start();
		
		return full;
	}
    
    public String post(String url, MyRequestParams params,final MyAsyncHttpResponseHandler responseHandler){
		RequestQueue mQueue = RequestManager.getAPIRequestQueue();
		
		url = URLs.getAbsoluteUrl(url);
		String message = String.format("%s and postted %s", url, params.toString());
		LOG.d(LOG.TAG_API, message);
		
//		System.out.println(String.format("%s", message));
		
		MyStringRequest request = new MyStringRequest(Method.POST, url, new Listener<String>(){
			@Override
			public void onResponse(String arg0) {
				responseHandler.onSuccess(200, arg0);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				NetworkResponse response = arg0.networkResponse;
				String parsed = null;
				if (response != null) {
					try {
						parsed = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
					} catch (UnsupportedEncodingException e) {
						parsed = new String(response.data);
					}
				}
				
				//TODO 0 需要重新定义
				responseHandler.onFailure(response == null ? 0 : response.statusCode, arg0, parsed);
			}
		});
		request.addParams(params.getParams());
		request.setTag(url);
		
		mQueue.add(request);
		mQueue.start();
		
		return url;
	}
}
