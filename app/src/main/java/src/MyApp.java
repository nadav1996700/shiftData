package src;

import android.app.Application;

import src.Utils.CommonUtils;
import src.Utils.My_Firebase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CommonUtils commonUtils = CommonUtils.initHelper(this);
        My_Firebase firebase = My_Firebase.initHelper();
    }
}
