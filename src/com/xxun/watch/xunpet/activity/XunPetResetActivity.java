package com.xxun.watch.xunpet.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.content.Intent;
import android.content.ComponentName;
import android.view.View;
import android.widget.ImageView;
import android.provider.Settings;

import com.xiaoxun.sdk.ResponseData;
import com.xiaoxun.sdk.IResponseDataCallBack;
import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.bean.XunPetBean;
import com.xxun.watch.xunpet.utils.LogUtil;
import com.xxun.watch.xunpet.utils.ToastUtil;
import com.xxun.watch.xunpet.view.DragLayout;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huangyouyang on 2017/9/4.
 */

public class XunPetResetActivity extends NormalActivity {

    private DragLayout layoutRoot;
    private GifImageView ivXunPetFeed;
    private ImageView btnConfirm;
    private ImageView btnCancel;

    private XunPetBean xunPet;
    private int reportTimes = 0;
    private static final int reportMaxTimes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunpet_reset);

        initView();
        initData();
        initListener();
        updateViewShow();
        ifHaveAdopt();
    }

    private void initView() {

        layoutRoot = (DragLayout) findViewById(R.id.layout_root);
        ivXunPetFeed = (GifImageView) findViewById(R.id.iv_xunpet_feed);
        btnConfirm = (ImageView) findViewById(R.id.btn_reset_confirm);
        btnCancel = (ImageView) findViewById(R.id.btn_reset_cancel);
    }

    private void initData() {

        int petType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, Const.KEY_XUNPET_TYPE_IMIBABY);
        xunPet = new XunPetBean(petType);
    }

    private void updateViewShow() {

        XunPetBean.showGif(XunPetResetActivity.this, xunPet.petFeedEatUp, ivXunPetFeed);
    }

    private void ifHaveAdopt() {

        boolean isAdopt = myApp.hasValue(Const.SHARE_PREF_KEY_XUNPET_TYPE);
        if (!isAdopt) {
            ToastUtil.show(XunPetResetActivity.this, XunPetResetActivity.this.getString(R.string.pet_not_adopt));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    XunPetResetActivity.this.finish();
                }
            }, 2 * 1000);
        }
    }

    private void initListener() {

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetXunPet();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackSettingActivity(false);
            }
        });

        layoutRoot.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void onDragLeft() {
            }

            @Override
            public void onDragRight() {
                goBackSettingActivity(false);
            }
        });
    }

    private void resetXunPet() {

        myApp.reportZeroToServer(new IResponseDataCallBack.Stub() {
            @Override
            public void onSuccess(ResponseData responseData) {
                LogUtil.i(Const.LOG_TAG + " reportPetExperToServer onSuccess: " + responseData.getResponseData());
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.i(Const.LOG_TAG + " reportPetExperToServer onError: " + "i=" + i + "  s=" + s);
                reportTimes++;
                if (reportTimes < reportMaxTimes)
                    myApp.reportZeroToServer(this);
            }
        });

        deleteSpValue();
        goBackSettingActivity(true);
    }

    private void deleteSpValue(){

        // delete pettype and experience,but save food and steps
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_TYPE);
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE);
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK);
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE);
        // myApp.deletAllValue();
//        SystemProperties.set("persist.sys.ispetadopted", "false");
        Settings.Global.putString(getContentResolver(),"ispetadopted","false");
    }

    private void goBackSettingActivity(boolean isReset) {

        if (isReset) {
            Intent settingIntent = new Intent();
            ComponentName settingCn = new ComponentName("com.xxun.watch.xunsettings", "com.xxun.watch.xunsettings.activity.MainActivity");
            settingIntent.setComponent(settingCn);
            settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            settingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(settingIntent);
        }
        XunPetResetActivity.this.finish();
    }
}
