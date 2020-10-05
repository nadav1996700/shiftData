package src.Activities;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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

import src.Utils.My_Firebase;
import src.Utils.My_images;
import src.fragments.Fragment_Profile;
import src.fragments.Fragment_Request;
import src.fragments.Fragment_currentShifts;

public class Activity_Manager extends AppCompatActivity {
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
        setContentView(R.layout.activity_manager);

        My_Firebase firebase = My_Firebase.getInstance();
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
                        title.setText(R.string.shifts_calender);
                        initFragment(new Fragment_currentShifts());
                        drawer.close();
                        return true;
                    case R.id.Menu_set_shifts:
                        title.setText(R.string.set_shifts);
                        initFragment(new Fragment_Request());
                        drawer.close();
                        return true;
                    case R.id.Menu_employees:
                        title.setText(R.string.company_employees);
                        initFragment(new Fragment_Request());
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
        transaction.replace(R.id.Manager_fragmentContainer, fragment);
        transaction.commit();
    }

    /* initialize variables */
    private void setValues() {
        drawer = findViewById(R.id.Manager_drawer_layout);
        navigationView = findViewById(R.id.Manager_nav_view);
        menu_BTN = findViewById(R.id.Manager_BTN_menu);
        title = findViewById(R.id.Manager_LBL_title);

        // header
        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.ManagerHeader_LBL_name);
        setHeader();
    }

    /* set name as the name of the company */
    private void setHeader() {
        name.setText(My_Firebase.getInstance().getCompany());
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
        images.setPlaceholder(R.id.ManagerHeader_IV_background);
        images.downloadImage("gs://shiftdata-a19a0.appspot.com/general_images/" +
                "manager_background.jpg");
    }
}