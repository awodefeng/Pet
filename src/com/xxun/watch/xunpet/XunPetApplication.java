package com.xxun.watch.xunpet;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import com.xiaoxun.sdk.XiaoXunNetworkManager;
import com.xiaoxun.sdk.ResponseData;
import com.xiaoxun.sdk.IResponseDataCallBack;
import com.xiaoxun.statistics.XiaoXunStatisticsManager;
import com.xxun.watch.xunpet.utils.AESUtil;
import com.xxun.watch.xunpet.utils.LogUtil;
import com.xxun.watch.xunpet.utils.TimeUtil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.File;

/**
 * Created by huangyouyang on 2017/9/2.
 */

public class XunPetApplication extends Application {

    private XiaoXunNetworkManager mNetService;
    public XiaoXunNetworkManager getNetService(){
        return mNetService;
    }
    private XiaoXunStatisticsManager mStatisticsManager;

    private String eid;
    private String gid;
    public int xunPetSteps;   //运动步数

    @Override
    public void onCreate() {
        super.onCreate();

//        addFoodNumber(10);

        initFiles();
        mNetService = (XiaoXunNetworkManager)getSystemService("xun.network.Service");
        mStatisticsManager = (XiaoXunStatisticsManager) getSystemService("xun.statistics.service");
    }

