package src.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import src.Classes.Worker;
import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Fragment_Profile extends Fragment {
    public static final int PICK_IMAGE = 1;
    protected View view;
    private Button edit;
    private Button select_photo;
    private EditText first_name;
    private EditText last_name;
    private EditText id;
    private EditText phone;
    private EditText age;
    private EditText username;
    private EditText password;
    private ImageView profile_photo;
    private ArrayList<EditText> list;
    My_Firebase firebase = My_Firebase.getInstance();
    My_images images = My_images.getInstance();

    public Fragment_Profile() {}

    public static Fragment_Profile newInstance() {
        Fragment_Profile fragment = new Fragment_Profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* initialize variables */
        setValues();
        /* present current data from firebase */
        presentData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_photo.setVisibility(View.VISIBLE);
                String action = edit.getText().toString();
                if(action.equals("EDIT")) {
                    edit.setText("Done");
                    enable_editText();
                }
                else {
                    select_photo.setVisibility(View.INVISIBLE);
                    edit.setText("EDIT");
                    disable_editText();
                    saveData();
                }
            }
        });

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            try {
                // convert data to drawable
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Drawable photo = new BitmapDrawable(getResources(), selectedImage);
                // set image
                images.setImage(R.id.Profile_IV_photo, photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

     */

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    /* initialize data of edit texts */
    private void presentData() {
        // get path
        String path = "/" + firebase.getCompany() + "/workers/" + firebase.getWorker_id();
        firebase.setReference(path);
        // read worker from firebase and present data
        setData();
        // read image
        images.setPlaceholder(R.id.Profile_IV_photo);
        path = "gs://shiftdata-a19a0.appspot.com/workers_images/" + firebase.getWorker_id();
        images.downloadImage(path);
    }

    /* save data after user done to editing */
    private void saveData() {
        // get path
        String path = "/" + firebase.getCompany() + "/workers/" + firebase.getWorker_id();
        firebase.setReference(path);
        // save data
        firebase.getReference().child("username").setValue(username.getText().toString());
        firebase.getReference().child("password").setValue(password.getText().toString());
        firebase.getReference().child("age").setValue(age.getText().toString());
        firebase.getReference().child("phone").setValue(phone.getText().toString());
        // save image
        path = "gs://shiftdata-a19a0.appspot.com/workers_images/" + firebase.getWorker_id();
        images.uploadImage(profile_photo, path);
    }

    private void disable_editText() {
        for(EditText editText: list)
            editText.setEnabled(false);
    }

    private void enable_editText() {
        for(EditText editText: list)
            editText.setEnabled(true);
    }

    private void setValues() {
        edit = view.findViewById(R.id.Profile_BTN_edit);
        select_photo = view.findViewById(R.id.Profile_BTN_editPhoto);
        first_name = view.findViewById(R.id.Profile_EDT_firstName);
        last_name = view.findViewById(R.id.Profile_EDT_lastName);
        id = view.findViewById(R.id.Profile_EDT_id);
        phone = view.findViewById(R.id.Profile_EDT_phone);
        age = view.findViewById(R.id.Profile_EDT_age);
        username = view.findViewById(R.id.Profile_EDT_username);
        password = view.findViewById(R.id.Profile_EDT_password);
        profile_photo = view.findViewById(R.id.Profile_IV_photo);

        list = new ArrayList<>();
        list.add(phone);
        list.add(age);
        list.add(username);
        list.add(password);
    }

    /* read single worker from firebase */
    private void setData() {
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = snapshot.child("first_name").getValue().toString();
                String last_name = snapshot.child("last_name").getValue().toString();
                String username = snapshot.child("username").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                String id = snapshot.child("id").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String age = snapshot.child("age").getValue().toString();
                // create Worker
                Worker worker = new Worker(first_name, last_name, username,
                        password, id, phone, Integer.valueOf(age));
                // show data
                showData(worker);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR_TAG", "Error in loading worker data");
            }
        });
    }

    /* present current data of worker */
    private void showData(Worker worker) {
        first_name.setText(worker.getFirst_name());
        last_name.setText(worker.getLast_name());
        id.setText(worker.getId());
        age.setText(String.valueOf(worker.getAge()));
        phone.setText(worker.getPhone());
        username.setText(worker.getUsername());
        password.setText(worker.getPassword());
    }
}