package com.example.booklister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    private ProgressBar mProgressBar;
    private BookAdapter mAdapter;
    private ConnectivityManager mConnectivityManager;
    private EditText mTextView;
    private static final int BOOKER_LOADER_ID=1;
    private static String QUERY_URL="https://www.googleapis.com/books/v1/volumes";
    private TextView mEmptyView;
    private LoaderManager loaderManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // mProgressBar.setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderManager=getLoaderManager();
        mTextView=findViewById(R.id.mTextBox);
        mProgressBar=findViewById(R.id.mBar);
        mProgressBar.setVisibility(View.GONE);
        mConnectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ListView booksList=findViewById(R.id.booksView);
        mAdapter=new BookAdapter(this, new ArrayList<Book>());
        booksList.setAdapter(mAdapter);
        mEmptyView=findViewById(R.id.EmptyView);
    }

    public void onClick(View v) {
        NetworkInfo networkInfo=mConnectivityManager.getActiveNetworkInfo();
        if(TextUtils.isEmpty(mTextView.getText())){
            mTextView.setError("Empty search query");
        }
        else {
            if (networkInfo != null && networkInfo.isConnected()) {
                hideKeyboard(this);
                mEmptyView.setText("Getting results");
                mProgressBar.setVisibility(View.VISIBLE);
                loaderManager.initLoader(1, null, this);
                if(mAdapter!=null)
                    loaderManager.restartLoader(1,null,this);
                //mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("No Connection.Cannot retrieve query results");
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view=activity.getCurrentFocus();
        if(view==null){
            view=new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String query=mTextView.getText().toString();
        Uri baseUri=Uri.parse(QUERY_URL);
        Uri.Builder queryUrl=baseUri.buildUpon();
        queryUrl.appendQueryParameter("q", query);
        queryUrl.appendQueryParameter("maxResults", "10");
        return new BookLoader(this,queryUrl.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
       mProgressBar.setVisibility(View.GONE);
        mEmptyView.setText("Cannot find your results");
        mAdapter.clear();
        if(data!=null && !data.isEmpty()) {
            mAdapter.addAll(data);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        String query=mTextView.getText().toString();
        Uri baseUri=Uri.parse(QUERY_URL);
        Uri.Builder queryUrl=baseUri.buildUpon();
        queryUrl.appendQueryParameter("q", query);
        queryUrl.appendQueryParameter("maxResults", "10");
         new BookLoader(this,queryUrl.toString());
    }
}
