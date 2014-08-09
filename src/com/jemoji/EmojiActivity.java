package com.jemoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jemoji.utils.ImageCacheManager;

public class EmojiActivity extends BaseActivity {
	Emoji emoji;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		WebPageFragment mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();
		
		emoji = (Emoji)EmojiActivity.pokeValus("emoji");
	}

	class WebPageFragment extends Fragment  implements OnClickListener{
		public WebPageFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_emoji, container, false);
			NetworkImageView image = (NetworkImageView)rootView.findViewById(R.id.image);

			ImageLoader loder = ImageCacheManager.instance().getImageLoader();
			image.setImageUrl(emoji.getImage(), loder);
			image.setOnClickListener(new VoicePlayClickListener(getActivity(), emoji));
			return rootView;
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.image:
					break;
			}
		}

	}
}
