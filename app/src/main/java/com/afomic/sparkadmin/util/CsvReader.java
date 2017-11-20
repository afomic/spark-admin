package com.afomic.sparkadmin.util;

import android.util.Log;
import android.widget.Toast;

import com.afomic.sparkadmin.model.Constitution;
import com.afomic.sparkadmin.model.Course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

/**
 * Created by afomic on 11/17/17.
 *
 */

public class CsvReader{
   private ArrayList<String[]> readFileData(String path) throws FileNotFoundException,InvalidPropertiesFormatException {
       ArrayList<String[]> csv=new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String csvLine;
                while ((csvLine = br.readLine()) != null) {
                    String[] data=csvLine.split(",");
                    csv.add(data);
                }
            }
            catch (IOException ex) {
                throw new InvalidPropertiesFormatException("wrong format"+ex);

            }
        } else {
            throw new FileNotFoundException("file not found");
        }
        return csv;
    }
    private Course getCourse(String[] data)throws NumberFormatException,InvalidPropertiesFormatException{
        Course course=new Course();
       try{
           course.setCourseName(data[0]);
           course.setTitle(data[1]);
           course.setCourseUnit(Integer.parseInt(data[2]));
           course.setCourseSemester(Integer.parseInt(data[3]));
           course.setCourseLevel(Integer.parseInt(data[4]));
           course.setPrerequisite(data[3]);
       }catch (IndexOutOfBoundsException e){
           throw new InvalidPropertiesFormatException("wrong format");
       }

       return course;

    }
    private Constitution getConstitution(String[] data)throws NumberFormatException{
        Constitution constitution=new Constitution();
        constitution.setArticle(Integer.parseInt(data[0]));
        constitution.setSection(Integer.parseInt(data[1]));
        constitution.setTitle(data[2]);
        constitution.setContent(data[3]);
        return constitution;

    }

    public ArrayList<Course> getCourses(String path) throws InvalidPropertiesFormatException,FileNotFoundException{
        ArrayList<String[]> CsvData=readFileData(path);
        ArrayList<Course> courseList=new ArrayList<>();
        for (String[] data:CsvData){
            Course item=getCourse(data);
            courseList.add(item);
        }
        return courseList;
    }
    public ArrayList<Constitution> getConstitution(String path) throws InvalidPropertiesFormatException,FileNotFoundException{
        ArrayList<String[]> CsvData=readFileData(path);
        ArrayList<Constitution> constitutionList=new ArrayList<>();
        for (String[] data:CsvData){
            Constitution item=getConstitution(data);
            constitutionList.add(item);
        }
        return constitutionList;
    }

}
