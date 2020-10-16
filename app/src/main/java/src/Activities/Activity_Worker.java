package src.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import src.Utils.My_Firebase;
import src.Utils.My_images;
import src.fragments.CallBack_contactsSweep;
import src.fragments.Fragment_Contacts;
import src.fragments.Fragment_Profile;
import src.fragments.Fragment_Request;
import src.fragments.Fragment_currentShifts;

import static src.fragments.Fragment_Profile.PICK_PROFILE_IMAGE;

public class Activity_Worker extends AppCompatActivity implements CallBack_contactsSweep {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton menu_BTN;
    private TextView title;
    private View header;
    private TextView name;
    private TextView id;
    private Activity activity = this;
    private ImageView header_background;
    private ShapeableImageView worker_image;
    private CallBack_contactsSweep callBack_contactsSweep = this;
    My_images images = My_images.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        /* initialize variables */
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
                    case R.id.Menu_submit_shifts:
                        title.setText(R.string.shifts_request);
                        initFragment(new Fragment_Request());
                        drawer.close();
                        return true;
                    case R.id.Menu_My_profile:
                        title.setText(R.string.Profile);
                        initFragment(new Fragment_Profile());
                        drawer.close();
                        return true;
                    case R.id.Menu_contacts:
                        title.setText(R.string.Contacts);
                        Fragment_Contacts fragment_contacts = new Fragment_Contacts(activity);
                        fragment_contacts.setCallBack_contactsSweep(callBack_contactsSweep);
                        initFragment(fragment_contacts);
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
        drawer = findViewById(R.id.Worker_drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        menu_BTN = findViewById(R.id.Worker_BTN_menu);
        title = findViewById(R.id.Worker_LBL_title);

        // header
        header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.WorkerHeader_LBL_name);
        id = header.findViewById(R.id.WorkerHeader_LBL_id);
        header_background = header.findViewById(R.id.WorkerHeader_IV_background);
        worker_image = header.findViewById(R.id.WorkerHeader_IV_workerPhoto);
        setHeader();
    }

    /* set name and id of worker */
    private void setHeader() {
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
        setImages();
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

    /* set worker image and background image on header */
    private void setImages() {
        /* worker image */
        //images.setActivity(this);
        String path = "gs://shiftdata-a19a0.appspot.com/workers_images/" +
                id.getText().toString();
        images.downloadImageUrl(path, worker_image);
        /* header background image */
        path = "gs://shiftdata-a19a0.appspot.com/general_images/" +
                "Worker_header.jpg";
        images.downloadImageUrl(path, header_background);
    }

    /* handle images from gallery */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PROFILE_IMAGE) {
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null) {
                ImageView imageView = findViewById(R.id.Profile_IV_photo);
                //images.setPlaceholder(R.id.Profile_IV_photo);
                images.setImage(photo, imageView);
            }
        }
    }

    @Override
    public void makeCall(String phone) {
        String requiredPermission = Manifest.permission.CALL_PHONE;
        int checkVal = activity.checkCallingOrSelfPermission(requiredPermission);
        if (checkVal == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(phone));
            startActivity(intent);
        } else {
            // show error dialog
            Log.d("pttt", "error in permission!");
        }
    }

    @Override
    public void onResume() {
        //title.setText(R.string.shifts_calender);
        //initFragment(new Fragment_currentShifts());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}