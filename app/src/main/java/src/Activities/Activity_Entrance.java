package src.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.src.R;

import src.Utils.Utils;

public class Activity_Entrance extends AppCompatActivity {

    private ImageView center_image;
    private Button sign_up;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        setValues();
    }

    private void setValues() {
        sign_up = findViewById(R.id.entrance_BTN_signUp);
        sign_in = findViewById(R.id.entrance_BTN_signIn);
        center_image = findViewById(R.id.entrance_IMG_entrance);
        //Utils.getInstance().setImage(center_image, );
    }
}