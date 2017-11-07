package com.afomic.sparkadmin.model;

/**
 * Created by afomic on 11/4/17.
 */

public class BigSizeTextElement implements BlogElement {
    private String body;
    public BigSizeTextElement(){
        this.body="";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getType() {
        return Type.BIG_SIZE_TEXT;
    }

    @Override
    public String toHtml() {
        return "<bt>"+body+"</bt>";
    }
}
