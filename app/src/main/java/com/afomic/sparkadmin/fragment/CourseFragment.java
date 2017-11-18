package com.afomic.sparkadmin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afomic.sparkadmin.CsvParserIntentService;
import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.adapter.CourseListAdapter;
import com.afomic.sparkadmin.model.Course;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.afomic.sparkadmin.util.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by afomic on 11/14/17.
 *
 */

public class CourseFragment extends Fragment {
    @BindView(R.id.rv_course_list)
    RecyclerView courseListView;
    @BindView(R.id.empty_view_layout)
    LinearLayout emptyView;

    DatabaseReference courseRef;
    CourseListAdapter mAdapter;

    ArrayList<Course> mCourses;

    CourseBroadcastReceiver mReceiver;

    private static final int REQUEST_CODE_GET_CSV=101;

    public static CourseFragment newInstance(){
        return new CourseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter(CsvParserIntentService.ACTION_GET_COURSE);
        mReceiver=new CourseBroadcastReceiver();
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_course_layout,container,false);
        ButterKnife.bind(this,v);

        mCourses=new ArrayList<>();

        courseRef= FirebaseDatabase.getInstance().getReference("course/nacoss");

        courseListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter=new CourseListAdapter(getActivity(),mCourses);

        courseListView.setAdapter(mAdapter);

        courseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count =0;
                        mCourses.clear();
                         for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                             Course course=snapshot.getValue(Course.class);
                             mCourses.add(course);
                             count++;
                         }
                         if(count>0){
                             emptyView.setVisibility(View.GONE);
                             mAdapter.notifyDataSetChanged();

                         }else {
                             emptyView.setVisibility(View.VISIBLE);
                         }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return v;
    }
    @OnClick(R.id.btn_upload_course)
    public void selectCourseCsv(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class CourseBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String error=intent.getStringExtra(Constant.EXTRA_ERROR);
            if(error!=null){
                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<Course> courseList=intent.getParcelableArrayListExtra(Constant.EXTRA_COURSE_LIST);
                if(courseList!=null){
                    //TODO use the list
                }

            }


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}
