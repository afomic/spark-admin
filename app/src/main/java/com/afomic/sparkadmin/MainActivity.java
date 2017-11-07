package com.afomic.sparkadmin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afomic.sparkadmin.adapter.PostAdapter;
import com.afomic.sparkadmin.fragment.NewPostDialog;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.util.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PostAdapter.BlogPostListener {
    RecyclerView postRecyclerView;
    TextView posterTextView;
    ImageView addPostImageView;
    CircleImageView posterIcon;
    ArrayList<BlogPost> mPostList;
    PostAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        postRecyclerView=(RecyclerView) findViewById(R.id.rv_post_list);
        posterIcon=(CircleImageView) findViewById(R.id.imv_poster_icon);
        posterTextView=(TextView) findViewById(R.id.tv_poster_name);
        addPostImageView=(ImageView) findViewById(R.id.imv_add_post);

        mPostList=new ArrayList<>();

        mAdapter=new PostAdapter(this,mPostList);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(mLayoutManager);
        postRecyclerView.setAdapter(mAdapter);

        addPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPostDialog.newInstance().show(getSupportFragmentManager(),null);
            }
        });

        DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("blog");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BlogPost mPost=dataSnapshot.getValue(BlogPost.class);
                mPostList.add(mPost);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void OnFileBlogPostClick(String fileUrl, String filename) {

    }

    @Override
    public void onBlogBlogPostClick(BlogPost BlogPost) {
        Intent intent=new Intent(MainActivity.this,BlogDetailActivity.class);
        intent.putExtra(Constant.EXTRA_BLOG_POST,BlogPost);
        startActivity(intent);

    }

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },100);
        }

    }
}
