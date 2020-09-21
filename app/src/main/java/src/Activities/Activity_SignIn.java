package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import src.Classes.Company;
import src.Classes.Worker;
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
                finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    // set company on firebase attribute
                    firebase.setCompany(spinner.getText().toString());
                    // check if user details correct
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
        // set icon
        setImage();
    }

    private void setImage() {
        My_images images = My_images.getInstance();
        images.setActivity(Activity_SignIn.this);
        images.setPlaceholder(R.id.signIn_IMG_sign_in);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/general_images/" +
                "login_icon.png");
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
                String db_username = snapshot.child("username").getValue().toString();
                if (!db_username.equals(username.getText().toString()))
                    checkIfUserIsWorker();
                else {
                    String db_password = snapshot.child("password").getValue().toString();
                    if (!db_password.equals(password.getText().toString()))
                        checkIfUserIsWorker();
                    else {
                        // get in to main screen of manager
                        startActivity(new Intent(Activity_SignIn.this, Activity_Shifts.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // check if the user is instance of Worker and return his id or 0 if it is not Worker
    private void checkWorker(ArrayList<Worker> workers) {
        for (Worker worker : workers) {
            if (worker.getUsername().equals(username.getText().toString()) &&
                    worker.getPassword().equals(password.getText().toString())) {
                // sign in as worker
                userIsWorker(worker.getId());
                return;
            }
        }
        // failed to commit login
        setError_message();
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
            public void onCancelled(DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    /* sign in as worker */
    private void userIsWorker(String id) {
        Intent intent = new Intent(Activity_SignIn.this, Activity_Shifts.class);
        intent.putExtra(Activity_Shifts.EXTRA_ID, id);
        startActivity(intent);
    }

    /* set error message if login fail */
    private void setError_message() {
        error_message.setText(R.string.error_message);
    }

    /* read all workers from firebase */
    public void checkIfUserIsWorker() {
        String path = "/" + firebase.getCompany() + "/workers";
        firebase.setReference(path);
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Worker> workers = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String first_name = child.child("first_name").getValue().toString();
                    String last_name = child.child("last_name").getValue().toString();
                    String username = child.child("username").getValue().toString();
                    String password = child.child("password").getValue().toString();
                    String id = child.child("id").getValue().toString();
                    String phone = child.child("phone").getValue().toString();
                    String age = child.child("age").getValue().toString();
                    String company = child.child("company").getValue().toString();
                    String photo = child.child("photo").getValue().toString();
                    // create Worker
                    workers.add(new Worker(first_name, last_name, username,
                            password, id, phone, company, Integer.parseInt(age), Integer.parseInt(photo)));
                }
                checkWorker(workers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }
}
