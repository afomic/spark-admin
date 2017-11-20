package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.Profile;
import com.afomic.sparkadmin.util.GlideApp;

import java.util.ArrayList;

/**
 * Created by afomic on 17-Oct-16.
 *
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.Holder> {
    private ArrayList<Profile> mProfiles;
    private Context context;
    public PersonAdapter(Context context, ArrayList<Profile> profiles){
        this.context=context;
        mProfiles=profiles;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.person_layout,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Profile item= getItem(position);
        GlideApp.with(context)
                .load(item.getPictureUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.personPicture);
        holder.personName.setText(item.getName());
        holder.background.setBackgroundColor(item.getColor());
        if(item.getType()==Profile.Type.EXCO){
            holder.personDescription.setText(item.getPost());
        }else {
            holder.personDescription.setText(item.getRoomNumber());
        }

    }

    @Override
    public int getItemCount() {
        if(mProfiles==null){
            return 0;
        }
        return mProfiles.size();
    }
    public Profile getItem(int position) {
       return mProfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView personName, personDescription;
        ImageView personPicture;
        RelativeLayout container;
        View background;
        public Holder(View itemView) {
            super(itemView);
            container= itemView.findViewById(R.id.person_container);
            background=itemView.findViewById(R.id.background_view);
            personDescription=itemView.findViewById(R.id.tv_person_description);
            personName= itemView.findViewById(R.id.tv_person_name);
            personPicture= itemView.findViewById(R.id.iv_person_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = null;
//            Profile item=getItem(getAdapterPosition());
//            if (item.getType()==Profile.Type.LECTURER) {
//                intent = new Intent(context, LecturerDetail.class);
//            } else {
//                intent = new Intent(context, ExcoViewerActivity.class);
//            }
//
//            intent.putExtra(Constant.EXTRA_PROFILE,item);
//            context.startActivity(intent);
        }
    }


}
