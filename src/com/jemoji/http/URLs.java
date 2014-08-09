package com.jemoji.http;


public class URLs {
	public static final int ENVORIMENT_FORMAL = 1;
	public static final int ENVORIMENT_TEST = 0;
	
	private static int envoriment = ENVORIMENT_FORMAL;
	
	private static final String[] BASE_URL = { "http://emoji.b0.upaiyun.com/test", "http://emoji.b0.upaiyun.com/test"};
    
    public static final String getBaseUrl(){
    	return BASE_URL[envoriment];
    }
    
    public static String getAbsoluteUrl(String relativeUrl) {
    	if(relativeUrl.trim().indexOf("http") == 0){
    		return relativeUrl;
    	}
        return URLs.getBaseUrl() + relativeUrl;
    }
    
    public static final void changeEnvoriment(int en){
    	envoriment = en;
    }
    
    public static int getEnvoriment(){
    	 return envoriment;
    }
 
}
