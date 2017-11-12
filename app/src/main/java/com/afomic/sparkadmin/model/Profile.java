package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by afomic on 17-Oct-16.
 *
 */
public class Profile implements Parcelable{
    private String areaOfSpecialization;
    private String roomNumber;
    private String degrees;
    private  String name;
    private String telephoneNumber;
    private String email;
    private String level;
    private String department;
    private String post;
    private int type;
    private String pictureUrl;
    private String id;


    public Profile(){

    }

    protected Profile(Parcel in) {
        areaOfSpecialization = in.readString();
        roomNumber = in.readString();
        degrees = in.readString();
        name = in.readString();
        telephoneNumber = in.readString();
        email = in.readString();
        level = in.readString();
        department = in.readString();
        post = in.readString();
        type = in.readInt();
        pictureUrl = in.readString();
        id = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaOfSpecialization);
        dest.writeString(roomNumber);
        dest.writeString(degrees);
        dest.writeString(name);
        dest.writeString(telephoneNumber);
        dest.writeString(email);
        dest.writeString(level);
        dest.writeString(department);
        dest.writeString(post);
        dest.writeInt(type);
        dest.writeString(pictureUrl);
        dest.writeString(id);
    }

    public class Type{
        public static final int EXCO=0;
        public static final int LECTURER=1 ;
        public static final int PARLIAMENTARIAN =1 ;

    }

    public String getAreaOfSpecialization() {
        return areaOfSpecialization;
    }

    public void setAreaOfSpecialization(String areaOfSpecialization) {
        this.areaOfSpecialization = areaOfSpecialization;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
