package com.afomic.sparkadmin.util;

import android.text.Editable;
import android.text.Html;


import com.afomic.sparkadmin.model.BigSizeTextElement;
import com.afomic.sparkadmin.model.BlogElement;
import com.afomic.sparkadmin.model.BulletListTextElement;
import com.afomic.sparkadmin.model.ImageElement;
import com.afomic.sparkadmin.model.NormalSizeTextElement;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

/**
 * Created by afomic on 10/18/17.
 */

public class MyTagHandler implements Html.TagHandler {


    private int type=-1;
    private int start=-1;
    ArrayList<BlogElement> elementList;
    public MyTagHandler(){
        elementList=new ArrayList<>();
    }
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(opening){
            start=output.length();
            if(tag.equals("nt")){
                type=BlogElement.Type.NORMAL_SIZE_TEXT;
            }else if(tag.equals("bt")){
                type=BlogElement.Type.BIG_SIZE_TEXT;
            }else if(tag.equals("imv")){
                type=BlogElement.Type.IMAGE;
            }else if(tag.equals("bl")){
                type=BlogElement.Type.BULLET_LIST_TEXT;
            }
        }else {
            switch (type){
                case BlogElement.Type.BIG_SIZE_TEXT:
                    String bigTextContent=output.subSequence(start,output.length()).toString();
                    BigSizeTextElement bigText=new BigSizeTextElement();
                    bigText.setBody(bigTextContent);
                    elementList.add(bigText);
                    break;
                case BlogElement.Type.NORMAL_SIZE_TEXT:
                    String normalTextContent=output.subSequence(start,output.length()).toString();
                    NormalSizeTextElement normalText=new NormalSizeTextElement();
                    normalText.setBody(normalTextContent);
                    elementList.add(normalText);
                    break;
                case BlogElement.Type.IMAGE:
                    String imageUrl=output.subSequence(start,output.length()).toString();
                    ImageElement image=new ImageElement();
                    image.setImageUrl(imageUrl);
                    elementList.add(image);
                    break;
                case BlogElement.Type.BULLET_LIST_TEXT:
                    String bulletText=output.subSequence(start,output.length()).toString();
                    BulletListTextElement bullet=new BulletListTextElement();
                    bullet.setBody(bulletText);
                    elementList.add(bullet);
                    break;

            }

        }

    }
    public ArrayList<BlogElement> getElementList(){
        return elementList;
    }
}
