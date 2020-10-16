package src.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.src.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import src.Classes.RecyclerViewAdapter;
import src.Classes.DataItem;
import src.Utils.My_Firebase;

public class Fragment_employees extends Fragment implements EmployeesClickListener {
    protected View view;
    private RecyclerView recyclerView;
    private ArrayList<DataItem> DataItems = new ArrayList<>();
    private FloatingActionButton addWorker;
    private Activity activity;
    private CallBack_employeesFragment callBack_employeesFragment;
    private MaterialAlertDialogBuilder dialog;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_employees(Activity activity) {
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
            view = inflater.inflate(R.layout.fragment_employees, container, false);
        // set AddWorker button
        addWorker = view.findViewById(R.id.employees_BTN_addWorker);
        floatingButtonListener();
        // set RecyclerView
        setRecyclerView();
        // fill ArrayList with data
        getData();
        return view;
    }

    private void setRecyclerView() {
        recyclerView = view.findViewById(R.id.employees_LST_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void getData() {
        // get path
        firebase.setReference("/" + firebase.getCompany() + "/workers");
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataItems.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String first_name = Objects.requireNonNull(child.child("first_name").
                            getValue()).toString();
                    String last_name = Objects.requireNonNull(child.child("last_name").
                            getValue()).toString();
                    String id = Objects.requireNonNull(child.child("id").getValue()).toString();
                    DataItems.add(new DataItem(id, first_name, last_name));
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    private void setAdapter() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(activity, DataItems);
        adapter.setEmployeeClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void floatingButtonListener() {
        addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack_employeesFragment.changeFragment(new Fragment_addWorker());
            }
        });
    }

    public void setCallBack(CallBack_employeesFragment callBack_employeesFragment) {
        this.callBack_employeesFragment = callBack_employeesFragment;
    }

    private void show_dialog() {
        dialog = new MaterialAlertDialogBuilder(activity);
        dialog.setTitle(getResources().getString(R.string.select_action));
        dialog.setMessage(R.string.employee_massage);
        dialog.setIcon(R.drawable.ic_employee_dialog);
        dialog.setPositiveButton(getResources().getString(R.string.see_profile), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // open profile fragment
                firebase.setWorker_id(firebase.getWorker_id());
                callBack_employeesFragment.changeFragment(new Fragment_Profile());
            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.delete_worker), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // remove worker
                firebase.setReference("/" + firebase.getCompany() + "/workers/" + firebase.getWorker_id());
                firebase.getReference().removeValue();
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }
        });
        dialog.setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Respond to neutral button press
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void presentDialog() {
        show_dialog();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}