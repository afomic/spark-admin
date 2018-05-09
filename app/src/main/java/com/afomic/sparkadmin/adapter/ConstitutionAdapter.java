package com.afomic.sparkadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.afomic.sparkadmin.R;
import com.afomic.sparkadmin.model.Constitution;

import java.util.ArrayList;

/**
 * Created by afolabi michael on 04-Nov-16.
 *
 */
public class ConstitutionAdapter extends RecyclerView.Adapter<ConstitutionAdapter.myHolder> {
    private Context context;
    private ArrayList<Constitution> constitutionList;
    public ConstitutionAdapter(Context c,ArrayList<Constitution> constitutions){
        context=c;
        constitutionList=constitutions;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.constiution_item,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(myHolder holder, final int position) {
        Constitution item=constitutionList.get(position);
        String article="Article: "+(item.getArticle()+1);
        holder.constitutionArticle.setText(article);
        String section="Section: "+(item.getSection()+1);
        holder.constitutionSection.setText(section);
        holder.constitutionBody.setText(getContent(item));

    }
    public String getContent(Constitution item){
        String content=item.getContent();
        return Html.fromHtml(content).toString();
    }


    @Override
    public int getItemCount() {
        return constitutionList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView constitutionSection,constitutionArticle,constitutionBody;
        public myHolder(View v){
            super(v);
            constitutionBody=v.findViewById(R.id.constitution_body);
            constitutionSection=v.findViewById(R.id.constitution_section);
            constitutionArticle=v.findViewById(R.id.constitution_article);
        }

    }
}
