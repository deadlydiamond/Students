package com.example.seekm.studemts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProfileBuilder extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences Profile_preferences ;

    EditText Education_Board;
    EditText Class_Grade;
    EditText School_private;
    EditText Field_OfStudy;
    EditText Latest_Quaification;

    int Checker=0;

    String education_board;
    String class_grade;
    String school_Private;
    String field_Study;
    String latest_Qualification;
    FloatingActionButton floatingActionButton_profile_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_builder);

        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens",0);


        Education_Board=findViewById(R.id.education_board_field);
        Class_Grade=findViewById(R.id.class_grade_field);
        School_private=findViewById(R.id.school_field);
        Field_OfStudy=findViewById(R.id.Field_of_study);
        Latest_Quaification=findViewById(R.id.Latest_Qualification);

        floatingActionButton_profile_builder=findViewById(R.id.floating_button_profile);

        floatingActionButton_profile_builder.setOnClickListener(this);



        //Education_Board.setText(Profile_preferences.getString("First_Name",null));
        //Class_Grade.setText(Profile_preferences.getString("Profile_Image_Url",null));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.floating_button_profile:

                validationData();


                break;

        }
    }

    private void validationData() {
    education_board=Education_Board.getText().toString();
    class_grade=Class_Grade.getText().toString();
    school_Private=School_private.getText().toString();
    field_Study=Field_OfStudy.getText().toString();
    latest_Qualification=Latest_Quaification.getText().toString();

    Checker=0;

        Checker+= CheckError(education_board,Education_Board,"Education Board");
        Checker+=CheckError(class_grade,Class_Grade,"Class/Grade");
        Checker+= CheckError(school_Private,School_private,"School Information");
        Checker+= CheckError(field_Study,Field_OfStudy,"Field of Study");
        Checker+=  CheckError(latest_Qualification,Latest_Quaification,"Highest Qualification");


        if(Checker==5){

            SharedPreferences.Editor editor = Profile_preferences.edit();
            editor.putString("Education_Board",education_board);
            editor.putString("Class_Grade",class_grade);
            editor.putString("School_private",school_Private);
            editor.putString("Field_OfStudy",field_Study);
            editor.putString("Latest_Qualification",latest_Qualification);
            editor.apply();

            startActivity(new Intent(ProfileBuilder.this, MapsActivity.class));




        }



    }

    private int CheckError(String String1, EditText EditText1,String String2) {

        if(String1.length()<4){

            EditText1.setError( String2+ " is required.");
            EditText1.requestFocus();


            return 0;
        }

        return 1;
    }

}
