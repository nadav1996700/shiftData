package src;

import android.app.Application;

import src.Utils.CommonUtils;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CommonUtils commonUtils = CommonUtils.initHelper(this);
    }
}
