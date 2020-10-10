package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;

import java.util.Timer;
import java.util.TimerTask;

import src.Utils.My_images;

public class Activity_Entrance extends AppCompatActivity {
    private final int DELAY = 5000;
    private ImageView imageView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        // set center image
        setCenterImage();

        // wait DELAY time before enter the app
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignIn.class));
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                finish();
            }
        }, DELAY);
    }

    private void setCenterImage() {
        imageView = findViewById(R.id.entrance_IMG_entrance);
        My_images images = My_images.initHelper(this);
        String path = "gs://shiftdata-a19a0.appspot.com/general_images/" +
                "icon_entarance.png";
        images.downloadImageUrl(path, imageView);
    }
}