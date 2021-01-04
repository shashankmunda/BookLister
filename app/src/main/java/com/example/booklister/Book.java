package com.example.booklister;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private Drawable mThumbsNail;

    public Book(String title, String author, String publisher, Drawable thumbsNail){
        this.mTitle=title;
        this.mAuthor=author;
        this.mPublisher=publisher;
        this.mThumbsNail=thumbsNail;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getAuthor(){
        return mAuthor;
    }
    public String getPublisher(){
        return mPublisher;
    }
    public Drawable getThumbsNail(){ return mThumbsNail;}
}
