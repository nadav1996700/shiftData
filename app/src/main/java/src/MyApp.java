package src;

import android.app.Application;

import src.Utils.Utils;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Utils utils = Utils.initHelper(this);
    }
}
