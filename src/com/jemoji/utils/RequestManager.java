package com.jemoji.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Manager for the queue
 * 
 * @author Trey Robinson
 *
 */
public class RequestManager {
	
	/**
	 * the queue :-)
	 */
	private static RequestQueue[] mRequestQueue;

	/**
	 * Nothing to see here.
	 */
	private RequestManager() {
	 // no instances
	} 

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {
		RequestQueue[] queue = {Volley.newRequestQueue(context), Volley.newRequestQueue(context)};
		mRequestQueue = queue;
	}

	/**
	 * @return
	 * 		instance of the queue
	 * @throws
	 * 		IllegalStatException if init has not yet been called
	 */
	public static RequestQueue getAPIRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue[0];
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
	
	public static RequestQueue getImageRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue[1];
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
}
