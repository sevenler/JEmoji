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

public class LoginActivity extends BaseActivity {
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
					System.out.println(String.format("user: %s ", user));
					HomeActivity.putValus("user", user);
					openActivity(HomeActivity.class, null);
					break;
			}
		}
	}
}
