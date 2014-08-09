
package com.jemoji;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.dd.CircularProgressButton;

public class WaveView {
	private static final int ANIMATIONEACHOFFSET = 600; // 每个动画的播放时间间隔
	private AnimationSet aniSet, aniSet2, aniSet3;
	private final ImageView wave1, wave2, wave3;
	private final CircularProgressButton btn;
	private boolean isStarting = false;
	ValueAnimator widthAnimation;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x222) {
				wave2.startAnimation(aniSet2);
			} else if (msg.what == 0x333) {
				wave3.startAnimation(aniSet3);
			}
			super.handleMessage(msg);
		}
	};
	public OnWaveListener mOnWaveListener;

	public static interface OnWaveListener {
		public int onStart();

		public void onStop();

		public void onWaiting();
	}

	public WaveView(OnWaveListener listener, CircularProgressButton btn, ImageView wave1,
			ImageView wave2, ImageView wave3) {
		aniSet = getNewAnimationSet();
		aniSet2 = getNewAnimationSet();
		aniSet3 = getNewAnimationSet();
		this.btn = btn;
		this.wave1 = wave1;
		this.wave2 = wave2;
		this.wave3 = wave3;
		mOnWaveListener = listener;

		this.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int status = isStarting ? STATUS_STOP : STATUS_START;
				setStatus(status);
			}
		});
	}

	private void simulateSuccessProgress(final CircularProgressButton button, int length) {
		widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(length);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer)animation.getAnimatedValue();
				button.setProgress(value);

				if (value >= 100) {
					setStatus(STATUS_STOP);
				}
			}
		});
		widthAnimation.start();
	}

	private void cancelProgress() {
		if (widthAnimation != null) widthAnimation.cancel();
	}

	public static final int STATUS_STOP = 1;
	public static final int STATUS_START = 2;
	public static final int STATUS_WAITING = 3;

	public void setStatus(int status) {
		switch (status) {
			case STATUS_START:
				showWaveAnimation();
				int length = mOnWaveListener.onStart();
				simulateSuccessProgress(btn, length);
				isStarting = true;
				break;
			case STATUS_STOP:
				cancalWaveAnimation();
				mOnWaveListener.onStop();
				cancelProgress();
				isStarting = false;
				break;
			case STATUS_WAITING:
				cancalWaveAnimation();
				mOnWaveListener.onWaiting();
				cancelProgress();
				break;
		}
	}

	private AnimationSet getNewAnimationSet() {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, 3.5f, 1f, 3.5f, ScaleAnimation.RELATIVE_TO_SELF,
				0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(ANIMATIONEACHOFFSET * 3);
		sa.setRepeatCount(-1);// 设置循环
		AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
		aniAlp.setRepeatCount(-1);// 设置循环
		as.setDuration(ANIMATIONEACHOFFSET * 3);
		as.addAnimation(sa);
		as.addAnimation(aniAlp);
		return as;
	}

	private void showWaveAnimation() {
		wave1.startAnimation(aniSet);
		handler.sendEmptyMessageDelayed(0x222, ANIMATIONEACHOFFSET);
		handler.sendEmptyMessageDelayed(0x333, ANIMATIONEACHOFFSET * 2);
	}

	private void cancalWaveAnimation() {
		wave1.clearAnimation();
		wave2.clearAnimation();
		wave3.clearAnimation();
	}
}
