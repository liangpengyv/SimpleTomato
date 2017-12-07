package online.laoliang.simpletomato.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import online.laoliang.simpletomato.model.Tomato;

/**
 * Created by lpy on 17/12/7.
 */

public class TomatoDatabase {

    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;

    public TomatoDatabase(Context context) {
        myDatabaseHelper = new MyDatabaseHelper(context, "SimpleTomato.db", null, 1);
        db = myDatabaseHelper.getWritableDatabase();
    }

    /**
     * 插入一条番茄数据
     *
     * @param tomato
     */
    public void insert(Tomato tomato) {
        String sql = "insert into Tomato (tomato_status, duration_min, nonce_timestamp) values (?, ?, ?)";
        String[] seats = new String[]{tomato.getTomatoStatus(), tomato.getDurationMin() + "", tomato.getNonceTimestamp() + ""};
        db.execSQL(sql, seats);
    }

    /**
     * 获取全部已达成番茄数
     *
     * @return
     */
    public int queryAllSuccessfulTomatoNumber() {
        String sql = "select * from Tomato where tomato_status='Great'";
        Cursor cursor = db.rawQuery(sql, null);
        int result = cursor.getCount();
        return result;
    }

    /**
     * 获取全部失败番茄数
     *
     * @return
     */
    public int queryAllFailedTomatoNumber() {
        String sql = "select * from Tomato where tomato_status='Bad'";
        Cursor cursor = db.rawQuery(sql, null);
        int result = cursor.getCount();
        return result;
    }

    /**
     * 获取全部工作时间分钟数
     *
     * @return
     */
    public int queryAllDurationMin() {
        String sql = "select sum(duration_min) as resultSum from Tomato";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("resultSum"));
        return result;
    }

    /**
     * 获取今日已达成番茄数
     *
     * @return
     */
    public int queryTodaySuccessfulTomatoNumber() {
        String sql = "select * from Tomato where tomato_status='Great' and nonce_timestamp>?";
        String[] seats = new String[]{getTimeMorning() + ""};
        Cursor cursor = db.rawQuery(sql, seats);
        int result = cursor.getCount();
        return result;
    }

    /**
     * 获取今日失败番茄数
     *
     * @return
     */
    public int queryTodayFailedTomatoNumber() {
        String sql = "select * from Tomato where tomato_status='Bad' and nonce_timestamp>?";
        String[] seats = new String[]{getTimeMorning() + ""};
        Cursor cursor = db.rawQuery(sql, seats);
        int result = cursor.getCount();
        return result;
    }

    /**
     * 获取今日工作时间分钟数
     *
     * @return
     */
    public int queryTodayDurationMin() {
        String sql = "select sum(duration_min) as resultSum from Tomato where nonce_timestamp>?";
        String[] seats = new String[]{getTimeMorning() + ""};
        Cursor cursor = db.rawQuery(sql, seats);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex("resultSum"));
        return result;
    }

    /**
     * 获取当天0点时间的时间戳
     *
     * @return
     */
    private long getTimeMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
}
