package com.example.booklister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context,0,books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View bookView=convertView;
        if(bookView==null){
            bookView= LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent,false);
        }
        Book currBook=getItem(position);
        TextView authorView=bookView.findViewById(R.id.authorView);
        TextView titleView=bookView.findViewById(R.id.bookTitle);
        TextView publishView=bookView.findViewById(R.id.publisherView);
        ImageView imgView=bookView.findViewById(R.id.imageView);
        //TextView priceView=bookView.findViewById(R.id.priceView);

        titleView.setText(currBook.getTitle());
        authorView.setText(currBook.getAuthor());
        publishView.setText(currBook.getPublisher());
        imgView.setImageDrawable(currBook.getThumbsNail());
        return bookView;
    }
}
