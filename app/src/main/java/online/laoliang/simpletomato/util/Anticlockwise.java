package online.laoliang.simpletomato.util;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;

import online.laoliang.simpletomato.activity.MainActivity;
import online.laoliang.simpletomato.service.RingtonePlayingService;

/**
 * Created by lpy on 17-12-3.
 */

public class Anticlockwise extends Chronometer {

    private long mTime;
    private long mNextTime;
    private OnTimeCompleteListener mListener;
    private SimpleDateFormat mTimeFormat;
    private Context context = ContextHolder.getContext();
    private Intent intent = new Intent(context, RingtonePlayingService.class);

    public Anticlockwise(Context context) {
        super(context);
    }

    public Anticlockwise(Context context, AttributeSet attrs) {
        super(context, attrs);
        //自动生成的构造函数存根
        mTimeFormat = new SimpleDateFormat("mm:ss");
        this.setOnChronometerTickListener(listener);
    }

//    /**
//     * 重新启动计时
//     */
//    public void reStart(long _time_s) {
//        if (_time_s == -1) {
//            mNextTime = mTime;
//        } else {
//            mTime = mNextTime = _time_s;
//        }
//        this.start();
//    }
//
//    public void reStart() {
//        reStart(-1);
//    }

    /**
     * 关闭闹钟响铃
     */
    public void ringStop() {
        context.stopService(intent);
    }

    /**
     * 设置时间格式
     *
     * @param pattern 计时格式
     */
    public void setTimeFormat(String pattern) {
        mTimeFormat = new SimpleDateFormat(pattern);
    }

    public void setOnTimeCompleteListener(OnTimeCompleteListener l) {
        mListener = l;
    }

    OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            // 更新进度条
            MainActivity.mainCircle.setProgress((int) ((float) (mTime - mNextTime) * ((float) 10000 / (float) mTime)));

            // 判断倒计时是否结束了
            if (mNextTime <= 0) {
                if (mNextTime == 0) {
                    Anticlockwise.this.stop();
                    if (null != mListener)
                        mListener.onTimeComplete();
                }
                mNextTime = 0;
                updateTimeText();
                // 进度条设置到100%
                MainActivity.mainCircle.setProgress(10000); //因为进度条控件总份数（mMax）设置成了10000
                // 启动一个服务播放当前系统闹钟音乐
                context.startService(intent);
                return;
            }

            mNextTime--;

            updateTimeText();
        }
    };

    /**
     * 初始化时间
     *
     * @param _time_s
     */
    public void initTime(long _time_s, String shouldDo) {
        mTime = mNextTime = _time_s;
        if (shouldDo != null) {
            this.setText(shouldDo);
        }
    }

    private void updateTimeText() {
        this.setText(mTimeFormat.format(new Date(mNextTime * 1000)));
    }

    interface OnTimeCompleteListener {
        void onTimeComplete();
    }
}
