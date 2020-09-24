package src;

import android.app.Application;

import src.Utils.My_Firebase;
import src.Utils.My_images;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        My_Firebase firebase = My_Firebase.initHelper();
    }
}
