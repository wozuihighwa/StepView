package me.wildchao.stepsview.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.wildchao.stepsview.R;

/**
 * Created by 孙俊伟 on 2016/3/13.
 */
public class StepsViewLayout extends RelativeLayout {

    private List<StepsView.AnchorDrawParams> mAnchorParams = new ArrayList<>();
    private List<StepsView.TrackDrawParams> mTrackParams = new ArrayList<>();

    public static final int ORIENTATION_HORIZONTAL = 0; // 水平方向
    public static final int ORIENTATION_VERTICAL = 1;   // 竖直方向

    private int mStepsViewLayoutWidth;      // 布局宽度
    private int mStepsViewLayoutHeight;     // 布局高度

    private int mStepViewWidth;             // StepView 的长度
    private int mStepViewHeight;            // StepView 的高度

    private int mOrientation;               // 布局的方向：0 水平 1 竖直
    private int mAnchorCount = 2;               // 锚点数量
    private int mAnchorColor = Color.GRAY;               // 锚点颜色
    private int mTrackColor = Color.GRAY;                // 轨迹颜色
    private int mTextColor = Color.BLACK;                // 标签字体颜色
    private int mTextSize = 14;                          // 标签字体大小
    private int mIndicatorIconResId = 0;                     // 指示器图标 id

    public static final int DEFAULT_ANCHOR_SIZE = 30;       // 锚点默认大小
    private int mAnchorSize = DEFAULT_ANCHOR_SIZE;          // 锚点大小
    private int mTrackLengh = DEFAULT_ANCHOR_SIZE / 3;      // 轨迹宽度
    public static final int DEFAULT_TRACK_HEIGHT = DEFAULT_ANCHOR_SIZE / 3;     // 轨迹默认宽度
    private static final int TITLE_AND_STEPS_VIEW_HEIGHT = 108;                                 // 默认 StepsView 高度
    private int mIndicatorSize = TITLE_AND_STEPS_VIEW_HEIGHT;
    private int mStepsViewLength;                            // StepsView 长度

    private StepsView mStepsView;                                        // 步骤控件
    private RelativeLayout mStepsViewContainer;                                 // 步骤控件包裹
    private StepsViewIndicator mIndicator;                                      // 指示器
    private LinearLayout mLeftTipsContainer;
    private LinearLayout mRightTipsContainer;

    private List<String> mTopTips = new ArrayList<>();
    private List<String> mBottomTips = new ArrayList<>();
    private List<String> mLeftTips = new ArrayList<>();
    private List<String> mRightTips = new ArrayList<>();
    private HashMap<String, String> mLeftRightTips = new HashMap<>();

    private StepChangeListener mStepChangeListener;

    public StepsViewLayout(Context context) {
        super(context);
    }

    public StepsViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStepsViewLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        mStepsViewLayoutHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 设置方向
     *
     * @param orientation
     * @return
     */
    public StepsViewLayout setOrientation(int orientation) {
        mOrientation = orientation;
        return this;
    }

