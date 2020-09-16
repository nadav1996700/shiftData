package src.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
// glide
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class CommonUtils {

    private static CommonUtils instance;
    private static final int REQUEST_CODE = 101;
    private Context context;

    private CommonUtils(Context context) {
        this.context = context;
    }

    public static CommonUtils getInstance() {return instance;}

    public static CommonUtils initHelper(Context context) {
        if(instance == null)
            instance = new CommonUtils(context);
        return instance;
    }

    /* set images using glide library*/
    public void setImage(ImageView imageView, Drawable photo) {
        Glide.with(context)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
