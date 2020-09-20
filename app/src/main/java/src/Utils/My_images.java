package src.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class My_images {
    private static My_images instance;
    private Context context;

    private My_images(Context context) {this.context = context;}

    public static My_images getInstance() {return instance;}

    public static My_images initHelper(Context context) {
        if(instance == null)
            instance = new My_images(context);
        return instance;
    }

    public void downloadImage(ImageView imageView, String ref) {
        // set storage reference
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference =  My_Firebase.getInstance().getStorage_reference();
        final long ONE_MEGABYTE = 1024 * 1024;
        byte[] bytes = reference.getBytes(ONE_MEGABYTE).getResult();
        // convert bytes to drawable
        Drawable drawable = new BitmapDrawable(context.getResources()
                , BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        setImage(imageView, drawable);
    }

    public void uploadImage(ImageView imageView, String ref) {
        // set storage reference
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference =  My_Firebase.getInstance().getStorage_reference();
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = reference.putBytes(data);
    }

    /* set images using glide library*/
    public void setImage(ImageView imageView, Drawable photo) {
        Glide.with(context)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
