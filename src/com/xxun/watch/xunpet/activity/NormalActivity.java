/**
 * Creation Date:2015-2-5
 * <p>
 * Copyright
 */
package com.xxun.watch.xunpet.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;

import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.XunPetApplication;
import com.xxun.watch.xunpet.bean.XunPetBean;

/**
 * Created by huangyouyang on 2017/6/9.
 */

public class NormalActivity extends Activity {

    XunPetApplication myApp;
    BroadcastReceiver baseRevReceiver;

    public long mResumeTime;
    public long mPauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        myApp = (XunPetApplication) getApplication();
        getWindow().setBackgroundDrawable(null);
    }

    protected XunPetApplication getMyApp() {
        return myApp;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumeTime = SystemClock.uptimeMillis();
        mPauseTime = -1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPauseTime = SystemClock.uptimeMillis();
        myApp.statsTime(mPauseTime - mResumeTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(baseRevReceiver);
            XunPetBean.recycleDrawable();
        } catch (Exception e) {
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_right);
    }
}
