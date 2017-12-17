package com.afomic.sparkadmin.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Abderrahim on 8/27/2015.
 */
public abstract class HidingScrollLinearListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 15;
    public boolean controlsVisible = true;
    public int scrolledDistance = 0;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 9; // The minimum amount of items to have below your current scroll position before loading more.
    public int current_page = 1;
    public LinearLayoutManager mLinearLayoutManager;

    public HidingScrollLinearListener(LinearLayoutManager layoutManager) {
        this.mLinearLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = mLinearLayoutManager.getChildCount();
            int totalItemCount = mLinearLayoutManager.getItemCount();

            if (firstVisibleItem == 0) {
                if (!controlsVisible) {
                    onShow();
                    controlsVisible = true;
                }
            } else {
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        if(dy > 0) //check for scroll down
        {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                current_page++;

                onLoadMore(current_page);

                loading = true;
            }
        }

    }

    public abstract void onHide();

    public abstract void onShow();

    public abstract void onLoadMore(int current_page);
}
