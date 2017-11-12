package com.afomic.sparkadmin.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.adapter.PersonAdapter;


/**
 * Created by afomic on 17-Oct-16.
 */
public class PersonViewerFragment extends Fragment {
    RecyclerView grid;
    int type;
    private static final String BUNDLE_TYPE="type";

    public static PersonViewerFragment getInstance(int type){
        PersonViewerFragment fragment=new PersonViewerFragment();
        Bundle arg=new Bundle();
        arg.putInt(BUNDLE_TYPE,type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getInt(BUNDLE_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.person_viewer,container,false);
        grid=(RecyclerView) view.findViewById(R.id.person_grid);
        int screenWidth= getResources().getConfiguration().screenWidthDp;
        int numberOfRows=screenWidth/140;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),numberOfRows);
        grid.setLayoutManager(mLayoutManager);

        grid.setItemAnimator(new DefaultItemAnimator());


//        PersonAdapter adapter=new PersonAdapter(getActivity(),type);
//        grid.setAdapter(adapter);

        return view;
    }

}
