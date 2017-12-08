package online.laoliang.simpletomato.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lpy on 17/12/7.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 创建Tomato表
     */
    public static final String CREATE_TOMATO = "create table Tomato( "
            + "id integer primary key autoincrement, "
            + "tomato_status text, "
            + "duration_min integer, "
            + "nonce_timestamp timestamp )";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TOMATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
