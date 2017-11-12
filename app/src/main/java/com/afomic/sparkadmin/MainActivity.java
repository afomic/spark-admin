package com.afomic.sparkadmin;

import android.*;
import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afomic.sparkadmin.adapter.PostAdapter;
import com.afomic.sparkadmin.fragment.NewPostDialog;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.util.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PostAdapter.BlogPostListener {
    RecyclerView postRecyclerView;
    TextView posterTextView;
    ImageView addPostImageView;
    CircleImageView posterIcon;
    ArrayList<BlogPost> mPostList;
    PostAdapter mAdapter;
    private static Map<Long,String> downloadRef=new HashMap<>();
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


        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(new DownloadBroadcastReciever(), filter);


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
                mPostList.add(0,mPost);
                mAdapter.notifyItemInserted(0);
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
    public void OnFileBlogPostClick(BlogPost blogPost) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Spark/doc");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        Uri file_uri = Uri.parse(blogPost.getFileUrl());
        DownloadManager downloadManager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(file_uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading " + blogPost.getTitle());
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir("Spark/doc", blogPost.getTitle());

        long refid = downloadManager.enqueue(request);
        downloadRef.put(refid,blogPost.getId());
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
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },100);
        }

    }
    public String getFileName(BlogPost post){
        String[] extension={".docx",".pdf",".pptx"};
        return  post.getTitle()+extension[post.getFileType()];
    }
    public static class DownloadBroadcastReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String id=downloadRef.get(referenceId);
            if(id!=null){
                Toast.makeText(context,"File downloaded",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
