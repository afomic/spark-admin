package com.afomic.sparkadmin.util;

import android.text.Editable;
import android.text.Html;


import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;
import com.afomic.sparkadmin.model.NumberListElement;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

/**
 *
 * Created by afomic on 10/18/17.
 */

public class MyTagHandler implements Html.TagHandler {


    private int type=-1;
    private int start=-1;
    private ArrayList<BlogElement> elementList;
    private int numberListPosition=0;
    public MyTagHandler(){
        elementList=new ArrayList<>();
    }
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(opening){
            start=output.length();
            switch (tag) {
                case "nt":
                    type = BlogElement.Type.NORMAL_SIZE_TEXT;
                    break;
                case "bt":
                    type = BlogElement.Type.BIG_SIZE_TEXT;
                    break;
                case "imv":
                    type = BlogElement.Type.IMAGE;
                    break;
                case "bl":
                    type = BlogElement.Type.BULLET_LIST_TEXT;
                    break;
                case "nl":
                    type = BlogElement.Type.NUMBER_LIST_TEXT;
                    break;
            }
        }else {
            switch (type){
                case BlogElement.Type.BIG_SIZE_TEXT:
                    resetNumberListPosition();
                    String bigTextContent=output.subSequence(start,output.length()).toString();
                    BigSizeTextElement bigText=new BigSizeTextElement();
                    bigText.setBody(bigTextContent);
                    elementList.add(bigText);
                    break;
                case BlogElement.Type.NORMAL_SIZE_TEXT:
                    resetNumberListPosition();
                    String normalTextContent=output.subSequence(start,output.length()).toString();
                    NormalSizeTextElement normalText=new NormalSizeTextElement();
                    normalText.setBody(normalTextContent);
                    elementList.add(normalText);
                    break;
                case BlogElement.Type.IMAGE:
                    resetNumberListPosition();
                    String imageUrl=output.subSequence(start,output.length()).toString();
                    ImageElement image=new ImageElement();
                    image.setImageUrl(imageUrl);
                    elementList.add(image);
                    break;
                case BlogElement.Type.BULLET_LIST_TEXT:
                    resetNumberListPosition();
                    String bulletText=output.subSequence(start,output.length()).toString();
                    BulletListTextElement bullet=new BulletListTextElement();
                    bullet.setBody(bulletText);
                    elementList.add(bullet);
                    break;
                case BlogElement.Type.NUMBER_LIST_TEXT:
                    numberListPosition+=1;
                    String numberListText=output.subSequence(start,output.length()).toString();
                    NumberListElement numberListElement=new NumberListElement(numberListPosition);
                    numberListElement.setBody(numberListText);
                    elementList.add(numberListElement);
                    break;

            }

        }

    }
    public ArrayList<BlogElement> getElementList(){
        return elementList;
    }
    public void resetNumberListPosition(){
        if(numberListPosition!=0){
            numberListPosition=0;
        }
    }
}
