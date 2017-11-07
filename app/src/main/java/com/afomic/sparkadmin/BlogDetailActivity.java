package com.afomic.sparkadmin;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.afomic.sparkadmin.adapter.BlogDisplayAdapter;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BlogPost;
import com.afomic.sparkadmin.util.Constant;
import com.afomic.sparkadmin.util.ElementParser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlogDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<BlogElement>> {
    @BindView(R.id.rv_blog_detail)
    RecyclerView blogView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    BlogPost mBlogPost;

    private static final String PARAM_BLOG_HTML = "html";

    private static final int BLOG_ELEMENT_LOADER_ID = 101;

    LoaderManager mLoadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        ButterKnife.bind(this);

        mBlogPost = getIntent().getParcelableExtra(Constant.EXTRA_BLOG_POST);

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
        BlogDisplayAdapter adapter = new BlogDisplayAdapter(BlogDetailActivity.this, data);
        blogView.setLayoutManager(new LinearLayoutManager(this));
        blogView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BlogElement>> loader) {

    }

}