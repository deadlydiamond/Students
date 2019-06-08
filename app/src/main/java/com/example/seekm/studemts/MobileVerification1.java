package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MobileVerification1 extends AppCompatActivity {

    //VARS
    EditText mNumber;
    TextView mSocial;

    //Preventing going back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);


        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >=21)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));


        //Views
        mNumber = (EditText)findViewById(R.id.number);
        mNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(MobileVerification1.this,EnterNumber.class);
            }
        });

        mSocial = (TextView) findViewById(R.id.connectSocial);
        mSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(MobileVerification1.this,Social.class);
            }
        });

    }

    void startNewActivity(Activity one,Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }
}

