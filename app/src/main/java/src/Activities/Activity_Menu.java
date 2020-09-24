package src.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.src.R;
import com.google.android.material.navigation.NavigationView;

public class Activity_Menu extends AppCompatActivity {

    private NavigationView navigationView;
    private View header;
    private TextView name;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setValues();

        name.setText("Nadav Cohen");
        id.setText("345267865");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_watch_shifts:
                startActivity(new Intent(Activity_Menu.this, Activity_Shifts.class));
                return true;
            case R.id.Menu_submit_shifts:
                startActivity(new Intent(Activity_Menu.this, Activity_Requests.class));
                return true;
            case R.id.Menu_My_profile:
                startActivity(new Intent(Activity_Menu.this, Activity_Profile.class));
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
}