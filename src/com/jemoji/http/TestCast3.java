
package com.jemoji.http;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.test.AndroidTestCase;

import com.jemoji.image.RequestManager;
import com.jemoji.utils.LOG;
import com.upyun.api.Uploader;
import com.upyun.api.utils.UpYunException;
import com.upyun.api.utils.UpYunUtils;

public class TestCast3 extends AndroidTestCase {
	// //////////////////////////////////////////////////////////////////////////////////////////

	private Throwable wrong;

	private void checkWrong() throws Throwable {
		if (wrong != null) {
			throw wrong;
		}
	}

	CountDownLatch latch;

	private void preRequest() {
		this.latch = new CountDownLatch(1);
	}

	private void checkResponse(int code, Object json, Throwable error) {
		switch (code) {
			case 200:
				System.out.println(String.format("%s", json));
				break;

			default:
				wrong = error;
				break;
		}
		latch.countDown();
	}

	private void finishResponse() throws Throwable {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkWrong();
	}

	public void htestCase() throws Throwable {
		RequestManager.init(getContext());
		URLs.changeEnvoriment(URLs.ENVORIMENT_FORMAL);
		
		String ur = URLs.getAbsoluteUrl(" http://www.baidu.com");
		System.out.println(String.format(" =========== %s   %s", ur, ur.trim().indexOf("http")));

		/*String IMG = "/1407549723664.amr";
		preRequest();
		GKHttpInterface.genFile(IMG, "amr", new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object json, Throwable error) {
				checkResponse(code, json, error);
			}
		});
		finishResponse();*/
		
//		preRequest();
//		GKHttpInterface.pushMessage("18511557126", "00000000000000000000", new GKJsonResponseHandler() {
//			@Override
//			public void onResponse(int code, Object json, Throwable error) {
//				checkResponse(code, json, error);
//			}
//		});
//		finishResponse();
	}

	
	private static final String TEST_API_KEY = "h2u0v4S4BRUtwQphIj6sBy+JVMo="; // 测试使用的表单api验证密钥
	private static final String BUCKET = "emoji"; // 存储空间
	private static final long EXPIRATION = System.currentTimeMillis() / 1000 + 1000 * 5 * 10; // 过期时间，必须大于当前时间
	
	public void testCase0() throws Throwable {
		RequestManager.init(getContext());
		URLs.changeEnvoriment(URLs.ENVORIMENT_FORMAL);
		
		
		File path = new File("/sdcard/Emoji_Image/");
		File[] list = path.listFiles();
		
		
		List<String> files = new LinkedList<String>();
		for(File f : list){
			files.add(f.getAbsolutePath());
		}
		
		for(String file : files){
			String result = uploadFile(file);
			System.out.println(String.format("hello:%s", result));
		}
	}
	
	private String getFullName(String name){
		return String.format("http://emoji.b0.upaiyun.com/%s", name);
	}
	
	private String uploadFile(String file) throws UpYunException{
		
		String result = file.substring(file.lastIndexOf(File.separator) + 1, file.length());
		
		String path = File.separator + "test" + File.separator + result;
		// 取得base64编码后的policy
		String policy = UpYunUtils.makePolicy(path, EXPIRATION, BUCKET);
		// 根据表单api签名密钥对policy进行签名
		// 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
		String signature = UpYunUtils.signature(policy + "&" + TEST_API_KEY);
		// 上传文件到对应的bucket中去。
		String string = Uploader.upload(policy, signature, BUCKET, file);
		return string;
	}
}
