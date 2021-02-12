package com.example.booklister;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
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

    protected Book(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
        mPublisher = in.readString();
        mThumbsNail=(Drawable)in.readValue(Drawable.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mPublisher);
        dest.writeValue(mThumbsNail);
    }
}
