package com.example.seekm.studemts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SocialVerification extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    public int flag = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CallbackManager callbackManager;
    LoginButton loginButton;
    private ProgressBar progressBar_Social;
    private ImageButton facebook_button;
    private ImageButton google_button;
    private SignInButton signInButton;
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageButton back_button;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_verification);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21)
            window.setStatusBarColor(getResources().getColor(R.color.colorSocial));

        //locking orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        facebook_button = findViewById(R.id.fb_login_btn);
        google_button = findViewById(R.id.google_login_btn);

        progressBar_Social = findViewById(R.id.progressBar_social);

        facebook_button.setOnClickListener(this);

        signInButton = findViewById(R.id.google_signin_button);
        google_button.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        back_button = findViewById(R.id.back_btn_social_1);
        back_button.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbSignIn();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void fbSignIn() {
        flag = 1;
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SocialVerification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR_EDMT", "" + e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String email23 = authResult.getUser().getEmail();

                progressBar_Social.setVisibility(View.GONE);
                FirebaseUser user = mAuth.getCurrentUser();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String User_uid1 = currentFirebaseUser.getUid();

                DocumentReference docRef = db.collection("Tutors").document(User_uid1);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                progressBar_Social.setVisibility(View.GONE);

                                finishAffinity();
                                startActivity(new Intent(SocialVerification.this, Drawer.class));
                            } else {

                                progressBar_Social.setVisibility(View.GONE);

                                finishAffinity();
                                startActivity(new Intent(SocialVerification.this, NextActivity.class));
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

                finishAffinity();
                startActivity(new Intent(SocialVerification.this, NextActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);
    }

    // [START o1nactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressBar_Social.setVisibility(View.VISIBLE);

        if (flag == 0) {

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    // [START_EXCLUDE]
                    //   updateUI(null);
                    // [END_EXCLUDE]
                }
            }
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String User_uid1 = currentFirebaseUser.getUid();

                            DocumentReference docRef = db.collection("Students").document(User_uid1);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {

                                            progressBar_Social.setVisibility(View.GONE);

                                            finishAffinity();
                                            startActivity(new Intent(SocialVerification.this, Drawer.class));
                                        } else {

                                            progressBar_Social.setVisibility(View.GONE);

                                            finishAffinity();
                                            startActivity(new Intent(SocialVerification.this, NextActivity.class));
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                            //    updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {

        flag = 0;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_btn_social_1:
                finish();
                startActivity(new Intent(SocialVerification.this, MobileV.class));
                break;

            case R.id.fb_login_btn:

                loginButton.performClick();

                break;

            case R.id.google_login_btn:

                signIn();

                //  signInButton.performClick();

                break;
        }
    }
}




