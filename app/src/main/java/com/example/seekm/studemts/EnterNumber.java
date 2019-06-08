package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterNumber extends AppCompatActivity {

    //VARS
    EditText mNumber;
    ImageView mBack;
    TextView mForgot,mNoAccount;
    FloatingActionButton mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);

        //changing status bar color dynamically
        Window window = getWindow();
//        //making button float
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (Build.VERSION.SDK_INT >=21)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Views
        mNext = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(EnterNumber.this,MobileVerification2.class);
            }
        });

        mNumber = (EditText)findViewById(R.id.number);

        mForgot = (TextView) findViewById(R.id.forgotBtn);
        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(EnterNumber.this,ForgotPassword.class);
            }
        });

        mNoAccount = (TextView)findViewById(R.id.noAccountBtn);
        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(EnterNumber.this,MobileVerification2.class);
            }
        });

        mBack = (ImageView)findViewById(R.id.backBtn2);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(EnterNumber.this,MobileVerification1.class);
            }
        });

    }

    void startNewActivity(Activity one, Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }
}

