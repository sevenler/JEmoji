
package com.jemoji;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.image.FileImageDecoder;
import com.jemoji.image.ImageDecoder.ImageScaleType;
import com.jemoji.image.ImageSize;
import com.jemoji.models.Emoji;
import com.jemoji.utils.Utility;

public class EmojiAdapter extends PagerAdapter {

	private List<Emoji> list = new ArrayList<Emoji>();

	private Activity mContext;

	public EmojiAdapter(Context context) {
		mContext = (Activity)context;
	}

	public void setData(List<Emoji> list) {
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
		final ImageView imageView = (ImageView)rootview.findViewById(R.id.imageView);

		Emoji emoji = list.get(arg1);
		int background = (Integer)emoji.getBackground();
		rootview.setBackgroundColor(background);
		
		emoji.showEmoji(imageView);
		
		arg0.addView(rootview);

		return rootview;
	}
}