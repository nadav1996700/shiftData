package src.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import src.Utils.My_Firebase;

public class Fragment_employees extends Fragment {
    protected View view;
    private ListView workers_list;
    private TextView userDetails;
    private Button detailsBTN;
    private Button deleteBTN;
    private Button addWorker;
    private ArrayList<String> names;
    private ArrayList<String> id_list;
    My_Firebase firebase = My_Firebase.getInstance();

    public Fragment_employees() {
    }

    public static Fragment_employees newInstance() {
        Fragment_employees fragment = new Fragment_employees();
        return fragment;
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

       // workers_list = new ListView();
        workers_list = view.findViewById(R.id.Workers_list_LST_list);
        addWorker = view.findViewById(R.id.Workers_list_BTN_addWorker);
        //workers_list.setAdapter(new Fragment_employees().CustomAdapter(this, R.layout.custom_raw));
        return view;
    }

    class CustomAdapter extends ArrayAdapter<String> {
        int resource;

        public CustomAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            this.resource = resource;
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public String getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resource, workers_list, false);
            }
            //View view = getLayoutInflater().inflate(R.layout.custom_raw, null);

            Log.d("pttt", "inside getView");
            // initialize variables
            setValues(convertView);
            // buttons listeners
            setListeners(position);
            Log.d("pttt", "after setListeners");
            // set worker image
            setUserImage(position);
            Log.d("pttt", "after setImage");
            // set details TextView
            setDetails(position);
            Log.d("pttt", "after setDetails");

            return convertView;
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
        userDetails.setText(details);
    }

    /* set user image at ImageView */
    private void setUserImage(int position) {
        String id = id_list.get(position);
        String path = "gs://shiftdata-a19a0.appspot.com/workers_images/" + id;
        //My_images images = My_images.initHelper(this);
        //images.setPlaceholder(R.id.raw_IMG_photo);
        //images.downloadImage(path);
        Log.d("pttt", "done with image");
    }

    /* set buttons listeners */
    private void setListeners(final int position) {
        detailsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase.setWorker_id(id_list.get(position));
                //startActivity(new Intent(Activity_WorkersList.this,
                //        Activity_Profile.class));
                //finish();
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove worker from firebase
                firebase.setReference("/" + firebase.getCompany() + "/workers/" +
                        id_list.get(position));
                firebase.getReference().removeValue();
                // remove from lists
                names.remove(position);
                id_list.remove(position);
            }
        });
    }

    /* initialize values */
    private void setValues(View view) {
        userDetails = view.findViewById(R.id.raw_LBL_details);
        detailsBTN = view.findViewById(R.id.raw_BTN_details);
        deleteBTN = view.findViewById(R.id.raw_BTN_delete);
    }
}