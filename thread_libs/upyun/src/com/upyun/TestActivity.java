package com.upyun;

import java.io.File;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.upyun.api.Uploader;
import com.upyun.api.utils.UpYunException;
import com.upyun.api.utils.UpYunUtils;

public class TestActivity extends Activity {

	private static final String TEST_API_KEY = "h2u0v4S4BRUtwQphIj6sBy+JVMo="; //测试使用的表单api验证密钥
	private static final String BUCKET = "emoji";						//存储空间
	private static final long EXPIRATION = System.currentTimeMillis()/1000 + 1000 * 5 * 10; //过期时间，必须大于当前时间
	
	private static final String SOURCE_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator + "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr"; //来源文件

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new UploadTask().execute();
	}

	public class UploadTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			String string = null;
			try {
				//设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
				String SAVE_KEY = File.separator + "test" + File.separator + System.currentTimeMillis()+".amr";
				
				System.out.println(String.format(" ========== %s ", SAVE_KEY));
				
				//取得base64编码后的policy
				String policy = UpYunUtils.makePolicy(SAVE_KEY, EXPIRATION, BUCKET);
				
				//根据表单api签名密钥对policy进行签名
				//通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
				String signature = UpYunUtils.signature(policy + "&" + TEST_API_KEY);

				//上传文件到对应的bucket中去。
				string = Uploader.upload(policy, signature , BUCKET, SOURCE_FILE);

			} catch (UpYunException e) {
				e.printStackTrace();
			}

			return string;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();

			}
		}

	}
}