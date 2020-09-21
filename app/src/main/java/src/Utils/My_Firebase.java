package src.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import src.Classes.Company;
import src.Classes.Worker;

public class My_Firebase {
    private static My_Firebase instance;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storage_reference = storage.getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database_reference = database.getReference();
    private String company;
    private Worker worker;

    private My_Firebase() {}

    public static My_Firebase getInstance() {return instance;}

    public static My_Firebase initHelper() {
        if(instance == null)
            instance = new My_Firebase();
        return instance;
    }

    public DatabaseReference getReference() {
        return database_reference;
    }

    public void setReference(String ref) {
        this.database_reference = database.getReference(ref);
    }

    public StorageReference getStorage_reference() {
        return storage_reference;
    }

    public void setStorage_reference(String ref) {
        this.storage_reference = storage.getReferenceFromUrl(ref);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /* read single worker from firebase */
    public Worker readWorker(String id) {
        String path = "/" + company + "/workers/" + id;
        database_reference = database.getReference(path);
        database_reference.addValueEventListener(new ValueEventListener() {
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

    /* add company details to firebase */
    public void addCompany(Company company) {
        // add company name to list of company names
        database_reference.child("/company_names/" + company.getName()).setValue("");
        // add company object to database
        database_reference = database.getReference("/" + company.getName());
        database_reference.child("/").setValue(company);
    }

    /* upload photo to firebase */
    public void uploadImage(String path, int drawable, Resources resources) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawable);
        String encodedImage = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.DEFAULT);
        database_reference.child(path).setValue(encodedImage);
    }

    /* convert from bitmap to byte array */
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }
}
