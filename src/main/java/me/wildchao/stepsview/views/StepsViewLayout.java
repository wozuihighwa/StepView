package me.wildchao.stepsview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
 * Created by 孙俊伟 on 2016/3/3.
 */
public class StepsViewLayout extends RelativeLayout {

    private List<StepsViewByChaoS.AnchorDrawParams> mAnchorParams = new ArrayList<>();
    private List<StepsViewByChaoS.TrackDrawParams> mTrackParams = new ArrayList<>();

    private int mStepsViewLayoutWidth;      // 布局宽度
    private int mStepsViewLayoutHeight;     // 布局高度

    private int mStepViewWidth;             // StepView 的长度
    private int mStepViewHeight;            // StepView 的高度

    private int mOrientation;               // 布局的方向：0 水平 1 竖直
    private int mAnchorCount;               // 锚点数量
    private int mAnchorColor;               // 锚点颜色
    private int mTrackColor;                // 轨迹颜色

    public static final int ORIENTATION_HORIZONTAL = 0; // 水平方向
    public static final int ORIENTATION_VERTICAL = 1;   // 竖直方向

    public static final int DEFAULT_ANCHOR_COUNT = 2;   // 锚点默认数量
    public static final int DEFAULT_ANCHOR_SIZE = 30;   // 锚点默认大小
    public static final int ANCHOR_RADIUS = 15;         // 锚点默认半径
    public static final int DEFAULT_TRACK_HEIGHT = 10;  // 轨迹默认宽度

    private int mAnchorSize = DEFAULT_ANCHOR_SIZE;          // 锚点大小
    private static final int anchorRadius = ANCHOR_RADIUS;  // 锚点默认半径
    private int mTrackLengh = DEFAULT_ANCHOR_SIZE / 3;      // 轨迹宽度

    private LinearLayout mTitleContainer;           // 放置 TextView 的容器
    private LinearLayout mLeftTipsContainer;        // 放置左边 Tips 的容器
    private LinearLayout mRightTipsContainer;       // 放置右边 Tips 的容器
    private RelativeLayout mStepsViewContainer;     // 放置 StepsView 的容器
    private StepsViewByChaoS mStepsView;            // 步骤控件
    private StepsViewIndicator mIndicator;                   // 指示器

    private boolean mAddTitle;                      // 是否添加标题
    private boolean mAddLeftTips;                   // 是否添加左边标签
    private boolean mAddRightTips;                  // 是否添加右边标签
    private int mLeftOrTopTipNum;                   // 左或上 Tip 数量
    private int mRigthOrBottomTipNum;               // 右或下 Tip 数量

    private int mTitleAndStepsViewHeight = 108;     // 默认 StepsView 高度

    private int mIndicatorIconResId;                // 指示器图案

    private boolean isFirstAddView = true;

    private StepChangeListener mStepChangeListener;

    public StepsViewLayout(Context context) {
        super(context);
    }

