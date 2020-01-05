package com.xxun.watch.xunpet.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.xxun.watch.xunpet.Const;
import com.xxun.watch.xunpet.utils.DensityUtil;
import com.xxun.watch.xunpet.utils.LogUtil;

/**
 * Created by huangyouyang on 2017/9/9.
 */

public class DragLayout extends RelativeLayout {

    public void setOnDragStatusChangeListener(OnDragStatusChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnDragStatusChangeListener {
        void onDragLeft();

        void onDragRight();
    }

    private OnDragStatusChangeListener mListener;
    private Context context;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startX = event.getX();
            } else if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                float endX = event.getX();
                float disX = endX - startX;
                // LogUtil.i(Const.LOG_TAG + "  onInterceptTouchEvent  disX=" + disX);
                // 在onInterceptTouchEvent中处理不太好，后续需要优化
                if (Math.abs(disX) > DensityUtil.dip2px(context, Const.XUNPET_DRAG_MIN)) {
                    if (disX > DensityUtil.dip2px(context, Const.XUNPET_DRAG_MIN) && mListener != null)
                        mListener.onDragRight();
                    if (disX < DensityUtil.dip2px(context, -1 * Const.XUNPET_DRAG_MIN) && mListener != null)
                        mListener.onDragLeft();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private float startX;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startX = event.getX();
            } else if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                float endX = event.getX();
                float disX = endX - startX;
//                LogUtil.i(Const.LOG_TAG + "  onTouchEvent  disX=" + disX);
                if (disX > DensityUtil.dip2px(context, Const.XUNPET_DRAG_MIN) && mListener != null)
                    mListener.onDragRight();
                if (disX < DensityUtil.dip2px(context, -1 * Const.XUNPET_DRAG_MIN) && mListener != null)
                    mListener.onDragLeft();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
