package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.ActionListener;
import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.GenericViewHolder;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.model.NumberListElement;
import com.afomic.sparkadmin.util.GlideApp;
import com.afomic.sparkadmin.util.SpringParser;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by afolabi michael on 11/4/17.
 *
 */

public class BlogDisplayAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private Context mContext;
    private ArrayList<BlogElement> elementList;
    private SpringParser stringParser;
    private Typeface mediumFont,largeFont,italics;
    public BlogDisplayAdapter(Context context, ArrayList<BlogElement> elements){
        mContext=context;
        elementList=elements;
        stringParser=new SpringParser();

        mediumFont=Typeface.createFromAsset(mContext.getAssets(), "font/medium.ttf");
        largeFont=Typeface.createFromAsset(mContext.getAssets(), "font/large.ttf");
        italics=Typeface.createFromAsset(mContext.getAssets(), "font/Lato-Italic.ttf");
    }
    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        switch (viewType){
            case BlogElement.Type.BIG_SIZE_TEXT:
                View v= mInflater.inflate(R.layout.item_output_big_text,parent,false);
                return new BigTextViewHolder(v);

            case BlogElement.Type.IMAGE:
                View mView= mInflater.inflate(R.layout.item_image,parent,false);
                return new ImageViewHolder(mView);
            case BlogElement.Type.NORMAL_SIZE_TEXT:
                View normalTextView= mInflater.inflate(R.layout.item_normal_text,parent,false);
                return new NormalTextHolder(normalTextView);
            case BlogElement.Type.BULLET_LIST_TEXT:
                View bullet= mInflater.inflate(R.layout.item_bullet_list,parent,false);
                return new BulletListViewHolder(bullet);
            case BlogElement.Type.NUMBER_LIST_TEXT:
                View numberedView= mInflater.inflate(R.layout.item_number_list,parent,false);
                return new NumberedListViewHolder(numberedView);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        BlogElement element=elementList.get(position);
        holder.bindView(null);
    }

    @Override
    public int getItemViewType(int position) {
        BlogElement element=elementList.get(position);
        return element.getType();
    }

    @Override
    public int getItemCount() {
        if(elementList==null){
            return 0;
        }
        //quick fix for multiple element insertion
        return elementList.size()-2;
    }
    public class ImageViewHolder extends GenericViewHolder{
        private ImageView mImageView;
        private TextView imageDescription;
        public ImageViewHolder(View itemView){
            super(itemView);
            mImageView= itemView.findViewById(R.id.imv_image);
            imageDescription=itemView.findViewById(R.id.tv_image_description);
        }
        @Override
        public void bindView(ActionListener listener) {
            ImageElement element=(ImageElement) elementList.get(getAdapterPosition());
            GlideApp.with(mContext)
                    .load(element.getImageUrl())
                    .thumbnail(0.2f)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mImageView);
            imageDescription.setText(element.getImageDescription());
        }
    }
    public class NormalTextHolder extends GenericViewHolder{
        private TextView mNormalTextView;
        public NormalTextHolder(View itemView) {
            super(itemView);
            mNormalTextView= itemView.findViewById(R.id.tv_normal_text);
            mNormalTextView.setTypeface(mediumFont);
        }

        @Override
        public void bindView(ActionListener listener) {
            int position=getAdapterPosition();
            NormalSizeTextElement element=(NormalSizeTextElement) elementList.get(position);
            CharSequence body=stringParser.parseString(element.getBody());
            mNormalTextView.setText(body);

        }
    }
    public class BigTextViewHolder extends GenericViewHolder {
        private TextView mBigTextView;
        public BigTextViewHolder(View itemView) {
            super(itemView);
            mBigTextView=itemView.findViewById(R.id.tv_big_text);
            mBigTextView.setTypeface(largeFont);
        }

        @Override
        public void bindView(ActionListener listener) {
            int position=getAdapterPosition();
            BigSizeTextElement element=(BigSizeTextElement) elementList.get(position);
            mBigTextView.setText(element.getBody());
        }
    }
    public class BulletListViewHolder extends GenericViewHolder {
        private TextView mBulletTextView;
        public BulletListViewHolder(View itemView) {
            super(itemView);
            mBulletTextView=itemView.findViewById(R.id.tv_bullet);
            mBulletTextView.setTypeface(italics);
        }

        @Override
        public void bindView(ActionListener listener) {
            int position=getAdapterPosition();
            BulletListTextElement element=(BulletListTextElement) elementList.get(position);
            mBulletTextView.setText(element.body);

        }
    }
    public class NumberedListViewHolder extends GenericViewHolder {
        private TextView mNumberedTextView;
        private TextView mNumberedPositionTextView;
        public NumberedListViewHolder(View itemView) {
            super(itemView);
            mNumberedTextView=itemView.findViewById(R.id.tv_number);
            mNumberedPositionTextView=itemView.findViewById(R.id.tv_number_position);

            mNumberedTextView.setTypeface(italics);
        }

        @Override
        public void bindView(ActionListener listener) {
            int position=getAdapterPosition();
            NumberListElement element=(NumberListElement) elementList.get(position);
            mNumberedTextView.setText(element.getBody());
            mNumberedPositionTextView.setText(element.getPosition());

        }
    }

}
