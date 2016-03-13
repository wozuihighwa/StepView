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
import java.util.List;

import me.wildchao.stepsview.R;

/**
 * Created by 孙俊伟 on 2016/3/13.
 */
public class StepsView extends RelativeLayout {

    private List<StepsViewByChaoS.AnchorDrawParams> mAnchorParams = new ArrayList<>();
    private List<StepsViewByChaoS.TrackDrawParams> mTrackParams = new ArrayList<>();

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
    private int mTextSize = 16;                          // 标签字体大小
    private int mIndicatorIconResId;                     // 指示器图标 id

    public static final int DEFAULT_ANCHOR_SIZE = 30;       // 锚点默认大小
    private int mAnchorSize = DEFAULT_ANCHOR_SIZE;          // 锚点大小
    private int mTrackLengh = DEFAULT_ANCHOR_SIZE / 3;      // 轨迹宽度
    public static final int DEFAULT_TRACK_HEIGHT = DEFAULT_ANCHOR_SIZE / 3;     // 轨迹默认宽度
    private int mTitleAndStepsViewHeight = 108;                                 // 默认 StepsView 高度

    private StepsViewByChaoS mStepsView;                                        // 步骤控件
    private RelativeLayout mStepsViewContainer;                                 // 步骤控件包裹
    private StepsViewIndicator mIndicator;                                      // 指示器

    private List<String> mTopTips = new ArrayList<>();
    private List<String> mBottomTips = new ArrayList<>();
    private List<String> mLeftTips = new ArrayList<>();
    private List<String> mRightTips = new ArrayList<>();

    private StepChangeListener mStepChangeListener;

    public StepsView(Context context) {
        super(context);
    }

