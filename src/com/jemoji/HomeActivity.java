
package com.jemoji;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.dd.CircularProgressButton;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.jemoji.WaveView.OnWaveListener;
import com.jemoji.http.URLs;
import com.jemoji.image.ImageCacheManager;
import com.jemoji.models.Emoji;
import com.jemoji.models.User;
import com.jemoji.utils.VoiceHandler;
import com.jemoji.utils.VoiceHandler.OnHandListener;

public class HomeActivity extends BaseActivity {
	Emoji mEmoji;
	User user;

	WebPageFragment mWebPageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();

		String url = URLs.getAbsoluteUrl("/1407549723664.amr");
		String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator
				+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
		mEmoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, url);

		user = (User)HomeActivity.pokeValus("user");
		setTag(user.getUsername());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebPageFragment.interaptBack()) return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onReceiveMessage(String values) {
		mWebPageFragment.onReceiveMessage(values);
	}

	class WebPageFragment extends Fragment implements OnClickListener {
		private ImageView image;
		private TextView unread_msg_number;

		private Spring mSpring;
		
		private void render() {
			double value = mSpring.getCurrentValue();

			float selectedPhotoScale = (float)SpringUtil
					.mapValueFromRangeToRange(value, 0, 1, 0, 1);
			selectedPhotoScale = Math.max(selectedPhotoScale, 0);
			image.setScaleX(selectedPhotoScale);
			image.setScaleY(selectedPhotoScale);
		}

		public void onReceiveMessage(String values) {
			String[] messages = values.split(",");
			String voiceUrl = URLs.getAbsoluteUrl(String.format("/%s", messages[0]));
			String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator
					+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
			final Emoji emoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, voiceUrl);
			emoji.setImageUrl(String.format("http://emoji.b0.upaiyun.com/test/%s", messages[1]));

			// 收到消息就开始下载
			ImageLoader loder = ImageCacheManager.instance().getImageLoader();
			loder.get(emoji.getImageUrl(), new ImageListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
				}

				@Override
				public void onResponse(ImageContainer arg0, boolean arg1) {
					emoji.setVoiceStatus(Emoji.STATUS_MEMORY);
					emoji.setBitmap(arg0.getBitmap());
					unread_msg_number.setVisibility(View.VISIBLE);
					unread_msg_number.setText("1");
					image.setImageBitmap(arg0.getBitmap());
					unread_msg_number.setTag(emoji);
				}
			});
		}

		public boolean interaptBack() {
			if (mSpring.getEndValue() == 1) {
				mSpring.setEndValue(0);
				image.setOnClickListener(null);
				image.setClickable(false);
				return true;
			} else return false;
		}

		private void initView(View rootview){
			//初始化头像
			CircleImageView header = (CircleImageView)rootview.findViewById(R.id.send);
			header.setOnClickListener(this);
			header.setImageResource(user.getHeader());
			
			//初始化表情列表
			rootview.findViewById(R.id.settings).setOnClickListener(this);
			ControlScrollViewPager mViewPager;
			mViewPager = (ControlScrollViewPager)rootview.findViewById(R.id.face_pager);
			EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity());
			emojiAdapter.setData(initEmojiData(new ArrayList<Map<?, ?>>()));
			mViewPager.setAdapter(emojiAdapter);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					mEmoji.setImage(EmojiSelector.instance().getEmoji(arg0));
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			// mViewPager.setScrollable(false);
			
			//初始化表情大图View
			image = (ImageView)rootview.findViewById(R.id.image);
			final View panel_main = rootview.findViewById(R.id.panel_main);
			mSpring = SpringSystem.create().createSpring().setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(200, 4.3))
					.addListener(new SimpleSpringListener() {
						@Override
						public void onSpringUpdate(Spring spring) {
							render();
						}
					});
			panel_main.getViewTreeObserver().addOnGlobalLayoutListener(
					new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							render();
							panel_main.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						}
					});
			unread_msg_number = (TextView)rootview.findViewById(R.id.unread_msg_number);
			unread_msg_number.setVisibility(View.GONE);
			unread_msg_number.setOnClickListener(this);
			
			//初始化录音按钮
			View buttonPressToSpeak = rootview.findViewById(R.id.btn_press_to_speak);
			final VoiceHandler voiceHandler = new VoiceHandler();
			voiceHandler.setOnHandListener(new OnHandListener() {
				@Override
				public void onRecored(boolean isFinish, int time, String file) {
					mEmoji.setVoice(file);
				}

				@Override
				public void onPlay(boolean isFinish) {
				}
			});
			buttonPressToSpeak.setOnTouchListener(voiceHandler);
			
			//初始化播放按钮
			ImageView wave1 = (ImageView)rootview.findViewById(R.id.wave1);
			ImageView wave2 = (ImageView)rootview.findViewById(R.id.wave2);
			ImageView wave3 = (ImageView)rootview.findViewById(R.id.wave3);
			final CircularProgressButton pay = (CircularProgressButton)rootview
					.findViewById(R.id.pay);
			WaveView mWaveView = new WaveView(new OnWaveListener() {
				@Override
				public void onStop() {
					pay.setProgress(0);
					if (voiceHandler.isVoicePlaying()) voiceHandler.playOrStop(mEmoji.getVoice());
				}

				@Override
				public int onStart() {
					voiceHandler.playOrStop(mEmoji.getVoice());
					return 1000 * 4;
				}

				@Override
				public void onWaiting() {}
			}, pay, wave1, wave2, wave3);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			initView(rootView);

			return rootView;
		}

		private List<Map<?, ?>> initEmojiData(List<Map<?, ?>> list) {
			EmojiSelector selector = EmojiSelector.instance();
			for (int i = 0; i < selector.size(); i++) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("emoji", selector.getEmoji(i));
				list.add(map);
			}
			return list;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.send:
					String friend = "18511557126";
					mEmoji.send(friend);
					break;
				case R.id.settings:
					openActivity(SettingsActivity.class, null);
					break;
				case R.id.unread_msg_number:
					if (mSpring.getEndValue() == 0) {
						mSpring.setEndValue(1);

						Emoji emoji = (Emoji)v.getTag();
						VoicePlayClickListener mVoicePlayClickListener = new VoicePlayClickListener(
								HomeActivity.this, emoji);
						image.setOnClickListener(mVoicePlayClickListener);
					}
					break;
			}
		}
	}
}
