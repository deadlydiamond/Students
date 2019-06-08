package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Trouble extends AppCompatActivity {
    //VAR
    ImageView mBack;
    Button mResend, mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble);

        //VIEWS
        mBack = (ImageView)findViewById(R.id.backBtn2);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(Trouble.this,MobileVerification1.class);
            }
        });

        mSignin = (Button)findViewById(R.id.signinTroubleBtn);
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(Trouble.this,SigninWithPassword.class);
            }
        });

        mResend = (Button)findViewById(R.id.resendBtn);
        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(Trouble.this,MobileVerification2.class);
            }
        });

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    void startNewActivity(Activity one, Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }

}
