
package com.jemoji;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
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

		Map<?, ?> map = list.get(arg1); 
		int background = (Integer)map.get("background");
		rootview.setBackgroundColor(background);
		
		String filename = (String)map.get("emoji");
		FileImageDecoder decoder = new FileImageDecoder(new File(filename));

		System.out.println(String.format(" %s ", filename.endsWith(".gif")));
		if(filename.endsWith(".gif")){
			try {
				GifAnimationDrawable little = new GifAnimationDrawable(new File(filename), false);
				little.setOneShot(false);
				imageView.setImageDrawable(little);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				Bitmap bitmap = decoder.decode(new ImageSize(510, 510), ImageScaleType.POWER_OF_2);
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		arg0.addView(rootview);

		return rootview;
	}
}