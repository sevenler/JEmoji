
package com.jemoji;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.jemoji.models.Emoji;

public class EmojiAdapter extends PagerAdapter {

	private List<Emoji> list = new ArrayList<Emoji>();

	private Activity mContext;
	private final int mItemSize;
	
	private OnClickListener mOnClickListener;
	
	public EmojiAdapter(Context context) {
		this(context, 1);
	}

	public EmojiAdapter(Context context, int size) {
		mContext = (Activity)context;
		mItemSize = size;
	}

	public void setData(List<Emoji> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void setOnClickListener(OnClickListener listener){
		mOnClickListener = listener;
	}
	
	@Override
	public int getCount() {
		return list.size() / mItemSize;
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
		if(mItemSize == 1){
			View rootview = LayoutInflater.from(mContext).inflate(R.layout.image_item, null, true);
			arg0.addView(rootview);
			
			Emoji emoji = list.get(arg1);
			final ImageView imageView = (ImageView)rootview.findViewById(R.id.imageView);
			int background = (Integer)emoji.getBackground();
			rootview.setBackgroundColor(background);
			emoji.showEmoji(imageView);
			
			return rootview;
		}else{
			View rootview = LayoutInflater.from(mContext).inflate(R.layout.image_item_six, null, true);
			arg0.addView(rootview);
			
			for (int i = 0; i < mItemSize; i++) {
				Emoji emoji = list.get(arg1 + i);
				final ImageView imageView = (ImageView)rootview.findViewById(R.id.imageView0 + i);
				int background = (Integer)emoji.getBackground();
				imageView.setTag(emoji);
				rootview.setBackgroundColor(background);
				emoji.showEmoji(imageView, false);
				imageView.setOnClickListener(mOnClickListener);
			}
			
			return rootview;
		}
	}
}