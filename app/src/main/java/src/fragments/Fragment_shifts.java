package src.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.src.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import src.Classes.Worker;
import src.Utils.My_Firebase;

public class Fragment_shifts extends Fragment implements CallBack_ShiftActivity {
    protected View view;
    private String chosen_date;
    private String chosen_shift;
    private TabLayout tabLayout;
    private ListView workers_list;
    My_Firebase firebase = My_Firebase.getInstance();

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
        setValues();
        setTabListener();
        return view;
    }

    private void setValues() {
        tabLayout = view.findViewById(R.id.fragment_shifts_LAY_tabs);
        workers_list = view.findViewById(R.id.fragment_shifts_LST_list);
    }

    private void setTabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "evening shift":
                        chosen_shift = "evening";
                        break;
                    case "night shift":
                        chosen_shift = "night";
                        break;
                    default:
                        chosen_shift = "morning";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showListByShift() {
        String path2 = "/" + firebase.getCompany();
        String path = "/" + firebase.getCompany() + "shifts/current_shifts/" +
                chosen_date + "/" + chosen_shift;
        firebase.setReference(path2);
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //String string = snapshot.getKey();
                if (snapshot.hasChildren()) {
                    Log.d("pttt", "path successes - date = " + chosen_date);
                    setList();
                }
                else
                    Log.d("pttt", "path else - date = " + chosen_date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void setList() {
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Worker> workers = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String first_name = child.child("first_name").getValue().toString();
                    String last_name = child.child("last_name").getValue().toString();
                    String username = child.child("username").getValue().toString();
                    String password = child.child("password").getValue().toString();
                    String id = child.child("id").getValue().toString();
                    String phone = child.child("phone").getValue().toString();
                    String age = child.child("age").getValue().toString();
                    String company = child.child("company").getValue().toString();
                    // create Worker
                    workers.add(new Worker(first_name, last_name, username,
                            password, id, phone, company, Integer.parseInt(age)));
                    Log.d("pttt", "created worker " + workers.toString());
                }
                showDataOnList(workers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    private void showDataOnList(ArrayList<Worker> workers) {
        String[] array = Arrays.copyOf(workers.toArray(), workers.toArray().length, String[].class);
        Log.d("pttt", "inside adapter method = " + array.toString());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        workers_list.setAdapter(adapter);
    }

    @Override
    public void setDate(String date) {
        chosen_date = date;
        showListByShift();
    }
}