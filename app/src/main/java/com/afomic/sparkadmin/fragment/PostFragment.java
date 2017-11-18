package com.afomic.sparkadmin.fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afomic.sparkadmin.BlogDetailActivity;
import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.adapter.PostAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by afomic on 11/14/17.
 *
 */

public class PostFragment extends Fragment implements PostAdapter.BlogPostListener{
    @BindView(R.id.rv_post_list)
    RecyclerView postRecyclerView;
    @BindView(R.id.tv_poster_name)
    TextView posterTextView;
    @BindView(R.id.imv_add_post)
    ImageView addPostImageView;
    @BindView(R.id.imv_poster_icon)
    CircleImageView posterIcon;
    ArrayList<BlogPost> mPostList;
    PostAdapter mAdapter;

    Unbinder mUnbinder;
    private static Map<Long,String> downloadRef=new HashMap<>();
    public static PostFragment newInstance(){
        return new PostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostList=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_post,container,false);
        mUnbinder= ButterKnife.bind(this,v);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(new DownloadBroadcastReciever(), filter);


        mAdapter=new PostAdapter(getActivity(),mPostList,this);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        postRecyclerView.setLayoutManager(mLayoutManager);
        postRecyclerView.setAdapter(mAdapter);

        addPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPostDialog.newInstance().show(getFragmentManager(),null);
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

        return v;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();

    }
    @Override
    public void OnFileBlogPostClick(BlogPost blogPost) {
        requestPermission();
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Spark/doc");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        Uri file_uri = Uri.parse(blogPost.getFileUrl());
        DownloadManager downloadManager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
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
        Intent intent=new Intent(getActivity(),BlogDetailActivity.class);
        intent.putExtra(Constant.EXTRA_BLOG_POST,BlogPost);
        startActivity(intent);

    }

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },100);
        }

    }
    public static class DownloadBroadcastReciever extends BroadcastReceiver {
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
