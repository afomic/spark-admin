package com.afomic.sparkadmin.model;

/**
 * Created by afomic on 17-Oct-16.
 *
 */
public class Course {
    private String courseName;
    private int courseSemester,courseLevel,courseUnit,option;
    private int resourceID;
    private int grade;
    private String prerequisite,title;

    public Course(String courseName,int option, int courseUnit, int courseLevel, int courseSemester,String prerequisite,String title) {
        this.courseName = courseName;
        this.courseSemester = courseSemester;
        this.option=option;
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

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
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
}
