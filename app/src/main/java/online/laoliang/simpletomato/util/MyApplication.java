package online.laoliang.simpletomato.util;

import android.app.Application;

/**
 * Created by lpy on 17-12-4.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 用于辅助全局获取获取Context对象
        ContextHolder.init(getApplicationContext());
    }
}
