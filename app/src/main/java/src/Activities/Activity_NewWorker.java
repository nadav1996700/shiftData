package src.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.src.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import src.Classes.Worker;
import src.Utils.My_Firebase;
import src.Utils.My_images;

public class Activity_NewWorker extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private ImageButton image;
    private EditText first_name;
    private EditText last_name;
    private EditText phone;
    private EditText age;
    private EditText id;
    private TextView error_message;
    private Button add;
    My_images images = My_images.initHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_worker);

        My_Firebase.getInstance().setCompany("benedict");

        setValues();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Worker worker = createWorker();
                    uploadWorker(worker);
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });

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
                images.setImage(R.id.newWorker_IV_photo, photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    /* initialize variables */
    private void setValues() {
        first_name = findViewById(R.id.newWorker_EDT_firstName);
        last_name = findViewById(R.id.newWorker_EDT_lastName);
        phone = findViewById(R.id.newWorker_EDT_phone);
        age = findViewById(R.id.newWorker_EDT_age);
        id = findViewById(R.id.newWorker_EDT_id);
        add = findViewById(R.id.newWorker_BTN_add);
        error_message = findViewById(R.id.newWorker_LBL_error);
        image = findViewById(R.id.newWorker_IV_photo);
    }
}