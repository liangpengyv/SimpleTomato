package online.laoliang.simpletomato.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import online.laoliang.simpletomato.R;
import online.laoliang.simpletomato.db.TomatoDatabase;
import online.laoliang.simpletomato.model.Tomato;
import online.laoliang.simpletomato.util.Anticlockwise;
import online.laoliang.simpletomato.util.CircleProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int DEFAULT_WORK_DURATION = 25;
    private final int DEFAULT_REST_DURATION = 5;

    private Anticlockwise countdown;
    public static CircleProgressBar mainCircle;
    private Button buttonSettings;
    private Button buttonStatistics;

    private Intent intent;

    private TomatoDatabase tomatoDatabase;

    private Tomato tomato;

    private void findView() {
        countdown = (Anticlockwise) findViewById(R.id.anticlockwise_count_down);
        mainCircle = (CircleProgressBar) findViewById(R.id.main_circle);
        mainCircle.setOnClickListener(this);
        buttonSettings = (Button) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(this);
        buttonStatistics = (Button) findViewById(R.id.button_statistics);
        buttonStatistics.setOnClickListener(this);
    }

    private void initView() {
        //当主活动被重新创建时，将闹钟状态重置为work_waiting
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("nonceStatus", "work_waiting");
        editor.apply();
        //同时将闹钟主按钮重置为初始状态
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        int workDuration = sharedPreferences.getInt("workDuration", DEFAULT_WORK_DURATION);
        countdown.initTime(workDuration, "开始");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    /**
     * 按钮点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_circle:
                mainButton();
                break;
            case R.id.button_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.button_statistics:
                intent = new Intent(this, StatisticsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 不同状态下，点击闹钟中间主按钮时，执行不同的动作
     */
    private void mainButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String nonceStatus = sharedPreferences.getString("nonceStatus", "work_waiting");
        final int workDuration = sharedPreferences.getInt("workDuration", DEFAULT_WORK_DURATION);
        int restDuration = sharedPreferences.getInt("restDuration", DEFAULT_REST_DURATION);

        final SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();

        switch (nonceStatus) {
            case "work_waiting":
                countdown.initTime(workDuration, null);
                countdown.start();

                buttonSettings.setVisibility(View.INVISIBLE);
                buttonStatistics.setVisibility(View.INVISIBLE);

                editor.putString("nonceStatus", "work_running");
                editor.apply();
                break;
            case "work_running":
                countdown.stop();
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("暂停").setMessage("要放弃这个番茄吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // 此处烂番茄+1
                        tomato = new Tomato();
                        tomato.setTomatoStatus("Bad");
                        tomato.setDurationMin(countdown.getAlreadyMin());
                        tomato.setNonceTimestamp(System.currentTimeMillis());
                        tomatoDatabase = new TomatoDatabase(MainActivity.this);
                        tomatoDatabase.insert(tomato);

                        countdown.initTime(workDuration, "开始");
                        buttonSettings.setVisibility(View.VISIBLE);
                        buttonStatistics.setVisibility(View.VISIBLE);
                        mainCircle.setProgress(0);

                        editor.putString("nonceStatus", "work_waiting");
                        editor.apply();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.start();
                    }
                }).create().show();
                break;
            case "work_over":
                //此处好番茄+1
                tomato = new Tomato();
                tomato.setTomatoStatus("Great");
                tomato.setDurationMin(countdown.getAlreadyMin());
                tomato.setNonceTimestamp(System.currentTimeMillis());
                tomatoDatabase = new TomatoDatabase(MainActivity.this);
                tomatoDatabase.insert(tomato);
                Toast.makeText(MainActivity.this, "一个番茄已达成！", Toast.LENGTH_SHORT).show();

                countdown.ringStop();
                countdown.initTime(restDuration, "休息");
                buttonSettings.setVisibility(View.VISIBLE);
                buttonStatistics.setVisibility(View.VISIBLE);
                mainCircle.setProgress(0);

                editor.putString("nonceStatus", "rest_waiting");
                editor.apply();
                break;
            case "rest_waiting":
                countdown.initTime(restDuration, null);
                countdown.start();

                buttonSettings.setVisibility(View.INVISIBLE);
                buttonStatistics.setVisibility(View.INVISIBLE);

                editor.putString("nonceStatus", "rest_running");
                editor.apply();
                break;
            case "rest_running":
                countdown.stop();
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("暂停").setMessage("结束本次休息").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.initTime(workDuration, "开始");
                        buttonSettings.setVisibility(View.VISIBLE);
                        buttonStatistics.setVisibility(View.VISIBLE);
                        mainCircle.setProgress(0);

                        editor.putString("nonceStatus", "work_waiting");
                        editor.apply();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.start();
                    }
                }).create().show();
                break;
            case "rest_over":
                countdown.ringStop();
                countdown.initTime(workDuration, "开始");
                buttonSettings.setVisibility(View.VISIBLE);
                buttonStatistics.setVisibility(View.VISIBLE);
                mainCircle.setProgress(0);

                editor.putString("nonceStatus", "work_waiting");
                editor.apply();
                break;
        }
    }

    /**
     * 针对闹钟不同状态，重写BACK键功能
     */
    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String nonceStatus = sharedPreferences.getString("nonceStatus", "work_waiting");

        switch (nonceStatus) {
            case "work_waiting":
                super.onBackPressed();
                break;
            case "work_running":
                countdown.stop();
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("暂停").setMessage("要放弃这个番茄吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //此处烂番茄+1
                        tomato = new Tomato();
                        tomato.setTomatoStatus("Bad");
                        tomato.setDurationMin(countdown.getAlreadyMin());
                        tomato.setNonceTimestamp(System.currentTimeMillis());
                        tomatoDatabase = new TomatoDatabase(MainActivity.this);
                        tomatoDatabase.insert(tomato);

                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.start();
                    }
                }).create().show();
                break;
            case "work_over":
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("退出").setMessage("要关闭响铃并退出应用吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.ringStop();

                        //此处好番茄+1
                        tomato = new Tomato();
                        tomato.setTomatoStatus("Great");
                        tomato.setDurationMin(countdown.getAlreadyMin());
                        tomato.setNonceTimestamp(System.currentTimeMillis());
                        tomatoDatabase = new TomatoDatabase(MainActivity.this);
                        tomatoDatabase.insert(tomato);
                        Toast.makeText(MainActivity.this, "一个番茄已达成！", Toast.LENGTH_SHORT).show();

                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
                break;
            case "rest_waiting":
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("退出").setMessage("退出应用？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
                break;
            case "rest_running":
                countdown.stop();
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("暂停").setMessage("要结束休息并退出应用吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.start();
                    }
                }).create().show();
                break;
            case "rest_over":
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert).setCancelable(false).setTitle("退出").setMessage("要关闭响铃并退出应用吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countdown.ringStop();

                        MainActivity.super.onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
                break;
        }
    }
}
