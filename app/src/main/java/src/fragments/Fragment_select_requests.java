package src.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.src.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import src.Classes.DataItem;
import src.Utils.Common_utils;
import src.Utils.My_Firebase;

public class Fragment_select_requests extends Fragment implements CallBack_setDate {
    protected View view;
    private String chosen_date;
    private ArrayList<Chip> chips;
    private Chip morning_chip;
    private Chip evening_chip;
    private Chip night_chip;
    private Button add_request;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_select_requests() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
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
                    createDataItem();
            }
        });
    }

    // worker can work in multiple shifts
    private void AddRequestToFirebase(DataItem dataItem) {
        if (morning_chip.isChecked()) {
            add_shift("Morning", dataItem);
        }
        if (evening_chip.isChecked()) {
            add_shift("Evening", dataItem);
        }
        if (night_chip.isChecked()) {
            add_shift("Night", dataItem);
        }
    }

    // add worker id to specific day and shift
    private void add_shift(String shift, DataItem dataItem) {
        firebase.setReference("/" + firebase.getCompany() + "/shifts/requests/"
                + chosen_date + "/" + shift + "/" + firebase.getWorker_id());
        firebase.getReference().setValue(dataItem);
    }

    // create dataItem object that contain id, name, last_name
    private void createDataItem() {
        firebase.setReference("/" + firebase.getCompany() + "/workers/"
                + firebase.getWorker_id());
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = Objects.requireNonNull(snapshot.child("first_name").getValue())
                        .toString();
                String last_name = Objects.requireNonNull(snapshot.child("last_name").getValue())
                        .toString();
                DataItem dataItem = new DataItem(firebase.getWorker_id(), first_name, last_name);
                AddRequestToFirebase(dataItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Common_utils.getInstance().error_dialog(getActivity());
            }
        });
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