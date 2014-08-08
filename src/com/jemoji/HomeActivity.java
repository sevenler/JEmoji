
package com.jemoji;

import java.io.File;

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
		System.out.println(String.format(" ==========  %s ============== ", values));
	}
	
	class WebPageFragment extends Fragment implements OnClickListener {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			Button send = (Button)rootView.findViewById(R.id.send);
			send.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.send:
					String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ File.separator
							+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
					new FileUploader().send(new Emoji("sdcard/emojis/IMG_0286.JPG", voice));
					break;
			}
		}
	}
}
