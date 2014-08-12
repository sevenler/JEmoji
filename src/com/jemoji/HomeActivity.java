
package com.jemoji;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.animation.ValueAnimator;
import android.graphics.Color;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
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
		private ImageView emojiImage;//表情大图
		private TextView unread_msg_number;//未读消息数量
		private CircleImageView to_chat_user_header;//对话的好友头像
		private TextView notice_message;//提示文字
		ValueAnimator voicePlayAnimation;
		VoiceHandler voicePlayHandler;

		private Spring mSpring;

		// 大图动画回调
		private void render() {
			double value = mSpring.getCurrentValue();

			float selectedPhotoScale = (float)SpringUtil
					.mapValueFromRangeToRange(value, 0, 1, 0, 1);
			selectedPhotoScale = Math.max(selectedPhotoScale, 0);
			emojiImage.setScaleX(selectedPhotoScale);
			emojiImage.setScaleY(selectedPhotoScale);
		}

		// 接收消息回调
		public void onReceiveMessage(String values) {
			String[] messages = values.split(",");
			String voiceUrl = URLs.getAbsoluteUrl(String.format("/%s", messages[0]));
			String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator
					+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
			final Emoji emoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, voiceUrl);
			emoji.setImageUrl(String.format("http://emoji.b0.upaiyun.com/test/%s", messages[1]));
			emoji.setBackground(Integer.parseInt(messages[2]));

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
					
					emojiImage.setImageBitmap(arg0.getBitmap());
					emojiImage.setBackgroundColor(emoji.getBackground());
					unread_msg_number.setVisibility(View.VISIBLE);
					unread_msg_number.setText("1");
					unread_msg_number.setTag(emoji);
				}
			});
		}

		// 拦截back键
		public boolean interaptBack() {
			if (mSpring.getEndValue() == 1) {
				mSpring.setEndValue(0);
				emojiImage.setOnClickListener(null);
				emojiImage.setClickable(false);
				return true;
			} else return false;
		}

		private void initView(View rootview) {
			// 初始化头像
			CircleImageView header = (CircleImageView)rootview.findViewById(R.id.send);
			header.setOnClickListener(this);
			header.setImageResource(user.getHeader());
			header.setTag(user);

			// 初始化表情列表
			rootview.findViewById(R.id.settings).setOnClickListener(this);
			ControlScrollViewPager mViewPager;
			mViewPager = (ControlScrollViewPager)rootview.findViewById(R.id.face_pager);
			EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity());
			emojiAdapter.setData(initEmojiData(new ArrayList<Map<?, ?>>()));
			mViewPager.setAdapter(emojiAdapter);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					mEmoji.setImage(EmojiSelector.instance().getEmojiName(arg0));
					mEmoji.setBackground(EmojiSelector.instance().getEmojiBackground(arg0));
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			// mViewPager.setScrollable(false);

			// 初始化表情大图View
			emojiImage = (ImageView)rootview.findViewById(R.id.image);
			final View panel_main = rootview.findViewById(R.id.panel_main);
			mSpring = SpringSystem.create().createSpring()
					.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(200, 4.3))
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

			// 初始化录音按钮
			View buttonPressToSpeak = rootview.findViewById(R.id.btn_press_to_speak);
			voicePlayHandler = new VoiceHandler();
			voicePlayHandler.setOnHandListener(new OnHandListener() {
				@Override
				public void onRecored(boolean isFinish, int time, String file) {
					mEmoji.setVoice(file);
					sendMessage();
				}

				@Override
				public void onPlay(boolean isFinish) {
				}
			});
			buttonPressToSpeak.setOnTouchListener(voicePlayHandler);

			// 初始化播放按钮
			rootview.findViewById(R.id.iv_voice_panel).setOnClickListener(this);
			to_chat_user_header = (CircleImageView)rootview.findViewById(R.id.to_chat_user_header);
			notice_message = (TextView)rootview.findViewById(R.id.notice_message);
		}
		
		private void changeChatUser(User user){
			BaseViewAnimator animator = ((BaseViewAnimator) (Techniques.BounceInUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator()).animate(to_chat_user_header);
			
			to_chat_user_header.setImageResource(user.getHeader());
			notice_message.setText(String.format("发送给 %s",user.getUsername()));
		}
		
		//发送消息
		private void sendMessage(){
			String friend = "18511557126";
			mEmoji.send(friend);
			
			BaseViewAnimator animator = ((BaseViewAnimator) (Techniques.SlideOutUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator()).animate(to_chat_user_header);
			
			notice_message.setText(String.format("已经发送给 %s",user.getUsername()));
			notice_message.postDelayed(new Runnable() {
				@Override
				public void run() {
					notice_message.setText("");
				}
			}, 1000 * 5);
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
				map.put("emoji", selector.getEmojiName(i));
				map.put("background", selector.getEmojiBackground(i));
				list.add(map);
			}
			return list;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.send:
					User user = (User)v.getTag();
					changeChatUser(user);
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
						emojiImage.setOnClickListener(mVoicePlayClickListener);
					}
					break;
				case R.id.iv_voice_panel:
					ImageView image = (ImageView)v.findViewById(R.id.iv_voice);
					if (voicePlayHandler.isVoicePlaying()) stopVioceAnimation(image);
					else startVioceAnimation(image, 1000 * 4);
					voicePlayHandler.playOrStop(mEmoji.getVoice());
					break;
			}
		}
	}
}



class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance() {
		if (instance == null) instance = new EmojiSelector();
		return instance;
	}

	public List<Emoji> emojis = new LinkedList<Emoji>();

	public EmojiSelector() {
		emojis.add(new Emoji("001.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1002.gif", Color.parseColor("#ffffff")));
		
		emojis.add(new Emoji("1001.gif", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1003.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1004.gif", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1005.gif", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0272.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0278.JPG", Color.parseColor("#FEFFBB")));
		emojis.add(new Emoji("IMG_0284.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0267.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0291.JPG", Color.parseColor("#AADFFF")));
		emojis.add(new Emoji("IMG_0273.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("despicable-me-2-Minion-icon-5.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0262.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0268.JPG", Color.parseColor("#ffffff")));
	}

	public String getEmojiName(int index) {
		return String.format("%s/%s", "/sdcard/emojis", emojis.get(index).getImage());
	}
	
	public int getEmojiBackground(int index) {
		return emojis.get(index).getBackground();
	}

	public int size() {
		return emojis.size();
	}
}
