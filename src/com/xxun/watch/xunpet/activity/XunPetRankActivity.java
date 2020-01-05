package com.xxun.watch.xunpet.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoxun.sdk.ResponseData;
import com.xiaoxun.sdk.IResponseDataCallBack;
import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.R;
import com.xxun.watch.xunpet.bean.XunPetRankBean;
import com.xxun.watch.xunpet.utils.LogUtil;
import com.xxun.watch.xunpet.utils.RankUtils;
import com.xxun.watch.xunpet.utils.TimeUtil;
import com.xxun.watch.xunpet.view.DragLayout;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 * Created by huangyouyang on 2017/9/4.
 */

public class XunPetRankActivity extends NormalActivity {

    private DragLayout layoutRoot;
    private TextView tvRankLevel;
    private TextView tvLocalCity;
    private TextView tvLocalRank;
    private TextView tvAllRank;
    private ImageView ivExperProgress;
    private TextView tvExperProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunpet_rank);

        initView();
        initListener();

        updatePetLevel();
        updatePetRank();
        getPetRankFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {

        layoutRoot = (DragLayout) findViewById(R.id.layout_root);
        tvRankLevel = (TextView) findViewById(R.id.tv_petrank_level);
        tvLocalCity = (TextView) findViewById(R.id.tv_local_city);
        tvLocalRank = (TextView) findViewById(R.id.tv_local_rank);
        tvAllRank = (TextView) findViewById(R.id.tv_all_rank);
        ivExperProgress = (ImageView) findViewById(R.id.iv_xunpet_exper_progress);
        tvExperProgress = (TextView) findViewById(R.id.tv_xunpet_exper_progress);
    }

    @SuppressLint("SetTextI18n")
    private void updatePetLevel() {

        double exper = myApp.getPetExper();
        int rankLevel = (int) RankUtils.getRankFromExper(exper);
        String allExperProgress = (int) (exper - RankUtils.getExperFromRank(rankLevel)) + getString(R.string.slash)
                + (int) RankUtils.getNeedExperFromRank(rankLevel + 1);

        tvRankLevel.setText(getString(R.string.pet_rank_level) + rankLevel);
        tvExperProgress.setText(allExperProgress);

        Bitmap progressBg = BitmapFactory.decodeResource(getResources(), R.drawable.pet_step_progress);
        double bgWidth = progressBg.getWidth();
        double newWidth = bgWidth * (exper - RankUtils.getExperFromRank(rankLevel)) / RankUtils.getNeedExperFromRank(rankLevel + 1);
        int bgHeight = progressBg.getHeight();

//        Bitmap newbmp = Bitmap.createBitmap((int)bgWidth, (int)bgHeight, Bitmap.Config.ARGB_8888);
//        Canvas cv = new Canvas(newbmp);
        if(newWidth==0) newWidth = 1;
        Bitmap newProgressBg = Bitmap.createBitmap(progressBg, 0, 0, (int)newWidth, bgHeight);
//        cv.drawBitmap(newProgressBg, 0, 0, null);
//        cv.save(Canvas.ALL_SAVE_FLAG);//保存
//        cv.restore();

        ivExperProgress.setBackground(new BitmapDrawable(getResources(),newProgressBg));
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivExperProgress.getLayoutParams();
//        layoutParams.width = (int) newWidth;
//        ivExperProgress.setLayoutParams(layoutParams);
    }

    @SuppressLint("SetTextI18n")
    private void updatePetRank(){
        String petRank=myApp.getStringValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK,"");
        if (!TextUtils.isEmpty(petRank)){
            JSONObject rankJo= (JSONObject) JSONValue.parse(petRank);
            XunPetRankBean rankBean=XunPetRankBean.toBeanFromJson(rankJo);
            tvLocalCity.setText(rankBean.cityName);
            tvLocalRank.setText(getString(R.string.front_rank) + 100*rankBean.cityOrder/rankBean.cityCount+"%");
            tvAllRank.setText(getString(R.string.front_rank) + 100*rankBean.totalOrder/rankBean.totalCount+"%");
        }
    }

    private void initListener() {

        layoutRoot.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void onDragLeft() {
            }

            @Override
            public void onDragRight() {
                XunPetRankActivity.this.finish();
            }
        });
    }

    public void getPetRankFromServer() {

        // 一天只拉取一次
        String timeStamp = myApp.getStringValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK_TIMESTAMP, "******");
        if (TimeUtil.getTimeStampDayLocal().equals(timeStamp))
            return;

        if (myApp.getNetService()!=null){
            myApp.getNetService().getPetRank(new IResponseDataCallBack.Stub() {
                @Override
                public void onSuccess(ResponseData responseData) {
                    LogUtil.i(Const.LOG_TAG + " getPetRank onSuccess: " + responseData.getResponseData());
                    myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK,responseData.getResponseData());
                    myApp.setValue(Const.SHARE_PREF_KEY_XUNPET_EXPERIENCE_RANK_TIMESTAMP, TimeUtil.getTimeStampDayLocal());
                    updatePetRank();
                }

                @Override
                public void onError(int i, String s) {
                    LogUtil.i(Const.LOG_TAG + " getPetRank onError: " + "i=" + i + "  s=" + s);
                }
            });
        }
    }
}
