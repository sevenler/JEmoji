package com.jemoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
		System.out.println(String.format("user: %s ", name));
		
		User user = UserCenter.instance().get(name);
		if (user != null) {
			HomeActivity.putValus("user", user);
			openActivity(HomeActivity.class, null);
		}
	}
	
	class WebPageFragment extends Fragment implements OnClickListener {
		public WebPageFragment() {
		}
		
		EditText username;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			Button login = (Button)rootView.findViewById(R.id.login);
			username = (EditText)rootView.findViewById(R.id.username);
			login.setOnClickListener(this);
			return rootView;
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
	}
}
