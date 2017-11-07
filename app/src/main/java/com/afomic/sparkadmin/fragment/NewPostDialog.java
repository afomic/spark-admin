package com.afomic.sparkadmin.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afomic.sparkadmin.CreateBlogActivity;
import com.afomic.sparkadmin.NewFilePostActivity;
import com.afomic.sparkadmin.R;

/**
 * Created by afomic on 9/24/17.
 */

public class NewPostDialog extends DialogFragment {
    public static NewPostDialog newInstance(){
        return new NewPostDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Select Post Type");
        mBuilder.setItems(R.array.post_type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        Intent mIntent=new Intent(getActivity(), NewFilePostActivity.class);
                        startActivity(mIntent);
                        dismiss();
                        break;
                    case 1:
                        Intent intent=new Intent(getActivity(), CreateBlogActivity.class);
                        startActivity(intent);
                        dismiss();
                        break;
                }
            }
        });
        return mBuilder.create();
    }
}