    /**
     * 设置上标签
     *
     * @param tips
     * @return
     */
    public StepsViewLayout setTopTips(List<String> tips) {
        mTopTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    /**
     * 设置下标签
     *
     * @param tips
     * @return
     */
    public StepsViewLayout setBottomTips(List<String> tips) {
        mBottomTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    /**
     * 设置左标签
     *
     * @param tips
     * @return
     */
    public StepsViewLayout setLeftTips(List<String> tips) {
        mLeftTips.clear();
        mLeftTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    /**
     * 设置右标签
     *
     * @param tips
     * @return
     */
    public StepsViewLayout setRightTips(List<String> tips) {
        mRightTips.clear();
        mRightTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    /**
     * 设置左右两边标签
     *
     * @param leftRightTips
     * @return
     */
    public StepsViewLayout setLeftRightTips(HashMap<String, String> leftRightTips) {
        mLeftRightTips.putAll(leftRightTips);
        mAnchorCount = leftRightTips.size();
        return this;
    }

    /**
     * 设置滑动指示器
     *
     * @param resId
     * @return
     */
    public StepsViewLayout setIndicator(int resId) {
        mIndicatorIconResId = resId;
        mIndicatorSize = ContextCompat.getDrawable(getContext(), mIndicatorIconResId).getIntrinsicWidth();
        return this;
    }

    /**
     * 设置锚点颜色
     *
     * @param resId
     * @return
     */
    public StepsViewLayout setAnchorColor(int resId) {
        mAnchorColor = ContextCompat.getColor(getContext(), resId);
        return this;
    }

    /**
     * 设置轨迹颜色
     *
     * @param resId
     * @return
     */
    public StepsViewLayout setTrackColor(int resId) {
        mTrackColor = ContextCompat.getColor(getContext(), resId);
        return this;
    }

    /**
     * 设置标签文字大小
     *
     * @param pixel
     * @return
     */
    public StepsViewLayout setTipTextSize(int pixel) {
        mTextSize = pixel;
        return this;
    }

    /**
     * 设置标签文字颜色
     *
     * @param resId
     * @return
     */
    public StepsViewLayout setTipTextColor(int resId) {
        mTextColor = ContextCompat.getColor(getContext(), resId);
        return this;
    }

    /**
     * 创建 StepsView
     *
     * @return
     */
    public void create() {
        post(new Runnable() {
            @Override
            public void run() {
                generateCoordinates();      // 生成坐标
                addTopTips();               // 添加上标签
                addStepsViewContainer();    // 添加 StepsView 包裹
                addStepsView();             // 添加 StepsView
                addBottomTips();            // 添加下标签
                addLeftTips();              // 添加左标签
                addRightTips();             // 添加右标签
                addLeftRigthTips();         // 添加左右标签
                addIndicator();             // 添加指示器
                addListener();              // 设置监听器
            }
        });
    }

    /**
     * 生成坐标
     */
    private void generateCoordinates() {
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL:
                if (mAnchorCount > 0) {
                    mStepsViewLength = mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize;
                } else {
                    mStepsViewLength = 0;
                }
                break;
            case ORIENTATION_VERTICAL:
                if (mAnchorCount > 0) {
                    mStepsViewLength = mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize;
                } else {
                    mStepsViewLength = 0;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加上标签
     */
    public void addTopTips() {
        if (mTopTips.size() > 0) {
            LinearLayout topTipsContainer = new LinearLayout(getContext());
            topTipsContainer.setId(R.id.title_container_id);
            topTipsContainer.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIndicatorSize);
            lp.addRule(ALIGN_PARENT_TOP);
            addView(topTipsContainer, lp);

            for (int i = 0; i < mTopTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mTopTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                topTipsContainer.addView(tip, itemLayoutParams);
            }
        }
    }

    /**
     * 添加下标签
     */
    public void addBottomTips() {
        if (mBottomTips.size() > 0) {
            LinearLayout bottomContainer = new LinearLayout(getContext());
            bottomContainer.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TITLE_AND_STEPS_VIEW_HEIGHT);
            lp.addRule(BELOW, R.id.steps_view_container_id);
            addView(bottomContainer, lp);

            for (int i = 0; i < mBottomTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mBottomTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                bottomContainer.addView(tip, itemLayoutParams);
            }
        }
    }

    /**
     * 添加左标签
     */
    public void addLeftTips() {
        if (mLeftTipsContainer != null) {
            removeView(mLeftTipsContainer);
        }
        if (mLeftTips.size() > 0) {
            mLeftTipsContainer = new LinearLayout(getContext());
            mLeftTipsContainer.setId(R.id.tips_left_container_id);
            mLeftTipsContainer.setOrientation(LinearLayout.VERTICAL);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(LEFT_OF, R.id.steps_view_container_id);

            for (int i = 0; i < mLeftTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mLeftTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                mLeftTipsContainer.addView(tip, itemLayoutParams);
            }

            addView(mLeftTipsContainer, lp);
        }
    }

    /**
     * 添加右标签
     */
    public void addRightTips() {
        if (mRightTipsContainer != null) {
            removeView(mRightTipsContainer);
        }
        if (mRightTips.size() > 0) {
            mRightTipsContainer = new LinearLayout(getContext());
            mRightTipsContainer.setId(R.id.tips_right_container_id);
            mRightTipsContainer.setOrientation(LinearLayout.VERTICAL);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RIGHT_OF, R.id.steps_view_container_id);

            for (int i = 0; i < mRightTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mRightTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                mRightTipsContainer.addView(tip, itemLayoutParams);
            }

            addView(mRightTipsContainer, lp);
        }
    }

    /**
     * 添加左右标签
     */
    public void addLeftRigthTips() {
        int tipCount = mLeftRightTips.size();
        if (tipCount > 0) {
            for (String key : mLeftRightTips.keySet()) {
                mLeftTips.add(key);
            }
            for (String value : mLeftRightTips.values()) {
                mRightTips.add(value);
            }
            addLeftTips();
            addRightTips();
        }

    }

    /**
     * 添加 StepView 的包裹布局
     */
    private void addStepsViewContainer() {
        if (mStepsViewContainer != null) {
            removeView(mStepsViewContainer);
        }
        if (mTopTips.size() > 0 || mBottomTips.size() > 0 || mLeftTips.size() > 0 || mRightTips.size() > 0) {
            mStepsViewContainer = new RelativeLayout(getContext());
            mStepsViewContainer.setId(R.id.steps_view_container_id);

            LayoutParams lp = null;

            // 如果是水平方向
            if (mOrientation == ORIENTATION_HORIZONTAL) {
                lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIndicatorSize);
                lp.addRule(CENTER_HORIZONTAL);

                lp.addRule(BELOW, R.id.title_container_id);
            }

            // 如果是垂直方向
            if (mOrientation == ORIENTATION_VERTICAL) {
                lp = new LayoutParams(mIndicatorSize, mStepsViewLayoutHeight);
                lp.addRule(CENTER_IN_PARENT);
            }

            addView(mStepsViewContainer, lp);
        }
    }

    /**
     * 添加 StepsView
     */
    private void addStepsView() {
        if (mTopTips.size() > 0 || mBottomTips.size() > 0 || mLeftTips.size() > 0 || mRightTips.size() > 0) {
            mStepsView = new StepsView(getContext(), mAnchorSize, mAnchorCount, mAnchorColor, mTrackColor, mOrientation);
            LayoutParams lp = null;
            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL:
                    lp = new LayoutParams(mStepsViewLength, mAnchorSize);
                    break;
                case ORIENTATION_VERTICAL:
                    lp = new LayoutParams(mAnchorSize, mStepsViewLength);
                    break;
                default:
                    break;
            }
            lp.addRule(CENTER_IN_PARENT);

            mStepsViewContainer.addView(mStepsView, lp);
        }
    }

    /**
     * 添加指示器
     */
    private void addIndicator() {
        if (mIndicatorIconResId != 0) {
            mIndicator = new StepsViewIndicator(getContext(), -1, mAnchorCount, mTrackLengh);
            mIndicator.setImageDrawable(ContextCompat.getDrawable(getContext(), mIndicatorIconResId));
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if (mOrientation == ORIENTATION_HORIZONTAL) {
                lp.addRule(CENTER_VERTICAL);
                lp.leftMargin = (mStepsViewLayoutWidth / mAnchorCount / 2) - (mIndicatorSize / 2);
                mIndicator.setStepsViewTrackLength(mStepsViewLength, ORIENTATION_HORIZONTAL);
            } else {
                lp.addRule(CENTER_HORIZONTAL);
                lp.topMargin = mStepsViewLayoutHeight / mAnchorCount / 2 - (mIndicatorSize / 2);
                mIndicator.setStepsViewTrackLength(mStepsViewLayoutHeight, ORIENTATION_VERTICAL);
            }

            mStepsViewContainer.addView(mIndicator, lp);
        }
    }

    /**
     * 添加监听器
     */
    private void addListener() {
        if (mStepsView != null) {
            mStepsView.setStepViewOnClickListener(new StepsView.StepViewOnClickListener() {
                @Override
                public void onStepViewClick(int position, StepsView.AnchorDrawParams params, int orientation) {
                    if (mStepChangeListener != null) {
                        mStepChangeListener.onStepClick(position);
                    }
                    LayoutParams lp = (LayoutParams) mIndicator.getLayoutParams();
                    switch (orientation) {
                        case ORIENTATION_HORIZONTAL:
                            lp.leftMargin = params.x - mIndicatorSize / 2 + getWidth() / mAnchorCount / 2 - DEFAULT_ANCHOR_SIZE / 2;
                            break;
                        case ORIENTATION_VERTICAL:
                            lp.topMargin = params.y - mIndicatorSize / 2 + getHeight() / mAnchorCount / 2 - DEFAULT_ANCHOR_SIZE / 2;
                            break;
                        default:
                            break;
                    }
                    mIndicator.requestLayout();
                }
            });
        }

//        if (mOrientation == ORIENTATION_HORIZONTAL) {
//            setHorizontalAnchorAndTrack();
//            if (mIndicatorIconResId != 0) {
//                mIndicator.setIndicatorChangeListener(new StepsViewIndicator.IndicatorChangeListener() {
//
//                    @Override
//                    public void onChanged(int moveXY) {
//                        int paramCount = mAnchorParams.size();
//
//                        for (int i = 0; i < paramCount; i++) {
//                            if (Math.abs(mAnchorParams.get(i).x - moveXY) < mTrackLengh / 2) {
//                                if (mStepChangeListener != null) {
//                                    mStepChangeListener.onIndicatorChanged(i);
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        } else {
//            setVerticalAnchorAndTrack();
//            mIndicator.setIndicatorChangeListener(new StepsViewIndicator.IndicatorChangeListener() {
//
//                @Override
//                public void onChanged(int moveXY) {
//                    int paramCount = mAnchorParams.size();
//                    for (int i = 0; i < paramCount; i++) {
//                        if (Math.abs(mAnchorParams.get(i).y - moveXY) < mTrackLengh / 2) {
//                            if (mStepChangeListener != null) {
//                                mStepChangeListener.onIndicatorChanged(i);
//                            }
//                        }
//                    }
//                }
//            });
//        }
    }

    /**
     * 生成竖直方向的锚点和轨迹坐标
     */
    private void setVerticalAnchorAndTrack() {
        mAnchorParams.clear();
        mTrackParams.clear();

        // 重新计算轨迹长度
        if (mAnchorCount == 1) {
            mTrackLengh = 0;
        } else {
            mTrackLengh = (mStepsViewLength - mAnchorSize) / (mAnchorCount - 1);
        }

        // 生成锚点坐标
        for (int i = 0; i < mAnchorCount; i++) {
            mAnchorParams.add(mStepsView.new AnchorDrawParams(
                    mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) / 2,
                    mAnchorSize / 2 + mTrackLengh * i));
        }
        // 生成轨迹坐标
        for (int j = 0; j < mAnchorCount - 1; j++) {
            mTrackParams.add(mStepsView.new TrackDrawParams(
                    mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) / 2 - DEFAULT_TRACK_HEIGHT / 2,
                    mAnchorSize / 2 + mTrackLengh * j,
                    mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) / 2 + DEFAULT_TRACK_HEIGHT / 2,
                    mAnchorSize / 2 + mTrackLengh * j + mTrackLengh
            ));
        }
