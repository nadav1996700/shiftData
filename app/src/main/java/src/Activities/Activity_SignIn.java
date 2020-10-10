package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Activity_SignIn extends AppCompatActivity {

    private AutoCompleteTextView spinner;
    private EditText username;
    private EditText password;
    private Button sign_in;
    private Button new_company;
    private TextView error_message;
    My_Firebase firebase = My_Firebase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        setValues();

        new_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_SignIn.this, Activity_SignUp.class));
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    // set company on firebase attribute
                    firebase.setCompany(spinner.getText().toString());
                    // check first if user is Manager, else - check if he is Worker
                    checkDetails();
                }
            }
        });
    }

    /* check the fields data and raise error message if necessary */
    private boolean validateData() {
        if (username.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_username);
            return false;
        } else if (password.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_password);
            return false;
        }
        error_message.setText("");
        return true;
    }

    private void setValues() {
        spinner = findViewById(R.id.signIn_SPN_business);
        username = findViewById(R.id.signIn_EDT_username);
        password = findViewById(R.id.signIn_EDT_password);
        sign_in = findViewById(R.id.signIn_BTN_signIn);
        error_message = findViewById(R.id.signIn_LBL_error);
        new_company = findViewById(R.id.signIn_BTN_register);

        // set spinner data
        setSpinnerData();
    }

    /* set spinner data */
    private void setSpinner(ArrayList<String> options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);
    }

    /* check if user exist in firebase */
    private void checkDetails() {
        firebase.setReference("/" + firebase.getCompany());
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String db_username = Objects.requireNonNull(snapshot.child("username").
                        getValue()).toString();
                if (!db_username.equals(username.getText().toString()))
                    checkIfUserIsWorker();
                else {
                    String db_password = Objects.requireNonNull(snapshot.child("password").
                            getValue()).toString();
                    if (!db_password.equals(password.getText().toString()))
                        checkIfUserIsWorker();
                    else {
                        // get in to main screen of manager - Activity Manager
                        startActivity(new Intent(Activity_SignIn.this, Activity_Manager.class));
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /* check if username and password match */
    private boolean checkWorker(String mUsername, String mPassword) {
        return mUsername.equals(username.getText().toString()) &&
                mPassword.equals(password.getText().toString());
    }

    /* read all company names from firebase to spinner */
    private void setSpinnerData() {
        firebase.setReference("/company_names");
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> company_names = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.getKey();
                    company_names.add(name);
                }
                setSpinner(company_names);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    /* sign in as worker */
    private void userIsWorker(String id) {
        firebase.setWorker_id(id);
        startActivity(new Intent(Activity_SignIn.this, Activity_Worker.class));
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }

    /* set error message if login fail */
    private void setError_message() {
        error_message.setText(R.string.error_message);
    }

    /* read username and password until find match */
    public void checkIfUserIsWorker() {
        firebase.setReference("/" + firebase.getCompany() + "/workers");
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0; // set flag in order to know if the user succeeded to sign in
                String username, password, id;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    id = Objects.requireNonNull(child.child("id").getValue()).toString();
                    username = Objects.requireNonNull(child.child("username").getValue()).toString();
                    password = Objects.requireNonNull(child.child("password").getValue()).toString();
                    if (checkWorker(username, password)) {
                        userIsWorker(id);
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0)
                    // failed to commit login
                    setError_message();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }
}
