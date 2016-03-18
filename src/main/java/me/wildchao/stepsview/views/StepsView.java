package me.wildchao.stepsview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙俊伟 on 2016/3/3.
 */
public class StepsView extends View {

    public static final int ORIENTATION_HORIZONTAL = 0; // 水平方向
    public static final int ORIENTATION_VERTICAL = 1;   // 竖直方向

    public static final int DEFAULT_ANCHOR_COUNT = 2;  // 锚点默认数量
    public static final int DEFAULT_ANCHOR_SIZE = 30;  // 锚点默认大小
    public static final int ANCHOR_RADIUS = 15;        // 锚点默认半径
    public static final int DEFAULT_TRACK_HEIGHT = 10; // 轨迹默认宽度

    private int mAnchorCount = DEFAULT_ANCHOR_COUNT;        // 锚点数量
    private int mAnchorSize = DEFAULT_ANCHOR_SIZE;          // 锚点大小
    private static final int anchorRadius = ANCHOR_RADIUS;  // 锚点默认半径
    private int mTrackLengh = DEFAULT_ANCHOR_SIZE / 3;      // 轨迹宽度


    private List<AnchorDrawParams> mAnchorAnchorDrawParamsList = new ArrayList<>(); // 保存锚点坐标
    private List<TrackDrawParams> mTrackDrawParamsList = new ArrayList<>(); // 保存轨迹绘制参数

    private int mAnchorColor;                  // 锚点颜色
    private int mTrackColor;                   // 轨迹颜色
    private int mOrientation;                  // 布局方向

    private Paint mAnchorPaint = new Paint(); // 绘制圆点
    private Paint mTrackPaint = new Paint(); // 绘制轨道

    private int mStepViewWidth; // StepView 宽度
    private int mStepViewHeight; // StepView 高度

    private StepViewOnClickListener mStepViewOnClickListener;

    public StepsView(Context context, int anchorSize, int anchorCount, int anchorColor, int trackColor, int orientation) {
        super(context);
        mAnchorSize = anchorSize;
        mAnchorCount = anchorCount;
        mAnchorColor = anchorColor;
        mTrackColor = trackColor;
        mOrientation = orientation;
        init();
    }

    public StepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     */
    private void init() {
        mAnchorPaint.setAntiAlias(true);
        mAnchorPaint.setColor(mAnchorColor);
        mAnchorPaint.setStyle(Paint.Style.FILL);
        mAnchorPaint.setStrokeWidth(1);

        mTrackPaint.setAntiAlias(true);
        mTrackPaint.setColor(mTrackColor);
        mTrackPaint.setStyle(Paint.Style.FILL);
        mTrackPaint.setStrokeWidth(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStepViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mStepViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        makeAnchorAndTrack();

        // 绘制锚点
        for (int i = 0; i < mAnchorCount; i++) {
            canvas.drawCircle(mAnchorAnchorDrawParamsList.get(i).x,
                    mAnchorAnchorDrawParamsList.get(i).y,
                    mAnchorSize / 2, mAnchorPaint);
        }

        // 绘制轨迹
        for (int j = 0; j < mAnchorCount - 1; j++) {
            canvas.drawRect(mTrackDrawParamsList.get(j).l, mTrackDrawParamsList.get(j).t,
                    mTrackDrawParamsList.get(j).r, mTrackDrawParamsList.get(j).b, mTrackPaint);
        }

    }

    /**
     * 生成锚点和轨迹坐标
     */
    private void makeAnchorAndTrack() {
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL:
                setHorizontalAnchorAndTrack();
                break;
            case ORIENTATION_VERTICAL:
                setVerticalAnchorAndTrack();
                break;
            default:
                break;
        }
    }

