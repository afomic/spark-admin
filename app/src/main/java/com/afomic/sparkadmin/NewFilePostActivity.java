package com.afomic.sparkadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afomic.sparkadmin.model.BlogPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewFilePostActivity extends AppCompatActivity {
    Spinner fileTypeSpinner;
    EditText fileTitleEditText;
    Button submitButton, selectFileButton;
    TextView fileNameTextView;
    int fileType=-1;
    String fileTitle;
    Uri fileUri;
    String fileUrl;
    StorageReference mFirebaseStorage;
    DatabaseReference mFirebaseDatabase;
    public static final int FILE_REQUEST_CODE=101;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file_post);

        fileTitleEditText=(EditText) findViewById(R.id.edt_file_title);
        fileTypeSpinner=(Spinner) findViewById(R.id.spn_file_type);
        submitButton=(Button) findViewById(R.id.btn_submit);
        selectFileButton=(Button) findViewById(R.id.btn_select_file);
        fileNameTextView=(TextView) findViewById(R.id.tv_file_name);

        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference().child("blog");
        mFirebaseStorage=FirebaseStorage.getInstance().getReference();

        fileTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileType=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileIntent=new Intent(Intent.ACTION_GET_CONTENT);
                startActivityForResult(fileIntent,FILE_REQUEST_CODE);
            }
        });

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Uploading file");
        mProgressDialog.setCancelable(false);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileTitle=fileTitleEditText.getText().toString();
                if(isValid()){
                    mProgressDialog.show();
                    StorageReference filePath= mFirebaseStorage.child("blog").child(fileUri.getLastPathSegment());
                    UploadTask fileTask=  filePath.putFile(fileUri);
                    fileTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(NewFilePostActivity.this,
                                    "Fail Upload Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("test")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            fileUrl=downloadUrl.toString();
                            BlogPost mPost=new BlogPost();
                            mPost.setTitle(fileUri.getLastPathSegment());
                            mPost.setFileType(fileType);
                            mPost.setBody(fileTitle);
                            mPost.setPosterName("President");
                            mPost.setFileUrl(fileUrl);
                            mPost.setType(BlogPost.Type.FILE);
                            uploadPost(mPost);
                            mProgressDialog.dismiss();
                            finish();
                        }
                    });

                }
            }
        });
    }
    public boolean isValid(){
        if(fileType==-1){
            Toast.makeText(this,"Select file type",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(fileTitle.equals("")){
            Toast.makeText(this,"Enter File Title",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(fileUri==null){
            Toast.makeText(this,"Select File to Upload",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FILE_REQUEST_CODE&&resultCode==RESULT_OK){
            fileUri=data.getData();
            fileNameTextView.setText(fileUri.getLastPathSegment());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadPost(BlogPost post){

       String pushID= mFirebaseDatabase.push().getKey();
        post.setId(pushID);
        mFirebaseDatabase.child(pushID).setValue(post);
        finish();
    }
}
