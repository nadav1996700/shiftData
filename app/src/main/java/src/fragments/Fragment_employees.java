package src.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Fragment_employees extends Fragment {
    protected View view;
    private ListView workers_list;
    private TextView name;
    private Button detailsBTN;
    private Button deleteBTN;
    private FloatingActionButton addWorker;
    private ArrayList<String> names;
    private ArrayList<String> id_list;
    private Context context;
    private CallBack_employeesFragment callBack_employeesFragment;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_employees(Context context) {
        this.context = context;
    }

    public static Fragment_employees newInstance(Context context) {
        Fragment_employees fragment = new Fragment_employees(context);
        return fragment;
    }

    public void setCallBack(CallBack_employeesFragment callBack) {
        this.callBack_employeesFragment = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_employees, container, false);
        firebase.setCompany("benedict");
        // download workers data to lists
        initLists();
        // initialize variables
        initValues();
        // set floating Button listener
        floatingButtonListener();
        return view;
    }

    private void floatingButtonListener() {
        addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack_employeesFragment.changeFragment(new Fragment_addWorker());
            }
        });
    }

    private void initValues() {
        workers_list = view.findViewById(R.id.Workers_list_LST_list);
        addWorker = view.findViewById(R.id.Workers_list_BTN_addWorker);
        workers_list.setAdapter(new CustomAdapter());
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public String getItem(int i) {
            return names.get(i) + " " + id_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.custom_raw, null);

            Log.d("pttt", "inside getView");
            // initialize variables
            setValues(view1);
            // buttons listeners
            setListeners(position);
            Log.d("pttt", "after setListeners");
            // set worker image
            setUserImage(position);
            Log.d("pttt", "after setImage");
            // set details TextView
            setDetails(position);
            Log.d("pttt", "after setDetails");

            return view1;
        }
    }

    private void initLists() {
        // initialize lists
        names = new ArrayList<>();
        id_list = new ArrayList<>();
        // insert data
        firebase.setReference("/" + firebase.getCompany() + "/workers");
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id_list.clear();
                names.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String first_name = child.child("first_name").getValue().toString();
                    String last_name = child.child("last_name").getValue().toString();
                    String id = child.child("id").getValue().toString();
                    names.add(first_name + " " + last_name);
                    id_list.add(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    /* set name and family of worker in TextView */
    private void setDetails(int position) {
        String details = names.get(position);
        name.setText(details);
    }

    /* set user image at ImageView */
    private void setUserImage(int position) {
        String id = id_list.get(position);
        String path = "gs://shiftdata-a19a0.appspot.com/workers_images/" + id;
        My_images images = My_images.getInstance();
        images.setPlaceholder(R.id.raw_IMG_photo);
        images.downloadImage(path);
        Log.d("pttt", "done with image");
    }

    /* set buttons listeners */
    private void setListeners(int position) {
        firebase.setWorker_id(id_list.get(position));
        detailsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack_employeesFragment != null) {
                    callBack_employeesFragment.changeFragment(new Fragment_Profile());
                }
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove worker from firebase
                firebase.setReference("/" + firebase.getCompany() + "/workers/" +
                        firebase.getWorker_id());
                firebase.getReference().removeValue();
                // remove from lists
                for (int i = 0; i < names.size(); i++) {
                    if (id_list.get(i).equals(firebase.getWorker_id())) {
                        names.remove(i);
                        id_list.remove(i);
                    }
                }
            }
        });
    }

    /* initialize values */
    private void setValues(View view) {
        name = view.findViewById(R.id.raw_LBL_details);
        detailsBTN = view.findViewById(R.id.raw_BTN_details);
        deleteBTN = view.findViewById(R.id.raw_BTN_delete);
    }
}