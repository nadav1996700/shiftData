package src.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;

import src.Utils.CommonUtils;
import src.Utils.My_Firebase;

public class Activity_SignIn extends AppCompatActivity {

    private ImageView signIn_image;
    private Spinner spinner;
    private EditText username;
    private EditText password;
    private Button sign_in;
    private ImageButton back;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        setValues();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_SignIn.this, Activity_SignUp.class));
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData() && My_Firebase.getInstance().userExist(spinner.getSelectedItem().toString(),
                        username.getText().toString().trim(), password.getText().toString().trim())) {
                    // create Business/Worker object and send it to next page
                    // set company on firebase attribute
                    My_Firebase.getInstance().setCompany(spinner.getSelectedItem().toString());
                }
            }
        });

    }

    /* check the fields data and raise error message if necessary */
    private boolean validateData() {
        if(spinner.getSelectedItem().toString().equals("")) {
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

    private void setValues() {
        signIn_image = findViewById(R.id.signIn_IMG_sign_in);
        spinner = findViewById(R.id.signIn_SPN_business);
        username = findViewById(R.id.signIn_EDT_username);
        password = findViewById(R.id.signIn_EDT_password);
        sign_in = findViewById(R.id.signIn_BTN_signIn);
        error_message = findViewById(R.id.signIn_LBL_error);
        back = findViewById(R.id.signIn_BTN_back);
    }
}
