package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 11/4/17.
 */

public class NormalSizeTextElement implements BlogElement{
    private String body;

    public NormalSizeTextElement(){
        this.body="";
    }

    protected NormalSizeTextElement(Parcel in) {
        body = in.readString();
    }

    public static final Creator<NormalSizeTextElement> CREATOR = new Creator<NormalSizeTextElement>() {
        @Override
        public NormalSizeTextElement createFromParcel(Parcel in) {
            return new NormalSizeTextElement(in);
        }

        @Override
        public NormalSizeTextElement[] newArray(int size) {
            return new NormalSizeTextElement[size];
        }
    };

    public String getBody() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getType() {
        return Type.NORMAL_SIZE_TEXT;
    }

    @Override
    public String toHtml() {
        return "<nt>"+body+"</nt>";
    }
}
