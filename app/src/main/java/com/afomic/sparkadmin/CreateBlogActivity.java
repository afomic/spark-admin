package com.afomic.sparkadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afomic.sparkadmin.adapter.CreatePostAdapter;
import com.afomic.sparkadmin.model.ActionListener;
import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.util.ElementParser;
import com.afomic.sparkadmin.util.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateBlogActivity extends AppCompatActivity implements CreatePostAdapter.CreatePostCallback{
    @BindView(R.id.rv_create_blog)
    RecyclerView createBlogList;

    CreatePostAdapter mAdapter;
    ArrayList<BlogElement> mBlogElements;

    ProgressDialog mProgressDialog;

//    private final String BUNDLE_BLOG_ELEMENTS="elements";

    private static final int GET_IMAGE_REQUEST_CODE=102;

    private ActionListener mActionListener=new ActionListener() {
        @Override
        public void onDelete(int position) {
            Toast.makeText(CreateBlogActivity.this,"On delete Called",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onEnter(int position) {
            BlogElement element=mBlogElements.get(position);
            int newPosition=position+1;
            if(element.getType()==BlogElement.Type.BULLET_LIST_TEXT){
                BulletListTextElement bulletElement=new BulletListTextElement();
                mBlogElements.add(newPosition,bulletElement);

            }else {
                NormalSizeTextElement normalText=new NormalSizeTextElement();
                mBlogElements.add(newPosition,normalText);
            }
            mAdapter.notifyItemInserted(newPosition);
            createBlogList.scrollToPosition(newPosition);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        ButterKnife.bind( this);

        mBlogElements=new ArrayList<>();
        mBlogElements.add(new BigSizeTextElement());
        mAdapter=new CreatePostAdapter(this,mActionListener,mBlogElements);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        createBlogList.setLayoutManager(layoutManager);
        createBlogList.setAdapter(mAdapter);

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading post...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);


    }
    @OnClick(R.id.btn_big_text)
    public void bigButtonClicked(){
        BigSizeTextElement element=new BigSizeTextElement();
        mBlogElements.add(element);
        mAdapter.notifyDataSetChanged();
        createBlogList.scrollToPosition(mBlogElements.size()-1);

    }
    @OnClick(R.id.btn_bullet_list)
    public void bulletListClicked(){
        BulletListTextElement bullet=new BulletListTextElement();
        mBlogElements.add(bullet);
        mAdapter.notifyDataSetChanged();
        createBlogList.scrollToPosition(mBlogElements.size()-1);

    }
    @OnClick(R.id.btn_number_list)
    public void numberedListClicked(){

    }
    @OnClick(R.id.btn_normal_text)
    public void normalTextClicked(){
        NormalSizeTextElement element=new NormalSizeTextElement();
        mBlogElements.add(element);
        mAdapter.notifyDataSetChanged();
        createBlogList.scrollToPosition(mBlogElements.size()-1);

    }
    @OnClick(R.id.btn_image)
    public void imageClicked(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .start(this);



    }

    @Override
    public void onImageRemovePressed(int position) {
        mBlogElements.remove(position);
        mAdapter.notifyItemRemoved(position);

    }
    //    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(BUNDLE_BLOG_ELEMENTS,mBlogElements);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                ImageElement element=new ImageElement();
                element.setImageUri(imageUri);
                mBlogElements.add(element);
                mAdapter.notifyDataSetChanged();
                createBlogList.scrollToPosition(mBlogElements.size()-1);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_submit_post){
            String postBody= ElementParser.toHtml(mBlogElements);
            BlogPost post=new BlogPost();
            post.setBody(postBody);
            post.setType(BlogPost.Type.BLOG);
            mProgressDialog.show();
            uploadPost(post);
        }
        return super.onOptionsItemSelected(item);
    }
    public void uploadPost(BlogPost post){
        DatabaseReference blogRef=
        FirebaseDatabase.getInstance().getReference("blog").push();
        post.setId(blogRef.getKey());
        blogRef.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
            }
        });



    }
}
