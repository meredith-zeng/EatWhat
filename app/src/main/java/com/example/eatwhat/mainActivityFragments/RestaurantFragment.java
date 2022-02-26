package com.example.eatwhat.mainActivityFragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;

import java.util.ArrayList;

//import butterknife.BindView;
//import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantFragment extends Fragment  {

    int count = 0, limit = 10;
    private ArrayList<String> categoryList;
    private ArrayList<String> sortConditionList;
    ArrayList<RestaurantCard> restaurantCardArrayList = new ArrayList<>();

    private String selectedCategory = null;
    private String selectedSortCondition = null;

    private RecyclerView recyclerView;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        createSpinners(view, container);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurant_recyclerview);
        initData();
        pullUpToRefresh(view);
        return view;
    }

    private void pullUpToRefresh(View rootView) {
        NestedScrollView nestedSV = (NestedScrollView) rootView.findViewById(R.id.swipe_container);
        ProgressBar loadingPB = (ProgressBar)rootView.findViewById(R.id.pb_loading);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    count += limit;
                    // on below line we are making our progress bar visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    if (count < 200) {
                        // on below line we are again calling
                        // a method to load data in our array list.
                        initData();
                    }
                }
            }
        });
    }

    private void createSpinners(View view, ViewGroup container) {
        String [] categoryArray = {"Chinese", "American", "Italian", "French", "Korean", "Japanese"};
        categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArray.length; i++) {
            categoryList.add(categoryArray[i]);
        }

        String [] sortConditionArray = { "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
                "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
                "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
                "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "ennessee", "Texas", "Utah",
                "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        sortConditionList = new ArrayList<>();
        for (int i = 0; i < sortConditionArray.length; i++) {
            sortConditionList.add(sortConditionArray[i]);
        }

        TextView categoryView = view.findViewById(R.id.selectCategoryView);
        TextView statesView = view.findViewById(R.id.selectStateView);

        createSpinnerDialog(categoryView, categoryList, "category");
        createSpinnerDialog(statesView, sortConditionList, "sort");

    }

    private void createSpinnerDialog(TextView textview, ArrayList<String> list, String type) {
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.restaurant_spinner_search);
                dialog.getWindow().setLayout(800, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        textview.setText(adapter.getItem(position));
                        switch (type) {
                            case "category":
                                selectedCategory = adapter.getItem(position);
                                Log.e("choose category", selectedCategory);
                                if (selectedCategory != null && selectedSortCondition != null) {
                                    restaurantCardArrayList.clear();
                                    initData();
                                }
                                break;
                            case "sort":
                                selectedSortCondition = adapter.getItem(position);
                                Log.e("choose location", selectedSortCondition);
                                if (selectedCategory != null && selectedSortCondition != null) {
                                    restaurantCardArrayList.clear();
                                    initData();
                                }
                                break;
                        }

                        dialog.dismiss();
                    }
                });
            }
        });
    }



    private void initData(){
        RetrofitClient retrofitClient = new RetrofitClient();
        RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);

        System.out.println("count" + count);
        Call<Restaurant> call = methods.queryRestaurantByCategory("Santa Clara", selectedCategory,  2, count);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.code() == 200){
                    for (Business business: response.body().getBusinesses()){
                        RestaurantCard restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().toString(), false);
                        restaurantCardArrayList.add(restaurantCard);
                    }
                    initRecycleView(restaurantCardArrayList);
                }

            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });
    }

    private void initRecycleView(ArrayList<RestaurantCard> restaurantCardArrayList) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getActivity(), restaurantCardArrayList);
            restaurantAdapter.setRecyclerViewOnItemClickListener(new RestaurantAdapter.RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    Intent intent = new Intent(getActivity(), RestaurantPageActivity.class);
                    intent.putExtra("title", restaurantCardArrayList.get(position).getTitle());
                    intent.putExtra("content", restaurantCardArrayList.get(position).getContent());
                    intent.putExtra("imageUrl", restaurantCardArrayList.get(position).getRestaurantImageUrl());
                    getContext().startActivity(intent);
                }
            });

            recyclerView.setAdapter(restaurantAdapter);
    }

}