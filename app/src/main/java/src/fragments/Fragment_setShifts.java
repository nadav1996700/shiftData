package src.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.src.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import src.Utils.My_Firebase;

public class Fragment_setShifts extends Fragment implements DatePickerDialog.OnDateSetListener {
    protected View view;
    private ImageButton calender;
    private EditText date;
    private AutoCompleteTextView select_shift;
    private AutoCompleteTextView select_worker;
    private TextView error_message;
    private Chip all_workers;
    private Chip only_submitted;
    private Button add_to_shift;
    private Button remove_from_shift;
    private String selected_date;
    private Activity activity;
    My_Firebase firebase = My_Firebase.getInstance();


    public Fragment_setShifts(Activity activity) {
        this.activity = activity;
    }

    public static Fragment_setShifts newInstance(Activity activity) {
        return new Fragment_setShifts(activity);
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
        // initialize variables
        initValues();
        // set calender button listener
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDataPickerDialog();
            }
        });
        set_select_shift_Adapter();
        // set chips listeners
        chip_listeners();
        // buttons listeners
        buttons_listeners();
        return view;
    }

    private void buttons_listeners() {
        add_to_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_worker.getText().toString().equals(""))
                    error_message.setText(R.string.select_worker);
                else {

                }
            }
        });
        remove_from_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_worker.getText().toString().equals(""))
                    error_message.setText(R.string.select_worker);
                else {

                }
            }
        });
    }

    private void chip_listeners() {
        only_submitted.setChecked(true);
        all_workers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (all_workers.isChecked()) {
                    only_submitted.setChecked(false);
                    setAdapter_allWorkers();
                } else {
                    only_submitted.setChecked(true);
                    setAdapter_only_submitted();
                }
            }
        });
        only_submitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (only_submitted.isChecked()) {
                    all_workers.setChecked(false);
                    setAdapter_only_submitted();
                } else {
                    all_workers.setChecked(true);
                    setAdapter_allWorkers();
                }
            }
        });
    }

    private void setAdapter_only_submitted() {
        if (date.getText().toString().equals("") || select_shift.getText().toString().equals(""))
            error_message.setText(R.string.setShifts_error);
        else {
            firebase.setReference("/" + firebase.getCompany() + "/shifts/requests/"
                    + selected_date + "/" + select_shift.getText().toString());
            firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setAdapter_allWorkers() {
        if (date.getText().toString().equals("") || select_shift.getText().toString().equals(""))
            error_message.setText(R.string.setShifts_error);
        else {
            firebase.setReference("/" + firebase.getCompany() + "/shifts/requests/"
                    + selected_date + "/" + select_shift.getText().toString());
            firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initValues() {
        calender = view.findViewById(R.id.setShifts_BTN_pickDate);
        date = view.findViewById(R.id.setShifts_EDT_pickDate);
        select_shift = view.findViewById(R.id.setShifts_SPN_select_shift);
        select_worker = view.findViewById(R.id.setShifts_SPN_select_worker);
        all_workers = view.findViewById(R.id.setShifts_CHIP_all);
        only_submitted = view.findViewById(R.id.setShifts_CHIP_only_requested);
        add_to_shift = view.findViewById(R.id.setShifts_BTN_add);
        remove_from_shift = view.findViewById(R.id.setShifts_BTN_remove);
        error_message = view.findViewById(R.id.setShifts_LBL_error);
    }

    private void showDataPickerDialog() {
        DatePickerDialog dataPickerDialog = new DatePickerDialog(
                activity,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        dataPickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // selected date used to save date on firebase
        selected_date = day + "," + (month + 1) + "," + year;
        // set EditText
        String chosen_date = day + "/" + (month + 1) + "/" + year;
        date.setText(chosen_date);
    }

    /* set spinner data */
    private void set_select_shift_Adapter() {
        ArrayList<String> options = new ArrayList();
        options.add("Morning");
        options.add("Evening");
        options.add("Night");
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, options);
        select_shift.setAdapter(adapter);
    }
}