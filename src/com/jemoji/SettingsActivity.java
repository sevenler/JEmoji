
package com.jemoji;

import com.jemoji.utils.PreferManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class SettingsActivity extends BaseActivity {
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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootview = inflater.inflate(R.layout.fragment_settings, container, false);
			rootview.findViewById(R.id.logout).setOnClickListener(this);
			return rootview;
		}

		public static final String KEY_USER_NAME = "KEY_USER_NAME";
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.logout:
					finish();
					PreferManager.instance().setStringToPrefs(getActivity(), KEY_USER_NAME, null);
					openActivity(LoginActivity.class, null);
					break;
			}
		}
	}
}
