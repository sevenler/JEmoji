package com.jemoji.http;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

class MyStringRequest extends StringRequest{
	Map<String, String> params = new HashMap<String, String>();

	public MyStringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
		
		setRetryPolicy(new DefaultRetryPolicy());
	}
	
	public MyStringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		
		setRetryPolicy(new DefaultRetryPolicy());
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}
	
	public void addParams(Map<String, String> params){
		this.params.putAll(params);
	}
}