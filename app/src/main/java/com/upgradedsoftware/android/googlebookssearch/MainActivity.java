package com.upgradedsoftware.android.googlebookssearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private EditText mIsbnTextView;
    private Button mSearchButton;
    private String urlString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIsbnTextView = (EditText) findViewById(R.id.isbnText);
        mIsbnTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                urlString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ResultSearchRecyclerView.class);
                intent.putExtra("URL", urlString);
                startActivity(intent);
            }
        });




    }
}