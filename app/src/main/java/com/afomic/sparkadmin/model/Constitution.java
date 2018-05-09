package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 26-Oct-16.
 */
public class Constitution  implements Parcelable{
    private int article,section;
    private String content,title;


    public Constitution(){

    }

    public Constitution(int article, int section,String title, String content) {
        this.article = article;
        this.section = section;
        this.content = content;
        this.title=title;
    }

    protected Constitution(Parcel in) {
        article = in.readInt();
        section = in.readInt();
        content = in.readString();
        title = in.readString();
    }

    public static final Creator<Constitution> CREATOR = new Creator<Constitution>() {
        @Override
        public Constitution createFromParcel(Parcel in) {
            return new Constitution(in);
        }

        @Override
        public Constitution[] newArray(int size) {
            return new Constitution[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(article);
        dest.writeInt(section);
        dest.writeString(content);
        dest.writeString(title);
    }

    public int getSection() {
        return section;
    }
    public String getTitle(){
        return title;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
