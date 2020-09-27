package src.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.src.R;
import com.google.android.material.navigation.NavigationView;

import src.Utils.My_Firebase;
import src.fragments.Fragment_Requestss;
import src.fragments.Fragment_calender;

public class Activity_Menu extends AppCompatActivity {

    private Fragment_Requestss fragment_requestss;
    private NavigationView navigationView;
    private View header;
    private TextView name;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setValues();
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setWorker_id("207896785");
        firebase.setCompany("benedict");

        name.setText("Nadav Cohen");
        id.setText("345267865");
        fragment_requestss = new Fragment_Requestss();
        initFragment(fragment_requestss, R.id.Menu_LAY_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("pttt", "inside onOption method");
        switch (item.getItemId()) {
            case R.id.Menu_watch_shifts:
                startActivity(new Intent(Activity_Menu.this, Activity_Shifts.class));
                return true;
            case R.id.Menu_submit_shifts:
                startActivity(new Intent(Activity_Menu.this, Activity_Requests.class));
                return true;
            case R.id.Menu_My_profile:
                Log.d("pttt", "inside my profile option");
                Fragment_calender fragment_calender = new Fragment_calender();
                initFragment(fragment_calender, R.id.Menu_LAY_fragment);
                //startActivity(new Intent(Activity_Menu.this, Activity_Profile.class));
                return true;
            case R.id.Menu_log_out:
                startActivity(new Intent(Activity_Menu.this, Activity_SignIn.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setValues() {
        navigationView = findViewById(R.id.Menu_NAV_menu);
        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.Header_LBL_name);
        id = header.findViewById(R.id.Header_LBL_id);
    }

    private void initFragment(Fragment fragment, int layout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    /*
    private void setMenuListener() {
        NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
    }
     */
}