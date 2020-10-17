package src.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import src.fragments.CallBack_employeesFragment;
import src.fragments.Fragment_addWorker;
import src.fragments.Fragment_currentShifts;
import src.fragments.Fragment_employees;
import src.fragments.Fragment_setShifts;

import static src.fragments.Fragment_addWorker.Add_Worker_IMAGE;

public class Activity_Manager extends AppCompatActivity implements CallBack_employeesFragment {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton menu_BTN;
    private ImageView headerImage;
    private TextView title;
    private View header;
    private TextView name;
    private CallBack_employeesFragment callBack_employeesFragment = this;
    My_images images = My_images.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        /* init variables */
        setValues();
        /* set first screen - shifts screen */
        title.setText(R.string.shifts_calender);
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
                        initFragment(new Fragment_setShifts());
                        drawer.close();
                        return true;
                    case R.id.Menu_employees:
                        title.setText(R.string.company_employees);
                        Fragment_employees employees = new Fragment_employees();
                        employees.setCallBack(callBack_employeesFragment);
                        initFragment(employees);
                        drawer.close();
                        return true;
                    case R.id.Menu_addWorker:
                        title.setText(R.string.newWorker);
                        initFragment(new Fragment_addWorker());
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
        headerImage = header.findViewById(R.id.ManagerHeader_IV_background);
        setHeader();
    }

    /* set name as the name of the company */
    private void setHeader() {
        name.setText(My_Firebase.getInstance().getCompany());
        setTitle();
        setHeaderImage();
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
    private void setHeaderImage() {
        /* header background image */
        String path = "gs://shiftdata-a19a0.appspot.com/general_images/" +
                "manager_background.jpg";
        images.downloadImageUrl(path, headerImage);
    }

    /* handle images from gallery */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Add_Worker_IMAGE) {
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null) {
                ImageView imageView = findViewById(R.id.addWorker_IV_photo);
                images.setImage(photo, imageView);
            }
        }
    }

    @Override
    public void changeFragment(Fragment fragment) {
        initFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}