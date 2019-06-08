package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ForgotPassword extends AppCompatActivity {


    //VAR
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //changing status bar color dynamically
        //making button float
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (Build.VERSION.SDK_INT >=21)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //VIEWS
        mBack = (ImageView)findViewById(R.id.backBtn2);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(ForgotPassword.this,SigninWithPassword.class);
            }
        });
    }

    void startNewActivity(Activity one, Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }
}
