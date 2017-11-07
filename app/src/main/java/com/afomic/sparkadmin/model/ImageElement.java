package com.afomic.sparkadmin.model;

import android.net.Uri;
import android.os.Parcel;

import com.google.firebase.database.Exclude;

/**
 * Created by afomic on 11/4/17.
 */

public class ImageElement implements BlogElement {
    private String imageUrl;
    private String imageDescription;
    private Uri imageUri;
    private boolean uploaded =false;

    public ImageElement(){
        imageUrl="";
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
