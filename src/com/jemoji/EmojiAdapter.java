
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jemoji.image.FileImageDecoder;
import com.jemoji.image.ImageDecoder.ImageScaleType;
import com.jemoji.image.ImageSize;

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

		FileImageDecoder decoder = new FileImageDecoder(new File((String)list.get(arg1)
				.get("emoji")));
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
