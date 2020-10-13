package src.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.src.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import src.Utils.My_images;

public class Activity_Entrance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        if (checkInternetConnection()) {
            // set center image
            setCenterImage();
            // initialize variables
            setValues();
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

    private void setValues() {
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation buttomAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        ImageView centerImage = findViewById(R.id.entrance_IMG_entrance);
        TextView title = findViewById(R.id.entrance_LBL_title);
        centerImage.setAnimation(topAnim);
        title.setAnimation(buttomAnim);
    }

    private void setCenterImage() {
        My_images images = My_images.initHelper(this);
        images.setPlaceholder(R.id.entrance_IMG_entrance);
        images.setImage(ContextCompat.getDrawable(this, R.drawable.calendar_entrance_icon));
    }

    private boolean checkInternetConnection() {
        // check connection to internet
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            internet_dialog();
            return false;
        }
        return true;
    }

    private void internet_dialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(getResources().getString(R.string.internet_dialog_title));
        dialog.setMessage(getResources().getString(R.string.internet_supporting_text));
        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Respond to positive button press
                finish();
            }
        });
        dialog.show();
    }
}