package src.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.src.R;

public class Fragment_calender extends Fragment implements CallBack_ShiftActivity {
    protected View view;
    private CalendarView calendarView;
    private CallBack_ShiftActivity callBack_shiftActivity;

    public Fragment_calender() {}

    public static Fragment_calender newInstance() {
        Fragment_calender fragment = new Fragment_calender();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_calender, container, false);

        calendarView = view.findViewById(R.id.calender_CAL_calender);
        setListener();
        return view;
    }

    public void setCallBack_shiftActivity(CallBack_ShiftActivity callBack_shiftActivity) {
        this.callBack_shiftActivity = callBack_shiftActivity;
    }

    private void setListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = day + "/" + (month + 1) + "/" + year;
                if(callBack_shiftActivity != null) {
                    callBack_shiftActivity.setDate(date);
                }
            }
        });
    }

    @Override
    public void setDate(String date) {
        this.setDate(date);
    }
}