package com.afomic.sparkadmin;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afomic.sparkadmin.data.Constant;
import com.afomic.sparkadmin.model.Profile;
import com.afomic.sparkadmin.util.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileDetailsActivity extends AppCompatActivity {
    @BindView(R.id.exco_view)
    LinearLayout executiveLayout;

    @BindView(R.id.lecturer_view)
    LinearLayout lecturerLayout;

    @BindView(R.id.tv_lecturer_room_number)
    TextView lecturerRoomNumber;

    @BindView(R.id.tv_lecturer_specialization_area)
    TextView lecturerSpecializationArea;

    @BindView(R.id.tv_lecturer_degree)
    TextView lecturerDegree;

    @BindView(R.id.tv_name)
    TextView profileName;

    @BindView(R.id.tv_phone_number)
    TextView profilePhoneNumber;

    @BindView(R.id.tv_email)
    TextView profileEmail;

    @BindView(R.id.tv_executive_position)
    TextView executivePosition;

    @BindView(R.id.tv_executive_level)
    TextView executiveLevel;
    @BindView(R.id.imv_profile_image)
    ImageView profileImage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        ButterKnife.bind(this);

        currentProfile=getIntent().getParcelableExtra(Constant.EXTRA_PROFILE);
        profileName.setText(currentProfile.getName());
        profileEmail.setText(currentProfile.getEmail());
        profilePhoneNumber.setText(currentProfile.getTelephoneNumber());
        GlideApp.with(this)
                .load(currentProfile.getPictureUrl())
                .centerCrop()
                .into(profileImage);
        if(currentProfile.getType()== Profile.Type.LECTURER){
            lecturerLayout.setVisibility(View.VISIBLE);
            executiveLayout.setVisibility(View.GONE);
            lecturerDegree.setText(currentProfile.getDegrees());
            lecturerRoomNumber.setText(currentProfile.getRoomNumber());
            lecturerSpecializationArea.setText(currentProfile.getAreaOfSpecialization());
        }else {
            executiveLevel.setText(currentProfile.getLevel());
            executivePosition.setText(currentProfile.getPost());
        }
    }
    @OnClick(R.id.fab_call)
    public void callUser(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + currentProfile.getTelephoneNumber()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
