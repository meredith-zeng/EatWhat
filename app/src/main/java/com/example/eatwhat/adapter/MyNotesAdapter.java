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
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.MyNotesCard;
import com.example.eatwhat.cardview.PostCard;

import java.util.ArrayList;

public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.Viewholder> {

    private Context context;
    private ArrayList<MyNotesCard> myNotesArrayList;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";

    // Constructor
    public MyNotesAdapter(Context context, ArrayList<MyNotesCard> myNotesArrayList) {
        this.context = context;
        this.myNotesArrayList = myNotesArrayList;
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView myNotesImage;
        private TextView myNotesTitle, myNotesContent,myNotesNumberOfLike;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            myNotesImage = itemView.findViewById(R.id.myNotesImage);
            myNotesTitle = itemView.findViewById(R.id.myNotesTitle);
            myNotesContent = itemView.findViewById(R.id.myNotesContent);
            myNotesNumberOfLike = itemView.findViewById(R.id.myNotesNumberOfLike);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotesAdapter.Viewholder holder, int position) {
        MyNotesCard model = myNotesArrayList.get(position);
        holder.myNotesTitle.setText(model.getMyNoteTitle());
        holder.myNotesContent.setText(model.getMyNoteContent());
        holder.myNotesNumberOfLike.setText(String.valueOf(model.getMyNoteNumberOfLikes()));
    }

    @NonNull
    @Override
    public MyNotesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_notes_card_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return myNotesArrayList.size();
    }

}
