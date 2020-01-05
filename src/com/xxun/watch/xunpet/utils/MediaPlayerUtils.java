package com.xxun.watch.xunpet.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.xxun.watch.xunpet.XunPetApplication;

/**
 * Created by huangyouyang on 2017/12/11.
 */

public class MediaPlayerUtils {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private static MediaPlayerUtils instance = null;

    public synchronized static MediaPlayerUtils getInstance() {
        if (instance == null)
            instance = new MediaPlayerUtils();
        return instance;
    }

    private MediaPlayerUtils() {

    }

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public void requestAudioFocus(XunPetApplication mApp) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mApp.getSystemService(Context.AUDIO_SERVICE);
        }
        mAudioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    public MediaPlayer starMediaPlayer(Uri soundUri, XunPetApplication mApp) {

        if (soundUri == null || mApp == null) {
            return null;
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mApp.getSystemService(Context.AUDIO_SERVICE);
        }
        mMediaPlayer.reset();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        try {
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer = MediaPlayer.create(mApp, soundUri);
            mMediaPlayer.start();
        } catch (Exception e) {
            LogUtil.e("starMediaPlayer : " + e.toString());
        }
        return mMediaPlayer;
    }

    public void stopMediaPlayer(XunPetApplication mApp) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }
        if (mAudioManager != null) {
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
            mAudioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
}
