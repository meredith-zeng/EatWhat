package com.example.eatwhat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.PostDetailActivity;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.cardview.RestaurantCard;

import java.util.ArrayList;

public class RestaurantAdapter  extends RecyclerView.Adapter<RestaurantAdapter.Viewholder> {

    private Context context;
    private ArrayList<RestaurantCard> restaurantCardArrayList;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";


    // Constructor
    public RestaurantAdapter(Context context, ArrayList<RestaurantCard> restaurantCardArrayList) {
        this.context = context;
        this.restaurantCardArrayList = restaurantCardArrayList;
    }

    @NonNull
    @Override
    public RestaurantAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_cardview_item, parent, false);
        return new RestaurantAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.Viewholder holder, int position) {
        RestaurantCard model = restaurantCardArrayList.get(position);
        holder.restaurantContent.setText(model.getContent());
        holder.restaurantTitle.setText(model.getTitle());
        holder.isCollect.setImageResource(R.drawable.ic_baseline_favorite_24);

        GlideUrl glideUrl = new GlideUrl(model.getRestaurantImageUrl(), new LazyHeaders.Builder()
                .addHeader("Authorization", " Bearer " + TOKEN)
                .build());

        Glide.with(this.context).load(glideUrl).into(holder.restaurantImage);

    }

    @Override
    public int getItemCount() {
        return restaurantCardArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView restaurantImage, isCollect;
        private TextView restaurantTitle, restaurantContent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantTitle = itemView.findViewById(R.id.restaurant_title);
            restaurantContent = itemView.findViewById(R.id.restaurant_content);
            isCollect = itemView.findViewById(R.id.is_collect);
        }


    }
}
