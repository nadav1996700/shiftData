package src.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import src.Classes.DataItem;
import src.Classes.RecyclerViewAdapter;
import src.Utils.Common_utils;
import src.Utils.My_Firebase;

public class Fragment_Contacts extends Fragment {
    protected View view;
    private RecyclerView recyclerView;
    private ArrayList<DataItem> DataItems = new ArrayList<>();
    private CallBack_contactsSweep callBack_contactsSweep;
    private Activity activity;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_Contacts(Activity activity) {
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
            view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // set RecyclerView
        setRecyclerView();
        // fill ArrayList with data
        getData();
        return view;
    }

    public void setCallBack_contactsSweep(CallBack_contactsSweep callBack_contactsSweep) {
        this.callBack_contactsSweep = callBack_contactsSweep;
    }

    /* settings of RecyclerView */
    private void setRecyclerView() {
        recyclerView = view.findViewById(R.id.contacts_LST_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    /* get data of workers for adapter */
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
                // show error dialog
                Common_utils.getInstance().error_dialog(activity);
            }
        });
    }

    /* set RecyclerView adapter */
    private void setAdapter() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(activity, DataItems);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    /* set OnSwiped method - call worker by swipe */
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // call worker
                    callWorker(DataItems.get(viewHolder.getAdapterPosition()).getId());
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                }
            };

    /* download worker's phone and make a call */
    private void callWorker(String worker_id) {
        firebase.setReference("/" + firebase.getCompany() + "/workers/" +
                worker_id + "/phone");
        firebase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phone = "tel: " + Objects.requireNonNull(snapshot.getValue()).toString();
                if (callBack_contactsSweep != null)
                    callBack_contactsSweep.makeCall(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // show error dialog
                Common_utils.getInstance().error_dialog(activity);
            }
        });
    }
}