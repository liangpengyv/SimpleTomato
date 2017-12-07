package online.laoliang.simpletomato.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import online.laoliang.simpletomato.R;
import online.laoliang.simpletomato.db.TomatoDatabase;
import online.laoliang.simpletomato.model.Tomato;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }
}
