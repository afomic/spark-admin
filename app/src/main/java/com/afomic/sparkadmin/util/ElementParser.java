package com.afomic.sparkadmin.util;





import com.afomic.sparkadmin.model.BlogElement;

import java.util.ArrayList;

/**
 * Created by afomic on 10/18/17.
 */

public class ElementParser {
    public static ArrayList<BlogElement> fromHtml(String elementsString){
        MyTagHandler tagHandler=new MyTagHandler();
        android.text.Html.fromHtml(elementsString,null,tagHandler);
        return tagHandler.getElementList();
    }
    public static String toHtml(ArrayList<BlogElement> htmlArrayList){
        String html ="<body>";
        for(BlogElement element:htmlArrayList){
            html=html+element.toHtml();
        }
        html=html+"</body>";
        return html;
    }
}
