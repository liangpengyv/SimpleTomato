package online.laoliang.simpletomato.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import online.laoliang.simpletomato.util.ContextHolder;

/**
 * Created by lpy on 17-12-3.
 */

public class RingtonePlayingService extends Service {

    private Ringtone alarmMusic;

    // 电源管理唤醒锁对象
    private PowerManager.WakeLock wakeLock;
    // 电源管理对象
    private PowerManager powerManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 播放当前系统闹钟音乐
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        alarmMusic = RingtoneManager.getRingtone(ContextHolder.getContext(), notification);
        alarmMusic.play();

        // 获取系统电源管理对象
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 这个target用于高速系统是哪个app在控制屏幕，并制定对屏幕的控制级别
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "target");
        // 点亮屏幕
        wakeLock.acquire();

        // 更新当前番茄状态
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String nonceStatus = sharedPreferences.getString("nonceStatus", "work_running");
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        if (nonceStatus.equals("work_running")) {
            editor.putString("nonceStatus", "work_over");
            editor.apply();
        } else {
            editor.putString("nonceStatus", "rest_over");
            editor.apply();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 关闭闹钟音乐
        alarmMusic.stop();

        // 关闭强制亮屏锁定
        wakeLock.release();
    }
}
