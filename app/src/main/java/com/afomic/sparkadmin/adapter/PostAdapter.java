package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.util.ElementParser;
import com.afomic.sparkadmin.util.GlideApp;
import com.afomic.sparkadmin.util.SpringParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by afomic on 9/21/17.
 *
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<BlogPost> mBlogPosts;
    private Typeface mediumFont,largeFont;
    private SpringParser mSpringParser;
    PreferenceManager mPreferenceManager;

    private int[] fileTypeImageId={R.drawable.doc,R.drawable.doc,R.drawable.pdf_logo,R.drawable.power};

    public interface BlogPostListener{
        void OnFileBlogPostClick(BlogPost blogPost);
        void onBlogBlogPostClick(BlogPost blogPost);
    }
    private BlogPostListener mListener;
    public PostAdapter(Context context, ArrayList<BlogPost> BlogPosts,BlogPostListener listener){
        mBlogPosts=BlogPosts;
        mListener=listener;
        mContext=context;
        mSpringParser=new SpringParser();
        mPreferenceManager=new PreferenceManager(context);
        mediumFont=Typeface.createFromAsset(mContext.getAssets(), "font/medium.ttf");
        largeFont=Typeface.createFromAsset(mContext.getAssets(), "font/large.ttf");
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=null;
        if(viewType==BlogPost.Type.BLOG){
            v= LayoutInflater.from(mContext).inflate(R.layout.blog_post_item,parent,false);
            return new BlogViewHolder(v);
        }
        v=LayoutInflater.from(mContext).inflate(R.layout.file_post_item,parent,false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BlogPost tempBlogPost=mBlogPosts.get(position);
        CharSequence time= DateUtils.getRelativeTimeSpanString(tempBlogPost.getTimeStamp());
        if(tempBlogPost.getType()==BlogPost.Type.BLOG){
            BlogViewHolder mHolder=(BlogViewHolder) holder;
            mHolder.blogTitle.setText(tempBlogPost.getTitle());
            mHolder.BlogPosterNameTextView.setText(tempBlogPost.getPosterName());
            mHolder.BlogPostTime.setText(time);
            //use the first normal text as the summary of the blog
            ArrayList<BlogElement> blogElements= ElementParser.fromHtml(tempBlogPost.getBody());
            NormalSizeTextElement firstText=getFirstNormalText(blogElements);
            if(firstText!=null){
                String firstTextBody=firstText.getBody();
                mHolder.blogDescription.setText(mSpringParser.parseString(firstTextBody));
            }else {
                mHolder.blogDescription.setText("");
            }

            mHolder.BlogPostTime.setText(time);
            GlideApp.with(mContext)
                    .load(tempBlogPost.getPictureUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(mHolder.blogImage);
            GlideApp.with(mContext)
                    .load(tempBlogPost.getPosterIconUrl())
                    .placeholder(R.drawable.default_logo)
                    .into(mHolder.BlogPosterIcon);

        }else {
            final FileViewHolder mHolder=(FileViewHolder) holder;
            mHolder.fileName.setText(tempBlogPost.getBody());
            mHolder.BlogPostTime.setText(String.valueOf(tempBlogPost.getTimeStamp()));
            mHolder.BlogPostTime.setText(time);

            GlideApp.with(mContext)
                    .load(tempBlogPost.getPosterIconUrl())
                    .placeholder(R.drawable.default_logo)
                    .into(mHolder.BlogPosterIcon);
            GlideApp.with(mContext)
                    .load(fileTypeImageId[tempBlogPost.getFileType()])
                    .into(mHolder.fileIcon);
            mHolder.BlogPosterNameTextView.setText(tempBlogPost.getPosterName());
            FirebaseDatabase.getInstance().getReference("recommendation")
                    .child(tempBlogPost.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long count =dataSnapshot.getChildrenCount();
                            mHolder.downloadCount.setText(String.valueOf(count));
                            String userId=mPreferenceManager.getUserId();
                            if(dataSnapshot.hasChild(userId)){
                                mHolder.downloadIcon.setEnabled(false);
                                mHolder.downloadIcon.setClickable(false);
                                mHolder.downloadIcon.setImageResource(R.drawable.ic_downloaded);

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }

    }

    @Override
    public int getItemCount() {
        return mBlogPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        BlogPost tempBlogPost=mBlogPosts.get(position);
        return tempBlogPost.getType();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView BlogPosterNameTextView,BlogPostTime,fileName,downloadCount;
        ImageView fileIcon,BlogPosterIcon,downloadIcon;

        public FileViewHolder(View itemView) {
            super(itemView);
            BlogPosterIcon=(ImageView)itemView.findViewById(R.id.imv_post_icon);
            fileIcon=(ImageView) itemView.findViewById(R.id.imv_file_icon);
            BlogPosterNameTextView=(TextView) itemView.findViewById(R.id.tv_poster_name);
            BlogPostTime=(TextView) itemView.findViewById(R.id.tv_post_time);
            fileName=(TextView) itemView.findViewById(R.id.tv_file_name);
            downloadIcon=itemView.findViewById(R.id.imv_download_icon);
            downloadCount=itemView.findViewById(R.id.tv_download_count);
            downloadIcon.setOnClickListener(this);
            fileName.setTypeface(largeFont);
            BlogPosterNameTextView.setTypeface(largeFont);
        }

        @Override
        public void onClick(View view) {
            BlogPost fileBlogPost=mBlogPosts.get(getAdapterPosition());
            mListener.OnFileBlogPostClick(fileBlogPost);
        }
    }
    public class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView BlogPosterNameTextView,BlogPostTime,blogTitle,blogDescription;
        ImageView BlogPosterIcon,blogImage;

        public BlogViewHolder(View itemView) {
            super(itemView);
            BlogPosterIcon=(ImageView)itemView.findViewById(R.id.imv_post_icon);
            BlogPosterNameTextView=(TextView) itemView.findViewById(R.id.tv_poster_name);
            BlogPostTime=(TextView) itemView.findViewById(R.id.tv_post_time);
            blogImage=(ImageView) itemView.findViewById(R.id.imv_blog_picture);
            blogTitle=(TextView) itemView.findViewById(R.id.tv_blog_title);
            blogDescription=(TextView) itemView.findViewById(R.id.tv_blog_description);

            blogTitle.setTypeface(largeFont);
            blogDescription.setTypeface(mediumFont);
            BlogPosterNameTextView.setTypeface(largeFont);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            BlogPost blogBlogPost=mBlogPosts.get(getAdapterPosition());
            mListener.onBlogBlogPostClick(blogBlogPost);

        }
    }
    private NormalSizeTextElement getFirstNormalText(ArrayList<BlogElement> blogElements){
        for(BlogElement element:blogElements){
            if(element.getType()==BlogElement.Type.NORMAL_SIZE_TEXT){
                return (NormalSizeTextElement) element;
            }
        }
        return null;
    }



}
