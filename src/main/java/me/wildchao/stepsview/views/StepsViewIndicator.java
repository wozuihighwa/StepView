package me.wildchao.stepsview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 孙俊伟 on 2016/3/6.
 */
public class StepsViewIndicator extends ImageView {

    private float mLastX = 0;
    private float mLastY = 0;

    private int mIndicatorSize = 108;

    private int mAnchorCount;

    private int mStepsViewContainerLengh;

    private int mTrackLength;

    private int mOrientation;

    public static final int ORIENTATION_HORIZONTAL = 0; // 水平方向
    public static final int ORIENTATION_VERTICAL = 1;   // 竖直方向

    private IndicatorChangeListener mIndicatorChangeListener;

    public StepsViewIndicator(Context context, int indicatorSize, int anchorCount, int trackLength) {
        super(context);
        this.mAnchorCount = anchorCount;
        this.mTrackLength = trackLength;
        if (this.mIndicatorSize != -1) {
            this.mIndicatorSize = indicatorSize;
        }
    }

    public StepsViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mIndicatorSize = MeasureSpec.getSize(widthMeasureSpec);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        float x = event.getX();
//        float y = event.getY();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mOrientation == ORIENTATION_HORIZONTAL) {   // 水平方向
//                    float deltaX = x - mLastX;  // 滑动距离
//                    if (deltaX > 0 && getLeft() < mStepsViewContainerLengh + StepsView.DEFAULT_ANCHOR_SIZE / 2 ||  // 向右滑
//                            deltaX < 0 && getLeft() > mStepsViewContainerLengh / mAnchorCount / 2 - mIndicatorSize / 2 + StepsView.DEFAULT_ANCHOR_SIZE / 2) {  // 向左滑
//                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                        lp.leftMargin += deltaX;
//                        requestLayout();
//                        return true;
//                    }
//                } else {    // 竖直方向
//                    float deltaY = y - mLastY;  // 滑动距离
//                    if (deltaY > 0 && getTop() < mStepsViewContainerLengh - mStepsViewContainerLengh / mAnchorCount / 2 - mIndicatorSize / 2 ||  // 向下滑
//                            deltaY < 0 && getTop() > mStepsViewContainerLengh / mAnchorCount / 2 - mIndicatorSize / 2) {  // 向上滑
//                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                        lp.topMargin += deltaY;
//                        requestLayout();
//                        return true;
//                    }
//                }
//                return true;
//            case MotionEvent.ACTION_UP:
//                if (mOrientation == ORIENTATION_HORIZONTAL && Math.abs(getLeft() - mLastX + mIndicatorSize / 2) > mTrackLength / 2) {
//                    mIndicatorChangeListener.onChanged(getLeft() + mIndicatorSize / 2);
//                } else if (mOrientation == ORIENTATION_VERTICAL && Math.abs(getTop() - mLastY + mIndicatorSize / 2) > mTrackLength / 2) {
//                    mIndicatorChangeListener.onChanged(getTop() + mIndicatorSize / 2);
//                }
//                break;
//            default:
//                break;
//        }
//        mLastX = x;
//        mLastY = y;
//        return true;
//    }

    public void setStepsViewTrackLength(int stepsViewTrackLength, int orientation) {
        this.mStepsViewContainerLengh = stepsViewTrackLength;
        this.mOrientation = orientation;
    }


    public interface IndicatorChangeListener {
        void onChanged(int moveXY);
    }

    public void setIndicatorChangeListener(IndicatorChangeListener listener) {
        mIndicatorChangeListener = listener;
    }

}
