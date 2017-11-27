package com.afomic.sparkadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afomic.sparkadmin.adapter.CreatePostAdapter;
import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.fragment.UploadBlogDialog;
import com.afomic.sparkadmin.model.ActionListener;
import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.model.NumberListElement;
import com.afomic.sparkadmin.util.ElementParser;
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

import static com.afomic.sparkadmin.data.Constant.STATUS_APPROVED;

public class CreateBlogActivity extends AppCompatActivity implements
        CreatePostAdapter.CreatePostCallback,UploadBlogDialog.UploadDialogListener{
    @BindView(R.id.rv_create_blog)
    RecyclerView createBlogList;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private CreatePostAdapter mAdapter;
    private ArrayList<BlogElement> mBlogElements;

    private ProgressDialog mProgressDialog;
    PreferenceManager mPreferenceManager;
    private int numberListPosition=0;
    private ActionListener mActionListener=new ActionListener() {
        @Override
        public void onDelete(int position) {
            BlogElement item=mBlogElements.get(position);
            if(item.getType()==BlogElement.Type.NUMBER_LIST_TEXT
                    ||item.getType()==BlogElement.Type.BULLET_LIST_TEXT){
                mBlogElements.remove(position);
                mBlogElements.add(new NormalSizeTextElement());
                mAdapter.notifyItemChanged(position);
            }else {
                mBlogElements.remove(position);
                mAdapter.notifyItemRemoved(position);
                numberListPosition-=1;
            }


        }

        @Override
        public void onEnter(int position) {
            BlogElement element=mBlogElements.get(position);
            int newPosition=position+1;
            if(element.getType()==BlogElement.Type.BULLET_LIST_TEXT){
                BulletListTextElement bulletElement=new BulletListTextElement();
                mBlogElements.add(newPosition,bulletElement);

            }else if(element.getType()==BlogElement.Type.NUMBER_LIST_TEXT){
                numberListPosition+=1;
                NumberListElement listElement=new NumberListElement(numberListPosition);
                mBlogElements.add(listElement);
                mAdapter.notifyDataSetChanged();

            } else {
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

        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Blog");
        }

        mBlogElements=new ArrayList<>();
        mBlogElements.add(new BigSizeTextElement());
        mAdapter=new CreatePostAdapter(this,mActionListener,mBlogElements);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        createBlogList.setLayoutManager(layoutManager);
        createBlogList.setAdapter(mAdapter);

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading post...");
        mProgressDialog.setIndeterminate(true);

        mPreferenceManager=new PreferenceManager(this);


    }
    @OnClick(R.id.btn_big_text)
    public void bigButtonClicked(){
        resetNumberListPosition();
        BigSizeTextElement element=new BigSizeTextElement();
        mBlogElements.add(element);
        notifyDataInserted();

    }
    @OnClick(R.id.btn_bullet_list)
    public void bulletListClicked(){
        resetNumberListPosition();
        BulletListTextElement bullet=new BulletListTextElement();
        mBlogElements.add(bullet);
        notifyDataInserted();

    }
    @OnClick(R.id.btn_number_list)
    public void numberedListClicked(){
        numberListPosition+=1;
        NumberListElement listElement=new NumberListElement(numberListPosition);
        mBlogElements.add(listElement);
        notifyDataInserted();

    }
    @OnClick(R.id.btn_normal_text)
    public void normalTextClicked(){
        resetNumberListPosition();
        NormalSizeTextElement element=new NormalSizeTextElement();
        mBlogElements.add(element);
         notifyDataInserted();


    }
    @OnClick(R.id.btn_image)
    public void imageClicked(){
        resetNumberListPosition();
        hideKeyboard(this);
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
                notifyDataInserted();
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
        switch (item.getItemId()){
            case R.id.menu_submit_post:
                if(isValidEntry()){
                    hideKeyboard(this);
                    showTitleDialog();
                }
                break;
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
    public void showTitleDialog(){
        String bigText="";
        Uri imageUri=null;
        ImageElement firstImageElement=getFirstImage(mBlogElements);
        BigSizeTextElement bigSizeTextElement=getFirstBigText(mBlogElements);
        if(bigSizeTextElement!=null){
            bigText=bigSizeTextElement.getBody();
        }
        if (firstImageElement!=null){
            imageUri=firstImageElement.getImageUri();
        }
        UploadBlogDialog dialog=UploadBlogDialog.newInstance(bigText,imageUri);
        dialog.show(getSupportFragmentManager(),null);
    }
    public void uploadPost(BlogPost post){
        DatabaseReference blogRef=
        FirebaseDatabase.getInstance().getReference("blog").push();
        post.setId(blogRef.getKey());
        blogRef.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                finish();
            }
        });

    }
    private ImageElement getFirstImage(ArrayList<BlogElement> blogElements){
        for(BlogElement element:blogElements){
            if(element.getType()==BlogElement.Type.IMAGE){
                return (ImageElement) element;
            }
        }
        return null;
    }
    private BigSizeTextElement getFirstBigText(ArrayList<BlogElement> blogElements){
        for(BlogElement element:blogElements){
            if(element.getType()==BlogElement.Type.BIG_SIZE_TEXT){
                return (BigSizeTextElement) element;
            }
        }
        return null;
    }

    @Override
    public void onTitleSubmitted(String title) {
        BlogPost post=new BlogPost();
        String postBody= ElementParser.toHtml(mBlogElements);
        ImageElement element=getFirstImage(mBlogElements);
        if(element!=null){
            post.setPictureUrl(element.getImageUrl());
        }
        post.setBody(postBody);
        post.setTitle(title);
        post.setPosterName(mPreferenceManager.getUsername());
        post.setPictureUrl(mPreferenceManager.getIconUrl());
        post.setType(BlogPost.Type.BLOG);
        post.setStatus(STATUS_APPROVED);
        mProgressDialog.show();
        uploadPost(post);
    }
    public boolean allImageUploaded(ArrayList<BlogElement> blogElements){
        for(BlogElement element:blogElements){
            if(element.getType()==BlogElement.Type.IMAGE){
                ImageElement imageElement=(ImageElement) element;
                if (!imageElement.isUploaded()){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isValidEntry(){
        if(!allImageUploaded(mBlogElements)){
            Toast.makeText(CreateBlogActivity.this,
                    "Some Images Are Still Uploading",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mBlogElements.size()<2){
            Toast.makeText(CreateBlogActivity.this,
                    "You can not submit empty post",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void resetNumberListPosition(){
        if(numberListPosition!=0){
            numberListPosition=0;
        }
    }
    public void scrollToPosition(int position){
        createBlogList.scrollToPosition(position);

    }
    public void notifyDataInserted(){
        int lastPosition=mBlogElements.size()-1;
        mAdapter.notifyItemInserted(lastPosition);
        scrollToPosition(lastPosition);
    }

}
