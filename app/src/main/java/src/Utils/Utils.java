package src.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
// glide
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Utils {

    private static Utils instance;
    private static final int REQUEST_CODE = 101;
    private Context context;

    private Utils(Context context) {
        this.context = context;
    }

    public static Utils getInstance() {return instance;}

    public static Utils initHelper(Context context) {
        if(instance == null)
            instance = new Utils(context);
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
