package com.example.seekm.studemts;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "haha";
    public ImageView Profile_Image;
    public TextView Profile_Email;
    public TextView Profile_Name;
    public ImageView message_req_button;
    public TextView message_req_textview;
    public String Image_Url;
    public String First_Name;
    public String Last_Name;
    public String Email_Address;
    public String Image_Url1;
    SharedPreferences Profile_preferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
//            View header1 = navigationView.getHeaderView(1);
//            View header2 = navigationView.getHeaderView(2);
        navigationView.setNavigationItemSelectedListener(this);
        Profile_Image = header.findViewById(R.id.sign_up_image_button);
        Profile_Email = header.findViewById(R.id.User_email_logged_In);
        Profile_Name = header.findViewById(R.id.User_Name_Logged_In);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String User_uid1 = currentFirebaseUser.getUid();
        DocumentReference docRef = db.collection("Students").document(User_uid1);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        First_Name = document.getString("FirstName");
                        Last_Name = document.getString("LastName");
                        Email_Address = document.getString("EmailAddress");
                        Image_Url1 = document.getString("ProfileImage_Url");

                        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens", 0);

                        //     String Email_user= Profile_preferences.getString("Email",null);
                        String Name_User = First_Name + " " + Last_Name;

                        Profile_Email.setText(Email_Address);
                        Profile_Name.setText(Name_User);

                        // Image_Url=Profile_preferences.getString("Profile_Image_Url",null);

                        Glide.with(Drawer.this)
                                .load(Image_Url1)
                                .into(Profile_Image);
                        Log.d(TAG, First_Name + Last_Name + Email_Address);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Profile_Image.setImageURI();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();

//            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
            startActivity(new Intent(Drawer.this, MobileV.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}