package src.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import src.Activities.Activity_SignIn;

public class My_images {
    private static My_images instance;
    private Activity activity;
    private int placeholder;

    private My_images(Activity activity) {
        this.activity = activity;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public static My_images getInstance() {
        return instance;
    }

    public static My_images initHelper(Activity activity) {
        if (instance == null)
            instance = new My_images(activity);
        return instance;
    }

    public void downloadImage(String ref) {
        // set storage reference
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference = My_Firebase.getInstance().getStorage_reference();
        final long ONE_MEGABYTE = 1024 * 1024;
        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Drawable drawable = new BitmapDrawable(activity.getResources(), BitmapFactory
                        .decodeByteArray(bytes, 0, bytes.length));
                setImage(placeholder, drawable);
            }
        });
    }

    public void uploadImage(ImageView imageView, String ref) {
        // set storage reference
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference = My_Firebase.getInstance().getStorage_reference();
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
    public void setImage(int placeholder, Drawable photo) {
        ImageView imageView = this.activity.findViewById(placeholder);
        Log.d("pttt", "imageView = " + imageView.toString());
        Glide.with(activity)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
