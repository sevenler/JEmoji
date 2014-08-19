package com.jemoji.models;

import java.io.InputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class NotificationViocePlayer {
	private byte[] buffer = null;
	AudioTrack at = null;
	int pcmlen = 0;
	final Context mContent;

	public NotificationViocePlayer(Context content) {
		mContent = content;
	}

	public void release() {
		if (at != null)
			at.release();
	}

	public void play() {
		try {
			InputStream input = mContent.getAssets().open("ding.wav");
			buffer = new byte[1024 * 1024 * 2];// 2M
			int len = input.read(buffer);
			pcmlen = 0;
			pcmlen += buffer[0x2b];
			pcmlen = pcmlen * 256 + buffer[0x2a];
			pcmlen = pcmlen * 256 + buffer[0x29];
			pcmlen = pcmlen * 256 + buffer[0x28];

			int channel = buffer[0x17];
			channel = channel * 256 + buffer[0x16];

			int bits = buffer[0x23];
			bits = bits * 256 + buffer[0x22];
			at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, channel,
					AudioFormat.ENCODING_PCM_16BIT, pcmlen,
					AudioTrack.MODE_STATIC);
			at.setStereoVolume(0.3f, 0.3f);
			at.write(buffer, 0x2C, pcmlen);
			at.play();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
