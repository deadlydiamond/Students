package com.example.seekm.studemts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileVerification3 extends AppCompatActivity implements View.OnClickListener  {









    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    public String Trouble= "Ugh! Not getting the code!";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
   // private static final int STATE_SIGNIN_FAILED = 5;
   // private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;
    private TextView mSocial;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;

    public TextView text_view_mb3_2;
    public TextView text_view_mb3_3;

    public EditText veri_code_1;
    public EditText veri_code_2;
    public EditText veri_code_3;
    public EditText veri_code_4;
    public EditText veri_code_5;
    public EditText veri_code_6;

    private ImageView back_btn_mb_3;
    private FloatingActionButton button1_floating;


    public String Rcvd_String;
    public String PAKISTAN_DIALING_NUMBER="+92";

    public String Mobile_Number;


    public String Mobile_Number_jugaar;
    public String part1;

    public ProgressBar progressBar_3;


    public TextView trouble_mb_ver_1;
    public TextView wrong_code_notifier;




    int seconds,minutes;

    private static final String FORMAT="%02d:%02d";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification3);


        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        progressBar_3=findViewById(R.id.progressBar_3);




        veri_code_1=findViewById(R.id.veri_code_1);
        veri_code_2=findViewById(R.id.veri_code_2);
        veri_code_3=findViewById(R.id.veri_code_3);
        veri_code_4=findViewById(R.id.veri_code_4);
        veri_code_5=findViewById(R.id.veri_code_5);
        veri_code_6=findViewById(R.id.veri_code_6);

        trouble_mb_ver_1=findViewById(R.id.trouble_mb_ver_1);
        trouble_mb_ver_1.setOnClickListener(this);

        wrong_code_notifier=findViewById(R.id.wrong_code_notifier);

        button1_floating=findViewById(R.id.floatingActionButton_mb_3);
        button1_floating.setOnClickListener(this);






        veri_code_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_1.length()==1){

                    wrong_code_notifier.setText("");

                    veri_code_2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        veri_code_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_2.length()==1){

                    wrong_code_notifier.setText("");

                    veri_code_3.requestFocus();

                }

                if(veri_code_2.length()==0){

                    veri_code_1.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        veri_code_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    if(veri_code_2.getText().length()==0) {

                        veri_code_1.requestFocus();
                    }
                }
                return false;

            }
        });



        veri_code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_3.length()==1){

                    wrong_code_notifier.setText("");

                    veri_code_4.requestFocus();
                }

                if(veri_code_3.length()==0){

                    veri_code_2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        veri_code_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    if(veri_code_3.getText().length()==0) {

                        veri_code_2.requestFocus();
                    }
                }
                return false;

            }
        });





        veri_code_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_4.length()==1){

                    wrong_code_notifier.setText("");

                    veri_code_5.requestFocus();
                }
                if(veri_code_4.length()==0){

                    veri_code_3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        veri_code_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    if(veri_code_4.getText().length()==0) {

                        veri_code_3.requestFocus();
                    }
                }
                return false;

            }
        });



        veri_code_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_5.length()==1){

                    wrong_code_notifier.setText("");

                    veri_code_6.requestFocus();
                }

                if(veri_code_5.length()==0){

                    veri_code_4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        veri_code_5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    if(veri_code_5.getText().length()==0) {

                        veri_code_4.requestFocus();
                    }
                }
                return false;

            }
        });








        veri_code_6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(veri_code_6.length()==1){

                    wrong_code_notifier.setText("");

                    button1_floating.setVisibility(View.VISIBLE);
                    button1_floating.performClick();

                }
                if(veri_code_6.length()==0){

                    veri_code_5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        veri_code_6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    if(veri_code_6.getText().length()==0) {

                        veri_code_5.requestFocus();
                    }
                }
                return false;

            }
        });












        back_btn_mb_3=findViewById(R.id.back_btn_mb_3);
        back_btn_mb_3.setOnClickListener(this);







        mAuth = FirebaseAuth.getInstance();














        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verificatlogin_buttonion. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
             //   updateUI(STATE_VERIFY_SUCCESS, credential);

                if (credential != null) {
                    if (credential.getSmsCode() != null) {


                        veri_code_1.setText(String.valueOf(credential.getSmsCode().charAt(0)));
                        veri_code_2.setText(String.valueOf(credential.getSmsCode().charAt(1)));
                        veri_code_3.setText(String.valueOf(credential.getSmsCode().charAt(2)));
                        veri_code_4.setText(String.valueOf(credential.getSmsCode().charAt(3)));
                        veri_code_5.setText(String.valueOf(credential.getSmsCode().charAt(4)));
                        veri_code_6.setText(String.valueOf(credential.getSmsCode().charAt(5)));






                    } else {
                      //  veri_code_1.setText(R.string.instant_validation);
                    }
                }
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]

                    progressBar_3.setVisibility(View.GONE);

                    wrong_code_notifier.setText("Invalid phone number.");

                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
               // updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();



        if(getIntent().getStringExtra("mobile_number")!=null){

            Rcvd_String=getIntent().getStringExtra("mobile_number");

            Mobile_Number = PAKISTAN_DIALING_NUMBER+Rcvd_String;

            startPhoneNumberVerification(Mobile_Number);
            part1="";
            for(int i = 1 ; i < Rcvd_String.length();i++){


                if(i==4){

                    part1+=" ";
                }



                if(i<11){
                    part1 += Rcvd_String.charAt(i);

                }

            }
            part1+=".";

            text_view_mb3_2=findViewById(R.id.text_view_mb3_2);


            text_view_mb3_2.setText(part1);




        }



        if(getIntent().getStringExtra("mobile_number_resend")!=null){

            Rcvd_String=getIntent().getStringExtra("mobile_number_resend");

            Mobile_Number = Rcvd_String;

            startPhoneNumberVerification(Mobile_Number);

            part1="";
            for(int i = 4 ; i < Rcvd_String.length();i++){


                if(i==7){

                    part1+=" ";
                }



                if(i<14){
                    part1 += Rcvd_String.charAt(i);

                }

            }
            part1+=".";

            text_view_mb3_2=findViewById(R.id.text_view_mb3_2);


            text_view_mb3_2.setText(part1);



        }







        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                trouble_mb_ver_1.setClickable(false);
                trouble_mb_ver_1.setText(""+String.format(FORMAT,TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                trouble_mb_ver_1.setText(Trouble);
                trouble_mb_ver_1.setClickable(true);
            }
        }.start();











    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }




    private void startPhoneNumberVerification(String phoneNumber) {


        Mobile_Number_jugaar=phoneNumber;

        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
//    private void resendVerificationCode(String phoneNumber,
//                                        PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks,         // OnVerificationStateChangedCallbacks
//                token);             // ForceResendingToken from callbacks
//    }
    // [END resend_verification]

    // [START sign_in_with_phone]


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");




                            //Activity delay function
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    progressBar_3.setVisibility(View.GONE);

                                    FirebaseUser user = task.getResult().getUser();

                                    finishAffinity();
                                    startActivity(new Intent(MobileVerification3.this,NextActivity.class));


                                }
                            }, 3000);


                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                progressBar_3.setVisibility(View.GONE);


                                veri_code_1.setText("");
                                veri_code_2.setText("");
                                veri_code_3.setText("");
                                veri_code_4.setText("");
                                veri_code_5.setText("");
                                veri_code_6.setText("");
                                veri_code_1.requestFocus();

                                button1_floating.setVisibility(View.GONE);



                                wrong_code_notifier.setText("The SMS passcode you've entered is incorrect.");


                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                         //   updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]
