package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;

import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Activity_Entrance extends AppCompatActivity {

    private Button sign_up;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        setValues();

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignIn.class));
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignUp.class));
                finish();
            }
        });
    }

    private void setValues() {
        sign_up = findViewById(R.id.entrance_BTN_signUp);
        sign_in = findViewById(R.id.entrance_BTN_signIn);
        // set center image
        setCenterImage();
    }

    private void setCenterImage() {
        My_images images = My_images.initHelper(this);
        images.setPlaceholder(R.id.entrance_IMG_entrance);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/general_images/" +
                "icon_entarance.png");
    }
}