package src.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.src.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import src.Classes.DataItem;
import src.Utils.My_Firebase;

public class Fragment_setShifts extends Fragment implements DatePickerDialog.OnDateSetListener {
    My_Firebase firebase = My_Firebase.getInstance();
    protected View view;
    private ImageButton calender;
    private EditText date;
    private AutoCompleteTextView select_shift;
    private AutoCompleteTextView select_worker;
    private Chip all_workers;
    private Chip only_submitted;
    private Button add_to_shift;
    private Button remove_from_shift;
    private String selected_shift;
    private String selected_date;
    private DataItem selected_worker;
    private Activity activity;
    private String path_only_submitted;
    private String path_all_workers;

    public Fragment_setShifts(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        // set select_shift adapter and listener
        set_select_shift_Adapter();
        // set chips listeners
        chip_listeners();
        // set on item selected - activate buttons
        setOnItemSelected_Worker();
        // buttons listeners
        buttons_listeners();
        return view;
    }

    private void buttons_listeners() {
        add_to_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_worker == null)
                    select_worker.setBackgroundColor(Color.RED);
                else {
                    firebase.setReference(getWorkerPath());
                    firebase.getReference().setValue(selected_worker);
                    select_worker.setBackgroundColor(Color.parseColor("#40B1E4"));
                }
            }
        });
        remove_from_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_worker == null)
                    select_worker.setBackgroundColor(Color.RED);
                else {
                    firebase.setReference(getWorkerPath());
                    firebase.getReference().removeValue();
                    select_worker.setBackgroundColor(Color.parseColor("#40B1E4"));
                }
            }
        });
    }

    /* set path that used to add or remove worker,
     * if date, shift, or worker is null, returns null */
    private String getWorkerPath() {
        try {
            return "/" + firebase.getCompany() + "/shifts/current_shifts/"
                    + selected_date + "/" + select_shift.getText().toString()
                    + "/" + selected_worker.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void chip_listeners() {
        all_workers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    only_submitted.setChecked(false);
                    setAdapter(path_all_workers);
                } else {
                    only_submitted.setChecked(true);
                    setAdapter(path_only_submitted);
                }
            }
        });
        only_submitted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    all_workers.setChecked(false);
                    setAdapter(path_only_submitted);
                } else {
                    all_workers.setChecked(true);
                    setAdapter(path_all_workers);
                }
            }
        });
    }

    /* this function keep listening to changes, get real time data about workers */
    private void setAdapter(String path) {
        firebase.setReference(path);
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataItem> workers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String id = Objects.requireNonNull(child.child("id").getValue()).toString();
                    String first_name = Objects.requireNonNull(child.child("first_name")
                            .getValue()).toString();
                    String last_name = Objects.requireNonNull(child.child("last_name")
                            .getValue()).toString();
                    workers.add(new DataItem(id, first_name, last_name));
                }
                setSpinner(workers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "error in loading workers");
            }
        });
    }

    /* set workers spinner data */
    private void setSpinner(ArrayList<DataItem> workers) {
        ArrayAdapter<DataItem> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item,
                workers);
        select_worker.setAdapter(adapter);
    }

    private void setOnItemSelected_Worker() {
        select_worker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                selected_worker = (DataItem) adapterView.getItemAtPosition(pos);
            }
        });
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

        // set path variables
        path_only_submitted = "/" + firebase.getCompany() + "/shifts/requests/" +
                selected_date + "/" + selected_shift;
        path_all_workers = "/" + firebase.getCompany() + "/workers";
    }

    private void showDataPickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                R.style.DialogTheme,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // selected date used to save date on firebase
        selected_date = day + "," + (month + 1) + "," + year;
        // set EditText
        String chosen_date = day + "/" + (month + 1) + "/" + year;
        date.setText(chosen_date);
        // set adapter
        select_workers_adapter();
    }

    /* decide which adapter to set for workers according to selected chip*/
    private void select_workers_adapter() {
        select_worker.setText("");
        if (all_workers.isChecked())
            setAdapter(path_all_workers);
        else {
            path_only_submitted = "/" + firebase.getCompany() + "/shifts/requests/" +
                    selected_date + "/" + selected_shift;
            setAdapter(path_only_submitted);
        }
    }

    /* set shift spinner adapter data */
    private void set_select_shift_Adapter() {
        ArrayList<String> options = new ArrayList();
        options.add("Morning");
        options.add("Evening");
        options.add("Night");
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, options);
        select_shift.setAdapter(adapter);
        // set listener
        select_shift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                selected_shift = adapterView.getItemAtPosition(pos).toString();
                select_workers_adapter();
            }
        });
    }
}