package com.example.seekm.studemts;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Trouble_Verification extends AppCompatActivity implements View.OnClickListener {






    public ImageButton back_btn_trouble_1;
    public Button resend_btn_trouble_1;
    public Button signin_btn_trouble_1;

    public TextView textView_trouble_1;

    public String Resend_String_Number_part1="Resend to ";
    public String Resend_String_Number_part2=" How would you like to receive your code?";

    public String mobile_number_code="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble__verification);

        mobile_number_code=getIntent().getStringExtra("mobile_number_1");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        back_btn_trouble_1=findViewById(R.id.back_btn_trouble_1);

        resend_btn_trouble_1=findViewById(R.id.resend_btn_trouble_1);

        signin_btn_trouble_1=findViewById(R.id.signin_btn_trouble);


        textView_trouble_1=findViewById(R.id.text_view_trouble_1);

        textView_trouble_1.setText(Resend_String_Number_part1+mobile_number_code+Resend_String_Number_part2);

        back_btn_trouble_1.setOnClickListener(this);

        resend_btn_trouble_1.setOnClickListener(this);

        signin_btn_trouble_1.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.back_btn_trouble_1:

                finishAffinity();

                startActivity(new Intent(Trouble_Verification.this,MobileV.class));
                break;


            case R.id.resend_btn_trouble_1:

                finishAffinity();


                startActivity(new Intent(Trouble_Verification.this,MobileVerification3.class).putExtra("mobile_number_resend",mobile_number_code));



                break;

            case R.id.signin_btn_trouble:

                finishAffinity();

                startActivity(new Intent(Trouble_Verification.this,UserForgotPassword.class));
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
