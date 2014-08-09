
package com.jemoji;

import java.io.File;

import com.jemoji.http.URLs;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		WebPageFragment mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();
	}

	@Override
	public void onReceiveMessage(String values) {
		super.onReceiveMessage(values);
	}
	
	class WebPageFragment extends Fragment implements OnClickListener {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			Button send = (Button)rootView.findViewById(R.id.send);
			send.setOnClickListener(this);
			Button recive = (Button)rootView.findViewById(R.id.recive);
			recive.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			String url = URLs.getAbsoluteUrl("/1407549723664.amr");
			String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator
					+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
			Emoji emoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, url);
			
			switch (v.getId()) {
				case R.id.send:
					new FileUploader().send(emoji);
					break;
				case R.id.recive:
					
					emoji.setImage("http://emoji.b0.upaiyun.com/test/1407524257043.jpg");
						
					EmojiActivity.putValus("emoji", emoji);
					openActivity(EmojiActivity.class, null);
					break;
			}
		}
	}
}
