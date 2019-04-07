package com.example.seekm.studemts;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
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
import com.google.firebase.auth.FirebaseAuth;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageView Profile_Image;
    public TextView Profile_Email;
    public TextView Profile_Name;
    ImageView advert,history,nearby,messages;
    public String Image_Url;

    SharedPreferences Profile_preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drawer);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            advert = (ImageView)findViewById(R.id.advert);
            advert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Drawer.this,Advert.class);
                    startActivity(intent);
                }
            });

        messages = (ImageView)findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drawer.this,Messages.class);
                startActivity(intent);
            }
        });

        history= (ImageView)findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drawer.this,History.class);
                startActivity(intent);
            }
        });

        nearby= (ImageView)findViewById(R.id.nearbyTutors);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drawer.this,NearbyTutors.class);
                startActivity(intent);
            }
        });







            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);


            navigationView.setNavigationItemSelectedListener(this);

        Profile_Image=header.findViewById(R.id.sign_up_image_button);
        Profile_Email=header.findViewById(R.id.User_email_logged_In);
        Profile_Name=header.findViewById(R.id.User_Name_Logged_In);


        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens",0);

        String Email_user= Profile_preferences.getString("Email",null);
        String Name_User= Profile_preferences.getString("First_Name",null)+ " " + Profile_preferences.getString("Last_Name",null);



        //Profile_Email.setText("Hello");
        Profile_Name.setText(Name_User);





        Image_Url=Profile_preferences.getString("Profile_Image_Url",null);

        Glide.with(Drawer.this)
                .load(Image_Url)
                .into(Profile_Image);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        Toast.makeText(this,"id=" + id,Toast.LENGTH_LONG).show();
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
            startActivity(new Intent(Drawer.this,MobileV.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}