
package com.jemoji;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
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
import com.jemoji.models.EmojiCenter;
import com.jemoji.models.User;
import com.jemoji.models.UserCenter;
import com.jemoji.utils.Utility;
import com.jemoji.utils.VoiceHandler;
import com.jemoji.utils.VoiceHandler.OnHandListener;

public class HomeActivity extends BaseActivity {
	Emoji mEmoji;
	User toChat;

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

		User me = UserCenter.instance().getMe();
		setTag(me.getUsername());
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
		private CircleImageView to_chat_user_header;//对话的好友头像
		private TextView notice_message;//提示文字
		private TextView unread_msg_number;
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
			final String username = messages[3];

			// 收到消息就开始下载
			ImageLoader loder = ImageCacheManager.instance().getImageLoader();
			loder.get(emoji.getImageUrl(), new ImageListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
				}

				@Override
				public void onResponse(ImageContainer arg0, boolean arg1) {
					String image = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ File.separator + "emojis_download" + File.separator
							+ System.currentTimeMillis() + ".png";
					try {
						Bitmap bitmap = arg0.getBitmap();
						if(bitmap != null){
							Utility.File.saveBitmap(new File(image), bitmap);
							emoji.setVoiceStatus(Emoji.STATUS_REMOTE);
							emoji.setImage(image);
							EmojiCenter.instance().pushUnread(username, emoji);
							
							unread_msg_number.setVisibility(View.VISIBLE);
							unread_msg_number.setText("" + EmojiCenter.instance().getUnreadCount());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
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
		
//		private void showBigImage(){
//			if (mSpring.getEndValue() == 0) {
//				mSpring.setEndValue(1);
//				Emoji emoji = (Emoji)v.getTag();
//				VoicePlayClickListener mVoicePlayClickListener = new VoicePlayClickListener(HomeActivity.this, emoji);
//				emojiImage.setOnClickListener(mVoicePlayClickListener);
//			}
//		}
		
		//初始化联系人头像
		private void initContactHeaders(View rootview){
			Context context = rootview.getContext();
			LayoutInflater LayoutInflater = (LayoutInflater)context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					User user = (User)v.getTag(R.id.tag_key_header_user);
					changeChatUser(user);
				}
			};
			Collection<User> users = UserCenter.instance().getAll();
			int i = 0;
			for(User user : users){
				int res = i++ / 4;
				LinearLayout layout1 = (LinearLayout)rootview.findViewById(R.id.user_header_panel_1 + res);
				View header = LayoutInflater.inflate(R.layout.include_user_header, layout1, false);
				ImageView image = (ImageView)header.findViewById(R.id.header);
				image.setImageResource(user.getHeader());
				header.setTag(R.id.tag_key_header_user, user);
				header.setOnClickListener(listener);
				layout1.addView(header);
			}
		}
		
		private void initView(View rootview) {
			//初始化联系人头像
			initContactHeaders(rootview);

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

			// 初始化录音按钮
			View buttonPressToSpeak = rootview.findViewById(R.id.btn_press_to_speak);
			voicePlayHandler = new VoiceHandler();
			voicePlayHandler.setOnHandListener(new OnHandListener() {
				@Override
				public void onRecored(boolean isFinish, int time, String file) {
					mEmoji.setVoice(file);
					sendMessage(toChat, mEmoji);
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
			unread_msg_number = (TextView)rootview.findViewById(R.id.unread_msg_number);
			unread_msg_number.setVisibility(View.GONE);
			unread_msg_number.setOnClickListener(this);
		}
		
		private void changeChatUser(User toUser){
			BaseViewAnimator animator = ((BaseViewAnimator) (Techniques.BounceInUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator()).animate(to_chat_user_header);
			
			to_chat_user_header.setImageResource(toUser.getHeader());
			notice_message.setText(String.format("发送给 %s",toUser.getNickname()));
			toChat = toUser;
		}
		
		//发送消息
		private void sendMessage(User toChat, Emoji emoji){
			emoji.send(toChat.getUsername());
			
			BaseViewAnimator animator = ((BaseViewAnimator) (Techniques.SlideOutUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator()).animate(to_chat_user_header);
			
			notice_message.setText(String.format("已经发送给 %s", toChat.getNickname()));
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
				case R.id.settings:
					openActivity(SettingsActivity.class, null);
					break;
				case R.id.iv_voice_panel:
					ImageView image = (ImageView)v.findViewById(R.id.iv_voice);
					if (voicePlayHandler.isVoicePlaying()) stopVioceAnimation(image);
					else startVioceAnimation(image, 1000 * 4);
					voicePlayHandler.playOrStop(mEmoji.getVoice());
					break;
				case R.id.unread_msg_number:
					EmojiActivity.putValus("user", toChat);
					openActivity(EmojiActivity.class, null);
					break;
			}
		}
	}
}
