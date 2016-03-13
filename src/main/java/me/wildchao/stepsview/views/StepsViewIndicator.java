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
    private float mLastY = 0;

    private int mStepsViewContainerWidth;

    private int mOrientation;

    private int mIndicatorWidth;

    public static final int ORIENTATION_HORIZONTAL = 0; // 水平方向
    public static final int ORIENTATION_VERTICAL = 1;   // 竖直方向

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
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mOrientation == ORIENTATION_HORIZONTAL) {
                    float deltaX = x - mLastX;  // 滑动距离
                    if (0 > deltaX && 0 < getLeft() ||  // 向右滑
                            0 < deltaX && getLeft() < mStepsViewContainerWidth - mIndicatorWidth) {  // 向左滑
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                        lp.leftMargin += deltaX;
                        requestLayout();
                    }
                } else {
                    float deltaY = x - mLastY;  // 滑动距离
                    if (0 > deltaY && 0 < getTop() ||  // 向右滑
                            0 < deltaY && getTop() < mStepsViewContainerWidth - mIndicatorWidth) {  // 向左滑
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                        lp.topMargin += deltaY;
                        requestLayout();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                if (mOrientation == ORIENTATION_HORIZONTAL) {
                    mIndicatorChangeListener.onChanged(lp.leftMargin + mIndicatorWidth / 2);
                } else {
                    mIndicatorChangeListener.onChanged(lp.topMargin + mIndicatorWidth / 2);
                }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void setStepsViewTrackLength(int stepsViewTrackLength, int orientation) {
        this.mStepsViewContainerWidth = stepsViewTrackLength;
        this.mOrientation = orientation;
    }


    public interface IndicatorChangeListener {
        void onChanged(int x);
    }

    public void setIndicatorChangeListener(IndicatorChangeListener listener) {
        mIndicatorChangeListener = listener;
    }

}
