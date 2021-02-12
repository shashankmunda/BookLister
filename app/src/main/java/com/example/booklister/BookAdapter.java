package com.example.booklister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    ArrayList<Book> mBooklist;
    Context mContext;
    public BookAdapter(Context context, ArrayList<Book> books) {
        mBooklist=books;
        mContext=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating our custom list view item
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Populating our custom layout
        final Book book=mBooklist.get(position);
        holder.bookName.setText(book.getTitle());
        holder.authorName.setText(book.getAuthor());
        holder.publisherName.setText(book.getPublisher());
        holder.imageView.setImageDrawable(book.getThumbsNail());
    }

    @Override
    public int getItemCount() {
        return mBooklist.size();
    }

    public void clear() {
        int size=mBooklist.size();
        mBooklist.clear();
        notifyDataSetChanged();
       // notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Book> data) {
        int size=data.size();
        for(int i=0;i<size;i++)
        mBooklist.add(data.get(i));
        notifyItemRangeInserted(0, size);
    }

    //Defining our custom view for the Recycler View adapter
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView bookName;
        TextView authorName;
        TextView publisherName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName=itemView.findViewById(R.id.authorView);
            bookName=itemView.findViewById(R.id.bookTitle);
            publisherName=itemView.findViewById(R.id.publisherView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
