package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.Course;

import java.util.ArrayList;

/**
 * Created by afomic on 03-Jun-16.
 *
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseHolder>{
    private ArrayList<Course> courses;
    private Context mContext;
    public CourseListAdapter(Context context, ArrayList<Course> result) {
       mContext=context;
        courses=result;
    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.course_search_item, parent, false);
        return new CourseHolder(v);
    }
    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {
        Course course=courses.get(position);
        holder.searchName.setText(course.getCourseName());
        holder.searchTitle.setText(course.getTitle());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
    public class CourseHolder extends RecyclerView.ViewHolder{
        TextView searchName,searchTitle;
        public CourseHolder(View itemView) {
            super(itemView);
            searchName=itemView.findViewById(R.id.gypee_course_name);
            searchTitle=itemView.findViewById(R.id.gypee_course_title);
        }
    }


}
