package online.laoliang.simpletomato.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import online.laoliang.simpletomato.util.ContextHolder;

/**
 * Created by lpy on 17-12-3.
 */

public class RingtonePlayingService extends Service {

    Ringtone alarmMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //播放当前系统闹钟音乐
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        alarmMusic = RingtoneManager.getRingtone(ContextHolder.getContext(), notification);
        alarmMusic.play();

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String nonceStatus = sharedPreferences.getString("nonceStatus", "work_running");

        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();

        if(nonceStatus.equals("work_running")){
            editor.putString("nonceStatus", "work_over");
            editor.apply();
        }else{
            editor.putString("nonceStatus", "rest_over");
            editor.apply();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmMusic.stop();
    }
}
