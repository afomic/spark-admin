package com.afomic.sparkadmin.model;

/**
 * Created by afomic on 11/4/17.
 */

public class BulletListTextElement implements BlogElement {
    public String body;
    public BulletListTextElement(){
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
        return Type.BULLET_LIST_TEXT;
    }

    @Override
    public String toHtml() {
        return "<bl>"+body+"</bl>";
    }
}
