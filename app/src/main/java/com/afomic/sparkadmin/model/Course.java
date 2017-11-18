package com.afomic.sparkadmin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 17-Oct-16.
 *
 */
public class Course  implements Parcelable{
    private String courseName;
    private int courseSemester,courseLevel,courseUnit;
    private int resourceID;
    private int grade;
    private String prerequisite,title;

    public Course(String courseName ,int courseUnit, int courseLevel, int courseSemester,String prerequisite,String title) {
        this.courseName = courseName;
        this.courseSemester = courseSemester;
        this.prerequisite=prerequisite;
        this.title=title;
        this.courseLevel = courseLevel;
        this.courseUnit = courseUnit;
    }
    public Course(String courseName,int courseUnit){
        this.courseName=courseName;
        this.courseUnit=courseUnit;

    }
    public Course(){

    }

    protected Course(Parcel in) {
        courseName = in.readString();
        courseSemester = in.readInt();
        courseLevel = in.readInt();
        courseUnit = in.readInt();
        resourceID = in.readInt();
        grade = in.readInt();
        prerequisite = in.readString();
        title = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeInt(courseSemester);
        dest.writeInt(courseLevel);
        dest.writeInt(courseUnit);
        dest.writeInt(resourceID);
        dest.writeInt(grade);
        dest.writeString(prerequisite);
        dest.writeString(title);
    }

    public int getCourseLevel() {
        return courseLevel;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseSemester() {
        return courseSemester;
    }


    public int getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(int courseUnit) {
        this.courseUnit = courseUnit;
    }
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getTitle() {
        return title;
    }

    public void setCourseSemester(int courseSemester) {
        this.courseSemester = courseSemester;
    }

    public void setCourseLevel(int courseLevel) {
        this.courseLevel = courseLevel;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
