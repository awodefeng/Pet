package com.xxun.watch.xunpet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemProperties;
import android.util.XiaoXunUtil;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.provider.Settings;

import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.bean.XunPetBean;
import com.xxun.watch.xunpet.view.DragLayout;
import com.xxun.watch.xunpet.utils.LogUtil;

import pl.droidsonroids.gif.GifImageView;

import com.xxun.watch.xunpet.view.CustomGifView;

/**
 * Created by huangyouyang on 2017/9/4.
 */

public class XunPetAdoptActivity extends NormalActivity {

    // View
    private DragLayout layoutRoot;
    private ImageView ivXunPet;
    private ImageButton btnConfirmAdopt;
    private ImageButton btnRight;
    private ImageButton btnLeft;
    private View layoutConfirm;
    private View layoutConfirmSecond;
    private ImageView btnConfirm;
    private ImageView btnCancel;

    private int petType;

    private int[] petArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunpet_adopt);

        ifHaveAdopt();
        initView();
        initData();
        initListener();
    }


    private void ifHaveAdopt() {

//        petType = myApp.getIntValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, -1);
        petType = Settings.Global.getInt(this.getContentResolver(), Const.SHARE_PREF_KEY_XUNPET_TYPE, -1);
        if (petType != -1) {
            startActivity(new Intent(XunPetAdoptActivity.this, XunPetMainActivity.class));
            XunPetAdoptActivity.this.finish();
        }
    }

    private void initView() {

        layoutRoot = (DragLayout) findViewById(R.id.layout_root);
        ivXunPet = (ImageView) findViewById(R.id.iv_xunpet_adopt);
        btnConfirmAdopt = (ImageButton) findViewById(R.id.btn_confirm_adopt);
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnLeft = (ImageButton) findViewById(R.id.btn_left);
        layoutConfirm = findViewById(R.id.layout_confirm_adopt);
        layoutConfirmSecond = findViewById(R.id.layout_confirm_second_adopt);
        btnConfirm = (ImageView) findViewById(R.id.btn_confirm);
        btnCancel = (ImageView) findViewById(R.id.btn_cancel);
    }

    private void initData() {

        if (XiaoXunUtil.XIAOXUN_CONFIG_PALACE_SUPPORT) {
            petArray = new int[]{R.drawable.pet_gugongcat_adopt, R.drawable.pet_imibaby_adopt, R.drawable.pet_cat_adopt,
                    R.drawable.pet_bear_adopt, R.drawable.pet_chicken_adopt};
        } else {
            petArray = new int[]{R.drawable.pet_imibaby_adopt, R.drawable.pet_cat_adopt,
                    R.drawable.pet_bear_adopt, R.drawable.pet_chicken_adopt};
        }
        petType = 0;
        XunPetBean.showImage(XunPetAdoptActivity.this, petArray[petType], ivXunPet);
    }

    private void initListener() {

        btnConfirmAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutConfirmSecond.setVisibility(View.VISIBLE);
                layoutConfirm.setVisibility(View.GONE);
                btnRight.setVisibility(View.GONE);
                btnLeft.setVisibility(View.GONE);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_TYPE, petType);
                Settings.Global.putInt(getContentResolver(), Const.SHARE_PREF_KEY_XUNPET_TYPE, petType);
//                SystemProperties.set("persist.sys.ispetadopted", "true");
                Settings.Global.putString(getContentResolver(), "ispetadopted", "true");
                resetSpValue();
                startActivity(new Intent(XunPetAdoptActivity.this, XunPetMainActivity.class));
                XunPetAdoptActivity.this.finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutConfirmSecond.setVisibility(View.GONE);
                layoutConfirm.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LogUtil.i(Const.LOG_TAG + " click right " );
                if (petType < petArray.length - 1)
                    petType++;
                else
                    petType = 0;
                XunPetBean.showImage(XunPetAdoptActivity.this, petArray[petType], ivXunPet);
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LogUtil.i(Const.LOG_TAG + " click left " );
                if (petType > 0)
                    petType--;
                else
                    petType = petArray.length - 1;
                XunPetBean.showImage(XunPetAdoptActivity.this, petArray[petType], ivXunPet);
            }
        });

        layoutRoot.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void onDragLeft() {
            }

            @Override
            public void onDragRight() {
                XunPetAdoptActivity.this.finish();
                Process.killProcess(Process.myPid());
            }
        });
    }

    private void resetSpValue() {

        // reset experience,but save food and steps
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_ANIM_TYPE);
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK);
        myApp.deletValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
