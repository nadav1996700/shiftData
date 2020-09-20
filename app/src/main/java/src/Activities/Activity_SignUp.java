package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;

import src.Classes.Company;
import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Activity_SignUp extends AppCompatActivity {

    private EditText company_name;
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


        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validateData()) {
                    Company company = new Company(company_name.getText().toString()
                            , username.getText().toString(), password.getText().toString());
                    // upload to firebase, and open login screen
                    My_Firebase.getInstance().addCompany(company);
                    startActivity(new Intent(Activity_SignUp.this, Activity_SignIn.class));
                }
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_SignUp.this, Activity_SignIn.class));
            }
        });
    }

    private void setValues() {
        company_name = findViewById(R.id.signUp_EDT_business);
        username = findViewById(R.id.signUp_EDT_username);
        password = findViewById(R.id.signUp_EDT_password);
        sign_in = findViewById(R.id.signUp_LBL_sign_in);
        sign_up = findViewById(R.id.signUp_BTN_signUp);
        error_message = findViewById(R.id.signUp_LBL_error);

        // set icon
        setImage();
    }

    private void setImage() {
        My_images images = My_images.initHelper(this);
        images.setPlaceholder(R.id.signUp_IMG_sign_up);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/general_images/" +
                "register_icon.png");
    }

    /* check the fields data and raise error message if necessary */
    private boolean validateData() {
        if (company_name.getText().toString().equals("")) {
            error_message.setText("select Business!");
            return false;
        } else if (username.getText().toString().trim().equals("")) {
            error_message.setText("enter username");
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            error_message.setText("enter password");
            return false;
        }
        return true;
    }
}
