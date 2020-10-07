package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.src.R;

public class Fragment_setShifts extends Fragment {
    protected View view;

    public Fragment_setShifts() {}

    public static Fragment_setShifts newInstance() {
        Fragment_setShifts fragment = new Fragment_setShifts();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_set_shifts, container, false);
        return view;
    }
}