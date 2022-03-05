package com.example.eatwhat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.cardview.ReviewCard;
import com.example.eatwhat.cardview.ReviewHistoryCard;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private ArrayList<ReviewCard> reviewCardArrayList;

    public ReviewAdapter(Context context, ArrayList<ReviewCard> reviewCardArrayList) {
        this.reviewCardArrayList = reviewCardArrayList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewText, reviewAuthor, reviewTime;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.review_text);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewTime = itemView.findViewById(R.id.review_time);
        }
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card_itm, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        ReviewAdapter.ReviewViewHolder myViewHolder = new ReviewAdapter.ReviewViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewCard model = reviewCardArrayList.get(position);
        holder.reviewAuthor.setText(model.getReviewAuthor());
        holder.reviewText.setText(model.getReviewText());
        holder.reviewTime.setText(model.getReviewTime());
    }

    @Override
    public int getItemCount() {
        return reviewCardArrayList.size();
    }

}
