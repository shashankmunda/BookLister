package com.example.booklister;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mLink;
    public BookLoader(Context context,String queryLink) {
        super(context);
        mLink=queryLink;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }
    @Override
    public List<Book> loadInBackground() {
        if(mLink==null) return null;
        return QuerySearch.fetchBookResults(mLink);
    }
}
