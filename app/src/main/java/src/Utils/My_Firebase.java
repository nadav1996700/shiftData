package src.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import src.Classes.Worker;

public class My_Firebase {
    private static My_Firebase instance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    private String company;
    private Worker worker;
    private ArrayList<Worker> workers;

    private My_Firebase() {

    }

    public static My_Firebase getInstance() {return instance;}

    public static My_Firebase initHelper() {
        if(instance == null)
            instance = new My_Firebase();
        return instance;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /* read single worker from firebase */
    public Worker readWorker(String id) {
        String path = "/" + company + "/workers" + "/" + id;
        reference = database.getReference(path);
        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String first_name = dataSnapshot.child("first_name").getValue().toString();
                String last_name = dataSnapshot.child("last_name").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String id = dataSnapshot.child("id").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String age = dataSnapshot.child("age").getValue().toString();
                String company = dataSnapshot.child("company").getValue().toString();
                String photo = dataSnapshot.child("photo").getValue().toString();
                // create Worker
                worker = new Worker(first_name, last_name, username,
                        password, id, phone, company, Integer.valueOf(age), Integer.valueOf(photo));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("pttt", "Error in loading worker data");
            }
        });
        return worker;
    }

    /* read all workers from firebase */
    public ArrayList<Worker> readWorkers() {
        String path = "/" + company + "/workers";
        reference = database.getReference(path);
        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workers = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String first_name = child.child("first_name").getValue().toString();
                    String last_name = child.child("last_name").getValue().toString();
                    String username = child.child("username").getValue().toString();
                    String password = child.child("password").getValue().toString();
                    String id = child.child("id").getValue().toString();
                    String phone = child.child("phone").getValue().toString();
                    String age = child.child("age").getValue().toString();
                    String company = child.child("company").getValue().toString();
                    String photo = child.child("photo").getValue().toString();
                    // create Worker
                    workers.add(new Worker(first_name, last_name, username,
                            password, id, phone, company, Integer.valueOf(age), Integer.valueOf(photo)));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("pttt", "Error in loading worker data");
            }
        });
        return workers;
    }

    public void addCompany(final String company_name , final String username, final String password) {
        reference = database.getReference("/company_names");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(company_name)) {
                    return;
                } else {
                    // add company name to list of company names
                    reference = database.getReference("/" + company + "/" + company_name);
                   // check_username_and_password(username, password);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // upload photo to firebase
    public void uploadImage(String name, String path, int drawable, Resources resources) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawable);
        String encodedImage = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.DEFAULT);
        reference.child("/" + path + "/" + name).setValue(encodedImage);
    }

    // convert from bitmap to byte array
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
