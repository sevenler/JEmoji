
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
		
		String filename = (String)emoji.getImage();
		System.out.println(String.format(" filename:%s ", filename));
		
		if(!Utility.Strings.isEmptyString(filename) && new File(filename).exists()){
			showFile(imageView, filename);
			System.out.println(String.format(" =======exists========= "));
		}else{
			filename = (String)emoji.getImageUrl();
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			System.out.println(String.format(" =======no no no  %s========= ", type));
			String image = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator + "emojis_download" + File.separator
					+ System.currentTimeMillis() + "." + type;
			GKHttpInterface.genFile(emoji.getImageUrl(), type, image, new GKJsonResponseHandler() {
				@Override
				public void onResponse(int code, Object file, Throwable error) {
					System.out.println(String.format(" file:%s ", file));
					showFile(imageView, (String)file);
				}
			});
		}
		
		
		arg0.addView(rootview);

		return rootview;
	}
	
	private void showFile(ImageView imageView, String filename){
		
		if(filename.endsWith(".gif")){
			System.out.println(String.format("GIF Image %s ", filename));
			try {
				GifAnimationDrawable little = new GifAnimationDrawable(new File(filename), false);
				little.setOneShot(false);
				imageView.setImageDrawable(little);
				little.setVisible(true, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println(String.format("Image %s ", filename));
			try {
				FileImageDecoder decoder = new FileImageDecoder(new File(filename));
				Bitmap bitmap = decoder.decode(new ImageSize(510, 510), ImageScaleType.POWER_OF_2);
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}