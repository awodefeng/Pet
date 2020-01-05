package com.xxun.watch.xunpet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.xxun.watch.xunpet.Const;
import net.minidev.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangjun5 on 2017/10/27.
 */

public class StepsCountUtils {

    public static void initSensor(final Context context, final String getType) {

        Log.e("steps", "ender sersor steps!");
        final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;
        final SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        final Sensor mStepCount = mSensorManager.getDefaultSensor(sensorTypeC);

        if (mStepCount != null) {
            mSensorManager.registerListener(new SensorEventListener() {

                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    try {
                        if (sensorEvent.sensor.getType() == sensorTypeC) {
                            int totalSteps = (int) sensorEvent.values[0];
                            Log.e("send steps broast:", totalSteps + ":");
                            Intent intent = new Intent(Const.ACTION_BROAST_SENSOR_STEPS);
                            intent.putExtra("sensor_steps", String.valueOf(totalSteps));
                            intent.putExtra("sensor_type", getType);
                            String timeStamp = getTimeStampLocal();
                            intent.putExtra("sensor_timestamp", timeStamp);
                            context.sendBroadcast(intent);
                            mSensorManager.unregisterListener(this, mStepCount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            }, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public static String getTimeStampLocal() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format.format(d).toString();
    }

    public static String getPhoneStepsByFirstSteps(Context context, String oldSteps, String mTotalSteps) {
        String curSteps = "0";
        if (mTotalSteps == null) {
            return curSteps;
        }
        try {
            if (oldSteps.equals("0")) {
                return curSteps;
            }
            Log.e("steps:", oldSteps + ":" + mTotalSteps);
            String steps[] = oldSteps.split("_");
            if (steps.length >= 3 && compareTodayToLastInfo(steps[0]) && Integer.valueOf(mTotalSteps) < Integer.valueOf(steps[2])) {
                int stepOffset = Integer.valueOf(steps[2]) - Integer.valueOf(steps[1]);
                curSteps = String.valueOf(Integer.valueOf(mTotalSteps) + stepOffset);
            } else if (Integer.valueOf(mTotalSteps) > Integer.valueOf(steps[1])) {
                curSteps = String.valueOf(Integer.valueOf(mTotalSteps) - Integer.valueOf(steps[1]));
            }
        } catch (Exception e) {
            curSteps = "0";
        }

        return curSteps;
    }

    public static boolean compareTodayToLastInfo(String oldData) {
        boolean isToday = false;
        String curTime = getTimeStampLocal();
        String curDate = curTime.substring(0, 8);
        String oldDate = oldData.substring(0, 8);
        Log.e("curTime:", oldDate + ":" + curDate);
        if (curDate.equals(oldDate)) {
            isToday = true;
        }
        return isToday;
    }


    public static JSONObject obtainCloudMsgContent(int cid, int sn, String sid, JSONObject pl) {
        JSONObject msg = new JSONObject();
        msg.put("Version", "00190000");
        msg.put("CID", cid);
        if (sid != null) {
            msg.put(Const.KEY_NAME_SID, sid);
        }
        msg.put("SN", sn);
        if (pl != null) {
            msg.put("PL", pl);
        }
        return msg;
    }

    public static void setValue(Context context, String key, String value) {
        final SharedPreferences preferences = context.getSharedPreferences(Const.PREF_FILE_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringValue(Context context, String key, String defValue) {
        return context.getSharedPreferences(Const.PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(key, defValue);
    }
}
