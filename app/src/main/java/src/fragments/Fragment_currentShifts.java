package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.src.R;

public class Fragment_currentShifts extends Fragment implements CallBack_RequestsActivity  {
    protected View view;
    private Fragment_shifts fragment_shifts;
    private Fragment_calender fragment_calender;

    public Fragment_currentShifts() {}

    public static Fragment_currentShifts newInstance() {
        Fragment_currentShifts fragment = new Fragment_currentShifts();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_calender = Fragment_calender.newInstance();
        fragment_calender.setCallBack_RequestsActivity(this);
        fragment_shifts = fragment_shifts.newInstance();
        initFragments(fragment_calender, R.id.current_shifts_LAY_calender);
        initFragments(fragment_shifts, R.id.current_shifts_LAY_shifts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_current_shifts, container, false);
        return view;
    }

    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_shifts.setDate(date);
    }
}