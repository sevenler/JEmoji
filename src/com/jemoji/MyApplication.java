package com.jemoji;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.jemoji.image.ImageCacheManager;
import com.jemoji.image.ImageCacheManager.CacheType;
import com.jemoji.image.RequestManager;

public class MyApplication extends Application {
    private static final String TAG = "JPush";
    
    private static Context context;
    
    public synchronized static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[ExampleApplication] onCreate");
         super.onCreate();
         
         MyApplication.context = getApplicationContext();
         
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
         
         initVolley();
    }
    
    private static int DISK_IMAGECACHE_SIZE = 10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100; 
	
	private void initVolley() {
		RequestManager.init(this);
		ImageCacheManager.instance().init(this,
				getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, CacheType.MEMORY);
	}
}
