package com.afomic.sparkadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.adapter.PersonAdapter;
import com.afomic.sparkadmin.model.Profile;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


/**
 *
 * Created by afomic on 17-Oct-16.
 */
public class PersonViewerFragment extends Fragment {
    RecyclerView grid;
    int type;
    private static final String BUNDLE_TYPE="type";
    ArrayList<Profile> mProfiles;
    DatabaseReference profileRef;
    PersonAdapter adapter;


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
        grid=view.findViewById(R.id.person_grid);
        int screenWidth= getResources().getConfiguration().screenWidthDp;
        int numberOfRows=screenWidth/140;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),numberOfRows);
        grid.setLayoutManager(mLayoutManager);

        grid.setItemAnimator(new DefaultItemAnimator());

        mProfiles=new ArrayList<>();
        adapter=new PersonAdapter(getActivity(),mProfiles);

        grid.setAdapter(adapter);

        profileRef=FirebaseDatabase.getInstance().getReference("profile/nacoss")
        ;
        profileRef.orderByChild("type")
                .equalTo(type)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProfiles.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Profile item=snapshot.getValue(Profile.class);
                    mProfiles.add(item);
                }
                if(mProfiles.size()>0){
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
