package src.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.src.R;

public class Fragment_calender extends Fragment implements CallBack_setDate {
    protected View view;
    private CalendarView calendarView;
    private CallBack_setDate callBack_setDate;

    public Fragment_calender() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_calender, container, false);

        calendarView = view.findViewById(R.id.calender_CAL_calender);
        setListener();
        return view;
    }

    public void setCallBack_setDate(CallBack_setDate callBack_requestsActivity) {
        this.callBack_setDate = callBack_requestsActivity;
    }

    private void setListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = day + "," + (month + 1) + "," + year;
                if (callBack_setDate != null)
                    callBack_setDate.setDate(date);
            }
        });
    }

    @Override
    public void setDate(String date) {
        this.setDate(date);
    }
}