package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.ActionListener;
import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.GenericViewHolder;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.util.CustomEditText;
import com.afomic.sparkadmin.util.GlideApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by afomic on 10/17/17.
 *
 */




public class CreatePostAdapter extends RecyclerView.Adapter<GenericViewHolder>{

    public interface CreatePostCallback{
        void onImageRemovePressed(int position);


    }
    private ArrayList<BlogElement> htmlList;
    private Context mContext;
    private CreatePostCallback mListener;
    private ActionListener mActionListener;

    private Map<String,UploadTask> uploadTaskArray= new HashMap<>();
    private Typeface mediumFont,largeFont,italics;

    public CreatePostAdapter(Context context,ActionListener listener, ArrayList<BlogElement> arrayList){
        htmlList=arrayList;
        mContext=context;
        mListener=(CreatePostCallback) context;
        mActionListener=listener;
        mediumFont=Typeface.createFromAsset(mContext.getAssets(), "font/medium.ttf");
        largeFont=Typeface.createFromAsset(mContext.getAssets(), "font/large.ttf");
        italics=Typeface.createFromAsset(mContext.getAssets(), "font/Lato-Italic.ttf");;

    }


    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case BlogElement.Type.BIG_SIZE_TEXT:
                View v= LayoutInflater.from(mContext).inflate(R.layout.item_input_big_text,parent,false);
                return new AddBigTextHolder(v);
            case BlogElement.Type.IMAGE:
                View mView=  LayoutInflater.from(mContext).inflate(R.layout.item_add_image,parent,false);
                return new AddImageViewHolder(mView);
            case BlogElement.Type.NORMAL_SIZE_TEXT:
                View normalTextView=  LayoutInflater.from(mContext).inflate(R.layout.item_add_normal_text,parent,false);
                return new AddNormalTextHolder(normalTextView);
            case BlogElement.Type.BULLET_LIST_TEXT:
                View addBulletView = LayoutInflater.from(mContext).inflate(R.layout.item_add_bullet_list,parent,false);
                return new AddBulletHolder(addBulletView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bindView(mActionListener);
    }
    @Override
    public int getItemViewType(int position) {
        BlogElement mHtml=htmlList.get(position);
        return mHtml.getType();
    }

    @Override
    public int getItemCount() {
        if(htmlList==null){
            return 0;
        }
        return htmlList.size();
    }

    public class AddImageViewHolder extends GenericViewHolder{
        ImageView mImageView,removeButton;
        ProgressBar imageUploadProgress;

