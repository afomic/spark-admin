package com.afomic.sparkadmin.util;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by afomic on 11/20/17.
 */

public class FloatingActionButtonLayoutBehaviour extends CoordinatorLayout.Behavior<FloatingActionButton> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RecyclerView;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(dependency.getTranslationY()>0){
            Log.e("TAG", "onDependentViewChanged: moved up");
        }else {
            Log.e("TAG", "onDependentViewChanged: moved down");
        }
//        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
//        child.setTranslationY(translationY);
        return true;
    }


}
