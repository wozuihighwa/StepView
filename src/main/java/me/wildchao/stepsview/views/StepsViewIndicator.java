package me.wildchao.stepsview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by 孙俊伟 on 2016/3/6.
 */
public class StepsViewIndicator extends ImageView {

    private float mLastX = 0;

    private int mStepsViewContainerWidth;

    private int mIndicatorWidth;

    private IndicatorChangeListener mIndicatorChangeListener;

    public StepsViewIndicator(Context context) {
        super(context);
    }

    public StepsViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mIndicatorWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;  // 滑动距离
                if (0 > deltaX && 0 < getLeft() ||  // 向右滑
                        0 < deltaX && getLeft() < mStepsViewContainerWidth - mIndicatorWidth) {  // 向左滑
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                    lp.leftMargin += deltaX;
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                mIndicatorChangeListener.onChanged(lp.leftMargin + mIndicatorWidth / 2);
            default:
                break;
        }
        mLastX = x;
        return true;
    }

    public void setStepsViewContainerWidth(int stepsViewContainerWidth) {
        this.mStepsViewContainerWidth = stepsViewContainerWidth;
    }


    public interface IndicatorChangeListener {
        void onChanged(int x);
    }

    public void setIndicatorChangeListener(IndicatorChangeListener listener) {
        mIndicatorChangeListener = listener;
    }

}
