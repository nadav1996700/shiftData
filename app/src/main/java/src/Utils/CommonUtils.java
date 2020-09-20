package src.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
// glide
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

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
}
