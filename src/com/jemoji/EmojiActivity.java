package com.jemoji;

import java.io.File;
import java.util.List;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.models.Emoji;
import com.jemoji.models.MessageCenter;
import com.jemoji.models.User;
import com.jemoji.utils.VoiceHandler;
import com.jemoji.utils.VoiceHandler.OnHandListener;

public class EmojiActivity extends BaseActivity {
	User from;//消息来源用户
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		WebPageFragment mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();
		
		from = (User)pokeValus("user");
	}

	class WebPageFragment extends Fragment implements OnClickListener  {
		public WebPageFragment() {
			voicePlayHandler = new VoiceHandler();
			voicePlayHandler.setOnHandListener(new OnHandListener() {
				@Override
				public void onRecored(boolean isFinish, int time, String file) {
				}

				@Override
				public void onPlay(boolean isFinish) {
				}
			});
		}
		
		VoiceHandler voicePlayHandler;
		ValueAnimator voicePlayAnimation;
		Emoji mEmoji;
		View iv_voice_panel;
		ImageView image;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_emoji, container, false);
			ControlScrollViewPager viewPager = (ControlScrollViewPager)rootView.findViewById(R.id.face_pager);
			EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity());
			final List<Emoji> list = initEmojiData();
			emojiAdapter.setData(list);
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					mEmoji = list.get(arg0);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			viewPager.setAdapter(emojiAdapter);
			
			ImageView header = (ImageView)rootView.findViewById(R.id.from_user_header);
			TextView name = (TextView)rootView.findViewById(R.id.from_user_name);
			header.setImageResource(from.getHeader());
			name.setText(String.format("来自 %s", from.getNickname()));
			iv_voice_panel = (View)rootView.findViewById(R.id.iv_voice_panel);
			image = (ImageView)rootView.findViewById(R.id.iv_voice);
			mEmoji = (Emoji)list.get(0);
			iv_voice_panel.setOnClickListener(this);
			rootView.findViewById(R.id.close).setOnClickListener(this);
			
			return rootView;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			playVoice();//自动播放声音
		}

		private List<Emoji> initEmojiData() {
			List<Emoji> emojis = MessageCenter.instance().pokeUnread(from.getUsername());
			return emojis;
		}

		private void startVioceAnimation(final ImageView iv_voice, int length) {
			voicePlayAnimation = ValueAnimator.ofInt(1, 100);
			voicePlayAnimation.setDuration(length);
			voicePlayAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
			voicePlayAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					Integer value = (Integer)animation.getAnimatedValue();
					if (value % 21 == 0) {
						iv_voice.setImageResource(R.drawable.chatfrom_voice_playing_f1);
					} else if (value % 21 == 7) {
						iv_voice.setImageResource(R.drawable.chatfrom_voice_playing_f2);
					} else if (value % 21 == 14) {
						iv_voice.setImageResource(R.drawable.chatfrom_voice_playing_f3);
					}
					if (value == 100) {
						iv_voice.setImageResource(R.drawable.chatfrom_voice_playing);
					}
				}
			});
			voicePlayAnimation.start();
		}
		
		private void stopVioceAnimation(final ImageView iv_voice){
			if (voicePlayAnimation != null) voicePlayAnimation.cancel();
			iv_voice.setImageResource(R.drawable.chatfrom_voice_playing);
		}
		
		private void playOrStopVoice(String voicePath){
			if (voicePlayHandler.isVoicePlaying()) stopVioceAnimation(image);
			else startVioceAnimation(image, 1000 * 4);
			voicePlayHandler.playOrStop(voicePath);
		}
		
		private void playVoice(){
			String voicepath = mEmoji.getVoice();
			if((voicepath !=null) && new File(voicepath).exists()){
				playOrStopVoice(voicepath);
			}else{
				String voiceUrl = mEmoji.getVoiceUrl();
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
						+ "emojis" + File.separator + System.currentTimeMillis() +  ".amr";
				GKHttpInterface.genFile(voiceUrl, "amr", path, new GKJsonResponseHandler() {
					@Override
					public void onResponse(int code, Object file, Throwable error) {
						mEmoji.setVoice((String)file);
						playOrStopVoice(mEmoji.getVoice());
					}
				});
			}
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.iv_voice_panel:
					playVoice();
					break;
				case R.id.close:
					finish();
					break;
			}
		}
	}
}
