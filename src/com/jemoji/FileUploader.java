package com.jemoji;

import java.io.File;

import com.upyun.api.Uploader;
import com.upyun.api.utils.UpYunException;
import com.upyun.api.utils.UpYunUtils;

public  class FileUploader {
	private static final String TEST_API_KEY = "h2u0v4S4BRUtwQphIj6sBy+JVMo="; // 测试使用的表单api验证密钥
	private static final String BUCKET = "emoji"; // 存储空间
	private static final long EXPIRATION = System.currentTimeMillis() / 1000 + 1000 * 5 * 10; // 过期时间，必须大于当前时间
	
	public static interface UploadCallBack{
		public void onResult();
	}
	
	private String sendFile(String file, String type){
		String result = null;
		try {
			// 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
			result = File.separator + "test" + File.separator + System.currentTimeMillis() + "." + type;

			// 取得base64编码后的policy
			String policy = UpYunUtils.makePolicy(result, EXPIRATION, BUCKET);

			// 根据表单api签名密钥对policy进行签名
			// 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
			String signature = UpYunUtils.signature(policy + "&" + TEST_API_KEY);

			// 上传文件到对应的bucket中去。
			String string = Uploader.upload(policy, signature, BUCKET, file);
			
			if (string != null) {
				System.out.println(String.format("成功 "));
			} else {
				System.out.println(String.format("失败 "));
			}

		} catch (UpYunException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void send(final Emoji emoji){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = sendFile(emoji.getImage(), "jpg");
				System.out.println(String.format(" ===== %s ", result));
				String result1 = sendFile(emoji.getVoice(), "amr");
				System.out.println(String.format(" ===== %s ", result1));
			}
		}).start();
	}
}