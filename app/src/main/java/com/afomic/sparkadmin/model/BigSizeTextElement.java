package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 11/4/17.
 */

public class BigSizeTextElement implements BlogElement {
    private String body;
    public BigSizeTextElement(){
        this.body="";
    }

    protected BigSizeTextElement(Parcel in) {
        body = in.readString();
    }

    public static final Creator<BigSizeTextElement> CREATOR = new Creator<BigSizeTextElement>() {
        @Override
        public BigSizeTextElement createFromParcel(Parcel in) {
            return new BigSizeTextElement(in);
        }

        @Override
        public BigSizeTextElement[] newArray(int size) {
            return new BigSizeTextElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getType() {
        return Type.BIG_SIZE_TEXT;
    }

    @Override
    public String toHtml() {
        return "<bt>"+body+"</bt>";
    }
}
