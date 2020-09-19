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

import src.Classes.Company;
import src.Classes.Worker;

public class My_Firebase {
    private static My_Firebase instance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    private String company;
    private Worker worker;
    private ArrayList<Worker> workers;

    private My_Firebase() {}

    public static My_Firebase getInstance() {return instance;}

    public static My_Firebase initHelper() {
        if(instance == null)
            instance = new My_Firebase();
        return instance;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /* check if user exist in firebase */
    public boolean userExist(String company, String username, String password) {
        reference = database.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("").getKey();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return true;
    }

    /* read single worker from firebase */
    public Worker readWorker(String id) {
        String path = "/" + company + "/workers/" + id;
        reference = database.getReference(path);
        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String first_name = snapshot.child("first_name").getValue().toString();
                String last_name = snapshot.child("last_name").getValue().toString();
                String username = snapshot.child("username").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                String id = snapshot.child("id").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String age = snapshot.child("age").getValue().toString();
                String company = snapshot.child("company").getValue().toString();
                String photo = snapshot.child("photo").getValue().toString();
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

    public void addCompany(Company company) {
        // add company name to list of company names
        reference = database.getReference("/");
        reference.child("company_names/" + company.getName()).setValue("");
        // add company object to database
        reference = database.getReference("/" + company.getName());
        reference.child("/").setValue(company);
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
