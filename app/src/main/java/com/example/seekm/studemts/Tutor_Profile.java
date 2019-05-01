package com.example.seekm.studemts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;


public class Tutor_Profile extends AppCompatActivity {
    private double latitude, longitude ;
    private  String First_Name ,Last_Name ;
    private static final String TAG = "MapActivity";
    ListView listView;
    Button send_request_button;
    public String First_Name_student , Last_Name_student ,Email_Address_student;

    public String doc_id_id;
    Intent intent = getIntent();
    //public     String doc_id_id = intent.getStringExtra("doc_id");

    TextView First_Name1 , LastName ;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor__profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doc_id_id = extras.getString("doc_id");
            //The key argument here must match that used in the other activity
        }

        First_Name1 = findViewById(R.id.FNAME);
        LastName = findViewById(R.id.FNAME1);
        send_request_button = findViewById(R.id.send_request_button);


        DocumentReference docRef = db.collection("Tutors").document(doc_id_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        First_Name = document.getString("FirstName");
                        Last_Name = document.getString("LastName");
                        First_Name1.setText(First_Name);
                        LastName.setText(Last_Name);


                    //    Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens", 0);

                        //     String Email_user= Profile_preferences.getString("Email",null);
                   //     String Name_User = First_Name + " " + Last_Name;

//                        Profile_Email.setText(Email_Address);
  //                       Profile_Name.setText(Name_User);

                        // Image_Url=Profile_preferences.getString("Profile_Image_Url",null);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        send_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String User_uid1 = currentFirebaseUser.getUid();

                DocumentReference docRef = db.collection("Tutors").document(User_uid1);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                First_Name_student = document.getString("FirstName");
                                Last_Name_student = document.getString("LastName");
                                Email_Address_student = document.getString("EmailAddress");

                            } else {
                       //         Log.d(TAG, "No such document");
                            }
                        } else {
                     //       Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


                Map<String, Object> Request_Tutor = new HashMap<>();
                Request_Tutor.put("User_uid",User_uid1);
                Request_Tutor.put("First_Name_student",First_Name_student);
                Request_Tutor.put("Last_Name_student",Last_Name_student);
                Request_Tutor.put("Email_Address_Student",Email_Address_student);
                Request_Tutor.put("Status","Requested");


                db.collection("Tutors").document(doc_id_id).collection("requests").document(User_uid1)
                        .set(Request_Tutor).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");


                      //  Toast.makeText(MapsActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT);
                  //      startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
                     //   finishAfterTransition();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


            }
        });

    }


}




