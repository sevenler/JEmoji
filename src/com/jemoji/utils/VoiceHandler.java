package com.jemoji.utils;

import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jemoji.R;

public class VoiceHandler implements OnTouchListener{
	private AudioRecorder mr = new AudioRecorder();
	private MediaPlayer mediaPlayer;
	
	private static int MIX_TIME = 1;

	private static int RECORD_NO = 0;
	private static int RECORD_ING = 1;
	private static int RECODE_ED = 2;
	private static int RECODE_STATE = 0;

	private static float recodeTime = 0.0f;
	private static double voiceValue = 0.0;
	private static boolean playState = false;
	
	private String recordFile;
	
	private ImageView dialog_img;
	private Dialog dialog;
	
	private OnHandListener mOnHandListener;
	
	public void setOnHandListener(OnHandListener onHandListener){
		mOnHandListener = onHandListener;
	}
	
	public boolean isVoicePlaying(){
		return playState;
	}
	
	void showVoiceDialog(Context context) {
		dialog = new Dialog(context, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		dialog_img = (ImageView)dialog.findViewById(R.id.dialog_img);
		dialog.show();
	}
	
	public interface OnHandListener {
		public void onPlay(boolean isFinish);

		public void onRecored(boolean isFinish, int time, String file);
	}
	
	void showWarnToast(Context context) {
		Toast toast = new Toast(context);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(20, 20, 20, 20);

		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.voice_to_short);

		TextView mTv = new TextView(context);
		mTv.setText("说话时间太短");
		mTv.setTextSize(14);
		mTv.setTextColor(Color.WHITE);

		linearLayout.addView(imageView);
		linearLayout.addView(mTv);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setBackgroundResource(R.drawable.record_bg);

		toast.setView(linearLayout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	void mythread() {
		Thread recordThread = new Thread(ImgThread);
		recordThread.start();
	}
	
	void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
	}
	
	private Runnable ImgThread = new Runnable() {
		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				try {
					Thread.sleep(200);
					recodeTime += 0.2;
					if (RECODE_STATE == RECORD_ING) {
						voiceValue = mr.getAmplitude();
						imgHandle.sendEmptyMessage(1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						setDialogImage();
						break;
				}
			}
		};
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					playState = false;
					if (mOnHandListener != null) mOnHandListener.onPlay(true);
				}
				
				if (RECODE_STATE != RECORD_ING) {
					RECODE_STATE = RECORD_ING;
					showVoiceDialog(v.getContext());
					try {
						recordFile = mr.start().getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mythread();
				}
				break;
				
			case MotionEvent.ACTION_UP:
				if (RECODE_STATE == RECORD_ING) {
					RECODE_STATE = RECODE_ED;
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					try {
						mr.stop();
						voiceValue = 0.0;
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (recodeTime < MIX_TIME) {
						showWarnToast(v.getContext());
//						record.setText("录音时间太短");
						RECODE_STATE = RECORD_NO;
					} else {
						if (mOnHandListener != null) mOnHandListener.onRecored(true, (int)recodeTime, recordFile);
					}
				}
				break;
		}
		return false;
	}
	
	public void playOrStop(String voice){
		if (!playState) {
			try {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(voice);
				mediaPlayer.prepare();
				mediaPlayer.start();
				if (mOnHandListener != null) mOnHandListener.onPlay(false);
				playState = true;
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						if (playState) {
							if (mOnHandListener != null) mOnHandListener.onPlay(true);
							playState = false;
						}
					}
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				playState = false;
			} else {
				playState = false;
			}
			if (mOnHandListener != null) mOnHandListener.onPlay(true);
		}
	}
}