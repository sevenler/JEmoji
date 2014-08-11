
package com.jemoji;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jemoji.image.FileImageDecoder;
import com.jemoji.image.ImageDecoder.ImageScaleType;
import com.jemoji.image.ImageSize;
import com.jemoji.models.Emoji;

public class EmojiAdapter extends PagerAdapter {

	private List<Map<?, ?>> list = new ArrayList<Map<?, ?>>();

	private Activity mContext;

	public EmojiAdapter(Context context) {
		mContext = (Activity)context;
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
		((ViewPager)arg0).removeView((View)arg2);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		View rootview = LayoutInflater.from(mContext).inflate(R.layout.image_item, null, true);
		ImageView imageView = (ImageView)rootview.findViewById(R.id.imageView);

		Map<?, ?> map = list.get(arg1); 
		int background = (Integer)map.get("background");
		rootview.setBackgroundColor(background);
		
		FileImageDecoder decoder = new FileImageDecoder(new File((String)map.get("emoji")));
		try {
			Bitmap bitmap = decoder.decode(new ImageSize(510, 510), ImageScaleType.POWER_OF_2);
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
		if (instance == null) instance = new EmojiSelector();
		return instance;
	}

	public List<Emoji> emojis = new LinkedList<Emoji>();

	public EmojiSelector() {
		emojis.add(new Emoji("001.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0259.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0272.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0278.JPG", Color.parseColor("#FEFFBB")));
		emojis.add(new Emoji("IMG_0284.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0267.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0291.JPG", Color.parseColor("#AADFFF")));
		emojis.add(new Emoji("IMG_0273.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("despicable-me-2-Minion-icon-5.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0262.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0268.JPG", Color.parseColor("#ffffff")));

		/*
		 * emojis.add("IMG_0273.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0279.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0279.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0285.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0292.PNG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0297.PNG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0256.PNG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0263.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 */

		/*
		 * emojis.add("IMG_0269.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0274.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0281.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0286.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0264.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0270.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0275.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0282.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0288.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0258.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0266.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0271.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0277.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor(""))); emojis.add("IMG_0283.JPG"); emojis.add(new
		 * Emoji("IMG_0267.JPG", Color.parseColor("")));
		 * emojis.add("IMG_0289.JPG"); emojis.add(new Emoji("IMG_0267.JPG",
		 * Color.parseColor("")));
		 */
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
