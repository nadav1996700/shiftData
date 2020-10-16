package src.fragments;

import android.app.Activity;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import src.Classes.RecyclerViewAdapter;
import src.Classes.DataItem;
import src.Utils.My_Firebase;

public class Fragment_shifts extends Fragment implements CallBack_setDate {
    protected View view;
    private String chosen_date;
    private String chosen_shift = "Morning";
    private TabLayout tabLayout;
    private RecyclerView workers_list;
    private Activity activity;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_shifts(Activity activity) {
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
            view = inflater.inflate(R.layout.fragment_shifts, container, false);
        setValues();
        setTabListener();
        showListByDateAndShift();
        return view;
    }

    private void setValues() {
        tabLayout = view.findViewById(R.id.fragment_shifts_LAY_tabs);
        workers_list = view.findViewById(R.id.fragment_shifts_LST_list);
        // set RecyclerView
        setRecyclerView();
        // initialize chosen date to current date
        chosen_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date()).
                replace('-', ',');
    }

    private void setTabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        chosen_shift = "Morning";
                        break;
                    case 1:
                        chosen_shift = "Evening";
                        break;
                    case 2:
                        chosen_shift = "Night";
                        break;
                }
                showListByDateAndShift();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setRecyclerView() {
        workers_list = view.findViewById(R.id.fragment_shifts_LST_list);
        workers_list.setLayoutManager(new LinearLayoutManager(activity));
        workers_list.addItemDecoration(new DividerItemDecoration(workers_list.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    public void showListByDateAndShift() {
        // set reference
        String path = "/" + firebase.getCompany() + "/shifts/current_shifts/" +
                chosen_date + "/" + chosen_shift;
        firebase.setReference(path);
        // get data
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<DataItem> workers = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String first_name = Objects.requireNonNull(child.child("first_name").
                            getValue()).toString();
                    String last_name = Objects.requireNonNull(child.child("last_name").
                            getValue()).toString();
                    String id = Objects.requireNonNull(child.child("id").getValue()).toString();
                    // create Worker item (dataItem)
                    workers.add(new DataItem(id, first_name, last_name));
                }
                workers_list.setAdapter(new RecyclerViewAdapter(activity, workers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    @Override
    public void setDate(String date) {
        chosen_date = date;
        showListByDateAndShift();
    }
}