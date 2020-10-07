package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.src.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import src.Utils.My_Firebase;

public class Fragment_select_requests extends Fragment implements CallBack_RequestsFragment {
    protected View view;
    private String chosen_date;
    private ArrayList<Chip> chips;
    private Chip morning_chip;
    private Chip evening_chip;
    private Chip night_chip;
    private Button add_request;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_select_requests() {}

    public static Fragment_select_requests newInstance() {
        Fragment_select_requests fragment = new Fragment_select_requests();
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
            view = inflater.inflate(R.layout.fragment_select_requests, container, false);
        // initialize variables
        setValues();
        // set on click listener
        setListener();
        return view;
    }

    private void setListener() {
        add_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosen_date != null)
                    AddRequestToFirebase();
            }
        });
    }

    // worker can work in multiple shifts
    private void AddRequestToFirebase() {
        if(morning_chip.isChecked()) {
            add_shift("morning");
        }
        if(evening_chip.isChecked()) {
            add_shift("evening");
        }
        if(night_chip.isChecked()) {
            add_shift("night");
        }
    }

    // add worker id to specific day and shift
    private void add_shift(String shift) {
        firebase.setReference("/" + firebase.getCompany() + "/shifts/requests/"
        + chosen_date + "/" + shift + "/" + firebase.getWorker_id());
        firebase.getReference().setValue("");
    }

    private void setValues() {
        morning_chip = view.findViewById(R.id.Requests_CHIP_morning);
        evening_chip = view.findViewById(R.id.Requests_CHIP_evening);
        night_chip = view.findViewById(R.id.Requests_CHIP_night);
        add_request = view.findViewById(R.id.Requests_BTN_addRequest);

        chips = new ArrayList<>();
        chips.add(morning_chip);
        chips.add(evening_chip);
        chips.add(night_chip);
    }

    @Override
    public void setDate(String date) {
        chosen_date = date;
    }
}