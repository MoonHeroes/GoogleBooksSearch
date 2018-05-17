package com.upgradedsoftware.android.googlebookssearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.upgradedsoftware.android.googlebookssearch.ResultSearchRecyclerView.EXTRA_AUTHOR;
import static com.upgradedsoftware.android.googlebookssearch.ResultSearchRecyclerView.EXTRA_DATE;
import static com.upgradedsoftware.android.googlebookssearch.ResultSearchRecyclerView.EXTRA_TITLE;
import static com.upgradedsoftware.android.googlebookssearch.ResultSearchRecyclerView.EXTRA_URL;

public class DetailBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_book_view);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String titleName = intent.getStringExtra(EXTRA_AUTHOR);
        String authorName = intent.getStringExtra(EXTRA_TITLE);
        String dateName = intent.getStringExtra(EXTRA_DATE);

        TextView titleText = findViewById(R.id.book_title);
        TextView authorText = findViewById(R.id.book_authors);
        TextView dateText = findViewById(R.id.book_date);
        ImageView bookImage = findViewById(R.id.imageView);

        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(bookImage);
        titleText.setText(titleName);
        authorText.setText(authorName);
        dateText.setText(dateName);
    }
}
