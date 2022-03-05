package com.example.eatwhat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatwhat.R;
import com.example.eatwhat.activity.post.PostCreationActivity;
import com.example.eatwhat.activity.post.PostDetailActivity;
import com.example.eatwhat.cardview.PostCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.Viewholder> implements View.OnClickListener{

    private Context context;
    private ArrayList<PostCard> myNotesArrayList;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";
    private RecyclerViewOnItemClickListener onItemClickListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // Constructor
    public MyNotesAdapter(Context context, ArrayList<PostCard> myNotesArrayList) {
        this.context = context;
        this.myNotesArrayList = myNotesArrayList;
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            // Use getTag() to get data
            onItemClickListener.onItemClickListener(view, (Integer) view.getTag());
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView myNotesImage;
        private TextView myNotesTitle, myNotesContent,myNotesNumberOfLike;
        private View root;
        private Button editbutton;
        MyClickListener listener;

        public Viewholder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            this.root = itemView;
            myNotesImage = itemView.findViewById(R.id.myNotesImage);
            myNotesTitle = itemView.findViewById(R.id.myNotesTitle);
            myNotesContent = itemView.findViewById(R.id.myNotesContent);
            myNotesNumberOfLike = itemView.findViewById(R.id.myNotesNumberOfLike);
            editbutton = itemView.findViewById(R.id.edit_post);
            editbutton.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onEdit(this.getLayoutPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        PostCard model = myNotesArrayList.get(position);
        holder.myNotesTitle.setText(model.getPost_title());
        holder.myNotesContent.setText(model.getPost_content());
        holder.myNotesNumberOfLike.setText("" + model.getNumber_of_likes());
        String imageUrl = model.getPost_image_url();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        Glide.with(this.context)
                .load(gsReference)
                .into(holder.myNotesImage);

        holder.root.setTag(position);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_notes_card_item, parent, false);
        MyNotesAdapter.Viewholder vh = new MyNotesAdapter.Viewholder(view, new MyClickListener() {
            @Override
            public void onEdit(int p) {
                Intent intent = new Intent(view.getContext(), PostCreationActivity.class);
                intent.putExtra("postId", myNotesArrayList.get(p).getPostId());
                intent.putExtra("imageUrl", myNotesArrayList.get(p).getPost_image_url());
                context.startActivity(intent);
            }
        });
        view.setOnClickListener(this);
//        Button editButton = (Button) view.findViewById(R.id.edit_my_post_button);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PostCreationActivity.class);
//                intent.putExtra("postId", myNotesArrayList.get(vh.getAdapterPosition()).getPostId());
//                intent.putExtra("imageUrl", myNotesArrayList.get(vh.getAdapterPosition()).getPost_image_url());
//                context.startActivity(intent);
//            }
//        });
        return vh;
    }

    @Override
    public int getItemCount() {
        return myNotesArrayList.size();
    }

    public void setRecyclerViewOnItemClickListener(MyNotesAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface RecyclerViewOnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public interface MyClickListener {
        void onEdit(int p);
    }
}
