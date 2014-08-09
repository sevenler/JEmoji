
package com.jemoji;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jemoji.http.URLs;
import com.jemoji.image.FileImageDecoder;
import com.jemoji.image.ImageSize;
import com.jemoji.image.ImageDecoder.ImageScaleType;
import com.jemoji.models.Emoji;
import com.jemoji.utils.VoiceHandler;
import com.jemoji.utils.VoiceHandler.OnHandListener;

public class HomeActivity extends BaseActivity {
	Emoji mEmoji;
	String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_container);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		WebPageFragment mWebPageFragment = new WebPageFragment();
		fragmentTransaction.replace(R.id.fragment, mWebPageFragment, "fragmentTag");
		fragmentTransaction.commit();
		
		String url = URLs.getAbsoluteUrl("/1407549723664.amr");
		String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator
				+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
		mEmoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, url);
		
		user = (String)HomeActivity.pokeValus("user");
		setTag(user);
	}

	@Override
	public void onReceiveMessage(String values) {
		super.onReceiveMessage(values);
		
		String[] messages = values.split(",");
		System.out.println(String.format("[%s,%s,%s]", values, messages[0], messages[1]));
		
		String voiceUrl = URLs.getAbsoluteUrl(String.format("/%s", messages[0]));
		String voice = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator
				+ "Android/data/com.easemob.chatuidemo/easemob-demo#chatdemoui/johnnyxyzw1/voice/johnnyxyz20140808T194607.amr";
		Emoji emoji = new Emoji("sdcard/emojis/IMG_0286.JPG", voice, voiceUrl);
		emoji.setImageUrl(String.format("http://emoji.b0.upaiyun.com/test/%s", messages[1]));
			
		EmojiActivity.putValus("emoji", emoji);
		openActivity(EmojiActivity.class, null);
	}
	
	class WebPageFragment extends Fragment implements OnClickListener {
		VoiceHandler voiceHandler;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			Button send = (Button)rootView.findViewById(R.id.send);
			send.setOnClickListener(this);
			
			ControlScrollViewPager mViewPager;
			mViewPager = (ControlScrollViewPager)rootView.findViewById(R.id.face_pager);
			MyPagerAdapter emojiAdapter = new MyPagerAdapter(getActivity());
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
//			mViewPager.setScrollable(false);
			
			
			View buttonPressToSpeak = rootView.findViewById(R.id.btn_press_to_speak);
			voiceHandler = new VoiceHandler();
			voiceHandler.setOnHandListener(new OnHandListener() {
				@Override
				public void onRecored(boolean isFinish, int time, String file) {
					System.out.println(String.format(" file:%s ", file));
					mEmoji.setVoice(file);
				}
				
				@Override
				public void onPlay(boolean isFinish) {
				}
			});
			buttonPressToSpeak.setOnTouchListener(voiceHandler);
			View play = rootView.findViewById(R.id.play);
			play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					voiceHandler.playOrStop(mEmoji.getVoice());
				}
			});
			
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
			}
		}
	}
}

class MyPagerAdapter extends PagerAdapter {

	private List<Map<?, ?>> list = new ArrayList<Map<?, ?>>();

	private Activity mContext;

	public MyPagerAdapter(Context context) {
		mContext = (Activity) context;
	}

	public void setData(List<Map<?, ?>> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		View rootview = LayoutInflater.from(mContext).inflate(R.layout.image_item, null, true);
		ImageView imageView = (ImageView) rootview.findViewById(R.id.imageView);
		
		
		FileImageDecoder decoder = new FileImageDecoder(new File((String) list.get(arg1).get("emoji")));
		try {
			Bitmap bitmap = decoder.decode(new ImageSize(510,  510), ImageScaleType.POWER_OF_2);
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		arg0.addView(rootview);
		
		return rootview;
	}
}

class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance() {
		if (instance == null)
			instance = new EmojiSelector();
		return instance;
	}

	public List<String> emojis = new LinkedList<String>();

	public EmojiSelector() {
		emojis.add("001.png");
		emojis.add("IMG_0259.JPG");

		emojis.add("IMG_0267.JPG");
		emojis.add("IMG_0272.JPG");
		emojis.add("IMG_0278.JPG");
		emojis.add("IMG_0284.JPG");

		emojis.add("IMG_0291.JPG");
		emojis.add("002.png");

		emojis.add("despicable-me-2-Minion-icon-5.png");
		emojis.add("IMG_0262.JPG");
		emojis.add("IMG_0268.JPG");

		emojis.add("IMG_0273.JPG");
		emojis.add("IMG_0279.JPG");
		emojis.add("IMG_0279.JPG");

		emojis.add("IMG_0285.JPG");
		emojis.add("IMG_0292.PNG");
		emojis.add("IMG_0297.PNG");

		emojis.add("IMG_0256.PNG");
		emojis.add("IMG_0263.JPG");

		emojis.add("IMG_0269.JPG");
		emojis.add("IMG_0274.JPG");
		emojis.add("IMG_0281.JPG");

		emojis.add("IMG_0286.JPG");
		emojis.add("IMG_0264.JPG");

		emojis.add("IMG_0270.JPG");
		emojis.add("IMG_0275.JPG");
		emojis.add("IMG_0282.JPG");

		emojis.add("IMG_0288.JPG");
		
		emojis.add("IMG_0258.JPG");
		emojis.add("IMG_0266.JPG");

		emojis.add("IMG_0271.JPG");
		emojis.add("IMG_0277.JPG");
		emojis.add("IMG_0283.JPG");

		emojis.add("IMG_0289.JPG");
	}

	public String getEmoji(int index) {
		return String.format("%s/%s", "/sdcard/emojis", emojis.get(index));
	}

	public int size() {
		return emojis.size();
	}
}
