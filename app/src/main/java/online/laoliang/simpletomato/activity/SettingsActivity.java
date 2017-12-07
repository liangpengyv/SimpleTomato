package online.laoliang.simpletomato.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import online.laoliang.simpletomato.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button setWorkDuration;
    private Button setRestDuration;
    private SeekBar chooseWorkDuration;
    private SeekBar chooseRestDuration;
    private TextView showNonceWorkDuration;
    private TextView showNonceRestDuration;

    private TextView back;

    final private int seekBarWorkMinNum = 1;
    final private int seekBarRestMinNum = 1;

    private void findView() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        setWorkDuration = (Button) findViewById(R.id.set_work_duration);
        setWorkDuration.setOnClickListener(this);
        setRestDuration = (Button) findViewById(R.id.set_rest_duration);
        setRestDuration.setOnClickListener(this);
        chooseWorkDuration = (SeekBar) findViewById(R.id.choose_work_duration);
        chooseWorkDuration.setOnSeekBarChangeListener(this);
        chooseRestDuration = (SeekBar) findViewById(R.id.choose_rest_duration);
        chooseRestDuration.setOnSeekBarChangeListener(this);
        showNonceWorkDuration = (TextView) findViewById(R.id.show_nonce_work_duration);
        showNonceWorkDuration.setText(sharedPreferences.getInt("workDuration", 25) + "");
        showNonceRestDuration = (TextView) findViewById(R.id.show_nonce_rest_duration);
        showNonceRestDuration.setText(sharedPreferences.getInt("restDuration", 5) + "");

        back = (TextView) findViewById(R.id.back);
        back.setText("<- 设置");
        back.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findView();
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        int id = view.getId();
        switch (id) {
            case R.id.set_work_duration:
                if (chooseWorkDuration.getVisibility() == View.GONE) {
                    chooseWorkDuration.setVisibility(View.VISIBLE);
                    int workDuration = sharedPreferences.getInt("workDuration", 25);
                    chooseWorkDuration.setProgress(workDuration - seekBarWorkMinNum);
                } else {
                    chooseWorkDuration.setVisibility(View.GONE);
                }
                break;
            case R.id.set_rest_duration:
                if (chooseRestDuration.getVisibility() == View.GONE) {
                    chooseRestDuration.setVisibility(View.VISIBLE);
                    int restDuration = sharedPreferences.getInt("restDuration", 5);
                    chooseRestDuration.setProgress(restDuration - seekBarRestMinNum);
                } else {
                    chooseRestDuration.setVisibility(View.GONE);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int id = seekBar.getId();
        switch (id) {
            case R.id.choose_work_duration:
                i += seekBarWorkMinNum;
                showNonceWorkDuration.setText(i + "");
                break;
            case R.id.choose_rest_duration:
                i += seekBarRestMinNum;
                showNonceRestDuration.setText(i + "");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        int id = seekBar.getId();
        switch (id) {
            case R.id.choose_work_duration:
                int nonceWorkProgress = seekBar.getProgress() + seekBarWorkMinNum;
                editor.putInt("workDuration", nonceWorkProgress);
                editor.apply();
                break;
            case R.id.choose_rest_duration:
                int nonceRestProgress = seekBar.getProgress() + seekBarRestMinNum;
                editor.putInt("restDuration", nonceRestProgress);
                editor.apply();
                break;
        }
    }
}
