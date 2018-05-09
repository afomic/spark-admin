package com.afomic.sparkadmin.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by afomic on 11/4/17.
 *
 */

public class ImageElement implements BlogElement {
    private String imageUrl;
    private String imageDescription;
    private Uri imageUri;
    private boolean uploaded =false;

    public ImageElement(){
        imageUrl="";
    }

    protected ImageElement(Parcel in) {
        imageUrl = in.readString();
        imageDescription = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        uploaded = in.readByte() != 0;
    }

    public static final Creator<ImageElement> CREATOR = new Creator<ImageElement>() {
        @Override
        public ImageElement createFromParcel(Parcel in) {
            return new ImageElement(in);
        }

        @Override
        public ImageElement[] newArray(int size) {
            return new ImageElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageDescription);
        dest.writeParcelable(imageUri, flags);
        dest.writeByte((byte) (uploaded ? 1 : 0));
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
    @Exclude
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int getType() {
        return Type.IMAGE;
    }

    @Override
    public String toHtml() {
        return "<imv>"+imageUrl+"</imv>";
    }

}
