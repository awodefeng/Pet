package com.xxun.watch.xunpet;

/**
 * Created by huangyouyang on 2017/9/4.
 */

public class Const {

    // tag
    public static final String LOG_TAG = "HYY ";
    public static final String XUNPET_UNDERLINE = "_";

    //
    public static final int XUNPET_FOODNUMBER_MAX = 5;
    public static final int XUNPET_FEEDTIME_MAX = 3;
    public static final int XUNPET_RECEIVE_FOOD_DAILY = 2;
    public static final int XUNPET_STEPS_CAN_RECEIVE_FOOD = 2000;

    //screen dp
    public static final float SCREEN_DP = 240f;  //屏幕大小 单位：dp

    // sound stretch params
    public static final float SOUNDSTRETCH_TEMPO = 0.0f;  //节奏
    public static final float SOUNDSTRETCH_PITCH = 9.0f;  //音调
    public static final float SOUNDSTRETCH_RATE = 0.0f;  //节奏和音调

    // sound path
    public static final String SOUND_SOURCE_WAV = "sourcesound.wav";  //源音频
    public static final String SOUND_STRETCH_WAV = "stretchsound.wav";  //变声后音频
    public static final String MY_BASE_DIR = "xunpet";

    // pet type
    public static final int KEY_XUNPET_TYPE_GUGONG_CAT = 0;
    public static final int KEY_XUNPET_TYPE_IMIBABY = 1;
    public static final int KEY_XUNPET_TYPE_CAT = 2;
    public static final int KEY_XUNPET_TYPE_BEAR = 3;
    public static final int KEY_XUNPET_TYPE_CHICKEN = 4;
    // anim type
    public static final int XUNPET_ANIM_TYPE_TOP = 0;
    public static final int XUNPET_ANIM_TYPE_BOTTOM = 1;
    public static final int XUNPET_ANIM_TYPE_LEFT = 2;
    public static final int XUNPET_ANIM_TYPE_RIGHT = 3;

    // drag
    public static final int XUNPET_DRAG_MIN = 50;

    //SHARE_PREF_NAME
    static final String SHARE_PREF_NAME = "xunpet_share";
    //SHARE_PREF Key
    public static final String SHARE_PREF_KEY_XUNPET_TYPE = "xunpet_type";  //int, 0 imibabay, 1 cat, 2 bear
    public static final String SHARE_PREF_KEY_XUNPET_ANIM_TYPE = "xunpet_anim_type";  //int, 0 top, 1 bottom, 2 left, 3 right
    public static final String SHARE_PREF_KEY_XUNPET_FEED_TIMES = "xunpet_feed_times";  //int, 可喂养次数
    public static final String SHARE_PREF_KEY_XUNPET_FOOD_NUMBER = "xunpet_food_number";  //int, 食物份数
    public static final String SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_LASTDAY = "receive_food_lastday";  //String, 上次领取食物日期 yyyyMMdd
    public static final String SHARE_PREF_KEY_XUNPET_RECEIVEFOOD_BY_STEP = "receive_food_by_step";  //int, 通过计步领取食物份数
    public static final String SHARE_PREF_KEY_XUNPET_STEP_LOCAL = "step_local";  //int, 步数
    // 等级、排名
    public static final String SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK = "xunpet_experience_rank";  //String，排名Json
    public static final String SHARE_PREF_KEY_XUNPET_EXPERIENCE = "xunpet_experience";  //int，经验等级
    public static final String SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK_TIMESTAMP = "xunpet_experience_rank_stamp";  //String，拉取经验排名的时间戳yyyyMMdd

    // action
    public static final String ACTION_BROAST_SENSOR_STEPS = "com.xxun.watch.stepstart.action.broast.sensor.steps";

    // constant
    public static final String KEY_NAME_PET_EXPER = "Empirical";
    public static final String KEY_NAME_PET_TGID = "TGID";
    public static final String KEY_NAME_SID = "SID";
    public static final String PREF_FILE_NAME = "StepFile";
}
