package com.afomic.sparkadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afomic.sparkadmin.data.Constant;
import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.model.AccessToken;
import com.afomic.sparkadmin.model.Admin;
import com.afomic.sparkadmin.util.GlideApp;
import com.afomic.sparkadmin.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.sign_up_with_email_view)
    LinearLayout emailSignUpView;

    @BindView(R.id.edt_access_token)
    EditText accessTokenEditText;

    @BindView(R.id.edt_email)
    EditText emailEditText;

    @BindView(R.id.edt_password)
    EditText passwordEditText;

    @BindView(R.id.edt_department_name)
    EditText departmentNameEditText;

    @BindView(R.id.edt_display_name)
    EditText displayNameEditText;

    @BindView(R.id.edt_association_name)
    EditText associationNameEditText;

    @BindView(R.id.imv_admin_photo)
    ImageView adminImage;
    ProgressDialog mProgressDialog;

    private boolean emailViewVisible=false;
    private PreferenceManager mPreferenceManager;
    private Uri imageUri;
    private  AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        mPreferenceManager=new PreferenceManager(this);
        int signUpType=getIntent().getIntExtra(Constant.EXTRA_SIGN_UP_TYPE,0);
        if(signUpType==Constant.TYPE_MAIL_AND_PASSWORD){
            emailSignUpView.setVisibility(View.VISIBLE);
            emailViewVisible=true;
        }

        mProgressDialog =new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }
    @OnClick(R.id.fab_image)
    public void selectImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .start(this);

    }
    @OnClick(R.id.btn_sign_up)
    public void signUpUser(){
        if(isValidEntry()){
            final String accessToken=Util.getString(accessTokenEditText);
            mProgressDialog.setMessage("checking Access Token..");
            mProgressDialog.show();
            FirebaseDatabase.getInstance()
                    .getReference("access")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(accessToken)){
                                token=dataSnapshot.child(accessToken)
                                        .getValue(AccessToken.class);
                                if(token!=null&&token.isUsed()){
                                    departmentNameEditText.setText(token.getDepartmentName());
                                    associationNameEditText.setText(token.getAssociationName());
                                }else if(token!=null) {
                                    token.setUsed(true);
                                    token.setDepartmentName(Util.getString(departmentNameEditText));
                                    token.setAssociationName(Util.getString(associationNameEditText));
                                    FirebaseDatabase.getInstance()
                                            .getReference("access")
                                            .child(token.getId())
                                            .setValue(token);
                                }
                                if(emailViewVisible){
                                   signUpWithEmail();
                                }else {
                                    setUpProfile();
                                }

                            }else {
                                mProgressDialog.dismiss();
                                Util.makeToast(SignUpActivity.this,"Invalid Access Token");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
    }
    public boolean isValidEntry(){
        if(emailViewVisible&& Util.isEmpty(emailEditText)){
            Util.makeToast(SignUpActivity.this,"Email Cannot be Empty");
            return false;
        }
        if(emailViewVisible&& Util.isEmpty(passwordEditText)){
            Util.makeToast(SignUpActivity.this,"Password Cannot be Empty");
            return false;
        }
        if( Util.isEmpty(accessTokenEditText)){
            Util.makeToast(SignUpActivity.this,"Provide your Access token");
            return false;
        }
        if( Util.isEmpty(departmentNameEditText)){
            Util.makeToast(SignUpActivity.this,"Provide your department Name ");
            return false;
        }
        if( Util.isEmpty(associationNameEditText)){
            Util.makeToast(SignUpActivity.this,"Provide your Association nae");
            return false;
        }
        return true;

    }
    public void signUpWithEmail(){
        mProgressDialog.setMessage("Creating profile...");
        String email=Util.getString(emailEditText);
        String password=Util.getString(passwordEditText);
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId=task.getResult().getUser().getUid();
                            mPreferenceManager.setUserId(userId);
                            mPreferenceManager.setUserLogin(true);
                            mPreferenceManager.setAdmin(true);
                            mPreferenceManager.setUsername(Util.getString(displayNameEditText));
                            mPreferenceManager.setAssociationName(Util.getString(associationNameEditText));
                            setUpProfile();
                        }else {
                            Util.makeToast(SignUpActivity.this,"Invalid email");
                        }

                    }
                });
    }
    public void setUpProfile(){
        String displayName=Util.getString(displayNameEditText);
        String associationName=Util.getString(associationNameEditText);
        final Admin newAdmin =new Admin();
        newAdmin.setAssociationName(associationName);
        newAdmin.setDisplayName(displayName);
        if(imageUri!=null){
            mProgressDialog.setMessage("Uploading Picture...");
            UploadTask adminPicUpload = FirebaseStorage.getInstance()
                    .getReference("admin")
                    .child(imageUri.getLastPathSegment())
                    .putFile(imageUri);
            adminPicUpload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        String pictureUrl=task.getResult().getDownloadUrl().toString();
                        mPreferenceManager.setIconUrl(pictureUrl);
                        newAdmin.setPictureUrl(pictureUrl);
                        saveAdmin(newAdmin);
                    }


                }
            });


        }else {
            saveAdmin(newAdmin);
        }



    }
    public void saveAdmin(Admin admin){
        FirebaseDatabase.getInstance()
                .getReference("admin")
                .child(mPreferenceManager.getUserId())
                .setValue(admin)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            showMainActivity();
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                GlideApp.with(SignUpActivity.this)
                        .load(imageUri)
                        .into(adminImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void postAccessToken(){
        DatabaseReference dataRef=FirebaseDatabase.getInstance()
                .getReference("access");
        for(int i=0;i<40;i++){
            String id=Util.getSaltString();
            AccessToken token=new AccessToken();
            token.setUsed(false);
            token.setId(id);
            dataRef.child(id)
                    .setValue(token);

        }
    }
    public void showMainActivity(){
        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
