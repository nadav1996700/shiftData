package src.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.src.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Timer;

import src.Classes.Worker;
import src.Utils.CommonUtils;
import src.Utils.My_Firebase;

public class Activity_Entrance extends AppCompatActivity {

    private ImageView center_image;
    private Button sign_up;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        setValues();

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //CommonUtils.getInstance().setImage(center_image, ContextCompat.getDrawable(this,R.drawable.icon_entarance));
        //DatabaseReference reference = database.getReference("/general_images/");

        /*
        My_Firebase firebase = My_Firebase.getInstance();
        firebase.uploadImage("icon_login", "/general_images/",
                R.drawable.login_icon, getResources());
        firebase.uploadImage("manager_background", "/general_images/",
                R.drawable.manager_background, getResources());
        firebase.uploadImage("icon_register", "/general_images/",
                R.drawable.register_icon, getResources());
        firebase.uploadImage("worker_background", "/general_images/",
                R.drawable.worker_background, getResources());
         */


        /*
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("icon_entarance").getValue().toString();
                try {
                    byte[] bytes = image.getBytes("UTF-8");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    CommonUtils.getInstance().setImage(center_image, drawable);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

         */


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignIn.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Entrance.this, Activity_SignUp.class));
            }
        });
    }

    private void setValues() {
        sign_up = findViewById(R.id.entrance_BTN_signUp);
        sign_in = findViewById(R.id.entrance_BTN_signIn);
        center_image = findViewById(R.id.entrance_IMG_entrance);
    }
}