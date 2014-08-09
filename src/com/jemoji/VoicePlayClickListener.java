/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jemoji;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.http.URLs;

public class VoicePlayClickListener implements View.OnClickListener {
	Activity activity;
	MediaPlayer mediaPlayer = null;
	Emoji emoji;
	
	public VoicePlayClickListener(Activity activity, Emoji emoji){
		this.activity = activity;
		this.emoji = emoji;
	}
	
	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}
		AudioManager audioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		audioManager.setMode(AudioManager.MODE_NORMAL);
		audioManager.setSpeakerphoneOn(true);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer.release();
					mediaPlayer = null;
				}
			});
			mediaPlayer.start();
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		
		switch(emoji.getVoiceStatus()){
			case Emoji.STATUS_LOCAL:
				playVoice(emoji.getVoice());
				break;
			case Emoji.STATUS_REMOTE:
				Toast.makeText(activity, "正在下载", Toast.LENGTH_SHORT).show();
				emoji.download(new GKJsonResponseHandler() {
					@Override
					public void onResponse(int code, Object file, Throwable error) {
						emoji.setVoice((String)file);
						playVoice(emoji.getVoice());
					}
				});
				break;
		}
	}
}
