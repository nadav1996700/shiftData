package src.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.src.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import src.Utils.My_Firebase;
import src.Utils.My_images;
import src.fragments.Fragment_Requestss;
import src.fragments.Fragment_calender;
import src.fragments.Fragment_requests;
import src.fragments.Fragment_shifts;

public class Activity_Worker extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton menu_BTN;
    private TextView title;
    private View header;
    private TextView name;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setWorker_id("207896781");
        firebase.setCompany("benedict");

        setValues();

        title.setText("Watch shifts");

        initFragment(new Fragment_Requestss());

        navigationView.setCheckedItem(R.id.Menu_My_profile);

        menu_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Menu_watch_shifts:
                        initFragment(new Fragment_calender());
                        drawer.close();
                        return true;
                    case R.id.Menu_submit_shifts:
                        initFragment(new Fragment_requests());
                        drawer.close();
                        return true;
                    case R.id.Menu_My_profile:
                        initFragment(new Fragment_Requestss());
                        return true;
                    case R.id.Menu_log_out:
                        initFragment(new Fragment_shifts());
                        drawer.close();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void initFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.worker_fragmentContainer, fragment);
        transaction.commit();
    }

    private void setValues() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        menu_BTN = findViewById(R.id.Worker_BTN_menu);
        title = findViewById(R.id.Worker_LBL_title);

        // header
        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.Header_LBL_name);
        id = header.findViewById(R.id.Header_LBL_id);
        setHeader();
    }

    /* set name and id of worker */
    private void setHeader() {
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setReference("/" + firebase.getCompany() + "/workers/" + firebase.getWorker_id());
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(String.format("%s %s", snapshot.child("first_name").getValue().toString(),
                        snapshot.child("last_name").getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
        id.setText(firebase.getWorker_id());
        // set worker image
        setHeaderImage();
    }

    /* set worker image on header */
    private void setHeaderImage() {
        My_images.initHelper(this).setPlaceholder(R.id.Header_IV_workerPhoto);
        try {
            My_images.getInstance().downloadImage("gs://shiftdata-a19a0.appspot.com/workers_images/" +
                    id.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}