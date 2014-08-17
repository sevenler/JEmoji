package com.jemoji;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jemoji.models.EmojiSelector;
import com.jemoji.models.User;
import com.jemoji.models.UserCenter;
import com.jemoji.utils.PreferManager;

public class LoginActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		checkLogin();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		WebPageFragment mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();
	}
	
	public static final String KEY_USER_NAME = "KEY_USER_NAME";
	
	public void checkLogin(){
		String user = PreferManager.instance().getStringFromPrefs(this, KEY_USER_NAME, null);
		if(user != null){
			redirect(user);
		}
	}

	public void redirect(String name){
		User user = UserCenter.instance().get(name);
		if (user != null) {
			UserCenter.instance().setMe(user);
			System.out.println(String.format(" user:%s ", user.getNickname()));
			openActivity(HomeActivity.class, null);
		}
	}
	
	class WebPageFragment extends Fragment implements OnClickListener {
		public WebPageFragment() {
		}
		
		EditText username;
		ProgressBar progressbar;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			username = (EditText)rootView.findViewById(R.id.username);
			rootView.findViewById(R.id.login).setOnClickListener(this);
			progressbar = (ProgressBar)rootView.findViewById(R.id.progressbar);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			
			if(!EmojiSelector.instance(getActivity()).isOfficialEmojiExsit()){
				progressbar.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						saveEmojiFromAssert(getAssets());
						runOnUiThread(new Runnable() {
							public void run() {
								progressbar.setVisibility(View.GONE);
							}
						});
					}
				}).start();;
			}else{
				progressbar.setVisibility(View.GONE);
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.login:
					String user = username.getText().toString();
					PreferManager.instance().setStringToPrefs(getActivity(), KEY_USER_NAME, user);
					redirect(user);
					break;
			}
		}
		
		public void saveEmojiFromAssert(AssetManager assetManager){
			try {
				String[] list = assetManager.list("emojis");
				for(String str : list){
					String targetfile = String.format("/sdcard/test_image/%s", str);
					save2File(str, new File(targetfile), assetManager);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void save2File(String assertfile, File targetfile, AssetManager assetManager)
				throws IOException {
			File path = targetfile.getParentFile();
			if (!path.exists()) path.mkdirs();
			if (!targetfile.exists()) targetfile.createNewFile();

			final OutputStream outputStream = new FileOutputStream(targetfile);

			final byte[] largeBuffer = new byte[1024 * 4];
			int bytesRead = 0;

			final InputStream inputStream = assetManager.open(String.format("emojis/%s", assertfile));
			while ((bytesRead = inputStream.read(largeBuffer)) > 0) {
				if (largeBuffer.length == bytesRead) {
					outputStream.write(largeBuffer);
				} else {
					final byte[] shortBuffer = new byte[bytesRead];
					System.arraycopy(largeBuffer, 0, shortBuffer, 0, bytesRead);
					outputStream.write(shortBuffer);
				}
			}
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		}
	}
}
