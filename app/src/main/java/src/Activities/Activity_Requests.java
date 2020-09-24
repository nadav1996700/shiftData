package src.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.src.R;

import src.fragments.CallBack_RequestsActivity;
import src.fragments.Fragment_calender;
import src.fragments.Fragment_requests;

public class Activity_Requests extends AppCompatActivity implements CallBack_RequestsActivity {
    private Fragment_requests fragment_requests;
    private Fragment_calender fragment_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        fragment_calender = Fragment_calender.newInstance();
        fragment_calender.setCallBack_RequestsActivity(this);
        fragment_requests = Fragment_requests.newInstance();
        initFragments(fragment_calender, R.id.Requests_LAY_calender);
        initFragments(fragment_requests, R.id.Requests_LAY_request);
    }

    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_requests.setDate(date);
    }
}