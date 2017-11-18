package com.afomic.sparkadmin.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.afomic.sparkadmin.CreateProfileActivity;
import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.Profile;
import com.afomic.sparkadmin.util.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by afomic on 11/15/17.
 *
 */

public class NewProfileDialog extends DialogFragment {
    public static NewProfileDialog newInstance(){
        return new NewProfileDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_profile,null,false);
        builder.setView(v);
        ButterKnife.bind(this,v);
        return builder.create();

    }
    @OnClick(R.id.btn_add_executives)
    public void addExco(){
        showCreateProfileUi(Profile.Type.EXCO);
    }
    @OnClick(R.id.btn_add_parliamentarian)
    public void addParliamentarian(){
        showCreateProfileUi(Profile.Type.PARLIAMENTARIAN);
    }
    @OnClick(R.id.btn_add_lecturers)
    public void addLecturers(){
        showCreateProfileUi(Profile.Type.LECTURER);
    }
    public void showCreateProfileUi(int profileType){
        Intent createProfileIntent=new Intent(getActivity(), CreateProfileActivity.class);
        createProfileIntent.putExtra(Constant.EXTRA_TYPE,profileType);
        startActivity(createProfileIntent);

    }
}
