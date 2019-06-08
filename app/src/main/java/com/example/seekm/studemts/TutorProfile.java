package com.example.seekm.studemts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class TutorProfile extends AppCompatActivity {

    TextView fullName, email, gender, qualification, dob, board;
    String fname, lname ,user_id;
    Button request, cancel ,decline;
    String myUID;
    DatabaseReference mfriendReqReference;
    DatabaseReference mDatabaseReference;
    DatabaseReference mFriendDatabase;
    DatabaseReference mNotificationReference;
    DatabaseReference mRootReference;
    DatabaseReference getmDatabaseReference;
    private String mCurrent_state;


    FirebaseUser mFirebaseUser;

    private String TAG = "TUTORS_PROFILE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        fullName = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.emaill);
        gender= (TextView) findViewById(R.id.genderr);
        qualification = (TextView) findViewById(R.id.qualificationn);
        dob = (TextView) findViewById(R.id.dobb);
        board = (TextView) findViewById(R.id.boardd);

        cancel = (Button)findViewById(R.id.btn_cancel);
        request = (Button)findViewById(R.id.btn_request);
        decline = (Button)findViewById(R.id.profileDeclineReqButton23);
        Intent intent = getIntent();
        myUID = intent.getStringExtra("uid");
        user_id =  intent.getStringExtra("uid");
        Toast.makeText(TutorProfile.this, myUID,Toast.LENGTH_LONG).show();
        decline.setVisibility(View.INVISIBLE);
        decline.setEnabled(false);

        mfriendReqReference= FirebaseDatabase.getInstance().getReference().child("friend_request");
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(myUID);
        mFriendDatabase=FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference=FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootReference=FirebaseDatabase.getInstance().getReference();
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        mCurrent_state = "not_friends";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Tutors")
                .whereEqualTo("User_uid", myUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {

                                    fname = document.get("FirstName").toString();
                                    lname = document.get("LastName").toString();
                                    fullName.setText(fname + " " + lname);
                                    qualification.setText(document.get("LatestQualification").toString());
                                    board.setText(document.get("EducationBoard").toString());
                                    email.setText(document.get("EmailAddress").toString());
                                    gender.setText(document.get("Gender").toString());
                                    dob.setText(document.get("DateOfBirth").toString());
                                } catch (NullPointerException e) {
                                    Log.d(TAG, "onComplete: Exception" + e.getMessage());
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String display_name=dataSnapshot.child("name").getValue().toString();
//                String display_status=dataSnapshot.child("status").getValue().toString();
//                String display_image=dataSnapshot.child("image").getValue().toString();
//                mProfileName.setText(display_name);
//                mProfileStatus.setText(display_status);

                //----ADDING TOTAL  NO OF FRIENDS---
                mFriendDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long len = dataSnapshot.getChildrenCount();
                      //  mprofileFriendCount.setText("TOTAL FRIENDS : "+len);

                        //----SEEING THE FRIEND STATE OF THE USER---
                        //----ADDING THE TWO BUTTON-----
                        mfriendReqReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----
                                if(dataSnapshot.hasChild(user_id)){

                                    String request_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                                    if(request_type.equals("sent")){

                                        mCurrent_state="req_sent";

                                        request.setVisibility(View.INVISIBLE);
                                        request.setEnabled(false);

                                        decline.setVisibility(View.VISIBLE);
                                                decline.setEnabled(true);

                                    }




                                }

                                //---USER IS FRIEND----
                                else{

                                    mFriendDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            decline.setVisibility(View.INVISIBLE);
                                            decline.setEnabled(false);

                                            if(dataSnapshot.hasChild(user_id)){
                                                mCurrent_state="friends";
                                                request.setText("Unfriend This Person");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            //mProgressDialog.dismiss();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(TutorProfile.this, "Error fetching Friend request data", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                      //  mProgressDialog.dismiss();
                    }

                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   mProgressDialog.dismiss();
            }

        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfile.this,NearbyTutorsMapActivity.class);
                startActivity(intent);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setEnabled(false);
                if(mCurrent_state.equals("not_friends")){

                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(user_id).push();

                    String newNotificationId = newNotificationReference.getKey();

                    HashMap<String,String> notificationData=new HashMap<String, String>();
                    notificationData.put("from",mFirebaseUser.getUid());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+mFirebaseUser.getUid()+ "/"+user_id + "/request_type","sent");
                    requestMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid()+"/request_type","received");
                    requestMap.put("notifications/"+user_id+"/"+newNotificationId,notificationData);

                    //----FRIEND REQUEST IS SEND----
                    mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){

                                Toast.makeText(TutorProfile.this, "Friend Request sent successfully", Toast.LENGTH_SHORT).show();

                                request.setEnabled(false);
                                request.setVisibility(View.INVISIBLE);
                                decline.setVisibility(View.VISIBLE);
                                mCurrent_state= "req_sent";
                                decline.setEnabled(true);

                            }
                            else{
                                request.setEnabled(true);
                                Toast.makeText(TutorProfile.this, "Some error in sending friend Request", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                if(mCurrent_state.equals("req_sent")){

                    Map valueMap=new HashMap();
                    valueMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----FRIEND REQUEST IS CANCELLED----
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){

                                mCurrent_state = "not_friends";
                                request.setVisibility(View.VISIBLE);
                                request.setEnabled(true);
                                Toast.makeText(TutorProfile.this, "Friend Request Cancelled Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //m0ProfileSendReqButton.setEnabled(true);
                                Toast.makeText(TutorProfile.this, "Cannot cancel friend request...", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });




                }

                if(mCurrent_state.equals("friends")){

                    Map valueMap=new HashMap();
                    valueMap.put("friends/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friends/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----UNFRIENDED THE PERSON---
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                mCurrent_state = "not_friends";
                                request.setText("Send Friend Request");
                                request.setEnabled(true);
                                Toast.makeText(TutorProfile.this, "Successfully Unfriended...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                request.setEnabled(true);
                                Toast.makeText(TutorProfile.this, "Cannot Unfriend..Contact Kshitiz..", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });


                }

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