//
//    private void signOut() {
//        mAuth.signOut();
//        updateUI(STATE_INITIALIZED);
//    }

  //  private void updateUI(int uiState) {
       // updateUI(uiState, mAuth.getCurrentUser(), null);
   // }

  //  private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            updateUI(STATE_SIGNIN_SUCCESS, user);
//        } else {
//            updateUI(STATE_INITIALIZED);
//        }
 //   }

//    private void updateUI(int uiState, FirebaseUser user) {
//        //updateUI(uiState, user, null);
//    }
//
//    private void updateUI(int uiState, PhoneAuthCredential cred) {
//        // updateUI(uiState, null, cred);
//    }

//    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
//
//        switch (uiState) {
//            case STATE_INITIALIZED:
//                // Initialized state, show only the phone number field and start button
//                enableViews(mStartButton, mPhoneNumberField);
//                disableViews(mVerifyButton, mResendButton, mVerificationField);
//                mDetailText.setText(null);
//                break;
//            case STATE_CODE_SENT:
//                // Code sent state, show the verification field, the
//                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
//                disableViews(mStartButton);
//                mDetailText.setText(R.string.status_code_sent);
//                break;
//            case STATE_VERIFY_FAILED:
//                // Verification has failed, show all options
//                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
//                        mVerificationField);
//                mDetailText.setText(R.string.status_verification_failed);
//                break;
//            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
//                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
//                        mVerificationField);
//                mDetailText.setText(R.string.status_verification_succeeded);

                // Set the verification text based on the credential
