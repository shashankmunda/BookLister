package com.example.booklister;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class QuerySearch {

    private QuerySearch(){ //Private constructor for the helper class so that it cannot be instantiated
    }

    private static List<Book> extractDataFromJson(String jsonResults) {
        if(TextUtils.isEmpty(jsonResults)) return null;
        List<Book> books=new ArrayList<>();
        try{
            JSONObject root=new JSONObject(jsonResults);
            JSONArray items=root.getJSONArray("items");
            for(int i=0;i<items.length();i++){
                JSONObject item=items.getJSONObject(i);
                JSONObject volInfo=item.getJSONObject("volumeInfo");
                String title=volInfo.getString("title");
                JSONArray author=volInfo.getJSONArray("authors");
                String authors=author.getString(0);
                for(int j=1;j<author.length();j++){
                    authors+=","+author.getString(i);
                }
                String publisher=volInfo.getString("publisher");
                JSONObject imageLink=volInfo.getJSONObject("imageLinks");
                String imageURL=imageLink.getString("thumbnail");
                Drawable bmp=getImageFromLink(imageURL);
                books.add(new Book(title, authors, publisher,bmp));
            }
        } catch (JSONException e) {
            Log.e("EXTRACT_DATA_ERROR","Problem fetching data from JSON response");
        }
        return books;
    }

    private static Drawable getImageFromLink(String imageURL)  {
        imageURL=imageURL.replace("http", "https");
        URL url=createURL(imageURL);
        Drawable bmp=null;
        try {
            HttpsURLConnection URLConnection = (HttpsURLConnection) url.openConnection();
            URLConnection.setRequestMethod("GET");
            URLConnection.setReadTimeout(10000);
            URLConnection.setConnectTimeout(25000);
            URLConnection.connect();
            if(URLConnection.getResponseCode()==HttpsURLConnection.HTTP_OK){
                InputStream inputStream=URLConnection.getInputStream();
                bmp=Drawable.createFromStream(inputStream, "BOOK cover");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return bmp;
    }

    private static String getJsonResults(URL url) throws IOException {
        String jsonResponse="";
        if(url==null) return jsonResponse;
        HttpsURLConnection urlConnection=null; //Object to handle the network operations
        InputStream inputStream=null;
        try{
            urlConnection=(HttpsURLConnection)url.openConnection(); //opening connection to the given URl
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(25000);
            urlConnection.connect(); //connecting to the given URL using the specified connection parameters
            if(urlConnection.getResponseCode()==HttpsURLConnection.HTTP_OK){ //If successful connection happens,then fetch the JSON response
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else{
                Log.e("CONNECTION ERROR","Error in connecting:"+urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e("JSON ERROR","Cannot get Json data"+urlConnection.getResponseCode());
        }
        finally {
            if(urlConnection!=null){ //close the connection to URL
                urlConnection.disconnect();
            }
            if(inputStream!=null){ //end the inputStream once we are done fetching the results
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{ //Fetching
        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br=new BufferedReader(inputStreamReader);
            String line=br.readLine();
            while(line!=null){
                output.append(line);
                line=br.readLine();
            }
        }
        return output.toString();
    }

    private static URL createURL(String queryURL) { //parses the string to create a ready-to-use URL
        URL url=null;
        try{
            url=new URL(queryURL);
        } catch (MalformedURLException e) {
            Log.e("ERROR IN LINK","Cannot parse the URL");
        }
        return url;
    }
    public static List<Book> fetchBookResults(String queryURL){
        URL url=createURL(queryURL); //create the URL from query string
        String jsonResults=null;
        try{
            jsonResults=getJsonResults(url); //fetch the JSON data from the URl
        }
        catch(IOException e){
            Log.e("ERROR FETCHING URL DATA","Unable to access data");
        }
        List<Book> books=extractDataFromJson(jsonResults);
        return books;
    }
}
