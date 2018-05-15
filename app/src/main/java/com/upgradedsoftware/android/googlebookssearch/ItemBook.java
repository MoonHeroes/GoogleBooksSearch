package com.upgradedsoftware.android.googlebookssearch;

public class ItemBook {
    private String mImageUrl;
    private String mAuthor;
    private String mTitle;
    private String mPublishedDate;

    public ItemBook(String imageUrl, String author, String title, String publishedDate){
        mImageUrl = imageUrl;
        mAuthor = author;
        mTitle = title;
        mPublishedDate = publishedDate;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getPublishedDate(){
        return mPublishedDate;
    }
}
