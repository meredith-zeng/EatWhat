package com.example.eatwhat.mainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PostCard> postCardArrayList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference likereference;
    private Boolean testclick = false;
    public static final String TAG = "NotesFragments";

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        postCardArrayList = new ArrayList<>();



        recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        likereference=FirebaseDatabase.getInstance().getReference("likes");

//        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    DataSnapshot dataSnapshot = task.getResult();
//                    initRecyclerView(dataSnapshot);
//                }
//            }
//        });
        FirebaseRecyclerOptions<PostCard> options =
                new FirebaseRecyclerOptions.Builder<PostCard>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts"), PostCard.class)
                        .build();


        FirebaseRecyclerAdapter<PostCard,PostViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PostCard, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostCard model) {

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                final String userid = firebaseUser.getUid();
                final String postId = getRef(position).getKey();

                holder.getlikebuttonstatus(postId,userid);

                holder.postTitleV.setText(model.getPost_title());
                holder.postContentV.setText(model.getPost_content());
                holder.like_text.setText("" + model.getNumber_of_likes());
                String imageUrl = model.getPost_image_url();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
                Glide.with(getActivity())
                        .load(gsReference)
                        .into(holder.postImageV);

                holder.like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        testclick = true;

                        likereference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                System.out.println(snapshot.getValue().toString());
                                if(testclick==true)
                                {
                                    if(snapshot.child(postId).hasChild(userid))
                                    {
                                        likereference.child(postId).child(userid).removeValue();
                                        testclick=false;
                                    }
                                    else
                                    {
                                        likereference.child(postId).child(userid).setValue(true);
                                        testclick=false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.using_post_card,parent,false);
                return new PostViewHolder(view);
            }


        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }


//    private void initRecyclerView(DataSnapshot dataSnapshot){
//
//        // 2. set layoutManger
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//            PostCard postCard = singleSnapshot.getValue(PostCard.class);
//            postCardArrayList.add(postCard);
//        }
//        // 3. create an adapter
//        PostAdapter postAdapter = new PostAdapter(getActivity(), postCardArrayList);
//        postAdapter.setRecyclerViewOnItemClickListener(new PostAdapter.RecyclerViewOnItemClickListener() {
//            @Override
//            public void onItemClickListener(View view, int position) {
//                Log.e(TAG, "click one post");
//                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                intent.putExtra("postId", postCardArrayList.get(position).getPostId());
//                intent.putExtra("imageUrl", postCardArrayList.get(position).getPost_image_url());
//                getContext().startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(postAdapter);
//    }


}