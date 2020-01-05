package com.xxun.watch.xunpet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.provider.Settings;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Process;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.XiaoXunUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.XunPetApplication;
import com.xxun.watch.xunpet.bean.XunPetBean;
import com.xxun.watch.xunpet.jni.SoundStretch;
import com.xxun.watch.xunpet.utils.AudioRecorderUtils;
import com.xxun.watch.xunpet.utils.MediaPlayerUtils;
import com.xxun.watch.xunpet.utils.DensityUtil;
import com.xxun.watch.xunpet.utils.LogUtil;
import com.xxun.watch.xunpet.view.DragLayout;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class XunPetMainActivity extends NormalActivity {

    // view
    private DragLayout layoutRoot;
    private GifImageView ivMainXunPet;
    private ImageView btnSoundStretch;
    private ImageView btnXunPetFeed;

    // sound stretch
    private AudioRecorderUtils audioRecorderUtils;
    private MediaPlayerUtils mediaPlayerUtils;
    private boolean recording;
    private File sourceDataFile;
    private File stretchDataFile;

    // handler
    private UIHandler mHandler;
    private int animType;
    private XunPetBean xunPet;

    // load library
    static {
        System.loadLibrary("soundstretch");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunpet_main);

        ifHaveAdopt();
        initView();
        initData();
        initListener();
//        updateViewShow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViewShow();
    }

    private void ifHaveAdopt() {

//        int petType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, -1);
//        if (petType == -1) {
//            startActivity(new Intent(XunPetMainActivity.this, XunPetAdoptActivity.class));
//            XunPetMainActivity.this.finish();
//        }
        myApp.statsTimes();
        boolean isPetAdopted = "true".equals(Settings.Global.getString(XunPetMainActivity.this.getContentResolver(), "ispetadopted"));
        if (!isPetAdopted) {
            startActivity(new Intent(XunPetMainActivity.this, XunPetAdoptActivity.class));
            XunPetMainActivity.this.finish();
        }
    }

    private void initView() {

        layoutRoot = (DragLayout) findViewById(R.id.layout_root);
        ivMainXunPet = (GifImageView) findViewById(R.id.iv_main_xunpet);
        btnXunPetFeed = (ImageView) findViewById(R.id.btn_xunpet_feed);
        btnSoundStretch = (ImageView) findViewById(R.id.btn_sound_stretch);
    }

    private void initData() {

        int petType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, Const.KEY_XUNPET_TYPE_IMIBABY);
        xunPet = new XunPetBean(petType);
        recording = false;
        sourceDataFile = new File(XunPetApplication.BASEDIR, Const.SOUND_SOURCE_WAV);
        stretchDataFile = new File(XunPetApplication.BASEDIR, Const.SOUND_STRETCH_WAV);
        mHandler = new UIHandler(this, getMainLooper());
    }

    private void updateViewShow() {

        btnXunPetFeed.setImageResource(xunPet.petFeedIcon);
        ivMainXunPet.setBackgroundResource(xunPet.petDefault);
        animType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE, Const.XUNPET_ANIM_TYPE_TOP);
        showXunpetAnim(animType);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        ivMainXunPet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (XiaoXunUtil.XIAOXUN_CONFIG_PALACE_SUPPORT) {
                        showXunpetAnim((int) (Math.random() * 4));
                    } else {
                        float screenPx = DensityUtil.dip2px(XunPetMainActivity.this, Const.SCREEN_DP);
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
//                    LogUtil.i(Const.LOG_TAG + "  screenPx=" + screenPx + "  x=" + x + "  y=" + y);
                        if (x > y && x + y < screenPx)
                            showXunpetAnim(Const.XUNPET_ANIM_TYPE_TOP);
                        else if (x < y && x + y < screenPx)
                            showXunpetAnim(Const.XUNPET_ANIM_TYPE_LEFT);
                        else if (x < y && x + y > screenPx)
                            showXunpetAnim(Const.XUNPET_ANIM_TYPE_BOTTOM);
                        else if (x > y && x + y > screenPx)
                            showXunpetAnim(Const.XUNPET_ANIM_TYPE_RIGHT);
                    }
                }
                return true;
            }
        });

        btnXunPetFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(XunPetMainActivity.this, XunPetFeedActivity.class));
            }
        });

        btnSoundStretch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mHandler.sendEmptyMessage(UIHandler.SOUNDSTRETCH_START);
                    startRecording();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mHandler.sendEmptyMessage(UIHandler.SOUNDSTRETCH_CAMCEL);
                    stopRecording();
                    playRecording();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mHandler.sendEmptyMessage(UIHandler.SOUNDSTRETCH_END);
                    stopRecording();
                    playRecording();
                }
                return true;
            }
        });

        layoutRoot.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void onDragLeft() {
                XunPetMainActivity.this.startActivity(new Intent(XunPetMainActivity.this, XunPetRankActivity.class));
            }

            @Override
            public void onDragRight() {
                XunPetMainActivity.this.finish();
                Process.killProcess(Process.myPid());
            }
        });
    }

    @Override
    protected void onPause() {
        if (mediaPlayerUtils != null)
            mediaPlayerUtils.stopMediaPlayer(myApp);
        ivMainXunPet.setBackgroundResource(xunPet.petDefault);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
//        Glide.with(this).onDestroy();
        super.onDestroy();
    }

    // pet anim type
    private void showXunpetAnim(int type) {
        switch (type) {
            case 0:
                showGif(xunPet.petAnimTop);
//                XunPetBean.showGif(XunPetMainActivity.this, xunPet.petAnimTop, ivMainXunPet);
                myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE, Const.XUNPET_ANIM_TYPE_TOP);
                break;
            case 1:
                showGif(xunPet.petAnimBottom);
//                XunPetBean.showGif(XunPetMainActivity.this, xunPet.petAnimBottom, ivMainXunPet);
                myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE, Const.XUNPET_ANIM_TYPE_BOTTOM);
                break;
            case 2:
                showGif(xunPet.petAnimLeft);