//                if (cred != null) {
//                    if (cred.getSmsCode() != null) {
//                        mVerificationField.setText(cred.getSmsCode());
//                    } else {
//                        mVerificationField.setText(R.string.instant_validation);
//                    }
//                }

             //  / break;
//            case STATE_SIGNIN_FAILED:
//                // No-op, handled by sign-in check
//                mDetailText.setText(R.string.status_sign_in_failed);
//                break;
//            case STATE_SIGNIN_SUCCESS:
//                // Np-op, handled by sign-in check
//                break;
       // }

//        if (user == null) {
//            // Signed out
//            mPhoneNumberViews.setVisibility(View.VISIBLE);
//            mSignedInViews.setVisibility(View.GONE);
//
//            mStatusText.setText(R.string.signed_out);
//        } else {
//            // Signed in
//            mPhoneNumberViews.setVisibility(View.GONE);
//            mSignedInViews.setVisibility(View.VISIBLE);
//
//            enableViews(mPhoneNumberField, mVerificationField);
//            mPhoneNumberField.setText(null);
//            mVerificationField.setText(null);
//
//            mStatusText.setText(R.string.signed_in);
//
//        }
    //}

//    private boolean validatePhoneNumber() {
//        String phoneNumber = mPhoneNumberField.getText().toString();
//        if (TextUtils.isEmpty(phoneNumber)) {
//            mPhoneNumberField.setError("Invalid phone number.");
//
//            return false;
//        }
//
//        return true;
//    }

//    private void enableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(true);
//        }
//    }
//
//    private void disableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(false);
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.buttonStartVerification:
//                if (!validatePhoneNumber()) {
//                    return;
//                }

              //  startPhoneNumberVerification(mPhoneNumberField.getText().toString());
             //   break;

            case R.id.floatingActionButton_mb_3:
                String code = veri_code_1.getText().toString() + veri_code_2.getText().toString() + veri_code_3.getText().toString() + veri_code_4.getText().toString() + veri_code_5.getText().toString() + veri_code_6.getText().toString();

                progressBar_3.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(code)) {

                    progressBar_3.setVisibility(View.GONE);
                    wrong_code_notifier.setText("Please enter your verification code");


                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;

            case R.id.back_btn_mb_3:

                finish();
                startActivity(new Intent(MobileVerification3.this,MobileVerification2.class));
                finish();

                break;

            case R.id.trouble_mb_ver_1:

                finishAfterTransition();
                startActivity(new Intent(MobileVerification3.this,Trouble_Verification.class).putExtra("mobile_number_1",Mobile_Number_jugaar));
                break;



        }
    }

}
