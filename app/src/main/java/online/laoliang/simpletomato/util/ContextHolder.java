package online.laoliang.simpletomato.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by lpy on 17-12-3.
 */

public class ContextHolder {

    private static Context ApplicationContext;

    /**
     * 初始化context，如果由于不同机型导致反射获取context失败可以在Application调用此方法
     *
     * @param context
     */
    public static void init(Context context) {
        ApplicationContext = context;
    }

    /**
     * 全局获取Context对象
     *
     * @return
     */
    public static Context getContext() {
        if (ApplicationContext == null) {
            try {
                Application application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Application application = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
        }
        return ApplicationContext;
    }

}
