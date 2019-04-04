package com.example.seekm.studemts;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class UserForgotPassword extends AppCompatActivity implements View.OnClickListener{

    public FloatingActionButton floatingActionButton_forgot_pwd;
    public ImageButton back_btn_forgot_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_password);

        //changing status bar color dynamically
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >=21)
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        floatingActionButton_forgot_pwd=findViewById(R.id.floating_btn_forgot_pwd_1);
        back_btn_forgot_pwd=findViewById(R.id.back_btn__forgot_pwd);

        floatingActionButton_forgot_pwd.setOnClickListener(this);
        back_btn_forgot_pwd.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.floating_btn_forgot_pwd_1:

                finishAffinity();
                startActivity(new Intent(UserForgotPassword.this,NextActivity.class));

                break;

            case R.id.back_btn__forgot_pwd:

                finishAffinity();
                startActivity(new Intent(UserForgotPassword.this,MobileV.class));


                break;

        }

    }
}
