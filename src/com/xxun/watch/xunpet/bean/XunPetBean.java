package com.xxun.watch.xunpet.bean;

import android.content.Context;
import android.util.XiaoXunUtil;
import android.widget.ImageView;

import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huangyouyang on 2017/9/13.
 */

public class XunPetBean {

    public int petDefault;
    public int petAnimTop;
    public int petAnimBottom;
    public int petAnimLeft;
    public int petAnimRight;
    public int petFeedListen;
    public int petFeedSpeak;
    public int petFeedIcon;
    public int petFood_0;
    public int petFood_1;
    public int petFood_2;
    public int petFood_3;
    public int petFood_4;
    public int petFood_5;
    public int petFeedEat;
    public int petFeedEatUp;

    public int petType;

    public XunPetBean(int petType) {

        this.petType = petType;

        switch (XiaoXunUtil.XIAOXUN_CONFIG_PALACE_SUPPORT ? petType : petType + 1) {
            case Const.KEY_XUNPET_TYPE_GUGONG_CAT:
                petDefault = R.drawable.pet_gugongcat_anim_default;
                petAnimTop = R.drawable.pet_gugongcat_anim_top;
                petAnimBottom = R.drawable.pet_gugongcat_anim_bottom;
                petAnimLeft = R.drawable.pet_gugongcat_anim_left;
                petAnimRight = R.drawable.pet_gugongcat_anim_right;
                petFeedListen = R.drawable.pet_gugongcat_listen;
                petFeedSpeak = R.drawable.pet_gugongcat_speak;
                petFeedIcon = R.drawable.btn_xunpet_imibaby_feed_selector;
                petFood_0 = R.drawable.pet_gugong_feed_fish_0;
                petFood_1 = R.drawable.pet_gugong_feed_fish_1;
                petFood_2 = R.drawable.pet_gugong_feed_fish_2;
                petFood_3 = R.drawable.pet_gugong_feed_fish_3;
                petFood_4 = R.drawable.pet_gugong_feed_fish_4;
                petFood_5 = R.drawable.pet_gugong_feed_fish_5;
                petFeedEat = R.drawable.pet_gugongcat_eat;
                petFeedEatUp = R.drawable.pet_gugongcat_eat_up;
                break;

            case Const.KEY_XUNPET_TYPE_IMIBABY:
                petDefault = R.drawable.pet_imibaby_default;
                petAnimTop = R.drawable.pet_imibaby_anim_top;
                petAnimBottom = R.drawable.pet_imibaby_anim_bottom;
                petAnimLeft = R.drawable.pet_imibaby_anim_left;
                petAnimRight = R.drawable.pet_imibaby_anim_right;
                petFeedListen = R.drawable.pet_imibaby_listen;
                petFeedSpeak = R.drawable.pet_imibaby_speak;
                petFeedIcon = R.drawable.btn_xunpet_imibaby_feed_selector;
                petFood_0 = R.drawable.pet_feed_radish_0;
                petFood_1 = R.drawable.pet_feed_radish_1;
                petFood_2 = R.drawable.pet_feed_radish_2;
                petFood_3 = R.drawable.pet_feed_radish_3;
                petFood_4 = R.drawable.pet_feed_radish_4;
                petFood_5 = R.drawable.pet_feed_radish_5;
                petFeedEat = R.drawable.pet_imibaby_eat;
                petFeedEatUp = R.drawable.pet_imibaby_eat_up;
                break;

            case Const.KEY_XUNPET_TYPE_CAT:
                petDefault = R.drawable.pet_cat_anim_right;
                petAnimTop = R.drawable.pet_cat_anim_top;
                petAnimBottom = R.drawable.pet_cat_anim_bottom;
                petAnimLeft = R.drawable.pet_cat_anim_left;
                petAnimRight = R.drawable.pet_cat_anim_right;
                petFeedListen = R.drawable.pet_cat_listen;
                petFeedSpeak = R.drawable.pet_cat_speak;
                petFeedIcon = R.drawable.btn_xunpet_cat_feed_selector;
                petFood_0 = R.drawable.pet_feed_fish_0;
                petFood_1 = R.drawable.pet_feed_fish_1;
                petFood_2 = R.drawable.pet_feed_fish_2;
                petFood_3 = R.drawable.pet_feed_fish_3;
                petFood_4 = R.drawable.pet_feed_fish_4;
                petFood_5 = R.drawable.pet_feed_fish_5;
                petFeedEat = R.drawable.pet_cat_eat;
                petFeedEatUp = R.drawable.pet_cat_eat_up;
                break;

            case Const.KEY_XUNPET_TYPE_BEAR:
                petDefault = R.drawable.pet_bear_anim_right;
                petAnimTop = R.drawable.pet_bear_anim_top;
                petAnimBottom = R.drawable.pet_bear_anim_bottom;
                petAnimLeft = R.drawable.pet_bear_anim_left;
                petAnimRight = R.drawable.pet_bear_anim_right;
                petFeedListen = R.drawable.pet_bear_listen;
                petFeedSpeak = R.drawable.pet_bear_speak;
                petFeedIcon = R.drawable.btn_xunpet_bear_feed_selector;
                petFood_0 = R.drawable.pet_feed_honey_0;
                petFood_1 = R.drawable.pet_feed_honey_1;
                petFood_2 = R.drawable.pet_feed_honey_2;
                petFood_3 = R.drawable.pet_feed_honey_3;
                petFood_4 = R.drawable.pet_feed_honey_4;
                petFood_5 = R.drawable.pet_feed_honey_5;
                petFeedEat = R.drawable.pet_bear_eat;
                petFeedEatUp = R.drawable.pet_bear_eat_up;
                break;

            case Const.KEY_XUNPET_TYPE_CHICKEN:
                petDefault = R.drawable.pet_chicken_anim_right;
                petAnimTop = R.drawable.pet_chicken_anim_top;
                petAnimBottom = R.drawable.pet_chicken_anim_bottom;
                petAnimLeft = R.drawable.pet_chicken_anim_left;
                petAnimRight = R.drawable.pet_chicken_anim_right;
                petFeedListen = R.drawable.pet_chicken_listen;
                petFeedSpeak = R.drawable.pet_chicken_speak;
                petFeedIcon = R.drawable.btn_xunpet_chicken_feed_selector;
                petFood_0 = R.drawable.pet_feed_worm_0;
                petFood_1 = R.drawable.pet_feed_worm_1;
                petFood_2 = R.drawable.pet_feed_worm_2;
                petFood_3 = R.drawable.pet_feed_worm_3;
                petFood_4 = R.drawable.pet_feed_worm_4;
                petFood_5 = R.drawable.pet_feed_worm_5;
                petFeedEat = R.drawable.pet_chicken_eat;
                petFeedEatUp = R.drawable.pet_chicken_eat_up;
                break;
        }
    }

    private static final float gifSpeed = 1.0f;
    private static GifDrawable gifResource;
    private static GifImageView gifImageView;

    public static void showGif(Context context, int resourceId, GifImageView imageView) {
        try {
            recycleDrawable();
            gifImageView = imageView;
            gifResource = new GifDrawable(context.getResources(), resourceId);
//            gifResource.setSpeed(XunPetBean.gifSpeed);
            if (!gifResource.isRecycled())
                gifImageView.setImageDrawable(gifResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recycleDrawable() {
        if (gifImageView != null) {
            gifImageView.setImageDrawable(null);
        }
        if (gifResource != null && !gifResource.isRecycled()) {
            gifResource.recycle();
            gifResource = null;
        }
    }

    public static void showImage(Context context, int resourceId, ImageView imageView) {
        imageView.setImageResource(resourceId);
    }
}
