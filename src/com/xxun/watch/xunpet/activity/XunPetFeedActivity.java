package com.xxun.watch.xunpet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoxun.sdk.ResponseData;
import com.xiaoxun.sdk.IResponseDataCallBack;
import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.bean.XunPetBean;
import com.xxun.watch.xunpet.utils.LogUtil;
import com.xxun.watch.xunpet.utils.StepsCountUtils;
import com.xxun.watch.xunpet.utils.TimeUtil;
import com.xxun.watch.xunpet.utils.ToastUtil;
import com.xxun.watch.xunpet.view.DragLayout;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huangyouyang on 2017/9/4.
 */

public class XunPetFeedActivity extends NormalActivity {

    // View
    private DragLayout layoutRoot;
    private ImageView ivXunPetFood;
    private TextView tvXunPetStep;
    private ImageView ivXunPetStepProgress;
    private GifImageView ivXunPetFeed;

    private Handler mHandler;
    private XunPetBean xunPet;
    private BroadcastReceiver mReceiver;

    private int feedTimes;  //已经喂养次数
    private int foodNumbers;
    private boolean ifFeedFlag = false;  //是否喂养
    private int reportTimes = 0;
    private static final int reportMaxTimes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunpet_feed);

        myApp.receiveFoodDaily();
        StepsCountUtils.initSensor(this, "0");

        initView();
        initData();
        initReceiver();
        initListener();
        updateImageViewShow();
    }

    @Override
    protected void onPause() {
//        myApp.reportPetExperToServer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //add by liaoyi 19/3/1 宠物退出时 上传喂食经验值
        if (ifFeedFlag) {

            myApp.reportPetExperToServer(new IResponseDataCallBack.Stub() {
                @Override
                public void onSuccess(ResponseData responseData) {
                    LogUtil.i(Const.LOG_TAG + " onDestroy reportPetExperToServer onSuccess: " + responseData.getResponseData());
//                    XunPetFeedActivity.this.finish();
                }

                @Override
                public void onError(int i, String s) {
                    LogUtil.i(Const.LOG_TAG + " onDestroy reportPetExperToServer onError: " + "i=" + i + "  s=" + s);
                    reportTimes++;
                    if (reportTimes < reportMaxTimes) {
                        myApp.reportPetExperToServer(this);
                    }
//                    else
//                        XunPetFeedActivity.this.finish();
                }
            });
        }
        //end

        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void initView() {

        layoutRoot = (DragLayout) findViewById(R.id.layout_root);
        ivXunPetFood= (ImageView) findViewById(R.id.iv_xunpet_food);
        tvXunPetStep= (TextView) findViewById(R.id.tv_xunpet_step);
        ivXunPetStepProgress= (ImageView) findViewById(R.id.iv_xunpet_step_progress);
        ivXunPetFeed= (GifImageView) findViewById(R.id.iv_xunpet_feed);
    }

    private void initData() {

        int petType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, Const.KEY_XUNPET_TYPE_IMIBABY);
        xunPet = new XunPetBean(petType);
        mHandler = new Handler();

        myApp.xunPetSteps = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_STEP_LOCAL + Const.XUNPET_UNDERLINE + TimeUtil.getTimeStampDayLocal(), 0);
        foodNumbers = myApp.getFoodNumber();
        feedTimes = myApp.getFeedTimes(TimeUtil.getTimeStampDayLocal(), TimeUtil.getTimeSlotByHour());
        LogUtil.i(Const.LOG_TAG + " foodNumbers=" + foodNumbers + "  feedTimes=" + feedTimes);
    }

    private void initReceiver() {

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (Const.ACTION_BROAST_SENSOR_STEPS.equals(action)) {
                    String sensorSteps = intent.getStringExtra("sensor_steps");
                    // 获取当前步数，需要在计算前一天数据之前才可以
                    String oldSteps = "0";
                    try {
                        //modify by liaoyi 由于计步模块存储步数方式改变，更改获取步数方式
//                        Uri uri = Uri.parse("content://com.xxun.watch.stepCountProvider/user");
//                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//                        assert cursor != null;
//                        while (cursor.moveToNext()) {
//                            oldSteps = cursor.getString(0);
//                        }

                        oldSteps = android.provider.Settings.Global.getString(getContentResolver(),"step_local");
                        String curSteps = StepsCountUtils.getPhoneStepsByFirstSteps(context, oldSteps, sensorSteps);
//                        cursor.close();
                        //end
                        myApp.xunPetSteps = Integer.parseInt(curSteps);
                        myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_STEP_LOCAL + Const.XUNPET_UNDERLINE + TimeUtil.getTimeStampDayLocal(), myApp.xunPetSteps);
                        myApp.receiveFoodBySteps();
                    } catch (Exception e) {
                        e.printStackTrace();
                        myApp.xunPetSteps = 0;
                    }

                    foodNumbers = myApp.getFoodNumber();
                    updateImageViewShow();
                    LogUtil.i(Const.LOG_TAG + " xunPetSteps= " + myApp.xunPetSteps);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Const.ACTION_BROAST_SENSOR_STEPS);
        registerReceiver(mReceiver, intentFilter);
    }

    private void initListener() {

        ivXunPetFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedTimes >= Const.XUNPET_FEEDTIME_MAX) {
                    ToastUtil.show(XunPetFeedActivity.this, getString(R.string.can_not_eat_again));
                    return;
                }
                if (myApp.getFeedMaxTimes() <= 0) {
                    ToastUtil.show(XunPetFeedActivity.this, getString(R.string.have_no_food));
                    return;
                }

                ivXunPetFeed.setVisibility(View.VISIBLE);
                ivXunPetFood.setClickable(false);
                XunPetBean.showGif(XunPetFeedActivity.this, xunPet.petFeedEat, ivXunPetFeed);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ifFeedFlag = true;
                        ivXunPetFeed.setBackgroundResource(xunPet.petFeedEat);
                        ivXunPetFeed.setVisibility(View.GONE);
                        ivXunPetFood.setClickable(true);
                        // 刷新食物View
                        ivXunPetFood.setBackgroundResource(getFoodResourceId(foodNumbers));
                    }
                }, 5 * 1000);

                // 重置食物份数、可喂食次数
                feedTimes++;
                myApp.reSetFeedTimes(TimeUtil.getTimeStampDayLocal(), TimeUtil.getTimeSlotByHour());
                myApp.addFoodNumber(-1);
                myApp.addPetExper(10);

                // 判断能否通过运动领取食物
                myApp.receiveFoodBySteps();
                foodNumbers = myApp.getFoodNumber();
                updateImageViewShow();
            }
        });

