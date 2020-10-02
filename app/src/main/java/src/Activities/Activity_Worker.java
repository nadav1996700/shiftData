package src.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.src.R;
import com.google.android.material.navigation.NavigationView;

public class Activity_Worker extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageButton menu_BTN;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        setValues();
        title.setText("shifts");

        menu_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        /*

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.worker_menu, menu);
        return true;
    }

    private void setValues() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.worker_nav_view);
        menu_BTN = findViewById(R.id.Worker_BTN_menu);
        title = findViewById(R.id.Worker_LBL_title);
    }
}