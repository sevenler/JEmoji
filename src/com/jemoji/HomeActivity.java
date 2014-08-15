
package com.jemoji;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.jemoji.models.Emoji;
import com.jemoji.models.EmojiSelector;
import com.jemoji.models.MessageCenter;
import com.jemoji.models.MessageCenter.OnReceiveMessageDelegate;
import com.jemoji.models.User;
import com.jemoji.models.UserCenter;
import com.jemoji.utils.ErrorCenter;
import com.jemoji.utils.ErrorCenter.ErrorDelegate;
import com.jemoji.utils.Utility;
import com.jemoji.utils.VoiceHandler;
import com.jemoji.utils.VoiceHandler.OnHandListener;

public class HomeActivity extends BaseActivity implements ErrorDelegate{
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

		User me = UserCenter.instance().getMe();
		setTag(me.getUsername());
		
		ErrorCenter.instance().regesterErrorDelegate(this);
	}

	@Override
	public void onError(Exception ex) {
			if(ex instanceof UnknownHostException){
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(HomeActivity.this, "发送失败，请先检查网络", Toast.LENGTH_LONG).show();
					}
				});
			}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ErrorCenter.instance().unregesterErrorDelegate(this);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebPageFragment.interaptBack()) return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class WebPageFragment extends Fragment implements OnClickListener, OnReceiveMessageDelegate {
		private ImageView emojiImage;// 表情大图
		private CircleImageView to_chat_user_header;// 对话的好友头像
		private TextView notice_message;// 提示文字
		private View unread_msg_number;// 未读消息数量
		private Button buttonPressToSpeak;//发送语音消息按钮

		ValueAnimator voicePlayAnimation;
		ValueAnimator recevingMessageAnimation;
		VoiceHandler voicePlayHandler;

		private Spring mSpring;

		public WebPageFragment(){
			MessageCenter.instance().regesterReceiveMessageDelegate(this);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			MessageCenter.instance().unregesterReceiveMessageDelegate(this);
		}

		// 大图动画回调
		private void render() {
			double value = mSpring.getCurrentValue();

			float selectedPhotoScale = (float)SpringUtil
					.mapValueFromRangeToRange(value, 0, 1, 0, 1);
			selectedPhotoScale = Math.max(selectedPhotoScale, 0);
			emojiImage.setScaleX(selectedPhotoScale);
			emojiImage.setScaleY(selectedPhotoScale);
		}

		@Override
		public void onReceiveMessage(Emoji emoji) {
			startRecevingMessageAnimation(unread_msg_number);
		}

		@Override
		public void onDownloadMessage(Emoji emoji, String file) {
			unread_msg_number.setVisibility(View.VISIBLE);
			((TextView)unread_msg_number.findViewById(R.id.textview)).setText(""
					+ MessageCenter.instance().getUnreadCount());
			stopRecevingMessageAnimation(unread_msg_number);
			
			BaseViewAnimator animator = ((BaseViewAnimator)(Techniques.BounceIn.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator())
					.animate(unread_msg_number);
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

		// 跳出大图动画
		private void showBigImage() {
			if (mSpring.getEndValue() == 0) {
				mSpring.setEndValue(1);
			}
		}
		
		private ViewGroup previewSelectedUserHeader;

		// 初始化联系人头像
		private void initContactHeaders(View rootview) {
			Context context = rootview.getContext();
			LayoutInflater LayoutInflater = (LayoutInflater)context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					User user = (User)v.getTag(R.id.tag_key_header_user);
					changeChatUser(user);
					
					if(previewSelectedUserHeader != null)previewSelectedUserHeader.getChildAt(0).setBackgroundResource(R.drawable.gray_circle);
					ViewGroup vg = (ViewGroup)v;
					vg.getChildAt(0).setBackgroundResource(R.drawable.gray_circle_selected);
					previewSelectedUserHeader = vg;
				}
			};
			User[] users = UserCenter.instance().getAll().toArray(new User[UserCenter.instance().getAll().size()]);
			for(int i  = 0 ; i <12 ; i++){
				int res = i / 4;
				LinearLayout layout1 = (LinearLayout)rootview.findViewById(R.id.user_header_panel_1
						+ res);
				View header = LayoutInflater.inflate(R.layout.include_user_header, layout1, false);
				ImageView image = (ImageView)header.findViewById(R.id.header);
				TextView nickname = (TextView)header.findViewById(R.id.nickname);
				layout1.addView(header);
				
				if (i < users.length) {
					User user = users[i];
					nickname.setVisibility(View.VISIBLE);
					nickname.setText(user.getNickname());
					image.setImageResource(user.getHeader());
					header.setTag(R.id.tag_key_header_user, user);
					header.setOnClickListener(listener);
				} else {
					nickname.setVisibility(View.INVISIBLE);
				}
			}
		}

		private void initView(View rootview) {
			// 初始化联系人头像
			initContactHeaders(rootview);

			// 初始化表情列表
			rootview.findViewById(R.id.settings).setOnClickListener(this);
			ControlScrollViewPager mViewPager;
			mViewPager = (ControlScrollViewPager)rootview.findViewById(R.id.face_pager);
			EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity());
			emojiAdapter.setData(initEmojiData());
			mViewPager.setAdapter(emojiAdapter);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					mEmoji = EmojiSelector.instance().get(arg0);
					
					System.out.println(String.format(" selected emoji %s ", mEmoji));
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			mEmoji = EmojiSelector.instance().get(0);
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
			buttonPressToSpeak = (Button)rootview.findViewById(R.id.btn_press_to_speak);
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
			unread_msg_number = rootview.findViewById(R.id.unread_msg_number);
			unread_msg_number.setOnClickListener(this);
		}

		private void changeChatUser(User toUser) {
			BaseViewAnimator animator = ((BaseViewAnimator)(Techniques.BounceInUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator())
					.animate(to_chat_user_header);

			to_chat_user_header.setImageResource(toUser.getHeader());
			notice_message.setText(String.format("发送给 %s", toUser.getNickname()));
			toChat = toUser;
			buttonPressToSpeak.setEnabled(true);
			buttonPressToSpeak.setText("长按发送语音");
		}

		// 发送消息
		private void sendMessage(User toChat, Emoji emoji) {
			emoji.send(toChat.getUsername());

			BaseViewAnimator animator = ((BaseViewAnimator)(Techniques.SlideOutUp.getAnimator()));
			animator.setDuration(1000).setInterpolator(new AccelerateInterpolator())
					.animate(to_chat_user_header);

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

		private void stopVioceAnimation(final ImageView iv_voice) {
			if (voicePlayAnimation != null) voicePlayAnimation.cancel();
			iv_voice.setImageResource(R.drawable.chatfrom_voice_playing);
		}

		private void startRecevingMessageAnimation(final View view) {
			if(recevingMessageAnimation != null && recevingMessageAnimation.isRunning()) return;
			
			recevingMessageAnimation = ValueAnimator.ofInt(1, 100 * 10000);
			recevingMessageAnimation.setDuration(100 * 10000);
			recevingMessageAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
			recevingMessageAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					Integer value = (Integer)animation.getAnimatedValue();

					if (value % 10 == 0) {
						((ImageView)view.findViewById(R.id.imageview))
								.setImageResource(R.drawable.red_circle_little);
					} else if (value % 10 == 5) {
						((ImageView)view.findViewById(R.id.imageview)).setImageBitmap(null);
					}
				}
			});
			recevingMessageAnimation.start();
			unread_msg_number.setVisibility(View.VISIBLE);
			unread_msg_number.setClickable(false);
			((TextView)view.findViewById(R.id.textview)).setText("");
			((ImageView)view.findViewById(R.id.imageview))
					.setImageResource(R.drawable.red_circle_little);
		}

		private void stopRecevingMessageAnimation(final View view) {
			if (recevingMessageAnimation != null) recevingMessageAnimation.cancel();
			((ImageView)view.findViewById(R.id.imageview)).setImageResource(R.drawable.red_circle);
			unread_msg_number.setClickable(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			initView(rootView);

			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();

			int count = MessageCenter.instance().getUnreadCount();
			((TextView)unread_msg_number.findViewById(R.id.textview)).setText("" + count);
			if (count <= 0) {
				unread_msg_number.setVisibility(View.GONE);
				((TextView)unread_msg_number.findViewById(R.id.textview)).setText("");
			}
		}

		private List<Emoji> initEmojiData() {
			List<Emoji> list = new LinkedList<Emoji>(); 
			EmojiSelector selector = EmojiSelector.instance();
			for (int i = 0; i < selector.size(); i++) {
				list.add(selector.get(i));
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
					String voice = mEmoji.getVoice();
					if (!Utility.Strings.isEmptyString(voice)) {
						ImageView image = (ImageView)v.findViewById(R.id.iv_voice);
						if (voicePlayHandler.isVoicePlaying()) stopVioceAnimation(image);
						else startVioceAnimation(image, 1000 * 4);
						voicePlayHandler.playOrStop(voice);
					}
					break;
				case R.id.unread_msg_number:
					String user = MessageCenter.instance().getTopUser();
					EmojiActivity.putValus("user", UserCenter.instance().get(user));
					openActivity(EmojiActivity.class, null);
					break;
			}
		}
	}
}
