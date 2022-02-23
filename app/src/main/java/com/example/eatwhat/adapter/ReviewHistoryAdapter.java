package com.example.eatwhat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.ReviewHistoryCard;
import java.util.ArrayList;

public class ReviewHistoryAdapter extends RecyclerView.Adapter<ReviewHistoryAdapter.ReviewHistoryViewHolder>{

    private ArrayList<ReviewHistoryCard> reviewHistoryCardArrayList;

    public ReviewHistoryAdapter(Context context, ArrayList<ReviewHistoryCard> reviewHistoryCardArrayList) {
        this.reviewHistoryCardArrayList = reviewHistoryCardArrayList;
    }


    public static class ReviewHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantTitle, restaurantContent;

        public ReviewHistoryViewHolder(View itemView) {
            super(itemView);
            restaurantTitle = itemView.findViewById(R.id.restaurant_title);
            restaurantContent = itemView.findViewById(R.id.restaurant_content);
        }
    }

    @NonNull
    @Override
    public ReviewHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_history_card_item, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        ReviewHistoryViewHolder myViewHolder = new ReviewHistoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHistoryViewHolder holder, int position) {
        ReviewHistoryCard model = reviewHistoryCardArrayList.get(position);
        holder.restaurantContent.setText(model.getContent());
        holder.restaurantTitle.setText(model.getTitle());
    }


    @Override
    public int getItemCount() {
        return reviewHistoryCardArrayList.size();
    }
}
