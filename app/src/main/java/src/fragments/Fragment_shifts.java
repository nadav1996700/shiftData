package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.src.R;

public class Fragment_shifts extends Fragment implements CallBack_ShiftActivity {
    protected View view;
    private TextView chosen_date;

    public Fragment_shifts() {}

    public static Fragment_shifts newInstance() {
        Fragment_shifts fragment = new Fragment_shifts();
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
            view = inflater.inflate(R.layout.fragment_shifts, container, false);
        chosen_date = view.findViewById(R.id.date);
        return view;
    }

    @Override
    public void setDate(String date) {
        chosen_date.setText(date);
        //updateData();
    }

    // change data of shifts list
    private void updateData() {

    }
}