    public static File BASEDIR;
    private void initFiles() {

        if (BASEDIR == null)
            BASEDIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Const.MY_BASE_DIR);
        else
            BASEDIR = new File(BASEDIR.getPath());
        if (BASEDIR.exists() && !BASEDIR.isDirectory()) {
            BASEDIR.delete();
        }
        if (!BASEDIR.exists()) {
            BASEDIR.mkdirs();
        }
    }

    /*-------------------------------- login ----------------------------------------*/

    private void loginServer() {
        String username = "******";
        String msn = "******";
    }

    /*--------------------------------share_pref start----------------------------------------*/
    public void setValue(String key, boolean value) {
        final SharedPreferences preferences = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setValue(String key, int value) {
        final SharedPreferences preferences = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setValue(String key, String value) {
        final SharedPreferences preferences = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(key, AESUtil.encryptDataStr(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public boolean getBoolValue(String key, boolean defValue) {
        return getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, defValue);
    }

    public int getIntValue(String key, int defValue) {
        return getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE)
                .getInt(key, defValue);
    }

    public String getStringValue(String key, String defValue) {
        String str = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE)
                .getString(key, defValue);
        if (str == null || str.equals(defValue)) {
            return str;
        } else {
            try {
                return AESUtil.decryptDataStr(str);
            } catch (Exception e) {
                e.printStackTrace();
                return defValue;
            }
        }
    }

    public void deletValue(String key) {
        final SharedPreferences prefs = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public void deletAllValue() {
        final SharedPreferences prefs = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public boolean hasValue(String key) {
        SharedPreferences prefs = getSharedPreferences(Const.SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(key);
    }
    /*--------------------------------share_pref end----------------------------------------*/


    /*-------------------------------- set food start ----------------------------------------*/

    /**
     * 每天领取食物：2份
     */
    public void receiveFoodDaily() {

        String day = TimeUtil.getTimeStampDayLocal();
        String lastDay = getStringValue(Const.SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_LASTDAY, "");
        int interval ;
        if (lastDay.equals(""))
            interval = 1;
        else
            interval = TimeUtil.getInterval(day, lastDay);
        addFoodNumber(interval * Const.XUNPET_RECEIVE_FOOD_DAILY);
        reSetReceiveFoodLastDay(day);
    }
    private void reSetReceiveFoodLastDay(String day){
        setValue(Const.SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_LASTDAY, day);
    }

    /**
     * 通过计步领取食物
     * 每2000步可以领取一份食物
     */
    public void receiveFoodBySteps() {

        int steps = xunPetSteps;
        int foodNumByStep = getIntValue(Const.SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_BY_STEP + Const.XUNPET_UNDERLINE + TimeUtil.getTimeStampDayLocal(), 0);
        steps = steps - foodNumByStep * Const.XUNPET_STEPS_CAN_RECEIVE_FOOD;
        int foodNumAddByStep = steps / Const.XUNPET_STEPS_CAN_RECEIVE_FOOD;
        int foodNow = getFoodNumber();
        if ((foodNumAddByStep + foodNow) > Const.XUNPET_FOODNUMBER_MAX)
            foodNumAddByStep = Const.XUNPET_FOODNUMBER_MAX - foodNow;
        addFoodNumber(foodNumAddByStep);
        setValue(Const.SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_BY_STEP + Const.XUNPET_UNDERLINE + TimeUtil.getTimeStampDayLocal(), foodNumByStep + foodNumAddByStep);
    }

    /**
     * @return 通过计步领取食物后的计步数
     */
    public int getSteps() {
        int steps = xunPetSteps;
        int foodNumByStep = getIntValue(Const.SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_BY_STEP + Const.XUNPET_UNDERLINE + TimeUtil.getTimeStampDayLocal(), 0);
        steps = steps - foodNumByStep * Const.XUNPET_STEPS_CAN_RECEIVE_FOOD;
        return steps;
    }

    public int getFoodNumber() {
        return getIntValue(Const.SHARE_PREF_KEY_XUNPET_FOOD_NUMBER , 0);
    }

    /**
     * 领取食物、喂食一次后重置次数
     * @param foodNumber 食物变化数
     */
    public void addFoodNumber(int foodNumber) {
        foodNumber = getFoodNumber() + foodNumber;
        if (foodNumber > Const.XUNPET_FOODNUMBER_MAX)
            foodNumber =  Const.XUNPET_FOODNUMBER_MAX;
        setValue(Const.SHARE_PREF_KEY_XUNPET_FOOD_NUMBER , foodNumber);
    }

    /*-------------------------------- set food end ----------------------------------------*/


    /*-------------------------------- feed start ----------------------------------------*/

    public int getFeedMaxTimes() {
        int feedMaxTimes = getFoodNumber();
        if (feedMaxTimes >  Const.XUNPET_FEEDTIME_MAX)
            feedMaxTimes = Const.XUNPET_FEEDTIME_MAX;
        return feedMaxTimes;
    }
    public int getFeedTimes(String day, String slot) {
        return getIntValue(Const.SHARE_PREF_KEY_XUNPET_FEED_TIMES + Const.XUNPET_UNDERLINE + day
                + Const.XUNPET_UNDERLINE + slot, 0);
    }
    // 喂食一次后重置次数
    public void reSetFeedTimes(String day, String slot) {
        int feedTimes = getFeedTimes(day, slot) + 1;
        if (feedTimes > 3)
            feedTimes = 3;
        setValue(Const.SHARE_PREF_KEY_XUNPET_FEED_TIMES + Const.XUNPET_UNDERLINE+ day
                + Const.XUNPET_UNDERLINE+ slot, feedTimes);
    }

    /*------------------------------------- feed end -------------------------------------------*/


    /*--------------------------------- pet exper start ---------------------------------------*/

    public void addPetExper(int addExper) {
        int oldExper = getPetExper();
        int newExper = oldExper + addExper;
        setValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE, newExper);
    }

    public int getPetExper() {
        return getIntValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE, 0);
    }

    public void reportPetExperToServer(IResponseDataCallBack callBack) {

        String experValue = TimeUtil.getTimeStampLocal() + Const.XUNPET_UNDERLINE + getPetExper();
        String[] key = new String[2];
        key[0] = Const.KEY_NAME_PET_TGID;
        key[1] = Const.KEY_NAME_PET_EXPER;
        String[] value = new String[2];
        value[0] = mNetService.getWatchGid();
        value[1] = experValue;
        mNetService.paddingSetMapMSetValue(gid, key, value, callBack);
    }

    public void reportZeroToServer(IResponseDataCallBack callBack) {

        String[] key = new String[2];
        key[0] = Const.KEY_NAME_PET_TGID;
        key[1] = Const.KEY_NAME_PET_EXPER;
        String[] value = new String[2];
        value[0] = mNetService.getWatchGid();
        value[1] = "0";
        mNetService.paddingSetMapMSetValue(gid, key, value, callBack);
    }

    /*--------------------------------- pet exper end ---------------------------------------*/

    // 统计次数
    public void statsTimes(){
        mStatisticsManager.stats(XiaoXunStatisticsManager.STATS_PET);
    }

    // 统计时间
    public void statsTime(long time) {
        int timeValue = (int) time / 1000;
        if (timeValue > 0)
            mStatisticsManager.stats(XiaoXunStatisticsManager.STATS_PET_TIME, timeValue);
    }
}
