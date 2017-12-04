package online.laoliang.simpletomato.util;

import android.app.Application;

/**
 * Created by lpy on 17-12-4.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.init(getApplicationContext());
    }

}
