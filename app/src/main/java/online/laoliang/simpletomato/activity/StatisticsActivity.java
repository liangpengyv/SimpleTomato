package online.laoliang.simpletomato.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import online.laoliang.simpletomato.R;
import online.laoliang.simpletomato.db.TomatoDatabase;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView todayGreatTomato;
    private TextView todayBadTomato;
    private TextView todayDurationHour;
    private TextView allGreatTomato;
    private TextView allBadTomato;
    private TextView allDurationHour;

    private TextView back;

    private void findView() {
        todayGreatTomato = (TextView) findViewById(R.id.today_great_tomato);
        todayBadTomato = (TextView) findViewById(R.id.today_bad_tomato);
        todayDurationHour = (TextView) findViewById(R.id.today_duration_hour);
        allGreatTomato = (TextView) findViewById(R.id.all_great_tomato);
        allBadTomato = (TextView) findViewById(R.id.all_bad_tomato);
        allDurationHour = (TextView) findViewById(R.id.all_duration_hour);

        back = (TextView) findViewById(R.id.back);
        back.setText("<- 统计");
        back.setOnClickListener(this);
    }

    private void initView() {
        TomatoDatabase tomatoDatabase = new TomatoDatabase(this);

        todayGreatTomato.setText(tomatoDatabase.queryTodaySuccessfulTomatoNumber() + "");
        todayBadTomato.setText(tomatoDatabase.queryTodayFailedTomatoNumber() + "");
        allGreatTomato.setText(tomatoDatabase.queryAllSuccessfulTomatoNumber() + "");
        allBadTomato.setText(tomatoDatabase.queryAllFailedTomatoNumber() + "");

        // 数据库得到分钟数，这里需要转换成小时，再展示
        String minToHourForToday = String.format("%.2f", ((float) tomatoDatabase.queryTodayDurationMin() / (float) 60));
        todayDurationHour.setText(minToHourForToday);
        String minToHourForAll = String.format("%.2f", ((float) tomatoDatabase.queryAllDurationMin() / (float) 60));
        allDurationHour.setText(minToHourForAll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        findView();
        initView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
        }
    }
}
