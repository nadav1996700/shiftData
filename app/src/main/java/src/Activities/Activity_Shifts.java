package src.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.src.R;

import src.fragments.CallBack_ShiftActivity;
import src.fragments.Fragment_calender;
import src.fragments.Fragment_shifts;

public class Activity_Shifts extends AppCompatActivity implements CallBack_ShiftActivity {

    private Fragment_shifts fragment_shifts;
    private Fragment_calender fragment_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        fragment_calender = Fragment_calender.newInstance();
        fragment_calender.setCallBack_shiftActivity(this);
        fragment_shifts = Fragment_shifts.newInstance();
        initFragments(fragment_calender, R.id.shifts_LAY_calender);
        initFragments(fragment_shifts, R.id.shifts_LAY_list);

    }


    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_shifts.setDate(date);
    }
}