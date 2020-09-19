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
    private boolean userIsManager = false;

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
                if(validateData()) {
                    // set company on firebase attribute
                    My_Firebase.getInstance().setCompany(spinner.getSelectedItem().toString());
                    // check if user is manager
                    checkIfUserIsManager();
                    if(userIsManager) {
                        // get in to main screen of manager
                        startActivity(new Intent(Activity_SignIn.this, Activity_Shifts.class));
                    }
                    // check if user is worker
                    String id = checkIfUserIsWorker();
                    if(!id.equals("0")) {
                        // get into main screen of worker, put extra - id
                        Intent intent = new Intent(Activity_SignIn.this, Activity_Shifts.class);
                        intent.putExtra(Activity_Shifts.EXTRA_ID, id);
                        startActivity(intent);
                    }
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

        // set spinner data
        setSpinner();
    }

    /* set spinner data */
    private void setSpinner() {
        ArrayList<String> options = My_Firebase.getInstance().readCompanyNames();
        Log.d("pttt", options.toString());
       // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,options);
        //spinner.setAdapter(adapter);
        //set selected value in spinner to first company
        //spinner.setSelection(0);
    }

    /* check if user exist in firebase */
    private void checkIfUserIsManager() {
        String company = spinner.getSelectedItem().toString();
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setReference("/" + company + "/");
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String db_username = snapshot.child("username").getValue().toString();
                if(db_username.equals(username.toString())) {
                    String db_password = snapshot.child("password").getValue().toString();
                    if(db_password.equals(password.toString())) {
                        userIsManager = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // check if the user is instance of Worker and return his id or 0 if it is not Worker
    private String checkIfUserIsWorker() {
        ArrayList<Worker> workers = My_Firebase.getInstance().readWorkers();
        for(Worker worker: workers) {
            if(worker.getUsername().equals(username) && worker.getPassword().equals(password)) {
                return worker.getId();
            }
        }
        return "0";
    }
}
