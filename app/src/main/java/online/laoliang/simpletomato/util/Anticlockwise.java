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

    // 总时间（秒）
    private long mTime;
    // 剩余时间（秒）
    private long mNextTime;
    private OnTimeCompleteListener mListener;
    private SimpleDateFormat mTimeFormat;
    private Context context = ContextHolder.getContext();
    // 启动一个服务用来播放闹钟铃声的intent
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

            // 继续倒计时并更新界面时间
            mNextTime--;
            updateTimeText();
        }
    };

    /**
     * 初始化时间
     *
     * @param _time_min
     */
    public void initTime(int _time_min, String shouldDo) {
        mTime = mNextTime = _time_min * 60;
        if (shouldDo != null) {
            this.setText(shouldDo);
        }
    }

    /**
     * 关闭闹钟响铃
     */
    public void ringStop() {
        context.stopService(intent);
    }

    /**
     * 获取已完成分钟数
     *
     * @return
     */
    public int getAlreadyMin() {
        return (int) ((float) (mTime - mNextTime) / (float) 60);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        // 始终将控件置为“VISIBLE”，保证按home键和熄灭屏幕时，倒计时仍在继续
        visibility = VISIBLE;
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * 更新桌面时间显示
     */
    private void updateTimeText() {
        this.setText(mTimeFormat.format(new Date(mNextTime * 1000)));
    }

    interface OnTimeCompleteListener {
        void onTimeComplete();
    }
}
