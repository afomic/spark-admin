package com.afomic.sparkadmin.model;

/**
 * Created by afomic on 11/12/17.
 */

public class NumberListElement implements BlogElement {
    private int position;
    private String body;
    public NumberListElement(int position){
        this.position=position;
        this.body="";

    }

    public String getPosition() {
        return position +".";
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getType() {
        return Type.NUMBER_LIST_TEXT;
    }

    @Override
    public String toHtml() {
        return "<nl>"+body+"</nl>";
    }
}
