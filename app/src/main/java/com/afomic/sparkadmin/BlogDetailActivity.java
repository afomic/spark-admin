package com.afomic.sparkadmin;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.afomic.sparkadmin.adapter.BlogDisplayAdapter;
import com.afomic.sparkadmin.data.PreferenceManager;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.data.Constant;
import com.afomic.sparkadmin.util.ElementParser;
import com.afomic.sparkadmin.util.HidingScrollLinearListener;
import com.afomic.sparkadmin.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<BlogElement>> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_blog_detail)
    RecyclerView blogView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.fab_like)
    FloatingActionButton likeFab;
    LinearLayoutManager mLayoutManager;
    BlogPost mBlogPost;
    ArrayList<BlogElement> mBlogElements;

    private static final String PARAM_BLOG_HTML = "html";

    private static final int BLOG_ELEMENT_LOADER_ID = 101;

    private LoaderManager mLoadManager;
    private PreferenceManager mPreferenceManager;
    private DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        ButterKnife.bind(this);

        mBlogPost = getIntent().getParcelableExtra(Constant.EXTRA_BLOG_POST);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBlogPost.getTitle());
        }
        mPreferenceManager=new PreferenceManager(this);
        String postPath="posts/"+mPreferenceManager.getAssociationName();

        postRef= FirebaseDatabase.getInstance().getReference(postPath)
                .child(mBlogPost.getId());


        //parse the html in the background
        mLoadManager = getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putString(PARAM_BLOG_HTML, mBlogPost.getBody());
        Loader<ArrayList<BlogElement>> mBlogLoader = mLoadManager.getLoader(BLOG_ELEMENT_LOADER_ID);
        if (mBlogLoader == null) {
            mLoadManager.initLoader(BLOG_ELEMENT_LOADER_ID, args, this);
        } else {
            mLoadManager.restartLoader(BLOG_ELEMENT_LOADER_ID, args, this);
        }

        mLayoutManager=new LinearLayoutManager(this);
        blogView.addOnScrollListener(new HidingScrollLinearListener(mLayoutManager) {
            @Override
            public void onHide() {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) likeFab.getLayoutParams();
                int fabBottomMargin = lp.bottomMargin;
                likeFab.animate().translationY(likeFab.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            public void onShow() {
                likeFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

            }

            @Override
            public void onLoadMore(int current_page) {

            }
        });

    }

    public static class parseBlogElement extends AsyncTaskLoader<ArrayList<BlogElement>> {
        private Bundle param;

        public parseBlogElement(Context context, Bundle param) {
            super(context);
            this.param = param;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public ArrayList<BlogElement> loadInBackground() {
            return ElementParser.fromHtml(param.getString(PARAM_BLOG_HTML));
        }
    }

    @Override
    public Loader<ArrayList<BlogElement>> onCreateLoader(int id, Bundle args) {
        return new parseBlogElement(this, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BlogElement>> loader, ArrayList<BlogElement> data) {
        mProgressBar.setVisibility(View.GONE);
        mBlogElements=data;
        BlogDisplayAdapter adapter = new BlogDisplayAdapter(BlogDetailActivity.this, data);
        blogView.setLayoutManager(new LinearLayoutManager(this));
        blogView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BlogElement>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mPreferenceManager.isAdmin()){
            getMenuInflater().inflate(R.menu.post_manager_menu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_approve_post:
                postRef.child("status")
                        .setValue(Constant.STATUS_APPROVED)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Util.makeToast(BlogDetailActivity.this,"Approved");
                            }
                        });
                break;
            case R.id.menu_delete_post:
                postRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Util.makeToast(BlogDetailActivity.this,"Deleted");
                                finish();
                            }
                        });
                break;
            case R.id.menu_edit_post:
                Intent intent=new Intent(this,CreateBlogActivity.class);
                intent.putExtra(Constant.EXTRA_BLOG_ELEMENTS,mBlogElements);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}