//                XunPetBean.showGif(XunPetMainActivity.this, xunPet.petAnimLeft, ivMainXunPet);
                myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE, Const.XUNPET_ANIM_TYPE_LEFT);
                break;
            case 3:
                showGif(xunPet.petAnimRight);
//                XunPetBean.showGif(XunPetMainActivity.this, xunPet.petAnimRight, ivMainXunPet);
                myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE, Const.XUNPET_ANIM_TYPE_RIGHT);
                break;
            default:
                showGif(xunPet.petAnimTop);
//                XunPetBean.showGif(XunPetMainActivity.this, xunPet.petAnimTop, ivMainXunPet);
                break;
        }
    }

    GifDrawable gifResource;
    GifDrawable gifDefaultResource;

    private void showGif(int resourceId) {
        try {
            if (gifResource != null && !gifResource.isRecycled()) {
                if (XiaoXunUtil.XIAOXUN_CONFIG_PALACE_SUPPORT)
                    gifResource.removeAnimationListener(listener);
                gifResource.recycle();
                gifResource = null;
            }
            gifResource = new GifDrawable(getResources(), resourceId);
            if (XiaoXunUtil.XIAOXUN_CONFIG_PALACE_SUPPORT)
                gifResource.addAnimationListener(listener);
            if (!gifResource.isRecycled())
                ivMainXunPet.setImageDrawable(gifResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AnimationListener listener = new AnimationListener() {
        @Override
        public void onAnimationCompleted() {
            if (gifDefaultResource == null) {
                try {
                    gifDefaultResource = new GifDrawable(getResources(), xunPet.petDefault);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ivMainXunPet.setImageDrawable(gifDefaultResource);
        }
    };

    private class UIHandler extends Handler {

        final WeakReference<XunPetMainActivity> mActivity;

        private UIHandler(XunPetMainActivity mActivity, Looper looper) {
            super(looper);
            this.mActivity = new WeakReference<>(mActivity);
        }

        private final static int SOUNDSTRETCH_START = 0;
        private final static int SOUNDSTRETCH_CAMCEL = 1;
        private final static int SOUNDSTRETCH_END = 2;
        private final static int SOUNDSTRETCH_PLAY_END = 3;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            XunPetMainActivity context = mActivity.get();
            switch (msg.what) {
                case SOUNDSTRETCH_START:
//                    btnSoundStretch.setText("正在录音");
                    btnSoundStretch.setImageResource(R.drawable.sound_stretch_down);
                    XunPetBean.showGif(context, xunPet.petFeedListen, ivMainXunPet);
                    break;
                case SOUNDSTRETCH_CAMCEL:
                case SOUNDSTRETCH_END:
//                    btnSoundStretch.setText(R.string.sound_stretch);
                    btnSoundStretch.setImageResource(R.drawable.sound_stretch);
                    XunPetBean.showGif(context, xunPet.petFeedSpeak, ivMainXunPet);
                    break;
                case SOUNDSTRETCH_PLAY_END:
                    showXunpetAnim(animType);
                    break;
            }
        }
    }

    /*--------------------------------soundstretch start----------------------------------------*/
    public void startRecording() {
        //add by liaoyi 19/1/10
        openWakeLock();
        //end
        MediaPlayerUtils.getInstance().stopMediaPlayer(myApp);
        MediaPlayerUtils.getInstance().requestAudioFocus(myApp);
        if (!recording) {
            audioRecorderUtils = AudioRecorderUtils.getInstanse(false);
            audioRecorderUtils.setOutputFile(sourceDataFile.getPath());
            audioRecorderUtils.prepare();
            audioRecorderUtils.start();
            recording = true;
        }
    }

    public void stopRecording() {
        //add by liaoyi 19/1/10
        closeWakeLock();
        //end
        if (recording) {
            audioRecorderUtils.stop();
            audioRecorderUtils.release();
            recording = false;
        }
    }

    /**
     * 屏幕保持常亮
     */
    private void openWakeLock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 关闭屏幕常亮
     */
    private void closeWakeLock() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void playRecording() {

        LogUtil.i(Const.LOG_TAG + " start_time");
        new SoundStretch().process(sourceDataFile.getPath(), stretchDataFile.getPath(),
                Const.SOUNDSTRETCH_TEMPO, Const.SOUNDSTRETCH_PITCH, Const.SOUNDSTRETCH_RATE);
        LogUtil.i(Const.LOG_TAG + " end_time");
        Uri soundUri = Uri.fromFile(stretchDataFile);
        mediaPlayerUtils = MediaPlayerUtils.getInstance();
        MediaPlayer mediaPlayer = mediaPlayerUtils.starMediaPlayer(soundUri, myApp);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayerUtils.stopMediaPlayer(myApp);
                mHandler.sendEmptyMessage(UIHandler.SOUNDSTRETCH_PLAY_END);
            }
        });
    }
    /*--------------------------------soundstretch end----------------------------------------*/

}
