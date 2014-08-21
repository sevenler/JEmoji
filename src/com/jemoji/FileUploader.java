package com.jemoji;

import java.io.File;

import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.models.Emoji;
import com.jemoji.models.UserCenter;
import com.jemoji.utils.ErrorCenter;
import com.jemoji.utils.Utility;
import com.upyun.api.Uploader;
import com.upyun.api.utils.UpYunException;
import com.upyun.api.utils.UpYunUtils;

public  class FileUploader {
	private static final String TEST_API_KEY = "h2u0v4S4BRUtwQphIj6sBy+JVMo="; // 测试使用的表单api验证密钥
	private static final String BUCKET = "emoji"; // 存储空间
	
	public static interface UploadCallBack{
		public void onResult();
	}
	
	private String sendFile(String file, String type){
		String result = System.currentTimeMillis() + "." + type;
		try {
			// 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
			String path = File.separator + "test" + File.separator + result;

			long EXPIRATION = System.currentTimeMillis() / 1000 + 1000 * 5 * 10;
			// 取得base64编码后的policy
			String policy = UpYunUtils.makePolicy(path, EXPIRATION, BUCKET);

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
	
	public void send(final Emoji emoji, final String toChatUser){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String imageUrlName = emoji.getImageUrl();
				System.out.println(String.format(" imageUrlName:%s %s", imageUrlName, Utility.Strings.isEmptyString(imageUrlName)));
				if(Utility.Strings.isEmptyString(imageUrlName)){
					String imageName = emoji.getImage();
					String type = imageName.substring(imageName.lastIndexOf(".") + 1, imageName.length()); 
					String result = sendFile(imageName, type);
					emoji.setImageUrl(result);
					imageUrlName = emoji.getImageUrl();
				}else{
					imageUrlName = imageUrlName.substring(imageUrlName.lastIndexOf("/") + 1, imageUrlName.length());
				}
				System.out.println(String.format(" imageUrlName:%s ", imageUrlName));
				String voiceUrl = sendFile(emoji.getVoice(), "amr");
				emoji.setVoiceUrl(voiceUrl);
				
				String me = UserCenter.instance().getMe().getUsername();
				String content = String.format("{\"message\":\"%s,%s,%s,%s\"}", voiceUrl, imageUrlName, emoji.getBackground(), me);
				GKHttpInterface.pushMessage(toChatUser, content, new GKJsonResponseHandler() {
					@Override
					public void onResponse(int code, Object json, Throwable error) {
						if(error == null){
							System.out.println(String.format(" onResponse json %s   user:[%s] ", json, toChatUser));
						}else{
							ErrorCenter.instance().onError(error);
						}
					}
				});
			}
		}).start();
	}
}