//
//        mStepsView.setAnchorDrawParams(mAnchorParams);
//        mStepsView.setTrackDrawParams(mTrackParams);
    }

    /**
     * 生成水平方向的锚点和轨迹坐标
     */
    private void setHorizontalAnchorAndTrack() {
        mAnchorParams.clear();
        mTrackParams.clear();

        // 重新计算轨迹长度
        if (mAnchorCount == 1) {
            mTrackLengh = 0;
        } else {
            mTrackLengh = (mStepsViewLength - mAnchorSize) / (mAnchorCount - 1);
        }

        // 生成锚点坐标
        for (int i = 0; i < mAnchorCount; i++) {
            mAnchorParams.add(mStepsView.new AnchorDrawParams(
                    mAnchorSize / 2 + mTrackLengh * i,
                    mStepViewHeight / 2));
        }
        // 生成轨迹坐标
        for (int j = 0; j < mAnchorCount - 1; j++) {
            mTrackParams.add(mStepsView.new TrackDrawParams(
                    mAnchorSize / 2 + mTrackLengh * j,
                    mStepViewHeight / 2 - DEFAULT_TRACK_HEIGHT / 2,
                    (mAnchorSize / 2 + mTrackLengh * j) + mTrackLengh,
                    mStepViewHeight / 2 + DEFAULT_TRACK_HEIGHT / 2));
        }

//        mStepsView.setAnchorDrawParams(mAnchorParams);
//        mStepsView.setTrackDrawParams(mTrackParams);
    }

    /**
     * 指示器发生改变监听器
     */
    public interface StepChangeListener {

        void onIndicatorChanged(int position);

        void onStepClick(int position);
    }

    public void setStepChangeListener(StepChangeListener listener) {
        mStepChangeListener = listener;
    }


}
