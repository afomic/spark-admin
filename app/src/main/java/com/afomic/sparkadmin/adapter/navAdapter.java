package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afomic.sparkadmin.R;


/**
 * Created by afomic on 30-Nov-16.
 *
 */
public class navAdapter extends BaseAdapter {
    private Context context;
    private String[] navigation={"Post","Post Manager","Profile","Constitution","Course",};
    private int[] imageId={R.drawable.feedback, R.drawable.feedback,R.drawable.feedback,R.drawable.feedback,R.drawable.feedback};
    public navAdapter(Context c){
        context=c;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public String getItem(int position) {
        return navigation[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.nav_item,parent,false);
        TextView navText=v.findViewById(R.id.nav_title);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/Lato-Regular.ttf");
        ImageView navIcon= v.findViewById(R.id.nav_icon);
        if(position==0){
            View indication =v.findViewById(R.id.nav_indicator);
            indication.setBackgroundColor(Color.argb(255,3, 169,244));
            navIcon.setImageResource(R.drawable.feedback);
            navText.setText(getItem(position));
            navText.setTextColor(Color.argb(255,3, 169,244));
            return v;
        }
        navText.setText(getItem(position));
        navText.setTypeface(typeface);
        navIcon.setImageResource(imageId[position]);
        return v;
    }
}
