package com.afomic.sparkadmin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.sparkadmin.adapter.PostAdapter;
import com.afomic.sparkadmin.data.Constant;
import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.model.BlogPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by afomic on 11/14/17.
 *
 */

public class PostMangerFragment extends Fragment {
    private DatabaseReference postRef;
    private PreferenceManager mPreferenceManager;
    private ArrayList<BlogPost> mBlogPosts;
    private PostAdapter mPostAdapter;
    private PostAdapter.BlogPostListener mPostListener=new PostAdapter.BlogPostListener() {
        @Override
        public void OnFileBlogPostClick(BlogPost blogPost) {

        }

        @Override
        public void onBlogBlogPostClick(BlogPost blogPost) {

        }
    };


    public static PostMangerFragment newInstance(){
        return new PostMangerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceManager=new PreferenceManager(getActivity());
        String departmentCode=mPreferenceManager.getAssociationName();
        postRef=FirebaseDatabase.getInstance().getReference("posts/"+departmentCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view=new RecyclerView(getActivity());
        mBlogPosts=new ArrayList<>();
        mPostAdapter=new PostAdapter(getActivity(),mBlogPosts,mPostListener);
        view.setAdapter(mPostAdapter);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRef.orderByChild("status")
                .equalTo(Constant.STATUS_UNAPPROVED)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBlogPosts.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            BlogPost post=snapshot.getValue(BlogPost.class);
                            mBlogPosts.add(post);
                        }
                        mPostAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return view;
    }
}
