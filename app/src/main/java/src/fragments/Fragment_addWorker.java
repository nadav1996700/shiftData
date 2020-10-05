package src.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.src.R;

import src.Classes.Worker;
import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Fragment_addWorker extends Fragment {
    public static final int Add_Worker_IMAGE = 2;
    protected View view;
    private ImageView image;
    private EditText first_name;
    private EditText last_name;
    private EditText phone;
    private EditText age;
    private EditText id;
    private TextView error_message;
    private Button add;
    private Button select_photo;
    My_images images = My_images.getInstance();

    public Fragment_addWorker() {}

    public static Fragment_addWorker newInstance() {
        Fragment_addWorker fragment = new Fragment_addWorker();
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
            view = inflater.inflate(R.layout.fragment_add_worker, container, false);

        My_Firebase.getInstance().setCompany("benedict");

        setValues(view);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Worker worker = createWorker();
                    uploadWorker(worker);
                }
            }
        });

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
        return view;
    }

    /* upload new worker to firebase database and firebase storage*/
    private void uploadWorker(Worker worker) {
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.setReference("/" + firebase.getCompany() + "/workers/" + worker.getId());
        firebase.getReference().setValue(worker);
        // upload image
        String ref = "gs://shiftdata-a19a0.appspot.com/workers_images/" + worker.getId();
        images.uploadImage(image, ref);
    }

    /* create new worker from editText data */
    private Worker createWorker() {
        String fname = first_name.getText().toString();
        String lname = last_name.getText().toString();
        int mage = Integer.parseInt(age.getText().toString());
        String mphone = phone.getText().toString();
        String uid = id.getText().toString();
        String username = fname + "." + lname;
        String password = uid;

        Worker worker = new Worker(fname, lname, username, password, uid, mphone, mage);
        return worker;
    }

    /* check that all fields are correctly filled */
    private boolean validateFields() {
        if (first_name.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_first_name);
            return false;
        } else if (last_name.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_last_name);
            return false;
        }
        else if (phone.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_phone);
            return false;
        }
        else if (age.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_age);
            return false;
        }
        else if (id.getText().toString().trim().length() == 0) {
            error_message.setText(R.string.enter_id);
            return false;
        }
        error_message.setText("");
        return true;
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent, Add_Worker_IMAGE);
    }

    /* initialize variables */
    private void setValues(View view) {
        first_name = view.findViewById(R.id.addWorker_EDT_firstName);
        last_name = view.findViewById(R.id.addWorker_EDT_lastName);
        phone = view.findViewById(R.id.addWorker_EDT_phone);
        age = view.findViewById(R.id.addWorker_EDT_age);
        id = view.findViewById(R.id.addWorker_EDT_id);
        add = view.findViewById(R.id.addWorker_BTN_add);
        error_message = view.findViewById(R.id.addWorker_LBL_error);
        image = view.findViewById(R.id.addWorker_IV_photo);
        select_photo = view.findViewById(R.id.addWorker_BTN_editPhoto);
    }
}