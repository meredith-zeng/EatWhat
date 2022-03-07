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
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder>  implements View.OnClickListener{

    private Context context;
    private ArrayList<PostCard> postCardArrayList;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";
    private RecyclerViewOnItemClickListener onItemClickListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    // Constructor
    public PostAdapter(Context context, ArrayList<PostCard> postCardArrayList) {
        this.context = context;
        this.postCardArrayList = postCardArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.using_post_card, parent, false);
        PostAdapter.Viewholder vh = new PostAdapter.Viewholder(root);
        root.setOnClickListener(this);
        return new PostAdapter.Viewholder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull PostAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PostCard model = postCardArrayList.get(position);
        holder.postTitleV.setText(model.getPost_title());
        holder.postContentV.setText(model.getPost_content());
        if (model.getLikedUidList() == null){
            holder.numberOfLikeV.setText("0");
        }else {
            holder.numberOfLikeV.setText("" + model.getLikedUidList().size() +  "people liked");
        }


        String imageUrl = model.getPost_image_url();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        Glide.with(this.context)
                .load(gsReference)
//                .bitmapTransform(new RoundedCornersTransformation(this, 100, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(holder.postImageV);


        holder.root.setTag(position);

    }


    @Override
    public int getItemCount() {
        return postCardArrayList.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            // Use getTag() to get data
            onItemClickListener.onItemClickListener(view, (Integer) view.getTag());
        }
    }
    public void setRecyclerViewOnItemClickListener(PostAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }
    public class Viewholder extends RecyclerView.ViewHolder{
        private ImageView postImageV;
        private TextView postTitleV, postContentV, numberOfLikeV;
        private View root;

        public Viewholder(@NonNull View root) {
            super(root);
            this.root = root;
            postImageV = root.findViewById(R.id.idPostImage);
            postTitleV = root.findViewById(R.id.idPostTitle);
            postContentV = root.findViewById(R.id.idPostContent);
            numberOfLikeV = root.findViewById(R.id.numberOfLike);

        }

    }
}
