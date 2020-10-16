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
    private MaterialAlertDialogBuilder dialog;
    private ImageView centerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        if (checkInternetConnection()) {
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

    private void setValues() {
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation buttomAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        centerImage = findViewById(R.id.entrance_IMG_entrance);
        TextView title = findViewById(R.id.entrance_LBL_title);
        centerImage.setAnimation(topAnim);
        title.setAnimation(buttomAnim);
    }

    private void setCenterImage() {
        My_images images = My_images.getInstance();
        images.setImage(ContextCompat.getDrawable(this, R.drawable.calendar_entrance_icon), centerImage);
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
        dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(getResources().getString(R.string.internet_dialog_title));
        dialog.setMessage(getResources().getString(R.string.internet_supporting_text));
        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Respond to positive button press
                finish();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        dialog.show();
    }
}