    public StepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStepsViewLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        mStepsViewLayoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 如果是水平方向
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            mStepViewWidth = mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize;
            mStepViewHeight = mTitleAndStepsViewHeight;
        }
        // 如果是垂直方向
        if (mOrientation == ORIENTATION_VERTICAL) {
            mStepViewWidth = mTitleAndStepsViewHeight;
            mStepViewHeight = mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize;
        }
    }

    public StepsView setOrientation(int orientation) {
        mOrientation = orientation;
        return this;
    }

    public StepsView setTopTips(List<String> tips) {
        mTopTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    public StepsView setBottomTips(List<String> tips) {
        mBottomTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    public StepsView setLeftTips(List<String> tips) {
        mLeftTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    public StepsView setRightTips(List<String> tips) {
        mRightTips.addAll(tips);
        mAnchorCount = tips.size();
        return this;
    }

    public StepsView setIndicator(int resId) {
        mIndicatorIconResId = resId;
        return this;
    }

    public StepsView setAnchorColor(int resId) {
        mAnchorColor = ContextCompat.getColor(getContext(), resId);
        return this;
    }

    public StepsView setTrackColor(int resId) {
        mTrackColor = ContextCompat.getColor(getContext(), resId);
        return this;
    }

    public StepsView setTipTextSize(int pixel) {
        mTextSize = pixel;
        return this;
    }

    public StepsView setTipTextColor(int resId) {
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
                addTopTips();               // 添加上标签
                addStepsViewContainer();    // 添加 StepsView 包裹
                addStepsView();             // 添加 StepsView
                addBottomTips();            // 添加下标签
                addLeftTips();              // 添加左标签
                addRightTips();             // 添加右标签
                addIndicator();             // 添加指示器
            }
        });
    }

    /**
     * 添加上标签
     */
    private void addTopTips() {
        if (mTopTips.size() > 0) {
            LinearLayout topTipsContainer = new LinearLayout(getContext());
            topTipsContainer.setId(R.id.title_container_id);
            topTipsContainer.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleAndStepsViewHeight);
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
    private void addBottomTips() {
        if (mBottomTips.size() > 0) {
            LinearLayout bottomContainer = new LinearLayout(getContext());
            bottomContainer.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleAndStepsViewHeight);
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
    private void addLeftTips() {
        if (mLeftTips.size() > 0) {
            LinearLayout leftTipsContainer = new LinearLayout(getContext());
            leftTipsContainer.setId(R.id.tips_left_container_id);
            leftTipsContainer.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(LEFT_OF, R.id.steps_view_container_id);

            for (int i = 0; i < mLeftTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mLeftTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                leftTipsContainer.addView(tip, itemLayoutParams);
            }

            addView(leftTipsContainer, lp);
        }
    }

    /**
     * 添加右标签
     */
    private void addRightTips() {
        if (mRightTips.size() > 0) {
            LinearLayout rightTipsContainer = new LinearLayout(getContext());
            rightTipsContainer.setId(R.id.tips_right_container_id);
            rightTipsContainer.setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RIGHT_OF, R.id.steps_view_container_id);

            for (int i = 0; i < mRightTips.size(); i++) {
                LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                itemLayoutParams.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(mRightTips.get(i));
                tip.setTextSize(mTextSize);
                tip.setTextColor(mTextColor);
                tip.setGravity(Gravity.CENTER);
                rightTipsContainer.addView(tip, itemLayoutParams);
            }

            addView(rightTipsContainer, lp);
        }
    }

    /**
     * 添加指示器
     */
    private void addIndicator() {
        mIndicator = new StepsViewIndicator(getContext());
        mIndicator.setImageDrawable(ContextCompat.getDrawable(getContext(), mIndicatorIconResId));
        LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (mOrientation == ORIENTATION_HORIZONTAL) {
            lp.addRule(CENTER_VERTICAL);
            mIndicator.setStepsViewTrackLength(mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize, ORIENTATION_HORIZONTAL);
            mIndicator.setIndicatorChangeListener(new StepsViewIndicator.IndicatorChangeListener() {
                @Override
                public void onChanged(int x) {
                    int paramCount = mAnchorParams.size();
                    int index = 0;
                    for (int i = 0; i < paramCount; i++) {
                        if (Math.abs(mAnchorParams.get(i).x - x) < mTrackLengh / 2) {
                            index = i;
                            break;
                        }
                    }
                    if (mStepChangeListener != null) {
                        mStepChangeListener.onIndicatorChanged(index);
                    }
                }
            });
        } else {
            lp.addRule(CENTER_HORIZONTAL);
            mIndicator.setStepsViewTrackLength(mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize, ORIENTATION_VERTICAL);
            mIndicator.setIndicatorChangeListener(new StepsViewIndicator.IndicatorChangeListener() {
                @Override
                public void onChanged(int x) {
                    int paramCount = mAnchorParams.size();
                    int index = 0;
                    for (int i = 0; i < paramCount; i++) {
                        if (Math.abs(mAnchorParams.get(i).x - x) < mTrackLengh / 2) {
                            index = i;
                            break;
                        }
                    }
                    if (mStepChangeListener != null) {
                        mStepChangeListener.onIndicatorChanged(index);
                    }
                }
            });
        }

        mStepsViewContainer.addView(mIndicator, lp);
    }

    /**
     * 添加 StepView 的包裹布局
     */
    private void addStepsViewContainer() {

        mStepsViewContainer = new RelativeLayout(getContext());
        mStepsViewContainer.setId(R.id.steps_view_container_id);

        RelativeLayout.LayoutParams lp = null;

        // 如果是水平方向
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            lp = new LayoutParams(mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize,
                    mTitleAndStepsViewHeight);
            lp.addRule(CENTER_HORIZONTAL);

            lp.addRule(BELOW, R.id.title_container_id);
        }

        // 如果是垂直方向
        if (mOrientation == ORIENTATION_VERTICAL) {
            lp = new LayoutParams(mTitleAndStepsViewHeight, mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize);
            lp.addRule(CENTER_IN_PARENT);
        }

        addView(mStepsViewContainer, lp);

    }

    /**
     * 添加 StepsView
     */
    private void addStepsView() {
        mStepsView = new StepsViewByChaoS(getContext(), mAnchorCount, mAnchorColor, mTrackColor, mOrientation);

        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(CENTER_HORIZONTAL);

        mStepsViewContainer.addView(mStepsView, lp);

        switch (mOrientation) {
            case StepsViewByChaoS.ORIENTATION_VERTICAL:
                setVerticalAnchorAndTrack();
                break;
            case StepsViewByChaoS.ORIENTATION_HORIZONTAL:
                setHorizontalAnchorAndTrack();
                break;
            default:
                break;
        }
    }

    /**
     * 生成竖直方向的锚点和轨迹坐标
     */
    private void setVerticalAnchorAndTrack() {
        mAnchorParams.clear();
        mTrackParams.clear();

        mTrackLengh = (mStepViewHeight - mAnchorSize) / (mAnchorCount - 1);   // 重新计算轨迹长度

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

        mStepsView.setAnchorDrawParams(mAnchorParams);
        mStepsView.setTrackDrawParams(mTrackParams);
    }

    /**
     * 生成水平方向的锚点和轨迹坐标
     */
    private void setHorizontalAnchorAndTrack() {
        mAnchorParams.clear();
        mTrackParams.clear();

        mTrackLengh = (mStepViewWidth - mAnchorSize) / (mAnchorCount - 1);  // 重新计算轨迹长度

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

        mStepsView.setAnchorDrawParams(mAnchorParams);
        mStepsView.setTrackDrawParams(mTrackParams);
    }

    /**
     * 指示器发生改变监听器
     */
    public interface StepChangeListener {
        void onIndicatorChanged(int index);
    }

    public void setStepChangeListener(StepChangeListener listener) {
        mStepChangeListener = listener;
    }
}
