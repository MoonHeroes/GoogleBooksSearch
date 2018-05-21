package com.upgradedsoftware.android.googlebookssearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultSearchRecyclerView extends AppCompatActivity implements BookAdapter.OnItemClickListener{

    public static final String EXTRA_URL="imageUrl";
    public static final String EXTRA_AUTHOR="authorName";
    public static final String EXTRA_TITLE="titleName";
    public static final String EXTRA_DATE="dateName";


    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private ArrayList<ItemBook> mItemBooks;
    private RequestQueue mRequestQueue;
    private String bookImageUrl;
    private String searchString;

    @Override
    public void onItemClick(int position) {
        Intent detaiIntent = new Intent(this, DetailBookActivity.class);
        ItemBook clickedItem = mItemBooks.get(position);

        detaiIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detaiIntent.putExtra(EXTRA_AUTHOR, clickedItem.getAuthor());
        detaiIntent.putExtra(EXTRA_TITLE, clickedItem.getTitle());
        detaiIntent.putExtra(EXTRA_DATE, clickedItem.getPublishedDate());

        startActivity(detaiIntent);

    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String strName = "111";
            try {
                JSONObject jsonObject = new JSONObject(s);

                //String bookInfo = jsonObject.getString("items");

                JSONArray arr = jsonObject.getJSONArray("items");
                for (int i = 0; i < arr.length(); i++){
                    JSONObject volumeInfo = arr.getJSONObject(i).getJSONObject("volumeInfo");
                    strName = volumeInfo.getString("title");

                    if(arr.getJSONObject(i).getJSONObject("volumeInfo").has("imageLinks")){
                        bookImageUrl = arr.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");

                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, bookImageUrl, Toast.LENGTH_SHORT);
                        toast.show();

                        // new DownloadImageTask((ImageView) findViewById(R.id.bookImageView)).execute(bookImageUrl);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImage;

        public DownloadImageTask(ImageView bmImage) {
            this.mImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mImage.setImageBitmap(result);
        }
    }









    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_search_recycler_view);

        Intent intent = getIntent();
        searchString = intent.getExtras().getString("URL");

       mRecyclerView = findViewById(R.id.recycler_view);
       mRecyclerView.setHasFixedSize(true);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       mItemBooks = new ArrayList<>();

       mRequestQueue = Volley.newRequestQueue(this);
       parseJSON();
    }

    private void parseJSON(){
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + searchString;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try{
                    JSONArray arr = response.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++){
                        JSONObject volumeInfo = arr.getJSONObject(i).getJSONObject("volumeInfo");
                        String title = "";
                        String publishedDate = "";
                        String author = "";
                        String imageUrl = "";

                        if(arr.getJSONObject(i).getJSONObject("volumeInfo").has("title")){
                            title = volumeInfo.getString("title");
                        }

                        if(arr.getJSONObject(i).getJSONObject("volumeInfo").has("authors")){
                            JSONObject authorsobject = arr.getJSONObject(i).getJSONObject("volumeInfo");
                            JSONArray authors = authorsobject.getJSONArray("authors");
                            if (authors.length() != 1) {
                                for (int j = 0; j < authors.length(); j++) {
                                    author = author + authors.getString(j) + "  ";
                                }
                            }
                            else{
                                author = authors.getString(0);
                            }
                        }

                        if(arr.getJSONObject(i).getJSONObject("volumeInfo").has("publishedDate")){
                            publishedDate = volumeInfo.getString("publishedDate");
                        }

                        if(arr.getJSONObject(i).getJSONObject("volumeInfo").has("imageLinks")){
                            imageUrl = arr.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
                        }

                        mItemBooks.add(new ItemBook(imageUrl,author,title,publishedDate));
                    }

                    mBookAdapter = new BookAdapter(ResultSearchRecyclerView.this, mItemBooks);
                    mRecyclerView.setAdapter(mBookAdapter);
                    mBookAdapter.setOnItemClickListener(ResultSearchRecyclerView.this);

                } catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

}
