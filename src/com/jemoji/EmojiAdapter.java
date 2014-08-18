
package com.jemoji;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jemoji.models.Emoji;

public class EmojiAdapter extends PagerAdapter {

	private List<Emoji> list = new ArrayList<Emoji>();
	private int mItemEveryPage;

	private Activity mContext;
	
	private OnClickListener mOnClickListener;

	public EmojiAdapter(Context context) {
		mContext = (Activity)context;
	}

	public void setData(List<Emoji> list) {
		setData(list, 1);
	}
	
	public void setData(List<Emoji> list, int itemEveryPage) {
		this.list.clear();
		this.list.addAll(list);
		mItemEveryPage = itemEveryPage;
		notifyDataSetChanged();
	}
	
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}

	public void setOnClickListener(OnClickListener listener){
		mOnClickListener = listener;
	}
	
	@Override
	public int getCount() {
		int more = list.size() % mItemEveryPage;
		more = (more > 0 ? 1 : 0 );
		return list.size() / mItemEveryPage + more;
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
		if(mItemEveryPage == 1){
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
			
			int total = list.size();
			for (int i = 0; i < mItemEveryPage; i++) {
				if (arg1 * mItemEveryPage  + i < total) {
					Emoji emoji = list.get(arg1 * mItemEveryPage  + i);
					final ImageView imageView = (ImageView)rootview.findViewById(R.id.imageView0 + i);
					int background = (Integer)emoji.getBackground();
					imageView.setTag(emoji);
					rootview.setBackgroundColor(background);
					emoji.showEmoji(imageView, false);
					imageView.setOnClickListener(mOnClickListener);
				}
			}
			
			return rootview;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		View view = (View)object;
		((ViewPager)container).removeView(view);
		if(mItemEveryPage == 1){
			ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
			BitmapDrawable drawable = ((BitmapDrawable)imageView.getDrawable());
			if(drawable != null){
				Bitmap bitmap = drawable.getBitmap();
				if(bitmap != null) bitmap.recycle();
			}
		}else{
			for (int i = 0; i < mItemEveryPage; i++) {
				ImageView imageView = (ImageView)view.findViewById(R.id.imageView0 + i);
				BitmapDrawable drawable = ((BitmapDrawable)imageView.getDrawable());
				if(drawable != null){
					Bitmap bitmap = drawable.getBitmap();
					System.out.println(String.format(" recycle bitmap %s ", bitmap));
					if(bitmap != null) bitmap.recycle();
				}
			}
		}
	}
}