        public AddImageViewHolder(View itemView) {
            super(itemView);
            mImageView= itemView.findViewById(R.id.imv_add_image);
            removeButton= itemView.findViewById(R.id.imv_remove_btn);
            imageUploadProgress=itemView.findViewById(R.id.image_upload_progress);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageElement element=(ImageElement) htmlList.get(getAdapterPosition());
                    cancelUpload(element.getImageUri().getLastPathSegment());
                    mListener.onImageRemovePressed(getAdapterPosition());
                }
            });
        }

        @Override
        public void bindView(ActionListener listener) {
            ImageElement element=(ImageElement) htmlList.get(getAdapterPosition());
            GlideApp.with(mContext)
                    .load(element.getImageUri())
                    .placeholder(R.drawable.image_placeholder)
                    .into(mImageView);
            imageUploadProgress.setVisibility(element.isUploaded()?View.GONE:View.VISIBLE);
            if(!element.isUploaded()){
                uploadPost(element,getAdapterPosition());
            }

        }
    }
    public class AddNormalTextHolder extends GenericViewHolder{
        CustomEditText normalTextInput;

        public AddNormalTextHolder(View itemView) {
            super(itemView);
            normalTextInput=itemView.findViewById(R.id.edt_add_normal_text);
            normalTextInput.setTypeface(mediumFont);
            normalTextInput.addTextChangedListener(new MyTextWatch(normalTextInput));
            normalTextInput.setOnKeyListener(new KeyListener(normalTextInput));

        }

        @Override
        public void bindView(ActionListener listener) {
            NormalSizeTextElement normalText=(NormalSizeTextElement) htmlList.get(getAdapterPosition());
            normalTextInput.setTag(getAdapterPosition());
            normalTextInput.setText(normalText.getBody());
            getFocus(normalTextInput,getAdapterPosition());
        }
    }
    public class AddBigTextHolder extends GenericViewHolder{
        CustomEditText bigTextInput;

        public AddBigTextHolder(View itemView) {
            super(itemView);
            bigTextInput=itemView.findViewById(R.id.edt_add_big_text);
            bigTextInput.setTypeface(largeFont);
            bigTextInput.addTextChangedListener(new MyTextWatch(bigTextInput) );
            bigTextInput.setOnKeyListener(new KeyListener(bigTextInput));
        }

        @Override
        public void bindView(ActionListener listener) {
            BigSizeTextElement bigText=(BigSizeTextElement) htmlList.get(getAdapterPosition());
            bigTextInput.setTag(getAdapterPosition());
            bigTextInput.setText(bigText.getBody());
            getFocus(bigTextInput,getAdapterPosition());
        }
    }
    public class AddBulletHolder extends GenericViewHolder{
        CustomEditText bulletListInput;

        public AddBulletHolder(View itemView) {
            super(itemView);
            bulletListInput=itemView.findViewById(R.id.edt_add_bullet_list);
            bulletListInput.setTypeface(italics);
            bulletListInput.addTextChangedListener(new MyTextWatch(bulletListInput) );
            bulletListInput.setOnKeyListener(new KeyListener(bulletListInput));
        }

        @Override
        public void bindView(ActionListener listener) {
            BulletListTextElement bulletText=(BulletListTextElement) htmlList.get(getAdapterPosition());
            bulletListInput.setTag(getAdapterPosition());
            final String text=bulletText.getBody();
            bulletListInput.setText(text);

            bulletListInput.post(new Runnable() {
                @Override
                public void run() {
                    bulletListInput.setSelection(text.length());
                }
            });
            getFocus(bulletListInput,getAdapterPosition());
        }
    }
    public void showKeyboard(final EditText editText){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
            }
        },100);

    }
    public class MyTextWatch implements TextWatcher{
        EditText editText;
        public MyTextWatch(EditText view){
            this.editText=view;

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position=(int)editText.getTag();
            BlogElement element=htmlList.get(position);
            if (s.length()>0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
                //enter pressed
                editText.setText(s.subSequence(0, s.length()-1));
                mActionListener.onEnter(position);
                return;
            }
            switch (element.getType()){

                case BlogElement.Type.BIG_SIZE_TEXT:
                    BigSizeTextElement bigText=(BigSizeTextElement) element;
                    bigText.setBody(s.toString());
                    break;
                case BlogElement.Type.NORMAL_SIZE_TEXT:
                    NormalSizeTextElement normalText=(NormalSizeTextElement) element;
                    normalText.setBody(s.toString());
                    break;
                case BlogElement.Type.BULLET_LIST_TEXT:
                    BulletListTextElement bulletText=(BulletListTextElement) element;
                    bulletText.setBody(s.toString());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    public void getFocus(EditText edt,int position){
        if(position==(htmlList.size()-1)){
            edt.requestFocus();
            showKeyboard(edt);
        }

    }
    private void uploadPost(ImageElement element, final int position){
        Uri imageUri=element.getImageUri();
        StorageReference blogImageRef= FirebaseStorage.getInstance()
                .getReference("blog")
                .child("image");
        final UploadTask imageUploadTask=blogImageRef.child(imageUri.getLastPathSegment())
                .putFile(imageUri);
        uploadTaskArray.put(imageUri.getLastPathSegment(),imageUploadTask);
        imageUploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                try{
                    if(task.isSuccessful()){
                        if(task.getResult().getDownloadUrl()==null){
                            return;
                        }
                        String imageUrl=task.getResult().getDownloadUrl().toString();
                        ImageElement uploadedImage=(ImageElement) htmlList.get(position);
                        uploadedImage.setImageUrl(imageUrl);
                        uploadedImage.setUploaded(true);
                        notifyItemChanged(position);
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void cancelUpload(String lastSegmentpath){
        UploadTask task=uploadTaskArray.get(lastSegmentpath);
        if(task.isSuccessful()){
            task.getResult().getStorage().delete();
        }else {
            task.cancel();
        }

    }
    public class KeyListener implements View.OnKeyListener{
        CustomEditText mEditText;
        public KeyListener(CustomEditText edt){
            mEditText=edt;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int textLength=mEditText.length();
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL&&textLength==0) {
                int position=(int) mEditText.getTag();
                mActionListener.onDelete(position);
            }
            return false;
        }
    }


}
