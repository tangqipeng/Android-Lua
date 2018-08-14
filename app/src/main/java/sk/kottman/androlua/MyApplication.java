package sk.kottman.androlua;

import android.app.Application;
import android.content.Context;

/**
 * Created by tangqipeng
 * 2018/8/13
 * email: tangqipeng@aograph.com
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
