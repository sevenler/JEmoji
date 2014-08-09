package com.jemoji.http;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler{

	public void onFailure(int code, Throwable arg0, String arg1) {
		super.onFailure(arg0, arg1);
	}
	
	@Deprecated
	@Override
	public void onFailure(Throwable arg0, String arg1) {
		super.onFailure(arg0, arg1);
	}

	@Deprecated
	@Override
	public void onFailure(Throwable arg0) {
		super.onFailure(arg0);
	}

	@Override
	public void onSuccess(int arg0, String arg1) {
		super.onSuccess(arg0, arg1);
	}

	@Deprecated
	@Override
	public void onSuccess(String arg0) {
		super.onSuccess(arg0);
	}

}
