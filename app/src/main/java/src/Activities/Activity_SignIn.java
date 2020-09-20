package src.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

import src.Classes.Worker;
import src.Utils.My_Firebase;

public class Activity_SignIn extends AppCompatActivity {

    private ImageView signIn_image;
    private Spinner spinner;
    private EditText username;
    private EditText password;
    private Button sign_in;
    private ImageButton back;
    private TextView error_message;
    My_Firebase firebase = My_Firebase.getInstance();

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
                if (validateData()) {
                    // set company on firebase attribute
                    firebase.setCompany(spinner.getSelectedItem().toString());
                    // check if user details correct
                    checkDetails();
                }
            }
        });
    }

    /* check the fields data and raise error message if necessary */
    private boolean validateData() {
        if (username.getText().toString().trim().length() == 0) {
            error_message.setText("enter username");
            return false;
        } else if (password.getText().toString().trim().length() == 0) {
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

        // set spinner data
        setSpinnerData();
    }

    /* set spinner data */
    private void setSpinner(ArrayList<String> options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);
        if (options.size() > 0)
            //set selected value in spinner to first company
            spinner.setSelection(0);
        else
            spinner.setContentDescription("select company");
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
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> company_names = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.getValue().toString();
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
        error_message.setText("wrong username or password");
    }

    /* read all workers from firebase */
    public void checkIfUserIsWorker() {
        String path = "/" + firebase.getCompany() + "/workers";
        firebase.setReference(path);
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                            password, id, phone, company, Integer.valueOf(age), Integer.valueOf(photo)));
                }
                checkWorker(workers);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }
}
