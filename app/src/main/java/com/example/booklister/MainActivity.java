package com.example.booklister;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {


    private static final String SAVED_LIST ="saved_list_items" ;
    private ProgressBar mProgressBar;
    private BookAdapter mAdapter;
    private ConnectivityManager mConnectivityManager;
    private EditText mTextView;
    private static final int BOOKER_LOADER_ID=1;
    private static String QUERY_URL="https://www.googleapis.com/books/v1/volumes";
    private TextView mEmptyView;
    private LoaderManager loaderManager;
    ArrayList<Book> books;
    boolean firstLoad;
    private static String SEARCH_TEXT="search text";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firstLoad=false;
       // mProgressBar.setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_main);
        loaderManager=getLoaderManager();
        mTextView=findViewById(R.id.mTextBox);
        mProgressBar=findViewById(R.id.mBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mConnectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        RecyclerView booksList=findViewById(R.id.booksView);
        booksList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if(savedInstanceState!=null) {
            mTextView.setText(savedInstanceState.getString(SEARCH_TEXT));
            List<Book> myBooks=savedInstanceState.getParcelableArrayList(SAVED_LIST);
            mAdapter=new BookAdapter(this,(ArrayList<Book>)myBooks);
        }
        else
        mAdapter=new BookAdapter(this, new ArrayList<Book>());
        booksList.setAdapter(mAdapter);
        mEmptyView=findViewById(R.id.EmptyView);
    }
    private  void hideActionBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }
    public void onClick(View v) {
        NetworkInfo networkInfo=mConnectivityManager.getActiveNetworkInfo();
        if(TextUtils.isEmpty(mTextView.getText())){
            mTextView.setError("Empty search query");
        }
        else {
            if (networkInfo != null && networkInfo.isConnected()) {
                hideKeyboard(this);
               mAdapter.clear();
              mAdapter.notifyDataSetChanged();
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("Getting results");
                mProgressBar.setVisibility(View.VISIBLE);
                loaderManager.initLoader(BOOKER_LOADER_ID, null, this);
                if(firstLoad)
                    loaderManager.restartLoader(BOOKER_LOADER_ID,null,this);
                //mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("No Connection.Cannot retrieve query results");
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm=(InputMethodManager)activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view=activity.getCurrentFocus();
        if(view==null){
            view=new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        firstLoad=true;
        String query=mTextView.getText().toString();
        Uri baseUri=Uri.parse(QUERY_URL);
        Uri.Builder queryUrl=baseUri.buildUpon();
        queryUrl.appendQueryParameter("q", query);
        queryUrl.appendQueryParameter("maxResults", "20");
        return new BookLoader(this,queryUrl.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
       mProgressBar.setVisibility(View.INVISIBLE);
       mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText("Cannot find your results");
        mAdapter.clear();
        if(data!=null && !data.isEmpty()) {
            books=(ArrayList<Book>)data;
            mAdapter.addAll(data);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
    @Override
    protected void onSaveInstanceState(Bundle  bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString(SEARCH_TEXT,mTextView.getText().toString());
        bundle.putParcelableArrayList(SAVED_LIST, books);
        //bundle.putParcelableArrayList(SAVED_LIST, (ArrayList<? extends Parcelable>) books);
    }
}
