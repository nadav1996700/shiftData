package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;

import src.Utils.My_images;

public class Activity_Entrance extends AppCompatActivity {

    private ImageView center_image;
    private Button sign_up;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        setValues();

        // set center image
        My_images.getInstance().downloadImage(center_image, "general_images/" +
                "icon_entarance.png");

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignIn.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignUp.class));
            }
        });
    }

    private void setValues() {
        sign_up = findViewById(R.id.entrance_BTN_signUp);
        sign_in = findViewById(R.id.entrance_BTN_signIn);
        center_image = findViewById(R.id.entrance_IMG_entrance);
    }
}