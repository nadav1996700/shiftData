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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import src.Classes.RecyclerViewAdapter;
import src.Classes.dataItem;
import src.Utils.My_Firebase;

public class Fragment_employees extends Fragment {
    protected View view;
    private RecyclerView recyclerView;
    private ArrayList<dataItem> dataItems = new ArrayList<>();
    private FloatingActionButton addWorker;
    private Activity activity;
    private CallBack_employeesFragment callBack_employeesFragment;
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
                dataItems.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String first_name = Objects.requireNonNull(child.child("first_name").
                            getValue()).toString();
                    String last_name = Objects.requireNonNull(child.child("last_name").
                            getValue()).toString();
                    String id = Objects.requireNonNull(child.child("id").getValue()).toString();
                    dataItems.add(new dataItem(id, first_name, last_name));
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(activity, dataItems));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
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
}