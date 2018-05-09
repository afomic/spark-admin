package com.afomic.sparkadmin.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.util.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by afomic on 10/19/17.
 *
 */

public class UploadBlogDialog extends DialogFragment {

    @BindView(R.id.edt_post_title)
    EditText postTitleEditText;

    @BindView(R.id.imv_post_image)
    ImageView postImage;

    Unbinder mUnbinder;

    String firstBigText;
    Uri imageUri;
    UploadDialogListener mDialogListener;
    public interface UploadDialogListener{
        void onTitleSubmitted(String title);
    }

    private static final String BUNDLE_FIRST_IMAGE_URI="image_uri";
    private static final String BUNDLE_FIRST_BIG_TEXT="big_text";
    public static UploadBlogDialog newInstance(String firstBigText,Uri firstImageUri){
        UploadBlogDialog dialog=new UploadBlogDialog();
        Bundle args=new Bundle();
        args.putString(BUNDLE_FIRST_BIG_TEXT,firstBigText);
        args.putParcelable(BUNDLE_FIRST_IMAGE_URI,firstImageUri);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        firstBigText=args.getString(BUNDLE_FIRST_BIG_TEXT);
        imageUri=args.getParcelable(BUNDLE_FIRST_IMAGE_URI);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDialogListener=(UploadDialogListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_post,null,false);
        mUnbinder= ButterKnife.bind(this,v);
        builder.setView(v);
        GlideApp.with(getActivity())
                .load(imageUri)
                .thumbnail(0.2f)
                .placeholder(R.drawable.image_placeholder)
                .into(postImage);
        postTitleEditText.setText(firstBigText);

        return builder.create();
    }
    @OnClick(R.id.tv_action_cancel)
    public void onCancelPressed(){
        dismiss();
    }

    @OnClick(R.id.tv_action_publish)
    public void onPublishPressed(){
        mDialogListener.onTitleSubmitted(postTitleEditText.getText().toString());
        dismiss();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDialogListener=null;
    }
}
