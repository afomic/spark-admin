package com.afomic.sparkadmin.model;

/**
 * Created by afomic on 26-Oct-16.
 */
public class Constitution {
    private int article,section;
    private String content,title;


    public Constitution(){

    }

    public Constitution(int article, int section,String title, String content) {
        this.article = article;
        this.section = section;
        this.content = content;
        this.title=title;
    }


    public int getSection() {
        return section;
    }
    public String getTitle(){
        return title;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