    /**
     * 生成水平方向的锚点和轨迹坐标
     */
    private void setHorizontalAnchorAndTrack() {
        mAnchorAnchorDrawParamsList.clear();
        mTrackDrawParamsList.clear();

        if (mAnchorCount == 1) {
            mTrackLengh = 0;
        } else {
            mTrackLengh = (mStepViewWidth - mAnchorSize) / (mAnchorCount - 1); // 重新计算轨迹长度
        }

        // 生成锚点坐标
        for (int i = 0; i < mAnchorCount; i++) {
            mAnchorAnchorDrawParamsList.add(new AnchorDrawParams(mAnchorSize / 2 + mTrackLengh * i,
                    mStepViewHeight / 2));
        }
        // 生成轨迹坐标
        for (int j = 0; j < mAnchorCount - 1; j++) {
            mTrackDrawParamsList.add(new TrackDrawParams(mAnchorSize / 2 + mTrackLengh * j,
                    mStepViewHeight / 2 - DEFAULT_TRACK_HEIGHT / 2,
                    (mAnchorSize / 2 + mTrackLengh * j) + mTrackLengh,
                    mStepViewHeight / 2 + DEFAULT_TRACK_HEIGHT / 2));
        }
    }

    /**
     * 生成竖直方向的锚点和轨迹坐标
     */
    private void setVerticalAnchorAndTrack() {
        mAnchorAnchorDrawParamsList.clear();
        mTrackDrawParamsList.clear();

        if (mAnchorCount == 1) {
            mTrackLengh = 0;
        } else {
            mTrackLengh = (mStepViewHeight - mAnchorSize) / (mAnchorCount - 1); // 重新计算轨迹长度
        }

        // 生成锚点坐标
        for (int i = 0; i < mAnchorCount; i++) {
            mAnchorAnchorDrawParamsList.add(new AnchorDrawParams(mStepViewWidth / 2,
                    mAnchorSize / 2 + mTrackLengh * i));
        }
        // 生成轨迹坐标
        for (int j = 0; j < mAnchorCount - 1; j++) {
            mTrackDrawParamsList.add(new TrackDrawParams(mStepViewWidth / 2 - DEFAULT_TRACK_HEIGHT / 2,
                    mAnchorSize / 2 + mTrackLengh * j,
                    mStepViewWidth / 2 + DEFAULT_TRACK_HEIGHT / 2,
                    mAnchorSize / 2 + mTrackLengh * j + mTrackLengh
            ));
        }
    }


    /**
     * 获取锚点坐标
     *
     * @return
     */
    public List<AnchorDrawParams> getAnchorDrawParams() {
        return mAnchorAnchorDrawParamsList;
    }

    public class AnchorDrawParams {
        public int x;
        public int y;

        public AnchorDrawParams(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public class TrackDrawParams {
        public int l;
        public int t;
        public int r;
        public int b;

        public TrackDrawParams(int l, int t, int r, int b) {
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
        }
    }

    public void setAnchorDrawParams(List<AnchorDrawParams> params) {
        mAnchorAnchorDrawParamsList.clear();
        mAnchorAnchorDrawParamsList.addAll(params);
        mAnchorCount = mAnchorAnchorDrawParamsList.size();
        invalidate();
    }

    public void setTrackDrawParams(List<TrackDrawParams> params) {
        mTrackDrawParamsList.clear();
        mTrackDrawParamsList.addAll(params);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int count = mAnchorAnchorDrawParamsList.size();
                switch (mOrientation) {
                    case ORIENTATION_HORIZONTAL:
                        for (int i = 0; i < count; i++) {
                            if (Math.abs(x - mAnchorAnchorDrawParamsList.get(i).x) < mTrackLengh / 2) {
                                if (mStepViewOnClickListener != null) {
                                    mStepViewOnClickListener.onStepViewClick(i, mAnchorAnchorDrawParamsList.get(i), mOrientation);
                                }
                            }
                        }
                        break;
                    case ORIENTATION_VERTICAL:
                        for (int i = 0; i < count; i++) {
                            if (Math.abs(y - mAnchorAnchorDrawParamsList.get(i).y) < mTrackLengh / 2) {
                                if (mStepViewOnClickListener != null) {
                                    mStepViewOnClickListener.onStepViewClick(i, mAnchorAnchorDrawParamsList.get(i), mOrientation);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    public interface StepViewOnClickListener {
        void onStepViewClick(int position, AnchorDrawParams params, int orientation);
    }

    public void setStepViewOnClickListener(StepViewOnClickListener stepViewOnClickListener) {
        this.mStepViewOnClickListener = stepViewOnClickListener;
    }
}
