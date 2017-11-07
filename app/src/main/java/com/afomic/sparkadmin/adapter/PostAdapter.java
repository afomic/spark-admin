package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.util.GlideApp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by afomic on 9/21/17.
 *
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<BlogPost> mBlogPosts;
    public interface BlogPostListener{
        void OnFileBlogPostClick(String fileUrl,String filename);
        void onBlogBlogPostClick(BlogPost BlogPost);
    }
    BlogPostListener mListener;
    public PostAdapter(Context context, ArrayList<BlogPost> BlogPosts){
        mBlogPosts=BlogPosts;
        mListener=(BlogPostListener) context;
        mContext=context;
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

            mHolder.BlogPostTime.setText(time);
            GlideApp.with(mContext)
                    .load(tempBlogPost.getPictureUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(mHolder.blogImage);
            GlideApp.with(mContext)
                    .load(tempBlogPost.getPosterIconUrl())
                    .placeholder(R.drawable.default_logo)
                    .into(mHolder.BlogPosterIcon);
            String shortDescription=String.format(Locale.ENGLISH,"%s ..",
                    tempBlogPost.getBody().substring(0,40));
            mHolder.blogDescription.setText(shortDescription);

        }else {
            FileViewHolder mHolder=(FileViewHolder) holder;
            mHolder.fileName.setText(tempBlogPost.getTitle());
            mHolder.BlogPostTime.setText(String.valueOf(tempBlogPost.getTimeStamp()));
            mHolder.BlogPostTime.setText(time);

            Glide.with(mContext)
                    .load(tempBlogPost.getPosterIconUrl())
                    .into(mHolder.BlogPosterIcon);
            mHolder.BlogPosterNameTextView.setText(tempBlogPost.getPosterName());

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
        TextView BlogPosterNameTextView,BlogPostTime,fileName;
        ImageView fileIcon,BlogPosterIcon;

        public FileViewHolder(View itemView) {
            super(itemView);
            BlogPosterIcon=(ImageView)itemView.findViewById(R.id.imv_post_icon);
            fileIcon=(ImageView) itemView.findViewById(R.id.imv_file_icon);
            BlogPosterNameTextView=(TextView) itemView.findViewById(R.id.tv_poster_name);
            BlogPostTime=(TextView) itemView.findViewById(R.id.tv_post_time);
            fileName=(TextView) itemView.findViewById(R.id.tv_file_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BlogPost fileBlogPost=mBlogPosts.get(getAdapterPosition());
            mListener.OnFileBlogPostClick(fileBlogPost.getFileUrl(),fileBlogPost.getTitle());
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
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            BlogPost blogBlogPost=mBlogPosts.get(getAdapterPosition());
            mListener.onBlogBlogPostClick(blogBlogPost);

        }
    }

}
