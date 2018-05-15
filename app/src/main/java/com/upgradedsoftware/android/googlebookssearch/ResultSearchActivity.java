package com.upgradedsoftware.android.googlebookssearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultSearchActivity extends AppCompatActivity {

    private TextView mTextView;
    private String bookImageUrl;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_search_activity);

        Intent intent = getIntent();
        String isbnString = intent.getExtras().getString("ISBN");

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(isbnString);

        DownloadTask task = new DownloadTask();
        task.execute("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbnString);
    }
}
