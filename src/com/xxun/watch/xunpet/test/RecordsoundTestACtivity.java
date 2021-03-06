package com.xxun.watch.xunpet.test;

import java.io.File;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.jni.SoundStretch;
import com.xxun.watch.xunpet.utils.AudioRecorderUtils;

public class RecordsoundTestACtivity extends Activity implements OnSeekBarChangeListener {
	private AudioRecorderUtils audioRecorderUtils;
	private boolean recording;
	private String dataPath;

	private SeekBar tempoBar;
	private SeekBar pitchBar;
	private SeekBar rateBar;

	private float tempo = 0;
	private float pitch = 0;
	private float rate = 0;

	private final static int MIDDLE = 10;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soundtest);

		System.loadLibrary("soundstretch");

		tempoBar = getSeekBar(R.id.tempoBar);
		pitchBar = getSeekBar(R.id.pitchBar);
		rateBar = getSeekBar(R.id.rateBar);

		recording = false;

		String cardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/";
		dataPath = cardRoot + this.getClass().getPackage().getName() + "/";

		File folder = new File(dataPath);
		boolean success = false;
		if (!folder.exists()) {
			success = folder.mkdir();
		}

		if (!success) {
		} else {
		}
	}

	private SeekBar getSeekBar(int id) {
		SeekBar seekBar = (SeekBar) findViewById(id);
		seekBar.setMax(MIDDLE * 2);
		seekBar.setProgress(MIDDLE);
		seekBar.setOnSeekBarChangeListener(this);
		return seekBar;
	}

	public void startRecording(View view) {

		if (!recording) {
			audioRecorderUtils = AudioRecorderUtils.getInstanse(false);
			audioRecorderUtils.setOutputFile(dataPath + "output.wav");
			audioRecorderUtils.prepare();
			audioRecorderUtils.start();
			recording = true;
		}
	}

	public void stopRecording(View view) {
		if (recording) {
			audioRecorderUtils.stop();
			audioRecorderUtils.release();
			recording = false;
		}
	}

	public void playRecording(View view) {
		System.out.println("tempo=" + tempo + "  pitch=" + pitch + "  rate="
				+ rate);
		new SoundStretch().process(dataPath + "output.wav", dataPath
				+ "output1.wav", tempo, pitch, rate);
		File sound = new File(dataPath + "output1.wav");
		Uri soundUri = Uri.fromFile(sound);
		MediaPlayer mediaPlayer = MediaPlayer.create(this, soundUri);
		mediaPlayer.start();
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar.equals(rateBar)) {
			rate = progress - MIDDLE;
		} else if (seekBar.equals(tempoBar)) {
			tempo = progress - MIDDLE;
		} else if (seekBar.equals(pitchBar)) {
			pitch = progress - MIDDLE;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
