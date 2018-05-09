package com.afomic.sparkadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afomic.sparkadmin.model.Profile;
import com.afomic.sparkadmin.data.Constant;
import com.afomic.sparkadmin.util.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateProfileActivity extends AppCompatActivity {
    @BindView(R.id.exco_view)
    LinearLayout executiveLayout;

    @BindView(R.id.lecturer_view)
    LinearLayout lecturerLayout;

    @BindView(R.id.edt_lecturer_room_number)
    EditText lecturerRoomNumber;

    @BindView(R.id.edt_lecturer_specialization_area)
    EditText lecturerSpecializationArea;

    @BindView(R.id.edt_lecturer_degree)
    EditText lecturerDegree;

    @BindView(R.id.edt_name)
    EditText profileName;

    @BindView(R.id.edt_phone_number)
    EditText profilePhoneNumber;

    @BindView(R.id.edt_email)
    EditText profileEmail;

    @BindView(R.id.edt_executive_position)
    EditText executivePosition;

    @BindView(R.id.edt_executive_level)
    EditText executiveLevel;
    @BindView(R.id.imv_profile_image)
    ImageView profileImage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private int profileType;

    Uri imageUri;
    ProgressDialog mProgressDialog;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("New Profile");
        }

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Creating profile..");
        mProgressDialog.setCancelable(false);

        profileType=getIntent().getIntExtra(Constant.EXTRA_TYPE,0);
        if(profileType== Profile.Type.LECTURER){
            lecturerLayout.setVisibility(View.VISIBLE);
            executiveLayout.setVisibility(View.GONE);
        }
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("profile/nacoss");
        mStorageReference= FirebaseStorage.getInstance().getReference("profile/nacoss");

    }
    @OnClick(R.id.fab_image)
    public void selectImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .start(this);

    }
    public boolean isValidForm(){
        if(isEmpty(profileEmail)){
            Toast.makeText(CreateProfileActivity.this,
                    "Email cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isEmpty(profilePhoneNumber)){
            Toast.makeText(CreateProfileActivity.this,
                    "Phone Number cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(getString(profilePhoneNumber).length()<10){
            Toast.makeText(CreateProfileActivity.this,
                    "Phone Number must be more than 10 digit",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isEmpty(profileName)){
            Toast.makeText(CreateProfileActivity.this,
                    "Full name cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public boolean isEmpty(EditText edt){
        return TextUtils.isEmpty(getString(edt));
    }
    public String getString(EditText edt){
        return edt.getText().toString();
    }

    public Profile getProfile(){
        Profile profile=new Profile();
        profile.setName(getString(profileName));
        profile.setTelephoneNumber(getString(profilePhoneNumber));
        profile.setEmail(getString(profileEmail));
        profile.setType(profileType);
        profile.setPost(getString(executivePosition));
        profile.setLevel(getString(executiveLevel));
        profile.setDegrees(getString(lecturerDegree));
        profile.setAreaOfSpecialization(getString(lecturerSpecializationArea));
        profile.setRoomNumber(getString(lecturerRoomNumber));
        return profile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                GlideApp.with(CreateProfileActivity.this)
                        .load(imageUri)
                        .centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .into(profileImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }else if(item.getItemId()==R.id.menu_create_profile){
            if(isValidForm()){
                mProgressDialog.show();
                if(imageUri!=null){
                    UploadTask logoUpload=mStorageReference
                            .child(imageUri.getLastPathSegment())
                            .putFile(imageUri);
                    logoUpload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                String pictureUrl=task.getResult().getDownloadUrl().toString();
                                Profile profile=getProfile();
                                profile.setPictureUrl(pictureUrl);
                                String id=mDatabaseReference.push().getKey();
                                profile.setId(id);
                                mDatabaseReference.child(id).setValue(profile);
                                mProgressDialog.dismiss();
                                finish();
                            }else {
                                mProgressDialog.dismiss();
                                finish();
                                Toast.makeText(CreateProfileActivity.this,
                                        "Failed to upload",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else {
                    Profile profile=getProfile();
                    String id=mDatabaseReference.push().getKey();
                    profile.setId(id);
                    mDatabaseReference.child(id).setValue(profile);
                    mProgressDialog.dismiss();
                    finish();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
