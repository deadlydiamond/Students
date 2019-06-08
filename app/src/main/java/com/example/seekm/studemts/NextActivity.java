package com.example.seekm.studemts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

public class NextActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressBar p1;
    FloatingActionButton signup_float;

    int Image_upload_checker=0;

    SharedPreferences Profile_preferences ;

    public int Next_Activity=0;

    EditText First_name;
    EditText Last_name;
    ImageButton add;

    EditText Email;

    EditText signup_password;
    EditText Confirm_password;

    EditText DateOfBirth;

    RadioGroup radioGroup_gender;

    RadioButton radioButton_male;
    RadioButton radioButton_female;

    RadioButton gender_1;

    ImageView signup_profile_image;
    String ProfileImageUrl=null;
    String ProfileUrl=null;
    Uri uriProfileImage;

    String first_name = "";
    String last_name = "";

    String email = "";

    String dateOfBirth = null;

    String user_password = "";
    String confirm_password = "";

    String Gender="";

    private static final int CHOOSE_IMAGE = 101;

    StorageReference profileImageRef;




    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);


        p1=findViewById(R.id.next_progres_bar);
        First_name = findViewById(R.id.name);




        Last_name = findViewById(R.id.emaill);

        Email = findViewById(R.id.qualificationn);


        signup_password = findViewById(R.id.genderr);

        Confirm_password = findViewById(R.id.boardd);

        DateOfBirth = findViewById(R.id.dobb);
        DateOfBirth.setLongClickable(false);

        radioGroup_gender = findViewById(R.id.radio_group_gender);

        radioButton_male = findViewById(R.id.radio_btn_male);
        radioButton_female = findViewById(R.id.radio_btn_female);

        signup_float = findViewById(R.id.floating_action_btn_signup_form);

        signup_float.setOnClickListener(this);

        signup_profile_image=findViewById(R.id.sign_up_image_button);

        signup_profile_image.setOnClickListener(this);


        DateOfBirth.setShowSoftInputOnFocus(false);

        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateDialog dialog = new DateDialog((v));


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");


            }
        });

        DateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                DateOfBirth.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




//        SharedPreferences signup_data
    }


    public void onStart() {
        super.onStart();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.floating_action_btn_signup_form:

                TakeUserData();

                break;
            case R.id.sign_up_image_button:

                showImageChooser();
                break;

        }
    }

    private void showImageChooser() {

        Intent  intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            Image_upload_checker=1;
            add = (ImageButton)findViewById(R.id.addButton);
            add.setVisibility(View.GONE);
            uriProfileImage  = data.getData();
            signup_profile_image.setImageDrawable(null);
            signup_profile_image.setBackgroundResource(0);
            signup_profile_image.setImageURI(uriProfileImage);
        }
    }

    private void TakeUserData() {


        first_name = First_name.getText().toString();
        last_name = Last_name.getText().toString();
        email = Email.getText().toString();
        user_password = signup_password.getText().toString();
        confirm_password = Confirm_password.getText().toString();
        dateOfBirth = DateOfBirth.getText().toString();

        int selectId =radioGroup_gender.getCheckedRadioButtonId();

        gender_1=findViewById(selectId);

        Gender = gender_1.getText().toString();


        Next_Activity+= validateName(first_name, First_name,"First Name");
        Next_Activity+= validateName(last_name, Last_name,"Last Name");

        Next_Activity+= validateEmail(email,Email);

        Next_Activity+= validatePassword(user_password,signup_password,confirm_password,Confirm_password);

        Next_Activity+= validateDateOfbirth(dateOfBirth,DateOfBirth);

        if(Next_Activity==5){


        p1.setVisibility(View.VISIBLE);
            


            Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens",0);


            if(Image_upload_checker==1){

                uploadImageToFirebase();


            }
            else
            {

                dothisNow();
            }






        }

        else {

            Next_Activity=0;
        }


    }

    private void uploadImageToFirebase() {



        profileImageRef=FirebaseStorage.getInstance().getReference().child("ProfilePicture/"+System.currentTimeMillis()+".jpg");

        if(uriProfileImage!=null){


            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ProfileImageUrl=uri.toString();
                            ProfileUrl=ProfileImageUrl;

                            dothisNow();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

        
    }

    private void dothisNow() {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {


                SharedPreferences.Editor editor = Profile_preferences.edit();
                editor.putString("First_Name",first_name);
                editor.putString("Last_Name",last_name);
                editor.putString("Email",email);
                editor.putString("Password",user_password);
                editor.putString("Date_Of_Birth",dateOfBirth);
                editor.putString("Gender",Gender);
                editor.putString("Profile_Image_Url",ProfileUrl);
                editor.apply();
                p1.setVisibility(View.GONE);


                GoToProfileBuilder();
//            }
//        }, 10000);
    }

    private void GoToProfileBuilder() {

        startActivity(new Intent(NextActivity.this,ProfileBuilder.class));
    }

    private int validateDateOfbirth(String dateOfBirth, EditText dateOfBirth1) {



        if(dateOfBirth.length()==0){

            dateOfBirth1.setError("Date Of Birth is required");
            dateOfBirth1.requestFocus();

            return 0;
        }

        return 1;

    }

    private int validatePassword(String user_password, EditText signup_password, String confirm_password, EditText confirm_password1) {


        if(!user_password.matches("^(?:(?=.*[a-z])(?:(?=.*[A-Z])(?=.*[\\d\\W])|(?=.*\\W)(?=.*\\d))|(?=.*\\W)(?=.*[A-Z])(?=.*\\d)).{8,}$")){

            signup_password.setError("Password must contain A Capital letter , A number ");
            signup_password.requestFocus();

            return 0;

        }

        if(user_password.length()==0){

            signup_password.setError("Password is required");
            signup_password.requestFocus();

            return 0;
        }


        if(confirm_password.length()==0){

            confirm_password1.setError("Enter password here too");
            confirm_password1.requestFocus();

            return 0;
        }

        if(!confirm_password.equals(user_password)){

            signup_password.setError("Both Passwords must match");
            signup_password.requestFocus();
            confirm_password1.setError("Both Passwords must match");
            confirm_password1.requestFocus();

            return 0;
        }

        return 1;

    }

    private int validateEmail(String email, EditText email1) {

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            email1.setError("Enter Valid Email");
            email1.requestFocus();

            return 0;

        }

        if(email.length() == 0){

            email1.setError("Email can not be empty");

            return 0;

        }

        return 1;

    }

    private int  validateName(String AnyName, EditText AnyEditText , String Datatypes) {

        if (!AnyName.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$") || AnyName.length()< 2) {

            AnyEditText.setError("Please Enter Valid Name");
            AnyEditText.requestFocus();

            return 0;



        }

        if (AnyName.length() == 0) {

            AnyEditText.setError(Datatypes + " is Required");
            AnyEditText.requestFocus();

            return 0;

        }

        return 1;

    }



}