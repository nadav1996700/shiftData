package src.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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

import java.util.Objects;

import src.Utils.My_Firebase;
import src.Utils.My_images;
import src.fragments.Fragment_Profile;
import src.fragments.Fragment_Request;
import src.fragments.Fragment_currentShifts;

import static src.fragments.Fragment_Profile.PICK_PROFILE_IMAGE;

public class Activity_Worker extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton menu_BTN;
    private TextView title;
    private View header;
    private TextView name;
    private TextView id;
    // need to be deleted
    My_images images = My_images.initHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setWorker_id("207896781");
        firebase.setCompany("benedict");

        setValues();

        /* set first screen - shifts screen */
        title.setText("shifts calender");
        initFragment(new Fragment_currentShifts());
        navigationView.setCheckedItem(R.id.Menu_watch_shifts);

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
                        title.setText("shifts calender");
                        initFragment(new Fragment_currentShifts());
                        drawer.close();
                        return true;
                    case R.id.Menu_submit_shifts:
                        title.setText("shifts request");
                        initFragment(new Fragment_Request());
                        drawer.close();
                        return true;
                    case R.id.Menu_My_profile:
                        title.setText("Profile");
                        initFragment(new Fragment_Profile());
                        drawer.close();
                        return true;
                    case R.id.Menu_log_out:
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /* start new fragment */
    private void initFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.worker_fragmentContainer, fragment);
        transaction.commit();
    }

    /* initialize variables */
    private void setValues() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        menu_BTN = findViewById(R.id.Worker_BTN_menu);
        title = findViewById(R.id.Worker_LBL_title);

        // header
        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.WorkerHeader_LBL_name);
        id = header.findViewById(R.id.WorkerHeader_LBL_id);
        setHeader();
    }

    /* set name and id of worker */
    private void setHeader() {
        setWorkerImage();
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setReference("/" + firebase.getCompany() + "/workers/" + firebase.getWorker_id());
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(String.format("%s %s",
                        Objects.requireNonNull(snapshot.child("first_name").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("last_name").getValue()).toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
        id.setText(firebase.getWorker_id());
        setTitle();
        setBackgroundImage();
    }

    /* set item "account" title style on header */
    private void setTitle() {
        //title - account
        MenuItem account = navigationView.getMenu().findItem(R.id.WorkerMenu_account);
        SpannableString s = new SpannableString(account.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance_title),
                0, s.length(), 0);
        account.setTitle(s);
    }

    /* set background image on header */
    private void setBackgroundImage() {
        /* header background image */
        images.setPlaceholder(R.id.WorkerHeader_IV_background);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/general_images/" +
                "worker_background.jpg");
    }

    /* set worker image on header */
    private void setWorkerImage() {
        /* worker image */
        images.setPlaceholder(R.id.WorkerHeader_IV_workerPhoto);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/workers_images/" +
                id.getText().toString());
    }

    /* handle images from gallery */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PROFILE_IMAGE) {
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null)
                images.setImage(R.id.Profile_IV_photo, photo);
        }
    }
}