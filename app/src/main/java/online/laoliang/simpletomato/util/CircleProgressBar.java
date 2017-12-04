package online.laoliang.simpletomato.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lpy on 17-12-4.
 */

public class CircleProgressBar extends View {

    private Paint mBackPaint;
    private Paint mFrontPaint;
    private Paint mTextPaint;
    // 画笔宽度
    private float mStrokeWidth = 20;
    // 画笔的一半宽度
    private float mHalfStrokeWidth = 50 / 2;
    // 半径
    private float mRadius = 560;
    private RectF mRect;
    // 当前进度
    private int mProgress = 0;
    // 目标进度
    private int mTargetProgress = 100;
    // 最大值(将一个圆圈分成多少份）
    private int mMax = 10000; //这里分成了10000份
    private int mWidth;
    private int mHeight;

    public CircleProgressBar(Context context) {
        this(context, null, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化画笔
    private void init() {
        // 背景画笔
        mBackPaint = new Paint();
        mBackPaint.setColor(Color.parseColor("#CD2626"));
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeWidth(mStrokeWidth);

        // 进度条画笔
        mFrontPaint = new Paint();
        mFrontPaint.setColor(Color.WHITE);
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStyle(Paint.Style.STROKE);
        mFrontPaint.setStrokeWidth(mStrokeWidth);

        // 文本画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(0); // 这里不需要显示文字，暂时将文字大小设置为0
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    // 计算真实大小
    private int getRealSize(int measureSpec) {
        int result = 1;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            result = (int) (mRadius * 2 + mStrokeWidth);
        } else {
            result = size;
        }
        return result;
    }

    // 设置当前进度
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    private void initRect() {
        if (mRect == null) {
            mRect = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            mRect.set(left, top, right, bottom);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        initRect();
        float angle = mProgress / (float) mMax * 360;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackPaint);
        canvas.drawArc(mRect, -90, angle, false, mFrontPaint);
        // 因为进度条总份数（mMax）设置成了10000，所以这里要显示百分比，就应该除以100
        canvas.drawText(mProgress / 100 + "%", mWidth / 2 + mHalfStrokeWidth, mHeight / 2 + mHalfStrokeWidth, mTextPaint);
//        if (mProgress < mTargetProgress) {
//            mProgress += 1;
//            // 延迟1000毫秒更新
//            postInvalidateDelayed(1000);
//        }
    }

}