//delete by liaoyi 19/3/1
//        layoutRoot.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
//            @Override
//            public void onDragLeft() {
//            }
//
//            @Override
//            public void onDragRight() {
//                if (ifFeedFlag) {
//
//                    myApp.reportPetExperToServer(new IResponseDataCallBack.Stub() {
//                        @Override
//                        public void onSuccess(ResponseData responseData) {
//                            LogUtil.i(Const.LOG_TAG + " reportPetExperToServer onSuccess: " + responseData.getResponseData());
//                            XunPetFeedActivity.this.finish();
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//                            LogUtil.i(Const.LOG_TAG + " reportPetExperToServer onError: " + "i=" + i + "  s=" + s);
//                            reportTimes++;
//                            if (reportTimes < reportMaxTimes)
//                                myApp.reportPetExperToServer(this);
//                            else
//                                XunPetFeedActivity.this.finish();
//                        }
//                    });
//                }
//                XunPetFeedActivity.this.finish();
//            }
//        });
        //end
    }

    private void updateImageViewShow() {

        // 食物
        ivXunPetFood.setBackgroundResource(getFoodResourceId(foodNumbers));

        // 计步
        int steps = myApp.getSteps();
        if (steps < 0) steps = 0;
        tvXunPetStep.setText(steps + getString(R.string.slash) + Const.XUNPET_STEPS_CAN_RECEIVE_FOOD);
        Bitmap progressBg = BitmapFactory.decodeResource(getResources(), R.drawable.pet_step_progress);
        int bgWidth = progressBg.getWidth();
        int newWidth = bgWidth * steps / Const.XUNPET_STEPS_CAN_RECEIVE_FOOD;
        int bgHeight = progressBg.getHeight();
//        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
//        Canvas cv = new Canvas(newbmp);
        if (newWidth <= 0) newWidth = 1;
        if (newWidth > bgWidth) newWidth = bgWidth;
        Bitmap newProgressBg = Bitmap.createBitmap(progressBg, 0, 0, newWidth, bgHeight);
//        cv.drawBitmap(newProgressBg, 0, 0, null);
//        cv.save(Canvas.ALL_SAVE_FLAG);//保存
//        cv.restore();

        ivXunPetStepProgress.setBackground(new BitmapDrawable(getResources(),newProgressBg));
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivXunPetStepProgress.getLayoutParams();
//        layoutParams.width = newWidth;
//        ivXunPetStepProgress.setLayoutParams(layoutParams);
    }

    private int getFoodResourceId(int foodNumbers){
        int resourceId=xunPet.petFood_0;
        switch (foodNumbers){
            case 0:
                resourceId=xunPet.petFood_0;
                break;
            case 1:
                resourceId=xunPet.petFood_1;
                break;
            case 2:
                resourceId=xunPet.petFood_2;
                break;
            case 3:
                resourceId=xunPet.petFood_3;
                break;
            case 4:
                resourceId=xunPet.petFood_4;
                break;
            case 5:
                resourceId=xunPet.petFood_5;
                break;
        }
        return resourceId;
    }
}
