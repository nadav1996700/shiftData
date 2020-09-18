package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;

public class Activity_SignUp extends AppCompatActivity {

    private ImageView signUp_image;
    private EditText business_name;
    private EditText username;
    private EditText password;
    private Button sign_up;
    private TextView sign_in;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setValues();

        /*
        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(validateData()) {
                    Business business = new Business(business_name.getText().toString()
                            , username.getText().toString(), password.getText().toString());
                    // upload to firebase, and open login screen
                }
            }
        });
        */

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_SignUp.this, Activity_SignIn.class));
            }
        });
    }

    private void setValues() {
        signUp_image = findViewById(R.id.signUp_IMG_sign_up);
        business_name = findViewById(R.id.signUp_EDT_business);
        username = findViewById(R.id.signUp_EDT_username);
        password = findViewById(R.id.signUp_EDT_password);
        sign_in = findViewById(R.id.signUp_LBL_sign_in);
        sign_up = findViewById(R.id.signUp_BTN_signUp);
        error_message = findViewById(R.id.signUp_LBL_error);
    }

    /* check the fields data and raise error message if necessary */
    private boolean validateData() {
        if(business_name.getText().toString().equals("")) {
            error_message.setText("select Business!");
            return false;
        } else if(username.getText().toString().trim().equals("")) {
            error_message.setText("enter username");
            return false;
        } else if(password.getText().toString().trim().equals("")) {
            error_message.setText("enter password");
            return false;
        }
        return true;
    }
}
