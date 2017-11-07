package com.afomic.sparkadmin.model;

import android.os.Parcelable;

/**
 * Created by afomic on 11/4/17.
 *
 */

public interface BlogElement{
    int getType();

    String toHtml();
    public class Type{
        public static final int BIG_SIZE_TEXT=1;
        public static final int NORMAL_SIZE_TEXT=2;
        public static final int BULLET_LIST_TEXT=3;
        public static final int IMAGE=4;
    }

}
