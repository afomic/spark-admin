package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 11/3/17.
 */

public class BlogPost implements Parcelable {
    private String mId;
    private int mType;
    private String mBody;
    private String mTitle;
    private String mPictureUrl;
    private String mFileUrl;
    private long mTimeStamp;
    private String mPosterName;
    private String mPosterIconUrl;
    private int  fileType;
    int status;

    public BlogPost(){
        mTimeStamp=System.currentTimeMillis();
    }

    protected BlogPost(Parcel in) {
        mId = in.readString();
        mType = in.readInt();
        mBody = in.readString();
        mTitle = in.readString();
        mPictureUrl = in.readString();
        mFileUrl = in.readString();
        mTimeStamp = in.readLong();
        mPosterName = in.readString();
        mPosterIconUrl = in.readString();
        fileType = in.readInt();
    }

    public static final Creator<BlogPost> CREATOR = new Creator<BlogPost>() {
        @Override
        public BlogPost createFromParcel(Parcel in) {
            return new BlogPost(in);
        }

        @Override
        public BlogPost[] newArray(int size) {
            return new BlogPost[size];
        }
    };

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        mFileUrl = fileUrl;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPosterName() {
        return mPosterName;
    }

    public void setPosterName(String posterName) {
        mPosterName = posterName;
    }

    public String getPosterIconUrl() {
        return mPosterIconUrl;
    }

    public void setPosterIconUrl(String posterIconUrl) {
        mPosterIconUrl = posterIconUrl;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }


    public class Type{
        public static final int FILE=0;
        public static final int BLOG=1;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeInt(mType);
        dest.writeString(mBody);
        dest.writeString(mTitle);
        dest.writeString(mPictureUrl);
        dest.writeString(mFileUrl);
        dest.writeLong(mTimeStamp);
        dest.writeString(mPosterName);
        dest.writeString(mPosterIconUrl);
        dest.writeInt(fileType);
    }
}
