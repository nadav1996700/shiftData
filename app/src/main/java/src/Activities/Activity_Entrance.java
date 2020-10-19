package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.src.R;

import src.Utils.Common_utils;
import src.Utils.My_images;

public class Activity_Entrance extends AppCompatActivity {
    private ImageView centerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        if (Common_utils.getInstance().checkInternetConnection(this)) {
            // initialize variables
            setValues();
            // set center image
            setCenterImage();
            // wait DELAY time before enter the app
            final int DELAY = 5000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Activity_Entrance.this, Activity_SignIn.class));
                    finish();
                }
            }, DELAY);
        }
    }

    /* initialize variables */
    private void setValues() {
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        centerImage = findViewById(R.id.entrance_IMG_entrance);
        TextView title = findViewById(R.id.entrance_LBL_title);
        centerImage.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
    }

    /* load image to the center of the screen */
    private void setCenterImage() {
        My_images images = My_images.getInstance();
        images.setImage(ContextCompat.getDrawable(this, R.drawable.calendar_entrance_icon), centerImage);
    }
}

