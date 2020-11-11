package com.example.ahmedakram.ovicnews;

public class News {
    private String title;
    private String author;
    private String section;
    private String url;
    private String date;

    public News(String title, String author, String section, String url, String date){
        this.title = title;
        this.author = author;
        this.section = section;
        this.url = url;
        this.date = date;
    }

    public String getTitle(){ return title; }
    public String getAuthor(){ return author; }
    public String getSection(){ return section; }
    public String getUrl(){ return url; }
    public String getDate(){ return date; }
}
