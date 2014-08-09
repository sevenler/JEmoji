
package com.jemoji.utils;

import java.io.File;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;

public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000;

	MediaRecorder recorder;
	final String path;

	public AudioRecorder() {
		this("test");
	}
	
	public AudioRecorder(String path) {
		this.path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "emojis" + File.separator + path;
	}

	public File start() throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted,It is  " + state + ".");
		}
		
		File file = new File(path + File.separator + String.format("%s.amr", System.currentTimeMillis()));
		File directory = file.getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created");
		}
		
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(file.getAbsolutePath());
		recorder.prepare();
		recorder.start();
		
		return file;
	}

	public void stop() throws IOException {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
		}
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else return 0;
	}
}
