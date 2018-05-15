package com.upgradedsoftware.android.googlebookssearch;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context mContext;
    private ArrayList<ItemBook> mItemBooks;

    public BookAdapter(Context context, ArrayList<ItemBook> itemBooks){
        mContext = context;
        mItemBooks = itemBooks;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        return  new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        ItemBook currentBook = mItemBooks.get(position);

        String imageUrl = currentBook.getImageUrl();
        String author = currentBook.getAuthor();
        String title = currentBook.getTitle();
        String datePublished = currentBook.getPublishedDate();

        holder.mTextViewTitle.setText(title);
        holder.mTextViewAuthor.setText(author);
        holder.mTextViewPublishedDate.setText(datePublished);
    }

    @Override
    public int getItemCount() {
        return mItemBooks.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageViewBook;
        public TextView mTextViewAuthor;
        public TextView mTextViewTitle;
        public TextView mTextViewPublishedDate;

        public BookViewHolder(View itemView){
            super(itemView);
            mImageViewBook = itemView.findViewById(R.id.book_image_view);
            mTextViewAuthor = itemView.findViewById(R.id.text_view_authors);
            mTextViewPublishedDate = itemView.findViewById(R.id.text_view_published_date);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
        }

    }
}
