package com.example.newsapk.model;


public class News {
    private String mtitle;
    private String msection;
    private String mwebPublicationDateAndTime;
    private String mthumbnail;
    private String mAuthor;
    private String murl;

    public News(String Title, String Section, String auther, String WebPublicationDateAndTime, String Thumbnail, String Url) {
        mtitle = Title;
        msection = Section;
        mAuthor = auther;
        mwebPublicationDateAndTime = WebPublicationDateAndTime;
        mthumbnail = Thumbnail;
        murl = Url;
    }

    public String getUrl() {

        return murl;
    }

    public String getTitle() {

        return mtitle;
    }

    public String getDate() {

        return mwebPublicationDateAndTime;
    }

    public String getSection() {

        return msection;
    }


    public String getAuthor() {
        return mAuthor;
    }


    public String getThumbnail() {

        return mthumbnail;
    }


}
