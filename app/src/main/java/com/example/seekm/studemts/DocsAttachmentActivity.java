package com.example.seekm.studemts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



import java.io.File;

public class DocsAttachmentActivity extends AppCompatActivity {

    StorageReference myRef;
    SharedPreferences Profile_preferences ;

    FirebaseDatabase myDB;


    ImageButton uploadButton;

    Button pause_button;
    Button  cancel_button_1;
    TextView myLabel;
    ProgressBar myprogressBar;
    TextView sizelabel;
    TextView current_progress;
    StorageTask myStorageTask;
    FloatingActionButton float_button1;


    int document_count = 0;

    UploadTask uploadTask;

    TextView File_type;


    ImageView signup_profile_image;
    String ProfileImageUrl=null;
    String ProfileUrl=null;
    Uri uriProfileImage;
    private static final int CHOOSE_IMAGE = 101;

    int checker=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_attachment);

        myRef =FirebaseStorage.getInstance().getReference();

        uploadButton=findViewById(R.id.choose_image_btn_doc);
        pause_button=findViewById(R.id.pause_upload_btn);
        cancel_button_1=findViewById(R.id.cancel_upload_btn);
        sizelabel=findViewById(R.id.size_label);
        current_progress=findViewById(R.id.progress_label);
        myprogressBar=findViewById(R.id.progress_bar_docs);
        File_type=findViewById(R.id.File_type);

        float_button1=findViewById(R.id.floating_action_btn_doc_attach);

        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens",0);



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText=pause_button.getText().toString();
                if(btnText.equals("PAUSE UPLOAD")){

                    myStorageTask.pause();
                    pause_button.setText("RESUME UPLOAD");
                }
                else{

                    myStorageTask.resume();
                    pause_button.setText("PAUSE UPLOAD");
                }
            }
        });

        cancel_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStorageTask.cancel();
            }
        });

        float_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checker==0){




                    startActivity(new Intent(DocsAttachmentActivity.this,MapsActivity.class));
                }
            }
        });

    }


    private void showImageChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            checker = 1;

            //Image_upload_checker=1;
            uriProfileImage  = data.getData();
//            signup_profile_image.setImageDrawable(null);
//            signup_profile_image.setBackgroundResource(0);
//            signup_profile_image.setImageURI(uriProfileImage);

            String uriString=uriProfileImage.toString();

            File myFile= new File((uriString));
            String displayName=null;
            if(uriString.startsWith("content://"))
            {
                Cursor cursor = null;

                try{

                    cursor=DocsAttachmentActivity.this.getContentResolver().query(uriProfileImage,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){

                        displayName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
                finally {

                    cursor.close();
                }



            }
            else if(uriString.startsWith("file://")){

                displayName=myFile.getName();
            }

            File_type.setText(displayName);


            myRef=FirebaseStorage.getInstance().getReference().child("User-Documents/"+displayName+".jpg");


            if(uriProfileImage!=null){


             myStorageTask=   myRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                document_count+=1;

                                ProfileImageUrl=uri.toString();
                                ProfileUrl=ProfileImageUrl;
                                checker = 0;


                                SharedPreferences.Editor editor = Profile_preferences.edit();
                                editor.putString("Attached-Document_"+document_count,ProfileUrl);
                                editor.apply();

                                //   dothisNow();



                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        myprogressBar.setProgress((int)progress);
                        String progressText=taskSnapshot.getBytesTransferred()/1024+"KB/"+taskSnapshot.getTotalByteCount()/1024+"KB";
                        sizelabel.setText(progressText);
                        current_progress.setText((int)progress+"%");


                    }
                })
                ;
            }




        }
    }
}
