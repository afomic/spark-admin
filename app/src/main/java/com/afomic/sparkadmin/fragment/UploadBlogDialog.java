package com.afomic.sparkadmin.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.util.Constant;
import com.afomic.sparkadmin.util.ElementParser;
import com.afomic.sparkadmin.util.GlideApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

    DatabaseReference blogRef;

    BlogPost mBlogPost;

    String postHtml;
    public static UploadBlogDialog newInstance(BlogPost post){
        UploadBlogDialog dialog=new UploadBlogDialog();
        Bundle args=new Bundle();
        args.putParcelable(Constant.EXTRA_BLOG_POST,post);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogPost=getArguments().getParcelable(Constant.EXTRA_BLOG_POST);
        blogRef= FirebaseDatabase.getInstance().getReference("blog");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_post,null,false);
        mUnbinder= ButterKnife.bind(this,v);
        ImageElement image=getFirstImage();
        if(image!=null){
            GlideApp.with(getActivity())
                    .load(mBlogPost.getPictureUrl())
                    .thumbnail(0.2f)
                    .placeholder(R.drawable.image_placeholder)
                    .into(postImage);
        }
        builder.setView(v);
        return builder.create();
    }
    @OnClick(R.id.tv_action_cancel)
    public void onCancelPressed(){
        dismiss();
    }

    @OnClick(R.id.tv_action_publish)
    public void onPublishPressed(){
        mBlogPost.setTitle(postTitleEditText.getText().toString());
        String id=blogRef.push().getKey();
        mBlogPost.setId(id);
        blogRef.child(id).setValue(mBlogPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dismiss();
            }
        });

    }
    private ImageElement getFirstImage(){
        ArrayList<BlogElement> blogElements= ElementParser.fromHtml(postHtml);
        for(BlogElement element:blogElements){
            if(element.getType()==BlogElement.Type.IMAGE){
                return (ImageElement) element;
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
