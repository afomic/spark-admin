package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 11/12/17.
 */

public class NumberListElement implements BlogElement{
    private int position;
    private String body;
    public NumberListElement(int position){
        this.position=position;
        this.body="";

    }

    protected NumberListElement(Parcel in) {
        position = in.readInt();
        body = in.readString();
    }

    public static final Creator<NumberListElement> CREATOR = new Creator<NumberListElement>() {
        @Override
        public NumberListElement createFromParcel(Parcel in) {
            return new NumberListElement(in);
        }

        @Override
        public NumberListElement[] newArray(int size) {
            return new NumberListElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(body);
    }

    public String getPosition() {
        return position +".";
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getType() {
        return Type.NUMBER_LIST_TEXT;
    }

    @Override
    public String toHtml() {
        return "<nl>"+body+"</nl>";
    }
}
