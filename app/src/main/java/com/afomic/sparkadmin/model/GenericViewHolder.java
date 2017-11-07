package com.afomic.sparkadmin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by afomic on 10/23/17.
 */

public abstract class GenericViewHolder extends RecyclerView.ViewHolder {

    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    public abstract  void bindView(ActionListener listener);
}
