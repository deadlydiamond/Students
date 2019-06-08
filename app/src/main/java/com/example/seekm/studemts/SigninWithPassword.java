package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SigninWithPassword extends AppCompatActivity {

    //VAR
    ImageView mBack;
    TextView mForgot, mNoAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin_with_password);

        //changing status bar color dynamically
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >=21)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //VIEWS
        mBack = (ImageView)findViewById(R.id.backBtn2);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SigninWithPassword.this,Trouble.class);
            }
        });

        mForgot = (TextView)findViewById(R.id.forgotBtn);
        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SigninWithPassword.this,ForgotPassword.class);
            }
        });

        mNoAccount = (TextView)findViewById(R.id.noAccountBtn);
        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SigninWithPassword.this,MobileVerification2.class);
            }
        });



    }
    void startNewActivity(Activity one, Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }

}