    public StepsViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepsViewLayout);
        mOrientation = ta.getInt(R.styleable.StepsViewLayout_orientation, ORIENTATION_HORIZONTAL);
        mAnchorCount = ta.getInt(R.styleable.StepsViewLayout_anchorCount, 2);
        mAnchorColor = ta.getColor(R.styleable.StepsViewLayout_anchorColor, Color.GRAY);
        mTrackColor = ta.getColor(R.styleable.StepsViewLayout_trackColor, Color.GRAY);
        mLeftOrTopTipNum = ta.getInt(R.styleable.StepsViewLayout_left_or_top_tip_num, 0);
        mRigthOrBottomTipNum = ta.getInt(R.styleable.StepsViewLayout_right_or_bottom_tip_num, 0);
        mIndicatorIconResId = ta.getResourceId(R.styleable.StepsViewLayout_indicator_icon, R.drawable.not_set_icon_id);
        mAddTitle = ta.getBoolean(R.styleable.StepsViewLayout_add_text_title, false);
        mAddLeftTips = ta.getBoolean(R.styleable.StepsViewLayout_add_left_tips, false);
        mAddRightTips = ta.getBoolean(R.styleable.StepsViewLayout_add_right_tips, false);

        ta.recycle();
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
//        if (isFirstAddView) {
//            addTitleCotainer();
//            addStepsViewContainer();
//            addStepsView();
//            addIndicator();
//            isFirstAddView = false;
//        }
    }

    /**
     * 添加 标题
     */
    private void addTitleCotainer() {
        if (mAddTitle && mOrientation == ORIENTATION_HORIZONTAL) {
            mTitleContainer = new LinearLayout(getContext());
            mTitleContainer.setId(R.id.title_container_id);
            mTitleContainer.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleAndStepsViewHeight);
            lp.addRule(ALIGN_PARENT_TOP);
            addView(mTitleContainer, lp);
        }
    }


    /**
     * 添加 StepView 的包裹布局
     */
    private void addStepsViewContainer() {
        if (mStepsViewContainer == null) {
            mStepsViewContainer = new RelativeLayout(getContext());
            mStepsViewContainer.setId(R.id.steps_view_container_id);
        }

        RelativeLayout.LayoutParams lp = null;

        // 如果是水平方向
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            lp = new LayoutParams(mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_HORIZONTAL);

            if (mAddTitle) {
                lp.addRule(BELOW, mTitleContainer.getId());
            }
        }

        // 如果是垂直方向
        if (mOrientation == ORIENTATION_VERTICAL) {
            lp = new LayoutParams(mTitleAndStepsViewHeight, mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize);
            lp.addRule(CENTER_IN_PARENT);
        }

        if (isFirstAddView) {
            addView(mStepsViewContainer, lp);
        } else {
            mStepsViewContainer.setLayoutParams(lp);
        }

    }

    /**
     * 重新计算 StepsViewContainer
     */
    private void reLayoutStepsViewContainer() {
        RelativeLayout.LayoutParams lp = null;
        // 如果是水平方向
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            lp = new LayoutParams(mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_HORIZONTAL);

            if (mAddTitle) {
                lp.addRule(BELOW, mTitleContainer.getId());
            }
        }
        // 如果是垂直方向
        if (mOrientation == ORIENTATION_VERTICAL) {
            lp = new LayoutParams(mTitleAndStepsViewHeight, mStepsViewLayoutHeight / mAnchorCount * (mAnchorCount - 1) + mAnchorSize);
            lp.addRule(CENTER_IN_PARENT);
        }
        mStepsViewContainer.setLayoutParams(lp);
    }

    /**
     * 添加 StepsView
     */
    private void addStepsView() {

        if (mStepsView == null) {
            mStepsView = new StepsViewByChaoS(getContext(), mAnchorCount, mAnchorColor, mTrackColor, mOrientation);
        }

        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(CENTER_HORIZONTAL);
        if (isFirstAddView) {
            mStepsViewContainer.addView(mStepsView, lp);
        } else {
            mStepsViewContainer.setLayoutParams(lp);
        }

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
     * 重新计算 StepsView
     */
    private void reLayoutStepsView() {
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(CENTER_HORIZONTAL);
        mStepsViewContainer.setLayoutParams(lp);
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
     * 添加左边标签
     */
    private void addLeftTipsContainer() {
        mLeftTipsContainer = new LinearLayout(getContext());
        mLeftTipsContainer.setBackgroundColor(Color.RED);
        mLeftTipsContainer.setId(R.id.tips_left_container_id);
        mLeftTipsContainer.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(LEFT_OF, R.id.steps_view_container_id);
        addView(mLeftTipsContainer, lp);
    }

    /**
     * 添加右边标签
     */
    private void addRightTipsContainer() {
        mRightTipsContainer = new LinearLayout(getContext());
        mRightTipsContainer.setBackgroundColor(Color.GREEN);
        mRightTipsContainer.setId(R.id.tips_right_container_id);
        mRightTipsContainer.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RIGHT_OF, R.id.steps_view_container_id);
        addView(mRightTipsContainer, lp);
    }

    /**
     * 添加 指示器
     */

    private void addIndicator() {
        if (mIndicatorIconResId != R.drawable.not_set_icon_id) {
            mIndicator = new StepsViewIndicator(getContext());
            mIndicator.setBackgroundResource(mIndicatorIconResId);
            LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_VERTICAL);
            mIndicator.setStepsViewTrackLength(mStepsViewLayoutWidth / mAnchorCount * (mAnchorCount - 1) + mAnchorSize,0);
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
                    mStepChangeListener.onIndicatorChanged(index);
                }
            });
            mStepsViewContainer.addView(mIndicator, lp);
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
     * 设置标题
     *
     * @param titleList
     */
    public void setTitle(List<String> titleList) {
        mAnchorCount = titleList.size();
        addStepsViewContainer();
        addStepsView();
        addIndicator();
        for (int i = 0; i < mAnchorCount; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            TextView title = new TextView(getContext());
            title.setText(titleList.get(i));
            title.setTextSize(16);
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            mTitleContainer.addView(title, lp);
        }
    }

    /**
     * 设置左边标签
     */
    public void setLeftTips(final List<String> tips) {
        if (mOrientation == ORIENTATION_VERTICAL && mAddLeftTips) {
            mAnchorCount = tips.size();
            reLayoutStepsViewContainer();
            reLayoutStepsView();
            addStepsView();
            addLeftTipsContainer();
            for (int i = 0; i < mAnchorCount; i++) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(tips.get(i));
                tip.setTextSize(16);
                tip.setTextColor(Color.BLACK);
                tip.setGravity(Gravity.CENTER);
                mLeftTipsContainer.addView(tip, lp);
            }
        }

    }

    /**
     * 设置右边标签
     *
     * @param tips
     */
    public void setRightTips(final List<String> tips) {
        if (mOrientation == ORIENTATION_VERTICAL && mAddRightTips) {
            mAnchorCount = tips.size();
            reLayoutStepsViewContainer();
            reLayoutStepsView();
            addRightTipsContainer();
            for (int i = 0; i < mAnchorCount; i++) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.weight = 1;
                TextView tip = new TextView(getContext());
                tip.setText(tips.get(i));
                tip.setTextSize(16);
                tip.setTextColor(Color.BLACK);
                tip.setGravity(Gravity.CENTER);
                mRightTipsContainer.addView(tip, lp);
            }
        }

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
